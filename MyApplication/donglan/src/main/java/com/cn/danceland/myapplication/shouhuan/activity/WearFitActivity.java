package com.cn.danceland.myapplication.shouhuan.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.db.HeartRate;
import com.cn.danceland.myapplication.db.HeartRateHelper;
import com.cn.danceland.myapplication.shouhuan.bean.HeartRateLastBean;
import com.cn.danceland.myapplication.shouhuan.bean.HeartRateResultBean;
import com.cn.danceland.myapplication.shouhuan.command.CommandManager;
import com.cn.danceland.myapplication.shouhuan.service.BluetoothLeService;
import com.cn.danceland.myapplication.shouhuan.utils.DataHandlerUtils;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.StringUtils;
import com.cn.danceland.myapplication.utils.TimeUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.cn.danceland.myapplication.view.DongLanTitleView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 手环首页
 * Created by feng on 2018/6/19.
 */

public class WearFitActivity extends Activity {
    private static final int REQUEST_SEARCH = 1;
    private static final int REQUEST_SETTING = 3;
    private TextView tv_connect;
    private String address;
    private String name;
    private GridView gv_wearfit;
    private DongLanTitleView shouhuan_title;
    private int[] imgID = {R.drawable.shouhuan_heart, R.drawable.shouhuan_sleep, R.drawable.shouhuan_pilao, R.drawable.shouhuan_photo, R.drawable.shouhuan_plan, R.drawable.shouhuan_find
            , R.drawable.shouhuan_setting};
    private String[] text1s = {"心率", "睡眠", "疲劳", "摇摇拍照", "健身计划", "查找手环", "设置"};
    private String[] text2s = {"--bpm", "-时-分", "--", "", "", "", ""};
    private ArrayList<ItemBean> itemBeans;
    private ProgressDialog progressDialog;
    private String address1;//手环默认链接地址  yxx
    private RelativeLayout rl_connect;
    private CommandManager commandManager;

    HeartRateHelper heartRateHelper = new HeartRateHelper();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wearfit);
        initHost();
        initView();
    }

    private void initHost() {
        commandManager = CommandManager.getInstance(this);
        progressDialog = new ProgressDialog(this);
        itemBeans = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            ItemBean itemBean = new ItemBean();
            itemBean.img_url = imgID[i];
            itemBean.text1 = text1s[i];
            itemBean.text2 = text2s[i];
            itemBeans.add(itemBean);
        }
        address1 = SPUtils.getString(Constants.ADDRESS, "");
    }

    private void initView() {
        rl_connect = findViewById(R.id.rl_connect);
        gv_wearfit = findViewById(R.id.gv_wearfit);
        gv_wearfit.setAdapter(new WearFitAdapter());
        shouhuan_title = findViewById(R.id.shouhuan_title);
        shouhuan_title.setTitle("我的手环");
        tv_connect = findViewById(R.id.tv_connect);
        setListener();
        if (!MyApplication.mBluetoothConnected && !StringUtils.isNullorEmpty(address1)) {
            try {
                if (MyApplication.mBluetoothLeService.connect(address1)) {
                    //tv_status.setText(name+"--"+address);
                    //ToastUtils.showToastShort("正在连接...");
                    progressDialog.setMessage("正在连接...");
                    progressDialog.show();
                } else {
                    ToastUtils.showToastShort("连接失败");
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            MyApplication.isBluetoothConnecting = true;
            invalidateOptionsMenu();

        } else {
            rl_connect.setVisibility(View.VISIBLE);
            tv_connect.setText("还未绑定手环，点击绑定");
        }
        initHeartData();
    }

    private class WearFitAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return itemBeans.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View inflate = View.inflate(WearFitActivity.this, R.layout.shouhuan_gv_item, null);
            ImageView img = inflate.findViewById(R.id.img);
            TextView text1 = inflate.findViewById(R.id.tv_text1);
            TextView text2 = inflate.findViewById(R.id.tv_text2);
            Glide.with(WearFitActivity.this).load(itemBeans.get(i).img_url).into(img);
            text1.setText(itemBeans.get(i).text1);
            text2.setText(itemBeans.get(i).text2);
            return inflate;
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (MyApplication.mBluetoothConnected) {
            rl_connect.setVisibility(View.GONE);
        } else {
            rl_connect.setVisibility(View.VISIBLE);
            tv_connect.setText("还未绑定手环，点击绑定");
        }
    }

    private void setListener() {

        tv_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WearFitActivity.this, WearFitEquipmentActivity.class);
                startActivity(intent);
            }
        });

        gv_wearfit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0://心率
                        startActivity(new Intent(WearFitActivity.this, WearFitHeartRateActivity.class));
                        //   commandManager.realTimeAndOnceMeasure(0x80, 1);
