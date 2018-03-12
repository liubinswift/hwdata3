package com.viewscenes.web.runplanManager.splitRunplan;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Element;
import org.jmask.web.controller.EXEException;

import com.viewscenes.bean.runplan.GJTLDRunplanBean;
import com.viewscenes.bean.runplan.GJTRunplanBean;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.dao.database.DbComponent.DbQuickExeSQL;
import com.viewscenes.printexcel.JExcel;
import com.viewscenes.pub.GDSet;
import com.viewscenes.pub.GDSetException;
import com.viewscenes.util.StringTool;

import flex.messaging.io.amf.ASObject;
import com.viewscenes.web.common.Common;

/**
 * 质量收测运行图拆分类
 * @author leeo
 *
 */
public class SplitRunplan {
	
	Common common = new Common();
	/**
	 *拆分质量国际台运行图
	 * @param runplanType
	 * @return
	 * @throws GDSetException 
	 */
	public Object ykzSplitRunplan(GJTRunplanBean gjtbean) throws GDSetException{
		ArrayList result = new ArrayList();
		String codes = gjtbean.getMon_area();
		String season = gjtbean.getSeason_id();
		String user = gjtbean.getInput_person();
		String delsql="update zres_runplan_chaifen_tab t set t.is_delete=1,input_person='"+user+"',store_datetime=sysdate where 1=1 and t.xg_mon_area is null and t.mon_area is not null ";
		try {
			if(season!=null&&!season.equalsIgnoreCase("")){//根据季节代号的有效期查询运行图
				delsql+=" and t.season_id='"+season+"'";
//				GDSet gd1=Common.getTimeBySeason(season);
//				if(gd1.getRowCount()>0){
//						delsql+=" and t.valid_start_time>=to_date('"+gd1.getString(0, "start_time")+"','yyyy-mm-dd hh24:mi:ss') ";
//						delsql+=" and t.valid_start_time<=to_date('"+gd1.getString(0, "end_time")+"','yyyy-mm-dd hh24:mi:ss') ";
//				}
			}
			
			if(codes!=null&&!codes.equalsIgnoreCase("")){
				delsql+=" and '"+codes+"' like '%'||mon_area||'%'";
			}
			DbComponent.exeUpdate(delsql);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return new EXEException("","拆分本季运行图前删除本季运行图报错："+e1.getMessage(),"");
		}
		
		StringBuffer sbf = new StringBuffer("");
		String sql = "select t.*,zlt.language_name as language  from zres_runplan_tab t,zdic_language_tab zlt  " +
	                "where t.language_id=zlt.language_id(+)  and t.runplan_type_id='1' and t.mon_area is not null and t.is_delete=0 and t.valid_end_time>=sysdate  ";
       // "where t.language_id=zlt.language_id(+)  and t.runplan_type_id='1' and t.mon_area is not null and t.is_delete=0 and t.valid_end_time>=sysdate and t.valid_start_time<=sysdate  ";
		
		if(season!=null&&!season.equalsIgnoreCase("")){//根据季节代号的有效期查询运行图
			sql+=" and t.season_id='"+season+"'";
//			GDSet gd1=Common.getTimeBySeason(season);
//			if(gd1.getRowCount()>0){
//					sql+=" and t.valid_start_time>=to_date('"+gd1.getString(0, "start_time")+"','yyyy-mm-dd hh24:mi:ss') ";
//					sql+=" and t.valid_start_time<=to_date('"+gd1.getString(0, "end_time")+"','yyyy-mm-dd hh24:mi:ss') ";
//			}
		}
		if(!codes.equalsIgnoreCase("")&&codes!=null){
			
			sbf.append("(");
			if(codes.indexOf(",")>0){
				String[] s = codes.split(",");
				for(int i=0;i<s.length;i++){
					
					if(i==s.length-1){
						sbf.append(" t.mon_area like '%"+s[i]+"%')");
						break;
					}else{
						sbf.append(" t.mon_area like '%"+s[i]+"%' or ");
					}
				}
			}else{
				sbf.append(" t.mon_area like '%"+codes+"%')");
			}
				
		}
		if(sbf.length()>0){
			sql+=" and "+sbf.toString();
		}
//		if(codes.indexOf(",")>0){
//			for(int j=0;j<codes.split(",").length;j++){
//				if(j==codes.split(",").length-1){
//					break;
//				}else
//				sql+=" union "+sql;
//			}
//		}
		
		
		ASObject resObj;
		try {
			resObj=StringTool.pageQuerySql(sql, gjtbean);
			ArrayList list =(ArrayList) resObj.get("resultList") ;//取出对象的结果集
			if(list.size()>0){
					for(int i=0;i<list.size();i++){
						String classpath = GJTRunplanBean.class.getName();
						GJTRunplanBean bean = (GJTRunplanBean) StringTool.convertObjToBean((ASObject) list.get(i),classpath);
						bean.setXg_mon_area("");
						if(bean.getMon_area()!=null&&!bean.getMon_area().equalsIgnoreCase("")){
							if(bean.getMon_area().indexOf(",")>=0){
								String[] mon_area = bean.getMon_area().split(",");
								for(int j=0;j<mon_area.length;j++){
									if(codes!=null&&!codes.equalsIgnoreCase("")){
								    	if(codes.indexOf(mon_area[j])<0){
								    		continue;
								    	}
								    }
										GJTRunplanBean newbean = (GJTRunplanBean) StringTool.convertObjToBean((ASObject) list.get(i),classpath);
										newbean.setXg_mon_area("");
										newbean.setMon_area(mon_area[j]);
										result.add(newbean);
									
								}
							}else{
								
								result.add(bean);
								continue;
							}
							
						}
						if(bean.getMon_area().equalsIgnoreCase("")){
							result.add(bean);
						}
					}
				}
				resObj.put("resultList", result);
				
				resObj.put("resultTotal",result.size());
				addRunplan(resObj);
		} catch (Exception e) {
			e.printStackTrace();
			return new EXEException("","质量收测运行图拆分异常："+e.getMessage(),"");
			
		}
		
		return resObj;
	}
	
