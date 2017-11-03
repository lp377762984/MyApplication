package com.cn.danceland.myapplication.utils;

public class Constants {

    public static final String HOST = "http://192.168.1.94:8003/";//服务器地址
    public static final String GET_SMS_URL = HOST + "user/encode/";//获取验证码
    public static final String REGISTER_URL = HOST + "user/register";//注册用户
    public static final String LOGIN_URL = HOST + "user/login ";//登录
    public static final String QUERY_USERINFO_URL = HOST + "user/findOne/";//查询用户资料
    public static final String RESET_PASSWORD_URL = HOST + "user/updatePwd";//重置密码
    public static final String SET_BASE_USERINFO_URL = HOST + "user/updateBase";//设置用户基本资料
    public static final String RESET_USERINFO_URL = HOST + "user/changeUserInformation";//重置用户资料
    public static final String LOGIN_BY_PHONE_URL = HOST + "user/findByPhone/";//短信登录
    public static final String LOGOUT_URL = HOST + "user/logOut";//用户退出


    public static final String UPLOADFILE_URL = HOST + "user/uploadFile";//上传文件


    public static final String ISLOGINED = "islogined";//是否登录
    public static final String MY_USERID = "my_userid";//我的ID
    public static final String MY_MEMBERNO = "memberNo";//我的会员号
    public static final String MY_PSWD = "mypswd";//我的密码


    public static final String MY_INFO = "my_info";//我的资料
}
