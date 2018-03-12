package com.viewscenes.bean.report;
/**
 * 频段效果统计bean
 * @author Administrator
 *
 */
public class ZrstRepFreqStatisticsBean {
	private String data_id = "";//
	private String report_id = "";			//报表id
	private String receive_name = "";		//遥控站名
	private String transmit_station = "";	//发射台
	private String language_name = "";		//语言
	private String play_time = "";			//各时段 如：01:00-02:00
	private String freq_listens = "";		//6到25m可听率集合 如：6_7_9_11_13_15_17_19_21_25
	private String sub_report_type = "";			//子报表类型 1、语言  2、发射台   3、时段
	
	public String getData_id() {
		return data_id;
	}
	public void setData_id(String data_id) {
		this.data_id = data_id;
	}
	public String getReport_id() {
		return report_id;
	}
	public void setReport_id(String report_id) {
		this.report_id = report_id;
	}
	public String getReceive_name() {
		return receive_name;
	}
	public void setReceive_name(String receive_name) {
		this.receive_name = receive_name;
	}
	public String getTransmit_station() {
		return transmit_station;
	}
	public void setTransmit_station(String transmit_station) {
		this.transmit_station = transmit_station;
	}
	public String getLanguage_name() {
		return language_name;
	}
	public void setLanguage_name(String language_name) {
		this.language_name = language_name;
	}
	public String getPlay_time() {
		return play_time;
	}
	public void setPlay_time(String play_time) {
		this.play_time = play_time;
	}
	public String getFreq_listens() {
		return freq_listens;
	}
	public void setFreq_listens(String freq_listens) {
		this.freq_listens = freq_listens;
	}
	public String getSub_report_type() {
		return sub_report_type;
	}
	public void setSub_report_type(String sub_report_type) {
		this.sub_report_type = sub_report_type;
	}
}
