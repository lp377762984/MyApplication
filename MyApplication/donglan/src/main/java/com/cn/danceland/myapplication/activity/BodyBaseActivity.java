package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.DLResult;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.bca.bcaoption.BcaOption;
import com.cn.danceland.myapplication.bean.bca.bcaquestion.BcaQuestion;
import com.cn.danceland.myapplication.bean.bca.bcaquestion.BcaQuestionCond;
import com.cn.danceland.myapplication.bean.bca.bcaquestion.BcaQuestionRequest;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.CustomGridView;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by feng on 2018/3/29.
 */

public class BodyBaseActivity extends Activity {

    ListView lv_bodybase;
    View hearerView;
    private BcaQuestionRequest request;
    private Gson gson;
    List<BcaQuestion> list;
    BodyBaseAdapter bodyBaseAdapter;
    Data myInfo;
    View footView;
    CustomGridView gv_bodybase;
    BodyBaseGridAdapter bodyBaseGridAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bodybase);
        initHost();
        initView();


    }

    private void initHost() {
        request = new BcaQuestionRequest();
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        list = new ArrayList<>();
        myInfo = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
    }

    private void initView() {

        lv_bodybase = findViewById(R.id.lv_bodybase);

        hearerView = View.inflate(BodyBaseActivity.this, R.layout.bodybase_header, null);

        footView = View.inflate(BodyBaseActivity.this, R.layout.commit_button, null);

        lv_bodybase.addHeaderView(hearerView);
        lv_bodybase.addFooterView(footView);

        bodyBaseAdapter = new BodyBaseAdapter();
        lv_bodybase.setAdapter(bodyBaseAdapter);
        queryList();


    }


    /**
     * @方法说明:按条件查询问题题干列表
     **/
    public void queryList() {
        BcaQuestionCond cond = new BcaQuestionCond();
        cond.setType(Byte.valueOf("1"));
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



    private class BodyBaseAdapter extends BaseAdapter{

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
            View view = View.inflate(BodyBaseActivity.this, R.layout.bodybase_item, null);


            gv_bodybase = view.findViewById(R.id.gv_bodybase);

            TextView tv_tigan = view.findViewById(R.id.tv_tigan);
            tv_tigan.setText(list.get(position).getOrder_no()+". "+list.get(position).getCentent());

            final ArrayList<Integer> select = new ArrayList<>();
            bodyBaseGridAdapter = new BodyBaseGridAdapter(list.get(position).getOptions(),select);
            gv_bodybase.setAdapter(bodyBaseGridAdapter);
            //gv_bodybase.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
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

        private int click = -1;
        private boolean checked;
        ArrayList<Integer> select;


        List<BcaOption> options;

        BodyBaseGridAdapter(List<BcaOption> options,ArrayList<Integer> select){
            this.options = options;
            this.select = select;
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

            View view = View.inflate(BodyBaseActivity.this, R.layout.bodybase_grid_item, null);
            RadioButton rb_grid = view.findViewById(R.id.rb_grid);
            rb_grid.setText(options.get(position).getTitle());
            if(select!=null&&select.size()>0){
                //for(int i = 0;i<select.size();i++){
                    if(select.get(0)==position){
                        rb_grid.setChecked(true);
                    }else{
                        rb_grid.setChecked(false);
                    }

                //}
            }


//            if(position==click){
//                rb_grid.setChecked(true);
//            }else{
//                rb_grid.setChecked(false);
//            }

            return view;
        }

    }


}
