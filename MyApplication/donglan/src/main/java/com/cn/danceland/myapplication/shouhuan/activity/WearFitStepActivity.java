package com.cn.danceland.myapplication.shouhuan.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.db.WearFitStepBean;
import com.cn.danceland.myapplication.db.WearFitStepHelper;
import com.cn.danceland.myapplication.shouhuan.adapter.StepAdapter;
import com.cn.danceland.myapplication.shouhuan.bean.StepBean;
import com.cn.danceland.myapplication.shouhuan.bean.WearFitUser;
import com.cn.danceland.myapplication.shouhuan.chart.BarEntity;
import com.cn.danceland.myapplication.shouhuan.chart.BarGroup;
import com.cn.danceland.myapplication.shouhuan.chart.BarView;
import com.cn.danceland.myapplication.shouhuan.chart.SourceEntity;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.StringUtils;
import com.cn.danceland.myapplication.utils.TimeUtils;
import com.cn.danceland.myapplication.view.DongLanTitleView;
import com.cn.danceland.myapplication.view.HorizontalPickerView;
import com.cn.danceland.myapplication.view.NoScrollListView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 计步
 * Created by yxx on 2018/8/13.
 */

public class WearFitStepActivity extends Activity {
    private static final int MSG_REFRESH_DATA_DATA = 0;//日
    private static final int MSG_REFRESH_DATA_MORE_DATA = 1;//周 月
    private int lableType = 1;//切换标签 1天  2周  3月
    private Context context;
    private DongLanTitleView title;//心率title
    private LinearLayout statistical_layout;//统计
    private CheckBox day_checkBox;//日
    private CheckBox week_checkBox;//周
    private CheckBox month_checkBox;//年
    private HorizontalPickerView picker;//水平选择器
    private LinearLayout day_layout;
    private LinearLayout more_layout;
    private NoScrollListView listview;
    private BarGroup barGroup;
    private TextView km_tv;
    private TextView step_tv;
    private TextView kilocalorie_tv;
    private TextView average_daily_step_tv;
    private TextView standard_days_tv;
    private ProgressBar progressb_target;

    private ArrayList<String> pickerList = new ArrayList<>();//选择器数据

    private List<String> dayPickerList = new ArrayList<>();//日选择器对应的时间戳  开始-截止  -连接
    private List<String> weekPickerList = new ArrayList<>();//周选择器对应的时间戳  开始-截止  -连接
    private List<String> monthPickerList = new ArrayList<>();//月选择器对应的时间戳  开始-截止  -连接
    private List<WearFitStepBean> wearFitDataBeanList = new ArrayList<>();//本地数据库和后台共用模式
    private List<StepBean> stepBeans = new ArrayList<>();//本地数据库和后台共用模式
    private WearFitStepHelper stepHelper = new WearFitStepHelper();


    private WearFitUser wearFitUser = new WearFitUser();//本地用户数据
    private StepAdapter adapter;

