package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.RequestAddFriendInfoBean;
import com.cn.danceland.myapplication.bean.RequestInfoBean;
import com.cn.danceland.myapplication.bean.SearchMember;
import com.cn.danceland.myapplication.evntbus.EventConstants;
import com.cn.danceland.myapplication.evntbus.StringEvent;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shy on 2017/10/30 09:32
 * <p>
 * Email:644563767@qq.com
 */


public class AddFriendsActivity extends Activity implements View.OnClickListener {
    EditText mEtPhone;
    private ImageButton iv_del;
    private LinearLayout ll_result;
    private LinearLayout ll_search;
    private TextView tv_search;
    private TextView tv_nickname;
    private ImageView iv_avatar;
    private TextView tv_guanzhu;
    private TextView tv_result_null,tv_title;
    private RequestAddFriendInfoBean userInfo;
    private String from;
    int memberId;
    int personId;
    String member_no;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        from = getIntent().getStringExtra("from");
        iv_del = findViewById(R.id.iv_del);
        ll_search = findViewById(R.id.ll_search);
        ll_result = findViewById(R.id.ll_result);
        tv_search = findViewById(R.id.tv_search);
        mEtPhone = findViewById(R.id.et_phone);
        tv_nickname = findViewById(R.id.tv_nickname);
        iv_avatar = findViewById(R.id.iv_avatar);
        iv_avatar.setOnClickListener(this);
        ll_result.setOnClickListener(this);
//        iv_avatar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });


        tv_guanzhu = findViewById(R.id.tv_guanzhu);
        tv_title = findViewById(R.id.tv_title);
        if("体测".equals(from)){
            tv_guanzhu.setVisibility(View.GONE);
            tv_title.setText("搜索会员");
        }
        findViewById(R.id.tv_guanzhu).setOnClickListener(this);
        tv_result_null = findViewById(R.id.tv_result_null);
        setListener();

        mEtPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    if("体测".equals(from)){
                        searchMember(mEtPhone.getText().toString().trim());
                    }else{
                        findUser(mEtPhone.getText().toString().trim());
                    }


                }
                return false;
            }
        });

    }

    private void setListener() {
        ll_search.setOnClickListener(this);
        iv_del.setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        mEtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                ll_result.setVisibility(View.GONE);
                tv_result_null.setVisibility(View.GONE);
            }

            //监听edit
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(mEtPhone.getText().toString().trim())) {
                    iv_del.setVisibility(View.VISIBLE);
                    ll_search.setVisibility(View.VISIBLE);

                    tv_search.setText("搜索：“" + mEtPhone.getText().toString().trim() + "”");

                } else {
                    iv_del.setVisibility(View.GONE);
                    ll_search.setVisibility(View.GONE);
                    tv_search.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_del:
                mEtPhone.setText("");
                break;
            case R.id.iv_back://返回
                finish();
                break;
            case R.id.tv_guanzhu://加关注
                if (!userInfo.getData().isFollower()) {//如果未加关注
                    addGuanzhu(userInfo.getData().getUserId(),true);
                }

                break;
            case R.id.ll_search://搜索

                if("体测".equals(from)){
                    searchMember(mEtPhone.getText().toString().trim());
                }else{
                    findUser(mEtPhone.getText().toString().trim());
                }
                // ToastUtils.showToastShort(mEtPhone.getText().toString());

                break;
            case R.id.ll_result:
                if("体测".equals(from)){
                    Intent intent = new Intent(AddFriendsActivity.this,ReadyTestActivity.class);
                    intent.putExtra("id",personId+"");
                    intent.putExtra("memberId",memberId+"");
                    intent.putExtra("member_no",member_no);
                    startActivity(intent);
                    //finish();
                }else{
                    startActivity(new Intent(AddFriendsActivity.this,UserHomeActivity.class).putExtra("id", userInfo.getData().getUserId()));
                }
                break;
            default:
                break;
        }
    }

    /**
     * 加关注
     *
     * @param id
     * @param b
     */
    private void addGuanzhu(final String id, final boolean b) {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.FIND_ADD_USER_USRL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                Gson gson = new Gson();
                RequestInfoBean requestInfoBean = new RequestInfoBean();
                requestInfoBean = gson.fromJson(s, RequestInfoBean.class);
                if (requestInfoBean.getSuccess()) {
//                    data.get(pos).setFollower(true);
//                    notifyDataSetChanged();

                    ToastUtils.showToastShort("关注成功");
                    EventBus.getDefault().post(new StringEvent(id, EventConstants.ADD_GUANZHU));

                } else {
                    ToastUtils.showToastShort("关注失败");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                LogUtil.i(volleyError.toString());

            }

        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("userId", id);
                map.put("isFollower", String.valueOf(b));
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, null));
                // LogUtil.i("Bearer+"+SPUtils.getString(Constants.MY_TOKEN,null));
                LogUtil.i(SPUtils.getString(Constants.MY_TOKEN, null));
                return map;
            }
        };
        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("addGuanzhu");
        // 设置超时时间
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);

    }


    private void searchMember(final String phone){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.FINDMEMBER, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                SearchMember searchMember = gson.fromJson(s, SearchMember.class);
                SearchMember.Data data = searchMember.getData();
                if(data!=null){
                    memberId = data.getId();
                    personId = data.getPerson_id();
                    member_no = data.getMember_no();
                }
                if (searchMember.getSuccess() && searchMember.getData() != null) {
                    ll_result.setVisibility(View.VISIBLE);
                    tv_nickname.setText(searchMember.getData().getNick_name());
                    RequestOptions options = new RequestOptions().placeholder(R.drawable.img_my_avatar);
                    Glide.with(AddFriendsActivity.this).load(searchMember.getData().getSelf_avatar_path()).apply(options).into(iv_avatar);
                } else {
                    ToastUtils.showToastShort(searchMember.getErrorMsg());
                    tv_result_null.setVisibility(View.VISIBLE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("phone",phone);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));
                return map;
            }
        };

        MyApplication.getHttpQueues().add(stringRequest);

    }


    /*** 查找用户加关注
     *
     * @param phone
     */

    private void findUser(final String phone) {


        StringRequest request = new StringRequest(Request.Method.POST, Constants.FIND_ADD_USER_USRL, new Response.Listener<String>() {


            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                Gson gson = new Gson();

                userInfo = new RequestAddFriendInfoBean();
                userInfo = gson.fromJson(s, RequestAddFriendInfoBean.class);

                if (userInfo.getSuccess() && userInfo.getData() != null) {
                    tv_guanzhu.setVisibility(View.VISIBLE);
                    ll_result.setVisibility(View.VISIBLE);
                    tv_nickname.setText(userInfo.getData().getNickName());
                    RequestOptions options = new RequestOptions().placeholder(R.drawable.img_my_avatar);
                    Glide.with(AddFriendsActivity.this).load(userInfo.getData().getSelfUrl()).apply(options).into(iv_avatar);
                    if (userInfo.getData().isFollower()) {
                        tv_guanzhu.setText("已关注");
                    }else {
                        tv_guanzhu.setText("+关注");
                    }
                    if (TextUtils.equals(userInfo.getData().getUserId(),SPUtils.getString(Constants.MY_USERID,null))){

                        tv_guanzhu.setText("");

                    }

                } else {
                    ToastUtils.showToastShort(userInfo.getErrorMsg());
                    tv_result_null.setVisibility(View.VISIBLE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                LogUtil.i(volleyError.toString());

            }

        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("phoneOrMemberNo", phone);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, null));
                // LogUtil.i("Bearer+"+SPUtils.getString(Constants.MY_TOKEN,null));
                LogUtil.i(SPUtils.getString(Constants.MY_TOKEN, null));
                return map;
            }
        };
        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("findUser");
        // 设置超时时间
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);
    }


}
