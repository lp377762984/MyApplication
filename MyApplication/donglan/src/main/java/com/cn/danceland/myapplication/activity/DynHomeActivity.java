package com.cn.danceland.myapplication.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.FrameLayout;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.adapter.CommentListviewAdapter;
import com.cn.danceland.myapplication.adapter.DynZanHeadviewRecylerViewAdapter;
import com.cn.danceland.myapplication.adapter.ImageGridAdapter;
import com.cn.danceland.myapplication.bean.RequestCommitCommentBean;
import com.cn.danceland.myapplication.bean.RequestInfoBean;
import com.cn.danceland.myapplication.bean.RequsetSimpleBean;
import com.cn.danceland.myapplication.bean.RequsetUserListBeanZan;
import com.cn.danceland.myapplication.bean.RequstCommentInfoBean;
import com.cn.danceland.myapplication.bean.RequstOneDynInfoBean;
import com.cn.danceland.myapplication.evntbus.EventConstants;
import com.cn.danceland.myapplication.evntbus.IntEvent;
import com.cn.danceland.myapplication.evntbus.StringEvent;
import com.cn.danceland.myapplication.pictureviewer.ImagePagerActivity;
import com.cn.danceland.myapplication.pictureviewer.PictureConfig;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DensityUtils;
import com.cn.danceland.myapplication.utils.KeyBoardUtils;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ScreenUtils;
import com.cn.danceland.myapplication.utils.TimeUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.cn.danceland.myapplication.view.NoScrollGridView;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.vondear.rxtools.view.likeview.RxShineButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;


/**
 * Created by shy on 2017/11/17 17:32
 * Email:644563767@qq.com
 * 动态详情页
 */


public class DynHomeActivity extends FragmentActivity implements View.OnClickListener {
    private PullToRefreshListView pullToRefresh;
    private List<RequstCommentInfoBean.Content> data = new ArrayList<RequstCommentInfoBean.Content>();
    private RequstCommentInfoBean commentInfoBean;
    private RequstOneDynInfoBean.Data oneDynInfo;
    private CommentListviewAdapter myAdater;
    private ProgressDialog dialog;
    private int mCurrentPage = 0;//当前请求页
    private String msgId;
    private String userId;
    private List<RequsetUserListBeanZan.Data.Content> zanUserList = new ArrayList<RequsetUserListBeanZan.Data.Content>();

    private DynZanHeadviewRecylerViewAdapter mRecylerViewAdapter;
    private TextView tv_zan_num;
    private ImageView iv_zan;
    //private EmojiconEditText et_comment;
    private EmojiconEditText et_comment;
    private TextView tv_send;
    private ImageView iv_emoji;
    private FrameLayout fl_emojicons;
    private TextView tv_nick_name;
    private TextView tv_time;
    private TextView tv_guanzhu;
    private TextView tv_location;
    private LinearLayout ll_location;
    private hani.momanii.supernova_emoji_library.Helper.EmojiconTextView tv_content;
    private ImageView iv_avatar;
    private NoScrollGridView gridView;
    private JZVideoPlayerStandard jzVideoPlayer;
    private RecyclerView mRecyclerView;
    private boolean init;
    private int replypos = -1;
    private RelativeLayout rl_more;
   // SlideFromBottomPopup slideFromBottomPopup;
    private EmojiconEditText et_popup_comment;
    private TextView tv_popup_title;
    private ImageView iv_pic;
    RxShineButton rx_zan;
    RxShineButton rx_guanzhu;
  //  private View listEmptyView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dyn_home);
        //注册event事件
        EventBus.getDefault().register(this);

