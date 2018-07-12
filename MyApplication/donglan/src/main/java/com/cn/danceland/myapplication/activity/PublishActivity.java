package com.cn.danceland.myapplication.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.HeadImageBean;
import com.cn.danceland.myapplication.bean.PublishBean;
import com.cn.danceland.myapplication.bean.RootBean;
import com.cn.danceland.myapplication.bean.UpImagesBean;
import com.cn.danceland.myapplication.bean.VideoBean;
import com.cn.danceland.myapplication.evntbus.EventConstants;
import com.cn.danceland.myapplication.evntbus.StringEvent;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.PictureUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.cn.danceland.myapplication.utils.UpLoadUtils;
import com.cn.danceland.myapplication.utils.multipartrequest.MultipartRequest;
import com.cn.danceland.myapplication.utils.multipartrequest.MultipartRequestParams;
import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.google.gson.Gson;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by feng on 2017/10/23.
 * 发布动态
 */

public class PublishActivity extends Activity {
    TextView publish_cancel;
    TextView publish_ok;
    hani.momanii.supernova_emoji_library.Helper.EmojiconEditText publish_status;
    RelativeLayout publish_photo, rl_video;
    TextView publish_location;
    TextView publish_share1;
    List<String> arrayList = new ArrayList<String>();
    GridView grid_view;
    String location = "";
    TextView location_img;
    Map<String, File> arrayFileMap;
    String videoPath, videoUrl;
    String cameraPath;
    final static int CAPTURE_VIDEO_CODE = 100;
    static String SAVED_IMAGE_DIR_PATH =
            Environment.getExternalStorageDirectory().getPath()
                    + "/donglan/camera/";// 拍照路径
    static String SAVED_IMAGE_PATH = Environment.getExternalStorageDirectory().getPath()
            + "/DCIM/Donglan/";
    String stringstatus = "";
    //LocationClient mLocationClient;
    Gson gson;
    RequestQueue queue;
    Button videoimg;
    String picUrl, picPath, vedioUrl, vedioPath;
    String isPhoto;
    File picFile, videoFile;
    public static Handler handler;
    ArrayList<String> arrImgUrl, arrImgPath;
    Uri uri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        queue = Volley.newRequestQueue(PublishActivity.this);
        gson = new Gson();
        isPhoto = getIntent().getStringExtra("isPhoto");
        if (isPhoto == null) {
            isPhoto = "999";
        }
        initView();
        setOnclick();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    PublishBean bean = new PublishBean();
                    if (!"".equals(stringstatus)) {
                        bean.setContent(stringstatus);
                    }
                    if (picPath != null && vedioPath != null) {
                        bean.setVedioUrl(vedioPath);
                        bean.setVedioImg(picPath);
                    }
                    bean.setMsgType(1);
                    bean.setPublishPlace(location);
                    if (bean.getVedioUrl() == null && bean.getContent() == null) {
                        ToastUtils.showToastShort("请填写需要发布的动态！");
                    } else {
                        try {
                            LogUtil.i(gson.toJson(bean));
                            commitUrl(gson.toJson(bean));
                            //     EventBus.getDefault().post(new StringEvent("", EventConstants.ADD_DYN));
                            //finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                } else if (msg.what == 2) {
                    PublishBean bean = new PublishBean();
                    if (!"".equals(stringstatus)) {
                        bean.setContent(stringstatus);
                    }
                    if (arrImgPath != null && arrImgPath.size() > 0) {
                        bean.setImgList(arrImgPath);
                        bean.setMsgType(0);
                    }
                    bean.setPublishPlace(location);
                    if (bean.getContent() == null && bean.getImgList() == null) {
                        ToastUtils.showToastShort("请填写需要发布的动态！");
                    } else {
                        String strBean = gson.toJson(bean);
                        LogUtil.i(gson.toJson(bean).toString());
                        try {
                            commitUrl(strBean);
                            //LogUtil.e("zzf",strBean);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //    EventBus.getDefault().post(new StringEvent("", EventConstants.ADD_DYN));
                        //finish();
                    }
                }
            }
        };

    }

    private void setOnclick() {
        //publish_photo.setOnClickListener(onClickListener);
        publish_location.setOnClickListener(onClickListener);
        location_img.setOnClickListener(onClickListener);
        publish_ok.setOnClickListener(onClickListener);
        publish_cancel.setOnClickListener(onClickListener);
    }

    private void initView() {
        publish_cancel = findViewById(R.id.publish_cancel);
        publish_ok = findViewById(R.id.publish_ok);
        publish_ok.setClickable(true);
        location_img = findViewById(R.id.location_img);
        publish_status = findViewById(R.id.publish_status);
        //publish_photo = findViewById(R.id.publish_photo);
        publish_location = findViewById(R.id.publish_location);
        //publish_share1 = findViewById(R.id.publish_share1);
        SPUtils.setInt("imgN", 0);
        grid_view = findViewById(R.id.grid_view);
        grid_view.setAdapter(new SmallGridAdapter(PublishActivity.this, arrayList));

        grid_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getPic();
            }
        });

    }

    public void getPic() {
        int m = SPUtils.getInt("imgN", 0);
        if (m <= 9) {
            Matisse.from(PublishActivity.this)
                    .choose(MimeType.allOf()) // 选择 mime 的类型
                    .countable(true)
//                .capture(true)
//                .captureStrategy(
//                        new CaptureStrategy(true, "com.cn.danceland.myapplication.fileprovider"))
                    .maxSelectable(9 - m) // 图片选择的最多数量
                    .theme(R.style.imgsStyle)
                    //.gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .thumbnailScale(0.85f) // 缩略图的比例
                    .imageEngine(new PicassoEngine()) // 使用的图片加载引擎
                    .forResult(100); // 设置作为标记的请求码
        }

    }

    long length;
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.location_img:
                    Intent intent1 = new Intent(PublishActivity.this, LocationActivity.class);
                    startActivityForResult(intent1, 1);
                    break;
                case R.id.publish_location:
                    Intent intent2 = new Intent(PublishActivity.this, LocationActivity.class);
                    startActivityForResult(intent2, 1);
                    break;
                case R.id.publish_ok:
                    //Intent intent3 = new Intent(PublishActivity.this,S);
                    final PublishBean publishBean = new PublishBean();
                    stringstatus = publish_status.getText().toString();

                    if ("0".equals(isPhoto)) {
                        if (arrayList != null && arrayList.size() > 0) {
                            MultipartRequestParams params = new MultipartRequestParams();

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    //try {
                                    arrayFileMap = new HashMap<>();
                                    ArrayList<File> files = new ArrayList<>();
                                    for (int i = 0; i < arrayList.size(); i++) {
                                        File file = new File(arrayList.get(i));
                                        files.add(file);
                                    }
                                    compressImg(files);//压缩图片

                                }
                            }).start();
                            finish();
