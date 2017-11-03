package com.cn.danceland.myapplication;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.cn.danceland.myapplication.utils.LocationService;

/**
 * Created by shy on 2017/9/30 09:27
 * Email:644563767@qq.com
 */


public class MyApplication extends Application {
    private static RequestQueue requestQueue;
    public static Context applicationContext;
    private static MyApplication instance;
    public LocationService locationClient;

    @Override
    public void onCreate() {
        super.onCreate();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        locationClient = new LocationService(getApplicationContext());
        SDKInitializer.initialize(getApplicationContext());
        applicationContext = this;
        instance = this;
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
}
