package com.cn.danceland.myapplication.bean;

/**
 * Created by feng on 2018/3/19.
 */

public class PingJiaCon {

    private Integer branch_id;
    private Integer bus_id;
    private String content;
    private Float score;
    private Integer member_id;
    private String type;
    public void setBranch_id(int branch_id) {
        this.branch_id = branch_id;
    }
    public int getBranch_id() {
        return branch_id;
    }

    public void setBus_id(int bus_id) {
        this.bus_id = bus_id;
    }
    public int getBus_id() {
        return bus_id;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }

    public void setScore(float score) {
        this.score = score;
    }
    public float getScore() {
        return score;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }
    public int getMember_id() {
        return member_id;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
}
