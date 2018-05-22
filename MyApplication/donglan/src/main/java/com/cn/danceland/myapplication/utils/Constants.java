package com.cn.danceland.myapplication.utils;


import com.cn.danceland.myapplication.MyApplication;

public class Constants  {
    public static final String PLATFORM = "1";//平台1：安卓，2：IOS,3：浏览器

    public static String getVersion(){
        return AppUtils.getVersionName(MyApplication.getContext());
    }
    public static String getPlatform(){
        return PLATFORM;
    }

    //public static final String HOST = "http://192.168.1.94:8003/";//服务器地址
   //public static final String HOST = "http://wx.dljsgw.com/";//阿里云服务器地址
    //public static final String HOST = "http://192.168.1.115:8003/";//老高服务器地址
    // public static final String HOST = "http://192.168.1.130:8003/";//超哥服务器地址
    //  public static final String HOST = " http://192.168.1.123:8003/";//佳楠
    //public static final String HOST = "http://192.168.1.117:8003/";//老高
   // public static final String HOST = " http://192.168.1.119:8003/";//
   public static final String HOST = " http://192.168.1.123:8003/";//
    //public static final String HOST = "http://192.168.1.122:8003/";//超哥

    public static final boolean HX_DEV_CONFIG = true;//是否是开发模式、连接阿里云必须改成false

    public static final String DYNAMICCODE = HOST + "/employ/getDynamicCode";//动态验证码
    public static final String CHECKUPDATE = HOST + "/versionLog/checkUpdate";//检查更新
    public static final String GET_SMS_URL = HOST + "person/encode/";//获取验证码
    public static final String REGISTER_URL = HOST + "person/register";//注册用户
    public static final String REGISTER_AGREEMENT_URL = "http://img.dljsgw.com/protocol/app_register_protocol.html";//注册用户协议


    public static final String LOGIN_URL = HOST + "person/login ";//登录
    public static final String QUERY_USERINFO_URL = HOST + "user/findOne/";//查询用户资料
    public static final String QUERY_USER_DYN_INFO_URL = HOST + "/appDynMsg/findPersonDyn/";//查询用户动态相关资料
    public static final String RESET_PASSWORD_URL = HOST + "/person/updatePwd";//忘记密码
    //   public static final String RESET_PASSWORD_URL = HOST + "user/updatePwd";//重置密码
    public static final String SET_BASE_USERINFO_URL = HOST + "user/updateBase";//设置用户基本资料
    public static final String RESET_USERINFO_URL = HOST + "user/changeUserInformation";//重置用户资料
    public static final String LOGIN_BY_PHONE_URL = HOST + "/person/loginNoPwd";//短信登录

    public static final String LOGOUT_URL = HOST + "/person/logout";//用户退出
    public static final String ZONE = HOST + "/zone";//城市区域
    public static final String MODIFY_ZONE = HOST + "/user/modifyZoneCode";//修改区域
    public static final String MODIFY_NAME = HOST + "user/modifyNickName";//修改昵称
    public static final String MODIFYY_IMAGE = HOST + "user/modifySelfAvatar";//修改头像
    public static final String RESET_PHONE_URL = HOST + "user/modifyPhone";//修改手机号
    public static final String MODIFY_GENDER = HOST + "user/modifyGender";//修改性别
 public static final String MODIFY_WEIXIN = HOST + "user/modifyWeichatNo";//修改微信
 public static final String MODIFY_MAIL = HOST + "user/modifyMail";//修改邮箱
    public static final String MODIFY_HEIGHT = HOST + "user/modifyHeight";//修改身高
    public static final String MODIFY_WEIGHT = HOST + "user/modifyWeight";//修改身高
    public static final String MODIFY_IDENTIFY = HOST + "user/modifyIdentityCard";//修改身份证
    public static final String MODIFY_HOBBY = HOST + "user/modifyHobby";//修改喜好
    public static final String MODIFY_SIGN = HOST + "user/modifySign";//修改个性签名
    public static final String BRANCH = HOST + "branch";//获取门店列表
    public static final String JOINBRANCH = HOST + "branch/join";//加入或者取消门店
    public static final String GETMENUS = HOST + "appRoleFunction/getMenus";//获取菜单权限
    public static final String GETYUANGONGMENUS = HOST + "appMenuFunction/queryEmpFunctions";//获取员工菜单权限
    public static final String GETHUIYUANMENUS = HOST + "appMenuFunction/queryMemFunctions";//获取会员菜单权限
    public static final String RELOAD_LOGININFO = HOST + "/person/reloadLoginInfo";//重新获取用户信息
    public static final String SAVE_REPORT = HOST + "/appReport/saveReport";//举报动态
    public static final String ISJOINBRANCH = HOST + "/branch/iSJoinBranch";//判断是否加入该门店


