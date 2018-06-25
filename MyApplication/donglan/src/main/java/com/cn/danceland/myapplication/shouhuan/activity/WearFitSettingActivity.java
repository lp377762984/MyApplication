package com.cn.danceland.myapplication.shouhuan.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.StringUtils;
import com.cn.danceland.myapplication.view.DongLanTitleView;

/**
 * Created by feng on 2018/6/21.
 */

public class WearFitSettingActivity extends Activity {

    private DongLanTitleView wearfit_setting_title;
    private TextView tv_shouhuan_name;
    private TextView tv_shouhuan_num;
    private TextView tv_status;
    private TextView tv_power;
    private Button btn_jiebang;
    private RelativeLayout rl_buchang;
    private RelativeLayout rl_peidai;
    private Switch sw_taishou;
    private Switch sw_fangdiu;
    private Button btn_juli_left;
    private Button btn_juli_right;
    private Button btn_buchang_left;
    private Button btn_buchang_right;
    private RelativeLayout rl_rushui;
    private RelativeLayout rl_wake;
    private Switch sw_laidian;
    private Switch sw_duanxin;
    private RelativeLayout rl_naozhong;
    private RelativeLayout rl_jiuzuo;
    private RelativeLayout rl_wurao;
    private RelativeLayout rl_app;
    private TextView tv_buchang;
    private TextView tv_peidai;
    private TextView tv_rushui;
    private TextView tv_wake;
    private String address;
    private String name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wearfitsetting);
        initHost();
        initView();
    }

    private void initHost() {
        address = SPUtils.getString(Constants.ADDRESS, "");
        name = SPUtils.getString(Constants.NAME, "");

    }

    private void initView() {

        wearfit_setting_title = findViewById(R.id.wearfit_setting_title);
        wearfit_setting_title.setTitle("设置");
        tv_shouhuan_name = findViewById(R.id.tv_shouhuan_name);
        tv_shouhuan_num = findViewById(R.id.tv_shouhuan_num);
        tv_status = findViewById(R.id.tv_status);
        tv_power = findViewById(R.id.tv_power);
        btn_jiebang = findViewById(R.id.btn_jiebang);
        rl_buchang = findViewById(R.id.rl_buchang);
        rl_peidai = findViewById(R.id.rl_peidai);
        sw_taishou = findViewById(R.id.sw_taishou);
        sw_fangdiu = findViewById(R.id.sw_fangdiu);
        btn_juli_left = findViewById(R.id.btn_juli_left);
        btn_juli_right = findViewById(R.id.btn_juli_right);
        btn_buchang_left = findViewById(R.id.btn_buchang_left);
        btn_buchang_right = findViewById(R.id.btn_buchang_right);
        rl_rushui = findViewById(R.id.rl_rushui);
        rl_wake = findViewById(R.id.rl_wake);
        sw_laidian = findViewById(R.id.sw_laidian);
        sw_duanxin = findViewById(R.id.sw_duanxin);
        rl_naozhong = findViewById(R.id.rl_naozhong);
        rl_jiuzuo = findViewById(R.id.rl_jiuzuo);
        rl_wurao = findViewById(R.id.rl_wurao);
        rl_app = findViewById(R.id.rl_app);
        tv_buchang = findViewById(R.id.tv_buchang);
        tv_peidai = findViewById(R.id.tv_peidai);
        tv_rushui = findViewById(R.id.tv_rushui);
        tv_wake = findViewById(R.id.tv_wake);
        setData();
        setClickListener();
    }

    private void setData() {

        if (StringUtils.isNullorEmpty(address) || StringUtils.isNullorEmpty(name)) {
            tv_shouhuan_name.setText("未绑定");
        }else{
            tv_shouhuan_name.setText(name);
            tv_shouhuan_num.setText(address);
            tv_status.setText("已绑定");
        }

    }

    private void setClickListener() {

        rl_app.setOnClickListener(onClickListener);
        rl_peidai.setOnClickListener(onClickListener);
        btn_jiebang.setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_app:
                    startActivity(new Intent(WearFitSettingActivity.this, WearFitRemindActivity.class));
                    break;
                case R.id.rl_peidai:
                    showPeiDaiSelect();
                    break;
                case R.id.btn_jiebang:
                    try {
                        MyApplication.mBluetoothLeService.disconnect();
                    } catch (RemoteException e) {

                    }
            }
        }
    };

    private void showPeiDaiSelect() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("请选择左右手");
        builder.setPositiveButton("左手", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tv_peidai.setText("左手");
            }
        });
        builder.setNegativeButton("右手", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tv_peidai.setText("右手");
            }
        });
        builder.show();
    }
}
