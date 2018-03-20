package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.RequestSellCardsInfoBean;
import com.cn.danceland.myapplication.bean.RequestSellCardsTypeBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DensityUtils;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.PriceUtils;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.cn.danceland.myapplication.view.XCRoundRectImageView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import razerdp.basepopup.BasePopupWindow;

import static com.cn.danceland.myapplication.R.id.tv_cardtype;

/**
 * Created by shy on 2017/11/2 16:37
 * Email:644563767@qq.com
 */


public class SellCardActivity extends Activity implements View.OnClickListener {


    private ImageView iv_fenlie;
    private LinearLayout ll_fenlie;
    private TextView tv_tiltle;
    private ListView listView;
    private RequestSellCardsTypeBean sellCardsTypeBean = new RequestSellCardsTypeBean();
    private RequestSellCardsInfoBean sellCardsInfoBean = new RequestSellCardsInfoBean();
    private MyListAdapter myListAdapter;
    ProgressDialog dialog;
    private List<RequestSellCardsTypeBean.Data> cardTypeData = new ArrayList<>();
    private MyListPopupViewAdapter myListPopupViewAdapter;
    private ListPopup listPopup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_card);

        initView();
        initData();
    }

    private void initData() {
        findAllCards();
        findCardsByCardId("");
    }

    private void initView() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("加载中……");
        listView = findViewById(R.id.listview);
        tv_tiltle = findViewById(R.id.tv_tiltle);
        findViewById(R.id.iv_back).setOnClickListener(this);
        myListAdapter = new MyListAdapter();

        ll_fenlie =
                findViewById(R.id.ll_fenlie);
        ll_fenlie.setOnClickListener(this);
        iv_fenlie = findViewById(R.id.iv_fenlie);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("cardinfo", sellCardsInfoBean.getData().get(i));
                startActivity(new Intent(SellCardActivity.this, SellCardConfirmActivity.class).putExtras(bundle));
            }
        });

        listPopup = new ListPopup(this);
        listPopup.setOnDismissListener(new BasePopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                iv_fenlie.setImageResource(R.drawable.img_up);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fenlie://分类

                listPopup.showPopupWindow(v);
                iv_fenlie.setImageResource(R.drawable.img_down);
                break;

            case R.id.iv_back://返回
                finish();
                break;
            default:
                break;
        }
    }

    public class MyListAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return sellCardsInfoBean.getData().size();
        }

        @Override
        public Object getItem(int i) {
            return sellCardsInfoBean.getData().get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            //         LayoutInflater.from(SellCardActivity.this).inflate( R.layout.listview_item_club_card, null);


            ViewHolder viewHolder = null;
            if (view == null) {
                viewHolder = new ViewHolder();
                view = LayoutInflater.from(SellCardActivity.this).inflate(R.layout.listview_item_club_card, null);

                viewHolder.tv_name = view.findViewById(R.id.tv_cardname);
                viewHolder.tv_number = view.findViewById(R.id.tv_number);
                viewHolder.tv_time = view.findViewById(R.id.tv_time);
                viewHolder.tv_price = view.findViewById(R.id.tv_price);
                viewHolder.tv_cardtype = view.findViewById(tv_cardtype);
                viewHolder.iv_card = view.findViewById(R.id.iv_card);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            RequestOptions options=new RequestOptions().placeholder(R.drawable.img_club_card);
            Glide.with(SellCardActivity.this).load(sellCardsInfoBean.getData().get(i).getImg_url()).apply(options).into(viewHolder.iv_card);


            if (sellCardsInfoBean.getData().get(i).getCharge_mode() == 1) {//计时卡
                viewHolder.tv_cardtype.setText("卡类型：计时卡");
            }
            if (sellCardsInfoBean.getData().get(i).getCharge_mode() == 2) {//计次卡
                viewHolder.tv_cardtype.setText("卡类型：计次卡");
                viewHolder.tv_cardtype.setText("卡类型：计次卡（" + sellCardsInfoBean.getData().get(i).getTotal_count() + "次）");
            }
            if (sellCardsInfoBean.getData().get(i).getCharge_mode() == 3) {//储值卡
                viewHolder.tv_cardtype.setText("卡类型：储值卡");
            }


            viewHolder.tv_name.setText(sellCardsInfoBean.getData().get(i).getName());
            viewHolder.tv_price.setText("售价：" + PriceUtils.formatPrice2String(sellCardsInfoBean.getData().get(i).getPrice()));
            if (!TextUtils.isEmpty(sellCardsInfoBean.getData().get(i).getTotal_count())) {
                viewHolder.tv_number.setText("次数：" + sellCardsInfoBean.getData().get(i).getTotal_count() + "次");
                viewHolder.tv_number.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tv_number.setVisibility(View.GONE);
            }

            if (sellCardsInfoBean.getData().get(i).getTime_unit() == 1) {
                viewHolder.tv_time.setText("使用时间：" + sellCardsInfoBean.getData().get(i).getTime_value() + "年");
            }
            if (sellCardsInfoBean.getData().get(i).getTime_unit() == 2) {
                viewHolder.tv_time.setText("使用时间：" + sellCardsInfoBean.getData().get(i).getTime_value() + "月");
            }


            return view;
        }


        class ViewHolder {
            TextView tv_name;
            TextView tv_price;
            TextView tv_number;
            TextView tv_time;
            TextView tv_cardtype;
            XCRoundRectImageView iv_card;
        }

    }


    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:

                    listView.setAdapter(myListAdapter);
                    myListAdapter.notifyDataSetChanged();
                    break;
            }
            return true;
        }
    });


    public class ListPopup extends BasePopupWindow {
        private Context context;

        private View popupView;
        private ListView listView;


        public ListPopup(Activity context) {
            super(context, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            setAutoLocatePopup(true);
            bindEvent();

            this.context = context;
        }

        //
        @Override
        protected Animation initShowAnimation() {
            TranslateAnimation translateAnimation = new TranslateAnimation(0f, 0f, -DensityUtils.dp2px(getContext(), 350f), 0);
            translateAnimation.setDuration(450);
            translateAnimation.setInterpolator(new OvershootInterpolator(1));
            return translateAnimation;
        }

        //
        @Override
        protected Animation initExitAnimation() {
            TranslateAnimation translateAnimation = new TranslateAnimation(0f, 0f, 0, -DensityUtils.dp2px(getContext(), 350f));
            translateAnimation.setDuration(450);
            translateAnimation.setInterpolator(new OvershootInterpolator(-4));
            return translateAnimation;
        }


        @Override
        public View getClickToDismissView() {
            return getPopupWindowView();
        }

        @Override
        public View onCreatePopupView() {
            popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_list_menu, null);
            return popupView;
        }

        @Override
        public View initAnimaView() {


            return findViewById(R.id.popup_contianer);
        }

        private void bindEvent() {
            if (popupView != null) {
                listView = popupView.findViewById(R.id.listview);
                myListPopupViewAdapter = new MyListPopupViewAdapter();
                listView.setAdapter(myListPopupViewAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        if (i == 0) {
                            tv_tiltle.setText("全部");
                            findCardsByCardId("");
                        } else {
                            //LogUtil.i(cardTypeData.get(id - 1).getName());
                            tv_tiltle.setText(cardTypeData.get(i - 1).getName());
                            findCardsByCardId(cardTypeData.get(i - 1).getId() + "");
                        }
                        listPopup.dismiss();
                    }
                });
            }

        }


    }

    class MyListPopupViewAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return cardTypeData.size() + 1;
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = View.inflate(SellCardActivity.this, R.layout.listview_item_popup, null);

            TextView tv_cardname = view.findViewById(R.id.tv_cardname);
            if (i == 0) {
                tv_cardname.setText("全部");
            } else {
                tv_cardname.setText(cardTypeData.get(i - 1).getName());
            }


            return view;
        }
    }

    /**
     * 查询所有在售卡
     */
    private void findAllCards() {

        StringRequest request = new StringRequest(Request.Method.GET, Constants.FINDALLCARDS, new Response.Listener<String>() {


            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                Gson gson = new Gson();
                sellCardsTypeBean = gson.fromJson(s, RequestSellCardsTypeBean.class);
                if (sellCardsTypeBean.getData() != null) {
                    cardTypeData = sellCardsTypeBean.getData();
                }


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

    /**
     * 查询所有在售卡by id
     */
    private void findCardsByCardId(String id) {

        dialog.show();
        String params = id;

        String url = Constants.FIND_CARDS_BY_CARDTYPE + id;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                dialog.dismiss();
                LogUtil.i(s);
                Gson gson = new Gson();
                // sellCardsInfoBean = new RequestSellCardsInfoBean();
                sellCardsInfoBean = gson.fromJson(s, RequestSellCardsInfoBean.class);
                if (sellCardsInfoBean.getSuccess()) {
                    Message message = new Message();
                    message.what = 1;
                    //message.obj="haha";
                    handler.sendMessage(message);
                } else {
                    ToastUtils.showToastShort(sellCardsInfoBean.getErrorMsg());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                dialog.dismiss();
                LogUtil.i(volleyError.toString());

            }

        }
        ) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//                Map<String, String> map = new HashMap<String, String>();
//
//                map.put("labelId", "5");
//                // LogUtil.i("Bearer+"+SPUtils.getString(Constants.MY_TOKEN,null));
//                return map;
//            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, null));
                // LogUtil.i("Bearer+"+SPUtils.getString(Constants.MY_TOKEN,null));
                return map;
            }
        };
        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("findCardsByCardId");
        // 设置超时时间
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);

    }
}
