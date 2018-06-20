package com.cn.danceland.myapplication.shouhuan.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.db.HeartRate;
import com.cn.danceland.myapplication.shouhuan.service.BluetoothLeService;
import com.cn.danceland.myapplication.shouhuan.utils.DataHandlerUtils;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.TimeUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;

import java.util.List;

/**
 * Created by feng on 2018/6/19.
 */

public class WearFitActivity extends Activity {
    private static final int REQUEST_SEARCH = 1;
    private TextView tv_connect;
    private String address;
    private String name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wearfit);
        initView();
    }

    private void initView() {

        tv_connect = findViewById(R.id.tv_connect);
        if (MyApplication.mBluetoothConnected) {
            tv_connect.setText("断开");
//            device_address.setText(SPUtils.getString(Constants.ADDRESS, ""));
//            device_name.setText(SPUtils.getString(Constants.NAME, ""));
        } else {
            tv_connect.setText("搜索");
        }
        setListener();
    }

    private void setListener() {

        tv_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.mBluetoothConnected) {
                    MyApplication.mBluetoothLeService.disconnect();
                    tv_connect.setText("搜索");
                } else {
                    Intent intent = new Intent(WearFitActivity.this, WearFitEquipmentActivity.class);
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
//            SPUtils.setString(Constants.ADDRESS, address);
//            SPUtils.setString(Constants.NAME, name);
            if (!TextUtils.isEmpty(address)) {
                if(MyApplication.mBluetoothLeService.connect(address)){
                    tv_connect.setText(address);
                    ToastUtils.showToastShort("正在连接...");
                }else{
                    ToastUtils.showToastShort("连接失败");
                }

                MyApplication.isBluetoothConnecting = true;
                invalidateOptionsMenu();//显示正在连接 ...
            }

        }
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
                tv_connect.setText(name);
                invalidateOptionsMenu();//更新菜单栏
                ToastUtils.showToastShort("连接成功");

            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                MyApplication.mBluetoothConnected = false;
                //todo 更改界面ui
//                device_address.setText("未连接");
//
//                device_name.setText("");
                invalidateOptionsMenu();//更新菜单栏
                MyApplication.mBluetoothLeService.close();//断开更彻底(没有这一句，在某些机型，重连会连不上)
                ToastUtils.showToastShort("断开");
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
