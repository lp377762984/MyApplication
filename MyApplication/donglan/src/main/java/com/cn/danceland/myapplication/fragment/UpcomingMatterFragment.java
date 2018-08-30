package com.cn.danceland.myapplication.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.RequsetSimpleBean;
import com.cn.danceland.myapplication.bean.RequsetUpcomingMaterListBean;
import com.cn.danceland.myapplication.evntbus.IntEvent;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.cn.danceland.myapplication.view.ContainsEmojiEditText;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.cn.danceland.myapplication.R.id.tv_lasttime;
import static com.cn.danceland.myapplication.R.id.tv_name;
import static com.cn.danceland.myapplication.R.id.tv_result;

/**
 * Created by shy on 2018/1/11 17:18
 * Email:644563767@qq.com
 */


public class UpcomingMatterFragment extends BaseFragment {

    private PullToRefreshListView mListView;
    private List<RequsetUpcomingMaterListBean.Data.Content> datalist = new ArrayList<>();

    private MyListAatapter myListAatapter;

    private Gson gson = new Gson();
    private int mCurrentPage = 1;//起始请求页
    private boolean isEnd = false;
    private String id;
    private String done="0";//0是未办，1是已办
    private String auth="1";
    private TextView tv_error;
    private ImageView imageView;

    @Override
    public View initViews() {
        View v = View.inflate(mActivity, R.layout.fragment_upcoming_matter_list, null);
//        v.findViewById(R.id.btn_add).setOnClickListener(this);
        mListView = v.findViewById(R.id.pullToRefresh);
        View    listEmptyView=v.findViewById(R.id.rl_no_info);
        tv_error = listEmptyView.findViewById(R.id.tv_error);
        imageView = listEmptyView.findViewById(R.id.iv_error);
        mListView.setEmptyView(listEmptyView);
        myListAatapter = new MyListAatapter();
        mListView.setAdapter(myListAatapter);
        //设置下拉刷新模式both是支持下拉和上拉
        mListView.setMode(PullToRefreshBase.Mode.BOTH);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                TimerTask task = new TimerTask(){
                    public void run(){
                        new DownRefresh().execute();
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 1000);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                TimerTask task = new TimerTask(){
                    public void run(){
                        new UpRefresh().execute();
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 1000);
            }
        });
        init_pullToRefresh();
        id = getArguments().getString("id");
        auth=getArguments().getString("auth");
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册event事件
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(IntEvent event) {

        switch (event.getEventCode()) {

            case 151://刷新页面
                mCurrentPage = 1;
                try {
                    find_upcoming_list(id, mCurrentPage,done);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 152://未办

                mCurrentPage = 1;
                done="0";
                try {
                    find_upcoming_list(id, mCurrentPage,done);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 153://已完成
                mCurrentPage = 1;
                done="1";
                try {
                    find_upcoming_list(id, mCurrentPage,done);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 154://全部
                mCurrentPage = 1;
                done=null;
                try {
                    find_upcoming_list(id, mCurrentPage,done);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }

    }


    @Override
    public void initDta() {
        mCurrentPage = 1;
      //  status=null;
        try {
            find_upcoming_list(id, mCurrentPage,done);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btn_add:
//
//                break;
            default:
                break;
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

    private void setEnd() {
        //没数据了
        isEnd = true;
        ILoadingLayout endLabels = mListView.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("—我是有底线的—");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("—我是有底线的—");// 刷新时
        endLabels.setReleaseLabel("—我是有底线的—");// 下来达到一定距离时，显示的提示
        endLabels.setLoadingDrawable(null);
        //  mListView.setMode(PullToRefreshBase.Mode.DISABLED);
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
                find_upcoming_list(id, mCurrentPage,done);
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
                    find_upcoming_list(id, mCurrentPage, done);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //   myListAatapter.notifyDataSetChanged();
            mListView.onRefreshComplete();
            myListAatapter.notifyDataSetChanged();
            if (isEnd) {//没数据了
                mListView.onRefreshComplete();
            }
        }
    }


    class StrBean {
        public String page;
        //public String auth = "1";
        public String member_id;
        public String status;
    }


    /**
     * 查询待办
     *
     * @param pageCount
     * @throws JSONException
     */
    public void find_upcoming_list(final String id, final int pageCount, final String done) throws JSONException {

        StrBean strBean = new StrBean();
        strBean.page = pageCount - 1 + "";
        strBean.member_id = id;
        if (!TextUtils.isEmpty(done)) {
            strBean.status = done;
        }
        String s = gson.toJson(strBean);

        JSONObject jsonObject = new JSONObject(s.toString());
        LogUtil.i(s.toString());
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Constants.FIND_UPCOMING_MATTER, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtil.i(jsonObject.toString());
                RequsetUpcomingMaterListBean potentialListBean = new RequsetUpcomingMaterListBean();
                potentialListBean = gson.fromJson(jsonObject.toString(), RequsetUpcomingMaterListBean.class);

                myListAatapter.notifyDataSetChanged();


                if (potentialListBean.getSuccess()) {
                    if (potentialListBean.getData().getLast()) {
                        //    mCurrentPage = mCurrentPage + 1;
                        isEnd = true;
                        setEnd();
                    } else {
                        //  datalist.addAll( orderinfo.getData().getContent());
                        //  myListAatapter.notifyDataSetChanged();
                        isEnd = false;
                        init_pullToRefresh();
                    }

                    if (mCurrentPage == 1) {
                        datalist = potentialListBean.getData().getContent();
                        myListAatapter.notifyDataSetChanged();
                        mCurrentPage = mCurrentPage + 1;
                    } else {
                        datalist.addAll(potentialListBean.getData().getContent());
                        myListAatapter.notifyDataSetChanged();
                        mCurrentPage = mCurrentPage + 1;
                    }

                } else {
                    ToastUtils.showToastLong(potentialListBean.getErrorMsg());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                //ToastUtils.showToastShort(volleyError.toString());
                imageView.setImageResource(R.drawable.img_error7);
                tv_error.setText("网络异常");
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


    private void change_done(RequsetUpcomingMaterListBean.Data.Content s) {


        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(gson.toJson(s).toString());
            LogUtil.i(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.PUT, Constants.UPDATE_MATTER_STATUS, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtil.i(jsonObject.toString());
                RequsetSimpleBean requsetSimpleBean = new RequsetSimpleBean();
                requsetSimpleBean = gson.fromJson(jsonObject.toString(), RequsetSimpleBean.class);
                if (requsetSimpleBean.isSuccess()) {
                    myListAatapter.notifyDataSetChanged();
                    EventBus.getDefault().post(new IntEvent(0, 151));
                } else {

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


    class MyListAatapter extends BaseAdapter {

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

                convertView = View.inflate(mActivity, R.layout.listview_item_upcoming_master_list, null);


                vh.tv_content = convertView.findViewById(R.id.tv_content);

                vh.tv_name = convertView.findViewById(tv_name);

                vh.tv_type = convertView.findViewById(R.id.tv_type);
                vh.tv_upcoming_name = convertView.findViewById(R.id.tv_upcoming_name);
                vh.tv_upcoming_time = convertView.findViewById(R.id.tv_upcoming_time);

                vh.ll_item = convertView.findViewById(R.id.ll_item);
                vh.tv_lasttime = convertView.findViewById(tv_lasttime);
                vh.iv_done = convertView.findViewById(R.id.iv_done);
                vh.tv_result = convertView.findViewById(tv_result);

                convertView.setTag(vh);

            } else {

                vh = (ViewHolder) convertView.getTag();

            }
            vh.tv_name.setText(datalist.get(position).getEmployee_name());
            vh.tv_type.setText(datalist.get(position).getWork_type_name());
            vh.tv_content.setText(datalist.get(position).getContent());
            vh.tv_lasttime.setText(datalist.get(position).getRecord_time());
            vh.tv_upcoming_name.setText(datalist.get(position).getMember_name());
            vh.tv_upcoming_time.setText(datalist.get(position).getWarn_time());
            if (TextUtils.equals(datalist.get(position).getStatus(), "1")) {
                vh.iv_done.setImageResource(R.drawable.img_isdone_off);
            } else {
                vh.iv_done.setImageResource(R.drawable.img_isdone_up);
            }
            vh.tv_result.setText(datalist.get(position).getResult());
//            vh.iv_done.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (!TextUtils.equals(datalist.get(position).getStatus(), "1")) {//未完成
//
//                    }
//                }
//            });
            vh.ll_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.equals(datalist.get(position).getStatus(), "0")) {
                        showDialog1(position);
                    }

                }
            });
            return convertView;

        }


        class ViewHolder {
            public TextView tv_content;
            public TextView tv_type;
            public TextView tv_name;
            public TextView tv_upcoming_name;
            public TextView tv_upcoming_time;
            public TextView tv_lasttime;
            public TextView tv_result;
            public LinearLayout ll_item;
            public ImageView iv_done;
        }

    }

    private void showDialog1(final int pos) {
        final String[] items = {"标记为已完成", "填写待办结果"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(mActivity);
        //listDialog.setTitle("我是一个列表Dialog");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case 0:
                        //     RequsetUpcomingMaterListBean.Data.Content content = new RequsetUpcomingMaterListBean().getData().Content;
                        datalist.get(pos).setStatus("1");
                        datalist.get(pos).setResult("");
                        change_done(datalist.get(pos));
                        break;
                    case 1:
                        showDialog2(pos);

                        break;
                    default:
                        break;
                }
            }
        });
        listDialog.show();
    }

    private void showDialog2(final int pos) {

        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(mActivity);
        final View dialogView = LayoutInflater.from(mActivity)
                .inflate(R.layout.dialog_customize_no_emoji, null);

        customizeDialog.setTitle("填写待办结果");
        customizeDialog.setView(dialogView);
        final ContainsEmojiEditText edit_text = dialogView.findViewById(R.id.edit_text);
     //   edit_text.setText(datalist.get(pos).getContent());
        customizeDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 获取EditView中的输入内容

                        if (TextUtils.isEmpty(edit_text.getText().toString().trim())) {
                            ToastUtils.showToastShort("请填写内容");
                            return;
                        }
                        datalist.get(pos).setStatus("1");
                        datalist.get(pos).setResult(edit_text.getText().toString().trim());
                        change_done(datalist.get(pos));

                    }
                });
        customizeDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        customizeDialog.show();
    }

}
