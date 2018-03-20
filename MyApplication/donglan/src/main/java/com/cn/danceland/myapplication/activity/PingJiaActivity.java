package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.EvaluateInfoBean;
import com.cn.danceland.myapplication.bean.MyCourseBean;
import com.cn.danceland.myapplication.bean.PingJiaCon;
import com.cn.danceland.myapplication.bean.RootBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.TimeUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by feng on 2018/3/19.
 */

public class PingJiaActivity extends Activity {

    CircleImageView img_user;
    TextView tv_user,tv_role,tv_status,tv_time;
    LinearLayout ll_detail,ll_sixin,ll_phone,ll_commmit;
    ScaleRatingBar jiaolian_ratingbar,kecheng_ratingbar,changdi_ratingbar;
    float jiaolianscore,kechengscore,changdiscore;
    MyCourseBean.Data item;
    Gson gson;
    ImageView pingjia_back;
    String currentTime;
    EditText my_pingjia;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pingjia_activity);
        initHost();
        initView();

    }

    private void initHost() {

        item = (MyCourseBean.Data)getIntent().getSerializableExtra("item");

        currentTime = TimeUtils.timeStamp2Date(System.currentTimeMillis() + "", "yyyy-MM-dd HH:mm:ss");

        gson = new Gson();
    }

    private void initView() {

        img_user  = findViewById(R.id.img_user);
        tv_user = findViewById(R.id.tv_user);
        tv_role = findViewById(R.id.tv_role);
        ll_detail = findViewById(R.id.ll_detail);
        ll_sixin = findViewById(R.id.ll_sixin);
        ll_phone = findViewById(R.id.ll_phone);
        tv_status = findViewById(R.id.tv_status);
        tv_time = findViewById(R.id.tv_time);
        jiaolian_ratingbar = findViewById(R.id.jiaolian_ratingbar);
        kecheng_ratingbar = findViewById(R.id.kecheng_ratingbar);
        changdi_ratingbar = findViewById(R.id.changdi_ratingbar);
        ll_commmit = findViewById(R.id.ll_commmit);
        pingjia_back = findViewById(R.id.pingjia_back);
        tv_time.setText(currentTime);
        my_pingjia = findViewById(R.id.my_pingjia);

        setClick();

    }

    private void setClick() {

        jiaolian_ratingbar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar baseRatingBar, float v) {
                jiaolianscore = v;
            }
        });
        kecheng_ratingbar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar baseRatingBar, float v) {
                kechengscore = v;
            }
        });
        changdi_ratingbar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar baseRatingBar, float v) {
                changdiscore = v;
            }
        });

        ll_commmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        pingjia_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void commit() throws JSONException {

        String text = my_pingjia.getText().toString();

        List<PingJiaCon> list = new ArrayList<PingJiaCon>();
        PingJiaCon jiaolianpingJiaCon = new PingJiaCon();
        jiaolianpingJiaCon.setBranch_id(item.getBranch_id());
        jiaolianpingJiaCon.setBus_id(item.getEmployee_id());
        jiaolianpingJiaCon.setScore(jiaolianscore);
        jiaolianpingJiaCon.setType("2");
        jiaolianpingJiaCon.setContent(text);

        PingJiaCon kechengpingJiaCon = new PingJiaCon();
        kechengpingJiaCon.setBranch_id(item.getBranch_id());
        kechengpingJiaCon.setBus_id(item.getCourse_type_id());
        kechengpingJiaCon.setScore(kechengscore);
        kechengpingJiaCon.setType("1");
        kechengpingJiaCon.setContent(text);

        list.add(jiaolianpingJiaCon);
        list.add(kechengpingJiaCon);

        EvaluateInfoBean evaluateInfoBean = new EvaluateInfoBean();
        evaluateInfoBean.setList(list);
        String s = gson.toJson(evaluateInfoBean);

        JSONObject jsonObject = new JSONObject(s);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.PINGJIA,jsonObject , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if(jsonObject.toString().contains("true")){
                    ToastUtils.showToastShort("评价成功！");
                    finish();
                }else{
                    ToastUtils.showToastShort("评价失败！");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.e("zzf",volleyError.toString());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<String,String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN,""));
                return map;
            }
        };

        MyApplication.getHttpQueues().add(jsonObjectRequest);

    }


}