    private String lastData = "";//选择器上次滚动的数据
    /*柱状图的最大值*/
    private float sourceMax = 0.00f;
    private int left;
    private int baseLineHeiht;
    private RelativeLayout.LayoutParams lp;
    private List<SourceEntity.Source> moreList = new ArrayList<>();
    private HorizontalScrollView root;
    private View popView;
    private PopupWindow popupWindow;
    private DecimalFormat mFormat = new DecimalFormat("##.####");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        context = this;
        wearFitUser = (WearFitUser) DataInfoCache.loadOneCache(Constants.MY_WEAR_FIT_SETTING);//手环设置
        initView();

    }

    private void initView() {
        title = findViewById(R.id.shouhuan_title);
        title.setTitle(context.getResources().getString(R.string.step_gauge_text));
        statistical_layout = (LinearLayout) findViewById(R.id.statistical_layout);
        day_checkBox = (CheckBox) findViewById(R.id.day_checkBox);//日
        week_checkBox = (CheckBox) findViewById(R.id.week_checkBox);//周
        month_checkBox = (CheckBox) findViewById(R.id.month_checkBox);//年
        picker = (HorizontalPickerView) findViewById(R.id.scrollPicker);//水平选择器
        day_layout = (LinearLayout) findViewById(R.id.day_layout);
        more_layout = (LinearLayout) findViewById(R.id.more_layout);
        listview = (NoScrollListView) findViewById(R.id.listview);
        barGroup = (BarGroup) findViewById(R.id.bar_group);
        root = (HorizontalScrollView) findViewById(R.id.bar_scroll);
        km_tv = (TextView) findViewById(R.id.km_tv);
        step_tv = (TextView) findViewById(R.id.step_tv);
        kilocalorie_tv = (TextView) findViewById(R.id.kilocalorie_tv);
        average_daily_step_tv = (TextView) findViewById(R.id.average_daily_step_tv);
        standard_days_tv = (TextView) findViewById(R.id.standard_days_tv);
        progressb_target = (ProgressBar) findViewById(R.id.progressb_target);

        popView = LayoutInflater.from(context).inflate(
                R.layout.pop_bg, null);

        day_checkBox.setOnCheckedChangeListener(onCheckedChangeListener);
        week_checkBox.setOnCheckedChangeListener(onCheckedChangeListener);
        month_checkBox.setOnCheckedChangeListener(onCheckedChangeListener);

        picker.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    String lableText = picker.getSelectedString();//得到触摸值
                    int lableSelectNum = picker.getSelectNum();//得到触摸位置
                    if (lastData == "") {
                        lastData = picker.getSelectedString();//第一次赋值
                    }
                    if (!lastData.equals(lableText)) {//最后一次和本次滚动的值不相等请求
                        String[] temp = null;
                        switch (lableType) {//切换标签 1天  2周  3月
                            case 1:
                                String[] splitDay = null;
                                splitDay = lableText.split("\\.");
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.MONTH, Integer.valueOf(splitDay[0]));
                                calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(splitDay[1]));
                                int year = calendar.get(Calendar.YEAR);
                                int month = calendar.get(Calendar.MONTH);
                                int day = calendar.get(Calendar.DAY_OF_MONTH);
                                LogUtil.i(year + "-" + month + "-" + day);
                                queryDataByDay(year + "", month + "", day + "");
                                break;
                            case 2:
                                temp = weekPickerList.get(lableSelectNum).split("&");
                                break;
                            case 3:
                                temp = monthPickerList.get(lableSelectNum).split("&");
                                break;
                        }
                        if (temp != null && temp.length == 2) {
                            LogUtil.i("上传参数1" + TimeUtils.timeToTopHour(Long.valueOf(temp[0] + "")) + "&" + TimeUtils.timeToTopHour(Long.valueOf(temp[1] + "")));
                            LogUtil.i("上传参数" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(TimeUtils.timeToTopHour(Long.valueOf(temp[0] + ""))).toString() + "&"
                                    + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(TimeUtils.timeToTopHour(Long.valueOf(temp[1] + ""))).toString());
                            querysDataByWeekOrMonth(TimeUtils.timeToTopHour(Long.valueOf(temp[0] + "")) + "", TimeUtils.timeToTopHour(Long.valueOf(temp[1] + "")) + "");
                        }
                    }
                    lastData = picker.getSelectedString();
                }
                return false;
            }
        });

        initPickerDay();//默认日数据
        defaultQueryDataByDay();
    }

    CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            String text = compoundButton.getText().toString();
            Toast.makeText(context, text, Toast.LENGTH_LONG).show();
            switch (compoundButton.getId()) {
                case R.id.day_checkBox://日
                    day_layout.setVisibility(View.VISIBLE);
                    more_layout.setVisibility(View.GONE);
                    initPickerDay();
                    lableType = 1;//切换标签 1天  2周  3月
                    day_checkBox.setTextColor(context.getResources().getColor(R.color.color_dl_yellow));
                    week_checkBox.setTextColor(context.getResources().getColor(R.color.colorGray8));
                    month_checkBox.setTextColor(context.getResources().getColor(R.color.colorGray8));
                    break;
                case R.id.week_checkBox://周
                    day_layout.setVisibility(View.GONE);
                    more_layout.setVisibility(View.VISIBLE);
                    initPickerWeek();
                    lableType = 2;//切换标签 1天  2周  3月
                    day_checkBox.setTextColor(context.getResources().getColor(R.color.colorGray8));
                    week_checkBox.setTextColor(context.getResources().getColor(R.color.color_dl_yellow));
                    month_checkBox.setTextColor(context.getResources().getColor(R.color.colorGray8));
                    break;
                case R.id.month_checkBox://月
                    day_layout.setVisibility(View.GONE);
                    more_layout.setVisibility(View.VISIBLE);
                    initPickerMonth();
                    lableType = 3;//切换标签 1天  2周  3月
                    day_checkBox.setTextColor(context.getResources().getColor(R.color.colorGray8));
                    week_checkBox.setTextColor(context.getResources().getColor(R.color.colorGray8));
                    month_checkBox.setTextColor(context.getResources().getColor(R.color.color_dl_yellow));
                    break;
            }
            getData();
        }
    };

    private void getData() {
        String[] temp = null;
        switch (lableType) {//切换标签 1天  2周  3月
            case 1:
                defaultQueryStepByDay();
                break;
            case 2:
                temp = weekPickerList.get(weekPickerList.size() - 1).split("&");
                break;
            case 3:
                temp = monthPickerList.get(monthPickerList.size() - 1).split("&");
                break;
        }
        if (temp != null && temp.length == 2) {
            LogUtil.i("上传参数" + new SimpleDateFormat("yyyy-MM-dd").format(TimeUtils.timeToTopHour(Long.valueOf(temp[0] + ""))).toString() + "&"
                    + new SimpleDateFormat("yyyy-MM-dd").format(TimeUtils.timeToTopHour(Long.valueOf(temp[0] + ""))).toString());
            querysDataByWeekOrMonth(TimeUtils.timeToTopHour(Long.valueOf(temp[0] + "")) + "", TimeUtils.timeToTopHour(Long.valueOf(temp[1] + "")) + "");
//            setHeartViewToService(temp[0] + "", temp[1] + "");
        }
    }

    /**
     * 初始化选择器 日数据
     */
    private void initPickerDay() {
        pickerList.clear();
        dayPickerList = new ArrayList<>();//日
        dayPickerList = TimeUtils.getLastMonthDayData();//日
        if (dayPickerList != null && dayPickerList.size() != 0) {
            for (int i = 0; i < dayPickerList.size(); i++) {
                pickerList.add(new SimpleDateFormat("M.d").format(new Date(dayPickerList.get(i))).toString());
            }
        }
        picker.setData(pickerList);
        picker.setSelectNum(pickerList.size() - 1);//时间选择器偏移
    }

    /**
     * 初始化选择器 周数据
     */
    private void initPickerWeek() {
        pickerList.clear();
        weekPickerList = new ArrayList<>();//周
        weekPickerList = TimeUtils.getWeekListData(160);//周  大概三年
        if (weekPickerList != null && weekPickerList.size() != 0) {
            for (int i = 0; i < weekPickerList.size(); i++) {
                String[] temp = null;
                temp = weekPickerList.get(i).split("&");
                if (temp != null && temp.length == 2) {
                    String wStr = new SimpleDateFormat("M.d").format(Long.valueOf(temp[0])).toString() + "-"
                            + new SimpleDateFormat("M.d").format(Long.valueOf(temp[1])).toString();
                    pickerList.add(wStr);
                }
            }
        }
        picker.setData(pickerList);
//        LogUtil.i("/"+pickerList.size()/2+"%"+pickerList.size()%2+"aa"+(pickerList.size()-pickerList.size()%2)/2);
        picker.setSelectNum(pickerList.size() - 1);//时间选择器偏移
    }

    /**
     * 初始化选择器 月数据
     */
    private void initPickerMonth() {
        pickerList.clear();
        int num = 36;//36个月  3年
        monthPickerList = new ArrayList<>();//周
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < num; i++) {
            pickerList.add(calendar.get(Calendar.YEAR) + "." + (calendar.get(Calendar.MONTH) + 1));
            String times = TimeUtils.getMonthFirstDay(calendar) + "&" + TimeUtils.getMonthLastDay(calendar);
            monthPickerList.add(times);//开始与截止的时间戳
            calendar.add(Calendar.MONTH, -1);
        }
        Collections.reverse(pickerList);
        Collections.reverse(monthPickerList);
        picker.setData(pickerList);
        picker.setSelectNum(pickerList.size() - 1);//时间选择器偏移
    }

    private void defaultQueryDataByDay() {
//        stepBeans.clear();//睡眠数据
//        wearFitStepBeanList.clear();//本地数据库和后台共用模式
//        column_layout.removeAllViews();
//        step_detail_tv.setVisibility(View.VISIBLE);
//        step_detail_tv.setText("");
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        LogUtil.i(year + "-" + month + "-" + day);
        queryDataByDay(year + "", month + "", day + "");
    }

    /**
     * 查询睡眠 日
     *
     * @param year
     * @param month
     * @param day
     */
    public void queryDataByDay(String year, String month, String day) {
        wearFitDataBeanList = stepHelper.queryByDay(TimeUtils.date2TimeStamp(year + "-" + month + "-" + day, "yyyy-MM-dd"));//获取本地数据库心率
        LogUtil.i("本地数据库共有个" + wearFitDataBeanList.size());
        if (wearFitDataBeanList != null && wearFitDataBeanList.size() != 0) {
            initViewToData();
        } else {
//            HeartRatePostBean heartRatePostBean = new HeartRatePostBean();
//            heartRatePostBean.setYear(year);
//            heartRatePostBean.setMonth(month);
//            heartRatePostBean.setDay(day);
//            LogUtil.i("请求后台心率" + heartRatePostBean.toString());
//            //获取后台数据
//            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.QUERY_WEAR_FIT_Step_LIST
//                    , new Gson().toJson(heartRatePostBean), new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject jsonObject) {
//                    LogUtil.i("jsonObject" + jsonObject.toString());
//                    if (jsonObject.toString().contains("true")) {
//                        StepListBean stepListBean = new Gson().fromJson(jsonObject.toString(), StepListBean.class);
//                        List<StepResultBean> data = stepListBean.getData();
//                        if (data != null && data.size() != 0) {
//                            LogUtil.i("data.size()" + data.size());
//                            wearFitDataBeanList.clear();
//                            for (int i = 0; i < data.size(); i++) {
//                                String time = data.get(i).getYear() + "-" + data.get(i).getMonth() + "-" + data.get(i).getDay() + " "
//                                        + data.get(i).getHour() + ":" + data.get(i).getMinute() + ":" + "00";
//                                WearFitStepBean wfsb = new WearFitStepBean();
//                                wfsb.setTimestamp(TimeUtils.date2TimeStamp(time, "yyyy-MM-dd HH:mm:ss"));
//                                wfsb.setState(data.get(i).getState());//11位state
//                                wfsb.setContinuoustime(Integer.valueOf(data.get(i).getMinutes()));//睡了多久 12位*256+13位
//                                wearFitDataBeanList.add(wfsb);
//                            }
//                        }
//                        Message message = new Message();
//                        message.what = MSG_REFRESH_DATA_DATA;
//                        handler.sendMessage(message);
//                    } else {
//                        ToastUtils.showToastShort("请查看网络连接");
//                        Message message = new Message();
//                        message.what = MSG_REFRESH_DATA_DATA;
//                        handler.sendMessage(message);
//                    }
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError volleyError) {
//                    ToastUtils.showToastShort(context.getResources().getText(R.string.network_connection_text).toString());
//                    LogUtil.e("onErrorResponse", volleyError.toString());
//                }
//            }) {
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    Map<String, String> map = new HashMap<String, String>();
//                    map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, null));
//                    return map;
//                }
//            };
//            MyApplication.getHttpQueues().add(request);
        }
    }

    /**
     * 查询心率 周 月
     *
     * @param timestamp_gt 开始时间
     * @param timestamp_lt 截止时间
     */
    private void querysDataByWeekOrMonth(String timestamp_gt, String timestamp_lt) {
//        more_deep_step_two_tv.setText(context.getResources().getString(R.string.step_line_text));//深睡
//        more_shallow_step_two_tv.setText(context.getResources().getString(R.string.step_line_text));//浅睡
//        more_step_time_tv.setText(context.getResources().getString(R.string.step_line_text));//睡眠时长
//        more_step_quality_tv.setText(context.getResources().getString(R.string.step_line_text));//睡眠质量
//        moreList.clear();
//        barGroup.removeAllViews();
//        StepMorePostBean weekPostBean = new StepMorePostBean();
//        weekPostBean.setGroup_time_gt(timestamp_gt);
//        weekPostBean.setGroup_time_lt(timestamp_lt);
//        LogUtil.i("请求后台心率" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.valueOf(timestamp_gt))) + "-"
//                + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.valueOf(timestamp_lt))));
//        //获取后台数据
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.QUERY_WEAR_FIT_Step_FINDSUM
//                , new Gson().toJson(weekPostBean), new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject jsonObject) {
//                LogUtil.i("jsonObject" + jsonObject.toString());
//                if (jsonObject.toString().contains("true")) {
//                    StepListBean stepListBean = new Gson().fromJson(jsonObject.toString(), StepListBean.class);
//                    List<StepResultBean> data = stepListBean.getData();
//                    if (data != null && data.size() != 0) {
//                        LogUtil.i("data.size()" + data.size());
//                        wearFitDataBeanList.clear();
//                        for (int i = 0; i < data.size(); i++) {
//                            SourceEntity.Source source = new SourceEntity.Source();
//                            source.setAwakeCount(0);//清醒
//                            int sum = Integer.valueOf(data.get(i).getMinutes1());
//                            int shenshui = Integer.valueOf(data.get(i).getMinutes2());
//                            source.setShallowCount(sum - shenshui);//浅睡
//                            source.setDeepCount(shenshui);//深睡
//                            source.setDayAwake(Long.valueOf(data.get(i).getState_one_count() + ""));
//                            source.setScale(100);
//                            source.setTime(Long.valueOf(data.get(i).getGroup_time()));
//                            String weekStr = TimeUtils.dateToWeek2(TimeUtils.timeStamp2Date(data.get(i).getGroup_time() + "", "yyyy-MM-dd"));
//                            switch (lableType) {//切换标签 1天  2周  3月
//                                case 2:
//                                    weekStr = TimeUtils.dateToWeek2(TimeUtils.timeStamp2Date(data.get(i).getGroup_time() + "", "yyyy-MM-dd"));
//                                    break;
//                                case 3:
//                                    weekStr = new SimpleDateFormat("d").format(new Date(Long.valueOf(data.get(i).getGroup_time()))).toString();
//                                    break;
//                            }
//                            source.setSource(weekStr);
//                            if (weekStr.equals("星期二")) {
//                                LogUtil.i("&&&&&-" + (sum - shenshui) + "-&&-" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.valueOf(data.get(i).getGroup_time()))));
//                            }
//                            source.setAllCount(source.getAwakeCount() + source.getShallowCount() + source.getDeepCount());
//                            moreList.add(source);
//                        }
//                    }
//
//                    Message message = new Message();
//                    message.what = MSG_REFRESH_DATA_MORE_DATA;
//                    handler.sendMessage(message);
//                } else {
//                    ToastUtils.showToastShort("请查看网络连接");
//                    Message message = new Message();
//                    message.what = MSG_REFRESH_DATA_MORE_DATA;
//                    handler.sendMessage(message);
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                Message message = new Message();
//                message.what = MSG_REFRESH_DATA_MORE_DATA;
//                handler.sendMessage(message);
//                ToastUtils.showToastShort(context.getResources().getText(R.string.network_connection_text).toString());
//                LogUtil.e("onErrorResponse", volleyError.toString());
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, null));
//                return map;
//            }
//        };
//        MyApplication.getHttpQueues().add(request);
    }

    private void defaultQueryStepByDay() {
//        stepBeans.clear();//睡眠数据
//        wearFitDataBeanList.clear();//本地数据库和后台共用模式
//        column_layout.removeAllViews();
//        step_detail_tv.setVisibility(View.VISIBLE);
//        step_detail_tv.setText("");
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        LogUtil.i(year + "-" + month + "-" + day);
        queryDataByDay(year + "", month + "", day + "");
    }

    private void initViewToData() {
        List<StepBean> stepListTemp = new ArrayList<>();//本地数据库和后台共用模式
        for (int i = 0; i < wearFitDataBeanList.size(); i++) {
            if (i == 0) {//第一条
                StepBean stepBean = new StepBean();
                stepBean.setStartTime(wearFitDataBeanList.get(i).getTimestamp());
                stepBean.setEndTime(0);
                stepBean.setStep(wearFitDataBeanList.get(i).getStep());
                stepBean.setCal(wearFitDataBeanList.get(i).getCal());
                stepListTemp.add(stepBean);
            } else if (i == wearFitDataBeanList.size() - 1) {//最后一条
                if (stepListTemp.get(stepListTemp.size() - 1).getEndTime() == 0) {//更改最后一条截止时间
                    StepBean stepB = stepListTemp.get(stepListTemp.size() - 1);
                    stepB.setEndTime(wearFitDataBeanList.get(i).getTimestamp());
                    stepListTemp.set(stepListTemp.size() - 1, stepB);
                }
            } else {
                //stepListTemp最后一条没有截止时间，并且步数和wearFitDataBeanList步数一样
                if (stepListTemp.get(stepListTemp.size() - 1).getEndTime() == 0) {//更改最后一条截止时间
                    StepBean stepB = stepListTemp.get(stepListTemp.size() - 1);
                    stepB.setEndTime(wearFitDataBeanList.get(i).getTimestamp());
                    stepListTemp.set(stepListTemp.size() - 1, stepB);
                }
                if (stepListTemp.get(stepListTemp.size() - 1).getStep() != wearFitDataBeanList.get(i).getStep()) {
                    StepBean stepBean2 = new StepBean();
                    stepBean2.setStartTime(wearFitDataBeanList.get(i).getTimestamp());
                    stepBean2.setEndTime(0);
                    stepBean2.setStep(wearFitDataBeanList.get(i).getStep());
                    stepBean2.setCal(wearFitDataBeanList.get(i).getCal());
                    stepListTemp.add(stepBean2);
                }
            }
        }
        for (int i = 0; i < stepListTemp.size(); i++) {
            if (i == 0) {//第一条
                stepBeans.add(stepListTemp.get(i));
            } else {
                StepBean lastStep = stepListTemp.get(i);
                int step = stepListTemp.get(i).getStep() - stepListTemp.get(i - 1).getStep();
                int cal = stepListTemp.get(i).getCal() - stepListTemp.get(i - 1).getCal();
                StepBean stepBean = new StepBean();
                stepBean.setStep(step);
                stepBean.setStartTime(lastStep.getStartTime());
                stepBean.setEndTime(lastStep.getEndTime());
                if (cal > 0) {
                    stepBean.setCal(cal);
                } else {
                    stepBean.setCal(stepListTemp.get(i).getCal());
                }
                stepBeans.add(stepBean);
            }
        }
        Collections.reverse(stepBeans);

        initChildView1();
        addChildLayout();

        adapter = new StepAdapter(context, stepBeans, wearFitUser.getStepLength());
        listview.setAdapter(adapter);
    }

    private void initChildView1() {
        float km = (float) wearFitUser.getStepLength() * (float) wearFitDataBeanList.get(wearFitDataBeanList.size() - 1).getStep() / (float) 100000.00;//100步长  1000km
        int step = wearFitDataBeanList.get(wearFitDataBeanList.size() - 1).getStep();
        int cal = wearFitDataBeanList.get(wearFitDataBeanList.size() - 1).getCal();
        NumberFormat numberFormat = NumberFormat.getInstance();// 创建一个数值格式化对象
        numberFormat.setMaximumFractionDigits(0);// 设置精确到小数点后2位
        String targetStr = numberFormat.format((float) step / (float) wearFitUser.getGold_steps() * 100);//达标
        int target = 0;
        if (StringUtils.isNumeric(targetStr)) {
            target = Integer.valueOf(targetStr);
        }
        km_tv.setText(km + "");
        step_tv.setText(step + "");
        kilocalorie_tv.setText(cal + "");
        switch (lableType) {//切换标签 1天  2周  3月
            case 1:
                //----日布局
                progressb_target.setProgress(target);
                day_layout.setVisibility(View.VISIBLE);
                more_layout.setVisibility(View.GONE);
                break;
            case 2:
                //----周月布局
                average_daily_step_tv.setText(km + "");
                standard_days_tv.setText(km + "");
                day_layout.setVisibility(View.GONE);
                more_layout.setVisibility(View.VISIBLE);
                break;
            case 3:
                //----周月布局
                average_daily_step_tv.setText(km + "");
                standard_days_tv.setText(km + "");
                day_layout.setVisibility(View.GONE);
                more_layout.setVisibility(View.VISIBLE);
                break;
        }


    }

    private void addChildLayout() {
        if (moreList != null && moreList.size() != 0) {
            setBarChart();
//            long deepSleep = 0;//深睡
//            long shallowSleep = 0;//浅睡
//            long awakeCount = 0;//清醒
//            long sleepTime = 0;//睡眠时长
//
//            for (int i = 0; i < moreList.size(); i++) {
//                shallowSleep += moreList.get(i).getShallowCount();//浅睡
//                deepSleep += moreList.get(i).getDeepCount();//深睡
//                awakeCount += moreList.get(i).getDayAwake();//清醒
//            }
//            sleepTime = shallowSleep + deepSleep;
//            initChildView2(deepSleep / moreList.size()
//                    , shallowSleep / moreList.size()
//                    , awakeCount / moreList.size()
//                    , sleepTime / moreList.size());
        }
    }

    public void setBarChart() {
        final SourceEntity sourceEntity = new SourceEntity();
//        sourceEntity.parseData();
        sourceEntity.setList(moreList);
        setYAxis(sourceEntity.getList());

        barGroup.removeAllViews();
        List<BarEntity> datas = new ArrayList<>();
        final int size = sourceEntity.getList().size();
        for (int i = 0; i < size; i++) {
            BarEntity barEntity = new BarEntity();
            SourceEntity.Source entity = sourceEntity.getList().get(i);
            String negative = mFormat.format(entity.getAwakeCount() / sourceMax);
            barEntity.setNegativePer(Float.parseFloat(negative));
            String neutral = mFormat.format(entity.getDeepCount() / sourceMax);
            barEntity.setNeutralPer(Float.parseFloat(neutral));
            String positive = mFormat.format(entity.getShallowCount() / sourceMax);
            barEntity.setPositivePer(Float.parseFloat(positive));
            barEntity.setTitle(entity.getSource());
            barEntity.setScale(entity.getScale());
            barEntity.setAllcount(entity.getAllCount());
            /*计算柱状图透明区域的比例*/
            barEntity.setFillScale(1 - entity.getAllCount() / sourceMax);
            datas.add(barEntity);
        }
        barGroup.setDatas(datas);
        //计算间距
        barGroup.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                barGroup.getViewTreeObserver().removeOnPreDrawListener(this);
                int height = findViewById(R.id.bg).getMeasuredHeight();
                final View baseLineView = findViewById(R.id.left_base_line);
                int baseLineTop = baseLineView.getTop();
                barGroup.setHeight(sourceMax, height - baseLineTop - baseLineView.getHeight() / 2);
                barGroup.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BarView barItem = (BarView) barGroup.getChildAt(0).findViewById(R.id.barView);
                        baseLineHeiht = findViewById(R.id.base_line).getTop();
                        lp = (RelativeLayout.LayoutParams) root.getLayoutParams();
                        left = baseLineView.getLeft();
                        lp.leftMargin = (int) (left + context.getResources().getDisplayMetrics().density * 3);
                        lp.topMargin = Math.abs(baseLineHeiht - barItem.getHeight());
                        root.setLayoutParams(lp);
//                        final int initHeight = barItem.getHeight();
//                        final ObjectAnimator anim = ObjectAnimator.ofFloat(barItem, "zch", 0.0F, 1.0F).setDuration(1500);
//                        final LinearLayout.LayoutParams barLP= (LinearLayout.LayoutParams) barItem.getLayoutParams();
//                        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                            @Override
//                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                                float cVal = (Float) anim.getAnimatedValue();
//                                barLP.height = (int) (initHeight * cVal);
//                                barItem.setLayoutParams(barLP);
//                            }
//                        });
//                        anim.start();
                    }
                }, 0);

                for (int i = 0; i < size; i++) {
                    final BarView barItem = (BarView) barGroup.getChildAt(i).findViewById(R.id.barView);
                    final int finalI = i;
                    barItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final float top = view.getHeight() - barItem.getFillHeight();
                            SourceEntity.Source ss = sourceEntity.getList().get(finalI);
                            String showText = "深睡：" + (int) ss.getDeepCount() + "分\n"
                                    + "浅睡：" + (int) ss.getShallowCount() + "分\n"
                                    + "清醒：" + (int) ss.getAwakeCount() + "分";
                            ((TextView) popView.findViewById(R.id.txt)).setText(showText);
                            showPop(barItem, top);
                        }
                    });
                }
                return false;
            }
        });
    }

    private void setYAxis(List<SourceEntity.Source> list) {
//        sourceMax = list.get(0).getAllCount();
        sourceMax = 750;
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i).getAllCount() > sourceMax) {
                sourceMax = list.get(i).getAllCount();
            }
        }
        ((TextView) findViewById(R.id.tv_num1)).setText((int) sourceMax / 3 + "");
        ((TextView) findViewById(R.id.tv_num2)).setText((int) sourceMax * 2 / 3 + "");
        ((TextView) findViewById(R.id.tv_num3)).setText((int) sourceMax + "");
    }

    private int initPopHeitht = 0;

    @SuppressLint("NewApi")
    private void showPop(final View barItem, final float top) {
        if (popupWindow != null)
            popupWindow.dismiss();
        popupWindow = null;
        popupWindow = new PopupWindow(popView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(barItem, barItem.getWidth() / 2, -((int) top + initPopHeitht));
        if (initPopHeitht == 0) {
            popView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    popView.getViewTreeObserver().removeOnPreDrawListener(this);
                    initPopHeitht = popView.getHeight();
                    popupWindow.update(barItem, barItem.getWidth() / 2, -((int) top + initPopHeitht),
                            popupWindow.getWidth(), popupWindow.getHeight());
                    return false;
                }
            });
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case MSG_REFRESH_DATA_DATA:
//                    initViewToData();
                    break;
                case MSG_REFRESH_DATA_MORE_DATA:
//                    if (moreList != null && moreList.size() != 0) {
//                        setBarChart();
//                        long deepStep = 0;//深睡
//                        long shallowStep = 0;//浅睡
//                        long awakeCount = 0;//清醒
//                        long stepTime = 0;//睡眠时长
//
//                        for (int i = 0; i < moreList.size(); i++) {
//                            shallowStep += moreList.get(i).getShallowCount();//浅睡
//                            deepStep += moreList.get(i).getDeepCount();//深睡
//                            awakeCount += moreList.get(i).getDayAwake();//清醒
//                        }
//                        stepTime = shallowStep + deepStep;
//                        initChildView2(deepStep / moreList.size()
//                                , shallowStep / moreList.size()
//                                , awakeCount / moreList.size()
//                                , stepTime / moreList.size());
//                    }
                    break;
                default:
                    break;
            }
        }
    };
}
