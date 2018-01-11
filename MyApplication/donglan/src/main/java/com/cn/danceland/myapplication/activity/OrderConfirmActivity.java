package com.cn.danceland.myapplication.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.OrderInfoBean;
import com.cn.danceland.myapplication.bean.RequestConsultantInfoBean;
import com.cn.danceland.myapplication.bean.RequestOrderInfoBean;
import com.cn.danceland.myapplication.bean.RequestSellCardsInfoBean;
import com.cn.danceland.myapplication.bean.RequestSimpleBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.PriceUtils;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import razerdp.basepopup.BasePopupWindow;

/**
 * Created by shy on 2017/12/20 10:14
 * Email:644563767@qq.com
 */

@RuntimePermissions
public class OrderConfirmActivity extends Activity implements View.OnClickListener {
    private RequestSellCardsInfoBean.Data CardsInfo;
    private boolean isme;//是否是本人购买
    private int product_type;//1是卡，2是定金，3是私教，4是其他
    private int number = 1;
    private RequestConsultantInfoBean.Data consultantInfo;
    private LinearLayout ll_02;
    private TextView tv_explain;//说明
    private EditText et_grant_name;//好友名字
    private EditText et_grant_phone;//好友电话
    private List<RequestConsultantInfoBean.Data> consultantListInfo = new ArrayList<>();

