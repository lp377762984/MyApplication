package com.cn.danceland.myapplication.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.PxUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by feng on 2018/6/13.
 */

public class CustomLine extends View {

    private int width, height;
    private float padding;//每条线的间距10dp
    private int top;//起始y
    private Paint paintLine;
    private Context context;
    private int startX;
    private List<Integer> positionList, statusList, roleList;
    private Paint paintRectSure, paintRectNoSure, paintRectCancel;

    public CustomLine(Context context) {
        super(context);
        this.context = context;
        initPaint();
    }
    public CustomLine(Context context,List<Integer> positionList,List<Integer> statusList,List<Integer> roleList) {
        super(context);
        this.context = context;
        this.positionList = positionList;
        this.statusList = statusList;
        this.roleList = roleList;
        initPaint();
    }

    public CustomLine(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initPaint();
    }

    public CustomLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initPaint();
    }


    private void initPaint() {
        //设置画笔基本属性
        paintLine = new Paint();
        paintLine.setAntiAlias(true);//抗锯齿功能
        paintLine.setColor(Color.GRAY);  //设置画笔颜色
        paintLine.setStyle(Paint.Style.FILL);//设置填充样式   Style.FILL/Style.FILL_AND_STROKE/Style.STROKE
        paintLine.setStrokeWidth(1);//设置画笔宽度

        paintRectSure = new Paint();//已确认颜色的画笔
        paintRectSure.setAntiAlias(true);
        paintRectSure.setColor(getResources().getColor(R.color.color_dl_yellow));
        paintRectSure.setStyle(Paint.Style.FILL);

        paintRectNoSure = new Paint();//等待确认颜色的画笔
        paintRectNoSure.setAntiAlias(true);
        paintRectNoSure.setColor(Color.parseColor("#87CEFA"));
        paintRectNoSure.setStyle(Paint.Style.FILL);

        paintRectCancel = new Paint();//已取消和已签到颜色的画笔
        paintRectCancel.setAntiAlias(true);
        paintRectCancel.setColor(Color.parseColor("#A9A9A9"));
        paintRectCancel.setStyle(Paint.Style.FILL);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(1800,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, PxUtils.dp2px(context, (float) 12.5), r, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        LogUtil.i("onDraw");
        for (int i = 0; i < 84; i++) {
            if(positionList!=null&&statusList!=null&&roleList!=null){
                if (positionList.get(i) != 999) {
                    if (statusList.get(i) == 1) {
                        canvas.drawRect(0, PxUtils.dp2px(context, padding), width, PxUtils.dp2px(context, padding) + PxUtils.dp2px(context, 10), paintRectSure);
                    } else if (statusList.get(i) == 2) {
                        canvas.drawRect(0, PxUtils.dp2px(context, padding), width, PxUtils.dp2px(context, padding) + PxUtils.dp2px(context, 10), paintRectNoSure);
                    } else if (statusList.get(i) == 3) {
                        canvas.drawRect(0, PxUtils.dp2px(context, padding), width, PxUtils.dp2px(context, padding) + PxUtils.dp2px(context, 10), paintRectCancel);
                    } else if (statusList.get(i) == 4) {
                        canvas.drawRect(0, PxUtils.dp2px(context, padding), width, PxUtils.dp2px(context, padding) + PxUtils.dp2px(context, 10), paintRectCancel);
                    }
                }
            }

            if (i % 6 == 0) {
                canvas.drawLine(0, PxUtils.dp2px(context, padding), width, PxUtils.dp2px(context, padding), paintLine);
            } else {
                startX = 0;
                for (int j = 0; j < 31; j++) {
                    canvas.drawLine(startX, PxUtils.dp2px(context, padding), startX + (width / 60), PxUtils.dp2px(context, padding), paintLine);
                    startX = startX + (width / 60) + (width / 60);
                }
            }
            padding = padding + 10;
        }
        canvas.drawLine(0, PxUtils.dp2px(context, padding), width, PxUtils.dp2px(context, padding), paintLine);

    }
}
