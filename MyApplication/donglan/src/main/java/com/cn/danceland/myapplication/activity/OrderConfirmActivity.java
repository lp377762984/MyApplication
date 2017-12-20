package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.RequestSellCardsInfoBean;

/**
 * Created by shy on 2017/12/20 10:14
 * Email:644563767@qq.com
 */


public class OrderConfirmActivity extends Activity implements View.OnClickListener{
    private RequestSellCardsInfoBean.Data CardsInfo;
    private boolean isme;
    private int product_type;//1是卡，2是定金，3是私教，4是其他
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_comfirm);
        initView();

    }

    private void initView() {
        Bundle bundle = this.getIntent().getExtras();
        CardsInfo = (RequestSellCardsInfoBean.Data) bundle.getSerializable("cardinfo");
        isme=bundle.getBoolean("isme",true);
        findViewById(R.id.iv_back).setOnClickListener(this);
        LinearLayout ll_01= findViewById(R.id.ll_01);
        LinearLayout ll_02= findViewById(R.id.ll_02);
        if (isme){
            ll_02.setVisibility(View.GONE);

        }else {
            ll_01.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
        case R.id.iv_back:
            finish();
        break;
        default:
        break;
        }
    }
}
