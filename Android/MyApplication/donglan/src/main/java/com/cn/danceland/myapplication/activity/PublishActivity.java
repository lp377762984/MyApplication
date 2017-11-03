package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.baidu.location.LocationClient;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.PictureUtil;
import com.cn.danceland.myapplication.utils.UpLoadUtils;

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
    String location;
    TextView location_img;
    Map<String,File> arrayFileMap;
    //LocationClient mLocationClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        //mLocationClient = ((MyApplication) getApplication()).locationClient;
        initView();
        setOnclick();
        initdata();
        
    }

    private void initdata() {

    }

    private void setOnclick() {
        publish_photo.setOnClickListener(onClickListener);
        publish_location.setOnClickListener(onClickListener);
        location_img.setOnClickListener(onClickListener);
        publish_ok.setOnClickListener(onClickListener);
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
                    Intent intent1 = new Intent(PublishActivity.this,MapActivity.class);
                    startActivityForResult(intent1,1);
                    break;
                case R.id.publish_location:
                    Intent intent2 = new Intent(PublishActivity.this,MapActivity.class);
                    startActivityForResult(intent2,1);
                    break;
                case R.id.publish_ok:
                    arrayFileMap = new HashMap<String,File>();
                    for (int i =0;i<arrayList.size();i++){
                        File file = new File(arrayList.get(i));
                        arrayFileMap.put(i+"",file);
                    }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String str = UpLoadUtils.postUpLoadFile("http://192.168.1.94:8003/appDynMsg/uploadImage",null,arrayFileMap);
                                }catch (IOException e){
                                }
                            }
                        }).start();
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==0){
            if(data!=null){
                arrayList = data.getStringArrayListExtra("arrPath");
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
            viewHolder.img.setImageBitmap(PictureUtil.getSmallBitmap(arrayList.get(position),windowManager.getDefaultDisplay().getWidth()/4,windowManager.getDefaultDisplay().getWidth()/4));

            return convertView;
        }
    }

    class ViewHolder{
        ImageView img,item_select;
    }

}
