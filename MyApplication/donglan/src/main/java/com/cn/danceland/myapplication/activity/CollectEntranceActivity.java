package com.cn.danceland.myapplication.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.view.DongLanTitleView;
import com.cn.danceland.myapplication.view.NoScrollListView;

/**
 * 我的收藏
 * Created by yxx on 2018-10-24.
 */

public class CollectEntranceActivity extends BaseActivity {
    private Context context;
    private LinearLayout column_layout;
    private LinearLayout more_layout;//周月布局
    private LinearLayout col_layout;//日布局
    private NoScrollListView listview;
    private TextView sleep_detail_tv;//上面最小的蓝色item描述

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_entrance);
        context = this;
        initView();
    }

    private void initView() {
        column_layout = (LinearLayout) findViewById(R.id.column_layout);
        more_layout = (LinearLayout) findViewById(R.id.more_layout);
        col_layout = (LinearLayout) findViewById(R.id.col_layout);
        listview = (NoScrollListView) findViewById(R.id.listview);
        sleep_detail_tv = (TextView) findViewById(R.id.sleep_detail_tv);
        initData();
    }

    private void initData() {

    }
}
