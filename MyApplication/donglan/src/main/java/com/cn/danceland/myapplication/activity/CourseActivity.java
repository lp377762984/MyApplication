package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.format.Time;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.GroupRecordBean;
import com.cn.danceland.myapplication.bean.SiJiaoRecordBean;
import com.cn.danceland.myapplication.bean.SiJiaoYuYueConBean;
import com.cn.danceland.myapplication.bean.TuanKeRecordBean;
import com.cn.danceland.myapplication.evntbus.StringEvent;
import com.cn.danceland.myapplication.fragment.CommentFragment;
import com.cn.danceland.myapplication.fragment.SiJiaoFragment;
import com.cn.danceland.myapplication.fragment.SiJiaoRecordFragment;
import com.cn.danceland.myapplication.fragment.TuanKeFragment;
import com.cn.danceland.myapplication.fragment.TuanKeRecordFragment;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.TimeUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;
import com.necer.ncalendar.calendar.NCalendar;
import com.necer.ncalendar.listener.OnCalendarChangedListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by feng on 2018/1/11.
 */

public class CourseActivity extends FragmentActivity {
    FragmentManager fragmentManager;
    TuanKeFragment tuanKeFragment;
    SiJiaoRecordFragment siJiaoRecordFragment;
    TuanKeRecordFragment tuanKeRecordFragment;
    ImageView course_back;
    NCalendar nccalendar;
    TextView tv_date;
    RelativeLayout rl_nv;
    TabLayout.Tab tab1,tab2;
    TabLayout tablayout;
    SiJiaoFragment siJiaoFragment;
    String type;//0是列表，1是记录，2是小团课课程表
    String startTime,endTime;
    int id,course_type_id;
    String isTuanke;//团课isTuanke==0；一对一和小团课==1
    Time time;
    String nowTime,nowTimeLen,role,auth;
    Data data;
    long monthFirstDay,monthLastDay;
    Gson gson;
    ArrayList<String> yuyueTimeList;
    String from;
    GradientDrawable background;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course);
        initHost();
        initView();
        setOnclick();
