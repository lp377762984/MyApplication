package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.OrderExtendsInfoBean;
import com.cn.danceland.myapplication.bean.RequestOrderListBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.PriceUtils;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cn.danceland.myapplication.R.id.pullToRefresh;
import static com.cn.danceland.myapplication.R.id.tv_product_type;

/**
 * Created by shy on 2017/12/28 13:35
 * Email:644563767@qq.com
 */


public class MyOrderActivity extends Activity implements View.OnClickListener {

    private PullToRefreshListView mListView;
    private List<RequestOrderListBean.Data.Content> datalist = new ArrayList<>();
    private MyListAatapter myListAatapter;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        initView();
        initData();
    }

    private void initView() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        mListView = findViewById(pullToRefresh);
        myListAatapter = new MyListAatapter();
        mListView.setAdapter(myListAatapter);
        //设置下拉刷新模式both是支持下拉和上拉
        mListView.setMode(PullToRefreshBase.Mode.BOTH);

        init_pullToRefresh();

    }

    private void init_pullToRefresh() {

        // 设置下拉刷新文本
        ILoadingLayout startLabels = mListView
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在加载...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        // 设置上拉刷新文本
        ILoadingLayout endLabels = mListView.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("上拉加载...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在加载...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
    }

    private void initData() {
        StrBean strBean = new StrBean();
        strBean.pageCount = "0";
        String s = gson.toJson(strBean);
        try {
            find_all_order(s.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }


    class MyListAatapter extends BaseAdapter {

        @Override
        public int getCount() {
            return datalist.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            ViewHolder vh = null;
            if (convertView == null) {
                vh = new ViewHolder();
                convertView = View.inflate(MyOrderActivity.this, R.layout.listview_item_order_list, null);

                vh.tv_branch_name = convertView.findViewById(R.id.tv_branch_name);
                vh.tv_status = convertView.findViewById(R.id.tv_status);
                vh.tv_product_type = convertView.findViewById(tv_product_type);
                vh.tv_product_name = convertView.findViewById(R.id.tv_product_name);
                vh.tv_price = convertView.findViewById(R.id.tv_price);
                vh.tv_pay_price = convertView.findViewById(R.id.tv_pay_price);
                vh.btn_cancel = convertView.findViewById(R.id.btn_cancel);
                vh.btn_pay = convertView.findViewById(R.id.btn_pay);
                vh.ll_pay = convertView.findViewById(R.id.ll_pay);
                vh.ll_item = convertView.findViewById(R.id.ll_item);

                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            vh.tv_price.setText(PriceUtils.formatPrice2String(datalist.get(position).getPrice()));

            LogUtil.i(datalist.get(position).getExtends_info());
            final OrderExtendsInfoBean content = gson.fromJson(datalist.get(position).getExtends_info(), OrderExtendsInfoBean.class);
            vh.tv_branch_name.setText(content.getBranch_name());


            if (datalist.get(position).getBus_type() == 1) {
                vh.tv_product_type.setText("预付定金");
                //vh.tv_product_name
                vh.tv_pay_price.setText(PriceUtils.formatPrice2String(datalist.get(position).getPrice()));
            }
            if (datalist.get(position).getBus_type() == 2) {
                vh.tv_product_type.setText("会员卡");
                vh.tv_price.setText(PriceUtils.formatPrice2String(content.getSell_price()));
                if (!TextUtils.isEmpty(content.getDeposit_id())) {
                    vh.tv_pay_price.setText(PriceUtils.formatPrice2String(content.getSell_price() - content.getDeposit_price()));
                } else {
                    vh.tv_pay_price.setText(PriceUtils.formatPrice2String(content.getSell_price()));
                }

            }
            if (datalist.get(position).getStatus() == 1) {
                vh.tv_status.setText("待付款");
                vh.ll_pay.setVisibility(View.VISIBLE);
            } else {
                vh.tv_status.setText("已付款");
                vh.ll_pay.setVisibility(View.GONE);
            }
            LogUtil.i(PriceUtils.formatPrice2String(datalist.get(position).getPrice()) + PriceUtils.formatPrice2String(content.getSell_price()));


            vh.ll_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("orderinfo", datalist.get(position));
                    bundle.putSerializable("orderExtendsInfo", content);
                    startActivity(new Intent(MyOrderActivity.this, OrderDetailsActivity.class).putExtras(bundle));

                }
            });

            return convertView;

        }


        class ViewHolder {
            public TextView tv_branch_name;
            public TextView tv_status;
            public TextView tv_product_type;
            public TextView tv_product_name;
            public TextView tv_price;
            public TextView tv_pay_price;
            public Button btn_cancel;
            public Button btn_pay;
            public LinearLayout ll_pay;
            public LinearLayout ll_item;
        }

    }

    class StrBean {
        public String pageCount;
    }

    /**
     * 提交卡订单
     *
     * @param str
     * @throws JSONException
     */
    public void find_all_order(final String str) throws JSONException {

        JSONObject jsonObject = new JSONObject(str);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Constants.FIND_ALL__ORDER, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtil.i(jsonObject.toString());
                RequestOrderListBean orderinfo = new RequestOrderListBean();
                Gson gson = new Gson();
                orderinfo = gson.fromJson(jsonObject.toString(), RequestOrderListBean.class);
                datalist = orderinfo.getData().getContent();
                myListAatapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                ToastUtils.showToastShort(volleyError.toString());

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));

                return map;
            }
        };
        MyApplication.getHttpQueues().add(stringRequest);

    }
}
