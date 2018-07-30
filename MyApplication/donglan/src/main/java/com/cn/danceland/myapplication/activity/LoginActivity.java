package com.cn.danceland.myapplication.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.RequestInfoBean;
import com.cn.danceland.myapplication.bean.RequestLoginInfoBean;
import com.cn.danceland.myapplication.bean.RequsetUserDynInfoBean;
import com.cn.danceland.myapplication.evntbus.StringEvent;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.MD5Utils;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.google.gson.Gson;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConnListener;
import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMSdkConfig;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.TIMUserStatusListener;
import com.tencent.qcloud.presentation.business.LoginBusiness;
import com.tencent.qcloud.presentation.event.FriendshipEvent;
import com.tencent.qcloud.presentation.event.GroupEvent;
import com.tencent.qcloud.presentation.event.MessageEvent;
import com.tencent.qcloud.presentation.event.RefreshEvent;
import com.tencent.qcloud.sdk.Constant;
import com.tencent.qcloud.tlslibrary.service.TLSService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

//import com.hyphenate.EMCallBack;
//import com.hyphenate.chat.EMClient;

public class LoginActivity extends Activity implements OnClickListener {


    private EditText mEtPhone;
    private EditText mEtPsw;
    private boolean isPswdChecked = false;
    private ImageView iv_pswd_see;
    ProgressDialog dialog;
    public static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    public static final String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;
    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    public static final String PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            permissions();

        }
    };
    private CheckBox cb_agreement;
    private TLSService tlsService;
    private Button btn_login;
    private boolean isPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        //注册event事件
        EventBus.getDefault().register(this);

        initTXIM();


        intView();
        dialog = new ProgressDialog(this);
        dialog.setMessage("登录中……");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //even事件处理
    @Subscribe
    public void onEventMainThread(StringEvent event) {
        switch (event.getEventCode()) {
            case 1010:
                finish();
                break;
            default:
                break;
        }


    }

    private void intView() {

        findViewById(R.id.tv_register).setOnClickListener(this);
        findViewById(R.id.tv_login_sms).setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.tv_forgetpsw).setOnClickListener(this);
        findViewById(R.id.tv_login_others).setOnClickListener(this);
        btn_login = findViewById(R.id.btn_login);
        findViewById(R.id.logo).setOnClickListener(this);
        iv_pswd_see = findViewById(R.id.iv_pswd_see);
        iv_pswd_see.setOnClickListener(this);
        mEtPhone = findViewById(R.id.et_phone);
        mEtPsw = findViewById(R.id.et_password);
        cb_agreement = findViewById(R.id.cb_agreement);
        findViewById(R.id.tv_agreemnet).setOnClickListener(this);
        //腾讯云im
        tlsService = TLSService.getInstance();
