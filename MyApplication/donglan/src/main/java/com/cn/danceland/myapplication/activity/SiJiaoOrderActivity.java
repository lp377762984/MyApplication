package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.BuySiJiaoBean;
import com.cn.danceland.myapplication.bean.CommitDepositBean;
import com.cn.danceland.myapplication.bean.DLResult;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.JiaoLianBean;
import com.cn.danceland.myapplication.bean.RequestOrderInfoBean;
import com.cn.danceland.myapplication.bean.RequestSimpleBean;
import com.cn.danceland.myapplication.bean.SijiaoOrderConfirmBean;
import com.cn.danceland.myapplication.bean.explain.Explain;
import com.cn.danceland.myapplication.bean.explain.ExplainCond;
import com.cn.danceland.myapplication.bean.explain.ExplainRequest;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.TimeUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by feng on 2018/1/15.
 */

public class SiJiaoOrderActivity extends Activity {
    private static final int PICK_CONTACT = 0;
    String type;
    EditText ed_phone,ed_name;
    LinearLayout rl_jiaolian,rl_kaikeshijian,rl_name,rl_phone,ll_dingjin;
    RadioButton btn_forme,btn_foryou;
    CheckBox btn_zhifubao,btn_weixin,btn_chuzhika;
    View line7;
    ImageView back_img,iv_phonebook;
    BuySiJiaoBean.Content itemContent;
    TextView goods_name,goods_type,goods_time,goods_price,tv_jiaolian,ed_time,tv_pay_price,tv_dingjin;
    ListPopup listPopup;
    List<JiaoLianBean.Data> JiaoLianList;
    Gson gson;
    int course_category;
    String forme = "0",zhifu,syear,smonth,sdate,strTime;
    int branch_id,year,daysByYearMonth;
    AlertDialog.Builder alertdialog;
    LoopView loopview,lp_year,lp_month,lp_date;
    View inflate1;
    int nowyear,month,monthDay,days,time_length;
    String toMonth,toYear,endTime;
    int employee_id;
    String employee_name;
    String course_category_name,course_name;
    Button btn_commit;
    int price;
    Data info;
    PayBean payBean;
    Long startMill;
    Long endMill;
    int course_id;
    int dingjinprice = 100;
    String deposit_id;
    float deposit;
    TextView tv_explain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sijiaoorder);
        initHost();
        initView();
        queryList();
    }

    /**
     * @方法说明:按条件查询说明须知列表
     **/
    public void queryList() {
        ExplainRequest request = new ExplainRequest();
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        ExplainCond cond = new ExplainCond();
        cond.setBranch_id(Long.valueOf(info.getPerson().getDefault_branch()));
        cond.setType(Byte.valueOf("5"));// 1 买卡须知 2 买私教须知 3 买储值须知 4 买卡说明 5 买私教说明

        request.queryList(cond, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject json) {
                DLResult<List<Explain>> result = gson.fromJson(json.toString(), new TypeToken<DLResult<List<Explain>>>() {
                }.getType());
                if (result.isSuccess()) {
                    List<Explain> list = result.getData();
                    if(list!=null&&list.size()>0){
                        tv_explain.setText(list.get(0).getContent());
                    }
                } else {
                    ToastUtils.showToastShort("查询分页列表失败,请检查手机网络！");
                }
            }
        });
    }

    private void initHost() {

        Time time = new Time();
        time.setToNow();
        nowyear = time.year;
        month = time.month+1;
        monthDay = time.monthDay;

        info = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
        payBean = new PayBean();

        gson = new Gson();
        type = getIntent().getStringExtra("type");
        itemContent = (BuySiJiaoBean.Content)getIntent().getSerializableExtra("itemContent");
        if(itemContent!=null){
            course_category_name = itemContent.getCourse_category_name();
            course_id = itemContent.getId();
            course_category = itemContent.getCourse_category();
            course_name = itemContent.getName();
            branch_id = itemContent.getBranch_id();
            days = itemContent.getDays();
            price = itemContent.getPrice();
            time_length = itemContent.getTime_length();
        }
        inflate1 = LayoutInflater.from(SiJiaoOrderActivity.this).inflate(R.layout.birthdayselect,null);
        lp_year = inflate1.findViewById(R.id.lp_year);
        lp_month  = inflate1.findViewById(R.id.lp_month);
        lp_date = inflate1.findViewById(R.id.lp_date);
        alertdialog = new AlertDialog.Builder(SiJiaoOrderActivity.this);

    }

    private void getJiaoLian(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.FINDCourseTypeEmployee, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.e("zzf",s);
                JiaoLianBean jiaoLianBean = gson.fromJson(s, JiaoLianBean.class);
                if(jiaoLianBean!=null){
                    JiaoLianList = jiaoLianBean.getData();
                    listPopup = new ListPopup(SiJiaoOrderActivity.this);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("courseTypeId",course_id+"");
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<String,String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN,""));

                return map;
            }
        };

        MyApplication.getHttpQueues().add(stringRequest);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_CONTACT){
            if (resultCode == Activity.RESULT_OK) {
                Uri contactData = data.getData();
                Cursor c = managedQuery(contactData, null, null, null, null);
                if (c.moveToFirst()) {
                    String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                    String phoneNumber = null;
                    if (hasPhone.equalsIgnoreCase("1")) {
                        hasPhone = "true";
                    } else {
                        hasPhone = "false";
                    }
                    if (Boolean.parseBoolean(hasPhone)) {
                        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                                        + contactId,
                                null,
                                null);
                        while (phones.moveToNext()) {
                            phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            //将电话放入
                            ed_phone.setText(phoneNumber.trim().replace(" ", ""));
                            if (TextUtils.isEmpty(ed_name.getText())) {
                                ed_name.setText(name);
                            }

                        }
                        phones.close();
                    }


                }
            }

        }else if(resultCode == 11){
            deposit = data.getFloatExtra("dingjin",0);
            deposit_id = data.getStringExtra("id");
            tv_dingjin.setText("- "+deposit+"元");
            tv_pay_price.setText("￥"+(price-deposit));
        }
    }

    private void initView() {

        rl_jiaolian = findViewById(R.id.rl_jiaolian);
        tv_explain = findViewById(R.id.tv_explain);
        tv_pay_price = findViewById(R.id.tv_pay_price);
        ed_time = findViewById(R.id.ed_time);
        ed_time.setText(nowyear+"年"+month+"月"+monthDay+"日");
        strTime = nowyear+"-"+month+"-"+monthDay;
        startMill = TimeUtils.date2TimeStamp(strTime, "yyyy-MM-dd");
        endMill = (long)days*86400000+startMill;
        endTime = TimeUtils.timeStamp2Date(endMill+"","yyyy-MM-dd");

        btn_forme = findViewById(R.id.btn_forme);
        btn_foryou = findViewById(R.id.btn_foryou);
        rl_kaikeshijian = findViewById(R.id.rl_kaikeshijian);
        rl_phone = findViewById(R.id.rl_phone);
        rl_name = findViewById(R.id.rl_name);
        line7 = findViewById(R.id.line7);
        btn_zhifubao = findViewById(R.id.btn_zhifubao);
        btn_weixin = findViewById(R.id.btn_weixin);
        btn_chuzhika = findViewById(R.id.btn_chuzhika);
        back_img = findViewById(R.id.back_img);
        ed_phone = findViewById(R.id.ed_phone);
        ed_name = findViewById(R.id.ed_name);
        goods_name = findViewById(R.id.goods_name);
        goods_type = findViewById(R.id.goods_type);
        goods_time = findViewById(R.id.goods_time);
        goods_price = findViewById(R.id.goods_price);
//        goods_all_price = findViewById(R.id.goods_all_price);
//        goods_num = findViewById(R.id.goods_num);
        tv_jiaolian = findViewById(R.id.tv_jiaolian);
        btn_commit = findViewById(R.id.btn_commit);
        ll_dingjin = findViewById(R.id.ll_dingjin);
        tv_dingjin = findViewById(R.id.tv_dingjin);

        ll_dingjin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(SiJiaoOrderActivity.this,DepositActivity.class).putExtra("bus_type","3"),22);
            }
        });


        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if("0".equals(type)){
                        if("0".equals(forme)){
                            if(tv_jiaolian.getText().toString().equals("请选择您的教练")||tv_jiaolian.getText().toString().isEmpty()){
                                ToastUtils.showToastShort("请选择教练！");
                            }else{
                                confirmOrder();
                            }
                        }else if("1".equals(forme)){
                            if (tv_jiaolian.getText().toString().equals("请选择您的教练")||tv_jiaolian.getText().toString().isEmpty()||ed_name.getText().toString().isEmpty()||ed_phone.getText().toString().isEmpty()){
                                ToastUtils.showToastShort("请补全订单信息！");
                            }else{
                                confirmOrder();
                            }
                        }

                    }else if("1".equals(type)){
                        if("1".equals(forme)){
                            if (tv_jiaolian.getText().toString().equals("请选择您的教练")||tv_jiaolian.getText().toString().isEmpty()||ed_name.getText().toString().isEmpty()||ed_phone.getText().toString().isEmpty()){
                                ToastUtils.showToastShort("请补全订单信息！");
                            }else{
                                commit_deposit();
                            }
                        }else if("0".equals(forme)){
                            commit_deposit();
                        }
                    }
            }
        });

        if(itemContent!=null){
            goods_name.setText("商品名称："+itemContent.getName());
            goods_type.setText("商品类型："+itemContent.getCourse_category_name());
            goods_time.setText("有效期："+days+"天");
            goods_price.setText("商品单价："+itemContent.getPrice()+"元");
//            goods_all_price.setText("合计金额："+itemContent.getPrice()+"元");
//            goods_num.setText("商品数量："+itemContent.getCount()+"节");
        }

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rl_jiaolian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listPopup!=null){
                    listPopup.showPopupWindow();
                }
            }
        });

