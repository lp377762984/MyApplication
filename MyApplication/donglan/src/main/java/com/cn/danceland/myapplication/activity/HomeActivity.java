package com.cn.danceland.myapplication.activity;


import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.RequestInfoBean;
import com.cn.danceland.myapplication.fragment.DiscoverFragment;
import com.cn.danceland.myapplication.fragment.HomeFragment;
import com.cn.danceland.myapplication.fragment.MeFragment;
import com.cn.danceland.myapplication.fragment.ShopFragment;
import com.cn.danceland.myapplication.fragment.ShopListFragment;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LocationService;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.cn.danceland.myapplication.utils.runtimepermissions.PermissionsManager;
import com.cn.danceland.myapplication.utils.runtimepermissions.PermissionsResultAction;
import com.google.gson.Gson;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseUI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jzvd.JZVideoPlayer;

public class HomeActivity extends FragmentActivity implements View.OnClickListener {


    private Button[] mTabs;

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


//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        //不处理崩溃时页面保存信息
//        // super.onSaveInstanceState(outState);
//    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        requestPermissions();//请求权限
        instance = this;
        initView();
        registerBroadcastReceiver();//注册环信监听
        homeFragment = new HomeFragment();
        shopFragment = new ShopFragment();

        shopListFragment = new ShopListFragment();

        discoverFragment = new DiscoverFragment();
        meFragment = new MeFragment();
//        if (savedInstanceState != null) {
//            homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG[0]);
//            shopFragment = (ShopFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG[1]);
//            discoverFragment = (DiscoverFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG[2]);
//            meFragment = (MeFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG[3]);
//        }

        myInfo = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
        if (myInfo != null) {
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

        if (SPUtils.getBoolean(Constants.UPDATE_MIPUSH_CONFIG,false)){
            setMipushId();
        }
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

        mTabs = new Button[4];
        mTabs[0] = (Button) findViewById(R.id.btn_home);
        mTabs[1] = (Button) findViewById(R.id.btn_shop);
        mTabs[2] = (Button) findViewById(R.id.btn_discover);
        mTabs[3] = (Button) findViewById(R.id.btn_me);
        for (int i = 0; i < mTabs.length; i++) {
            mTabs[i].setOnClickListener(this);
        }
        // 默认首页
        mTabs[0].setSelected(true);


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
        mLocationClient = MyApplication.getInstance().locationClient;
        mLocationClient.registerListener(myListener);

        mLocationClient.start();

    }

    public String getlocationString() {
        return jingdu + "," + weidu;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationClient.unregisterListener(myListener);
        mLocationClient.stop();
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //获取周边POI信息
            //POI信息包括POI ID、名称等，具体信息请参照类参考中POI类的相关说明
            //LogUtil.e("zzf",location.getLocType()+"");
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                weidu = location.getLatitude();
                jingdu = location.getLongitude();
                SPUtils.setString("jingdu",jingdu+"");
                SPUtils.setString("weidu",weidu+"");
                if (shopListFragment != null) {
                    Bundle bundle=new Bundle();
                    bundle.putString("jingdu",jingdu+"");
                    bundle.putString("weidu",weidu+"");
                    shopListFragment.setArguments(bundle);
                }
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
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_home:
                index = 0;
                break;

            case R.id.btn_shop:
                index = 1;
                break;
            case R.id.btn_discover:
                index = 2;
                break;
            case R.id.btn_me:
                index = 3;
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
        }
        mTabs[currentTabIndex].setSelected(false);
        // set current tab selected
        mTabs[index].setSelected(true);
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
        public void onMessageChanged(EMMessage message, Object change) {
        }
    };

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
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
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
                    SPUtils.setBoolean(Constants.UPDATE_MIPUSH_CONFIG,false);

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
}}
