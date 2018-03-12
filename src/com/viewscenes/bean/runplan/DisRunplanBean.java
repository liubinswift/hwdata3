package com.viewscenes.bean.runplan;

import com.viewscenes.bean.BaseBean;

public class DisRunplanBean extends BaseBean {
	private String disrun_id;
	private String runplan_id;
	private String station_id;
	private String station_name;
	private String power;
	private String sencity_id;
	private String sencity;
	private String transmiter_no;
	private String freq;
	private String language;
	private String start_time;
	private String end_time;
	private String valid_start_time;
	private String valid_end_time;
	private String disturb;
	private String is_delete;
	private String redisseminators;
	private String receive_country;
	private String receive_city;
	private String type;
	private String receive_station;
	
	public String getReceive_station() {
		return receive_station;
	}
	public void setReceive_station(String receiveStation) {
		receive_station = receiveStation;
	}
	public String getDisrun_id() {
		return disrun_id;
	}
	public void setDisrun_id(String disrunId) {
		disrun_id = disrunId;
	}
	public String getRunplan_id() {
		return runplan_id;
	}
	public void setRunplan_id(String runplanId) {
		runplan_id = runplanId;
	}
	public String getStation_id() {
		return station_id;
	}
	public void setStation_id(String stationId) {
		station_id = stationId;
	}
	public String getSencity_id() {
		return sencity_id;
	}
	public void setSencity_id(String sencityId) {
		sencity_id = sencityId;
	}
	public String getTransmiter_no() {
		return transmiter_no;
	}
	public void setTransmiter_no(String transmiterNo) {
		transmiter_no = transmiterNo;
	}
	public String getFreq() {
		return freq;
	}
	public void setFreq(String freq) {
		this.freq = freq;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
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
	public String getValid_start_time() {
		return valid_start_time;
	}
	public void setValid_start_time(String validStartTime) {
		valid_start_time = validStartTime;
	}
	public String getValid_end_time() {
		return valid_end_time;
	}
	public void setValid_end_time(String validEndTime) {
		valid_end_time = validEndTime;
	}
	public String getDisturb() {
		return disturb;
	}
	public void setDisturb(String disturb) {
		this.disturb = disturb;
	}
	public String getIs_delete() {
		return is_delete;
	}
	public void setIs_delete(String isDelete) {
		is_delete = isDelete;
	}
	public String getSencity() {
		return sencity;
	}
	public void setSencity(String sencity) {
		this.sencity = sencity;
	}
	public String getStation_name() {
		return station_name;
	}
	public void setStation_name(String stationName) {
		station_name = stationName;
	}
	public String getPower() {
		return power;
	}
	public void setPower(String power) {
		this.power = power;
	}
	public String getRedisseminators() {
		return redisseminators;
	}
	public void setRedisseminators(String redisseminators) {
		this.redisseminators = redisseminators;
	}
	public String getReceive_country() {
		return receive_country;
	}
	public void setReceive_country(String receiveCountry) {
		receive_country = receiveCountry;
	}
	public String getReceive_city() {
		return receive_city;
	}
	public void setReceive_city(String receiveCity) {
		receive_city = receiveCity;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