//        if(course_category == 2){
//            rl_jiaolian.setClickable(false);
//            tv_jiaolian.setText(itemContent.getEmployees());
//        }else{
//            rl_jiaolian.setClickable(true);
//        }

        btn_forme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_forme.setChecked(true);
                btn_foryou.setChecked(false);
                if("1".equals(type)){
                    rl_kaikeshijian.setVisibility(View.GONE);
                }else{
                    rl_kaikeshijian.setVisibility(View.VISIBLE);
                }
                line7.setVisibility(View.GONE);
                rl_phone.setVisibility(View.GONE);
                rl_name.setVisibility(View.GONE);
                if("1".equals(type)){
                    rl_jiaolian.setVisibility(View.VISIBLE);
                }
                forme = "0";
            }
        });

        btn_foryou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_forme.setChecked(false);
                btn_foryou.setChecked(true);
                rl_kaikeshijian.setVisibility(View.GONE);
                rl_name.setVisibility(View.VISIBLE);
                line7.setVisibility(View.VISIBLE);
                rl_phone.setVisibility(View.VISIBLE);
                rl_name.setVisibility(View.VISIBLE);
                if("1".equals(type)){
                    rl_jiaolian.setVisibility(View.VISIBLE);
                }
                forme = "1";
            }
        });

        btn_zhifubao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_zhifubao.setChecked(true);
                btn_weixin.setChecked(false);
                btn_chuzhika.setChecked(false);
                zhifu = "2";
            }
        });

        btn_weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_zhifubao.setChecked(false);
                btn_weixin.setChecked(true);
                btn_chuzhika.setChecked(false);
                zhifu = "3";
            }
        });
        btn_chuzhika.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_zhifubao.setChecked(false);
                btn_weixin.setChecked(false);
                btn_chuzhika.setChecked(true);
                zhifu="5";
            }
        });
        iv_phonebook = findViewById(R.id.iv_phonebook);
        iv_phonebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                read_contacts();
            }
        });

        ed_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate();
            }
        });
        if("1".equals(type)){//1是订金
            rl_kaikeshijian.setVisibility(View.GONE);
            ll_dingjin.setVisibility(View.GONE);
        }else{
            rl_jiaolian.setVisibility(View.VISIBLE);
        }

        if("1".equals(type)){
            tv_pay_price.setText("￥"+dingjinprice);
        }else {
            tv_pay_price.setText("￥"+price);
        }
        getJiaoLian();
    }


    private void read_contacts() {
        //  Intent intent = new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI);
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

        startActivityForResult(intent, PICK_CONTACT);
    }

    class ListPopup extends BasePopupWindow {


        Context context;

        public ListPopup(Context context) {
            super(context);
            ListView popup_list = (ListView) findViewById(R.id.popup_list);
            if(JiaoLianList!=null){
                popup_list.setAdapter(new MyPopupListAdapter(MyApplication.getContext(),JiaoLianList));
            }
            this.context = context;
        }

        @Override
        protected Animation initShowAnimation() {
            return null;
        }

        @Override
        public View getClickToDismissView() {
            return getPopupWindowView();
        }

        @Override
        public View onCreatePopupView() {

            //  popupView=View.inflate(context,R.layout.popup_list_consultant,null);
            return createPopupById(R.layout.popup_list_consultant);

        }

        @Override
        public View initAnimaView() {
            return null;
        }
    }

    class PayBean {
        public String id;
        public String order_no;
        public float price;
        public int bus_type;

        @Override
        public String toString() {
            return "PayBean{" +
                    "id='" + id + '\'' +
                    ", order_no='" + order_no + '\'' +
                    ", price='" + price + '\'' +
                    '}';
        }


    }

    private void confirmOrder() {

        SijiaoOrderConfirmBean sijiaoOrderConfirmBean = new SijiaoOrderConfirmBean();
        SijiaoOrderConfirmBean.Extends_params extends_params = sijiaoOrderConfirmBean.new Extends_params();
        if("1".equals(forme)){//给好友买
            sijiaoOrderConfirmBean.setBus_type(57);
        }else if("0".equals(forme)){
            sijiaoOrderConfirmBean.setBus_type(56);
        }
        sijiaoOrderConfirmBean.setPay_way(zhifu);//1支付宝
        sijiaoOrderConfirmBean.setPlatform(2);
        sijiaoOrderConfirmBean.setDeposit_id(deposit_id);
        sijiaoOrderConfirmBean.setBranch_id(Integer.valueOf(info.getPerson().getDefault_branch()));
        extends_params.setCourse_type_id(course_id+"");
        extends_params.setCourse_type_name(course_name);
        extends_params.setEmployee_id(employee_id+"");
        extends_params.setEmployee_name(employee_name);
        extends_params.setTime_length(time_length);
        sijiaoOrderConfirmBean.setReceive((price-deposit)+"");
        sijiaoOrderConfirmBean.setPrice(price+"");
        if("1".equals(forme)){
            extends_params.setOther_name(ed_name.getText().toString());
            extends_params.setPhone_no(ed_phone.getText().toString());
        }else if("0".equals(forme)){
            extends_params.setStart_date(strTime+" 00:00:00");
            extends_params.setEnd_date(endTime+" 00:00:00");
        }

        sijiaoOrderConfirmBean.setExtends_params(extends_params);

        String s = gson.toJson(sijiaoOrderConfirmBean);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.COMMIT_CARD_ORDER, s, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                RequestOrderInfoBean requestOrderInfoBean = new RequestOrderInfoBean();
                Gson gson = new Gson();
                requestOrderInfoBean = gson.fromJson(jsonObject.toString(), RequestOrderInfoBean.class);

                if (requestOrderInfoBean.getSuccess()) {
                    alipay(requestOrderInfoBean.getData().getId());
                    btn_commit.setVisibility(View.GONE);
                } else {
                    ToastUtils.showToastShort("订单提交失败");
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


        MyApplication.getHttpQueues().add(jsonObjectRequest);

    }


    /**
     * 提交定金订单
     *
     *
     * @throws JSONException
     */
    public void commit_deposit() {
        CommitDepositBean commitDepositBean = new CommitDepositBean();
        CommitDepositBean.Extends_params extends_params = commitDepositBean.new Extends_params();
        commitDepositBean.setBranch_id(Integer.valueOf(info.getPerson().getDefault_branch()));
        if("0".equals(forme)){//为自己买
            commitDepositBean.setBus_type(31);
        }else if("1".equals(forme)){
            commitDepositBean.setBus_type(33);
        }
        commitDepositBean.setPlatform(2);
        commitDepositBean.setPay_way("1");
        commitDepositBean.setReceive(dingjinprice+"");
        commitDepositBean.setPrice(dingjinprice+"");
        extends_params.setBus_type("3");
        extends_params.setDeposit_type("3");
        extends_params.setAdmin_emp_id(employee_id+"");
        extends_params.setAdmin_emp_name(employee_name);
        extends_params.setMoney(dingjinprice+"");
        commitDepositBean.setExtends_params(extends_params);
        String s = gson.toJson(commitDepositBean);


        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Constants.COMMIT_DEPOSIT, s, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtil.i(jsonObject.toString());
                RequestOrderInfoBean requestOrderInfoBean = new RequestOrderInfoBean();
                Gson gson = new Gson();
                requestOrderInfoBean = gson.fromJson(jsonObject.toString(), RequestOrderInfoBean.class);

                if (requestOrderInfoBean.getSuccess()) {
                    btn_commit.setVisibility(View.GONE);
                    alipay(requestOrderInfoBean.getData().getId());
                } else {
                    ToastUtils.showToastShort("订单提交失败");
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
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));
                return map;
            }
        };
        MyApplication.getHttpQueues().add(stringRequest);

    }



    /**
     * 支付宝支付
     */
    private void alipay(String id) {
        PayBean payBean = new PayBean();
        payBean.id = id;
        payBean.order_no = 12345 + "";
        if("1".equals(type)){
            payBean.price = dingjinprice;
        }else{
            if(deposit_id!=null){
                payBean.price = price - deposit;
            }else{
                payBean.price = price;
            }
        }

        if("1".equals(forme)){
            payBean.bus_type = 57;
            if("1".equals(type)){
                payBean.bus_type = 33;
            }
        }else{
            payBean.bus_type = 56;
            if("1".equals(type)){
                payBean.bus_type = 31;
            }
        }

        String str = gson.toJson(payBean);

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(str);
            LogUtil.i(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url;
        if("5".equals(zhifu)){
            url = Constants.COMMIT_CHUZHIKA;

        }else{
            url = Constants.COMMIT_ALIPAY;
        }

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtil.i(jsonObject.toString());
                RequestSimpleBean requestSimpleBean = gson.fromJson(jsonObject.toString(), RequestSimpleBean.class);
                if (requestSimpleBean.getSuccess()) {
                    ToastUtils.showToastShort("支付成功");
                } else {
                    ToastUtils.showToastShort("支付失败");
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
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));
                return map;
            }
        };
        MyApplication.getHttpQueues().add(stringRequest);

    }


    class MyPopupListAdapter extends BaseAdapter {
        private Context context;
        private List<JiaoLianBean.Data> dataList;

        public MyPopupListAdapter(Context context,List<JiaoLianBean.Data> dataList) {
            this.context = context;
            this.dataList = dataList;
        }

        @Override
        public int getCount() {
            return dataList.size();
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
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            //     LogUtil.i("asdasdjalsdllasjdlk");
            ViewHolder vh = null;
            if (convertView == null) {
                vh = new ViewHolder();
                convertView = View.inflate(context, R.layout.listview_item_list_consultant, null);
                vh.mTextView = (TextView) convertView.findViewById(R.id.item_tx);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

//            employee_id = dataList.get(position).getEmployee_id();
//            employee_name = dataList.get(position).getEmployee_name();
            vh.mTextView.setText(dataList.get(position).getEmployee_name());

            vh.mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    employee_id = dataList.get(position).getEmployee_id();
                    employee_name = dataList.get(position).getEmployee_name();
                    tv_jiaolian.setText(employee_name);
                    listPopup.dismiss();
                }
            });

            return convertView;

        }


        class ViewHolder {
            public TextView mTextView;
        }
    }


    private void showDate(){
        ViewGroup parent = (ViewGroup)inflate1.getParent();
        if(parent!=null){
            parent.removeAllViews();
        }

        final ArrayList<String> yearList = new ArrayList<String>();
        final ArrayList<String> monthList = new ArrayList<String>();
        final ArrayList<String> dateList = new ArrayList<String>();
        int n=nowyear;
        int len = 50;
        for(int i=0;i<=len;i++){
            yearList.add((n+i)+"");
        }
        for(int j = 0;j<12;j++){
            monthList.add((1+j)+"");
        }
        lp_year.setNotLoop();
        lp_date.setNotLoop();
        lp_month.setNotLoop();
        lp_year.setItems(yearList);
        lp_month.setItems(monthList);

        lp_year.setInitPosition(0);
        syear = yearList.get(0);
        lp_month.setInitPosition(month-1);
        smonth = monthList.get(month-1);

        daysByYearMonth = TimeUtils.getDaysByYearMonth(Integer.valueOf(syear), Integer.valueOf(smonth));
        dateList.clear();
        for(int z=1;z<=daysByYearMonth;z++){
            dateList.add(z+"");
        }
        lp_date.setItems(dateList);

        //设置字体大小
        lp_year.setTextSize(16);
        lp_month.setTextSize(16);
        lp_date.setTextSize(16);

        lp_year.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                syear = yearList.get(index);
                daysByYearMonth = TimeUtils.getDaysByYearMonth(Integer.valueOf(syear), Integer.valueOf(smonth));
                dateList.clear();
                for(int z=1;z<=daysByYearMonth;z++){
                    dateList.add(z+"");
                }
                lp_date.setItems(dateList);
            }
        });

        lp_month.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                smonth = monthList.get(index);
                daysByYearMonth = TimeUtils.getDaysByYearMonth(Integer.valueOf(syear), Integer.valueOf(smonth));
                dateList.clear();
                for(int z=1;z<=daysByYearMonth;z++){
                    dateList.add(z+"");
                }
                lp_date.setItems(dateList);
            }
        });

        lp_date.setInitPosition(monthDay-1);
        sdate = dateList.get(monthDay);
        lp_date.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                sdate = dateList.get(index);
            }
        });

        alertdialog.setTitle("选择开课日期");
        alertdialog.setView(inflate1);
        alertdialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ed_time.setText(syear+"年"+smonth+"月"+sdate+"日");
                strTime = syear+"-"+smonth+"-"+sdate;

                startMill = TimeUtils.date2TimeStamp(strTime, "yyyy-MM-dd");
                endMill = (long)days*86400000+startMill;
                endTime = TimeUtils.timeStamp2Date(endMill+"","yyyy-MM-dd");
            }
        });
        alertdialog.show();

    }


}
