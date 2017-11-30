package com.cn.danceland.myapplication.bean;

import java.util.List;

/**
 * Created by feng on 2017/11/28.
 */

public class StoreBean {
        private Data data;
        private String errorMsg;
        private boolean success;
        public void setData(Data data) {
            this.data = data;
        }
        public Data getData() {
            return data;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }
        public String getErrorMsg() {
            return errorMsg;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
        public boolean getSuccess() {
            return success;
        }


    public class Data {

        private List<Items> items;
        private int total;
        public void setItems(List<Items> items) {
            this.items = items;
        }
        public List<Items> getItems() {
            return items;
        }

        public void setTotal(int total) {
            this.total = total;
        }
        public int getTotal() {
            return total;
        }

    }


    public class Items {
        private String address;
        private String bname;
        private String description;
        private boolean enabled;
        private int id;
        private double lat;
        private double lng;
        private String logo;
        private boolean status;
        private String telphoneNo;
        private int zoneCode;
        public void setAddress(String address) {
            this.address = address;
        }
        public String getAddress() {
            return address;
        }

        public void setBname(String bname) {
            this.bname = bname;
        }
        public String getBname() {
            return bname;
        }

        public void setDescription(String description) {
            this.description = description;
        }
        public String getDescription() {
            return description;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        public boolean getEnabled() {
            return enabled;
        }

        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public void setLat(int lat) {
            this.lat = lat;
        }
        public double getLat() {
            return lat;
        }

        public void setLng(int lng) {
            this.lng = lng;
        }
        public double getLng() {
            return lng;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }
        public String getLogo() {
            return logo;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }
        public boolean getStatus() {
            return status;
        }

        public void setTelphoneNo(String telphoneNo) {
            this.telphoneNo = telphoneNo;
        }
        public String getTelphoneNo() {
            return telphoneNo;
        }

        public void setZoneCode(int zoneCode) {
            this.zoneCode = zoneCode;
        }
        public int getZoneCode() {
            return zoneCode;
        }

    }

}
