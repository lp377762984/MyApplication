package com.cn.danceland.myapplication.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.request.RequestOptions;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.ParamInfoBean;
import com.cn.danceland.myapplication.bean.PotentialInfo;
import com.cn.danceland.myapplication.bean.RequestConsultantInfoBean;
import com.cn.danceland.myapplication.bean.RequestSellCardsInfoBean;
import com.cn.danceland.myapplication.bean.RequsetSimpleBean;
import com.cn.danceland.myapplication.evntbus.IntEvent;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.MyJsonObjectRequest;
import com.cn.danceland.myapplication.utils.MyStringRequest;
import com.cn.danceland.myapplication.utils.PhoneFormatCheckUtils;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.cn.danceland.myapplication.view.ContainsEmojiEditText;
import com.cn.danceland.myapplication.view.CustomDatePicker;
import com.google.gson.Gson;
import com.weigan.loopview.LoopView;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import razerdp.basepopup.BasePopupWindow;


/**
 * Created by shy on 2018/1/9 11:50
 * Email:644563767@qq.com
 * 添加潜客
 */


public class EditPotentialActivity extends BaseActivity implements OnClickListener {

    public static final int REGISTER_CHANNEL = 1;//客户来源
    public static final int VISIT_TYPE = 9;//回访方式
    public static final int TARGET = 8;//健身目的
    public static final int LIKE = 7;//喜欢项目
    public static final int MEDICAL = 11;//病史
    private ContainsEmojiEditText et_remark,et_biaoqian;//备注
    private TextView tv_medical_history;//慢性病史
    private ContainsEmojiEditText et_phone;//电话
    private EditText et_name;//名字
    private TextView tv_sex;//性别
    private ContainsEmojiEditText et_weixin_no;//微信号
    private ContainsEmojiEditText et_company;//单位
    private ContainsEmojiEditText et_address;//地址
    private ContainsEmojiEditText et_email;//邮箱
    private TextView tv_guest_aware_way;//客户来源
    private ScaleRatingBar sr_fitness_level;//健身指数
    private ScaleRatingBar sr_follow_level;//关注程度
    private TextView tv_target;//健身目的
    private TextView tv_card_type;//意向卡型
    private TextView tv_like;//喜欢项目
    private List<ParamInfoBean.Data> mParamInfoList = new ArrayList<>();
    private List<RequestSellCardsInfoBean.Data> mParamCradList = new ArrayList<>();
    private ListPopup listPopup;
    private int codeType = 0;
    private ListPopupMultiSelect listPopupMultiSelect;
    private ListCardPopup listCardPopup;
    private PotentialInfo info = new PotentialInfo();
    private Gson gson = new Gson();
    private int jiaolian_type = 0;
    private TextView tv_certificate_type;
    private ListJiaoLianPopup listJiaoLianPopup;
    private ContainsEmojiEditText et_certificate_no, et_nationality;
    private ContainsEmojiEditText et_emergency_name;
    private ContainsEmojiEditText et_emergency_phone;
    private TextView tv_birthday;
    private ContainsEmojiEditText et_height;
    private ContainsEmojiEditText et_weight;
    private TextView tv_admin_name;
    private TextView tv_teach_name;
    private Data data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_potential_customer);
        initView();
        setData();
    }

    private void setData() {
        if (!TextUtils.isEmpty(info.getSelf_avatar_url())) {
            RequestOptions options = new RequestOptions().placeholder(R.drawable.img_my_avatar);
            String S = info.getSelf_avatar_url();
            //    Glide.with(mActivity).load(S).apply(options).into(iv_avatar);
        }

//        if (TextUtils.equals(info.getGender(), "1")) {
//            iv_sex.setImageResource(R.drawable.img_sex1);
//        }
//        if (TextUtils.equals(info.getGender(), "2")) {
//            iv_sex.setImageResource(R.drawable.img_sex2);
//        }
        data = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
        et_phone.setText(info.getPhone_no());
        et_name.setText(info.getCname());
        //et_lasttime.setText("最后维护时间：" + info.getLast_time());
        et_weixin_no.setText(info.getWeichat_no());
        et_company.setText(info.getCompany());
        et_address.setText(info.getAddress());
        et_email.setText(info.getMail());
        tv_guest_aware_way.setText(info.getGuest_way());
        if (!TextUtils.isEmpty(info.getFitness_level())) {
            sr_fitness_level.setRating(Integer.parseInt(info.getFitness_level()));
        }
        if (!TextUtils.isEmpty(info.getFollow_level())) {
            sr_follow_level.setRating(Integer.parseInt(info.getFollow_level()));
        }
        if (!TextUtils.isEmpty(info.getTeach_name())) {
            tv_teach_name.setText(info.getTeach_name());
        }


        tv_card_type.setText(info.getCard_type());
        et_remark.setText(info.getRemark());


        //  List<String> list = new ArrayList<String>();
        if (info.getTargetList() != null && info.getTargetList().size() > 0) {
            String s = "";
            for (int j = 0; j < info.getTargetList().size(); j++) {

                if (s == "") {
                    s = info.getTargetList().get(j).getData_value();
                } else {
                    s = s + "," + info.getTargetList().get(j).getData_value();
                }
                //  list.add(mParamInfoList.get(j).getData_key() + "");
            }
            tv_target.setText(s);
        }
        if (info.getProjectList() != null && info.getProjectList().size() > 0) {
            String s1 = "";
            for (int j = 0; j < info.getProjectList().size(); j++) {

                if (s1 == "") {
                    s1 = info.getProjectList().get(j).getData_value();
                } else {
                    s1 = s1 + "," + info.getProjectList().get(j).getData_value();
                }
            }
            tv_like.setText(s1);
        }
        if (info.getChonicList() != null && info.getChonicList().size() > 0) {
            String s2 = "";
            for (int j = 0; j < info.getChonicList().size(); j++) {

                if (s2 == "") {
                    s2 = info.getChonicList().get(j).getData_value();
                } else {
                    s2 = s2 + "," + info.getChonicList().get(j).getData_value();
                }
            }
            tv_medical_history.setText(s2);
        }
        if (TextUtils.equals(info.getGender(), "1")) {
            tv_sex.setText("男");
        }
        if (TextUtils.equals(info.getGender(), "2")) {
            tv_sex.setText("女"
            );
        }
        et_nationality.setText(info.getNationality());
        tv_certificate_type.setText(info.getCertificate_type());
        et_certificate_no.setText(info.getIdentity_card());
        et_emergency_name.setText(info.getEmergency_name());
        et_emergency_phone.setText(info.getEmergency_phone());
        et_height.setText(info.getHeight());
        et_weight.setText(info.getWeight());

        if (TextUtils.equals(info.getAuth(), "2")) {
            findViewById(R.id.ll_admin).setVisibility(View.GONE);
        }
        tv_birthday.setText(info.getBirthday());

        tv_admin_name.setText(data.getEmployee().getCname());
        tv_admin_name.setClickable(false);
        tv_admin_name.setTextColor(Color.GRAY);
        tv_teach_name.setTextColor(Color.GRAY);
        tv_admin_name.setFocusable(false);
        tv_teach_name.setClickable(false);
        tv_teach_name.setClickable(false);
        et_phone.setTextColor(Color.GRAY);
        if (SPUtils.getInt(Constants.ROLE_ID, 0) == Constants.ROLE_ID_HUIJIGUWEN || SPUtils.getInt(Constants.ROLE_ID, 0) == Constants.ROLE_ID_HUIJIZHUGUANG) {
            info.setAdmin_emp_id(data.getEmployee().getId() + "");
            info.setAdmin_name(data.getEmployee().getCname());
            tv_admin_name.setText(data.getEmployee().getCname());
            tv_admin_name.setClickable(false);
            tv_admin_name.setFocusable(false);
            et_biaoqian.setText(info.getAdmin_mark());

        }
        if (SPUtils.getInt(Constants.ROLE_ID, 0) == Constants.ROLE_ID_JIAOLIAN || SPUtils.getInt(Constants.ROLE_ID, 0) == Constants.ROLE_ID_JIAOLIANZHUGUAN) {
            info.setTeach_emp_id(data.getEmployee().getId() + "");
            info.setAdmin_name(data.getEmployee().getCname());
            tv_teach_name.setText(data.getEmployee().getCname());
            tv_teach_name.setClickable(false);
            tv_teach_name.setClickable(false);
            et_biaoqian.setText(info.getTeach_mark());

        }

        if (TextUtils.equals(info.getAuth(), "2")) {

            et_name.setFocusable(false);
            tv_sex.setClickable(false);
            tv_certificate_type.setClickable(false);
            et_certificate_no.setFocusable(false);
            et_name.setTextColor(Color.GRAY);
            tv_sex.setTextColor(Color.GRAY);
            tv_certificate_type.setTextColor(Color.GRAY);
            et_certificate_no.setTextColor(Color.GRAY);
        }


    }


    private void initView() {
        Bundle bundle = getIntent().getExtras();
        info = (PotentialInfo) bundle.getSerializable("info");
        listPopup = new ListPopup(this);
        listJiaoLianPopup = new ListJiaoLianPopup(this);
        listPopupMultiSelect = new ListPopupMultiSelect(this);
        listCardPopup = new ListCardPopup(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        et_phone = findViewById(R.id.et_phone);
        et_name = findViewById(R.id.et_name);
        tv_sex = findViewById(R.id.tv_sex);
        tv_sex.setOnClickListener(this);

        et_weixin_no = findViewById(R.id.et_weixin_no);
        et_biaoqian = findViewById(R.id.et_biaoqian);
        et_company = findViewById(R.id.et_company);
        et_address = findViewById(R.id.et_address);
        et_email = findViewById(R.id.et_email);


        tv_guest_aware_way = findViewById(R.id.tv_guest_aware_way);
        tv_guest_aware_way.setOnClickListener(this);


        sr_fitness_level = findViewById(R.id.sr_fitness_level);
        sr_fitness_level.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar baseRatingBar, float rating) {


                if ((int) rating != 0) {
                    info.setFitness_level((int) rating + "");
                }
            }
        });
        sr_follow_level = findViewById(R.id.sr_follow_level);
        sr_follow_level.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar baseRatingBar, float rating) {
                if ((int) rating != 0) {
                    info.setFollow_level((int) rating + "");
                }

            }
        });
        tv_target = findViewById(R.id.tv_target);
        tv_target.setOnClickListener(this);
        tv_card_type = findViewById(R.id.tv_card_type);
        tv_card_type.setOnClickListener(this);
        tv_like = findViewById(R.id.tv_like);
        tv_like.setOnClickListener(this);
        tv_medical_history = findViewById(R.id.tv_medical_history);
        tv_medical_history.setOnClickListener(this);
        et_remark = findViewById(R.id.et_remark);
        findViewById(R.id.dlbtn_commit).setOnClickListener(this);

        tv_certificate_type = findViewById(R.id.et_certificate_type);
        tv_certificate_type.setOnClickListener(this);
        et_certificate_no = findViewById(R.id.et_certificate_no);
        et_emergency_name = findViewById(R.id.et_emergency_name);
        et_emergency_phone = findViewById(R.id.et_emergency_phone);
        tv_birthday = findViewById(R.id.tv_birthday);
        tv_birthday.setOnClickListener(this);
        et_height = findViewById(R.id.et_height);
        et_weight = findViewById(R.id.et_weight);
        tv_teach_name = findViewById(R.id.tv_teach_name);


        tv_teach_name.setOnClickListener(this);
        tv_admin_name = findViewById(R.id.tv_admin_name);
        tv_admin_name.setOnClickListener(this);
        et_nationality = findViewById(R.id.et_nationality);


        inflate1 = LayoutInflater.from(this).inflate(R.layout.birthdayselect, null);
        lp_year = inflate1.findViewById(R.id.lp_year);
        lp_month = inflate1.findViewById(R.id.lp_month);
        lp_date = inflate1.findViewById(R.id.lp_date);
        alertdialog = new AlertDialog.Builder(this);
        Time time = new Time();
        time.setToNow();
        year = time.year;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_sex://选择性别
                showListDialog();
                break;

            case R.id.tv_teach_name://选择潜客教练
                jiaolian_type = 1;
                findConsultant(data.getPerson().getDefault_branch(), jiaolian_type);
                break;
            case R.id.tv_admin_name://选择潜客会籍
                jiaolian_type = 2;
                findConsultant(data.getPerson().getDefault_branch(), jiaolian_type);
                break;
            case R.id.et_certificate_type://证件类型
                showCertificate_type();
                break;
            case R.id.tv_birthday://选择生日
                showDate();
                break;

            case R.id.tv_card_type://选择意向卡型
                codeType = 20;
                findCardsByCardId("");
                break;


            case R.id.tv_guest_aware_way://选择客户来源
                codeType = REGISTER_CHANNEL;
                findParams(codeType);

                break;
            case R.id.tv_target://选择健身目的
                codeType = TARGET;
                findParams(codeType);
                break;
            case R.id.tv_like://选择喜欢项目
                codeType = LIKE;
                findParams(codeType);
                break;
            case R.id.tv_medical_history://选择慢性病史
                codeType = MEDICAL;
                findParams(codeType);
                break;
            case R.id.dlbtn_commit://保存
                if (TextUtils.isEmpty(et_phone.getText().toString().trim())) {
                    ToastUtils.showToastShort("手机号码必须填写");
                    return;
                }
                info.setPhone_no(et_phone.getText().toString());
                if (TextUtils.isEmpty(info.getGender())) {
                    ToastUtils.showToastShort("性别必须填写");
                    return;
                }
                if (TextUtils.isEmpty(et_name.getText().toString().trim())) {
                    ToastUtils.showToastShort("姓名必须填写");
                    return;
                }

                if (!TextUtils.isEmpty(et_email.getText().toString())) {
                    if (et_email.getText().toString().matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")) {

                    } else {
                        ToastUtils.showToastShort("邮箱格式不合法");
                        return;
                    }
                }
                if (!TextUtils.isEmpty(et_certificate_no.getText().toString()) && TextUtils.isEmpty(tv_certificate_type.getText().toString())) {
                    ToastUtils.showToastShort("请选择证件类型");
                    return;
                }
                if (TextUtils.equals(tv_certificate_type.getText().toString(), "身份证") && !TextUtils.isEmpty(et_certificate_no.getText().toString())) {

                    try {
                        if (!PhoneFormatCheckUtils.isIDNumber(et_certificate_no.getText().toString())) {
                            ToastUtils.showToastShort("身份证不合法");
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                info.setCname(et_name.getText().toString());
                info.setCompany(et_company.getText().toString());
                info.setAddress(et_address.getText().toString());
                info.setMail(et_email.getText().toString());
                info.setRemark(et_remark.getText().toString());
                info.setWeichat_no(et_weixin_no.getText().toString());
                Data data = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
                info.setDefault_branch(data.getPerson().getDefault_branch());

                info.setIdentity_card(et_certificate_no.getText().toString().trim());
                info.setEmergency_name(et_emergency_name.getText().toString().trim());
                info.setEmergency_phone(et_emergency_phone.getText().toString().trim());
                info.setHeight(et_height.getText().toString().trim());
                info.setWeight(et_weight.getText().toString().trim());
                info.setNationality(et_nationality.getText().toString().trim());
                //会籍或会籍主管
                if (SPUtils.getInt(Constants.ROLE_ID, 0) == Constants.ROLE_ID_HUIJIGUWEN || SPUtils.getInt(Constants.ROLE_ID, 0) == Constants.ROLE_ID_HUIJIZHUGUANG) {
                    info.setAdmin_mark(et_biaoqian.getText().toString());

                }
                //教练或教练主管
                if (SPUtils.getInt(Constants.ROLE_ID, 0) == Constants.ROLE_ID_JIAOLIAN || SPUtils.getInt(Constants.ROLE_ID, 0) == Constants.ROLE_ID_JIAOLIANZHUGUAN) {
                    info.setTeach_mark(et_biaoqian.getText().toString());
                }

                LogUtil.i(gson.toJson(info).toString());
                try {
                    update_potential(gson.toJson(info).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }


    private void showCertificate_type() {
        final String[] items = {"身份证", "军官证", "驾驶证"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(this);
        //listDialog.setTitle("我是一个列表Dialog");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case 0:
                        info.setCertificate_type(items[0]);
                        tv_certificate_type.setText(items[0]);
                        break;
                    case 1:
                        info.setCertificate_type(items[1]);
                        tv_certificate_type.setText(items[1]);
                        break;
                    case 2:
                        info.setCertificate_type(items[2]);
                        tv_certificate_type.setText(items[2]);
                        break;
                    default:
                        break;
                }
            }
        });
        listDialog.show();
    }


    View inflate, inflate1;
    LoopView loopview, lp_year, lp_month, lp_date;
    int year;
    String syear, smonth, sdate;//性别:1、男，2、女，3、未知，4、保密
    int daysByYearMonth;
    AlertDialog.Builder alertdialog;

    private void showDate() {

//        ViewGroup parent = (ViewGroup) inflate1.getParent();
//        if (parent != null) {
//            parent.removeAllViews();
//        }
//
//        final ArrayList<String> yearList = new ArrayList<String>();
//        final ArrayList<String> monthList = new ArrayList<String>();
//        final ArrayList<String> dateList = new ArrayList<String>();
//        int n = 1900;
//        int len = year - n;
//        for (int i = 0; i <= len; i++) {
//            yearList.add((n + i) + "");
//        }
//        for (int j = 0; j < 12; j++) {
//            monthList.add((1 + j) + "");
//        }
//        lp_year.setNotLoop();
//        lp_date.setNotLoop();
//        lp_month.setNotLoop();
//        lp_year.setItems(yearList);
//        lp_month.setItems(monthList);
//
//        lp_year.setInitPosition(yearList.size() - 20);
//        syear = yearList.get(yearList.size() - 20);
//        lp_month.setInitPosition(0);
//        smonth = monthList.get(0);
//        sdate = "1";
//        daysByYearMonth = TimeUtils.getDaysByYearMonth(Integer.valueOf(syear), Integer.valueOf(smonth));
//        dateList.clear();
//        for (int z = 1; z <= daysByYearMonth; z++) {
//            dateList.add(z + "");
//        }
//        lp_date.setItems(dateList);
//
//        //设置字体大小
//        lp_year.setTextSize(16);
//        lp_month.setTextSize(16);
//        lp_date.setTextSize(16);
//
//        lp_year.setListener(new OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(int index) {
//                syear = yearList.get(index);
//                daysByYearMonth = TimeUtils.getDaysByYearMonth(Integer.valueOf(syear), Integer.valueOf(smonth));
//                dateList.clear();
//                for (int z = 1; z <= daysByYearMonth; z++) {
//                    dateList.add(z + "");
//                }
//                lp_date.setItems(dateList);
//            }
//        });
//
//        lp_month.setListener(new OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(int index) {
//                smonth = monthList.get(index);
//                daysByYearMonth = TimeUtils.getDaysByYearMonth(Integer.valueOf(syear), Integer.valueOf(smonth));
//                dateList.clear();
//                for (int z = 1; z <= daysByYearMonth; z++) {
//                    dateList.add(z + "");
//                }
//                lp_date.setItems(dateList);
//            }
//        });
//
//        lp_date.setListener(new OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(int index) {
//                sdate = dateList.get(index);
//            }
//        });
//
//        alertdialog.setTitle("选择出生年月");
//        alertdialog.setView(inflate1);
//        alertdialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                tv_birthday.setText(syear + "年" + smonth + "月" + sdate + "日");
//                info.setBirthday(syear + "-" + smonth + "-" + sdate);
//            }
//        });
//        alertdialog.show();
        final CustomDatePicker customDatePicker = new CustomDatePicker(this, "请选择日期");
        customDatePicker.setGoneHourAndMinute();
        customDatePicker.showWindow();
        customDatePicker.setDialogOnClickListener(new CustomDatePicker.OnClickEnter() {
            @Override
            public void onClick() {
                String dateString = customDatePicker.getHorizongtal();
                tv_birthday.setText(dateString);
                info.setBirthday(dateString);
            }
        });
    }

    private void showListDialog() {
        final String[] items = {"男", "女"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(this);
        //listDialog.setTitle("我是一个列表Dialog");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case 0:

                        tv_sex.setText("男");
                        info.setGender("1");
                        break;
                    case 1:

                        tv_sex.setText("女");
                        info.setGender("2");
                        break;
                    default:
                        break;
                }
            }
        });
        listDialog.show();
    }


    private void showInputDialog() {
    /*@setView 装入一个EditView
     */
        final EditText editText = new EditText(this);
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(this);
        inputDialog.setTitle("请输入电话号码").setView(editText);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(EditPotentialActivity.this,
                                editText.getText().toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
        inputDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        inputDialog.show();
    }

    private List<RequestConsultantInfoBean.Data> consultantInfo = new ArrayList<>();

    class ListJiaoLianPopup extends BasePopupWindow {


        Context context;

        public ListJiaoLianPopup(Context context) {
            super(context);
            ListView popup_list = (ListView) findViewById(R.id.popup_list);
            MyPopupListAdapter myPopupListAdapter = new MyPopupListAdapter(context);
            popup_list.setAdapter(myPopupListAdapter);
            this.context = context;
        }

        @Override
        protected Animation initShowAnimation() {
            return null;
        }

        @Override
        public View getClickToDismissView() {
            return getPopupWindowView();
        }

        @Override
        public View onCreatePopupView() {

            //  popupView=View.inflate(context,R.layout.popup_list_consultant,null);
            return createPopupById(R.layout.popup_list_potential);

        }

        @Override
        public View initAnimaView() {
            return null;
        }

        class MyPopupListAdapter extends BaseAdapter {
            private Context context;

            public MyPopupListAdapter(Context context) {
                this.context = context;
            }

            @Override
            public int getCount() {
                return consultantInfo.size();
            }

            @Override
            public Object getItem(int i) {
                return i;
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup viewGroup) {
                //     LogUtil.i("asdasdjalsdllasjdlk");
                ViewHolder vh = null;
                if (convertView == null) {
                    vh = new ViewHolder();
                    convertView = View.inflate(context, R.layout.listview_item_list_consultant, null);
                    vh.mTextView = (TextView) convertView.findViewById(R.id.item_tx);
                    convertView.setTag(vh);
                } else {
                    vh = (ViewHolder) convertView.getTag();
                }
                vh.mTextView.setText(consultantInfo.get(position).getCname());

                vh.mTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        switch (jiaolian_type) {
                            case 1://选择教练
                                tv_teach_name.setText(consultantInfo.get(position).getCname());
                                info.setTeach_name(consultantInfo.get(position).getCname());
                                info.setTeach_emp_id(consultantInfo.get(position).getId() + "");
                                break;
                            case 2://选择会籍
                                tv_admin_name.setText(consultantInfo.get(position).getCname());
                                info.setAdmin_name(consultantInfo.get(position).getCname());
                                info.setAdmin_emp_id(consultantInfo.get(position).getId() + "");
                                break;
                            default:
                                break;
                        }

                        dismiss();
                    }
                });

                return convertView;

            }


            class ViewHolder {
                public TextView mTextView;
            }
        }
    }


    /**
     * 查找会籍顾问或教练
     */
    private void findConsultant(final String branchId, int jiaolian_type) {
        String url;
        if (jiaolian_type == 1) {//教练
            url = Constants.FIND_JIAOLIAN_URL;
        } else {
            url = Constants.FIND_CONSULTANT_URL;
        }

        MyStringRequest request = new MyStringRequest(Request.Method.POST, Constants.FIND_CONSULTANT_URL, new Response.Listener<String>() {


            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                Gson gson = new Gson();
                RequestConsultantInfoBean requestConsultantInfoBean = gson.fromJson(s, RequestConsultantInfoBean.class);
                if (requestConsultantInfoBean.getSuccess()) {
                    consultantInfo = requestConsultantInfoBean.getData();
                    //  LogUtil.i(consultantListInfo.toString());
                    listJiaoLianPopup.showPopupWindow();

                } else {
                    ToastUtils.showToastShort(requestConsultantInfoBean.getErrorMsg());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToastShort(volleyError.toString());
            }
        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();

                map.put("branch_id", branchId);

                return map;

            }

        };
        MyApplication.getHttpQueues().add(request);

    }

    /**
     * 编辑资料
     *
     * @param data
     * @throws JSONException
     */
    public void update_potential(final String data) throws JSONException {


        JSONObject jsonObject = new JSONObject(data);

        MyJsonObjectRequest stringRequest = new MyJsonObjectRequest(Request.Method.PUT, Constants.UPDATE_POTENTIAL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtil.i(jsonObject.toString());
                RequsetSimpleBean requestOrderBean = gson.fromJson(jsonObject.toString(), RequsetSimpleBean.class);
                if (requestOrderBean.isSuccess()) {
                    ToastUtils.showToastShort("保存成功");
                    EventBus.getDefault().post(new IntEvent(0, 211));//更新资料详情页面
                    finish();
                } else {
                    ToastUtils.showToastShort("保存失败");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                ToastUtils.showToastShort(volleyError.toString());

            }
        });
        MyApplication.getHttpQueues().add(stringRequest);
    }

    /**
     * 查询所有在售卡by id
     */
    private void findCardsByCardId(String id) {

        String params = id;

        String url = Constants.FIND_CARDS_BY_CARDTYPE + id;

        MyStringRequest request = new MyStringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                Gson gson = new Gson();
                // sellCardsInfoBean = new RequestSellCardsInfoBean();
                RequestSellCardsInfoBean sellCardsInfoBean = gson.fromJson(s, RequestSellCardsInfoBean.class);
                if (sellCardsInfoBean.getSuccess()) {
                    mParamCradList = sellCardsInfoBean.getData();
                    listCardPopup.showPopupWindow();
                } else {
                    ToastUtils.showToastShort(sellCardsInfoBean.getErrorMsg());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                LogUtil.i(volleyError.toString());

            }

        }
        ) {

        };
        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("findCardsByCardId");
        // 设置超时时间
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);

    }

    class StrBean {
        public String branch_id;
        public String type_code;
    }

    private void findParams(final int codetype) {
        StrBean strBean = new StrBean();
        strBean.type_code = codetype + "";
        if (codetype != REGISTER_CHANNEL) {//客户来源不分门店
            Data data = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
            strBean.branch_id = data.getPerson().getDefault_branch();
        } else {
            strBean.branch_id = "0";
        }
        //      LogUtil.i( gson.toJson(strBean).toString());
        MyJsonObjectRequest request = new MyJsonObjectRequest(Request.Method.POST, Constants.FIND_BY_TYPE_CODE, gson.toJson(strBean), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtil.i(jsonObject.toString());

                ParamInfoBean paramInfoBean = gson.fromJson(jsonObject.toString(), ParamInfoBean.class);

                if (paramInfoBean.getSuccess()) {
                    mParamInfoList = paramInfoBean.getData();

                    if (codetype == REGISTER_CHANNEL) {
                        listPopup = new ListPopup(EditPotentialActivity.this);
                        listPopup.showPopupWindow();
                    }
                    if (codetype == LIKE || codetype == TARGET || codetype == MEDICAL) {
                        listPopupMultiSelect = new ListPopupMultiSelect(EditPotentialActivity.this);
                        listPopupMultiSelect.showPopupWindow();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
//        StringRequest request = new StringRequest(Request.Method.POST, Constants.FIND_BY_TYPE_CODE + codetype, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//
//                ToastUtils.showToastShort(volleyError.toString());
//
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> map = new HashMap<String, String>();
//                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));
//
//                return map;
//            }
//        };
        MyApplication.getHttpQueues().add(request);


    }

    // private MyPopupListAdapter myPopupListAdapter = new MyPopupListAdapter(this);


    class ListPopup extends BasePopupWindow {


        Context context;

        public ListPopup(Context context) {
            super(context);
            ListView popup_list = (ListView) findViewById(R.id.popup_list);
            MyPopupListAdapter myPopupListAdapter = new MyPopupListAdapter(context);
            popup_list.setAdapter(myPopupListAdapter);
            this.context = context;
        }

        @Override
        protected Animation initShowAnimation() {
            return null;
        }

        @Override
        public View getClickToDismissView() {
            return getPopupWindowView();
        }

        @Override
        public View onCreatePopupView() {

            //  popupView=View.inflate(context,R.layout.popup_list_consultant,null);
            return createPopupById(R.layout.popup_list_potential);

        }

        @Override
        public View initAnimaView() {
            return null;
        }

        class MyPopupListAdapter extends BaseAdapter {
            private Context context;

            public MyPopupListAdapter(Context context) {
                this.context = context;
            }

            @Override
            public int getCount() {
                return mParamInfoList.size();
            }

            @Override
            public Object getItem(int i) {
                return i;
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup viewGroup) {
                //     LogUtil.i("asdasdjalsdllasjdlk");
                ViewHolder vh = null;
                if (convertView == null) {
                    vh = new ViewHolder();
                    convertView = View.inflate(context, R.layout.listview_item_list_consultant, null);
                    vh.mTextView = (TextView) convertView.findViewById(R.id.item_tx);
                    convertView.setTag(vh);
                } else {
                    vh = (ViewHolder) convertView.getTag();
                }
                vh.mTextView.setText(mParamInfoList.get(position).getData_value());

                vh.mTextView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        switch (codeType) {
                            case 1:
                                tv_guest_aware_way.setText(mParamInfoList.get(position).getData_value());
                                info.setGuest_aware_way(mParamInfoList.get(position).getData_key() + "");
                                break;
                            default:
                                break;
                        }


                        listPopup.dismiss();
                    }
                });

                return convertView;

            }


            class ViewHolder {
                public TextView mTextView;
            }
        }
    }


    class ListCardPopup extends BasePopupWindow {


        Context context;

        public ListCardPopup(Context context) {
            super(context);
            ListView popup_list = (ListView) findViewById(R.id.popup_list);
            MyPopupListAdapter myPopupListAdapter = new MyPopupListAdapter(context);
            popup_list.setAdapter(myPopupListAdapter);
            this.context = context;
        }

        @Override
        protected Animation initShowAnimation() {
            return null;
        }

        @Override
        public View getClickToDismissView() {
            return getPopupWindowView();
        }

        @Override
        public View onCreatePopupView() {

            //  popupView=View.inflate(context,R.layout.popup_list_consultant,null);
            return createPopupById(R.layout.popup_list_potential);

        }

        @Override
        public View initAnimaView() {
            return null;
        }

        class MyPopupListAdapter extends BaseAdapter {
            private Context context;

            public MyPopupListAdapter(Context context) {
                this.context = context;
            }

            @Override
            public int getCount() {
                return mParamCradList.size();
            }

            @Override
            public Object getItem(int i) {
                return i;
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup viewGroup) {
                //     LogUtil.i("asdasdjalsdllasjdlk");
                ViewHolder vh = null;
                if (convertView == null) {
                    vh = new ViewHolder();
                    convertView = View.inflate(context, R.layout.listview_item_list_consultant, null);
                    vh.mTextView = (TextView) convertView.findViewById(R.id.item_tx);
                    convertView.setTag(vh);
                } else {
                    vh = (ViewHolder) convertView.getTag();
                }
                vh.mTextView.setText(mParamCradList.get(position).getName());

                vh.mTextView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        switch (codeType) {
                            case 20:
                                tv_card_type.setText(mParamCradList.get(position).getName());
                                info.setCard_type(tv_card_type.getText().toString());
                                break;
                            default:
                                break;
                        }

                        dismiss();
                    }
                });

                return convertView;

            }


            class ViewHolder {
                public TextView mTextView;
            }
        }
    }


    class ListPopupMultiSelect extends BasePopupWindow {


        Context context;

        public ListPopupMultiSelect(Context context) {
            super(context);
            this.context = context;
            ListView popup_list = (ListView) findViewById(R.id.popup_list);
            popup_list.setAdapter(new MyPopupListMultiSelectAdapter(context));
            findViewById(R.id.btn_ok).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    String s = "";
                    List<String> list = new ArrayList<String>();
                    for (int j = 0; j < mParamInfoList.size(); j++) {
                        if (mParamInfoList.get(j).isCheck()) {
                            if (s == "") {
                                s = mParamInfoList.get(j).getData_value();
                            } else {
                                s = s + "," + mParamInfoList.get(j).getData_value();
                            }
                            list.add(mParamInfoList.get(j).getData_key() + "");

                        }

                    }
                    switch (codeType) {
                        case TARGET:
                            if (!TextUtils.isEmpty(s)) {
                                tv_target.setText(s);
                                info.setTarget_ids(list);
                            } else {
                                tv_target.setText("未选择");
                                info.setTarget_ids(null);
                            }
                            break;
                        case LIKE:
                            if (!TextUtils.isEmpty(s)) {
                                tv_like.setText(s);
                                info.setProject_ids(list);
                            } else {
                                tv_like.setText("未选择");
                                info.setProject_ids(null);
                            }
                            break;
                        case MEDICAL:
                            if (!TextUtils.isEmpty(s)) {
                                tv_medical_history.setText(s);
                                info.setChronic_ids(list);
                            } else {
                                tv_medical_history.setText("未选择");
                                info.setChronic_ids(null);
                            }
                            break;
                        default:
                            break;
                    }

                    dismiss();
                }

            });
        }

        @Override
        protected Animation initShowAnimation() {
            return null;
        }

        @Override
        public View getClickToDismissView() {
            return getPopupWindowView();
        }

        @Override
        public View onCreatePopupView() {

            //  popupView=View.inflate(context,R.layout.popup_list_consultant,null);
            return createPopupById(R.layout.popup_list_potential_multiselect);

        }

        @Override
        public View initAnimaView() {
            return null;
        }


        class MyPopupListMultiSelectAdapter extends BaseAdapter {
            private Context context;

            public MyPopupListMultiSelectAdapter(Context context) {
                this.context = context;
            }

            @Override
            public int getCount() {
                return mParamInfoList.size();
            }

            @Override
            public Object getItem(int i) {
                return i;
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup viewGroup) {
                //     LogUtil.i("asdasdjalsdllasjdlk");
                ViewHolder vh = null;
                if (convertView == null) {
                    vh = new ViewHolder();
                    convertView = View.inflate(context, R.layout.listview_item_list_multiselect, null);
                    vh.mTextView = convertView.findViewById(R.id.item_tx);
                    vh.checkBox = convertView.findViewById(R.id.cb_item);
                    convertView.setTag(vh);
                } else {
                    vh = (ViewHolder) convertView.getTag();
                }
                vh.mTextView.setText(mParamInfoList.get(position).getData_value());
                if (mParamInfoList.get(position).isCheck()) {
                    vh.checkBox.setChecked(true);
                } else {
                    vh.checkBox.setChecked(false);
                }
                vh.checkBox.setText(mParamInfoList.get(position).getData_value());
                vh.checkBox.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mParamInfoList.get(position).isCheck()) {
                            mParamInfoList.get(position).setCheck(false);
                        } else {
                            mParamInfoList.get(position).setCheck(true);
                        }
                        notifyDataSetChanged();
                    }
                });


                return convertView;

            }


            class ViewHolder {
                public TextView mTextView;
                public CheckBox checkBox;
            }
        }
    }


//
//    /**
//     * 查找会籍顾问
//     */
//    private void findConsultant(final String branchId) {
//
//
//        StringRequest request = new StringRequest(Request.Method.POST, Constants.FIND_CONSULTANT_URL, new Response.Listener<String>() {
//
//
//            @Override
//            public void onResponse(String s) {
//                LogUtil.i(s);
//                Gson gson = new Gson();
//                RequestConsultantInfoBean requestConsultantInfoBean = gson.fromJson(s, RequestConsultantInfoBean.class);
//                if (requestConsultantInfoBean.getSuccess()) {
//                    consultantListInfo = requestConsultantInfoBean.getData();
//                    //  LogUtil.i(consultantListInfo.toString());
//                    myPopupListAdapter.notifyDataSetChanged();
//
//                } else {
//                    ToastUtils.showToastShort(requestConsultantInfoBean.getErrorMsg());
//                }
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                ToastUtils.showToastShort(volleyError.toString());
//            }
//        }) {
//
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//                Map<String, String> map = new HashMap<String, String>();
//
//                map.put("branchId", branchId);
//
//                return map;
//
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, null));
//                return map;
//            }
//        };
//        MyApplication.getHttpQueues().add(request);
//
//    }


}
