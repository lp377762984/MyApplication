package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
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
    RelativeLayout publish_photo,rl_video;
    TextView publish_location;
    TextView publish_share1;
    List<String> arrayList = new ArrayList<String>();
    GridView grid_view;
    String location="";
    TextView location_img;
    Map<String,File> arrayFileMap;
    String videoPath,videoUrl;
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
    String picUrl,vedioUrl;
    String isPhoto;
    File picFile,videoFile;
    public static Handler handler;
    ArrayList<String> arrImgUrl;
    Uri uri;

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
        publish_share1 = findViewById(R.id.publish_share1);
        SPUtils.setInt("imgN",0);
        grid_view = findViewById(R.id.grid_view);
        grid_view.setAdapter(new SmallGridAdapter(PublishActivity.this,arrayList));

        grid_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getPic();
            }
        });

    }
    public void getPic(){
        int m = SPUtils.getInt("imgN",0);
        if(m<=9){
            Matisse.from(PublishActivity.this)
                    .choose(MimeType.allOf()) // 选择 mime 的类型
                    .countable(true)
//                .capture(true)
//                .captureStrategy(
//                        new CaptureStrategy(true, "com.cn.danceland.myapplication.fileprovider"))
                    .maxSelectable(9-m) // 图片选择的最多数量
                    .theme(R.style.imgsStyle)
                    //.gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .thumbnailScale(0.85f) // 缩略图的比例
                    .imageEngine(new PicassoEngine()) // 使用的图片加载引擎
                    .forResult(100); // 设置作为标记的请求码
        }

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
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

    private void showListDialog() {
        final String[] items = { "拍照","从相册选择"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(PublishActivity.this);
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    if(SPUtils.getInt("imgN",0)<9){
                        showCamera();
                    }else{
                        ToastUtils.showToastShort("最多选择9张图片");
                    }
                }else{
                    getPic();
                }
            }
        });
        listDialog.show();
    }

    private void showCamera(){
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
            if(PictureUtil.getSDKV()<24){
                uri = Uri.fromFile(new File(cameraPath));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, 99);
            }else{
                // 设置系统相机拍摄照片完成后图片文件的存放地址
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, cameraPath);
                uri = getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, 99);
            }
            //uri = Uri.fromFile(new File(cameraPath));

        }
        else {
            Toast.makeText(getApplicationContext(), "请确认已经插入SD卡",
                    Toast.LENGTH_LONG).show();
        }

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
        if(requestCode==100 && resultCode == RESULT_OK){
            if(data!=null){
//                arrayList = data.getStringArrayListExtra("arrPath");
                List<Uri> uris = Matisse.obtainResult(data);

                if(uris!=null){
                    for(int i = 0;i<uris.size();i++){
                        arrayList.add(PictureUtil.getRealPath(getApplicationContext(),uris.get(i)));
                    }
                    SPUtils.setInt("imgN",arrayList.size()+SPUtils.getInt("imgN",0));
                    grid_view.setAdapter(new SmallGridAdapter(PublishActivity.this,arrayList));
                }

            }
        }else if(requestCode==99&&resultCode == RESULT_OK){
            arrayList.add(cameraPath);
            SPUtils.setInt("imgN",arrayList.size()+SPUtils.getInt("imgN",0));
            grid_view.setAdapter(new SmallGridAdapter(PublishActivity.this,arrayList));
        }else if(resultCode==1){
                location = data.getStringExtra("location");
                publish_location.setText(location);
        }else if(resultCode == 111){
            videoPath = data.getStringExtra("videoPath");
            arrayList.clear();
            if(videoPath!=null){
                MediaMetadataRetriever media = new MediaMetadataRetriever();
                media.setDataSource(videoPath);
                Bitmap frameAtTime = media.getFrameAtTime();
                picFile = saveBitmapFile(frameAtTime);
                arrayList.add(picFile.getAbsolutePath());
                grid_view.setAdapter(new SmallGridAdapter(PublishActivity.this,arrayList));
//                videoimg.setBackground(new BitmapDrawable(frameAtTime));
//                //publish_photo.setVisibility(View.GONE);
//                videoimg.setVisibility(View.VISIBLE);
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
        List<String> arrayLists;
        int imgN;

        SmallGridAdapter(Context context,List<String> asList){
            imgN = SPUtils.getInt("imgN",0);
            this.context = context;
            arrayLists = asList;
            mInflater = LayoutInflater.from(context);
        }


        @Override
        public int getCount() {
            return arrayLists.size()+1;
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
            ViewHolder viewHolder=null;
            if(convertView==null){
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.images_item, null);
                viewHolder.img = convertView.findViewById(R.id.image_item);
                viewHolder.rl_item = convertView.findViewById(R.id.rl_item);
                viewHolder.pl_sta = convertView.findViewById(R.id.pl_sta);
                viewHolder.item_select = convertView.findViewById(R.id.item_select);
                viewHolder.item_select.setVisibility(View.GONE);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if("0".equals(isPhoto)){
                if(arrayLists.size()==0){
                    viewHolder.pl_sta.setVisibility(View.VISIBLE);
                }else{
                    if(position<arrayLists.size()){
                        Glide.with(context).load(arrayLists.get(position)).into(viewHolder.img);
                        viewHolder.pl_sta.setVisibility(View.GONE);
                    }else if(position==arrayLists.size()){
                        viewHolder.pl_sta.setVisibility(View.VISIBLE);
                    }
                }
            }else {
                if(arrayLists.size()==0){
                    viewHolder.pl_sta.setVisibility(View.VISIBLE);
                }else{
                    if(position<arrayLists.size()){
                        Glide.with(context).load(arrayLists.get(position)).into(viewHolder.img);
                        viewHolder.pl_sta.setVisibility(View.GONE);
                    }else if(position==arrayLists.size()){
                        viewHolder.rl_item.setVisibility(View.GONE);
                    }
                }
            }
            viewHolder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(imgN<=9){
                        LogUtil.e("zzf",imgN+"");
                        if(position<=arrayLists.size()){
                            if("0".equals(isPhoto)){
                                showListDialog();
                            }else{
                                Intent intentr = new Intent(PublishActivity.this,RecordView.class);
                                startActivityForResult(intentr,111);
                            }
                        }
                    }else{
                        ToastUtils.showToastShort("最多选择9张图片");
                    }

                }
            });


            return convertView;
        }
    }

    class ViewHolder{
        ImageView img,item_select;
        TextView pl_sta;
        RelativeLayout rl_item;
    }

}
