package com.viewscenes.util.report.charts;
/**
 * <p>Pie3D 3D��ͼ </p>
 * <p>��Ӧ FusionCharts for Flex > XML Reference > Pie 3D</p>
 * @author ��ʿ��
 * @version 1.0
 */
public class Pie3D {
	
	
	//�ڵ������ǩ
	public static String BuildChartEndXML = "\n</chart>";
	
	//Chart Objects  ͼ������Լ�����,��������
	public static String BACKGROUND   = "BACKGROUND";
	public static String CAPTION   = "CAPTION";
	public static String DATALABELS   = "DATALABELS";
	public static String DATAPLOT   = "DATAPLOT";
	public static String SUBCAPTION   = "SUBCAPTION";
	public static String TOOLTIP   = "TOOLTIP ";
	
	//<chart> element Attributes Ԫ������ -----------------------------------------
	//Functional Attributes  ��������
	private String animation  = null;			//�����Ƿ���ʾ����
	private String palette   = null;			//���õ�ɫ����1-5
	private String paletteColors  = null;		//���õ�ɫ�����ɫ,�Զ��ŷָ�
	private String showAboutMenuItem  = null;	//�����Ƿ���ʾ���ڲ˵�
	private String aboutMenuItemLabel  = null;	//���ù��ڲ˵��ı���
	private String aboutMenuItemLink  = null;	//���ù��ڲ˵�������
	private String showLabels  = null;			//�����Ƿ���ʾX���ǩ
	private String showValues   = null;			//�����Ƿ���ʾY���ֵ
	private String clickURL    = null;			//���õ������
	private String defaultAnimation    = null;	//����Ĭ�ϵĶ�������ʽ
	private String showZeroPies   = null;		//�����Ƿ���ʾ0ֵ
	private String showPercentValues = null;	//���ñ�ͼ��ǩ��ֵ�Ƿ��԰ٷֱ���ʽ��ʾ
	private String showPercentInToolTip = null;	//���ñ�ͼ��ʾ��ֵ�Ƿ��԰ٷֱ���ʽ��ʾ
	private String labelSepChar   = null;		//���ñ�ǩ������ֵ�ķָ���
	private String exportEnabled = null;			//�����Ƿ����õ�������
	private String exportShowMenuItem = null;		//�����Ƿ���ʾ����ͼƬ�Ҳ˵�
	private String exportFileName = null;			//���õ���ͼƬ������
	private String exportFormats = null;			//���õ���ͼƬ�ķָ���ʾ(���API��ʹ��)
	private String showExportDataMenuItem = null; 	//�����Ƿ���ʾͼ���XML�˵�
	//Chart Titles and Axis Names ͼ�����ͱ�����
	private String caption = null;				//����ͼ���������
	private String subCaption  = null;			//����ͼ��ĸ�����
	
	//Chart Cosmetics ͼ������
	private String bgColor  = null;				//���ñ���ɫ<�ɽ���>
	private String bgAlpha   = null;			//����͸����<�ɽ���>
	private String bgRatio   = null;			//���ñ����������<�ɽ���>
	private String bgAngle   = null;			//���ñ���ɫ���Ƕ�<�ɽ���>
	private String bgSWF   = null;				//���ñ���ͼƬ,SWF��ַ
	private String bgSWFAlpha   = null;			//���ñ���ͼƬ,SWF��͸����
	private String showBorder  = null;			//�����Ƿ���ʾ�߿�
	private String borderColor   = null;		//���ñ߿����ɫ
	private String borderThickness   = null;	//���ñ߿�ĺ��,���﷽��
	private String borderAlpha   = null;		///���ñ߿�͸����
	private String logoURL = null;				//����LOGO��·��
	private String logoPosition  = null;		//����LOGO�����λ��	
	private String logoAlpha  = null;			//����LOGO��͸����
	private String logoScale  = null;			//����LOGO�����ű���
	private String logoLink  = null;			//����LOGO������
	
	//Data Plot Cosmetics  ����ͼ������
	private String showPlotBorder = null;		//�����Ƿ���ʾͼ��ı߿�
	private String plotBorderColor   = null;	//����ͼ��߿����ɫ
	private String plotBorderThickness  = null;	//����ͼ��߿�ĺ��
	private String plotBorderAlpha  = null;		//����ͼ��߿��͸����
	private String plotFillAlpha = null;		//����ͼ�����ɫ��͸����
	private String use3DLighting    = null;		//�����Ƿ���ʾ����<3D>
	
	//Pie / Doughnut Properties ��ͼ����
	private String slicingDistance  = null;		//������Ƭ��ͼ��ľ���
	private String pieRadius  = null;			//���ñ�ͼ�İ뾶
	private String startingAngle  = null;		//������ת����ʼ����
	private String enableRotation  = null;		//�����Ƿ�Ĭ����ת?
	private String pieInnerFaceAlpha= null;		//���ñ�ͼ�ڲ��͸����<3D>
	private String pieOuterFaceAlpha = null;	//���ñ�ͼ����͸����<3D>
	private String pieYScale     = null;		//���ñ�ͼ�Ŀɼ��Ƕ�<3D>
	private String pieSliceDepth     = null;	//���ñ�ͼ�ĺ��<3D>

	//Smart Lines & Labels ��ͼ����������
	private String enableSmartLabels  = null;	//���ñ�ͼ�Ƿ���ʾ������	
	private String skipOverlapLabels  = null;	//���ñ�ͼ�Ƿ���ʾ������	
	private String isSmartLineSlanted  = null;	//���ñ�ͼ�Ƿ���ʾ������	
	private String smartLineColor  = null;	//���ñ�ͼ�Ƿ���ʾ������	
	private String smartLineThickness  = null;	//���ñ�ͼ�Ƿ���ʾ������	
	private String smartLineAlpha  = null;	//���ñ�ͼ�Ƿ���ʾ������	
	private String labelDistance  = null;	//���ñ�ͼ�Ƿ���ʾ������	
	private String smartLabelClearance  = null;	//���ñ�ͼ�Ƿ���ʾ������	
	
	//Number Formatting ���ָ�ʽ��
	private String formatNumber  = null;				//�����Ƿ����ָ�ʽ��				
	private String formatNumberScale  = null;			//�����Ƿ���"K"������ǧ��"M"���������			
	private String defaultNumberScale  = null;			//����Ĭ�ϵ����ָ�ʽ���ĺ�׺��λ			
	private String numberScaleUnit  = null;				//�������ָ�ʽ���ĺ�׺��λ,��'K,M,B' 		
	private String numberScaleValue  = null;			//�������ָ�ʽ������ʽ,��'1000,1000,1000		
	private String numberPrefix = null;					//��������ֵ��ǰ׺
	private String numberSuffix = null;					//��������ֵ�ĺ�׺������������ַ�����Ҫʹ��URL Encode�ر��룩
	private String decimalSeparator  = null;			//����ָ�����ַ�������С����			
	private String thousandSeparator  = null;			//����ָ�����ַ�������ǧλ�ָ���				
	private String inDecimalSeparator   = null;			//����С����ת���			
	private String inThousandSeparator   = null;		//����ǧ��λת���				
	private String decimals = null;						//���þ�ȷ��С�����λ
	private String forceDecimals   = null;				//����С����λ�������Ƿ���		

	
	//Font Properties ��������
	private String baseFont = null;						//����Value��ǩ������
	private String baseFontSize = null;					//����Value��ǩ�������С
	private String baseFontColor = null;				//����Value��ǩ��������ɫ
	
	
	//Tool-tip ������ʾ	---------------------------------------------
	private String showToolTip  = null;					//�����Ƿ���ʾ��ʾ
	private String toolTipBgColor   = null;				//������ʾ��������ɫ
	private String toolTipBorderColor   = null;			//������ʾ���������ɫ
	private String toolTipSepChar   = null;				//������ʾ�ָ���
	private String showToolTipShadow  = null;			//�����Ƿ���ʾ��ʾ���������Ӱ
	
