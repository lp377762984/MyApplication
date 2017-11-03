package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.MD5Utils;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;

import static com.cn.danceland.myapplication.R.id.btn_commit;
import static com.cn.danceland.myapplication.R.id.iv_back;

/**
 * Created by shy on 2017/10/13 11:42
 * Email:644563767@qq.com
 */


public class ConfirmPasswordActivity extends Activity implements View.OnClickListener {

    private EditText et_password;
    private TextView tv_phone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_password);
        initView();
    }

    private void initView() {
        findViewById(btn_commit).setOnClickListener(this);
        findViewById(iv_back).setOnClickListener(this);
        tv_phone = findViewById(R.id.tv_phone);
        et_password = findViewById(R.id.et_password);
        tv_phone.setText(getIntent().getStringExtra("phone"));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case btn_commit:
                if (!TextUtils.equals(SPUtils.getString(Constants.MY_PSWD,null), MD5Utils.encode(et_password.getText().toString()))){

                    ToastUtils.showToastShort("面输入有误，请重新输入");
                    et_password.setText("");
                    return;
                }

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
