package com.cn.danceland.myapplication.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.adapter.NewsListviewAdapter;
import com.cn.danceland.myapplication.bean.NewsDataBean;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment {
    private PullToRefreshListView pullToRefresh;
    private ProgressDialog dialog;
    private List<NewsDataBean> data = new ArrayList<>();
    private List<String> imagelist = new ArrayList<>();
    private NewsListviewAdapter newsListviewAdapter;
    private ViewPager mViewPager;
    private static final int TOP_NEWS_CHANGE_TIME = 4000;// 顶部新闻切换事件
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int item = mViewPager.getCurrentItem();
            if (item < imagelist.size() - 1) {
                item++;
            } else {// 判断是否到达最后一个
                item = 0;
            }
            // Log.d(TAG, "轮播条:" + item);

            mViewPager.setCurrentItem(item);
            mHandler.sendMessageDelayed(Message.obtain(),
                    TOP_NEWS_CHANGE_TIME);


        }
    };
    private TextView tv_indecater;
    private TextView tv_image_title;

    @Override
    public View initViews() {
        View v = View.inflate(mActivity, R.layout.fragment_home, null);

        pullToRefresh = v.findViewById(R.id.pullToRefresh);

        //       headView = initHeadview();
        dialog = new ProgressDialog(mActivity);
        dialog.setMessage("加载中……");
//        dialog.show();
        //    data = getData();

        newsListviewAdapter = new NewsListviewAdapter(data, mActivity);
        pullToRefresh.setAdapter(newsListviewAdapter);
        //加入头布局
        pullToRefresh.getRefreshableView().addHeaderView(initHeadview());

        //禁止头部出现分割线
        pullToRefresh.getRefreshableView().setHeaderDividersEnabled(false);

        pullToRefresh.getRefreshableView().setOverScrollMode(View.OVER_SCROLL_NEVER);//去掉下拉阴影
        //设置下拉刷新模式both是支持下拉和上拉
        pullToRefresh.setMode(PullToRefreshBase.Mode.BOTH);

        init();

        pullToRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {

                new DownRefresh().execute();


            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {


                new UpRefresh().execute();
            }
        });
        return v;
    }

    private TopNewsAdapter topNewsAdapter;

    private View initHeadview() {
        View headView = View.inflate(mActivity, R.layout.headview_homepage, null);
        mViewPager = headView.findViewById(R.id.vp_images);
        tv_image_title = headView.findViewById(R.id.tv_image_title);
        tv_indecater = headView.findViewById(R.id.tv_indecater);
        LinearLayout ll_image_title_bg=headView.findViewById(R.id.ll_image_title_bg);
        ll_image_title_bg.setBackgroundColor(Color.BLACK);
        ll_image_title_bg.getBackground().setAlpha(80);

        topNewsAdapter = new TopNewsAdapter(mActivity, imagelist);
        mViewPager.setAdapter(topNewsAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tv_indecater.setText((position + 1) + "/" + imagelist.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });

        return headView;
    }


    /**
     * 下拉刷新
     */
    private class DownRefresh extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            //  findSelectionDyn_Down(1);
            init();

            for (int i = 0; i < 10; i++) {
                NewsDataBean newsDataBean = new NewsDataBean();
                newsDataBean.setTitle("新闻下拉" + i);
                newsDataBean.setContent("该吃放了吧");
                newsDataBean.setTime("11-9");
                newsDataBean.setImage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1511937477493&di=a44f237b0eeab978b10bcb43ca3e98e9&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F8c1001e93901213f4a21fac35ee736d12f2e959b.jpg");
                newsDataBean.setUrl("http://money.163.com/17/1129/01/D4CGFLLC002580S6.html");
                data.add(0, newsDataBean);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
            newsListviewAdapter.setData(data);
            newsListviewAdapter.notifyDataSetChanged();
            //   myDynListviewAdater.notifyDataSetChanged();
            pullToRefresh.onRefreshComplete();
        }
    }


    /**
     * 上拉拉刷新
     */
    private class UpRefresh extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