    public static final String FINDMyLOCKERS = HOST + "appLocker/findMyLockers";//我的租柜

    //报表相关
    public static final String BUSSTATISTICSREPORT = HOST + "statistics/busStatisticsReport";//业务报表
    public static final String SCORESTATISTICSREPORT = HOST + "statistics/scoreStatisticsReport";//业绩报表
    public static final String SAVEREPORT = HOST + "/dailySummary/save";//新增报表
    public static final String FINDREPORT = HOST + "/dailySummary/findByEmployeeId";//查询报表


    //体测上传图片
    public static final String BCAUPLOAD = HOST + "bcaAnalysis/uploadFile";//体测上传图片
    public static final String FIND_ADD_USER_USRL = HOST + "/appDynMsg/queryByPhone";//搜索好友
    public static final String FIND_JOIN_SHOP_URL = HOST + "/myJoinBranch/findJoinBranchs";//查找已经加入门店
    public static final String FIND_CONSULTANT_URL = HOST + "/employ/queryAdmin";//查找会籍顾问
    public static final String FIND_JIAOLIAN_URL = HOST + "/employ/queryTeach";//查找教练

    public static final String FIND_CLUBDYNAMIC_URL = HOST + "/clubDynamic/queryPage";//会所动态

    public static final String CHANGE_CURRENT_SHOP_URL = HOST + "branch/changeCurrentBranch";//更换默认店
    public static final String ADD_GUANZHU = HOST + "appUserFollow/saveUserFollower";//加关注
    public static final String ADD_ZAN_URL = HOST + "appPraise/giveThumbs";//点赞

    public static final String FIND_SELF_DT_MSG = HOST + "/appDynMsg/findSelfDynMsg";//个人动态

    public static final String FIND_COMMENT_LIST = HOST + "/reply/queryReplyByMsgId";//动态评论列表
    public static final String FIND_ONE_DYN = HOST + "/appDynMsg/findOneDynMsg";   //查一条动态

    public static final String SAVE_DYN_MSG = HOST + "appDynMsg/saveDynMsg";//发布动态
    public static final String DEL_DYN_MSG = HOST + "appDynMsg/deleteOneDynMsg/";//删除一条动态
    public static final String SET_MIPUSH_ID = HOST + "user/modifyRegId";//设置mipushid
    public static final String UPDATE_MIPUSH_CONFIG =  "update_mipush_config";//更新mipush


    public static final String FIND_JINGXUAN_DT_MSG = HOST + "/appDynMsg/findChoiceness";//精选动态
    public static final String FIND_GUANZHU_DT_MSG = HOST + "appDynMsg/findFollowerUserDyn";//关注的动态
    public static final String FIND_GUANZHU_USER_LIST_MSG = HOST + "/appUserFollow/queryByFollowPage";//查看关注的人
    public static final String FIND_FANS_USER_LIST_MSG = HOST + "/appUserFollow/queryByUserIdPage";//查看粉丝
    public static final String FIND_ZAN_USER_LIST_MSG_5 = HOST + "/appPraise/findByMsgIdOrderByIdDesc";//查看点赞（前5个）
    public static final String FIND_ZAN_USER_LIST_MSG = HOST + "/appPraise/findByMsgIdOrderByIdDesc";//查看点赞（全部）
    public static final String FIND_PUSH_MSG = HOST + "/appDynMsg/findAllFlowerUserById";//推荐用户
    public static final String UPLOADFILE_URL = HOST + "user/uploadFile";//上传文件
    public static final String UPLOADVEDIO = HOST + "appDynMsg/uploadVedio";//上传视频

