package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.view.DongLanTitleView;

/**
 * Created by feng on 2018/5/2.
 */

public class MyConsumeActivity extends Activity {

    DongLanTitleView consume_title;
    ListView lv_consume;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_myconsume);
        initHost();
        initView();

    }

    private void initView() {

        consume_title = findViewById(R.id.consume_title);
        consume_title.setTitle("我的消费");
        lv_consume = findViewById(R.id.lv_consume);

    }

    private void initHost() {


    }
}
