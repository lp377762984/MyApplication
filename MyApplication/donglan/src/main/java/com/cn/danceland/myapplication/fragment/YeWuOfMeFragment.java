package com.cn.danceland.myapplication.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
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
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.RequestMyYeWuBean;
import com.cn.danceland.myapplication.evntbus.IntEvent;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.MyStringRequest;
import com.cn.danceland.myapplication.utils.TimeUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.cn.danceland.myapplication.R.id.tv_lasttime;
import static com.cn.danceland.myapplication.R.id.tv_name;
import static com.cn.danceland.myapplication.utils.Constants.YEWU_URL;

/**
 * Created by shy on 2018/1/11 17:18
 * Email:644563767@qq.com
 */


public class YeWuOfMeFragment extends BaseFragment {

    private PullToRefreshListView mListView;
    private List<RequestMyYeWuBean.Data.Content> datalist = new ArrayList<>();

    private MyListAatapter myListAatapter;

    private Gson gson = new Gson();
    private int mCurrentPage = 1;//起始请求页
    private boolean isEnd = false;
    private String id;
    private String yewu_type = "";//1是定金业务，2是储值业务，3是卡业务，4是租柜业务，5是私教业务
    private String auth = "1";
    private TextView tv_error;
    private ImageView imageView;
    Map<Integer,String> yewumap=new HashMap<>();
    private Data data;

