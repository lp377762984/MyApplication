package com.cn.danceland.myapplication.shouhuan.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.db.WearFitSleepBean;
import com.cn.danceland.myapplication.db.WearFitSleepHelper;
import com.cn.danceland.myapplication.shouhuan.adapter.SleepAdapter;
import com.cn.danceland.myapplication.shouhuan.bean.SleepBean;
import com.cn.danceland.myapplication.utils.AppUtils;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.StringUtils;
import com.cn.danceland.myapplication.utils.TimeUtils;
import com.cn.danceland.myapplication.view.DongLanTitleView;
import com.cn.danceland.myapplication.view.NoScrollListView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 睡眠
 * Created by yxx on 2018/7/18.
 */
public class WearFitSleepActivity extends Activity {
    private Context context;
    private DongLanTitleView heart_title;//心率title
    private LinearLayout column_layout;
    private NoScrollListView listview;
    private TextView sleep_detail_tv;//上面最小的蓝色item描述
    private SleepAdapter sleepAdapter;

    private List<SleepBean> sleepBeans = new ArrayList<>();//睡眠数据
    private WearFitSleepHelper sleepHelper = new WearFitSleepHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wearfit_sleep);
        context = this;
        initView();
        initData();
    }

    private void initView() {
        heart_title = findViewById(R.id.shouhuan_title);
        heart_title.setTitle(context.getResources().getString(R.string.sleep_text));
        column_layout = (LinearLayout) findViewById(R.id.column_layout);
        listview = (NoScrollListView) findViewById(R.id.listview);
        sleep_detail_tv = (TextView) findViewById(R.id.sleep_detail_tv);
    }

    private void initData() {
        SleepBean sb = new SleepBean();
        sb.setTime(40);
        sb.setState(1);
        sb.setStartTime(new Date().getTime());
        sb.setEndTime(new Date().getTime());
        sleepBeans.add(sb);
        SleepBean sb1 = new SleepBean();
        sb1.setTime(90);
        sb1.setState(-1);
        sb1.setStartTime(new Date().getTime());
        sb1.setEndTime(new Date().getTime());
        sleepBeans.add(sb1);
        SleepBean sb2 = new SleepBean();
        sb2.setTime(60);
        sb2.setState(2);
        sb2.setStartTime(new Date().getTime());
        sb2.setEndTime(new Date().getTime());
        sleepBeans.add(sb2);
        SleepBean sb3 = new SleepBean();
        sb3.setTime(1);
        sb3.setState(-1);
        sb3.setStartTime(new Date().getTime());
        sb3.setEndTime(new Date().getTime());
        sleepBeans.add(sb3);
        SleepBean sb4 = new SleepBean();
        sb4.setTime(20);
        sb4.setState(2);
        sb4.setStartTime(new Date().getTime());
        sb4.setEndTime(new Date().getTime());
        sleepBeans.add(sb4);
        addChildLayout();
        sleepAdapter = new SleepAdapter(context, sleepBeans);
        listview.setAdapter(sleepAdapter);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        LogUtil.i(year + "-" + month + "-" + day);
        querySleepByDay(year + "", month + "", day + "");
    }

    private void addChildLayout() {//-1 清醒  1 浅水  2 深睡\
        int screenWidth = AppUtils.getWidth() - 30;//获取屏幕宽度dp,px 减掉左右边距
        int sumItemWidth = 0;
        for (SleepBean sbb : sleepBeans
                ) {
            sumItemWidth += sbb.getTime();
        }
        LogUtil.i("所有柱子总宽度" + sumItemWidth);
        for (int i = 0; i < sleepBeans.size(); i++) {
            NumberFormat numberFormat = NumberFormat.getInstance();// 创建一个数值格式化对象
            numberFormat.setMaximumFractionDigits(0);// 设置精确到小数点后2位
            String successHeart = numberFormat.format((float) sleepBeans.get(i).getTime() / (float) sumItemWidth * screenWidth);//达标
            int successHeartInt = 0;
            if (StringUtils.isNumeric(successHeart)) {
                successHeartInt = Integer.valueOf(successHeart);
            }
            LogUtil.i("所有柱子占比" + successHeartInt);

            LinearLayout chlidLayout = new LinearLayout(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(successHeartInt, LinearLayout.LayoutParams.MATCH_PARENT);
            switch (sleepBeans.get(i).getState()) {
                case -1:
                    layoutParams.setMargins(0, 80, 0, 0);
                    chlidLayout.setBackgroundColor(context.getResources().getColor(R.color.color_dl_yellow));
                    break;
                case 1:
                    layoutParams.setMargins(0, 40, 0, 0);
                    chlidLayout.setBackgroundColor(context.getResources().getColor(R.color.shallow_sleep_bg));
                    break;
                case 2:
                    layoutParams.setMargins(0, 0, 0, 0);
                    chlidLayout.setBackgroundColor(context.getResources().getColor(R.color.deep_sleep_bg));
                    break;
            }
            chlidLayout.setLayoutParams(layoutParams);
            chlidLayout.setTag(i);//标识
            chlidLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i = 0; i < column_layout.getChildCount(); i++) {
                        if (view.getTag().equals(column_layout.getChildAt(i).getTag())) {
                            sleep_detail_tv.setVisibility(View.VISIBLE);
                            switch (sleepBeans.get(i).getState()) {
                                case -1:
                                    sleep_detail_tv.setText(context.getResources().getString(R.string.awake_text) + sleepBeans.get(i).getTime() + "分钟");
                                    sleep_detail_tv.setTextColor(context.getResources().getColor(R.color.color_dl_yellow));
                                    column_layout.getChildAt(i).setBackgroundColor(context.getResources().getColor(R.color.color_tab_text_yellow));
                                    break;
                                case 1:
                                    sleep_detail_tv.setText(context.getResources().getString(R.string.shallow_sleep_text) + sleepBeans.get(i).getTime() + "分钟");
                                    sleep_detail_tv.setTextColor(context.getResources().getColor(R.color.shallow_sleep_bg));
                                    column_layout.getChildAt(i).setBackgroundColor(context.getResources().getColor(R.color.color_tab_text_yellow));
                                    break;
                                case 2:
                                    sleep_detail_tv.setText(context.getResources().getString(R.string.deep_sleep_text) + sleepBeans.get(i).getTime() + "分钟");
                                    sleep_detail_tv.setTextColor(context.getResources().getColor(R.color.deep_sleep_bg));
                                    column_layout.getChildAt(i).setBackgroundColor(context.getResources().getColor(R.color.color_tab_text_yellow));
                                    break;
                            }
                        } else {
                            switch (sleepBeans.get(i).getState()) {
                                case -1:
                                    column_layout.getChildAt(i).setBackgroundColor(context.getResources().getColor(R.color.color_dl_yellow));
                                    break;
                                case 1:
                                    column_layout.getChildAt(i).setBackgroundColor(context.getResources().getColor(R.color.shallow_sleep_bg));
                                    break;
                                case 2:
                                    column_layout.getChildAt(i).setBackgroundColor(context.getResources().getColor(R.color.deep_sleep_bg));
                                    break;
                            }
                        }
                    }
                }
            });
            column_layout.addView(chlidLayout);
        }
    }

    /**
     * 查询睡眠 日
     *
     * @param year
     * @param month
     * @param day
     */
    private void querySleepByDay(String year, String month, String day) {
        List<WearFitSleepBean>  sleeps = sleepHelper.queryByDay(TimeUtils.date2TimeStamp(year + "-" + month + "-" + day, "yyyy-MM-dd"));//获取本地数据库心率
        //  LogUtil.i(TimeUtils.date2TimeStamp(year+"-"+month+"-"+day+" 00:00:00","yyyy-MM-dd HH:mm:ss")+"");
        LogUtil.i("本地数据库共有个" + sleeps.size());
        if (sleeps != null && sleeps.size() != 0) {

        }
    }
}