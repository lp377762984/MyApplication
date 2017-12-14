package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.cn.danceland.myapplication.R;

/**
 * Created by shy on 2017/12/13 10:15
 * Email:644563767@qq.com
 */


public class MyShopActivity extends Activity implements View.OnClickListener{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shop);
        initView();
        initData();
    }

    private void initView() {
        findViewById(R.id.iv_back).setOnClickListener(this);
    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
        case R.id.iv_back:
            finish();
        break;
        default:
        break;
        }
    }
}
