package com.cn.danceland.myapplication.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cn.danceland.myapplication.R;

/**
 * Created by shy on 2017/10/26 09:44
 * Email:644563767@qq.com
 */

    public class MyRecylerViewAdapter extends RecyclerView.Adapter<MyRecylerViewAdapter.ViewHolder> {
        public String[] datas = null;
        public MyRecylerViewAdapter(String[] datas) {
            this.datas = datas;
        }
        //创建新View，被LayoutManager所调用
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recylerview_item_recommend,viewGroup,false);
            ViewHolder vh = new ViewHolder(view);
            return vh;
        }
        //将数据与界面进行绑定的操作
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            viewHolder.tv_pcikname.setText(datas[position]);
        }
        //获取数据的数量
        @Override
        public int getItemCount() {
            return datas.length;
        }
        //自定义的ViewHolder，持有每个Item的的所有界面元素
        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_pcikname;
            public ViewHolder(View view){
                super(view);
                tv_pcikname = (TextView) view.findViewById(R.id.tv_pcikname);
            }
        }
    }

