package com.viewscenes.bean.task;

import java.util.ArrayList;

/**
 * *************************************   
*    
* ��Ŀ���ƣ�HW   
* �����ƣ�Subtask   
* ��������   
* �����ˣ�����
* ����ʱ�䣺2012-7-19 ����03:01:13   
* �޸��ˣ�����
* �޸�ʱ�䣺2012-7-19 ����03:01:13   
* �޸ı�ע��   
* @version    
*    
***************************************
 */
public class Subtask {

	public Subtask() {
		// TODO Auto-generated constructor stub
	}

	private String sub_task_id;

	private String band;

	private String startfreq;
	private String endfreq;
	private String stepfreq;
	private String task_id;
	private String from_runplan;
	private String is_delete;
	private String equ_code;
	private String bps;
	

	public String getBps() {
		return bps;
	}
	public void setBps(String bps) {
		this.bps = bps;
	}

	private String freq;
	public String getFreq() {
		return freq;
	}
	public void setFreq(String freq) {
		this.freq = freq;
	}

	private ArrayList<CycleSubTask> cyctask;
	private ArrayList<SingleSubTask> sintask;
	
	public ArrayList<CycleSubTask> getCyctask() {
		return cyctask;
	}
	public void setCyctask(ArrayList<CycleSubTask> cyctask) {
		this.cyctask = cyctask;
	}
	public ArrayList<SingleSubTask> getSintask() {
		return sintask;
	}
	public void setSintask(ArrayList<SingleSubTask> sintask) {
		this.sintask = sintask;
	}
	public String getSub_task_id() {
		return sub_task_id;
	}
	public void setSub_task_id(String sub_task_id) {
		this.sub_task_id = sub_task_id;
	}

	public String getBand() {
		return band;
	}
	public void setBand(String band) {
		this.band = band;
	}

	public String getStartfreq() {
		return startfreq;
	}
	public void setStartfreq(String startfreq) {
		this.startfreq = startfreq;
	}
	public String getEndfreq() {
		return endfreq;
	}
	public void setEndfreq(String endfreq) {
		this.endfreq = endfreq;
	}
	public String getStepfreq() {
		return stepfreq;
	}
	public void setStepfreq(String stepfreq) {
		this.stepfreq = stepfreq;
	}
	public String getTask_id() {
		return task_id;
	}
	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}
	public String getFrom_runplan() {
		return from_runplan;
	}
	public void setFrom_runplan(String from_runplan) {
		this.from_runplan = from_runplan;
	}
	public String getIs_delete() {
		return is_delete;
	}
	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}
	public String getEqu_code() {
		return equ_code;
	}
	public void setEqu_code(String equ_code) {
		this.equ_code = equ_code;
	}



}
