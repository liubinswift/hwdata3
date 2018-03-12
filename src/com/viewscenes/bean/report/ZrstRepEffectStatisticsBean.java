package com.viewscenes.bean.report;

import java.util.ArrayList;

public class ZrstRepEffectStatisticsBean {
	String data_id = "";			//
	String report_id = "";			//报表id
	String play_time = "";			//播音时间
	String language_name = "";		//语言
	String freq = "";				//频率
	String transmit_station = "";	//发射台
	String transmit_direction = "";	//发射方向
	String transmit_power = "";		//发射功率
	String service_area = "";		//服务区
	String receive_code = "";		//收测地点
	String receive_count = "";		//收测次数
	String fen0 = "";				//0分的
	String fen1 = "";				//1分的
	String fen2 = "";				//2分的
	String fen3 = "";				//3分的
	String fen4 = "";				//4分的
	String fen5 = "";				//5分的
	String listen = "";				//可听率%   可听度大于等于3分次数与总收听次数之比，用百分数表示
	String listen_middle = "";		//可听度中值
	
	String valid_start_time_temp = "";
	String valid_end_time_temp = "";
	String runplan_type_id_temp = "";
    String runplan_id = "";

	public String getRunplan_id() {
		return runplan_id;
	}


	public void setRunplan_id(String runplan_id) {
		this.runplan_id = runplan_id;
	}


	public String getValid_start_time_temp() {
		return valid_start_time_temp;
	}


	public void setValid_start_time_temp(String valid_start_time_temp) {
		this.valid_start_time_temp = valid_start_time_temp;
	}


	public String getValid_end_time_temp() {
		return valid_end_time_temp;
	}


	public void setValid_end_time_temp(String valid_end_time_temp) {
		this.valid_end_time_temp = valid_end_time_temp;
	}


	public String getRunplan_type_id_temp() {
		return runplan_type_id_temp;
	}


	public void setRunplan_type_id_temp(String runplan_type_id_temp) {
		this.runplan_type_id_temp = runplan_type_id_temp;
	}


	ArrayList listen_middleList = new ArrayList();		//可听度中值 临时存储
	


	public ArrayList getListen_middleList() {
		return listen_middleList;
	}


	public void setListen_middleList(ArrayList listen_middleList) {
		this.listen_middleList = listen_middleList;
	}


	String bak = "";				//备注
	String receive_name = "";		//遥控站名
	String transmit_station_listens = "";//发射台_>=3分_总次数_可听率%  例如：2022_88_123_78,2032_23_423_28
	String receive_name_total_hours = "";//遥控站名_总频时  例如：大阪_96,吉隆坡_34
	String receive_listens = "";		 //保证收听频时	_基本可收听频时	_有时可收听频时_	无法收听频时_总频时
	String language_name_listens = "";	 //语言_>=3分_总次数_可听率%  例如：德_88_123_78,2032_23_423_28
	String state_name = "";			//地区 如：欧洲
	String month_listens = "";		//各月可听率 一月_二月_三月_四月_五月_六月_七月_八月_九月_十月_十一月_十二月 如：1_2_3_4_5_7_8_8_2_10_33_44
	String average_listens = "";	//平均可听率%
	String sub_report_type = "";	//子报表类型  11：国际台广播效果统计；21：发射台总体播出效果统计1；22：发射台总体播出效果统计2；
												//23：发射台总体播出效果统计3；31：语言总体播出效果统计1；32：语言总体播出效果统计2；
												//41：各遥控站、各地区、各大洲、可听率统计；51：各遥控站、各地区、各大洲、可保证收听频时统计；61：各月可听率对比；71：频率平均可听率统计表
	String report_type = "";		//报表类型  1：国际台效果报告统计  2：海外落地效果报告统计
	
	String all_listens = "";        //地区或大洲_>=3分_总次数_可听率% 
	
	public String getAll_listens() {
		return all_listens;
	}


	public void setAll_listens(String all_listens) {
		this.all_listens = all_listens;
	}


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


	public String getPlay_time() {
		return play_time;
	}


	public void setPlay_time(String play_time) {
		this.play_time = play_time;
	}