    @Override
    public View initViews() {
        View v = View.inflate(mActivity, R.layout.fragment_upcoming_matter_list, null);
//        v.findViewById(R.id.btn_add).setOnClickListener(this);
        mListView = v.findViewById(R.id.pullToRefresh);
        View listEmptyView = v.findViewById(R.id.rl_no_info);
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

                TimerTask task = new TimerTask() {
                    public void run() {
                        new DownRefresh().execute();
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 1000);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

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
        id = getArguments().getString("id");
        auth = getArguments().getString("auth");
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

            case 171://刷新页面
                mCurrentPage = 1;
                try {
                    find_upcoming_list(id, mCurrentPage, yewu_type);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 172://

                mCurrentPage = 1;
                yewu_type = "1";
                try {
                    find_upcoming_list(id, mCurrentPage, yewu_type);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 173://
                mCurrentPage = 1;
                yewu_type = "2";
                try {
                    find_upcoming_list(id, mCurrentPage, yewu_type);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 174://
                mCurrentPage = 1;
                yewu_type = "3";
                try {
                    find_upcoming_list(id, mCurrentPage, yewu_type);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 175://
                mCurrentPage = 1;
                yewu_type = "4";
                try {
                    find_upcoming_list(id, mCurrentPage, yewu_type);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 176://
                mCurrentPage = 1;
                yewu_type = "5";
                try {
                    find_upcoming_list(id, mCurrentPage, yewu_type);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 177://全部
                mCurrentPage = 1;
                yewu_type = "";
                try {
                    find_upcoming_list(id, mCurrentPage, yewu_type);
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

        yewumap.put(11,"买定金");
        yewumap.put(12,"用定金");
        yewumap.put(13,"退定金");
        yewumap.put(14,"充储值");
        yewumap.put(15,"退储值");
        yewumap.put(100,"花储值");
        yewumap.put(21,"买卡");
        yewumap.put(22,"卡升级");
        yewumap.put(23,"续卡");
        yewumap.put(24,"补卡");
        yewumap.put(25,"转卡");
        yewumap.put(26,"退卡");
        yewumap.put(27,"停卡");
        yewumap.put(28,"卡延期");
        yewumap.put(29,"卡挂失");
        yewumap.put(30,"卡加次");
        yewumap.put(41,"租柜");
        yewumap.put(42,"续柜");
        yewumap.put(43,"退柜");
        yewumap.put(44,"转柜");
        yewumap.put(45,"换柜");
        yewumap.put(51,"购买私教");
        yewumap.put(52,"私教转会员");
        yewumap.put(53,"私教换教练");
        yewumap.put(61,"退私教");

        yewumap.put(1,"定金业务");
        yewumap.put(2,"储值业务");
        yewumap.put(3,"卡业务");
        yewumap.put(4,"租柜业务");
        yewumap.put(5,"私教业务");

        data = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);

        mCurrentPage = 1;
        //  status=null;
        try {
            find_upcoming_list(id, mCurrentPage, yewu_type);
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
                find_upcoming_list(id, mCurrentPage, yewu_type);
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
                    find_upcoming_list(id, mCurrentPage, yewu_type);
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




    /**
     * 查询待办
     *
     * @param pageCount
     * @throws JSONException
     */
    public void find_upcoming_list(final String id, final int pageCount, final String big_type) throws JSONException {

//        StrBean strBean = new StrBean();
//        strBean.page = pageCount - 1 + "";
//        strBean.member_id = id;
//        if (!TextUtils.isEmpty(done)) {
//            strBean.status = done;
//        }
//        String s = gson.toJson(strBean);
//
//        JSONObject jsonObject = new JSONObject(s.toString());
//        LogUtil.i(s.toString());

        MyStringRequest stringRequest = new MyStringRequest(Request.Method.POST, YEWU_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                RequestMyYeWuBean potentialListBean = new RequestMyYeWuBean();
                potentialListBean = gson.fromJson(s, RequestMyYeWuBean.class);

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

                LogUtil.e(volleyError.toString());
            }
        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("member_id", id);
                map.put("page", pageCount - 1 + "");
                map.put("size", 10 + "");
                map.put("big_type", big_type);
                map.put("operater_id", data.getEmployee().getId()+"");

                LogUtil.i(map.toString());

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

                convertView = View.inflate(mActivity, R.layout.listview_item_my_yewu, null);


                //          vh.tv_content = convertView.findViewById(R.id.tv_content);

                vh.tv_name = convertView.findViewById(tv_name);

                vh.tv_type = convertView.findViewById(R.id.tv_type);
//                vh.tv_upcoming_name = convertView.findViewById(R.id.tv_upcoming_name);
//                vh.tv_upcoming_time = convertView.findViewById(R.id.tv_upcoming_time);

                vh.ll_item = convertView.findViewById(R.id.ll_item);
                vh.tv_lasttime = convertView.findViewById(tv_lasttime);
                vh.tv_money = convertView.findViewById(R.id.tv_money);
                //   vh.tv_result = convertView.findViewById(tv_result);

                convertView.setTag(vh);

            } else {

                vh = (ViewHolder) convertView.getTag();

            }


//
//            {"key": 11, "value": "买定金","big_type":1},
//            {"key": 12, "value": "用定金","big_type":1},
//            {"key": 13, "value": "退定金","big_type":1},
//
//            {"key": 14, "value": "充储值","big_type":2},
//            {"key": 15, "value": "退储值","big_type":2},
//            {"key": 100, "value": "花储值","big_type":2},
//
//            {"key": 21, "value": "买卡","big_type":3},
//            {"key": 22, "value": "卡升级","big_type":3},
//            {"key": 23, "value": "续卡","big_type":3},
//            {"key": 24, "value": "补卡","big_type":3},
//            {"key": 25, "value": "转卡","big_type":3},
//            {"key": 26, "value": "退卡","big_type":3},
//            {"key": 27, "value": "停卡","big_type":3},
//            {"key": 28, "value": "卡延期","big_type":3},
//            {"key": 29, "value": "卡挂失","big_type":3},
//            {"key": 30, "value": "卡加次","big_type":3},
//
//            {"key": 41, "value": "租柜","big_type":4},
//            {"key": 42, "value": "续柜","big_type":4},
//            {"key": 43, "value": "退柜","big_type":4},
//            {"key": 44, "value": "转柜","big_type":4},
//            {"key": 45, "value": "换柜","big_type":4},
//
//            {"key": 51, "value": "购买私教","big_type":5},
//            {"key": 52, "value": "私教转会员","big_type":5},
//            {"key": 53, "value": "私教换教练","big_type":5},
//            {"key": 61, "value": "退私教","big_type":5}];

            vh.tv_name.setText(datalist.get(position).getOperater_name());

         vh.tv_type.setText(yewumap.get(datalist.get(position).getBig_type())+"、"+yewumap.get(datalist.get(position).getType()));
            vh.tv_money.setText("¥"+datalist.get(position).getMoney());
            vh.tv_lasttime.setText(TimeUtils.timeStamp2Date(datalist.get(position).getDeal_time()+"","yyyy-MM-dd HH:mm"));
//
//
//            vh.tv_result.setText(datalist.get(position).getResult());
////            vh.iv_done.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View view) {
////                    if (!TextUtils.equals(datalist.get(position).getStatus(), "1")) {//未完成
////
////                    }
////                }
////            });
//            vh.ll_item.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (TextUtils.equals(datalist.get(position).getStatus(), "0")) {
//                        showDialog1(position);
//                    }
//
//                }
//            });
            return convertView;

        }


        class ViewHolder {
            public TextView tv_money;
            public TextView tv_type;
            public TextView tv_name;
            public TextView tv_lasttime;
            public TextView tv_result;
            public LinearLayout ll_item;

        }

    }


}