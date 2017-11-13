package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.adapter.MyListviewAdater;
import com.cn.danceland.myapplication.bean.PullBean;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shy on 2017/11/1 13:55
 * Email:644563767@qq.com
 */


public class UserHomeActivity extends Activity {
    private PullToRefreshListView pullToRefresh;
    private List<PullBean> data = new ArrayList<PullBean>();
    MyListviewAdater myListviewAdater;
    private RecyclerView mRecyclerView;
    ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        initData();
        initView();

    }

    private void initView() {


        pullToRefresh = findViewById(R.id.pullToRefresh);
        dialog = new ProgressDialog(this);
        dialog.setMessage("登录中……");


        myListviewAdater = new MyListviewAdater(this, (ArrayList<PullBean>) data);
        pullToRefresh.setAdapter(myListviewAdater);
        //加入头布局
        pullToRefresh.getRefreshableView().addHeaderView(initHeadview());

        //设置下拉刷新模式both是支持下拉和上拉
        pullToRefresh.setMode(PullToRefreshBase.Mode.PULL_UP_TO_REFRESH);

        init();


        pullToRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                new FinishRefresh().execute();
            }
        });


    }

    private void initData() {
        data = getData();
    }

    private View initHeadview() {

        View headview = View.inflate(this, R.layout.headview_user_home, null);
        ImageView iv_avatar=headview.findViewById(R.id.iv_avatar);

    Glide.with(this).load("http://www.027art.com/shaoer/UploadFiles_5898/201410/20141030164404328.jpg").into(iv_avatar);
//        RequestBuilder<GifDrawable> requestBuilder = Glide.with(this).asGif();
//        requestBuilder.load("http://img.mp.itc.cn/upload/20170404/eaa6a2543b0f4bc88a111ec086cad9dd_th.gif").into(iv_avatar);
        return headview;
    }

    private List<PullBean> getData() {


        List<PullBean> list = new ArrayList<PullBean>();
        for (int i = 0; i < 3; i++) {
            PullBean bean = new PullBean();
            bean.setTitle("派大星 " + i);
            bean.setContent(DateUtils.formatDateTime(this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL));
            list.add(bean);
        }

        return list;
    }
    /**
     * 一秒钟延迟
     */
    private class FinishRefresh extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
               Thread.sleep(100);
                List<PullBean> list = new ArrayList<PullBean>();
                for (int i = 0; i < 3; i++) {
                    PullBean bean = new PullBean();
                    bean.setTitle("派大星3333" + System.currentTimeMillis() + i);
                    bean.setContent(DateUtils.formatDateTime(UserHomeActivity.this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL));

                    list.add(bean);
                }
                myListviewAdater.addLastList((ArrayList<PullBean>) list);

                dialog.dismiss();

            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            myListviewAdater.notifyDataSetChanged();
            pullToRefresh.onRefreshComplete();
        }
    }


    private void init() {
        // 设置下拉刷新文本
        ILoadingLayout startLabels = pullToRefresh
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在加载...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        // 设置上拉刷新文本
        ILoadingLayout endLabels = pullToRefresh.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("上拉加载...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在加载...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示


    }

}
