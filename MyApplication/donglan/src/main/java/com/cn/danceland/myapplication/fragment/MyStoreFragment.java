package com.cn.danceland.myapplication.fragment;

import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.DLResult;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.store.storeaccount.StoreAccount;
import com.cn.danceland.myapplication.bean.store.storeaccount.StoreAccountCond;
import com.cn.danceland.myapplication.bean.store.storeaccount.StoreAccountRequest;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by feng on 2018/3/14.
 */

public class MyStoreFragment extends BaseFragment {

    private StoreAccountRequest request;
    private Gson gson;
    private SimpleDateFormat sdf;
    Data info;
    TextView tv_mystore;


    @Override
    public View initViews() {

        View view = View.inflate(mActivity, R.layout.mystore, null);

        tv_mystore = view.findViewById(R.id.tv_mystore);
        initHost();

        queryList();




        return view;
    }

    private void initHost() {
        request = new StoreAccountRequest();

        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        info = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
    }

    @Override
    public void onClick(View v) {

    }


    /**
     * @方法说明:按条件查询储值帐户列表
     **/
    public void queryList() {
        StoreAccountCond cond = new StoreAccountCond();
        // TODO 准备查询条件
        request.queryList(cond, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject json) {
                DLResult<List<StoreAccount>> result = gson.fromJson(json.toString(), new TypeToken<DLResult<List<StoreAccount>>>() {
                }.getType());
                if (result.isSuccess()) {
                    List<StoreAccount> list = result.getData();
                    tv_mystore.setText("我的余额："+list.get(0).getRemain()+"");
                    // TODO 请求成功后的代码
                    LogUtil.e("zzf",json.toString());

                } else {
                    ToastUtils.showToastShort("查询分页列表失败,请检查手机网络！");
                }
            }
        });
    }
}
