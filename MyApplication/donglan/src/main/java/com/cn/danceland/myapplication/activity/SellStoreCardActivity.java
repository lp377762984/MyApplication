package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
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
import com.cn.danceland.myapplication.bean.explain.Explain;
import com.cn.danceland.myapplication.bean.explain.ExplainCond;
import com.cn.danceland.myapplication.bean.explain.ExplainRequest;
import com.cn.danceland.myapplication.bean.store.storetype.StoreType;
import com.cn.danceland.myapplication.bean.store.storetype.StoreTypeRequest;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.cn.danceland.myapplication.view.DongLanTitleView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by feng on 2018/3/14.
 */

public class SellStoreCardActivity extends Activity{
    StoreType cardid;
    LinearLayout ll_zhifu;
    private StoreTypeRequest request;
    private Gson gson;
    private SimpleDateFormat sdf;
    Data info;
    StoreType storeType;
    CheckBox btn_weixin,btn_zhifubao,cb_shuoming;
    TextView storecard_tv,tv_price;
    DongLanTitleView storecard_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storecardbill);
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
        cond.setType(Byte.valueOf("3"));// 1 买卡须知 2 买私教须知 3 买储值须知 4 买卡说明 5 买私教说明

        request.queryList(cond, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject json) {
                DLResult<List<Explain>> result = gson.fromJson(json.toString(), new TypeToken<DLResult<List<Explain>>>() {
                }.getType());
                if (result.isSuccess()) {
                    List<Explain> list = result.getData();
                    if(list!=null&&list.size()>0){
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
        sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cardid = (StoreType)getIntent().getSerializableExtra("item");
        info = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
    }

    private void initView() {

        storecard_title = findViewById(R.id.store_title);
        storecard_title.setTitle("充值");
        btn_weixin = findViewById(R.id.btn_weixin);
        btn_zhifubao = findViewById(R.id.btn_zhifubao);
        storecard_tv = findViewById(R.id.storecard_tv);
        tv_price = findViewById(R.id.tv_price);
        cb_shuoming = findViewById(R.id.cb_shuoming);

        btn_zhifubao.setChecked(true);

        ll_zhifu = findViewById(R.id.ll_zhifu);
        ll_zhifu.setOnClickListener(new View.OnClickListener() {
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
                    tv_price.setText("待支付：￥"+storeType.getFace()+"元");

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
        sijiaoOrderConfirmBean.setBranch_id(Integer.valueOf(info.getPerson().getDefault_branch()));
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
                    finish();
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
