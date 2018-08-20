package com.cn.danceland.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by feng on 2018/6/28.
 */
public class ClockBean implements Serializable {
    private String time;
    private int offOn;//0关1开
    private int hour;//小时
    private int minute;//分钟
    private int id;//时钟ID 最多8个
    private List<Integer> weekday;//周日-周六 0 1 2 3 4 5 6

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getOffOn() {
        return offOn;
    }

    public void setOffOn(int offOn) {
        this.offOn = offOn;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Integer> getWeekday() {
        return weekday;
    }

    public void setWeekday(List<Integer> weekday) {
        this.weekday = weekday;
    }

    @Override
    public String toString() {
        return "ClockBean{" +
                "time=" + time +
                ", offOn=" + offOn  +
                ", hour=" + hour +
                ", minute=" + minute  +
                ", id=" + id  +
                '}';
    }
}
