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

import com.cn.danceland.myapplication.R;

/**
 * Created by feng on 2017/12/11.
 */

public class MessageActivity extends Activity{

    RelativeLayout zan_message;
    ListView lv_message;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message);
        initViews();
    }

    private void initViews() {
        zan_message = findViewById(R.id.zan_message);
        lv_message = findViewById(R.id.lv_message);
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
                convertView = View.inflate(MessageActivity.this,R.layout.message_item,null);


            }
            return null;
        }
    }

    private class ViewHolder{
        ImageView im_user;
        TextView tv_name,tv_content,tv_date;
    }
}