//        tlsService.initAccountLoginService(this,mEtPhone,mEtPsw,btn_login);
//        tlsService.init

    }

    private void initTXIM() {

        TIMSdkConfig config = new TIMSdkConfig(Constant.SDK_APPID).enableCrashReport(false).enableLogPrint(true)
                .setLogLevel(TIMLogLevel.DEBUG)
                .setLogPath(Environment.getExternalStorageDirectory().getPath() + "/donglan/log");
        boolean b = TIMManager.getInstance().init(getApplicationContext(), config);
        LogUtil.i(b + "");

        //基本用户配置
        TIMUserConfig userConfig = new TIMUserConfig()
                .setUserStatusListener(new TIMUserStatusListener() {
                    @Override
                    public void onForceOffline() {
                        //被其他终端踢下线
                        LogUtil.i("onForceOffline");
//                        App.TOKEN = "";
//                        UserControl.getInstance().clear();
//                        DataCleanManager.clearAllCache(getContext());
//                        PageRouter.startLogin(getContext());
//                        finish();
                    }

                    @Override
                    public void onUserSigExpired() {
                        //用户签名过期了，需要刷新userSig重新登录SDK
                        LogUtil.i("onUserSigExpired");
                    }
                })
                //设置连接状态事件监听器
                .setConnectionListener(new TIMConnListener() {
                    @Override
                    public void onConnected() {
                        LogUtil.i("onConnected连接聊天服务器");
                    }

                    @Override
                    public void onDisconnected(int code, String desc) {
                        LogUtil.i("onDisconnected");
                    }

                    @Override
                    public void onWifiNeedAuth(String name) {
                        LogUtil.i("onWifiNeedAuth");
                    }
                });
        RefreshEvent.getInstance().init(userConfig);
        userConfig = FriendshipEvent.getInstance().init(userConfig);
        userConfig = MessageEvent.getInstance().init(userConfig);
        userConfig = GroupEvent.getInstance().init(userConfig);
        //将用户配置与通讯管理器进行绑定
        TIMManager.getInstance().setUserConfig(userConfig);


    }

    @Override
    protected void onResume() {
        super.onResume();
        permissions();
    }


    public void permissions() {
        PermissionsUtil.TipInfo tip = new PermissionsUtil.TipInfo("注意:", "未授予位置和文件权限，应用将无法使用", "不了，谢谢", "打开权限");
        if (PermissionsUtil.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            //有权限
            isPermission = true;
        } else {
            PermissionsUtil.requestPermission(this, new PermissionListener() {
                @Override
                public void permissionGranted(@NonNull String[] permissions) {
                    //用户授予了权限
                    isPermission = true;
                }

                @Override
                public void permissionDenied(@NonNull String[] permissions) {
                    //用户拒绝了申请
                    isPermission = false;

                }
            }, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION}, true, tip);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register://注册
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.tv_login_sms://短信登录
                startActivity(new Intent(LoginActivity.this, LoginSMSActivity.class));
//                finish();
                break;
            case R.id.btn_login://登录
                //判断电话号码是否为空
                if (TextUtils.isEmpty(mEtPhone.getText().toString())) {
                    ToastUtils.showToastShort("请输入账号或手机号");
                    return;
                }
                //判断密码是否为空
                if (TextUtils.isEmpty(mEtPsw.getText().toString())) {
                    ToastUtils.showToastShort("请输入密码");
                    return;
                }
                //判断密码是否包含空格
                if (mEtPsw.getText().toString().contains(" ")) {
                    ToastUtils.showToastShort("密码不能包含空格，请重新输入");
                    return;
                }

                if (!cb_agreement.isChecked()) {
                    ToastUtils.showToastShort("请阅读用户协议，并勾选");
                    return;
                }
                if (!isPermission) {
                    ToastUtils.showToastShort("请开启权限后登录");
                    return;
                }
                dialog.show();
                login();
                LogUtil.i(MD5Utils.encode(mEtPsw.getText().toString().trim()));
                break;
            case R.id.tv_forgetpsw://忘记密码


                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
                break;
            case R.id.tv_login_others://其他方式登陆
                //  Toast.makeText(this, "其他方式登陆", Toast.LENGTH_SHORT).show();
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            getServerVersion();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }.start();


                break;
            case R.id.tv_agreemnet:
                startActivity(new Intent(LoginActivity.this, NewsDetailsActivity.class).putExtra("url", Constants.REGISTER_AGREEMENT_URL).putExtra("title", "用户协议"));

                break;
            case R.id.iv_pswd_see://设置密码可见
                if (isPswdChecked) {
                    //密码不可见
                    mEtPsw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    isPswdChecked = false;
                    iv_pswd_see.setImageResource(R.drawable.img_unlook);

                } else {
                    //密码可见
                    mEtPsw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    isPswdChecked = true;
                    iv_pswd_see.setImageResource(R.drawable.img_look);
                }

                break;
            case R.id.logo:
                // tlsService.initAccountLoginService();
                //  showHostDialog();

                break;
            default:
                break;
        }
    }
