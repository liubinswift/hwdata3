package com.viewscenes.util.report.charts;
/**
 * <p>Column3D ��״ͼ </p>
 * <p>��Ӧ FusionCharts for Flex > XML Reference > Column 3D</p>
 * @author ��ʿ��
 * @version 1.0
 */
public class Column3D {
	
	public static String BuildChartEndXML = "\n</chart>";
	
	//Chart Objects  ͼ������Լ�����,��������
	public static String BACKGROUND = "BACKGROUND";
	public static String CANVAS  = "CANVAS";
	public static String CAPTION  = "CAPTION";
	public static String DATALABELS  = "DATALABELS";
	public static String DATAPLOT  = "DATAPLOT";
	public static String DATAVALUES  = "DATAVALUES";
	public static String DIVLINES  = "DIVLINES";
	public static String SUBCAPTION  = "SUBCAPTION";
	public static String TOOLTIP  = "TOOLTIP";
	public static String TRENDLINES  = "TRENDLINES";
	public static String VLINES   = "VLINES";
	public static String XAXISNAME    = "XAXISNAME";
	public static String YAXISNAME   = "YAXISNAME";
	public static String YAXISVALUES   = "YAXISVALUES";
	
	
	//<chart> element Attributes Ԫ������ -----------------------------------------
	//Functional Attributes  ��������
	private String animation  = null;			//�����Ƿ���ʾ����
	private String palette   = null;			//���õ�ɫ����1-5
	private String paletteColors  = null;		//���õ�ɫ�����ɫ,�Զ��ŷָ�
	private String showAboutMenuItem  = null;	//�����Ƿ���ʾ���ڲ˵�
	private String aboutMenuItemLabel  = null;	//���ù��ڲ˵��ı���
	private String aboutMenuItemLink  = null;	//���ù��ڲ˵�������
	private String showLabels  = null;			//�����Ƿ���ʾX���ǩ
	private String labelDisplay  = null;		//����X���ǩ�ı�����ʽ
	private String rotateLabels   = null;		//�����Ƿ���תX���ǩ
	private String slantLabels   = null;		//�����Ƿ���бX���ǩ
	private String labelStep  = null;			//����X���ǩ����ʾ����
	private String staggerLines   = null;		//����X���ǩ�Ĵ�����(������labelDisplay ='STAGGER'���ʹ��)
	private String showValues   = null;			//�����Ƿ���ʾY���ֵ
	private String rotateValues   = null;		//�����Ƿ���תY���ֵ
	private String placeValuesInside   = null;	//����Valueֵ�Ƿ���ͼ���ڲ�
	private String showYAxisValues   = null;	//�����Ƿ���ʾY��ı�ǩֵ
	private String showLimits   = null;			//�����Ƿ�����Y�����ֵ**(����)
	private String showDivLineValues   = null;	//�����Ƿ���ʾY��DIV�ߵ�ֵ
	private String yAxisValuesStep    = null;	//����Y���ǩ�ļ��
	private String showShadow    = null;		//�����Ƿ���ʾ��Ӱ
	private String adjustDiv    = null;			//�����Ƿ��Զ�����У׼��
	private String rotateYAxisName    = null;	//�����Ƿ���תY�����	
	private String yAxisNameWidth = null;		//����Y�����Ŀ��
	private String clickURL    = null;			//���õ������
	private String defaultAnimation    = null;	//����Ĭ�ϵĶ�������ʽ
	private String yAxisMinValue    = null;		//����Y�����Сֵ
	private String yAxisMaxValue    = null;		//����Y������ֵ
	private String setAdaptiveYMin = null;		//����Y��ֵ�������Ƿ�Ϊ0	
	private String maxColWidth    = null;		//�������������п�<3D>
	private String use3DLighting    = null;		//�����Ƿ���ʾ����<3D>
	
	
	private String exportEnabled = null;			//�����Ƿ����õ�������
	private String exportShowMenuItem = null;		//�����Ƿ���ʾ����ͼƬ�Ҳ˵�
	private String exportFileName = null;			//���õ���ͼƬ������
	private String exportFormats = null;			//���õ���ͼƬ�ķָ���ʾ(���API��ʹ��)
	private String showExportDataMenuItem = null; 	//�����Ƿ���ʾͼ���XML�˵�

	//Chart Titles and Axis Names ͼ�����ͱ�����
	private String caption = null;				//����ͼ���������
	private String subCaption  = null;			//����ͼ��ĸ�����
	private String xAxisName  = null;			//����ͼ���X���ǩ
	private String yAxisName  = null;			//����ͼ���Y���ǩ
	
	//Chart Cosmetics ͼ������
	
	private String bgColor  = null;				//���ñ���ɫ<�ɽ���>
	private String bgAlpha   = null;			//����͸����<�ɽ���>
	private String bgRatio   = null;			//���ñ����������<�ɽ���>
	private String bgAngle   = null;			//���ñ���ɫ���Ƕ�<�ɽ���>
	private String bgSWF   = null;				//���ñ���ͼƬ,SWF��ַ
	private String bgSWFAlpha   = null;			//���ñ���ͼƬ,SWF��͸����
	private String canvasBgColor   = null;		//���û����ı���ɫ<�ɽ���>
	private String canvasBgAlpha  = null;		//���û�������ɫ͸����<�ɽ���>
	private String canvasBorderColor  = null;	//���û����߿����ɫ<2D>
	private String canvasBorderThickness= null;	//���û����߿�ĺ��<2D>
	private String canvasBorderAlpha   = null;	//���û����߿��͸����<2D>
	private String showBorder  = null;			//�����Ƿ���ʾ�߿�
	private String borderColor   = null;		//���ñ߿����ɫ
	private String borderThickness   = null;	//���ñ߿�ĺ��,���﷽��
	private String borderAlpha   = null;		///���ñ߿�͸����
	private String showVLineLabelBorder = null;	//�����Ƿ���ʾ���������ߵı߿�
	private String logoURL = null;				//����LOGO��·��
	private String logoPosition  = null;		//����LOGO�����λ��	
	private String logoAlpha  = null;			//����LOGO��͸����
	private String logoScale  = null;			//����LOGO�����ű���
	private String logoLink  = null;			//����LOGO������
	
