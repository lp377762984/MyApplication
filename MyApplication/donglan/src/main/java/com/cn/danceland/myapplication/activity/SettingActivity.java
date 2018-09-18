package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.JsonBean;
import com.cn.danceland.myapplication.bean.RequsetSimpleBean;
import com.cn.danceland.myapplication.db.DBData;
import com.cn.danceland.myapplication.db.Donglan;
import com.cn.danceland.myapplication.db.HeartRateHelper;
import com.cn.danceland.myapplication.db.WearFitSleepHelper;
import com.cn.danceland.myapplication.db.WearFitStepHelper;
import com.cn.danceland.myapplication.evntbus.StringEvent;
import com.cn.danceland.myapplication.im.model.FriendshipInfo;
import com.cn.danceland.myapplication.im.model.GroupInfo;
import com.cn.danceland.myapplication.im.model.UserInfo;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataCleanManager;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.GetJsonDataUtil;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.qcloud.presentation.business.LoginBusiness;
import com.tencent.qcloud.presentation.event.MessageEvent;
import com.tencent.qcloud.tlslibrary.service.TlsBusiness;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingActivity extends Activity implements View.OnClickListener {

    //View locationView;
    TextView lo_cancel_action, over_action, tx_location;
    //PopupWindow locationWindow;
    //ListView list_province, list_city;
    //LocationAdapter proAdapter, cityAdapter;
    private TextView tv_number;
    private TextView tv_phone, tv_weixin, tv_email;
    private Data mInfo;
    private String myCity, myProvince;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0012;
    private static final int MSG_LOAD_FAILED = 0x0013;
    DBData dbData;
    String zoneCode, mZoneCode;

    List<Donglan> zoneArr, codeArr;
    //ArrayList<String> cityList1;
    String location;
    List<Donglan> cityList;
    ArrayList<String> proList;
    String zone = null;
    static String emailFormat = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
    private boolean isLoaded = false;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0x01:
                    // dialog.dismiss();
                    tv_cache.setText("0.0KB");
                    Toast.makeText(SettingActivity.this, "已清除缓存！", Toast.LENGTH_SHORT).show();
                    break;
                case 0x02:
                    // dialog.dismiss();
                    break;


                case MSG_LOAD_SUCCESS:
                    // Toast.makeText(SettingActivity.this, "Parse Succeed", Toast.LENGTH_SHORT).show();
                    isLoaded = true;

                    LogUtil.i("修改，地区");

                    if (!TextUtils.isEmpty(mInfo.getPerson().getZone_code())) {
                        Long zonecode = Long.valueOf(mInfo.getPerson().getZone_code());
                        for (int i = 0; options1Items.size() > i; i++) {
                            if ((int)(zonecode/1000)==(int)(options1Items.get(i).getValue()/1000)){
                                LogUtil.i(zone);
                                zone=options1Items.get(i).getLabel();
                                if (options1Items.get(i).getChildren()!=null) {
                                    for(int j = 0;j<options1Items.get(i).getChildren().size();j++){
                                        if (options1Items.get(i).getChildren().get(j)!=null&&zonecode==(options1Items.get(i).getChildren().get(j).getValue())){
                                            zone=options1Items.get(i).getLabel()+options1Items.get(i).getChildren().get(j).getLabel();
                                            LogUtil.i(zone);
                                        }

                                    }
                                }

                            }

                        }

                    }

                    tx_location.setText(zone);




                    break;

                case MSG_LOAD_FAILED:
                    //     Toast.makeText(SettingActivity.this, "Parse Failed", Toast.LENGTH_SHORT).show();
                    break;
                case 1223:
                    LogUtil.i("修改，地区");

                    if (!TextUtils.isEmpty(mInfo.getPerson().getZone_code())) {
                        Long zonecode = Long.valueOf(mInfo.getPerson().getZone_code());
                        for (int i = 0; options1Items.size() > i; i++) {
                            if ((int)(zonecode/1000)==(int)(options1Items.get(i).getValue()/1000)){
                               LogUtil.i(zone);
                                zone=options1Items.get(i).getLabel();
                                if (options1Items.get(i).getChildren()!=null) {
                                    for(int j = 0;j<options1Items.get(i).getChildren().size();j++){
                                        if (options1Items.get(i).getChildren().get(j)!=null&&zonecode==(options1Items.get(i).getChildren().get(j).getValue())){
                                            zone=options1Items.get(i).getLabel()+options1Items.get(i).getChildren().get(j).getLabel();
                                            LogUtil.i(zone);
                                        }

                                    }
                                }
                            }

                        }

                    }

                    tx_location.setText(zone);

                    break;


            }
        }

        ;
    };
    private TextView tv_cache;
    private Thread thread;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //注册event事件
        EventBus.getDefault().register(this);
        initHost();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册event事件
        EventBus.getDefault().unregister(this);
    }

    //even事件处理
    @Subscribe
    public void onEventMainThread(StringEvent event) {
        if (99 == event.getEventCode()) {
            String msg = event.getMsg();

            tv_phone.setText(msg);
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        }

    }

    private void initHost() {
        dbData = new DBData();
        mInfo = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
        zoneCode = mInfo.getPerson().getZone_code();
        zoneArr = new ArrayList<Donglan>();
        LogUtil.i(zoneCode);
        if (zoneCode != null && !"".equals(zoneCode)) {
            if (zoneCode.contains(".0")) {
                zoneArr = dbData.queryCityValue(zoneCode);
                LogUtil.i(zoneArr.size() + "" + dbData);
            } else {
                zoneArr = dbData.queryCityValue(zoneCode + ".0");
                LogUtil.i(zoneArr.size() + "");
            }
        }
    }

    private void initView() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_quit).setOnClickListener(this);
        findViewById(R.id.ll_setting).setOnClickListener(this);
        findViewById(R.id.ll_setting_phone).setOnClickListener(this);
        findViewById(R.id.ll_setting_location).setOnClickListener(this);
        findViewById(R.id.ll_about_us).setOnClickListener(this);
        findViewById(R.id.ll_clear).setOnClickListener(this);
        findViewById(R.id.ll_my_shop).setOnClickListener(this);
        findViewById(R.id.ll_setting_weixin).setOnClickListener(this);
        findViewById(R.id.ll_setting_email).setOnClickListener(this);
        findViewById(R.id.ll_blacklist).setOnClickListener(this);

        tv_number = findViewById(R.id.tv_number);
        tv_phone = findViewById(R.id.tv_phone);
        tx_location = findViewById(R.id.tx_location);
        tv_weixin = findViewById(R.id.tv_weixin);
        tv_email = findViewById(R.id.tv_email);

        if (mInfo.getPerson() != null) {
            if (mInfo.getPerson().getWeichat_no() == null || "".equals(mInfo.getPerson().getWeichat_no())) {
                tv_weixin.setText("未绑定");
            } else {
                tv_weixin.setText(mInfo.getPerson().getWeichat_no());
            }
            if (mInfo.getPerson().getMail() == null || "".equals(mInfo.getPerson().getMail())) {
                tv_email.setText("未绑定");
            } else {
                tv_email.setText(mInfo.getPerson().getMail());
            }
        } else {
            tv_weixin.setText("未绑定");
            tv_email.setText("未绑定");
        }

