package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.view.SwitchButton;

public class MainActivity extends Activity {
    private SwitchButton btn_switchbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initView();
    }

//    private void initView() {
//        btn_switchbutton = (SwitchButton) findViewById(R.id.btn_switchbutton);
//        btn_switchbutton.setHydropowerListener(hydropowerListener);
//        btn_switchbutton.setSoftFloorListener(softFloorListener);
//    }
//
//    SwitchButton.HydropowerListener hydropowerListener = new SwitchButton.HydropowerListener() {
//        @Override
//        public void hydropower() {
//            Toast.makeText(MainActivity.this, "关", Toast.LENGTH_SHORT).show();
//
//            startActivity(new Intent(MainActivity.this,LoginSMSActivity.class));
//
//        }
//    };
//    SwitchButton.SoftFloorListener softFloorListener = new SwitchButton.SoftFloorListener() {
//        @Override
//        public void softFloor() {
//            Toast.makeText(MainActivity.this, "开", Toast.LENGTH_SHORT).show();
//
//        }
//
//    };
}