	/**
	 *小换频拆分质量国际台运行图
	 * @param runplanType
	 * @return
	 * @throws GDSetException 
	 */
	public Object HpSplitRunplan(GJTRunplanBean gjtbean) throws GDSetException{
		ArrayList result = new ArrayList();
		String codes = gjtbean.getMon_area();
		String season = gjtbean.getSeason_id();
		String starttime = gjtbean.getValid_start_time();
		String endtime = gjtbean.getValid_end_time();
		String delsql="update zres_runplan_chaifen_tab t set t.is_delete=1 where 1=1 and t.xg_mon_area is null and t.mon_area is not null ";
		try {
			if(season!=null&&!season.equalsIgnoreCase("")){//根据季节代号的有效期查询运行图
				delsql+=" and t.season_id='"+season+"'";
			}
			if(starttime.equalsIgnoreCase(endtime)){
				delsql+=" and to_char(t.store_datetime,'yyyy-mm-dd')='"+starttime+"' ";
			}else{
				delsql+=" and t.store_datetime>=to_date('"+starttime+"','yyyy-mm-dd') and t.store_datetime<=to_date('"+endtime+"','yyyy-mm-dd') ";
			}
			if(codes!=null&&!codes.equalsIgnoreCase("")){
				delsql+=" and '"+codes+"' like '%'||mon_area||'%'";
			}
			DbComponent.exeUpdate(delsql);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return new EXEException("","拆分本季运行图前删除本季运行图报错："+e1.getMessage(),"");
		}
		
		StringBuffer sbf = new StringBuffer("");
		String sql = "select t.*,zlt.language_name as language  from zres_runplan_tab t,zdic_language_tab zlt  " +
	                "where t.language_id=zlt.language_id(+)  and t.runplan_type_id='1' and t.mon_area is not null and t.is_delete=0 ";
		if(starttime.equalsIgnoreCase(endtime)){
			sql+=" and to_char(t.store_datetime,'yyyy-mm-dd')='"+starttime+"' ";
		}else
		sql+=" and t.store_datetime>=to_date('"+starttime+"','yyyy-mm-dd') and t.store_datetime<=to_date('"+endtime+"','yyyy-mm-dd') ";
		if(season!=null&&!season.equalsIgnoreCase("")){
			sql+=" and t.season_id='"+season+"' ";
		}
		if(!codes.equalsIgnoreCase("")&&codes!=null){
			
			sbf.append("(");
			if(codes.indexOf(",")>0){
				String[] s = codes.split(",");
				for(int i=0;i<s.length;i++){
					
					if(i==s.length-1){
						sbf.append(" t.mon_area like '%"+s[i]+"%')");
						break;
					}else{
						sbf.append(" t.mon_area like '%"+s[i]+"%' or ");
					}
				}
			}else{
				sbf.append(" t.mon_area like '%"+codes+"%')");
			}
				
		}
		if(sbf.length()>0){
			sql+=" and "+sbf.toString();
		}
//		if(codes.indexOf(",")>0){
//			for(int j=0;j<codes.split(",").length;j++){
//				if(j==codes.split(",").length-1){
//					break;
//				}else
//				sql+=" union "+sql;
//			}
//		}
//		
		
		ASObject resObj;
		try {
			resObj=StringTool.pageQuerySql(sql, gjtbean);
			ArrayList list =(ArrayList) resObj.get("resultList") ;//取出对象的结果集
			if(list.size()>0){
					for(int i=0;i<list.size();i++){
						String classpath = GJTRunplanBean.class.getName();
						GJTRunplanBean bean = (GJTRunplanBean) StringTool.convertObjToBean((ASObject) list.get(i),classpath);
						bean.setXg_mon_area("");
						if(bean.getMon_area()!=null&&!bean.getMon_area().equalsIgnoreCase("")){
							if(bean.getMon_area().indexOf(",")>=0){
								String[] mon_area = bean.getMon_area().split(",");
								for(int j=0;j<mon_area.length;j++){
									if(codes!=null&&!codes.equalsIgnoreCase("")){
								    	if(codes.indexOf(mon_area[j])<0){
								    		continue;
								    	}
								    }
										GJTRunplanBean newbean = (GJTRunplanBean) StringTool.convertObjToBean((ASObject) list.get(i),classpath);
										newbean.setMon_area(mon_area[j]);
										
										result.add(newbean);
									
								}
							}else{
								result.add(bean);
								continue;
							}
							
						}
						if(bean.getMon_area().equalsIgnoreCase("")){
							result.add(bean);
						}
					}
				}
				resObj.put("resultList", result);
				
				resObj.put("resultTotal",result.size());
				addRunplan(resObj);
		} catch (Exception e) {
			e.printStackTrace();
			return new EXEException("","小换频质量收测运行图拆分异常："+e.getMessage(),"");
			
		}
		
		return resObj;
	}
	/**
	 * 国际台运行图拆分查询
	 * @param gjtbean
	 * @return
	 */
	public Object querySplitRunplan(GJTRunplanBean gjtbean){
		ASObject resObj;
		String codes = gjtbean.getMon_area();
		String season = gjtbean.getSeason_id();
		String remark = gjtbean.getRemark();//xg:效果收测；zl:质量收测
		StringBuffer sbf = new StringBuffer("");
		ArrayList result = new ArrayList();
		String column="";
		String sql="select t.* ,zlt.language_name as language from zres_runplan_chaifen_tab t,zdic_language_tab zlt" +
				" where t.is_delete=0 and t.runplan_type_id=1 and t.language_id=zlt.language_id(+) ";
		try{
			if(remark.equalsIgnoreCase("xg")){
				sql+=" and t.xg_mon_area is not null";
				column="xg_mon_area";
			}
			if(remark.equalsIgnoreCase("zl")){
				sql+=" and t.mon_area is not null";
				column="mon_area";
			}
			if(season!=null&&!season.equalsIgnoreCase("")){//根据季节代号的有效期查询运行图
				sql+=" and t.season_id='"+season+"' ";
//				GDSet gd1=Common.getTimeBySeason(season);
//				if(gd1.getRowCount()>0){
//						sql+=" and t.valid_start_time>=to_date('"+gd1.getString(0, "start_time")+"','yyyy-mm-dd hh24:mi:ss') ";
//						sql+=" and t.valid_start_time<=to_date('"+gd1.getString(0, "end_time")+"','yyyy-mm-dd hh24:mi:ss') ";
//				}
			}
			if(!codes.equalsIgnoreCase("")&&codes!=null){
				sql+=" and '"+codes+"' like '%'||t."+column+"||'%' ";
				
			}
			String sql1 = "select * from ("+sql+") order by "+column+" ";
			resObj = StringTool.pageQuerySql(sql1, gjtbean);
			ArrayList list =(ArrayList) resObj.get("resultList") ;//取出对象的结果集
			 if(list.size()>0){
				 for(int i=0;i<list.size();i++){
					 String classpath = GJTRunplanBean.class.getName();
					 GJTRunplanBean bean1 = (GJTRunplanBean) StringTool.convertObjToBean((ASObject) list.get(i),classpath);
					 if(bean1.getMon_area()!=null&&!bean1.getMon_area().equalsIgnoreCase("")){
						 String mon_area = bean1.getMon_area();
						 String[] s = mon_area.split(",");
						 if(s.length>0){
							 String mon_name="";
							 for(int j=0;j<s.length;j++){
								 mon_name+=Common.getShortNameByCode(s[j])+",";
							 }
							 bean1.setMon_area(mon_name.substring(0, mon_name.length()-1));
						 }
					 }
					 if(bean1.getXg_mon_area()!=null&&!bean1.getXg_mon_area().equalsIgnoreCase("")){
						 String xg_mon_area = bean1.getXg_mon_area();
						 String[] s = xg_mon_area.split(",");
						 if(s.length>0){
							 String xg_mon_name="";
							 for(int j=0;j<s.length;j++){
								 xg_mon_name+=Common.getShortNameByCode(s[j])+",";
							 }
							 bean1.setXg_mon_area(xg_mon_name.substring(0, xg_mon_name.length()-1));
						 }
					 }
					 result.add(bean1);
				 }
				 resObj.put("resultList", result);
			 }
		}catch(Exception e){
			e.printStackTrace();
			return new EXEException("","运行图拆分查询异常："+e.getMessage(),"");
		}
		
		return resObj;
	}
	
	
	/**
	 * 国际台运行图拆分查询
	 * @param gjtbean
	 * @return
	 */
	public Object queryHpSplitRunplan(GJTRunplanBean gjtbean){
		ASObject resObj;
		String codes = gjtbean.getMon_area();
		String season = gjtbean.getSeason_id();
		String remark = gjtbean.getRemark();//xg:效果收测；zl:质量收测
		String starttime = gjtbean.getValid_start_time();
		String endtime = gjtbean.getValid_end_time();
		StringBuffer sbf = new StringBuffer("");
		ArrayList result = new ArrayList();
		String column="";
		String sql="select t.* ,zlt.language_name as language from zres_runplan_chaifen_tab t,zdic_language_tab zlt" +
				" where t.is_delete=0 and t.runplan_type_id=1 and t.language_id=zlt.language_id(+) ";
		try{
			if(remark.equalsIgnoreCase("xg")){
				sql+=" and t.xg_mon_area is not null";
				column="xg_mon_area";
			}
			if(remark.equalsIgnoreCase("zl")){
				sql+=" and t.mon_area is not null";
				column="mon_area";
			}
			if(season!=null&&!season.equalsIgnoreCase("")){//根据季节代号的有效期查询运行图
				sql+=" and t.season_id='"+season+"' ";
//				GDSet gd1=Common.getTimeBySeason(season);
//				if(gd1.getRowCount()>0){
//						sql+=" and t.valid_start_time>=to_date('"+gd1.getString(0, "start_time")+"','yyyy-mm-dd hh24:mi:ss') ";
//						sql+=" and t.valid_start_time<=to_date('"+gd1.getString(0, "end_time")+"','yyyy-mm-dd hh24:mi:ss') ";
//				}
			}
			if(!codes.equalsIgnoreCase("")&&codes!=null){
				sql+=" and '"+codes+"' like '%'||t."+column+"||'%' ";
				
			}
			if(starttime.equalsIgnoreCase(endtime)){
				sql+=" and to_char(t.store_datetime,'yyyy-mm-dd')='"+starttime+"' ";
			}else{
				sql+=" and t.store_datetime>=to_date('"+starttime+"','yyyy-mm-dd') and t.store_datetime<=to_date('"+endtime+"','yyyy-mm-dd') ";
			}
			String sql1 = "select * from ("+sql+") order by "+column+" ";
			resObj = StringTool.pageQuerySql(sql1, gjtbean);
			ArrayList list =(ArrayList) resObj.get("resultList") ;//取出对象的结果集
			 if(list.size()>0){
				 for(int i=0;i<list.size();i++){
					 String classpath = GJTRunplanBean.class.getName();
					 GJTRunplanBean bean1 = (GJTRunplanBean) StringTool.convertObjToBean((ASObject) list.get(i),classpath);
					 if(bean1.getMon_area()!=null&&!bean1.getMon_area().equalsIgnoreCase("")){
						 String mon_area = bean1.getMon_area();
						 String[] s = mon_area.split(",");
						 if(s.length>0){
							 String mon_name="";
							 for(int j=0;j<s.length;j++){
								 mon_name+=Common.getShortNameByCode(s[j])+",";
							 }
							 bean1.setMon_area(mon_name.substring(0, mon_name.length()-1));
						 }
					 }
					 if(bean1.getXg_mon_area()!=null&&!bean1.getXg_mon_area().equalsIgnoreCase("")){
						 String xg_mon_area = bean1.getXg_mon_area();
						 String[] s = xg_mon_area.split(",");
						 if(s.length>0){
							 String xg_mon_name="";
							 for(int j=0;j<s.length;j++){
								 xg_mon_name+=Common.getShortNameByCode(s[j])+",";
							 }
							 bean1.setXg_mon_area(xg_mon_name.substring(0, xg_mon_name.length()-1));
						 }
					 }
					 result.add(bean1);
				 }
				 resObj.put("resultList", result);
			 }
		}catch(Exception e){
			e.printStackTrace();
			return new EXEException("","运行图拆分查询异常："+e.getMessage(),"");
		}
		
		return resObj;
	}
	
