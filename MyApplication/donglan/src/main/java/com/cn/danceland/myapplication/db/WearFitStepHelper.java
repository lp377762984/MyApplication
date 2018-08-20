package com.cn.danceland.myapplication.db;

import com.cn.danceland.myapplication.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * 手环睡眠DB
 * Created by ${yxx} on 2018/8/5.
 */
public class WearFitStepHelper {
    private WearFitStepBeanDao wearFitStepDao = MyApplication.getInstance().getWearFitStepSession().getWearFitStepBeanDao();

    public void insert(WearFitStepBean wearFitStepBean) {
        if (wearFitStepDao==null){
            wearFitStepDao = MyApplication.getInstance().getWearFitStepSession().getWearFitStepBeanDao();
        }
        wearFitStepDao.insertOrReplace(wearFitStepBean);
    }

    public void insertList(ArrayList<WearFitStepBean> wearFitStepBeans) {
        wearFitStepDao.insertOrReplaceInTx(wearFitStepBeans);
    }

    public List<WearFitStepBean> queryByDay(Long day) {
        if (wearFitStepDao==null){
            wearFitStepDao = MyApplication.getInstance().getWearFitStepSession().getWearFitStepBeanDao();
        }
        List<WearFitStepBean> wearFitStepBeans =   wearFitStepDao.queryBuilder().where(WearFitStepBeanDao.Properties.Timestamp.between(day,day+ 1 * 24 * 60 * 60 * 1000)).orderAsc(WearFitStepBeanDao.Properties.Timestamp).list();
        return wearFitStepBeans;
    }

    public void deleteAll() {
        wearFitStepDao.deleteAll();
    }
}
