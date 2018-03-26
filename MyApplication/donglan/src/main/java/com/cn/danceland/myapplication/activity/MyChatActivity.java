package com.cn.danceland.myapplication.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.fragment.MyChatFragment;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.PreferenceManager;

public class MyChatActivity extends AppCompatActivity {

    // 当前聊天的 ID
    private String mChatId;
    private MyChatFragment chatFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_chat);
        Data data= (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
        PreferenceManager.getInstance().setCurrentUserNick(data.getPerson().getNick_name());
        PreferenceManager.getInstance().setCurrentUserName(data.getPerson().getMember_no());
        PreferenceManager.getInstance().setCurrentUserAvatar(data.getPerson().getSelf_avatar_path());

        // 这里直接使用EaseUI封装好的聊天界面
        chatFragment = new MyChatFragment();
        // 将参数传递给聊天界面
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();

        initView();
    }

    /**
     * 初始化界面
     */
    private void initView() {

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
