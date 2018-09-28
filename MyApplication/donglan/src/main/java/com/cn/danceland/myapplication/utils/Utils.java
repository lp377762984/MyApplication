package com.cn.danceland.myapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * 工具类
 * Created by yxx on 2018-09-28.
 */

public class Utils {
    /**
     * 获取渠道名
     * * @param ctx 此处习惯性的设置为activity，实际上context就可以
     * * @return 如果没有获取成功，那么返回值为空
     */
    public static String getChannelName(Context ctx) {
        if (ctx == null) {
            return null;
        }
        String channelName = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {                //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelName = applicationInfo.metaData.getString("");
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channelName;
    }
}