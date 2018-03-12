package com.viewscenes.web.statistics;

import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.jdom.Element;



import com.viewscenes.bean.ReportBean;
import com.viewscenes.bean.zrst_rep_quality_tab;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.printexcel.JExcel;
import com.viewscenes.pub.GDSet;
import com.viewscenes.pub.GDSetException;
import com.viewscenes.util.StringTool;
import com.viewscenes.web.Daoutil.ReportUtil;

import flex.messaging.io.amf.ASObject;

public class QualityDataStatistics {

	/**************************************************
	
	 * @Title: main
	
	 * @Description: TODO(指标数据统计功能)
	
	 * @param @param args    设定文件
	
	 * @author  刘斌
	
	 * @return void    返回类型
	
	 * @throws
	
	 *************************************************/

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws SecurityException 
	 * @throws GDSetException 
	 * @throws DbException 
	 * ************************************************
	
	* @Title: doReport
	
	* @Description: TODO(指标数据统计)
	
	* @param @param starttime
	* @param @param endtime
	* @param @param type 
	* @param @param report_type
	* @param @return    设定文件
	
	* @author  刘斌
	
	* @return ArrayList    返回类型
	
	* @throws
	
	************************************************
	 */
	 public static boolean doReport(String starttime, String endtime,String report_type,String user_name,ASObject queryParam) throws DbException, GDSetException, SecurityException, InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
		 	String headnames=(String)queryParam.get("headnames");
		 	
		 	String freq=(String)queryParam.get("freq");
		 	String code=(String) queryParam.get("code");
		 	String part="";
		 	if(freq!=null&&freq.length()!=0){
		 		part+=" and str.frequency='"+freq+"' ";
		 	}
//		 	if(headnames!=null&&headnames.length()>2){
//		 		part+=" and decode(head.type_id||head.version, '102V8', substr(head.shortname, 0, length(head.shortname)-1),head.shortname) in ('"+headnames.replace(",", "','")+"') ";
//		 	}
		 	if(code!=null && !code.equalsIgnoreCase("")){
		 		if(code.indexOf(",")>0){
					String[] s=code.split(",");
					StringBuffer temp = new StringBuffer("(");
					for(int i=0;i<s.length;i++){
						temp.append(" head.code like '%"+s[i]+"%' or");
					}
					part+=" and "+temp.toString().substring(0, temp.toString().length()-2)+")";
				}else{
					part+=" and head.code like '%"+code+"%'";
				}
		 	}
	       String totalsql="select str.band, str.frequency, lan.language_name, run.station_name,  head.shortname," +
	       		" run.start_time || '-' || run.end_time play_time, to_char(str.start_datetime,'yyyy-mm-dd') start_datetime," +
	       		"  median(to_number(str.level_value)) level_value,   median(to_number(str.fm_modulation)) fm_modulation," +
	       		" median(to_number(str.am_modulation)) am_modulation,   median(to_number(str.offset)) offset" +
	       		" from radio_stream_result_tab str, zres_runplan_chaifen_tab run,  res_headend_tab  head,  zdic_language_tab lan" +
	       		" where str.runplan_id = run.runplan_id and run.language_id = lan.language_id and str.head_id = head.head_id" +
				" and str.is_delete=0 " +
	       		" and str.start_datetime >= to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss') and str.start_datetime <= to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss')" +part+
	       		" group by to_char(str.start_datetime,'yyyy-mm-dd'),str.band,str.frequency,start_time,end_time,lan.language_name,run.station_name, head.shortname  order by to_char(str.start_datetime,'yyyy-mm-dd') ,shortname,frequency ,play_time,language_name,station_name";	        
	        ArrayList descList=new ArrayList();
	       GDSet gd = DbComponent.Query(totalsql);
	       String report_id=ReportUtil.getReportId();
	       int rowCount=0;
	     
