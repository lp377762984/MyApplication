package com.cn.danceland.myapplication.utils;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ${yxx} on 2018/8/23.
 */

public class ViewUtils {
    /**
     * 设置margin
     * @param v view
     * @param l left
     * @param t top
     * @param r right
     * @param b bottom
     */
    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }
}
