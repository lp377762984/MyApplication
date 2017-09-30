package com.cn.danceland.myapplication.fragment;


import android.support.v4.app.Fragment;
import android.view.View;

import com.cn.danceland.myapplication.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends BaseFragment {


    public MeFragment() {
        // Required empty public constructor
    }


    @Override
    public View initViews() {
        View v = View.inflate(mActivity, R.layout.fragment_me, null);
        return v;
    }



}
