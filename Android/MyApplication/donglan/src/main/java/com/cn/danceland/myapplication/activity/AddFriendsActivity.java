package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.utils.ToastUtils;

/**
 * Created by shy on 2017/10/30 09:32
 * <p>
 * Email:644563767@qq.com
 */


public class AddFriendsActivity extends Activity implements View.OnClickListener {
    EditText mEtPhone;
    private ImageButton iv_del;
    private LinearLayout ll_search;
    private TextView tv_search;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        iv_del = findViewById(R.id.iv_del);
        ll_search = findViewById(R.id.ll_search);
        tv_search = findViewById(R.id.tv_search);
        mEtPhone = findViewById(R.id.et_phone);

        setListener();
    }

    private void setListener() {
        ll_search.setOnClickListener(this);
        iv_del.setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        mEtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            //监听edit
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(mEtPhone.getText().toString().trim())) {
                    iv_del.setVisibility(View.VISIBLE);
                    ll_search.setVisibility(View.VISIBLE);

//                    ll_search.setFocusable(true);
//                    ll_search.setFocusableInTouchMode(true);
//                    ll_search.requestFocus();


                    tv_search.setText("搜索：“" + mEtPhone.getText().toString().trim() + "”");

                } else {
                    iv_del.setVisibility(View.GONE);
                    ll_search.setVisibility(View.GONE);
                    tv_search.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_del:
                mEtPhone.setText("");
                break;
            case R.id.iv_back://返回
                finish();
                break;
            case R.id.ll_search://搜索
                ToastUtils.showToastShort(mEtPhone.getText().toString());
                break;
            default:
                break;
        }
    }
}
