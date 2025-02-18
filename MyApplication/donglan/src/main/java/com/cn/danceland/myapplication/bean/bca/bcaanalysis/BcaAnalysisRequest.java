package com.cn.danceland.myapplication.bean.bca.bcaanalysis;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.MyJsonObjectRequest;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.MyStringRequest;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @友情提示: 请清理掉用不到的代码包括这段注释
 **/

/**
 * @类说明:体测分析--网络请求层
 * @author:高振中
 * @date:2018-04-08 13:41:17
 **/
public class BcaAnalysisRequest {

	/**
	 * @方法说明:新增体测分析
	 **/
	public void save(BcaAnalysis bcaAnalysis, Listener<JSONObject> listener) {
		JSONObject json = null;
		try {
			json = new JSONObject(new Gson().toJson(bcaAnalysis));
			LogUtil.i("体测提交参数"+json.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MyJsonObjectRequest request = new MyJsonObjectRequest(1, Constants.HOST + "bcaAnalysis/save", json, listener, new Response.ErrorListener() {
			public void onErrorResponse(VolleyError error) {
				ToastUtils.showToastShort("请检查手机网络！");
			}
		});
		MyApplication.getHttpQueues().add(request);
	}

	

	/**
	 * @方法说明:修改体测分析
	 **/
	public void update(BcaAnalysis bcaAnalysis, Listener<JSONObject> listener) {
		JSONObject json = null;
		try {
			json = new JSONObject(new Gson().toJson(bcaAnalysis));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MyJsonObjectRequest request = new MyJsonObjectRequest(1, Constants.HOST + "bcaAnalysis/update", json, listener, new Response.ErrorListener() {
			public void onErrorResponse(VolleyError error) {
				ToastUtils.showToastShort("请检查手机网络！");
			}
		});
		MyApplication.getHttpQueues().add(request);
	}


	/**
	 * @方法说明:按主键删除体测分析
	 **/
	public void delete(Long id, Listener<String> listener) {
		MyStringRequest request = new MyStringRequest(1, Constants.HOST + "bcaAnalysis/delete?id=" + id, listener, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {
				ToastUtils.showToastShort("请检查手机网络！");
			}
		}) {

		};
		MyApplication.getHttpQueues().add(request);
	}


	/**
	 * @方法说明:按条件查询体测分析列表
	 **/
	public void queryList(BcaAnalysisCond cond, Listener<JSONObject> listener) {
		JSONObject json = null;
		try {
			json = new JSONObject(new Gson().toJson(cond));
		} catch (JSONException e) {
			// LogUtil.i(bcaAnalysis.toString());
			e.printStackTrace();
		}
		MyJsonObjectRequest request = new MyJsonObjectRequest(1, Constants.HOST + "bcaAnalysis/queryList", json, listener, new Response.ErrorListener() {
			public void onErrorResponse(VolleyError error) {
				ToastUtils.showToastShort("请检查手机网络！");
			}
		}) ;
		MyApplication.getHttpQueues().add(request);
	}

 

	/**
	 * @方法说明:按条件查询体测分析分页列表
	 **/
	public void queryPage(BcaAnalysisCond cond, Listener<JSONObject> listener) {

		JSONObject json = null;
		try {
			json = new JSONObject(new Gson().toJson(cond));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MyJsonObjectRequest request = new MyJsonObjectRequest(1, Constants.HOST + "bcaAnalysis/queryPage", json, listener, new Response.ErrorListener() {
			public void onErrorResponse(VolleyError error) {
				ToastUtils.showToastShort("请检查手机网络！");
			}
		});
		MyApplication.getHttpQueues().add(request);
	}

 

	/**
	 * @方法说明:按主键查询体测分析单个数据
	 **/
	public void findById(Long id, Listener<String> listener) {
		MyStringRequest request = new MyStringRequest(1, Constants.HOST + "bcaAnalysis/findById?id=" + id, listener, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {
				ToastUtils.showToastShort("请检查手机网络！");
			}
		}) {

		};
		MyApplication.getHttpQueues().add(request);
	}

 

	/**
	 * @方法说明:按条件查询体测分析数据个数
	 **/
	public void queryCount(BcaAnalysisCond cond, Listener<JSONObject> listener) {
		JSONObject json = null;
		try {
			json = new JSONObject(new Gson().toJson(cond));
		} catch (JSONException e) {
			// LogUtil.i(bcaAnalysis.toString());
			e.printStackTrace();
		}
		MyJsonObjectRequest request = new MyJsonObjectRequest(1, Constants.HOST + "bcaAnalysis/queryCount", json, listener, new Response.ErrorListener() {
			public void onErrorResponse(VolleyError error) {
				ToastUtils.showToastShort("请检查手机网络！");
			}
		});
		MyApplication.getHttpQueues().add(request);
	}
}