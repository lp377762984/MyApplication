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
    public static final String MODIFY_ZONE = HOST + "/user/modifyZoneCode";//修改区域
    public static final String MODIFY_NAME = HOST + "user/modifyNickName";//修改昵称
    public static final String MODIFYY_IMAGE = HOST + "user/modifySelfAvatar";//修改头像
    public static final String RESET_PHONE_URL = HOST + "user/modifyPhone";//修改手机号
    public static final String MODIFY_GENDER = HOST+"user/modifyGender";//修改性别
    public static final String MODIFY_HEIGHT = HOST+"user/modifyHeight";//修改身高
    public static final String MODIFY_WEIGHT = HOST+"user/modifyWeight";//修改身高
    public static final String BRANCH = HOST+"branch";//获取门店列表
    public static final String JOINBRANCH = HOST+"branch/follow";//关注或者取消门店


    public static final String FIND_ADD_USER_USRL = HOST + "/appDynMsg/findByPhoneOrMemberNoUser";//搜索好友
    public static final String ADD_GUANZHU = HOST + "appDynMsg/saveFollower";//加关注
    public static final String ADD_ZAN_URL = HOST + "appDynMsg/giveThumbs";//加关注

    public static final String FIND_SELF_DT_MSG = HOST + "/appDynMsg/findSelfDynMsg";//个人动态

    public static final String FIND_COMMENT_LIST = HOST + "/appDynMsg/findByReplyUserId";//动态评论列表
    public static final String FIND_ONE_DYN = HOST + "/appDynMsg/findOneDynMsg";   //查一条动态

    public static final String SAVE_DYN_MSG = HOST + "appDynMsg/saveDynMsg";//发布动态
    public static final String DEL_DYN_MSG = HOST + "appDynMsg/deleteOneDynMsg/";//删除一条动态


    public static final String FIND_JINGXUAN_DT_MSG = HOST + "/appDynMsg/findById";//精选动态
    public static final String FIND_GUANZHU_DT_MSG = HOST + "appDynMsg/findFollowerUserDyn";//关注的动态

    public static final String FIND_GUANZHU_USER_LIST_MSG = HOST + "appDynMsg/findByFollower";//查看关注的人
    public static final String FIND_FANS_USER_LIST_MSG = HOST + "appDynMsg/findByUserId";//查看粉丝
    public static final String FIND_ZAN_USER_LIST_MSG_5 = HOST + "/appDynMsg/findByMsgIdOrderByIdDesc";//查看点赞（前5个）
    public static final String FIND_ZAN_USER_LIST_MSG = HOST + "/appDynMsg/findByMsgId";//查看点赞（全部）
    public static final String FIND_PUSH_MSG = HOST + "/appDynMsg/findAllFlowerUserById";//推荐用户
    public static final String UPLOADFILE_URL = HOST + "user/uploadFile";//上传文件
    public static final String UPLOADVEDIO = HOST + "appDynMsg/uploadVedio";//上传视频

    public static final String UPLOADTH = HOST + "appDynMsg/uploadThumbnail";//上传缩略图
    public static final String UPLOAD_FILES_URL = HOST + "appDynMsg/uploadFiles";//上传多文件


    public static final String SEND_COMMENT_REPLY = HOST + "/reply";//发布评论
    public static final String DEL_COMMENT_REPLY = HOST + "/reply";//删除发布评论

    public static final String FINDALLCARDS = HOST + "card/labels";//查找会员卡
    public static final String FIND_CARDS_BY_CARDTYPE = HOST + "card/types?labelId=";//查找会员卡


    public static final String ISLOGINED = "islogined";//是否登录
    public static final String MY_USERID = "my_userid";//我的ID
    public static final String MY_MEMBERNO = "memberNo";//我的会员号
    public static final String MY_PSWD = "mypswd";//我的密码
    public static final String MY_TOKEN = "my_token";//我的token
    public static final String MY_LOCATION = "my_location";//我的地区

    public static final String MY_INFO = "my_info";//我的资料
}
