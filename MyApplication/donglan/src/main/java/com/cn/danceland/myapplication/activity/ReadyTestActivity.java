package com.cn.danceland.myapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.ReadyTestBean;
import com.cn.danceland.myapplication.bean.RequestInfoBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.MyJsonObjectRequest;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.cn.danceland.myapplication.view.CustomDatePicker;
import com.google.gson.Gson;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by feng on 2018/1/2.
 */

public class ReadyTestActivity extends BaseActivity {
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
    View inflate;
    LoopView loopview;
    AlertDialog.Builder alertdialog;
    TextView tv_start,over_time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.readytest);
        initHost();
        initView();
        initData();
    }

    private void initView() {

        tv_start = inflate.findViewById(R.id.tv_start);
        tv_start.setVisibility(View.GONE);
        loopview = inflate.findViewById(R.id.loopview);
        over_time = inflate.findViewById(R.id.over_time);
        over_time.setVisibility(View.GONE);

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
                commit();
            }
        });

        ed_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate();
            }
        });

        ed_height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWH();
            }
        });
    }

    private void showDate(){

        final CustomDatePicker customDatePicker = new CustomDatePicker(this,"请选择生日");
        customDatePicker.showWindow();
        customDatePicker.setDialogOnClickListener(new CustomDatePicker.OnClickEnter() {
            @Override
            public void onClick() {
                String dateString = customDatePicker.getDateStringF();
                ed_birthday.setText(dateString);
            }
        });

    }

    private void showWH(){
        int n;
        final ArrayList<String> arrayList = new ArrayList<String>();
        ViewGroup parent = (ViewGroup)inflate.getParent();
        if(parent!=null){
            parent.removeAllViews();
        }

        for(int i = 0;i<71;i++){
            n = 150+i;
            arrayList.add(n+"");
        }

        loopview.setNotLoop();
        loopview.setItems(arrayList);
        //设置初始位置
        loopview.setInitPosition(0);
        loopview.setTextSize(18);
        loopview.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                ed_height.setText(arrayList.get(index));
            }
        });
        alertdialog.setTitle("选择身高");

        alertdialog.setView(inflate);
        alertdialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertdialog.show();
    }

    private void commit(){
        if(ed_name.getText().toString().isEmpty()){
            ToastUtils.showToastShort("请填写姓名");
            return;
        }else {
            readyTestBean.setCname(ed_name.getText().toString());
        }
        if(ed_birthday.getText().toString().isEmpty()){
            ToastUtils.showToastShort("请选择生日");
            return;
        }else{
            readyTestBean.setBirthday(ed_birthday.getText().toString());
        }

        if(ed_height.getText().toString().isEmpty()){
            ToastUtils.showToastShort("请选择身高");
            return;
        }else{
            readyTestBean.setHeight(Float.valueOf(ed_height.getText().toString()));
        }

        readyTestBean.setGender(gender);

        readyTestBean.setId(Integer.valueOf(id));

        String strbean = gson.toJson(readyTestBean);

        MyJsonObjectRequest jsonObjectRequest = new MyJsonObjectRequest(Request.Method.POST, Constants.REAYTEST, strbean, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if(jsonObject.toString().contains("true")){
                    startActivity(new Intent(ReadyTestActivity.this,EquipmentActivity.class).putExtra("memberId",memberId).putExtra("member_no",member_no));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.i(volleyError.toString());
            }
        });
        MyApplication.getHttpQueues().add(jsonObjectRequest);
    }

    private void initHost() {
        inflate = LayoutInflater.from(this).inflate(R.layout.timeselect, null);
        alertdialog = new AlertDialog.Builder(this);
        readyTestBean = new ReadyTestBean();
        id = getIntent().getStringExtra("id");

        memberId = getIntent().getStringExtra("memberId");
        member_no = getIntent().getStringExtra("member_no");

        gson = new Gson();
        gender = 1;
    }

    private void initData() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.QUERY_USER_DYN_INFO_URL + id, new Response.Listener<String>() {
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
            Glide.with(ReadyTestActivity.this).load(data.getPerson().getSelf_avatar_path()).into(circleimage);
            nicheng.setText(data.getPerson().getNick_name());
            tv_phone.setText(data.getPerson().getPhone_no());
            ed_birthday.setText(data.getPerson().getBirthday());
            ed_height.setText(data.getPerson().getHeight());
            if(data.getPerson().getGender().equals("1")){
                tv_blue.setVisibility(View.VISIBLE);
                tv_gray.setVisibility(View.GONE);
                tv_female_blue.setVisibility(View.GONE);
                tv_female_gray.setVisibility(View.VISIBLE);
            }else if(data.getPerson().getGender().equals("2")){
                tv_blue.setVisibility(View.GONE);
                tv_gray.setVisibility(View.VISIBLE);
                tv_female_blue.setVisibility(View.VISIBLE);
                tv_female_gray.setVisibility(View.GONE);
            }
        }
    }

}
