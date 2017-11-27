package com.cn.danceland.myapplication.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.cn.danceland.myapplication.R;

/**
 * Created by feng on 2017/11/6.
 */

public class StoreFragment extends BaseFragment {
    TextView store_name;
    ListView storelist;

    @Override
    public View initViews() {
        View v  = View.inflate(mActivity, R.layout.activity_store,null);
        store_name = v.findViewById(R.id.store_name);
        storelist = v.findViewById(R.id.storelist);

        return v;
    }

    @Override
    public void onClick(View v) {

    }

    public class MyStoreAdapter extends BaseAdapter{

        MyStoreAdapter(){

        }

        @Override
        public int getCount() {
            return 0;
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
            return null;
        }
    }
}
