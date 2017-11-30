package com.cn.danceland.myapplication.bean;

import java.util.List;

/**
 * Created by shy on 2017/11/23 13:51
 * Email:644563767@qq.com
 * 一条动态请求bean
 */
public class RequstOneDynInfoBean {

    private boolean success;
    private String errorMsg;
    private Data data;

    @Override
    public String toString() {
        return "RequstOneDynInfoBean{" +
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

        @Override
        public String toString() {
            return "Data{" +
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


        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public List<String> getImgList() {
            return imgList;
        }

        public void setImgList(List<String> imgList) {
            this.imgList = imgList;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getFollowerNumber() {
            return followerNumber;
        }

        public void setFollowerNumber(int followerNumber) {
            this.followerNumber = followerNumber;
        }

        public boolean isFollower() {
            return follower;
        }

        public boolean isPraise() {
            return praise;
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

        public void setVedioUrl(String vedioUrl) {
            this.vedioUrl = vedioUrl;
        }

        public String getVedioUrl() {
            return vedioUrl;
        }

        public void setVedioImg(String vedioImg) {
            this.vedioImg = vedioImg;
        }

        public String getVedioImg() {
            return vedioImg;
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

        public void setPraise(boolean praise) {
            this.praise = praise;
        }

        public boolean getPraise() {
            return praise;
        }

        public void setFollower(boolean follower) {
            this.follower = follower;
        }

        public boolean getFollower() {
            return follower;
        }

    }
}