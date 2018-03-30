package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.JiaoLianCourseBean;
import com.cn.danceland.myapplication.bean.MyCourseBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.TimeUtils;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by feng on 2018/3/30.
 */

public class CourseDetailActivity extends Activity {

    String startTime,endTime,startTimeTv,endTimeTv;
    Data data;
    String role;
    String auth;
    MyCourseBean.Data item;
    JiaoLianCourseBean.Content item1;
    TextView course_name,course_length,course_place,course_room,
    course_jiaolian_huiyuan_name,course_renshu,tv_kecheng_fenshu,tv_jiaolian_fenshu,tv_changdi_fenshu,
            tv_content;
    ImageView course_img;
    CircleImageView course_jiaolian_huiyuan_circle;
    RelativeLayout rl_button_yuyue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.coursedetail_activity);
        initHost();

        initView();
    }

    private void initHost() {

        data = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
        startTimeTv = getIntent().getStringExtra("startTime");
        startTime = TimeUtils.date2TimeStamp(startTimeTv+" 00:00:00", "yyyy-MM-dd 00:00:00")+"";
        endTimeTv = getIntent().getStringExtra("endTime");
        role = getIntent().getStringExtra("role");
        auth = getIntent().getStringExtra("auth");
        if(role!=null){
            item1 = (JiaoLianCourseBean.Content)getIntent().getSerializableExtra("item");
        }else{
            item = (MyCourseBean.Data)getIntent().getSerializableExtra("item");
        }


    }

    private void initView() {

        course_name = findViewById(R.id.course_name);
        course_length = findViewById(R.id.course_length);
        course_place = findViewById(R.id.course_place);
        course_room = findViewById(R.id.course_room);
        course_img = findViewById(R.id.course_img);
        course_jiaolian_huiyuan_name = findViewById(R.id.course_jiaolian_huiyuan_name);
        course_jiaolian_huiyuan_circle = findViewById(R.id.course_jiaolian_huiyuan_circle);
        course_renshu = findViewById(R.id.course_renshu);
        rl_button_yuyue = findViewById(R.id.rl_button_yuyue);
        tv_kecheng_fenshu = findViewById(R.id.tv_kecheng_fenshu);
        tv_jiaolian_fenshu = findViewById(R.id.tv_jiaolian_fenshu);
        tv_changdi_fenshu = findViewById(R.id.tv_changdi_fenshu);
        tv_content = findViewById(R.id.tv_content);

        setclick();

        initData();
    }

    private void setclick() {

        rl_button_yuyue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CourseDetailActivity.this,SiJiaoDetailActivity.class);
                if(role!=null){
                    intent.putExtra("item",item1);
                }else{
                    intent.putExtra("item",item);
                }
                intent.putExtra("startTime",startTime);
                intent.putExtra("endTime",endTime);
                intent.putExtra("role",role);
                intent.putExtra("auth",auth);

                startActivity(intent);
            }
        });

    }

    private void initData() {
        if(role!=null){
            course_name.setText(item1.getCourse_type_name());

        }else{
            course_name.setText(item.getCourse_type_name());
            course_length.setText("课程时长："+item.getTime_length()+"分钟");
            course_place.setText("上课场馆：");
            course_room.setText("上课场地：");
            Glide.with(CourseDetailActivity.this).load("").into(course_img);
            course_jiaolian_huiyuan_name.setText(item.getEmployee_name());
        }
    }
}
