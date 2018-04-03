package com.cn.danceland.myapplication.bean.bca.bcaresult;

/**
 * @类说明:结果项--实体类
 * @author:高振中
 * @date:2018-03-29 11:54:48
 **/
public class BcaResult {

	//原始属性
	private Long id;// 主键
	private Long a_id;// 体测分析编号
	private Long question_id;// 问题编号
	private Long opt_id;// 选项编号
	private String content;// 结果内容
	private Long branch_id;// 门店

	//新增属性

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getA_id() {
		return a_id;
	}
	public void setA_id(Long a_id) {
		this.a_id = a_id;
	}
	public Long getQuestion_id() {
		return question_id;
	}
	public void setQuestion_id(Long question_id) {
		this.question_id = question_id;
	}
	public Long getOpt_id() {
		return opt_id;
	}
	public void setOpt_id(Long opt_id) {
		this.opt_id = opt_id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Long getBranch_id() {
		return branch_id;
	}
	public void setBranch_id(Long branch_id) {
		this.branch_id = branch_id;
	}
}