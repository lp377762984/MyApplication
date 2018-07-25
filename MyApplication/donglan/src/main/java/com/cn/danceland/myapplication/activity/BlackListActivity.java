package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.BlackListBean;
import com.cn.danceland.myapplication.bean.RequestSimpleBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.view.DongLanTitleView;
import com.google.gson.Gson;
import com.tencent.qcloud.ui.CircleImageView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BlackListActivity extends Activity {


    ListView listView;
    DongLanTitleView dongLanTitleView;
    List<BlackListBean.Data> dataList = new ArrayList<>();
    MyAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_list1);
        listView = (ListView) findViewById(R.id.list);
        dongLanTitleView = findViewById(R.id.dl_title);
        dongLanTitleView.setTitle("黑名单");
        findBlack();
        myAdapter=new MyAdapter();
        listView.setAdapter(myAdapter);


    }

    private class Strbean {
        public String blocked_id;
    }

    private void findBlack() {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.FIND_BLACKLIST_URL, new Gson().toJson(new Strbean()), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                BlackListBean blackListBean = new Gson().fromJson(jsonObject.toString(), BlackListBean.class);
                dataList=blackListBean.getData();
                myAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.e(volleyError.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));
                //  LogUtil.i(SPUtils.getString(Constants.MY_TOKEN, ""));
                return map;
            }
        };
        MyApplication.getHttpQueues().add(request);

    }
    private void delBlack(final int pos) {

        StringRequest request=new StringRequest(Request.Method.POST, Constants.DEL_BLACKLIST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                RequestSimpleBean simpleBean=new Gson().fromJson(s,RequestSimpleBean.class);
                if (simpleBean.getSuccess()){
                    dataList.remove(pos);
                    myAdapter.notifyDataSetChanged();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));
                //  LogUtil.i(SPUtils.getString(Constants.MY_TOKEN, ""));
                return map;


            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", dataList.get(pos).getId()+"");
                return map;
            }
        };

        MyApplication.getHttpQueues().add(request);

    }
    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View view = View.inflate(BlackListActivity.this, R.layout.listview_item_blacklist, null);
            TextView name = view.findViewById(R.id.name);
            TextView tv_cancel = view.findViewById(R.id.tv_cancel);
            CircleImageView avatar = view.findViewById(R.id.avatar);
            Glide.with(BlackListActivity.this).load(dataList.get(position).getSelf_avatar_path()).into(avatar);
            name.setText(dataList.get(position).getNick_name());
            tv_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delBlack(position);
                }
            });
            return view;
        }
    }
}
