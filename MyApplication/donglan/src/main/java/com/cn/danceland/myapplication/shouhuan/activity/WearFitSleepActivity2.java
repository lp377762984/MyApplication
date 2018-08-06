//package com.cn.danceland.myapplication.shouhuan.activity;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.widget.Toast;
//
//import com.cn.danceland.myapplication.R;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
//import lecho.lib.hellocharts.model.Axis;
//import lecho.lib.hellocharts.model.Column;
//import lecho.lib.hellocharts.model.ColumnChartData;
//import lecho.lib.hellocharts.model.SubcolumnValue;
//import lecho.lib.hellocharts.util.ChartUtils;
//import lecho.lib.hellocharts.view.ColumnChartView;
//
///**
// * 睡眠
// * Created by yxx on 2018/7/18.
// *
// *
// *  <lecho.lib.hellocharts.view.ColumnChartView
// android:id="@+id/column_chart"
// android:layout_width="fill_parent"
// android:layout_height="@dimen/line_chart_layout_height_d" />
// */
//
//public class WearFitSleepActivity2 extends Activity {
//    private ColumnChartView columnChart; //柱状图的自定义View
//    private ColumnChartData data;             //存放柱状图数据的对象
//    private boolean hasAxes = true;            //是否有坐标轴
//    private boolean hasAxesNames = true;       //是否有坐标轴的名字
//    private boolean hasLabels = false;          //柱子上是否显示标识文字
//    private boolean hasLabelForSelected = true;    //柱子被点击时，是否显示标识的文字
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_wearfit_sleep);
//        initView();
//        initData();
//        initEvent();
//    }
//    private void initView() {
//        columnChart = (ColumnChartView) findViewById(R.id.column_chart);//折线
//    }
//    private void initData() {
//        generateDefaultData();
//
//    }
//
//    private void initEvent() {
//        columnChart.setOnValueTouchListener(new ValueTouchListener());
//    }
//    /**
//     * 默认显示的数据
//     */
//    private void generateDefaultData() {
//        int numSubcolumns = 1;
//        int numColumns = 8;
//        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
//        List<Column> columns = new ArrayList<Column>();
//        List<SubcolumnValue> values;
//        for (int i = 0; i < numColumns; ++i) {
//
//            values = new ArrayList<SubcolumnValue>();
//            for (int j = 0; j < numSubcolumns; ++j) {
//                values.add(new SubcolumnValue((float) Math.random() * 50f + 5, ChartUtils.pickColor()));
//            }
//
//            Column column = new Column(values);
//            column.setHasLabels(hasLabels);
//            column.setHasLabelsOnlyForSelected(hasLabelForSelected);
//            columns.add(column);
//        }
//
//        data = new ColumnChartData(columns);
//
//        if (hasAxes) {
//            Axis axisX = new Axis();
//            Axis axisY = new Axis().setHasLines(true);
////            if (hasAxesNames) {
////                axisX.setName("Axis X");
////                axisY.setName("Axis Y");
////            }
//            data.setAxisXBottom(axisX);
//            data.setAxisYLeft(axisY);
//        } else {
//            data.setAxisXBottom(null);
//            data.setAxisYLeft(null);
//        }
//
//        columnChart.setColumnChartData(data);
//    }
//
//
//    /**
//     * 图像的监听
//     */
//    private class ValueTouchListener implements ColumnChartOnValueSelectListener {
//
//        @Override
//        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
//            showToast("Selected: " + value);
//        }
//
//        @Override
//        public void onValueDeselected() {
//
//        }
//
//    }
//
//
//    Toast toast;
//
//    public void showToast(String msg) {
//        if (toast == null) {
//            toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
//        } else {
//            toast.setText(msg);
//        }
//        toast.show();
//    }
//}