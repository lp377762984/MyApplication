package com.cn.danceland.myapplication.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.activity.CollectEntranceActivity;
import com.cn.danceland.myapplication.activity.NewsDetailsActivity;
import com.cn.danceland.myapplication.activity.PaiMingActivity;
import com.cn.danceland.myapplication.activity.UserHomeActivity;
import com.cn.danceland.myapplication.adapter.NewsListviewAdapter;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.RequestImageNewsDataBean;
import com.cn.danceland.myapplication.bean.RequestNewsDataBean;
import com.cn.danceland.myapplication.bean.RequsetMyPaiMingBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.DensityUtils;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.MyStringRequest;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.cn.danceland.myapplication.utils.UIUtils;
import com.cn.danceland.myapplication.view.NumberAnimTextView;
import com.cn.danceland.myapplication.view.RoundImageView;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.api.ScrollBoundaryDecider;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yxx on 2018-10-18.
 */

public class HomeFragment extends BaseFragment {
    private static final int MSG_REFRESH_VIEW = 0;//刷新界面
    private static final int MSG_REFRESH_DATA = 0x100;//暂无数据
    private SmartRefreshLayout refreshLayout;
    private RecyclerView mRecycler;
    private ProgressDialog dialog;
    private List<RequestNewsDataBean.Data.Items> data = new ArrayList<>();
    private List<RequestImageNewsDataBean.Data> imagelist = new ArrayList<>();
    private NewsListviewAdapter newsListviewAdapter;
    private ViewPager mViewPager;
    private int mCurrentPage = 0;//起始请求页
    private int mCurrentIamgenews = 1;//轮播开始页
    private static final int TOP_NEWS_CHANGE_TIME = 4000;// 顶部新闻切换事件


    private static LinearLayout header_layout;//header布局 头像 姓名 俩按钮
    private CardView meun_cradview;//菜单 粉色整体布局 健身日记 打卡排行layout
    private TextView in_the_cumulative_tv;//进场累计
    private NumberAnimTextView cumulative_num_tv;//进场累计数
    private ImageView fitness_diary_white_iv;//健身日记 header布局 白色按钮
    private ImageView punch_list_white_iv;//打卡排行 header布局 白色按钮
    private ImageView fitness_diary_pink_iv;//打卡排行 header布局 粉色按钮
    private ImageView punch_list_pink_iv;//健身日记 header布局 粉色按钮
    private LinearLayout fitness_diary_pink_ll;//健身日记 菜单 粉色布局
    private LinearLayout punch_list_pink_ll;//打卡排行 菜单 粉色布局
    private ImageView header_background_iv;//打卡排行 菜单 粉色布局
    private ImageView iv_avatar;
    private TextView tv_nick_name;
    private View banner_header2;

    private int recLen = 99;//进场累计  99-0 0-x
    private int cumulative_num = 0;//进场累计  99-0 0-x
    private boolean isCumulative = false;//进场累计 0-x开关
    private Data mInfo;//登录对象

    private int alphaNum = 0;//透明度
    int i = 255;

    Handler handler3 = new Handler();

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
                    // 设置数据
                    mMZBanner.setPages(imagelist, new MZHolderCreator<BannerViewHolder>() {
                        @Override
                        public BannerViewHolder createViewHolder() {
                            return new BannerViewHolder();
                        }
                    });
                    mMZBanner.start();
                    break;
                case MSG_REFRESH_DATA:
                    ToastUtils.showToastShort("暂无更多数据");
                    break;
                default:
                    break;
            }
        }
    };

    private MZBannerView mMZBanner;

