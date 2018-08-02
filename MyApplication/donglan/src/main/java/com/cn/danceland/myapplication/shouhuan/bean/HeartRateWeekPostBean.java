package com.cn.danceland.myapplication.shouhuan.bean;

import java.io.Serializable;

/**
 * 请求后台心率对象  提交  周数据
 * 根据条件查询心率每日平均数
 * Created by ${yxx} on 2018/7/30.
 */
public class HeartRateWeekPostBean implements Serializable {
    private String timestamp_gt;
    private String timestamp_lt;

    public String getTimestamp_gt() {
        return timestamp_gt;
    }

    public void setTimestamp_gt(String timestamp_gt) {
        this.timestamp_gt = timestamp_gt;
    }

    public String getTimestamp_lt() {
        return timestamp_lt;
    }

    public void setTimestamp_lt(String timestamp_lt) {
        this.timestamp_lt = timestamp_lt;
    }

    @Override
    public String toString() {
        return "HeartRateWeekPostBean{" +
                ", timestamp_gt=" + timestamp_gt +
                ", timestamp_lt=" + timestamp_lt +
                '}';
    }
}
