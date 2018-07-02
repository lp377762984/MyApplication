package com.cn.danceland.myapplication.shouhuan.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.ClockBean;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.StringUtils;
import com.cn.danceland.myapplication.view.DongLanTitleView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.cn.danceland.myapplication.utils.SPUtils.PREF_NAME;

/**
 * Created by feng on 2018/6/27.
 */

public class AddClockActivity extends Activity {

    private DongLanTitleView addclock_title;
    private TextView rightTv;
    private String time;
    private ListView lv_clock;
    private List<ClockBean> arrayList;
    private Gson gson;
    private ClockAdapter clockAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addclock);
        gson = new Gson();
        initView();
    }

    private void initView() {

        addclock_title = findViewById(R.id.addclock_title);
        addclock_title.setTitle("闹钟提醒");
        rightTv = addclock_title.getRightTv();
        rightTv.setVisibility(View.VISIBLE);
        rightTv.setText("添加");
        rightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(AddClockActivity.this,ClockSettingActivity.class),3);
            }
        });
        lv_clock = findViewById(R.id.lv_clock);
        arrayList = new ArrayList<ClockBean>();
        clockAdapter = new ClockAdapter();
        lv_clock.setAdapter(clockAdapter);
        arrayList.clear();
        getHistory();
        clockAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==3){
            arrayList.clear();
            getHistory();
            time = data.getStringExtra("time");
            ClockBean clockBean = new ClockBean();
            clockBean.setTime(time);
            arrayList.add(clockBean);
            clockAdapter.notifyDataSetChanged();
            SPUtils.setString("ClockList",gson.toJson(arrayList));
        }
    }

    private void getHistory(){
        String clockList = SPUtils.getString("ClockList", "");
        if(!StringUtils.isNullorEmpty(clockList)){
            Type listType = new TypeToken<List<ClockBean>>() {
            }.getType();
            List<ClockBean> clockBeans = gson.fromJson(clockList, listType);
            arrayList.addAll(clockBeans);
        }
    }

    private class ClockAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            View inflate = LayoutInflater.from(AddClockActivity.this).inflate(R.layout.item_clock, null);
            TextView tv_time = inflate.findViewById(R.id.tv_time);
            tv_time.setText(arrayList.get(i).getTime());
            return inflate;
        }
    }
}
