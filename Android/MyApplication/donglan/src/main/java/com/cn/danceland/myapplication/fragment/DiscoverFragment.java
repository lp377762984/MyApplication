package com.cn.danceland.myapplication.fragment;


import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.adapter.TabAdapter;
import com.viewpagerindicator.TabPageIndicator;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverFragment extends BaseFragment {

    private ViewPager mViewPager;
    private TabPageIndicator mTabPageIndicator;
    private TabAdapter mAdapter ;


    @Override
    public View initViews() {
        View v = View.inflate(mActivity, R.layout.fragment_discover, null);
        mViewPager = (ViewPager) v.findViewById(R.id.id_viewpager);
        mTabPageIndicator = (TabPageIndicator) v.findViewById(R.id.id_indicator);
        mAdapter = new TabAdapter(getFragmentManager());
        mViewPager.setAdapter(mAdapter);

        mTabPageIndicator.setViewPager(mViewPager, 0);

        return v;
    }

    @Override
    public void initDta() {

    }
}
