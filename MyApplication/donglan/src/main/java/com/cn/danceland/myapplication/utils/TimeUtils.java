package com.cn.danceland.myapplication.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.necer.ncalendar.utils.MyLog;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;


/**
 * Created by shy on 2017/11/27 14:35
 * Email:644563767@qq.com
 * 处理时间和日期的工具类
 */

@SuppressLint("WrongConstant")
public class TimeUtils {
    private static SimpleDateFormat DATE_FORMAT_TILL_SECOND = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    private static SimpleDateFormat DATE_FORMAT_TILL_DAY_CURRENT_YEAR = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat DATE_FORMAT_TILL_DAY_CH = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    /**
     * 日期字符串转换为Date
     *
     * @param dateStr
     * @param format
     * @return
     */
    public static Date strToDate(String dateStr, String format) {
        Date date = null;

        if (!TextUtils.isEmpty(dateStr)) {
            DateFormat df = new SimpleDateFormat(format);
            try {
                date = df.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    /**
     * 日期转换为字符串
     *
     * @param timeStr
     * @param format
     * @return
     */
    public static String dateToString(String timeStr, String format) {
        // 判断是否是今年
        Date date = TimeUtils.strToDate(timeStr, format);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 如果是今年的话，才去“xx月xx日”日期格式
        if (calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {
            //   LogUtil.i(Calendar.getInstance().get(Calendar.YEAR)+"");
            return DATE_FORMAT_TILL_DAY_CURRENT_YEAR.format(date);
        }

        return DATE_FORMAT_TILL_DAY_CH.format(date);
    }

    //毫秒转日期
    public static String millToDate(long time3) {
        Date date2 = new Date();
        date2.setTime(time3);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date2);
    }

    /**
     * 日期逻辑
     *
     * @param dateStr 日期字符串
     * @return
     */
    public static String timeLogic(String dateStr) {
        Calendar calendar = Calendar.getInstance();
        calendar.get(Calendar.DAY_OF_MONTH);
        long now = calendar.getTimeInMillis();
        Date date = strToDate(dateStr, "yyyy-MM-dd HH:mm:ss");
        calendar.setTime(date);
        long past = calendar.getTimeInMillis();

        // 相差的秒数
        long time = (now - past) / 1000;

        StringBuffer sb = new StringBuffer();
        if (time < 60) { // 1小时内
            //  return sb.append(time + "秒前").toString();
            return sb.append("刚刚").toString();
        } else if (time > 60 && time < 3600) {
            return sb.append(time / 60 + "分钟前").toString();
        } else if (time >= 3600 && time < 3600 * 24) {
            return sb.append(time / 3600 + "小时前").toString();
        } else if (time >= 3600 * 24 && time < 3600 * 48) {
            return sb.append("昨天").toString();
        } else if (time >= 3600 * 48 && time < 3600 * 72) {
            return sb.append("前天").toString();
        } else if (time >= 3600 * 72) {
            return dateToString(dateStr, "yyyy-MM-dd HH:mm:ss");
        }
        return dateToString(dateStr, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 比较两个日期的大小，日期格式为yyyy-MM-dd
     *
     * @param str1 the first date
     * @param str2 the second date
     * @return true <br/>false
     */
    public static boolean isDateOneBigger(String str1, String str2) {
        boolean isBigger = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = sdf.parse(str1);
            dt2 = sdf.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dt1.getTime() > dt2.getTime()) {
            isBigger = true;
        } else if (dt1.getTime() < dt2.getTime()) {
            isBigger = false;
        }
        return isBigger;
    }

    /**
     * 计算剩余时间
     *
     * @param orderTime
     * @return
     */
    public static String leftTime(String orderTime) {

        Calendar calendar = Calendar.getInstance();
        calendar.get(Calendar.DAY_OF_MONTH);
        long now = calendar.getTimeInMillis();
        Date date = strToDate(orderTime, "yyyy-MM-dd HH:mm:ss");
        calendar.setTime(date);
        long end = calendar.getTimeInMillis() + 24 * 60 * 60 * 1000;

        // 相差的秒数
        long time = (end - now) / 1000;
        StringBuffer sb = new StringBuffer();
        if (end < now) {
            return "支付超时";
        } else {
            return sb.append(time / 3600 + "小时" + (time % 3600) / 60 + "分钟").toString();

        }


    }


    /**
     * 根据年 月 获取对应的月份 天数
     */
    public static int getDaysByYearMonth(int year, int month) {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 厘米转换成米
     */
    public static String convertMi(String limi) {
        DecimalFormat df2 = new DecimalFormat("###.0");
        return df2.format(Float.valueOf(limi) / 100d);
    }


    // 将字符串转为时间戳
    //string为字符串的日期格式，如："yyyy年MM月dd日HH时mm分ss秒"

    /**
     * 22      * 日期格式字符串转换成时间戳
     * 23      * @param date 字符串日期
     * 24      * @param format 如：yyyy-MM-dd HH:mm:ss
     * 25      * @return
     * 26
     */
    public static Long date2TimeStamp(String date_str, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINESE);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
            return sdf.parse(date_str).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }

    //毫秒转字符串
    public static String timeStamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds)));
    }


