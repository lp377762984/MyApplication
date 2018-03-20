package com.cn.danceland.myapplication.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.activity.TuanKeDetailActivity;
import com.cn.danceland.myapplication.bean.CourseBean;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.GroupClassBean;
import com.cn.danceland.myapplication.bean.KeChengBiaoBean;
import com.cn.danceland.myapplication.bean.SiJiaoYuYueConBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.MyListView;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by feng on 2018/1/11.
 */

public class TuanKeFragment extends BaseFragment {
    FragmentManager fragmentManager;
    MyListView lv_tuanke;
    Gson gson = new Gson();
    Data info;
    String small;
    String from;
    int member_course_id;
    String yuyueStartTime,yuyueEndTime;

    boolean yuyue;
    MyAdapter myAdapter;
    List<KeChengBiaoBean.Data> xiaoTuanList;

    @Override
    public View initViews() {

        View view = View.inflate(mActivity, R.layout.tuanke, null);

        try {
            getData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        lv_tuanke = view.findViewById(R.id.lv_tuanke);

//        myAdapter = new MyAdapter(xiaoTuanList);
//        lv_tuanke.setAdapter(myAdapter);
        lv_tuanke.setDividerHeight(0);
        //ListViewUtil.setListViewHeightBasedOnChildren(lv_tuanke);
        lv_tuanke.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(xiaoTuanList!=null){
                    startActivity(new Intent(mActivity, TuanKeDetailActivity.class).putExtra("groupId",xiaoTuanList.get(position).getId()));
                }
            }
        });



        return view;
    }

    @Override
    public void onClick(View v) {

    }

    private void commitYuyue(KeChengBiaoBean.Data data) throws JSONException {
        if(data!=null){
            SiJiaoYuYueConBean siJiaoYuYueConBean = new SiJiaoYuYueConBean();
            siJiaoYuYueConBean.setGroup_course_id(data.getId());
            if("小团课".equals(from)){
                siJiaoYuYueConBean.setMember_course_id(member_course_id);
            }
            siJiaoYuYueConBean.setCourse_type_id(data.getCourse_type_id());
            siJiaoYuYueConBean.setCourse_type_name(data.getCourse_type_name());
            siJiaoYuYueConBean.setDate(yuyueStartTime+"000");

            String s = gson.toJson(siJiaoYuYueConBean);
            JSONObject jsonObject = new JSONObject(s);
            String url;
            if("小团课".equals(from)){
                url = Constants.GROUPAPPOINT;
            }else{
                url = Constants.FreeCourseApply;
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    if(jsonObject.toString().contains("true")){
                        yuyue = true;
                    }else{
                        yuyue = false;
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    ToastUtils.showToastShort("预约失败！请重新预约！");
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
    }



    public void refresh(String from,String startTime,String endTime,int course_type_id,int member_course_id) throws JSONException {
        this.member_course_id = member_course_id;
        this.from = from;
        yuyueStartTime = startTime;
        yuyueEndTime = endTime;
        SiJiaoYuYueConBean siJiaoYuYueConBean = new SiJiaoYuYueConBean();
        if("小团课".equals(from)){
            siJiaoYuYueConBean.setCourse_type_id(course_type_id);
        }
        siJiaoYuYueConBean.setStart_date(Long.valueOf(startTime+"000"));
        //siJiaoYuYueConBean.setEnd_date(Long.valueOf(endTime+"000"));

        String s = gson.toJson(siJiaoYuYueConBean);

        JSONObject jsonObject = new JSONObject(s);
        String url;
        if("小团课".equals(from)){
            url = Constants.QUERYKECHENGBIAO;
        }else{
            url = Constants.FreeCourse;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,jsonObject ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                KeChengBiaoBean keChengBiaoBean = gson.fromJson(jsonObject.toString(), KeChengBiaoBean.class);
                if(keChengBiaoBean!=null){

                    xiaoTuanList = keChengBiaoBean.getData();
                    lv_tuanke.setAdapter(new MyAdapter(xiaoTuanList));
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


    private void getData() throws JSONException {
        info = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
        GroupClassBean groupClassBean = new GroupClassBean();
        groupClassBean.setPageCount(12);
        groupClassBean.setPage(0);
        groupClassBean.setBranch_id(Integer.valueOf(info.getPerson().getDefault_branch()));
        groupClassBean.setCourse_date(System.currentTimeMillis());
        String s = gson.toJson(groupClassBean);
        JSONObject jsonObject = new JSONObject(s);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.FINDGROUPCLASS, jsonObject,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                CourseBean courseBean = gson.fromJson(jsonObject.toString(), CourseBean.class);
                List<CourseBean.Content> content = courseBean.getData().getContent();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

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




    private class MyAdapter extends BaseAdapter{

        List<KeChengBiaoBean.Data> xiaoTuanList;

        MyAdapter(List<KeChengBiaoBean.Data> xiaoTuanList){
            this.xiaoTuanList = xiaoTuanList;

        }


        @Override
        public int getCount() {
            return xiaoTuanList.size();

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
                convertView = View.inflate(mActivity,R.layout.tuanke_item,null);
                viewHolder.tuanke_img = convertView.findViewById(R.id.tuanke_img);
                viewHolder.tuanke_jibie = convertView.findViewById(R.id.tuanke_jibie);
                viewHolder.tuanke_leibie = convertView.findViewById(R.id.tuanke_leibie);
                viewHolder.tuanke_room = convertView.findViewById(R.id.tuanke_room);
                viewHolder.tuanke_name = convertView.findViewById(R.id.tuanke_name);
                viewHolder.tuanke_time = convertView.findViewById(R.id.tuanke_time);
                viewHolder.tuanke_yuyue = convertView.findViewById(R.id.tuanke_yuyue);
                viewHolder.tv_yuyue = convertView.findViewById(R.id.tv_yuyue);
                viewHolder.renshu = convertView.findViewById(R.id.renshu);
                convertView.setTag(viewHolder);
            }else{
                viewHolder  = (ViewHolder) convertView.getTag();
            }
            String startTime,endTime;
            if(xiaoTuanList.get(position).getStart_time()%60==0){
                startTime = xiaoTuanList.get(position).getStart_time()/60+":00";
            }else{
                startTime = xiaoTuanList.get(position).getStart_time()/60+":"+xiaoTuanList.get(position).getStart_time()%60;
            }

            if(xiaoTuanList.get(position).getEnd_time()%60==0){
                endTime = xiaoTuanList.get(position).getEnd_time()/60+":00";
            }else{
                endTime = xiaoTuanList.get(position).getEnd_time()/60+":"+xiaoTuanList.get(position).getEnd_time()%60;
            }
            viewHolder.tuanke_time.setText(startTime+"-"+endTime);
            viewHolder.tuanke_leibie.setText(xiaoTuanList.get(position).getCourse_type_name());
            viewHolder.tuanke_name.setText(xiaoTuanList.get(position).getEmployee_name());

            if(xiaoTuanList.get(position).getLevel()!=null){
                viewHolder.tuanke_jibie.setText("课程级别:"+xiaoTuanList.get(position).getLevel());
            }
            if(xiaoTuanList.get(position).getRoom_name()!=null){
                viewHolder.tuanke_room.setText(xiaoTuanList.get(position).getRoom_name());
            }else{
                viewHolder.tuanke_room.setText("未知");
            }

            if(xiaoTuanList.get(position).getMax_count()==xiaoTuanList.get(position).getAppoint_count()){
                viewHolder.tuanke_yuyue.setBackgroundColor(Color.parseColor("#A9A9A9"));
                viewHolder.tv_yuyue.setText("预约已满");
                viewHolder.tuanke_yuyue.setClickable(false);
            }else{
                viewHolder.tuanke_yuyue.setClickable(true);
            }



            if("小团课".equals(from)){
                viewHolder.renshu.setVisibility(View.VISIBLE);
                viewHolder.renshu.setText("人数("+xiaoTuanList.get(position).getAppoint_count()+"/"+xiaoTuanList.get(position).getMax_count()+")");
            }else{
                viewHolder.renshu.setVisibility(View.GONE);
            }

            if(xiaoTuanList.get(position).getSelf_appoint_count()>0){
                yuyue = false;//有预约项，无法点击
                viewHolder.tuanke_yuyue.setBackgroundColor(Color.parseColor("#ADFF2F"));
                viewHolder.tv_yuyue.setText("已预约");
            }

            viewHolder.tuanke_yuyue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        commitYuyue(xiaoTuanList.get(position));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(yuyue){
                        viewHolder.tuanke_yuyue.setBackgroundColor(Color.parseColor("#ADFF2F"));
                        viewHolder.tv_yuyue.setText("已预约");
                    }else{
                        ToastUtils.showToastShort("预约失败！请重新预约！");
                    }

                }
            });


            Glide.with(mActivity).load(xiaoTuanList.get(position).getCover_img_url()).into(viewHolder.tuanke_img);

            return convertView;
        }
    }

    class ViewHolder{
        ImageView tuanke_img;
        TextView tuanke_time,tuanke_leibie,tuanke_jibie,tuanke_room,tuanke_name,tv_yuyue,renshu;
        RelativeLayout tuanke_yuyue;

    }
}