    private MyPopupListAdapter myPopupListAdapter;
    private TextView tv_counselor;
    private ListPopup listPopup;
    private TextView tv_pay_price;
    private float total_price;
    private float pay_price;
    private Button btn_commit;
    private CheckBox cb_alipay;
    private CheckBox cb_wechat;
    private int pay_way = 1;
    private TextView tv_dingjin;
    private String depositId;
    private float deposit_price;
    private Button btn_repay;
    private String strBean;
    private String unpaidOrder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_comfirm);
        initView();
        initData();
    }

    private void initData() {
        findConsultant(CardsInfo.getBranch_id());
    }

    private void initView() {
        Bundle bundle = this.getIntent().getExtras();
        CardsInfo = (RequestSellCardsInfoBean.Data) bundle.getSerializable("cardinfo");
        //  consultantInfo = (RequestConsultantInfoBean.Data) bundle.getSerializable("consultantInfo");
        isme = bundle.getBoolean("isme", true);
        product_type = bundle.getInt("product_type", 1);
        myPopupListAdapter = new MyPopupListAdapter(this);

        listPopup = new ListPopup(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        //  LinearLayout ll_01 = findViewById(R.id.ll_01);

        ll_02 = findViewById(R.id.ll_02);
        LinearLayout ll_03 = findViewById(R.id.ll_03);
        ll_03.setOnClickListener(this);


        TextView tv_name = findViewById(R.id.tv_product_name);
        // TextView tv_number = findViewById(R.id.tv_number);
        TextView tv_useful_life = findViewById(R.id.tv_useful_life);
        TextView tv_price = findViewById(R.id.tv_price);
        TextView tv_product_type = findViewById(R.id.tv_product_type);
        TextView tv_total_price = findViewById(R.id.tv_total_price);
        tv_dingjin = findViewById(R.id.tv_dingjin);
        tv_explain = findViewById(R.id.tv_explain);
        findViewById(R.id.ll_counselor).setOnClickListener(this);
        findViewById(R.id.iv_phonebook).setOnClickListener(this);
        btn_commit = findViewById(R.id.btn_commit);
        btn_repay = findViewById(R.id.btn_commit2);
        btn_repay.setOnClickListener(this);
        findViewById(R.id.btn_commit).setOnClickListener(this);
        tv_counselor = findViewById(R.id.tv_counselor);
        et_grant_name = findViewById(R.id.et_grant_name);
        et_grant_phone = findViewById(R.id.et_grant_phone);
        tv_pay_price = findViewById(R.id.tv_pay_price);
        cb_alipay = findViewById(R.id.cb_alipay);
        cb_wechat = findViewById(R.id.cb_wechat);
        cb_alipay.setChecked(true);//默认支付宝支付

        cb_alipay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    cb_wechat.setChecked(false);
                    pay_way = 1;
                } else {
                    cb_wechat.setChecked(true);
                    pay_way = 2;
                }
            }
        });
        cb_wechat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    cb_alipay.setChecked(false);
                    pay_way = 2;
                } else {
                    cb_alipay.setChecked(true);
                    pay_way = 1;
                }
            }
        });

        RadioGroup radioGroup = findViewById(R.id.rg_who);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {

                    case R.id.rbtn_me:

                        isme = true;

                        ll_02.setVisibility(View.GONE);
                        if (product_type == 1) {
                            //     startDate=bundle.getString("startDate",null);
                            //     tv_start_date.setText(startDate);
                            tv_explain.setText(R.string.explain_for_card);
                        }

                        if (product_type == 2) {//是定金
                            //  ll_01.setVisibility(View.GONE);
                            tv_explain.setText(R.string.explain_for_dingjin);


                        }


                        break;
                    case R.id.rbtn_other:

                        ll_02.setVisibility(View.VISIBLE);
                        //  ll_01.setVisibility(View.GONE);
                        if (product_type == 1) {
                            tv_explain.setText(R.string.explain_for_card_for_other);
                        }

                        if (product_type == 2) {//是定金
                            tv_explain.setText(R.string.explain_for_dingjin_for_other);
                        }


                        isme = false;

                        break;
                    default:
                        break;
                }

            }
        });
        if (isme) {//是否是本人
            ll_02.setVisibility(View.GONE);
            if (product_type == 1) {

                tv_explain.setText(R.string.explain_for_card);
            }

            if (product_type == 2) {//是定金
                //  ll_01.setVisibility(View.GONE);
                tv_explain.setText(R.string.explain_for_dingjin);
            }
        } else

        {//给其他人买

            if (product_type == 1) {
                tv_explain.setText(R.string.explain_for_card_for_other);
            }

            if (product_type == 2) {//是定金
                tv_explain.setText(R.string.explain_for_dingjin_for_other);
            }
        }


        //    tv_counselor.setText(consultantInfo.getCname());

        if (product_type == 1)

        {//如果商品是卡


            if (CardsInfo.getCharge_mode() == 1) {//计时卡
                tv_product_type.setText("计时卡");
            }
            if (CardsInfo.getCharge_mode() == 2) {//计次卡
                tv_product_type.setText("计次卡（" + CardsInfo.getTotal_count() + "次）");

            }
            if (CardsInfo.getCharge_mode() == 3) {//储值卡
                tv_product_type.setText("储值卡");
            }
            //tv_number.setText(number + "");

            tv_name.setText(CardsInfo.getName());


            //单价
            tv_price.setText(PriceUtils.formatPrice2String(CardsInfo.getPrice()));
            total_price = CardsInfo.getPrice();

            //总价
            //     tv_total_price.setText(PriceUtils.formatPrice2String(CardsInfo.getPrice() * number));
            // total_price = CardsInfo.getPrice() * number;


            if (CardsInfo.getTime_unit() == 1) {
                tv_useful_life.setText(CardsInfo.getTime_value() + "年");
            }
            if (CardsInfo.getTime_unit() == 2) {
                tv_useful_life.setText(CardsInfo.getTime_value() + "个月");
            }
            pay_price = total_price;
            tv_pay_price.setText(PriceUtils.formatPrice2String(pay_price));

        }
        if (product_type == 2)

        {//如果商品是定金
            ll_03.setVisibility(View.GONE);
            tv_name.setText("预付定金");
            tv_product_type.setText("会籍卡定金");
            tv_useful_life.setText("1个月");
            tv_price.setText(PriceUtils.formatPrice2String(100.00f));
            tv_total_price.setText(PriceUtils.formatPrice2String(100.00f * number));
            tv_pay_price.setText(PriceUtils.formatPrice2String(100.00f * number));
            total_price = PriceUtils.formatPrice2float(100.00f * number);
            pay_price = total_price;
        }

    }

    Gson gson = new Gson();


    /**
     * 提交卡订单
     *
     * @param str
     * @throws JSONException
     */
    public void commit_card(final String str) throws JSONException {

        JSONObject jsonObject = new JSONObject(str);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Constants.COMMIT_CARD_ORDER, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtil.i(jsonObject.toString());
                RequestOrderInfoBean requestOrderInfoBean = new RequestOrderInfoBean();
                Gson gson = new Gson();
                requestOrderInfoBean = gson.fromJson(jsonObject.toString(), RequestOrderInfoBean.class);

                if (requestOrderInfoBean.getSuccess()) {
                    ToastUtils.showToastShort("提交成功");
                    btn_commit.setVisibility(View.GONE);
                    showPayDialog(pay_way, requestOrderInfoBean.getData().getId());


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
                LogUtil.i(SPUtils.getString(Constants.MY_TOKEN, "") + "====" + SPUtils.getString(Constants.MY_USERID, ""));
                return map;
            }
        };
        MyApplication.getHttpQueues().add(stringRequest);

    }

    /**
     * 提交定金订单
     *
     * @param str
     * @throws JSONException
     */
    public void commit_deposit(final String str) throws JSONException {

        JSONObject jsonObject = new JSONObject(str);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Constants.COMMIT_DEPOSIT, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtil.i(jsonObject.toString());
                RequestOrderInfoBean requestOrderInfoBean = new RequestOrderInfoBean();
                Gson gson = new Gson();
                requestOrderInfoBean = gson.fromJson(jsonObject.toString(), RequestOrderInfoBean.class);

                if (requestOrderInfoBean.getSuccess()) {
                    ToastUtils.showToastShort("提交成功");
                    btn_commit.setVisibility(View.GONE);

                    showPayDialog(pay_way, requestOrderInfoBean.getData().getId());

//                    if (pay_way == 1) {//支付宝
//                        alipay(requestOrderInfoBean.getData().getId());
//                    }
//                    if (pay_way == 2) {
//                        //微信
//                        wechatPay(requestOrderInfoBean.getData().getId());
//                    }

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
                LogUtil.i(SPUtils.getString(Constants.MY_TOKEN, "") + "====" + SPUtils.getString(Constants.MY_USERID, ""));
                return map;
            }
        };
        MyApplication.getHttpQueues().add(stringRequest);

    }


    class PayBean {
        public String id;
        public String order_no;
        public float price;

        @Override
        public String toString() {
            return "PayBean{" +
                    "id='" + id + '\'' +
                    ", order_no='" + order_no + '\'' +
                    ", price='" + price + '\'' +
                    '}';
        }


    }

    private void showPayDialog(final int pay_way, final String orderId) {
        AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
        normalDialog.setMessage("订单已提交，是否现在支付");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (pay_way == 1) {
                            //支付宝
                            alipay(orderId);
                        }
                        if (pay_way == 2) {
                            //微信
                            wechatPay(orderId);
                        }
                    }
                });
        normalDialog.setNegativeButton("稍后",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        btn_repay.setVisibility(View.VISIBLE);
                        unpaidOrder=orderId;
                    }
                });
        // 显示
        normalDialog.show();
    }

    /**
     * 支付宝支付
     */
    private void wechatPay(String id) {
        PayBean payBean = new PayBean();
        payBean.id = id;
        payBean.order_no = 12345 + "";
        payBean.price = pay_price;
        String str = gson.toJson(payBean);

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(str);
            LogUtil.i(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Constants.COMMIT_WECHAT_PAY, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtil.i(jsonObject.toString());
                RequestSimpleBean requestSimpleBean = gson.fromJson(jsonObject.toString(), RequestSimpleBean.class);
                if (requestSimpleBean.getSuccess()) {
                    ToastUtils.showToastShort("支付成功");
                    finish();
                } else {
                    ToastUtils.showToastShort("支付失败");
                    btn_repay.setVisibility(View.VISIBLE);
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
        //   MyApplication.getHttpQueues().add(stringRequest);

    }


    /**
     * 支付宝支付
     */
    private void alipay(String id) {
        PayBean payBean = new PayBean();
        payBean.id = id;
        payBean.order_no = 12345 + "";
        payBean.price = pay_price;
        String str = gson.toJson(payBean);

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(str);
            LogUtil.i(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Constants.COMMIT_ALIPAY, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtil.i(jsonObject.toString());
                RequestSimpleBean requestSimpleBean = gson.fromJson(jsonObject.toString(), RequestSimpleBean.class);
                if (requestSimpleBean.getSuccess()) {
                    ToastUtils.showToastShort("支付成功");
                    finish();
                } else {
                    ToastUtils.showToastShort("支付失败");
                    btn_repay.setVisibility(View.VISIBLE);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case (PICK_CONTACT):
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
                                et_grant_phone.setText(phoneNumber.trim().replace(" ", ""));
                                if (TextUtils.isEmpty(et_grant_name.getText())) {
                                    et_grant_name.setText(name);
                                }

                            }
                            phones.close();
                        }


                    }
                }
                break;
            case 11:

//                LogUtil.i(data.getFloatExtra("dingjin", 0)+"");
                if (data != null) {
                    deposit_price = data.getFloatExtra("dingjin", 0);
                    if (deposit_price != 0f) {
                        depositId = data.getStringExtra("id");
                        //        LogUtil.i(depositId);
                        tv_dingjin.setText("- " + PriceUtils.formatPrice2String(deposit_price));

                        pay_price = total_price - deposit_price;
                        tv_pay_price.setText(PriceUtils.formatPrice2String(pay_price));

                    } else {
                        depositId = "";
                        tv_dingjin.setText("未使用");
                        pay_price = total_price - deposit_price;
                        tv_pay_price.setText(PriceUtils.formatPrice2String(pay_price));
                    }


                }

                break;


        }


    }

    private static final int PICK_CONTACT = 0;


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();

                break;

            case R.id.iv_phonebook://选择通讯录

                read_contacts();
                break;
            case R.id.ll_counselor://选择会籍顾问

                listPopup.showPopupWindow();

                break;
            case R.id.ll_03://选择支付定金

                startActivityForResult(new Intent(OrderConfirmActivity.this, DepositActivity.class), 11);


                break;
            case R.id.btn_commit://提交订单

                if (product_type == 1) {//卡订单

                    final OrderInfoBean orderInfoBean = new OrderInfoBean();
                    if (consultantInfo == null) {
                        ToastUtils.showToastLong("请选择会籍顾问");
                        break;
                    }
                    orderInfoBean.setAdmin_emp_id(consultantInfo.getEmployee_id());
                    orderInfoBean.setAdmin_emp_name(consultantInfo.getCname());
                    orderInfoBean.setBranch_name(consultantInfo.getBranch_name());
                    orderInfoBean.setBranch_id(consultantInfo.getBranch_id());
                    orderInfoBean.setAdmin_emp_phone(consultantInfo.getPhone_no());
                    Data data = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
                    orderInfoBean.setCname(data.getCname());
                    orderInfoBean.setMember_no(data.getMember_no());
                    orderInfoBean.setPrice(pay_price + "");
                    orderInfoBean.setPay_way(pay_way + "");
                    orderInfoBean.setCard_type_id(CardsInfo.getId());
                    orderInfoBean.setCard_name(CardsInfo.getName());
                    orderInfoBean.setMonth_count(CardsInfo.getMonth_count() + "");
                    if (!TextUtils.isEmpty(depositId)) {
                        orderInfoBean.setDeposit_id(depositId);
                        orderInfoBean.setDeposit_price(deposit_price + "");
                    }
                    if (CardsInfo.getCharge_mode() == 2) {//如果是计次卡
                        orderInfoBean.setTotal_count(CardsInfo.getTotal_count());

                    }
                    if (isme) {
                        orderInfoBean.setFor_other(0);
                    } else {
                        orderInfoBean.setFor_other(1);
                        orderInfoBean.setName(et_grant_name.getText().toString().trim());
                        orderInfoBean.setPhone_no(et_grant_phone.getText().toString().trim());
                    }
                    //   LogUtil.i(orderInfoBean.toString());

                    if (!isme && TextUtils.isEmpty(et_grant_name.getText().toString().trim())) {
                        ToastUtils.showToastLong("请输入好友姓名");
                        break;
                    }
                    if (!isme && TextUtils.isEmpty(et_grant_phone.getText().toString().trim())) {
                        ToastUtils.showToastLong("请输入好友手机号");
                        break;
                    }

                    Gson gson = new Gson();
                    strBean = gson.toJson(orderInfoBean);
                    LogUtil.i(strBean.toString());
                    try {
                        commit_card(strBean.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


                if (product_type == 2) {//定金订单

                    final OrderInfoBean orderInfoBean = new OrderInfoBean();
                    if (consultantInfo == null) {
                        ToastUtils.showToastLong("请选择会籍顾问");
                        break;
                    }
                    orderInfoBean.setAdmin_emp_id(consultantInfo.getEmployee_id());
                    orderInfoBean.setAdmin_emp_name(consultantInfo.getCname());
                    orderInfoBean.setBranch_name(consultantInfo.getBranch_name());
                    orderInfoBean.setBranch_id(consultantInfo.getBranch_id());
                    orderInfoBean.setAdmin_emp_phone(consultantInfo.getPhone_no());
                    Data data = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
                    orderInfoBean.setCname(data.getCname());
                    orderInfoBean.setMember_no(data.getMember_no());
                    orderInfoBean.setPrice(pay_price + "");
                    orderInfoBean.setPay_way(pay_way + "");
                    orderInfoBean.setDeposit_type("1");//定金类型
                    orderInfoBean.setMonth_count("3");
                    if (isme) {
                        orderInfoBean.setFor_other(0);
                    } else {
                        orderInfoBean.setFor_other(1);
                        orderInfoBean.setName(et_grant_name.getText().toString().trim());
                        orderInfoBean.setPhone_no(et_grant_phone.getText().toString().trim());
                    }
                    //   LogUtil.i(orderInfoBean.toString());

                    if (!isme && TextUtils.isEmpty(et_grant_name.getText().toString().trim())) {
                        ToastUtils.showToastLong("请输入好友姓名");
                        break;
                    }
                    if (!isme && TextUtils.isEmpty(et_grant_phone.getText().toString().trim())) {
                        ToastUtils.showToastLong("请输入好友手机号");
                        break;
                    }

                    Gson gson = new Gson();
                    strBean = gson.toJson(orderInfoBean);
                    LogUtil.i(strBean.toString());
                    try {
                        commit_deposit(strBean.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


                break;
            case R.id.btn_commit2://重新支付

                if (pay_way == 1) {
                    //支付宝
                    alipay(unpaidOrder);
                }
                if (pay_way == 2) {
                    //微信
                    wechatPay(unpaidOrder);
                }

                break;


            default:
                break;
        }
    }


    @NeedsPermission({Manifest.permission.READ_CONTACTS, Manifest.permission_group.CONTACTS})
    void read_contacts() {
        //  Intent intent = new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI);
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

        startActivityForResult(intent, PICK_CONTACT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        OrderConfirmActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
        //MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);

    }

    class ListPopup extends BasePopupWindow {


        Context context;

        public ListPopup(Context context) {
            super(context);
            ListView popup_list = (ListView) findViewById(R.id.popup_list);
            popup_list.setAdapter(myPopupListAdapter);
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


    class MyPopupListAdapter extends BaseAdapter {
        private Context context;

        public MyPopupListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return consultantListInfo.size();
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
            vh.mTextView.setText(consultantListInfo.get(position).getCname());

            vh.mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    consultantInfo = consultantListInfo.get(position);

                    tv_counselor.setText(consultantListInfo.get(position).getCname());
                    tv_counselor.setTextColor(Color.BLACK);
                    listPopup.dismiss();
                }
            });

            return convertView;

        }


        class ViewHolder {
            public TextView mTextView;
        }
    }

    /**
     * 查找会籍顾问
     */
    private void findConsultant(final String branchId) {


        StringRequest request = new StringRequest(Request.Method.POST, Constants.FIND_CONSULTANT_URL, new Response.Listener<String>() {


            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                Gson gson = new Gson();
                RequestConsultantInfoBean requestConsultantInfoBean = gson.fromJson(s, RequestConsultantInfoBean.class);
                if (requestConsultantInfoBean.getSuccess()) {
                    consultantListInfo = requestConsultantInfoBean.getData();
                    //  LogUtil.i(consultantListInfo.toString());
                    myPopupListAdapter.notifyDataSetChanged();

                } else {
                    ToastUtils.showToastShort(requestConsultantInfoBean.getErrorMsg());
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

                Map<String, String> map = new HashMap<String, String>();

                map.put("branchId", branchId);

                return map;

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, null));
                return map;
            }
        };
        MyApplication.getHttpQueues().add(request);

    }
}
