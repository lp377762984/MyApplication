package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.cn.danceland.myapplication.R;

/**
 * Created by shy on 2017/11/2 16:37
 * Email:644563767@qq.com
 */


public class SellCardActivity extends Activity {

    private String[] names = {"黄金年卡", "白金年卡", "钻石年卡"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_card);
        initView();
    }

    private void initView() {
        ListView listView = findViewById(R.id.listview);
        listView.setAdapter(new MyListAdapter());
    }

    public class MyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int i) {
            return names[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
           //         LayoutInflater.from(SellCardActivity.this).inflate( R.layout.listview_item_club_card, null);
            view =  LayoutInflater.from(SellCardActivity.this).inflate( R.layout.listview_item_club_card, null);


            return view;
        }
    }
}
