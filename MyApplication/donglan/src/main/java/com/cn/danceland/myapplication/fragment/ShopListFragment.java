package com.cn.danceland.myapplication.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.activity.MapActivity;
import com.cn.danceland.myapplication.activity.ShopDetailedActivity;
import com.cn.danceland.myapplication.bean.BranchBannerBean;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.StoreBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.google.gson.Gson;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by feng on 2018/4/9.
 */

public class ShopListFragment extends BaseFragment {

    ListView lv_shoplist;
    View headView;
    MZBannerView shop_banner;
    ArrayList<String> drawableArrayList;
    String jingdu, weidu;
    List<StoreBean.Items> itemsList;
    Gson gson;
    Data info;
    TextView tv_shopname, tv_shopAddress;
    ImageButton ibtn_gps, ibtn_call;

    @Override
    public View initViews() {
        View inflate = View.inflate(mActivity, R.layout.shoplist_fragment, null);
        headView = LayoutInflater.from(mActivity).inflate(R.layout.shoplist_fragment_head, null);
        shop_banner = headView.findViewById(R.id.shop_banner);
        tv_shopname = headView.findViewById(R.id.tv_shopname);
        tv_shopAddress = headView.findViewById(R.id.tv_shopAddress);
        ibtn_gps = headView.findViewById(R.id.ibtn_gps);
        ibtn_call = headView.findViewById(R.id.ibtn_call);
        drawableArrayList = new ArrayList<>();

        gson = new Gson();
        lv_shoplist = inflate.findViewById(R.id.lv_shoplist);
        lv_shoplist.addHeaderView(headView);
        initData();

        return inflate;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        String getlocationString = ((HomeActivity) activity).getlocationString();
//        if (getlocationString != null) {
//            jingdu = getlocationString.split(",")[0];
//            weidu = getlocationString.split(",")[1];
//        }
        jingdu = getArguments().getString("jingdu");
        weidu = getArguments().getString("weidu");
       // LogUtil.i(jingdu + weidu);
    }

