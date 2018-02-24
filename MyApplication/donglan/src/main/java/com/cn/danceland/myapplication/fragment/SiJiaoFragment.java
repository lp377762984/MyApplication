package com.cn.danceland.myapplication.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.activity.SiJiaoDetailActivity;
import com.cn.danceland.myapplication.utils.DensityUtils;
import com.cn.danceland.myapplication.utils.MyListView;
import com.cn.danceland.myapplication.utils.NestedExpandaleListView;

/**
 * Created by feng on 2018/1/12.
 */

public class SiJiaoFragment extends BaseFragment {

    NestedExpandaleListView ex_lv;
    ImageView down_img,up_img;

    @Override
    public View initViews() {
        View inflate = View.inflate(mActivity, R.layout.sijiao, null);
        ex_lv = inflate.findViewById(R.id.ex_lv);
        ex_lv.setGroupIndicator(null);
        ex_lv.setAdapter(new MyExAdapter());
        ex_lv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
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


        return inflate;
    }

    @Override
    public void onClick(View v) {

    }

    private class MyExAdapter extends BaseExpandableListAdapter{


        @Override
        public int getGroupCount() {
            return 5;
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
            if(convertView == null){
                convertView = LayoutInflater.from(mActivity).inflate(R.layout.sijiao_parent_item, null);
            }
            RelativeLayout sijiao_yuyue = convertView.findViewById(R.id.sijiao_yuyue);
            sijiao_yuyue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(mActivity, SiJiaoDetailActivity.class));
                }
            });

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            ViewHolder viewHolder=null;
            if (convertView==null){
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mActivity).inflate(R.layout.sijiao_child_item,null);
                viewHolder.mylist = convertView.findViewById(R.id.mylist);

                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.mylist.setDividerHeight(0);
            viewHolder.mylist.setAdapter(new MyListAdapter());

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }

     class ViewHolder{
        MyListView mylist;
    }

    private class MyListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 3;
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
            if(convertView==null){
                convertView = LayoutInflater.from(mActivity).inflate(R.layout.sijiao_list_item, null);
            }


            return convertView;
        }
    }
}
