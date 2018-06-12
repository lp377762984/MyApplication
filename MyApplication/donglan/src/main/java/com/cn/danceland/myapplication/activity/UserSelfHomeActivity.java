package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.RequestInfoBean;
import com.cn.danceland.myapplication.bean.RequsetUserDynInfoBean;
import com.cn.danceland.myapplication.evntbus.EventConstants;
import com.cn.danceland.myapplication.evntbus.StringEvent;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;
import com.vondear.rxtools.view.likeview.RxShineButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;


/**
 * Created by shy on 2018/4/16 17:58
 * Email:644563767@qq.com
 */


public class UserSelfHomeActivity extends Activity implements View.OnClickListener {
    private RequsetUserDynInfoBean.Data userInfo;
    private TextView tv_dyn;
    private TextView tv_gauzhu_num, tv_message, tv_guanzhu;
    private TextView tv_fans;
    private float pingfen;
    //  private AnimButton iv_guanzhu;
    private TextView tv_hobby;
    private EmojiconTextView tv_nick_name;
    private ImageView iv_avatar;
    private String userId;
    private boolean isdyn;
    private ImageView iv_sex;
    RxShineButton rx_guangzhu;
    private ImageView iv_guanzhu;
    private TextView tv_sign;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册event事件
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_user_self_home);
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initData() {

        queryUserInfo(userId);
    }

    private void initView() {
        userId = getIntent().getStringExtra("id");
        isdyn = getIntent().getBooleanExtra("isdyn", false);
        findViewById(R.id.ll_my_dyn).setOnClickListener(this);
        findViewById(R.id.ll_my_guanzhu).setOnClickListener(this);
        findViewById(R.id.ll_my_fans).setOnClickListener(this);
        findViewById(R.id.ll_guanzhu).setOnClickListener(this);
        findViewById(R.id.ll_sixin).setOnClickListener(this);
        findViewById(R.id.ll_edit).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        tv_dyn = findViewById(R.id.tv_dyn);
        tv_gauzhu_num = findViewById(R.id.tv_gauzhu_num);
        rx_guangzhu = findViewById(R.id.rx_guangzhu);
        iv_guanzhu = findViewById(R.id.iv_guanzhu);
        iv_guanzhu.setOnClickListener(this);
        rx_guangzhu.setOnClickListener(this);

        tv_fans = findViewById(R.id.tv_fans);
        tv_nick_name = findViewById(R.id.tv_nick_name);
        iv_avatar = findViewById(R.id.iv_avatar);
        iv_sex = findViewById(R.id.iv_sex);
        iv_avatar.setOnClickListener(this);
        tv_guanzhu = findViewById(R.id.tv_guanzhu);
        //  iv_guanzhu = findViewById(R.id.iv_guanzhu);
        //   iv_guanzhu.setOnClickListener(this);
        tv_hobby = findViewById(R.id.tv_hobby);
        tv_sign = findViewById(R.id.tv_sign);
        if (TextUtils.equals(userId, SPUtils.getString(Constants.MY_USERID, ""))) {
            findViewById(R.id.ll_01).setVisibility(View.INVISIBLE);
            findViewById(R.id.ll_edit).setVisibility(View.VISIBLE);
        }

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {      //判断标志位
                case 1:

                    break;
                case 2:
                    setData();
                    break;
                case 3:
                    break;
            }

        }
    };


    //even事件处理
    @Subscribe
    public void onEventMainThread(StringEvent event) {

        if (TextUtils.equals(userId,SPUtils.getString(Constants.MY_USERID,""))){

            switch (event.getEventCode()) {
                case 99:
                    String msg = event.getMsg();
                    LogUtil.i("收到消息" + msg);
                    RequestOptions options = new RequestOptions().placeholder(R.drawable.img_my_avatar);
                    Glide.with(this).load(msg).apply(options).into(iv_avatar);

                    break;
                case 100:
                    tv_nick_name.setText(event.getMsg());

                    break;


                case EventConstants.ADD_DYN:  //设置动态数+1
                    LogUtil.i("动态加1");
                    //mInfo.setDynMsgNumber(mInfo.getDynMsgNumber() + 1);
                    //   DataInfoCache.saveOneCache(mInfo, Constants.MY_INFO);
                    //      SPUtils.setInt(Constants.MY_DYN, SPUtils.getInt(Constants.MY_DYN, 0) + 1);
                    tv_dyn.setText(SPUtils.getInt(Constants.MY_DYN, 0) + "");
                    break;
                case EventConstants.DEL_DYN:
                    //设置动态数-1
//                mInfo.setDynMsgNumber(mInfo.getDynMsgNumber() - 1);
//                tv_dyn.setText(mInfo.getDynMsgNumber() + "");
//                DataInfoCache.saveOneCache(mInfo, Constants.MY_INFO);
                    //      SPUtils.setInt(Constants.MY_DYN, SPUtils.getInt(Constants.MY_DYN, 0) - 1);
                    tv_dyn.setText(SPUtils.getInt(Constants.MY_DYN, 0) + "");

                    break;
                case EventConstants.ADD_GUANZHU:
                    LogUtil.i("设置关注数+1");
                    //设置关注数+1
//                mInfo.setFollowNumber(mInfo.getFollowNumber() + 1);
//                tv_guanzhu.setText(mInfo.getFollowNumber() + "");
//                DataInfoCache.saveOneCache(mInfo, Constants.MY_INFO);
                    //     SPUtils.setInt(Constants.MY_FOLLOWS, SPUtils.getInt(Constants.MY_FOLLOWS, 0) + 1);
                    tv_guanzhu.setText(SPUtils.getInt(Constants.MY_FOLLOWS, 0) + "");

                    break;
                case EventConstants.DEL_GUANZHU:
                    LogUtil.i("设置关注数-1");
                    //设置关注数-1
//                mInfo.setFollowNumber(mInfo.getFollowNumber() - 1);
//                tv_guanzhu.setText(mInfo.getFollowNumber() + "");
//                DataInfoCache.saveOneCache(mInfo, Constants.MY_INFO);
                    //    SPUtils.setInt(Constants.MY_FOLLOWS, SPUtils.getInt(Constants.MY_FOLLOWS, 0) - 1);
                    tv_guanzhu.setText(SPUtils.getInt(Constants.MY_FOLLOWS, 0) + "");
                    break;
//
//            case EventConstants.UPDATE_FANS:
//
//                //设置粉丝数
//                // tv_fans.setText(mInfo.getFansNum() + "");
//                DataInfoCache.saveOneCache(mInfo, Constants.MY_INFO);
//
//                //    SPUtils.setInt(Constants.MY_FOLLOWS,SPUtils.getInt(Constants.MY_FOLLOWS,0));
//                tv_fans.setText(SPUtils.getInt(Constants.MY_FANS, 0) + "");
//
//                break;
//
//            case EventConstants.UPDATE_USER_INFO:
//                Data myinfo = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
//
//                updateUserInfo(myinfo);
//
//                //更新聊天头像和昵称
//                PreferenceManager.getInstance().setCurrentUserNick(myinfo.getPerson().getNick_name());
//                PreferenceManager.getInstance().setCurrentUserAvatar(myinfo.getPerson().getSelf_avatar_path());
//
//
//                break;
//

                default:
                    break;
            }
        }


    }


    private void setData() {
        tv_gauzhu_num.setText(userInfo.getFollow_no() + "");
        tv_fans.setText(userInfo.getFanse_no() + "");
        tv_dyn.setText(userInfo.getDyn_no() + "");
        tv_nick_name.setText(userInfo.getPerson().getNick_name());
        RequestOptions options = new RequestOptions().placeholder(R.drawable.img_my_avatar);
        Glide.with(UserSelfHomeActivity.this)
                .load(userInfo.getPerson().getSelf_avatar_path())
                .apply(options)
                .into(iv_avatar);
        if (userInfo.getIs_follow()) {
            tv_guanzhu.setText("已关注");
            iv_guanzhu.setImageResource(R.drawable.img_xin1);
            rx_guangzhu.setChecked(true);
        } else {
            tv_guanzhu.setText("+关注");
            iv_guanzhu.setImageResource(R.drawable.img_xin);
            rx_guangzhu.setChecked(false);
        }


        if (!TextUtils.isEmpty(userInfo.getPerson().getHobby())) {
            tv_hobby.setText(userInfo.getPerson().getHobby());
        }
        if (!TextUtils.isEmpty(userInfo.getPerson().getSign())) {
            tv_sign.setText(userInfo.getPerson().getSign());
        }

        if (TextUtils.equals(userInfo.getPerson().getGender(), "1")) {
            iv_sex.setImageResource(R.drawable.img_sex1);
        } else if (TextUtils.equals(userInfo.getPerson().getGender(), "2")) {
            iv_sex.setImageResource(R.drawable.img_sex2);
        } else {
            iv_sex.setVisibility(View.INVISIBLE);
        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_my_dyn://我的动态
                startActivity(new Intent(UserSelfHomeActivity.this, UserHomeActivity.class).putExtra("id", userId).putExtra("isdyn", true));
                break;
            case R.id.ll_my_guanzhu://我的关注
                startActivity(new Intent(UserSelfHomeActivity.this, UserListActivity.class).putExtra("id", userId).putExtra("type", 1));
                break;
            case R.id.ll_my_fans://我的粉丝
                startActivity(new Intent(UserSelfHomeActivity.this, UserListActivity.class).putExtra("id", userId).putExtra("type", 2));
                break;
            case R.id.iv_avatar://头像
                startActivity(new Intent(UserSelfHomeActivity.this, AvatarActivity.class).putExtra("url", userInfo.getPerson().getSelf_avatar_path()));
                break;
//            case R.id.ll_guanzhu:
//                if (userInfo.getIs_follow()){
//                    addGuanzhu(userId,false);
//                }else {
//                    addGuanzhu(userId,true);
//                }
//
//                break;
            case R.id.iv_guanzhu:
                if (userInfo.getIs_follow()) {
                    showClearDialog();
                } else {
                    addGuanzhu(userId, true);
                }
                break;
            case R.id.rx_guangzhu:
                if (userInfo.getIs_follow()) {
                    showClearDialog();
                } else {
                    addGuanzhu(userId, true);
                }


                break;

            case R.id.ll_edit:
                startActivity(new Intent(UserSelfHomeActivity.this, MyProActivity.class));
                break;
            case R.id.ll_sixin:
//
//                String userName = userInfo.getPerson().getNick_name();
//                String userPic = userInfo.getPerson().getSelf_avatar_path();
//                String hxIdFrom;
//                if (Constants.HX_DEV_CONFIG) {
//                    hxIdFrom = "dev" + userInfo.getPerson().getMember_no();
//                } else {
//                    hxIdFrom = userInfo.getPerson().getMember_no();
//                }
//
//                LogUtil.i(userName + userPic + hxIdFrom);
//                EaseUser easeUser = new EaseUser(hxIdFrom);
//                easeUser.setAvatar(userPic);
//                easeUser.setNick(userName);
//
//                List<EaseUser> users = new ArrayList<EaseUser>();
//                users.add(easeUser);
//                if (easeUser != null) {
//                    DemoHelper.getInstance().updateContactList(users);
//                } else {
//                    LogUtil.i("USER IS NULL");
//
//                }
//
//                    startActivity(new Intent(UserSelfHomeActivity.this, MyChatActivity.class).putExtra("userId", hxIdFrom).putExtra("chatType", EMMessage.ChatType.Chat));


                break;
            default:
                break;
        }
    }

    /**
     * 取消关注
     */
    private void showClearDialog() {
        AlertDialog.Builder dialog =
                new AlertDialog.Builder(this);
        //  dialog.setTitle("提示");
        dialog.setMessage("是否取消关注");
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                addGuanzhu(userId, false);
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }


    /***
     * 查找个人资料
     * @param id 用户id
     */
    private void queryUserInfo(final String id) {

        String params = id;

        String url = Constants.QUERY_USER_DYN_INFO_URL + params;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String s) {

                LogUtil.i(s);
                Gson gson = new Gson();
                RequsetUserDynInfoBean requestInfoBean = gson.fromJson(s, RequsetUserDynInfoBean.class);


                userInfo = requestInfoBean.getData();


                if (TextUtils.equals(id, SPUtils.getString(Constants.MY_USERID, null))) {
                    //如果是本人更新本地缓存
                    Data data = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
                    data.setPerson(userInfo.getPerson());
                    DataInfoCache.saveOneCache(data, Constants.MY_INFO);
                    SPUtils.setInt(Constants.MY_DYN, requestInfoBean.getData().getDyn_no());
                    SPUtils.setInt(Constants.MY_FANS, requestInfoBean.getData().getFanse_no());
                    SPUtils.setInt(Constants.MY_FOLLOWS, requestInfoBean.getData().getFollow_no());


                    EventBus.getDefault().post(new StringEvent("", EventConstants.UPDATE_USER_INFO));
                }


                Message msg = Message.obtain();
                //   msg.obj = data;
                msg.what = 2; //标志消息的标志
                handler.sendMessage(msg);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                LogUtil.i(volleyError.toString());
                //dialog.dismiss();
                ToastUtils.showToastShort("请查看网络连接");

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

    class StrBean1 {
        public boolean is_follower;
        public String user_id;

    }


    /**
     * 加关注
     *
     * @param id
     * @param b
     */
    private void addGuanzhu(final String id, final boolean b) {

        StrBean1 strBean1 = new StrBean1();
        strBean1.is_follower = b;
        strBean1.user_id = id;


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.ADD_GUANZHU, new Gson().toJson(strBean1), new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtil.i(jsonObject.toString());
                Gson gson = new Gson();
                RequestInfoBean requestInfoBean = new RequestInfoBean();
                requestInfoBean = gson.fromJson(jsonObject.toString(), RequestInfoBean.class);
                if (b) {
                    if (requestInfoBean.getSuccess()) {
                        queryUserInfo(userId);
                        ToastUtils.showToastShort("关注成功");
                        EventBus.getDefault().post(new StringEvent(userId, EventConstants.ADD_GUANZHU));

                    } else {
                        ToastUtils.showToastShort("关注失败");
                    }
                } else {

                    if (requestInfoBean.getSuccess()) {
                        queryUserInfo(userId);
                        ToastUtils.showToastShort("取消关注成功");
                        EventBus.getDefault().post(new StringEvent(userId, EventConstants.DEL_GUANZHU));

                    } else {
                        ToastUtils.showToastShort("取消关注失败");
                    }


                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                LogUtil.i(volleyError.toString());
                ToastUtils.showToastShort("请查看网络连接");
            }

        }
        ) {


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


}
