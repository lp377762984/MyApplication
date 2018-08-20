package com.cn.danceland.myapplication.shouhuan.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.shouhuan.utils.OnColumnClickListener;
import com.cn.danceland.myapplication.view.ColumnView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 摇摇拍照
 * Created by yxx on 2018/7/18.
 */

public class WearFitCameraActivity extends Activity {
    private Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wearfit_camera);
        context=this;
        initView();
    }
    private void initView() {
//        zzt1 = (ColumnView) findViewById(R.id.zzt1);
    }
}
