package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.cn.danceland.myapplication.R;

/**
 * Created by feng on 2017/11/13.
 */

public class AboutUsActivity extends Activity {

    ImageView about_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.about_back:
                    finish();
                    break;
            }

        }
    };


    private void initView() {
        about_back = findViewById(R.id.about_back);
        about_back.setOnClickListener(onClickListener);
    }
}
