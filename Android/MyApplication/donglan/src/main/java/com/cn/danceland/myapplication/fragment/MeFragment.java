package com.cn.danceland.myapplication.fragment;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.activity.MyProActivity;
import com.cn.danceland.myapplication.activity.SettingActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends BaseFragment  {


    @Override
    public View initViews() {
        View v = View.inflate(mActivity, R.layout.fragment_me, null);
        v.findViewById(R.id.ll_setting).setOnClickListener(this);
        v.findViewById(R.id.iv_edit).setOnClickListener(this);



        return v;
    }
    @Override
    public void initDta() {

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
