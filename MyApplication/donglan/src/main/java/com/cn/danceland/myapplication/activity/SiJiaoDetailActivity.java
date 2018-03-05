package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.MyCourseBean;
import com.necer.ncalendar.calendar.NCalendar;
import com.necer.ncalendar.listener.OnCalendarChangedListener;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    List<String> timeList;
    TextView over_time,tv_date,course_name,course_jiaolian,buy,course_shengyu,course_length;
    int pos=999;
    ImageView sijiao_back,detail_img;
    MyListF myListF;
    MyListS myListS;
    AlertDialog.Builder alertdialog;
    NCalendar nccalendar;
    MyCourseBean.Data item;
    String startTime,endTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sijiao_detail);
        initHost();
        iniView();
    }

    private void initHost() {
        item = (MyCourseBean.Data)getIntent().getSerializableExtra("item");
        startTime = getIntent().getStringExtra("startTime");
        endTime = getIntent().getStringExtra("endTime");
    }

    private void iniView() {

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
        }
        course_length.setText("有效期："+startTime+"至"+endTime);

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
        nccalendar = findViewById(R.id.nccalendar);

        nccalendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChanged(DateTime dateTime) {
                if(dateTime!=null){
                    String[] ts = dateTime.toString().split("T");
                    tv_date.setText(ts[0]);
                }
            }


        });
        list_1 = findViewById(R.id.list_1);
        list_1.setDividerHeight(0);
        list_2 = findViewById(R.id.list_2);
        list_2.setDividerHeight(0);

        myListF = new MyListF(pos);

        myListS = new MyListS(pos);
        list_1.setAdapter(myListF);
        list_2.setAdapter(myListS);
//        list_1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                showTime();
//            }
//        });
        list_2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showTime();
            }
        });
    }

    private void showTime() {
        ViewGroup parent = (ViewGroup)inflate.getParent();
        if(parent!=null){
            parent.removeAllViews();
        }

        timeList = new ArrayList<String>();
        for(int i =0;i<time.length-2;i++){
            timeList.add(time[i]);
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
                over_time.setText(time[index+2]+"结束");
            }
        });
        alertdialog.setTitle("选择预约时间");
        alertdialog.setView(inflate);
        alertdialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(pos==999){
                    pos= 0;
                }
                if(timeList.get(pos).contains("30")){
                    myListS = new MyListS(pos);
                    list_2.setAdapter(myListS);
                }else{
                    myListF = new MyListF(pos);
                    list_1.setAdapter(myListF);
                }
            }
        });

        alertdialog.show();

    }

    private class MyListF extends BaseAdapter{

        RelativeLayout shixian_item;
        int po=999;


        MyListF(int pos){

            po = pos;
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
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
                if(position==(po/2)){
                    viewHolder.shixian_item1.setVisibility(View.VISIBLE);
                }else{
                    viewHolder.shixian_item1.setVisibility(View.GONE);
                }
            return convertView;
        }
    }

    private class MyListS extends BaseAdapter{

        RelativeLayout xuxian_item;
        int po=999;

        MyListS(int pos){
            po = pos;

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
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if(position==(po/2)){
               // viewHolder.xuxian_item.setBackgroundColor(Color.parseColor("#61C5E7"));
                viewHolder.xuxian_item1.setVisibility(View.VISIBLE);
            }else{
                viewHolder.xuxian_item1.setVisibility(View.GONE);
            }



            return convertView;
        }
    }
    class ViewHolder{

        RelativeLayout xuxian_item,xuxian_item1,shixian_item,shixian_item1;
    }
}
