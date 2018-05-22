package com.cn.danceland.myapplication.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
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
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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
import com.cn.danceland.myapplication.activity.AddFriendsActivity;
import com.cn.danceland.myapplication.activity.AdviseActivity;
import com.cn.danceland.myapplication.activity.AllReportActivity;
import com.cn.danceland.myapplication.activity.BuySiJiaoActivity;
import com.cn.danceland.myapplication.activity.CabinetActivity;
import com.cn.danceland.myapplication.activity.ClubDynActivity;
import com.cn.danceland.myapplication.activity.CourseActivity;
import com.cn.danceland.myapplication.activity.FitnessTestActivity;
import com.cn.danceland.myapplication.activity.HomeActivity;
import com.cn.danceland.myapplication.activity.LoginNumberActivity;
import com.cn.danceland.myapplication.activity.MapActivity;
import com.cn.danceland.myapplication.activity.MyCardActivity;
import com.cn.danceland.myapplication.activity.MyDepositListActivity;
import com.cn.danceland.myapplication.activity.MyOrderActivity;
import com.cn.danceland.myapplication.activity.MySijiaoActivity;
import com.cn.danceland.myapplication.activity.PotentialCustomerRevisitActivity;
import com.cn.danceland.myapplication.activity.RecommendActivity;
import com.cn.danceland.myapplication.activity.ReportFormActivity;
import com.cn.danceland.myapplication.activity.ScanerCodeActivity;
import com.cn.danceland.myapplication.activity.SellCardActivity;
import com.cn.danceland.myapplication.activity.ShopDetailedActivity;
import com.cn.danceland.myapplication.activity.StoreCardActivity;
import com.cn.danceland.myapplication.bean.BranchBannerBean;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.MenusBean;
import com.cn.danceland.myapplication.bean.RequestLoginInfoBean;
import com.cn.danceland.myapplication.bean.RolesBean;
import com.cn.danceland.myapplication.bean.ShopDetailBean;
import com.cn.danceland.myapplication.bean.StoreBean;
import com.cn.danceland.myapplication.evntbus.StringEvent;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

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
    TextView tv_shopname,tv_role;
    View v;
    ArrayList<String> roleList;
    ArrayAdapter arrayAdapter;
    String role;
    List<Data.Roles> roles;
    HashMap<String, String> roleMap, authMap;
    MZBannerView shop_banner;
    ArrayList<String> drawableArrayList;
    PopupWindow popupWindow;
    RelativeLayout rl_role;
    ImageView down_img,up_img;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册event事件
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //even事件处理
    @Subscribe
    public void onEventMainThread(StringEvent event) {
        if (event.getEventCode() == 20001) {
            refresh();
        }
    }

    @Override
    public View initViews() {

        v = View.inflate(mActivity, R.layout.fragment_shop, null);

        info = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);

        roleList = new ArrayList<String>();
        shop_banner = v.findViewById(R.id.shop_banner);
        tv_role = v.findViewById(R.id.tv_role);

        rl_role = v.findViewById(R.id.rl_role);
        down_img = v.findViewById(R.id.down_img);
        up_img = v.findViewById(R.id.up_img);


        setMap();
        addRoles();
        setPop();
        rl_role.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popupWindow.isShowing()){
                    dismissPop();
                    up_img.setVisibility(View.GONE);
                    down_img.setVisibility(View.VISIBLE);
                }else{
                    showPop();
                    up_img.setVisibility(View.VISIBLE);
                    down_img.setVisibility(View.GONE);
                }
            }
        });
        //setSpinner();
        if(roleList!=null&&roleList.size()>0){
            tv_role.setText(roleList.get(0));
            role = roleList.get(0);
        }

        gson = new Gson();
        mGridView = v.findViewById(R.id.gridview);

        ibtn_call = v.findViewById(R.id.ibtn_call);
        ibtn_gps = v.findViewById(R.id.ibtn_gps);

        ll_top = v.findViewById(R.id.ll_top);
        tv_shopname = v.findViewById(R.id.tv_shopname);
        drawableArrayList = new ArrayList<>();
        mGridView.setOnItemClickListener(new MyOnItemClickListener());
        //storelist = v.findViewById(R.id.storelist);
        initData();

        v.findViewById(R.id.ibtn_call).setOnClickListener(this);
        v.findViewById(R.id.ibtn_gps).setOnClickListener(this);
        tv_shopname.setOnClickListener(this);

