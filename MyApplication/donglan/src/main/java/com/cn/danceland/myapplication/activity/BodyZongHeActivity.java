package com.cn.danceland.myapplication.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.app.AppManager;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.DLResult;
import com.cn.danceland.myapplication.bean.HeadImageBean;
import com.cn.danceland.myapplication.bean.RequsetFindUserBean;
import com.cn.danceland.myapplication.bean.bca.bcaanalysis.BcaAnalysis;
import com.cn.danceland.myapplication.bean.bca.bcaanalysis.BcaAnalysisRequest;
import com.cn.danceland.myapplication.bean.bca.bcaresult.BcaResult;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.PictureUtil;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.cn.danceland.myapplication.utils.multipartrequest.MultipartRequest;
import com.cn.danceland.myapplication.utils.multipartrequest.MultipartRequestParams;
import com.cn.danceland.myapplication.view.DongLanTitleView;
import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by feng on 2018/4/12.
 */

public class BodyZongHeActivity extends BaseActivity {
    private static final int MSG_REFRESH_DATA = 0;//刷新数据
    RelativeLayout rl_01, rl_02, rl_03;
    Uri uri;
    String cameraPath;
    public static String SAVED_IMAGE_DIR_PATH =
            Environment.getExternalStorageDirectory().getPath()
                    + "/DCIM/camera/";// 拍照路径
    Gson gson;
    String num;//判断第几个图片
    HashMap<Integer, String> numMap;
    ImageView img_01, img_02, img_03;
    DongLanTitleView body_zonghe_title;
    List<BcaResult> resultList;
    EditText et_content;
    Button btn_commit;
    private boolean isClick = true;
    private RequsetFindUserBean.Data requsetInfo;//前面搜索到的对象

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bodyzonghe);
        AppManager.getAppManager().addActivity(this);
        initHost();
        initView();
    }

    private void initHost() {
        resultList = (List<BcaResult>) getIntent().getSerializableExtra("resultList");
        requsetInfo = (RequsetFindUserBean.Data) getIntent().getSerializableExtra("requsetInfo");//前面搜索到的对象

        if (resultList == null) {
            resultList = new ArrayList<>();
        }

        numMap = new HashMap<>();

        gson = new Gson();

    }

    private void initView() {
        body_zonghe_title = findViewById(R.id.body_zonghe_title);
        body_zonghe_title.setTitle("综合评价");
        et_content = findViewById(R.id.et_content);
        btn_commit = findViewById(R.id.btn_commit);

        rl_01 = findViewById(R.id.rl_01);
        rl_02 = findViewById(R.id.rl_02);
        rl_03 = findViewById(R.id.rl_03);

        img_01 = findViewById(R.id.img_01);
        img_02 = findViewById(R.id.img_02);
        img_03 = findViewById(R.id.img_03);

        setOnClick();

    }

    private void setOnClick() {
        rl_01.setOnClickListener(onClickListener);
        rl_02.setOnClickListener(onClickListener);
        rl_03.setOnClickListener(onClickListener);
        btn_commit.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LogUtil.i("num-" + num);
            LogUtil.i("numMap.size()-" + numMap.size());
            if (isClick) {
                switch (v.getId()) {
                    case R.id.rl_01:
                        num = "1";
                        getPermission();
                        break;
                    case R.id.rl_02:
                        num = "2";
                        getPermission();
                        break;
                    case R.id.rl_03:
                        num = "3";
                        getPermission();
                        break;
                    case R.id.btn_commit:
                        if (numMap.size() != 3) {
                            ToastUtils.showToastShort("请拍照完成后提交！");
                        } else {
                            save();
                        }
                        break;
                }
            } else {
                ToastUtils.showToastShort("正在提交图片，请稍后...");
            }
        }
    };

    private void getPermission() {
        if (PermissionsUtil.hasPermission(BodyZongHeActivity.this, Manifest.permission.CAMERA)) {
            isClick = false;
            //有权限
            takePhoto();

            startActivity(new Intent(BodyZongHeActivity.this, FloatingLayerCameraActivity.class).putExtra("requsetInfo", requsetInfo));
        } else {
            PermissionsUtil.requestPermission(BodyZongHeActivity.this, new PermissionListener() {
                @Override
                public void permissionGranted(@NonNull String[] permissions) {
                    takePhoto();
                }

                @Override
                public void permissionDenied(@NonNull String[] permissions) {
                    //用户拒绝了申请
                    ToastUtils.showToastShort("没有权限");
                }
            }, new String[]{Manifest.permission.CAMERA}, false, null);
        }

    }

    /**
     * @方法说明:新增体测分析
     **/
    public void save() {
        BcaAnalysis bcaAnalysis = new BcaAnalysis();
        final BcaAnalysisRequest request = new BcaAnalysisRequest();
        bcaAnalysis.setMember_id(Long.valueOf(requsetInfo.getId()));
        bcaAnalysis.setMember_no(requsetInfo.getMember_no());
        bcaAnalysis.setFrontal_path(numMap.get(1));//正面照
        bcaAnalysis.setSide_path(numMap.get(2));//侧面照
        bcaAnalysis.setBehind_path(numMap.get(3));//背面照
        bcaAnalysis.setResult(resultList);
        if (et_content.getText() != null) {
            bcaAnalysis.setContent(et_content.getText().toString());
        }
        request.save(bcaAnalysis, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject json) {
                DLResult<String> result = gson.fromJson(json.toString(), new TypeToken<DLResult<String>>() {
                }.getType());
                if (result.isSuccess()) {
                    LogUtil.i(""+json.toString());
                    ToastUtils.showToastShort("提交成功！");
                    startActivity(new Intent(BodyZongHeActivity.this, FitnessResultsSummaryActivity.class)
                            .putExtra("requsetInfo", requsetInfo)
                    .putExtra("saveId",result.getData()));
                } else {
                    ToastUtils.showToastShort("保存数据失败,请检查手机网络！");
                }
            }
        });

    }

    private void takePhoto() {

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
            if (PictureUtil.getSDKV() < 24) {
                uri = Uri.fromFile(new File(cameraPath));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, 1);
            } else {
                // 设置系统相机拍摄照片完成后图片文件的存放地址
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, cameraPath);
                uri = getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, 1);
            }
            //uri = Uri.fromFile(new File(cameraPath));

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                if (cameraPath != null) {
                    Bitmap bitmap = PictureUtil.FileCompressToBitmap(cameraPath);//压缩

                    if ("1".equals(num)) {
                        rl_01.setVisibility(View.GONE);
                        img_01.setVisibility(View.VISIBLE);
                        Glide.with(BodyZongHeActivity.this).load(uri).into(img_01);
                    } else if ("2".equals(num)) {
                        rl_02.setVisibility(View.GONE);
                        img_02.setVisibility(View.VISIBLE);
                        Glide.with(BodyZongHeActivity.this).load(uri).into(img_02);
                    } else if ("3".equals(num)) {
                        rl_03.setVisibility(View.GONE);
                        img_03.setVisibility(View.VISIBLE);
                        Glide.with(BodyZongHeActivity.this).load(uri).into(img_03);
                    }
                    //上传图片
                    MultipartRequestParams params = new MultipartRequestParams();
//                    File file = new File(cameraPath);
                    File file = PictureUtil.SaveBitmapFile(bitmap, cameraPath);

                    params.put("file", file);
                    LogUtil.i("上传图片大小--" + file.length());
                    LogUtil.i("上传图片参数--" + params.toString());

                    MultipartRequest request = new MultipartRequest(Request.Method.POST, params, Constants.BCAUPLOAD, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String s) {
                            LogUtil.i("上传图片接收--" + s.toString());
                            HeadImageBean headImageBean = gson.fromJson(s, HeadImageBean.class);
                            if (headImageBean != null && headImageBean.getData() != null) {
                                String imgPath = headImageBean.getData().getImgPath();
                                String imgUrl = headImageBean.getData().getImgUrl();
                                Message message = new Message();
                                message.what = MSG_REFRESH_DATA;
                                message.obj = imgPath;
                                handler.sendMessage(message);
                                ToastUtils.showToastShort("上传图片成功！");
                                LogUtil.i("上传图片成功");
                            } else {
                                LogUtil.i("上传图片失败");
                                ToastUtils.showToastShort("上传图片失败！请重新拍照！");
                                isClick = true;
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            isClick = true;
                            ToastUtils.showToastShort("上传图片失败！请重新拍照！");
                            LogUtil.i("上传图片失败--" + volleyError.toString());
                        }
                    }
                    );
                    request.setRetryPolicy(new DefaultRetryPolicy(30000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    MyApplication.getHttpQueues().add(request);
                }
            }
        } else {
            isClick = true;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case MSG_REFRESH_DATA:
                    isClick = true;
                    if ("1".equals(num)) {
                        numMap.put(1, (String) message.obj);
                    } else if ("2".equals(num)) {
                        numMap.put(2, (String) message.obj);
                    } else if ("3".equals(num)) {
                        numMap.put(3, (String) message.obj);
                    }
                    LogUtil.i("2num-" + num);
                    LogUtil.i("2numMap.size()-" + numMap.size());
                    break;
                default:
                    break;
            }
        }
    };
}
