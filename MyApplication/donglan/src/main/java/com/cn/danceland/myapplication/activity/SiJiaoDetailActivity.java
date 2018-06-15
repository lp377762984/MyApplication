package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.JiaoLianCourseBean;
import com.cn.danceland.myapplication.bean.MyCourseBean;
import com.cn.danceland.myapplication.bean.RootBean;
import com.cn.danceland.myapplication.bean.SiJiaoRecordBean;
import com.cn.danceland.myapplication.bean.SiJiaoYuYueConBean;
import com.cn.danceland.myapplication.bean.TimeAxisBean;
import com.cn.danceland.myapplication.bean.TimeAxisCon;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.TimeUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.cn.danceland.myapplication.view.CustomLine;
import com.google.gson.Gson;
import com.necer.ncalendar.calendar.NCalendar;
import com.necer.ncalendar.listener.OnCalendarChangedListener;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.xiaomi.smack.packet.h.a.s;

/**
 * Created by feng on 2018/1/13.
 */

public class SiJiaoDetailActivity extends Activity {
    //ArrayList<Integer> arrPositionF, arrPositionS;
    ArrayList<Integer> arrStatusF, arrStatusS;
    //HashMap<Integer, Integer> arrPosition, arrStatus;
    //ListView list_1, list_2;
    LinearLayout ll_time;
    LoopView loopview;
    View inflate;
    TextView over_time, tv_date, course_name, course_jiaolian, buy, course_shengyu, course_length;
    int pos = 999;
    ImageView sijiao_back, detail_img;
    //    MyListF myListF;
//    MyListS myListS;
    AlertDialog.Builder alertdialog;
    NCalendar nccalendar;
    MyCourseBean.Data item;
    JiaoLianCourseBean.Content item1;
    String startTime, endTime, startTimeTv, endTimeTv;
    Gson gson;
    ArrayList<String> yuyueStartList;
    ArrayList<Integer> index, indexF, indexS;
    //int status=999;//1:等待对方确认,2:预约成功,3:上课中,4:已结束,5:待评分,6:已评分
    String weekDay;
    String role;
    String auth;
    long hourMill = 3600000;
    ArrayList<Long> startMillArr;
    ArrayList<Integer> requestStatusArr;
    Data data;
    boolean isYuYue;
    private ArrayList<Integer> positionList;
    private ArrayList<Integer> textPositionList;
    private ArrayList<Integer> statusList;
    private ArrayList<Integer> roleList;
    private CustomLine customLine;
    private NestedScrollView ns_view;
    private LinearLayout ll_01;
    private ArrayList<String> timeStrList;
    private ArrayList<Integer> startMinutesList;
    private int courseLength;
    private ArrayList<Integer> endMinuteList;
    private ArrayList<Integer> jiaolianMinuteList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sijiao_detail);
        initHost();
        iniView();
    }


    private void initHost() {
        positionList = new ArrayList<>();
        textPositionList = new ArrayList<>();
        statusList = new ArrayList<>();
        roleList = new ArrayList<>();
        for (int i = 0; i < 84; i++) {
            positionList.add(999);
        }
        for (int i = 0; i < 84; i++) {
            statusList.add(999);
        }
        for (int i = 0; i < 84; i++) {
            roleList.add(999);
        }
//        for (int i = 0; i < 84; i++) {
//            textPositionList.add(999);
//        }


        startMillArr = new ArrayList<>();
        requestStatusArr = new ArrayList<>();

        gson = new Gson();

        data = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
        startTimeTv = getIntent().getStringExtra("startTime");
        startTime = TimeUtils.date2TimeStamp(startTimeTv + " 00:00:00", "yyyy-MM-dd 00:00:00") + "";
        endTimeTv = getIntent().getStringExtra("endTime");
        role = getIntent().getStringExtra("role");
        auth = getIntent().getStringExtra("auth");
        if (role != null) {
            item1 = (JiaoLianCourseBean.Content) getIntent().getSerializableExtra("item");
            courseLength = item1.getTime_length();
        } else {
            item = (MyCourseBean.Data) getIntent().getSerializableExtra("item");
            courseLength = item.getTime_length();
        }
        timeStrList = new ArrayList<>();
        for (int i = 8; i < 23; i++) {
            for (int j = 0; j < 6; j++) {
                timeStrList.add(i+":"+j+"0");
            }
        }
        startMinutesList = new ArrayList<>();
        for(int i=0;i<timeStrList.size();i++){
            String[] split = timeStrList.get(i).split(":");
            startMinutesList.add(Integer.valueOf(split[0])*60+Integer.valueOf(split[1]));
        }
        endMinuteList = new ArrayList<>();
        for(int i=0;i<startMinutesList.size();i++){
            endMinuteList.add(startMinutesList.get(i)+courseLength);
        }

        jiaolianMinuteList = new ArrayList<>();

        nccalendar = findViewById(R.id.nccalendar);
        nccalendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChanged(DateTime dateTime) {
                if (dateTime != null) {
                    positionList.clear();
                    statusList.clear();
                    roleList.clear();
                    textPositionList.clear();
                    for (int i = 0; i < 84; i++) {
                        positionList.add(999);
                    }
                    for (int i = 0; i < 84; i++) {
                        statusList.add(999);
                    }
                    for (int i = 0; i < 84; i++) {
                        roleList.add(999);
                    }
//                    for (int i = 0; i < 84; i++) {
//                        textPositionList.add(999);
//                    }

                    String[] ts = dateTime.toString().split("T");
                    tv_date.setText(ts[0]);
                    startTime = TimeUtils.date2TimeStamp(ts[0] + " 00:00:00", "yyyy-MM-dd 00:00:00") + "";
                    endTime = (Long.valueOf(startTime) + 86399) + "";
                    weekDay = TimeUtils.dateToWeek(ts[0]);
                    getData();
                }
            }


        });
    }

    public void iniView() {
        //customLine = findViewById(R.id.customLine);
        ll_01 = findViewById(R.id.ll_01);
        ll_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTime();
            }
        });
        ns_view = findViewById(R.id.ns_view);
        inflate = LayoutInflater.from(SiJiaoDetailActivity.this).inflate(R.layout.timeselect, null);
        alertdialog = new AlertDialog.Builder(SiJiaoDetailActivity.this);
        detail_img = findViewById(R.id.detail_img);
        course_name = findViewById(R.id.course_name);
        course_jiaolian = findViewById(R.id.course_jiaolian);
        buy = findViewById(R.id.buy);
        course_shengyu = findViewById(R.id.course_shengyu);
        course_length = findViewById(R.id.course_length);
        if (item != null) {
            course_name.setText(item.getCourse_type_name());
            if (item.getEmployee_name() != null) {
                course_jiaolian.setText(item.getEmployee_name());
            } else {
                course_jiaolian.setText("");
            }
            buy.setText("购买节数：" + item.getCount());
            course_shengyu.setText("剩余节数：" + item.getSurplus_count());
            Glide.with(SiJiaoDetailActivity.this).load(item.getImg_url()).into(detail_img);
        } else {
            course_name.setText(item1.getCourse_type_name());
            if (item1.getEmployee_name() != null) {
                course_jiaolian.setText(item1.getEmployee_name());
            } else {
                course_jiaolian.setText("");
            }
            buy.setText("购买节数：" + item1.getCount());
            course_shengyu.setText("剩余节数：" + item1.getSurplus_count());
            Glide.with(SiJiaoDetailActivity.this).load(item1.getImg_url()).into(detail_img);
        }
        course_length.setText("有效期：" + startTimeTv + "至" + endTimeTv);


        loopview = inflate.findViewById(R.id.loopview);
        over_time = inflate.findViewById(R.id.over_time);

        sijiao_back = findViewById(R.id.sijiao_back);
        sijiao_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_date = findViewById(R.id.tv_date);

        getData();
    }

    private void showTime() {
        ViewGroup parent = (ViewGroup) inflate.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }

        loopview.setNotLoop();
        loopview.setItems(timeStrList);
        //设置初始位置
        loopview.setInitPosition(0);
        int endMinute = startMinutesList.get(0) + courseLength;
        String endTime;
        if(endMinute%60<10){
            endTime = endMinute / 60 + ":0" + endMinute % 60;
        }else{
            endTime = endMinute / 60 + ":" + endMinute % 60;
        }
        over_time.setText(endTime + "结束");
        //设置字体大小
        loopview.setTextSize(16);
        loopview.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                pos = index;
                int endMinute = startMinutesList.get(index) + courseLength;
                String endTime;
                if(endMinute%60<10){
                    endTime = endMinute / 60 + ":0" + endMinute % 60;
                }else{
                    endTime = endMinute / 60 + ":" + endMinute % 60;
                }
                over_time.setText(endTime + "结束");

            }
        });
        alertdialog.setTitle("选择预约时间");
        alertdialog.setView(inflate);
        alertdialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < jiaolianMinuteList.size(); i++) {
                    if (startMinutesList.get(pos).intValue() == jiaolianMinuteList.get(i).intValue() || endMinuteList.get(pos).intValue() == jiaolianMinuteList.get(i).intValue()) {
                        ToastUtils.showToastShort("该时间段已被预约！请重新选择");
                        return;
                    }

                }
                if(pos==999){
                    pos = 0;
                }
                int start = startMinutesList.get(pos);
                int end = endMinuteList.get(pos);

                if (startTime != null && start != 0 && end != 0) {
                    if (Long.valueOf(startTime) + start * 60000 >= System.currentTimeMillis()) {
                        commitYuYue((long)start, (long)end);
                    } else {
                        ToastUtils.showToastShort("无法预约过去的时间段");
                    }
                } else {
                    ToastUtils.showToastShort("请选择时间重新提交");
                }
            }
        });

        alertdialog.show();

    }

    private void getData() {

        TimeAxisCon timeAxis = new TimeAxisCon();
        if (item != null) {
            timeAxis.setEmployee_id(item.getEmployee_id());
        } else if (item1 != null) {
            timeAxis.setEmployee_id(item1.getEmployee_id());
        }
        timeAxis.setWeek(weekDay);
        timeAxis.setCourse_date(Long.valueOf(startTime));
        String s = gson.toJson(timeAxis);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.FINDAVAI, s, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtil.e("zzf", jsonObject.toString());
                TimeAxisBean timeAxisBean = gson.fromJson(jsonObject.toString(), TimeAxisBean.class);
                List<TimeAxisBean.Data> data = timeAxisBean.getData();
                if (data != null && data.size() > 0) {
                    for (int i = 0; i < data.size(); i++) {
                        int start_time = data.get(i).getStart_time() - 480;
                        int end_time = data.get(i).getEnd_time() - 480;
                        int pos = start_time / 10;
                        textPositionList.add(pos);
                        for (int j = 0; j < (end_time - start_time) / 10; j++) {
                            jiaolianMinuteList.add(data.get(i).getStart_time()+(j*10));
                            positionList.set(pos, pos);
                            statusList.set(pos, data.get(i).getStatus());
                            roleList.set(pos, 1);
                            pos++;
                        }
                    }
                }
                getHistory();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.e("zzf", volleyError.toString());

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));
                return map;
            }
        };

        MyApplication.getHttpQueues().add(jsonObjectRequest);

    }


    private void getHistory() {

        SiJiaoYuYueConBean siJiaoYuYueConBean = new SiJiaoYuYueConBean();
        if (role != null) {
            siJiaoYuYueConBean.setEmployee_id(data.getEmployee().getId());
        } else {
            siJiaoYuYueConBean.setMember_no(data.getPerson().getMember_no());
        }

        if (startTime != null) {
            siJiaoYuYueConBean.setCourse_date(Long.valueOf(startTime));
        } else {
            siJiaoYuYueConBean.setCourse_date(System.currentTimeMillis());
        }
        String s = gson.toJson(siJiaoYuYueConBean);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.APPOINTLIST, s, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtil.e("zzf", jsonObject.toString());
                SiJiaoRecordBean siJiaoRecordBean = gson.fromJson(jsonObject.toString(), SiJiaoRecordBean.class);
                if (siJiaoRecordBean != null) {
                    SiJiaoRecordBean.Data data = siJiaoRecordBean.getData();
                    if (data != null) {
                        List<SiJiaoRecordBean.Content> content = data.getContent();
                        if (content != null && content.size() > 0) {
                            for (int i = 0; i < content.size(); i++) {
                                int start_time = content.get(i).getStart_time() - 480;
                                int end_time = content.get(i).getEnd_time() - 480;
                                int pos = start_time / 10;
                                textPositionList.add(pos);
                                for (int j = 0; j < (end_time - start_time) / 10; j++) {
                                    positionList.set(pos, pos);
                                    statusList.set(pos, content.get(i).getStatus());
                                    roleList.set(pos, 2);
                                    pos++;
                                }
                            }
                        }
                    }
                }
                if (ll_01.getChildCount() > 0) {
                    ll_01.removeAllViews();
                }
                CustomLine customLine = new CustomLine(SiJiaoDetailActivity.this, positionList, statusList, roleList,textPositionList,courseLength/10);
                ll_01.addView(customLine);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.e("zzf", volleyError.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));
                return map;
            }

        };

        MyApplication.getHttpQueues().add(jsonObjectRequest);

    }


    private void commitYuYue(final long startM, final long endM) {

        SiJiaoYuYueConBean siJiaoYuYueConBean = new SiJiaoYuYueConBean();
        if (role != null) {
            siJiaoYuYueConBean.setAppointment_type(1);
            siJiaoYuYueConBean.setEmployee_id(item1.getEmployee_id());
            siJiaoYuYueConBean.setMember_course_id(item1.getId());
        } else {
            siJiaoYuYueConBean.setAppointment_type(2);
            siJiaoYuYueConBean.setEmployee_id(item.getEmployee_id());
            siJiaoYuYueConBean.setMember_course_id(item.getId());
        }
        siJiaoYuYueConBean.setCourse_date(Long.valueOf(startTime));
        siJiaoYuYueConBean.setStart_time(startM);
        siJiaoYuYueConBean.setEnd_time(endM);

        siJiaoYuYueConBean.setWeek(Integer.valueOf(TimeUtils.dateToWeek(TimeUtils.timeStamp2Date(startTime, "yyyy-MM-dd"))));

        String s = gson.toJson(siJiaoYuYueConBean);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.COURSEAPPOIN, s, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                RootBean rootBean = gson.fromJson(jsonObject.toString(), RootBean.class);
                if (rootBean != null) {
                    Integer success = Integer.valueOf(rootBean.data);
                    if (success > 0) {
                        int start_time = (int)startM - 480;
                        int end_time = (int)endM - 480;
                        int pos = start_time / 10;
                        textPositionList.add(pos);
                        for (int j = 0; j < (end_time - start_time) / 10; j++) {
                            positionList.set(pos, pos);
                            statusList.set(pos, 1);
                            roleList.set(pos, 2);
                            pos++;
                        }
                        if (ll_01.getChildCount() > 0) {
                            ll_01.removeAllViews();
                        }
                        CustomLine customLine = new CustomLine(SiJiaoDetailActivity.this, positionList, statusList, roleList,textPositionList,courseLength/10);
                        ll_01.addView(customLine);
                        ToastUtils.showToastShort("预约成功！");
                    } else {
                        ToastUtils.showToastShort("失败！请重新预约！");
                    }

                }

                LogUtil.e("zzf", jsonObject.toString());


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.e("zzf", volleyError.toString());

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));
                return map;
            }
        };

        MyApplication.getHttpQueues().add(jsonObjectRequest);

    }
}
