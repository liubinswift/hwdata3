package com.viewscenes.web.freqtimetab;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Element;

import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.printexcel.JExcel;
import com.viewscenes.pub.GDSet;
import com.viewscenes.util.StringTool;

import flex.messaging.io.amf.ASObject;

/**
 * 频率时间表生成类
 * @author leeo
 *
 */
public class FreqRecord {
	public FreqRecord(){
		
	}

	public ArrayList getInfo(ASObject asobj){
		ArrayList list = new ArrayList();
		//String runplanType = (String) asobj.get("runplanType");
		String website = (String) asobj.get("website");
		String sql="";
		//if(runplanType.equalsIgnoreCase("1")){
			sql="select distinct t.freq,t.broadcast_time,t.language,t.station_name,t.redisseminators,t.power,t.direction,t.service_area,t.ciraf,t.program_type,t.runplan_type_id " +
					"from zres_freq_time_tab t where 1=1  and to_date(t.valid_end_date,'yyyy-mm-dd hh24:mi:ss')>sysdate ";
		 
			//	}else{
		//	sql="select t.freq,t.broadcast_time,t.language,t.redisseminators,t.power,t.direction,t.service_area,t.program_type " +
	//		"from zres_freq_time_tab t where t.runplan_type_id=2 ";
	//	}
		 if(website!=null&&!website.equalsIgnoreCase("")){
				if(website.indexOf(",")>=0){
					 String[] s = website.split(",");
					 String ss="";
					 String sss="";
					 for(int i=0;i<s.length;i++){
						 ss+=" t.mon_area like '%"+s[i]+"%' or ";
						 sss+=" t.xg_mon_area like '%"+s[i]+"%' or ";
					 }
					 sql+=" and (("+ss.substring(0, ss.length()-3)+") or ("+sss.substring(0, sss.length()-3)+"))";
				 }else{
					 sql+=" and (t.mon_area like '%"+website+"%' or t.xg_mon_area like '%"+website+"%' )";
				 }
			}
		 sql+=" order by t.runplan_type_id,t.broadcast_time,t.language,t.freq ";
		try {
			GDSet gd = DbComponent.Query(sql);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					ASObject obj = new ASObject();
					obj.put("freq",gd.getString(i, "freq"));
					obj.put("start_time", gd.getString(i, "broadcast_time").split("-")[0]);
					obj.put("end_time", gd.getString(i, "broadcast_time").split("-")[1]);
					obj.put("language", gd.getString(i, "language"));
					if(gd.getString(i, "runplan_type_id").equalsIgnoreCase("1")){
						obj.put("station", gd.getString(i, "station_name"));
						obj.put("ciraf", gd.getString(i, "ciraf"));
					}else{
						obj.put("redisseminators", gd.getString(i, "redisseminators"));
					}
					obj.put("power", gd.getString(i, "power"));
					obj.put("direction", gd.getString(i, "direction"));
					obj.put("service_area", gd.getString(i, "service_area"));
					obj.put("program_type", gd.getString(i, "program_type"));
					list.add(obj);
;				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	/**
	 * 导出Excel
	 * @param msg
	 * @param request
	 * @param response
	 */
	public void doExcel(String msg,HttpServletRequest request,HttpServletResponse response){
		Element root = StringTool.getXMLRoot(msg);
		String website = root.getChildText("website");
		String sql="select t.freq,t.broadcast_time,t.language,decode( t.station_name,'',t.redisseminators,t.station_name) as station_name,t.power,t.direction,t.service_area,t.ciraf,t.program_type " +
		           "from zres_freq_time_tab t where 1=1 and to_date(t.valid_end_date,'yyyy-mm-dd hh24:mi:ss')>sysdate  ";
		 if(website!=null&&!website.equalsIgnoreCase("")){
				if(website.indexOf(",")>=0){
					 String[] s = website.split(",");
					 String ss="";
					 String sss="";
					 for(int i=0;i<s.length;i++){
						 ss+=" t.mon_area like '%"+s[i]+"%' or ";
						 sss+=" t.xg_mon_area like '%"+s[i]+"%' or ";
					 }
					 sql+=" and (("+ss.substring(0, ss.length()-3)+") or ("+sss.substring(0, sss.length()-3)+"))";
				 }else{
					 sql+=" and (t.mon_area like '%"+website+"%' or t.xg_mon_area like '%"+website+"%' )";
				 }
			}
		 sql+=" order by t.runplan_type_id,t.broadcast_time,t.language,t.freq "; 
		String fileName="收听记录表";
		String downFileName = "收听记录表";
		try {
			GDSet gd = DbComponent.Query(sql);
			JExcel jExcel = new JExcel();
			downFileName=jExcel.openDocument();
			jExcel.WorkBookGetSheet(0);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					jExcel.addDate(0, i+1,gd.getString(i, "freq"),jExcel.dateCellFormat);
					jExcel.addDate(1, i+1,gd.getString(i, "broadcast_time").split("-")[0],jExcel.dateCellFormat);
					jExcel.addDate(2, i+1,gd.getString(i, "broadcast_time").split("-")[1],jExcel.dateCellFormat);
					jExcel.addDate(3, i+1,gd.getString(i, "language"),jExcel.dateCellFormat);
					jExcel.addDate(4, i+1,gd.getString(i, "station_name"),jExcel.dateCellFormat);
					jExcel.addDate(5, i+1,gd.getString(i, "power"),jExcel.dateCellFormat);
					jExcel.addDate(6, i+1,gd.getString(i, "direction"),jExcel.dateCellFormat);
					jExcel.addDate(7, i+1,gd.getString(i, "service_area"),jExcel.dateCellFormat);
//					jExcel.addDate(8, i+1,gd.getString(i, "ciraf"),jExcel.dateCellFormat);
					jExcel.addDate(8, i+1,"",jExcel.dateCellFormat);
					jExcel.addDate(9, i+1,"",jExcel.dateCellFormat);
					jExcel.addDate(10, i+1,"",jExcel.dateCellFormat);
//					jExcel.addDate(12, i+1,gd.getString(i, "program_type"),jExcel.dateCellFormat);
					jExcel.addDate(11, i+1,"",jExcel.dateCellFormat);
					jExcel.addDate(12, i+1,"",jExcel.dateCellFormat);
					jExcel.addDate(13, i+1,"",jExcel.dateCellFormat);
					jExcel.addDate(14, i+1,"",jExcel.dateCellFormat);
					jExcel.addDate(15, i+1,"",jExcel.dateCellFormat);
					jExcel.addDate(16, i+1,"",jExcel.dateCellFormat);
					jExcel.addDate(17, i+1,"",jExcel.dateCellFormat);
					jExcel.getWorkSheet().setRowView(i+1, 600);
				}
			}
			jExcel.getWorkSheet().setRowView(0, 600);
			jExcel.getWorkSheet().setColumnView(0, 15);
	    	jExcel.getWorkSheet().setColumnView(1, 10);
	    	jExcel.getWorkSheet().setColumnView(2, 10);	
	    	jExcel.getWorkSheet().setColumnView(3, 10);
	    	jExcel.getWorkSheet().setColumnView(4, 10);
	    	jExcel.getWorkSheet().setColumnView(5, 10);	
	    	jExcel.getWorkSheet().setColumnView(6, 10);	
	    	jExcel.getWorkSheet().setColumnView(7, 10);	
	    	jExcel.getWorkSheet().setColumnView(8, 10);
	    	jExcel.getWorkSheet().setColumnView(9, 10);
	    	jExcel.getWorkSheet().setColumnView(10, 10);
//	    	jExcel.getWorkSheet().setColumnView(11, 10);
//	    	jExcel.getWorkSheet().setColumnView(12, 10);
	    	jExcel.getWorkSheet().setColumnView(11, 15);
	    	jExcel.getWorkSheet().setColumnView(12, 10);
	    	jExcel.getWorkSheet().setColumnView(13, 10);
	    	jExcel.getWorkSheet().setColumnView(14, 10);
	    	jExcel.getWorkSheet().setColumnView(15, 10);
	    	jExcel.getWorkSheet().setColumnView(16, 10);
	    	jExcel.getWorkSheet().setColumnView(17, 20);
	    	
	    	jExcel.addDate(0, 0,"频率",jExcel.dateTITLEFormat);
	    	jExcel.addDate(1, 0,"开播时间",jExcel.dateTITLEFormat);
	    	jExcel.addDate(2, 0,"停播时间",jExcel.dateTITLEFormat);
	    	jExcel.addDate(3, 0,"语言",jExcel.dateTITLEFormat);
	    	jExcel.addDate(4, 0,"发射台",jExcel.dateTITLEFormat);
	    	jExcel.addDate(5, 0,"发射功率",jExcel.dateTITLEFormat);
	    	jExcel.addDate(6, 0,"发射方向",jExcel.dateTITLEFormat);
	    	jExcel.addDate(7, 0,"服务区",jExcel.dateTITLEFormat);
//	    	jExcel.addDate(8, 0,"CIRAF区",jExcel.dateTITLEFormat);
	    	jExcel.addDate(8, 0,"收测国家",jExcel.dateTITLEFormat);
	    	jExcel.addDate(9, 0,"收测城市",jExcel.dateTITLEFormat);
	    	jExcel.addDate(10, 0,"资料来源",jExcel.dateTITLEFormat);
//	    	jExcel.addDate(12, 0,"节目类别",jExcel.dateTITLEFormat);
	    	jExcel.addDate(11, 0,"收测日期",jExcel.dateTITLEFormat);
	    	jExcel.addDate(12, 0,"收测时间",jExcel.dateTITLEFormat);
	    	jExcel.addDate(13, 0,"S",jExcel.dateTITLEFormat);
	    	jExcel.addDate(14, 0,"I",jExcel.dateTITLEFormat);
	    	jExcel.addDate(15, 0,"O",jExcel.dateTITLEFormat);
	    	jExcel.addDate(16, 0,"场强",jExcel.dateTITLEFormat);
	    	jExcel.addDate(17, 0,"备注",jExcel.dateTITLEFormat);
	    	jExcel.getWorkSheet().setName("收听记录表");
			jExcel.saveDocument();
	    	response.setContentType("application/vnd.ms-excel");
	        response.setHeader("Location", "Export.xls");
	        response.setHeader("Expires", "0");
	        response.setHeader("Content-Disposition",new String(("filename="+fileName).getBytes("GBK"),"ISO-8859-1")+".xls");
	        OutputStream outputStream = response.getOutputStream();
	        InputStream inputStream = new FileInputStream(downFileName);
	        byte[] buffer = new byte[1024];
	        int i = -1;
	        while ( (i = inputStream.read(buffer)) != -1) {
	          outputStream.write(buffer, 0, i);
	        }
	        outputStream.flush();
	        outputStream.close();
	        outputStream = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 导出Excel
	 * @param msg
	 * @param request
	 * @param response
	 */
	public void doExcel1(String msg,HttpServletRequest request,HttpServletResponse response){
		Element root = StringTool.getXMLRoot(msg);
		String website = root.getChildText("website");
		String sql="select t.freq,t.broadcast_time,t.language,t.redisseminators,t.power,t.direction,t.service_area,t.program_type " +
		           "from zres_freq_time_tab t where t.runplan_type_id=2 and to_date(t.valid_end_date,'yyyy-mm-dd hh24:mi:ss')>sysdate ";
		 if(website!=null&&!website.equalsIgnoreCase("")){
				if(website.indexOf(",")>=0){
					 String[] s = website.split(",");
					 String ss="";
					 String sss="";
					 for(int i=0;i<s.length;i++){
						 ss+=" t.mon_area like '%"+s[i]+"%' or ";
						 sss+=" t.xg_mon_area like '%"+s[i]+"%' or ";
					 }
					 sql+=" and (("+ss.substring(0, ss.length()-3)+") or ("+sss.substring(0, sss.length()-3)+"))";
				 }else{
					 sql+=" and (t.mon_area like '%"+website+"%' or t.xg_mon_area like '%"+website+"%' )";
				 }
			}
		 sql+=" order by t.broadcast_time,t.freq,t.language ";
		String fileName="海外落地收听记录表";
		String downFileName = "海外落地收听记录表";
		try {
			GDSet gd = DbComponent.Query(sql);
			JExcel jExcel = new JExcel();
			downFileName=jExcel.openDocument();
			jExcel.WorkBookGetSheet(0);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					jExcel.addDate(0, i+1,gd.getString(i, "freq"),jExcel.dateCellFormat);
					jExcel.addDate(1, i+1,gd.getString(i, "broadcast_time").split("-")[0],jExcel.dateCellFormat);
					jExcel.addDate(2, i+1,gd.getString(i, "broadcast_time").split("-")[1],jExcel.dateCellFormat);
					jExcel.addDate(3, i+1,gd.getString(i, "language"),jExcel.dateCellFormat);
					jExcel.addDate(4, i+1,gd.getString(i, "redisseminators"),jExcel.dateCellFormat);
					jExcel.addDate(5, i+1,gd.getString(i, "power"),jExcel.dateCellFormat);
					jExcel.addDate(6, i+1,gd.getString(i, "direction"),jExcel.dateCellFormat);
					jExcel.addDate(7, i+1,gd.getString(i, "service_area"),jExcel.dateCellFormat);
					jExcel.addDate(8, i+1,"",jExcel.dateCellFormat);
					jExcel.addDate(9, i+1,"",jExcel.dateCellFormat);
					jExcel.addDate(10, i+1,"",jExcel.dateCellFormat);
//					jExcel.addDate(11, i+1,gd.getString(i, "program_type"),jExcel.dateCellFormat);
					jExcel.addDate(11, i+1,"",jExcel.dateCellFormat);
					jExcel.addDate(12, i+1,"",jExcel.dateCellFormat);
					jExcel.addDate(13, i+1,"",jExcel.dateCellFormat);
					jExcel.addDate(14, i+1,"",jExcel.dateCellFormat);
					jExcel.addDate(15, i+1,"",jExcel.dateCellFormat);
					jExcel.addDate(16, i+1,"",jExcel.dateCellFormat);
					jExcel.addDate(17, i+1,"",jExcel.dateCellFormat);
					jExcel.getWorkSheet().setRowView(i+1, 600);
				}
			}
			jExcel.getWorkSheet().setRowView(0, 600);
			jExcel.getWorkSheet().setColumnView(0, 15);
	    	jExcel.getWorkSheet().setColumnView(1, 10);
	    	jExcel.getWorkSheet().setColumnView(2, 10);	
	    	jExcel.getWorkSheet().setColumnView(3, 10);
	    	jExcel.getWorkSheet().setColumnView(4, 25);
	    	jExcel.getWorkSheet().setColumnView(5, 10);	
	    	jExcel.getWorkSheet().setColumnView(6, 10);	
	    	jExcel.getWorkSheet().setColumnView(7, 10);	
	    	jExcel.getWorkSheet().setColumnView(8, 10);
	    	jExcel.getWorkSheet().setColumnView(9, 10);
	    	jExcel.getWorkSheet().setColumnView(10, 10);
//	    	jExcel.getWorkSheet().setColumnView(11, 10);
	    	jExcel.getWorkSheet().setColumnView(11, 10);
	    	jExcel.getWorkSheet().setColumnView(12, 15);
	    	jExcel.getWorkSheet().setColumnView(13, 10);
	    	jExcel.getWorkSheet().setColumnView(14, 10);
	    	jExcel.getWorkSheet().setColumnView(15, 10);
	    	jExcel.getWorkSheet().setColumnView(16, 10);
	    	jExcel.getWorkSheet().setColumnView(17, 20);
	    	
	    	jExcel.addDate(0, 0,"频率",jExcel.dateTITLEFormat);
	    	jExcel.addDate(1, 0,"开播时间",jExcel.dateTITLEFormat);
	    	jExcel.addDate(2, 0,"停播时间",jExcel.dateTITLEFormat);
	    	jExcel.addDate(3, 0,"语言",jExcel.dateTITLEFormat);
	    	jExcel.addDate(4, 0,"转播机构",jExcel.dateTITLEFormat);
	    	jExcel.addDate(5, 0,"发射功率",jExcel.dateTITLEFormat);
	    	jExcel.addDate(6, 0,"发射方向",jExcel.dateTITLEFormat);
	    	jExcel.addDate(7, 0,"服务区",jExcel.dateTITLEFormat);
	    	jExcel.addDate(8, 0,"收测国家",jExcel.dateTITLEFormat);
	    	jExcel.addDate(9, 0,"收测城市",jExcel.dateTITLEFormat);
	    	jExcel.addDate(10, 0,"资料来源",jExcel.dateTITLEFormat);
//	    	jExcel.addDate(11, 0,"节目类别",jExcel.dateTITLEFormat);
	    	jExcel.addDate(11, 0,"收测日期",jExcel.dateTITLEFormat);
	    	jExcel.addDate(12, 0,"收测时间",jExcel.dateTITLEFormat);
	    	jExcel.addDate(13, 0,"S",jExcel.dateTITLEFormat);
	    	jExcel.addDate(14, 0,"I",jExcel.dateTITLEFormat);
	    	jExcel.addDate(15, 0,"O",jExcel.dateTITLEFormat);
	    	jExcel.addDate(16, 0,"场强",jExcel.dateTITLEFormat);
	    	jExcel.addDate(17, 0,"备注",jExcel.dateTITLEFormat);
	    	jExcel.getWorkSheet().setName("海外落地收听记录表");
			jExcel.saveDocument();
	    	response.setContentType("application/vnd.ms-excel");
	        response.setHeader("Location", "Export.xls");
	        response.setHeader("Expires", "0");
	        response.setHeader("Content-Disposition",new String(("filename="+fileName).getBytes("GBK"),"ISO-8859-1")+".xls");
	        OutputStream outputStream = response.getOutputStream();
	        InputStream inputStream = new FileInputStream(downFileName);
	        byte[] buffer = new byte[1024];
	        int i = -1;
	        while ( (i = inputStream.read(buffer)) != -1) {
	          outputStream.write(buffer, 0, i);
	        }
	        outputStream.flush();
	        outputStream.close();
	        outputStream = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
