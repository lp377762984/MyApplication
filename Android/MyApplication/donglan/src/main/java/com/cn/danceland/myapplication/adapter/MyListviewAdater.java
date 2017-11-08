package com.cn.danceland.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.SuperKotlin.pictureviewer.ImagePagerActivity;
import com.SuperKotlin.pictureviewer.PictureConfig;
import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.activity.UserHomeActivity;
import com.cn.danceland.myapplication.bean.PullBean;
import com.cn.danceland.myapplication.view.NoScrollGridView;

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
            viewHolder.gridView = convertView.findViewById(R.id.gridview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_pick_name.setText(data.get(position).getTitle());
        viewHolder.tv_time.setText(data.get(position).getContent());
        Glide.with(context)
                .load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509706698422&di=92a343f766c683c086be21e2ff0eea66&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3Ddfed83e2eafe9925df0161135cc134aa%2Fbd315c6034a85edf6875650443540923dd547518.jpg")
                .into(viewHolder.iv_avatar);
        viewHolder.iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, UserHomeActivity.class));
            }
        });


        final List<String> list = new ArrayList<String>();
     //   list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509529741192&di=8798f823cde65a24788bc7741e0f8956&imgtype=0&src=http%3A%2F%2Fb.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F500fd9f9d72a6059341ae1ae2234349b033bba7f.jpg");
       // list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509529741192&di=fb637042af5878b887969222e1f55975&imgtype=0&src=http%3A%2F%2Fg.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F9a504fc2d5628535d3baa0959aef76c6a7ef632f.jpg");
       // list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509599618510&di=a285851c997adfb117b8992144b8c215&imgtype=0&src=http%3A%2F%2Fwww.pp3.cn%2Fuploads%2F201410%2F2014102406.jpg");
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509706698422&di=9ced5f6e12498585405213597c31768f&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3Db3ac16dadf62853586edda62f8861cb3%2Fe4dde71190ef76c68f1397039716fdfaae5167d0.jpg");
       list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509706698421&di=0b400f04ef15b2cd602065989845e293&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F8b82b9014a90f603054b2c623312b31bb051ed18.jpg");
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509706698418&di=4e6175c3f0ec0b3579106647ff179032&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3D14eb806481d4b31ce4319cf8efbf4d0a%2F8601a18b87d6277fc029a91f22381f30e924fcda.jpg");

        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509706698422&di=9ced5f6e12498585405213597c31768f&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3Db3ac16dadf62853586edda62f8861cb3%2Fe4dde71190ef76c68f1397039716fdfaae5167d0.jpg");
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509706698421&di=0b400f04ef15b2cd602065989845e293&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F8b82b9014a90f603054b2c623312b31bb051ed18.jpg");
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509706698418&di=4e6175c3f0ec0b3579106647ff179032&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3D14eb806481d4b31ce4319cf8efbf4d0a%2F8601a18b87d6277fc029a91f22381f30e924fcda.jpg");

        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509706698422&di=9ced5f6e12498585405213597c31768f&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3Db3ac16dadf62853586edda62f8861cb3%2Fe4dde71190ef76c68f1397039716fdfaae5167d0.jpg");
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509706698421&di=0b400f04ef15b2cd602065989845e293&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F8b82b9014a90f603054b2c623312b31bb051ed18.jpg");
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509706698418&di=4e6175c3f0ec0b3579106647ff179032&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3D14eb806481d4b31ce4319cf8efbf4d0a%2F8601a18b87d6277fc029a91f22381f30e924fcda.jpg");

        viewHolder.gridView.setAdapter(new ImageGridAdapter(context, list));
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
                        .setListData((ArrayList<String>) list)//图片数据List<String> list
                        .setPosition(i)//图片下标（从第position张图片开始浏览）
                        .setDownloadPath("donglan")//图片下载文件夹地址
                        .setIsShowNumber(true)//是否显示数字下标
                        .needDownload(true)//是否支持图片下载
                        .setPlacrHolder(R.drawable.img_loading)//占位符图片（图片加载完成前显示的资源图片，来源drawable或者mipmap）
                        .build();
                ImagePagerActivity.startActivity(context, config);


            }
        });


        return convertView;
    }

    class ViewHolder {
        TextView tv_pick_name;
        TextView tv_time;
        ImageView iv_avatar;
        NoScrollGridView gridView;
    }
}
