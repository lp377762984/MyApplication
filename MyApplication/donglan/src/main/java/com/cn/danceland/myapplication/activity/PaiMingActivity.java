package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.RequsetAllPaiMingBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shy on 2018/1/31 11:55
 * Email:644563767@qq.com
 * 等级排名
 */


public class PaiMingActivity extends Activity {

    private PullToRefreshListView pullToRefresh;
    MyUserListviewAdapter myUserListviewAdapter;
    List<RequsetAllPaiMingBean.Data> data = new ArrayList<>();
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
        if (data.size() > 2) {
            RequestOptions options = new RequestOptions().placeholder(R.drawable.img_my_avatar);

            Glide.with(this)
                    .load(data.get(0).getSelf_avatar_url())
                    .apply(options)
                    .into(iv_avatar_top1);
            tv_nick_name1.setText(data.get(0).getNick_name());
            tv_daka_nun1.setText(data.get(0).getBranchScore() + "次");

            Glide.with(this)
                    .load(data.get(1).getSelf_avatar_url())
                    .apply(options)
                    .into(iv_avatar_top2);
            tv_nick_name2.setText(data.get(1).getNick_name());
            tv_daka_nun2.setText(data.get(1).getBranchScore() + "次");

            Glide.with(this)
                    .load(data.get(2).getSelf_avatar_url())
                    .apply(options)
                    .into(iv_avatar_top3);
            tv_nick_name3.setText(data.get(2).getNick_name());
            tv_daka_nun3.setText(data.get(2).getBranchScore() + "次");


        }


    }

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
        int paiming = getIntent().getIntExtra("paiming",0);
        int cishu = getIntent().getIntExtra("cishu",0);
        TextView tv_paiming = findViewById(R.id.tv_paiming);
        TextView tv_daka_nun = findViewById(R.id.tv_daka_nun);
        ImageView iv_avatar=findViewById(R.id.iv_avatar);
        tv_daka_nun.setText(cishu + "次");
        tv_paiming.setText("NO."+paiming);

        RequestOptions options = new RequestOptions().placeholder(R.drawable.img_my_avatar);
        Data data= (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
        Glide.with(this)
                .load(data.getPerson().getSelf_avatar_path())
                .apply(options)
                .into(iv_avatar);
    }

    private void initView() {
        pullToRefresh = findViewById(R.id.pullToRefresh);
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
        iv_avatar_top1 = v.findViewById(R.id.iv_avatar_top1);
        tv_nick_name1 = v.findViewById(R.id.tv_nick_name1);
        tv_daka_nun1 = v.findViewById(R.id.tv_daka_nun1);
        iv_avatar_top2 = v.findViewById(R.id.iv_avatar_top2);
        tv_nick_name2 = v.findViewById(R.id.tv_nick_name2);
        tv_daka_nun2 = v.findViewById(R.id.tv_daka_nun2);
        iv_avatar_top3 = v.findViewById(R.id.iv_avatar_top3);
        tv_nick_name3 = v.findViewById(R.id.tv_nick_name3);
        tv_daka_nun3 = v.findViewById(R.id.tv_daka_nun3);
        return v;
    }

    private void findPaiming() {

        StringRequest request = new StringRequest(Request.Method.GET, Constants.FIND_BRANCHRANKING, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                Gson gson = new Gson();
                RequsetAllPaiMingBean myPaiMingBean = gson.fromJson(s, RequsetAllPaiMingBean.class);

                if (myPaiMingBean.getSuccess()) {
                    data = myPaiMingBean.getData();
                    myUserListviewAdapter.setData(data);
                    myUserListviewAdapter.notifyDataSetChanged();
                    Message message = Message.obtain();
                    message.what = 1;
                    mHandler.sendMessage(message);
                } else {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, null));

                return map;
            }
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
            // return data.size();
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
        public View getView(final int i, View view, ViewGroup viewGroup) {

            ViewHolder viewHolder = null;
            if (view == null) {
                viewHolder = new ViewHolder();
                view = mInflater.inflate(R.layout.listview_item_paiminguser_list, null);
                viewHolder.iv_avatar = view.findViewById(R.id.iv_avatar);
                viewHolder.tv_nickname = view.findViewById(R.id.tv_nickname);
                viewHolder.tv_daka_nun = view.findViewById(R.id.tv_daka_nun);
                viewHolder.ll_item = view.findViewById(R.id.ll_item);
                viewHolder.tv_paiming = view.findViewById(R.id.tv_paiming);

                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.tv_nickname.setText(data.get(i).getNick_name());
            RequestOptions options = new RequestOptions().placeholder(R.drawable.img_my_avatar);

            Glide.with(context)
                    .load(data.get(i).getSelf_avatar_url())
                    .apply(options)
                    .into(viewHolder.iv_avatar);
            viewHolder.tv_daka_nun.setText(data.get(i).getBranchScore() + "次");
            viewHolder.tv_paiming.setText("NO." + data.get(i).getBranchRanking());
            return view;
        }

        class ViewHolder {
            TextView tv_nickname;//昵称
            ImageView iv_avatar;//头像
            ImageView iv_sex;//性别
            LinearLayout ll_item;
            TextView tv_daka_nun;
            TextView tv_paiming;
        }
    }
}
