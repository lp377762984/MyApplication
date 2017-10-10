package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.cn.danceland.myapplication.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by feng on 2017/9/28.
 */

public class RegisterInfoActivity extends Activity{

    TextView text_birthday,cancel_action;
    PopupWindow mPopWindow;
    ListView list_year,list_date;
    SimpleAdapter mSchedule;
    View contentView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_info);
        intiView();
        setClick();

    }

    public void intiView(){
        contentView = LayoutInflater.from(RegisterInfoActivity.this).inflate(R.layout.selectorwindowdate,null);
        mPopWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        list_year = contentView.findViewById(R.id.list_year);
        list_date = contentView.findViewById(R.id.list_date);
        cancel_action = contentView.findViewById(R.id.cancel_action);


        text_birthday = findViewById(R.id.text_birthday);

    }
    public void setClick(){
        text_birthday.setOnClickListener(onclick);
        cancel_action.setOnClickListener(onclick);
    }

    View.OnClickListener onclick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int v = view.getId();
            switch(v){
                case R.id.text_birthday:{
                    showSelectorWindow();
                }
                break;
                case R.id.cancel_action:{
                    dismissWindow();
                }
                break;
            }

        }
    };

    public void showSelectorWindow(){

        mPopWindow.setContentView(contentView);
        //显示PopupWindow
        View rootview = LayoutInflater.from(RegisterInfoActivity.this).inflate(R.layout.activity_register_info, null);
        mPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
        mPopWindow.setAnimationStyle(R.style.selectorMenuAnim);

        HashMap map = new HashMap();
        ArrayList mylistyear = new ArrayList();
        map.put("Item",2001);
        mylistyear.add(map);
        map = new HashMap();
        map.put("Item",2002);
        mylistyear.add(map);
        map = new HashMap();
        map.put("Item",2003);
        mylistyear.add(map);
        map = new HashMap();
        map.put("Item",2004);
        mylistyear.add(map);


        //生成适配器，数组===》ListItem
        mSchedule = new SimpleAdapter(this, mylistyear, R.layout.selector_item, new String[] {"Item"}, new int[] {R.id.item_text});
        list_year.setAdapter(mSchedule);
        list_date.setAdapter(mSchedule);
    }

    public void dismissWindow(){
        if(null != mPopWindow && mPopWindow.isShowing()){
            mPopWindow.dismiss();
        }
    }

}
