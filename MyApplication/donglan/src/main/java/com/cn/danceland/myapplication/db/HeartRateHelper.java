package com.cn.danceland.myapplication.db;

import com.cn.danceland.myapplication.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shy on 2018/5/24 09:56
 * Email:644563767@qq.com
 */


public class HeartRateHelper {
    private HeartRateDao heartRateDao = MyApplication.getInstance().getHeartRateSessionSession().getHeartRateDao();


    public void insert(HeartRate heartRate) {
        if (heartRateDao==null){
            heartRateDao = MyApplication.getInstance().getHeartRateSessionSession().getHeartRateDao();
        }
        heartRateDao.insertOrReplace(heartRate);
    }

    public void insertList(ArrayList<HeartRate> heartRateList) {
        heartRateDao.insertOrReplaceInTx(heartRateList);
    }

    public List<HeartRate> queryByDay(Long day) {
        if (heartRateDao==null){
            heartRateDao = MyApplication.getInstance().getHeartRateSessionSession().getHeartRateDao();
        }
        List<HeartRate> heartRateArrayList =  heartRateDao.queryBuilder().where(HeartRateDao.Properties.Date.between(day,day+ 1 * 24 * 60 * 60 * 1000)).orderAsc(HeartRateDao.Properties.Date).list();
        return heartRateArrayList;
    }

    public void deleteAll() {
        heartRateDao.deleteAll();
    }


}
