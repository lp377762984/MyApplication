package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cn.danceland.myapplication.R;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_card_confirm);
        initData();
        initView();
    }

    private void initData() {
    }

    private void initView() {
        RadioGroup radioGroup = findViewById(R.id.rg_who);
        tv_select_date = findViewById(R.id.tv_select_date);
        tv_select_counselor = findViewById(R.id.tv_select_counselor);
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
            case value:
                break;
            default:
                break;
        }
    }
}
