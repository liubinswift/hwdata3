package com.viewscenes.printexcel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.SheetSettings;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import java.net.*;
import com.viewscenes.util.StringTool;

//import com.viewscenes.crystalreport.Util;


/**
 * <p>Title:EXCEL�ļ������� </p>
 *
 * <p>Description:��ResultSet�����е�����д��EXCEL�ļ� </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class JExcel {

	public static final String TYPE = "application/vnd.ms-excel";
    /**
     * ���췽��
     */
    public JExcel() {

        try {
            //�趨��������
            dateFont = new WritableFont(WritableFont.createFont(fontType),
                                        10, WritableFont.NO_BOLD, false);
            //private WritableFont dateFont14 = null;
            dateFont14 = new WritableFont(WritableFont.createFont(fontType),
                    14, WritableFont.NO_BOLD, false);
            datetitleFont = new WritableFont(WritableFont.createFont(fontType),
            		10, WritableFont.BOLD, false);
            //�趨��ǩ����
            titleFont = new WritableFont(WritableFont.createFont(fontType),
                                         12, WritableFont.BOLD, false);
            repTitleFont = new WritableFont(WritableFont.createFont(fontType),
                    24, WritableFont.BOLD, false);
            subtitleFont = new WritableFont(WritableFont.createFont(fontType),
            		11, WritableFont.NO_BOLD, false);
            //public WritableCellFormat noButtonFormat = null;
            //public WritableCellFormat noTopRightFormat = null;
            //public WritableCellFormat noTopLeftFormat = null;
            noButtonFormat = new WritableCellFormat();//ȡ��Ԫ��
            noButtonFormat.setAlignment(jxl.format.Alignment.CENTRE);//ˮƽ����
            noButtonFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//��ֱ����
            noButtonFormat.setBorder(jxl.format.Border.TOP,
                                     jxl.format.BorderLineStyle.THIN);//ȡ�����б߿�,������
            noButtonFormat.setBorder(jxl.format.Border.LEFT,
                    jxl.format.BorderLineStyle.THIN);//ȡ�����б߿�,������
            noButtonFormat.setBorder(jxl.format.Border.RIGHT,
                    jxl.format.BorderLineStyle.THIN);//ȡ�����б߿�,������
            noButtonFormat.setFont(dateFont);//��������
            noButtonFormat.setWrap(true);//�Զ�����
            
            
            noTopRightFormat = new WritableCellFormat();//ȡ��Ԫ��
            noTopRightFormat.setAlignment(jxl.format.Alignment.CENTRE);//ˮƽ����
            noTopRightFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//��ֱ����
            noTopRightFormat.setBorder(jxl.format.Border.LEFT,
                                     jxl.format.BorderLineStyle.THIN);//ȡ�����б߿�,������
            noTopRightFormat.setBorder(jxl.format.Border.BOTTOM,
                    jxl.format.BorderLineStyle.THIN);//ȡ�����б߿�,������
            noTopRightFormat.setFont(dateFont);//��������
            noTopRightFormat.setWrap(true);//�Զ�����
            
            noTopLeftFormat = new WritableCellFormat();//ȡ��Ԫ��
            noTopLeftFormat.setAlignment(jxl.format.Alignment.CENTRE);//ˮƽ����
            noTopLeftFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//��ֱ����          
            noTopLeftFormat.setBorder(jxl.format.Border.BOTTOM,
                    jxl.format.BorderLineStyle.THIN);//ȡ�����б߿�,������
            noTopLeftFormat.setBorder(jxl.format.Border.RIGHT,
                    jxl.format.BorderLineStyle.THIN);//ȡ�����б߿�,������
            noTopLeftFormat.setFont(dateFont);//��������
            noTopLeftFormat.setWrap(true);//�Զ�����
            // public WritableCellFormat noFormat = null;
            noFormat = new WritableCellFormat();//ȡ��Ԫ��
            noFormat.setAlignment(jxl.format.Alignment.LEFT);//ˮƽ����
            noFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//��ֱ����
            noFormat.setBorder(jxl.format.Border.NONE,
                                     jxl.format.BorderLineStyle.THIN);//ȡ�����б߿�,������
            noFormat.setFont(dateFont14);//��������
            noFormat.setWrap(true);//�Զ�����
            
            
         // public WritableCellFormat noFormat = null;
            alignmentRightFormat = new WritableCellFormat();//ȡ��Ԫ��
            alignmentRightFormat.setAlignment(jxl.format.Alignment.RIGHT);//ˮƽ����
            alignmentRightFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//��ֱ����
            alignmentRightFormat.setBorder(jxl.format.Border.NONE,
                                     jxl.format.BorderLineStyle.THIN);//ȡ�����б߿�,������
            alignmentRightFormat.setFont(dateFont14);//��������
            alignmentRightFormat.setWrap(true);//�Զ�����
            
            
            // public WritableCellFormat noFormat = null;
               alignmentRightFormat1 = new WritableCellFormat();//ȡ��Ԫ��
               alignmentRightFormat1.setAlignment(jxl.format.Alignment.RIGHT);//ˮƽ����
               alignmentRightFormat1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//��ֱ����
               alignmentRightFormat1.setBorder(jxl.format.Border.NONE,
                                        jxl.format.BorderLineStyle.THIN);//ȡ�����б߿�,������
               alignmentRightFormat1.setFont(dateFont);//��������
               alignmentRightFormat1.setWrap(true);//�Զ�����
            //�趨���ݸ�ʽ
            dateCellFormat = new WritableCellFormat();//ȡ��Ԫ��
            dateCellFormat.setAlignment(jxl.format.Alignment.CENTRE);//ˮƽ����
            dateCellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//��ֱ����
            dateCellFormat.setBorder(jxl.format.Border.ALL,
                                     jxl.format.BorderLineStyle.THIN);//ȡ�����б߿�,������
            dateCellFormat.setFont(dateFont);//��������
            dateCellFormat.setWrap(true);//�Զ�����
            //�̲�Ƶ�׼��ĸ���ʽ 
            //public WritableCellFormat topLeftFormat = null;
            //public WritableCellFormat topRightFormat = null;
            //public WritableCellFormat leftButtomFormat = null;
            //public WritableCellFormat buttomRightFormat = null;
            topLeftFormat = new WritableCellFormat();//ȡ��Ԫ��
            topLeftFormat.setAlignment(jxl.format.Alignment.CENTRE);//ˮƽ����
            topLeftFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//��ֱ����
            topLeftFormat.setBorder(jxl.format.Border.TOP,
                                     jxl.format.BorderLineStyle.THIN);//ȡ�����б߿�,������
            topLeftFormat.setBorder(jxl.format.Border.LEFT,
                    jxl.format.BorderLineStyle.THIN);//ȡ�����б߿�,������
            topLeftFormat.setFont(dateFont);//��������
            topLeftFormat.setWrap(true);//�Զ�����
            
            topRightFormat = new WritableCellFormat();//ȡ��Ԫ��
            topRightFormat.setAlignment(jxl.format.Alignment.CENTRE);//ˮƽ����
            topRightFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//��ֱ����
            topRightFormat.setBorder(jxl.format.Border.TOP,
                                     jxl.format.BorderLineStyle.THIN);//ȡ�����б߿�,������    
            topRightFormat.setBorder(jxl.format.Border.RIGHT,
                    jxl.format.BorderLineStyle.THIN);//ȡ�����б߿�,������  
            topRightFormat.setFont(dateFont);//��������
            topRightFormat.setWrap(true);//�Զ�����
            
            
            leftButtomFormat = new WritableCellFormat();//ȡ��Ԫ��
            leftButtomFormat.setAlignment(jxl.format.Alignment.CENTRE);//ˮƽ����
            leftButtomFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//��ֱ����
            leftButtomFormat.setBorder(jxl.format.Border.LEFT,
                                     jxl.format.BorderLineStyle.THIN);//ȡ�����б߿�,������
            leftButtomFormat.setBorder(jxl.format.Border.BOTTOM,
                    jxl.format.BorderLineStyle.THIN);//ȡ�����б߿�,������
            leftButtomFormat.setFont(dateFont);//��������
            leftButtomFormat.setWrap(true);//�Զ�����
            
            
            buttomRightFormat = new WritableCellFormat();//ȡ��Ԫ��
            buttomRightFormat.setAlignment(jxl.format.Alignment.CENTRE);//ˮƽ����
            buttomRightFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//��ֱ����
            buttomRightFormat.setBorder(jxl.format.Border.BOTTOM,
                                     jxl.format.BorderLineStyle.THIN);//ȡ�����б߿�,������   
            buttomRightFormat.setBorder(jxl.format.Border.RIGHT,
                    jxl.format.BorderLineStyle.THIN);//ȡ�����б߿�,������     
            buttomRightFormat.setFont(dateFont);//��������
            buttomRightFormat.setWrap(true);//�Զ�����
            
            
            dateTITLEFormat = new WritableCellFormat();
            dateTITLEFormat.setAlignment(jxl.format.Alignment.CENTRE);
            dateTITLEFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
            dateTITLEFormat.setBorder(jxl.format.Border.ALL,
            		jxl.format.BorderLineStyle.THIN);
            dateTITLEFormat.setFont(datetitleFont);
            dateTITLEFormat.setWrap(true);
//            Colour c= new Colour();
       //     dateTITLEFormat.setBackground(Colour.LIGHT_GREEN);
            
            dateTITLELEFTFormat=new WritableCellFormat();
            dateTITLELEFTFormat.setAlignment(jxl.format.Alignment.LEFT);
            dateTITLELEFTFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
            dateTITLELEFTFormat.setBorder(jxl.format.Border.ALL,
            		jxl.format.BorderLineStyle.THIN);
            dateTITLELEFTFormat.setFont(datetitleFont);
            dateTITLELEFTFormat.setWrap(true);
            
            
            dateTITLERIGHTFormat=new WritableCellFormat();
            dateTITLERIGHTFormat.setAlignment(jxl.format.Alignment.RIGHT);
            dateTITLERIGHTFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
            dateTITLERIGHTFormat.setBorder(jxl.format.Border.ALL,
            		jxl.format.BorderLineStyle.THIN);
            dateTITLERIGHTFormat.setFont(datetitleFont);
            dateTITLERIGHTFormat.setWrap(true);
            
            
            
             //Colour c= new Colour();
          //  dateTITLEFormat.setBackground(Colour.LIGHT_GREEN);
            
            
            dateCellFormat1 = new WritableCellFormat();
            dateCellFormat1.setAlignment(jxl.format.Alignment.JUSTIFY);
            dateCellFormat1.setVerticalAlignment(jxl.format.VerticalAlignment.JUSTIFY);
            dateCellFormat1.setBorder(jxl.format.Border.ALL,
                                     jxl.format.BorderLineStyle.THIN);
            dateCellFormat1.setFont(dateFont);
            dateCellFormat1.setWrap(true);

//            dateCellFormat.setShrinkToFit(true);//��Ԫ���е�����ȥ��Ӧ��Ԫ���С
            //�趨��ǩ��ʽ
            titleCellFormat = new WritableCellFormat();
            titleCellFormat.setAlignment(jxl.format.Alignment.CENTRE);
            titleCellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
            titleCellFormat.setWrap(false);
            titleCellFormat.setFont(titleFont);
            
            //
            repTitleFormat = new WritableCellFormat();
            repTitleFormat.setAlignment(jxl.format.Alignment.CENTRE);
            repTitleFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
            repTitleFormat.setWrap(false);
            repTitleFormat.setFont(repTitleFont);
            repTitleFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.NONE);
            //�趨���ݸ�ʽ
            dateCellCountFormat = new WritableCellFormat();
            dateCellCountFormat.setAlignment(jxl.format.Alignment.LEFT);
            dateCellCountFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
            dateCellCountFormat.setWrap(false);
            dateCellCountFormat.setBorder(jxl.format.Border.ALL,
                                          jxl.format.BorderLineStyle.THIN);
            dateCellCountFormat.setFont(dateFont);

            titleCellnoBorderFormat = new WritableCellFormat();
            titleCellnoBorderFormat.setAlignment(jxl.format.Alignment.LEFT);
            titleCellnoBorderFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
            titleCellnoBorderFormat.setWrap(false);
            titleCellnoBorderFormat.setFont(dateFont);
            
            SUBtitleCellFormat = new WritableCellFormat();
            SUBtitleCellFormat.setAlignment(jxl.format.Alignment.RIGHT);
            SUBtitleCellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
//            SUBtitleCellFormat.setBorder(jxl.format.Border.ALL,
//                    jxl.format.BorderLineStyle.THIN);
            SUBtitleCellFormat.setWrap(false);
            SUBtitleCellFormat.setFont(subtitleFont);
            
        } catch (WriteException ex) {
            System.err.println(ex);
        }
            if (!(new File(System.getProperty("user.dir") + "/exportedExcel/").
                  isDirectory())) {
                new File(System.getProperty("user.dir") + "/exportedExcel/").
                        mkdir();
            }


    }
    public JExcel(WritableCellFormat CellFormat){
    	new JExcel();
    	this.Format=CellFormat;
    }
    private Workbook templateWorkBook = null;
    private WritableWorkbook instanceWorkBook = null;
    private WritableSheet workSheet = null;
    private WritableFont titleFont = null;
    private WritableFont repTitleFont = null;
    private WritableFont subtitleFont = null;
    private WritableFont datetitleFont = null;
    private WritableFont dateFont = null;
    private WritableFont dateFont14 = null;
    public WritableCellFormat titleCellFormat = null;
    public WritableCellFormat repTitleFormat = null;
    public WritableCellFormat SUBtitleCellFormat = null;
    //Ϊ�̲�Ƶ�׼��ĸ���ʽ
    public WritableCellFormat topLeftFormat = null;
    public WritableCellFormat topRightFormat = null;
    public WritableCellFormat leftButtomFormat = null;
    public WritableCellFormat buttomRightFormat = null;
    
    public WritableCellFormat noButtonFormat = null;
    public WritableCellFormat noTopRightFormat = null;
    public WritableCellFormat noTopLeftFormat = null;
    
    
    
    public WritableCellFormat Format = null;
    public WritableCellFormat dateCellFormat = null;
    public WritableCellFormat noFormat = null;
    public WritableCellFormat alignmentRightFormat = null;
    public WritableCellFormat alignmentRightFormat1 = null;
    public WritableCellFormat dateCellFormat1 = null;
    public WritableCellFormat dateTITLEFormat = null;
    public WritableCellFormat dateTITLELEFTFormat=null;
    public WritableCellFormat dateTITLERIGHTFormat=null;
    public WritableCellFormat dateCellCountFormat = null;
    public WritableCellFormat titleCellnoBorderFormat = null;
    public final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";//ʱ���ʽ
    private String fontType = "����";
    private SheetSettings setting =null;

    private String station = ""; //�ܾ�����
    private String reportDate = ""; //1977-01-01
    private String sheetName1 = "sheet1";
    private String sheetName2 = "sheet2";
    private String sheetName3 = "sheet3";
    private String sheetName4 = "sheet4";
    private String sheetName5 = "sheet5";

    private String filename;
    private String imageUrl;
    private String templateid="";
    private int LimitRowCount=65535;
    private String absoluteFilePath;

    /**
     * �趨ģ��EXCEL�ļ�����
     * @param templateWorkBookName String ģ���ļ�����
     * @throws Exception
     */
    private void setTemplateWorkBook(String templateWorkBookName) throws
            Exception {
        templateWorkBook = Workbook.getWorkbook(new File(templateWorkBookName));
    }


    /**
     * ��һ���ļ��ĸ���������ָ������д�ص�ʵ���ļ�
     */
    public String openDocument() throws Exception {
    	filename=System.currentTimeMillis()+"";
    	absoluteFilePath = System.getProperty("user.dir") + "/exportedExcel/"+ filename + ".xls";
        //ģ��ָ��
        if (! (templateWorkBook == null)) {
          instanceWorkBook = Workbook.createWorkbook(new File(absoluteFilePath),
                                                     templateWorkBook);
          //��ȡһ��������
          workSheet = instanceWorkBook.getSheet(4);
          workSheet = instanceWorkBook.getSheet(3);
          workSheet = instanceWorkBook.getSheet(2);
          workSheet = instanceWorkBook.getSheet(1);
          workSheet = instanceWorkBook.getSheet(0);
        }
        else {
          instanceWorkBook = Workbook.createWorkbook(new File(absoluteFilePath));
          workSheet = instanceWorkBook.createSheet(sheetName5, 4);
          workSheet = instanceWorkBook.createSheet(sheetName4, 3);
          workSheet = instanceWorkBook.createSheet(sheetName3, 2);
          workSheet = instanceWorkBook.createSheet(sheetName2, 1);
          workSheet = instanceWorkBook.createSheet(sheetName1, 0);
        }
      
        WorkBookGetSheet(0);
        return absoluteFilePath;
    }

    public void WorkBookGetSheet(int sheet) throws Exception{
        //��ȡһ�������� sheet
//      workSheet=instanceWorkBook.createSheet("Sheet1",0);
      workSheet = instanceWorkBook.getSheet(sheet);
      setting = workSheet.getSettings();
    }
    /**
     * ��EXCEL�ļ����TITLE����
     * @throws Exception
     */
    private void addTitleDate() throws Exception {
//        workSheet.addCell(new Label(0, 18, station, titleCellFormat));
//        workSheet.addCell(new Label(7, 19, reportDate, titleCellFormat));
    }

    /**
     *  ��EXCEL�ļ��е�ĳһ��Ԫ���������
     *  String ���� �������
     * @throws Exception
     */
    public void addDate(int col, int row, String data ,WritableCellFormat dateCellFormat) throws Exception {
    	int TrueRow=row-(row/LimitRowCount*LimitRowCount);
    	try {
    		if(TrueRow==0&&col==0&&row/LimitRowCount>0)
    			WorkBookGetSheet(Integer.parseInt(workSheet.getName().substring(workSheet.getName().length()-1, workSheet.getName().length())));
    		if(data.startsWith("-")){
    			if(StringTool.isNumeric(data.substring(1, data.length())))
    				workSheet.addCell(new jxl.write.Number(col, TrueRow, Double.parseDouble(data), dateCellFormat));
    			else
    				workSheet.addCell(new Label(col, TrueRow, data, dateCellFormat));
    		}else{
    			if(StringTool.isNumeric(data))
    				workSheet.addCell(new jxl.write.Number(col, TrueRow, Double.parseDouble(data), dateCellFormat));
    			else
    				workSheet.addCell(new Label(col, TrueRow, data, dateCellFormat));
    		}
		} catch (Exception e) {
			workSheet.addCell(new Label(col, TrueRow, data, dateCellFormat));
		}
    }
    /**
     *  ��EXCEL�ļ��е�ĳһ��Ԫ���������(�绰����,���֤�ţ��ʱ�ȵ�)
     *  String ���� �������
     * @throws Exception
     */
    public void addDate(int col, int row, String data) throws Exception {
    	int TrueRow=row-(row/LimitRowCount*LimitRowCount);
    	try {
    		if(TrueRow==0&&col==0&&row/LimitRowCount>0)
    			WorkBookGetSheet(Integer.parseInt(workSheet.getName().substring(workSheet.getName().length()-1, workSheet.getName().length())));
    		if(data.startsWith("-")){
    			if(StringTool.isNumeric(data.substring(1, data.length())))
    				workSheet.addCell(new jxl.write.Number(col, TrueRow, Double.parseDouble(data), dateCellFormat));
    			else
    				workSheet.addCell(new Label(col, TrueRow, data, dateCellFormat));
    		}else{
    				workSheet.addCell(new Label(col, TrueRow, data, dateCellFormat));
    		}
		} catch (Exception e) {
			workSheet.addCell(new Label(col, TrueRow, data, dateCellFormat));
		}
    }
    /**
     *  ��EXCEL�ļ��е�ĳһ��Ԫ���������
     *  double ���� �������
     * @throws Exception
     */
    public void addDate(int col, int row, double data) throws Exception {
    	addDate(col, row, ""+data, dateCellFormat);
//        workSheet.addCell(new jxl.write.Number(col, row, data, dateCellFormat));
    }
    /**
     * ��Ԫ��������������� 
     * @ add by wangfuxiang
     * @date 2010/12/21
     * @param col
     * @param row
     * @param data
     * @throws RowsExceededException
     * @throws WriteException
     * @throws ParseException 
     */
    public void addDateTime(int col, int row, String data) throws RowsExceededException, WriteException, ParseException{
    	SimpleDateFormat d = new SimpleDateFormat(DATE_FORMAT);
    	Date date1=null;
    	if(!data.equalsIgnoreCase("")){
    		 date1 = d.parse(data);
    	}else{
    		date1=new Date();
    	}
        
    	jxl.write.DateFormat df = new jxl.write.DateFormat("yyyy-MM-dd HH:mm:ss");
    	jxl.write.WritableCellFormat wcf = new jxl.write.WritableCellFormat(df);
    	wcf.setAlignment(jxl.format.Alignment.CENTRE);
    	wcf.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
    	wcf.setFont(dateFont);
    	wcf.setBorder(jxl.format.Border.ALL,
                 jxl.format.BorderLineStyle.THIN);//ȡ�����б߿�,������
    	jxl.write.DateTime jwdt = new jxl.write.DateTime(col,row,date1,wcf);
    	workSheet.addCell(jwdt);
    }
    /**
     * �ϲ���Ԫ��
     * @param col int
     * @param row int
     * @param int1 int
     * @param int2 int
     * @throws Exception
     */
    public void mergeCells(int col, int row, int int1, int int2) throws
            Exception {
        workSheet.mergeCells(col, row, int1, int2);
    }

    /**
     * ���ͼƬ����֧��PNG
     * @param col int ͼƬ������
     * @param row int ͼƬ������
     * @param width int ͼƬռ�ݵ�����
     * @param height int ͼƬռ�ݵ�����
     * @param imgName String ͼƬ����
     * @throws Exception
     */
    public void addImg(double col, double row, double width, double height, String imgName) throws
            Exception {
        File imgFile = new File(imgName);
        WritableImage img = new WritableImage(col, row, width, height, imgFile);
        workSheet.addImage(img);
    }

    /**
     * ���ͼƬ����֧��PNG
     * @param imgName String ͼƬ����
     * @throws Exception
     */
    public void addImg(String imgName) throws Exception {
        if (!(imgName == null || imgName.equals(""))) {
            addImg(1, 1, 7, 17, imgName);
        }
    }


    /**
     * �����ĵ�
     * @throws Exception
     */
    public void saveDocument() throws Exception {
        instanceWorkBook.write();
        instanceWorkBook.close();
//        System.out.println("done");
    }

    /**
     * �����ڵ������ߵ��Ӳ�����̬�ձ����������߹㲥������̬�ձ���
     */
    public void formatSheet(){
//        workSheet.setColumnView(1,10);
//        workSheet.setColumnView(2,10);
//        workSheet.setColumnView(3,18);
//        workSheet.setColumnView(4,12);
//        workSheet.setColumnView(5,10);
//        workSheet.setColumnView(6,11);
//        workSheet.setColumnView(7,9);
//        workSheet.setColumnView(8,16);
//        workSheet.setColumnView(9,10);
    }
    /**
     * ����ҳ���ӡ��ʽ��Ϊ����
     *
     */
    public void PageLandscape (){
        setting.setOrientation(jxl.format.PageOrientation.LANDSCAPE);
    }
    /**
     * ����ҳ���ӡ��ʽ��Ϊ����
     *
     */
    public void PagePortrait (){
        setting.setOrientation(jxl.format.PageOrientation.PORTRAIT);
    }
    /**
     * ����ҳ���ӡ���ű���
     * @param scale Ϊ����ֵ
     */
    public void PageScaleFactor(int scale) {
    	setting.setScaleFactor(scale);
	}
    /**
     * ����ҳ���ӡ��ҳ�߾�
     * ע��Ĳ���������Ӣ�磬�����õĻ�дΪnull
     * @param bottom	�±߾�
     * @param top		�ϱ߾�
     * @param left		��߾�
     * @param right		�ұ߾�
     */
    public void PageMargin(Object bottom,Object top,Object left,Object right) {
    	if (bottom != null) {
			setting.setBottomMargin(Double.valueOf(bottom.toString()));
		}
		if (top != null) {
			setting.setTopMargin(Double.valueOf(top.toString()));
		}
		if (left != null) {
			setting.setLeftMargin(Double.valueOf(left.toString()));
		}
		if (right != null) {
			setting.setRightMargin(Double.valueOf(right.toString()));
		}
	}
    /**
     * ��������ҳ��
     * @author liqing
     */
    public void RowPageBreak(int rownum){
    	workSheet.addRowPageBreak(rownum);
    }
    /**
     * 
     * ��������ҳ�� ����
     * @author liqing
     * @deprecated
     */
    public void ColumnPageBreak(int rownum){
    	
    }
