package com.viewscenes.bean;

/**
 * ����ʶ�������������
 * 
 * @author thinkpad
 * 
 */
public class ASRCmdBean {
	// �������ͣ�WorkFlow;TaskStatus
	private String type = "TaskStatus";
	// ����������:BC573
	private String wfType = "BC573";
	// ¼���ļ�����
	private String file;
	// �ļ���¼����ʼʱ��
	private String fileStartTime;
	// �ļ���¼������ʱ��
	private String fileEndTime;
	// ����������Чʱ��εĿ�ʼʱ��
	private String taskStartTime;
	// ����������Чʱ��εĽ���ʱ��
	private String taskEndTime;
	//Ƶ��
	private String freq;
	//����
	private String language;
	// �ɼ���ʽ��¼����ʽ��ң��վʵʱ1��ң��վ�ش�2���ɼ���ʵʱ3���ɼ���ش�4��
	private String collectMethod;
	// �ο�Դͨ�������������֣���1��2����21��
	private String collectChannel;

	
	/**************************************************
	 *      2012-11-5 �����ӽӿ�����
	 * 		̷��ΰ
	 ************************************************/
	//���ջ�����(545/713/NI1000)
	private String receiverType;
	
	//ҵ��ϵͳID��(�ַ���������_����)
	private String taskId;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getWfType() {
		return wfType;
	}

	public void setWfType(String wfType) {
		this.wfType = wfType;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getFileStartTime() {
		return fileStartTime;
	}

	public void setFileStartTime(String fileStartTime) {
		this.fileStartTime = fileStartTime;
	}

	public String getFileEndTime() {
		return fileEndTime;
	}

	public void setFileEndTime(String fileEndTime) {
		this.fileEndTime = fileEndTime;
	}

	public String getTaskStartTime() {
		return taskStartTime;
	}

	public void setTaskStartTime(String taskStartTime) {
		this.taskStartTime = taskStartTime;
	}

	public String getTaskEndTime() {
		return taskEndTime;
	}

	public void setTaskEndTime(String taskEndTime) {
		this.taskEndTime = taskEndTime;
	}

	
	
	public String getFreq() {
		return freq;
	}

	public void setFreq(String freq) {
		this.freq = freq;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCollectMethod() {
		return collectMethod;
	}

	public void setCollectMethod(String collectMethod) {
		this.collectMethod = collectMethod;
	}

	public String getCollectChannel() {
		return collectChannel;
	}

	public void setCollectChannel(String collectChannel) {
		this.collectChannel = collectChannel;
	}

	public String getReceiverType() {
		return receiverType;
	}

	public void setReceiverType(String receiverType) {
		this.receiverType = receiverType;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	
}