//            if (!isEnd) {//还有数据请求
//                findSelectionDyn_Up(mCurrentPage);
//            }

            try {
                Thread.sleep(1000);

                //       dialog.dismiss();
                for (int i = 0; i < 10; i++) {
                    NewsDataBean newsDataBean = new NewsDataBean();
                    newsDataBean.setTitle("新闻上拉" + i);
                    newsDataBean.setContent("该吃放了吧");
                    newsDataBean.setTime("11-9");
                    newsDataBean.setImage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1511937477493&di=a44f237b0eeab978b10bcb43ca3e98e9&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F8c1001e93901213f4a21fac35ee736d12f2e959b.jpg");
                    newsDataBean.setUrl("http://money.163.com/17/1129/01/D4CGFLLC002580S6.html");
                    data.add(newsDataBean);
                }

            } catch (Exception e) {
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
//            myDynListviewAdater.notifyDataSetChanged();
//            if (isEnd) {//没数据了
//                pullToRefresh.onRefreshComplete();
//            }
            newsListviewAdapter.setData(data);
            newsListviewAdapter.notifyDataSetChanged();

            pullToRefresh.onRefreshComplete();
        }
    }

    @Override
    public void initDta() {
        for (int i = 0; i < 10; i++) {
            NewsDataBean newsDataBean = new NewsDataBean();
            newsDataBean.setTitle("新闻" + i);
            newsDataBean.setContent("该吃放了吧");
            newsDataBean.setTime("11-9");
            newsDataBean.setImage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1511937477493&di=a44f237b0eeab978b10bcb43ca3e98e9&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F8c1001e93901213f4a21fac35ee736d12f2e959b.jpg");
            newsDataBean.setUrl("http://money.163.com/17/1129/01/D4CGFLLC002580S6.html");
            data.add(newsDataBean);
        }
        newsListviewAdapter.notifyDataSetChanged();

        imagelist.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1511937477493&di=a44f237b0eeab978b10bcb43ca3e98e9&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F8c1001e93901213f4a21fac35ee736d12f2e959b.jpg");
        imagelist.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1511956867148&di=76f9d5c92f0035fbc0cdc2cca7bd29ba&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F902397dda144ad349ad41299daa20cf431ad8541.jpg");
        imagelist.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1511956867147&di=9bf28246ada4dde0fa4e263ba6d8aed5&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F203fb80e7bec54e7fd218537b2389b504ec26ace.jpg");
        imagelist.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1511956867147&di=762d313a4230efdf7efc46dbb6e40305&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F574e9258d109b3de9ad6474fc6bf6c81800a4caf.jpg");
        imagelist.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1511956867146&di=9c103cfb0dcd01886029edcb2db8c471&imgtype=0&src=http%3A%2F%2Fpic.qiantucdn.com%2F58pic%2F22%2F72%2F01%2F57c657a7e4258_1024.jpg");
        imagelist.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1511956867146&di=fdf729f81b3cf3042dbf11acb2d9f4cb&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3D1fdc96bad333c895b2739038b97a1985%2Fd043ad4bd11373f092fa8eb2ae0f4bfbfaed04d1.jpg");

        topNewsAdapter.setData(imagelist);
        topNewsAdapter.notifyDataSetChanged();
        mHandler.sendMessageDelayed(Message.obtain(),
                TOP_NEWS_CHANGE_TIME);

    }


    @Override
    public void onClick(View view) {

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


    public class TopNewsAdapter extends PagerAdapter {
        private Context context;
        private List<String> images;

        public TopNewsAdapter(Context context, List<String> images) {
            this.context = context;
            this.images = images;
        }

        public void setData(List<String> images) {
            this.images = images;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {


            return object == view;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView image = new ImageView(context);
            image.setScaleType(ImageView.ScaleType.FIT_XY);// 设置图片展现样式为:
            // 宽高填充ImageView(图片可能被拉伸或者缩放)
            //   image.setImageResource(R.drawable.topnews_item_default);
            container.addView(image);


            Glide.with(context).load(images.get(position)).into(image);

            // 参2表示图片url
            image.setOnTouchListener(new TopNewsTouchListener());

            return image;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    class TopNewsTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //
                    mHandler.removeCallbacksAndMessages(null);// 移除消息队列中的所有元素
                    break;
                case MotionEvent.ACTION_CANCEL:// 事件取消(比如按下后开始移动,
                    // 那么就不会响应ACTION_UP动作了)
                    //     Log.d(TAG, "事件取消");
                    mHandler.sendMessageDelayed(Message.obtain(),
                            TOP_NEWS_CHANGE_TIME);
                    break;
                case MotionEvent.ACTION_UP:
                    //     Log.d(TAG, "手指抬起");
                    mHandler.sendMessageDelayed(Message.obtain(),
                            TOP_NEWS_CHANGE_TIME);
                    break;

                default:
                    break;
            }

            return true;
        }
    }

}
