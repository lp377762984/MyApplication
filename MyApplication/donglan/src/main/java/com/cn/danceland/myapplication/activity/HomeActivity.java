package com.cn.danceland.myapplication.activity;


import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.CheckUpdateBean;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.RequestInfoBean;
import com.cn.danceland.myapplication.evntbus.StringEvent;
import com.cn.danceland.myapplication.fragment.DiscoverFragment;
import com.cn.danceland.myapplication.fragment.HomeFragment;
import com.cn.danceland.myapplication.fragment.MeFragment;
import com.cn.danceland.myapplication.fragment.ShopFragment;
import com.cn.danceland.myapplication.fragment.ShopListFragment;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.HttpUtils;
import com.cn.danceland.myapplication.utils.LocationService;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.cn.danceland.myapplication.utils.runtimepermissions.PermissionsManager;
import com.cn.danceland.myapplication.utils.runtimepermissions.PermissionsResultAction;
import com.google.gson.Gson;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConnListener;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMGroupEventListener;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMGroupTipsElem;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMRefreshListener;
import com.tencent.imsdk.TIMSNSChangeInfo;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMUserStatusListener;
import com.tencent.imsdk.ext.group.TIMGroupAssistantListener;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;
import com.tencent.imsdk.ext.group.TIMUserConfigGroupExt;
import com.tencent.imsdk.ext.message.TIMConversationExt;
import com.tencent.imsdk.ext.message.TIMManagerExt;
import com.tencent.imsdk.ext.message.TIMUserConfigMsgExt;
import com.tencent.imsdk.ext.sns.TIMFriendshipProxyListener;
import com.tencent.imsdk.ext.sns.TIMUserConfigSnsExt;
import com.tencent.qcloud.presentation.event.FriendshipEvent;
import com.tencent.qcloud.presentation.event.GroupEvent;
import com.tencent.qcloud.presentation.event.MessageEvent;
import com.tencent.qcloud.presentation.event.RefreshEvent;
import com.tencent.qcloud.presentation.presenter.ConversationPresenter;
import com.tencent.qcloud.presentation.viewfeatures.ConversationView;
import com.xiaomi.mipush.sdk.MiPushClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jzvd.JZVideoPlayer;

import static android.view.animation.Animation.REVERSE;

public class HomeActivity extends FragmentActivity implements View.OnClickListener {


    private TextView[] mTabs;
    private ImageView[] mmTabsImgs;
    private Fragment[] fragments;
    private int index;
    private int currentTabIndex;
    private HomeFragment homeFragment;
    private ShopFragment shopFragment;
    private ShopListFragment shopListFragment;
    private DiscoverFragment discoverFragment;
    private MeFragment meFragment;
    public static HomeActivity instance = null;
    private static final String[] FRAGMENT_TAG = {"homeFragment", "shopFragment", "discoverFragment", "meFragment"};
    public LocationService mLocationClient;
    public BDAbstractLocationListener myListener = new MyLocationListener();
    double jingdu, weidu;
    Data myInfo;

