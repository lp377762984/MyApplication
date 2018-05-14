package com.cn.danceland.myapplication.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.bean.UpdateBean;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by feng on 2018/5/12.
 */

public class ForceUpdateUtil {

    String result;
    Context context;

    public ForceUpdateUtil(String result, Context context){
        this.result = result;
        this.context = context;
    }

    public void paseResult() {

        Gson gson = new Gson();
        UpdateBean updateBean = gson.fromJson(result, UpdateBean.class);
        if(updateBean!=null && updateBean.getData()!=null){
            showDialog(updateBean.getData().getUrl());
        }else {
            return;
        }


    }

    private void showDialog(final String url){

        AlertDialog.Builder builder = new AlertDialog.Builder(MyApplication.getCurrentActivity());
        builder.setMessage("您的应用版本过低，系统将为您强制升级");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri;
                if(HttpUtils.IsUrl(url)){
                    uri = Uri.parse(url);
                }else{
                    uri = Uri.parse("https://www.baidu.com/");
                }
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


        builder.setCancelable(false);

        builder.show();
    }

}