	/**
	 * 海外运行图拆分查询
	 * @param gjtbean
	 * @return
	 */
	public Object querySplitHWRunplan(GJTLDRunplanBean gjtldbean){
		ASObject resObj;
		String codes = gjtldbean.getMon_area();
		String remark = gjtldbean.getRemark();//xg:效果收测；zl:质量收测
		String seasonType = gjtldbean.getSummer();//季节类型
		StringBuffer sbf = new StringBuffer("");
		ArrayList result = new ArrayList();
		String column="";
		String sql="select t.* ,zlt.language_name as language from zres_runplan_chaifen_tab t ,zdic_language_tab zlt " +
				" where t.is_delete=0 and t.runplan_type_id=2 and t.language_id=zlt.language_id(+)";
		try{
			if(remark.equalsIgnoreCase("xg")){
				sql+=" and t.xg_mon_area is not null";
				column="xg_mon_area";
			}
			if(remark.equalsIgnoreCase("zl")){
				sql+=" and t.mon_area is not null";
				column="mon_area";
			}
			if(seasonType!=null&&!seasonType.equalsIgnoreCase("")){
				 sql+=" and summer like '%"+seasonType+"%' ";
			}
			if(!codes.equalsIgnoreCase("")&&codes!=null){
				sql+=" and '"+codes+"' like '%'||t."+column+"||'%' "; 
			}
			String sql1 = "select * from ("+sql+") order by "+column+" ";
			resObj = StringTool.pageQuerySql(sql1, gjtldbean);
			ArrayList list =(ArrayList) resObj.get("resultList") ;//取出对象的结果集
			 if(list.size()>0){
				 for(int i=0;i<list.size();i++){
					 String classpath = GJTLDRunplanBean.class.getName();
					 GJTLDRunplanBean bean1 = (GJTLDRunplanBean) StringTool.convertObjToBean((ASObject) list.get(i),classpath);
					 if(bean1.getMon_area()!=null&&!bean1.getMon_area().equalsIgnoreCase("")){
						 String mon_area = bean1.getMon_area();
						 String[] s = mon_area.split(",");
						 if(s.length>0){
							 String mon_name="";
							 for(int j=0;j<s.length;j++){
								 mon_name+=Common.getShortNameByCode(s[j])+",";
							 }
							 bean1.setMon_area(mon_name.substring(0, mon_name.length()-1));
						 }
					 }
					 if(bean1.getXg_mon_area()!=null&&!bean1.getXg_mon_area().equalsIgnoreCase("")){
						 String xg_mon_area = bean1.getXg_mon_area();
						 String[] s = xg_mon_area.split(",");
						 if(s.length>0){
							 String xg_mon_name="";
							 for(int j=0;j<s.length;j++){
								 xg_mon_name+=Common.getShortNameByCode(s[j])+",";
							 }
							 bean1.setXg_mon_area(xg_mon_name.substring(0, xg_mon_name.length()-1));
						 }
					 }
					 result.add(bean1);
				 }
				 resObj.put("resultList", result);
			 }
			
		}catch(Exception e){
			e.printStackTrace();
			return new EXEException("","运行图拆分查询异常："+e.getMessage(),"");
		}
		
		return resObj;
	}
	
