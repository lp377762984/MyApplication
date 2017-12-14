/**
 * Copyright 2017 bejson.com
 */
package com.cn.danceland.myapplication.bean;


import java.io.Serializable;

/**
 * Auto-generated: 2017-11-07 13:20:11
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Data implements Serializable {

    private String id;// 会员id
    private String personId;// 个人id

    private String cname; // 中文名称 Varchar(50)
    private String default_branch;//默认店
    private String phone_no; // 手机号 Varchar(20)
    private String nick_name; // 昵称 Vharchar(50)
    private int sys_role;//用户身份
    private String login_name;//登录名
    private String weichat_no;//微信号
    private String identity_card;//身份证号
    private String department_id;//加盟商号
    private String height; // 身高 Float
    private String weight; // 体重 Float
    private String reg_date; // 注册日期 Date
    private String birthday; // 出生日期
    private String zone_code; // 区化编码
    private String enabled; // 是否启用
    private String auth; // 身份
    private String branchId; // 所属门店
    private String gender; // 用户性别
    private String status; // 在线状态
    private String member_no; // 会员编号 Varchar(50)
    private String password;// 登录密码
    private int platform; // 平台
    private String userName; // 用户自定义的会员号 Varchar(50)
    private String avatar_path; // 头像物理路径 Varchar(200)
    private String self_avatar_path; // 个性头像物理路径 Varchar(200)
    private String teachMumberId; // 指导教练
    private String aware_way; // 了解途径
    private String adminMumberId; // 所属会籍
    private String remark; // 备注
    private boolean follower;
    private int  followNumber;//关注数
    private int   dynMsgNumber;//动态数

    private int  fansNum;//粉丝数
    private String token;
    private String verCode;

    private String selfUrl;


    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getDefault_branch() {
        return default_branch;
    }

    public void setDefault_branch(String default_branch) {
        this.default_branch = default_branch;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public int getSys_role() {
        return sys_role;
    }

    public void setSys_role(int sys_role) {
        this.sys_role = sys_role;
    }

    public String getLogin_name() {
        return login_name;
    }

    public void setLogin_name(String login_name) {
        this.login_name = login_name;
    }

    public String getWeichat_no() {
        return weichat_no;
    }

    public void setWeichat_no(String weichat_no) {
        this.weichat_no = weichat_no;
    }

    public String getIdentity_card() {
        return identity_card;
    }

    public void setIdentity_card(String identity_card) {
        this.identity_card = identity_card;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getZone_code() {
        return zone_code;
    }

    public void setZone_code(String zone_code) {
        this.zone_code = zone_code;
    }

    public String getMember_no() {
        return member_no;
    }

    public void setMember_no(String member_no) {
        this.member_no = member_no;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public String getAvatar_path() {
        return avatar_path;
    }

    public void setAvatar_path(String avatar_path) {
        this.avatar_path = avatar_path;
    }

    public String getSelf_avatar_path() {
        return self_avatar_path;
    }

    public void setSelf_avatar_path(String self_avatar_path) {
        this.self_avatar_path = self_avatar_path;
    }

    public String getAware_way() {
        return aware_way;
    }

    public void setAware_way(String aware_way) {
        this.aware_way = aware_way;
    }


    @Override
    public String toString() {
        return "Data{" +
                "id='" + id + '\'' +
                ", personId='" + personId + '\'' +
                ", cname='" + cname + '\'' +
                ", default_branch='" + default_branch + '\'' +
                ", phone_no='" + phone_no + '\'' +
                ", nick_name='" + nick_name + '\'' +
                ", sys_role=" + sys_role +
                ", login_name='" + login_name + '\'' +
                ", weichat_no='" + weichat_no + '\'' +
                ", identity_card='" + identity_card + '\'' +
                ", department_id='" + department_id + '\'' +
                ", height='" + height + '\'' +
                ", weight='" + weight + '\'' +
                ", reg_date='" + reg_date + '\'' +
                ", birthday='" + birthday + '\'' +
                ", zone_code='" + zone_code + '\'' +
                ", enabled='" + enabled + '\'' +
                ", auth='" + auth + '\'' +
                ", branchId='" + branchId + '\'' +
                ", gender='" + gender + '\'' +
                ", status='" + status + '\'' +
                ", member_no='" + member_no + '\'' +
                ", password='" + password + '\'' +
                ", platform=" + platform +
                ", userName='" + userName + '\'' +
                ", avatar_path='" + avatar_path + '\'' +
                ", self_avatar_path='" + self_avatar_path + '\'' +
                ", teachMumberId='" + teachMumberId + '\'' +
                ", aware_way='" + aware_way + '\'' +
                ", adminMumberId='" + adminMumberId + '\'' +
                ", remark='" + remark + '\'' +
                ", follower=" + follower +
                ", followNumber=" + followNumber +
                ", dynMsgNumber=" + dynMsgNumber +
                ", fansNum=" + fansNum +
                ", token='" + token + '\'' +
                ", verCode='" + verCode + '\'' +
                ", selfUrl='" + selfUrl + '\'' +
                '}';
    }

    public int getDynMsgNumber() {
        return dynMsgNumber;
    }

    public void setDynMsgNumber(int dynMsgNumber) {
        this.dynMsgNumber = dynMsgNumber;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getcName() {
        return cname;
    }

    public void setcName(String cName) {
        this.cname = cName;
    }

    public String getPhone() {
        return phone_no;
    }

    public void setPhone(String phone) {
        this.phone_no = phone;
    }

    public String getNickName() {
        return nick_name;
    }

    public void setNickName(String nickName) {
        this.nick_name = nickName;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getRegDate() {
        return reg_date;
    }

    public void setRegDate(String regDate) {
        this.reg_date = regDate;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getZoneCode() {
        return zone_code;
    }

    public void setZoneCode(String zoneCode) {
        this.zone_code = zoneCode;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemberNo() {
        return member_no;
    }

    public void setMemberNo(String memberNo) {
        this.member_no = memberNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRomType() {
        return platform;
    }

    public void setRomType(int romType) {
        this.platform = romType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatarPath() {
        return avatar_path;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatar_path = avatarPath;
    }

    public String getSelfAvatarPath() {
        return self_avatar_path;
    }

    public void setSelfAvatarPath(String selfAvatarPath) {
        this.self_avatar_path = selfAvatarPath;
    }

    public String getTeachMumberId() {
        return teachMumberId;
    }

    public void setTeachMumberId(String teachMumberId) {
        this.teachMumberId = teachMumberId;
    }

    public String getAwareWay() {
        return aware_way;
    }

    public void setAwareWay(String awareWay) {
        this.aware_way = awareWay;
    }

    public String getAdminMumberId() {
        return adminMumberId;
    }

    public void setAdminMumberId(String adminMumberId) {
        this.adminMumberId = adminMumberId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isFollower() {
        return follower;
    }

    public void setFollower(boolean follower) {
        this.follower = follower;
    }

    public int getFollowNumber() {
        return followNumber;
    }

    public void setFollowNumber(int followNumber) {
        this.followNumber = followNumber;
    }

    public int getFansNum() {
        return fansNum;
    }

    public void setFansNum(int fansNum) {
        this.fansNum = fansNum;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getVerCode() {
        return verCode;
    }

    public void setVerCode(String verCode) {
        this.verCode = verCode;
    }

    public String getSelfUrl() {
        return selfUrl;
    }

    public void setSelfUrl(String selfUrl) {
        this.selfUrl = selfUrl;
    }
}