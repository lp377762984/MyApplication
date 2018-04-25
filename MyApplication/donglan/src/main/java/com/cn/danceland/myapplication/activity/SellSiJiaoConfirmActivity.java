package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.DLResult;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.explain.Explain;
import com.cn.danceland.myapplication.bean.explain.ExplainCond;
import com.cn.danceland.myapplication.bean.explain.ExplainRequest;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by feng on 2018/1/15.
 */

public class SellSiJiaoConfirmActivity extends Activity {
    int state=999;

    RelativeLayout rl_buy;
    RadioButton btn_sijiao,btn_sijiaodingjin;
    ImageView sell_img;
    Serializable itemContent;
    Gson gson;
    ExplainRequest request;
    Data info;
    TextView tv_shuoming;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sellsijiaoconfirm);

        request = new ExplainRequest();

        info = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        itemContent = getIntent().getSerializableExtra("itemContent");
        initView();
        queryList();
    }

    /**
     * @方法说明:按条件查询说明须知列表
     **/
    public void queryList() {

        ExplainCond cond = new ExplainCond();
        cond.setBranch_id(Long.valueOf(info.getPerson().getDefault_branch()));
        cond.setType(Byte.valueOf("2"));// 1 买卡须知 2 买私教须知 3 买储值须知 4 买卡说明 5 买私教说明

        request.queryList(cond, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject json) {
                DLResult<List<Explain>> result = gson.fromJson(json.toString(), new TypeToken<DLResult<List<Explain>>>() {
                }.getType());
                if (result.isSuccess()) {
                    List<Explain> list = result.getData();
                    if(list!=null&&list.size()>0){
                        tv_shuoming.setText(list.get(0).getContent());
                    }
                } else {
                    ToastUtils.showToastShort("查询分页列表失败,请检查手机网络！");
                }
            }
        });
    }

    private void initView() {
        state = 0;
        sell_img = findViewById(R.id.sell_img);
        sell_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_shuoming = findViewById(R.id.tv_shuoming);

        rl_buy = findViewById(R.id.rl_buy);
        btn_sijiao = findViewById(R.id.btn_sijiao);
        btn_sijiaodingjin = findViewById(R.id.btn_sijiaodingjin);

        btn_sijiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                btn_sijiao.setChecked(true);
//                btn_sijiaodingjin.setChecked(false);
                state = 0;
            }
        });

        btn_sijiaodingjin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                btn_sijiao.setChecked(false);
//                btn_sijiaodingjin.setChecked(true);
                state = 1;
            }
        });

        rl_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state==0){
                    startActivity(new Intent(SellSiJiaoConfirmActivity.this,SiJiaoOrderActivity.class).putExtra("type","0").putExtra("itemContent",itemContent));
                }else if(state==1){
                    startActivity(new Intent(SellSiJiaoConfirmActivity.this,SiJiaoOrderActivity.class).putExtra("type","1").putExtra("itemContent",itemContent));
                }

            }
        });

    }
}
