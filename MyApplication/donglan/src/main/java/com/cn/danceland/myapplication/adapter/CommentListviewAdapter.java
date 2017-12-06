package com.cn.danceland.myapplication.adapter;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.activity.UserHomeActivity;
import com.cn.danceland.myapplication.bean.RequestInfoBean;
import com.cn.danceland.myapplication.bean.RequstCommentInfoBean;
import com.cn.danceland.myapplication.evntbus.EventConstants;
import com.cn.danceland.myapplication.evntbus.IntEvent;
import com.cn.danceland.myapplication.evntbus.StringEvent;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.TimeUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by shy on 2017/11/21 09:36
 * Email:644563767@qq.com
 */


public class CommentListviewAdapter extends BaseAdapter {

    public List<RequstCommentInfoBean.Items> data = new ArrayList<RequstCommentInfoBean.Items>();
    private LayoutInflater mInflater;
    private Context context;
    private String msgId;

    public CommentListviewAdapter(List<RequstCommentInfoBean.Items> data, Context context, String msgId) {
        this.data = data;
        this.context = context;
        this.msgId = msgId;
        mInflater = LayoutInflater.from(context);
    }

    public void addFirst(RequstCommentInfoBean.Items bean) {
        data.add(0, bean);
    }

    public void setData(List<RequstCommentInfoBean.Items> data) {
        this.data = data;
    }

    //增加数据
    public void addLastList(List<RequstCommentInfoBean.Items> bean) {

        data.addAll(bean);
        // LogUtil.i(data.toString());
    }


