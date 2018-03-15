package com.cn.danceland.myapplication.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.activity.MainActivity;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.easeui.DemoHelper;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.ui.EaseChatFragment.EaseChatFragmentHelper;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.easeui.widget.presenter.EaseChatRowPresenter;
import com.hyphenate.util.EasyUtils;
import com.hyphenate.util.PathUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MyChatFragment extends EaseChatFragment implements EaseChatFragmentHelper{

	// Constants start from 11 to avoid conflict with Constants in base class
    private static final int ITEM_VIDEO = 11;
    private static final int ITEM_FILE = 12;
    private static final int ITEM_VOICE_CALL = 13;
    private static final int ITEM_VIDEO_CALL = 14;

    private static final int REQUEST_CODE_SELECT_VIDEO = 11;
    private static final int REQUEST_CODE_SELECT_FILE = 12;
    private static final int REQUEST_CODE_GROUP_DETAIL = 13;
    private static final int REQUEST_CODE_CONTEXT_MENU = 14;
    private static final int REQUEST_CODE_SELECT_AT_USER = 15;
    

    private static final int MESSAGE_TYPE_SENT_VOICE_CALL = 1;
    private static final int MESSAGE_TYPE_RECV_VOICE_CALL = 2;
    private static final int MESSAGE_TYPE_SENT_VIDEO_CALL = 3;
    private static final int MESSAGE_TYPE_RECV_VIDEO_CALL = 4;
    private static final int MESSAGE_TYPE_RECALL = 9;

    /**
     * if it is chatBot 
     */
    private boolean isRobot;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState,false
                //DemoHelper.getInstance().getModel().isMsgRoaming() && (chatType != EaseConstant.CHATTYPE_CHATROOM)
        );
    }

    @Override
    protected void setUpView() {
        setChatFragmentHelper(this);
        if (chatType == Constants.CHATTYPE_SINGLE) {
//            Map<String,RobotUser> robotMap = DemoHelper.getInstance().getRobotList();
//            if(robotMap!=null && robotMap.containsKey(toChatUsername)){
//                isRobot = true;
//            }
        }
        super.setUpView();
        // set click listener
        titleBar.setLeftLayoutClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (EasyUtils.isSingleActivity(getActivity())) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }
                onBackPressed();
            }
        });



      //  ((EaseEmojiconMenu)inputMenu.getEmojiconMenu()).addEmojiconGroup(EmojiconExampleGroupData.getData());
        if(chatType == EaseConstant.CHATTYPE_GROUP){
            inputMenu.getPrimaryMenu().getEditText().addTextChangedListener(new TextWatcher() {
                
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(count == 1 && "@".equals(String.valueOf(s.charAt(start)))){
                        ToastUtils.showToastShort("选择联系人");
//                        startActivityForResult(new Intent(getActivity(), PickAtUserActivity.class).
//                                putExtra("groupId", toChatUsername), REQUEST_CODE_SELECT_AT_USER);
                    }
                }
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    
                }
                @Override
                public void afterTextChanged(Editable s) {
                    
                } 
            });
        }
    }
    
    @Override
    protected void registerExtendMenuItem() {
        //use the menu in base class
        super.registerExtendMenuItem();
        //extend menu items
//        inputMenu.registerExtendMenuItem(R.string.attach_video, R.drawable.em_chat_video_selector, ITEM_VIDEO, extendMenuItemClickListener);
//        inputMenu.registerExtendMenuItem(R.string.attach_file, R.drawable.em_chat_file_selector, ITEM_FILE, extendMenuItemClickListener);
//        if(chatType == Constants.CHATTYPE_SINGLE){
//            inputMenu.registerExtendMenuItem(R.string.attach_voice_call, R.drawable.em_chat_voice_call_selector, ITEM_VOICE_CALL, extendMenuItemClickListener);
//            inputMenu.registerExtendMenuItem(R.string.attach_video_call, R.drawable.em_chat_video_call_selector, ITEM_VIDEO_CALL, extendMenuItemClickListener);
//        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CONTEXT_MENU) {
//            switch (resultCode) {
//            case ContextMenuActivity.RESULT_CODE_COPY: // copy
//                clipboard.setPrimaryClip(ClipData.newPlainText(null,
//                        ((EMTextMessageBody) contextMenuMessage.getBody()).getMessage()));
//                break;
//            case ContextMenuActivity.RESULT_CODE_DELETE: // delete
//                conversation.removeMessage(contextMenuMessage.getMsgId());
//                messageList.refresh();
//                // To delete the ding-type message native stored acked users.
//                EaseDingMessageHelper.get().delete(contextMenuMessage);
//                break;
//
//            case ContextMenuActivity.RESULT_CODE_FORWARD: // forward
//                Intent intent = new Intent(getActivity(), ForwardMessageActivity.class);
//                intent.putExtra("forward_msg_id", contextMenuMessage.getMsgId());
//                startActivity(intent);
//                break;
//            case ContextMenuActivity.RESULT_CODE_RECALL://recall
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            EMMessage msgNotification = EMMessage.createTxtSendMessage(" ",contextMenuMessage.getTo());
//                            EMTextMessageBody txtBody = new EMTextMessageBody(getResources().getString(R.string.msg_recall_by_self));
//                            msgNotification.addBody(txtBody);
//                            msgNotification.setMsgTime(contextMenuMessage.getMsgTime());
//                            msgNotification.setLocalTime(contextMenuMessage.getMsgTime());
//                            msgNotification.setAttribute(Constants.MESSAGE_TYPE_RECALL, true);
//                            msgNotification.setStatus(EMMessage.Status.SUCCESS);
//                            EMClient.getInstance().chatManager().recallMessage(contextMenuMessage);
//                            EMClient.getInstance().chatManager().saveMessage(msgNotification);
//                            messageList.refresh();
//                        } catch (final HyphenateException e) {
//                            e.printStackTrace();
//                            getActivity().runOnUiThread(new Runnable() {
//                                public void run() {
//                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        }
//                    }
//                }).start();
//
//                // Delete group-ack data according to this message.
//                EaseDingMessageHelper.get().delete(contextMenuMessage);
//                break;
//
//            default:
//                break;
 //           }
        }
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode) {
            case REQUEST_CODE_SELECT_VIDEO: //send the video
                if (data != null) {
                    int duration = data.getIntExtra("dur", 0);
                    String videoPath = data.getStringExtra("path");
                    File file = new File(PathUtil.getInstance().getImagePath(), "thvideo" + System.currentTimeMillis());
                    try {
                        FileOutputStream fos = new FileOutputStream(file);
                        Bitmap ThumbBitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
                        ThumbBitmap.compress(CompressFormat.JPEG, 100, fos);
                        fos.close();
                        sendVideoMessage(videoPath, file.getAbsolutePath(), duration);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case REQUEST_CODE_SELECT_FILE: //send the file
                if (data != null) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        sendFileByUri(uri);
                    }
                }
                break;
            case REQUEST_CODE_SELECT_AT_USER:
                if(data != null){
                    String username = data.getStringExtra("username");
                    inputAtUsername(username, false);
                }
                break;
            default:
                break;
            }
        }
        if (requestCode == REQUEST_CODE_GROUP_DETAIL) {
//            switch (resultCode) {
//                case GroupDetailsActivity.RESULT_CODE_SEND_GROUP_NOTIFICATION:
//                    // Start the ding-type msg send ui.
//                    EMLog.i(TAG, "Intent to the ding-msg send activity.");
//                    Intent intent = new Intent(getActivity(), EaseDingMsgSendActivity.class);
//                    intent.putExtra(EaseConstants.EXTRA_USER_ID, toChatUsername);
//                    startActivityForResult(intent, REQUEST_CODE_DING_MSG);
//                    break;
//            }
        }
    }

    @Override
    public void onSetMessageAttributes(EMMessage message) {
//        if (isRobot) {
//            // set message extension
//            message.setAttribute("em_robot_message", isRobot);
//
//        }
        // 通过扩展属性，将userPic和userName发送出去。
       Data myInfo = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
        String userPic = myInfo.getSelf_avatar_path();
        if (!TextUtils.isEmpty(userPic)) {
            message.setAttribute("userPic", userPic);
        }
        String userName =  myInfo.getNick_name();
        if (!TextUtils.isEmpty(userName)) {
            message.setAttribute("userName", userName);
        }

    }
    
    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return new CustomChatRowProvider();
    }
  

    @Override
    public void onEnterToChatDetails() {
        if (chatType == Constants.CHATTYPE_GROUP) {
            EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
            if (group == null) {
                Toast.makeText(getActivity(), R.string.gorup_not_found, Toast.LENGTH_SHORT).show();
                return;
            }
            //群组详情
//            startActivityForResult(
//                    (new Intent(getActivity(), GroupDetailsActivity.class).putExtra("groupId", toChatUsername)),
//                    REQUEST_CODE_GROUP_DETAIL);
        }
//        else if(chatType == Constants.CHATTYPE_CHATROOM){
//        	startActivityForResult(new Intent(getActivity(), ChatRoomDetailsActivity.class).putExtra("roomId", toChatUsername), REQUEST_CODE_GROUP_DETAIL);
//        }
    }

    @Override
    public void onAvatarClick(String username) {
        //handling when user click avatar
//        Intent intent = new Intent(getActivity(), UserProfileActivity.class);
//        intent.putExtra("username", username);
//        startActivity(intent);
    }
    
    @Override
    public void onAvatarLongClick(String username) {
        inputAtUsername(username);
    }
    
    
    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        //消息框点击事件，demo这里不做覆盖，如需覆盖，return true
        return false;
    }
    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        //  LogUtil.i("收到消息");

        for (EMMessage message : messages) {
            // message.setMsgTime(System.currentTimeMillis());
            //LogUtil.i(DateUtils.getTimestampString(new Date(message.getMsgTime())) + message.getBody());
            //  LogUtil.i(DateUtils.getTimestampString(new Date(System.currentTimeMillis() + 1L)) + message.getBody());



            String username = null;
            // 群消息
            if (message.getChatType() == EMMessage.ChatType.GroupChat || message.getChatType() == EMMessage.ChatType
                    .ChatRoom) {
                username = message.getTo();
            } else {
                // 个人消息
                username = message.getFrom();
            }
            // if the message is for current conversation
            if (username.equals(toChatUsername) || message.getTo().equals(toChatUsername)) {
                messageList.refreshSelectLast();
                EaseUI.getInstance().getNotifier().vibrateAndPlayTone(message);
            } else {
                EaseUI.getInstance().getNotifier().onNewMsg(message);
            }

            //更新对话列表，设置通知声音
            messageList.refreshSelectLast();
            EaseUI.getInstance().getNotifier().vibrateAndPlayTone(message);
            //   messageList.refresh();
            //    message.setMsgTime(System.currentTimeMillis());
            //************接收并处理扩展消息***********************
            String userName = message.getStringAttribute("userName", "");
            String userPic = message.getStringAttribute("userPic", "");
            LogUtil.i(userName+userPic);
            String hxIdFrom = message.getFrom();
            EaseUser easeUser = new EaseUser(hxIdFrom);
            easeUser.setAvatar(userPic);
            easeUser.setNick(userName);

            List<EaseUser> users = new ArrayList<EaseUser>();
            users.add(easeUser);

//            // 存入内存
//            DemoHelper.getInstance().getContactList();
//            DemoHelper.contactList.put(hxIdFrom, easeUser);
//            // 存入db
//            UserDao dao = new UserDao(MesApplication.getInstance().getContext());
//            dao.saveContactList(users);

            DemoHelper.getInstance().updateContactList(users);
//
//            DemoHelper.getInstance().getModel().setContactSynced(true);
//            // 通知listeners联系人同步完毕
//            DemoHelper.getInstance().notifyContactsSyncListener(true);
//            if (DemoHelper.getInstance().isGroupsSyncedWithServer()) {
//                //  notifyForRecevingEvents();
//                DemoHelper.getInstance().notify();
//                //   notifyNewInviteMessage();
//            }

        }

        super.onMessageReceived(messages);
    }


    @Override
    public void onMessageBubbleLongClick(EMMessage message) {
    	// no message forward when in chat room
//        startActivityForResult((new Intent(getActivity(), ContextMenuActivity.class)).putExtra("message",message)
//                .putExtra("ischatroom", chatType == EaseConstant.CHATTYPE_CHATROOM),
//                REQUEST_CODE_CONTEXT_MENU);
    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        switch (itemId) {
        case ITEM_VIDEO:
//            Intent intent = new Intent(getActivity(), ImageGridActivity.class);
//            startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
            break;
        case ITEM_FILE: //file
            selectFileFromLocal();
            break;
        case ITEM_VOICE_CALL:
          //  startVoiceCall();
            break;
        case ITEM_VIDEO_CALL:
            //startVideoCall();
            break;
        default:
            break;
        }
        //keep exist extend menu
        return false;
    }
    
    /**
     * select file
     */
    protected void selectFileFromLocal() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT < 19) { //api 19 and later, we can't use this way, demo just select from images
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
    }
    
    /**
     * make a voice call
     */
