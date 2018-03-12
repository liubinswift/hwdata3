package com.viewscenes.web.freqtimetab;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Element;
import org.jmask.web.controller.EXEException;

import com.viewscenes.bean.freqtimetab.HWLDFreqBean;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbComponent.DbQuickExeSQL;
import com.viewscenes.printexcel.JExcel;
import com.viewscenes.pub.GDSet;
import com.viewscenes.pub.GDSetTool;
import com.viewscenes.util.StringTool;

import flex.messaging.io.amf.ASObject;

public class HWLDFreq {

	public String statisticsInfo(HWLDFreqBean bean){
		ASObject resObj=null;
		String language = bean.getLanguage();
		String language_id = bean.getLanguage_id();
		String freq = bean.getFreq();
		String website = bean.getMon_area();
		String seasonType = bean.getSeasonType();
		String delSql = "delete from zres_freq_time_tab t where t.runplan_type_id=2 ";
		
		String sql="select t.start_time||'-'||t.end_time as broadcast_time,t.local_start_time,local_end_time,t.service_area,t.freq,t.launch_country,rct.city as sentcity ," +
				"t.redisseminators,zlt.language_name as language,t.direction,t.mon_area,t.xg_mon_area," +
				"t.valid_start_time,t.valid_end_time,t.rest_datetime,t.summer,t.summer_starttime,t.summer_endtime " +
				" from zres_runplan_tab t,zdic_language_tab zlt,res_city_tab rct where t.runplan_type_id=2 and t.sentcity_id=rct.id " +
				" and t.is_delete=0 and t.language_id=zlt.language_id and t.valid_end_time>=sysdate";
		
		if(language!=null && !language.equalsIgnoreCase("")){
			sql+=" and t.language_id like '%"+language_id+"%'";
			delSql+=" and t.language like '%"+language+"%'";
		}
		if(freq!=null && !freq.equalsIgnoreCase("")){
			sql+=" and t.freq='"+freq+"'";
			delSql+=" and t.freq='"+freq+"'";
		}
		if(seasonType!=null&&!seasonType.equalsIgnoreCase("")){
			sql+=" and t.summer='"+seasonType+"'";
			delSql+=" and t.summer='"+seasonType+"'";
		}
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
				 delSql+=" and ("+ss.substring(0, ss.length()-3)+")";
			 }else{
				 sql+=" and (t.mon_area like '%"+website+"%' or t.xg_mon_area like '%"+website+"%' )";
				 delSql+=" and (t.mon_area like '%"+website+"%' or t.xg_mon_area like '%"+website+"%' ) ";
			 }
		}
		
		StringBuffer insertSql = new StringBuffer("insert into zres_freq_time_tab (BROADCAST_TIME,LOCAL_START_TIME,SERVICE_AREA,FREQ,LAUNCH_COUNTRY,SENTCITY,");
        insertSql.append("REDISSEMINATORS,LANGUAGE,DIRECTION,MON_AREA,XG_MON_AREA,VALID_START_DATE,VALID_START_TIME,");
        insertSql.append("VALID_END_DATE,VALID_END_TIME,REST_DATETIME,SUMMER,SUMMER_STARTTIME,SUMMER_ENDTIME,RUNPLAN_TYPE_ID,LOCAL_END_TIME) ");
        insertSql.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
      
        DbComponent db = new DbComponent();
        DbComponent.DbQuickExeSQL prepExeSQL = db.new DbQuickExeSQL(insertSql.toString());
        prepExeSQL.getConnect();  
        
		try {
			DbComponent.exeUpdate(delSql);//先删除原有的数据
			GDSet gd = DbComponent.Query(sql);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){//新统计的数据入库
					prepExeSQL.setString(1, gd.getString(i, "broadcast_time"));
					prepExeSQL.setString(2, gd.getString(i, "local_start_time"));
					prepExeSQL.setString(3, gd.getString(i, "service_area"));
					prepExeSQL.setInt(4, Integer.parseInt(gd.getString(i, "freq")));
					prepExeSQL.setString(5, gd.getString(i, "launch_country"));
					prepExeSQL.setString(6, gd.getString(i, "sentcity"));
					prepExeSQL.setString(7, gd.getString(i, "redisseminators"));
					prepExeSQL.setString(8, gd.getString(i, "language"));
					prepExeSQL.setString(9, gd.getString(i, "direction"));
					prepExeSQL.setString(10, gd.getString(i, "mon_area"));
					prepExeSQL.setString(11, gd.getString(i, "xg_mon_area"));
					prepExeSQL.setString(12, gd.getString(i, "valid_start_time").split(" ")[0]);
					prepExeSQL.setString(13, gd.getString(i, "valid_start_time").split(" ")[1]);
					prepExeSQL.setString(14, gd.getString(i, "valid_end_time").split(" ")[0]);
					prepExeSQL.setString(15, gd.getString(i, "valid_end_time").split(" ")[1]);
					prepExeSQL.setString(16, gd.getString(i, "rest_datetime"));
					prepExeSQL.setString(17, gd.getString(i, "summer"));
					prepExeSQL.setString(18, gd.getString(i, "summer_starttime"));
					prepExeSQL.setString(19, gd.getString(i, "summer_endtime"));
					prepExeSQL.setInt(20, 2);
					prepExeSQL.setString(21, gd.getString(i, "local_end_time"));
					prepExeSQL.exeSQL();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		
		return "频率时间表成功生成!";
	}
	
	public Object getInfo(HWLDFreqBean bean){
		ASObject resObj=null;
		String language = bean.getLanguage();
		String freq = bean.getFreq();
		String website = bean.getMon_area();
		String sql="select * from zres_freq_time_tab  where runplan_type_id=2";
		if(language!=null && !language.equalsIgnoreCase("")){
			sql+=" and language like '%"+language+"%'";
		}
		if(freq!=null && !freq.equalsIgnoreCase("")){
			sql+=" and freq='"+freq+"'";
		}
		if(website!=null&&!website.equalsIgnoreCase("")){
			if(website.indexOf(",")>=0){
				 String[] s = website.split(",");
				 String ss="";
				 String sss="";
				 for(int i=0;i<s.length;i++){
					 ss+=" mon_area like '%"+s[i]+"%' or ";
					 sss+=" xg_mon_area like '%"+s[i]+"%' or ";
				 }
				 sql+=" and (("+ss.substring(0, ss.length()-3)+") or ("+sss.substring(0, sss.length()-3)+"))";
			 }else{
				 sql+=" and ( mon_area like '%"+website+"%' or xg_mon_area like '%"+website+"%' )";
			 }
		}
		sql+=" order by  broadcast_time ";
		
		try {
			resObj=StringTool.pageQuerySql(sql, bean);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resObj;
	}
	/**
	 * 导出Excel
	 * @param msg
	 * @param request
	 * @param response
	 */
	public void doExcel(String msg,HttpServletRequest request,HttpServletResponse response){
		Element root = StringTool.getXMLRoot(msg);
		String language = root.getChildText("language");
		String freq = root.getChildText("freq");
		String website = root.getChildText("website");
		String seasonType = root.getChildText("seasonType");
		String sql="select t.broadcast_time,t.local_start_time,t.local_end_time,t.service_area,t.freq,t.launch_country,t.sentcity ," +
		"t.redisseminators,t.language,t.direction,t.mon_area,t.xg_mon_area," +
		"t.valid_start_date,t.valid_start_time,t.valid_end_date,t.valid_end_time,t.rest_datetime " +
		" from zres_freq_time_tab t where t.runplan_type_id=2 ";

        if(language!=null && !language.equalsIgnoreCase("")){
	       sql+=" and t.language like '%"+language+"%'";
        }
        if(freq!=null && !freq.equalsIgnoreCase("")){
	       sql+=" and t.freq='"+freq+"'";
        }
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
        if(seasonType!=null&&!seasonType.equalsIgnoreCase("")){
			sql+=" and t.summer='"+seasonType+"'";
		}
        sql+=" order by  t.broadcast_time";
        String fileName="海外落地节目频率时间表";
		String downFileName = "海外落地节目频率时间表";
        try {
        	JExcel jExcel = new JExcel();
			downFileName=jExcel.openDocument();
			jExcel.WorkBookGetSheet(0);
			GDSet gd= DbComponent.Query(sql,GDSetTool.Return_ArrayListRow);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					for(int j=0;j<17;j++){
						jExcel.addDate(j, i+1,(String)gd.getCell(i, j),jExcel.dateCellFormat);
						jExcel.getWorkSheet().setRowView(i+1, 600);
					}
					
				}
			
			}
			jExcel.getWorkSheet().setRowView(0, 600);
			
			jExcel.getWorkSheet().setColumnView(0, 15);
	    	jExcel.getWorkSheet().setColumnView(1, 20);
	    	jExcel.getWorkSheet().setColumnView(2, 10);	
	    	jExcel.getWorkSheet().setColumnView(3, 10);
	    	jExcel.getWorkSheet().setColumnView(4, 10);
	    	jExcel.getWorkSheet().setColumnView(5, 10);	
	    	jExcel.getWorkSheet().setColumnView(6, 10);	
	    	jExcel.getWorkSheet().setColumnView(7, 10);	
	    	jExcel.getWorkSheet().setColumnView(8, 10);
	    	jExcel.getWorkSheet().setColumnView(9, 10);
	    	jExcel.getWorkSheet().setColumnView(10, 10);
	    	jExcel.getWorkSheet().setColumnView(11, 15);
	    	jExcel.getWorkSheet().setColumnView(12, 10);
	    	jExcel.getWorkSheet().setColumnView(13, 15);
	    	jExcel.getWorkSheet().setColumnView(14, 10);
	    	jExcel.getWorkSheet().setColumnView(15, 20);
	    	jExcel.getWorkSheet().setColumnView(16, 10);
//	    	jExcel.getWorkSheet().setColumnView(17, 20);
//	    	jExcel.getWorkSheet().setColumnView(18, 20);
	    	
	    	jExcel.addDate(0, 0,"播音时间",jExcel.dateTITLEFormat);
	    	jExcel.addDate(1, 0,"当地开始时间",jExcel.dateTITLEFormat);
	    	jExcel.addDate(2, 0,"当地结束时间",jExcel.dateTITLEFormat);
	    	jExcel.addDate(3, 0,"服务区",jExcel.dateTITLEFormat);
	    	jExcel.addDate(4, 0,"频率(KHZ)",jExcel.dateTITLEFormat);
	    	jExcel.addDate(5, 0,"发射国家",jExcel.dateTITLEFormat);
	    	jExcel.addDate(6, 0,"发射城市",jExcel.dateTITLEFormat);
	    	jExcel.addDate(7, 0,"转播机构",jExcel.dateTITLEFormat);
	    	jExcel.addDate(8, 0,"语言",jExcel.dateTITLEFormat);
	    	jExcel.addDate(9, 0,"方向",jExcel.dateTITLEFormat);
	    	jExcel.addDate(10, 0,"质量收测站点",jExcel.dateTITLEFormat);
	    	jExcel.addDate(11, 0,"效果收测站点",jExcel.dateTITLEFormat);
	    	jExcel.addDate(12, 0,"启用期",jExcel.dateTITLEFormat);
	    	jExcel.addDate(13, 0,"启用时间",jExcel.dateTITLEFormat);
	    	jExcel.addDate(14, 0,"停用期",jExcel.dateTITLEFormat);
	    	jExcel.addDate(15, 0,"停用时间",jExcel.dateTITLEFormat);
	    	jExcel.addDate(16, 0,"休息日期",jExcel.dateTITLEFormat);
//	    	jExcel.addDate(16, 0,"夏令时",jExcel.dateTITLEFormat);
//	    	jExcel.addDate(17, 0,"夏令时启用期",jExcel.dateTITLEFormat);
//	    	jExcel.addDate(18, 0,"夏令时停用期",jExcel.dateTITLEFormat);
	    	jExcel.getWorkSheet().setName("海外落地节目频率时间表");
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		
	}
	/**
	 * 导出Excel
	 * 按转播机构导出格式
	 * @param msg
	 * @param request
	 * @param response
	 */
	public void doExcel1(String msg,HttpServletRequest request,HttpServletResponse response){
		Element root = StringTool.getXMLRoot(msg);
		String language = root.getChildText("language");
		String freq = root.getChildText("freq");
		String website = root.getChildText("website");
		String seasonType = root.getChildText("seasonType");
		String sql="select t.redisseminators,zlt.language_name as language,t.freq,t.start_time||'-'||t.end_time as broadcast_time," +
		"t.power,t.remark from zres_runplan_tab t,zdic_language_tab zlt " +
		"where t.is_delete=0 and t.runplan_type_id=2 and t.language_id=zlt.language_id and t.valid_end_time>=sysdate";

        if(language!=null && !language.equalsIgnoreCase("")){
	       sql+=" and t.language_id ="+language+"";
        }
        if(freq!=null && !freq.equalsIgnoreCase("")){
	       sql+=" and t.freq='"+freq+"'";
        }
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
        if(seasonType!=null&&!seasonType.equalsIgnoreCase("")){
			sql+=" and t.summer='"+seasonType+"'";
		}
        sql+=" order by  broadcast_time";
        String fileName="海外落地节目频率时间表";
		String downFileName = "海外落地节目频率时间表";
        try {
        	JExcel jExcel = new JExcel();
			downFileName=jExcel.openDocument();
			jExcel.WorkBookGetSheet(0);
			GDSet gd= DbComponent.Query(sql,GDSetTool.Return_ArrayListRow);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					for(int j=0;j<6;j++){
						jExcel.addDate(j, i+1,(String)gd.getCell(i, j),jExcel.dateCellFormat);
						jExcel.getWorkSheet().setRowView(i+1, 600);
					}
					
				}
			
			}
			jExcel.getWorkSheet().setRowView(0, 600);
			jExcel.getWorkSheet().setColumnView(0, 15);
	    	jExcel.getWorkSheet().setColumnView(1, 10);
	    	jExcel.getWorkSheet().setColumnView(2, 15);	
	    	jExcel.getWorkSheet().setColumnView(3, 20);
	    	jExcel.getWorkSheet().setColumnView(4, 10);
	    	jExcel.getWorkSheet().setColumnView(5, 20);	
	    	
	    	
	    	jExcel.addDate(0, 0,"转播机构",jExcel.dateTITLEFormat);
	    	jExcel.addDate(1, 0,"语言",jExcel.dateTITLEFormat);
	    	jExcel.addDate(2, 0,"频率(KHZ)",jExcel.dateTITLEFormat);
	    	jExcel.addDate(3, 0,"播音时间",jExcel.dateTITLEFormat);
	    	jExcel.addDate(4, 0,"功率",jExcel.dateTITLEFormat);
	    	jExcel.addDate(5, 0,"备注",jExcel.dateTITLEFormat);
	    	jExcel.getWorkSheet().setName("海外落地节目频率时间表");
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		
	}
	
	/**
	 * 按语言导出Excel
	 * @param msg
	 * @param request
	 * @param response
	 */
	public void doExcel2(String msg,HttpServletRequest request,HttpServletResponse response){
		Element root = StringTool.getXMLRoot(msg);
		String station_id = root.getChildText("station_id");
		String language_id = root.getChildText("language_id");
		String freq = root.getChildText("freq");
		String website =root.getChildText("website");
		String seasonType = root.getChildText("seasonType");
		String sql="select zlt.language_name as language,t.redisseminators,t.freq,t.start_time||'-'||t.end_time as broadcast_time,t.remark " +
		           "from zres_runplan_tab t ,zdic_language_tab zlt where t.is_delete =0 and  t.runplan_type_id=2 and t.language_id=zlt.language_id and t.valid_end_time>=sysdate";
		          

        if(station_id!=null && !station_id.equalsIgnoreCase("")){
	        sql+=" and t.station_id='"+station_id+"'";
        }
        if(language_id!=null && !language_id.equalsIgnoreCase("")){
	       sql+=" and t.language_id ="+language_id+"";
        }
        if(freq!=null && !freq.equalsIgnoreCase("")){
	       sql+=" and t.freq='"+freq+"'";
        }
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
        if(seasonType!=null&&!seasonType.equalsIgnoreCase("")){
			sql+=" and t.summer='"+seasonType+"'";
		}
        
        sql+=" order by  broadcast_time";
        String fileName="海外落地节目频率时间表";
		String downFileName = "海外落地节目频率时间表";
        try {
        	JExcel jExcel = new JExcel();
			downFileName=jExcel.openDocument();
			jExcel.WorkBookGetSheet(0);
			GDSet gd= DbComponent.Query(sql,GDSetTool.Return_ArrayListRow);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					for(int j=0;j<5;j++){
						jExcel.addDate(j, i+1,(String)gd.getCell(i, j),jExcel.dateCellFormat);
						jExcel.getWorkSheet().setRowView(i+1, 600);
					}
				}
			
			}
			jExcel.getWorkSheet().setRowView(0, 600);
			jExcel.getWorkSheet().setColumnView(0, 10);
	    	jExcel.getWorkSheet().setColumnView(1, 20);
	    	jExcel.getWorkSheet().setColumnView(2, 15);	
	    	jExcel.getWorkSheet().setColumnView(3, 20);
	    	jExcel.getWorkSheet().setColumnView(4, 10);
	    	jExcel.addDate(0, 0,"语言",jExcel.dateTITLEFormat);
	    	jExcel.addDate(1, 0,"转播机构",jExcel.dateTITLEFormat);
	    	jExcel.addDate(2, 0,"频率(KHZ)",jExcel.dateTITLEFormat);
	    	jExcel.addDate(3, 0,"播音时间",jExcel.dateTITLEFormat);
	    	jExcel.addDate(4, 0,"备注",jExcel.dateTITLEFormat);
	    	jExcel.getWorkSheet().setName("海外落地节目频率时间表");
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		
	}
}
