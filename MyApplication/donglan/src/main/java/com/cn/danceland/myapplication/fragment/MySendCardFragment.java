package com.cn.danceland.myapplication.fragment;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.RequestSendCardBean;
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
 * Created by shy on 2018/3/23 10:22
 * Email:644563767@qq.com
 */


public class MySendCardFragment extends BaseFragment {
    private ListView mListView;
    private List<RequestSendCardBean.Data> mCardList = new ArrayList<>();
    private MyListViewAdapter myListViewAdapter;
    Gson gson = new Gson();
    private TextView tv_error;
    private ImageView iv_error;

    @Override
    public View initViews() {
        View v = View.inflate(mActivity, R.layout.fragment_my_card, null);
        mListView = v.findViewById(R.id.listview);
        View listEmptyView = v.findViewById(R.id.rl_no_info);
        tv_error = listEmptyView.findViewById(R.id.tv_error);
        iv_error = listEmptyView.findViewById(R.id.iv_error);
        iv_error.setImageResource(R.drawable.img_error3);
        tv_error.setText("您还没有给他人送出会员卡");
        mListView.setEmptyView(listEmptyView);
        myListViewAdapter = new MyListViewAdapter();
        mListView.setAdapter(myListViewAdapter);
        return v;

    }

    @Override
    public void onClick(View view) {

    }


    @Override
    public void initDta() {
        findAllCard();
    }

    /**
     * 查找全部送出会员卡
     */
    private void findAllCard() {

        StringRequest request = new StringRequest(Request.Method.GET, Constants.FIND_ALL_OTHER_CARD_LIST, new Response.Listener<String>() {


            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                RequestSendCardBean myCardListBean = new RequestSendCardBean();
                myCardListBean = gson.fromJson(s, RequestSendCardBean.class);
                mCardList = myCardListBean.getData();
                myListViewAdapter.notifyDataSetChanged();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                iv_error.setImageResource(R.drawable.img_error7);
                tv_error.setText("网络异常");
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

                view = View.inflate(mActivity, R.layout.listview_item_my_send_club_card, null);
                viewHolder.tv_name = view.findViewById(R.id.tv_cardname);
                viewHolder.tv_number = view.findViewById(R.id.tv_number);
                viewHolder.tv_time = view.findViewById(R.id.tv_time);

                viewHolder.tv_cardtype = view.findViewById(tv_cardtype);
                viewHolder.tv_order_name = view.findViewById(R.id.tv_order_name);
                viewHolder.tv_phone = view.findViewById(R.id.tv_phone);
                viewHolder.iv_card = view.findViewById(R.id.iv_card);

                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }


            if (mCardList.get(i).getCharge_mode() == 1) {//计时卡
                viewHolder.tv_cardtype.setText("卡类型：计时卡");
            }
            if (mCardList.get(i).getCharge_mode() == 2) {//计次卡
                viewHolder.tv_cardtype.setText("卡类型：计次卡");
                viewHolder.tv_cardtype.setText("卡类型：计次卡（剩余次数：" + mCardList.get(i).getTotal_count() + "次）");
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
            if (TextUtils.isEmpty(mCardList.get(i).getEnd_date())) {
                viewHolder.tv_time.setText("未开卡");
            } else {
                StringBuilder sb = new StringBuilder(mCardList.get(i).getEnd_date());

                String[] b = sb.toString().split(" ");

                viewHolder.tv_time.setText(b[0] + "到期");
            }
            viewHolder.tv_phone.setText("好友电话：" + mCardList.get(i).getPhone_no());
            viewHolder.tv_order_name.setText("好友姓名：" + mCardList.get(i).getMember_name());


            //设置图片圆角角度
            RoundedCorners roundedCorners = new RoundedCorners(6);
//通过RequestOptions扩展功能
            RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(300, 300).placeholder(R.drawable.sijiao_card);


            Glide.with(mActivity).load(mCardList.get(i).getImg_url()).apply(options).into(viewHolder.iv_card);
            return view;
        }

        class ViewHolder {
            TextView tv_name;
            TextView tv_number;
            TextView tv_time;
            TextView tv_cardtype;
            TextView tv_order_name;
            TextView tv_phone;
            ImageView iv_card;
        }

    }
}