	//Chart Padding & Margins ͼ��߾�
	private String captionPadding   = null;				//���ñ������ͼ��ı߾�
	private String chartLeftMargin    = null;			//����ͼ����뱳������߾�
	private String chartRightMargin    = null;			//����ͼ����뱳�����ұ߾�
	private String chartTopMargin    = null;			//����ͼ����뱳�����ϱ߾�
	private String chartBottomMargin    = null;			//����ͼ����뱳�����±߾�
	
	
	
	/**
	 * ����ͼ�����������XML
	 * @return ͼ�����������XML
	 */
	public String BuildChartXML(){
		StringBuffer chartXML = new StringBuffer();
		chartXML.append("<chart ");
		//Functional Attributes  �������� *********************************************************************
		
		//�����Ƿ���ʾ����
		if(this.getAnimation()!=null){
			chartXML.append(" animation = '" +this.animation + "' \n");
		}
		//���õ�ɫ����1-5
		if(this.getPalette()!=null){
			chartXML.append(" palette = '" +this.palette + "' \n");
		}
		//���õ�ɫ�����ɫ
		if(this.getPaletteColors()!=null){
			chartXML.append(" paletteColors = '" +this.paletteColors + "' \n");
		}
		//�����Ƿ���ʾ���ڲ˵�
		if(this.getShowAboutMenuItem()!=null){
			chartXML.append(" showAboutMenuItem = '" +this.showAboutMenuItem + "' \n");
		}
		//���ù��ڲ˵��ı���
		if(this.getAboutMenuItemLabel()!=null){
			chartXML.append(" aboutMenuItemLabel = '" +this.aboutMenuItemLabel + "' \n");
		}
		//���ù��ڲ˵�������
		if(this.getAboutMenuItemLink()!=null){
			chartXML.append(" aboutMenuItemLink = '" +this.aboutMenuItemLink + "' \n");
		}
		//�����Ƿ���ʾX���ǩ
		if(this.getShowLabels()!=null){
			chartXML.append(" showLabels = '" +this.showLabels + "' \n");
		}
		//�����Ƿ���ʾY���ֵ
		if(this.getShowValues()!=null){
			chartXML.append(" showValues = '" +this.showValues + "' \n");
		}
		//���õ������
		if(this.getClickURL()!=null){
			chartXML.append(" clickURL = '" +this.clickURL + "' \n");
		}
		//������ʾĬ�ϵĶ���
		if(this.getDefaultAnimation()!=null){
			chartXML.append(" defaultAnimation = '" +this.defaultAnimation + "' \n");
		}
		//�����Ƿ���ʾ0ֵ
		if(this.getDefaultAnimation()!=null){
			chartXML.append(" showZeroPies = '" +this.showZeroPies + "' \n");
		}
		//���ñ�ͼ��ǩ��ֵ�Ƿ��԰ٷֱ���ʽ��ʾ
		if(this.getDefaultAnimation()!=null){
			chartXML.append(" showPercentValues = '" +this.showPercentValues + "' \n");
		}
		//���ñ�ͼ��ʾ��ֵ�Ƿ��԰ٷֱ���ʽ��ʾ
		if(this.getDefaultAnimation()!=null){
			chartXML.append(" showPercentInToolTip = '" +this.showPercentInToolTip + "' \n");
		}
		//���ñ�ǩ������ֵ�ķָ���
		if(this.getDefaultAnimation()!=null){
			chartXML.append(" labelSepChar = '" +this.labelSepChar + "' \n");
		}
		//�����Ƿ����õ�������
		if(this.getExportEnabled()!=null){
			chartXML.append(" exportEnabled = '" +this.exportEnabled + "' \n");
		}
		//�����Ƿ���ʾ����ͼƬ�Ҳ˵�
		if(this.getExportShowMenuItem()!=null){
			chartXML.append(" exportShowMenuItem = '" +this.exportShowMenuItem + "' \n");
		}
		//���õ���ͼƬ������
		if(this.getExportFileName()!=null){
			chartXML.append(" exportFileName = '" +this.exportFileName + "' \n");
		}
		//���õ���ͼƬ�ķָ���ʾ(���API��ʹ��)
		if(this.getExportFormats()!=null){
			chartXML.append(" exportFormats = '" +this.exportFormats + "' \n");
		}
		//�����Ƿ���ʾͼ���XML�˵�
		if(this.getShowExportDataMenuItem()!=null){
			chartXML.append(" showExportDataMenuItem = '" +this.showExportDataMenuItem + "' \n");
		}
		//Chart Titles and Axis Names ͼ�����ͱ�����*********************************************************************
		
		//����ͼ���������
		if(this.getCaption()!=null){
			chartXML.append(" caption = '" +this.caption + "' \n");
		}
		//����ͼ��ĸ�����
		if(this.getSubCaption()!=null){
			chartXML.append(" subCaption = '" +this.subCaption + "' \n");
		}
		
		//Chart Cosmetics ͼ������*********************************************************************
		//���ñ���ɫ<�ɽ���>
		if(this.getBgColor()!=null){
			chartXML.append(" bgColor = '" +this.bgColor + "' \n");
		}
		//����͸����<�ɽ���>
		if(this.getBgAlpha()!=null){
			chartXML.append(" bgAlpha = '" +this.bgAlpha + "' \n");
		}
		//���ñ����������<�ɽ���>
		if(this.getBgRatio()!=null){
			chartXML.append(" bgRatio = '" +this.bgRatio + "' \n");
		}
		//���ñ���ɫ���Ƕ�<�ɽ���>
		if(this.getBgAngle()!=null){
			chartXML.append(" bgAngle = '" +this.bgAngle + "' \n");
		}
		//���ñ���ͼƬ,SWF��ַ
		if(this.getBgSWF()!=null){
			chartXML.append(" bgSWF = '" +this.bgSWF + "' \n");
		}
		//���ñ���ͼƬ,SWF��͸����
		if(this.getBgSWFAlpha()!=null){
			chartXML.append(" bgSWFAlpha = '" +this.bgSWFAlpha + "' \n");
		}
		//�����Ƿ���ʾ�߿�
		if(this.getShowBorder()!=null){
			chartXML.append(" showBorder = '" +this.showBorder + "' \n");
		}
		//���ñ߿����ɫ
		if(this.getBorderColor()!=null){
			chartXML.append(" borderColor = '" +this.borderColor + "' \n");
		}
		//���ñ߿�ĺ��,���﷽��
		if(this.getBorderThickness()!=null){
			chartXML.append(" borderThickness = '" +this.borderThickness + "' \n");
		}
		///���ñ߿�͸����
		if(this.getBorderAlpha()!=null){
			chartXML.append(" borderAlpha = '" +this.borderAlpha + "' \n");
		}
		//����LOGO��·��
		if(this.getLogoURL()!=null){
			chartXML.append(" logoURL = '" +this.logoURL + "' \n");
		}
		//����LOGO�����λ��	
		if(this.getLogoPosition()!=null){
			chartXML.append(" logoPosition = '" +this.logoPosition + "' \n");
		}
		//����LOGO��͸����
		if(this.getLogoAlpha()!=null){
			chartXML.append(" logoAlpha = '" +this.logoAlpha + "' \n");
		}
		//����LOGO�����ű���
		if(this.getLogoScale()!=null){
			chartXML.append(" logoScale = '" +this.logoScale + "' \n");
		}
		//����LOGO������
		if(this.getLogoLink()!=null){
			chartXML.append(" logoLink = '" +this.logoLink + "' \n");
		}
		//Data Plot Cosmetics  ����ͼ������*********************************************************************
		//�����Ƿ���ʾͼ��ı߿�
		if(this.getShowPlotBorder()!=null){
			chartXML.append(" showPlotBorder = '" +this.showPlotBorder + "' \n");
		}
		//����ͼ��߿����ɫ
		if(this.getPlotBorderColor()!=null){
			chartXML.append(" plotBorderColor = '" +this.plotBorderColor + "' \n");
		}
		//����ͼ��߿�ĺ��
		if(this.getPlotBorderThickness()!=null){
			chartXML.append(" plotBorderThickness = '" +this.plotBorderThickness + "' \n");
		}
		//����ͼ��߿��͸����
		if(this.getPlotBorderAlpha()!=null){
			chartXML.append(" plotBorderAlpha = '" +this.plotBorderAlpha + "' \n");
		}
		//����ͼ�����ɫ��͸����
		if(this.getPlotFillAlpha()!=null){
			chartXML.append(" plotFillAlpha = '" +this.plotFillAlpha + "' \n");
		}
		//�����Ƿ�ʹ���Ƚ��Ķ���Ч��
		if(this.getUse3DLighting()!=null){
			chartXML.append(" use3DLighting = '" +this.use3DLighting + "' \n");
		}
		
		//Pie / Doughnut Properties ��ͼ����-----------------------------------
		//������Ƭ��ͼ��ľ���
		if(this.getSlicingDistance()!=null){
			chartXML.append(" slicingDistance = '" +this.slicingDistance + "' \n");
		}
		//���ñ�ͼ�İ뾶
		if(this.getPieRadius()!=null){
			chartXML.append(" pieRadius = '" +this.pieRadius + "' \n");
		}
		//������ת����ʼ����
		if(this.getStartingAngle()!=null){
			chartXML.append(" startingAngle = '" +this.startingAngle + "' \n");
		}
		//�����Ƿ�Ĭ����ת
		if(this.getEnableRotation()!=null){
			chartXML.append(" enableRotation = '" +this.enableRotation + "' \n");
		}
		//���ñ�ͼ�ڲ��͸����<3D>
		if(this.getPieInnerFaceAlpha()!=null){
			chartXML.append(" pieInnerFaceAlpha = '" +this.pieInnerFaceAlpha + "' \n");
		}
		//���ñ�ͼ����͸����<3D>
		if(this.getPieOuterFaceAlpha()!=null){
			chartXML.append(" pieOuterFaceAlpha = '" +this.pieOuterFaceAlpha + "' \n");
		}
		//���ñ�ͼ�Ŀɼ��Ƕ�<3D>
		if(this.getPieYScale()!=null){
			chartXML.append(" pieYScale = '" +this.pieYScale + "' \n");
		}
		//���ñ�ͼ�ĺ��<3D>
		if(this.getPieSliceDepth()!=null){
			chartXML.append(" pieSliceDepth = '" +this.pieSliceDepth + "' \n");
		}
		
		//Smart Lines & Labels ��ͼ����������
		//��ͼ:���ñ�ͼ�Ƿ���ʾ������	
		if(this.getEnableSmartLabels()!=null){
			chartXML.append(" enableSmartLabels='"+this.getEnableSmartLabels()+"' \n");
		}
		//��ͼ:���ñ�ͼ�����ߵ���ɫ
		if(this.getSmartLineColor()!=null){
			chartXML.append(" smartLineColor='"+this.getSmartLineColor()+"' \n");
		}
		//��ͼ:���ñ�ͼ�����ߵĺ��
		if(this.getSmartLineThickness()!=null){
			chartXML.append(" smartLineThickness='"+this.getSmartLineThickness()+"' \n");
		}
		//��ͼ:���ñ�ͼ�����ߵ�͸����
		if(this.getSmartLineAlpha()!=null){
			chartXML.append(" smartLineAlpha='"+this.getSmartLineAlpha()+"' \n");
		}
		//��ͼ:���ñ�ͼ�������Ƿ���ֱ��
		if(this.getIsSmartLineSlanted()!=null){
			chartXML.append(" isSmartLineSlanted='"+this.getIsSmartLineSlanted()+"' \n");
		}
		//��ͼ:����δ֪		
		if(this.getLabelDistance()!=null){
			chartXML.append(" labelDistance='"+this.getLabelDistance()+"' \n");
		}
		//��ͼ:���ñ�ͼ�����ǩ�ľ���
		if(this.getSmartLabelClearance()!=null){
			chartXML.append(" smartLabelClearance='"+this.getSmartLabelClearance()+"' \n");
		}
		//��ͼ:���ñ�ͼ��ǩ̫���Ƿ�©�����ֱ�ǩ
		if(this.getSkipOverlapLabels()!=null){
			chartXML.append(" skipOverlapLabels='"+this.getSkipOverlapLabels()+"' \n");
		}
		
		//Number Formatting ���ָ�ʽ��******************************************************************
		//�����Ƿ����ָ�ʽ��	
		if(this.getFormatNumber()!=null){
			chartXML.append(" formatNumber = '" +this.formatNumber + "' \n");
		}
		//�����Ƿ���"K"������ǧ��"M"���������	
		if(this.getFormatNumberScale()!=null){
			chartXML.append(" formatNumberScale = '" +this.formatNumberScale + "' \n");
		}
		//����Ĭ�ϵ����ָ�ʽ���ĺ�׺��λ	
		if(this.getDefaultNumberScale()!=null){
			chartXML.append(" defaultNumberScale = '" +this.defaultNumberScale + "' \n");
		}
		//�������ָ�ʽ���ĺ�׺��λ,��'K,M,B'
		if(this.getNumberScaleUnit()!=null){
			chartXML.append(" numberScaleUnit = '" +this.numberScaleUnit + "' \n");
		}
		//�������ָ�ʽ������ʽ,��'1000,1000,1000	
		if(this.getNumberScaleValue()!=null){
			chartXML.append(" numberScaleValue = '" +this.numberScaleValue + "' \n");
		}
		//��������ֵ��ǰ׺
		if(this.getNumberPrefix()!=null){
			chartXML.append(" numberPrefix = '" +this.numberPrefix + "' \n");
		}
		//��������ֵ�ĺ�׺������������ַ�����Ҫʹ��URL Encode�ر��룩
		if(this.getNumberSuffix()!=null){
			chartXML.append(" numberSuffix = '" +this.numberSuffix + "' \n");
		}
		//����ָ�����ַ�������С����		
		if(this.getDecimalSeparator()!=null){
			chartXML.append(" decimalSeparator = '" +this.decimalSeparator + "' \n");
		}
		//����ָ�����ַ�������ǧλ�ָ���	
		if(this.getThousandSeparator()!=null){
			chartXML.append(" thousandSeparator = '" +this.thousandSeparator + "' \n");
		}
		//����С����ת���			
		if(this.getInDecimalSeparator()!=null){
			chartXML.append(" inDecimalSeparator = '" +this.inDecimalSeparator + "' \n");
		}
		//����ǧ��λת���				
		if(this.getInThousandSeparator()!=null){
			chartXML.append(" inThousandSeparator = '" +this.inThousandSeparator + "' \n");
		}
		//���þ�ȷ��С�����λ
		if(this.getDecimals()!=null){
			chartXML.append(" decimals = '" +this.decimals + "' \n");
		}
		//����С����λ�������Ƿ���	
		if(this.getForceDecimals()!=null){
			chartXML.append(" forceDecimals = '" +this.forceDecimals + "' \n");
		}
		
		//Font Properties ��������---------------------------------------------
		//����Value��ǩ������
		if(this.getBaseFont()!=null){
			chartXML.append(" baseFont = '" +this.baseFont + "' \n");
		}
		//����Value��ǩ�������С
		if(this.getBaseFontSize()!=null){
			chartXML.append(" baseFontSize = '" +this.baseFontSize + "' \n");
		}
		//����Value��ǩ��������ɫ
		if(this.getBaseFontColor()!=null){
			chartXML.append(" baseFontColor = '" +this.baseFontColor + "' \n");
		}
		
		//Tool-tip ������ʾ	---------------------------------------------
		//�����Ƿ���ʾ��ʾ
		if(this.getShowToolTip()!=null){
			chartXML.append(" showToolTip = '" +this.showToolTip + "' \n");
		}
		//������ʾ��������ɫ
		if(this.getToolTipBgColor()!=null){
			chartXML.append(" toolTipBgColor = '" +this.toolTipBgColor + "' \n");
		}
		//������ʾ���������ɫ
		if(this.getToolTipBorderColor()!=null){
			chartXML.append(" toolTipBorderColor = '" +this.toolTipBorderColor + "' \n");
		}
		//������ʾ�ָ���
		if(this.getToolTipSepChar()!=null){
			chartXML.append(" toolTipSepChar = '" +this.toolTipSepChar + "' \n");
		}
		//�����Ƿ���ʾ��ʾ���������Ӱ
		if(this.getShowToolTipShadow()!=null){
			chartXML.append(" showToolTipShadow = '" +this.showToolTipShadow + "' \n");
		}
		
		//Chart Padding & Margins ͼ��߾� ---------------------------------------------
		
		//���ñ������ͼ��ı߾�
		if(this.getCaptionPadding()!=null){
			chartXML.append(" captionPadding = '" +this.captionPadding + "' \n");
		}
		//����ͼ����뱳������߾�
		if(this.getChartLeftMargin()!=null){
			chartXML.append(" chartLeftMargin = '" +this.chartLeftMargin + "' \n");
		}
		//����ͼ����뱳�����ұ߾�
		if(this.getChartRightMargin()!=null){
			chartXML.append(" chartRightMargin = '" +this.chartRightMargin + "' \n");
		}
		//����ͼ����뱳�����ϱ߾�
		if(this.getChartTopMargin()!=null){
			chartXML.append(" chartTopMargin = '" +this.chartTopMargin + "' \n");
		}
		//����ͼ����뱳�����±߾�
		if(this.getChartBottomMargin()!=null){
			chartXML.append(" chartBottomMargin = '" +this.chartBottomMargin + "' \n");
		}
		chartXML.append(" > \n");
		return chartXML.toString();
	}
	
	
	//<chart> element Attributes Ԫ������ -----------------------------------------
	//Functional Attributes  ��������
	/**
	 * ��ȡ�Ƿ���ʾ����
	 * @return ȡֵ��Χ(1:true 0:false)
	 */
	public String getAnimation() {
		return animation;
	}
	/**
	 * �����Ƿ���ʾ����
	 * @param animation ȡֵ��Χ(1:true 0:false)
	 */
	public void setAnimation(String animation) {
		this.animation = animation;
	}
	
