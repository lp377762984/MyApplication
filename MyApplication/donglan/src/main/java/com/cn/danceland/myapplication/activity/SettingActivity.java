package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.RequsetSimpleBean;
import com.cn.danceland.myapplication.db.DBData;
import com.cn.danceland.myapplication.db.Donglan;
import com.cn.danceland.myapplication.evntbus.StringEvent;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataCleanManager;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.cn.danceland.myapplication.view.CustomLocationPicker;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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


    DBData dbData;
    String zoneCode, mZoneCode;

    List<Donglan> zoneArr, codeArr;
    //ArrayList<String> cityList1;
    String location;
    List<Donglan> cityList;
    ArrayList<String> proList;
    static String emailFormat = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";

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
            }
        };
    };
    private TextView tv_cache;

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
        if (zoneCode != null && !"".equals(zoneCode)) {
            if (zoneCode.contains(".0")) {
                zoneArr = dbData.queryCityValue(zoneCode);
            } else {
                zoneArr = dbData.queryCityValue(zoneCode + ".0");
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

        tv_number = findViewById(R.id.tv_number);
        tv_phone = findViewById(R.id.tv_phone);
        tx_location = findViewById(R.id.tx_location);
        tv_weixin = findViewById(R.id.tv_weixin);
        tv_email = findViewById(R.id.tv_email);

        if (mInfo.getPerson() != null) {
            tv_weixin.setText(mInfo.getPerson().getWeichat_no());
            tv_email.setText(mInfo.getPerson().getMail());
        }

        if (zoneArr.size() > 0) {
            tx_location.setText(zoneArr.get(0).getProvince() + " " + zoneArr.get(0).getCity());
            zoneArr.clear();
        }

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

                final CustomLocationPicker customLocationPicker = new CustomLocationPicker(this);
                customLocationPicker.setDialogOnClickListener(new CustomLocationPicker.OnClickEnter() {
                    @Override
                    public void onClick() {
                        String city = customLocationPicker.getCity();
                        mZoneCode = dbData.queryCity(city).get(0).getCityValue();
                        tx_location.setText(customLocationPicker.getZone());

                        mInfo.getPerson().setZone_code(mZoneCode);
                        DataInfoCache.saveOneCache(mInfo, Constants.MY_INFO);

                        if (mZoneCode.contains(".0")) {
                            commitSelf(Constants.MODIFY_ZONE, "zoneCode", mZoneCode.replace(".0", ""));
                        } else {
                            commitSelf(Constants.MODIFY_ZONE, "zoneCode", mZoneCode);
                        }

                    }
                });
                customLocationPicker.showLocation();

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
                Message msg=Message.obtain();
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
//                    HomeActivity.instance.finish();
//                    finish();
                    logouthx();
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

//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> map = new HashMap<String, String>();
//
//                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, null));
//                // LogUtil.i("Bearer+"+SPUtils.getString(Constants.MY_TOKEN,null));
//                return map;
//            }
        };

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("logOut");
        // 设置超时时间
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);
    }

    private void logouthx() {
        EMClient.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                //成功
                startActivity(new Intent(SettingActivity.this, LoginActivity.class));

                SPUtils.setBoolean(Constants.ISLOGINED, false);
                //退出主页面
                HomeActivity.instance.finish();
                finish();
            }

            @Override
            public void onProgress(int progress, String status) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(int code, String message) {
                // TODO Auto-generated method stub
                //失败
                LogUtil.i("环信退出登录失败");
            }
        });


    }


}