//    public void PrintExcel() throws Exception {
//        setTemplateWorkBook("c:/template"+templateid+".xls");
//        openDocument();
//        setStationName("" + ExcelName);
//        setReportDate("");
//        addImg(imageUrl);

//        Util util = new Util();
//        util.getConnection("anbo", "anbo", "10.1.6.19", "orcl19");
//        String sql = BuildSQL.getSqlStr(ExcelName);
//
//        util.exeQuery(sql);

//        addTitleDate();
//        addSheetDate(util.getResultSet());
//        util.closeConncetion();


//    }

    public String getFilename() {
        return filename;
    }

    /**
     * �趨�����������
     * @param stationName String �����������
     */
    public void setStationName(String stationName) {
        station = stationName;
    }

    /**
     * �趨��������
     * @param stationName String ��������
     */
    public void setReportDate(String date) {
        reportDate = date;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setTemplateid(String templateid) {
		this.templateid = templateid;
	}

	public WritableSheet getWorkSheet() {
		return workSheet;
	}


	public String getSheetName1() {
		return sheetName1;
	}


	public void setSheetName1(String sheetName1) {
		this.sheetName1 = sheetName1;
	}


	public String getSheetName2() {
		return sheetName2;
	}


	public void setSheetName2(String sheetName2) {
		this.sheetName2 = sheetName2;
	}


	public String getSheetName3() {
		return sheetName3;
	}


	public void setSheetName3(String sheetName3) {
		this.sheetName3 = sheetName3;
	}


	public String getSheetName4() {
		return sheetName4;
	}


	public void setSheetName4(String sheetName4) {
		this.sheetName4 = sheetName4;
	}


	public String getSheetName5() {
		return sheetName5;
	}


	public void setSheetName5(String sheetName5) {
		this.sheetName5 = sheetName5;
	}


	public WritableCellFormat getFormat() {
		return Format;
	}
	

	public String getAbsoluteFilePath() {
		return absoluteFilePath;
	}
	public void setFormat(WritableCellFormat format) {
		try {
			Format = new WritableCellFormat();
			if(format.getAlignment()!=null)
				Format.setAlignment(format.getAlignment());
			if(format.getBackgroundColour()!=null)Format.setBackground(format.getBackgroundColour());
			if(format.getBorder(jxl.format.Border.ALL)!=null)Format.setBorder(jxl.format.Border.ALL,
            		format.getBorder(jxl.format.Border.ALL));
			if(format.getBorder(jxl.format.Border.LEFT)!=null)Format.setBorder(jxl.format.Border.LEFT,
					format.getBorder(jxl.format.Border.LEFT));
			if(format.getBorder(jxl.format.Border.RIGHT)!=null)Format.setBorder(jxl.format.Border.RIGHT,
					format.getBorder(jxl.format.Border.RIGHT));
			if(format.getBorder(jxl.format.Border.TOP)!=null)Format.setBorder(jxl.format.Border.TOP,
					format.getBorder(jxl.format.Border.TOP));
			if(format.getBorder(jxl.format.Border.BOTTOM)!=null)Format.setBorder(jxl.format.Border.BOTTOM,
					format.getBorder(jxl.format.Border.BOTTOM));
			if(format.getBorder(jxl.format.Border.NONE)!=null)Format.setBorder(jxl.format.Border.NONE,
					format.getBorder(jxl.format.Border.NONE));
			Format.setWrap(format.getWrap());
			if(format.getVerticalAlignment()!=null)Format.setVerticalAlignment(format.getVerticalAlignment());
//			if(format.getFont()!=null)Format.setFont((WritableFont)format.getFont());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * col:��
	 * row:��
	 * excelTitle:��ͷ����
	 * <p>class/function:com.viewscenes.printexcel
	 * <p>explain:
	 * <p>author-date:̷��ΰ 2012-12-6
	 * @param:
	 * @return:
	 * @throws Exception 
	 */
	public void setTitle(int col,int row,ExcelTitle[] excelTitle) throws Exception{
		
		 
		boolean hasSubTitle = false;	//Ĭ��û�ж�����ͷ
		
		if (excelTitle == null)
			throw new Exception("EXCEL ��ͷΪ"+excelTitle);
		//����Ƿ����������ͷ
		for(int i=0;i<excelTitle.length;i++){
			ExcelTitle tmpEt = excelTitle[i];
			int colCount = tmpEt.getSubTitles().length;//������ͷ����
			if (colCount >0)
				hasSubTitle = true;
		}
		
		
		//��
		for(int j=1;j<=excelTitle.length;j++){
			
			ExcelTitle tmpEt = excelTitle[j-1];
			int colCount = tmpEt.getSubTitles().length;//������ͷ����
			
			//�����и�
			workSheet.setRowView(row, tmpEt.getHeight());
			if (colCount>0){
				
				hasSubTitle = true;
				
				addDate(col+j-1,row,tmpEt.getName(),tmpEt.getTitleCellFormat());
				
				mergeCells(col+j-1,row,(col+j-1)+colCount-1,row);
				
				
				for(int sub=0;sub<colCount;sub++){
					ExcelTitle subEt = tmpEt.getSubTitles()[sub];
					
					//��ǰ������
					int curcol = (j-1)+sub;
					
					//���ж�����ͷ�Ŀ�ʼλ�ü������һ��λ��
					addDate(curcol,row+1,subEt.getName(),subEt.getTitleCellFormat());
					
					
					//���úϲ���Ԫ��Ŀ����
//					int w=0;
//					int h=0;
//					if (subEt.getHeight()==0)
//						if (tmpEt.getHeight()==0)
//							h = workSheet.getRowView(row).getSize();
//						else
//							h = tmpEt.getHeight();
//					else
//						h = subEt.getHeight();
//					
//					if (subEt.getWidth()==0)
//						if (tmpEt.getWidth()==0)
//							w = workSheet.getColumnView(col+j-1).getSize();
//						else
//							w = tmpEt.getWidth()/colCount;
//					else
//						w = subEt.getWidth();
//					
					workSheet.setRowView(row+1, tmpEt.getHeight());
					workSheet.setColumnView(curcol,tmpEt.getWidth()/colCount);
				}
				//�ϲ���ɶ�����ͷ����һ�����ڵ�λ��
				col = col + colCount-1;
			}else{
				//��ǰ������
				int curcol = col+j-1;
				addDate(curcol,row,tmpEt.getName(),tmpEt.getTitleCellFormat());
				//�ж�����ͷ�����ºϲ�
				if (hasSubTitle)
					mergeCells(curcol,row,curcol,row+1);
				
				//����һ����Ԫ��Ŀ��
				workSheet.setColumnView(curcol, tmpEt.getWidth()==0?workSheet.getColumnView(curcol).getSize():tmpEt.getWidth());
				workSheet.setRowView(row, tmpEt.getHeight());
			}
			
		}
		
	}
	
	/**
	 * ���EXCEL�ļ�
	 * <p>class/function:com.viewscenes.printexcel
	 * <p>explain:
	 * <p>author-date:̷��ΰ 2012-12-10
	 * @param:
	 * @return:
	 */
	public void postExcel(HttpServletRequest request,HttpServletResponse response) throws IOException{
		response.setContentType(JExcel.TYPE);
		response.setHeader("Location", "Export.xls");
		response.setHeader("Expires", "0");
		response.setHeader("Content-Disposition", "attachment;filename="+java.net.URLEncoder.encode(getFilename(),"UTF-8")+".xls");

		OutputStream outputStream = response.getOutputStream();
		InputStream inputStream = new FileInputStream(absoluteFilePath);
		byte[] buffer = new byte[1024];
		int i=-1;
		while ( (i=inputStream.read(buffer)) != -1){
			outputStream.write(buffer,0,i);
		}
		outputStream.flush();
		outputStream.close();
		outputStream = null;
		inputStream.close();
	}

}
