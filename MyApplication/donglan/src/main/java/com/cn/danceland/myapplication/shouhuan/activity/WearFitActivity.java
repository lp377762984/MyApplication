package com.cn.danceland.myapplication.shouhuan.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.db.HeartRate;
import com.cn.danceland.myapplication.db.HeartRateHelper;
import com.cn.danceland.myapplication.db.WearFitSleepBean;
import com.cn.danceland.myapplication.db.WearFitSleepHelper;
import com.cn.danceland.myapplication.db.WearFitStepBean;
import com.cn.danceland.myapplication.db.WearFitStepHelper;
import com.cn.danceland.myapplication.shouhuan.bean.WearFitUser;
import com.cn.danceland.myapplication.shouhuan.command.CommandManager;
import com.cn.danceland.myapplication.shouhuan.service.BluetoothLeService;
import com.cn.danceland.myapplication.shouhuan.utils.DataHandlerUtils;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.StringUtils;
import com.cn.danceland.myapplication.utils.TimeUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.cn.danceland.myapplication.view.DongLanTitleView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 手环首页
 * Created by feng on 2018/6/19.
 */
public class WearFitActivity extends Activity {
    private static final int REQUEST_SEARCH = 1;
    private static final int MSG_REFRESH_DATA = 0;//请求数据  心率  睡眠
    private TextView tv_connect;
    private TextView tv_step;
    private TextView tv_kcal;
    private TextView tv_km;
    private ProgressBar progressb_target;
    private String address;
    private String name;
    private GridView gv_wearfit;
    private DongLanTitleView shouhuan_title;
    private int[] imgID = {R.drawable.shouhuan_heart, R.drawable.shouhuan_sleep, R.drawable.shouhuan_pilao, R.drawable.shouhuan_photo, R.drawable.shouhuan_plan, R.drawable.shouhuan_find
            , R.drawable.shouhuan_setting};
    private String[] text1s = {"心率", "睡眠", "疲劳", "摇摇拍照", "健身计划", "查找手环", "设置"};
    private String[] text2s = {"--bpm", "-时-分", "--", "", "", "", ""};
    private ArrayList<ItemBean> itemBeans;
    private FrameLayout step_gauge_layout;//计步
    private ProgressDialog progressDialog;
    private String address1;//手环默认链接地址  yxx
    private RelativeLayout rl_connect;
    private CommandManager commandManager;
    private WearFitAdapter adapter;

    private HeartRateHelper heartRateHelper = new HeartRateHelper();
    private WearFitSleepHelper sleepHelper = new WearFitSleepHelper();
    private WearFitStepHelper stepHelper = new WearFitStepHelper();

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
        step_gauge_layout = findViewById(R.id.step_gauge_layout);
        adapter = new WearFitAdapter();
        gv_wearfit.setAdapter(adapter);
        shouhuan_title = findViewById(R.id.shouhuan_title);
        shouhuan_title.setTitle("我的手环");
        tv_connect = findViewById(R.id.tv_connect);
        tv_step = findViewById(R.id.tv_step_gauge);
        tv_kcal = findViewById(R.id.tv_kcal);
        tv_km = findViewById(R.id.tv_km);
        progressb_target = findViewById(R.id.progressb_target);
        setListener();
        step_gauge_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WearFitActivity.this, WearFitStepActivity.class));//计步
            }
        });
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
        initHeartData();//心率
        initSleepData();//睡眠
        initStepGaugeData();//计步
        commandManager.sendStep();//首页数据
//        commandManager.setTimeSync();//同步时间给手环
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
                        startActivity(new Intent(WearFitActivity.this, WearFitFatigueActivity.class));
                        break;
                    case 3://摇摇拍照
                        startActivity(new Intent(WearFitActivity.this, WearFitCameraActivity.class));
                        break;
                    case 4://健身计划
                        startActivity(new Intent(WearFitActivity.this, WearFitFitnessPlanActivity.class));
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
                MyApplication.mBluetoothConnected = true;
                MyApplication.isBluetoothConnecting = false;
                //todo 更改界面ui
                invalidateOptionsMenu();//更新菜单栏

