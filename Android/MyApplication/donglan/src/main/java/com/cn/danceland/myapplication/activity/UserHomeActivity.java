package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.adapter.MyListviewAdater;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.PullBean;
import com.cn.danceland.myapplication.bean.RequestInfoBean;
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
 * Created by shy on 2017/11/1 13:55
 * Email:644563767@qq.com
 */


public class UserHomeActivity extends Activity {
    private PullToRefreshListView pullToRefresh;
    //  private List<PullBean> data = new ArrayList<PullBean>();
    List<RequsetDynInfoBean.Data.Items> data = new ArrayList<RequsetDynInfoBean.Data.Items>();
    MyListviewAdater myListviewAdater;
    private RecyclerView mRecyclerView;
    ProgressDialog dialog;
    private int mCurrentPage = 1;//当前请求页
    private String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        userId = getIntent().getStringExtra("id");
        LogUtil.i("userid:" + userId);
        //  userId="74";

        initView();
        initData();

    }

    private void initView() {


        pullToRefresh = findViewById(R.id.pullToRefresh);
        dialog = new ProgressDialog(this);
        dialog.setMessage("正在加载……");


        myListviewAdater = new MyListviewAdater(this, (ArrayList<RequsetDynInfoBean.Data.Items>) data);
        pullToRefresh.setAdapter(myListviewAdater);
        //加入头布局
        //  pullToRefresh.getRefreshableView().addHeaderView(initHeadview());

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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {      //判断标志位
                case 1:

                    myListviewAdater.notifyDataSetChanged();
                    pullToRefresh.onRefreshComplete();

                    break;
                case 2:
                    pullToRefresh.getRefreshableView().addHeaderView(initHeadview(requestInfoBean.getData()));
                    dialog.dismiss();

                    break;
            }

        }
    };

    private void initData() {
        dialog.show();
        findSelfDT();
        queryUserInfo(userId);
    }

    private RequestInfoBean requestInfoBean;

    /***
     * 查找个人资料
     * @param id 用户id
     */
    private void queryUserInfo(String id) {

        String params = id;

        String url = Constants.QUERY_USERINFO_URL + params;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String s) {
                dialog.dismiss();
                LogUtil.i(s);
                Gson gson = new Gson();
                requestInfoBean = gson.fromJson(s, RequestInfoBean.class);

                Message msg = Message.obtain();
                //   msg.obj = data;
                msg.what = 2; //标志消息的标志
                handler.sendMessage(msg);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                LogUtil.i(volleyError.toString());
                dialog.dismiss();
                ToastUtils.showToastShort("请查看网络连接");

            }

        }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, null));
                // LogUtil.i("Bearer+"+SPUtils.getString(Constants.MY_TOKEN,null));
                return map;
            }
        };
        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("queryUserInfo");
        // 设置超时时间
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);

    }

    /***
     * 查找个人动态
     */
    private void findSelfDT() {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.FIND_SELF_DT_MSG, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                RequsetDynInfoBean requsetDynInfoBean = new RequsetDynInfoBean();
                requsetDynInfoBean = gson.fromJson(s, RequsetDynInfoBean.class);
                LogUtil.i(requsetDynInfoBean.toString());
                if (requsetDynInfoBean.getSuccess()) {
                    data = requsetDynInfoBean.getData().getItems();
                    // requsetDynInfoBean.getData().getItems().toString();

                    if (data.size() > 0) {

                        myListviewAdater.addLastList((ArrayList<RequsetDynInfoBean.Data.Items>) data);
                        myListviewAdater.notifyDataSetChanged();
                        mCurrentPage = mCurrentPage + 1;
                    } else {
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


//                Message msg =Message.obtain();
//                msg.obj = data;
//                msg.what=1; //标志消息的标志
//                handler.sendMessage(msg);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.i(volleyError.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id", userId);//用户id
                map.put("page", mCurrentPage + "");//页数
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

    private View initHeadview(Data data) {

        View headview = View.inflate(this, R.layout.headview_user_home, null);
        ImageView iv_avatar = headview.findViewById(R.id.iv_avatar);
        TextView tv_nickname = headview.findViewById(R.id.tv_nickname);
        TextView tv_follwer = headview.findViewById(R.id.tv_follwer);
        TextView tv_add_gz = headview.findViewById(R.id.tv_add_gz);
        //m默认头像
        RequestOptions options = new RequestOptions().placeholder(R.drawable.img_my_avatar);
        Glide.with(this).load(data.getSelfAvatarPath()).apply(options).into(iv_avatar);
        tv_nickname.setText(data.getNickName());
        tv_follwer.setText(data.getFollower());


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
                findSelfDT();
//                Thread.sleep(100);
//                List<PullBean> list = new ArrayList<PullBean>();
//                for (int i = 0; i < 3; i++) {
//                    PullBean bean = new PullBean();
//                    bean.setTitle("派大星3333" + System.currentTimeMillis() + i);
//                    bean.setContent(DateUtils.formatDateTime(UserHomeActivity.this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL));
//
//                    list.add(bean);
//                }
//               // myListviewAdater.addLastList((ArrayList<PullBean>) list);

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
