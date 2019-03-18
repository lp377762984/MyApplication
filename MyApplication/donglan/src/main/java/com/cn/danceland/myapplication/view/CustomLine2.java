package com.cn.danceland.myapplication.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.SiJiaoYuYueBean;
import com.cn.danceland.myapplication.utils.DensityUtils;

import java.util.List;

public class CustomLine2 extends View {
    private List<SiJiaoYuYueBean.Data> data;
    private Paint linePaint;
    private Paint recPaint;
    private Paint textPaint;
    private int totalHours = 6 * 14;
    private int perLineHeight;
    private int lineWidth;

    public CustomLine2(Context context, List<SiJiaoYuYueBean.Data> data) {
        super(context);
        this.data = data;
        initPaint(context);
    }

    public CustomLine2(Context context, AttributeSet attrs, List<SiJiaoYuYueBean.Data> data) {
        super(context, attrs);
        this.data = data;
        initPaint(context);
    }

    public CustomLine2(Context context, AttributeSet attrs, int defStyleAttr, List<SiJiaoYuYueBean.Data> data) {
        super(context, attrs, defStyleAttr);
        this.data = data;
        initPaint(context);
    }

    private void initPaint(Context context) {
        perLineHeight = PxUtils.dip2px(context, 10);
        lineWidth = PxUtils.dip2px(context, 150);
        //画线
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.GRAY);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setStrokeWidth(PxUtils.dip2px(context, 0.5f));
        //画矩形
        recPaint = new Paint();
        recPaint.setAntiAlias(true);
        recPaint.setColor(getResources().getColor(R.color.color_dl_deep_blue));
        recPaint.setStyle(Paint.Style.FILL);
        //画字
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(getResources().getColor(R.color.black));
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(PxUtils.sp2px(context, 8));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画线
        int startX = PxUtils.sp2px(getContext(), 45);
        int startY = 0;
        for (int i = 0; i < totalHours; i++) {
            int x = 480 + i * 10;//480为8点
            String showTime = x / 60 + ":" + x % 60;
            canvas.drawText(showTime, 50, (float) (startY + ((i + 0.4) * perLineHeight)), textPaint);
            canvas.drawLine(startX, startY + (i * perLineHeight),
                    startX + lineWidth, startY + (i * perLineHeight), linePaint);
        }
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                SiJiaoYuYueBean.Data data = this.data.get(i);
                int status = data.getOcc_obj_status();
                if (status != 0) {
                    int startTime = data.getStart_time();
                    int endTime = data.getEnd_time();
                    int countStart = (startTime - 480) / 10;
                    int countEnd = (endTime - 480) / 10;
                    int left = startX;
                    int top = startY + (countStart * perLineHeight);
                    int right = startX + lineWidth;
                    int bottom = startY + (countEnd * perLineHeight);
                    canvas.drawRect(new Rect(left, top, right, bottom), recPaint);
                }
            }
        }
    }
}
