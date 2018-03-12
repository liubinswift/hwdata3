package com.viewscenes.util.report.FChart;

public class GraphBean {
	//flash����������
	private String bgColor=null;
	private String bgAlpha=null;
	private String bgSWF=null;
	private String bgRatio=null;
	private String bgAngle=null;
	
	//ͼ����������
	private String canvasBgColor=null;
	private String canvasBaseColor=null;
	private String canvasBaseDepth=null;
	private String canvasBgDepth=null;
	private String showCanvasBg=null;
	private String showCanvasBase=null;
	//ͼ�����ı���
	private String caption=null; // ͼ���Ϸ��ı���
	private String subCaption= null; // ͼ���Ϸ��ĸ�����
	private String xAxisName= null; // X�������
	private String yAxisName= null; // y�������
	//ͼ������ֵ������
	private String yAxisMinValue=null;// y����Сֵ
	private String yAxisMaxValue=null;// y�����ֵ
	//ͨ�ò���
	private String shownames=null; // �����Ƿ���x������ʾset��ָ����name
	private String showValues=null; // �����Ƿ�������ͼ�����ͼ����ʾ���ݵ�ֵ
	private String showLimits=null; // �����Ƿ���ͼ���y����������ʾ�����С������ֵ
	private String rotateNames=null; // ����X���µ�name ��ˮƽ��ʾ���Ǵ�ֱ��ʾ
	private String rotateYAxisName=null; // ����Y���µ�name ��ˮƽ��ʾ���Ǵ�ֱ��ʾ
	private String animation=null; // ������ʾ�Ƿ��Ƕ�����ʾ
	private String showBorder=null; 
	