//        if(startTime!=null){
//            getRecordTime();
//            setPoint();
//        }

    }

    private void setPoint(){

        nccalendar.setPoint(yuyueTimeList);
        yuyueTimeList.clear();
    }

    private void initHost() {
        data = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
        isTuanke = getIntent().getStringExtra("isTuanke");
        if(isTuanke==null){
            isTuanke = "1";
        }
        time = new Time();
        time.setToNow();
        yuyueTimeList = new ArrayList<String>();
        nowTime = time.year+"-"+time.month+"-"+time.monthDay+" 00:00:00";
        startTime = TimeUtils.date2TimeStamp(nowTime,"yyyy-MM-dd 00:00:00")+"";


        role = getIntent().getStringExtra("role");
        auth = getIntent().getStringExtra("auth");

        gson = new Gson();

    }


    public void setBackGround(GradientDrawable background){
        this.background = background;
    }


    private void getSiJiaoRecordTime(){

        SiJiaoYuYueConBean siJiaoYuYueConBean = new SiJiaoYuYueConBean();
        if(role!=null||!"".equals(role)){
            siJiaoYuYueConBean.setEmployee_id(data.getEmployee().getId());
        }else{
            siJiaoYuYueConBean.setMember_no(data.getPerson().getMember_no());
        }
        final Calendar calendar = TimeUtils.dataToCalendar(new Date(Long.valueOf(startTime)));

        monthFirstDay = TimeUtils.getMonthFirstDay(calendar);
        monthLastDay = TimeUtils.getMonthLastDay(calendar);

        siJiaoYuYueConBean.setCourse_date_gt(monthFirstDay);
        siJiaoYuYueConBean.setCourse_date_lt(monthLastDay);

        final String s = gson.toJson(siJiaoYuYueConBean);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.APPOINTLIST, s, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtil.e("zzf",jsonObject.toString());
                //yuyueTimeList.clear();
                SiJiaoRecordBean siJiaoRecordBean = gson.fromJson(jsonObject.toString(), SiJiaoRecordBean.class);
                if(siJiaoRecordBean!=null){
                    SiJiaoRecordBean.Data data = siJiaoRecordBean.getData();
                    if(data!=null){
                        List<SiJiaoRecordBean.Content> content = data.getContent();
                        if(content!=null){
                            for(int i=0;i<content.size();i++){
                                String s1 = TimeUtils.timeStamp2Date(content.get(i).getCourse_date() + "", "yyyy-MM-dd");
                                yuyueTimeList.add(s1);
                            }

                        }
                    }
                }
                getGroupData(s);
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
                LogUtil.e("zzf",SPUtils.getString(Constants.MY_TOKEN,""));
                return map;
            }

        };

        MyApplication.getHttpQueues().add(jsonObjectRequest);
    }


    private void getGroupData(String s) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.FINDGROUPCOURSEAPPOINTLIST, s,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                //if("2".equals(course_category)){
                GroupRecordBean groupRecordBean = gson.fromJson(jsonObject.toString(), GroupRecordBean.class);
                if(groupRecordBean!=null){
                    List<GroupRecordBean.Data> data = groupRecordBean.getData();
                    if(data!=null){
                        for(int i = 0;i<data.size();i++){
                            String s1 = TimeUtils.timeStamp2Date(data.get(i).getDate() + "", "yyyy-MM-dd");
                            yuyueTimeList.add(s1);
                        }
                    }
                }

                setPoint();
                //}
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
                LogUtil.e("zzf",SPUtils.getString(Constants.MY_TOKEN,""));
                return map;
            }

        };

        MyApplication.getHttpQueues().add(jsonObjectRequest);


    }


    private void setOnclick() {

        nccalendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChanged(DateTime dateTime) {
                if(dateTime!=null){
                    LogUtil.e("zzf",dateTime.toString());
                    String[] ts = dateTime.toString().split("T");
                    tv_date.setText(ts[0]);

                    startTime = TimeUtils.date2TimeStamp(ts[0]+" 00:00:00", "yyyy-MM-dd 00:00:00")+"";
                    endTime = (Long.valueOf(startTime)+86399)+"";
                    if("0".equals(isTuanke)||"1".equals(type)){
                        showFragment(type,isTuanke);
                    }else if("2".equals(type)){
                        showFragment(type,isTuanke);
                    }


                    if("0".equals(isTuanke)){
                        getTuanKeRecordTime();
                    }else {
                        if(startTime!=null){
                            getSiJiaoRecordTime();
                        }
                    }

                }
            }


        });

        nccalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(CourseActivity.this,SiJiaoOrderActivity.class));

            }
        });

    }


    private void getTuanKeRecordTime(){

        SiJiaoYuYueConBean siJiaoYuYueConBean = new SiJiaoYuYueConBean();
        siJiaoYuYueConBean.setMember_no(data.getPerson().getMember_no());
        final Calendar calendar = TimeUtils.dataToCalendar(new Date(Long.valueOf(startTime)));

        monthFirstDay = TimeUtils.getMonthFirstDay(calendar);
        monthLastDay = TimeUtils.getMonthLastDay(calendar);

        siJiaoYuYueConBean.setCourse_date_gt(monthFirstDay);
        siJiaoYuYueConBean.setCourse_date_lt(monthLastDay);

        String s = gson.toJson(siJiaoYuYueConBean);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.FREECOURSELIST, s,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtil.e("zzf",jsonObject.toString());
                TuanKeRecordBean tuanKeRecordBean = gson.fromJson(jsonObject.toString(), TuanKeRecordBean.class);
                if(tuanKeRecordBean!=null){

                    List<TuanKeRecordBean.Data> data = tuanKeRecordBean.getData();
                    if(data!=null){
                        for(int i = 0;i<data.size();i++){
                            String s1 = TimeUtils.timeStamp2Date(data.get(i).getDate() + "", "yyyy-MM-dd");
                            yuyueTimeList.add(s1);
                        }
                    }

                }
                setPoint();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToastShort("获取记录失败！");
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



    private void initView() {
        course_back = findViewById(R.id.course_back);
        course_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //List<Fragment> fragments = fragmentManager.getFragments();
//                if("2".equals(type)){
//                    type= "1";
//                    isTuanke = "1";
//                    showFragment(type,isTuanke);
//                }else{
                if(background!=null){
                    background.setColor(getResources().getColor(R.color.color_dl_yellow));
                }
                    finish();
               // }
            }
        });

        nccalendar = findViewById(R.id.nccalendar);
        tv_date = findViewById(R.id.tv_date);
        tablayout = findViewById(R.id.tablayout);
        tab1 = tablayout.getTabAt(0);
        tab2 = tablayout.getTabAt(1);
        rl_nv = findViewById(R.id.rl_nv);
        if(type==null){
            type="0";
        }
        showFragment(type,isTuanke);
        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        type = "0";
                        showFragment(type,isTuanke);
                        break;
                    case 1:
                        type="1";
                        showFragment(type,isTuanke);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public void getItemId(int id,int course_type_id,String type){
        this.id = id;
        this.course_type_id = course_type_id;
        this.type = type;
    }

    public void showFragment(String type,String isTuanke){
        fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if("0".equals(type)){
            if("0".equals(isTuanke)){
                tab1.setText("团课");
                try {
                    tuanKeFragment = new TuanKeFragment();
                    from = "免费团课";
                    tuanKeFragment.refresh(from,startTime,endTime,course_type_id,id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                fragmentTransaction.replace(R.id.rl_nv,tuanKeFragment);
            }else{
                siJiaoFragment = new SiJiaoFragment();
                siJiaoFragment.getRoles(role,auth,startTime);
                fragmentTransaction.replace(R.id.rl_nv,siJiaoFragment);
            }

        }else if("1".equals(type)){

            if("0".equals(isTuanke)){
                tuanKeRecordFragment = new TuanKeRecordFragment();
                tuanKeRecordFragment.getStartTime(startTime);
                fragmentTransaction.replace(R.id.rl_nv,tuanKeRecordFragment);
            }else{
                siJiaoRecordFragment = new SiJiaoRecordFragment();
                siJiaoRecordFragment.getStartTime(startTime);
                siJiaoRecordFragment.getRoles(role,auth);
                fragmentTransaction.replace(R.id.rl_nv,siJiaoRecordFragment);
            }

        }else if("2".equals(type)){
            tuanKeFragment = new TuanKeFragment();
            try {
                from = "小团课";
                tuanKeFragment.refresh(from,startTime,endTime,course_type_id,id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            fragmentTransaction.replace(R.id.rl_nv,tuanKeFragment);
        }

        fragmentTransaction.commit();
    }
}
