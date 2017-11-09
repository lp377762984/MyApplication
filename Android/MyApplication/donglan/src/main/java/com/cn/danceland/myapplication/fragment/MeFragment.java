package com.cn.danceland.myapplication.fragment;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.activity.MyProActivity;
import com.cn.danceland.myapplication.activity.SettingActivity;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends BaseFragment {


    private ImageView iv_avatar;
    private TextView tv_nick_name;
    private Data mInfo;

    @Override
    public View initViews() {
        View v = View.inflate(mActivity, R.layout.fragment_me, null);
        v.findViewById(R.id.ll_setting).setOnClickListener(this);
        v.findViewById(R.id.iv_edit).setOnClickListener(this);
        tv_nick_name = v.findViewById(R.id.tv_nick_name);
        iv_avatar = v.findViewById(R.id.iv_avatar);

        return v;
    }

    @Override
    public void initDta() {
        //获取本地用户信息缓存
        mInfo = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
        Glide.with(mActivity).load(mInfo.getAvatarPath()).into(iv_avatar);
        tv_nick_name.setText(mInfo.getNickName());



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_setting://设置页面
                startActivity(new Intent(mActivity, SettingActivity.class));
                break;
            case R.id.iv_edit://编辑资料页面
                startActivity(new Intent(mActivity, MyProActivity.class));

                break;

            default:
                break;
        }
    }
}