    /**
     * 日期转星期
     *
     * @param datetime
     * @return
     */
    public static String dateToWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = {"0", "1", "2", "3", "4", "5", "6"};
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;

        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        LogUtil.i(w + "");
        return weekDays[w];
    }

    /**
     * date转calendar
     */
    public static Calendar dataToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static String dateToString(Date date) {
        // 获得SimpleDateFormat类
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        //打印当前时间
        return sf.format(date);
    }

    /**
     * 获取月初的毫秒时间戳
     */
    public static long getMonthFirstDay(Calendar calendar) {
        //Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.add(Calendar.MONTH + 1, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取月末的毫秒时间戳
     */
    public static long getMonthLastDay(Calendar calendar) {
        //Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));// 设置本月最后一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * start
     * 本周开始时间戳 - 以星期一为本周的第一天
     */
    public static String getWeekStartTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        cal.add(Calendar.DATE, -day_of_week + 1);
        return simpleDateFormat.format(cal.getTime()) + " 00:00:00";
    }

    /**
     * end
     * 本周结束时间戳 - 以星期一为本周的第一天
     */
    public static String getWeekEndTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        cal.add(Calendar.DATE, -day_of_week + 7);
        return simpleDateFormat.format(cal.getTime()) + " 23:59:59";
    }

    /**
     * 根据分钟数获取时间字符串24小时制
     */
    public static String MinuteToTime(int minute) {
        String time = null;
        if (minute % 60 == 0) {
            time = minute / 60 + ":00";
        } else {
            if (minute % 60 < 10) {
                time = minute / 60 + ":0" + minute % 60;
            } else {
                time = minute / 60 + ":" + minute % 60;
            }
        }
        return time;
    }


    /**
     * 时间戳
     *
     * @param segmentation 分割  如：手环折线图 5分钟一累加
     * @param date
     * @return
     */
    public static List<Date> getSegmentationTime(int segmentation, Date date) {
        Date start = dayStartDate(date);//转换为天的起始date
        Date nextDayDate = nextDay(start);//下一天的date

        List<Date> result = new ArrayList<Date>();
        while (start.compareTo(nextDayDate) < 0) {
            result.add(start);
            //日期加15分钟
            start = addFiveMin(start, segmentation);
        }

        return result;
    }

    private static Date addFiveMin(Date start, int offset) {
        Calendar c = Calendar.getInstance();
        c.setTime(start);
        c.add(Calendar.MINUTE, offset);
        return c.getTime();
    }

    private static Date nextDay(Date start) {
        Calendar c = Calendar.getInstance();
        c.setTime(start);
        c.add(Calendar.DATE, 1);
        return c.getTime();
    }

    private static Date dayStartDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 当天的起始时间
     *
     * @return
     */
    public static Date getStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime();
    }

    /**
     * 当天的结束时间
     *
     * @return
     */
    public static Date getEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 59);
        return todayEnd.getTime();
    }

    /**
     * 时间戳  如：手环折线图
     * 上个月所有的日期
     *
     * @return 61天日期List 天  算本天
     */
    public static ArrayList<String> getLastMonthDayData() {
        long temp = getPeriodTopDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), 8);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(temp));
        ArrayList<String> results = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, 1); //向后推移一天
            Date date = calendar.getTime();
            results.add(calendar.getTime() + "");
        }
