package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.RequestConsultantInfoBean;
import com.cn.danceland.myapplication.bean.RequestSellCardsInfoBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.TimeUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import razerdp.basepopup.BasePopupWindow;

import static android.R.attr.value;

/**
 * Created by shy on 2017/11/3 09:44
 * Email:644563767@qq.com
 */


public class SellCardConfirmActivity extends Activity implements View.OnClickListener {

    private LinearLayout ll_phone;
    private EditText et_name;
    private TextView tv_select_date;
    private TextView tv_select_counselor;
    private EditText et_phone;
    private RequestSellCardsInfoBean.Data CardsInfo;
    private Calendar cal;
    private int year, month, day;
    private List<RequestConsultantInfoBean.Data> consultantListInfo = new ArrayList<>();
    private MyPopupListAdapter myPopupListAdapter;
    private ListPopup listPopup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_card_confirm);

        initView();
        initData();
    }

    private void initData() {
        getDate();

        findConsultant(CardsInfo.getBranch_id());
    }

    //获取当前日期
    private void getDate() {
        cal = Calendar.getInstance();

        year = cal.get(Calendar.YEAR);       //获取年月日时分秒

        month = cal.get(Calendar.MONTH);   //获取到的月份是从0开始计数
        day = cal.get(Calendar.DAY_OF_MONTH);
    }

    private void initView() {
        Bundle bundle = this.getIntent().getExtras();
        CardsInfo = (RequestSellCardsInfoBean.Data) bundle.getSerializable("cardinfo");

        myPopupListAdapter = new MyPopupListAdapter(this);
        listPopup = new ListPopup(this);
        TextView tv_name = findViewById(R.id.tv_cardname);
        TextView tv_number = findViewById(R.id.tv_number);
        TextView tv_time = findViewById(R.id.tv_time);
        TextView tv_price = findViewById(R.id.tv_price);
        TextView tv_cardtype = findViewById(R.id.tv_cardtype);


        if (CardsInfo.getCharge_mode() == 1) {//计时卡
            tv_cardtype.setText("卡类型：计时卡");
        }
        if (CardsInfo.getCharge_mode() == 2) {//计次卡
            tv_cardtype.setText("卡类型：计次卡");
        }
        if (CardsInfo.getCharge_mode() == 3) {//储值卡
            tv_cardtype.setText("卡类型：储值卡");
        }


        tv_name.setText(CardsInfo.getName());
        tv_price.setText("售价：" + CardsInfo.getPrice() + "");
        if (!TextUtils.isEmpty(CardsInfo.getTotal_count())) {
            tv_number.setText("次数：" + CardsInfo.getTotal_count() + "次");
            tv_number.setVisibility(View.VISIBLE);
        } else {
            tv_number.setVisibility(View.GONE);
        }

        if (CardsInfo.getTime_unit() == 1) {
            tv_time.setText("使用时间：" + CardsInfo.getTime_value() + "年");
        }
        if (CardsInfo.getTime_unit() == 2) {
            tv_time.setText("使用时间：" + CardsInfo.getTime_value() + "月");
        }


        RadioGroup radioGroup = findViewById(R.id.rg_who);
        tv_select_date = findViewById(R.id.tv_select_date);
        tv_select_date.setOnClickListener(this);
        tv_select_counselor = findViewById(R.id.tv_select_counselor);
        tv_select_counselor.setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_commit).setOnClickListener(this);
        findViewById(R.id.btn_dj_commit).setOnClickListener(this);
        ll_phone = findViewById(R.id.ll_phone);

        et_name = findViewById(R.id.et_name);
        et_phone = findViewById(R.id.et_phone);
        findViewById(R.id.iv_phonebook).setOnClickListener(this);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {

                    case R.id.rbtn_me:
                        //     ToastUtils.showToastShort("自己买");
                        ll_phone.setVisibility(View.GONE);
                        et_name.setVisibility(View.GONE);
                        tv_select_date.setVisibility(View.VISIBLE);
                        tv_select_counselor.setVisibility(View.VISIBLE);


                        break;
                    case R.id.rbtn_other:
                        //    ToastUtils.showToastShort("别人买");
                        ll_phone.setVisibility(View.VISIBLE);
                        et_name.setVisibility(View.VISIBLE);
                        tv_select_date.setVisibility(View.GONE);
                        tv_select_counselor.setVisibility(View.GONE);


                        break;
                    default:
                        break;
                }

            }
        });
    }

    private static final int PICK_CONTACT = 0;

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        String phoneNumber = null;
                        if (hasPhone.equalsIgnoreCase("1")) {
                            hasPhone = "true";
                        } else {
                            hasPhone = "false";
                        }
                        if (Boolean.parseBoolean(hasPhone)) {
                            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                                            + contactId,
                                    null,
                                    null);
                            while (phones.moveToNext()) {
                                phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                //将电话放入
                                et_phone.setText(phoneNumber.trim());

                            }
                            phones.close();
                        }


                    }
                }
                break;
        }
    }