//        if (zoneArr.size() > 0) {
//            tx_location.setText(zoneArr.get(0).getProvince() + " " + zoneArr.get(0).getCity());
//            myCity = zoneArr.get(0).getCity();
//            myProvince = dbData.queryCity(myCity).get(0).getProvince();
//            zoneArr.clear();
//        }

        location = tx_location.getText().toString();

        // mInfo = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);

        if (!TextUtils.isEmpty(mInfo.getPerson().getPhone_no())) {
            tv_phone.setText(mInfo.getPerson().getPhone_no());
        }

        //设置会员号
        if (!TextUtils.isEmpty(mInfo.getPerson().getMember_no())) {
            tv_number.setText(mInfo.getPerson().getMember_no());
        } else {
            tv_number.setText("未设置");
        }
        tv_cache = (TextView) findViewById(R.id.tv_cache);
        //获得应用内部缓存(/data/data/com.example.androidclearcache/cache)
        File file = new File(this.getCacheDir().getPath());
        LogUtil.i(this.getCacheDir().getPath());
        try {
            tv_cache.setText(DataCleanManager.getAllCacheSize());
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (thread == null) {//如果已创建就不再重新创建子线程了


            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    // 子线程中解析省市区数据
                    initJsonData();
                }
            });
            thread.start();
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back://返回
                finish();
                break;
            case R.id.tv_quit://退出

                logOut();
                dbData.deleteMessageD();
                break;
            case R.id.ll_setting://设置会员
                //   showInputDialog();

                break;
            case R.id.ll_setting_phone://设置手机号
                showSettingPhoneDialog();
                break;
            case R.id.ll_setting_location://设置位置
                //showLocation();

