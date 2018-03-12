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
import com.viewscenes.bean.Zrst_rep_effect_summary_tab;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.printexcel.JExcel;
import com.viewscenes.pub.GDSet;
import com.viewscenes.pub.GDSetException;
import com.viewscenes.util.StringTool;
import com.viewscenes.util.TimeMethod;
import com.viewscenes.util.business.SiteVersionUtil;
import com.viewscenes.web.Daoutil.ReportUtil;
import com.viewscenes.web.Daoutil.StringDateUtil;


import flex.messaging.io.amf.ASObject;


public class EffectSummaryStatistics {

	/**************************************************
	
	 * @Title: main
	
	 * @Description: TODO(效果数据汇总功能)
	
	 * @param @param args    设定文件
	
	 * @author  刘斌
	
	 * @return void    返回类型
	
	 * @throws
	
	 *************************************************/

	public static void main(String[] args) {
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
	
	* @Description: TODO(效果数据汇总统计)
	
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
	 public static boolean doReport(String starttime, String endtime,String report_type,String user_name ,ASObject queryParam) throws DbException, GDSetException, SecurityException, InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
	    String part="";	
	    String headcodes=(String)queryParam.get("headnames");
	    String freqs=(String)queryParam.get("freqs");
		 if(report_type.equals("1008"))//国际台只统计遥控站
	    	{
			 part=" and type_id=102 and run.runplan_type_id='1'"	;
	    	}else {
	    		part=" and run.runplan_type_id !='1'";
	    	}
		 if(headcodes!=null&&headcodes.length()!=0){
			 String[] names=headcodes.split(",");
			 StringBuffer bf=new StringBuffer();
			 int n=names.length;
			 for(int i=0;i<n;i++){
				if(i==0){
					bf.append(SiteVersionUtil.getSiteHeadIdsByCodeNoAB(names[i]));
				} else{
					bf.append(",").append(SiteVersionUtil.getSiteHeadIdsByCodeNoAB(names[i]));
				}
			 }
			 part+=" and head_id in ("+bf.toString()+") ";
		 }
		 if(freqs!=null&&freqs.length()!=0){
			 part+=" and frequency in ("+freqs+")";
		 }
	       String totalsql="select  mark.headname,mark.start_datetime mark_datetime ,mark.frequency,mark.play_time,mark.counto,dis.disturb ,city.city,city.contry launch_country ,run.redisseminators,run.direction,run.power,run.service_area,run.station_name,run.ciraf,lan.language_name,head.head_id,head.type_id" +
	       		" from radio_mark_view_xiaohei mark , zres_runplan_disturb_tab dis   ,zres_runplan_chaifen_tab run,res_city_tab city,zdic_language_tab lan,res_headend_tab head" +
	       		" where mark.runplan_id=run.runplan_id  and mark.counto is not null and mark.counti is not null and mark.counts is not null and  run.language_id=lan.language_id and mark.head_code=head.code and run.sentcity_id=city.id(+) and mark.runplan_id=dis.runplan_id(+) "+

	       		" and mark.mark_datetime>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss') " +
	       		" and mark.mark_datetime<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss')" +part+
	       		" order by play_time,frequency,language_name,station_name,power ,headname,direction ,mark_datetime asc ";	        
	       ArrayList descList=new ArrayList();
	       GDSet gd = DbComponent.Query(totalsql);
	       String report_id=ReportUtil.getReportId();
	       int rowCount=0;
	       String play_time="";
		   Zrst_rep_effect_summary_tab flagbean= new Zrst_rep_effect_summary_tab();
	       for(int i=0;i<gd.getRowCount();i++)
	       {
               
	    	   play_time = getPlayTime(gd.getString(i, "mark_datetime"));
	    	   if(i==0)
	    	   { 	
//	    		   setDayList(flagbean);
	    		   flagbean.setBroadcasting_organizations(gd.getString(i, "redisseminators"));
	    		   flagbean.setCiraf(gd.getString(i, "ciraf"));
	    		   if(gd.getString(i, "type_id").equals("101"))
	    		   {
	    			   flagbean.setCollect_headname(gd.getString(i, "headname")); 
	    			   flagbean.setRemote_headname("");
	    		   }else
	    		   {
	    			   flagbean.setRemote_headname(gd.getString(i, "headname"))  ; 
	    			   flagbean.setCollect_headname("");
	    		   }
	    		   flagbean.setCount("1");
	    		   flagbean.setDatetime(gd.getString(i, "mark_datetime"));
	    		   flagbean.setDirection(gd.getString(i, "direction"));
	    		   flagbean.setFrequency(gd.getString(i, "frequency"));
	    		   flagbean.setLanguage_name(gd.getString(i, "language_name"));
	    		   flagbean.setPlay_time(play_time);
	    		   flagbean.setPower(gd.getString(i, "power"));
	    		   flagbean.setRemark(gd.getString(i, "disturb"));
	    		   flagbean.setReport_id(report_id);
	    		   flagbean.setSend_city(gd.getString(i, "city"));
	    		   flagbean.setSend_county(gd.getString(i, "launch_country"));
	    		   flagbean.setService_area(gd.getString(i, "service_area"));
	    		   flagbean.setStation_name(gd.getString(i, "station_name"));
	    		   flagbean.setListen_middle(gd.getString(i, "counto"));
//	    		  setDays(flagbean, gd.getString(i, "mark_datetime"), gd.getString(i, "counto"));
	    	   }
	    	   else
	    	   {
	    		   if(play_time.equals(flagbean.getPlay_time())
	    				   &&gd.getString(i, "mark_datetime").equals(flagbean.getDatetime())
	    				   &&gd.getString(i, "language_name").equals(flagbean.getLanguage_name())
	    				   &&gd.getString(i, "frequency").equals(flagbean.getFrequency())
	    				   &&gd.getString(i, "station_name").equals(flagbean.getStation_name())
	    				   &&gd.getString(i, "direction").equals(flagbean.getDirection())
	    				   &&gd.getString(i, "power").equals(flagbean.getPower())
	    				   &&gd.getString(i, "service_area").equals(flagbean.getService_area())
	    				   &&gd.getString(i, "ciraf").equals(flagbean.getCiraf())
	    				   &&gd.getString(i, "redisseminators").equals(flagbean.getBroadcasting_organizations())
	                       &&gd.getString(i, "launch_country").endsWith(flagbean.getSend_county()) 
	                       &&gd.getString(i, "city").equals(flagbean.getSend_city())
	                       
	    		   ){
	    			   if(gd.getString(i, "type_id").equals("101")){
	    			       flagbean.setCollect_headname(StringTool.delDuplicateStr((flagbean.getCollect_headname().equals("")?"":flagbean.getCollect_headname()+",")+gd.getString(i, "headname")));
	    			       flagbean.setRemote_headname(flagbean.getRemote_headname());
		    		   }else{
		    			   flagbean.setRemote_headname(StringTool.delDuplicateStr((flagbean.getRemote_headname().equals("")?"":flagbean.getRemote_headname()+",")+gd.getString(i, "headname")));
		    			   flagbean.setCollect_headname(flagbean.getCollect_headname());
				    		     
		    		   }
	    			   flagbean.setCount((Integer.parseInt(flagbean.getCount())+1)+"");
	    			   flagbean.setListen_middle(flagbean.getListen_middle()+","+gd.getString(i, "counto"));
	    		   }else {
	    			   //添加前需要计算可听度中值和可听率.
	    			   setListen(flagbean,flagbean.getListen_middle());
//	    			   if(flagbean.getListen()<60)
	    			   descList.add(flagbean);  
	    			   flagbean= new Zrst_rep_effect_summary_tab();
	    			   flagbean.setBroadcasting_organizations(gd.getString(i, "redisseminators"));
		    		   flagbean.setCiraf(gd.getString(i, "ciraf"));
		    		   if(gd.getString(i, "type_id").equals("101"))
		    		   {
		    			   flagbean.setCollect_headname(gd.getString(i, "headname"));  
		    			   flagbean.setRemote_headname("");
		    			 
		    		   }else
		    		   {
		    			   flagbean.setRemote_headname(gd.getString(i, "headname"))  ; 
		    			   flagbean.setCollect_headname("");
		    		   }
		    		   flagbean.setCount("1");
		    		   flagbean.setDatetime(gd.getString(i, "mark_datetime"));
		    		   flagbean.setDirection(gd.getString(i, "direction"));
		    		   flagbean.setFrequency(gd.getString(i, "frequency"));
		    		   flagbean.setLanguage_name(gd.getString(i, "language_name"));
		    		   flagbean.setPlay_time(play_time);
		    		   flagbean.setPower(gd.getString(i, "power"));
		    		   flagbean.setRemark(gd.getString(i, "disturb"));
		    		   flagbean.setReport_id(report_id);
		    		   flagbean.setSend_city(gd.getString(i, "city"));
		    		   flagbean.setSend_county(gd.getString(i, "launch_country"));
		    		   flagbean.setService_area(gd.getString(i, "service_area"));
		    		   flagbean.setStation_name(gd.getString(i, "station_name"));
		    		   flagbean.setListen_middle(gd.getString(i, "counto"));
	    		   }
	    		   if(i==gd.getRowCount()-1){
	    			   setListen(flagbean,flagbean.getListen_middle());
    		           descList.add(flagbean);    		           
    		       }	    		   
	    	   }	    	   
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
     boolean boo=ReportUtil.insertReportByReportBean(reportBean,  descList, "Zrst_rep_effect_summary_tab", "com.viewscenes.bean.Zrst_rep_effect_summary_tab");
			
   return true;
}
	 /**
	  * ************************************************

	 * @Title: doGJTExcel

	 * @Description: TODO(导出国际台效果报表)

	 * @param @param msg
	 * @param @param request
	 * @param @param response
	 * @param @throws Exception    设定文件

	 * @author  刘斌

	 * @return void    返回类型

	 * @throws

	 ************************************************
	  */
	 	 public void doGJTExcel(String msg,HttpServletRequest request,HttpServletResponse response) throws Exception{
	 			Element root = StringTool.getXMLRoot(msg);
	 			String reportId = root.getChildText("reportId");
	 			String starttime = root.getChildText("starttime");
	 			String endtime = root.getChildText("endtime");
	 			long countAll= (StringDateUtil.string2Date(endtime.substring(0, 11),"yyyy-MM-dd").getTime()-StringDateUtil.string2Date(starttime.substring(0, 11),"yyyy-MM-dd").getTime())/86400000l;
	 			String fileName="国际台广播效果汇总";
	 			if(starttime.equals(endtime)){
	 				fileName = "国际台广播效果"+starttime+"日汇总";
	 			} else 
	 			{
	 				fileName = "国际台广播效果("+starttime.substring(0, 11)+"到"+endtime.substring(0, 11)+")汇总";
	 			}
	 			String descSql="select * from zrst_rep_effect_summary_tab t where report_id="+reportId+"   "+
	 					" order by t.report_id,play_time,frequency,language_name,station_name,power ,remote_headname,direction ,datetime asc ";
	 			GDSet gdSet=DbComponent.Query(descSql);
	 			String classpath = Zrst_rep_effect_summary_tab.class.getName();
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
	 			
	 			WritableSheet ws2 = null;
	 			
	 		    ws2=wwb.createSheet("国际台广播效果汇总表", wwb.getNumberOfSheets()+2);//表二
	 		    WritableFont wf = new WritableFont(WritableFont.createFont("黑体"), 10,WritableFont.BOLD, false);
	 			WritableCellFormat wcfF = new WritableCellFormat(wf);
	 		    wcfF.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);//带边框 
	             wcfF.setWrap(true);//自动换行
	              // 把水平对齐方式指定为左对齐
	              wcfF.setAlignment(jxl.format.Alignment.CENTRE);
	              // 把垂直对齐方式指定为居中对齐
	              wcfF.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	              //设置列名
	              ws2.mergeCells(0,1,0,2);
	              ws2.addCell(new Label(0,1,"播音时间",wcfF));
	              ws2.mergeCells(1,1,1,2);
	              ws2.addCell(new Label(1,1,"语言",wcfF));
	              ws2.mergeCells(2,1,2,2);
	              ws2.addCell(new Label(2,1,"频率",wcfF));
	              ws2.mergeCells(3,1,3,2);
	              ws2.addCell(new Label(3,1,"发射台",wcfF));
	              ws2.mergeCells(4,1,4,2);
	              ws2.addCell(new Label(4,1,"发射方向",wcfF));
	              ws2.mergeCells(5,1,5,2);
	              ws2.addCell(new Label(5,1,"发射功率",wcfF));
	              ws2.mergeCells(6,1,6,2);
	              ws2.addCell(new Label(6,1,"服务区",wcfF));
	              ws2.mergeCells(7,1,7,2);
	              ws2.addCell(new Label(7,1,"CIRAF区",wcfF));
	              ws2.mergeCells(8,1,8,2);
	              ws2.addCell(new Label(8,1,"収测遥控站",wcfF));
	              ws2.mergeCells(9,1,9,2);
	              ws2.addCell(new Label(9,1,"収测次数",wcfF));
	           
	            String[] times=  TimeMethod.getTimeArrayFromStartToEnd(starttime.substring(0, 11), endtime.substring(0, 11), "yyyy-MM-dd");
	            ws2.mergeCells(10,1,9+times.length,1); //合并日期
	            ws2.addCell(new Label(10,1,"日期("+starttime.substring(0, 11)+"到"+ endtime.substring(0, 11)+")",wcfF));
	            for(int t=0;t<times.length;t++)
	              {
	              	ws2.addCell(new Label(10+t,2,times[t].substring(8,times[t].length()),wcfF));
	              	ws2.setColumnView(10+t, 9);
	              }
	              ws2.mergeCells(10+times.length,1,10+times.length,2);
	              ws2.addCell(new Label(10+times.length,1,"可听率%",wcfF));
	              ws2.mergeCells(11+times.length,1,11+times.length,2);
	              ws2.addCell(new Label(11+times.length,1,"可听度中值",wcfF));
	              ws2.mergeCells(12+times.length,1,12+times.length,2);
	              ws2.addCell(new Label(12+times.length,1,"备注",wcfF));
	              
	              ws2.setColumnView(10+times.length, 20);
	              ws2.setColumnView(11+times.length, 20);
	              ws2.setColumnView(12+times.length, 20);
	              ws2.mergeCells(0,0,12+times.length,0);
	              wf = new WritableFont(WritableFont.createFont("黑体"), 12,WritableFont.BOLD, false);
	 				 wcfF = new WritableCellFormat(wf);
	 				 wcfF.setAlignment(jxl.format.Alignment.CENTRE);
	              // 把垂直对齐方式指定为居中对齐
	              wcfF.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	          
	             
	              ws2.addCell(new Label(0, 0, "国际台广播效果汇总表",wcfF));
	             
	              ws2.setRowView(0, 740);//设置第一行高度
	              ws2.setRowView(1, 740);
	              
	              ws2.setColumnView(0, 20);
	              ws2.setColumnView(1, 13);
	              ws2.setColumnView(2, 15);
	              ws2.setColumnView(3, 20);
	              ws2.setColumnView(4, 13);
	              ws2.setColumnView(5, 13);
	              ws2.setColumnView(6, 13);
	              ws2.setColumnView(7, 13);
	              ws2.setColumnView(8, 13);
	              ws2.setColumnView(9, 10);
	     
	          
	          	WritableFont wf1 = new WritableFont(WritableFont.createFont("宋体"), 10,WritableFont.NO_BOLD, false);
	             //设置CellFormat
	             jxl.write.WritableCellFormat wcfF2 = new jxl.write.WritableCellFormat(wf1);
	             // 把水平对齐方式指定为左对齐
	             wcfF2.setAlignment(jxl.format.Alignment.CENTRE);
	             // 把垂直对齐方式指定为居中对齐
	             wcfF2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	             wcfF2.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); 
                 int flag=0;
                 Zrst_rep_effect_summary_tab flagbean=new Zrst_rep_effect_summary_tab();
	 			for(int i=0;i<list.size();i++){
	 				Zrst_rep_effect_summary_tab bean = (Zrst_rep_effect_summary_tab)list.get(i);
	 	         if(i==0)
	 	         {
	 	        	flagbean=bean;  
	 	        	int curRow = ws2.getRows();
	 	        	ws2.addCell(new Label(0,curRow, bean.getPlay_time(),wcfF2));
	 	        	ws2.addCell(new Label(1,curRow, bean.getLanguage_name(),wcfF2));
	 	        	ws2.addCell(new Label(2,curRow, bean.getFrequency(),wcfF2));
	 	        	ws2.addCell(new Label(3,curRow, bean.getStation_name(),wcfF2));
	 	        	ws2.addCell(new Label(4,curRow, bean.getDirection(),wcfF2));
	 	        	ws2.addCell(new Label(5,curRow, bean.getPower(),wcfF2));
	 	        	ws2.addCell(new Label(6,curRow, bean.getService_area(),wcfF2));
	 	        	ws2.addCell(new Label(7,curRow, bean.getCiraf(),wcfF2));
	 	        	ws2.addCell(new Label(8,curRow, bean.getRemote_headname(),wcfF2));
	 	        	if(bean.getDatetime()!=null&&!bean.getDatetime().equals(""))
	 	        	{
	 	        
	 	        	 int countDay= (int)((StringDateUtil.string2Date(bean.getDatetime(),"yyyy-MM-dd").getTime()-StringDateUtil.string2Date(starttime.substring(0, 11),"yyyy-MM-dd").getTime())/86400000l);
			    			  
			 	           for(int m=0;m<=countAll;m++)
		    			   {
		    				  
		    				   if(countDay==m){
		    					   ws2.addCell(new Label(10+countDay,curRow, bean.getListen_middle(),wcfF2));  
		    				   }else
		    				   {
		    					   ws2.addCell(new Label(10+m,curRow, "",wcfF2)); 
		    				   }
		    			   }
		    			  
	 	        	}
	 	        	continue;
	 	         }
	 	        if(bean.getPlay_time().equals(flagbean.getPlay_time())
	    				   &&bean.getLanguage_name().equals(flagbean.getLanguage_name())
	    				   &&bean.getFrequency().equals(flagbean.getFrequency())
	    				   &&bean.getStation_name().equals(flagbean.getStation_name())
	    				   &&bean.getDirection().equals(flagbean.getDirection())
	    				   &&bean.getPower().equals(flagbean.getPower())
	    				   &&bean.getService_area().equals(flagbean.getService_area())
	    				   &&bean.getCiraf().equals(flagbean.getCiraf())
	    				   &&bean.getBroadcasting_organizations().equals(flagbean.getBroadcasting_organizations())
	                       &&bean.getSend_county().endsWith(flagbean.getSend_county()) 
	                       &&bean.getSend_city().equals(flagbean.getSend_city())
	                       &&bean.getRemote_headname().equals(flagbean.getRemote_headname())
	                       
	    		   )
	 	            {
	 	        	 int curRow = ws2.getRows();
	 	        	   flagbean.setCount((Integer.parseInt(flagbean.getCount())+Integer.parseInt(bean.getCount()))+"");
	    			   flagbean.setListen_middle(flagbean.getListen_middle()+","+bean.getListen_middle());
	    				if(bean.getDatetime()!=null&&!bean.getDatetime().equals(""))
		 	        	{
	    					 int countDay= (int)((StringDateUtil.string2Date(bean.getDatetime(),"yyyy-MM-dd").getTime()-StringDateUtil.string2Date(starttime.substring(0, 11),"yyyy-MM-dd").getTime())/86400000l);
	    			    			  
	    				     for(int m=0;m<=countAll;m++)
			    			   {
			    				  
			    				   if(countDay==m){
			    					   ws2.addCell(new Label(10+countDay,curRow-1, bean.getListen_middle(),wcfF2));  
			    				   }else
			    				   {	  
			    					   if(ws2.getCell(10+m, curRow-1).getContents().equals(""))
			    					   {
			    					     ws2.addCell(new Label(10+m,curRow-1, "",wcfF2)); 
			    					   }
			    				   }
			    			   }
		 	        	}
	 	            }else
	 	            {
	 	            	setListenAndListen_middle(flagbean);
	 	              	int curRow = ws2.getRows();
	 	              	ws2.addCell(new Label(9,curRow-1, flagbean.getCount(),wcfF2));
	 	                ws2.addCell(new Label(10+times.length,curRow-1,flagbean.getListen(),wcfF2));
	 	                ws2.addCell(new Label(11+times.length,curRow-1,flagbean.getListen_middle(),wcfF2));
	 	                ws2.addCell(new Label(12+times.length,curRow-1,flagbean.getRemark(),wcfF2));
	 	              	
	 	            	ws2.addCell(new Label(0,curRow, bean.getPlay_time(),wcfF2));
		 	        	ws2.addCell(new Label(1,curRow, bean.getLanguage_name(),wcfF2));
		 	        	ws2.addCell(new Label(2,curRow, bean.getFrequency(),wcfF2));
		 	        	ws2.addCell(new Label(3,curRow, bean.getStation_name(),wcfF2));
		 	        	ws2.addCell(new Label(4,curRow, bean.getDirection(),wcfF2));
		 	        	ws2.addCell(new Label(5,curRow, bean.getPower(),wcfF2));
		 	        	ws2.addCell(new Label(6,curRow, bean.getService_area(),wcfF2));
		 	        	ws2.addCell(new Label(7,curRow, bean.getCiraf(),wcfF2));
		 	        	ws2.addCell(new Label(8,curRow, bean.getRemote_headname(),wcfF2));
		 	        
		 	       	if(bean.getDatetime()!=null&&!bean.getDatetime().equals(""))
	 	        	{
		 	       	 int countDay= (int)((StringDateUtil.string2Date(bean.getDatetime(),"yyyy-MM-dd").getTime()-StringDateUtil.string2Date(starttime.substring(0, 11),"yyyy-MM-dd").getTime())/86400000l);
			    			  
					 	       for(int m=0;m<=countAll;m++)
			    			   {
			    				  
			    				   if(countDay==m){
			    					   ws2.addCell(new Label(10+countDay,curRow, bean.getListen_middle(),wcfF2));  
			    				   }else
			    				   {
			    					   ws2.addCell(new Label(10+m,curRow, "",wcfF2)); 
			    				   }
			    			   }
	 	        	}
		 	        	flagbean=bean;
	 	            }
	
	 	   		if(i==(list.size()-1))
	 			{
	 			int curRow = ws2.getRows(); 	
	 		 	setListenAndListen_middle(flagbean);
	              	
	              	ws2.addCell(new Label(9,curRow-1, flagbean.getCount(),wcfF2));
	                ws2.addCell(new Label(10+times.length,curRow-1,flagbean.getListen(),wcfF2));
	                ws2.addCell(new Label(11+times.length,curRow-1,flagbean.getListen_middle(),wcfF2));
	                ws2.addCell(new Label(12+times.length,curRow-1,flagbean.getRemark(),wcfF2));
	              	
	 
	            }
	 			
	 	      
	 		}
	 	
	 			
	 				wwb.write();
	 		        wwb.close();
	 		        outputStream.close();



	 		} 
	 	/**
		  * ************************************************

		 * @Title: doHWLDExcel

		 * @Description: TODO(导出海外落地效果汇总报表)

		 * @param @param msg
		 * @param @param request
		 * @param @param response
		 * @param @throws Exception    设定文件

		 * @author  刘斌

		 * @return void    返回类型

		 * @throws

		 ************************************************
		  */
		 	 public void doHWLDExcel(String msg,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 			Element root = StringTool.getXMLRoot(msg);
		 			String reportId = root.getChildText("reportId");
		 			String starttime = root.getChildText("starttime");
		 			String endtime = root.getChildText("endtime");
		 			long countAll= (StringDateUtil.string2Date(endtime.substring(0, 11),"yyyy-MM-dd").getTime()-StringDateUtil.string2Date(starttime.substring(0, 11),"yyyy-MM-dd").getTime())/86400000l;
			    		
		 			String fileName="海外落地广播效果汇总";
		 			if(starttime.equals(endtime)){
		 				fileName = "海外落地广播效果"+starttime+"日汇总";
		 			} else 
		 			{
		 				fileName = "海外落地广播效果("+starttime.substring(0, 11)+"到"+endtime.substring(0, 11)+")汇总";
		 			} 
		 			String descSql="select * from zrst_rep_effect_summary_tab t where report_id="+reportId+"   " +
		 					" order by t.report_id,play_time,frequency,language_name,station_name,power ,collect_headname,direction ,datetime asc  ";
		 			GDSet gdSet=DbComponent.Query(descSql);
		 			String classpath = Zrst_rep_effect_summary_tab.class.getName();
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
		 			
		 			WritableSheet ws2 = null;
		 			
		 		    ws2=wwb.createSheet("海外落地广播效果汇总表", wwb.getNumberOfSheets()+2);//表二
		 		    WritableFont wf = new WritableFont(WritableFont.createFont("黑体"), 10,WritableFont.BOLD, false);
		 			WritableCellFormat wcfF = new WritableCellFormat(wf);
		 		    wcfF.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);//带边框 
		             wcfF.setWrap(true);//自动换行
		              // 把水平对齐方式指定为左对齐
		              wcfF.setAlignment(jxl.format.Alignment.CENTRE);
		              // 把垂直对齐方式指定为居中对齐
		              wcfF.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		              //设置列名
		              ws2.mergeCells(0,1,0,2);
		              ws2.addCell(new Label(0,1,"播音时间",wcfF));
		              ws2.mergeCells(1,1,1,2);
		              ws2.addCell(new Label(1,1,"语言",wcfF));
		              ws2.mergeCells(2,1,2,2);
		              ws2.addCell(new Label(2,1,"频率",wcfF));
		              ws2.mergeCells(3,1,3,2);
		              ws2.addCell(new Label(3,1,"转播机构",wcfF));
		              ws2.mergeCells(4,1,4,2);
		              ws2.addCell(new Label(4,1,"发射国家",wcfF));
		              ws2.mergeCells(5,1,5,2);
		              ws2.addCell(new Label(5,1,"发射城市",wcfF));
		              ws2.mergeCells(6,1,6,2);
		              ws2.addCell(new Label(6,1,"发射功率",wcfF));
		              ws2.mergeCells(7,1,7,2);
		              ws2.addCell(new Label(7,1,"服务区",wcfF));
		              ws2.mergeCells(8,1,8,2);
		              ws2.addCell(new Label(8,1,"收测遥控站",wcfF));
		              ws2.mergeCells(9,1,9,2);
		              ws2.addCell(new Label(9,1,"收测采集点",wcfF));
		              ws2.mergeCells(10,1,10,2);
		              ws2.addCell(new Label(10,1,"收测次数",wcfF));
		           
		            String[] times=  TimeMethod.getTimeArrayFromStartToEnd(starttime.substring(0, 11), endtime.substring(0, 11), "yyyy-MM-dd");
		            ws2.mergeCells(11,1,10+times.length,1); //合并日期
		            ws2.addCell(new Label(11,1,"日期("+starttime.substring(0, 11)+"到"+ endtime.substring(0, 11)+")",wcfF));
		            for(int t=0;t<times.length;t++)
		              {
		              	ws2.addCell(new Label(11+t,2,times[t].substring(8,times[t].length()),wcfF));
		              	ws2.setColumnView(11+t, 9);
		              }
		              ws2.mergeCells(11+times.length,1,11+times.length,2);
		              ws2.addCell(new Label(11+times.length,1,"可听率%",wcfF));
		              ws2.mergeCells(12+times.length,1,12+times.length,2);
		              ws2.addCell(new Label(12+times.length,1,"可听度中值",wcfF));
		              ws2.mergeCells(13+times.length,1,13+times.length,2);
		              ws2.addCell(new Label(13+times.length,1,"备注",wcfF));
		              
		              ws2.setColumnView(11+times.length, 20);
		              ws2.setColumnView(12+times.length, 20);
		              ws2.setColumnView(13+times.length, 20);
		              ws2.mergeCells(0,0,13+times.length,0);
		              wf = new WritableFont(WritableFont.createFont("黑体"), 12,WritableFont.BOLD, false);
		 				 wcfF = new WritableCellFormat(wf);
		 				 wcfF.setAlignment(jxl.format.Alignment.CENTRE);
		              // 把垂直对齐方式指定为居中对齐
		              wcfF.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		          
		             
		              ws2.addCell(new Label(0, 0, "海外落地广播效果汇总表",wcfF));
		             
		              ws2.setRowView(0, 740);//设置第一行高度
		              ws2.setRowView(1, 740);
		              
		              ws2.setColumnView(0, 20);
		              ws2.setColumnView(1, 13);
		              ws2.setColumnView(2, 13);
		              ws2.setColumnView(3, 10);
		              ws2.setColumnView(4, 15);
		              ws2.setColumnView(5, 15);
		              ws2.setColumnView(6, 10);
		              ws2.setColumnView(7, 15);
		              ws2.setColumnView(8, 13);
		              ws2.setColumnView(9, 13);
		              ws2.setColumnView(10, 10);
		     
		          
		          	WritableFont wf1 = new WritableFont(WritableFont.createFont("宋体"), 10,WritableFont.NO_BOLD, false);
		             //设置CellFormat
		             jxl.write.WritableCellFormat wcfF2 = new jxl.write.WritableCellFormat(wf1);
		             // 把水平对齐方式指定为左对齐
		             wcfF2.setAlignment(jxl.format.Alignment.CENTRE);
		             // 把垂直对齐方式指定为居中对齐
		             wcfF2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		             wcfF2.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); 
	                 int flag=0;
	                 Zrst_rep_effect_summary_tab flagbean=new Zrst_rep_effect_summary_tab();
		 			for(int i=0;i<list.size();i++){
		 				Zrst_rep_effect_summary_tab bean = (Zrst_rep_effect_summary_tab)list.get(i);
		 	         if(i==0)
		 	         {
		 	        	flagbean=bean;  
		 	        	int curRow = ws2.getRows();
		 	        	ws2.addCell(new Label(0,curRow, bean.getPlay_time(),wcfF2));
		 	        	ws2.addCell(new Label(1,curRow, bean.getLanguage_name(),wcfF2));
		 	        	ws2.addCell(new Label(2,curRow, bean.getFrequency(),wcfF2));
		 	        	ws2.addCell(new Label(3,curRow, bean.getBroadcasting_organizations(),wcfF2));
		 	        	ws2.addCell(new Label(4,curRow, bean.getSend_county(),wcfF2));
		 	        	ws2.addCell(new Label(5,curRow, bean.getSend_city(),wcfF2));
		 	        	ws2.addCell(new Label(6,curRow, bean.getPower(),wcfF2));
		 	        	ws2.addCell(new Label(7,curRow, bean.getService_area(),wcfF2));
		 	        	ws2.addCell(new Label(8,curRow, bean.getRemote_headname(),wcfF2));
		 	        	ws2.addCell(new Label(9,curRow, bean.getCollect_headname(),wcfF2));
		 	        
		 	        	if(bean.getDatetime()!=null&&!bean.getDatetime().equals(""))
		 	        	{
		 	        
		 	        		 int countDay= (int)((StringDateUtil.string2Date(bean.getDatetime(),"yyyy-MM-dd").getTime()-StringDateUtil.string2Date(starttime.substring(0, 11),"yyyy-MM-dd").getTime())/86400000l);
				    			  
				 	           for(int m=0;m<countAll;m++)
			    			   {
			    				  
			    				   if(countDay==m){
			    					   ws2.addCell(new Label(11+countDay,curRow, bean.getListen_middle(),wcfF2));  
			    				   }else
			    				   {
			    					   if(ws2.getCell(11+m, curRow).getContents().equals(""))
			    					   ws2.addCell(new Label(11+m,curRow, "",wcfF2)); 
			    				   }
			    			   }
			    			  
		 	        	}
		 	        	continue;
		 	         }
		 	        if(bean.getPlay_time().equals(flagbean.getPlay_time())
		    				   &&bean.getLanguage_name().equals(flagbean.getLanguage_name())
		    				   &&bean.getFrequency().equals(flagbean.getFrequency())
		    				   &&bean.getStation_name().equals(flagbean.getStation_name())
		    				   &&bean.getDirection().equals(flagbean.getDirection())
		    				   &&bean.getPower().equals(flagbean.getPower())
		    				   &&bean.getService_area().equals(flagbean.getService_area())
		    				   &&bean.getCiraf().equals(flagbean.getCiraf())
		    				   &&bean.getBroadcasting_organizations().equals(flagbean.getBroadcasting_organizations())
		                       &&bean.getSend_county().endsWith(flagbean.getSend_county()) 
		                       &&bean.getSend_city().equals(flagbean.getSend_city())
		                       &&bean.getRemote_headname().equals(flagbean.getRemote_headname())
		                       &&bean.getRemote_headname().equals(flagbean.getRemote_headname())
		                       
		    		   )
		 	            {
		 	        	 int curRow = ws2.getRows();
		 	        	   flagbean.setCount((Integer.parseInt(flagbean.getCount())+Integer.parseInt(bean.getCount()))+"");
		    			   flagbean.setListen_middle(flagbean.getListen_middle()+","+bean.getListen_middle());
		    				if(bean.getDatetime()!=null&&!bean.getDatetime().equals(""))
			 	        	{
		    					 int countDay= (int)((StringDateUtil.string2Date(bean.getDatetime(),"yyyy-MM-dd").getTime()-StringDateUtil.string2Date(starttime.substring(0, 11),"yyyy-MM-dd").getTime())/86400000l);
		    			    			  
		    				     for(int m=0;m<=countAll;m++)
				    			   {
				    				  
				    				   if(countDay==m){
				    					   ws2.addCell(new Label(11+countDay,curRow-1, bean.getListen_middle(),wcfF2));  
				    				   }else
				    				   {	  
				    					   if(ws2.getCell(11+m, curRow-1).getContents().equals(""))
				    					   {
				    					     ws2.addCell(new Label(11+m,curRow-1, "",wcfF2)); 
				    					   }
				    				   }
				    			   }
			 	        	}
		 	            }else
		 	            {
		 	            	setListenAndListen_middle(flagbean);
		 	              	int curRow = ws2.getRows();
		 	              	ws2.addCell(new Label(10,curRow-1, flagbean.getCount(),wcfF2));
		 	                ws2.addCell(new Label(11+times.length,curRow-1,flagbean.getListen(),wcfF2));
		 	                ws2.addCell(new Label(12+times.length,curRow-1,flagbean.getListen_middle(),wcfF2));
		 	                ws2.addCell(new Label(13+times.length,curRow-1,flagbean.getRemark(),wcfF2));
		 	              	
			 	           	ws2.addCell(new Label(0,curRow, bean.getPlay_time(),wcfF2));
			 	        	ws2.addCell(new Label(1,curRow, bean.getLanguage_name(),wcfF2));
			 	        	ws2.addCell(new Label(2,curRow, bean.getFrequency(),wcfF2));
			 	        	ws2.addCell(new Label(3,curRow, bean.getBroadcasting_organizations(),wcfF2));
			 	        	ws2.addCell(new Label(4,curRow, bean.getSend_county(),wcfF2));
			 	        	ws2.addCell(new Label(5,curRow, bean.getSend_city(),wcfF2));
			 	        	ws2.addCell(new Label(6,curRow, bean.getPower(),wcfF2));
			 	        	ws2.addCell(new Label(7,curRow, bean.getService_area(),wcfF2));
			 	        	ws2.addCell(new Label(8,curRow, bean.getRemote_headname(),wcfF2));
			 	        	ws2.addCell(new Label(9,curRow, bean.getCollect_headname(),wcfF2));
		 	        
			 	        
			 	       	if(bean.getDatetime()!=null&&!bean.getDatetime().equals(""))
		 	        	{
			 	       	 int countDay= (int)((StringDateUtil.string2Date(bean.getDatetime(),"yyyy-MM-dd").getTime()-StringDateUtil.string2Date(starttime.substring(0, 11),"yyyy-MM-dd").getTime())/86400000l);
				    			  
						 	       for(int m=0;m<=countAll;m++)
				    			   {
				    				  
				    				   if(countDay==m){
				    					   ws2.addCell(new Label(11+countDay,curRow, bean.getListen_middle(),wcfF2));  
				    				   }else
				    				   {
				    					   if(ws2.getCell(11+m, curRow-1).getContents().equals(""))
				    					   ws2.addCell(new Label(11+m,curRow, "",wcfF2)); 
				    				   }
				    			   }
		 	        	}
			 	        	flagbean=bean;
		 	            }
		
		 	   		if(i==(list.size()-1))
		 			{
		 			int curRow = ws2.getRows(); 	
		 		 	setListenAndListen_middle(flagbean);
		              	
		              	ws2.addCell(new Label(10,curRow-1, flagbean.getCount(),wcfF2));
		                ws2.addCell(new Label(11+times.length,curRow-1,flagbean.getListen(),wcfF2));
		                ws2.addCell(new Label(12+times.length,curRow-1,flagbean.getListen_middle(),wcfF2));
		                ws2.addCell(new Label(13+times.length,curRow-1,flagbean.getRemark(),wcfF2));
		              	
		 
		            }
		 			
		 	      
		 		}
		 	
		 			
		 				wwb.write();
		 		        wwb.close();
		 		        outputStream.close();



		 		} 
	 	 /**
	 	  * ************************************************
	 	 
	 	 * @Title: setListen
	 	 
	 	 * @Description: TODO(计算可听率和可听度中值)
	 	 
	 	 * @param @param bean
	 	 * @param @param lisens    设定文件
	 	 
	 	 * @author  刘斌
	 	 
	 	 * @return void    返回类型
	 	 
	 	 * @throws
	 	 
	 	 ************************************************
	 	  */
		public static void setListen(Zrst_rep_effect_summary_tab bean,String lisens)
		{
			String[] fens=lisens.split(",");
			
			bean.setListen_middle(fens[(fens.length+1)/2-1]);
			double count3=0;
			for(int i=0;i<fens.length;i++)
			{
				if(fens[i]!=null&&!fens[i].equalsIgnoreCase("")){
					if(Integer.parseInt(fens[i])>=3){
						count3++;
					}
				}
			}
			double db=(count3/fens.length)*100;
			bean.setListen(String.format("%.2f",db));
		}
		 /**
	 	  * ************************************************
	 	 
	 	 * @Title: setListenAndListen_middle
	 	 
	 	 * @Description: TODO(计算可听率和可听度中值,用的是求平均数的方法)
	 	 
	 	 * @param @param bean
	 	 * @param @param lisens    设定文件
	 	 
	 	 * @author  刘斌
	 	 
	 	 * @return void    返回类型
	 	 
	 	 * @throws
	 	 
	 	 ************************************************
	 	  */
		public static void setListenAndListen_middle(Zrst_rep_effect_summary_tab bean)
		{
			String[] Listens=bean.getListen().split(",");
			String[] Listens_middle=bean.getListen_middle().split(",");
            int countListens_middle=0;
            double countListens=0;
			for(int i=0;i<Listens_middle.length;i++)
			{
				countListens_middle+=Integer.parseInt(Listens_middle[i]);
			}
			for(int i=0;i<Listens.length;i++)
			{
				countListens+=Double.parseDouble(Listens[i]);
			}
			bean.setListen_middle(countListens_middle/Listens_middle.length+"");
			double listens_middle=countListens/Listens.length;
			bean.setListen(String.format("%.2f",listens_middle));
			if(countListens<60)
			{
				bean.setRemark("信号弱，杂音大。");	
			}
				
		}
		/**将录音时间转换成半小时
		 * @author leeo
		 * @param type
		 * @param recordTime
		 * @return
		 */
		public static String getPlayTime(String recordTime){
			String play_time = "";
			String hour = recordTime.substring(11,13);
				String minute = recordTime.substring(14,16);
				int min = Integer.parseInt(minute);
				if(min<30){
					play_time = hour + ":00-"+hour+":30";
				} else{
					if(hour.equals("23")){
						play_time = "23:30-00:00";
					} else{
						int otherHour = Integer.parseInt(hour)+1;
						String otherHourStr = otherHour+"";
						if(otherHour<10){
							otherHourStr = "0"+otherHour;
						}
						play_time = hour + ":30-"+otherHourStr+":00";
					}
				}
			return play_time;
		}
}
