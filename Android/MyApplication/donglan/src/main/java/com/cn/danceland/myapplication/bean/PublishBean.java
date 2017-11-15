package com.cn.danceland.myapplication.bean;

import java.util.ArrayList;

/**
 * Created by feng on 2017/11/14.
 */
/**
 * Copyright 2017 bejson.com
 */
import java.util.List;
import java.util.Date;

/**
 * Auto-generated: 2017-11-15 9:47:39
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class PublishBean {

    private int author;
    private String content;
    private boolean enabled;
    private int id;
    private List<String> imgList;
    private String imgs;
    private int msgType;
    private int privacyType;
    private String publishPlace;
    private Date publishTime;
    private int publishType;
    private int shareId;
    private String title;
    public void setAuthor(int author) {
        this.author = author;
    }
    public int getAuthor() {
        return author;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
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

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }
    public List<String> getImgList() {
        return imgList;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }
    public String getImgs() {
        return imgs;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }
    public int getMsgType() {
        return msgType;
    }

    public void setPrivacyType(int privacyType) {
        this.privacyType = privacyType;
    }
    public int getPrivacyType() {
        return privacyType;
    }

    public void setPublishPlace(String publishPlace) {
        this.publishPlace = publishPlace;
    }
    public String getPublishPlace() {
        return publishPlace;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }
    public Date getPublishTime() {
        return publishTime;
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

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

}
