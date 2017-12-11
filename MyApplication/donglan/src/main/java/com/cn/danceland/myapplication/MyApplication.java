package com.cn.danceland.myapplication;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.SDKInitializer;
import com.cn.danceland.myapplication.activity.HomeActivity;
import com.cn.danceland.myapplication.activity.MainActivity;
import com.cn.danceland.myapplication.db.DaoMaster;
import com.cn.danceland.myapplication.db.DaoSession;
import com.cn.danceland.myapplication.utils.LocationService;
import com.danikula.videocache.HttpProxyCacheServer;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

/**
 * Created by shy on 2017/9/30 09:27
 * Email:644563767@qq.com
 */


public class MyApplication extends Application {
    private static RequestQueue requestQueue;
    public static Context applicationContext;
    private static MyApplication instance;
    public LocationService locationClient;
    private DaoMaster.DevOpenHelper donglan,message;
    private SQLiteDatabase db,messagedb;
    private DaoMaster daoMaster,messageMaster;
    private DaoSession daoSession,messageSession;
    // user your appid the key.
    private static final String APP_ID = "1000270";
    // user your appid the key.
    private static final String APP_KEY = "670100056270";
    //private static DemoHandler sHandler = null;
    private static HomeActivity sMainActivity = null;

    private HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy(Context context) {
        MyApplication app = (MyApplication) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {

        return new HttpProxyCacheServer.Builder(this)
                .maxCacheSize(1024 * 1024 * 200)       // 200M for cache
                .build();

    }

    @Override
    public void onCreate() {
        super.onCreate();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        locationClient = new LocationService(getApplicationContext());
        SDKInitializer.initialize(this);
        applicationContext = this;
        instance = this;
        setUpDb();
        // 注册push服务，注册成功后会向DemoMessageReceiver发送广播
        // 可以从DemoMessageReceiver的onCommandResult方法中MiPushCommandMessage对象参数中获取注册信息
//        if (shouldInit()) {
//            MiPushClient.registerPush(this, APP_ID, APP_KEY);
//        }
////        if (sHandler == null) {
////            sHandler = new DemoHandler(getApplicationContext());
////        }
    }

    public static RequestQueue getHttpQueues() {
        return requestQueue;
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return applicationContext;
    }

    private void setUpDb(){

        donglan = new DaoMaster.DevOpenHelper(this, "donglan", null);
        message = new DaoMaster.DevOpenHelper(this, "message", null);

        db = donglan.getWritableDatabase();
        messagedb = message.getWritableDatabase();

        daoMaster = new DaoMaster(db);
        messageMaster = new DaoMaster(messagedb);

        daoSession = daoMaster.newSession();
        messageSession = messageMaster.newSession();
    }

    public SQLiteDatabase getDb(){
        return db;
    }
    public SQLiteDatabase getMessageDb(){
        return messagedb;
    }
    public DaoSession getDaoSession(){

        return daoSession;
    }
    public DaoSession getMessageDaoSession(){

        return messageSession;
    }


    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
//    public static class DemoHandler extends Handler {
//
//        private Context context;
//
//        public DemoHandler(Context context) {
//            this.context = context;
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            String s = (String) msg.obj;
////            if (sMainActivity != null) {
////                sMainActivity.refreshLogInfo();
////            }
//            if (!TextUtils.isEmpty(s)) {
//
//            }
//        }
//    }
}