    public static final String UPLOADTH = HOST + "appDynMsg/uploadThumbnail";//上传缩略图
    public static final String UPLOAD_FILES_URL = HOST + "appDynMsg/uploadFiles";//上传多文件

    public static final String FIND_NEWS_URL = HOST + "appHome/showNews/";//查找新闻列表
    public static final String FIND_IMAGE_NEWS_URL = HOST + "appHome/showCarousel";//查找新闻轮播图片
    public static final String FIND_BRANCHRANKING = HOST + "/cardenter/branchRanking";//我的门店的排名
    public static final String FIND_MYRANKING_URL = HOST + "/cardenter/myRanking";//我的排名


    public static final String SEND_COMMENT_REPLY = HOST + "reply/saveReply";//发布评论
    public static final String DEL_COMMENT_REPLY = HOST + "/reply/delete";//删除发布评论

    public static final String FINDALLCARDS = HOST + "card/labels";//查找会员卡
    public static final String FIND_CARDS_BY_CARDTYPE = HOST + "card/types?labelId=";//查找会员卡
    public static final String FINDMEMBER = HOST + "appBca/searchMember";//

    //订单相关

    public static final String COMMIT_DEPOSIT = HOST + "appOrder/saveOrder";//提交定金订单
    public static final String FIND_ALL_DEPOSIT = HOST + "appDeposit/findAllDeposit";//查询订单
    public static final String COMMIT_CARD_ORDER = HOST + "appOrder/saveOrder";//提交卡订单
    public static final String FIND_ALL_ORDER = HOST + "appOrder/findAllPage";//查询订单
    public static final String FIND_ALL_MY_CARD_LIST = HOST + "card/list";//查询我的会员卡
    public static final String FIND_ALL_OTHER_CARD_LIST = HOST + "/card/othersList";//查询我送出的会员卡

    public static final String CANSEL_ORDER = HOST + "/appOrder/cancelOrder";//取消订单

    public static final String FIND_BC_DATA = HOST + "appBca/findMemberBcaData";//查找最近一次体测记录
    public static final String BANNER = HOST + "branch/banner";//门店轮播图
    public static final String MYCONSUME = HOST + "/appOrder/queryMyOrder";//我的消费


    //参数相关

    public static final String FIND_BY_TYPE_CODE = HOST + "/appDict/queryList";//查询潜客资料相关参数
    public static final String FIND_BY_TYPE_CODE_GET = HOST + "/appDict/findByTypeCode/";//查询潜客资料相关参数

    public static final String FIND_PING_FEN = HOST + "evaluate/findEmployeeAverage";//查找教练

    public static final String COMMIT_ALIPAY = HOST + "/appOrder/alipayNotify";//支付宝回调
    public static final String COMMIT_WECHAT_PAY = HOST + "/appOrder/weichatNotify";//微信回调
    public static final String COMMIT_CHUZHIKA = HOST + "appOrder/storeConsume";//储值卡消费回调

