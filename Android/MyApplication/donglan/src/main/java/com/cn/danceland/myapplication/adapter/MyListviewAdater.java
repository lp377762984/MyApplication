package com.cn.danceland.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.activity.UserHomeActivity;
import com.cn.danceland.myapplication.bean.RequsetDynInfoBean;
import com.cn.danceland.myapplication.view.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

import static com.cn.danceland.myapplication.R.id.tv_guanzhu;

/**
 * Created by shy on 2017/10/24 17:40
 * Email:644563767@qq.com
 */


public class MyListviewAdater extends BaseAdapter {
    //   private List<PullBean> data = new ArrayList<PullBean>();
    private List<RequsetDynInfoBean.Data.Items> data = new ArrayList<RequsetDynInfoBean.Data.Items>();
    private LayoutInflater mInflater;
    private Context context;
    boolean isMe = false;

    public MyListviewAdater(Context context, ArrayList<RequsetDynInfoBean.Data.Items> data) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
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
            viewHolder.iv_zan = convertView.findViewById(R.id.iv_zan);
            viewHolder.gridView = convertView.findViewById(R.id.gridview);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (data == null) {
            return convertView;
        }

        viewHolder.tv_zan_num.setText(data.get(position).getFollowerNumber() + "");
        viewHolder.iv_zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int i = data.get(position).getFollowerNumber() + 1;
                data.get(position).setFollowerNumber(i);
                notifyDataSetChanged();

            }
        });

        if (isMe) {
            viewHolder.tv_guanzhu.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.tv_guanzhu.setVisibility(View.VISIBLE);
        }

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
        ImageView iv_avatar;
        ImageView iv_zan;
        LinearLayout ll_location;
        NoScrollGridView gridView;
    }
}
