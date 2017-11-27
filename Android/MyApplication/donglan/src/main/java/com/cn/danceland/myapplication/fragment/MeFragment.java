package com.cn.danceland.myapplication.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.activity.MyProActivity;
import com.cn.danceland.myapplication.activity.SettingActivity;
import com.cn.danceland.myapplication.activity.UserHomeActivity;
import com.cn.danceland.myapplication.activity.UserListActivity;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.others.StringEvent;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends BaseFragment {


    private ImageView iv_avatar;
    private TextView tv_nick_name;
    private Data mInfo;
    private TextView tv_dyn;
    private TextView tv_guanzhu;
    private TextView tv_fans;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册event事件
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View initViews() {


        View v = View.inflate(mActivity, R.layout.fragment_me, null);
        v.findViewById(R.id.ll_setting).setOnClickListener(this);
        v.findViewById(R.id.ll_my_msg).setOnClickListener(this);
        v.findViewById(R.id.iv_edit).setOnClickListener(this);
        v.findViewById(R.id.ll_my_dyn).setOnClickListener(this);
        v.findViewById(R.id.ll_my_guanzhu).setOnClickListener(this);
        v.findViewById(R.id.ll_my_fans).setOnClickListener(this);
        tv_dyn = v.findViewById(R.id.tv_dyn);
        tv_guanzhu = v.findViewById(R.id.tv_gauzhu_num);
        tv_fans = v.findViewById(R.id.tv_fans);
        tv_nick_name = v.findViewById(R.id.tv_nick_name);
        iv_avatar = v.findViewById(R.id.iv_avatar);
        iv_avatar.setOnClickListener(this);
        return v;
    }

    @Override
    public void initDta() {
        //获取本地用户信息缓存
        mInfo = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
        //设置动态数

        //设置关注数
        //  LogUtil.i(mInfo.getFollowNumber()+"");
        tv_guanzhu.setText(mInfo.getFollowNumber() + "");
        //设置粉丝数
        tv_fans.setText(mInfo.getFansNum() + "");
        //设置头像
        RequestOptions options = new RequestOptions().placeholder(R.drawable.img_my_avatar);
        Glide.with(mActivity).load(mInfo.getSelfAvatarPath()).apply(options).into(iv_avatar);
        tv_nick_name.setText(mInfo.getNickName());


    }

    //even事件处理
    @Subscribe
    public void onEventMainThread(StringEvent event) {
        if (99 == event.getEventCode()) {
            String msg = event.getMsg();
            LogUtil.i("收到消息" + msg);
            RequestOptions options = new RequestOptions().placeholder(R.drawable.img_my_avatar);
            Glide.with(mActivity).load(msg).apply(options).into(iv_avatar);

            // tv_phone.setText(msg);
            //   Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

        }
        if (100 == event.getEventCode()) {
            tv_nick_name.setText(event.getMsg());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_setting://设置页面
                startActivity(new Intent(mActivity, SettingActivity.class));

                break;
            case R.id.iv_edit://编辑资料页面
                Intent intent = new Intent(mActivity, MyProActivity.class);
                startActivityForResult(intent, 119);
                //  startActivity(intent);
                break;
            case R.id.ll_my_msg://我的消息
                break;
            case R.id.ll_my_dyn://我的动态
                startActivity(new Intent(mActivity, UserHomeActivity.class).putExtra("id", SPUtils.getString(Constants.MY_USERID, null)).putExtra("isdyn",true));
                break;
            case R.id.ll_my_guanzhu://我的关注
                startActivity(new Intent(mActivity, UserListActivity.class).putExtra("id",SPUtils.getString(Constants.MY_USERID,null)).putExtra("type",1));
                break;
            case R.id.ll_my_fans://我的粉丝
                startActivity(new Intent(mActivity, UserListActivity.class).putExtra("id",SPUtils.getString(Constants.MY_USERID,null)).putExtra("type",2));
                break;
            case R.id.iv_avatar://头像
                startActivity(new Intent(mActivity, UserHomeActivity.class).putExtra("id", SPUtils.getString(Constants.MY_USERID, null)));
                break;
            default:
                break;
        }
    }
}
