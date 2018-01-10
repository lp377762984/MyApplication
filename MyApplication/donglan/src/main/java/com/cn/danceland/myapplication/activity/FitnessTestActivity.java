package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mapapi.map.Text;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.FitnessTestBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * Created by feng on 2017/12/27.
 */

public class FitnessTestActivity extends Activity {
    PieChartView pie_chart;
    PieChartData pieChardata;
    RelativeLayout rl_age;
    ImageView fitness_back;
    Gson gson;
    Data myInfo;
    String id;
    String bcaId;
    TextView tv_age,tv_height_mubiao,tv_height_kongzhi,tv_fat_kongzhi,tv_muscle_kongzhi
            ,tv_height_dengji,tv_fat_baifenbi,tv_fat_yaotunbi,tv_danbaizhi,tv_fat_yingyang,tv_wujiyan
            ,tv_jichudaixie,tv_zuoshangzhi,tv_youshangzhi,tv_zuoxiazhi,tv_youxiazhi,tv_qugan,tv_neizang
            ,tv_shuifenlv,tv_neiye,tv_waiye,tv_zuoshangzhishuifen,tv_zuoxiazhishuifen,tv_youshangzhishuifen
            ,tv_youxiazhishuifen,tv_xishu,history,no_data;
    ScrollView sv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fitnesstest);
        initHost();
        initView();
        bcaId = getIntent().getStringExtra("bcaId");
        if(bcaId!=null){
            rl_age.setClickable(false);
            initHistory();
        }else{
            initData();
        }
    }

    private void initHost() {
        myInfo = (Data)DataInfoCache.loadOneCache(Constants.MY_INFO);
        gson = new Gson();
        id = getIntent().getStringExtra("id");
        if(id==null){
            id = myInfo.getId();
        }
    }

    private void initHistory(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.FINDONEHISTORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(s.contains("true")){
                    FitnessTestBean fitnessTestBean = gson.fromJson(s, FitnessTestBean.class);
                    if(fitnessTestBean!=null){
                        FitnessTestBean.Data data = fitnessTestBean.getData();
                        if(data!=null){
                            setData(data);
                        }
                    }
                }else {
                    //noPie();
                    sv.setVisibility(View.GONE);
                    no_data.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                sv.setVisibility(View.GONE);
                no_data.setVisibility(View.VISIBLE);
                ToastUtils.showToastShort("请检查网络！");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<String,String>();
                map.put("bcaId",bcaId);
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

    private void initData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.FIND_BC_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(s.contains("true")){
                    FitnessTestBean fitnessTestBean = gson.fromJson(s, FitnessTestBean.class);
                    if(fitnessTestBean!=null){
                        FitnessTestBean.Data data = fitnessTestBean.getData();
                        if(data!=null){
                            setData(data);
                        }
                    }
                }else {
                    //noPie();
                    sv.setVisibility(View.GONE);
                    no_data.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                sv.setVisibility(View.GONE);
                no_data.setVisibility(View.VISIBLE);
                ToastUtils.showToastShort("请检查网络！");
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<String,String>();
                map.put("memberId",id);
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

    private void setData(FitnessTestBean.Data data){
        tv_age.setText(data.getBodyage());
        tv_height_mubiao.setText(data.getStandard_weight());
        tv_height_kongzhi.setText(data.getWeight_control());
        tv_fat_kongzhi.setText(data.getFat_control());
        tv_muscle_kongzhi.setText(data.getMuscle_control());
        tv_height_dengji.setText(data.getLbm());
        tv_fat_baifenbi.setText(data.getPbf());
        tv_fat_yaotunbi.setText(data.getWhr());
        tv_danbaizhi.setText(data.getProtein());
        tv_fat_yingyang.setText(data.getFat());
        //tv_wujiyan.setText(data.get);
        tv_jichudaixie.setText(data.getBmr());
        tv_zuoshangzhi.setText(data.getLa_fat());
        tv_youshangzhi.setText(data.getRa_fat());
        tv_zuoxiazhi.setText(data.getLl_fat());
        tv_youxiazhi.setText(data.getRl_fat());
        tv_qugan.setText(data.getTr_fat());
        initPie(data);


    }

    private void initView() {
        no_data = findViewById(R.id.no_data);
        sv = findViewById(R.id.sv);
        pie_chart = findViewById(R.id.pie_chart);
        fitness_back = findViewById(R.id.fitness_back);
        fitness_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        history = findViewById(R.id.history);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FitnessTestActivity.this,FitnessHistoryActivity.class).putExtra("id",id));
            }
        });


        rl_age = findViewById(R.id.rl_age);
        rl_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FitnessTestActivity.this,BodyAgeActivity.class);
                startActivity(intent);
            }
        });


        tv_age = findViewById(R.id.tv_age);
        tv_height_mubiao = findViewById(R.id.tv_height_mubiao);
        tv_height_kongzhi = findViewById(R.id.tv_height_kongzhi);
        tv_fat_kongzhi = findViewById(R.id.tv_fat_kongzhi);
        tv_muscle_kongzhi = findViewById(R.id.tv_muscle_kongzhi);
        tv_height_dengji = findViewById(R.id.tv_height_dengji);
        tv_fat_baifenbi = findViewById(R.id.tv_fat_baifenbi);
        tv_fat_yaotunbi = findViewById(R.id.tv_fat_yaotunbi);
        tv_danbaizhi = findViewById(R.id.tv_danbaizhi);
        tv_fat_yingyang = findViewById(R.id.tv_fat_yingyang);
        tv_wujiyan = findViewById(R.id.tv_wujiyan);
        tv_jichudaixie = findViewById(R.id.tv_jichudaixie);
        tv_zuoshangzhi = findViewById(R.id.tv_zuoshangzhi);
        tv_youshangzhi = findViewById(R.id.tv_youshangzhi);
        tv_zuoxiazhi = findViewById(R.id.tv_zuoxiazhi);
        tv_youxiazhi = findViewById(R.id.tv_youxiazhi);
        tv_qugan = findViewById(R.id.tv_qugan);
        tv_neizang = findViewById(R.id.tv_neizang);
        tv_shuifenlv = findViewById(R.id.tv_shuifenlv);
        tv_neiye = findViewById(R.id.tv_neiye);
        tv_waiye = findViewById(R.id.tv_waiye);
        tv_zuoshangzhishuifen = findViewById(R.id.tv_zuoshangzhishuifen);
        tv_zuoxiazhishuifen = findViewById(R.id.tv_zuoxiazhishuifen);
        tv_youshangzhishuifen = findViewById(R.id.tv_youshangzhishuifen);
        tv_youxiazhishuifen = findViewById(R.id.tv_youxiazhishuifen);
        tv_xishu = findViewById(R.id.tv_xishu);


    }

    private void initPie(FitnessTestBean.Data data){
        /**
         * 初始化
         */
        List<SliceValue> values = new ArrayList<SliceValue>();
        pieChardata = new PieChartData();
            pieChardata.setHasLabels(true);//显示表情
            pieChardata.setHasLabelsOnlyForSelected(false);//不用点击显示占的百分比
            pieChardata.setHasLabelsOutside(true);//占的百分比是否显示在饼图外面
            pieChardata.setHasCenterCircle(true);//是否是环形显示
            pieChardata.setSlicesSpacing(0);
        if(data.getWater()!=null&&data.getFat()!=null&&data.getBone()!=null&&data.getProtein()!=null){
            Double[] lv = {Double.valueOf(data.getWater()),Double.valueOf(data.getFat()),Double.valueOf(data.getBone()),Double.valueOf(data.getProtein())};
            Integer[] color = {0xFFF5F5F5,0xFFD3D3D3,0xFFA9A9A9,0xFF696969};
            String[] str = {"水分","脂肪","骨质","蛋白质"};
            for(int i=0;i<=3;i++){
                SliceValue sliceValue = new SliceValue(lv[i].floatValue(), color[i]);//这里的颜色是我写了一个工具类 是随机选择颜色的
                sliceValue.setLabel(str[i]+" "+lv[i]+"%");
                values.add(sliceValue);
            }

            pieChardata.setValues(values);//填充数据
            pieChardata.setCenterCircleColor(Color.WHITE);//设置环形中间的颜色
            pieChardata.setCenterCircleScale(0.65f);//设置环形的大小级别
            //pieChardata.setCenterText1("身体成分");//环形中间的文字1
            pieChardata.setCenterText1Color(Color.BLACK);//文字颜色
            pieChardata.setCenterText1FontSize(12);//文字大小

            pie_chart.setPieChartData(pieChardata);
            pie_chart.setViewportCalculationEnabled(true);
            pie_chart.setValueSelectionEnabled(false);//选择饼图某一块变大
            pie_chart.setAlpha(0.9f);//设置透明度
            pie_chart.setCircleFillRatio(1f);//设置饼图大小

        }

    }

}
