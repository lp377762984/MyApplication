package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.adapter.UserListviewAdapter;
import com.cn.danceland.myapplication.bean.RequsetUserListBean;
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

import static android.R.attr.value;

/**
 * Created by shy on 2017/11/21 15:08
 * Email:644563767@qq.com
 */


public class UserListActivity extends Activity implements View.OnClickListener {

    private PullToRefreshListView pullToRefresh;
    List<RequsetUserListBean.Data.Items> data = new ArrayList<RequsetUserListBean.Data.Items>();
    UserListviewAdapter mListviewAdapter;
    ProgressDialog dialog;
    private int mCurrentPage = 1;//当前请求页
    private String userId;
    private String msgId;
    private boolean isdyn = false;
    private TextView tv_tiltle;
    int type = 1;//1是关注，2是粉丝，3是点赞。默认是1

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        userId = getIntent().getStringExtra("id");
        isdyn = getIntent().getBooleanExtra("isdyn", false);
        type = getIntent().getIntExtra("type", 1);


        initView();
        initData();
    }

    private void initData() {
        switch (type) {
            case 1:
                tv_tiltle.setText("关注");
                findGZuserList(userId, 1);
                break;
            case 2:
                tv_tiltle.setText("粉丝");
                findFansUserList(userId, 1);
                break;
            case 3:
                break;
            default:
                break;
        }


    }

    private void initView() {
        tv_tiltle = findViewById(R.id.tv_tiltle);
        pullToRefresh = findViewById(R.id.pullToRefresh);
        dialog = new ProgressDialog(this);
        dialog.setMessage("正在加载……");
        findViewById(R.id.iv_back).setOnClickListener(this);

        mListviewAdapter = new UserListviewAdapter(this, data);
        pullToRefresh.setAdapter(mListviewAdapter);
        //加入头布局
        //  pullToRefresh.getRefreshableView().addHeaderView(initHeadview());

        //设置下拉刷新模式both是支持下拉和上拉
        pullToRefresh.setMode(PullToRefreshBase.Mode.PULL_FROM_END);

        init();


        pullToRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                new FinishRefresh().execute();
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }

    }


    private class FinishRefresh extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                switch (type) {
                    case 1:
                        findGZuserList(userId, mCurrentPage);
                        break;
                    case 2:
                        findFansUserList(userId, mCurrentPage);
                        break;
                    case 3:
                        break;
                    default:
                        break;
                }

            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mListviewAdapter.notifyDataSetChanged();
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


    private boolean isEnd;

    private void setEnd() {
        isEnd = true;//没数据了
        ILoadingLayout endLabels = pullToRefresh.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("—我是有底线的—");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("—我是有底线的—");// 刷新时
        endLabels.setReleaseLabel("—我是有底线的—");// 下来达到一定距离时，显示的提示
        endLabels.setLoadingDrawable(null);
        pullToRefresh.setMode(PullToRefreshBase.Mode.DISABLED);
    }


    /**
     * 查找关注人数
     *
     * @param followerId//userid
     * @param page//页数
     */
    private void findGZuserList(final String followerId, final int page) {
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Constants.FIND_GUANZHU_USER_LIST_MSG, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                dialog.dismiss();
                Gson gson = new Gson();
                RequsetUserListBean UserListBean = new RequsetUserListBean();
                UserListBean = gson.fromJson(s, RequsetUserListBean.class);
                LogUtil.i(UserListBean.toString());
                if (UserListBean.getSuccess()) {
                    data = UserListBean.getData().getItems();
                    // requsetDynInfoBean.getData().getItems().toString();


                    if (data.size() > 0) {
                        if (data.size() < 10) {
                            setEnd();
                        }


                        mListviewAdapter.addLastList((ArrayList<RequsetUserListBean.Data.Items>) data);
                        mListviewAdapter.notifyDataSetChanged();


                        mCurrentPage = mCurrentPage + 1;
                    } else {
                        ToastUtils.showToastShort("到底啦");
                        setEnd();
                    }
                } else {
                    ToastUtils.showToastShort("请求失败：" + UserListBean.getErrorMsg());
                }


//                Message msg =Message.obtain();
//                msg.obj = data;
//                msg.what=1; //标志消息的标志
//                handler.sendMessage(msg);

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
                map.put("followerId", followerId);//用户id
                map.put("page", page + "");//页数
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


    /**
     * 查找关注人数
     *
     * @param userId//userid
     * @param page//页数
     */
    private void findFansUserList(final String userId, final int page) {
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Constants.FIND_FANS_USER_LIST_MSG, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                dialog.dismiss();
                Gson gson = new Gson();
                RequsetUserListBean UserListBean = new RequsetUserListBean();
                UserListBean = gson.fromJson(s, RequsetUserListBean.class);
                LogUtil.i(UserListBean.toString());
                if (UserListBean.getSuccess()) {
                    data = UserListBean.getData().getItems();
                    // requsetDynInfoBean.getData().getItems().toString();


                    if (data.size() > 0) {
                        if (data.size() < 10) {
                            setEnd();
                        }


                        mListviewAdapter.addLastList((ArrayList<RequsetUserListBean.Data.Items>) data);
                        mListviewAdapter.notifyDataSetChanged();


                        mCurrentPage = mCurrentPage + 1;
                    } else {
                        ToastUtils.showToastShort("到底啦");
                        setEnd();
                    }
                } else {
                    ToastUtils.showToastShort("请求失败：" + UserListBean.getErrorMsg());
                }


//                Message msg =Message.obtain();
//                msg.obj = data;
//                msg.what=1; //标志消息的标志
//                handler.sendMessage(msg);

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
                map.put("userId", userId);//用户id
                map.put("page", page + "");//页数
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

}
