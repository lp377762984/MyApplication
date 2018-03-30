package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
    ArrayList<Integer> arrPositionF,arrPositionS;
    ArrayList<Integer> arrStatusF,arrStatusS;
    HashMap<Integer,Integer> arrPosition,arrStatus;
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
    long hourMill = 3600000;
    ArrayList<Long> startMillArr;
    ArrayList<Integer> requestStatusArr;
    Data data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sijiao_detail);
        initHost();
        iniView();
    }


    private void initHost() {

        startMillArr = new ArrayList<>();
        requestStatusArr = new ArrayList<>();

        gson = new Gson();

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
        timeList = new ArrayList<String>();
        for(int i =0;i<time.length-2;i++){
            timeList.add(time[i]);
        }
        nccalendar = findViewById(R.id.nccalendar);
        nccalendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChanged(DateTime dateTime) {
                if(dateTime!=null){
                    arrPosition.clear();
                    arrStatus.clear();
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
        arrPosition = new HashMap<>();
        arrPositionF = new ArrayList<>();
        arrPositionS = new ArrayList<>();

        for(int i = 0;i < 23;i++){
            arrPosition.put(i,0);
        }

        arrStatus = new HashMap<>();
        arrStatusF = new ArrayList<>();
        arrStatusS = new ArrayList<>();
        for(int i = 0;i < 23;i++){
            arrStatus.put(i,0);
        }
        myListF = new MyListF(arrPositionF,arrStatusF);
        myListS = new MyListS(arrPositionS,arrStatusS);
        list_1.setAdapter(myListF);
        list_2.setAdapter(myListS);
//        list_1.setAdapter(myListF);
//        list_2.setAdapter(myListS);
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
        getData();
    }

    private void showTime() {
        ViewGroup parent = (ViewGroup)inflate.getParent();
        if(parent!=null){
            parent.removeAllViews();
        }

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
                for(int i=0;i<arrPosition.size();i++){
                    if(pos==arrPosition.get(i)){
                        ToastUtils.showToastShort("该时间段已被预约！请重新选择");
                        return;
                    }

                }

                String[] split = timeList.get(pos).split(":");
                Long start=null,end=null;
                if(split.length>=2) {
                    if (split[1].equals("00")) {
                        start = Long.valueOf(split[0]) * 60;
                    } else {
                        start = Long.valueOf(Long.valueOf(split[0]) * 60 + 30);
                    }

                    end = Long.valueOf(start + 60);
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

            }
        });

        alertdialog.show();

    }

    private void getData(){

        TimeAxisCon timeAxis = new TimeAxisCon();
        if(item!=null){
            timeAxis.setEmployee_id(item.getEmployee_id());
        }else if(item1!=null){
            timeAxis.setEmployee_id(item1.getEmployee_id());
        }
        timeAxis.setWeek(weekDay);
        timeAxis.setCourse_date(Long.valueOf(startTime));
        String s = gson.toJson(timeAxis);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.FINDAVAI,s, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                TimeAxisBean timeAxisBean = gson.fromJson(jsonObject.toString(), TimeAxisBean.class);
                List<TimeAxisBean.Data> data = timeAxisBean.getData();
                SharedPreferences myYuYue = getSharedPreferences("myYuYue", MODE_PRIVATE);
                String myYuYueString = myYuYue.getString(startTime,null);

                if(data!=null){
                    for(int i=0;i<data.size();i++){
                            if(data.get(i).getStart_time()!=null){
                                Integer start_time = data.get(i).getStart_time();
                                setTimeLine(data.get(i).getStatus(),start_time*60000);
                            }
                    }
                }
                getHistory();
//                list_1.setAdapter(myListF);
//                list_2.setAdapter(myListS);
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


    private void getHistory(){

        SiJiaoYuYueConBean siJiaoYuYueConBean = new SiJiaoYuYueConBean();
        if(role!=null){
            siJiaoYuYueConBean.setEmployee_id(data.getEmployee().getId());
        }else{
            siJiaoYuYueConBean.setMember_no(data.getPerson().getMember_no());
        }

        if(startTime!=null){
            siJiaoYuYueConBean.setCourse_date(Long.valueOf(startTime));
        }else{
            siJiaoYuYueConBean.setCourse_date(System.currentTimeMillis());
        }
        String s = gson.toJson(siJiaoYuYueConBean);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.APPOINTLIST, s,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtil.e("zzf",jsonObject.toString());
                SiJiaoRecordBean siJiaoRecordBean = gson.fromJson(jsonObject.toString(), SiJiaoRecordBean.class);
                if(siJiaoRecordBean!=null){
                    SiJiaoRecordBean.Data data = siJiaoRecordBean.getData();
                    if(data!=null){
                        List<SiJiaoRecordBean.Content> content = data.getContent();
                        if(content!=null){
                            for(int i=0;i<content.size();i++){
                                Integer start_time = content.get(i).getStart_time();
                                setTimeLine(content.get(i).getStatus(),start_time*60000);
                            }
                            arrPositionS.clear();
                            arrStatusS.clear();
                            arrStatusF.clear();
                            arrPositionF.clear();
                            if(arrPosition.size()>0){
                                for(int j=0;j<arrPosition.size();j++){
                                    if(arrPosition.get(j)!=0){
                                        if(j%2==0){
                                            arrPositionF.add(3);
                                            arrStatusF.add(arrStatus.get(j));
                                        }else{
                                            arrPositionS.add(3);
                                            arrStatusS.add(arrStatus.get(j));
                                        }
                                    }else{
                                        if(j%2==0){
                                            arrPositionF.add(0);
                                            arrStatusF.add(0);
                                        }else{
                                            arrPositionS.add(0);
                                            arrStatusS.add(0);
                                        }
                                    }
                                }
                            }
                            myListF.notifyDataSetChanged();
                            myListS.notifyDataSetChanged();
//                            arrPositionF.clear();
//                            arrStatusF.clear();
//                            arrPositionS.clear();
//                            arrStatusS.clear();
                        }else{
                            ToastUtils.showToastShort("当天无预约记录");
                        }

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
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<String,String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN,""));
                return map;
            }

        };

        MyApplication.getHttpQueues().add(jsonObjectRequest);

    }


    private void commitYuYue(final Long startM, Long endM) throws JSONException {

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
        siJiaoYuYueConBean.setCourse_date(Long.valueOf(startTime));
        siJiaoYuYueConBean.setStart_time(startM);
        siJiaoYuYueConBean.setEnd_time(endM);

        siJiaoYuYueConBean.setWeek(Integer.valueOf(TimeUtils.dateToWeek(startTime)));

        String s = gson.toJson(siJiaoYuYueConBean);
        JSONObject jsonObject = new JSONObject(s);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.COURSEAPPOIN, jsonObject,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                RootBean rootBean = gson.fromJson(jsonObject.toString(), RootBean.class);
                if(rootBean!=null){
                    Integer success = Integer.valueOf(rootBean.data);
                    if(success>0){
                        for(int i = 0;i < 23;i++){
                            arrPosition.put(i,0);
                        }
                        getHistory();
                        ToastUtils.showToastShort("预约成功！");
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
        ArrayList<Integer> arrPositionF;
        ArrayList<Integer> arrStatusF;


        MyListF(ArrayList<Integer> arrPosition,ArrayList<Integer> arrStatus){
            this.arrPositionF = arrPosition;
            this.arrStatusF = arrStatus;
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

            //for (int i=0;i<arrPosition.size();i++){
            if(arrPositionF.size()>0){
                if(arrPositionF.get(position)!=0){
                    if(arrStatusF.get(position)==1){
                        viewHolder.tv_status.setText("等待对方确认");
                        viewHolder.shixian_item1.setBackgroundColor(Color.parseColor("#87CEFA"));
                        viewHolder.shixian_item1.setVisibility(View.VISIBLE);
                    }else if(arrStatusF.get(position)==2){
                        viewHolder.tv_status.setText("已确认未签到");
                        viewHolder.shixian_item1.setBackgroundColor(Color.parseColor("#FF8C00"));
                        viewHolder.shixian_item1.setVisibility(View.VISIBLE);
                    }else if(arrStatusF.get(position)==3){
                        viewHolder.tv_status.setText("已取消");
                        viewHolder.shixian_item1.setBackgroundColor(Color.parseColor("#A9A9A9"));
                        viewHolder.shixian_item1.setVisibility(View.VISIBLE);
                    }else if(arrStatusF.get(position)==4){
                        viewHolder.tv_status.setText("已签到");
                        viewHolder.shixian_item1.setBackgroundColor(Color.parseColor("#A9A9A9"));
                        viewHolder.shixian_item1.setVisibility(View.VISIBLE);

                    }
                }else{
                    viewHolder.shixian_item1.setVisibility(View.GONE);
                }
            }

               // break;
            //}
            return convertView;
        }
    }

    private class MyListS extends BaseAdapter{
        ArrayList<Integer> arrPositionS;
        ArrayList<Integer> arrStatusS;


        MyListS(ArrayList<Integer> arrPosition,ArrayList<Integer> arrStatus){
            this.arrPositionS = arrPosition;
            this.arrStatusS = arrStatus;
        }

        @Override
        public int getCount() {
            return 11;
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

            //for (int i=0;i<arrPosition.size();i++){
            if(arrPositionS.size()>0){
                if(arrPositionS.get(position)!=0){
                    if(arrStatusS.get(position)==1){
                        viewHolder.tv_status.setText("等待对方确认");
                        viewHolder.xuxian_item1.setBackgroundColor(Color.parseColor("#87CEFA"));
                        viewHolder.xuxian_item1.setVisibility(View.VISIBLE);
                        viewHolder.xuxian_item.setVisibility(View.GONE);
                    }else if(arrStatusS.get(position)==2){
                        viewHolder.tv_status.setText("已确认未签到");
                        viewHolder.xuxian_item1.setBackgroundColor(Color.parseColor("#FF8C00"));
                        viewHolder.xuxian_item1.setVisibility(View.VISIBLE);
                        viewHolder.xuxian_item.setVisibility(View.GONE);
                    }else if(arrStatusS.get(position)==3){
                        viewHolder.tv_status.setText("已取消");
                        viewHolder.xuxian_item1.setBackgroundColor(Color.parseColor("#A9A9A9"));
                        viewHolder.xuxian_item1.setVisibility(View.VISIBLE);
                        viewHolder.xuxian_item.setVisibility(View.GONE);
                    }else if(arrStatusS.get(position)==4){
                        viewHolder.tv_status.setText("已签到");
                        viewHolder.xuxian_item1.setBackgroundColor(Color.parseColor("#A9A9A9"));
                        viewHolder.xuxian_item1.setVisibility(View.VISIBLE);
                        viewHolder.xuxian_item.setVisibility(View.GONE);
                    }
                }else{
                    viewHolder.xuxian_item1.setVisibility(View.GONE);
                    viewHolder.xuxian_item.setVisibility(View.VISIBLE);
                }

            }

                //break;
            //}
            return convertView;
        }
    }
    class ViewHolder{

        RelativeLayout xuxian_item,xuxian_item1,shixian_item,shixian_item1;
        TextView tv_status;
    }


    private void setTimeLine(int status,long startTime){
        ArrayList<Long> longArr = new ArrayList<>();
        long nine = hourMill*9;
        long nine_half = hourMill*9+1800000;
        long ten = hourMill*10;
        long ten_half = hourMill*10+1800000;
        long eleven = hourMill*11;
        long eleven_half = hourMill*11+1800000;
        long twelve = hourMill*12;
        long twelve_half = hourMill*12+1800000;
        long thirteen = hourMill*13;
        long thirteen_half = hourMill*13+1800000;
        long fourteen = hourMill*14;
        long fourteen_half = hourMill*14+1800000;
        long fifteen = hourMill*15;
        long fifteen_half = hourMill*15+1800000;
        long sixteen = hourMill*16;
        long sixteen_half = hourMill*16+1800000;
        long seventeen = hourMill*17;
        long seventeen_half = hourMill*17+1800000;
        long eighteen = hourMill*18;
        long eighteen_half = hourMill*18+1800000;
        long nineteen = hourMill*19;
        long nineteen_half = hourMill*19+1800000;
        long twenty = hourMill*20;

        longArr.add(nine);
        longArr.add(nine_half);
        longArr.add(ten);
        longArr.add(ten_half);
        longArr.add(eleven);
        longArr.add(eleven_half);
        longArr.add(twelve);
        longArr.add(twelve_half);
        longArr.add(thirteen);
        longArr.add(thirteen_half);
        longArr.add(fourteen);
        longArr.add(fourteen_half);
        longArr.add(fifteen);
        longArr.add(fifteen_half);
        longArr.add(sixteen);
        longArr.add(sixteen_half);
        longArr.add(seventeen);
        longArr.add(seventeen_half);
        longArr.add(eighteen);
        longArr.add(eighteen_half);
        longArr.add(nineteen);
        longArr.add(nineteen_half);
        longArr.add(twenty);

        arrAdd(startTime,status,longArr);

    }

    private void arrAdd(long starTime,int status,ArrayList<Long> longArr){
            for(int i=0;i<longArr.size();i++){
                //if(arrPosition.size()<=longArr.size()){
                    if(longArr.get(i)==starTime){
                        arrPosition.put(i,1);
                        if(status==1){
                            arrStatus.put(i,1);
                        }else if(status==2){
                            arrStatus.put(i,2);
                        }else if(status==3){
                            arrStatus.put(i,3);
                        }else if(status==4){
                            arrStatus.put(i,4);
                        }
                    }
                //}
            }

    }

}
