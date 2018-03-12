package com.viewscenes.bean;

/**
 * 基于频率的效果统计类与结果类
 * @author thinkpad
 *
 */
public class EffectStatByFreqBean implements Cloneable {

	String freq;
	String language_name;
	String station_name;
	String rebroadcastorg;
	String receivearea;
	String receivedate;
	String o;
	String start_tinme;
	String end_time;
	String disturb;
	double level_value;
	
	//下面字段只在统计完成后使用
	
	//收测总次数
	int count = 0;
	//可听度满意度
	int audibility1 = 0;
	int audibility2 = 0;
	int audibility3 = 0;
	int audibility4 = 0;
	int audibility5 = 0;
	
	//可听度中值
	int audibilityMiddleValue = 0;
	
	//可听率
	double audible = 0;
	
	public String getFreq() {
		return freq;
	}
	public void setFreq(String freq) {
		this.freq = freq;
	}
	public String getLanguage_name() {
		return language_name;
	}
	public void setLanguage_name(String languageName) {
		language_name = languageName;
	}
	public String getStation_name() {
		return station_name;
	}
	public void setStation_name(String stationName) {
		station_name = stationName;
	}
	public String getRebroadcastorg() {
		return rebroadcastorg;
	}
	public void setRebroadcastorg(String rebroadcastorg) {
		this.rebroadcastorg = rebroadcastorg;
	}
	public String getReceivearea() {
		return receivearea;
	}
	public void setReceivearea(String receivearea) {
		this.receivearea = receivearea;
	}
	public String getReceivedate() {
		return receivedate;
	}
	public void setReceivedate(String receivetime) {
		this.receivedate = receivetime;
	}
	public String getO() {
		return o;
	}
	public void setO(String o) {
		this.o = o;
	}
	public String getStart_tinme() {
		return start_tinme;
	}
	public void setStart_tinme(String startTinme) {
		start_tinme = startTinme;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String endTime) {
		end_time = endTime;
	}
	@Override
	public EffectStatByFreqBean clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (EffectStatByFreqBean) super.clone();
	}
	public int getAudibility1() {
		return audibility1;
	}
	public void setAudibility1(int audibility1) {
		this.audibility1 = audibility1;
	}
	public int getAudibility2() {
		return audibility2;
	}
	public void setAudibility2(int audibility2) {
		this.audibility2 = audibility2;
	}
	public int getAudibility3() {
		return audibility3;
	}
	public void setAudibility3(int audibility3) {
		this.audibility3 = audibility3;
	}
	public int getAudibility4() {
		return audibility4;
	}
	public void setAudibility4(int audibility4) {
		this.audibility4 = audibility4;
	}
	public int getAudibility5() {
		return audibility5;
	}
	public void setAudibility5(int audibility5) {
		this.audibility5 = audibility5;
	}
	public int getAudibilityMiddleValue() {
		return audibilityMiddleValue;
	}
	public void setAudibilityMiddleValue(int audibilityMiddleValue) {
		this.audibilityMiddleValue = audibilityMiddleValue;
	}
	public double getAudible() {
		return audible;
	}
	public void setAudible(double audible) {
		this.audible = audible;
	}
	public String getDisturb() {
		return disturb;
	}
	public void setDisturb(String disturb) {
		this.disturb = disturb;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public double getLevel_value() {
		return level_value;
	}
	public void setLevel_value(double levelValue) {
		level_value = levelValue;
	}
	
	
}
