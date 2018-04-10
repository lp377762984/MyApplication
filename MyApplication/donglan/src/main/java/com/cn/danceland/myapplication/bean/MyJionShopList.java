package com.cn.danceland.myapplication.bean;

import java.util.List;

/**
 * Created by shy on 2018/4/9 15:58
 * Email:644563767@qq.com
 */


public class MyJionShopList {

    private boolean success;
    private String errorMsg;
    private String code;
    private List<Data> data;
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

    public void setCode(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }
    public List<Data> getData() {
        return data;
    }

    public class Data {

        private String name;
        private String create_time;
        private String auth;
        private int branch_id;
        private String auths;
        public void setName(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }
        public String getCreate_time() {
            return create_time;
        }

        public void setAuth(String auth) {
            this.auth = auth;
        }
        public String getAuth() {
            return auth;
        }

        public void setBranch_id(int branch_id) {
            this.branch_id = branch_id;
        }
        public int getBranch_id() {
            return branch_id;
        }

        public void setAuths(String auths) {
            this.auths = auths;
        }
        public String getAuths() {
            return auths;
        }
}}