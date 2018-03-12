package com.viewscenes.bean.runplan;

import com.viewscenes.bean.BaseBean;

public class GJTLDRunplanBean extends BaseBean {

	public GJTLDRunplanBean() {
		// TODO Auto-generated constructor stub
	}
	private String runplan_id;        //运行图id
	private String runplan_type_id;   //运行图类型id
	private String sentcity_id;       //发射城市id
	private String sentcity;          //发射城市名称
	private String redisseminators;   //转播机构
	private String freq;              //频率
	private String band;              //波段
	private String direction;         //方向
	private String program_id;        //节目id
	private String program_name;      // 节目名称
	private String language_id;       //语言id
	private String language;          //语言名称
	private String power;             //功率
	private String service_area;      //服务区
	private String start_time;       //播音开始时间
	private String end_time;         //播音结束时间
	private String local_time ;       //当地时间
	private String mon_area;         //质量收测站点
	private String mon_area_name;         //质量收测站点名称
	private String rest_datetime;    //休息日期
	private String valid_start_time; //启用期
	private String valid_end_time;  //停用期
	private String remark;           //备注
	private String summer;           // 夏令时
	private String summer_starttime; //夏令时启用时间
	private String summer_endtime;   //夏令时停用时间
	private String input_person;    //录入人
	private String launch_country;    //发射国家
	private String xg_mon_area;      //效果收测站点
	private String xg_mon_area_name;      //效果收测站点名称
    private String type_id; //站点类型
    private String local_start_time;//当地播音开始时间
    private String local_end_time;//当地播音结束时间
    private String weekday;//周设置
	public String getWeekday() {
		return weekday;
	}
	public void setWeekday(String weekday) {
		this.weekday = weekday;
	}
	public String getMon_area_name() {
		return mon_area_name;
	}
	public void setMon_area_name(String mon_area_name) {
		this.mon_area_name = mon_area_name;
	}
	public String getXg_mon_area_name() {
		return xg_mon_area_name;
	}
	public void setXg_mon_area_name(String xg_mon_area_name) {
		this.xg_mon_area_name = xg_mon_area_name;
	}
	public String getLocal_start_time() {
		return local_start_time;
	}
	public void setLocal_start_time(String local_start_time) {
		this.local_start_time = local_start_time;
	}
	public String getLocal_end_time() {
		return local_end_time;
	}
	public void setLocal_end_time(String local_end_time) {
		this.local_end_time = local_end_time;
	}
	public String getType_id() {
		return type_id;
	}
	public void setType_id(String type_id) {
		this.type_id = type_id;
	}
	public String getXg_mon_area() {
		return xg_mon_area;
	}
	public void setXg_mon_area(String xg_mon_area) {
		this.xg_mon_area = xg_mon_area;
	}
	public String getLaunch_country() {
		return launch_country;
	}
	public void setLaunch_country(String launch_country) {
		this.launch_country = launch_country;
	}
	public String getInput_person() {
		return input_person;
	}
	public void setInput_person(String input_person) {
		this.input_person = input_person;
	}
	public String getRunplan_id() {
		return runplan_id;
	}
	public void setRunplan_id(String runplan_id) {
		this.runplan_id = runplan_id;
	}
	public String getRunplan_type_id() {
		return runplan_type_id;
	}
	public void setRunplan_type_id(String runplan_type_id) {
		this.runplan_type_id = runplan_type_id;
	}
	public String getSentcity_id() {
		return sentcity_id;
	}
	public void setSentcity_id(String sentcity_id) {
		this.sentcity_id = sentcity_id;
	}
	public String getSentcity() {
		return sentcity;
	}
	public void setSentcity(String sentcity) {
		this.sentcity = sentcity;
	}
	public String getRedisseminators() {
		return redisseminators;
	}
	public void setRedisseminators(String redisseminators) {
		this.redisseminators = redisseminators;
	}
	public String getFreq() {
		return freq;
	}
	public void setFreq(String freq) {
		this.freq = freq;
	}
	public String getBand() {
		return band;
	}
	public void setBand(String band) {
		this.band = band;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getProgram_id() {
		return program_id;
	}
	public void setProgram_id(String program_id) {
		this.program_id = program_id;
	}
	public String getProgram_name() {
		return program_name;
	}
	public void setProgram_name(String program_name) {
		this.program_name = program_name;
	}
	public String getLanguage_id() {
		return language_id;
	}
	public void setLanguage_id(String language_id) {
		this.language_id = language_id;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getPower() {
		return power;
	}
	public void setPower(String power) {
		this.power = power;
	}
	public String getService_area() {
		return service_area;
	}
	public void setService_area(String service_area) {
		this.service_area = service_area;
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
	public String getLocal_time() {
		return local_time;
	}
	public void setLocal_time(String local_time) {
		this.local_time = local_time;
	}
	public String getMon_area() {
		return mon_area;
	}
	public void setMon_area(String mon_area) {
		this.mon_area = mon_area;
	}
	public String getRest_datetime() {
		return rest_datetime;
	}
	public void setRest_datetime(String rest_datetime) {
		this.rest_datetime = rest_datetime;
	}
	public String getValid_start_time() {
		return valid_start_time;
	}
	public void setValid_start_time(String valid_start_time) {
		this.valid_start_time = valid_start_time;
	}
	public String getValid_end_time() {
		return valid_end_time;
	}
	public void setValid_end_time(String valid_end_time) {
		this.valid_end_time = valid_end_time;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSummer() {
		return summer;
	}
	public void setSummer(String summer) {
		this.summer = summer;
	}
	public String getSummer_starttime() {
		return summer_starttime;
	}
	public void setSummer_starttime(String summer_starttime) {
		this.summer_starttime = summer_starttime;
	}
	public String getSummer_endtime() {
		return summer_endtime;
	}
	public void setSummer_endtime(String summer_endtime) {
		this.summer_endtime = summer_endtime;
	}
}
