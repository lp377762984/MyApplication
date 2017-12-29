package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cn.danceland.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * Created by feng on 2017/12/27.
 */

public class FitnessTestActivity extends Activity {
    PieChartView pie_chart;
    PieChartData pieChardata;
    RelativeLayout rl_age;
    ImageView fitness_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fitnesstest);
        initView();
        initPie();
    }

    private void initView() {

        pie_chart = findViewById(R.id.pie_chart);
        fitness_back = findViewById(R.id.fitness_back);
        fitness_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rl_age = findViewById(R.id.rl_age);
        rl_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FitnessTestActivity.this,BodyAgeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initPie(){
        /**
         * 初始化
         */
        List<SliceValue> values = new ArrayList<SliceValue>();
        pieChardata = new PieChartData();
            pieChardata.setHasLabels(true);//显示表情
            pieChardata.setHasLabelsOnlyForSelected(false);//不用点击显示占的百分比
            pieChardata.setHasLabelsOutside(true);//占的百分比是否显示在饼图外面
            pieChardata.setHasCenterCircle(true);//是否是环形显示
            pieChardata.setSlicesSpacing(0);
            Double[] lv = {62.3,14.7,5.5,17.6};
            Integer[] color = {0xFFF5F5F5,0xFFD3D3D3,0xFFA9A9A9,0xFF696969};
            String[] str = {"水分","脂肪","骨质","蛋白质"};
            for(int i=0;i<=3;i++){
                SliceValue sliceValue = new SliceValue(lv[i].floatValue(), color[i]);//这里的颜色是我写了一个工具类 是随机选择颜色的
                sliceValue.setLabel(str[i]+" "+lv[i]+"%");
                values.add(sliceValue);
            }

            pieChardata.setValues(values);//填充数据
            pieChardata.setCenterCircleColor(Color.WHITE);//设置环形中间的颜色
            pieChardata.setCenterCircleScale(0.65f);//设置环形的大小级别
            pieChardata.setCenterText1("身体成分");//环形中间的文字1
            pieChardata.setCenterText1Color(Color.BLACK);//文字颜色
            pieChardata.setCenterText1FontSize(12);//文字大小

            pie_chart.setPieChartData(pieChardata);
            pie_chart.setViewportCalculationEnabled(true);
            pie_chart.setValueSelectionEnabled(false);//选择饼图某一块变大
            pie_chart.setAlpha(0.9f);//设置透明度
            pie_chart.setCircleFillRatio(1f);//设置饼图大小

    }
}
