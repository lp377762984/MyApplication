package com.cn.danceland.myapplication.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
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
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.RequestInfoBean;
import com.cn.danceland.myapplication.db.DBData;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.MD5Utils;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity implements OnClickListener {


    private EditText mEtPhone;
    private EditText mEtPsw;
    private boolean isPswdChecked = false;
    private ImageView iv_pswd_see;

    ProgressDialog dialog;
    public static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    public static final String PERMISSION_GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;
    public static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static final String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;
    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    public static final String PERMISSION_SMS = Manifest.permission.SEND_SMS;
    public static final String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        intView();
        dialog = new ProgressDialog(this);
        dialog.setMessage("登录中……");
        permissions();
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

    public void permissions(){
        if(ContextCompat.checkSelfPermission(LoginActivity.this,PERMISSION_RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{PERMISSION_RECORD_AUDIO},1);
        }
        if(ContextCompat.checkSelfPermission(LoginActivity.this,PERMISSION_CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{PERMISSION_CALL_PHONE},2);
        }
        if(ContextCompat.checkSelfPermission(LoginActivity.this,PERMISSION_CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{PERMISSION_CAMERA},3);
        }
        if(ContextCompat.checkSelfPermission(LoginActivity.this,PERMISSION_ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{PERMISSION_ACCESS_COARSE_LOCATION},4);
        }
        if(ContextCompat.checkSelfPermission(LoginActivity.this,PERMISSION_WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{PERMISSION_WRITE_EXTERNAL_STORAGE},5);
        }

    }
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1||requestCode==3||requestCode==2||requestCode==4||requestCode==5||requestCode==6){
            if(grantResults[0]!=0){
                AlertDialog.Builder inputDialog =
                        new AlertDialog.Builder(LoginActivity.this);
                inputDialog.setTitle("请在系统应用管理中开启相机定位等必要权限，否则程序无法正常使用");
                inputDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(requestCode==1){
                                    if(ContextCompat.checkSelfPermission(LoginActivity.this,PERMISSION_RECORD_AUDIO)
                                            != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{PERMISSION_RECORD_AUDIO},1);
                                    }
                                }
                                if(requestCode==2){
                                    if(ContextCompat.checkSelfPermission(LoginActivity.this,PERMISSION_CALL_PHONE)
                                            != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{PERMISSION_CALL_PHONE},2);
                                    }
                                }else if(requestCode==3){
                                    if(ContextCompat.checkSelfPermission(LoginActivity.this,PERMISSION_CAMERA)
                                            != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{PERMISSION_CAMERA},3);
                                    }
                                }else if(requestCode==4){
                                    if(ContextCompat.checkSelfPermission(LoginActivity.this,PERMISSION_ACCESS_COARSE_LOCATION)
                                            != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{PERMISSION_ACCESS_COARSE_LOCATION},4);
                                    }
                                }else if(requestCode==5){
                                    if(ContextCompat.checkSelfPermission(LoginActivity.this,PERMISSION_WRITE_EXTERNAL_STORAGE)
                                            != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{PERMISSION_WRITE_EXTERNAL_STORAGE},5);
                                    }
                                }
                                //System.exit(0);
                            }
                        }).show();

            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register://注册
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.tv_login_sms://短信登录
                startActivity(new Intent(LoginActivity.this, LoginSMSActivity.class));
                finish();
                break;
            case R.id.btn_login://登录
                //判断电话号码是否为空
                if (TextUtils.isEmpty(mEtPhone.getText().toString())) {
                    ToastUtils.showToastShort("请输入账号或手机号");
                    return;
                }
                //判断密码是否为空
                if (TextUtils.isEmpty(mEtPsw.getText().toString())) {
                    ToastUtils.showToastShort("请输入密码");
                    return;
                }
                //判断密码是否包含空格
                if (mEtPsw.getText().toString().contains(" ")) {
                    ToastUtils.showToastShort("密码不能包含空格，请重新输入");
                    return;
                }

                dialog.show();
                login();

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

    /**
     * 登录
     */
    private void login() {
        dialog.show();
        String url = Constants.LOGIN_URL;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                dialog.dismiss();
                LogUtil.i(s);

                Gson gson = new Gson();
                RequestInfoBean requestInfoBean = new RequestInfoBean();
                requestInfoBean = gson.fromJson(s, RequestInfoBean.class);
                if (requestInfoBean.getSuccess()) {
                    //成功
                    String mUserId = requestInfoBean.getData().getId();
                    SPUtils.setString(Constants.MY_USERID, mUserId);//保存id
                    SPUtils.setString(Constants.MY_TOKEN, "Bearer+" + requestInfoBean.getData().getToken());
                    SPUtils.setString(Constants.MY_PSWD, MD5Utils.encode(mEtPsw.getText().toString().trim()));//保存id




                    //查询信息
                    queryUserInfo(mUserId);

                    ToastUtils.showToastShort("登录成功");
                    SPUtils.setBoolean(Constants.ISLOGINED, true);//保存登录状态
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                    finish();
                } else {
                    //注册失败
                    ToastUtils.showToastShort(requestInfoBean.getErrorMsg());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dialog.dismiss();
                ToastUtils.showToastShort("请求失败，请查看网络连接");
//                LogUtil.i(volleyError.toString() + "Error: " + volleyError
//                        + ">>" + volleyError.networkResponse.statusCode
//                        + ">>" + volleyError.networkResponse.data
//                        + ">>" + volleyError.getCause()
//                        + ">>" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put("name", mEtPhone.getText().toString().trim());
                map.put("password", MD5Utils.encode(mEtPsw.getText().toString().trim()));
                // map.put("romType", "0");
                return map;
            }
        };

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("login");
        // 设置超时时间
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);
    }

    private void queryUserInfo(String id) {

        String params = id;

        String url = Constants.QUERY_USERINFO_URL + params;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                Gson gson = new Gson();
                RequestInfoBean requestInfoBean = gson.fromJson(s, RequestInfoBean.class);

                //      LogUtil.i(requestInfoBean.toString());
//                ArrayList<Data> mInfoBean = new ArrayList<>();
//                mInfoBean.add(requestInfoBean.getData());
//                DataInfoCache.saveListCache(mInfoBean, Constants.MY_INFO);
                //保存个人信息
                Data data = requestInfoBean.getData();
                DataInfoCache.saveOneCache(data,Constants.MY_INFO);

               LogUtil.i(DataInfoCache.loadOneCache(Constants.MY_INFO).toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                LogUtil.i(volleyError.toString());

            }

        }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, null));
                // LogUtil.i("Bearer+"+SPUtils.getString(Constants.MY_TOKEN,null));
                return map;
            }
        };
        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("queryUserInfo");
        // 设置超时时间
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);

    }



}
