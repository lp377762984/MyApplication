package com.cn.danceland.myapplication.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
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
import com.cn.danceland.myapplication.activity.HomeActivity;
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
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.MyStringRequest;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.cn.danceland.myapplication.utils.UIUtils;
import com.cn.danceland.myapplication.view.NumberAnimTextView;
import com.cn.danceland.myapplication.view.RoundImageView;
import com.cn.danceland.myapplication.view.StepArcView;
import com.cn.danceland.myapplication.view.adapter.CommonAdapter;
import com.cn.danceland.myapplication.view.adapter.ViewHolder;
import com.cn.danceland.myapplication.view.refresh.PullRefreshLayout;
import com.google.gson.Gson;
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

public class NewHomeFragment extends BaseFragment {
    private static final int MSG_REFRESH_VIEW = 0;//刷新界面
    private static final int MSG_REFRESH_DATA = 0x100;//暂无数据
    //    private PullToRefreshListView pullToRefresh;
    private RecyclerView mRecycler;
    private SwipeRefreshLayout mSwipe;
    private PullRefreshLayout mPull;
    private CoordinatorLayout drawer_cil;
    private ArrayList<String> mDatas;

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
    private LinearLayout fitness_diary_pink_ll;//健身日记 菜单 粉色布局
    private LinearLayout punch_list_pink_ll;//打卡排行 菜单 粉色布局
    private ImageView header_background_iv;//打卡排行 菜单 粉色布局
    private ImageView iv_avatar;
    private TextView tv_nick_name;

    private int recLen = 99;//进场累计  99-0 0-x
    private int cumulative_num = 0;//进场累计  99-0 0-x
    private boolean isCumulative = false;//进场累计 0-x开关
    private Data mInfo;//登录对象

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

    private static CollapsingToolbarLayoutState state;
    AppBarLayout appBarLayout;
    LinearLayout toolbarone;
    CollapsingToolbarLayout collapsingToolbarLayout;


    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }

    private int alphaNum = 0;//透明度
    int i = 255;

    @Override
    public View initViews() {
        LogUtil.i(Constants.HOST);
//        View v = View.inflate(mActivity, R.layout.fragment_home, null);
//        View v = View.inflate(mActivity, R.layout.fragment_home_header_view_two, null);
        View v = View.inflate(mActivity, R.layout.fragment_home_header_view_three, null);
//        View v = View.inflate(mActivity, R.layout.fragment_home_header_view, null);

        mRecycler = (RecyclerView) v.findViewById(R.id.recycler);
        mSwipe = (SwipeRefreshLayout) v.findViewById(R.id.swipe);
        mPull = (PullRefreshLayout) v.findViewById(R.id.pull);
        drawer_cil = v.findViewById(R.id.drawer_cil);
        meun_cradview = v.findViewById(R.id.meun_cradview);
        header_layout = v.findViewById(R.id.header_layout);
        in_the_cumulative_tv = v.findViewById(R.id.in_the_cumulative_tv);
        cumulative_num_tv = v.findViewById(R.id.cumulative_num_tv);
        fitness_diary_white_iv = v.findViewById(R.id.fitness_diary_white_iv);
        punch_list_white_iv = v.findViewById(R.id.punch_list_white_iv);
        fitness_diary_pink_ll = v.findViewById(R.id.fitness_diary_pink_ll);
        punch_list_pink_ll = v.findViewById(R.id.punch_list_pink_ll);
        iv_avatar = v.findViewById(R.id.iv_avatar);
        tv_nick_name = v.findViewById(R.id.tv_nick_name);
        mInfo = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
        //设置头像
        RequestOptions options = new RequestOptions().placeholder(R.drawable.img_my_avatar);

        Glide.with(mActivity).load(mInfo.getPerson().getSelf_avatar_path()).apply(options).into(iv_avatar);
        tv_nick_name.setText("Hello " + mInfo.getPerson().getNick_name());

        fitness_diary_white_iv.setOnClickListener(onClickListener);
        punch_list_white_iv.setOnClickListener(onClickListener);
        fitness_diary_pink_ll.setOnClickListener(onClickListener);
        punch_list_pink_ll.setOnClickListener(onClickListener);

        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = i - 10;
                LogUtil.i("i--" + i);
                alphaNum = (int) i;
//                if (alphaNum >= 0 && alphaNum <= 255) {
//                    meun_cradview.getBackground().mutate().setAlpha(alphaNum);//参数x为透明度，取值范围为0~255，数值越小越透明。
//                }
                new HeaderViewRefresh().execute();
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                Message message = new Message();
//                message.what = MSG_REFRESH_VIEW;
//                message.obj = i;
//                handler.sendMessage(message);
//                    }
//                }).start();
            }
        });


        toolbarone = v.findViewById(R.id.home_toolbarone);
