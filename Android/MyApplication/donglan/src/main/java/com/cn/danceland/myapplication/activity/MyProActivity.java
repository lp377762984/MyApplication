package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.danceland.myapplication.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by feng on 2017/10/10.
 */

public class MyProActivity extends Activity {
    CircleImageView circleImageView;
    TextView text_name;
    TextView text_sex;
    RelativeLayout  headimage,name,sex;
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
                }
                break;
                case R.id.name:{
                    showName();
                }
                break;
                case R.id.sex:{
                    showSex();
                }
                break;
            }
        }
    };

    public void showEditImage(){




    }

    public void showName(){


    }

    public void showSex(){


    }

}
