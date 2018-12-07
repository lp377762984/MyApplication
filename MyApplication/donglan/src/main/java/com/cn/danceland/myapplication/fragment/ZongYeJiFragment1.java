package com.cn.danceland.myapplication.fragment;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.adapter.recyclerview.CommonAdapter;
import com.cn.danceland.myapplication.adapter.recyclerview.base.ViewHolder;
import com.cn.danceland.myapplication.bean.HuiJiYeJiBean;
import com.cn.danceland.myapplication.evntbus.StringEvent;
import com.cn.danceland.myapplication.fragment.base.BaseRecyclerViewRefreshFragment;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.GlideRoundTransform;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.MyStringRequest;
import com.cn.danceland.myapplication.utils.TimeUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by shy on 2018/12/5 10:37
 * Email:644563767@qq.com
 * <p>
 * 总业绩
 */


public class ZongYeJiFragment1 extends BaseRecyclerViewRefreshFragment {

    private List<HuiJiYeJiBean.Data> dataList = new ArrayList<>();
    private MylistAtapter mylistAtapter;
    private int mCurrentPage = 0;
    private String mCurrentDate =null;

    @Override
    public void onEventMainThread(StringEvent event) {
        switch (event.getEventCode()) {


            case 7100://刷新页面
                if (event.getMsg() != null) {
                    mCurrentDate=event.getMsg();
                    findhjyj(event.getMsg(), event.getMsg());
                }

            default:
                break;
        }
    }



    @Override
    public CommonAdapter setAtapter() {
        mylistAtapter = new MylistAtapter(mActivity, R.layout.listview_item_jinriyeji, dataList);
//        EmptyWrapper mEmptyWrapper = new EmptyWrapper(mylistAtapter);
//        mEmptyWrapper.setEmptyView(R.layout.no_info_layout);
        mylistAtapter.setEmptyView(R.layout.no_info_layout);
        mylistAtapter.addHeaderView(View.inflate(mActivity,R.layout.empty_8dp_layout,null));
        mylistAtapter.addFootView(View.inflate(mActivity,R.layout.empty_8dp_layout,null));
        return mylistAtapter;
    }


    @Override
    public void initDownRefreshData() {
        mCurrentPage = 0;
        if (mCurrentDate==null){
            mCurrentDate= TimeUtils.timeStamp2Date(System.currentTimeMillis()+"","yyyy-MM-dd");
        }
        findhjyj(mCurrentDate, mCurrentDate);
        setOnlyDownReresh();
    }

    @Override
    public void upDownRefreshData() {


    }


    private void findhjyj(final String start, final String end) {
        MyStringRequest request = new MyStringRequest(Request.Method.POST, Constants.QUERY_HUIJI, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                HuiJiYeJiBean huiJiYeJiBean = new Gson().fromJson(s, HuiJiYeJiBean.class);
                dataList = huiJiYeJiBean.getData();
                getListAdapter().setDatas(dataList);
                getListAdapter().notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.e(volleyError.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("start", start);
                map.put("end", end);
                return map;
            }
        };
        MyApplication.getHttpQueues().add(request);
    }

    class MylistAtapter extends CommonAdapter<HuiJiYeJiBean.Data> {

        private List<HuiJiYeJiBean.Data> datas;


        public MylistAtapter(Context context, int layoutId, List<HuiJiYeJiBean.Data> datas) {
            super(context, layoutId, datas);
            this.datas = datas;
        }


        @Override
        public void convert(ViewHolder viewHolder, HuiJiYeJiBean.Data data, int position) {
            viewHolder.setText(R.id.tv_name, data.getEmp_name());
            viewHolder.setText(R.id.tv_sum, "总业绩：" + data.getTotal() + "元");
            viewHolder.setText(R.id.tv_yewu1, "办卡：" + data.getNewcard());
            viewHolder.setText(R.id.tv_yewu2, "租柜：" + data.getLeaselocker());
            RequestOptions options = new RequestOptions()
                    .transform(new GlideRoundTransform(mActivity, 10));

            Glide.with(mActivity).load(data.getAvatar_url()).apply(options).into((ImageView) viewHolder.getView(R.id.iv_avatar));
        }


    }
}
