package com.viewscenes.web.statistics.effectReport;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

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
import com.viewscenes.bean.report.ZrstRepFreqStatisticsBean;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.printexcel.JExcel;
import com.viewscenes.pub.GDSet;
import com.viewscenes.util.StringTool;
import com.viewscenes.web.Daoutil.ReportUtil;

import flex.messaging.io.amf.ASObject;

public class FreqEffectReportStatistics {
	
	/**
	 * 
	 * @detail  
	 * @method  
	  * @param starttime 开始时间
	 * @param endtime	结束时间
	 * @param report_type 		报表类型
	 * @param user_name 用户名
	 * @param asobj
	 * @return
	 * @throws Exception 
	 * @return  boolean  
	 * @author  zhaoyahui
	 * @version 2013-3-7 下午02:38:07
	 */
	public static boolean statisticsReport(String starttime, String endtime,String report_type,String user_name,ASObject asobj) throws Exception{
		String report_id=ReportUtil.getReportId();
		ArrayList descList = new ArrayList();
		String subReportType = (String)asobj.get("subReportType");
		String[] subReportTypeArr = subReportType.split(","); 
		for(int i=0;i<subReportTypeArr.length;i++){
			if(subReportTypeArr[i].equals("1")){//各语言各频段效果对比表
				LinkedHashMap map = FreqEffectReportStatistics.statisticsReportCondition1(starttime, endtime, report_type, asobj, report_id);
				for(Object o:map.keySet()){
//					System.out.println(o); 		// Map的键
					descList.add(map.get(o));   // Map的值
				}


			} else if(subReportTypeArr[i].equals("2")){//各发射台各频段效果对比表
				LinkedHashMap map = FreqEffectReportStatistics.statisticsReportCondition2(starttime, endtime, report_type, asobj, report_id);
				for(Object o:map.keySet()){
					descList.add(map.get(o));   // Map的值
				}
				
			} else if(subReportTypeArr[i].equals("3")){//各时段各频段效果对比表
				LinkedHashMap map = FreqEffectReportStatistics.statisticsReportCondition3(starttime, endtime, report_type, asobj, report_id);
				for(Object o:map.keySet()){
					descList.add(map.get(o));   // Map的值
				}
				
			}
		}
		
		
		ReportBean reportBean = ReportUtil.getBasicStatisticsReportBean(report_id, starttime, endtime, report_type, user_name);

		ReportUtil.insertReportByReportBean(reportBean,  descList, "zrst_rep_freq_statistics_tab", "com.viewscenes.bean.report.ZrstRepFreqStatisticsBean");
		return true;
	}
	/**
	 * 根据频率返回对应的频段
	 * @detail  
	 * @method  
	 * @param freq
	 * @return 0、6M；1、7M；2、9M；3、11M；4、13M；5、15M；6、17M；7、19M；8、21M；9、25M；
	 * @return  int  
	 * @author  zhaoyahui
	 * @version 2013-3-7 下午04:42:45
	 */
	public static int getFreqM(String freq){
		int res = -1;
		int freqNum = Integer.parseInt(freq);
		if(freqNum>=5900 && freqNum<=6200){
			res = 0;
		} else if(freqNum>=7100 && freqNum<=7400){
			res = 1;
		} else if(freqNum>=9400 && freqNum<=9900){
			res = 2;
		} else if(freqNum>=11600 && freqNum<=12100){
			res = 3;
		} else if(freqNum>=13570 && freqNum<=13870){
			res = 4;
		} else if(freqNum>=15100 && freqNum<=15800){
			res = 5;
		} else if(freqNum>=17480 && freqNum<=17900){
			res = 6;
		} else if(freqNum>=18900 && freqNum<=19020){
			res = 7;
		} else if(freqNum>=21450 && freqNum<=21850){
			res = 8;
		} else if(freqNum>=25670 && freqNum<=26100){
			res = 9;
		}
		return res;
	}
	/**
	 * 
	 * @detail  
	 * @method  
	 * @param starttime
	 * @param endtime
	 * @param my_report_type
	 * @param report_type
	 * @param asobj
	 * @param report_id
	 * @return
	 * @throws Exception 
	 * @return  LinkedHashMap  
	 * @author  zhaoyahui
	 * @version 2013-3-7 下午02:53:53
	 */
	public static LinkedHashMap statisticsReportCondition1(String starttime, String endtime, String report_type,ASObject asobj,String report_id) throws Exception{
		String headcodes = (String)asobj.get("headcodes");
		LinkedHashMap<String,ZrstRepFreqStatisticsBean> map = new LinkedHashMap<String,ZrstRepFreqStatisticsBean>();
		HashMap<String,int[]> mapCount = new HashMap<String,int[]>();
		LinkedHashMap<String,String> listHead = new LinkedHashMap<String,String>();//存放查询出来的站点
		LinkedHashMap<String,String> listLanguage = new LinkedHashMap<String,String>();//存放查询出来的语言
		String sql = "select  " +
		" head.shortname receive_name,head.code headcode,mark.counto ,record.start_datetime,lan.language_name, runplan.freq,head.type_id" +
		" from RADIO_MARK_ZST_VIEW_TAB mark," +
		" radio_stream_result_tab record," +
		" zres_runplan_chaifen_tab runplan," +
		" zdic_language_tab lan ," +
		" res_headend_tab head" +
				" where mark.mark_file_url=record.url and mark.runplan_id=runplan.runplan_id and mark.counto is not null and mark.counti is not null and mark.counts is not null " +
				" and head.head_id=record.head_id" +
				" and record.is_delete=0 " +
				" and runplan.language_id=lan.language_id" +
				" and record.start_datetime>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss')" +
				" and end_datetime<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') order by head.shortname,lan.language_name ";
		
		 GDSet gd = DbComponent.Query(sql);
		 String[] freq_listensAll = {" "," "," "," "," "," "," "," "," "," "};
		 for(int i=0;i<gd.getRowCount();i++){

			 String headcode = gd.getString(i, "headcode");
			 String receive_name = gd.getString(i, "receive_name");
			 String type_id = gd.getString(i, "type_id");
			 if("102".equals(type_id) && ( headcode.endsWith("A") || headcode.endsWith("B")||headcode.endsWith("C") || headcode.endsWith("D")|| headcode.endsWith("E") || headcode.endsWith("F") || headcode.endsWith("G")  )){
						 headcode = headcode.substring(0, headcode.length()-1);
				// receive_name = receive_name.substring(0, receive_name.length()-1);
			 }
			 if("102".equals(type_id) && ( receive_name.endsWith("A") || receive_name.endsWith("B")||( receive_name.endsWith("C") || receive_name.endsWith("D")|| receive_name.endsWith("E") || receive_name.endsWith("F") || receive_name.endsWith("G")  ))){
						// headcode = headcode.substring(0, headcode.length()-1);
				receive_name = receive_name.substring(0, receive_name.length()-1);
			 }
			 if(headcodes == null || headcodes.equals("") || headcodes.indexOf(headcode+",")>=0){
			 } else{
				 continue;
			 }
//			 String transmit_station = gd.getString(i, "transmit_station");
			 String language_name = gd.getString(i, "language_name");
			 String freq = gd.getString(i, "freq");
			 int freqPosi = getFreqM(freq);
			 if(freqPosi == -1)
				 continue;
//			 String start_datetime = gd.getString(i, "start_datetime");//2012-02-03 21:00:33
//			 String monthStr = Integer.parseInt(start_datetime.substring(1,13))+"月";
			 listHead.put(receive_name, "");
			 listLanguage.put(language_name, "");
			 String counto = gd.getString(i, "counto");
			 int countoNum = Integer.parseInt(counto);
			 int big3Count = (countoNum>=3?1:0);
			 int[] headLanCount = {0,0};//同一站点语言的大于等于3分的次数和总的次数
			 int[] headAllLanCount = {0,0};//同一站点全部语言的大于等于3分的次数和总的次数
			 int[] headLanCountAll = {0,0};//全部站点语言的大于等于3分的次数和总的次数
			 int[] headLanAllCountAll = {0,0};//全部站点全部语言的大于等于3分的次数和总的次数
			 if(mapCount.get(receive_name+"_"+language_name+"_"+freqPosi) != null){
				 headLanCount = mapCount.get(receive_name+"_"+language_name+"_"+freqPosi);
			 }
			 if(mapCount.get(receive_name+"_"+freqPosi) != null){
				 headAllLanCount = mapCount.get(receive_name+"_"+freqPosi);
			 }
			 if(mapCount.get("All_"+language_name+"_"+freqPosi) != null){
				 headLanCountAll = mapCount.get("All_"+language_name+"_"+freqPosi);
			 }
			 if(mapCount.get("All"+"_"+freqPosi) != null){
				 headLanAllCountAll = mapCount.get("All"+"_"+freqPosi);
			 }
			 headLanCount[0] = headLanCount[0] + big3Count;
			 headLanCount[1] = headLanCount[1] + 1;
			 headAllLanCount[0] = headAllLanCount[0] + big3Count;
			 headAllLanCount[1] = headAllLanCount[1] + 1;
			 headLanCountAll[0] = headLanCountAll[0] + big3Count;
			 headLanCountAll[1] = headLanCountAll[1] + 1;
			 headLanAllCountAll[0] = headLanAllCountAll[0] + big3Count;
			 headLanAllCountAll[1] = headLanAllCountAll[1] + 1;
			 mapCount.put(receive_name+"_"+language_name+"_"+freqPosi,headLanCount);//计算次数后更新
			 mapCount.put(receive_name+"_"+freqPosi,headAllLanCount);
			 mapCount.put("All_"+language_name+"_"+freqPosi,headLanCountAll);
			 mapCount.put("All"+"_"+freqPosi,headLanAllCountAll);
			 
		 }
		 
		 int totalHead = 0;
		 int totalLanguage = 0;
		for(String o:listHead.keySet()){
			totalHead++;
			String receive_name = o;
			totalLanguage = 0;
			for(String p:listLanguage.keySet()){
				totalLanguage++;
				String language_name = p;
				String freq_listens = "";
				for(int q=0;q<10;q++){
					if(mapCount.get(receive_name+"_"+language_name+"_"+q) == null){
						freq_listens += " _";
					} else{
						int[] counts = mapCount.get(receive_name+"_"+language_name+"_"+q);
						freq_listens += (counts[0]*100/counts[1])+"_";
					}
				}
				freq_listens = freq_listens.substring(0,freq_listens.length()-1);
				ZrstRepFreqStatisticsBean detailBean = new ZrstRepFreqStatisticsBean();
				detailBean.setLanguage_name(language_name);
				detailBean.setReceive_name(receive_name);
				detailBean.setSub_report_type("1");
				detailBean.setFreq_listens(freq_listens);
				detailBean.setReport_id(report_id);
				map.put(receive_name+"_"+language_name, detailBean);
				if(totalLanguage == listLanguage.size()){//总计
					freq_listens = "";
					for(int qq=0;qq<10;qq++){
						if(mapCount.get(receive_name+"_"+qq) == null){
							freq_listens += " _";
						} else{
							int[] counts = mapCount.get(receive_name+"_"+qq);
							freq_listens += (counts[0]*100/counts[1])+"_";
						}
					}
					freq_listens = freq_listens.substring(0,freq_listens.length()-1);
					ZrstRepFreqStatisticsBean detailBean1 = new ZrstRepFreqStatisticsBean();
					detailBean1.setLanguage_name("总计");
					detailBean1.setReceive_name(receive_name);
					detailBean1.setSub_report_type("1");
					detailBean1.setFreq_listens(freq_listens);
					detailBean1.setReport_id(report_id);
					map.put(receive_name, detailBean1);
				}
			}
			if(totalHead == listHead.size()){//全部站点
				totalLanguage = 0;
				for(String p:listLanguage.keySet()){
					totalLanguage++;
					String language_name = p;
					String freq_listens = "";
					for(int q=0;q<10;q++){
						if(mapCount.get("All_"+language_name+"_"+q) == null){
							freq_listens += " _";
						} else{
							int[] counts = mapCount.get("All_"+language_name+"_"+q);
							freq_listens += (counts[0]*100/counts[1])+"_";
						}
					}
					freq_listens = freq_listens.substring(0,freq_listens.length()-1);
					ZrstRepFreqStatisticsBean detailBean = new ZrstRepFreqStatisticsBean();
					detailBean.setLanguage_name(language_name);
					detailBean.setReceive_name("全部");
					detailBean.setSub_report_type("1");
					detailBean.setFreq_listens(freq_listens);
					detailBean.setReport_id(report_id);
					map.put("全部_"+language_name, detailBean);
					if(totalLanguage == listLanguage.size()){//总计
						freq_listens = "";
						for(int qq=0;qq<10;qq++){
							if(mapCount.get("All_"+qq) == null){
								freq_listens += " _";
							} else{
								int[] counts = mapCount.get("All_"+qq);
								freq_listens += (counts[0]*100/counts[1])+"_";
							}
						}
						freq_listens = freq_listens.substring(0,freq_listens.length()-1);
						ZrstRepFreqStatisticsBean detailBean1 = new ZrstRepFreqStatisticsBean();
						detailBean1.setLanguage_name("总计");
						detailBean1.setReceive_name("全部");
						detailBean1.setSub_report_type("1");
						detailBean1.setFreq_listens(freq_listens);
						detailBean1.setReport_id(report_id);
						map.put("全部", detailBean1);
					}
				}
			}
		}
		return map;
	}
	
