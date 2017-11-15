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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends BaseFragment {

    GridView mGridView;
    String[] icon_name = {"健身圈", "私人教练", "会所动态", "在线售卡", "课程表", "会所商城", "意见反馈", "会所活动", "我的会员卡"};
    int[] icons = {R.drawable.img_jsq, R.drawable.img_srjl, R.drawable.img_hsdt, R.drawable.img_zxsk
            , R.drawable.img_kcb, R.drawable.img_hssc, R.drawable.img_yjfk, R.drawable.img_hshd, R.drawable.img_hyk};

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

    /***
     * 查找个人动态
     */
    private void findSelfDT() {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.FIND_SELF_DT_MSG, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.i(volleyError.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                LogUtil.i(SPUtils.getString(Constants.MY_USERID,""));
                map.put("id", SPUtils.getString(Constants.MY_USERID,""));//用户id
                map.put("page", 1+"");//页数
                LogUtil.i(map.toString());
                return map;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> hm = new HashMap<String, String>();
                String token = SPUtils.getString(Constants.MY_TOKEN, "");
                hm.put("Authorization", token);
                LogUtil.i(hm.toString());
                return hm;
            }

        };
        MyApplication.getHttpQueues().add(request);

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
                    if (!FileUtil.fileIsExists(Environment.getExternalStorageDirectory() + "/300.jpg")) {
                        ToastUtils.showToastShort("文件不存在");
                        return;
                    }
                    params.put("file", new File(Environment.getExternalStorageDirectory() + "/123.jpg"));

                  params.put("files", new File(Environment.getExternalStorageDirectory() + "/300.jpg"));

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
                 //   final String[] uploadFiles=new String[]{Environment.getExternalStorageDirectory() + "/123.jpg",Environment.getExternalStorageDirectory() + "/300.jpg"};
                    final String[] uploadFiles=new String[]{Environment.getExternalStorageDirectory() + "/123.jpg"};

                    new Thread(new Runnable() {
                     @Override
                     public void run() {
                         upload(uploadFiles,Constants.UPLOAD_FILES_URL);
                     }
                 }).start();

                    break;
                case 5:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Map<String, File> map = new HashMap<>();
                            map.put("file", new File(Environment.getExternalStorageDirectory() + "/123.jpg"));
                            map.put("file1", new File(Environment.getExternalStorageDirectory() + "/300.jpg"));
                            try {
                                post(Constants.UPLOAD_FILES_URL,null,map);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();


                    break;
                case 6:
                    break;
                case 7:
                    findSelfDT();
                    break;
                case 8:
                    LogUtil.i(SPUtils.getString(Constants.MY_USERID,null));
                    LogUtil.i(SPUtils.getString(Constants.MY_TOKEN,null));
                    break;


                default:
                    break;
            }


        }
    }

    private void upload(String[] uploadFiles, String actionUrl) {
        String end = "/r/n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            URL url = new URL(actionUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            // 发送POST请求必须设置如下两行
            con.setRequestProperty("Authorization", SPUtils.getString(Constants.MY_TOKEN,null));
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestMethod("POST");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
            DataOutputStream ds =
                    new DataOutputStream(con.getOutputStream());
            for (int i = 0; i < uploadFiles.length; i++) {
                String uploadFile = uploadFiles[i];
                String filename = uploadFile.substring(uploadFile.lastIndexOf("//") + 1);
                ds.writeBytes(twoHyphens + boundary + end);
                ds.writeBytes("Content-Disposition: form-data;");
              //  ds.writeBytes("name="+"\"file\""+i+";");
                ds.writeBytes(" name="+"\"files\";");
                ds.writeBytes(" filename="+"\""+filename+"\"");
                ds.writeBytes(end);

               // ds.writeBytes(end);
//                ds.writeBytes("Content-Disposition: form-data;"+"name=\"file\"" + i + "/";filename=/"" +
//                        filename + "/"" + end);


                FileInputStream fStream = new FileInputStream(uploadFile);
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];
                int length = -1;
                while ((length = fStream.read(buffer)) != -1) {
                    ds.write(buffer, 0, length);
                }
                ds.writeBytes(end);
              /* close streams */
                fStream.close();
            }
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            ds.flush();
            // 定义BufferedReader输入流来读取URL的响应
            InputStream is = con.getInputStream();
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            String s = b.toString();
            LogUtil.i(s);
            if (s.contains("successfully")) {
                // for (int i = 1; i < 5; i++) {
                int beginIndex = s.indexOf("url =") + 5;
                int endIndex = s.indexOf("/n", beginIndex);
                String urlStr = s.substring(beginIndex, endIndex).trim();
                System.out.println(urlStr);
                // }
            }
            ds.close();
        } catch (Exception e) {
        }
    }


    /**
     * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
     *
     * @param url Service net address
     * @param params text content
     * @param files pictures
     * @return String result of Service response
     * @throws IOException
     */
    public static String post(String url, Map<String, String> params, Map<String, File> files)
            throws IOException {
        String BOUNDARY = UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";


        URL uri = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
        conn.setReadTimeout(10 * 1000); // 缓存的最长时间
        conn.setDoInput(true);// 允许输入
        conn.setDoOutput(true);// 允许输出
        conn.setUseCaches(false); // 不允许使用缓存
        conn.setRequestMethod("POST");
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Charsert", "UTF-8");
        conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);


        // 首先组拼文本类型的参数
        StringBuilder sb = new StringBuilder();
        if (params!=null){
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINEND);
                sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
                sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
                sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
                sb.append(LINEND);
                sb.append(entry.getValue());
                sb.append(LINEND);
            }
        }



        DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
        outStream.write(sb.toString().getBytes());
        // 发送文件数据
        if (files != null)
            for (Map.Entry<String, File> file : files.entrySet()) {
                StringBuilder sb1 = new StringBuilder();
                sb1.append(PREFIX);
                sb1.append(BOUNDARY);
                sb1.append(LINEND);
                sb1.append("Content-Disposition: form-data; name=\"files\"; filename=\""
                        + file.getValue().getName() + "\"" + LINEND);
                sb1.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINEND);
                sb1.append(LINEND);
                outStream.write(sb1.toString().getBytes());


                InputStream is = new FileInputStream(file.getValue());
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }


                is.close();
                outStream.write(LINEND.getBytes());
            }


        // 请求结束标志
        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
        outStream.write(end_data);
        outStream.flush();
        // 得到响应码
        int res = conn.getResponseCode();
        InputStream in = conn.getInputStream();
        StringBuilder sb2 = new StringBuilder();
        if (res == 200) {
            int ch;
            while ((ch = in.read()) != -1) {
                sb2.append((char) ch);

            }
            LogUtil.i(sb2.toString());
        }
        outStream.close();
        conn.disconnect();
        return sb2.toString();
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
