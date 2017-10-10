package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.utils.LogUtil;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity implements OnClickListener {


    private EditText mEtPhone;
    private EditText mEtPsw;
    private boolean isPswdChecked = false;
    private ImageView iv_pswd_see;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        intView();


    }

    private void intView() {

        findViewById(R.id.tv_register).setOnClickListener(this);
        findViewById(R.id.tv_login_sms).setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.tv_forgetpsw).setOnClickListener(this);
        findViewById(R.id.tv_login_others).setOnClickListener(this);
        iv_pswd_see = findViewById(R.id.iv_pswd_see);
        iv_pswd_see.setOnClickListener(this);
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
            case R.id.btn_login://登录
                Toast.makeText(this, "登录成功！", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, HomeActivity.class));
                break;
            case R.id.tv_forgetpsw://忘记密码
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
                break;
            case R.id.tv_login_others://其他方式登陆
                Toast.makeText(this, "其他方式登陆", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_pswd_see://设置密码可见
                if (isPswdChecked) {
                    //密码不可见
                    mEtPsw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    isPswdChecked = false;
                    iv_pswd_see.setImageResource(R.drawable.img_unlook);

                } else {
                    //密码可见
                    mEtPsw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    isPswdChecked = true;
                    iv_pswd_see.setImageResource(R.drawable.img_look);
                }

                break;
            default:
                break;
        }
    }

    private void login() {

        StringRequest request = new StringRequest(Request.Method.POST, "", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put("params1", "value1");
                map.put("params2", "value2");
                return map;
            }
        };

    }

    private void QUERY_user_info() {

        String params = "";

        String url ="" + params;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                LogUtil.i(volleyError.toString());

            }
        });
        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("query_userinfo");
        // 设置超时时间
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);

    }
}
