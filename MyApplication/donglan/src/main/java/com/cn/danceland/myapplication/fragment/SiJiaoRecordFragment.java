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
import com.cn.danceland.myapplication.bean.SiJiaoRecordBean;
import com.cn.danceland.myapplication.bean.SiJiaoYuYueConBean;
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
 * Created by feng on 2018/3/7.
 */

public class SiJiaoRecordFragment extends BaseFragment {

    MyListView lv_tuanke;
    View inflate;
    Data data;
    Gson gson;
    String startTime,role,auth;

    @Override
    public View initViews() {

        inflate = View.inflate(mActivity, R.layout.tuanke, null);//界面类似，使用团课列表布局

        data = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);

        gson = new Gson();

        initView();
        try {
            initData();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return inflate;
    }

    public void getStartTime(String startTime){
        this.startTime =startTime;

    }

    public void getRoles(String role,String auth){
        this.role = role;
        this.auth = auth;

    }

    private void initData() throws JSONException {

        SiJiaoYuYueConBean siJiaoYuYueConBean = new SiJiaoYuYueConBean();
        if(role!=null||!"".equals(role)){
            siJiaoYuYueConBean.setEmployee_id(data.getEmployee().getId());
        }else{
            siJiaoYuYueConBean.setMember_no(data.getPerson().getMember_no());
        }

        if(startTime!=null){
            siJiaoYuYueConBean.setCourse_date(Long.valueOf(startTime+"000"));
        }else{
            siJiaoYuYueConBean.setCourse_date(System.currentTimeMillis());
        }

        //siJiaoYuYueConBean.setEmployee_id(32);
        String s = gson.toJson(siJiaoYuYueConBean);

        JSONObject jsonObject = new JSONObject(s);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.APPOINTLIST, jsonObject,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtil.e("zzf",jsonObject.toString());
                SiJiaoRecordBean siJiaoRecordBean = gson.fromJson(jsonObject.toString(), SiJiaoRecordBean.class);
                if(siJiaoRecordBean!=null){
                    SiJiaoRecordBean.Data data = siJiaoRecordBean.getData();
                    if(data!=null){
                        List<SiJiaoRecordBean.Content> content = data.getContent();
                        if(content!=null){
                            lv_tuanke.setAdapter(new RecordAdapter(content));
                        }else{
                            ToastUtils.showToastShort("当天无预约记录");
                        }

                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.e("zzf",volleyError.toString());
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
        lv_tuanke = inflate.findViewById(R.id.lv_tuanke);


    }

    @Override
    public void onClick(View v) {

    }

    private class RecordAdapter extends BaseAdapter{

        List<SiJiaoRecordBean.Content> list;

        RecordAdapter(List<SiJiaoRecordBean.Content> list){
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
            String time = TimeUtils.timeStamp2Date(list.get(position).getConfirm_date() + "", null);
            viewHolder.course_date.setText("预约时间:"+time);
            viewHolder.course_type.setText("一对一");
            viewHolder.course_jiaolian.setText("上课教练:"+list.get(position).getEmployee_name());

            viewHolder.rl_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(list.get(position).getId(),viewHolder.rl_button,viewHolder.rl_button_tv);
                }

            });
            if(list.get(position).getStatus()==3){
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

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.APPOINTCANCEL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.e("zzf",s);
                if(Integer.valueOf(s)>0){
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
                map.put("courseAppointId",id+"");
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
