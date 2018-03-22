package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.cn.danceland.myapplication.bean.JiaoLianCourseBean;
import com.cn.danceland.myapplication.bean.MyCourseBean;
import com.cn.danceland.myapplication.bean.RootBean;
import com.cn.danceland.myapplication.bean.SiJiaoYuYueConBean;
import com.cn.danceland.myapplication.bean.TimeAxisBean;
import com.cn.danceland.myapplication.bean.TimeAxisCon;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.TimeUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
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

    ListView list_1,list_2;
    LinearLayout ll_time;
    LoopView loopview;
    View inflate;
    String[] time = {"9:00","9:30","10:00","10:30","11:00","11:30","12:00","12:30","13:00","13:30","14:00","14:30","15:00"
            ,"15:30","16:00","16:30","17:00","17:30","18:00","18:30","19:00","19:30","20:00","20:30","21:00"};

    List<String> timeList,timeMiList;
    TextView over_time,tv_date,course_name,course_jiaolian,buy,course_shengyu,course_length;
    int pos=999;
    ArrayList<Integer> status;
    int yuyuestatus,statustype;//标记预约的时间段，时间段的状态类型
    ImageView sijiao_back,detail_img;
    MyListF myListF;
    MyListS myListS;
    AlertDialog.Builder alertdialog;
    NCalendar nccalendar;
    MyCourseBean.Data item;
    JiaoLianCourseBean.Content item1;
    String startTime,endTime,startTimeTv,endTimeTv;
    Gson gson;
    ArrayList<String> yuyueList;
    ArrayList<String> yuyueStartList;
    ArrayList<Integer> index,indexF,indexS;
    boolean click=true;
    //int status=999;//1:等待对方确认,2:预约成功,3:上课中,4:已结束,5:待评分,6:已评分
    String weekDay;
    String role;
    String auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sijiao_detail);
        initHost();
        iniView();
    }

    private void initHost() {

        status = new ArrayList<Integer>();
        index = new ArrayList<Integer>();
        indexF = new ArrayList<Integer>();
        indexS = new ArrayList<Integer>();
        yuyueList = new ArrayList<String>();
        yuyueStartList = new ArrayList<String>();
        gson = new Gson();

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
        timeList = new ArrayList<String>();
        for(int i =0;i<time.length-2;i++){
            timeList.add(time[i]);
        }
        nccalendar = findViewById(R.id.nccalendar);
        nccalendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChanged(DateTime dateTime) {
                if(dateTime!=null){
                    status.clear();
                    index.clear();
                    indexF.clear();
                    indexS.clear();
                    yuyueList.clear();
                    yuyueStartList.clear();
                    String[] ts = dateTime.toString().split("T");
                    tv_date.setText(ts[0]);
                    startTime = TimeUtils.date2TimeStamp(ts[0]+" 00:00:00", "yyyy-MM-dd 00:00:00")+"";
                    endTime = (Long.valueOf(startTime)+86399)+"";
                    weekDay = TimeUtils.dateToWeek(ts[0]);
                    iniView();
                }
            }


        });
    }

    public void iniView() {

        inflate = LayoutInflater.from(SiJiaoDetailActivity.this).inflate(R.layout.timeselect, null);
        alertdialog = new AlertDialog.Builder(SiJiaoDetailActivity.this);
        detail_img = findViewById(R.id.detail_img);
        course_name = findViewById(R.id.course_name);
        course_jiaolian = findViewById(R.id.course_jiaolian);
        buy = findViewById(R.id.buy);
        course_shengyu = findViewById(R.id.course_shengyu);
        course_length = findViewById(R.id.course_length);
        if(item!=null){
            course_name.setText(item.getCourse_type_name());
            if(item.getEmployee_name()!=null){
                course_jiaolian.setText(item.getEmployee_name());
            }else{
                course_jiaolian.setText("");
            }
            buy.setText("购买节数："+item.getCount());
            course_shengyu.setText("剩余节数："+item.getSurplus_count());
        }else{
            course_name.setText(item1.getCourse_type_name());
            if(item1.getEmployee_name()!=null){
                course_jiaolian.setText(item1.getEmployee_name());
            }else{
                course_jiaolian.setText("");
            }
            buy.setText("购买节数："+item1.getCount());
            course_shengyu.setText("剩余节数："+item1.getSurplus_count());

        }
        course_length.setText("有效期："+startTimeTv+"至"+endTimeTv);

        Glide.with(SiJiaoDetailActivity.this).load("http://file06.16sucai.com/2016/0407/06368ac0797a6a7cb6e3f10bcc1ea36e.jpg").into(detail_img);

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

        list_1 = findViewById(R.id.list_1);
        list_1.setDividerHeight(0);
        list_2 = findViewById(R.id.list_2);
        list_2.setDividerHeight(0);

        myListF = new MyListF(indexF);

        myListS = new MyListS(indexS);
        list_1.setAdapter(myListF);
        list_2.setAdapter(myListS);
        list_2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(click){
                    //startActivity(new Intent(SiJiaoDetailActivity.this,PingJiaActivity.class).putExtra("item",item));
                    showTime();
                }else{
                    ToastUtils.showToastShort("不可重复预约！");
                }
            }
        });
        try {
            getData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showTime() {
        ViewGroup parent = (ViewGroup)inflate.getParent();
        if(parent!=null){
            parent.removeAllViews();
        }

//        Iterator<String> iterator = timeList.iterator();
//        while (iterator.hasNext()){
//            String s = iterator.next();
//            for(int j=0;j<yuyueList.size();j++){
//                if(s.equals(yuyueList.get(j))){
//                    iterator.remove();
//                }
//            }
//        }


        loopview.setNotLoop();
        loopview.setItems(timeList);
        //设置初始位置
        loopview.setInitPosition(0);
        over_time.setText(time[2]+"结束");
        //设置字体大小
        loopview.setTextSize(16);
        loopview.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                pos = index;
                if(index+2<=timeList.size()-1){
                    over_time.setText(timeList.get(index+2)+"结束");
                }else if(index+2==timeList.size()){
                    over_time.setText(time[time.length-2]+"结束");
                }else if(index+2==timeList.size()+1){
                    over_time.setText(time[time.length-1]+"结束");
                }

            }
        });
        alertdialog.setTitle("选择预约时间");
        alertdialog.setView(inflate);
        alertdialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for(int i=0;i<yuyueList.size();i++){
                    if(timeList.get(pos).equals(yuyueList.get(i))){
                        ToastUtils.showToastShort("该时间段已被预约！请重新选择");
                        return;
                    }
                }

                String[] split = timeList.get(pos).split(":");
                Long start=null,end=null;
                if(split.length>=2){
                    if(split[1].equals("00")){
                        start = Long.valueOf(split[0])*60;
                    }else{
                        start = Long.valueOf(Long.valueOf(split[0])*60+30);
                    }

                    end = Long.valueOf(start+60);
                }

                try {
                    if(startTime!=null&&start!=null&&end!=null){
                        commitYuYue(start,end);
                    }else{
                        ToastUtils.showToastShort("请选择时间重新提交");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                        myListF = new MyListF(indexF);
//                        list_1.setAdapter(myListF);
//                        myListS = new MyListS(indexS);
//                        list_2.setAdapter(myListS);

            }
        });

        alertdialog.show();

    }

    private void getData() throws JSONException {

        TimeAxisCon timeAxis = new TimeAxisCon();
        //timeAxis.setEmployee_id(item.getEmployee_id());
        timeAxis.setWeek(weekDay);
        timeAxis.setCourse_date(Long.valueOf(startTime+"000"));
        String s = gson.toJson(timeAxis);
        JSONObject jsonObject = new JSONObject(s);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.FINDAVAI,jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                TimeAxisBean timeAxisBean = gson.fromJson(jsonObject.toString(), TimeAxisBean.class);
                List<TimeAxisBean.Data> data = timeAxisBean.getData();
                if(data!=null){
                    String time;
                    String time1,time2,time3;//time为已选时间
                    for(int i=0;i<data.size();i++){
                            if(data.get(i).getStart_time()!=null){
                                Integer start_time = data.get(i).getStart_time();
                                if(start_time%60!=0){
                                    time = start_time/60+":"+":30";
                                    time1 = start_time/60+1+":00";
                                    time2 = start_time/60+":00";
                                }else{
                                    time = start_time/60+":00";
                                    time1 = start_time/60+":30";
                                    time2 = start_time/60-1+":30";
                                }
                                yuyueList.add(time);
                                yuyueList.add(time1);
                                yuyueList.add(time2);
                                yuyueStartList.add(time);
                            }
                            status.add(data.get(i).getStatus());
                    }
                }

                for(int i = 0;i<timeList.size();i++){
                    for(int j=0;j<yuyueStartList.size();j++){
                        if(timeList.get(i).equals(yuyueStartList.get(j))){
                            yuyuestatus  = i;
                            if(i%2==0){
                                indexF.add(i);
                            }else{
                                indexS.add(i);
                            }
                        }
                    }
                }

                myListF.notifyDataSetChanged();
                myListS.notifyDataSetChanged();

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


    private void commitYuYue(Long startM,Long endM) throws JSONException {

        SiJiaoYuYueConBean siJiaoYuYueConBean = new SiJiaoYuYueConBean();
        if(role!=null){
            siJiaoYuYueConBean.setAppointment_type(1);
            siJiaoYuYueConBean.setEmployee_id(item1.getEmployee_id());
            siJiaoYuYueConBean.setMember_course_id(item1.getId());
        }else{
            siJiaoYuYueConBean.setAppointment_type(2);
            siJiaoYuYueConBean.setEmployee_id(item.getEmployee_id());
            siJiaoYuYueConBean.setMember_course_id(item.getId());
        }
        siJiaoYuYueConBean.setCourse_date(Long.valueOf(startTime+"000"));
        siJiaoYuYueConBean.setStart_time(startM);
        siJiaoYuYueConBean.setEnd_time(endM);

        siJiaoYuYueConBean.setWeek(Integer.valueOf(TimeUtils.dateToWeek(startTime+"000")));

        String s = gson.toJson(siJiaoYuYueConBean);
        JSONObject jsonObject = new JSONObject(s);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.COURSEAPPOIN, jsonObject,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                RootBean rootBean = gson.fromJson(jsonObject.toString(), RootBean.class);
                if(rootBean!=null){
                    Integer success = Integer.valueOf(rootBean.data);
                    if(success>0){
                        click = false;
                        if(pos-1>=0){
                            yuyueList.add(timeList.get(pos-1));
                        }
                        yuyueList.add(timeList.get(pos));
                        if(pos+1<=timeList.size()-1){
                            yuyueList.add(timeList.get(pos+1));
                        }
                        if(pos%2==0){
                            indexF.add(pos);
                            myListF.notifyDataSetChanged();
                        }else{
                            indexS.add(pos);
                            myListS.notifyDataSetChanged();
                        }

                    }else{
                        ToastUtils.showToastShort("失败！请重新预约！");
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


    private class MyListF extends BaseAdapter{

        RelativeLayout shixian_item;
        ArrayList<Integer> arrIndex;


        MyListF(ArrayList<Integer> arrIndex){
            this.arrIndex = arrIndex;
        }

        @Override
        public int getCount() {
            return 12;
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
            ViewHolder viewHolder;
            if (convertView==null){
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(SiJiaoDetailActivity.this).inflate(R.layout.shixian_line_item,null);
                viewHolder.shixian_item = convertView.findViewById(R.id.shixian_item);
                viewHolder.shixian_item1 = convertView.findViewById(R.id.shixian_item1);
                viewHolder.tv_status = convertView.findViewById(R.id.tv_status);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if(pos!=999){//用户选择了预约
                if(arrIndex.size()>0){
                    if(position==arrIndex.get(arrIndex.size()-1)/2){
                        viewHolder.tv_status.setText("等待对方确认");
                        viewHolder.shixian_item1.setBackgroundColor(Color.parseColor("#87CEFA"));
                        viewHolder.shixian_item1.setVisibility(View.VISIBLE);
                    }
                }

            }else{//默认时间轴（包括已经预约和预约成功）
                if(arrIndex.size()>0){
                    for(int i=0;i<arrIndex.size();i++){
                        if(arrIndex.size()<=timeList.size()){
                            if(position==arrIndex.get(i)/2){
                                if(status!=null){
                                    if(status.size()>i){
                                        if(status.get(i)==1){
                                            viewHolder.tv_status.setText("等待对方确认");
                                            viewHolder.shixian_item1.setBackgroundColor(Color.parseColor("#87CEFA"));
                                            viewHolder.shixian_item1.setVisibility(View.VISIBLE);

                                        }else if(status.get(i)==2){
                                            viewHolder.tv_status.setText("预约成功");
                                            viewHolder.shixian_item1.setBackgroundColor(Color.parseColor("#FF8C00"));
                                            viewHolder.shixian_item1.setVisibility(View.VISIBLE);
                                        }else{
                                            viewHolder.shixian_item1.setVisibility(View.VISIBLE);
                                        }
                                    }

                                }

                            }
                        }
                    }
                }
            }


            return convertView;
        }
    }

    private class MyListS extends BaseAdapter{

        RelativeLayout xuxian_item;
        ArrayList<Integer> arrIndex;


        MyListS(ArrayList<Integer> arrIndex){
            this.arrIndex = arrIndex;
        }

        @Override
        public int getCount() {
            return 12;
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
            ViewHolder viewHolder;
            if (convertView==null){
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(SiJiaoDetailActivity.this).inflate(R.layout.xuxian_list_item,null);
                viewHolder.xuxian_item = convertView.findViewById(R.id.xuxian_item);
                viewHolder.xuxian_item1 = convertView.findViewById(R.id.xuxian_item1);
                viewHolder.tv_status = convertView.findViewById(R.id.tv_status);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if(pos!=999){
                if(arrIndex.size()>0){
                    if(position==arrIndex.get(arrIndex.size()-1)/2){
                        viewHolder.tv_status.setText("等待对方确认");
                        viewHolder.xuxian_item1.setBackgroundColor(Color.parseColor("#87CEFA"));
                        // viewHolder.xuxian_item.setBackgroundColor(Color.parseColor("#61C5E7"));
                        viewHolder.xuxian_item1.setVisibility(View.VISIBLE);
                    }
                }
            }else{
                if(arrIndex.size()>0){
                    for(int i=0;i<arrIndex.size();i++){
                        if(arrIndex.size()<=timeList.size()){
                            if(position==arrIndex.get(i)/2){
                                if(status!=null){
                                    if(status.get(i)==1){
                                        viewHolder.tv_status.setText("等待对方确认");
                                        viewHolder.xuxian_item1.setBackgroundColor(Color.parseColor("#87CEFA"));
                                        viewHolder.xuxian_item1.setVisibility(View.VISIBLE);

                                    }else if(status.get(i)==2){
                                        viewHolder.tv_status.setText("预约成功");
                                        viewHolder.xuxian_item1.setBackgroundColor(Color.parseColor("#FF8C00"));
                                        viewHolder.xuxian_item1.setVisibility(View.VISIBLE);
                                    }else{
                                        viewHolder.xuxian_item1.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        }
                    }
                }
            }



            return convertView;
        }
    }
    class ViewHolder{

        RelativeLayout xuxian_item,xuxian_item1,shixian_item,shixian_item1;
        TextView tv_status;
    }
}
