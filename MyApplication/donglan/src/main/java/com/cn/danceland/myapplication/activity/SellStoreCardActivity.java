package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.DLResult;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.RequestOrderPayInfoBean;
import com.cn.danceland.myapplication.bean.SijiaoOrderConfirmBean;
import com.cn.danceland.myapplication.bean.WeiXinBean;
import com.cn.danceland.myapplication.bean.explain.Explain;
import com.cn.danceland.myapplication.bean.explain.ExplainCond;
import com.cn.danceland.myapplication.bean.explain.ExplainRequest;
import com.cn.danceland.myapplication.bean.store.storetype.StoreType;
import com.cn.danceland.myapplication.bean.store.storetype.StoreTypeRequest;
import com.cn.danceland.myapplication.evntbus.StringEvent;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.cn.danceland.myapplication.view.DongLanTitleView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.vondear.rxtools.module.alipay.PayResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by feng on 2018/3/14.
 */

public class SellStoreCardActivity extends Activity {
    private String unpaidOrder;
    StoreType cardid;
    LinearLayout ll_zhifu, btn_repay;
    private StoreTypeRequest request;
    private Gson gson;
    private SimpleDateFormat sdf;
    Data info;
    //StoreType storeType;
    CheckBox btn_weixin, btn_zhifubao, cb_shuoming;
    TextView storecard_tv, tv_price;
    DongLanTitleView storecard_title;
    String zhifu;
    public static final int SDK_PAY_FLAG = 0x1001;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    LogUtil.i(payResult.toString());
                    switch (payResult.getResultStatus()) {
                        case "9000":
                            ToastUtils.showToastShort("支付成功");
                            finish();
                            break;
                        case "8000":
                            ToastUtils.showToastShort("正在处理中");
                            break;
                        case "4000":
                            ToastUtils.showToastShort("订单支付失败");
                            btn_repay.setVisibility(View.VISIBLE);
                            btn_weixin.setClickable(false);
                            break;
                        case "5000":
                            ToastUtils.showToastShort("重复请求");
                            break;
                        case "6001":
                            ToastUtils.showToastShort("已取消支付");
                            btn_repay.setVisibility(View.VISIBLE);
                            btn_weixin.setClickable(false);
                            break;
                        case "6002":
                            ToastUtils.showToastShort("网络连接出错");
                            btn_repay.setVisibility(View.VISIBLE);
                            btn_weixin.setClickable(false);
                            break;
                        case "6004":
                            ToastUtils.showToastShort("正在处理中");
                            break;
                        default:
                            ToastUtils.showToastShort("支付失败");
                            btn_repay.setVisibility(View.VISIBLE);
                            btn_weixin.setClickable(false);
                            break;
                    }


