package com.cn.danceland.myapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.ConsultBean;

import java.util.List;

/**
 * 咨询列表adapter
 * Created by ${yxx} on 2018/8/30.
 */

public class ConsultAdapter extends BaseAdapter {

    private List<ConsultBean> datas;
    private Context context;

    public ConsultAdapter(Context context, List<ConsultBean> datas) {
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
            view = LayoutInflater.from(context).inflate(R.layout.listitem_consult, null);
            viewHolder = new ViewHolder();
            viewHolder.consult_reply_tv = (TextView) view.findViewById(R.id.consult_reply_tv);
            viewHolder.first_reply_tv = (TextView) view.findViewById(R.id.first_reply_tv);
            viewHolder.btn_consult_state = (Button) view.findViewById(R.id.btn_consult_state);
            viewHolder.consult_icon_image = (ImageView) view.findViewById(R.id.consult_icon_image);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.consult_reply_tv.setText(datas.get(i).getCreate_time() + "");
//        viewHolder.first_reply_tv.setText(new SimpleDateFormat("HH:mm").format(new Date(datas.get(i).getEndTime())).toString());//暂时没有此处
        switch (datas.get(i).getType()) {
            case "1"://加盟
                viewHolder.consult_icon_image.setImageDrawable(context.getDrawable(R.drawable.img_join));
                break;
            case "2"://培训
                viewHolder.consult_icon_image.setImageDrawable(context.getDrawable(R.drawable.img_train));
                break;
            case "3"://购买
                viewHolder.consult_icon_image.setImageDrawable(context.getDrawable(R.drawable.img_buy));
                break;
        }
        switch (datas.get(i).getStatus()) {
            case "0"://0=咨询中
                viewHolder.btn_consult_state.setText(context.getResources().getString(R.string.consult_state_in_text));
                viewHolder.btn_consult_state.setBackground(context.getResources().getDrawable(R.drawable.btn_bg_square_blue));
                break;
            case "1"://1 = 完成
                viewHolder.btn_consult_state.setText(context.getResources().getString(R.string.consult_state_succeed_text));
                viewHolder.btn_consult_state.setBackground(context.getResources().getDrawable(R.drawable.btn_bg_blue_square_deep));
                break;
            case "2"://2 = 失败
                viewHolder.btn_consult_state.setText(context.getResources().getString(R.string.consult_state_fails_text));
                viewHolder.btn_consult_state.setBackground(context.getResources().getDrawable(R.drawable.btn_bg_square_gary));
                break;
        }
        return view;
    }

    static class ViewHolder {
        TextView consult_reply_tv;
        TextView first_reply_tv;
        Button btn_consult_state;
        ImageView consult_icon_image;
    }
}