	/**
	 * ��ȡ��ɫ����1-5
	 * @return ȡֵ��Χ(1-5)
	 */
	public String getPalette() {
		return palette;
	}
	/**
	 * ���õ�ɫ����1-5
	 * @param palette ȡֵ��Χ(1-5)
	 */
	public void setPalette(String palette) {
		this.palette = palette;
	}
	/**
	 * ��ȡ��ɫ�����ɫ
	 * @return ȡֵ��Χ(16������ɫ,�Զ��ŷָ�)
	 */
	public String getPaletteColors() {
		return paletteColors;
	}
	/**
	 * ���õ�ɫ�����ɫ
	 * @param paletteColors ȡֵ��Χ(16������ɫ,�Զ��ŷָ�)
	 */
	public void setPaletteColors(String paletteColors) {
		this.paletteColors = paletteColors;
	}
	/**
	 * ��ȡ�Ƿ���ʾ���ڲ˵�
	 * @return ȡֵ��Χ(1:true 0:false)
	 */
	public String getShowAboutMenuItem() {
		return showAboutMenuItem;
	}
	/**
	 * �����Ƿ���ʾ���ڲ˵�
	 * @param showAboutMenuItem ȡֵ��Χ(1:true 0:false)
	 */
	public void setShowAboutMenuItem(String showAboutMenuItem) {
		this.showAboutMenuItem = showAboutMenuItem;
	}
	/**
	 * ��ȡ���ڲ˵��ı���
	 * @return 
	 */
	public String getAboutMenuItemLabel() {
		return aboutMenuItemLabel;
	}
	/**
	 * ���ù��ڲ˵��ı���
	 * @param aboutMenuItemLabel
	 */
	public void setAboutMenuItemLabel(String aboutMenuItemLabel) {
		this.aboutMenuItemLabel = aboutMenuItemLabel;
	}
	/**
	 * ��ȡ���ڲ˵�������
	 * @return ȡֵ��Χ(URL)
	 */
	public String getAboutMenuItemLink() {
		return aboutMenuItemLink;
	}
	/**
	 * ���ù��ڲ˵�������
	 * @param aboutMenuItemLink ȡֵ��Χ(URL)
	 */
	public void setAboutMenuItemLink(String aboutMenuItemLink) {
		this.aboutMenuItemLink = aboutMenuItemLink;
	}
	/**
	 * ��ȡ�Ƿ���ʾX���ǩ
	 * @return ȡֵ��Χ(1:true 0:false)
	 */
	public String getShowLabels() {
		return showLabels;
	}
	/**
	 * �����Ƿ���ʾX���ǩ
	 * @param showLabels ȡֵ��Χ(1:true 0:false)
	 */
	public void setShowLabels(String showLabels) {
		this.showLabels = showLabels;
	}
	/**
	 * ��ȡ�Ƿ���ʾY���ֵ
	 * @return	ȡֵ��Χ(1:true 0:false)
	 */
	public String getShowValues() {
		return showValues;
	}
	/**
	 * �����Ƿ���ʾY���ֵ
	 * @param showValues	ȡֵ��Χ(1:true 0:false)
	 */
	public void setShowValues(String showValues) {
		this.showValues = showValues;
	}
	/**
	 * ��ȡ�������
	 * @return
	 */
	public String getClickURL() {
		return clickURL;
	}
	/**
	 * ���õ������
	 * @param clickURL
	 */
	public void setClickURL(String clickURL) {
		this.clickURL = clickURL;
	}
	/**
	 * ��ȡĬ�ϵĶ�������ʽ
	 * @return	ȡֵ��Χ(0:�޶���,style)
	 */
	public String getDefaultAnimation() {
		return defaultAnimation;
	}
	/**
	 * ����Ĭ�ϵĶ�������ʽ
	 * @param defaultAnimation	ȡֵ��Χ(0:�޶���,style)
	 */
	public void setDefaultAnimation(String defaultAnimation) {
		this.defaultAnimation = defaultAnimation;
	}
	/**
	 * ��ȡ�Ƿ���ʾ0ֵ
	 * @return
	 */
	public String getShowZeroPies() {
		return showZeroPies;
	}
	/**
	 * �����Ƿ���ʾ0ֵ
	 * @param showZeroPies
	 */
	public void setShowZeroPies(String showZeroPies) {
		this.showZeroPies = showZeroPies;
	}
	/**
	 * ��ȡ��ͼ��ǩ��ֵ�Ƿ��԰ٷֱ���ʽ��ʾ
	 * @return
	 */
	public String getShowPercentValues() {
		return showPercentValues;
	}
	/**
	 * ���ñ�ͼ��ǩ��ֵ�Ƿ��԰ٷֱ���ʽ��ʾ
	 * @param showPercentValues
	 */
	public void setShowPercentValues(String showPercentValues) {
		this.showPercentValues = showPercentValues;
	}
	/**
	 * ��ȡ��ͼ��ʾ��ֵ�Ƿ��԰ٷֱ���ʽ��ʾ
	 * @return
	 */
	public String getShowPercentInToolTip() {
		return showPercentInToolTip;
	}
	/**
	 * ���ñ�ͼ��ʾ��ֵ�Ƿ��԰ٷֱ���ʽ��ʾ
	 * @param showPercentInToolTip
	 */
	public void setShowPercentInToolTip(String showPercentInToolTip) {
		this.showPercentInToolTip = showPercentInToolTip;
	}
	/**
	 * ��ȡ��ǩ������ֵ�ķָ���
	 * @return
	 */
	public String getLabelSepChar() {
		return labelSepChar;
	}
	/**
	 * ���ñ�ǩ������ֵ�ķָ���
	 * @param labelSepChar
	 */
	public void setLabelSepChar(String labelSepChar) {
		this.labelSepChar = labelSepChar;
	}
	/**
	 * ��ȡ�Ƿ����õ�������
	 * @return
	 */
	public String getExportEnabled() {
		return exportEnabled;
	}
	/**
	 * �����Ƿ����õ�������
	 * @param exportEnabled
	 */
	public void setExportEnabled(String exportEnabled) {
		this.exportEnabled = exportEnabled;
	}
	/**
	 * ��ȡ�Ƿ���ʾ����ͼƬ�Ҳ˵�
	 * @return
	 */
	public String getExportShowMenuItem() {
		return exportShowMenuItem;
	}
	/**
	 * �����Ƿ���ʾ����ͼƬ�Ҳ˵�
	 * @param exportShowMenuItem
	 */
	public void setExportShowMenuItem(String exportShowMenuItem) {
		this.exportShowMenuItem = exportShowMenuItem;
	}
	/**
	 * ��ȡ����ͼƬ������
	 * @return
	 */
	public String getExportFileName() {
		return exportFileName;
	}
	/**
	 * ���õ���ͼƬ������
	 * @param exportFileName
	 */
	public void setExportFileName(String exportFileName) {
		this.exportFileName = exportFileName;
	}
	