//    private void showListDialog() {
//        AlertDialog.Builder dialog =
//                new AlertDialog.Builder(this);
//        dialog.setTitle("请选择会籍顾问");
//        //  dialog.setView()
//
//        dialog.show();
//
//    }



    class ListPopup extends BasePopupWindow {


        Context context;

        public ListPopup(Context context) {
            super(context);
            ListView popup_list = (ListView) findViewById(R.id.popup_list);
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
            return createPopupById(R.layout.popup_list_consultant);

        }

        @Override
        public View initAnimaView() {
            return null;
        }
    }

    class MyPopupListAdapter extends BaseAdapter {
        private Context context;

        public MyPopupListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return consultantListInfo.size();
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
            LogUtil.i("asdasdjalsdllasjdlk");
            ViewHolder vh = null;
            if (convertView == null) {
                vh = new ViewHolder();
                convertView = View.inflate(context, R.layout.listview_item_list_consultant, null);
                vh.mTextView = (TextView) convertView.findViewById(R.id.item_tx);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            vh.mTextView.setText(consultantListInfo.get(position).getCname());

            vh.mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_select_counselor.setText(consultantListInfo.get(position).getCname());
                    listPopup.dismiss();
                }
            });

            return convertView;

        }


        class ViewHolder {
            public TextView mTextView;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_phonebook://打开通讯录

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                startActivityForResult(intent, PICK_CONTACT);

//                Uri uri = ContactsContract.Contacts.CONTENT_URI;
//                Intent intent = new Intent(Intent.ACTION_PICK,
//                        uri);
//                startActivityForResult(intent, 0);
                break;

            case R.id.iv_back://返回
                finish();
            case R.id.tv_select_counselor://选择会籍顾问

                listPopup.showPopupWindow();
                break;
            case R.id.tv_select_date://选择日期

                DatePickerDialog dialog = new DatePickerDialog(SellCardConfirmActivity.this, 0, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year1, int month2, int day3) {
                        tv_select_date.setText(year1 + "-" + (month2 + 1) + "-" + day3);

                        if (TimeUtils.isDateOneBigger(tv_select_date.getText().toString(), year + "-" + (month + 1) + "-" + day)) {

                        } else {
                            tv_select_date.setText("*请选开卡日期");
                            ToastUtils.showToastShort("不能选择今天以前的日期");
                        }

                    }
                }, year, month, day);//后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
                dialog.show();

                break;
            case R.id.btn_commit://全款支付

                break;
            case R.id.btn_dj_commit://支付定金
                break;
            case value:
                break;
            default:
                break;
        }
    }

    /**
     * 查找会籍顾问
     */
    private void findConsultant(final String branchId) {


        StringRequest request = new StringRequest(Request.Method.POST, Constants.FIND_CONSULTANT_URL, new Response.Listener<String>() {


            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                Gson gson = new Gson();
                RequestConsultantInfoBean requestConsultantInfoBean = gson.fromJson(s, RequestConsultantInfoBean.class);
                if (requestConsultantInfoBean.getSuccess()) {
                    consultantListInfo = requestConsultantInfoBean.getData();
                    LogUtil.i(consultantListInfo.toString());
                    myPopupListAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showToastShort(requestConsultantInfoBean.getErrorMsg());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToastShort(volleyError.toString());
            }
        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();

                map.put("branchId", branchId);

                return map;

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, null));
                return map;
            }
        };
        MyApplication.getHttpQueues().add(request);

    }

}
