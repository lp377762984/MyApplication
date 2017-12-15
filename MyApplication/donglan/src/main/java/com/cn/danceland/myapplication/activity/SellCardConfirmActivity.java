package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.RequestSellCardsInfoBean;
import com.cn.danceland.myapplication.utils.TimeUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;

import java.util.Calendar;

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
    private int year,month,day;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_card_confirm);

        initView();
        initData();
    }

    private void initData() {
        getDate();
    }

    //获取当前日期
    private void getDate() {
        cal=Calendar.getInstance();

        year=cal.get(Calendar.YEAR);       //获取年月日时分秒

        month=cal.get(Calendar.MONTH);   //获取到的月份是从0开始计数
        day=cal.get(Calendar.DAY_OF_MONTH);
    }
    private void initView() {
        Bundle bundle = this.getIntent().getExtras();
        CardsInfo = (RequestSellCardsInfoBean.Data) bundle.getSerializable("cardinfo");

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
       tv_price.setText("售价："+CardsInfo.getPrice() + "");
        if (!TextUtils.isEmpty(CardsInfo.getTotal_count())){
           tv_number.setText("次数："+CardsInfo.getTotal_count() + "次");
           tv_number.setVisibility(View.VISIBLE);
        }else {
           tv_number.setVisibility(View.GONE);
        }

        if (CardsInfo.getTime_unit()==1){
          tv_time.setText("使用时间："+CardsInfo.getTime_value() + "年");
        }
        if (CardsInfo.getTime_unit()==2){
            tv_time.setText("使用时间："+CardsInfo.getTime_value() + "月");
        }


        RadioGroup radioGroup = findViewById(R.id.rg_who);
        tv_select_date = findViewById(R.id.tv_select_date);
        tv_select_date.setOnClickListener(this);
        tv_select_counselor = findViewById(R.id.tv_select_counselor);
        tv_select_counselor.setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
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

    private void showListDialog(){
        AlertDialog.Builder dialog =
                new AlertDialog.Builder(this);
        dialog.setTitle("请选择会籍顾问");
      //  dialog.setView()

        dialog.show();

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
            case R.id.tv_select_counselor:



                break;
            case R.id.tv_select_date:

                DatePickerDialog dialog=new DatePickerDialog(SellCardConfirmActivity.this, 0, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year1, int month2, int day3) {
                        tv_select_date.setText(year1+"-"+(month2+1)+"-"+day3);

                        if (TimeUtils.isDateOneBigger(tv_select_date.getText().toString(),year+"-"+(month+1)+"-"+day)){

                        }else {
                            tv_select_date.setText("*请选开卡日期");
                            ToastUtils.showToastShort("不能选择今天以前的日期");
                        }

                    }
                }, year, month, day);//后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
                dialog.show();

                break;
            case value:
                break;
            default:
                break;
        }
    }
}