	/**
	 * 运行图添加入库
	 * @param bean
	 * @return
	 * @throws DbException
	 */
	public void addRunplan(ASObject resObj ) throws DbException{
		String sql="insert into zres_runplan_chaifen_tab (runplan_id,station_name,station_id,freq,language_id,start_time,end_time," +
				"valid_start_time,valid_end_time,mon_area,xg_mon_area,runplan_type_id,season_id,transmiter_no," +
				"direction,power,program_type,service_area,input_person,satellite_channel,store_datetime,parent_id,weekday)" +
				"values(zres_runplan_seq.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		DbComponent db = new DbComponent();
	    DbComponent.DbQuickExeSQL prepExeSQL = db.new DbQuickExeSQL(sql);
	    prepExeSQL.getConnect();
	    ArrayList list =(ArrayList) resObj.get("resultList") ;//取出对象的结果集
	    try{
	    	if(list.size()>0){
		    	for(int i=0;i<list.size();i++){
		    		String classpath = GJTRunplanBean.class.getName();
					GJTRunplanBean bean = (GJTRunplanBean) list.get(i);
					prepExeSQL.setString(1, bean.getStation_name()==null?"":bean.getStation_name());
			    	prepExeSQL.setString(2, bean.getStation_id()==null?"":bean.getStation_id());
			    	prepExeSQL.setString(3, bean.getFreq()==null?"":bean.getFreq());
				    prepExeSQL.setString(4, bean.getLanguage_id()==null?"":bean.getLanguage_id());
				    prepExeSQL.setString(5, bean.getStart_time()==null?"":bean.getStart_time());
			    	prepExeSQL.setString(6, bean.getEnd_time()==null?"":bean.getEnd_time());
			    	
				    prepExeSQL.setString(7, bean.getValid_start_time()==null?"":bean.getValid_start_time());
				    prepExeSQL.setString(8, bean.getValid_end_time()==null?"":bean.getValid_end_time());
				    prepExeSQL.setString(9, bean.getMon_area()==null?"":bean.getMon_area());
				    prepExeSQL.setString(10,bean.getXg_mon_area()==null?"":bean.getXg_mon_area());
				    prepExeSQL.setString(11,bean.getRunplan_type_id()==null?"":bean.getRunplan_type_id());
				    prepExeSQL.setString(12,bean.getSeason_id()==null?"":bean.getSeason_id());
				    prepExeSQL.setString(13,bean.getTransmiter_no()==null?"":bean.getTransmiter_no());
				    prepExeSQL.setString(14,bean.getDirection()==null?"":bean.getDirection());
				    prepExeSQL.setString(15,bean.getPower()==null?"":bean.getPower());
				    prepExeSQL.setString(16,bean.getProgram_type()==null?"":bean.getProgram_type());
				    prepExeSQL.setString(17,bean.getService_area()==null?"":bean.getService_area());
				    prepExeSQL.setString(18,bean.getInput_person()==null?"":bean.getInput_person());
				    prepExeSQL.setString(19,bean.getSatellite_channel()==null?"":bean.getSatellite_channel());
				    prepExeSQL.setString(20,bean.getStore_datetime()==null?"":bean.getStore_datetime());
				    prepExeSQL.setString(21,bean.getRunplan_id()==null?"":bean.getRunplan_id());
				    prepExeSQL.setString(22,bean.getWeekday()==null?"":bean.getWeekday());
				    prepExeSQL.exeSQL();
			    }
		    }
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	    
	}
	
	/**
	 *拆分效果国际台运行图
	 * @param runplanType
	 * @return
	 * @throws GDSetException 
	 */
	public Object XGSplitRunplan(GJTRunplanBean gjtbean) throws GDSetException{
		ArrayList result = new ArrayList();
		String codes = gjtbean.getMon_area();
		String season = gjtbean.getSeason_id();
		String user = gjtbean.getInput_person();
		String delsql="update zres_runplan_chaifen_tab t  set t.is_delete=1,input_person='"+user+"',store_datetime=sysdate where 1=1 and t.mon_area is null and t.xg_mon_area is not null ";
		
		try {
			if(season!=null&&!season.equalsIgnoreCase("")){//根据季节代号的有效期查询运行图
				delsql+=" and t.season_id='"+season+"' ";
//				GDSet gd1=Common.getTimeBySeason(season);
//				if(gd1.getRowCount()>0){
//						delsql+=" and t.valid_start_time>=to_date('"+gd1.getString(0, "start_time")+"','yyyy-mm-dd hh24:mi:ss') ";
//						delsql+=" and t.valid_start_time<=to_date('"+gd1.getString(0, "end_time")+"','yyyy-mm-dd hh24:mi:ss') ";
//				}
			}
			
			if(codes!=null&&!codes.equalsIgnoreCase("")){
				delsql+=" and '"+codes+"' like '%'||xg_mon_area||'%'";
			}
			DbComponent.exeUpdate(delsql);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return new EXEException("","拆分本季运行图前删除本季运行图报错："+e1.getMessage(),"");
		}
		StringBuffer sbf = new StringBuffer("");
		String sql = "select t.*,zlt.language_name as language  from zres_runplan_tab t,zdic_language_tab zlt " +
				"where t.language_id=zlt.language_id(+) and t.season_id='"+season+"'  and runplan_type_id='1' and t.xg_mon_area is not null and t.is_delete=0  and t.valid_end_time>=sysdate ";
		if(season!=null&&!season.equalsIgnoreCase("")){//根据季节代号的有效期查询运行图
			sql+=" and t.season_id='"+season+"' ";
//			GDSet gd1=Common.getTimeBySeason(season);
//			if(gd1.getRowCount()>0){
//					sql+=" and t.valid_start_time>=to_date('"+gd1.getString(0, "start_time")+"','yyyy-mm-dd hh24:mi:ss') ";
//					sql+=" and t.valid_start_time<=to_date('"+gd1.getString(0, "end_time")+"','yyyy-mm-dd hh24:mi:ss') ";
//			}
		}
		if(!codes.equalsIgnoreCase("")&&codes!=null){
			
			sbf.append("(");
			if(codes.indexOf(",")>0){
				String[] s = codes.split(",");
				for(int i=0;i<s.length;i++){
					
					if(i==s.length-1){
						sbf.append(" t.xg_mon_area like '%"+s[i]+"%')");
						break;
					}else{
						sbf.append(" t.xg_mon_area like '%"+s[i]+"%' or ");
					}
				}
			}else{
				sbf.append(" t.xg_mon_area like '%"+codes+"%')");
			}
				
		}
		if(sbf.length()>0){
			sql+=" and "+sbf.toString();
		}
//		if(codes.indexOf(",")>0){
//			for(int j=0;j<codes.split(",").length;j++){
//				if(j==codes.split(",").length-1){
//					break;
//				}else
//				sql+=" union "+sql;
//			}
//		}
		ASObject resObj;
		try {
			resObj=StringTool.pageQuerySql(sql, gjtbean);
			
			ArrayList list =(ArrayList) resObj.get("resultList") ;//取出对象的结果集
			if(list.size()>0){
					for(int i=0;i<list.size();i++){
						String classpath = GJTRunplanBean.class.getName();
						GJTRunplanBean bean = (GJTRunplanBean) StringTool.convertObjToBean((ASObject) list.get(i),classpath);
						bean.setMon_area("");
						if(bean.getXg_mon_area()!=null&&!bean.getXg_mon_area().equalsIgnoreCase("")){
							if(bean.getXg_mon_area().indexOf(",")>=0){
								String[] mon_area = bean.getXg_mon_area().split(",");
								for(int j=0;j<mon_area.length;j++){
									    if(codes!=null&&!codes.equalsIgnoreCase("")){
									    	if(codes.indexOf(mon_area[j])<0){
									    		continue;
									    	}
									    }
										GJTRunplanBean newbean = (GJTRunplanBean) StringTool.convertObjToBean((ASObject) list.get(i),classpath);
										newbean.setMon_area("");
										newbean.setXg_mon_area(mon_area[j]);
										result.add(newbean);
									
								}
							}else {
								result.add(bean);
								continue;
							}
							
						}
						if(bean.getXg_mon_area().equalsIgnoreCase("")){
							result.add(bean);
						}
					}
				}
				resObj.put("resultList", result);
				
				resObj.put("resultTotal",result.size());
				//ArrayList list1 =(ArrayList) resObj.get("resultList") ;//取出对象的结果集
				addRunplan(resObj);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new EXEException("","效果收测运行图拆分异常："+e.getMessage(),"");
			
		}
		
		return resObj;
	}
	
	/**
	 *小换频拆分效果国际台运行图
	 * @param runplanType
	 * @return
	 * @throws GDSetException 
	 */
	public Object HpXGSplitRunplan(GJTRunplanBean gjtbean) throws GDSetException{
		ArrayList result = new ArrayList();
		String codes = gjtbean.getMon_area();
		String season = gjtbean.getSeason_id();
		String starttime = gjtbean.getValid_start_time();
		String endtime = gjtbean.getValid_end_time();
		String delsql="update zres_runplan_chaifen_tab t  set t.is_delete=1 where 1=1 and t.mon_area is null and t.xg_mon_area is not null ";
		
		try {
			if(season!=null&&!season.equalsIgnoreCase("")){//根据季节代号的有效期查询运行图
				delsql+=" and t.season_id='"+season+"' ";
			}
			if(starttime.equalsIgnoreCase(endtime)){
				delsql+=" and to_char(t.store_datetime,'yyyy-mm-dd')='"+starttime+"' ";
			}else{
				delsql+=" and t.store_datetime>=to_date('"+starttime+"','yyyy-mm-dd') and t.store_datetime<=to_date('"+endtime+"','yyyy-mm-dd') ";
			}
			if(codes!=null&&!codes.equalsIgnoreCase("")){
				delsql+=" and '"+codes+"' like '%'||xg_mon_area||'%'";
			}
			DbComponent.exeUpdate(delsql);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return new EXEException("","拆分本季运行图前删除本季运行图报错："+e1.getMessage(),"");
		}
		StringBuffer sbf = new StringBuffer("");
		String sql = "select t.*,zlt.language_name as language  from zres_runplan_tab t,zdic_language_tab zlt " +
				"where t.language_id=zlt.language_id(+) and t.season_id='"+season+"'  and runplan_type_id='1' and t.xg_mon_area is not null and t.is_delete=0  ";
		if(starttime.equalsIgnoreCase(endtime)){
			sql+=" and to_char(t.store_datetime,'yyyy-mm-dd')='"+starttime+"' ";
		}else
		sql+=" and t.store_datetime>=to_date('"+starttime+"','yyyy-mm-dd') and t.store_datetime<=to_date('"+endtime+"','yyyy-mm-dd')  ";
		if(season!=null&&!season.equalsIgnoreCase("")){//根据季节代号的有效期查询运行图
			sql+=" and t.season_id='"+season+"' ";
//			GDSet gd1=Common.getTimeBySeason(season);
//			if(gd1.getRowCount()>0){
//				sql+=" and (to_char(t.valid_start_time,'yyyy-mm-dd hh24:mi:ss')!='"+gd1.getString(0, "start_time")+"'";
//				sql+=" or to_char(t.valid_end_time,'yyyy-mm-dd hh24:mi:ss')!='"+gd1.getString(0, "end_time")+"')";
//				//sql+=" and t.valid_end_time>=sysdate and t.valid_start_time<=sysdate ";
//			}
		}
		if(!codes.equalsIgnoreCase("")&&codes!=null){
			
			sbf.append("(");
			if(codes.indexOf(",")>0){
				String[] s = codes.split(",");
				for(int i=0;i<s.length;i++){
					
					if(i==s.length-1){
						sbf.append(" t.xg_mon_area like '%"+s[i]+"%')");
						break;
					}else{
						sbf.append(" t.xg_mon_area like '%"+s[i]+"%' or ");
					}
				}
			}else{
				sbf.append(" t.xg_mon_area like '%"+codes+"%')");
			}
				
		}
		if(sbf.length()>0){
			sql+=" and "+sbf.toString();
		}
//		if(codes.indexOf(",")>0){
//			for(int j=0;j<codes.split(",").length;j++){
//				if(j==codes.split(",").length-1){
//					break;
//				}else
//				sql+=" union "+sql;
//			}
//		}
		ASObject resObj;
		try {
			resObj=StringTool.pageQuerySql(sql, gjtbean);
			
			ArrayList list =(ArrayList) resObj.get("resultList") ;//取出对象的结果集
			if(list.size()>0){
					for(int i=0;i<list.size();i++){
						String classpath = GJTRunplanBean.class.getName();
						GJTRunplanBean bean = (GJTRunplanBean) StringTool.convertObjToBean((ASObject) list.get(i),classpath);
						bean.setMon_area("");
						if(bean.getXg_mon_area()!=null&&!bean.getXg_mon_area().equalsIgnoreCase("")){
							if(bean.getXg_mon_area().indexOf(",")>=0){
								String[] mon_area = bean.getXg_mon_area().split(",");
								for(int j=0;j<mon_area.length;j++){
									    if(codes!=null&&!codes.equalsIgnoreCase("")){
									    	if(codes.indexOf(mon_area[j])<0){
									    		continue;
									    	}
									    }
										GJTRunplanBean newbean = (GJTRunplanBean) StringTool.convertObjToBean((ASObject) list.get(i),classpath);
										newbean.setXg_mon_area(mon_area[j]);
										result.add(newbean);
									
								}
							}else {
								result.add(bean);
								continue;
							}
							
						}
						if(bean.getXg_mon_area().equalsIgnoreCase("")){
							result.add(bean);
						}
					}
				}
				resObj.put("resultList", result);
				
				resObj.put("resultTotal",result.size());
				//ArrayList list1 =(ArrayList) resObj.get("resultList") ;//取出对象的结果集
				addRunplan(resObj);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new EXEException("","小换频效果收测运行图拆分异常："+e.getMessage(),"");
			
		}
		
		return resObj;
	}
	
	/**
	 * 运行图添加入库
	 * @param bean
	 * @return
	 * @throws DbException
	 */
	public void addHWRunplan(ASObject resObj,String flag){
		String sql="insert into zres_runplan_chaifen_tab (runplan_id,redisseminators,freq,language_id,start_time,end_time," +
		"valid_start_time,valid_end_time,mon_area,xg_mon_area,runplan_type_id,sentcity_id,direction,service_area,rest_datetime," +
		"summer,weekday)" +
		" values(zres_runplan_seq.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        DbComponent db = new DbComponent();
        DbComponent.DbQuickExeSQL prepExeSQL = db.new DbQuickExeSQL(sql);
        prepExeSQL.getConnect();
        ArrayList list =(ArrayList) resObj.get("resultList") ;//取出对象的结果集
        try{
	     if(list.size()>0){
    	  for(int i=0;i<list.size();i++){
    		GJTLDRunplanBean bean = (GJTLDRunplanBean) list.get(i);
			prepExeSQL.setString(1, bean.getRedisseminators()==null?"":bean.getRedisseminators());
	    	prepExeSQL.setString(2, bean.getFreq()==null?"":bean.getFreq());
	    	prepExeSQL.setString(3, bean.getLanguage_id()==null?"":bean.getLanguage_id());
		    prepExeSQL.setString(4, bean.getStart_time()==null?"":bean.getStart_time());
	    	prepExeSQL.setString(5, bean.getEnd_time()==null?"":bean.getEnd_time());
	    	
		    prepExeSQL.setString(6, bean.getValid_start_time()==null?"":bean.getValid_start_time());
		    prepExeSQL.setString(7, bean.getValid_end_time()==null?"":bean.getValid_end_time());
		    if(!flag.equalsIgnoreCase("102")){
		    	 prepExeSQL.setString(8, "");
				 prepExeSQL.setString(9,bean.getXg_mon_area()==null?"":bean.getXg_mon_area());
		    }else{
		    	prepExeSQL.setString(8, bean.getMon_area()==null?"":bean.getMon_area());
				prepExeSQL.setString(9,"");
		    }
		   
		    prepExeSQL.setString(10,bean.getRunplan_type_id()==null?"":bean.getRunplan_type_id());
		    prepExeSQL.setString(11,bean.getSentcity_id()==null?"":bean.getSentcity_id());
		    prepExeSQL.setString(12,bean.getDirection()==null?"":bean.getDirection());
		    prepExeSQL.setString(13,bean.getService_area()==null?"":bean.getService_area());
		    prepExeSQL.setString(14,bean.getRest_datetime()==null?"":bean.getRest_datetime());
		    prepExeSQL.setString(15,bean.getSummer()==null?"":bean.getSummer());
		    prepExeSQL.setString(16,bean.getWeekday()==null?"":bean.getWeekday());
		    prepExeSQL.exeSQL();
	    }
      }
      }catch(Exception e){
	    e.printStackTrace();
      }

	}
	
	/**
	 * 导出Excel
	 * @param msg
	 * @param request
	 * @param response
	 */
	public void doExcel(String msg,HttpServletRequest request,HttpServletResponse response){
		Element root = StringTool.getXMLRoot(msg);
		String runplan_type_id = root.getChildText("runplan_type");
		String codes = root.getChildText("codes");
		String season = root.getChildText("season");
		String starttime = root.getChildText("starttime");
		String endtime = root.getChildText("endtime");
		GJTRunplanBean gjtbean = new GJTRunplanBean();
		gjtbean.setStartRow(1);
		gjtbean.setEndRow(10000);
		gjtbean.setMon_area(codes);
//		ArrayList list = new ArrayList();
//		ASObject resObj=(ASObject) ykzSplitRunplan(gjtbean);
//		list =(ArrayList) resObj.get("resultList") ;//取出对象的结果集
		String sql="select t.*,zlt.language_name as language from zres_runplan_chaifen_tab t,zdic_language_tab zlt " +
				"where t.language_id=zlt.language_id(+) and t.runplan_type_id=1 and t.is_delete=0 and t.xg_mon_area is null and t.mon_area is not null ";
		
		String fileName="国际台质量收测运行图拆分";
		String title="国际台质量收测运行图拆分";
		String downFileName = "";
		try {
			//GDSet gd= DbComponent.Query(sql,GDSetTool.Return_ArrayListRow);
			if(season!=null&&!season.equalsIgnoreCase("")){//根据季节代号的有效期查询运行图
				sql+="  and t.season_id='"+season+"'";
//				GDSet gd1=Common.getTimeBySeason(season);
//				if(gd1.getRowCount()>0){
//						sql+=" and t.valid_start_time>=to_date('"+gd1.getString(0, "start_time")+"','yyyy-mm-dd hh24:mi:ss') ";
//						sql+=" and t.valid_start_time<=to_date('"+gd1.getString(0, "end_time")+"','yyyy-mm-dd hh24:mi:ss') ";
//				}
			}
			if(codes!=null&&!codes.equalsIgnoreCase("")){
				sql+=" and '"+codes+"' like '%'||mon_area||'%'";
			}
			if(starttime!=null&&!starttime.equalsIgnoreCase("")){
				if(starttime.equalsIgnoreCase(endtime)){
					sql+=" and to_char(t.store_datetime,'yyyy-mm-dd')='"+starttime+"' ";
				}else{
					sql+=" and t.store_datetime>=to_date('"+starttime+"','yyyy-mm-dd') and t.store_datetime<=to_date('"+endtime+"','yyyy-mm-dd') ";
				}
			}
			
			sql+=" order by mon_area ";
			JExcel jExcel = new JExcel();
			downFileName=jExcel.openDocument();
			jExcel.WorkBookGetSheet(0);
			GDSet gd = DbComponent.Query(sql);
			String systemdate = StringTool.date2String(new Date());
			for(int i=0;i<gd.getRowCount();i++){
					jExcel.addDate(0, i+2,gd.getString(i, "station_name"),jExcel.dateCellFormat);
					jExcel.addDate(1, i+2,gd.getString(i, "transmiter_no"),jExcel.dateCellFormat);
					jExcel.addDate(2, i+2,gd.getString(i, "freq"),jExcel.dateCellFormat);
					jExcel.addDate(3, i+2,gd.getString(i, "antenna"),jExcel.dateCellFormat);
					jExcel.addDate(4, i+2,gd.getString(i, "antennatype"),jExcel.dateCellFormat);
					jExcel.addDate(5, i+2,gd.getString(i, "direction"),jExcel.dateCellFormat);
					jExcel.addDate(6, i+2,gd.getString(i, "language"),jExcel.dateCellFormat);
					jExcel.addDate(7, i+2,gd.getString(i, "power"),jExcel.dateCellFormat);
					jExcel.addDate(8, i+2,gd.getString(i, "program_type"),jExcel.dateCellFormat);
					jExcel.addDate(9, i+2,gd.getString(i, "start_time"),jExcel.dateCellFormat);
					jExcel.addDate(10, i+2,gd.getString(i, "end_time"),jExcel.dateCellFormat);
					jExcel.addDate(11, i+2,gd.getString(i, "valid_start_time"),jExcel.dateCellFormat);
					jExcel.addDate(12, i+2,gd.getString(i, "valid_end_time"),jExcel.dateCellFormat);
					jExcel.addDate(13, i+2,gd.getString(i, "input_person"),jExcel.dateCellFormat);
					jExcel.addDate(14, i+2,gd.getString(i, "mon_area"),jExcel.dateCellFormat);
					jExcel.addDate(15, i+2,gd.getString(i, "station_id"),jExcel.dateCellFormat);
					jExcel.addDate(16, i+2,gd.getString(i, "runplan_id"),jExcel.dateCellFormat);
					jExcel.addDate(17, i+2,gd.getString(i, "season_id"),jExcel.dateCellFormat);
					jExcel.addDate(18, i+2,gd.getString(i, "satellite_channel"),jExcel.dateCellFormat);
					jExcel.addDate(19, i+2,gd.getString(i, "weekday"),jExcel.dateCellFormat);
					jExcel.addDate(20, i+2,systemdate,jExcel.dateCellFormat);
					jExcel.getWorkSheet().setRowView(i+2, 600);
			}
//			for(int i=0;i<list.size();i++){
//				gjtbean = (GJTRunplanBean) list.get(i);
//				//jExcel.addDate(0, i+2,gjtbean.getSentcity(),jExcel.dateCellFormat);
//				jExcel.addDate(0, i+2,gjtbean.getStation_name(),jExcel.dateCellFormat);
//				jExcel.addDate(1, i+2,gjtbean.getTransmiter_no(),jExcel.dateCellFormat);
//				jExcel.addDate(2, i+2,gjtbean.getFreq(),jExcel.dateCellFormat);
//				jExcel.addDate(3, i+2,gjtbean.getAntenna(),jExcel.dateCellFormat);
//				jExcel.addDate(4, i+2,gjtbean.getAntennatype(),jExcel.dateCellFormat);
//				jExcel.addDate(5, i+2,gjtbean.getDirection(),jExcel.dateCellFormat);
//				jExcel.addDate(6, i+2,gjtbean.getLanguage(),jExcel.dateCellFormat);
//				jExcel.addDate(7, i+2,gjtbean.getPower(),jExcel.dateCellFormat);
//				jExcel.addDate(8, i+2,gjtbean.getProgram_type(),jExcel.dateCellFormat);
//				jExcel.addDate(9, i+2,gjtbean.getStart_time(),jExcel.dateCellFormat);
//				jExcel.addDate(10, i+2,gjtbean.getEnd_time(),jExcel.dateCellFormat);
//				jExcel.addDate(11, i+2,gjtbean.getValid_start_time(),jExcel.dateCellFormat);
//				jExcel.addDate(12, i+2,gjtbean.getValid_end_time(),jExcel.dateCellFormat);
//				jExcel.addDate(13, i+2,gjtbean.getInput_person(),jExcel.dateCellFormat);
//				jExcel.addDate(14, i+2,gjtbean.getMon_area(),jExcel.dateCellFormat);
//				jExcel.addDate(15, i+2,gjtbean.getStation_id(),jExcel.dateCellFormat);
//				jExcel.getWorkSheet().setRowView(i+2, 600);
//			}
			
			jExcel.getWorkSheet().setColumnView(0, 10);
	    	jExcel.getWorkSheet().setColumnView(1, 10);
	    	jExcel.getWorkSheet().setColumnView(2, 10);	
	    	jExcel.getWorkSheet().setColumnView(3, 12);
	    	jExcel.getWorkSheet().setColumnView(4, 10);
	    	jExcel.getWorkSheet().setColumnView(5, 10);	
	    	jExcel.getWorkSheet().setColumnView(6, 10);	
	    	jExcel.getWorkSheet().setColumnView(7, 10);	
	    	jExcel.getWorkSheet().setColumnView(8, 10);
	    	jExcel.getWorkSheet().setColumnView(9, 10);
	    	jExcel.getWorkSheet().setColumnView(10, 10);
	    	jExcel.getWorkSheet().setColumnView(11, 20);
	    	jExcel.getWorkSheet().setColumnView(12, 20);
	    	jExcel.getWorkSheet().setColumnView(13, 10);
	    	jExcel.getWorkSheet().setColumnView(14, 20);
	    	jExcel.getWorkSheet().setColumnView(15, 15);
	    	jExcel.getWorkSheet().setColumnView(16, 15);
	    	jExcel.getWorkSheet().setColumnView(17, 15);
	    	jExcel.getWorkSheet().setColumnView(18, 15);
	    	jExcel.getWorkSheet().setColumnView(19, 15);
	    	jExcel.getWorkSheet().setColumnView(20, 20);
	    	//jExcel.getWorkSheet().setColumnView(16, 15);
	    	
	    	jExcel.addDate(0, 0,title,jExcel.repTitleFormat);
	    	jExcel.getWorkSheet().setRowView(0, 800);
	    //	jExcel.addDate(0, 1,"发射城市",jExcel.dateTITLEFormat);
	    	jExcel.addDate(0, 1,"发射台",jExcel.dateTITLEFormat);
	    	jExcel.addDate(1, 1,"机号",jExcel.dateTITLEFormat);
	    	jExcel.addDate(2, 1,"频率(KHZ)",jExcel.dateTITLEFormat);
	    	jExcel.addDate(3, 1,"天线号",jExcel.dateTITLEFormat);
	    	jExcel.addDate(4, 1,"天线程式",jExcel.dateTITLEFormat);
	    	jExcel.addDate(5, 1,"方向",jExcel.dateTITLEFormat);
	    	jExcel.addDate(6, 1,"语言",jExcel.dateTITLEFormat);
	    	jExcel.addDate(7, 1,"功率",jExcel.dateTITLEFormat);
	    	jExcel.addDate(8, 1,"节目类型",jExcel.dateTITLEFormat);
	    	jExcel.addDate(9, 1,"开始时间",jExcel.dateTITLEFormat);
	    	jExcel.addDate(10, 1,"结束时间",jExcel.dateTITLEFormat);
	    	jExcel.addDate(11, 1,"启用期",jExcel.dateTITLEFormat);
	    	jExcel.addDate(12, 1,"停用期",jExcel.dateTITLEFormat);
	    	jExcel.addDate(13, 1,"录入人",jExcel.dateTITLEFormat);
	    	jExcel.addDate(14, 1,"质量收测站点",jExcel.dateTITLEFormat);
	    	jExcel.addDate(15, 1,"发射台ID",jExcel.dateTITLEFormat);
	    	jExcel.addDate(16, 1,"运行图ID",jExcel.dateTITLEFormat);
	    	jExcel.addDate(17, 1,"季节代号",jExcel.dateTITLEFormat);
	    	jExcel.addDate(18, 1,"通道",jExcel.dateTITLEFormat);
	    	jExcel.addDate(19, 1,"休息日期",jExcel.dateTITLEFormat);
	    	jExcel.addDate(20, 1,"导出时间",jExcel.dateTITLEFormat);
	    	jExcel.getWorkSheet().setRowView(1, 600);
	    	jExcel.mergeCells(0,0,20,0);//合并表头
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
	public void doExcelXG(String msg,HttpServletRequest request,HttpServletResponse response){
		Element root = StringTool.getXMLRoot(msg);
		String runplan_type_id = root.getChildText("runplan_type");
		String codes = root.getChildText("codes");
		String season = root.getChildText("season");
		String starttime = root.getChildText("starttime");
		String endtime = root.getChildText("endtime");
//		GJTRunplanBean gjtbean = new GJTRunplanBean();
//		gjtbean.setMon_area(codes);
//		gjtbean.setStartRow(1);
//		gjtbean.setEndRow(10000);
//		ArrayList list = new ArrayList();
//		ASObject resObj=(ASObject) XGSplitRunplan(gjtbean);
//		list =(ArrayList) resObj.get("resultList") ;//取出对象的结果集
		String sql="select t.*,zlt.language_name as language from zres_runplan_chaifen_tab t,zdic_language_tab zlt " +
		"where t.language_id=zlt.language_id(+) and t.runplan_type_id=1 and t.is_delete=0 and t.mon_area is null and t.xg_mon_area is not null  ";
       
		String fileName="国际台效果收测运行图拆分";
		String title="国际台效果收测运行图拆分";
		String downFileName = "";
		try {
			//GDSet gd= DbComponent.Query(sql,GDSetTool.Return_ArrayListRow);
			if(season!=null&&!season.equalsIgnoreCase("")){//根据季节代号的有效期查询运行图
				sql+=" and t.season_id='"+season+"' ";
//				GDSet gd1=Common.getTimeBySeason(season);
//				if(gd1.getRowCount()>0){
//						sql+=" and t.valid_start_time>=to_date('"+gd1.getString(0, "start_time")+"','yyyy-mm-dd hh24:mi:ss') ";
//						sql+=" and t.valid_start_time<=to_date('"+gd1.getString(0, "end_time")+"','yyyy-mm-dd hh24:mi:ss') ";
//				}
			}
			 if(codes!=null&&!codes.equalsIgnoreCase("")){
			     sql+=" and '"+codes+"' like '%'||xg_mon_area||'%'";
		        }
			 if(starttime!=null&&!starttime.equalsIgnoreCase("")){
					if(starttime.equalsIgnoreCase(endtime)){
						sql+=" and to_char(t.store_datetime,'yyyy-mm-dd')='"+starttime+"' ";
					}else{
						sql+=" and t.store_datetime>=to_date('"+starttime+"','yyyy-mm-dd') and t.store_datetime<=to_date('"+endtime+"','yyyy-mm-dd') ";
					}
				}
			 sql+=" order by xg_mon_area ";
			JExcel jExcel = new JExcel();
			downFileName=jExcel.openDocument();
			jExcel.WorkBookGetSheet(0);
			GDSet gd = DbComponent.Query(sql);
			String systemdate = StringTool.date2String(new Date());
			for(int i=0;i<gd.getRowCount();i++){
					jExcel.addDate(0, i+2,gd.getString(i, "station_name"),jExcel.dateCellFormat);
					jExcel.addDate(1, i+2,gd.getString(i, "transmiter_no"),jExcel.dateCellFormat);
					jExcel.addDate(2, i+2,gd.getString(i, "freq"),jExcel.dateCellFormat);
					jExcel.addDate(3, i+2,gd.getString(i, "antenna"),jExcel.dateCellFormat);
					jExcel.addDate(4, i+2,gd.getString(i, "antennatype"),jExcel.dateCellFormat);
					jExcel.addDate(5, i+2,gd.getString(i, "direction"),jExcel.dateCellFormat);
					jExcel.addDate(6, i+2,gd.getString(i, "language"),jExcel.dateCellFormat);
					jExcel.addDate(7, i+2,gd.getString(i, "power"),jExcel.dateCellFormat);
					jExcel.addDate(8, i+2,gd.getString(i, "program_type"),jExcel.dateCellFormat);
					jExcel.addDate(9, i+2,gd.getString(i, "start_time"),jExcel.dateCellFormat);
					jExcel.addDate(10, i+2,gd.getString(i, "end_time"),jExcel.dateCellFormat);
					jExcel.addDate(11, i+2,gd.getString(i, "valid_start_time"),jExcel.dateCellFormat);
					jExcel.addDate(12, i+2,gd.getString(i, "valid_end_time"),jExcel.dateCellFormat);
					jExcel.addDate(13, i+2,gd.getString(i, "input_person"),jExcel.dateCellFormat);
					jExcel.addDate(14, i+2,gd.getString(i, "xg_mon_area"),jExcel.dateCellFormat);
					jExcel.addDate(15, i+2,gd.getString(i, "station_id"),jExcel.dateCellFormat);
					jExcel.addDate(16, i+2,gd.getString(i, "runplan_id"),jExcel.dateCellFormat);
					jExcel.addDate(17, i+2,gd.getString(i, "season_id"),jExcel.dateCellFormat);
					jExcel.addDate(18, i+2,gd.getString(i, "satellite_channel"),jExcel.dateCellFormat);
					jExcel.addDate(19, i+2,gd.getString(i, "weekday"),jExcel.dateCellFormat);
					jExcel.addDate(20, i+2,systemdate,jExcel.dateCellFormat);
					jExcel.getWorkSheet().setRowView(i+2, 600);
			}
//			for(int i=0;i<list.size();i++){
//				gjtbean = (GJTRunplanBean) list.get(i);
//				//jExcel.addDate(0, i+2,gjtbean.getSentcity(),jExcel.dateCellFormat);
//				jExcel.addDate(0, i+2,gjtbean.getStation_name(),jExcel.dateCellFormat);
//				jExcel.addDate(1, i+2,gjtbean.getTransmiter_no(),jExcel.dateCellFormat);
//				jExcel.addDate(2, i+2,gjtbean.getFreq(),jExcel.dateCellFormat);
//				jExcel.addDate(3, i+2,gjtbean.getAntenna(),jExcel.dateCellFormat);
//				jExcel.addDate(4, i+2,gjtbean.getAntennatype(),jExcel.dateCellFormat);
//				jExcel.addDate(5, i+2,gjtbean.getDirection(),jExcel.dateCellFormat);
//				jExcel.addDate(6, i+2,gjtbean.getLanguage(),jExcel.dateCellFormat);
//				jExcel.addDate(7, i+2,gjtbean.getPower(),jExcel.dateCellFormat);
//				jExcel.addDate(8, i+2,gjtbean.getProgram_type(),jExcel.dateCellFormat);
//				jExcel.addDate(9, i+2,gjtbean.getStart_time(),jExcel.dateCellFormat);
//				jExcel.addDate(10, i+2,gjtbean.getEnd_time(),jExcel.dateCellFormat);
//				jExcel.addDate(11, i+2,gjtbean.getValid_start_time(),jExcel.dateCellFormat);
//				jExcel.addDate(12, i+2,gjtbean.getValid_end_time(),jExcel.dateCellFormat);
//				jExcel.addDate(13, i+2,gjtbean.getInput_person(),jExcel.dateCellFormat);
//				jExcel.addDate(14, i+2,gjtbean.getXg_mon_area(),jExcel.dateCellFormat);
//				jExcel.addDate(15, i+2,gjtbean.getStation_id(),jExcel.dateCellFormat);
//				jExcel.getWorkSheet().setRowView(i+2, 600);
//			}
			
			jExcel.getWorkSheet().setColumnView(0, 10);
	    	jExcel.getWorkSheet().setColumnView(1, 10);
	    	jExcel.getWorkSheet().setColumnView(2, 10);	
	    	jExcel.getWorkSheet().setColumnView(3, 12);
	    	jExcel.getWorkSheet().setColumnView(4, 10);
	    	jExcel.getWorkSheet().setColumnView(5, 10);	
	    	jExcel.getWorkSheet().setColumnView(6, 10);	
	    	jExcel.getWorkSheet().setColumnView(7, 10);	
	    	jExcel.getWorkSheet().setColumnView(8, 10);
	    	jExcel.getWorkSheet().setColumnView(9, 10);
	    	jExcel.getWorkSheet().setColumnView(10, 10);
	    	jExcel.getWorkSheet().setColumnView(11, 20);
	    	jExcel.getWorkSheet().setColumnView(12, 20);
	    	jExcel.getWorkSheet().setColumnView(13, 10);
	    	jExcel.getWorkSheet().setColumnView(14, 15);
	    	jExcel.getWorkSheet().setColumnView(15, 15);
	    	jExcel.getWorkSheet().setColumnView(16, 15);
	    	jExcel.getWorkSheet().setColumnView(17, 15);
	    	jExcel.getWorkSheet().setColumnView(18, 15);
	    	jExcel.getWorkSheet().setColumnView(19, 15);
	    	jExcel.getWorkSheet().setColumnView(20, 20);
	    	//jExcel.getWorkSheet().setColumnView(16, 15);
	    	
	    	jExcel.addDate(0, 0,title,jExcel.repTitleFormat);
	    	jExcel.getWorkSheet().setRowView(0, 800);
	    	//jExcel.addDate(0, 1,"发射城市",jExcel.dateTITLEFormat);
	    	jExcel.addDate(0, 1,"发射台",jExcel.dateTITLEFormat);
	    	jExcel.addDate(1, 1,"机号",jExcel.dateTITLEFormat);
	    	jExcel.addDate(2, 1,"频率(KHZ)",jExcel.dateTITLEFormat);
	    	jExcel.addDate(3, 1,"天线号",jExcel.dateTITLEFormat);
	    	jExcel.addDate(4, 1,"天线程式",jExcel.dateTITLEFormat);
	    	jExcel.addDate(5, 1,"方向",jExcel.dateTITLEFormat);
	    	jExcel.addDate(6, 1,"语言",jExcel.dateTITLEFormat);
	    	jExcel.addDate(7, 1,"功率",jExcel.dateTITLEFormat);
	    	jExcel.addDate(8, 1,"节目类型",jExcel.dateTITLEFormat);
	    	jExcel.addDate(9, 1,"开始时间",jExcel.dateTITLEFormat);
	    	jExcel.addDate(10, 1,"结束时间",jExcel.dateTITLEFormat);
	    	jExcel.addDate(11, 1,"启用期",jExcel.dateTITLEFormat);
	    	jExcel.addDate(12, 1,"停用期",jExcel.dateTITLEFormat);
	    	jExcel.addDate(13, 1,"录入人",jExcel.dateTITLEFormat);
	    	jExcel.addDate(14, 1,"效果收测站点",jExcel.dateTITLEFormat);
	    	jExcel.addDate(15, 1,"发射台ID",jExcel.dateTITLEFormat);
	    	jExcel.addDate(16, 1,"运行图ID",jExcel.dateTITLEFormat);
	    	jExcel.addDate(17, 1,"季节代号",jExcel.dateTITLEFormat);
	    	jExcel.addDate(18, 1,"通道",jExcel.dateTITLEFormat);
	    	jExcel.addDate(19, 1,"休息日期",jExcel.dateTITLEFormat);
	    	jExcel.addDate(20, 1,"导出时间",jExcel.dateTITLEFormat);
	    	jExcel.getWorkSheet().setRowView(1, 600);
	    	jExcel.mergeCells(0,0,20,0);//合并表头
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
	 * 国际台海外落地运行图拆分
	 * @param bean
	 * @return
	 */
	public Object ykzSplitHWRunplan(GJTLDRunplanBean bean){
		ArrayList result = new ArrayList();
		String typeID = bean.getType_id();//站点类型
		String codes = bean.getMon_area();
		String seasonType=bean.getSummer();//季节类型
		String user = bean.getInput_person();
		String sql="";
		String message="";
		String delSql="update  zres_runplan_chaifen_tab set is_delete=1,input_person='"+user+"',store_datetime=sysdate  where runplan_type_id=2 ";
		StringBuffer sbf = new StringBuffer("");
		if(typeID.equalsIgnoreCase("102")){//质量收测运行图拆分
		  delSql+=" and mon_area is not null and xg_mon_area is null";
		  if(codes!=null&&!codes.equalsIgnoreCase("")){
			  delSql+=" and '"+codes+"' like '%'||mon_area||'%'";
		  }
		  if(seasonType!=null&&!seasonType.equalsIgnoreCase("")){
			  delSql+=" and summer like '%"+seasonType+"%' ";
		  }
		  try {
				DbComponent.exeUpdate(delSql);
			} catch (DbException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return new EXEException("","拆分运行图前删除运行图报错："+e1.getMessage(),"");
			}
		  
		  sql = "select t.*,rct.city as sentcity,zlt.language_name as language  from zres_runplan_tab t,res_city_tab rct,zdic_language_tab zlt " +
			"where t.language_id=zlt.language_id(+) and t.sentcity_id=rct.id(+) and runplan_type_id='2' and  t.mon_area is not null   and t.is_delete=0  and t.valid_end_time>=sysdate ";
		  if(seasonType!=null&&!seasonType.equalsIgnoreCase("")){
			  sql+=" and t.summer like '%"+seasonType+"%' ";
		  }
		  if(!codes.equalsIgnoreCase("")&&codes!=null){
				
				sbf.append("(");
				if(codes.indexOf(",")>0){
					String[] s = codes.split(",");
					for(int i=0;i<s.length;i++){
						
						if(i==s.length-1){
							sbf.append(" t.mon_area like '%"+s[i]+"%')");
							break;
						}else{
							sbf.append(" t.mon_area like '%"+s[i]+"%' or ");
						}
					}
				}else{
					sbf.append(" t.mon_area like '%"+codes+"%')");
				}
					
			}
			if(sbf.length()>0){
				sql+=" and "+sbf.toString();
			}
//			if(codes.indexOf(",")>0){
//				for(int j=0;j<codes.split(",").length;j++){
//					if(j==codes.split(",").length-1){
//						break;
//					}else
//					sql+=" union "+sql;
//				}
//			}
		}else{//效果收测运行图拆分
			delSql+=" and xg_mon_area is not null and mon_area is null ";
			if(codes!=null&&!codes.equalsIgnoreCase("")){
				  delSql+=" and '"+codes+"' like '%'||xg_mon_area||'%'";
			  }
			if(seasonType!=null&&!seasonType.equalsIgnoreCase("")){
				  delSql+=" and summer like '%"+seasonType+"%' ";
			  }
			  try {
					DbComponent.exeUpdate(delSql);
				} catch (DbException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					return new EXEException("","拆分运行图前删除运行图报错："+e1.getMessage(),"");
				}
		  sql = "select t.*,rct.city as sentcity,zlt.language_name as language  from zres_runplan_tab t,res_city_tab rct,zdic_language_tab zlt " +
			"where t.language_id=zlt.language_id(+) and t.sentcity_id=rct.id(+) and runplan_type_id='2' and t.xg_mon_area is not null and t.is_delete=0  and t.valid_end_time>=sysdate";
		  if(seasonType!=null&&!seasonType.equalsIgnoreCase("")){
			  sql+=" and t.summer like '%"+seasonType+"%' ";
		  }
		  if(!codes.equalsIgnoreCase("")&&codes!=null){
				
				sbf.append("(");
				if(codes.indexOf(",")>0){
					String[] s = codes.split(",");
					for(int i=0;i<s.length;i++){
						
						if(i==s.length-1){
							sbf.append(" t.xg_mon_area like '%"+s[i]+"%')");
							break;
						}else{
							sbf.append(" t.xg_mon_area like '%"+s[i]+"%' or ");
						}
					}
				}else{
					sbf.append(" t.xg_mon_area like '%"+codes+"%')");
				}
					
			}
			if(sbf.length()>0){
				sql+=" and "+sbf.toString();
			}
//			if(codes.indexOf(",")>0){
//				for(int j=0;j<codes.split(",").length;j++){
//					if(j==codes.split(",").length-1){
//						break;
//					}else
//					sql+=" union "+sql;
//				}
//			}
		}
		
		ASObject resObj=new ASObject();
		try {
			resObj=StringTool.pageQuerySql(sql, bean);
			ArrayList list =(ArrayList) resObj.get("resultList") ;//取出对象的结果集
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					String classpath = GJTLDRunplanBean.class.getName();
					GJTLDRunplanBean ldbean = (GJTLDRunplanBean) StringTool.convertObjToBean((ASObject) list.get(i),classpath);
					if(typeID.equalsIgnoreCase("102")){
						ldbean.setXg_mon_area("");
						if(ldbean.getMon_area()!=null&&!ldbean.getMon_area().equalsIgnoreCase("")){
							String[] mon_area = ldbean.getMon_area().split(",");
							for(int j=0;j<mon_area.length;j++){
								if(codes!=null&&!codes.equalsIgnoreCase("")){
							    	if(codes.indexOf(mon_area[j])<0){
							    		continue;
							    	}
							    }
										GJTLDRunplanBean newbean = (GJTLDRunplanBean) StringTool.convertObjToBean((ASObject) list.get(i),classpath);
										newbean.setMon_area(mon_area[j]);
										result.add(newbean);
								}
								
							}
						resObj.put("resultList", result);
						resObj.put("resultTotal",result.size());
					}
				   else{
					   ldbean.setMon_area("");
						if(ldbean.getXg_mon_area()!=null&&!ldbean.getXg_mon_area().equalsIgnoreCase("")){
							String[] xg_mon_area = ldbean.getXg_mon_area().split(",");
							for(int k=0;k<xg_mon_area.length;k++){
								if(codes!=null&&!codes.equalsIgnoreCase("")){
							    	if(codes.indexOf(xg_mon_area[k])<0){
							    		continue;
							    	}
							    }
										GJTLDRunplanBean newbean = (GJTLDRunplanBean) StringTool.convertObjToBean((ASObject) list.get(i),classpath);
										newbean.setXg_mon_area(xg_mon_area[k]);
										result.add(newbean);
								}
						}
						resObj.put("resultList", result);
						resObj.put("resultTotal",result.size());
						
					}
				}
				addHWRunplan(resObj,typeID);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(typeID.equalsIgnoreCase("102")){
				return new EXEException("","海外落地质量运行图拆分异常："+e.getMessage(),"");
			}else return new EXEException("","海外落地效果运行图拆分异常："+e.getMessage(),"");
		}
		
		return resObj;
	}
	
/**
 * 国际台海外落地运行图拆分导出Excel
 * @param msg
 * @param request
 * @param response
 */
	public void doHWExcel(String msg,HttpServletRequest request,HttpServletResponse response){
		Element root = StringTool.getXMLRoot(msg);
		String runplan_type_id = root.getChildText("runplan_type");
		String type_id = root.getChildText("typeID");
		String codes = root.getChildText("codes");
		String season = root.getChildText("season");
//		GJTLDRunplanBean bean = new GJTLDRunplanBean();
//		bean.setRunplan_type_id(runplan_type_id);
//		bean.setType_id(type_id);
//		bean.setMon_area(codes);
//		bean.setStartRow(1);
//		bean.setEndRow(10000);
//		ArrayList list = new ArrayList();
//		ASObject resObj=(ASObject) ykzSplitHWRunplan(bean);
//		list =(ArrayList) resObj.get("resultList") ;//取出对象的结果集
		String sql="select t.*,zlt.language_name as language,rct.city as sentcity" +
				" from zres_runplan_chaifen_tab t,zdic_language_tab zlt ,res_city_tab rct " +
		        " where t.language_id=zlt.language_id(+) and t.runplan_type_id=2 and t.sentcity_id=rct.id(+) and t.is_delete=0 ";
		 if(season!=null&&!season.equalsIgnoreCase("")){
			  sql+=" and t.summer like '%"+season+"%' ";
		 }
		String fileName="";
		String title="";
		if(type_id.equalsIgnoreCase("102")){
			sql+=" and t.mon_area is not null and t.xg_mon_area is null ";
			fileName = "海外落地质量收测运行图拆分";
			title = "海外落地质量收测运行图拆分";
			 if(codes!=null&&!codes.equalsIgnoreCase("")){
			     sql+=" and '"+codes+"' like '%'||mon_area||'%'";
		     }
			
			 sql+=" order by mon_area ";
		}else{
			sql+=" and xg_mon_area is not null and t.mon_area is null ";
			fileName = "海外落地效果收测运行图拆分";
			title = "海外落地效果收测运行图拆分";
			 if(codes!=null&&!codes.equalsIgnoreCase("")){
			     sql+=" and '"+codes+"' like '%'||xg_mon_area||'%'";
		     }
			 sql+=" order by xg_mon_area ";
		}
		
		
		String downFileName = "";
		try {
			
			JExcel jExcel = new JExcel();
			downFileName=jExcel.openDocument();
			jExcel.WorkBookGetSheet(0);
			GDSet gd = DbComponent.Query(sql);
			String systemdate = StringTool.Date2String(new Date());
			for(int i=0;i<gd.getRowCount();i++){
					jExcel.addDate(0, i+2,gd.getString(i, "sentcity"),jExcel.dateCellFormat);
					jExcel.addDate(1, i+2,gd.getString(i, "redisseminators"),jExcel.dateCellFormat);
					jExcel.addDate(2, i+2,gd.getString(i, "freq"),jExcel.dateCellFormat);
					jExcel.addDate(3, i+2,gd.getString(i, "direction"),jExcel.dateCellFormat);
					jExcel.addDate(4, i+2,gd.getString(i, "language"),jExcel.dateCellFormat);
					jExcel.addDate(5, i+2,gd.getString(i, "service_area"),jExcel.dateCellFormat);
					jExcel.addDate(6, i+2,gd.getString(i, "start_time"),jExcel.dateCellFormat);
					jExcel.addDate(7, i+2,gd.getString(i, "end_time"),jExcel.dateCellFormat);
					jExcel.addDate(8, i+2,gd.getString(i, "rest_datetime"),jExcel.dateCellFormat);
					jExcel.addDate(9, i+2,gd.getString(i, "valid_start_time"),jExcel.dateCellFormat);
					jExcel.addDate(10, i+2,gd.getString(i, "valid_end_time"),jExcel.dateCellFormat);
					jExcel.addDate(11, i+2,gd.getString(i, "summer"),jExcel.dateCellFormat);
					jExcel.addDate(12, i+2,gd.getString(i, "summer_starttime"),jExcel.dateCellFormat);
					jExcel.addDate(13, i+2,gd.getString(i, "summer_endtime"),jExcel.dateCellFormat);
					if(type_id.equalsIgnoreCase("102")){
						jExcel.addDate(14, i+2,gd.getString(i, "mon_area"),jExcel.dateCellFormat);
					}else{
						jExcel.addDate(14, i+2,gd.getString(i, "xg_mon_area"),jExcel.dateCellFormat);
					}
					
					jExcel.addDate(15, i+2,gd.getString(i, "remark"),jExcel.dateCellFormat);
					jExcel.addDate(16, i+2,gd.getString(i, "runplan_id"),jExcel.dateCellFormat);
					jExcel.addDate(17, i+2,gd.getString(i, "launch_country"),jExcel.dateCellFormat);
					jExcel.addDate(18, i+2,gd.getString(i, "weekday"),jExcel.dateCellFormat);
					jExcel.addDate(19, i+2,systemdate,jExcel.dateCellFormat);
					jExcel.getWorkSheet().setRowView(i+2, 600);
			}
//			for(int i=0;i<list.size();i++){
//				
//				bean = (GJTLDRunplanBean) list.get(i);
//				
//				jExcel.addDate(0, i+2,bean.getSentcity(),jExcel.dateCellFormat);
//				jExcel.addDate(1, i+2,bean.getRedisseminators(),jExcel.dateCellFormat);
//				jExcel.addDate(2, i+2,bean.getFreq(),jExcel.dateCellFormat);
//				jExcel.addDate(3, i+2,bean.getDirection(),jExcel.dateCellFormat);
//				jExcel.addDate(4, i+2,bean.getLanguage(),jExcel.dateCellFormat);
//				jExcel.addDate(5, i+2,bean.getService_area(),jExcel.dateCellFormat);
//				jExcel.addDate(6, i+2,bean.getStart_time(),jExcel.dateCellFormat);
//				jExcel.addDate(7, i+2,bean.getEnd_time(),jExcel.dateCellFormat);
//				jExcel.addDate(8, i+2,bean.getRest_datetime(),jExcel.dateCellFormat);
//				jExcel.addDate(9, i+2,bean.getValid_start_time(),jExcel.dateCellFormat);
//				jExcel.addDate(10, i+2,bean.getValid_end_time(),jExcel.dateCellFormat);
//				jExcel.addDate(11, i+2,bean.getSummer(),jExcel.dateCellFormat);
//				jExcel.addDate(12, i+2,bean.getSummer_starttime(),jExcel.dateCellFormat);
//				jExcel.addDate(13, i+2,bean.getSummer_endtime(),jExcel.dateCellFormat);
//				if(type_id.equalsIgnoreCase("102")){
//					jExcel.addDate(14, i+2,bean.getMon_area(),jExcel.dateCellFormat);
//				}else{
//					jExcel.addDate(14, i+2,bean.getXg_mon_area(),jExcel.dateCellFormat);
//				}
//				
//				jExcel.addDate(15, i+2,bean.getRemark(),jExcel.dateCellFormat);
//				
//				jExcel.getWorkSheet().setRowView(i+2, 600);
//			}
			
			jExcel.getWorkSheet().setColumnView(0, 10);
	    	jExcel.getWorkSheet().setColumnView(1, 10);
	    	jExcel.getWorkSheet().setColumnView(2, 10);	
	    	jExcel.getWorkSheet().setColumnView(3, 12);
	    	jExcel.getWorkSheet().setColumnView(4, 10);
	    	jExcel.getWorkSheet().setColumnView(5, 10);	
	    	jExcel.getWorkSheet().setColumnView(6, 10);	
	    	jExcel.getWorkSheet().setColumnView(7, 10);	
	    	jExcel.getWorkSheet().setColumnView(8, 15);
	    	jExcel.getWorkSheet().setColumnView(9, 15);
	    	jExcel.getWorkSheet().setColumnView(10, 15);
	    	jExcel.getWorkSheet().setColumnView(11, 10);
	    	jExcel.getWorkSheet().setColumnView(12, 15);
	    	jExcel.getWorkSheet().setColumnView(13, 15);
	    	jExcel.getWorkSheet().setColumnView(14, 15);
	    	jExcel.getWorkSheet().setColumnView(15, 15);
	    	jExcel.getWorkSheet().setColumnView(16, 15);
	    	jExcel.getWorkSheet().setColumnView(17, 15);
	    	jExcel.getWorkSheet().setColumnView(18, 15);
	    	jExcel.getWorkSheet().setColumnView(19, 20);
	    	jExcel.addDate(0, 0,title,jExcel.repTitleFormat);
	    	jExcel.getWorkSheet().setRowView(0, 800);
	    	
	    	jExcel.addDate(0, 1,"发射城市",jExcel.dateTITLEFormat);
	    	jExcel.addDate(1, 1,"转播机构",jExcel.dateTITLEFormat);
	    	jExcel.addDate(2, 1,"频率",jExcel.dateTITLEFormat);
	    	jExcel.addDate(3, 1,"方向",jExcel.dateTITLEFormat);
	    	jExcel.addDate(4, 1,"语言",jExcel.dateTITLEFormat);
	    	jExcel.addDate(5, 1,"服务区",jExcel.dateTITLEFormat);
	    	jExcel.addDate(6, 1,"开始时间",jExcel.dateTITLEFormat);
	    	jExcel.addDate(7, 1,"结束时间",jExcel.dateTITLEFormat);
	    	jExcel.addDate(8, 1,"播音日期",jExcel.dateTITLEFormat);
	    	jExcel.addDate(9, 1,"启用期",jExcel.dateTITLEFormat);
	    	jExcel.addDate(10, 1,"停用期",jExcel.dateTITLEFormat);
	    	jExcel.addDate(11, 1,"夏令时",jExcel.dateTITLEFormat);
	    	jExcel.addDate(12, 1,"夏令时启用日期",jExcel.dateTITLEFormat);
	    	jExcel.addDate(13, 1,"夏令时停用日期",jExcel.dateTITLEFormat);
	    	if(type_id.equalsIgnoreCase("102")){
	    		jExcel.addDate(14, 1,"质量收测站点",jExcel.dateTITLEFormat);
	    	}else{
	    		jExcel.addDate(14, 1,"效果收测站点",jExcel.dateTITLEFormat);
	    	}
	    	
	    	jExcel.addDate(15, 1,"备注",jExcel.dateTITLEFormat);
	    	jExcel.addDate(16, 1,"运行图ID",jExcel.dateTITLEFormat);
	    	jExcel.addDate(17, 1,"发射国家",jExcel.dateTITLEFormat);
	    	jExcel.addDate(18, 1,"周设置",jExcel.dateTITLEFormat);
	    	jExcel.addDate(19, 1,"导出时间",jExcel.dateTITLEFormat);
	    	jExcel.getWorkSheet().setRowView(1, 600);
	    	jExcel.mergeCells(0,0,19,0);//合并表头
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
