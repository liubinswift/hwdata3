package com.viewscenes.web.statistics.ykz;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WriteException;

import org.jdom.Element;

import com.viewscenes.bean.EffectStatCommonBean;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.printexcel.ExcelTitle;
import com.viewscenes.printexcel.JExcel;
import com.viewscenes.pub.GDSet;
import com.viewscenes.pub.GDSetException;
import com.viewscenes.util.StringTool;

import edu.emory.mathcs.backport.java.util.Collections;
import flex.messaging.io.amf.ASObject;

public class EffectStatByYkz {
	
//	private static WritableFont titleWf = null;
//	private static WritableCellFormat titleWcf = null;
//	private static WritableFont bodyWf = null;
//	private static WritableCellFormat bodyWcf = null;
//	static{
//		titleWf = new WritableFont(WritableFont.createFont("宋体"), 14,WritableFont.BOLD, false);
//	    titleWcf = new WritableCellFormat(titleWf);
//	    
//	    bodyWf = new WritableFont(WritableFont.createFont("宋体"), 10,WritableFont.NO_BOLD, false);
//		bodyWcf = new WritableCellFormat(bodyWf);
//		
//		try {
//			titleWcf.setAlignment(jxl.format.Alignment.CENTRE);
//			titleWcf.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
//			titleWcf.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); 
//			
//			
//			
//            // 把水平对齐方式指定为左对齐
//			bodyWcf.setAlignment(jxl.format.Alignment.CENTRE);
//            // 把垂直对齐方式指定为居中对齐
//			bodyWcf.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
//			bodyWcf.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); 
//		} catch (WriteException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//	}
	
	
	//统计类型
	private  final String A = "a";
	private  final String B = "b";
	private  final String C = "c";
	/**
	 * 6.1.6.6遥控站为基础的效果统计功能
	 * <p>class/function:com.viewscenes.web.statistics.ykz
	 * <p>explain:
	 * <p>author-date:谭长伟 2013-2-26
	 * @param:
	 * @return:
	 */
	public Object statEffectByYkz(ASObject obj){
		
		ASObject resultObj = new ASObject();
		resultObj.put(A, new ArrayList<EffectStatCommonBean>());
		resultObj.put(B, new ArrayList<EffectStatCommonBean>());
		resultObj.put(C, new ArrayList<EffectStatCommonBean>());
		String xml ="<chart yAxisName='可听率 单位:(%)' xAxisName='遥控站' caption='基于遥控站的效果统计可听率' baseFontSize='15' showBorder='1' imageSave='1'>";
//		ArrayList<EffectStatCommonBean> resultList = new ArrayList<EffectStatCommonBean>();
		//遥控站
		String headcode = (String)obj.get("headcode");	
		String startDate = (String)obj.get("startDate");	
		String endDate = (String)obj.get("endDate");	
		//统计类型	对比，统计
		String statType = (String)obj.get("statType");
		//a:根据遥控站统计,b:根据遥控站语言统计,c:根据遥控站发射台统计
		String fields = (String)obj.get("fields");	
		StringBuffer sql = new StringBuffer();
		sql.append(" select distinct t.mark_datetime,t.headname,t.head_code,t.frequency,run.start_time,run.end_time,run.language_name,t.station_name,t.counto  ");
		sql.append(" from radio_mark_zst_view_tab t, ");
		sql.append(" (select distinct t.*, lan.language_name ");
		sql.append(" from zres_runplan_chaifen_tab t,(select * from zdic_language_tab where is_delete = 0) lan ");
		sql.append(" where 1 = 1 and t.language_id = lan.language_id(+)) run ");
//		sql.append(" res_headend_tab rd ");
		sql.append(" where t.runplan_id = run.runplan_id(+) ");
		sql.append(" and run.runplan_type_id = 1 ");
//		sql.append(" and t.head_code = rd.code ");
//		sql.append(" and rd.type_id = 102 ");
		sql.append(" and t.mark_datetime >= ");
		sql.append(" to_date('"+startDate+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss') ");
		sql.append(" and t.mark_datetime <= ");
		sql.append(" to_date('"+endDate+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') ");
		if(headcode!=null&&!headcode.equalsIgnoreCase("")){
			sql.append(" and '"+headcode+"' like '%'||t.headname||'%' ");
		}
		sql.append(" order by t.headname ");
//		sql.append(" order by t.head_code desc ");

		
		try {
			GDSet set = null;
			set = DbComponent.Query(sql.toString());
			
			//遥控站
			HashMap<String,ArrayList<EffectStatCommonBean>> headnameMap = new HashMap<String,ArrayList<EffectStatCommonBean>>();
			ArrayList<String> AList=new ArrayList<String>();
			//语言
			HashMap<String,ArrayList<EffectStatCommonBean>> languageMap = new HashMap<String,ArrayList<EffectStatCommonBean>>();
			ArrayList<String> BList=new ArrayList<String>();
			//发射台
			HashMap<String,ArrayList<EffectStatCommonBean>> stationMap = new HashMap<String,ArrayList<EffectStatCommonBean>>();
			ArrayList<String> CList=new ArrayList<String>();
			
			String aKey = "";
			String bKey = "";
			String cKey = "";
			
			for(int i=0;i<set.getRowCount();i++){
				
				EffectStatCommonBean comm = new EffectStatCommonBean();
				String mark_datetime = set.getString(i, "mark_datetime");
				String _headname = set.getString(i, "headname");
				String _frequency = set.getString(i, "frequency");
				String _start_time = set.getString(i, "start_time");
				String _end_time = set.getString(i, "end_time");
				String _language_name = set.getString(i, "language_name");
				String _station_name = set.getString(i, "station_name");
				String _counto = set.getString(i, "counto");
				
				if (_headname.endsWith("A") || _headname.endsWith("B")|| _headname.endsWith("C")|| _headname.endsWith("D")
						|| _headname.endsWith("E")
						|| _headname.endsWith("F")|| _headname.endsWith("G"))
					_headname = _headname.substring(0, _headname.length()-1);
				comm.setMark_datetime(mark_datetime);
				comm.setHeadname(_headname);
				comm.setFreq(_frequency);
				comm.setStart_time(_start_time);
				comm.setEnd_time(_end_time);
				comm.setLanguage_name(_language_name);
				comm.setStation_name(_station_name);
				comm.setO(_counto);
				
				
				if (fields.indexOf(A)>-1){
					aKey =  _headname+A;
					if (!AList.contains(aKey)){
						AList.add(aKey);
						ArrayList<EffectStatCommonBean> list=new ArrayList<EffectStatCommonBean>(); 
						list.add(comm);
						headnameMap.put(aKey, list);
					}else{
						ArrayList<EffectStatCommonBean> _list = headnameMap.get(aKey);
						_list.add(comm);
						headnameMap.put(aKey, _list);
					}
				}
				if (fields.indexOf(B)>-1){
					bKey = _headname + _language_name +B  ;
					if (!BList.contains(bKey)){
						BList.add(bKey);
						ArrayList<EffectStatCommonBean> list=new ArrayList<EffectStatCommonBean>(); 
						list.add(comm);
						languageMap.put(bKey, list);
					}else{
						ArrayList<EffectStatCommonBean> _list = languageMap.get(bKey);
						_list.add(comm);
						languageMap.put(bKey, _list);
					}
				}
				if (fields.indexOf(C)>-1){
					cKey = _headname + _station_name +C ;
					if (!CList.contains(cKey)){
						CList.add(cKey);
						ArrayList<EffectStatCommonBean> list=new ArrayList<EffectStatCommonBean>(); 
						list.add(comm);
						stationMap.put(cKey, list);
					}else{
						ArrayList<EffectStatCommonBean> _list = stationMap.get(cKey);
						_list.add(comm);
						stationMap.put(cKey, _list);
					}
				}
				
			}
			
			//存放三类统计内容
			ArrayList<HashMap<String,ArrayList<EffectStatCommonBean>>> mapList = new ArrayList<HashMap<String,ArrayList<EffectStatCommonBean>>>();
			mapList.add(languageMap);
			mapList.add(stationMap);
			mapList.add(headnameMap);
			
			for(int ia=0;ia<mapList.size();ia++){
				ArrayList<String> keyList=new ArrayList<String>();
				if(ia==0){
					keyList=BList;
				}else if(ia==1){
					keyList=CList;
				}else{
					keyList=AList;
				}
				for(Object o : keyList){ 
					ArrayList<EffectStatCommonBean> list = mapList.get(ia).get(o);
					//在对比页面，不对统计内容进行计算了
					if (o.toString().indexOf(A)>-1 && statType.equals("对比")){
						continue;
					//在统计页面，不对对比内容进行计算了
					}else if ((o.toString().indexOf(B)> -1 || o.toString().indexOf(C)> -1) && statType.equals("统计"))
						continue;
					//频率总数
					int freqcount = 0;
					HashMap<String,String> freqMap = new HashMap<String,String>();
					
					//频时数
					float freqtime = 0;
					
					//可保证收听频时数
					float goodfreqtime = 0;
					//遥控站频率总数map
					HashMap ykzfeqmap = new HashMap();
					//遥控站频时map
					HashMap ykztimemap = new HashMap();
					//语言频率总数map
					HashMap languagefeqmap = new HashMap();
					//发射台频率总数map
					HashMap stationfeqmap = new HashMap();
					
					//记录不同分数(5,4,3,2,1)的总次数
					int count1 = 0;
					int count2 = 0;
					int count3 = 0;
					int count4 = 0;
					int count5 = 0;
					
					//总收测次数
					int receivecount = list.size();
					//不同分数的可听度满意程度
					//(可听度的满意程度中若“5”栏中填8，则表示有80—89%的时间可听度达到5分；“4”栏中填1，则表示有10—19%的时间可听度达到4分。)
					int audibility1 = 0;
					int audibility2 = 0;
					int audibility3 = 0;
					int audibility4 = 0;
					int audibility5 = 0;
	
					//可听率(可听度大于等于3分次数与总收听次数之比)
					int audible = 0;
					//合并后的语言, 逗号分隔
					String language = "";
					//合并后的发射台, 逗号分隔
					String stationName = "";
					for(int i=0;i<list.size();i++){
						EffectStatCommonBean result = list.get(i);
//						if(i>0){
//						System.out.println(list.get(i).getHeadname()+"----"+list.get(i-1).getHeadname());
//							if(!list.get(i).getHeadname().equalsIgnoreCase(list.get(i-1).getHeadname())){
//								freqcount=0;
//								freqtime=0;
//							}
//
//						}
					    if (o.toString().indexOf(A)>-1){
							aKey = o.toString();
						}
						
						if (o.toString().indexOf(B)>-1){
							bKey = o.toString();
						}

						if (o.toString().indexOf(C)>-1){
							cKey = o.toString();
						}
						
						//1.遥控站
						if (aKey.equals(o)){
							//合并语言
							if (language.indexOf(result.getLanguage_name()) == -1)
								language += result.getLanguage_name()+",";
							
							//合并发射台
							if (stationName.indexOf(result.getStation_name()) == -1)
								stationName += result.getStation_name()+",";
						}
						
						//2.遥控站语言
						if (bKey.equals(o)){
							//合并发射台
							if (stationName.indexOf(result.getStation_name()) == -1)
								stationName += result.getStation_name()+",";
						}
						
						//3.遥控站发射台
						if (cKey.equals(o)){
							//合并语言
							if (language.indexOf(result.getLanguage_name()) == -1)
								language += result.getLanguage_name()+",";
						}
						
						ykztimemap.put(result.getStart_time()+"-"+result.getEnd_time()+"-"+result.getMark_datetime().split(" ")[0]+"-"+result.getFreq()+"-"+result.getStation_name(),result.getStart_time()+"-"+result.getEnd_time()+"-"+result.getMark_datetime().split(" ")[0]+"-"+result.getFreq()+"-"+result.getStation_name() );
//						String stime = result.getStart_time();
//						String etime = result.getEnd_time();
						//5，4，3，2，1分值的次数
						int oo = Integer.valueOf(result.getO());
						switch(oo){
							case 1:count1++; break;
							case 2:count2++; break;
							case 3:count3++; break;
							case 4:count4++; break;
							case 5:count5++; break;
							default:;
						}
						
						//计算频率总数
						if (freqMap.get(result.getFreq())==null){
							ykzfeqmap.put(result.getFreq(), result.getFreq());						
							//freqcount++;
						}else							
							freqMap.put(result.getFreq(), "1");
						
						
						
//						//计算频时数	
//						String a = "2000-01-01 ";
//						String b = "2000-01-02 ";
//						int tmps = Integer.valueOf(stime.split(":")[0])*60+Integer.valueOf(stime.split(":")[1]);
//						int tmpe = Integer.valueOf(etime.split(":")[0])*60+Integer.valueOf(etime.split(":")[1]);
//						Date _etime = null;
//						Date _stime = null;
//						//开始时间大于结束时间,跨天了
//						if (tmps > tmpe){
//							_etime = StringTool.stringToDate(b+etime+":00");
//						}else{
//							_etime = StringTool.stringToDate(a+etime+":00");
//						}
//						
//						_stime = StringTool.stringToDate(a+stime+":00");
//						
//						long l = _etime.getTime() - _stime.getTime();
//						
//						freqtime += l/(1000*60*60);
					}
					//计算频时数	
					Iterator iter = ykztimemap.entrySet().iterator();
					while (iter.hasNext()) {
					   Map.Entry entry = (Map.Entry) iter.next();
					   String stime = entry.getKey().toString().split("-")[0];
					   String etime = entry.getKey().toString().split("-")[1];
					 //计算频时数	
						String a = "2000-01-01 ";
						String b = "2000-01-02 ";
						int tmps = Integer.valueOf(stime.split(":")[0])*60+Integer.valueOf(stime.split(":")[1]);
						int tmpe = Integer.valueOf(etime.split(":")[0])*60+Integer.valueOf(etime.split(":")[1]);
						Date _etime = null;
						Date _stime = null;
						//开始时间大于结束时间,跨天了
						if (tmps > tmpe){
							_etime = StringTool.stringToDate(b+etime+":00");
						}else{
							_etime = StringTool.stringToDate(a+etime+":00");
						}
						
						_stime = StringTool.stringToDate(a+stime+":00");
						
						long l = _etime.getTime() - _stime.getTime();
						
						freqtime += l/(1000*60*60);
					}
					language = language.endsWith(",")?language.substring(0,language.length()-1):language;
					stationName = stationName.endsWith(",")?stationName.substring(0,stationName.length()-1):stationName;
					
					if (receivecount >0){
						//1.计算可听度满意度
						audibility1 = (int)(((double)count1/receivecount)*10);
						audibility2 = (int)(((double)count2/receivecount)*10);
						audibility3 = (int)(((double)count3/receivecount)*10);
						audibility4 = (int)(((double)count4/receivecount)*10);
						audibility5 = (int)(((double)count5/receivecount)*10);
						
						//3.计算可听率
						audible = (Integer)(count3 + count4 + count5)*100/receivecount;
						
						EffectStatCommonBean resultBean = list.get(0);
						
						if (aKey.equals(o)){
							EffectStatCommonBean aBean = null;
							try {
								aBean = resultBean.clone();
							} catch (CloneNotSupportedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							aBean.setAudibility1(audibility1);
							aBean.setAudibility2(audibility2);
							aBean.setAudibility3(audibility3);
							aBean.setAudibility4(audibility4);
							aBean.setAudibility5(audibility5);
							aBean.setAudible(audible);
							aBean.setReceivecount(receivecount);
							
							//效果与可听率的关系：可听率>=80%，效果是可保证收听
							if (audible > 0.8)
								goodfreqtime = freqtime;
							aBean.setFreqtime(freqtime);
							//aBean.setFreqcount(freqcount);
							aBean.setFreqcount(ykzfeqmap.size());
							aBean.setGoodfreqtime(goodfreqtime);
							
							aBean.setLanguage_name(language);
							aBean.setStation_name(stationName);
							((ArrayList)resultObj.get(A)).add(aBean);

						}
						if (bKey.equals(o)){
							EffectStatCommonBean bBean = null;
							try {
								bBean = resultBean.clone();
							} catch (CloneNotSupportedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							bBean.setAudibility1(audibility1);
							bBean.setAudibility2(audibility2);
							bBean.setAudibility3(audibility3);
							bBean.setAudibility4(audibility4);
							bBean.setAudibility5(audibility5);
							bBean.setAudible(audible);
							bBean.setReceivecount(receivecount);
							
							//效果与可听率的关系：可听率>=80%，效果是可保证收听
							if (audible > 0.8)
								goodfreqtime = freqtime;
							bBean.setFreqtime(freqtime);
							bBean.setFreqcount(ykzfeqmap.size());
							bBean.setGoodfreqtime(goodfreqtime);
							bBean.setStation_name(stationName);
							((ArrayList)resultObj.get(B)).add(bBean);

						}
						if (cKey.equals(o)){
							EffectStatCommonBean cBean = null;
							try {
								cBean = resultBean.clone();
							} catch (CloneNotSupportedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							cBean.setAudibility1(audibility1);
							cBean.setAudibility2(audibility2);
							cBean.setAudibility3(audibility3);
							cBean.setAudibility4(audibility4);
							cBean.setAudibility5(audibility5);
							cBean.setAudible(audible);
							cBean.setReceivecount(receivecount);
							
							//效果与可听率的关系：可听率>=80%，效果是可保证收听
							if (audible > 0.8)
								goodfreqtime = freqtime;
							cBean.setFreqtime(freqtime);
							cBean.setFreqcount(ykzfeqmap.size());
							cBean.setGoodfreqtime(goodfreqtime);
							cBean.setLanguage_name(language);
							((ArrayList)resultObj.get(C)).add(cBean);

						}	
						
//						resultBean.setLanguage_name(language);
//						resultBean.setStation_name(stationName);
//						resultList.add(resultBean);
					}
				} 
			}
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GDSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        ArrayList list=(ArrayList) resultObj.get("a");
        if(list.size()>0){
        	for(int ii=0;ii<list.size();ii++){
        		EffectStatCommonBean aBean = (EffectStatCommonBean) list.get(ii);
        		xml+="<set label='"+aBean.getHeadname()+"' value='"+aBean.getAudible()+"'/>\r";
        	}
        	xml+="</chart>";
        }else{
        	xml="没有统计数据";
        }
        resultObj.put("xml", xml);
		return resultObj;
	}
	
	/**
	 * 6.1.6.7遥控站收测语言为基础的效果统计功能
	 * <p>class/function:com.viewscenes.web.statistics.ykz
	 * <p>explain:
	 * <p>author-date:谭长伟 2013-2-26
	 * @param:
	 * @return:
	 */
	public Object statEffectByYkzLanguage(ASObject obj){
		
		ASObject resultObj = new ASObject();
		resultObj.put(A, new ArrayList<EffectStatCommonBean>());
		resultObj.put(B, new ArrayList<EffectStatCommonBean>());
		resultObj.put(C, new ArrayList<EffectStatCommonBean>());
		String xml ="<chart yAxisName='可听率 单位:(%)' xAxisName='语言' caption='基于语言的效果统计可听率' baseFontSize='15' showBorder='1' imageSave='1'>";
//		ArrayList<EffectStatCommonBean> resultList = new ArrayList<EffectStatCommonBean>();
		//遥控站
		String headcode = (String)obj.get("headcode");	
		//语言 ，有多个
		String language_ids = (String)obj.get("language_ids");	
		
		String startDate = (String)obj.get("startDate");	
		String endDate = (String)obj.get("endDate");	
		//统计类型	对比，统计
		String statType = (String)obj.get("statType");
		//a:根据遥控站收测语言统计,b:根据语言服务区统计,c:根据语言遥控站统计
		String fields = (String)obj.get("fields");	
		
		StringBuffer sql = new StringBuffer();
		sql.append(" select distinct t.headname,t.frequency,run.start_time,run.end_time,run.language_name,t.station_name,t.counto,run.service_area  ");
		sql.append(" from radio_mark_zst_view_tab t, ");
		sql.append(" (select distinct t.*, lan.language_name ");
		sql.append(" from zres_runplan_chaifen_tab t,(select * from zdic_language_tab where is_delete = 0) lan ");
		sql.append(" where 1 = 1 and t.language_id = lan.language_id(+)) run, ");
		sql.append(" res_headend_tab rd ");
		sql.append(" where t.runplan_id = run.runplan_id(+) and t.counto is not null and t.counti is not null and t.counts is not null ");
		sql.append(" and run.runplan_type_id = 1 ");
		sql.append(" and t.head_code = rd.code ");
		sql.append(" and rd.type_id = 102 ");
		sql.append(" and t.mark_datetime >= ");
		sql.append(" to_date('"+startDate+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss') ");
		sql.append(" and t.mark_datetime <= ");
		sql.append(" to_date('"+endDate+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') ");
		if(headcode!=null&&!headcode.equalsIgnoreCase("")){
			sql.append(" and '"+headcode+"' like '%'||t.headname||'%' ");
		}
//		sql.append(" and rd.code like '"+headcode.toUpperCase()+"%' ");
		sql.append(" and run.language_id in ( "+language_ids +") ");
//		sql.append(" order by t.head_code desc ");

		
		try {
			GDSet set = null;
			set = DbComponent.Query(sql.toString());
			
			//语言
			HashMap<String,ArrayList<EffectStatCommonBean>> languageMap = new HashMap<String,ArrayList<EffectStatCommonBean>>();
			//服务区
			HashMap<String,ArrayList<EffectStatCommonBean>> serviceareaMap = new HashMap<String,ArrayList<EffectStatCommonBean>>();
			//遥控站
			HashMap<String,ArrayList<EffectStatCommonBean>> headnameMap = new HashMap<String,ArrayList<EffectStatCommonBean>>();
			
			
			String aKey = "";
			String bKey = "";
			String cKey = "";
			
			for(int i=0;i<set.getRowCount();i++){
				EffectStatCommonBean comm = new EffectStatCommonBean();
				String _headname = set.getString(i, "headname");
				String _frequency = set.getString(i, "frequency");
				String _start_time = set.getString(i, "start_time");
				String _end_time = set.getString(i, "end_time");
				String _language_name = set.getString(i, "language_name");
				String _station_name = set.getString(i, "station_name");
				String _counto = set.getString(i, "counto");
				String _service_area = set.getString(i, "service_area");
				
				if (_headname.endsWith("A") || _headname.endsWith("B")|| _headname.endsWith("C")|| _headname.endsWith("D")
						|| _headname.endsWith("E")
						|| _headname.endsWith("F")|| _headname.endsWith("G"))
					_headname = _headname.substring(0, _headname.length()-1);
				
				comm.setHeadname(_headname);
				comm.setFreq(_frequency);
				comm.setStart_time(_start_time);
				comm.setEnd_time(_end_time);
				comm.setLanguage_name(_language_name);
				comm.setStation_name(_station_name);
				comm.setO(_counto);
				comm.setService_area(_service_area);
				
			
//				String key = _language_name ;//+ _service_area +_station_name + _headname ;
//				if (languageMap.get(key)==null){
//					ArrayList<EffectStatCommonBean> list=new ArrayList<EffectStatCommonBean>(); 
//					list.add(comm);
//					languageMap.put(key, list);
//				}else{
//					ArrayList<EffectStatCommonBean> _list = languageMap.get(key);
//					_list.add(comm);
//					languageMap.put(key, _list);
//				}
				
				if (fields.indexOf(A)>-1){
					aKey =  _language_name +A;// + _service_area + _language_name + _headname ;
					if (languageMap.get(aKey)==null){
						ArrayList<EffectStatCommonBean> list=new ArrayList<EffectStatCommonBean>(); 
						list.add(comm);
						languageMap.put(aKey, list);
					}else{
						ArrayList<EffectStatCommonBean> _list = languageMap.get(aKey);
						_list.add(comm);
						languageMap.put(aKey, _list);
					}
				}
				if (fields.indexOf(B)>-1){
					bKey =  _language_name + _service_area + B;
					if (serviceareaMap.get(bKey)==null){
						ArrayList<EffectStatCommonBean> list=new ArrayList<EffectStatCommonBean>(); 
						list.add(comm);
						serviceareaMap.put(bKey, list);
					}else{
						ArrayList<EffectStatCommonBean> _list = serviceareaMap.get(bKey);
						_list.add(comm);
						serviceareaMap.put(bKey, _list);
					}
				}
				if (fields.indexOf(C)>-1){
					cKey =  _language_name + _headname + C;
					if (headnameMap.get(cKey)==null){
						ArrayList<EffectStatCommonBean> list=new ArrayList<EffectStatCommonBean>(); 
						list.add(comm);
						headnameMap.put(cKey, list);
					}else{
						ArrayList<EffectStatCommonBean> _list = headnameMap.get(cKey);
						_list.add(comm);
						headnameMap.put(cKey, _list);
					}
				}
			}
			
			//存放三类统计内容
			ArrayList<HashMap<String,ArrayList<EffectStatCommonBean>>> mapList = new ArrayList<HashMap<String,ArrayList<EffectStatCommonBean>>>();
			mapList.add(languageMap);
			mapList.add(serviceareaMap);
			mapList.add(headnameMap);
			
			for(int ia=0;ia<mapList.size();ia++){
			
				for(Object o : mapList.get(ia).keySet()){ 
					ArrayList<EffectStatCommonBean> list = mapList.get(ia).get(o);
					
					//在对比页面，不对统计内容进行计算了
					if (o.toString().indexOf(A)>-1 && statType.equals("对比")){
						continue;
					//在统计页面，不对对比内容进行计算了
					}else if ((o.toString().indexOf(B)> -1 || o.toString().indexOf(C)> -1) && statType.equals("统计"))
						continue;
					
					//频率总数
					int freqcount = 0;
					HashMap<String,String> freqMap = new HashMap<String,String>();
					
					//频时数
					float freqtime = 0;
					
					//可保证收听频时数
					float goodfreqtime = 0;
					
					//记录不同分数(5,4,3,2,1)的总次数
					int count1 = 0;
					int count2 = 0;
					int count3 = 0;
					int count4 = 0;
					int count5 = 0;
					
					//总收测次数
					int receivecount = list.size();
					//不同分数的可听度满意程度
					//(可听度的满意程度中若“5”栏中填8，则表示有80—89%的时间可听度达到5分；“4”栏中填1，则表示有10—19%的时间可听度达到4分。)
					int audibility1 = 0;
					int audibility2 = 0;
					int audibility3 = 0;
					int audibility4 = 0;
					int audibility5 = 0;
	
					//可听率(可听度大于等于3分次数与总收听次数之比)
					int audible = 0;
					
					//合并后的服务区, 逗号分隔
					String servicearea = "";
					//合并后的发射台, 逗号分隔
					String stationName = "";
					//合并后的站点, 逗号分隔
					String headname = "";
					for(int i=0;i<list.size();i++){
						EffectStatCommonBean result = list.get(i);
						
						if (o.toString().indexOf(A)>-1){
							aKey = o.toString();
						}
						
						if (o.toString().indexOf(B)>-1){
							bKey = o.toString();
						}

						if (o.toString().indexOf(C)>-1){
							cKey = o.toString();
						}
						
						//1.遥控站收测语言
						if (aKey.equals(o)){
							//合并服务区
							if (servicearea.indexOf(result.getService_area()) == -1)
								servicearea += result.getService_area()+",";
							
							//合并发射台
							if (stationName.indexOf(result.getStation_name()) == -1)
								stationName += result.getStation_name()+",";
							
							//合并站点
							if (headname.indexOf(result.getHeadname()) == -1)
								headname += result.getHeadname()+",";
						}
						
						//2.语言服务区统计
						if (bKey.equals(o)){
							//合并站点
							if (headname.indexOf(result.getHeadname()) == -1)
								headname += result.getHeadname()+",";
						}
						
						//3.语言遥控站统计
						if (cKey.equals(o)){
							//合并服务区
							if (servicearea.indexOf(result.getService_area()) == -1)
								servicearea += result.getService_area()+",";
						}
						
						
						
						
						
						
						
						String stime = result.getStart_time();
						String etime = result.getEnd_time();
						//5，4，3，2，1分值的次数
						int oo = Integer.valueOf(result.getO());
						switch(oo){
							case 1:count1++; break;
							case 2:count2++; break;
							case 3:count3++; break;
							case 4:count4++; break;
							case 5:count5++; break;
							default:;
						}
						
						//计算频率总数
						if (freqMap.get(result.getFreq())==null)
							freqcount++;
						else
							freqMap.put(result.getFreq(), "1");
						
						
						
						//计算频时数	
						String a = "2000-01-01 ";
						String b = "2000-01-02 ";
						int tmps = Integer.valueOf(stime.split(":")[0])*60+Integer.valueOf(stime.split(":")[1]);
						int tmpe = Integer.valueOf(etime.split(":")[0])*60+Integer.valueOf(etime.split(":")[1]);
						Date _etime = null;
						Date _stime = null;
						//开始时间大于结束时间,跨天了
						if (tmps > tmpe){
							_etime = StringTool.stringToDate(b+etime+":00");
						}else{
							_etime = StringTool.stringToDate(a+etime+":00");
						}
						
						_stime = StringTool.stringToDate(a+stime+":00");
						
						long l = _etime.getTime() - _stime.getTime();
						
						freqtime += l/(1000*60*60);
					}
					
					servicearea = servicearea.endsWith(",")?servicearea.substring(0,servicearea.length()-1):servicearea;
					stationName = stationName.endsWith(",")?stationName.substring(0,stationName.length()-1):stationName;
					headname = headname.endsWith(",")?headname.substring(0,headname.length()-1):headname; 
					
					if (receivecount >0){
						//1.计算可听度满意度
						audibility1 = (int)(((double)count1/receivecount)*10);
						audibility2 = (int)(((double)count2/receivecount)*10);
						audibility3 = (int)(((double)count3/receivecount)*10);
						audibility4 = (int)(((double)count4/receivecount)*10);
						audibility5 = (int)(((double)count5/receivecount)*10);
						
						//3.计算可听率
						audible = (Integer)(count3 + count4 + count5)*100/receivecount;
						
						EffectStatCommonBean resultBean = list.get(0);
						
						if (aKey.equals(o)){
							
							EffectStatCommonBean aBean = null;
							try {
								aBean = resultBean.clone();
							} catch (CloneNotSupportedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							aBean.setAudibility1(audibility1);
							aBean.setAudibility2(audibility2);
							aBean.setAudibility3(audibility3);
							aBean.setAudibility4(audibility4);
							aBean.setAudibility5(audibility5);
							aBean.setAudible(audible);
							aBean.setReceivecount(receivecount);
							
							//效果与可听率的关系：可听率>=80%，效果是可保证收听
							if (audible > 0.8)
								goodfreqtime = freqtime;
							aBean.setFreqtime(freqtime);
							aBean.setFreqcount(freqcount);
							aBean.setGoodfreqtime(goodfreqtime);
							
							aBean.setService_area(servicearea);
							aBean.setStation_name(stationName);
							aBean.setHeadname(headname);
							((ArrayList)resultObj.get(A)).add(aBean);

						}
						if (bKey.equals(o)){
							EffectStatCommonBean bBean = null;
							try {
								bBean = resultBean.clone();
							} catch (CloneNotSupportedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							bBean.setAudibility1(audibility1);
							bBean.setAudibility2(audibility2);
							bBean.setAudibility3(audibility3);
							bBean.setAudibility4(audibility4);
							bBean.setAudibility5(audibility5);
							bBean.setAudible(audible);
							bBean.setReceivecount(receivecount);
							
							//效果与可听率的关系：可听率>=80%，效果是可保证收听
							if (audible > 0.8)
								goodfreqtime = freqtime;
							bBean.setFreqtime(freqtime);
							bBean.setFreqcount(freqcount);
							bBean.setGoodfreqtime(goodfreqtime);
							
							bBean.setHeadname(headname);
							((ArrayList)resultObj.get(B)).add(bBean);

						}
						if (cKey.equals(o)){
							EffectStatCommonBean cBean = null;
							try {
								cBean = resultBean.clone();
							} catch (CloneNotSupportedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							cBean.setAudibility1(audibility1);
							cBean.setAudibility2(audibility2);
							cBean.setAudibility3(audibility3);
							cBean.setAudibility4(audibility4);
							cBean.setAudibility5(audibility5);
							cBean.setAudible(audible);
							cBean.setReceivecount(receivecount);
							
							//效果与可听率的关系：可听率>=80%，效果是可保证收听
							if (audible > 0.8)
								goodfreqtime = freqtime;
							cBean.setFreqtime(freqtime);
							cBean.setFreqcount(freqcount);
							cBean.setGoodfreqtime(goodfreqtime);
							cBean.setService_area(servicearea);
							((ArrayList)resultObj.get(C)).add(cBean);

						}
						
						
//						resultBean.setService_area(servicearea);
//						resultBean.setStation_name(stationName);
//						resultBean.setHeadname(headname);
//						resultList.add(resultBean);
					}
				} 
			}
			
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GDSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList list=(ArrayList) resultObj.get("a");
	    if(list.size()>0){
	      for(int ii=0;ii<list.size();ii++){
	        EffectStatCommonBean aBean = (EffectStatCommonBean) list.get(ii);
	        xml+="<set label='"+aBean.getLanguage_name()+"' value='"+aBean.getAudible()+"'/>\r";
	      }
	      xml+="</chart>";
	    }else{
	      xml="没有统计数据";
        }
	    resultObj.put("xml", xml);
		return resultObj;
	}
	
	
	/**
	 * 6.1.6.8遥控站收测发射台为基础的效果统计功能
	 * <p>class/function:com.viewscenes.web.statistics.ykz
	 * <p>explain:
	 * <p>author-date:谭长伟 2013-2-26
	 * @param:
	 * @return:statEffectByYkzLanguage
	 */
	public Object statEffectByYkzStation(ASObject obj){
		ASObject resultObj = new ASObject();
		resultObj.put(A, new ArrayList<EffectStatCommonBean>());
		resultObj.put(B, new ArrayList<EffectStatCommonBean>());
		resultObj.put(C, new ArrayList<EffectStatCommonBean>());
		String xml ="<chart yAxisName='可听率 单位:(%)' xAxisName='发射台' caption='基于发射台的效果统计可听率' baseFontSize='15' showBorder='1' imageSave='1'>";
		//遥控站
		String headcode = (String)obj.get("headcode");	
		//语言 ，有多个
		String station_ids = (String)obj.get("station_ids");	
		String startDate = (String)obj.get("startDate");	
		String endDate = (String)obj.get("endDate");	
		//统计类型	对比，统计
		String statType = (String)obj.get("statType");
		//a:根据遥控站发射台统计,b:根据发射台服务区统计,c:根据发射台遥控站统计
		String fields = (String)obj.get("fields");	
		
		StringBuffer sql = new StringBuffer();
		sql.append(" select distinct t.headname,t.frequency,run.start_time,run.end_time,run.language_name,t.station_name,t.counto,run.service_area  ");
		sql.append(" from radio_mark_zst_view_tab t, ");
		sql.append(" (select distinct t.*, lan.language_name ");
		sql.append(" from zres_runplan_chaifen_tab t,(select * from zdic_language_tab where is_delete = 0) lan ");
		sql.append(" where 1 = 1 and t.language_id = lan.language_id(+)) run, ");
		sql.append(" res_headend_tab rd ");
		sql.append(" where t.runplan_id = run.runplan_id(+) and t.counto is not null and t.counti is not null and t.counts is not null ");
		sql.append(" and run.runplan_type_id = 1 ");
		sql.append(" and t.head_code = rd.code ");
		sql.append(" and rd.type_id = 102 ");
		sql.append(" and t.mark_datetime >= ");
		sql.append(" to_date('"+startDate+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss') ");
		sql.append(" and t.mark_datetime <= ");
		sql.append(" to_date('"+endDate+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') ");
//		sql.append(" and rd.code like '"+headcode.toUpperCase()+"%' ");
		if(headcode!=null&&!headcode.equalsIgnoreCase("")){
			sql.append(" and '"+headcode+"' like '%'||t.headname||'%' ");
		}
		if(station_ids!=null&&!station_ids.equalsIgnoreCase("")){
			sql.append(" and t.station_id in ( "+station_ids +") ");
		}
		
//		sql.append(" order by t.head_code desc ");

		
		try {
			GDSet set = null;
			set = DbComponent.Query(sql.toString());
			//发射台
			HashMap<String,ArrayList<EffectStatCommonBean>> stationMap = new HashMap<String,ArrayList<EffectStatCommonBean>>();
			//服务区
			HashMap<String,ArrayList<EffectStatCommonBean>> serviceareaMap = new HashMap<String,ArrayList<EffectStatCommonBean>>();
			//遥控站
			HashMap<String,ArrayList<EffectStatCommonBean>> headnameMap = new HashMap<String,ArrayList<EffectStatCommonBean>>();
			
			String aKey = "";
			String bKey = "";
			String cKey = "";
			
			for(int i=0;i<set.getRowCount();i++){
				EffectStatCommonBean comm = new EffectStatCommonBean();
				String _headname = set.getString(i, "headname");
				String _frequency = set.getString(i, "frequency");
				String _start_time = set.getString(i, "start_time");
				String _end_time = set.getString(i, "end_time");
				String _language_name = set.getString(i, "language_name");
				String _station_name = set.getString(i, "station_name");
				String _counto = set.getString(i, "counto");
				String _service_area = set.getString(i, "service_area");
				
				
				if (_headname.endsWith("A") || _headname.endsWith("B")|| _headname.endsWith("C")|| _headname.endsWith("D")
						|| _headname.endsWith("E")
						|| _headname.endsWith("F")|| _headname.endsWith("G"))
					_headname = _headname.substring(0, _headname.length()-1);
				
				comm.setHeadname(_headname);
				comm.setFreq(_frequency);
				comm.setStart_time(_start_time);
				comm.setEnd_time(_end_time);
				comm.setLanguage_name(_language_name);
				comm.setStation_name(_station_name);
				comm.setO(_counto);
				comm.setService_area(_service_area);
				
				if (fields.indexOf(A)>-1){
					aKey =  _station_name +A ;// + _service_area + _language_name + _headname ;
					if (stationMap.get(aKey)==null){
						ArrayList<EffectStatCommonBean> list=new ArrayList<EffectStatCommonBean>(); 
						list.add(comm);
						stationMap.put(aKey, list);
					}else{
						ArrayList<EffectStatCommonBean> _list = stationMap.get(aKey);
						_list.add(comm);
						stationMap.put(aKey, _list);
					}
				}
				if (fields.indexOf(B)>-1){
					bKey =  _station_name + _service_area +B;
					if (serviceareaMap.get(bKey)==null){
						ArrayList<EffectStatCommonBean> list=new ArrayList<EffectStatCommonBean>(); 
						list.add(comm);
						serviceareaMap.put(bKey, list);
					}else{
						ArrayList<EffectStatCommonBean> _list = serviceareaMap.get(bKey);
						_list.add(comm);
						serviceareaMap.put(bKey, _list);
					}
				}
				if (fields.indexOf(C)>-1){
					cKey =  _station_name + _headname +C;
					if (headnameMap.get(cKey)==null){
						ArrayList<EffectStatCommonBean> list=new ArrayList<EffectStatCommonBean>(); 
						list.add(comm);
						headnameMap.put(cKey, list);
					}else{
						ArrayList<EffectStatCommonBean> _list = headnameMap.get(cKey);
						_list.add(comm);
						headnameMap.put(cKey, _list);
					}
				}
			}
			//存放三类统计内容
			ArrayList<HashMap<String,ArrayList<EffectStatCommonBean>>> mapList = new ArrayList<HashMap<String,ArrayList<EffectStatCommonBean>>>();
			mapList.add(stationMap);
			mapList.add(serviceareaMap);
			mapList.add(headnameMap);
			
			
			for(int ia=0;ia<mapList.size();ia++){
				
				//分别统计不同类型结果
				for(Object o : mapList.get(ia).keySet()){ 
					ArrayList<EffectStatCommonBean> list = mapList.get(ia).get(o);
					
					//在对比页面，不对统计内容进行计算了
					if (o.toString().indexOf(A)>-1 && statType.equals("对比")){
						continue;
					//在统计页面，不对对比内容进行计算了
					}else if ((o.toString().indexOf(B)> -1 || o.toString().indexOf(C)> -1) && statType.equals("统计"))
						continue;
					
					
					//频率总数
					int freqcount = 0;
					HashMap<String,String> freqMap = new HashMap<String,String>();
					
					//频时数
					float freqtime = 0;
					
					//可保证收听频时数
					float goodfreqtime = 0;
					
					//记录不同分数(5,4,3,2,1)的总次数
					int count1 = 0;
					int count2 = 0;
					int count3 = 0;
					int count4 = 0;
					int count5 = 0;
					
					//总收测次数
					int receivecount = list.size();
					//不同分数的可听度满意程度
					//(可听度的满意程度中若“5”栏中填8，则表示有80—89%的时间可听度达到5分；“4”栏中填1，则表示有10—19%的时间可听度达到4分。)
					int audibility1 = 0;
					int audibility2 = 0;
					int audibility3 = 0;
					int audibility4 = 0;
					int audibility5 = 0;
	
					//可听率(可听度大于等于3分次数与总收听次数之比)
					int audible = 0;
					
					//合并后的服务区, 逗号分隔
					String servicearea = "";
					//合并后的遥控站, 逗号分隔
					String headname = "";
					//合并后的发射台, 逗号分隔
//					String station = "";
					for(int j=0;j<list.size();j++){
						EffectStatCommonBean result = list.get(j);
						
						if (o.toString().indexOf(A)>-1){
							aKey = o.toString();
						}
						
						if (o.toString().indexOf(B)>-1){
							bKey = o.toString();
						}

						if (o.toString().indexOf(C)>-1){
							cKey = o.toString();
						}
						
						
						//1.遥控站发射台统计
						if (aKey.equals(o)){
							//合并服务区
							if (servicearea.indexOf(result.getService_area()) == -1)
								servicearea += result.getService_area()+",";
							
							//合并站点
							if (headname.indexOf(result.getHeadname()) == -1)
								headname += result.getHeadname()+",";
						}
						
						//2.根据发射台服务区统计
						if (bKey.equals(o)){
							//合并站点
							if (headname.indexOf(result.getHeadname()) == -1)
								headname += result.getHeadname()+",";
						}
						
						//3.根据发射台遥控站统计
						if (cKey.equals(o)){
							//合并服务区
							if (servicearea.indexOf(result.getService_area()) == -1)
								servicearea += result.getService_area()+",";
						}
						
//						//合并发射台
//						if (station.indexOf(result.getStation_name()) == -1)
//							station += result.getStation_name()+",";
						
						
						
						String stime = result.getStart_time();
						String etime = result.getEnd_time();
						//5，4，3，2，1分值的次数
						int oo = Integer.valueOf(result.getO());
						switch(oo){
							case 1:count1++; break;
							case 2:count2++; break;
							case 3:count3++; break;
							case 4:count4++; break;
							case 5:count5++; break;
							default:;
						}
						
						//计算频率总数
						if (freqMap.get(result.getFreq())==null)
							freqcount++;
						else
							freqMap.put(result.getFreq(), "1");
						
						
						
						//计算频时数	
						String a = "2000-01-01 ";
						String b = "2000-01-02 ";
						int tmps = Integer.valueOf(stime.split(":")[0])*60+Integer.valueOf(stime.split(":")[1]);
						int tmpe = Integer.valueOf(etime.split(":")[0])*60+Integer.valueOf(etime.split(":")[1]);
						Date _etime = null;
						Date _stime = null;
						//开始时间大于结束时间,跨天了
						if (tmps > tmpe){
							_etime = StringTool.stringToDate(b+etime+":00");
						}else{
							_etime = StringTool.stringToDate(a+etime+":00");
						}
						
						_stime = StringTool.stringToDate(a+stime+":00");
						
						long l = _etime.getTime() - _stime.getTime();
						
						freqtime += l/(1000*60*60);
					}
					
					servicearea = servicearea.endsWith(",")?servicearea.substring(0,servicearea.length()-1):servicearea;
//					station = station.endsWith(",")?station.substring(0,station.length()-1):station;
					headname = headname.endsWith(",")?headname.substring(0,headname.length()-1):headname; 
					
					if (receivecount >0){
						//1.计算可听度满意度
						audibility1 = (int)(((double)count1/receivecount)*10);
						audibility2 = (int)(((double)count2/receivecount)*10);
						audibility3 = (int)(((double)count3/receivecount)*10);
						audibility4 = (int)(((double)count4/receivecount)*10);
						audibility5 = (int)(((double)count5/receivecount)*10);
						
						//3.计算可听率
						audible = (Integer)(count4 + count5)*100/receivecount;
						
						EffectStatCommonBean resultBean = list.get(0);
						
						
						if (aKey.equals(o)){
							EffectStatCommonBean aBean = null;
							try {
								aBean = resultBean.clone();
							} catch (CloneNotSupportedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							aBean.setAudibility1(audibility1);
							aBean.setAudibility2(audibility2);
							aBean.setAudibility3(audibility3);
							aBean.setAudibility4(audibility4);
							aBean.setAudibility5(audibility5);
							aBean.setAudible(audible);
							aBean.setReceivecount(receivecount);
							
							//效果与可听率的关系：可听率>=80%，效果是可保证收听
							if (audible > 0.8)
								goodfreqtime = freqtime;
							aBean.setFreqtime(freqtime);
							aBean.setFreqcount(freqcount);
							aBean.setGoodfreqtime(goodfreqtime);
							
							aBean.setService_area(servicearea);
							aBean.setHeadname(headname);
							((ArrayList)resultObj.get(A)).add(aBean);

						}
						if (bKey.equals(o)){
							EffectStatCommonBean bBean = null;
							try {
								bBean = resultBean.clone();
							} catch (CloneNotSupportedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							bBean.setAudibility1(audibility1);
							bBean.setAudibility2(audibility2);
							bBean.setAudibility3(audibility3);
							bBean.setAudibility4(audibility4);
							bBean.setAudibility5(audibility5);
							bBean.setAudible(audible);
							bBean.setReceivecount(receivecount);
							
							//效果与可听率的关系：可听率>=80%，效果是可保证收听
							if (audible > 0.8)
								goodfreqtime = freqtime;
							bBean.setFreqtime(freqtime);
							bBean.setFreqcount(freqcount);
							bBean.setGoodfreqtime(goodfreqtime);
							
							bBean.setHeadname(headname);
							((ArrayList)resultObj.get(B)).add(bBean);

						}
						if (cKey.equals(o)){
							EffectStatCommonBean cBean = null;
							try {
								cBean = resultBean.clone();
							} catch (CloneNotSupportedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							cBean.setAudibility1(audibility1);
							cBean.setAudibility2(audibility2);
							cBean.setAudibility3(audibility3);
							cBean.setAudibility4(audibility4);
							cBean.setAudibility5(audibility5);
							cBean.setAudible(audible);
							cBean.setReceivecount(receivecount);
							
							//效果与可听率的关系：可听率>=80%，效果是可保证收听
							if (audible > 0.8)
								goodfreqtime = freqtime;
							cBean.setFreqtime(freqtime);
							cBean.setFreqcount(freqcount);
							cBean.setGoodfreqtime(goodfreqtime);
							
							cBean.setService_area(servicearea);
							((ArrayList)resultObj.get(C)).add(cBean);

						}
					}
					
				}
			}
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GDSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList list=(ArrayList) resultObj.get("a");
	    if(list.size()>0){
	      for(int ii=0;ii<list.size();ii++){
	        EffectStatCommonBean aBean = (EffectStatCommonBean) list.get(ii);
	        xml+="<set label='"+aBean.getStation_name()+"' value='"+aBean.getAudible()+"'/>\r";
	      }
	      xml+="</chart>";
	    }else{
	      xml="没有统计数据";
        }
	    resultObj.put("xml", xml);
		return resultObj;
	}
	
	
	public void statEffectByYkzToExcel(String msg,HttpServletRequest request,HttpServletResponse response){
		
		Element root = StringTool.getXMLRoot(msg);
		String headcode = root.getChildText("headcode");
		String startDate = root.getChildText("startDate");
		String endDate = root.getChildText("endDate");
		String statType = root.getChildText("statType");
		String fields = root.getChildText("fields");
		
			
		ASObject msgObj = new ASObject();
		msgObj.put("headcode",headcode);
		msgObj.put("startDate", startDate);
		msgObj.put("endDate", endDate);
		msgObj.put("statType", statType);
		msgObj.put("fields", fields);
		
		ASObject result = (ASObject)statEffectByYkz(msgObj);
		
		ArrayList<EffectStatCommonBean> aList = (ArrayList<EffectStatCommonBean>)result.get(A);
		ArrayList<EffectStatCommonBean> bList = (ArrayList<EffectStatCommonBean>)result.get(B);
		ArrayList<EffectStatCommonBean> cList = (ArrayList<EffectStatCommonBean>)result.get(C);
		
		
		
		
		ExcelTitle id = new ExcelTitle("序号",10);
		ExcelTitle stationtitle = new ExcelTitle("遥控站",10);
		ExcelTitle freqcounttitle = new ExcelTitle("频率数",10);
		ExcelTitle freqtimetitle = new ExcelTitle("频时数",10);
		ExcelTitle languagetitle = new ExcelTitle("语言",10);
		ExcelTitle stationTitile = new ExcelTitle("发射台",10);
		ExcelTitle receivecounttitle = new ExcelTitle("收测次数",10);
		ExcelTitle goodfreqcounttitle = new ExcelTitle("可保证收听频时数",20);
		ExcelTitle ketinglvtitle = new ExcelTitle("可听率%",10);
		ExcelTitle manyidutitle = new ExcelTitle("可听度满意程度",30);
		ExcelTitle title1 = new ExcelTitle("1",6);
		ExcelTitle title2 = new ExcelTitle("2",6);
		ExcelTitle title3 = new ExcelTitle("3",6);
		ExcelTitle title4 = new ExcelTitle("4",6);
		ExcelTitle title5 = new ExcelTitle("5",6);
		manyidutitle.setSubTitles(new ExcelTitle[]{title1,title2,title3,title4,title5});
		
		
		
		try {
			ExcelTitle[] ets_ykz = new ExcelTitle[]{id,stationtitle,freqcounttitle,freqtimetitle,languagetitle,stationTitile,receivecounttitle,goodfreqcounttitle,ketinglvtitle,manyidutitle};
			ExcelTitle[] ets_ykz_lang = new ExcelTitle[]{id,languagetitle,freqcounttitle,freqtimetitle,stationTitile,receivecounttitle,goodfreqcounttitle,ketinglvtitle,manyidutitle};
			ExcelTitle[] ets_ykz_station = new ExcelTitle[]{id,stationTitile,freqcounttitle,freqtimetitle,languagetitle,receivecounttitle,goodfreqcounttitle,ketinglvtitle,manyidutitle};
			
			//表头
			ArrayList<ExcelTitle[]> titles = new ArrayList<ExcelTitle[]>(3);
			titles.add(ets_ykz);
			titles.add(ets_ykz_lang);
			titles.add(ets_ykz_station);
			
			//工作溥名、表头
			String[] sheetNames = new String[]{"根据遥控站统计","根据遥控站语言统计","根据遥控站发射台统计"};
			ArrayList[] resultList = new ArrayList[]{aList,bList,cList};;
			
			
			JExcel jExcel = new JExcel();
			jExcel.openDocument();
			//文件名称
			jExcel.setFilename(sheetNames[0]);
			for(int sheet = 0;sheet<resultList.length;sheet++){
				
				//改变工作溥 
				jExcel.WorkBookGetSheet(sheet);
				//工作溥名称
				jExcel.getWorkSheet().setName(sheetNames[sheet]);
				
				//列名称
				jExcel.setTitle(0, 2, titles.get(sheet));
				
				//表头
				jExcel.mergeCells(0, 0, sheet==0?13:12, 1);
				//内容
				jExcel.addDate(0,0, sheetNames[sheet]+"("+startDate+"~"+endDate+")",jExcel.titleCellFormat);
	            
				for(int i=0;i<resultList.length;i++){
					
					for(int k=0;k<resultList[i].size();k++){
						EffectStatCommonBean aBean = (EffectStatCommonBean)resultList[i].get(k);
						switch(sheet){
							case 0:
								jExcel.addDate(0, k+4, k+1);
								jExcel.addDate(1, k+4, aBean.getHeadname(),jExcel.dateCellFormat);
								jExcel.addDate(2, k+4, aBean.getFreqcount());
								jExcel.addDate(3, k+4, aBean.getFreqtime());
								jExcel.addDate(4, k+4, aBean.getLanguage_name(),jExcel.dateCellFormat);
								jExcel.addDate(5, k+4, aBean.getStation_name(),jExcel.dateCellFormat);
								jExcel.addDate(6, k+4, aBean.getReceivecount());
								jExcel.addDate(7, k+4, aBean.getGoodfreqtime());
								jExcel.addDate(8, k+4, aBean.getAudible());
								jExcel.addDate(9, k+4, aBean.getAudibility1());
								jExcel.addDate(10, k+4, aBean.getAudibility2());
								jExcel.addDate(11, k+4, aBean.getAudibility3());
								jExcel.addDate(12, k+4, aBean.getAudibility4());
								jExcel.addDate(13, k+4, aBean.getAudibility5());
								break;
							case 1:
								jExcel.addDate(0, k+4, k+1);
								jExcel.addDate(1, k+4, aBean.getLanguage_name(),jExcel.dateCellFormat);
								jExcel.addDate(2, k+4, aBean.getFreqcount());
								jExcel.addDate(3, k+4, aBean.getFreqtime());
								jExcel.addDate(4, k+4, aBean.getStation_name(),jExcel.dateCellFormat);
								jExcel.addDate(5, k+4, aBean.getReceivecount());
								jExcel.addDate(6, k+4, aBean.getGoodfreqtime());
								jExcel.addDate(7, k+4, aBean.getAudible());
								jExcel.addDate(8, k+4, aBean.getAudibility1());
								jExcel.addDate(9, k+4, aBean.getAudibility2());
								jExcel.addDate(10, k+4, aBean.getAudibility3());
								jExcel.addDate(11, k+4, aBean.getAudibility4());
								jExcel.addDate(12, k+4, aBean.getAudibility5());
								break;
							case 2:
								jExcel.addDate(0, k+4, k+1);
								jExcel.addDate(1, k+4, aBean.getStation_name(),jExcel.dateCellFormat);
								jExcel.addDate(2, k+4, aBean.getFreqcount());
								jExcel.addDate(3, k+4, aBean.getFreqtime());
								jExcel.addDate(4, k+4, aBean.getLanguage_name(),jExcel.dateCellFormat);
								jExcel.addDate(5, k+4, aBean.getReceivecount());
								jExcel.addDate(6, k+4, aBean.getGoodfreqtime());
								jExcel.addDate(7, k+4, aBean.getAudible());
								jExcel.addDate(8, k+4, aBean.getAudibility1());
								jExcel.addDate(9, k+4, aBean.getAudibility2());
								jExcel.addDate(10, k+4, aBean.getAudibility3());
								jExcel.addDate(11, k+4, aBean.getAudibility4());
								jExcel.addDate(12, k+4, aBean.getAudibility5());
								break;
						}
					}
				}
				
			}
			
			jExcel.saveDocument();
			jExcel.postExcel(request, response);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public void statEffectByYkzLanguageToExcel(String msg,HttpServletRequest request,HttpServletResponse response){

		Element root = StringTool.getXMLRoot(msg);
		String language_ids = root.getChildText("language_ids");
		String startDate = root.getChildText("startDate");
		String endDate = root.getChildText("endDate");
		String statType = root.getChildText("statType");
		String fields = root.getChildText("fields");
		String headcode = root.getChildText("headcode");	
			
		ASObject msgObj = new ASObject();
		msgObj.put("language_ids",language_ids);
		msgObj.put("startDate", startDate);
		msgObj.put("endDate", endDate);
		msgObj.put("statType", statType);
		msgObj.put("fields", fields);
		msgObj.put("headcode", headcode);
		ASObject result = (ASObject)statEffectByYkzLanguage(msgObj);
		
		ArrayList<EffectStatCommonBean> aList = (ArrayList<EffectStatCommonBean>)result.get(A);
		ArrayList<EffectStatCommonBean> bList = (ArrayList<EffectStatCommonBean>)result.get(B);
		ArrayList<EffectStatCommonBean> cList = (ArrayList<EffectStatCommonBean>)result.get(C);
		
		
		
		
		ExcelTitle id = new ExcelTitle("序号",10);
		ExcelTitle languagetitle = new ExcelTitle("语言",10);
		ExcelTitle freqcounttitle = new ExcelTitle("频率数",10);
		ExcelTitle freqtimetitle = new ExcelTitle("频时数",10);
		ExcelTitle servicetitle = new ExcelTitle("服务区",10);
		ExcelTitle stationtitile = new ExcelTitle("发射台",10);
		ExcelTitle headendtitile = new ExcelTitle("遥控站",10);
		ExcelTitle receivecounttitle = new ExcelTitle("收测次数",10);
		ExcelTitle goodfreqcounttitle = new ExcelTitle("可保证收听频时数",20);
		ExcelTitle ketinglvtitle = new ExcelTitle("可听率%",10);
		ExcelTitle manyidutitle = new ExcelTitle("可听度满意程度",30);
		ExcelTitle title1 = new ExcelTitle("1",6);
		ExcelTitle title2 = new ExcelTitle("2",6);
		ExcelTitle title3 = new ExcelTitle("3",6);
		ExcelTitle title4 = new ExcelTitle("4",6);
		ExcelTitle title5 = new ExcelTitle("5",6);
		manyidutitle.setSubTitles(new ExcelTitle[]{title1,title2,title3,title4,title5});
		
		
		
		try {
			ExcelTitle[] ets_lang = new ExcelTitle[]{id,languagetitle,freqcounttitle,freqtimetitle,servicetitle,stationtitile,headendtitile,receivecounttitle,goodfreqcounttitle,ketinglvtitle,manyidutitle};
			ExcelTitle[] ets_lang_service = new ExcelTitle[]{id,servicetitle,freqcounttitle,freqtimetitle,headendtitile,receivecounttitle,goodfreqcounttitle,ketinglvtitle,manyidutitle};
			ExcelTitle[] ets_lang_headend = new ExcelTitle[]{id,headendtitile,freqcounttitle,freqtimetitle,servicetitle,receivecounttitle,goodfreqcounttitle,ketinglvtitle,manyidutitle};
			
			//表头
			ArrayList<ExcelTitle[]> titles = new ArrayList<ExcelTitle[]>(3);
			titles.add(ets_lang);
			titles.add(ets_lang_service);
			titles.add(ets_lang_headend);
			
			//工作溥名、表头
			String[] sheetNames = new String[]{"根据语言统计","根据语言服务区统计","根据语言遥控站统计"};
			ArrayList[] resultList = new ArrayList[]{aList,bList,cList};;
			
			
			JExcel jExcel = new JExcel();
			jExcel.openDocument();
			//文件名称
			jExcel.setFilename(sheetNames[0]);
			for(int sheet = 0;sheet<resultList.length;sheet++){
				
				//改变工作溥 
				jExcel.WorkBookGetSheet(sheet);
				//工作溥名称
				jExcel.getWorkSheet().setName(sheetNames[sheet]);
				
				//列名称
				jExcel.setTitle(0, 2, titles.get(sheet));
				
				//表头
				jExcel.mergeCells(0, 0, sheet==0?14:12, 1);
				//内容
				jExcel.addDate(0,0, sheetNames[sheet]+"("+startDate+"~"+endDate+")",jExcel.titleCellFormat);
	            
				for(int i=0;i<resultList.length;i++){
					
					for(int k=0;k<resultList[i].size();k++){
						EffectStatCommonBean aBean = (EffectStatCommonBean)resultList[i].get(k);
						switch(sheet){
							case 0:
								jExcel.addDate(0, k+4, k+1);
								jExcel.addDate(1, k+4, aBean.getLanguage_name(),jExcel.dateCellFormat);
								jExcel.addDate(2, k+4, aBean.getFreqcount());
								jExcel.addDate(3, k+4, aBean.getFreqtime());
								jExcel.addDate(4, k+4, aBean.getService_area(),jExcel.dateCellFormat);
								jExcel.addDate(5, k+4, aBean.getStation_name(),jExcel.dateCellFormat);
								jExcel.addDate(6, k+4, aBean.getHeadname(),jExcel.dateCellFormat);
								jExcel.addDate(7, k+4, aBean.getReceivecount());
								jExcel.addDate(8, k+4, aBean.getGoodfreqtime());
								jExcel.addDate(9, k+4, aBean.getAudible());
								jExcel.addDate(10, k+4, aBean.getAudibility1());
								jExcel.addDate(11, k+4, aBean.getAudibility2());
								jExcel.addDate(12, k+4, aBean.getAudibility3());
								jExcel.addDate(13, k+4, aBean.getAudibility4());
								jExcel.addDate(14, k+4, aBean.getAudibility5());
								break;
							case 1:
								jExcel.addDate(0, k+4, k+1);
								jExcel.addDate(1, k+4, aBean.getService_area(),jExcel.dateCellFormat);
								jExcel.addDate(2, k+4, aBean.getFreqcount());
								jExcel.addDate(3, k+4, aBean.getFreqtime());
								jExcel.addDate(4, k+4, aBean.getHeadname(),jExcel.dateCellFormat);
								jExcel.addDate(5, k+4, aBean.getReceivecount());
								jExcel.addDate(6, k+4, aBean.getGoodfreqtime());
								jExcel.addDate(7, k+4, aBean.getAudible());
								jExcel.addDate(8, k+4, aBean.getAudibility1());
								jExcel.addDate(9, k+4, aBean.getAudibility2());
								jExcel.addDate(10, k+4, aBean.getAudibility3());
								jExcel.addDate(11, k+4, aBean.getAudibility4());
								jExcel.addDate(12, k+4, aBean.getAudibility5());
								break;
							case 2:
								jExcel.addDate(0, k+4, k+1);
								jExcel.addDate(1, k+4, aBean.getHeadname(),jExcel.dateCellFormat);
								jExcel.addDate(2, k+4, aBean.getFreqcount());
								jExcel.addDate(3, k+4, aBean.getFreqtime());
								jExcel.addDate(4, k+4, aBean.getService_area(),jExcel.dateCellFormat);
								jExcel.addDate(5, k+4, aBean.getReceivecount());
								jExcel.addDate(6, k+4, aBean.getGoodfreqtime());
								jExcel.addDate(7, k+4, aBean.getAudible());
								jExcel.addDate(8, k+4, aBean.getAudibility1());
								jExcel.addDate(9, k+4, aBean.getAudibility2());
								jExcel.addDate(10, k+4, aBean.getAudibility3());
								jExcel.addDate(11, k+4, aBean.getAudibility4());
								jExcel.addDate(12, k+4, aBean.getAudibility5());
								break;
						}
					}
				}
				
			}
			
			jExcel.saveDocument();
			jExcel.postExcel(request, response);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	
	
	public void statEffectByYkzStationToExcel(String msg,HttpServletRequest request,HttpServletResponse response){
		Element root = StringTool.getXMLRoot(msg);
		String station_ids = root.getChildText("station_ids");
		String startDate = root.getChildText("startDate");
		String endDate = root.getChildText("endDate");
		String statType = root.getChildText("statType");
		String fields = root.getChildText("fields");
		String headcode = root.getChildText("headcode");
			
		ASObject msgObj = new ASObject();
		msgObj.put("station_ids",station_ids);
		msgObj.put("startDate", startDate);
		msgObj.put("endDate", endDate);
		msgObj.put("statType", statType);
		msgObj.put("fields", fields);
		msgObj.put("headcode", headcode);
		ASObject result = (ASObject)statEffectByYkzStation(msgObj);
		
		ArrayList<EffectStatCommonBean> aList = (ArrayList<EffectStatCommonBean>)result.get(A);
		ArrayList<EffectStatCommonBean> bList = (ArrayList<EffectStatCommonBean>)result.get(B);
		ArrayList<EffectStatCommonBean> cList = (ArrayList<EffectStatCommonBean>)result.get(C);
		
		
		
		
		ExcelTitle id = new ExcelTitle("序号",10);
		ExcelTitle languagetitle = new ExcelTitle("语言",10);
		ExcelTitle freqcounttitle = new ExcelTitle("频率数",10);
		ExcelTitle freqtimetitle = new ExcelTitle("频时数",10);
		ExcelTitle servicetitle = new ExcelTitle("服务区",10);
		ExcelTitle stationtitile = new ExcelTitle("发射台",10);
		ExcelTitle headendtitile = new ExcelTitle("遥控站",10);
		ExcelTitle receivecounttitle = new ExcelTitle("收测次数",10);
		ExcelTitle goodfreqcounttitle = new ExcelTitle("可保证收听频时数",20);
		ExcelTitle ketinglvtitle = new ExcelTitle("可听率%",10);
		ExcelTitle manyidutitle = new ExcelTitle("可听度满意程度",30);
		ExcelTitle title1 = new ExcelTitle("1",6);
		ExcelTitle title2 = new ExcelTitle("2",6);
		ExcelTitle title3 = new ExcelTitle("3",6);
		ExcelTitle title4 = new ExcelTitle("4",6);
		ExcelTitle title5 = new ExcelTitle("5",6);
		manyidutitle.setSubTitles(new ExcelTitle[]{title1,title2,title3,title4,title5});
		
		
		
		try {
			ExcelTitle[] ets_station = new ExcelTitle[]{id,stationtitile,freqcounttitle,freqtimetitle,servicetitle,languagetitle,headendtitile,receivecounttitle,goodfreqcounttitle,ketinglvtitle,manyidutitle};
			ExcelTitle[] ets_station_service = new ExcelTitle[]{id,servicetitle,freqcounttitle,freqtimetitle,headendtitile,receivecounttitle,goodfreqcounttitle,ketinglvtitle,manyidutitle};
			ExcelTitle[] ets_station_headend = new ExcelTitle[]{id,headendtitile,freqcounttitle,freqtimetitle,servicetitle,receivecounttitle,goodfreqcounttitle,ketinglvtitle,manyidutitle};
			
			//表头
			ArrayList<ExcelTitle[]> titles = new ArrayList<ExcelTitle[]>(3);
			titles.add(ets_station);
			titles.add(ets_station_service);
			titles.add(ets_station_headend);
			
			//工作溥名、表头
			String[] sheetNames = new String[]{"根据发射台统计","根据发射台服务区统计","根据发射台遥控站统计"};
			ArrayList[] resultList = new ArrayList[]{aList,bList,cList};;
			
			
			JExcel jExcel = new JExcel();
			jExcel.openDocument();
			//文件名称
			jExcel.setFilename(sheetNames[0]);
			for(int sheet = 0;sheet<resultList.length;sheet++){
				
				//改变工作溥 
				jExcel.WorkBookGetSheet(sheet);
				//工作溥名称
				jExcel.getWorkSheet().setName(sheetNames[sheet]);
				
				//列名称
				jExcel.setTitle(0, 2, titles.get(sheet));
				
				//表头
				jExcel.mergeCells(0, 0, sheet==0?14:12, 1);
				//内容
				jExcel.addDate(0,0, sheetNames[sheet]+"("+startDate+"~"+endDate+")",jExcel.titleCellFormat);
	            
				for(int i=0;i<resultList.length;i++){
					
					for(int k=0;k<resultList[i].size();k++){
						EffectStatCommonBean aBean = (EffectStatCommonBean)resultList[i].get(k);
						switch(sheet){
							case 0:
								jExcel.addDate(0, k+4, k+1);
								jExcel.addDate(1, k+4, aBean.getStation_name(),jExcel.dateCellFormat);
								jExcel.addDate(2, k+4, aBean.getFreqcount());
								jExcel.addDate(3, k+4, aBean.getFreqtime());
								jExcel.addDate(4, k+4, aBean.getService_area(),jExcel.dateCellFormat);
								jExcel.addDate(5, k+4, aBean.getLanguage_name(),jExcel.dateCellFormat);
								jExcel.addDate(6, k+4, aBean.getHeadname(),jExcel.dateCellFormat);
								jExcel.addDate(7, k+4, aBean.getReceivecount());
								jExcel.addDate(8, k+4, aBean.getGoodfreqtime());
								jExcel.addDate(9, k+4, aBean.getAudible());
								jExcel.addDate(10, k+4, aBean.getAudibility1());
								jExcel.addDate(11, k+4, aBean.getAudibility2());
								jExcel.addDate(12, k+4, aBean.getAudibility3());
								jExcel.addDate(13, k+4, aBean.getAudibility4());
								jExcel.addDate(14, k+4, aBean.getAudibility5());
								break;
							case 1:
								jExcel.addDate(0, k+4, k+1);
								jExcel.addDate(1, k+4, aBean.getService_area(),jExcel.dateCellFormat);
								jExcel.addDate(2, k+4, aBean.getFreqcount());
								jExcel.addDate(3, k+4, aBean.getFreqtime());
								jExcel.addDate(4, k+4, aBean.getHeadname(),jExcel.dateCellFormat);
								jExcel.addDate(5, k+4, aBean.getReceivecount());
								jExcel.addDate(6, k+4, aBean.getGoodfreqtime());
								jExcel.addDate(7, k+4, aBean.getAudible());
								jExcel.addDate(8, k+4, aBean.getAudibility1());
								jExcel.addDate(9, k+4, aBean.getAudibility2());
								jExcel.addDate(10, k+4, aBean.getAudibility3());
								jExcel.addDate(11, k+4, aBean.getAudibility4());
								jExcel.addDate(12, k+4, aBean.getAudibility5());
								break;
							case 2:
								jExcel.addDate(0, k+4, k+1);
								jExcel.addDate(1, k+4, aBean.getHeadname(),jExcel.dateCellFormat);
								jExcel.addDate(2, k+4, aBean.getFreqcount());
								jExcel.addDate(3, k+4, aBean.getFreqtime());
								jExcel.addDate(4, k+4, aBean.getService_area(),jExcel.dateCellFormat);
								jExcel.addDate(5, k+4, aBean.getReceivecount());
								jExcel.addDate(6, k+4, aBean.getGoodfreqtime());
								jExcel.addDate(7, k+4, aBean.getAudible());
								jExcel.addDate(8, k+4, aBean.getAudibility1());
								jExcel.addDate(9, k+4, aBean.getAudibility2());
								jExcel.addDate(10, k+4, aBean.getAudibility3());
								jExcel.addDate(11, k+4, aBean.getAudibility4());
								jExcel.addDate(12, k+4, aBean.getAudibility5());
								break;
						}
					}
				}
				
			}
			
			jExcel.saveDocument();
			jExcel.postExcel(request, response);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