    //潜客相关
    public static final String ADD_POTENTIAL = HOST + "/appPotential/savePotential";//添加潜客
    public static final String FIND_POTENTIAL_LIST = HOST + "/appPotential/recentMaintain";//查询潜客列表
    public static final String ADD_VISIT_RECOR = HOST + "/appPotential/saveVisitRecord";//添加回访记录
    public static final String FIND_BY_ID_POTENTIAL = HOST + "/appPotential/findByIdPotential/";//查询潜客详情
    public static final String FIND_VISIT_RECORD = HOST + "/appPotential/findVisitRecord";//查询回访记录
    public static final String ADD_UPCOMING_MATTER = HOST + "/appPotential/saveUpcomingMatter";//添加待办
    public static final String FIND_UPCOMING_MATTER_PARAM = HOST + "appWorkResult/queryList";//查询待办参数
    public static final String FIND_UPCOMING_MATTER = HOST + "/appPotential/findUpcomingMatter";//查询待办列表
    public static final String UPDATE_MATTER_STATUS = HOST + "/appPotential/updateMatterStatus";//处理待办
    public static final String FIND_NOT_UPCOMINGMATTER = HOST + "/appPotential/findNotUpcomingMatter";//查询未处理待办
    public static final String UPDATE_POTENTIAL = HOST + "/appPotential/updatePotential";//编辑潜客资料

    public static final String INTRODUCE_SAVE = HOST + "/introduce/save";//推荐好友
    public static final String INTRODUCE_CONFIRM = HOST + "/introduce/confirm";//提交推荐
    public static final String INTRODUCE_QUERYLIST = HOST + "/introduce/queryList";//查询推荐和被推荐

    public static final String SELL_CARD_PRARMS = "http://192.168.1.93/test.txt";//买卡参数

    public static final String ISLOGINED = "islogined";//是否登录
    public static final String MY_USERID = "my_userid";//我的ID
    public static final String MY_MEMBER_ID = "my_member_id";//我的member_ID

    public static final String MY_DYN = "my_nyn";//我的动态数
    public static final String MY_FANS = "my_fans";//我的粉丝数
    public static final String MY_FOLLOWS = "my_follows";//我的评论数

    public static final String MY_MEMBERNO = "memberNo";//我的会员号
    public static final String MY_PSWD = "mypswd";//我的密码
    public static final String MY_TOKEN = "my_token";//我的token
    public static final String MY_LOCATION = "my_location";//我的地区
    public static final String MY_INFO = "my_info";//我的资料
    public static final String MY_MIPUSH_ID = "my_mipush_id";//小米推送id

    public static final String BRANCH_DEPOSIT_DAYS = "deposit_days";//定金有效期
    public static final String BRANCH_OPEN_DAYS = "open_days";//开卡有效期
    public static final String FIND_PARAM_KEY = HOST + "appParam/findParamKey";//查询参数
    public static final String REAYTEST = HOST + "appBca/bcaTestUpdateMemberAndPerson";//提交用户信息
    public static final String GETEQUIPMENT = HOST + "appBca/findPageBranchBca";//获取门店体测仪列表
    public static final String CONNECTEQU = HOST + "appBca/connectTester";//连接设备
    public static final String ISFINISHED = HOST + "appBca/isFinishedBca";//体测是否完成
    public static final String FITNESS_HITORY = HOST + "appBca/findHistoryRecord";//体测历史记录
    public static final String FINDONEHISTORY = HOST + "appBca/findOneHistoryRecord";//查看某条历史记录
    public static final String FINDBODYAGE = HOST + "appBca/findBodyAge";//查询身体年龄
    public static final String CANCELTEST = HOST + "appBca/cancelTester";//取消体测

