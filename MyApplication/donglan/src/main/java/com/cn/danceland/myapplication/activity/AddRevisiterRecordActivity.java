package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
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
import com.cn.danceland.myapplication.bean.ParamInfoBean;
import com.cn.danceland.myapplication.bean.PotentialInfo;
import com.cn.danceland.myapplication.bean.RequsetSimpleBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by shy on 2018/1/11 19:43
 * Email:644563767@qq.com
 * 添加回访记录
 */


public class AddRevisiterRecordActivity extends Activity implements View.OnClickListener {

    private TextView tv_type;
    private EditText et_content;
    private Gson gson = new Gson();
    private ListPopup listPopup;
    private RequsetBean requsetBean = new RequsetBean();
    private String time;
    PotentialInfo info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_revisiter_record);
        initView();
    }

    private void initView() {
        Bundle bundle = getIntent().getExtras();
        time = bundle.getString("time");
        info = (PotentialInfo) bundle.getSerializable("info");
        requsetBean.member_id =  bundle.getString("id");
        requsetBean.length = time;
        requsetBean.auth = bundle.getString("auth");
        requsetBean.member_name=bundle.getString("member_name");
        requsetBean.member_no=bundle.getString("member_no");
        listPopup = new ListPopup(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_commit).setOnClickListener(this);
        tv_type = findViewById(R.id.tv_type);
        tv_type.setOnClickListener(this);
        et_content = findViewById(R.id.et_content);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_commit:
                if (TextUtils.isEmpty(et_content.getText().toString().trim())) {
                    ToastUtils.showToastShort("回访内容不能为空");
                    return;
                }
                if (TextUtils.isEmpty(requsetBean.type)) {
                    ToastUtils.showToastShort("请选择回访方式");
                    return;
                }
                requsetBean.content = et_content.getText().toString().trim();
                Data data = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
                requsetBean.branch_id = data.getDefault_branch();


                try {
                    add_visit_recor(gson.toJson(requsetBean).toString());
                    LogUtil.i(gson.toJson(requsetBean).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_type://选择回访方式
                findParams(9);
                break;
            default:
                break;
        }
    }

    class RequsetBean {
        public String member_id;
        public String branch_id;
        public String content;//回访内容
        public String member_name;//潜客姓名
        public String type;//回访方式
        public String length;
        public String auth;
        public String member_no;
    }

    /**
     * 添加回访记录
     *
     * @param data
     * @throws JSONException
     */
    public void add_visit_recor(final String data) throws JSONException {


        JSONObject jsonObject = new JSONObject(data);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Constants.ADD_VISIT_RECOR, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtil.i(jsonObject.toString());
                RequsetSimpleBean requestOrderBean = gson.fromJson(jsonObject.toString(), RequsetSimpleBean.class);
                if (requestOrderBean.isSuccess()) {
                    ToastUtils.showToastShort("回访记录添加成功");
                    finish();
                } else {
                    ToastUtils.showToastShort("回访记录添加失败");
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

    private List<ParamInfoBean.Data> mParamInfoList = new ArrayList<>();

    private void findParams(final int codetype) {
        StringRequest request = new StringRequest(Request.Method.GET, Constants.FIND_BY_TYPE_CODE + codetype, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                ParamInfoBean paramInfoBean = gson.fromJson(s, ParamInfoBean.class);
                if (paramInfoBean.getSuccess()) {
                    mParamInfoList = paramInfoBean.getData();

                    listPopup.showPopupWindow();
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
        MyApplication.getHttpQueues().add(request);


    }


    class ListPopup extends BasePopupWindow {


        Context context;

        public ListPopup(Context context) {
            super(context);
            ListView popup_list = (ListView) findViewById(R.id.popup_list);
            MyPopupListAdapter myPopupListAdapter = new MyPopupListAdapter(context);
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
            return createPopupById(R.layout.popup_list_potential);

        }

        @Override
        public View initAnimaView() {
            return null;
        }

        class MyPopupListAdapter extends BaseAdapter {
            private Context context;

            public MyPopupListAdapter(Context context) {
                this.context = context;
            }

            @Override
            public int getCount() {
                return mParamInfoList.size();
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
                vh.mTextView.setText(mParamInfoList.get(position).getData_value());

                vh.mTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        tv_type.setText(mParamInfoList.get(position).getData_value());

                        requsetBean.type = mParamInfoList.get(position).getData_value();
                        listPopup.dismiss();
                    }
                });

                return convertView;

            }


            class ViewHolder {
                public TextView mTextView;
            }
        }
    }
}
