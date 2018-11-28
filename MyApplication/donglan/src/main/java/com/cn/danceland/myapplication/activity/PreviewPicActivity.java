package com.cn.danceland.myapplication.activity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.adapter.ViewPagerAdapter;

/**
 * 预览-图片查看 预览图片GIF等
 * Created by ${yxx} on 2018/11/27.
 */
public class PreviewPicActivity extends BaseActivity {
    private ArrayList<String> mPhotos = new ArrayList<>();
    private ViewPager photos_pager;
    private Context context;
    private List<View> mView;
    private int isPage = 0;
    private TextView mTv;
    private TextView okText;
    private ViewPagerAdapter adapter;
    private View v;
    private int lookIdx = 0;//图片下标（从第position张图片开始浏览）

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        context = this;
        setContentView(R.layout.activity_share_pic);
        mPhotos = getIntent().getStringArrayListExtra("photos");
        lookIdx = getIntent().getIntExtra("lookIdx", 0);//图片下标（从第position张图片开始浏览）
        photos_pager = findViewById(R.id.photos_pager);
        mTv = findViewById(R.id.number);
        okText = findViewById(R.id.sharePicOkT);
        okText.setText("完成");
        okText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PreviewPicActivity.this.finish();
            }
        });
        initData();

    }

    /**
     * 初始化数据
     */
    @SuppressLint("NewApi")
    private void initData() {
        mView = new ArrayList<View>();
        RequestOptions options1 = new RequestOptions()
                .placeholder(R.drawable.loading_img)//加载占位图
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .priority(Priority.HIGH);
        if (mPhotos != null && mPhotos.size() > 0) {
            for (int i = 0; i < mPhotos.size(); i++) {
                ImageView iv = new ImageView(this);
                Glide.with(context)
                        .load(mPhotos.get(i).toString())
                        .apply(options1)
                        .into(iv);
                mView.add(iv);
            }
            mTv.setText((lookIdx+1) + "/" + mPhotos.size());
            adapter = new ViewPagerAdapter(context, mView);
            photos_pager.setAdapter(adapter);
            photos_pager.setOnPageChangeListener(new PageChangeListener());
            photos_pager.setCurrentItem(lookIdx);
        }
    }

    /**
     * @author ZQL
     */
    private final class PageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            isPage = arg0;
            mTv.setText((isPage + 1) + "/" + mPhotos.size());
        }
    }
}
