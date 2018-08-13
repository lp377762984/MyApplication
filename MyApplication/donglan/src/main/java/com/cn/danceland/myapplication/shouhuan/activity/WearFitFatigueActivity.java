package com.cn.danceland.myapplication.shouhuan.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.activity.MainActivity;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.view.HistoGram;

/**
 * 疲劳
 * Created by yxx on 2018/7/18.
 */

public class WearFitFatigueActivity extends Activity {
    private Context context;
    PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wearfit_fatigue);
        context = this;
        final HistoGram histoGram = (HistoGram) findViewById(R.id.staticview);
        String[] data = {"80", "20", "40", "20", "80", "20", "60"};
        final String[] title = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        histoGram.setNum(title.length);
        histoGram.setData(data);
        histoGram.setxTitleString(title);
        histoGram.setOnChartClickListener(new HistoGram.OnChartClickListener() {
            @Override
            public void onClick(int num, float x, float y, float value) {
                //显示提示窗
                View inflate = View.inflate(WearFitFatigueActivity.this, R.layout.popupwindow, null);
                TextView textView = (TextView) inflate.findViewById(R.id.main_tv);
                textView.setText(value + "%\n" + title[num - 1]);
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
                mPopupWindow = new PopupWindow(inflate, 140, 60, true);
                mPopupWindow.setTouchable(true);
                LogUtil.i("num" + num + ";x" + x + ";y" + y + ";value" + value
                        + ";(int)((- histoGram.getHeight()) + y - 65)"
                        + (int) ((-histoGram.getHeight()) + y - 65)
                        + "histoGram.getHeight()" + histoGram.getHeight());
                // 设置好参数之后再show
//        Toast.makeText(MainActivity.this, "num" + num +";x" + x+";y"+ y + ";value" + value
//            +";popupWindow.getWidth()"+ mPopupWindow.getWidth()+";"+ mPopupWindow.getHeight(), Toast.LENGTH_SHORT).show();
                //xoff,yoff基于anchor的左下角进行偏移。window.showAsDropDown(anchor, xoff, yoff);
                mPopupWindow.showAsDropDown(histoGram, (int) (x - 65), (int) ((-histoGram.getHeight()) + y - 65));
                mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_bg_blue));
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        mPopupWindow.dismiss();
                    }
                }, 1000);
            }
        });
    }

}
