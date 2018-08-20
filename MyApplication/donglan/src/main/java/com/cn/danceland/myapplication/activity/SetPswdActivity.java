package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.RequestSimpleBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.MD5Utils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shy on 2018/7/26 17:36
 * Email:644563767@qq.com
 */


public class SetPswdActivity extends Activity implements View.OnClickListener {
    private String id;
    private EditText mEtPsw;
    private EditText mEtConfirmPsd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setpswd);
        initView();
        initData();

    }

    private void initData() {
        id = getIntent().getStringExtra("id");
        LogUtil.i(id);

    }

    private void initView() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.btn_commit).setOnClickListener(this);
        mEtPsw = findViewById(R.id.et_password);
        mEtConfirmPsd = findViewById(R.id.et_confirm_password);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit:

                //判断密码是否为空
                if (TextUtils.isEmpty(mEtPsw.getText().toString())) {
                    Toast.makeText(SetPswdActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                //判断密码是否包含空格
                if (mEtPsw.getText().toString().contains(" ")) {
                    Toast.makeText(SetPswdActivity.this, "密码不能包含空格，请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }
                //判断密码位数是否符合6-20
                if (mEtPsw.getText().toString().length() < 6) {
                    Toast.makeText(SetPswdActivity.this, "设置密码要大于等于6位，请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mEtPsw.getText().toString().length() > 20) {
                    Toast.makeText(SetPswdActivity.this, "设置密码要小于等于20位，请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }
                //判断密码输入是否一致
                if (!TextUtils.equals(mEtConfirmPsd.getText().toString(), mEtPsw.getText().toString())) {
                    Toast.makeText(SetPswdActivity.this, "密码输入不一致，请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }


                resetPwd(id, mEtPsw.getText().toString());
                break;
            default:
                break;
        }
    }

    private void resetPwd(final String id, final String pswd) {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.RESET_PASSWORD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                RequestSimpleBean simpleBean = new Gson().fromJson(s, RequestSimpleBean.class);
                if (simpleBean.getSuccess()) {
                    ToastUtils.showToastShort("密码设置成功");
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.e(volleyError.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put("person_id", id);
                map.put("pwd", MD5Utils.encode(pswd));

                LogUtil.i(map.toString());
                return map;
            }
        };
        MyApplication.getHttpQueues().add(request);
    }


}
