package com.cn.danceland.myapplication.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.fragment.ShopListFragment;
import com.cn.danceland.myapplication.utils.SPUtils;

/**
 * Created by shy on 2018/4/11 11:22
 * Email:644563767@qq.com
 */


public class ShopListActivity extends FragmentActivity {

    private ShopListFragment shopListFragment;

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
        shopListFragment = new ShopListFragment();
        Bundle bundle=new Bundle();
        bundle.putString("jingdu", SPUtils.getString("jingdu","0"));

        bundle.putString("weidu",SPUtils.getString("weidu","0"));
        shopListFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().add(R.id.container, shopListFragment).commit();

    }


}