//                                LogUtil.e("zzf",publishBean.getImgList().toString());
                            publish_ok.setClickable(false);
                        } else {
                            if (!"".equals(stringstatus)) {
                                publish_ok.setClickable(false);
                                publishBean.setContent(stringstatus);
                                publishBean.setPublishPlace(location);
                                String strBean = gson.toJson(publishBean);
                                try {
                                    commitUrl(strBean);
                                    //LogUtil.e("zzf",strBean);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                finish();
                            } else {
                                ToastUtils.showToastShort("请填写需要发布的动态！");

                            }

                        }
                    } else {

                        if (videoPath != null && !"".equals(videoPath)) {
                            videoFile = new File(videoPath);
                            if (picFile != null) {
                                MultipartRequestParams params = new MultipartRequestParams();
                                params.put("file", picFile);
                                MultipartRequest request = new MultipartRequest(Request.Method.POST, params, Constants.UPLOADTH, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String s) {
                                        HeadImageBean headImageBean = gson.fromJson(s, HeadImageBean.class);
                                        if (headImageBean != null && headImageBean.getData() != null) {
                                            picUrl = headImageBean.getData().getImgUrl();
                                            picPath = headImageBean.getData().getImgPath();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {

                                    }
                                });
                                MyApplication.getHttpQueues().add(request);
                            }
                            if (videoFile != null) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            HashMap<String, File> fileHashMap = new HashMap<String, File>();
                                            fileHashMap.put("vedio", videoFile);
                                            String s = UpLoadUtils.postUPloadIamges(Constants.UPLOADVEDIO, null, fileHashMap);
                                            VideoBean videoBean = gson.fromJson(s, VideoBean.class);
                                            if (videoBean != null && videoBean.getData() != null) {
                                                vedioUrl = videoBean.getData().getImgUrl();
                                                vedioPath = videoBean.getData().getImgPath();
                                                Message message = new Message();
                                                message.what = 1;
                                                handler.sendMessage(message);
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                                finish();
                                publish_ok.setClickable(false);
                            }
                        } else {
                            PublishBean bean = new PublishBean();
                            if (!"".equals(stringstatus)) {
                                bean.setContent(stringstatus);
                            }
                            bean.setPublishPlace(location);
                            if (bean.getVedioUrl() == null && bean.getContent() == null) {
                                ToastUtils.showToastShort("请填写需要发布的动态！");
                            } else {
                                try {
                                    commitUrl(gson.toJson(bean));
                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }

                    break;
                case R.id.publish_cancel:
                    finish();
                    break;
            }
        }
    };

    private void showListDialog() {
        final String[] items = {"拍摄", "从相册选择"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(PublishActivity.this);
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    if (SPUtils.getInt("imgN", 0) < 9) {
                        //showCamera();
                        startActivityForResult(new Intent(PublishActivity.this, ShowCameraActivity.class).putExtra("isPhoto", isPhoto), 102);
                        //startActivity(new Intent(PublishActivity.this,ShowCameraActivity.class));
                    } else {
                        ToastUtils.showToastShort("最多选择9张图片");
                    }
                } else {
                    if ("1".equals(isPhoto)) {
                        arrayList.clear();
                    }
                    isPhoto = "0";
                    getPic();
                }
            }
        });
        listDialog.show();
    }

    private void showCamera() {
// 指定相机拍摄照片保存地址
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            cameraPath = Environment.getExternalStorageDirectory().getPath()
                    + "/DCIM/Donglan/" + System.currentTimeMillis() + ".png";
            Intent intent = new Intent();
            // 指定开启系统相机的Action
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            String out_file_path = SAVED_IMAGE_PATH;
            File dir = new File(out_file_path);
            if (!dir.exists()) {
                dir.mkdirs();
            } // 把文件地址转换成Uri格式
            if (PictureUtil.getSDKV() < 24) {
                uri = Uri.fromFile(new File(cameraPath));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, 99);
            } else {
                // 设置系统相机拍摄照片完成后图片文件的存放地址
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, cameraPath);
                uri = getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, 99);
            }
            //uri = Uri.fromFile(new File(cameraPath));

        } else {
            Toast.makeText(getApplicationContext(), "请确认已经插入SD卡",
                    Toast.LENGTH_LONG).show();
        }

    }

    public void commitUrl(final String str) throws JSONException {

        JSONObject jsonObject = new JSONObject(str);
        LogUtil.i(str);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Constants.SAVE_DYN_MSG, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtil.i(jsonObject.toString());
                RootBean rootBean = gson.fromJson(jsonObject.toString(), RootBean.class);
                if ("true".equals(rootBean.success)) {
                    ToastUtils.showToastShort("发布成功！");
                    EventBus.getDefault().post(new StringEvent("", EventConstants.ADD_DYN));
                    //finish();
                } else {
                    ToastUtils.showToastShort("发布失败！请检查网络连接");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                ToastUtils.showToastShort(volleyError.toString());

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));
                LogUtil.e("zzf", SPUtils.getString(Constants.MY_TOKEN, ""));
                return map;
            }
        };
        MyApplication.getHttpQueues().add(stringRequest);

    }

    int maptag = 0;

    //压缩图片
    private void compressImg(final List<File> files) {
        List<String> paths = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
//            try {
//                LogUtil.i("压缩前大小" + SDCardUtils.formatFileSize(files.get(i)));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            paths.add(files.get(i).getAbsolutePath());
        }
        Luban.with(this)
                .load(paths)                                   // 传人要压缩的图片列表
                .ignoreBy(100)                                  // 忽略不压缩图片的大小
                //   .setTargetDir(Environment.getExternalStorageDirectory().getAbsolutePath())                        // 设置压缩后文件存储位置
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                        //      LogUtil.i("压缩开始前调用");
                    }

                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        //         LogUtil.i(file.getAbsolutePath());
                        arrayFileMap.put(maptag + "", file);
                        maptag++;