	/**
	 * 
	 * @detail  
	 * @method  
	 * @param starttime
	 * @param endtime
	 * @param my_report_type
	 * @param report_type
	 * @param asobj
	 * @param report_id
	 * @return
	 * @throws Exception 
	 * @return  LinkedHashMap  
	 * @author  zhaoyahui
	 * @version 2013-3-7 下午02:53:53
	 */
	public static LinkedHashMap statisticsReportCondition2(String starttime, String endtime, String report_type,ASObject asobj,String report_id) throws Exception{
		String headcodes = (String)asobj.get("headcodes");
		LinkedHashMap<String,ZrstRepFreqStatisticsBean> map = new LinkedHashMap<String,ZrstRepFreqStatisticsBean>();
		HashMap<String,int[]> mapCount = new HashMap<String,int[]>();
		LinkedHashMap<String,String> listHead = new LinkedHashMap<String,String>();//存放查询出来的站点
		LinkedHashMap<String,String> listTransmit = new LinkedHashMap<String,String>();//存放查询出来的发射台
		String sql = "select  " +
		" head.shortname receive_name,head.code headcode,mark.counto ,record.start_datetime,station.name stationname, runplan.freq,head.type_id" +
		" from RADIO_MARK_ZST_VIEW_TAB mark," +
		" radio_stream_result_tab record," +
		" zres_runplan_chaifen_tab runplan," +
		" res_transmit_station_tab station," +
		" res_headend_tab head" +
				" where mark.mark_file_url=record.url and mark.runplan_id=runplan.runplan_id and mark.counto is not null and mark.counti is not null and mark.counts is not null " +
				" and head.head_id=record.head_id" +
				" and runplan.station_id=station.station_id" +
				" and record.is_delete=0 " +
				" and record.start_datetime>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss')" +
				" and end_datetime<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') order by head.shortname,station.name ";
		 GDSet gd = DbComponent.Query(sql);
		 String[] freq_listensAll = {" "," "," "," "," "," "," "," "," "," "};
		 for(int i=0;i<gd.getRowCount();i++){
			 String headcode = gd.getString(i, "headcode");
			 String receive_name = gd.getString(i, "receive_name");
			 String type_id = gd.getString(i, "type_id");
			 if("102".equals(type_id) && ( headcode.endsWith("A") || headcode.endsWith("B")||headcode.endsWith("C") || headcode.endsWith("D")|| headcode.endsWith("E") || headcode.endsWith("F") || headcode.endsWith("G")  )){
						 headcode = headcode.substring(0, headcode.length()-1);
				// receive_name = receive_name.substring(0, receive_name.length()-1);
			 }
			 if("102".equals(type_id) && ( receive_name.endsWith("A") || receive_name.endsWith("B")||( receive_name.endsWith("C") || receive_name.endsWith("D")|| receive_name.endsWith("E") || receive_name.endsWith("F") || receive_name.endsWith("G")  ))){
							// headcode = headcode.substring(0, headcode.length()-1);
					receive_name = receive_name.substring(0, receive_name.length()-1);
				 }
			 if(headcodes == null || headcodes.equals("") || headcodes.indexOf(headcode+",")>=0){
			 } else{
				 continue;
			 }
//			 String transmit_station = gd.getString(i, "transmit_station");
			 String stationname = gd.getString(i, "stationname");
			 String freq = gd.getString(i, "freq");
			 int freqPosi = getFreqM(freq);
			 if(freqPosi == -1)
				 continue;
//			 String start_datetime = gd.getString(i, "start_datetime");//2012-02-03 21:00:33
//			 String monthStr = Integer.parseInt(start_datetime.substring(1,13))+"月";
			 listHead.put(receive_name, "");
			 listTransmit.put(stationname, "");
			 String counto = gd.getString(i, "counto");
			 int countoNum = Integer.parseInt(counto);
			 int big3Count = (countoNum>=3?1:0);
			 int[] headLanCount = {0,0};//同一站点发射台的大于等于3分的次数和总的次数
			 int[] headAllLanCount = {0,0};//同一站点全部发射台的大于等于3分的次数和总的次数
			 int[] headLanCountAll = {0,0};//全部站点发射台的大于等于3分的次数和总的次数
			 int[] headLanAllCountAll = {0,0};//全部站点全部发射台的大于等于3分的次数和总的次数
			 if(mapCount.get(receive_name+"_"+stationname+"_"+freqPosi) != null){
				 headLanCount = mapCount.get(receive_name+"_"+stationname+"_"+freqPosi);
			 }
			 if(mapCount.get(receive_name+"_"+freqPosi) != null){
				 headAllLanCount = mapCount.get(receive_name+"_"+freqPosi);
			 }
			 if(mapCount.get("All_"+stationname+"_"+freqPosi) != null){
				 headLanCountAll = mapCount.get("All_"+stationname+"_"+freqPosi);
			 }
			 if(mapCount.get("All"+"_"+freqPosi) != null){
				 headLanAllCountAll = mapCount.get("All"+"_"+freqPosi);
			 }
			 headLanCount[0] = headLanCount[0] + big3Count;
			 headLanCount[1] = headLanCount[1] + 1;
			 headAllLanCount[0] = headAllLanCount[0] + big3Count;
			 headAllLanCount[1] = headAllLanCount[1] + 1;
			 headLanCountAll[0] = headLanCountAll[0] + big3Count;
			 headLanCountAll[1] = headLanCountAll[1] + 1;
			 headLanAllCountAll[0] = headLanAllCountAll[0] + big3Count;
			 headLanAllCountAll[1] = headLanAllCountAll[1] + 1;
			 mapCount.put(receive_name+"_"+stationname+"_"+freqPosi,headLanCount);//计算次数后更新
			 mapCount.put(receive_name+"_"+freqPosi,headAllLanCount);
			 mapCount.put("All_"+stationname+"_"+freqPosi,headLanCountAll);
			 mapCount.put("All"+"_"+freqPosi,headLanAllCountAll);
			 
		 }
		 
		 int totalHead = 0;
		 int totalTransmit = 0;
		for(String o:listHead.keySet()){
			totalHead++;
			String receive_name = o;
			totalTransmit = 0;
			for(String p:listTransmit.keySet()){
				totalTransmit++;
				String tran_station = p;
				String freq_listens = "";
				for(int q=0;q<10;q++){
					if(mapCount.get(receive_name+"_"+tran_station+"_"+q) == null){
						freq_listens += " _";
					} else{
						int[] counts = mapCount.get(receive_name+"_"+tran_station+"_"+q);
						freq_listens += (counts[0]*100/counts[1])+"_";
					}
				}
				freq_listens = freq_listens.substring(0,freq_listens.length()-1);
				ZrstRepFreqStatisticsBean detailBean = new ZrstRepFreqStatisticsBean();
				detailBean.setTransmit_station(tran_station);
				detailBean.setReceive_name(receive_name);
				detailBean.setSub_report_type("2");
				detailBean.setFreq_listens(freq_listens);
				detailBean.setReport_id(report_id);
				map.put(receive_name+"_"+tran_station, detailBean);
				if(totalTransmit == listTransmit.size()){//总计
					freq_listens = "";
					for(int qq=0;qq<10;qq++){
						if(mapCount.get(receive_name+"_"+qq) == null){
							freq_listens += " _";
						} else{
							int[] counts = mapCount.get(receive_name+"_"+qq);
							freq_listens += (counts[0]*100/counts[1])+"_";
						}
					}
					freq_listens = freq_listens.substring(0,freq_listens.length()-1);
					ZrstRepFreqStatisticsBean detailBean1 = new ZrstRepFreqStatisticsBean();
					detailBean1.setTransmit_station("总计");
					detailBean1.setReceive_name(receive_name);
					detailBean1.setSub_report_type("2");
					detailBean1.setFreq_listens(freq_listens);
					detailBean1.setReport_id(report_id);
					map.put(receive_name, detailBean1);
				}
			}
			if(totalHead == listHead.size()){//全部站点
				totalTransmit = 0;
				for(String p:listTransmit.keySet()){
					totalTransmit++;
					String tran_station = p;
					String freq_listens = "";
					for(int q=0;q<10;q++){
						if(mapCount.get("All_"+tran_station+"_"+q) == null){
							freq_listens += " _";
						} else{
							int[] counts = mapCount.get("All_"+tran_station+"_"+q);
							freq_listens += (counts[0]*100/counts[1])+"_";
						}
					}
					freq_listens = freq_listens.substring(0,freq_listens.length()-1);
					ZrstRepFreqStatisticsBean detailBean = new ZrstRepFreqStatisticsBean();
					detailBean.setTransmit_station(tran_station);
					detailBean.setReceive_name("全部");
					detailBean.setSub_report_type("2");
					detailBean.setFreq_listens(freq_listens);
					detailBean.setReport_id(report_id);
					map.put("全部_"+tran_station, detailBean);
					if(totalTransmit == listTransmit.size()){//总计
						freq_listens = "";
						for(int qq=0;qq<10;qq++){
							if(mapCount.get("All_"+qq) == null){
								freq_listens += " _";
							} else{
								int[] counts = mapCount.get("All_"+qq);
								freq_listens += (counts[0]*100/counts[1])+"_";
							}
						}
						freq_listens = freq_listens.substring(0,freq_listens.length()-1);
						ZrstRepFreqStatisticsBean detailBean1 = new ZrstRepFreqStatisticsBean();
						detailBean1.setTransmit_station("总计");
						detailBean1.setReceive_name("全部");
						detailBean1.setSub_report_type("2");
						detailBean1.setFreq_listens(freq_listens);
						detailBean1.setReport_id(report_id);
						map.put("全部", detailBean1);
					}
				}
			}
		}
		return map;
	}
	
