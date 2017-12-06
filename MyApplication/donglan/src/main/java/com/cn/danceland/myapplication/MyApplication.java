package com.cn.danceland.myapplication;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.SDKInitializer;
import com.cn.danceland.myapplication.db.DaoMaster;
import com.cn.danceland.myapplication.db.DaoSession;
import com.cn.danceland.myapplication.utils.LocationService;
import com.danikula.videocache.HttpProxyCacheServer;

/**
 * Created by shy on 2017/9/30 09:27
 * Email:644563767@qq.com
 */


public class MyApplication extends Application {
    private static RequestQueue requestQueue;
    public static Context applicationContext;
    private static MyApplication instance;
    public LocationService locationClient;
    private DaoMaster.DevOpenHelper donglan;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;


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

        db = donglan.getWritableDatabase();

        daoMaster = new DaoMaster(db);

        daoSession = daoMaster.newSession();
    }

    public SQLiteDatabase getDb(){
        return db;
    }
    public DaoSession getDaoSession(){

        return daoSession;
    }

}
