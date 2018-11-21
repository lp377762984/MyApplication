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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.RequestSimpleBean;
import com.cn.danceland.myapplication.bean.RequestSimpleRemarkBean;
import com.cn.danceland.myapplication.bean.TimeTableResultBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.CustomGridView;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.MyJsonObjectRequest;
import com.cn.danceland.myapplication.utils.MyListView;
import com.cn.danceland.myapplication.utils.TimeUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.cn.danceland.myapplication.view.DongLanTitleView;
import com.cn.danceland.myapplication.view.VerticleTextView;
import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * 课程安排
 * Created by feng on 2018/6/6.
 */

public class TimeTableActivity extends BaseActivity {
    private MyListView lv_timetable;
    private TextView time_tv;
    private TextView hint_tv;
    private Gson gson;
    private ArrayList<String> placeList;
    private HashSet<String> placeSet;
    private ArrayList<List<TimeTableResultBean.DataBean>> listViewList;
    private ArrayList<ArrayList<TimeTableResultBean.DataBean>> allGridViewList;
    private ScrollView wholeView;

    private DongLanTitleView donglantitle;
    //    private List<List<TimeTableResultBean.DataBean>> childListMap;//时间条目
    private String shopName = "";

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

        shopName = getIntent().getStringExtra("shopName");
    }

    class StrBean {
        public String date_gt;
        public String date_lt;
    }

    private void initData() {
//        time_tv.setText("时间：" + TimeUtils.timeStamp2Date(TimeUtils.date2TimeStamp(TimeUtils.getWeekStartTime(), "yyyy-MM-dd HH:mm:ss") + "", "yyyy-MM-dd")
//                + "-" + TimeUtils.timeStamp2Date(TimeUtils.date2TimeStamp(TimeUtils.getWeekEndTime(), "yyyy-MM-dd HH:mm:ss") + "", "yyyy-MM-dd"));
//        StrBean strBean = new StrBean();
//        strBean.date_gt = TimeUtils.date2TimeStamp(TimeUtils.getWeekStartTime(), "yyyy-MM-dd HH:mm:ss") + "";
//        strBean.date_lt = TimeUtils.date2TimeStamp(TimeUtils.getWeekEndTime(), "yyyy-MM-dd HH:mm:ss") + "";

        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        Calendar sevenStart = Calendar.getInstance(); // 当时的日期和时间
        int day = sevenStart.get(Calendar.DAY_OF_MONTH) + 6;
        sevenStart.set(Calendar.DAY_OF_MONTH, day);
        sevenStart.set(Calendar.HOUR_OF_DAY, 0);
        sevenStart.set(Calendar.MINUTE, 0);
        sevenStart.set(Calendar.SECOND, 0);
        sevenStart.set(Calendar.MILLISECOND, 0);
        LogUtil.i(" todayStart.getTime()" + new SimpleDateFormat("yyyy-MM-dd").format(todayStart.getTime()));
        time_tv.setText("时间：" + new SimpleDateFormat("yyyy-MM-dd").format(todayStart.getTime())
                + "-" + new SimpleDateFormat("yyyy-MM-dd").format(sevenStart.getTime()));
        StrBean strBean = new StrBean();
        strBean.date_gt = TimeUtils.date2TimeStamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(todayStart.getTime()), "yyyy-MM-dd HH:mm:ss") + "";
        strBean.date_lt = TimeUtils.date2TimeStamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(sevenStart.getTime()), "yyyy-MM-dd HH:mm:ss") + "";
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
                            for (int i = 0; i < data.size(); i++) {//如果是一个课室
                                if (placeArr[j].equals(data.get(i).getRoom_name())) {
                                    placeList.add(data.get(i));
                                }
                            }
                            Collections.sort(placeList, new Comparator<TimeTableResultBean.DataBean>() {
                                @Override
                                public int compare(TimeTableResultBean.DataBean o1, TimeTableResultBean.DataBean o2) {
                                    if (Integer.parseInt(o1.getStart_time()) > Integer.parseInt(o2.getStart_time())) {
                                        return 1;
                                    }
                                    if (Integer.parseInt(o1.getStart_time()) == Integer.parseInt(o2.getStart_time())) {
                                        return 0;
                                    }
                                    return -1;
                                }
                            });
                            listViewList.add(placeList);
                        }
                        Collections.reverse(listViewList);//倒序


                        for (List<TimeTableResultBean.DataBean> list : listViewList) {//输出打印
                            for (TimeTableResultBean.DataBean databen : list) {//
                                String startTime;
                                int startInt = Integer.valueOf(databen.getStart_time());
                                if (startInt % 60 == 0) {
                                    startTime = startInt / 60 + ":00";
                                } else {
                                    startTime = startInt / 60 + ":" + startInt % 60;
                                }
                                LogUtil.i("房间" + databen.getRoom_name() + "课程" + databen.getCourse_type_name() + "时间" + startTime);
                            }
                            LogUtil.i("-----");
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

        PrarmsBean prarmsBean = new PrarmsBean();
        prarmsBean.param_key = "group_course_table_remark";

        MyJsonObjectRequest request = new MyJsonObjectRequest(Request.Method.POST, Constants.FIND_PARAM_KEY, gson.toJson(prarmsBean), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtil.i(jsonObject.toString());
                RequestSimpleRemarkBean requsetSimpleBean = gson.fromJson(jsonObject.toString(), RequestSimpleRemarkBean.class);
                if (requsetSimpleBean.getSuccess()) {
                    hint_tv.setText(requsetSimpleBean.getData().getGroup_course_table_remark() + "");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.i(volleyError.toString());
            }
        });
        MyApplication.getHttpQueues().add(request);
    }

    private void initView() {
        lv_timetable = findViewById(R.id.lv_timetable);
        time_tv = findViewById(R.id.time_tv);
        hint_tv = findViewById(R.id.hint_tv);
        wholeView = findViewById(R.id.wholeView);
        donglantitle = findViewById(R.id.title);
        TextView more_iv = donglantitle.getMoreTv();
        if (shopName != null && shopName.length() > 0) {
            donglantitle.setTitle(shopName + "课表");
        }

        more_iv.setVisibility(View.VISIBLE);
        more_iv.setText("生成图片");
        more_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermissionsUtil.hasPermission(TimeTableActivity.this, Manifest.permission.CAMERA)) {
                    saveBitmapFile(getBitmapByView(wholeView));// 保存图片
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
        public View getView(int position, View view, ViewGroup viewGroup) {

            View inflate = View.inflate(TimeTableActivity.this, R.layout.timetable_item, null);
            TextView tv_place = inflate.findViewById(R.id.tv_place);
            MyListView timetable_lv = inflate.findViewById(R.id.timetable_lv);
            RelativeLayout timetable_rl = inflate.findViewById(R.id.timetable_rl);
            switch (position % 3) {
                case 0:
                    timetable_rl.setBackground(getResources().getDrawable(R.drawable.shade_red_bg));
                    break;
                case 1:
                    timetable_rl.setBackground(getResources().getDrawable(R.drawable.shade_blue_bg));
                    break;
                case 2:
                    timetable_rl.setBackground(getResources().getDrawable(R.drawable.shade_green_bg));
                    break;
            }

            tv_place.setText(listViewList.get(position).get(0).getRoom_name());
            ArrayList<String> timetempList = new ArrayList<>();//本教室 所有时间 去重 临时

            for (TimeTableResultBean.DataBean bean : listViewList.get(position)) {
                timetempList.add(bean.getStart_time());
            }
            List<String> timeList = new ArrayList();//本教室 所有时间 去重
            for (Iterator it = timetempList.iterator(); it.hasNext(); )//集合循环
            {
                Object obj = it.next();
                if (!timeList.contains(obj)) {//不包含重复的输出
                    timeList.add(obj + "");
                }
            }
            allGridViewList = new ArrayList<>();//这个教室所有课
            List<List<TimeTableResultBean.DataBean>> childListMap = new ArrayList<>();
            for (int i = 0; i < timeList.size(); i++) {
                TimeTableResultBean.DataBean temp1 = new TimeTableResultBean.DataBean();
                TimeTableResultBean.DataBean temp2 = new TimeTableResultBean.DataBean();
                TimeTableResultBean.DataBean temp3 = new TimeTableResultBean.DataBean();
                TimeTableResultBean.DataBean temp4 = new TimeTableResultBean.DataBean();
                TimeTableResultBean.DataBean temp5 = new TimeTableResultBean.DataBean();
                TimeTableResultBean.DataBean temp6 = new TimeTableResultBean.DataBean();
                TimeTableResultBean.DataBean temp7 = new TimeTableResultBean.DataBean();
                List<TimeTableResultBean.DataBean> list = new ArrayList<>();//假数据
                list.add(temp1);
                list.add(temp2);
                list.add(temp3);
                list.add(temp4);
                list.add(temp5);
                list.add(temp6);
                list.add(temp7);
                HashMap<String, List<TimeTableResultBean.DataBean>> hashmap = new HashMap<>();
                hashmap.put(timeList.get(i), list);
                childListMap.add(list);
            }

            for (TimeTableResultBean.DataBean bean : listViewList.get(position)) {
                for (int i = 0; i < timeList.size(); i++) {
                    if (timeList.get(i).equals(bean.getStart_time())) {//如果两个时间相等 看是周几的数据
                        List<TimeTableResultBean.DataBean> list = childListMap.get(i);
                        switch (bean.getWeek()) {
                            case "1"://周一
                                list.set(0, bean);
                                break;
                            case "2"://周二
                                list.set(1, bean);
                                break;
                            case "3"://周三
                                list.set(2, bean);
                                break;
                            case "4"://周四
                                list.set(3, bean);
                                break;
                            case "5"://周五
                                list.set(4, bean);
                                break;
                            case "6"://周六
                                list.set(5, bean);
                                break;
                            case "0"://周日
                                list.set(6, bean);
                                break;
                        }
                        childListMap.set(i, list);
                    }
                }
            }

            timetable_lv.setAdapter(new ChildTimeTableAdapter(timeList, childListMap));
            return inflate;
        }
    }

    private class ChildTimeTableAdapter extends BaseAdapter {
        List<String> listItem;
        List<List<TimeTableResultBean.DataBean>> childListMap;

        ChildTimeTableAdapter(List<String> listItem, List<List<TimeTableResultBean.DataBean>> childListMap) {
            this.listItem = listItem;
            this.childListMap = childListMap;
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
        public View getView(int position, View view, ViewGroup viewGroup) {
            View inflate = View.inflate(TimeTableActivity.this, R.layout.timetable_child_item, null);
            VerticleTextView time_rtv = inflate.findViewById(R.id.time_rtv);
            CustomGridView gv_timetable = inflate.findViewById(R.id.gv_timetable);
            String startTime;
            int startInt = Integer.valueOf(listItem.get(position) + "");
            if (startInt % 60 == 0) {
                startTime = startInt / 60 + ":00";
            } else {
                startTime = startInt / 60 + ":" + startInt % 60;
            }
            if (startInt / 60 < 10) {
                startTime = "0" + startTime;
            }
            time_rtv.setText(startTime);
            gv_timetable.setAdapter(new TimeTableGv(childListMap.get(position)));
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
            LinearLayout rl_boot = inflate.findViewById(R.id.rl_boot);
            ImageView gv_img = inflate.findViewById(R.id.gv_img);
            TextView tv_name = inflate.findViewById(R.id.tv_name);
            TextView tv_emp_name = inflate.findViewById(R.id.tv_emp_name);
            TextView tv_level = inflate.findViewById(R.id.tv_level);
            TextView time_tv = inflate.findViewById(R.id.time_tv);
            View isfree_view = inflate.findViewById(R.id.isfree_view);
            if (listItem.get(i).getCourse_type_name() != null) {
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
                time_tv.setText((entInt - startInt) + "分钟");
                RequestOptions options = new RequestOptions().placeholder(R.drawable.loading_img);
                Glide.with(TimeTableActivity.this).load(listItem.get(i).getCover_img_url()).apply(options).into(gv_img);
                tv_name.setText(listItem.get(i).getCourse_type_name());
                tv_name.setTextColor(Color.parseColor(listItem.get(i).getColor()));
                tv_emp_name.setText(listItem.get(i).getEmployee_name());
                tv_level.setText(listItem.get(i).getLevel());
                if ("初".equals(listItem.get(i).getLevel())) {
                    tv_level.setTextColor(getResources().getColor(R.color.green_color3));
                } else if ("中".equals(listItem.get(i).getLevel())) {
                    tv_level.setTextColor(getResources().getColor(R.color.blue_color3));
                } else if ("高".equals(listItem.get(i).getLevel())) {
                    tv_level.setTextColor(getResources().getColor(R.color.home_enter_total_text_color));
                } else if ("综合".equals(listItem.get(i).getLevel())) {
                    tv_level.setTextColor(getResources().getColor(R.color.home_top_bg_color));
                }
                switch (listItem.get(i).getCourse_category()) {//1	一对一私教 2	小团体私教 3	免费团课
                    case "1":
                        isfree_view.setVisibility(View.INVISIBLE);
                        break;
                    case "2":
                        isfree_view.setVisibility(View.VISIBLE);
                        isfree_view.setBackgroundColor(getResources().getColor(R.color.home_enter_total_text_color));
                        break;
                    case "3":
                        isfree_view.setVisibility(View.VISIBLE);
                        isfree_view.setBackgroundColor(getResources().getColor(R.color.blue_color3));
                        break;
                    default:
                        isfree_view.setVisibility(View.INVISIBLE);
                        break;
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
     *
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

    class PrarmsBean {
        public String param_key;

    }
}
