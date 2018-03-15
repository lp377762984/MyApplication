package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.baidu.mapapi.map.Text;
import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.ShopDetailBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.CustomGridView;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.NestedExpandaleListView;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.HashMap;
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
    String jingdu,weidu,shopJingdu,shopWeidu,branchID;
    RelativeLayout s_button;
    Data myInfo;
    ExpandableListView jiaolian_grid,huiji_grid;

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
        if(branchID==null){
            branchID = myInfo.getDefault_branch();
        }

    }

    private void initView() {

        img_kechenganpai = findViewById(R.id.img_kechenganpai);
        jiaolian_grid = findViewById(R.id.jiaolian_grid);
        huiji_grid = findViewById(R.id.huiji_grid);
        jiaolian_grid.setAdapter(new MyAdapter());
        huiji_grid.setAdapter(new MyAdapter());
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

        join_button = findViewById(R.id.join_button);
        if(branchID!=null){
            join_button.setVisibility(View.GONE);
        }

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
        s_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SellCardActivity.class);
                startActivity(intent);
            }
        });

        join_button.setOnClickListener(new View.OnClickListener() {
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

        getShopDetail();
    }


    private void join(final String shopID){
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, Constants.JOINBRANCH, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(s.contains("true")){
                    join_button.setVisibility(View.GONE);
                    ToastUtils.showToastShort("加入成功！");
                    myInfo.setDefault_branch(branchID);
                    DataInfoCache.saveOneCache(myInfo,Constants.MY_INFO);
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
                map.put("follow","true");
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

            CircleImageView circle_1 = convertView.findViewById(R.id.circle_1);
            CircleImageView circle_2 = convertView.findViewById(R.id.circle_2);
            CircleImageView circle_3 = convertView.findViewById(R.id.circle_3);
            CircleImageView circle_4 = convertView.findViewById(R.id.circle_4);
            CircleImageView circle_5 = convertView.findViewById(R.id.circle_5);

            Glide.with(ShopDetailedActivity.this).load("http://news.hainan.net/Editor/img/201602/20160215/big/20160215234302136_2731088.jpg").into(circle_1);
            Glide.with(ShopDetailedActivity.this).load("http://img06.tooopen.com/images/20160807/tooopen_sy_174504721543.jpg").into(circle_2);
            Glide.with(ShopDetailedActivity.this).load("http://file06.16sucai.com/2016/0407/90ed68d09c8777d6336862beca17f317.jpg").into(circle_3);
            Glide.with(ShopDetailedActivity.this).load("http://img1.juimg.com/160622/330831-1606220TG086.jpg").into(circle_4);
            Glide.with(ShopDetailedActivity.this).load("http://img.mp.itc.cn/upload/20160408/6c46c0a65f32450e9941f9ef84091104_th.jpg").into(circle_5);

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
