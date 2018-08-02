package com.cn.danceland.myapplication.shouhuan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.db.HeartRate;
import com.cn.danceland.myapplication.shouhuan.bean.DeviceBean;

import java.util.ArrayList;

/**
 * Created by yxx on 2018/7/19.
 */

public class HeartRateAdapter extends BaseAdapter {
    private ArrayList<HeartRate> heartrateBeens;
    private Context context;
    private DeviceBean deviceBean;

    public HeartRateAdapter(Context context, ArrayList<HeartRate> heartrateBeens) {
        super();
        this.heartrateBeens = heartrateBeens;
        this.context = context;

    }

    public void setData( ArrayList<HeartRate> heartrateBeens){
        this.heartrateBeens = heartrateBeens;
    }

    public void clear() {
        heartrateBeens.clear();//先清除这个
    }

//    public void sort() {
//        Log.d("zgy", "sort: ");
//
//        for (int i = 0; i < heartrateBeens.size(); i++) {
//            for (int j = i + 1; j < heartrateBeens.size(); j++) {
//                if (heartrateBeens.get(i).getRssi() < heartrateBeens.get(j).getRssi()) {
//                    heartrateBeens.set(i, heartrateBeens.get(j));
//                    heartrateBeens.set(j, deviceBean);
//                }
//            }
//        }
//
//    }

    @Override
    public int getCount() {
        return heartrateBeens.size();
    }

    @Override
    public Object getItem(int i) {
        return heartrateBeens.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.listitem_heartrate, null);
            viewHolder = new ViewHolder();
            viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
            viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
            viewHolder.rssi = (TextView) view.findViewById(R.id.rssi);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.deviceAddress.setText(String.valueOf(heartrateBeens.get(i).getDate()));
        viewHolder.deviceName.setText(String.valueOf(heartrateBeens.get(i).getHeartRate()));
        viewHolder.rssi.setText(String.valueOf(heartrateBeens.get(i).getId()));
        return view;
    }

    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
        TextView rssi;
    }
}
