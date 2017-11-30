package com.cn.danceland.myapplication.bean;

import java.util.List;

/**
 * Created by shy on 2017/11/16 16:13
 * Email:644563767@qq.com
 */

public class RequestSellCardsInfoBean {

    private boolean success;
    private String errorMsg;
    private List<Data> data;

    @Override
    public String toString() {
        return "RequestSellCardsInfoBean{" +
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

    public void setData(List<Data> data) {
        this.data = data;
    }

    public List<Data> getData() {
        return data;
    }

    public class Data {

        private int id;
        private int branchId;
        private String name;
        private int chargeMode;
        private int totalCount;
        private int price;
        private int indate;
        private int timeUnit;
        private int onSale;
        private int labelId;
        private int length;

        @Override
        public String toString() {
            return "Data{" +
                    "id=" + id +
                    ", branchId=" + branchId +
                    ", name='" + name + '\'' +
                    ", chargeMode=" + chargeMode +
                    ", totalCount=" + totalCount +
                    ", price=" + price +
                    ", indate=" + indate +
                    ", timeUnit=" + timeUnit +
                    ", onSale=" + onSale +
                    ", labelId=" + labelId +
                    ", length=" + length +
                    '}';
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setBranchId(int branchId) {
            this.branchId = branchId;
        }

        public int getBranchId() {
            return branchId;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setChargeMode(int chargeMode) {
            this.chargeMode = chargeMode;
        }

        public int getChargeMode() {
            return chargeMode;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getPrice() {
            return price;
        }

        public void setIndate(int indate) {
            this.indate = indate;
        }

        public int getIndate() {
            return indate;
        }

        public void setTimeUnit(int timeUnit) {
            this.timeUnit = timeUnit;
        }

        public int getTimeUnit() {
            return timeUnit;
        }

        public void setOnSale(int onSale) {
            this.onSale = onSale;
        }

        public int getOnSale() {
            return onSale;
        }

        public void setLabelId(int labelId) {
            this.labelId = labelId;
        }

        public int getLabelId() {
            return labelId;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public int getLength() {
            return length;
        }

    }

}