//        long temp = getPeriodTopDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), 8);
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date(temp));
//        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);  //获得当前日期所在月份有多少天（或者说day的最大值)，用于后面的计算
//        calendar.set(Calendar.DAY_OF_MONTH, 1); //由于是获取当月日期信息，所以直接操作当月Calendar即可。将日期调为当月第一天
//        ArrayList<String> results = new ArrayList<>();
//        for (int i = 0; i < 60; i++) {
//            Date date = calendar.getTime();
//            results.add(format.format(date).toString());
//            calendar.add(Calendar.DAY_OF_MONTH, 1); //向后推移一天
//        }
        return results;
    }

    /**
     * 时间戳  如：手环折线图
     *
     * @param format SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     * @return 本月日期List 天
     */
    public static ArrayList<String> getDayData(SimpleDateFormat format) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int maxDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);  //获得当前日期所在月份有多少天（或者说day的最大值)，用于后面的计算
        c.set(Calendar.DAY_OF_MONTH, 1); //由于是获取当月日期信息，所以直接操作当月Calendar即可。将日期调为当月第一天
        ArrayList<String> results = new ArrayList<>();
        for (int i = 0; i < maxDay; i++) {
            Date date = c.getTime();
            results.add(format.format(date).toString());
            c.add(Calendar.DAY_OF_MONTH, 1); //向后推移一天
        }
        return results;
    }

    /**
     * 本周起始截止时间  如：手环折线图
     * <p>
     * format      SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     *
     * @param currentTime System.currentTimeMillis();//当前时间戳
     * @return 本月日期List 周
     */
    public static String getWeekData(long currentTime) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        String results = System.currentTimeMillis() + "";
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        //当前星期几
        int mWay = c.get(Calendar.DAY_OF_WEEK);

        //一天的时间戳天数___减去礼拜几,,获取礼拜一是几号
        long weekStart = currentTime - ((1000 * 60 * 60 * 24) * (mWay - 1));//礼拜天
        long weekStop = currentTime + ((1000 * 60 * 60 * 24) * (7 - mWay));//礼拜六
