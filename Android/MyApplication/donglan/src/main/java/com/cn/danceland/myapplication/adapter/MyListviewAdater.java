package com.cn.danceland.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.activity.UserHomeActivity;
import com.cn.danceland.myapplication.bean.PullBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shy on 2017/10/24 17:40
 * Email:644563767@qq.com
 */


public class MyListviewAdater extends BaseAdapter {
    private List<PullBean> data = new ArrayList<PullBean>();

    private LayoutInflater mInflater;
    private Context context;

    public MyListviewAdater(Context context, ArrayList<PullBean> data) {
        // TODO Auto-generated constructor stub
        mInflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }


    public void addFirst(PullBean bean) {
        data.add(0, bean);
    }

    public void addFirstList(ArrayList<PullBean> bean) {
        data.addAll(0, bean);
    }

    public void addLast(PullBean bean) {
        data.add(bean);
    }

    public void addLastList(ArrayList<PullBean> bean) {
        data.addAll(bean);
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.listview_item_dynamic, null);
            viewHolder.tv_pick_name = (TextView) convertView.findViewById(R.id.tv_pick_name);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.iv_avatar = convertView.findViewById(R.id.iv_avatar);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_pick_name.setText(data.get(position).getTitle());
        viewHolder.tv_time.setText(data.get(position).getContent());
        Glide.with(context)
                .load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509534557271&di=c4a8f91b55493002326319ffb193f767&imgtype=0&src=http%3A%2F%2Fb.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F500fd9f9d72a6059341ae1ae2234349b033bba7f.jpg")
                .into(viewHolder.iv_avatar);
        viewHolder.iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, UserHomeActivity.class));
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tv_pick_name;
        TextView tv_time;
        ImageView iv_avatar;
    }
}
