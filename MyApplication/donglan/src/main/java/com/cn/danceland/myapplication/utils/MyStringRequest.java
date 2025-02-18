package com.cn.danceland.myapplication.utils;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shy on 2018/4/28 11:29
 * Email:644563767@qq.com
 */


public class MyStringRequest extends StringRequest {
    public Response.Listener<String> mListener;

    public MyStringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.mListener = listener;

    }

    public MyStringRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> map = new HashMap<String, String>();

        map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, null));
        map.put("version", Constants.getVersion());
        map.put("platform", Constants.getPlatform());
        map.put("channel", AppUtils.getChannelCode());
    //    LogUtil.e(map.toString());
        return map;

    }
}
