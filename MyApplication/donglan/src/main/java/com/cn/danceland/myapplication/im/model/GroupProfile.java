package com.cn.danceland.myapplication.im.model;

import android.content.Context;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.im.ui.ChatActivity;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMGroupMemberRoleType;
import com.tencent.imsdk.TIMGroupReceiveMessageOpt;
import com.tencent.imsdk.ext.group.TIMGroupBasicSelfInfo;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfo;

/**
 * 群资料
 */
public class GroupProfile implements ProfileSummary {


    private TIMGroupDetailInfo profile;
    private TIMGroupBasicSelfInfo selfInfo;

    public GroupProfile(TIMGroupCacheInfo profile) {
        this.profile = profile.getGroupInfo();
        selfInfo = profile.getSelfInfo();
    }

    public GroupProfile(TIMGroupDetailInfo profile) {
        this.profile = profile;
    }

    /**
     * 获取群ID
     */
    @Override
    public String getIdentify() {
        return profile.getGroupId();
    }


    public void setProfile(TIMGroupCacheInfo profile) {
        this.profile = profile.getGroupInfo();
        selfInfo = profile.getSelfInfo();
    }

    /**
     * 获取头像资源
     */
    @Override
    public int getAvatarRes() {
        return R.drawable.head_group;
    }

    /**
     * 获取头像地址
     */
    @Override
    public String getAvatarUrl() {
        return null;
    }

    /**
     * 获取名字
     */
    @Override
    public String getName() {
        return profile.getGroupName();
    }

    /**
     * 获取描述信息
     */
    @Override
    public String getDescription() {
        return null;
    }


    /**
     * 获取自己身份
     */
    public TIMGroupMemberRoleType getRole() {
        return selfInfo.getRole();
    }


    /**
     * 获取消息接收状态
     */
    public TIMGroupReceiveMessageOpt getMessagOpt() {
        return selfInfo.getRecvMsgOption();
    }

    /**
     * 显示详情
     *
     * @param context 上下文
     */
    @Override
    public void onClick(Context context) {
//        Intent intent = new Intent(context, GroupProfileActivity.class);
//        intent.putExtra("identify", profile.getGroupId());
//        context.startActivity(intent);
//直接跳转到聊天界面
        ChatActivity.navToChat(context, profile.getGroupId(), TIMConversationType.Group, "","");
    }
}
