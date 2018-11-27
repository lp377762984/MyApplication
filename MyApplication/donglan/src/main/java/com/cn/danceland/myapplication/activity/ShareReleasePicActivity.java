//package com.cn.danceland.myapplication.activity;
//
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.util.ArrayList;
//import java.util.List;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.support.v4.view.ViewPager;
//import android.support.v4.view.ViewPager.OnPageChangeListener;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import com.cn.danceland.myapplication.R;
//import com.cn.danceland.myapplication.adapter.ViewPagerAdapter;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.assist.FailReason;
//import com.nostra13.universalimageloader.core.assist.ImageScaleType;
//import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
//import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
//import com.puji.youme.config.PJ_StaticConfig;
//import com.puji.youme.widget.photoview.PhotoView;
//
///**
// * 分享-图片查看
// *
// * @author YXX
// *
// */
//public class ShareReleasePicActivity extends BaseActivity {
//
//    private DisplayImageOptions Options;
//    private String mPhotos;
//    private ViewPager photos_pager;
//    private Context context;
//    private List<View> mView;
//    private int isPage = 0;
//    private TextView mTv;
//    private TextView okText;
//    private String[] reslut;
//    private ViewPagerAdapter adapter;
////    private  String path;
//    private View v;
//
//    @Override
//    protected void onCreate(Bundle arg0) {
//        super.onCreate(arg0);
//        context = this;
//        setContentView(R.layout.activity_share_pic);
//        mPhotos = getIntent().getStringExtra("Photos");
////        path = context.getCacheDir().getAbsolutePath() + File.separator;
//        photos_pager = (ViewPager) findViewById(R.id.photos_pager);
//        mTv = (TextView) findViewById(R.id.number);
//        okText = (TextView) findViewById(R.id.sharePicOkT);
//        okText.setText("完成");
//        okText.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ShareReleasePicActivity.this.finish();
//
//            }
//        });
//        initDisplayImage();
//        initData();
//
//    }
//
//    /**
//     * 初始化
//     */
//    public void initDisplayImage() {
//        if (Options == null) {
//            Options = new DisplayImageOptions.Builder()
//                    .cacheOnDisc(true)
//                    // 图片存本地
//                    .cacheInMemory(true)
//                    .displayer(new FadeInBitmapDisplayer(50))
//                    .bitmapConfig(Bitmap.Config.RGB_565)
//                    .imageScaleType(ImageScaleType.EXACTLY) // default
//                    .build();
//        }
//    }
//
//    /**
//     * 初始化数据
//     */
//    @SuppressLint("NewApi")
//	private void initData() {
//        reslut = mPhotos.split(",");
//        mView = new ArrayList<View>();
//        if (reslut != null && reslut.length > 0) {
//            for (int i = 0; i < reslut.length; i++) {
//                ImageView iv=new ImageView(this);
//                iv.setImageBitmap(getLoacalBitmap(reslut[i].toString()));
//                mView.add(iv);
//            }
//            mTv.setText("1/" + reslut.length);
//            adapter = new ViewPagerAdapter(context, mView);
//            photos_pager.setAdapter(adapter);
//            photos_pager.setOnPageChangeListener(new PageChangeListener());
//        }
//
//    }
//
//    /**
//     *
//     * @author ZQL
//     *
//     */
//    private final class PageChangeListener implements OnPageChangeListener {
//
//        @Override
//        public void onPageScrollStateChanged(int arg0) {
//        }
//
//        @Override
//        public void onPageScrolled(int arg0, float arg1, int arg2) {
//
//        }
//
//        @Override
//        public void onPageSelected(int arg0) {
//            isPage = arg0;
//            mTv.setText((isPage + 1) + "/" + reslut.length);
//        }
//    }
//
//    /**
//    * 加载本地图片
//    * http://bbs.3gstdy.com
//    * @param url
//    * @return
//    */
//    public static Bitmap getLoacalBitmap(String url) {
//         try {
//              FileInputStream fis = new FileInputStream(url);
//              return BitmapFactory.decodeStream(fis);
//         } catch (FileNotFoundException e) {
//              e.printStackTrace();
//              return null;
//         }
//    }
//}
