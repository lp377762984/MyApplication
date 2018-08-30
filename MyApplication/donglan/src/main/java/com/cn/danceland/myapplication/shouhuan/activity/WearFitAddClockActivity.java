package com.cn.danceland.myapplication.shouhuan.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.ClockBean;
import com.cn.danceland.myapplication.shouhuan.command.CommandManager;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.StringUtils;
import com.cn.danceland.myapplication.view.DongLanTitleView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by feng on 2018/6/27.
 */
public class WearFitAddClockActivity extends Activity {

    private TextView addclock_title;
    private TextView rightTv;
    private ListView lv_clock;
    private List<ClockBean> localClockList;
    private Gson gson;
    private ClockAdapter clockAdapter;
    private ImageView donglan_back;
    private CommandManager commandManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addclock);
        commandManager = CommandManager.getInstance(getApplicationContext());
        gson = new Gson();
        initView();
    }

    private void initView() {
        addclock_title = findViewById(R.id.donglan_title);
        addclock_title.setText("闹钟提醒");
        rightTv = findViewById(R.id.donglan_right_tv);
        donglan_back = findViewById(R.id.donglan_back);
        rightTv.setVisibility(View.VISIBLE);
        rightTv.setText("添加");
        lv_clock = findViewById(R.id.lv_clock);
        localClockList = new ArrayList<ClockBean>();
        clockAdapter = new ClockAdapter();
        lv_clock.setAdapter(clockAdapter);
        localClockList.clear();
        rightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (localClockList.size() >= 8) {
                    Toast.makeText(WearFitAddClockActivity.this, "闹钟设置最多不超过八个", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(WearFitAddClockActivity.this, WearFitClockSettingActivity.class);
                        intent.putExtra("hour", "00");
                        intent.putExtra("minute", "00");
                    startActivity(intent);
//                    startActivity(new Intent(WearFitAddClockActivity.this, WearFitClockSettingActivity.class));
//                startActivityForResult(new Intent(WearFitAddClockActivity.this,WearFitClockSettingActivity.class),3);
                }
            }
        });
        donglan_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        lv_clock.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(WearFitAddClockActivity.this, WearFitClockSettingActivity.class);
                if (localClockList.get(i).getHour() == 0) {
                    intent.putExtra("hour", "00");
                } else {
                    intent.putExtra("hour", localClockList.get(i).getHour() + "");
                }
                if (localClockList.get(i).getMinute() == 0) {
                    intent.putExtra("minute", "00");
                } else {
                    intent.putExtra("minute", localClockList.get(i).getMinute() + "");
                }
                intent.putExtra("clock_id", i);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getHistory();
        clockAdapter.notifyDataSetChanged();
    }

    private void getHistory() {
        localClockList.clear();
        String clockList = SPUtils.getString("ClockList", "");
        if (!StringUtils.isNullorEmpty(clockList)) {
            Type listType = new TypeToken<List<ClockBean>>() {
            }.getType();
            List<ClockBean> clockBeans = gson.fromJson(clockList, listType);
            localClockList.addAll(clockBeans);
        }
    }

    private void getData() {
        getHistory();
        clockAdapter.notifyDataSetChanged();
        for (int i = 0; i < localClockList.size(); i++) {
            ClockBean clockBean = localClockList.get(i);
            List<Integer> weekdayList = clockBean.getWeekday();//周几 0 1 2 3 4 5 6
            if (weekdayList != null) {
                for (int j = 0; j < weekdayList.size(); j++) {
                    LogUtil.i("提交clockBean--------" + localClockList.get(i).toString());
                    LogUtil.i("提交weekdayList.get(j)--------" + weekdayList.get(j));
                    commandManager.setAlarmClock(localClockList.get(i), weekdayList.get(j));
                }
            }
        }
    }

    private class ClockAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return localClockList.size();
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
        public View getView(final int i, View view, ViewGroup viewGroup) {

            View inflate = LayoutInflater.from(WearFitAddClockActivity.this).inflate(R.layout.item_clock, null);
            TextView tv_time = inflate.findViewById(R.id.tv_time);
            tv_time.setText(localClockList.get(i).getTime());
            Switch btn_switch = inflate.findViewById(R.id.btn_switch);//闹钟id开关，最多开8个
            if (localClockList.get(i).getOffOn() == 1) {//0关1开
                btn_switch.setChecked(true);
            } else {
                btn_switch.setChecked(false);
            }
            btn_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    ClockBean clockBean = localClockList.get(i);
                    if (b) {
                        clockBean.setOffOn(1);//0关1开
                    } else {
                        clockBean.setOffOn(0);//0关1开
                    }
                    localClockList.set(i, clockBean);
                    SPUtils.setString("ClockList", gson.toJson(localClockList));
                    getData();
                }
            });
            return inflate;
        }
    }
}
