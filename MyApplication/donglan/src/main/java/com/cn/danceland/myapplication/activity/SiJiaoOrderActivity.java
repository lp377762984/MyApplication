package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.cn.danceland.myapplication.R;

/**
 * Created by feng on 2018/1/15.
 */

public class SiJiaoOrderActivity extends Activity {

    String type;
    RelativeLayout rl_jiaolian,rl_kaikeshijian,rl_name,rl_phone;
    RadioButton btn_forme,btn_foryou,btn_zhifubao,btn_weixin;
    View line7;
    ImageView back_img;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sijiaoorder);
        initHost();
        initView();
    }

    private void initHost() {


        type = getIntent().getStringExtra("type");

    }

    private void initView() {

        rl_jiaolian = findViewById(R.id.rl_jiaolian);
        if("1".equals(type)){
            rl_jiaolian.setVisibility(View.GONE);
        }else{
            rl_jiaolian.setVisibility(View.VISIBLE);
        }

        btn_forme = findViewById(R.id.btn_forme);
        btn_foryou = findViewById(R.id.btn_foryou);
        rl_kaikeshijian = findViewById(R.id.rl_kaikeshijian);
        rl_name = findViewById(R.id.rl_name);
        line7 = findViewById(R.id.line7);
        rl_phone = findViewById(R.id.rl_phone);
        btn_zhifubao = findViewById(R.id.btn_zhifubao);
        btn_weixin = findViewById(R.id.btn_weixin);
        back_img = findViewById(R.id.back_img);

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        btn_forme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_forme.setChecked(true);
                btn_foryou.setChecked(false);
                rl_kaikeshijian.setVisibility(View.VISIBLE);
                rl_name.setVisibility(View.GONE);
                line7.setVisibility(View.GONE);
                rl_phone.setVisibility(View.GONE);
                if("1".equals(type)){
                    rl_jiaolian.setVisibility(View.GONE);
                }

            }
        });

        btn_foryou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_forme.setChecked(false);
                btn_foryou.setChecked(true);
                rl_kaikeshijian.setVisibility(View.GONE);
                rl_name.setVisibility(View.VISIBLE);
                line7.setVisibility(View.VISIBLE);
                rl_phone.setVisibility(View.VISIBLE);
                if("1".equals(type)){
                    rl_jiaolian.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_zhifubao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_zhifubao.setChecked(true);
                btn_weixin.setChecked(false);
            }
        });

        btn_weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_zhifubao.setChecked(false);
                btn_weixin.setChecked(true);
            }
        });

    }
}
