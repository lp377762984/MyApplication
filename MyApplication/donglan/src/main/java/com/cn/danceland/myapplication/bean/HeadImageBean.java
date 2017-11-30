package com.cn.danceland.myapplication.bean;

/**
 * Created by feng on 2017/11/15.
 */

public class HeadImageBean {

    private boolean success;
    private String errorMsg;
    private Data data;
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
    public class Data {

        private String imgUrl;
        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }
        public String getImgUrl() {
            return imgUrl;
        }

    }

}
