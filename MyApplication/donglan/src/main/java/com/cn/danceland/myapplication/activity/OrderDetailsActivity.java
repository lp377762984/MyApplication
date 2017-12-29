package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.OrderExtendsInfoBean;
import com.cn.danceland.myapplication.bean.RequestOrderListBean;
import com.cn.danceland.myapplication.utils.LogUtil;

import static android.R.attr.value;

/**
 * Created by shy on 2017/12/29 15:39
 * Email:644563767@qq.com
 * 订单详情
 */


public class OrderDetailsActivity extends Activity implements View.OnClickListener {

    private RequestOrderListBean.Data.Content orderInfo;
    private OrderExtendsInfoBean orderExtendsInfo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        initView();
    }

    private void initView() {

        Bundle bundle = this.getIntent().getExtras();
        orderInfo= (RequestOrderListBean.Data.Content) bundle.getSerializable("orderinfo");
        orderExtendsInfo= (OrderExtendsInfoBean) bundle.getSerializable("orderExtendsInfo");
        LogUtil.i(orderInfo.toString());
        LogUtil.i(orderExtendsInfo.toString());
        findViewById(R.id.iv_back).setOnClickListener(this);
        TextView tv_pay_status = findViewById(R.id.tv_pay_status);
        TextView tv_end_time = findViewById(R.id.tv_end_time);
        TextView tv_useful_life = findViewById(R.id.tv_useful_life);
        TextView tv_price = findViewById(R.id.tv_price);
        TextView tv_product_type = findViewById(R.id.tv_product_type);
        TextView tv_product_name = findViewById(R.id.tv_product_name);
        TextView tv_counselor = findViewById(R.id.tv_counselor);
        TextView tv_order_time = findViewById(R.id.tv_order_time);
        TextView tv_pay_way = findViewById(R.id.tv_pay_way);
        TextView tv_deposit_price = findViewById(R.id.tv_deposit_price);
        TextView tv_pay_price_STATUS = findViewById(R.id.tv_pay_price_STATUS);
        TextView tv_pay_price = findViewById(R.id.tv_pay_price);
        Button btn_cancel = findViewById(R.id.btn_cancel);
        Button btn_pay = findViewById(R.id.btn_pay);





    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case value:
                break;
            default:
                break;
        }
    }
}
