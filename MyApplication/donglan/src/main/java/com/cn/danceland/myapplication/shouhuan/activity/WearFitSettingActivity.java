package com.cn.danceland.myapplication.shouhuan.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.shouhuan.command.CommandManager;
import com.cn.danceland.myapplication.shouhuan.service.BluetoothLeService;
import com.cn.danceland.myapplication.shouhuan.utils.DataHandlerUtils;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.StringUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.cn.danceland.myapplication.view.CustomDatePicker;
import com.cn.danceland.myapplication.view.DongLanTitleView;

import java.util.List;

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
    private CommandManager commandManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wearfitsetting);
//注册广播
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mGattUpdateReceiver, makeGattUpdateIntentFilter());
        initHost();
        initView();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
//    }
//    @Override
//    protected void onPause() {
//        super.onPause();
//        unregisterReceiver(mGattUpdateReceiver);
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mGattUpdateReceiver);
    }

    private void initHost() {
        address = SPUtils.getString(Constants.ADDRESS, "");
        name = SPUtils.getString(Constants.NAME, "");
        commandManager = CommandManager.getInstance(getApplicationContext());
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

        if (!MyApplication.mBluetoothConnected) {
            tv_shouhuan_name.setText("未绑定");
            btn_jiebang.setText("去绑定");
        }else{
            tv_shouhuan_name.setText(name);
            tv_shouhuan_num.setText(address);
            tv_status.setText("已绑定");
            btn_jiebang.setText("解绑");
        }

    }

    private void setClickListener() {

        rl_app.setOnClickListener(onClickListener);
        rl_peidai.setOnClickListener(onClickListener);
        btn_jiebang.setOnClickListener(onClickListener);
        rl_naozhong.setOnClickListener(onClickListener);
        rl_rushui.setOnClickListener(onClickListener);
        rl_wake.setOnClickListener(onClickListener);
        rl_jiuzuo.setOnClickListener(onClickListener);
        rl_wurao.setOnClickListener(onClickListener);
        //抬手亮屏开关
        sw_taishou.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.i("yxx","抬手亮屏开关"+MyApplication.mBluetoothConnected+MyApplication.mBluetoothConnected+";b="+b);
                if(MyApplication.mBluetoothConnected){
                    if(b){
                        commandManager.setUpHandLightScreen(1);
                    }else {
                        commandManager.setUpHandLightScreen(0);
                    }
                }
            }
        });
        //防丢开关
        sw_fangdiu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    commandManager.setAntiLostAlert(1);
                }else {
                    commandManager.setAntiLostAlert(0);
                }
            }
        });
        //来电提醒
        sw_laidian.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    commandManager.setSmartWarnNoContent(1,1);
                }else {
                    commandManager.setSmartWarnNoContent(1,0);
                }
            }
        });
        //来短信提醒
        sw_duanxin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    commandManager.setSmartWarnNoContent(3,1);
                }else {
                    commandManager.setSmartWarnNoContent(3,0);
                }
            }
        });

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
                    if(MyApplication.mBluetoothConnected){
                        try {
                            MyApplication.mBluetoothLeService.disconnect();
                            MyApplication.mBluetoothConnected=false;//更改解绑连接状态 yxx

                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }else{
                        startActivityForResult(new Intent(WearFitSettingActivity.this,WearFitEquipmentActivity.class),2);
                    }
                    setData();//更改解绑连接状态 yxx



//                    if(MyApplication.mBluetoothConnected){
//                        try {
//                            MyApplication.mBluetoothLeService.disconnect();
//                        } catch (RemoteException e) {
//
//                        }
//                    }else{
//                        startActivityForResult(new Intent(WearFitSettingActivity.this,WearFitEquipmentActivity.class),2);
//                    }
                    break;
                case R.id.rl_naozhong:
                    startActivity(new Intent(WearFitSettingActivity.this,AddClockActivity.class));
                    break;
                case R.id.rl_rushui:
                    showTimeSelect("入睡时间");
                    break;
                case R.id.rl_wake:
                    showTimeSelect("醒来时间");
                    break;
                case R.id.rl_jiuzuo:
                    startActivity(new Intent(WearFitSettingActivity.this,LongSitActivity.class).putExtra("from","久坐提醒"));
                    break;
                case R.id.rl_wurao:
                    startActivity(new Intent(WearFitSettingActivity.this,LongSitActivity.class).putExtra("from","勿扰模式"));
                    break;
            }
        }
    };

    private void showTimeSelect(final String str) {
        final CustomDatePicker customDatePicker = new CustomDatePicker(this, str);
        customDatePicker.setGoneYearAndMounth();
        customDatePicker.showWindow();
        customDatePicker.setDialogOnClickListener(new CustomDatePicker.OnClickEnter() {
            @Override
            public void onClick() {
                String dateString = customDatePicker.getTime();
                if("入睡时间".equals(str)){
                    tv_rushui.setText(dateString);
                }else if ("醒来时间".equals(str)){
                    tv_wake.setText(dateString);
                }

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2){
            if(MyApplication.mBluetoothConnected){
                String name = SPUtils.getString(Constants.NAME, "");
                String address = SPUtils.getString(Constants.ADDRESS,"");
                tv_shouhuan_name.setText(name);
                tv_shouhuan_num.setText(address);
                tv_status.setText("已绑定");
                btn_jiebang.setText("解绑");
            }else{
                tv_shouhuan_name.setText("已断开");
                tv_shouhuan_num.setText("");
                tv_status.setText("");
                btn_jiebang.setText("去绑定");
            }
        }
    }

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


    private IntentFilter makeGattUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    //接收蓝牙状态改变的广播
    private BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                MyApplication.mBluetoothConnected = true;
                MyApplication.isBluetoothConnecting = false;
                //todo 更改界面ui
                ToastUtils.showToastShort("连接成功");

            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
//                MyApplication.mBluetoothConnected = false;
                //todo 更改界面ui
                try {
                    MyApplication.mBluetoothLeService.close();//断开更彻底(没有这一句，在某些机型，重连会连不上)
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                tv_shouhuan_name.setText("已断开");
                tv_shouhuan_num.setText("");
                tv_status.setText("");
                btn_jiebang.setText("去绑定");
                ToastUtils.showToastShort("已断开");
                SPUtils.setString(Constants.ADDRESS,"");
                SPUtils.setString(Constants.NAME,"");
                //LogUtil.d("BluetoothLeService", "断开");
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
//                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
//                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
//                Log.i("zgy", "接收到的数据：" + intent.getStringExtra(BluetoothLeService.EXTRA_DATA));

                final byte[] txValue = intent
                        .getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
                LogUtil.i("接收的数据：" + DataHandlerUtils.bytesToHexStr(txValue));

                List<Integer> datas = DataHandlerUtils.bytesToArrayList(txValue);

                if(datas.get(4) == 0x51 && datas.get(5)==17){
                    LogUtil.i(datas.toString());
                }
                if (datas.get(4) == 0x51 && datas.get(5)==8){
                    LogUtil.i(datas.toString());
                }
                if (datas.get(4) == 0x77 ){
                    LogUtil.i("抬手亮屏");
                }

            }
        }
    };
}
