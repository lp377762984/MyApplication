package com.cn.danceland.myapplication.shouhuan.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.db.HeartRate;
import com.cn.danceland.myapplication.db.WearFitSleepBean;
import com.cn.danceland.myapplication.db.WearFitStepBean;
import com.cn.danceland.myapplication.db.WearFitStepHelper;
import com.cn.danceland.myapplication.shouhuan.bean.HeartRatePostBean;
import com.cn.danceland.myapplication.shouhuan.bean.MorePostBean;
import com.cn.danceland.myapplication.shouhuan.bean.StepListBean;
import com.cn.danceland.myapplication.shouhuan.bean.StepResultBean;
import com.cn.danceland.myapplication.shouhuan.bean.WearFitUser;
import com.cn.danceland.myapplication.shouhuan.command.CommandManager;
import com.cn.danceland.myapplication.shouhuan.service.BluetoothLeService;
import com.cn.danceland.myapplication.shouhuan.utils.DataHandlerUtils;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.StringUtils;
import com.cn.danceland.myapplication.utils.TimeUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.cn.danceland.myapplication.view.DongLanTitleView;
import com.cn.danceland.myapplication.view.HorizontalPickerView;
import com.cn.danceland.myapplication.view.StepArcView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * 疲劳  单次测量
 * Created by yxx on 2018/7/18.
 */
public class WearFitFatigueMeasureActivity extends Activity {
    private Context context;
    private DongLanTitleView heart_title;//数据title
    private StepArcView arc_measure;
    private Button btn_measure;
    private TextView measure_tv;

    private CommandManager commandManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wearfit_fatigue_measure);
        context = this;
        commandManager = CommandManager.getInstance(this);
        initView();
    }

    private void initView() {
        heart_title = findViewById(R.id.shouhuan_title);
        heart_title.setTitle(context.getResources().getString(R.string.fatigue_measure_text));
        arc_measure = findViewById(R.id.arc_measure);
        btn_measure = findViewById(R.id.btn_measure);
        measure_tv = findViewById(R.id.measure_tv);
        btn_measure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyApplication.mBluetoothConnected) {
                    Toast.makeText(context, "请先连接手环", Toast.LENGTH_SHORT).show();
                } else {
                    commandManager.sendStep();//首页数据
                    arc_measure.setCurrentCount(1000, 1000);
                }
            }
        });
    }

    //接收蓝牙状态改变的广播
    private BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                final byte[] txValue = intent
                        .getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
//                LogUtil.i("接收的数据：" + DataHandlerUtils.bytesToHexStr(txValue));
                List<Integer> datas = DataHandlerUtils.bytesToArrayList(txValue);
                if (datas.get(4) == 0x51 && datas.size() == 17) {//首页数据
                    LogUtil.i(datas.toString());
                    int step = (datas.get(6) << 16) + (datas.get(7) << 8) + datas.get(8);//计步
                    Calendar c = Calendar.getInstance();
                    int hour = c.get(Calendar.HOUR_OF_DAY);
                    int fatigue = 0;//疲劳
                    if (hour >= 6 && hour < 11) {
                        fatigue = 0;
                    } else if (hour >= 11 && hour < 18) {
                        fatigue = 10;
                    } else if (hour >= 18 && hour < 24) {
                        fatigue = 20;
                    } else {
                        fatigue = 30;
                    }
                    fatigue = (int) (fatigue + Math.sqrt(step) / 2);
                    measure_tv.setText(fatigue + "");
                }
            }
        }
    };

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
}
