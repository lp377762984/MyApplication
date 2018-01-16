package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.utils.CustomGridView;
import com.cn.danceland.myapplication.utils.NestedExpandaleListView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by feng on 2018/1/12.
 */

public class TuanKeDetailActivity extends Activity {
    ImageView kecheng_img,img_1,img_2,img_3,tuanke_back,down_img,up_img;
    NestedExpandaleListView kecheng_ex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tuankedetail);
        initView();
    }

    private void initView() {
        tuanke_back = findViewById(R.id.tuanke_back);
        tuanke_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        kecheng_img =  findViewById(R.id.kecheng_img);
        img_1 = findViewById(R.id.img_1);
        img_2 = findViewById(R.id.img_2);
        img_3 = findViewById(R.id.img_3);
        Glide.with(TuanKeDetailActivity.this).load("http://file06.16sucai.com/2016/0407/d3e6a989a0dc78464a96c0b506871705.jpg").into(kecheng_img);
        Glide.with(TuanKeDetailActivity.this).load("http://scimg.jb51.net/allimg/160829/103-160R91H143142.jpg").into(img_1);
        Glide.with(TuanKeDetailActivity.this).load("http://file06.16sucai.com/2016/0407/04a36c003aacf5654804b93df7c8db43.jpg").into(img_2);
        Glide.with(TuanKeDetailActivity.this).load("http://img.sc115.com/uploads/sc/jpgs/1412/apic7673_sc115.com.jpg").into(img_3);


        kecheng_ex = findViewById(R.id.kecheng_ex);
        kecheng_ex.setAdapter(new MyAdapter());
        kecheng_ex.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                down_img = v.findViewById(R.id.down_img);
                up_img = v.findViewById(R.id.up_img);
                if(down_img.getVisibility()==View.GONE){
                    down_img.setVisibility(View.VISIBLE);
                    up_img.setVisibility(View.GONE);
                }else{
                    down_img.setVisibility(View.GONE);
                    up_img.setVisibility(View.VISIBLE);
                }

                return false;
            }
        });

    }

    private class MyAdapter extends BaseExpandableListAdapter{

        @Override
        public int getGroupCount() {
            return 1;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            if(convertView==null){
                convertView = LayoutInflater.from(TuanKeDetailActivity.this).inflate(R.layout.kecheng_parent,null);
            }

            CircleImageView circle_1 = convertView.findViewById(R.id.circle_1);
            CircleImageView circle_2 = convertView.findViewById(R.id.circle_2);
            CircleImageView circle_3 = convertView.findViewById(R.id.circle_3);
            CircleImageView circle_4 = convertView.findViewById(R.id.circle_4);
            CircleImageView circle_5 = convertView.findViewById(R.id.circle_5);

            Glide.with(TuanKeDetailActivity.this).load("http://news.hainan.net/Editor/img/201602/20160215/big/20160215234302136_2731088.jpg").into(circle_1);
            Glide.with(TuanKeDetailActivity.this).load("http://img06.tooopen.com/images/20160807/tooopen_sy_174504721543.jpg").into(circle_2);
            Glide.with(TuanKeDetailActivity.this).load("http://file06.16sucai.com/2016/0407/90ed68d09c8777d6336862beca17f317.jpg").into(circle_3);
            Glide.with(TuanKeDetailActivity.this).load("http://img1.juimg.com/160622/330831-1606220TG086.jpg").into(circle_4);
            Glide.with(TuanKeDetailActivity.this).load("http://img.mp.itc.cn/upload/20160408/6c46c0a65f32450e9941f9ef84091104_th.jpg").into(circle_5);

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            if(convertView==null){
                convertView = LayoutInflater.from(TuanKeDetailActivity.this).inflate(R.layout.kecheng_child,null);
            }
            CustomGridView grid_view = convertView.findViewById(R.id.grid_view);
            grid_view.setAdapter(new MyGridAdapter());

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }


    private class MyGridAdapter extends BaseAdapter{

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
            View inflate = LayoutInflater.from(TuanKeDetailActivity.this).inflate(R.layout.kecheng_grid_item, null);
            CircleImageView circle_item = inflate.findViewById(R.id.circle_item);
            Glide.with(TuanKeDetailActivity.this).load("http://pic1.win4000.com/wallpaper/d/58997071ac2b1.jpg").into(circle_item);

            return inflate;
        }
    }

}
