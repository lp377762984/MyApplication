package com.cn.danceland.myapplication.bean;

import java.io.Serializable;

/**
 * Created by shy on 2017/12/29 10:24
 * Email:644563767@qq.com
 */


public class OrderExtendsInfoBean implements Serializable {
    private String admin_emp_id;
    private String admin_emp_name;
    private String admin_emp_phone;
    private String branch_id;
    private String branch_name;
    private int charge_mode;
    private String deposit_id;
    private float deposit_price;
    private float face_value;
    private String gender;
    private String member_id;
    private String member_name;
    private String member_no;
    private int month_count;
    private String operater_id;
    private String phone_no;
    private String sell_date;
    private String sell_id;
    private String sell_name;
    private float sell_price;
    private int total_count;
    private String type_id;

    @Override
    public String toString() {
        return "OrderExtendsInfoBean{" +
                "admin_emp_id='" + admin_emp_id + '\'' +
                ", admin_emp_name='" + admin_emp_name + '\'' +
                ", admin_emp_phone='" + admin_emp_phone + '\'' +
                ", branch_id='" + branch_id + '\'' +
                ", branch_name='" + branch_name + '\'' +
                ", charge_mode=" + charge_mode +
                ", deposit_id='" + deposit_id + '\'' +
                ", deposit_price=" + deposit_price +
                ", face_value=" + face_value +
                ", gender='" + gender + '\'' +
                ", member_id='" + member_id + '\'' +
                ", member_name='" + member_name + '\'' +
                ", member_no='" + member_no + '\'' +
                ", month_count=" + month_count +
                ", operater_id='" + operater_id + '\'' +
                ", phone_no='" + phone_no + '\'' +
                ", sell_date='" + sell_date + '\'' +
                ", sell_id='" + sell_id + '\'' +
                ", sell_name='" + sell_name + '\'' +
                ", sell_price=" + sell_price +
                ", total_count=" + total_count +
                ", type_id='" + type_id + '\'' +
                '}';
    }

    public String getAdmin_emp_id() {
        return admin_emp_id;
    }

    public void setAdmin_emp_id(String admin_emp_id) {
        this.admin_emp_id = admin_emp_id;
    }

    public String getAdmin_emp_name() {
        return admin_emp_name;
    }

    public void setAdmin_emp_name(String admin_emp_name) {
        this.admin_emp_name = admin_emp_name;
    }

    public String getAdmin_emp_phone() {
        return admin_emp_phone;
    }

    public void setAdmin_emp_phone(String admin_emp_phone) {
        this.admin_emp_phone = admin_emp_phone;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public int getCharge_mode() {
        return charge_mode;
    }

    public void setCharge_mode(int charge_mode) {
        this.charge_mode = charge_mode;
    }

    public String getDeposit_id() {
        return deposit_id;
    }

    public void setDeposit_id(String deposit_id) {
        this.deposit_id = deposit_id;
    }

    public float getDeposit_price() {
        return deposit_price;
    }

    public void setDeposit_price(float deposit_price) {
        this.deposit_price = deposit_price;
    }

    public float getFace_value() {
        return face_value;
    }

    public void setFace_value(float face_value) {
        this.face_value = face_value;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getMember_no() {
        return member_no;
    }

    public void setMember_no(String member_no) {
        this.member_no = member_no;
    }

    public int getMonth_count() {
        return month_count;
    }

    public void setMonth_count(int month_count) {
        this.month_count = month_count;
    }

    public String getOperater_id() {
        return operater_id;
    }

    public void setOperater_id(String operater_id) {
        this.operater_id = operater_id;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getSell_date() {
        return sell_date;
    }

    public void setSell_date(String sell_date) {
        this.sell_date = sell_date;
    }

    public String getSell_id() {
        return sell_id;
    }

    public void setSell_id(String sell_id) {
        this.sell_id = sell_id;
    }

    public String getSell_name() {
        return sell_name;
    }

    public void setSell_name(String sell_name) {
        this.sell_name = sell_name;
    }

    public float getSell_price() {
        return sell_price;
    }

    public void setSell_price(float sell_price) {
        this.sell_price = sell_price;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }
}
