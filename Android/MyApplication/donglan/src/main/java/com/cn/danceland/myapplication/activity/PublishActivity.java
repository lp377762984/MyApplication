package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.location.LocationClient;
import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.PublishBean;
import com.cn.danceland.myapplication.bean.RequsetDynInfoBean;
import com.cn.danceland.myapplication.bean.RootBean;
import com.cn.danceland.myapplication.bean.UpImagesBean;
import com.cn.danceland.myapplication.db.DBData;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.PictureUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.cn.danceland.myapplication.utils.UpLoadUtils;
import com.cn.danceland.myapplication.utils.multipartrequest.FileUtil;
import com.cn.danceland.myapplication.utils.multipartrequest.MultipartRequest;
import com.cn.danceland.myapplication.utils.multipartrequest.MultipartRequestParams;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by feng on 2017/10/23.
 */

public class PublishActivity extends Activity {
    TextView publish_cancel;
    TextView publish_ok;
    EditText publish_status;
    RelativeLayout publish_photo;
    TextView publish_location;
    TextView publish_share1;
    ArrayList<String> arrayList;
    GridView grid_view;
    String location="";
    TextView location_img;
    Map<String,File> arrayFileMap;
    String stringstatus = "";
    //LocationClient mLocationClient;
    Gson gson;
    RequestQueue queue;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);


        queue = Volley.newRequestQueue(PublishActivity.this);
        gson = new Gson();
        //mLocationClient = ((MyApplication) getApplication()).locationClient;
        initView();
        setOnclick();
        initdata();
//
    }

    private void initdata() {

    }

    private void setOnclick() {
        publish_photo.setOnClickListener(onClickListener);
        publish_location.setOnClickListener(onClickListener);
        location_img.setOnClickListener(onClickListener);
        publish_ok.setOnClickListener(onClickListener);
        publish_cancel.setOnClickListener(onClickListener);
    }

    private void initView() {
        publish_cancel = findViewById(R.id.publish_cancel);
        publish_ok = findViewById(R.id.publish_ok);

        location_img = findViewById(R.id.location_img);
        publish_status = findViewById(R.id.publish_status);
        publish_photo = findViewById(R.id.publish_photo);
        publish_location = findViewById(R.id.publish_location);
        publish_share1 = findViewById(R.id.publish_share1);
        grid_view = findViewById(R.id.grid_view);

        grid_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PublishActivity.this,ImagesActivity.class);
                startActivityForResult(intent,0);
            }
        });

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.publish_photo:
                    Intent intent = new Intent(PublishActivity.this,ImagesActivity.class);
                    startActivityForResult(intent,0);
                    break;
                case R.id.location_img:
                    Intent intent1 = new Intent(PublishActivity.this,LocationActivity.class);
                    startActivityForResult(intent1,1);
                    break;
                case R.id.publish_location:
                    Intent intent2 = new Intent(PublishActivity.this,LocationActivity.class);
                    startActivityForResult(intent2,1);
                    break;
                case R.id.publish_ok:
                    //Intent intent3 = new Intent(PublishActivity.this,S);
                    stringstatus = publish_status.getText().toString();
                    MultipartRequestParams params = new MultipartRequestParams();
                    arrayFileMap = new HashMap<String,File>();
                    if(arrayList!=null&&arrayList.size()>0){
                        File[] files = new File[arrayList.size()];
                        for (int i =0;i<arrayList.size();i++){
                            File file = new File(arrayList.get(i));
                            arrayFileMap.put(i+"",file);
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String s=   UpLoadUtils.postUPloadIamges(Constants.UPLOAD_FILES_URL,null,arrayFileMap);
                                    UpImagesBean upImagesBean = gson.fromJson(s, UpImagesBean.class);
                                    List<UpImagesBean.Data> beanList = upImagesBean.getData();
                                    ArrayList<String> arrImgUrl = new ArrayList<String>();
                                    for(int k = 0;k<beanList.size();k++){
                                        arrImgUrl.add(beanList.get(k).getImgUrl());
                                    }
                                    PublishBean publishBean = new PublishBean();
                                    publishBean.setContent(stringstatus);
                                    publishBean.setPublishPlace(location);
                                    if(arrImgUrl.size()>0){
                                        publishBean.setImgList(arrImgUrl);
                                    }
                                    String strBean = gson.toJson(publishBean);
                                    commitUrl(strBean);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }).start();
                    }else{
                        PublishBean publishBean = new PublishBean();
                        publishBean.setContent(stringstatus);
                        publishBean.setPublishPlace(location);
                        String strBean = gson.toJson(publishBean);
                        try {
                            commitUrl(strBean);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    finish();
                    break;
                case R.id.publish_cancel:
                    finish();
                    break;
            }
        }
    };

    public void commitUrl(final String str) throws JSONException {

        JSONObject jsonObject = new JSONObject(str);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,Constants.SAVE_DYN_MSG,jsonObject,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                RootBean rootBean = gson.fromJson(jsonObject.toString(), RootBean.class);
                if("true".equals(rootBean.success)){
                    ToastUtils.showToastShort("发布成功！");
                }else{
                    ToastUtils.showToastShort("发布失败！请检查网络连接");
                }
                //LogUtil.e("zzf",jsonObject.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> map  = new HashMap<String,String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN,""));
                return map;
            }
        };
        queue.add(stringRequest);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==0){
            if(data!=null){
                arrayList = data.getStringArrayListExtra("arrPath");
                if(arrayList!=null&&arrayList.size()!=0){
                    publish_photo.setVisibility(View.GONE);
                }else{
                    publish_photo.setVisibility(View.VISIBLE);
                }
                grid_view.setAdapter(new SmallGridAdapter(PublishActivity.this,arrayList));
            }
        }else if(resultCode==1){
                location = data.getStringExtra("location");
                publish_location.setText(location);
        }

    }

    public class SmallGridAdapter extends BaseAdapter{

        LayoutInflater mInflater;
        Context context;
        ArrayList<String> arrayList;

        SmallGridAdapter(Context context,ArrayList<String> asList){
            this.context = context;
            arrayList = asList;
            mInflater = LayoutInflater.from(context);
        }


        @Override
        public int getCount() {
            return arrayList.size();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder=null;
            if(convertView==null){
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.images_item, null);
                viewHolder.img = convertView.findViewById(R.id.image_item);
                viewHolder.item_select = convertView.findViewById(R.id.item_select);
                viewHolder.item_select.setVisibility(View.GONE);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            viewHolder.img.setMaxHeight(windowManager.getDefaultDisplay().getWidth()/4);
            viewHolder.img.setMaxWidth(windowManager.getDefaultDisplay().getWidth()/4);
            Glide.with(context).load(arrayList.get(position)).into(viewHolder.img);
            //viewHolder.img.setImageBitmap(PictureUtil.getSmallBitmap(arrayList.get(position),windowManager.getDefaultDisplay().getWidth()/4,windowManager.getDefaultDisplay().getWidth()/4));

            return convertView;
        }
    }

    class ViewHolder{
        ImageView img,item_select;
    }

}
