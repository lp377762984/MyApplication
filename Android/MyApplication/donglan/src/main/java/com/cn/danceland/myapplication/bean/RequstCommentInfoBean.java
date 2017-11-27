package com.cn.danceland.myapplication.bean;

import java.util.List;

/**
 * Created by shy on 2017/11/23 13:42
 * Email:644563767@qq.com
 */

public class RequstCommentInfoBean {

    private boolean success;
    private String errorMsg;
    private Data data;

    @Override
    public String toString() {
        return "RequstCommentInfoBean{" +
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

    }

    public static class Items {

        private String id;//评论的id
        private String replyUserId;//发评论人的id
        private String content;//发评论的内容
        private String time;
        private String parentId;//回复评论的id
        private String replyMsgId;//动态的id
        private String success;
        private String errorMsg;
        private String selfUrl;//发评论的头像
        private String nickName;//发评论人的昵称
        private int gender;
        private String replyUser;//要回复的人id
        private String replySelfUrl;//要回复的头像
        private String replyNickName;//要回复人的昵称

        public String getReplyUser() {
            return replyUser;
        }

        public void setReplyUser(String replyUser) {
            this.replyUser = replyUser;
        }

        public String getReplySelfUrl() {
            return replySelfUrl;
        }

        public void setReplySelfUrl(String replySelfUrl) {
            this.replySelfUrl = replySelfUrl;
        }

        public String getReplyNickName() {
            return replyNickName;
        }

        public void setReplyNickName(String replyNickName) {
            this.replyNickName = replyNickName;
        }

        @Override
        public String toString() {
            return "Items{" +
                    "id=" + id +
                    ", replyUserId='" + replyUserId + '\'' +
                    ", content='" + content + '\'' +
                    ", time='" + time + '\'' +
                    ", parentId='" + parentId + '\'' +
                    ", replyMsgId='" + replyMsgId + '\'' +
                    ", success='" + success + '\'' +
                    ", errorMsg='" + errorMsg + '\'' +
                    ", selfUrl='" + selfUrl + '\'' +
                    ", nickName='" + nickName + '\'' +
                    ", gender=" + gender +
                    ", replyUser='" + replyUser + '\'' +
                    ", replySelfUrl='" + replySelfUrl + '\'' +
                    ", replyNickName='" + replyNickName + '\'' +
                    '}';
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setReplyUserId(String replyUserId) {
            this.replyUserId = replyUserId;
        }

        public String getReplyUserId() {
            return replyUserId;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTime() {
            return time;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public String getParentId() {
            return parentId;
        }

        public void setReplyMsgId(String replyMsgId) {
            this.replyMsgId = replyMsgId;
        }

        public String getReplyMsgId() {
            return replyMsgId;
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