//        drawableArrayList.add(R.drawable.img_man);
//        drawableArrayList.add(R.drawable.img_man);
//        drawableArrayList.add(R.drawable.img_man);
        // mGridView.setVisibility(View.VISIBLE);

        return v;
    }

    private void setPop(){

        View inflate = LayoutInflater.from(mActivity).inflate(R.layout.shop_pop, null);

        popupWindow = new PopupWindow(inflate);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        ListView pop_lv = inflate.findViewById(R.id.pop_lv);
        pop_lv.setAdapter(new PopAdapter());
        pop_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                role = roleList.get(position);
                tv_role.setText(role);
                initData();
                dismissPop();
                up_img.setVisibility(View.GONE);
                down_img.setVisibility(View.VISIBLE);
            }
        });


    }
    private void showPop(){
        popupWindow.showAsDropDown(tv_role);
    }
    private  void dismissPop(){
        popupWindow.dismiss();
    }


    private class PopAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return roleList ==null? 0:roleList.size();
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
            View inflate = LayoutInflater.from(mActivity).inflate(R.layout.shop_pop_item, null);
            TextView tv_item = inflate.findViewById(R.id.tv_item);
            tv_item.setText(roleList.get(position));

            return inflate;
        }
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
        if(drawableArrayList!=null&&drawableArrayList.size()==0){
            drawableArrayList.add("http://i3.hoopchina.com.cn/blogfile/201403/31/BbsImg139626653396762_620*413.jpg");
//        drawableArrayList.add(R.drawable.img_man);
//        drawableArrayList.add(R.drawable.img_man);
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

    private void setMap() {
        roleMap = new HashMap<>();
        authMap = new HashMap<>();
        roleMap.put("会籍顾问", "1");
        roleMap.put("教练", "2");
        roleMap.put("前台", "3");
        roleMap.put("店长", "4");
        roleMap.put("会籍主管", "5");
        roleMap.put("教练主管", "6");
        roleMap.put("前台主管", "7");
        roleMap.put("操教", "8");
        roleMap.put("出纳", "9");
        roleMap.put("收银", "10");
        roleMap.put("兼职教练", "11");

        authMap.put("准会员", "1");
        authMap.put("会员", "2");

    }

    public void refresh() {
        if (myAdapter != null) {
            myAdapter.notifyDataSetChanged();
            //     LogUtil.i("刷新");
        }

    }

    private void addRoles() {
        if (info != null) {
            roles = info.getRoles();
            if (roles != null && roles.size() > 0) {
                for (int i = 0; i < roles.size(); i++) {
                    if (roles.get(i).getRole_type() == 1) {
                        roleList.add("会籍顾问");
                    } else if (roles.get(i).getRole_type() == 2) {
                        roleList.add("教练");
                    } else if (roles.get(i).getRole_type() == 3) {
                        roleList.add("前台");
                    } else if (roles.get(i).getRole_type() == 4) {
                        roleList.add("店长");
                    } else if (roles.get(i).getRole_type() == 5) {
                        roleList.add("会籍主管");
                    } else if (roles.get(i).getRole_type() == 6) {
                        roleList.add("教练主管");
                    } else if (roles.get(i).getRole_type() == 7) {
                        roleList.add("前台主管");
                    } else if (roles.get(i).getRole_type() == 8) {
                        roleList.add("操教");
                    } else if (roles.get(i).getRole_type() == 9) {
                        roleList.add("出纳");
                    } else if (roles.get(i).getRole_type() == 10) {
                        roleList.add("收银");
                    } else if (roles.get(i).getRole_type() == 11) {
                        roleList.add("兼职教练");
                    }
                }
            }

            if (info.getMember() != null) {
                if ("1".equals(info.getMember().getAuth())) {
                    roleList.add("准会员");
                } else if ("2".equals(info.getMember().getAuth())) {
                    roleList.add("会员");
                }
            }
        }

    }

    public void initData() {
        branchId = info.getPerson().getDefault_branch();
        if (info.getPerson().getDefault_branch() != null && !info.getPerson().getDefault_branch().equals("")) {
            getMenus();
            getBanner(info.getPerson().getDefault_branch());
            getShop(info.getPerson().getDefault_branch());
        }
    }

    private void getBanner(final String branchId){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BANNER, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                BranchBannerBean branchBannerBean = gson.fromJson(s, BranchBannerBean.class);
                if(branchBannerBean!=null){
                    List<BranchBannerBean.Data> data = branchBannerBean.getData();
                    if(data!=null){
                        for(int i = 0;i<data.size();i++){
                            drawableArrayList.add(data.get(i).getImg_url());
                        }
                        setBannner();
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
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("branchId",branchId);
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

    MyAdapter myAdapter;

    private void getMenus() {
        final String id;
        String url;
        RolesBean rolesBean = new RolesBean();
        if (role != null) {
            if (!"准会员".equals(role) && !"会员".equals(role)) {
                rolesBean.setRole_type(roleMap.get(role));
                //id = roleMap.get(role);
                url = Constants.GETYUANGONGMENUS;
                String s = gson.toJson(rolesBean);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, s, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        LogUtil.e("zzf", jsonObject.toString());
                        if (jsonObject.toString().contains("true")) {
                            MenusBean menusBean = gson.fromJson(jsonObject.toString(), MenusBean.class);
                            data = menusBean.getData();
                            if (data != null) {
                                LogUtil.i(data.toString());
                                myAdapter = new MyAdapter(data);
                                mGridView.setAdapter(myAdapter);
                            }
                        } else {
                            ToastUtils.showToastShort("请查看网络连接");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        ToastUtils.showToastShort("请查看网络连接");
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

                MyApplication.getHttpQueues().add(jsonObjectRequest);

            } else {
                id = authMap.get(role);
                url = Constants.GETHUIYUANMENUS;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        LogUtil.e("zzf", s);
                        if (s.contains("true")) {
                            MenusBean menusBean = gson.fromJson(s, MenusBean.class);
                            data = menusBean.getData();
                            if (data != null) {
                                LogUtil.i(data.toString());
                                myAdapter = new MyAdapter(data);
                                mGridView.setAdapter(myAdapter);
                            }
                        } else {
                            ToastUtils.showToastShort("请查看网络连接");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        ToastUtils.showToastShort("请查看网络连接");
                        LogUtil.e("zzf", volleyError.toString());
                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("role_type", id);
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
        }

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

//    public void getListData() {
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.BRANCH + "/0/" + weidu + "/" + jingdu, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                StoreBean storeBean = gson.fromJson(s, StoreBean.class);
//                if (storeBean != null && storeBean.getData() != null) {
//                    itemsList = storeBean.getData().getItems();
//                    if (itemsList != null && itemsList.size() > 0) {
//                        storelist.setAdapter(new MyStoreAdapter(getActivity(), itemsList));
//                    }
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                LogUtil.e("zzf", volleyError.toString());
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> map = new HashMap<String, String>();
//                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));
//                return map;
//            }
//        };
//
//        MyApplication.getHttpQueues().add(stringRequest);
//
//    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            info = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
            roleList.clear();
            role = SPUtils.getString("role", "");
            tv_role.setText(role);
            setMap();
            addRoles();
            setPop();
            //setSpinner();
            initData();
        }else{
            SPUtils.setString("role",role);
            dismissPop();
            up_img.setVisibility(View.GONE);
            down_img.setVisibility(View.VISIBLE);
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
                intent1.putExtra("branchID",branchId);
                intent1.putStringArrayListExtra("imgList",drawableArrayList);
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
                    case 5://会所动态
                        startActivity(new Intent(mActivity, ClubDynActivity.class));
                        break;
                    case 6://预约私教
                        Intent intent1 = new Intent(mActivity, CourseActivity.class);
                        intent1.putExtra("isTuanke", "1");
                        if (role != null && !role.equals("准会员") && !role.equals("会员")) {
                            intent1.putExtra("role", role);
                        } else {
                            intent1.putExtra("auth", role);
                        }
                        startActivity(intent1);
                        break;
                    case 9://意见反馈
                        //startActivity(new Intent(mActivity, LoginNumberActivity.class));
                        startActivity(new Intent(mActivity, AdviseActivity.class));
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

                    case 25://推荐好友
                        startActivity(new Intent(mActivity, RecommendActivity.class));
                        break;
                    case 18://意见反馈
                        startActivity(new Intent(mActivity, AdviseActivity.class));
                        break;
                    case 19://扫码入场


                        startActivity(new Intent(mActivity, ScanerCodeActivity.class));
                        break;

                    case 20://预约团课
                        startActivity(new Intent(mActivity, CourseActivity.class).putExtra("isTuanke", "0"));
                        break;

                    case 21://储值卡
                        startActivity(new Intent(mActivity, StoreCardActivity.class));
                        break;
                    case 23://会籍报表
                        startActivity(new Intent(mActivity, ReportFormActivity.class).putExtra("role_type",role).putExtra("target_role_type","1"));
                        break;
                    case 24://全店报表
                        startActivity(new Intent(mActivity, AllReportActivity.class).putExtra("role_type",role).putExtra("target_role_type","4"));
                        break;
                    case 26://体测分析
                        startActivity(new Intent(mActivity, AddFriendsActivity.class).putExtra("from","体测").putExtra("isAnalysis","true"));
                        break;
                    case 29://私信
                   //     startActivity(new Intent(mActivity, MyChatListActivity.class));
                        break;
                    case 30://教练报表
                        startActivity(new Intent(mActivity, ReportFormActivity.class).putExtra("role_type",role).putExtra("target_role_type","2"));
                        break;
                    case 31://我的私教
                        startActivity(new Intent(mActivity, MySijiaoActivity.class));
                        break;
                    case 33://预约会员
                        Intent intent2 = new Intent(mActivity, CourseActivity.class);
                        intent2.putExtra("isTuanke", "1");
                        if (role != null && !role.equals("准会员") && !role.equals("会员")) {
                            intent2.putExtra("role", role);
                        } else {
                            intent2.putExtra("auth", role);
                        }
                        startActivity(intent2);
                        break;
                    case 34://我的体测

                        startActivity(new Intent(mActivity, FitnessTestActivity.class));

                        break;
                    default:
                        break;
                }
            }

        }
    }

//    public class MyStoreAdapter extends BaseAdapter {
//        Context mContext;
//        List<StoreBean.Items> itemsArrayList;
//
//        MyStoreAdapter(Context context, List<StoreBean.Items> list) {
//            mContext = context;
//            itemsArrayList = list;
//        }
//
//        @Override
//        public int getCount() {
//            return itemsArrayList.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            ViewHolder viewHolder = null;
//            StoreBean.Items items;
//
//            if (convertView == null) {
//                viewHolder = new ViewHolder();
//                convertView = View.inflate(mActivity, R.layout.store_item, null);
//                viewHolder.store_item_img = convertView.findViewById(R.id.store_item_img);
//                viewHolder.store_address = convertView.findViewById(R.id.store_address);
//                viewHolder.distance = convertView.findViewById(R.id.distance);
//                viewHolder.img_location = convertView.findViewById(R.id.img_location);
//                viewHolder.img_phone = convertView.findViewById(R.id.img_phone);
//                viewHolder.img_join = convertView.findViewById(R.id.img_join);
//                viewHolder.clickitem = convertView.findViewById(R.id.clickitem);
//                //iewById(R.id.unread_msg_number);
//                convertView.setTag(viewHolder);
//            } else {
//                viewHolder = (ViewHolder) convertView.getTag();
//            }
//
//
//            if (itemsArrayList != null) {
//                items = itemsArrayList.get(position);
//                viewHolder.store_address.setText(items.getBname());
//                viewHolder.distance.setText(items.getAddress());
//                Glide.with(getActivity()).load("http://img0.imgtn.bdimg.com/it/u=2269433745,3578312737&fm=214&gp=0.jpg").into(viewHolder.store_item_img);
//                //PhoneNo = items.getTelphone_no();
////                shopJingdu = items.getLat()+"";
////                shopWeidu = items.getLng()+"";
////                branchId = items.getBranch_id()+"";
//            }
//            viewHolder.img_phone.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (itemsArrayList != null) {
//                        showDialog(itemsArrayList.get(position).getTelphone_no());
//                    }
//                }
//            });
//            viewHolder.img_location.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (itemsArrayList != null) {
//                        Intent intent = new Intent(getActivity(), MapActivity.class);
//                        intent.putExtra("shopWeidu", itemsArrayList.get(position).getLat() + "");
//                        intent.putExtra("shopJingdu", itemsArrayList.get(position).getLng() + "");
//                        intent.putExtra("jingdu", jingdu);
//                        intent.putExtra("weidu", weidu);
//                        startActivity(intent);
//                    }
//                }
//            });
//            viewHolder.img_join.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    AlertDialog.Builder dialog =
//                            new AlertDialog.Builder(mActivity);
//                    dialog.setTitle("提示");
//                    dialog.setMessage("是否加入此门店");
//                    dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            if (itemsArrayList != null) {
//                                join(itemsArrayList.get(position).getBranch_id() + "");
//                            }
//
//                        }
//                    });
//                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//
//                        }
//                    });
//                    dialog.show();
//                }
//            });
//            viewHolder.clickitem.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (itemsArrayList != null) {
//                        Intent intent = new Intent(getActivity(), ShopDetailedActivity.class);
//                        intent.putExtra("shopWeidu", itemsArrayList.get(position).getLat() + "");
//                        intent.putExtra("shopJingdu", itemsArrayList.get(position).getLng() + "");
//                        intent.putExtra("jingdu", jingdu);
//                        intent.putExtra("weidu", weidu);
//                        intent.putExtra("branchID", itemsArrayList.get(position).getBranch_id() + "");
//                        startActivityForResult(intent, 111);
//                    }
//                }
//            });
//
//            return convertView;
//        }
//    }

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
                    reloadInfo();
                    //  login(shopID);
                } else {
                    ToastUtils.showToastShort("加入失败！请检查网络！");
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
        TextView store_address, distance, unread_msg_number;
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
            TextView unread_msg_number = view.findViewById(R.id.unread_msg_number);
            if (menuList.get(i).getId() == 29) {
                //    LogUtil.i(EMClient.getInstance().chatManager().getUnreadMsgsCount()+"");
//                if (EMClient.getInstance().chatManager().getUnreadMsgsCount() == 0) {
//                    unread_msg_number.setVisibility(View.GONE);
//                } else {
//                    unread_msg_number.setVisibility(View.VISIBLE);
//                    unread_msg_number.setText(EMClient.getInstance().chatManager().getUnreadMsgsCount() + "");
//                }
            }


            tv_dcs.setText(menuList.get(i).getName());
            Glide.with(mActivity).load(menuList.get(i).getIcon()).into(ibtn);
            return view;
        }
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
                    info = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
                    setMap();
                    addRoles();
                    setPop();
                    //setSpinner();
                    initData();
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


}
