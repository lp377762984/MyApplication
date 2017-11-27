package com.cn.danceland.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.SuperKotlin.pictureviewer.ImagePagerActivity;
import com.SuperKotlin.pictureviewer.PictureConfig;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.activity.DynHomeActivity;
import com.cn.danceland.myapplication.activity.UserHomeActivity;
import com.cn.danceland.myapplication.bean.RequestInfoBean;
import com.cn.danceland.myapplication.bean.RequsetDynInfoBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.cn.danceland.myapplication.view.NoScrollGridView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

import static com.cn.danceland.myapplication.R.id.iv_comment;
import static com.cn.danceland.myapplication.R.id.iv_transpond;
import static com.cn.danceland.myapplication.R.id.iv_zan;
import static com.cn.danceland.myapplication.R.id.tv_guanzhu;

/**
 * Created by shy on 2017/10/24 17:40
 * Email:644563767@qq.com
 */


public class MyDynListviewAdater extends BaseAdapter {
    private List<RequsetDynInfoBean.Data.Items> data = new ArrayList<RequsetDynInfoBean.Data.Items>();
    private LayoutInflater mInflater;
    private Context context;
    boolean isMe = false;

    public MyDynListviewAdater(Context context, ArrayList<RequsetDynInfoBean.Data.Items> data) {
        // TODO Auto-generated constructor stub
        mInflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }


    public void addFirst(RequsetDynInfoBean.Data.Items bean) {
        data.add(0, bean);
    }

    public void addFirstList(ArrayList<RequsetDynInfoBean.Data.Items> bean) {
        data.addAll(0, bean);
    }

    public void addLast(RequsetDynInfoBean.Data.Items bean) {
        data.add(bean);
    }

    public void addLastList(ArrayList<RequsetDynInfoBean.Data.Items> bean) {
        data.addAll(bean);
    }


    public void setData(ArrayList<RequsetDynInfoBean.Data.Items> bean) {

        data = bean;
    }

    /**
     *
     */
    public void setGzType(boolean isMe) {
        this.isMe = isMe;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        // LogUtil.i(data.size() + "");
//        if (data.size() == 0) {
//            return 1;
//        } else {
            return data.size();
    //    }


    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        LogUtil.i("data.size()"+data.size()+"+++++++++++");
//        LogUtil.i(data.toString());
        //如果没有数据


        // TODO Auto-generated method stub
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.listview_item_dynamic, null);
            viewHolder.tv_nick_name = (TextView) convertView.findViewById(R.id.tv_nick_name);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.tv_guanzhu = (TextView) convertView.findViewById(tv_guanzhu);
            viewHolder.tv_location = convertView.findViewById(R.id.tv_location);
            viewHolder.ll_location = convertView.findViewById(R.id.ll_location);
            viewHolder.tv_content = convertView.findViewById(R.id.tv_content);
            viewHolder.tv_zan_num = convertView.findViewById(R.id.tv_zan_num);
            viewHolder.iv_avatar = convertView.findViewById(R.id.iv_avatar);
            viewHolder.iv_zan = convertView.findViewById(iv_zan);
            viewHolder.iv_comment = convertView.findViewById(iv_comment);
            viewHolder.iv_transpond = convertView.findViewById(iv_transpond);
            viewHolder.gridView = convertView.findViewById(R.id.gridview);
            viewHolder.jzVideoPlayer = convertView.findViewById(R.id.videoplayer);
            viewHolder.ll_item = convertView.findViewById(R.id.ll_item);
            viewHolder.tv_pinglun = convertView.findViewById(R.id.tv_pinglun);
          //  viewHolder.tv_no_data = convertView.findViewById(R.id.tv_no_data);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (data == null) {
            return convertView;
        }

