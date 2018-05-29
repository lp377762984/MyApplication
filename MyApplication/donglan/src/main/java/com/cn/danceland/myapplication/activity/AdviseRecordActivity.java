package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.DLResult;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.feedback.FeedBack;
import com.cn.danceland.myapplication.bean.feedback.FeedBackCond;
import com.cn.danceland.myapplication.bean.feedback.FeedBackRequest;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.TimeUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by feng on 2018/3/26.
 */

public class AdviseRecordActivity extends Activity {

    ListView advise_record_lv;
    private FeedBackRequest request;
    private Gson gson;
    Data data;
    List<FeedBack> list;
    ImageView fankui_back;
    RelativeLayout rl_error;
    ImageView iv_error;
    TextView tv_error;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advise_record);


        data = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
        request = new FeedBackRequest();
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        initView();
        queryList();

    }

    private void initView() {

        advise_record_lv = findViewById(R.id.advise_record_lv);
        advise_record_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(AdviseRecordActivity.this,AdviseDetailActivity.class).putExtra("id",list.get(position).getId()));
            }
        });
        rl_error = findViewById(R.id.rl_error);
        iv_error = rl_error.findViewById(R.id.iv_error);
        Glide.with(this).load(R.drawable.img_error4).into(iv_error);
        tv_error = rl_error.findViewById(R.id.tv_error);
        tv_error.setText("您还没有反馈任何信息");

        advise_record_lv.setEmptyView(rl_error);
        fankui_back = findViewById(R.id.fankui_back);
        fankui_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /**
     * @方法说明:按条件查询意见反馈列表
     **/
    public void queryList() {
        FeedBackCond cond = new FeedBackCond();
        cond.setPage(0);
        cond.setSize(20);
        request.queryList(cond, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject json) {
                DLResult<List<FeedBack>> result = gson.fromJson(json.toString(), new TypeToken<DLResult<List<FeedBack>>>() {
                }.getType());
                if (result.isSuccess()) {
                    list = result.getData();
                    if(list!=null){
                        advise_record_lv.setAdapter(new RecordAdapter(list));
                    }
                } else {
                    ToastUtils.showToastShort("查询分页列表失败,请检查手机网络！");
                }
            }
        },rl_error,iv_error,tv_error);
    }


    private class RecordAdapter extends BaseAdapter{

        List<FeedBack> list;

        RecordAdapter(List<FeedBack> list){

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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView==null){
                convertView = View.inflate(AdviseRecordActivity.this, R.layout.advise_record_item, null);
                viewHolder = new ViewHolder();
                viewHolder.tv_type = convertView.findViewById(R.id.tv_type);
                viewHolder.fankui_time = convertView.findViewById(R.id.fankui_time);
                viewHolder.huifu_time = convertView.findViewById(R.id.huifu_time);
                viewHolder.tv_status = convertView.findViewById(R.id.tv_status);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)convertView.getTag();
            }

                if(list.get(position).getType()==1){
                    viewHolder.tv_type.setText("批评");
                }else if(list.get(position).getType()==2){
                    viewHolder.tv_type.setText("表扬");
                }else if(list.get(position).getType()==3){
                    viewHolder.tv_type.setText("建议");
                }else if(list.get(position).getType()==4){
                    viewHolder.tv_type.setText("投诉");
                }else{
                    viewHolder.tv_type.setText("未知类型");
                }

                if(list.get(position).getStatus()==1){
                    viewHolder.tv_status.setText("已回复");
                }else if(list.get(position).getStatus()==2){
                    viewHolder.tv_status.setText("未回复");
                }else{
                    viewHolder.tv_status.setText("状态未知");
                }

            if(list.get(position).getOpinion_date()!=null){
                viewHolder.fankui_time.setText("反馈时间："+ TimeUtils.dateToString(list.get(position).getOpinion_date()));
            }
            if(list.get(position).getReply_date()!=null){
                viewHolder.huifu_time.setText("回复时间："+TimeUtils.dateToString(list.get(position).getReply_date()));
            }

            return convertView;
        }
    }

    class ViewHolder{
        TextView tv_type,fankui_time,huifu_time,tv_status;

    }
}