//                final CustomLocationPicker customLocationPicker = new CustomLocationPicker(this, myCity, myProvince);
//                customLocationPicker.setDialogOnClickListener(new CustomLocationPicker.OnClickEnter() {
//                    @Override
//                    public void onClick() {
//                        String city = customLocationPicker.getCity();
//                        myCity = city;
//                        myProvince = dbData.queryCity(city).get(0).getProvince();
//                        mZoneCode = dbData.queryCity(city).get(0).getCityValue();
//                        tx_location.setText(customLocationPicker.getZone());
//
//                        mInfo.getPerson().setZone_code(mZoneCode);
//                        DataInfoCache.saveOneCache(mInfo, Constants.MY_INFO);
//
//                        if (mZoneCode.contains(".0")) {
//                            commitSelf(Constants.MODIFY_ZONE, "zoneCode", mZoneCode.replace(".0", ""));
//                        } else {
//                            commitSelf(Constants.MODIFY_ZONE, "zoneCode", mZoneCode);
//                        }
//
//                    }
//                });
//                customLocationPicker.showLocation();

                if (isLoaded) {
                    showPickerView();
                } else {
                    Toast.makeText(SettingActivity.this, "Please waiting until the data is parsed", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_about_us://关于我们
                showAboutUs();
                break;
            case R.id.ll_clear://清除缓存

                showClearDialog();

                break;
            case R.id.ll_my_shop:
                LogUtil.i(mInfo.getPerson().getDefault_branch());
                if (TextUtils.isEmpty(mInfo.getPerson().getDefault_branch())) {
                    ToastUtils.showToastShort("请先加入一个门店");
                    return;
                }
                //  startActivity(new Intent(SettingActivity.this, MyShopActivity.class));
                break;
            case R.id.ll_setting_weixin:
                showName(0);
                break;
            case R.id.ll_setting_email:
                showName(1);
                break;
            case R.id.ll_blacklist:
                startActivity(new Intent(SettingActivity.this, BlackListActivity.class));
                break;
            default:
                break;
        }
    }


    public void showName(final int i) {
        //i==0是编辑微信 i==1表示邮箱
        android.support.v7.app.AlertDialog.Builder normalDialog =
                new android.support.v7.app.AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.edit_name, null);

        TextView dialogTitleName = dialogView.findViewById(R.id.tv_nick_name);
        final EditText ed = dialogView.findViewById(R.id.edit_name);
        if (i == 0) {
            dialogTitleName.setText("输入微信号");
            InputFilter[] lengthFilter = {new InputFilter.LengthFilter(20)};
            ed.setFilters(lengthFilter);
        } else {
            dialogTitleName.setText("输入邮箱");
        }
        //normalDialog.setTitle("编辑昵称");

        normalDialog.setView(dialogView);
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String s = ed.getText().toString();
                        if (i == 0) {
                            tv_weixin.setText(s);
                            commitSelf(Constants.MODIFY_WEIXIN, "weichat_no", s);
                            mInfo.getPerson().setWeichat_no(s);
                            DataInfoCache.saveOneCache(mInfo, Constants.MY_INFO);
                        } else {
                            if (s.matches(emailFormat)) {
                                tv_email.setText(s);
                                commitSelf(Constants.MODIFY_MAIL, "mail", s);
                                mInfo.getPerson().setMail(s);
                                DataInfoCache.saveOneCache(mInfo, Constants.MY_INFO);
                            } else {
                                ToastUtils.showToastShort("请输入合法邮箱地址");
                            }
                        }
                    }
                });
        // 显示
        normalDialog.show();

    }

    public void commitSelf(String url, final String mapkey, final String mapvalue) {

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s.contains("true")) {
                    if (TextUtils.equals("zoneCode", mapkey)) {
                        mInfo.getPerson().setZone_code(mapvalue);
                        DataInfoCache.saveOneCache(mInfo, Constants.MY_INFO);
                        Message message=Message.obtain();
                        message.what=1223;
                        handler.sendMessage(message);
                    }
                    ToastUtils.showToastShort("修改成功！");
                } else {
                    ToastUtils.showToastShort("修改失败！");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToastShort("修改失败！请检查网络");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(mapkey, mapvalue);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));

                return map;
            }
        };
        MyApplication.getHttpQueues().add(stringRequest);
    }

    private void showAboutUs() {
        Intent intent = new Intent(SettingActivity.this, AboutUsActivity.class);
        startActivity(intent);
    }


    /**
     * 设置手机号
     */
    private void showSettingPhoneDialog() {
        AlertDialog.Builder dialog =
                new AlertDialog.Builder(this);
        dialog.setTitle("提示");
        dialog.setMessage("是否重新绑定手机号");
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                startActivity(new Intent(SettingActivity.this, ConfirmPasswordActivity.class).putExtra("phone", tv_phone.getText().toString()));
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }


    /**
     * 清除缓存
     */
    private void showClearDialog() {
        AlertDialog.Builder dialog =
                new AlertDialog.Builder(this);
        dialog.setTitle("提示");
        dialog.setMessage("是否清除全部缓存");
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Message msg = Message.obtain();
                try {
                    //清理内部缓存
                    DataCleanManager.cleanInternalCache(getApplicationContext());
                    DataCleanManager.cleanExternalCache(getApplicationContext());
                    // LogUtil.i(getApplicationContext().getCacheDir().getPath());
                    msg.what = 0x01;
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = 0x02;
                }
                handler.sendMessageDelayed(msg, 1000);
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }

    /**
     * 设置会员号
     */
    private void showInputDialog() {

        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(this);
        final View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_input_phone, null);
        inputDialog.setTitle("设置会员号");
        inputDialog.setView(dialogView);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 获取EditView中的输入内容
                        EditText et_number =

                                dialogView.findViewById(R.id.et_number);

                        mInfo.getPerson().setMember_no(et_number.getText().toString());
                        // DataInfoCache.saveOneCache(mInfo, Constants.MY_INFO);

                        tv_number.setText(et_number.getText().toString());