//
//    private final String[] HistList = new String[]{"http://192.168.1.117:8003/", "http://192.168.1.123:8003/", "http://192.168.1.122:8003/", "http://192.168.1.119:8003/", "http://wx.dljsgw.com/"};
//    private final String[] HistListName = new String[]{"高振中服务器", "李佳楠服务器", "唐值超服务器", "王丽萍服务器", "阿里云服务器"};
//
//    private void showHostDialog() {
//        AlertDialog.Builder listDialog =
//                new AlertDialog.Builder(this);
//        listDialog.setTitle("请选择您要切换的服务器地址，阿里云环境无法使用沙箱版支付宝");
//        listDialog.setItems(HistListName, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // which 下标从0开始
//                // ...To-do
//                Toast.makeText(LoginActivity.this, "已切换" + which + HistListName[which], Toast.LENGTH_SHORT).show();
//                motifyfiled(Constants.HOST, HistList[which]);
//
////                Constants.HOST = HistList[which];
////                LogUtil.i(Constants.HOST);
////                if (which == 4) {
////                    Constants.DEV_CONFIG = false;
////                } else {
////                    Constants.DEV_CONFIG = true;
////                }
//
//                LogUtil.i(Constants.HOST);
//            }
//        });
//        listDialog.show();
//
//    }
//
//    public void motifyfiled(String oldHost, String newHost) {
//        String updateOldHost = newHost;
//        Class clazz = null;
//        try {
//            clazz = Class.forName("com.cn.danceland.myapplication.utils.Constants");
//            //通过反射拿到变量名
//            Field[] fields = clazz.getDeclaredFields();
//            for (Field f : fields) {
//                LogUtil.i(f.getType().toString());
//                if (f.getType().toString().equals("class java.lang.String")) {
//                    String address = (String) f.get(clazz);
//                    if (address.contains(oldHost) && !address.equals(oldHost)) {
//                        address = address.replace(oldHost, updateOldHost);
//                    }
//                    f.set(clazz, address);
//                }
//            }
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//
//    }

    private boolean getServerVersion() {
        String urlStr = "http://192.168.1.93/test.txt";
        //long a = System.currentTimeMillis();
        try {
            /*
             * 通过URL取得HttpURLConnection 要网络连接成功，需在AndroidMainfest.xml中进行权限配置
             * <uses-permission android:name="android.permission.INTERNET" />
             */
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(60 * 1000);
            conn.setReadTimeout(60 * 1000);
            // 取得inputStream，并进行读取
            InputStream input = conn.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = in.readLine()) != null) {
                sb.append(line);

            }

            LogUtil.e("zzf", sb.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * 登录
     */
    private void login() {


        StringRequest request = new StringRequest(Request.Method.POST, Constants.LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                dialog.dismiss();
                LogUtil.i(s);

                Gson gson = new Gson();

                RequestLoginInfoBean loginInfoBean = gson.fromJson(s, RequestLoginInfoBean.class);
                LogUtil.i(loginInfoBean.toString());
                if (loginInfoBean.getSuccess()) {

                    SPUtils.setString(Constants.MY_USERID, loginInfoBean.getData().getPerson().getId());//保存id

                    SPUtils.setString(Constants.MY_TOKEN, "Bearer+" + loginInfoBean.getData().getToken());
                    SPUtils.setString(Constants.MY_PSWD, MD5Utils.encode(mEtPsw.getText().toString().trim()));//保存id\
                    if (loginInfoBean.getData().getMember() != null) {
                        SPUtils.setString(Constants.MY_MEMBER_ID, loginInfoBean.getData().getMember().getId());
                    }
                    Data data = loginInfoBean.getData();
                    DataInfoCache.saveOneCache(data, Constants.MY_INFO);

                    //查询信息
                    queryUserInfo(loginInfoBean.getData().getPerson().getId());


                    if (Constants.DEV_CONFIG) {
                        login_txim("dev" + data.getPerson().getMember_no(), data.getSig());
                    } else {
                        login_txim(data.getPerson().getMember_no(), data.getSig());
                    }
                setMipushId();
                  SPUtils.setBoolean(Constants.ISLOGINED, true);//保存登录状态
                    // startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                    //        finish();
                    ToastUtils.showToastShort("登录成功");
                    //    login_hx(data.getPerson().getMember_no(),"QWE",data);
                } else {

                    ToastUtils.showToastShort("用户名或密码错误");
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dialog.dismiss();
                ToastUtils.showToastShort("请求失败，请查看网络连接");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put("phone", mEtPhone.getText().toString().trim());
                map.put("password", MD5Utils.encode(mEtPsw.getText().toString().trim()));
                map.put("terminal", "1");
                return map;
            }
        };

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("login");
        // 设置超时时间
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);
    }

    private void queryUserInfo(String id) {

        String params = id;

        String url = Constants.QUERY_USER_DYN_INFO_URL + params;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                Gson gson = new Gson();
                RequsetUserDynInfoBean requestInfoBean = gson.fromJson(s, RequsetUserDynInfoBean.class);

                if (requestInfoBean.getSuccess()) {
                    SPUtils.setInt(Constants.MY_DYN, requestInfoBean.getData().getDyn_no());
                    SPUtils.setInt(Constants.MY_FANS, requestInfoBean.getData().getFanse_no());
                    SPUtils.setInt(Constants.MY_FOLLOWS, requestInfoBean.getData().getFollow_no());
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();

                } else {
                    ToastUtils.showToastShort(requestInfoBean.getErrorMsg());
                }


                // LogUtil.i(DataInfoCache.loadOneCache(Constants.MY_INFO).toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                LogUtil.i(volleyError.toString());

            }

        }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, null));
                // LogUtil.i("Bearer+"+SPUtils.getString(Constants.MY_TOKEN,null));
                return map;
            }
        };
        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("queryUserInfo");
        // 设置超时时间
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);

    }


    /**
     * 登录腾讯im
     *
     * @param identifier 账号
     * @param userSig
     */
    private void login_txim(final String identifier, final String userSig) {
        LogUtil.i(identifier + "/n" + userSig);
        // identifier为用户名，userSig 为用户登录凭证
        //     LogUtil.i("isServiceRunning  " + ServiceUtils.isServiceRunning(getApplicationContext(), "com.tencent.qalsdk.service.QalService"));


        LoginBusiness.loginIm(identifier, userSig, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                LogUtil.i("login failed. code: " + code + " errmsg: " + desc);
                TLSService.getInstance().setLastErrno(-1);
            }

            @Override
            public void onSuccess() {
                LogUtil.i("login succ 登录成功");
                TLSService.getInstance().setLastErrno(0);
                SPUtils.setString("sig", userSig);

                //  TLSHelper.getInstance().setLocalId(UserInfo.ge);
            }
        });

