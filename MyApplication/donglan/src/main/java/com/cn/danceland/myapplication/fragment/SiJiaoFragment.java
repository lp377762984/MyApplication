package com.cn.danceland.myapplication.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.activity.CourseActivity;
import com.cn.danceland.myapplication.activity.CourseDetailActivity;
import com.cn.danceland.myapplication.activity.PingJiaActivity;
import com.cn.danceland.myapplication.activity.SiJiaoDetailActivity;
import com.cn.danceland.myapplication.activity.SmallTuankeDetailActivity;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.GroupRecordBean;
import com.cn.danceland.myapplication.bean.JiaoLianBean;
import com.cn.danceland.myapplication.bean.JiaoLianCourseBean;
import com.cn.danceland.myapplication.bean.MyCourseBean;
import com.cn.danceland.myapplication.bean.MyCourseConBean;
import com.cn.danceland.myapplication.bean.SiJiaoRecordBean;
import com.cn.danceland.myapplication.bean.SiJiaoYuYueConBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.MyListView;
import com.cn.danceland.myapplication.utils.NestedExpandaleListView;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.TimeUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by feng on 2018/1/12.
 */

public class SiJiaoFragment extends BaseFragment {

    NestedExpandaleListView ex_lv;
    ImageView down_img, up_img;
    String role, auth;
    Data data;
    Gson gson;
    MyListAdapter childListAdapter;
    List<JiaoLianCourseBean.Content> jiaolianContent;
    List<MyCourseBean.Data> myCourseList;
    List<SiJiaoRecordBean.Content> childContent;
    MyListView mylist;
    View view;
    String startTime;
    RelativeLayout rl_error;
    ImageView iv_error;
    TextView tv_error;