	/**
	 * 
	 * @detail  
	 * @method  
	 * @param starttime
	 * @param endtime
	 * @param my_report_type
	 * @param report_type
	 * @param asobj
	 * @param report_id
	 * @return
	 * @throws Exception 
	 * @return  LinkedHashMap  
	 * @author  zhaoyahui
	 * @version 2013-3-7 下午02:53:53
	 */
	public static LinkedHashMap statisticsReportCondition3(String starttime, String endtime, String report_type,ASObject asobj,String report_id) throws Exception{
		String headcodes = (String)asobj.get("headcodes");
		LinkedHashMap<String,ZrstRepFreqStatisticsBean> map = new LinkedHashMap<String,ZrstRepFreqStatisticsBean>();
		HashMap<String,int[]> mapCount = new HashMap<String,int[]>();
		LinkedHashMap<String,String> listHead = new LinkedHashMap<String,String>();//存放查询出来的站点
		LinkedHashMap<String,String> listPlanTime = new LinkedHashMap<String,String>();//存放查询出来的时段
		String sql = "select  " +
		" head.shortname receive_name,head.code headcode,mark.counto ,record.start_datetime ,runplan.freq,head.type_id" +
		" from RADIO_MARK_ZST_VIEW_TAB mark," +
		" radio_stream_result_tab record," +
		" zres_runplan_chaifen_tab runplan," +
		" res_headend_tab head" +
				" where mark.mark_file_url=record.url and mark.runplan_id=runplan.runplan_id and mark.counto is not null and mark.counti is not null and mark.counts is not null " +
				" and head.head_id=record.head_id" +
				" and record.is_delete=0 " +
				" and record.start_datetime>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss')" +
				" and end_datetime<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') order by head.shortname ";
		 GDSet gd = DbComponent.Query(sql);
		 String[] freq_listensAll = {" "," "," "," "," "," "," "," "," "," "};
		 for(int i=0;i<gd.getRowCount();i++){
			 String headcode = gd.getString(i, "headcode");
			 String receive_name = gd.getString(i, "receive_name");
			 String type_id = gd.getString(i, "type_id");
			 if("102".equals(type_id) && ( headcode.endsWith("A") || headcode.endsWith("B")||headcode.endsWith("C") || headcode.endsWith("D")|| headcode.endsWith("E") || headcode.endsWith("F") || headcode.endsWith("G")  )){
						 headcode = headcode.substring(0, headcode.length()-1);
				 //receive_name = receive_name.substring(0, receive_name.length()-1);
			 }
			 if("102".equals(type_id) && ( receive_name.endsWith("A") || receive_name.endsWith("B")||( receive_name.endsWith("C") || receive_name.endsWith("D")|| receive_name.endsWith("E") || receive_name.endsWith("F") || receive_name.endsWith("G")  ))){
							// headcode = headcode.substring(0, headcode.length()-1);
					receive_name = receive_name.substring(0, receive_name.length()-1);
				 }
			 if(headcodes == null || headcodes.equals("") || headcodes.indexOf(headcode+",")>=0){
			 } else{
				 continue;
			 }
			 String freq = gd.getString(i, "freq");
			 int freqPosi = getFreqM(freq);
			 if(freqPosi == -1)
				 continue;
			 String start_datetime = gd.getString(i, "start_datetime");//2012-02-03 21:00:33
			 String playTime = "test";
			 switch(Integer.parseInt(start_datetime.substring(11,13))){
			 	case 0:
			 		playTime = "00:00-01:00";
			 		break;
			 	case 1:
			 		playTime = "01:00-02:00";
			 		break;
			 	case 2:
			 		playTime = "02:00-03:00";
			 		break;
			 	case 3:
			 		playTime = "03:00-04:00";
			 		break;
			 	case 4:
			 		playTime = "04:00-05:00";
			 		break;
			 	case 5:
			 		playTime = "05:00-06:00";
			 		break;
			 	case 6:
			 		playTime = "06:00-06:00";
			 		break;
			 	case 7:
			 		playTime = "07:00-08:00";
			 		break;
			 	case 8:
			 		playTime = "08:00-09:00";
			 		break;
			 	case 9:
			 		playTime = "09:00-10:00";
			 		break;
			 	case 10:
			 		playTime = "10:00-11:00";
			 		break;
			 	case 11:
			 		playTime = "11:00-12:00";
			 		break;
			 	case 12:
			 		playTime = "12:00-13:00";
			 		break;
			 	case 13:
			 		playTime = "13:00-14:00";
			 		break;
			 	case 14:
			 		playTime = "14:00-15:00";
			 		break;
			 	case 15:
			 		playTime = "15:00-16:00";
			 		break;
			 	case 16:
			 		playTime = "16:00-17:00";
			 		break;
			 	case 17:
			 		playTime = "17:00-18:00";
			 		break;
			 	case 18:
			 		playTime = "18:00-19:00";
			 		break;
			 	case 19:
			 		playTime = "19:00-20:00";
			 		break;
			 	case 20:
			 		playTime = "20:00-21:00";
			 		break;
			 	case 21:
			 		playTime = "21:00-22:00";
			 		break;
			 	case 22:
			 		playTime = "22:00-23:00";
			 		break;
			 	case 23:
			 		playTime = "23:00-00:00";
			 		break;
			 }
			 listPlanTime.put(playTime, "");
			 listHead.put(receive_name, "");
			 String counto = gd.getString(i, "counto");
			 int countoNum = Integer.parseInt(counto);
			 int big3Count = (countoNum>=3?1:0);
			 int[] headLanCount = {0,0};//同一站点时段的大于等于3分的次数和总的次数
			 int[] headAllLanCount = {0,0};//同一站点全部时段的大于等于3分的次数和总的次数
			 int[] headLanCountAll = {0,0};//全部站点时段的大于等于3分的次数和总的次数
			 int[] headLanAllCountAll = {0,0};//全部站点全部时段的大于等于3分的次数和总的次数
			 if(mapCount.get(receive_name+"_"+playTime+"_"+freqPosi) != null){
				 headLanCount = mapCount.get(receive_name+"_"+playTime+"_"+freqPosi);
			 }
			 if(mapCount.get(receive_name+"_"+freqPosi) != null){
				 headAllLanCount = mapCount.get(receive_name+"_"+freqPosi);
			 }
			 if(mapCount.get("All_"+playTime+"_"+freqPosi) != null){
				 headLanCountAll = mapCount.get("All_"+playTime+"_"+freqPosi);
			 }
			 if(mapCount.get("All"+"_"+freqPosi) != null){
				 headLanAllCountAll = mapCount.get("All"+"_"+freqPosi);
			 }
			 headLanCount[0] = headLanCount[0] + big3Count;
			 headLanCount[1] = headLanCount[1] + 1;
			 headAllLanCount[0] = headAllLanCount[0] + big3Count;
			 headAllLanCount[1] = headAllLanCount[1] + 1;
			 headLanCountAll[0] = headLanCountAll[0] + big3Count;
			 headLanCountAll[1] = headLanCountAll[1] + 1;
			 headLanAllCountAll[0] = headLanAllCountAll[0] + big3Count;
			 headLanAllCountAll[1] = headLanAllCountAll[1] + 1;
			 mapCount.put(receive_name+"_"+playTime+"_"+freqPosi,headLanCount);//计算次数后更新
			 mapCount.put(receive_name+"_"+freqPosi,headAllLanCount);
			 mapCount.put("All_"+playTime+"_"+freqPosi,headLanCountAll);
			 mapCount.put("All"+"_"+freqPosi,headLanAllCountAll);
			 
		 }
		 
		 int totalHead = 0;
		 int totalTransmit = 0;
		for(String o:listHead.keySet()){
			totalHead++;
			String receive_name = o;
			totalTransmit = 0;
			for(String p:listPlanTime.keySet()){
				totalTransmit++;
				String play_time = p;
				String freq_listens = "";
				for(int q=0;q<10;q++){
					if(mapCount.get(receive_name+"_"+play_time+"_"+q) == null){
						freq_listens += " _";
					} else{
						int[] counts = mapCount.get(receive_name+"_"+play_time+"_"+q);
						freq_listens += (counts[0]*100/counts[1])+"_";
					}
				}
				freq_listens = freq_listens.substring(0,freq_listens.length()-1);
				ZrstRepFreqStatisticsBean detailBean = new ZrstRepFreqStatisticsBean();
				detailBean.setPlay_time(play_time);
				detailBean.setReceive_name(receive_name);
				detailBean.setSub_report_type("3");
				detailBean.setFreq_listens(freq_listens);
				detailBean.setReport_id(report_id);
				map.put(receive_name+"_"+play_time, detailBean);
				if(totalTransmit == listPlanTime.size()){//总计
					freq_listens = "";
					for(int qq=0;qq<10;qq++){
						if(mapCount.get(receive_name+"_"+qq) == null){
							freq_listens += " _";
						} else{
							int[] counts = mapCount.get(receive_name+"_"+qq);
							freq_listens += (counts[0]*100/counts[1])+"_";
						}
					}
					freq_listens = freq_listens.substring(0,freq_listens.length()-1);
					ZrstRepFreqStatisticsBean detailBean1 = new ZrstRepFreqStatisticsBean();
					detailBean1.setPlay_time("总计");
					detailBean1.setReceive_name(receive_name);
					detailBean1.setSub_report_type("3");
					detailBean1.setFreq_listens(freq_listens);
					detailBean1.setReport_id(report_id);
					map.put(receive_name, detailBean1);
				}
			}
			if(totalHead == listHead.size()){//全部站点
				totalTransmit = 0;
				for(String p:listPlanTime.keySet()){
					totalTransmit++;
					String play_time = p;
					String freq_listens = "";
					for(int q=0;q<10;q++){
						if(mapCount.get("All_"+play_time+"_"+q) == null){
							freq_listens += " _";
						} else{
							int[] counts = mapCount.get("All_"+play_time+"_"+q);
							freq_listens += (counts[0]*100/counts[1])+"_";
						}
					}
					freq_listens = freq_listens.substring(0,freq_listens.length()-1);
					ZrstRepFreqStatisticsBean detailBean = new ZrstRepFreqStatisticsBean();
					detailBean.setPlay_time(play_time);
					detailBean.setReceive_name("全部");
					detailBean.setSub_report_type("3");
					detailBean.setFreq_listens(freq_listens);
					detailBean.setReport_id(report_id);
					map.put("全部_"+play_time, detailBean);
					if(totalTransmit == listPlanTime.size()){//总计
						freq_listens = "";
						for(int qq=0;qq<10;qq++){
							if(mapCount.get("All_"+qq) == null){
								freq_listens += " _";
							} else{
								int[] counts = mapCount.get("All_"+qq);
								freq_listens += (counts[0]*100/counts[1])+"_";
							}
						}
						freq_listens = freq_listens.substring(0,freq_listens.length()-1);
						ZrstRepFreqStatisticsBean detailBean1 = new ZrstRepFreqStatisticsBean();
						detailBean1.setPlay_time("总计");
						detailBean1.setReceive_name("全部");
						detailBean1.setSub_report_type("3");
						detailBean1.setFreq_listens(freq_listens);
						detailBean1.setReport_id(report_id);
						map.put("全部", detailBean1);
					}
				}
			}
		}
		return map;
	}
	
