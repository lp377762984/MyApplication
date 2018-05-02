package com.cn.danceland.myapplication.bean;

/**
 * Created by shy on 2017/12/26 14:20
 * Email:644563767@qq.com
 */

public class RequestOrderPayInfoBean {

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

        private String alipayOrderInfo;
        private String wxpayOrderInfo;
        private int payWay;//

        public String getAlipayOrderInfo() {
            return alipayOrderInfo;
        }

        public void setAlipayOrderInfo(String alipayOrderInfo) {
            this.alipayOrderInfo = alipayOrderInfo;
        }

        public String getWxpayOrderInfo() {
            return wxpayOrderInfo;
        }

        public void setWxpayOrderInfo(String wxpayOrderInfo) {
            this.wxpayOrderInfo = wxpayOrderInfo;
        }

        public int getPayWay() {
            return payWay;
        }

        public void setPayWay(int payWay) {
            this.payWay = payWay;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "alipayOrderInfo='" + alipayOrderInfo + '\'' +
                    ", wxpayOrderInfo='" + wxpayOrderInfo + '\'' +
                    ", payWay=" + payWay +
                    '}';

        }
    }
}