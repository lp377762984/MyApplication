package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cn.danceland.myapplication.R;

/**
 * Created by shy on 2017/11/29 13:19
 * Email:644563767@qq.com
 */


public class NewsDetailsActivity extends Activity implements View.OnClickListener {
    private String mUrl;
    private ProgressBar mProgress;
    private WebView mWebView;
    private String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_detail);

        initView();
        initData();
    }

    private void initView() {
        mUrl = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        findViewById(R.id.tv_colse).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        TextView tv_tiltle = findViewById(R.id.tv_tiltle);
        tv_tiltle.setText(title);
        mProgress = findViewById(R.id.myProgressBar);
        mWebView = findViewById(R.id.wv_webview);

        if (!TextUtils.isEmpty(mUrl)) {
            WebSettings settings = mWebView.getSettings();

            settings.setJavaScriptEnabled(true);// 打开js功能
            settings.setBuiltInZoomControls(true);// 显示放大缩小的按钮
            settings.setUseWideViewPort(true);// 双击缩放

            mWebView.setWebChromeClient(new WebChromeClient() {

                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    if (newProgress == 100) {
                        mProgress.setVisibility(View.INVISIBLE);
                    } else {
                        if (View.INVISIBLE == mProgress.getVisibility()) {
                            mProgress.setVisibility(View.VISIBLE);
                        }
                        mProgress.setProgress(newProgress);
                    }
                    super.onProgressChanged(view, newProgress);
                }

            });


            mWebView.setWebViewClient(new WebViewClient() {

                // 监听网页加载结束的事件
                @Override
                public void onPageFinished(WebView view, String url) {
                    //  mProgress.setVisibility(View.GONE);
                }
            });

            mWebView.loadUrl(mUrl);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_back:
                if (mWebView.canGoBack()) {
                    mWebView.goBack();// 返回前一个页面
                } else {
                    finish();
                }
                break;
            case R.id.tv_colse:
                finish();
                break;
//            case R.id.iv_back:
//                break;
            default:
                break;
        }

    }
}
