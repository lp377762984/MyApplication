package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.PictureUtil;
import com.cn.danceland.myapplication.utils.multipartrequest.MultipartRequest;
import com.cn.danceland.myapplication.utils.multipartrequest.MultipartRequestParams;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.R.attr.bitmap;

/**
 * Created by feng on 2017/10/10.
 */

public class MyProActivity extends Activity {
    CircleImageView circleImageView;
    TextView text_name,text_sex,photograph,photo_album,cancel,cancel1,male,female;
    RelativeLayout  headimage,name,sex;
    PopupWindow head_image_window;
    View headView,rootview,sexView;
    ImageView back;
    int flag;
    Uri uri,mCutUri;
    ContentResolver resolver;
    public final static int ALBUM_REQUEST_CODE = 1;
    public final static int CROP_REQUEST = 2;
    public final static int CAMERA_REQUEST_CODE = 3;
    public static String SAVED_IMAGE_DIR_PATH =
            Environment.getExternalStorageDirectory().getPath()
                    + "/donglan/camera/";// 拍照路径
    String cameraPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypro);
        initHost();
        initView();
        setClick();
    }

    public void initHost(){
        resolver = getContentResolver();
    }

    public void initView(){
        circleImageView = findViewById(R.id.circleimageview);
        text_name = findViewById(R.id.text_name);
        text_sex = findViewById(R.id.text_sex);
        headimage = findViewById(R.id.head_image);
        name = findViewById(R.id.name);
        sex = findViewById(R.id.sex);
        back = findViewById(R.id.back);

        headView = LayoutInflater.from(MyProActivity.this).inflate(R.layout.head_image_popwindow, null);

        photograph = headView.findViewById(R.id.photograph);
        photo_album = headView.findViewById(R.id.photo_album);
        cancel = headView.findViewById(R.id.cancel);

    }

    public void setClick(){
        headimage.setOnClickListener(onClickListener);
        name.setOnClickListener(onClickListener);
        sex.setOnClickListener(onClickListener);
        cancel.setOnClickListener(onClickListener);
        photo_album.setOnClickListener(onClickListener);
        photograph.setOnClickListener(onClickListener);
        back.setOnClickListener(onClickListener);
    }
    public void dismissWindow(){
        if(null != head_image_window && head_image_window.isShowing()){
            head_image_window.dismiss();
        }
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id){
                case R.id.head_image:{
                    flag = 0;
                    dismissWindow();
                    showEditImage();
                    showPop();
                }
                break;
                case R.id.name:{
                    showName();
                }
                break;
                case R.id.sex:{
                    flag = 1;
                    dismissWindow();
                    showSex();
                    showPop();
                }
                break;
                case R.id.cancel:
                    if(flag==0){
                        dismissWindow();
                    }else if(flag==1){
                        dismissWindow();
                        text_sex.setText("女");
                    }
                    break;
                case R.id.photo_album:
                    if(flag==0){
                        dismissWindow();
                        photoAlbum();
                    }else if(flag==1){
                        dismissWindow();
                        text_sex.setText("男");
                    }
                    break;
                case R.id.photograph:
                    if(flag==0){
                        dismissWindow();
                        photoGraph();
                    }else{
                        break;
                    }
                    break;
                case R.id.back:
                    finish();
                    break;
            }
        }
    };




    public void showPop(){
        head_image_window = new PopupWindow(headView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        head_image_window.setOutsideTouchable(true);
        head_image_window.setBackgroundDrawable(new BitmapDrawable());
        //显示PopupWindow
        rootview = LayoutInflater.from(MyProActivity.this).inflate(R.layout.activity_mypro, null);
        head_image_window.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
        head_image_window.setAnimationStyle(R.style.selectorMenuAnim);

    }

    public void showEditImage(){
        if(null != head_image_window && head_image_window.isShowing()){
            head_image_window.dismiss();
        }
        photograph.setText("拍照");
        photograph.setTextColor(Color.rgb(46,167,224));
        photo_album.setText("从手机相册选择");
        cancel.setText("取消");
        //cancel1.setVisibility(View.GONE);
    }

    public void showName(){

            AlertDialog.Builder normalDialog =
                    new AlertDialog.Builder(MyProActivity.this);
            View dialogView = LayoutInflater.from(MyProActivity.this)
                .inflate(R.layout.edit_name,null);
            //normalDialog.setTitle("编辑昵称");
           final EditText ed = dialogView.findViewById(R.id.edit_name);
            normalDialog.setView(dialogView);
            normalDialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            text_name.setText(ed.getText());
                        }
                    });
            normalDialog.setNegativeButton("关闭",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            // 显示
            normalDialog.show();

    }

    public void showSex(){
        if(null != head_image_window && head_image_window.isShowing()){
            head_image_window.dismiss();
        }
        photograph.setText("修改性别");
        photograph.setTextColor(Color.rgb(153,153,153));
        photo_album.setText("男");
        cancel.setText("女");
    }

    public void photoGraph (){
//        File outputfile = new File(MyProActivity.this.getExternalCacheDir(),System.currentTimeMillis() + ".png");
//        try {
//            if (outputfile.exists()){
//                outputfile.delete();//删除
//            }
//            outputfile.createNewFile();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Uri imageuri ;
//        if (Build.VERSION.SDK_INT >= 24){
//            imageuri = FileProvider.getUriForFile(MyProActivity.this,
//                    "com.cn.danceland.myapplication", //可以是任意字符串
//                    outputfile);
//        }else{
//            imageuri = Uri.fromFile(outputfile);
//        }
//        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageuri);
//        startActivityForResult(intent,CAMERA_REQUEST_CODE);
        // 指定相机拍摄照片保存地址
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            cameraPath = SAVED_IMAGE_DIR_PATH + System.currentTimeMillis() + ".png";
            Intent intent = new Intent();
            // 指定开启系统相机的Action
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            String out_file_path = SAVED_IMAGE_DIR_PATH;
            File dir = new File(out_file_path);
            if (!dir.exists()) {
                dir.mkdirs();
            } // 把文件地址转换成Uri格式
            uri = Uri.fromFile(new File(cameraPath));
            // 设置系统相机拍摄照片完成后图片文件的存放地址
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }
        else {
            Toast.makeText(getApplicationContext(), "请确认已经插入SD卡",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            MultipartRequestParams params = new MultipartRequestParams();
            if (requestCode == CAMERA_REQUEST_CODE) {
                startPhotoZoom(uri);
            }
            if(requestCode == ALBUM_REQUEST_CODE){
                startActivityForResult(CutForPhoto(data.getData()),10010);
            }
            if(requestCode==10010){
                Glide.with(MyProActivity.this).load(mCutUri).into(circleImageView);
            }
            if(requestCode==222){
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    File fileCut = new File(SAVED_IMAGE_DIR_PATH + System.currentTimeMillis() + ".png");
                    try{
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileCut));
                        photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        bos.flush();
                        bos.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    Drawable drawable = new BitmapDrawable(getResources(), photo);
                    Glide.with(MyProActivity.this).load(drawable).into(circleImageView);
                }
            }

            MultipartRequest request = new MultipartRequest(Request.Method.POST, params, Constants.UPLOADFILE_URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String s) {

                    LogUtil.e("zzf",s);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    LogUtil.e("zzf",volleyError.toString());
                }
            }
            ) {
            };

      //      MyApplication.getHttpQueues().add(request);

        }

    }

    public void  photoAlbum(){

        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, ALBUM_REQUEST_CODE);

    }

    private Intent CutForPhoto(Uri uri) {
        try {
            //直接裁剪
            Intent intent = new Intent("com.android.camera.action.CROP");
            //设置裁剪之后的图片路径文件
            File cutfile = new File(Environment.getExternalStorageDirectory().getPath(),
                    System.currentTimeMillis()+".png"); //随便命名一个
            if (cutfile.exists()){ //如果已经存在，则先删除,这里应该是上传到服务器，然后再删除本地的，没服务器，只能这样了
                cutfile.delete();
            }
            cutfile.createNewFile();
            //初始化 uri
            Uri imageUri = uri; //返回来的 uri
            Uri outputUri = null; //真实的 uri
            //Log.d(TAG, "CutForPhoto: "+cutfile);
            outputUri = Uri.fromFile(cutfile);
            mCutUri = outputUri;
            //Log.d(TAG, "mCameraUri: "+mCutUri);
            // crop为true是设置在开启的intent中设置显示的view可以剪裁
            intent.putExtra("crop",true);
            // aspectX,aspectY 是宽高的比例，这里设置正方形
            intent.putExtra("aspectX",1);
            intent.putExtra("aspectY",1);
            //设置要裁剪的宽高
            intent.putExtra("outputX", 200); //200dp
            intent.putExtra("outputY",200);
            intent.putExtra("scale",true);
            //如果图片过大，会导致oom，这里设置为false
            intent.putExtra("return-data",false);
            if (imageUri != null) {
                intent.setDataAndType(imageUri, "image/*");
            }
            if (outputUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
            }
            intent.putExtra("noFaceDetection", true);
            //压缩图片
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            return intent;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Intent CutForCamera(String camerapath,String imgname) {
        try {

            //设置裁剪之后的图片路径文件
            File cutfile = new File(Environment.getExternalStorageDirectory().getPath(),
                    System.currentTimeMillis() + ".png"); //随便命名一个
            if (cutfile.exists()){ //如果已经存在，则先删除,这里应该是上传到服务器，然后再删除本地的，没服务器，只能这样了
                cutfile.delete();
            }
            cutfile.createNewFile();
            //初始化 uri
            Uri imageUri = null; //返回来的 uri
            Uri outputUri = null; //真实的 uri
            Intent intent = new Intent("com.android.camera.action.CROP");
            //拍照留下的图片
            File camerafile = new File(camerapath,imgname);
            if (Build.VERSION.SDK_INT >= 24) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                imageUri = FileProvider.getUriForFile(MyProActivity.this,
                        "com.cn.danceland.myapplication",
                        camerafile);
            } else {
                imageUri = Uri.fromFile(camerafile);
            }
            outputUri = Uri.fromFile(cutfile);
            //把这个 uri 提供出去，就可以解析成 bitmap了
            mCutUri = outputUri;
            // crop为true是设置在开启的intent中设置显示的view可以剪裁
            intent.putExtra("crop",true);
            // aspectX,aspectY 是宽高的比例，这里设置正方形
            intent.putExtra("aspectX",1);
            intent.putExtra("aspectY",1);
            //设置要裁剪的宽高
            intent.putExtra("outputX", 200);
            intent.putExtra("outputY",200);
            intent.putExtra("scale",true);
            //如果图片过大，会导致oom，这里设置为false
            intent.putExtra("return-data",false);
            if (imageUri != null) {
                intent.setDataAndType(imageUri, "image/*");
            }
            if (outputUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
            }
            intent.putExtra("noFaceDetection", true);
            //压缩图片
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            return intent;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, 222);
    }
}
