/**
 * Copyright 2017 bejson.com
 */
package com.cn.danceland.myapplication.bean;

import java.util.List;

/**
 * Auto-generated: 2017-11-14 13:33:29
 * <p>
 * 动态列表请求bean
 */
public class RequsetDynInfoBean {

    private boolean success;
    private String errorMsg;
    private Data data;


    @Override
    public String toString() {
        return "RequsetDynInfoBean{" +
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
            private String title;
            private String content;
            private int privacyType;
            private int msgType;
            private String author;
            private int publishType;
            private int shareId;
            private boolean enabled;
            private String publishTime;
            private String publishPlace;
            private List<String> imgList;
            private String success;
            private String errorMsg;
            private String userName;
            private String userId;
            private String selfUrl;
            private String nickName;
            private int priaseNumber;
            private int replyNumber;
            private int followerNumber;
            private boolean follower;
            private boolean praise;

            private String vedioImg;
            private String vedioUrl;

            public String getVedioImg() {
                return vedioImg;
            }

            public void setVedioImg(String vedioImg) {
                this.vedioImg = vedioImg;
            }

            public String getVedioUrl() {
                return vedioUrl;
            }

            public void setVedioUrl(String vedioUrl) {
                this.vedioUrl = vedioUrl;
            }

            public boolean isEnabled() {
                return enabled;
            }

            public boolean isFollower() {
                return follower;
            }

            public void setFollower(boolean follower) {
                this.follower = follower;
            }

            public boolean isPraise() {
                return praise;
            }

            public void setPraise(boolean praise) {
                this.praise = praise;
            }

            @Override
            public String toString() {
                return "Items{" +
                        "id='" + id + '\'' +
                        ", title='" + title + '\'' +
                        ", content='" + content + '\'' +
                        ", privacyType=" + privacyType +
                        ", msgType=" + msgType +
                        ", author='" + author + '\'' +
                        ", publishType=" + publishType +
                        ", shareId=" + shareId +
                        ", enabled=" + enabled +
                        ", publishTime='" + publishTime + '\'' +
                        ", publishPlace='" + publishPlace + '\'' +
                        ", imgList=" + imgList +
                        ", success='" + success + '\'' +
                        ", errorMsg='" + errorMsg + '\'' +
                        ", userName='" + userName + '\'' +
                        ", userId='" + userId + '\'' +
                        ", selfUrl='" + selfUrl + '\'' +
                        ", nickName='" + nickName + '\'' +
                        ", priaseNumber=" + priaseNumber +
                        ", replyNumber=" + replyNumber +
                        ", followerNumber=" + followerNumber +
                        ", follower=" + follower +
                        ", praise=" + praise +
                        ", vedioImg='" + vedioImg + '\'' +
                        ", vedioUrl='" + vedioUrl + '\'' +
                        '}';
            }


            public void setId(String id) {
                this.id = id;
            }

            public String getId() {
                return id;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTitle() {
                return title;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getContent() {
                return content;
            }

            public void setPrivacyType(int privacyType) {
                this.privacyType = privacyType;
            }

            public int getPrivacyType() {
                return privacyType;
            }

            public void setMsgType(int msgType) {
                this.msgType = msgType;
            }

            public int getMsgType() {
                return msgType;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public String getAuthor() {
                return author;
            }

            public void setPublishType(int publishType) {
                this.publishType = publishType;
            }

            public int getPublishType() {
                return publishType;
            }

            public void setShareId(int shareId) {
                this.shareId = shareId;
            }

            public int getShareId() {
                return shareId;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public boolean getEnabled() {
                return enabled;
            }

            public void setPublishTime(String publishTime) {
                this.publishTime = publishTime;
            }

            public String getPublishTime() {
                return publishTime;
            }

            public void setPublishPlace(String publishPlace) {
                this.publishPlace = publishPlace;
            }

            public String getPublishPlace() {
                return publishPlace;
            }

            public void setImgList(List<String> imgList) {
                this.imgList = imgList;
            }

            public List<String> getImgList() {
                return imgList;
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

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getUserId() {
                return userId;
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

            public void setPriaseNumber(int priaseNumber) {
                this.priaseNumber = priaseNumber;
            }

            public int getPriaseNumber() {
                return priaseNumber;
            }

            public void setReplyNumber(int replyNumber) {
                this.replyNumber = replyNumber;
            }

            public int getReplyNumber() {
                return replyNumber;
            }

            public void setFollowerNumber(int followerNumber) {
                this.followerNumber = followerNumber;
            }

            public int getFollowerNumber() {
                return followerNumber;
            }

        }


    }


}