package com.viewscenes.bean;

public class NotificationBean extends BaseBean {
	private String id;
	private String from_userid;
	private String create_time;
	private String to_userid;
	private String check_userid;
	private String description;
	private String msg_checked;
	private String start_time;
	private String end_time;
	private String from_username;
	private String to_username;

	public String getMsg_checked() {
		return msg_checked;
	}
	public void setMsg_checked(String msgChecked) {
		msg_checked = msgChecked;
	}	
	public String getFrom_username() {
		return from_username;
	}
	public void setFrom_username(String from_username) {
		this.from_username = from_username;
	}
	public String getTo_username() {
		return to_username;
	}
	public void setTo_username(String to_username) {
		this.to_username = to_username;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFrom_userid() {
		return from_userid;
	}
	public void setFrom_userid(String from_userid) {
		this.from_userid = from_userid;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getTo_userid() {
		return to_userid;
	}
	public void setTo_userid(String to_userid) {
		this.to_userid = to_userid;
	}
	public String getCheck_userid() {
		return check_userid;
	}
	public void setCheck_userid(String check_userid) {
		this.check_userid = check_userid;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	
}
