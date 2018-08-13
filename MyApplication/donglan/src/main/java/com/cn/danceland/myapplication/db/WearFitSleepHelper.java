package com.cn.danceland.myapplication.db;

import com.cn.danceland.myapplication.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * 手环睡眠DB
 * Created by ${yxx} on 2018/8/5.
 */
public class WearFitSleepHelper {
    private WearFitSleepBeanDao wearFitSleepDao = MyApplication.getInstance().getWearFitSleepSession().getWearFitSleepBeanDao();

    public void insert(WearFitSleepBean wearFitSleepBean) {
        if (wearFitSleepDao==null){
            wearFitSleepDao = MyApplication.getInstance().getWearFitSleepSession().getWearFitSleepBeanDao();
        }
        wearFitSleepDao.insertOrReplace(wearFitSleepBean);
    }

    public void insertList(ArrayList<WearFitSleepBean> wearFitSleepBeans) {
        wearFitSleepDao.insertOrReplaceInTx(wearFitSleepBeans);
    }

    public List<WearFitSleepBean> queryByDay(Long day) {
        if (wearFitSleepDao==null){
            wearFitSleepDao = MyApplication.getInstance().getWearFitSleepSession().getWearFitSleepBeanDao();
        }
        List<WearFitSleepBean> wearFitSleepBeans =  wearFitSleepDao.queryBuilder().where(WearFitSleepBeanDao.Properties.Timestamp.between(day,day+ 1 * 24 * 60 * 60 * 1000)).orderAsc(WearFitSleepBeanDao.Properties.Timestamp).list();
        return wearFitSleepBeans;
    }

    public void deleteAll() {
        wearFitSleepDao.deleteAll();
    }
}