//        if (data.size() == 0) {
//            viewHolder.ll_item.setVisibility(View.GONE);
//            viewHolder.tv_no_data.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.ll_item.setVisibility(View.VISIBLE);
//            viewHolder.tv_no_data.setVisibility(View.GONE);
//        }

       // LogUtil.i(data.size() + "");
        viewHolder.ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//点击整个页面
                // ToastUtils.showToastShort(position + "");
                context.startActivity(new Intent(context, DynHomeActivity.class).putExtra("msgId", data.get(position).getId()).putExtra("userId", data.get(position).getAuthor()));

            }
        });
        //设置评论数量
        viewHolder.tv_pinglun.setText(data.get(position).getReplyNumber() + "");
        //设置点赞数量
        viewHolder.tv_zan_num.setText(data.get(position).getPriaseNumber() + "");

        if (data.get(position).isPraise()) {//设置点赞
            viewHolder.iv_zan.setImageResource(R.drawable.img_xin1);
        } else {
            viewHolder.iv_zan.setImageResource(R.drawable.img_xin);
        }

        viewHolder.iv_zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点赞

                if (data.get(position).isPraise()) {//已点赞

                    int pos = position;
                    addZan(data.get(position).getId(), false, pos);


                } else {//未点赞
                    int pos = position;
                    addZan(data.get(position).getId(), true, pos);


                }

                notifyDataSetChanged();

            }
        });

        viewHolder.iv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           //     ToastUtils.showToastShort("评论");
                context.startActivity(new Intent(context, DynHomeActivity.class).putExtra("msgId", data.get(position).getId()).putExtra("userId", data.get(position).getAuthor()));

            }
        });
        viewHolder.iv_transpond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showToastShort("转发");
            }
        });

        if (isMe) {//是否是个人页面
            viewHolder.tv_guanzhu.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.tv_guanzhu.setVisibility(View.VISIBLE);
        }


        if (data.get(position).isFollower()) {
            viewHolder.tv_guanzhu.setText("已关注");
            viewHolder.tv_guanzhu.setTextColor(Color.GRAY);
        } else {
            viewHolder.tv_guanzhu.setText("+关注");
        }
        viewHolder.tv_guanzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!data.get(position).isFollower()) {//未关注添加关注
                    int pos = position;
                    addGuanzhu(data.get(position).getAuthor(), true, pos);
                }


            }
        });


        //  LogUtil.i(data.get(position).getNickName());
        if (!TextUtils.isEmpty(data.get(position).getNickName())) {
            viewHolder.tv_nick_name.setText(data.get(position).getNickName());
        }


        viewHolder.tv_time.setText(data.get(position).getPublishTime());
        if (TextUtils.isEmpty(data.get(position).getContent())) {
            viewHolder.tv_content.setVisibility(View.GONE);
        } else {//内容不为空赋值
            viewHolder.tv_content.setText(data.get(position).getContent());
            viewHolder.tv_content.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(data.get(position).getPublishPlace())) {
            viewHolder.ll_location.setVisibility(View.GONE);
        } else {//地点不为空赋值
            viewHolder.ll_location.setVisibility(View.VISIBLE);
            viewHolder.tv_location.setText(data.get(position).getPublishPlace());
        }


        RequestOptions options = new RequestOptions().placeholder(R.drawable.img_my_avatar);
        Glide.with(context)
                .load(data.get(position).getSelfUrl())
                .apply(options)
                .into(viewHolder.iv_avatar);
        viewHolder.iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, UserHomeActivity.class).putExtra("id", data.get(position).getAuthor()));
            }
        });

        if (data.get(position).getVedioUrl() != null && data.get(position).getMsgType() == 1) {//如果是视频消息
            viewHolder.jzVideoPlayer.setVisibility(View.VISIBLE);

            viewHolder.jzVideoPlayer.setUp(
                    data.get(position).getVedioUrl(), JZVideoPlayer.SCREEN_WINDOW_LIST,
                    "");
            Glide.with(convertView.getContext())
                    .load(data.get(position).getVedioImg())
                    .into(viewHolder.jzVideoPlayer.thumbImageView);
            viewHolder.jzVideoPlayer.positionInList = position;
        } else {
            viewHolder.jzVideoPlayer.setVisibility(View.GONE);
        }

        if (data.get(position).getImgList() != null && data.get(position).getImgList().size() > 0) {

            viewHolder.gridView.setVisibility(View.VISIBLE);
            viewHolder.gridView.setAdapter(new ImageGridAdapter(context, data.get(position).getImgList()));
            /**
             * 图片列表点击事件
             */
            viewHolder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


//                Intent intent = new Intent(context, ImagePagerActivity.class);
//                //intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, (Parcelable) list);
//                intent.putStringArrayListExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, (ArrayList<String>) list);
//                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, i);
//                context.startActivity(intent);

                    PictureConfig config = new PictureConfig.Builder()
                            .setListData((ArrayList<String>) data.get(position).getImgList())//图片数据List<String> list
                            .setPosition(i)//图片下标（从第position张图片开始浏览）
                            .setDownloadPath("donglan")//图片下载文件夹地址
                            .setIsShowNumber(true)//是否显示数字下标
                            .needDownload(true)//是否支持图片下载
                            .setPlacrHolder(R.drawable.img_loading)//占位符图片（图片加载完成前显示的资源图片，来源drawable或者mipmap）
                            .build();
                    ImagePagerActivity.startActivity(context, config);


                }
            });
        } else {
            viewHolder.gridView.setVisibility(View.GONE);


        }


        return convertView;


    }

    class ViewHolder {
        TextView tv_nick_name;
        TextView tv_time;
        TextView tv_content;
        TextView tv_location;//地点
        TextView tv_zan_num;//点赞数量
        TextView tv_guanzhu;
      //  TextView tv_no_data;
        ImageView iv_avatar;
        ImageView iv_zan;//点赞
        ImageView iv_transpond;//转发
        ImageView iv_comment;//评论
        LinearLayout ll_location;
        NoScrollGridView gridView;
        JZVideoPlayerStandard jzVideoPlayer;
        LinearLayout ll_item;
        TextView tv_pinglun;//评论数


    }


    /**
     * 点赞
     *
     * @param isPraise
     * @param isPraise
     */
    private void addZan(final String msgId, final boolean isPraise, final int pos) {


        StringRequest request = new StringRequest(Request.Method.POST, Constants.ADD_ZAN_URL, new Response.Listener<String>() {


            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                Gson gson = new Gson();
                RequestInfoBean requestInfoBean = new RequestInfoBean();
                requestInfoBean = gson.fromJson(s, RequestInfoBean.class);

                if (requestInfoBean.getSuccess()) {

                    if (data.get(pos).isPraise()) {//如果已点赞
                        data.get(pos).setPraise(false);
                        int i = data.get(pos).getPriaseNumber() - 1;
                        data.get(pos).setPriaseNumber(i);
                        ToastUtils.showToastShort("取消点赞成功");
                    } else {
                        data.get(pos).setPraise(true);
                        int i = data.get(pos).getPriaseNumber() + 1;
                        data.get(pos).setPriaseNumber(i);
                        ToastUtils.showToastShort("点赞成功");
                    }


                    notifyDataSetChanged();

                } else {
                    ToastUtils.showToastShort("点赞失败");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                LogUtil.i(volleyError.toString());

            }

        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("msgId", msgId);
                map.put("id", SPUtils.getString(Constants.MY_USERID, null));
                map.put("isPraise", String.valueOf(isPraise));
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, null));
                // LogUtil.i("Bearer+"+SPUtils.getString(Constants.MY_TOKEN,null));
                //       LogUtil.i(SPUtils.getString(Constants.MY_TOKEN, null));
                return map;
            }
        };
        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("addGuanzhu");
        // 设置超时时间
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);

    }


    /**
     * 加关注
     *
     * @param id
     * @param b
     */
    private void addGuanzhu(final String id, final boolean b, final int pos) {

        StringRequest request = new StringRequest(Request.Method.POST, Constants.ADD_GUANZHU, new Response.Listener<String>() {


            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                Gson gson = new Gson();
                RequestInfoBean requestInfoBean = new RequestInfoBean();
                requestInfoBean = gson.fromJson(s, RequestInfoBean.class);
                if (requestInfoBean.getSuccess()) {
                    data.get(pos).setFollower(true);
                    notifyDataSetChanged();

                    ToastUtils.showToastShort("关注成功");
                } else {
                    ToastUtils.showToastShort("关注失败");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                LogUtil.i(volleyError.toString());
                ToastUtils.showToastShort("请查看网络连接");
            }

        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("userId", id);
                map.put("isFollower", String.valueOf(b));
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, null));
                // LogUtil.i("Bearer+"+SPUtils.getString(Constants.MY_TOKEN,null));
                LogUtil.i(SPUtils.getString(Constants.MY_TOKEN, null));
                return map;
            }
        };
        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("addGuanzhu");
        // 设置超时时间
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);

    }


}
