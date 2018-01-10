package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.OrderExtendsInfoBean;
import com.cn.danceland.myapplication.bean.RequestOrderListBean;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.PriceUtils;
import com.cn.danceland.myapplication.utils.TimeUtils;

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
        orderInfo = (RequestOrderListBean.Data.Content) bundle.getSerializable("orderinfo");
        orderExtendsInfo = (OrderExtendsInfoBean) bundle.getSerializable("orderExtendsInfo");
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
        LinearLayout ll_deposit = findViewById(R.id.ll_deposit);
        TextView tv_pay_price_STATUS = findViewById(R.id.tv_pay_price_STATUS);
        TextView tv_pay_price = findViewById(R.id.tv_pay_price);
        Button btn_cancel = findViewById(R.id.btn_cancel);
        Button btn_pay = findViewById(R.id.btn_pay);

        btn_cancel.setOnClickListener(this);
        btn_pay.setOnClickListener(this);

        if (orderInfo.getBus_type() == 1) {
            tv_price.setText(PriceUtils.formatPrice2String(orderInfo.getPrice()));
            tv_counselor.setText(orderExtendsInfo.getAdmin_emp_name());
            tv_product_type.setText("预付定金");
            if (orderExtendsInfo.getDeposit_type() == 1) {
                tv_product_name.setText("会员卡定金");
            } else if (orderExtendsInfo.getDeposit_type() == 2) {
                tv_product_name.setText("私教定金");
            } else if (orderExtendsInfo.getDeposit_type() == 3) {
                tv_product_name.setText("租柜定金");
            }
            tv_useful_life.setText("1个月");
        }

        if (orderInfo.getBus_type() == 2) {
            tv_price.setText(PriceUtils.formatPrice2String(orderExtendsInfo.getSell_price()));
            tv_counselor.setText(orderExtendsInfo.getSell_name());
            tv_product_type.setText("会员卡");
            if (orderExtendsInfo.getCharge_mode() == 2) {
                //如果是计次卡
                tv_product_name.setText(orderExtendsInfo.getCard_name() + "(" + orderExtendsInfo.getTotal_count() + "次)")
                ;
            } else {
                tv_product_name.setText(orderExtendsInfo.getCard_name());
            }


        }
        tv_useful_life.setText(orderExtendsInfo.getMonth_count()+"个月");


//        if (CardsInfo.getTime_unit() == 1) {
//            tv_useful_life.setText(orderExtendsInfo.get + "年");
//        }
//        if (CardsInfo.getTime_unit() == 2) {
//            tv_useful_life.setText(CardsInfo.getTime_value() + "个月");
//        }
//        tv_useful_life.setText(orderExtendsInfo.gete);
//
//
//        if (CardsInfo.getCharge_mode() == 1) {//计时卡
//            tv_product_type.setText("计时卡");
//        }
//        if (CardsInfo.getCharge_mode() == 2) {//计次卡
//            tv_product_type.setText("计次卡（" + CardsInfo.getTotal_count() + "次）");
//
//        }
//        if (CardsInfo.getCharge_mode() == 3) {//储值卡
//            tv_product_type.setText("储值卡");
//        }
//        //tv_number.setText(number + "");
//
//        tv_name.setText(CardsInfo.getName());
//
//
//        //单价
//        tv_price.setText(PriceUtils.formatPrice2String(CardsInfo.getPrice()));
//        total_price = CardsInfo.getPrice();
//
//        //总价
//        //     tv_total_price.setText(PriceUtils.formatPrice2String(CardsInfo.getPrice() * number));
//        // total_price = CardsInfo.getPrice() * number;
//
//
//        if (CardsInfo.getTime_unit() == 1) {
//            tv_useful_life.setText(CardsInfo.getTime_value() + "年");
//        }
//        if (CardsInfo.getTime_unit() == 2) {
//            tv_useful_life.setText(CardsInfo.getTime_value() + "个月");
//        }
//


        if (orderInfo.getStatus() == 1) {
            tv_pay_status.setText("订单待支付");
            tv_pay_price_STATUS.setText("待支付金额：");
            tv_end_time.setVisibility(View.VISIBLE);
            tv_end_time.setText("剩余时间：" + TimeUtils.leftTime(orderInfo.getOrder_time()));

            btn_cancel.setVisibility(View.VISIBLE);
            btn_pay.setVisibility(View.VISIBLE);

        }
        if (orderInfo.getStatus() == 2) {
            tv_pay_status.setText("订单已支付");
            tv_pay_price_STATUS.setText("已支付金额：");
            tv_end_time.setVisibility(View.GONE);
        }
        if (orderInfo.getStatus() == 3) {
            tv_pay_status.setText("订单已完成");
            tv_end_time.setVisibility(View.GONE);
            tv_pay_price_STATUS.setText("已支付金额：");
        }
        if (orderInfo.getStatus() == 4) {
            tv_pay_status.setText("订单已取消");
            tv_pay_price_STATUS.setText("待支付金额：");
            tv_end_time.setVisibility(View.GONE);
        }
        tv_pay_price.setText(PriceUtils.formatPrice2String(orderInfo.getPrice()));

        tv_order_time.setText(orderInfo.getOrder_time());
        if (orderInfo.getPay_way() == 1) {
            tv_pay_way.setText("支付宝");
        }
        if (orderInfo.getPay_way() == 2) {
            tv_pay_way.setText("微信");
        }
        if (!TextUtils.isEmpty(orderExtendsInfo.getDeposit_id())) {
            ll_deposit.setVisibility(View.VISIBLE);
            tv_deposit_price.setText("-" + PriceUtils.formatPrice2String(orderExtendsInfo.getDeposit_price()));
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_cancel:
                break;
            case R.id.btn_pay:
                break;
            default:
                break;
        }
    }
}
