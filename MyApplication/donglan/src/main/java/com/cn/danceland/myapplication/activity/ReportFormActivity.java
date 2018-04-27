package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.ReportCommitBean;
import com.cn.danceland.myapplication.bean.ReportCommitResultBean;
import com.cn.danceland.myapplication.bean.ReportResultBean;
import com.cn.danceland.myapplication.bean.RequestConsultantInfoBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.MyListView;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.cn.danceland.myapplication.view.CustomDatePicker;
import com.cn.danceland.myapplication.view.DongLanTitleView;
import com.cn.danceland.myapplication.view.XCRoundRectImageView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by feng on 2018/4/24.
 */

public class ReportFormActivity extends Activity {
    MyListView report_mv,report_mv_02;
    String role_type,role,emp_id;
    DongLanTitleView report_title;
    Gson gson;
    MyListViewAdapter report_mv_Adapter,report_mv_02_Adapter;
    RecyclerView report_rv;
    String nowDate,selectDate;
    TextView tv_date;
    RelativeLayout btn_date;
    Button btn_all;
    String str_meet,str_clean,str_item_placement,str_body_build,str_sport_device,str_group_course,str_course,str_power,str_door,str_remark;
    Data myInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        initHost();
        initView();
    }

    //业务报表
    private void initBusData(final String date, final String current_role_type, final String target_role_type, final String employee_id) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BUSSTATISTICSREPORT, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                ReportResultBean reportResultBean = gson.fromJson(s, ReportResultBean.class);
                if(reportResultBean!=null){
                    report_mv_Adapter = new MyListViewAdapter(reportResultBean.getData());
                    report_mv.setAdapter(report_mv_Adapter);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.i(volleyError.toString());
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("date",date);
                map.put("current_role_type",current_role_type);
                map.put("target_role_type",target_role_type);
                if(employee_id!=null){
                    map.put("employee_id",employee_id);
                }

                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));
                return map;
            }
        };

        MyApplication.getHttpQueues().add(stringRequest);

    }

    //业绩报表
    private void initScoreData(final String date, final String current_role_type, final String target_role_type, final String employee_id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.SCORESTATISTICSREPORT, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                ReportResultBean reportResultBean = gson.fromJson(s, ReportResultBean.class);
                if(reportResultBean!=null){
                    report_mv_02_Adapter = new MyListViewAdapter(reportResultBean.getData());
                    report_mv_02.setAdapter(report_mv_02_Adapter);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.i(volleyError.toString());
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("date",date);
                map.put("current_role_type",current_role_type);
                map.put("target_role_type",target_role_type);
                if(employee_id!=null){
                    map.put("employee_id",employee_id);
                }

                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));
                return map;
            }
        };

        MyApplication.getHttpQueues().add(stringRequest);

    }

    private void initView() {

        report_mv = findViewById(R.id.report_mv);
        report_mv_02 = findViewById(R.id.report_mv_02);
        tv_date = findViewById(R.id.tv_date);
        tv_date.setText(nowDate);

        btn_date = findViewById(R.id.btn_date);
        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate();
            }
        });

        btn_all = findViewById(R.id.btn_all);
        btn_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emp_id = null;
                if(PreclickText!=null){
                    PreclickText.setTextColor(Color.parseColor("#808080"));
                }
                initBusData(selectDate,role,role,emp_id);
                initScoreData(selectDate,role,role,emp_id);
                initReportData(selectDate,emp_id);
            }
        });

        report_rv = findViewById(R.id.report_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        report_rv.setLayoutManager(linearLayoutManager);
        report_rv.addItemDecoration(new SpacesItemDecoration(40));

        report_title = findViewById(R.id.report_title);
        if("会籍顾问".equals(role_type)){
            role = "1";
            emp_id = myInfo.getEmployee().getId()+"";
            report_rv.setVisibility(View.GONE);
            btn_all.setVisibility(View.GONE);
            report_title.setTitle("会籍报表");
        }else if("教练".equals(role_type)){
            role = "2";
            emp_id = myInfo.getEmployee().getId()+"";
            report_rv.setVisibility(View.GONE);
            btn_all.setVisibility(View.GONE);
            report_title.setTitle("教练报表");
        }else if("店长".equals(role_type)){
            role = "4";
            report_title.setTitle("全店报表");
            getPeople();
        }else if("会籍主管".equals(role_type)){
            role = "5";
            report_title.setTitle("会籍报表");
            getPeople();
        }else if("教练主管".equals(role_type)){
            role = "6";
            report_title.setTitle("教练报表");
            getPeople();
        }else if("操教".equals(role_type)){
            role = "8";
            emp_id = myInfo.getEmployee().getId()+"";
            report_rv.setVisibility(View.GONE);
            btn_all.setVisibility(View.GONE);
            report_title.setTitle("教练报表");
        }else if("兼职教练".equals(role_type)){
            role = "11";
            emp_id = myInfo.getEmployee().getId()+"";
            report_rv.setVisibility(View.GONE);
            btn_all.setVisibility(View.GONE);
            report_title.setTitle("教练报表");
        }

        initBusData(selectDate,role,role,emp_id);
        initScoreData(selectDate,role,role,emp_id);
        initReportData(selectDate,emp_id);

        tv_meet = findViewById(R.id.tv_meet);
        tv_clean = findViewById(R.id.tv_clean);
        tv_item_placement = findViewById(R.id.tv_item_placement);
        tv_body_build = findViewById(R.id.tv_body_build);
        tv_sport_device = findViewById(R.id.tv_sport_device);
        tv_group_course = findViewById(R.id.tv_group_course);
        tv_course = findViewById(R.id.tv_course);
        tv_power = findViewById(R.id.tv_power);
        tv_door = findViewById(R.id.tv_door);
        tv_remark = findViewById(R.id.tv_remark);

        btn_commit = findViewById(R.id.btn_commit);

        tv_meet.setOnClickListener(onClickListener);
        tv_clean.setOnClickListener(onClickListener);
        tv_item_placement.setOnClickListener(onClickListener);
        tv_body_build.setOnClickListener(onClickListener);
        tv_sport_device.setOnClickListener(onClickListener);
        tv_group_course.setOnClickListener(onClickListener);
        tv_course.setOnClickListener(onClickListener);
        tv_power.setOnClickListener(onClickListener);
        tv_door.setOnClickListener(onClickListener);
        tv_remark.setOnClickListener(onClickListener);

        btn_commit.setOnClickListener(onClickListener);

    }

    private void initReportData(final String selectDate, final String emp_id){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.FINDREPORT, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                ReportCommitResultBean reportCommitResultBean = gson.fromJson(s, ReportCommitResultBean.class);
                if(reportCommitResultBean!=null&&reportCommitResultBean.getData()!=null){
                    ReportCommitResultBean.Data data = reportCommitResultBean.getData();
                    tv_meet.setText(data.getMeet()+"");
                    tv_clean.setText(data.getClean()+"");
                    tv_item_placement.setText(data.getItem_placement()+"");
                    tv_body_build.setText(data.getBody_build()+"");
                    tv_sport_device.setText(data.getSport_device()+"");
                    tv_group_course.setText(data.getGroup_course()+"");
                    tv_course.setText(data.getCourse()+"");
                    tv_power.setText(data.getPower()+"");
                    tv_door.setText(data.getDoor()+"");
                    tv_remark.setText(data.getRemark()+"");
                    clickAble = true;
                }
                if(!nowDate.equals(selectDate) || clickAble){
                    tv_meet.setClickable(false);
                    tv_clean.setClickable(false);
                    tv_item_placement.setClickable(false);
                    tv_body_build.setClickable(false);
                    tv_sport_device.setClickable(false);
                    tv_group_course.setClickable(false);
                    tv_course.setClickable(false);
                    tv_power.setClickable(false);
                    tv_door.setClickable(false);
                    tv_remark.setClickable(false);
                    btn_commit.setClickable(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.i(volleyError.toString());
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("employee_id",emp_id);
                map.put("date",selectDate);

                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));
                return map;
            }

        };
        MyApplication.getHttpQueues().add(stringRequest);
    }


    Button btn_commit;

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_meet:
                    startActivityForResult(new Intent(ReportFormActivity.this,ReportEditActivity.class),11);
                    break;
                case R.id.tv_clean:
                    startActivityForResult(new Intent(ReportFormActivity.this,ReportEditActivity.class),12);
                    break;
                case R.id.tv_item_placement:
                    startActivityForResult(new Intent(ReportFormActivity.this,ReportEditActivity.class),13);
                    break;
                case R.id.tv_body_build:
                    startActivityForResult(new Intent(ReportFormActivity.this,ReportEditActivity.class),14);
                    break;
                case R.id.tv_sport_device:
                    startActivityForResult(new Intent(ReportFormActivity.this,ReportEditActivity.class),15);
                    break;
                case R.id.tv_group_course:
                    startActivityForResult(new Intent(ReportFormActivity.this,ReportEditActivity.class),16);
                    break;
                case R.id.tv_course:
                    startActivityForResult(new Intent(ReportFormActivity.this,ReportEditActivity.class),17);
                    break;
                case R.id.tv_power:
                    startActivityForResult(new Intent(ReportFormActivity.this,ReportEditActivity.class),18);
                    break;
                case R.id.tv_door:
                    startActivityForResult(new Intent(ReportFormActivity.this,ReportEditActivity.class),19);
                    break;
                case R.id.tv_remark:
                    startActivityForResult(new Intent(ReportFormActivity.this,ReportEditActivity.class),20);
                    break;
                case R.id.btn_commit:
                    showAleart();
                    break;
            }
        }
    };

    private void showAleart(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("提示");
        builder.setMessage("提交成功后将不能修改！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                commit();
            }
        });
        builder.show();

    }
    boolean clickAble;
    TextView tv_meet,tv_clean,tv_item_placement,tv_body_build,tv_sport_device,tv_group_course,tv_course,tv_power,tv_door,tv_remark;
    private void commit() {
        ReportCommitBean reportCommitBean = new ReportCommitBean();
        reportCommitBean.setMeet(str_meet);
        reportCommitBean.setClean(str_clean);
        reportCommitBean.setItem_placement(str_item_placement);
        reportCommitBean.setBody_build(str_body_build);
        reportCommitBean.setSport_device(str_sport_device);
        reportCommitBean.setGroup_course(str_group_course);
        reportCommitBean.setCourse(str_course);
        reportCommitBean.setPower(str_power);
        reportCommitBean.setDoor(str_door);
        reportCommitBean.setRemark(str_remark);
        //reportCommitBean.setDate(nowDate);

        String s = gson.toJson(reportCommitBean);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.SAVEREPORT, s, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if(jsonObject.toString().contains("true")){
                    clickAble = true;
                    tv_meet.setClickable(false);
                    tv_clean.setClickable(false);
                    tv_item_placement.setClickable(false);
                    tv_body_build.setClickable(false);
                    tv_sport_device.setClickable(false);
                    tv_group_course.setClickable(false);
                    tv_course.setClickable(false);
                    tv_power.setClickable(false);
                    tv_door.setClickable(false);
                    tv_remark.setClickable(false);
                    btn_commit.setClickable(false);
                    ToastUtils.showToastShort("提交成功");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.i(volleyError.toString());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));
                return map;
            }

        };
        MyApplication.getHttpQueues().add(jsonObjectRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1){
            String tv = data.getStringExtra("tv");
            if(requestCode==11){
                str_meet = tv;
                tv_meet.setText(str_meet);
            }else if(requestCode==12){
                str_clean = tv;
                tv_clean.setText(str_clean);
            }else if(requestCode==13){
                str_item_placement = tv;
                tv_item_placement.setText(str_item_placement);
            }else if(requestCode==14){
                str_body_build = tv;
                tv_body_build.setText(str_body_build);
            }else if(requestCode==15){
                str_sport_device = tv;
                tv_sport_device.setText(str_sport_device);
            }else if(requestCode==16){
                str_group_course = tv;
                tv_group_course.setText(str_group_course);
            }else if(requestCode==17){
                str_course = tv;
                tv_course.setText(str_course);
            }else if(requestCode==18){
                str_power = tv;
                tv_power.setText(str_power);
            }else if(requestCode==19){
                str_door = tv;
                tv_door.setText(str_door);
            }else if(requestCode==20){
                str_remark = tv;
                tv_remark.setText(str_remark);
            }
        }
    }

    private void showDate() {

        final CustomDatePicker customDatePicker = new CustomDatePicker(this, "选择日期");
        customDatePicker.setDialogOnClickListener(new CustomDatePicker.OnClickEnter() {
            @Override
            public void onClick() {
                selectDate = customDatePicker.getDateStringF();
                tv_date.setText(selectDate);
                initBusData(selectDate,role,role,emp_id);
                initScoreData(selectDate,role,role,emp_id);
                initReportData(selectDate,emp_id);

            }
        });

        customDatePicker.showWindow();

    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;
        public SpacesItemDecoration(int space) {
            this.space = space;
        }
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            //outRect.bottom = space;
        }
    }

    private void initHost() {
        gson = new Gson();
        role_type = getIntent().getStringExtra("role_type");
        Time time = new Time();
        time.setToNow();


        myInfo = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);

        nowDate = time.year +"-"+(time.month+1)+"-"+time.monthDay;
        selectDate = nowDate;
    }

    private void getPeople(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.FIND_CONSULTANT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                RequestConsultantInfoBean requestConsultantInfoBean = gson.fromJson(s, RequestConsultantInfoBean.class);
                if(requestConsultantInfoBean!=null){
                    List<RequestConsultantInfoBean.Data> data = requestConsultantInfoBean.getData();
                    if(data!=null){
                        report_rv.setAdapter(new MyRecyclerViewAdapter(data));
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("branch_id","2");
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));
                return map;
            }
        };
        MyApplication.getHttpQueues().add(stringRequest);
    }

    TextView PreclickText;
    public class MyRecyclerViewAdapter extends RecyclerView.Adapter<ReportFormActivity.ViewHolder>{
        List<RequestConsultantInfoBean.Data> data;
        int click;


        MyRecyclerViewAdapter(List<RequestConsultantInfoBean.Data> data){
            this.data = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = View.inflate(ReportFormActivity.this, R.layout.report_huiji_item, null);

            ViewHolder viewHolder = new ViewHolder(inflate);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            Glide.with(ReportFormActivity.this).load(data.get(position).getSelf_avatar_path()).into(holder.img_touxiang);
            holder.img_touxiang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(click!=position && PreclickText!=null){
                        PreclickText.setTextColor(Color.parseColor("#808080"));
                    }
                    click = position;
                    PreclickText = holder.tv_name;
                    holder.tv_name.setTextColor(Color.parseColor("#ff6600"));
                    emp_id = data.get(position).getEmployee_id() + "";
                    initBusData(selectDate,role,role,emp_id);
                    initScoreData(selectDate,role,role,emp_id);
                    initReportData(selectDate,emp_id);
                }
            });
            holder.tv_name.setText(data.get(position).getCname());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }


    }

    public  static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        XCRoundRectImageView img_touxiang;
        public ViewHolder(View itemView) {
            super(itemView);
            img_touxiang = itemView.findViewById(R.id.img_touxiang);
            tv_name = itemView.findViewById(R.id.tv_name);
        }
    }

    private class MyListViewAdapter extends BaseAdapter{
        List<ReportResultBean.Data> dataList;

        MyListViewAdapter(List<ReportResultBean.Data> dataList){
            this.dataList = dataList;
        }

        @Override
        public int getCount() {
            return dataList==null? 0:dataList.size();
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
        public View getView(int position, View convertView, ViewGroup parent) {

            View inflate = View.inflate(ReportFormActivity.this, R.layout.report_item, null);
            TextView tv_name = inflate.findViewById(R.id.tv_name);
            TextView tv_today = inflate.findViewById(R.id.tv_today);
            TextView tv_thisMonth = inflate.findViewById(R.id.tv_thisMonth);
            TextView tv_total = inflate.findViewById(R.id.tv_total);
            ReportResultBean.Data data = dataList.get(position);
            tv_name.setText(data.getTitle());
            tv_today.setText(data.getToday()+data.getUnit());
            tv_thisMonth.setText(data.getEndOfToDay()+data.getUnit());
            tv_total.setText(data.getAllOfMonth()+data.getUnit());

            return inflate;
        }
    }
}
