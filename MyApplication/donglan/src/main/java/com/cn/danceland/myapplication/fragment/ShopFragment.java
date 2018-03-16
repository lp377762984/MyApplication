package com.cn.danceland.myapplication.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.activity.AddFriendsActivity;
import com.cn.danceland.myapplication.activity.AdviseActivity;
import com.cn.danceland.myapplication.activity.BuySiJiaoActivity;
import com.cn.danceland.myapplication.activity.CabinetActivity;
import com.cn.danceland.myapplication.activity.CourseActivity;
import com.cn.danceland.myapplication.activity.HomeActivity;
import com.cn.danceland.myapplication.activity.MapActivity;
import com.cn.danceland.myapplication.activity.MyCardActivity;
import com.cn.danceland.myapplication.activity.MyDepositListActivity;
import com.cn.danceland.myapplication.activity.MyOrderActivity;
import com.cn.danceland.myapplication.activity.PotentialCustomerRevisitActivity;
import com.cn.danceland.myapplication.activity.RecommendActivity;
import com.cn.danceland.myapplication.activity.SellCardActivity;
import com.cn.danceland.myapplication.activity.ShopDetailedActivity;
import com.cn.danceland.myapplication.activity.StoreCardActivity;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.MenusBean;
import com.cn.danceland.myapplication.bean.RequestInfoBean;
import com.cn.danceland.myapplication.bean.ShopDetailBean;
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
    ListView storelist;
    GridView mGridView;
    String jingdu, weidu, shopJingdu, shopWeidu, branchId;
    String PhoneNo;
    Gson gson;
    Data info;
    List<StoreBean.Items> itemsList;
    ImageButton ibtn_call, ibtn_gps;
    List<MenusBean.Data> data;
    LinearLayout ll_top;
    TextView tv_shopname;
    View v;
    Spinner spinner;
    ArrayList<String> roleList;
    ArrayAdapter arrayAdapter;

    @Override
    public View initViews() {

        v = View.inflate(mActivity, R.layout.fragment_shop, null);

        info = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);

        roleList = new ArrayList<String>();
        spinner = v.findViewById(R.id.spinner);
        addRoles();
        arrayAdapter = new ArrayAdapter<String>(mActivity,R.layout.spinner_style,roleList);
        //arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        gson = new Gson();
        mGridView = v.findViewById(R.id.gridview);

        ibtn_call = v.findViewById(R.id.ibtn_call);
        ibtn_gps = v.findViewById(R.id.ibtn_gps);

        ll_top = v.findViewById(R.id.ll_top);
      
        ll_top.setBackgroundColor(Color.BLACK);
        ll_top.getBackground().setAlpha(88);
        tv_shopname = v.findViewById(R.id.tv_shopname);

        mGridView.setOnItemClickListener(new MyOnItemClickListener());
        storelist = v.findViewById(R.id.storelist);
        initData();

        v.findViewById(R.id.ibtn_call).setOnClickListener(this);
        v.findViewById(R.id.ibtn_gps).setOnClickListener(this);
        tv_shopname.setOnClickListener(this);
        // mGridView.setVisibility(View.VISIBLE);

        return v;
    }

    private void addRoles() {
        if(info!=null){
            List<Integer> roles = info.getRoles();
            if(roles!=null&&roles.size()>0){
                for(int i=0;i<roles.size();i++){
                    if(roles.get(i)==1){
                        roleList.add("会籍顾问");
                    }else if(roles.get(i)==2){
                        roleList.add("教练");
                    }else if(roles.get(i)==3){
                        roleList.add("前台");
                    }else if(roles.get(i)==4){
                        roleList.add("店长");
                    }else if(roles.get(i)==5){
                        roleList.add("会籍主管");
                    }else if(roles.get(i)==6){
                        roleList.add("教练主管");
                    }else if(roles.get(i)==7){
                        roleList.add("前台主管");
                    }else if(roles.get(i)==8){
                        roleList.add("操教");
                    }else if(roles.get(i)==9){
                        roleList.add("出纳");
                    }else if(roles.get(i)==10){
                        roleList.add("收银");
                    }else if(roles.get(i)==11){
                        roleList.add("兼职教练");
                    }
                }
            }
            if("1".equals(info.getAuth())){
                roleList.add("潜客");
            }else if("2".equals(info.getAuth())){
                roleList.add("会员");
            }
        }

    }

    public void initData() {
        if (info.getDefault_branch() != null && !info.getDefault_branch().equals("")) {
            getMenus();
            getShop(info.getDefault_branch());
            mGridView.setVisibility(View.VISIBLE);
            storelist.setVisibility(View.GONE);
            ll_top.setVisibility(View.VISIBLE);
        } else {
            getListData();
            ll_top.setVisibility(View.GONE);
            mGridView.setVisibility(View.GONE);
            storelist.setVisibility(View.VISIBLE);

        }
    }

    private void getShop(String shopID) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.BRANCH + "/" + shopID, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                ShopDetailBean shopDetailBean = gson.fromJson(s, ShopDetailBean.class);
                ShopDetailBean.Data data = shopDetailBean.getData();
                if (data != null) {
                    tv_shopname.setText(data.getBname());
                    shopWeidu = data.getLat() + "";
                    shopJingdu = data.getLng() + "";
                    PhoneNo = data.getTelphone_no();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

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

    private void getMenus() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.GETMENUS, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                if (s.contains("true")) {
                    MenusBean menusBean = gson.fromJson(s, MenusBean.class);
                    data = menusBean.getData();
                    if (data != null) {
                        LogUtil.i(data.toString());
                        mGridView.setAdapter(new MyAdapter(data));
                    }
                } else {
                    ToastUtils.showToastShort("请查看网络连接");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ll_top.setVisibility(View.GONE);
                ToastUtils.showToastShort("请查看网络连接");
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        String getlocationString = ((HomeActivity) activity).getlocationString();
        if (getlocationString != null) {
            jingdu = getlocationString.split(",")[0];
            weidu = getlocationString.split(",")[1];
        }
    }

    public void getListData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.BRANCH + "/0/" + weidu + "/" + jingdu, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                StoreBean storeBean = gson.fromJson(s, StoreBean.class);
                if(storeBean!=null&&storeBean.getData()!=null){
                    itemsList = storeBean.getData().getItems();
                    if (itemsList != null && itemsList.size() > 0) {
                        storelist.setAdapter(new MyStoreAdapter(getActivity(), itemsList));
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            info = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
            initData();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_call:
                showDialog(PhoneNo);
                break;
            case R.id.ibtn_gps:
                Intent intent = new Intent(getActivity(), MapActivity.class);
                intent.putExtra("jingdu", jingdu);
                intent.putExtra("weidu", weidu);
                intent.putExtra("shopJingdu", shopJingdu);
                intent.putExtra("shopWeidu", shopWeidu);
                startActivity(intent);

                break;
            case R.id.tv_shopname:
                Intent intent1 = new Intent(getActivity(), ShopDetailedActivity.class);
                intent1.putExtra("jingdu", jingdu);
                intent1.putExtra("weidu", weidu);
                intent1.putExtra("shopJingdu", shopJingdu);
                intent1.putExtra("shopWeidu", shopWeidu);
                startActivity(intent1);
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

            if (data != null) {
                if (data.get(i).getId() == 1) {
                    //id为1,表示在线售卡
                    startActivity(new Intent(mActivity, SellCardActivity.class));
                }
                switch (data.get(i).getId()) {
                    case 2://我的会员卡
                        startActivity(new Intent(mActivity, MyCardActivity.class));
                        break;
                    case 4://购买私教
                        startActivity(new Intent(mActivity, BuySiJiaoActivity.class));
                        break;
                    case 6://预约私教
                        startActivity(new Intent(mActivity, CourseActivity.class).putExtra("isTuanke","1"));
                        break;
                    case 20://预约团课
                        startActivity(new Intent(mActivity, CourseActivity.class).putExtra("isTuanke","0"));
                        break;
                    case 10://我的定金
                        startActivity(new Intent(mActivity, MyDepositListActivity.class));
                        break;
                    case 11://我的订单
                        startActivity(new Intent(mActivity, MyOrderActivity.class));
                        break;
                    case 12://体测
                        Intent intent = new Intent(mActivity, AddFriendsActivity.class);
                        intent.putExtra("from", "体测");
                        startActivity(intent);
                        break;
                    case 13://潜客维护
                        startActivity(new Intent(mActivity, PotentialCustomerRevisitActivity.class).putExtra("auth", "1"));
                        break;
                    case 14://会员维护
                        startActivity(new Intent(mActivity, PotentialCustomerRevisitActivity.class).putExtra("auth", "2"));
                        //  startActivity(new Intent(mActivity, MyOrderActivity.class));
                        break;
                    case 15://待办事项
                        //    startActivity(new Intent(mActivity, MyOrderActivity.class));
                        break;
                    case 16://我的租柜
                        startActivity(new Intent(mActivity, CabinetActivity.class));
                        break;
                    case 18://意见反馈
                        startActivity(new Intent(mActivity, AdviseActivity.class));
                        break;
                    case 17://推荐好友
                        startActivity(new Intent(mActivity, RecommendActivity.class));
                        break;
                    case 21:
                        startActivity(new Intent(mActivity, StoreCardActivity.class));
                        break;
                    default:
                        break;
                }
            }

        }
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
                            if (itemsArrayList != null) {
                                join(itemsArrayList.get(position).getBranch_id() + "");
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
                    if (itemsArrayList != null) {
                        Intent intent = new Intent(getActivity(), ShopDetailedActivity.class);
                        intent.putExtra("shopWeidu", itemsArrayList.get(position).getLat() + "");
                        intent.putExtra("shopJingdu", itemsArrayList.get(position).getLng() + "");
                        intent.putExtra("jingdu", jingdu);
                        intent.putExtra("weidu", weidu);
                        intent.putExtra("branchID", itemsArrayList.get(position).getBranch_id() + "");
                        startActivityForResult(intent, 111);
                    }
                }
            });

            return convertView;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 111) {

            initViews();
        }

    }

    private void join(final String shopID) {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, Constants.JOINBRANCH, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s.contains("true")) {
                    login(shopID);
                } else {
                    ToastUtils.showToastShort("加入失败！请检查网络！");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.e("zzf",volleyError.toString());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("branchId", shopID);
                map.put("join", "true");
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

    class ViewHolder {
        ImageView store_item_img, img_location, img_phone, img_join;
        TextView store_address, distance;
        RelativeLayout clickitem;
    }


    class MyAdapter extends BaseAdapter {

        List<MenusBean.Data> menuList;

        MyAdapter(List<MenusBean.Data> list) {
            menuList = list;
        }


        @Override
        public int getCount() {
            return menuList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            view = View.inflate(mActivity, R.layout.gridview_item_shop, null);
            TextView tv_dcs = view.findViewById(R.id.tv_dcs);
            ImageView ibtn = view.findViewById(R.id.ibtn);
            tv_dcs.setText(menuList.get(i).getName());
            Glide.with(mActivity).load(menuList.get(i).getIcon()).into(ibtn);
            return view;
        }
    }

    private void login(final String shopID) {
        String url = Constants.LOGIN_URL;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                RequestInfoBean requestInfoBean = new RequestInfoBean();
                requestInfoBean = gson.fromJson(s, RequestInfoBean.class);
                if (requestInfoBean.getSuccess()) {
                    SPUtils.setString(Constants.MY_TOKEN, "Bearer+" + requestInfoBean.getData().getToken());
                    //成功
                    String mUserId = requestInfoBean.getData().getPersonId();
                    info.setDefault_branch(shopID);
                    DataInfoCache.saveOneCache(info, Constants.MY_INFO);
                    if (info.getDefault_branch() != null && !info.getDefault_branch().equals("")) {
                        mGridView.setVisibility(View.VISIBLE);
                        storelist.setVisibility(View.GONE);
                        ll_top.setVisibility(View.VISIBLE);
                        getMenus();
                        getShop(info.getDefault_branch());

                    } else {
                        ll_top.setVisibility(View.GONE);
                        mGridView.setVisibility(View.GONE);
                        storelist.setVisibility(View.VISIBLE);
                        getListData();

                    }
                    //查询信息
                    queryUserInfo(mUserId);
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("name", info.getPhone());
                map.put("password", SPUtils.getString(Constants.MY_PSWD,""));
                // map.put("romType", "0");
                return map;
            }
        };

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("login");
        // 设置超时时间
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);
    }

    private void queryUserInfo(String id) {

        String params = id;

        String url = Constants.QUERY_USERINFO_URL + params;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                Gson gson = new Gson();
                RequestInfoBean requestInfoBean = gson.fromJson(s, RequestInfoBean.class);

                //      LogUtil.i(requestInfoBean.toString());
//                ArrayList<Data> mInfoBean = new ArrayList<>();
//                mInfoBean.add(requestInfoBean.getData());
//                DataInfoCache.saveListCache(mInfoBean, Constants.MY_INFO);
                //保存个人信息
                Data data = requestInfoBean.getData();
                DataInfoCache.saveOneCache(data, Constants.MY_INFO);
                SPUtils.setBoolean(Constants.ISLOGINED, true);//保存登录状态
                LogUtil.i(DataInfoCache.loadOneCache(Constants.MY_INFO).toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                LogUtil.i(volleyError.toString());

            }

        }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, null));
                // LogUtil.i("Bearer+"+SPUtils.getString(Constants.MY_TOKEN,null));
                return map;
            }
        };
        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("queryUserInfo");
        // 设置超时时间
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);

    }

}
