package com.viewscenes.bean;

/**
 * Ч��ͳ����ͨ����
 * @author thinkpad
 *
 */
public class EffectStatCommonBean implements Cloneable {

	String freq;			//Ƶ��
	String language_name;	//����
	String station_name;	//����̨����
	String rebroadcastorg;	//ת������
	String receivearea;		//�ղ����
	String receivedate;		//�ղ�ʱ��
	String o;				//������
	String start_time;		//����ͼ������ʼʱ��
	String end_time;		//����ͼ��������ʱ��	
	String disturb;			//����ͼ�ܸ������
	double level_value;		//��ƽֵ
	String service_area;	//����ͼ������
	
	
	int freqcount = 0;		//Ƶ����
	float freqtime  = 0;	//Ƶʱ��
	String headname;		//վ������
	int receivecount = 0;	//�ղ����
	float goodfreqtime = 0;//�ɱ�֤����Ƶʱ��	
	
	//�����ֶ�ֻ��ͳ����ɺ�ʹ��
	
	
//	int count = 0;			//�ղ��ܴ���
	
	int audibility1 = 0;	//�����������
	int audibility2 = 0;
	int audibility3 = 0;
	int audibility4 = 0;
	int audibility5 = 0;
	
	
	int audibilityMiddleValue = 0;	//��������ֵ
	
	
	double audible = 0;		//������
	String mark_datetime;
	
	public String getMark_datetime() {
		return mark_datetime;
	}
	public void setMark_datetime(String mark_datetime) {
		this.mark_datetime = mark_datetime;
	}
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
	@Override
	public EffectStatCommonBean clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (EffectStatCommonBean) super.clone();
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
	
	public double getLevel_value() {
		return level_value;
	}
	public void setLevel_value(double levelValue) {
		level_value = levelValue;
	}
	public int getFreqcount() {
		return freqcount;
	}
	public void setFreqcount(int freqcount) {
		this.freqcount = freqcount;
	}
	public float getFreqtime() {
		return freqtime;
	}
	public void setFreqtime(float freqtime) {
		this.freqtime = freqtime;
	}
	public String getHeadname() {
		return headname;
	}
	public void setHeadname(String headname) {
		this.headname = headname;
	}
	public int getReceivecount() {
		return receivecount;
	}
	public void setReceivecount(int receivecount) {
		this.receivecount = receivecount;
	}
	public float getGoodfreqtime() {
		return goodfreqtime;
	}
	public void setGoodfreqtime(float goodfreqcount) {
		this.goodfreqtime = goodfreqcount;
	}
	public String getService_area() {
		return service_area;
	}
	public void setService_area(String serviceArea) {
		service_area = serviceArea;
	}
	
	
	
}
