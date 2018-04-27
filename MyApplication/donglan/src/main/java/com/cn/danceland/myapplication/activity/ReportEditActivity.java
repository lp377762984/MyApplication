package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.view.DongLanTitleView;

/**
 * Created by feng on 2018/4/26.
 */

public class ReportEditActivity extends Activity {

    DongLanTitleView editpro_title;
    EditText et_edit;
    Button btn_edit;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_editpro);
        initView();

    }

    private void initView() {
        editpro_title = findViewById(R.id.editpro_title);
        editpro_title.setTitle("填写总结");


        et_edit = findViewById(R.id.et_edit);
        btn_edit = findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1,new Intent().putExtra("tv",et_edit.getText().toString()));
                finish();
            }
        });

    }
}
