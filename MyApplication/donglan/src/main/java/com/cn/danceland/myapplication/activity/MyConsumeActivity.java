package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.MyConsumeCon;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.view.DongLanTitleView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by feng on 2018/5/2.
 */

public class MyConsumeActivity extends Activity {

    DongLanTitleView consume_title;
    ListView lv_consume;
    Gson gson;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_myconsume);
        initHost();
        initView();

    }

    private void initView() {

        consume_title = findViewById(R.id.consume_title);
        consume_title.setTitle("我的消费");
        lv_consume = findViewById(R.id.lv_consume);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载中...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        initData();
    }

    private void initData() {
        MyConsumeCon myConsumeCon = new MyConsumeCon();
        myConsumeCon.setPage(0);
        myConsumeCon.setSize(20);
        String s = gson.toJson(myConsumeCon);

        //progressDialog.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.MYCONSUME, s,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                //progressDialog.dismiss();
                LogUtil.i(jsonObject.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.i(volleyError.toString());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));
                return map;
            }

        };

        MyApplication.getHttpQueues().add(jsonObjectRequest);

    }

    private void initHost() {
        gson = new Gson();
    }
}
