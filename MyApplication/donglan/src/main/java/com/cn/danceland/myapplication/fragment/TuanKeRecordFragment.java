package com.cn.danceland.myapplication.fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
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
import com.cn.danceland.myapplication.bean.SiJiaoYuYueConBean;
import com.cn.danceland.myapplication.bean.TuanKeRecordBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.MyListView;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.TimeUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by feng on 2018/3/12.
 */

public class TuanKeRecordFragment extends BaseFragment {
    View view;
    Data data;
    Gson gson;
    MyListView lv_tuanke;
    String startTime;
    @Override
    public View initViews() {

        view = View.inflate(mActivity, R.layout.tuanke, null);
        data = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
        gson = new Gson();
        initView();
        try {
            initData();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }

    public void getStartTime(String startTime){
        this.startTime =startTime;

    }

    private void initData() throws JSONException {
        SiJiaoYuYueConBean siJiaoYuYueConBean = new SiJiaoYuYueConBean();
        siJiaoYuYueConBean.setMember_no(data.getMember_no());
        siJiaoYuYueConBean.setDate(startTime+"000");

        String s = gson.toJson(siJiaoYuYueConBean);
        JSONObject jsonObject = new JSONObject(s);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.FREECOURSELIST, jsonObject,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtil.e("zzf",jsonObject.toString());
                TuanKeRecordBean tuanKeRecordBean = gson.fromJson(jsonObject.toString(), TuanKeRecordBean.class);
                if(tuanKeRecordBean!=null){

                    List<TuanKeRecordBean.Data> data = tuanKeRecordBean.getData();
                    if(data!=null){
                        lv_tuanke.setAdapter(new RecordAdapter(data));
                    }else{
                        ToastUtils.showToastShort("暂无记录！");
                    }
                }else{
                    ToastUtils.showToastShort("暂无记录！");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToastShort("获取记录失败！");
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

    private void initView() {

        lv_tuanke = view.findViewById(R.id.lv_tuanke);

    }

    @Override
    public void onClick(View v) {

    }



    private class RecordAdapter extends BaseAdapter {

        List<TuanKeRecordBean.Data> list;

        RecordAdapter(List<TuanKeRecordBean.Data> list){
            this.list = list;

        }
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if(convertView==null){
                viewHolder = new ViewHolder();
                convertView = View.inflate(mActivity,R.layout.sijiaorecord_item, null);
                viewHolder.course_name = convertView.findViewById(R.id.course_name);
                viewHolder.course_date = convertView.findViewById(R.id.course_date);
                viewHolder.course_type = convertView.findViewById(R.id.course_type);
                viewHolder.course_jiaolian = convertView.findViewById(R.id.course_jiaolian);
                viewHolder.rl_button = convertView.findViewById(R.id.rl_button);
                viewHolder.rl_button_tv = convertView.findViewById(R.id.rl_button_tv);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.course_name.setText(list.get(position).getCourse_type_name());
            String time = TimeUtils.timeStamp2Date(list.get(position).getCreate_date() + "", null);
            viewHolder.course_date.setText("预约时间:"+time);
            viewHolder.course_type.setText("一对一");
            viewHolder.course_jiaolian.setText("");

            viewHolder.rl_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(list.get(position).getId(),viewHolder.rl_button,viewHolder.rl_button_tv);
                }

            });
            if(list.get(position).getStatus()==2){
                viewHolder.rl_button_tv.setText("已取消");
                viewHolder.rl_button.setBackgroundColor(Color.parseColor("#A9A9A9"));
                viewHolder.rl_button.setClickable(false);
            }else{
                viewHolder.rl_button.setClickable(true);
            }

            return convertView;
        }
    }

    class ViewHolder{
        TextView course_name,course_date,course_type,course_jiaolian,rl_button_tv;
        RelativeLayout rl_button;
    }

    private void showDialog(final int id, final RelativeLayout rl, final TextView tv){

        AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
        dialog.setTitle("提示");
        dialog.setMessage("确定取消预约吗");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancleYuYue(id,rl,tv);
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();

    }


    private void cancleYuYue(final int id, final RelativeLayout rl, final TextView tv) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.FREECANCELGROUP, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.e("zzf",s);
                if(s.contains("1")){
                    ToastUtils.showToastShort("取消成功！");
                    tv.setText("已取消");
                    rl.setBackgroundColor(Color.parseColor("#A9A9A9"));
                    rl.setClickable(false);
                }else{
                    ToastUtils.showToastShort("取消失败！请重新操作");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("applyId",id+"");
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


}
