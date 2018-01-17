package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.R;

/**
 * Created by feng on 2018/1/15.
 */

public class BuySiJiaoActivity extends Activity {
    ListView lv_sijiaocard;
    ImageView buy_img;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buysijiao);
        intiView();
    }

    private void intiView() {

        lv_sijiaocard = findViewById(R.id.lv_sijiaocard);
        lv_sijiaocard.setAdapter(new MyAdapter());
        lv_sijiaocard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(BuySiJiaoActivity.this,SellSiJiaoConfirmActivity.class));
                finish();
            }
        });

        buy_img = findViewById(R.id.buy_img);
        buy_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private class MyAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return 5;
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
            ViewHolder viewHolder;

            if(convertView==null){
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(BuySiJiaoActivity.this).inflate(R.layout.sijiaocard, null);
                viewHolder.card_img = convertView.findViewById(R.id.card_img);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)convertView.getTag();
            }
            Glide.with(BuySiJiaoActivity.this).load(R.drawable.sijiao_card).into(viewHolder.card_img);


            return convertView;
        }
    }

    class ViewHolder{
        ImageView card_img;

    }
}
