package com.cn.danceland.myapplication.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.activity.NewsDetailsActivity;
import com.cn.danceland.myapplication.adapter.NewsListviewAdapter;
import com.cn.danceland.myapplication.bean.RequestImageNewsDataBean;
import com.cn.danceland.myapplication.bean.RequestNewsDataBean;
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


public class HomeFragment extends BaseFragment {
    private PullToRefreshListView pullToRefresh;
    private ProgressDialog dialog;
    private List<RequestNewsDataBean.Data.Items> data = new ArrayList<>();
    private List<RequestImageNewsDataBean.Data> imagelist = new ArrayList<>();
    private NewsListviewAdapter newsListviewAdapter;
    private ViewPager mViewPager;
    private int mCurrentPage = 1;//起始请求页
    private int mCurrentIamgenews = 1;//轮播开始页
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

    private Handler mHandler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:
                    //加入头布局
                    pullToRefresh.getRefreshableView().addHeaderView(initHeadview());
                    topNewsAdapter.setData(imagelist);
                    topNewsAdapter.notifyDataSetChanged();
                    tv_indecater.setText((1) + "/" + imagelist.size());
                    tv_image_title.setText(imagelist.get(1).getTitle());
                    mHandler.sendMessageDelayed(Message.obtain(),
                            TOP_NEWS_CHANGE_TIME);
                    break;
                default:
                    break;
            }


        }
    };

    private TextView tv_indecater;
    private TextView tv_image_title;

    @Override
    public View initViews() {
        View v = View.inflate(mActivity, R.layout.fragment_home, null);

        pullToRefresh = v.findViewById(R.id.pullToRefresh1);
        init();
        //       headView = initHeadview();
        dialog = new ProgressDialog(mActivity);
        dialog.setMessage("加载中……");

        if (newsListviewAdapter == null) {
            newsListviewAdapter = new NewsListviewAdapter(data, mActivity);
        }


        //禁止头部出现分割线
        //   pullToRefresh.getRefreshableView().setHeaderDividersEnabled(false);

        pullToRefresh.getRefreshableView().setOverScrollMode(View.OVER_SCROLL_NEVER);//去掉下拉阴影
        //设置下拉刷新模式both是支持下拉和上拉
        pullToRefresh.setMode(PullToRefreshBase.Mode.BOTH);


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
        // pullToRefresh.setVisibility(View.GONE);
        pullToRefresh.setAdapter(newsListviewAdapter);
        return v;
    }

    private TopNewsAdapter topNewsAdapter;

    private View initHeadview() {
        View headView = View.inflate(mActivity, R.layout.headview_homepage, null);
        RelativeLayout lv_home_img_news=headView.findViewById(R.id.lv_home_img_news);
        DisplayMetrics dm =getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        // 1、设置固定大小
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(w_screen, w_screen*9/16);
//        // 设置包裹内容或者填充父窗体大小
//        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        //设置padding值
//        textView.setPadding(10, 10, 10, 10);
//        //设置margin值
//        lp.setMargins(20, 20, 0, 20);
  //      view.addView(textView,lp);

        mViewPager = headView.findViewById(R.id.vp_images);
        lv_home_img_news.setLayoutParams(lp);
        tv_image_title = headView.findViewById(R.id.tv_image_title);
        tv_indecater = headView.findViewById(R.id.tv_indecater);
        LinearLayout ll_image_title_bg = headView.findViewById(R.id.ll_image_title_bg);
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
                tv_image_title.setText(imagelist.get(position).getTitle());
                mCurrentIamgenews = position;
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
            mCurrentPage = 1;
            findNews(mCurrentPage);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
            newsListviewAdapter.notifyDataSetChanged();
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
                findNews(mCurrentPage);
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
            // newsListviewAdapter.notifyDataSetChanged();
            if (isEnd) {//没数据了
                pullToRefresh.onRefreshComplete();
            }

            //    pullToRefresh.onRefreshComplete();
        }
    }

    @Override
    public void initDta() {
        dialog.show();
        findNews(mCurrentPage);
        findImageNews();

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
        private List<RequestImageNewsDataBean.Data> images;

        public TopNewsAdapter(Context context, List<RequestImageNewsDataBean.Data> images) {
            this.context = context;
            this.images = images;
        }

        public void setData(List<RequestImageNewsDataBean.Data> images) {
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


            Glide.with(context).load(images.get(position).getImg_url()).into(image);

            // 参2表示图片url
            image.setOnTouchListener(new TopNewsTouchListener());

            return image;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    private long starttime = 0;
    private long endtime = 0;
    float xDown, yDown, xUp;

    class TopNewsTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {


            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                xDown = event.getX();
                yDown = event.getY();

                mHandler.removeCallbacksAndMessages(null);// 移除消息队列中的所有元素

                starttime = event.getEventTime();
            } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                // 那么就不会响应ACTION_UP动作了)
                //     Log.d(TAG, "事件取消");
                mHandler.sendMessageDelayed(Message.obtain(),
                        TOP_NEWS_CHANGE_TIME);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                xUp = event.getX();
                mHandler.sendMessageDelayed(Message.obtain(),
                        TOP_NEWS_CHANGE_TIME);
                endtime = event.getEventTime();


                if ((xUp - xDown) > 20) {
                    //添加要处理的内容
                } else if ((xUp - xDown) < -20) {
                    //添加要处理的内容
                } else if (0 == (xDown - xUp)) {

             //       LogUtil.i("点击了图片");
                    mActivity.startActivity(new Intent(mActivity, NewsDetailsActivity.class).putExtra("url", imagelist.get(mCurrentIamgenews).getUrl()).putExtra("title", imagelist.get(mCurrentIamgenews).getTitle()));

//                    int viewWidth = v.getWidth();
//                    if( xDown < viewWidth/3 )
//                    {
//                        //靠左点击
//                    }
//                    else if(xDown > viewWidth/3 && xDown < viewWidth * 2 /3)
//                    {
//                        //中间点击
//                    }
//                    else
//                    {
//                        //靠右点击
//                    }
                }


            }
            // LogUtil.i((endtime - starttime) + "");


            return true;
        }
    }

    private boolean isEnd = false;

    private void setEnd() {
        isEnd = true;//没数据了
        ILoadingLayout endLabels = pullToRefresh.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("—我是有底线的—");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("—我是有底线的—");// 刷新时
        endLabels.setReleaseLabel("—我是有底线的—");// 下来达到一定距离时，显示的提示
        endLabels.setLoadingDrawable(null);
        // pullToRefresh.setMode(PullToRefreshBase.Mode.DISABLED);
    }

    private void findNews(int page) {
        String params = page + "";

        String url = Constants.FIND_NEWS_URL + params;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String s) {
                dialog.dismiss();
                //   pullToRefresh.onRefreshComplete();
                LogUtil.i(s);
                Gson gson = new Gson();
                RequestNewsDataBean newsDataBean = gson.fromJson(s, RequestNewsDataBean.class);
                if (newsDataBean.getSuccess()) {
                    data = newsDataBean.getData().getItems();


                    //    LogUtil.i(data.toString());
                    if (mCurrentPage == 1) {
                        newsListviewAdapter.setData(data);

                        newsListviewAdapter.notifyDataSetChanged();
                        //   pullToRefresh.setVisibility(View.VISIBLE);

                    } else {
                        LogUtil.i("addLastList");
                        newsListviewAdapter.addLastList(data);
                        newsListviewAdapter.notifyDataSetChanged();
                    }
                    if (data.size() > 0 && data.size() < 10) {
                        setEnd();
                    } else {
                        mCurrentPage = mCurrentPage + 1;

                    }

                }


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
        request.setTag("findNews");
        // 设置超时时间
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);

    }


    private void findImageNews() {


        StringRequest request = new StringRequest(Request.Method.GET, Constants.FIND_IMAGE_NEWS_URL, new Response.Listener<String>() {


            @Override
            public void onResponse(String s) {

                LogUtil.i(s);
                Gson gson = new Gson();

                RequestImageNewsDataBean imageNewsDataBean = new RequestImageNewsDataBean();
                imageNewsDataBean = gson.fromJson(s, RequestImageNewsDataBean.class);
                if (imageNewsDataBean.getSuccess()) {

                    if (imageNewsDataBean.getData().size() > 0) {
                        imagelist = imageNewsDataBean.getData();

                        Message message = Message.obtain();
                        message.what = 1;
                        mHandler2.sendMessage(message);

                    } else {
                        ToastUtils.showToastShort("轮播图片为空");
                    }

                } else {

                    ToastUtils.showToastShort(imageNewsDataBean.getErrorMsg());
                }


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
        request.setTag("findImageNews");
        // 设置超时时间
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);

    }


}
