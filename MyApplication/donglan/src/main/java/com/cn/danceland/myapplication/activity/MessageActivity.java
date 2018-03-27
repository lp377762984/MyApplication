package com.cn.danceland.myapplication.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.evntbus.StringEvent;
import com.cn.danceland.myapplication.fragment.CommentFragment;
import com.cn.danceland.myapplication.fragment.MyConversationListFragment;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseUI;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

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
    private MyConversationListFragment myConversationListFragment;
    private int currentTabIndex=4;

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
                pinglunNum = SPUtils.getInt("pinglunNum", 0);
                dianzanNum = SPUtils.getInt("dianzanNum", 0);
                fansNum = SPUtils.getInt("fansNum", 0);
                EventBus.getDefault().post(new StringEvent(pinglunNum + dianzanNum + fansNum + "", 101));
                finish();
            }
        });
        //lv_message.setAdapter(new MessageAdapter());
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
        if (pinglunNum > 0) {
            tablayout.addTab(tablayout.newTab().setText("评论" + "(" + pinglunNum + ")"));
        } else {
            tablayout.addTab(tablayout.newTab().setText("评论"));
        }
        if (dianzanNum > 0) {
            tablayout.addTab(tablayout.newTab().setText("点赞" + "(" + dianzanNum + ")"));
        } else {
            tablayout.addTab(tablayout.newTab().setText("点赞"));
        }
        if (fansNum > 0) {
            tablayout.addTab(tablayout.newTab().setText("粉丝" + "(" + fansNum + ")"));
        } else {
            tablayout.addTab(tablayout.newTab().setText("粉丝"));
        }
        tablayout.addTab(tablayout.newTab().setText("消息"));
        tablayout.addTab(tablayout.newTab().setText("私信"));
//        if(pinglunNum>0){
//            new QBadgeView(MessageActivity.this).bindTarget(tab2).setBadgeNumber(pinglunNum).setBadgeGravity(Gravity.RIGHT);
//        }else if(dianzanNum>0){
//            new QBadgeView(MessageActivity.this).bindTarget(tab3).setBadgeNumber(dianzanNum).setBadgeGravity(Gravity.RIGHT);
//        }else if(fansNum>0){
//            new QBadgeView(MessageActivity.this).bindTarget(tab4).setBadgeNumber(fansNum).setBadgeGravity(Gravity.RIGHT);
//        }

        showFragment("5");
        SPUtils.setInt("pinglunNum", 0);
        tablayout.getTabAt(4).select();
        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTabIndex = tab.getPosition();
                if (currentTabIndex == 0) {
                    SPUtils.setInt("pinglunNum", 0);
                    showFragment("3");//评论
                } else if (currentTabIndex == 1) {
                    SPUtils.setInt("dianzanNum", 0);
                    showFragment("1");//点赞
                } else if (currentTabIndex == 2) {
                    SPUtils.setInt("fansNum", 0);
                    showFragment("2");//关注
                } else if (currentTabIndex == 3) {
                    showFragment("4");
                    //ToastUtils.showToastShort("没有系统消息");
                } else if (currentTabIndex == 4) {//私信对话列表
                    showFragment("5");
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

    public void showFragment(String str) {
        fragmentManager = getSupportFragmentManager();
        commentFragment = new CommentFragment();
        myConversationListFragment = new MyConversationListFragment();


        Bundle bundle = new Bundle();
        bundle.putString("type", str);
        commentFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (TextUtils.equals(str, "5")) {
            LogUtil.i("显示对话列表");
            fragmentTransaction
                    .replace(R.id.message_fragment, myConversationListFragment)
//                    .add(R.id.message_fragment, myConversationListFragment)
//                    .show(myConversationListFragment)
                    .commit();
        } else {
            fragmentTransaction.replace(R.id.message_fragment, commentFragment);
            fragmentTransaction.commit();
//            fragmentTransaction
//                    .add(R.id.message_fragment, commentFragment)
//                    .show(commentFragment)
//                    .commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBroadcastReceiver();
    }
    //private EaseUI easeUI;
    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // notify new message
            for (EMMessage message : messages) {
           //     DemoHelper.getInstance().getNotifier().onNewMsg(message);
                EaseUI.getInstance().getNotifier().onNewMesg(messages);
            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            refreshUIWithMessage();
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
        }

        @Override
        public void onMessageRecalled(List<EMMessage> messages) {
            refreshUIWithMessage();
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {}
    };

    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                // refresh unread count
                //updateUnreadLabel();
                if (currentTabIndex == 4) {
                    // refresh conversation list
                    if (myConversationListFragment != null) {
                        myConversationListFragment.refresh();
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);

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
                    if (myConversationListFragment != null) {
                        myConversationListFragment.refresh();
                    }
                }
//                else if (currentTabIndex == 1) {
//                    if(contactListFragment != null) {
//                        contactListFragment.refresh();
//                    }
//                }
//                String action = intent.getAction();
//                if(action.equals(Constants.ACTION_GROUP_CHANAGED)){
//                    if (EaseCommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
//                        GroupsActivity.instance.onResume();
//                    }
//                }
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    private void unregisterBroadcastReceiver(){
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

}
