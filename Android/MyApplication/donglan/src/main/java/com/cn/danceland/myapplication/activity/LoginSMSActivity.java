package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.utils.PhoneFormatCheckUtils;

/**
 * Created by 64456 on 2017/9/22.
 */

public class LoginSMSActivity extends Activity implements View.OnClickListener {
    private Spinner mSpinner;
    private TextView mTvGetsms;
    private EditText mEtSms;

    private EditText mEtPhone;
    private int recLen = 30;//倒计时时长
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            recLen--;
            mTvGetsms.setText("" + recLen + "秒后重试");
            handler.postDelayed(this, 1000);

            if (recLen <= 0) {
                //倒计时结束后设置可以点击
                mTvGetsms.setFocusable(true);
                mTvGetsms.setClickable(true);
                mTvGetsms.setTextColor(Color.WHITE);
                mTvGetsms.setText("获取验证码");
                handler.removeCallbacks(runnable);

            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activit_login_sms);
        initView();
    }

    private void initView() {
//        mSpinner = findViewById(R.id.sp_phone);
//        //设置默认值
//        mSpinner.setSelection(0, true);
        mTvGetsms = findViewById(R.id.tv_getsms);
        mTvGetsms.setOnClickListener(this);
        mEtPhone = findViewById(R.id.et_phone);
        mEtSms = findViewById(R.id.et_sms);
        findViewById(R.id.btn_commit).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);


    }

    //返回
    public void back(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_getsms:
                //判断电话号码是否为空
                if (TextUtils.isEmpty(mEtPhone.getText().toString())) {
                    Toast.makeText(LoginSMSActivity.this, "请输入电话号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                //判断电话号码是否合法
                if (!PhoneFormatCheckUtils.isPhoneLegal(mEtPhone.getText().toString())) {
                    Toast.makeText(LoginSMSActivity.this, "电话号码有误，请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }

                //设置不能点击
                mTvGetsms.setFocusable(false);
                mTvGetsms.setClickable(false);
                mTvGetsms.setTextColor(Color.GRAY);
                recLen = 30;
                mTvGetsms.setText("" + recLen + "秒后重试");
                //设置倒计时
                handler.postDelayed(runnable, 1000);


                break;
            case R.id.btn_commit:
                //判断验证码是否为空
                if (TextUtils.isEmpty(mEtSms.getText().toString().trim())) {
                    Toast.makeText(LoginSMSActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }

                break;
            case R.id.iv_back://返回
                finish();
                break;
            default:
                break;


        }
    }
}
