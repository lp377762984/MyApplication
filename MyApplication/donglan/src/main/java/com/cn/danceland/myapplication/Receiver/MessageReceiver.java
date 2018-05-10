package com.cn.danceland.myapplication.Receiver;

import android.app.Notification;
import android.content.Context;
import android.text.TextUtils;
import android.text.format.Time;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.RequestLoginInfoBean;
import com.cn.danceland.myapplication.db.DBData;
import com.cn.danceland.myapplication.db.MiMessage;
import com.cn.danceland.myapplication.evntbus.StringEvent;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by feng on 2017/12/8.
 */

public class MessageReceiver extends PushMessageReceiver {

    int i;
    int pinglunNum;
    int dianzanNum;
    int fansNum;
    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {
        DBData db = new DBData();
        i = SPUtils.getInt("messageN",1);
        pinglunNum = SPUtils.getInt("pinglunNum",0);
        dianzanNum = SPUtils.getInt("dianzanNum",0);
        fansNum = SPUtils.getInt("fansNum",0);
        MiMessage miMessage = new MiMessage();
        Time time = new Time();
        time.setToNow();
        if(message!=null){
            LogUtil.i(message.getContent());

            Map<String, String> extra = message.getExtra();
            String type = extra.get("type");
            miMessage.setType(type);
            if(("1".equals(type))){
                SPUtils.setInt("dianzanNum",dianzanNum+1);
            }else if("2".equals(type)){
                SPUtils.setInt("fansNum",fansNum+1);
            }else if("3".equals(type)){
                SPUtils.setInt("pinglunNum",pinglunNum+1);
            }else if("4".equals(type)){
                reloadInfo();
            }
            String personId = extra.get("personId");
            miMessage.setPersonId(personId);
            String personName = extra.get("personName");
            miMessage.setPersonName(personName);
            String dynId = extra.get("dynId");
            miMessage.setDynId(dynId);
            String selfPath = extra.get("selfPath");
            miMessage.setSelfPath(selfPath);
            miMessage.setId(i);
            miMessage.setContent(message.getContent());
            miMessage.setTime((time.month+1)+"-"+time.monthDay);
            db.addMessageD(miMessage);
            SPUtils.setInt("messageN",i+1);
            pinglunNum = SPUtils.getInt("pinglunNum",0);
            dianzanNum = SPUtils.getInt("dianzanNum",0);
            fansNum = SPUtils.getInt("fansNum",0);
            EventBus.getDefault().post(new StringEvent(pinglunNum+dianzanNum+fansNum+"",101));
            //LogUtil.e("zzf",message.getContent());
            //int badgeCount = 1;
            ShortcutBadger.applyCount(context, pinglunNum+dianzanNum+fansNum); //for 1.1.4+
        }
    }

    private void reloadInfo() {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.RELOAD_LOGININFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                Gson gson = new Gson();
                RequestLoginInfoBean loginInfoBean = gson.fromJson(s, RequestLoginInfoBean.class);
                if (loginInfoBean.getSuccess()) {
                    DataInfoCache.saveOneCache(loginInfoBean.getData(), Constants.MY_INFO);
                    ToastUtils.showToastShort("您的角色信息已发生变化！");
                } else {
                    ToastUtils.showToastShort("角色信息拉取失败！请检查网络！");
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToastShort("请求失败，请查看网络连接");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, null));
                // LogUtil.i("Bearer+"+SPUtils.getString(Constants.MY_TOKEN,null));
                return map;
            }

        };
        MyApplication.getHttpQueues().add(request);
    }

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage miPushMessage) {
        super.onNotificationMessageArrived(context, miPushMessage);
        ToastUtils.showToastLong("收到通知推送" + miPushMessage.toString());
        LogUtil.i("收到通知推送" + miPushMessage.toString());
    }

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        super.onReceiveRegisterResult(context, miPushCommandMessage);
        List<String> arguments = miPushCommandMessage.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
       // LogUtil.i(cmdArg1);

    }

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();



        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                List<String> arguments = message.getCommandArguments();
                String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
                if (!TextUtils.isEmpty(cmdArg1)) {
                    if (!TextUtils.equals(cmdArg1, SPUtils.getString(Constants.MY_MIPUSH_ID, ""))) {
                        SPUtils.setString(Constants.MY_MIPUSH_ID, cmdArg1);

                       LogUtil.i("MY_REGID="+SPUtils.getString(Constants.MY_MIPUSH_ID,""));
                        SPUtils.setBoolean(Constants.UPDATE_MIPUSH_CONFIG,true);


                    }
                }

               // LogUtil.i("REGID="+SPUtils.getString(Constants.MY_MIPUSH_ID,""));


//                String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);

                LogUtil.i(MiPushClient.getRegId(context));


            } else {
            }
        } else if (MiPushClient.COMMAND_SET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
            } else {
            }
        } else if (MiPushClient.COMMAND_UNSET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {

            } else {

            }
        } else if (MiPushClient.COMMAND_SET_ACCOUNT.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {

            } else {

            }
        } else if (MiPushClient.COMMAND_UNSET_ACCOUNT.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {

            } else {

            }
        } else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {

            } else {

            }
        } else if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {

            } else {

            }
        } else if (MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {

            } else {

            }
        }
    }


}
