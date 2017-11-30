package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.HeadImageBean;
import com.cn.danceland.myapplication.db.DBData;
import com.cn.danceland.myapplication.db.Donglan;
import com.cn.danceland.myapplication.evntbus.StringEvent;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.cn.danceland.myapplication.utils.multipartrequest.MultipartRequest;
import com.cn.danceland.myapplication.utils.multipartrequest.MultipartRequestParams;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by feng on 2017/10/10.
 */

public class MyProActivity extends Activity {
    CircleImageView circleImageView;
    MyAdapter arrayAdapter;

    PopupWindow head_image_window;
    View headView,rootview,sexView;
    ImageView back;
    int flag;
    Uri uri,mCutUri;
    ContentResolver resolver;
    public final static int ALBUM_REQUEST_CODE = 1;
    public final static int CROP_REQUEST = 2;
    public final static int CAMERA_REQUEST_CODE = 3;
    public static String SAVED_IMAGE_DIR_PATH =
            Environment.getExternalStorageDirectory().getPath()
                    + "/donglan/camera/";// 拍照路径
    String cameraPath,gemder,nickName,selfAvatarPath,strHeight,strWeight;
    Data infoData;
    Gson gson;
    RequestQueue queue;
    File cutfile;
    RelativeLayout  headimage,name,sex,height,weight,rl_zone,rl_phone,identity;
    TextView text_name,text_sex,photograph,photo_album,cancel,cancel1,male,female,tv_height
            ,tv_weight,tv_zone,tv_phone,tv_identity,selecttitle,over,cancel_action,lo_cancel_action,over_action;
    View contentView;
    PopupWindow mPopWindow;
    ListView list_height;
    View locationView;
    PopupWindow locationWindow;
    LocationAdapter proAdapter, cityAdapter;
    int x = 999;
    ArrayList<String> proList,cityList1;
    ListView list_province, list_city;
    DBData dbData;
    String zoneCode,mZoneCode;
    List<Donglan> zoneArr,cityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypro);
        initHost();
        initView();
        setClick();
    }

    public void initHost(){
        dbData = new DBData();
        gson = new Gson();
        resolver = getContentResolver();
        infoData = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
        queue = Volley.newRequestQueue(MyProActivity.this);
        zoneCode = infoData.getZoneCode();
        zoneArr = new ArrayList<Donglan>();
        if(zoneCode!=null&&!"".equals(zoneCode)){
            zoneArr = dbData.queryCityValue(zoneCode);
        }
        initLocationData();
    }

    public void initView(){
        circleImageView = findViewById(R.id.circleimageview);
        if(infoData.getSelfAvatarPath()!=null&&!infoData.getSelfAvatarPath().equals("")){
            Glide.with(MyProActivity.this).load(infoData.getSelfAvatarPath()).into(circleImageView);
        }
        text_name = findViewById(R.id.text_name);
        text_sex = findViewById(R.id.text_sex);
        headimage = findViewById(R.id.head_image);
        name = findViewById(R.id.name);
        sex = findViewById(R.id.sex);
        back = findViewById(R.id.back);
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        rl_zone = findViewById(R.id.rl_zone);
        rl_phone = findViewById(R.id.rl_phone);
        identity = findViewById(R.id.identity);
        tv_height = findViewById(R.id.tv_height);
        tv_weight = findViewById(R.id.tv_weight);
        tv_zone = findViewById(R.id.tv_zone);
        tv_phone = findViewById(R.id.tv_phone);
        tv_identity = findViewById(R.id.tv_identity);

        if(infoData.getHeight()!=null){
            tv_height.setText(infoData.getHeight()+" cm");
        }
        if(infoData.getWeight()!=null){
            tv_weight.setText(infoData.getWeight()+" kg");
        }


        if("1".equals(infoData.getGender())){
            text_sex.setText("男");
        }else {
            text_sex.setText("女");
        }
        text_name.setText(infoData.getNickName());

        headView = LayoutInflater.from(MyProActivity.this).inflate(R.layout.head_image_popwindow, null);

        photograph = headView.findViewById(R.id.photograph);
        photo_album = headView.findViewById(R.id.photo_album);
        cancel = headView.findViewById(R.id.cancel);

        contentView = LayoutInflater.from(MyProActivity.this).inflate(R.layout.selectorwindowsingle,null);
        mPopWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        list_height = contentView.findViewById(R.id.list_height);
        selecttitle = contentView.findViewById(R.id.selecttitle);
        over = contentView.findViewById(R.id.over);
        cancel_action = contentView.findViewById(R.id.cancel_action);

        locationView = LayoutInflater.from(MyProActivity.this).inflate(R.layout.selectorwindowlocation, null);
        locationWindow = new PopupWindow(locationView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        locationWindow.setOutsideTouchable(true);
        locationWindow.setBackgroundDrawable(new BitmapDrawable());
        lo_cancel_action = locationView.findViewById(R.id.cancel_action);
        over_action = locationView.findViewById(R.id.over_action);
        list_province = locationView.findViewById(R.id.list_province);
        list_city = locationView.findViewById(R.id.list_city);
        if(zoneArr.size()>0){
            tv_zone.setText(zoneArr.get(0).getProvince()+" "+zoneArr.get(0).getCity());
            zoneArr.clear();
        }
    }

    public void setClick(){
        headimage.setOnClickListener(onClickListener);
        name.setOnClickListener(onClickListener);
        sex.setOnClickListener(onClickListener);
        cancel.setOnClickListener(onClickListener);
        photo_album.setOnClickListener(onClickListener);
        photograph.setOnClickListener(onClickListener);
        back.setOnClickListener(onClickListener);
        height.setOnClickListener(onClickListener);
        weight.setOnClickListener(onClickListener);
        over.setOnClickListener(onClickListener);
        cancel_action.setOnClickListener(onClickListener);
        rl_zone.setOnClickListener(onClickListener);
    }
    public void dismissWindow(){
        if(null != head_image_window && head_image_window.isShowing()){
            head_image_window.dismiss();
        }
        if(null != mPopWindow && mPopWindow.isShowing()){
            mPopWindow.dismiss();
        }
        if (null != locationWindow && locationWindow.isShowing()) {
            locationWindow.dismiss();
        }
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id){
                case R.id.head_image:{
                    flag = 0;
                    dismissWindow();
                    showEditImage();
                    showPop();
                }
                    break;
                case R.id.height:
                    //修改身高
                    x = 0;
                    showSelectorWindow(x);
                    selecttitle.setText("选择身高");
                    break;
                case R.id.weight:
                    x = 1;
                    showSelectorWindow(x);
                    selecttitle.setText("选择体重");
                    break;
                case R.id.over:
                    if(x==0){
                        infoData.setHeight(strHeight);
                        commitSelf(Constants.MODIFY_HEIGHT,"height",strHeight);
                    }else if(x==1){
                        infoData.setWeight(strWeight);
                        commitSelf(Constants.MODIFY_WEIGHT,"weight",strWeight);
                    }
                    DataInfoCache.saveOneCache(infoData,Constants.MY_INFO);
                    dismissWindow();
                    break;
                case R.id.cancel_action:
                    dismissWindow();
                    break;
                case R.id.name:{
                    showName();
                }
                    break;
                case R.id.sex:{
                    flag = 1;
                    dismissWindow();
                    showSex();
                    showPop();
                }
                    break;
                case R.id.cancel:
                    if(flag==0){
                        dismissWindow();
                    }else if(flag==1){
                        dismissWindow();
                        text_sex.setText("女");
                        infoData.setGender("2");
                        DataInfoCache.saveOneCache(infoData,Constants.MY_INFO);
                        commitSelf(Constants.MODIFY_GENDER,"gender","2");
                    }
                    break;
                case R.id.photo_album:
                    if(flag==0){
                        dismissWindow();
                        photoAlbum();
                    }else if(flag==1){
                        dismissWindow();
                        text_sex.setText("男");
                        infoData.setGender("1");
                        DataInfoCache.saveOneCache(infoData,Constants.MY_INFO);
                        commitSelf(Constants.MODIFY_GENDER,"gender","1");
                    }
                    break;
                case R.id.photograph:
                    if(flag==0){
                        dismissWindow();
                        photoGraph();
                    }else{
                        break;
                    }
                    break;
                case R.id.back:
                    Intent intent = new Intent();
                    intent.putExtra("selfAvatarPath",selfAvatarPath);
                    setResult(99,intent);
                    finish();
                    break;
                case R.id.over_action:
                    infoData.setZoneCode(mZoneCode);
                    DataInfoCache.saveOneCache(infoData,Constants.MY_INFO);
                    commitSelf(Constants.MODIFY_ZONE,"zoneCode",mZoneCode);
                    dismissWindow();
                    break;
                case R.id.rl_zone:
                    showLocation();
                    break;
            }
        }
    };


    public void showLocation() {

        locationWindow.setContentView(locationView);
        proAdapter = new LocationAdapter(proList, this);
        list_province.setAdapter(proAdapter);

        list_province.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String pro = proList.get(position);
                tv_zone.setText(pro);
                List<Donglan> queryPro = dbData.queryPro(pro);

                cityList1 = new ArrayList<String>();
                for(int i=0;i<queryPro.size();i++){
                    cityList1.add(queryPro.get(i).getCity());
                }
                //ArrayList<String> cityList = proCityMap.get(pro);
                if(cityList1!=null&&cityList1.size()>0){
                    cityAdapter = new LocationAdapter(cityList1, MyProActivity.this);
                    list_city.setAdapter(cityAdapter);
                }
            }
        });
        list_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String city = cityList1.get(position);
                mZoneCode = dbData.queryCity(city).get(0).getCityValue();
                tv_zone.setText(tv_zone.getText().toString().split(" ")[0]+" "+city);
            }
        });


        locationWindow.showAsDropDown(identity, 0, 40);
        //locationWindow.setAnimationStyle(R.style.selectorMenuAnim);

    }

    public void initLocationData(){
        cityList = dbData.getCityList();
        //省份列表
        proList = new ArrayList<String>();
        if(cityList!=null&&cityList.size()>0){
            for(int i=0;i<cityList.size();i++){
                //城市名字为key，城市代码为value
                String prokey = cityList.get(i).getProvince();
                proList.add(prokey);
                for(int m=0;m<proList.size()-1;m++){
                    if(proList.get(m).equals(proList.get(m+1))){
                        proList.remove(m);
                        m--;
                    }
                }

            }
        }

    }
    public class LocationAdapter extends BaseAdapter {

        ArrayList<String> arrayList;
        LayoutInflater inflater = null;

        public LocationAdapter(ArrayList<String> list, Context context) {
            arrayList = list;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return arrayList.size();
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
            TextView item_text = null;
            if (view == null) {
                view = inflater.inflate(R.layout.selector_item, null);
            }
            item_text = view.findViewById(R.id.item_text);
            item_text.setText(arrayList.get(i));
            return view;
        }
    }

    public void showSelectorWindow(int x){
        final int j = x;
        mPopWindow.setContentView(contentView);
        //显示PopupWindow
        //View rootview = LayoutInflater.from(RegisterInfoActivity.this).inflate(R.layout.activity_register_info, null);
        String[] str  = new String[71];
        Integer[] str1 = new Integer[165];
        final ArrayList<String> arHeight = new ArrayList<String>();
        int n;
        if(j==0){
            for(int i = 0;i<71;i++){
                n = 150+i;
                str[i] = n+"";
            }
            Arrays.sort(str);
            for(int z = 0;z<str.length;z++){
                arHeight.add(str[z]);
            }
        }else {
            for(int y=0;y<165;y++){
                n = 35+y;
                str1[y] = n;
            }
            Arrays.sort(str1);
            for(int z = 0;z<str1.length;z++){
                arHeight.add(str1[z]+"");
            }
        }

        arrayAdapter = new MyAdapter(arHeight,this);
        list_height.setAdapter(arrayAdapter);

        mPopWindow.showAsDropDown(identity,0,40);
        mPopWindow.setAnimationStyle(R.style.selectorMenuAnim);

        list_height.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(j==0){
                    tv_height.setText(arHeight.get(i)+" cm");
                    strHeight = arHeight.get(i)+"";
                }else{
                    tv_weight.setText(arHeight.get(i)+" kg");
                    strWeight = arHeight.get(i)+"";
                }
            }
        });

    }
    public class MyAdapter extends BaseAdapter {

        ArrayList<String> arrayList;
        LayoutInflater inflater = null;
        public MyAdapter(ArrayList<String> list, Context context){
            arrayList = list;
            inflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return arrayList.size();
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
            TextView item_text = null;
            if(view==null){
                view  = inflater.inflate(R.layout.selector_item,null);
            }
            item_text = view.findViewById(R.id.item_text);
            item_text.setText(arrayList.get(i));
            return view;
        }
    }

    public void showPop(){
        head_image_window = new PopupWindow(headView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        head_image_window.setOutsideTouchable(true);
        head_image_window.setBackgroundDrawable(new BitmapDrawable());
        //显示PopupWindow
        rootview = LayoutInflater.from(MyProActivity.this).inflate(R.layout.activity_mypro, null);
        head_image_window.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
        head_image_window.setAnimationStyle(R.style.selectorMenuAnim);

    }

    public void showEditImage(){
        if(null != head_image_window && head_image_window.isShowing()){
            head_image_window.dismiss();
        }
        photograph.setText("拍照");
        photograph.setTextColor(Color.rgb(46,167,224));
        photo_album.setText("从手机相册选择");
        cancel.setText("取消");
        //cancel1.setVisibility(View.GONE);
    }

    public void showName(){

            AlertDialog.Builder normalDialog =
                    new AlertDialog.Builder(MyProActivity.this);
            View dialogView = LayoutInflater.from(MyProActivity.this)
                .inflate(R.layout.edit_name,null);
            //normalDialog.setTitle("编辑昵称");
           final EditText ed = dialogView.findViewById(R.id.edit_name);
            normalDialog.setView(dialogView);
            normalDialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            nickName = ed.getText().toString();
                            text_name.setText(nickName);
                            commitSelf(Constants.MODIFY_NAME,"nickName",nickName);
                            infoData.setNickName(nickName);
                            DataInfoCache.saveOneCache(infoData,Constants.MY_INFO);
                            //发送事件
                            EventBus.getDefault().post(new StringEvent(nickName,100));
                        }
                    });
            normalDialog.setNegativeButton("关闭",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            // 显示
            normalDialog.show();

    }
    public void commitSelf(String url, final String mapkey, final String mapvalue){

        StringRequest stringRequest = new StringRequest(Request.Method.PUT,url , new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(s.contains("true")){
                    ToastUtils.showToastShort("修改成功！");
                }else{
                    ToastUtils.showToastShort("修改失败！");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToastShort("修改失败！请检查网络");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<String,String>();
                map.put(mapkey,mapvalue);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<String,String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN,""));

                return map;
            }
        };
        queue.add(stringRequest);
    }


    public void showSex(){
        if(null != head_image_window && head_image_window.isShowing()){
            head_image_window.dismiss();
        }
        photograph.setText("修改性别");
        photograph.setTextColor(Color.rgb(153,153,153));
        photo_album.setText("男");
        cancel.setText("女");
    }

    public void photoGraph (){

        // 指定相机拍摄照片保存地址
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            cameraPath = SAVED_IMAGE_DIR_PATH + System.currentTimeMillis() + ".png";
            Intent intent = new Intent();
            // 指定开启系统相机的Action
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            String out_file_path = SAVED_IMAGE_DIR_PATH;
            File dir = new File(out_file_path);
            if (!dir.exists()) {
                dir.mkdirs();
            } // 把文件地址转换成Uri格式
            uri = Uri.fromFile(new File(cameraPath));
            // 设置系统相机拍摄照片完成后图片文件的存放地址
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }
        else {
            Toast.makeText(getApplicationContext(), "请确认已经插入SD卡",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            MultipartRequestParams params = new MultipartRequestParams();
            File file = null;
            if (requestCode == CAMERA_REQUEST_CODE) {
                startPhotoZoom(uri);
            }
            if(requestCode == ALBUM_REQUEST_CODE){
                startActivityForResult(CutForPhoto(data.getData()),10010);

            }
            if(requestCode==10010){
                Glide.with(MyProActivity.this).load(mCutUri).into(circleImageView);
                file = cutfile;

            }
            if(requestCode==222){
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    File fileCut = new File(SAVED_IMAGE_DIR_PATH + System.currentTimeMillis() + ".png");
                    //LogUtil.e("zzf",fileCut.toString());
                    file = fileCut;
                    try{
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileCut));
                        photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        bos.flush();
                        bos.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                    Drawable drawable = new BitmapDrawable(getResources(), photo);
                    Glide.with(MyProActivity.this).load(drawable).into(circleImageView);
                }
            }
            if(file!=null){
                params.put("file",file);
                MultipartRequest request = new MultipartRequest(Request.Method.POST, params, Constants.UPLOADFILE_URL, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String s) {
                        HeadImageBean headImageBean = gson.fromJson(s, HeadImageBean.class);
                        selfAvatarPath = headImageBean.getData().getImgUrl();
                        infoData.setSelfAvatarPath(selfAvatarPath);
                        //发送事件
                        EventBus.getDefault().post(new StringEvent(selfAvatarPath,99));
                        commitSelf(Constants.MODIFYY_IMAGE,"self_Avatar_path",selfAvatarPath);
                        DataInfoCache.saveOneCache(infoData,Constants.MY_INFO);
                        //LogUtil.e("zzf",s);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //LogUtil.e("zzf",volleyError.toString());
                    }
                }
                );

                MyApplication.getHttpQueues().add(request);
            }

        }

    }



    public void  photoAlbum(){

        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, ALBUM_REQUEST_CODE);

    }

    private Intent CutForPhoto(Uri uri) {
        try {
            //直接裁剪
            Intent intent = new Intent("com.android.camera.action.CROP");
            //设置裁剪之后的图片路径文件
             //随便命名一个
            cutfile = new File(SAVED_IMAGE_DIR_PATH + System.currentTimeMillis() + ".png");
            if (cutfile.exists()){ //如果已经存在，则先删除,这里应该是上传到服务器，然后再删除本地的，没服务器，只能这样了
                cutfile.delete();
            }
            cutfile.createNewFile();
            //初始化 uri
            Uri imageUri = uri; //返回来的 uri
            Uri outputUri = null; //真实的 uri
            //Log.d(TAG, "CutForPhoto: "+cutfile);
            outputUri = Uri.fromFile(cutfile);
            mCutUri = outputUri;
            //Log.d(TAG, "mCameraUri: "+mCutUri);
            // crop为true是设置在开启的intent中设置显示的view可以剪裁
            intent.putExtra("crop",true);
            // aspectX,aspectY 是宽高的比例，这里设置正方形
            intent.putExtra("aspectX",1);
            intent.putExtra("aspectY",1);
            //设置要裁剪的宽高
            intent.putExtra("outputX", 100); //200dp
            intent.putExtra("outputY",100);
            intent.putExtra("scale",true);
            //如果图片过大，会导致oom，这里设置为false
            intent.putExtra("return-data",false);
            if (imageUri != null) {
                intent.setDataAndType(imageUri, "image/*");
            }
            if (outputUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
            }
            intent.putExtra("noFaceDetection", true);
//            //压缩图片
            intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
            return intent;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, 222);
    }


}
