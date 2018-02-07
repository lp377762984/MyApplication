package com.cn.danceland.myapplication.utils;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by shy on 2017/11/27 14:35
 * Email:644563767@qq.com
 * 处理时间和日期的工具类
 */


public class TimeUtils {
    private static SimpleDateFormat DATE_FORMAT_TILL_SECOND = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    private static SimpleDateFormat DATE_FORMAT_TILL_DAY_CURRENT_YEAR = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat DATE_FORMAT_TILL_DAY_CH = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    /**
     * 日期字符串转换为Date
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
    public static String millToDate(long time3){
        Date date2 = new Date();
        date2.setTime(time3);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date2);
    }
    /**
     * 日期逻辑
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
            return sb.append(time / 60+"分钟前").toString();
        } else if (time >= 3600 && time < 3600 * 24) {
            return sb.append(time / 3600 +"小时前").toString();
        }else if (time >= 3600 * 24 && time < 3600 * 48) {
            return sb.append("昨天").toString();
        }else if (time >= 3600 * 48 && time < 3600 * 72) {
            return sb.append("前天").toString();
        }else if (time >= 3600 * 72) {
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
     * @param orderTime
     * @return
     */
    public static String leftTime(String orderTime){

        Calendar calendar = Calendar.getInstance();
        calendar.get(Calendar.DAY_OF_MONTH);
        long now = calendar.getTimeInMillis();
        Date date = strToDate(orderTime, "yyyy-MM-dd HH:mm:ss");
        calendar.setTime(date);
        long end = calendar.getTimeInMillis()+24*60*60*1000;

        // 相差的秒数
        long time = (end -now ) / 1000;
        StringBuffer sb = new StringBuffer();
        if (end<now){
            return "支付超时";
        }else {
            return sb.append(time/3600+"小时"+ (time%3600)/ 60+"分钟").toString();

        }


    }

    public static String isleapyear(int year) {
        String is;
        is=((year%4==0&&year%100!=0)||year%400==0)?"是闰年":"不是闰年";
        return is;
    }

    /**
     * 根据年 月 获取对应的月份 天数
     * */
    public static int getDaysByYearMonth(int year, int month) {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

}