	       for(int i=0;i<gd.getRowCount();i++)
	       {
	    	   zrst_rep_quality_tab flagbean= new zrst_rep_quality_tab();
	    	      if(gd.getString(i, "band").equals("调频"))
	    	      {
	    	    	  flagbean.setData_fmoram(gd.getString(i, "fm_modulation")); 
	    	      }else
	    	      {
	    	    	  flagbean.setData_fmoram(gd.getString(i, "am_modulation"));  
	    	      }
	    		   
	    		   flagbean.setData_level(gd.getString(i, "level_value"));
	    		   flagbean.setData_offset(gd.getString(i, "offset"));
	    		   flagbean.setData_time(gd.getString(i, "start_datetime"));
	    	       flagbean.setFrequency(gd.getString(i, "frequency"));
	    		   flagbean.setHead_name(gd.getString(i, "shortname"));
	    		   flagbean.setLanguage(gd.getString(i, "language_name"));
	    		   flagbean.setPlay_time(gd.getString(i, "play_time"));
	    		   flagbean.setStation_name(gd.getString(i, "station_name"));
	    		   flagbean.setRemark("");
	    		   flagbean.setReport_id(report_id);
	    
	    		   descList.add(flagbean);
	    		           
	    		       
	    	   
	       }
	   
	       
	       ReportBean reportBean =new ReportBean();	
	       reportBean.setReport_id(report_id);
	       reportBean.setReport_date(StringTool.Date2String(new Date()));
           reportBean.setReport_type(report_type);
           reportBean.setPeriod_type("");
           reportBean.setStart_datetime(starttime);
           reportBean.setEnd_datetime(endtime);
           reportBean.setCharacter("");
           reportBean.getData_num();
          
           reportBean.setImport_status("1");
           reportBean.setImport_datetime(StringTool.Date2String(new Date()));
           reportBean.setImport_user(user_name);
           reportBean.setModify_status("1");
           reportBean.setModify_user("");
           reportBean.setModify_datetime("");

			reportBean.setVerify_status("");
			reportBean.setVerify_user("");
			reportBean.setVerify_datetime("");
            reportBean.setAuthentic_status("");
            reportBean.setAuthentic_user("");
            reportBean.setAuthentic_datetime("");
            reportBean.setRemark("");
     boolean boo=ReportUtil.insertReportByReportBean(reportBean,  descList, "zrst_rep_quality_tab", "com.viewscenes.bean.zrst_rep_quality_tab");
			
