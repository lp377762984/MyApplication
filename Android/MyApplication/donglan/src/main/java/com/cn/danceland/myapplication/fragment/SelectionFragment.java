package com.cn.danceland.myapplication.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.activity.FitnessManActivity;
import com.cn.danceland.myapplication.adapter.MyListviewAdater;
import com.cn.danceland.myapplication.adapter.MyRecylerViewAdapter;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.PullBean;
import com.cn.danceland.myapplication.bean.RequestPushUserBean;
import com.cn.danceland.myapplication.bean.RequsetDynInfoBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shy on 2017/10/20 13:56
 * Email:644563767@qq.com
 * 精选页面
 */


public class SelectionFragment extends BaseFragment {
    private PullToRefreshListView pullToRefresh;
    private List<RequsetDynInfoBean.Data.Items> data = new ArrayList<>();
    private RequsetDynInfoBean requsetDynInfoBean = new RequsetDynInfoBean();
    private List<Data> pushUserDatas = new ArrayList<>();
    MyListviewAdater myListviewAdater;
    private RecyclerView mRecyclerView;
    ProgressDialog dialog;
    int mCurrentPage = 1;//当前请求页
    private MyRecylerViewAdapter mRecylerViewAdapter;
    private View headView;
    private boolean isEnd = false;//是否没有数据了 默认值false

    @Override
    public View initViews() {
        View v = View.inflate(mActivity, R.layout.fragment_selection, null);
        pullToRefresh = v.findViewById(R.id.pullToRefresh);
        headView = initHeadview();
        dialog = new ProgressDialog(mActivity);
        dialog.setMessage("登录中……");
//        dialog.show();
//

        //    data = getData();


        myListviewAdater = new MyListviewAdater(mActivity, (ArrayList<RequsetDynInfoBean.Data.Items>) data);
        pullToRefresh.setAdapter(myListviewAdater);
        //加入头布局
        /// pullToRefresh.getRefreshableView().addHeaderView(initHeadview());

        //设置下拉刷新模式both是支持下拉和上拉
        pullToRefresh.setMode(PullToRefreshBase.Mode.BOTH);

        init();

        pullToRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // TODO Auto-generated method stub


                //   new FinishRefresh().execute();
                //  myListviewAdater.notifyDataSetChanged();

                new DownRefresh().execute();


            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // TODO Auto-generated method stub

                List<PullBean> list = new ArrayList<PullBean>();
                for (int i = 0; i < 3; i++) {
                    PullBean bean = new PullBean();
                    bean.setTitle("派大星222" + System.currentTimeMillis() + i);
                    bean.setContent(DateUtils.formatDateTime(mActivity, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL));

                    list.add(bean);
                }
                //     myListviewAdater.addLastList((ArrayList<<RequsetDynInfoBean.Data.Items>) list);
                //new FinishRefresh().execute();

                //   myListviewAdater.notifyDataSetChanged();
                //  pullToRefresh.getRefreshableView().setSelection(1);
                //   LogUtil.i( pullToRefresh.getRefreshableView().getFirstVisiblePosition()+"");
                // myListviewAdater.notifyDataSetChanged();

                new UpRefresh().execute();
            }
        });


        return v;
    }


    private View initHeadview() {

        View headview = View.inflate(mActivity, R.layout.recycleview_headview, null);

        mRecyclerView = headview.findViewById(R.id.my_recycler_view);
        headview.findViewById(R.id.tv_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FitnessManActivity.class);
                startActivity(intent);
            }
        });

        //创建默认的线性LayoutManager

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        //创建并设置Adapter
        //   MyRecylerViewAdapter mAdapter = new MyRecylerViewAdapter(mActivity, new String[]{"章魚哥", "派大星", "海绵宝宝", "派大星", "派大星", "派大星", "派大星", "派大星"});
        mRecylerViewAdapter = new MyRecylerViewAdapter(mActivity, pushUserDatas);
        mRecyclerView.setAdapter(mRecylerViewAdapter);
        return headview;
    }

    /**
     * 加载数据
     */
    @Override
    public void initDta() {
        dialog.show();
        findSelectionDyn_Down(1);
        findPushUser();
    }


    @Override
    public void onClick(View view) {

    }

    /**
     * 下拉刷新
     */
    private class DownRefresh extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            findSelectionDyn_Down(1);
            init();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
            myListviewAdater.notifyDataSetChanged();
            pullToRefresh.onRefreshComplete();
        }
    }


    /**
     * 上拉拉刷新
     */
    private class UpRefresh extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            if (!isEnd){//还有数据请求
                findSelectionDyn_Up(mCurrentPage);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
            myListviewAdater.notifyDataSetChanged();
            pullToRefresh.onRefreshComplete();
        }
    }


    /**
     * 一秒钟延迟
     */
    private class FinishRefresh extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
