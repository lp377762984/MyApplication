package com.cn.danceland.myapplication.activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.TestingBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by feng on 2018/1/3.
 */

public class TestingActivity extends Activity {
    ImageView test_saomiao;
    Timer timer;
    Task task;
    static Handler handler;
    String eqId;
    Gson gson;
    String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testing);
        initHost();
        initView();
    }

    private void initHost() {

        gson = new Gson();
        eqId = getIntent().getStringExtra("deviceId");
        id = getIntent().getStringExtra("id");
        handler = new Handler();
        timer = new Timer();
        task = new Task();
        //schedule 计划安排，时间表
        timer.schedule(task, 1000, 1000);
    }

    private class Task extends TimerTask {
        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    getStatus();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    private void getStatus(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.ISFINISHED, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                TestingBean testingBean = gson.fromJson(s, TestingBean.class);
                if(testingBean!=null){
                    if(testingBean.getData().getStatus()==1){
                        startActivity(new Intent(TestingActivity.this,FitnessHistoryActivity.class).putExtra("id",id));
                        finish();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<String,String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN,""));
                return map;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String,String> map = new HashMap<String,String>();
                map.put("deviceId",eqId);

                return map;
            }
        };
        MyApplication.getHttpQueues().add(stringRequest);
    }

    private void initView() {
        test_saomiao = findViewById(R.id.test_saomiao);
        ObjectAnimator animator = ObjectAnimator.ofFloat(test_saomiao,"rotation",0f,360f);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(2000);
        animator.setRepeatCount(-1);
        animator.start();

    }
}