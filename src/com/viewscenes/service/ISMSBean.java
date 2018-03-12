
package com.viewscenes.service;

import java.util.HashMap;
import java.util.Map;

/**
 * 存放发放短信相关的信息
 * 
 */
public class ISMSBean {
	
	private String id;
	private String sms;
	private String mobilephone;
	private String user_id;
	private String user_name;
	
	
	public  ISMSBean(String id,String user_id,String user_name,String sms,String mobilephone) {
		this.id = id;
		this.user_id = user_id;
		this.sms = sms;
		this.mobilephone = mobilephone;
	}	

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String userId) {
		user_id = userId;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String userName) {
		user_name = userName;
	}
	public String getSms() {
		return sms;
	}
	public void setSms(String sms) {
		this.sms = sms;
	}
	public String getMobilephone() {
		return mobilephone;
	}
	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}	
}
