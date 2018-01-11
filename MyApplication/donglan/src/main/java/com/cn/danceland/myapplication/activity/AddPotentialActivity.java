package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.ParamInfoBean;
import com.cn.danceland.myapplication.bean.RequestSellCardsInfoBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import razerdp.basepopup.BasePopupWindow;


/**
 * Created by shy on 2018/1/9 11:50
 * Email:644563767@qq.com
 * 添加潜客
 */


public class AddPotentialActivity extends Activity implements OnClickListener {

    public static final int REGISTER_CHANNEL = 1;//客户来源
    public static final int VISIT_TYPE = 9;//回访方式

    private EditText et_remark;//备注
    private TextView tv_medical_history;//慢性病史
    private EditText et_phone;//电话
    private EditText et_name;//名字
    private TextView tv_sex;//性别
    private EditText et_weixin_no;//微信号
    private EditText et_company;//单位
    private EditText et_address;//地址
    private EditText et_email;//邮箱
    private TextView tv_guest_aware_way;//客户来源
    private ScaleRatingBar sr_fitness_level;//健身指数
    private ScaleRatingBar sr_follow_level;//关注程度
    private TextView tv_target;//健身目的
    private TextView tv_card_type;//意向卡型
    private TextView tv_like;//喜欢项目
    private Gson gson = new Gson();
    private List<ParamInfoBean.Data> mParamInfoList = new ArrayList<>();
    private List<RequestSellCardsInfoBean.Data> mParamCradList = new ArrayList<>();
    private ListPopup listPopup;
    private int codeType = 0;
    private ListPopupMultiSelect listPopupMultiSelect;
    private ListCardPopup listCardPopup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_potential_customer);
        initView();
    }


    private void initView() {
        listPopup = new ListPopup(this);
        listPopupMultiSelect = new ListPopupMultiSelect(this);
        listCardPopup = new ListCardPopup(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        et_phone = findViewById(R.id.et_phone);
        et_name = findViewById(R.id.et_name);
        tv_sex = findViewById(R.id.tv_sex);
        tv_sex.setOnClickListener(this);

        et_weixin_no = findViewById(R.id.et_weixin_no);
        et_company = findViewById(R.id.et_company);
        et_address = findViewById(R.id.et_address);
        et_email = findViewById(R.id.et_email);


        tv_guest_aware_way = findViewById(R.id.tv_guest_aware_way);
        tv_guest_aware_way.setOnClickListener(this);


        sr_fitness_level = findViewById(R.id.sr_fitness_level);
        sr_fitness_level.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar baseRatingBar, float rating) {

                LogUtil.i("sr_fitness_level: " + (int) rating);
            }
        });
        sr_follow_level = findViewById(R.id.sr_follow_level);
        sr_follow_level.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar baseRatingBar, float rating) {

                LogUtil.i("sr_follow_level: " + (int) rating);
            }
        });
        tv_target = findViewById(R.id.tv_target);
        tv_target.setOnClickListener(this);
        tv_card_type = findViewById(R.id.tv_card_type);
        tv_card_type.setOnClickListener(this);
        tv_like = findViewById(R.id.tv_like);
        tv_like.setOnClickListener(this);
        tv_medical_history = findViewById(R.id.tv_medical_history);
        tv_medical_history.setOnClickListener(this);
        et_remark = findViewById(R.id.et_remark);
        findViewById(R.id.btn_commit).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_sex://选择性别
                showListDialog();
                break;
            case R.id.tv_card_type://选择意向卡型
                codeType = 20;
                findCardsByCardId("");
                break;
            case R.id.tv_guest_aware_way://选择客户来源
                codeType = REGISTER_CHANNEL;
                findParams(codeType);

                break;
            case R.id.tv_target://选择健身目的
                codeType = 5;
                findParams(codeType);
                break;
            case R.id.tv_like://选择喜欢项目
                codeType = 6;
                findParams(codeType);
                break;
            case R.id.tv_medical_history://选择慢性病史
                codeType = 13;
                findParams(codeType);
                break;
            case R.id.btn_commit://保存

                break;
            default:
                break;
        }
    }

    private void showListDialog() {
        final String[] items = {"男", "女"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(this);
        //listDialog.setTitle("我是一个列表Dialog");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case 0:

                        tv_sex.setText("男");
                        break;
                    case 1:

                        tv_sex.setText("女");
                        break;
                    default:
                        break;
                }
            }
        });
        listDialog.show();
    }


    private void showInputDialog() {
    /*@setView 装入一个EditView
     */
        final EditText editText = new EditText(this);
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(this);
        inputDialog.setTitle("请输入电话号码").setView(editText);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(AddPotentialActivity.this,
                                editText.getText().toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
        inputDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        inputDialog.show();
    }


    /**
     * 查询所有在售卡by id
     */
    private void findCardsByCardId(String id) {

        String params = id;

        String url = Constants.FIND_CARDS_BY_CARDTYPE + id;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                Gson gson = new Gson();
                // sellCardsInfoBean = new RequestSellCardsInfoBean();
                RequestSellCardsInfoBean sellCardsInfoBean = gson.fromJson(s, RequestSellCardsInfoBean.class);
                if (sellCardsInfoBean.getSuccess()) {
                    mParamCradList = sellCardsInfoBean.getData();
                    listCardPopup.showPopupWindow();
                } else {
                    ToastUtils.showToastShort(sellCardsInfoBean.getErrorMsg());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                LogUtil.i(volleyError.toString());

            }

        }
        ) {

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


    private void findParams(final int codetype) {
        StringRequest request = new StringRequest(Request.Method.GET, Constants.FIND_BY_TYPE_CODE + codetype, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                ParamInfoBean paramInfoBean = gson.fromJson(s, ParamInfoBean.class);
                mParamInfoList = paramInfoBean.getData();
                if (codetype == 1) {
                    listPopup.showPopupWindow();
                }
                if (codetype == 5 || codetype == 6 || codetype == 13) {
                    listPopupMultiSelect.showPopupWindow();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                ToastUtils.showToastShort(volleyError.toString());

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));

                return map;
            }
        };
        MyApplication.getHttpQueues().add(request);


    }

    // private MyPopupListAdapter myPopupListAdapter = new MyPopupListAdapter(this);


    class ListPopup extends BasePopupWindow {


        Context context;

        public ListPopup(Context context) {
            super(context);
            ListView popup_list = (ListView) findViewById(R.id.popup_list);
            MyPopupListAdapter myPopupListAdapter = new MyPopupListAdapter(context);
            popup_list.setAdapter(myPopupListAdapter);
            this.context = context;
        }

        @Override
        protected Animation initShowAnimation() {
            return null;
        }

        @Override
        public View getClickToDismissView() {
            return getPopupWindowView();
        }

        @Override
        public View onCreatePopupView() {

            //  popupView=View.inflate(context,R.layout.popup_list_consultant,null);
            return createPopupById(R.layout.popup_list_potential);

        }

        @Override
        public View initAnimaView() {
            return null;
        }

        class MyPopupListAdapter extends BaseAdapter {
            private Context context;

            public MyPopupListAdapter(Context context) {
                this.context = context;
            }

            @Override
            public int getCount() {
                return mParamInfoList.size();
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
            public View getView(final int position, View convertView, ViewGroup viewGroup) {
                //     LogUtil.i("asdasdjalsdllasjdlk");
                ViewHolder vh = null;
                if (convertView == null) {
                    vh = new ViewHolder();
                    convertView = View.inflate(context, R.layout.listview_item_list_consultant, null);
                    vh.mTextView = (TextView) convertView.findViewById(R.id.item_tx);
                    convertView.setTag(vh);
                } else {
                    vh = (ViewHolder) convertView.getTag();
                }
                vh.mTextView.setText(mParamInfoList.get(position).getData_value());

                vh.mTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        switch (codeType) {
                            case 1:
                                tv_guest_aware_way.setText(mParamInfoList.get(position).getData_value());
                                break;
                            default:
                                break;
                        }


                        listPopup.dismiss();
                    }
                });

                return convertView;

            }


            class ViewHolder {
                public TextView mTextView;
            }
        }
    }


    class ListCardPopup extends BasePopupWindow {


        Context context;

        public ListCardPopup(Context context) {
            super(context);
            ListView popup_list = (ListView) findViewById(R.id.popup_list);
            MyPopupListAdapter myPopupListAdapter = new MyPopupListAdapter(context);
            popup_list.setAdapter(myPopupListAdapter);
            this.context = context;
        }

        @Override
        protected Animation initShowAnimation() {
            return null;
        }

        @Override
        public View getClickToDismissView() {
            return getPopupWindowView();
        }

        @Override
        public View onCreatePopupView() {

            //  popupView=View.inflate(context,R.layout.popup_list_consultant,null);
            return createPopupById(R.layout.popup_list_potential);

        }

        @Override
        public View initAnimaView() {
            return null;
        }

        class MyPopupListAdapter extends BaseAdapter {
            private Context context;

            public MyPopupListAdapter(Context context) {
                this.context = context;
            }

            @Override
            public int getCount() {
                return mParamCradList.size();
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
            public View getView(final int position, View convertView, ViewGroup viewGroup) {
                //     LogUtil.i("asdasdjalsdllasjdlk");
                ViewHolder vh = null;
                if (convertView == null) {
                    vh = new ViewHolder();
                    convertView = View.inflate(context, R.layout.listview_item_list_consultant, null);
                    vh.mTextView = (TextView) convertView.findViewById(R.id.item_tx);
                    convertView.setTag(vh);
                } else {
                    vh = (ViewHolder) convertView.getTag();
                }
                vh.mTextView.setText(mParamCradList.get(position).getName());

                vh.mTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        switch (codeType) {
                            case 20:
                                tv_card_type.setText(mParamCradList.get(position).getName());
                                break;
                            default:
                                break;
                        }

                        dismiss();
                    }
                });

                return convertView;

            }


            class ViewHolder {
                public TextView mTextView;
            }
        }
    }


    class ListPopupMultiSelect extends BasePopupWindow {


        Context context;

        public ListPopupMultiSelect(Context context) {
            super(context);
            this.context = context;
            ListView popup_list = (ListView) findViewById(R.id.popup_list);
            popup_list.setAdapter(new MyPopupListMultiSelectAdapter(context));
            findViewById(R.id.btn_ok).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    String s = "";
                    for (int j = 0; j < mParamInfoList.size(); j++) {
                        if (mParamInfoList.get(j).isCheck()) {
                            if (s == "") {
                                s = mParamInfoList.get(j).getData_value();
                            } else {
                                s = s + "," + mParamInfoList.get(j).getData_value();
                            }
                        }

                    }
                    switch (codeType) {
                        case 5:
                            if (!TextUtils.isEmpty(s)) {
                                tv_target.setText(s);
                            } else {
                                tv_target.setText("未选择");
                            }
                            break;
                        case 6:
                            if (!TextUtils.isEmpty(s)) {
                                tv_like.setText(s);
                            } else {
                                tv_like.setText("未选择");
                            }
                            break;
                        case 13:
                            if (!TextUtils.isEmpty(s)) {
                                tv_medical_history.setText(s);
                            } else {
                                tv_medical_history.setText("未选择");
                            }
                            break;
                        default:
                            break;
                    }

                    dismiss();
                }

            });
        }

        @Override
        protected Animation initShowAnimation() {
            return null;
        }

        @Override
        public View getClickToDismissView() {
            return getPopupWindowView();
        }

        @Override
        public View onCreatePopupView() {

            //  popupView=View.inflate(context,R.layout.popup_list_consultant,null);
            return createPopupById(R.layout.popup_list_potential_multiselect);

        }

        @Override
        public View initAnimaView() {
            return null;
        }


        class MyPopupListMultiSelectAdapter extends BaseAdapter {
            private Context context;

            public MyPopupListMultiSelectAdapter(Context context) {
                this.context = context;
            }

            @Override
            public int getCount() {
                return mParamInfoList.size();
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
            public View getView(final int position, View convertView, ViewGroup viewGroup) {
                //     LogUtil.i("asdasdjalsdllasjdlk");
                ViewHolder vh = null;
                if (convertView == null) {
                    vh = new ViewHolder();
                    convertView = View.inflate(context, R.layout.listview_item_list_multiselect, null);
                    vh.mTextView = convertView.findViewById(R.id.item_tx);
                    vh.checkBox = convertView.findViewById(R.id.cb_item);
                    convertView.setTag(vh);
                } else {
                    vh = (ViewHolder) convertView.getTag();
                }
                vh.mTextView.setText(mParamInfoList.get(position).getData_value());
                if (mParamInfoList.get(position).isCheck()) {
                    vh.checkBox.setChecked(true);
                } else {
                    vh.checkBox.setChecked(false);
                }
                vh.checkBox.setText(mParamInfoList.get(position).getData_value());
                vh.checkBox.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mParamInfoList.get(position).isCheck()) {
                            mParamInfoList.get(position).setCheck(false);
                        } else {
                            mParamInfoList.get(position).setCheck(true);
                        }
                        notifyDataSetChanged();
                    }
                });


                return convertView;

            }


            class ViewHolder {
                public TextView mTextView;
                public CheckBox checkBox;
            }
        }
    }


//
//    /**
//     * 查找会籍顾问
//     */
//    private void findConsultant(final String branchId) {
//
//
//        StringRequest request = new StringRequest(Request.Method.POST, Constants.FIND_CONSULTANT_URL, new Response.Listener<String>() {
//
//
//            @Override
//            public void onResponse(String s) {
//                LogUtil.i(s);
//                Gson gson = new Gson();
//                RequestConsultantInfoBean requestConsultantInfoBean = gson.fromJson(s, RequestConsultantInfoBean.class);
//                if (requestConsultantInfoBean.getSuccess()) {
//                    consultantListInfo = requestConsultantInfoBean.getData();
//                    //  LogUtil.i(consultantListInfo.toString());
//                    myPopupListAdapter.notifyDataSetChanged();
//
//                } else {
//                    ToastUtils.showToastShort(requestConsultantInfoBean.getErrorMsg());
//                }
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                ToastUtils.showToastShort(volleyError.toString());
//            }
//        }) {
//
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//                Map<String, String> map = new HashMap<String, String>();
//
//                map.put("branchId", branchId);
//
//                return map;
//
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, null));
//                return map;
//            }
//        };
//        MyApplication.getHttpQueues().add(request);
//
//    }


}