	/**
	 * 根据报表id查询详细数据
	 * @detail  
	 * @method  
	 * @param asobj
	 * @return 
	 * @return  ArrayList  
	 * @author  zhaoyahui
	 * @version 2013-1-11 上午10:45:21
	 */
	public ArrayList queryDetailReport(ASObject asobj) throws Exception{
		ArrayList list = new ArrayList();
		String reportId  = (String)asobj.get("reportId");
		String  sql  = "select t.*　from zrst_rep_freq_statistics_tab t where t.report_id="+reportId +" order by  t.sub_report_type ,t.data_id";
		GDSet gdSet= DbComponent.Query(sql);
		String classpath = ZrstRepFreqStatisticsBean.class.getName();
		list = StringTool.converGDSetToBeanList(gdSet, classpath);
		
		return list;
	}
	public void doExcel(String msg,HttpServletRequest request,HttpServletResponse response) throws Exception{
		Element root = StringTool.getXMLRoot(msg);
		String reportId = root.getChildText("reportId");
		String starttime = root.getChildText("starttime").replaceAll("-", "");
		String endtime = root.getChildText("endtime").replaceAll("-", "");

		String fileName="海外站点频段效果统计表";
		if(starttime.equals(endtime)){
			fileName = "海外站点"+starttime+"期频段效果统计表";
		} else if(starttime.substring(0, 6).equals(endtime.substring(0, 6))){
			fileName = "海外站点"+starttime.substring(0, 6)+"期频段效果统计表";
		} else{
			fileName = "海外站点"+starttime.substring(0, 4)+"期频段效果统计表";
		}
		ASObject obj = new ASObject();
		obj.put("reportId", reportId);
		ArrayList list = (ArrayList) queryDetailReport(obj);
		
		JExcel jExcel = new JExcel();
		
		OutputStream outputStream = response.getOutputStream();
		response.setContentType("application/vnd.ms-excel");
		response.reset();
		response.setHeader("Location", "Export.xls");
		response.setHeader("Expires", "0");
        response.setHeader("Content-disposition", "attachment; filename="+new String((fileName).getBytes("GBK"),"ISO-8859-1")+".xls");
        
		WritableWorkbook wwb =Workbook.createWorkbook(outputStream);
		
		 WritableFont wfTitle = new WritableFont(WritableFont.createFont("黑体"), 12,WritableFont.BOLD, false);
         WritableCellFormat wcfFTitle = new WritableCellFormat(wfTitle);
		wcfFTitle.setAlignment(jxl.format.Alignment.CENTRE);
		wcfFTitle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			 
		WritableFont wfHead = new WritableFont(WritableFont.createFont("黑体"), 10,WritableFont.BOLD, false);
		WritableCellFormat wcfFHead = new WritableCellFormat(wfHead);
		wcfFHead.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);//带边框 
		wcfFHead.setWrap(true);//自动换行
         //用于Number的格式
         //jxl.write.NumberFormat nf = new jxl.write.NumberFormat("0.00"); 
         //jxl.write.WritableCellFormat priceformat = new jxl.write.WritableCellFormat(nf);
         // 把水平对齐方式指定为左对齐
		wcfFHead.setAlignment(jxl.format.Alignment.CENTRE);
         // 把垂直对齐方式指定为居中对齐
		wcfFHead.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		
		WritableFont wf1 = new WritableFont(WritableFont.createFont("宋体"), 10,WritableFont.NO_BOLD, false);
        jxl.write.WritableCellFormat wcfF2 = new jxl.write.WritableCellFormat(wf1);
        wcfF2.setAlignment(jxl.format.Alignment.CENTRE);
        wcfF2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        wcfF2.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); 
        

		HashMap<String,Integer> beginRow = new HashMap<String,Integer>();//存放每页第二、第三个表格开始的行数
		for(int i=0;i<list.size();i++){
			ZrstRepFreqStatisticsBean bean = (ZrstRepFreqStatisticsBean)list.get(i);
			WritableSheet ws =  null;
			if(bean.getSub_report_type().equals("1")){//各语言各频段效果对比表
				if(wwb.getSheet(bean.getReceive_name()) == null){
					ws = wwb.createSheet(bean.getReceive_name(), wwb.getNumberOfSheets()+1);
                    ws.addCell(new Label(0,0,bean.getReceive_name()+"各语言频段效果对比表",wcfFTitle));
                    ws.mergeCells(0,0,10,0);//开始列，开始行，结束列，结束行
                    ws.setRowView(0, 740);//设置第一行高度
                    ws.setRowView(1, 500);
                     //设置列名
                     ws.addCell(new Label(0,1,"语言",wcfFHead));
                     ws.addCell(new Label(1,1,"6M",wcfFHead));
                     ws.addCell(new Label(2,1,"7M",wcfFHead));
                     ws.addCell(new Label(3,1,"9M",wcfFHead));
                     ws.addCell(new Label(4,1,"11M",wcfFHead));
                     ws.addCell(new Label(5,1,"13M",wcfFHead));
                     ws.addCell(new Label(6,1,"15M",wcfFHead));
                     ws.addCell(new Label(7,1,"17M",wcfFHead));
                     ws.addCell(new Label(8,1,"19M",wcfFHead));
                     ws.addCell(new Label(9,1,"21M",wcfFHead));
                     ws.addCell(new Label(10,1,"25M",wcfFHead));
				} else  {
					ws = wwb.getSheet(bean.getReceive_name()); 
				}
				int currRow = ws.getRows();
				String[] freq_linstens = bean.getFreq_listens().split("_");
                ws.addCell(new Label(0,currRow,bean.getLanguage_name(),wcfF2));
                ws.addCell(new Label(1,currRow,(freq_linstens[0].equals(" ")?" ":freq_linstens[0]+"%"),wcfF2));
                ws.addCell(new Label(2,currRow,(freq_linstens[1].equals(" ")?" ":freq_linstens[1]+"%"),wcfF2));
                ws.addCell(new Label(3,currRow,(freq_linstens[2].equals(" ")?" ":freq_linstens[2]+"%"),wcfF2));
                ws.addCell(new Label(4,currRow,(freq_linstens[3].equals(" ")?" ":freq_linstens[3]+"%"),wcfF2));
                ws.addCell(new Label(5,currRow,(freq_linstens[4].equals(" ")?" ":freq_linstens[4]+"%"),wcfF2));
                ws.addCell(new Label(6,currRow,(freq_linstens[5].equals(" ")?" ":freq_linstens[5]+"%"),wcfF2));
                ws.addCell(new Label(7,currRow,(freq_linstens[6].equals(" ")?" ":freq_linstens[6]+"%"),wcfF2));
                ws.addCell(new Label(8,currRow,(freq_linstens[7].equals(" ")?" ":freq_linstens[7]+"%"),wcfF2));
                ws.addCell(new Label(9,currRow,(freq_linstens[8].equals(" ")?" ":freq_linstens[8]+"%"),wcfF2));
                ws.addCell(new Label(10,currRow,(freq_linstens[9].equals(" ")?" ":freq_linstens[9]+"%"),wcfF2));
                
				
			} else if(bean.getSub_report_type().equals("2")){//各发射台各频段效果对比表
				int currRow = 0;
				if(wwb.getSheet(bean.getReceive_name()) == null ){
					ws = wwb.createSheet(bean.getReceive_name(), wwb.getNumberOfSheets()+1);
					
				} else  {
					ws = wwb.getSheet(bean.getReceive_name());
				}
				if(beginRow.get(bean.getReceive_name()+"_发射台") == null){
					currRow = (ws.getRows()==0)?0:ws.getRows()+3;
                    ws.addCell(new Label(0,currRow,bean.getReceive_name()+"各发射台频段效果对比表",wcfFTitle));
                    ws.mergeCells(0,currRow,10,currRow);//开始列，开始行，结束列，结束行
                    ws.setRowView(currRow, 740);//设置第一行高度
                    currRow++;
                    ws.setRowView(currRow, 500);
                    beginRow.put(bean.getReceive_name()+"_发射台", currRow);
                     //设置列名
                     ws.addCell(new Label(0,currRow,"发射台",wcfFHead));
                     ws.addCell(new Label(1,currRow,"6M",wcfFHead));
                     ws.addCell(new Label(2,currRow,"7M",wcfFHead));
                     ws.addCell(new Label(3,currRow,"9M",wcfFHead));
                     ws.addCell(new Label(4,currRow,"11M",wcfFHead));
                     ws.addCell(new Label(5,currRow,"13M",wcfFHead));
                     ws.addCell(new Label(6,currRow,"15M",wcfFHead));
                     ws.addCell(new Label(7,currRow,"17M",wcfFHead));
                     ws.addCell(new Label(8,currRow,"19M",wcfFHead));
                     ws.addCell(new Label(9,currRow,"21M",wcfFHead));
                     ws.addCell(new Label(10,currRow,"25M",wcfFHead));
                    currRow++;
				} else{
					currRow = beginRow.get(bean.getReceive_name()+"_发射台")+1;
				}
				beginRow.put(bean.getReceive_name()+"_发射台", currRow);
				
				String[] freq_linstens = bean.getFreq_listens().split("_");
                ws.addCell(new Label(0,currRow,bean.getTransmit_station(),wcfF2));
                ws.addCell(new Label(1,currRow,(freq_linstens[0].equals(" ")?" ":freq_linstens[0]+"%"),wcfF2));
                ws.addCell(new Label(2,currRow,(freq_linstens[1].equals(" ")?" ":freq_linstens[1]+"%"),wcfF2));
                ws.addCell(new Label(3,currRow,(freq_linstens[2].equals(" ")?" ":freq_linstens[2]+"%"),wcfF2));
                ws.addCell(new Label(4,currRow,(freq_linstens[3].equals(" ")?" ":freq_linstens[3]+"%"),wcfF2));
                ws.addCell(new Label(5,currRow,(freq_linstens[4].equals(" ")?" ":freq_linstens[4]+"%"),wcfF2));
                ws.addCell(new Label(6,currRow,(freq_linstens[5].equals(" ")?" ":freq_linstens[5]+"%"),wcfF2));
                ws.addCell(new Label(7,currRow,(freq_linstens[6].equals(" ")?" ":freq_linstens[6]+"%"),wcfF2));
                ws.addCell(new Label(8,currRow,(freq_linstens[7].equals(" ")?" ":freq_linstens[7]+"%"),wcfF2));
                ws.addCell(new Label(9,currRow,(freq_linstens[8].equals(" ")?" ":freq_linstens[8]+"%"),wcfF2));
                ws.addCell(new Label(10,currRow,(freq_linstens[9].equals(" ")?" ":freq_linstens[9]+"%"),wcfF2));
                

                
			} else if(bean.getSub_report_type().equals("3")){//各时段各频段效果对比表
				int currRow = 0;
				if(wwb.getSheet(bean.getReceive_name()) == null ){
					ws = wwb.createSheet(bean.getReceive_name(), wwb.getNumberOfSheets()+1);
					
				} else  {
					ws = wwb.getSheet(bean.getReceive_name());
				}
				if(beginRow.get(bean.getReceive_name()+"_时段") == null){
					currRow = (ws.getRows()==0)?0:ws.getRows()+3;
                    ws.addCell(new Label(0,currRow,bean.getReceive_name()+"各时段各频段效果对比表",wcfFTitle));
                    ws.mergeCells(0,currRow,10,currRow);//开始列，开始行，结束列，结束行
                    ws.setRowView(currRow, 740);//设置第一行高度
                    currRow++;
                    ws.setRowView(currRow, 500);
                    beginRow.put(bean.getReceive_name()+"_时段", currRow);
                     //设置列名
                     ws.addCell(new Label(0,currRow,"时段",wcfFHead));
                     ws.addCell(new Label(1,currRow,"6M",wcfFHead));
                     ws.addCell(new Label(2,currRow,"7M",wcfFHead));
                     ws.addCell(new Label(3,currRow,"9M",wcfFHead));
                     ws.addCell(new Label(4,currRow,"11M",wcfFHead));
                     ws.addCell(new Label(5,currRow,"13M",wcfFHead));
                     ws.addCell(new Label(6,currRow,"15M",wcfFHead));
                     ws.addCell(new Label(7,currRow,"17M",wcfFHead));
                     ws.addCell(new Label(8,currRow,"19M",wcfFHead));
                     ws.addCell(new Label(9,currRow,"21M",wcfFHead));
                     ws.addCell(new Label(10,currRow,"25M",wcfFHead));

                     ws.setColumnView(0, 13);
                    currRow++;
				} else{
					currRow = beginRow.get(bean.getReceive_name()+"_时段")+1;
				}
				beginRow.put(bean.getReceive_name()+"_时段", currRow);
				
				String[] freq_linstens = bean.getFreq_listens().split("_");
                ws.addCell(new Label(0,currRow,bean.getPlay_time(),wcfF2));
                ws.addCell(new Label(1,currRow,(freq_linstens[0].equals(" ")?" ":freq_linstens[0]+"%"),wcfF2));
                ws.addCell(new Label(2,currRow,(freq_linstens[1].equals(" ")?" ":freq_linstens[1]+"%"),wcfF2));
                ws.addCell(new Label(3,currRow,(freq_linstens[2].equals(" ")?" ":freq_linstens[2]+"%"),wcfF2));
                ws.addCell(new Label(4,currRow,(freq_linstens[3].equals(" ")?" ":freq_linstens[3]+"%"),wcfF2));
                ws.addCell(new Label(5,currRow,(freq_linstens[4].equals(" ")?" ":freq_linstens[4]+"%"),wcfF2));
                ws.addCell(new Label(6,currRow,(freq_linstens[5].equals(" ")?" ":freq_linstens[5]+"%"),wcfF2));
                ws.addCell(new Label(7,currRow,(freq_linstens[6].equals(" ")?" ":freq_linstens[6]+"%"),wcfF2));
                ws.addCell(new Label(8,currRow,(freq_linstens[7].equals(" ")?" ":freq_linstens[7]+"%"),wcfF2));
                ws.addCell(new Label(9,currRow,(freq_linstens[8].equals(" ")?" ":freq_linstens[8]+"%"),wcfF2));
                ws.addCell(new Label(10,currRow,(freq_linstens[9].equals(" ")?" ":freq_linstens[9]+"%"),wcfF2));
                
			}
		}
		
	    	
			wwb.write();
	        wwb.close();
	        outputStream.close();


	}
}
