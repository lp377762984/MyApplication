package com.cn.danceland.myapplication.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 从手环拉取的睡眠对象
 * Created by yxx on 2018/8/3.
 */
@Entity
public class WearFitSleepBean {
    //    private int year;
//    private int month;
//    private int day;
//    private int hour;
//    private int minute;
//    private int minutes;
    @Id(autoincrement = true)
    private Long id;
    private int state; // 11位state  1 睡眠(包含深睡和浅睡) 2 深睡
    private long timestamp;//时间戳
    private int group_time;//12位 * 256+13位 睡了多久

    @Generated(hash = 1114343845)
    public WearFitSleepBean() {
    }

    @Generated(hash = 1437105322)
    public WearFitSleepBean(Long id, int state, long timestamp, int group_time) {
        this.id = id;
        this.state = state;
        this.timestamp = timestamp;
        this.group_time = group_time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getGroup_time() {
        return group_time;
    }

    public void setGroup_time(int group_time) {
        this.group_time = group_time;
    }

    @Override
    public String toString() {
        return "WearFitSleepBean{" +
//                "year=" + year +
//                ", month=" + month +
//                "day=" + day +
//                ", hour=" + hour +
//                "minute=" + minute +
//                ", minutes=" + minutes +
                "state=" + state +
                ", timestamp=" + timestamp +
                ", group_time=" + group_time +
                '}';
    }
}
