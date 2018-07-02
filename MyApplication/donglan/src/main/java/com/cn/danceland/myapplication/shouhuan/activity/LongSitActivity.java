package com.cn.danceland.myapplication.shouhuan.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.view.CustomDatePicker;
import com.cn.danceland.myapplication.view.DongLanTitleView;

/**
 * Created by feng on 2018/6/28.
 */

public class LongSitActivity extends Activity {

    private DongLanTitleView longsit_title;
    private TextView tv_start;
    private TextView tv_end;
    private TextView tv_tixing;
    private String from;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_longsit);
        from = getIntent().getStringExtra("from");
        initView();
    }

    private void initView() {
        longsit_title = findViewById(R.id.longsit_title);
        tv_tixing = findViewById(R.id.tv_tixing);
        if("久坐提醒".equals(from)){
            longsit_title.setTitle("久坐提醒");
            tv_tixing.setText("久坐提醒");
        }else if("勿扰模式".equals(from)){
            longsit_title.setTitle("久坐提醒");
            tv_tixing.setText("勿扰模式");
        }

        tv_start = findViewById(R.id.tv_start);
        tv_end = findViewById(R.id.tv_end);

        setClick();
    }

    private void setClick() {
        tv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeSelect("开始时间");
            }
        });
        tv_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeSelect("结束时间");
            }
        });

    }

    private void showTimeSelect(final String str) {
        final CustomDatePicker customDatePicker = new CustomDatePicker(this, str);
        customDatePicker.setGoneYearAndMounth();
        customDatePicker.showWindow();
        customDatePicker.setDialogOnClickListener(new CustomDatePicker.OnClickEnter() {
            @Override
            public void onClick() {
                String dateString = customDatePicker.getTime();
                if("开始时间".equals(str)){
                    tv_start.setText(dateString);
                }else if ("结束时间".equals(str)){
                    tv_end.setText(dateString);
                }

            }
        });

    }
}
