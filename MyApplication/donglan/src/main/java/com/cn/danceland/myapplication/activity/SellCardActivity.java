package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
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
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.RequestSellCardsInfoBean;
import com.cn.danceland.myapplication.bean.RequestSellCardsTypeBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import razerdp.basepopup.BasePopupWindow;

import static android.R.attr.id;

/**
 * Created by shy on 2017/11/2 16:37
 * Email:644563767@qq.com
 */


public class SellCardActivity extends Activity implements View.OnClickListener {

    private String[] names = {"黄金年卡", "白金年卡", "钻石年卡"};
    private String[] data = new String[]{"全部", "年卡", "季卡", "月卡", "限时活动"};
    private ImageView iv_fenlie;
    private LinearLayout ll_fenlie;
    private TextView tv_tiltle;
    private ListView listView;
    private RequestSellCardsTypeBean sellCardsTypeBean = new RequestSellCardsTypeBean();
    private RequestSellCardsInfoBean sellCardsInfoBean = new RequestSellCardsInfoBean();
    private MyListAdapter myListAdapter;
    ProgressDialog dialog;
   private List<RequestSellCardsTypeBean.Data> cardTypeData=new ArrayList<>();
    private  MyListPopupViewAdapter myListPopupViewAdapter;
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
                startActivity(new Intent(SellCardActivity.this, SellCardConfirmActivity.class));
            }
        });

        listPopup = new ListPopup(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fenlie://分类
//                if (sellCardsTypeBean.getData() != null && sellCardsTypeBean.getData().size() > 0) {
//                    initDroppyMenu(ll_fenlie, sellCardsTypeBean.getData());
//                    showDroppyMenu();
//                } else {
//                    ToastUtils.showToastShort("没有数据");
//                }

                listPopup.showPopupWindow(v);

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


                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            if (sellCardsInfoBean.getData().get(i).getChargeMode() == 1) {//计时卡

            }
            if (sellCardsInfoBean.getData().get(i).getChargeMode() == 2) {//计次卡

            }
            if (sellCardsInfoBean.getData().get(i).getChargeMode() == 3) {//储值卡

            }


            viewHolder.tv_name.setText(sellCardsInfoBean.getData().get(i).getName());
            viewHolder.tv_price.setText(sellCardsInfoBean.getData().get(i).getPrice() + "");
            viewHolder.tv_number.setText(sellCardsInfoBean.getData().get(i).getTotalCount() + "");
            viewHolder.tv_time.setText(sellCardsInfoBean.getData().get(i).getLength() + "");
            return view;
        }


        class ViewHolder {
            TextView tv_name;
            TextView tv_price;
            TextView tv_number;
            TextView tv_time;

        }

    }


//    //弹出下拉框
//    protected void showDroppyMenu() {
//
//        iv_fenlie.setImageResource(R.drawable.img_down);
////        ListView listView = (ListView) droppyMenu.getMenuView().findViewById(R.id.listview);
////        listView.setAdapter(new MyAdapter());
//        droppyMenu.show();
//    }

    //DroppyMenuPopup droppyMenu;


//    //绑定下拉框
//    private void initDroppyMenu(View btn, final List<RequestSellCardsTypeBean.Data> data) {
//        DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(SellCardActivity.this, btn);
//
//        droppyBuilder.addMenuItem(new DroppyMenuItem("全部"))
//                .addSeparator();
//
//        for (int j = 0; j < data.size(); j++) {
//
//            droppyBuilder.addMenuItem(new DroppyMenuItem(data.get(j).getName()))
//                    .addSeparator();
//        }
//
//
//        droppyBuilder
//                .setOnDismissCallback(new DroppyMenuPopup.OnDismissCallback() {
//                    @Override
//                    public void call() {
//                        iv_fenlie.setImageResource(R.drawable.img_up);
//                    }
//                })
//                .setOnClick(new DroppyClickCallbackInterface() {
//                    @Override
//                    public void call(View v, int id) {
//
//                        iv_fenlie.setImageResource(R.drawable.img_up);
//                        if (id == 0) {
//                            tv_tiltle.setText("全部");
//                            findCardsByCardId("");
//                        } else {
//                            tv_tiltle.setText(data.get(id - 1).getName());
//                            findCardsByCardId(data.get(id - 1).getId() + "");
//                        }
//
//
//                    }
//                })
//                .setPopupAnimation(new DroppyFadeInAnimation())
//                .triggerOnAnchorClick(false);
//
//
//        droppyMenu = droppyBuilder.build();
//    }

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
            super(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            setAutoLocatePopup(true);
            bindEvent();

            this.context = context;
        }


        @Override
        protected Animation initShowAnimation() {
            return getDefaultAlphaAnimation();
        }


        @Override
        public View getClickToDismissView() {
            return null;
        }

        @Override
        public View onCreatePopupView() {
            popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_list_menu, null);
            return popupView;
        }

        @Override
        public View initAnimaView() {
            // return popupView.findViewById(R.id.popup_contianer);
            return null;
        }

        private void bindEvent() {
            if (popupView != null) {
                listView = popupView.findViewById(R.id.listview);
                myListPopupViewAdapter=new MyListPopupViewAdapter();
                listView.setAdapter(myListPopupViewAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                     //   ToastUtils.showToastShort("我被点击了"+ i);

                        if (id == 0) {
                            tv_tiltle.setText("全部");
                            findCardsByCardId("");
                        } else {
                            tv_tiltle.setText(cardTypeData.get(id - 1).getName());
                            findCardsByCardId(cardTypeData.get(id - 1).getId() + "");
                        }

                    }
                });
            }

        }





    }

    class MyListPopupViewAdapter extends BaseAdapter{



        @Override
        public int getCount() {
            return cardTypeData.size()+1;
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
            view=View.inflate(SellCardActivity.this,R.layout.listview_item_popup,null);

            TextView tv_cardname=view.findViewById(R.id.tv_cardname);
            if (i==0){
                tv_cardname.setText("全部");
            }else {
                tv_cardname.setText(cardTypeData.get(i-1).getName());
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
                if (sellCardsTypeBean.getData()!=null){
                    cardTypeData=sellCardsTypeBean.getData();
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
