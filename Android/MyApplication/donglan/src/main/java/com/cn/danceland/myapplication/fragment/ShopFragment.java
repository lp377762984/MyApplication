package com.cn.danceland.myapplication.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.activity.SellCardActivity;
import com.cn.danceland.myapplication.bean.RequestInfoBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.cn.danceland.myapplication.utils.multipartrequest.FileUtil;
import com.cn.danceland.myapplication.utils.multipartrequest.HttpUrlConnectionOpts;
import com.cn.danceland.myapplication.utils.multipartrequest.MultipartRequest;
import com.cn.danceland.myapplication.utils.multipartrequest.MultipartRequestParams;
import com.google.gson.Gson;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends BaseFragment {

    GridView mGridView;
    String[] icon_name = {"健身圈", "私人教练", "会所动态", "在线售卡", "课程表", "会所商城", "意见反馈", "会所活动","我的会员卡"};
    int[] icons = {R.drawable.img_jsq, R.drawable.img_srjl, R.drawable.img_hsdt, R.drawable.img_zxsk
            , R.drawable.img_kcb, R.drawable.img_hssc, R.drawable.img_yjfk, R.drawable.img_hshd,R.drawable.img_hyk};

    @Override
    public View initViews() {
        View v = View.inflate(mActivity, R.layout.fragment_shop, null);

        mGridView = v.findViewById(R.id.gridview);
        mGridView.setAdapter(new MyAdapter());
        mGridView.setOnItemClickListener(new MyOnItemClickListener());
        LinearLayout ll_top = v.findViewById(R.id.ll_top);
        ll_top.setBackgroundColor(Color.WHITE);
        ll_top.getBackground().setAlpha(80);
        v.findViewById(R.id.ibtn_call).setOnClickListener(this);
        v.findViewById(R.id.ibtn_gps).setOnClickListener(this);


        return v;
    }

    @Override
    public void initDta() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_call:

                showDialog();
                break;
            case R.id.ibtn_gps:

                ToastUtils.showToastShort("显示位置");

                break;
            default:
                break;
        }
    }


    /**
     * 提示
     */
    private void showDialog() {
        AlertDialog.Builder dialog =
                new AlertDialog.Builder(mActivity);
        dialog.setTitle("提示");
        dialog.setMessage("是否呼叫" + "010-12345678");
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                call("010-12345678");
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
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }


    class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            ToastUtils.showToastShort(icon_name[i]);
            switch (i) {
                case 0:
                    //     logOut();

                    break;
                case 1:
                    MultipartRequestParams params = new MultipartRequestParams();
                    //   params.put("userName",username);
                    if (!FileUtil.fileIsExists(Environment.getExternalStorageDirectory() + "/300.jpg")) {
                        ToastUtils.showToastShort("文件不存在");
                        return;
                    }

//                    List<File> files = new ArrayList<File>();
//                    files.add(new File(Environment.getExternalStorageDirectory() + "/123.jpg"));
//                    files.add(new File(Environment.getExternalStorageDirectory() + "/123.jpg"));
//


                    params.put("file", new File(Environment.getExternalStorageDirectory() + "/123.jpg"));

                    params.put("myfiles", new File(Environment.getExternalStorageDirectory() + "/300.jpg"));

                    params.put("files", new File(Environment.getExternalStorageDirectory() + "/123.jpg"));
                    MultipartRequest request = new MultipartRequest(Request.Method.POST, params, Constants.UPLOAD_FILES_URL, new Response.Listener<String>() {
                        //     LogUtil.i(SPUtils.getString(Constants.MY_TOKEN, null));

                        //     MultipartRequest request = new MultipartRequest(Request.Method.POST, params, "http://192.168.1.94:8003/user/uploadFile", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {

                            LogUtil.i(s);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            LogUtil.i(volleyError.toString());
                        }
                    }
                    ) {

                    };

                    MyApplication.getHttpQueues().add(request);

                    break;
                case 2:

                    HttpUrlConnectionOpts opts = new HttpUrlConnectionOpts();
                    Map<String, File> map = new HashMap<>();
                    map.put("file", new File(Environment.getExternalStorageDirectory() + "/123.jpg"));
                    map.put("file1", new File(Environment.getExternalStorageDirectory() + "/300.jpg"));
                    //    opts.addIfParameter( opts.createMultiPartConnection(Constants.UPLOAD_FILES_URL),map);
                    opts.fileUpLoad(Constants.UPLOAD_FILES_URL, map);
                    break;
                case 3://在线售卡
                    startActivity(new Intent(mActivity, SellCardActivity.class));
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    break;
                case 8:
                    break;


                default:
                    break;
            }


        }
    }


    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return icon_name.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return icons.length;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            view = View.inflate(mActivity, R.layout.gridview_item_shop, null);
            TextView tv_dcs = view.findViewById(R.id.tv_dcs);
            ImageView ibtn = view.findViewById(R.id.ibtn);
            tv_dcs.setText(icon_name[i]);

            ibtn.setBackgroundResource(icons[i]);
            return view;
        }
    }


    /***
     * 退出登录
     */
    private void logOut() {

        String url = "http://192.168.1.119:8888/user/logOut";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s);

                Gson gson = new Gson();
                RequestInfoBean requestInfoBean = new RequestInfoBean();
                requestInfoBean = gson.fromJson(s, RequestInfoBean.class);
                if (requestInfoBean.getSuccess()) {
                    //成功
                    //    startActivity(new Intent(mActivity, LoginActivity.class));
                    SPUtils.setBoolean(Constants.ISLOGINED, false);
                    //退出主页面
                    //  HomeActivity.instance.finish();
                    //   mActivity.finish();
                } else {
                    //失败
                    ToastUtils.showToastShort(requestInfoBean.getErrorMsg());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToastShort("请求失败，请查看网络连接");
                LogUtil.i(volleyError.toString() + "Error: " + volleyError
                        + ">>" + volleyError.networkResponse.statusCode
                        + ">>" + volleyError.networkResponse.data
                        + ">>" + volleyError.getCause()
                        + ">>" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put("id", SPUtils.getString(Constants.MY_USERID, null));

                // map.put("romType", "0");
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {


                Map<String, String> map = new HashMap<String, String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, null));
                return map;
            }
        };

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("login");
        // 设置超时时间
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);
    }

}
