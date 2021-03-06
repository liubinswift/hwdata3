package com.viewscenes.web.runplanStatistics;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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

public class HWRunplanStatistics {

	//转播机构列表
	private ArrayList redisseminatorsList=new ArrayList();
	//语言列表
	private ArrayList languageList = new ArrayList();
	//服务区列表
	private ArrayList areaList = new ArrayList();
	//播音时段列表
	private ArrayList broadcastList = new ArrayList();
	//站点
	private HashMap websiteMap = new HashMap();

	public Object statisticsInfo(ASObject obj){
		ArrayList list = new ArrayList();
		String flag = (String) obj.get("flag");
		//	String station = (String) obj.get("station_name");
		String language_id = (String) obj.get("language_id");
		String languageName= (String) obj.get("language_name");
		String serviceArea = (String) obj.get("service_area");
		String broadcastTime =(String) obj.get("broadcast_time");
		String websites=(String) obj.get("website");
		String reportDate =(String) obj.get("reportDate");
		String  doBefore=(String) obj.get("doBefore");
		
		double bc_total_freq = 0;
		double bc_total_hour = 0;
		double sc_total_freq = 0;
		double sc_total_hour = 0;
		double xgbc_total_freq = 0;
		double xgbc_total_hour = 0;
		try {
			if(flag.equalsIgnoreCase("redisseminators")){
				getRedisseminators(reportDate,doBefore);//获得所有转播机构
				for(int j=0;j<redisseminatorsList.size();j++){
					String redisseminators =(String) redisseminatorsList.get(j);
					HashMap map = calculateRunplanByRedisseminators(redisseminators,obj);
					Iterator iter = map.entrySet().iterator();
					while(iter.hasNext()){
						Map.Entry entry = (Map.Entry) iter.next();
						ASObject asobj = new ASObject();
						asobj.put("redisseminators",redisseminators);
						asobj.put("bc_freq",entry.getKey().toString().split("&")[0]);
						asobj.put("bc_hours",entry.getValue().toString().split("&")[0]);
						asobj.put("sc_freq",entry.getKey().toString().split("&")[1]);
						asobj.put("sc_hours",entry.getValue().toString().split("&")[1]);
						asobj.put("xgsc_freq",entry.getKey().toString().split("&")[2]);
						asobj.put("xgsc_hours",entry.getValue().toString().split("&")[2]);
						bc_total_freq+=Double.parseDouble(entry.getKey().toString().split("&")[0]);
						bc_total_hour+=Double.parseDouble(entry.getValue().toString().split("&")[0]);
						sc_total_freq+=Double.parseDouble(entry.getKey().toString().split("&")[1]);
						sc_total_hour+=Double.parseDouble(entry.getValue().toString().split("&")[1]);
						xgbc_total_freq+=Double.parseDouble(entry.getKey().toString().split("&")[2]);
						xgbc_total_hour+=Double.parseDouble(entry.getValue().toString().split("&")[2]);
						list.add(asobj);
					}


				}
				ASObject taotal = new ASObject();
				taotal.put("redisseminators","总计");
				taotal.put("bc_freq",bc_total_freq);
				taotal.put("bc_hours",StringTool.formatDouble(bc_total_hour,2));
				taotal.put("sc_freq",sc_total_freq);
				taotal.put("sc_hours",StringTool.formatDouble(sc_total_hour,2));
				taotal.put("xgsc_freq",xgbc_total_freq);
				taotal.put("xgsc_hours",StringTool.formatDouble(xgbc_total_hour,2));
				list.add(taotal);
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
						asobj.put("xgsc_freq",entry.getKey().toString().split("&")[2]);
						asobj.put("xgsc_hours",entry.getValue().toString().split("&")[2]);
						bc_total_freq+=Double.parseDouble(entry.getKey().toString().split("&")[0]);
						bc_total_hour+=Double.parseDouble(entry.getValue().toString().split("&")[0]);
						sc_total_freq+=Double.parseDouble(entry.getKey().toString().split("&")[1]);
						sc_total_hour+=Double.parseDouble(entry.getValue().toString().split("&")[1]);
						xgbc_total_freq+=Double.parseDouble(entry.getKey().toString().split("&")[2]);
						xgbc_total_hour+=Double.parseDouble(entry.getValue().toString().split("&")[2]);
						list.add(asobj);
					}
				}else{
					getLanguage(reportDate,doBefore);//获得语言
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
							asobj.put("xgsc_freq",entry.getKey().toString().split("&")[2]);
							asobj.put("xgsc_hours",entry.getValue().toString().split("&")[2]);
							bc_total_freq+=Double.parseDouble(entry.getKey().toString().split("&")[0]);
							bc_total_hour+=Double.parseDouble(entry.getValue().toString().split("&")[0]);
							sc_total_freq+=Double.parseDouble(entry.getKey().toString().split("&")[1]);
							sc_total_hour+=Double.parseDouble(entry.getValue().toString().split("&")[1]);
							xgbc_total_freq+=Double.parseDouble(entry.getKey().toString().split("&")[2]);
							xgbc_total_hour+=Double.parseDouble(entry.getValue().toString().split("&")[2]);
							list.add(asobj);
						}
					}
				}

				ASObject taotal = new ASObject();
				taotal.put("language","总计");
				taotal.put("bc_freq",bc_total_freq);
				taotal.put("bc_hours",StringTool.formatDouble(bc_total_hour, 2));
				taotal.put("sc_freq",sc_total_freq);
				taotal.put("sc_hours",StringTool.formatDouble(sc_total_hour,2));
				taotal.put("xgsc_freq",xgbc_total_freq);
				taotal.put("xgsc_hours",StringTool.formatDouble(xgbc_total_hour,2));
				list.add(taotal);
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
						asobj.put("xgsc_freq",entry.getKey().toString().split("&")[2]);
						asobj.put("xgsc_hours",entry.getValue().toString().split("&")[2]);
						bc_total_freq+=Double.parseDouble(entry.getKey().toString().split("&")[0]);
						bc_total_hour+=Double.parseDouble(entry.getValue().toString().split("&")[0]);
						sc_total_freq+=Double.parseDouble(entry.getKey().toString().split("&")[1]);
						sc_total_hour+=Double.parseDouble(entry.getValue().toString().split("&")[1]);
						xgbc_total_freq+=Double.parseDouble(entry.getKey().toString().split("&")[2]);
						xgbc_total_hour+=Double.parseDouble(entry.getValue().toString().split("&")[2]);
						list.add(asobj);
					}
				}else{
					getArea(reportDate,doBefore);//获得地区
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
							asobj.put("xgsc_freq",entry.getKey().toString().split("&")[2]);
							asobj.put("xgsc_hours",entry.getValue().toString().split("&")[2]);
							bc_total_freq+=Double.parseDouble(entry.getKey().toString().split("&")[0]);
							bc_total_hour+=Double.parseDouble(entry.getValue().toString().split("&")[0]);
							sc_total_freq+=Double.parseDouble(entry.getKey().toString().split("&")[1]);
							sc_total_hour+=Double.parseDouble(entry.getValue().toString().split("&")[1]);
							xgbc_total_freq+=Double.parseDouble(entry.getKey().toString().split("&")[2]);
							xgbc_total_hour+=Double.parseDouble(entry.getValue().toString().split("&")[2]);
							list.add(asobj);
						}
					}
				}
				ASObject taotal = new ASObject();
				taotal.put("area","总计");
				taotal.put("bc_freq",bc_total_freq);
				taotal.put("bc_hours",StringTool.formatDouble(bc_total_hour,2));
				taotal.put("sc_freq",sc_total_freq);
				taotal.put("sc_hours",StringTool.formatDouble(sc_total_hour,2));
				taotal.put("xgsc_freq",xgbc_total_freq);
				taotal.put("xgsc_hours",StringTool.formatDouble(xgbc_total_hour,2));
				list.add(taotal);
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
						asobj.put("xgsc_freq",entry.getKey().toString().split("&")[2]);
						asobj.put("xgsc_hours",entry.getValue().toString().split("&")[2]);
						bc_total_freq+=Double.parseDouble(entry.getKey().toString().split("&")[0]);
						bc_total_hour+=Double.parseDouble(entry.getValue().toString().split("&")[0]);
						sc_total_freq+=Double.parseDouble(entry.getKey().toString().split("&")[1]);
						sc_total_hour+=Double.parseDouble(entry.getValue().toString().split("&")[1]);
						xgbc_total_freq+=Double.parseDouble(entry.getKey().toString().split("&")[2]);
						xgbc_total_hour+=Double.parseDouble(entry.getValue().toString().split("&")[2]);
						list.add(asobj);
					}

				}else{
					getBroadcastTime(reportDate,doBefore);
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
							asobj.put("xgsc_freq",entry.getKey().toString().split("&")[2]);
							asobj.put("xgsc_hours",entry.getValue().toString().split("&")[2]);
							bc_total_freq+=Double.parseDouble(entry.getKey().toString().split("&")[0]);
							bc_total_hour+=Double.parseDouble(entry.getValue().toString().split("&")[0]);
							sc_total_freq+=Double.parseDouble(entry.getKey().toString().split("&")[1]);
							sc_total_hour+=Double.parseDouble(entry.getValue().toString().split("&")[1]);
							xgbc_total_freq+=Double.parseDouble(entry.getKey().toString().split("&")[2]);
							xgbc_total_hour+=Double.parseDouble(entry.getValue().toString().split("&")[2]);
							list.add(asobj);
						}
					}
					Collections.sort(list, new Comparator<ASObject>(){
						public int compare(ASObject obj1,ASObject obj2){
							return obj1.get("broadcast_time").toString().compareTo(obj2.get("broadcast_time").toString());
						}
					});
				}
				ASObject taotal = new ASObject();
				taotal.put("broadcast_time","总计");
				taotal.put("bc_freq",bc_total_freq);
				taotal.put("bc_hours",StringTool.formatDouble(bc_total_hour,2));
				taotal.put("sc_freq",sc_total_freq);
				taotal.put("sc_hours",StringTool.formatDouble(sc_total_hour,2));
				taotal.put("xgsc_freq",xgbc_total_freq);
				taotal.put("xgsc_hours",StringTool.formatDouble(xgbc_total_hour,2));
				list.add(taotal);
			}

			if(flag.equalsIgnoreCase("website")){
				if(websites!=null&&!websites.equalsIgnoreCase("")){
					if(websites.indexOf(",")>=0){
						String[] s=websites.split(",");
						for(int i=0;i<s.length;i++){
							HashMap map = calculateRunplanByWebsite(s[i],obj);
							Iterator itera = map.entrySet().iterator();
							while(itera.hasNext()){
								Map.Entry entr = (Map.Entry) itera.next();
								ASObject asobj = new ASObject();
								asobj.put("website",s[i]);
								asobj.put("hour",StringTool.formatDouble(Double.valueOf(entr.getValue().toString()),2));
								asobj.put("freq",entr.getKey().toString());
								bc_total_freq+=Double.parseDouble(entr.getKey().toString());
								bc_total_hour+=Double.parseDouble(entr.getValue().toString());
								list.add(asobj);

							}
						}
					}else{
						HashMap map = calculateRunplanByWebsite(websites,obj);
						Iterator itera = map.entrySet().iterator();
						while(itera.hasNext()){
							Map.Entry entr = (Map.Entry) itera.next();
							ASObject asobj = new ASObject();
							asobj.put("website",websites);
							asobj.put("hour",StringTool.formatDouble(Double.valueOf(entr.getValue().toString()),2));
							asobj.put("freq",entr.getKey().toString());
							bc_total_freq+=Double.parseDouble(entr.getKey().toString());
							bc_total_hour+=Double.parseDouble(entr.getValue().toString());
							list.add(asobj);

						}
					}
				}else{
					getwebSite(reportDate,doBefore);
					List<Map.Entry<String,String>> mappingList = null;
					//通过ArrayList构造函数把map.entrySet()转换成list
					mappingList = new ArrayList<Map.Entry<String,String>>(websiteMap.entrySet());
					//通过比较器实现比较排序
					Collections.sort(mappingList, new Comparator<Map.Entry<String,String>>(){
						public int compare(Map.Entry<String,String> mapping1,Map.Entry<String,String> mapping2){
							return mapping1.getKey().compareTo(mapping2.getKey());
						}
					});
					//Iterator iter = websiteMap.entrySet().iterator();
					for(Map.Entry<String,String> mapping:mappingList){
						String website = mapping.getValue().toString();
						HashMap map = calculateRunplanByWebsite(website,obj);
						Iterator itera = map.entrySet().iterator();
						while(itera.hasNext()){
							Map.Entry entr = (Map.Entry) itera.next();
							ASObject asobj = new ASObject();
							asobj.put("website",website);
							asobj.put("hour",StringTool.formatDouble(Double.valueOf(entr.getValue().toString()),2));
							asobj.put("freq",entr.getKey().toString());
							bc_total_freq+=Double.parseDouble(entr.getKey().toString());
							bc_total_hour+=Double.parseDouble(entr.getValue().toString());
							list.add(asobj);

						}
					}
//					while(iter.hasNext()){
//						Map.Entry entry = (Entry) iter.next();
//					    String website = entry.getValue().toString();
//					    HashMap map = calculateRunplanByWebsite(website,obj);
//					    Iterator itera = map.entrySet().iterator();
//					    while(itera.hasNext()){
//					    	Map.Entry entr = (Map.Entry) itera.next();
//							ASObject asobj = new ASObject();
//							asobj.put("website",website);
//							asobj.put("hour",entr.getValue().toString());
//							asobj.put("freq",entr.getKey().toString());
//							bc_total_freq+=Double.parseDouble(entr.getKey().toString());
//							bc_total_hour+=Double.parseDouble(entr.getValue().toString());
//							list.add(asobj);
//
//					    }
//					}
				}


				ASObject asobj = new ASObject();
				asobj.put("website","总计");
				asobj.put("hour",StringTool.formatDouble(bc_total_hour,2));
				asobj.put("freq",StringTool.formatDouble(bc_total_freq,2));
				list.add(asobj);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new EXEException("", e.getMessage(), "");
		}
		return list;
	}
	/**
	 * 从运行图表中，取出有效的转播机构。
	 */
	public void getRedisseminators(String reportDate,String doBefore ){
		String sql="select distinct(t.redisseminators) from zres_runplan_tab t where t.runplan_type_id=2  ";
		if(reportDate!=null){
			sql+=" and t.valid_end_time>to_date('"+reportDate+"','yyyy-mm-dd hh24:mi:ss')  and t.valid_start_time<=to_date('"+reportDate+"','yyyy-mm-dd hh24:mi:ss') ";
		}
		if("0".equals(doBefore)){
			sql+=" and t.is_delete=0 ";
		}
		sql+="  order by t.redisseminators ";
		try {
			GDSet  gd = DbComponent.Query(sql);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					redisseminatorsList.add(gd.getString(i, "redisseminators"));
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
	public void getLanguage(String reportDate,String doBefore){
		String sql = "select  distinct(zlt.language_name) from zres_runplan_tab t,zdic_language_tab zlt where  t.runplan_type_id=2  and t.language_id=zlt.language_id  and zlt.is_delete=0 ";
		if(reportDate!=null){
			sql+=" and t.valid_end_time>to_date('"+reportDate+"','yyyy-mm-dd hh24:mi:ss')  and t.valid_start_time<=to_date('"+reportDate+"','yyyy-mm-dd hh24:mi:ss') ";
		}
		if("0".equals(doBefore)){
			sql+=" and t.is_delete=0 ";
		}
		sql+=" order by zlt.language_name ";
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
	public void getArea(String reportDate,String doBefore){

		String sql="select distinct(dst.chinese_name) from zres_runplan_tab t,dic_servicesarea_tab dst where  t.runplan_type_id=2  and t.service_area=dst.chinese_name ";
		if("0".equals(doBefore)){
			sql+=" and t.is_delete=0 ";
		}
		if(reportDate!=null){
			sql+=" and t.valid_end_time>to_date('"+reportDate+"','yyyy-mm-dd hh24:mi:ss')  and t.valid_start_time<=to_date('"+reportDate+"','yyyy-mm-dd hh24:mi:ss') ";
		}
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
	public void getBroadcastTime(String reportDate,String doBefore){
		String sql="select distinct(t.start_time||'-'||t.end_time) as broadcasttime from zres_runplan_tab t where 1=1 ";
		if("0".equals(doBefore)){
			sql+=" and t.is_delete=0 ";
		}
		if(reportDate!=null){
			sql+=" and t.valid_end_time>to_date('"+reportDate+"','yyyy-mm-dd hh24:mi:ss')  and t.valid_start_time<=to_date('"+reportDate+"','yyyy-mm-dd hh24:mi:ss') ";
		}
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
	 * 从运行图中获取收测站点
	 */
	public void getwebSite(String reportDate,String doBefore){
		String sql="select t.mon_area,t.xg_mon_area from zres_runplan_tab t where  (t.mon_area||t.xg_mon_area) is not null ";
		if("0".equals(doBefore)){
			sql+=" and t.is_delete=0 ";
		}
		if(reportDate!=null){
			sql+=" and t.valid_end_time>to_date('"+reportDate+"','yyyy-mm-dd hh24:mi:ss')  and t.valid_start_time<=to_date('"+reportDate+"','yyyy-mm-dd hh24:mi:ss') ";
		}
		try {
			GDSet gd = DbComponent.Query(sql);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					if(!gd.getString(i, "mon_area").equalsIgnoreCase("")){
						if(gd.getString(i, "mon_area").indexOf(",")>0){
							String[] s = gd.getString(i, "mon_area").split(",");
							for(int j=0;j<s.length;j++){
								websiteMap.put( s[j], s[j]);
							}
						}else websiteMap.put(gd.getString(i, "mon_area"), gd.getString(i, "mon_area"));
					}
					if(!gd.getString(i, "xg_mon_area").equalsIgnoreCase("")){
						if(gd.getString(i, "xg_mon_area").indexOf(",")>0){
							String[] s = gd.getString(i, "xg_mon_area").split(",");
							for(int k=0;k<s.length;k++){
								websiteMap.put( s[k], s[k]);
							}
						}else websiteMap.put(gd.getString(i, "xg_mon_area"), gd.getString(i, "xg_mon_area"));
					}


				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 根据发射台名称从运行图表中统计播出频率和频时
	 * @param redisseminators,obj
	 */
	public HashMap calculateRunplanByRedisseminators(String redisseminators,ASObject obj){
		String language_id = (String) obj.get("language_id");
		String language_name= (String) obj.get("language_name");
		String serviceArea = (String) obj.get("service_area");
		String broadcastTime =(String) obj.get("broadcast_time");
		String band = (String) obj.get("band");
		String season=(String) obj.get("season");
		String website = (String) obj.get("website");
		String sql="select * from zres_runplan_tab t where  t.runplan_type_id=2    and t.redisseminators like '%"+redisseminators+"%'";
		String sql1 = "select * from zres_runplan_tab t where  t.runplan_type_id=2   and t.redisseminators like '%"+redisseminators+"%' and t.mon_area is not null ";
		String sql2 = "select * from zres_runplan_tab t where  t.runplan_type_id=2    and t.redisseminators like '%"+redisseminators+"%' and t.xg_mon_area is not null ";
		String doBefore =  (String) obj.get("doBefore");
		if("0".equals(doBefore)){
			sql+=" and t.is_delete=0 ";
			sql1+=" and t.is_delete=0 ";
			sql2+=" and t.is_delete=0 ";
		}
		if(season!=null&&!season.equalsIgnoreCase("")){
			sql+=" and t.summer='"+season+"'";
			sql1+=" and t.summer='"+season+"'";
			sql2+=" and t.summer='"+season+"'";
		}
		String reportDate=(String) obj.get("reportDate");
		if(reportDate!=null){
			sql+=" and t.valid_end_time>to_date('"+reportDate+"','yyyy-mm-dd hh24:mi:ss')  and t.valid_start_time<=to_date('"+reportDate+"','yyyy-mm-dd hh24:mi:ss') ";
			sql1+=" and t.valid_end_time>to_date('"+reportDate+"','yyyy-mm-dd hh24:mi:ss') and t.valid_start_time<=to_date('"+reportDate+"','yyyy-mm-dd hh24:mi:ss') ";
			sql2+=" and t.valid_end_time>to_date('"+reportDate+"','yyyy-mm-dd hh24:mi:ss') and t.valid_start_time<=to_date('"+reportDate+"','yyyy-mm-dd hh24:mi:ss') ";
		}
		if(language_name!=null&&!language_name.equalsIgnoreCase("全部")){
			sql+=" and (t.language_id='"+language_id+"')";
			sql1+=" and t.language_id='"+language_id+"'";
			sql2+=" and t.language_id='"+language_id+"'";
		}
		if(serviceArea!=null&&!serviceArea.equalsIgnoreCase("全部")){
			sql+=" and t.service_area like '%"+serviceArea+"%'";
			sql1+=" and t.service_area like '%"+serviceArea+"%'";
			sql2+=" and t.service_area like '%"+serviceArea+"%'";
		}
		if(broadcastTime!=null&&!broadcastTime.equalsIgnoreCase("全部")){
			sql+=" and t.start_time='"+broadcastTime.split("-")[0]+"' and t.end_time='"+broadcastTime.split("-")[1]+"'";
			sql1+=" and t.start_time='"+broadcastTime.split("-")[0]+"' and t.end_time='"+broadcastTime.split("-")[1]+"'";
			sql2+=" and t.start_time='"+broadcastTime.split("-")[0]+"' and t.end_time='"+broadcastTime.split("-")[1]+"'";
		}
		if(!band.equalsIgnoreCase("")&&band!=null){
			if(band.equalsIgnoreCase("0")){//短波
				sql+=" and (t.freq>=2300 and t.freq<=26100) ";
				sql1+=" and (t.freq>=2300 and t.freq<=26100) ";
				sql2+=" and (t.freq>=2300 and t.freq<=26100) ";
			}
			if(band.equalsIgnoreCase("1")){
				sql+=" and (t.freq>=531 and t.freq<=1602) ";
				sql1+=" and (t.freq>=531 and t.freq<=1602) ";
				sql2+=" and (t.freq>=531 and t.freq<=1602) ";
			}
			if(band.equalsIgnoreCase("2")){
				sql+=" and (t.freq>=50000 and t.freq<=200000) ";
				sql1+=" and (t.freq>=50000 and t.freq<=200000) ";
				sql2+=" and (t.freq>=50000 and t.freq<=200000) ";
			}
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
				// sql+=" and (("+ss.substring(0, ss.length()-3)+") or ("+sss.substring(0, sss.length()-3)+"))";
				sql1+=" and ("+ss.substring(0, ss.length()-3)+")";
				sql2+=" and ("+sss.substring(0, sss.length()-3)+")";
			}else{
				// sql+=" and (t.mon_area like '%"+website+"%' or t.xg_mon_area like '%"+website+"%' )";
				sql1+=" and t.mon_area like '%"+website+"%' ";
				sql2+=" and t.xg_mon_area like '%"+website+"%' ";
			}
		}
		HashMap bc_map = new HashMap();
		HashMap sc_map = new HashMap();
		HashMap xgsc_map = new HashMap();
		HashMap result = new HashMap();
		double value=0.0;
		double value1=0.0;
		double value2=0.0;
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
			//统计质量收测频率和频时数
			GDSet gd1 = DbComponent.Query(sql1);
			if(gd1.getRowCount()>0){
				for(int i=0;i<gd1.getRowCount();i++){
					String key="";
					String start_time = gd1.getString(i, "start_time");
					String end_time = gd1.getString(i, "end_time");
					long time = 0;
					time = StringTool.getTimeSecond(start_time.trim()+":00",end_time.trim()+":00");
					double hours = time * 1.0 / 3600;
					hours = StringTool.formatDouble(hours,2);
					value1+=hours;
					key=gd1.getString(i, "station_name")+gd1.getString(i, "freq")+gd1.getString(i, "direction")+gd1.getString(i, "service_area");
					sc_map.put(key, hours);

				}
			}
			//统计效果收测频率和频时数
			GDSet gd2 = DbComponent.Query(sql2);
			if(gd2.getRowCount()>0){
				for(int i=0;i<gd2.getRowCount();i++){
					String key="";
					String start_time = gd2.getString(i, "start_time");
					String end_time = gd2.getString(i, "end_time");
					long time = 0;
					time = StringTool.getTimeSecond(start_time.trim()+":00",end_time.trim()+":00");
					double hours = time * 1.0 / 3600;
					hours = StringTool.formatDouble(hours,2);
					value2+=hours;
					key=gd2.getString(i, "station_name")+gd2.getString(i, "freq")+gd2.getString(i, "direction")+gd2.getString(i, "service_area");
					xgsc_map.put(key, hours);

				}
			}
			result.put(bc_map.size()+"&"+sc_map.size()+"&"+xgsc_map.size(), StringTool.formatDouble(value,2)+"&"+StringTool.formatDouble(value1,2)+"&"+StringTool.formatDouble(value2,2));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 根据语言名称从运行图表中统计播出频率和频时
	 * @param language_name
	 */
	public HashMap calculateRunplanByLanguage(String language_name,ASObject obj){
		String language_id = (String) obj.get("language_id");
		String languageName= (String) obj.get("language_name");
		String serviceArea = (String) obj.get("service_area");
		String broadcastTime =(String) obj.get("broadcast_time");
		String band = (String) obj.get("band");
		String website = (String) obj.get("website");
		String sql="select * from zres_runplan_tab t,zdic_language_tab zlt where  t.runplan_type_id=2    and t.language_id=zlt.language_id and zlt.language_name = '"+language_name+"' ";
		String sql1 = "select * from zres_runplan_tab t,zdic_language_tab zlt where  t.runplan_type_id=2   and t.language_id=zlt.language_id and zlt.language_name = '"+language_name+"' and t.mon_area is not null  ";
		String sql2 = "select * from zres_runplan_tab t,zdic_language_tab zlt where  t.runplan_type_id=2  and t.language_id=zlt.language_id and zlt.language_name = '"+language_name+"' and t.xg_mon_area is not null ";
		String doBefore =  (String) obj.get("doBefore");
		if("0".equals(doBefore)){
			sql+=" and t.is_delete=0 ";
			sql1+=" and t.is_delete=0 ";
			sql2+=" and t.is_delete=0 ";
		}
		if(languageName!=null&&!languageName.equalsIgnoreCase("全部")){
			sql+=" and t.language_id='"+language_id+"'";
			sql1+=" and t.language_id='"+language_id+"'";
			sql2+=" and t.language_id='"+language_id+"'";
		}
		String reportDate=(String) obj.get("reportDate");
		if(reportDate!=null){
			sql+=" and t.valid_end_time>to_date('"+reportDate+"','yyyy-mm-dd hh24:mi:ss')  and t.valid_start_time<=to_date('"+reportDate+"','yyyy-mm-dd hh24:mi:ss') ";
			sql1+=" and t.valid_end_time>to_date('"+reportDate+"','yyyy-mm-dd hh24:mi:ss') and t.valid_start_time<=to_date('"+reportDate+"','yyyy-mm-dd hh24:mi:ss') ";
			sql2+=" and t.valid_end_time>to_date('"+reportDate+"','yyyy-mm-dd hh24:mi:ss') and t.valid_start_time<=to_date('"+reportDate+"','yyyy-mm-dd hh24:mi:ss') ";
		}
		if(serviceArea!=null&&!serviceArea.equalsIgnoreCase("全部")){
			sql+=" and t.service_area like '%"+serviceArea+"%'";
			sql1+=" and t.service_area like '%"+serviceArea+"%'";
			sql2+=" and t.service_area like '%"+serviceArea+"%'";
		}
		if(broadcastTime!=null&&!broadcastTime.equalsIgnoreCase("全部")){
			sql+=" and t.start_time='"+broadcastTime.split("-")[0]+"' and t.end_time='"+broadcastTime.split("-")[1]+"'";
			sql1+=" and t.start_time='"+broadcastTime.split("-")[0]+"' and t.end_time='"+broadcastTime.split("-")[1]+"'";
			sql2+=" and t.start_time='"+broadcastTime.split("-")[0]+"' and t.end_time='"+broadcastTime.split("-")[1]+"'";
		}
		if(!band.equalsIgnoreCase("")&&band!=null){
			if(band.equalsIgnoreCase("0")){//短波
				sql+=" and (t.freq>=2300 and t.freq<=26100) ";
				sql1+=" and (t.freq>=2300 and t.freq<=26100) ";
				sql2+=" and (t.freq>=2300 and t.freq<=26100) ";
			}
			if(band.equalsIgnoreCase("1")){
				sql+=" and (t.freq>=531 and t.freq<=1602) ";
				sql1+=" and (t.freq>=531 and t.freq<=1602) ";
				sql2+=" and (t.freq>=531 and t.freq<=1602) ";
			}
			if(band.equalsIgnoreCase("2")){
				sql+=" and (t.freq>=50000 and t.freq<=200000) ";
				sql1+=" and (t.freq>=50000 and t.freq<=200000) ";
				sql2+=" and (t.freq>=50000 and t.freq<=200000) ";
			}
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
				// sql+=" and (("+ss.substring(0, ss.length()-3)+") or ("+sss.substring(0, sss.length()-3)+"))";
				sql1+=" and ("+ss.substring(0, ss.length()-3)+")";
				sql2+=" and ("+sss.substring(0, sss.length()-3)+")";
			}else{
				// sql+=" and (t.mon_area like '%"+website+"%' or t.xg_mon_area like '%"+website+"%' )";
				sql1+=" and t.mon_area like '%"+website+"%' ";
				sql2+=" and t.xg_mon_area like '%"+website+"%' ";
			}
		}

		sql+=" order by zlt.language_name ";
		sql1+=" order by zlt.language_name";
		sql2+=" order by zlt.language_name";
		HashMap bc_map = new HashMap();
		HashMap sc_map = new HashMap();
		HashMap xgsc_map = new HashMap();
		HashMap result = new HashMap();
		double value=0.0;
		double value1=0.0;
		double value2=0.0;
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
			//统计质量收测频率和频时数
			GDSet gd1 = DbComponent.Query(sql1);
			if(gd1.getRowCount()>0){
				for(int i=0;i<gd1.getRowCount();i++){
					String key="";
					String start_time = gd1.getString(i, "start_time");
					String end_time = gd1.getString(i, "end_time");
					long time = 0;
					time = StringTool.getTimeSecond(start_time.trim()+":00",end_time.trim()+":00");
					double hours = time * 1.0 / 3600;
					hours = StringTool.formatDouble(hours,2);
					value1+=hours;
					key=gd1.getString(i, "station_name")+gd1.getString(i, "freq")+gd1.getString(i, "direction")+gd1.getString(i, "service_area");
					sc_map.put(key, hours);

				}
			}
			//统计效果收测频率和频时数
			GDSet gd2 = DbComponent.Query(sql2);
			if(gd2.getRowCount()>0){
				for(int i=0;i<gd2.getRowCount();i++){
					String key="";
					String start_time = gd2.getString(i, "start_time");
					String end_time = gd2.getString(i, "end_time");
					long time = 0;
					time = StringTool.getTimeSecond(start_time.trim()+":00",end_time.trim()+":00");
					double hours = time * 1.0 / 3600;
					hours = StringTool.formatDouble(hours,2);
					value2+=hours;
					key=gd2.getString(i, "station_name")+gd2.getString(i, "freq")+gd2.getString(i, "direction")+gd2.getString(i, "service_area");
					xgsc_map.put(key, hours);

				}
			}
			result.put(bc_map.size()+"&"+sc_map.size()+"&"+xgsc_map.size(), StringTool.formatDouble(value,2)+"&"+StringTool.formatDouble(value1,2)+"&"+StringTool.formatDouble(value2,2));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 根据 服务区名称从运行图表中统计播出频率和频时
	 * @param area
	 */
	public HashMap calculateRunplanByArea(String area,ASObject obj){
		String language_id = (String) obj.get("language_id");
		String languageName= (String) obj.get("language_name");
		String serviceArea = (String) obj.get("service_area");
		String broadcastTime =(String) obj.get("broadcast_time");
		String band = (String) obj.get("band");
		String website = (String) obj.get("website");
		String sql="select * from zres_runplan_tab t where  t.runplan_type_id=2   and t.service_area like '%"+area+"%' order by t.service_area";
		String sql1 = "select * from zres_runplan_tab t where  t.runplan_type_id=2   and t.service_area like '%"+area+"%' and t.mon_area is not null  order by t.service_area";
		String sql2 = "select * from zres_runplan_tab t where  t.runplan_type_id=2   and t.service_area like '%"+area+"%' and t.xg_mon_area is not null order by t.service_area";
		String doBefore =  (String) obj.get("doBefore");
		if("0".equals(doBefore)){
			sql+=" and t.is_delete=0 ";
			sql1+=" and t.is_delete=0 ";
			sql2+=" and t.is_delete=0 ";
		}
		if(languageName!=null&&!languageName.equalsIgnoreCase("全部")){
			sql+=" and t.language_id='"+language_id+"'";
			sql1+=" and t.language_id='"+language_id+"'";
			sql2+=" and t.language_id='"+language_id+"'";
		}
		String reportDate=(String) obj.get("reportDate");
		if(reportDate!=null){
			sql+=" and t.valid_end_time>to_date('"+reportDate+"','yyyy-mm-dd hh24:mi:ss')  and t.valid_start_time<=to_date('"+reportDate+"','yyyy-mm-dd hh24:mi:ss') ";
			sql1+=" and t.valid_end_time>to_date('"+reportDate+"','yyyy-mm-dd hh24:mi:ss') and t.valid_start_time<=to_date('"+reportDate+"','yyyy-mm-dd hh24:mi:ss') ";
			sql2+=" and t.valid_end_time>to_date('"+reportDate+"','yyyy-mm-dd hh24:mi:ss') and t.valid_start_time<=to_date('"+reportDate+"','yyyy-mm-dd hh24:mi:ss') ";
		}
		if(serviceArea!=null&&!serviceArea.equalsIgnoreCase("全部")){
			sql+=" and t.service_area like '%"+serviceArea+"%'";
			sql1+=" and t.service_area like '%"+serviceArea+"%'";
			sql2+=" and t.service_area like '%"+serviceArea+"%'";
		}
		if(broadcastTime!=null&&!broadcastTime.equalsIgnoreCase("全部")){
			sql+=" and t.start_time='"+broadcastTime.split("-")[0]+"' and t.end_time='"+broadcastTime.split("-")[1]+"'";
			sql1+=" and t.start_time='"+broadcastTime.split("-")[0]+"' and t.end_time='"+broadcastTime.split("-")[1]+"'";
			sql2+=" and t.start_time='"+broadcastTime.split("-")[0]+"' and t.end_time='"+broadcastTime.split("-")[1]+"'";
		}
		if(!band.equalsIgnoreCase("")&&band!=null){
			if(band.equalsIgnoreCase("0")){//短波
				sql+=" and (t.freq>=2300 and t.freq<=26100) ";
				sql1+=" and (t.freq>=2300 and t.freq<=26100) ";
				sql2+=" and (t.freq>=2300 and t.freq<=26100) ";
			}
			if(band.equalsIgnoreCase("1")){
				sql+=" and (t.freq>=531 and t.freq<=1602) ";
				sql1+=" and (t.freq>=531 and t.freq<=1602) ";
				sql2+=" and (t.freq>=531 and t.freq<=1602) ";
			}
			if(band.equalsIgnoreCase("2")){
				sql+=" and (t.freq>=50000 and t.freq<=200000) ";
				sql1+=" and (t.freq>=50000 and t.freq<=200000) ";
				sql2+=" and (t.freq>=50000 and t.freq<=200000) ";
			}
		}
		if(website!=null&&!website.equalsIgnoreCase("")){
			if(website.indexOf(",")>=0){
				String[] s = website.split(",");
				String ss="";
				String sss="";
				for(int i=0;i<s.length;i++){
					ss+=" t.mon_area like '%"+s[i]+"%' or ";
					sss+=" t.xg_mon_area like '%S"+s[i]+"%' or ";
				}
				sql+=" and (("+ss.substring(0, ss.length()-3)+") or ("+sss.substring(0, sss.length()-3)+"))";
				sql1+=" and ("+ss.substring(0, ss.length()-3)+")";
				sql2+=" and ("+sss.substring(0, sss.length()-3)+")";
			}else{
				sql+=" and (t.mon_area like '%"+website+"%' or t.xg_mon_area like '%"+website+"%' )";
				sql1+=" and t.mon_area like '%"+website+"%' ";
				sql2+=" and t.xg_mon_area like '%"+website+"%' ";
			}
		}
		HashMap bc_map = new HashMap();
		HashMap sc_map = new HashMap();
		HashMap xgsc_map = new HashMap();
		HashMap result = new HashMap();
		double value=0.0;
		double value1=0.0;
		double value2=0.0;
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
			//统计质量收测频率和频时数
			GDSet gd1 = DbComponent.Query(sql1);
			if(gd1.getRowCount()>0){
				for(int i=0;i<gd1.getRowCount();i++){
					String key="";
					String start_time = gd1.getString(i, "start_time");
					String end_time = gd1.getString(i, "end_time");
					long time = 0;
					time = StringTool.getTimeSecond(start_time.trim()+":00",end_time.trim()+":00");
					double hours = time * 1.0 / 3600;
					hours = StringTool.formatDouble(hours,2);
					value1+=hours;
					key=gd1.getString(i, "station_name")+gd1.getString(i, "freq")+gd1.getString(i, "direction")+gd1.getString(i, "service_area");
					sc_map.put(key, hours);

				}
			}
			//统计效果收测频率和频时数
			GDSet gd2 = DbComponent.Query(sql2);
			if(gd2.getRowCount()>0){
				for(int i=0;i<gd1.getRowCount();i++){
					String key="";
					String start_time = gd2.getString(i, "start_time");
					String end_time = gd2.getString(i, "end_time");
					long time = 0;
					time = StringTool.getTimeSecond(start_time.trim()+":00",end_time.trim()+":00");
					double hours = time * 1.0 / 3600;
					hours = StringTool.formatDouble(hours,2);
					value2+=hours;
					key=gd2.getString(i, "station_name")+gd2.getString(i, "freq")+gd2.getString(i, "direction")+gd2.getString(i, "service_area");
					xgsc_map.put(key, hours);

				}
			}
			result.put(bc_map.size()+"&"+sc_map.size()+"&"+xgsc_map.size(), value+"&"+value1+"&"+value2);
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
		String sql="select * from zres_runplan_tab t where  t.runplan_type_id=2   and t.start_time='"+broadcastTime.split("-")[0]+"' and t.end_time='"+broadcastTime.split("-")[1]+"' ";
		String sql1 = "select * from zres_runplan_tab t where  t.runplan_type_id=2  and t.start_time='"+broadcastTime.split("-")[0]+"' and t.end_time='"+broadcastTime.split("-")[1]+"'" +
			" and t.mon_area is not null  ";
		String sql2 = "select * from zres_runplan_tab t where  t.runplan_type_id=2  and t.start_time='"+broadcastTime.split("-")[0]+"' and t.end_time='"+broadcastTime.split("-")[1]+"'" +
			" and t.xg_mon_area is not null ";
		//String stationName = (String) obj.get("station_name");
		//String station_id = (String) obj.get("station_id");
		String language_name = (String) obj.get("language_name");
		String language_id = (String) obj.get("language_id");
		String serviceArea = (String) obj.get("service_area");
		String band = (String) obj.get("band");
		String website = (String) obj.get("website");
		String doBefore =  (String) obj.get("doBefore");
		if("0".equals(doBefore)){
			sql+=" and t.is_delete=0 ";
			sql1+=" and t.is_delete=0 ";
			sql2+=" and t.is_delete=0 ";
		}
		if(language_name!=null&&!language_name.equalsIgnoreCase("全部")){
			sql+=" and t.language_id='"+language_id+"'";
			sql1+=" and t.language_id='"+language_id+"'";
			sql2+=" and t.language_id='"+language_id+"'";
		}
		String reportDate=(String) obj.get("reportDate");
		if(reportDate!=null){
			sql+=" and t.valid_end_time>to_date('"+reportDate+"','yyyy-mm-dd hh24:mi:ss')  and t.valid_start_time<=to_date('"+reportDate+"','yyyy-mm-dd hh24:mi:ss') ";
			sql1+=" and t.valid_end_time>to_date('"+reportDate+"','yyyy-mm-dd hh24:mi:ss') and t.valid_start_time<=to_date('"+reportDate+"','yyyy-mm-dd hh24:mi:ss') ";
			sql2+=" and t.valid_end_time>to_date('"+reportDate+"','yyyy-mm-dd hh24:mi:ss') and t.valid_start_time<=to_date('"+reportDate+"','yyyy-mm-dd hh24:mi:ss') ";
		}
		if(serviceArea!=null&&!serviceArea.equalsIgnoreCase("全部")){
			sql+=" and t.service_area like '%"+serviceArea+"%'";
			sql1+=" and t.service_area like '%"+serviceArea+"%'";
			sql2+=" and t.service_area like '%"+serviceArea+"%'";
		}
		if(!band.equalsIgnoreCase("")&&band!=null){
			if(band.equalsIgnoreCase("0")){//短波
				sql+=" and (t.freq>=2300 and t.freq<=26100) ";
				sql1+=" and (t.freq>=2300 and t.freq<=26100) ";
				sql2+=" and (t.freq>=2300 and t.freq<=26100) ";
			}
			if(band.equalsIgnoreCase("1")){
				sql+=" and (t.freq>=531 and t.freq<=1602) ";
				sql1+=" and (t.freq>=531 and t.freq<=1602) ";
				sql2+=" and (t.freq>=531 and t.freq<=1602) ";
			}
			if(band.equalsIgnoreCase("2")){
				sql+=" and (t.freq>=50000 and t.freq<=200000) ";
				sql1+=" and (t.freq>=50000 and t.freq<=200000) ";
				sql2+=" and (t.freq>=50000 and t.freq<=200000) ";
			}
		}
		if(website!=null&&!website.equalsIgnoreCase("")){
			if(website.indexOf(",")>=0){
				String[] s = website.split(",");
				String ss="";
				String sss="";
				for(int i=0;i<s.length;i++){
					ss+=" t.mon_area like '"+s[i]+"' or ";
					sss+=" t.xg_mon_area like '"+s[i]+"' or ";
				}
				// sql+=" and (("+ss.substring(0, ss.length()-3)+") or ("+sss.substring(0, sss.length()-3)+"))";
				sql1+=" and ("+ss.substring(0, ss.length()-3)+")";
				sql2+=" and ("+sss.substring(0, sss.length()-3)+")";
			}else{
				// sql+=" and (t.mon_area like '%"+website+"%' or t.xg_mon_area like '%"+website+"%' )";
				sql1+=" and t.mon_area like '%"+website+"%' ";
				sql2+=" and t.xg_mon_area like '%"+website+"%' ";
			}
		}
		sql+=" order by t.start_time";
		sql1+=" order by t.start_time";
		sql2+=" order by t.start_time";
		HashMap bc_map = new HashMap();
		HashMap sc_map = new HashMap();
		HashMap xgsc_map = new HashMap();
		HashMap result = new HashMap();
		double value=0.0;
		double value1=0.0;
		double value2=0.0;
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
			//统计质量收测频率和频时数
			GDSet gd1 = DbComponent.Query(sql1);
			if(gd1.getRowCount()>0){
				for(int i=0;i<gd1.getRowCount();i++){
					String key="";
					String start_time = gd1.getString(i, "start_time");
					String end_time = gd1.getString(i, "end_time");
					long time = 0;
					time = StringTool.getTimeSecond(start_time.trim()+":00",end_time.trim()+":00");
					double hours = time * 1.0 / 3600;
					hours = StringTool.formatDouble(hours,2);
					value1+=hours;
					key=gd1.getString(i, "station_name")+gd1.getString(i, "freq");//+gd1.getString(i, "direction")+gd1.getString(i, "service_area");
					sc_map.put(key, hours);

				}
			}
			//统计效果收测频率和频时数
			GDSet gd2 = DbComponent.Query(sql2);
			if(gd2.getRowCount()>0){
				for(int i=0;i<gd2.getRowCount();i++){
					String key="";
					String start_time = gd1.getString(i, "start_time");
					String end_time = gd1.getString(i, "end_time");
					long time = 0;
					time = StringTool.getTimeSecond(start_time.trim()+":00",end_time.trim()+":00");
					double hours = time * 1.0 / 3600;
					hours = StringTool.formatDouble(hours,2);
					value2+=hours;
					key=gd2.getString(i, "station_name")+gd2.getString(i, "freq");//+gd1.getString(i, "direction")+gd1.getString(i, "service_area");
					xgsc_map.put(key, hours);

				}
			}
			result.put(bc_map.size()+"&"+sc_map.size()+"&"+xgsc_map.size(), value+"&"+value1+"&"+value2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 根据站点code从运行图表中统计播出频率和频时
	 * @param website
	 */
	public HashMap calculateRunplanByWebsite(String website,ASObject obj){
		String sql="select * from zres_runplan_tab t where  t.runplan_type_id=2   and (t.mon_area like '%"+website+"%' or t.xg_mon_area like '%"+website+"%') ";
		//String sql1 = "select * from zres_runplan_tab t where  t.runplan_type_id=1 and t.station_name like '%"+station_name+"%' and t.mon_area is not null ";
		//String sql2 = "select * from zres_runplan_tab t where  t.runplan_type_id=1 and t.station_name like '%"+station_name+"%' and t.xg_mon_area is not null";
		String language_name = (String) obj.get("language_name");
		String language_id = (String) obj.get("language_id");
		String serviceArea = (String) obj.get("service_area");
		String broadcastTime =(String) obj.get("broadcast_time");
		String band = (String) obj.get("band");
		String doBefore =  (String) obj.get("doBefore");
		if("0".equals(doBefore)){
			sql+=" and t.is_delete=0 ";
		}
		if(language_name!=null&&!language_name.equalsIgnoreCase("全部")){
			sql+=" and t.language_id='"+language_id+"'";
			//sql1+=" and t.language_id='"+language_id+"'";
			//sql2+=" and t.language_id='"+language_id+"'";
		}
		String reportDate=(String) obj.get("reportDate");
		if(reportDate!=null){
			sql+=" and t.valid_end_time>to_date('"+reportDate+"','yyyy-mm-dd hh24:mi:ss')  and t.valid_start_time<=to_date('"+reportDate+"','yyyy-mm-dd hh24:mi:ss') ";
		}
		if(serviceArea!=null&&!serviceArea.equalsIgnoreCase("全部")){
			sql+=" and t.service_area like '%"+serviceArea+"%'";
			//sql1+=" and t.service_area like '%"+serviceArea+"%'";
			//sql2+=" and t.service_area like '%"+serviceArea+"%'";
		}
		if(broadcastTime!=null&&!broadcastTime.equalsIgnoreCase("全部")){
			sql+=" and t.start_time='"+broadcastTime.split("-")[0]+"' and t.end_time='"+broadcastTime.split("-")[1]+"'";
			//sql1+=" and t.start_time='"+broadcastTime.split("-")[0]+"' and t.end_time='"+broadcastTime.split("-")[1]+"'";
			//sql2+=" and t.start_time='"+broadcastTime.split("-")[0]+"' and t.end_time='"+broadcastTime.split("-")[1]+"'";
		}
		if(!band.equalsIgnoreCase("")&&band!=null){
			if(band.equalsIgnoreCase("0")){//短波
				sql+=" and (t.freq>=2300 and t.freq<=26100) ";
				//	sql1+=" and (t.freq>=2300 and t.freq<=26100) ";
				//sql2+=" and (t.freq>=2300 and t.freq<=26100) ";
			}
			if(band.equalsIgnoreCase("1")){
				sql+=" and (t.freq>=531 and t.freq<=1602) ";
				//sql1+=" and (t.freq>=531 and t.freq<=1602) ";
				//sql2+=" and (t.freq>=531 and t.freq<=1602) ";
			}
		}
		HashMap bc_map = new HashMap();
		HashMap sc_map = new HashMap();
		HashMap xgsc_map = new HashMap();
		HashMap result = new HashMap();
		double value=0.0;
		double value1=0.0;
		double value2=0.0;
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
					key=website+gd.getString(i, "freq");
					//key=gd.getString(i, "freq")+gd.getString(i, "direction")+gd.getString(i, "service_area");//频率统计现在只考虑发射台和频率，不考虑方向和服务区了。
					bc_map.put(key, hours);

				}

			}
//			//统计质量收测频率和频时数
//			GDSet gd1 = DbComponent.Query(sql1);
//			if(gd1.getRowCount()>0){
//				for(int i=0;i<gd1.getRowCount();i++){
//					String key="";
//					String start_time = gd1.getString(i, "start_time");
//					String end_time = gd1.getString(i, "end_time");
//					long time = 0;
//					time = StringTool.getTimeSecond(start_time.trim()+":00",end_time.trim()+":00");
//                    double hours = time * 1.0 / 3600;
//                    hours = StringTool.formatDouble(hours,2);
//                    value1+=hours;
//                    key=gd1.getString(i, "freq")+station_name;
//					//key=gd1.getString(i, "freq")+gd1.getString(i, "direction")+gd1.getString(i, "service_area");
//					sc_map.put(key, hours);
//
//				}
//			}
//			//统计效果收测频率和频时数
//			GDSet gd2 = DbComponent.Query(sql2);
//			if(gd2.getRowCount()>0){
//				for(int i=0;i<gd2.getRowCount();i++){
//					String key="";
//					String start_time = gd2.getString(i, "start_time");
//					String end_time = gd2.getString(i, "end_time");
//					long time = 0;
//					time = StringTool.getTimeSecond(start_time.trim()+":00",end_time.trim()+":00");
//                    double hours = time * 1.0 / 3600;
//                    hours = StringTool.formatDouble(hours,2);
//                    value2+=hours;
//                    key=gd2.getString(i, "freq")+station_name;
//					xgsc_map.put(key, hours);
//
//				}
//			}
			result.put(bc_map.size(), value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public void doExcel(String msg,HttpServletRequest request,HttpServletResponse response){
		Element root = StringTool.getXMLRoot(msg);
		String flag = root.getChildText("flag");
		String language_id = root.getChildText("language_id");
		String language_name= root.getChildText("language_name");
		String serviceArea = root.getChildText("service_area");
		String broadcastTime =root.getChildText("broadcast_time");
		String band = root.getChildText("band");
		String reportDate = root.getChildText("reportDate");
		String websites = root.getChildText("websites");
		ASObject obj = new ASObject();
		obj.put("flag", flag);
		obj.put("language_name", language_name);
		obj.put("language_id", language_id);
		obj.put("service_area", serviceArea);
		obj.put("broadcast_time", broadcastTime);
		obj.put("band", band);
		obj.put("reportDate", reportDate);
		obj.put("website", websites);
		ArrayList list = (ArrayList) statisticsInfo(obj);
		String fileName="";

		String downFileName = "";
		try{
			JExcel jExcel = new JExcel();
			downFileName=jExcel.openDocument();
			jExcel.WorkBookGetSheet(0);
			for(int i=0;i<list.size();i++){
				ASObject asobj = (ASObject) list.get(i);
				if(flag.equalsIgnoreCase("redisseminators")){
					jExcel.addDate(0, i+2,asobj.get("redisseminators").toString(),jExcel.dateCellFormat);
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
				jExcel.addDate(5, i+2,asobj.get("xgsc_freq").toString(),jExcel.dateCellFormat);
				jExcel.addDate(6, i+2,asobj.get("xgsc_hours").toString(),jExcel.dateCellFormat);
				jExcel.getWorkSheet().setRowView(i+2, 600);
			}
			jExcel.getWorkSheet().setColumnView(0, 10);
			jExcel.getWorkSheet().setColumnView(1, 10);
			jExcel.getWorkSheet().setColumnView(2, 10);
			jExcel.getWorkSheet().setColumnView(3, 12);
			jExcel.getWorkSheet().setColumnView(4, 10);
			jExcel.getWorkSheet().setColumnView(5, 10);
			jExcel.getWorkSheet().setColumnView(6, 10);
			if(flag.equalsIgnoreCase("redisseminators")){
				fileName="转播机构频率频时数";
				jExcel.addDate(0, 0,"转播机构",jExcel.dateTITLEFormat);
				jExcel.getWorkSheet().setName("转播机构频率频时数");
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
			jExcel.addDate(3, 0,"质量收测统计",jExcel.dateTITLEFormat);
			jExcel.addDate(5, 0,"效果收测统计",jExcel.dateTITLEFormat);
			jExcel.addDate(1, 1,"频率数",jExcel.dateTITLEFormat);
			jExcel.addDate(2, 1,"频时数",jExcel.dateTITLEFormat);
			jExcel.addDate(3, 1,"频率数",jExcel.dateTITLEFormat);
			jExcel.addDate(4, 1,"频时数",jExcel.dateTITLEFormat);
			jExcel.addDate(5, 1,"频率数",jExcel.dateTITLEFormat);
			jExcel.addDate(6, 1,"频时数",jExcel.dateTITLEFormat);
			jExcel.mergeCells(0,0,0,1);
			jExcel.mergeCells(1,0,2,0);
			jExcel.mergeCells(3,0,4,0);
			jExcel.mergeCells(5,0,6,0);
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

	/**
	 * 站点频率频时数统计导出Excel
	 * @param msg
	 * @param request
	 * @param response
	 */

	public void doExcel1(String msg,HttpServletRequest request,HttpServletResponse response){
		Element root = StringTool.getXMLRoot(msg);
		String flag = root.getChildText("flag");
		String station = root.getChildText("station_name");
		String station_id = root.getChildText("station_id");
		String language_id = root.getChildText("language_id");
		String language_name= root.getChildText("language_name");
		String serviceArea = root.getChildText("service_area");
		String broadcastTime =root.getChildText("broadcast_time");
		String band = root.getChildText("band");
		String websites = root.getChildText("websites");
		String reportDate = root.getChildText("reportDate");
		ASObject obj = new ASObject();
		obj.put("flag", flag);
		obj.put("station_name", station);
		obj.put("station_id", station_id);
		obj.put("language_name", language_name);
		obj.put("language_id", language_id);
		obj.put("service_area", serviceArea);
		obj.put("broadcast_time", broadcastTime);
		obj.put("band", band);
		obj.put("website", websites);
		obj.put("reportDate", reportDate);
		ArrayList list = (ArrayList) statisticsInfo(obj);
		String fileName="站点频率频时数";

		String downFileName = "";
		try{
			JExcel jExcel = new JExcel();
			downFileName=jExcel.openDocument();
			jExcel.WorkBookGetSheet(0);
			for(int i=0;i<list.size();i++){
				ASObject asobj = (ASObject) list.get(i);
				jExcel.addDate(0, i+1,asobj.get("website").toString(),jExcel.dateCellFormat);
				jExcel.addDate(1, i+1,asobj.get("freq").toString(),jExcel.dateCellFormat);
				jExcel.addDate(2, i+1,asobj.get("hour").toString(),jExcel.dateCellFormat);
				jExcel.getWorkSheet().setRowView(i+1, 600);
			}
			jExcel.getWorkSheet().setColumnView(0, 10);
			jExcel.getWorkSheet().setColumnView(1, 10);
			jExcel.getWorkSheet().setColumnView(2, 10);

			jExcel.addDate(0, 0,"站点",jExcel.dateTITLEFormat);
			jExcel.addDate(1, 0,"频率数",jExcel.dateTITLEFormat);
			jExcel.addDate(2, 0,"频时数",jExcel.dateTITLEFormat);
			jExcel.getWorkSheet().setRowView(0, 600);
			jExcel.getWorkSheet().setRowView(1, 400);

			jExcel.getWorkSheet().setName("站点频率频时数");
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
