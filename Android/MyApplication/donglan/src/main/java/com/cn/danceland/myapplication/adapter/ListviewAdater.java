package com.cn.danceland.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.PullBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shy on 2017/10/24 17:40
 * Email:644563767@qq.com
 */


public class ListviewAdater extends BaseAdapter {
    private List<PullBean> data = new ArrayList<PullBean>();

    private LayoutInflater mInflater;

    public ListviewAdater(Context context, ArrayList<PullBean> data) {
        // TODO Auto-generated constructor stub
        mInflater = LayoutInflater.from(context);
        this.data = data;

    }

    public void addFirst(PullBean bean) {
        data.add(0, bean);
    }

    public void addLast(PullBean bean) {
        data.add(bean);
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

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_pick_name.setText(data.get(position).getTitle());
        viewHolder.tv_time.setText(data.get(position).getContent());

        return convertView;
    }

    class ViewHolder {
        TextView tv_pick_name;
        TextView tv_time;
    }
}
