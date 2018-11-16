package com.cn.danceland.myapplication.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.evntbus.StringEvent;
import com.cn.danceland.myapplication.fragment.NoticeFragment;
import com.cn.danceland.myapplication.fragment.SystemMessageFragment;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.view.DongLanTitleView;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by feng on 2017/12/11.
 */

public class MessageActivity extends BaseActivity {
    int pinglunNum;
    int dianzanNum;
    int fansNum;
    RelativeLayout zan_message;
    ListView lv_message;
    DongLanTitleView dongLanTitleView;
    ImageView im_back;
    TabLayout tablayout;
    FragmentManager fragmentManager;
    //CommentFragment commentFragment;//不用了
    NoticeFragment noticeFragment;//yxx 通知
    SystemMessageFragment systemMessageFragment;//yxx 系统消息
    TabItem tab1;
    TabItem tab2;
    TabItem tab3;
    TabItem tab4;
    private int currentTabIndex = 0;

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
                SPUtils.setInt("pinglunNum", 0);
                SPUtils.setInt("dianzanNum", 0);
                SPUtils.setInt("fansNum", 0);
                EventBus.getDefault().post(new StringEvent(0 + "", 101));
                finish();
            }
        });
        //lv_message.setAdapter(new MessageAdapter());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            SPUtils.setInt("pinglunNum", 0);
            SPUtils.setInt("dianzanNum", 0);
            SPUtils.setInt("fansNum", 0);
            EventBus.getDefault().post(new StringEvent(0 + "", 101));
            finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    private void initViews() {
        pinglunNum = SPUtils.getInt("pinglunNum", 0);
        dianzanNum = SPUtils.getInt("dianzanNum", 0);
        fansNum = SPUtils.getInt("fansNum", 0);
        registerBroadcastReceiver();
        tablayout = findViewById(R.id.tablayout);
//        tab1 = findViewById(R.id.tab1);
//        tab2 = findViewById(R.id.tab2);
//        tab3 = findViewById(R.id.tab3);
//        tab4 = findViewById(R.id.tab4);
        if (pinglunNum + dianzanNum + fansNum > 0) {
            tablayout.addTab(tablayout.newTab().setText("通知" + "(" + (pinglunNum + dianzanNum + fansNum) + ")"));
        } else {
            tablayout.addTab(tablayout.newTab().setText("通知"));
        }
//        if (dianzanNum > 0) {
//            tablayout.addTab(tablayout.newTab().setText("点赞" + "(" + dianzanNum + ")"));
//        } else {
//            tablayout.addTab(tablayout.newTab().setText("点赞"));
//        }
//        if (fansNum > 0) {
//            tablayout.addTab(tablayout.newTab().setText("粉丝" + "(" + fansNum + ")"));
//        } else {
//            tablayout.addTab(tablayout.newTab().setText("粉丝"));
//        }
        tablayout.addTab(tablayout.newTab().setText("系统消息"));
        //   tablayout.addTab(tablayout.newTab().setText("私信"));

//        if(pinglunNum>0){
//            new QBadgeView(MessageActivity.this).bindTarget(tab2).setBadgeNumber(pinglunNum).setBadgeGravity(Gravity.RIGHT);
//        }else if(dianzanNum>0){
//            new QBadgeView(MessageActivity.this).bindTarget(tab3).setBadgeNumber(dianzanNum).setBadgeGravity(Gravity.RIGHT);
//        }else if(fansNum>0){
//            new QBadgeView(MessageActivity.this).bindTarget(tab4).setBadgeNumber(fansNum).setBadgeGravity(Gravity.RIGHT);
//        }

        showFragment("1");
        //SPUtils.setInt("pinglunNum", 0);
        tablayout.getTabAt(0).select();
        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTabIndex = tab.getPosition();
                if (currentTabIndex == 0) {
                    SPUtils.setInt("pinglunNum", 0);
                    SPUtils.setInt("dianzanNum", 0);
                    SPUtils.setInt("fansNum", 0);
                    showFragment("1");//评论
                }
//                else if (currentTabIndex == 1) {
//                    SPUtils.setInt("dianzanNum", 0);
//                    showFragment("1");//点赞
//                } else if (currentTabIndex == 2) {
//                    SPUtils.setInt("fansNum", 0);
//                    showFragment("2");//关注
//                }
                else if (currentTabIndex == 1) {
                    showFragment("4");
                    //ToastUtils.showToastShort("没有系统消息");
                }
//                else if (currentTabIndex == 4) {//私信对话列表
//                    showFragment("5");
//                }
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
        dongLanTitleView = findViewById(R.id.title);
        im_back = dongLanTitleView.findViewById(R.id.iv_back);
    }

    public void showFragment(String str) {
        fragmentManager = getSupportFragmentManager();
//        commentFragment = new CommentFragment();
        noticeFragment = new NoticeFragment();
        systemMessageFragment = new SystemMessageFragment();


        Bundle bundle = new Bundle();
        bundle.putString("type", str);
//        commentFragment.setArguments(bundle);
        noticeFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        LogUtil.i("str" + str);
        if (TextUtils.equals(str, "1")) {
            fragmentTransaction.replace(R.id.message_fragment, noticeFragment).commit();
        } else {
            fragmentTransaction.replace(R.id.message_fragment, systemMessageFragment).commit();
        }
//        if (TextUtils.equals(str, "5")) {
//            LogUtil.i("显示对话列表");
        //        fragmentTransaction
//                    .replace(R.id.message_fragment, myConversationListFragment)
////                    .add(R.id.message_fragment, myConversationListFragment)
////                    .show(myConversationListFragment)
//                    .commit();

//            fragmentTransaction
//                    .add(R.id.message_fragment, commentFragment)
//                    .show(commentFragment)
//                    .commit();
//        }
        //            fragmentTransaction.replace(R.id.message_fragment, commentFragment);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBroadcastReceiver();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager broadcastManager;

    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constants.ACTION_GROUP_CHANAGED);
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                LogUtil.i("收到广播");
                //  updateUnreadLabel();
                //   updateUnreadAddressLable();
                if (currentTabIndex == 4) {
                    // refresh conversation list
                }
            }
//                else if (currentTabIndex == 1) {
//                    if(contactListFragment != null) {
//                        contactListFragment.refresh();
//                    }
//                }
//                String action = intent.getAction();
//                if(action.equals(Constants.ACTION_GROUP_CHANAGED)){
//                    if (EaseCommonUtils.getTopActivity(ShouHuanMainActivity.this).equals(GroupsActivity.class.getName())) {
//                        GroupsActivity.instance.onResume();
//                    }
//                }
        };
    }

    private void unregisterBroadcastReceiver() {
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

}
