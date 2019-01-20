package com.viewscenes.web.freqtimetab;
/**
 * 国际台频率时间表生成类
 */
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Element;
import org.jmask.web.controller.EXEException;
import com.viewscenes.bean.freqtimetab.GJTFreqBean;

import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.printexcel.JExcel;
import com.viewscenes.pub.GDSet;
import com.viewscenes.pub.GDSetTool;
import com.viewscenes.util.StringTool;
import flex.messaging.io.amf.ASObject;

public class GJTFreq {

	public String statisticsInfo(GJTFreqBean bean){
		
		String station_id = bean.getStation_id();
		String language_id = bean.getLanguage_id();
		String language = bean.getLanguage();
		String freq = bean.getFreq();
		String website = bean.getMon_area();
		String season_id = bean.getSeason_id();
		String is_now=bean.getIs_now();
		String delSql = "delete from zres_freq_time_tab t where t.runplan_type_id=1";
		String sql="select t.start_time||'-'||t.end_time as broadcast_time,t.service_area,t.freq,t.station_name as station,t.station_id," +
				"t.power,t.direction,t.transmiter_no,t.antenna,t.mon_area,t.xg_mon_area,t.program_type,t.ciraf," +
				"t.valid_start_time,t.valid_end_time,t.season_id ," +
				"zlt.language_name as language from zres_runplan_tab t,zdic_language_tab zlt where t.runplan_type_id=1 and t.is_delete=0 " +
				"and t.language_id=zlt.language_id ";
		if(is_now.equalsIgnoreCase("0")){
			sql+=" and t.valid_end_time>=sysdate ";
		}
		if(station_id!=null && !station_id.equalsIgnoreCase("")){
			sql+=" and t.station_id='"+station_id+"'";
			delSql+=" and t.station_id='"+station_id+"'";
		}
		if(language_id!=null && !language_id.equalsIgnoreCase("")){
			sql+=" and t.language_id like '%"+language_id+"%'";
			delSql+=" and t.language like'%"+language+"%'";
		}
		if(freq!=null && !freq.equalsIgnoreCase("")){
			sql+=" and t.freq='"+freq+"'";
			delSql+=" and t.freq='"+freq+"'";
		}
		if(season_id!=null&&!season_id.equalsIgnoreCase("")){
			sql+=" and t.season_id like '"+season_id+"' ";
			delSql+=" and t.season_id like '"+season_id+"' ";
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
		StringBuffer insertSql = new StringBuffer("insert into zres_freq_time_tab (BROADCAST_TIME,SERVICE_AREA,FREQ,STATION_NAME,STATION_ID,");
		             insertSql.append("POWER,DIRECTION,LANGUAGE,TRANSMITER_NO,ANTENNA,PROGRAM_TYPE,CIRAF,VALID_START_DATE,VALID_START_TIME,");
		             insertSql.append("VALID_END_DATE,VALID_END_TIME,SEASON_ID,MON_AREA,XG_MON_AREA,RUNPLAN_TYPE_ID) ");
		             insertSql.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		DbComponent db = new DbComponent();
		DbComponent.DbQuickExeSQL prepExeSQL = db.new DbQuickExeSQL(insertSql.toString());
		prepExeSQL.getConnect();             
		try {
			GDSet gd = DbComponent.Query(sql);
			DbComponent.exeUpdate(delSql);//先删除原有的数据
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){//新统计的数据入库
					prepExeSQL.setString(1, gd.getString(i, "broadcast_time"));
					prepExeSQL.setString(2, gd.getString(i, "service_area"));
					prepExeSQL.setInt(3, Integer.parseInt(gd.getString(i, "freq")));
					prepExeSQL.setString(4, gd.getString(i, "station"));
					prepExeSQL.setInt(5, gd.getString(i, "station_id").equalsIgnoreCase("")?0:Integer.parseInt(gd.getString(i, "station_id")));
					prepExeSQL.setInt(6, gd.getString(i, "power").equalsIgnoreCase("")?0:Integer.parseInt(gd.getString(i, "power")));
					prepExeSQL.setString(7, gd.getString(i, "direction"));
					prepExeSQL.setString(8, gd.getString(i, "language"));
					prepExeSQL.setString(9, gd.getString(i, "transmiter_no"));
					prepExeSQL.setString(10, gd.getString(i, "antenna"));
					prepExeSQL.setString(11, gd.getString(i, "program_type"));
					prepExeSQL.setString(12, gd.getString(i, "ciraf"));
					prepExeSQL.setString(13, gd.getString(i, "valid_start_time").split(" ")[0]);
					prepExeSQL.setString(14, gd.getString(i, "valid_start_time").split(" ")[1]);
					prepExeSQL.setString(15, gd.getString(i, "valid_end_time").split(" ")[0]);
					prepExeSQL.setString(16, gd.getString(i, "valid_end_time").split(" ")[1]);
					prepExeSQL.setString(17, gd.getString(i, "season_id"));
					prepExeSQL.setString(18, gd.getString(i, "mon_area"));
					prepExeSQL.setString(19, gd.getString(i, "xg_mon_area"));
					prepExeSQL.setInt(20, 1);
					prepExeSQL.exeSQL();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		finally{
	    	prepExeSQL.closeConnect();
	    }
		return "频率时间表成功生成!";
	}
	
	public Object getInfo(GJTFreqBean bean){
		String station_id = bean.getStation_id();
		String language = bean.getLanguage();
		String freq = bean.getFreq();
		String website = bean.getMon_area();
		String season_id = bean.getSeason_id();
		ASObject resObj=null;
		String sql="select * from zres_freq_time_tab t where t.runplan_type_id=1 and t.valid_end_date>=to_char(sysdate,'yyyy-MM-dd HH24:mi:ss') ";
		if(station_id!=null && !station_id.equalsIgnoreCase("")){
			sql+=" and t.station_id='"+station_id+"'";
		}
		if(language!=null && !language.equalsIgnoreCase("")){
			sql+=" and t.language like '%"+language+"%'";
		}
		if(freq!=null && !freq.equalsIgnoreCase("")){
			sql+=" and t.freq='"+freq+"'";
		}
		if(season_id!=null&&!season_id.equalsIgnoreCase("")){
			sql+=" and t.season_id like '"+season_id+"' ";
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
		sql+=" order by  t.broadcast_time ";
		try {
			resObj=StringTool.pageQuerySql(sql, bean);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("","频率时间表查询异常："+e.getMessage(),"");
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
		String station_id = root.getChildText("station_id");
		String language = root.getChildText("language");
		String freq = root.getChildText("freq");
		String website = root.getChildText("website");
		String season_id = root.getChildText("season_id");
		String sql="select broadcast_time,t.service_area,t.freq,t.station_name," +
		           "t.power,t.direction,t.language,t.transmiter_no,t.antenna,t.mon_area,t.xg_mon_area,t.program_type,t.ciraf," +
		           "t.valid_start_date,t.valid_start_time,t.valid_end_date,t.valid_end_time,t.season_id " +
		           "from zres_freq_time_tab t where t.runplan_type_id=1 and t.valid_end_date>=to_char(sysdate,'yyyy-MM-dd HH24:mi:ss')";
		          

        if(station_id!=null && !station_id.equalsIgnoreCase("")){
	        sql+=" and t.station_id='"+station_id+"'";
        }
        if(language!=null && !language.equalsIgnoreCase("")){
	       sql+=" and t.language like '%"+language+"%'";
        }
        if(freq!=null && !freq.equalsIgnoreCase("")){
	       sql+=" and t.freq='"+freq+"'";
        }
        if(season_id!=null&&!season_id.equalsIgnoreCase("")){
			sql+=" and t.season_id like '"+season_id+"' ";
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
        sql+=" order by  t.broadcast_time ";
        String fileName="国际台频率时间表";
		String downFileName = "国际台频率时间表";
        try {
        	JExcel jExcel = new JExcel();
			downFileName=jExcel.openDocument();
			jExcel.WorkBookGetSheet(0);
			GDSet gd= DbComponent.Query(sql,GDSetTool.Return_ArrayListRow);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					for(int j=0;j<18;j++){
						jExcel.addDate(j, i+1,(String)gd.getCell(i, j),jExcel.dateCellFormat);
						jExcel.getWorkSheet().setRowView(i+1, 600);
					}
//					jExcel.addDate(0, i+1,gd.getString(i, "broadcast_time"),jExcel.dateCellFormat);
//					jExcel.addDate(1, i+1,gd.getString(i, "service_area"),jExcel.dateCellFormat);
//					jExcel.addDate(2, i+1,gd.getString(i, "freq"),jExcel.dateCellFormat);
//					jExcel.addDate(3, i+1,gd.getString(i, "station"),jExcel.dateCellFormat);
//					jExcel.addDate(4, i+1,gd.getString(i, "power"),jExcel.dateCellFormat);
//					jExcel.addDate(5, i+1,gd.getString(i, "direction"),jExcel.dateCellFormat);
//					jExcel.addDate(6, i+1,gd.getString(i, "language"),jExcel.dateCellFormat);
//					jExcel.addDate(7, i+1,gd.getString(i, "transmiter_no"),jExcel.dateCellFormat);
//					jExcel.addDate(8, i+1,gd.getString(i, "antenna"),jExcel.dateCellFormat);
//					jExcel.addDate(9, i+1,gd.getString(i, "ykz"),jExcel.dateCellFormat);
//					jExcel.addDate(10, i+1,gd.getString(i, "program_type"),jExcel.dateCellFormat);
//					jExcel.addDate(11, i+1,gd.getString(i, "ciraf"),jExcel.dateCellFormat);
//					jExcel.addDate(12, i+1,gd.getString(i, "valid_start_time").split(" ")[0],jExcel.dateCellFormat);
//					jExcel.addDate(13, i+1,gd.getString(i, "valid_start_time").split(" ")[1],jExcel.dateCellFormat);
//					jExcel.addDate(14, i+1,gd.getString(i, "valid_end_time").split(" ")[0],jExcel.dateCellFormat);
//					jExcel.addDate(15, i+1,gd.getString(i, "valid_end_time").split(" ")[1],jExcel.dateCellFormat);
//					jExcel.addDate(16, i+1,gd.getString(i, "season_id"),jExcel.dateCellFormat);
					//jExcel.getWorkSheet().setRowView(i+1, 600);
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
	    	jExcel.getWorkSheet().setColumnView(11, 10);
	    	jExcel.getWorkSheet().setColumnView(12, 10);
	    	jExcel.getWorkSheet().setColumnView(13, 15);
	    	jExcel.getWorkSheet().setColumnView(14, 10);
	    	jExcel.getWorkSheet().setColumnView(15, 15);
	    	jExcel.getWorkSheet().setColumnView(16, 10);
	    	jExcel.getWorkSheet().setColumnView(17, 10);
	    	
	    	jExcel.addDate(0, 0,"播音时间",jExcel.dateTITLEFormat);
	    	jExcel.addDate(1, 0,"服务区",jExcel.dateTITLEFormat);
	    	jExcel.addDate(2, 0,"频率(KHZ)",jExcel.dateTITLEFormat);
	    	jExcel.addDate(3, 0,"发射台",jExcel.dateTITLEFormat);
	    	jExcel.addDate(4, 0,"功率",jExcel.dateTITLEFormat);
	    	jExcel.addDate(5, 0,"方向",jExcel.dateTITLEFormat);
	    	jExcel.addDate(6, 0,"语言",jExcel.dateTITLEFormat);
	    	jExcel.addDate(7, 0,"发射机号",jExcel.dateTITLEFormat);
	    	jExcel.addDate(8, 0,"天线号",jExcel.dateTITLEFormat);
	    	jExcel.addDate(9, 0,"质量收测站点",jExcel.dateTITLEFormat);
	    	jExcel.addDate(10, 0,"效果收测站点",jExcel.dateTITLEFormat);
	    	jExcel.addDate(11, 0,"节目类型",jExcel.dateTITLEFormat);
	    	jExcel.addDate(12, 0,"CIRAF区",jExcel.dateTITLEFormat);
	    	jExcel.addDate(13, 0,"启用期",jExcel.dateTITLEFormat);
	    	jExcel.addDate(14, 0,"启用时间",jExcel.dateTITLEFormat);
	    	jExcel.addDate(15, 0,"停用期",jExcel.dateTITLEFormat);
	    	jExcel.addDate(16, 0,"停用时间",jExcel.dateTITLEFormat);
	    	jExcel.addDate(17, 0,"季节代号",jExcel.dateTITLEFormat);
	    	jExcel.getWorkSheet().setName("国际台频率时间表");
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
	 * 按发射台导出Excel
	 * @param msg
	 * @param request
	 * @param response
	 */
	public void doExcel1(String msg,HttpServletRequest request,HttpServletResponse response){
		Element root = StringTool.getXMLRoot(msg);
		String station_id = root.getChildText("station_id");
		String language_id = root.getChildText("language_id");
		String freq = root.getChildText("freq");
		String season_id = root.getChildText("season_id");
		String website = root.getChildText("website");
		String sql="select t.station_name,zlt.language_name as language,t.freq,t.start_time||'-'||t.end_time as broadcast_time," +
		           "t.power,t.direction,t.transmiter_no,t.antenna,t.antennatype,t.ciraf," +
		           "t.valid_start_time,t.valid_end_time,t.remark " +
		           "from zres_runplan_tab t ,zdic_language_tab zlt" +
		           " where t.is_delete =0 and t.runplan_type_id=1 and t.language_id=zlt.language_id and t.valid_end_time>=sysdate";
		          

        if(station_id!=null && !station_id.equalsIgnoreCase("")){
	        sql+=" and t.station_id='"+station_id+"'";
        }
        if(language_id!=null && !language_id.equalsIgnoreCase("")){
	       sql+=" and t.language_id ="+language_id+"";
        }
        if(freq!=null && !freq.equalsIgnoreCase("")){
	       sql+=" and t.freq='"+freq+"'";
        }
        if(season_id!=null&&!season_id.equalsIgnoreCase("")){
			sql+=" and t.season_id like '"+season_id+"' ";
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
        sql+=" order by station_name, broadcast_time ";
        String fileName="国际台频率时间表";
		String downFileName = "国际台频率时间表";
        try {
        	JExcel jExcel = new JExcel();
			downFileName=jExcel.openDocument();
			jExcel.WorkBookGetSheet(0);
			GDSet gd= DbComponent.Query(sql,GDSetTool.Return_ArrayListRow);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					for(int j=0;j<13;j++){
						jExcel.addDate(j, i+1,(String)gd.getCell(i, j),jExcel.dateCellFormat);
						jExcel.getWorkSheet().setRowView(i+1, 600);
					}
				}
			
			}
			jExcel.getWorkSheet().setRowView(0, 600);
			jExcel.getWorkSheet().setColumnView(0, 15);
	    	jExcel.getWorkSheet().setColumnView(1, 10);
	    	jExcel.getWorkSheet().setColumnView(2, 10);	
	    	jExcel.getWorkSheet().setColumnView(3, 20);
	    	jExcel.getWorkSheet().setColumnView(4, 10);
	    	jExcel.getWorkSheet().setColumnView(5, 10);	
	    	jExcel.getWorkSheet().setColumnView(6, 10);	
	    	jExcel.getWorkSheet().setColumnView(7, 10);	
	    	jExcel.getWorkSheet().setColumnView(8, 10);
	    	jExcel.getWorkSheet().setColumnView(9, 10);
	    	jExcel.getWorkSheet().setColumnView(10, 20);
	    	jExcel.getWorkSheet().setColumnView(11, 20);
	    	jExcel.getWorkSheet().setColumnView(12, 10);
	    	
	    	jExcel.addDate(0, 0,"发射台",jExcel.dateTITLEFormat);
	    	jExcel.addDate(1, 0,"语言",jExcel.dateTITLEFormat);
	    	jExcel.addDate(2, 0,"频率(KHZ)",jExcel.dateTITLEFormat);
	    	jExcel.addDate(3, 0,"播音时间",jExcel.dateTITLEFormat);
	    	jExcel.addDate(4, 0,"功率",jExcel.dateTITLEFormat);
	    	jExcel.addDate(5, 0,"方向",jExcel.dateTITLEFormat);
	    	jExcel.addDate(6, 0,"发射机号",jExcel.dateTITLEFormat);
	    	jExcel.addDate(7, 0,"天线号",jExcel.dateTITLEFormat);
	    	jExcel.addDate(8, 0,"天线程式",jExcel.dateTITLEFormat);
	    	jExcel.addDate(9, 0,"CIRAF区",jExcel.dateTITLEFormat);
	    	jExcel.addDate(10, 0,"启用期",jExcel.dateTITLEFormat);
	    	jExcel.addDate(11, 0,"停用期",jExcel.dateTITLEFormat);
	    	jExcel.addDate(12, 0,"备注",jExcel.dateTITLEFormat);
	    	jExcel.getWorkSheet().setName("国际台频率时间表");
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
		String season_id = root.getChildText("season_id");
		String website =root.getChildText("website");
		String sql="select zlt.language_name as language,t.start_time||'-'||t.end_time as broadcast_time,t.freq,t.remark " +
		           "from zres_runplan_tab t ,zdic_language_tab zlt where t.is_delete =0 and t.runplan_type_id=1 and t.language_id=zlt.language_id and t.valid_end_time>=sysdate";
		          

        if(station_id!=null && !station_id.equalsIgnoreCase("")){
	        sql+=" and t.station_id='"+station_id+"'";
        }
        if(language_id!=null && !language_id.equalsIgnoreCase("")){
	       sql+=" and t.language_id ="+language_id+"";
        }
        if(freq!=null && !freq.equalsIgnoreCase("")){
	       sql+=" and t.freq='"+freq+"'";
        }
        if(season_id!=null&&!season_id.equalsIgnoreCase("")){
			sql+=" and t.season_id like '"+season_id+"' ";
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
        sql+=" order by language, broadcast_time ";
        String fileName="国际台频率时间表";
		String downFileName = "国际台频率时间表";
        try {
        	JExcel jExcel = new JExcel();
			downFileName=jExcel.openDocument();
			jExcel.WorkBookGetSheet(0);
			GDSet gd= DbComponent.Query(sql,GDSetTool.Return_ArrayListRow);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					for(int j=0;j<4;j++){
						jExcel.addDate(j, i+1,(String)gd.getCell(i, j),jExcel.dateCellFormat);
						jExcel.getWorkSheet().setRowView(i+1, 600);
					}
				}
			
			}
			jExcel.getWorkSheet().setRowView(0, 600);
			jExcel.getWorkSheet().setColumnView(0, 10);
	    	jExcel.getWorkSheet().setColumnView(1, 20);
	    	jExcel.getWorkSheet().setColumnView(2, 15);	
	    	jExcel.getWorkSheet().setColumnView(3, 10);
	    	
	    	jExcel.addDate(0, 0,"语言",jExcel.dateTITLEFormat);
	    	jExcel.addDate(1, 0,"播音时间",jExcel.dateTITLEFormat);
	    	jExcel.addDate(2, 0,"频率(KHZ)",jExcel.dateTITLEFormat);
	    	jExcel.addDate(3, 0,"备注",jExcel.dateTITLEFormat);
	    	
	    	jExcel.getWorkSheet().setName("国际台频率时间表");
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
