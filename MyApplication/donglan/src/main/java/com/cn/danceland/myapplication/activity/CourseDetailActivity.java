package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.CourseMemberBean;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.JiaoLianCourseBean;
import com.cn.danceland.myapplication.bean.MyCourseBean;
import com.cn.danceland.myapplication.bean.SiJiaoYuYueConBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.CustomGridView;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.NestedExpandaleListView;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.StringUtils;
import com.cn.danceland.myapplication.utils.TimeUtils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by feng on 2018/3/30.
 */

public class CourseDetailActivity extends Activity {

    String startTime,endTime,startTimeTv,endTimeTv;
    Data data;
    String role;
    String auth;
    MyCourseBean.Data item;
    JiaoLianCourseBean.Content item1;
    TextView course_name,course_length,course_place,course_room,
    course_jiaolian_huiyuan_name,course_renshu,tv_kecheng_fenshu,tv_jiaolian_fenshu,tv_changdi_fenshu,
            tv_content;
    ImageView course_img,course_back;
    CircleImageView course_jiaolian_huiyuan_circle;
    RelativeLayout rl_button_yuyue;
    NestedExpandaleListView my_expanda;
    ImageView down_img,up_img;
    Gson gson;
    CourseMemberBean courseMemberBean;
    List<CourseMemberBean.Content> headList,childList;
    MyAdapter myAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.coursedetail_activity);
        initHost();

        initView();
    }

    private void initHost() {

        data = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
        startTimeTv = getIntent().getStringExtra("startTime");
        startTime = TimeUtils.date2TimeStamp(startTimeTv+" 00:00:00", "yyyy-MM-dd 00:00:00")+"";
        endTimeTv = getIntent().getStringExtra("endTime");
        role = getIntent().getStringExtra("role");
        auth = getIntent().getStringExtra("auth");
        if(role!=null){
            item1 = (JiaoLianCourseBean.Content)getIntent().getSerializableExtra("item");
        }else{
            item = (MyCourseBean.Data)getIntent().getSerializableExtra("item");
        }


        gson = new Gson();
        myAdapter = new MyAdapter();

    }

    private class MyAdapter extends BaseExpandableListAdapter {

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
                convertView = LayoutInflater.from(CourseDetailActivity.this).inflate(R.layout.kecheng_parent,null);
            }

            CircleImageView circle_1 = convertView.findViewById(R.id.circle_1);
            CircleImageView circle_2 = convertView.findViewById(R.id.circle_2);
            CircleImageView circle_3 = convertView.findViewById(R.id.circle_3);
            CircleImageView circle_4 = convertView.findViewById(R.id.circle_4);
            CircleImageView circle_5 = convertView.findViewById(R.id.circle_5);
            CircleImageView circle_6 = convertView.findViewById(R.id.circle_6);


            CircleImageView[] imgArr = {circle_1,circle_2,circle_3,circle_4,circle_5,circle_6};

            if(headList!=null&&headList.size()>0){
                for(int i = 0;i<headList.size();i++){
                    if(headList.get(i).getSelf_avatar_path()==null||headList.get(i).getSelf_avatar_path().equals("")){
                        Glide.with(CourseDetailActivity.this).load(R.drawable.img_my_avatar).into(imgArr[i]);
                    }else{
                        Glide.with(CourseDetailActivity.this).load(headList.get(i).getSelf_avatar_path()).into(imgArr[i]);
                    }
                }
            }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            if(convertView==null){
                convertView = LayoutInflater.from(CourseDetailActivity.this).inflate(R.layout.kecheng_child,null);
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


    private class MyGridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return childList==null? 0:childList.size();
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
            View inflate = LayoutInflater.from(CourseDetailActivity.this).inflate(R.layout.kecheng_grid_item, null);
            CircleImageView circle_item = inflate.findViewById(R.id.circle_item);
            if(childList!=null){
                if(childList.get(position).getSelf_avatar_path()==null||childList.get(position).getSelf_avatar_path().equals("")){
                    Glide.with(CourseDetailActivity.this).load(R.drawable.img_my_avatar).into(circle_item);
                }else{
                    Glide.with(CourseDetailActivity.this).load(childList.get(position).getSelf_avatar_path()).into(circle_item);
                }

            }


            return inflate;
        }
    }

    private void initView() {

        course_back = findViewById(R.id.course_back);

        course_name = findViewById(R.id.course_name);
        course_length = findViewById(R.id.course_length);
        course_place = findViewById(R.id.course_place);
        course_room = findViewById(R.id.course_room);
        course_img = findViewById(R.id.course_img);
        course_jiaolian_huiyuan_name = findViewById(R.id.course_jiaolian_huiyuan_name);
        course_jiaolian_huiyuan_circle = findViewById(R.id.course_jiaolian_huiyuan_circle);
        course_renshu = findViewById(R.id.course_renshu);
        rl_button_yuyue = findViewById(R.id.rl_button_yuyue);
        tv_kecheng_fenshu = findViewById(R.id.tv_kecheng_fenshu);
        tv_jiaolian_fenshu = findViewById(R.id.tv_jiaolian_fenshu);
        tv_changdi_fenshu = findViewById(R.id.tv_changdi_fenshu);
        tv_content = findViewById(R.id.tv_content);
        my_expanda = findViewById(R.id.my_expanda);
        my_expanda.setGroupIndicator(null);
        my_expanda.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
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

        setclick();

        initData();
    }

    private void setclick() {

        rl_button_yuyue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CourseDetailActivity.this,SiJiaoDetailActivity.class);
                if(role!=null){
                    intent.putExtra("item",item1);
                }else{
                    intent.putExtra("item",item);
                }
                intent.putExtra("startTime",startTimeTv);
                intent.putExtra("endTime",endTimeTv);
                intent.putExtra("role",role);
                intent.putExtra("auth",auth);

                startActivity(intent);
            }
        });

        course_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void getPeople(){

        SiJiaoYuYueConBean siJiaoYuYueConBean = new SiJiaoYuYueConBean();
        siJiaoYuYueConBean.setCourse_type_id(item.getCourse_type_id());

        siJiaoYuYueConBean.setPage(0);
        siJiaoYuYueConBean.setSize(6);

        String s = gson.toJson(siJiaoYuYueConBean);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.QUERYGROUPCOURSE,s ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                courseMemberBean = gson.fromJson(jsonObject.toString(), CourseMemberBean.class);
                if(courseMemberBean!=null){
                    if(courseMemberBean.getTotalElements()>6){
                        getTotlePeple();
                    }else if(courseMemberBean.getTotalElements()<1){
                        my_expanda.setVisibility(View.GONE);
                    }
                    course_renshu.setText("购买会员("+courseMemberBean.getTotalElements()+")");
                    headList = courseMemberBean.getContent();
                    my_expanda.setAdapter(myAdapter);
                }else{
                    my_expanda.setVisibility(View.GONE);
                    course_renshu.setText("购买会员(0)");
                }

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

    private void getTotlePeple(){

        SiJiaoYuYueConBean siJiaoYuYueConBean = new SiJiaoYuYueConBean();
        siJiaoYuYueConBean.setCourse_type_id(item.getCourse_type_id());

        siJiaoYuYueConBean.setPage(0);
        siJiaoYuYueConBean.setSize(100);

        String s = gson.toJson(siJiaoYuYueConBean);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.QUERYGROUPCOURSE,s ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                courseMemberBean = gson.fromJson(jsonObject.toString(), CourseMemberBean.class);
                childList = courseMemberBean.getContent();
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

    private void initData() {
        if(role!=null){
            course_name.setText(item1.getCourse_type_name());

        }else{
            course_name.setText(item.getCourse_type_name());
            course_length.setText("课程时长："+item.getTime_length()+"分钟");
            course_place.setText("上课场馆："+data.getMember().getBranch_name());
            course_room.setText("上课场地：");
            Glide.with(CourseDetailActivity.this).load("http://cdn.duitang.com/uploads/item/201603/02/20160302141852_2tMwx.jpeg").into(course_img);
            course_jiaolian_huiyuan_name.setText(item.getEmployee_name());
        }

        getPeople();
    }

}
