package com.cn.danceland.myapplication.shouhuan.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.cn.danceland.myapplication.R;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;

import java.util.ArrayList;

/**
 * Created by feng on 2018/6/27.
 */

public class ClockSettingActivity extends Activity {

    private LoopView lp_hour, lp_minute;
    private String shour, sminute;
    private ArrayList<String> hourList;
    private ArrayList<String> minuteList;
    private Button btn_save;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clocksetting);
        initView();
    }

    private void initView() {
        btn_save = findViewById(R.id.btn_save);
        lp_hour = findViewById(R.id.lp_hour);
        lp_minute = findViewById(R.id.lp_minute);
        initLoopData();
        setClickListener();
    }

    private void setClickListener() {

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("time",shour+":"+sminute);
                setResult(3,intent);
                finish();
            }
        });

    }

    private void initLoopData() {
        hourList = new ArrayList<>();
        minuteList = new ArrayList<>();
        for (int x = 0; x < 24; x++) {
            if (x < 10) {
                hourList.add("0" + x);
            } else {
                hourList.add(x + "");
            }
        }
        for (int y = 0; y < 60; y++) {
            if (y < 10) {
                minuteList.add("0" + y);
            } else {
                minuteList.add(y + "");
            }

        }

        lp_hour.setItems(hourList);
        lp_minute.setItems(minuteList);

        lp_hour.setTextSize(18);
        lp_minute.setTextSize(18);
        lp_hour.setItemsVisibleCount(7);
        lp_minute.setItemsVisibleCount(7);

        lp_hour.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                shour = hourList.get(index);
            }
        });
        lp_minute.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                sminute = minuteList.get(index);
            }
        });


    }
}
