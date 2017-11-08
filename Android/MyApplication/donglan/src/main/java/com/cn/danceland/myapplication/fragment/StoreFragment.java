package com.cn.danceland.myapplication.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.cn.danceland.myapplication.R;

/**
 * Created by feng on 2017/11/6.
 */

public class StoreFragment extends BaseFragment {

    @Override
    public View initViews() {
        View v  = View.inflate(mActivity, R.layout.activity_store,null);
        TextView store_name = v.findViewById(R.id.store_name);

        return v;
    }

    @Override
    public void onClick(View v) {

    }
}
