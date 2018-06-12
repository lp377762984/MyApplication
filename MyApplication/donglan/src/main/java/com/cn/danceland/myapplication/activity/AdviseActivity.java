package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.DLResult;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.feedback.FeedBack;
import com.cn.danceland.myapplication.bean.feedback.FeedBackRequest;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.cn.danceland.myapplication.view.ContainsEmojiEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.Date;

/**
 * Created by feng on 2018/3/13.
 */

public class AdviseActivity extends Activity {

    RadioGroup advise_rg;
    private FeedBackRequest request;
    private Gson gson;
    Integer type = 2;
    ContainsEmojiEditText advise_ed;
    Data data;
    RelativeLayout rl_commit;
    ImageView feed_back,img_biaoqing;
    TextView feed_record;
    RadioButton rb_0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.advise);

        data = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
        request = new FeedBackRequest();
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        initView();

    }

    private void initView() {

        img_biaoqing = findViewById(R.id.img_biaoqing);
        advise_rg = findViewById(R.id.advise_rg);
        advise_rg.setOnCheckedChangeListener(onCheckedChangeListener);

        rb_0 = findViewById(R.id.rb_0);
        rb_0.setChecked(true);

        advise_ed = findViewById(R.id.advise_ed);
        rl_commit = findViewById(R.id.rl_commit);
        rl_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        feed_back = findViewById(R.id.feed_back);
        feed_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        feed_record = findViewById(R.id.feed_record);
        feed_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdviseActivity.this,AdviseRecordActivity.class));
            }
        });

    }


    /**
     * @方法说明:新增意见反馈
     **/
    public void save() {
        String content = advise_ed.getText().toString();
        FeedBack feedBack = new FeedBack();
        feedBack.setType(type);
        feedBack.setContent(content);
        feedBack.setBranch_id((long)data.getMember().getBranch_id());
        feedBack.setContact_way(data.getMember().getPhone_no());

        request.save(feedBack, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject json) {
                DLResult<Integer> result = gson.fromJson(json.toString(), new TypeToken<DLResult<Integer>>() {
                }.getType());
                if (result.isSuccess()) {
                    ToastUtils.showToastShort("提交成功！");
                    finish();
                } else {
                    ToastUtils.showToastShort("保存数据失败,请检查手机网络！");
                }
            }
        });

    }


    OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            int checkedRadioButtonId = group.getCheckedRadioButtonId();
            switch (checkedRadioButtonId){
                case R.id.rb_0://表扬
                    type = 2;
                    img_biaoqing.setImageResource(R.drawable.biaoyang);
                    break;
                case R.id.rb_1://建议
                    type = 3;
                    img_biaoqing.setImageResource(R.drawable.jianyi);
                    break;
                case R.id.rb_2://批评
                    type = 1;
                    img_biaoqing.setImageResource(R.drawable.piping);
                    break;
                case R.id.rb_3://投诉
                    type = 4;
                    img_biaoqing.setImageResource(R.drawable.tousu);
                    break;

            }

        }
    };
}