//                        Toast.makeText(SettingActivity.this,
//                                edit_phone.getText().toString(),
//                                Toast.LENGTH_SHORT).show();
                    }
                });
        inputDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        inputDialog.show();
    }


    /***
     * 退出登录
     */
    private void logOut() {

        String url = Constants.LOGOUT_URL;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s);

                Gson gson = new Gson();
                RequsetSimpleBean requestInfoBean = new RequsetSimpleBean();
                requestInfoBean = gson.fromJson(s, RequsetSimpleBean.class);
                if (requestInfoBean.getSuccess()) {
//                    //成功
//                    startActivity(new Intent(SettingActivity.this, LoginActivity.class));
//
//                    SPUtils.setBoolean(Constants.ISLOGINED, false);
//                    //退出主页面
//                    TXIMHomeActivity.instance.finish();
//                    finish();
                    //   logouthx();
                    logoutTXIM();
                    startActivity(new Intent(SettingActivity.this, LoginActivity.class));

                    SPUtils.setBoolean(Constants.ISLOGINED, false);
                    //清空手环数据-- 开始
                    if (MyApplication.mBluetoothConnected) {
                        try {
                            MyApplication.mBluetoothLeService.disconnect();
                            MyApplication.mBluetoothConnected = false;//更改解绑连接状态 yxx
                            SPUtils.setString(Constants.ADDRESS, "");
                            SPUtils.setString(Constants.NAME, "");
                            SPUtils.setString("ClockList", "");//闹钟
                            HeartRateHelper heartRateHelper = new HeartRateHelper();
                            WearFitSleepHelper sleepHelper = new WearFitSleepHelper();
                            WearFitStepHelper stepHelper = new WearFitStepHelper();
                            heartRateHelper.deleteAll();
                            sleepHelper.deleteAll();
                            stepHelper.deleteAll();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    //清空手环数据-- 结束

                    //退出主页面
                    HomeActivity.instance.finish();
                    finish();

                } else {
                    //失败
                    ToastUtils.showToastShort("退出登录失败");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToastShort("请求失败，请查看网络连接");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put("terminal", "1");
                map.put("member_no", mInfo.getPerson().getMember_no());
                map.put("person_id", SPUtils.getString(Constants.MY_USERID, null));
                // map.put("romType", "0");
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, null));
                // LogUtil.i("Bearer+"+SPUtils.getString(Constants.MY_TOKEN,null));
                return map;
            }
        };

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("logOut");
        // 设置超时时间
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);
    }

    public void logoutTXIM() {

        LoginBusiness.logout(new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
//                if (getActivity() != null){
//                    Toast.makeText(getActivity(), getResources().getString(R.string.setting_logout_fail), Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onSuccess() {
                LogUtil.i("退出IM");
                TlsBusiness.logout(UserInfo.getInstance().getId());
                UserInfo.getInstance().setId(null);
                MessageEvent.getInstance().clear();
                FriendshipInfo.getInstance().clear();
                GroupInfo.getInstance().clear();
            }
        });


