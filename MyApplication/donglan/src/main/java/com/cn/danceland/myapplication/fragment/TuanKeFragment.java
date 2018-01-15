package com.cn.danceland.myapplication.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.activity.TuanKeDetailActivity;
import com.cn.danceland.myapplication.utils.ListViewUtil;
import com.cn.danceland.myapplication.utils.ToastUtils;

/**
 * Created by feng on 2018/1/11.
 */

public class TuanKeFragment extends BaseFragment {
    FragmentManager fragmentManager;
    ListView lv_tuanke;


    @Override
    public View initViews() {

        View view = View.inflate(mActivity, R.layout.tuanke, null);
        lv_tuanke = view.findViewById(R.id.lv_tuanke);
        lv_tuanke.setAdapter(new MyAdapter());
        lv_tuanke.setDividerHeight(0);
        ListViewUtil.setListViewHeightBasedOnChildren(lv_tuanke);
        lv_tuanke.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(mActivity, TuanKeDetailActivity.class));
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {

    }



    private class MyAdapter extends BaseAdapter{


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
            final ViewHolder viewHolder;
            if(convertView==null){
                viewHolder = new ViewHolder();
                convertView = View.inflate(mActivity,R.layout.tuanke_item,null);
                viewHolder.tuanke_img = convertView.findViewById(R.id.tuanke_img);
                viewHolder.tuanke_jibie = convertView.findViewById(R.id.tuanke_jibie);
                viewHolder.tuanke_leibie = convertView.findViewById(R.id.tuanke_leibie);
                viewHolder.tuanke_room = convertView.findViewById(R.id.tuanke_room);
                viewHolder.tuanke_name = convertView.findViewById(R.id.tuanke_name);
                viewHolder.tuanke_time = convertView.findViewById(R.id.tuanke_time);
                viewHolder.tuanke_yuyue = convertView.findViewById(R.id.tuanke_yuyue);
                viewHolder.tv_yuyue = convertView.findViewById(R.id.tv_yuyue);
                convertView.setTag(viewHolder);
            }else{
                viewHolder  = (ViewHolder) convertView.getTag();
            }

            viewHolder.tuanke_yuyue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.tuanke_yuyue.setBackgroundColor(Color.parseColor("#808080"));
                    viewHolder.tv_yuyue.setText("已预约");
                    viewHolder.tv_yuyue.setTextColor(Color.parseColor("#000000"));
                }
            });


            Glide.with(mActivity).load("http://img1.imgtn.bdimg.com/it/u=2023041661,3472950886&fm=27&gp=0.jpg").into(viewHolder.tuanke_img);

            return convertView;
        }
    }

    class ViewHolder{
        ImageView tuanke_img;
        TextView tuanke_time,tuanke_leibie,tuanke_jibie,tuanke_room,tuanke_name,tv_yuyue;
        RelativeLayout tuanke_yuyue;

    }
}
