package com.cn.danceland.myapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharePreference封装
 * 
 * 
 */
public class SPUtils {
	public static final String PREF_NAME = "saveInfo";


	public static boolean getBoolean(Context ctx, String key, boolean defaultValue) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		return sp.getBoolean(key, defaultValue);
	}

	public static void setBoolean(Context ctx, String key, boolean value) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();

	}

	public static String getString(Context ctx, String key, String defaultValue) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		return sp.getString(key, defaultValue);
	}

	public static void setString(Context ctx, String key, String value) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();

	}

	public static int getInt(Context ctx, String key, int defaultValue) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		return sp.getInt(key, defaultValue);
	}

	public static void setInt(Context ctx, String key, int value) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		sp.edit().putInt(key, value).commit();

	}

	public static void remove(Context ctx, String key) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		sp.edit().remove(key).commit();

	}
	// 退出登录时要调用
	public static void clean(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		try {

			if (null != sp) {
				sp.edit().clear().commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
