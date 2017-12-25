package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.evntbus.StringEvent;
import com.cn.danceland.myapplication.fragment.CommentFragment;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import q.rorbin.badgeview.QBadgeView;

/**
 * Created by feng on 2017/12/11.
 */

public class MessageActivity extends FragmentActivity {
    int pinglunNum;
    int dianzanNum;
    int fansNum;
    RelativeLayout zan_message;
    ListView lv_message;
    ImageView im_back;
    TabLayout tablayout;
    FragmentManager fragmentManager;
    CommentFragment commentFragment;
    TabItem tab1;
    TabItem tab2;
    TabItem tab3;
    TabItem tab4;

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
                pinglunNum = SPUtils.getInt("pinglunNum",0);
                dianzanNum = SPUtils.getInt("dianzanNum",0);
                fansNum = SPUtils.getInt("fansNum",0);
                EventBus.getDefault().post(new StringEvent(pinglunNum+dianzanNum+fansNum+"",101));
                finish();
            }
        });
        //lv_message.setAdapter(new MessageAdapter());
    }

    private void initViews() {
        pinglunNum = SPUtils.getInt("pinglunNum",0);
        dianzanNum = SPUtils.getInt("dianzanNum",0);
        fansNum = SPUtils.getInt("fansNum",0);

        tablayout = findViewById(R.id.tablayout);
//        tab1 = findViewById(R.id.tab1);
//        tab2 = findViewById(R.id.tab2);
//        tab3 = findViewById(R.id.tab3);
//        tab4 = findViewById(R.id.tab4);
        if(pinglunNum>0){
            tablayout.addTab(tablayout.newTab().setText("评论"+"(" + pinglunNum + ")"));
        }else {
            tablayout.addTab(tablayout.newTab().setText("评论"));
        }
        if(dianzanNum>0){
            tablayout.addTab(tablayout.newTab().setText("点赞"+"("+dianzanNum+")"));
        }else{
            tablayout.addTab(tablayout.newTab().setText("点赞"));
        }
        if(fansNum>0){
            tablayout.addTab(tablayout.newTab().setText("粉丝"+"("+fansNum+")"));
        }else {
            tablayout.addTab(tablayout.newTab().setText("粉丝"));
        }
        tablayout.addTab(tablayout.newTab().setText("消息"));
//        if(pinglunNum>0){
//            new QBadgeView(MessageActivity.this).bindTarget(tab2).setBadgeNumber(pinglunNum).setBadgeGravity(Gravity.RIGHT);
//        }else if(dianzanNum>0){
//            new QBadgeView(MessageActivity.this).bindTarget(tab3).setBadgeNumber(dianzanNum).setBadgeGravity(Gravity.RIGHT);
//        }else if(fansNum>0){
//            new QBadgeView(MessageActivity.this).bindTarget(tab4).setBadgeNumber(fansNum).setBadgeGravity(Gravity.RIGHT);
//        }

        showFragment("3");
        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int i = tab.getPosition();
                if(i==0){
                    SPUtils.setInt("pinglunNum",0);
                    showFragment("3");//评论
                }else if(i==1){
                    SPUtils.setInt("dianzanNum",0);
                    showFragment("1");//点赞
                }else if(i==2){
                    SPUtils.setInt("fansNum",0);
                    showFragment("2");//关注
                }else if(i==3){
                    showFragment("4");
                    ToastUtils.showToastShort("没有系统消息");
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
