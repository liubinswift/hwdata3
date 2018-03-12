package com.viewscenes.printexcel;


import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;

public class ExcelTitle {

	private String name = ""; // title����
	private static WritableCellFormat titleCellFormat = null; // title��ʽ
	private ExcelTitle[] subTitles = new ExcelTitle[0]; // ���ر�ͷ,Ŀǰֻ֧�ֶ�����ͷ
	private int width = 0;
	private int height = 0;
	
	
	static{
		try{
			WritableFont titleFont = new WritableFont(WritableFont.createFont("����"),
	                12, WritableFont.BOLD, false);
			titleCellFormat = new WritableCellFormat();
			titleCellFormat.setAlignment(jxl.format.Alignment.CENTRE);
			titleCellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			titleCellFormat.setWrap(false);
			titleCellFormat.setFont(titleFont);
			titleCellFormat.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);//���߿� 
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	
	public ExcelTitle(String name,int width){
		this(name);
		this.width = width;
	}
	
	public ExcelTitle(String name){
		this(name,titleCellFormat);
		
		
	}
	
	public ExcelTitle(String name,int width ,int height){
		this(name);
		this.width = width;
		this.height = height;
	}
	
	
	public ExcelTitle(String name,WritableCellFormat titleCellFormat,int width ,int height) {
		this(name,titleCellFormat);
		this.width = width;
		this.height = height;
		
	}
	public ExcelTitle(String name,WritableCellFormat titleCellFormat) {
		this.name = name;
		this.titleCellFormat = titleCellFormat;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public WritableCellFormat getTitleCellFormat() {
		return titleCellFormat;
	}

	public void setTitleCellFormat(WritableCellFormat titleCellFormat) {
		this.titleCellFormat = titleCellFormat;
	}

	public ExcelTitle[] getSubTitles() {
		return subTitles;
	}

	public void setSubTitles(ExcelTitle[] subTitles)  {
//		if (subTitles!=null){
//			int totalWidth = 0;
//			for(int i=0;i<subTitles.length;i++){
//				ExcelTitle et = subTitles[i];
//				totalWidth += et.getWidth();
//			}
//			if (this.width>0){
//				if (totalWidth>this.width)
//					throw new Exception("������ͷ�ܿ��"+totalWidth+"����һ����ͷ���:"+width);
//			}
//		}
		this.subTitles = subTitles;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	
	
	
//	/**
//	 * ���м�����ͷ
//	 * <p>class/function:com.viewscenes.printexcel
//	 * <p>explain:
//	 * <p>author-date:̷��ΰ 2012-12-6
//	 * @param:
//	 * @return:
//	 */
//	public int getLevel(){
//		
//		
//		
//		return calcLevel(this);
//	}
//
//	
//	public static int calcLevel(ExcelTitle et){
//		
//		for(int i=0;i<et.getSubTitles().length;i++){
//			ExcelTitle tmpEt = et.getSubTitles()[i];
//			int level = tmpEt.subTitles.length;
//			if (level>0){
//				
//				return calcLevel(tmpEt);
//			}
//				
//		}
//		return 0;
//	}
}
