package com.viewscenes.bean.report;
/**
 * Ƶ��Ч��ͳ��bean
 * @author Administrator
 *
 */
public class ZrstRepFreqStatisticsBean {
	private String data_id = "";//
	private String report_id = "";			//����id
	private String receive_name = "";		//ң��վ��
	private String transmit_station = "";	//����̨
	private String language_name = "";		//����
	private String play_time = "";			//��ʱ�� �磺01:00-02:00
	private String freq_listens = "";		//6��25m�����ʼ��� �磺6_7_9_11_13_15_17_19_21_25
	private String sub_report_type = "";			//�ӱ������� 1������  2������̨   3��ʱ��
	
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
