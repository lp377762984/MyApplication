package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.RequestShopListInfo;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shy on 2017/12/13 10:15
 * Email:644563767@qq.com
 */


public class MyShopActivity extends Activity implements View.OnClickListener {

    //   @BindView(R.id.lv_myshop)  ListView lv_myshop;

    private MyListViewAdapter listViewAdapter;
    private List<RequestShopListInfo.Data> data = new ArrayList<>();
    private ListView lv_myshop;
    private String defaultshopId;
    private Data userInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shop);
        initView();
        initData();
    }

    private void initView() {
        userInfo = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
        defaultshopId = userInfo.getPerson().getDefault_branch();
        findViewById(R.id.iv_back).setOnClickListener(this);
        lv_myshop = findViewById(R.id.lv_myshop);
        listViewAdapter = new MyListViewAdapter();
        lv_myshop.setAdapter(listViewAdapter);

        lv_myshop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!TextUtils.equals(defaultshopId,data.get(i).getBranch_id())){
                    showMYDialog(i);
                }

            }
        });
    }

    private void initData() {
        findJoinSHOP();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 设置手机号
     */
    private void showMYDialog(final int pos) {
        AlertDialog.Builder dialog =
                new AlertDialog.Builder(MyShopActivity.this);
        dialog.setTitle("提示");
        dialog.setMessage("是否切换为当前门店");
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                changeShop(data.get(pos).getBranch_id());
              //  LogUtil.i(data.get(pos).getBranch_id());
                //listViewAdapter.notifyDataSetChanged();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listViewAdapter.notifyDataSetChanged();
            }
        });
        dialog.show();
    }

    private void findJoinSHOP() {
        StringRequest request = new StringRequest(Request.Method.GET, Constants.FIND_JOIN_SHOP_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                Gson gson = new Gson();
                RequestShopListInfo shopListInfo = new RequestShopListInfo();
                shopListInfo = gson.fromJson(s, RequestShopListInfo.class);
                if (shopListInfo.getSuccess()) {
                    if (shopListInfo.getData().size() > 0) {
                        data = shopListInfo.getData();
                        listViewAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.showToastShort("您还没有加入任何门店");
                    }
                } else {
                    ToastUtils.showToastShort(shopListInfo.getErrorMsg());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToastShort(volleyError.toString());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String, String> hm = new HashMap<String, String>();
                String token = SPUtils.getString(Constants.MY_TOKEN, "");
                hm.put("Authorization", token);
                return hm;

            }
        };
        MyApplication.getHttpQueues().add(request);
    }

    private void changeShop(final String BranchId) {


        StringRequest stringRequest1 = new StringRequest(Request.Method.PUT, Constants.CHANGE_CURRENT_SHOP_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                Gson gson = new Gson();
                RequestShopListInfo shopListInfo = new RequestShopListInfo();
                shopListInfo = gson.fromJson(s, RequestShopListInfo.class);
                if (shopListInfo.getSuccess()) {
                    defaultshopId = BranchId;
                    userInfo.getPerson().setDefault_branch(defaultshopId);
                    DataInfoCache.saveOneCache(userInfo,Constants.MY_INFO);
                    listViewAdapter.notifyDataSetChanged();

                } else {
                    ToastUtils.showToastShort(shopListInfo.getErrorMsg());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToastShort(volleyError.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("branch_id", BranchId);

                return hashMap;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));
                return hashMap;
            }
        };


        MyApplication.getHttpQueues().add(stringRequest1);
    }


    class MyListViewAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view = View.inflate(MyShopActivity.this, R.layout.listview_item_my_shop, null);

            ImageView iv_shop_logo = view.findViewById(R.id.iv_shop_logo);
            TextView tv_name = view.findViewById(R.id.tv_name);
            TextView tv_default = view.findViewById(R.id.tv_default);
            ImageView cb_default = view.findViewById(R.id.cb_default);

            Glide.with(MyShopActivity.this).load(data.get(i).getLogo_url()).into(iv_shop_logo);
            tv_name.setText(data.get(i).getBname());
            if (TextUtils.equals(defaultshopId, data.get(i).getBranch_id())) {
                tv_default.setText("当前门店");
                cb_default.setImageResource(R.drawable.img_cb1);
            } else {
                tv_default.setText("");
                cb_default.setImageResource(R.drawable.img_cb);
            }


            return view;
        }
    }


}
