package com.cn.danceland.myapplication.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.fragment.ShopListFragment;

/**
 * Created by shy on 2018/4/11 11:22
 * Email:644563767@qq.com
 */


public class ShopListActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ShopListFragment shopListFragment=new ShopListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container, shopListFragment).commit();


    }




}