	/**
	 * ��ȡ����ͼƬ�ķָ���ʾ(���API��ʹ��)
	 * @return
	 */
	public String getExportFormats() {
		return exportFormats;
	}
	/**
	 * ���õ���ͼƬ�ķָ���ʾ(���API��ʹ��)
	 * @param exportFormats
	 */
	public void setExportFormats(String exportFormats) {
		this.exportFormats = exportFormats;
	}
	/**
	 * ��ȡ�Ƿ���ʾͼ���XML�˵�
	 * @return
	 */
	public String getShowExportDataMenuItem() {
		return showExportDataMenuItem;
	}
	/**
	 * �����Ƿ���ʾͼ���XML�˵�
	 * @param showExportDataMenuItem
	 */
	public void setShowExportDataMenuItem(String showExportDataMenuItem) {
		this.showExportDataMenuItem = showExportDataMenuItem;
	}
	//Chart Titles and Axis Names ͼ�����ͱ����� *******************************************
	/**
	 * ��ȡͼ���������
	 * @return
	 */
	public String getCaption() {
		return caption;
	}
	/**
	 * ����ͼ���������
	 * @param caption
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
	/**
	 * ��ȡͼ��ĸ�����
	 * @return
	 */
	public String getSubCaption() {
		return subCaption;
	}
	/**
	 * ����ͼ��ĸ�����
	 * @param subCaption
	 */
	public void setSubCaption(String subCaption) {
		this.subCaption = subCaption;
	}
	
