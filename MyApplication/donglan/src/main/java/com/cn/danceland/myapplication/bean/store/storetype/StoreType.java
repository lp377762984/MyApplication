package com.cn.danceland.myapplication.bean.store.storetype;

import java.io.Serializable;

/**
 * @类说明:储值卡类型--实体类
 * @author:高振中
 * @date:2018-03-14 10:16:26
 **/
public class StoreType implements Serializable{

	//原始属性
	private Long id;// 主键
	private Long branch_id;// 门店ID
	private Float face;// 面值
	private Float giving;// 赠送金额
	private String remark;// 备注
	private Byte enable;// 状态
	private String name;// 储值卡名称

	//新增属性

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getBranch_id() {
		return branch_id;
	}
	public void setBranch_id(Long branch_id) {
		this.branch_id = branch_id;
	}
	public Float getFace() {
		return face;
	}
	public void setFace(Float face) {
		this.face = face;
	}
	public Float getGiving() {
		return giving;
	}
	public void setGiving(Float giving) {
		this.giving = giving;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Byte getEnable() {
		return enable;
	}
	public void setEnable(Byte enable) {
		this.enable = enable;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}