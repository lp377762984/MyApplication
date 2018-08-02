//package com.cn.danceland.myapplication.shouhuan.activity;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.annotation.Nullable;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.StringRequest;
//import com.cn.danceland.myapplication.MyApplication;
//import com.cn.danceland.myapplication.R;
//import com.cn.danceland.myapplication.db.HeartRate;
//import com.cn.danceland.myapplication.db.HeartRateHelper;
//import com.cn.danceland.myapplication.shouhuan.bean.HeartRateBean;
//import com.cn.danceland.myapplication.shouhuan.bean.HeartRateLastBean;
//import com.cn.danceland.myapplication.shouhuan.bean.HeartRatePostBean;
//import com.cn.danceland.myapplication.shouhuan.bean.HeartRateResultBean;
//import com.cn.danceland.myapplication.utils.Constants;
//import com.cn.danceland.myapplication.utils.LogUtil;
//import com.cn.danceland.myapplication.utils.SPUtils;
//import com.cn.danceland.myapplication.utils.StringUtils;
//import com.cn.danceland.myapplication.utils.TimeUtils;
//import com.cn.danceland.myapplication.utils.ToastUtils;
//import com.cn.danceland.myapplication.view.DongLanTitleView;
//import com.cn.danceland.myapplication.view.HorizontalPickerView;
//import com.google.gson.Gson;
//
//import org.json.JSONObject;
//
//import java.text.NumberFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import lecho.lib.hellocharts.gesture.ZoomType;
//import lecho.lib.hellocharts.model.Axis;
//import lecho.lib.hellocharts.model.AxisValue;
//import lecho.lib.hellocharts.model.Line;
//import lecho.lib.hellocharts.model.LineChartData;
//import lecho.lib.hellocharts.model.PointValue;
//import lecho.lib.hellocharts.model.ValueShape;
//import lecho.lib.hellocharts.model.Viewport;
//import lecho.lib.hellocharts.view.LineChartView;
//
///**
// * 心率
// * Created by yxx on 2018/7/18.
// */
//public class WearFitHeartRateActivity extends Activity implements View.OnClickListener {
//    private int lableType = 1;//切换标签 1天  2周  3月
//    protected final int MSG_LABLE_TYPE_DATA = 0;
//    private Context context;
//    private DongLanTitleView heart_title;//心率title
//    private CheckBox day_checkBox;//日
//    private CheckBox week_checkBox;//周
//    private CheckBox month_checkBox;//年
//    private TextView heart_success_percentage_tv;//达标率
//    private TextView heart_success_count_tv;//达标次数
//    private ProgressBar heart_success_pro;//达标率进度条
//    private TextView heart_abnormal_percentage_tv;//异常率
//    private TextView heart_abnormal_count_tv;//异常次数
//    private ProgressBar heart_abnormal_pro;//异常率进度条
//    private TextView heart_average_tv;//平均心率
//    private LineChartView lineChart; //折线
//    private HorizontalPickerView picker;//水平选择器
//    private View leftImageView;//选择器左箭头
//    private View rightImageView;//选择器右箭头
//
//    private List<HeartRate> heartRates = new ArrayList<>();//心率数据 HeartRate
//    private ArrayList<String> pickerList = new ArrayList<>();//选择器数据
//    private List<HeartRateResultBean> allHeartRates = new ArrayList<>();//最后一条后面所有的心率数据 ALL
//    private List<PointValue> mPointValues = new ArrayList<PointValue>();//折线数据list
//    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();//折线X轴list
//    private List<AxisValue> mAxisYValues = new ArrayList<AxisValue>();//折线Y轴list
//
//    private List<Date> ds = TimeUtils.getSegmentationTime(5, new Date());//默认轴数据
//    private String lastData = "";//选择器上次滚动的数据
//
//    HeartRateHelper heartRateHelper = new HeartRateHelper();
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_wearfit_heartrate);
//        context = this;
//        initView();
//    }
//
//    private void initView() {
//        heart_title = findViewById(R.id.shouhuan_title);
//        heart_title.setTitle("心率");
//        day_checkBox = (CheckBox) findViewById(R.id.day_checkBox);//日
//        week_checkBox = (CheckBox) findViewById(R.id.week_checkBox);//周
//        month_checkBox = (CheckBox) findViewById(R.id.month_checkBox);//年
//        lineChart = (LineChartView) findViewById(R.id.line_chart);//折线
//        heart_success_percentage_tv = (TextView) findViewById(R.id.heart_success_percentage_tv);//达标率
//        heart_success_count_tv = (TextView) findViewById(R.id.heart_success_count_tv);//达标次数
//        heart_success_pro = (ProgressBar) findViewById(R.id.heart_success_pro);//达标率进度条
//        heart_abnormal_percentage_tv = (TextView) findViewById(R.id.heart_abnormal_percentage_tv);//异常率
//        heart_abnormal_count_tv = (TextView) findViewById(R.id.heart_abnormal_count_tv);//异常次数
//        heart_abnormal_pro = (ProgressBar) findViewById(R.id.heart_abnormal_pro);//异常率进度条
//        heart_average_tv = (TextView) findViewById(R.id.heart_average_tv);//平均心率
//        picker = (HorizontalPickerView) findViewById(R.id.scrollPicker);//水平选择器
//
//        heart_success_count_tv.setText(0 + context.getResources().getString(R.string.count_text));
//        heart_success_pro.setProgress(0);
//        heart_abnormal_count_tv.setText(0 + context.getResources().getString(R.string.count_text));
//        heart_abnormal_pro.setProgress(0);
//        day_checkBox.setOnCheckedChangeListener(onCheckedChangeListener);
//        week_checkBox.setOnCheckedChangeListener(onCheckedChangeListener);
//        month_checkBox.setOnCheckedChangeListener(onCheckedChangeListener);
//        initPickerDay();//默认日数据
//
//        Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH) + 1;
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        LogUtil.i(year + "-" + month + "-" + day);
//        queryHeartByDay(year + "", month + "", day + "");
//        getLastHeart();//服务器最后心率
//        picker.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
//                    String lableText = picker.getSelectedString();//得到触摸值
//                    if (lastData == "") {
//                        lastData = picker.getSelectedString();//第一次赋值
//                    }
//                    if (!lastData.equals(lableText)) {//最后一次和本次滚动的值不相等请求
//                        heartRates.clear();//心率数据 HeartRate
//                        mPointValues.clear();
//                        switch (lableType) {//切换标签 1天  2周  3月
//                            case 1:
//                                String[] splitDay = null;
//                                splitDay = lableText.split("\\.");
//                                Calendar calendar = Calendar.getInstance();
//                                calendar.set(Calendar.MONTH, Integer.valueOf(splitDay[0]));
//                                calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(splitDay[1]));
//                                int year = calendar.get(Calendar.YEAR);
//                                int month = calendar.get(Calendar.MONTH);
//                                int day = calendar.get(Calendar.DAY_OF_MONTH);
//                                LogUtil.i(year + "-" + month + "-" + day);
//                                queryHeartByDay(year + "", month + "", day + "");
//
//                                break;
//                            case 2:
//                                lableText += "---周";
//                                break;
//                            case 3:
//                                lableText += "---月";
//                                break;
//                        }
//                    }
//                    lastData = picker.getSelectedString();
//                }
//                return false;
//            }
//        });
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.iv_left:
//                picker.setAnLeftOffset();
//                break;
//            case R.id.iv_right:
//                picker.setAnRightOffset();
//                break;
//            default:
//                break;
//        }
//    }
//
//    CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
//        @Override
//        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//            String text = compoundButton.getText().toString();
//            Toast.makeText(context, text, Toast.LENGTH_LONG).show();
//            switch (compoundButton.getId()) {
//                case R.id.day_checkBox://日
//                    initPickerDay();
//                    lableType = 1;//切换标签 1天  2周  3月
//                    day_checkBox.setTextColor(context.getResources().getColor(R.color.color_dl_yellow));
//                    week_checkBox.setTextColor(context.getResources().getColor(R.color.colorGray8));
//                    month_checkBox.setTextColor(context.getResources().getColor(R.color.colorGray8));
//                    break;
//                case R.id.week_checkBox://周
//                    initPickerWeek();
//                    lableType = 2;//切换标签 1天  2周  3月
//                    day_checkBox.setTextColor(context.getResources().getColor(R.color.colorGray8));
//                    week_checkBox.setTextColor(context.getResources().getColor(R.color.color_dl_yellow));
//                    month_checkBox.setTextColor(context.getResources().getColor(R.color.colorGray8));
//                    break;
//                case R.id.month_checkBox://月
//                    initPickerMonth();
//                    lableType = 3;//切换标签 1天  2周  3月
//                    day_checkBox.setTextColor(context.getResources().getColor(R.color.colorGray8));
//                    week_checkBox.setTextColor(context.getResources().getColor(R.color.colorGray8));
//                    month_checkBox.setTextColor(context.getResources().getColor(R.color.color_dl_yellow));
//                    break;
//            }
//        }
//    };
//
//    /**
//     * 初始化选择器 日数据
//     */
//    private void initPickerDay() {
//        pickerList.clear();
//        ArrayList<String> dayDataStr = new ArrayList<>();//日
//        dayDataStr = TimeUtils.getLastMonthDayData(new SimpleDateFormat("M.d"));//日
//        if (dayDataStr != null && dayDataStr.size() != 0) {
//            for (int i = 0; i < dayDataStr.size(); i++) {
//                pickerList.add(dayDataStr.get(i));
//            }
//        }
//        picker.setData(pickerList);
//        picker.setSelectNum(pickerList.size()/2);//时间选择器偏移
//    }
//
//    /**
//     * 初始化选择器 周数据
//     */
//    private void initPickerWeek() {
//        pickerList.clear();
//        ArrayList<String> weekDataStr = new ArrayList<>();//周
//        weekDataStr = TimeUtils.getWeekListData(2000);//周
//        if (weekDataStr != null && weekDataStr.size() != 0) {
//            for (int i = 0; i < weekDataStr.size(); i++) {
//                String[] temp = null;
//                temp = weekDataStr.get(i).split("-");
//                if (temp != null && temp.length == 2) {
//                    String wStr = new SimpleDateFormat("M.d").format(Long.valueOf(temp[0])).toString() + "-"
//                            + new SimpleDateFormat("M.d").format(Long.valueOf(temp[1])).toString();
//                    pickerList.add(wStr);
//                }
//            }
//        }
//        picker.setData(pickerList);
//    }
//
//    /**
//     * 初始化选择器 月数据
//     */
//    private void initPickerMonth() {
//        pickerList.clear();
//        Calendar calendar = Calendar.getInstance();
//        calendar.get(Calendar.MONTH);
//        for (int i = 1; i < 13; i++) {//12个月
//            pickerList.add(calendar.get(Calendar.YEAR) + "." + i);
//        }
//        picker.setData(pickerList);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }
//
//    private HeartRateResultBean lastHeart;//服务器最后心率
//
//    //服务器最后心率
//    private void getLastHeart() {
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.QUERY_WEAR_FIT_HEART_RATE_FANDLAST, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                LogUtil.i("服务器最后心率" + s);
//                HeartRateLastBean heartRateLastBean = new Gson().fromJson(s, HeartRateLastBean.class);
//                lastHeart = heartRateLastBean.getData();//服务器最后心率
//                queryAllHeartByDay(lastHeart.getTimestamp());//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                if (volleyError != null) {
//                    LogUtil.i(volleyError.toString());
//                } else {
//                    LogUtil.i("NULL");
//                }
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String, String> map = new HashMap<>();
//                return map;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, null));
//                return map;
//            }
//        };
//        MyApplication.getHttpQueues().add(stringRequest);
//    }
//
//    private void queryAllHeartByDay(long date1Time) {//最后一条心率数据时间  获取本地数据库这时间后的数据
//        Date date1 = new Date(date1Time);//后台时间
//        Date date2 = new Date();
//        int day = Integer.valueOf(TimeUtils.getInterval(date1, date2));// 计算差多少天
//        for (int i = 0; i < day+1; i++) {
//            List<HeartRate> hrList = new ArrayList<>();
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(new Date(date1Time));
//            hrList = heartRateHelper.queryByDay(TimeUtils.date2TimeStamp(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + (calendar.get(Calendar.DAY_OF_MONTH)+i), "yyyy-MM-dd"));//获取本地数据库心率
//            for (int j = 0; j < hrList.size(); j++) {
//                HeartRateResultBean heartRateResultBean = new HeartRateResultBean();//提交对象
//                heartRateResultBean.setYear(new SimpleDateFormat("yyyy").format(new Date(hrList.get(j).getDate())).toString());//年
//                heartRateResultBean.setMonth(new SimpleDateFormat("MM").format(new Date(hrList.get(j).getDate())).toString());//月
//                heartRateResultBean.setDay(new SimpleDateFormat("dd").format(new Date(hrList.get(j).getDate())).toString());//日
//                heartRateResultBean.setHour(new SimpleDateFormat("HH").format(new Date(hrList.get(j).getDate())).toString());//时
//                heartRateResultBean.setMinute(new SimpleDateFormat("mm").format(new Date(hrList.get(j).getDate())).toString());//分
//                heartRateResultBean.setMax_value(hrList.get(j).getHeartRate() + "");//心率
//                heartRateResultBean.setTimestamp(hrList.get(j).getDate());//long 时间戳
//                allHeartRates.add(heartRateResultBean);
//            }
//        }
//        isPostHeart();
//    }
//
//    /**
//     * 提交心率  先请求后台最后一条   对比本地   提交剩下未提交的数据
//     */
//    private void isPostHeart() {
//        List<HeartRateResultBean> postHeartList = new ArrayList<>();
//        for (int i = 0; i < allHeartRates.size(); i++) {
//            if (lastHeart != null) {
//                if (lastHeart.getTimestamp() != 0) {//不为空
//                    Date date1 = new Date(lastHeart.getTimestamp());//后台时间
//                    Date date2 = new Date(allHeartRates.get(i).getTimestamp());
//                    LogUtil.i("比较" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date1) + "**"
//                            + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date2));
//                    if (date1.before(date2)){ //表示date1小于date2  最后一条心率日期小于手环date2
//                        postHeartList.add(allHeartRates.get(i));
//                    }
//                } else {
//                    postHeartList = allHeartRates;
//                }
//            } else {
//                postHeartList = allHeartRates;
//            }
//        }
//        if (postHeartList != null && postHeartList.size() != 0) {
//            LogUtil.i("提交心率" + postHeartList.size());
//            postHeart(postHeartList);
//        }
//    }
//    private void postHeart(List<HeartRateResultBean> postHeartList) {
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.QUERY_WEAR_FIT_HEART_RATE_SAVE
//                , new Gson().toJson(postHeartList), new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject jsonObject) {
//                LogUtil.i("提交返回" + jsonObject.toString());
//                if (jsonObject.toString().contains("true")) {
//                    LogUtil.i("提交成功");
//                } else {
//                    LogUtil.i("提交失败");
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                LogUtil.e("onErrorResponse", volleyError.toString());
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> map = new HashMap<String, String>();
//                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));
//                return map;
//            }
//        };
//        MyApplication.getHttpQueues().add(jsonObjectRequest);
//    }
//
//    /**
//     * 查询心率
//     *
//     * @param year
//     * @param month
//     * @param day
//     */
//    private void queryHeartByDay(String year, String month, String day) {
//        heartRates = heartRateHelper.queryByDay(TimeUtils.date2TimeStamp(year + "-" + month + "-" + day, "yyyy-MM-dd"));//获取本地数据库心率
//        //  LogUtil.i(TimeUtils.date2TimeStamp(year+"-"+month+"-"+day+" 00:00:00","yyyy-MM-dd HH:mm:ss")+"");
//        LogUtil.i("本地数据库共有个" + heartRates.size());
//        if (heartRates != null && heartRates.size() != 0) {
//            getAxisXLables();//获取x轴的标注
//            getAxisYLables();//获取y轴的标注
//            getAxisPoints();//获取坐标点
//            initLineChart();//初始化
//            setHeartView(heartRates);
//        } else {
//            HeartRatePostBean heartRatePostBean = new HeartRatePostBean();
//            heartRatePostBean.setYear(year);
//            heartRatePostBean.setMonth(month);
//            heartRatePostBean.setDay(day);
//            LogUtil.i("请求后台心率" + heartRatePostBean.toString());
//            //获取后台数据
//            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.QUERY_WEAR_FIT_HEART_RATE_LIST
//                    , new Gson().toJson(heartRatePostBean), new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject jsonObject) {
//                    LogUtil.i("jsonObject" + jsonObject.toString());
//                    if (jsonObject.toString().contains("true")) {
//                        HeartRateBean heartRateBean = new Gson().fromJson(jsonObject.toString(), HeartRateBean.class);
//                        List<HeartRateResultBean> data = heartRateBean.getData();
//                        if (data != null && data.size() != 0) {
//                            LogUtil.i("data.size()" + data.size());
//                            heartRates.clear();
//                            for (int i = 0; i < data.size(); i++) {
//                                HeartRate heartRate = new HeartRate();
//                                String time = data.get(i).getYear() + "-" + data.get(i).getMonth() + "-" + data.get(i).getDay() + " "
//                                        + data.get(i).getHour() + ":" + data.get(i).getMinute() + ":" + "00";
//                                heartRate.setDate(TimeUtils.date2TimeStamp(time, "yyyy-MM-dd HH:mm:ss"));
//                                heartRate.setHeartRate(Integer.valueOf(data.get(i).getMax_value()));
//                                heartRates.add(heartRate);
//                            }
//                        }
//                        Message message = new Message();
//                        message.what = MSG_LABLE_TYPE_DATA;
//                        handler.sendMessage(message);
//                    } else {
//                        ToastUtils.showToastShort("请查看网络连接");
//                        Message message = new Message();
//                        message.what = MSG_LABLE_TYPE_DATA;
//                        handler.sendMessage(message);
//                    }
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError volleyError) {
//                    Message message = new Message();
//                    message.what = MSG_LABLE_TYPE_DATA;
//                    handler.sendMessage(message);
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
//    }
//
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message message) {
//            switch (message.what) {
//                case MSG_LABLE_TYPE_DATA:
//                    getAxisXLables();//获取x轴的标注
//                    getAxisYLables();//获取y轴的标注
//                    getAxisPoints();//获取坐标点
//                    initLineChart();//初始化
//                    setHeartView(heartRates);
//                    break;
//                default:
//                    break;
//            }
//        }
//    };
//
//    /**
//     * 折线图下方布局
//     */
//    private void setHeartView(List<HeartRate> heartRatesTemp) {
//        List<HeartRate> successHeartRates = new ArrayList<>();//达标
//        List<HeartRate> abnormalHeartRates = new ArrayList<>();//异常
//        int averageHeart = 0; //平均心率：全天总心率除总心率个数
//
//        for (int i = 0; i < heartRatesTemp.size(); i++) {
//            int hTemp = Integer.valueOf(heartRatesTemp.get(i).getHeartRate());
//            averageHeart += hTemp;
//            if (50 < hTemp && hTemp < 130) {//达标范围50~xx~130   不包含50、130
//                successHeartRates.add(heartRatesTemp.get(i)); //达标率：全天总达标心率除总达标数  达标范围50~xx~130   不包含50、130
//            } else {
//                abnormalHeartRates.add(heartRatesTemp.get(i)); //异常率：全天总异常心率除总异常数
//            }
//        }
//
//        NumberFormat numberFormat = NumberFormat.getInstance();// 创建一个数值格式化对象
//        numberFormat.setMaximumFractionDigits(0);// 设置精确到小数点后2位
//        String successHeart = numberFormat.format((float) successHeartRates.size() / (float) heartRatesTemp.size() * 100);//达标
//        String abnormalHeart = numberFormat.format((float) abnormalHeartRates.size() / (float) heartRatesTemp.size() * 100);//异常
//
//        heart_success_count_tv.setText(successHeartRates.size() + context.getResources().getString(R.string.count_text));
//        heart_abnormal_count_tv.setText(abnormalHeartRates.size() + context.getResources().getString(R.string.count_text));
//        if (StringUtils.isNumeric(successHeart)) {
//            heart_success_percentage_tv.setText(successHeart + context.getResources().getString(R.string.percentage_text));//达标
//            heart_success_pro.setProgress(Integer.valueOf(successHeart));
//        }
//        if (StringUtils.isNumeric(abnormalHeart)) {
//            heart_abnormal_percentage_tv.setText(abnormalHeart + context.getResources().getString(R.string.percentage_text));//异常
//            heart_abnormal_pro.setProgress(Integer.valueOf(abnormalHeart));
//        }
//        if (StringUtils.isNumeric(averageHeart + "")) {
//            if (averageHeart != 0) {
//                heart_average_tv.setText(averageHeart / heartRatesTemp.size() + "");//平均心率
//            }
//        }
//    }
//
//    /**
//     * 初始化LineChart的一些设置
//     */
//    private void initLineChart() {
//        Line line = new Line(mPointValues).setColor(Color.parseColor("#ff6600"));  //折线的颜色
//        List<Line> lines = new ArrayList<Line>();
//        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
//        line.setCubic(false);//曲线是否平滑
//        line.setStrokeWidth(1);//线条的粗细，默认是3
//        line.setPointRadius(2);//点的大小
//        line.setFilled(false);//是否填充曲线的面积
//        line.setHasLabels(true);//曲线的数据坐标是否加上备注
////		line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
//        line.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
//        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示
//        lines.add(line);
//        LineChartData data = new LineChartData();
//        data.setLines(lines);
//
//        //坐标轴
//        Axis axisX = new Axis(); //X轴
//        Axis axisY = new Axis();  //Y轴
//        axisX.setName("");  //表格名称
//        axisY.setName("");//y轴标注
//        axisX.setHasTiltedLabels(true);  //X轴下面坐标轴字体是斜的显示还是直的，true是斜的显示
//        axisY.setHasTiltedLabels(false);//true表示斜的
//        axisX.setTextColor(Color.parseColor("#666666"));//设置字体颜色
//        axisY.setTextColor(Color.parseColor("#666666"));//设置字体颜色
//        axisX.setTextSize(11);//设置字体大小
//        axisY.setTextSize(11);//设置字体大小
//        axisX.setMaxLabelChars(7); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisValues.length
//        axisY.setMaxLabelChars(7);//最多几个y轴坐标，意思就是你的缩放让y轴上数据的个数7<=y<=mAxisValues.length
//        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
//        axisY.setValues(mAxisYValues); //设置Y轴各个坐标点名称
//        axisX.setHasLines(true); //x 轴分割线
//        axisY.setHasLines(false); //y 轴分割线
//        data.setAxisXBottom(axisX); //x 轴在底部
//        data.setAxisYLeft(axisY);  //Y轴设置在左边
//        //data.setAxisXTop(axisX);  //x 轴在顶部
//        //data.setAxisYRight(axisY);  //y轴设置在右边
//
//        //设置行为属性，支持缩放、滑动以及平移
//        lineChart.setInteractive(true);//设置该图表是否可交互。如不可交互，则图表不会响应缩放、滑动、选择或点击等操作。默认值为true，可交互。
//        lineChart.setZoomType(ZoomType.HORIZONTAL);  //缩放类型，水平
//        lineChart.setMaxZoom((float) 3);//缩放比例,默认值20。
//        lineChart.setLineChartData(data);
//        lineChart.setVisibility(View.VISIBLE);
////        lineChart.setZoomEnabled(false);//设置是否可缩放。
////        lineChart.setScrollEnabled(true);//设置是否可滑动。
////        lineChart.setValueTouchEnabled(false);//设置是否允许点击图标上的值，默认为true。
////        lineChart.startDataAnimation();//开始以动画的形式更新图表数据。
//
//        /**注：下面的7，10只是代表一个数字去类比而已
//         * 尼玛搞的老子好辛苦！！！见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
//         * 下面几句可以设置X轴数据的显示个数（x轴0-7个数据），当数据点个数小于（29）的时候，缩小到极致hellochart默认的是所有显示。当数据点个数大于（29）的时候，
//         * 若不设置axisX.setMaxLabelChars(int count)这句话,则会自动适配X轴所能显示的尽量合适的数据个数。
//         * 若设置axisX.setMaxLabelChars(int count)这句话,
//         * 33个数据点测试，若 axisX.setMaxLabelChars(10);里面的10大于v.right= 7; 里面的7，则
//         刚开始X轴显示7条数据，然后缩放的时候X轴的个数会保证大于7小于10
//         若小于v.right= 7;中的7,反正我感觉是这两句都好像失效了的样子 - -!
//         * 并且Y轴是根据数据的大小自动设置Y轴上限
//         * 若这儿不设置 v.right= 7; 这句话，则图表刚开始就会尽可能的显示所有数据，交互性太差
//         */
//        Viewport v = new Viewport(lineChart.getMaximumViewport());
//        v.bottom = 0;
//        v.top = 210;
//        lineChart.setMaximumViewport(v);//固定Y轴的范围,如果没有这个,Y轴的范围会根据数据的最大值和最小值决定
//        v.left = 0;
//        v.right = 7;
//        lineChart.setCurrentViewport(v);//这2个属性的设置一定要在lineChart.setMaximumViewport(v);这个方法之后,不然显示的坐标数据是不能左右滑动查看更多数据的
//    }
//
//    /**
//     * X 轴的显示
//     */
//    private void getAxisXLables() {
//        mAxisXValues.clear();//重新绘图的时候，clear 避免旧数据残留问题
//        if (heartRates != null && heartRates.size() != 0) {//手环无数据展示X轴
//            for (int i = 0; i < heartRates.size(); i++) {
//                mAxisXValues.add(new AxisValue(i).setLabel( new SimpleDateFormat(" HH:mm").format(heartRates.get(i).getDate()).toString()));
//            }
//        } else {
//            for (int i = 0; i < ds.size(); i++) {
//                mAxisXValues.add(new AxisValue(i).setLabel( new SimpleDateFormat(" HH:mm").format(ds.get(i)).toString()));
//            }
//        }
//    }
//
//    /**
//     * Y 轴的显示
//     */
//    private void getAxisYLables() {
//        mAxisYValues.clear();//重新绘图的时候，clear 避免旧数据残留问题
//        for (int i = 0; i < 245; i += 35) {
//            mAxisYValues.add(new AxisValue(i).setLabel(i + ""));
//        }
//    }
//
//    /**
//     * 图表的每个点的显示
//     */
//    private void getAxisPoints() {
//        mPointValues.clear();//重新绘图的时候，clear 避免旧数据残留问题
//        for (int i = 0; i < heartRates.size(); i++) {
//            mPointValues.add(new PointValue(i, heartRates.get(i).getHeartRate()));
//        }
//    }
//}