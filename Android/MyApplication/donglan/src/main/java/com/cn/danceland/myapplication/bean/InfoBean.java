/**
 * Copyright 2017 bejson.com
 */
package com.cn.danceland.myapplication.bean;

/**
 * Auto-generated: 2017-10-27 10:48:15
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class InfoBean {

    private boolean success;
    private String errorMsg;
    private Data data;

    @Override
    public String toString() {
        return "InfoBean{" +
                "success=" + success +
                ", errorMsg='" + errorMsg + '\'' +
                ", data=" + data +
                '}';
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

}