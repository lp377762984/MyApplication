package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.cn.danceland.myapplication.adapter.UserHomeDynListviewAdater;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.RequestInfoBean;
import com.cn.danceland.myapplication.bean.RequsetDynInfoBean;
import com.cn.danceland.myapplication.evntbus.EventConstants;
import com.cn.danceland.myapplication.evntbus.IntEvent;
import com.cn.danceland.myapplication.evntbus.StringEvent;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jzvd.JZVideoPlayer;

import static com.cn.danceland.myapplication.R.id.iv_avatar;
import static com.cn.danceland.myapplication.R.id.tv_nickname;

/**
 * Created by shy on 2017/11/1 13:55
 * Email:644563767@qq.com
 */


public class UserHomeActivity extends Activity {
    private PullToRefreshListView pullToRefresh;
    //  private List<PullBean> data = new ArrayList<PullBean>();
    public List<RequsetDynInfoBean.Data.Items> data = new ArrayList<RequsetDynInfoBean.Data.Items>();
    UserHomeDynListviewAdater myDynListviewAdater;
    private RecyclerView mRecyclerView;
    ProgressDialog dialog;
    private int mCurrentPage = 1;//当前请求页
    private String userId;
    private boolean isdyn = false;
    private ImageView iv_userifno_avatar;
    private TextView tv_head_nickname;
    private TextView tv_no_data;
    private TextView tv_add_gz;

    private Data userInfo;
    private TextView tv_fans;
    private TextView tv_guanzhu_num;
    private LinearLayout ll_edit;
    private ImageView iv_sex;

