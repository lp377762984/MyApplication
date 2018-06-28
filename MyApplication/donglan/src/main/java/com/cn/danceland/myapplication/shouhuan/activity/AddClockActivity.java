package com.cn.danceland.myapplication.shouhuan.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.view.DongLanTitleView;

/**
 * Created by feng on 2018/6/27.
 */

public class AddClockActivity extends Activity {

    private DongLanTitleView addclock_title;
    private TextView rightTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addclock);
        initView();
    }

    private void initView() {
        addclock_title = findViewById(R.id.addclock_title);
        addclock_title.setTitle("闹钟提醒");
        rightTv = addclock_title.getRightTv();
        rightTv.setVisibility(View.VISIBLE);
        rightTv.setText("添加");
        rightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
