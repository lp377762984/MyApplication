package com.cn.danceland.myapplication.db;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.bean.CityBean;
import com.cn.danceland.myapplication.bean.RootBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.internal.Utils;

import org.greenrobot.greendao.AbstractDaoMaster;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by feng on 2017/11/6.
 */

public class DBData {

    public ArrayList<String> cityDis = new ArrayList<String>();
    DonglanDao donglanDao;
    MiMessageDao miMessageDao;
    int id;

    //添加记录
    public void addD(Donglan d){
        if(donglanDao==null){
            donglanDao = MyApplication.getInstance().getDaoSession().getDonglanDao();
        }
        donglanDao.insert(d);
    }

    //查询全部数据
    public List<Donglan> getCityList(){
        if(donglanDao==null){
            donglanDao = MyApplication.getInstance().getDaoSession().getDonglanDao();
        }
        List<Donglan> donglanList = donglanDao.loadAll();
        return donglanList;
    }

    //根据ID查询数据
    public List<Donglan> queryID(String s) {
        if(donglanDao==null){
            donglanDao = MyApplication.getInstance().getDaoSession().getDonglanDao();
        }
        return donglanDao.queryBuilder().where(DonglanDao.Properties.Id.eq(s)).list();
    }
    //根据ID查询数据
    public List<Donglan> queryPro(String s) {
        if(donglanDao==null){
            donglanDao = MyApplication.getInstance().getDaoSession().getDonglanDao();
        }
        return donglanDao.queryBuilder().where(DonglanDao.Properties.Province.eq(s)).list();
    }
    //根据ID查询数据
    public List<Donglan> queryProValue(String s) {
        if(donglanDao==null){
            donglanDao = MyApplication.getInstance().getDaoSession().getDonglanDao();
        }
        return donglanDao.queryBuilder().where(DonglanDao.Properties.ProvinceValue.eq(s)).list();
    }
    //根据ID查询数据
    public List<Donglan> queryCity(String s) {
        if(donglanDao==null){
            donglanDao = MyApplication.getInstance().getDaoSession().getDonglanDao();
        }
        return donglanDao.queryBuilder().where(DonglanDao.Properties.City.eq(s)).list();
    }
    //根据ID查询数据
    public List<Donglan> queryCityValue(String s) {
        if(donglanDao==null){
            donglanDao = MyApplication.getInstance().getDaoSession().getDonglanDao();
        }
        return donglanDao.queryBuilder().where(DonglanDao.Properties.CityValue.eq(s)).list();
    }



    //更新数据
    public void upDate(Donglan d){
        if(donglanDao==null){
            donglanDao = MyApplication.getInstance().getDaoSession().getDonglanDao();
        }
        donglanDao.update(d);
    }

    public static boolean copyRawDBToApkDb(Context context, int copyRawDbResId, String apkDbPath, String dbName,boolean refresh)
            throws IOException
    {
        boolean b = false;

        File f = new File(apkDbPath);
        if (!f.exists())
        {
            f.mkdirs();
        }

        File dbFile = new File(apkDbPath + dbName);
        //b = isDbFileExists(dbFile,refresh);
        if (!b)
        {
            InputStream is = context.getResources().openRawResource(copyRawDbResId);

            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null)
            {
                int size;
                byte[] buffer = new byte[1024 * 2];

                OutputStream fos = new FileOutputStream(apkDbPath + entry.getName());
                BufferedOutputStream bos = new BufferedOutputStream(fos, buffer.length);

                while ((size = zis.read(buffer, 0, buffer.length)) != -1)
                {
                    bos.write(buffer, 0, size);
                }
                bos.flush();
                bos.close();
            }
            zis.close();
            is.close();
        }
        return !b;
    }

    //查询全部数据
    public List<MiMessage> getMessageList(){
        if(miMessageDao==null){
            miMessageDao = MyApplication.getInstance().getMessageDaoSession().getMiMessageDao();
        }
        List<MiMessage> messagesList = miMessageDao.loadAll();
        return messagesList;
    }
    //查询相应类型的消息
    public List<MiMessage> getMessageList(String type){
        if(miMessageDao==null){
            miMessageDao = MyApplication.getInstance().getMessageDaoSession().getMiMessageDao();
        }
        return miMessageDao.queryBuilder().where(MiMessageDao.Properties.Type.eq(type)).list();
    }
    //添加记录
    public void addMessageD(MiMessage d){
        if(miMessageDao==null){
            miMessageDao = MyApplication.getInstance().getMessageDaoSession().getMiMessageDao();
        }
        miMessageDao.insert(d);
    }

}
