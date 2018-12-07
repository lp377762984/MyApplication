package com.cn.danceland.myapplication.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.MotionBean;
import com.cn.danceland.myapplication.bean.TextPushListBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.DensityUtils;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.MyStringRequest;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.TimeUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.cn.danceland.myapplication.view.DongLanTitleView;
import com.cn.danceland.myapplication.view.RoundImageView;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 门店 文字推送列表
 * Created by yxx on 2018-12-03.
 */

public class TextPushListActivity extends BaseActivity {
    private Context context;
    private RoundImageView circle_image;
    private TextView tv_nick_name, tv_male_age, tv_phone;
    private ImageView iv_sex;
    private PullToRefreshListView pullToRefresh;
    private ProgressDialog dialog;
    private RelativeLayout rl_error;

    private MotionDataAdapter myListAatapter;
    private List<TextPushListBean.Data.Content> datalist = new ArrayList<>();

    private Data myInfo;
    private int mCurrentPage = 0;//当前请求页
    private boolean isEnd = false;
    private int from;;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_push_list);
        context = this;
        myInfo = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
        if (this.getIntent() != null) {
            from = this.getIntent().getIntExtra("from", 0);
        }
        initView();
    }

    private void initView() {
        pullToRefresh = findViewById(R.id.pullToRefresh);
        dialog = new ProgressDialog(this);
        dialog.setMessage("正在加载……");
        rl_error = findViewById(R.id.rl_no_info);

        pullToRefresh.getRefreshableView().setEmptyView(rl_error);
        myListAatapter = new MotionDataAdapter();
        pullToRefresh.setAdapter(myListAatapter);
        pullToRefresh.getRefreshableView().setOverScrollMode(View.OVER_SCROLL_NEVER);//去掉下拉阴影
        //设置下拉刷新模式both是支持下拉和上拉
        pullToRefresh.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {

                TimerTask task = new TimerTask() {
                    public void run() {
                        new DownRefresh().execute();
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 1000);
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                TimerTask task = new TimerTask() {
                    public void run() {
                        new UpRefresh().execute();
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 1000);
            }
        });

        init_pullToRefresh();
    }

    private void refreshView() {
        datalist = new ArrayList<>();
        mCurrentPage = 0;
        try {
            find_all_data(mCurrentPage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshView();
    }

    /**
     * 下拉刷新
     */
    private class DownRefresh extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            init_pullToRefresh();
            mCurrentPage = 0;
            try {
                find_all_data(mCurrentPage);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            myListAatapter.notifyDataSetChanged();
            pullToRefresh.onRefreshComplete();
        }
    }

    /**
     * 上拉拉刷新
     */
    private class UpRefresh extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            if (!isEnd) {//还有数据请求
                try {
                    find_all_data(mCurrentPage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pullToRefresh.onRefreshComplete();
            if (isEnd) {//没数据了
                pullToRefresh.onRefreshComplete();
            }
        }
    }

    private void init_pullToRefresh() {
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
     * 查询数据
     *
     * @param pageCount
     * @throws JSONException
     */
    public void find_all_data(final int pageCount) throws JSONException {
        MyStringRequest request = new MyStringRequest(Request.Method.POST, Constants.QUERY_TEXT_PUSH_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s.toString());
                TextPushListBean datainfo = new Gson().fromJson(s.toString(), TextPushListBean.class);
                if (datainfo.getSuccess()) {
                    if ((mCurrentPage + 1) >= datainfo.getData().getTotalPages()) {
                        isEnd = true;
                        setEnd();
                    } else {
                        isEnd = false;
                        init_pullToRefresh();
                    }
                    if (mCurrentPage == 0) {
                        datalist = datainfo.getData().getContent();
                        myListAatapter.notifyDataSetChanged();
                    } else {
                        datalist.addAll(datainfo.getData().getContent());
                        myListAatapter.notifyDataSetChanged();
                    }
                    mCurrentPage = mCurrentPage + 1;
                } else {
                    ToastUtils.showToastLong(datainfo.getErrorMsg());
                }
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
                map.put("push_range", from + "");
                map.put("page", pageCount + "");
                LogUtil.i(map.toString());
                return map;
            }
        };
        MyApplication.getHttpQueues().add(request);
    }

    private void setEnd() {
        LogUtil.i("没数据了");
        isEnd = true;//没数据了
        ILoadingLayout endLabels = pullToRefresh.getLoadingLayoutProxy(
                false, true);
//        endLabels.setPullLabel("—我是有底线的—");// 刚下拉时，显示的提示
//        endLabels.setRefreshingLabel("—我是有底线的—");// 刷新时
        endLabels.setReleaseLabel("—我是有底线的—");// 下来达到一定距离时，显示的提示
        endLabels.setLoadingDrawable(null);
    }

    class MotionDataAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return datalist.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            MotionDataAdapter.ViewHolder vh = null;
            if (convertView == null) {
                vh = new MotionDataAdapter.ViewHolder();
                convertView = View.inflate(context, R.layout.item_text_push, null);
                vh.icon_iv = convertView.findViewById(R.id.icon_iv);
                vh.tv_time = convertView.findViewById(R.id.tv_time);
                vh.tv_title = convertView.findViewById(R.id.tv_title);
                vh.tv_content = convertView.findViewById(R.id.tv_content);
                vh.item_layout = convertView.findViewById(R.id.item_layout);
                vh.item_layout_cv = convertView.findViewById(R.id.item_layout_cv);
                convertView.setTag(vh);
            } else {
                vh = (MotionDataAdapter.ViewHolder) convertView.getTag();
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (position == 0) {
                layoutParams.setMargins(DensityUtils.dp2px(context, 16f), DensityUtils.dp2px(context, 16f), DensityUtils.dp2px(context, 16f), DensityUtils.dp2px(context, 11f));
            } else if (position == datalist.size() - 1) {
                layoutParams.setMargins(DensityUtils.dp2px(context, 16f), DensityUtils.dp2px(context, 5f), DensityUtils.dp2px(context, 16f), DensityUtils.dp2px(context, 16f));
            } else {
                layoutParams.setMargins(DensityUtils.dp2px(context, 16f), DensityUtils.dp2px(context, 5f), DensityUtils.dp2px(context, 16f), DensityUtils.dp2px(context, 16f));
            }
            switch (from) { //推送范围(100:店长推送;101:会籍推送;102:教练推送;103:服务部推送)
                case 100:
                    vh.icon_iv.setImageDrawable(getResources().getDrawable(R.drawable.text_push_whole_shop_icon));
                    break;
                case 101:
                    vh.icon_iv.setImageDrawable(getResources().getDrawable(R.drawable.text_push_membership_icon));
                    break;
                case 102:
                    vh.icon_iv.setImageDrawable(getResources().getDrawable(R.drawable.text_push_coach_icon));
                    break;
                case 103:
                    vh.icon_iv.setImageDrawable(getResources().getDrawable(R.drawable.text_push_service_icon));
                    break;
            }
            vh.item_layout_cv.setLayoutParams(layoutParams);
            vh.tv_time.setText(datalist.get(position).getPush_time());
            vh.tv_title.setText(datalist.get(position).getTitle());
            vh.tv_content.setText(datalist.get(position).getContent());

            vh.item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            return convertView;
        }

        class ViewHolder {
            public TextView tv_title;
            public TextView tv_time;
            public TextView tv_content;
            public ImageView icon_iv;

            public LinearLayout item_layout;
            public CardView item_layout_cv;
        }
    }
}
