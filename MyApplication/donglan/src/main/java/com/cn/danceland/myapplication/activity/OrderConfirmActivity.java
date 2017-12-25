package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.RequestConsultantInfoBean;
import com.cn.danceland.myapplication.bean.RequestSellCardsInfoBean;
import com.cn.danceland.myapplication.utils.PriceUtils;

/**
 * Created by shy on 2017/12/20 10:14
 * Email:644563767@qq.com
 */


public class OrderConfirmActivity extends Activity implements View.OnClickListener {
    private RequestSellCardsInfoBean.Data CardsInfo;
    private boolean isme;//是否是本人购买
    private int product_type;//1是卡，2是定金，3是私教，4是其他
    private int number = 1;
    private RequestConsultantInfoBean.Data consultantInfo;
    private String name;
    private String phone;
    private String startDate;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_comfirm);
        initView();

    }

    private void initView() {
        Bundle bundle = this.getIntent().getExtras();
        CardsInfo = (RequestSellCardsInfoBean.Data) bundle.getSerializable("cardinfo");
        consultantInfo= (RequestConsultantInfoBean.Data) bundle.getSerializable("consultantInfo");
        isme = bundle.getBoolean("isme", true);
        product_type = bundle.getInt("product_type", 1);

        findViewById(R.id.iv_back).setOnClickListener(this);
        LinearLayout ll_01 = findViewById(R.id.ll_01);
        LinearLayout ll_02 = findViewById(R.id.ll_02);
        LinearLayout ll_03 = findViewById(R.id.ll_03);
        TextView tv_name = findViewById(R.id.tv_product_name);
        TextView tv_number = findViewById(R.id.tv_number);
        TextView tv_useful_life = findViewById(R.id.tv_useful_life);
        TextView tv_price = findViewById(R.id.tv_price);
        TextView tv_product_type = findViewById(R.id.tv_product_type);
        TextView tv_total_price = findViewById(R.id.tv_total_price);
        TextView tv_explain = findViewById(R.id.tv_explain);
        TextView tv_start_date= findViewById(R.id.tv_start_date);
        TextView tv_counselor= findViewById(R.id.tv_counselor);

        if (isme) {//是否是本人
            ll_02.setVisibility(View.GONE);
            if (product_type == 1) {
                startDate=bundle.getString("startDate",null);
                tv_start_date.setText(startDate);
                tv_explain.setText(R.string.explain_for_card);
            }

            if (product_type == 2) {//是定金
                ll_01.setVisibility(View.GONE);
                tv_explain.setText(R.string.explain_for_dingjin);
            }
        } else {//给其他人买

            name=bundle.getString("name",null);
            phone=bundle.getString("phone",null);
            ll_01.setVisibility(View.GONE);
            if (product_type == 1) {
                tv_explain.setText(R.string.explain_for_card_for_other);
            }

            if (product_type == 2) {//是定金
                tv_explain.setText(R.string.explain_for_dingjin_for_other);
            }
        }


        tv_counselor.setText(consultantInfo.getCname());

        if (product_type == 1) {//如果商品是卡


            if (CardsInfo.getCharge_mode() == 1) {//计时卡
                tv_product_type.setText("计时卡");
            }
            if (CardsInfo.getCharge_mode() == 2) {//计次卡
                tv_product_type.setText("计次卡（" + CardsInfo.getTotal_count() + "次）");

            }
            if (CardsInfo.getCharge_mode() == 3) {//储值卡
                tv_product_type.setText("储值卡");
            }
            tv_number.setText(number + "");

            tv_name.setText(CardsInfo.getName());


            //单价
            tv_price.setText(PriceUtils.formatPrice2String(CardsInfo.getPrice()));


            //总价
            tv_total_price.setText(PriceUtils.formatPrice2String(CardsInfo.getPrice() * number));

//        if (!TextUtils.isEmpty(CardsInfo.getTotal_count())) {
//            tv_number.setText("次数：" + CardsInfo.getTotal_count() + "次");
//            tv_number.setVisibility(View.VISIBLE);
//        } else {
//            tv_number.setVisibility(View.GONE);
//        }

            if (CardsInfo.getTime_unit() == 1) {
                tv_useful_life.setText(CardsInfo.getTime_value() + "年");
            }
            if (CardsInfo.getTime_unit() == 2) {
                tv_useful_life.setText(CardsInfo.getTime_value() + "个月");
            }

        }
        if (product_type == 2) {//如果商品是定金
            ll_03.setVisibility(View.GONE);
            tv_name.setText("预付定金");
            tv_product_type.setText("会籍卡定金");
            tv_useful_life.setText("1个月");
            tv_price.setText(PriceUtils.formatPrice2String(100));
            tv_total_price.setText(PriceUtils.formatPrice2String(100 * number));

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
}