//    private static CollapsingToolbarLayoutState state;
//    AppBarLayout appBarLayout;
//    LinearLayout toolbarone;
//    CollapsingToolbarLayout collapsingToolbarLayout;
//
//
//    private enum CollapsingToolbarLayoutState {
//        EXPANDED,
//        COLLAPSED,
//        INTERNEDIATE
//    }
//
//    private int alphaNum = 0;//透明度
//    int i = 255;

    @Override
    public View initViews() {
        LogUtil.i(Constants.HOST);
        View v = View.inflate(mActivity, R.layout.fragment_home_header_view_haoshi, null);

        refreshLayout = v.findViewById(R.id.refreshLayout);
        mRecycler = (RecyclerView) v.findViewById(R.id.recycler);

        meun_cradview = v.findViewById(R.id.meun_cradview);
        header_layout = v.findViewById(R.id.header_layout);
        in_the_cumulative_tv = v.findViewById(R.id.in_the_cumulative_tv);
        cumulative_num_tv = v.findViewById(R.id.cumulative_num_tv);
        fitness_diary_white_iv = v.findViewById(R.id.fitness_diary_white_iv);
        punch_list_white_iv = v.findViewById(R.id.punch_list_white_iv);
        fitness_diary_pink_iv = v.findViewById(R.id.fitness_diary_pink_iv);
        punch_list_pink_iv = v.findViewById(R.id.punch_list_pink_iv);
        fitness_diary_pink_ll = v.findViewById(R.id.fitness_diary_pink_ll);
        punch_list_pink_ll = v.findViewById(R.id.punch_list_pink_ll);
        iv_avatar = v.findViewById(R.id.iv_avatar);
        tv_nick_name = v.findViewById(R.id.tv_nick_name);
        header_background_iv = v.findViewById(R.id.header_background_iv);

        banner_header2 = v.findViewById(R.id.banner_header2);

        mInfo = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
        //设置头像
        RequestOptions options = new RequestOptions().placeholder(R.drawable.img_my_avatar);

        Glide.with(mActivity).load(mInfo.getPerson().getSelf_avatar_path()).apply(options).into(iv_avatar);
        tv_nick_name.setText("Hello " + mInfo.getPerson().getNick_name());

        fitness_diary_white_iv.setOnClickListener(onClickListener);
        punch_list_white_iv.setOnClickListener(onClickListener);
        fitness_diary_pink_ll.setOnClickListener(onClickListener);
        punch_list_pink_ll.setOnClickListener(onClickListener);
        dialog = new ProgressDialog(mActivity);
        dialog.setMessage("加载中……");

        if (newsListviewAdapter == null) {
            newsListviewAdapter = new NewsListviewAdapter(data, mActivity);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(layoutManager);
        header_background_iv = (ImageView) UIUtils.setViewRatio(mActivity, header_background_iv, (float) 187.5, 118);

        newsListviewAdapter.setHeaderView(initBanner());
        newsListviewAdapter.setOnItemClickListener(new NewsListviewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, List<RequestNewsDataBean.Data.Items> data) {
                setReadNum(data.get(position).getId());
                mActivity.startActivity(new Intent(mActivity, NewsDetailsActivity.class).putExtra("url", data.get(position).getUrl()).putExtra("title", data.get(position).getTitle()));
            }
        });
        mRecycler.setAdapter(newsListviewAdapter);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        ///////////////////////////////
//        Window window = getActivity().getWindow();
//        //取消状态栏透明
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        //添加Flag把状态栏设为可绘制模式
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        //设置状态栏颜色
//        window.setStatusBarColor(getResources().getColor(R.color.transparent));
//        //设置系统状态栏处于可见状态
//        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
//        //让view不根据系统窗口来调整自己的布局
//        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
//        View mChildView = mContentView.getChildAt(0);
//        if (mChildView != null) {
//            ViewCompat.setFitsSystemWindows(mChildView, false);
//            ViewCompat.requestApplyInsets(mChildView);
//        }
//        ///////////////////////////////

        dialog = new ProgressDialog(mActivity);
        dialog.setMessage("加载中……");
//设置 Header 为 贝塞尔雷达 样式
        refreshLayout.setPrimaryColorsId(R.color.transparent, android.R.color.transparent);//下拉刷新主题颜色
        refreshLayout.setRefreshHeader(new BezierRadarHeader(mActivity).setEnableHorizontalDrag(true));//设置Header
        refreshLayout.setEnableLoadMoreWhenContentNotFull(false);//取消内容不满一页时开启上拉加载功能
        refreshLayout.setEnableAutoLoadMore(false);//是否启用列表惯性滑动到底部时自动加载更多
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                LogUtil.i("onRefresh");
//                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                TimerTask task = new TimerTask() {
                    public void run() {
                        new DownRefresh().execute();
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 1000);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                LogUtil.i("onLoadMore");
//                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
                TimerTask task = new TimerTask() {
                    public void run() {
                        new UpRefresh().execute();
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 1000);
            }
        });

