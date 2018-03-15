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
    //    private List<Items> items;


        private List<Content> content;
        private int number;
        private int size;
        private int totalElements;
        private boolean last;
        private int totalPages;
        private int numberOfElements;

        @Override
        public String toString() {
            return "Data{" +
                    "total=" + total +
                    ", items=" + content +
                    '}';
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getTotal() {
            return total;
        }

        public void setItems(List<Content> items) {
            this.content = items;
        }

        public List<Content> getItems() {
            return content;
        }




        public class Content {


            private boolean animationFlag=false;
            private String id;
            private String title;
            private String content;
            private int privacy_type;
            private int msg_type;
            private String author;
            private int publish_type;
            private int share_id;
            private int enabled;
            private String publish_time;
            private String publish_place;
            private List<String> imgList;
            private String success;
            private String errorMsg;
            private String userName;
            private String userId;
            private String self_url;
            private String nick_name;
            private int priase_number;
            private int reply_number;
            private int followerNumber;
            private boolean is_follower;
            private boolean is_praise;

            private String vedio_img;
            private String vedio_url;


            private String imgs;



            public boolean isAnimationFlag() {
                return animationFlag;
            }

            public void setAnimationFlag(boolean animationFlag) {
                this.animationFlag = animationFlag;
            }

            public String getVedioImg() {
                return vedio_img;
            }

            public void setVedioImg(String vedioImg) {
                this.vedio_img = vedioImg;
            }

            public String getVedioUrl() {
                return vedio_url;
            }

            public void setVedioUrl(String vedioUrl) {
                this.vedio_url = vedioUrl;
            }

            public int isEnabled() {
                return enabled;
            }

            public boolean isFollower() {
                return is_follower;
            }

            public void setFollower(boolean follower) {
                this.is_follower = follower;
            }

            public boolean isPraise() {
                return is_praise;
            }

            public void setPraise(boolean praise) {
                this.is_praise = praise;
            }

            @Override
            public String toString() {
                return "Items{" +
                        "id='" + id + '\'' +
                        ", title='" + title + '\'' +
                        ", content='" + content + '\'' +
                        ", privacyType=" + privacy_type +
                        ", msgType=" + msg_type +
                        ", author='" + author + '\'' +
                        ", publishType=" + publish_type +
                        ", shareId=" + share_id +
                        ", enabled=" + enabled +
                        ", publishTime='" + publish_time + '\'' +
                        ", publishPlace='" + publish_place + '\'' +
                        ", imgList=" + imgList +
                        ", success='" + success + '\'' +
                        ", errorMsg='" + errorMsg + '\'' +
                        ", userName='" + userName + '\'' +
                        ", userId='" + userId + '\'' +
                        ", selfUrl='" + self_url + '\'' +
                        ", nickName='" + nick_name + '\'' +
                        ", priaseNumber=" + priase_number +
                        ", replyNumber=" + reply_number +
                        ", followerNumber=" + followerNumber +
                        ", follower=" + is_follower +
                        ", praise=" + is_praise +
                        ", vedioImg='" + vedio_img + '\'' +
                        ", vedioUrl='" + vedio_url + '\'' +
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
                this.privacy_type = privacyType;
            }

            public int getPrivacyType() {
                return privacy_type;
            }

            public void setMsgType(int msgType) {
                this.msg_type = msgType;
            }

            public int getMsgType() {
                return msg_type;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public String getAuthor() {
                return author;
            }

            public void setPublishType(int publishType) {
                this.publish_type = publishType;
            }

            public int getPublishType() {
                return publish_type;
            }

            public void setShareId(int shareId) {
                this.share_id = shareId;
            }

            public int getShareId() {
                return share_id;
            }

            public void setEnabled(int enabled) {
                this.enabled = enabled;
            }

            public int getEnabled() {
                return enabled;
            }

            public void setPublishTime(String publishTime) {
                this.publish_time = publishTime;
            }

            public String getPublishTime() {
                return publish_time;
            }

            public void setPublishPlace(String publishPlace) {
                this.publish_place = publishPlace;
            }

            public String getPublishPlace() {
                return publish_place;
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
                this.self_url = selfUrl;
            }

            public String getSelfUrl() {
                return self_url;
            }

            public void setNickName(String nickName) {
                this.nick_name = nickName;
            }

            public String getNickName() {
                return nick_name;
            }

            public void setPriaseNumber(int priaseNumber) {
                this.priase_number = priaseNumber;
            }

            public int getPriaseNumber() {
                return priase_number;
            }

            public void setReplyNumber(int replyNumber) {
                this.reply_number = replyNumber;
            }

            public int getReplyNumber() {
                return reply_number;
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