        initView();
        initData();
    }

    private void initData() {
        findCommentList(msgId, mCurrentPage);
        findOneDyn(msgId, userId);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //even事件处理
    @Subscribe
    public void onEventMainThread(IntEvent event) {
        if (8001 == event.getEventCode()) {
            replypos = event.getMsg();
            //   LogUtil.i("收到消息" + replypos + data.get(replypos).getNickName());
            et_comment.setHint("回复" + data.get(replypos).getNickName() + ":");
            //    LogUtil.i("id" + data.get(replypos).getId() + "#########" + data.get(replypos).getReplyUserId());

            //    slideFromBottomPopup.showPopupWindow();
            //  tv_popup_title.setText("回复评论");
            //  et_popup_comment.setHint("回复" + data.get(replypos).getNickName() + ":");
        }


    }

    //even事件处理
    @Subscribe
    public void onEventMainThread(StringEvent event) {

        switch (event.getEventCode()) {
            case EventConstants.ADD_GUANZHU:

                requstOneDynInfoBean.getData().
                        setFollower(true);

                oneDynInfo = requstOneDynInfoBean.getData();
                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = oneDynInfo;
                handler.sendMessage(msg);

                break;
            default:
                break;
        }

    }

    private AnimationSet mAnimationSet;//点赞动画

    /**
     * 创建点赞动画
     */
    private void buildAnima() {
        ScaleAnimation mScaleAnimation = new ScaleAnimation(1f, 2f, 1f, 2f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mScaleAnimation.setDuration(500);
        mScaleAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        mScaleAnimation.setFillAfter(false);

        AlphaAnimation mAlphaAnimation = new AlphaAnimation(1, .2f);
        mAlphaAnimation.setDuration(500);
        mAlphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        mAlphaAnimation.setFillAfter(false);

        mAnimationSet = new AnimationSet(false);
        mAnimationSet.setDuration(500);
        mAnimationSet.addAnimation(mScaleAnimation);
        mAnimationSet.addAnimation(mAlphaAnimation);
        mAnimationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    private void initView() {

        msgId = getIntent().getStringExtra("msgId");
        userId = getIntent().getStringExtra("userId");
      //  slideFromBottomPopup = new SlideFromBottomPopup(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        tv_send = findViewById(R.id.tv_send);
        tv_send.setOnClickListener(this);
        tv_zan_num = findViewById(R.id.tv_zan_num);
        iv_zan = findViewById(R.id.iv_zan);
        rx_zan = findViewById(R.id.rx_zan);
        buildAnima();
        emojiButton = findViewById(R.id.emoji_btn);
        et_comment = findViewById(R.id.et_comment);
        et_comment.setImeOptions(EditorInfo.IME_ACTION_SEND);
        LinearLayout ll_comment = findViewById(R.id.ll_comment);
        emojIcon = new EmojIconActions(this, ll_comment, et_comment, emojiButton);
        emojIcon.ShowEmojIcon();
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e("Keyboard", "open");
                tv_zan_num.setVisibility(View.GONE);
              iv_zan.setVisibility(View.GONE);
             //   rx_zan.setVisibility(View.GONE);
                emojiButton.setVisibility(View.VISIBLE);
                //     tv_send.setVisibility(View.VISIBLE);
            }

            @Override
            public void onKeyboardClose() {
                Log.e("Keyboard", "close");
                tv_zan_num.setVisibility(View.VISIBLE);
               iv_zan.setVisibility(View.VISIBLE);
            //    rx_zan.setVisibility(View.VISIBLE);
                emojiButton.setVisibility(View.GONE);
                //   tv_send.setVisibility(View.GONE);
            }
        });
        emojIcon.addEmojiconEditTextList(et_comment);
        //  et_comment.setImeOptions();
        // android:imeOptions="actionDone"


        findViewById(R.id.tv_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  slideFromBottomPopup.showPopupWindow();
                startActivity(new Intent(DynHomeActivity.this, CommentActivity.class));

            }
        });


        pullToRefresh = findViewById(R.id.pullToRefresh);
