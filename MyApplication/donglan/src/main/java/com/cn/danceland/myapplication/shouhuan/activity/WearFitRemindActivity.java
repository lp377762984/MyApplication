package com.cn.danceland.myapplication.shouhuan.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.view.DongLanTitleView;

/**
 * Created by feng on 2018/6/22.
 */

public class WearFitRemindActivity extends Activity {

    private DongLanTitleView shouhuan_remind_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wearfitremind);
        initView();
    }

    private void initView() {

        shouhuan_remind_title = findViewById(R.id.shouhuan_remind_title);
        shouhuan_remind_title.setTitle("APP提醒");

    }
}