    //私教相关
    public static final String COURSETYPELIST = HOST + "appCourse/findPageCourseType";//购买私教课程列表
    public static final String FINDCourseTypeEmployee = HOST + "appCourse/findCourseTypeEmployee";//查私教教练
    public static final String FINDGROUPCLASS = HOST + "appGroupClass/queryPage";//小团课列表
    public static final String FINDMEMBERCOURSE = HOST + "appCourse/findMemberCourse";//我的私教
    public static final String FINDEMPCOURSE = HOST + "appCourse/findEmployeeCourse";//教练的私教
    public static final String FINDAVAI = HOST + "appCourse/availableList";//我的私教
    public static final String COURSEAPPOIN = HOST + "appCourse/courseAppoint";//预约私教
    public static final String QUERYKECHENGBIAO = HOST + "appGroupCourse/queryGroupCourseByData";//小团课课程表
    public static final String GROUPAPPOINT = HOST + "appGroupCourse/groupAppoint";//预约小团课
    public static final String FINDGROUP = HOST + "appGroupCourse/findById";//单节小团课详情
    public static final String FREEGROUPCOURSE = HOST + "appFreeGroupCourse/findById";//免费团课详情
    public static final String FINDGROUPCOURSEAPPOINTLIST = HOST + "appGroupCourse/findGroupCourseAppointList";//小团课预约记录
    public static final String FreeCourse = HOST + "appFreeGroupCourse/queryFreeGroupCourseByData";//查询某会员某天的团课课程表
    public static final String FreeCourseApply = HOST + "appFreeGroupCourse/freeGroupApply";//报名小团课
    public static final String APPOINTLIST = HOST + "appCourse/findCourseAppointList";//获取自己的一对一预约记录
    public static final String APPOINTCANCEL = HOST + "appCourse/courseAppointCancel";//取消私教
    public static final String FREECOURSELIST = HOST + "appFreeGroupCourse/findFreeGroupCourseApplyList";//免费团课记录
    public static final String FREECANCELGROUP = HOST + "appFreeGroupCourse/freeGroupCancelApply";//取消免费团课报名
    public static final String PINGJIA = HOST + "evaluate/save";//评价接口
    public static final String FINDPINGJIA = HOST + "evaluate/findById/";//查询一条评价
    public static final String ENTERCOURSE = HOST + "appCourse/courseAppointConfirm ";//确认私教
    public static final String SCAN_QRCODE = HOST + "qrcode";//扫码入场
    public static final String QUERYGROUPCOURSE = HOST + "appGroupCourse/queryBuyCoursePersonInfo";//查询购买此小团课的人
    public static final String FINDGROUPCOURSEAPPOINTPERSON = HOST + "appGroupCourse/findGroupCourseAppointPerson";//查看某节小团课参与成员
    public static final String QUERYBUYCOURSEPERSONINFO = HOST + "appCourse/queryBuyCoursePersonInfo";//查询购买此一对一课种的人
    public static final String FINDFREEGROUPCOURSEAPPLYPERSON = HOST + "appFreeGroupCourse/findFreeGroupCourseApplyPerson";//免费团课
  public static final String FOROTHERSIJIAOLIST = HOST + "appCourse/queryForOthersList";//为他人购买的私教列表
    public static final String QUERYAVERAGE = HOST + "/evaluate/queryAverage";//课程详情里边的三个评分


    //环信相关
    public static final String NEW_FRIENDS_USERNAME = "item_new_friends";
    public static final String GROUP_USERNAME = "item_groups";
    public static final String CHAT_ROOM = "item_chatroom";
    public static final String ACCOUNT_REMOVED = "account_removed";
    public static final String ACCOUNT_CONFLICT = "conflict";
    public static final String ACCOUNT_FORBIDDEN = "user_forbidden";
    public static final String ACCOUNT_KICKED_BY_CHANGE_PASSWORD = "kicked_by_change_password";
    public static final String ACCOUNT_KICKED_BY_OTHER_DEVICE = "kicked_by_another_device";
    public static final String CHAT_ROBOT = "item_robots";
    public static final String MESSAGE_ATTR_ROBOT_MSGTYPE = "msgtype";
    public static final String ACTION_GROUP_CHANAGED = "action_group_changed";
    public static final String ACTION_CONTACT_CHANAGED = "action_contact_changed";

    public static final String EXTRA_CONFERENCE_ID = "confId";
    public static final String EXTRA_CONFERENCE_PASS = "password";
    public static final String EXTRA_CONFERENCE_IS_CREATOR = "is_creator";


    public static final int QR_MAPPING_CARD_ENTER = 10; //刷卡入场
    public static final int QR_MAPPING_COURSE_ENTER = 11; //私教入场
    public static final int QR_MAPPING_GROUP_COURSE_ENTER = 12;//小团课入场


}
