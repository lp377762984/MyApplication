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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.MD5Utils;
import com.cn.danceland.myapplication.utils.PhoneFormatCheckUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 64456 on 2017/9/22.
 */

public class RegisterActivity extends Activity implements View.OnClickListener {
    private Spinner mSpinner;
    private TextView mTvGetsms;
    private EditText mEtSms;
    private EditText mEtPsd;
    private EditText mEtConfirmPsd;
    private String smsCode = "";
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
                mTvGetsms.setTextColor(Color.BLACK);
                mTvGetsms.setText("获取验证码");
                handler.removeCallbacks(runnable);

            }
        }
    };
    private EditText mEtPhone;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activit_register);
        initView();


    }

    private void initView() {
//        mSpinner = findViewById(R.id.sp_phone);
//        mSpinner.setSelection(0, true);
        mTvGetsms = findViewById(R.id.tv_getsms);
        mTvGetsms.setOnClickListener(this);
        mEtPhone = findViewById(R.id.et_phone);
        mEtSms = findViewById(R.id.et_sms);
        findViewById(R.id.btn_commit).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        mEtPsd = findViewById(R.id.et_password);
        mEtConfirmPsd = findViewById(R.id.et_confirm_password);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_getsms:
                //判断电话号码是否为空
                if (TextUtils.isEmpty(mEtPhone.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "请输入电话号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                //判断电话号码是否合法
                if (!PhoneFormatCheckUtils.isPhoneLegal(mEtPhone.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "电话号码有误，请重新输入", Toast.LENGTH_SHORT).show();
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

                getSMS();//获取短信验证码
                break;
            case R.id.btn_commit:
                //判断验证码是否为空
                if (TextUtils.isEmpty(mEtSms.getText().toString().trim())) {
                    Toast.makeText(RegisterActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtils.equals(smsCode, mEtSms.getText().toString().trim())) {
                    Toast.makeText(RegisterActivity.this, "验证码有误，请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }
                //判断密码是否为空
                if (TextUtils.isEmpty(mEtPsd.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                //判断密码是否包含空格
                if (mEtPsd.getText().toString().contains(" ")) {
                    Toast.makeText(RegisterActivity.this, "密码不能包含空格，请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }
                //判断密码位数是否符合6-20
                if (mEtPsd.getText().toString().length() < 6) {
                    Toast.makeText(RegisterActivity.this, "设置密码要大于等于6位，请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mEtPsd.getText().toString().length() > 20) {
                    Toast.makeText(RegisterActivity.this, "设置密码要小于等于20位，请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }
                //判断密码输入是否一致
                if (!TextUtils.equals(mEtConfirmPsd.getText().toString(), mEtPsd.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "密码输入不一致，请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }

                userRegister();//注册账户

                break;
            case R.id.iv_back://返回
                finish();
                break;
            default:
                break;
        }

    }

    /**
     * 获取短信验证码
     */
    private void getSMS() {

        String params = mEtPhone.getText().toString().trim();

        String url = Constants.GET_SMS_URL + params;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                try {
                    JSONObject jo = new JSONObject(s);
                    smsCode = jo.getString("verCode");

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                LogUtil.i(volleyError.toString());

            }
        });
        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("get_sms");
        // 设置超时时间
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);

    }

    /**
     * 注册用户
     */
    private void userRegister() {

        String url = Constants.REGISTER_URL;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.i(volleyError.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put("phone", mEtPhone.getText().toString().trim());
                map.put("password", MD5Utils.encode(mEtPsd.getText().toString().trim()));
                map.put("romType", "ANDROID");
                return map;
            }
        };

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("userRegister");
        // 设置超时时间
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);
    }

}
