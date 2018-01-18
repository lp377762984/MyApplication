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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.request.RequestOptions;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.ParamInfoBean;
import com.cn.danceland.myapplication.bean.PotentialInfo;
import com.cn.danceland.myapplication.bean.RequestSellCardsInfoBean;
import com.cn.danceland.myapplication.bean.RequsetSimpleBean;
import com.cn.danceland.myapplication.evntbus.IntEvent;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

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


public class EditPotentialActivity extends Activity implements OnClickListener {

    public static final int REGISTER_CHANNEL = 1;//客户来源
    public static final int VISIT_TYPE = 9;//回访方式
    public static final int TARGET = 8;//健身目的
    public static final int LIKE = 7;//喜欢项目
    public static final int MEDICAL = 11;//病史
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
    private List<ParamInfoBean.Data> mParamInfoList = new ArrayList<>();
    private List<RequestSellCardsInfoBean.Data> mParamCradList = new ArrayList<>();
    private ListPopup listPopup;
    private int codeType = 0;
    private ListPopupMultiSelect listPopupMultiSelect;
    private ListCardPopup listCardPopup;
    private PotentialInfo info = new PotentialInfo();
    private Gson gson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_potential_customer);
        initView();
        setData();
    }

    private void setData() {
        if (!TextUtils.isEmpty(info.getSelf_avatar_url())) {
            RequestOptions options = new RequestOptions().placeholder(R.drawable.img_my_avatar);
            String S = info.getSelf_avatar_url();
            //    Glide.with(mActivity).load(S).apply(options).into(iv_avatar);
        }

//        if (TextUtils.equals(info.getGender(), "1")) {
//            iv_sex.setImageResource(R.drawable.img_sex1);
//        }
//        if (TextUtils.equals(info.getGender(), "2")) {
//            iv_sex.setImageResource(R.drawable.img_sex2);
//        }
        et_phone.setText(info.getPhone_no());
        et_name.setText(info.getCname());
        //et_lasttime.setText("最后维护时间：" + info.getLast_time());
        et_weixin_no.setText(info.getWeichat_no());
        et_company.setText(info.getCompany());
        et_address.setText(info.getAddress());
        et_email.setText(info.getMail());
        tv_guest_aware_way.setText(info.getGuest_aware_way());
        if (!TextUtils.isEmpty(info.getFitness_level())) {
            sr_fitness_level.setRating(Integer.parseInt(info.getFitness_level()));
        }
        if (!TextUtils.isEmpty(info.getFollow_level())) {
            sr_follow_level.setRating(Integer.parseInt(info.getFollow_level()));
        }


        tv_card_type.setText(info.getCard_type());
        et_remark.setText(info.getRemark());


        //  List<String> list = new ArrayList<String>();
        if (info.getTargetList() != null && info.getTargetList().size() > 0) {
            String s = "";
            for (int j = 0; j < info.getTargetList().size(); j++) {

                if (s == "") {
                    s = info.getTargetList().get(j).getData_value();
                } else {
                    s = s + "," + info.getTargetList().get(j).getData_value();
                }
                //  list.add(mParamInfoList.get(j).getData_key() + "");
            }
            tv_target.setText(s);
        }
        if (info.getProjectList() != null && info.getProjectList().size() > 0) {
            String s1 = "";
            for (int j = 0; j < info.getProject_ids().size(); j++) {

                if (s1 == "") {
                    s1 = info.getProjectList().get(j).getData_value();
                } else {
                    s1 = s1 + "," + info.getProjectList().get(j).getData_value();
                }
            }
            tv_like.setText(s1);
        }
        if (info.getChonicList() != null && info.getChonicList().size() > 0) {
            String s2 = "";
            for (int j = 0; j < info.getChonicList().size(); j++) {

                if (s2 == "") {
                    s2 = info.getChonicList().get(j).getData_value();
                } else {
                    s2 = s2 + "," + info.getChonicList().get(j).getData_value();
                }
            }
            tv_medical_history.setText(s2);
        }
        if (TextUtils.equals(info.getGender(), "1")) {
            tv_sex.setText("男");
        }
        if (TextUtils.equals(info.getGender(), "2")) {
            tv_sex.setText("女"
            );
        }

    }


    private void initView() {
        Bundle bundle = getIntent().getExtras();
        info = (PotentialInfo) bundle.getSerializable("info");
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


                if ((int) rating != 0) {
                    info.setFitness_level((int) rating + "");
                }
            }
        });
        sr_follow_level = findViewById(R.id.sr_follow_level);
        sr_follow_level.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar baseRatingBar, float rating) {
                if ((int) rating != 0) {
                    info.setFollow_level((int) rating + "");
                }

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
                codeType = TARGET;
                findParams(codeType);
                break;
            case R.id.tv_like://选择喜欢项目
                codeType = LIKE;
                findParams(codeType);
                break;
            case R.id.tv_medical_history://选择慢性病史
                codeType = MEDICAL;
                findParams(codeType);
                break;
            case R.id.btn_commit://保存
                if (TextUtils.isEmpty(et_phone.getText().toString().trim())) {
                    ToastUtils.showToastShort("手机号码必须填写");
                    return;
                }
                info.setPhone_no(et_phone.getText().toString());
                if (TextUtils.isEmpty(info.getGender())) {
                    ToastUtils.showToastShort("性别必须填写");
                    return;
                }
                if (TextUtils.isEmpty(et_name.getText().toString().trim())) {
                    ToastUtils.showToastShort("姓名必须填写");
                    return;
                }
                info.setCname(et_name.getText().toString());
                info.setCompany(et_company.getText().toString());
                info.setAddress(et_address.getText().toString());
                info.setMail(et_email.getText().toString());
                info.setRemark(et_remark.getText().toString());
                info.setWeichat_no(et_weixin_no.getText().toString());
                Data data = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
                info.setDefault_branch(data.getDefault_branch());

                LogUtil.i(gson.toJson(info).toString());
                try {
                    update_potential(gson.toJson(info).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                        info.setGender("1");
                        break;
                    case 1:

                        tv_sex.setText("女");
                        info.setGender("2");
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
                        Toast.makeText(EditPotentialActivity.this,
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
     * 编辑资料
     *
     * @param data
     * @throws JSONException
     */
    public void update_potential(final String data) throws JSONException {


        JSONObject jsonObject = new JSONObject(data);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.PUT, Constants.UPDATE_POTENTIAL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtil.i(jsonObject.toString());
                RequsetSimpleBean requestOrderBean = gson.fromJson(jsonObject.toString(), RequsetSimpleBean.class);
                if (requestOrderBean.isSuccess()) {
                    ToastUtils.showToastShort("保存成功");
                    EventBus.getDefault().post(new IntEvent(0,211));//更新资料详情页面
                    finish();
                } else {
                    ToastUtils.showToastShort("保存失败");
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
        MyApplication.getHttpQueues().add(stringRequest);

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

                if (paramInfoBean.getSuccess()) {
                    mParamInfoList = paramInfoBean.getData();


                    if (codetype == REGISTER_CHANNEL) {
                        listPopup.showPopupWindow();
                    }
                    if (codetype == LIKE || codetype == TARGET || codetype == MEDICAL) {
                        listPopupMultiSelect.showPopupWindow();
                    }
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

                vh.mTextView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        switch (codeType) {
                            case 1:
                                tv_guest_aware_way.setText(mParamInfoList.get(position).getData_value());
                                info.setGuest_aware_way(mParamInfoList.get(position).getData_key() + "");
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

                vh.mTextView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        switch (codeType) {
                            case 20:
                                tv_card_type.setText(mParamCradList.get(position).getName());
                                info.setCard_type(tv_card_type.getText().toString());
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
                    List<String> list = new ArrayList<String>();
                    for (int j = 0; j < mParamInfoList.size(); j++) {
                        if (mParamInfoList.get(j).isCheck()) {
                            if (s == "") {
                                s = mParamInfoList.get(j).getData_value();
                            } else {
                                s = s + "," + mParamInfoList.get(j).getData_value();
                            }
                            list.add(mParamInfoList.get(j).getData_key() + "");

                        }

                    }
                    switch (codeType) {
                        case TARGET:
                            if (!TextUtils.isEmpty(s)) {
                                tv_target.setText(s);
                                info.setTarget_ids(list);
                            } else {
                                tv_target.setText("未选择");
                                info.setTarget_ids(null);
                            }
                            break;
                        case LIKE:
                            if (!TextUtils.isEmpty(s)) {
                                tv_like.setText(s);
                                info.setProject_ids(list);
                            } else {
                                tv_like.setText("未选择");
                                info.setProject_ids(null);
                            }
                            break;
                        case MEDICAL:
                            if (!TextUtils.isEmpty(s)) {
                                tv_medical_history.setText(s);
                                info.setChronic_ids(list);
                            } else {
                                tv_medical_history.setText("未选择");
                                info.setChronic_ids(null);
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