//
//        listEmptyView = findViewById(R.id.rl_no_info);
//        TextView tv_error= listEmptyView.findViewById(R.id.tv_error);
//        ImageView imageView = listEmptyView.findViewById(R.id.iv_error);
      //  pullToRefresh.getRefreshableView().setEmptyView(listEmptyView);

        dialog = new ProgressDialog(this);
        dialog.setMessage("正在加载……");


        myAdater = new CommentListviewAdapter(data, this, msgId);

        pullToRefresh.setAdapter(myAdater);


        //设置下拉刷新模式both是支持下拉和上拉
        pullToRefresh.setMode(PullToRefreshBase.Mode.PULL_FROM_END);

        initPull2Refesh();


        pullToRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                //    new FinishRefresh().execute();
                if (!isEnd) {
                    findCommentList(msgId, mCurrentPage);
                } else {
                    pullToRefresh.onRefreshComplete();
                }


            }
        });
        pullToRefresh.getRefreshableView().addHeaderView(initHeadview());

    }

    private boolean isKeyboardShown(View rootView) {
        final int SOFT_KEYBOARD_HEIGHT_DP_THRESHOLD = 128;

        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        int heightDiff = rootView.getBottom() - r.bottom;

        return heightDiff > DensityUtils.dp2px(this, SOFT_KEYBOARD_HEIGHT_DP_THRESHOLD);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_send:
                //  LogUtil.i(et_comment.getText().toString());

                if (!TextUtils.isEmpty(et_comment.getText().toString())) {

                    if (et_comment.getText().toString().length()>1000){
                        ToastUtils.showToastShort("输入字数不能超过1000字");
                        return;
                    }
                    if (replypos < 0) {
                        sendCommentReply(msgId, et_comment.getText().toString());
                    } else {
                        sendCommentReply(msgId, et_comment.getText().toString(), data.get(replypos).getId(), data.get(replypos).getReplyUserId());
                    }
                } else {
                    ToastUtils.showToastShort("请输入");
                }


                break;


            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }


    private RequstOneDynInfoBean.Data info;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {      //判断标志位
                case 1:
                    //加入头布局
                    info = new RequstOneDynInfoBean().getData();
                    info = (RequstOneDynInfoBean.Data) msg.obj;

                    initHeadviewData((RequstOneDynInfoBean.Data) msg.obj);

                    findZanUserList_5(((RequstOneDynInfoBean.Data) msg.obj).getId(), 1);
                    if (info.getPriaseNumber() >= 0) {
                        tv_zan_num.setText(info.getPriaseNumber() + "");
                    }
                    if (info.getPraise()) {//如果点赞
                     iv_zan.setImageResource(R.drawable.img_zan1);
                        rx_zan.setChecked(true);
                    } else {
                       iv_zan.setImageResource(R.drawable.img_zan);
                        rx_zan.setChecked(false);
                    }



                    iv_zan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (info.getPraise()) {//如果点赞
                                addZan(info.getId(), false);
                            } else {//未点赞
                             //   iv_zan.clearAnimation();//添加动画
                             //   iv_zan.startAnimation(mAnimationSet);
                                addZan(info.getId(), true);
                            }
                        }
                    });


                    break;
                case 2:


                    break;
            }

        }
    };


    private void initPull2Refesh() {
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

    private boolean isEnd = false;

    private void initHeadviewData(final RequstOneDynInfoBean.Data oneDynInfo) {

        rl_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.equals(SPUtils.getString(Constants.MY_USERID, null), oneDynInfo.getAuthor())) {//是否是个人页面

                    showListDialogSelf(1);
                } else {
                    showListDialog(1);
                }
            }
        });

        if (TextUtils.equals(SPUtils.getString(Constants.MY_USERID, null), oneDynInfo.getAuthor())) {//是否是个人页面
            tv_guanzhu.setVisibility(View.INVISIBLE);
            rx_guanzhu.setVisibility(View.INVISIBLE);
        } else {
            rx_guanzhu.setVisibility(View.VISIBLE);
            rx_guanzhu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //      ToastUtils.showToastShort("点击了关注");
                    LogUtil.i(oneDynInfo.getAuthor());
                    if (!oneDynInfo.isFollower()) {//如果未关注，加关注

                        addGuanzhu(oneDynInfo.getAuthor(), true);
                    }
                }
            });
        }


        if (oneDynInfo.isFollower()) {
            tv_guanzhu.setText("已关注");
          //  tv_guanzhu.setTextColor(Color.GRAY);
            rx_guanzhu.setChecked(true);
            rx_guanzhu.setClickable(false);
        } else {
            tv_guanzhu.setText("+关注");
            rx_guanzhu.setChecked(false);
            rx_guanzhu.setClickable(true);
        }


        //  LogUtil.i(data.get(position).getNickName());
        if (!TextUtils.isEmpty(oneDynInfo.getNickName())) {
            tv_nick_name.setText(oneDynInfo.getNickName());
        }


        tv_time.setText(TimeUtils.timeLogic(oneDynInfo.getPublishTime()));
        if (TextUtils.isEmpty(oneDynInfo.getContent())) {
            tv_content.setVisibility(View.GONE);
        } else {//内容不为空赋值
            tv_content.setText(oneDynInfo.getContent());
            tv_content.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(oneDynInfo.getPublishPlace())) {
            ll_location.setVisibility(View.GONE);
        } else {//地点不为空赋值
            ll_location.setVisibility(View.VISIBLE);
            tv_location.setText(oneDynInfo.getPublishPlace());
        }


        RequestOptions options = new RequestOptions().placeholder(R.drawable.img_my_avatar);
        Glide.with(this)
                .load(oneDynInfo.getSelfUrl())
                .apply(options)
                .into(iv_avatar);
        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DynHomeActivity.this, UserSelfHomeActivity.class).putExtra("id", oneDynInfo.getAuthor()));
            }
        });

        if (oneDynInfo.getVedioUrl() != null && oneDynInfo.getMsgType() == 1) {//如果是视频消息
            jzVideoPlayer.setVisibility(View.VISIBLE);

            jzVideoPlayer.setUp(
                    oneDynInfo.getVedioUrl(), JZVideoPlayer.SCREEN_WINDOW_NORMAL,
                    "");
            Glide.with(this)
                    .load(oneDynInfo.getVedioImg())
                    .into(jzVideoPlayer.thumbImageView);
            //jzVideoPlayer.positionInList = position;
        } else {
            jzVideoPlayer.setVisibility(View.GONE);
        }

        if (oneDynInfo.getImgList() != null && oneDynInfo.getImgList().size() > 0) {


            gridView.setVisibility(View.VISIBLE);


            if (oneDynInfo.getImgList().size() == 1) {
                gridView.setVisibility(View.GONE);
                //  int height = DensityUtils.dp2px(context,100f);//此处的高度需要动态计算
                //   int width = DensityUtils.dp2px(context,100f);//此处的宽度需要动态计算
                RequestOptions options1 = new RequestOptions()
                        //  .placeholder(R.drawable.img_loading)//加载占位图
                        .error(R.drawable.img_loadfail)//
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .priority(Priority.HIGH);
                iv_pic.setVisibility(View.VISIBLE);
                StringBuilder sb = new StringBuilder(oneDynInfo.getImgList().get(0));

                String houzhui =oneDynInfo.getImgList().get(0).substring(oneDynInfo.getImgList().get(0).lastIndexOf(".")+ 1);
                sb.insert(oneDynInfo.getImgList().get(0).length() - houzhui.length()-1, "_400X400");
//                String[] b = sb.toString().split("_");
//                String[] c = b[2].toString().toString().split("X");
                String[] b = sb.toString().split("_");
                String[] c = b[1].toString().toString().split("X");

//                LogUtil.i(b[1].toString());
//
//                LogUtil.i(c[0]);
//                LogUtil.i(c[1]);
                if (TextUtils.isDigitsOnly(c[0]) && TextUtils.isDigitsOnly(c[1]) && c.length > 1) {

                    if (Float.parseFloat(c[0]) >= Float.parseFloat(c[1])) {
                        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(DynHomeActivity.this)-DensityUtils.dp2px(this,10f), (int) ((ScreenUtils.getScreenWidth(DynHomeActivity.this)-DensityUtils.dp2px(this,30f))* Float.parseFloat(c[1]) / Float.parseFloat(c[0])));
                        linearParams.setMargins(DensityUtils.dp2px(this, 5f), DensityUtils.dp2px(this, 5f), 0, 0);
                        iv_pic.setLayoutParams(linearParams);
                    } else {
                        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(DynHomeActivity.this)-DensityUtils.dp2px(this,10f), (int) ((ScreenUtils.getScreenWidth(DynHomeActivity.this)-DensityUtils.dp2px(this,30f))* Float.parseFloat(c[1]) / Float.parseFloat(c[0])));
                        linearParams.setMargins(DensityUtils.dp2px(this, 5f), DensityUtils.dp2px(this, 5f), 0, 0);
                        iv_pic.setLayoutParams(linearParams);
                    }
                }



                Glide.with(this)
                    //    .load(sb.toString())
                        .load(oneDynInfo.getImgList().get(0))
                        // .apply(options1)
                        .into(iv_pic);
                iv_pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PictureConfig config = new PictureConfig.Builder()
                                .setListData((ArrayList<String>) oneDynInfo.getImgList())//图片数据List<String> list
                                .setPosition(0)//图片下标（从第position张图片开始浏览）
                                .setDownloadPath("DCIM")//图片下载文件夹地址
                                .setIsShowNumber(false)//是否显示数字下标
                                .needDownload(true)//是否支持图片下载
                                .setPlacrHolder(R.drawable.img_loading)//占位符图片（图片加载完成前显示的资源图片，来源drawable或者mipmap）
                                .build();
                        ImagePagerActivity.startActivity(DynHomeActivity.this, config);
                    }
                });
                LinearLayout.LayoutParams linearParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                linearParams1.setMargins(DensityUtils.dp2px(this, 15f), DensityUtils.dp2px(this, 5f), 0, 0);
                gridView.setLayoutParams(linearParams1); //使设置好的布局参数应用到控件
