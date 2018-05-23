package com.cn.danceland.myapplication;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.SDKInitializer;
import com.cn.danceland.myapplication.activity.HomeActivity;
import com.cn.danceland.myapplication.db.DaoMaster;
import com.cn.danceland.myapplication.db.DaoSession;
import com.cn.danceland.myapplication.im.utils.Foreground;
import com.cn.danceland.myapplication.shouhuan.service.BluetoothLeService;
import com.cn.danceland.myapplication.utils.LocationService;
import com.danikula.videocache.HttpProxyCacheServer;
import com.tencent.imsdk.TIMGroupReceiveMessageOpt;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMOfflinePushListener;
import com.tencent.imsdk.TIMOfflinePushNotification;
import com.tencent.qalsdk.sdk.MsfSdkUtils;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

/**
 * Created by shy on 2017/9/30 09:27
 * Email:644563767@qq.com
 */


public class MyApplication extends android.support.multidex.MultiDexApplication {
    private static RequestQueue requestQueue;
    public static Context applicationContext;
    private static MyApplication instance;
    public LocationService locationClient;
    private DaoMaster.DevOpenHelper donglan, message;
    private SQLiteDatabase db, messagedb;
    private DaoMaster daoMaster, messageMaster;
    private DaoSession daoSession, messageSession;
    // 小米推送ID.
    private static final String APP_ID = "2882303761517681383";
    // 小米推送KEY.
    private static final String APP_KEY = "5681768120383";
    //private static DemoHandler sHandler = null;
    private static HomeActivity sMainActivity = null;

    private HttpProxyCacheServer proxy;
    private static Activity currentActivity;

    public static BluetoothLeService mBluetoothLeService;//蓝牙连接服务
    public static boolean mBluetoothConnected = false;
    public static boolean isBluetoothConnecting = false;


    public static HttpProxyCacheServer getProxy(Context context) {
        MyApplication app = (MyApplication) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {

        return new HttpProxyCacheServer.Builder(this)
                .maxCacheSize(1024 * 1024 *1024)       // 1G for cache
                .build();

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onCreate() {
        super.onCreate();
        Foreground.init(this);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        locationClient = new LocationService(getApplicationContext());
        SDKInitializer.initialize(this);
        applicationContext = this;
        instance = this;
        setUpDb();
        // 注册push服务，注册成功后会向DemoMessageReceiver发送广播
        // 可以从DemoMessageReceiver的onCommandResult方法中MiPushCommandMessage对象参数中获取注册信息
        if (shouldInit()) {
            MiPushClient.registerPush(this, APP_ID, APP_KEY);
        }
////        if (sHandler == null) {
////            sHandler = new DemoHandler(getApplicationContext());
////        }


        // EaseUI.getInstance().init(applicationContext, null);
//在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        //   EMClient.getInstance().setDebugMode(true);

        //DemoHelper.getInstance().init(applicationContext);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }
   //    initTXIM();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            bindBleService();
        }

        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {


            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                currentActivity = activity;
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
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

    private void setUpDb() {

        donglan = new DaoMaster.DevOpenHelper(this, "donglan", null);
        message = new DaoMaster.DevOpenHelper(this, "message", null);

        db = donglan.getWritableDatabase();
        messagedb = message.getWritableDatabase();

        daoMaster = new DaoMaster(db);
        messageMaster = new DaoMaster(messagedb);

        daoSession = daoMaster.newSession();
        messageSession = messageMaster.newSession();
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public SQLiteDatabase getMessageDb() {
        return messagedb;
    }

    public DaoSession getDaoSession() {

        return daoSession;
    }

    public DaoSession getMessageDaoSession() {

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

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e("zgy", "Unable to initialize Bluetooth");

            }
            // Automatically connects to the device upon successful start-up initialization.
//            mBluetoothLeService.connect(mDeviceAddress);
            Log.d("zgy", "onServiceConnected");

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };
    private void bindBleService() {
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }



    private void initTXIM() {
        if (MsfSdkUtils.isMainProcess(this)) {
            TIMManager.getInstance().setOfflinePushListener(new TIMOfflinePushListener() {
                @Override
                public void handleNotification(TIMOfflinePushNotification timOfflinePushNotification) {
                    if (timOfflinePushNotification.getGroupReceiveMsgOpt() == TIMGroupReceiveMessageOpt.ReceiveAndNotify) {
                        //消息被设置为需要需要提醒
                        timOfflinePushNotification.doNotify(getApplicationContext(), R.mipmap.app_launcher);
                        //注册推送服务
                    }
                }
            });
        }
//        TIMSdkConfig config = new TIMSdkConfig(Constant.SDK_APPID).enableCrashReport(false).enableLogPrint(true)
//                .setLogLevel(TIMLogLevel.DEBUG)
//                .setLogPath(Environment.getExternalStorageDirectory().getPath() + "/donglan/log");
//        boolean b = TIMManager.getInstance().init(this, config);
    }
}
