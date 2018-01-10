package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.cn.danceland.myapplication.R;

/**
 * Created by shy on 2018/1/8 16:00
 * Email:644563767@qq.com
 * 潜客回访
 */


public class PotentialCustomerRevisitActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_potential_customer_revisit);
        initView();
    }

    private void initView() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_add).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                break;
            case R.id.btn_add:
                startActivity(new Intent(PotentialCustomerRevisitActivity.this,AddPotentialActivity.class));
                break;
            default:
                break;
        }
    }
}