    @Override
    public View initViews() {
        View inflate = View.inflate(mActivity, R.layout.sijiao, null);
        view = LayoutInflater.from(mActivity).inflate(R.layout.sijiao_child_item, null);
        mylist = view.findViewById(R.id.mylist);
        mylist.setDividerHeight(0);
        ex_lv = inflate.findViewById(R.id.ex_lv);
        ex_lv.setGroupIndicator(null);
        ex_lv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                down_img = v.findViewById(R.id.down_img);
                up_img = v.findViewById(R.id.up_img);
                if (down_img.getVisibility() == View.GONE) {
                    down_img.setVisibility(View.VISIBLE);
                    up_img.setVisibility(View.GONE);
                } else {
                    if (role != null) {
                        getChildData(jiaolianContent.get(groupPosition).getId(), jiaolianContent.get(groupPosition).getCourse_category() + "");
                    } else {
                        getChildData(myCourseList.get(groupPosition).getId(), myCourseList.get(groupPosition).getCourse_category() + "");
                    }
                    down_img.setVisibility(View.GONE);
                    up_img.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
        rl_error = inflate.findViewById(R.id.rl_error);
        iv_error = rl_error.findViewById(R.id.iv_error);
        Glide.with(mActivity).load(R.drawable.img_error4).into(iv_error);
        tv_error = rl_error.findViewById(R.id.tv_error);
        tv_error.setText("请先购买私教");

        ex_lv.setEmptyView(rl_error);
        gson = new Gson();
        data = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);

        try {
            refresh();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return inflate;
    }


    @Override
    public void onClick(View v) {

    }

    public void getRoles(String role, String auth, String startTime) {

        this.role = role;
        this.auth = auth;
        this.startTime = startTime;

    }

    public void refresh() throws JSONException {

        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Data info = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
        final MyCourseConBean myCourseConBean = new MyCourseConBean();
        String url;
        if (info != null) {
            myCourseConBean.setBranch_id(Integer.valueOf(info.getPerson().getDefault_branch()));
            myCourseConBean.setPage(0);
            myCourseConBean.setPageCount(30);
            url = Constants.FINDMEMBERCOURSE;
            if (role != null) {
                myCourseConBean.setEmployee_id(info.getEmployee().getId());
                url = Constants.FINDEMPCOURSE;
            }
            String s = gson.toJson(myCourseConBean);
            JSONObject jsonObject = new JSONObject(s);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    LogUtil.e("zzf", jsonObject.toString());
                    if (role != null) {
                        JiaoLianCourseBean jiaoLianCourseBean = gson.fromJson(jsonObject.toString(), JiaoLianCourseBean.class);
                        if (jiaoLianCourseBean != null && jiaoLianCourseBean.getData() != null) {

                            jiaolianContent = jiaoLianCourseBean.getData().getContent();
                            if (jiaolianContent != null) {
                                ex_lv.setAdapter(new JiaoLianAdapter(jiaolianContent));
                            }

                        }

                    } else {
                        MyCourseBean myCourseBean = gson.fromJson(jsonObject.toString(), MyCourseBean.class);
                        if (myCourseBean != null) {

                            myCourseList = myCourseBean.getData();
                            if (myCourseList != null) {
                                ex_lv.setAdapter(new MyExAdapter(myCourseList));
                            }
                        }
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    LogUtil.e("zzf", volleyError.toString());
                    rl_error.setVisibility(View.VISIBLE);
                    tv_error.setText("网络异常");
                    Glide.with(mActivity).load(R.drawable.img_error7).into(iv_error);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));
                    return map;
                }
            };
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000,
                    2,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            MyApplication.getHttpQueues().add(jsonObjectRequest);
        }


    }


    private void getChildData(Integer courseid, final String course_category) {

        String url = null;

        SiJiaoYuYueConBean siJiaoYuYueConBean = new SiJiaoYuYueConBean();
        if (role != null) {
            siJiaoYuYueConBean.setEmployee_id(data.getEmployee().getId());
        } else {
            siJiaoYuYueConBean.setMember_no(data.getPerson().getMember_no());
        }

        if ("2".equals(course_category)) {
            url = Constants.FINDGROUPCOURSEAPPOINTLIST;
        } else {
            url = Constants.APPOINTLIST;
        }

        siJiaoYuYueConBean.setCourse_date_lt(System.currentTimeMillis());
        siJiaoYuYueConBean.setId(courseid);
        String s = gson.toJson(siJiaoYuYueConBean);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, s, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if ("2".equals(course_category)) {
                    GroupRecordBean groupRecordBean = gson.fromJson(jsonObject.toString(), GroupRecordBean.class);
                    if (groupRecordBean != null) {
                        List<GroupRecordBean.Data> data = groupRecordBean.getData();
                        if (data != null) {
                            MyGroupListAdapter myGroupListAdapter = new MyGroupListAdapter(data, course_category);
                            mylist.setAdapter(myGroupListAdapter);
                            //childListAdapter.notifyDataSetChanged();
                            //childListAdapter.notifyDataSetChanged();
                            //lv_tuanke.setAdapter(new RecordAdapter(content));
                        }
                    } else {
                        ToastUtils.showToastShort("当天无预约记录");
                    }
                } else {
                    LogUtil.e("zzf", jsonObject.toString());
                    SiJiaoRecordBean siJiaoRecordBean = gson.fromJson(jsonObject.toString(), SiJiaoRecordBean.class);
                    if (siJiaoRecordBean != null) {
                        SiJiaoRecordBean.Data data = siJiaoRecordBean.getData();
                        if (data != null) {
                            if (childContent != null) {
                                childContent.clear();
                            }
                            childContent = data.getContent();
                            if (childContent != null) {
                                childListAdapter = new MyListAdapter(childContent, course_category);
                                mylist.setAdapter(childListAdapter);
                                //childListAdapter.notifyDataSetChanged();
                                //childListAdapter.notifyDataSetChanged();
                                //lv_tuanke.setAdapter(new RecordAdapter(content));
                            } else {
                                ToastUtils.showToastShort("当天无预约记录");
                            }

                        }
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.e("zzf", volleyError.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));
                LogUtil.e("zzf", SPUtils.getString(Constants.MY_TOKEN, ""));
                return map;
            }

        };

        MyApplication.getHttpQueues().add(jsonObjectRequest);


    }

    private class JiaoLianAdapter extends BaseExpandableListAdapter {

        List<JiaoLianCourseBean.Content> list;

        JiaoLianAdapter(List<JiaoLianCourseBean.Content> list) {
            this.list = list;

        }

        @Override
        public int getGroupCount() {
            return list.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mActivity).inflate(R.layout.sijiao_parent_item, null);

                viewHolder.sijiao_title = convertView.findViewById(R.id.sijiao_title);
                viewHolder.sijiao_jiaolian = convertView.findViewById(R.id.sijiao_jiaolian);
                viewHolder.sijiao_num = convertView.findViewById(R.id.sijiao_num);
                viewHolder.sijiao_shengyu = convertView.findViewById(R.id.sijiao_shengyu);
                viewHolder.sijiao_date = convertView.findViewById(R.id.sijiao_date);
                viewHolder.sijiao_fangshi = convertView.findViewById(R.id.sijiao_fangshi);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            RelativeLayout sijiao_yuyue = convertView.findViewById(R.id.sijiao_yuyue);
            final String startTime = TimeUtils.timeStamp2Date(list.get(groupPosition).getStart_date() + "", "yyyy-MM-dd");
            final String endTime = TimeUtils.timeStamp2Date(list.get(groupPosition).getEnd_date() + "", "yyyy-MM-dd");
            sijiao_yuyue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.get(groupPosition).getCourse_category() == 1) {
                        startActivity(new Intent(mActivity, SiJiaoDetailActivity.class).
                                putExtra("item", list.get(groupPosition)).
                                putExtra("startTime", startTime).
                                putExtra("endTime", endTime)
                                .putExtra("role", role)
                                .putExtra("auth", auth));
                    } else if (list.get(groupPosition).getCourse_category() == 2) {
                        CourseActivity activity = (CourseActivity) getActivity();
                        if (activity != null) {
                            activity.getItemId(list.get(groupPosition).getId(), list.get(groupPosition).getCourse_type_id(), "2");
                            activity.showFragment("2", "");
                        }
                    }
                }
            });
            viewHolder.sijiao_title.setText(list.get(groupPosition).getCourse_type_name());
            viewHolder.sijiao_num.setText("购买节数：" + list.get(groupPosition).getCount() + "节");
            viewHolder.sijiao_shengyu.setText("剩余节数：" + list.get(groupPosition).getSurplus_count() + "节");

            viewHolder.sijiao_date.setText("有效期：" + startTime + "至" + endTime);
            if (list.get(groupPosition).getCourse_category() == 1) {
                viewHolder.sijiao_fangshi.setText("一对一");
            } else if (list.get(groupPosition).getCourse_category() == 2) {
                viewHolder.sijiao_fangshi.setText("小团体");
            } else {
                viewHolder.sijiao_fangshi.setText("");
            }

            if (list.get(groupPosition).getEmployee_name() != null) {
                viewHolder.sijiao_jiaolian.setText("上课会员：" + list.get(groupPosition).getMember_name());
            } else {
                viewHolder.sijiao_jiaolian.setVisibility(View.GONE);
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mActivity).inflate(R.layout.sijiao_child_item, null);
                //mylist = convertView.findViewById(R.id.mylist);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //mylist.setDividerHeight(0);