//        String startTime = format.format(weekStart).toString();
//        String stopTime = format.format(weekStop).toString();
        results = weekStart + "&" + weekStop;
        return results;
    }

    /**
     * 多周起始截止时间
     *
     * @param count 多少周
     *              format      SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     * @return
     */
    public static ArrayList<String> getWeekListData(int count) {
        ArrayList<String> results = new ArrayList<>();
        Calendar c = Calendar.getInstance(); // 当时的日期和时间
        int day = c.get(Calendar.DAY_OF_MONTH); // 需要更改的天数
        for (int i = 0; i < count; i++) {
            if (i != 0) {
                day = c.get(Calendar.DAY_OF_MONTH) - 7;
            }
            c.set(Calendar.DAY_OF_MONTH, day);
            long currentTime = c.getTimeInMillis();
            String weekDataStr = getWeekData(currentTime);
            results.add(weekDataStr);
        }
        Collections.reverse(results);
        return results;
    }

    /**
     * 获取阶段日期 整点的小时、分钟、秒  格式如：yyyy-MM-dd + 00:00:00:00
     *
     * @param dateType 使用方法 char datetype = '7';
     * @author Yangtse
     */
    public static long getPeriodTopDate(SimpleDateFormat format, int dateType) {
        Calendar c = Calendar.getInstance(); // 当时的日期和时间
        int hour; // 需要更改的小时
        int day; // 需要更改的天数
        switch (dateType) {
            case 0: // 1小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 1;
                c.set(Calendar.HOUR_OF_DAY, hour);
                break;
            case 1: // 2小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 2;
                c.set(Calendar.HOUR_OF_DAY, hour);
                break;
            case 2: // 3小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 3;
                c.set(Calendar.HOUR_OF_DAY, hour);
                break;
            case 3: // 6小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 6;
                c.set(Calendar.HOUR_OF_DAY, hour);
                break;
            case 4: // 12小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 12;
                c.set(Calendar.HOUR_OF_DAY, hour);
                break;
            case 5: // 一天前
                day = c.get(Calendar.DAY_OF_MONTH) - 1;
                c.set(Calendar.DAY_OF_MONTH, day);
                break;
            case 6: // 一星期前
                day = c.get(Calendar.DAY_OF_MONTH) - 7;
                c.set(Calendar.DAY_OF_MONTH, day);
                break;
            case 7: // 一个月前 System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000
                day = c.get(Calendar.DAY_OF_MONTH) - 30;
                c.set(Calendar.DAY_OF_MONTH, day);
                break;
            case 8://60天前 手环心率用到   加上本天
                day = c.get(Calendar.DAY_OF_MONTH) - 60;
                c.set(Calendar.DAY_OF_MONTH, day);
                break;
        }
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        StringBuilder strForwardDate = new StringBuilder().append(mYear).append(
                (mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append(
                (mDay < 10) ? "0" + mDay : mDay);
        LogUtil.i("strDate------->" + strForwardDate + "-" + format.format(c.getTime()) + "-" + c.getTimeInMillis());
        return c.getTimeInMillis();
    }

    /**
     * 是否过了这个时间
     *
     * @param year
     * @param month
     * @param date
     * @param hour
     * @param minute
     * @return
     */
    public static boolean isAfterToday(int year, int month, int date, int hour, int minute) {
        Date date1 = new Date(year, month, date, hour, minute);
        Calendar c = Calendar.getInstance();
        Date now = new Date(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR), c.get(Calendar.MINUTE));

        if (date1.after(now)) {
            return true;//超过了今天
        } else {
            return false;//没有超过今天
        }
    }

    /**
     * 计算两个日期相差多少天
     *
     * @param begin_date
     * @param end_date
     * @return 天数
     */
    public static String getInterval(Date begin_date, Date end_date) {
        long day = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            if (begin_date != null) {
                String begin = sdf.format(begin_date);
                begin_date = sdf.parse(begin);
            }
            if (end_date != null) {
                String end = sdf.format(end_date);
                end_date = sdf.parse(end);
            }
            day = (end_date.getTime() - begin_date.getTime()) / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("d").format(day);
    }

//    /**
//     * 多月起始截止时间
//     *
//     * @param count
//     * @return
//     */
//    public static ArrayList<String> getMonthListData(int count) {
//        ArrayList<String> results = new ArrayList<>();
//        Calendar c = Calendar.getInstance(); // 当时的日期和时间
//        int month; // 需要更改的月数
//        for (int i = 0; i < count; i++) {
//            month = c.get(Calendar.MONTH) - 1;
//            c.set(Calendar.MONTH, month);
//            String weekDataStr = getTimesMonthmorning(c) + "-" + getTimesMonthnight(c);//本月第一天0点时间 + 本月最后一天24点时间
//            results.add(weekDataStr);
//        }
//
//
//
////        Calendar c = Calendar.getInstance();
////        int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
//        return results;
//    }
//
//    /**
//     * 获得本月第一天0点时间 可改变Calendar cal = Calendar.getInstance();
//     *
//     * @param cal
//     * @return
//     */
//    public static long getTimesMonthmorning(Calendar cal) {
//        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
//        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
////        return (int) (cal.getTimeInMillis() / 1000);
//        return cal.getTimeInMillis();
//    }
//
//    /**
//     * 获得本月最后一天24点时间 可改变Calendar cal = Calendar.getInstance();
//     *
//     * @return
//     */
//    public static long getTimesMonthnight(Calendar cal) {
//        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
//        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
//        cal.set(Calendar.HOUR_OF_DAY, 24);
////        return (int) (cal.getTimeInMillis() / 1000);
//        return cal.getTimeInMillis();
//    }
//
//    /**
//     * 获取阶段日期
//     *
//     * @param dateType 使用方法 char datetype = '7';
//     * @author Yangtse
//     */
//    public static long getPeriodDate(SimpleDateFormat format, int dateType) {
//        Calendar c = Calendar.getInstance(); // 当时的日期和时间
//        int hour; // 需要更改的小时
//        int day; // 需要更改的天数
//        switch (dateType) {
//            case 0: // 1小时前
//                hour = c.get(Calendar.HOUR_OF_DAY) - 1;
//                c.set(Calendar.HOUR_OF_DAY, hour);
//                break;
//            case 1: // 2小时前
//                hour = c.get(Calendar.HOUR_OF_DAY) - 2;
//                c.set(Calendar.HOUR_OF_DAY, hour);
//                break;
//            case 2: // 3小时前
//                hour = c.get(Calendar.HOUR_OF_DAY) - 3;
//                c.set(Calendar.HOUR_OF_DAY, hour);
//                break;
//            case 3: // 6小时前
//                hour = c.get(Calendar.HOUR_OF_DAY) - 6;
//                c.set(Calendar.HOUR_OF_DAY, hour);
//                break;
//            case 4: // 12小时前
//                hour = c.get(Calendar.HOUR_OF_DAY) - 12;
//                c.set(Calendar.HOUR_OF_DAY, hour);
//                break;
//            case 5: // 一天前
//                day = c.get(Calendar.DAY_OF_MONTH) - 1;
//                c.set(Calendar.DAY_OF_MONTH, day);
//                break;
//            case 6: // 一星期前
//                day = c.get(Calendar.DAY_OF_MONTH) - 7;
//                c.set(Calendar.DAY_OF_MONTH, day);
//                break;
//            case 7: // 一个月前
//                day = c.get(Calendar.DAY_OF_MONTH) - 30;
//                c.set(Calendar.DAY_OF_MONTH, day);
//                break;
//        }
//        int mYear = c.get(Calendar.YEAR);
//        int mMonth = c.get(Calendar.MONTH);
//        int mDay = c.get(Calendar.DAY_OF_MONTH);
//        StringBuilder strForwardDate = new StringBuilder().append(mYear).append(
//                (mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append(
//                (mDay < 10) ? "0" + mDay : mDay);
//        LogUtil.i("strDate------->" + strForwardDate + "-" + format.format(c.getTime()) + "-" + c.getTimeInMillis());
//        return c.getTimeInMillis();
//    }
//
//
//  //  获得本周一0点时间
//    public static int getTimesWeekmorning() {
//        Calendar cal = Calendar.getInstance();
//        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
//        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
//        return (int) (cal.getTimeInMillis() / 1000);
//    }
//
//    //获得本周日24点时间
//    public static int getTimesWeeknight() {
//        Calendar cal = Calendar.getInstance();
//        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
//        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
//        return (int) ((cal.getTime().getTime() + (7 * 24 * 60 * 60 * 1000)) / 1000);
//    }
//
//
//    //获得当天0点时间
//    public static int getTimesmorning() {
//        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.HOUR_OF_DAY, 0);
//        cal.set(Calendar.SECOND, 0);
//        cal.set(Calendar.MINUTE, 0);
//        cal.set(Calendar.MILLISECOND, 0);
//        return (int) (cal.getTimeInMillis() / 1000);
//    }
//
//    //获得当天24点时间
//    public static int getTimesnight() {
//        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.HOUR_OF_DAY, 24);
//        cal.set(Calendar.SECOND, 0);
//        cal.set(Calendar.MINUTE, 0);
//        cal.set(Calendar.MILLISECOND, 0);
//        return (int) (cal.getTimeInMillis() / 1000);
//    }
//
//
//    //-----------------------------------
//
//    private static char[] daysInGregorianMonth = {31, 28, 31, 30, 31, 30, 31,
//            31, 30, 31, 30, 31};
//
//    // 判断是否是闰年
//    public static boolean isGregorianLeapYear(int year) {
//        boolean isLeap = false;
//        if (year % 4 == 0)
//            isLeap = true;
//        if (year % 100 == 0)
//            isLeap = false;
//        if (year % 400 == 0)
//            isLeap = true;
//        return isLeap;
//    }
//
//    // 返回一个月有几天
//    public static int daysInGregorianMonth(int y, int m) {
//        int d = daysInGregorianMonth[m - 1];
//        if (m == 2 && isGregorianLeapYear(y))
//            d++; // 公历闰年二月多一天
//        return d;
//    }
//
//    // 计算当前天在本年中是第几天
//    public static int dayOfYear(int y, int m, int d) {
//        int c = 0;
//        for (int i = 1; i < m; i++) {
//            c = c + daysInGregorianMonth(y, i);
//        }
//        c = c + d;
//        return c;
//    }
//
//    // 当前天是本周的第几天 ， 从星期天开始算
//    public static int dayOfWeek(int y, int m, int d) {
//        int w = 1; // 公历一年一月一日是星期一，所以起始值为星期日
//        y = (y - 1) % 400 + 1; // 公历星期值分部 400 年循环一次
//        int ly = (y - 1) / 4; // 闰年次数
//        ly = ly - (y - 1) / 100;
//        ly = ly + (y - 1) / 400;
//        int ry = y - 1 - ly; // 常年次数
//        w = w + ry; // 常年星期值增一
//        w = w + 2 * ly; // 闰年星期值增二
//        w = w + dayOfYear(y, m, d);
//        w = (w - 1) % 7 + 1;
//        return w;
//    }
}
