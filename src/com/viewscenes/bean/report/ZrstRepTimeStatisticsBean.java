package com.viewscenes.bean.report;
/**
 * 时段效果统计bean
 * @author Administrator
 *
 */
public class ZrstRepTimeStatisticsBean {

	private String data_id = "";//
	private String report_id = "";			//报表id
	private String receive_name = "";		//遥控站名
	private String transmit_station = "";	//发射台
	private String language_name = "";		//语言
	private String freq_type = "";			//频率类型 如：6M
	private String time_listens = "";		//24小时时间段的可听率集合 如：1_2_3_4_5_6_7_8_9_10_11_12_13_14_15_16_17_18_19_20_21_22_23_24
	private String sub_report_type = "";			//子报表类型 1、语言  2、发射台   3、频段
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
	public String getFreq_type() {
		return freq_type;
	}
	public void setFreq_type(String freq_type) {
		this.freq_type = freq_type;
	}
	public String getTime_listens() {
		return time_listens;
	}
	public void setTime_listens(String time_listens) {
		this.time_listens = time_listens;
	}
	public String getSub_report_type() {
		return sub_report_type;
	}
	public void setSub_report_type(String sub_report_type) {
		this.sub_report_type = sub_report_type;
	}
}
