package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.RequestMyCardListBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cn.danceland.myapplication.R.id.tv_cardtype;


/**
 * Created by shy on 2017/12/19 09:34
 * Email:644563767@qq.com
 */


public class MyCardActivity extends Activity implements View.OnClickListener {

    private ListView mListView;
    private List<RequestMyCardListBean.Data> mCardList = new ArrayList<>();
    private MyListViewAdapter myListViewAdapter;
    Gson gson = new Gson();
    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_card);
        initView();
        initData();
    }

    private void initView() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        mListView = findViewById(R.id.listview);
        myListViewAdapter = new MyListViewAdapter();
        mListView.setAdapter(myListViewAdapter);

    }

    private void initData() {

        findAllCard();
    }

    /**
     * 查找全部会员卡
     */
    private void findAllCard() {

        StringRequest request = new StringRequest(Request.Method.GET, Constants.FIND_ALL_MY_CARD_LIST, new Response.Listener<String>() {


            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                RequestMyCardListBean myCardListBean=new RequestMyCardListBean();
                myCardListBean=gson.fromJson(s,RequestMyCardListBean.class);
                mCardList=myCardListBean.getData();
                myListViewAdapter.notifyDataSetChanged();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, null));
                return map;
            }
        };
        MyApplication.getHttpQueues().add(request);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }


    class MyListViewAdapter extends BaseAdapter

    {

        @Override
        public int getCount() {
            return mCardList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            ViewHolder viewHolder = null;
            if (view == null) {
                viewHolder = new ViewHolder();
                view = View.inflate(MyCardActivity.this, R.layout.listview_item_my_club_card, null);
                viewHolder.tv_name = view.findViewById(R.id.tv_cardname);
                viewHolder.tv_number = view.findViewById(R.id.tv_number);
                viewHolder.tv_time = view.findViewById(R.id.tv_time);

                viewHolder.tv_cardtype = view.findViewById(tv_cardtype);

                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }


            if (mCardList.get(i).getCharge_mode() == 1) {//计时卡
                viewHolder.tv_cardtype.setText("卡类型：计时卡");
            }
            if (mCardList.get(i).getCharge_mode() == 2) {//计次卡
                viewHolder.tv_cardtype.setText("卡类型：计次卡");
                viewHolder.   tv_cardtype.setText("卡类型：计次卡（剩余次数："+mCardList.get(i).getTotal_count() + "次）");
            }
            if (mCardList.get(i).getCharge_mode() == 3) {//储值卡
                viewHolder.tv_cardtype.setText("卡类型：储值卡");
            }


            viewHolder.tv_name.setText(mCardList.get(i).getType_name());

//            if (!TextUtils.isEmpty(mCardList.get(i).getTotal_count())){
//                viewHolder.tv_number.setText("次数："+mCardList.get(i).getTotal_count() + "次");
//                viewHolder.tv_number.setVisibility(View.VISIBLE);
//            }else {
//                viewHolder.tv_number.setVisibility(View.GONE);
//            }

            viewHolder.tv_time.setText(mCardList.get(i).getEnd_date()+"到期");


            return view;
        }

        class ViewHolder {
            TextView tv_name;
            TextView tv_number;
            TextView tv_time;
            TextView tv_cardtype;
        }

    }


}
