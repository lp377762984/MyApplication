package com.cn.danceland.myapplication.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.TimeTableResultBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.CustomGridView;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.MyJsonObjectRequest;
import com.cn.danceland.myapplication.utils.MyListView;
import com.cn.danceland.myapplication.utils.TimeUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by feng on 2018/6/6.
 */

public class TimeTableActivity extends BaseActivity {


    private MyListView lv_timetable;
    private Gson gson;
    private ArrayList<String> placeList;
    private HashSet<String> placeSet;
    private ArrayList<List<TimeTableResultBean.DataBean>> listViewList;
    private int num1, num2, num3, num4, num5, num6, num7, hangshu;
    private ArrayList<ArrayList<TimeTableResultBean.DataBean>> allGridViewList;
    private ScrollView wholeView;
    private Button btn_img;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        initHost();
        initView();
        initData();
    }

    private void initHost() {
        gson = new Gson();
        placeList = new ArrayList<>();
        placeSet = new HashSet<>();
        listViewList = new ArrayList<>();
        allGridViewList = new ArrayList<>();
    }

    class StrBean{

     public  String   date_gt;
     public  String   date_lt;

    }

    private void initData() {
//        TimeTableConBean timeTableConBean = new TimeTableConBean();
//        timeTableConBean.setStart_date(TimeUtils.date2TimeStamp(TimeUtils.getWeekStartTime(), "yyyy-MM-dd HH:mm:ss") + "");
//        timeTableConBean.setEnd_date(TimeUtils.date2TimeStamp(TimeUtils.getWeekEndTime(), "yyyy-MM-dd HH:mm:ss") + "");
        StrBean strBean=new StrBean();
        strBean.date_gt=TimeUtils.date2TimeStamp(TimeUtils.getWeekStartTime(), "yyyy-MM-dd HH:mm:ss") + "";
        strBean.date_lt=TimeUtils.date2TimeStamp(TimeUtils.getWeekEndTime(), "yyyy-MM-dd HH:mm:ss") + "";
        String s = gson.toJson(strBean);

        MyJsonObjectRequest jsonObjectRequest = new MyJsonObjectRequest(Request.Method.POST, Constants.TIMETABLES, s, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtil.i(jsonObject.toString());
                TimeTableResultBean timeTableResultBean = gson.fromJson(jsonObject.toString(), TimeTableResultBean.class);
                if (timeTableResultBean != null) {
                    List<TimeTableResultBean.DataBean> data = timeTableResultBean.getData();
                    if (data != null) {
                        for (int i = 0; i < data.size(); i++) {
                            placeList.add(data.get(i).getRoom_name());
                        }
                        placeSet.addAll(placeList);
                        String[] placeArr = placeSet.toArray(new String[placeSet.size()]);
                        for (int j = 0; j < placeArr.length; j++) {
                            ArrayList<TimeTableResultBean.DataBean> placeList = new ArrayList<>();
                            for (int i = 0; i < data.size(); i++) {
                                if (placeArr[j].equals(data.get(i).getRoom_name())) {
                                    placeList.add(data.get(i));
                                }
                            }
                            listViewList.add(placeList);
                            for (int n = 0; n < placeList.size(); n++) {
                                if ("1".equals(placeList.get(n).getWeek())) {
                                    num1++;
                                } else if ("2".equals(placeList.get(n).getWeek())) {
                                    num2++;
                                } else if ("3".equals(placeList.get(n).getWeek())) {
                                    num3++;
                                } else if ("4".equals(placeList.get(n).getWeek())) {
                                    num4++;
                                } else if ("5".equals(placeList.get(n).getWeek())) {
                                    num5++;
                                } else if ("6".equals(placeList.get(n).getWeek())) {
                                    num6++;
                                } else if ("0".equals(placeList.get(n).getWeek())) {
                                    num7++;
                                }
                            }
                            hangshu = num1;
                            int[] numarr = {num1, num2, num3, num4, num5, num6, num7};
                            for (int i = 0; i < numarr.length; i++) {
                                if (numarr[i] > hangshu) {
                                    hangshu = numarr[i];
                                }
                            }
                            num1 = 0;
                            num2 = 0;
                            num3 = 0;
                            num4 = 0;
                            num5 = 0;
                            num6 = 0;
                            num7 = 0;
                            TimeTableResultBean.DataBean[] week1 = new TimeTableResultBean.DataBean[hangshu];
                            TimeTableResultBean.DataBean[] week2 = new TimeTableResultBean.DataBean[hangshu];
                            TimeTableResultBean.DataBean[] week3 = new TimeTableResultBean.DataBean[hangshu];
                            TimeTableResultBean.DataBean[] week4 = new TimeTableResultBean.DataBean[hangshu];
                            TimeTableResultBean.DataBean[] week5 = new TimeTableResultBean.DataBean[hangshu];
                            TimeTableResultBean.DataBean[] week6 = new TimeTableResultBean.DataBean[hangshu];
                            TimeTableResultBean.DataBean[] week7 = new TimeTableResultBean.DataBean[hangshu];
                            for (int n = 0; n < placeList.size(); n++) {
                                if ("1".equals(placeList.get(n).getWeek())) {
                                    week1[num1] = placeList.get(n);
                                    num1++;
                                } else if ("2".equals(placeList.get(n).getWeek())) {
                                    week2[num2] = placeList.get(n);
                                    num2++;
                                } else if ("3".equals(placeList.get(n).getWeek())) {
                                    week3[num3] = placeList.get(n);
                                    num3++;
                                } else if ("4".equals(placeList.get(n).getWeek())) {
                                    week4[num4] = placeList.get(n);
                                    num4++;
                                } else if ("5".equals(placeList.get(n).getWeek())) {
                                    week5[num5] = placeList.get(n);
                                    num5++;
                                } else if ("6".equals(placeList.get(n).getWeek())) {
                                    week6[num6] = placeList.get(n);
                                    num6++;
                                } else if ("0".equals(placeList.get(n).getWeek())) {
                                    week7[num7] = placeList.get(n);
                                    num7++;
                                }
                            }
                            num1 = 0;
                            num2 = 0;
                            num3 = 0;
                            num4 = 0;
                            num5 = 0;
                            num6 = 0;
                            num7 = 0;
                            ArrayList<TimeTableResultBean.DataBean> gridviewItemList = new ArrayList<>();
                            for (int i = 0; i < hangshu; i++) {
                                gridviewItemList.add(week1[i]);
                                gridviewItemList.add(week2[i]);
                                gridviewItemList.add(week3[i]);
                                gridviewItemList.add(week4[i]);
                                gridviewItemList.add(week5[i]);
                                gridviewItemList.add(week6[i]);
                                gridviewItemList.add(week7[i]);
                            }
                            allGridViewList.add(gridviewItemList);
                        }
                        TimeTableAdapter timeTableAdapter = new TimeTableAdapter(listViewList);
                        lv_timetable.setAdapter(timeTableAdapter);

                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.i(volleyError.toString());
            }
        });
        MyApplication.getHttpQueues().add(jsonObjectRequest);
    }

    private void initView() {
        lv_timetable = findViewById(R.id.lv_timetable);
        wholeView = findViewById(R.id.wholeView);
//        wholeView.setDrawingCacheEnabled(true);
//        wholeView.buildDrawingCache();
        btn_img = findViewById(R.id.btn_img);
        btn_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermissionsUtil.hasPermission(TimeTableActivity.this, Manifest.permission.CAMERA)) {
                    //有权限
                    //Bitmap bmp = wholeView.getDrawingCache(); // 获取图片
                    saveBitmapFile(getBitmapByView(wholeView));// 保存图片
                    //wholeView.destroyDrawingCache(); // 保存过后释放资源
                } else {
                    PermissionsUtil.requestPermission(TimeTableActivity.this, new PermissionListener() {
                        @Override
                        public void permissionGranted(@NonNull String[] permissions) {
                            //用户授予了权限
                            saveBitmapFile(getBitmapByView(wholeView));// 保存图片
                        }

                        @Override
                        public void permissionDenied(@NonNull String[] permissions) {
                            //用户拒绝了申请
                            ToastUtils.showToastShort("没有权限");
                        }
                    }, new String[]{Manifest.permission.CAMERA}, false, null);
                }
            }
        });
    }

    private class TimeTableAdapter extends BaseAdapter {
        private ArrayList<List<TimeTableResultBean.DataBean>> listViewList;

        TimeTableAdapter(ArrayList<List<TimeTableResultBean.DataBean>> listViewList) {
            this.listViewList = listViewList;
        }

        @Override
        public int getCount() {
            return listViewList == null ? 0 : listViewList.size();
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

            View inflate = View.inflate(TimeTableActivity.this, R.layout.timetable_item, null);
            CustomGridView gv_timetable = inflate.findViewById(R.id.gv_timetable);
            TextView tv_place = inflate.findViewById(R.id.tv_place);
            tv_place.setText(listViewList.get(i).get(0).getRoom_name());
            gv_timetable.setAdapter(new TimeTableGv(allGridViewList.get(i)));

            return inflate;
        }
    }

    private class TimeTableGv extends BaseAdapter {
        List<TimeTableResultBean.DataBean> listItem;

        TimeTableGv(List<TimeTableResultBean.DataBean> listItem) {
            this.listItem = listItem;
        }

        @Override
        public int getCount() {
            return listItem.size();
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
            View inflate = View.inflate(TimeTableActivity.this, R.layout.timetable_gridview_item, null);
            RelativeLayout rl_boot = inflate.findViewById(R.id.rl_boot);
            ImageView gv_img = inflate.findViewById(R.id.gv_img);
            TextView tv_name = inflate.findViewById(R.id.tv_name);
            TextView tv_time = inflate.findViewById(R.id.tv_time);
            TextView tv_emp_name = inflate.findViewById(R.id.tv_emp_name);
            TextView tv_level = inflate.findViewById(R.id.tv_level);
            if (listItem.get(i) != null) {
                String startTime, endTime;
                int startInt = Integer.valueOf(listItem.get(i).getStart_time());
                int entInt = Integer.valueOf(listItem.get(i).getEnd_time());
                if (startInt % 60 == 0) {
                    startTime = startInt / 60 + ":00";
                } else {
                    startTime = startInt / 60 + ":" + startInt % 60;
                }
                if (entInt % 60 == 0) {
                    endTime = entInt / 60 + ":00";
                } else {
                    endTime = entInt / 60 + ":" + entInt % 60;
                }

                Glide.with(TimeTableActivity.this).load(listItem.get(i).getCover_img_url()).into(gv_img);
                tv_name.setText(listItem.get(i).getCourse_type_name());
                tv_emp_name.setText(listItem.get(i).getEmployee_name());
                tv_level.setText(listItem.get(i).getLevel());
                tv_time.setText(startTime + "-" + endTime);
                if ("初".equals(listItem.get(i).getLevel())) {
                    tv_level.setTextColor(getResources().getColor(R.color.green));
                } else if ("中".equals(listItem.get(i).getLevel())) {
                    tv_level.setTextColor(getResources().getColor(R.color.blue));
                } else {
                    tv_level.setTextColor(getResources().getColor(R.color.red));
                }

                rl_boot.setVisibility(View.VISIBLE);
            } else {
                rl_boot.setVisibility(View.GONE);
            }


            return inflate;
        }
    }

    public File saveBitmapFile(Bitmap bitmap) {
        File file = new File(Environment.getExternalStorageDirectory().getPath()
                + "/donglan/camera/" + System.currentTimeMillis() + ".jpg");//将要保存图片的路径
        File dir = new File(Environment.getExternalStorageDirectory().getPath()
                + "/donglan/camera/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            ToastUtils.showToastShort("保存成功");
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 截取scrollview的屏幕
     * @param scrollView
     * @return
     */
    public static Bitmap getBitmapByView(ScrollView scrollView) {
        int h = 0;
        Bitmap bitmap = null;
        // 获取scrollview实际高度
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
            scrollView.getChildAt(i).setBackgroundColor(
                    Color.parseColor("#ffffff"));
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }
}
