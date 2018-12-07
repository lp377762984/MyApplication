package com.cn.danceland.myapplication.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.activity.base.BaseActivity;
import com.cn.danceland.myapplication.view.StepArcView;

/**
 * Created by shy on 2018/8/21 09:18
 * Email:644563767@qq.com
 */


public class TestActivity extends BaseActivity {
    StepArcView stepArcView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        stepArcView = findViewById(R.id.sav_step);
        stepArcView.setCurrentCount(1000,1000);

    }
}