                    break;
                default:
                    break;
            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (Constants.DEV_CONFIG) {
            EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);//支付宝沙箱环境
        }
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.storecardbill);
        initHost();
        initView();
        queryList();

    }

    //even事件处理
    @Subscribe
    public void onEventMainThread(StringEvent event) {
        if (event.getEventCode() == 40001) {
            ToastUtils.showToastShort("支付成功");
            finish();
        }
        if (event.getEventCode() == 40002) {
            ToastUtils.showToastShort("支付失败");
            btn_zhifubao.setClickable(false);
            btn_repay.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 支付宝支付
     */
    private void alipay(final String orderInfo) {

        unpaidOrder = orderInfo;
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(SellStoreCardActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * @方法说明:按条件查询说明须知列表
     **/
    public void queryList() {
        ExplainRequest request = new ExplainRequest();
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        ExplainCond cond = new ExplainCond();
        cond.setBranch_id(Long.valueOf(info.getPerson().getDefault_branch()));
        cond.setType(Byte.valueOf("3"));// 1 买卡须知 2 买私教须知 3 买储值须知 4 买卡说明 5 买私教说明

        request.queryList(cond, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject json) {
                DLResult<List<Explain>> result = gson.fromJson(json.toString(), new TypeToken<DLResult<List<Explain>>>() {
                }.getType());
                if (result.isSuccess()) {
                    List<Explain> list = result.getData();
                    if (list != null && list.size() > 0) {
                        storecard_tv.setText(list.get(0).getContent());
                    }
                } else {
                    ToastUtils.showToastShort("查询分页列表失败,请检查手机网络！");
                }
            }
        });
    }

    private void initHost() {
        request = new StoreTypeRequest();
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cardid = (StoreType) getIntent().getSerializableExtra("item");
        info = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
        initWechat();
    }

    private void initView() {

        storecard_title = findViewById(R.id.store_title);
        storecard_title.setTitle("充值");
        btn_weixin = findViewById(R.id.btn_weixin);
        btn_zhifubao = findViewById(R.id.btn_zhifubao);
        storecard_tv = findViewById(R.id.storecard_tv);
        tv_price = findViewById(R.id.tv_price);
        cb_shuoming = findViewById(R.id.cb_shuoming);
        btn_repay = findViewById(R.id.btn_repay);

        btn_zhifubao.setChecked(true);
        zhifu = "2";

        btn_zhifubao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_zhifubao.setChecked(true);
                btn_weixin.setChecked(false);
                zhifu = "2";
            }
        });

        btn_weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_zhifubao.setChecked(false);
                btn_weixin.setChecked(true);
                zhifu = "3";
            }
        });

        btn_repay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("2".equals(zhifu)) {
                    alipay(unpaidOrder);
                } else if ("3".equals(zhifu)) {
                    wxPay(unpaidOrder);
                }
            }
        });

        ll_zhifu = findViewById(R.id.ll_zhifu);
        ll_zhifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_shuoming.isChecked()) {
                    if (cardid != null) {
                        confirmOrder(cardid.getFace() + "");
                    } else {
                        ToastUtils.showToastShort("获取面额失败");
                    }
                } else {
                    ToastUtils.showToastShort("请阅读购买说明，并同意");
                }


            }
        });
        if (cardid != null) {
            tv_price.setText("待支付：￥" + cardid.getFace() + "元");
        }
        //findById();
    }

