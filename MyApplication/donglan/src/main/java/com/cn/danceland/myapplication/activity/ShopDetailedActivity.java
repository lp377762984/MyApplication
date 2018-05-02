package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
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
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.RequestLoginInfoBean;
import com.cn.danceland.myapplication.bean.ShopDetailBean;
import com.cn.danceland.myapplication.bean.ShopJiaoLianBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.CustomGridView;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by feng on 2017/11/29.
 */

public class ShopDetailedActivity extends Activity{
    Button bt_back,join_button;
    RequestQueue requestQueue;
    Gson gson;
    TextView tv_adress,tv_time,store_name;
    ExpandableTextView tv_detail;
    String phoneNo;
    ImageView detail_phone,detail_adress,img_01,img_02,img_kechenganpai;
    String jingdu,weidu,shopJingdu,shopWeidu,branchID,myBranchId;
    RelativeLayout s_button;
    Data myInfo;
    ExpandableListView jiaolian_grid,huiji_grid;
    ImageView down_img,up_img;
    List<ShopJiaoLianBean.Data> jiaolianList;
    ArrayList<String> imgList;
    MZBannerView shop_banner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopdetailed);
        initHost();
        initView();
    }

    private void initHost() {

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        myInfo = (Data)DataInfoCache.loadOneCache(Constants.MY_INFO);
        gson = new Gson();
        jingdu = getIntent().getStringExtra("jingdu");
        weidu = getIntent().getStringExtra("weidu");
        shopJingdu = getIntent().getStringExtra("shopJingdu");
        shopWeidu = getIntent().getStringExtra("shopWeidu");
        branchID = getIntent().getStringExtra("branchID");
        imgList = getIntent().getStringArrayListExtra("imgList");
        //myBranchId = myInfo.getPerson().getDefault_branch();
        isJoinBranch(branchID);

    }

    private void isJoinBranch(final String branchId) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.ISJOINBRANCH, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
            LogUtil.i(s);
                if(s.contains("1")){
                    s_button.setVisibility(View.GONE);
                }else{
                    s_button.setVisibility(View.VISIBLE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("branchId",branchId);

                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<String,String>();
                map.put("Authorization",SPUtils.getString(Constants.MY_TOKEN,""));
                return map;
            }
        };
        MyApplication.getHttpQueues().add(stringRequest);
    }

    private void initView() {

        shop_banner = findViewById(R.id.shop_banner);
        img_kechenganpai = findViewById(R.id.img_kechenganpai);
        jiaolian_grid = findViewById(R.id.jiaolian_grid);
        huiji_grid = findViewById(R.id.huiji_grid);
        jiaolian_grid.setAdapter(new MyAdapter());
        huiji_grid.setAdapter(new MyAdapter());
        jiaolian_grid.setGroupIndicator(null);
        huiji_grid.setGroupIndicator(null);
        jiaolian_grid.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                down_img = v.findViewById(R.id.down_img);
                up_img = v.findViewById(R.id.up_img);
                if(down_img.getVisibility()==View.GONE){
                    down_img.setVisibility(View.VISIBLE);
                    up_img.setVisibility(View.GONE);
                }else{
                    down_img.setVisibility(View.GONE);
                    up_img.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        img_01 = findViewById(R.id.img_01);
        img_02 = findViewById(R.id.img_02);
        Glide.with(ShopDetailedActivity.this).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1521091228318&di=fdf182a124da7454353241da6e101ab5&imgtype=0&src=http%3A%2F%2Fpic.58pic.com%2F58pic%2F15%2F25%2F82%2F47I58PICGQK_1024.jpg").into(img_01);
        Glide.with(ShopDetailedActivity.this).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1521091228318&di=fdf182a124da7454353241da6e101ab5&imgtype=0&src=http%3A%2F%2Fpic.58pic.com%2F58pic%2F15%2F25%2F82%2F47I58PICGQK_1024.jpg").into(img_02);
        Glide.with(ShopDetailedActivity.this).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1521091228318&di=fdf182a124da7454353241da6e101ab5&imgtype=0&src=http%3A%2F%2Fpic.58pic.com%2F58pic%2F15%2F25%2F82%2F47I58PICGQK_1024.jpg").into(img_kechenganpai);

        tv_adress = findViewById(R.id.tv_adress);
        tv_time = findViewById(R.id.tv_time);
        tv_detail= findViewById(R.id.tv_detail);
        store_name = findViewById(R.id.store_name);
        detail_phone = findViewById(R.id.detail_phone);
        detail_adress = findViewById(R.id.detail_adress);

        s_button = findViewById(R.id.s_button);

//        join_button = findViewById(R.id.join_button);

        bt_back = findViewById(R.id.bt_back);
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        detail_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        detail_adress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                intent.putExtra("shopJingdu",shopJingdu);
                intent.putExtra("shopWeidu",shopWeidu);
                intent.putExtra("jingdu",jingdu);
                intent.putExtra("weidu",weidu);
                startActivity(intent);
            }
        });
