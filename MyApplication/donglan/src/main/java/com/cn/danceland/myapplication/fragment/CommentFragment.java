package com.cn.danceland.myapplication.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ListView;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.adapter.MessageAdapter;
import com.cn.danceland.myapplication.db.DBData;
import com.cn.danceland.myapplication.db.MiMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by feng on 2017/12/22.
 */

public class CommentFragment extends BaseFragment {
    DBData dbData;
    ListView lv_message;
    List<MiMessage> messageList;
    Bundle arguments;
    String type;
    @Override
    public View initViews() {
        View v = View.inflate(mActivity, R.layout.fragment_comment,null);
        lv_message = v.findViewById(R.id.lv_message);
        arguments = getArguments();
        initData();
        return v;
    }

    private void initData() {
        dbData = new DBData();
        type = arguments.getString("type", null);
        //3表示的是评论类型的推送消息
        messageList = new ArrayList<MiMessage>();
        messageList = dbData.getMessageList(type);
        if(messageList!=null){
            lv_message.setAdapter(new MessageAdapter(messageList,mActivity));
        }
    }

    @Override
    public void onClick(View v) {

    }
}