//

            } else if (oneDynInfo.getImgList().size() == 4) {
                //  int height = DensityUtils.dp2px(context,100f);//此处的高度需要动态计算
                gridView.setNumColumns(2);
                int width = DensityUtils.dp2px(DynHomeActivity.this, 195f);//此处的宽度需要动态计算
                LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
                linearParams.setMargins(DensityUtils.dp2px(DynHomeActivity.this, 15f), DensityUtils.dp2px(this, 5f), 0, 0);
                gridView.setLayoutParams(linearParams); //使设置好的布局参数应用到控件

            } else {
                gridView.setNumColumns(3);
                int width = DensityUtils.dp2px(DynHomeActivity.this, 290f);//此处的宽度需要动态计算
                LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
                linearParams.setMargins(DensityUtils.dp2px(DynHomeActivity.this, 15f), DensityUtils.dp2px(this, 5f), 0, 0);
                gridView.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
            }


            gridView.setAdapter(new ImageGridAdapter(DynHomeActivity.this, oneDynInfo.getImgList()));
            /**
             * 图片列表点击事件
             */
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                    PictureConfig config = new PictureConfig.Builder()
                            .setListData((ArrayList<String>) oneDynInfo.getImgList())//图片数据List<String> list
                            .setPosition(i)//图片下标（从第position张图片开始浏览）
                            .setDownloadPath("donglan")//图片下载文件夹地址
                            .setIsShowNumber(true)//是否显示数字下标
                            .needDownload(true)//是否支持图片下载
                            .setPlacrHolder(R.drawable.img_loading)//占位符图片（图片加载完成前显示的资源图片，来源drawable或者mipmap）
                            .build();
                    ImagePagerActivity.startActivity(DynHomeActivity.this, config);


                }
            });
        } else {
            gridView.setVisibility(View.GONE);


        }

    }

    private View initHeadview() {

        View headview = View.inflate(this, R.layout.headview_dyn_home, null);

        tv_nick_name = headview.findViewById(R.id.tv_nick_name);
        tv_time = headview.findViewById(R.id.tv_time);
        tv_guanzhu = headview.findViewById(R.id.tv_guanzhu);
        tv_location = headview.findViewById(R.id.tv_location);
        ll_location = headview.findViewById(R.id.ll_location);
        tv_content = headview.findViewById(R.id.tv_content);
        iv_avatar = headview.findViewById(R.id.iv_avatar);
        gridView = headview.findViewById(R.id.gridview);
        jzVideoPlayer = headview.findViewById(R.id.videoplayer);
        rx_guanzhu = headview.findViewById(R.id.rx_guanzhu);

        rl_more = headview.findViewById(R.id.rl_more);
        iv_pic = headview.findViewById(R.id.iv_pic);

        //创建默认的线性LayoutManager
        mRecyclerView = headview.findViewById(R.id.my_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        //创建并设置Adapter
        mRecylerViewAdapter = new DynZanHeadviewRecylerViewAdapter(this, zanUserList);
        mRecyclerView.setAdapter(mRecylerViewAdapter);


        return headview;
    }


    private void setEnd() {
        isEnd = true;//没数据了
        ILoadingLayout endLabels = pullToRefresh.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("—我是有底线的—");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("—我是有底线的—");// 刷新时
        endLabels.setReleaseLabel("—我是有底线的—");// 下来达到一定距离时，显示的提示
        endLabels.setLoadingDrawable(null);
        pullToRefresh.setMode(PullToRefreshBase.Mode.DISABLED);
    }

    class StrBean {
        public Integer page;
        public Integer size;
        public String reply_msg_id;
        public String msg_id;
    }

    /***
     * 加载评论列表
     * @param page
     */
    private void findCommentList(final String msgId, final int page) {

        StrBean strBean = new StrBean();
        strBean.page = page;
        strBean.reply_msg_id = msgId;
        strBean.size = 20;

        JsonRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Constants.FIND_COMMENT_LIST, new Gson().toJson(strBean), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtil.i(jsonObject.toString());
                dialog.dismiss();
                pullToRefresh.onRefreshComplete();
                Gson gson = new Gson();
                commentInfoBean = gson.fromJson(jsonObject.toString(), RequstCommentInfoBean.class);
                //  LogUtil.i(commentInfoBean.toString());
                if (commentInfoBean.getSuccess()) {
                //    listEmptyView.setVisibility(View.GONE);

                    if (commentInfoBean.getData().getItems() != null) {
                        data = commentInfoBean.getData().getItems();

                        if (mCurrentPage == 0) {
//                            if (data.size()==0){
//                                listEmptyView.setVisibility(View.VISIBLE);
//                            }

                            myAdater.setData(data);
                        } else {
                            myAdater.addLastList(data);
                        }


                        myAdater.notifyDataSetChanged();
                    }
                    if (commentInfoBean.getData().isLast()) {
                        setEnd();
                    }


                    mCurrentPage = mCurrentPage + 1;//下次从第二页请求
                } else {
                    ToastUtils.showToastShort(commentInfoBean.getErrorMsg());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> hm = new HashMap<String, String>();
                String token = SPUtils.getString(Constants.MY_TOKEN, "");
                hm.put("Authorization", token);
                return hm;
            }
        };


        MyApplication.getHttpQueues().add(jsonRequest);

    }


    private RequstOneDynInfoBean requstOneDynInfoBean;

    private void findOneDyn(final String msgId, final String userId) {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.FIND_ONE_DYN, new Response.Listener<String>() {


            @Override
            public void onResponse(String s) {

                LogUtil.i(s);
                Gson gson = new Gson();
                requstOneDynInfoBean = new RequstOneDynInfoBean();
                requstOneDynInfoBean = gson.fromJson(s, RequstOneDynInfoBean.class);
                oneDynInfo = requstOneDynInfoBean.getData();

                LogUtil.i(requstOneDynInfoBean.toString());
                if (requstOneDynInfoBean.getSuccess()) {

                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.obj = oneDynInfo;
                    handler.dispatchMessage(msg);

                } else {
                    ToastUtils.showToastShort(requstOneDynInfoBean.getErrorMsg());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToastShort("查看网络连接");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("usId", userId);//用户id
                map.put("msgId", msgId);//页数

                return map;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> hm = new HashMap<String, String>();
                String token = SPUtils.getString(Constants.MY_TOKEN, "");
                hm.put("Authorization", token);
                return hm;
            }

        };
        MyApplication.getHttpQueues().add(request);

    }


    /**
     * 查找点赞人数（前五个）
     *
     * @param msgId//消息id
     * @param page//页数
     */
    private void findZanUserList_5(final String msgId, final int page) {
        StrBean strBean = new StrBean();
        strBean.size = 5;
        strBean.page = 0;
        strBean.msg_id = msgId;

        // dialog.show();
        JsonRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Constants.FIND_ZAN_USER_LIST_MSG_5, new Gson().toJson(strBean), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                LogUtil.i(jsonObject.toString());
                dialog.dismiss();
                Gson gson = new Gson();
                RequsetUserListBeanZan UserListBean = new RequsetUserListBeanZan();
                UserListBean = gson.fromJson(jsonObject.toString(), RequsetUserListBeanZan.class);

                zanUserList = UserListBean.getData().getItems();
                //     LogUtil.i(UserListBean.toString());
                if (UserListBean.getSuccess()) {


                    if (zanUserList.size() > 0) {


                        mRecylerViewAdapter.setData(zanUserList, msgId);

                        mRecylerViewAdapter.notifyDataSetChanged();


                    } else {
                        // ToastUtils.showToastShort("没有点赞数据");
                        mRecylerViewAdapter.setData(zanUserList, msgId);
                        mRecylerViewAdapter.notifyDataSetChanged();
                    }
                } else {
                    ToastUtils.showToastShort("请求失败：" + UserListBean.getErrorMsg());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dialog.dismiss();
                LogUtil.i(volleyError.toString());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> hm = new HashMap<String, String>();
                String token = SPUtils.getString(Constants.MY_TOKEN, "");
                hm.put("Authorization", token);
                return hm;
            }
        };
//
//        StringRequest request = new StringRequest(Request.Method.POST, Constants.FIND_ZAN_USER_LIST_MSG_5, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                LogUtil.i(s);
//                dialog.dismiss();
//                Gson gson = new Gson();
//                RequsetUserListBean UserListBean = new RequsetUserListBean();
//                UserListBean = gson.fromJson(s, RequsetUserListBean.class);
//
//                zanUserList = UserListBean.getData().getItems();
//                //     LogUtil.i(UserListBean.toString());
//                if (UserListBean.getSuccess()) {
//
//
//                    if (zanUserList.size() > 0) {
//
//
//                        mRecylerViewAdapter.setData(zanUserList, msgId);
//
//                        mRecylerViewAdapter.notifyDataSetChanged();
//
//
//                    } else {
//                        // ToastUtils.showToastShort("没有点赞数据");
//                        mRecylerViewAdapter.setData(zanUserList, msgId);
//                        mRecylerViewAdapter.notifyDataSetChanged();
//                    }
//                } else {
//                    ToastUtils.showToastShort("请求失败：" + UserListBean.getErrorMsg());
//                }
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                dialog.dismiss();
//                LogUtil.i(volleyError.toString());
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> map = new HashMap<>();
//                map.put("msgId", msgId);//用户id
//                map.put("page", page + "");//页数
//                return map;
//            }
//
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> hm = new HashMap<String, String>();
//                String token = SPUtils.getString(Constants.MY_TOKEN, "");
//                hm.put("Authorization", token);
//                return hm;
//            }
//
//        };
        MyApplication.getHttpQueues().add(jsonRequest);

    }


    public class ReplyBean {
        public String id;
        public String content;
        public String nick_name;
        public String parent_id;
        public String reply_msg_id;
        public String reply_user_id;
    }

    /**
     * 发送评论
     *
     * @param msgId
     * @param content
     */
    private void sendCommentReply(final String msgId, final String content) {
        ReplyBean replyBean = new ReplyBean();
        replyBean.reply_msg_id = msgId;
        replyBean.content = content;
        LogUtil.i(new Gson().toJson(replyBean));

        JsonRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Constants.SEND_COMMENT_REPLY, new Gson().toJson(replyBean), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                LogUtil.i(jsonObject.toString());

                Gson gson = new Gson();
                RequestCommitCommentBean requestCommitCommentBean=gson.fromJson(jsonObject.toString(), RequestCommitCommentBean.class);
                if (requestCommitCommentBean.isSuccess()){

                    ToastUtils.showToastShort("评论成功");
                    mCurrentPage = 0;
                  //  findCommentList(msgId, mCurrentPage);
                    myAdater.addFirst(requestCommitCommentBean.getData());
                    myAdater.notifyDataSetChanged();
                    et_comment.setText("");
                    EventBus.getDefault().post(new StringEvent(msgId, EventConstants.ADD_COMMENT));

                    KeyBoardUtils.closeKeybord(et_comment, DynHomeActivity.this);
                //    slideFromBottomPopup.dismiss();

                }else {
                    ToastUtils.showToastShort("评论失败");
                }


//                RequsetSimpleBean requestInfoBean = new RequsetSimpleBean();
//                requestInfoBean = gson.fromJson(jsonObject.toString(), RequsetSimpleBean.class);
//                if (requestInfoBean.getSuccess()) {
//                    ToastUtils.showToastShort("评论成功");
//                    mCurrentPage = 0;
//                    findCommentList(msgId, mCurrentPage);
//
//
//                    et_comment.setText("");
//                    EventBus.getDefault().post(new StringEvent(msgId, EventConstants.ADD_COMMENT));
//
//                    KeyBoardUtils.closeKeybord(et_comment, DynHomeActivity.this);
//                    slideFromBottomPopup.dismiss();
//                } else {
//                    ToastUtils.showToastShort("评论失败");
//                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dialog.dismiss();
                LogUtil.i(volleyError.toString());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> hm = new HashMap<String, String>();
                String token = SPUtils.getString(Constants.MY_TOKEN, "");
                hm.put("Authorization", token);
                return hm;
            }
        };


