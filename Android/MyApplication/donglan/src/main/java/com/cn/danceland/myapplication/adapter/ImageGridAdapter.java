package com.cn.danceland.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.cn.danceland.myapplication.R;

import java.util.List;

/**
 * Created by Hankkin on 15/11/22.
 */
public class ImageGridAdapter extends BaseAdapter {

    private Context context;
    private List<String> imgUrls;
    private LayoutInflater inflater;

    public ImageGridAdapter(Context context, List<String> imgUrls) {
        this.context = context;
        this.imgUrls = imgUrls;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return imgUrls.size();
    }

    @Override
    public Object getItem(int i) {
        return imgUrls.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.gridview_item, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_content);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.img_loading)//加载占位图
                .error(R.drawable.img_del)//
                .priority(Priority.HIGH);

        Glide
                .with(context)
                .load(imgUrls.get(i))
                .apply(options)
                .into(imageView);

        return view;
    }
}
