package com.cn.danceland.myapplication.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.RequsetAllPaiMingBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.DensityUtils;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.MyStringRequest;
import com.cn.danceland.myapplication.utils.UIUtils;
import com.google.gson.Gson;
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
public class PaiMingActivity extends BaseActivity {

    private TextView title;
    private TextView tv_paiming;
    private TextView tv_daka_num;
    private ImageView header_background_iv;//打卡排行 菜单 粉色布局

    private PullToRefreshListView pullToRefresh;
    private MyUserListviewAdapter myUserListviewAdapter;
    private List<RequsetAllPaiMingBean.Data> data = new ArrayList<>();//全部的
    private List<RequsetAllPaiMingBean.Data> dataList = new ArrayList<>();//不算前三个
    private List<RequsetAllPaiMingBean.Data> headData = new ArrayList<>();//前三个
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    setHeadView();
                    break;
                default:
                    break;
            }
        }
    };

    private void setHeadView() {
        RequestOptions options = new RequestOptions().placeholder(R.drawable.img_my_avatar);
        switch (headData.size()) {
            case 0:
                top1_layout.setVisibility(View.INVISIBLE);
                top2_layout.setVisibility(View.INVISIBLE);
                top3_layout.setVisibility(View.INVISIBLE);
                break;
            case 1:
                top1_layout.setVisibility(View.VISIBLE);
                top2_layout.setVisibility(View.INVISIBLE);
                top3_layout.setVisibility(View.INVISIBLE);
                break;
            case 2:
                top1_layout.setVisibility(View.VISIBLE);
                top2_layout.setVisibility(View.VISIBLE);
                top3_layout.setVisibility(View.INVISIBLE);
                break;
            case 3:
                top1_layout.setVisibility(View.VISIBLE);
                top2_layout.setVisibility(View.VISIBLE);
                top3_layout.setVisibility(View.VISIBLE);
                break;
        }
        if (headData.size() > 0) {
            Glide.with(this)
                    .load(headData.get(0).getSelf_avatar_url())
                    .apply(options)
                    .into(iv_avatar_top1);
            tv_nick_name1.setText(headData.get(0).getNick_name());
            tv_daka_nun1.setText(headData.get(0).getBranchScore() + "次");
        }
        if (headData.size() > 1) {
            Glide.with(this)
                    .load(headData.get(1).getSelf_avatar_url())
                    .apply(options)
                    .into(iv_avatar_top2);
            tv_nick_name2.setText(headData.get(1).getNick_name());
            tv_daka_nun2.setText(headData.get(1).getBranchScore() + "次");
        }
        if (headData.size() > 2) {
            Glide.with(this)
                    .load(headData.get(2).getSelf_avatar_url())
                    .apply(options)
                    .into(iv_avatar_top3);
            tv_nick_name3.setText(headData.get(2).getNick_name());
            tv_daka_nun3.setText(headData.get(2).getBranchScore() + "次");
        }
    }

    private LinearLayout top1_layout;
    private LinearLayout top2_layout;
    private LinearLayout top3_layout;
    private TextView tv_nick_name1;
    private ImageView iv_avatar_top1;
    private TextView tv_daka_nun1;
    private ImageView iv_avatar_top2;
    private TextView tv_nick_name2;
    private TextView tv_daka_nun2;
    private ImageView iv_avatar_top3;
    private TextView tv_nick_name3;
    private TextView tv_daka_nun3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pai_ming);

        initData();
        initView();
    }

    private void initData() {
        findPaiming();
        title = findViewById(R.id.donglan_title);
        title.setText("打卡排行");
        int paiming = getIntent().getIntExtra("paiming", 0);
        int cishu = getIntent().getIntExtra("cishu", 0);
        tv_paiming = findViewById(R.id.tv_paiming);
        tv_daka_num = findViewById(R.id.tv_daka_num);

        ImageView iv_avatar = findViewById(R.id.iv_avatar);
        tv_daka_num.setText(cishu + "次");
        if (paiming == 0) {
            tv_paiming.setText("NO.--");
        } else {
            tv_paiming.setText("NO." + paiming);
        }

        RequestOptions options = new RequestOptions().placeholder(R.drawable.img_my_avatar);
        Data data = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
        Glide.with(this)
                .load(data.getPerson().getSelf_avatar_path())
                .apply(options)
                .into(iv_avatar);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    private void initView() {
        pullToRefresh = findViewById(R.id.pullToRefresh);
        header_background_iv = findViewById(R.id.header_background_iv);
        header_background_iv = (ImageView) UIUtils.setViewRatio(PaiMingActivity.this, header_background_iv, (float) 187.5, 118);
        init();
        //设置下拉刷新模式both是支持下拉和上拉
        pullToRefresh.setMode(PullToRefreshBase.Mode.DISABLED);
        myUserListviewAdapter = new MyUserListviewAdapter(this, data);
        pullToRefresh.setAdapter(myUserListviewAdapter);

        pullToRefresh.getRefreshableView().addHeaderView(initHeadview());
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
        View v = View.inflate(this, R.layout.headview_my_paiming, null);
        top1_layout = v.findViewById(R.id.top1_layout);
        iv_avatar_top1 = v.findViewById(R.id.iv_avatar_top1);
        tv_nick_name1 = v.findViewById(R.id.tv_nick_name1);
        tv_daka_nun1 = v.findViewById(R.id.tv_daka_nun1);
        top2_layout = v.findViewById(R.id.top2_layout);
        iv_avatar_top2 = v.findViewById(R.id.iv_avatar_top2);
        tv_nick_name2 = v.findViewById(R.id.tv_nick_name2);
        tv_daka_nun2 = v.findViewById(R.id.tv_daka_nun2);
        top3_layout = v.findViewById(R.id.top3_layout);
        iv_avatar_top3 = v.findViewById(R.id.iv_avatar_top3);
        tv_nick_name3 = v.findViewById(R.id.tv_nick_name3);
        tv_daka_nun3 = v.findViewById(R.id.tv_daka_nun3);

        return v;
    }

    private void findPaiming() {

        MyStringRequest request = new MyStringRequest(Request.Method.GET, Constants.FIND_BRANCHRANKING, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                Gson gson = new Gson();
                RequsetAllPaiMingBean myPaiMingBean = gson.fromJson(s, RequsetAllPaiMingBean.class);

                if (myPaiMingBean.getSuccess()) {
                    data = myPaiMingBean.getData();
                    for (int i = 0; i < data.size(); i++) {
                        if (i >= 3) {
                            dataList.add(data.get(i));
                        } else {
                            headData.add(data.get(i));
                        }
                    }
                    myUserListviewAdapter.setData(dataList);
                    myUserListviewAdapter.notifyDataSetChanged();
                    Message message = Message.obtain();
                    message.what = 1;
                    mHandler.sendMessage(message);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {

        };
        MyApplication.getHttpQueues().add(request);

    }


    public class MyUserListviewAdapter extends BaseAdapter {
        private List<RequsetAllPaiMingBean.Data> data = new ArrayList<RequsetAllPaiMingBean.Data>();
        private LayoutInflater mInflater;
        private Context context;


        public MyUserListviewAdapter(Context context, List<RequsetAllPaiMingBean.Data> data) {
            mInflater = LayoutInflater.from(context);
            this.data = data;
            this.context = context;

        }

        public void addLastList(List<RequsetAllPaiMingBean.Data> bean) {
            this.data.addAll(bean);
        }

        public void setData(List<RequsetAllPaiMingBean.Data> bean) {
            this.data = bean;
        }

        @Override
        public int getCount() {
            return data.size();
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
                viewHolder.tv_daka_nun = view.findViewById(R.id.tv_daka_nun);
                viewHolder.ll_item = view.findViewById(R.id.ll_item);
                viewHolder.tv_paiming = view.findViewById(R.id.tv_paiming);
                viewHolder.item_layout_cv = view.findViewById(R.id.item_layout_cv);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtils.dp2px(context, 80f));
            if (position == 0) {
                layoutParams.setMargins(DensityUtils.dp2px(context, 16f), DensityUtils.dp2px(context, 16f), DensityUtils.dp2px(context, 16f), DensityUtils.dp2px(context, 11f));
            } else if (position == data.size() - 1) {
                layoutParams.setMargins(DensityUtils.dp2px(context, 16f), DensityUtils.dp2px(context, 5f), DensityUtils.dp2px(context, 16f), DensityUtils.dp2px(context, 96f));
            } else {
                layoutParams.setMargins(DensityUtils.dp2px(context, 16f), DensityUtils.dp2px(context, 5f), DensityUtils.dp2px(context, 16f), DensityUtils.dp2px(context, 11f));
            }
            viewHolder.item_layout_cv.setLayoutParams(layoutParams);

            viewHolder.tv_nickname.setText(data.get(position).getNick_name());
            RequestOptions options = new RequestOptions().placeholder(R.drawable.img_my_avatar);

            Glide.with(context)
                    .load(data.get(position).getSelf_avatar_url())
                    .apply(options)
                    .into(viewHolder.iv_avatar);
            viewHolder.tv_daka_nun.setText(data.get(position).getBranchScore() + "次");
            viewHolder.tv_paiming.setText("NO." + data.get(position).getBranchRanking());
            return view;
        }

        class ViewHolder {
            TextView tv_nickname;//昵称
            ImageView iv_avatar;//头像
            ImageView iv_sex;//性别
            LinearLayout ll_item;
            TextView tv_daka_nun;
            TextView tv_paiming;
            CardView item_layout_cv;
        }
    }
}
