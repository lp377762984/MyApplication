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
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.activity.NewsDetailsActivity;
import com.cn.danceland.myapplication.bean.RequestNewsDataBean;
import com.cn.danceland.myapplication.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by shy on 2017/11/21 09:36
 * Email:644563767@qq.com
 */


public class NewsListviewAdapter2 extends BaseAdapter {

    private List<RequestNewsDataBean.Data.Items> data = new ArrayList<RequestNewsDataBean.Data.Items>();
    private LayoutInflater mInflater;
    private Context context;

    public NewsListviewAdapter2(List<RequestNewsDataBean.Data.Items> data, Context context) {
        this.data = data;
        this.context = context;

        mInflater = LayoutInflater.from(context);

    }

    public void addFirst(RequestNewsDataBean.Data.Items bean) {
        data.add(0, bean);
    }

    public void setData(List<RequestNewsDataBean.Data.Items> data) {
        this.data = data;
        //LogUtil.i(data.toString());
    }

    //增加数据
    public void addLastList(List<RequestNewsDataBean.Data.Items> bean) {
     //   LogUtil.i(data.toString());
        data.addAll(bean);
        // LogUtil.i(data.toString());
    }


    @Override
    public int getCount() {
     //   LogUtil.i(data.size()+"");
        return data.size();

      //

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

    //    LogUtil.i("getView"+position);
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.listview_item_homepage, null);
            viewHolder.iv_image = view.findViewById(R.id.iv_image);
            viewHolder.tv_title = view.findViewById(R.id.tv_tiltle);
            viewHolder.tv_time = view.findViewById(R.id.tv_time);
            viewHolder.tv_content = view.findViewById(R.id.tv_content);
            viewHolder.ll_item = view.findViewById(R.id.ll_item);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
      //  RequestOptions options = new RequestOptions().placeholder(R.drawable.img_my_avatar);
        Glide.with(context)
                .load(data.get(position).getImg_url())
              //  .apply(options)
                .into(viewHolder.iv_image);
        viewHolder.tv_title.setText(data.get(position).getTitle());
        viewHolder.tv_time.setText(TimeUtils.timeStamp2Date(TimeUtils.date2TimeStamp(data.get(position).getPublish_time(), "yyyy-MM-dd HH:mm:ss").toString(), "yyyy.MM.dd"));
        viewHolder.tv_content.setText(data.get(position).getNews_txt());

        viewHolder.ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, NewsDetailsActivity.class).putExtra("url",data.get(position).getUrl()).putExtra("title",data.get(position).getTitle()));

            }
        });
        return view;
    }

    class ViewHolder {
        TextView tv_title;
        ImageView iv_image;//头像
        TextView tv_time;//时间
        TextView tv_content;//内容

        LinearLayout ll_item;
    }


}
