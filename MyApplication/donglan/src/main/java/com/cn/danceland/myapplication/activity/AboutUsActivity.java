package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.danceland.myapplication.R;

/**
 * Created by feng on 2017/11/13.
 */

public class AboutUsActivity extends Activity {

    ImageView about_back;
    private TextView about_verson;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
    }

    private void initView() {
        about_back = findViewById(R.id.about_back);
        about_back.setOnClickListener(onClickListener);
        about_verson = findViewById(R.id.about_verson);
        findViewById(R.id.about_verson).setOnClickListener(onClickListener);

    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.about_back:
                    finish();
                    break;
                case R.id.about_verson://切换服务器
//                    if (TextUtils.equals("http://192.168.1.94:8003/", Constants.HOST)){
//                        Constants.HOST="http://47.104.3.118:8003/";
//                        ToastUtils.showToastLong("已切换至正式版");
//                      //  about_verson.setText("正式版:"+ AppUtils.getVersionName(AboutUsActivity.this));
//                    }else if (TextUtils.equals("http://47.104.3.118:8003/", Constants.HOST)){
//                        Constants.HOST="http://192.168.1.94:8003/";
//                        ToastUtils.showToastLong("已切换至测试版");
//                       // about_verson.setText("测试版:"+ AppUtils.getVersionName(AboutUsActivity.this));
//                    }

                    break;
            }

        }
    };

}
