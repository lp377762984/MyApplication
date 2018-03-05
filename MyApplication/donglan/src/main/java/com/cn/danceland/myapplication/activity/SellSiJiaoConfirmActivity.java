package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.cn.danceland.myapplication.R;

import java.io.Serializable;

/**
 * Created by feng on 2018/1/15.
 */

public class SellSiJiaoConfirmActivity extends Activity {
    int state=999;

    RelativeLayout rl_buy;
    CheckBox btn_sijiao,btn_sijiaodingjin;
    ImageView sell_img;
    Serializable itemContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sellsijiaoconfirm);

        itemContent = getIntent().getSerializableExtra("itemContent");
        initView();
    }

    private void initView() {
        sell_img = findViewById(R.id.sell_img);
        sell_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rl_buy = findViewById(R.id.rl_buy);
        btn_sijiao = findViewById(R.id.btn_sijiao);
        btn_sijiaodingjin = findViewById(R.id.btn_sijiaodingjin);

        btn_sijiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_sijiao.setChecked(true);
                btn_sijiaodingjin.setChecked(false);
                state = 0;
            }
        });

        btn_sijiaodingjin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_sijiao.setChecked(false);
                btn_sijiaodingjin.setChecked(true);
                state = 1;
            }
        });

        rl_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state==0){
                    startActivity(new Intent(SellSiJiaoConfirmActivity.this,SiJiaoOrderActivity.class).putExtra("type","0").putExtra("itemContent",itemContent));
                }else if(state==1){
                    startActivity(new Intent(SellSiJiaoConfirmActivity.this,SiJiaoOrderActivity.class).putExtra("type","1").putExtra("itemContent",itemContent));
                }

            }
        });

    }
}