//            if(childContent!=null){
//                viewHolder.mylist.setAdapter(childListAdapter);
//            }

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }


    private class MyExAdapter extends BaseExpandableListAdapter {
        List<MyCourseBean.Data> list;

        MyExAdapter(List<MyCourseBean.Data> list) {
            this.list = list;
        }


        @Override
        public int getGroupCount() {
            return list.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mActivity).inflate(R.layout.sijiao_parent_item, null);

                viewHolder.sijiao_title = convertView.findViewById(R.id.sijiao_title);
                viewHolder.sijiao_jiaolian = convertView.findViewById(R.id.sijiao_jiaolian);
                viewHolder.sijiao_num = convertView.findViewById(R.id.sijiao_num);
                viewHolder.sijiao_shengyu = convertView.findViewById(R.id.sijiao_shengyu);
                viewHolder.sijiao_date = convertView.findViewById(R.id.sijiao_date);
                viewHolder.sijiao_fangshi = convertView.findViewById(R.id.sijiao_fangshi);
                viewHolder.sijiao_yuyue = convertView.findViewById(R.id.sijiao_yuyue);
                viewHolder.sijiao_detail = convertView.findViewById(R.id.sijiao_detail);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final String startTime = TimeUtils.timeStamp2Date(list.get(groupPosition).getStart_date() + "", "yyyy-MM-dd");
            final String endTime = TimeUtils.timeStamp2Date(list.get(groupPosition).getEnd_date() + "", "yyyy-MM-dd");
            viewHolder.sijiao_yuyue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.get(groupPosition).getCourse_category() == 1) {
                        startActivity(new Intent(mActivity, SiJiaoDetailActivity.class).
                                putExtra("item", list.get(groupPosition)).
                                putExtra("startTime", startTime).
                                putExtra("endTime", endTime)
                                .putExtra("role", role)
                                .putExtra("auth", auth));
                    } else if (list.get(groupPosition).getCourse_category() == 2) {
                        CourseActivity activity = (CourseActivity) getActivity();
                        if (activity != null) {
                            activity.getItemId(list.get(groupPosition).getId(), list.get(groupPosition).getCourse_type_id(), "2");
                            activity.showFragment("2", "");
                        }
                    }
                }
            });
            viewHolder.sijiao_title.setText(list.get(groupPosition).getCourse_type_name());
            viewHolder.sijiao_num.setText("购买节数：" + list.get(groupPosition).getCount() + "节");
            viewHolder.sijiao_shengyu.setText("剩余节数：" + list.get(groupPosition).getSurplus_count() + "节");

            viewHolder.sijiao_date.setText("有效期：" + startTime + "至" + endTime);
            if (list.get(groupPosition).getCourse_category() == 1) {
                viewHolder.sijiao_fangshi.setText("一对一");
            } else if (list.get(groupPosition).getCourse_category() == 2) {
                viewHolder.sijiao_fangshi.setText("小团体");
            } else {
                viewHolder.sijiao_fangshi.setText("");
            }

            if (list.get(groupPosition).getEmployee_name() != null) {
                viewHolder.sijiao_jiaolian.setText("上课教练：" + list.get(groupPosition).getEmployee_name());
            } else {
                viewHolder.sijiao_jiaolian.setText("上课教练：---");
            }

            viewHolder.sijiao_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.get(groupPosition).getCourse_category() == 1) {
                        startActivity(new Intent(mActivity, CourseDetailActivity.class).
                                putExtra("item", list.get(groupPosition)).
                                putExtra("startTime", startTime).
                                putExtra("endTime", endTime)
                                .putExtra("role", role)
                                .putExtra("auth", auth));
                    }
