package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.CourseEvaluateBean;
import com.cn.danceland.myapplication.bean.CourseMemberBean;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.JiaoLianCourseBean;
import com.cn.danceland.myapplication.bean.KeChengBiaoBean;
import com.cn.danceland.myapplication.bean.MyCourseBean;
import com.cn.danceland.myapplication.bean.SiJiaoRecordBean;
import com.cn.danceland.myapplication.bean.SiJiaoYuYueConBean;
import com.cn.danceland.myapplication.bean.TuanKeBean;
import com.cn.danceland.myapplication.evntbus.StringEvent;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.CustomGridView;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.NestedExpandaleListView;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.TimeUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;

import org.apache.http.cookie.SM;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by feng on 2018/3/30.
 */

public class SmallTuankeDetailActivity extends Activity {

    String startTime,endTime,startTimeTv,endTimeTv;
    Data data;
    String role;
    String auth;
    KeChengBiaoBean.Data item;
    JiaoLianCourseBean.Content item1;
    TextView course_name,course_length,course_place,course_room,
            course_jiaolian_huiyuan_name,course_renshu,tv_kecheng_fenshu,tv_jiaolian_fenshu,tv_changdi_fenshu,
            tv_content,tv_status;
    ImageView course_img,small_back;
    CircleImageView course_jiaolian_huiyuan_circle;
    RelativeLayout rl_button_yuyue;
    NestedExpandaleListView my_expanda;
    ImageView down_img,up_img;
    Gson gson;
    CourseMemberBean courseMemberBean;
    List<CourseMemberBean.Content> headList,childList;
    MyAdapter myAdapter;
    ImageView pic_01,pic_02,pic_03;
    int member_course_id;
    String yuyueStartTime;
    SiJiaoRecordBean.Content record;
    String emp_id,room_id,courseTypeId,branchId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smalltuankedetail_activity);
        initHost();
        initView();
        getDeatil();
        getPeople();
        queryAverage();
    }

    private void queryAverage(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.QUERYAVERAGE, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                CourseEvaluateBean courseEvaluateBean = gson.fromJson(s, CourseEvaluateBean.class);
                if(courseEvaluateBean!=null && courseEvaluateBean.getData()!=null){
                    CourseEvaluateBean.Data data = courseEvaluateBean.getData();
                    if(data.getCourse_type_score()!=null){
                        tv_kecheng_fenshu.setText(data.getCourse_type_score());
                    }else{
                        tv_kecheng_fenshu.setText("暂无评分");
                    }
                    if(data.getEmployee_score()!=null){
                        tv_jiaolian_fenshu.setText(data.getEmployee_score());
                    }else{
                        tv_jiaolian_fenshu.setText("暂无评分");
                    }
                    if(data.getRoom_score()!=null){
                        tv_changdi_fenshu.setText(data.getRoom_score());
                    }else {
                        tv_changdi_fenshu.setText("暂无评分");
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(volleyError!=null){
                    LogUtil.i(volleyError.toString());
                }else {
                    LogUtil.i("获取评分失败");
                }
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> map = new HashMap<>();
                map.put("employeeId",emp_id);
                map.put("courseTypeId",courseTypeId);
                if(room_id!=null){
                    map.put("roomId",room_id);
                }
                map.put("branchId",branchId);

                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, null));

                return map;
            }
        };
        MyApplication.getHttpQueues().add(stringRequest);
    }

    private void initHost() {


        gson = new Gson();
        data = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
        startTimeTv = getIntent().getStringExtra("startTime");
        startTime = TimeUtils.date2TimeStamp(startTimeTv+" 00:00:00", "yyyy-MM-dd 00:00:00")+"";
        endTimeTv = getIntent().getStringExtra("endTime");
        role = getIntent().getStringExtra("role");
        auth = getIntent().getStringExtra("auth");

        record = (SiJiaoRecordBean.Content)getIntent().getSerializableExtra("record");
//        if(role!=null){
//            item1 = (JiaoLianCourseBean.Content)getIntent().getSerializableExtra("item");
//        }else{
        item = (KeChengBiaoBean.Data)getIntent().getSerializableExtra("item");
        emp_id = item.getEmployee_id()+"";
        courseTypeId = item.getCourse_type_id()+"";
        branchId = item.getBranch_id()+"";

        member_course_id = getIntent().getIntExtra("member_course_id",-1);

        yuyueStartTime = getIntent().getStringExtra("yuyueStartTime");
        //}
    }

    private void initView() {

        small_back = findViewById(R.id.small_back);
        my_expanda = findViewById(R.id.my_expanda);
        myAdapter = new MyAdapter();
        //my_expanda.setAdapter(myAdapter);

        pic_01 = findViewById(R.id.pic_01);
        pic_02 = findViewById(R.id.pic_02);
        pic_03 = findViewById(R.id.pic_03);


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
        tv_status = findViewById(R.id.tv_status);


        setclick();

        //initData();a
    }


    private void getDeatil(){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.FINDGROUP, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                TuanKeBean tuanKeBean = gson.fromJson(s, TuanKeBean.class);
                if(tuanKeBean!=null){
                    TuanKeBean.Data detailData = tuanKeBean.getData();
                    if(detailData!=null){
                        initData(detailData);
                    }
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
                if(item!=null){
                    map.put("id",item.getId()+"");
                }else{
                    map.put("id",record.getId()+"");
                }

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

    private void initData(final TuanKeBean.Data detailData){
        course_name.setText(detailData.getCourse_type_name());
        course_length.setText("上课时间:"+TimeUtils.timeStamp2Date(detailData.getCourse_date(),"yyyy-MM-dd")+" "+TimeUtils.MinuteToTime(detailData.getStart_time())
        +"-"+TimeUtils.MinuteToTime(detailData.getEnd_time()));
        course_place.setText("上课场馆:"+detailData.getRoom_name());
        course_room.setText("上课场地:"+detailData.getRoom_name());
        Glide.with(SmallTuankeDetailActivity.this).load(detailData.getCover_img_url()).into(course_img);
        if(detailData.getEmployee_avatar_path()==null||detailData.getEmployee_avatar_path().equals("")){
            Glide.with(SmallTuankeDetailActivity.this).load(R.drawable.img_my_avatar).into(course_jiaolian_huiyuan_circle);
        }else{
            Glide.with(SmallTuankeDetailActivity.this).load(detailData.getEmployee_avatar_path()).into(course_jiaolian_huiyuan_circle);
        }

        course_jiaolian_huiyuan_name.setText(detailData.getEmployee_name());
        Glide.with(SmallTuankeDetailActivity.this).load(detailData.getCourse_img_url_1()).into(pic_01);
        Glide.with(SmallTuankeDetailActivity.this).load(detailData.getCourse_img_url_2()).into(pic_02);
        Glide.with(SmallTuankeDetailActivity.this).load(detailData.getCourse_img_url_3()).into(pic_03);
        pic_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SmallTuankeDetailActivity.this, AvatarActivity.class).putExtra("url", detailData.getCourse_img_url_1()));
            }
        });
        pic_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SmallTuankeDetailActivity.this, AvatarActivity.class).putExtra("url", detailData.getCourse_img_url_2()));
            }
        });
        pic_03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SmallTuankeDetailActivity.this, AvatarActivity.class).putExtra("url", detailData.getCourse_img_url_3()));
            }
        });
    }


    private void setclick() {

//        rl_button_yuyue.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(SmallTuankeDetailActivity.this,SiJiaoDetailActivity.class);
////                if(role!=null){
////                    intent.putExtra("item",item1);
////                }else{
//                    intent.putExtra("item",item);
//                //}
//                intent.putExtra("startTime",startTime);
//                intent.putExtra("endTime",endTime);
//                intent.putExtra("role",role);
//                intent.putExtra("auth",auth);
//
//                startActivity(intent);
//            }
//        });

        small_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rl_button_yuyue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitYuyue();
            }
        });

        if(item!=null){
            if(item.getSelf_appoint_count()>0){
                tv_status.setText("已预约");
                rl_button_yuyue.setClickable(false);
                rl_button_yuyue.setBackground(getResources().getDrawable(R.drawable.btn_bg_gray));
            }
        }else{
            tv_status.setText("已结束");
            rl_button_yuyue.setClickable(false);
            rl_button_yuyue.setBackground(getResources().getDrawable(R.drawable.btn_bg_gray));
        }


    }

    private void commitYuyue(){


        SiJiaoYuYueConBean siJiaoYuYueConBean = new SiJiaoYuYueConBean();
        siJiaoYuYueConBean.setGroup_course_id(item.getId());

        siJiaoYuYueConBean.setMember_course_id(member_course_id);

        siJiaoYuYueConBean.setCourse_type_id(item.getCourse_type_id());
        siJiaoYuYueConBean.setCourse_type_name(item.getCourse_type_name());
        siJiaoYuYueConBean.setDate(yuyueStartTime);
        String s = gson.toJson(siJiaoYuYueConBean);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.GROUPAPPOINT, s,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if(jsonObject.toString().contains("true")){
//                    rl.setBackgroundColor(Color.parseColor("#ADFF2F"));
//                    tv.setText("已预约");
                    ToastUtils.showToastShort("预约成功！");
                    setResult(222);
                    finish();
                }else{
                    ToastUtils.showToastShort("预约失败！请重新预约！");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToastShort("预约失败！请重新预约！");
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


    private void getPeople(){
        String url = null;

        SiJiaoYuYueConBean siJiaoYuYueConBean = new SiJiaoYuYueConBean();
        if(item!=null){
            siJiaoYuYueConBean.setGroup_course_id(item.getId());
            url = Constants.QUERYGROUPCOURSE;
            siJiaoYuYueConBean.setPage(0);
            siJiaoYuYueConBean.setSize(6);
        }else{
            siJiaoYuYueConBean.setGroup_course_id(record.getId());
            url = Constants.FINDGROUPCOURSEAPPOINTPERSON;
            siJiaoYuYueConBean.setDate(yuyueStartTime);
        }

        String s = gson.toJson(siJiaoYuYueConBean);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,s ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if(item!=null){
                    courseMemberBean = gson.fromJson(jsonObject.toString(), CourseMemberBean.class);
                    if(courseMemberBean!=null){
                        CourseMemberBean.Data data = courseMemberBean.getData();
                        if(data!=null){
                            if(data.getTotalElements()>6){
                                getTotlePeple();
                            }else if(data.getTotalElements()<1){
                                my_expanda.setVisibility(View.GONE);
                            }
                            if(item!=null){
                                course_renshu.setText("购买会员("+data.getTotalElements()+")");
                            }else{
                                course_renshu.setText("上课会员("+data.getTotalElements()+")");
                            }

                            headList = data.getContent();

                            my_expanda.setAdapter(myAdapter);
                        }

                    }
                }else{
                    my_expanda.setVisibility(View.GONE);
                    if(item!=null){
                        course_renshu.setText("购买会员(0)");
                    }else{
                        course_renshu.setText("上课会员(0)");
                    }

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
        siJiaoYuYueConBean.setDate(yuyueStartTime);
        siJiaoYuYueConBean.setPage(0);
        siJiaoYuYueConBean.setSize(100);

        String s = gson.toJson(siJiaoYuYueConBean);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.QUERYGROUPCOURSE,s ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                courseMemberBean = gson.fromJson(jsonObject.toString(), CourseMemberBean.class);
                if(courseMemberBean!=null&&courseMemberBean.getData()!=null){
                    childList = courseMemberBean.getData().getContent();
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
                convertView = LayoutInflater.from(SmallTuankeDetailActivity.this).inflate(R.layout.kecheng_parent,null);
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
                    imgArr[i].setVisibility(View.VISIBLE);
                    if(headList.get(i).getSelf_avatar_path()==null||headList.get(i).getSelf_avatar_path().equals("")){
                        Glide.with(SmallTuankeDetailActivity.this).load(R.drawable.img_my_avatar).into(imgArr[i]);
                    }else{
                        Glide.with(SmallTuankeDetailActivity.this).load(headList.get(i).getSelf_avatar_path()).into(imgArr[i]);
                    }
                }
            }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            if(convertView==null){
                convertView = LayoutInflater.from(SmallTuankeDetailActivity.this).inflate(R.layout.kecheng_child,null);
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
            View inflate = LayoutInflater.from(SmallTuankeDetailActivity.this).inflate(R.layout.kecheng_grid_item, null);
            CircleImageView circle_item = inflate.findViewById(R.id.circle_item);
            if(childList!=null){
                if(childList.get(position).getSelf_avatar_path()==null||childList.get(position).getSelf_avatar_path().equals("")){
                    Glide.with(SmallTuankeDetailActivity.this).load(R.drawable.img_my_avatar).into(circle_item);
                }else{
                    Glide.with(SmallTuankeDetailActivity.this).load(childList.get(position).getSelf_avatar_path()).into(circle_item);
                }

            }


            return inflate;
        }
    }
}
