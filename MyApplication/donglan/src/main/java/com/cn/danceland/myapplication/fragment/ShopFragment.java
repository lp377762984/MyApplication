package com.cn.danceland.myapplication.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.activity.HomeActivity;
import com.cn.danceland.myapplication.activity.MapActivity;
import com.cn.danceland.myapplication.activity.SellCardActivity;
import com.cn.danceland.myapplication.activity.ShopDetailedActivity;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.StoreBean;
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
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends BaseFragment {
    RequestQueue requestQueue;
    ListView storelist;
    GridView mGridView;
    String jingdu,weidu,shopJingdu,shopWeidu,branchId;
    String PhoneNo;
    Gson gson;
    Data info;
    List<StoreBean.Items> itemsList;
    ImageButton ibtn_call,ibtn_gps;
    String[] icon_name = {"健身圈", "私人教练", "会所动态", "在线售卡", "课程表", "会所商城", "意见反馈", "会所活动", "我的会员卡"};
    int[] icons = {R.drawable.img_jsq, R.drawable.img_srjl, R.drawable.img_hsdt, R.drawable.img_zxsk
            , R.drawable.img_kcb, R.drawable.img_hssc, R.drawable.img_yjfk, R.drawable.img_hshd, R.drawable.img_hyk};

    @Override
    public View initViews() {
        View v = View.inflate(mActivity, R.layout.fragment_shop, null);

        info = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);

        gson = new Gson();
        requestQueue = Volley.newRequestQueue(getActivity());
        mGridView = v.findViewById(R.id.gridview);

        ibtn_call = v.findViewById(R.id.ibtn_call);
        ibtn_gps = v.findViewById(R.id.ibtn_gps);

        mGridView.setAdapter(new MyAdapter());
        mGridView.setOnItemClickListener(new MyOnItemClickListener());

        storelist = v.findViewById(R.id.storelist);


        getData();
        if(info.getDefault_branch()!=null&&!info.getDefault_branch().equals("")){
            mGridView.setVisibility(View.VISIBLE);
            ibtn_call.setVisibility(View.VISIBLE);
            ibtn_gps.setVisibility(View.VISIBLE);
            storelist.setVisibility(View.GONE);
        }else{

            mGridView.setVisibility(View.GONE);
            storelist.setVisibility(View.VISIBLE);
            ibtn_call.setVisibility(View.GONE);
            ibtn_gps.setVisibility(View.GONE);
        }
        LinearLayout ll_top = v.findViewById(R.id.ll_top);
        ll_top.setBackgroundColor(Color.WHITE);
        ll_top.getBackground().setAlpha(80);
        v.findViewById(R.id.ibtn_call).setOnClickListener(this);
        v.findViewById(R.id.ibtn_gps).setOnClickListener(this);
       // mGridView.setVisibility(View.VISIBLE);

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        String getlocationString = ((HomeActivity) activity).getlocationString();
        if(getlocationString!=null){
            jingdu = getlocationString.split(",")[0];
            weidu = getlocationString.split(",")[1];
        }
    }

    public void getData(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.BRANCH+"/1/"+jingdu+"/"+weidu, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                StoreBean storeBean = gson.fromJson(s, StoreBean.class);
                itemsList = storeBean.getData().getItems();
                if(itemsList!=null&&itemsList.size()>0){
                    storelist.setAdapter(new MyStoreAdapter(getActivity(),itemsList));
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

        requestQueue.add(stringRequest);

    }



    @Override
    public void initDta() {

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden){
            getData();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_call:

                //showDialog();
                break;
            case R.id.ibtn_gps:

                ToastUtils.showToastShort("显示位置");

                break;
            default:
                break;
        }
    }

    /**
     * 提示
     */
    private void showDialog(final String phoneNo) {
        AlertDialog.Builder dialog =
                new AlertDialog.Builder(mActivity);
        dialog.setTitle("提示");
        dialog.setMessage("是否呼叫" + phoneNo);
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                call(phoneNo);
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }

    /**
     * 调用拨号功能
     *
     * @param phone 电话号码
     */
    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }


    class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            ToastUtils.showToastShort(icon_name[i]);
            switch (i) {
                case 0:
                    break;
                case 1:

                    break;
                case 2:
                    break;
                case 3://在线售卡
                    startActivity(new Intent(mActivity, SellCardActivity.class));
                    break;
                case 4:

                    break;
                case 5:

                    break;
                case 6:
                    break;
                case 7:
                    break;
                case 8:
                    LogUtil.i(SPUtils.getString(Constants.MY_USERID,null));
                    LogUtil.i(SPUtils.getString(Constants.MY_TOKEN,null));
                    break;
                default:
                    break;
            }


        }
    }

    public class MyStoreAdapter extends BaseAdapter{
        Context mContext;
        List<StoreBean.Items> itemsArrayList;

        MyStoreAdapter(Context context, List<StoreBean.Items> list){
            mContext = context;
            itemsArrayList = list;
        }

        @Override
        public int getCount() {
            return itemsArrayList.size();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder=null;
            StoreBean.Items items;

            if(convertView==null){
                viewHolder = new ViewHolder();
                convertView = View.inflate(mActivity,R.layout.store_item,null);
                viewHolder.store_item_img = convertView.findViewById(R.id.store_item_img);
                viewHolder.store_address = convertView.findViewById(R.id.store_address);
                viewHolder.distance = convertView.findViewById(R.id.distance);
                viewHolder.img_location = convertView.findViewById(R.id.img_location);
                viewHolder.img_phone = convertView.findViewById(R.id.img_phone);
                viewHolder.img_join = convertView.findViewById(R.id.img_join);
                viewHolder.clickitem = convertView.findViewById(R.id.clickitem);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }


            if(itemsArrayList!=null){
                items = itemsArrayList.get(position);
                viewHolder.store_address.setText(items.getBname());
                viewHolder.distance.setText(items.getAddress());
                Glide.with(getActivity()).load(items.getLogo()).into(viewHolder.store_item_img);
                //PhoneNo = items.getTelphone_no();
//                shopJingdu = items.getLat()+"";
//                shopWeidu = items.getLng()+"";
//                branchId = items.getBranch_id()+"";
            }
            viewHolder.img_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemsArrayList!=null){
                        showDialog(itemsArrayList.get(position).getTelphone_no());
                    }
                }
            });
            viewHolder.img_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemsArrayList!=null){
                        Intent intent = new Intent(getActivity(), MapActivity.class);
                        intent.putExtra("shopJingdu",itemsArrayList.get(position).getLat()+"");
                        intent.putExtra("shopWeidu",itemsArrayList.get(position).getLng()+"");
                        intent.putExtra("jingdu",jingdu);
                        intent.putExtra("weidu",weidu);
                        startActivity(intent);
                    }
                }
            });
            viewHolder.img_join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder dialog =
                            new AlertDialog.Builder(mActivity);
                    dialog.setTitle("提示");
                    dialog.setMessage("是否加入此门店");
                    dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(itemsArrayList!=null){
                                join(itemsArrayList.get(position).getBranch_id()+"");
                            }

                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    dialog.show();
                }
            });
            viewHolder.clickitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemsArrayList!=null){
                        Intent intent = new Intent(getActivity(), ShopDetailedActivity.class);
                        intent.putExtra("shopJingdu",itemsArrayList.get(position).getLat()+"");
                        intent.putExtra("shopWeidu",itemsArrayList.get(position).getLng()+"");
                        intent.putExtra("jingdu",jingdu);
                        intent.putExtra("weidu",weidu);
                        intent.putExtra("branchID",itemsArrayList.get(position).getBranch_id()+"");
                        startActivityForResult(intent,111);
                    }
                }
            });

            return convertView;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==111){
            initViews();
        }

    }

    private void join(final String shopID){
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, Constants.JOINBRANCH, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(s.contains("true")){
                    info.setDefault_branch(shopID);
                    DataInfoCache.saveOneCache(info,Constants.MY_INFO);
                    mGridView.setVisibility(View.VISIBLE);
                    ibtn_call.setVisibility(View.VISIBLE);
                    ibtn_gps.setVisibility(View.VISIBLE);
                    storelist.setVisibility(View.GONE);
                }else{
                    ToastUtils.showToastShort("加入失败！请检查网络！");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<String,String>();
                map.put("branchId",shopID);
                map.put("join","true");
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<String,String>();
                map.put("Authorization",SPUtils.getString(Constants.MY_TOKEN,""));
                return map;
            }
        };

        requestQueue.add(stringRequest);

    }

    class ViewHolder{
        ImageView store_item_img,img_location,img_phone,img_join;
        TextView store_address,distance;
        RelativeLayout clickitem;
    }


    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return icon_name.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return icons.length;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            view = View.inflate(mActivity, R.layout.gridview_item_shop, null);
            TextView tv_dcs = view.findViewById(R.id.tv_dcs);
            ImageView ibtn = view.findViewById(R.id.ibtn);
            tv_dcs.setText(icon_name[i]);

            ibtn.setBackgroundResource(icons[i]);
            return view;
        }
    }


}
