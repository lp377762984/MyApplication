package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.cn.danceland.myapplication.R;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by feng on 2017/9/28.
 */

public class RegisterInfoActivity extends Activity{

    String strName,strSex,strBirthday,strHeight,strWeight ;
    TextView text_birthday,cancel_action,text_height,button,text_name,text_male,text_female,over,text_weight,
            selecttitle;
    PopupWindow mPopWindow;
    ListView list_year,list_date,list_height;
    SimpleAdapter mSchedule;
    MyAdapter arrayAdapter;
    View contentView;
    private final static int DATE_DIALOG = 0;
    private Calendar c = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_info);
        intiView();
        setClick();

    }

    public void intiView(){
        contentView = LayoutInflater.from(RegisterInfoActivity.this).inflate(R.layout.selectorwindowsingle,null);
        mPopWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
//        list_year = contentView.findViewById(R.id.list_year);
//        list_date = contentView.findViewById(R.id.list_date);
        list_height = contentView.findViewById(R.id.list_height);
        cancel_action = contentView.findViewById(R.id.cancel_action);
        over = contentView.findViewById(R.id.over);
        selecttitle = contentView.findViewById(R.id.selecttitle);
        button = findViewById(R.id.button);
        text_birthday = findViewById(R.id.text_birthday);
        text_height = findViewById(R.id.text_height);
        text_weight = findViewById(R.id.text_weight);

        text_male = findViewById(R.id.text_male);
        text_female = findViewById(R.id.text_female);

        text_name = findViewById(R.id.text_name);
    }
    public void setClick(){
        text_birthday.setOnClickListener(onclick);
        cancel_action.setOnClickListener(onclick);
        text_height.setOnClickListener(onclick);
        text_name.setOnClickListener(onclick);
        text_male.setOnClickListener(onclick);
        text_female.setOnClickListener(onclick);
        over.setOnClickListener(onclick);
        text_weight.setOnClickListener(onclick);
    }

    View.OnClickListener onclick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int v = view.getId();
            switch(v){
                case R.id.text_birthday:{
                    c = Calendar.getInstance();
                    new DatePickerDialog(RegisterInfoActivity.this,new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {
                            //et.setText("您选择了：" + year + "年" + (month+1) + "月" + dayOfMonth + "日");
                            text_birthday.setText(year+"年"+(month+1)+"月"+dayOfMonth+"日");
                            strBirthday = year+"-"+(month+1)+"-"+dayOfMonth;
                        }
                    }, c.get(Calendar.YEAR), // 传入年份
                            c.get(Calendar.MONTH), // 传入月份
                            c.get(Calendar.DAY_OF_MONTH) // 传入天数
                    ).show();
                    //showSelectorWindow();
                }
                break;
                case R.id.text_height:
                    showSelectorWindow(0);
                    selecttitle.setText("选择身高");
                    break;
                case R.id.text_weight:
                    showSelectorWindow(1);
                    selecttitle.setText("选择体重");
                    break;
                case R.id.text_name:
                    text_name.setText("");
                    showName();
                    break;
                case R.id.cancel_action:{
                    dismissWindow();
                }
                break;
                case R.id.over:
                    dismissWindow();
                    break;
                case R.id.text_male:
                    text_male.setBackgroundResource(R.drawable.male_blue);
                    text_female.setBackgroundResource(R.drawable.female_gray);
                    strSex = "男";
                    break;
                case R.id.text_female:
                    text_male.setBackgroundResource(R.drawable.male_gray);
                    text_female.setBackgroundResource(R.drawable.female_blue);
                    strSex = "女";
                    break;
            }

        }
    };

    public void showName(){

        AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(RegisterInfoActivity.this);
        View dialogView = LayoutInflater.from(RegisterInfoActivity.this)
                .inflate(R.layout.edit_name,null);
        //normalDialog.setTitle("编辑昵称");
        final TextView edit_name = dialogView.findViewById(R.id.edit_name);
        normalDialog.setView(dialogView);
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str =  edit_name.getText().toString();
                        text_name.setText(str);
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        // 显示
        normalDialog.show();

    }

    public void showSelectorWindow(int x){
        final int j = x;
        mPopWindow.setContentView(contentView);
        //显示PopupWindow
        //View rootview = LayoutInflater.from(RegisterInfoActivity.this).inflate(R.layout.activity_register_info, null);
        String[] str  = new String[71];
        Integer[] str1 = new Integer[165];
        final ArrayList<String> arHeight = new ArrayList<String>();
        int n;
        if(j==0){
            for(int i = 0;i<71;i++){
                n = 150+i;
                str[i] = n+"";
            }
            Arrays.sort(str);
            for(int z = 0;z<str.length;z++){
                arHeight.add(str[z]);
            }
        }else {
            for(int y=0;y<165;y++){
                n = 35+y;
                str1[y] = n;
            }
            Arrays.sort(str1);
            for(int z = 0;z<str1.length;z++){
                arHeight.add(str1[z]+"");
            }
        }

        arrayAdapter = new MyAdapter(arHeight,this);
        list_height.setAdapter(arrayAdapter);

        mPopWindow.showAsDropDown(button,0,40);
        mPopWindow.setAnimationStyle(R.style.selectorMenuAnim);

        list_height.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(j==0){
                    text_height.setText(arHeight.get(i)+" cm");
                    strHeight = arHeight.get(i);
                }else{
                    text_weight.setText(arHeight.get(i)+" kg");
                    strWeight = arHeight.get(i);
                }
            }
        });

    }

    public void dismissWindow(){
        if(null != mPopWindow && mPopWindow.isShowing()){
            mPopWindow.dismiss();
        }
    }

    public class MyAdapter extends BaseAdapter{

        ArrayList<String> arrayList;
        LayoutInflater inflater = null;
        public MyAdapter(ArrayList<String> list, Context context){
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
}