    private void initData() {
        getListData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            info = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
            initData();
        }
    }

    public void getListData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.BRANCH + "/0/" + weidu + "/" + jingdu, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                StoreBean storeBean = gson.fromJson(s, StoreBean.class);
                if (storeBean != null && storeBean.getData() != null) {
                    itemsList = storeBean.getData().getItems();
                    if (itemsList != null && itemsList.size() > 0) {
                        tv_shopname.setText(itemsList.get(0).getBname());
                        tv_shopAddress.setText(itemsList.get(0).getAddress());
                        getBanner(itemsList.get(0).getBranch_id() + "");

                        ibtn_gps.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), MapActivity.class);
                                intent.putExtra("shopWeidu", itemsList.get(0).getLat() + "");
                                intent.putExtra("shopJingdu", itemsList.get(0).getLng() + "");
                                intent.putExtra("jingdu", jingdu);
                                intent.putExtra("weidu", weidu);
                                startActivity(intent);
                            }
                        });

                        ibtn_call.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showDialog(itemsList.get(0).getTelphone_no());
                            }
                        });

                        List<StoreBean.Items> itemsList1 = new ArrayList<>();
                        if (itemsList.size() > 1) {
                            for (int i = 1; i < itemsList.size(); i++) {
                                itemsList1.add(itemsList.get(i));
                            }
                        }
                        lv_shoplist.setAdapter(new MyStoreAdapter(getActivity(), itemsList1));
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.e("zzf", volleyError.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));
                return map;
            }
        };

        MyApplication.getHttpQueues().add(stringRequest);

    }


    private void getBanner(final String branchId) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BANNER, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                BranchBannerBean branchBannerBean = gson.fromJson(s, BranchBannerBean.class);
                if (branchBannerBean != null) {
                    drawableArrayList.clear();
                    List<BranchBannerBean.Data> data = branchBannerBean.getData();
                    if (data != null) {
                        for (int i = 0; i < data.size(); i++) {
                            drawableArrayList.add(data.get(i).getImg_url());
                        }
                        setBannner();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.e("zzf", volleyError.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("branchId", branchId);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));
                return map;
            }
        };
        MyApplication.getHttpQueues().add(stringRequest);
    }


    public class MyStoreAdapter extends BaseAdapter {
        Context mContext;
        List<StoreBean.Items> itemsArrayList;

        MyStoreAdapter(Context context, List<StoreBean.Items> list) {
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
            ViewHolder viewHolder = null;
            StoreBean.Items items;

            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(mActivity, R.layout.store_item, null);
                viewHolder.store_item_img = convertView.findViewById(R.id.store_item_img);
                viewHolder.store_address = convertView.findViewById(R.id.store_address);
                viewHolder.distance = convertView.findViewById(R.id.distance);
                viewHolder.img_location = convertView.findViewById(R.id.img_location);
                viewHolder.img_phone = convertView.findViewById(R.id.img_phone);
                viewHolder.img_join = convertView.findViewById(R.id.img_join);
                viewHolder.clickitem = convertView.findViewById(R.id.clickitem);
                //iewById(R.id.unread_msg_number);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }


            if (itemsArrayList != null) {
                items = itemsArrayList.get(position);
                viewHolder.store_address.setText(items.getBname());
                viewHolder.distance.setText(items.getAddress());
                Glide.with(getActivity()).load("http://img0.imgtn.bdimg.com/it/u=2269433745,3578312737&fm=214&gp=0.jpg").into(viewHolder.store_item_img);
                //PhoneNo = items.getTelphone_no();
//                shopJingdu = items.getLat()+"";
//                shopWeidu = items.getLng()+"";
//                branchId = items.getBranch_id()+"";
            }
            viewHolder.img_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemsArrayList != null) {
                        showDialog(itemsArrayList.get(position).getTelphone_no());
                    }
                }
            });
            viewHolder.img_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemsArrayList != null) {
                        Intent intent = new Intent(getActivity(), MapActivity.class);
                        intent.putExtra("shopWeidu", itemsArrayList.get(position).getLat() + "");
                        intent.putExtra("shopJingdu", itemsArrayList.get(position).getLng() + "");
                        intent.putExtra("jingdu", jingdu);
                        intent.putExtra("weidu", weidu);
                        startActivity(intent);
                    }
                }
            });
            viewHolder.clickitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemsArrayList != null) {
                        Intent intent = new Intent(getActivity(), ShopDetailedActivity.class);
                        intent.putExtra("shopWeidu", itemsArrayList.get(position).getLat() + "");
                        intent.putExtra("shopJingdu", itemsArrayList.get(position).getLng() + "");
                        intent.putExtra("jingdu", jingdu);
                        intent.putExtra("weidu", weidu);
                        intent.putExtra("branchID", itemsArrayList.get(position).getBranch_id() + "");
                        intent.putStringArrayListExtra("imgList", drawableArrayList);
                        startActivityForResult(intent, 111);
                    }
                }
            });

            return convertView;
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

    class ViewHolder {
        ImageView store_item_img, img_location, img_phone, img_join;
        TextView store_address, distance, unread_msg_number;
        RelativeLayout clickitem;
    }


    private void setBannner() {

        //监听事件必须在setpages之前
        shop_banner.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int i) {
                Intent intent = new Intent(mActivity, ShopDetailedActivity.class);
                intent.putExtra("shopWeidu", itemsList.get(0).getLat() + "");
                intent.putExtra("shopJingdu", itemsList.get(0).getLng() + "");
                intent.putExtra("jingdu", jingdu);
                intent.putExtra("weidu", weidu);
                intent.putExtra("branchID", itemsList.get(0).getBranch_id() + "");
                intent.putStringArrayListExtra("imgList", drawableArrayList);
                startActivityForResult(intent, 111);
            }
        });
        if (drawableArrayList != null && drawableArrayList.size() == 0) {
            drawableArrayList.add("http://i3.hoopchina.com.cn/blogfile/201403/31/BbsImg139626653396762_620*413.jpg");
        }
        // 设置数据
        shop_banner.setPages(drawableArrayList, new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });

        shop_banner.setIndicatorVisible(false);
        shop_banner.start();

    }

    @Override
    public void onPause() {
        super.onPause();
        shop_banner.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        shop_banner.start();
    }

    public static class BannerViewHolder implements MZViewHolder<String> {
        private ImageView mImageView;

        @Override
        public View createView(Context context) {
            // 返回页面布局
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
            mImageView = (ImageView) view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(Context context, int position, String data) {
            // 数据绑定
            Glide.with(context).load(data).into(mImageView);
            //mImageView.setImageResource(data);
        }
    }

    @Override
    public void onClick(View v) {

    }
}