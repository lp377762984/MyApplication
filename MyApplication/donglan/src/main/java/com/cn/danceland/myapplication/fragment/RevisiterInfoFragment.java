package com.cn.danceland.myapplication.fragment;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.request.RequestOptions;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.activity.AddRevisiterRecordActivity;
import com.cn.danceland.myapplication.activity.EditPotentialActivity;
import com.cn.danceland.myapplication.bean.PotentialInfo;
import com.cn.danceland.myapplication.bean.RequsetPotentialInfoBean;
import com.cn.danceland.myapplication.evntbus.IntEvent;
import com.cn.danceland.myapplication.utils.CallLogUtils;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shy on 2018/1/11 17:09
 * Email:644563767@qq.com
 */


public class RevisiterInfoFragment extends BaseFragmentEventBus {
    private String id;
    private Gson gson = new Gson();
    private RequsetPotentialInfoBean potentialInfoBean;
    private PotentialInfo info;
    private ScaleRatingBar sr_fitness_level;//健身指数
    private ScaleRatingBar sr_follow_level;//关注程度

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    setview();
                    break;
                default:
                    break;
            }
        }
    };
    private ImageView iv_avatar;
    private ImageView iv_sex;
    private TextView tv_name;
    private TextView tv_lasttime;
    private TextView tv_weixin_no;
    private TextView tv_company;
    private TextView tv_address;
    private TextView tv_email;
    private TextView tv_guest_aware_way;
    private TextView tv_target;
    private TextView tv_card_type;
    private TextView tv_like;
    private TextView tv_medical_history;
    private TextView tv_remark;
    private TextView tv_phone;

    private ImageView iv_callphone;
    private ImageView iv_send_msg;
    private TextView tv_nationality;
    private TextView tv_remark1;
    private TextView tv_certificate_type;
    private TextView tv_emergency_name;
    private TextView tv_certificate_no;
    private TextView tv_emergency_phone;
    private TextView tv_birthday;
    private TextView tv_height;
    private TextView tv_weight;
    private TextView tv_branch_name;
    private TextView tv_teach_name;
    private TextView tv_admin_name;
    private TextView tv_guest_recom;
    private TextView tv_final_teach_name;
    private TextView tv_final_admin_name;
    private LinearLayout ll_final_admin;
    private LinearLayout ll_admin;

    public void setview() {
        if (!TextUtils.isEmpty(info.getSelf_avatar_url())) {
            RequestOptions options = new RequestOptions().placeholder(R.drawable.img_my_avatar);
            String S = info.getSelf_avatar_url();
            //    Glide.with(mActivity).load(S).apply(options).into(iv_avatar);
        }

        if (TextUtils.equals(info.getGender(), "1")) {
            iv_sex.setImageResource(R.drawable.img_sex1);
        }
        if (TextUtils.equals(info.getGender(), "2")) {
            iv_sex.setImageResource(R.drawable.img_sex2);
        }
        tv_phone.setText(info.getPhone_no());

        if (TextUtils.isEmpty(info.getNick_name())) {
            tv_name.setText(info.getCname());
        } else {
            tv_name.setText(info.getCname() + "(" + info.getNick_name() + ")");
        }
        tv_lasttime.setText("最后维护时间：" + info.getLast_time());
        tv_weixin_no.setText(info.getWeichat_no());
        tv_company.setText(info.getCompany());
        tv_address.setText(info.getAddress());
        tv_email.setText(info.getMail());
        tv_guest_aware_way.setText(info.getGuest_way());
        if (!TextUtils.isEmpty(info.getFitness_level())) {
            sr_fitness_level.setRating(Integer.parseInt(info.getFitness_level()));
        }
        if (!TextUtils.isEmpty(info.getFollow_level())) {
            sr_follow_level.setRating(Integer.parseInt(info.getFollow_level()));
        }


        tv_card_type.setText(info.getCard_type());
        tv_remark.setText(info.getRemark());


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
                LogUtil.i(j + "");
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
        iv_callphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(info.getPhone_no());
            }
        });
        iv_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSendSMSTo(info.getPhone_no(), "");
            }
        });


        tv_nationality.setText(info.getNationality());
        tv_certificate_type.setText(info.getCertificate_type());
        tv_certificate_no.setText(info.getIdentity_card());
        tv_emergency_name.setText(info.getEmergency_name());
        tv_emergency_phone.setText(info.getEmergency_name());
        tv_birthday.setText(info.birthday);
        tv_height.setText(info.getHeight());
        tv_weight.setText(info.getWeight());

        tv_branch_name.setText(info.getBranch_name());
        tv_teach_name.setText(info.getTeach_name());
        tv_admin_name.setText(info.getAdmin_name());
        tv_guest_recom.setText(info.getGuest_recom());

        tv_final_admin_name.setText(info.getFinal_admin_name());
        tv_final_teach_name.setText(info.getFinal_teach_name());
        if (TextUtils.equals(info.getAuth(), "2")) {
        ll_final_admin.setVisibility(View.VISIBLE);
            ll_admin.setVisibility(View.GONE);
        }


    }

    /**
     * 调起系统发短信功能
     *
     * @param phoneNumber
     * @param message
     */
    public void doSendSMSTo(String phoneNumber, String message) {
        if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
            intent.putExtra("sms_body", message);
            startActivity(intent);
        }
    }

    @Override
    public View initViews() {
        View v = View.inflate(mActivity, R.layout.fragment_revisiter_details, null);
        iv_avatar = v.findViewById(R.id.iv_avatar);
        iv_callphone = v.findViewById(R.id.iv_callphone);
        iv_send_msg = v.findViewById(R.id.iv_send_msg);
        tv_phone = v.findViewById(R.id.tv_phone);
        iv_sex = v.findViewById(R.id.iv_sex);
        tv_name = v.findViewById(R.id.tv_name);
        tv_lasttime = v.findViewById(R.id.tv_lasttime);
        tv_weixin_no = v.findViewById(R.id.tv_weixin_no);
        tv_company = v.findViewById(R.id.tv_company);
        tv_address = v.findViewById(R.id.tv_address);
        tv_email = v.findViewById(R.id.tv_email);
        tv_guest_aware_way = v.findViewById(R.id.tv_guest_aware_way);
        sr_fitness_level = v.findViewById(R.id.sr_fitness_level);
        sr_follow_level = v.findViewById(R.id.sr_follow_level);
        tv_target = v.findViewById(R.id.tv_target);
        tv_card_type = v.findViewById(R.id.tv_card_type);
        tv_like = v.findViewById(R.id.tv_like);
        tv_medical_history = v.findViewById(R.id.tv_medical_history);
        tv_remark = v.findViewById(R.id.tv_remark);
        tv_nationality = v.findViewById(R.id.tv_nationality);
        tv_certificate_type = v.findViewById(R.id.tv_certificate_type);
        tv_certificate_no = v.findViewById(R.id.tv_certificate_no);
        tv_emergency_name = v.findViewById(R.id.tv_emergency_name);
        tv_emergency_phone = v.findViewById(R.id.tv_emergency_phone);
        tv_birthday = v.findViewById(R.id.tv_birthday);
        tv_height = v.findViewById(R.id.tv_height);
        tv_weight = v.findViewById(R.id.tv_weight);

        tv_branch_name = v.findViewById(R.id.tv_branch_name);
        tv_teach_name = v.findViewById(R.id.tv_teach_name);
        tv_admin_name = v.findViewById(R.id.tv_admin_name);
        tv_guest_recom = v.findViewById(R.id.tv_guest_recom);

        tv_final_teach_name = v.findViewById(R.id.tv_final_teach_name);
        tv_final_admin_name = v.findViewById(R.id.tv_final_admin_name);
        ll_final_admin = v.findViewById(R.id.ll_final_admin);
        ll_admin = v.findViewById(R.id.ll_admin);

        v.findViewById(R.id.iv_more).setOnClickListener(this);
        return v;
    }

    @Override
    public void initDta() {

        id = getArguments().getString("id");

        find_by_id_potential(id);

    }

    @Override
    public void onEventMainThread(IntEvent event) {

        switch (event.getEventCode()) {
            case 211://刷新页面
                find_by_id_potential(id);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_more:
                showListDialog();
                break;
            default:
                break;
        }
    }


    /**
     * 是否添加回访记录
     */
    public void showDialogRrcord() {
        final ContentResolver cr;
        cr = getActivity().getContentResolver();
        AlertDialog.Builder dialog =
                new AlertDialog.Builder(mActivity);
        dialog.setTitle("提示");
        dialog.setMessage("是否读取通话记录，并添加到回访记录");
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Bundle bundle = new Bundle();
                bundle.putString("time", CallLogUtils.getLastCallHistoryDuration(null, cr) + "");
                bundle.putString("id", info.getId());
                bundle.putString("auth", info.getAuth());
                bundle.putString("member_name", info.getCname());
                bundle.putString("member_no", info.getMember_no());
                startActivity(new Intent(mActivity, AddRevisiterRecordActivity.class)
                        .putExtras(bundle));

            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }


    private void showListDialog() {
        final String[] items = {"编辑资料"};
        //final String[] items = {"编辑资料", "转让", "放弃维护"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(mActivity);
        //listDialog.setTitle("我是一个列表Dialog");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case 0:
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("info", potentialInfoBean.getData());
                        startActivity(new Intent(mActivity, EditPotentialActivity.class).putExtras(bundle));

                    case 1:


                        break;
                    case 2:


                        break;
                    default:
                        break;
                }
            }
        });
        listDialog.show();
    }

    /**
     * 提示
     */
    private void showDialog(final String phoneNo) {

        AlertDialog.Builder dialog =
                new AlertDialog.Builder(mActivity);
        dialog.setTitle("提示");
        dialog.setMessage("是否呼叫" + phoneNo);
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                call(phoneNo);

                // call("13436907535");
                showDialogRrcord();

            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }

    /**
     * 调用拨号功能
     *
     * @param phone 电话号码
     */
    private void call(String phone) {

        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        //   Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phone));
        //   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }


    private void find_by_id_potential(String id) {


        StringRequest request = new StringRequest(Request.Method.GET, Constants.FIND_BY_ID_POTENTIAL + id, new Response.Listener<String>() {


            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                potentialInfoBean = new RequsetPotentialInfoBean();
                potentialInfoBean = gson.fromJson(s, RequsetPotentialInfoBean.class);
                //    LogUtil.i(potentialInfoBean.toString());
                if (potentialInfoBean.getSuccess()) {
                    //    LogUtil.i(potentialInfoBean.getData().toString());
                    info = potentialInfoBean.getData();
                    //   LogUtil.i(info.toString());
                    Message message = Message.obtain();
                    message.what = 1;
                    handler.sendMessage(message);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                ToastUtils.showToastShort(volleyError.toString());

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));

                return map;
            }
        };
        MyApplication.getHttpQueues().add(request);
    }


}
