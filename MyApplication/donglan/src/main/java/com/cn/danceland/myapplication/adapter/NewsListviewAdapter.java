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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.activity.NewsDetailsActivity;
import com.cn.danceland.myapplication.bean.RequestCollectBean;
import com.cn.danceland.myapplication.bean.RequestNewsDataBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.MyStringRequest;
import com.cn.danceland.myapplication.utils.TimeUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.cn.danceland.myapplication.view.adapter.CommonAdapter;
import com.cn.danceland.myapplication.view.adapter.ViewHolder;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
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
            ((Holder) holder).read_number_tv.setText(data.get(position).getRead_number());

            if(data.get(position).is_collect()){
                ((Holder) holder).collect_iv.setImageDrawable(context.getResources().getDrawable(R.drawable.home_item_on_collect_icon));
            }else{
                ((Holder) holder).collect_iv.setImageDrawable(context.getResources().getDrawable(R.drawable.home_item_collect_icon));
            }
            if (mListener == null) return;
            ((Holder) holder).ll_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(position, data);
                }
            });
            ((Holder) holder).collect_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyStringRequest request = new MyStringRequest(Request.Method.POST, Constants.PUSH_COLLECT_SAVE, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            LogUtil.i(s);

                            Gson gson = new Gson();
                            RequestCollectBean requestInfoBean = gson.fromJson(s, RequestCollectBean.class);
                            if (requestInfoBean.getSuccess() && requestInfoBean.getCode() == 0) {
                                data.get(position).setIs_collect(!data.get(position).is_collect());
                                if(data.get(position).is_collect()){
                                    ((Holder) holder).collect_iv.setImageDrawable(context.getResources().getDrawable(R.drawable.home_item_on_collect_icon));
                                }else{
                                    ((Holder) holder).collect_iv.setImageDrawable(context.getResources().getDrawable(R.drawable.home_item_collect_icon));
                                }
                            } else {
                                //失败
                                ToastUtils.showToastShort("请求失败，请查看网络连接");
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            ToastUtils.showToastShort("请求失败，请查看网络连接");
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> map = new HashMap<>();
                            map.put("news_id", data.get(position).getId());
                            map.put("type", "0");
                            map.put("is_collect", String.valueOf(!data.get(position).is_collect()));
                            LogUtil.i("map--" + map.toString());
                            return map;
                        }
                    };

                    // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
                    request.setTag("userRegister");
                    // 设置超时时间
                    request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    // 将请求加入全局队列中
                    MyApplication.getHttpQueues().add(request);
                }
            });
        }

    }
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
        ImageView collect_iv;//收藏
        TextView read_number_tv;//阅读数

        LinearLayout ll_item;

        public Holder(View itemView) {
            super(itemView);
            if (itemView == mHeaderView) return;
            tv_title = itemView.findViewById(R.id.tv_tiltle);
            iv_image = itemView.findViewById(R.id.iv_image);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_content = itemView.findViewById(R.id.tv_content);
            ll_item = itemView.findViewById(R.id.ll_item);
            collect_iv = itemView.findViewById(R.id.collect_iv);
            read_number_tv = itemView.findViewById(R.id.read_number_tv);
        }
    }

    public void setData(List<RequestNewsDataBean.Data.Items> data) {
        this.data = data;
    }

    //增加数据
    public void addLastList(List<RequestNewsDataBean.Data.Items> bean) {
        data.addAll(bean);
    }


    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return mHeaderView;
    }
}