//        /* Fragment中，注册
//                * 接收MainActivity的Touch回调的对象
//                * 重写其中的onTouchEvent函数，并进行该Fragment的逻辑处理
//                */
//        HomeActivity.MyTouchListener myTouchListener = new HomeActivity.MyTouchListener() {
//            private float mPosX = 0;
//            private float mPosY = 0;
//            private float mCurPosX = 0;
//            private float mCurPosY = 0;
//
//            @Override
//            public void onTouchEvent(MotionEvent event) {
//                // 处理手势事件
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN://当屏幕检测到第一个触点按下之后就会触发到这个事件。
//                        mPosX = event.getX();
//                        mPosY = event.getY();
//                        break;
//                    case MotionEvent.ACTION_MOVE://当触点在屏幕上移动时触发，触点在屏幕上停留也是会触发的，主要是由于它的灵敏度很高，而我们的手指又不可能完全静止（即使我们感觉不到移动，但其实我们的手指也在不停地抖动）。
////                        mCurPosX = event.getX();
////                        mCurPosY = event.getY();
//////                        LogUtil.i("ACTION_MOVE--(" + (mCurPosY - mPosY));
//////                            LogUtil.i("---->" + (mCurPosY - mPosY));
////                        if (refreshLayout.getState()==RefreshState.None) {
////                            setHeader((int) (mCurPosY - mPosY));
////                        }
//                        break;
//                    case MotionEvent.ACTION_UP://当触点松开时被触发。
////                        if (refreshLayout.getState()==RefreshState.None) {
////                            originalTop += (int) (mCurPosY - mPosY);
////                            setHeader(0);
////                        }
//                        break;
//                }
//            }
//        };
        //// 将myTouchListener注册到分发列表
//        ((HomeActivity) this.getActivity()).registerMyTouchListener(myTouchListener);

        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LogUtil.i("X=" + dx + "Y=" + dy);
                offsetNum += (dy);

//                if (offsetNum != 0) {
//                    nullView.setVisibility(View.VISIBLE);
//                    banner_header2.setVisibility(View.GONE);
//                }
                setFoldView();
            }
        });
//        nullView.setVisibility(View.GONE);
//        banner_header2.setVisibility(View.VISIBLE);
//        refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
//            @Override
//            public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {
//                super.onHeaderMoving(header, isDragging, percent, offset, headerHeight, maxDragHeight);
//                LogUtil.i("isDragging---" + isDragging);
//                LogUtil.i("percent--" + percent);
//                LogUtil.i("offsetNum--" + offsetNum);
//                if (isDragging == true) {
//
//                    nullView.setVisibility(View.VISIBLE);
//                    banner_header2.setVisibility(View.GONE);
//                    if (percent != 0.0) {
//                        nullView.setVisibility(View.GONE);
//                        banner_header2.setVisibility(View.VISIBLE);
//                    } else {
//                        nullView.setVisibility(View.VISIBLE);
//                        banner_header2.setVisibility(View.GONE);
//                    }
//                } else {
//
//                    if (percent != 0.0) {
//                        nullView.setVisibility(View.GONE);
//                        banner_header2.setVisibility(View.VISIBLE);
//                    } else {
//                        if (offsetNum != 0) {
//                            nullView.setVisibility(View.VISIBLE);
//                            banner_header2.setVisibility(View.GONE);
//                        } else {
//                            nullView.setVisibility(View.GONE);
//                            banner_header2.setVisibility(View.VISIBLE);
//                        }
//                    }
//                }
//            }

