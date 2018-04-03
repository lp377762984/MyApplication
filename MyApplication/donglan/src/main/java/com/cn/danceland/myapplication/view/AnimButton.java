package com.cn.danceland.myapplication.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by shy on 2018/3/28 11:07
 * Email:644563767@qq.com
 */


public class AnimButton extends android.support.v7.widget.AppCompatImageView implements View.OnClickListener{

    public AnimButton(Context context) {
        this(context,null);
    }

    public AnimButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    @Override
    public void onClick(View view) {

    }
}
