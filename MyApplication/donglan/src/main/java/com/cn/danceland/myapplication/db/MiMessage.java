package com.cn.danceland.myapplication.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by feng on 2017/12/7.
 */
@Entity
public class MiMessage {

    private int id;
    private String time;
    private String message;
    @Generated(hash = 653716247)
    public MiMessage(int id, String time, String message) {
        this.id = id;
        this.time = time;
        this.message = message;
    }
    @Generated(hash = 932895701)
    public MiMessage() {
    }
    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getMessage() {
        return this.message;
    }
    public void setMessage(String message) {
        this.message = message;
    }


}
