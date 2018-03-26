package com.cn.danceland.myapplication.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.bean.RequsetSimpleBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;
import com.vondear.rxtools.activity.ActivityScanerCode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shy on 2018/3/26 14:07
 * Email:644563767@qq.com
 * 扫码并处理
 */


public class ScanerCodeActivity extends ActivityScanerCode {


    @Override
    public void do_result(String result) {
        LogUtil.i(result);

        showConfirmDialog(result);
    }

    private void scan_qrcode(final String result) {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.SCAN_QRCODE, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                RequsetSimpleBean requsetSimpleBean=new Gson().fromJson(s,RequsetSimpleBean.class);
                if (requsetSimpleBean.isSuccess()){
                    ToastUtils.showToastShort("入场成功");
                    finish();
                }else {
                    ToastUtils.showToastShort("入场失败");
                    finish();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.i(volleyError.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));
                LogUtil.i( SPUtils.getString(Constants.MY_TOKEN, ""));
                return map;

            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("qrcode", result);
                return map;

            }
        };
        MyApplication.getHttpQueues().add(request);
    }


    /**
     * 确认对话
     */
    private void showConfirmDialog(final String result) {
        AlertDialog.Builder dialog =
                new AlertDialog.Builder(this);
        //   dialog.setTitle("提示");
        dialog.setMessage("是否入场");
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                scan_qrcode(result);

            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        dialog.show();
    }


}
