package com.cn.danceland.myapplication.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by feng on 2017/11/7.
 */
@Entity
public class Donglan {

    private int id;
    private String province;
    private String city;
    @Generated(hash = 1808725935)
    public Donglan(int id, String province, String city) {
        this.id = id;
        this.province = province;
        this.city = city;
    }
    @Generated(hash = 843809695)
    public Donglan() {
    }
    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getProvince() {
        return this.province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getCity() {
        return this.city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public void clear(){
        province = "";
        city = "";
    }

}
