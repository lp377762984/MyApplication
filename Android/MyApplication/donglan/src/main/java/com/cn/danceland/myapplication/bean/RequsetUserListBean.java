package com.cn.danceland.myapplication.bean;

import java.util.List;

/**
 * Created by shy on 2017/11/21 14:33
 * Email:644563767@qq.com
 */


public class RequsetUserListBean {

    private boolean success;
    private String errorMsg;
    private Data data;

    @Override
    public String toString() {
        return "RequsetUserListBean{" +
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

    public class Data {

        private int total;
        private List<Items> items;

        @Override
        public String toString() {
            return "Data{" +
                    "total=" + total +
                    ", items=" + items +
                    '}';
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getTotal() {
            return total;
        }

        public void setItems(List<Items> items) {
            this.items = items;
        }

        public List<Items> getItems() {
            return items;
        }


        public class Items {

            private String id;
            private String follower;
            private String userId;
            private String success;
            private String errorMsg;
            private String selfUrl;
            private String nickName;
            private int gender;
            private String praiseUserId;

//            private int id;
//            private int follower;
//            private int userId;
//            private String success;
//            private String errorMsg;
//            private String selfUrl;
//            private String nickName;
//            private int gender;


            @Override
            public String toString() {
                return "Items{" +
                        "id='" + id + '\'' +
                        ", follower=" + follower +
                        ", userId='" + userId + '\'' +
                        ", success='" + success + '\'' +
                        ", errorMsg='" + errorMsg + '\'' +
                        ", selfUrl='" + selfUrl + '\'' +
                        ", nickName='" + nickName + '\'' +
                        ", gender=" + gender +
                        ", praiseUserId='" + praiseUserId + '\'' +
                        '}';
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String isFollower() {
                return follower;
            }

            public void setFollower(String follower) {
                this.follower = follower;
            }

            public String getPraiseUserId() {
                return praiseUserId;
            }

            public void setPraiseUserId(String praiseUserId) {
                this.praiseUserId = praiseUserId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getUserId() {
                return userId;
            }

            public void setSuccess(String success) {
                this.success = success;
            }

            public String getSuccess() {
                return success;
            }

            public void setErrorMsg(String errorMsg) {
                this.errorMsg = errorMsg;
            }

            public String getErrorMsg() {
                return errorMsg;
            }

            public void setSelfUrl(String selfUrl) {
                this.selfUrl = selfUrl;
            }

            public String getSelfUrl() {
                return selfUrl;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getNickName() {
                return nickName;
            }

            public void setGender(int gender) {
                this.gender = gender;
            }

            public int getGender() {
                return gender;
            }

        }

    }
}