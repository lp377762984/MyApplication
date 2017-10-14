package com.cn.danceland.myapplication.utils;

import android.widget.Toast;

import com.cn.danceland.myapplication.MyApplication;

/**
 * Toast封装
 */
public class ToastUtils {
    public static void showToastShort(String text) {
        Toast.makeText(MyApplication.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    public static void showToastLong(String text) {
        Toast.makeText(MyApplication.getContext(), text, Toast.LENGTH_LONG).show();
    }
}
