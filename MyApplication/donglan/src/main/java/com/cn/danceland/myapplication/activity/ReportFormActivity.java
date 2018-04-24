package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.view.CustomDatePicker;

/**
 * Created by feng on 2018/4/24.
 */

public class ReportFormActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_report);
        initHost();
        initView();
    }

    private void initView() {

    }

    private void initHost() {
        final CustomDatePicker customDatePicker = new CustomDatePicker(this, "选择日期");
        customDatePicker.setDialogOnClickListener(new CustomDatePicker.OnClickEnter() {
            @Override
            public void onClick() {
                customDatePicker.getTimeString();
            }
        });

        customDatePicker.showWindow();
    }
}
