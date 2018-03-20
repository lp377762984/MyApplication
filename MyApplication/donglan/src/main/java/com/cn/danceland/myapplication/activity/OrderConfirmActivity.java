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
    public static int ORDER_BUS_TYPE_DEPOSIT_APP = 31;// app买定金
    public static int ORDER_BUS_TYPE_CARD_OPEN_APP = 32;// APP卖卡,业务系统取卡
    private int order_bustype = 0;

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

        if (product_type == 1) {//如实卡
            order_bustype = ORDER_BUS_TYPE_CARD_OPEN_APP;

        } else if (product_type == 2) {//如果是定金
            order_bustype = ORDER_BUS_TYPE_DEPOSIT_APP;
        }
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
             //   LogUtil.i(SPUtils.getString(Constants.MY_TOKEN, "") + "====" + SPUtils.getString(Constants.MY_USERID, ""));
                return map;
            }
        };
        MyApplication.getHttpQueues().add(stringRequest);

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

    private void showPayDialog(final int pay_way, final String orderId) {
        AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
        normalDialog.setMessage("订单已提交，是否现在支付");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        unpaidOrder = orderId;
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
                        unpaidOrder = orderId;
                    }
                });
        // 显示
        normalDialog.show();
    }

    /**
     * weixin支付
     */
    private void wechatPay(String id) {
        PayBean payBean = new PayBean();
        payBean.id = id;
        payBean.order_no = 12345 + "";
        payBean.price = pay_price;
        payBean.bus_type = order_bustype;
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
        payBean.bus_type = order_bustype;
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
                    final NewOrderInfoBean newOrderInfoBean = new NewOrderInfoBean();
                    final OrderInfoBean orderInfoBean = new OrderInfoBean();


                    if (consultantInfo == null) {
                        ToastUtils.showToastLong("请选择会籍顾问");
                        break;
                    }
                    newOrderInfoBean.setBranch_id(consultantInfo.getBranch_id());
                    newOrderInfoBean.setBus_type(order_bustype);
                    NewOrderInfoBean.ExtendsParams extendsParams = new NewOrderInfoBean.ExtendsParams();
                    extendsParams.setSell_id(consultantInfo.getId() + "");
                    extendsParams.setBus_type("1");
                    extendsParams.setFace_value(CardsInfo.getPrice() + "");
                    extendsParams.setBranch_name(consultantInfo.getBranch_name());
                    extendsParams.setSell_name(consultantInfo.getCname());
                    extendsParams.setMonth_count(CardsInfo.getMonth_count() + "");
                    extendsParams.setType_id(CardsInfo.getId());
                    extendsParams.setCharge_mode(CardsInfo.getCharge_mode() + "");
                    extendsParams.setType_name(CardsInfo.getName());
                    newOrderInfoBean.setPay_way(pay_way + "");
                    newOrderInfoBean.setPrice(pay_price + "");
                    newOrderInfoBean.setReceive(CardsInfo.getPrice() + "");
                    if (isme) {
                        newOrderInfoBean.setFor_other(0);
                    } else {
                        newOrderInfoBean.setFor_other(1);
                        extendsParams.setMember_name(et_grant_name.getText().toString().trim());
                        extendsParams.setPhone_no(et_grant_phone.getText().toString().trim());
                    }


                    orderInfoBean.setAdmin_emp_id(consultantInfo.getEmployee_id());
                    orderInfoBean.setAdmin_emp_name(consultantInfo.getCname());
                    orderInfoBean.setBranch_name(consultantInfo.getBranch_name());
                    orderInfoBean.setBranch_id(consultantInfo.getBranch_id());
                    orderInfoBean.setAdmin_emp_phone(consultantInfo.getPhone_no());
                    Data data = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
                    orderInfoBean.setCname(data.getPerson().getCname());
                    orderInfoBean.setMember_no(data.getPerson().getMember_no());
                    orderInfoBean.setPrice(pay_price + "");
                    orderInfoBean.setPay_way(pay_way + "");
                    orderInfoBean.setCard_type_id(CardsInfo.getId());
                    orderInfoBean.setCard_name(CardsInfo.getName());
                    orderInfoBean.setMonth_count(CardsInfo.getMonth_count() + "");
                    if (!TextUtils.isEmpty(depositId)) {
                        orderInfoBean.setDeposit_id(depositId);
                        orderInfoBean.setDeposit_price(deposit_price + "");
                        newOrderInfoBean.setDeposit_id(depositId);
                    }
                    if (CardsInfo.getCharge_mode() == 2) {//如果是计次卡
                        orderInfoBean.setTotal_count(CardsInfo.getTotal_count());
                        extendsParams.setTotal_count(CardsInfo.getTotal_count());
                    }
                    if (isme) {
                        newOrderInfoBean.setBus_type(32);//给别人买定金
                        orderInfoBean.setFor_other(0);
                    } else {
                        order_bustype=34;
                        newOrderInfoBean.setBus_type(34);//给别人买定金
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
                    newOrderInfoBean.setExtends_params(extendsParams);
                    Gson gson = new Gson();
                    strBean = gson.toJson(newOrderInfoBean);
                    LogUtil.i(strBean.toString());
                    try {
                        commit_card(strBean.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


                if (product_type == 2) {//定金订单

                 //   final OrderInfoBean orderInfoBean = new OrderInfoBean();
                    final NewOrderInfoBean newOrderInfoBean = new NewOrderInfoBean();
                    if (consultantInfo == null) {
                        ToastUtils.showToastLong("请选择会籍顾问");
                        break;
                    }
                    newOrderInfoBean.setBranch_id(consultantInfo.getBranch_id());
                    newOrderInfoBean.setBus_type(31);
                    NewOrderInfoBean.ExtendsParams extendsParams = new NewOrderInfoBean.ExtendsParams();
                    extendsParams.setAdmin_emp_id(consultantInfo.getId() + "");
                    extendsParams.setBus_type("1");
                    extendsParams.setMoney(pay_price + "");
                    extendsParams.setAdmin_emp_name(consultantInfo.getCname());
                    extendsParams.setDeposit_type("1");//定金类型


                    if (isme) {
                        newOrderInfoBean.setFor_other(0);
                        newOrderInfoBean.setBus_type(31);
                    } else {
                        order_bustype=33;
                        newOrderInfoBean.setBus_type(33);//给别人买定金
                        newOrderInfoBean.setFor_other(1);
                        extendsParams.setMember_name(et_grant_name.getText().toString().trim());
                        extendsParams.setPhone_no(et_grant_phone.getText().toString().trim());
                    }

                    newOrderInfoBean.setExtends_params(extendsParams);
                    newOrderInfoBean.setPay_way(pay_way + "");
                    newOrderInfoBean.setPrice(pay_price + "");
                    newOrderInfoBean.setReceive(pay_price + "");

//
//                    orderInfoBean.setAdmin_emp_id(consultantInfo.getEmployee_id());
//                    orderInfoBean.setAdmin_emp_name(consultantInfo.getCname());
//                    orderInfoBean.setBranch_name(consultantInfo.getBranch_name());
//                    orderInfoBean.setBranch_id(consultantInfo.getBranch_id());
//                    orderInfoBean.setAdmin_emp_phone(consultantInfo.getPhone_no());
//                    Data data = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
//                    orderInfoBean.setCname(data.getCname());
//                    orderInfoBean.setMember_no(data.getMember_no());
//                    orderInfoBean.setPrice(pay_price + "");
//                    orderInfoBean.setPay_way(pay_way + "");
//                    orderInfoBean.setDeposit_type("1");//定金类型
//                    orderInfoBean.setMonth_count("3");
//                    if (isme) {
//                        orderInfoBean.setFor_other(0);
//                    } else {
//                        orderInfoBean.setFor_other(1);
//                        orderInfoBean.setName(et_grant_name.getText().toString().trim());
//                        orderInfoBean.setPhone_no(et_grant_phone.getText().toString().trim());
//                    }
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
                    strBean = gson.toJson(newOrderInfoBean);
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


    static class NewOrderInfoBean

    {

//        private int admin_emp_id;//会籍顾问id
//        private String admin_emp_name;//会籍顾问名称
//        private String admin_emp_phone;//会籍顾问电话
//   //     private int branch_id;//门店id
//        private String branch_name;//门店名称
//        private int for_other;//0自己1别人
//        private String name;//
//        private String order_no;
//        private String order_time;
//     //   private String pay_way;//1是支付宝，2是微信
//
//      //  private String price;//
//        private String member_no;//会员编号
//        private String member_name;//会员自己的真实姓名
//  private String phone_no;//
//        private String card_type_id;//卡id
//        private String deposit_id;//定金id
//        private String deposit_price;//定金金额
//
//        private String deposit_type;  //定金类型
//        private String card_name;//卡名称
//        private String month_count;// 使用期限月数
//        private String total_count; //次卡总次数

        public int bus_type;// 业务类型
        //	byte ORDER_BUS_TYPE_DEPOSIT_APP = 31;// app买定金
        //	byte ORDER_BUS_TYPE_CARD_OPEN_APP = 32;// APP卖卡,业务系统取卡
        public String person_id;// 人主键
        public String member_id;// 会员主键
        public int branch_id;// 门店主键
        public String pay_way;// 支付方式
        public String price;// 支付金额
        public String receive;
        public int platform = 2;
        public ExtendsParams extends_params;
        private String deposit_id;//定金id
        private int for_other;//0自己1别人

        @Override
        public String toString() {
            return "NewOrderInfoBean{" +
                    "bus_type=" + bus_type +
                    ", person_id='" + person_id + '\'' +
                    ", member_id='" + member_id + '\'' +
                    ", branch_id=" + branch_id +
                    ", pay_way='" + pay_way + '\'' +
                    ", price='" + price + '\'' +
                    ", receive='" + receive + '\'' +
                    ", platform=" + platform +
                    ", extends_params=" + extends_params +
                    ", deposit_id='" + deposit_id + '\'' +
                    ", for_other=" + for_other +
                    '}';
        }

        public int getFor_other() {
            return for_other;
        }

        public void setFor_other(int for_other) {
            this.for_other = for_other;
        }

        public String getDeposit_id() {
            return deposit_id;
        }

        public void setDeposit_id(String deposit_id) {
            this.deposit_id = deposit_id;
        }

        public int getBus_type() {
            return bus_type;
        }

        public void setBus_type(int bus_type) {
            this.bus_type = bus_type;
        }

        public String getPerson_id() {
            return person_id;
        }

        public void setPerson_id(String person_id) {
            this.person_id = person_id;
        }

        public String getMember_id() {
            return member_id;
        }

        public void setMember_id(String member_id) {
            this.member_id = member_id;
        }

        public int getBranch_id() {
            return branch_id;
        }

        public void setBranch_id(int branch_id) {
            this.branch_id = branch_id;
        }

        public String getPay_way() {
            return pay_way;
        }

        public void setPay_way(String pay_way) {
            this.pay_way = pay_way;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getReceive() {
            return receive;
        }

        public void setReceive(String receive) {
            this.receive = receive;
        }

        public int getPlatform() {
            return platform;
        }

        public void setPlatform(int platform) {
            this.platform = platform;
        }

        public ExtendsParams getExtends_params() {
            return extends_params;
        }

        public void setExtends_params(ExtendsParams extends_params) {
            this.extends_params = extends_params;
        }

        static class ExtendsParams {
            public String admin_emp_id;
            public String bus_type;
            public String member_id;
            public String money;
            public String remark;


            public String month_count;// 使用期限月数
            public String type_id;
            public String total_count; //次卡总次数
            public String face_value;//type.price
            public String charge_mode;
            public String sell_id;
            public String sell_name;//会藉顾问名
            public String sell_price;//=type.price-定金
            public String branch_name;// 门店名
            public String type_name;//卡名称

            public String admin_emp_name;
            //        private String card_type_id;//卡id
//        private String deposit_id;//定金id
//        private String deposit_price;//定金金额
//
            private String deposit_type;  //定金类型
//        private String card_name;//卡名称
//        private String month_count;// 使用期限月数
//        private String total_count; //次卡总次数

       //     private String member_name;//好友真实姓名
            private String phone_no;//好友电话
            private String other_name;//好友真实姓名
            @Override
            public String toString() {
                return "ExtendsParams{" +
                        "admin_emp_id='" + admin_emp_id + '\'' +
                        ", bus_type='" + bus_type + '\'' +
                        ", member_id='" + member_id + '\'' +
                        ", money='" + money + '\'' +
                        ", remark='" + remark + '\'' +
                        ", month_count='" + month_count + '\'' +
                        ", type_id='" + type_id + '\'' +
                        ", total_count='" + total_count + '\'' +
                        ", face_value='" + face_value + '\'' +
                        ", charge_mode='" + charge_mode + '\'' +
                        ", sell_id='" + sell_id + '\'' +
                        ", sell_name='" + sell_name + '\'' +
                        ", sell_price='" + sell_price + '\'' +
                        ", branch_name='" + branch_name + '\'' +
                        ", type_name='" + type_name + '\'' +
                        ", admin_emp_name='" + admin_emp_name + '\'' +
                        ", deposit_type='" + deposit_type + '\'' +
                        ", other_name='" + other_name + '\'' +
                        ", phone_no='" + phone_no + '\'' +
                        '}';
            }

            public String getMember_name() {
                return other_name;
            }

            public void setMember_name(String member_name) {
                this.other_name = member_name;
            }

            public String getPhone_no() {
                return phone_no;
            }

            public void setPhone_no(String phone_no) {
                this.phone_no = phone_no;
            }

            public String getDeposit_type() {
                return deposit_type;
            }

            public void setDeposit_type(String deposit_type) {
                this.deposit_type = deposit_type;
            }

            public String getAdmin_emp_name() {
                return admin_emp_name;
            }

            public void setAdmin_emp_name(String admin_emp_name) {
                this.admin_emp_name = admin_emp_name;
            }

            public String getType_name() {
                return type_name;
            }

            public void setType_name(String type_name) {
                this.type_name = type_name;
            }

            public String getMonth_count() {
                return month_count;
            }

            public void setMonth_count(String month_count) {
                this.month_count = month_count;
            }

            public String getType_id() {
                return type_id;
            }

            public void setType_id(String type_id) {
                this.type_id = type_id;
            }

            public String getTotal_count() {
                return total_count;
            }

            public void setTotal_count(String total_count) {
                this.total_count = total_count;
            }

            public String getFace_value() {
                return face_value;
            }

            public void setFace_value(String face_value) {
                this.face_value = face_value;
            }

            public String getCharge_mode() {
                return charge_mode;
            }

            public void setCharge_mode(String charge_mode) {
                this.charge_mode = charge_mode;
            }

            public String getSell_id() {
                return sell_id;
            }

            public void setSell_id(String sell_id) {
                this.sell_id = sell_id;
            }

            public String getSell_name() {
                return sell_name;
            }

            public void setSell_name(String sell_name) {
                this.sell_name = sell_name;
            }

            public String getSell_price() {
                return sell_price;
            }

            public void setSell_price(String sell_price) {
                this.sell_price = sell_price;
            }

            public String getBranch_name() {
                return branch_name;
            }

            public void setBranch_name(String branch_name) {
                this.branch_name = branch_name;
            }

            public String getAdmin_emp_id() {
                return admin_emp_id;
            }

            public void setAdmin_emp_id(String admin_emp_id) {
                this.admin_emp_id = admin_emp_id;
            }

            public String getBus_type() {
                return bus_type;
            }

            public void setBus_type(String bus_type) {
                this.bus_type = bus_type;
            }

            public String getMember_id() {
                return member_id;
            }

            public void setMember_id(String member_id) {
                this.member_id = member_id;
            }

            public String getMoney() {
                return money;
            }

            public void setMoney(String money) {
                this.money = money;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }
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

                map.put("branch_id", branchId);

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
