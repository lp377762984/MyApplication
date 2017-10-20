package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.SPUtils;

import java.util.ArrayList;

public class SettingActivity extends Activity implements View.OnClickListener {

    View locationView;
    TextView cancel_action,over_action;
    PopupWindow locationWindow;
    ListView list_province,list_city;
    LocationAdapter proAdapter,cityAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_quit).setOnClickListener(this);
        findViewById(R.id.ll_setting).setOnClickListener(this);
        findViewById(R.id.ll_setting_phone).setOnClickListener(this);
        findViewById(R.id.ll_setting_location).setOnClickListener(this);
        findViewById(R.id.ll_about_us).setOnClickListener(this);
        findViewById(R.id.ll_clear).setOnClickListener(this);

        locationView = LayoutInflater.from(SettingActivity.this).inflate(R.layout.selectorwindowlocation,null);
        locationWindow = new PopupWindow(locationView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        locationWindow.setOutsideTouchable(true);
        locationWindow.setBackgroundDrawable(new BitmapDrawable());
        cancel_action = locationView.findViewById(R.id.cancel_action);
        over_action = locationView.findViewById(R.id.over_action);
        list_province = locationView.findViewById(R.id.list_province);
        list_city = locationView.findViewById(R.id.list_city);
        cancel_action.setOnClickListener(this);
        over_action.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back://返回
                finish();
                break;
            case R.id.tv_quit://退出
                startActivity(new Intent(this, LoginActivity.class));
                SPUtils.setBoolean(Constants.ISLOGINED,false);
                //退出主页面
                HomeActivity.instance.finish();
                finish();
                break;
            case R.id.ll_setting://设置会员
                showInputDialog();

                break;
            case R.id.ll_setting_phone://设置手机号
                showSettingPhoneDialog();
                break;
            case R.id.ll_setting_location://设置位置
                showLocation();
                break;
            case R.id.ll_about_us://关于我们

                break;
            case R.id.ll_clear://清除缓存
                Toast.makeText(this, "已清除缓存！", Toast.LENGTH_SHORT).show();
                showClearDialog();
                break;
            case R.id.cancel_action:
                dismissWindow();
                break;
            case R.id.over_action:
                dismissWindow();
                break;
            default:
                break;
        }
    }

    /**
     * 设置手机号
     */
    private void showSettingPhoneDialog() {
        AlertDialog.Builder dialog =
                new AlertDialog.Builder(this);
        dialog.setTitle("提示");
        dialog.setMessage("是否重新绑定手机号");
        dialog.setPositiveButton("确认",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                startActivity(new  Intent(SettingActivity.this,ConfirmPasswordActivity.class));
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }


    /**
     * 清除缓存
     */
    private void showClearDialog() {
        AlertDialog.Builder dialog =
                new AlertDialog.Builder(this);
        dialog.setTitle("提示");
        dialog.setMessage("清除缓存成功");
        dialog.setPositiveButton("确认",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }

    /**
     * 设置会员号
     */
    private void showInputDialog() {

        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(this);
        final View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_input_phone, null);
        inputDialog.setTitle("设置会员号");
        inputDialog.setView(dialogView);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 获取EditView中的输入内容
                        EditText edit_phone =
                                dialogView.findViewById(R.id.edit_phone);
                        Toast.makeText(SettingActivity.this,
                                edit_phone.getText().toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
        inputDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        inputDialog.show();
    }


    public  void showLocation(){

        ArrayList<String> proList = new ArrayList<String>();
        proList.add("河南");
        proList.add("河北");
        proList.add("山西");
        proList.add("山西");
        proList.add("山西");
        proList.add("山西");
        proList.add("山西");
        proList.add("山西");
        ArrayList<String> cityList = new ArrayList<String>();
        cityList.add("郑州");
        cityList.add("武汉");
        cityList.add("合肥");
        cityList.add("合肥");
        cityList.add("合肥");
        cityList.add("合肥");
        cityList.add("合肥");
        cityList.add("合肥");

        locationWindow.setContentView(locationView);
        proAdapter = new LocationAdapter(proList,this);
        cityAdapter = new LocationAdapter(cityList,this);
        list_province.setAdapter(proAdapter);
        list_city.setAdapter(cityAdapter);


        locationWindow.showAsDropDown(findViewById(R.id.tv_quit),0,40);
        locationWindow.setAnimationStyle(R.style.selectorMenuAnim);

    }

    public class LocationAdapter extends BaseAdapter{

        ArrayList<String> arrayList;
        LayoutInflater inflater = null;
        public LocationAdapter(ArrayList<String> list, Context context){
            arrayList = list;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TextView item_text = null;
            if(view==null){
                view  = inflater.inflate(R.layout.selector_item,null);
            }
            item_text = view.findViewById(R.id.item_text);
            item_text.setText(arrayList.get(i));
            return view;
        }
    }

    public void dismissWindow(){
        if(null != locationWindow && locationWindow.isShowing()){
            locationWindow.dismiss();
        }
    }

}