   return true;
}
	 /**
	  * ************************************************

	 * @Title: doExcel

	 * @Description: TODO(导出遥控站指标测量报表)

	 * @param @param msg
	 * @param @param request
	 * @param @param response
	 * @param @throws Exception    设定文件

	 * @author  刘斌

	 * @return void    返回类型

	 * @throws

	 ************************************************
	  */
	 	 public void doExcel(String msg,HttpServletRequest request,HttpServletResponse response) throws Exception{
	 			Element root = StringTool.getXMLRoot(msg);
	 			String reportId = root.getChildText("reportId");
	 			String starttime = root.getChildText("starttime");
	 			String endtime = root.getChildText("endtime");

	 			String fileName="遥控站指标测量数据统计表";
	 			if(starttime.equals(endtime)){
	 				fileName = "遥控站指标测量数据"+starttime+"日统计表";
	 			} else 
	 			{
	 				fileName = "遥控站指标测量数据("+starttime.substring(0, 11)+"到"+endtime.substring(0, 11)+")统计表";
	 			} 
	 			String descSql="select * from zrst_rep_quality_tab t  where report_id="+reportId+"   order by head_name,t.frequency,t.station_name,t.language,t.data_time,play_time asc   ";
	 			GDSet gdSet=DbComponent.Query(descSql);
	 			String classpath = zrst_rep_quality_tab.class.getName();
	 		    ArrayList list = StringTool.converGDSetToBeanList(gdSet, classpath);
	 			JExcel jExcel = new JExcel();
	 			OutputStream outputStream = response.getOutputStream();
	 			response.setContentType("application/vnd.ms-excel");
	 			response.reset();
	 			response.setHeader("Location", "Export.xls");
	 			response.setHeader("Expires", "0");
	             response.setHeader("Content-disposition", "attachment; filename="+new String((fileName).getBytes("GBK"),"ISO-8859-1")+".xls");
	 	        
	 			WritableWorkbook wwb =Workbook.createWorkbook(outputStream);
	 			int sheetNum = 0;
	 			WritableSheet ws =  null;
	 			WritableSheet ws2 = null;
	 			ws = wwb.createSheet("单站点指标测量数据统计表1", wwb.getNumberOfSheets()+1);//表一
	 		
	 		    ws2=wwb.createSheet("多站点电平中值统计表2", wwb.getNumberOfSheets()+2);//表二
	 		    WritableFont wf = new WritableFont(WritableFont.createFont("黑体"), 10,WritableFont.BOLD, false);
	 			WritableCellFormat wcfF = new WritableCellFormat(wf);
	 		    wcfF.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);//带边框 
	             wcfF.setWrap(true);//自动换行
	              // 把水平对齐方式指定为左对齐
	              wcfF.setAlignment(jxl.format.Alignment.CENTRE);
	              // 把垂直对齐方式指定为居中对齐
	              wcfF.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	              //设置列名

	              ws.addCell(new Label(0,1,"日期",wcfF));
	              ws.addCell(new Label(1,1,"频率(KHz)",wcfF));
	              ws.addCell(new Label(2,1,"播音时间",wcfF));
	              ws.addCell(new Label(3,1,"语言",wcfF));
	              ws.addCell(new Label(4,1,"发射台",wcfF));
	              ws.addCell(new Label(5,1,"収测遥控站",wcfF));
	              ws.addCell(new Label(6,1,"电平中值",wcfF));
	              ws.addCell(new Label(7,1,"调幅度或调制度",wcfF));
	              ws.addCell(new Label(8,1,"频偏",wcfF));
	        
	              ws.mergeCells(0,0,8,0);
	              
	              ws2.addCell(new Label(0,1,"日期",wcfF));
	              ws2.addCell(new Label(1,1,"频率(KHz)",wcfF));
	              ws2.addCell(new Label(2,1,"播音时间",wcfF));
	              ws2.addCell(new Label(3,1,"语言",wcfF));
	              ws2.addCell(new Label(4,1,"发射台",wcfF));
	              ws2.mergeCells(0,0,6,0);
	              wf = new WritableFont(WritableFont.createFont("黑体"), 12,WritableFont.BOLD, false);
	 				 wcfF = new WritableCellFormat(wf);
	 				 wcfF.setAlignment(jxl.format.Alignment.CENTRE);
	              // 把垂直对齐方式指定为居中对齐
	              wcfF.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	          
	              ws.addCell(new Label(0, 0, "单站点指标测量数据统计表1",wcfF));
	              ws2.addCell(new Label(0, 0, "多站点指标测量数据统计表2",wcfF));
	              ws.setRowView(0, 740);//设置第一行高度
	              ws.setRowView(1, 740);
	              ws2.setRowView(0, 740);//设置第一行高度
	              ws2.setRowView(1, 740);
	              ws.setColumnView(0, 20);
	              ws.setColumnView(1, 13);
	              ws.setColumnView(2, 20);
	              ws.setColumnView(3, 9);
	              ws.setColumnView(4, 13);
	              ws.setColumnView(5, 13);
	              ws.setColumnView(6, 10);
	              ws.setColumnView(7, 10);
	              ws.setColumnView(8, 10);
	              
	              ws2.setColumnView(0, 20);
	              ws2.setColumnView(1, 13);
	              ws2.setColumnView(2, 20);
	              ws2.setColumnView(3, 9);
	              ws2.setColumnView(4, 13);
	     
	          
	          	WritableFont wf1 = new WritableFont(WritableFont.createFont("宋体"), 10,WritableFont.NO_BOLD, false);
	             //设置CellFormat
	             jxl.write.WritableCellFormat wcfF2 = new jxl.write.WritableCellFormat(wf1);
	             // 把水平对齐方式指定为左对齐
	             wcfF2.setAlignment(jxl.format.Alignment.CENTRE);
	             // 把垂直对齐方式指定为居中对齐
	             wcfF2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	             wcfF2.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); 
                 int flag=0;
	             zrst_rep_quality_tab bean_flag=new zrst_rep_quality_tab();
	 			for(int i=0;i<list.size();i++){
	 				zrst_rep_quality_tab bean = (zrst_rep_quality_tab)list.get(i);
	 	         if(i==0)
	 	         {
	 	        	 bean_flag=bean;  
	 	        	int curRow = ws.getRows();
	 	        	ws.addCell(new Label(0,curRow, bean.getData_time(),wcfF2));
	 	        	ws.addCell(new Label(1,curRow, bean.getFrequency(),wcfF2));
	 	        	ws.addCell(new Label(2,curRow, bean.getPlay_time(),wcfF2));
	 	        	ws.addCell(new Label(3,curRow, bean.getLanguage(),wcfF2));
	 	        	ws.addCell(new Label(4,curRow, bean.getStation_name(),wcfF2));
	 	        	ws.addCell(new Label(5,curRow, bean.getHead_name(),wcfF2));
	 	        	ws.addCell(new Label(6,curRow, bean.getData_level(),wcfF2));
	 	        	ws.addCell(new Label(7,curRow, bean.getData_fmoram(),wcfF2));
	 	        	ws.addCell(new Label(8,curRow, bean.getData_offset(),wcfF2));
	 	        	continue;
	 	         }
	 	            if(bean_flag.getFrequency().equals(bean.getFrequency())
	 	            		&&bean_flag.getPlay_time().equals(bean.getPlay_time())
	 	            		&&bean_flag.getLanguage().equals(bean.getLanguage())
	 	            		&&bean_flag.getStation_name().equals(bean.getStation_name())
	 	            		&&bean_flag.getData_time().equals(bean.getData_time())
	 	            		)
	 	            {
	 	            	bean_flag.setHead_name(bean_flag.getHead_name()+bean.getHead_name());
	 	            	bean_flag.setData_level(bean_flag.getData_level()+bean.getData_level());
		 	        	
	 	            }else
	 	            {
	 	              	int curRow = ws2.getRows();
	 	              	
	 	            	ws2.addCell(new Label(0,curRow, bean_flag.getData_time(),wcfF2));
		 	        	ws2.addCell(new Label(1,curRow, bean_flag.getFrequency(),wcfF2));
		 	        	ws2.addCell(new Label(2,curRow, bean_flag.getPlay_time(),wcfF2));
		 	        	ws2.addCell(new Label(3,curRow, bean_flag.getLanguage(),wcfF2));
		 	        	ws2.addCell(new Label(4,curRow, bean_flag.getStation_name(),wcfF2));
		 	        	String[] headnames=bean_flag.getHead_name().split(",");
		 	        	String[] levels=bean_flag.getData_level().split(",");
		 	       	for(int k=0;k<headnames.length;k++)
 					{
		 	       	ws2.setColumnView(4+k+1, 25);
		 	        ws2.addCell(new Label(4+k+1,1,"收测遥控站"+(k+1)+"电平中值",wcfF));
		 	       	ws2.addCell(new Label(4+k+1,curRow, headnames[k]+"["+levels[k]+"]",wcfF2));
 					}
	 				bean_flag=bean;
	 	            }
	 	            
	 	          	int curRow = ws.getRows();
	 	           	ws.addCell(new Label(0,curRow, bean.getData_time(),wcfF2));
	 	        	ws.addCell(new Label(1,curRow, bean.getFrequency(),wcfF2));
	 	        	ws.addCell(new Label(2,curRow, bean.getPlay_time(),wcfF2));
	 	        	ws.addCell(new Label(3,curRow, bean.getLanguage(),wcfF2));
	 	        	ws.addCell(new Label(4,curRow, bean.getStation_name(),wcfF2));
	 	        	ws.addCell(new Label(5,curRow, bean.getHead_name(),wcfF2));
	 	        	ws.addCell(new Label(6,curRow, bean.getData_level(),wcfF2));
	 	        	ws.addCell(new Label(7,curRow, bean.getData_fmoram(),wcfF2));
	 	        	ws.addCell(new Label(8,curRow, bean.getData_offset(),wcfF2));
	 	        	
	 	        	
	 	   		if(i==(list.size()-1))
	 			{
	 			int curRow2 = ws2.getRows(); 	
	            ws2.addCell(new Label(0,curRow2, bean_flag.getData_time(),wcfF2));
 	        	ws2.addCell(new Label(1,curRow2, bean_flag.getFrequency(),wcfF2));
 	        	ws2.addCell(new Label(2,curRow2, bean_flag.getPlay_time(),wcfF2));
 	        	ws2.addCell(new Label(3,curRow2, bean_flag.getLanguage(),wcfF2));
 	        	ws2.addCell(new Label(4,curRow2, bean_flag.getStation_name(),wcfF2));
 	        	String[] headnames=bean_flag.getHead_name().split(",");
 	        	String[] levels=bean_flag.getData_level().split(",");
 	       	    for(int k=0;k<headnames.length;k++)
				{
 	       	   	ws2.setColumnView(4+k+1, 25);
	 	        ws2.addCell(new Label(4+k+1,1,"收测遥控站"+(k+1)+"电平中值",wcfF));
	 	       	ws2.addCell(new Label(4+k+1,curRow, headnames[k]+"["+levels[k]+"]",wcfF2));
				}
			
	            }
	 			
	 	      
	 		}
	 	
	 			
	 				wwb.write();
	 		        wwb.close();
	 		        outputStream.close();



	 		} 

}