//                Message message = new Message();
//                message.what = MSG_REFRESH_DATA;
//                handler.sendMessage(message);

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
//                if (datas.get(4) == 0x92 && datas.size() == 17) {//不知道是啥  首次进入“我的手环”连接手环后会走
//                    initHeartData();//请求心率数据
//                }
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
                    heartRateHelper.insert(heartRate);

                    Calendar calendar = Calendar.getInstance();
                    Date nowTime = new Date(TimeUtils.date2TimeStamp(date, "yyyy-MM-dd HH:mm:ss"));
                    Date endTime = new Date(calendar.getTimeInMillis());
                    calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - 15);
                    Date startTime = new Date(calendar.getTimeInMillis());

                    if (TimeUtils.isEffectiveDate(nowTime, startTime, endTime)) {
                        heartRateLast = datas.get(11);
                        itemBeans.get(0).text2 = heartRateLast + "";//item 心率
                        adapter.notifyDataSetChanged();
                    }
                }
                //拉取睡眠数据
                if (datas.get(4) == 0x52 && datas.size() == 14) {//[171, 0, 11, 255, 82, 128, 18, 7, 31, 0, 49, 2, 0, 29]  11位state 12位*256+13位
                    LogUtil.i("14位" + datas.toString());
                    String date = "20" + datas.get(6) + "-" + datas.get(7) + "-" + datas.get(8) + " " + datas.get(9) + ":" + datas.get(10) + ":" + "00";
                    WearFitSleepBean sleepBean = new WearFitSleepBean();
                    sleepBean.setTimestamp(TimeUtils.date2TimeStamp(date, "yyyy-MM-dd HH:mm:ss"));
                    sleepBean.setState(datas.get(11) + "");//11位state
                    sleepBean.setContinuoustime(datas.get(12) * 256 + datas.get(13));//睡了多久 12位*256+13位
                    LogUtil.i("sleepBean" + sleepBean + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(sleepBean.getTimestamp())).toString());
                    sleepHelper.insert(sleepBean);
                }
                if (datas.get(4) == 0x51 && datas.get(5) == 17) {
//                    LogUtil.i(datas.toString());
                }
                if (datas.get(4) == 0x51 && datas.get(5) == 8) {
//                    LogUtil.i(datas.toString());
                }
                //拉取计步数据
                if (datas.get(4) == 0x51 && datas.size() == 20) {
//                    LogUtil.i(datas.toString());

                    String date = "20" + datas.get(6) + "-" + datas.get(7) + "-" + datas.get(8) + " " + datas.get(9) + ":" + "00" + ":" + "00";
                    int step = datas.get(10) * 256 * 256 + datas.get(11) * 256 + datas.get(12);
                    int cal = datas.get(13) * 256 * 256 + datas.get(14) * 256 + datas.get(15);
                    WearFitStepBean wearFitStepBean = new WearFitStepBean();
                    wearFitStepBean.setTimestamp(TimeUtils.date2TimeStamp(date, "yyyy-MM-dd HH:mm:ss"));
                    wearFitStepBean.setStep(step);
                    wearFitStepBean.setCal(cal);
                    LogUtil.i("时间：" + date + " 步数：" + step + " 卡路里：" + cal);
                    stepHelper.insert(wearFitStepBean);
                }
                if (datas.get(4) == 0x51 && datas.size() == 17) {//首页数据
//                    LogUtil.i(datas.toString());
                    String date = "20" + datas.get(6) + "-" + datas.get(7) + "-" + datas.get(8) + " " + datas.get(9) + ":" + "00" + ":" + "00";
                    int step = (datas.get(6) << 16) + (datas.get(7) << 8) + datas.get(8);//计步
                    int cal = (datas.get(9) << 16) + (datas.get(10) << 8) + datas.get(11);//卡路里
                    int ligthSleep = datas.get(12) * 60 + datas.get(13);//浅睡
                    int deepSleep = datas.get(14) * 60 + datas.get(15);//深睡
                    int wakeupTime = datas.get(16);//醒来次数

                    WearFitUser wearFitUser = (WearFitUser) DataInfoCache.loadOneCache(Constants.MY_WEAR_FIT_SETTING);//手环设置
                    float kmf = (float) wearFitUser.getStepLength() * (float) step / (float) 100000.00;//100步长  1000km
                    DecimalFormat fnum = new DecimalFormat("##0.00");
                    String km = fnum.format(kmf);

                    NumberFormat numberFormat = NumberFormat.getInstance();// 创建一个数值格式化对象
                    numberFormat.setMaximumFractionDigits(0);// 设置精确到小数点后2位
                    String targetStr = numberFormat.format((float) step / (float) wearFitUser.getGold_steps() * 100);//达标
                    int target = 0;
                    if (StringUtils.isNumeric(targetStr)) {
                        target = Integer.valueOf(targetStr);
                    }
                    Calendar c = Calendar.getInstance();
                    int hour = c.get(Calendar.HOUR_OF_DAY);
                    int fatigue = 0;
                    if (hour >= 6 && hour < 11) {
                        fatigue = 0;
                    } else if (hour >= 11 && hour < 18) {
                        fatigue = 10;
                    } else if (hour >= 18 && hour < 24) {
                        fatigue = 20;
                    } else {
                        fatigue = 30;
                    }
                    fatigue = (int) (fatigue + Math.sqrt(step) / 2);
                    tv_step.setText(step + "");
                    tv_kcal.setText(cal + context.getResources().getString(R.string.kcal_text));
                    tv_km.setText(km + context.getResources().getString(R.string.km_english_text));
                    progressb_target.setProgress(target);
                    itemBeans.get(0).text2 = heartRateLast + "";//item 心率
                    itemBeans.get(1).text2 = (deepSleep + ligthSleep) + "";//item 睡眠
                    itemBeans.get(2).text2 = fatigue + "";//item 疲劳
                    adapter.notifyDataSetChanged();
                }
                if (datas.get(4) == 0x51) {//首页数据
//                    LogUtil.i(datas.toString());
                }
            }
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case MSG_REFRESH_DATA:
                    initHeartData();//心率
                    initSleepData();//睡眠
                    initStepGaugeData();//计步
                    commandManager.sendStep();//首页数据
                    commandManager.setTimeSync();//同步时间给手环
                    break;
                default:
                    break;
            }
        }
    };
    int heartRateLast = 0;//最后心率值

    private void initHeartData() {
        heartRateHelper.deleteAll();//删除所有的   因为本地只留七天
        long time = TimeUtils.getPeriodTopDate(new SimpleDateFormat("yyyy-MM-dd"), 6);
        LogUtil.i("获取这个之后的心率数据" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time)));
        commandManager.setSyncData(time, time);
    }

    private void initSleepData() {
        sleepHelper.deleteAll();//删除所有的   因为本地只留七天
        long time = TimeUtils.getPeriodTopDate(new SimpleDateFormat("yyyy-MM-dd"), 6);
        LogUtil.i("获取这个之后的睡眠数据" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time)));
        commandManager.setSyncSleepData(time);
    }

    private void initStepGaugeData() {
        stepHelper.deleteAll();//删除所有的   因为本地只留七天
        long time = TimeUtils.getPeriodTopDate(new SimpleDateFormat("yyyy-MM-dd"), 6);//TODO
        LogUtil.i("获取这个之后的计步数据" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time)));
        commandManager.setSyncData(time, time);//整点计步
    }

    private class ItemBean {
        int img_url;
        String text1;
        String text2;
    }
}
