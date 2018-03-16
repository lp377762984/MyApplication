package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.DLResult;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.RequestOrderInfoBean;
import com.cn.danceland.myapplication.bean.RequestSimpleBean;
import com.cn.danceland.myapplication.bean.SijiaoOrderConfirmBean;
import com.cn.danceland.myapplication.bean.store.storetype.StoreType;
import com.cn.danceland.myapplication.bean.store.storetype.StoreTypeRequest;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by feng on 2018/3/14.
 */

public class SellStoreCardActivity extends Activity{
    StoreType cardid;
    TextView storecardbill_name,storecardbill_price,storecardbill_zengsong
            ,storecardbill_status;
    RelativeLayout rl_button;
    private StoreTypeRequest request;
    private Gson gson;
    private SimpleDateFormat sdf;
    Data info;
    StoreType storeType;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storecardbill);
        initHost();
        initView();


    }

    private void initHost() {
        request = new StoreTypeRequest();
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cardid = (StoreType)getIntent().getSerializableExtra("item");
        info = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
    }

    private void initView() {
        storecardbill_name = findViewById(R.id.storecardbill_name);
        storecardbill_price = findViewById(R.id.storecardbill_price);
        storecardbill_zengsong = findViewById(R.id.storecardbill_zengsong);
        storecardbill_status = findViewById(R.id.storecardbill_status);
        rl_button = findViewById(R.id.rl_button);
        rl_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    confirmOrder(storeType.getFace()+"");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        findById();
    }

    public void findById() {
        Long id = null;
        // TODO 准备数据
        if(cardid!=null){
            id = cardid.getId();
        }
        request.findById(id, new Response.Listener<String>() {
            public void onResponse(String res) {
                DLResult<StoreType> result = gson.fromJson(res, new TypeToken<DLResult<StoreType>>() {
                }.getType());
                if (result.isSuccess()) {
                    LogUtil.e("zzf",res);

                    storeType = result.getData();
                    storecardbill_name.setText(storeType.getName());
                    storecardbill_price.setText(storeType.getFace()+"");
                    storecardbill_zengsong.setText(storeType.getGiving()+"");
                    storecardbill_status.setText(storeType.getEnable().toString());
                } else {
                    ToastUtils.showToastShort("请检查手机网络！");
                }
            }
        });

    }

    private void confirmOrder(String price) throws JSONException {

        SijiaoOrderConfirmBean sijiaoOrderConfirmBean = new SijiaoOrderConfirmBean();
        SijiaoOrderConfirmBean.Extends_params extends_params = sijiaoOrderConfirmBean.new Extends_params();
        sijiaoOrderConfirmBean.setPay_way("1");//1支付宝
        sijiaoOrderConfirmBean.setPlatform(2);
        sijiaoOrderConfirmBean.setBranch_id(Integer.valueOf(info.getDefault_branch()));
        sijiaoOrderConfirmBean.setBus_type(16);
        extends_params.setStore_type_id(cardid.getId()+"");
        extends_params.setFace(cardid.getFace()+"");
        extends_params.setGiving(cardid.getGiving()+"");
        sijiaoOrderConfirmBean.setReceive(price);
        sijiaoOrderConfirmBean.setPrice(price);
        sijiaoOrderConfirmBean.setExtends_params(extends_params);
        String s = gson.toJson(sijiaoOrderConfirmBean);
        JSONObject jsonObject = new JSONObject(s);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.COMMIT_CARD_ORDER, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                RequestOrderInfoBean requestOrderInfoBean = new RequestOrderInfoBean();
                Gson gson = new Gson();
                requestOrderInfoBean = gson.fromJson(jsonObject.toString(), RequestOrderInfoBean.class);

                if (requestOrderInfoBean.getSuccess()) {
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
     * 支付宝支付
     */
    private void alipay(String id) {
        PayBean payBean = new PayBean();
        payBean.id = id;
        payBean.order_no = 12345 + "";
        payBean.price = cardid.getFace();
        payBean.bus_type = 16;
        payBean.member_id = info.getMember().getId();

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

    class PayBean {
        public String id;
        public String order_no;
        public float price;
        public int bus_type;
        public int member_id;


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
