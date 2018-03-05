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
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.fragment.CommentFragment;
import com.cn.danceland.myapplication.fragment.SiJiaoFragment;
import com.cn.danceland.myapplication.fragment.TuanKeFragment;
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
    ImageView course_back;
    NCalendar nccalendar;
    TextView tv_date;
    RelativeLayout rl_nv;
    TabItem tab1,tab2;
    TabLayout tablayout;
    SiJiaoFragment siJiaoFragment;
    String type;
    Long startTime,endTime;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course);
        initHost();
        initView();
        setOnclick();
    }

    private void initHost() {


    }

    private void setOnclick() {

        nccalendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChanged(DateTime dateTime) {
                if(dateTime!=null){
                    String[] ts = dateTime.toString().split("T");
                    tv_date.setText(ts[0]);

                    startTime = TimeUtils.date2TimeStamp(ts[0]+" 00:00:00", "yyyy-MM-dd 00:00:00");
                    endTime = startTime+86399;
                    showFragment(type);
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
        tab1 = findViewById(R.id.tab1);
        tab2 = findViewById(R.id.tab2);
        rl_nv = findViewById(R.id.rl_nv);
        showFragment("0");
        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        type = "0";
                        showFragment(type);
                        break;
                    case 1:
                        type="1";
                        showFragment(type);
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


    public void showFragment(String str){
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if("0".equals(str)){
            siJiaoFragment = new SiJiaoFragment();
            fragmentTransaction.replace(R.id.rl_nv,siJiaoFragment);
//            tuanKeFragment = new TuanKeFragment();
//            fragmentTransaction.replace(R.id.rl_nv,tuanKeFragment);
        }else if("1".equals(str)){

//            try {
//                siJiaoFragment.refresh(startTime,endTime);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

        }

        fragmentTransaction.commit();
    }
}
