package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.db.DBData;
import com.cn.danceland.myapplication.utils.AppUtils;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.SPUtils;

import java.io.IOException;


/**
 * 开屏页
 */
public class SplashActivity extends Activity {

    private static final int sleepTime = 2000;


    @Override
    protected void onCreate(Bundle arg0) {
        setContentView(R.layout.activity_splash);
        super.onCreate(arg0);

        RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.splash_root);
        TextView versionText = (TextView) findViewById(R.id.tv_version);
        versionText.setText("1.0.1");
        	versionText.setText(AppUtils.getVersionName(this));


        AlphaAnimation animation = new AlphaAnimation(1.0f, 1.0f);
        animation.setDuration(1500);
        rootLayout.startAnimation(animation);
    }

    @Override
    protected void onStart() {
        super.onStart();

        new Thread(new Runnable() {
            public void run() {
                if (!SPUtils.getBoolean("iscopy",false)){
                    copyDb();
                }
                //判断是否登录
                if (SPUtils.getBoolean(Constants.ISLOGINED, false)) {

                    long start = System.currentTimeMillis();



//                    if (!TextUtils.isEmpty(PrefUtils.getString(SplashActivity.this, Constants.MY_ID, "")) && !TextUtils.isEmpty
//                            (PrefUtils.getString(SplashActivity.this, Constants.MY_MIPUSHID, ""))) {
//                        bindMIPushID_post();
//                    }
                    long costTime = System.currentTimeMillis() - start;
                    //wait
                    if (sleepTime - costTime > 0) {
                        try {
                            Thread.sleep(sleepTime - costTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //已经登录，进入主界面
                 
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));

                    finish();
                } else {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                    }
            
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }).start();

    }

    public void copyDb(){

        try {
            boolean bl = DBData.copyRawDBToApkDb(SplashActivity.this,R.raw.donglan,"/data/data/com.cn.danceland.myapplication/databases/","donglan.db",false);

            if (bl){
                SPUtils.setBoolean("iscopy",bl);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * get sdk version
     */
//    private String getVersion() {
//        return "3.4";
//    }
}
