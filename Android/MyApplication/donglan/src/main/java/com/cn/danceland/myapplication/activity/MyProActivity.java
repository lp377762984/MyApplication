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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.utils.PictureUtil;

import java.io.File;
import java.net.URI;

import de.hdodenhof.circleimageview.CircleImageView;

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
    Uri uri;
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
        } else {
            Toast.makeText(getApplicationContext(), "请确认已经插入SD卡",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                circleImageView.setImageBitmap(PictureUtil.getSmallBitmap(cameraPath,65,65));
            }
            if(requestCode == ALBUM_REQUEST_CODE){
                Uri photo = data.getData();
                String path = PictureUtil.getRealPathFromUri_AboveApi19(this,photo);
                circleImageView.setImageBitmap(PictureUtil.getSmallBitmap(path,65,65));
            }
        }

    }

    public void  photoAlbum(){

        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, ALBUM_REQUEST_CODE);

    }

}