    @Override
    public int getCount() {
        return data.size();
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
    public View getView(final int position, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.listview_item_comment, null);
            viewHolder.iv_avatar = view.findViewById(R.id.iv_avatar);
            viewHolder.tv_nickname = view.findViewById(R.id.tv_nickname);
            viewHolder.tv_time = view.findViewById(R.id.tv_time);
            viewHolder.tv_content = view.findViewById(R.id.tv_content);
            viewHolder.ll_item = view.findViewById(R.id.ll_item);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        RequestOptions options = new RequestOptions().placeholder(R.drawable.img_my_avatar);
        Glide.with(context)
                .load(data.get(position).getSelfUrl())
                .apply(options)
                .into(viewHolder.iv_avatar);
        viewHolder.tv_nickname.setText(data.get(position).getNickName());
        viewHolder.tv_time.setText(TimeUtils.timeLogic(data.get(position).getTime()));


        if (!TextUtils.isEmpty(data.get(position).getReplyUser())) {
            //   LogUtil.i("要回复的人的id="+data.get(position).getReplyUser());
            int pos = position;
            viewHolder.tv_content.setText(getClickableSpan(data.get(position).getReplyNickName(), data.get(position).getContent(), pos));
            viewHolder.tv_content.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            //   LogUtil.i("要回复的人的id="+data.get(position).getReplyUser());
            viewHolder.tv_content.setText(data.get(position).getContent());
        }


        // LogUtil.i(data.get(position).getContent()+"!!!!!!!!!!!!!!!!!!!");
        viewHolder.tv_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, UserHomeActivity.class).putExtra("id", data.get(position).getReplyUserId()));
            }
        });
        viewHolder.iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, UserHomeActivity.class).putExtra("id", data.get(position).getReplyUserId()));
            }
        });

        viewHolder.ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = position;
                if (TextUtils.equals(data.get(position).getReplyUserId(), SPUtils.getString(Constants.MY_USERID, null))) {
                    //如果动态评论是自己发布的
                    showListDialog_self(pos);
                } else {
                    showListDialog(pos);
                }


            }
        });

        return view;
    }

    class ViewHolder {
        TextView tv_nickname;//昵称
        ImageView iv_avatar;//头像
        TextView tv_time;//时间
        TextView tv_content;//内容
        LinearLayout ll_item;
    }

    private void showListDialog(final int pos) {
        final String[] items = {"回复", "复制内容", "举报"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(context);
        //listDialog.setTitle("我是一个列表Dialog");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case 0:

                        EventBus.getDefault().post(new IntEvent(pos, 8001));
                        break;
                    case 1:

                        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        // 将文本内容放到系统剪贴板里。
                        cm.setText(data.get(pos).getContent());
                        ToastUtils.showToastShort("复制成功");

                        break;
                    case 2:
                        ToastUtils.showToastShort("已举报");
                        break;
                    default:
                        break;
                }
            }
        });
        listDialog.show();
    }

    private void showListDialog_self(final int pos) {
        final String[] items = {"复制内容", "删除评论"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(context);
        //listDialog.setTitle("我是一个列表Dialog");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case 0:

                        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        // 将文本内容放到系统剪贴板里。
                        cm.setText(data.get(pos).getContent());
                        ToastUtils.showToastShort("复制成功");

                        break;

                    case 1:
                        //确认一下
                        showConfirmDialog(pos);


                        break;

                    default:
                        break;
                }
            }
        });
        listDialog.show();
    }

    /**
     * 确认对话
     */
    private void showConfirmDialog(final int pos) {
        AlertDialog.Builder dialog =
                new AlertDialog.Builder(context);
        //   dialog.setTitle("提示");
        dialog.setMessage("是否删除该评论");
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sendCommentReply(msgId, data.get(pos).getId(), pos);

            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }


    private SpannableString getClickableSpan(String userNickName, String content, final int pos) {
        View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "Click Success",
//                        Toast.LENGTH_SHORT).show();

                context.startActivity(new Intent(context, UserHomeActivity.class).putExtra("id", data.get(pos).getReplyUser()));
            }
        };
        StringBuffer sb = new StringBuffer();
        String s1 = new String("回复");
        String s2 = new String(userNickName);
        String s3 = new String(":" + content);
        sb.append(s1 + s2 + s3);

        ForegroundColorSpan FC = new ForegroundColorSpan(Color.parseColor("#61C5E7"));
        SpannableString spanableInfo = new SpannableString(sb);

        int start = s1.length();
        int end = s2.length() + start;

        spanableInfo.setSpan(new Clickable(l), start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanableInfo.setSpan(FC, start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanableInfo;
    }


    /**
     * 内部类，用于截获点击富文本后的事件
     *
     * @author pro
     */
    class Clickable extends ClickableSpan implements View.OnClickListener {
        private final View.OnClickListener mListener;

        public Clickable(View.OnClickListener l) {
            mListener = l;
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            //ds.setColor(ds.linkColor);
            //   ds.setColor(ds.linkColor);
            ds.setUnderlineText(false);    //去除超链接的下划线
        }
    }

    /**
     * 删除评论
     *
     * @param msgId//动态id
     * @param id//评论id
     */
    private void sendCommentReply(final String msgId, final String id, final int pos) {

        String Params = Constants.SEND_COMMENT_REPLY + "/" + id + "/" + msgId;

        final StringRequest request = new StringRequest(Request.Method.DELETE, Params, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s);

                Gson gson = new Gson();
                RequestInfoBean requestInfoBean = new RequestInfoBean();
                requestInfoBean = gson.fromJson(s, RequestInfoBean.class);
                if (requestInfoBean.getSuccess()) {
                    ToastUtils.showToastShort("删除成功");
                    data.remove(pos);
                    notifyDataSetChanged();
                    EventBus.getDefault().post(new StringEvent(msgId, EventConstants.DEL_COMMENT));

                } else {
                    ToastUtils.showToastShort("删除失败：" + requestInfoBean.getErrorMsg());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                LogUtil.i(volleyError.toString());
            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> hm = new HashMap<String, String>();
                String token = SPUtils.getString(Constants.MY_TOKEN, "");
                hm.put("Authorization", token);
                return hm;
            }

        };
        MyApplication.getHttpQueues().add(request);
    }

}