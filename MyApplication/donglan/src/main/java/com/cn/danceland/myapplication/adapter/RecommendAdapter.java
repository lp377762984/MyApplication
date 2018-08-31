package com.cn.danceland.myapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.ConsultBean;
import com.cn.danceland.myapplication.utils.TimeUtils;

import java.util.List;

/**
 * 推荐列表adapter
 * Created by ${yxx} on 2018/8/30.
 */

public class RecommendAdapter extends BaseAdapter {

    private List<ConsultBean> datas;
    private Context context;

    public RecommendAdapter(Context context, List<ConsultBean> datas) {
        super();
        this.datas = datas;
        this.context = context;
    }

    public void setData(List<ConsultBean> datas) {
        this.datas = datas;
    }

    public void clear() {
        datas.clear();//先清除这个
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.listitem_recommend, null);
            viewHolder = new ViewHolder();
            viewHolder.name_tv = (TextView) view.findViewById(R.id.name_tv);
            viewHolder.sex_tv = (TextView) view.findViewById(R.id.sex_tv);
            viewHolder.tel_tv = (TextView) view.findViewById(R.id.tel_tv);
            viewHolder.time_tv = (TextView) view.findViewById(R.id.time_tv);
            viewHolder.btn_consult_state = (Button) view.findViewById(R.id.btn_consult_state);
            viewHolder.recommend_icon_image = (ImageView) view.findViewById(R.id.recommend_icon_image);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.name_tv.setText(datas.get(i).getCname() + "");
        viewHolder.sex_tv.setText(datas.get(i).getGender() + "");
        viewHolder.tel_tv.setText(datas.get(i).getPhone_no() + "");
        String time = TimeUtils.timeStamp2Date(TimeUtils.date2TimeStamp(datas.get(i).getCreate_time(), "yyyy-MM-dd HH:mm:ss") + "", "yyyy年MM月dd日");
        viewHolder.time_tv.setText(time);
        if (!TextUtils.isEmpty(datas.get(i).getType()))
            switch (datas.get(i).getType()) {
                case "1"://加盟
                    viewHolder.recommend_icon_image.setImageDrawable(context.getDrawable(R.drawable.img_join));
                    break;
                case "2"://培训
                    viewHolder.recommend_icon_image.setImageDrawable(context.getDrawable(R.drawable.img_train));
                    break;
                case "3"://购买
                    viewHolder.recommend_icon_image.setImageDrawable(context.getDrawable(R.drawable.img_buy));
                    break;
            }
        switch (datas.get(i).getStatus()) {
            case "0"://0=推广中
                viewHolder.btn_consult_state.setText(context.getResources().getString(R.string.recommend_state_in_text));
                viewHolder.btn_consult_state.setBackground(context.getResources().getDrawable(R.drawable.btn_bg_square_blue));
                break;
            case "1"://1 = 完成
                viewHolder.btn_consult_state.setText(context.getResources().getString(R.string.recommend_state_succeed_text));
                viewHolder.btn_consult_state.setBackground(context.getResources().getDrawable(R.drawable.btn_bg_blue_square_deep));
                break;
            case "2"://2 = 失败
                viewHolder.btn_consult_state.setText(context.getResources().getString(R.string.recommend_state_fails_text));
                viewHolder.btn_consult_state.setBackground(context.getResources().getDrawable(R.drawable.btn_bg_square_gary));
                break;
        }
        return view;
    }

    static class ViewHolder {
        TextView name_tv;
        TextView sex_tv;
        TextView tel_tv;
        TextView time_tv;
        Button btn_consult_state;
        ImageView recommend_icon_image;
    }
}
