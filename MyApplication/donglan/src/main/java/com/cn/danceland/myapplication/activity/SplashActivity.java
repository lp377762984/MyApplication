package com.cn.danceland.myapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.db.DBData;
import com.cn.danceland.myapplication.im.model.UserInfo;
import com.cn.danceland.myapplication.utils.AppUtils;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConnListener;
import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.TIMUserStatusListener;
import com.tencent.qalsdk.QALSDKManager;
import com.tencent.qcloud.presentation.business.InitBusiness;
import com.tencent.qcloud.presentation.business.LoginBusiness;
import com.tencent.qcloud.presentation.event.FriendshipEvent;
import com.tencent.qcloud.presentation.event.GroupEvent;
import com.tencent.qcloud.presentation.event.MessageEvent;
import com.tencent.qcloud.presentation.event.RefreshEvent;
import com.tencent.qcloud.presentation.presenter.SplashPresenter;
import com.tencent.qcloud.tlslibrary.service.TLSService;
import com.tencent.qcloud.tlslibrary.service.TlsBusiness;
import com.tencent.qcloud.ui.NotifyDialog;

import java.io.IOException;

import tencent.tls.platform.TLSHelper;


/**
 * 开屏页
 */
public class SplashActivity extends FragmentActivity implements TIMCallBack {

    private static final int sleepTime = 2000;
    private SplashPresenter presenter;


    @Override
    protected void onCreate(Bundle arg0) {
        setContentView(R.layout.activity_splash);
        super.onCreate(arg0);

        RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.splash_root);
        TextView versionText = (TextView) findViewById(R.id.tv_version);

        versionText.setText("版本号：" + AppUtils.getVersionName(this));


