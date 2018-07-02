package com.cn.danceland.myapplication.bean;

import java.io.Serializable;

/**
 * Created by feng on 2018/6/28.
 */

public class ClockBean implements Serializable {

    String time;
    String offOn;//0关1开

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOffOn() {
        return offOn;
    }

    public void setOffOn(String offOn) {
        this.offOn = offOn;
    }
}
