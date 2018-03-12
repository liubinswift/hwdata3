package com.viewscenes.web.runplanStatistics;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Element;
import org.jmask.web.controller.EXEException;

import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.printexcel.JExcel;
import com.viewscenes.pub.GDSet;
import com.viewscenes.util.StringTool;

import flex.messaging.io.amf.ASObject;

public class WGTRunplanStatistics {

	//发射台列表
	private ArrayList stationList=new ArrayList();
	 //语言列表
	private ArrayList languageList = new ArrayList();
	 //服务区列表
	private ArrayList areaList = new ArrayList();
	 //播音时段列表
	private ArrayList broadcastList = new ArrayList();
	
	public Object statisticsInfo(ASObject obj){
		ArrayList list = new ArrayList();
		String flag = (String) obj.get("flag");
		String station = (String) obj.get("station_name");
		String language_id = (String) obj.get("language_id");
		String languageName= (String) obj.get("language_name");
		String serviceArea = (String) obj.get("service_area");
		String broadcastTime =(String) obj.get("broadcast_time");
		try {
			if(flag.equalsIgnoreCase("station")){
				if(station!=null&&!station.equalsIgnoreCase("全部")){
					HashMap map = calculateRunplanByStation(station,obj);
					Iterator iter = map.entrySet().iterator();
					while(iter.hasNext()){
						Map.Entry entry = (Map.Entry) iter.next();
						ASObject asobj = new ASObject();
						asobj.put("station",station);
						asobj.put("bc_freq",entry.getKey().toString().split("&")[0]);
						asobj.put("bc_hours",entry.getValue().toString().split("&")[0]);
						asobj.put("sc_freq",entry.getKey().toString().split("&")[1]);
						asobj.put("sc_hours",entry.getValue().toString().split("&")[1]);
						list.add(asobj);
					}
				}else{
					getStatoin();//获得所有发射台
					for(int j=0;j<stationList.size();j++){
						String station_name =(String) stationList.get(j);
						HashMap map = calculateRunplanByStation(station_name,obj);
						Iterator iter = map.entrySet().iterator();
						while(iter.hasNext()){
							Map.Entry entry = (Map.Entry) iter.next();
							ASObject asobj = new ASObject();
							asobj.put("station",station_name);
							asobj.put("bc_freq",entry.getKey().toString().split("&")[0]);
							asobj.put("bc_hours",entry.getValue().toString().split("&")[0]);
							asobj.put("sc_freq",entry.getKey().toString().split("&")[1]);
							asobj.put("sc_hours",entry.getValue().toString().split("&")[1]);
							list.add(asobj);
						}
						
						
					}
				}
				
			}
		  if(flag.equalsIgnoreCase("language")){
			  if(languageName!=null&&!languageName.equalsIgnoreCase("全部")){
				  HashMap map = calculateRunplanByLanguage(languageName,obj);
					Iterator iter = map.entrySet().iterator();
					while(iter.hasNext()){
						Map.Entry entry = (Map.Entry) iter.next();
						ASObject asobj = new ASObject();
						asobj.put("language",languageName);
						asobj.put("bc_freq",entry.getKey().toString().split("&")[0]);
						asobj.put("bc_hours",entry.getValue().toString().split("&")[0]);
						asobj.put("sc_freq",entry.getKey().toString().split("&")[1]);
						asobj.put("sc_hours",entry.getValue().toString().split("&")[1]);
						list.add(asobj);
					}
			  }else{
				  getLanguage();//获得语言
					for(int i=0;i<languageList.size();i++){
						String language_name =(String) languageList.get(i);
						HashMap map = calculateRunplanByLanguage(language_name,obj);
						Iterator iter = map.entrySet().iterator();
						while(iter.hasNext()){
							Map.Entry entry = (Map.Entry) iter.next();
							ASObject asobj = new ASObject();
							asobj.put("language",language_name);
							asobj.put("bc_freq",entry.getKey().toString().split("&")[0]);
							asobj.put("bc_hours",entry.getValue().toString().split("&")[0]);
							asobj.put("sc_freq",entry.getKey().toString().split("&")[1]);
							asobj.put("sc_hours",entry.getValue().toString().split("&")[1]);
							list.add(asobj);
						}
					}
			  }
				
				
			}
			if(flag.equalsIgnoreCase("area")){
				if(serviceArea!=null&&!serviceArea.equalsIgnoreCase("全部")){
					HashMap map = calculateRunplanByArea(serviceArea,obj);
					Iterator iter = map.entrySet().iterator();
					while(iter.hasNext()){
						Map.Entry entry = (Map.Entry) iter.next();
						ASObject asobj = new ASObject();
						asobj.put("area",serviceArea);
						asobj.put("bc_freq",entry.getKey().toString().split("&")[0]);
						asobj.put("bc_hours",entry.getValue().toString().split("&")[0]);
						asobj.put("sc_freq",entry.getKey().toString().split("&")[1]);
						asobj.put("sc_hours",entry.getValue().toString().split("&")[1]);
						list.add(asobj);
					}
				}else{
					getArea();//获得语言
					for(int i=0;i<areaList.size();i++){
						String area =(String) areaList.get(i);
						HashMap map = calculateRunplanByArea(area,obj);
						Iterator iter = map.entrySet().iterator();
						while(iter.hasNext()){
							Map.Entry entry = (Map.Entry) iter.next();
							ASObject asobj = new ASObject();
							asobj.put("area",area);
							asobj.put("bc_freq",entry.getKey().toString().split("&")[0]);
							asobj.put("bc_hours",entry.getValue().toString().split("&")[0]);
							asobj.put("sc_freq",entry.getKey().toString().split("&")[1]);
							asobj.put("sc_hours",entry.getValue().toString().split("&")[1]);
							list.add(asobj);
						}
					}
				}
				
			}
			if(flag.equalsIgnoreCase("broadcast_time")){
				if(broadcastTime!=null&&!broadcastTime.equalsIgnoreCase("全部")){
					HashMap map = this.calculateRunplanByTime(broadcastTime, obj);
					Iterator iter = map.entrySet().iterator();
					while(iter.hasNext()){
						Map.Entry entry = (Map.Entry) iter.next();
						ASObject asobj = new ASObject();
						asobj.put("broadcast_time",broadcastTime);
						asobj.put("bc_freq",entry.getKey().toString().split("&")[0]);
						asobj.put("bc_hours",entry.getValue().toString().split("&")[0]);
						asobj.put("sc_freq",entry.getKey().toString().split("&")[1]);
						asobj.put("sc_hours",entry.getValue().toString().split("&")[1]);
						list.add(asobj);
					}
				}else{
					getBroadcastTime();
					for(int i=0;i<broadcastList.size();i++){
						String broadcast = (String) broadcastList.get(i);
						HashMap map = calculateRunplanByTime(broadcast,obj);
						Iterator iter = map.entrySet().iterator();
						while(iter.hasNext()){
							Map.Entry entry = (Map.Entry) iter.next();
							ASObject asobj = new ASObject();
							asobj.put("broadcast_time",broadcast);
							asobj.put("bc_freq",entry.getKey().toString().split("&")[0]);
							asobj.put("bc_hours",entry.getValue().toString().split("&")[0]);
							asobj.put("sc_freq",entry.getKey().toString().split("&")[1]);
							asobj.put("sc_hours",entry.getValue().toString().split("&")[1]);
							list.add(asobj);
						}
					}
					
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return new EXEException("", e.getMessage(), "");
		}
		return list;
	}
	/**
	 * 从运行图表中，取出有效的发射台。
	 */
	public void getStatoin(){
		String sql="select distinct(t.station_name) from zres_runplan_tab t where t.runplan_type_id=3 and t.valid_end_time>=sysdate and t.is_delete=0  order by t.station_name ";
		
		try {
			GDSet  gd = DbComponent.Query(sql);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					stationList.add(gd.getString(i, "station_name"));
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * 根据运行图和语言表取出有效的语言名称
	 */
	public void getLanguage(){
		String sql = "select  distinct(zlt.language_name) from zres_runplan_tab t,zdic_language_tab zlt where  t.runplan_type_id=3 and t.valid_end_time>=sysdate and t.language_id=zlt.language_id and t.is_delete=0 and zlt.is_delete=0 order by zlt.language_name ";
		try {
			GDSet  gd = DbComponent.Query(sql);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					languageList.add(gd.getString(i, "language_name"));
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 根据运行图和服务区表取出有效的区域名称
	 */
	public void getArea(){
		String sql="select distinct(dst.chinese_name) from zres_runplan_tab t,dic_servicesarea_tab dst where  t.runplan_type_id=3 and t.valid_end_time>=sysdate and t.service_area=dst.chinese_name and t.is_delete=0";
		try {
			GDSet  gd = DbComponent.Query(sql);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					areaList.add(gd.getString(i, "chinese_name"));
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 从运行图中获取播音时段
	 */
	 public void getBroadcastTime(){
	    	String sql="select distinct(t.start_time||'-'||t.end_time) as broadcasttime from zres_runplan_tab t where t.is_delete=0 and t.valid_end_time>=sysdate";
	    	try {
				GDSet gd = DbComponent.Query(sql);
				if(gd.getRowCount()>0){
					for(int i=0;i<gd.getRowCount();i++){
						broadcastList.add(gd.getString(i, "broadcasttime"));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	/**
	 * 根据发射台名称从运行图表中统计播出频率和频时
	 * @param station_name
	 */
	public HashMap calculateRunplanByStation(String station_name,ASObject obj){
		String sql="select * from zres_runplan_tab t where  t.is_delete =0 and t.runplan_type_id=3 and t.valid_end_time>=sysdate and t.station_name like '%"+station_name+"%'";
		String sql1 = "select * from zres_runplan_tab t where  t.is_delete =0 and t.runplan_type_id=3 and t.valid_end_time>=sysdate and t.station_name like '%"+station_name+"%' and (t.mon_area is not null or t.xg_mon_area is not null)";
		String language_name = (String) obj.get("language_name");
		String language_id = (String) obj.get("language_id");
		String serviceArea = (String) obj.get("service_area");
		String broadcastTime =(String) obj.get("broadcast_time");
		if(language_name!=null&&!language_name.equalsIgnoreCase("全部")){
			sql+=" and t.language_id='"+language_id+"'";
			sql1+=" and t.language_id='"+language_id+"'";
		}
		if(serviceArea!=null&&!serviceArea.equalsIgnoreCase("全部")){
			sql+=" and t.service_area like '%"+serviceArea+"%'";
			sql1+=" and t.service_area like '%"+serviceArea+"%'";
		}
		if(broadcastTime!=null&&!broadcastTime.equalsIgnoreCase("全部")){
			sql+=" and t.start_time='"+broadcastTime.split("-")[0]+"' and t.end_time='"+broadcastTime.split("-")[1]+"'";
			sql1+=" and t.start_time='"+broadcastTime.split("-")[0]+"' and t.end_time='"+broadcastTime.split("-")[1]+"'";
		}
		sql+=" order by station_name";
		sql1+=" order by station_name";
		HashMap bc_map = new HashMap();
		HashMap sc_map = new HashMap();
		HashMap result = new HashMap();
		double value=0.0;
		double value1=0.0;
		try {
			//统计播出频率数和频时数
			GDSet gd = DbComponent.Query(sql);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					String key="";
					String start_time = gd.getString(i, "start_time");
					String end_time = gd.getString(i, "end_time");
					long time = 0;
					time = StringTool.getTimeSecond(start_time.trim()+":00",end_time.trim()+":00");
                    double hours = time * 1.0 / 3600;
                    hours = StringTool.formatDouble(hours,2);
                    value+=hours;
					key=gd.getString(i, "freq")+gd.getString(i, "direction")+gd.getString(i, "service_area");
					bc_map.put(key, hours);
				  
				}
				
			}
			//统计收测频率和频时数
			GDSet gd1 = DbComponent.Query(sql1);
			if(gd1.getRowCount()>0){
				for(int i=0;i<gd1.getRowCount();i++){
					String key="";
					String start_time = gd.getString(i, "start_time");
					String end_time = gd.getString(i, "end_time");
					long time = 0;
					time = StringTool.getTimeSecond(start_time.trim()+":00",end_time.trim()+":00");
                    double hours = time * 1.0 / 3600;
                    hours = StringTool.formatDouble(hours,2);
                    value1+=hours;
					key=gd1.getString(i, "freq")+gd1.getString(i, "direction")+gd1.getString(i, "service_area");
					sc_map.put(key, hours);
				  
				}
			}
			result.put(bc_map.size()+"&"+sc_map.size(), value+"&"+value1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 根据语言名称从运行图表中统计播出频率和频时
	 * @param language_id
	 */
	public HashMap calculateRunplanByLanguage(String language_name,ASObject obj){
		String sql="select * from zres_runplan_tab t,zdic_language_tab zlt where  t.is_delete =0 and t.runplan_type_id=3 and t.valid_end_time>=sysdate and t.language_id=zlt.language_id and zlt.language_name like '%"+language_name+"%' ";
		String sql1 = "select * from zres_runplan_tab t,zdic_language_tab zlt where t.is_delete =0 and t.runplan_type_id=3 and t.valid_end_time>=sysdate and t.language_id=zlt.language_id and zlt.language_name like '%"+language_name+"%' and (t.mon_area is not null or t.xg_mon_area is not null) ";
		String stationName = (String) obj.get("station_name");
		String station_id = (String) obj.get("station_id");
		String serviceArea = (String) obj.get("service_area");
		String broadcastTime =(String) obj.get("broadcast_time");
		if(stationName!=null&&!stationName.equalsIgnoreCase("全部")){
			sql+=" and t.station_id='"+station_id+"'";
			sql1+=" and t.station_id='"+station_id+"'";
		}
		if(serviceArea!=null&&!serviceArea.equalsIgnoreCase("全部")){
			sql+=" and t.service_area like '%"+serviceArea+"%'";
			sql1+=" and t.service_area like '%"+serviceArea+"%'";
		}
		if(broadcastTime!=null&&!broadcastTime.equalsIgnoreCase("全部")){
			sql+=" and t.start_time='"+broadcastTime.split("-")[0]+"' and t.end_time='"+broadcastTime.split("-")[1]+"'";
			sql1+=" and t.start_time='"+broadcastTime.split("-")[0]+"' and t.end_time='"+broadcastTime.split("-")[1]+"'";
		}
		sql+=" order by zlt.language_name";
		sql1+=" order by zlt.language_name";
		HashMap bc_map = new HashMap();
		HashMap sc_map = new HashMap();
		HashMap result = new HashMap();
		double value=0.0;
		double value1=0.0;
		try {
			//统计播出频率数和频时数
			GDSet gd = DbComponent.Query(sql);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					String key="";
					String start_time = gd.getString(i, "start_time");
					String end_time = gd.getString(i, "end_time");
					long time = 0;
					time = StringTool.getTimeSecond(start_time.trim()+":00",end_time.trim()+":00");
                    double hours = time * 1.0 / 3600;
                    hours = StringTool.formatDouble(hours,2);
                    value+=hours;
					key=gd.getString(i, "station_name")+gd.getString(i, "freq")+gd.getString(i, "direction")+gd.getString(i, "service_area");
					bc_map.put(key, hours);
				  
				}
				
			}
			//统计收测频率和频时数
			GDSet gd1 = DbComponent.Query(sql1);
			if(gd1.getRowCount()>0){
				for(int i=0;i<gd1.getRowCount();i++){
					String key="";
					String start_time = gd.getString(i, "start_time");
					String end_time = gd.getString(i, "end_time");
					long time = 0;
					time = StringTool.getTimeSecond(start_time.trim()+":00",end_time.trim()+":00");
                    double hours = time * 1.0 / 3600;
                    hours = StringTool.formatDouble(hours,2);
                    value1+=hours;
					key=gd1.getString(i, "station_name")+gd1.getString(i, "freq")+gd1.getString(i, "direction")+gd1.getString(i, "service_area");
					sc_map.put(key, hours);
				  
				}
			}
			result.put(bc_map.size()+"&"+sc_map.size(), value+"&"+value1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 根据 服务区名称从运行图表中统计播出频率和频时
	 * @param language_id
	 */
	public HashMap calculateRunplanByArea(String area,ASObject obj){
		String sql="select * from zres_runplan_tab t where t.is_delete =0 and t.runplan_type_id=3 and t.valid_end_time>=sysdate and t.service_area like '%"+area+"%' ";
		String sql1 = "select * from zres_runplan_tab t where t.is_delete =0 and t.runplan_type_id=3 and t.valid_end_time>=sysdate and t.service_area like '%"+area+"%' and (t.mon_area is not null or t.xg_mon_area is not null)";
		String stationName = (String) obj.get("station_name");
		String station_id = (String) obj.get("station_id");
		String language_name = (String) obj.get("language_name");
		String language_id = (String) obj.get("language_id");
		String broadcastTime =(String) obj.get("broadcast_time");
		if(stationName!=null&&!stationName.equalsIgnoreCase("全部")){
			sql+=" and t.station_id='"+station_id+"'";
			sql1+=" and t.station_id='"+station_id+"'";
		}
		if(language_name!=null&&!language_name.equalsIgnoreCase("全部")){
		    sql+=" and t.language_id='"+language_id+"'";
			sql1+=" and t.language_id='"+language_id+"'";
		}
		if(broadcastTime!=null&&!broadcastTime.equalsIgnoreCase("全部")){
			sql+=" and t.start_time='"+broadcastTime.split("-")[0]+"' and t.end_time='"+broadcastTime.split("-")[1]+"'";
			sql1+=" and t.start_time='"+broadcastTime.split("-")[0]+"' and t.end_time='"+broadcastTime.split("-")[1]+"'";
		}
		sql+=" order by t.service_area";
		sql1+=" order by t.service_area";
		HashMap bc_map = new HashMap();
		HashMap sc_map = new HashMap();
		HashMap result = new HashMap();
		double value=0.0;
		double value1=0.0;
		try {
			//统计播出频率数和频时数
			GDSet gd = DbComponent.Query(sql);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					String key="";
					String start_time = gd.getString(i, "start_time");
					String end_time = gd.getString(i, "end_time");
					long time = 0;
					time = StringTool.getTimeSecond(start_time.trim()+":00",end_time.trim()+":00");
                    double hours = time * 1.0 / 3600;
                    hours = StringTool.formatDouble(hours,2);
                    value+=hours;
					key=gd.getString(i, "station_name")+gd.getString(i, "freq")+gd.getString(i, "direction")+gd.getString(i, "service_area");
					bc_map.put(key, hours);
				  
				}
				
			}
			//统计收测频率和频时数
			GDSet gd1 = DbComponent.Query(sql1);
			if(gd1.getRowCount()>0){
				for(int i=0;i<gd1.getRowCount();i++){
					String key="";
					String start_time = gd.getString(i, "start_time");
					String end_time = gd.getString(i, "end_time");
					long time = 0;
					time = StringTool.getTimeSecond(start_time.trim()+":00",end_time.trim()+":00");
                    double hours = time * 1.0 / 3600;
                    hours = StringTool.formatDouble(hours,2);
                    value1+=hours;
					key=gd1.getString(i, "station_name")+gd1.getString(i, "freq")+gd1.getString(i, "direction")+gd1.getString(i, "service_area");
					sc_map.put(key, hours);
				  
				}
			}
			result.put(bc_map.size()+"&"+sc_map.size(), value+"&"+value1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 根据播音时段从运行图表中统计播出频率和频时
	 * 
	 */
	public HashMap calculateRunplanByTime(String broadcastTime,ASObject obj){
		String sql="select * from zres_runplan_tab t where  t.is_delete =0 and t.runplan_type_id=3 and t.start_time='"+broadcastTime.split("-")[0]+"' and t.end_time='"+broadcastTime.split("-")[1]+"' ";
		String sql1 = "select * from zres_runplan_tab t where t.is_delete =0 and t.runplan_type_id=3 and t.start_time='"+broadcastTime.split("-")[0]+"' and t.end_time='"+broadcastTime.split("-")[1]+"'" +
				" and (t.mon_area is not null or t.xg_mon_area is not null) ";
		String stationName = (String) obj.get("station_name");
		String station_id = (String) obj.get("station_id");
		String language_name = (String) obj.get("language_name");
		String language_id = (String) obj.get("language_id");
		String serviceArea = (String) obj.get("service_area");
		if(stationName!=null&&!stationName.equalsIgnoreCase("全部")){
			sql+=" and t.station_id='"+station_id+"'";
			sql1+=" and t.station_id='"+station_id+"'";
		}
		if(language_name!=null&&!language_name.equalsIgnoreCase("全部")){
		    sql+=" and t.language_id='"+language_id+"'";
			sql1+=" and t.language_id='"+language_id+"'";
		}
		if(serviceArea!=null&&!serviceArea.equalsIgnoreCase("全部")){
			sql+=" and t.service_area like '%"+serviceArea+"%'";
			sql1+=" and t.service_area like '%"+serviceArea+"%'";
		}
		sql+=" order by t.start_time";
		sql1+=" order by t.start_time";
		HashMap bc_map = new HashMap();
		HashMap sc_map = new HashMap();
		HashMap result = new HashMap();
		double value=0.0;
		double value1=0.0;
		try {
			//统计播出频率数和频时数
			GDSet gd = DbComponent.Query(sql);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					String key="";
					String start_time = gd.getString(i, "start_time");
					String end_time = gd.getString(i, "end_time");
					long time = 0;
					time = StringTool.getTimeSecond(start_time.trim()+":00",end_time.trim()+":00");
                    double hours = time * 1.0 / 3600;
                    hours = StringTool.formatDouble(hours,2);
                    value+=hours;
					key=gd.getString(i, "station_name")+gd.getString(i, "freq");//+gd.getString(i, "direction")+gd.getString(i, "service_area");
					bc_map.put(key, hours);
				  
				}
				
			}
			//统计收测频率和频时数
			GDSet gd1 = DbComponent.Query(sql1);
			if(gd1.getRowCount()>0){
				for(int i=0;i<gd1.getRowCount();i++){
					String key="";
					String start_time = gd.getString(i, "start_time");
					String end_time = gd.getString(i, "end_time");
					long time = 0;
					time = StringTool.getTimeSecond(start_time.trim()+":00",end_time.trim()+":00");
                    double hours = time * 1.0 / 3600;
                    hours = StringTool.formatDouble(hours,2);
                    value1+=hours;
					key=gd1.getString(i, "station_name")+gd1.getString(i, "freq");//+gd1.getString(i, "direction")+gd1.getString(i, "service_area");
					sc_map.put(key, hours);
				  
				}
			}
			result.put(bc_map.size()+"&"+sc_map.size(), value+"&"+value1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void doExcel(String msg,HttpServletRequest request,HttpServletResponse response){
		Element root = StringTool.getXMLRoot(msg);
		String flag = root.getChildText("flag");
		String station = root.getChildText("station_name");
		String station_id = root.getChildText("station_id");
		String language_id = root.getChildText("language_id");
		String language_name= root.getChildText("language_name");
		String serviceArea = root.getChildText("service_area");
		String broadcastTime =root.getChildText("broadcast_time");
		ASObject obj = new ASObject();
		obj.put("flag", flag);
		obj.put("station_name", station);
		obj.put("station_id", station_id);
		obj.put("language_name", language_name);
		obj.put("language_id", language_id);
		obj.put("service_area", serviceArea);
		obj.put("broadcast_time", broadcastTime);
		ArrayList list = (ArrayList) statisticsInfo(obj);
		String fileName="";
		
		String downFileName = "";
		try{
			JExcel jExcel = new JExcel();
			downFileName=jExcel.openDocument();
			jExcel.WorkBookGetSheet(0);
			for(int i=0;i<list.size();i++){
				ASObject asobj = (ASObject) list.get(i);
				if(flag.equalsIgnoreCase("station")){
					jExcel.addDate(0, i+2,asobj.get("station").toString(),jExcel.dateCellFormat);
				}
				if(flag.equalsIgnoreCase("language")){
					jExcel.addDate(0, i+2,asobj.get("language").toString(),jExcel.dateCellFormat);
				}
				if(flag.equalsIgnoreCase("area")){
					jExcel.addDate(0, i+2,asobj.get("area").toString(),jExcel.dateCellFormat);
				}
				if(flag.equalsIgnoreCase("broadcast_time")){
					jExcel.addDate(0, i+2,asobj.get("broadcast_time").toString(),jExcel.dateCellFormat);
				}
				jExcel.addDate(1, i+2,asobj.get("bc_freq").toString(),jExcel.dateCellFormat);
				jExcel.addDate(2, i+2,asobj.get("bc_hours").toString(),jExcel.dateCellFormat);
				jExcel.addDate(3, i+2,asobj.get("sc_freq").toString(),jExcel.dateCellFormat);
				jExcel.addDate(4, i+2,asobj.get("sc_hours").toString(),jExcel.dateCellFormat);
				jExcel.getWorkSheet().setRowView(i+2, 600);
			}
			jExcel.getWorkSheet().setColumnView(0, 10);
	    	jExcel.getWorkSheet().setColumnView(1, 10);
	    	jExcel.getWorkSheet().setColumnView(2, 10);	
	    	jExcel.getWorkSheet().setColumnView(3, 12);
	    	jExcel.getWorkSheet().setColumnView(4, 10);
	    	jExcel.getWorkSheet().setColumnView(5, 10);	
	    	if(flag.equalsIgnoreCase("station")){
				fileName="各台频率频时数";
				jExcel.addDate(0, 0,"台名",jExcel.dateTITLEFormat);
				jExcel.getWorkSheet().setName("各台频率频时数");
			}
			if(flag.equalsIgnoreCase("language")){
				fileName="语言频率频时数";
				jExcel.addDate(0, 0,"语言",jExcel.dateTITLEFormat);
				jExcel.getWorkSheet().setName("语言频率频时数");
			}
			if(flag.equalsIgnoreCase("area")){
				fileName="地区频率频时数";
				jExcel.addDate(0, 0,"地区",jExcel.dateTITLEFormat);
				jExcel.getWorkSheet().setName("地区频率频时数");
			}
			if(flag.equalsIgnoreCase("broadcast_time")){
				jExcel.getWorkSheet().setColumnView(0, 15);
				fileName="播音时段频率频时数";
				jExcel.addDate(0, 0,"播音时段",jExcel.dateTITLEFormat);
				jExcel.getWorkSheet().setName("播音时段频率频时数");
			}
	    	jExcel.addDate(1, 0,"播出统计",jExcel.dateTITLEFormat);
	    	jExcel.addDate(3, 0,"收测统计",jExcel.dateTITLEFormat);
	    	jExcel.addDate(1, 1,"频率数",jExcel.dateTITLEFormat);
	    	jExcel.addDate(2, 1,"频时数",jExcel.dateTITLEFormat);
	    	jExcel.addDate(3, 1,"频率数",jExcel.dateTITLEFormat);
	    	jExcel.addDate(4, 1,"频时数",jExcel.dateTITLEFormat);
	    	jExcel.mergeCells(0,0,0,1);
	    	jExcel.mergeCells(1,0,2,0);
	    	jExcel.mergeCells(3,0,4,0);
	    	jExcel.getWorkSheet().setRowView(0, 600);
	    	jExcel.getWorkSheet().setRowView(1, 400);
	    	
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
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