    private int from ;//来着那个页面
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册event事件
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_user_home);
        userId = getIntent().getStringExtra("id");
        isdyn = getIntent().getBooleanExtra("isdyn", false);
        from=getIntent().getIntExtra("from",-1);
        //    LogUtil.i("userid:" + userId);
        //  userId="74";

        initView();
        initData();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        EventBus.getDefault().post(new IntEvent(from,8901));

        EventBus.getDefault().unregister(this);
    }

    private void initView() {


        pullToRefresh = findViewById(R.id.pullToRefresh);
        dialog = new ProgressDialog(this);
        dialog.setMessage("正在加载……");


        myDynListviewAdater = new UserHomeDynListviewAdater(this, (ArrayList<RequsetDynInfoBean.Data.Items>) data);
        myDynListviewAdater.setGzType(true);//隐藏关注按钮
        pullToRefresh.setAdapter(myDynListviewAdater);
        //加入头布局
        pullToRefresh.getRefreshableView().addHeaderView(initHeadview(userInfo));

        //设置下拉刷新模式both是支持下拉和上拉
        pullToRefresh.setMode(PullToRefreshBase.Mode.PULL_FROM_END);

        init();


        pullToRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                new FinishRefresh().execute();
            }
        });
        pullToRefresh.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //    JZVideoPlayer.onScrollReleaseAllVideos(view, firstVisibleItem, visibleItemCount, totalItemCount);
                //   LogUtil.i("firstVisibleItem="+firstVisibleItem+"visibleItemCount="+visibleItemCount+"totalItemCount="+totalItemCount);


                JZVideoPlayer.onScrollAutoTiny(view, firstVisibleItem, visibleItemCount, 2);
            }
        });

    }

    private void setHeadViewData(final Data data) {

        if (TextUtils.equals(data.getGender(), "1")) {
            iv_sex.setImageResource(R.drawable.img_sex1);
        } else if (TextUtils.equals(data.getGender(), "2")) {
            iv_sex.setImageResource(R.drawable.img_sex2);
        } else {
            iv_sex.setVisibility(View.INVISIBLE);
        }


        tv_guanzhu_num.setText("关注 " + data.getFollowNumber());
        tv_guanzhu_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//查看关注
                startActivity(new Intent(UserHomeActivity.this, UserListActivity.class).putExtra("id", userId).putExtra("type", 1));

            }
        });
        tv_fans.setText("粉丝 " + data.getFansNum());
        tv_fans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//查看粉丝
                startActivity(new Intent(UserHomeActivity.this, UserListActivity.class).putExtra("id", userId).putExtra("type", 2));

            }
        });


        if (data.getFollower()) {
            if (TextUtils.equals(SPUtils.getString(Constants.MY_USERID, null), data.getId())) {
                tv_add_gz.setVisibility(View.INVISIBLE);
            } else {
                tv_add_gz.setText("已关注");
            }

        } else {
            tv_add_gz.setText("+关注");
        }
        tv_add_gz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.equals(tv_add_gz.getText().toString(), "+关注")) {//未关注添加关注

                    addGuanzhu(userId, true);
                } else {//已关注取消关注
                    addGuanzhu(userId, false);
                }

            }
        });


        if (TextUtils.equals(data.getId(), SPUtils.getString(Constants.MY_USERID, null))) {

            ll_edit.setVisibility(View.VISIBLE);
            ll_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(UserHomeActivity.this, MyProActivity.class));
                }
            });
            tv_add_gz.setVisibility(View.GONE);
        }

        //m默认头像
        RequestOptions options = new RequestOptions().placeholder(R.drawable.img_my_avatar);
        Glide.with(this).load(data.getSelfAvatarPath()).apply(options).into(iv_userifno_avatar);
        tv_head_nickname.setText(data.getNickName());

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {      //判断标志位
                case 1:
                    dialog.dismiss();
                    myDynListviewAdater.notifyDataSetChanged();
                    pullToRefresh.onRefreshComplete();

                    break;
                case 2:
                    //    pullToRefresh.getRefreshableView().addHeaderView(initHeadview(requestInfoBean.getData()));
                    setHeadViewData(userInfo);
                    dialog.dismiss();

                    if (isdyn) {//跳转到动态的那行

                        pullToRefresh.getRefreshableView().setSelection(2);
                        // pullToRefresh.getRefreshableView().smoothScrollToPosition(2);
                    }
                    break;
                case 3:
                    //  tv_no_data.setVisibility(View.VISIBLE);
                    break;
            }

        }
    };



    //even事件处理
    @Subscribe
    public void onEventMainThread(IntEvent event) {
        LogUtil.i("收到消息" + event.getEventCode());


        switch (event.getEventCode()) {

            case EventConstants.DEL_DYN://删除动态

                int pos = event.getMsg();

                myDynListviewAdater.data.remove(pos);
                myDynListviewAdater.notifyDataSetChanged();
                break;


            default:
                break;
        }

    }


    //even事件处理
    @Subscribe
    public void onEventMainThread(StringEvent event) {

        int dynpos = -1;
        switch (event.getEventCode()) {


            case EventConstants.ADD_ZAN_DYN_HOME://点赞
                for (int i = 0; i < myDynListviewAdater.data.size(); i++) {
                    if (TextUtils.equals(myDynListviewAdater.data.get(i).getId(), event.getMsg())) {
                        dynpos = i;
                    }
                }

                if (dynpos >= 0) {
                    myDynListviewAdater.data.get(dynpos).setPraise(true);
                    myDynListviewAdater.data.get(dynpos).setPriaseNumber(myDynListviewAdater.data.get(dynpos).getPriaseNumber() + 1);
                    myDynListviewAdater.notifyDataSetChanged();
                }

                break;
            case EventConstants.DEL_ZAN_DYN_HOME://取消点赞
                for (int i = 0; i < myDynListviewAdater.data.size(); i++) {
                    if (TextUtils.equals(myDynListviewAdater.data.get(i).getId(), event.getMsg())) {
                        dynpos = i;
                    }
                }
                if (dynpos >= 0) {
                    myDynListviewAdater.data.get(dynpos).setPraise(false);
                    myDynListviewAdater.data.get(dynpos).setPriaseNumber(myDynListviewAdater.data.get(dynpos).getPriaseNumber() - 1);
                    myDynListviewAdater.notifyDataSetChanged();
                }
                break;


            case EventConstants.ADD_DYN:

                break;
            case EventConstants.DEL_DYN_DYN_HOME:

                break;
            case 99:
                String msg = event.getMsg();
                LogUtil.i("收到消息" + msg);
                RequestOptions options = new RequestOptions().placeholder(R.drawable.img_my_avatar);
                Glide.with(this).load(msg).apply(options).into(iv_userifno_avatar);

                break;
            case 100:
                if (100 == event.getEventCode()) {
                    tv_head_nickname.setText(event.getMsg());
                }
                break;
            case EventConstants.ADD_GUANZHU:

                tv_add_gz.setText("已关注");
                requestInfoBean.getData().setFollower(true);

                break;
            case EventConstants.DEL_GUANZHU:
                tv_add_gz.setText("+关注");
                requestInfoBean.getData().setFollower(false);
                break;
            case EventConstants.ADD_ZAN:

                break;
            case EventConstants.DEL_ZAN:

                break;
            default:
                break;
        }


    }

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
    private void queryUserInfo(final String id) {

        String params = id;

        String url = Constants.QUERY_USERINFO_URL + params;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String s) {

                LogUtil.i(s);
                Gson gson = new Gson();
                requestInfoBean = gson.fromJson(s, RequestInfoBean.class);



                userInfo = requestInfoBean.getData();



                if (TextUtils.equals(id,SPUtils.getString(Constants.MY_USERID,null))){
                    //如果是本人更新本地缓存
                    DataInfoCache.saveOneCache(userInfo,Constants.MY_INFO);

                    EventBus.getDefault().post(new StringEvent("",EventConstants.UPDATE_USER_INFO));
                }



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

    /***
     * 查找个人动态
     */
    private void findSelfDT() {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.FIND_SELF_DT_MSG, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s);

                Gson gson = new Gson();
                RequsetDynInfoBean requsetDynInfoBean = new RequsetDynInfoBean();
                requsetDynInfoBean = gson.fromJson(s, RequsetDynInfoBean.class);
                // LogUtil.i(requsetDynInfoBean.toString());
                if (requsetDynInfoBean.getSuccess()) {
                    data = requsetDynInfoBean.getData().getItems();
                    // requsetDynInfoBean.getData().getItems().toString();
                    LogUtil.i(data.toString());
                    if (data.size() > 0) {
                        if (data.size() < 10) {
                            setEnd();

                        }


                        myDynListviewAdater.addLastList((ArrayList<RequsetDynInfoBean.Data.Items>) data);
               //         myDynListviewAdater.notifyDataSetChanged();
                        mCurrentPage = mCurrentPage + 1;
                    } else {
                        // LogUtil.i(mCurrentPage + "@@@@@" + data.size());
                        if (mCurrentPage == 1) {
                            Message message = Message.obtain();
                            message.what = 3;
                            handler.sendMessage(message);


                        }


                        setEnd();
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

    private View initHeadview(final Data data) {

        View headview = View.inflate(this, R.layout.headview_user_home, null);
        iv_userifno_avatar = headview.findViewById(iv_avatar);
        tv_head_nickname = headview.findViewById(tv_nickname);
        tv_no_data = headview.findViewById(R.id.tv_no_data);
        tv_fans = headview.findViewById(R.id.tv_fans);
        tv_guanzhu_num = headview.findViewById(R.id.tv_guanzhu_num);
        tv_add_gz = headview.findViewById(R.id.tv_add_gz);
        ll_edit = headview.findViewById(R.id.ll_edit);
        iv_sex = headview.findViewById(R.id.iv_sex);
        headview.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        return headview;
    }

    private boolean isEnd;

    /**
     * 一秒钟延迟
     */
    private class FinishRefresh extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (!isEnd) {//还有数据请求
                    findSelfDT();
                }


                dialog.dismiss();

            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            myDynListviewAdater.notifyDataSetChanged();
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

    /**
     * 加关注
     *
     * @param id
     * @param b
     */
    private void addGuanzhu(final String id, final boolean b) {

        StringRequest request = new StringRequest(Request.Method.POST, Constants.ADD_GUANZHU, new Response.Listener<String>() {


            @Override
            public void onResponse(String s) {
                //  LogUtil.i(s);
                Gson gson = new Gson();
                RequestInfoBean requestInfoBean = new RequestInfoBean();
                requestInfoBean = gson.fromJson(s, RequestInfoBean.class);
                if (b) {
                    if (requestInfoBean.getSuccess()) {

                        ToastUtils.showToastShort("关注成功");
                        EventBus.getDefault().post(new StringEvent(userId, EventConstants.ADD_GUANZHU));

                    } else {
                        ToastUtils.showToastShort("关注失败");
                    }
                } else {

                    if (requestInfoBean.getSuccess()) {

                        ToastUtils.showToastShort("取消关注成功");
                        EventBus.getDefault().post(new StringEvent(userId, EventConstants.DEL_GUANZHU));

                    } else {
                        ToastUtils.showToastShort("取消关注失败");
                    }


                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                LogUtil.i(volleyError.toString());
                ToastUtils.showToastShort("请查看网络连接");
            }

        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("userId", id);
                map.put("isFollower", String.valueOf(b));
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, null));
                // LogUtil.i("Bearer+"+SPUtils.getString(Constants.MY_TOKEN,null));
                LogUtil.i(SPUtils.getString(Constants.MY_TOKEN, null));
                return map;
            }
        };
        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("addGuanzhu");
        // 设置超时时间
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);

    }

}