//            @Override
//            public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
//                LogUtil.i("newState---" + newState);
//                if (newState == RefreshState.None) {
//                    nullView.setVisibility(View.GONE);
//                    banner_header2.setVisibility(View.VISIBLE);
//                }else{
//                    nullView.setVisibility(View.GONE);
//                    banner_header2.setVisibility(View.VISIBLE);
//                }
//                super.onStateChanged(refreshLayout, oldState, newState);
//            }
//        });//设置多功能监听器
        return v;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isCumulative) {
                recLen++;
                if (recLen > cumulative_num) {
                    //倒计时结束
                    handler3.removeCallbacks(runnable);
                } else {
                    cumulative_num_tv.setText(recLen + "");
                    handler3.postDelayed(this, 50);
                }
            } else {
                recLen--;
                cumulative_num_tv.setText(recLen + "");
                handler3.postDelayed(this, 50);

                if (recLen <= 0) {
                    //倒计时结束
                    handler3.removeCallbacks(runnable);
                    if (cumulative_num > 0) {//0-x开始
                        isCumulative = true;
                        handler3.postDelayed(runnable, 50);
                    }
                }
            }
        }
    };

    private int offsetNum = 0;//总偏移量

    /**
     * 本Activity透明度刷新有问题，所以如下这么写
     */
    public void setFoldView() {
        RelativeLayout.LayoutParams lppTemp = (RelativeLayout.LayoutParams) header_layout.getLayoutParams();
        int shakeNum = offsetNum / 50;//抖动偏移
        LogUtil.i("shakeNum==" + shakeNum + "------" + offsetNum);


        int listMaxOffset = DensityUtils.dp2px(mActivity, 94f);//listview 最大偏移
        int listOffset = listMaxOffset - offsetNum;
        LogUtil.i("白色" + listOffset + "=============" + listMaxOffset + "-" + offsetNum);

        int headerMarginLeft = DensityUtils.dp2px(mActivity, 16f);//header MarginLeft
        int headerMarginRight = DensityUtils.dp2px(mActivity, 65f);//header MarginRight
        int headerMaxOffset = DensityUtils.dp2px(mActivity, 55f);//header 最大偏移
        int headerOffset = DensityUtils.dp2px(mActivity, 55f) - (offsetNum - listMaxOffset);
        LogUtil.i("头部" + headerOffset + "=============" + headerMaxOffset + "-" + offsetNum + "-" + listMaxOffset);


        if (0 <= offsetNum && offsetNum <= 390) {
//            listviewLayoutParams.setMargins(0, listOffset, 0, 0);
            if (0 <= offsetNum && offsetNum <= 246) {
                setMeunCradview();
            }

            if (listMaxOffset <= offsetNum && offsetNum <= 390) {
                lppTemp.setMargins(headerMarginLeft, headerOffset, headerMarginRight, 0);
                in_the_cumulative_tv.setVisibility(View.GONE);
            } else if (offsetNum == 0) {
                lppTemp.setMargins(headerMarginLeft, headerMaxOffset, headerMarginRight, 0);
                in_the_cumulative_tv.setVisibility(View.VISIBLE);
            }
        } else {
//            listviewLayoutParams.setMargins(0, 0, 0, 0);
//            lppTemp.setMargins(headerMarginLeft, 0, headerMarginRight, 0);

            meun_cradview.setVisibility(View.GONE);//改变日记、排行布局
            fitness_diary_white_iv.setVisibility(View.VISIBLE);
            punch_list_white_iv.setVisibility(View.VISIBLE);
            fitness_diary_white_iv.setImageDrawable(getResources().getDrawable(R.drawable.fitness_diary_white_img));
            punch_list_white_iv.setImageDrawable(getResources().getDrawable(R.drawable.punch_list_white_img));
        }
        header_layout.setLayoutParams(lppTemp);
//        listview_top_layout.setLayoutParams(listviewLayoutParams);
        LogUtil.i("总偏移量-----(" + offsetNum);

    }

    private void setMeunCradview() {
        if (0 <= offsetNum && offsetNum < 25) {
            LogUtil.i("setMeunCradview--0--(" + offsetNum);
            meun_cradview.setVisibility(View.VISIBLE);//改变日记、排行布局
            fitness_diary_white_iv.setVisibility(View.GONE);
            punch_list_white_iv.setVisibility(View.GONE);
            meun_cradview.setBackground(getResources().getDrawable(R.drawable.white_rounded_corners_bg));
            fitness_diary_pink_iv.setImageDrawable(getResources().getDrawable(R.drawable.fitness_diary_pink_img));
            punch_list_pink_iv.setImageDrawable(getResources().getDrawable(R.drawable.punch_list_pink_img));
        } else if (25 <= offsetNum && offsetNum < 50) {
            meun_cradview.setVisibility(View.VISIBLE);//改变日记、排行布局
            fitness_diary_white_iv.setVisibility(View.VISIBLE);
            punch_list_white_iv.setVisibility(View.VISIBLE);
            LogUtil.i("setMeunCradview--1--(" + offsetNum);
            meun_cradview.setBackground(getResources().getDrawable(R.drawable.white_rounded_corners_eight_bg));
            fitness_diary_pink_iv.setImageDrawable(getResources().getDrawable(R.drawable.fitness_diary_pink_eight_img));
            punch_list_pink_iv.setImageDrawable(getResources().getDrawable(R.drawable.punch_list_pink_eight_img));
            fitness_diary_white_iv.setImageDrawable(getResources().getDrawable(R.drawable.fitness_diary_white_two_img));
            punch_list_white_iv.setImageDrawable(getResources().getDrawable(R.drawable.punch_list_white_two_img));
        } else if (50 <= offsetNum && offsetNum < 75) {
            meun_cradview.setVisibility(View.VISIBLE);//改变日记、排行布局
            fitness_diary_white_iv.setVisibility(View.VISIBLE);
            punch_list_white_iv.setVisibility(View.VISIBLE);
            LogUtil.i("setMeunCradview--2--(" + offsetNum);
            meun_cradview.setBackground(getResources().getDrawable(R.drawable.white_rounded_corners_seven_bg));
            fitness_diary_pink_iv.setImageDrawable(getResources().getDrawable(R.drawable.fitness_diary_pink_seven_img));
            punch_list_pink_iv.setImageDrawable(getResources().getDrawable(R.drawable.punch_list_pink_seven_img));
            fitness_diary_white_iv.setImageDrawable(getResources().getDrawable(R.drawable.fitness_diary_white_three_img));
            punch_list_white_iv.setImageDrawable(getResources().getDrawable(R.drawable.punch_list_white_three_img));
        } else if (75 <= offsetNum && offsetNum < 100) {
            meun_cradview.setVisibility(View.VISIBLE);//改变日记、排行布局
            fitness_diary_white_iv.setVisibility(View.VISIBLE);
            punch_list_white_iv.setVisibility(View.VISIBLE);
            LogUtil.i("setMeunCradview--3--(" + offsetNum);
            meun_cradview.setBackground(getResources().getDrawable(R.drawable.white_rounded_corners_six_bg));
            fitness_diary_pink_iv.setImageDrawable(getResources().getDrawable(R.drawable.fitness_diary_pink_six_img));
            punch_list_pink_iv.setImageDrawable(getResources().getDrawable(R.drawable.punch_list_pink_six_img));
            fitness_diary_white_iv.setImageDrawable(getResources().getDrawable(R.drawable.fitness_diary_white_four_img));
            punch_list_white_iv.setImageDrawable(getResources().getDrawable(R.drawable.punch_list_white_four_img));
        } else if (100 <= offsetNum && offsetNum < 125) {
            meun_cradview.setVisibility(View.VISIBLE);//改变日记、排行布局
            fitness_diary_white_iv.setVisibility(View.VISIBLE);
            punch_list_white_iv.setVisibility(View.VISIBLE);
            LogUtil.i("setMeunCradview--4--(" + offsetNum);
            meun_cradview.setBackground(getResources().getDrawable(R.drawable.white_rounded_corners_five_bg));
            fitness_diary_pink_iv.setImageDrawable(getResources().getDrawable(R.drawable.fitness_diary_pink_five_img));
            punch_list_pink_iv.setImageDrawable(getResources().getDrawable(R.drawable.punch_list_pink_five_img));
            fitness_diary_white_iv.setImageDrawable(getResources().getDrawable(R.drawable.fitness_diary_white_five_img));
            punch_list_white_iv.setImageDrawable(getResources().getDrawable(R.drawable.punch_list_white_five_img));
        } else if (125 <= offsetNum && offsetNum < 150) {
            meun_cradview.setVisibility(View.VISIBLE);//改变日记、排行布局
            fitness_diary_white_iv.setVisibility(View.VISIBLE);
            punch_list_white_iv.setVisibility(View.VISIBLE);
            LogUtil.i("setMeunCradview--5--(" + offsetNum);
            meun_cradview.setBackground(getResources().getDrawable(R.drawable.white_rounded_corners_four_bg));
            fitness_diary_pink_iv.setImageDrawable(getResources().getDrawable(R.drawable.fitness_diary_pink_four_img));
            punch_list_pink_iv.setImageDrawable(getResources().getDrawable(R.drawable.punch_list_pink_four_img));
            fitness_diary_white_iv.setImageDrawable(getResources().getDrawable(R.drawable.fitness_diary_white_six_img));
            punch_list_white_iv.setImageDrawable(getResources().getDrawable(R.drawable.punch_list_white_six_img));
        } else if (150 <= offsetNum && offsetNum < 175) {
            meun_cradview.setVisibility(View.VISIBLE);//改变日记、排行布局
            fitness_diary_white_iv.setVisibility(View.VISIBLE);
            punch_list_white_iv.setVisibility(View.VISIBLE);
            LogUtil.i("setMeunCradview--6--(" + offsetNum);
            meun_cradview.setBackground(getResources().getDrawable(R.drawable.white_rounded_corners_three_bg));
            fitness_diary_pink_iv.setImageDrawable(getResources().getDrawable(R.drawable.fitness_diary_pink_three_img));
            punch_list_pink_iv.setImageDrawable(getResources().getDrawable(R.drawable.punch_list_pink_three_img));
            fitness_diary_white_iv.setImageDrawable(getResources().getDrawable(R.drawable.fitness_diary_white_seven_img));
            punch_list_white_iv.setImageDrawable(getResources().getDrawable(R.drawable.punch_list_white_seven_img));
        } else if (175 <= offsetNum && offsetNum < 200) {
            meun_cradview.setVisibility(View.VISIBLE);//改变日记、排行布局
            fitness_diary_white_iv.setVisibility(View.VISIBLE);
            punch_list_white_iv.setVisibility(View.VISIBLE);
            LogUtil.i("setMeunCradview--7--(" + offsetNum);
            meun_cradview.setBackground(getResources().getDrawable(R.drawable.white_rounded_corners_two_bg));
            meun_cradview.setBackground(getResources().getDrawable(R.drawable.white_rounded_corners_three_bg));
            fitness_diary_pink_iv.setImageDrawable(getResources().getDrawable(R.drawable.fitness_diary_pink_two_img));
            punch_list_pink_iv.setImageDrawable(getResources().getDrawable(R.drawable.punch_list_pink_two_img));
            fitness_diary_white_iv.setImageDrawable(getResources().getDrawable(R.drawable.fitness_diary_white_eight_img));
            punch_list_white_iv.setImageDrawable(getResources().getDrawable(R.drawable.punch_list_white_eight_img));
        } else if (200 <= offsetNum && offsetNum <= 246) {
            meun_cradview.setVisibility(View.GONE);//改变日记、排行布局
            fitness_diary_white_iv.setVisibility(View.VISIBLE);
            punch_list_white_iv.setVisibility(View.VISIBLE);
            LogUtil.i("setMeunCradview--7--(" + offsetNum);
            fitness_diary_white_iv.setImageDrawable(getResources().getDrawable(R.drawable.fitness_diary_white_img));
            punch_list_white_iv.setImageDrawable(getResources().getDrawable(R.drawable.punch_list_white_img));

//        } else if (offsetNum <= -140) {
//            LogUtil.i("setMeunCradview--8--(" + offsetNum);
//            meun_cradview.setVisibility(View.GONE);//改变日记、排行布局
//            fitness_diary_white_iv.setVisibility(View.VISIBLE);
//            punch_list_white_iv.setVisibility(View.VISIBLE);
//            fitness_diary_white_iv.setImageDrawable(getResources().getDrawable(R.drawable.fitness_diary_white_img));
//            punch_list_white_iv.setImageDrawable(getResources().getDrawable(R.drawable.punch_list_white_img));
//        } else {
//            meun_cradview.setVisibility(View.GONE);//改变日记、排行布局
//            fitness_diary_white_iv.setVisibility(View.VISIBLE);
//            punch_list_white_iv.setVisibility(View.VISIBLE);
//            fitness_diary_white_iv.setImageDrawable(getResources().getDrawable(R.drawable.fitness_diary_white_img));
//            punch_list_white_iv.setImageDrawable(getResources().getDrawable(R.drawable.punch_list_white_img));
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fitness_diary_white_iv:
                    startActivity(new Intent(mActivity, UserHomeActivity.class)
                            .putExtra("id", SPUtils.getString(Constants.MY_USERID, null))
                            .putExtra("isdyn", true).putExtra("title", "健身日记"));
                    break;
                case R.id.punch_list_white_iv:
                    if (TextUtils.isEmpty(mInfo.getPerson().getDefault_branch())) {
                        ToastUtils.showToastShort("您还没有参加健身运动");
                        return;
                    }
                    if (mInfo.getMember() == null || TextUtils.equals(mInfo.getMember().getAuth(), "1")) {
                        ToastUtils.showToastShort("您还没有参加健身运动");
                        return;
                    }
                    if (myPaiMingBean == null) {
                        ToastUtils.showToastShort("您还没有参加健身运动");
                        return;
                    }
                    startActivity(new Intent(mActivity, PaiMingActivity.class).putExtra("paiming", myPaiMingBean.getData().getBranchRanking()).putExtra("cishu", myPaiMingBean.getData().getBranchScore()));

                    break;
                case R.id.fitness_diary_pink_ll:

                    //      startActivity(new Intent(mActivity, TestActivity.class));
                    startActivity(new Intent(mActivity, UserHomeActivity.class)
                            .putExtra("id", SPUtils.getString(Constants.MY_USERID, null))
                            .putExtra("isdyn", true).putExtra("title", "健身日记"));
                    break;
                case R.id.punch_list_pink_ll:
                    if (TextUtils.isEmpty(mInfo.getPerson().getDefault_branch())) {
                        ToastUtils.showToastShort("您还没有参加健身运动");
                        return;
                    }
                    if (mInfo.getMember() == null || TextUtils.equals(mInfo.getMember().getAuth(), "1")) {
                        ToastUtils.showToastShort("您还没有参加健身运动");
                        return;
                    }
                    if (myPaiMingBean == null) {
                        ToastUtils.showToastShort("您还没有参加健身运动");
                        return;
                    }
                    startActivity(new Intent(mActivity, PaiMingActivity.class).putExtra("paiming", myPaiMingBean.getData().getBranchRanking()).putExtra("cishu", myPaiMingBean.getData().getBranchScore()));
                    break;
            }
        }
    };

    /**
     * //     * 下拉刷新
     * //
     */
    private class DownRefresh extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            //  findSelectionDyn_Down(1);


            init();
            mCurrentPage = 0;
            isEnd = false;
            findNews(mCurrentPage);
            findImageNews();
            findPaiming();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
            newsListviewAdapter.notifyDataSetChanged();
            refreshLayout.finishRefresh(0, false);//传入false表示刷新失败
        }
    }

    private boolean isEnd = false;

    private void setEnd() {
        isEnd = true;//没数据了
//        ILoadingLayout endLabels = pullToRefresh.getLoadingLayoutProxy(
//                false, true);
//        endLabels.setPullLabel("—我是有底线的—");// 刚下拉时，显示的提示
//        endLabels.setRefreshingLabel("—我是有底线的—");// 刷新时
//        endLabels.setReleaseLabel("—我是有底线的—");// 下来达到一定距离时，显示的提示
//        endLabels.setLoadingDrawable(null);
        // pullToRefresh.setMode(PullToRefreshBase.Mode.DISABLED);
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
//                pullToRefresh.onRefreshComplete();
                refreshLayout.finishLoadMore(0);//传入false表示加载失败
            }
            refreshLayout.finishLoadMore(0);//传入false表示加载失败
        }
    }


    private void init() {
        // 设置下拉刷新文本
//        ILoadingLayout startLabels = pullToRefresh
//                .getLoadingLayoutProxy(true, false);
//        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
//        startLabels.setRefreshingLabel("正在加载...");// 刷新时
//        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
//        // 设置上拉刷新文本
//        ILoadingLayout endLabels = pullToRefresh.getLoadingLayoutProxy(
//                false, true);
//        endLabels.setPullLabel("上拉加载...");// 刚下拉时，显示的提示
//        endLabels.setRefreshingLabel("正在加载...");// 刷新时
//        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示


    }

    private View nullView;

    private View initBanner() {
        View v = View.inflate(mActivity, R.layout.headview_banner, null);
        CardView banner_cardview = v.findViewById(R.id.banner_cardview);
        LinearLayout collect_entrance_layout = v.findViewById(R.id.collect_entrance_layout);
        mMZBanner = (MZBannerView) v.findViewById(R.id.banner);
        nullView = v.findViewById(R.id.banner_header);
        mMZBanner.setIndicatorRes(R.drawable.home_banner_indicator_icon, R.drawable.home_banner_indicator_select_icon);
        banner_cardview = (CardView) UIUtils.setViewRatio(mActivity, banner_cardview, 155, 80);
        collect_entrance_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//收藏
                mActivity.startActivity(new Intent(mActivity, CollectEntranceActivity.class));
            }
        });
        mMZBanner.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int i) {
                //   Toast.makeText(getContext(),"click page:"+position,Toast.LENGTH_LONG).show();
                mActivity.startActivity(new Intent(mActivity, NewsDetailsActivity.class).putExtra("url", imagelist.get(i).getUrl()).putExtra("title", imagelist.get(i).getTitle()));

            }
        });

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        mMZBanner.pause();//暂停轮播
    }

    @Override
    public void onResume() {
        super.onResume();
        mMZBanner.start();//开始轮播

    }

    public static class BannerViewHolder implements MZViewHolder<RequestImageNewsDataBean.Data> {
        private RoundImageView mImageView;

        @Override
        public View createView(Context context) {
            // 返回页面布局
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
            mImageView = (RoundImageView) view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(Context context, int position, RequestImageNewsDataBean.Data data) {
            if (context != null)
                Glide.with(context).load(data.getImg_url()).into(mImageView);
        }
    }

    @Override
    public void initDta() {
        dialog.show();
        findNews(mCurrentPage);
        findImageNews();
        findPaiming();
    }

    @Override
    public void onClick(View view) {

    }

    private long starttime = 0;
    private long endtime = 0;
    float xDown, yDown, xUp;


    private class HeaderViewRefresh extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            LogUtil.i("透明度--" + alphaNum);
            if (alphaNum >= 0 && alphaNum <= 255) {
                meun_cradview.getBackground().mutate().setAlpha(alphaNum);//参数x为透明度，取值范围为0~255，数值越小越透明。
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case MSG_REFRESH_VIEW:
                    LogUtil.i("透明度--" + message.obj);
                    alphaNum = (int) message.obj;
                    if (alphaNum >= 0 && alphaNum <= 255) {
                        meun_cradview.getBackground().mutate().setAlpha(alphaNum);//参数x为透明度，取值范围为0~255，数值越小越透明。
                    }
                    break;
                case 1234:
                    mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                            LogUtil.i("X=" + dx + "Y=" + dy);
                            offsetNum += (dy);
                            if (offsetNum > 0) {
                                nullView.setVisibility(View.VISIBLE);
                                banner_header2.setVisibility(View.GONE);
//                                Message message=Message.obtain();
//                                message.what=1234;
//                                message.obj=
//                                handler.sendMessage(message);
                            } else {
                                nullView.setVisibility(View.GONE);
                                banner_header2.setVisibility(View.VISIBLE);
                            }

                            setFoldView();
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    };

    private void findNews(final int page) {
        MyStringRequest request = new MyStringRequest(Request.Method.POST, Constants.FIND_NEWS_URL_NEW, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                dialog.dismiss();
                //   pullToRefresh.onRefreshComplete();
                LogUtil.i("热门话题--" + s);
                Gson gson = new Gson();
                RequestNewsDataBean newsDataBean = gson.fromJson(s, RequestNewsDataBean.class);
                if (newsDataBean.getSuccess()) {
                    data = newsDataBean.getData().getItems();
                    //    LogUtil.i(data.toString());
                    if (mCurrentPage == 0) {
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
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToastShort("请求失败，请查看网络连接");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("page", page + "");
                return map;
            }
        };

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("findNews");
        // 设置超时时间
//        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);

    }

    private RequsetMyPaiMingBean myPaiMingBean;

    private void findPaiming() {

        MyStringRequest request = new MyStringRequest(Request.Method.GET, Constants.FIND_MYRANKING_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                myPaiMingBean = new Gson().fromJson(s, RequsetMyPaiMingBean.class);
                if (myPaiMingBean.getSuccess()) {
                    cumulative_num = myPaiMingBean.getData().getBranchScore();
                    isCumulative = false;
                    recLen = 99;
                    handler3.postDelayed(runnable, 50);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MyApplication.getHttpQueues().add(request);
    }

    private void findImageNews() {
        MyStringRequest request = new MyStringRequest(Request.Method.GET, Constants.FIND_IMAGE_NEWS_URL, new Response.Listener<String>() {

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
        );
        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("findImageNews");
        // 设置超时时间
//        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);
    }

    //增加阅读数
    private void setReadNum(final String news_id) {
        MyStringRequest request = new MyStringRequest(Request.Method.POST, Constants.PUSH_READ_NUMBER, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToastShort("请求失败，请查看网络连接");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("news_id", news_id);
                LogUtil.i("map--" + map.toString());
                return map;
            }
        };
        // 设置超时时间
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);
    }
}