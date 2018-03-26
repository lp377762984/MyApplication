package com.cn.danceland.myapplication.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.activity.MyChatActivity;
import com.cn.danceland.myapplication.utils.Constants;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

/**
 * Created by shy on 2018/3/9 11:38
 * Email:644563767@qq.com
 */


public class MyConversationListFragment extends EaseConversationListFragment {
    private TextView errorText;

    @Override
    protected void initView() {
        super.initView();
//        View errorView = (LinearLayout) View.inflate(getActivity(), R.layout.em_chat_neterror_item, null);
//        errorItemContainer.addView(errorView);
//        errorText = (TextView) errorView.findViewById(R.id.tv_connect_errormsg);
    }

    @Override
    protected void setUpView() {
        super.setUpView();
        // register context menu
        //隐藏标题栏
        hideTitleBar();

        registerForContextMenu(conversationListView);
        conversationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMConversation conversation = conversationListView.getItem(position);
                String username = conversation.conversationId();
                if (username.equals(EMClient.getInstance().getCurrentUser()))
                    Toast.makeText(getActivity(), R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
                else {
                    // start chat acitivity
                    Intent intent = new Intent(getActivity(), MyChatActivity.class);
                    intent.putExtra(Constants.EXTRA_CHAT_TYPE, Constants.CHATTYPE_SINGLE);
                    if(conversation.isGroup()){
                        if(conversation.getType() == EMConversation.EMConversationType.ChatRoom){
                            // it's group chat
                            intent.putExtra(Constants.EXTRA_CHAT_TYPE, Constants.CHATTYPE_CHATROOM);
                        }else{
                            intent.putExtra(Constants.EXTRA_CHAT_TYPE, Constants.CHATTYPE_GROUP);
                        }

                    }
                    // it's single chat

                    intent.putExtra(Constants.EXTRA_USER_ID, username);
                    startActivity(intent);
                }
            }
        });
        super.setUpView();
    }

    @Override
    protected void onConnectionDisconnected() {
        super.onConnectionDisconnected();
//        if (NetUtils.hasNetwork(getActivity())){
//            errorText.setText("连接不到聊天服务器");
//        } else {
//            errorText.setText(R.string.the_current_network);
//        }
    }


//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        getActivity().getMenuInflater().inflate(R.menu.em_delete_message, menu);
//    }
//
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        boolean deleteMessage = false;
//        if (item.getItemId() == R.id.delete_message) {
//            deleteMessage = true;
//        } else if (item.getItemId() == R.id.delete_conversation) {
//            deleteMessage = false;
//        }
//        EMConversation tobeDeleteCons = conversationListView.getItem(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
//        if (tobeDeleteCons == null) {
//            return true;
//        }
//        if(tobeDeleteCons.getType() == EMConversationType.GroupChat){
//            EaseAtMessageHelper.get().removeAtMeGroup(tobeDeleteCons.conversationId());
//        }
//        try {
//            // delete conversation
//            EMClient.getInstance().chatManager().deleteConversation(tobeDeleteCons.conversationId(), deleteMessage);
//            InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
//            inviteMessgeDao.deleteMessage(tobeDeleteCons.conversationId());
//            // To delete the native stored adked users in this conversation.
//            if (deleteMessage) {
//                EaseDingMessageHelper.get().delete(tobeDeleteCons);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        refresh();
//
//        // update unread count
//        ((MainActivity) getActivity()).updateUnreadLabel();
//        return true;
//    }
}
