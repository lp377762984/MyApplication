package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.DLResult;
import com.cn.danceland.myapplication.bean.bca.bcaoption.BcaOption;
import com.cn.danceland.myapplication.bean.bca.bcaquestion.BcaQuestion;
import com.cn.danceland.myapplication.bean.bca.bcaquestion.BcaQuestionCond;
import com.cn.danceland.myapplication.bean.bca.bcaquestion.BcaQuestionRequest;
import com.cn.danceland.myapplication.utils.CustomGridView;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.cn.danceland.myapplication.view.DongLanTitleView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by feng on 2018/4/8.
 */

public class BodyDeatilActivity extends Activity {

    DongLanTitleView rl_bodybase_title;
    ListView lv_bodybase;
    View footView;
    private BcaQuestionRequest request;
    private Gson gson;
    List<BcaQuestion> list;
    BodyBaseAdapter bodyBaseAdapter;
    CustomGridView gv_bodybase;
    BodyBaseGridAdapter bodyBaseGridAdapter;
    Button body_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bodybase);
        initHost();
        initView();
        queryList();
    }

    private void initHost() {

        request = new BcaQuestionRequest();
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        list = new ArrayList<>();

    }

    private void initView() {

        rl_bodybase_title = findViewById(R.id.rl_bodybase_title);
        rl_bodybase_title.setTitle("身体详细情况");
        lv_bodybase = findViewById(R.id.lv_bodybase);
        footView = View.inflate(BodyDeatilActivity.this, R.layout.commit_button, null);
        lv_bodybase.addFooterView(footView);
        bodyBaseAdapter = new BodyBaseAdapter();
        lv_bodybase.setAdapter(bodyBaseAdapter);

        body_button = footView.findViewById(R.id.body_button);
        body_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BodyDeatilActivity.this,PhysicalTestActivity.class));
            }
        });

    }


    /**
     * @方法说明:按条件查询问题题干列表
     **/
    public void queryList() {
        BcaQuestionCond cond = new BcaQuestionCond();
        cond.setType(Byte.valueOf("2"));
        request.queryList(cond, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject json) {
                DLResult<List<BcaQuestion>> result = gson.fromJson(json.toString(), new TypeToken<DLResult<List<BcaQuestion>>>() {
                }.getType());
                list.clear();
                if (result.isSuccess()) {
                    list = result.getData();
                    bodyBaseAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showToastShort("查询分页列表失败,请检查手机网络！");
                }
            }
        });
    }


    private class BodyBaseAdapter extends BaseAdapter {

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
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(BodyDeatilActivity.this, R.layout.bodybase_item, null);


            gv_bodybase = view.findViewById(R.id.gv_bodybase);

            TextView tv_tigan = view.findViewById(R.id.tv_tigan);
            tv_tigan.setText(list.get(position).getOrder_no()+". "+list.get(position).getCentent());

            final ArrayList<Integer> select = new ArrayList<>();
            bodyBaseGridAdapter = new BodyBaseGridAdapter(list.get(position).getOptions(),position);
            gv_bodybase.setAdapter(bodyBaseGridAdapter);

            gv_bodybase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    select.clear();
                    select.add(position);
                    bodyBaseGridAdapter.notifyDataSetChanged();
                }
            });
            return view;
        }
    }


    private class BodyBaseGridAdapter extends BaseAdapter{


        Integer itemPositon;

        List<BcaOption> options;

        BodyBaseGridAdapter(List<BcaOption> options,Integer itemPositon){
            this.options = options;
            this.itemPositon = itemPositon;
        }

        @Override
        public int getCount() {
            return options.size();
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

            View view = View.inflate(BodyDeatilActivity.this, R.layout.bodybase_grid_item, null);
            CheckBox rb_grid = view.findViewById(R.id.rb_grid);
            rb_grid.setText(options.get(position).getTitle());


            return view;
        }

    }
}
