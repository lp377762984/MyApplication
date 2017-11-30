package com.cn.danceland.myapplication.evntbus;

/**
 * Created by shy on 2017/11/10 10:05
 * Email:644563767@qq.com
 */


public class StringEvent {
    private String mMsg;
    private int mEventCode;

    public StringEvent(String msg, int eventCode) {
        this.mMsg = msg;
        this.mEventCode = eventCode;
    }

    public String getMsg() {
        return mMsg;
    }

    public int getEventCode() {
        return mEventCode;
    }
}