//                        commandManager.setSyncData(System.currentTimeMillis() - 15 * 24 * 60 * 60 * 1000, System.currentTimeMillis() - 15 * 24 * 60 * 60 * 1000);

                        break;
                    case 1://睡眠
                        startActivity(new Intent(WearFitActivity.this, WearFitSleepActivity.class));
                        break;
                    case 2://疲劳
                        break;
                    case 3://摇摇拍照
                        startActivity(new Intent(WearFitActivity.this, WearFitCameraActivity.class));
                        break;
                    case 4://健身计划
                        break;
                    case 5://查找手环
                        startActivityForResult(new Intent(WearFitActivity.this, DeviceScanActivity.class), REQUEST_SEARCH);
                        break;
                    case 6://设置
                        startActivity(new Intent(WearFitActivity.this, WearFitSettingActivity.class));
                        break;

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SEARCH && resultCode == RESULT_OK) {
            address = data.getStringExtra(Constants.ADDRESS);
            name = data.getStringExtra(Constants.NAME);
            SPUtils.setString(Constants.ADDRESS, address);
            SPUtils.setString(Constants.NAME, name);
            if (!TextUtils.isEmpty(address)) {

                try {
                    if (MyApplication.mBluetoothLeService.connect(address)) {
                        //tv_status.setText(name+"--"+address);
                        //ToastUtils.showToastShort("正在连接...");
                        progressDialog.setMessage("正在连接...");
                        progressDialog.show();
                    } else {
                        ToastUtils.showToastShort("连接失败");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                MyApplication.isBluetoothConnecting = true;
                invalidateOptionsMenu();//显示正在连接 ...
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    private IntentFilter makeGattUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    //接收蓝牙状态改变的广播
    private BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                initHeartData();//请求心率数据

                MyApplication.mBluetoothConnected = true;
                MyApplication.isBluetoothConnecting = false;
                //todo 更改界面ui
                invalidateOptionsMenu();//更新菜单栏
                ToastUtils.showToastShort("连接成功");
                rl_connect.setVisibility(View.GONE);
                progressDialog.dismiss();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                MyApplication.mBluetoothConnected = false;
                //todo 更改界面ui
                invalidateOptionsMenu();//更新菜单栏
                try {
                    MyApplication.mBluetoothLeService.close();//断开更彻底(没有这一句，在某些机型，重连会连不上)
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                ToastUtils.showToastShort("已断开");
                tv_connect.setText("还未绑定手环，点击绑定");
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
//                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
//                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
                final byte[] txValue = intent
                        .getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
//                LogUtil.i("接收的数据：" + DataHandlerUtils.bytesToHexStr(txValue));

                List<Integer> datas = DataHandlerUtils.bytesToArrayList(txValue);
                if (datas.get(4) == 0x92 && datas.size() == 17) {//不知道是啥  首次进入“我的手环”连接手环后会走
                    initHeartData();//请求心率数据
                }
                //心率传感器
                if (datas.get(4) == 0XB4) {//[171, 0, 4, 255, 180, 128, 1]
                    Integer integer = datas.get(6);
                    if (integer == 0) {
                        Toast.makeText(context, "手环通信不正常", Toast.LENGTH_SHORT).show();
                    } else if (integer == 1) {
                        Toast.makeText(context, "手环通信正常", Toast.LENGTH_SHORT).show();
                    }
                }
                //测量心率
                if (datas.get(4) == 0x84) {//[171, 0, 5, 255, 49, 10, 0, 190]   [171, 0, 5, 255, 49, 10, 84, 48]
                    Integer integer = datas.get(6);
//                    test_result.setText(String.valueOf(integer));
                }
                //拉取心率数据
                if (datas.get(4) == 0x51 && datas.size() == 13) {//[171, 0, 10, 255, 81, 17, 18, 5, 19, 4, 35, 62, 62]
                    String date = "20" + datas.get(6) + "-" + datas.get(7) + "-" + datas.get(8) + " " + datas.get(9) + ":" + datas.get(10) + ":" + "00";
                    HeartRate heartRate = new HeartRate();
                    heartRate.setDate(TimeUtils.date2TimeStamp(date, "yyyy-MM-dd HH:mm:ss"));
                    heartRate.setHeartRate(datas.get(11));
                    String thTemp = new SimpleDateFormat("yyyy").format(new Date(heartRate.getDate())).toString();
                    LogUtil.i("心率"+heartRate.getHeartRate()+",时间"+new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(heartRate.getDate())).toString());
                    heartRateHelper.insert(heartRate);
                    HeartRateResultBean heartRateResultBean = new HeartRateResultBean();//提交对象
                    heartRateResultBean.setYear(new SimpleDateFormat("yyyy").format(new Date(heartRate.getDate())).toString());//年
                    heartRateResultBean.setMonth(new SimpleDateFormat("MM").format(new Date(heartRate.getDate())).toString());//月
                    heartRateResultBean.setDay(new SimpleDateFormat("dd").format(new Date(heartRate.getDate())).toString());//日
                    heartRateResultBean.setHour(new SimpleDateFormat("HH").format(new Date(heartRate.getDate())).toString());//时
                    heartRateResultBean.setMinute(new SimpleDateFormat("mm").format(new Date(heartRate.getDate())).toString());//分
//                    LogUtil.i("--" + thTemp+"--"+new SimpleDateFormat("yyyy").format(new Date(heartRate.getDate())).toString());
                    heartRateResultBean.setMax_value(datas.get(11) + "");//心率
                    heartRateResultBean.setTimestamp(heartRate.getDate());//long 时间戳
                    postHeartRateBeans.add(heartRateResultBean);
                }
                if (datas.get(4) == 0x51 && datas.size() == 20) {//结束心率
//                    getLastHeart();//服务器最后心率
                }
                if (datas.get(4) == 0x51 && datas.get(5) == 17) {
//                    LogUtil.i(datas.toString());
                }
                if (datas.get(4) == 0x51 && datas.get(5) == 8) {
//                    LogUtil.i(datas.toString());
                }

            }
        }
    };

    String temp = "";//最后心率值  专为打印

    private void initHeartData() {
        postHeartRateBeans=new ArrayList<>();//清空提交心率
        heartRateHelper.deleteAll();//删除所有的   因为本地只留七天
        long time = TimeUtils.getPeriodTopDate(new SimpleDateFormat("yyyy-MM-dd"), 6);
        LogUtil.i("获取这个之后的数据" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time)));
        temp = new SimpleDateFormat("yyyy-MM-dd").format(new Date(time)).toString();
        commandManager.setSyncData(time, time);
    }

    private List<HeartRateResultBean> postHeartRateBeans = new ArrayList<>();//心率数据 HeartRate
    private HeartRateResultBean lastHeart;//服务器最后心率

    /**
     * 提交心率  先请求后台最后一条   对比本地   提交剩下未提交的数据
     */
    private void isPostHeart() {
        List<HeartRateResultBean> postHeartList = new ArrayList<>();
        for (int i = 0; i < postHeartRateBeans.size(); i++) {
            if (lastHeart != null) {
                if (lastHeart.getTimestamp() != 0) {//不为空
                    Date date1 = new Date(lastHeart.getTimestamp());//后台时间
                    Date date2 = new Date(postHeartRateBeans.get(i).getTimestamp());
//                    Date date2 = new Date(
//                            Integer.valueOf(postHeartRateBeans.get(i).getYear())-20,
//                            Integer.valueOf(postHeartRateBeans.get(i).getMonth())-1,
//                            Integer.valueOf(postHeartRateBeans.get(i).getDay()),
//                            Integer.valueOf(postHeartRateBeans.get(i).getHour()),
//                            Integer.valueOf(postHeartRateBeans.get(i).getMinute()));//这条数据时间
                    LogUtil.i("比较" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date1) + "**"
                    + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date2));
                    if (date1.before(date2)){ //表示date1小于date2  最后一条心率日期小于手环date2
                        postHeartList.add(postHeartRateBeans.get(i));
                    }
//                    if (date1.after(date2)) {
//                        postHeartList.add(postHeartRateBeans.get(i));
//                        LogUtil.i("提交心率000");
//                    }
                } else {
                    postHeartList = postHeartRateBeans;
                }
            } else {
                postHeartList = postHeartRateBeans;
            }
        }
        if (postHeartList != null && postHeartList.size() != 0) {
            LogUtil.i("提交心率" + postHeartList.size()+"----"+(i++));
//            postHeart(postHeartList);
        }
    }
int i=0;
    private void postHeart(List<HeartRateResultBean> postHeartList) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.QUERY_WEAR_FIT_HEART_RATE_SAVE
                , new Gson().toJson(postHeartList), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtil.i("提交返回" + jsonObject.toString());
                if (jsonObject.toString().contains("true")) {
                    LogUtil.i("提交成功");
                } else {
                    LogUtil.i("提交失败");
                }
//                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.e("onErrorResponse", volleyError.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));
                return map;
            }
        };
        MyApplication.getHttpQueues().add(jsonObjectRequest);
    }

    //服务器最后心率
    private void getLastHeart() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.QUERY_WEAR_FIT_HEART_RATE_FANDLAST, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i("服务器最后心率" + s);
                HeartRateLastBean heartRateLastBean = new Gson().fromJson(s, HeartRateLastBean.class);
                lastHeart = heartRateLastBean.getData();//服务器最后心率
                isPostHeart();//提交心率
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError != null) {
                    LogUtil.i(volleyError.toString());
                } else {
                    LogUtil.i("NULL");
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, null));
                return map;
            }
        };
        MyApplication.getHttpQueues().add(stringRequest);
    }

    private class ItemBean {
        int img_url;
        String text1;
        String text2;
    }
}
