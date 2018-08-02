package com.cn.danceland.myapplication.shouhuan.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cn.danceland.myapplication.R;

/**
 * 摇摇拍照
 * Created by yxx on 2018/7/18.
 */

public class WearFitCameraActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wearfit_camera);
        initView();
    }
    private void initView() {

    }
}
