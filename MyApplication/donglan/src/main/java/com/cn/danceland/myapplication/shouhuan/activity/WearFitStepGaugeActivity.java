package com.cn.danceland.myapplication.shouhuan.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.db.WearFitSleepBean;
import com.cn.danceland.myapplication.db.WearFitSleepHelper;
import com.cn.danceland.myapplication.shouhuan.adapter.StepGaugeAdapter;
import com.cn.danceland.myapplication.shouhuan.bean.HeartRatePostBean;
import com.cn.danceland.myapplication.shouhuan.bean.SleepListBean;
import com.cn.danceland.myapplication.shouhuan.bean.SleepMorePostBean;
import com.cn.danceland.myapplication.shouhuan.bean.SleepResultBean;
import com.cn.danceland.myapplication.shouhuan.chart.SourceEntity;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.TimeUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.cn.danceland.myapplication.view.DongLanTitleView;
import com.cn.danceland.myapplication.view.HorizontalPickerView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 计步
 * Created by yxx on 2018/8/13.
 */

public class WearFitStepGaugeActivity extends Activity {
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

    PopupWindow mPopupWindow;

    private ArrayList<String> pickerList = new ArrayList<>();//选择器数据
    private List<String> dayPickerList = new ArrayList<>();//日选择器对应的时间戳  开始-截止  -连接
    private List<String> weekPickerList = new ArrayList<>();//周选择器对应的时间戳  开始-截止  -连接
    private List<String> monthPickerList = new ArrayList<>();//月选择器对应的时间戳  开始-截止  -连接
    private List<WearFitSleepBean> wearFitDataBeanList = new ArrayList<>();//本地数据库和后台共用模式
//    private List<WearFitSleepBean> sleepBeans = new ArrayList<>();//本地数据库和后台共用模式
    private List<SourceEntity.Source> moreList = new ArrayList<>();//柱形图数据

    private WearFitSleepHelper sleepHelper = new WearFitSleepHelper();

    private StepGaugeAdapter adapter;
    private String lastData = "";//选择器上次滚动的数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_gauge);
        context = this;
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
                defaultQuerySleepByDay();
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
//        sleepBeans.clear();//睡眠数据
//        wearFitSleepBeanList.clear();//本地数据库和后台共用模式
//        column_layout.removeAllViews();
//        sleep_detail_tv.setVisibility(View.VISIBLE);
//        sleep_detail_tv.setText("");
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
//        deep_sleep_two_tv.setText(context.getResources().getString(R.string.sleep_line_text));//深睡
//        shallow_sleep_two_tv.setText(context.getResources().getString(R.string.sleep_line_text));//浅睡
//        sleep_time_tv.setText(context.getResources().getString(R.string.sleep_line_text));//睡眠时长
//        sleep_quality_tv.setText(context.getResources().getString(R.string.sleep_line_text));//睡眠质量
//        wearFitDataBeanList = sleepHelper.queryByDay(TimeUtils.date2TimeStamp(year + "-" + month + "-" + day, "yyyy-MM-dd"));//获取本地数据库心率
//        LogUtil.i("本地数据库共有个" + wearFitDataBeanList.size());
//        if (wearFitDataBeanList != null && wearFitDataBeanList.size() != 0) {
////            initViewToData();
//        } else {
//            HeartRatePostBean heartRatePostBean = new HeartRatePostBean();
//            heartRatePostBean.setYear(year);
//            heartRatePostBean.setMonth(month);
//            heartRatePostBean.setDay(day);
//            LogUtil.i("请求后台心率" + heartRatePostBean.toString());
//            //获取后台数据
//            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.QUERY_WEAR_FIT_SLEEP_LIST
//                    , new Gson().toJson(heartRatePostBean), new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject jsonObject) {
//                    LogUtil.i("jsonObject" + jsonObject.toString());
//                    if (jsonObject.toString().contains("true")) {
//                        SleepListBean sleepListBean = new Gson().fromJson(jsonObject.toString(), SleepListBean.class);
//                        List<SleepResultBean> data = sleepListBean.getData();
//                        if (data != null && data.size() != 0) {
//                            LogUtil.i("data.size()" + data.size());
//                            wearFitDataBeanList.clear();
//                            for (int i = 0; i < data.size(); i++) {
//                                String time = data.get(i).getYear() + "-" + data.get(i).getMonth() + "-" + data.get(i).getDay() + " "
//                                        + data.get(i).getHour() + ":" + data.get(i).getMinute() + ":" + "00";
//                                WearFitSleepBean wfsb = new WearFitSleepBean();
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
//        }
    }

    /**
     * 查询心率 周 月
     *
     * @param timestamp_gt 开始时间
     * @param timestamp_lt 截止时间
     */
    private void querysDataByWeekOrMonth(String timestamp_gt, String timestamp_lt) {
//        more_deep_sleep_two_tv.setText(context.getResources().getString(R.string.sleep_line_text));//深睡
//        more_shallow_sleep_two_tv.setText(context.getResources().getString(R.string.sleep_line_text));//浅睡
//        more_sleep_time_tv.setText(context.getResources().getString(R.string.sleep_line_text));//睡眠时长
//        more_sleep_quality_tv.setText(context.getResources().getString(R.string.sleep_line_text));//睡眠质量
//        moreList.clear();
//        barGroup.removeAllViews();
//        SleepMorePostBean weekPostBean = new SleepMorePostBean();
//        weekPostBean.setGroup_time_gt(timestamp_gt);
//        weekPostBean.setGroup_time_lt(timestamp_lt);
//        LogUtil.i("请求后台心率" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.valueOf(timestamp_gt))) + "-"
//                + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.valueOf(timestamp_lt))));
//        //获取后台数据
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.QUERY_WEAR_FIT_SLEEP_FINDSUM
//                , new Gson().toJson(weekPostBean), new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject jsonObject) {
//                LogUtil.i("jsonObject" + jsonObject.toString());
//                if (jsonObject.toString().contains("true")) {
//                    SleepListBean sleepListBean = new Gson().fromJson(jsonObject.toString(), SleepListBean.class);
//                    List<SleepResultBean> data = sleepListBean.getData();
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

    private void defaultQuerySleepByDay() {
//        sleepBeans.clear();//睡眠数据
//        wearFitDataBeanList.clear();//本地数据库和后台共用模式
//        column_layout.removeAllViews();
//        sleep_detail_tv.setVisibility(View.VISIBLE);
//        sleep_detail_tv.setText("");
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        LogUtil.i(year + "-" + month + "-" + day);
        queryDataByDay(year + "", month + "", day + "");
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
//                        long deepSleep = 0;//深睡
//                        long shallowSleep = 0;//浅睡
//                        long awakeCount = 0;//清醒
//                        long sleepTime = 0;//睡眠时长
//
//                        for (int i = 0; i < moreList.size(); i++) {
//                            shallowSleep += moreList.get(i).getShallowCount();//浅睡
//                            deepSleep += moreList.get(i).getDeepCount();//深睡
//                            awakeCount += moreList.get(i).getDayAwake();//清醒
//                        }
//                        sleepTime = shallowSleep + deepSleep;
//                        initChildView2(deepSleep / moreList.size()
//                                , shallowSleep / moreList.size()
//                                , awakeCount / moreList.size()
//                                , sleepTime / moreList.size());
//                    }
                    break;
                default:
                    break;
            }
        }
    };
}
