package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.cn.danceland.myapplication.R;

import static com.cn.danceland.myapplication.R.id.btn_commit;
import static com.cn.danceland.myapplication.R.id.iv_back;

/**
 * Created by shy on 2017/10/13 11:42
 * Email:644563767@qq.com
 */


public class ConfirmPasswordActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_password);
        initView();
    }

    private void initView() {
        findViewById(btn_commit).setOnClickListener(this);
        findViewById(iv_back).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case btn_commit:
                startActivity(new Intent(ConfirmPasswordActivity.this,ChangePhoneActivity.class));
                break;
            case iv_back:
                finish();
                break;
            default:
                break;
        }
    }
}
