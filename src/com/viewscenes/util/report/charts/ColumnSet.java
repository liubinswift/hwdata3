package com.viewscenes.util.report.charts;

/**
 * <p>2D��״ͼ<Column2D>,3D��״ͼ<Column3D>����Դ<set> </p>
 * @author ��ʿ��
 * @version 1.0
 */
public class ColumnSet {
	
	
	private String label   = null;			//�������ݽڵ�ı�ǩ
	private String value    = null;			//�������ݽڵ��ֵ
	private String displayValue	= null;		//�����Զ������ݽڵ��ֵ
	private String color    = null;			//�������ݽڵ����ɫ
	private String link    = null;			//�������ݽڵ������
	private String toolText    = null;		//�������ݽڵ���ı�
	private String showLabel    = null;		//�����Ƿ���ʾ���ݽڵ�ı�ǩ
	private String showValue    = null;		//�����Ƿ���ʾ���ݽڵ��ֵ
	private String dashed   = null;			//�������ݽڵ�ı߽��Ƿ�������
	private String alpha    = null;			//�������ݽڵ��͸����
	
	
	

	/**
	 * ����ͼ������ݽڵ�����XML
	 * @return ͼ������ݽڵ�����XML
	 */
	public String BuildSetXML(){
		StringBuffer setXML = new StringBuffer();
		setXML.append("<set ");
		
		
		//���ñ�ǩ������
		if(this.getLabel()!=null){
			setXML.append(" label = '" +this.label + "' \t");
		}
		//���ñ�ǩ��ֵ
		if(this.getValue()!=null){
			setXML.append(" value = '" +this.value + "' \t");
		}
		//������ʾ�Զ����ֵ
		if(this.getDisplayValue()!=null){
			setXML.append(" displayValue = '" +this.displayValue + "' \t");
		}
		//���ñ�ǩ����ɫ
		if(this.getColor()!=null){
			setXML.append(" color = '" +this.color + "' \t");
		}
		//���ñ�ǩ������
		if(this.getLink()!=null){
			setXML.append(" link = '" +this.link + "' \t");
		}
		//������ʾ��ǩ���ı�
		if(this.getToolText()!=null){
			setXML.append(" toolText = '" +this.toolText + "' \t");
		}
		//�����Ƿ���ʾ��ǩ
		if(this.getShowLabel()!=null){
			setXML.append(" showLabel = '" +this.showLabel + "' \t");
		}
		//�����Ƿ���ʾֵ
		if(this.getShowValue()!=null){
			setXML.append(" showValue = '" +this.showValue + "' \t");
		}
		//�����������߽��Ƿ�������
		if(this.getDashed()!=null){
			setXML.append(" dashed = '" +this.dashed + "' \t");
		}
		//���ñ�ǩ��͸����
		if(this.getAlpha()!=null){
			setXML.append(" alpha = '" +this.alpha + "' \t");
		}
		
		
		
		
		setXML.append("/> \n");
		return setXML.toString();
	}
	
	
	
	/**
	 * ��ȡ���ݽڵ�ı�ǩ
	 * @return
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * �������ݽڵ�ı�ǩ
	 * @param label
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * ��ȡ�������ݽڵ��ֵ
	 * @return
	 */
	public String getValue() {
		return value;
	}
	/**
	 * �����������ݽڵ��ֵ
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * ��ȡ�Զ������ݽڵ��ֵ
	 * @return
	 */
	public String getDisplayValue() {
		return displayValue;
	}
	/**
	 * �����Զ������ݽڵ��ֵ
	 * @param displayValue
	 */
	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}
	/**
	 * ��ȡ���ݽڵ����ɫ
	 * @return
	 */
	public String getColor() {
		return color;
	}
	/**
	 * �������ݽڵ����ɫ
	 * @param color
	 */
	public void setColor(String color) {
		this.color = color;
	}
	/**
	 * ��ȡ���ݽڵ������
	 * @return
	 */
	public String getLink() {
		return link;
	}
	/**
	 * �������ݽڵ������
	 * @param link
	 */
	public void setLink(String link) {
		this.link = link;
	}
	/**
	 * ��ȡ���ݽڵ���ı�
	 * @return
	 */
	public String getToolText() {
		return toolText;
	}
	/**
	 * �������ݽڵ���ı�
	 * @param toolText
	 */
	public void setToolText(String toolText) {
		this.toolText = toolText;
	}
	/**
	 * ��ȡ�Ƿ���ʾ���ݽڵ�ı�ǩ
	 * @return
	 */
	public String getShowLabel() {
		return showLabel;
	}
	/**
	 * �����Ƿ���ʾ���ݽڵ�ı�ǩ
	 * @param showLabel
	 */
	public void setShowLabel(String showLabel) {
		this.showLabel = showLabel;
	}
	/**
	 * ��ȡ�Ƿ���ʾ���ݽڵ��ֵ
	 * @return
	 */
	public String getShowValue() {
		return showValue;
	}
	/**
	 * �����Ƿ���ʾ���ݽڵ��ֵ
	 * @param showValue
	 */
	public void setShowValue(String showValue) {
		this.showValue = showValue;
	}
	/**
	 * ��ȡ���ݽڵ�ı߽��Ƿ�������
	 * @return
	 */
	public String getDashed() {
		return dashed;
	}
	/**
	 * �������ݽڵ�ı߽��Ƿ�������
	 * @param dashed
	 */
	public void setDashed(String dashed) {
		this.dashed = dashed;
	}
	/**
	 * ��ȡ���ݽڵ��͸����
	 * @return
	 */
	public String getAlpha() {
		return alpha;
	}
	/**
	 * �������ݽڵ��͸����
	 * @param alpha
	 */
	public void setAlpha(String alpha) {
		this.alpha = alpha;
	}
}
