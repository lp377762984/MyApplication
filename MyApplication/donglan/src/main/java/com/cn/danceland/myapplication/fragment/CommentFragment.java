package com.cn.danceland.myapplication.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

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
    TextView tv_no;
    @Override
    public View initViews() {
        View v = View.inflate(mActivity, R.layout.fragment_comment,null);
        lv_message = v.findViewById(R.id.lv_message);
        tv_no = v.findViewById(R.id.tv_no);
        arguments = getArguments();
        initData();
        return v;
    }

    private void initData() {
        dbData = new DBData();
        type = arguments.getString("type", null);
        //3表示的是评论类型的推送消息
        messageList = new ArrayList<MiMessage>();
        messageList = dbData.getMessageList();
        if(messageList!=null&&messageList.size()>0){
            if("4".equals(type)){
                tv_no.setVisibility(View.VISIBLE);
                lv_message.setVisibility(View.GONE);
            }else{
                tv_no.setVisibility(View.GONE);
                lv_message.setAdapter(new MessageAdapter(messageList,mActivity));
            }
        }else{
            tv_no.setVisibility(View.VISIBLE);
            lv_message.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {

    }
}
