package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.fragment.CommentFragment;
import com.cn.danceland.myapplication.utils.ToastUtils;

/**
 * Created by feng on 2017/12/11.
 */

public class MessageActivity extends FragmentActivity {

    RelativeLayout zan_message;
    ListView lv_message;
    ImageView im_back;
    TabLayout tablayout;
    FragmentManager fragmentManager;
    CommentFragment commentFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message);
        initViews();
        setOnclick();
    }

    private void setOnclick() {
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //lv_message.setAdapter(new MessageAdapter());
    }

    private void initViews() {
        tablayout = findViewById(R.id.tablayout);
        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int i = tab.getPosition();
                if(i==0){
                    ToastUtils.showToastShort("没有系统消息");
                }else if(i==1){
                    showFragment("3");//评论
                }else if(i==2){
                    showFragment("1");//点赞
                }else if(i==3){
                    showFragment("2");//关注
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
//        zan_message = findViewById(R.id.zan_message);
//        lv_message = findViewById(R.id.lv_message);
        im_back = findViewById(R.id.im_back);

    }

    public void showFragment(String str){
        fragmentManager = getSupportFragmentManager();
        commentFragment = new CommentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type",str);
        commentFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.message_fragment,commentFragment);
        fragmentTransaction.commit();
    }
}
