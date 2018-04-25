package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.utils.MyListView;
import com.cn.danceland.myapplication.view.CustomDatePicker;

import java.util.ArrayList;

/**
 * Created by feng on 2018/4/24.
 */

public class ReportFormActivity extends Activity {
    ArrayList<Object> dataList;
    MyListView report_mv,report_mv_02;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_report);
        initHost();
        initView();
    }

    private void initView() {

        report_mv = findViewById(R.id.report_mv);
        report_mv_02 = findViewById(R.id.report_mv_02);



    }

    private void initHost() {


        dataList = new ArrayList<>();

    }

    private class MyListViewAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return dataList==null? 0:dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View inflate = View.inflate(ReportFormActivity.this, R.layout.report_item, null);
            TextView tv_name = inflate.findViewById(R.id.tv_name);
            TextView tv_today = inflate.findViewById(R.id.tv_today);
            TextView tv_thisMonth = findViewById(R.id.tv_thisMonth);
            TextView tv_total = findViewById(R.id.tv_total);



            return inflate;
        }
    }
}
