package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Spinner;

import com.cn.danceland.myapplication.R;

/**
 * Created by 64456 on 2017/9/22.
 */

public class LoginSMSActivity extends Activity {
    private Spinner mSpinner;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activit_login_sms);
        initView();
    }

    private void initView() {
        mSpinner=findViewById(R.id.sp_phone);
        mSpinner.setSelection(0,true);
    }
    //返回
    public void back(View view) {
        finish();
    }
}
