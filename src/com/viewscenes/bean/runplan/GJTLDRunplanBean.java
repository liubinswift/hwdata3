package com.viewscenes.bean.runplan;

import com.viewscenes.bean.BaseBean;

public class GJTLDRunplanBean extends BaseBean {

	public GJTLDRunplanBean() {
		// TODO Auto-generated constructor stub
	}
	private String runplan_id;        //����ͼid
	private String runplan_type_id;   //����ͼ����id
	private String sentcity_id;       //�������id
	private String sentcity;          //�����������
	private String redisseminators;   //ת������
	private String freq;              //Ƶ��
	private String band;              //����
	private String direction;         //����
	private String program_id;        //��Ŀid
	private String program_name;      // ��Ŀ����
	private String language_id;       //����id
	private String language;          //��������
	private String power;             //����
	private String service_area;      //������
	private String start_time;       //������ʼʱ��
	private String end_time;         //��������ʱ��
	private String local_time ;       //����ʱ��
	private String mon_area;         //�����ղ�վ��
	private String mon_area_name;         //�����ղ�վ������
	private String rest_datetime;    //��Ϣ����
	private String valid_start_time; //������
	private String valid_end_time;  //ͣ����
	private String remark;           //��ע
	private String summer;           // ����ʱ
	private String summer_starttime; //����ʱ����ʱ��
	private String summer_endtime;   //����ʱͣ��ʱ��
	private String input_person;    //¼����
	private String launch_country;    //�������
	private String xg_mon_area;      //Ч���ղ�վ��
	private String xg_mon_area_name;      //Ч���ղ�վ������
    private String type_id; //վ������
    private String local_start_time;//���ز�����ʼʱ��
    private String local_end_time;//���ز�������ʱ��
    private String weekday;//������
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