//    protected void startVoiceCall() {
//        if (!EMClient.getInstance().isConnected()) {
//            Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
//        } else {
//            startActivity(new Intent(getActivity(), VoiceCallActivity.class).putExtra("username", toChatUsername)
//                    .putExtra("isComingCall", false));
//            // voiceCallBtn.setEnabled(false);
//            inputMenu.hideExtendMenuContainer();
//        }
//    }
//
    /**
     * make a video call
     */
//    protected void startVideoCall() {
//        if (!EMClient.getInstance().isConnected())
//            Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
//        else {
//            startActivity(new Intent(getActivity(), VideoCallActivity.class).putExtra("username", toChatUsername)
//                    .putExtra("isComingCall", false));
//            // videoCallBtn.setEnabled(false);
//            inputMenu.hideExtendMenuContainer();
//        }
//    }
    
    /**
     * chat row provider 
     *
     */
    private final class CustomChatRowProvider implements EaseCustomChatRowProvider {
        @Override
        public int getCustomChatRowTypeCount() {
            //here the number is the message type in EMMessage::Type
        	//which is used to count the number of different chat row
            return 11;
        }

        @Override
        public int getCustomChatRowType(EMMessage message) {
            if(message.getType() == EMMessage.Type.TXT){
                //voice call
                if (message.getBooleanAttribute(Constants.MESSAGE_ATTR_IS_VOICE_CALL, false)){
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE_CALL : MESSAGE_TYPE_SENT_VOICE_CALL;
                }else if (message.getBooleanAttribute(Constants.MESSAGE_ATTR_IS_VIDEO_CALL, false)){
                    //video call
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO_CALL : MESSAGE_TYPE_SENT_VIDEO_CALL;
                }
                 //messagee recall
                else if(message.getBooleanAttribute(Constants.MESSAGE_TYPE_RECALL, false)){
                    return MESSAGE_TYPE_RECALL;
                }
            }
            return 0;
        }

        @Override
        public EaseChatRowPresenter getCustomChatRow(EMMessage message, int position, BaseAdapter adapter) {
            if(message.getType() == EMMessage.Type.TXT){
//                // voice call or video call
//                if (message.getBooleanAttribute(Constants.MESSAGE_ATTR_IS_VOICE_CALL, false) ||
//                    message.getBooleanAttribute(Constants.MESSAGE_ATTR_IS_VIDEO_CALL, false)){
//                    EaseChatRowPresenter presenter = new EaseChatVoiceCallPresenter();
//                    return presenter;
//                }
//                //recall message
//                else if(message.getBooleanAttribute(Constants.MESSAGE_TYPE_RECALL, false)){
//                    EaseChatRowPresenter presenter = new EaseChatRecallPresenter();
//                    return presenter;
//                }
            }
            return null;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
