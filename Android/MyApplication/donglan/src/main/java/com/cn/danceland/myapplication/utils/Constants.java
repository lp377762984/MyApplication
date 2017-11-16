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
    public static final String ZONE = HOST + "/zone";//城市区域
    public static final String MODIFY_ZONE = HOST+"/user/modifyZoneCode";//修改区域
    public static final String MODIFY_NAME = HOST+"user/modifyNickName";//修改昵称

    public static final String FIND_SELF_DT_MSG = HOST+"/appDynMsg/findSelfDynMsg";//个人动态
    public static final String SAVE_DYN_MSG = HOST+"appDynMsg/saveDynMsg";//发布动态

    public static final String FIND_JINGXUAN_DT_MSG = HOST+"/appDynMsg/findById";//精选动态

    public static final String FIND_PUSH_MSG = HOST+"/appDynMsg/findAllFlowerUserById";//推荐用户
    public static final String RESET_PHONE_URL = HOST + "user/modifyPhone";//修改手机号
    public static final String UPLOADFILE_URL = HOST + "user/uploadFile";//上传文件

    public static final String UPLOAD_FILES_URL = HOST + "appDynMsg/uploadFiles";//上传多文件





    public static final String ISLOGINED = "islogined";//是否登录
    public static final String MY_USERID = "my_userid";//我的ID
    public static final String MY_MEMBERNO = "memberNo";//我的会员号
    public static final String MY_PSWD = "mypswd";//我的密码
    public static final String MY_TOKEN = "my_token";//我的token
    public static final String MY_LOCATION = "my_location";//我的地区

    public static final String MY_INFO = "my_info";//我的资料
}
