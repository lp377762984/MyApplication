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
import com.cn.danceland.myapplication.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by shy on 2017/11/21 09:36
 * Email:644563767@qq.com
 */


public class UserListviewAdapter extends BaseAdapter {
    private List<RequsetUserListBean.Data.Content> data = new ArrayList<RequsetUserListBean.Data.Content>();
    private LayoutInflater mInflater;
    private Context context;
    private int type = 1;//等于3是点赞，默认是1

    public UserListviewAdapter(Context context, List<RequsetUserListBean.Data.Content> data, int type) {
        mInflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
        this.type = type;
    }

    public void addLastList(ArrayList<RequsetUserListBean.Data.Content> bean) {
        this.data.addAll(bean);
    }

    public void addLastList(ArrayList<RequsetUserListBean.Data.Content> bean, int type) {
        this.type = type;
        this.data.addAll(bean);
    }

//    public void setData(ArrayList<RequsetUserListBean.Data.Content> bean) {
//
//        this.data = bean;
//    }

    public void setData(ArrayList<RequsetUserListBean.Data.Content> bean, int type) {
        this.type = type;
        this.data = bean;

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
    public View getView(final int position, View view, ViewGroup viewGroup) {

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
        LogUtil.i(type+"");
        if (type==3){
            Glide.with(context)
                    .load(data.get(position).getSelf_url())
                    .apply(options)
                    .into(viewHolder.iv_avatar);
            LogUtil.i(data.get(position).getSelf_url());

        }else {
            Glide.with(context)
                    .load(data.get(position).getSelf_path())
                    .apply(options)
                    .into(viewHolder.iv_avatar);
            LogUtil.i(data.get(position).getSelf_path());

        }

        viewHolder.tv_nickname.setText(data.get(position).getNickName());
        // LogUtil.i(data.get(position).getNickName());
        if (data.get(position).getGender() == 1) {
            viewHolder.iv_sex.setImageResource(R.drawable.img_sex1);
        }
        if (data.get(position).getGender() == 2) {
            viewHolder.iv_sex.setImageResource(R.drawable.img_sex2);
        }
        viewHolder.ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // LogUtil.i("TYPE+"+type);
                switch (type) {
                    case 1://查看关注
                        context.startActivity(new Intent(context, UserHomeActivity.class).putExtra("id", data.get(position).getUserId()));
                        break;
                    case 2://查看粉丝
                        context.startActivity(new Intent(context, UserHomeActivity.class).putExtra("id", data.get(position).getFollower()));
                        break;
                    case 3://查看点赞
                        context.startActivity(new Intent(context, UserHomeActivity.class).putExtra("id", data.get(position).getPraiseUserId()));
                        break;
                    default:
                        break;
                }



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
