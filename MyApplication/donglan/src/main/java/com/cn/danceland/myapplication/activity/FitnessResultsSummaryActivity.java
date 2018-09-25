package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.adapter.FitnessResultsSummaryAdapter;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.FitnessResultsSummaryBean;
import com.cn.danceland.myapplication.bean.RequsetFindUserBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.TimeUtils;
import com.cn.danceland.myapplication.view.NoScrollListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 体测分析-结果汇总
 * Created by yxx on 2018-09-20.
 */

public class FitnessResultsSummaryActivity extends Activity {
    private Context context;
    private CircleImageView iv_avatar;//头像
    private TextView name_tv;//姓名
    private TextView sex_tv;//性别
    private TextView age_tv;//年龄
    private TextView tel_tv;//电话
    private TextView date_tv;//体测日期
    private TextView operator_tv;//操作人员
    private TextView stores_tv;//体测门店
    private TextView content_tv;//综合评价
    private ImageView frontal_iv;//正面照
    private ImageView side_iv;//侧面照
    private ImageView behind_iv;//背后照
    private NoScrollListView listview;//listview
    private Button ok_btn;//完成

    private FitnessResultsSummaryAdapter adapter;//adapter

    private RequsetFindUserBean.Data requsetInfo;//前面搜索到的对象
    private List<FitnessResultsSummaryBean.QuestionTypes> questionTypesList;

    private Data infoData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness_results_summary);
        context = this;
        requsetInfo = (RequsetFindUserBean.Data) getIntent().getSerializableExtra("requsetInfo");//前面搜索到的对象

        infoData = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);//动岚个人资料

        initView();
    }

    private void initView() {
        iv_avatar = findViewById(R.id.iv_avatar);
        name_tv = findViewById(R.id.name_tv);
        sex_tv = findViewById(R.id.sex_tv);
        age_tv = findViewById(R.id.age_tv);
        tel_tv = findViewById(R.id.tel_tv);
        date_tv = findViewById(R.id.date_tv);
        operator_tv = findViewById(R.id.operator_tv);
        stores_tv = findViewById(R.id.stores_tv);
        listview = findViewById(R.id.listview);
        content_tv = findViewById(R.id.content_tv);
        frontal_iv = findViewById(R.id.frontal_iv);
        side_iv = findViewById(R.id.side_iv);
        behind_iv = findViewById(R.id.behind_iv);
        ok_btn = findViewById(R.id.ok_btn);

        questionTypesList = new ArrayList<>();

        adapter = new FitnessResultsSummaryAdapter(context, questionTypesList);
        listview.setAdapter(adapter);
        LogUtil.i("MY_TOKEN--" + SPUtils.getString(Constants.MY_TOKEN, ""));
        LogUtil.i("memberId--" + requsetInfo.getId());
        queryData();

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * 查询数据
     */
    private void queryData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.QUERY_BCAQUESTION_FIND_RECENTLY + "?member_id=" + requsetInfo.getId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i("Response--" + s);
                FitnessResultsSummaryBean responseBean = new Gson().fromJson(s, FitnessResultsSummaryBean.class);
                List<FitnessResultsSummaryBean.QuestionTypes> data = responseBean.getData().getQuestionTypes();
                String frontal_path = responseBean.getData().getFrontal_path();// 正面照
                String side_path = responseBean.getData().getSide_path();// 侧面照
                String behind_path = responseBean.getData().getBehind_path();// 背后照
                String content = responseBean.getData().getContent();//综合评价
                if (data != null && data.size() != 0) {
                    LogUtil.i("data.size()" + data.size());
                    questionTypesList.clear();
                    name_tv.setText(requsetInfo.getCname() + "");//姓名
                    sex_tv.setText(requsetInfo.getGender() + "");//性别
                    if (requsetInfo.getBirthday() != null) {
                        int age = TimeUtils.getAgeFromBirthTime(new Date(TimeUtils.date2TimeStamp(requsetInfo.getBirthday(), "yyyy-MM-dd")));
                        age_tv.setText(age + "岁");//年龄
                    }
                    tel_tv.setText(requsetInfo.getPhone_no() + "");//电话
                    operator_tv.setText(infoData.getPerson().getCname() + "");//操作人员
                    if (responseBean.getData().getTest_time() != null && responseBean.getData().getTest_time().length() > 0) {
                        date_tv.setText(TimeUtils.millToDate(Long.valueOf(responseBean.getData().getTest_time())));//体测日期
                    }
                    stores_tv.setText(infoData.getMember().getBranch_name() + "");//体测门店

                    if (content != null && content.length() > 0) {//综合评价
                        content_tv.setText(content);
                        content_tv.setVisibility(View.VISIBLE);
                    } else {
                        content_tv.setVisibility(View.GONE);
                    }
                    if (context != null) {//设置图片
                        RequestOptions options = new RequestOptions().placeholder(R.drawable.img_my_avatar);
                        if (frontal_path != null && frontal_path.length() > 0) {
                            Glide.with(context).load(frontal_path).apply(options).into(frontal_iv);
                        }
                        if (side_path != null && side_path.length() > 0) {
                            Glide.with(context).load(side_path).apply(options).into(side_iv);
                        }
                        if (behind_path != null && behind_path.length() > 0) {
                            Glide.with(context).load(behind_path).apply(options).into(behind_iv);
                        }
                    }

                    for (int i = 0; i < data.size(); i++) {
                        //5.体型形体分析   6.综合评价 因接口数据结构，单独处理，后面追加布局
                        if ((data.get(i).getTypeValue() != null && !data.get(i).getTypeValue().equals("5"))
                                && (data.get(i).getTypeValue() != null && !data.get(i).getTypeValue().equals("6"))) {
                            questionTypesList.add(data.get(i));
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError != null) {
                    LogUtil.i(volleyError.toString());
                } else {
                    LogUtil.i("NULL");
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, null));
                return map;
            }
        };
        MyApplication.getHttpQueues().add(stringRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
