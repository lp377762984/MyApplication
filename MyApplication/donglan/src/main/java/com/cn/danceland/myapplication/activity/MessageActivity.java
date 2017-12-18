package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.R;

/**
 * Created by feng on 2017/12/11.
 */

public class MessageActivity extends Activity{

    RelativeLayout zan_message;
    ListView lv_message;
    ImageView im_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message);
        initViews();
        setOnclick();
    }

    private void setOnclick() {
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lv_message.setAdapter(new MessageAdapter());
    }

    private void initViews() {
        zan_message = findViewById(R.id.zan_message);
        lv_message = findViewById(R.id.lv_message);
        im_back = findViewById(R.id.im_back);

    }

    private class MessageAdapter extends BaseAdapter{

        MessageAdapter(){

        }

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;

            if(convertView==null){
                viewHolder = new ViewHolder();
                convertView = View.inflate(MessageActivity.this,R.layout.message_item,null);
                viewHolder.im_user = convertView.findViewById(R.id.im_user);
                viewHolder.tv_name = convertView.findViewById(R.id.tv_name);
                viewHolder.tv_content = convertView.findViewById(R.id.tv_content);
                viewHolder.tv_date = convertView.findViewById(R.id.tv_date);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Glide.with(getApplicationContext()).load("http://e.hiphotos.baidu.com/image/pic/item/4e4a20a4462309f7b4fc96fc780e0cf3d6cad648.jpg").into(viewHolder.im_user);
            viewHolder.tv_name.setText("消息标题");
            viewHolder.tv_content.setText("消息内容");
            viewHolder.tv_date.setText("12-20");

            return convertView;
        }
    }

    private  class ViewHolder{
        ImageView im_user;
        TextView tv_name,tv_content,tv_date;
    }
}