//                        try {
//                            LogUtil.i("压缩后大小" + SDCardUtils.formatFileSize(file));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }

                        if (files.size() == maptag) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String s = null;
                                    try {
                                        s = UpLoadUtils.postUPloadIamges(Constants.UPLOAD_FILES_URL, null, arrayFileMap);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    UpImagesBean upImagesBean = gson.fromJson(s, UpImagesBean.class);
                                    if (upImagesBean.getSuccess()) {
                                        List<UpImagesBean.Data> beanList = upImagesBean.getData();

                                        arrImgUrl = new ArrayList<String>();
                                        arrImgPath = new ArrayList<String>();
                                        if (beanList != null && beanList.size() > 0) {
                                            for (int k = 0; k < beanList.size(); k++) {
                                                arrImgUrl.add(beanList.get(k).getImgUrl());
                                                arrImgPath.add(beanList.get(k).getImgPath());
                                            }
                                        }

                                        Message message = new Message();
                                        message.what = 2;
                                        handler.sendMessage(message);
                                        maptag = 0;
                                    } else {
                                        LogUtil.i("图片上传失败");
                                    }

                                }
                            }).start();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                        LogUtil.i("压缩过程出现问题时调用" + e.toString());
                    }
                }).launch();    //启动压缩


//        Luban.compress(this,files)
//                .setMaxSize(500)                // limit the final image size（unit：Kb）
////                .setMaxHeight(1920)             // limit image height
////                .setMaxWidth(1080)              // limit image width
//                .putGear(Luban.CUSTOM_GEAR)     // use CUSTOM GEAR compression mode
//                .launch(new OnMultiCompressListener() {
//                    @Override
//                    public void onStart() {
//                        LogUtil.i("开始压缩");
//                    }
//
//                    @Override
//                    public void onSuccess(List<File> fileList) {
//                        int size = fileList.size();
//                        for(int i=0;i<size;i++){
//                            arrayFileMap.put(i + "", fileList.get(i));
//                            try {
//                                LogUtil.i("压缩后大小" + SDCardUtils.formatFileSize(fileList.get(i)));
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        for(int j = 0;j<arrayFileMap.size();j++){
//                            LogUtil.i(arrayFileMap.get(j+"").length()+"");
//                        }
//
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                String s = null;
//                                try {
//                                    s = UpLoadUtils.postUPloadIamges(Constants.UPLOAD_FILES_URL, null, arrayFileMap);
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                                UpImagesBean upImagesBean = gson.fromJson(s, UpImagesBean.class);
//                                List<UpImagesBean.Data> beanList = upImagesBean.getData();
//
//                                arrImgUrl = new ArrayList<String>();
//                                arrImgPath = new ArrayList<String>();
//                                if (beanList != null && beanList.size() > 0) {
//                                    for (int k = 0; k < beanList.size(); k++) {
//                                        arrImgUrl.add(beanList.get(k).getImgUrl());
//                                        arrImgPath.add(beanList.get(k).getImgPath());
//                                    }
//                                }
//
//                                Message message = new Message();
//                                message.what = 2;
//                                handler.sendMessage(message);
//                            }
//                        }).start();
//
//
//                    }
//
//                    @Override
//                    public void onError(Throwable throwable) {
//                        LogUtil.i("压缩失败" + throwable.toString());
//                    }
//                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            if (data != null) {
//                arrayList = data.getStringArrayListExtra("arrPath");
                List<Uri> uris = Matisse.obtainResult(data);

                if (uris != null) {
                    for (int i = 0; i < uris.size(); i++) {
                        arrayList.add(PictureUtil.getRealPath(getApplicationContext(), uris.get(i)));
                    }

                    SPUtils.setInt("imgN", arrayList.size() + SPUtils.getInt("imgN", 0));
                    grid_view.setAdapter(new SmallGridAdapter(PublishActivity.this, arrayList));
                }

            }
        } else if (resultCode == 99) {
            isPhoto = "0";
            cameraPath = data.getStringExtra("picpath");
            if (cameraPath != null) {
                arrayList.add(cameraPath);
                SPUtils.setInt("imgN", 1 + SPUtils.getInt("imgN", 0));
                grid_view.setAdapter(new SmallGridAdapter(PublishActivity.this, arrayList));
            }
        } else if (resultCode == 1) {
            location = data.getStringExtra("location");
            publish_location.setText(location);
        } else if (resultCode == 111) {
            isPhoto = "1";
            videoPath = data.getStringExtra("videoPath");
            arrayList.clear();
            if (videoPath != null) {
                MediaMetadataRetriever media = new MediaMetadataRetriever();
                media.setDataSource(videoPath);
                Bitmap frameAtTime = media.getFrameAtTime();
                picFile = saveBitmapFile(frameAtTime);
                arrayList.add(picFile.getAbsolutePath());
                grid_view.setAdapter(new SmallGridAdapter(PublishActivity.this, arrayList));
//                videoimg.setBackground(new BitmapDrawable(frameAtTime));
//                //publish_photo.setVisibility(View.GONE);
//                videoimg.setVisibility(View.VISIBLE);
            }

        }

    }

    public File saveBitmapFile(Bitmap bitmap) {
        File file = new File(Environment.getExternalStorageDirectory().getPath()
                + "/donglan/camera/" + System.currentTimeMillis() + ".png");//将要保存图片的路径
        File dir = new File(Environment.getExternalStorageDirectory().getPath()
                + "/donglan/camera/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    public class SmallGridAdapter extends BaseAdapter {

        LayoutInflater mInflater;
        Context context;
        List<String> arrayLists;
        int imgN;

        SmallGridAdapter(Context context, List<String> asList) {
            imgN = SPUtils.getInt("imgN", 0);
            this.context = context;
            arrayLists = asList;
            mInflater = LayoutInflater.from(context);
        }


        @Override
        public int getCount() {
            return arrayLists.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.images_item, null);
                viewHolder.img = convertView.findViewById(R.id.image_item);
                viewHolder.rl_item = convertView.findViewById(R.id.rl_item);
                viewHolder.pl_sta = convertView.findViewById(R.id.pl_sta);
                viewHolder.item_select = convertView.findViewById(R.id.item_select);
                viewHolder.item_select.setVisibility(View.GONE);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if ("0".equals(isPhoto)) {
                if (arrayLists.size() == 0) {
                    viewHolder.pl_sta.setVisibility(View.VISIBLE);
                } else {
                    if (position < arrayLists.size()) {
                        Glide.with(context).load(arrayLists.get(position)).into(viewHolder.img);
                        viewHolder.pl_sta.setVisibility(View.GONE);
                    } else if (position == arrayLists.size()) {
                        viewHolder.pl_sta.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                if (arrayLists.size() == 0) {
                    viewHolder.pl_sta.setVisibility(View.VISIBLE);
                } else {
                    if (position < arrayLists.size()) {
                        Glide.with(context).load(arrayLists.get(position)).into(viewHolder.img);
                        viewHolder.pl_sta.setVisibility(View.GONE);
                    } else if (position == arrayLists.size()) {
                        viewHolder.rl_item.setVisibility(View.GONE);
                    }
                }
            }
            viewHolder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (PermissionsUtil.hasPermission(PublishActivity.this, Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO)) {
                        //有权限
                        if (imgN <= 9) {
                            if (position <= arrayLists.size()) {
                                if ("0".equals(isPhoto)) {
                                    showListDialog();
                                } else {
                                    showListDialog();
                                }
                            }
                        } else {
                            ToastUtils.showToastShort("最多选择9张图片");
                        }
                    } else {
                        PermissionsUtil.requestPermission(PublishActivity.this, new PermissionListener() {
                            @Override
                            public void permissionGranted(@NonNull String[] permissions) {
                                //用户授予了权限
                                if (imgN <= 9) {
                                    if (position <= arrayLists.size()) {
                                        if ("0".equals(isPhoto)) {
                                            showListDialog();
                                        } else {
                                            showListDialog();
                                        }
                                    }
                                } else {
                                    ToastUtils.showToastShort("最多选择9张图片");
                                }
                            }

                            @Override
                            public void permissionDenied(@NonNull String[] permissions) {
                                //用户拒绝了申请
                                ToastUtils.showToastShort("没有权限");
                            }
                        }, new String[]{Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO}, false, null);
                    }
                }
            });


            return convertView;
        }
    }

    class ViewHolder {
        ImageView img, item_select;
        TextView pl_sta;
        RelativeLayout rl_item;
    }

}
