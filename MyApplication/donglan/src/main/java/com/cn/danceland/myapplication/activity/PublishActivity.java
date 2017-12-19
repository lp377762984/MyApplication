package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.google.gson.Gson;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

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

/**
 * Created by feng on 2017/10/23.
 * 发布动态
 */

public class PublishActivity extends Activity {
    TextView publish_cancel;
    TextView publish_ok;
    EditText publish_status;
    RelativeLayout publish_photo;
    TextView publish_location;
    TextView publish_share1;
    List<String> arrayList;
    GridView grid_view;
    String location="";
    TextView location_img;
    Map<String,File> arrayFileMap;
    String videoPath,videoUrl;
    final static int CAPTURE_VIDEO_CODE = 100;
    static String SAVED_IMAGE_DIR_PATH =
            Environment.getExternalStorageDirectory().getPath()
                    + "/donglan/camera/";// 拍照路径
    String stringstatus = "";
    //LocationClient mLocationClient;
    Gson gson;
    RequestQueue queue;
    ImageView videoimg;
    String picUrl,vedioUrl;
    String isPhoto;
    File picFile,videoFile;
    public static Handler handler;
    ArrayList<String> arrImgUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        queue = Volley.newRequestQueue(PublishActivity.this);
        gson = new Gson();
        isPhoto = getIntent().getStringExtra("isPhoto");
        initView();
        setOnclick();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 1){
                    PublishBean bean = new PublishBean();
                    if(!"".equals(stringstatus)){
                        bean.setContent(stringstatus);
                    }
                    if(picUrl!=null&&vedioUrl!=null){
                        bean.setVedioUrl(vedioUrl);
                        bean.setVedioImg(picUrl);
                        bean.setMsgType(1);
                    }
                    bean.setPublishPlace(location);
                    if(bean.getVedioUrl()==null&&bean.getContent()==null){
                        ToastUtils.showToastShort("请填写需要发布的动态！");
                    }else{
                        try {
                            commitUrl(gson.toJson(bean));
                       //     EventBus.getDefault().post(new StringEvent("", EventConstants.ADD_DYN));
                           //finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }else if(msg.what == 2){
                    PublishBean bean = new PublishBean();
                    if(!"".equals(stringstatus)){
                        bean.setContent(stringstatus);
                    }
                    if(arrImgUrl!=null&&arrImgUrl.size()>0){
                        bean.setImgList(arrImgUrl);
                        bean.setMsgType(0);
                    }
                    bean.setPublishPlace(location);
                    if(bean.getContent()==null && bean.getImgList()==null){
                        ToastUtils.showToastShort("请填写需要发布的动态！");
                    }else{
                        String strBean = gson.toJson(bean);
                        LogUtil.e("zzf",strBean);
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
        publish_photo.setOnClickListener(onClickListener);
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
        publish_photo = findViewById(R.id.publish_photo);
        publish_location = findViewById(R.id.publish_location);
        publish_share1 = findViewById(R.id.publish_share1);
        grid_view = findViewById(R.id.grid_view);
        videoimg = findViewById(R.id.videoimg);
        videoimg.setOnClickListener(onClickListener);
        grid_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getPic();
            }
        });

    }
    public void getPic(){
        Matisse.from(PublishActivity.this)
                .choose(MimeType.allOf()) // 选择 mime 的类型
                .countable(true)
                .capture(true)
                .captureStrategy(
                        new CaptureStrategy(true, "com.cn.danceland.myapplication.Receiver.MyFileProvider"))
                .maxSelectable(9) // 图片选择的最多数量
                .theme(R.style.imgsStyle)
                //.gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f) // 缩略图的比例
                .imageEngine(new PicassoEngine()) // 使用的图片加载引擎
                .forResult(0); // 设置作为标记的请求码

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.publish_photo:
                    if("0".equals(isPhoto)){
                        getPic();
                    }else{
                        Intent intentr = new Intent(PublishActivity.this,RecordView.class);
                        startActivityForResult(intentr,111);
                    }
                    break;
                case R.id.videoimg:
                    Intent intentr = new Intent(PublishActivity.this,RecordView.class);
                    startActivityForResult(intentr,111);
                    break;
                case R.id.location_img:
                    Intent intent1 = new Intent(PublishActivity.this,LocationActivity.class);
                    startActivityForResult(intent1,1);
                    break;
                case R.id.publish_location:
                    Intent intent2 = new Intent(PublishActivity.this,LocationActivity.class);
                    startActivityForResult(intent2,1);
                    break;
                case R.id.publish_ok:
                    //Intent intent3 = new Intent(PublishActivity.this,S);
                    final PublishBean publishBean = new PublishBean();
                    stringstatus = publish_status.getText().toString();

                    if("0".equals(isPhoto)){
                        if(arrayList!=null&&arrayList.size()>0){
                            MultipartRequestParams params = new MultipartRequestParams();
                            arrayFileMap = new HashMap<String,File>();
                            File[] files = new File[arrayList.size()];
                            for (int i =0;i<arrayList.size();i++){
                                File file = new File(arrayList.get(i));
                                arrayFileMap.put(i+"",file);
                            }
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        String s=   UpLoadUtils.postUPloadIamges(Constants.UPLOAD_FILES_URL,null,arrayFileMap);
                                        LogUtil.e("zzf",s);
                                        UpImagesBean upImagesBean = gson.fromJson(s, UpImagesBean.class);
                                        List<UpImagesBean.Data> beanList = upImagesBean.getData();

                                        arrImgUrl = new ArrayList<String>();
                                        if(beanList!=null&&beanList.size()>0){
                                            for(int k = 0;k<beanList.size();k++){
                                                arrImgUrl.add(beanList.get(k).getImgUrl());
                                            }
                                        }

//                                        publishBean.setContent(stringstatus);
//                                        publishBean.setPublishPlace(location);
//                                        if(arrImgUrl!=null&&arrImgUrl.size()>0){
//                                            publishBean.setImgList(arrImgUrl);
//                                        }
                                        Message message = new Message();
                                        message.what = 2;
                                        handler.sendMessage(message);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                            finish();
//                                LogUtil.e("zzf",publishBean.getImgList().toString());
                            publish_ok.setClickable(false);
                        }else{
                            if(!"".equals(stringstatus)){
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
                            }else{
                                ToastUtils.showToastShort("请填写需要发布的动态！");

                            }

                        }
                    }else{

                        if(videoPath!=null&&!"".equals(videoPath)){
                            videoFile = new File(videoPath);
                            if(picFile!=null){
                                MultipartRequestParams params = new MultipartRequestParams();
                                params.put("file",picFile);
                                MultipartRequest request = new MultipartRequest(Request.Method.POST, params, Constants.UPLOADTH, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String s) {
                                        HeadImageBean headImageBean = gson.fromJson(s, HeadImageBean.class);
                                        if(headImageBean!=null&&headImageBean.getData()!=null){
                                            picUrl = headImageBean.getData().getImgUrl();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {

                                    }
                                });
                                MyApplication.getHttpQueues().add(request);
                            }
                            if(videoFile!=null){
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            HashMap<String,File> fileHashMap = new HashMap<String,File>();
                                            fileHashMap.put("vedio",videoFile);
                                            String s=   UpLoadUtils.postUPloadIamges(Constants.UPLOADVEDIO,null,fileHashMap);
                                            VideoBean videoBean = gson.fromJson(s, VideoBean.class);
                                            if(videoBean!=null&&videoBean.getData()!=null){
                                                vedioUrl = videoBean.getData().getImgUrl();
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
                        }
                    }

                    break;
                case R.id.publish_cancel:
                    finish();
                    break;
            }
        }
    };

    /**
     * 获得视频的缩略图
     *
     *
     */
    public Bitmap getBitmap(String imgPath) {

        Bitmap bp = ThumbnailUtils.createVideoThumbnail(imgPath,
                MediaStore.Video.Thumbnails.MINI_KIND);
        return bp;
    }


    public void commitUrl(final String str) throws JSONException {

        JSONObject jsonObject = new JSONObject(str);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,Constants.SAVE_DYN_MSG,jsonObject,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                RootBean rootBean = gson.fromJson(jsonObject.toString(), RootBean.class);
                if("true".equals(rootBean.success)){
                    ToastUtils.showToastShort("发布成功！");
                    EventBus.getDefault().post(new StringEvent("", EventConstants.ADD_DYN));
                    //finish();
                }else{
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
                HashMap<String,String> map  = new HashMap<String,String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN,""));
                return map;
            }
        };
        queue.add(stringRequest);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0 && resultCode == RESULT_OK){
            if(data!=null){
//                arrayList = data.getStringArrayListExtra("arrPath");
                List<Uri> uris = Matisse.obtainResult(data);
                arrayList = new ArrayList<String>();
                if(uris!=null){
                    for(int i = 0;i<uris.size();i++){
                        arrayList.add(PictureUtil.getRealPath(getApplicationContext(),uris.get(i)));
                    }
                    if(arrayList!=null&&arrayList.size()!=0){
                        publish_photo.setVisibility(View.GONE);
                    }else{
                        publish_photo.setVisibility(View.VISIBLE);
                    }
                    grid_view.setAdapter(new SmallGridAdapter(PublishActivity.this,arrayList));
                }

            }
        }else if(resultCode==1){
                location = data.getStringExtra("location");
                publish_location.setText(location);
        }else if(resultCode == 111){
            videoPath = data.getStringExtra("videoPath");
            if(videoPath!=null){
                MediaMetadataRetriever media = new MediaMetadataRetriever();
                media.setDataSource(videoPath);
                Bitmap frameAtTime = media.getFrameAtTime();
                picFile = saveBitmapFile(frameAtTime);
                videoimg.setImageBitmap(frameAtTime);
                publish_photo.setVisibility(View.GONE);
                videoimg.setVisibility(View.VISIBLE);
            }

        }

    }

    public File saveBitmapFile(Bitmap bitmap) {
        File file=new File(Environment.getExternalStorageDirectory().getPath()
                + "/donglan/camera/"+System.currentTimeMillis()+".png");//将要保存图片的路径
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
        }catch (IOException e){
            e.printStackTrace();
        }
        return file;
    }


    public class SmallGridAdapter extends BaseAdapter{

        LayoutInflater mInflater;
        Context context;
        List<String> arrayList;

        SmallGridAdapter(Context context,List<String> asList){
            this.context = context;
            arrayList = asList;
            mInflater = LayoutInflater.from(context);
        }


        @Override
        public int getCount() {
            return arrayList.size();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder=null;
            if(convertView==null){
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.images_item, null);
                viewHolder.img = convertView.findViewById(R.id.image_item);
                viewHolder.item_select = convertView.findViewById(R.id.item_select);
                viewHolder.item_select.setVisibility(View.GONE);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            viewHolder.img.setMaxHeight(windowManager.getDefaultDisplay().getWidth()/4);
            viewHolder.img.setMaxWidth(windowManager.getDefaultDisplay().getWidth()/4);
            Glide.with(context).load(arrayList.get(position)).into(viewHolder.img);
            //viewHolder.img.setImageBitmap(PictureUtil.getSmallBitmap(arrayList.get(position),windowManager.getDefaultDisplay().getWidth()/4,windowManager.getDefaultDisplay().getWidth()/4));

            return convertView;
        }
    }

    class ViewHolder{
        ImageView img,item_select;
    }

}
