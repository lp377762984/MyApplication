package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.map.Text;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.ShopDetailBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by feng on 2017/11/29.
 */

public class ShopDetailedActivity extends Activity{
    Button bt_back;
    RequestQueue requestQueue;
    Gson gson;
    TextView tv_adress,tv_time,tv_detail,store_name;
    String phoneNo;
    ImageView detail_phone,detail_adress;
    String jingdu,weidu,shopJingdu,shopWeidu;
    RelativeLayout s_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopdetailed);
        initHost();
        initView();
    }

    private void initHost() {

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        gson = new Gson();
        jingdu = getIntent().getStringExtra("jingdu");
        weidu = getIntent().getStringExtra("weidu");
        shopJingdu = getIntent().getStringExtra("shopJingdu");
        shopWeidu = getIntent().getStringExtra("shopWeidu");

    }

    private void initView() {

        tv_adress = findViewById(R.id.tv_adress);
        tv_time = findViewById(R.id.tv_time);
        tv_detail= findViewById(R.id.tv_detail);
        store_name = findViewById(R.id.store_name);
        detail_phone = findViewById(R.id.detail_phone);
        detail_adress = findViewById(R.id.detail_adress);

        s_button = findViewById(R.id.s_button);

        bt_back = findViewById(R.id.bt_back);
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        detail_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        detail_adress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                intent.putExtra("shopJingdu",shopJingdu);
                intent.putExtra("shopWeidu",shopWeidu);
                intent.putExtra("jingdu",jingdu);
                intent.putExtra("weidu",weidu);
                startActivity(intent);
            }
        });
        s_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SellCardActivity.class);
                startActivity(intent);
            }
        });

        getShopDetail();
    }

    private void getShopDetail(){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.BRANCH + "/1", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                ShopDetailBean shopDetailBean = gson.fromJson(s, ShopDetailBean.class);
                ShopDetailBean.Data data = shopDetailBean.getData();
                store_name.setText(data.getBname());
                tv_adress.setText(data.getAddress());
                tv_detail.setText(data.getDescription());
                phoneNo = data.getTelphone_no();
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
        };
        requestQueue.add(stringRequest);
    }

    /**
     * 提示
     */
    private void showDialog() {
        AlertDialog.Builder dialog =
                new AlertDialog.Builder(ShopDetailedActivity.this);
        dialog.setTitle("提示");
        dialog.setMessage("是否呼叫" + phoneNo);
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                call(phoneNo);
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }
    /**
     * 调用拨号功能
     *
     * @param phone 电话号码
     */
    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}
