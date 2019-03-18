package com.cn.danceland.myapplication.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.activity.base.BaseActivity;
import com.cn.danceland.myapplication.bean.SiJiaoYuYueBean;
import com.cn.danceland.myapplication.view.CustomLine2;
import com.cn.danceland.myapplication.view.StepArcView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shy on 2018/8/21 09:18
 * Email:644563767@qq.com
 */


public class TestActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        LinearLayout ll = findViewById(R.id.ll);
        ll.addView(new CustomLine2(this, null));
    }
}