//    public void findById() {
//        Long id = null;
//        // TODO 准备数据
//        if(cardid!=null){
//            id = cardid.getId();
//        }
//        request.findById(id, new Response.Listener<String>() {
//            public void onResponse(String res) {
//                DLResult<StoreType> result = gson.fromJson(res, new TypeToken<DLResult<StoreType>>() {
//                }.getType());
//                if (result.isSuccess()) {
//                    LogUtil.e("zzf",res);
//
//                    storeType = result.getData();
//
//
//                } else {
//                    ToastUtils.showToastShort("请检查手机网络！");
//                }
//            }
//        });
//
//    }

    private void confirmOrder(String price) {

        SijiaoOrderConfirmBean sijiaoOrderConfirmBean = new SijiaoOrderConfirmBean();
        SijiaoOrderConfirmBean.Extends_params extends_params = sijiaoOrderConfirmBean.new Extends_params();
        sijiaoOrderConfirmBean.setPay_way(zhifu);//2支付宝
        sijiaoOrderConfirmBean.setPlatform(1);
        sijiaoOrderConfirmBean.setBranch_id(Integer.valueOf(info.getPerson().getDefault_branch()));
        sijiaoOrderConfirmBean.setBus_type(16);
        extends_params.setStore_type_id(cardid.getId() + "");
        extends_params.setFace(cardid.getFace() + "");
        extends_params.setGiving(cardid.getGiving() + "");
        sijiaoOrderConfirmBean.setReceive(price);
        sijiaoOrderConfirmBean.setPrice(price);
        sijiaoOrderConfirmBean.setProduct_type("储值卡充值");
        sijiaoOrderConfirmBean.setProduct_name("");
        sijiaoOrderConfirmBean.setExtends_params(extends_params);
        String s = gson.toJson(sijiaoOrderConfirmBean);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.COMMIT_CARD_ORDER, s, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Gson gson = new Gson();
                RequestOrderPayInfoBean requestOrderInfoBean = gson.fromJson(jsonObject.toString(), RequestOrderPayInfoBean.class);

                if (requestOrderInfoBean.getSuccess()) {
                    ll_zhifu.setVisibility(View.GONE);
                    if (requestOrderInfoBean.getData() != null) {
                        if (requestOrderInfoBean.getData().getPayWay() == 2) {
                            alipay(requestOrderInfoBean.getData().getPay_params());
                        }
                        if (requestOrderInfoBean.getData().getPayWay() == 3) {
                            wxPay(requestOrderInfoBean.getData().getPay_params());
                        }
                    }
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


        MyApplication.getHttpQueues().add(jsonObjectRequest);

    }


    /**
     * 微信支付
     */
    private IWXAPI api;


    /**
     * 初始化微信支付api
     */
    private void initWechat() {
        api = WXAPIFactory.createWXAPI(this, "wx530b17b3c2de2e0d", true);
        api.registerApp("wx530b17b3c2de2e0d");
    }

    /****
     * 微信支付
     * @param orderInfo 订单信息
     */
    private void wxPay(String orderInfo) {
        unpaidOrder = orderInfo;
        orderInfo = orderInfo.replaceAll("package", "packageValue");
        WeiXinBean wxOrderBean = new Gson().fromJson(orderInfo.toString(), WeiXinBean.class);
        LogUtil.i(wxOrderBean.toString());
        sendPayRequest(wxOrderBean);

    }


    /**
     * 调用微信支付
     */
    public void sendPayRequest(WeiXinBean weiXinBean) {

        PayReq req = new PayReq();
        req.appId = weiXinBean.getAppid();
        req.partnerId = weiXinBean.getPartnerid();
        //预支付订单
        req.prepayId = weiXinBean.getPrepayid();
        req.nonceStr = weiXinBean.getNoncestr();
        req.timeStamp = weiXinBean.getTimestamp() + "";
        req.packageValue = weiXinBean.getPackageValue();
        req.sign = weiXinBean.getSign();

        api.sendReq(req);
    }

//    /**
//     * 支付宝支付
//     */
//    private void alipay(String id) {
//        PayBean payBean = new PayBean();
//        payBean.id = id;
//        payBean.order_no = 12345 + "";
//        payBean.price = cardid.getFace();
//        payBean.bus_type = 16;
//        payBean.member_id = info.getMember().getId();
//
//        String str = gson.toJson(payBean);
//
//        JSONObject jsonObject = null;
//        try {
//            jsonObject = new JSONObject(str);
//            LogUtil.i(jsonObject.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Constants.COMMIT_ALIPAY, jsonObject, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject jsonObject) {
//                LogUtil.i(jsonObject.toString());
//                RequestSimpleBean requestSimpleBean = gson.fromJson(jsonObject.toString(), RequestSimpleBean.class);
//                if (requestSimpleBean.getSuccess()) {
//                    ToastUtils.showToastShort("支付成功");
//                    finish();
//                } else {
//                    ToastUtils.showToastShort("支付失败");
//                }
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//
//                ToastUtils.showToastShort(volleyError.toString());
//
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> map = new HashMap<String, String>();
//                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));
//                return map;
//            }
//        };
//        MyApplication.getHttpQueues().add(stringRequest);
//
//    }

    class PayBean {
        public String id;
        public String order_no;
        public float price;
        public int bus_type;
        public String member_id;


        @Override
        public String toString() {
            return "PayBean{" +
                    "id='" + id + '\'' +
                    ", order_no='" + order_no + '\'' +
                    ", price='" + price + '\'' +
                    '}';
        }


    }
}
