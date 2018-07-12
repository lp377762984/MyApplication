package com.cn.danceland.myapplication.shouhuan.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.shouhuan.service.BluetoothLeService;
import com.cn.danceland.myapplication.shouhuan.utils.DataHandlerUtils;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.cn.danceland.myapplication.view.DongLanTitleView;

import java.util.List;

/**
 * Created by feng on 2018/6/19.
 */

public class WearFitEquipmentActivity extends Activity {
    private static final int REQUEST_SEARCH = 1;
    private TextView tv_status;
    private String address;
    private String name;
    private Button btn_bind;
    private ProgressDialog progressDialog;
    private DongLanTitleView wearfitequipment_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wearfitequipment);
        initView();
    }

    private void initView() {
        wearfitequipment_title = findViewById(R.id.wearfitequipment_title);
        wearfitequipment_title.setTitle("连接管理");
        progressDialog = new ProgressDialog(this);

        tv_status = findViewById(R.id.tv_status);
        btn_bind = findViewById(R.id.btn_bind);
        if (MyApplication.mBluetoothConnected) {
            tv_status.setText(SPUtils.getString(Constants.NAME, "")+"--"+SPUtils.getString(Constants.ADDRESS, ""));
        } else {
            tv_status.setText("未绑定");
        }
        setListener();
    }

    private void setListener() {

        btn_bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.mBluetoothConnected) {
                    try {
                        MyApplication.mBluetoothLeService.disconnect();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                } else {
                    Intent intent = new Intent(WearFitEquipmentActivity.this, DeviceScanActivity.class);
                    startActivityForResult(intent, REQUEST_SEARCH);
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SEARCH && resultCode == RESULT_OK) {
            address = data.getStringExtra(Constants.ADDRESS);

            name = data.getStringExtra(Constants.NAME);
            SPUtils.setString(Constants.ADDRESS, address);
            SPUtils.setString(Constants.NAME, name);
            if (!TextUtils.isEmpty(address)) {

                try {
                    if(MyApplication.mBluetoothLeService.connect(address)){
                        //tv_status.setText(name+"--"+address);
                        //ToastUtils.showToastShort("正在连接...");
                        progressDialog.setMessage("正在连接...");
                        progressDialog.show();
                    }else{
                        ToastUtils.showToastShort("连接失败");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                MyApplication.isBluetoothConnecting = true;
                invalidateOptionsMenu();//显示正在连接 ...
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
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
                //tv_connect.setText(address);
                tv_status.setText(name);
                btn_bind.setText("解绑手环");
                invalidateOptionsMenu();//更新菜单栏
                ToastUtils.showToastShort("连接成功");
                progressDialog.dismiss();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                MyApplication.mBluetoothConnected = false;
                //todo 更改界面ui
                invalidateOptionsMenu();//更新菜单栏
                try {
                    MyApplication.mBluetoothLeService.close();//断开更彻底(没有这一句，在某些机型，重连会连不上)
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                tv_status.setText("未绑定");
                btn_bind.setText("绑定手环");
                ToastUtils.showToastShort("已解绑");
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
                //LogUtil.i("BluetoothLeService", "接收的数据：" + DataHandlerUtils.bytesToHexStr(txValue));

                List<Integer> datas = DataHandlerUtils.bytesToArrayList(txValue);

                if(datas.get(4) == 0x51 && datas.get(5)==17){
                    LogUtil.i(datas.toString());
                }
                if (datas.get(4) == 0x51 && datas.get(5)==8){
                    LogUtil.i(datas.toString());
                }

            }
        }
    };

}