    private ConversationPresenter presenter;
    private ImageView msgUnread;
    private Animation animation;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //不处理崩溃时页面保存信息
        // super.onSaveInstanceState(outState);
    }




    //even事件处理
    @Subscribe
    public void onEventMainThread(StringEvent event) {
        switch (event.getEventCode()) {
            case 20001:

                if (shopFragment != null) {
                    shopFragment.refresh();
                }
                setMsgUnread(getTotalUnreadNum() == 0);
                break;
            default:
                break;
        }
    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }


    private AnimationSet mAnimationSet;

    private void buildAnima() {
        //加载动画资源
        animation = AnimationUtils.loadAnimation(this, R.anim.tab_anim);


        ScaleAnimation mScaleAnimation = new ScaleAnimation(0.8f, 1.4f, 0.8f, 1.4f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mScaleAnimation.setDuration(300);
       mScaleAnimation.setInterpolator(new AccelerateInterpolator());
        mScaleAnimation.setFillAfter(true);
        mScaleAnimation.setRepeatCount(1);
        mScaleAnimation.setRepeatMode(REVERSE);

        ScaleAnimation mScaleAnimation1 = new ScaleAnimation(1.5f, 0.8f, 1.5f, 0.8f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mScaleAnimation1.setDuration(1000);
        mScaleAnimation1.setInterpolator(new AccelerateDecelerateInterpolator());
    //    mScaleAnimation.setFillAfter(true);
        ScaleAnimation mScaleAnimation2 = new ScaleAnimation(0.8f, 1.2f, 0.8f, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mScaleAnimation2.setDuration(1000);
    //    mScaleAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        mScaleAnimation2.setFillAfter(true);

//        AlphaAnimation mAlphaAnimation = new AlphaAnimation(1, .2f);
//        mAlphaAnimation.setDuration(300);
//        mAlphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
//        mAlphaAnimation.setFillAfter(false);

        mAnimationSet = new AnimationSet(true);


        mAnimationSet.addAnimation(mScaleAnimation);
//        mAnimationSet.addAnimation(mScaleAnimation1);
//        mAnimationSet.addAnimation(mScaleAnimation2);
        mAnimationSet.setRepeatCount(1);
        mAnimationSet.setRepeatMode(REVERSE);
     //   mAnimationSet.addAnimation(mAlphaAnimation);
        mAnimationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_home);
        //    requestPermissions();//请求权限
        instance = this;
        initView();
        checkUpdate();

        buildAnima();
        homeFragment = new HomeFragment();
        shopFragment = new ShopFragment();

        shopListFragment = new ShopListFragment();

        discoverFragment = new DiscoverFragment();
        meFragment = new MeFragment();
        msgUnread = (ImageView) findViewById(R.id.tabUnread);
        presenter = new ConversationPresenter(new ConversationView() {
            @Override
            public void initView(List<TIMConversation> conversationList) {

            }

            @Override
            public void updateMessage(TIMMessage message) {
//                LogUtil.i("shuaxin xiaoxi"+message.getElementCount());
//                getTotalUnreadNum();
//                LogUtil.i( "未读数"+  getTotalUnreadNum());

                EventBus.getDefault().post(new StringEvent("刷新消息", 20001));
            }

            @Override
            public void updateFriendshipMessage() {

            }

            @Override
            public void removeConversation(String identify) {

            }

            @Override
            public void updateGroupInfo(TIMGroupCacheInfo info) {

            }

            @Override
            public void refresh() {
                LogUtil.i("shuaxin");
            }
        });
        presenter.getConversation();

        initTimUser();
//        if (savedInstanceState != null) {
//            homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG[0]);
//            shopFragment = (ShopFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG[1]);
//            discoverFragment = (DiscoverFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG[2]);
//            meFragment = (MeFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG[3]);
//        }
        try {
            myInfo = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
        } catch (ClassCastException e) {
            LogUtil.i(e.toString());
            startActivity(new Intent(this, LoginActivity.class));
            if (SPUtils.getBoolean(Constants.ISLOGINED, false)) {
                SPUtils.setBoolean(Constants.ISLOGINED, false);
                ToastUtils.showToastShort("请重新登录您的账号");
            }

            //退出主页面
            finish();
        }

        if (myInfo != null) {//判断资料是否全
            if (myInfo.getHasPwd()!=null&&!myInfo.getHasPwd()){
                ToastUtils.showToastShort("请您设置密码");
                startActivity(new Intent(HomeActivity.this, SetPswdActivity.class).putExtra("id",myInfo.getPerson().getId()));

            }

            LogUtil.i(myInfo.toString());
            if (TextUtils.isEmpty(myInfo.getPerson().getNick_name())||TextUtils.isEmpty(myInfo.getPerson().getBirthday())||TextUtils.isEmpty(myInfo.getPerson().getHeight())||TextUtils.isEmpty(myInfo.getPerson().getWeight())) {
                startActivity(new Intent(this, RegisterInfoActivity.class));
                ToastUtils.showToastShort("请您填写个人信息");
                finish();
            }


            if (myInfo.getPerson().getDefault_branch() != null) {
                fragments = new Fragment[]{homeFragment, shopFragment, discoverFragment, meFragment};
            } else {
                fragments = new Fragment[]{homeFragment, shopListFragment, discoverFragment, meFragment};
            }
        } else {
            fragments = new Fragment[]{homeFragment, shopListFragment, discoverFragment, meFragment};
        }


//        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, homeFragment)
//                .add(R.id.fragment_container, discoverFragment).hide(discoverFragment).show(homeFragment)
//                .commit();

        if (getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG[0]) == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, meFragment, FRAGMENT_TAG[3])
                    .hide(meFragment)
                    .add(R.id.fragment_container, homeFragment, FRAGMENT_TAG[0])
                    .show(homeFragment)
                    .commit();
        }
        //   getFragmentManager().findFragmentByTag()
        LogUtil.i(MiPushClient.getRegId(this));
        if (SPUtils.getBoolean(Constants.UPDATE_MIPUSH_CONFIG, false)) {
            setMipushId();
        }
    }


    public void initTimUser() {
        //基本用户配置
        TIMUserConfig userConfig = new TIMUserConfig()
                //设置群组资料拉取字段
                //.setGroupSettings(initGroupSettings())
                //设置资料关系链拉取字段
                //.setFriendshipSettings(initFriendshipSettings())
                //设置用户状态变更事件监听器
                .setUserStatusListener(new TIMUserStatusListener() {
                    @Override
                    public void onForceOffline() {
                        //被其他终端踢下线
                        LogUtil.i("onForceOffline");
                        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                        builder.setMessage("您的账号已从从其他设备登录，请重新登录");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                                SPUtils.setBoolean(Constants.ISLOGINED,false);
                                finish();
                            }
                        });
                        builder.setCancelable(false);

                    }

                    @Override
                    public void onUserSigExpired() {
                        //用户签名过期了，需要刷新userSig重新登录SDK
                        LogUtil.i("onUserSigExpired");
                    }
                })
                //设置连接状态事件监听器
                .setConnectionListener(new TIMConnListener() {
                    @Override
                    public void onConnected() {
                        LogUtil.i("onConnected");
                    }

                    @Override
                    public void onDisconnected(int code, String desc) {
                        LogUtil.i("onDisconnected");
                    }

                    @Override
                    public void onWifiNeedAuth(String name) {
                        LogUtil.i("onWifiNeedAuth");
                    }
                })
                //设置群组事件监听器
                .setGroupEventListener(new TIMGroupEventListener() {
                    @Override
                    public void onGroupTipsEvent(TIMGroupTipsElem elem) {
                        LogUtil.i("onGroupTipsEvent, type: " + elem.getTipsType());
                    }
                })
                //设置会话刷新监听器
                .setRefreshListener(new TIMRefreshListener() {
                    @Override
                    public void onRefresh() {
                        LogUtil.i("onRefresh");
                    }

                    @Override
                    public void onRefreshConversation(List<TIMConversation> conversations) {
                        LogUtil.i("onRefreshConversation, conversation size: " + conversations.size());
                    }
                });

        userConfig = new TIMUserConfigMsgExt(userConfig)
                .enableStorage(false)
                .enableReadReceipt(true);
        userConfig = new TIMUserConfigSnsExt(userConfig)
                .enableFriendshipStorage(true)
                .setFriendshipProxyListener(new TIMFriendshipProxyListener() {
                    @Override
                    public void OnAddFriends(List<TIMUserProfile> users) {
                        LogUtil.i("OnAddFriends");
                    }

                    @Override
                    public void OnDelFriends(List<String> identifiers) {
                        LogUtil.i("OnDelFriends");
                    }

                    @Override
                    public void OnFriendProfileUpdate(List<TIMUserProfile> profiles) {
                        LogUtil.i("OnFriendProfileUpdate");
                    }

                    @Override
                    public void OnAddFriendReqs(List<TIMSNSChangeInfo> reqs) {
                        LogUtil.i("OnAddFriendReqs");
                    }
                });
        //设置刷新监听
        RefreshEvent.getInstance().init(userConfig);
        userConfig = FriendshipEvent.getInstance().init(userConfig);
        userConfig = GroupEvent.getInstance().init(userConfig);
        userConfig = MessageEvent.getInstance().init(userConfig);

        userConfig = new TIMUserConfigGroupExt(userConfig)
                .enableGroupStorage(true)
                .setGroupAssistantListener(new TIMGroupAssistantListener() {
                    @Override
                    public void onMemberJoin(String groupId, List<TIMGroupMemberInfo> memberInfos) {
                        LogUtil.i("onMemberJoin");
                    }

                    @Override
                    public void onMemberQuit(String groupId, List<String> members) {
                        LogUtil.i("onMemberQuit");
                    }

                    @Override
                    public void onMemberUpdate(String groupId, List<TIMGroupMemberInfo> memberInfos) {
                        LogUtil.i("onMemberUpdate");
                    }

                    @Override
                    public void onGroupAdd(TIMGroupCacheInfo groupCacheInfo) {
                        LogUtil.i("onGroupAdd");

                    }

                    @Override
                    public void onGroupDelete(String groupId) {
                        LogUtil.i("onGroupDelete");
                    }

                    @Override
                    public void onGroupUpdate(TIMGroupCacheInfo groupCacheInfo) {
                        LogUtil.i("onGroupUpdate");
                    }
                });

        TIMManager.getInstance().setUserConfig(userConfig);
    }

    /**
     * 设置未读tab显示
     */
    public void setMsgUnread(boolean noUnread) {
        msgUnread.setVisibility(noUnread ? View.GONE : View.VISIBLE);
    }

    public long getTotalUnreadNum() {

//        //获取会话扩展实例
//        TIMConversation con = TIMManager.getInstance().getConversation(TIMConversationType.C2C, peer);
//        TIMConversationExt conExt = new TIMConversationExt(con);
//        //获取会话未读数
//        long num = conExt.getUnreadMessageNum();

        List<TIMConversation> conversationList = TIMManagerExt.getInstance().getConversationList();
        long num = 0;
        for (TIMConversation conversation : conversationList) {
            if (conversation.getType() == TIMConversationType.System) {
                new TIMConversationExt(conversation).setReadMessage(null, new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onSuccess() {

                    }
                });
            }

            num += new TIMConversationExt(conversation).getUnreadMessageNum();
        }
        return num;
    }


    private void checkUpdate() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.CHECKUPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                LogUtil.i(s);
                CheckUpdateBean checkUpdateBean = new Gson().fromJson(s, CheckUpdateBean.class);
                if (checkUpdateBean != null && checkUpdateBean.getData() != null) {
                    String status = checkUpdateBean.getData().getStatus();
                    if ("2".equals(status) && checkUpdateBean.getData().getUrl() != null) {
                        showDialog(checkUpdateBean.getData().getUrl());
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("version", Constants.getVersion());
                map.put("platform", Constants.getPlatform());
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));
                return map;
            }
        };

        MyApplication.getHttpQueues().add(stringRequest);
    }

    private void showDialog(final String url) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MyApplication.getCurrentActivity());
        builder.setMessage("发现新版本，是否需要升级");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri;
                if (HttpUtils.IsUrl(url)) {
                    uri = Uri.parse(url);
                } else {
                    uri = Uri.parse("https://www.baidu.com/");
                }
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        builder.show();
    }

    @TargetApi(23)
    private void requestPermissions() {
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
//				Toast.makeText(MainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(String permission) {
                //Toast.makeText(MainActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {

        mTabs = new TextView[4];
        mTabs[0] = findViewById(R.id.tv_home);
        mTabs[1] =  findViewById(R.id.tv_shop);
        mTabs[2] =  findViewById(R.id.tv_discover);
        mTabs[3] =  findViewById(R.id.tv_me);
        mmTabsImgs=new ImageView[4];
        mmTabsImgs[0] = findViewById(R.id.iv_home);
        mmTabsImgs[1] =  findViewById(R.id.iv_shop);
        mmTabsImgs[2] =  findViewById(R.id.iv_discover);
        mmTabsImgs[3] =  findViewById(R.id.iv_me);

//        for (int i = 0; i < mTabs.length; i++) {
//            mTabs[i].setOnClickListener(this);
//            mmTabsImgs[i].setOnClickListener(this);
//
//        }
        findViewById(R.id.ll_home).setOnClickListener(this);
        findViewById(R.id.ll_shop).setOnClickListener(this);
        findViewById(R.id.ll_discover).setOnClickListener(this);
        findViewById(R.id.ll_me).setOnClickListener(this);


        // 默认首页
        mTabs[0].setSelected(true);
        mmTabsImgs[0].setSelected(true);


        SharedPreferences bus_type = getSharedPreferences("bus_type", MODE_PRIVATE);
        SharedPreferences.Editor edit = bus_type.edit();
        edit.putString(11 + "", "PC买定金");
        edit.putString(13 + "", "PC退定金");
        edit.putString(14 + "", "PC储值卡充值");
        edit.putString(15 + "", "PC储值卡退钱");
        edit.putString(16 + "", "App储值卡充值");
        edit.putString(31 + "", "App买定金");
        edit.putString(32 + "", "App买卡");
        edit.putString(33 + "", "App为他人买定金");
        edit.putString(34 + "", "App为他人买卡");
        edit.putString(21 + "", "开卡");
        edit.putString(22 + "", "卡升级");
        edit.putString(23 + "", "续卡");
        edit.putString(24 + "", "补卡");
        edit.putString(25 + "", "转卡");
        edit.putString(26 + "", "退卡");
        edit.putString(27 + "", "停卡");
        edit.putString(28 + "", "延期");
        edit.putString(29 + "", "挂失");
        edit.putString(41 + "", "租柜");
        edit.putString(42 + "", "续柜");
        edit.putString(43 + "", "退柜");
        edit.putString(44 + "", "转柜");
        edit.putString(45 + "", "换柜");
        edit.putString(51 + "", "购买私教");
        edit.putString(52 + "", "私教转会员");
        edit.putString(53 + "", "私教转教练");
        edit.putString(54 + "", "购买小团课");
        edit.putString(55 + "", "小团课转会员");
        edit.putString(56 + "", "App购买私教");
        edit.putString(57 + "", "为他人购买私教");

        edit.apply();


    }

    @Override
    protected void onStart() {
        super.onStart();
        mLocationClient = new LocationService(getApplicationContext());
        SDKInitializer.initialize(getApplicationContext());
        //mLocationClient = ((MyApplication) getApplication()).locationClient;
//        mLocationClient = new LocationService(getApplicationContext());
        mLocationClient.registerListener(myListener);


        mLocationClient.start();
        LogUtil.i("mLocationClient_start");

    }

    public String getlocationString() {
        return jingdu + "," + weidu;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationClient.unregisterListener(myListener);
        mLocationClient.stop();
        LogUtil.i("mLocationClient_stop");
        //   EMClient.getInstance().chatManager().removeMessageListener(messageListener);
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //获取周边POI信息
            //POI信息包括POI ID、名称等，具体信息请参照类参考中POI类的相关说明
            if (location != null) {
                //   LogUtil.i(location.getLocType() + "");
            }
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                weidu = location.getLatitude();
                jingdu = location.getLongitude();
                //  LogUtil.i(weidu + "----" + jingdu);
                SPUtils.setString("jingdu", jingdu + "");
                SPUtils.setString("weidu", weidu + "");
                if (shopListFragment != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("jingdu", jingdu + "");
                    bundle.putString("weidu", weidu + "");
                    shopListFragment.setArguments(bundle);
                }
//                mLocationClient.unregisterListener(myListener);
//                mLocationClient.stop();
//                LogUtil.i("mLocationClient_stop");
            } else {
                ToastUtils.showToastShort("定位失败!");
            }
        }

    }

    public void setShopFragment(ShopFragment shopFragment, ShopListFragment shopListFragment) {

        if (shopFragment != null) {
            fragments = new Fragment[]{homeFragment, shopFragment, discoverFragment, meFragment};
        } else if (shopListFragment != null) {
            fragments = new Fragment[]{homeFragment, shopListFragment, discoverFragment, meFragment};
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 111) {
            fragments = new Fragment[]{homeFragment, shopFragment, discoverFragment, meFragment};
            FragmentTransaction trx =
                    getSupportFragmentManager().beginTransaction();

            index = 1;
            currentTabIndex = 1;

            //trx.hide(fragments[currentTabIndex]);
//            if(fragments[1].equals(shopListFragment)){
//                trx.remove(fragments[1]);
//            }
//            if (!fragments[1].isAdded()) {
//                trx.remove(fragments[1]);
//                trx.add(R.id.fragment_container, fragments[1], FRAGMENT_TAG[1]);
//            }
            //  trx.replace(R.id.fragment_container, shopFragment);
            trx.hide(shopListFragment);
            trx.add(R.id.fragment_container, shopFragment);
            trx.commit();
            //判断当前页
//            if (currentTabIndex != index) {
//
//            }else{
//                if (!fragments[2].isAdded()) {
//                    trx.add(R.id.fragment_container, fragments[index], FRAGMENT_TAG[index]);
//                }
//                trx.show(fragments[2]);
//            }
            //mTabs[1].setSelected(false);
            // set current tab selected
            mTabs[1].setSelected(true);
            mmTabsImgs[1].setSelected(true);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ll_home:

                //开启动画
                mmTabsImgs[0].startAnimation(mAnimationSet);
             //   mAnimationSet.start();
                index = 0;
                break;

            case R.id.ll_shop:
                index = 1;


                mmTabsImgs[1].startAnimation(mAnimationSet);
                break;
            case R.id.ll_discover:
                LogUtil.i("ll_discover");
                index = 2;

                mmTabsImgs[2].startAnimation(mAnimationSet);
                break;
            case R.id.ll_me:
                index = 3;

                mmTabsImgs[3].startAnimation(mAnimationSet);
                break;





        }
        //判断当前页
        if (currentTabIndex != index) {
            FragmentTransaction trx =
                    getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index], FRAGMENT_TAG[index]);
            }
            trx.show(fragments[index]).commit();

            mTabs[currentTabIndex].setSelected(false);
            mmTabsImgs[currentTabIndex].setSelected(false);
            mTabs[index].setSelected(true);
            mmTabsImgs[index].setSelected(true);
        }else {

        }

        // set current tab selected

        currentTabIndex = index;


    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //主页面返回两次退出
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (JZVideoPlayer.backPress()) {
                return true;
            }

            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                System.exit(0);
                return super.onKeyDown(keyCode, event);
            }

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // unregisterBroadcastReceiver();
        EventBus.getDefault().unregister(this);
    }


    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                // refresh unread count
                //updateUnreadLabel();

                // refresh conversation list
                if (shopFragment != null) {
                    shopFragment.refresh();
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //EMClient.getInstance().chatManager().addMessageListener(messageListener);
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

                // refresh conversation list
                if (shopFragment != null) {
                    shopFragment.refresh();
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

    private void unregisterBroadcastReceiver() {
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

    /**
     * 设置mipusid
     */
    private void setMipushId() {

        StringRequest request = new StringRequest(Request.Method.PUT, Constants.SET_MIPUSH_ID, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //   LogUtil.i(s);
                Gson gson = new Gson();
                RequestInfoBean requestInfoBean = gson.fromJson(s, RequestInfoBean.class);
                if (requestInfoBean.getSuccess()) {
                    SPUtils.setBoolean(Constants.UPDATE_MIPUSH_CONFIG, false);

                    LogUtil.i("设置mipush成功");
                } else {
                    LogUtil.i("设置mipush失败");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                LogUtil.i("设置mipush失败" + volleyError.toString());

            }

        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("regId", SPUtils.getString(Constants.MY_MIPUSH_ID, null));
                map.put("terminal", "1");
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, null));

                return map;
            }
        };
        MyApplication.getHttpQueues().add(request);
    }
}
