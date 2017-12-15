package com.cn.danceland.myapplication.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.fragment.DiscoverFragment;
import com.cn.danceland.myapplication.fragment.HomeFragment;
import com.cn.danceland.myapplication.fragment.MeFragment;
import com.cn.danceland.myapplication.fragment.ShopFragment;
import com.cn.danceland.myapplication.utils.LocationService;
import com.cn.danceland.myapplication.utils.ToastUtils;

import cn.jzvd.JZVideoPlayer;

public class HomeActivity extends FragmentActivity implements View.OnClickListener {


    private Button[] mTabs;

    private Fragment[] fragments;
    private int index;
    private int currentTabIndex;
    private HomeFragment homeFragment;
    private ShopFragment shopFragment;
    private DiscoverFragment discoverFragment;
    private MeFragment meFragment;
    public static HomeActivity instance = null;
    private static final String[] FRAGMENT_TAG = {"homeFragment", "shopFragment", "discoverFragment", "meFragment"};
    public LocationService mLocationClient;
    public BDAbstractLocationListener myListener = new MyLocationListener();
    double jingdu,weidu;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //不处理崩溃时页面保存信息
        // super.onSaveInstanceState(outState);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        instance = this;
        initView();
        homeFragment = new HomeFragment();
        shopFragment = new ShopFragment();
        discoverFragment = new DiscoverFragment();
        meFragment = new MeFragment();
//        if (savedInstanceState != null) {
//            homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG[0]);
//            shopFragment = (ShopFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG[1]);
//            discoverFragment = (DiscoverFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG[2]);
//            meFragment = (MeFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG[3]);
//        }

        fragments = new Fragment[]{homeFragment, shopFragment, discoverFragment, meFragment};

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
    }

    @Override
    protected void onStart() {
           super.onStart();
        mLocationClient = MyApplication.getInstance().locationClient;
        mLocationClient.registerListener(myListener);

        mLocationClient.start();

    }
    public String getlocationString(){
        return jingdu+","+weidu;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationClient.unregisterListener(myListener);
        mLocationClient.stop();
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            //获取周边POI信息
            //POI信息包括POI ID、名称等，具体信息请参照类参考中POI类的相关说明
            //LogUtil.e("zzf",location.getLocType()+"");
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                weidu = location.getLatitude();
                jingdu = location.getLongitude();

            }else{
                ToastUtils.showToastShort("定位失败!");
            }
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
}
