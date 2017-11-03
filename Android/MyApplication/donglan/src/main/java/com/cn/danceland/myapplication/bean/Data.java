/**
 * Copyright 2017 bejson.com
 */
package com.cn.danceland.myapplication.bean;

/**
 * Auto-generated: 2017-10-26 11:25:11
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Data {

    private String verCode;
    private String id;//用户id
    private ResultObject resultObject;

    public void setResultObject(ResultObject resultObject) {
        this.resultObject = resultObject;
    }

    public ResultObject getResultObject() {
        return resultObject;
    }

    @Override
    public String toString() {
        return "Data{" +
                "verCode='" + verCode + '\'' +
                ", id='" + id + '\'' +
                ", resultObject=" + resultObject +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setVerCode(String verCode) {
        this.verCode = verCode;
    }

    public String getVerCode() {
        return verCode;
    }

}