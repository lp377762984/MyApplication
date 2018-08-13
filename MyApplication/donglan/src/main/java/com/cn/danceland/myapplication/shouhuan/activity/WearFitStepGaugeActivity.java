package com.cn.danceland.myapplication.shouhuan.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.view.DongLanTitleView;
import com.cn.danceland.myapplication.view.HistoGram;

/**
 * 计步
 * Created by yxx on 2018/8/13.
 */

public class WearFitStepGaugeActivity extends Activity {
    private Context context;
    private DongLanTitleView title;//心率title
    PopupWindow mPopupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_gauge);
        context = this;
        initView();
    }

    private void initView() {
        title = findViewById(R.id.shouhuan_title);
        title.setTitle(context.getResources().getString(R.string.step_gauge_text));
    }

}
