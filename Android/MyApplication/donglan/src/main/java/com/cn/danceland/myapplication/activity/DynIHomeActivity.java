package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.adapter.MyListviewAdater;
import com.cn.danceland.myapplication.bean.RequsetDynInfoBean;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shy on 2017/11/17 17:32
 * Email:644563767@qq.com
 */


public class DynIHomeActivity extends Activity {
    private PullToRefreshListView pullToRefresh;
    List<RequsetDynInfoBean.Data.Items> data = new ArrayList<RequsetDynInfoBean.Data.Items>();
    MyListviewAdater myListviewAdater;
    ProgressDialog dialog;
    private int mCurrentPage = 1;//当前请求页
    private String userId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dyn_home);
        initView();
        initData();
    }

    private void initData() {


    }

    private void initView() {



    }
}