//                Thread.sleep(1000);
//                List<PullBean> list = new ArrayList<PullBean>();
//                for (int i = 0; i < 3; i++) {
//                    PullBean bean = new PullBean();
//                    bean.setTitle("派大星3333" + System.currentTimeMillis() + i);
//                    bean.setContent(DateUtils.formatDateTime(mActivity, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL));
//
//                    list.add(bean);
//                }
                //   myListviewAdater.addFirstList((ArrayList<PullBean>) list);

                //  findSelfDT();


                // mCurrentPage=mCurrentPage+1;

                dialog.dismiss();

            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            myListviewAdater.notifyDataSetChanged();
            pullToRefresh.onRefreshComplete();
        }
    }

    /**
     * 查找推荐用户
     */
    private void findPushUser() {


        StringRequest request = new StringRequest(Request.Method.POST, Constants.FIND_PUSH_MSG, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                dialog.dismiss();
                Gson gson = new Gson();
                RequestPushUserBean pushUserBean = new RequestPushUserBean();
                pushUserBean = gson.fromJson(s, RequestPushUserBean.class);

                LogUtil.i(pushUserBean.toString());
                pushUserDatas = pushUserBean.getData();
                if (pushUserDatas.size() > 0) {
                    mRecylerViewAdapter.setData(pushUserDatas);
                    mRecylerViewAdapter.notifyDataSetChanged();
                    pullToRefresh.getRefreshableView().addHeaderView(headView);
                } else {
                    ToastUtils.showToastShort("没有数据");
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dialog.dismiss();
                LogUtil.i(volleyError.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("uid", SPUtils.getString(Constants.MY_USERID, null));//用户id

                return map;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> hm = new HashMap<String, String>();
                String token = SPUtils.getString(Constants.MY_TOKEN, "");
                hm.put("Authorization", token);
                return hm;
            }

        };
        MyApplication.getHttpQueues().add(request);

    }


    /***
     * 下拉查找精选动态
     * @param page
     */
    private void findSelectionDyn_Down(final int page) {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.FIND_JINGXUAN_DT_MSG, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                dialog.dismiss();

                Gson gson = new Gson();
                requsetDynInfoBean = gson.fromJson(s, RequsetDynInfoBean.class);
                LogUtil.i(requsetDynInfoBean.toString());
                if (requsetDynInfoBean.getSuccess()) {
                    data = requsetDynInfoBean.getData().getItems();
                    myListviewAdater.setData((ArrayList<RequsetDynInfoBean.Data.Items>) data);
                    myListviewAdater.notifyDataSetChanged();
                    mCurrentPage = 2;//下次从第二页请求
                } else {
                    ToastUtils.showToastShort(requsetDynInfoBean.getErrorMsg());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToastShort("查看网络连接");
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id", "");//用户id
                map.put("currentPage", page + "");//页数
                return map;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> hm = new HashMap<String, String>();
                String token = SPUtils.getString(Constants.MY_TOKEN, "");
                hm.put("Authorization", token);
                return hm;
            }

        };
        MyApplication.getHttpQueues().add(request);

    }

    /***
     * 上拉查找精选动态
     */
    private void findSelectionDyn_Up(final int page) {

        StringRequest request = new StringRequest(Request.Method.POST, Constants.FIND_JINGXUAN_DT_MSG, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                //  RequsetDynInfoBean requsetDynInfoBean=new RequsetDynInfoBean();
                requsetDynInfoBean = gson.fromJson(s, RequsetDynInfoBean.class);

                if (requsetDynInfoBean.getSuccess()) {
                    data = requsetDynInfoBean.getData().getItems();
                    // requsetDynInfoBean.getData().getItems().toString();
                    LogUtil.i(requsetDynInfoBean.getData().toString());
                    if (data.size() > 0) {

                        myListviewAdater.addLastList((ArrayList<RequsetDynInfoBean.Data.Items>) data);
                        myListviewAdater.notifyDataSetChanged();
                        mCurrentPage = mCurrentPage + 1;
                    } else {
                        isEnd = true;
                        ToastUtils.showToastShort("到底啦");
                        ILoadingLayout endLabels = pullToRefresh.getLoadingLayoutProxy(
                                false, true);
                        endLabels.setPullLabel("—我是有底线的—");// 刚下拉时，显示的提示
                        endLabels.setRefreshingLabel("—我是有底线的—");// 刷新时
                        endLabels.setReleaseLabel("—我是有底线的—");// 下来达到一定距离时，显示的提示
                        endLabels.setLoadingDrawable(null);
                    }
                } else {
                    ToastUtils.showToastShort("请求失败：" + requsetDynInfoBean.getErrorMsg());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToastShort("查看网络连接");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id", SPUtils.getString(Constants.MY_USERID, ""));//用户id
                map.put("currentPage", page + "");//页数
                return map;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> hm = new HashMap<String, String>();
                String token = SPUtils.getString(Constants.MY_TOKEN, "");
                hm.put("Authorization", token);
                return hm;
            }

        };
        MyApplication.getHttpQueues().add(request);


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