	//Chart Cosmetics ͼ������----------------------------
	/**
	 * ��ȡͼ��ı���ɫ<�ɽ���>
	 * @return Hex Color Code16
	 */
	public String getBgColor() {
		return bgColor;
	}
	/**
	 * ����ͼ��ı���ɫ<�ɽ���>
	 * @param bgColor Hex Color Code16
	 */
	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}
	/**
	 * ��ȡFCͼ��ı���͸����<�ɽ���>
	 * @return
	 */
	public String getBgAlpha() {
		return bgAlpha;
	}
	/**
	 * ����ͼ����͸����<�ɽ���>
	 * @param bgAlpha ȡֵ��Χ(1-100)
	 */
	public void setBgAlpha(String bgAlpha) {
		this.bgAlpha = bgAlpha;
	}
	/**
	 * ��ȡͼ�����������<�ɽ���>
	 * @return
	 */
	public String getBgRatio() {
		return bgRatio;
	}
	/**
	 * ����ͼ�����������<�ɽ���>
	 * ������ֵ֮��Ϊ100,����40,��������ͼ������40%�������
	 * @param bgRatio ��('40','60')
	 */
	public void setBgRatio(String bgRatio) {
		this.bgRatio = bgRatio;
	}
	/**
	 * ��ȡͼ����ɫ���Ƕ�<�ɽ���>
	 * @return
	 */
	public String getBgAngle() {
		return bgAngle;
	}
	/**
	 * ����ͼ����ɫ���Ƕ�<�ɽ���>
	 * @param bgAngle ȡֵ��Χ(1-360)
	 */
	public void setBgAngle(String bgAngle) {
		this.bgAngle = bgAngle;
	}
	/**
	 * ��ȡͼ����ͼƬ,SWF��ַ
	 * @return
	 */
	public String getBgSWF() {
		return bgSWF;
	}
	/**
	 * ����ͼ����ͼƬ,SWF��ַ
	 * @param bgSWF ,�����Ǿ���·���������·��
	 */
	public void setBgSWF(String bgSWF) {
		this.bgSWF = bgSWF;
	}
	/**
	 * ��ȡͼ����ͼƬ,SWF��ַ͸����
	 * @return
	 */
	public String getBgSWFAlpha() {
		return bgSWFAlpha;
	}
	/**
	 * ����ͼ����ͼƬ,SWF��ַ͸����
	 * @param bgSWFAlpha,ȡֵ��Χ(1-100)
	 */
	public void setBgSWFAlpha(String bgSWFAlpha) {
		this.bgSWFAlpha = bgSWFAlpha;
	}
	/**
	 * ��ȡ�Ƿ���ʾͼ��߿�
	 * @return
	 */
	public String getShowBorder() {
		return showBorder;
	}
	/**
	 * �����Ƿ���ʾͼ��߿�
	 * @param showBorder,ȡֵ(0,1)
	 */
	public void setShowBorder(String showBorder) {
		this.showBorder = showBorder;
	}
	/**
	 * ��ȡͼ��߿����ɫ
	 * @return Hex Color Code16
	 */
	public String getBorderColor() {
		return borderColor;
	}
	/**
	 * ����ͼ��߿����ɫ
	 * @param borderColor Hex Color Code16
	 */
	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}
	/**
	 * ��ȡͼ��߿�ĺ��
	 * @return
	 */
	public String getBorderThickness() {
		return borderThickness;
	}
	/**
	 * ����ͼ��߿�ĺ��
	 * @param borderThickness
	 */
	public void setBorderThickness(String borderThickness) {
		this.borderThickness = borderThickness;
	}
	/**
	 * ��ȡͼ��߿��͸����
	 * @return
	 */
	public String getBorderAlpha() {
		return borderAlpha;
	}
	/**
	 * ����ͼ��߿��͸����
	 * @param borderAlpha
	 */
	public void setBorderAlpha(String borderAlpha) {
		this.borderAlpha = borderAlpha;
	}
	/**
	 * ��ȡLOGO��·��
	 * @return ȡֵ��Χ(���·�����߾���·��)
	 */
	public String getLogoURL() {
		return logoURL;
	}
	/**
	 * ����LOGO��·��
	 * @param logoURL ȡֵ��Χ(���·�����߾���·��)
	 */
	public void setLogoURL(String logoURL) {
		this.logoURL = logoURL;
	}
	/**
	 * ��ȡLOGO�����λ��
	 * ȡֵ��Χ:
	 * TL - Top-left 
	 * TR - Top-right 
	 * BR - Bottom right 
	 * BL - Bottom left 
	 * CC - Center of screen 
	 * @return 
	 */
	public String getLogoPosition() {
		return logoPosition;
	}
	/**
	 * ����LOGO�����λ��
	 * ȡֵ��Χ:
	 * TL - Top-left 
	 * TR - Top-right 
	 * BR - Bottom right 
	 * BL - Bottom left 
	 * CC - Center of screen 
	 * @param logoPosition
	 */
	public void setLogoPosition(String logoPosition) {
		this.logoPosition = logoPosition;
	}
	/**
	 * ��ȡLOGO��͸����
	 * @return	ȡֵ��Χ(1~100)
	 */
	public String getLogoAlpha() {
		return logoAlpha;
	}
	/**
	 * ����LOGO��͸����
	 * @param logoAlpha ȡֵ��Χ(1~100)
	 */
	public void setLogoAlpha(String logoAlpha) {
		this.logoAlpha = logoAlpha;
	}
	/**
	 * ��ȡLOGO�����ű���
	 * @return ȡֵ��Χ(1~?)
	 */
	public String getLogoScale() {
		return logoScale;
	}
	/**
	 * ����LOGO�����ű���
	 * @param logoScale	ȡֵ��Χ(1~?)
	 */
	public void setLogoScale(String logoScale) {
		this.logoScale = logoScale;
	}
	/**
	 * ��ȡLOGO������
	 * @return	ȡֵ��Χ(http://??)
	 */
	public String getLogoLink() {
		return logoLink;
	}
	/**
	 * ����LOGO������
	 * @param logoLink ȡֵ��Χ(http://??)
	 */
	public void setLogoLink(String logoLink) {
		this.logoLink = logoLink;
	}
	
	//Data Plot Cosmetics  ����ͼ������ ********************************
	/**
	 * ��ȡ�Ƿ���ʾͼ��ı߿�
	 * @return
	 */
	public String getShowPlotBorder() {
		return showPlotBorder;
	}
	/**
	 * �����Ƿ���ʾͼ��ı߿�,ȡֵ��Χ(0,1)
	 * @param showPlotBorder
	 */
	public void setShowPlotBorder(String showPlotBorder) {
		this.showPlotBorder = showPlotBorder;
	}
	/**
	 * ��ȡͼ��߿����ɫ
	 * @return
	 */
	public String getPlotBorderColor() {
		return plotBorderColor;
	}
	/**
	 * ����ͼ��߿����ɫ
	 * @param plotBorderColor
	 */
	public void setPlotBorderColor(String plotBorderColor) {
		this.plotBorderColor = plotBorderColor;
	}
	/**
	 * ��ȡͼ��߿�ĺ��
	 * @return
	 */
	public String getPlotBorderThickness() {
		return plotBorderThickness;
	}
	/**
	 * ����ͼ��߿�ĺ��
	 * @param plotBorderThickness
	 */
	public void setPlotBorderThickness(String plotBorderThickness) {
		this.plotBorderThickness = plotBorderThickness;
	}
	/**
	 * ��ȡͼ��߿��͸����
	 * @return
	 */
	public String getPlotBorderAlpha() {
		return plotBorderAlpha;
	}
	/**
	 * ����ͼ��߿��͸����
	 * @param plotBorderAlpha
	 */
	public void setPlotBorderAlpha(String plotBorderAlpha) {
		this.plotBorderAlpha = plotBorderAlpha;
	}
	/**
	 * ��ȡͼ�����ɫ��͸����
	 * @return
	 */
	public String getPlotFillAlpha() {
		return plotFillAlpha;
	}
	/**
	 * ����ͼ�����ɫ��͸����,ȡֵ��Χ(0~100)
	 * @param plotFillAlpha ,ȡֵ��Χ(0~100)
	 */
	public void setPlotFillAlpha(String plotFillAlpha) {
		this.plotFillAlpha = plotFillAlpha;
	}
	/**
	 * ��ȡ�Ƿ�ʹ���Ƚ��Ķ���Ч��<3D>
	 * @return
	 */
	public String getUse3DLighting() {
		return use3DLighting;
	}
	/**
	 * �����Ƿ�ʹ���Ƚ��Ķ���Ч��<3D>
	 * @param use3DLighting
	 */
	public void setUse3DLighting(String use3DLighting) {
		this.use3DLighting = use3DLighting;
	}
	//Pie / Doughnut Properties ��ͼ����-----------------------------------
	/**
	 * ��ȡ��Ƭ��ͼ��ľ���
	 * @return
	 */
	public String getSlicingDistance() {
		return slicingDistance;
	}
	/**
	 * ������Ƭ��ͼ��ľ���
	 * @param slicingDistance
	 */
	public void setSlicingDistance(String slicingDistance) {
		this.slicingDistance = slicingDistance;
	}
	/**
	 * ��ȡ��ͼ�İ뾶
	 * @return
	 */
	public String getPieRadius() {
		return pieRadius;
	}
	/**
	 * ���ñ�ͼ�İ뾶
	 * @param pieRadius
	 */
	public void setPieRadius(String pieRadius) {
		this.pieRadius = pieRadius;
	}
	/**
	 * ��ȡ��ת����ʼ����
	 * @return
	 */
	public String getStartingAngle() {
		return startingAngle;
	}
	/**
	 * ������ת����ʼ����
	 * @param startingAngle
	 */
	public void setStartingAngle(String startingAngle) {
		this.startingAngle = startingAngle;
	}
	/**
	 * ��ȡ�Ƿ�Ĭ����ת
	 * @return
	 */
	public String getEnableRotation() {
		return enableRotation;
	}
	/**
	 * �����Ƿ�Ĭ����ת
	 * @param enableRotation
	 */
	public void setEnableRotation(String enableRotation) {
		this.enableRotation = enableRotation;
	}
	/**
	 * ��ȡ��ͼ�ڲ��͸����<3D>
	 * @return
	 */
	public String getPieInnerFaceAlpha() {
		return pieInnerFaceAlpha;
	}
	/**
	 * ���ñ�ͼ�ڲ��͸����<3D>
	 * @param pieInnerFaceAlpha
	 */
	public void setPieInnerFaceAlpha(String pieInnerFaceAlpha) {
		this.pieInnerFaceAlpha = pieInnerFaceAlpha;
	}
	/**
	 * ��ȡ��ͼ����͸����<3D>
	 * @return
	 */
	public String getPieOuterFaceAlpha() {
		return pieOuterFaceAlpha;
	}
	/**
	 * ���ñ�ͼ����͸����<3D>
	 * @param pieOuterFaceAlpha
	 */
	public void setPieOuterFaceAlpha(String pieOuterFaceAlpha) {
		this.pieOuterFaceAlpha = pieOuterFaceAlpha;
	}
	/**
	 * ��ȡ��ͼ�Ŀɼ��Ƕ�<3D>
	 * @return
	 */
	public String getPieYScale() {
		return pieYScale;
	}
	/**
	 * ���ñ�ͼ�Ŀɼ��Ƕ�<3D>
	 * @param pieYScale
	 */
	public void setPieYScale(String pieYScale) {
		this.pieYScale = pieYScale;
	}
	/**
	 * ��ȡ��ͼ�ĺ��<3D>
	 * @return
	 */
	public String getPieSliceDepth() {
		return pieSliceDepth;
	}
	/**
	 * ���ñ�ͼ�ĺ��<3D>
	 * @param pieSliceDepth
	 */
	public void setPieSliceDepth(String pieSliceDepth) {
		this.pieSliceDepth = pieSliceDepth;
	}
	//------------------------------------ ��ͼ ---------------------------------------
	/**
	 * ��ͼ:��ȡ��ͼ�Ƿ���ʾ������
	 * @return
	 */
	public String getEnableSmartLabels() {
		return enableSmartLabels;
	}
	/**
	 * ��ͼ:���ñ�ͼ�Ƿ���ʾ������
	 * @param enableSmartLabels
	 */
	public void setEnableSmartLabels(String enableSmartLabels) {
		this.enableSmartLabels = enableSmartLabels;
	}
	/**
	 * ��ͼ:��ȡ��ͼ�����ߵ���ɫ
	 * @return Hex Color Code16
	 */
	public String getSmartLineColor() {
		return smartLineColor;
	}
	/**
	 * ��ͼ:���ñ�ͼ�����ߵ���ɫ
	 * @param smartLineColor Hex Color Code16
	 */
	public void setSmartLineColor(String smartLineColor) {
		this.smartLineColor = smartLineColor;
	}
	/**
	 * ��ͼ:��ȡ��ͼ�����ߵĺ��
	 * @return
	 */
	public String getSmartLineThickness() {
		return smartLineThickness;
	}
	/**
	 * ��ͼ:���ñ�ͼ�����ߵĺ��
	 * @param smartLineThickness
	 */
	public void setSmartLineThickness(String smartLineThickness) {
		this.smartLineThickness = smartLineThickness;
	}
	
	/**
	 * ��ͼ:��ȡ��ͼ�����ߵ�͸����
	 * @return
	 */
	public String getSmartLineAlpha() {
		return smartLineAlpha;
	}
	/**
	 * ��ͼ:���ñ�ͼ�����ߵ�͸����
	 * @param smartLineAlpha
	 */
	public void setSmartLineAlpha(String smartLineAlpha) {
		this.smartLineAlpha = smartLineAlpha;
	}
	/**
	 * ��ͼ:��ȡ��ͼ�������Ƿ���ֱ��	
	 * @return
	 */
	public String getIsSmartLineSlanted() {
		return isSmartLineSlanted;
	}
	/**
	 * ��ͼ:���ñ�ͼ�������Ƿ���ֱ��	
	 * @param isSmartLineSlanted ȡֵ:(1:��ͨ,0:ֱ��)
	 */
	public void setIsSmartLineSlanted(String isSmartLineSlanted) {
		this.isSmartLineSlanted = isSmartLineSlanted;
	}	
	/**
	 * ��ͼ:����δ֪,��������ʹ��
	 * @return
	 */
	public String getLabelDistance() {
		return labelDistance;
	}
	/**
	 * ��ͼ:����δ֪,��������ʹ��
	 * @param labelDistance
	 */
	public void setLabelDistance(String labelDistance) {
		this.labelDistance = labelDistance;
	}
	/**
	 * ��ͼ:��ȡ��ͼ�����ǩ�ľ���
	 * @return
	 */
	public String getSmartLabelClearance() {
		return smartLabelClearance;
	}
	/**
	 * ��ͼ:���ñ�ͼ�����ǩ�ľ���
	 * @param smartLabelClearance
	 */
	public void setSmartLabelClearance(String smartLabelClearance) {
		this.smartLabelClearance = smartLabelClearance;
	}
	/**
	 * ��ͼ:��ȡ��ͼ��ǩ̫���Ƿ�©�����ֱ�ǩ
	 * @return
	 */
	public String getSkipOverlapLabels() {
		return skipOverlapLabels;
	}
	/**
	 * ��ͼ:���ñ�ͼ��ǩ̫���Ƿ�©�����ֱ�ǩ
	 * @param skipOverlapLabels
	 */
	public void setSkipOverlapLabels(String skipOverlapLabels) {
		this.skipOverlapLabels = skipOverlapLabels;
	}
	//Number Formatting ���ָ�ʽ��
	/**
	 * ��ȡ�Ƿ����ָ�ʽ��
	 * @return
	 */
	public String getFormatNumber() {
		return formatNumber;
	}
	/**
	 * �����Ƿ����ָ�ʽ��
	 * @param formatNumber
	 */
	public void setFormatNumber(String formatNumber) {
		this.formatNumber = formatNumber;
	}
	/**
	 * ��ȡ�Ƿ���"K"������ǧ��"M"���������
	 * @return
	 */
	public String getFormatNumberScale() {
		return formatNumberScale;
	}
	/**
	 * �����Ƿ���"K"������ǧ��"M"���������
	 * @param formatNumberScale
	 */
	public void setFormatNumberScale(String formatNumberScale) {
		this.formatNumberScale = formatNumberScale;
	}
	/**
	 * ��ȡĬ�ϵ����ָ�ʽ���ĺ�׺��λ	
	 * @return
	 */
	public String getDefaultNumberScale() {
		return defaultNumberScale;
	}
	/**
	 * ����Ĭ�ϵ����ָ�ʽ���ĺ�׺��λ	
	 * @param defaultNumberScale
	 */
	public void setDefaultNumberScale(String defaultNumberScale) {
		this.defaultNumberScale = defaultNumberScale;
	}
	/**
	 * ��ȡ���ָ�ʽ���ĺ�׺��λ,��'K,M,B' 	
	 * @return
	 */
	public String getNumberScaleUnit() {
		return numberScaleUnit;
	}
	/**
	 * �������ָ�ʽ���ĺ�׺��λ,��'K,M,B' 	
	 * @param numberScaleUnit
	 */
	public void setNumberScaleUnit(String numberScaleUnit) {
		this.numberScaleUnit = numberScaleUnit;
	}
	/**
	 * ��ȡ���ָ�ʽ������ʽ,��'1000,1000,1000		
	 * @return
	 */
	public String getNumberScaleValue() {
		return numberScaleValue;
	}
	/**
	 * �������ָ�ʽ������ʽ,��'1000,1000,1000		
	 * @param numberScaleValue
	 */
	public void setNumberScaleValue(String numberScaleValue) {
		this.numberScaleValue = numberScaleValue;
	}
	/**
	 * ��ȡ����ֵ��ǰ׺
	 * @return
	 */
	public String getNumberPrefix() {
		return numberPrefix;
	}
	/**
	 * ��������ֵ��ǰ׺
	 * @param numberPrefix
	 */
	public void setNumberPrefix(String numberPrefix) {
		this.numberPrefix = numberPrefix;
	}
	/**
	 * ��ȡ����ֵ�ĺ�׺������������ַ�����Ҫʹ��URL Encode�ر��룩
	 * @return
	 */
	public String getNumberSuffix() {
		return numberSuffix;
	}
	/**
	 * ��������ֵ�ĺ�׺������������ַ�����Ҫʹ��URL Encode�ر��룩
	 * @param numberSuffix
	 */
	public void setNumberSuffix(String numberSuffix) {
		this.numberSuffix = numberSuffix;
	}
	/**
	 * ��ȡָ�����ַ�������С����	
	 * @return
	 */
	public String getDecimalSeparator() {
		return decimalSeparator;
	}
	/**
	 * ����ָ�����ַ�������С����	
	 * @param decimalSeparator
	 */
	public void setDecimalSeparator(String decimalSeparator) {
		this.decimalSeparator = decimalSeparator;
	}
	/**
	 * ��ȡָ�����ַ�������ǧλ�ָ���		
	 * @return
	 */
	public String getThousandSeparator() {
		return thousandSeparator;
	}
	/**
	 * ����ָ�����ַ�������ǧλ�ָ���		
	 * @param thousandSeparator
	 */
	public void setThousandSeparator(String thousandSeparator) {
		this.thousandSeparator = thousandSeparator;
	}
	/**
	 * ��ȡС����ת���		
	 * @return
	 */
	public String getInDecimalSeparator() {
		return inDecimalSeparator;
	}
	/**
	 * ����С����ת���		
	 * @param inDecimalSeparator
	 */
	public void setInDecimalSeparator(String inDecimalSeparator) {
		this.inDecimalSeparator = inDecimalSeparator;
	}
	/**
	 * ��ȡǧ��λת���		
	 * @return
	 */
	public String getInThousandSeparator() {
		return inThousandSeparator;
	}
	/**
	 * ����ǧ��λת���		
	 * @param inThousandSeparator
	 */
	public void setInThousandSeparator(String inThousandSeparator) {
		this.inThousandSeparator = inThousandSeparator;
	}
	/**
	 * ��ȡ��ȷ��С�����λ
	 * @return
	 */
	public String getDecimals() {
		return decimals;
	}
	/**
	 * ���þ�ȷ��С�����λ
	 * @param decimals
	 */
	public void setDecimals(String decimals) {
		this.decimals = decimals;
	}
	/**
	 * ��ȡС����λ�������Ƿ���	
	 * @return
	 */
	public String getForceDecimals() {
		return forceDecimals;
	}
	/**
	 * ����С����λ�������Ƿ���	
	 * @param forceDecimals
	 */
	public void setForceDecimals(String forceDecimals) {
		this.forceDecimals = forceDecimals;
	}
	
	//Font Properties ��������---------------------------------------------
	/**
	 * ��ȡ����Value��ǩ������
	 * @return
	 */
	public String getBaseFont() {
		return baseFont;
	}
	/**
	 * ��������Value��ǩ������
	 * @param baseFont
	 */
	public void setBaseFont(String baseFont) {
		this.baseFont = baseFont;
	}
	/**
	 * ��ȡValue��ǩ�������С
	 * @return
	 */
	public String getBaseFontSize() {
		return baseFontSize;
	}
	/**
	 * ����Value��ǩ�������С
	 * @param baseFontSize
	 */
	public void setBaseFontSize(String baseFontSize) {
		this.baseFontSize = baseFontSize;
	}
	/**
	 * ��ȡValue��ǩ��������ɫ
	 * @return
	 */
	public String getBaseFontColor() {
		return baseFontColor;
	}
	/**
	 * ����Value��ǩ��������ɫ
	 * @param baseFontColor
	 */
	public void setBaseFontColor(String baseFontColor) {
		this.baseFontColor = baseFontColor;
	}
	

	//Tool-tip ������ʾ	---------------------------------------------
	/**
	 * ��ȡ�Ƿ���ʾ��ʾ
	 * @return
	 */
	public String getShowToolTip() {
		return showToolTip;
	}
	/**
	 * �����Ƿ���ʾ��ʾ
	 * @param showToolTip
	 */
	public void setShowToolTip(String showToolTip) {
		this.showToolTip = showToolTip;
	}
	
	/**
	 * ��ȡ�Ƿ���ʾ��ʾ���������Ӱ
	 * @return
	 */
	public String getShowToolTipShadow() {
		return showToolTipShadow;
	}
	/**
	 * �����Ƿ���ʾ��ʾ���������Ӱ
	 * @param showToolTipShadow
	 */
	public void setShowToolTipShadow(String showToolTipShadow) {
		this.showToolTipShadow = showToolTipShadow;
	}
	/**
	 * ��ȡ��ʾ���������ɫ
	 * @return Hex Color Code16
	 */
	public String getToolTipBorderColor() {
		return toolTipBorderColor;
	}
	/**
	 * ������ʾ���������ɫ
	 * @param toolTipBorderColor Hex Color Code16
	 */
	public void setToolTipBorderColor(String toolTipBorderColor) {
		this.toolTipBorderColor = toolTipBorderColor;
	}
	/**
	 * ��ȡ��ʾ��������ɫ
	 * @return Hex Color Code16
	 */
	public String getToolTipBgColor() {
		return toolTipBgColor;
	}
	/**
	 * ������ʾ��������ɫ
	 * @param toolTipBgColor Hex Color Code16
	 */
	public void setToolTipBgColor(String toolTipBgColor) {
		this.toolTipBgColor = toolTipBgColor;
	}
	/**
	 * ��ȡ��ʾ�ָ���
	 * @return
	 */
	public String getToolTipSepChar() {
		return toolTipSepChar;
	}
	/**
	 * ������ʾ�ָ���
	 * @param toolTipSepChar
	 */
	public void setToolTipSepChar(String toolTipSepChar) {
		this.toolTipSepChar = toolTipSepChar;
	}
	
	//Chart Padding & Margins ͼ��߾� -------------------------------------
	/**
	 * ��ȡͼ����뱳������߾�
	 * @return
	 */
	public String getChartLeftMargin() {
		return chartLeftMargin;
	}
	/**
	 * ����ͼ����뱳������߾�
	 * @param chartLeftMargin
	 */
	public void setChartLeftMargin(String chartLeftMargin) {
		this.chartLeftMargin = chartLeftMargin;
	}
	/**
	 * ��ȡͼ����뱳�����ұ߾�
	 * @return
	 */
	public String getChartRightMargin() {
		return chartRightMargin;
	}
	/**
	 * ����ͼ����뱳�����ұ߾�
	 * @param chartRightMargin
	 */
	public void setChartRightMargin(String chartRightMargin) {
		this.chartRightMargin = chartRightMargin;
	}
	/**
	 * ��ȡͼ����뱳�����ϱ߾�
	 * @return
	 */
	public String getChartTopMargin() {
		return chartTopMargin;
	}
	/**
	 * ����ͼ����뱳�����ϱ߾�
	 * @param chartTopMargin
	 */
	public void setChartTopMargin(String chartTopMargin) {
		this.chartTopMargin = chartTopMargin;
	}
	/**
	 * ��ȡͼ����뱳�����±߾�
	 * @return
	 */
	public String getChartBottomMargin() {
		return chartBottomMargin;
	}
	/**
	 * ����ͼ����뱳�����±߾�
	 * @param chartBottomMargin
	 */
	public void setChartBottomMargin(String chartBottomMargin) {
		this.chartBottomMargin = chartBottomMargin;
	}
	/**
	 * ��ȡ�������ͼ��ı߾�
	 * @return
	 */
	public String getCaptionPadding() {
		return captionPadding;
	}
	/**
	 * ���ñ������ͼ��ı߾�
	 * @param captionPadding
	 */
	public void setCaptionPadding(String captionPadding) {
		this.captionPadding = captionPadding;
	}
}
