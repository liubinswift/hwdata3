package com.viewscenes.util.report.FChart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Collection;


public class SetBean {
	private String label=null;		//������ͼ�������ֳ���������
	private String value=null; 		//������ͼ���и����������Ӧ��ֵ
	private String color=null; 		//������ͼ�������Ӧ������ͼ����ɫ
	private String hoverText=null; 	//���������ͣ�����Ӧ������ͼ �ϳ��ֵ��ı�����
	private String link=null; 		//���ø�����ͼ�����ӵ�ַ����ҪURL Encode�ر��룩
	private String alpha=null; 		//������ͼ�������Ӧ������ͼ��͸����
	private String showName=null; 	//�������Ƿ���ʾͼ�������Ӧ������ͼ��name
	private String dataset=null; 	//����ͼ��ʹ��
	
	public static ArrayList colorlist =new ArrayList();
	static {
		colorlist.add("AFD8F8");
		colorlist.add("F6BD0F");
		colorlist.add("8BBA00");
		colorlist.add("FF8E46");
		colorlist.add("008E8E");
		colorlist.add("D64646");
		colorlist.add("8E468E");
		colorlist.add("588526");
		colorlist.add("B3AA00");
		colorlist.add("008ED6");
		colorlist.add("9D080D");
		colorlist.add("A186BE");

	}
	/**
	 * ȡ����ͼ�������ֳ���������
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * ������ͼ�������ֳ���������
	 * @param label String
	 * ��: ��һ
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * ȡ����ͼ���и����������Ӧ��ֵ
	 */
	public String getValue() {
		return value;
	}
	/**
	 * ������ͼ���и����������Ӧ��ֵ
	 * @param value String
	 * ��: 98.2319
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * ȡ����ͼ�������Ӧ������ͼ����ɫ
	 */
	public String getColor() {
		return color;
	}
	/**
	 * ������ͼ�������Ӧ������ͼ����ɫ
	 * @param color HexCode
	 * ��: 000000
	 */
	public void setColor(String color) {
		this.color = color;
	}
	/**
	 * ȡ�������ͣ�����Ӧ������ͼ �ϳ��ֵ��ı�����
	 */
	public String getHoverText() {
		return hoverText;
	}
	/**
	 * ���������ͣ�����Ӧ������ͼ �ϳ��ֵ��ı�����
	 * @param hoverText String
	 * ��: �½�����
	 */
	public void setHoverText(String hoverText) {
		this.hoverText = hoverText;
	}
	/**
	 * ȡ�ø�����ͼ�����ӵ�ַ
	 */
	public String getLink() {
		return link;
	}
	/**
	 * ���ø�����ͼ�����ӵ�ַ����ҪURL Encode�ر��룩
	 * @param link String
	 * ��: /%BD%F0%CA%AF%CD%FE%CA%D3.html (/��ʯ����.html)
	 */
	public void setLink(String link) {
		this.link = link;
	}
	/**
	 * ȡ����ͼ�������Ӧ������ͼ��͸����
	 */
	public String getAlpha() {
		return alpha;
	}
	/**
	 * ������ͼ�������Ӧ������ͼ��͸����
	 * @param alpha Numerical Value
	 * ��: 0~100
	 */
	public void setAlpha(String alpha) {
		this.alpha = alpha;
	}
	/**
	 * ȡ�����Ƿ���ʾͼ�������Ӧ������ͼ��name
	 */
	public String getShowName() {
		return showName;
	}
	/**
	 * �������Ƿ���ʾͼ�������Ӧ������ͼ��name
	 * @param showName boolean
	 * ��: 1/0
	 */
	public void setShowName(String showName) {
		this.showName = showName;
	}
	
	public String getDataset() {
		return dataset;
	}
	public void setDataset(String dataset) {
		this.dataset = dataset;
	}
	
	
	
}