//        Intent intent = new Intent(HomeActivity.this,SplashActivity.class);
//        finish();
//        startActivity(intent);

    }

    //    private void logouthx() {
//        EMClient.getInstance().logout(true, new EMCallBack() {
//
//            @Override
//            public void onSuccess() {
//                // TODO Auto-generated method stub
//                //成功
//                startActivity(new Intent(SettingActivity.this, LoginActivity.class));
//
//                SPUtils.setBoolean(Constants.ISLOGINED, false);
//                //退出主页面
//                TXIMHomeActivity.instance.finish();
//                finish();
//            }
//
//            @Override
//            public void onProgress(int progress, String status) {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void onError(int code, String message) {
//                // TODO Auto-generated method stub
//                //失败
//                LogUtil.i("环信退出登录失败");
//            }
//        });
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<Long> zonecode1 = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<Long>> zonecode2 = new ArrayList<>();
    //  private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();

    private void showPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getLabel() + options2Items.get(options1).get(options2);


            //    ToastUtils.showToastShort(tx + zonecode2.get(options1).get(options2));


                commitSelf(Constants.MODIFY_ZONE, "zoneCode", zonecode2.get(options1).get(options2) + "");
            }
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

//        pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器
        //     pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "address.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;
        //    LogUtil.i(jsonBean.toString());
        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<Long> CityListcode = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）
            zonecode1.add(jsonBean.get(i).getValue());

//
            //        LogUtil.i(CityList.toString());

            for (int c = 0; c < jsonBean.get(i).getChildren().size(); c++) {//遍历该省份的所有城市
                //     LogUtil.i(jsonBean.get(i).getChildren().toString());
                if (jsonBean.get(i).getChildren() != null && jsonBean.get(i).getChildren().get(c) != null) {
                    String CityName = jsonBean.get(i).getChildren().get(c).getLabel();
                    CityList.add(CityName);//添加城市
                    CityListcode.add(jsonBean.get(i).getChildren().get(c).getValue());

                    //        LogUtil.i(CityList.toString());

//                    ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表
//
//                    //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
//                    if (jsonBean.get(i).getChildren().get(c).getChildren() == null
//                            || jsonBean.get(i).getChildren().get(c).getChildren().size() == 0) {
//                        City_AreaList.add("");
//                    } else {
//                        for (int j = 0; j < jsonBean.get(i).getChildren().get(c).getChildren().size(); j++) {
//                            if (jsonBean.get(i).getChildren()!=null){
//                                City_AreaList.add(jsonBean.get(i).getChildren().get(c).getChildren().get(j).getLabel());
//                            }
//
//                        }
//
//                    }
//
//                    Province_AreaList.add(City_AreaList);//添加该省所有地区数据
//                    LogUtil.i(Province_AreaList.toString());

                }


            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);
            zonecode2.add(CityListcode);
//            /**
//             * 添加地区数据
//             */
//            options3Items.add(Province_AreaList);
        }

        handler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }


    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            handler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }

}