//        toolbartwo = v.findViewById(R.id.home_toolbartwo);
        appBarLayout = v.findViewById(R.id.app_bar);
        collapsingToolbarLayout = v.findViewById(R.id.toolbar_layout);
        header_background_iv = v.findViewById(R.id.header_background_iv);
        header_background_iv = (ImageView) UIUtils.setViewRatio(mActivity, header_background_iv, (float) 187.5, 118);

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

        if (newsListviewAdapter == null) {
            newsListviewAdapter = new NewsListviewAdapter(data, mActivity);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(layoutManager);

        //   pullToRefresh.getRefreshableView().setHeaderDividersEnabled(false);   //禁止头部出现分割线
//        mRecycler.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));//添加Android自带的分割线

        mPull.setColorSchemeColors(Color.BLACK, Color.CYAN, Color.YELLOW);

        mDatas = new ArrayList<>();
        for (int i = 0; i < 220; i++) {
            mDatas.add(i + "");
        }
//        final MyAdapter adapter = new MyAdapter(getActivity(),R.layout.recyclercview_item,mDatas);
        newsListviewAdapter.setHeaderView(initBanner());
        newsListviewAdapter.setOnItemClickListener(new NewsListviewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, List<RequestNewsDataBean.Data.Items> data) {
                setReadNum(data.get(position).getId());
                mActivity.startActivity(new Intent(mActivity, NewsDetailsActivity.class).putExtra("url", data.get(position).getUrl()).putExtra("title", data.get(position).getTitle()));
            }
        });
        mRecycler.setAdapter(newsListviewAdapter);

        //下拉刷新
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                LogUtil.i("下拉刷新");
                TimerTask task = new TimerTask() {
                    public void run() {
                        new DownRefresh().execute();
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 1000);
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        SystemClock.sleep(2000);
//                        mDatas.clear();
//                        for(int i=0;i<220;i++){
//                            mDatas.add(i+"");
//                        }
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                adapter.notifyDataSetChanged();
//                                mSwipe.setRefreshing(false);
//                            }
//                        });
//                    }
//                }).start();
            }
        });

        //上拉加载更多
        mPull.setOnPullListener(new PullRefreshLayout.OnPullListener() {
            @Override
            public void onLoadMore(final PullRefreshLayout pullRefreshLayout) {
//                LogUtil.i("上拉加载更多");
                TimerTask task = new TimerTask() {
                    public void run() {
                        new UpRefresh().execute();
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 1000);
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        SystemClock.sleep(2000);
//                        mDatas.add(mDatas.size() + "");
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                adapter.notifyDataSetChanged();
//                                pullRefreshLayout.setRefreshing(false);
//                            }
//                        });
//                    }
//                }).start();
            }
        });


        GridLayoutManager gridLayoutManager = new GridLayoutManager(NewHomeFragment.this.getContext(), 4);
        state = CollapsingToolbarLayoutState.EXPANDED;
        collapsingToolbarLayout.setTitleEnabled(false);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    //全展开
                    if (state != CollapsingToolbarLayoutState.EXPANDED) {
                        state = CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
//                        toolbartwo.setVisibility(View.GONE);
                        fitness_diary_white_iv.setVisibility(View.GONE);
                        punch_list_white_iv.setVisibility(View.GONE);
                    }
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != CollapsingToolbarLayoutState.COLLAPSED) {
                        state = CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
//                        toolbartwo.setVisibility(View.VISIBLE);
                        fitness_diary_white_iv.setVisibility(View.VISIBLE);
                        punch_list_white_iv.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {
                        if (state == CollapsingToolbarLayoutState.COLLAPSED) {
                            //由折叠变为中间状态时隐藏播放按钮
                        }
                        state = CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间
//                        toolbartwo.setVisibility(View.GONE);
                        fitness_diary_white_iv.setVisibility(View.GONE);
                        punch_list_white_iv.setVisibility(View.GONE);
                    }
                }
            }

        });
        mPull.setHeader_layout(header_layout);

        /* Fragment中，注册
                * 接收MainActivity的Touch回调的对象
                * 重写其中的onTouchEvent函数，并进行该Fragment的逻辑处理
                */
        HomeActivity.MyTouchListener myTouchListener = new HomeActivity.MyTouchListener() {
            private float mPosX = 0;
            private float mPosY = 0;
            private float mCurPosX = 0;
            private float mCurPosY = 0;

            @Override
            public void onTouchEvent(MotionEvent event) {
                // 处理手势事件
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN://当屏幕检测到第一个触点按下之后就会触发到这个事件。
                        mPosX = event.getX();
                        mPosY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE://当触点在屏幕上移动时触发，触点在屏幕上停留也是会触发的，主要是由于它的灵敏度很高，而我们的手指又不可能完全静止（即使我们感觉不到移动，但其实我们的手指也在不停地抖动）。
                        mCurPosX = event.getX();
                        mCurPosY = event.getY();
//                        LogUtil.i("ACTION_MOVE--(" + (mCurPosY - mPosY));
                        setHeader((int) (mCurPosY - mPosY));
                        break;
                    case MotionEvent.ACTION_UP://当触点松开时被触发。
                        if (state == CollapsingToolbarLayoutState.COLLAPSED) {
                            int temp = originalTop + (int) (mCurPosY - mPosY);
                            if (temp >= 0 && temp <= 140) {
                                originalTop += (int) (mCurPosY - mPosY);
                            }
                        }
//                        if (mCurPosY - mPosY > 0
//                                && (Math.abs(mCurPosY - mPosY) > 25)) {
//                            //向下滑动
//                            LogUtil.i("ACTION_UP向下滑动--(" + (mCurPosY - mPosY));
//                            setHeader((int) (mCurPosY - mPosY));
//                        } else if (mCurPosY - mPosY < 0
//                                && (Math.abs(mCurPosY - mPosY) > 25)) {
//                            //向上滑动
//                            LogUtil.i("ACTION_UP向上滑动--(" + (mCurPosY - mPosY));
//                            setHeader((int) (mCurPosY - mPosY));
//                        }
                        break;
                }
            }
        };

        // 将myTouchListener注册到分发列表
        ((HomeActivity) this.getActivity()).registerMyTouchListener(myTouchListener);
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
    private int originalTop = 140;

    public void setHeader(int offsetNum) {
//        LogUtil.i("abs--(" + Math.abs(offsetNum));
//        LogUtil.i("state--(" + state);
        //140是默认布局margin top
        FrameLayout.LayoutParams lppTemp = (FrameLayout.LayoutParams) header_layout.getLayoutParams();
        if (state == CollapsingToolbarLayoutState.COLLAPSED) {
            if ((originalTop + offsetNum) >= 0 && (originalTop + offsetNum) <= 140) {
                lppTemp.setMargins(0, originalTop + offsetNum, 140, 0);

                in_the_cumulative_tv.setVisibility(View.GONE);
            } else {
                if (offsetNum < 0) {//向上滑动
                    lppTemp.setMargins(0, 0, 140, 0);
                    in_the_cumulative_tv.setVisibility(View.GONE);
                    originalTop = 0;
                } else if (offsetNum > 0) {//向下滑动
                    lppTemp.setMargins(0, 140, 140, 0);
                    in_the_cumulative_tv.setVisibility(View.VISIBLE);
                    originalTop = 140;
                }
            }
            header_layout.setLayoutParams(lppTemp);
        } else {

        }

//        if (offsetNum < 0) {//向上滑动
//            if ((originalTop + offsetNum) >= 0) {
//                lppTemp.setMargins(0, originalTop + offsetNum, 140, 0);
//                header_layout.setLayoutParams(lppTemp);
//                in_the_cumulative_tv.setVisibility(View.GONE);
//            } else {
//                lppTemp.setMargins(0, 0, 140, 0);
//                header_layout.setLayoutParams(lppTemp);
//                in_the_cumulative_tv.setVisibility(View.GONE);
//                originalTop = 0;
//            }
//        } else if (offsetNum > 0) {//向下滑动
//            LogUtil.i("111--(" + offsetNum);
//            LogUtil.i("222--(" + originalTop);
//            LogUtil.i("444--(" + (originalTop + offsetNum));
//            if ((originalTop + offsetNum) <= 140) {
//                lppTemp.setMargins(0, originalTop + offsetNum, 140, 0);
//                header_layout.setLayoutParams(lppTemp);
//                in_the_cumulative_tv.setVisibility(View.GONE);
//            } else {
//                lppTemp.setMargins(0, 140, 140, 0);
//                in_the_cumulative_tv.setVisibility(View.VISIBLE);
//                header_layout.setLayoutParams(lppTemp);
//                originalTop = 140;
//            }
//        }


//        originalTop += offsetNum;
//        if (state == CollapsingToolbarLayoutState.COLLAPSED) {
//        if (offsetNum >= 0 && offsetNum <= 140) {
//            LogUtil.i("setHeader滑动--(" + offsetNum);
//            LogUtil.i("setHeader实际--" + originalTop);
//            lppTemp.setMargins(0, originalTop, 140, 0);
//            header_layout.setLayoutParams(lppTemp);
//            in_the_cumulative_tv.setVisibility(View.VISIBLE);
//        } else {
//                if (offsetNum < 0) {//向上滑动
//                    lppTemp.setMargins(0, 0, 140, 0);
//                    header_layout.setLayoutParams(lppTemp);
//                    in_the_cumulative_tv.setVisibility(View.GONE);
//                    originalTop=0;
//                } else if (offsetNum > 0) {//向下滑动
//                    lppTemp.setMargins(0, 140, 140, 0);
//                    in_the_cumulative_tv.setVisibility(View.VISIBLE);
//                    header_layout.setLayoutParams(lppTemp);
//                    originalTop=140;
//                }
//            }
//        }
//        if (state == CollapsingToolbarLayoutState.COLLAPSED) {
//            if (Math.abs(offsetNum) >= 50 && Math.abs(offsetNum) <= 350) {
//                int marginTopNum = offsetNum / 30;
//                LogUtil.i("setHeader滑动--" + offsetNum);
//                LogUtil.i("setHeader实际--" + marginTopNum);
//                lppTemp.setMargins(0, marginTopNum, 140, 0);
//                header_layout.setLayoutParams(lppTemp);
//                in_the_cumulative_tv.setVisibility(View.VISIBLE);
//            } else {
//                if (offsetNum < 0) {//向上滑动
//                    lppTemp.setMargins(0, 0, 140, 0);
//                    header_layout.setLayoutParams(lppTemp);
//                    in_the_cumulative_tv.setVisibility(View.GONE);
//                } else if (offsetNum > 0) {//向下滑动
//                    lppTemp.setMargins(0, 140, 140, 0);
//                    in_the_cumulative_tv.setVisibility(View.VISIBLE);
//                    header_layout.setLayoutParams(lppTemp);
//                }
//            }
//        } else {
//            lppTemp.setMargins(0, 140, 140, 0);
//            in_the_cumulative_tv.setVisibility(View.VISIBLE);
//            header_layout.setLayoutParams(lppTemp);
//        }

//        AlphaAnimation anim = new AlphaAnimation(0, 1);
//        anim.setDuration(1500);
//        anim.setInterpolator(getActivity(), android.R.interpolator.linear);
//        meun_cradview.setAnimation(anim);
//        meun_cradview.setAlpha((int) distance);
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

    /**
     * 下拉刷新
     */
    private class DownRefresh extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
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
            mSwipe.setRefreshing(false);
        }
    }

    private boolean isEnd = false;

    private void setEnd() {
        isEnd = true;//没数据了
    }

    /**
     * 上拉加载更多
     */
    private class UpRefresh extends AsyncTask<Void, PullRefreshLayout, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            if (!isEnd) {//还有数据请求
                findNews(mCurrentPage);
            } else {
                Message message = Message.obtain();
                message.what = MSG_REFRESH_DATA;
                mHandler2.sendMessage(message);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
            newsListviewAdapter.notifyDataSetChanged();
            mPull.setRefreshing(false);
        }
    }

    class MyAdapter extends CommonAdapter<String> {

        public MyAdapter(Context context, int layoutId, List<String> datas) {
            super(context, layoutId, datas);
        }

        @Override
        public void convert(ViewHolder viewHolder, String item, int position) {
            viewHolder.setText(R.id.recyclerView_tv, item);

        }
    }

    private void setHeaderView(int distance) {
        LogUtil.i("透明度--" + distance);
        alphaNum = distance;
        if (alphaNum >= 0 && alphaNum <= 255) {
            meun_cradview.getBackground().mutate().setAlpha(alphaNum);//参数x为透明度，取值范围为0~255，数值越小越透明。
        }
    }

    private View initBanner() {
        View v = View.inflate(mActivity, R.layout.headview_banner, null);
        CardView banner_cardview = v.findViewById(R.id.banner_cardview);
        LinearLayout collect_entrance_layout = v.findViewById(R.id.collect_entrance_layout);
        mMZBanner = (MZBannerView) v.findViewById(R.id.banner);
        mMZBanner.setIndicatorRes(R.drawable.home_banner_indicator_icon,R.drawable.home_banner_indicator_select_icon);
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
                LogUtil.i("热门话题--"+s);
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
}