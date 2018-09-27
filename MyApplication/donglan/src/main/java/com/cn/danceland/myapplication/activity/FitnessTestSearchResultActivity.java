package com.cn.danceland.myapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.RequsetFindUserBean;
import com.cn.danceland.myapplication.utils.TimeUtils;
import com.cn.danceland.myapplication.view.DongLanTitleView;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 体测分析-搜索结果
 * Created by yxx on 2018-09-26.
 */

public class FitnessTestSearchResultActivity extends BaseActivity {
    private Context context;
    private DongLanTitleView title;
    private CircleImageView iv_avatar;
    private TextView name_tv;
    private TextView tel_tv;
    private TextView real_name_ev;
    private TextView birthday_ev;
    private TextView height_ev;
    private TextView history_btn;
    private TextView finess_btn;
    private TextView text_male;
    private TextView text_female;

    private RequsetFindUserBean.Data requsetInfo;//前面搜索到的对象

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness_search_results);
        context = this;
        requsetInfo = (RequsetFindUserBean.Data) getIntent().getSerializableExtra("requsetInfo");//前面搜索到的对象
        initView();
        initData();
    }

    private void initView() {
        title = findViewById(R.id.title);
        title.setTitle("体测分析");
        iv_avatar = findViewById(R.id.iv_avatar);
        name_tv = findViewById(R.id.name_tv);
        tel_tv = findViewById(R.id.tel_tv);
        real_name_ev = findViewById(R.id.real_name_ev);
        birthday_ev = findViewById(R.id.birthday_ev);
        height_ev = findViewById(R.id.height_ev);
        history_btn = findViewById(R.id.history_btn);
        finess_btn = findViewById(R.id.finess_btn);
        text_male = findViewById(R.id.text_male);
        text_female = findViewById(R.id.text_female);
        history_btn.setOnClickListener(onClickListener);
        finess_btn.setOnClickListener(onClickListener);
    }

    private void initData() {
        RequestOptions options2 = new RequestOptions().placeholder(R.drawable.img_my_avatar);
        if (requsetInfo.getAvatar_url() != null && requsetInfo.getAvatar_url().length() > 0) {//头像
            Glide.with(context).load(requsetInfo.getAvatar_url()).apply(options2).into(iv_avatar);
        }
        name_tv.setText(requsetInfo.getNick_name());
        tel_tv.setText(requsetInfo.getPhone_no());
        real_name_ev.setText(requsetInfo.getCname());
        birthday_ev.setText(requsetInfo.getBirthday());//年龄
        height_ev.setText(requsetInfo.getHeight()+"cm");
        if(requsetInfo.getGender()==1){//性别 1男  2女
            text_male.setBackgroundResource(R.drawable.male_blue);
            text_female.setBackgroundResource(R.drawable.female_gray);
        }else{
            text_male.setBackgroundResource(R.drawable.male_gray);
            text_female.setBackgroundResource(R.drawable.female_blue);
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.history_btn://历史记录
                    startActivity(new Intent(context, FitnessTestHistoryActivity.class).putExtra("requsetInfo", requsetInfo));
                    break;
                case R.id.finess_btn://体测分析
//                    Intent intent = new Intent(context, FitnessTestNoticeActivity.class);
//                    intent.putExtra("requsetInfo", requsetInfo);
//                    startActivity(intent);
                    startActivity(new Intent(context, FitnessTestNoticeActivity.class).putExtra("requsetInfo", requsetInfo));
                    break;
            }
        }
    };

}
