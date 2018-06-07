package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.FitnessTestBean;
import com.cn.danceland.myapplication.utils.AppUtils;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.TimeUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    String member_no;
    String bcaId;
    TextView tv_age, tv_height_mubiao, tv_height_kongzhi, tv_fat_kongzhi, tv_muscle_kongzhi, tv_height_dengji, tv_fat_baifenbi, tv_fat_yaotunbi, tv_danbaizhi, tv_fat_yingyang, tv_wujiyan, tv_jichudaixie, tv_zuoshangzhi, tv_youshangzhi, tv_zuoxiazhi, tv_youxiazhi, tv_qugan, tv_neizang, tv_shuifenlv, tv_neiye, tv_waiye, tv_zuoshangzhishuifen, tv_zuoxiazhishuifen, tv_youshangzhishuifen, tv_youxiazhishuifen, tv_xishu, history, no_data, test_score, test_classify, test_time, tv_line1, tv_line2, tv_line3, tv_line4, tv_line5, tv_line6, tv_line7, tv_line8, tv_tizhong, tv_jirou, tv_tizhilv, tv_guzhi, tv_zongshuifen, tv_gugeji, tv_yaotunbi, tv_tizhishu;
    ScrollView sv;
    String xingbie, height, weight;
    ProgressBar base_line1, base_line2, base_line3, base_line4, base_line5, base_line6, base_line7, base_line8;
    int width, low, normal, high;
    RelativeLayout rl_error;
    ImageView iv_error;
    TextView tv_error;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fitnesstest);
        initHost();
        initView();
        bcaId = getIntent().getStringExtra("bcaId");
        if (bcaId != null) {
            rl_age.setClickable(false);
            initHistory();
        } else {
            initData();
        }
    }

    private void initHost() {
        myInfo = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
        if (xingbie == null) {
            xingbie = myInfo.getPerson().getGender() + "";
        }
        if (height == null) {
            height = myInfo.getPerson().getHeight();
        }
        if (weight == null) {
            weight = myInfo.getPerson().getWeight();
        }
        gson = new Gson();
        if (member_no == null) {
            member_no = myInfo.getPerson().getMember_no();
        }

        width = AppUtils.getWidth();

        low = width / 4;
        normal = width / 2 - 20;
        high = width * 3 / 4 - 40;
    }


    private void initHistory() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.FINDONEHISTORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s.contains("true")) {
                    FitnessTestBean fitnessTestBean = gson.fromJson(s, FitnessTestBean.class);
                    if (fitnessTestBean != null) {
                        FitnessTestBean.Data data = fitnessTestBean.getData();
                        if (data != null) {
                            sv.setVisibility(View.VISIBLE);
                            setData(data);
                        } else {
                            rl_error.setVisibility(View.VISIBLE);
                        }
                    } else {
                        rl_error.setVisibility(View.VISIBLE);
                    }
                } else {
                    rl_error.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                rl_error.setVisibility(View.VISIBLE);
                ToastUtils.showToastShort("请检查网络！");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("bcaId", bcaId);
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

    private void initData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.FIND_BC_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s.contains("true")) {
                    FitnessTestBean fitnessTestBean = gson.fromJson(s, FitnessTestBean.class);
                    if (fitnessTestBean != null) {
                        FitnessTestBean.Data data = fitnessTestBean.getData();
                        if (data != null) {
                            sv.setVisibility(View.VISIBLE);
                            setData(data);
                        } else {
                            rl_error.setVisibility(View.VISIBLE);
                        }
                    } else {
                        rl_error.setVisibility(View.VISIBLE);
                    }
                } else {
                    rl_error.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                rl_error.setVisibility(View.VISIBLE);
                ToastUtils.showToastShort("请检查网络！");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("memberNo", member_no);
                LogUtil.i(member_no);
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

    private void setData(FitnessTestBean.Data data) {
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
        tv_wujiyan.setText(data.getBone());
        tv_jichudaixie.setText(data.getBmr());
        tv_zuoshangzhi.setText(data.getLa_fat());
        tv_youshangzhi.setText(data.getRa_fat());
        tv_zuoxiazhi.setText(data.getLl_fat());
        tv_youxiazhi.setText(data.getRl_fat());
        tv_qugan.setText(data.getTr_fat());
        tv_neizang.setText(data.getVfi());
        tv_shuifenlv.setText(data.getWater());
        tv_neiye.setText(data.getIcw());
        tv_waiye.setText(data.getEcw());
        tv_zuoshangzhishuifen.setText(data.getLa_water());
        tv_youshangzhishuifen.setText(data.getRa_water());
        tv_zuoxiazhishuifen.setText(data.getLl_water());
        tv_youxiazhishuifen.setText(data.getRl_water());
        tv_xishu.setText(data.getEdema());
        test_score.setText(data.getScore());
        test_time.setText("测试日期 " + data.getDate());
        if (weight == null) {
            weight = data.getWeight();
        }
        if (height == null) {
            height = data.getHeight();
        }
        if (weight != null || height != null) {
            setLine("体重", data.getWeight());
            setLine("肌肉", data.getMuscle());
            setLine("体脂百分比", data.getPbf());
            setLine("骨质", data.getBone());
            setLine("总水分", data.getWater());
            setLine("骨骼肌", data.getSmm());
            setLine("体质指数", data.getBmi());
            setLine("腰臀比", data.getWhr());
        }

        test_classify.setText(tiXing(Float.valueOf(data.getBmi()), Float.valueOf(data.getPbf()), xingbie));

        initPie(data);


    }

    private void setLine(String type, String value) {
        double realValue = Float.valueOf(value);
        double dw = Double.valueOf(weight);
        double h = Double.valueOf(height);
        double sm1 = 0.00344 * h * h - 0.37678 * h + 14.40021;
        double sm2 = 0.00351 * h * h - 0.4661 * h + 23.04821;


        if ("体重".equals(type)) {
            tv_line1.setText(value);
            String mi = TimeUtils.convertMi(height);
            double m = Double.valueOf(mi);
            double min = m * m * 18.5;
            double max = m * m * 23.9;
            if (realValue < min) {
                setLowLine(base_line1, tv_line1);
                tv_tizhong.setText("偏低");
            } else if (realValue >= min && realValue <= max) {
                setNormalLine(base_line1, tv_line1);
                tv_tizhong.setText("正常");
            } else {
                setHighLine(base_line1, tv_line1);
                tv_tizhong.setText("偏高");
            }
        } else if ("肌肉".equals(type)) {
            tv_line2.setText(value);
            if ("1".equals(xingbie)) {
                if (realValue < (sm1 - 5)) {
                    setLowLine(base_line2, tv_line2);
                    tv_jirou.setText("偏低");
                } else if (realValue >= (sm1 - 5) && realValue <= (sm1 - 5)) {
                    setNormalLine(base_line2, tv_line2);
                    tv_jirou.setText("正常");
                } else {
                    setHighLine(base_line2, tv_line2);
                    tv_jirou.setText("偏高");
                }
            } else if ("2".equals(xingbie)) {
                if (realValue < (sm2 - 3)) {
                    setLowLine(base_line2, tv_line2);
                    tv_jirou.setText("偏低");
                } else if (realValue >= (sm2 - 3) && realValue <= (sm2 - 3)) {
                    setNormalLine(base_line2, tv_line2);
                    tv_jirou.setText("正常");
                } else {
                    setHighLine(base_line2, tv_line2);
                    tv_jirou.setText("偏高");
                }
            }

        } else if ("体脂百分比".equals(type)) {
            tv_line3.setText(value);
            if ("1".equals(xingbie)) {
                if (realValue < 10) {
                    setLowLine(base_line3, tv_line3);
                    tv_tizhilv.setText("偏低");
                } else if (realValue > 20) {
                    setHighLine(base_line3, tv_line3);
                    tv_tizhilv.setText("偏高");
                } else {
                    setNormalLine(base_line3, tv_line3);
                    tv_tizhilv.setText("正常");
                }

            } else if ("2".equals(xingbie)) {
                if (realValue < 18) {
                    setLowLine(base_line3, tv_line3);
                    tv_tizhilv.setText("偏低");
                } else if (realValue > 28) {
                    setHighLine(base_line3, tv_line3);
                    tv_tizhilv.setText("偏高");
                } else {
                    setNormalLine(base_line3, tv_line3);
                    tv_tizhilv.setText("正常");
                }
            }
        } else if ("骨质".equals(type)) {
            tv_line4.setText(value);
            double min = 0.045 * dw;
            double max = 0.055 * dw;
            if (realValue < min) {
                setLowLine(base_line4, tv_line4);
                tv_guzhi.setText("偏低");
            } else if (realValue > max) {
                setHighLine(base_line4, tv_line4);
                tv_guzhi.setText("偏高");
            } else {
                setNormalLine(base_line4, tv_line4);
                tv_guzhi.setText("正常");
            }

        } else if ("总水分".equals(type)) {
            tv_line5.setText(value);
            double min = 0.54 * dw;
            double max = 0.66 * dw;
            if (realValue < min) {
                setLowLine(base_line5, tv_line5);
                tv_zongshuifen.setText("偏低");
            } else if (realValue > max) {
                setHighLine(base_line5, tv_line5);
                tv_zongshuifen.setText("偏高");
            } else {
                setNormalLine(base_line5, tv_line5);
                tv_zongshuifen.setText("正常");
            }

        } else if ("骨骼肌".equals(type)) {
            tv_line6.setText(value);
            if ("1".equals(xingbie)) {
                double min = (sm1 - 5) * 0.75;
                double max = (sm1 + 5) * 0.75;
                if (realValue < min) {
                    setLowLine(base_line6, tv_line6);
                    tv_gugeji.setText("偏低");
                } else if (realValue > max) {
                    setHighLine(base_line6, tv_line6);
                    tv_gugeji.setText("偏高");
                } else {
                    setNormalLine(base_line6, tv_line6);
                    tv_gugeji.setText("正常");
                }
            } else if ("2".equals(xingbie)) {
                double min = (sm2 - 3) * 0.75;
                double max = (sm2 + 3) * 0.75;
                if (realValue < min) {
                    setLowLine(base_line6, tv_line6);
                    tv_gugeji.setText("偏低");
                } else if (realValue > max) {
                    setHighLine(base_line6, tv_line6);
                    tv_gugeji.setText("偏高");
                } else {
                    setNormalLine(base_line6, tv_line6);
                    tv_gugeji.setText("正常");
                }
            }

        } else if ("体质指数".equals(type)) {
            tv_line7.setText(value);
            if (realValue < 18.5) {
                setLowLine(base_line7, tv_line7);
                tv_tizhishu.setText("体重较轻");
            } else if (realValue > 23.9) {
                setHighLine(base_line7, tv_line7);
                tv_tizhishu.setText("超重");
            } else {
                setNormalLine(base_line7, tv_line7);
                tv_tizhishu.setText("正常");
            }

        } else if ("腰臀比".equals(type)) {
            tv_line8.setText(value);
            if ("1".equals(xingbie)) {
                if (realValue < 0.85) {
                    setLowLine(base_line8, tv_line8);
                    tv_yaotunbi.setText("梨型");
                } else if (realValue > 0.95) {
                    setHighLine(base_line8, tv_line8);
                    tv_yaotunbi.setText("苹果型");
                } else {
                    setNormalLine(base_line8, tv_line8);
                    tv_yaotunbi.setText("正常");
                }

            } else if ("2".equals(xingbie)) {
                if (realValue < 0.7) {
                    setLowLine(base_line8, tv_line8);
                    tv_yaotunbi.setText("梨型");
                } else if (realValue > 0.8) {
                    setHighLine(base_line8, tv_line8);
                    tv_yaotunbi.setText("苹果型");
                } else {
                    setNormalLine(base_line8, tv_line8);
                    tv_yaotunbi.setText("正常");
                }
            }

        }


    }

    private void setLowLine(ProgressBar pb, TextView tv) {
        ClipDrawable drawableLow = new ClipDrawable(new ColorDrawable(0xff3fc1f7), Gravity.LEFT, ClipDrawable.HORIZONTAL);
        pb.setProgressDrawable(drawableLow);
        drawableLow.setLevel(25 * 100);
        pb.setProgressDrawable(drawableLow);
        pb.setProgress(25);
        tv.setPadding(low, 0, 0, 0);
        tv.setTextColor(0xff3fc1f7);
    }

    private void setNormalLine(ProgressBar pb, TextView tv) {
        ClipDrawable drawableNormal = new ClipDrawable(new ColorDrawable(0xffff6600), Gravity.LEFT, ClipDrawable.HORIZONTAL);
        pb.setProgressDrawable(drawableNormal);
        drawableNormal.setLevel(50 * 100);
        pb.setProgressDrawable(drawableNormal);
        pb.setProgress(50);
        tv.setPadding(normal, 0, 0, 0);
        tv.setTextColor(0xffff6600);
    }

    private void setHighLine(ProgressBar pb, TextView tv) {
        ClipDrawable drawableHigh = new ClipDrawable(new ColorDrawable(0xff007ef1), Gravity.LEFT, ClipDrawable.HORIZONTAL);
        pb.setProgressDrawable(drawableHigh);
        drawableHigh.setLevel(75 * 100);
        pb.setProgressDrawable(drawableHigh);
        pb.setProgress(75);
        tv.setPadding(high, 0, 0, 0);
        tv.setTextColor(0xff007ef1);
    }

    private void initView() {
        //no_data = findViewById(R.id.no_data);
        rl_error = findViewById(R.id.rl_error);
        iv_error = rl_error.findViewById(R.id.iv_error);
        Glide.with(this).load(R.drawable.img_error13).into(iv_error);
        tv_error = rl_error.findViewById(R.id.tv_error);
        tv_error.setText("请联系您的会籍或教练为您体测");
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
                startActivityForResult(new Intent(FitnessTestActivity.this, FitnessHistoryActivity.class).putExtra("member_no", member_no), 101);
                finish();
            }
        });


        rl_age = findViewById(R.id.rl_age);
        rl_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FitnessTestActivity.this, BodyAgeActivity.class);
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
        test_score = findViewById(R.id.test_score);
        test_classify = findViewById(R.id.test_classify);
        test_time = findViewById(R.id.test_time);
        tv_line1 = findViewById(R.id.tv_line1);
        tv_line2 = findViewById(R.id.tv_line2);
        tv_line3 = findViewById(R.id.tv_line3);
        tv_line4 = findViewById(R.id.tv_line4);
        tv_line5 = findViewById(R.id.tv_line5);
        tv_line6 = findViewById(R.id.tv_line6);
        tv_line7 = findViewById(R.id.tv_line7);
        tv_line8 = findViewById(R.id.tv_line8);
        base_line1 = findViewById(R.id.base_line1);
        base_line2 = findViewById(R.id.base_line2);
        base_line3 = findViewById(R.id.base_line3);
        base_line4 = findViewById(R.id.base_line4);
        base_line5 = findViewById(R.id.base_line5);
        base_line6 = findViewById(R.id.base_line6);
        base_line7 = findViewById(R.id.base_line7);
        base_line8 = findViewById(R.id.base_line8);
        tv_tizhong = findViewById(R.id.tv_tizhong);
        tv_jirou = findViewById(R.id.tv_jirou);
        tv_tizhilv = findViewById(R.id.tv_tizhilv);
        tv_guzhi = findViewById(R.id.tv_guzhi);
        tv_zongshuifen = findViewById(R.id.tv_zongshuifen);
        tv_gugeji = findViewById(R.id.tv_gugeji);
        tv_tizhishu = findViewById(R.id.tv_tizhishu);
        tv_yaotunbi = findViewById(R.id.tv_yaotunbi);

    }


    private void initPie(FitnessTestBean.Data data) {
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
        if (data.getWater() != null && data.getFat() != null && data.getBone() != null && data.getProtein() != null) {
            Double[] lv = {Double.valueOf(data.getWater()), Double.valueOf(data.getFat()), Double.valueOf(data.getBone()), Double.valueOf(data.getProtein())};
            Integer[] color = {0xFFFF6600, 0xFFFFBE0E, 0xFFFD8403, 0xFFFF004E};
            String[] str = {"水分", "脂肪", "骨质", "蛋白质"};
            for (int i = 0; i <= 3; i++) {
                SliceValue sliceValue = new SliceValue(lv[i].floatValue(), color[i]);//这里的颜色是我写了一个工具类 是随机选择颜色的
                sliceValue.setLabel(str[i] + " " + lv[i] + "%");
                values.add(sliceValue);
            }

            pieChardata.setValues(values);//填充数据
            pieChardata.setCenterCircleColor(0x00FFFFFF);//设置环形中间的颜色
            pieChardata.setCenterCircleScale(0.75f);//设置环形的大小级别
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

    //判断体形
    //bmi体质指数，pbf体脂百分比,gender==1男，2女
    private String tiXing(float bmi, float pbf, String gender) {
        String tixing = "体型未知";
        if ("1".equals(gender)) {
            if (bmi < 18.5) {
                if (pbf > 20) {
                    tixing = "隐性肥胖型";
                } else if (pbf >= 10 && pbf <= 20) {
                    tixing = "肌肉不足型";
                } else {
                    tixing = "消瘦型";
                }
            } else if (bmi >= 18.5 && bmi <= 23.9) {
                if (pbf > 20) {
                    tixing = "脂肪过多型";
                } else if (pbf >= 10 && pbf <= 20) {
                    tixing = "健康匀称型";
                } else {
                    tixing = "低脂肪型";
                }
            } else {
                if (pbf > 20) {
                    tixing = "肥胖型";
                } else if (pbf >= 10 && pbf <= 20) {
                    tixing = "超重肌肉型";
                } else {
                    tixing = "运动员型";
                }
            }
        } else if ("2".equals(gender)) {
            if (bmi < 18.5) {
                if (pbf > 28) {
                    tixing = "隐性肥胖型";
                } else if (pbf >= 18 && pbf <= 28) {
                    tixing = "肌肉不足型";
                } else {
                    tixing = "消瘦型";
                }
            } else if (bmi >= 18.5 && bmi <= 23.9) {
                if (pbf > 28) {
                    tixing = "脂肪过多型";
                } else if (pbf >= 18 && pbf <= 28) {
                    tixing = "健康匀称型";
                } else {
                    tixing = "低脂肪型";
                }
            } else {
                if (pbf > 28) {
                    tixing = "肥胖型";
                } else if (pbf >= 18 && pbf <= 28) {
                    tixing = "超重肌肉型";
                } else {
                    tixing = "运动员型";
                }
            }
        }
        return tixing;
    }

}
