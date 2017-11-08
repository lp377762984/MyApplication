package com.cn.danceland.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.activity.UserHomeActivity;

/**
 * Created by shy on 2017/10/26 09:44
 * Email:644563767@qq.com
 */

public class MyRecylerViewAdapter extends RecyclerView.Adapter<MyRecylerViewAdapter.ViewHolder> {
    public String[] datas = null;
    private Context context;

    public MyRecylerViewAdapter(Context context, String[] datas) {
        this.datas = datas;
        this.context = context;
    } 

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recylerview_item_recommend, viewGroup, false);

        ViewHolder vh = new ViewHolder(view);

        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.tv_pcikname.setText(datas[position]);
        viewHolder.iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, UserHomeActivity.class));
            }
        });
        Glide.with(context)
                .load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509706698422&di=92a343f766c683c086be21e2ff0eea66&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3Ddfed83e2eafe9925df0161135cc134aa%2Fbd315c6034a85edf6875650443540923dd547518.jpg")
                .into(viewHolder.iv_avatar);
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.length;
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_pcikname;
        public ImageView iv_avatar;

        public ViewHolder(View view) {
            super(view);
            tv_pcikname = (TextView) view.findViewById(R.id.tv_pcikname);
            iv_avatar = view.findViewById(R.id.iv_avatar);

        }
    }
}
