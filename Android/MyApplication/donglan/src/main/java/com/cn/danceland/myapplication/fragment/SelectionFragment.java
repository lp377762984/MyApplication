package com.cn.danceland.myapplication.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.danceland.myapplication.R;
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
    MyAdapter adapter;

    @Override
    public View initViews() {
        View v = View.inflate(mActivity, R.layout.fragment_selection, null);
        pullToRefresh = v.findViewById(R.id.pullToRefresh);

        data = getData();
        adapter = new MyAdapter(mActivity);
        pullToRefresh.setAdapter(adapter);
        //设置下拉刷新模式both是支持下拉和上拉
        pullToRefresh.setMode(PullToRefreshBase.Mode.BOTH);

        init();

        pullToRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // TODO Auto-generated method stub
                PullBean bean = new PullBean();
                bean.setTitle("派大星");
                bean.setContent("最近发布");
                adapter.addFirst(bean);
                new FinishRefresh().execute();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // TODO Auto-generated method stub
//                PullBean bean = new PullBean();
//                bean.setTitle("派大星");
//                bean.setContent("最近发布");
//                adapter.addLast(bean);
                List<PullBean> list = new ArrayList<PullBean>();
                for (int i = 0; i < 10; i++) {
                    PullBean bean = new PullBean();
                    bean.setTitle("派大星 " + i);
                    bean.setContent("最近更新");
                    list.add(bean);
                }
                adapter.addLastList((ArrayList<PullBean>) list);
                new FinishRefresh().execute();
                adapter.notifyDataSetChanged();

            }
        });

        return v;
    }

    @Override
    public void initDta() {
        super.initDta();
    }

    private List<PullBean> getData() {
        List<PullBean> list = new ArrayList<PullBean>();
        for (int i = 0; i < 10; i++) {
            PullBean bean = new PullBean();
            bean.setTitle("派大星 " + i);
            bean.setContent("最近更新");
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
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
//          adapter.notifyDataSetChanged();
            pullToRefresh.onRefreshComplete();
        }
    }

    // 设置下拉刷新文本
    private void init() {
        ILoadingLayout startLabels = pullToRefresh
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在加载...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        ILoadingLayout endLabels = pullToRefresh.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("上拉加载...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在加载...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

//      // 设置下拉刷新文本
//      pullToRefresh.getLoadingLayoutProxy(false, true)
//              .setPullLabel("上拉刷新...");
//      pullToRefresh.getLoadingLayoutProxy(false, true).setReleaseLabel(
//              "放开刷新...");
//      pullToRefresh.getLoadingLayoutProxy(false, true).setRefreshingLabel(
//              "正在加载...");
//      // 设置上拉刷新文本
//      pullToRefresh.getLoadingLayoutProxy(true, false)
//              .setPullLabel("下拉刷新...");
//      pullToRefresh.getLoadingLayoutProxy(true, false).setReleaseLabel(
//              "放开刷新...");
//      pullToRefresh.getLoadingLayoutProxy(true, false).setRefreshingLabel(
//              "正在加载...");
    }


    private class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public MyAdapter(Context context) {
            // TODO Auto-generated constructor stub
            mInflater = LayoutInflater.from(context);
        }

        public void addFirst(PullBean bean) {
            data.add(0, bean);
        }

        public void addFirstList(ArrayList<PullBean> bean) {
            data.addAll(0, bean);
        }

        public void addLast(PullBean bean) {
            data.add(bean);
        }
        public void addLastList(ArrayList<PullBean> bean) {
            data.addAll(bean);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.listview_item_dynamic, null);
                viewHolder.tv_pick_name = (TextView) convertView.findViewById(R.id.tv_pick_name);
                viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.tv_pick_name.setText(data.get(position).getTitle());
            viewHolder.tv_time.setText(data.get(position).getContent());

            return convertView;
        }

        class ViewHolder {
            TextView tv_pick_name;
            TextView tv_time;
        }
    }
}
