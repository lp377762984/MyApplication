package com.cn.danceland.myapplication.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.view.RulerView;

/**
 * Created by shy on 2018/10/18 10:13
 * Email:644563767@qq.com
 */


public class SetRegisterInfoSaveActivity extends BaseActivity {
   String gender,name,birthday;
    private TextView tv_height,tv_weight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_register_info_save);
        initdata();
        initView();
     
    }

    private void initView() {

        RulerView    ruler_height=(RulerView)findViewById(R.id.ruler_height);
        tv_height = findViewById(R.id.tv_height);
        ruler_height.setOnValueChangeListener(new RulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                tv_height.setText(value+"cm");
                LogUtil.i(value+"");
            }
        });
        RulerView    ruler_weight=(RulerView)findViewById(R.id.ruler_weight);
        tv_weight = findViewById(R.id.tv_weight);
        ruler_weight.setOnValueChangeListener(new RulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                tv_weight.setText(value+"kg");
                LogUtil.i(value+"");
            }
        });

/**
 *
 * @param selectorValue 未选择时 默认的值 滑动后表示当前中间指针正在指着的值
 * @param minValue   最大数值
 * @param maxValue   最小的数值
 * @param per   最小单位  如 1:表示 每2条刻度差为1.   0.1:表示 每2条刻度差为0.1 在demo中 身高mPerValue为1  体重mPerValue 为0.1
 */
        ruler_height.setValue(165, 50, 300, 1);//身高
        ruler_weight.setValue(50, 30, 200, 0.1f);//体重


    }

    private void initdata() {
        gender=getIntent().getStringExtra("gender");
        name=getIntent().getStringExtra("name");
        birthday=getIntent().getStringExtra("birthday");
    }


}
