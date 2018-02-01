package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.danceland.myapplication.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by feng on 2018/1/31.
 */

public class CabinetActivity extends Activity {

    ListView cabinet_lv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cabinet);
        initView();
    }

    private void initView() {

        cabinet_lv = findViewById(R.id.cabinet_lv);
        cabinet_lv.setAdapter(new MyAdapter());

    }

    private class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 3;
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

            View inflate = LayoutInflater.from(CabinetActivity.this).inflate(R.layout.cabinet_item, null);
            CircleImageView img_guizi = inflate.findViewById(R.id.img_guizi);
            TextView cabinet_num = inflate.findViewById(R.id.cabinet_num);
            TextView starttime = inflate.findViewById(R.id.starttime);
            TextView overtime = inflate.findViewById(R.id.overtime);



            return inflate;
        }
    }
}
