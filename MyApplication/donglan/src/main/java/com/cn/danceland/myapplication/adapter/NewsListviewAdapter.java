package com.cn.danceland.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.activity.NewsDetailsActivity;
import com.cn.danceland.myapplication.bean.RequestNewsDataBean;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.TimeUtils;
import com.cn.danceland.myapplication.view.adapter.CommonAdapter;
import com.cn.danceland.myapplication.view.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by shy on 2017/11/21 09:36
 * Email:644563767@qq.com
 */

public class NewsListviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;

    private List<RequestNewsDataBean.Data.Items> data = new ArrayList<RequestNewsDataBean.Data.Items>();
    private LayoutInflater mInflater;
    private Context context;

    private View mHeaderView;

    private OnItemClickListener mListener;

    public NewsListviewAdapter(List<RequestNewsDataBean.Data.Items> data, Context context) {
        this.data = data;
        this.context = context;

        mInflater = LayoutInflater.from(context);

    }

    public void setOnItemClickListener(OnItemClickListener li) {
        mListener = li;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, List<RequestNewsDataBean.Data.Items> data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        ViewHolder viewHolder = ViewHolder.get(context, parent, R.layout.listview_item_homepage);
//        return viewHolder;

        if (mHeaderView != null && viewType == TYPE_HEADER)
            return new Holder(mHeaderView);

        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_homepage, parent, false);
        return new Holder(layout);
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null) return TYPE_NORMAL;
        if (position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == TYPE_HEADER) return;

        final int pos = getRealPosition(holder);
        if (holder instanceof Holder) {
            Glide.with(context)
                    .load(data.get(position).getImg_url())
                    //  .apply(options)
                    .into(((Holder) holder).iv_image);
            ((Holder) holder).tv_title.setText(data.get(position).getTitle());
            ((Holder) holder).tv_time.setText(TimeUtils.timeStamp2Date(TimeUtils.date2TimeStamp(data.get(position).getPublish_time(), "yyyy-MM-dd HH:mm:ss").toString(), "yyyy.MM.dd"));
            ((Holder) holder).tv_content.setText(data.get(position).getNews_txt());

            if (mListener == null) return;
            ((Holder) holder).ll_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    mListener.onItemClick(position, data);
                }
            });
        }

    }
//    @Override
//    public void onBindViewHolder(ViewHolder holder, final int position) {
//
//        MyViewHolder viewHolder = new MyViewHolder();
//        viewHolder.iv_image = holder.getView(R.id.iv_image);
//        viewHolder.tv_title = holder.getView(R.id.tv_tiltle);
//        viewHolder.tv_time = holder.getView(R.id.tv_time);
//        viewHolder.tv_content = holder.getView(R.id.tv_content);
//        viewHolder.ll_item = holder.getView(R.id.ll_item);
//
//        //  RequestOptions options = new RequestOptions().placeholder(R.drawable.img_my_avatar);
//        Glide.with(context)
//                .load(data.get(position).getImg_url())
//                //  .apply(options)
//                .into(viewHolder.iv_image);
//        viewHolder.tv_title.setText(data.get(position).getTitle());
//        viewHolder.tv_time.setText(data.get(position).getPublish_time());
//        viewHolder.tv_content.setText(data.get(position).getNews_txt());
//
//        viewHolder.ll_item.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                context.startActivity(new Intent(context, NewsDetailsActivity.class).putExtra("url", data.get(position).getUrl()).putExtra("title", data.get(position).getTitle()));
//
//            }
//        });
//    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    class Holder extends RecyclerView.ViewHolder {

        TextView tv_title;
        ImageView iv_image;//头像
        TextView tv_time;//时间
        TextView tv_content;//内容

        LinearLayout ll_item;

        public Holder(View itemView) {
            super(itemView);
            if (itemView == mHeaderView) return;
            tv_title = itemView.findViewById(R.id.tv_tiltle);
            iv_image = itemView.findViewById(R.id.iv_image);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_content = itemView.findViewById(R.id.tv_content);
            ll_item = itemView.findViewById(R.id.ll_item);
        }
    }
//    class Holder extends RecyclerView.ViewHolder {
//
//        TextView text;
//
//        public Holder(View itemView) {
//            super(itemView);
//            if (itemView == mHeaderView) return;
//            text = (TextView) itemView.findViewById(R.id.text);
//        }
//    }

    public void setData(List<RequestNewsDataBean.Data.Items> data) {
        this.data = data;
        //LogUtil.i(data.toString());
    }

    //增加数据
    public void addLastList(List<RequestNewsDataBean.Data.Items> bean) {
        data.addAll(bean);
        // LogUtil.i(data.toString());
    }

    class MyViewHolder {
        TextView tv_title;
        ImageView iv_image;//头像
        TextView tv_time;//时间
        TextView tv_content;//内容

        LinearLayout ll_item;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return mHeaderView;
    }
}
