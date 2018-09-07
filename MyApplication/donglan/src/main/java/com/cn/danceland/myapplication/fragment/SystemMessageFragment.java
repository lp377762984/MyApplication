package com.cn.danceland.myapplication.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.RequestNoticeListBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 系统消息
 * Created by yxx on 2018-09-06.
 */

public class SystemMessageFragment extends BaseFragment {
    private Context context;
    private PullToRefreshListView mListView;
    private RelativeLayout rl_error;
    private ImageView iv_error;
    private TextView tv_error;

    private NoticeAdapter myListAatapter;
    private Gson gson = new Gson();

    private List<RequestNoticeListBean.Data.Content> datalist = new ArrayList<>();
    private int mCurrentPage = 1;//起始请求页
    private boolean isEnd = false;

    @Override
    public View initViews() {
        View v = View.inflate(mActivity, R.layout.fragment_comment, null);
        context = this.getActivity();
        mListView = v.findViewById(R.id.lv_message);
        rl_error = v.findViewById(R.id.rl_error);
        iv_error = rl_error.findViewById(R.id.iv_error);
        Glide.with(mActivity).load(R.drawable.img_error4).into(iv_error);
        tv_error = rl_error.findViewById(R.id.tv_error);
        tv_error.setText("您还未收到任何消息");

        mListView.getRefreshableView().setEmptyView(rl_error);
        myListAatapter = new NoticeAdapter();
        mListView.setAdapter(myListAatapter);
        //设置下拉刷新模式both是支持下拉和上拉
        mListView.setMode(PullToRefreshBase.Mode.DISABLED);
//        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
//            @Override
//            public void onPullDownToRefresh(
//                    PullToRefreshBase<ListView> refreshView) {
//
//                TimerTask task = new TimerTask() {
//                    public void run() {
//                        new DownRefresh().execute();
//                    }
//                };
//                Timer timer = new Timer();
//                timer.schedule(task, 1000);
//            }
//
//            @Override
//            public void onPullUpToRefresh(
//                    PullToRefreshBase<ListView> refreshView) {
//                TimerTask task = new TimerTask() {
//                    public void run() {
//                        new UpRefresh().execute();
//                    }
//                };
//                Timer timer = new Timer();
//                timer.schedule(task, 1000);
//            }
//        });
//
//        init_pullToRefresh();
        return v;
    }
    /**
     * 加载数据
     */
    @Override
    public void initDta() {
//        try {
//            find_all_data(mCurrentPage-1);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }
    /**
     * 下拉刷新
     */
    private class DownRefresh extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            init_pullToRefresh();
            mCurrentPage = 1;
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
            mListView.onRefreshComplete();
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
            mListView.onRefreshComplete();
            myListAatapter.notifyDataSetChanged();
            if (isEnd) {//没数据了
                mListView.onRefreshComplete();
            }
        }
    }

    private void init_pullToRefresh() {
        mListView.setMode(PullToRefreshBase.Mode.BOTH);
        // 设置下拉刷新文本
        ILoadingLayout startLabels = mListView
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在加载...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        // 设置上拉刷新文本
        ILoadingLayout endLabels = mListView.getLoadingLayoutProxy(
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

        StrBean strBean = new StrBean();
        strBean.page = pageCount - 1 + "";
        String s = gson.toJson(strBean);

        JSONObject jsonObject = new JSONObject(s.toString());

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Constants.QUERY_QUERY_PAGE, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtil.i(jsonObject.toString());
                RequestNoticeListBean datainfo = new RequestNoticeListBean();
                Gson gson = new Gson();
                datainfo = gson.fromJson(jsonObject.toString(), RequestNoticeListBean.class);

                if (datainfo.getSuccess()) {
                    if (datainfo.getData().getLast()) {
                        isEnd = true;
                        setEnd();
                    } else {
                        isEnd = false;
                        init_pullToRefresh();
                    }

                    if (mCurrentPage == 1) {
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
                ToastUtils.showToastShort(volleyError.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));

                return map;
            }
        };
        MyApplication.getHttpQueues().add(stringRequest);
    }

    private void setEnd() {
        //没数据了
        isEnd = true;
        ILoadingLayout endLabels = mListView.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("—我是有底线的—");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("—我是有底线的—");// 刷新时
        endLabels.setReleaseLabel("—我是有底线的—");// 下来达到一定距离时，显示的提示
        endLabels.setLoadingDrawable(null);
    }

    class StrBean {
        public String page;
    }

    @Override
    public void onClick(View v) {

    }

    class NoticeAdapter extends BaseAdapter {

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
            ViewHolder vh = null;
            if (convertView == null) {
                vh = new ViewHolder();
                convertView = View.inflate(context, R.layout.notice_item, null);
                vh.tv_title = convertView.findViewById(R.id.tv_title);
                vh.tv_content = convertView.findViewById(R.id.tv_content);
                vh.tv_time = convertView.findViewById(R.id.tv_time);
                vh.tv_status = convertView.findViewById(R.id.tv_status);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            vh.tv_title.setText(datalist.get(position).getTitle());
            vh.tv_content.setText(datalist.get(position).getContent());
            vh.tv_time.setText(datalist.get(position).getPush_date());
            if(datalist.get(position).getStatus().equals("0")){
                vh.tv_status.setText("未读");
            }else{
                vh.tv_status.setText("已读");
            }
            return convertView;
        }

        class ViewHolder {
            public TextView tv_title;
            public TextView tv_time;
            public TextView tv_content;
            public TextView tv_status;
        }
    }
}
