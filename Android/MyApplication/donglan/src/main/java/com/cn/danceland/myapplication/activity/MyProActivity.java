package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.danceland.myapplication.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by feng on 2017/10/10.
 */

public class MyProActivity extends Activity {
    CircleImageView circleImageView;
    TextView text_name,text_sex,photograph,photo_album,cancel,cancel1;
    RelativeLayout  headimage,name,sex;
    PopupWindow head_image_window;
    View headView,rootview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypro);
        initView();
        setClick();
    }

    public void initView(){
        circleImageView = findViewById(R.id.circleimageview);
        text_name = findViewById(R.id.text_name);
        text_sex = findViewById(R.id.text_sex);
        headimage = findViewById(R.id.head_image);
        name = findViewById(R.id.name);
        sex = findViewById(R.id.sex);



        headView = LayoutInflater.from(MyProActivity.this).inflate(R.layout.head_image, null);



        photograph = headView.findViewById(R.id.photograph);
        photo_album = headView.findViewById(R.id.photo_album);
        cancel = headView.findViewById(R.id.cancel);
        cancel1 = headView.findViewById(R.id.cancel1);
    }

    public void setClick(){
        headimage.setOnClickListener(onClickListener);
        name.setOnClickListener(onClickListener);
        sex.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id){
                case R.id.head_image:{
                    showEditImage();
                    showPop();
                }
                break;
                case R.id.name:{
                    showName();
                }
                break;
                case R.id.sex:{
                    showSex();
                    showPop();
                }
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
        cancel1.setVisibility(View.GONE);
    }

    public void showName(){

            AlertDialog.Builder normalDialog =
                    new AlertDialog.Builder(MyProActivity.this);
            View dialogView = LayoutInflater.from(MyProActivity.this)
                .inflate(R.layout.edit_name,null);
            //normalDialog.setTitle("编辑昵称");

            normalDialog.setView(dialogView);
            normalDialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

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
        cancel1.setVisibility(View.VISIBLE);
        cancel1.setText("取消");
    }

}
