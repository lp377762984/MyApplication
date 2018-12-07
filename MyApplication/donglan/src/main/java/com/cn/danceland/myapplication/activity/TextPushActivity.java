package com.cn.danceland.myapplication.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.activity.base.BaseActivity;

/**
 * 门店 文字推送
 * Created by yxx on 2018-12-07.
 */

public class TextPushActivity extends BaseActivity {
    private Context context;
    private EditText title_et;
    private EditText content_et;

    private int from;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_push);
        context = this;
        if (this.getIntent() != null) {
            from = this.getIntent().getIntExtra("from",0);
        }
//        initView();
//        initHeaderData();
//        initData();
    }

//    private void initView() {
//        circle_image = findViewById(R.id.circle_image);
//        tv_nick_name = findViewById(R.id.tv_nick_name);
//        tv_male_age = findViewById(R.id.tv_male_age);
//        tv_phone = findViewById(R.id.tv_phone);
//        iv_sex = findViewById(R.id.iv_sex);
//        rl_error = findViewById(R.id.rl_no_info);
//
//        tv_movement_time = findViewById(R.id.tv_movement_time);
//        tv_movement_distance = findViewById(R.id.tv_movement_distance);
//        tv_average_velocity = findViewById(R.id.tv_average_velocity);
//        tv_average_heart1 = findViewById(R.id.tv_average_heart1);
//        tv_number = findViewById(R.id.tv_number);
//        tv_group_number = findViewById(R.id.tv_group_number);
//        tv_average_weight = findViewById(R.id.tv_average_weight);
//        tv_average_heart2 = findViewById(R.id.tv_average_heart2);
//    }
//
//    private void initHeaderData() {
//        if (myInfo != null) {
//            Glide.with(context).load(myInfo.getPerson().getSelf_avatar_path()).into(circle_image);
//            tv_nick_name.setText(myInfo.getPerson().getNick_name());
//            if (TextUtils.equals(myInfo.getPerson().getGender(), "1")) {
//                iv_sex.setImageResource(R.drawable.img_sex1);
//            } else if (TextUtils.equals(myInfo.getPerson().getGender(), "2")) {
//                iv_sex.setImageResource(R.drawable.img_sex2);
//            } else {
//                iv_sex.setVisibility(View.INVISIBLE);
//            }
//
//            if (myInfo.getPerson().getBirthday() != null) {
//                Time time = new Time();
//                time.setToNow();
//                int age = time.year - Integer.valueOf(myInfo.getPerson().getBirthday().split("-")[0]);
//                tv_male_age.setText(age + " 岁");
//            }
//            tv_phone.setText(myInfo.getPerson().getPhone_no());
//        }
//    }
//
//    private void initData() {
//        LogUtil.i("token=" + SPUtils.getString(Constants.MY_TOKEN, null));
//        MyStringRequest request = new MyStringRequest(Request.Method.POST, Constants.QUERY_SH_TOTAL, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                LogUtil.i(s.toString());
//                MotionTotalBean datainfo = new Gson().fromJson(s.toString(), MotionTotalBean.class);
//                if (datainfo.getSuccess() && datainfo.getCode().equals("0")) {
//                    tv_movement_time.setText((Integer.valueOf(datainfo.getData().getTime()) / 60) + "分" + (Integer.valueOf(datainfo.getData().getTime()) % 60) + "秒");//运动时长 秒
//                    float kmf = (Float.valueOf(datainfo.getData().getDistance())) / (float) 1000.00;//100步长  1000km
//                    DecimalFormat fnum = new DecimalFormat("##0.00");
//                    DecimalFormat fnum2 = new DecimalFormat("##0");
//                    String km = fnum.format(kmf);
//                    tv_movement_distance.setText(km + "km");//运动距离 km
//                    tv_average_velocity.setText(fnum2.format(Float.valueOf(datainfo.getData().getAvg_speed())) + "km/h");//平均速度
//                    tv_average_heart1.setText(datainfo.getData().getAerobic_heart() + "bpm");//平均心率 有氧
//                    tv_number.setText(datainfo.getData().getTimes() + "次");//运动次数
//                    tv_group_number.setText(datainfo.getData().getGroup_count() + "组");//运动组数
//                    if (datainfo.getData().getAvg_heavy() != null) {
//                        tv_average_weight.setText(fnum2.format(Float.valueOf(datainfo.getData().getAvg_heavy())) + "kg");//平均重量
//                    } else {
//                        tv_average_weight.setText(0 + "kg");//平均重量
//                    }
//                    tv_average_heart2.setText(datainfo.getData().getAnaerobic_heart() + "bpm");//平均心率 无氧
//                    rl_error.setVisibility(View.GONE);
//                } else {
//                    rl_error.setVisibility(View.VISIBLE);
//                    ToastUtils.showToastLong(datainfo.getErrorMsg());
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                LogUtil.i(volleyError.toString());
//                rl_error.setVisibility(View.VISIBLE);
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> map = new HashMap<>();
//                map.put("person_id", myInfo.getPerson().getId());
//                LogUtil.i(map.toString());
//                return map;
//            }
//        };
//        MyApplication.getHttpQueues().add(request);
//    }
}
