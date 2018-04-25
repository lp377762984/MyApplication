package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.utils.ToastUtils;

/**
 * Created by feng on 2017/11/13.
 */

public class AboutUsActivity extends Activity {

    ImageView about_back,about_logo;
    private TextView about_verson;
    long [] mHits = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        initView();
    }

    private void initView() {
        about_back = findViewById(R.id.about_back);
        about_logo = findViewById(R.id.about_logo);
        about_logo.setOnClickListener(onClickListener);
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
                case R.id.about_logo:
                    // 需要点击几次 就设置几
                    if (mHits == null) {
                        mHits = new long[5];
                    }
                    System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);//把从第二位至最后一位之间的数字复制到第一位至倒数第一位
                    mHits[mHits.length - 1] = SystemClock.uptimeMillis();//记录一个时间
                    if (SystemClock.uptimeMillis() - mHits[0] <= 1000) {//一秒内连续点击。
                        mHits = null;	//这里说明一下，我们在进来以后需要还原状态，否则如果点击过快，第六次，第七次 都会不断进来触发该效果。重新开始计数即可
//                        if (mShow) {
//                            mShow = false;
//                        } else {
//                            mShow = true;
                            ToastUtils.showToastShort("女友叫谢北北");
                        //}
                        //这里一般会把mShow存储到sp中。
                    }

                    break;
            }

        }
    };

}
