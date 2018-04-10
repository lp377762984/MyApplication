package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.CourseMemberBean;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.SiJiaoYuYueConBean;
import com.cn.danceland.myapplication.bean.TuanKeBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.CustomGridView;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.NestedExpandaleListView;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.google.gson.Gson;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by feng on 2018/1/12.
 */

public class TuanKeDetailActivity extends Activity {
    ImageView kecheng_img,img_1,img_2,img_3,tuanke_back,down_img,up_img;
    NestedExpandaleListView kecheng_ex;
    int groupId;
    Gson gson;
    TextView kecheng_name,kecheng_time,kecheng_place,tv_jieshao,
    kecheng_room,course_type,tv_tuanke_title;
    Data data;
    String yuyueStartTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smalltuankedetail_activity);
        initHost();
        initView();
        getPeople();
    }

    private void initHost() {

        gson = new Gson();

        groupId = getIntent().getIntExtra("groupId",999);
        data = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
        yuyueStartTime = getIntent().getStringExtra("yuyueStartTime");


    }

    private void initView() {
        tuanke_back = findViewById(R.id.small_back);
        tuanke_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        kecheng_img =  findViewById(R.id.course_img);
        kecheng_name = findViewById(R.id.course_name);
        kecheng_time = findViewById(R.id.course_length);
        kecheng_place = findViewById(R.id.course_place);
        kecheng_room = findViewById(R.id.course_room);
        tv_jieshao = findViewById(R.id.tv_content);
        course_type = findViewById(R.id.course_type);
        tv_tuanke_title = findViewById(R.id.tv_tuanke_title);
        tv_tuanke_title.setText("免费团课");
        course_type.setText("免费团课");


        img_1 = findViewById(R.id.pic_01);
        img_2 = findViewById(R.id.pic_02);
        img_3 = findViewById(R.id.pic_03);


        kecheng_ex = findViewById(R.id.my_expanda);
        kecheng_ex.setAdapter(new MyAdapter());
        kecheng_ex.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                down_img = v.findViewById(R.id.down_img);
                up_img = v.findViewById(R.id.up_img);
                if(down_img.getVisibility()==View.GONE){
                    down_img.setVisibility(View.VISIBLE);
                    up_img.setVisibility(View.GONE);
                }else{
                    down_img.setVisibility(View.GONE);
                    up_img.setVisibility(View.VISIBLE);
                }

                return false;
            }
        });
        if(groupId!=999){
            getData(groupId);
        }
    }

    public void initData(TuanKeBean.Data detailData){

        RequestOptions  options =new RequestOptions().placeholder(R.drawable.error_pic);
        Glide.with(TuanKeDetailActivity.this).load(detailData.getCover_img_url()).apply(options).into(kecheng_img);
        Glide.with(TuanKeDetailActivity.this).load(detailData.getCourse_img_url_1()).apply(options).into(img_1);
        Glide.with(TuanKeDetailActivity.this).load(detailData.getCourse_img_url_2()).apply(options).into(img_2);
        Glide.with(TuanKeDetailActivity.this).load(detailData.getCourse_img_url_3()).apply(options).into(img_3);


        String startTime,endTime;
        if(detailData.getStart_time()%60==0){
            startTime = detailData.getStart_time()/60+":00";
        }else{
            startTime = detailData.getStart_time()/60+":"+detailData.getStart_time()%60;
        }

        if(detailData.getEnd_time()%60==0){
            endTime = detailData.getEnd_time()/60+":00";
        }else{
            endTime = detailData.getEnd_time()/60+":"+detailData.getEnd_time()%60;
        }
        kecheng_time.setText("上课时间:"+startTime+"-"+endTime);
        kecheng_place.setText("上课场馆:"+detailData.getBranch_name());
        kecheng_room.setText("上课场地:"+detailData.getRoom_name());
        kecheng_name.setText(detailData.getCourse_type_name());
        tv_jieshao.setText(detailData.getCourse_describe());


    }


    private void getPeople(){

        SiJiaoYuYueConBean siJiaoYuYueConBean = new SiJiaoYuYueConBean();
        siJiaoYuYueConBean.setGroup_course_id(groupId);
        siJiaoYuYueConBean.setDate(yuyueStartTime);
        siJiaoYuYueConBean.setPage(0);
        siJiaoYuYueConBean.setSize(6);

        String s = gson.toJson(siJiaoYuYueConBean);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.FINDFREEGROUPCOURSEAPPLYPERSON,s ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {


                LogUtil.e("zzf",jsonObject.toString());

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


    private void getData(final int groupId){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.FREEGROUPCOURSE, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                TuanKeBean tuanKeBean = gson.fromJson(s, TuanKeBean.class);
                if(tuanKeBean!=null){
                    TuanKeBean.Data detailData = tuanKeBean.getData();
                    initData(detailData);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.e("zzf",volleyError.toString());
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("id",groupId+"");
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<String,String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN,""));
                return map;
            }
        };

        MyApplication.getHttpQueues().add(stringRequest);

    }

    private class MyAdapter extends BaseExpandableListAdapter{

        @Override
        public int getGroupCount() {
            return 1;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            if(convertView==null){
                convertView = LayoutInflater.from(TuanKeDetailActivity.this).inflate(R.layout.kecheng_parent,null);
            }

            CircleImageView circle_1 = convertView.findViewById(R.id.circle_1);
            CircleImageView circle_2 = convertView.findViewById(R.id.circle_2);
            CircleImageView circle_3 = convertView.findViewById(R.id.circle_3);
            CircleImageView circle_4 = convertView.findViewById(R.id.circle_4);
            CircleImageView circle_5 = convertView.findViewById(R.id.circle_5);
            CircleImageView circle_6 = convertView.findViewById(R.id.circle_6);

            Glide.with(TuanKeDetailActivity.this).load("http://news.hainan.net/Editor/img/201602/20160215/big/20160215234302136_2731088.jpg").into(circle_1);
            Glide.with(TuanKeDetailActivity.this).load("http://img06.tooopen.com/images/20160807/tooopen_sy_174504721543.jpg").into(circle_2);
            Glide.with(TuanKeDetailActivity.this).load("http://file06.16sucai.com/2016/0407/90ed68d09c8777d6336862beca17f317.jpg").into(circle_3);
            Glide.with(TuanKeDetailActivity.this).load("http://img1.juimg.com/160622/330831-1606220TG086.jpg").into(circle_4);
            Glide.with(TuanKeDetailActivity.this).load("http://img.mp.itc.cn/upload/20160408/6c46c0a65f32450e9941f9ef84091104_th.jpg").into(circle_5);
            Glide.with(TuanKeDetailActivity.this).load("http://img1.juimg.com/160622/330831-1606220TG086.jpg").into(circle_6);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            if(convertView==null){
                convertView = LayoutInflater.from(TuanKeDetailActivity.this).inflate(R.layout.kecheng_child,null);
            }
            CustomGridView grid_view = convertView.findViewById(R.id.grid_view);
            grid_view.setAdapter(new MyGridAdapter());

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }


    private class MyGridAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View inflate = LayoutInflater.from(TuanKeDetailActivity.this).inflate(R.layout.kecheng_grid_item, null);
            CircleImageView circle_item = inflate.findViewById(R.id.circle_item);
            Glide.with(TuanKeDetailActivity.this).load("http://pic1.win4000.com/wallpaper/d/58997071ac2b1.jpg").into(circle_item);

            return inflate;
        }
    }

}
