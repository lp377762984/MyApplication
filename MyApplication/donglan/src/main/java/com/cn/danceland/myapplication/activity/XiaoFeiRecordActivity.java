package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.DLResult;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.store.storebill.StoreBill;
import com.cn.danceland.myapplication.bean.store.storebill.StoreBillCond;
import com.cn.danceland.myapplication.bean.store.storebill.StoreBillRequest;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by feng on 2018/3/16.
 */

public class XiaoFeiRecordActivity extends Activity {

    ListView lv_xiaofei;
    private StoreBillRequest request;
    private Gson gson;
    Data info;
    private SimpleDateFormat sdf;
    ImageView xiaofei_back;
    float allchongzhi,allxiaofei;
    TextView tv_leijichongzhi,tv_leijixiaofei;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.xiaofeirecord);
        initHost();

        initView();
        queryList();


    }

    private void initHost() {

        request = new StoreBillRequest();

        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        info = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
    }

    private void initView() {
        lv_xiaofei = findViewById(R.id.lv_xiaofei);
        tv_leijichongzhi = findViewById(R.id.tv_leijichongzhi);
        tv_leijixiaofei = findViewById(R.id.tv_leijixiaofei);

        xiaofei_back = findViewById(R.id.xiaofei_back);
        xiaofei_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /**
     * @方法说明:按条件查询储值卡流水帐单列表
     **/
    public void queryList() {
        StoreBillCond cond = new StoreBillCond();
        // TODO 准备查询条件
        request.queryList(cond, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject json) {
                LogUtil.e("zzf",json.toString());
                DLResult<List<StoreBill>> result = gson.fromJson(json.toString(), new TypeToken<DLResult<List<StoreBill>>>() {
                }.getType());
                if (result.isSuccess()) {
                    List<StoreBill> list = result.getData();
                    if(list!=null){

                        for(int i=0;i<list.size();i++){
                            if(list.get(i).getType()==1){
                                allchongzhi = allchongzhi+list.get(i).getPrice();
                            }else if(list.get(i).getType()==3){
                                allxiaofei = allxiaofei+list.get(i).getPrice();
                            }
                        }
                        tv_leijichongzhi.setText(allchongzhi+"元");
                        tv_leijixiaofei.setText(allxiaofei+"元");
                        lv_xiaofei.setAdapter(new MyAdapter(list));
                    }
                    System.out.println(list);
                    // TODO 请求成功后的代码
                } else {
                    ToastUtils.showToastShort("查询分页列表失败,请检查手机网络！");
                }
            }
        });
    }




    private class MyAdapter extends BaseAdapter{
        List<StoreBill> list;
        MyAdapter(List<StoreBill> list){

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
                viewHolder = new ViewHolder();
                convertView = View.inflate(XiaoFeiRecordActivity.this, R.layout.xiaofei_item, null);
                viewHolder.tv_goodtype = convertView.findViewById(R.id.tv_goodtype);
                viewHolder.tv_time = convertView.findViewById(R.id.tv_time);
                viewHolder.tv_xiaofei = convertView.findViewById(R.id.tv_xiaofei);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if(list.get(position).getType()==1){
                viewHolder.tv_goodtype.setText("充值");
                viewHolder.tv_xiaofei.setText("¥ "+list.get(position).getPrice()+"元");
                viewHolder.tv_xiaofei.setTextColor(Color.parseColor("#ff6600"));
            }else if(list.get(position).getType()==3){
                viewHolder.tv_goodtype.setText("购物");
                viewHolder.tv_xiaofei.setText("¥ "+list.get(position).getPrice()+"元");
                viewHolder.tv_xiaofei.setTextColor(Color.parseColor("#808080"));
            }else if(list.get(position).getType()==2){
                viewHolder.tv_goodtype.setText("退款");
                viewHolder.tv_xiaofei.setText("¥ "+list.get(position).getPrice()+"元");
                viewHolder.tv_xiaofei.setTextColor(Color.parseColor("#808080"));
            }

            viewHolder.tv_time.setText(sdf.format(list.get(position).getOperate_time()));

            return convertView;
        }
    }

    class ViewHolder{
        TextView tv_goodtype,tv_time,tv_xiaofei;
    }

}