//
        MyApplication.getHttpQueues().add(jsonRequest);
    }

    /**
     * @param msgId//动态id
     * @param content//内容
     * @param parentId//这条评论的id
     * @param replyUserId//这条回复的userid
     */
    private void sendCommentReply(final String msgId, final String content, final String parentId, final String replyUserId) {

        LogUtil.i("这是一条评论回复" + "msgId=" + msgId + "content=" + content + "parentId=" + parentId + "replyUserId=" + replyUserId);


        ReplyBean replyBean = new ReplyBean();
        replyBean.reply_msg_id = msgId;
        replyBean.content = content;
        replyBean.reply_user_id = replyUserId;
        replyBean.parent_id = parentId;
        LogUtil.i(new Gson().toJson(replyBean));

        JsonRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Constants.SEND_COMMENT_REPLY, new Gson().toJson(replyBean), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtil.i(jsonObject.toString());

                Gson gson = new Gson();
                RequestCommitCommentBean requestCommitCommentBean=gson.fromJson(jsonObject.toString(), RequestCommitCommentBean.class);
                if (requestCommitCommentBean.isSuccess()) {
                    ToastUtils.showToastShort("评论成功");
                    mCurrentPage = 0;
                    myAdater.addFirst(requestCommitCommentBean.getData());
                    myAdater.notifyDataSetChanged();
                    replypos = -1;
                    et_comment.setText("");
                    et_comment.setHint("写评论");
                    KeyBoardUtils.closeKeybord(et_comment, DynHomeActivity.this);
                    EventBus.getDefault().post(new StringEvent(msgId, EventConstants.ADD_COMMENT));
                  //  slideFromBottomPopup.dismiss();
                } else {
                    ToastUtils.showToastShort("评论失败：" + requestCommitCommentBean.getErrorMsg());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dialog.dismiss();
                LogUtil.i(volleyError.toString());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> hm = new HashMap<String, String>();
                String token = SPUtils.getString(Constants.MY_TOKEN, "");
                hm.put("Authorization", token);
                return hm;
            }
        };


        MyApplication.getHttpQueues().add(jsonRequest);
    }

    class StrBeanZan {
        public boolean is_praise;
        public String msg_id;

    }

    class StrBeanGaunZhu {
        public boolean is_follower;
        public String user_id;

    }

    /**
     * 点赞
     *
     * @param isPraise
     * @param isPraise
     */
    private void addZan(final String msgId, final boolean isPraise) {
        StrBeanZan strBeanZan = new StrBeanZan();
        strBeanZan.msg_id = msgId;
        strBeanZan.is_praise = isPraise;


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.ADD_ZAN_URL, new Gson().toJson(strBeanZan), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtil.i(jsonObject.toString());
                Gson gson = new Gson();
                RequsetSimpleBean requestInfoBean = new RequsetSimpleBean();
                requestInfoBean = gson.fromJson(jsonObject.toString(), RequsetSimpleBean.class);

                if (requestInfoBean.getSuccess()) {


                    if (requstOneDynInfoBean.getData().getPraise()) {//如果已点赞
                        requstOneDynInfoBean.getData().setPraise(false);
                        int i = requstOneDynInfoBean.getData().getPriaseNumber() - 1;
                        requstOneDynInfoBean.getData().setPriaseNumber(i);
                        oneDynInfo = requstOneDynInfoBean.getData();
                        ToastUtils.showToastShort("取消点赞成功");
                        EventBus.getDefault().post(new StringEvent(msgId, EventConstants.DEL_ZAN_DYN_HOME));
                        Message msg = Message.obtain();
                        msg.what = 1;
                        msg.obj = oneDynInfo;
                        handler.dispatchMessage(msg);
                    } else {
                        requstOneDynInfoBean.getData().setPraise(true);
                        int i = requstOneDynInfoBean.getData().getPriaseNumber() + 1;
                        requstOneDynInfoBean.getData().setPriaseNumber(i);
                        oneDynInfo = requstOneDynInfoBean.getData();
                        ToastUtils.showToastShort("点赞成功");
                        EventBus.getDefault().post(new StringEvent(msgId, EventConstants.ADD_ZAN_DYN_HOME));
                        Message msg = Message.obtain();
                        msg.what = 1;
                        msg.obj = oneDynInfo;
                        handler.dispatchMessage(msg);
                    }


                } else {
                    ToastUtils.showToastShort("点赞失败");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.i(volleyError.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, null));
                // LogUtil.i("Bearer+"+SPUtils.getString(Constants.MY_TOKEN,null));
                //       LogUtil.i(SPUtils.getString(Constants.MY_TOKEN, null));
                return map;
            }
        };


        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("addGuanzhu");
        // 设置超时时间
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);

    }


    /**
     * 加关注
     *
     * @param id
     * @param b
     */
    private void addGuanzhu(final String id, final boolean b) {
        StrBeanGaunZhu strBeanGaunZhu = new StrBeanGaunZhu();
        strBeanGaunZhu.user_id = id;
        strBeanGaunZhu.is_follower = b;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.ADD_GUANZHU, new Gson().toJson(strBeanGaunZhu), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtil.i(jsonObject.toString());
                Gson gson = new Gson();
                RequsetSimpleBean requestInfoBean = new RequsetSimpleBean();
                requestInfoBean = gson.fromJson(jsonObject.toString(), RequsetSimpleBean.class);
                if (requestInfoBean.getSuccess()) {


                    ToastUtils.showToastShort("关注成功");
                    EventBus.getDefault().post(new StringEvent(userId, EventConstants.ADD_GUANZHU));
                } else {
                    ToastUtils.showToastShort("关注失败");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToastShort("请查看网络连接");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, null));
                // LogUtil.i("Bearer+"+SPUtils.getString(Constants.MY_TOKEN,null));
                LogUtil.i(SPUtils.getString(Constants.MY_TOKEN, null));
                return map;
            }
        };


        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("addGuanzhu");
        // 设置超时时间
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);

    }

    private void showListDialog(final int pos) {
        final String[] items = {"举报"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(this);
        //listDialog.setTitle("我是一个列表Dialog");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case 0:
                        ToastUtils.showToastShort("已举报");
                        break;
                    case 1:

                        break;
                    default:
                        break;
                }
            }
        });
        listDialog.show();
    }

    private void showListDialogSelf(final int pos) {
        final String[] items = {"删除动态"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(this);
        //listDialog.setTitle("我是一个列表Dialog");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case 0:

                        showConfirmDialog(pos);

                        break;
                    case 1:

                        break;
                    default:
                        break;
                }
            }
        });
        listDialog.show();
    }

    /**
     * 确认对话
     */
    private void showConfirmDialog(final int pos) {
        AlertDialog.Builder dialog =
                new AlertDialog.Builder(this);
        //   dialog.setTitle("提示");
        dialog.setMessage("是否删除该动态");
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                delDyn(msgId, pos);
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }


    ImageView emojiButton;
    EmojIconActions emojIcon;
    private View popupView;

