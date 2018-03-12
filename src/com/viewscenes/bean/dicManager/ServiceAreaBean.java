package com.viewscenes.bean.dicManager;

import com.viewscenes.bean.BaseBean;

/**
 * 服务区对象类
 * @author leeo
 *
 */
public class ServiceAreaBean extends BaseBean {

	public String id;
	public String chinese_name;
	public String english_name;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getChinese_name() {
		return chinese_name;
	}
	public void setChinese_name(String chinese_name) {
		this.chinese_name = chinese_name;
	}
	public String getEnglish_name() {
		return english_name;
	}
	public void setEnglish_name(String english_name) {
		this.english_name = english_name;
	}
	
}