        AlphaAnimation animation = new AlphaAnimation(1.0f, 1.0f);
        animation.setDuration(1500);
        rootLayout.startAnimation(animation);
        init();

    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.i(AppUtils.getDeviceId(this));
        new Thread(new Runnable() {

            public void run() {
                LogUtil.i(SPUtils.getBoolean("iscopy", false)+"");
                if (!SPUtils.getBoolean("iscopy", false)) {

                    copyDb();
                }
                //判断是否登录
                if (SPUtils.getBoolean(Constants.ISLOGINED, false)) {

                    long start = System.currentTimeMillis();


                    long costTime = System.currentTimeMillis() - start;
                    //wait
                    if (sleepTime - costTime > 0) {
                        try {
                            Thread.sleep(sleepTime - costTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //已经登录，进入主界面
                    if (DataInfoCache.loadOneCache(Constants.MY_INFO) == null) {
                        SPUtils.setBoolean(Constants.ISLOGINED, false);
                        LogUtil.i("MY_INFO=NULL");
//                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//                        finish();
                        navToLogin();
                    } else {
//                        startActivity(new Intent(SplashActivity.this, HomeActivity.class));

//                        finish();
                        navToHome();
                    }


                } else {


                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                    }
//
//                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//                    finish();

                    navToLogin();
                }
            }
        }).start();

    }

    public void copyDb() {

        try {
            boolean bl = DBData.copyRawDBToApkDb(SplashActivity.this, R.raw.donglan, "/data/data/com.cn.danceland.myapplication/databases/", "donglan.db", false);

            if (bl) {
                SPUtils.setBoolean("iscopy", bl);
                LogUtil.i("拷贝成功");
            }else {
                SPUtils.setBoolean("iscopy", false);
                LogUtil.i("拷贝不成功");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void init() {
//        // 务必检查IMSDK已做以下初始化
        QALSDKManager.getInstance().setEnv(0);
        QALSDKManager.getInstance().init(getApplicationContext(), 1400090939);
// 初始化TLSSDK
        TLSHelper tlsHelper = TLSHelper.getInstance().init(getApplicationContext(), 1400090939);

        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        int loglvl = pref.getInt("loglvl", TIMLogLevel.DEBUG.ordinal());
        //初始化IMSDK
        InitBusiness.start(getApplicationContext(), loglvl);
        //初始化TLS
        TlsBusiness.init(getApplicationContext());
        LogUtil.i(UserInfo.getInstance().getId() + UserInfo.getInstance().getUserSig());
        String id = TLSService.getInstance().getLastUserIdentifier();
        UserInfo.getInstance().setId(id);
        UserInfo.getInstance().setUserSig(TLSService.getInstance().getUserSig(id));

           LogUtil.i(UserInfo.getInstance().getId() + UserInfo.getInstance().getUserSig());
             LogUtil.i((UserInfo.getInstance().getId() != null && (!TLSService.getInstance().needLogin(UserInfo.getInstance().getId()))) + "@@@@" + (UserInfo.getInstance().getId() + (!TLSService.getInstance().needLogin(UserInfo.getInstance().getId()))));
//        LogUtil.i(UserInfo.getInstance().getId() + TLSService.getInstance().getLastUserInfo().accountType);
//        presenter = new SplashPresenter(this);
//        presenter.start();

//        tlsHelper.LOGIN
    }

    private void init_txim() {
        //登录之前要初始化群和好友关系链缓存
        TIMUserConfig userConfig = new TIMUserConfig();
        userConfig.setUserStatusListener(new TIMUserStatusListener() {
            @Override
            public void onForceOffline() {
                LogUtil.i("receive force offline message");
//                   Intent intent = new Intent(this, DialogActivity.class);
//                 startActivity(intent);
            }

            @Override
            public void onUserSigExpired() {
                //票据过期，需要重新登录
                new NotifyDialog().show(getString(R.string.tls_expire), getSupportFragmentManager(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                            logout();
                        LogUtil.i("过期");
                    }
                });

            }
        })
                .setConnectionListener(new TIMConnListener() {
                    @Override
                    public void onConnected() {
                        LogUtil.i("onConnected");
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

        //设置刷新监听
        RefreshEvent.getInstance().init(userConfig);
        userConfig = FriendshipEvent.getInstance().init(userConfig);
        userConfig = GroupEvent.getInstance().init(userConfig);
        userConfig = MessageEvent.getInstance().init(userConfig);
        TIMManager.getInstance().setUserConfig(userConfig);

        LogUtil.i(UserInfo.getInstance().getId() + SPUtils.getString("sig", null));
        LoginBusiness.loginIm(UserInfo.getInstance().getId(), SPUtils.getString("sig", null), this);
//        int sdkAppid = 开发者申请的SDK Appid;
//Constants.DEV_CONFIG?"dev"+UserInfo.getInstance().getId():UserInfo.getInstance().getId()
//
//
//// 务必检查IMSDK已做以下初始化
//        QALSDKManager.getInstance().setEnv(0);
//        QALSDKManager.getInstance().init(getApplicationContext(), sdkAppid, 0);
//
//// 初始化TLSSDK
//        TLSHelper tlsHelper = TLSHelper.getInstance().init(getApplicationContext(), sdkAppid);
    }

    @Override
    public void onError(int i, String s) {
        LogUtil.e("login error : code " + i + " " + s);
        TLSService.getInstance().setLastErrno(-1);
        switch (i) {
            case 6208:
                //离线状态下被其他终端踢下线
                NotifyDialog dialog = new NotifyDialog();
                dialog.show(getString(R.string.kick_logout), getSupportFragmentManager(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        navToHome();
                    }
                });
                break;
            case 6200:
                Toast.makeText(this, getString(R.string.login_error_timeout), Toast.LENGTH_SHORT).show();
                navToLogin();
                break;
            default:
                Toast.makeText(this, getString(R.string.login_error), Toast.LENGTH_SHORT).show();
                navToLogin();
                break;
        }
    }

    @Override
    public void onSuccess() {
        LogUtil.i("连接成功");
        TLSService.getInstance().setLastErrno(0);
        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
        finish();
    }


    public void navToHome() {
        LogUtil.i("tx进入主页面");
        //    startActivity(new Intent(SplashActivity.this, TXIMHomeActivity.class));


        if (!TextUtils.isEmpty(SPUtils.getString("sig", null))) {
            init_txim();
        } else {
            LogUtil.i("sig=null");
            navToLogin();
        }
    }


    public void navToLogin() {
        LogUtil.i("tx去登录");
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }


    public boolean isUserLogin() {
        LogUtil.i("tx已有登录");
        LogUtil.i((UserInfo.getInstance().getId() != null && (!TLSService.getInstance().needLogin(UserInfo.getInstance().getId()))) + "@@@@" + (UserInfo.getInstance().getId() + (!TLSService.getInstance().needLogin(UserInfo.getInstance().getId()))));

        return UserInfo.getInstance().getId() != null && (!TLSService.getInstance().needLogin(UserInfo.getInstance().getId()));


    }

    /**
     * get sdk version
     */
//    private String getVersion() {
//        return "3.4";
//    }
}
