package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.format.Time;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.fragment.CommentFragment;
import com.cn.danceland.myapplication.fragment.SiJiaoFragment;
import com.cn.danceland.myapplication.fragment.SiJiaoRecordFragment;
import com.cn.danceland.myapplication.fragment.TuanKeFragment;
import com.cn.danceland.myapplication.fragment.TuanKeRecordFragment;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.TimeUtils;
import com.necer.ncalendar.calendar.NCalendar;
import com.necer.ncalendar.listener.OnCalendarChangedListener;

import org.joda.time.DateTime;
import org.json.JSONException;


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
    String nowTime,nowTimeLen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course);
        initHost();
        initView();
        setOnclick();
    }

    private void initHost() {
        isTuanke = getIntent().getStringExtra("isTuanke");
        if(isTuanke==null){
            isTuanke = "1";
        }
        time = new Time();
        time.setToNow();

        nowTime = time.year+"-"+time.month+"-"+time.monthDay+" 00:00:00";
        startTime = TimeUtils.date2TimeStamp(nowTime,"yyyy-MM-dd 00:00:00")+"";

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

    private void initView() {
        course_back = findViewById(R.id.course_back);
        course_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                    tuanKeFragment.refresh("免费团课",startTime,endTime,course_type_id,id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                fragmentTransaction.replace(R.id.rl_nv,tuanKeFragment);
            }else{
                siJiaoFragment = new SiJiaoFragment();
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
                fragmentTransaction.replace(R.id.rl_nv,siJiaoRecordFragment);
            }

        }else if("2".equals(type)){
            tuanKeFragment = new TuanKeFragment();
            try {
                tuanKeFragment.refresh("小团课",startTime,endTime,course_type_id,id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            fragmentTransaction.replace(R.id.rl_nv,tuanKeFragment);
        }

        fragmentTransaction.commit();
    }
}
