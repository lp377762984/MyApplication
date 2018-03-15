package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.fragment.MyStoreFragment;
import com.cn.danceland.myapplication.fragment.SellStoreCardFragment;


/**
 * Created by feng on 2018/3/14.
 */

public class StoreCardActivity extends FragmentActivity {

    TabLayout tl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storecard);
        initView();

    }

    private void initView() {
        tl = findViewById(R.id.tl);
        showFragment(0);
        tl.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        showFragment(0);
                        break;
                    case 1:
                        showFragment(1);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public void showFragment(int type){


        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        if(type==0){
            MyStoreFragment myStoreFragment = new MyStoreFragment();
            fragmentTransaction.replace(R.id.rl_fragment,myStoreFragment);
        }else if(type==1){
            SellStoreCardFragment sellStoreCardFragment = new SellStoreCardFragment();
            fragmentTransaction.replace(R.id.rl_fragment,sellStoreCardFragment);
        }

        fragmentTransaction.commit();
    }

}
