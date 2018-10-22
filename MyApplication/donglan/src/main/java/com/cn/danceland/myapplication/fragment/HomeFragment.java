//package com.cn.danceland.myapplication.fragment;
//
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.design.widget.AppBarLayout;
//import android.support.design.widget.CollapsingToolbarLayout;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.PagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v4.widget.NestedScrollView;
//import android.support.v7.widget.CardView;
//import android.support.v7.widget.GridLayoutManager;
//import android.text.TextUtils;
//import android.util.DisplayMetrics;
//import android.util.SparseArray;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.AbsListView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.bumptech.glide.Glide;
//import com.cn.danceland.myapplication.MyApplication;
//import com.cn.danceland.myapplication.R;
//import com.cn.danceland.myapplication.activity.NewsDetailsActivity;
//import com.cn.danceland.myapplication.activity.PaiMingActivity;
//import com.cn.danceland.myapplication.activity.UserHomeActivity;
//import com.cn.danceland.myapplication.adapter.NewsListviewAdapter;
//import com.cn.danceland.myapplication.bean.Data;
//import com.cn.danceland.myapplication.bean.RequestImageNewsDataBean;
//import com.cn.danceland.myapplication.bean.RequestNewsDataBean;
//import com.cn.danceland.myapplication.bean.RequsetMyPaiMingBean;
//import com.cn.danceland.myapplication.utils.Constants;
//import com.cn.danceland.myapplication.utils.DataInfoCache;
//import com.cn.danceland.myapplication.utils.LogUtil;
//import com.cn.danceland.myapplication.utils.MyStringRequest;
//import com.cn.danceland.myapplication.utils.SPUtils;
//import com.cn.danceland.myapplication.utils.ToastUtils;
//import com.cn.danceland.myapplication.view.NumberAnimTextView;
//import com.cn.danceland.myapplication.view.StepArcView;
//import com.google.gson.Gson;
//import com.handmark.pulltorefresh.library.ILoadingLayout;
//import com.handmark.pulltorefresh.library.PullToRefreshBase;
//import com.handmark.pulltorefresh.library.PullToRefreshListView;
//import com.zhouwei.mzbanner.MZBannerView;
//import com.zhouwei.mzbanner.holder.MZHolderCreator;
//import com.zhouwei.mzbanner.holder.MZViewHolder;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import static com.cn.danceland.myapplication.adapter.TabAdapter.TITLES;
//
//
//public class HomeFragment extends BaseFragment {
//    private static final int MSG_REFRESH_VIEW = 0;//刷新界面
//    private PullToRefreshListView pullToRefresh;
//    private ProgressDialog dialog;
//    private List<RequestNewsDataBean.Data.Items> data = new ArrayList<>();
//    private List<RequestImageNewsDataBean.Data> imagelist = new ArrayList<>();
//    private NewsListviewAdapter newsListviewAdapter;
//    private ViewPager mViewPager;
//    private int mCurrentPage = 0;//起始请求页
//    private int mCurrentIamgenews = 1;//轮播开始页
//    private static final int TOP_NEWS_CHANGE_TIME = 4000;// 顶部新闻切换事件
//
//
//    private LinearLayout header_layout;//header布局 头像 姓名 俩按钮
//    private CardView meun_cradview;//菜单 粉色整体布局 健身日记 打卡排行layout
//    private TextView in_the_cumulative_tv;//进场累计
//    private ImageView fitness_diary_white_iv;//健身日记 header布局 白色按钮
//    private ImageView punch_list_white_iv;//打卡排行 header布局 白色按钮
//    private LinearLayout fitness_diary_pink_ll;//健身日记 菜单 粉色布局
//    private LinearLayout punch_list_pink_ll;//打卡排行 菜单 粉色布局
//
//
//    private Handler mHandler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            int item = mViewPager.getCurrentItem();
//            if (item < imagelist.size() - 1) {
//                item++;
//            } else {// 判断是否到达最后一个
//                item = 0;
//            }
//            // Log.d(TAG, "轮播条:" + item);
//
//            mViewPager.setCurrentItem(item);
//
//            mHandler.sendMessageDelayed(Message.obtain(),
//                    TOP_NEWS_CHANGE_TIME);
//
//
//        }
//    };
//
//    private Handler mHandler2 = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//
//            switch (msg.what) {
//                case 1:
//
//                    // 设置数据
//                    mMZBanner.setPages(imagelist, new MZHolderCreator<BannerViewHolder>() {
//                        @Override
//                        public BannerViewHolder createViewHolder() {
//                            return new BannerViewHolder();
//                        }
//                    });
//                    mMZBanner.start();
////                    stepArcView.setCurrentCount(1000, 1000);//进场次数
//
//                    break;
//                case 2:
//
//
//                    break;
//                default:
//                    break;
//            }
//        }
//    };
//
//    private TextView tv_indecater;
//    private TextView tv_image_title;
//    private MZBannerView mMZBanner;
//    private NumberAnimTextView natv_number;
//
//    private CollapsingToolbarLayoutState state;
////    private HeaderLayoutState state;
//
//    //    private RecyclerView recyclerViewist;
//    AppBarLayout appBarLayout;
//    LinearLayout toolbarone;
//    LinearLayout toolbartwo;
//    CollapsingToolbarLayout collapsingToolbarLayout;
//    //    List<ApplicationImageModel> list = new ArrayList<>();
////    private ShowMyAdapter showMyAdapter;
//    private LinearLayout top_layout;//打卡排行 菜单 粉色布局
////private View header_perch_view;//占位
//private NestedScrollView nestedScrollView;//占位
//
//
//    private enum CollapsingToolbarLayoutState {
//        EXPANDED,
//        COLLAPSED,
//        INTERNEDIATE
//    }
//    private enum HeaderLayoutState {
//        INITIAL,//初始状态
//        FIRST_FOLD,//一次折叠
//        SECOND_FOLD//二次折叠
//    }
//
//    private int alphaNum = 0;//透明度
//    int i = 255;
//    @Override
//    public View initViews() {
//        LogUtil.i(Constants.HOST);
////        View v = View.inflate(mActivity, R.layout.fragment_home, null);
////        View v = View.inflate(mActivity, R.layout.fragment_home_header_view_two, null);
//        View v = View.inflate(mActivity, R.layout.fragment_home_header_view_three, null);
////        View v = View.inflate(mActivity, R.layout.fragment_home_header_view, null);
//
//        pullToRefresh = v.findViewById(R.id.pullToRefresh1);
//        meun_cradview = v.findViewById(R.id.meun_cradview);
//        header_layout = v.findViewById(R.id.header_layout);
//        in_the_cumulative_tv = v.findViewById(R.id.in_the_cumulative_tv);
//        fitness_diary_white_iv = v.findViewById(R.id.fitness_diary_white_iv);
//        punch_list_white_iv = v.findViewById(R.id.punch_list_white_iv);
//        fitness_diary_pink_ll = v.findViewById(R.id.fitness_diary_pink_ll);
//        punch_list_pink_ll = v.findViewById(R.id.punch_list_pink_ll);
//        nestedScrollView = v.findViewById(R.id.nestedScrollView);
//
//        fitness_diary_white_iv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                i=i-10;
//                LogUtil.i("i--"+i);
//                alphaNum = (int)i;
////                new HeaderViewRefresh().execute();
////                new Thread(new Runnable() {
////                    @Override
////                    public void run() {
//                        Message message = new Message();
//                        message.what = MSG_REFRESH_VIEW;
//                        message.obj = i;
//                        handler.sendMessage(message);
////                    }
////                }).start();
//            }
//        });
//
//
//        toolbarone = v.findViewById(R.id.home_toolbarone);
//        toolbartwo = v.findViewById(R.id.home_toolbartwo);
//        appBarLayout = v.findViewById(R.id.app_bar);
//        collapsingToolbarLayout = v.findViewById(R.id.toolbar_layout);
////        recyclerViewist = v.findViewById(R.id.recycle_list);
//        top_layout = v.findViewById(R.id.top_layout);
////        header_perch_view = v.findViewById(R.id.header_perch_view);
//
//        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
////        ///////////////////////////////
////        Window window = getActivity().getWindow();
////        //取消状态栏透明
////        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
////        //添加Flag把状态栏设为可绘制模式
////        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
////        //设置状态栏颜色
////        window.setStatusBarColor(getResources().getColor(R.color.transparent));
////        //设置系统状态栏处于可见状态
////        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
////        //让view不根据系统窗口来调整自己的布局
////        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
////        View mChildView = mContentView.getChildAt(0);
////        if (mChildView != null) {
////            ViewCompat.setFitsSystemWindows(mChildView, false);
////            ViewCompat.requestApplyInsets(mChildView);
////        }
////        ///////////////////////////////
//
//        init();
//        //       headView = initHeadview();
//        dialog = new ProgressDialog(mActivity);
//        dialog.setMessage("加载中……");
//
//        if (newsListviewAdapter == null) {
//            newsListviewAdapter = new NewsListviewAdapter(data, mActivity);
//        }
//
//
//        //禁止头部出现分割线
//        //   pullToRefresh.getRefreshableView().setHeaderDividersEnabled(false);
//
//        pullToRefresh.getRefreshableView().setOverScrollMode(View.OVER_SCROLL_NEVER);//去掉下拉阴影
//        //设置下拉刷新模式both是支持下拉和上拉
//        pullToRefresh.setMode(PullToRefreshBase.Mode.BOTH);
//
//
//        pullToRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
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
//
//
//            }
//        });
//        // pullToRefresh.setVisibility(View.GONE);
//        pullToRefresh.setAdapter(newsListviewAdapter);
////        pullToRefresh.getRefreshableView().addHeaderView(initPMHeadView());
//        pullToRefresh.getRefreshableView().addHeaderView(initBanner());
//        // pullToRefresh.getRefreshableView().addHeaderView(initHeadview());
//
//
////        pullToRefresh.getRefreshableView().setOnScrollListener(onScrollListener);
////        pullToRefresh.getRefreshableView().setOnTouchListener(onTouchListener);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(HomeFragment.this.getContext(), 4);
////        recyclerViewist.setLayoutManager(gridLayoutManager);
////        recyclerViewist.setNestedScrollingEnabled(false);
//        state = CollapsingToolbarLayoutState.EXPANDED;
//        collapsingToolbarLayout.setTitleEnabled(false);
//        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                if (verticalOffset == 0) {
//                    //全展开
//                    if (state != CollapsingToolbarLayoutState.EXPANDED) {
//                        state = CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
//                        toolbartwo.setVisibility(View.GONE);
////                        top_layout.setVisibility(View.GONE);
////                        header_perch_view.setVisibility(View.GONE);
//                        fitness_diary_white_iv.setVisibility(View.GONE);
//                        punch_list_white_iv.setVisibility(View.GONE);
////                        toolbarone.setVisibility(View.VISIBLE);
//                        LogUtil.i("----11---");
//
//                    }
//                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
//                    if (state != CollapsingToolbarLayoutState.COLLAPSED) {
//                        state = CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
////                        toolbarone.setVisibility(View.GONE);
//                        toolbartwo.setVisibility(View.VISIBLE);
////                        top_layout.setVisibility(View.VISIBLE);
////                        header_perch_view.setVisibility(View.VISIBLE);
//                        fitness_diary_white_iv.setVisibility(View.VISIBLE);
//                        punch_list_white_iv.setVisibility(View.VISIBLE);
//                        LogUtil.i("----22---");
////                        header_layout.setPadding(0,-50,0,0);
////                        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) header_layout.getLayoutParams();
////                        lp.setMargins(0, -50, 0, 0);
////                        header_layout.setLayoutParams(lp);
//
//                    }
//                } else {
//                    if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {
//                        if (state == CollapsingToolbarLayoutState.COLLAPSED) {
//                            //由折叠变为中间状态时隐藏播放按钮
//                        }
//                        state = CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间
//                        toolbartwo.setVisibility(View.GONE);
////                        top_layout.setVisibility(View.GONE);
////                        header_perch_view.setVisibility(View.GONE);
//                        fitness_diary_white_iv.setVisibility(View.GONE);
//                        punch_list_white_iv.setVisibility(View.GONE);
////                        toolbarone.setVisibility(View.VISIBLE);
//                        LogUtil.i("----33---");
//                    }
//                }
//            }
//
//        });
//
//
////        /* Fragment中，注册
////                * 接收MainActivity的Touch回调的对象
////                * 重写其中的onTouchEvent函数，并进行该Fragment的逻辑处理
////                */
////        HomeActivity.MyTouchListener myTouchListener = new HomeActivity.MyTouchListener() {
////            private float mPosX = 0;
////            private float mPosY = 0;
////            private float mCurPosX = 0;
////            private float mCurPosY = 0;
////
////            @Override
////            public void onTouchEvent(MotionEvent event) {
////                // 处理手势事件
////                switch (event.getAction()) {
////
////                    case MotionEvent.ACTION_DOWN:
////                        mPosX = event.getX();
////                        mPosY = event.getY();
////
////                        break;
////                    case MotionEvent.ACTION_MOVE:
////                        mCurPosX = event.getX();
////                        mCurPosY = event.getY();
//////                        LogUtil.i("ACTION_MOVE--滑动--（" + (mCurPosY - mPosY));
////
////                        if (mCurPosY - mPosY > 0
////                                && (Math.abs(mCurPosY - mPosY) > 25)) {
////                            //向下滑动
////                            LogUtil.i("向下滑动--" + (Math.abs(mCurPosY - mPosY)));
////                            int d = (int) (mCurPosY - mPosY);
////                            alphaNum=255-d;
////                            if(alphaNum>=0&&alphaNum<=255){
////                                meun_cradview.getBackground().mutate().setAlpha((255-d));//参数x为透明度，取值范围为0~255，数值越小越透明。
////                            }
////                        } else if (mCurPosY - mPosY < 0
////                                && (Math.abs(mCurPosY - mPosY) > 25)) {
////                            //向上滑动
////                            LogUtil.i("向上滑动--" + (Math.abs(mCurPosY - mPosY)));
////                            int d = (int) (Math.abs(mCurPosY - mPosY));
////                            alphaNum=d;
////                            if(alphaNum>=0&&alphaNum<=255){
////                                meun_cradview.getBackground().mutate().setAlpha((255-d));//参数x为透明度，取值范围为0~255，数值越小越透明。
////                            }
////                            switch (state) {
////                                case INITIAL://初始状态
////                                    state = HeaderLayoutState.FIRST_FOLD;//一次折叠
////                                    break;
////                                case FIRST_FOLD://一次折叠
////                                    break;
////                                case SECOND_FOLD://二次折叠
////                                    break;
////                            }
////                            //全展开
////                            if (state != HeaderLayoutState.INITIAL) {
////                                state = HeaderLayoutState.INITIAL;//初始状态
////                            } else if (state != HeaderLayoutState.FIRST_FOLD) {
////                                state = HeaderLayoutState.FIRST_FOLD;//一次折叠
//////                            } else if (state != CollapsingToolbarLayoutState.SECOND_FOLD) {
//////                                if (state == CollapsingToolbarLayoutState.FIRST_FOLD) {
//////                                    //由折叠变为中间状态时隐藏播放按钮
//////                                }
//////                                state = CollapsingToolbarLayoutState.SECOND_FOLD;//二次折叠
////                            }
////
////
//////                        setHeaderView(mCurPosY - mPosY);
////                        }
////
////
//////                        int d = (int) (mCurPosY - mPosY);
//////                        if (d < 200) {
//////                            float scale = (float) (-d) / 200;
//////                            if (scale > 1) {
//////                                scale = 1;
//////                            }
//////                            float alpha = 255 * scale;
////////                            meun_cradview.setAlpha((int) alpha);
////////                            meun_cradview.getBackground().mutate().setAlpha((int) 50);//参数x为透明度，取值范围为0~255，数值越小越透明。
////////                            AlphaAnimation alphaAni = new AlphaAnimation(0.5F, 0.5F);
////////                            alphaAni.setDuration(0);
////////                            alphaAni.setFillAfter(true);
////////                            meun_cradview.startAnimation(alphaAni);
////////                            setHeaderView(alpha);
//////                        }
////                        break;
////                    case MotionEvent.ACTION_UP:
//////                        LogUtil.i("ACTION_UP--滑动--（" + (mCurPosY - mPosY));
//////                        setHeaderView(mCurPosY - mPosY);
////                        break;
////                }
////            }
////        };
////
////        // 将myTouchListener注册到分发列表
////        ((HomeActivity) this.getActivity()).registerMyTouchListener(myTouchListener);
//
//        return v;
//    }
//
//
//    private void setHeaderView(int distance) {
//        LogUtil.i("透明度--"+distance);
//        alphaNum = distance;
//        if (alphaNum >= 0 && alphaNum <= 255) {
//            meun_cradview.getBackground().mutate().setAlpha(alphaNum);//参数x为透明度，取值范围为0~255，数值越小越透明。
//        }
//
////        AlphaAnimation anim = new AlphaAnimation(0, 1);
////        anim.setDuration(1500);
////        anim.setInterpolator(getActivity(), android.R.interpolator.linear);
////        meun_cradview.setAnimation(anim);
////        meun_cradview.setAlpha((int) distance);
////        private LinearLayout header_layout;//header布局 头像 姓名 俩按钮
////        private CardView meun_cradview;//菜单 粉色整体布局 健身日记 打卡排行layout
////        private TextView in_the_cumulative_tv;//进场累计
////        private ImageView fitness_diary_white_iv;//健身日记 header布局 白色按钮
////        private ImageView punch_list_white_iv;//打卡排行 header布局 白色按钮
////        private LinearLayout fitness_diary_pink_ll;//健身日记 菜单 粉色布局
////        private LinearLayout punch_list_pink_ll;//打卡排行 菜单 粉色布局
////
////        switch (state) {
////            case INITIAL://初始状态
////                if (distance < 80) {//
////                } else if (distance >= 80 && distance < 130) {
////                } else if (distance >= 130) {
////                }
////                break;
////            case FIRST_FOLD://一次折叠
////                break;
////            case SECOND_FOLD://二次折叠
////                break;
////        }
//
//    }
//
//    private SparseArray recordSp = new SparseArray(0);
//    private int mCurrentfirstVisibleItem = 0;
//
//    private boolean scrollFlag = false;
//
//
//    //又一次尝试
//    private int mLastFirstPostion;
//    private int mLastFirstTop;
//    private int touchSlop;
//
//
//    //listview滚动监听，有动画的哦
//    AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
//
//        @Override
//        public void onScrollStateChanged(AbsListView view, int scrollState) {
//            /**
//             *scrollState有三种状态，分别是SCROLL_STATE_IDLE、SCROLL_STATE_TOUCH_SCROLL、SCROLL_STATE_FLING
//             *SCROLL_STATE_IDLE是当屏幕停止滚动时
//             *SCROLL_STATE_TOUCH_SCROLL是当用户在以触屏方式滚动屏幕并且手指仍然还在屏幕上时（The user is scrolling using touch, and their finger is still on the screen）
//             *SCROLL_STATE_FLING是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时（The user had previously been scrolling using touch and had performed a fling）
//             */
////            LogUtil.i("onScrollStateChanged--" + scrollState);
//            switch (scrollState) {
//                // 当不滚动时
//                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
//                    scrollFlag = false;
////                    // 判断滚动到底部
////                    if (pullToRefresh.getRefreshableView().getLastVisiblePosition() == (pullToRefresh.getRefreshableView()
////                            .getCount() - 1)) {
////                        toTopBtn.setVisibility(View.VISIBLE);
////                    }
////                    // 判断滚动到顶部
////                    if (pullToRefresh.getRefreshableView().getFirstVisiblePosition() == 0) {
////                        toTopBtn.setVisibility(View.GONE);
////                    }
//
//                    break;
//                case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 滚动时
//                    scrollFlag = true;
//                    break;
//                case AbsListView.OnScrollListener.SCROLL_STATE_FLING:// 是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
//                    scrollFlag = false;
//                    break;
//            }
//        }
//
//        @Override
//        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//            /**
//             * firstVisibleItem 表示在当前屏幕显示的第一个listItem在整个listView里面的位置（下标从0开始）
//             * visibleItemCount表示在现时屏幕可以见到的ListItem(部分显示的ListItem也算)总数
//             * totalItemCount表示ListView的ListItem总数
//             * listView.getLastVisiblePosition()表示在现时屏幕最后一个ListItem
//             * (最后ListItem要完全显示出来才算)在整个ListView的位置（下标从0开始）
//             */
//            int currentTop;
//
//            View firstChildView = view.getChildAt(0);
//            if (firstChildView != null) {
//                currentTop = view.getChildAt(0).getTop();
//            } else {
//                //ListView初始化的时候会回调onScroll方法，此时getChildAt(0)仍是为空的
//                return;
//            }
//            mCurrentfirstVisibleItem = firstVisibleItem;
//            //计算距离--开始
//            ItemRecod itemRecord = (ItemRecod) recordSp.get(firstVisibleItem);
//            if (null == itemRecord) {
//                itemRecord = new ItemRecod();
//            }
//            itemRecord.height = firstChildView.getHeight();
//            itemRecord.top = firstChildView.getTop();
//            recordSp.append(firstVisibleItem, itemRecord);
//            int scrollDistance = getScrollY();//滚动距离
////            LogUtil.i("scrollFlag滚了---" + scrollDistance);
////            LogUtil.i("scrollFlag---" + scrollFlag);
////            if (scrollFlag) {
////                if (scrollDistance < 80) {
////                } else if (scrollDistance >= 80 && scrollDistance < 130) {
////                } else if (scrollDistance >= 130) {
////                }
////            }
//            //计算距离--结束
//
//
//            //判断上次可见的第一个位置和这次可见的第一个位置
//            if (firstVisibleItem != mLastFirstPostion) {
//                //不是同一个位置
//                if (firstVisibleItem > mLastFirstPostion) {
//                    //TODO do down
//                    LogUtil.i("--->down--"+scrollDistance);
////                    setHeaderView(255- scrollDistance);
////                    Message message = new Message();
////                    message.what = MSG_REFRESH_VIEW;
////                    message.obj=255- scrollDistance;
////                    handler.sendMessage(message);
////                    if (state != CollapsingToolbarLayoutState.COLLAPSED) {
////                        LogUtil.i("----up-44---");
//////                        header_layout.setPadding(0,-50,0,0);
////                        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) header_layout.getLayoutParams();
////                        lp.setMargins(0, -50, 0, 0);
////                        header_layout.setLayoutParams(lp);
////
////                    }
//                } else {
//                    //TODO do up
//                    LogUtil.i("--->up--"+scrollDistance);
////                    setHeaderView(scrollDistance);
////                    Message message = new Message();
////                    message.what = MSG_REFRESH_VIEW;
////                    message.obj= scrollDistance;
////                    handler.sendMessage(message);
//                    if (state != CollapsingToolbarLayoutState.COLLAPSED) {
//                        LogUtil.i("----up-44---");
////                        header_layout.setPadding(0,-50,0,0);
//                        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) header_layout.getLayoutParams();
//                        lp.setMargins(0, -50, 0, 0);
//                        header_layout.setLayoutParams(lp);
//
//                    }
//                }
//                mLastFirstTop = currentTop;
//            } else {
//                //是同一个位置
//                if (Math.abs(currentTop - mLastFirstTop) > touchSlop) {
//                    //避免动作执行太频繁或误触，加入touchSlop判断，具体值可进行调整
//                    if (currentTop > mLastFirstTop) {
//                        //TODO do up
//                        LogUtil.i("equals--->up--"+scrollDistance);
////                        Message message = new Message();
////                        message.what = MSG_REFRESH_VIEW;
////                        message.obj= scrollDistance;
////                        handler.sendMessage(message);
//                        if (state != CollapsingToolbarLayoutState.COLLAPSED) {
//                            LogUtil.i("----up-55---");
////                        header_layout.setPadding(0,-50,0,0);
//                            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) header_layout.getLayoutParams();
//                            lp.setMargins(0, -50, 0, 0);
//                            header_layout.setLayoutParams(lp);
//
//                        }
//                    } else if (currentTop < mLastFirstTop) {
//                        //TODO do down
//                        LogUtil.i("equals--->down--"+scrollDistance);
////                        Message message = new Message();
////                        message.what = MSG_REFRESH_VIEW;
////                        message.obj=255- scrollDistance;
////                        handler.sendMessage(message);
//                    }
//                    mLastFirstTop = currentTop;
//                }
//            }
//            mLastFirstPostion = firstVisibleItem;
//
//
////            mCurrentfirstVisibleItem = firstVisibleItem;
////            View firstView = view.getChildAt(0);
////            if (null != firstView) {
////                ItemRecod itemRecord = (ItemRecod) recordSp.get(firstVisibleItem);
////                if (null == itemRecord) {
////                    itemRecord = new ItemRecod();
////                }
////                itemRecord.height = firstView.getHeight();
////                itemRecord.top = firstView.getTop();
////                recordSp.append(firstVisibleItem, itemRecord);
////                int h = getScrollY();//滚动距离
////                LogUtil.i("scrollFlag---" + scrollFlag);
////                if (scrollFlag) {
////                    LogUtil.i("scrollFlag滚了---" + h);
////                    if (h < 80) {
////                    } else if (h >= 80 && h < 130) {
////                    } else if (h >= 130) {
////                    }
////                }
////
////            }
//        }
//    };
//
//    private int getScrollY() {
//        int height = 0;
//        for (int i = 0; i < mCurrentfirstVisibleItem; i++) {
//            ItemRecod itemRecod = (ItemRecod) recordSp.get(i);
//            height += itemRecod.height;
//        }
//        ItemRecod itemRecod = (ItemRecod) recordSp.get(mCurrentfirstVisibleItem);
//        if (null == itemRecod) {
//            itemRecod = new ItemRecod();
//        }
//        return height - itemRecod.top;
//    }
//
//    class ItemRecod {
//        int height = 0;
//        int top = 0;
//    }
//
//    private TopNewsAdapter topNewsAdapter;
//    private StepArcView stepArcView;
//
//    //初始化banner
//    private View initPMHeadView() {
//        View v = View.inflate(mActivity, R.layout.headview_paiming, null);
////        View v = View.inflate(mActivity, R.layout.fragment_home_header_view, null);
//        LinearLayout ll_paiming = v.findViewById(R.id.ll_paiming);
//        LinearLayout ll_riji = v.findViewById(R.id.ll_riji);
//        natv_number = v.findViewById(R.id.natv_number);
//        stepArcView = v.findViewById(R.id.sav_step);
//
//
//        ll_paiming.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Data data = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
//                if (TextUtils.isEmpty(data.getPerson().getDefault_branch())) {
//                    ToastUtils.showToastShort("您还没有参加健身运动");
//                    return;
//                }
//                if (data.getMember() == null || TextUtils.equals(data.getMember().getAuth(), "1")) {
//                    ToastUtils.showToastShort("您还没有参加健身运动");
//                    return;
//                }
//                if (myPaiMingBean == null) {
//                    ToastUtils.showToastShort("您还没有参加健身运动");
//                    return;
//                }
//                startActivity(new Intent(mActivity, PaiMingActivity.class).putExtra("paiming", myPaiMingBean.getData().getBranchRanking()).putExtra("cishu", myPaiMingBean.getData().getBranchScore()));
//
//            }
//        });
//        ll_riji.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                //      startActivity(new Intent(mActivity, TestActivity.class));
//                startActivity(new Intent(mActivity, UserHomeActivity.class).putExtra("id", SPUtils.getString(Constants.MY_USERID, null)).putExtra("isdyn", true).putExtra("title", "健身日记"));
//            }
//        });
//
//        return v;
//    }
//
//
//    private View initBanner() {
//        View v = View.inflate(mActivity, R.layout.headview_banner, null);
//        mMZBanner = (MZBannerView) v.findViewById(R.id.banner);
//        mMZBanner.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
//            @Override
//            public void onPageClick(View view, int i) {
//                //   Toast.makeText(getContext(),"click page:"+position,Toast.LENGTH_LONG).show();
//                mActivity.startActivity(new Intent(mActivity, NewsDetailsActivity.class).putExtra("url", imagelist.get(i).getUrl()).putExtra("title", imagelist.get(i).getTitle()));
//
//            }
//        });
//
//        return v;
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        mMZBanner.pause();//暂停轮播
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        mMZBanner.start();//开始轮播
//    }
//
//    public static class BannerViewHolder implements MZViewHolder<RequestImageNewsDataBean.Data> {
//        private ImageView mImageView;
//
//        @Override
//        public View createView(Context context) {
//            // 返回页面布局
//            View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
//            mImageView = (ImageView) view.findViewById(R.id.banner_image);
//            return view;
//        }
//
//        @Override
//        public void onBind(Context context, int position, RequestImageNewsDataBean.Data data) {
//            // 数据绑定
//            // mImageView.setImageResource(data);
//            Glide.with(context).load(data.getImg_url()).into(mImageView);
//        }
//    }
//
//    private View initHeadview() {
//        View headView = View.inflate(mActivity, R.layout.headview_homepage, null);
//        RelativeLayout lv_home_img_news = headView.findViewById(R.id.lv_home_img_news);
//        DisplayMetrics dm = getResources().getDisplayMetrics();
//        int w_screen = dm.widthPixels;
//        int h_screen = dm.heightPixels;
//        // 1、设置固定大小
//        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(w_screen, w_screen * 9 / 16);
////        // 设置包裹内容或者填充父窗体大小
////        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
////                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
////        //设置padding值
////        textView.setPadding(10, 10, 10, 10);
////        //设置margin值
////        lp.setMargins(20, 20, 0, 20);
//        //      view.addView(textView,lp);
//
//        mViewPager = headView.findViewById(R.id.vp_images);
//        lv_home_img_news.setLayoutParams(lp);
//        tv_image_title = headView.findViewById(R.id.tv_image_title);
//        tv_indecater = headView.findViewById(R.id.tv_indecater);
//        LinearLayout ll_image_title_bg = headView.findViewById(R.id.ll_image_title_bg);
//        ll_image_title_bg.setBackgroundColor(Color.BLACK);
//        ll_image_title_bg.getBackground().setAlpha(80);
//
//        topNewsAdapter = new TopNewsAdapter(mActivity, imagelist);
//        mViewPager.setAdapter(topNewsAdapter);
//        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                tv_indecater.setText((position + 1) + "/" + imagelist.size());
//                tv_image_title.setText(imagelist.get(position).getTitle());
//                mCurrentIamgenews = position;
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//
//            }
//        });
//
//        return headView;
//    }
//
//
//    /**
//     * 下拉刷新
//     */
//    private class DownRefresh extends AsyncTask<Void, Void, Void> {
//
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            //  findSelectionDyn_Down(1);
//
//
//            init();
//            mCurrentPage = 0;
//            isEnd = false;
//            findNews(mCurrentPage);
//            findImageNews();
//            findPaiming();
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            dialog.dismiss();
//            newsListviewAdapter.notifyDataSetChanged();
//            pullToRefresh.onRefreshComplete();
//        }
//    }
//
//
//    /**
//     * 上拉拉刷新
//     */
//    private class UpRefresh extends AsyncTask<Void, Void, Void> {
//
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            if (!isEnd) {//还有数据请求
//                findNews(mCurrentPage);
//            }
//
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            dialog.dismiss();
//            // newsListviewAdapter.notifyDataSetChanged();
//            if (isEnd) {//没数据了
//                pullToRefresh.onRefreshComplete();
//            }
//
//            pullToRefresh.onRefreshComplete();
//        }
//    }
//
//    @Override
//    public void initDta() {
//        dialog.show();
//        findNews(mCurrentPage);
//        findImageNews();
//        findPaiming();
//
//    }
//
//
//    @Override
//    public void onClick(View view) {
//
//    }
//
//    private void init() {
//        // 设置下拉刷新文本
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
//
//
//    }
//
//
//    public class TopNewsAdapter extends PagerAdapter {
//        private Context context;
//        private List<RequestImageNewsDataBean.Data> images;
//
//        public TopNewsAdapter(Context context, List<RequestImageNewsDataBean.Data> images) {
//            this.context = context;
//            this.images = images;
//        }
//
//        public void setData(List<RequestImageNewsDataBean.Data> images) {
//            this.images = images;
//        }
//
//        @Override
//        public int getCount() {
//            return images.size();
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//
//
//            return object == view;
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup container, final int position) {
//            ImageView image = new ImageView(context);
//            image.setScaleType(ImageView.ScaleType.FIT_XY);// 设置图片展现样式为:
//            // 宽高填充ImageView(图片可能被拉伸或者缩放)
//            //   image.setImageResource(R.drawable.topnews_item_default);
//            container.addView(image);
//
//
//            Glide.with(context).load(images.get(position).getImg_url()).into(image);
//
//            // 参2表示图片url
//            image.setOnTouchListener(new TopNewsTouchListener());
//
//            return image;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            container.removeView((View) object);
//        }
//
//    }
//
//    private long starttime = 0;
//    private long endtime = 0;
//    float xDown, yDown, xUp;
//
//    class TopNewsTouchListener implements View.OnTouchListener {
//
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//
//
//            if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                xDown = event.getX();
//                yDown = event.getY();
//
//                mHandler.removeCallbacksAndMessages(null);// 移除消息队列中的所有元素
//
//                starttime = event.getEventTime();
//            } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
//                // 那么就不会响应ACTION_UP动作了)
//                //     Log.d(TAG, "事件取消");
//                mHandler.sendMessageDelayed(Message.obtain(),
//                        TOP_NEWS_CHANGE_TIME);
//            } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                xUp = event.getX();
//                mHandler.sendMessageDelayed(Message.obtain(),
//                        TOP_NEWS_CHANGE_TIME);
//                endtime = event.getEventTime();
//
//
//                if ((xUp - xDown) > 20) {
//                    //添加要处理的内容
//                } else if ((xUp - xDown) < -20) {
//                    //添加要处理的内容
//                } else if (0 == (xDown - xUp)) {
//
//                    //       LogUtil.i("点击了图片");
//                    mActivity.startActivity(new Intent(mActivity, NewsDetailsActivity.class).putExtra("url", imagelist.get(mCurrentIamgenews).getUrl()).putExtra("title", imagelist.get(mCurrentIamgenews).getTitle()));
//
////                    int viewWidth = v.getWidth();
////                    if( xDown < viewWidth/3 )
////                    {
////                        //靠左点击
////                    }
////                    else if(xDown > viewWidth/3 && xDown < viewWidth * 2 /3)
////                    {
////                        //中间点击
////                    }
////                    else
////                    {
////                        //靠右点击
////                    }
//                }
//
//
//            }
//            // LogUtil.i((endtime - starttime) + "");
//
//
//            return true;
//        }
//    }
//    private class HeaderViewRefresh extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            LogUtil.i("透明度--"+alphaNum);
//            if (alphaNum >= 0 && alphaNum <= 255) {
//                meun_cradview.getBackground().mutate().setAlpha(alphaNum);//参数x为透明度，取值范围为0~255，数值越小越透明。
//            }
//        }
//    }
//
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message message) {
//            switch (message.what) {
//                case MSG_REFRESH_VIEW:
//                    LogUtil.i("透明度--"+message.obj);
//                    alphaNum = (int)message.obj;
//                    if (alphaNum >= 0 && alphaNum <= 255) {
//                        meun_cradview.getBackground().setAlpha(alphaNum);//参数x为透明度，取值范围为0~255，数值越小越透明。
//                    }
//                    break;
//                default:
//                    break;
//            }
//        }
//    };
//
//    private boolean isEnd = false;
//
//    private void setEnd() {
//        isEnd = true;//没数据了
//        ILoadingLayout endLabels = pullToRefresh.getLoadingLayoutProxy(
//                false, true);
//        endLabels.setPullLabel("—我是有底线的—");// 刚下拉时，显示的提示
//        endLabels.setRefreshingLabel("—我是有底线的—");// 刷新时
//        endLabels.setReleaseLabel("—我是有底线的—");// 下来达到一定距离时，显示的提示
//        endLabels.setLoadingDrawable(null);
//        // pullToRefresh.setMode(PullToRefreshBase.Mode.DISABLED);
//    }
//
//    private void findNews(int page) {
//
//        String params = page + "";
//        String url = Constants.FIND_NEWS_URL + params;
//        MyStringRequest request = new MyStringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//
//
//            @Override
//            public void onResponse(String s) {
//                dialog.dismiss();
//                //   pullToRefresh.onRefreshComplete();
//                LogUtil.i(s);
//                Gson gson = new Gson();
//                RequestNewsDataBean newsDataBean = gson.fromJson(s, RequestNewsDataBean.class);
//                if (newsDataBean.getSuccess()) {
//                    data = newsDataBean.getData().getItems();
//
//
//                    //    LogUtil.i(data.toString());
//                    if (mCurrentPage == 0) {
//                        newsListviewAdapter.setData(data);
//
//                        newsListviewAdapter.notifyDataSetChanged();
//                        //   pullToRefresh.setVisibility(View.VISIBLE);
//
//                    } else {
//                        LogUtil.i("addLastList");
//                        newsListviewAdapter.addLastList(data);
//                        newsListviewAdapter.notifyDataSetChanged();
//                    }
//                    if (data.size() > 0 && data.size() < 10) {
//                        setEnd();
//                    } else {
//                        mCurrentPage = mCurrentPage + 1;
//
//                    }
//
//                }
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(final VolleyError volleyError) {
//                LogUtil.i(volleyError.toString());
//                dialog.dismiss();
//                ToastUtils.showToastShort("请查看网络连接");
//
//            }
//
//        }
//        ) {
//
//        };
//        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
//        request.setTag("findNews");
//        // 设置超时时间
////        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
////                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        // 将请求加入全局队列中
//        MyApplication.getHttpQueues().add(request);
//
//    }
//
//    private RequsetMyPaiMingBean myPaiMingBean;
//
//    private void findPaiming() {
//
//        MyStringRequest request = new MyStringRequest(Request.Method.GET, Constants.FIND_MYRANKING_URL, new Response.Listener<String>() {
//
//
//            @Override
//            public void onResponse(String s) {
//                LogUtil.i(s);
//                myPaiMingBean = new Gson().fromJson(s, RequsetMyPaiMingBean.class);
//
//                if (myPaiMingBean.getSuccess()) {
//
//
////                    // 设置动画时长
////                    natv_number.setDuration(1000);//进场次数
////                    // 设置数字增加范围
////                    natv_number.setNumberString("0", myPaiMingBean.getData().getBranchScore() + "");//进场次数
//
//                    Message message = Message.obtain();
//                    message.what = 2;
//                    mHandler2.sendMessage(message);
//                } else {
//
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//
//            }
//        }) {
//
//        };
//        MyApplication.getHttpQueues().add(request);
//
//    }
//
//    private void findImageNews() {
//
//
//        MyStringRequest request = new MyStringRequest(Request.Method.GET, Constants.FIND_IMAGE_NEWS_URL, new Response.Listener<String>() {
//
//
//            @Override
//            public void onResponse(String s) {
//
//                LogUtil.i(s);
//                Gson gson = new Gson();
//
//                RequestImageNewsDataBean imageNewsDataBean = new RequestImageNewsDataBean();
//                imageNewsDataBean = gson.fromJson(s, RequestImageNewsDataBean.class);
//                if (imageNewsDataBean.getSuccess()) {
//
//                    if (imageNewsDataBean.getData().size() > 0) {
//                        imagelist = imageNewsDataBean.getData();
//
//                        Message message = Message.obtain();
//                        message.what = 1;
//                        mHandler2.sendMessage(message);
//
//                    } else {
//                        ToastUtils.showToastShort("轮播图片为空");
//                    }
//
//                } else {
//
//                    ToastUtils.showToastShort(imageNewsDataBean.getErrorMsg());
//                }
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(final VolleyError volleyError) {
//                LogUtil.i(volleyError.toString());
//                dialog.dismiss();
//                ToastUtils.showToastShort("请查看网络连接");
//
//            }
//
//        }
//        ) {
//
//        };
//        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
//        request.setTag("findImageNews");
//        // 设置超时时间
////        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
////                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        // 将请求加入全局队列中
//        MyApplication.getHttpQueues().add(request);
//
//    }
//
//
//}
