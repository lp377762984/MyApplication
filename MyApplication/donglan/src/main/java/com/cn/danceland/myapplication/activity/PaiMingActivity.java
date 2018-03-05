package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.RequsetUserListBean;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shy on 2018/1/31 11:55
 * Email:644563767@qq.com
 * 等级排名
 */


public class PaiMingActivity extends Activity {

    private PullToRefreshListView pullToRefresh;
    MyUserListviewAdapter myUserListviewAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pai_ming);

        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {
        pullToRefresh = findViewById(R.id.pullToRefresh);
        init();
        //设置下拉刷新模式both是支持下拉和上拉
        pullToRefresh.setMode(PullToRefreshBase.Mode.DISABLED);
        myUserListviewAdapter=new MyUserListviewAdapter(this,null);
        pullToRefresh.setAdapter(myUserListviewAdapter);

        pullToRefresh.getRefreshableView().addHeaderView(initHeadview());
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

    private View initHeadview() {
        View v=View.inflate(this,R.layout.headview_my_paiming,null);
        return v;
    }

    public class MyUserListviewAdapter extends BaseAdapter {
        private List<RequsetUserListBean.Data.Content> data = new ArrayList<RequsetUserListBean.Data.Content>();
        private LayoutInflater mInflater;
        private Context context;


        public MyUserListviewAdapter(Context context, List<RequsetUserListBean.Data.Content> data) {
            mInflater = LayoutInflater.from(context);
            this.data = data;
            this.context = context;

        }

        public void addLastList(ArrayList<RequsetUserListBean.Data.Content> bean) {
            this.data.addAll(bean);
        }


        public void setData(ArrayList<RequsetUserListBean.Data.Content> bean) {

            this.data = bean;
        }


        @Override
        public int getCount() {
           // return data.size();
            return 10;
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {

            ViewHolder viewHolder = null;
            if (view == null) {
                viewHolder = new ViewHolder();
                view = mInflater.inflate(R.layout.listview_item_paiminguser_list, null);
                viewHolder.iv_avatar = view.findViewById(R.id.iv_avatar);
                viewHolder.tv_nickname = view.findViewById(R.id.tv_nickname);
                // viewHolder.iv_sex = view.findViewById(R.id.iv_sex);
                viewHolder.ll_item = view.findViewById(R.id.ll_item);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }


            return view;
        }

        class ViewHolder {
            TextView tv_nickname;//昵称
            ImageView iv_avatar;//头像
            ImageView iv_sex;//性别
            LinearLayout ll_item;
        }
    }
}
