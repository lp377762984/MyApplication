package com.cn.danceland.myapplication.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * Created by feng on 2018/1/12.
 */

public class NestedExpandaleListView extends ExpandableListView {
    public NestedExpandaleListView(Context context, AttributeSet attrs){
        super(context,attrs);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,

                MeasureSpec.AT_MOST);

        //将重新计算的高度传递回去
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
