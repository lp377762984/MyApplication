package com.cn.danceland.myapplication.fragment;


import android.support.v4.app.Fragment;
import android.view.View;

import com.cn.danceland.myapplication.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends BaseFragment {


    @Override
    public View initViews() {
        View v = View.inflate(mActivity, R.layout.fragment_shop, null);
        return v;
    }

    @Override
    public void initDta() {

    }
}
