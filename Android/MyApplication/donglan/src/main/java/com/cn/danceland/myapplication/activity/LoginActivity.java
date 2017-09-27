package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.cn.danceland.myapplication.R;

public class LoginActivity extends Activity implements OnClickListener {


    private EditText mEtPhone;
    private EditText mEtPsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        intView();


    }

    private void intView() {

        findViewById(R.id.tv_register).setOnClickListener(this);
        findViewById(R.id.tv_login_sms).setOnClickListener(this);
        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.tv_forgetpsw).setOnClickListener(this);
        findViewById(R.id.tv_login_others).setOnClickListener(this);
        mEtPhone = findViewById(R.id.et_phone);
        mEtPsw = findViewById(R.id.et_password);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register://注册
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.tv_login_sms://短信登录
                startActivity(new Intent(LoginActivity.this, LoginSMSActivity.class));
                break;
            case R.id.login://登录
                Toast.makeText(this, "登录成功！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_forgetpsw://忘记密码
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
                break;
            case R.id.tv_login_others://其他方式登陆
                Toast.makeText(this, "其他方式登陆", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
