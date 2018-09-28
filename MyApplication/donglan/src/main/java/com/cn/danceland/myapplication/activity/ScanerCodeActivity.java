package com.cn.danceland.myapplication.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.bean.RequestSimpleBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.MyStringRequest;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
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
        startActivity(new Intent(ScanerCodeActivity.this, ScanerCodeDetailActivity.class).putExtra("message", result));
        finish();
//        showConfirmDialog(result);
    }

    // BaseActivity中统一调用MobclickAgent 类的 onResume/onPause 接口
    // 子类中无需再调用
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); // 基础指标统计，不能遗漏
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this); // 基础指标统计，不能遗漏
    }

    private void scan_qrcode(final String result) {
        MyStringRequest request = new MyStringRequest(Request.Method.POST, Constants.SCAN_QRCODE, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                RequestSimpleBean requsetSimpleBean = new Gson().fromJson(s, RequestSimpleBean.class);
                if (requsetSimpleBean.getSuccess()) {
                    if (TextUtils.equals(requsetSimpleBean.getCode(), "0")) {
                        showResultDialog("入场成功");
                    } else {

                        showResultDialog(requsetSimpleBean.getErrorMsg());
                    }

                } else {

                    showResultDialog(requsetSimpleBean.getErrorMsg());

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.i(volleyError.toString());
                showResultDialog("入场失败:请查看网络连接");
            }
        }) {


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

    /**
     * 显示结果对话
     */
    private void showResultDialog(final String result) {
        final AlertDialog.Builder dialog =
                new AlertDialog.Builder(this);
        //   dialog.setTitle("提示");
        dialog.setMessage(result);
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
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