//    public class SlideFromBottomPopup extends BasePopupWindow implements View.OnClickListener {
//
//
//        private Context context;
//
//        public SlideFromBottomPopup(Activity context) {
//            super(context);
//            this.context = context;
//            bindEvent();
//        }
//
//        @Override
//        protected Animation initShowAnimation() {
//            return getTranslateAnimation(250 * 2, 0, 300);
//        }
//
//        @Override
//        public View getClickToDismissView() {
//            return popupView.findViewById(R.id.click_to_dismiss);
//        }
//
//        @Override
//        public View onCreatePopupView() {
//            popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_slide_from_bottom, null);
//            return popupView;
//        }
//
//        @Override
//        public View initAnimaView() {
//            return popupView.findViewById(R.id.popup_anima);
//        }
//
//        private void bindEvent() {
//            if (popupView != null) {
//                popupView.findViewById(R.id.tv_popup_send).setOnClickListener(this);
//                popupView.findViewById(R.id.tv_cancel).setOnClickListener(this);
//                tv_popup_title = popupView.findViewById(R.id.tv_popup_title);
//                et_popup_comment = popupView.findViewById(R.id.et_popup_comment);
//                emojiButton = popupView.findViewById(R.id.emoji_btn);
//                Message message = Message.obtain();
//                message.what = 2;
//                handler.sendMessage(message);
//
//            }
//
//        }
//
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.tv_popup_send:
//
//                    //  LogUtil.i(et_comment.getText().toString());
//                    if (!TextUtils.isEmpty(et_popup_comment.getText().toString())) {
//                        if (replypos < 0) {
//                            sendCommentReply(msgId, et_popup_comment.getText().toString());
//                        } else {
//                            sendCommentReply(msgId, et_popup_comment.getText().toString(), data.get(replypos).getId(), data.get(replypos).getReplyUserId());
//                        }
//                    } else {
//                        ToastUtils.showToastShort("请输入");
//                    }
//
//
//                    break;
//                case R.id.tv_cancel:
//                    et_popup_comment.setText("");
//                    et_popup_comment.setHint("写评论");
//                    dismiss();
//                    break;
////                case R.id.tx_3:
////
////                    break;
//                default:
//                    break;
//            }
//
//        }
//    }


    /**
     * 删除动态
     *
     * @param msgId//动态id
     */
    private void delDyn(final String msgId, final int pos) {

        String Params = Constants.DEL_DYN_MSG + "/" + msgId;

        final StringRequest request = new StringRequest(Request.Method.DELETE, Params, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s);

                Gson gson = new Gson();
                RequestInfoBean requestInfoBean = new RequestInfoBean();
                requestInfoBean = gson.fromJson(s, RequestInfoBean.class);
                if (requestInfoBean.getSuccess()) {
                    ToastUtils.showToastShort("删除成功");
                    //  data.remove(pos);
                    //notifyDataSetChanged();
                    EventBus.getDefault().post(new StringEvent("home", EventConstants.DEL_DYN));
                    EventBus.getDefault().post(new StringEvent(msgId, EventConstants.DEL_DYN_DYN_HOME));
                    finish();

                } else {
                    ToastUtils.showToastShort("删除失败：" + requestInfoBean.getErrorMsg());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                LogUtil.i(volleyError.toString());
            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> hm = new HashMap<String, String>();
                String token = SPUtils.getString(Constants.MY_TOKEN, "");
                hm.put("Authorization", token);
                return hm;
            }

        };
        MyApplication.getHttpQueues().add(request);
    }


}
