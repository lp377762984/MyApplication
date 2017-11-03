package com.cn.danceland.myapplication.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ListView;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.adapter.MyListviewAdater;
import com.cn.danceland.myapplication.adapter.MyRecylerViewAdapter;
import com.cn.danceland.myapplication.bean.PullBean;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shy on 2017/10/20 13:56
 * Email:644563767@qq.com
 * 精选页面
 */


public class SelectionFragment extends BaseFragment {
    private PullToRefreshListView pullToRefresh;
    private List<PullBean> data = new ArrayList<PullBean>();
    MyListviewAdater myListviewAdater;
    private RecyclerView mRecyclerView;
    ProgressDialog dialog;


    @Override
    public View initViews() {
        View v = View.inflate(mActivity, R.layout.fragment_selection, null);
        pullToRefresh = v.findViewById(R.id.pullToRefresh);
        dialog = new ProgressDialog(mActivity);
        dialog.setMessage("登录中……");
        dialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);//延时1s
                  dialog.dismiss();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        data = getData();
        myListviewAdater = new MyListviewAdater(mActivity, (ArrayList<PullBean>) data);
        pullToRefresh.setAdapter(myListviewAdater);
        //加入头布局
        pullToRefresh.getRefreshableView().addHeaderView(initHeadview());

        //设置下拉刷新模式both是支持下拉和上拉
        pullToRefresh.setMode(PullToRefreshBase.Mode.BOTH);

        init();

        pullToRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // TODO Auto-generated method stub
                //最后刷新时间
//                String label = DateUtils.formatDateTime(mActivity, System.currentTimeMillis(),
//                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
//                PullBean bean = new PullBean();
//                bean.setTitle("派大星333");
//                bean.setContent("最近发布");
//                myListviewAdater.addFirst(bean);

                new FinishRefresh().execute();
                myListviewAdater.notifyDataSetChanged();


            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // TODO Auto-generated method stub
                //最后刷新时间
//                String label = "最后更新时间："+DateUtils.formatDateTime(mActivity, System.currentTimeMillis(),
//                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

//                PullBean bean = new PullBean();
//                bean.setTitle("派大星");
//                bean.setContent("最近发布");
//                myListviewAdater.addLast(bean);
                List<PullBean> list = new ArrayList<PullBean>();
                for (int i = 0; i < 3; i++) {
                    PullBean bean = new PullBean();
                    bean.setTitle("派大星222" + System.currentTimeMillis() + i);
                    bean.setContent(DateUtils.formatDateTime(mActivity, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL));

                    list.add(bean);
                }
                myListviewAdater.addLastList((ArrayList<PullBean>) list);
                new FinishRefresh().execute();
                //   myListviewAdater.notifyDataSetChanged();
                //  pullToRefresh.getRefreshableView().setSelection(1);
                //   LogUtil.i( pullToRefresh.getRefreshableView().getFirstVisiblePosition()+"");
                // myListviewAdater.notifyDataSetChanged();
            }
        });

        return v;
    }

    private View initHeadview() {

        View headview = View.inflate(mActivity, R.layout.recycleview_headview, null);

        mRecyclerView = headview.findViewById(R.id.my_recycler_view);


        //创建默认的线性LayoutManager

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        //创建并设置Adapter
        MyRecylerViewAdapter mAdapter = new MyRecylerViewAdapter(mActivity,new String[]{"章魚哥", "派大星", "海绵宝宝", "派大星", "派大星", "派大星", "派大星", "派大星"});
        mRecyclerView.setAdapter(mAdapter);
        return headview;
    }


    @Override
    public void initDta() {
        super.initDta();
    }


    private List<PullBean> getData() {


        List<PullBean> list = new ArrayList<PullBean>();
        for (int i = 0; i < 3; i++) {
            PullBean bean = new PullBean();
            bean.setTitle("派大星 " + i);
            bean.setContent(DateUtils.formatDateTime(mActivity, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL));
            list.add(bean);
        }

        return list;
    }

    @Override
    public void onClick(View view) {

    }

    /**
     * 一秒钟延迟
     */
    private class FinishRefresh extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
                List<PullBean> list = new ArrayList<PullBean>();
                for (int i = 0; i < 3; i++) {
                    PullBean bean = new PullBean();
                    bean.setTitle("派大星3333" + System.currentTimeMillis() + i);
                    bean.setContent(DateUtils.formatDateTime(mActivity, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL));

                    list.add(bean);
                }
                myListviewAdater.addFirstList((ArrayList<PullBean>) list);

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
