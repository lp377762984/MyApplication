package com.cn.danceland.myapplication.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.danceland.myapplication.R;

import java.util.zip.Inflater;

/**
 * Created by feng on 2018/4/4.
 */

public class DongLanTitleView extends RelativeLayout {

    ImageView donglan_back;
    TextView donglan_title,donglan_right_tv;

    public DongLanTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View inflate = LayoutInflater.from(context).inflate(R.layout.donglantitle, this);

        donglan_back = inflate.findViewById(R.id.donglan_back);
        donglan_title = inflate.findViewById(R.id.donglan_title);
        donglan_right_tv = inflate.findViewById(R.id.donglan_right_tv);
        donglan_right_tv.setTextColor(getResources().getColor(R.color.color_dl_yellow));

        donglan_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) getContext();
                activity.finish();
            }
        });

    }

    public TextView getRightTv(){
        return donglan_right_tv;
    }

    public void setTitle(String s){
        donglan_title.setText(s);
    }

}
