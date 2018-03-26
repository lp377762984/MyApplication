package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.cn.danceland.myapplication.R;
import com.vondear.rxtools.view.RxQRCode;

/**
 * Created by shy on 2018/3/26 11:01
 * Email:644563767@qq.com
 */


public class MyQRCodeActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_qrcode);

        initData();
    }

    private void initData() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ImageView mIvCode=findViewById( R.id.iv_qrcode);
        String data=getIntent().getStringExtra("data");
        RxQRCode.createQRCode(data, 800, 800, mIvCode);
    }
}
