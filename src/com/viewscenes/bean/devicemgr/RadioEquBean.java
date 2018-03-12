package com.viewscenes.bean.devicemgr;

import com.viewscenes.bean.BaseBean;

public class RadioEquBean  extends BaseBean  {
	
	private String id;
    private String head_id;
    private String installation;
    private String use_case;
    private String fault;
    private String replacement;
    private String maintain;
    private String equ_number;
    private String equ_id;
    private String head_code;
    private String main_datetime;
    private String head_name;
    private String pic_path;
    private String country;			//国家
	private String state_name;			//大洲
	private String start_time;      	//故障开始时间
	private String end_time;			//故障结束时间
	private String person;			//维护人
	private String resault;			//处理结果
	private String record_path;		//录音文件路径
	private String old_equ;
	private String old_place;
	private String old_status;
	private String new_equ;
	private String new_from;
	private String mail_order;
	private String mail_time;
    public String getPic_path() {
		return pic_path;
	}
	public void setPic_path(String pic_path) {
		this.pic_path = pic_path;
	}
	public String getHead_name() {
		return head_name;
	}
	public void setHead_name(String head_name) {
		this.head_name = head_name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getHead_id() {
		return head_id;
	}
	public void setHead_id(String head_id) {
		this.head_id = head_id;
	}
	public String getInstallation() {
		return installation;
	}
	public void setInstallation(String installation) {
		this.installation =installation;
	}
	public String getUse_case() {
		return use_case;
	}
	public void setUse_case(String use_case) {
		this.use_case = use_case;
	}
	public String getFault() {
		return fault;
	}
	public void setFault(String fault) {
		this.fault = fault;
	}
	public String getReplacement() {
		return replacement;
	}
	public void setReplacement(String replacement) {
		this.replacement = replacement;
	}
	public String getMaintain() {
		return maintain;
	}
	public void setMaintain(String maintain) {
		this.maintain = maintain;
	}
	public String getEqu_number() {
		return equ_number;
	}
	public void setEqu_number(String equ_number) {
		this.equ_number = equ_number;
	}
	public String getEqu_id() {
		return equ_id;
	}
	public void setEqu_id(String equ_id) {
		this.equ_id = equ_id;
	}
	public String getHead_code() {
		return head_code;
	}
	public void setHead_code(String head_code) {
		this.head_code = head_code;
	}
	public String getMain_datetime() {
		return main_datetime;
	}
	public void setMain_datetime(String main_datetime) {
		this.main_datetime = main_datetime;
	}
	
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getState_name() {
		return state_name;
	}
	public void setState_name(String stateName) {
		state_name = stateName;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String startTime) {
		start_time = startTime;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String endTime) {
		end_time = endTime;
	}
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	public String getResault() {
		return resault;
	}
	public void setResault(String resault) {
		this.resault = resault;
	}
	public String getRecord_path() {
		return record_path;
	}
	public void setRecord_path(String recordPath) {
		record_path = recordPath;
	}
	public String getOld_equ() {
		return old_equ;
	}
	public void setOld_equ(String oldEqu) {
		old_equ = oldEqu;
	}
	public String getOld_place() {
		return old_place;
	}
	public void setOld_place(String oldPlace) {
		old_place = oldPlace;
	}
	public String getOld_status() {
		return old_status;
	}
	public void setOld_status(String oldStatus) {
		old_status = oldStatus;
	}
	public String getNew_equ() {
		return new_equ;
	}
	public void setNew_equ(String newEqu) {
		new_equ = newEqu;
	}
	public String getNew_from() {
		return new_from;
	}
	public void setNew_from(String newFrom) {
		new_from = newFrom;
	}
	public String getMail_order() {
		return mail_order;
	}
	public void setMail_order(String mailOrder) {
		mail_order = mailOrder;
	}
	public String getMail_time() {
		return mail_time;
	}
	public void setMail_time(String mailTime) {
		mail_time = mailTime;
	}
	
}