//        TIMManager.getInstance().login(identifier, userSig, new TIMCallBack() {
//            @Override
//            public void onError(int code, String desc) {
//                //错误码 code 和错误描述 desc，可用于定位请求失败原因
//                //错误码 code 列表请参见错误码表
//                LogUtil.i("login failed. code: " + code + " errmsg: " + desc);
//                TLSService.getInstance().setLastErrno(-1);
//
//            }
//
//            @Override
//            public void onSuccess() {
//                LogUtil.i("login succ 登录成功");
//                TLSService.getInstance().setLastErrno(0);
//                UserInfo userInfo=UserInfo.getInstance();
//                UserInfo.getInstance().setId(identifier);
//                UserInfo.getInstance().setUserSig(userSig);
//          //      TLSUserInfo tlsUserInfo =TLSService.getInstance().getLastUserInfo();
//
//            //    startActivity(new Intent(LoginActivity.this, TXIMHomeActivity.class));
//
////                        SPUtils.setBoolean(Constants.ISLOGINED, true);//保存登录状态
////                      startActivity(new Intent(LoginActivity.this, HomeActivity.class));
////
////                       finish();
//            }
//        });
    }


    /**
     * 设置mipusid
     */
    private void setMipushId() {

        StringRequest request = new StringRequest(Request.Method.PUT, Constants.SET_MIPUSH_ID, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //   LogUtil.i(s);
                Gson gson = new Gson();
                RequestInfoBean requestInfoBean = gson.fromJson(s, RequestInfoBean.class);
                if (requestInfoBean.getSuccess()) {
                    LogUtil.i("设置mipush成功");
                } else {
                    LogUtil.i("设置mipush失败");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                LogUtil.i("设置mipush失败" + volleyError.toString());

            }

        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("regId", SPUtils.getString(Constants.MY_MIPUSH_ID, null));
                map.put("terminal", "1");
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, null));

                return map;
            }
        };

        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);

    }

}