	private String canvasBaseColor  = null;		//���û����ײ��ı�����ɫ<3D>
	private String showCanvasBg  = null;		//�����Ƿ���ʾ��������<3D>
	private String showCanvasBase  = null;		//�����Ƿ���ʾ�����ײ�<3D>
	private String canvasBaseDepth  = null;		//���û����ײ������<3D>
	private String canvasBgDepth  = null;		//���û��������<3D>
	
	
	//Data Plot Cosmetics  ����ͼ������
	private String showPlotBorder = null;		//�����Ƿ���ʾͼ��ı߿�
	private String plotBorderColor   = null;	//����ͼ��߿����ɫ
	private String plotBorderAlpha  = null;		//����ͼ��߿��͸����
	private String plotFillAlpha = null;		//����ͼ�����ɫ��͸����
	private String overlapColumns   = null;		//����ͼ�����Ƿ��ص�<3D>
	
	
	//Divisional Lines & Grids �����ߺ�����
	private String numDivLines = null;					//���ý�׼���ߵ�����
	private String divLineColor = null;					//���ý�׼���ߵ���ɫ
	private String divLineThickness = null;				//���ý�׼���ߵĺ��
	private String divLineAlpha = null;					//���ý�׼���ߵ�͸����
	private String divLineIsDashed = null;				//���ý�׼�����Ƿ������߱�ʾ
	private String divLineDashLen = null;				//���ý�׼�������ߵĴ�С
	private String divLineDashGap = null;				//���ý�׼�������ߵļ��
	private String zeroPlaneColor = null;				//��������ߵ���ɫ
	private String zeroPlaneAlpha = null;				//��������ߵ�͸���� 
	private String zeroPlaneShowBorder  = null;			//�����Ƿ���ʾ��ƽ��ı߽�<3D>
	private String zeroPlaneBorderColor  = null;		//������ƽ��߽��߱߿����ɫ<3D>
	
	//Number Formatting 	���ָ�ʽ��
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
	private String yAxisValueDecimals = null;			//���ý�׼������ֵ��С��λ��,��Ҫ���Զ�����У׼����(adjustDiv)����Ϊ0,�����Զ���У׼���ߵ����ֵ(yAxisMaxValue)����Сֵ(yAxisMinValue)�Լ�У׼���ߵ�����(numVDivLines)		
	
	//Font Properties ��������
	private String baseFont = null;						//����Value��ǩ������
	private String baseFontSize = null;					//����Value��ǩ�������С
	private String baseFontColor = null;				//����Value��ǩ��������ɫ
	private String outCnvBaseFont  = null;				//���û���������(������Value��ǩ)
	private String outCnvBaseFontSize  = null;			//���û�������Ĵ�С(������Value��ǩ)
	private String outCnvBaseFontColor  = null;			//���û����������ɫ(������Value��ǩ)
	
	//Tool-tip ������ʾ	---------------------------------------------
	private String showToolTip  = null;					//�����Ƿ���ʾ��ʾ
	private String toolTipBgColor   = null;				//������ʾ��������ɫ
	private String toolTipBorderColor   = null;			//������ʾ���������ɫ
	private String toolTipSepChar   = null;				//����
	private String showToolTipShadow  = null;			//�����Ƿ���ʾ��ʾ���������Ӱ

	//Chart Padding & Margins ͼ��߾�
	private String captionPadding   = null;				//���ñ������ͼ��ı߾�
	private String xAxisNamePadding    = null;			//����ͼ�����X�����ı߾�
	private String yAxisNamePadding    = null;			//����ͼ�����Y�����ı߾�
	private String yAxisValuesPadding    = null;		//����ͼ�����Y���ǩ�ı߾�
	private String labelPadding    = null;				//����ͼ�����X���ǩ�ı߾�
	private String valuePadding    = null;				//����ͼ�����ͼ����ʾֵ�ı߾�
	private String plotSpacePercent    = null;			//����ͼ������֮��ľ���<2D��ͼ>
	private String chartLeftMargin    = null;			//����ͼ����뱳������߾�
	private String chartRightMargin    = null;			//����ͼ����뱳�����ұ߾�
	private String chartTopMargin    = null;			//����ͼ����뱳�����ϱ߾�
	private String chartBottomMargin    = null;			//����ͼ����뱳�����±߾�
	private String canvasPadding     = null;			//����ͼ�����������뻭�������ұ߾�<3D>
	
