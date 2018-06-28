package com.cn.danceland.myapplication.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.TimeUtils;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;

import java.util.ArrayList;

/**
 * Created by feng on 2018/4/20.
 */

public class CustomDatePicker extends AlertDialog {
    View inflate1, inflate;
    LoopView loopview, lp_year, lp_month, lp_date, lp_hour, lp_minute;
    AlertDialog.Builder alertdialog;
    String title;
    private OnClickEnter onClickEnter;
    int maxMonth, maxDate;
    private final TextView tv_hour;
    private final TextView tv_minute;
    private Time time;
    private final TextView tv_year;
    private final TextView tv_mounth;
    private final TextView tv_date;

    public CustomDatePicker(Context context, String title) {
        super(context);
        this.title = title;
        inflate1 = LayoutInflater.from(context).inflate(R.layout.datepicker, null);
        lp_year = inflate1.findViewById(R.id.lp_year);
        tv_year = inflate1.findViewById(R.id.tv_year);
        lp_month = inflate1.findViewById(R.id.lp_month);
        tv_mounth = inflate1.findViewById(R.id.tv_mounth);
        lp_date = inflate1.findViewById(R.id.lp_date);
        tv_date = inflate1.findViewById(R.id.tv_date);
        lp_hour = inflate1.findViewById(R.id.lp_hour);
        lp_minute = inflate1.findViewById(R.id.lp_minute);
        tv_hour = inflate1.findViewById(R.id.tv_hour);
        tv_minute = inflate1.findViewById(R.id.tv_minute);
        alertdialog = new AlertDialog.Builder(context);
        alertdialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onClickEnter.onClick();
            }
        });
    }

    public interface OnClickEnter {
        public void onClick();
    }

    public void setDialogOnClickListener(OnClickEnter onClickEnter) {

        this.onClickEnter = onClickEnter;
    }

    public void setGoneHourAndMinute() {
        lp_hour.setVisibility(View.GONE);
        lp_minute.setVisibility(View.GONE);
        tv_hour.setVisibility(View.GONE);
        tv_minute.setVisibility(View.GONE);
    }

    public void setGoneYearAndMounth() {
        lp_year.setVisibility(View.GONE);
        lp_month.setVisibility(View.GONE);
        lp_date.setVisibility(View.GONE);

        tv_year.setVisibility(View.GONE);
        tv_mounth.setVisibility(View.GONE);
        tv_date.setVisibility(View.GONE);
    }

    public void setMax(int month, int date) {
        maxMonth = month;
        maxDate = date;
    }


    String syear, smonth, sdate, shour, sminute, timeString, dateString;
    int daysByYearMonth;

    private void showDate() {
        time = new Time();
        time.setToNow();
        final int year = time.year;
        ViewGroup parent = (ViewGroup) inflate1.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }

        final ArrayList<String> yearList = new ArrayList<String>();
        final ArrayList<String> monthList = new ArrayList<String>();
        final ArrayList<String> dateList = new ArrayList<String>();
        int n = 1900;
        int len = year - n;
        for (int i = 0; i <= len; i++) {
            yearList.add((n + i) + "");
        }
        if (maxMonth == 0) {
            for (int j = 0; j < 12; j++) {
                monthList.add((1 + j) + "");
            }
        } else {
            for (int j = 0; j < (time.month + 1); j++) {
                monthList.add((1 + j) + "");
            }
        }

        lp_year.setNotLoop();
        lp_date.setNotLoop();
        lp_month.setNotLoop();
        lp_year.setItems(yearList);
        lp_month.setItems(monthList);

        syear = year + "";
        smonth = (time.month + 1) + "";
        sdate = time.monthDay + "";

        for (int i = 0; i < yearList.size(); i++) {
            if (syear.equals(yearList.get(i))) {
                lp_year.setInitPosition(i);
            }
        }

        for (int i = 0; i < monthList.size(); i++) {
            if (smonth.equals(monthList.get(i))) {
                lp_month.setInitPosition(i);
            }
        }


        if (maxDate == 0) {
            daysByYearMonth = TimeUtils.getDaysByYearMonth(Integer.valueOf(syear), Integer.valueOf(smonth));
            dateList.clear();
            for (int z = 1; z <= daysByYearMonth; z++) {
                dateList.add(z + "");
            }
        } else {
            for (int z = 1; z <= Integer.valueOf(sdate); z++) {
                dateList.add(z + "");
            }
        }

        lp_date.setItems(dateList);

        for (int i = 0; i < dateList.size(); i++) {
            if (sdate.equals(dateList.get(i))) {
                lp_date.setInitPosition(i);
            }
        }


        //设置字体大小
        lp_year.setTextSize(16);
        lp_month.setTextSize(16);
        lp_date.setTextSize(16);

        lp_year.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                monthList.clear();
                if (maxMonth == 0) {
                    for (int j = 0; j < 12; j++) {
                        monthList.add((1 + j) + "");
                    }
                } else {
                    if (year != Integer.valueOf(yearList.get(index))) {
                        for (int j = 0; j < 12; j++) {
                            monthList.add((1 + j) + "");
                        }
                    } else {
                        for (int j = 0; j < maxMonth; j++) {
                            monthList.add((1 + j) + "");
                        }
                    }
                }

                lp_month.setItems(monthList);
                syear = yearList.get(index);
                dateList.clear();
                if (maxDate == 0) {
                    daysByYearMonth = TimeUtils.getDaysByYearMonth(Integer.valueOf(syear), Integer.valueOf(smonth));
                    for (int z = 1; z <= daysByYearMonth; z++) {
                        dateList.add(z + "");
                    }
                } else {
                    if ((time.month + 1) == Integer.valueOf(smonth) && time.year == Integer.valueOf(syear)) {
                        for (int z = 1; z <= maxDate; z++) {
                            dateList.add(z + "");
                        }
                    } else {
                        daysByYearMonth = TimeUtils.getDaysByYearMonth(Integer.valueOf(syear), Integer.valueOf(smonth));
                        for (int z = 1; z <= daysByYearMonth; z++) {
                            dateList.add(z + "");
                        }
                    }
                }
                lp_date.setItems(dateList);
            }
        });

        lp_month.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                smonth = monthList.get(index);
                dateList.clear();
                if (maxDate == 0) {
                    daysByYearMonth = TimeUtils.getDaysByYearMonth(Integer.valueOf(syear), Integer.valueOf(smonth));
                    for (int z = 1; z <= daysByYearMonth; z++) {
                        dateList.add(z + "");
                    }
                } else {
                    if ((time.month + 1) == Integer.valueOf(smonth) && time.year == Integer.valueOf(syear)) {
                        for (int z = 1; z <= maxDate; z++) {
                            dateList.add(z + "");
                        }
                    } else {
                        daysByYearMonth = TimeUtils.getDaysByYearMonth(Integer.valueOf(syear), Integer.valueOf(smonth));
                        for (int z = 1; z <= daysByYearMonth; z++) {
                            dateList.add(z + "");
                        }
                    }
                }

                lp_date.setItems(dateList);
            }
        });

        lp_date.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                sdate = dateList.get(index);
            }
        });

        final ArrayList<String> hourList = new ArrayList<String>();
        final ArrayList<String> minuteList = new ArrayList<String>();

        for (int x = 0; x < 24; x++) {
            if(x<10){
                hourList.add("0"+x);
            }else{
                hourList.add(x + "");
            }
        }
        for (int y = 0; y < 60; y++) {
            minuteList.add(y + "");
        }
        lp_hour.setItems(hourList);
        lp_minute.setItems(minuteList);
        shour = time.hour + "";
        for (int i = 0; i < hourList.size(); i++) {
            if (shour.equals(hourList.get(i))) {
                lp_hour.setInitPosition(i);
            }
        }
        sminute = time.minute + "";
        for (int i = 0; i < minuteList.size(); i++) {
            if (sminute.equals(minuteList.get(i))) {
                lp_minute.setInitPosition(i);
            }
        }

        lp_hour.setTextSize(18);
        lp_minute.setTextSize(18);
        lp_hour.setItemsVisibleCount(7);
        lp_minute.setItemsVisibleCount(7);
        lp_year.setItemsVisibleCount(7);
        lp_month.setItemsVisibleCount(7);
        lp_date.setItemsVisibleCount(7);

        //sminute = minuteList.get(30);

        lp_hour.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                shour = hourList.get(index);
            }
        });
        lp_minute.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                sminute = minuteList.get(index);
            }
        });


        alertdialog.setTitle(title);
        alertdialog.setView(inflate1);
        //alertdialog.setPositiveButton("确定", onClickListener);
        alertdialog.show();

    }


    public String getYear() {

        return syear;
    }

    public String getMonth() {
        return smonth;

    }

    public String getDay() {

        return sdate;
    }

    public String getHour() {
        return shour;
    }

    public String getMinute() {
        return sminute;

    }


    public String getDateString() {

        dateString = syear + "年" + smonth + "月" + sdate + "日";
        return dateString;
    }

    public String getDateStringF() {
        if (Integer.valueOf(smonth) < 10 && Integer.valueOf(sdate) >= 10) {
            dateString = syear + "-0" + smonth + "-" + sdate;
        } else if (Integer.valueOf(sdate) < 10 && Integer.valueOf(smonth) >= 10) {
            dateString = syear + "-" + smonth + "-0" + sdate;
        } else if (Integer.valueOf(sdate) < 10 && Integer.valueOf(smonth) < 10) {
            dateString = syear + "-0" + smonth + "-0" + sdate;
        } else {
            dateString = syear + "-" + smonth + "-" + sdate;
        }
        return dateString;
    }


    public String getTimeString() {
        timeString = syear + "年" + smonth + "月" + sdate + "日" + shour + "时" + sminute + "分";
        return timeString;

    }
    public String getHorizongtal(){
        return timeString = syear + "-" + smonth + "-" + sdate;
    }

    public String getTime(){
        return shour+":"+sminute;
    }

    public void showWindow() {
        showDate();
    }

}
