package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.ReadyTestBean;
import com.cn.danceland.myapplication.bean.RequestInfoBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by feng on 2018/1/2.
 */

public class ReadyTestActivity extends Activity {
    String id;
    CircleImageView circleimage;
    TextView nicheng,tv_phone,tv_gray,tv_blue,tv_female_gray,tv_female_blue;
    EditText ed_birthday,ed_height,ed_name;
    Gson gson;
    ImageView readytest_back;
    int gender;
    RelativeLayout rl_connect;
    ReadyTestBean readyTestBean;
    private Calendar c = null;
    String memberId,member_no;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.readytest);
        initHost();
        initView();
        initData();
    }

    private void initView() {

        rl_connect = findViewById(R.id.rl_connect);
        circleimage = findViewById(R.id.circleimage);
        nicheng = findViewById(R.id.nicheng);
        tv_phone = findViewById(R.id.tv_phone);
        tv_gray = findViewById(R.id.tv_gray);
        tv_blue = findViewById(R.id.tv_blue);
        tv_female_gray = findViewById(R.id.tv_female_gray);
        tv_female_blue = findViewById(R.id.tv_female_blue);
        ed_birthday = findViewById(R.id.ed_birthday);
        ed_height = findViewById(R.id.ed_height);
        ed_name = findViewById(R.id.ed_name);
        readytest_back = findViewById(R.id.readytest_back);
        readytest_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_gray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_blue.setVisibility(View.VISIBLE);
                tv_gray.setVisibility(View.GONE);
                tv_female_blue.setVisibility(View.GONE);
                tv_female_gray.setVisibility(View.VISIBLE);
                gender = 1;
            }
        });
        tv_female_gray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_blue.setVisibility(View.GONE);
                tv_gray.setVisibility(View.VISIBLE);
                tv_female_blue.setVisibility(View.VISIBLE);
                tv_female_gray.setVisibility(View.GONE);
                gender = 2;
            }
        });

        rl_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(gender==1||gender==2){
                        commit();
                    }else{
                        ToastUtils.showToastShort("请选择性别！");
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                if(gender==1||gender==2){
                    startActivity(new Intent(ReadyTestActivity.this,EquipmentActivity.class).putExtra("memberId",memberId).putExtra("member_no",member_no));
                    //finish();
                }
            }
        });

        ed_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                new DatePickerDialog(ReadyTestActivity.this,new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
                        //et.setText("您选择了：" + year + "年" + (month+1) + "月" + dayOfMonth + "日");
                        ed_birthday.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                    }
                }, c.get(Calendar.YEAR), // 传入年份
                        c.get(Calendar.MONTH), // 传入月份
                        c.get(Calendar.DAY_OF_MONTH) // 传入天数
                ).show();
            }
        });
    }

    private void commit() throws JSONException {
        readyTestBean.setCname(ed_name.getText().toString());
        readyTestBean.setGender(gender);
        readyTestBean.setBirthday(ed_birthday.getText().toString());
        readyTestBean.setId(Integer.valueOf(id));
        readyTestBean.setHeight(Float.valueOf(ed_height.getText().toString()));
        final String strbean = gson.toJson(readyTestBean);
        JSONObject jsonObject = new JSONObject(strbean);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.REAYTEST, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<String,String>();
                map.put("Authorization",SPUtils.getString(Constants.MY_TOKEN,""));
                return map;
            }
        };
        MyApplication.getHttpQueues().add(jsonObjectRequest);
    }

    private void initHost() {

        readyTestBean = new ReadyTestBean();
        id = getIntent().getStringExtra("id");

        memberId = getIntent().getStringExtra("memberId");
        member_no = getIntent().getStringExtra("member_no");

        gson = new Gson();

    }

    private void initData() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.QUERY_USERINFO_URL+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                RequestInfoBean requestInfoBean = gson.fromJson(s, RequestInfoBean.class);
                if(requestInfoBean!=null){
                    Data data = requestInfoBean.getData();
                    initDatas(data);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToastShort("请检查手机网络！");
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<String,String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN,""));
                return map;
            }
        };

        MyApplication.getHttpQueues().add(stringRequest);

    }

    private void initDatas(Data data){

        if(data!=null){
            Glide.with(ReadyTestActivity.this).load(data.getSelfAvatarPath()).into(circleimage);
            nicheng.setText(data.getNickName());
            tv_phone.setText(data.getPhone_no());
            ed_birthday.setText(data.getBirthday());
            ed_height.setText(data.getHeight());
            if(data.getGender().equals("1")){
                tv_blue.setVisibility(View.VISIBLE);
                tv_gray.setVisibility(View.GONE);
                tv_female_blue.setVisibility(View.GONE);
                tv_female_gray.setVisibility(View.VISIBLE);
            }else if(data.getGender().equals("2")){
                tv_blue.setVisibility(View.GONE);
                tv_gray.setVisibility(View.VISIBLE);
                tv_female_blue.setVisibility(View.VISIBLE);
                tv_female_gray.setVisibility(View.GONE);
            }
        }
    }

}
