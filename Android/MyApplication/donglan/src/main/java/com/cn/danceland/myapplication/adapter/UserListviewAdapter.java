package com.cn.danceland.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.activity.UserHomeActivity;
import com.cn.danceland.myapplication.bean.RequsetUserListBean;

import java.util.ArrayList;
import java.util.List;

import static com.SuperKotlin.pictureviewer.PictureConfig.position;

/**
 * Created by shy on 2017/11/21 09:36
 * Email:644563767@qq.com
 */


public class UserListviewAdapter extends BaseAdapter {
    private List<RequsetUserListBean.Data.Items> data = new ArrayList<RequsetUserListBean.Data.Items>();
    private LayoutInflater mInflater;
    private Context context;

    public UserListviewAdapter(Context context, List<RequsetUserListBean.Data.Items> data) {
        mInflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    public void addLastList(ArrayList<RequsetUserListBean.Data.Items> bean) {
        data.addAll(bean);
    }


    public void setData(ArrayList<RequsetUserListBean.Data.Items> bean) {

        data = bean;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.listview_item_user_list, null);
            viewHolder.iv_avatar = view.findViewById(R.id.iv_avatar);
            viewHolder.tv_nickname = view.findViewById(R.id.tv_nickname);
            viewHolder.iv_sex = view.findViewById(R.id.iv_sex);
            viewHolder.ll_item = view.findViewById(R.id.ll_item);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        RequestOptions options = new RequestOptions().placeholder(R.drawable.img_my_avatar);
        Glide.with(context)
                .load(data.get(position).getSelfUrl())
                .apply(options)
                .into(viewHolder.iv_avatar);
        viewHolder.tv_nickname.setText(data.get(position).getNickName());
        if (data.get(position).getGender() == 1) {
            viewHolder.iv_sex.setImageResource(R.drawable.img_sex1);
        }
        if (data.get(position).getGender() == 2) {
            viewHolder.iv_sex.setImageResource(R.drawable.img_sex2);
        }
        viewHolder.ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, UserHomeActivity.class).putExtra("id",data.get(position).getUserId()));
               // context.startActivity(new Intent(context, UserHomeActivity.class).putExtra("id", data.get(position).getAuthor()));
            }
        });


        return view;
    }

    class ViewHolder {
        TextView tv_nickname;//昵称
        ImageView iv_avatar;//头像
        ImageView iv_sex;//性别
        LinearLayout ll_item;
    }
}
