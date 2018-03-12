package com.viewscenes.bean.runplan;

import com.viewscenes.bean.BaseBean;

/**
 * ���̨����ͼ��
 * @author ������
 * @date 2012/07/18
 */
public class WGTRunplanBean extends BaseBean {

	public WGTRunplanBean() {
		// TODO Auto-generated constructor stub
	}
	
	private String runplan_id;      //����ͼid
	private String runplan_type_id; //����ͼ����
	private String broadcast_station;     //������̨
	private String broadcast_country;  //��������
	private String launch_country;  //�������
	private String sentcity_id;     //�������id
	private String sentcity;        //�����������
	private String station_id;      //����̨id
	private String station_name;    //����̨����
	private String transmiter_no;   //�������
	private String freq;            //Ƶ��
	private String antenna;         //���ߺ�
	private String antennatype;     //���߳�ʽ
	private String direction;       //����
	private String language_id;     //����id
	private String language;        //����
	private String power;           //����
	private String service_area;    //������
	private String ciraf;           //CIRAF��
	private String start_time;      //������ʼʱ��
	private String end_time;        //��������ʱ��
	private String mon_area;        //�����ղ�վ��
	private String rest_datetime;   //��Ϣ����
	private String rest_time;       //��Ϣʱ��
	private String valid_start_time;//������
	private String valid_end_time;  //ͣ����
	private String remark;          //��ע
	private String season_id;       //���ڴ���
	private String input_person;    //¼����
	private String xg_mon_area;     //Ч���ղ�վ��
	private String local_start_time;//���ز�����ʼʱ��
	private String local_end_time;//���ز�������ʱ��
	
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
	public String getXg_mon_area() {
		return xg_mon_area;
	}
	public void setXg_mon_area(String xg_mon_area) {
		this.xg_mon_area = xg_mon_area;
	}
	public String getInput_person() {
		return input_person;
	}
	public void setInput_person(String input_person) {
		this.input_person = input_person;
	}
	public String getSeason_id() {
		return season_id;
	}
	public void setSeason_id(String season_id) {
		this.season_id = season_id;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
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
	public String getBroadcast_station() {
		return broadcast_station;
	}
	public void setBroadcast_station(String broadcast_station) {
		this.broadcast_station = broadcast_station;
	}
	public String getBroadcast_country() {
		return broadcast_country;
	}
	public void setBroadcast_country(String broadcast_country) {
		this.broadcast_country = broadcast_country;
	}
	public String getLaunch_country() {
		return launch_country;
	}
	public void setLaunch_country(String launch_country) {
		this.launch_country = launch_country;
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
	public String getStation_id() {
		return station_id;
	}
	public void setStation_id(String station_id) {
		this.station_id = station_id;
	}
	public String getStation_name() {
		return station_name;
	}
	public void setStation_name(String station_name) {
		this.station_name = station_name;
	}
	public String getTransmiter_no() {
		return transmiter_no;
	}
	public void setTransmiter_no(String transmiter_no) {
		this.transmiter_no = transmiter_no;
	}
	public String getFreq() {
		return freq;
	}
	public void setFreq(String freq) {
		this.freq = freq;
	}
	public String getAntenna() {
		return antenna;
	}
	public void setAntenna(String antenna) {
		this.antenna = antenna;
	}
	public String getAntennatype() {
		return antennatype;
	}
	public void setAntennatype(String antennatype) {
		this.antennatype = antennatype;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getLanguage_id() {
		return language_id;
	}
	public void setLanguage_id(String language_id) {
		this.language_id = language_id;
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
	public String getCiraf() {
		return ciraf;
	}
	public void setCiraf(String ciraf) {
		this.ciraf = ciraf;
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
	public String getRest_time() {
		return rest_time;
	}
	public void setRest_time(String rest_time) {
		this.rest_time = rest_time;
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
	
}