	/**
	 * ����ͼ�����������XML
	 * @return ͼ�����������XML
	 */
	public String BuildChartXML(){
		StringBuffer chartXML = new StringBuffer();
		chartXML.append("<chart ");
			
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
		//����X���ǩ�ı�����ʽ
		if(this.getLabelDisplay()!=null){
			chartXML.append(" labelDisplay = '" +this.labelDisplay + "' \n");
		}
		//�����Ƿ���תX���ǩ
		if(this.getRotateLabels()!=null){
			chartXML.append(" rotateLabels = '" +this.rotateLabels + "' \n");
		}
		//�����Ƿ���бX���ǩ
		if(this.getSlantLabels()!=null){
			chartXML.append(" slantLabels = '" +this.slantLabels + "' \n");
		}
		//����X���ǩ����ʾ����
		if(this.getLabelStep()!=null){
			chartXML.append(" labelStep = '" +this.labelStep + "' \n");
		}
		//����X���ǩ�Ĵ�����(������labelDisplay ='STAGGER'���ʹ��)
		if(this.getStaggerLines()!=null){
			chartXML.append(" staggerLines = '" +this.staggerLines + "' \n");
		}
		//�����Ƿ���ʾY���ֵ
		if(this.getShowValues()!=null){
			chartXML.append(" showValues = '" +this.showValues + "' \n");
		}
		//�����Ƿ���תY���ֵ
		if(this.getRotateValues()!=null){
			chartXML.append(" rotateValues = '" +this.rotateValues + "' \n");
		}
		//����Valueֵ�Ƿ���ͼ���ڲ�
		if(this.getPlaceValuesInside()!=null){
			chartXML.append(" placeValuesInside = '" +this.placeValuesInside + "' \n");
		}
		//�����Ƿ���ʾY��ı�ǩֵ
		if(this.getShowYAxisValues()!=null){
			chartXML.append(" showYAxisValues = '" +this.showYAxisValues + "' \n");
		}
		//�����Ƿ�����Y�����ֵ**(����)
		if(this.getShowLimits()!=null){
			chartXML.append(" showLimits = '" +this.showLimits + "' \n");
		}
		//�����Ƿ���ʾY��DIV�ߵ�ֵ
		if(this.getShowDivLineValues()!=null){
			chartXML.append(" showDivLineValues = '" +this.showDivLineValues + "' \n");
		}
		//����Y���ǩ�ļ��
		if(this.getYAxisValuesStep()!=null){
			chartXML.append(" yAxisValuesStep = '" +this.yAxisValuesStep + "' \n");
		}
		//�����Ƿ���ʾ�е���Ӱ
		if(this.getShowShadow()!=null){
			chartXML.append(" showShadow = '" +this.showShadow + "' \n");
		}
		//�����Ƿ��Զ�����У׼��
		if(this.getAdjustDiv()!=null){
			chartXML.append(" adjustDiv = '" +this.adjustDiv + "' \n");
		}
		//�����Ƿ���תY�����
		if(this.getRotateYAxisName()!=null){
			chartXML.append(" rotateYAxisName = '" +this.rotateYAxisName + "' \n");
		}
		//����Y�����Ŀ��
		if(this.getYAxisNameWidth()!=null){
			chartXML.append(" yAxisNameWidth = '" +this.yAxisNameWidth + "' \n");
		}
		//���õ������
		if(this.getClickURL()!=null){
			chartXML.append(" clickURL = '" +this.clickURL + "' \n");
		}
		//������ʾĬ�ϵĶ���
		if(this.getDefaultAnimation()!=null){
			chartXML.append(" defaultAnimation = '" +this.defaultAnimation + "' \n");
		}
		//����Y�����Сֵ
		if(this.getYAxisMinValue()!=null){
			chartXML.append(" yAxisMinValue = '" +this.yAxisMinValue + "' \n");
		}
		//����Y������ֵ
		if(this.getYAxisMaxValue()!=null){
			chartXML.append(" yAxisMaxValue = '" +this.yAxisMaxValue + "' \n");
		}
		//����Y��ֵ�������Ƿ�Ϊ0	
		if(this.getSetAdaptiveYMin()!=null){
			chartXML.append(" setAdaptiveYMin = '" +this.setAdaptiveYMin + "' \n");
		}
		//�������ӵ�����п�
		if(this.getMaxColWidth()!=null){
			chartXML.append(" maxColWidth = '" +this.maxColWidth + "' \n");
		}
		//�����Ƿ�ʹ���Ƚ��Ķ���Ч��
		if(this.getUse3DLighting()!=null){
			chartXML.append(" use3DLighting = '" +this.use3DLighting + "' \n");
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
		//����ͼ���X���ǩ
		if(this.getXAxisName()!=null){
			chartXML.append(" xAxisName = '" +this.xAxisName + "' \n");
		}
		//����ͼ���Y���ǩ
		if(this.getYAxisName()!=null){
			chartXML.append(" yAxisName = '" +this.yAxisName + "' \n");
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
		//���û����ı���ɫ<�ɽ���>
		if(this.getCanvasBgColor()!=null){
			chartXML.append(" canvasBgColor = '" +this.canvasBgColor + "' \n");
		}
		//���û�������ɫ͸����<�ɽ���>
		if(this.getCanvasBgAlpha()!=null){
			chartXML.append(" canvasBgAlpha = '" +this.canvasBgAlpha + "' \n");
		}
		//���û����߿����ɫ<2D>
		if(this.getCanvasBorderColor()!=null){
			chartXML.append(" canvasBorderColor = '" +this.canvasBorderColor + "' \n");
		}
		//���û����߿�ĺ��<2D>
		if(this.getCanvasBorderThickness()!=null){
			chartXML.append(" canvasBorderThickness = '" +this.canvasBorderThickness + "' \n");
		}
		//���û����߿��͸����<2D>
		if(this.getCanvasBorderAlpha()!=null){
			chartXML.append(" canvasBorderAlpha = '" +this.canvasBorderAlpha + "' \n");
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
		//�����Ƿ���ʾ���������ߵı߿�
		if(this.getShowVLineLabelBorder()!=null){
			chartXML.append(" showVLineLabelBorder = '" +this.showVLineLabelBorder + "' \n");
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
		//����ͼ��߿��͸����
		if(this.getPlotBorderAlpha()!=null){
			chartXML.append(" plotBorderAlpha = '" +this.plotBorderAlpha + "' \n");
		}
		//����ͼ�����ɫ��͸����
		if(this.getPlotFillAlpha()!=null){
			chartXML.append(" plotFillAlpha = '" +this.plotFillAlpha + "' \n");
		}
		//����ͼ�����Ƿ��ص�
		if(this.getOverlapColumns()!=null){
			chartXML.append(" overlapColumns = '" +this.overlapColumns + "' \n");
		}
		
		//Divisional Lines & Grids �����ߺ����� -----------------------------------
		//���ý�׼���ߵ�����
		if(this.getNumDivLines()!=null){
			chartXML.append(" numDivLines = '" +this.numDivLines + "' \n");
		}
		//���ý�׼���ߵ���ɫ
		if(this.getDivLineColor()!=null){
			chartXML.append(" divLineColor = '" +this.divLineColor + "' \n");
		}
		//���ý�׼���ߵĺ��
		if(this.getDivLineThickness()!=null){
			chartXML.append(" divLineThickness = '" +this.divLineThickness + "' \n");
		}
		//���ý�׼���ߵ�͸����
		if(this.getDivLineAlpha()!=null){
			chartXML.append(" divLineAlpha = '" +this.divLineAlpha + "' \n");
		}
		//���ý�׼�����Ƿ������߱�ʾ
		if(this.getDivLineIsDashed()!=null){
			chartXML.append(" divLineIsDashed = '" +this.divLineIsDashed + "' \n");
		}
		//���ý�׼�������ߵĴ�С
		if(this.getDivLineDashLen()!=null){
			chartXML.append(" divLineDashLen = '" +this.divLineDashLen + "' \n");
		}
		//���ý�׼�������ߵļ��
		if(this.getDivLineDashGap()!=null){
			chartXML.append(" divLineDashGap = '" +this.divLineDashGap + "' \n");
		}
		//��������ߵ���ɫ
		if(this.getZeroPlaneColor()!=null){
			chartXML.append(" zeroPlaneColor = '" +this.zeroPlaneColor + "' \n");
		}
		//��������ߵ�͸����
		if(this.getZeroPlaneAlpha()!=null){
			chartXML.append(" zeroPlaneAlpha = '" +this.zeroPlaneAlpha + "' \n");
		}
		//�����Ƿ���ʾ��ƽ��ı߽�
		if(this.getZeroPlaneShowBorder()!=null){
			chartXML.append(" zeroPlaneShowBorder = '" +this.zeroPlaneShowBorder + "' \n");
		}
		//������ƽ��߽��߱߿����ɫ
		if(this.getZeroPlaneBorderColor()!=null){
			chartXML.append(" zeroPlaneBorderColor = '" +this.zeroPlaneBorderColor + "' \n");
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
		//���ý�׼������ֵ��С��λ��,��Ҫ���Զ�����У׼����(adjustDiv)����Ϊ0,�����Զ���У׼���ߵ����ֵ(yAxisMaxValue)����Сֵ(yAxisMinValue)�Լ�У׼���ߵ�����(numVDivLines)
		if(this.getYAxisValueDecimals()!=null){
			chartXML.append(" yAxisValueDecimals = '" +this.yAxisValueDecimals + "' \n");
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
		//���û���������(������Value��ǩ)
		if(this.getOutCnvBaseFont()!=null){
			chartXML.append(" outCnvBaseFont = '" +this.outCnvBaseFont + "' \n");
		}
		//���û���������(������Value��ǩ)
		if(this.getOutCnvBaseFontSize()!=null){
			chartXML.append(" outCnvBaseFontSize = '" +this.outCnvBaseFontSize + "' \n");
		}
		//���û����������ɫ(������Value��ǩ)
		if(this.getOutCnvBaseFontColor()!=null){
			chartXML.append(" outCnvBaseFontColor = '" +this.outCnvBaseFontColor + "' \n");
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
		//����ͼ�����X�����ı߾�
		if(this.getXAxisNamePadding()!=null){
			chartXML.append(" xAxisNamePadding = '" +this.xAxisNamePadding + "' \n");
		}
		//����ͼ�����Y�����ı߾�
		if(this.getYAxisNamePadding()!=null){
			chartXML.append(" yAxisNamePadding = '" +this.yAxisNamePadding + "' \n");
		}
		//����ͼ�����Y���ǩ�ı߾�
		if(this.getYAxisValuesPadding()!=null){
			chartXML.append(" yAxisValuesPadding = '" +this.yAxisValuesPadding + "' \n");
		}
		//����ͼ�����X���ǩ�ı߾�
		if(this.getLabelPadding()!=null){
			chartXML.append(" labelPadding = '" +this.labelPadding + "' \n");
		}
		//����ͼ�����ͼ����ʾֵ�ı߾�
		if(this.getValuePadding()!=null){
			chartXML.append(" valuePadding = '" +this.valuePadding + "' \n");
		}
		//����ͼ������֮��ľ���<2D��ͼ>
		if(this.getPlotSpacePercent()!=null){
			chartXML.append(" plotSpacePercent = '" +this.plotSpacePercent + "' \n");
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
		//����ͼ�����������뻭�������ұ߾�<3D>
		if(this.getCanvasPadding()!=null){
			chartXML.append(" canvasPadding = '" +this.canvasPadding + "' \n");
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
	 * ��ȡX���ǩ�ı�����ʽ
	 * @return	ȡֵ��Χ(WRAP:,STAGGER:��,ROTATE:��ת,NONE:��)
	 */
	public String getLabelDisplay() {
		return labelDisplay;
	}
	/**
	 * ����X���ǩ�ı�����ʽ
	 * @param labelDisplay	ȡֵ��Χ(WRAP:,STAGGER:��,ROTATE:��ת,NONE:��)
	 */
	public void setLabelDisplay(String labelDisplay) {
		this.labelDisplay = labelDisplay;
	}
	/**
	 * ��ȡ�Ƿ���תX���ǩ
	 * @return	ȡֵ��Χ(1:true 0:false)
	 */
	public String getRotateLabels() {
		return rotateLabels;
	}
	/**
	 * �����Ƿ���תX���ǩ
	 * @param rotateLabels	ȡֵ��Χ(1:true 0:false)
	 */
	public void setRotateLabels(String rotateLabels) {
		this.rotateLabels = rotateLabels;
	}
	/**
	 * ��ȡ�Ƿ���бX���ǩ
	 * @return	ȡֵ��Χ(1:true 0:false)
	 */
	public String getSlantLabels() {
		return slantLabels;
	}
	/**
	 * �����Ƿ���бX���ǩ
	 * @param slantLabels	ȡֵ��Χ(1:true 0:false)
	 */
	public void setSlantLabels(String slantLabels) {
		this.slantLabels = slantLabels;
	}
	/**
	 * ��ȡX���ǩ����ʾ����
	 * @return	ȡֵ��Χ(���ڵ���1)
	 */
	public String getLabelStep() {
		return labelStep;
	}
	/**
	 * ����X���ǩ����ʾ����
	 * @param labelStep	ȡֵ��Χ(���ڵ���1)
	 */
	public void setLabelStep(String labelStep) {
		this.labelStep = labelStep;
	}
	/**
	 * ��ȡX���ǩ�Ĵ�����
	 * ������labelDisplay ='STAGGER'���ʹ��
	 * @return	ȡֵ��Χ(���ڵ���2)
	 */
	public String getStaggerLines() {
		return staggerLines;
	}
	/**
	 * ����X���ǩ�Ĵ�����
	 * ������labelDisplay ='STAGGER'���ʹ��
	 * @param staggerLines	ȡֵ��Χ(���ڵ���2)
	 */
	public void setStaggerLines(String staggerLines) {
		this.staggerLines = staggerLines;
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
	 *  ��ȡ�Ƿ���תY���ֵ
	 * @return	ȡֵ��Χ(1:true 0:false)
	 */
	public String getRotateValues() {
		return rotateValues;
	}
	/**
	 * �����Ƿ���תY���ֵ
	 * @param rotateValues	ȡֵ��Χ(1:true 0:false)
	 */
	public void setRotateValues(String rotateValues) {
		this.rotateValues = rotateValues;
	}
	
	/**
	 * ��ȡValueֵ�Ƿ���ͼ���ڲ�
	 * @return
	 */
	public String getPlaceValuesInside() {
		return placeValuesInside;
	}
	/**
	 * ����Valueֵ�Ƿ���ͼ���ڲ�
	 * @param placeValuesInside
	 */
	public void setPlaceValuesInside(String placeValuesInside) {
		this.placeValuesInside = placeValuesInside;
	}
	/**
	 * ��ȡ�Ƿ���ʾY��ı�ǩֵ
	 * @return
	 */
	public String getShowYAxisValues() {
		return showYAxisValues;
	}
	/**
	 * �����Ƿ���ʾY��ı�ǩֵ
	 * @param showYAxisValues
	 */
	public void setShowYAxisValues(String showYAxisValues) {
		this.showYAxisValues = showYAxisValues;
	}
	/**
	 * ��ȡ�Ƿ�����Y�����ֵ**(����)
	 * @return
	 */
	public String getShowLimits() {
		return showLimits;
	}
	/**
	 * �����Ƿ�����Y�����ֵ**(����)
	 * @param showLimits
	 */
	public void setShowLimits(String showLimits) {
		this.showLimits = showLimits;
	}
	/**
	 * ��ȡ�Ƿ���ʾY��DIV�ߵ�ֵ
	 * @return
	 */
	public String getShowDivLineValues() {
		return showDivLineValues;
	}
	/**
	 * �����Ƿ���ʾY��DIV�ߵ�ֵ
	 * @param showDivLineValues
	 */
	public void setShowDivLineValues(String showDivLineValues) {
		this.showDivLineValues = showDivLineValues;
	}
	/**
	 * ��ȡY���ǩ�ļ��
	 * @return
	 */
	public String getYAxisValuesStep() {
		return yAxisValuesStep;
	}
	/**
	 * ����Y���ǩ�ļ��
	 * @param axisValuesStep
	 */
	public void setYAxisValuesStep(String axisValuesStep) {
		yAxisValuesStep = axisValuesStep;
	}
	/**
	 * ��ȡ�Ƿ���ʾ�е���Ӱ
	 * @return
	 */
	public String getShowShadow() {
		return showShadow;
	}
	/**
	 * �����Ƿ���ʾ�е���Ӱ
	 * @param showShadow
	 */
	public void setShowShadow(String showShadow) {
		this.showShadow = showShadow;
	}
	/**
	 * ��ȡ�Ƿ��Զ�����У׼��
	 * @return
	 */
	public String getAdjustDiv() {
		return adjustDiv;
	}
	/**
	 * �����Ƿ��Զ�����У׼��
	 * @param adjustDiv
	 */
	public void setAdjustDiv(String adjustDiv) {
		this.adjustDiv = adjustDiv;
	}
	/**
	 * ��ȡ�Ƿ���תY������,Ĭ��Ϊ1:����ת
	 * @return
	 */
	public String getRotateYAxisName() {
		return rotateYAxisName;
	}
	/**
	 * �����Ƿ���תY������,Ĭ��Ϊ1:����ת;
	 * @param rotateYAxisName (1:����ת,0:��ת)
	 */
	public void setRotateYAxisName(String rotateYAxisName) {
		this.rotateYAxisName = rotateYAxisName;
	}
	
	/**
	 * ��ȡY�����Ŀ��
	 * @return
	 */
	public String getYAxisNameWidth() {
		return yAxisNameWidth;
	}
	/**
	 * ����Y�����Ŀ��
	 * @param axisNameWidth
	 */
	public void setYAxisNameWidth(String axisNameWidth) {
		yAxisNameWidth = axisNameWidth;
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
	 * ��ȡY�����Сֵ
	 * @return
	 */
	public String getYAxisMinValue() {
		return yAxisMinValue;
	}
	/**
	 * ����Y�����Сֵ
	 * @param axisMinValue
	 */
	public void setYAxisMinValue(String axisMinValue) {
		yAxisMinValue = axisMinValue;
	}
	/**
	 * ��ȡY������ֵ
	 * @return
	 */
	public String getYAxisMaxValue() {
		return yAxisMaxValue;
	}
	/**
	 * ����Y������ֵ
	 * @param axisMaxValue
	 */
	public void setYAxisMaxValue(String axisMaxValue) {
		yAxisMaxValue = axisMaxValue;
	}
	/**
	 * ��ȡY��ֵ�������Ƿ�Ϊ0
	 * @return
	 */
	public String getSetAdaptiveYMin() {
		return setAdaptiveYMin;
	}
	/**
	 * ����Y��ֵ�������Ƿ�Ϊ0
	 * @param setAdaptiveYMin
	 */
	public void setSetAdaptiveYMin(String setAdaptiveYMin) {
		this.setAdaptiveYMin = setAdaptiveYMin;
	}
	/**
	 * ��ȡ���ӵ�����п�<3D>
	 * @return
	 */
	public String getMaxColWidth() {
		return maxColWidth;
	}
	/**
	 * �������ӵ�����п�<3D>
	 * @param maxColWidth
	 */
	public void setMaxColWidth(String maxColWidth) {
		this.maxColWidth = maxColWidth;
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
	/**
	 * ��ȡͼ���X���ǩ
	 * @return
	 */
	public String getXAxisName() {
		return xAxisName;
	}
	/**
	 * ����ͼ���X���ǩ
	 * @param axisName
	 */
	public void setXAxisName(String axisName) {
		xAxisName = axisName;
	}
	/**
	 * ��ȡͼ���Y���ǩ
	 * @return
	 */
	public String getYAxisName() {
		return yAxisName;
	}
	/**
	 * ����ͼ���Y���ǩ
	 * @param axisName
	 */
	public void setYAxisName(String axisName) {
		yAxisName = axisName;
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
	 * ��ȡ����������ɫ<�ɽ���>
	 * @return Hex Color Code16
	 */
	public String getCanvasBgColor() {
		return canvasBgColor;
	}
	/**
	 * ���û���������ɫ<�ɽ���>
	 * @param canvasBgColor Hex Color Code16
	 */
	public void setCanvasBgColor(String canvasBgColor) {
		this.canvasBgColor = canvasBgColor;
	}
	/**
	 * ��ȡ����������ɫ��͸����<�ɽ���>
	 * @return
	 */
	public String getCanvasBgAlpha() {
		return canvasBgAlpha;
	}
	/**
	 * ���û���������ɫ��͸����<�ɽ���>
	 * @param canvasBgAlpha,ȡֵ��Χ(1-100)
	 */
	public void setCanvasBgAlpha(String canvasBgAlpha) {
		this.canvasBgAlpha = canvasBgAlpha;
	}
	/**
	 * ��ȡ�����߿����ɫ <2D>
	 * @return Hex Color Code16
	 */
	public String getCanvasBorderColor() {
		return canvasBorderColor;
	}
	/**
	 * ���û����߿����ɫ <2D>
	 * @param canvasBorderColor Hex Color Code16
	 */
	public void setCanvasBorderColor(String canvasBorderColor) {
		this.canvasBorderColor = canvasBorderColor;
	}
	/**
	 * ��ȡ�����߿�ĺ�� <2D>
	 * @return
	 */
	public String getCanvasBorderThickness() {
		return canvasBorderThickness;
	}
	/**
	 * ���û����߿�ĺ�� <2D>
	 * @param canvasBorderThickness
	 */
	public void setCanvasBorderThickness(String canvasBorderThickness) {
		this.canvasBorderThickness = canvasBorderThickness;
	}
	/**
	 * ��ȡ�����߿��͸���� <2D>
	 * @return
	 */
	public String getCanvasBorderAlpha() {
		return canvasBorderAlpha;
	}
	/**
	 * ���û����߿��͸���� <2D>
	 * @param canvasBorderAlpha
	 */
	public void setCanvasBorderAlpha(String canvasBorderAlpha) {
		this.canvasBorderAlpha = canvasBorderAlpha;
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
	 * ��ȡ�Ƿ���ʾ���������ߵı߿�		
	 * @return
	 */	
	public String getShowVLineLabelBorder() {
		return showVLineLabelBorder;
	}
	/**
	 * �����Ƿ���ʾ���������ߵı߿�		
	 * @param showVLineLabelBorder
	 */
	public void setShowVLineLabelBorder(String showVLineLabelBorder) {
		this.showVLineLabelBorder = showVLineLabelBorder;
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
	/**
	 * ��ȡ�����ײ��ı�����ɫ<3D>
	 * @return
	 */
	public String getCanvasBaseColor() {
		return canvasBaseColor;
	}
	/**
	 * ���û����ײ��ı�����ɫ<3D>
	 * @param canvasBaseColor
	 */
	public void setCanvasBaseColor(String canvasBaseColor) {
		this.canvasBaseColor = canvasBaseColor;
	}
	/**
	 * ��ȡ�Ƿ���ʾ��������<3D>
	 * @return
	 */
	public String getShowCanvasBg() {
		return showCanvasBg;
	}
	/**
	 * �����Ƿ���ʾ��������<3D>
	 * @param showCanvasBg
	 */
	public void setShowCanvasBg(String showCanvasBg) {
		this.showCanvasBg = showCanvasBg;
	}
	/**
	 * ��ȡ�Ƿ���ʾ�����ײ�<3D>
	 * @return
	 */
	public String getShowCanvasBase() {
		return showCanvasBase;
	}
	/**
	 * �����Ƿ���ʾ�����ײ�<3D>
	 * @param showCanvasBase
	 */
	public void setShowCanvasBase(String showCanvasBase) {
		this.showCanvasBase = showCanvasBase;
	}
	/**
	 * ��ȡ�����ײ������<3D>
	 * @return
	 */
	public String getCanvasBaseDepth() {
		return canvasBaseDepth;
	}
	/**
	 * ���û����ײ������<3D>
	 * @param canvasBaseDepth
	 */
	public void setCanvasBaseDepth(String canvasBaseDepth) {
		this.canvasBaseDepth = canvasBaseDepth;
	}
	/**
	 * ��ȡ���������<3D>
	 * @return
	 */
	public String getCanvasBgDepth() {
		return canvasBgDepth;
	}
	/**
	 * ���û��������<3D>
	 * @param canvasBgDepth
	 */
	public void setCanvasBgDepth(String canvasBgDepth) {
		this.canvasBgDepth = canvasBgDepth;
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
	 * ��ȡͼ�����Ƿ��ص�<3D>
	 * @return
	 */
	public String getOverlapColumns() {
		return overlapColumns;
	}
	/**
	 * ����ͼ�����Ƿ��ص�<3D>
	 * @param overlapColumns
	 */
	public void setOverlapColumns(String overlapColumns) {
		this.overlapColumns = overlapColumns;
	}
	
	//Divisional Lines & Grids �����ߺ�����
	/**
	 * ��ȡУ׼���ߵ�����
	 * @return
	 */
	public String getNumDivLines() {
		return numDivLines;
	}
	/**
	 * ����У׼���ߵ�����
	 * @param numDivLines
	 */
	public void setNumDivLines(String numDivLines) {
		this.numDivLines = numDivLines;
	}
	/**
	 * ��ȡУ׼���ߵ���ɫ
	 * @return Hex Color Code16
	 */
	public String getDivLineColor() {
		return divLineColor;
	}
	/**
	 * ����У׼���ߵ���ɫ
	 * @param divLineColor Hex Color Code16
	 */
	public void setDivLineColor(String divLineColor) {
		this.divLineColor = divLineColor;
	}
	/**
	 * ��ȡУ׼���ߵĺ��
	 * @return
	 */
	public String getDivLineThickness() {
		return divLineThickness;
	}
	/**
	 * ����У׼���ߵĺ��
	 * @param divLineThickness
	 */
	public void setDivLineThickness(String divLineThickness) {
		this.divLineThickness = divLineThickness;
	}
	/**
	 * ��ȡУ׼���ߵ�͸����
	 * @return
	 */
	public String getDivLineAlpha() {
		return divLineAlpha;
	}
	/**
	 * ����У׼���ߵ�͸����
	 * @param divLineAlpha
	 */
	public void setDivLineAlpha(String divLineAlpha) {
		this.divLineAlpha = divLineAlpha;
	}

	/**
	 * ��ȡУ׼�����Ƿ������߱�ʾ
	 * @return
	 */
	public String getDivLineIsDashed() {
		return divLineIsDashed;
	}
	/**
	 * ����У׼�����Ƿ������߱�ʾ
	 * @param divLineIsDashed
	 */
	public void setDivLineIsDashed(String divLineIsDashed) {
		this.divLineIsDashed = divLineIsDashed;
	}
	/**
	 * ��ȡУ׼���������߱�ʾ��,���ߵĳ���
	 * @return
	 */
	public String getDivLineDashLen() {
		return divLineDashLen;
	}
	/**
	 * ����У׼���������߱�ʾ��,���ߵĳ���
	 * ��Ҫ�������У׼�����Ƿ������߱�ʾ(setDivLineIsDashed����)ʹ��
	 * @param divLineDashLen
	 */
	public void setDivLineDashLen(String divLineDashLen) {
		this.divLineDashLen = divLineDashLen;
	}
	/**
	 * ��ȡ��У׼���������߱�ʾ��,���ߵļ��
	 * @return
	 */
	public String getDivLineDashGap() {
		return divLineDashGap;
	}
	/**
	 * ����У׼���������߱�ʾ��,���ߵļ��
	 * ��Ҫ�������У׼�����Ƿ������߱�ʾ(setDivLineIsDashed����)ʹ��
	 * @param divLineDashGap
	 */
	public void setDivLineDashGap(String divLineDashGap) {
		this.divLineDashGap = divLineDashGap;
	}
	/**
	 * ��ȡ����ߵ���ɫ
	 * @return Hex Color Code16
	 */
	public String getZeroPlaneColor() {
		return zeroPlaneColor;
	}
	/**
	 * ��������ߵ���ɫ
	 * @param zeroPlaneColor Hex Color Code16
	 */
	public void setZeroPlaneColor(String zeroPlaneColor) {
		this.zeroPlaneColor = zeroPlaneColor;
	}
	/**
	 * ��ȡ����ߵ�͸����
	 * @return
	 */
	public String getZeroPlaneAlpha() {
		return zeroPlaneAlpha;
	}
	/**
	 * ��������ߵ�͸����
	 * @param zeroPlaneAlpha
	 */
	public void setZeroPlaneAlpha(String zeroPlaneAlpha) {
		this.zeroPlaneAlpha = zeroPlaneAlpha;
	}
	/**
	 * ��ȡ�Ƿ���ʾ��ƽ��ı߽�<3D>
	 * @return
	 */
	public String getZeroPlaneShowBorder() {
		return zeroPlaneShowBorder;
	}
	/**
	 * �����Ƿ���ʾ��ƽ��ı߽�<3D>
	 * @param zeroPlaneShowBorder
	 */
	public void setZeroPlaneShowBorder(String zeroPlaneShowBorder) {
		this.zeroPlaneShowBorder = zeroPlaneShowBorder;
	}
	/**
	 * ��ȡ��ƽ��߽��߱߿����ɫ<3D>
	 * @return
	 */
	public String getZeroPlaneBorderColor() {
		return zeroPlaneBorderColor;
	}
	/**
	 * ������ƽ��߽��߱߿����ɫ<3D>
	 * @param zeroPlaneBorderColor
	 */
	public void setZeroPlaneBorderColor(String zeroPlaneBorderColor) {
		this.zeroPlaneBorderColor = zeroPlaneBorderColor;
	}
	//Number Formatting ���ָ�ʽ�� ---------------------------------------
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
	/**
	 * ��ȡ��׼������ֵ��С��λ��,
	 * ��Ҫ���Զ�����У׼����(adjustDiv)����Ϊ0,
	 * �����Զ���У׼���ߵ����ֵ(yAxisMaxValue)����Сֵ(yAxisMinValue)
	 * �Լ�У׼���ߵ�����(numVDivLines)
	 * @return
	 */
	public String getYAxisValueDecimals() {
		return yAxisValueDecimals;
	}
	/**
	 * ���ý�׼������ֵ��С��λ��,
	 * ��Ҫ���Զ�����У׼����(adjustDiv)����Ϊ0,
	 * �����Զ���У׼���ߵ����ֵ(yAxisMaxValue)����Сֵ(yAxisMinValue)
	 * �Լ�У׼���ߵ�����(numVDivLines)
	 * @param axisValueDecimals
	 */
	public void setYAxisValueDecimals(String axisValueDecimals) {
		yAxisValueDecimals = axisValueDecimals;
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
	/**
	 * ��ȡ����������(������Value��ǩ)
	 * @return
	 */
	public String getOutCnvBaseFont() {
		return outCnvBaseFont;
	}
	/**
	 * ���û���������(������Value��ǩ)
	 * @param outCnvBaseFont
	 */
	public void setOutCnvBaseFont(String outCnvBaseFont) {
		this.outCnvBaseFont = outCnvBaseFont;
	}
	/**
	 * ��ȡ��������Ĵ�С(������Value��ǩ)
	 * @return
	 */
	public String getOutCnvBaseFontSize() {
		return outCnvBaseFontSize;
	}
	/**
	 * ���û�������Ĵ�С(������Value��ǩ)
	 * @param outCnvBaseFontSize
	 */
	public void setOutCnvBaseFontSize(String outCnvBaseFontSize) {
		this.outCnvBaseFontSize = outCnvBaseFontSize;
	}
	/**
	 * ��ȡ���������ɫ(������Value��ǩ)
	 * @return
	 */
	public String getOutCnvBaseFontColor() {
		return outCnvBaseFontColor;
	}
	/**
	 * ���ò��������ɫ(������Value��ǩ)
	 * @param outCnvBaseFontColor
	 */
	public void setOutCnvBaseFontColor(String outCnvBaseFontColor) {
		this.outCnvBaseFontColor = outCnvBaseFontColor;
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
	/**
	 * ��ȡͼ�����X�����ı߾�
	 * @return
	 */
	public String getXAxisNamePadding() {
		return xAxisNamePadding;
	}
	/**
	 * ����ͼ�����X�����ı߾�
	 * @param axisNamePadding
	 */
	public void setXAxisNamePadding(String axisNamePadding) {
		xAxisNamePadding = axisNamePadding;
	}
	/**
	 * ��ȡͼ�����X���ǩ�ı߾�
	 * @return
	 */
	public String getLabelPadding() {
		return labelPadding;
	}
	/**
	 * ����ͼ�����X���ǩ�ı߾�
	 * @param labelPadding
	 */
	public void setLabelPadding(String labelPadding) {
		this.labelPadding = labelPadding;
	}
	/**
	 * ��ȡͼ�����Y�����ı߾�
	 * @return
	 */
	public String getYAxisNamePadding() {
		return yAxisNamePadding;
	}
	/**
	 * ����ͼ�����Y�����ı߾�
	 * @param axisNamePadding
	 */
	public void setYAxisNamePadding(String axisNamePadding) {
		yAxisNamePadding = axisNamePadding;
	}
	/**
	 * ��ȡͼ�����Y���ǩ�ı߾�
	 * @return
	 */
	public String getYAxisValuesPadding() {
		return yAxisValuesPadding;
	}
	/**
	 * ����ͼ�����Y���ǩ�ı߾�
	 * @param axisValuesPadding
	 */
	public void setYAxisValuesPadding(String axisValuesPadding) {
		yAxisValuesPadding = axisValuesPadding;
	}
	/**
	 * ��ȡͼ�����ͼ����ʾֵ�ı߾�
	 * @return
	 */
	public String getValuePadding() {
		return valuePadding;
	}
	/**
	 * ����ͼ�����ͼ����ʾֵ�ı߾�
	 * @param valuePadding
	 */
	public void setValuePadding(String valuePadding) {
		this.valuePadding = valuePadding;
	}
	/**
	 * ��ȡͼ������֮��ľ���<2D��ͼ>
	 * @return
	 */
	public String getPlotSpacePercent() {
		return plotSpacePercent;
	}
	/**
	 * ����ͼ������֮��ľ���<2D��ͼ>
	 * @param plotSpacePercent
	 */
	public void setPlotSpacePercent(String plotSpacePercent) {
		this.plotSpacePercent = plotSpacePercent;
	}
	/**
	 * ��ȡͼ�����������뻭�������ұ߾�<3D>
	 * @return
	 */
	public String getCanvasPadding() {
		return canvasPadding;
	}
	/**
	 * ����ͼ�����������뻭�������ұ߾�<3D>
	 * @param canvasPadding
	 */
	public void setCanvasPadding(String canvasPadding) {
		this.canvasPadding = canvasPadding;
	}
}
