package com.cn.danceland.myapplication.shouhuan.bean;

/**
 * 手环睡眠对象
 * Created by yxx on 22018/8/3.
 */
public class SleepBean {
    private int state;//状态 -1清醒  1浅水  2深睡
    private int time;//时间 图表item的宽度
    private long startTime;//开始时间
    private long endTime;//结束时间

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "SleepBean{" +
                "state=" + state +
                ", time=" + time +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