//        s_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(),SellCardActivity.class);
//                startActivity(intent);
//            }
//        });

        s_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog =
                        new AlertDialog.Builder(ShopDetailedActivity.this);
                dialog.setTitle("提示");
                dialog.setMessage("是否加入此门店");
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        join(branchID);
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
        setBannner();
        getShopDetail();
        getJiaolian(branchID);
    }
    private void setBannner() {

        shop_banner.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int i) {
//                Intent intent = new Intent(mActivity, ShopDetailedActivity.class);
//                intent.putExtra("shopWeidu", itemsList.get(0).getLat() + "");
//                intent.putExtra("shopJingdu", itemsList.get(0).getLng() + "");
//                intent.putExtra("jingdu", jingdu);
//                intent.putExtra("weidu", weidu);
//                intent.putExtra("branchID", itemsList.get(0).getBranch_id() + "");
//                startActivity(intent);
            }
        });
        if(imgList!=null&&imgList.size()==0){
            imgList.add("http://i3.hoopchina.com.cn/blogfile/201403/31/BbsImg139626653396762_620*413.jpg");
//        drawableArrayList.add(R.drawable.img_man);
//        drawableArrayList.add(R.drawable.img_man);
        }
        // 设置数据
        shop_banner.setPages(imgList, new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });

        shop_banner.setIndicatorVisible(false);
        shop_banner.start();

    }

    public static class BannerViewHolder implements MZViewHolder<String> {
        private ImageView mImageView;
        @Override
        public View createView(Context context) {
            // 返回页面布局
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item,null);
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

    private void join(final String shopID){
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, Constants.JOINBRANCH, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(s.contains("true")){
                    reloadInfo();
                }else{
                    ToastUtils.showToastShort("加入失败！请检查网络！");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.e("zzf",volleyError.toString());
                ToastUtils.showToastShort("加入失败！请检查网络！");
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
                LogUtil.e("zzf",SPUtils.getString(Constants.MY_TOKEN,""));
                return map;
            }
        };

        requestQueue.add(stringRequest);

    }


    private void reloadInfo() {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.RELOAD_LOGININFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                Gson gson = new Gson();
                RequestLoginInfoBean loginInfoBean = gson.fromJson(s, RequestLoginInfoBean.class);
                if (loginInfoBean.getSuccess()) {
                    DataInfoCache.saveOneCache(loginInfoBean.getData(), Constants.MY_INFO);
                    myInfo = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
                    ToastUtils.showToastShort("加入成功！");
                    setResult(111);
                    finish();
                } else {
                    ToastUtils.showToastShort("加入失败！请检查网络！");
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToastShort("请求失败，请查看网络连接");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, null));
                // LogUtil.i("Bearer+"+SPUtils.getString(Constants.MY_TOKEN,null));
                return map;
            }

        };
        MyApplication.getHttpQueues().add(request);
    }


    private void getJiaolian(final String shopID){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.FIND_JIAOLIAN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.e("zzf",s);
                ShopJiaoLianBean shopJiaoLianBean = gson.fromJson(s, ShopJiaoLianBean.class);
                if(shopJiaoLianBean!=null){

                    jiaolianList = shopJiaoLianBean.getData();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> map = new HashMap<>();
                map.put("branch_id",shopID);

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

    private void getShopDetail(){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.BRANCH + "/"+branchID, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                ShopDetailBean shopDetailBean = gson.fromJson(s, ShopDetailBean.class);
                ShopDetailBean.Data data = shopDetailBean.getData();
                if (data!=null){
                    store_name.setText(data.getBname());
                    tv_adress.setText(data.getAddress());
                    //tv_detail.setText(data.getDescription());
                    tv_detail.setText("程序员(英文Programmer)是从事程序开发、程序维护的专业人员。一般将程序员分为程序设计人员和程序编码人员，但两者的界限并不非常清楚，特别是在中国。软件从业人员分为初级程序员、中级程序员、高级程序员（现为软件设计师）、系统分析员，系统架构师，测试工程师六大类。动岚健健身创办于2005年，做为全国连锁健身俱乐部，目前在北京市、河北省、河南省、山东省、广西省、广东省、山西省、四川省、江苏省、陕西省、甘肃、湖南. 湖北 已经拥有近50家连锁店，总营业面积已达6万平米，会员总量已达数十万人，员工总量已达2000人 短短7年就已发展到近50家连锁店，是全国发展最快的健身俱乐部之一。\n" +
                            "动岚健身全国连锁目前以三大形式进行发展");
                    phoneNo = data.getTelphone_no();
                    branchID = data.getBranch_id()+"";
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

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

    /**
     * 提示
     */
    private void showDialog() {
        AlertDialog.Builder dialog =
                new AlertDialog.Builder(ShopDetailedActivity.this);
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

    private class MyAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return 1;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            if(convertView==null){
                convertView = LayoutInflater.from(ShopDetailedActivity.this).inflate(R.layout.kecheng_parent,null);
            }

//            if(jiaolianList!=null){
//                if(jiaolianList.size()<=5){
//                    for(int i=0;i<jiaolianList.size();i++){
//
//
//                    }
//                }
//            }
            CircleImageView circle_1 = convertView.findViewById(R.id.circle_1);
            CircleImageView circle_2 = convertView.findViewById(R.id.circle_2);
            CircleImageView circle_3 = convertView.findViewById(R.id.circle_3);
            CircleImageView circle_4 = convertView.findViewById(R.id.circle_4);
            CircleImageView circle_5 = convertView.findViewById(R.id.circle_5);
            CircleImageView circle_6 = convertView.findViewById(R.id.circle_6);

            Glide.with(ShopDetailedActivity.this).load("http://news.hainan.net/Editor/img/201602/20160215/big/20160215234302136_2731088.jpg").into(circle_1);
            Glide.with(ShopDetailedActivity.this).load("http://img06.tooopen.com/images/20160807/tooopen_sy_174504721543.jpg").into(circle_2);
            Glide.with(ShopDetailedActivity.this).load("http://file06.16sucai.com/2016/0407/90ed68d09c8777d6336862beca17f317.jpg").into(circle_3);
            Glide.with(ShopDetailedActivity.this).load("http://img1.juimg.com/160622/330831-1606220TG086.jpg").into(circle_4);
            Glide.with(ShopDetailedActivity.this).load("http://img.mp.itc.cn/upload/20160408/6c46c0a65f32450e9941f9ef84091104_th.jpg").into(circle_5);
            Glide.with(ShopDetailedActivity.this).load("http://img1.juimg.com/160622/330831-1606220TG086.jpg").into(circle_6);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            if(convertView==null){
                convertView = LayoutInflater.from(ShopDetailedActivity.this).inflate(R.layout.kecheng_child,null);
            }
            CustomGridView grid_view = convertView.findViewById(R.id.grid_view);
            grid_view.setAdapter(new MyGridAdapter());

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }


    private class MyGridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 10;
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
            View inflate = LayoutInflater.from(ShopDetailedActivity.this).inflate(R.layout.kecheng_grid_item, null);
            CircleImageView circle_item = inflate.findViewById(R.id.circle_item);
            Glide.with(ShopDetailedActivity.this).load("http://pic1.win4000.com/wallpaper/d/58997071ac2b1.jpg").into(circle_item);

            return inflate;
        }
    }
}