//                    else if(list.get(groupPosition).getCourse_category()==2){
//                        CourseActivity activity = (CourseActivity) getActivity();
//                        if(activity!=null){
//                            activity.getItemId(list.get(groupPosition).getId(),list.get(groupPosition).getCourse_type_id(),"2");
//                            activity.showFragment("2","");
//                        }
//                    }
                }
            });

            if (list.get(groupPosition).getCourse_category() == 2) {
                viewHolder.sijiao_detail.setVisibility(View.GONE);
            } else if (list.get(groupPosition).getCourse_category() == 1) {
                viewHolder.sijiao_detail.setVisibility(View.VISIBLE);
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = view;
                //mylist = convertView.findViewById(R.id.mylist);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //mylist.setDividerHeight(0);
//            if(childContent!=null){
//                viewHolder.mylist.setAdapter(childListAdapter);
//            }


            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }

    class ViewHolder {
        RelativeLayout sijiao_detail, sijiao_yuyue;
        MyListView mylist;
        TextView sijiao_title, sijiao_jiaolian, sijiao_num, sijiao_shengyu, sijiao_date, sijiao_fangshi;
    }

    private class MyListAdapter extends BaseAdapter {
        List<SiJiaoRecordBean.Content> list;
        String course_category;

        MyListAdapter(List<SiJiaoRecordBean.Content> list, String course_category) {
            this.list = list;
            this.course_category = course_category;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder1 viewHolder1;
            if (convertView == null) {
                viewHolder1 = new ViewHolder1();
                convertView = LayoutInflater.from(mActivity).inflate(R.layout.sijiao_list_item, null);
                viewHolder1.tv_time = convertView.findViewById(R.id.tv_time);
                viewHolder1.tv_status = convertView.findViewById(R.id.tv_status);
                viewHolder1.tv_pingfen = convertView.findViewById(R.id.tv_pingfen);
                convertView.setTag(viewHolder1);
            } else {
                viewHolder1 = (ViewHolder1) convertView.getTag();
            }

            String time;
            if (list.get(position).getStart_time() % 60 == 0) {
                time = list.get(position).getStart_time() / 60 + ":00";
            } else {
                time = list.get(position).getStart_time() / 60 + ":" + list.get(position).getStart_time() % 60;
            }

            viewHolder1.tv_time.setText("时间：" + TimeUtils.timeStamp2Date(list.get(position).getCourse_date() + "", "yyyy-MM-dd HH:mm:ss").split(" ")[0] + " " + time);

            if ("2".equals(course_category)) {

                viewHolder1.tv_pingfen.setText("查看详情");

            } else {
                int status = list.get(position).getStatus();
                viewHolder1.tv_pingfen.setText("无法评分");
                if (status == 1) {
                    viewHolder1.tv_status.setText("已过期");
                } else if (status == 2) {
                    viewHolder1.tv_status.setText("未签到");
                } else if (status == 3) {
                    viewHolder1.tv_status.setText("已取消");
                } else if (status == 4) {
                    viewHolder1.tv_status.setText("已签到");
                    if (list.get(position).getEvaluate_id() > 0) {
                        viewHolder1.tv_pingfen.setText("查看评价");
                    } else {
                        viewHolder1.tv_pingfen.setText("去评分");
                    }
                }
            }


            viewHolder1.tv_pingfen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.get(position).getStatus() == 4) {
                        if ("2".equals(course_category)) {
                            startActivity(new Intent(mActivity, SmallTuankeDetailActivity.class).putExtra("record", list.get(position)));
                        } else {
                            startActivity(new Intent(mActivity, PingJiaActivity.class).putExtra("item", list.get(position)).
                                    putExtra("course_category", course_category).putExtra("evaluate_id", list.get(position).getEvaluate_id()));
                        }
                    }
                }
            });

            return convertView;
        }
    }


    private class MyGroupListAdapter extends BaseAdapter {
        List<GroupRecordBean.Data> list;
        String course_category;

        MyGroupListAdapter(List<GroupRecordBean.Data> list, String course_category) {
            this.list = list;
            this.course_category = course_category;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder1 viewHolder1;
            if (convertView == null) {
                viewHolder1 = new ViewHolder1();
                convertView = LayoutInflater.from(mActivity).inflate(R.layout.sijiao_list_item, null);
                viewHolder1.tv_time = convertView.findViewById(R.id.tv_time);
                viewHolder1.tv_status = convertView.findViewById(R.id.tv_status);
                viewHolder1.tv_pingfen = convertView.findViewById(R.id.tv_pingfen);
                convertView.setTag(viewHolder1);
            } else {
                viewHolder1 = (ViewHolder1) convertView.getTag();
            }

            viewHolder1.tv_time.setText(TimeUtils.timeStamp2Date(list.get(position).getDate(), "yyyy-MM-dd"));

            if ("2".equals(course_category)) {

                viewHolder1.tv_pingfen.setText("查看详情");

            }


            viewHolder1.tv_pingfen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //if(list.get(position).getStatus()==4){
                    if ("2".equals(course_category)) {
                        startActivity(new Intent(mActivity, SmallTuankeDetailActivity.class).putExtra("record", list.get(position)));
                    }
                }
            });

            return convertView;
        }
    }

    class ViewHolder1 {
        TextView tv_time, tv_status, tv_pingfen;
    }
}
