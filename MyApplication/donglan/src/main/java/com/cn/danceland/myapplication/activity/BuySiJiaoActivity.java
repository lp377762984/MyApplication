package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.BuySiJiaoBean;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.FindSiJiaoBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by feng on 2018/1/15.
 */

public class BuySiJiaoActivity extends Activity {
    ListView lv_sijiaocard;
    ImageView buy_img;
    Gson gson;
    FindSiJiaoBean findSiJiaoBean;
    List<BuySiJiaoBean.Content> content;

    Data info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buysijiao);
        initHost();
        intiView();
    }

    private void initHost() {
        info = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
        gson = new Gson();
        findSiJiaoBean = new FindSiJiaoBean();

    }

    private void getData() throws JSONException {
        findSiJiaoBean.setPage(0);
        findSiJiaoBean.setSize(15);
        findSiJiaoBean.setBranch_id(Integer.valueOf(info.getPerson().getDefault_branch()));
        String s = gson.toJson(findSiJiaoBean);
        JSONObject jsonObject = new JSONObject(s);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.COURSETYPELIST,jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if(jsonObject!=null){
                    LogUtil.e("zzf",jsonObject.toString());
                    String string = jsonObject.toString();
                    BuySiJiaoBean buySiJiaoBean = gson.fromJson(string, BuySiJiaoBean.class);
                    content = buySiJiaoBean.getData().getContent();
                    if(content!=null){
                        lv_sijiaocard.setAdapter(new MyAdapter(content));
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.e("zzf",volleyError.toString());

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<String,String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN,""));
                return map;
            }
        };

        MyApplication.getHttpQueues().add(jsonObjectRequest);

    }


    private void intiView() {


        lv_sijiaocard = findViewById(R.id.lv_sijiaocard);

        lv_sijiaocard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(content!=null){
                    BuySiJiaoBean.Content itemContent = BuySiJiaoActivity.this.content.get(position);
                    startActivity(new Intent(BuySiJiaoActivity.this,SellSiJiaoConfirmActivity.class).putExtra("itemContent",itemContent));
                    finish();
                }

            }
        });

        buy_img = findViewById(R.id.buy_img);
        buy_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        try {
            getData();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private class MyAdapter extends BaseAdapter{

        List<BuySiJiaoBean.Content> contentList;

        MyAdapter(List<BuySiJiaoBean.Content> contentList){
            this.contentList = contentList;

        }


        @Override
        public int getCount() {
            return contentList.size();
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
            ViewHolder viewHolder;

            if(convertView==null){
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(BuySiJiaoActivity.this).inflate(R.layout.sijiaocard, null);
                viewHolder.card_img = convertView.findViewById(R.id.card_img);
                viewHolder.sijiao_name = convertView.findViewById(R.id.sijiao_name);
                viewHolder.sijiao_type = convertView.findViewById(R.id.sijiao_type);
                viewHolder.sijiao_amount = convertView.findViewById(R.id.sijiao_amount);
                viewHolder.sijiao_price = convertView.findViewById(R.id.sijiao_price);

                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)convertView.getTag();
            }
            Glide.with(BuySiJiaoActivity.this).load(contentList.get(position).getImg_url()).into(viewHolder.card_img);
            viewHolder.sijiao_name.setText("私教名称："+contentList.get(position).getName());
            viewHolder.sijiao_type.setText("课程类型："+contentList.get(position).getCourse_category_name());
            viewHolder.sijiao_amount.setText("课程节数："+contentList.get(position).getCount()+"节");
            viewHolder.sijiao_price.setText("金额："+contentList.get(position).getPrice()+"元");


            return convertView;
        }
    }

    class ViewHolder{
        ImageView card_img;
        TextView sijiao_name,sijiao_type,sijiao_amount,sijiao_price;

    }
}