	//��������
	private String baseFont=null; // ����������ʽ
	private String baseFontSize=null; // ���������С
	private String baseFontColor=null; // ����������ɫ
	private String outCnvBaseFont = null; // ����ͼ������������ʽ
	private String outCnvBaseFontSze=null; // ����ͼ�����������С
	private String outCnvBaseFontColor=null;// ����ͼ������������ɫ
	//���ָ�ʽѡ��
	private String numberPrefix=null; // ��������ֵ��ǰ׺
	private String numberSuffix=null ;// ��������ֵ�ĺ�׺������������ַ�����Ҫʹ��URL Encode�ر��룩
	private String formatNumber=null; // �����Ƿ��ʽ������
	private String formatNumberScale=null; // �����Ƿ���"K"������ǧ��"M"���������
	private String decimalSeparator=null; // ��ָ�����ַ�������С����
	private String thousandSeparator=null; // ��ָ�����ַ�������ǧλ�ָ���
	private String decimalPrecision=null; // ��������������ֵ��С��λ��
	private String divLineDecimalPrecision=null;// ����y����ֵ��С��λ��
	private String limitsDecimalPrecision=null ; // ����y��������Сֵ��С��λ��
	//ˮƽ�ָ���
	private String numdivlines=null; // ����ˮƽ�ָ��ߵ�����
	private String divlinecolor=null; // ����ˮƽ�ָ��ߵ���ɫ
	private String divLineThickness=null; // ����ˮƽ�ָ��ߵĿ��
	private String divLineAlpha=null; // ����ˮƽ�ָ��ߵ�͸����
	private String showDivLineValue=null; // �����Ƿ���ʾˮƽ�ָ��ߵ���ֵ
	//�����ͣ����
	private String showhovercap=null; // ��ʾ�Ƿ񼤻������ͣЧ��
	private String hoverCapBgColor=null; // ���������ͣЧ���ı�����ɫ
	private String hoverCapBorderColor=null; // ���������ͣЧ���ı߿���ɫ
	private String hoverCapSepChar=null; // ���������ͣ����ʾ���ı��еķָ�����
	//ͼ��߾������
	private String chartLeftMargin=null; // ����ͼ����߾�
	private String chartRightMargin=null; // ����ͼ���ұ߾�
	private String chartTopMargin=null; // ����ͼ���ϱ߾�
	private String chartBottomMargin=null; // ����ͼ���±߾�
	//Zero Plane
	//The zero plane is a 3D plane that signifies the 0 position on the chart. If there are no negative numbers on the chart, you won��t see a visible zero plane.
	private String zeroPlaneShowBorder=null; // Whether the border of a 3D zero plane would be plotted or not.
	private String zeroPlaneBorderColor=null; // If the border is to be plotted, this attribute sets the border color for the plane.
	private String zeroPlaneColor=null; // The intended color for the zero plane.
	private String zeroPlaneAlpha=null; // The intended transparency for the zero plane. 

	
	/**
	 * ȡ��flash�ı�����ɫ
	 */
	public String getBgColor() {
		return bgColor;
	}
	/**
	 * ����flash�ı�����ɫ
	 * @param bgColor HexColorCode
	 * ��: ffffff
	 */
	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}
	/**
	 * ȡ�ñ�����͸����
	 */
	public String getBgAlpha() {
		return bgAlpha;
	}
	
	/**
	 * ���ñ�����͸����
	 * @param bgAlpha Numerical Value
	 * ��: 0~100
	 */
	public void setBgAlpha(String bgAlpha) {
		this.bgAlpha = bgAlpha;
	}
	/**
	 * ȡ��һ���ⲿ��Flash Ϊflash�ı���
	 */
	public String getBgSWF() {
		return bgSWF;
	}
	/**
	 * ����һ���ⲿ��Flash Ϊflash�ı���
	 * @param bgSWF Path of SWF File
	 * ��: C:\demo.swf
	 */
	public void setBgSWF(String bgSWF) {
		this.bgSWF = bgSWF;
	}
	/**
	 * ȡ�ñ���ɫ�Ľ������ 
	 */
	public String getBgRatio() {
		return bgRatio;
	}
	/**
	 * ���ñ���ɫ�Ľ������ 
	 * @param bgRatio  Numerical Value
	 * ��: 0~100
	 */
	public void setBgRatio(String bgRatio) {
		this.bgRatio = bgRatio;
	}
	/**
	 * ȡ�ñ���ɫ�Ĳ����Ƕ�
	 */
	public String getBgAngle() {
		return bgAngle;
	}
	/**
	 * ���ñ���ɫ�Ĳ����Ƕ�
	 * @param bgAngle Numerical Value
	 * ��: 0~360
	 */
	public void setBgAngle(String bgAngle) {
		this.bgAngle = bgAngle;
	}
	/**
	 * ȡ��ͼ��������ɫ
	 */
	public String getCanvasBgColor() {
		return canvasBgColor;
	}
	/**
	 * ����ͼ��������ɫ
	 * @param canvasBgColor HexColorCode
	 * ��: ffffff
	 */
	public void setCanvasBgColor(String canvasBgColor) {
		this.canvasBgColor = canvasBgColor;
	}
	/**
	 * ȡ��ͼ���������ɫ
	 */
	public String getCanvasBaseColor() {
		return canvasBaseColor;
	}
	/**
	 * ����ͼ���������ɫ
	 * @param canvasBaseColor HexColorCode
	 * ��: ffffff
	 */
	public void setCanvasBaseColor(String canvasBaseColor) {
		this.canvasBaseColor = canvasBaseColor;
	}
	/**
	 * ȡ��ͼ���������
	 */
	public String getCanvasBaseDepth() {
		return canvasBaseDepth;
	}
	/**
	 * ����ͼ���������
	 * @param canvasBaseDepth Numerical Value
	 * ��: 10
	 */
	public void setCanvasBaseDepth(String canvasBaseDepth) {
		this.canvasBaseDepth = canvasBaseDepth;
	}
	/**
	 * ȡ��ͼ���������
	 */
	public String getCanvasBgDepth() {
		return canvasBgDepth;
	}
	/**
	 * ����ͼ���������
	 * @param canvasBgDepth Numerical Value
	 * ��: 10
	 */
	public void setCanvasBgDepth(String canvasBgDepth) {
		this.canvasBgDepth = canvasBgDepth;
	}
	/**
	 * ȡ���Ƿ���ʾͼ����
	 */
	public String getShowCanvasBg() {
		return showCanvasBg;
	}
	/**
	 * �����Ƿ���ʾͼ����
	 * @param showCanvasBg boolean
	 * ��: 1/0
	 */
	public void setShowCanvasBg(String showCanvasBg) {
		this.showCanvasBg = showCanvasBg;
	}
	/**
	 * ȡ���Ƿ���ʾͼ�����
	 */
	public String getShowCanvasBase() {
		return showCanvasBase;
	}
	/**
	 * �����Ƿ���ʾͼ�����
	 * @param showCanvasBase boolean
	 * ��: 1/0
	 */
	public void setShowCanvasBase(String showCanvasBase) {
		this.showCanvasBase = showCanvasBase;
	}
	/**
	 * ȡ��ͼ���Ϸ��ı���
	 */
	public String getCaption() {
		return caption;
	}
	/**
	 * ����ͼ���Ϸ��ı���
	 * @param caption String
	 * ��: Ƶ�β��ϸ�
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
	/**
	 * ȡ��ͼ���Ϸ��ĸ�����
	 */
	public String getSubCaption() {
		return subCaption;
	}
	/**
	 * ����ͼ���Ϸ��ı���
	 * @param subCaption String
	 * ��: 2009-06-01~2009-06-07
	 */
	public void setSubCaption(String subCaption) {
		this.subCaption = subCaption;
	}
	/**
	 * ȡ��X�������
	 */
	public String getXAxisName() {
		return xAxisName;
	}
	/**
	 * ����X�������
	 * @param axisName String
	 * ��: ����
	 */
	public void setXAxisName(String axisName) {
		xAxisName = axisName;
	}
	/**
	 * ȡ��Y�������
	 */
	public String getYAxisName() {
		return yAxisName;
	}
	/**
	 * ����ȡ��Y�������
	 * @param axisName
	 */
	public void setYAxisName(String axisName) {
		yAxisName = axisName;
	}
	/**
	 * ȡ��Y����Сֵ
	 */
	public String getYAxisMinValue() {
		return yAxisMinValue;
	}
	/**
	 * ����Y����Сֵ
	 * @param axisMinValue value
	 * ��: 90
	 */
	public void setYAxisMinValue(String axisMinValue) {
		yAxisMinValue = axisMinValue;
	}
	/**
	 * ȡ��Y�����ֵ
	 */
	public String getYAxisMaxValue() {
		return yAxisMaxValue;
	}
	/**
	 * ����Y�����ֵ
	 * @param axisMaxValue value
	 * ��: 100
	 */
	public void setYAxisMaxValue(String axisMaxValue) {
		yAxisMaxValue = axisMaxValue;
	}
	/**
	 * ȡ���Ƿ���x������ʾ<set>��ָ����name
	 */
	public String getShownames() {
		return shownames;
	}
	/**
	 * �����Ƿ���x������ʾ<set>��ָ����name
	 * @param shownames boolean
	 * ��: 1/0
	 */
	public void setShownames(String shownames) {
		this.shownames = shownames;
	}
	/**
	 * ȡ���Ƿ�������ͼ�����ͼ����ʾ���ݵ�ֵ
	 */
	public String getShowValues() {
		return showValues;
	}
	/**
	 * �����Ƿ�������ͼ�����ͼ����ʾ���ݵ�ֵ
	 * @param showValues boolean
	 * ��: 1/0
	 */
	public void setShowValues(String showValues) {
		this.showValues = showValues;
	}
	/**
	 * ȡ���Ƿ���ͼ���y����������ʾ�����С������ֵ
	 */
	public String getShowLimits() {
		return showLimits;
	}
	/**
	 * �����Ƿ���ͼ���y����������ʾ�����С������ֵ
	 * @param showLimits boolean
	 * ��: 1/0
	 */
	public void setShowLimits(String showLimits) {
		this.showLimits = showLimits;
	}
	/**
	 * ȡ��Y������� ��ˮƽ��ʾ���Ǵ�ֱ��ʾ
	 */
	public String getRotateYAxisName() {
		return rotateYAxisName;
	}
	/**
	 * ����Y������� ��ˮƽ��ʾ���Ǵ�ֱ��ʾ
	 * @param rotateNames boolean
	 * ��: 1/0
	 */
	public void setRotateYAxisName(String rotateYAxisName) {
		this.rotateYAxisName = rotateYAxisName;
	}
	/**
	 * ȡ��x���µ�name ��ˮƽ��ʾ���Ǵ�ֱ��ʾ
	 */
	public String getRotateNames() {
		return rotateNames;
	}
	/**
	 * ����x���µ�name ��ˮƽ��ʾ���Ǵ�ֱ��ʾ
	 * @param rotateNames boolean
	 * ��: 1/0
	 */
	public void setRotateNames(String rotateNames) {
		this.rotateNames = rotateNames;
	}
	/**
	 * ȡ������ͼ����ʾ�Ƿ��Ƕ�����ʾ
	 */
	public String getAnimation() {
		return animation;
	}
	/**
	 * ��������ͼ����ʾ�Ƿ��Ƕ�����ʾ
	 * @param animation boolean
	 * ��: 1/0
	 */
	public void setAnimation(String animation) {
		this.animation = animation;
	}
	/**
	 * ȡ�ñ߿��С
	 */
	public String getShowBorder() {
		return showBorder;
	}
	/**
	 * ���ñ߿��С
	 * @param showBorder Numerical Value
	 * ��: 1~10
	 */
	public void setShowBorder(String showBorder) {
		this.showBorder = showBorder;
	}
	/**
	 * ȡ��������ʽ
	 */
	public String getBaseFont() {
		return baseFont;
	}
	/**
	 * ����������ʽ
	 * @param baseFont FontName
	 * ��: ����
	 */
	public void setBaseFont(String baseFont) {
		this.baseFont = baseFont;
	}
	/**
	 * ȡ�������С
	 */
	public String getBaseFontSize() {
		return baseFontSize;
	}
	/**
	 * ���������С
	 * @param baseFontSize FontSize
	 * ��: 14
	 */
	public void setBaseFontSize(String baseFontSize) {
		this.baseFontSize = baseFontSize;
	}
	/**
	 * ȡ��������ɫ
	 */
	public String getBaseFontColor() {
		return baseFontColor;
	}
	/**
	 * ����������ɫ
	 * @param baseFontColor HexColorCode
	 * ��: 000000
	 */
	public void setBaseFontColor(String baseFontColor) {
		this.baseFontColor = baseFontColor;
	}
	/**
	 * ȡ��ͼ������������ʽ
	 */
	public String getOutCnvBaseFont() {
		return outCnvBaseFont;
	}
	/**
	 * ����ͼ������������ʽ
	 * @param outCnvBaseFont FontName
	 * ��: ����
	 */
	public void setOutCnvBaseFont(String outCnvBaseFont) {
		this.outCnvBaseFont = outCnvBaseFont;
	}
	/**
	 *  ȡ��ͼ�����������С
	 */
	public String getOutCnvBaseFontSze() {
		return outCnvBaseFontSze;
	}
	/**
	 *  ����ͼ�����������С
	 * @param outCnvBaseFontSze FontSize
	 * ��: 14
	 */
	public void setOutCnvBaseFontSze(String outCnvBaseFontSze) {
		this.outCnvBaseFontSze = outCnvBaseFontSze;
	}
	/**
	 * ȡ��ͼ������������ɫ
	 */
	public String getOutCnvBaseFontColor() {
		return outCnvBaseFontColor;
	}
	/**
	 * ����ͼ������������ɫ
	 * @param outCnvBaseFontColor HexColorCode
	 * ��: 000000
	 */
	public void setOutCnvBaseFontColor(String outCnvBaseFontColor) {
		this.outCnvBaseFontColor = outCnvBaseFontColor;
	}
	/**
	 * ȡ������ֵ��ǰ׺
	 */
	public String getNumberPrefix() {
		return numberPrefix;
	}
	/**
	 * ��������ֵ��ǰ׺
	 * @param numberPrefix Sting 
	 * ��: ��
	 */
	public void setNumberPrefix(String numberPrefix) {
		this.numberPrefix = numberPrefix;
	}
	/**
	 * ȡ������ֵ�ĺ�׺������������ַ�����Ҫʹ��URL Encode�ر��룩
	 */
	public String getNumberSuffix() {
		return numberSuffix;
	}
	/**
	 * ��������ֵ�ĺ�׺������������ַ�����Ҫʹ��URL Encode�ر��룩
	 * @param numberSuffix String
	 * ��: %BD%F0%CA%AF%CD%FE%CA%D3 (��ʯ����)
	 */
	public void setNumberSuffix(String numberSuffix) {
		this.numberSuffix = numberSuffix;
	}
	/**
	 * ȡ���Ƿ��ʽ������
	 */
	public String getFormatNumber() {
		return formatNumber;
	}
	/**
	 * �����Ƿ��ʽ������
	 * @param formatNumber boolean
	 * ��: 1/0
	 */
	public void setFormatNumber(String formatNumber) {
		this.formatNumber = formatNumber;
	}
	/**
	 * ȡ���Ƿ��á�K��������ǧ����M�����������
	 */
	public String getFormatNumberScale() {
		return formatNumberScale;
	}
	/**
	 * �����Ƿ��á�K��������ǧ����M�����������
	 * @param formatNumberScale boolean
	 * ��: 1/0
	 */
	public void setFormatNumberScale(String formatNumberScale) {
		this.formatNumberScale = formatNumberScale;
	}
	/**
	 * ȡ����ָ�����ַ�������С����
	 */
	public String getDecimalSeparator() {
		return decimalSeparator;
	}
	/**
	 * ������ָ�����ַ�������С����
	 * @param decimalSeparator String
	 * ��: .
	 */
	public void setDecimalSeparator(String decimalSeparator) {
		this.decimalSeparator = decimalSeparator;
	}
	/**
	 * ��ָ�����ַ�������ǧλ�ָ���
	 */
	public String getThousandSeparator() {
		return thousandSeparator;
	}
	/**
	 * ������ָ�����ַ�������ǧλ�ָ���
	 * @param thousandSeparator String
	 * ��: ,
	 */
	public void setThousandSeparator(String thousandSeparator) {
		this.thousandSeparator = thousandSeparator;
	}
	/**
	 * ȡ����������������ֵ��С��λ��
	 */
	public String getDecimalPrecision() {
		return decimalPrecision;
	}
	/**
	 * ��������������ֵ��С��λ��
	 * @param decimalPrecision String
	 * ��: 2
	 */
	public void setDecimalPrecision(String decimalPrecision) {
		this.decimalPrecision = decimalPrecision;
	}
	/**
	 * ȡ��y����ֵ��С��λ��
	 */
	public String getDivLineDecimalPrecision() {
		return divLineDecimalPrecision;
	}
	/**
	 * ����y����ֵ��С��λ��
	 * @param divLineDecimalPrecision String
	 * ��: 0
	 */
	public void setDivLineDecimalPrecision(String divLineDecimalPrecision) {
		this.divLineDecimalPrecision = divLineDecimalPrecision;
	}
	/**
	 * ȡ��y��������Сֵ��С��λ��
	 */
	public String getLimitsDecimalPrecision() {
		return limitsDecimalPrecision;
	}
	/**
	 * ����y��������Сֵ��С��λ��
	 * @param limitsDecimalPrecision String
	 * ��: 0
	 */
	public void setLimitsDecimalPrecision(String limitsDecimalPrecision) {
		this.limitsDecimalPrecision = limitsDecimalPrecision;
	}
	/**
	 * ȡ��ˮƽ�ָ��ߵ�����
	 */
	public String getNumdivlines() {
		return numdivlines;
	}
	/**
	 * ����ˮƽ�ָ��ߵ�����
	 * @param numdivlines String 
	 * ��: 5
	 */
	public void setNumdivlines(String numdivlines) {
		this.numdivlines = numdivlines;
	}
	/**
	 * ȡ��ˮƽ�ָ��ߵ���ɫ
	 */
	public String getDivlinecolor() {
		return divlinecolor;
	}
	/**
	 * ����ˮƽ�ָ��ߵ���ɫ
	 * @param divlinecolor HexColorCode
	 * ��: ffffff
	 */
	public void setDivlinecolor(String divlinecolor) {
		this.divlinecolor = divlinecolor;
	}
	/**
	 * ȡ��ˮƽ�ָ��ߵĿ��
	 */
	public String getDivLineThickness() {
		return divLineThickness;
	}
	/**
	 * ����ˮƽ�ָ��ߵĿ��
	 * @param divLineThickness String
	 * ��: 5
	 */
	public void setDivLineThickness(String divLineThickness) {
		this.divLineThickness = divLineThickness;
	}
	/**
	 * ȡ��ˮƽ�ָ��ߵ�͸����
	 */
	public String getDivLineAlpha() {
		return divLineAlpha;
	}
	/**
	 * ����ˮƽ�ָ��ߵ�͸����
	 * @param divLineAlpha Numerical Value 
	 * ��: 0~100
	 */
	public void setDivLineAlpha(String divLineAlpha) {
		this.divLineAlpha = divLineAlpha;
	}
	/**
	 * ȡ���Ƿ���ʾˮƽ�ָ��ߵ���ֵ
	 */
	public String getShowDivLineValue() {
		return showDivLineValue;
	}
	/**
	 * �����Ƿ���ʾˮƽ�ָ��ߵ���ֵ
	 * @param showDivLineValue boolean
	 * ��: 1/0
	 */
	public void setShowDivLineValue(String showDivLineValue) {
		this.showDivLineValue = showDivLineValue;
	}
	/**
	 * ȡ����ʾ�Ƿ񼤻������ͣЧ��
	 */
	public String getShowhovercap() {
		return showhovercap;
	}
	/**
	 * ������ʾ�Ƿ񼤻������ͣЧ��
	 * @param showhovercap boolean
	 * ��: 1/0
	 */
	public void setShowhovercap(String showhovercap) {
		this.showhovercap = showhovercap;
	}
	/**
	 * ȡ�������ͣЧ���ı�����ɫ
	 */
	public String getHoverCapBgColor() {
		return hoverCapBgColor;
	}
	/**
	 * ���������ͣЧ���ı�����ɫ
	 * @param hoverCapBgColor HexColorCode
	 * ��: ffffff
	 */
	public void setHoverCapBgColor(String hoverCapBgColor) {
		this.hoverCapBgColor = hoverCapBgColor;
	}
	/**
	 * ȡ�������ͣЧ���ı߿���ɫ
	 */
	public String getHoverCapBorderColor() {
		return hoverCapBorderColor;
	}
	/**
	 * ���������ͣЧ���ı߿���ɫ
	 * @param hoverCapBorderColor HexColorCode
	 * ��: ffffff
	 */
	public void setHoverCapBorderColor(String hoverCapBorderColor) {
		this.hoverCapBorderColor = hoverCapBorderColor;
	}
	/**
	 * ȡ�������ͣ����ʾ���ı��еķָ�����
	 */
	public String getHoverCapSepChar() {
		return hoverCapSepChar;
	}
	/**
	 * ���������ͣ����ʾ���ı��еķָ�����
	 * @param hoverCapSepChar Char
	 * ��: ;
	 */
	public void setHoverCapSepChar(String hoverCapSepChar) {
		this.hoverCapSepChar = hoverCapSepChar;
	}
	/**
	 * ȡ��ͼ����߾�
	 */
	public String getChartLeftMargin() {
		return chartLeftMargin;
	}
	/**
	 * ����ͼ����߾�
	 * @param chartLeftMargin String
	 * ��: 1
	 */
	public void setChartLeftMargin(String chartLeftMargin) {
		this.chartLeftMargin = chartLeftMargin;
	}
	/**
	 * ȡ��ͼ���ұ߾�
	 */
	public String getChartRightMargin() {
		return chartRightMargin;
	}
	/**
	 * ����ͼ���ұ߾�
	 * @param chartRightMargin String
	 * ��: 1
	 */
	public void setChartRightMargin(String chartRightMargin) {
		this.chartRightMargin = chartRightMargin;
	}
	/**
	 * ȡ��ͼ���ϱ߾�
	 */
	public String getChartTopMargin() {
		return chartTopMargin;
	}
	/**
	 * ����ͼ���ϱ߾�
	 * @param chartTopMargin String
	 * ��: 1
	 */
	public void setChartTopMargin(String chartTopMargin) {
		this.chartTopMargin = chartTopMargin;
	}
	/**
	 * ȡ��ͼ���±߾�
	 */
	public String getChartBottomMargin() {
		return chartBottomMargin;
	}
	/**
	 * ����ͼ���±߾�
	 * @param chartBottomMargin String
	 * ��: 1
	 */
	public void setChartBottomMargin(String chartBottomMargin) {
		this.chartBottomMargin = chartBottomMargin;
	}
	/**
	 * ˮƽ�߿��Ƿ�һ��Ҫ��ʾ
	 */
	public String getZeroPlaneShowBorder() {
		return zeroPlaneShowBorder;
	}
	/**
	 * ��3Dͼ���� ˮƽ(0λ��)�߿��Ƿ�һ��Ҫ��ʾ
	 * @param zeroPlaneShowBorder boolean
	 * ��: 1/0
	 */
	public void setZeroPlaneShowBorder(String zeroPlaneShowBorder) {
		this.zeroPlaneShowBorder = zeroPlaneShowBorder;
	}
	/**
	 * ȡ��ˮƽ(0λ��)�߿���ɫ
	 * @return
	 */
	public String getZeroPlaneBorderColor() {
		return zeroPlaneBorderColor;
	}
	/**
	 * ����ˮƽ(0λ��)�߿���ɫ
	 * @param zeroPlaneBorderColor HexColorCode
	 * ��: ffffff
	 */
	public void setZeroPlaneBorderColor(String zeroPlaneBorderColor) {
		this.zeroPlaneBorderColor = zeroPlaneBorderColor;
	}
	/**
	 * ȡ��ˮƽ(0λ��)�������ɫ
	 */
	public String getZeroPlaneColor() {
		return zeroPlaneColor;
	}
	/**
	 * ����ˮƽ(0λ��)�������ɫ
	 * @param zeroPlaneColor HexColorCode
	 * ��: ffffff
	 */
	public void setZeroPlaneColor(String zeroPlaneColor) {
		this.zeroPlaneColor = zeroPlaneColor;
	}
	/**
	 * ȡ��ˮƽ(0λ��)�����͸����
	 */
	public String getZeroPlaneAlpha() {
		return zeroPlaneAlpha;
	}
	/**
	 * ����ˮƽ(0λ��)�����͸����
	 * @param zeroPlaneAlpha Numerical Value 
	 * ��: 0~100
	 */
	public void setZeroPlaneAlpha(String zeroPlaneAlpha) {
		this.zeroPlaneAlpha = zeroPlaneAlpha;
	}
}