	public String getLanguage_name() {
		return language_name;
	}


	public void setLanguage_name(String language_name) {
		this.language_name = language_name;
	}


	public String getFreq() {
		return freq;
	}


	public void setFreq(String freq) {
		this.freq = freq;
	}


	public String getTransmit_station() {
		return transmit_station;
	}


	public void setTransmit_station(String transmit_station) {
		this.transmit_station = transmit_station;
	}


	public String getTransmit_direction() {
		return transmit_direction;
	}


	public void setTransmit_direction(String transmit_direction) {
		this.transmit_direction = transmit_direction;
	}


	public String getTransmit_power() {
		return transmit_power;
	}


	public void setTransmit_power(String transmit_power) {
		this.transmit_power = transmit_power;
	}


	public String getService_area() {
		return service_area;
	}


	public void setService_area(String service_area) {
		this.service_area = service_area;
	}


	public String getReceive_code() {
		return receive_code;
	}


	public void setReceive_code(String receive_code) {
		this.receive_code = receive_code;
	}


	public String getReceive_count() {
		return receive_count;
	}


	public void setReceive_count(String receive_count) {
		this.receive_count = receive_count;
	}


	public String getFen0() {
		return fen0;
	}


	public void setFen0(String fen0) {
		this.fen0 = fen0;
	}


	public String getFen1() {
		return fen1;
	}


	public void setFen1(String fen1) {
		this.fen1 = fen1;
	}


	public String getFen2() {
		return fen2;
	}


	public void setFen2(String fen2) {
		this.fen2 = fen2;
	}


	public String getFen3() {
		return fen3;
	}


	public void setFen3(String fen3) {
		this.fen3 = fen3;
	}


	public String getFen4() {
		return fen4;
	}


	public void setFen4(String fen4) {
		this.fen4 = fen4;
	}


	public String getFen5() {
		return fen5;
	}


	public void setFen5(String fen5) {
		this.fen5 = fen5;
	}


	public String getListen() {
		return listen;
	}


	public void setListen(String listen) {
		this.listen = listen;
	}


	public String getListen_middle() {
		return listen_middle;
	}


	public void setListen_middle(String listen_middle) {
		this.listen_middle = listen_middle;
	}


	public String getBak() {
		return bak;
	}


	public void setBak(String bak) {
		this.bak = bak;
	}


	public String getReceive_name() {
		return receive_name;
	}


	public void setReceive_name(String receive_name) {
		this.receive_name = receive_name;
	}


	public String getTransmit_station_listens() {
		return transmit_station_listens;
	}


	public void setTransmit_station_listens(String transmit_station_listens) {
		this.transmit_station_listens = transmit_station_listens;
	}


	public String getReceive_name_total_hours() {
		return receive_name_total_hours;
	}


	public void setReceive_name_total_hours(String receive_name_total_hours) {
		this.receive_name_total_hours = receive_name_total_hours;
	}


	public String getReceive_listens() {
		return receive_listens;
	}


	public void setReceive_listens(String receive_listens) {
		this.receive_listens = receive_listens;
	}


	public String getLanguage_name_listens() {
		return language_name_listens;
	}


	public void setLanguage_name_listens(String language_name_listens) {
		this.language_name_listens = language_name_listens;
	}


	public String getState_name() {
		return state_name;
	}


	public void setState_name(String state_name) {
		this.state_name = state_name;
	}


	public String getMonth_listens() {
		return month_listens;
	}


	public void setMonth_listens(String month_listens) {
		this.month_listens = month_listens;
	}


	public String getAverage_listens() {
		return average_listens;
	}


	public void setAverage_listens(String average_listens) {
		this.average_listens = average_listens;
	}


	public String getSub_report_type() {
		return sub_report_type;
	}


	public void setSub_report_type(String sub_report_type) {
		this.sub_report_type = sub_report_type;
	}


	public String getReport_type() {
		return report_type;
	}


	public void setReport_type(String report_type) {
		this.report_type = report_type;
	}


	public ZrstRepEffectStatisticsBean()
	{
	}
}
