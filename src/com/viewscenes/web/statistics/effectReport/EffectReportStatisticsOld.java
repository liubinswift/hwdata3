package com.viewscenes.web.statistics.effectReport;

import com.viewscenes.bean.ReportBean;
import com.viewscenes.bean.report.ZrstRepEffectStatisticsBean;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.pub.GDSet;
import com.viewscenes.util.StringTool;
import com.viewscenes.web.Daoutil.ReportUtil;
import flex.messaging.io.amf.ASObject;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.*;
import org.jdom.Element;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
public class EffectReportStatisticsOld {

	/**
	 * 
	 * @detail  
	 * @method  
	 * @param starttime ��ʼʱ��
	 * @param endtime	����ʱ��
	 * @param my_report_type	��������  1������̨Ч������ͳ��  2���������Ч������ͳ��
	 * @param report_type 		��������
	 * @param user_name �û���
	 * @throws Exception 
	 * @return  void  
	 * @author  zhaoyahui
	 * @version 2013-1-6 ����03:12:17
	 */
	public boolean statisticsReport(String starttime, String endtime,String my_report_type,String report_type,String user_name,ASObject asobj) throws Exception{
        long a=System.currentTimeMillis();
		String report_id=ReportUtil.getReportId();
		ArrayList descList = new ArrayList();
		String subReportType = (String)asobj.get("subReportType");

		String[] subReportTypeArr = subReportType.split(","); 
		
		for(int i=0;i<subReportTypeArr.length;i++){
			if(subReportTypeArr[i].equals("11")){//����̨�㲥Ч��ͳ�Ʊ� ���ߺ������Ч��ͳ��
				LinkedHashMap map = statisticsReportCondition11(starttime, endtime, my_report_type,report_type, asobj, report_id);
				for(Object o:map.keySet()){
					descList.add(map.get(o));   // Map��ֵ
				}
			} else if(subReportTypeArr[i].equals("21")){// 21������̨���岥��Ч��ͳ��1��
				LinkedHashMap map = statisticsReportCondition21(starttime, endtime, my_report_type,report_type, asobj, report_id);
				for(Object o:map.keySet()){
					descList.add(map.get(o));   // Map��ֵ
				}
				
			} else if(subReportTypeArr[i].equals("22")){//22������̨���岥��Ч��ͳ��2��
				LinkedHashMap map = statisticsReportCondition22(starttime, endtime, my_report_type,report_type, asobj, report_id);
				for(Object o:map.keySet()){
					descList.add(map.get(o));   // Map��ֵ
				}
				
			} else if(subReportTypeArr[i].equals("23")){//23������̨���岥��Ч��ͳ��3��
				LinkedHashMap map = statisticsReportCondition23(starttime, endtime, my_report_type,report_type, asobj, report_id);
				for(Object o:map.keySet()){
					descList.add(map.get(o));   // Map��ֵ
				}
				
			} else if(subReportTypeArr[i].equals("31")){//31���������岥��Ч��ͳ��1��
				LinkedHashMap map = statisticsReportCondition31(starttime, endtime, my_report_type,report_type, asobj, report_id);
				for(Object o:map.keySet()){
					descList.add(map.get(o));   // Map��ֵ
				}
				
			} else if(subReportTypeArr[i].equals("32")){//32���������岥��Ч��ͳ��2��
				LinkedHashMap map = statisticsReportCondition32(starttime, endtime, my_report_type,report_type, asobj, report_id);
				for(Object o:map.keySet()){
					descList.add(map.get(o));   // Map��ֵ
				}
				
			} else if(subReportTypeArr[i].equals("41")){//41���������������ޡ�������ͳ�ƣ�
				LinkedHashMap map = statisticsReportCondition41(starttime, endtime, my_report_type,report_type, asobj, report_id);
				for(Object o:map.keySet()){
					descList.add(map.get(o));   // Map��ֵ
				}
				
			} else if(subReportTypeArr[i].equals("51")){//51����ң��վ���������������ޡ��ɱ�֤����Ƶʱͳ�ƣ�
				LinkedHashMap map = statisticsReportCondition51(starttime, endtime, my_report_type,report_type, asobj, report_id);
				for(Object o:map.keySet()){
					descList.add(map.get(o));   // Map��ֵ
				}
			} else if(subReportTypeArr[i].equals("61")){//61�����¿����ʶԱȣ�
				LinkedHashMap map = statisticsReportCondition61(starttime, endtime, my_report_type,report_type, asobj, report_id);
				for(Object o:map.keySet()){
					descList.add(map.get(o));   // Map��ֵ
				}
				
			} else if(subReportTypeArr[i].equals("71")){//71��Ƶ��ƽ��������ͳ�Ʊ�
				LinkedHashMap map = statisticsReportCondition71(starttime, endtime, my_report_type,report_type, asobj, report_id);
				for(Object o:map.keySet()){
					descList.add(map.get(o));   // Map��ֵ
				}
			}
		}
		
//		ZrstRepEffectStatisticsBean detailBean = new ZrstRepEffectStatisticsBean();
		
		ReportBean reportBean = ReportUtil.getBasicStatisticsReportBean(report_id, starttime, endtime, report_type, user_name);

		boolean boo =ReportUtil.insertReportByReportBean(reportBean,  descList, "ZRST_REP_EFFECT_STATISTICS_TAB", "com.viewscenes.bean.report.ZrstRepEffectStatisticsBean");
        System.out.println("\r<br>ִ�к�ʱ : "+(System.currentTimeMillis()-a)/1000f+" �� ");
		return boo;
	}
	/**
	 * type: 1��������ʱ��ͳ�� 2������Сʱͳ�� 3����һСʱͳ��
	 * recordTime ¼���ļ���ʼʱ��
	 * @detail  
	 * @method  
	 * @param type
	 * @return 
	 * @return  String  
	 * @author  zhaoyahui
	 * @version 2013-6-3 ����10:37:00
	 */
	public String getPlayTime(String type,String recordTime){
		String play_time = "";
		String hour = recordTime.substring(11,13);
		if(type.equals("2")){
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
		} else if(type.equals("3")){
			if(hour.equals("23")){
				play_time = "23:00-00:00";
			} else{
				int otherHour = Integer.parseInt(hour)+1;
				String otherHourStr = otherHour+"";
				if(otherHour<10){
					otherHourStr = "0"+otherHour;
				}
				play_time = hour + ":00-"+otherHourStr+":00";
			}
		}
		return play_time;
	}
	/**
	 * ����̨�㲥Ч��ͳ�Ʊ� 
	 * @detail  
	 * @method  
	 * @param starttime
	 * @param endtime
	 * @param my_report_type
	 * @param report_type
	 * @param asobj
	 * @return
	 * @throws Exception 
	 * @return  HashMap  
	 * @author  zhaoyahui
	 * @version 2013-1-10 ����07:32:41
	 */
	public LinkedHashMap statisticsReportCondition11(String starttime, String endtime,String my_report_type,String report_type,ASObject asobj,String report_id) throws Exception{
		String headcodes = (String)asobj.get("headcodes");
		String rule = (String)asobj.get("rule");//1��������ʱ��ͳ�� 2������Сʱͳ�� 3����һСʱͳ��
//		String headcodes = null;//test
		String sql  = "";
		if(my_report_type.equals("1")){//����̨
			sql = "select runplan.runplan_id,runplan.start_time ||'-'|| runplan.end_time as play_time,lan.language_name, runplan.freq,station.name stationname,runplan.direction," +
						" runplan.power, head.code  headcode,head.service_name service_name_headend,head.type_id, mark.headname as shortname," +
						" mark.counto,runplan.service_area,runplan.valid_start_time ,runplan.valid_end_time,mark.start_datetime," +
						" dis.runplan_id as dis_runplan_id,dis.disturb,runplan.program_type  " +
						" from radio_mark_view_xiaohei mark," +
					//	" radio_stream_result_tab record," +
						" zres_runplan_chaifen_tab runplan," +
						" zdic_language_tab lan ," +
						" res_transmit_station_tab station," +
						" res_headend_tab head," +
						" (select * from zres_runplan_disturb_tab where  valid_end_time>to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss')) dis  "+
						//" zres_runplan_disturb_tab dis " +
								" where mark.runplan_id=runplan.runplan_id and mark.counto is not null and mark.counti is not null and mark.counts is not null and  runplan.language_id=lan.language_id " +
								" and runplan.station_id=station.station_id and head.code=mark.head_code" +
								" and mark.report_type=1 " +//Ч��¼��
							//	" and mark.is_delete=0 " +
								//" and runplan.is_delete=0 " +
								" and runplan.valid_end_time>=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') "+
								" and runplan.parent_id = dis.runplan_id(+) " +
								//" and dis.is_delete=0 " +
								" and runplan.runplan_type_id='"+my_report_type+"'"+
								" and mark.start_datetime>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss')" +
								" and mark.end_datetime<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') ";
			if(headcodes!=null&&!headcodes.equalsIgnoreCase("")){
				//sql+=" and '"+headcodes.substring(0, headcodes.length()-1)+"' like '%'||mark.HEAD_CODE||'%'  ";
				if(headcodes.split(",").length>1){
					String[] ss = headcodes.split(",");
					String newsql="";
					for(int m=0;m<ss.length;m++){
						newsql+=" mark.HEAD_CODE like '%"+ss[m]+"%' or";
					}
					sql+=" and ("+newsql.substring(0, newsql.length()-2)+")";
				}else{
					sql+=" and mark.HEAD_CODE like '%"+headcodes.substring(0, headcodes.length()-1)+"%'";
				}
				
			}
			
			sql+=" order by head.shortname,station.name,runplan.freq";
		} else if(my_report_type.equals("2")){//�������
			sql = "select runplan.runplan_id,runplan.start_time ||'-'|| runplan.end_time as play_time,lan.language_name, runplan.freq,runplan.direction,runplan.redisseminators," +
			" runplan.power, head.code  headcode,head.service_name service_name_headend,head.type_id, mark.HEADNAME shortname," +
			" mark.counto,runplan.service_area,runplan.valid_start_time ,runplan.valid_end_time,mark.start_datetime," +
			" dis.runplan_id as dis_runplan_id,dis.disturb,runplan.program_type  " +
			" from radio_mark_view_xiaohei mark," +
			//" radio_stream_result_tab record," +
			" zres_runplan_chaifen_tab runplan," +
			" zdic_language_tab lan ," +
			" res_headend_tab head," +
			" (select * from zres_runplan_disturb_tab where  valid_end_time>to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss')) dis  "+
			//" zres_runplan_disturb_tab dis " +
					" where mark.runplan_id=runplan.runplan_id and mark.counto is not null and mark.counti is not null and mark.counts is not null and  runplan.language_id=lan.language_id " +
					"  and head.code=mark.head_code" +
					" and mark.report_type=1 " +//Ч��¼��
				//	" and mark.is_delete=0 " +
					//" and runplan.is_delete=0 " +
					//" and runplan.valid_end_time>=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') "+
				
					" and runplan.parent_id = dis.runplan_id(+) " +
					//" and dis.is_delete=0 " +
					" and runplan.runplan_type_id='"+my_report_type+"'"+
					" and mark.start_datetime>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss')" +
					" and mark.end_datetime<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') ";
			if(headcodes!=null&&!headcodes.equalsIgnoreCase("")){
				//sql+=" and '"+headcodes.substring(0, headcodes.length()-1)+"' like '%'||mark.HEAD_CODE||'%'  ";
				if(headcodes.split(",").length>1){
					String[] ss = headcodes.split(",");
					String newsql="";
					for(int m=0;m<ss.length;m++){
						newsql+=" mark.HEAD_CODE like '%"+ss[m]+"%' or";
					}
					sql+=" and ("+newsql.substring(0, newsql.length()-2)+")";
				}else{
					//sql+="and '"+headcodes.substring(0, headcodes.length()-1)+"' like '%'||head.code||'%' ";
					sql+=" and mark.HEAD_CODE like '%"+headcodes.substring(0, headcodes.length()-1)+"%'";
				}
				
			}
			sql+=" order by head.shortname,runplan.freq";
		}
				 GDSet gd = DbComponent.Query(sql);
				 LinkedHashMap map = new LinkedHashMap();
				 for(int i=0;i<gd.getRowCount();i++){
					 String program_type = gd.getString(i, "program_type");//��Ŀ����
					//��ע��Ϣ��ͬ��Ƶ���������Ϣ����룬���ڿ����ʵ���60%δ����ͬ��Ƶ������Ϣ����Ҫ�ڱ�ע��Ϣ��ֱ�Ӽ����ź�����������
					 String dis_runplan_id = gd.getString(i, "dis_runplan_id");//������Ϣ������ͼid
					 String runplan_id = gd.getString(i, "runplan_id");//����ͼid
					 String disturb = gd.getString(i, "disturb");//������Ϣ
					 String play_time = gd.getString(i , "play_time");
					 String start_datetime = gd.getString(i, "start_datetime");
					 if(!rule.equals("1")){
						 play_time = getPlayTime(rule,start_datetime);
					 }
					 String language_name = gd.getString(i, "language_name");
					 String freq = gd.getString(i, "freq");
					 String valid_start_time = gd.getString(i, "valid_start_time");
					 String valid_end_time = gd.getString(i, "valid_end_time");
					 String stationname = "";
					 String runplan_type_id = "";//1������̨����ͼ2���������
					 if(my_report_type.equals("2")){
						 String redisseminators = gd.getString(i, "redisseminators");//ת������
						 if(redisseminators.equals("")){
							 redisseminators = " ";
						 }
						 stationname = redisseminators;
						 runplan_type_id = "2";
					 } else{
						 stationname = gd.getString(i, "stationname");
						 runplan_type_id = "1";
					 }
					 String direction = gd.getString(i, "direction");
					 String power = gd.getString(i, "power");
					 String type_id = gd.getString(i, "type_id");
					 String headcode = gd.getString(i, "headcode");
					 String shortname = gd.getString(i, "shortname");
					 if("102".equals(type_id) && ( headcode.endsWith("A") || headcode.endsWith("B")||headcode.endsWith("C") || headcode.endsWith("D")|| headcode.endsWith("E") || headcode.endsWith("F") || headcode.endsWith("G")  )){
						 headcode = headcode.substring(0, headcode.length()-1);
						// shortname = shortname.substring(0, shortname.length()-1);
					 }
					 if("102".equals(type_id) && ( shortname.endsWith("A") || shortname.endsWith("B")||shortname.endsWith("C") || shortname.endsWith("D") || shortname.endsWith("E")|| shortname.endsWith("F")|| shortname.endsWith("G"))){
						shortname = shortname.substring(0, shortname.length()-1);
					 }
					 if(headcodes == null || headcodes.equals("") || headcodes.indexOf(headcode+",")>=0){
					 } else{
						 continue;
					 }
					 String counto = gd.getString(i, "counto");
					 String service_area = gd.getString(i, "service_area");
					 if(service_area == null || service_area.equals("")){
						 throw new Exception(shortname+"�ķ�����Ϊ�գ���������");
					 }
					 int countoNum = Integer.parseInt(counto);
					 String[] service_areaArr = service_area.split(",");
					 for(int ai=0;ai<service_areaArr.length;ai++){
						 service_area = service_areaArr[ai];
						 //������Ҫ���Ǵ����������
						 String mapKey = headcode+play_time+language_name+freq+stationname+direction+power+service_area;
						 //String mapKey = headcode+play_time+language_name+freq+direction+power+service_area;
						 
						 ZrstRepEffectStatisticsBean allBean = (ZrstRepEffectStatisticsBean)map.get(headcode+"����Ч��");
						 if(allBean == null){
							 double fen0 = countoNum==0?1:0;
							 double fen1 = countoNum==1?1:0;
							 double fen2 = countoNum==2?1:0;
							 double fen3 = countoNum==3?1:0;
							 double fen4 = countoNum==4?1:0;
							 double fen5 = countoNum==5?1:0;
							 allBean = new ZrstRepEffectStatisticsBean();
							 allBean.setReceive_name(shortname);
							 allBean.setReceive_code(headcode);
							 allBean.setPlay_time("����Ч��");
							 allBean.setReceive_count("1");
							 allBean.setFen0(fen0+"");
							 allBean.setFen1(fen1+"");
							 allBean.setFen2(fen2+"");
							 allBean.setFen3(fen3+"");
							 allBean.setFen4(fen4+"");
							 allBean.setFen5(fen5+"");
							 allBean.setListen(new BigDecimal((fen3+fen4+fen5)*100/1).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
							 allBean.setListen_middle(countoNum+"");//��һ��
							 allBean.getListen_middleList().add(countoNum);
							 allBean.setBak("");
							 allBean.setSub_report_type("11");
							 allBean.setReport_type(my_report_type);
							 allBean.setReport_id(report_id);
							 map.put(headcode+"����Ч��", allBean);
						 } else{
							 int receive_count_all = Integer.parseInt(allBean.getReceive_count());
							 receive_count_all++;
							 allBean.setReceive_count(receive_count_all+"");
							 
							 double fen0_all = countoNum==0?Double.parseDouble(allBean.getFen0())+1:Double.parseDouble(allBean.getFen0());
							 double fen1_all = countoNum==1?Double.parseDouble(allBean.getFen1())+1:Double.parseDouble(allBean.getFen1());
							 double fen2_all = countoNum==2?Double.parseDouble(allBean.getFen2())+1:Double.parseDouble(allBean.getFen2());
							 double fen3_all = countoNum==3?Double.parseDouble(allBean.getFen3())+1:Double.parseDouble(allBean.getFen3());
							 double fen4_all = countoNum==4?Double.parseDouble(allBean.getFen4())+1:Double.parseDouble(allBean.getFen4());
							 double fen5_all = countoNum==5?Double.parseDouble(allBean.getFen5())+1:Double.parseDouble(allBean.getFen5());
							 allBean.setFen0(fen0_all+"");
							 allBean.setFen1(fen1_all+"");
							 allBean.setFen2(fen2_all+"");
							 allBean.setFen3(fen3_all+"");
							 allBean.setFen4(fen4_all+"");
							 allBean.setFen5(fen5_all+"");
							 allBean.setListen(new BigDecimal((fen3_all+fen4_all+fen5_all)*100/receive_count_all).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
							 allBean.getListen_middleList().add(countoNum);
							 ArrayList mList_all = allBean.getListen_middleList();
							 Collections.sort(mList_all);
							 int middleNum_all = (int) Math.ceil(mList_all.size()/2);
							 allBean.setListen_middle(mList_all.get(middleNum_all)+"");
							 allBean.setListen_middleList(mList_all);
							
						 }
						 map.put(headcode+"����Ч��", allBean);
						 
						 if(map.get(mapKey) == null){
							 ZrstRepEffectStatisticsBean detailBean = new ZrstRepEffectStatisticsBean();
							 detailBean.setPlay_time(play_time);
							 detailBean.setLanguage_name(language_name);
							 detailBean.setFreq(freq);
							 detailBean.setRunplan_id(runplan_id);
							 detailBean.setTransmit_station(stationname);
							 detailBean.setRunplan_type_id_temp(runplan_type_id);
							 detailBean.setTransmit_direction(direction);
							 detailBean.setTransmit_power(power);
							 detailBean.setService_area(service_area);
							 detailBean.setReceive_name(shortname);
							 detailBean.setReceive_code(headcode);
							 detailBean.setValid_start_time_temp(valid_start_time);
							 detailBean.setValid_end_time_temp(valid_end_time);
							 detailBean.setAll_listens(program_type);//��11������ ����ֶ���ʱ������Ž�Ŀ���� ��Ŀ���1:����ת��,2:����ֱ��,3:����ֱ��,4:���ڵط�
							 
							 double fen0 = countoNum==0?1:0;
							 double fen1 = countoNum==1?1:0;
							 double fen2 = countoNum==2?1:0;
							 double fen3 = countoNum==3?1:0;
							 double fen4 = countoNum==4?1:0;
							 double fen5 = countoNum==5?1:0;
							 detailBean.setReceive_count("1");
							 detailBean.setFen0(fen0+"");
							 detailBean.setFen1(fen1+"");
							 detailBean.setFen2(fen2+"");
							 detailBean.setFen3(fen3+"");
							 detailBean.setFen4(fen4+"");
							 detailBean.setFen5(fen5+"");
							 detailBean.setListen(new BigDecimal((fen3+fen4+fen5)*100/1).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
							 detailBean.setListen_middle(countoNum+"");//��һ��
							 detailBean.getListen_middleList().add(countoNum);
							 if(dis_runplan_id.equals("")){
								 if((fen3+fen4+fen5)*100/1<60){
									 disturb = "�ź�����������";
								 }
							 }
							 detailBean.setBak(disturb);
							 detailBean.setSub_report_type("11");
							 detailBean.setReport_type(my_report_type);
							 detailBean.setReport_id(report_id);
							 map.put(mapKey, detailBean);
							 
							 
						 } else{
							 ZrstRepEffectStatisticsBean detailBean = (ZrstRepEffectStatisticsBean)map.get(mapKey);
							 int receive_count = Integer.parseInt(detailBean.getReceive_count());
							 receive_count++;
							 detailBean.setReceive_count(receive_count+"");
							 
							 double fen0 = countoNum==0?Double.parseDouble(detailBean.getFen0())+1:Double.parseDouble(detailBean.getFen0());
							 double fen1 = countoNum==1?Double.parseDouble(detailBean.getFen1())+1:Double.parseDouble(detailBean.getFen1());
							 double fen2 = countoNum==2?Double.parseDouble(detailBean.getFen2())+1:Double.parseDouble(detailBean.getFen2());
							 double fen3 = countoNum==3?Double.parseDouble(detailBean.getFen3())+1:Double.parseDouble(detailBean.getFen3());
							 double fen4 = countoNum==4?Double.parseDouble(detailBean.getFen4())+1:Double.parseDouble(detailBean.getFen4());
							 double fen5 = countoNum==5?Double.parseDouble(detailBean.getFen5())+1:Double.parseDouble(detailBean.getFen5());
							 detailBean.setFen0(fen0+"");
							 detailBean.setFen1(fen1+"");
							 detailBean.setFen2(fen2+"");
							 detailBean.setFen3(fen3+"");
							 detailBean.setFen4(fen4+"");
							 detailBean.setFen5(fen5+"");
							 detailBean.setListen(new BigDecimal((fen3+fen4+fen5)*100/receive_count).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
							 if(dis_runplan_id.equals("")){
								 if((fen3+fen4+fen5)*100/receive_count<60){
									 disturb = "�ź�����������";
								 }else disturb = "";
							 }
							 detailBean.getListen_middleList().add(countoNum);
							 ArrayList mList = detailBean.getListen_middleList();
							 Collections.sort(mList);
							 int middleNum = (int) Math.ceil(mList.size()/2);
							 detailBean.setBak(disturb);
							 detailBean.setListen_middle(mList.get(middleNum)+"");
							 detailBean.setListen_middleList(mList);
							 map.put(mapKey, detailBean);
							 
							 
						 }
						 
						
					 }
				 }
//				//��ע��Ϣ��ͬ��Ƶ���������Ϣ����룬���ڿ����ʵ���60%δ����ͬ��Ƶ������Ϣ����Ҫ�ڱ�ע��Ϣ��ֱ�Ӽ����ź�����������
//				for(Object o:map.keySet()){
//					ZrstRepEffectStatisticsBean detailBean = (ZrstRepEffectStatisticsBean)map.get(o);
//					if(detailBean.getPlay_time().equals("����Ч��")){
//						continue;
//					}
//					//����Ƶ�ʡ�����ʱ�䡢��Ч��ʼʱ�䡢��Ч����ʱ�䡢����̨(ת������) 
//					String sqlsome = "select disturb from zres_runplan_disturb_tab where " +
//									" freq='"+detailBean.getFreq()+"'" +
//									" and language='"+detailBean.getLanguage_name()+"' " +
//									" and start_time || '-' || end_time='"+detailBean.getPlay_time()+"'" +
//									" and valid_start_time='"+detailBean.getValid_start_time_temp()+"' " +
//									" and valid_end_time='"+detailBean.getValid_end_time_temp()+"' " +
//									" and is_delete=0";
//					String subSql = "";
//					if(detailBean.getRunplan_type_id_temp().equals("1")){
//						subSql = " and station_name = '"+detailBean.getTransmit_station()+"'";
//					} else if(detailBean.getRunplan_type_id_temp().equals("2")){
//						subSql = " and redisseminators = '"+detailBean.getTransmit_station()+"'";
//					}
//					sqlsome += subSql;
//					String disturb = "";
//					 GDSet gds = DbComponent.Query(sqlsome);
//					 if(gds.getRowCount()>0){
//						 disturb = gds.getString(0, "disturb");
//						 detailBean.setBak(disturb);
//					 } else{
//						 if(Integer.parseInt(detailBean.getListen())<60){
//							 detailBean.setBak("�ź�����������");
//						 }
//					 }
//					 map.put(o, detailBean);
//				}

		return map;
	}
	
	/**
	 * ����̨���岥��Ч��ͳ��1
	 * @detail  
	 * @method  
	 * @param starttime
	 * @param endtime
	 * @param my_report_type
	 * @param report_type
	 * @param asobj
	 * @return
	 * @throws Exception 
	 * @return  HashMap  
	 * @author  zhaoyahui
	 * @version 2013-1-10 ����07:32:41
	 */
	public LinkedHashMap statisticsReportCondition21(String starttime, String endtime,String my_report_type,String report_type,ASObject asobj,String report_id) throws Exception{
		String headcodes = (String)asobj.get("headcodes");
//		String headcodes = null;//test
		String sql  = "";
		if(my_report_type.equals("1")){//����̨
			sql = "select  " +
					"  head.code  headcode,head.service_name service_name_headend,head.type_id, mark.headname as shortname,mark.counto,runplan.service_area,state.state_name,station.name stationname,runplan.redisseminators " +
					" from radio_mark_view_xiaohei mark," +
				//	" radio_stream_result_tab record," +
					" zres_runplan_chaifen_tab runplan," +
					" res_headend_tab head," +
					" res_transmit_station_tab station," +
					" dic_state_tab            state" +
							" where mark.runplan_id=runplan.runplan_id  and mark.counto is not null and mark.counti is not null and mark.counts is not null " +
							" and runplan.station_id=station.station_id and head.code=mark.head_code" +
							" and mark.report_type=1 " +//Ч��¼��
						//	" and mark.is_delete=0 " +
							//" and runplan.is_delete=0 " +
							" and runplan.valid_end_time>=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') "+
							" and runplan.runplan_type_id='"+my_report_type+"'"+
							" and state.state = head.state" +
							" and mark.start_datetime>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss')" +
							" and mark.end_datetime<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') order by head.code,stationname ";
		} else if(my_report_type.equals("2")){//�������
			sql = "select  " +
			"  head.code  headcode,head.service_name service_name_headend,head.type_id, mark.headname as shortname,mark.counto,runplan.service_area,state.state_name,runplan.redisseminators " +
			" from radio_mark_view_xiaohei mark," +
			//" radio_stream_result_tab record," +
			" zres_runplan_chaifen_tab runplan," +
			" res_headend_tab head," +
			" dic_state_tab            state" +
					" where mark.runplan_id=runplan.runplan_id and mark.counto is not null and mark.counti is not null and mark.counts is not null " +
					" and head.code=mark.head_code" +
					" and mark.report_type=1 " +//Ч��¼��
					//" and runplan.is_delete=0 " +
					//" and runplan.valid_end_time>=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') "+
					
					//" and record.is_delete=0 " +
					" and runplan.runplan_type_id='"+my_report_type+"'"+
					" and state.state = head.state" +
					" and mark.start_datetime>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss')" +
					" and mark.end_datetime<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') order by head.code ";
		}
				 GDSet gd = DbComponent.Query(sql);
				 
//				 //�ȰѴ˴β�ѯ��ȫ������̨�����
//				 sql = "select  " +
//					"  station.name stationname" +
//					" from RADIO_MARK_ZST_VIEW_TAB mark," +
//					" radio_stream_result_tab record," +
//					" zres_runplan_chaifen_tab runplan," +
//					" res_headend_tab head," +
//					" res_transmit_station_tab station," +
//					" dic_state_tab            state" +
//							" where mark.mark_file_url=record.url and mark.runplan_id=runplan.runplan_id  " +
//							" and runplan.station_id=station.station_id and head.head_id=record.head_id" +
//							" and record.report_type=0 " +//Ч��¼��
//							" and state.state = head.state" +
//							" and record.start_datetime>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss')" +
//							" and end_datetime<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss')";
//				 
//				 GDSet gdstation = DbComponent.Query(sql);
//				 ArrayList<String> listAllStaion = new ArrayList<String>();
//				 for(int ii=0;ii<gdstation.getRowCount();ii++){
//					 listAllStaion.add(gdstation.getString(ii, "stationname"));
//				 }
				 LinkedHashMap<String,String> mapHead = new LinkedHashMap<String,String>();
				 LinkedHashMap<String,String> mapState = new LinkedHashMap<String,String>();
				 LinkedHashMap<String,String> mapArea = new LinkedHashMap<String,String>();
				 LinkedHashMap<String,String> mapTotal = new LinkedHashMap<String,String>();
				 for(int i=0;i<gd.getRowCount();i++){
					 String service_area = gd.getString(i, "service_name_headend");
					 String state_name = gd.getString(i, "state_name");
					 String stationname = "";
					 
					 if(my_report_type.equals("2")){
						 String redisseminators = gd.getString(i, "redisseminators");//ת������
						 if(redisseminators.equals("")){
							 redisseminators = " ";
						 }
						 stationname = redisseminators;
					 } else{
						 stationname = gd.getString(i, "stationname");
					 }

					 String type_id = gd.getString(i, "type_id");
					 String headcode = gd.getString(i, "headcode");
					 String shortname = gd.getString(i, "shortname");
					 if(service_area == null || service_area.equals("")){
						 throw new Exception(shortname+"�ķ�����Ϊ�գ���������");
					 }
					 if("102".equals(type_id) && ( headcode.endsWith("A") || headcode.endsWith("B")||headcode.endsWith("C") || headcode.endsWith("D")|| headcode.endsWith("E") || headcode.endsWith("F") || headcode.endsWith("G")  )){
								 headcode = headcode.substring(0, headcode.length()-1);
						// shortname = shortname.substring(0, shortname.length()-1);
					 }
					 if("102".equals(type_id) && ( shortname.endsWith("A") || shortname.endsWith("B")||shortname.endsWith("C") || shortname.endsWith("D") || shortname.endsWith("E")|| shortname.endsWith("F")|| shortname.endsWith("G"))){
								shortname = shortname.substring(0, shortname.length()-1);
					 }
					 if(headcodes == null || headcodes.equals("") || headcodes.indexOf(headcode+",")>=0){
					 } else{
						 continue;
					 }
					 String counto = gd.getString(i, "counto");
					 int countoNum = Integer.parseInt(counto);
					 String transmit_station_listens = "";//����̨_>=3��_�ܴ���_������%  ���磺2022_88_123_78,2032_23_423_28
					 if(mapHead.get(shortname) == null){
						 int big3Count = (countoNum>=3?1:0);
						 String needStr = stationname+"_"+big3Count+"_1_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+",";
//						 for(String tempStation:listAllStaion){
//							 
//							 if(stationname.equals(tempStation)){
//								 needStr += stationname+"_"+big3Count+"_1_"+(big3Count*100/1)+",";
//							 } else{
//								 needStr += stationname+"___,";
//							 }
//						 }
						 mapHead.put(shortname, needStr);
//						 mapHead.put(shortname, needStr + stationname+"A_6_6_6,");//test
					 } else{
						 String newHeadValue = "";
						 if((mapHead.get(shortname)).indexOf(stationname+"_")<0){
							 newHeadValue = mapHead.get(shortname)+stationname+"_0_0_0,";
							 mapHead.put(shortname, newHeadValue);
							 newHeadValue = "";
						 }
						 String[] headStationArr = (mapHead.get(shortname)).split(",");
						 for(String headValue:headStationArr){
							 String[] headValueArr = headValue.split("_");
//							 if(headValue.indexOf("___")>0){
//								 newHeadValue += headValue;
//							 } else{
								 if(headValueArr[0].equals(stationname)){
									 int big3Count = (countoNum>=3?1:0)+Integer.parseInt(headValueArr[1]);
									 int totalCount = Integer.parseInt(headValueArr[2])+1;
									 newHeadValue += stationname+"_"+big3Count+"_"+totalCount+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+",";
								 } else{
									 int big3Count = (countoNum>=3?1:0);
									 if((mapHead.get(shortname)).indexOf(stationname+"_")>-1){//������������̨���˴ΰ����ݴ��·Ż�ȥ
										 newHeadValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
									 } else{//���û�У������µ�
									 }
								 }
//							 }
						 }
						 mapHead.put(shortname, newHeadValue);
					 }
					 String[] service_areaArr = service_area.split(",");
					 for(int ai=0;ai<service_areaArr.length;ai++){
						 service_area = service_areaArr[ai];
						 if(mapArea.get(service_area) == null){//����
							 int big3Count = (countoNum>=3?1:0);
							 mapArea.put(service_area, stationname+"_"+big3Count+"_1_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+",");
						 } else{
							 String newHeadValue = "";
							 if((mapArea.get(service_area)).indexOf(stationname+"_")<0){
								 newHeadValue = mapArea.get(service_area)+stationname+"_0_0_0,";
								 mapArea.put(service_area, newHeadValue);
								 newHeadValue = "";
							 }
							 String[] headStationArr = (mapArea.get(service_area)).split(",");
							 for(String headValue:headStationArr){
								 String[] headValueArr = headValue.split("_");
	//							 if(headValue.indexOf("___")>0){
	//								 newHeadValue = headValue;
	//							 } else{
									 if(headValueArr[0].equals(stationname)){ 
										 int big3Count = (countoNum>=3?1:0)+Integer.parseInt(headValueArr[1]);
										 int totalCount = Integer.parseInt(headValueArr[2])+1;
										 newHeadValue += stationname+"_"+big3Count+"_"+totalCount+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+",";
									 } else{
										 int big3Count = (countoNum>=3?1:0);
										 if((mapArea.get(service_area)).indexOf(stationname+"_")>-1){
											 newHeadValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
										 } else{
										 }
									 }
	//							 }
							 }
							 mapArea.put(service_area, newHeadValue);
						 }
					 }
					 if(mapState.get(state_name) == null){//����
						 int big3Count = (countoNum>=3?1:0);
						 mapState.put(state_name, stationname+"_"+big3Count+"_1_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+",");
					 } else{
						 String newHeadValue = "";
						 if((mapState.get(state_name)).indexOf(stationname+"_")<0){
							 newHeadValue = mapState.get(state_name)+stationname+"_0_0_0,";
							 mapState.put(state_name, newHeadValue);
							 newHeadValue = "";
						 }
						 String[] headStationArr = (mapState.get(state_name)).split(",");
						 for(String headValue:headStationArr){
							 String[] headValueArr = headValue.split("_");
//							 if(headValue.indexOf("___")>0){
//								 newHeadValue = headValue;
//							 } else{
								 if(headValueArr[0].equals(stationname)){
									 int big3Count = (countoNum>=3?1:0)+Integer.parseInt(headValueArr[1]);
									 int totalCount = Integer.parseInt(headValueArr[2])+1;
									 newHeadValue += stationname+"_"+big3Count+"_"+totalCount+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+",";
								 } else{
									 int big3Count = (countoNum>=3?1:0);
									 if((mapState.get(state_name)).indexOf(stationname+"_")>-1){
										 newHeadValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
									 } else{
									 }
								 }
							 }
//						 }
						 mapState.put(state_name, newHeadValue);
					 }
					 
					 if(mapTotal.get("ȫ��") == null){//ȫ��
						 int big3Count = (countoNum>=3?1:0);
						 mapTotal.put("ȫ��", stationname+"_"+big3Count+"_1_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+",");
					 } else{
						 String newHeadValue = "";
						 if((mapTotal.get("ȫ��")).indexOf(stationname+"_")<0){
							 newHeadValue = mapTotal.get("ȫ��")+stationname+"_0_0_0,";
							 mapTotal.put("ȫ��", newHeadValue);
							 newHeadValue = "";
						 }
						 String[] headStationArr = (mapTotal.get("ȫ��")).split(",");
						 for(String headValue:headStationArr){
							 String[] headValueArr = headValue.split("_");
//							 if(headValue.indexOf("___")>0){
//								 newHeadValue = headValue;
//							 } else{
								 if(headValueArr[0].equals(stationname)){
									 int big3Count = (countoNum>=3?1:0)+Integer.parseInt(headValueArr[1]);
									 int totalCount = Integer.parseInt(headValueArr[2])+1;
									 newHeadValue += stationname+"_"+big3Count+"_"+totalCount+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+",";
								 } else{
									 int big3Count = (countoNum>=3?1:0);
									 if((mapTotal.get("ȫ��")).indexOf(stationname+"_")>-1){
										 newHeadValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
									 } else{
									 }
								 }
//							 }
						 }
						 mapTotal.put("ȫ��", newHeadValue);
					 }
				 }
				 
		LinkedHashMap<String,ZrstRepEffectStatisticsBean> map = new LinkedHashMap<String,ZrstRepEffectStatisticsBean>();
		//����̨_>=3��_�ܴ���_������%  ���磺2022_88_123_78,2032_23_423_28
		for(Object o:mapHead.keySet()){
			String val = mapHead.get(o);
			ZrstRepEffectStatisticsBean  bean = new ZrstRepEffectStatisticsBean();
			bean.setReceive_name(o.toString());
			bean.setTransmit_station_listens(val.substring(0, val.length()-1));
			bean.setSub_report_type("21");
			bean.setReport_type(my_report_type);
			bean.setReport_id(report_id);
			map.put((String)o, bean);
			
		}
		for(Object o:mapArea.keySet()){
			String val = mapArea.get(o);
			ZrstRepEffectStatisticsBean  bean = new ZrstRepEffectStatisticsBean();
			bean.setService_area(o.toString());
			bean.setTransmit_station_listens(val.substring(0, val.length()-1));
			bean.setSub_report_type("21");
			bean.setReport_type(my_report_type);
			bean.setReport_id(report_id);
			map.put((String)o, bean);
		}
		for(Object o:mapState.keySet()){
			String val = mapState.get(o);
			ZrstRepEffectStatisticsBean  bean = new ZrstRepEffectStatisticsBean();
			bean.setState_name(o.toString());
			bean.setTransmit_station_listens(val.substring(0, val.length()-1));
			bean.setSub_report_type("21");
			bean.setReport_type(my_report_type);
			bean.setReport_id(report_id);
			map.put((String)o, bean);
		}
		for(Object o:mapTotal.keySet()){
			String val = mapTotal.get(o);
			ZrstRepEffectStatisticsBean  bean = new ZrstRepEffectStatisticsBean();
			bean.setTransmit_station_listens(val.substring(0, val.length()-1));
			bean.setSub_report_type("21");
			bean.setReport_type(my_report_type);
			bean.setReport_id(report_id);
			map.put((String)o, bean);
		}
		return map;
	}
	/**
	 * ����̨���岥��Ч��ͳ��2
	 * @detail  
	 * @method  
	 * @param starttime
	 * @param endtime
	 * @param my_report_type
	 * @param report_type
	 * @param asobj
	 * @return
	 * @throws Exception 
	 * @return  HashMap  
	 * @author  zhaoyahui
	 * @version 2013-1-10 ����07:32:41
	 */
	public LinkedHashMap statisticsReportCondition22(String starttime, String endtime,String my_report_type,String report_type,ASObject asobj,String report_id) throws Exception{
		LinkedHashMap<String,ZrstRepEffectStatisticsBean> map = new LinkedHashMap<String,ZrstRepEffectStatisticsBean>();
		String headcodes = (String)asobj.get("headcodes");
		String sql  = "";
		String order="";
		if(my_report_type.equals("1")){//����̨
			order=" order by head.code,stationname ";
				sql = "select  " +
						"  head.code  headcode,head.service_name service_name_headend,head.type_id, mark.headname as shortname,mark.counto,runplan.service_area,state.state_name,station.name stationname,runplan.redisseminators" +
						" from radio_mark_view_xiaohei mark," +
						//" radio_stream_result_tab record," +
						" zres_runplan_chaifen_tab runplan," +
						" res_headend_tab head," +
						" res_transmit_station_tab station," +
						" dic_state_tab            state" +
								" where mark.runplan_id=runplan.runplan_id and mark.counto is not null and mark.counti is not null and mark.counts is not null " +
								" and runplan.station_id=station.station_id and head.code=mark.head_code" +
								" and mark.report_type=1 " +//Ч��¼��
								//" and record.is_delete=0 " +
								//" and runplan.is_delete=0 " +
								" and runplan.valid_end_time>=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') "+
								
								" and runplan.runplan_type_id='"+my_report_type+"'"+
								" and state.state = head.state" +
								" and mark.start_datetime>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss')" +
								" and mark.end_datetime<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') ";
		} else if(my_report_type.equals("2")){//�������
			order=" order by head.code,redisseminators ";
			sql = "select  " +
			"  head.code  headcode,head.service_name service_name_headend,head.type_id, mark.headname as shortname,mark.counto,runplan.service_area,state.state_name,runplan.redisseminators" +
			" from radio_mark_view_xiaohei mark," +
			//" radio_stream_result_tab record," +
			" zres_runplan_chaifen_tab runplan," +
			" res_headend_tab head," +
			" dic_state_tab            state" +
					" where mark.runplan_id=runplan.runplan_id and mark.counto is not null and mark.counti is not null and mark.counts is not null " +
					" and head.code=mark.head_code" +
					" and mark.report_type=1 " +//Ч��¼��
					//" and runplan.is_delete=0 " +
					//" and runplan.valid_end_time>=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') "+
					
				//	" and record.is_delete=0 " +
					" and runplan.runplan_type_id='"+my_report_type+"'"+
					" and state.state = head.state" +
					" and mark.start_datetime>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss')" +
					" and mark.end_datetime<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') ";
		}
		if(headcodes!=null&&!headcodes.equalsIgnoreCase("")){
		//	sql+=" and '"+headcodes.substring(0, headcodes.length()-1)+"' like '%'||head.code||'%' ";
			if(headcodes.split(",").length>1){
				String[] ss = headcodes.split(",");
				String newsql="";
				for(int m=0;m<ss.length;m++){
					newsql+=" head.code like '%"+ss[m]+"%' or";
				}
				sql+=" and ("+newsql.substring(0, newsql.length()-2)+")";
			}else{
				//sql+="and '"+headcodes.substring(0, headcodes.length()-1)+"' like '%'||head.code||'%' ";
				sql+=" and head.code like '%"+headcodes.substring(0, headcodes.length()-1)+"%'";
			}
			
		}
		sql+=order;
				 GDSet gd = DbComponent.Query(sql);

				 LinkedHashMap<String,String> mapStation = new LinkedHashMap<String,String>();
				 LinkedHashMap<String,String> mapState = new LinkedHashMap<String,String>();
				 LinkedHashMap<String,String> mapArea = new LinkedHashMap<String,String>();
				 LinkedHashMap<String,String> mapTotal = new LinkedHashMap<String,String>();
				 String allHead = "";
				 String allArea = "";
				 String allState = "";
				 for(int i=0;i<gd.getRowCount();i++){
					 String type_id = gd.getString(i, "type_id");
					 String headcode = gd.getString(i, "headcode");
					 String shortname = gd.getString(i, "shortname"); 
					 String service_area = gd.getString(i, "service_name_headend");
					 if(service_area == null || service_area.equals("")){
						 throw new Exception(shortname+"�ķ�����Ϊ�գ���������");
					 }
					 String state_name = gd.getString(i, "state_name");
					 if("102".equals(type_id) && ( headcode.endsWith("A") || headcode.endsWith("B")||headcode.endsWith("C") || headcode.endsWith("D")|| headcode.endsWith("E") || headcode.endsWith("F") || headcode.endsWith("G")  )){
								 headcode = headcode.substring(0, headcode.length()-1);
						// shortname = shortname.substring(0, shortname.length()-1);
					 }
					 if("102".equals(type_id) && ( shortname.endsWith("A") || shortname.endsWith("B")||shortname.endsWith("C") || shortname.endsWith("D") || shortname.endsWith("E")|| shortname.endsWith("F")|| shortname.endsWith("G"))){
								shortname = shortname.substring(0, shortname.length()-1);
					 }
					 //�ղ�վ��_������_����3�ִ���_�ܴ���
					 if(allHead.indexOf(shortname+"_A_A_A,")<0){
						 allHead += shortname+"_A_A_A,";
					 }
					 if(allArea.indexOf(","+service_area+"_A_A_A,")<0){
						 allArea += ","+service_area+"_A_A_A";
					 }
					
					 if(allState.indexOf(state_name+"_A_A_A,")<0){
						 allState += state_name+"_A_A_A,";
					 }
				 }
			
				if( allArea.length()>1)
				{
				 allArea=allArea.substring(1, allArea.length())+",";
				}
				 for(int i=0;i<gd.getRowCount();i++){
					 String service_area = gd.getString(i, "service_name_headend");
					 String state_name = gd.getString(i, "state_name");
					 String stationname = "";
					 
					 if(my_report_type.equals("2")){
						 String redisseminators = gd.getString(i, "redisseminators");//ת������
						 if(redisseminators.equals("")){
							 redisseminators = " ";
						 }
						 stationname = redisseminators;
					 } else{
						 stationname = gd.getString(i, "stationname");
					 }

					 String type_id = gd.getString(i, "type_id");
					 String headcode = gd.getString(i, "headcode");
					 String shortname = gd.getString(i, "shortname");
					 if("102".equals(type_id) && ( headcode.endsWith("A") || headcode.endsWith("B")||headcode.endsWith("C") || headcode.endsWith("D")|| headcode.endsWith("E") || headcode.endsWith("F") || headcode.endsWith("G")  )){
								 headcode = headcode.substring(0, headcode.length()-1);
						// shortname = shortname.substring(0, shortname.length()-1);
					 }
					 if("102".equals(type_id) && ( shortname.endsWith("A") || shortname.endsWith("B")||shortname.endsWith("C") || shortname.endsWith("D") || shortname.endsWith("E")|| shortname.endsWith("F")|| shortname.endsWith("G"))){
								shortname = shortname.substring(0, shortname.length()-1);
					 }
					 if(headcodes == null || headcodes.equals("") || headcodes.indexOf(headcode+",")>=0){
					 } else{
						 continue;
					 }
					 String counto = gd.getString(i, "counto");
					 int countoNum = Integer.parseInt(counto);
//					 String needStr = stationname+"_"+big3Count+"_1_"+(big3Count*100/1)+",";
					 if(mapStation.get(stationname) == null){
						 String needStr = allHead;
						 mapStation.put(stationname, needStr);
					 } else{
						 
					 }
					 //�ղ�վ��_������_����3�ִ���_�ܴ���
					 String newHeadendValue = "";
					 String[] headendStationArr = (mapStation.get(stationname)).split(",");
					 for(String headValue:headendStationArr){
						 String[] headValueArr = headValue.split("_");
						 int big3Count = (countoNum>=3?1:0)+ Integer.parseInt(  (headValueArr[2].equals("A")?"0":headValueArr[2]) );
						 int totalCount = 1 + Integer.parseInt(  (headValueArr[3].equals("A")?"0":headValueArr[3]) );
						 if(headValueArr[0].equals(shortname)){
							 newHeadendValue += shortname+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+"_"+big3Count+"_"+totalCount+",";
						 } else{
							 if((mapStation.get(stationname)).indexOf(shortname+"_")>-1){
								 newHeadendValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
							 } else{
								 newHeadendValue += shortname+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+"_"+big3Count+"_1,";
								 
							 }
						 }
					 }
					 mapStation.put(stationname, newHeadendValue);
					 
					//������޵���������
					 if(mapState.get(stationname) == null){
						 String needStr = allState;
						 mapState.put(stationname, needStr);
					 } else{
					 }
					 String newSatateValue = "";
					 String[] stateStationArr = (mapState.get(stationname)).split(",");
					 for(String headValue:stateStationArr){
						 String[] headValueArr = headValue.split("_");
						 int big3Count = (countoNum>=3?1:0)+ Integer.parseInt(  (headValueArr[2].equals("A")?"0":headValueArr[2]) );
						 int totalCount = 1 + Integer.parseInt(  (headValueArr[3].equals("A")?"0":headValueArr[3]) );
						 if(headValueArr[0].equals(state_name)){
							 newSatateValue += state_name+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+"_"+big3Count+"_"+totalCount+",";
						 } else{
							 if((mapState.get(stationname)).indexOf(state_name+"_")>-1){
								 newSatateValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
							 } else{
								 newSatateValue += state_name+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+"_"+big3Count+"_1,";
								 
							 }
						 }
					 }
					 mapState.put(stationname, newSatateValue);
					//�����������������
					 String[] service_areaArr = service_area.split(",");
					 for(int ai=0;ai<service_areaArr.length;ai++){
						 service_area = service_areaArr[ai];
						 if(mapArea.get(stationname) == null){
							 String needStr = allArea;
								 
							 mapArea.put(stationname, needStr);
						 } else{
						 }
						 String newAreaValue = "";
						 String[] areaStationArr = (mapArea.get(stationname)).split(",");
						 for(String headValue:areaStationArr){
							 String[] headValueArr = headValue.split("_");
							 int big3Count = (countoNum>=3?1:0)+ Integer.parseInt(  (headValueArr[2].equals("A")?"0":headValueArr[2]) );
							 int totalCount = 1 + Integer.parseInt(  (headValueArr[3].equals("A")?"0":headValueArr[3]) );
							 if(headValueArr[0].equals(service_area)){
								 newAreaValue += service_area+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+"_"+big3Count+"_"+totalCount+",";
							 } else{
								 if((mapArea.get(stationname)).indexOf(service_area+"_")>-1){
									 newAreaValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
								 } else{
									 newAreaValue += service_area+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+"_"+big3Count+"_1,";
									 
								 }
							 }
						 }
						 
						 mapArea.put(stationname, newAreaValue);
					 }
					//����ȫ������������
					 int big3Count = (countoNum>=3?1:0);
					 if(mapTotal.get(stationname) == null){
						 String needStr = "ȫ��_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+"_"+big3Count+"_1";
						 mapTotal.put(stationname, needStr);
					 } else{
						 int big3CountT = (countoNum>=3?1:0)+ Integer.parseInt(mapTotal.get(stationname).split("_")[2]);
						 int totalCountT = 1 + Integer.parseInt(mapTotal.get(stationname).split("_")[3]);
								
						 mapTotal.put(stationname, "ȫ��_"+new BigDecimal((Double.parseDouble(big3CountT+"")*100/totalCountT)).setScale(1,BigDecimal.ROUND_HALF_UP)+"_"+big3CountT+"_"+totalCountT);
					 }
					 
					 
				 }
		boolean temp = false;
		for(String o:mapStation.keySet()){
			String val = mapStation.get(o);
			ZrstRepEffectStatisticsBean  bean = new ZrstRepEffectStatisticsBean();
			bean.setTransmit_station(o.toString());
			String traVal = val;
			traVal +=  mapArea.get(o);
			traVal +=  mapState.get(o);
			traVal +=  mapTotal.get(o);
//			if(traVal != null && traVal.length()>0){
//				traVal = traVal.substring(0,traVal.length()-1);
//			}
//			if(traVal.indexOf("$")>0){
//				String[] allarr = traVal.split("$");
//				if(allarr[1].indexOf(mapArea.get(o))<0){
//					allarr[1] += mapArea.get(o);
//				}
//				if(allarr[2].indexOf(mapState.get(o))<0){
//					allarr[2] += mapState.get(o);
//				}
////				if(allarr[3].indexOf(mapTotal.get(o))<0){
////					allarr[3] += mapTotal.get(o);
////				}
//				traVal = "$" + allarr[0] + "$" + allarr[1] + "$" + allarr[2] + "$" + allarr[3];
//			} else{//��һ��
//				traVal += "$" + mapArea.get(o);
//				traVal += "$" + mapState.get(o);
//				traVal += "$" + mapTotal.get(o);
//			}
			bean.setReceive_name_total_hours(traVal);
			bean.setSub_report_type("22");
			bean.setReport_type(my_report_type);
			bean.setReport_id(report_id);
			map.put((String)o, bean);
			temp = true;
		}
		return map;
	}
	/**
	 * ����̨���岥��Ч��ͳ��3
	 * @detail  
	 * @method  
	 * @param starttime
	 * @param endtime
	 * @param my_report_type
	 * @param report_type
	 * @param asobj
	 * @return
	 * @throws Exception 
	 * @return  HashMap  
	 * @author  zhaoyahui
	 * @version 2013-1-10 ����07:32:41
	 */
	public LinkedHashMap statisticsReportCondition23(String starttime, String endtime,String my_report_type,String report_type,ASObject asobj,String report_id) throws Exception{
		asobj.put("rule", "3");
		LinkedHashMap<String,double[]> mapStation = new LinkedHashMap<String,double[]>();
		LinkedHashMap map = statisticsReportCondition11(starttime, endtime, my_report_type,report_type, asobj, report_id);
		LinkedHashMap<String,double[]> mapProgram = new LinkedHashMap<String,double[]>();
		for(Object o:map.keySet()){
			 ZrstRepEffectStatisticsBean bean = (ZrstRepEffectStatisticsBean)map.get(o);   // Map��ֵ
			 String stationname = bean.getTransmit_station();
			 if(stationname.equals("")){
				 continue;
			 }
			 int listionRate = Integer.parseInt(bean.getListen());
			 double timeLengthVal = Integer.parseInt(bean.getReceive_count())*0.5;
			 double[] vals = {0,0,0,0};
			 double[] vals1 = {0,0,0,0};
			 if(mapStation.get(stationname) == null){
				 
			 } else{
				 vals =  mapStation.get(stationname);
			 }
			 if(listionRate>=80){//Ч���ǿɱ�֤����
				 vals[0] += timeLengthVal;
			 } else if(listionRate>=60 && listionRate<80){//Ч���ǻ���������
				 vals[1] += timeLengthVal;
			 } else if(listionRate>=30 && listionRate<60){//Ч������ʱ������
				 vals[2] += timeLengthVal;
			 } else if(listionRate<30){//Ч���ǲ�������
				 vals[3] += timeLengthVal;
//				 LogTool.fatal(stationname+">>>>>>>>>>>>>>>>>>>>>>>>>>>>"+bean.getListen()+"<<<<<<<<<<<<<<<<<<<"+timeLengthVal);
			 }
			 mapStation.put(stationname, vals);
			 
			 
			 String program_type = bean.getAll_listens();
			 if(program_type.equals("")){
				 continue;
			 } else if("1".equals(program_type)){
				 program_type = "����ת��";
			 } else if("2".equals(program_type)){
				 program_type = "����ֱ��";
			 } else if("3".equals(program_type)){
				 program_type = "����ֱ��";
			 } else if("4".equals(program_type)){
				 program_type = "���ڵط�";
			 }
			 listionRate = Integer.parseInt(bean.getListen());
			 timeLengthVal = Integer.parseInt(bean.getReceive_count())*0.5;
			// vals[0] = vals[1] = vals[2] = vals[3] = 0;
			 if(mapProgram.get(program_type) == null){
				 
			 } else{
				 vals1 =  mapProgram.get(program_type);
			 }
			 if(listionRate>=80){//Ч���ǿɱ�֤����
				 vals1[0] += timeLengthVal;
			 } else if(listionRate>=60 && listionRate<80){//Ч���ǻ���������
				 vals1[1] += timeLengthVal;
			 } else if(listionRate>=30 && listionRate<60){//Ч������ʱ������
				 vals1[2] += timeLengthVal;
			 } else if(listionRate<30){//Ч���ǲ�������
				 vals1[3] += timeLengthVal;
//				 LogTool.fatal(stationname+">>>>>>>>>>>>>>>>>>>>>>>>>>>>"+bean.getListen()+"<<<<<<<<<<<<<<<<<<<"+timeLengthVal);
			 }
			 mapProgram.put(program_type, vals1);
		}
		
		
		
		
		
	
			
		 double[] totals = {0,0,0,0}; 
		 LinkedHashMap resmap = new LinkedHashMap();
		 for(String o:mapStation.keySet()){
			 double[] valNum = mapStation.get(o);
			 ZrstRepEffectStatisticsBean bean = new ZrstRepEffectStatisticsBean();
			 bean.setTransmit_station(o);
			 bean.setReceive_listens(valNum[0]+"_"+valNum[1]+"_"+valNum[2]+"_"+valNum[3]);
			 bean.setSub_report_type("23");
			 bean.setReport_type(my_report_type);
			 bean.setReport_id(report_id);
			 resmap.put((String)o, bean);
			 totals[0] += valNum[0];
			 totals[1] += valNum[1];
			 totals[2] += valNum[2];
			 totals[3] += valNum[3];
		 }
		 for(String o:mapProgram.keySet()){
			 double[] valNum = mapProgram.get(o);
			 ZrstRepEffectStatisticsBean bean = new ZrstRepEffectStatisticsBean();
			 bean.setTransmit_station(o);
			 bean.setReceive_listens(valNum[0]+"_"+valNum[1]+"_"+valNum[2]+"_"+valNum[3]);
			 bean.setSub_report_type("23");
			 bean.setReport_type(my_report_type);
			 bean.setReport_id(report_id);
			 resmap.put((String)o, bean);
		 }
		 if((totals[0]+totals[0]+totals[1]+totals[2]+totals[3])>0){
			 ZrstRepEffectStatisticsBean bean = new ZrstRepEffectStatisticsBean();
			 bean.setTransmit_station("��  ��");
			 bean.setReceive_listens(totals[0]+"_"+totals[1]+"_"+totals[2]+"_"+totals[3]);
			 bean.setSub_report_type("23");
			 bean.setReport_type(my_report_type);
			 bean.setReport_id(report_id);
			 resmap.put("��  ��", bean);
		 }
		return resmap;
	}
	/**
	 * �������岥��Ч��ͳ��1
	 * @detail  
	 * @method  
	 * @param starttime
	 * @param endtime
	 * @param my_report_type
	 * @param report_type
	 * @param asobj
	 * @return
	 * @throws Exception 
	 * @return  HashMap  
	 * @author  zhaoyahui
	 * @version 2013-1-10 ����07:32:41
	 */
	public LinkedHashMap statisticsReportCondition31(String starttime, String endtime,String my_report_type,String report_type,ASObject asobj,String report_id) throws Exception{
		String headcodes = (String)asobj.get("headcodes");
//		String headcodes = null;//test
		String valid_end_timeString="";
		if(my_report_type.equals("1")){//����̨
			valid_end_timeString=" and runplan.valid_end_time>=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') ";
			}
				String sql = "select  " +
						"  head.code  headcode,head.service_name service_name_headend,head.type_id, mark.headname as shortname,mark.counto,runplan.service_area,state.state_name ,lan.language_name" +
						" from radio_mark_view_xiaohei mark," +
						//" radio_stream_result_tab record," +
						" zres_runplan_chaifen_tab runplan," +
						" res_headend_tab head," +
						" zdic_language_tab lan ," +
						" dic_state_tab            state" +
								" where mark.runplan_id=runplan.runplan_id and mark.counto is not null and mark.counti is not null and mark.counts is not null " +
								" and head.code=mark.head_code" +
								" and  runplan.language_id=lan.language_id " +
								" and mark.report_type=1 " +//Ч��¼��
								//" and runplan.is_delete=0 " +
								
								valid_end_timeString +
								
								
							//	" and record.is_delete=0 " +
								" and runplan.runplan_type_id='"+my_report_type+"'"+
								" and state.state = head.state" +
								" and mark.start_datetime>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss')" +
								" and mark.end_datetime<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') order by head.code,lan.language_name ";
				 GDSet gd = DbComponent.Query(sql);
				 LinkedHashMap<String,String> mapHead = new LinkedHashMap<String,String>();
				 LinkedHashMap<String,String> mapState = new LinkedHashMap<String,String>();
				 LinkedHashMap<String,String> mapArea = new LinkedHashMap<String,String>();
				 LinkedHashMap<String,String> mapTotal = new LinkedHashMap<String,String>();
				 for(int i=0;i<gd.getRowCount();i++){
					 String language_name = gd.getString(i, "language_name");
					 String service_area = gd.getString(i, "service_name_headend");
					 String state_name = gd.getString(i, "state_name");
//					 String stationname = gd.getString(i, "stationname");

					 String type_id = gd.getString(i, "type_id");
					 String headcode = gd.getString(i, "headcode");
					 String shortname = gd.getString(i, "shortname");
					 if(service_area == null || service_area.equals("")){
						 throw new Exception(shortname+"�ķ�����Ϊ�գ���������");
					 }
					 if("102".equals(type_id) && ( headcode.endsWith("A") || headcode.endsWith("B")||headcode.endsWith("C") || headcode.endsWith("D")|| headcode.endsWith("E") || headcode.endsWith("F") || headcode.endsWith("G")  )){
								 headcode = headcode.substring(0, headcode.length()-1);
						// shortname = shortname.substring(0, shortname.length()-1);
					 }
					 if("102".equals(type_id) && ( shortname.endsWith("A") || shortname.endsWith("B")||shortname.endsWith("C") || shortname.endsWith("D") || shortname.endsWith("E")|| shortname.endsWith("F")|| shortname.endsWith("G"))){
								shortname = shortname.substring(0, shortname.length()-1);
					 }
					 if(headcodes == null || headcodes.equals("") || headcodes.indexOf(headcode+",")>=0){
					 } else{
						 continue;
					 }
					 String counto = gd.getString(i, "counto");
					 int countoNum = Integer.parseInt(counto);
					 String transmit_station_listens = "";//����_>=3��_�ܴ���_������%  ���磺��_88_123_78,2032_23_423_28
					 if(mapHead.get(shortname) == null){
						 int big3Count = (countoNum>=3?1:0);
						 String needStr = language_name+"_"+big3Count+"_1_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+",";
						 mapHead.put(shortname, needStr);
//						 mapHead.put(shortname, needStr + stationname+"A_6_6_6,");//test
					 } else{
						 String newHeadValue = "";
						 if((mapHead.get(shortname)).indexOf(language_name+"_")<0){
							 newHeadValue = mapHead.get(shortname)+language_name+"_0_0_0,";
							 mapHead.put(shortname, newHeadValue);
							 newHeadValue = "";
						 }
						 String[] headStationArr = (mapHead.get(shortname)).split(",");
						 for(String headValue:headStationArr){
							 String[] headValueArr = headValue.split("_");
//							 if(headValue.indexOf("___")>0){
//								 newHeadValue += headValue;
//							 } else{
								 if(headValueArr[0].equals(language_name)){
									 int big3Count = (countoNum>=3?1:0)+Integer.parseInt(headValueArr[1]);
									 int totalCount = Integer.parseInt(headValueArr[2])+1;
									 newHeadValue += language_name+"_"+big3Count+"_"+totalCount+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+",";
								 } else{
									 int big3Count = (countoNum>=3?1:0);
									 if((mapHead.get(shortname)).indexOf(language_name+"_")>-1){//�����������ԣ��˴ΰ����ݴ��·Ż�ȥ
										 newHeadValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
									 } else{//���û�У������µ�
									 }
								 }
//							 }
						 }
						 mapHead.put(shortname, newHeadValue);
					 }
					 
					 String[] service_areaArr = service_area.split(",");
					 for(int ai=0;ai<service_areaArr.length;ai++){
						 service_area = service_areaArr[ai];
						 if(mapArea.get(service_area) == null){//����
							 int big3Count = (countoNum>=3?1:0);
							 mapArea.put(service_area, language_name+"_"+big3Count+"_1_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+",");
						 } else{
							 String newHeadValue = "";
							 if((mapArea.get(service_area)).indexOf(language_name+"_")<0){
								 newHeadValue = mapArea.get(service_area)+language_name+"_0_0_0,";
								 mapArea.put(service_area, newHeadValue);
								 newHeadValue = "";
							 }
							 String[] headStationArr = (mapArea.get(service_area)).split(",");
							 for(String headValue:headStationArr){
								 String[] headValueArr = headValue.split("_");
	//							 if(headValue.indexOf("___")>0){
	//								 newHeadValue = headValue;
	//							 } else{
									 if(headValueArr[0].equals(language_name)){
										 int big3Count = (countoNum>=3?1:0)+Integer.parseInt(headValueArr[1]);
										 int totalCount = Integer.parseInt(headValueArr[2])+1;
										 newHeadValue += language_name+"_"+big3Count+"_"+totalCount+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+",";
									 } else{
										 int big3Count = (countoNum>=3?1:0);
										 if((mapArea.get(service_area)).indexOf(language_name+"_")>-1){
											 newHeadValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
										 } else{
										 }
									 }
	//							 }
							 }
							 mapArea.put(service_area, newHeadValue);
						 }
					 }
					 if(mapState.get(state_name) == null){//����
						 int big3Count = (countoNum>=3?1:0);
						 mapState.put(state_name, language_name+"_"+big3Count+"_1_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+",");
					 } else{
						 String newHeadValue = "";
						 if((mapState.get(state_name)).indexOf(language_name+"_")<0){
							 newHeadValue = mapState.get(state_name)+language_name+"_0_0_0,";
							 mapState.put(state_name, newHeadValue);
							 newHeadValue = "";
						 }
						 String[] headStationArr = (mapState.get(state_name)).split(",");
						 for(String headValue:headStationArr){
							 String[] headValueArr = headValue.split("_");
//							 if(headValue.indexOf("___")>0){
//								 newHeadValue = headValue;
//							 } else{
								 if(headValueArr[0].equals(language_name)){
									 int big3Count = (countoNum>=3?1:0)+Integer.parseInt(headValueArr[1]);
									 int totalCount = Integer.parseInt(headValueArr[2])+1;
									 newHeadValue += language_name+"_"+big3Count+"_"+totalCount+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+",";
								 } else{
									 int big3Count = (countoNum>=3?1:0);
									 if((mapState.get(state_name)).indexOf(language_name+"_")>-1){
										 newHeadValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
									 } else{
									 }
								 }
							 }
//						 }
						 mapState.put(state_name, newHeadValue);
					 }
					 
					 if(mapTotal.get("ȫ��") == null){//ȫ��
						 int big3Count = (countoNum>=3?1:0);
						 mapTotal.put("ȫ��", language_name+"_"+big3Count+"_1_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+",");
					 } else{
						 String newHeadValue = "";
						 if((mapTotal.get("ȫ��")).indexOf(language_name+"_")<0){
							 newHeadValue = mapTotal.get("ȫ��")+language_name+"_0_0_0,";
							 mapTotal.put("ȫ��", newHeadValue);
							 newHeadValue = "";
						 }
						 String[] headStationArr = (mapTotal.get("ȫ��")).split(",");
						 for(String headValue:headStationArr){
							 String[] headValueArr = headValue.split("_");
//							 if(headValue.indexOf("___")>0){
//								 newHeadValue = headValue;
//							 } else{
								 if(headValueArr[0].equals(language_name)){
									 int big3Count = (countoNum>=3?1:0)+Integer.parseInt(headValueArr[1]);
									 int totalCount = Integer.parseInt(headValueArr[2])+1;
									 newHeadValue += language_name+"_"+big3Count+"_"+totalCount+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+",";
								 } else{
									 int big3Count = (countoNum>=3?1:0);
									 if((mapTotal.get("ȫ��")).indexOf(language_name+"_")>-1){
										 newHeadValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
									 } else{
									 }
								 }
//							 }
						 }
						 mapTotal.put("ȫ��", newHeadValue);
					 }
				 }
				 
		LinkedHashMap<String,ZrstRepEffectStatisticsBean> map = new LinkedHashMap<String,ZrstRepEffectStatisticsBean>();
		//����̨_>=3��_�ܴ���_������%  ���磺2022_88_123_78,2032_23_423_28
		for(Object o:mapHead.keySet()){
			String val = mapHead.get(o);
			ZrstRepEffectStatisticsBean  bean = new ZrstRepEffectStatisticsBean();
			bean.setReceive_name(o.toString());
			bean.setLanguage_name_listens(val.substring(0, val.length()-1));
			bean.setSub_report_type("31");
			bean.setReport_type(my_report_type);
			bean.setReport_id(report_id);
			map.put((String)o, bean);
			
		}
		for(Object o:mapArea.keySet()){
			String val = mapArea.get(o);
			ZrstRepEffectStatisticsBean  bean = new ZrstRepEffectStatisticsBean();
			bean.setService_area(o.toString());
			bean.setLanguage_name_listens(val.substring(0, val.length()-1));
			bean.setSub_report_type("31");
			bean.setReport_type(my_report_type);
			bean.setReport_id(report_id);
			map.put((String)o, bean);
		}
		for(Object o:mapState.keySet()){
			String val = mapState.get(o);
			ZrstRepEffectStatisticsBean  bean = new ZrstRepEffectStatisticsBean();
			bean.setState_name(o.toString());
			bean.setLanguage_name_listens(val.substring(0, val.length()-1));
			bean.setSub_report_type("31");
			bean.setReport_type(my_report_type);
			bean.setReport_id(report_id);
			map.put((String)o, bean);
		}
		for(Object o:mapTotal.keySet()){
			String val = mapTotal.get(o);
			ZrstRepEffectStatisticsBean  bean = new ZrstRepEffectStatisticsBean();
			bean.setLanguage_name_listens(val.substring(0, val.length()-1));
			bean.setSub_report_type("31");
			bean.setReport_type(my_report_type);
			bean.setReport_id(report_id);
			map.put((String)o, bean);
		}
		return map;
	}
	/**
	 * �������岥��Ч��ͳ��2
	 * @detail  
	 * @method  
	 * @param starttime
	 * @param endtime
	 * @param my_report_type
	 * @param report_type
	 * @param asobj
	 * @return
	 * @throws Exception 
	 * @return  HashMap  
	 * @author  zhaoyahui
	 * @version 2013-1-10 ����07:32:41
	 */
	public LinkedHashMap statisticsReportCondition32(String starttime, String endtime,String my_report_type,String report_type,ASObject asobj,String report_id) throws Exception{
		LinkedHashMap<String,ZrstRepEffectStatisticsBean> map = new LinkedHashMap<String,ZrstRepEffectStatisticsBean>();
		String headcodes = (String)asobj.get("headcodes");
		String valid_end_timeString="";
		if(my_report_type.equals("1")){//����̨
			valid_end_timeString=" and runplan.valid_end_time>=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') ";
			}
		String sql = "select  " +
						"  head.code  headcode,head.service_name service_name_headend,head.type_id, mark.headname as shortname,mark.counto,runplan.service_area,state.state_name,lan.language_name" +
						" from radio_mark_view_xiaohei mark," +
						//" radio_stream_result_tab record," +
						" zres_runplan_chaifen_tab runplan," +
						" res_headend_tab head," +
						" zdic_language_tab lan ," +
						" dic_state_tab            state" +
								" where mark.runplan_id=runplan.runplan_id and mark.counto is not null and mark.counti is not null and mark.counts is not null " +
								" and head.code=mark.head_code" +
								" and mark.report_type=1 " +//Ч��¼��
								//" and runplan.is_delete=0 " +
								valid_end_timeString +
								
							 //	" and record.is_delete=0 " +
								" and runplan.runplan_type_id='"+my_report_type+"'"+
								" and state.state = head.state" +
								" and  runplan.language_id=lan.language_id " +
								" and mark.start_datetime>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss')" +
								" and mark.end_datetime<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss')  ";
				if(headcodes!=null&&!headcodes.equalsIgnoreCase("")){
				//	sql+=" and '"+headcodes.substring(0, headcodes.length()-1)+"' like '%'||head.code||'%' ";
					if(headcodes.split(",").length>1){
						String[] ss = headcodes.split(",");
						String newsql="";
						for(int m=0;m<ss.length;m++){
							newsql+=" head.code like '%"+ss[m]+"%' or";
						}
						sql+=" and ("+newsql.substring(0, newsql.length()-2)+")";
					}else{
						//sql+="and '"+headcodes.substring(0, headcodes.length()-1)+"' like '%'||head.code||'%' ";
						sql+=" and head.code like '%"+headcodes.substring(0, headcodes.length()-1)+"%'";
					}
				}
				sql+=" order by head.code,lan.language_name ";
				GDSet gd = DbComponent.Query(sql);

				 LinkedHashMap<String,String> mapStation = new LinkedHashMap<String,String>();
				 LinkedHashMap<String,String> mapState = new LinkedHashMap<String,String>();
				 LinkedHashMap<String,String> mapArea = new LinkedHashMap<String,String>();
				 LinkedHashMap<String,String> mapTotal = new LinkedHashMap<String,String>();
				 String allHead = "";
				 String allArea = "";
				 String allState = "";
				 for(int i=0;i<gd.getRowCount();i++){
					 String type_id = gd.getString(i, "type_id");
					 String headcode = gd.getString(i, "headcode");
					 String shortname = gd.getString(i, "shortname"); 
					 String service_area = gd.getString(i, "service_name_headend");
					 if(service_area == null || service_area.equals("")){
						 throw new Exception(shortname+"�ķ�����Ϊ�գ���������");
					 }
					 String state_name = gd.getString(i, "state_name");
					 if("102".equals(type_id) && ( headcode.endsWith("A") || headcode.endsWith("B")||headcode.endsWith("C") || headcode.endsWith("D")|| headcode.endsWith("E") || headcode.endsWith("F") || headcode.endsWith("G")  )){
								 headcode = headcode.substring(0, headcode.length()-1);
						// shortname = shortname.substring(0, shortname.length()-1);
					 }
					 if("102".equals(type_id) && ( shortname.endsWith("A") || shortname.endsWith("B")||shortname.endsWith("C") || shortname.endsWith("D") || shortname.endsWith("E")|| shortname.endsWith("F")|| shortname.endsWith("G"))){
								shortname = shortname.substring(0, shortname.length()-1);
					 }
					 //�ղ�վ��_������_����3�ִ���_�ܴ���
					 if(allHead.indexOf(shortname+"_A_A_A,")<0){
						 allHead += shortname+"_A_A_A,";
					 }
					 if(allArea.indexOf(","+service_area+"_A_A_A,")<0){
						 allArea += ","+service_area+"_A_A_A";
					 }
					 if(allState.indexOf(state_name+"_A_A_A,")<0){
						 allState += state_name+"_A_A_A,";
					 }
				 }
				 if(allArea.length()>1)
				 {
				 allArea=allArea.substring(1, allArea.length())+",";
				 }
				 for(int i=0;i<gd.getRowCount();i++){
					 String service_area = gd.getString(i, "service_name_headend");
					 String state_name = gd.getString(i, "state_name");
					 String language_name = gd.getString(i, "language_name");

					 String type_id = gd.getString(i, "type_id");
					 String headcode = gd.getString(i, "headcode");
					 String shortname = gd.getString(i, "shortname");
					 if("102".equals(type_id) && ( headcode.endsWith("A") || headcode.endsWith("B")||headcode.endsWith("C") || headcode.endsWith("D")|| headcode.endsWith("E") || headcode.endsWith("F") || headcode.endsWith("G")  )){
								 headcode = headcode.substring(0, headcode.length()-1);
						// shortname = shortname.substring(0, shortname.length()-1);
					 }
					 if("102".equals(type_id) && ( shortname.endsWith("A") || shortname.endsWith("B")||shortname.endsWith("C") || shortname.endsWith("D") || shortname.endsWith("E")|| shortname.endsWith("F")|| shortname.endsWith("G"))){
								shortname = shortname.substring(0, shortname.length()-1);
					 }
					 if(headcodes == null || headcodes.equals("") || headcodes.indexOf(headcode+",")>=0){
					 } else{
						 continue;
					 }
					 String counto = gd.getString(i, "counto");
					 int countoNum = Integer.parseInt(counto);
					 if(mapStation.get(language_name) == null){
						 String needStr = allHead;
						 mapStation.put(language_name, needStr);
					 } else{
						 
					 }
					 //�ղ�վ��_������_����3�ִ���_�ܴ���
					 String newHeadendValue = "";
					 String[] headendStationArr = (mapStation.get(language_name)).split(",");
					 for(String headValue:headendStationArr){
						 String[] headValueArr = headValue.split("_");
						 int big3Count = (countoNum>=3?1:0)+ Integer.parseInt(  (headValueArr[2].equals("A")?"0":headValueArr[2]) );
						 int totalCount = 1 + Integer.parseInt(  (headValueArr[3].equals("A")?"0":headValueArr[3]) );
						 if(headValueArr[0].equals(shortname)){
							 newHeadendValue += shortname+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+"_"+big3Count+"_"+totalCount+",";
						 } else{
							 if((mapStation.get(language_name)).indexOf(shortname+"_")>-1){
								 newHeadendValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
							 } else{
								 newHeadendValue += shortname+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+"_"+big3Count+"_1,";
								 
							 }
						 }
					 }
					 mapStation.put(language_name, newHeadendValue);
					 
					//������޵���������
					 if(mapState.get(language_name) == null){
						 String needStr = allState;
						 mapState.put(language_name, needStr);
					 } else{
					 }
					 String newSatateValue = "";
					 String[] stateStationArr = (mapState.get(language_name)).split(",");
					 for(String headValue:stateStationArr){
						 String[] headValueArr = headValue.split("_");
						 int big3Count = (countoNum>=3?1:0)+ Integer.parseInt(  (headValueArr[2].equals("A")?"0":headValueArr[2]) );
						 int totalCount = 1 + Integer.parseInt(  (headValueArr[3].equals("A")?"0":headValueArr[3]) );
						 if(headValueArr[0].equals(state_name)){
							 newSatateValue += state_name+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+"_"+big3Count+"_"+totalCount+",";
						 } else{
							 if((mapState.get(language_name)).indexOf(state_name+"_")>-1){
								 newSatateValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
							 } else{
								 newSatateValue += state_name+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+"_"+big3Count+"_1,";
							 }
						 }
					 }
					 mapState.put(language_name, newSatateValue);
					//�����������������
					 String[] service_areaArr = service_area.split(",");
					 for(int ai=0;ai<service_areaArr.length;ai++){
						 service_area = service_areaArr[ai];
						 if(mapArea.get(language_name) == null){
							 String needStr = allArea;
								 
							 mapArea.put(language_name, needStr);
						 } else{
						 }
						 String newAreaValue = "";
						 String[] areaStationArr = (mapArea.get(language_name)).split(",");
						 for(String headValue:areaStationArr){
							 String[] headValueArr = headValue.split("_");
							 int big3Count = (countoNum>=3?1:0)+ Integer.parseInt(  (headValueArr[2].equals("A")?"0":headValueArr[2]) );
							 int totalCount = 1 + Integer.parseInt(  (headValueArr[3].equals("A")?"0":headValueArr[3]) );
							 if(headValueArr[0].equals(service_area)){
								 newAreaValue += service_area+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+"_"+big3Count+"_"+totalCount+",";
							 } else{
								 if((mapArea.get(language_name)).indexOf(service_area+"_")>-1){
									 newAreaValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
								 } else{
									 newAreaValue += service_area+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+"_"+big3Count+"_1,";
									 
								 }
							 }
						 }
						 
						 mapArea.put(language_name, newAreaValue);
					 }
					//����ȫ������������
					 int big3Count = (countoNum>=3?1:0);
					 if(mapTotal.get(language_name) == null){
						 String needStr = "ȫ��_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+"_"+big3Count+"_1";
						 mapTotal.put(language_name, needStr);
					 } else{
						 int big3CountT = (countoNum>=3?1:0)+ Integer.parseInt(mapTotal.get(language_name).split("_")[2]);
						 int totalCountT = 1 + Integer.parseInt(mapTotal.get(language_name).split("_")[3]);
								
						 mapTotal.put(language_name, "ȫ��_"+new BigDecimal((Double.parseDouble(big3CountT+"")*100/totalCountT)).setScale(1,BigDecimal.ROUND_HALF_UP)+"_"+big3CountT+"_"+totalCountT);
					 }
					 
					 
				 }
				
		for(String o:mapStation.keySet()){
			String val = mapStation.get(o);
			ZrstRepEffectStatisticsBean  bean = new ZrstRepEffectStatisticsBean();
			bean.setLanguage_name(o.toString());
			String traVal = val;
			traVal += mapArea.get(o);
			traVal += mapState.get(o);
			traVal += mapTotal.get(o);
//			traVal = traVal.substring(0, traVal.length()-1);
			bean.setReceive_name_total_hours(traVal);
			bean.setSub_report_type("32");
			bean.setReport_type(my_report_type);
			bean.setReport_id(report_id);
			map.put((String)o, bean);
			
		}
		return map;
	}
	/**
	 * �������������ޡ�������ͳ�� ------------����sql���ִ��̫������˸�����̨������
	 * @detail  
	 * @method  
	 * @param starttime
	 * @param endtime
	 * @param my_report_type
	 * @param report_type
	 * @param asobj
	 * @return
	 * @throws Exception 
	 * @return  HashMap  
	 * @author  zhaoyahui
	 * @version 2013-1-10 ����07:32:41
	 */
	public LinkedHashMap statisticsReportCondition41(String starttime, String endtime,String my_report_type,String report_type,ASObject asobj,String report_id) throws Exception{
		LinkedHashMap<String,ZrstRepEffectStatisticsBean> map = new LinkedHashMap<String,ZrstRepEffectStatisticsBean>();
		String headcodes = (String)asobj.get("headcodes");
		String valid_end_timeString="";
		if(my_report_type.equals("1")){//����̨
			valid_end_timeString=" and runplan.valid_end_time>=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') ";
			}	
		String sql = "select  " +
						"  mark.counto,runplan.service_area,state.state_name,head.service_name service_name_headend " +
						" from radio_mark_view_xiaohei mark," +
					//	" radio_stream_result_tab record," +
						" zres_runplan_chaifen_tab runplan," +
						" res_headend_tab head," +
						" dic_state_tab state" +
								" where  mark.runplan_id=runplan.runplan_id and mark.counto is not null and mark.counti is not null and mark.counts is not null " +
								" and head.code=mark.head_code" +
								" and mark.report_type=1 " +//Ч��¼��
								//" and runplan.is_delete=0 " +
								
								valid_end_timeString +
								
								//" and record.is_delete=0 " +
								" and runplan.runplan_type_id='"+my_report_type+"' "+
								" and state.state = head.state" +
								" and mark.start_datetime>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss')" +
								" and mark.end_datetime<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss')  ";
				if(headcodes!=null&&!headcodes.equalsIgnoreCase("")){
				//	sql+=" and '"+headcodes.substring(0, headcodes.length()-1)+"' like '%'||mark.HEAD_CODE||'%' ";
					if(headcodes.split(",").length>1){
						String[] ss = headcodes.split(",");
						String newsql="";
						for(int m=0;m<ss.length;m++){
							newsql+=" mark.HEAD_CODE like '%"+ss[m]+"%' or";
						}
						sql+=" and ("+newsql.substring(0, newsql.length()-2)+")";
					}else{
						//sql+="and '"+headcodes.substring(0, headcodes.length()-1)+"' like '%'||head.code||'%' ";
						sql+=" and mark.HEAD_CODE like '%"+headcodes.substring(0, headcodes.length()-1)+"%'";
					}
					
				}
				sql+=" order by state.state_name";
				 GDSet gd = DbComponent.Query(sql);

				 LinkedHashMap<String,String> mapState = new LinkedHashMap<String,String>();
				 LinkedHashMap<String,String> mapArea = new LinkedHashMap<String,String>();
				 LinkedHashMap<String,String> mapTotal = new LinkedHashMap<String,String>();
				 for(int i=0;i<gd.getRowCount();i++){
					 String state_name = gd.getString(i, "state_name");
					 String service_area = gd.getString(i, "service_name_headend");
//					 String stationname = gd.getString(i, "stationname");
					 String counto = gd.getString(i, "counto");
					 int countoNum = Integer.parseInt(counto);
					 
					     
					//������޵���������
					 if(mapState.get("temp") == null){
						 int big3Count = (countoNum>=3?1:0);
						 mapState.put("temp", state_name+"_"+big3Count+"_1_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+",");
					 } else{
						 String newHeadValue = "";
						 if((mapState.get("temp")).indexOf(state_name+"_")<0){
							 newHeadValue = mapState.get("temp")+state_name+"_0_0_0,";
							 mapState.put("temp", newHeadValue);
							 newHeadValue = "";
						 }
						 String[] headStationArr = (mapState.get("temp")).split(",");
						 for(String headValue:headStationArr){
							 String[] headValueArr = headValue.split("_");
							 if(headValueArr[0].equals(state_name)){
								 int big3Count = (countoNum>=3?1:0)+Integer.parseInt(headValueArr[1]);
								 int totalCount = Integer.parseInt(headValueArr[2])+1;
								 
								 newHeadValue += state_name+"_"+big3Count+"_"+totalCount+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+",";
							 } else{
								 if((mapState.get("temp")).indexOf(state_name+"_")>-1){
									 newHeadValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
								 } else{
								 }
							 }
								
						 }
						 mapState.put("temp", newHeadValue);
					 }
					 
					//�����������������
					 String[] service_areaArr = service_area.split(",");
					 for(int ai=0;ai<service_areaArr.length;ai++){
						 service_area = service_areaArr[ai];
						 if(mapArea.get("temp") == null){
							 int big3Count = (countoNum>=3?1:0);
							 mapArea.put("temp", service_area+"_"+big3Count+"_1_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+",");
						 } else{
							 String newHeadValue = "";
							 if((mapArea.get("temp")).indexOf(service_area+"_")<0){
								 newHeadValue = mapArea.get("temp")+service_area+"_0_0_0,";
								 mapArea.put("temp", newHeadValue);
								 newHeadValue = "";
							 }
							 String[] headStationArr = (mapArea.get("temp")).split(",");
							 for(String headValue:headStationArr){
								 String[] headValueArr = headValue.split("_");
								 if(headValueArr[0].equals(service_area)){
									 int big3Count = (countoNum>=3?1:0)+Integer.parseInt(headValueArr[1]);
									 int totalCount = Integer.parseInt(headValueArr[2])+1;
									 
									 newHeadValue += service_area+"_"+big3Count+"_"+totalCount+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+",";
								 } else{
									 if((mapArea.get("temp")).indexOf(service_area+"_")>-1){
										 newHeadValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
									 } else{
									 }
								 }
									
							 }
							 mapArea.put("temp", newHeadValue);
						 }
					 }
					//����ȫ������������
					 if(mapTotal.get("temp") == null){
						 int big3Count = (countoNum>=3?1:0);
						 mapTotal.put("temp", "�ܼ�_"+big3Count+"_1_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP));
					 } else{
						 String newHeadValue = "";
						 String[] totalValArr = (mapTotal.get("temp")).split("_");
						 int big3Count = (countoNum>=3?1:0)+Integer.parseInt(totalValArr[1]);
						 int totalCount = Integer.parseInt(totalValArr[2])+1;
						 
						 newHeadValue = "�ܼ�_"+big3Count+"_"+totalCount+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+",";
								
						 mapTotal.put("temp", newHeadValue);
					 }
					 
					 
				 }
		ZrstRepEffectStatisticsBean  bean = new ZrstRepEffectStatisticsBean();
		String traVal = "";
		if(mapArea.get("temp") != null && mapState.get("temp")!=null && mapTotal.get("temp")!=null){
			traVal += mapArea.get("temp");
			traVal += mapState.get("temp");
			traVal += mapTotal.get("temp");
			bean.setAll_listens(traVal);
			bean.setSub_report_type("41");
			bean.setReport_type(my_report_type);
			bean.setReport_id(report_id);
			map.put("temp", bean);
		}
		return map;
	}
	/**
	 * ��ң��վ���������������ޡ��ɱ�֤����Ƶʱͳ��   ------------����sql���ִ��̫������˸�����̨������
	 * @detail  
	 * @method  
	 * @param starttime
	 * @param endtime
	 * @param my_report_type
	 * @param report_type
	 * @param asobj
	 * @return
	 * @throws Exception 
	 * @return  HashMap  
	 * @author  zhaoyahui
	 * @version 2013-1-10 ����07:32:41
	 */
	public LinkedHashMap statisticsReportCondition51(String starttime, String endtime,String my_report_type,String report_type,ASObject asobj,String report_id) throws Exception{
		LinkedHashMap<String,ZrstRepEffectStatisticsBean> map = new LinkedHashMap<String,ZrstRepEffectStatisticsBean>();
		String headcodes = (String)asobj.get("headcodes");
		String valid_end_timeString="";
		if(my_report_type.equals("1")){//����̨
			valid_end_timeString=" and runplan.valid_end_time>=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') ";
			}
		String sql = "select   runplan.start_time ||'-'|| runplan.end_time as play_time," +
				"  head.code  headcode,head.service_name service_name_headend,head.type_id, mark.headname as shortname,mark.counto,runplan.service_area,state.state_name, mark.file_length,runplan.freq freq " +
				" from radio_mark_view_xiaohei mark," +
			//	" radio_stream_result_tab record," +
				" zres_runplan_chaifen_tab runplan," +
				" res_headend_tab head," +
				" dic_state_tab            state" +
						" where mark.runplan_id=runplan.runplan_id and mark.counto is not null and mark.counti is not null and mark.counts is not null " +
						" and head.code=mark.head_code" +
						" and mark.report_type=1 " +//Ч��¼��
						//" and runplan.is_delete=0 " +
						valid_end_timeString +
						
						//" and record.is_delete=0 " +
						" and runplan.runplan_type_id='"+my_report_type+"'"+
						" and state.state = head.state " +
						" and mark.start_datetime>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss')" +
						" and mark.end_datetime<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss')  ";
		if(headcodes!=null&&!headcodes.equalsIgnoreCase("")){
			sql+=" and '"+headcodes.substring(0, headcodes.length()-1)+"' like '%'||mark.HEAD_CODE||'%' ";
		}
		sql+=" order by mark.headname ";
		 GDSet gd = DbComponent.Query(sql);

		 LinkedHashMap<String,double[]> mapStationFinal = new LinkedHashMap<String,double[]>();
		 LinkedHashMap<String,double[]> mapAreaFinal = new LinkedHashMap<String,double[]>();
		 LinkedHashMap<String,double[]> mapStateFinal = new LinkedHashMap<String,double[]>();
		 LinkedHashMap<String,double[]> mapTotalFinal = new LinkedHashMap<String,double[]>();
		 
		 LinkedHashMap<String,HashMap> mapHead = new LinkedHashMap<String,HashMap>();
		 LinkedHashMap<String,HashMap> mapArea = new LinkedHashMap<String,HashMap>();
		 LinkedHashMap<String,HashMap> mapState = new LinkedHashMap<String,HashMap>();
		 LinkedHashMap<String,HashMap> mapTotal = new LinkedHashMap<String,HashMap>();
		 /**
		  * ����1�������ʣ������ȴ��ڵ���3�ִ���������������֮�ȣ��ðٷ�����ʾ��
������			2��Ч��������ʵĹ�ϵ��������>=80%��Ч���ǿɱ�֤������ 80%>������>=60%��Ч���ǻ�����������60%>������>=30%��Ч������ʱ��������������<30%��Ч���ǲ���������
		  */
		 for(int i=0;i<gd.getRowCount();i++){
			 String play_time = gd.getString(i, "play_time");
//			 double timeLength = StringTool.getPlayTimeLength("2000-01-01 "+play_time.split("-")[1]+":00", "2000-01-01 "+play_time.split("-")[0]+":00");
			 double timeLength = 0.5;
			 String service_area = gd.getString(i, "service_name_headend");
			 String state_name = gd.getString(i, "state_name");
			 String freq = gd.getString(i, "freq");
			 String[] file_lengths = gd.getString(i, "file_length").split(":");//00:00:00
			 String type_id = gd.getString(i, "type_id");
			 String headcode = gd.getString(i, "headcode");
			 String shortname = gd.getString(i, "shortname");
			 if(service_area == null || service_area.equals("")){
				 throw new Exception(shortname+"�ķ�����Ϊ�գ���������");
			 }
			 if(service_area == null || service_area.equals("")){
				 throw new Exception(shortname+"�ķ�����Ϊ�գ���������");
			 }
			 if("102".equals(type_id) && ( headcode.endsWith("A") || headcode.endsWith("B")||headcode.endsWith("C") || headcode.endsWith("D")|| headcode.endsWith("E") || headcode.endsWith("F") || headcode.endsWith("G")  )){
						 headcode = headcode.substring(0, headcode.length()-1);
				// shortname = shortname.substring(0, shortname.length()-1);
			 }
			 if("102".equals(type_id) && ( shortname.endsWith("A") || shortname.endsWith("B")||shortname.endsWith("C") || shortname.endsWith("D") || shortname.endsWith("E")|| shortname.endsWith("F")|| shortname.endsWith("G"))){
						shortname = shortname.substring(0, shortname.length()-1);
			 }
			 if(headcodes == null || headcodes.equals("") || headcodes.indexOf(headcode+",")>=0){
			 } else{
				 continue;
			 }
			 String counto = gd.getString(i, "counto");
			 int countoNum = Integer.parseInt(counto);
			 
			 if(mapHead.get(freq+"_"+shortname) == null){
				 int big3Count = (countoNum>=3?1:0);
				 int listionRate = Integer.parseInt(new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
				 HashMap valMap = new HashMap();
				 valMap.put("big3Count", big3Count);//����3�ִ���
				 valMap.put("totalCount", 1);//�ܹ�����
				 valMap.put("listionRate", listionRate);//������
				 valMap.put("timeLength", timeLength);//ʱ��
				 valMap.put("type", "");//���� ����ֱ�� ���ڵط� ����ֱ�� ����ת��
				 mapHead.put(freq+"_"+shortname, valMap);
			 } else{
				 HashMap valMap = mapHead.get(freq+"_"+shortname);
				 int big3Count = (countoNum>=3?1:0) + (Integer)valMap.get("big3Count");
				 int totalCount = 1 + (Integer)valMap.get("totalCount");
				 int listionRate = Integer.parseInt(new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
				 double timeLengthTemp = (Double)valMap.get("timeLength") + timeLength;
				 valMap.put("big3Count", big3Count);//����3�ִ���
				 valMap.put("totalCount", totalCount);//�ܹ�����
				 valMap.put("listionRate", listionRate);//������
				 valMap.put("timeLength", timeLengthTemp);//ʱ��
				 mapHead.put(freq+"_"+shortname, valMap);
			 }
			 String[] service_areaArr = service_area.split(",");
			 for(int ai=0;ai<service_areaArr.length;ai++){
				 service_area = service_areaArr[ai];
				 if(mapArea.get(freq+"_"+service_area) == null){
					 int big3Count = (countoNum>=3?1:0);
					 int listionRate = Integer.parseInt(new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
					 HashMap valMap = new HashMap();
					 valMap.put("big3Count", big3Count);//����3�ִ���
					 valMap.put("totalCount", 1);//�ܹ�����
					 valMap.put("listionRate", listionRate);//������
					 valMap.put("timeLength", timeLength);//ʱ��
					 valMap.put("type", "");//���� ����ֱ�� ���ڵط� ����ֱ�� ����ת��
					 mapArea.put(freq+"_"+service_area, valMap);
				 } else{
					 HashMap valMap = mapArea.get(freq+"_"+service_area);
					 int big3Count = (countoNum>=3?1:0) + (Integer)valMap.get("big3Count");
					 int totalCount = 1 + (Integer)valMap.get("totalCount");
					 int listionRate = Integer.parseInt(new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
					 double timeLengthTemp = (Double)valMap.get("timeLength") + timeLength;
					 valMap.put("big3Count", big3Count);//����3�ִ���
					 valMap.put("totalCount", totalCount);//�ܹ�����
					 valMap.put("listionRate", listionRate);//������
					 valMap.put("timeLength", timeLengthTemp);//ʱ��
					 mapArea.put(freq+"_"+service_area, valMap);
				 }
			 }
			 if(mapState.get(freq+"_"+state_name) == null){
				 int big3Count = (countoNum>=3?1:0);
				 int listionRate = Integer.parseInt(new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
				 HashMap valMap = new HashMap();
				 valMap.put("big3Count", big3Count);//����3�ִ���
				 valMap.put("totalCount", 1);//�ܹ�����
				 valMap.put("listionRate", listionRate);//������
				 valMap.put("timeLength", timeLength);//ʱ��
				 valMap.put("type", "");//���� ����ֱ�� ���ڵط� ����ֱ�� ����ת��
				 mapState.put(freq+"_"+state_name, valMap);
			 } else{
				 HashMap valMap = mapState.get(freq+"_"+state_name);
				 int big3Count = (countoNum>=3?1:0) + (Integer)valMap.get("big3Count");
				 int totalCount = 1 + (Integer)valMap.get("totalCount");
				 int listionRate = Integer.parseInt(new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
				 double timeLengthTemp = (Double)valMap.get("timeLength") + timeLength;
				 valMap.put("big3Count", big3Count);//����3�ִ���
				 valMap.put("totalCount", totalCount);//�ܹ�����
				 valMap.put("listionRate", listionRate);//������
				 valMap.put("timeLength", timeLengthTemp);//ʱ��
				 mapState.put(freq+"_"+state_name, valMap);
			 }
			 
			 if(mapTotal.get(freq+"_"+"ȫ��") == null){
				 int big3Count = (countoNum>=3?1:0);
				 int listionRate = Integer.parseInt(new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
				 HashMap valMap = new HashMap();
				 valMap.put("big3Count", big3Count);//����3�ִ���
				 valMap.put("totalCount", 1);//�ܹ�����
				 valMap.put("listionRate", listionRate);//������
				 valMap.put("timeLength", timeLength);//ʱ��
				 valMap.put("type", "");//���� ����ֱ�� ���ڵط� ����ֱ�� ����ת��
				 mapTotal.put(freq+"_"+"ȫ��", valMap);
			 } else{
				 HashMap valMap = mapTotal.get(freq+"_"+"ȫ��");
				 int big3Count = (countoNum>=3?1:0) + (Integer)valMap.get("big3Count");
				 int totalCount = 1 + (Integer)valMap.get("totalCount");
				 int listionRate = Integer.parseInt(new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
				 double timeLengthTemp = (Double)valMap.get("timeLength") + timeLength;
				 valMap.put("big3Count", big3Count);//����3�ִ���
				 valMap.put("totalCount", totalCount);//�ܹ�����
				 valMap.put("listionRate", listionRate);//������
				 valMap.put("timeLength", timeLengthTemp);//ʱ��
				 mapTotal.put(freq+"_"+"ȫ��", valMap);
			 }
			
		 }
		 
		//ȡͬһ��Ƶ�ʲ�ͬվ��Ŀ����ʣ�������ϣ����ʱ��
		for(String o:mapHead.keySet()){
			String freq_shortname = o; 		// Map�ļ�
//			System.out.println("freq_shortname1=="+freq_shortname);
			String shortnameval = freq_shortname.split("_")[1];
			HashMap valMap = mapHead.get(o);   		// Map��ֵ
			int listionRate = (Integer)valMap.get("listionRate");
			double timeLengthVal = (Double)valMap.get("timeLength");
			double[] vals = {0,0,0,0,0};
			if(mapStationFinal.get(shortnameval) != null){
				vals = mapStationFinal.get(shortnameval);
			}
			
			 if(listionRate>=80){//Ч���ǿɱ�֤����
				 vals[0] += timeLengthVal;
			 } else if(listionRate>=60 && listionRate<80){//Ч���ǻ���������
				 vals[1] += timeLengthVal;
			 } else if(listionRate>=30 && listionRate<60){//Ч������ʱ������
				 vals[2] += timeLengthVal;
			 } else if(listionRate<30){//Ч���ǲ�������
				 vals[3] += timeLengthVal;
			 }
			 mapStationFinal.put(shortnameval, vals);
		}
			 
		for(String o:mapArea.keySet()){
			String freq_shortname = o; 		// Map�ļ�
//			System.out.println("freq_shortname=="+freq_shortname);
			String shortnameval = freq_shortname.split("_")[1];
			HashMap valMap = mapArea.get(o);   		// Map��ֵ
			int listionRate = (Integer)valMap.get("listionRate");
			double timeLengthVal = (Double)valMap.get("timeLength");
			double[] vals = {0,0,0,0,0};
			if(mapAreaFinal.get(shortnameval) != null){
				vals = mapAreaFinal.get(shortnameval);
			}
			
			 if(listionRate>=80){//Ч���ǿɱ�֤����
				 vals[0] += timeLengthVal;
			 } else if(listionRate>=60 && listionRate<80){//Ч���ǻ���������
				 vals[1] += timeLengthVal;
			 } else if(listionRate>=30 && listionRate<60){//Ч������ʱ������
				 vals[2] += timeLengthVal;
			 } else if(listionRate<30){//Ч���ǲ�������
				 vals[3] += timeLengthVal;
			 }
			 mapAreaFinal.put(shortnameval, vals);
		}
		
		for(String o:mapState.keySet()){
			String freq_shortname = o; 		// Map�ļ�
			String shortnameval = freq_shortname.split("_")[1];
			HashMap valMap = mapState.get(o);   		// Map��ֵ
			int listionRate = (Integer)valMap.get("listionRate");
			double timeLengthVal = (Double)valMap.get("timeLength");
			double[] vals = {0,0,0,0,0};
			if(mapStateFinal.get(shortnameval) != null){
				vals = mapStateFinal.get(shortnameval);
			}
			
			 if(listionRate>=80){//Ч���ǿɱ�֤����
				 vals[0] += timeLengthVal;
			 } else if(listionRate>=60 && listionRate<80){//Ч���ǻ���������
				 vals[1] += timeLengthVal;
			 } else if(listionRate>=30 && listionRate<60){//Ч������ʱ������
				 vals[2] += timeLengthVal;
			 } else if(listionRate<30){//Ч���ǲ�������
				 vals[3] += timeLengthVal;
			 }
			 mapStateFinal.put(shortnameval, vals);
		}
		
		for(String o:mapTotal.keySet()){
			String freq_shortname = o; 		// Map�ļ�
			String shortnameval = freq_shortname.split("_")[1];
			HashMap valMap = mapTotal.get(o);   		// Map��ֵ
			int listionRate = (Integer)valMap.get("listionRate");
			double timeLengthVal = (Double)valMap.get("timeLength");
			double[] vals = {0,0,0,0,0};
			if(mapTotalFinal.get(shortnameval) != null){
				vals = mapTotalFinal.get(shortnameval);
			}
			
			 if(listionRate>=80){//Ч���ǿɱ�֤����
				 vals[0] += timeLengthVal;
			 } else if(listionRate>=60 && listionRate<80){//Ч���ǻ���������
				 vals[1] += timeLengthVal;
			 } else if(listionRate>=30 && listionRate<60){//Ч������ʱ������
				 vals[2] += timeLengthVal;
			 } else if(listionRate<30){//Ч���ǲ�������
				 vals[3] += timeLengthVal;
			 }
			 mapTotalFinal.put(shortnameval, vals);
		}
		
		 for(String o:mapStationFinal.keySet()){
			 double[] valNum = mapStationFinal.get(o);
			 ZrstRepEffectStatisticsBean bean = new ZrstRepEffectStatisticsBean();
			 bean.setReceive_name(o);
			 bean.setReceive_listens(valNum[0]+"_"+valNum[1]+"_"+valNum[2]+"_"+valNum[3]+"_"+(valNum[0]+valNum[1]+valNum[2]+valNum[3]));
			 bean.setSub_report_type("51");
			 bean.setReport_type(my_report_type);
			 bean.setReport_id(report_id);
			 map.put((String)o, bean);
		 }
		 
		 for(String o:mapAreaFinal.keySet()){
			 double[] valNum = mapAreaFinal.get(o);
			 ZrstRepEffectStatisticsBean bean = new ZrstRepEffectStatisticsBean();
			 bean.setService_area(o);
			 bean.setReceive_listens(valNum[0]+"_"+valNum[1]+"_"+valNum[2]+"_"+valNum[3]+"_"+(valNum[0]+valNum[1]+valNum[2]+valNum[3]));
			 bean.setSub_report_type("51");
			 bean.setReport_type(my_report_type);
			 bean.setReport_id(report_id);
			 map.put((String)o, bean);
		 }
		 
		 for(String o:mapStateFinal.keySet()){
			 double[] valNum = mapStateFinal.get(o);
			 ZrstRepEffectStatisticsBean bean = new ZrstRepEffectStatisticsBean();
			 bean.setState_name(o);
			 bean.setReceive_listens(valNum[0]+"_"+valNum[1]+"_"+valNum[2]+"_"+valNum[3]+"_"+(valNum[0]+valNum[1]+valNum[2]+valNum[3]));
			 bean.setSub_report_type("51");
			 bean.setReport_type(my_report_type);
			 bean.setReport_id(report_id);
			 map.put((String)o, bean);
		 }
		 
		 for(String o:mapTotalFinal.keySet()){
			 double[] valNum = mapTotalFinal.get(o);
			 ZrstRepEffectStatisticsBean bean = new ZrstRepEffectStatisticsBean();
			 bean.setReceive_listens(valNum[0]+"_"+valNum[1]+"_"+valNum[2]+"_"+valNum[3]+"_"+(valNum[0]+valNum[1]+valNum[2]+valNum[3]));
			 bean.setSub_report_type("51");
			 bean.setReport_type(my_report_type);
			 bean.setReport_id(report_id);
			 map.put((String)o, bean);
		 }
		return map;
	}
	/**
	 * ���¿����ʶԱ�  ------------����sql���ִ��̫������˸�����̨������
	 * @detail  
	 * @method  
	 * @param starttime
	 * @param endtime
	 * @param my_report_type
	 * @param report_type
	 * @param asobj
	 * @return
	 * @throws Exception 
	 * @return  HashMap  
	 * @author  zhaoyahui
	 * @version 2013-1-10 ����07:32:41
	 */
	public LinkedHashMap statisticsReportCondition61(String starttime, String endtime,String my_report_type,String report_type,ASObject asobj,String report_id) throws Exception{
		String headcodes = (String)asobj.get("headcodes");
//		String headcodes = null;//test
		String valid_end_timeString="";
		if(my_report_type.equals("1")){//����̨
			valid_end_timeString=" and runplan.valid_end_time>=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') ";
			}
				String sql = "select  " +
						"  head.code  headcode,head.service_name service_name_headend,head.type_id, mark.headname as shortname,mark.counto,runplan.service_area,state.state_name,mark.start_datetime " +
						" from radio_mark_view_xiaohei mark," +
					//	" radio_stream_result_tab record," +
						" zres_runplan_chaifen_tab runplan," +
						" res_headend_tab head," +
						" dic_state_tab            state" +
								" where mark.runplan_id=runplan.runplan_id and mark.counto is not null and mark.counti is not null and mark.counts is not null " +
								" and head.code=mark.head_code  " +
								" and mark.report_type=1 " +//Ч��¼��
								//" and runplan.is_delete=0 " +
								valid_end_timeString+
								
							//	" and record.is_delete=0 " +
								" and runplan.runplan_type_id='"+my_report_type+"'"+
								" and state.state = head.state" +
								" and mark.start_datetime>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss')" +
								" and mark.end_datetime<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') order by mark.headname ";
				 GDSet gd = DbComponent.Query(sql);
				 LinkedHashMap<String,String> mapHead = new LinkedHashMap<String,String>();
				 LinkedHashMap<String,String> mapState = new LinkedHashMap<String,String>();
				 LinkedHashMap<String,String> mapArea = new LinkedHashMap<String,String>();
				 LinkedHashMap<String,String> mapTotal = new LinkedHashMap<String,String>();
				 for(int i=0;i<gd.getRowCount();i++){
					 String service_area = gd.getString(i, "service_name_headend");
					 String state_name = gd.getString(i, "state_name");
					 String start_datetime = gd.getString(i, "start_datetime");
					 String monthStr = Integer.parseInt(start_datetime.substring(5,7))+"��";
					 String type_id = gd.getString(i, "type_id");
					 String headcode = gd.getString(i, "headcode");
					 String shortname = gd.getString(i, "shortname");
					 if(service_area == null || service_area.equals("")){
						 throw new Exception(shortname+"�ķ�����Ϊ�գ���������");
					 }
					 if("102".equals(type_id) && ( headcode.endsWith("A") || headcode.endsWith("B")||headcode.endsWith("C") || headcode.endsWith("D")|| headcode.endsWith("E") || headcode.endsWith("F") || headcode.endsWith("G")  )){
								 headcode = headcode.substring(0, headcode.length()-1);
						// shortname = shortname.substring(0, shortname.length()-1);
					 }
					 if("102".equals(type_id) && ( shortname.endsWith("A") || shortname.endsWith("B")||shortname.endsWith("C") || shortname.endsWith("D") || shortname.endsWith("E")|| shortname.endsWith("F")|| shortname.endsWith("G"))){
								shortname = shortname.substring(0, shortname.length()-1);
					 }
					 if(headcodes == null || headcodes.equals("") || headcodes.indexOf(headcode+",")>=0){
					 } else{
						 continue;
					 }
					 String counto = gd.getString(i, "counto");
					 int countoNum = Integer.parseInt(counto);
					 String transmit_station_listens = "";//����_>=3��_�ܴ���_������%  ���磺��_88_123_78,2032_23_423_28
					 if(mapHead.get(shortname) == null){
						 int big3Count = (countoNum>=3?1:0);
						 String needStr = monthStr+"_"+big3Count+"_1_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+",";
						 mapHead.put(shortname, needStr);
//						 mapHead.put(shortname, needStr + stationname+"A_6_6_6,");//test
					 } else{
						 String newHeadValue = "";
						 if((mapHead.get(shortname)).indexOf(monthStr+"_")<0){
							 newHeadValue = mapHead.get(shortname)+monthStr+"_0_0_0,";
							 mapHead.put(shortname, newHeadValue);
							 newHeadValue = "";
						 }
						 String[] headStationArr = (mapHead.get(shortname)).split(",");
						 for(String headValue:headStationArr){
							 String[] headValueArr = headValue.split("_");
//							 if(headValue.indexOf("___")>0){
//								 newHeadValue += headValue;
//							 } else{
								 if(headValueArr[0].equals(monthStr)){
									 int big3Count = (countoNum>=3?1:0)+Integer.parseInt(headValueArr[1]);
									 int totalCount = Integer.parseInt(headValueArr[2])+1;
									 newHeadValue += monthStr+"_"+big3Count+"_"+totalCount+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+",";
								 } else{
									 int big3Count = (countoNum>=3?1:0);
									 if((mapHead.get(shortname)).indexOf(monthStr+"_")>-1){//�����������˴ΰ����ݴ��·Ż�ȥ
										 newHeadValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
									 } else{//���û�У������µ�
//										 newHeadValue += monthStr+"_"+big3Count+"_1_"+(big3Count*100/1)+",";
									 }
								 }
//							 }
						 }
						 mapHead.put(shortname, newHeadValue);
					 }
					 String[] service_areaArr = service_area.split(",");
					 for(int ai=0;ai<service_areaArr.length;ai++){
						 service_area = service_areaArr[ai];
						 if(mapArea.get(service_area) == null){//����
							 int big3Count = (countoNum>=3?1:0);
							 mapArea.put(service_area, monthStr+"_"+big3Count+"_1_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+",");
						 } else{
							 String newHeadValue = "";
							 if((mapArea.get(service_area)).indexOf(monthStr+"_")<0){
								 newHeadValue = mapArea.get(service_area)+monthStr+"_0_0_0,";
								 mapArea.put(service_area, newHeadValue);
								 newHeadValue = "";
							 }
							 String[] headStationArr = (mapArea.get(service_area)).split(",");
							 for(String headValue:headStationArr){
								 String[] headValueArr = headValue.split("_");
	//							 if(headValue.indexOf("___")>0){
	//								 newHeadValue = headValue;
	//							 } else{
									 if(headValueArr[0].equals(monthStr)){ 
										 int big3Count = (countoNum>=3?1:0)+Integer.parseInt(headValueArr[1]);
										 int totalCount = Integer.parseInt(headValueArr[2])+1;
										 newHeadValue += monthStr+"_"+big3Count+"_"+totalCount+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+",";
									 } else{
										 int big3Count = (countoNum>=3?1:0);
										 if((mapArea.get(service_area)).indexOf(monthStr+"_")>-1){
											 newHeadValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
										 } else{
//											 newHeadValue += monthStr+"_"+big3Count+"_1_"+(big3Count*100/1)+",";
										 }
									 }
	//							 }
							 }
							 mapArea.put(service_area, newHeadValue);
						 }
					 }
					 if(mapState.get(state_name) == null){//����
						 int big3Count = (countoNum>=3?1:0);
						 mapState.put(state_name, monthStr+"_"+big3Count+"_1_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+",");
					 } else{
						 String newHeadValue = "";
						 if((mapState.get(state_name)).indexOf(monthStr+"_")<0){
							 newHeadValue = mapState.get(state_name)+monthStr+"_0_0_0,";
							 mapState.put(state_name, newHeadValue);
							 newHeadValue = "";
						 }
						 String[] headStationArr = (mapState.get(state_name)).split(",");
						 for(String headValue:headStationArr){
							 String[] headValueArr = headValue.split("_");
//							 if(headValue.indexOf("___")>0){
//								 newHeadValue = headValue;
//							 } else{
								 if(headValueArr[0].equals(monthStr)){
									 int big3Count = (countoNum>=3?1:0)+Integer.parseInt(headValueArr[1]);
									 int totalCount = Integer.parseInt(headValueArr[2])+1;
									 newHeadValue += monthStr+"_"+big3Count+"_"+totalCount+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+",";
								 } else{
									 int big3Count = (countoNum>=3?1:0);
									 if((mapState.get(state_name)).indexOf(monthStr+"_")>-1){
										 newHeadValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
									 } else{
//										 newHeadValue += monthStr+"_"+big3Count+"_1_"+(big3Count*100/1)+",";
									 }
								 }
							 }
//						 }
						 mapState.put(state_name, newHeadValue);
					 }
					 
					 if(mapTotal.get("ȫ��") == null){//ȫ��
						 int big3Count = (countoNum>=3?1:0);
						 mapTotal.put("ȫ��", monthStr+"_"+big3Count+"_1_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+",");
					 } else{
						 String newHeadValue = "";
						 if((mapTotal.get("ȫ��")).indexOf(monthStr+"_")<0){
							 newHeadValue = mapTotal.get("ȫ��")+monthStr+"_0_0_0,";
							 mapTotal.put("ȫ��", newHeadValue);
							 newHeadValue = "";
						 }
						 String[] headStationArr = (mapTotal.get("ȫ��")).split(",");
						 for(String headValue:headStationArr){
							 String[] headValueArr = headValue.split("_");
//							 if(headValue.indexOf("___")>0){
//								 newHeadValue = headValue;
//							 } else{
								 if(headValueArr[0].equals(monthStr)){
									 int big3Count = (countoNum>=3?1:0)+Integer.parseInt(headValueArr[1]);
									 int totalCount = Integer.parseInt(headValueArr[2])+1;
									 newHeadValue += monthStr+"_"+big3Count+"_"+totalCount+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+",";
								 } else{
									 int big3Count = (countoNum>=3?1:0);
									 if((mapTotal.get("ȫ��")).indexOf(monthStr+"_")>-1){
										 newHeadValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
									 } else{
//										 newHeadValue += monthStr+"_"+big3Count+"_1_"+(big3Count*100/1)+",";
									 }
								 }
//							 }
						 }
						 mapTotal.put("ȫ��", newHeadValue);
					 }
				 }
				 
		LinkedHashMap<String,ZrstRepEffectStatisticsBean> map = new LinkedHashMap<String,ZrstRepEffectStatisticsBean>();
		for(Object o:mapHead.keySet()){
			String val = mapHead.get(o);
			ZrstRepEffectStatisticsBean  bean = new ZrstRepEffectStatisticsBean();
			bean.setReceive_name(o.toString());
			String[] monthArr = val.substring(0, val.length()-1).split(",");
			HashMap<String,String> tempMap = new HashMap<String,String>();
			for(String mons:monthArr){
//				if(mons.split("_")[3].equals("0"))
//					continue;
				tempMap.put(mons.split("_")[0], mons.split("_")[3]);
			}
			String month_listens = (tempMap.get("1��")==null?"$":tempMap.get("1��"))+"_"+(tempMap.get("2��")==null?"$":tempMap.get("2��"))+"_"+(tempMap.get("3��")==null?"$":tempMap.get("3��"))+"_"
			+(tempMap.get("4��")==null?"$":tempMap.get("4��"))+"_"+(tempMap.get("5��")==null?"$":tempMap.get("5��"))+"_"+(tempMap.get("6��")==null?"$":tempMap.get("6��"))+"_"
			+(tempMap.get("7��")==null?"$":tempMap.get("7��"))+"_"+(tempMap.get("8��")==null?"$":tempMap.get("8��"))+"_"+(tempMap.get("9��")==null?"$":tempMap.get("9��"))+"_"
			+(tempMap.get("10��")==null?"$":tempMap.get("10��"))+"_"+(tempMap.get("11��")==null?"$":tempMap.get("11��"))+"_"+(tempMap.get("12��")==null?"$":tempMap.get("12��"));
			bean.setMonth_listens(month_listens);
			bean.setSub_report_type("61");
			bean.setReport_type(my_report_type);
			bean.setReport_id(report_id);
			map.put((String)o, bean);
			
		}

		for(Object o:mapArea.keySet()){
			String val = mapArea.get(o);
			ZrstRepEffectStatisticsBean  bean = new ZrstRepEffectStatisticsBean();
			bean.setService_area(o.toString());
			String[] monthArr = val.substring(0, val.length()-1).split(",");
			HashMap<String,String> tempMap = new HashMap<String,String>();
			for(String mons:monthArr){
//				if(mons.split("_")[3].equals("0"))
//					continue;
				tempMap.put(mons.split("_")[0], mons.split("_")[3]);
			}
			String month_listens = (tempMap.get("1��")==null?"$":tempMap.get("1��"))+"_"+(tempMap.get("2��")==null?"$":tempMap.get("2��"))+"_"+(tempMap.get("3��")==null?"$":tempMap.get("3��"))+"_"
			+(tempMap.get("4��")==null?"$":tempMap.get("4��"))+"_"+(tempMap.get("5��")==null?"$":tempMap.get("5��"))+"_"+(tempMap.get("6��")==null?"$":tempMap.get("6��"))+"_"
			+(tempMap.get("7��")==null?"$":tempMap.get("7��"))+"_"+(tempMap.get("8��")==null?"$":tempMap.get("8��"))+"_"+(tempMap.get("9��")==null?"$":tempMap.get("9��"))+"_"
			+(tempMap.get("10��")==null?"$":tempMap.get("10��"))+"_"+(tempMap.get("11��")==null?"$":tempMap.get("11��"))+"_"+(tempMap.get("12��")==null?"$":tempMap.get("12��"));
			bean.setMonth_listens(month_listens);
			bean.setSub_report_type("61");
			bean.setReport_type(my_report_type);
			bean.setReport_id(report_id);
			map.put((String)o, bean);
		}
		for(Object o:mapState.keySet()){
			String val = mapState.get(o);
			ZrstRepEffectStatisticsBean  bean = new ZrstRepEffectStatisticsBean();
			bean.setState_name(o.toString());
			String[] monthArr = val.substring(0, val.length()-1).split(",");
			HashMap<String,String> tempMap = new HashMap<String,String>();
			for(String mons:monthArr){
//				if(mons.split("_")[3].equals("0"))
//					continue;
				tempMap.put(mons.split("_")[0], mons.split("_")[3]);
			}
			String month_listens = (tempMap.get("1��")==null?"$":tempMap.get("1��"))+"_"+(tempMap.get("2��")==null?"$":tempMap.get("2��"))+"_"+(tempMap.get("3��")==null?"$":tempMap.get("3��"))+"_"
			+(tempMap.get("4��")==null?"$":tempMap.get("4��"))+"_"+(tempMap.get("5��")==null?"$":tempMap.get("5��"))+"_"+(tempMap.get("6��")==null?"$":tempMap.get("6��"))+"_"
			+(tempMap.get("7��")==null?"$":tempMap.get("7��"))+"_"+(tempMap.get("8��")==null?"$":tempMap.get("8��"))+"_"+(tempMap.get("9��")==null?"$":tempMap.get("9��"))+"_"
			+(tempMap.get("10��")==null?"$":tempMap.get("10��"))+"_"+(tempMap.get("11��")==null?"$":tempMap.get("11��"))+"_"+(tempMap.get("12��")==null?"$":tempMap.get("12��"));
			bean.setMonth_listens(month_listens);
			bean.setSub_report_type("61");
			bean.setReport_type(my_report_type);
			bean.setReport_id(report_id);
			map.put((String)o, bean);
		}
		for(Object o:mapTotal.keySet()){
			String val = mapTotal.get(o);
			ZrstRepEffectStatisticsBean  bean = new ZrstRepEffectStatisticsBean();
			String[] monthArr = val.substring(0, val.length()-1).split(",");
			HashMap<String,String> tempMap = new HashMap<String,String>();
			for(String mons:monthArr){
//				if(mons.split("_")[3].equals("0"))
//					continue;
				tempMap.put(mons.split("_")[0], mons.split("_")[3]);
			}
			String month_listens = (tempMap.get("1��")==null?"$":tempMap.get("1��"))+"_"+(tempMap.get("2��")==null?"$":tempMap.get("2��"))+"_"+(tempMap.get("3��")==null?"$":tempMap.get("3��"))+"_"
			+(tempMap.get("4��")==null?"$":tempMap.get("4��"))+"_"+(tempMap.get("5��")==null?"$":tempMap.get("5��"))+"_"+(tempMap.get("6��")==null?"$":tempMap.get("6��"))+"_"
			+(tempMap.get("7��")==null?"$":tempMap.get("7��"))+"_"+(tempMap.get("8��")==null?"$":tempMap.get("8��"))+"_"+(tempMap.get("9��")==null?"$":tempMap.get("9��"))+"_"
			+(tempMap.get("10��")==null?"$":tempMap.get("10��"))+"_"+(tempMap.get("11��")==null?"$":tempMap.get("11��"))+"_"+(tempMap.get("12��")==null?"$":tempMap.get("12��"));
			bean.setMonth_listens(month_listens);
			bean.setSub_report_type("61");
			bean.setReport_type(my_report_type);
			bean.setReport_id(report_id);
			map.put((String)o, bean);
		}
		return map;
	}
	/**
	 * Ƶ��ƽ��������ͳ�Ʊ�
	 * @detail  
	 * @method  
	 * @param starttime
	 * @param endtime
	 * @param my_report_type
	 * @param report_type
	 * @param asobj
	 * @return
	 * @throws Exception 
	 * @return  HashMap  
	 * @author  zhaoyahui
	 * @version 2013-1-10 ����07:32:41
	 */
	public LinkedHashMap statisticsReportCondition71(String starttime, String endtime,String my_report_type,String report_type,ASObject asobj,String report_id) throws Exception{
		String headcodes = (String)asobj.get("headcodes");
		String sql  = "";
		if(my_report_type.equals("1")){//����̨
			sql = "select runplan.start_time ||'-'|| runplan.end_time as play_time,lan.language_name, runplan.freq,station.name stationname,runplan.direction," +
			" runplan.power, head.code  headcode,head.service_name service_name_headend,head.type_id, mark.headname as shortname,mark.counto,runplan.service_area," +
			" runplan.valid_start_time ,runplan.valid_end_time, dis.runplan_id as dis_runplan_id,dis.disturb " +
			" from radio_mark_view_xiaohei mark," +
		//	" radio_stream_result_tab record," +
			" zres_runplan_chaifen_tab runplan," +
			" zdic_language_tab lan ," +
			" res_transmit_station_tab station," +
			" res_headend_tab head," +
			" (select * from zres_runplan_disturb_tab where  valid_end_time>to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss')) dis  "+
			//" zres_runplan_disturb_tab dis " +
					" where mark.runplan_id=runplan.runplan_id and  runplan.language_id=lan.language_id and mark.counto is not null and mark.counti is not null and mark.counts is not null " +
					" and runplan.station_id=station.station_id and head.code=mark.head_code" +
					" and mark.report_type=1 " +//Ч��¼��
					//" and runplan.is_delete=0 " +
					" and runplan.valid_end_time>=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') "+
					
				//	" and record.is_delete=0 " +
					" and runplan.parent_id = dis.runplan_id(+) " +
					//" and dis.is_delete=0 " +
					" and runplan.runplan_type_id='"+my_report_type+"'"+
					" and mark.start_datetime>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss')" +
					" and mark.end_datetime<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') ";
		} else if(my_report_type.equals("2")){//�������
			sql = "select runplan.start_time ||'-'|| runplan.end_time as play_time,lan.language_name, runplan.freq ,runplan.direction,runplan.redisseminators," +
			" runplan.power, head.code  headcode,head.service_name service_name_headend,head.type_id, mark.headname as shortname,mark.counto,runplan.service_area," +
			" runplan.valid_start_time ,runplan.valid_end_time, dis.runplan_id as dis_runplan_id,dis.disturb " +
			" from radio_mark_view_xiaohei mark," +
		//	" radio_stream_result_tab record," +
			" zres_runplan_chaifen_tab runplan," +
			" zdic_language_tab lan ," +
			" res_headend_tab head," +
			" zres_runplan_disturb_tab dis " +
					" where mark.runplan_id=runplan.runplan_id and  runplan.language_id=lan.language_id and mark.counto is not null and mark.counti is not null and mark.counts is not null " +
					" and head.code=mark.head_code" +
					" and mark.report_type=1 " +//Ч��¼��
					//" and runplan.is_delete=0 " +
					//" and runplan.valid_end_time>=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') "+
					
					//" and record.is_delete=0 " +
					" and runplan.parent_id = dis.runplan_id(+) " +
					//" and dis.is_delete=0 " +
					" and runplan.runplan_type_id='"+my_report_type+"'"+
					" and mark.start_datetime>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss')" +
					" and mark.end_datetime<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') ";
		}
//		 if(headcodes!=null&&!headcodes.equalsIgnoreCase("")){
//			 sql+="and '"+headcodes.substring(0, headcodes.length()-1)+"' like '%'||head.code||'%' ";
//		 }
		if(headcodes!=null&&!headcodes.equalsIgnoreCase("")){
			if(headcodes.split(",").length>1){
				String[] ss = headcodes.split(",");
				String newsql="";
				for(int m=0;m<ss.length;m++){
					newsql+=" mark.HEAD_CODE like '%"+ss[m]+"%' or";
				}
				sql+=" and ("+newsql.substring(0, newsql.length()-2)+")";
			}else{
				//sql+="and '"+headcodes.substring(0, headcodes.length()-1)+"' like '%'||head.code||'%' ";
				sql+=" and mark.HEAD_CODE like '%"+headcodes.substring(0, headcodes.length()-1)+"%'";
			}
			
		}
		 sql+=" order by mark.headname,runplan.freq ";
		 GDSet gd = DbComponent.Query(sql);
		 LinkedHashMap<String,ZrstRepEffectStatisticsBean> map = new LinkedHashMap<String,ZrstRepEffectStatisticsBean>();
		 for(int i=0;i<gd.getRowCount();i++){
			 //��ע��Ϣ��ͬ��Ƶ���������Ϣ����룬���ڿ����ʵ���60%δ����ͬ��Ƶ������Ϣ����Ҫ�ڱ�ע��Ϣ��ֱ�Ӽ����ź�����������
			 String dis_runplan_id = gd.getString(i, "dis_runplan_id");//������Ϣ������ͼid
			 String disturb = gd.getString(i, "disturb");//������Ϣ
			 String play_time = gd.getString(i, "play_time");
			 String language_name = gd.getString(i, "language_name");
			 String freq = gd.getString(i, "freq");
			 String stationname = "";
			 String valid_start_time = gd.getString(i, "valid_start_time");
			 String valid_end_time = gd.getString(i, "valid_end_time");
			 String runplan_type_id = "";//1������̨����ͼ2���������
			 
			 if(my_report_type.equals("2")){
				 String redisseminators = gd.getString(i, "redisseminators");//ת������
				 if(redisseminators.equals("")){
					 redisseminators = " ";
				 }
				 stationname = redisseminators;
				 runplan_type_id = "2";
			 } else{
				 stationname = gd.getString(i, "stationname");
				 runplan_type_id = "1";
			 }
			 String direction = gd.getString(i, "direction");
			 String power = gd.getString(i, "power");
			 String counto = gd.getString(i, "counto");
			 String service_area = gd.getString(i, "service_area");
			 String type_id = gd.getString(i, "type_id");
//			 String headcode = gd.getString(i, "headcode");
//			 String shortname = gd.getString(i, "shortname");
//			 if("102".equals(type_id) && ( headcode.endsWith("A") || headcode.endsWith("B") )){
//				 headcode = headcode.substring(0, headcode.length()-1);
//				 shortname = shortname.substring(0, shortname.length()-1);
//			 }
			 int countoNum = Integer.parseInt(counto);
//			 double timeLength = StringTool.getPlayTimeLength("2000-01-01 "+play_time.split("-")[0]+":00", "2000-01-01 "+play_time.split("-")[1]+":00");
			 double timeLength = 0.5;
			 String[] service_areaArr = service_area.split(",");
			 for(int ai=0;ai<service_areaArr.length;ai++){
				 service_area = service_areaArr[ai];
				 String mapKey = play_time+language_name+freq+stationname+direction+power+service_area;
				 if(map.get(mapKey) == null){
					 ZrstRepEffectStatisticsBean detailBean = new ZrstRepEffectStatisticsBean();
					 detailBean.setPlay_time(play_time);
					 detailBean.setLanguage_name(language_name);
					 detailBean.setFreq(freq);
					 detailBean.setTransmit_station(stationname);
					 detailBean.setRunplan_type_id_temp(runplan_type_id);
					 detailBean.setValid_start_time_temp(valid_start_time);
					 detailBean.setValid_end_time_temp(valid_end_time);
					 detailBean.setTransmit_direction(direction);
					 detailBean.setTransmit_power(power);
					 detailBean.setService_area(service_area);
//					 detailBean.setReceive_name(shortname);
//					 detailBean.setReceive_code(headcode);
					 
					 int fenCount0 = countoNum==0?1:0;
					 int fenCount1 = countoNum==1?1:0;
					 int fenCount2 = countoNum==2?1:0;
					 int fenCount3 = countoNum==3?1:0;
					 int fenCount4 = countoNum==4?1:0;
					 int fenCount5 = countoNum==5?1:0;
					 double fen0 = countoNum==0?timeLength:0;
					 double fen1 = countoNum==1?timeLength:0;
					 double fen2 = countoNum==2?timeLength:0;
					 double fen3 = countoNum==3?timeLength:0;
					 double fen4 = countoNum==4?timeLength:0;
					 double fen5 = countoNum==5?timeLength:0;
					 detailBean.setReceive_count(timeLength+"_1");
					 //��ʱ��� ����ʱ��_��ֵĴ���
					 detailBean.setFen0(fen0+"_"+fenCount0);
					 detailBean.setFen1(fen1+"_"+fenCount1);
					 detailBean.setFen2(fen2+"_"+fenCount2);
					 detailBean.setFen3(fen3+"_"+fenCount3);
					 detailBean.setFen4(fen4+"_"+fenCount4);
					 detailBean.setFen5(fen5+"_"+fenCount5);
					 detailBean.setAverage_listens(new BigDecimal(Double.parseDouble((fenCount3+fenCount4+fenCount5)+"")*100/1).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
					 if(dis_runplan_id.equals("")){
						 if(Integer.parseInt(new BigDecimal(Double.parseDouble((fenCount3+fenCount4+fenCount5)+"")*100/1).setScale(1,BigDecimal.ROUND_HALF_UP)+"")<60){
							 disturb = "�ź�����������";
						 }
					 }
					 detailBean.setBak(disturb);
					 detailBean.setSub_report_type("71");
					 detailBean.setReport_type(my_report_type);
					 detailBean.setReport_id(report_id);
					 map.put(mapKey, detailBean);
				 } else{
					 ZrstRepEffectStatisticsBean detailBean = (ZrstRepEffectStatisticsBean)map.get(mapKey);
					 double receive_length = Double.parseDouble(detailBean.getReceive_count().split("_")[0]);
					 int receive_count = Integer.parseInt(detailBean.getReceive_count().split("_")[1]);
					 receive_length += timeLength;
					 receive_count++;
					 detailBean.setReceive_count(receive_length+"_"+receive_count);
					 double fen0 = Double.parseDouble(detailBean.getFen0().split("_")[0])+(countoNum==0?timeLength:0);
					 double fen1 = Double.parseDouble(detailBean.getFen1().split("_")[0])+(countoNum==1?timeLength:0);
					 double fen2 = Double.parseDouble(detailBean.getFen2().split("_")[0])+(countoNum==2?timeLength:0);
					 double fen3 = Double.parseDouble(detailBean.getFen3().split("_")[0])+(countoNum==3?timeLength:0);
					 double fen4 = Double.parseDouble(detailBean.getFen4().split("_")[0])+(countoNum==4?timeLength:0);
					 double fen5 = Double.parseDouble(detailBean.getFen5().split("_")[0])+(countoNum==5?timeLength:0);
	
					 int fenCount0 = Integer.parseInt(detailBean.getFen0().split("_")[1])+(countoNum==0?1:0);
					 int fenCount1 = Integer.parseInt(detailBean.getFen1().split("_")[1])+(countoNum==1?1:0);
					 int fenCount2 = Integer.parseInt(detailBean.getFen2().split("_")[1])+(countoNum==2?1:0);
					 int fenCount3 = Integer.parseInt(detailBean.getFen3().split("_")[1])+(countoNum==3?1:0);
					 int fenCount4 = Integer.parseInt(detailBean.getFen4().split("_")[1])+(countoNum==4?1:0);
					 int fenCount5 = Integer.parseInt(detailBean.getFen5().split("_")[1])+(countoNum==5?1:0);
					 
					 detailBean.setFen0(fen0+"_"+fenCount0);
					 detailBean.setFen1(fen1+"_"+fenCount1);
					 detailBean.setFen2(fen2+"_"+fenCount2);
					 detailBean.setFen3(fen3+"_"+fenCount3);
					 detailBean.setFen4(fen4+"_"+fenCount4);
					 detailBean.setFen5(fen5+"_"+fenCount5);
					 detailBean.setAverage_listens(new BigDecimal(Double.parseDouble((fenCount3+fenCount4+fenCount5)+"")*100/receive_count).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
					 if(dis_runplan_id.equals("")){
						 if(Integer.parseInt(new BigDecimal(Double.parseDouble((fenCount3+fenCount4+fenCount5)+"")*100/receive_count).setScale(1,BigDecimal.ROUND_HALF_UP)+"")<60){
							 disturb = "�ź�����������";
						 }
					 }
					 detailBean.setBak(disturb);
					 map.put(mapKey, detailBean);
				 }
			 }
		 }
		
		 
		 for(String o:map.keySet()){
			 ZrstRepEffectStatisticsBean bean = map.get(o);
			 //�����ȵ�����̶�������5��������8�����ʾ��80��89%��ʱ������ȴﵽ5�֣���4��������1�����ʾ��10��19%��ʱ������ȴﵽ4�֡�
			 //0�ֵĲ���ʱ�������ʱ��
			 bean.setFen0(Math.floor(Double.parseDouble(bean.getFen0().split("_")[0])*10/Double.parseDouble(bean.getReceive_count().split("_")[0]))+"");
			 bean.setFen1(Math.floor(Double.parseDouble(bean.getFen1().split("_")[0])*10/Double.parseDouble(bean.getReceive_count().split("_")[0]))+"");
			 bean.setFen2(Math.floor(Double.parseDouble(bean.getFen2().split("_")[0])*10/Double.parseDouble(bean.getReceive_count().split("_")[0]))+"");
			 bean.setFen3(Math.floor(Double.parseDouble(bean.getFen3().split("_")[0])*10/Double.parseDouble(bean.getReceive_count().split("_")[0]))+"");
			 bean.setFen4(Math.floor(Double.parseDouble(bean.getFen4().split("_")[0])*10/Double.parseDouble(bean.getReceive_count().split("_")[0]))+"");
			 bean.setFen5(Math.floor(Double.parseDouble(bean.getFen5().split("_")[0])*10/Double.parseDouble(bean.getReceive_count().split("_")[0]))+"");
			 bean.setReceive_count(bean.getReceive_count().split("_")[1]);
			 map.put((String)o, bean);
			 

//			//��ע��Ϣ��ͬ��Ƶ���������Ϣ����룬���ڿ����ʵ���60%δ����ͬ��Ƶ������Ϣ����Ҫ�ڱ�ע��Ϣ��ֱ�Ӽ����ź�����������
//			//����Ƶ�ʡ�����ʱ�䡢��Ч��ʼʱ�䡢��Ч����ʱ�䡢����̨(ת������) 
//			String sqlsome = "select disturb from zres_runplan_disturb_tab where " +
//							" freq='"+bean.getFreq()+"'" +
//							" and language='"+bean.getLanguage_name()+"' " +
//							" and start_time || '-' || end_time='"+bean.getPlay_time()+"'" +
//							" and valid_start_time='"+bean.getValid_start_time_temp()+"' " +
//							" and valid_end_time='"+bean.getValid_end_time_temp()+"' " +
//							" and is_delete=0";
//			String subSql = "";
//			if(bean.getRunplan_type_id_temp().equals("1")){
//				subSql = " and station_name = '"+bean.getTransmit_station()+"'";
//			} else if(bean.getRunplan_type_id_temp().equals("2")){
//				subSql = " and redisseminators = '"+bean.getTransmit_station()+"'";
//			}
//			sqlsome += subSql;
//			String disturb = "";
//			 GDSet gds = DbComponent.Query(sqlsome);
//			 if(gds.getRowCount()>0){
//				 disturb = gds.getString(0, "disturb");
//				 bean.setBak(disturb);
//			 } else{
//				 if(Integer.parseInt(bean.getAverage_listens())<60){
//					 bean.setBak("�ź�����������");
//				 }
//			 }
//			 map.put((String)o, bean);
		 }
		return map;
	}
	/**
	 * ���ݱ���id��ѯ��ϸ����
	 * @detail  
	 * @method  
	 * @param asobj
	 * @return 
	 * @return  ArrayList  
	 * @author  zhaoyahui
	 * @version 2013-1-11 ����10:45:21
	 */
	public ArrayList queryDetailReport(ASObject asobj) throws Exception{
		ArrayList list = new ArrayList();
		String reportId  = (String)asobj.get("reportId");
		String sub_report_type  = (String)asobj.get("sub_report_type");
		String  sql  = "select t.*��from zrst_rep_effect_statistics_tab t where t.report_id="+reportId;
		if(sub_report_type != null && !sub_report_type.equals("")){
			sql += " and t.sub_report_type!='"+sub_report_type+"'";
		}
//		if(codes!=null&&!codes.equalsIgnoreCase("")){
//			sql+=" and '"+codes+"' like '%'||t.receive_code||'%' and t.receive_code is not null ";
//		}
		sql += " order by  t.sub_report_type ,t.data_id";
		GDSet gdSet= DbComponent.Query(sql);
		String classpath = ZrstRepEffectStatisticsBean.class.getName();
		list = StringTool.converGDSetToBeanList(gdSet, classpath);
		
		return list;
	}

	/**
	 * ���ݱ���id��ѯ��ϸ����
	 * @detail  
	 * @method  
	 * @param asobj
	 * @return 
	 * @return  ArrayList  
	 * @author  zhaoyahui
	 * @version 2013-1-11 ����10:45:21
	 */
	public ArrayList queryDetailReport1(ASObject asobj) throws Exception{
		ArrayList list = new ArrayList();
		String reportId  = (String)asobj.get("reportId");
		String sub_report_type  = (String)asobj.get("sub_report_type");
		String  sql  = "select t.*��from zrst_rep_effect_statistics_tab t where   t.report_id="+reportId;
		if(sub_report_type != null && !sub_report_type.equals("")){
			sql += " and t.sub_report_type='"+sub_report_type+"'";
		}
//		if(codes!=null&&!codes.equalsIgnoreCase("")){
//			sql+=" and '"+codes+"' like '%'||t.receive_code||'%' and t.receive_code is not null ";
//		}
		sql += " order by   t.receive_code,t.language_name,t.transmit_station,t.freq,t.play_time ";
		GDSet gdSet= DbComponent.Query(sql);
		String classpath = ZrstRepEffectStatisticsBean.class.getName();
		list = StringTool.converGDSetToBeanList(gdSet, classpath);
		
		return list;
	}
	
	/**
	 * ���ݱ���id��ȡÿ������̨���ܿ�������Ϣ
	 * @param asobj
	 * @author leeo
	 * @date 2013/09/13
	 * @return
	 */
	public String queryStation_ZListens(ASObject asobj) {
		String s = "";
		String reportId  = (String)asobj.get("reportId");
		String sub_report_type  = (String)asobj.get("sub_report_type");
		String  sql  = "select t.transmit_station_listens��from zrst_rep_effect_statistics_tab t where t.receive_name is null and t.state_name is null and t.service_area is null and  t.sub_report_type='21' and  t.report_id="+reportId;
        sql+=" order by receive_name ";
		//		if(sub_report_type != null && !sub_report_type.equals("")){
//			sql += " and t.sub_report_type='"+sub_report_type+"'";
//		}
		try {
			GDSet gdSet= DbComponent.Query(sql);
			if(gdSet.getRowCount()>0){
				s=gdSet.getString(0, "transmit_station_listens");
			}
		} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		
		return s;
	}
	/**
	 *  ���ݱ���id��ȡÿ�����Ե��ܿ�������Ϣ
	 * @param asobj
	 * @author leeo
	 * @date 2013/09/13
	 * @return
	 */
	public ArrayList queryLanguage_ZListens(ASObject asobj){
		ArrayList list = new ArrayList();
		String reportId  = (String)asobj.get("reportId");
		String sub_report_type  = (String)asobj.get("sub_report_type");
		String  sql  = "select t.language_name,t.receive_name_total_hours��from zrst_rep_effect_statistics_tab t where t.language_name is not null and  t.sub_report_type='32' and  t.report_id="+reportId;
		sql+=" order by t.language_name";
		GDSet gdSet=null;
		try {
			 gdSet= DbComponent.Query(sql);
			 if(gdSet.getRowCount()>0){
				 for(int i=0;i<gdSet.getRowCount();i++){
					 ASObject obj = new ASObject();
					 if(gdSet.getString(i, "language_name")!=null&&!gdSet.getString(i, "language_name").equalsIgnoreCase("")){
						 obj.put("language", gdSet.getString(i, "language_name"));
						 
						 obj.put("listens", (gdSet.getString(i, "receive_name_total_hours").split(",")[gdSet.getString(i, "receive_name_total_hours").split(",").length-1]).split("_")[1]);
						 list.add(obj);
					 }
				 }
			 }
		} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		
		return list;
	}
	
	
	/**
	 *  ���ݱ���id��ȡÿ��ң��վ���ܿ�����
	 * @param asobj
	 * @author leeo
	 * @date 2013/10/16
	 * @return
	 */
	public ArrayList queryYkz_ZListens(ASObject asobj){
		ArrayList list = new ArrayList();
		String reportId  = (String)asobj.get("reportId");
		String sub_report_type  = (String)asobj.get("sub_report_type");
		String  sql  = "select t.receive_name,t.listen��from zrst_rep_effect_statistics_tab t where t.sub_report_type='11' and t.play_time like '%����Ч��%' and  t.report_id="+reportId;
		sql+=" order by t.receive_code ";
		GDSet gdSet=null;
		try {
			 gdSet= DbComponent.Query(sql);
			 if(gdSet.getRowCount()>0){
				 for(int i=0;i<gdSet.getRowCount();i++){
					 ASObject obj = new ASObject();
					 if(gdSet.getString(i, "receive_name")!=null&&!gdSet.getString(i, "receive_name").equalsIgnoreCase("")){
						 obj.put("receive_name", gdSet.getString(i, "receive_name"));
						 obj.put("listen", gdSet.getString(i, "listen"));
						 list.add(obj);
					 }
				 }
			 }
		} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		
		return list;
	}
	/**
	 * ��ѯƵ�ʿ����ʵ���ϸ��Ϣ
	 * @param reportId
	 * @param sub_reportId
	 * @return
	 */
	public ArrayList queryFreqListen(String reportId,String sub_reportId){
		ArrayList list = new ArrayList();
		String sql="select * from zrst_rep_effect_statistics_tab t where t.sub_report_type = '"+sub_reportId+"' and t.report_id = '"+reportId+"'";
		sql+=" order by t.service_area,t.language_name,t.transmit_station,t.freq,t.play_time ";
		try {
			GDSet gd = DbComponent.Query(sql);
			String classpath = ZrstRepEffectStatisticsBean.class.getName();
			list = StringTool.converGDSetToBeanList(gd, classpath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}
	
	/**
	 * ��ȡҪͳ�Ƶķ���̨����
	 * @param asobj
	 * @return
	 */
	public ArrayList getStation(ASObject asobj){
		ArrayList list = new ArrayList();
		String reportId  = (String)asobj.get("reportId");
		String sub_report_type  = (String)asobj.get("sub_report_type");
		String sql="select distinct transmit_station,rt.station_type from  zrst_rep_effect_statistics_tab t,res_transmit_station_tab rt " +
				"where t.sub_report_type='11' " +
				" and t.transmit_station is not null and rt.name like '%'||t.transmit_station||'%' and rt.is_delete=0   " +
				" and t.play_time is not null and  t.report_id="+reportId;
		sql+=" order by transmit_station ";
		try {
			GDSet gd =DbComponent.Query(sql);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					ASObject obj=StationEffectHours(asobj,gd.getString(i, "transmit_station"),gd.getString(i, "station_type"),"101");
					list.add(obj);
				}
			}
			if(list.size()>0){
				//�ܼ�Ƶʱ������
				double total_hour1 = 0;//�ɱ�֤����Ƶʱ��
				double total_hour2=0;//����������Ƶʱ��
				double total_hour3=0;//��ʱ������Ƶʱ��
				double total_hour4=0;//��������Ƶʱ��
				double total_hour5=0;//�ܼ�Ƶʱ��
				//���ڵط�Ƶʱ������
				double hour1 = 0;//�ɱ�֤����Ƶʱ��
				double hour2=0;//����������Ƶʱ��
				double hour3=0;//��ʱ������Ƶʱ��
				double hour4=0;//��������Ƶʱ��
				double hour5=0;//�ܼ�Ƶʱ��
				//����ֱ��Ƶʱ������
				double gn_hour1 = 0;//�ɱ�֤����Ƶʱ��
				double gn_hour2=0;//����������Ƶʱ��
				double gn_hour3=0;//��ʱ������Ƶʱ��
				double gn_hour4=0;//��������Ƶʱ��
				double gn_hour5=0;//�ܼ�Ƶʱ��
				//����ֱ��Ƶʱ������
				double gw_hour1 = 0;//�ɱ�֤����Ƶʱ��
				double gw_hour2=0;//����������Ƶʱ��
				double gw_hour3=0;//��ʱ������Ƶʱ��
				double gw_hour4=0;//��������Ƶʱ��
				double gw_hour5=0;//�ܼ�Ƶʱ��
				//����ת��Ƶʱ������
				double hw_hour1 = 0;//�ɱ�֤����Ƶʱ��
				double hw_hour2=0;//����������Ƶʱ��
				double hw_hour3=0;//��ʱ������Ƶʱ��
				double hw_hour4=0;//��������Ƶʱ��
				double hw_hour5=0;//�ܼ�Ƶʱ��
				for(int i=0;i<list.size();i++){
					
					ASObject obj1 = (ASObject) list.get(i);
					total_hour1+=Double.parseDouble(obj1.get("hour1").toString());
					total_hour2+=Double.parseDouble(obj1.get("hour2").toString());
					total_hour3+=Double.parseDouble(obj1.get("hour3").toString());
					total_hour4+=Double.parseDouble(obj1.get("hour4").toString());
					total_hour5+=Double.parseDouble(obj1.get("hour5").toString());
					if(obj1.get("station_type").toString().equalsIgnoreCase("���ڵط�")){
						hour1+=Double.parseDouble(obj1.get("hour1").toString());
						hour2+=Double.parseDouble(obj1.get("hour2").toString());
						hour3+=Double.parseDouble(obj1.get("hour3").toString());
						hour4+=Double.parseDouble(obj1.get("hour4").toString());
						hour5+=Double.parseDouble(obj1.get("hour5").toString());
					}
					if(obj1.get("station_type").toString().equalsIgnoreCase("����ֱ��")){
						gn_hour1+=Double.parseDouble(obj1.get("hour1").toString());
						gn_hour2+=Double.parseDouble(obj1.get("hour2").toString());
						gn_hour3+=Double.parseDouble(obj1.get("hour3").toString());
						gn_hour4+=Double.parseDouble(obj1.get("hour4").toString());
						gn_hour5+=Double.parseDouble(obj1.get("hour5").toString());
					}
	                if(obj1.get("station_type").toString().equalsIgnoreCase("����ֱ��")){
	                	gw_hour1+=Double.parseDouble(obj1.get("hour1").toString());
						gw_hour2+=Double.parseDouble(obj1.get("hour2").toString());
						gw_hour3+=Double.parseDouble(obj1.get("hour3").toString());
						gw_hour4+=Double.parseDouble(obj1.get("hour4").toString());
						gw_hour5+=Double.parseDouble(obj1.get("hour5").toString());
					}
	                if(obj1.get("station_type").toString().equalsIgnoreCase("����ת��")){
	                	hw_hour1+=Double.parseDouble(obj1.get("hour1").toString());
						hw_hour2+=Double.parseDouble(obj1.get("hour2").toString());
						hw_hour3+=Double.parseDouble(obj1.get("hour3").toString());
						hw_hour4+=Double.parseDouble(obj1.get("hour4").toString());
						hw_hour5+=Double.parseDouble(obj1.get("hour5").toString());
					}
				}
				if(hour1>0||hour2>0||hour3>0||hour4>0){
					ASObject df_obj= new ASObject();
					df_obj.put("station", "���ڵط�");
					df_obj.put("hour1", hour1);
					df_obj.put("hour2", hour2);
					df_obj.put("hour3", hour3);
					df_obj.put("hour4", hour4);
					df_obj.put("hour5", hour5);
					list.add(df_obj);
				}
				if(gn_hour1>0||gn_hour2>0||gn_hour3>0||gn_hour4>0){
				    ASObject gn_obj= new ASObject();
				    gn_obj.put("station", "����ֱ��");
				    gn_obj.put("hour1", gn_hour1);
				    gn_obj.put("hour2", gn_hour2);
				    gn_obj.put("hour3", gn_hour3);
				    gn_obj.put("hour4", gn_hour4);
				    gn_obj.put("hour5", gn_hour5);
				    list.add(gn_obj);
				}
				if(gw_hour1>0||gw_hour2>0||gw_hour3>0||gw_hour4>0){
					ASObject gw_obj= new ASObject();
					gw_obj.put("station", "����ֱ��");
					gw_obj.put("hour1", gw_hour1);
					gw_obj.put("hour2", gw_hour2);
					gw_obj.put("hour3", gw_hour3);
					gw_obj.put("hour4", gw_hour4);
					gw_obj.put("hour5", gw_hour5);
					list.add(gw_obj);
				}
				if(hw_hour1>0||hw_hour2>0||hw_hour3>0||hw_hour4>0){
					ASObject hw_obj= new ASObject();
					hw_obj.put("station", "����ת��");
					hw_obj.put("hour1", hw_hour1);
					hw_obj.put("hour2", hw_hour2);
					hw_obj.put("hour3", hw_hour3);
					hw_obj.put("hour4", hw_hour4);
					hw_obj.put("hour5", hw_hour5);
					list.add(hw_obj);
				}
				
				
				
				
				ASObject total_obj= new ASObject();
				total_obj.put("station", "�ܼ�");
				total_obj.put("hour1", total_hour1);
				total_obj.put("hour2", total_hour2);
				total_obj.put("hour3", total_hour3);
				total_obj.put("hour4", total_hour4);
				total_obj.put("hour5", total_hour5);
				list.add(total_obj);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return list;
	}
	
	
	/**
	 * ��ȡҪͳ�Ƶ�ת����������
	 * @param asobj
	 * @return
	 */
	public ArrayList getRedisseminators(ASObject asobj){
		ArrayList list = new ArrayList();
		String reportId  = (String)asobj.get("reportId");
		String sub_report_type  = (String)asobj.get("sub_report_type");
		String sql="select distinct transmit_station from  zrst_rep_effect_statistics_tab t " +
				"where t.sub_report_type='11' and t.transmit_station is not null and  t.play_time is not null and  t.report_id="+reportId;
		sql+=" order by transmit_station ";
		try {
			GDSet gd =DbComponent.Query(sql);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					ASObject obj=StationEffectHours(asobj,gd.getString(i, "transmit_station"),"1","102");
					list.add(obj);
				}
			}
			if(list.size()>0){
				//�ܼ�Ƶʱ������
				double total_hour1 = 0;//�ɱ�֤����Ƶʱ��
				double total_hour2=0;//����������Ƶʱ��
				double total_hour3=0;//��ʱ������Ƶʱ��
				double total_hour4=0;//��������Ƶʱ��
				
				for(int i=0;i<list.size();i++){
					
					ASObject obj1 = (ASObject) list.get(i);
					total_hour1+=Double.parseDouble(obj1.get("hour1").toString());
					total_hour2+=Double.parseDouble(obj1.get("hour2").toString());
					total_hour3+=Double.parseDouble(obj1.get("hour3").toString());
					total_hour4+=Double.parseDouble(obj1.get("hour4").toString());
					
				}
				ASObject total_obj= new ASObject();
				total_obj.put("station", "�ܼ�");
				total_obj.put("hour1", total_hour1);
				total_obj.put("hour2", total_hour2);
				total_obj.put("hour3", total_hour3);
				total_obj.put("hour4", total_hour4);
				list.add(total_obj);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return list;
	}
	/**
	 * ͳ��ÿ������̨���������Ƶʱ��
	 * @param obj
	 * @return
	 */
	public ASObject StationEffectHours(ASObject asobj,String stationName,String station_type,String runplanType){
		ASObject obj = new ASObject();
		String reportId  = (String)asobj.get("reportId");
		String sub_report_type  = (String)asobj.get("sub_report_type");
		String  sql1="";
//		if(runplanType.equalsIgnoreCase("101")){
//			  sql1  = "select distinct t.receive_name  from zrst_rep_effect_statistics_tab t ,zres_runplan_chaifen_tab zrt  ";
//	        sql1+="where t.sub_report_type='11' and zrt.is_delete=0 and zrt.runplan_id=t.runplan_id and t.transmit_station is not null and  t.play_time is not null and  t.report_id="+reportId;
//		}else{
			  sql1  = "select distinct t.receive_name  from zrst_rep_effect_statistics_tab t ";
	        sql1+="where t.sub_report_type='11'  and t.transmit_station is not null and  t.play_time is not null and  t.report_id="+reportId;
		//}
	
		double hour1=0;//�ɱ�֤����Ƶʱ��
		double hour2=0;//����������Ƶʱ��
		double hour3=0;//��ʱ������Ƶʱ��
		double hour4=0;//��������Ƶʱ��
		try {
			GDSet gd1 = DbComponent.Query(sql1);
			if(gd1.getRowCount()>0){
				for(int j=0;j<gd1.getRowCount();j++){
					String  sql="";
//					if(runplanType.equalsIgnoreCase("101")){
//						  sql  = "select distinct t.freq,t.language_name,t.play_time,t.transmit_station,t.receive_count,t.fen0,t.fen1,t.fen2,t.fen3,t.fen4,t.fen5,t.all_listens from zrst_rep_effect_statistics_tab t ,zres_runplan_chaifen_tab zrt ";
//				        sql+="where t.sub_report_type='11' and zrt.is_delete=0 and t.runplan_id=zrt.runplan_id and t.transmit_station is not null and  t.play_time is not null and t.transmit_station like '%"+stationName+"%' and t.receive_name like '%"+gd1.getString(j, "receive_name")+"%' and t.report_id="+reportId;
//				        sql+=" order by t.freq,t.play_time ";
//					}else{
						  sql  = "select distinct t.freq,t.language_name,t.play_time,t.transmit_station,t.receive_count,t.fen0,t.fen1,t.fen2,t.fen3,t.fen4,t.fen5,t.all_listens, t.service_area from zrst_rep_effect_statistics_tab t  ";
				        sql+="where t.sub_report_type='11'  and t.transmit_station is not null and  t.play_time is not null and t.transmit_station like '%"+stationName+"%' and t.receive_name like '%"+gd1.getString(j, "receive_name")+"%' and t.report_id="+reportId;
				        sql+=" order by t.freq,t.play_time,transmit_station ";
					//}
					
			        GDSet gd = DbComponent.Query(sql);
					if(gd.getRowCount()>0){
						for(int i=0;i<gd.getRowCount();i++){
							int counts=0;
							int count3=0;
							int count4=0;
							int count5=0;
							int listionRate=0;
							if(i<gd.getRowCount()-1){
								if(gd.getString(i, "play_time").equalsIgnoreCase(gd.getString(i+1, "play_time"))
										 &&gd.getString(i, "freq").equalsIgnoreCase(gd.getString(i+1, "freq"))
										  &&gd.getString(i, "language_name").equalsIgnoreCase(gd.getString(i+1, "language_name"))
										  &&gd.getString(i, "service_area").equalsIgnoreCase(gd.getString(i+1, "service_area"))
										    &&gd.getString(i, "transmit_station").equalsIgnoreCase(gd.getString(i+1, "transmit_station"))
									){
									continue;
								   }
								if(gd.getString(i, "play_time").split("-")[1].equalsIgnoreCase(gd.getString(i+1, "play_time").split("-")[0])
										&&gd.getString(i, "freq").equalsIgnoreCase(gd.getString(i+1, "freq"))
										&&gd.getString(i, "transmit_station").equalsIgnoreCase(gd.getString(i+1, "transmit_station"))
										&&gd.getString(i, "service_area").equalsIgnoreCase(gd.getString(i+1, "service_area"))
										&&gd.getString(i, "language_name").equalsIgnoreCase(gd.getString(i+1, "language_name")))
								{
									 counts=Integer.parseInt(gd.getString(i, "receive_count"))+Integer.parseInt(gd.getString(i+1, "receive_count"));//һ��Сʱ��Ԫ�����ղ����
									 count3 = Integer.parseInt(gd.getString(i, "fen3"))+ Integer.parseInt(gd.getString(i+1, "fen3"));//3�ֵĴ���
									 count4 = Integer.parseInt(gd.getString(i, "fen4"))+ Integer.parseInt(gd.getString(i+1, "fen4"));//3�ֵĴ���
									 count5 = Integer.parseInt(gd.getString(i, "fen5"))+ Integer.parseInt(gd.getString(i+1, "fen5"));//3�ֵĴ���
									 listionRate=Integer.parseInt(new BigDecimal((Double.parseDouble((count3+count4+count5)+"")*100/counts)).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
									// listionRate=(count3+count4+count5)*100/counts;
									if(listionRate>=80){//Ч���ǿɱ�֤����
										hour1 += 1.0;
									 } else if(listionRate>=60 && listionRate<80){//Ч���ǻ���������
										 hour2 += 1.0;
									 } else if(listionRate>=30 && listionRate<60){//Ч������ʱ������
										 hour3 += 1;
									 } else if(listionRate<30){//Ч���ǲ�������
										 hour4 += 1;
									 }
									i+=1;
								}else{
									 counts=Integer.parseInt(gd.getString(i, "receive_count"));//һ��Сʱ��Ԫ�����ղ����
									 count3 = Integer.parseInt(gd.getString(i, "fen3"));
									 count4 = Integer.parseInt(gd.getString(i, "fen4"));
									 count5 = Integer.parseInt(gd.getString(i, "fen5"));
									 listionRate=Integer.parseInt(new BigDecimal((Double.parseDouble((count3+count4+count5)+"")*100/counts)).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
									 //listionRate=(count3+count4+count5)*100/counts;
									if(listionRate>=80){//Ч���ǿɱ�֤����
										hour1 += 0.5;
									 } else if(listionRate>=60 && listionRate<80){//Ч���ǻ���������
										 hour2 += 0.5;
									 } else if(listionRate>=30 && listionRate<60){//Ч������ʱ������
										 hour3 += 0.5;
									 } else if(listionRate<30){//Ч���ǲ�������
										 hour4 += 0.5;
									 }
								}
							}else{
								counts=Integer.parseInt(gd.getString(i, "receive_count"));//һ��Сʱ��Ԫ�����ղ����
								 count3 = Integer.parseInt(gd.getString(i, "fen3"));
								 count4 = Integer.parseInt(gd.getString(i, "fen4"));
								 count5 = Integer.parseInt(gd.getString(i, "fen5"));
								 listionRate=Integer.parseInt(new BigDecimal((Double.parseDouble((count3+count4+count5)+"")*100/counts)).setScale(1,BigDecimal.ROUND_HALF_UP)+""); 
								// listionRate=(count3+count4+count5)*100/counts;
								if(listionRate>=80){//Ч���ǿɱ�֤����
									hour1 += 0.5;
								 } else if(listionRate>=60 && listionRate<80){//Ч���ǻ���������
									 hour2 += 0.5;
								 } else if(listionRate>=30 && listionRate<60){//Ч������ʱ������
									 hour3 += 0.5;
								 } else if(listionRate<30){//Ч���ǲ�������
									 hour4 += 0.5;
								 }
							}
							
						}
					}
				}
			}
			
			obj.put("station", stationName);
			obj.put("station_type", station_type);
			obj.put("hour1", hour1);
			obj.put("hour2", hour2);
			obj.put("hour3", hour3);
			obj.put("hour4", hour4);
			obj.put("hour5", hour1+hour2+hour3+hour4);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}
	
	/**
	 *ͳ��ÿ��վ����������
	 * @param asobj
	 * @return
	 */
	public ArrayList getHeadend(ASObject asobj,String runplanType){
		ArrayList list = new ArrayList();
		String reportId  = (String)asobj.get("reportId");
		String sub_report_type  = (String)asobj.get("sub_report_type");
		String sql="";
//		if(runplanType.equalsIgnoreCase("101")){
//			 sql="select distinct t.receive_name,dst.state_name,t.receive_code from  zrst_rep_effect_statistics_tab t,res_headend_tab res,dic_state_tab dst,zres_runplan_chaifen_tab zrt " +
//			"where t.sub_report_type='11' and zrt.is_delete=0 and t.runplan_id=zrt.runplan_id and t.receive_name is not null and res.shortname like '%'||t.receive_name||'%' " +
//			" and res.state=dst.state and res.is_delete=0   and  t.play_time is not null and  t.report_id="+reportId;
//	       sql+=" order by t.receive_code ";
//		}else{
			 sql="select distinct t.receive_name,dst.state_name,t.receive_code from  zrst_rep_effect_statistics_tab t,res_headend_tab res,dic_state_tab dst " +
				"where t.sub_report_type='11' and t.receive_name is not null and res.shortname like '%'||t.receive_name||'%' " +
				" and res.state=dst.state and res.is_delete=0   and  t.play_time is not null and  t.report_id="+reportId;
		       sql+=" order by t.receive_code ";
//		}
		
		
		try {
			GDSet gd =DbComponent.Query(sql);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					ASObject obj=HeadendEffectHours(asobj,gd.getString(i, "receive_name"),gd.getString(i, "state_name"),runplanType);
					list.add(obj);
				}
			}
			if(list.size()>0){
				HashMap map = new HashMap();
				
				//�ܼ�Ƶʱ������
				double total_hour1 = 0;//�ɱ�֤����Ƶʱ��
				double total_hour2=0;//����������Ƶʱ��
				double total_hour3=0;//��ʱ������Ƶʱ��
				double total_hour4=0;//��������Ƶʱ��
				for(int i=0;i<list.size();i++){
					double[] var= {0.0,0.0,0.0,0.0};
				    ASObject obj1 = (ASObject) list.get(i);
					String state = (String) obj1.get("state");
					String name = (String) obj1.get("ykz");
					total_hour1+=Double.parseDouble(obj1.get("hour1").toString());
					total_hour2+=Double.parseDouble(obj1.get("hour2").toString());
					total_hour3+=Double.parseDouble(obj1.get("hour3").toString());
					total_hour4+=Double.parseDouble(obj1.get("hour4").toString());
				    if(map.get(state)==null){
				      var[0]=0;
					  var[1]=0;
					  var[2]=0;
					  var[3]=0;
					  var[0]+=Double.parseDouble(obj1.get("hour1").toString());
					  var[1]+=Double.parseDouble(obj1.get("hour2").toString());
					  var[2]+=Double.parseDouble(obj1.get("hour3").toString());
					  var[3]+=Double.parseDouble(obj1.get("hour4").toString());
					  map.put(state, var);
				    }else{
					  var=(double[]) map.get(state);
					  var[0]+=Double.parseDouble(obj1.get("hour1").toString());
					  var[1]+=Double.parseDouble(obj1.get("hour2").toString());
					  var[2]+=Double.parseDouble(obj1.get("hour3").toString());
					  var[3]+=Double.parseDouble(obj1.get("hour4").toString());
					  map.put(state, var);
				    }
			    }
				
				Iterator iter = map.entrySet().iterator();
				while(iter.hasNext()){
					Map.Entry entry = (Map.Entry) iter.next();
					ASObject obj1 = new ASObject();
					obj1.put("ykz", entry.getKey().toString());
					obj1.put("hour1", ((double[])entry.getValue())[0]);
					obj1.put("hour2", ((double[])entry.getValue())[1]);
					obj1.put("hour3", ((double[])entry.getValue())[2]);
					obj1.put("hour4", ((double[])entry.getValue())[3]);
					list.add(obj1);
				}
				ASObject total_obj= new ASObject();
				total_obj.put("ykz", "ȫ��");
				total_obj.put("hour1", total_hour1);
				total_obj.put("hour2", total_hour2);
				total_obj.put("hour3", total_hour3);
				total_obj.put("hour4", total_hour4);
				list.add(total_obj);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return list;
	}
	
	/**
	 * ͳ��ÿ��վ�����������Ƶʱ��
	 * @param obj
	 * @return
	 */
	public ASObject HeadendEffectHours(ASObject asobj,String headname,String state,String runplanType){
		ASObject obj = new ASObject();
		String reportId  = (String)asobj.get("reportId");
		String sub_report_type  = (String)asobj.get("sub_report_type");
		double hour1 = 0;//�ɱ�֤����Ƶʱ��
		double hour2=0;//����������Ƶʱ��
		double hour3=0;//��ʱ������Ƶʱ��
		double hour4=0;//��������Ƶʱ��
		String sql ="";
		try {
//			if(runplanType.equalsIgnoreCase("101")){
//				sql  = "select distinct t.freq,t.language_name,t.play_time,t.transmit_station,t.receive_count,t.fen0,t.fen1,t.fen2,t.fen3,t.fen4,t.fen5,t.all_listens from zrst_rep_effect_statistics_tab t,zres_runplan_chaifen_tab zrt ";
//		        sql+="where t.sub_report_type='11' and zrt.is_delete=0 and t.runplan_id=zrt.runplan_id and t.transmit_station is not null and  t.play_time is not null  and t.receive_name like '%"+headname+"%' and t.report_id="+reportId;
//		        sql+=" order by t.freq,t.play_time ";
			///}else{
				sql  = "select distinct t.freq,t.language_name,t.play_time,t.transmit_station,t.receive_count,t.fen0,t.fen1,t.fen2,t.fen3,t.fen4,t.fen5,t.all_listens from zrst_rep_effect_statistics_tab t ";
		        sql+="where t.sub_report_type='11'  and t.transmit_station is not null and  t.play_time is not null  and t.receive_name like '%"+headname+"%' and t.report_id="+reportId;
		        sql+=" order by t.freq,t.play_time,transmit_station ";
			//}
			  
			        GDSet gd = DbComponent.Query(sql);
					if(gd.getRowCount()>0){
						for(int i=0;i<gd.getRowCount();i++){
							int counts=0;
							int count3=0;
							int count4=0;
							int count5=0;
							int listionRate=0;
							if(i<gd.getRowCount()-1){
								if(gd.getString(i, "play_time").equalsIgnoreCase(gd.getString(i+1, "play_time"))
								 &&gd.getString(i, "freq").equalsIgnoreCase(gd.getString(i+1, "freq"))
								  &&gd.getString(i, "language_name").equalsIgnoreCase(gd.getString(i+1, "language_name"))
								    &&gd.getString(i, "transmit_station").equalsIgnoreCase(gd.getString(i+1, "transmit_station"))
								){
									continue;
								}
								if(gd.getString(i, "play_time").split("-")[1].equalsIgnoreCase(gd.getString(i+1, "play_time").split("-")[0])
										&&gd.getString(i, "freq").equalsIgnoreCase(gd.getString(i+1, "freq"))
										&&gd.getString(i, "transmit_station").equalsIgnoreCase(gd.getString(i+1, "transmit_station"))
										&&gd.getString(i, "language_name").equalsIgnoreCase(gd.getString(i+1, "language_name"))){
									 counts=Integer.parseInt(gd.getString(i, "receive_count"))+Integer.parseInt(gd.getString(i+1, "receive_count"));//һ��Сʱ��Ԫ�����ղ����
									 count3 = Integer.parseInt(gd.getString(i, "fen3"))+ Integer.parseInt(gd.getString(i+1, "fen3"));//3�ֵĴ���
									 count4 = Integer.parseInt(gd.getString(i, "fen4"))+ Integer.parseInt(gd.getString(i+1, "fen4"));//3�ֵĴ���
									 count5 = Integer.parseInt(gd.getString(i, "fen5"))+ Integer.parseInt(gd.getString(i+1, "fen5"));//3�ֵĴ���
									 listionRate=Integer.parseInt(new BigDecimal(Double.parseDouble((count3+count4+count5)+"")*100/counts).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
									if(listionRate>=80){//Ч���ǿɱ�֤����
										hour1 += 1.0;
									 } else if(listionRate>=60 && listionRate<80){//Ч���ǻ���������
										 hour2 += 1.0;
									 } else if(listionRate>=30 && listionRate<60){//Ч������ʱ������
										 hour3 += 1;
									 } else if(listionRate<30){//Ч���ǲ�������
										 hour4 += 1;
									 }
									i+=1;
								}else{
									 counts=Integer.parseInt(gd.getString(i, "receive_count"));//һ��Сʱ��Ԫ�����ղ����
									 count3 = Integer.parseInt(gd.getString(i, "fen3"));
									 count4 = Integer.parseInt(gd.getString(i, "fen4"));
									 count5 = Integer.parseInt(gd.getString(i, "fen5"));
									 listionRate=Integer.parseInt(new BigDecimal(Double.parseDouble((count3+count4+count5)+"")*100/counts).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
									if(listionRate>=80){//Ч���ǿɱ�֤����
										hour1 += 0.5;
									 } else if(listionRate>=60 && listionRate<80){//Ч���ǻ���������
										 hour2 += 0.5;
									 } else if(listionRate>=30 && listionRate<60){//Ч������ʱ������
										 hour3 += 0.5;
									 } else if(listionRate<30){//Ч���ǲ�������
										 hour4 += 0.5;
									 }
								}
							}else{
								counts=Integer.parseInt(gd.getString(i, "receive_count"));//һ��Сʱ��Ԫ�����ղ����
								 count3 = Integer.parseInt(gd.getString(i, "fen3"));
								 count4 = Integer.parseInt(gd.getString(i, "fen4"));
								 count5 = Integer.parseInt(gd.getString(i, "fen5"));
								// listionRate=(count3+count4+count5)*100/counts; 
								 listionRate=Integer.parseInt(new BigDecimal(Double.parseDouble((count3+count4+count5)+"")*100/counts).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
								if(listionRate>=80){//Ч���ǿɱ�֤����
									hour1 += 0.5;
								 } else if(listionRate>=60 && listionRate<80){//Ч���ǻ���������
									 hour2 += 0.5;
								 } else if(listionRate>=30 && listionRate<60){//Ч������ʱ������
									 hour3 += 0.5;
								 } else if(listionRate<30){//Ч���ǲ�������
									 hour4 += 0.5;
								 }
							}
							
						}
			}
			
			obj.put("ykz", headname);
			obj.put("state", state);
			obj.put("hour1", hour1);
			obj.put("hour2", hour2);
			obj.put("hour3", hour3);
			obj.put("hour4", hour4);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}
	
	public void doExcel(String msg,HttpServletRequest request,HttpServletResponse response) throws Exception{
		Element root = StringTool.getXMLRoot(msg);
		String reportId = root.getChildText("reportId");
		String report_type = root.getChildText("report_type");//��������  1������̨Ч������ͳ��  2���������Ч������ͳ��
		String starttime = root.getChildText("starttime").replaceAll("-", "");
		String endtime = root.getChildText("endtime").replaceAll("-", "");
		if(report_type.equals("1")){
			doExcelGJT(msg,request,response);
		} else{
			doExcelHWLD(msg,request,response);
		}
		
//	    	jExcel.saveDocument();
	    	
//	        InputStream inputStream = new FileInputStream(downFileName);
//	        byte[] buffer = new byte[1024];
//	        int i = -1;
//	        while ( (i = inputStream.read(buffer)) != -1) {
//	          outputStream.write(buffer, 0, i);
//	        }
//	        outputStream.flush();
//	        outputStream.close();
//	        outputStream = null;


	}
	
    /**
     * ��������̨Ч��ͳ��
     * @detail  
     * @method  
     * @param msg
     * @param request
     * @param response
     * @throws Exception 
     * @return  void  
     * @author  zhaoyahui
     * @version 2013-5-21 ����04:50:30
     */
    private void doExcelGJT(String msg,HttpServletRequest request,HttpServletResponse response) throws Exception{
    	Element root = StringTool.getXMLRoot(msg);
		String reportId = root.getChildText("reportId");
		String report_type = root.getChildText("report_type");//��������  1������̨Ч������ͳ��  2���������Ч������ͳ��
		String starttime = root.getChildText("starttime").replaceAll("-", "");
		String endtime = root.getChildText("endtime").replaceAll("-", "");
    	String fileName="����վ�����̨Ч��ͳ�Ʊ�";
		if(starttime.equals(endtime)){
			fileName = "����վ��"+starttime+"�ڹ���̨Ч��ͳ�Ʊ�";
		} else if(starttime.substring(0, 6).equals(endtime.substring(0, 6))){
			fileName = "����վ��"+starttime.substring(0, 6)+"�ڹ���̨Ч��ͳ�Ʊ�";
		} else{
			fileName = "����վ��"+starttime.substring(0, 4)+"�ڹ���̨Ч��ͳ�Ʊ�";
		}
		ASObject obj = new ASObject();
		obj.put("reportId", reportId);
		obj.put("sub_report_type","11");
		ArrayList list = (ArrayList) queryDetailReport(obj);
		
		ArrayList list1 = (ArrayList) queryDetailReport1(obj);
		
		String  station_listens = queryStation_ZListens(obj);
		ArrayList  language_listens = queryLanguage_ZListens(obj);
		ArrayList  ykz_listens = queryYkz_ZListens(obj);
		ArrayList  frqList = queryFreqListen(reportId,"71");
		OutputStream outputStream = response.getOutputStream();
		response.setContentType("application/vnd.ms-excel");
		response.reset();
		response.setHeader("Location", "Export.xls");
		response.setHeader("Expires", "0");
//		response.setHeader("Content-Disposition",new String(("filename="+fileName).getBytes("GBK"),"ISO-8859-1")+".xls");
        response.setHeader("Content-disposition", "attachment; filename="+new String((fileName).getBytes("GBK"),"ISO-8859-1")+".xls");
        
		WritableWorkbook wwb =Workbook.createWorkbook(outputStream);
		int sheetNum = 0;
		//�ӱ�������  11������̨�㲥Ч��ͳ�ƣ�21������̨���岥��Ч��ͳ��1��22������̨���岥��Ч��ͳ��2��23������̨���岥��Ч��ͳ��3��31���������岥��Ч��ͳ��1��
		//32���������岥��Ч��ͳ��2��41����ң��վ���������������ޡ�������ͳ�ƣ�51����ң��վ���������������ޡ��ɱ�֤����Ƶʱͳ�ƣ�61�����¿����ʶԱȣ�71��Ƶ��ƽ��������ͳ�Ʊ�
		
		 WritableFont wfTitle = new WritableFont(WritableFont.createFont("����"), 12,WritableFont.BOLD, false);
         WritableCellFormat wcfFTitle = new WritableCellFormat(wfTitle);
		wcfFTitle.setAlignment(jxl.format.Alignment.CENTRE);
		wcfFTitle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			 
		WritableFont wfHead = new WritableFont(WritableFont.createFont("����"), 10,WritableFont.BOLD, false);
		WritableCellFormat wcfFHead = new WritableCellFormat(wfHead);
		wcfFHead.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);//���߿� 
		wcfFHead.setWrap(true);//�Զ�����
         //����Number�ĸ�ʽ
         //jxl.write.NumberFormat nf = new jxl.write.NumberFormat("0.00"); 
         //jxl.write.WritableCellFormat priceformat = new jxl.write.WritableCellFormat(nf);
         // ��ˮƽ���뷽ʽָ��Ϊ�����
		wcfFHead.setAlignment(jxl.format.Alignment.CENTRE);
         // �Ѵ�ֱ���뷽ʽָ��Ϊ���ж���
		wcfFHead.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		
		WritableFont wf1 = new WritableFont(WritableFont.createFont("����"), 10,WritableFont.NO_BOLD, false);
        jxl.write.WritableCellFormat wcfF2 = new jxl.write.WritableCellFormat(wf1);
        wcfF2.setAlignment(jxl.format.Alignment.CENTRE);
        wcfF2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        wcfF2.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); 
        
        jxl.write.WritableCellFormat wcfF3 = new jxl.write.WritableCellFormat(wf1);
        wcfF3.setAlignment(jxl.format.Alignment.CENTRE);
        wcfF3.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        wcfF3.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); 
        wcfF3.setBackground(Colour.YELLOW2);//���õ�Ԫ�񱳾�ɫ
        
		HashMap stationTotal1Map = new HashMap();
		HashMap languageTotal1Map = new HashMap();
		HashMap head22Map = new HashMap();
		HashMap head32Map = new HashMap();
		int beginRow22 = 0;
		int beginRow23 = 0;
		int beginRow32 = 0;
		int beginRow51 = 0;
		int beginRow61 = 0;
		int beginRow71 = 0;
		ZrstRepEffectStatisticsBean totalBean = new ZrstRepEffectStatisticsBean();
		for(int j=0;j<list1.size();j++){
			ZrstRepEffectStatisticsBean bean = (ZrstRepEffectStatisticsBean)list1.get(j);
			WritableSheet ws =  null;
			if(wwb.getNumberOfSheets() == 0 || wwb.getSheet(bean.getReceive_name()) == null){
				ws = wwb.createSheet(bean.getReceive_name(), wwb.getNumberOfSheets()+1);
                 //��������
                 ws.addCell(new Label(0,1,"����ʱ��",wcfFHead));
                 ws.addCell(new Label(1,1,"����",wcfFHead));
                 ws.addCell(new Label(2,1,"Ƶ��",wcfFHead));
                 ws.addCell(new Label(3,1,"����̨",wcfFHead));
                 ws.addCell(new Label(4,1,"���䷽��",wcfFHead));
                 ws.addCell(new Label(5,1,"���书��",wcfFHead));
                 ws.addCell(new Label(6,1,"������",wcfFHead));
                 ws.addCell(new Label(7,1,"�ղ�ص�",wcfFHead));
                 ws.addCell(new Label(8,1,"�ղ����",wcfFHead));
                 ws.addCell(new Label(9,1,"5",wcfFHead));
                 ws.addCell(new Label(10,1,"4",wcfFHead));
                 ws.addCell(new Label(11,1,"3",wcfFHead));
                 ws.addCell(new Label(12,1,"2",wcfFHead));
                 ws.addCell(new Label(13,1,"1",wcfFHead));
                 ws.addCell(new Label(14,1,"0",wcfFHead));
                 ws.addCell(new Label(15,1,"������%",wcfFHead));
                 ws.addCell(new Label(16,1,"��������ֵ",wcfFHead));
                 ws.addCell(new Label(17,1,"��ע",wcfFHead));

                 ws.mergeCells(0,0,17,0);
                
                 ws.addCell(new Label(0, 0, bean.getReceive_code()+bean.getReceive_name()+"ң��վ����̨�㲥Ч��ͳ�Ʊ�",wcfFTitle));
                 ws.setRowView(0, 740);//���õ�һ�и߶�
                 ws.setRowView(1, 550); 
                 ws.setColumnView(0, 13);
                 ws.setColumnView(1, 5);
                 ws.setColumnView(2, 7);
                 ws.setColumnView(3, 9);
                 ws.setColumnView(4, 5);
                 ws.setColumnView(5, 5);
                 ws.setColumnView(6, 11);
                 ws.setColumnView(7, 7);
                 ws.setColumnView(8, 7);
                 ws.setColumnView(9, 6);
                 ws.setColumnView(10, 5);
                 ws.setColumnView(11, 5);
                 ws.setColumnView(12, 5);
                 ws.setColumnView(13, 5);
                 ws.setColumnView(14, 5);
                 ws.setColumnView(15, 6);
                 ws.setColumnView(16, 8);
                 ws.setColumnView(17, 28);
                 
			} else  {
				ws = wwb.getSheet(bean.getReceive_name()); 
			}
			int curRow = ws.getRows();
			int nextRow = curRow;
			if(bean.getPlay_time().equals("����Ч��")){
				totalBean = bean;
				ws.mergeCells(0,nextRow,7,nextRow);
				ws.addCell(new Label(0,nextRow, "����Ч��",wcfF2));
				ws.addCell(new jxl.write.Number(8,nextRow, Integer.parseInt(totalBean.getReceive_count()),wcfF2));
				ws.addCell(new jxl.write.Number(9,nextRow, Integer.parseInt(totalBean.getFen5()),wcfF2));
				ws.addCell(new jxl.write.Number(10,nextRow, Integer.parseInt(totalBean.getFen4()),wcfF2));
				ws.addCell(new jxl.write.Number(11,nextRow, Integer.parseInt(totalBean.getFen3()),wcfF2));
				ws.addCell(new jxl.write.Number(12,nextRow, Integer.parseInt(totalBean.getFen2()),wcfF2));
				ws.addCell(new jxl.write.Number(13,nextRow, Integer.parseInt(totalBean.getFen1()),wcfF2));
				ws.addCell(new jxl.write.Number(14,nextRow, Integer.parseInt(totalBean.getFen0()),wcfF2));
				ws.addCell(new jxl.write.Number(15,nextRow, Integer.parseInt(totalBean.getListen()),wcfF2));
				ws.addCell(new Label(16,nextRow, totalBean.getListen_middle(),wcfF2));
				ws.addCell(new Label(17,nextRow, totalBean.getBak(),wcfF2));
			}else{
				ws.addCell(new Label(0,curRow, bean.getPlay_time(),wcfF2));
				ws.addCell(new Label(1,curRow, bean.getLanguage_name(),wcfF2));
				ws.addCell(new Label(2,curRow, bean.getFreq(),wcfF2));
				ws.addCell(new Label(3,curRow, bean.getTransmit_station(),wcfF2));
				ws.addCell(new Label(4,curRow, bean.getTransmit_direction(),wcfF2));
				ws.addCell(new Label(5,curRow, bean.getTransmit_power(),wcfF2));
				ws.addCell(new Label(6,curRow, bean.getService_area(),wcfF2));
				ws.addCell(new Label(7,curRow, bean.getReceive_code(),wcfF2));
				ws.addCell(new jxl.write.Number(8,curRow, Integer.parseInt(bean.getReceive_count()),wcfF2));
//				ws.addCell(new Label(9,curRow, bean.getFen5(),wcfF2));
//				ws.addCell(new Label(10,curRow, bean.getFen4(),wcfF2));
//				ws.addCell(new Label(11,curRow, bean.getFen3(),wcfF2));
//				ws.addCell(new Label(12,curRow, bean.getFen2(),wcfF2));
//				ws.addCell(new Label(13,curRow, bean.getFen1(),wcfF2));
//				ws.addCell(new Label(14,curRow, bean.getFen0(),wcfF2));
				ws.addCell(new jxl.write.Number(9,curRow, Integer.parseInt(bean.getFen5()),wcfF2));//ת�������ָ�ʽ
				ws.addCell(new jxl.write.Number(10,curRow, Integer.parseInt(bean.getFen4()),wcfF2));
				ws.addCell(new jxl.write.Number(11,curRow, Integer.parseInt(bean.getFen3()),wcfF2));
				ws.addCell(new jxl.write.Number(12,curRow, Integer.parseInt(bean.getFen2()),wcfF2));
				ws.addCell(new jxl.write.Number(13,curRow, Integer.parseInt(bean.getFen1()),wcfF2));
				ws.addCell(new jxl.write.Number(14,curRow, Integer.parseInt(bean.getFen0()),wcfF2));
				if(bean.getListen()!=null&&!bean.getListen().equalsIgnoreCase("")){
					if(Integer.parseInt(bean.getListen())<60){
						ws.addCell(new jxl.write.Number(15,curRow, Integer.parseInt(bean.getListen()),wcfF3));
					}else{
						ws.addCell(new jxl.write.Number(15,curRow, Integer.parseInt(bean.getListen()),wcfF2));
					}
				}else{
					ws.addCell(new jxl.write.Number(15,curRow, Integer.parseInt(bean.getListen()),wcfF2));
				}
				
				
				ws.addCell(new Label(16,curRow, bean.getListen_middle(),wcfF2));
				ws.addCell(new Label(17,curRow, bean.getBak(),wcfF2));
			}
		}
		for(int i=0;i<list.size();i++){
			ZrstRepEffectStatisticsBean bean = (ZrstRepEffectStatisticsBean)list.get(i);
			WritableSheet ws =  null;

			if(bean.getSub_report_type().equals("21")){//����̨���岥��Ч��ͳ��1
				
				String name = "����̨";
				if(bean.getReport_type().equals("2")){
					name = "ת������";
				}
				if(wwb.getNumberOfSheets() == 0 || wwb.getSheet(name+"���岥��Ч��ͳ��") == null){
					ws = wwb.createSheet(name+"���岥��Ч��ͳ��", wwb.getNumberOfSheets()+1);
	                ws.addCell(new Label(0,1,"ң��վ",wcfFHead));
	                ws.mergeCells(0,1,0,2);//��ʼ�У���ʼ�У������У�������
	                 
	                for(int ai=0;ai<list.size();ai++){
						ZrstRepEffectStatisticsBean aibean = (ZrstRepEffectStatisticsBean)list.get(ai);
						//����̨_>=3��_�ܴ���_������%  ���磺2022_88_123_78,2032_23_423_28
		 				String[] headStationArr = aibean.getTransmit_station_listens().split(",");
		 				for(String ones:headStationArr){
		 					if( aibean.getSub_report_type().equals("21")){

								if(stationTotal1Map.get(ones.split("_")[0]) == null){
									stationTotal1Map.put(ones.split("_")[0],ws.getColumns());
					                 ws.addCell(new Label((Integer)stationTotal1Map.get(ones.split("_")[0]),2,">=3��",wcfFHead));
					                 ws.addCell(new Label(ws.getColumns(),2,"�ܴ���",wcfFHead));
					                 ws.addCell(new Label(ws.getColumns(),2,"������%",wcfFHead));
					                 ws.mergeCells((Integer)stationTotal1Map.get(ones.split("_")[0]),1,((Integer)stationTotal1Map.get(ones.split("_")[0])+2),1);
					                 ws.addCell(new Label((Integer)stationTotal1Map.get(ones.split("_")[0]),1,ones.split("_")[0],wcfFHead));
								}
		 					}
		 				}
					} 
				} else{
					ws = wwb.getSheet(name+"���岥��Ч��ͳ��");
				}
				
				
				String[] headStationArr = bean.getTransmit_station_listens().split(",");
				int currRow = ws.getRows();
 				for(String ones:headStationArr){
 						String showName = bean.getReceive_name();
 						if(showName == null || showName.equals(""))
 							showName = bean.getService_area();
 						if(showName == null || showName.equals(""))
 							showName = bean.getState_name();
 						if(showName == null || showName.equals(""))
 							showName = "�ܼ�";
 						
 						 ws.addCell(new Label(0,currRow,showName,wcfFHead));
 						 for(int yy=1;yy<ws.getColumns();yy++){
 							if(ws.getCell(yy, currRow).getContents().equals("")){
 								ws.addCell(new Label(yy, currRow,"",wcfF2));
 							}
 							 if((Integer)stationTotal1Map.get(ones.split("_")[0]) == yy){
 								 ws.addCell(new Label((Integer)stationTotal1Map.get(ones.split("_")[0]), currRow,ones.split("_").length==4?ones.split("_")[1]:"",wcfF2));
 							 } 
 							 else if((Integer)stationTotal1Map.get(ones.split("_")[0])+1 == yy){
 								 ws.addCell(new Label((Integer)stationTotal1Map.get(ones.split("_")[0])+1, currRow, ones.split("_").length==4?ones.split("_")[2]:"",wcfF2));
 							 }
 							 else if((Integer)stationTotal1Map.get(ones.split("_")[0])+2 == yy){
 								 ws.addCell(new Label((Integer)stationTotal1Map.get(ones.split("_")[0])+2, currRow, ones.split("_").length==4?ones.split("_")[3]+"%":"",wcfF2));
 							 }
 						 }
 				}
			} else if(bean.getSub_report_type().equals("22")){//����̨���岥��Ч��ͳ��2
 				String name = "����̨";
				if(bean.getReport_type().equals("2")){
					name = "ת������";
				}
				ws = wwb.getSheet(name+"���岥��Ч��ͳ��");
				ws.setColumnView(0, 15);
				if(beginRow22 == 0){
					beginRow22 = ws.getRows()+2;
				}
				ws.addCell(new Label(0,beginRow22,name+"\\ң��վ",wcfFHead));
				
				String[] transmitStationArr = bean.getReceive_name_total_hours().split(",");
//				System.out.println("bean.getReceive_name_total_hours()=="+bean.getReceive_name_total_hours());
				if(transmitStationArr.length>0){
					ws.addCell(new Label(0,ws.getRows(),bean.getTransmit_station(),wcfF2));
				}
				for(String transmitStation:transmitStationArr){
					if(transmitStation.equals(""))
						continue;
//					System.out.println("transmitStation=="+transmitStation);
					String[] receiveVal = transmitStation.split("_");
					if(head22Map.get(receiveVal[0]) == null){
						int colNum = 0;
						for(Object o:head22Map.keySet()){
							colNum = colNum>=(Integer)head22Map.get(o)?colNum:(Integer)head22Map.get(o);   // Map��ֵ
						}
						if(colNum == 0){
							for(int ai=0;ai<list.size();ai++){
								ZrstRepEffectStatisticsBean aibean = (ZrstRepEffectStatisticsBean)list.get(ai);
								//ң��վ��_��Ƶʱ  ���磺����_96,��¡��_34
				 				String[] headStationArr = aibean.getReceive_name_total_hours().split(",");
				 				for(String ones:headStationArr){
				 					if( aibean.getSub_report_type().equals("22")){
//						 				System.out.println(aibean.getReceive_name_total_hours()+"<<<<<<<<>>"+bean.getSub_report_type());
						 				if(colNum == 0){
						 					colNum++;
											head22Map.put(ones.split("_")[0],colNum);
						 				} else if(head22Map.get(ones.split("_")[0]) == null){
						 					for(Object o:head22Map.keySet()){
												colNum = colNum>=(Integer)head22Map.get(o)?colNum:(Integer)head22Map.get(o);   // Map��ֵ
											}
											head22Map.put(ones.split("_")[0],colNum+1);
										}

										ws.addCell(new Label((Integer)head22Map.get(ones.split("_")[0]),ws.getRows()-2,ones.split("_")[0],wcfFHead));
				 					}
				 				}
							}
						}
					} else{
					}
					
						if(Integer.parseInt(receiveVal[1].equals("A")?"100":receiveVal[1])<60){
							ws.addCell(new Label((Integer)head22Map.get(receiveVal[0]),ws.getRows()-1,(receiveVal[1].equals("A")?"":receiveVal[1]+"%"),wcfF3));
						}else{
							ws.addCell(new Label((Integer)head22Map.get(receiveVal[0]),ws.getRows()-1,(receiveVal[1].equals("A")?"":receiveVal[1]+"%"),wcfF2));
						}
				}
				
			} 
			else if(bean.getSub_report_type().equals("31")){//�������岥��Ч��ͳ��1
				if(wwb.getNumberOfSheets() == 0 || wwb.getSheet("�������岥��Ч��ͳ��") == null){
					ws = wwb.createSheet("�������岥��Ч��ͳ��", wwb.getNumberOfSheets()+1);
	                ws.addCell(new Label(0,1,"ң��վ",wcfFHead));
	                ws.mergeCells(0,1,0,2);//��ʼ�У���ʼ�У������У�������
	                 
	                for(int ai=0;ai<list.size();ai++){
						ZrstRepEffectStatisticsBean aibean = (ZrstRepEffectStatisticsBean)list.get(ai);
						//����_>=3��_�ܴ���_������%  ���磺��_88_123_78,2032_23_423_28
		 				String[] headLanguageArr = aibean.getLanguage_name_listens().split(",");
		 				for(String ones:headLanguageArr){
		 					if( aibean.getSub_report_type().equals("31")){
								if(languageTotal1Map.get(ones.split("_")[0]) == null){
									languageTotal1Map.put(ones.split("_")[0],ws.getColumns());
					                 ws.addCell(new Label((Integer)languageTotal1Map.get(ones.split("_")[0]),2,">=3��",wcfFHead));
					                 ws.addCell(new Label(ws.getColumns(),2,"�ܴ���",wcfFHead));
					                 ws.addCell(new Label(ws.getColumns(),2,"������%",wcfFHead));
					                 ws.mergeCells((Integer)languageTotal1Map.get(ones.split("_")[0]),1,((Integer)languageTotal1Map.get(ones.split("_")[0])+2),1);
					                 ws.addCell(new Label((Integer)languageTotal1Map.get(ones.split("_")[0]),1,ones.split("_")[0],wcfFHead));
								}
		 					}
		 				}
					} 
				} else{
					ws = wwb.getSheet("�������岥��Ч��ͳ��");
				}
				
				
				String[] headLanguageArr = bean.getLanguage_name_listens().split(",");
				int currRow = ws.getRows();
 				for(String ones:headLanguageArr){
//	                 System.out.println("ones.split()=="+ones);
 						String showName = bean.getReceive_name();
 						if(showName == null || showName.equals(""))
 							showName = bean.getService_area();
 						if(showName == null || showName.equals(""))
 							showName = bean.getState_name();
 						if(showName == null || showName.equals(""))
 							showName = "�ܼ�";
 						
 						 ws.addCell(new Label(0,currRow,showName,wcfFHead));
 						 for(int yy=1;yy<ws.getColumns();yy++){
 							if(ws.getCell(yy, currRow).getContents().equals("")){
 								ws.addCell(new Label(yy, currRow,"",wcfF2));
 							}
 							 if((Integer)languageTotal1Map.get(ones.split("_")[0]) == yy){
 								 ws.addCell(new Label((Integer)languageTotal1Map.get(ones.split("_")[0]), currRow,ones.split("_").length==4?ones.split("_")[1]:"",wcfF2));
 							 } 
 							 else if((Integer)languageTotal1Map.get(ones.split("_")[0])+1 == yy){
 								 ws.addCell(new Label((Integer)languageTotal1Map.get(ones.split("_")[0])+1, currRow, ones.split("_").length==4?ones.split("_")[2]:"",wcfF2));
 							 }
 							 else if((Integer)languageTotal1Map.get(ones.split("_")[0])+2 == yy){
 								 ws.addCell(new Label((Integer)languageTotal1Map.get(ones.split("_")[0])+2, currRow, ones.split("_").length==4?ones.split("_")[3]+"%":"",wcfF2));
 							 }
// 							 else{
// 								 ws.addCell(new Label(yy, ws.getRows()-1, "",wcfF2));
// 							 }
 						 }
 				}
			} else if(bean.getSub_report_type().equals("32")){//�������岥��Ч��ͳ��2
				ws = wwb.getSheet("�������岥��Ч��ͳ��");
				ws.setColumnView(0, 15);
				if(beginRow32 == 0){
					beginRow32 = ws.getRows()+2;
				}
				ws.addCell(new Label(0,beginRow32,"����\\ң��վ",wcfFHead));
				
				String[] languageArr = bean.getReceive_name_total_hours().split(",");
//				System.out.println("bean.getReceive_name_total_hours()=="+bean.getReceive_name_total_hours());
				if(languageArr.length>0){
					ws.addCell(new Label(0,ws.getRows(),bean.getLanguage_name(),wcfF2));
				}
				for(String language:languageArr){
					if(language.equals(""))
						continue;
//					System.out.println("language=="+language);
					String[] receiveVal = language.split("_");
					if(head32Map.get(receiveVal[0]) == null){
						int colNum = 0;
						for(Object o:head32Map.keySet()){
							colNum = colNum>=(Integer)head32Map.get(o)?colNum:(Integer)head32Map.get(o);   // Map��ֵ
						}
						if(colNum == 0){
							for(int ai=0;ai<list.size();ai++){
								ZrstRepEffectStatisticsBean aibean = (ZrstRepEffectStatisticsBean)list.get(ai);
								//ң��վ��_��Ƶʱ  ���磺����_96,��¡��_34
				 				String[] headStationArr = aibean.getReceive_name_total_hours().split(",");
				 				for(String ones:headStationArr){
				 					if( aibean.getSub_report_type().equals("32")){

//						 				System.out.println(aibean.getReceive_name_total_hours()+"<<<<<<<<>>"+bean.getSub_report_type());
						 				if(colNum == 0){
						 					colNum++;
						 					head32Map.put(ones.split("_")[0],colNum);
						 				} else if(head32Map.get(ones.split("_")[0]) == null){
						 					for(Object o:head32Map.keySet()){
												colNum = colNum>=(Integer)head32Map.get(o)?colNum:(Integer)head32Map.get(o);   // Map��ֵ
											}
						 					head32Map.put(ones.split("_")[0],colNum+1);
										}

										ws.addCell(new Label((Integer)head32Map.get(ones.split("_")[0]),ws.getRows()-2,ones.split("_")[0],wcfFHead));
				 					}
				 				}
							}
						}
//						head32Map.put(receiveVal[0],colNum+1);
					} else{
					}
					if(Integer.parseInt(receiveVal[1].equals("A")?"100":receiveVal[1])<60){
						ws.addCell(new Label((Integer)head32Map.get(receiveVal[0]),ws.getRows()-1,(receiveVal[1].equals("A")?"":receiveVal[1]+"%"),wcfF3));
					}else{
						ws.addCell(new Label((Integer)head32Map.get(receiveVal[0]),ws.getRows()-1,(receiveVal[1].equals("A")?"":receiveVal[1]+"%"),wcfF2));
					}
				//	ws.addCell(new Label((Integer)head32Map.get(receiveVal[0]),ws.getRows()-1,(receiveVal[1].equals("A")?"":receiveVal[1]+"%"),wcfF2));
				}
			} 
			else if(bean.getSub_report_type().equals("41")){//��������ÿ�����޷�Ϊ�����������������޺�ȫ���ղ������3�����ϴ����Ϳ�����ͳ��
				if(wwb.getNumberOfSheets() == 0 || wwb.getSheet("����ͳ��") == null){
					ws = wwb.createSheet("����ͳ��", wwb.getNumberOfSheets()+1);
	                ws.addCell(new Label(0,0,"������������",wcfFTitle));
	                ws.mergeCells(0,0,3,0);//��ʼ�У���ʼ�У������У�������
	                ws.addCell(new Label(0,1,"����",wcfFHead));
	                ws.addCell(new Label(1,1,"3�����ϴ���",wcfFHead));
	                ws.addCell(new Label(2,1,"�ܴ���",wcfFHead));
	                ws.addCell(new Label(3,1,"������%",wcfFHead));
	                ws.setRowView(0, 740);//���õ�һ�и߶�
	                ws.setColumnView(0, 15);
	                ws.setColumnView(1, 16);
	                ws.setColumnView(2, 16);
	                ws.setColumnView(3, 16);
	                ws.setColumnView(4, 16);
	                ws.setColumnView(5, 13);
	                
	                String[] allListensArr = bean.getAll_listens().split(",");
//	                System.out.println("bean.getAll_listens()=="+bean.getAll_listens());
	                //����_>=3��_�ܴ���_������%
	                for(int ai=0;ai<allListensArr.length;ai++){
	                	String[] oneValArr = allListensArr[ai].split("_");
	                	int curRow = ws.getRows();
	                	ws.addCell(new Label(0,curRow,oneValArr[0],wcfF2));
		                 ws.addCell(new Label(1,curRow,oneValArr[1],wcfF2));
		                 ws.addCell(new Label(2,curRow,oneValArr[2],wcfF2));
		                 ws.addCell(new Label(3,curRow,oneValArr[3]+"%",wcfF2));
					} 
				} else{
					ws = wwb.getSheet("����ͳ��");
				}
				
			} 
			else if(bean.getSub_report_type().equals("61")){//���¿����ʶԱȣ�
				if(wwb.getNumberOfSheets() == 0 || wwb.getSheet("����ͳ��") == null){
					ws = wwb.createSheet("����ͳ��", wwb.getNumberOfSheets()+1);
	                ws.setColumnView(0, 15);
	                ws.setColumnView(1, 16);
	                ws.setColumnView(2, 16);
	                ws.setColumnView(3, 16);
	                ws.setColumnView(4, 16);
	                ws.setColumnView(5, 13);
				} else{
					ws = wwb.getSheet("����ͳ��");
				}
				if(beginRow61 == 0){
					beginRow61 = ws.getRows()+3;

	                ws.setRowView(beginRow61-1, 740);//���õ�һ�и߶�
					ws.addCell(new Label(0,beginRow61-1,"���¿����ʶԱ�",wcfFTitle));
	                ws.mergeCells(0,beginRow61-1,12,beginRow61-1);//��ʼ�У���ʼ�У������У�������
	                ws.addCell(new Label(0,beginRow61,"ң��վ",wcfFHead));
	                ws.addCell(new Label(1,beginRow61,"1��",wcfFHead));
	                ws.addCell(new Label(2,beginRow61,"2��",wcfFHead));
	                ws.addCell(new Label(3,beginRow61,"3��",wcfFHead));
	                ws.addCell(new Label(4,beginRow61,"4��",wcfFHead));
	                ws.addCell(new Label(5,beginRow61,"5��",wcfFHead));
	                ws.addCell(new Label(6,beginRow61,"6��",wcfFHead));
	                ws.addCell(new Label(7,beginRow61,"7��",wcfFHead));
	                ws.addCell(new Label(8,beginRow61,"8��",wcfFHead));
	                ws.addCell(new Label(9,beginRow61,"9��",wcfFHead));
	                ws.addCell(new Label(10,beginRow61,"10��",wcfFHead));
	                ws.addCell(new Label(11,beginRow61,"11��",wcfFHead));
	                ws.addCell(new Label(12,beginRow61,"12��",wcfFHead));
	                
	                ws.setRowView(0, 740);//���õ�һ�и߶�
//	                ws.setColumnView(0, 12);
//	                ws.setColumnView(1, 12);
//	                ws.setColumnView(2, 12);
//	                ws.setColumnView(3, 12);
//	                ws.setColumnView(4, 12);
//	                ws.setColumnView(5, 12);
	                ws.setColumnView(6, 12);
	                ws.setColumnView(7, 12);
	                ws.setColumnView(8, 12);
	                ws.setColumnView(9, 12);
	                ws.setColumnView(10, 12);
	                ws.setColumnView(11, 12);
				}
				String showName = bean.getReceive_name();
				if(showName == null || showName.equals(""))
					showName = bean.getState_name();
				if(showName == null || showName.equals(""))
					showName = bean.getService_area();
				if(showName == null || showName.equals(""))
					showName = "ȫ��";
//				System.out.println("bean.getMonth_listens()=="+bean.getMonth_listens());
				String[] month_listens = bean.getMonth_listens().split("_");
				ws.addCell(new Label(0,ws.getRows(),showName,wcfF2));
				ws.addCell(new Label(1,ws.getRows()-1,(month_listens[0].equals("$")?"":month_listens[0]+"%"),wcfF2));
				ws.addCell(new Label(2,ws.getRows()-1,(month_listens[1].equals("$")?"":month_listens[1]+"%"),wcfF2));
				ws.addCell(new Label(3,ws.getRows()-1,(month_listens[2].equals("$")?"":month_listens[2]+"%"),wcfF2));
				ws.addCell(new Label(4,ws.getRows()-1,(month_listens[3].equals("$")?"":month_listens[3]+"%"),wcfF2));
				ws.addCell(new Label(5,ws.getRows()-1,(month_listens[4].equals("$")?"":month_listens[4]+"%"),wcfF2));
				ws.addCell(new Label(6,ws.getRows()-1,(month_listens[5].equals("$")?"":month_listens[5]+"%"),wcfF2));
				ws.addCell(new Label(7,ws.getRows()-1,(month_listens[6].equals("$")?"":month_listens[6]+"%"),wcfF2));
				ws.addCell(new Label(8,ws.getRows()-1,(month_listens[7].equals("$")?"":month_listens[7]+"%"),wcfF2));
				ws.addCell(new Label(9,ws.getRows()-1,(month_listens[8].equals("$")?"":month_listens[8]+"%"),wcfF2));
				ws.addCell(new Label(10,ws.getRows()-1,(month_listens[9].equals("$")?"":month_listens[9]+"%"),wcfF2));
				ws.addCell(new Label(11,ws.getRows()-1,(month_listens[10].equals("$")?"":month_listens[10]+"%"),wcfF2));
				ws.addCell(new Label(12,ws.getRows()-1,(month_listens[11].equals("$")?"":month_listens[11]+"%"),wcfF2));
                
			} 
		}
		//����̨�������Ƶʱ��
		ArrayList slist =getStation(obj);
		if(slist.size()>0){
			WritableSheet ws=wwb.getSheet("����̨���岥��Ч��ͳ��");
			if(ws!=null){
				int startRow=ws.getRows()+2;
				ws.addCell(new Label(0,startRow,"����̨",wcfFHead));
				ws.addCell(new Label(1,startRow,"�ɱ�֤����Ƶʱ",wcfFHead));
				ws.addCell(new Label(2,startRow,"����������Ƶʱ",wcfFHead));
				ws.addCell(new Label(3,startRow,"��ʱ������Ƶʱ",wcfFHead));
				ws.addCell(new Label(4,startRow,"��������Ƶʱ",wcfFHead));
				ws.addCell(new Label(5,startRow,"��Ƶʱ��",wcfFHead));
				ws.setColumnView(1, 16);
                ws.setColumnView(2, 16);
                ws.setColumnView(3, 16);
                ws.setColumnView(4, 16);
                ws.setColumnView(5, 16);
				for(int i=0;i<slist.size();i++){
					ASObject obj1 = (ASObject) slist.get(i);
					ws.addCell(new Label(0,startRow+1+i,obj1.get("station").toString(),wcfF2));
					ws.addCell(new jxl.write.Number(1,startRow+1+i,Double.parseDouble(obj1.get("hour1").toString()),wcfF2));
					ws.addCell(new jxl.write.Number(2,startRow+1+i,Double.parseDouble(obj1.get("hour2").toString()),wcfF2));
					ws.addCell(new jxl.write.Number(3,startRow+1+i,Double.parseDouble(obj1.get("hour3").toString()),wcfF2));
					ws.addCell(new jxl.write.Number(4,startRow+1+i,Double.parseDouble(obj1.get("hour4").toString()),wcfF2));
					ws.addCell(new jxl.write.Number(5,startRow+1+i,Double.parseDouble(obj1.get("hour5").toString()),wcfF2));
				}
			} 
			
			
		}
		
		//ÿ������̨���ܿ�����
		if(station_listens!=null&&!station_listens.equalsIgnoreCase("")){
			WritableSheet ws=wwb.getSheet("����̨���岥��Ч��ͳ��");
			if(ws!=null){
				int startRow=ws.getRows()+2;
				ws.addCell(new Label(0,startRow,"����̨",wcfFHead));
				ws.addCell(new Label(0,startRow+1,"�ܿ�����",wcfFHead));
				String[] ss=station_listens.split(",");
				if(ss.length>0){
					ArrayList newlist = new ArrayList();

					for(int i=0;i<ss.length;i++){
						newlist.add(ss[i]);
					}
					Collections.sort(newlist, new Comparator(){
						public int compare(Object o1,Object o2){
							return new String((String) o1).compareTo(new String((String) o2));
						}
					});
					for(int j=0;j<newlist.size();j++){
						ws.setColumnView(j+1, 15);
						ws.addCell(new Label(j+1,startRow,newlist.get(j).toString().split("_")[0],wcfFHead));
						ws.addCell(new Label(j+1,startRow+1,newlist.get(j).toString().split("_")[3]+"%",wcfF2));
					}
			}
			
			}
			
		}
		//ÿ�����Ե��ܿ�����
		if(language_listens.size()>0){
			WritableSheet ws=wwb.getSheet("�������岥��Ч��ͳ��");
			if(ws!=null){
				int startRow=ws.getRows()+2;
				ws.addCell(new Label(0,startRow,"����",wcfFHead));
				ws.addCell(new Label(0,startRow+1,"�ܿ�����",wcfFHead));
				for(int i=0;i<language_listens.size();i++){
					ws.setColumnView(i+1, 15);
					ASObject asobj = (ASObject)language_listens.get(i);
					ws.addCell(new Label(i+1,startRow,asobj.get("language").toString(),wcfFHead));
					ws.addCell(new Label(i+1,startRow+1,asobj.get("listens").toString()+"%",wcfF2));
				}
			}
			
		}
		//����ͳ�� Ƶ�ʿ�����ͳ��
		if(frqList.size()>0){
			WritableSheet ws=wwb.getSheet("����ͳ��");
			if(ws!=null){
				for(int i=0;i<frqList.size();i++){
					ZrstRepEffectStatisticsBean bean = (ZrstRepEffectStatisticsBean)frqList.get(i);
					if(wwb.getNumberOfSheets() == 0 || wwb.getSheet("����ͳ��") == null){
						ws = wwb.createSheet("����ͳ��", wwb.getNumberOfSheets()+1);
		                ws.setColumnView(0, 15);
		                ws.setColumnView(1, 16);
		                ws.setColumnView(2, 16);
		                ws.setColumnView(3, 16);
		                ws.setColumnView(4, 16);
		                ws.setColumnView(5, 13);
					} else{
						ws = wwb.getSheet("����ͳ��");
					}
					if(beginRow71 == 0){
						beginRow71 = ws.getRows()+4;
						
						//��������
						ws.mergeCells(8,beginRow71-1,13,beginRow71-1);
		                ws.addCell(new Label(8,beginRow71-1,"����������̶�",wcfFHead));
		                
						ws.mergeCells(0,beginRow71-1,0,beginRow71);
						ws.mergeCells(1,beginRow71-1,1,beginRow71);
						ws.mergeCells(2,beginRow71-1,2,beginRow71);
						ws.mergeCells(3,beginRow71-1,3,beginRow71);
						ws.mergeCells(4,beginRow71-1,4,beginRow71);
						ws.mergeCells(5,beginRow71-1,5,beginRow71);
						ws.mergeCells(6,beginRow71-1,6,beginRow71);
						ws.mergeCells(7,beginRow71-1,7,beginRow71);
						ws.mergeCells(14,beginRow71-1,14,beginRow71);
						ws.mergeCells(15,beginRow71-1,15,beginRow71);
						ws.mergeCells(16,beginRow71-1,16,beginRow71);
						
		                ws.addCell(new Label(0,beginRow71-1,"Ƶ��",wcfFHead));
		                ws.addCell(new Label(1,beginRow71-1,"����",wcfFHead));
		                ws.addCell(new Label(2,beginRow71-1,"����ʱ��",wcfFHead));
		                ws.addCell(new Label(3,beginRow71-1,"����̨",wcfFHead));
		                ws.addCell(new Label(4,beginRow71-1,"���䷽��",wcfFHead));
		                ws.addCell(new Label(5,beginRow71-1,"���书��",wcfFHead));
		                ws.addCell(new Label(6,beginRow71-1,"������",wcfFHead));
		                ws.addCell(new Label(7,beginRow71-1,"�ղ����",wcfFHead));
		                ws.addCell(new Label(8,beginRow71,"5",wcfFHead));
		                ws.addCell(new Label(9,beginRow71,"4",wcfFHead));
		                ws.addCell(new Label(10,beginRow71,"3",wcfFHead));
		                ws.addCell(new Label(11,beginRow71,"2",wcfFHead));
		                ws.addCell(new Label(12,beginRow71,"1",wcfFHead));
		                ws.addCell(new Label(13,beginRow71,"0",wcfFHead));
		                ws.addCell(new Label(14,beginRow71-1,"������%",wcfFHead));
		                ws.addCell(new Label(15,beginRow71-1,"�������",wcfFHead));
		                ws.addCell(new Label(16,beginRow71-1,"��ע",wcfFHead));

		                ws.mergeCells(0,beginRow71-2,16,beginRow71-2);

		                ws.setRowView(beginRow71-2, 740);//���õ�һ�и߶�
		                ws.addCell(new Label(0, beginRow71-2, "Ƶ�ʿ�����ͳ�Ʊ�",wcfFTitle));
		                ws.setRowView(0, 740);//���õ�һ�и߶�
		                ws.setColumnView(6, 11);
		                ws.setColumnView(7, 6);
		                ws.setColumnView(8, 6);
		                ws.setColumnView(9, 5);
		                ws.setColumnView(10, 5);
		                ws.setColumnView(11, 5);
		                ws.setColumnView(12, 5);
		                ws.setColumnView(13, 5);
		                ws.setColumnView(14, 13);
		                ws.setColumnView(15, 14);
		                ws.setColumnView(16, 14);
					}

					ws.addCell(new Label(0,ws.getRows(), bean.getFreq(),wcfF2));
					ws.addCell(new Label(1,ws.getRows()-1, bean.getLanguage_name(),wcfF2));
					ws.addCell(new Label(2,ws.getRows()-1, bean.getPlay_time(),wcfF2));
					ws.addCell(new Label(3,ws.getRows()-1, bean.getTransmit_station(),wcfF2));
					ws.addCell(new Label(4,ws.getRows()-1, bean.getTransmit_direction(),wcfF2));
					ws.addCell(new Label(5,ws.getRows()-1, bean.getTransmit_power(),wcfF2));
					ws.addCell(new Label(6,ws.getRows()-1, bean.getService_area(),wcfF2));
					ws.addCell(new Label(7,ws.getRows()-1, bean.getReceive_count(),wcfF2));
					ws.addCell(new Label(8,ws.getRows()-1, bean.getFen5(),wcfF2));
					ws.addCell(new Label(9,ws.getRows()-1, bean.getFen4(),wcfF2));
					ws.addCell(new Label(10,ws.getRows()-1, bean.getFen3(),wcfF2));
					ws.addCell(new Label(11,ws.getRows()-1, bean.getFen2(),wcfF2));
					ws.addCell(new Label(12,ws.getRows()-1, bean.getFen1(),wcfF2));
					ws.addCell(new Label(13,ws.getRows()-1, bean.getFen0(),wcfF2));
					ws.addCell(new Label(14,ws.getRows()-1, bean.getAverage_listens(),wcfF2));
					
					if(Integer.parseInt(bean.getAverage_listens())>=80){
						ws.addCell(new Label(15,ws.getRows()-1, "�ɱ�֤����",wcfF2));
					}
					if(Integer.parseInt(bean.getAverage_listens())>=60&&Integer.parseInt(bean.getAverage_listens())<80){
						ws.addCell(new Label(15,ws.getRows()-1, "����������",wcfF2));
					}
					if(Integer.parseInt(bean.getAverage_listens())>=30&&Integer.parseInt(bean.getAverage_listens())<60){
						ws.addCell(new Label(15,ws.getRows()-1, "��ʱ������",wcfF2));
					}
					if(Integer.parseInt(bean.getAverage_listens())<30){
						ws.addCell(new Label(15,ws.getRows()-1, "��������",wcfF2));
					}
					ws.addCell(new Label(16,ws.getRows()-1, bean.getBak(),wcfF2));
				}
			}
			
		}
		
			
		
		 //��ң��վ�Լ������޵���������Ч��
		ArrayList hlist=getHeadend(obj,"101");
		if(hlist.size()>0){
			WritableSheet ws=wwb.getSheet("����ͳ��");
			if(ws!=null){
				int startRow=ws.getRows()+3;
				ws.addCell(new Label(0,startRow-2,"����Ч��",wcfFTitle));
                ws.mergeCells(0,startRow-2,4,startRow-1);//��ʼ�У���ʼ�У������У�������
				ws.addCell(new Label(0,startRow,"ң��վ",wcfFHead));
				ws.addCell(new Label(1,startRow,"�ɱ�֤����Ƶʱ",wcfFHead));
				ws.addCell(new Label(2,startRow,"����������Ƶʱ",wcfFHead));
				ws.addCell(new Label(3,startRow,"��ʱ������Ƶʱ",wcfFHead));
				ws.addCell(new Label(4,startRow,"��������Ƶʱ",wcfFHead));
				ws.addCell(new Label(5,startRow,"��Ƶʱ��",wcfFHead));
				ws.setColumnView(1, 16);
                ws.setColumnView(2, 16);
                ws.setColumnView(3, 16);
                ws.setColumnView(4, 16);
				for(int i=0;i<hlist.size();i++){
					ASObject hobj = (ASObject) hlist.get(i);
					ws.addCell(new Label(0,startRow+1+i,hobj.get("ykz").toString(),wcfF2));
					ws.addCell(new jxl.write.Number(1,startRow+1+i,Double.parseDouble(hobj.get("hour1").toString()),wcfF2));
					ws.addCell(new jxl.write.Number(2,startRow+1+i,Double.parseDouble(hobj.get("hour2").toString()),wcfF2));
					ws.addCell(new jxl.write.Number(3,startRow+1+i,Double.parseDouble(hobj.get("hour3").toString()),wcfF2));
					ws.addCell(new jxl.write.Number(4,startRow+1+i,Double.parseDouble(hobj.get("hour4").toString()),wcfF2));
					ws.addCell(new jxl.write.Number(5,startRow+1+i,Double.parseDouble(hobj.get("hour1").toString())+
							Double.parseDouble(hobj.get("hour2").toString())+
							Double.parseDouble(hobj.get("hour3").toString())+
							Double.parseDouble(hobj.get("hour4").toString()),wcfF2));
				}
			} 
			
			
		}
		
		// ÿ��ң��վ���ܿ�����
		if(ykz_listens.size()>0){
			WritableSheet ws=wwb.getSheet("����ͳ��");
			if(ws!=null){
				int startRow=ws.getRows()+2;
				ws.addCell(new Label(0,startRow,"ң��վ",wcfFHead));
				ws.addCell(new Label(0,startRow+1,"�ܿ�����",wcfFHead));
				for(int i=0;i<ykz_listens.size();i++){
					ws.setColumnView(i+1, 15);
					ASObject asobj = (ASObject)ykz_listens.get(i);
					ws.addCell(new Label(i+1,startRow,asobj.get("receive_name").toString(),wcfFHead));
					ws.addCell(new Label(i+1,startRow+1,asobj.get("listen").toString()+"%",wcfF2));
				}
			}
			
		}
	   
			wwb.write();
	        wwb.close();
	        outputStream.close();
    }
    
    /**
     * �����������Ч��ͳ��
     * @detail  
     * @method  
     * @param msg
     * @param request
     * @param response
     * @throws Exception 
     * @return  void  
     * @author  zhaoyahui
     * @version 2013-5-21 ����04:50:30
     */
    private void doExcelHWLD(String msg,HttpServletRequest request,HttpServletResponse response) throws Exception{
    	Element root = StringTool.getXMLRoot(msg);
		String reportId = root.getChildText("reportId");
		String report_type = root.getChildText("report_type");//��������  1������̨Ч������ͳ��  2���������Ч������ͳ��
		String starttime = root.getChildText("starttime").replaceAll("-", "");
		String endtime = root.getChildText("endtime").replaceAll("-", "");
		
    	String fileName="����վ�㺣�����Ч��ͳ�Ʊ�";
		if(starttime.equals(endtime)){
			fileName = "����վ��"+starttime+"�ں������Ч��ͳ�Ʊ�";
		} else if(starttime.substring(0, 6).equals(endtime.substring(0, 6))){
			fileName = "����վ��"+starttime.substring(0, 6)+"�ں������Ч��ͳ�Ʊ�";
		} else{
			fileName = "����վ��"+starttime.substring(0, 4)+"�ں������Ч��ͳ�Ʊ�";
		}
		ASObject obj = new ASObject();
		obj.put("reportId", reportId);
		obj.put("sub_report_type","11");
		ArrayList list = (ArrayList) queryDetailReport(obj);
		
		ArrayList list1 = (ArrayList) queryDetailReport1(obj);
		
		ArrayList  language_listens = queryLanguage_ZListens(obj);
		
		ArrayList  ykz_listens = queryYkz_ZListens(obj);
		
		String  station_listens = queryStation_ZListens(obj);
		
		ArrayList  frqList = queryFreqListen(reportId,"71");
//		String downFileName=jExcel.openDocument();
//		jExcel.WorkBookGetSheet(0);
		OutputStream outputStream = response.getOutputStream();
		response.setContentType("application/vnd.ms-excel");
		response.reset();
		response.setHeader("Location", "Export.xls");
		response.setHeader("Expires", "0");
//		response.setHeader("Content-Disposition",new String(("filename="+fileName).getBytes("GBK"),"ISO-8859-1")+".xls");
        response.setHeader("Content-disposition", "attachment; filename="+new String((fileName).getBytes("GBK"),"ISO-8859-1")+".xls");
        
		WritableWorkbook wwb =Workbook.createWorkbook(outputStream);
		int sheetNum = 0;
		//�ӱ�������  11������̨�㲥Ч��ͳ�ƣ�21������̨���岥��Ч��ͳ��1��22������̨���岥��Ч��ͳ��2��23������̨���岥��Ч��ͳ��3��31���������岥��Ч��ͳ��1��
		//32���������岥��Ч��ͳ��2��41����ң��վ���������������ޡ�������ͳ�ƣ�51����ң��վ���������������ޡ��ɱ�֤����Ƶʱͳ�ƣ�61�����¿����ʶԱȣ�71��Ƶ�ʿ�����ͳ�Ʊ�
		
		 WritableFont wfTitle = new WritableFont(WritableFont.createFont("����"), 12,WritableFont.BOLD, false);
         WritableCellFormat wcfFTitle = new WritableCellFormat(wfTitle);
		wcfFTitle.setAlignment(jxl.format.Alignment.CENTRE);
		wcfFTitle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			 
		WritableFont wfHead = new WritableFont(WritableFont.createFont("����"), 10,WritableFont.BOLD, false);
		WritableCellFormat wcfFHead = new WritableCellFormat(wfHead);
		wcfFHead.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);//���߿� 
		wcfFHead.setWrap(true);//�Զ�����
         //����Number�ĸ�ʽ
         //jxl.write.NumberFormat nf = new jxl.write.NumberFormat("0.00"); 
         //jxl.write.WritableCellFormat priceformat = new jxl.write.WritableCellFormat(nf);
         // ��ˮƽ���뷽ʽָ��Ϊ�����
		wcfFHead.setAlignment(jxl.format.Alignment.CENTRE);
         // �Ѵ�ֱ���뷽ʽָ��Ϊ���ж���
		wcfFHead.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		
		WritableFont wf1 = new WritableFont(WritableFont.createFont("����"), 10,WritableFont.NO_BOLD, false);
        jxl.write.WritableCellFormat wcfF2 = new jxl.write.WritableCellFormat(wf1);
        wcfF2.setAlignment(jxl.format.Alignment.CENTRE);
        wcfF2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        wcfF2.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); 
        
        jxl.write.WritableCellFormat wcfF3 = new jxl.write.WritableCellFormat(wf1);
        wcfF3.setAlignment(jxl.format.Alignment.CENTRE);
        wcfF3.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        wcfF3.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); 
        wcfF3.setBackground(Colour.YELLOW2);//���õ�Ԫ�񱳾�ɫ
        
		HashMap stationTotal1Map = new HashMap();
		HashMap languageTotal1Map = new HashMap();
		HashMap head22Map = new HashMap();
		HashMap head32Map = new HashMap();
		int beginRow22 = 0;
		int beginRow23 = 0;
		int beginRow32 = 0;
		int beginRow51 = 0;
		int beginRow61 = 0;
		int beginRow71 = 0;
		ZrstRepEffectStatisticsBean totalBean = new ZrstRepEffectStatisticsBean();
		for(int i=0;i<list1.size();i++){
			ZrstRepEffectStatisticsBean bean = (ZrstRepEffectStatisticsBean)list1.get(i);
			WritableSheet ws =  null;
			if(wwb.getNumberOfSheets() == 0 || wwb.getSheet(bean.getReceive_name()) == null){
				ws = wwb.createSheet(bean.getReceive_name(), wwb.getNumberOfSheets()+1);
                 //��������
                 ws.addCell(new Label(0,1,"����ʱ��",wcfFHead));
                 ws.addCell(new Label(1,1,"����",wcfFHead));
                 ws.addCell(new Label(2,1,"Ƶ��",wcfFHead));
                 ws.addCell(new Label(3,1,"ת������",wcfFHead));
                 ws.addCell(new Label(4,1,"���书��",wcfFHead));
                 ws.addCell(new Label(5,1,"������",wcfFHead));
                 ws.addCell(new Label(6,1,"�ղ�ص�",wcfFHead));
                 ws.addCell(new Label(7,1,"�ղ����",wcfFHead));
                 ws.addCell(new Label(8,1,"5",wcfFHead));
                 ws.addCell(new Label(9,1,"4",wcfFHead));
                 ws.addCell(new Label(10,1,"3",wcfFHead));
                 ws.addCell(new Label(11,1,"2",wcfFHead));
                 ws.addCell(new Label(12,1,"1",wcfFHead));
                 ws.addCell(new Label(13,1,"0",wcfFHead));
                 ws.addCell(new Label(14,1,"������%",wcfFHead));
                 ws.addCell(new Label(15,1,"��������ֵ",wcfFHead));
                 ws.addCell(new Label(16,1,"��ע",wcfFHead));

                 ws.mergeCells(0,0,16,0);
                
                 ws.addCell(new Label(0, 0, bean.getReceive_code()+bean.getReceive_name()+"������ؽ�ĿЧ��ͳ�Ʊ�",wcfFTitle));
                 ws.setRowView(0, 740);//���õ�һ�и߶�
                 ws.setRowView(1, 550);
                 ws.setColumnView(0, 13);
                 ws.setColumnView(1, 5);
                 ws.setColumnView(2, 7);
                 ws.setColumnView(3, 9);
                 ws.setColumnView(4, 5);
                 ws.setColumnView(5, 11);
                 ws.setColumnView(6, 7);
                 ws.setColumnView(7, 7);
                 ws.setColumnView(8, 6);
                 ws.setColumnView(9, 5);
                 ws.setColumnView(10, 5);
                 ws.setColumnView(11, 5);
                 ws.setColumnView(12, 5);
                 ws.setColumnView(13, 5);
                 ws.setColumnView(14, 6);
                 ws.setColumnView(15, 8);
                 ws.setColumnView(16, 28);
                 
			} else  {
				ws = wwb.getSheet(bean.getReceive_name()); 
			}
			int curRow = ws.getRows();
			int nextRow = curRow;
			if(bean.getPlay_time().equals("����Ч��")){
				totalBean = bean;
				ws.mergeCells(0,nextRow,6,nextRow);
				ws.addCell(new Label(0,nextRow, "����Ч��",wcfF2));
				ws.addCell(new jxl.write.Number(7,nextRow, Integer.parseInt(totalBean.getReceive_count()),wcfF2));
				ws.addCell(new jxl.write.Number(8,nextRow, Integer.parseInt(totalBean.getFen5()),wcfF2));
				ws.addCell(new jxl.write.Number(9,nextRow, Integer.parseInt(totalBean.getFen4()),wcfF2));
				ws.addCell(new jxl.write.Number(10,nextRow, Integer.parseInt(totalBean.getFen3()),wcfF2));
				ws.addCell(new jxl.write.Number(11,nextRow, Integer.parseInt(totalBean.getFen2()),wcfF2));
				ws.addCell(new jxl.write.Number(12,nextRow, Integer.parseInt(totalBean.getFen1()),wcfF2));
				ws.addCell(new jxl.write.Number(13,nextRow, Integer.parseInt(totalBean.getFen0()),wcfF2));
				ws.addCell(new jxl.write.Number(14,nextRow, Integer.parseInt(totalBean.getListen()),wcfF2));
				ws.addCell(new Label(15,nextRow, totalBean.getListen_middle(),wcfF2));
				ws.addCell(new Label(16,nextRow, totalBean.getBak(),wcfF2));
			}else{
				ws.addCell(new Label(0,curRow, bean.getPlay_time(),wcfF2));
				ws.addCell(new Label(1,curRow, bean.getLanguage_name(),wcfF2));
				ws.addCell(new Label(2,curRow, bean.getFreq(),wcfF2));
				ws.addCell(new Label(3,curRow, bean.getTransmit_station(),wcfF2));
				ws.addCell(new Label(4,curRow, bean.getTransmit_power(),wcfF2));
				ws.addCell(new Label(5,curRow, bean.getService_area(),wcfF2));
				ws.addCell(new Label(6,curRow, bean.getReceive_code(),wcfF2));
				ws.addCell(new jxl.write.Number(7,curRow, Integer.parseInt(bean.getReceive_count()),wcfF2));
				//ws.addCell(new Label(8,curRow, bean.getFen5(),wcfF2));
				ws.addCell(new jxl.write.Number(8,curRow, Integer.parseInt(bean.getFen5()),wcfF2));//ת�������ָ�ʽ
				ws.addCell(new jxl.write.Number(9,curRow, Integer.parseInt(bean.getFen4()),wcfF2));
				ws.addCell(new jxl.write.Number(10,curRow, Integer.parseInt(bean.getFen3()),wcfF2));
				ws.addCell(new jxl.write.Number(11,curRow, Integer.parseInt(bean.getFen2()),wcfF2));
				ws.addCell(new jxl.write.Number(12,curRow, Integer.parseInt(bean.getFen1()),wcfF2));
				ws.addCell(new jxl.write.Number(13,curRow, Integer.parseInt(bean.getFen0()),wcfF2));
				//ws.addCell(new Label(14,curRow, bean.getListen(),wcfF2));
				if(bean.getListen()!=null&&!bean.getListen().equalsIgnoreCase("")){
					if(Integer.parseInt(bean.getListen())<60){
						ws.addCell(new jxl.write.Number(14,curRow, Integer.parseInt(bean.getListen()),wcfF3));
					}else{
						ws.addCell(new jxl.write.Number(14,curRow, Integer.parseInt(bean.getListen()),wcfF2));
					}
				}else{
					ws.addCell(new jxl.write.Number(14,curRow, Integer.parseInt(bean.getListen()),wcfF2));
				}
				ws.addCell(new Label(15,curRow, bean.getListen_middle(),wcfF2));
				ws.addCell(new Label(16,curRow, bean.getBak(),wcfF2));
			}
		}
		for(int i=0;i<list.size();i++){
			ZrstRepEffectStatisticsBean bean = (ZrstRepEffectStatisticsBean)list.get(i);
//			bean.setReceive_name(bean.getReceive_name()+xx);
			WritableSheet ws =  null;
			if(bean.getSub_report_type().equals("21")){//����̨���岥��Ч��ͳ��1
				
				String name = "����̨";
				if(bean.getReport_type().equals("2")){
					name = "ת������";
				}
				if(wwb.getNumberOfSheets() == 0 || wwb.getSheet(name+"���岥��Ч��ͳ��") == null){
					ws = wwb.createSheet(name+"���岥��Ч��ͳ��", wwb.getNumberOfSheets()+1);
	                ws.addCell(new Label(0,1,"ң��վ",wcfFHead));
	                ws.mergeCells(0,1,0,2);//��ʼ�У���ʼ�У������У�������
	                 
	                for(int ai=0;ai<list.size();ai++){
						ZrstRepEffectStatisticsBean aibean = (ZrstRepEffectStatisticsBean)list.get(ai);
						//����̨_>=3��_�ܴ���_������%  ���磺2022_88_123_78,2032_23_423_28
		 				String[] headStationArr = aibean.getTransmit_station_listens().split(",");
		 				for(String ones:headStationArr){
		 					if( aibean.getSub_report_type().equals("21")){

//				 				System.out.println(aibean.getTransmit_station_listens()+"<<<<<<<<>>"+bean.getSub_report_type());
								if(stationTotal1Map.get(ones.split("_")[0]) == null){
									stationTotal1Map.put(ones.split("_")[0],ws.getColumns());
					                 ws.addCell(new Label((Integer)stationTotal1Map.get(ones.split("_")[0]),2,">=3��",wcfFHead));
					                 ws.addCell(new Label(ws.getColumns(),2,"�ܴ���",wcfFHead));
					                 ws.addCell(new Label(ws.getColumns(),2,"������%",wcfFHead));
					                 ws.mergeCells((Integer)stationTotal1Map.get(ones.split("_")[0]),1,((Integer)stationTotal1Map.get(ones.split("_")[0])+2),1);
					                 
			
			//		                 ws.addCell(new Label(0,ws.getRows(),bean.getReceive_name(),wcfFHead));
					                 ws.addCell(new Label((Integer)stationTotal1Map.get(ones.split("_")[0]),1,ones.split("_")[0],wcfFHead));
								}
		 					}
		 				}
					} 
				} else{
					ws = wwb.getSheet(name+"���岥��Ч��ͳ��");
				}
				
				
				String[] headStationArr = bean.getTransmit_station_listens().split(",");
				int currRow = ws.getRows();
 				for(String ones:headStationArr){
//	                 System.out.println("ones.split()=="+ones);
 						String showName = bean.getReceive_name();
 						if(showName == null || showName.equals(""))
 							showName = bean.getService_area();
 						if(showName == null || showName.equals(""))
 							showName = bean.getState_name();
 						if(showName == null || showName.equals(""))
 							showName = "�ܼ�";
 						
 						 ws.addCell(new Label(0,currRow,showName,wcfFHead));
// 						 System.out.println(ones.split("_").length==4?ones.split("_")[1]:"B"+"--"+ws.getColumns());
 						 for(int yy=1;yy<ws.getColumns();yy++){
 							if(ws.getCell(yy, currRow).getContents().equals("")){
 								ws.addCell(new Label(yy, currRow,"",wcfF2));
 							}
 							 if((Integer)stationTotal1Map.get(ones.split("_")[0]) == yy){
 								 ws.addCell(new Label((Integer)stationTotal1Map.get(ones.split("_")[0]), currRow,ones.split("_").length==4?ones.split("_")[1]:"",wcfF2));
 							 } 
 							 else if((Integer)stationTotal1Map.get(ones.split("_")[0])+1 == yy){
 								 ws.addCell(new Label((Integer)stationTotal1Map.get(ones.split("_")[0])+1, currRow, ones.split("_").length==4?ones.split("_")[2]:"",wcfF2));
 							 }
 							 else if((Integer)stationTotal1Map.get(ones.split("_")[0])+2 == yy){
 								 ws.addCell(new Label((Integer)stationTotal1Map.get(ones.split("_")[0])+2, currRow, ones.split("_").length==4?ones.split("_")[3]+"%":"",wcfF2));
 							 }
// 							 else{
// 								 ws.addCell(new Label(yy, ws.getRows()-1, "",wcfF2));
// 							 }
 						 }
 				}
// 				System.out.println("11111111");
			} else if(bean.getSub_report_type().equals("22")){//����̨���岥��Ч��ͳ��2
// 				System.out.println("2222222222");
 				String name = "����̨";
				if(bean.getReport_type().equals("2")){
					name = "ת������";
				}
				ws = wwb.getSheet(name+"���岥��Ч��ͳ��");
				ws.setColumnView(0, 15);
				if(beginRow22 == 0){
					beginRow22 = ws.getRows()+2;
				}
				ws.addCell(new Label(0,beginRow22,name+"\\ң��վ",wcfFHead));
				
				String[] transmitStationArr = bean.getReceive_name_total_hours().split(",");
//				System.out.println("bean.getReceive_name_total_hours()=="+bean.getReceive_name_total_hours());
				if(transmitStationArr.length>0){
					ws.addCell(new Label(0,ws.getRows(),bean.getTransmit_station(),wcfF2));
				}
				for(String transmitStation:transmitStationArr){
					if(transmitStation.equals(""))
						continue;
//					System.out.println("transmitStation=="+transmitStation);
					String[] receiveVal = transmitStation.split("_");
					if(head22Map.get(receiveVal[0]) == null){
						int colNum = 0;
						for(Object o:head22Map.keySet()){
							colNum = colNum>=(Integer)head22Map.get(o)?colNum:(Integer)head22Map.get(o);   // Map��ֵ
						}
						if(colNum == 0){
							for(int ai=0;ai<list.size();ai++){
								ZrstRepEffectStatisticsBean aibean = (ZrstRepEffectStatisticsBean)list.get(ai);
								//ң��վ��_��Ƶʱ  ���磺����_96,��¡��_34
				 				String[] headStationArr = aibean.getReceive_name_total_hours().split(",");
				 				for(String ones:headStationArr){
				 					if( aibean.getSub_report_type().equals("22")){

//						 				System.out.println(aibean.getReceive_name_total_hours()+"<<<<<<<<>>"+bean.getSub_report_type());
						 				if(colNum == 0){
						 					colNum++;
											head22Map.put(ones.split("_")[0],colNum);
						 				} else if(head22Map.get(ones.split("_")[0]) == null){
						 					for(Object o:head22Map.keySet()){
												colNum = colNum>=(Integer)head22Map.get(o)?colNum:(Integer)head22Map.get(o);   // Map��ֵ
											}
											head22Map.put(ones.split("_")[0],colNum+1);
										}

										ws.addCell(new Label((Integer)head22Map.get(ones.split("_")[0]),ws.getRows()-2,ones.split("_")[0],wcfFHead));
				 					}
				 				}
							}
						}
//						head22Map.put(receiveVal[0],colNum+1);
					} else{
					}
					if(Integer.parseInt(receiveVal[1].equals("A")?"100":receiveVal[1])<60){
						ws.addCell(new Label((Integer)head22Map.get(receiveVal[0]),ws.getRows()-1,(receiveVal[1].equals("A")?"":receiveVal[1]+"%"),wcfF3));
					}else{
						ws.addCell(new Label((Integer)head22Map.get(receiveVal[0]),ws.getRows()-1,(receiveVal[1].equals("A")?"":receiveVal[1]+"%"),wcfF2));
					}
					
				}
				
			} else if(bean.getSub_report_type().equals("23")){//����̨���岥��Ч��ͳ��3
				if(bean.getReport_type().equals("1")){
//					System.out.println("222223333333333");
					ws = wwb.getSheet("����̨���岥��Ч��ͳ��");
					if(beginRow23 == 0){
						beginRow23 = ws.getRows()+2;
						ws.addCell(new Label(0,beginRow23,"����̨",wcfFHead));
						ws.addCell(new Label(1,beginRow23,"�ɱ�֤����Ƶʱ",wcfFHead));
						ws.addCell(new Label(2,beginRow23,"����������Ƶʱ",wcfFHead));
						ws.addCell(new Label(3,beginRow23,"��ʱ������Ƶʱ",wcfFHead));
						ws.addCell(new Label(4,beginRow23,"��������Ƶʱ",wcfFHead));
						ws.setColumnView(1, 16);
		                ws.setColumnView(2, 16);
		                ws.setColumnView(3, 16);
		                ws.setColumnView(4, 16);
					}
					String tt = bean.getReceive_listens()+"_";
					tt = tt.replaceAll(".0_", "_");
					tt = tt.substring(0,tt.length()-1);
					String[] val23 = tt.split("_");
					ws.addCell(new Label(0,ws.getRows(),bean.getTransmit_station(),wcfF2));
					ws.addCell(new Label(1,ws.getRows()-1,val23[0],wcfF2));
					ws.addCell(new Label(2,ws.getRows()-1,val23[1],wcfF2));
					ws.addCell(new Label(3,ws.getRows()-1,val23[2],wcfF2));
					ws.addCell(new Label(4,ws.getRows()-1,val23[3],wcfF2));
				}
			} else if(bean.getSub_report_type().equals("31")){//�������岥��Ч��ͳ��1
				if(wwb.getNumberOfSheets() == 0 || wwb.getSheet("�������岥��Ч��ͳ��") == null){
					ws = wwb.createSheet("�������岥��Ч��ͳ��", wwb.getNumberOfSheets()+1);
	                ws.addCell(new Label(0,1,"ң��վ",wcfFHead));
	                ws.mergeCells(0,1,0,2);//��ʼ�У���ʼ�У������У�������
	                 
	                for(int ai=0;ai<list.size();ai++){
						ZrstRepEffectStatisticsBean aibean = (ZrstRepEffectStatisticsBean)list.get(ai);
						//����_>=3��_�ܴ���_������%  ���磺��_88_123_78,2032_23_423_28
		 				String[] headLanguageArr = aibean.getLanguage_name_listens().split(",");
		 				for(String ones:headLanguageArr){
		 					if( aibean.getSub_report_type().equals("31")){

//				 				System.out.println(aibean.getLanguage_name_listens()+"<<<<<<<<>>"+bean.getSub_report_type());
								if(languageTotal1Map.get(ones.split("_")[0]) == null){
									languageTotal1Map.put(ones.split("_")[0],ws.getColumns());
					                 ws.addCell(new Label((Integer)languageTotal1Map.get(ones.split("_")[0]),2,">=3��",wcfFHead));
					                 ws.addCell(new Label(ws.getColumns(),2,"�ܴ���",wcfFHead));
					                 ws.addCell(new Label(ws.getColumns(),2,"������%",wcfFHead));
					                 ws.mergeCells((Integer)languageTotal1Map.get(ones.split("_")[0]),1,((Integer)languageTotal1Map.get(ones.split("_")[0])+2),1);
					                 
			
			//		                 ws.addCell(new Label(0,ws.getRows(),bean.getReceive_name(),wcfFHead));
					                 ws.addCell(new Label((Integer)languageTotal1Map.get(ones.split("_")[0]),1,ones.split("_")[0],wcfFHead));
								}
		 					}
		 				}
					} 
				} else{
					ws = wwb.getSheet("�������岥��Ч��ͳ��");
				}
				
				
				String[] headLanguageArr = bean.getLanguage_name_listens().split(",");
				int currRow = ws.getRows();
 				for(String ones:headLanguageArr){
//	                 System.out.println("ones.split()=="+ones);
 						String showName = bean.getReceive_name();
 						if(showName == null || showName.equals(""))
 							showName = bean.getService_area();
 						if(showName == null || showName.equals(""))
 							showName = bean.getState_name();
 						if(showName == null || showName.equals(""))
 							showName = "�ܼ�";
 						
 						 ws.addCell(new Label(0,currRow,showName,wcfFHead));
 						 for(int yy=1;yy<ws.getColumns();yy++){
 							if(ws.getCell(yy, currRow).getContents().equals("")){
 								ws.addCell(new Label(yy, currRow,"",wcfF2));
 							}
 							 if((Integer)languageTotal1Map.get(ones.split("_")[0]) == yy){
 								 ws.addCell(new Label((Integer)languageTotal1Map.get(ones.split("_")[0]), currRow,ones.split("_").length==4?ones.split("_")[1]:"",wcfF2));
 							 } 
 							 else if((Integer)languageTotal1Map.get(ones.split("_")[0])+1 == yy){
 								 ws.addCell(new Label((Integer)languageTotal1Map.get(ones.split("_")[0])+1, currRow, ones.split("_").length==4?ones.split("_")[2]:"",wcfF2));
 							 }
 							 else if((Integer)languageTotal1Map.get(ones.split("_")[0])+2 == yy){
 								 ws.addCell(new Label((Integer)languageTotal1Map.get(ones.split("_")[0])+2, currRow, ones.split("_").length==4?ones.split("_")[3]+"%":"",wcfF2));
 							 }
// 							 else{
// 								 ws.addCell(new Label(yy, ws.getRows()-1, "",wcfF2));
// 							 }
 						 }
 				}
// 				System.out.println("333333311111111");
			} else if(bean.getSub_report_type().equals("32")){//�������岥��Ч��ͳ��2
//				System.out.println("333333333332222222222");
				ws = wwb.getSheet("�������岥��Ч��ͳ��");
				ws.setColumnView(0, 15);
				if(beginRow32 == 0){
					beginRow32 = ws.getRows()+2;
				}
				ws.addCell(new Label(0,beginRow32,"����\\ң��վ",wcfFHead));
				
				String[] languageArr = bean.getReceive_name_total_hours().split(",");
//				System.out.println("bean.getReceive_name_total_hours()=="+bean.getReceive_name_total_hours());
				if(languageArr.length>0){
					ws.addCell(new Label(0,ws.getRows(),bean.getLanguage_name(),wcfF2));
				}
				for(String language:languageArr){
					if(language.equals(""))
						continue;
//					System.out.println("language=="+language);
					String[] receiveVal = language.split("_");
					if(head32Map.get(receiveVal[0]) == null){
						int colNum = 0;
						for(Object o:head32Map.keySet()){
							colNum = colNum>=(Integer)head32Map.get(o)?colNum:(Integer)head32Map.get(o);   // Map��ֵ
						}
						if(colNum == 0){
							for(int ai=0;ai<list.size();ai++){
								ZrstRepEffectStatisticsBean aibean = (ZrstRepEffectStatisticsBean)list.get(ai);
								//ң��վ��_��Ƶʱ  ���磺����_96,��¡��_34
				 				String[] headStationArr = aibean.getReceive_name_total_hours().split(",");
				 				for(String ones:headStationArr){
				 					if( aibean.getSub_report_type().equals("32")){

//						 				System.out.println(aibean.getReceive_name_total_hours()+"<<<<<<<<>>"+bean.getSub_report_type());
						 				if(colNum == 0){
						 					colNum++;
						 					head32Map.put(ones.split("_")[0],colNum);
						 				} else if(head32Map.get(ones.split("_")[0]) == null){
						 					for(Object o:head32Map.keySet()){
												colNum = colNum>=(Integer)head32Map.get(o)?colNum:(Integer)head32Map.get(o);   // Map��ֵ
											}
						 					head32Map.put(ones.split("_")[0],colNum+1);
										}

										ws.addCell(new Label((Integer)head32Map.get(ones.split("_")[0]),ws.getRows()-2,ones.split("_")[0],wcfFHead));
				 					}
				 				}
							}
						}
//						head32Map.put(receiveVal[0],colNum+1);
					} else{
					}
					if(Integer.parseInt(receiveVal[1].equals("A")?"100":receiveVal[1])<60){
						ws.addCell(new Label((Integer)head32Map.get(receiveVal[0]),ws.getRows()-1,(receiveVal[1].equals("A")?"":receiveVal[1]+"%"),wcfF3));
					}else{
						ws.addCell(new Label((Integer)head32Map.get(receiveVal[0]),ws.getRows()-1,(receiveVal[1].equals("A")?"":receiveVal[1]+"%"),wcfF2));
					}
					
				}
			} else if(bean.getSub_report_type().equals("41")){//��������ÿ�����޷�Ϊ�����������������޺�ȫ���ղ������3�����ϴ����Ϳ�����ͳ��
				if(wwb.getNumberOfSheets() == 0 || wwb.getSheet("����ͳ��") == null){
					ws = wwb.createSheet("����ͳ��", wwb.getNumberOfSheets()+1);
	                ws.addCell(new Label(0,0,"������������",wcfFTitle));
	                ws.mergeCells(0,0,3,0);//��ʼ�У���ʼ�У������У�������
	                ws.addCell(new Label(0,1,"����",wcfFHead));
	                ws.addCell(new Label(1,1,"3�����ϴ���",wcfFHead));
	                ws.addCell(new Label(2,1,"�ܴ���",wcfFHead));
	                ws.addCell(new Label(3,1,"������%",wcfFHead));
	                ws.setRowView(0, 740);//���õ�һ�и߶�
	                ws.setColumnView(0, 15);
	                ws.setColumnView(1, 16);
	                ws.setColumnView(2, 16);
	                ws.setColumnView(3, 16);
	                ws.setColumnView(4, 16);
	                ws.setColumnView(5, 13);
	                
	                String[] allListensArr = bean.getAll_listens().split(",");
//	                System.out.println("bean.getAll_listens()=="+bean.getAll_listens());
	                //����_>=3��_�ܴ���_������%
	                for(int ai=0;ai<allListensArr.length;ai++){
	                	String[] oneValArr = allListensArr[ai].split("_");
	                	int curRow = ws.getRows();
	                	ws.addCell(new Label(0,curRow,oneValArr[0],wcfF2));
		                 ws.addCell(new Label(1,curRow,oneValArr[1],wcfF2));
		                 ws.addCell(new Label(2,curRow,oneValArr[2],wcfF2));
		                 ws.addCell(new Label(3,curRow,oneValArr[3]+"%",wcfF2));
					} 
				} else{
					ws = wwb.getSheet("����ͳ��");
				}
				
			}
			else if(bean.getSub_report_type().equals("61")){//���¿����ʶԱȣ�
				if(wwb.getNumberOfSheets() == 0 || wwb.getSheet("����ͳ��") == null){
					ws = wwb.createSheet("����ͳ��", wwb.getNumberOfSheets()+1);
	                ws.setColumnView(0, 15);
	                ws.setColumnView(1, 16);
	                ws.setColumnView(2, 16);
	                ws.setColumnView(3, 16);
	                ws.setColumnView(4, 16);
	                ws.setColumnView(5, 13);
				} else{
					ws = wwb.getSheet("����ͳ��");
				}
				if(beginRow61 == 0){
					beginRow61 = ws.getRows()+3;

	                ws.setRowView(beginRow61-1, 740);//���õ�һ�и߶�
					ws.addCell(new Label(0,beginRow61-1,"���¿����ʶԱ�",wcfFTitle));
	                ws.mergeCells(0,beginRow61-1,12,beginRow61-1);//��ʼ�У���ʼ�У������У�������
	                ws.addCell(new Label(0,beginRow61,"ң��վ",wcfFHead));
	                ws.addCell(new Label(1,beginRow61,"1��",wcfFHead));
	                ws.addCell(new Label(2,beginRow61,"2��",wcfFHead));
	                ws.addCell(new Label(3,beginRow61,"3��",wcfFHead));
	                ws.addCell(new Label(4,beginRow61,"4��",wcfFHead));
	                ws.addCell(new Label(5,beginRow61,"5��",wcfFHead));
	                ws.addCell(new Label(6,beginRow61,"6��",wcfFHead));
	                ws.addCell(new Label(7,beginRow61,"7��",wcfFHead));
	                ws.addCell(new Label(8,beginRow61,"8��",wcfFHead));
	                ws.addCell(new Label(9,beginRow61,"9��",wcfFHead));
	                ws.addCell(new Label(10,beginRow61,"10��",wcfFHead));
	                ws.addCell(new Label(11,beginRow61,"11��",wcfFHead));
	                ws.addCell(new Label(12,beginRow61,"12��",wcfFHead));
	                
	                ws.setRowView(0, 740);//���õ�һ�и߶�
	                ws.setColumnView(6, 12);
	                ws.setColumnView(7, 12);
	                ws.setColumnView(8, 12);
	                ws.setColumnView(9, 12);
	                ws.setColumnView(10, 12);
	                ws.setColumnView(11, 12);
				}
				String showName = bean.getReceive_name();
				if(showName == null || showName.equals(""))
					showName = bean.getState_name();
				if(showName == null || showName.equals(""))
					showName = bean.getService_area();
				if(showName == null || showName.equals(""))
					showName = "ȫ��";
//				System.out.println("bean.getMonth_listens()=="+bean.getMonth_listens());
				String[] month_listens = bean.getMonth_listens().split("_");
				ws.addCell(new Label(0,ws.getRows(),showName,wcfF2));
				ws.addCell(new Label(1,ws.getRows()-1,(month_listens[0].equals("$")?"":month_listens[0]+"%"),wcfF2));
				ws.addCell(new Label(2,ws.getRows()-1,(month_listens[1].equals("$")?"":month_listens[1]+"%"),wcfF2));
				ws.addCell(new Label(3,ws.getRows()-1,(month_listens[2].equals("$")?"":month_listens[2]+"%"),wcfF2));
				ws.addCell(new Label(4,ws.getRows()-1,(month_listens[3].equals("$")?"":month_listens[3]+"%"),wcfF2));
				ws.addCell(new Label(5,ws.getRows()-1,(month_listens[4].equals("$")?"":month_listens[4]+"%"),wcfF2));
				ws.addCell(new Label(6,ws.getRows()-1,(month_listens[5].equals("$")?"":month_listens[5]+"%"),wcfF2));
				ws.addCell(new Label(7,ws.getRows()-1,(month_listens[6].equals("$")?"":month_listens[6]+"%"),wcfF2));
				ws.addCell(new Label(8,ws.getRows()-1,(month_listens[7].equals("$")?"":month_listens[7]+"%"),wcfF2));
				ws.addCell(new Label(9,ws.getRows()-1,(month_listens[8].equals("$")?"":month_listens[8]+"%"),wcfF2));
				ws.addCell(new Label(10,ws.getRows()-1,(month_listens[9].equals("$")?"":month_listens[9]+"%"),wcfF2));
				ws.addCell(new Label(11,ws.getRows()-1,(month_listens[10].equals("$")?"":month_listens[10]+"%"),wcfF2));
				ws.addCell(new Label(12,ws.getRows()-1,(month_listens[11].equals("$")?"":month_listens[11]+"%"),wcfF2));
                
			}
		}
		
		//ת�������������Ƶʱ��
		ArrayList slist =getRedisseminators(obj);
		if(slist.size()>0){
			WritableSheet ws=wwb.getSheet("ת���������岥��Ч��ͳ��");
			if(ws!=null){
				int startRow=ws.getRows()+2;
				ws.addCell(new Label(0,startRow,"ת������",wcfFHead));
				ws.addCell(new Label(1,startRow,"�ɱ�֤����Ƶʱ",wcfFHead));
				ws.addCell(new Label(2,startRow,"����������Ƶʱ",wcfFHead));
				ws.addCell(new Label(3,startRow,"��ʱ������Ƶʱ",wcfFHead));
				ws.addCell(new Label(4,startRow,"��������Ƶʱ",wcfFHead));
				ws.setColumnView(1, 16);
                ws.setColumnView(2, 16);
                ws.setColumnView(3, 16);
                ws.setColumnView(4, 16);
				for(int i=0;i<slist.size();i++){
					ASObject obj1 = (ASObject) slist.get(i);
					ws.addCell(new Label(0,startRow+1+i,obj1.get("station").toString(),wcfF2));
					ws.addCell(new Label(1,startRow+1+i,obj1.get("hour1").toString(),wcfF2));
					ws.addCell(new Label(2,startRow+1+i,obj1.get("hour2").toString(),wcfF2));
					ws.addCell(new Label(3,startRow+1+i,obj1.get("hour3").toString(),wcfF2));
					ws.addCell(new Label(4,startRow+1+i,obj1.get("hour4").toString(),wcfF2));
				}
			} 
			
			
		}
		
		//ÿ��ת���������ܿ�����
		if(station_listens!=null&&!station_listens.equalsIgnoreCase("")){
			WritableSheet ws=wwb.getSheet("ת���������岥��Ч��ͳ��");
			if(ws!=null){
				int startRow=ws.getRows()+2;
				ws.addCell(new Label(0,startRow,"ת������",wcfFHead));
				ws.addCell(new Label(0,startRow+1,"�ܿ�����",wcfFHead));
				String[] ss=station_listens.split(",");
				if(ss.length>0){
					ArrayList newlist = new ArrayList();

					for(int i=0;i<ss.length;i++){
						newlist.add(ss[i]);
					}
					Collections.sort(newlist, new Comparator(){
						public int compare(Object o1,Object o2){
							return new String((String) o1).compareTo(new String((String) o2));
						}
					});
					for(int j=0;j<newlist.size();j++){
						ws.setColumnView(j+1, 15);
						ws.addCell(new Label(j+1,startRow,newlist.get(j).toString().split("_")[0],wcfFHead));
						ws.addCell(new Label(j+1,startRow+1,newlist.get(j).toString().split("_")[3]+"%",wcfF2));
					}
			}
			
			}
			
		}
		
		//ÿ�����Ե��ܿ�����
		if(language_listens.size()>0){
			WritableSheet ws=wwb.getSheet("�������岥��Ч��ͳ��");
			if(ws!=null){
				int startRow=ws.getRows()+2;
				ws.addCell(new Label(0,startRow,"����",wcfFHead));
				ws.addCell(new Label(0,startRow+1,"�ܿ�����",wcfFHead));
				for(int i=0;i<language_listens.size();i++){
					ws.setColumnView(i+1, 15);
					ASObject asobj = (ASObject)language_listens.get(i);
					ws.addCell(new Label(i+1,startRow,asobj.get("language").toString(),wcfFHead));
					ws.addCell(new Label(i+1,startRow+1,asobj.get("listens").toString()+"%",wcfF2));
				}
			}
			
		}
		
		 //��ң��վ�Լ������޵���������Ч��
		ArrayList hlist=getHeadend(obj,"102");
		if(hlist.size()>0){
			WritableSheet ws=wwb.getSheet("����ͳ��");
			if(ws!=null){
				int startRow=ws.getRows()+3;
				ws.addCell(new Label(0,startRow-2,"����Ч��",wcfFTitle));
                ws.mergeCells(0,startRow-2,4,startRow-1);//��ʼ�У���ʼ�У������У�������
				ws.addCell(new Label(0,startRow,"ң��վ",wcfFHead));
				ws.addCell(new Label(1,startRow,"�ɱ�֤����Ƶʱ",wcfFHead));
				ws.addCell(new Label(2,startRow,"����������Ƶʱ",wcfFHead));
				ws.addCell(new Label(3,startRow,"��ʱ������Ƶʱ",wcfFHead));
				ws.addCell(new Label(4,startRow,"��������Ƶʱ",wcfFHead));
				ws.setColumnView(1, 16);
                ws.setColumnView(2, 16);
                ws.setColumnView(3, 16);
                ws.setColumnView(4, 16);
				for(int i=0;i<hlist.size();i++){
					ASObject hobj = (ASObject) hlist.get(i);
					ws.addCell(new Label(0,startRow+1+i,hobj.get("ykz").toString(),wcfF2));
					ws.addCell(new Label(1,startRow+1+i,hobj.get("hour1").toString(),wcfF2));
					ws.addCell(new Label(2,startRow+1+i,hobj.get("hour2").toString(),wcfF2));
					ws.addCell(new Label(3,startRow+1+i,hobj.get("hour3").toString(),wcfF2));
					ws.addCell(new Label(4,startRow+1+i,hobj.get("hour4").toString(),wcfF2));
				}
			} 
			
			
		}
		
		//����ͳ�� Ƶ�ʿ�����ͳ��
		if(frqList.size()>0){
			WritableSheet ws=wwb.getSheet("����ͳ��");
			if(ws!=null){
				for(int i=0;i<frqList.size();i++){
					ZrstRepEffectStatisticsBean bean = (ZrstRepEffectStatisticsBean)frqList.get(i);
					if(wwb.getNumberOfSheets() == 0 || wwb.getSheet("����ͳ��") == null){
						ws = wwb.createSheet("����ͳ��", wwb.getNumberOfSheets()+1);
		                ws.setColumnView(0, 15);
		                ws.setColumnView(1, 16);
		                ws.setColumnView(2, 16);
		                ws.setColumnView(3, 16);
//		                ws.setColumnView(4, 16);
//		                ws.setColumnView(5, 13);
					} else{
						ws = wwb.getSheet("����ͳ��");
					}
					if(beginRow71 == 0){
						beginRow71 = ws.getRows()+4;
						
						//��������
						ws.mergeCells(6,beginRow71-1,11,beginRow71-1);
		                ws.addCell(new Label(6,beginRow71-1,"����������̶�",wcfFHead));
		                
						ws.mergeCells(0,beginRow71-1,0,beginRow71);
						ws.mergeCells(1,beginRow71-1,1,beginRow71);
						ws.mergeCells(2,beginRow71-1,2,beginRow71);
						ws.mergeCells(3,beginRow71-1,3,beginRow71);
						ws.mergeCells(4,beginRow71-1,4,beginRow71);
						ws.mergeCells(5,beginRow71-1,5,beginRow71);
//						ws.mergeCells(6,beginRow71-1,6,beginRow71);
//						ws.mergeCells(7,beginRow71-1,7,beginRow71);
						ws.mergeCells(12,beginRow71-1,12,beginRow71);
						ws.mergeCells(13,beginRow71-1,13,beginRow71);
						ws.mergeCells(14,beginRow71-1,14,beginRow71);
						
		                ws.addCell(new Label(0,beginRow71-1,"Ƶ��",wcfFHead));
		                ws.addCell(new Label(1,beginRow71-1,"����",wcfFHead));
		                ws.addCell(new Label(2,beginRow71-1,"����ʱ��",wcfFHead));
		                ws.addCell(new Label(3,beginRow71-1,"ת������",wcfFHead));
//		                ws.addCell(new Label(4,beginRow71-1,"���䷽��",wcfFHead));
//		                ws.addCell(new Label(5,beginRow71-1,"���书��",wcfFHead));
		                ws.addCell(new Label(4,beginRow71-1,"������",wcfFHead));
		                ws.addCell(new Label(5,beginRow71-1,"�ղ����",wcfFHead));
		                ws.addCell(new Label(6,beginRow71,"5",wcfFHead));
		                ws.addCell(new Label(7,beginRow71,"4",wcfFHead));
		                ws.addCell(new Label(8,beginRow71,"3",wcfFHead));
		                ws.addCell(new Label(9,beginRow71,"2",wcfFHead));
		                ws.addCell(new Label(10,beginRow71,"1",wcfFHead));
		                ws.addCell(new Label(11,beginRow71,"0",wcfFHead));
		                ws.addCell(new Label(12,beginRow71-1,"������%",wcfFHead));
		                ws.addCell(new Label(13,beginRow71-1,"�������",wcfFHead));
		                ws.addCell(new Label(14,beginRow71-1,"��ע",wcfFHead));

		                ws.mergeCells(0,beginRow71-2,14,beginRow71-2);

		                ws.setRowView(beginRow71-2, 740);//���õ�һ�и߶�
		                ws.addCell(new Label(0, beginRow71-2, "Ƶ�ʿ�����ͳ�Ʊ�",wcfFTitle));
		                ws.setRowView(0, 740);//���õ�һ�и߶�
		                ws.setColumnView(4, 11);
		                ws.setColumnView(5, 6);
		                ws.setColumnView(6, 6);
		                ws.setColumnView(7, 5);
		                ws.setColumnView(8, 5);
		                ws.setColumnView(9, 5);
		                ws.setColumnView(10, 5);
		                ws.setColumnView(11, 5);
		                ws.setColumnView(12, 14);
		                ws.setColumnView(13, 14);
		                ws.setColumnView(14, 14);
					}

					ws.addCell(new Label(0,ws.getRows(), bean.getFreq(),wcfF2));
					ws.addCell(new Label(1,ws.getRows()-1, bean.getLanguage_name(),wcfF2));
					ws.addCell(new Label(2,ws.getRows()-1, bean.getPlay_time(),wcfF2));
					ws.addCell(new Label(3,ws.getRows()-1, bean.getTransmit_station(),wcfF2));
					ws.addCell(new Label(4,ws.getRows()-1, bean.getService_area(),wcfF2));
					ws.addCell(new Label(5,ws.getRows()-1, bean.getReceive_count(),wcfF2));
					ws.addCell(new Label(6,ws.getRows()-1, bean.getFen5(),wcfF2));
					ws.addCell(new Label(7,ws.getRows()-1, bean.getFen4(),wcfF2));
					ws.addCell(new Label(8,ws.getRows()-1,  bean.getFen3(),wcfF2));
					ws.addCell(new Label(9,ws.getRows()-1, bean.getFen2(),wcfF2));
					ws.addCell(new Label(10,ws.getRows()-1, bean.getFen1(),wcfF2));
					ws.addCell(new Label(11,ws.getRows()-1, bean.getFen0(),wcfF2));
					ws.addCell(new Label(12,ws.getRows()-1, bean.getAverage_listens(),wcfF2));
					if(Integer.parseInt(bean.getAverage_listens())>=80){
						ws.addCell(new Label(13,ws.getRows()-1, "�ɱ�֤����",wcfF2));
					}
					if(Integer.parseInt(bean.getAverage_listens())>=60&&Integer.parseInt(bean.getAverage_listens())<80){
						ws.addCell(new Label(13,ws.getRows()-1, "����������",wcfF2));
					}
					if(Integer.parseInt(bean.getAverage_listens())>=30&&Integer.parseInt(bean.getAverage_listens())<60){
						ws.addCell(new Label(13,ws.getRows()-1, "��ʱ������",wcfF2));
					}
					if(Integer.parseInt(bean.getAverage_listens())<30){
						ws.addCell(new Label(13,ws.getRows()-1, "��������",wcfF2));
					}
					ws.addCell(new Label(14,ws.getRows()-1, bean.getBak(),wcfF2));
				}
			}
			
		}
		
		
		// ÿ��ң��վ���ܿ�����
		if(ykz_listens.size()>0){
			WritableSheet ws=wwb.getSheet("����ͳ��");
			if(ws!=null){
				int startRow=ws.getRows()+2;
				ws.addCell(new Label(0,startRow,"ң��վ",wcfFHead));
				ws.addCell(new Label(0,startRow+1,"�ܿ�����",wcfFHead));
				for(int i=0;i<ykz_listens.size();i++){
					ws.setColumnView(i+1, 15);
					ASObject asobj = (ASObject)ykz_listens.get(i);
					ws.addCell(new Label(i+1,startRow,asobj.get("receive_name").toString(),wcfFHead));
					ws.addCell(new Label(i+1,startRow+1,asobj.get("listen").toString()+"%",wcfF2));
				}
			}
			
		}
	    	
			wwb.write();
	        wwb.close();
	        outputStream.close();
    }
    /**
     * ����ΪExcel
     * @param cdosCategoryKeyWords
     * @return
     */
    public void doExcel2(String msg,HttpServletRequest request,HttpServletResponse response) throws Exception{
		OutputStream os=null;

		WritableWorkbook wwb=null;
		WritableSheet ws=null;
		try 
		{
			os = response.getOutputStream();
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String curDatetime = sdf.format(cal.getTime());
	    response.reset();
        response.setHeader("Content-disposition", "attachment; filename=CategoryKeyWords"+curDatetime+".xls");
	    response.setContentType("application/msexcel");
			wwb = Workbook.createWorkbook(os);
			ws=wwb.createSheet("�ؼ�����Ŀƥ���б�",0);
			ws.getSettings().setDefaultColumnWidth(15);
			//������ͷ
			WritableFont wfc = new WritableFont(WritableFont.ARIAL,10,WritableFont.NO_BOLD,false,UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.RED);
            WritableCellFormat wcfFC = new WritableCellFormat(wfc);
            Label lId2HeadLabel = new Label(0,0,"���ID",wcfFC);
			Label strKeyWordHeadLabel = new Label(1,0,"�ؼ���",wcfFC);
			Label strCategoryConfigHeadLabel = new Label(2,0,"��Ŀ",wcfFC);
			Label dtModifyTimeHeadLabel = new Label(3,0,"����ʱ��",wcfFC);
			
			ws.addCell(lId2HeadLabel);
			ws.addCell(strKeyWordHeadLabel);
			ws.setColumnView(2, 50);
			ws.addCell(strCategoryConfigHeadLabel);
			ws.addCell(dtModifyTimeHeadLabel);
			
	        Label lId2Label = null;
			Label strKeyWordLabel = null;
			Label strCategoryConfigLabel = null;
			Label dtModifyTimeLabel = null;
			for(int i=1;i<=5;i++)
			{
				String lId2 = "//lId"; //ID
				String strKeyWord = "//�ؼ���"; //�ؼ���
				String strCategoryConfig = "//��Ŀ";//��Ŀ
				String dtModifyTime = "//����ʱ��";//����ʱ��
				lId2Label = new Label(0,i,lId2);
				strKeyWordLabel = new Label(1,i,strKeyWord);
				strCategoryConfigLabel = new Label(2,i,strCategoryConfig);
				dtModifyTimeLabel = new Label(3,i,dtModifyTime);
				ws.addCell(lId2Label);
				ws.addCell(strKeyWordLabel);
				ws.addCell(strCategoryConfigLabel);
				ws.addCell(dtModifyTimeLabel);
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				wwb.write();
				wwb.close();
				os.close();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}
    
    /**
     * ����������ID��ѯ��ϸ�������ݣ�ҳ����Ҫ��
     * @detail  
     * @method  
     * @param reportId
     * @param reportType �������� 1003������̨Ч������ͳ��  1004���������Ч������ͳ��
     * @return
     * @throws Exception 
     * @return  ASObject  
     * @author  zhaoyahui
     * @version 2013-5-30 ����11:08:24
     */
    public ASObject queryDeatiReport(String reportId,String reportType) throws Exception{
    	String descSql="select * from zrst_rep_effect_statistics_tab  where report_id="+reportId;
    	String sqlPageTotal = "select t.receive_code  from zrst_rep_effect_statistics_tab t where  t.report_id="+reportId+" group by t.receive_code";
    	String sqlPage = StringTool.pageSql(descSql, 1, 1000);
		ASObject objRes = new ASObject();
		ArrayList list = new ArrayList();
		GDSet pageGD = DbComponent.Query(sqlPage);
		GDSet coutn = DbComponent.Query(sqlPageTotal);
		int count = 0;

		String xml21 ="<chart yAxisName='������ ��λ��(%)' xAxisName='����̨' caption='����̨������ͳ��'  baseFontSize='15' showBorder='1' imageSave='1'>";
		String xml31 ="<chart yAxisName='������ ��λ��(%)' xAxisName='����' caption='���Կ�����ͳ��'  baseFontSize='15' showBorder='1' imageSave='1'>";
		String xml41 ="<chart yAxisName='������ ��λ��(%)' xAxisName='����' caption='����������ͳ��'  baseFontSize='15' showBorder='1' imageSave='1'>";
		if(reportType.equals("1004")){
			xml21 ="<chart yAxisName='������ ��λ��(%)' xAxisName='ת������' caption='ת������������ͳ��'  baseFontSize='15' showBorder='1' imageSave='1'>";
		}
		for(int i=0; i<pageGD.getRowCount(); i++){
			String data_id = pageGD.getString(i, "data_id");
			String report_id = pageGD.getString(i, "report_id");
			String play_time = pageGD.getString(i, "play_time");
			String language_name = pageGD.getString(i, "language_name");
			String freq = pageGD.getString(i, "freq");
			String transmit_station = pageGD.getString(i, "transmit_station");
			String transmit_direction = pageGD.getString(i, "transmit_direction");
			String transmit_power = pageGD.getString(i, "transmit_power");
			String service_area = pageGD.getString(i, "service_area");
			String receive_code = pageGD.getString(i, "receive_code");
			String receive_count = pageGD.getString(i, "receive_count");
			String fen0 = pageGD.getString(i, "fen0");
			String fen1 = pageGD.getString(i, "fen1");
			String fen2 = pageGD.getString(i, "fen2");
			String fen3 = pageGD.getString(i, "fen3");
			String fen4 = pageGD.getString(i, "fen4");
			String fen5 = pageGD.getString(i, "fen5");
			String listen = pageGD.getString(i, "listen");
			String listen_middle = pageGD.getString(i, "listen_middle");
			String bak = pageGD.getString(i, "bak");
			String receive_name = pageGD.getString(i, "receive_name");
			String transmit_station_listens = pageGD.getString(i, "transmit_station_listens");
			String receive_name_total_hours = pageGD.getString(i, "receive_name_total_hours");
			String receive_listens = pageGD.getString(i, "receive_listens");
			String language_name_listens = pageGD.getString(i, "language_name_listens");
			String state_name = pageGD.getString(i, "state_name");
			String month_listens = pageGD.getString(i, "month_listens");
			String average_listens = pageGD.getString(i, "average_listens");
			String sub_report_type = pageGD.getString(i, "sub_report_type");
			String report_type = pageGD.getString(i, "report_type");
			String all_listens = pageGD.getString(i, "all_listens");
			ASObject detailObj = new ASObject();
			detailObj.put("sub_report_type", sub_report_type);
			detailObj.put("xml", "");
			
			if(sub_report_type.equals("11") || sub_report_type.equals("71")){
				detailObj.put("play_time", play_time);
				detailObj.put("language_name", language_name);
				detailObj.put("freq", freq);
				detailObj.put("transmit_station", transmit_station);
				detailObj.put("transmit_direction", transmit_direction);
				detailObj.put("transmit_power", transmit_power);
				detailObj.put("service_area", service_area);
				detailObj.put("receive_code", receive_code);
				detailObj.put("receive_count", receive_count);
				detailObj.put("fen0", fen0);
				detailObj.put("fen1", fen1);
				detailObj.put("fen2", fen2);
				detailObj.put("fen3", fen3);
				detailObj.put("fen4", fen4);
				detailObj.put("fen5", fen5);
				detailObj.put("listen", listen);
				detailObj.put("listen_middle", listen_middle);
				detailObj.put("bak", bak);
				detailObj.put("average_listens", average_listens);

				list.add(detailObj);
			} else if(sub_report_type.equals("21")){

				String[] headStationArr = transmit_station_listens.split(",");
				String showName = receive_name;
				if(showName == null || showName.equals(""))
					showName = service_area;
				if(showName == null || showName.equals(""))
					showName = state_name;
				if(showName == null || showName.equals(""))
					showName = "�ܼ�";
				if(showName.equals("�ܼ�")){
					ArrayList<String> sortlist = new ArrayList<String>();
					HashMap<String,String> map = new HashMap<String,String>();
					
	 				for(String ones:headStationArr){
	 					String[] onesArr = ones.split("_");
						map.put(onesArr[0], onesArr[3]);
						sortlist.add(onesArr[0]);
					}
					Collections.sort(sortlist);
					for(int k=0;k<sortlist.size();k++){
						xml21+="<set label='"+sortlist.get(k)+"' value='"+map.get(sortlist.get(k))+"'/>\r";
					}
	 				xml21+="</chart>";
	 				detailObj.put("xml", xml21);
					list.add(detailObj);
				}
			} else if(sub_report_type.equals("22")){

//				list.add(detailObj);
			} else if(sub_report_type.equals("23")){
//				var listensArr:Array = (receive_listens as String).split("_");
//				var obj:Object = new Object();
//				obj.transmit_station = transmit_station;
//				obj.guarantee_freqhour = listensArr[0];
//				obj.basic_freqhour = listensArr[1];
//				obj.sometimes_freqhour = listensArr[2];
//				obj.no_receive_freqhour = listensArr[3];
//				rArr23.addItem(obj);
//				list.add(detailObj);
			} else if(sub_report_type.equals("31")){
				
				String[] lanListenArr = language_name_listens.split(",");
				String showName = receive_name;
				if(showName == null || showName.equals(""))
					showName = service_area;
				if(showName == null || showName.equals(""))
					showName = state_name;
				if(showName == null || showName.equals(""))
					showName = "�ܼ�";
				if(showName.equals("�ܼ�")){
					ArrayList<String> sortlist = new ArrayList<String>();
					HashMap<String,String> map = new HashMap<String,String>();
					
	 				for(String ones:lanListenArr){
	 					String[] onesArr = ones.split("_");
						map.put(onesArr[0], onesArr[3]);
						sortlist.add(onesArr[0]);
					}
					Collections.sort(sortlist);
					for(int k=0;k<sortlist.size();k++){
						xml31+="<set label='"+sortlist.get(k)+"' value='"+map.get(sortlist.get(k))+"'/>\r";
					}
					
	 				xml31+="</chart>";
	 				detailObj.put("xml", xml31);
					list.add(detailObj);
				}
//				list.add(detailObj);
			} else if(sub_report_type.equals("32")){
//				list.add(detailObj);
			} else if(sub_report_type.equals("41")){
				String[] alllistensArr = all_listens.split(",");
				for(int iii=0;iii<alllistensArr.length;iii++){
					String[] detailArr = alllistensArr[iii].split("_");
					xml41+="<set label='"+detailArr[0]+"' value='"+detailArr[3]+"'/>\r";
				}
				if(xml41.indexOf("<set label")>-1){
					xml41+="</chart>";
					detailObj.put("xml", xml41);
					list.add(detailObj);
				}
			} else if(sub_report_type.equals("51")){
				receive_listens = receive_listens.replaceAll("\\.0", "");
				String[] listensArr = receive_listens.split("_");
				if(receive_name == null || receive_name.equals(""))
					receive_name = state_name;
				if(receive_name == null || receive_name.equals(""))
					receive_name = service_area;
				if(receive_name == null || receive_name.equals(""))
					receive_name = "ȫ��";
				detailObj.put("receive_name",receive_name);
				detailObj.put("guarantee_freqhour",listensArr[0]);
				detailObj.put("basic_freqhour",listensArr[1]);
				detailObj.put("sometimes_freqhour",listensArr[2]);
				detailObj.put("no_receive_freqhour",listensArr[3]);
				detailObj.put("total_freqhour",listensArr[4]);
				list.add(detailObj);
			} else if(sub_report_type.equals("61")){
				if(receive_name == null || receive_name.equals(""))
					receive_name = state_name;
				if(receive_name == null || receive_name.equals(""))
					receive_name = service_area;
				if(receive_name == null || receive_name.equals(""))
					receive_name = "ȫ��";
				month_listens = month_listens.replaceAll("\\$", " ");
				String[] mon = month_listens.split("_");
				detailObj.put("receive_name",receive_name);
				detailObj.put("month1",mon[0]);
				detailObj.put("month2",mon[1]);
				detailObj.put("month3",mon[2]);
				detailObj.put("month4",mon[3]);
				detailObj.put("month5",mon[4]);
				detailObj.put("month6",mon[5]);
				detailObj.put("month7",mon[6]);
				detailObj.put("month8",mon[7]);
				detailObj.put("month9",mon[8]);
				detailObj.put("month10",mon[9]);
				detailObj.put("month11",mon[10]);
				detailObj.put("month12",mon[11]);
				list.add(detailObj);
			}
			
		}
		for(int j=0; j<coutn.getRowCount(); j++){
			if(coutn.getString(j, "receive_code") != null && !coutn.getString(j, "receive_code").equals("")) {
				count++;
			}
		}
		ASObject detailObj = new ASObject();
		detailObj.put("resultList", list);
		detailObj.put("resultTotal", count);
    	return detailObj;
    }
    public void doExcelCompare1(String msg,HttpServletRequest request,HttpServletResponse response) throws Exception{
    	Element root = StringTool.getXMLRoot(msg);
		String reprottype = root.getChildText("reprottype");//1003������̨Ч������ͳ�ƣ�1004���������Ч������ͳ��
		String reportIdOld = root.getChildText("reportIdOld");
		String reportIdNew = root.getChildText("reportIdNew");
    	ASObject obj = new ASObject();
    	obj.put("reportId", reportIdOld);
    	obj.put("sub_report_type", "11");
    	ArrayList listOld = queryDetailReport(obj);
    	obj.put("reportId", reportIdNew);
    	ArrayList listNew = queryDetailReport(obj);
    	
    	
        	String myName = "";
    		if(reprottype.equals("1003")){
    			myName="����̨";
    		} else if(reprottype.equals("1004")){
    			myName="�������";
    		}
        	String fileName=myName+"վ��Ч��ͳ�ƶԱȱ�";
        	
    		OutputStream outputStream = response.getOutputStream();
    		response.setContentType("application/vnd.ms-excel");
    		response.reset();
    		response.setHeader("Location", "Export.xls");
    		response.setHeader("Expires", "0");
//    		response.setHeader("Content-Disposition",new String(("filename="+fileName).getBytes("GBK"),"ISO-8859-1")+".xls");
            response.setHeader("Content-disposition", "attachment; filename="+new String((fileName).getBytes("GBK"),"ISO-8859-1")+".xls");
           
        
    		WritableWorkbook wwb =Workbook.createWorkbook(outputStream);
    		int sheetNum = 0;
    		//�ӱ�������  11������̨�㲥Ч��ͳ�ƣ�21������̨���岥��Ч��ͳ��1��22������̨���岥��Ч��ͳ��2��23������̨���岥��Ч��ͳ��3��31���������岥��Ч��ͳ��1��
    		//32���������岥��Ч��ͳ��2��41����ң��վ���������������ޡ�������ͳ�ƣ�51����ң��վ���������������ޡ��ɱ�֤����Ƶʱͳ�ƣ�61�����¿����ʶԱȣ�71��Ƶ��ƽ��������ͳ�Ʊ�
    		
    		 WritableFont wfTitle = new WritableFont(WritableFont.createFont("����"), 12,WritableFont.BOLD, false);
             WritableCellFormat wcfFTitle = new WritableCellFormat(wfTitle);
    		wcfFTitle.setAlignment(jxl.format.Alignment.CENTRE);
    		wcfFTitle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
    			 
    		WritableFont wfHead = new WritableFont(WritableFont.createFont("����"), 10,WritableFont.BOLD, false);
    		WritableCellFormat wcfFHead = new WritableCellFormat(wfHead);
    		wcfFHead.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);//���߿� 
    		wcfFHead.setWrap(true);//�Զ�����
             //����Number�ĸ�ʽ
             //jxl.write.NumberFormat nf = new jxl.write.NumberFormat("0.00"); 
             //jxl.write.WritableCellFormat priceformat = new jxl.write.WritableCellFormat(nf);
             // ��ˮƽ���뷽ʽָ��Ϊ�����
    		wcfFHead.setAlignment(jxl.format.Alignment.CENTRE);
             // �Ѵ�ֱ���뷽ʽָ��Ϊ���ж���
    		wcfFHead.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
    		
    		WritableFont wf1 = new WritableFont(WritableFont.createFont("����"), 10,WritableFont.NO_BOLD, false);
            jxl.write.WritableCellFormat wcfF2 = new jxl.write.WritableCellFormat(wf1);
            wcfF2.setAlignment(jxl.format.Alignment.CENTRE);
            wcfF2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
            wcfF2.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); 
            WritableSheet ws =  null;
            
    		if(listOld.size()>0){
    			for(int i=0;i<listOld.size();i++){
        			ZrstRepEffectStatisticsBean bean = (ZrstRepEffectStatisticsBean)listOld.get(i);
        			ws = wwb.createSheet(bean.getReceive_name(), wwb.getNumberOfSheets()+1);
        			if(!bean.getSub_report_type().equals("11")){//�㲥Ч��ͳ��
        				if(wwb.getNumberOfSheets() == 0 || wwb.getSheet(bean.getReceive_name()) == null){
        					
                             //��������
                             ws.addCell(new Label(0,1,"����ʱ��",wcfFHead));
                             ws.addCell(new Label(1,1,"����",wcfFHead));
                             ws.addCell(new Label(2,1,"Ƶ��",wcfFHead));
                             ws.addCell(new Label(3,1,"����̨",wcfFHead));
                             ws.addCell(new Label(4,1,"���䷽��",wcfFHead));
                             ws.addCell(new Label(5,1,"���书��",wcfFHead));
                             ws.addCell(new Label(6,1,"������",wcfFHead));
                             ws.addCell(new Label(7,1,"�ղ�ص�",wcfFHead));
                             ws.addCell(new Label(8,1,reportIdOld+"������%",wcfFHead));
                             ws.addCell(new Label(9,1,reportIdNew+"������%",wcfFHead));

                             ws.mergeCells(0,0,9,0);
                            
                             ws.addCell(new Label(0, 0, bean.getReceive_code()+bean.getReceive_name()+"ң��վ"+myName+"�㲥Ч��ͳ�ƶԱȱ�",wcfFTitle));
                             ws.setRowView(0, 740);//���õ�һ�и߶�
                             ws.setRowView(1, 550); 
                             ws.setColumnView(0, 13);
                             ws.setColumnView(1, 5);
                             ws.setColumnView(2, 7);
                             ws.setColumnView(3, 9);
                             ws.setColumnView(4, 5);
                             ws.setColumnView(5, 5);
                             ws.setColumnView(6, 11);
                             ws.setColumnView(7, 7);
                             ws.setColumnView(8, 17);
                             ws.setColumnView(9, 17);
                             
        				} else  {
        					ws = wwb.getSheet(bean.getReceive_name()); 
        				}
        				int curRow = ws.getRows();
        				if(bean.getPlay_time().equals("����Ч��")){
        					continue;
        				}

        				ws.addCell(new Label(0,curRow, bean.getPlay_time(),wcfF2));
        				ws.addCell(new Label(1,curRow, bean.getLanguage_name(),wcfF2));
        				ws.addCell(new Label(2,curRow, bean.getFreq(),wcfF2));
        				ws.addCell(new Label(3,curRow, bean.getTransmit_station(),wcfF2));
        				ws.addCell(new Label(4,curRow, bean.getTransmit_direction(),wcfF2));
        				ws.addCell(new Label(5,curRow, bean.getTransmit_power(),wcfF2));
        				ws.addCell(new Label(6,curRow, bean.getService_area(),wcfF2));
        				ws.addCell(new Label(7,curRow, bean.getReceive_code(),wcfF2));
        				ws.addCell(new Label(8,curRow, bean.getListen(),wcfF2));
        				ws.addCell(new Label(9,curRow, "",wcfF2));
        				bean.setData_id(curRow+"");
        				

        			}
        		}
    		}
    		if(listNew.size()>0){
    			for(int j=0;j<listNew.size();j++){
    	   			ZrstRepEffectStatisticsBean beanNew = (ZrstRepEffectStatisticsBean)listNew.get(j);
        			
    	   			if(!beanNew.getSub_report_type().equals("11")){//����̨�㲥Ч��ͳ��
        				if(wwb.getNumberOfSheets() == 0 || wwb.getSheet(beanNew.getReceive_name()) == null){
        					ws = wwb.createSheet(beanNew.getReceive_name(), wwb.getNumberOfSheets()+2);
        					 //��������
                            ws.addCell(new Label(0,1,"����ʱ��",wcfFHead));
                            ws.addCell(new Label(1,1,"����",wcfFHead));
                            ws.addCell(new Label(2,1,"Ƶ��",wcfFHead));
                            ws.addCell(new Label(3,1,"����̨",wcfFHead));
                            ws.addCell(new Label(4,1,"���䷽��",wcfFHead));
                            ws.addCell(new Label(5,1,"���书��",wcfFHead));
                            ws.addCell(new Label(6,1,"������",wcfFHead));
                            ws.addCell(new Label(7,1,"�ղ�ص�",wcfFHead));
                            ws.addCell(new Label(8,1,reportIdOld+"������%",wcfFHead));
                            ws.addCell(new Label(9,1,reportIdNew+"������%",wcfFHead));

                            ws.mergeCells(0,0,9,0);
                           
                            ws.addCell(new Label(0, 0, beanNew.getReceive_code()+beanNew.getReceive_name()+"ң��վ"+myName+"�㲥Ч��ͳ�ƶԱȱ�",wcfFTitle));
                            ws.setRowView(0, 740);//���õ�һ�и߶�
                            ws.setRowView(1, 550); 
                            ws.setColumnView(0, 13);
                            ws.setColumnView(1, 5);
                            ws.setColumnView(2, 7);
                            ws.setColumnView(3, 9);
                            ws.setColumnView(4, 5);
                            ws.setColumnView(5, 5);
                            ws.setColumnView(6, 11);
                            ws.setColumnView(7, 7);
                            ws.setColumnView(8, 17);
                            ws.setColumnView(9, 17);
                            
    	   				} else  {
    	   					ws = wwb.getSheet(beanNew.getReceive_name()); 
    	   				}
    					int curRow = ws.getRows();
        				if(beanNew.getPlay_time().equals("����Ч��")){
        					continue;
        				}
        				if(listOld.size()>0){
        					for(int k=0;k<listOld.size();k++){
        						ZrstRepEffectStatisticsBean bean = (ZrstRepEffectStatisticsBean)listOld.get(k);
        			   			if(	beanNew.getPlay_time().equals(bean.getPlay_time()) && beanNew.getLanguage_name().equals(bean.getLanguage_name()) && 
        			   				beanNew.getFreq().equals(bean.getFreq()) && beanNew.getTransmit_station().equals(bean.getTransmit_station()) && 
        			   				beanNew.getTransmit_direction().equals(bean.getTransmit_direction()) && beanNew.getTransmit_power().equals(bean.getTransmit_power()) && 
        			   				beanNew.getService_area().equals(bean.getService_area()) && beanNew.getReceive_code().equals(bean.getReceive_code()) ){
        			   				ws.addCell(new Label(9,Integer.parseInt(bean.getData_id()), beanNew.getListen(),wcfF2));
        			   				curRow=-10;
        			   				break;
        			   			}
        					}
        				}
    					
    					if(curRow!=-10){
    						ws.addCell(new Label(0,curRow, beanNew.getPlay_time(),wcfF2));
    	    				ws.addCell(new Label(1,curRow, beanNew.getLanguage_name(),wcfF2));
    	    				ws.addCell(new Label(2,curRow, beanNew.getFreq(),wcfF2));
    	    				ws.addCell(new Label(3,curRow, beanNew.getTransmit_station(),wcfF2));
    	    				ws.addCell(new Label(4,curRow, beanNew.getTransmit_direction(),wcfF2));
    	    				ws.addCell(new Label(5,curRow, beanNew.getTransmit_power(),wcfF2));
    	    				ws.addCell(new Label(6,curRow, beanNew.getService_area(),wcfF2));
    	    				ws.addCell(new Label(7,curRow, beanNew.getReceive_code(),wcfF2));
    	    				ws.addCell(new Label(8,curRow, "",wcfF2));
    	    				ws.addCell(new Label(9,curRow, beanNew.getListen(),wcfF2));
    					}
    				}
    			}
    		}
			wwb.write();
	        wwb.close();
	        outputStream.close();
    }
    
    public void doExcelCompare(String msg,HttpServletRequest request,HttpServletResponse response) throws Exception{
    	String fileName="վ��Ч��ͳ�ƶԱȱ�";
    	OutputStream outputStream = response.getOutputStream();
		response.setContentType("application/vnd.ms-excel");
		response.reset();
		response.setHeader("Location", "Export.xls");
		response.setHeader("Expires", "0");
//		response.setHeader("Content-Disposition",new String(("filename="+fileName).getBytes("GBK"),"ISO-8859-1")+".xls");
        response.setHeader("Content-disposition", "attachment; filename="+new String((fileName).getBytes("GBK"),"ISO-8859-1")+".xls");
        
		WritableWorkbook wwb =Workbook.createWorkbook(outputStream);
		int sheetNum = 0;
		//�ӱ�������  11������̨�㲥Ч��ͳ�ƣ�21������̨���岥��Ч��ͳ��1��22������̨���岥��Ч��ͳ��2��23������̨���岥��Ч��ͳ��3��31���������岥��Ч��ͳ��1��
		//32���������岥��Ч��ͳ��2��41����ң��վ���������������ޡ�������ͳ�ƣ�51����ң��վ���������������ޡ��ɱ�֤����Ƶʱͳ�ƣ�61�����¿����ʶԱȣ�71��Ƶ��ƽ��������ͳ�Ʊ�
		
		 WritableFont wfTitle = new WritableFont(WritableFont.createFont("����"), 12,WritableFont.BOLD, false);
         WritableCellFormat wcfFTitle = new WritableCellFormat(wfTitle);
		wcfFTitle.setAlignment(jxl.format.Alignment.CENTRE);
		wcfFTitle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			 
		WritableFont wfHead = new WritableFont(WritableFont.createFont("����"), 10,WritableFont.BOLD, false);
		WritableCellFormat wcfFHead = new WritableCellFormat(wfHead);
		wcfFHead.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);//���߿� 
		wcfFHead.setWrap(true);//�Զ�����
         //����Number�ĸ�ʽ
         //jxl.write.NumberFormat nf = new jxl.write.NumberFormat("0.00"); 
         //jxl.write.WritableCellFormat priceformat = new jxl.write.WritableCellFormat(nf);
         // ��ˮƽ���뷽ʽָ��Ϊ�����
		wcfFHead.setAlignment(jxl.format.Alignment.CENTRE);
         // �Ѵ�ֱ���뷽ʽָ��Ϊ���ж���
		wcfFHead.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		
		WritableFont wf1 = new WritableFont(WritableFont.createFont("����"), 10,WritableFont.NO_BOLD, false);
        jxl.write.WritableCellFormat wcfF2 = new jxl.write.WritableCellFormat(wf1);
        wcfF2.setAlignment(jxl.format.Alignment.CENTRE);
        wcfF2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        wcfF2.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); 
        
        jxl.write.WritableCellFormat wcfF3 = new jxl.write.WritableCellFormat(wf1);
        wcfF3.setAlignment(jxl.format.Alignment.CENTRE);
        wcfF3.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        wcfF3.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); 
        wcfF3.setBackground(Colour.YELLOW2);//���õ�Ԫ�񱳾�ɫ
        wwb.write();
        wwb.close();
        outputStream.close();
    }
    
   
    /**
     * ���ݿ�ʼ����ʱ�䡢�������Ͳ�ѯ�����б�
     * @detail  
     * @method  
     * @param asobj
     * @return
     * @throws Exception 
     * @return  Object  
     * @author  zhaoyahui
     * @version 2013-7-8 ����04:49:47
     */
    public Object queryReportBeans(ASObject asobj) throws Exception{
		String reprottype  = (String)asobj.get("reprottype");//1003������̨Ч������ͳ�ƣ�1004���������Ч������ͳ��
		String starttime  = (String)asobj.get("starttime");
		String endtime  = (String)asobj.get("endtime");
    	String  report_sql="     SELECT t.* FROM zrst_rep_tab t,dic_report_type_tab tt" +
	 		"  WHERE  t.report_type=tt.id and report_type = "+reprottype+
			" and END_DATETIME >= TO_DATE('"+starttime+"','yyyy-mm-dd HH24:MI:SS')"+
			" and START_DATETIME <= TO_DATE('"+endtime+"','yyyy-mm-dd HH24:MI:SS') order by report_date desc";

    	GDSet gdSet = gdSet= DbComponent.Query(report_sql);

		String classpath = ReportBean.class.getName();
		ArrayList list = StringTool.converGDSetToBeanList(gdSet, classpath);
		return list;
    }
    /**
     * Ϊ�˹���С��Ƶ�����ݴ�������ͳ�Ʒ���
     * @param starttime
     * @param endtime
     * @param my_report_type
     * @param report_type
     * @param asobj
     * @param report_id
     * @return
     * @throws Exception
     */
    public LinkedHashMap statisticsReportCondition11new(String starttime, String endtime,String my_report_type,String report_type,ASObject asobj,String report_id) throws Exception{
		String headcodes = (String)asobj.get("headcodes");
		String rule = (String)asobj.get("rule");//1��������ʱ��ͳ�� 2������Сʱͳ�� 3����һСʱͳ��
		String sql  = "";
		if(my_report_type.equals("1")){//����̨
			sql = "select runplan.start_time ||'-'|| runplan.end_time as play_time,lan.language_name, runplan.freq,station.name stationname,runplan.direction," +
						" runplan.power, head.code  headcode,head.service_name service_name_headend,head.type_id, head.shortname," +
						" mark.counto,runplan.service_area,runplan.valid_start_time ,runplan.valid_end_time,mark.start_datetime," +
						" dis.runplan_id as dis_runplan_id,dis.disturb,runplan.program_type  " +
						" from radio_mark_view_xiaohei mark," +
					//	" radio_stream_result_tab record," +
						" zres_runplan_chaifen_tab runplan," +
						" zdic_language_tab lan ," +
						" res_transmit_station_tab station," +
						" res_headend_tab head," +
						" (select * from zres_runplan_disturb_tab where  valid_end_time>to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss')) dis  "+
						//" zres_runplan_disturb_tab dis " +
								" where mark.runplan_id=runplan.runplan_id and  runplan.language_id=lan.language_id " +
								" and runplan.station_id=station.station_id and head.code=mark.head_code" +
								" and mark.report_type=1 " +//Ч��¼��
							
								//" and runplan.is_delete=0 " +
								" and runplan.valid_end_time>=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') "+
							
								" and runplan.parent_id = dis.runplan_id(+) " +
								//" and dis.is_delete=0 " +
								" and runplan.runplan_type_id='"+my_report_type+"'"+
								" and mark.start_datetime>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss')" +
								" and mark.end_datetime<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') ";
			if(headcodes!=null&&!headcodes.equalsIgnoreCase("")){
				if(headcodes.split(",").length>1){
					String[] ss = headcodes.split(",");
					String newsql="";
					for(int m=0;m<ss.length;m++){
						newsql+=" mark.HEAD_CODE like '%"+ss[m]+"%' or";
					}
					sql+=" and ("+newsql.substring(0, newsql.length()-2)+")";
				}else{
					//sql+="and '"+headcodes.substring(0, headcodes.length()-1)+"' like '%'||head.code||'%' ";
					sql+=" and mark.HEAD_CODE like '%"+headcodes.substring(0, headcodes.length()-1)+"%'";
				}
				
			}
			
			sql+=" order by head.shortname,station.name,runplan.freq";
		} else if(my_report_type.equals("2")){//�������
			sql = "select runplan.runplan_id,runplan.start_time ||'-'|| runplan.end_time as play_time,lan.language_name, runplan.freq,runplan.direction,runplan.redisseminators," +
			" runplan.power, head.code  headcode,head.service_name service_name_headend,head.type_id, mark.HEADNAME shortname," +
			" mark.counto,runplan.service_area,runplan.valid_start_time ,runplan.valid_end_time,mark.start_datetime," +
			" dis.runplan_id as dis_runplan_id,dis.disturb,runplan.program_type  " +
			" from radio_mark_view_xiaohei mark," +
			//" radio_stream_result_tab record," +
			" zres_runplan_chaifen_tab runplan," +
			" zdic_language_tab lan ," +
			" res_headend_tab head," +
			" (select * from zres_runplan_disturb_tab where  valid_end_time>to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss')) dis  "+
			//" zres_runplan_disturb_tab dis " +
					" where mark.runplan_id=runplan.runplan_id and  runplan.language_id=lan.language_id " +
					"  and head.code=mark.head_code" +
					" and mark.report_type=1 " +//Ч��¼��
					//" and runplan.is_delete=0 " +
					//" and runplan.valid_end_time>=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') "+
				
				//	" and mark.is_delete=0 " +
					" and runplan.parent_id = dis.runplan_id(+) " +
					//" and dis.is_delete=0 " +
					" and runplan.runplan_type_id='"+my_report_type+"'"+
					" and mark.start_datetime>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss')" +
					" and mark.end_datetime<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') ";
			if(headcodes!=null&&!headcodes.equalsIgnoreCase("")){
				if(headcodes.split(",").length>1){
					String[] ss = headcodes.split(",");
					String newsql="";
					for(int m=0;m<ss.length;m++){
						newsql+=" mark.HEAD_CODE like '%"+ss[m]+"%' or";
					}
					sql+=" and ("+newsql.substring(0, newsql.length()-2)+")";
				}else{
					//sql+="and '"+headcodes.substring(0, headcodes.length()-1)+"' like '%'||head.code||'%' ";
					sql+=" and mark.HEAD_CODE like '%"+headcodes.substring(0, headcodes.length()-1)+"%'";
				}
				
			}
			sql+=" order by head.shortname,runplan.freq";
		}
				 GDSet gd = DbComponent.Query(sql);
				 LinkedHashMap map = new LinkedHashMap();
				 for(int i=0;i<gd.getRowCount();i++){
					 String program_type = gd.getString(i, "program_type");//��Ŀ����
					//��ע��Ϣ��ͬ��Ƶ���������Ϣ����룬���ڿ����ʵ���60%δ����ͬ��Ƶ������Ϣ����Ҫ�ڱ�ע��Ϣ��ֱ�Ӽ����ź�����������
					 String dis_runplan_id = gd.getString(i, "dis_runplan_id");//������Ϣ������ͼid
					 String disturb = gd.getString(i, "disturb");//������Ϣ
					 String play_time = gd.getString(i , "play_time");
					 String start_datetime = gd.getString(i, "start_datetime");
					 String runplan_id = gd.getString(i, "runplan_id");//����ͼid
					 if(!rule.equals("1")){
						 play_time = getPlayTime(rule,start_datetime);
					 }
					 String language_name = gd.getString(i, "language_name");
					 String freq = gd.getString(i, "freq");
					 String valid_start_time = gd.getString(i, "valid_start_time");
					 String valid_end_time = gd.getString(i, "valid_end_time");
					 String stationname = "";
					 String runplan_type_id = "";//1������̨����ͼ2���������
					 if(my_report_type.equals("2")){
						 String redisseminators = gd.getString(i, "redisseminators");//ת������
						 if(redisseminators.equals("")){
							 redisseminators = " ";
						 }
						 stationname = redisseminators;
						 runplan_type_id = "2";
					 } else{
						 stationname = gd.getString(i, "stationname");
						 runplan_type_id = "1";
					 }
					 String direction = gd.getString(i, "direction");
					 String power = gd.getString(i, "power");
					 String type_id = gd.getString(i, "type_id");
					 String headcode = gd.getString(i, "headcode");
					 String shortname = gd.getString(i, "shortname");
					 if("102".equals(type_id) && ( headcode.endsWith("A") || headcode.endsWith("B")||headcode.endsWith("C") || headcode.endsWith("D")|| headcode.endsWith("E") || headcode.endsWith("F") || headcode.endsWith("G")  )){
								 headcode = headcode.substring(0, headcode.length()-1);
						// shortname = shortname.substring(0, shortname.length()-1);
					 }
					 if("102".equals(type_id) && ( shortname.endsWith("A") || shortname.endsWith("B")||shortname.endsWith("C") || shortname.endsWith("D") || shortname.endsWith("E")|| shortname.endsWith("F")|| shortname.endsWith("G"))){
								shortname = shortname.substring(0, shortname.length()-1);
					 }
					 if(headcodes == null || headcodes.equals("") || headcodes.indexOf(headcode+",")>=0){
					 } else{
						 continue;
					 }
					 String counto = gd.getString(i, "counto");
					 String service_area = gd.getString(i, "service_area");
					 if(service_area == null || service_area.equals("")){
						 throw new Exception(shortname+"�ķ�����Ϊ�գ���������");
					 }
					 int countoNum = Integer.parseInt(counto);
					 String[] service_areaArr = service_area.split(",");
					 for(int ai=0;ai<service_areaArr.length;ai++){
						 service_area = service_areaArr[ai];
						 String mapKey = headcode+play_time+language_name+freq+stationname+direction+power+service_area;
						 
						 ZrstRepEffectStatisticsBean allBean = (ZrstRepEffectStatisticsBean)map.get(headcode+"����Ч��");
						 if(allBean == null){
							 double fen0 = countoNum==0?1:0;
							 double fen1 = countoNum==1?1:0;
							 double fen2 = countoNum==2?1:0;
							 double fen3 = countoNum==3?1:0;
							 double fen4 = countoNum==4?1:0;
							 double fen5 = countoNum==5?1:0;
							 allBean = new ZrstRepEffectStatisticsBean();
							 allBean.setReceive_name(shortname);
							 allBean.setReceive_code(headcode);
							 allBean.setPlay_time("����Ч��");
							 allBean.setReceive_count("1");
							 allBean.setFen0(fen0+"");
							 allBean.setFen1(fen1+"");
							 allBean.setFen2(fen2+"");
							 allBean.setFen3(fen3+"");
							 allBean.setFen4(fen4+"");
							 allBean.setFen5(fen5+"");
							 allBean.setListen(new BigDecimal((fen3+fen4+fen5)*100/1).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
							 allBean.setListen_middle(countoNum+"");//��һ��
							 allBean.getListen_middleList().add(countoNum);
							 allBean.setBak("");
							 allBean.setSub_report_type("11");
							 allBean.setReport_type(my_report_type);
							 allBean.setReport_id(report_id);
							 map.put(headcode+"����Ч��", allBean);
						 } else{
							 int receive_count_all = Integer.parseInt(allBean.getReceive_count());
							 receive_count_all++;
							 allBean.setReceive_count(receive_count_all+"");
							 
							 double fen0_all = countoNum==0?Double.parseDouble(allBean.getFen0())+1:Double.parseDouble(allBean.getFen0());
							 double fen1_all = countoNum==1?Double.parseDouble(allBean.getFen1())+1:Double.parseDouble(allBean.getFen1());
							 double fen2_all = countoNum==2?Double.parseDouble(allBean.getFen2())+1:Double.parseDouble(allBean.getFen2());
							 double fen3_all = countoNum==3?Double.parseDouble(allBean.getFen3())+1:Double.parseDouble(allBean.getFen3());
							 double fen4_all = countoNum==4?Double.parseDouble(allBean.getFen4())+1:Double.parseDouble(allBean.getFen4());
							 double fen5_all = countoNum==5?Double.parseDouble(allBean.getFen5())+1:Double.parseDouble(allBean.getFen5());
							 allBean.setFen0(fen0_all+"");
							 allBean.setFen1(fen1_all+"");
							 allBean.setFen2(fen2_all+"");
							 allBean.setFen3(fen3_all+"");
							 allBean.setFen4(fen4_all+"");
							 allBean.setFen5(fen5_all+"");
							 allBean.setListen(new BigDecimal((fen3_all+fen4_all+fen5_all)*100/receive_count_all).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
							 allBean.getListen_middleList().add(countoNum);
							 ArrayList mList_all = allBean.getListen_middleList();
							 Collections.sort(mList_all);
							 int middleNum_all = (int) Math.ceil(mList_all.size()/2);
							 allBean.setListen_middle(mList_all.get(middleNum_all)+"");
							 allBean.setListen_middleList(mList_all);
							
						 }
						 map.put(headcode+"����Ч��", allBean);
						 
						 if(map.get(mapKey) == null){
							 ZrstRepEffectStatisticsBean detailBean = new ZrstRepEffectStatisticsBean();
							 detailBean.setPlay_time(play_time);
							 detailBean.setLanguage_name(language_name);
							 detailBean.setFreq(freq);
							 detailBean.setRunplan_id(runplan_id);
							 detailBean.setTransmit_station(stationname);
							 detailBean.setRunplan_type_id_temp(runplan_type_id);
							 detailBean.setTransmit_direction(direction);
							 detailBean.setTransmit_power(power);
							 detailBean.setService_area(service_area);
							 detailBean.setReceive_name(shortname);
							 detailBean.setReceive_code(headcode);
							 detailBean.setValid_start_time_temp(valid_start_time);
							 detailBean.setValid_end_time_temp(valid_end_time);
							 detailBean.setAll_listens(program_type);//��11������ ����ֶ���ʱ������Ž�Ŀ���� ��Ŀ���1:����ת��,2:����ֱ��,3:����ֱ��,4:���ڵط�
							 
							 double fen0 = countoNum==0?1:0;
							 double fen1 = countoNum==1?1:0;
							 double fen2 = countoNum==2?1:0;
							 double fen3 = countoNum==3?1:0;
							 double fen4 = countoNum==4?1:0;
							 double fen5 = countoNum==5?1:0;
							 detailBean.setReceive_count("1");
							 detailBean.setFen0(fen0+"");
							 detailBean.setFen1(fen1+"");
							 detailBean.setFen2(fen2+"");
							 detailBean.setFen3(fen3+"");
							 detailBean.setFen4(fen4+"");
							 detailBean.setFen5(fen5+"");
							 detailBean.setListen(new BigDecimal((fen3+fen4+fen5)*100/1).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
							 detailBean.setListen_middle(countoNum+"");//��һ��
							 detailBean.getListen_middleList().add(countoNum);
							 if(dis_runplan_id.equals("")){
								 if((fen3+fen4+fen5)*100/1<60){
									 disturb = "�ź�����������";
								 }
							 }
							 detailBean.setBak(disturb);
							 detailBean.setSub_report_type("11");
							 detailBean.setReport_type(my_report_type);
							 detailBean.setReport_id(report_id);
							 map.put(mapKey, detailBean);
							 
							 
						 } else{
							 ZrstRepEffectStatisticsBean detailBean = (ZrstRepEffectStatisticsBean)map.get(mapKey);
							 int receive_count = Integer.parseInt(detailBean.getReceive_count());
							 receive_count++;
							 detailBean.setReceive_count(receive_count+"");
							 
							 double fen0 = countoNum==0?Double.parseDouble(detailBean.getFen0())+1:Double.parseDouble(detailBean.getFen0());
							 double fen1 = countoNum==1?Double.parseDouble(detailBean.getFen1())+1:Double.parseDouble(detailBean.getFen1());
							 double fen2 = countoNum==2?Double.parseDouble(detailBean.getFen2())+1:Double.parseDouble(detailBean.getFen2());
							 double fen3 = countoNum==3?Double.parseDouble(detailBean.getFen3())+1:Double.parseDouble(detailBean.getFen3());
							 double fen4 = countoNum==4?Double.parseDouble(detailBean.getFen4())+1:Double.parseDouble(detailBean.getFen4());
							 double fen5 = countoNum==5?Double.parseDouble(detailBean.getFen5())+1:Double.parseDouble(detailBean.getFen5());
							 detailBean.setFen0(fen0+"");
							 detailBean.setFen1(fen1+"");
							 detailBean.setFen2(fen2+"");
							 detailBean.setFen3(fen3+"");
							 detailBean.setFen4(fen4+"");
							 detailBean.setFen5(fen5+"");
							 detailBean.setListen(new BigDecimal((fen3+fen4+fen5)*100/receive_count).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
							 if(dis_runplan_id.equals("")){
								 if((fen3+fen4+fen5)*100/receive_count<60){
									 disturb = "�ź�����������";
								 }else disturb = "";
							 }
							 detailBean.getListen_middleList().add(countoNum);
							 ArrayList mList = detailBean.getListen_middleList();
							 Collections.sort(mList);
							 int middleNum = (int) Math.ceil(mList.size()/2);
							 detailBean.setBak(disturb);
							 detailBean.setListen_middle(mList.get(middleNum)+"");
							 detailBean.setListen_middleList(mList);
							 map.put(mapKey, detailBean);
						 }
					 }
				 }

		return map;
	}
    
	/**     
	 * @detail  
	 * @method  
	 * @param args 
	 * @return  void  
	 * @author  zhaoyahui
	 * @version 2013-1-6 ����03:10:29
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EffectReportStatisticsOld s=new EffectReportStatisticsOld();
		s.HeadendEffectHours("Ī˹��","aa","101");
//		System.out.println((int)9.6);
	}


public void HeadendEffectHours(String headname,String state,String runplanType){

	String reportId  = "19904";
	String sub_report_type  ="11";
	double hour1 = 0;//�ɱ�֤����Ƶʱ��
	double hour2=0;//����������Ƶʱ��
	double hour3=0;//��ʱ������Ƶʱ��
	double hour4=0;//��������Ƶʱ��
	String sql ="";
	try {
//		if(runplanType.equalsIgnoreCase("101")){
//			sql  = "select distinct t.freq,t.language_name,t.play_time,t.transmit_station,t.receive_count,t.fen0,t.fen1,t.fen2,t.fen3,t.fen4,t.fen5,t.all_listens from zrst_rep_effect_statistics_tab t,zres_runplan_chaifen_tab zrt ";
//	        sql+="where t.sub_report_type='11' and zrt.is_delete=0 and t.runplan_id=zrt.runplan_id and t.transmit_station is not null and  t.play_time is not null  and t.receive_name like '%"+headname+"%' and t.report_id="+reportId;
//	        sql+=" order by t.freq,t.play_time ";
		///}else{
			sql  = "select distinct t.freq,t.language_name,t.play_time,t.transmit_station,t.receive_count,t.fen0,t.fen1,t.fen2,t.fen3,t.fen4,t.fen5,t.all_listens from zrst_rep_effect_statistics_tab t ";
	        sql+="where t.sub_report_type='11'  and t.transmit_station is not null and  t.play_time is not null  and t.receive_name like '%"+headname+"%' and t.report_id="+reportId;
	        sql+=" order by t.freq,t.play_time,t.transmit_station ";
		//}
		  
		        GDSet gd = DbComponent.Query(sql);
				if(gd.getRowCount()>0){
					for(int i=0;i<gd.getRowCount();i++){
						int counts=0;
						int count3=0;
						int count4=0;
						int count5=0;
						int listionRate=0;
						if(i<gd.getRowCount()-1){
							if(gd.getString(i, "play_time").equalsIgnoreCase(gd.getString(i+1, "play_time"))
									 &&gd.getString(i, "freq").equalsIgnoreCase(gd.getString(i+1, "freq"))
							){
								System.out.println(gd.getString(i, "play_time")+gd.getString(i+1, "play_time"));
								continue;
							}
							if(gd.getString(i, "play_time").split("-")[1].equalsIgnoreCase(gd.getString(i+1, "play_time").split("-")[0])&&gd.getString(i, "freq").equalsIgnoreCase(gd.getString(i+1, "freq"))
									&&gd.getString(i, "transmit_station").equalsIgnoreCase(gd.getString(i+1, "transmit_station"))
									&&gd.getString(i, "language_name").equalsIgnoreCase(gd.getString(i+1, "language_name"))){
								 counts=Integer.parseInt(gd.getString(i, "receive_count"))+Integer.parseInt(gd.getString(i+1, "receive_count"));//һ��Сʱ��Ԫ�����ղ����
								 count3 = Integer.parseInt(gd.getString(i, "fen3"))+ Integer.parseInt(gd.getString(i+1, "fen3"));//3�ֵĴ���
								 count4 = Integer.parseInt(gd.getString(i, "fen4"))+ Integer.parseInt(gd.getString(i+1, "fen4"));//3�ֵĴ���
								 count5 = Integer.parseInt(gd.getString(i, "fen5"))+ Integer.parseInt(gd.getString(i+1, "fen5"));//3�ֵĴ���
								 listionRate=Integer.parseInt(new BigDecimal(Double.parseDouble((count3+count4+count5)+"")*100/counts).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
								if(listionRate>=80){//Ч���ǿɱ�֤����
									hour1 += 1.0;
								 } else if(listionRate>=60 && listionRate<80){//Ч���ǻ���������
									 hour2 += 1.0;
								 } else if(listionRate>=30 && listionRate<60){//Ч������ʱ������
									 hour3 += 1;
								 } else if(listionRate<30){//Ч���ǲ�������
									 hour4 += 1;
								 }
								i+=1;
							}else{
								 counts=Integer.parseInt(gd.getString(i, "receive_count"));//һ��Сʱ��Ԫ�����ղ����
								 count3 = Integer.parseInt(gd.getString(i, "fen3"));
								 count4 = Integer.parseInt(gd.getString(i, "fen4"));
								 count5 = Integer.parseInt(gd.getString(i, "fen5"));
								 listionRate=Integer.parseInt(new BigDecimal(Double.parseDouble((count3+count4+count5)+"")*100/counts).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
								if(listionRate>=80){//Ч���ǿɱ�֤����
									hour1 += 0.5;
								 } else if(listionRate>=60 && listionRate<80){//Ч���ǻ���������
									 hour2 += 0.5;
								 } else if(listionRate>=30 && listionRate<60){//Ч������ʱ������
									 hour3 += 0.5;
								 } else if(listionRate<30){//Ч���ǲ�������
									 hour4 += 0.5;
								 }
							}
						}else{
							counts=Integer.parseInt(gd.getString(i, "receive_count"));//һ��Сʱ��Ԫ�����ղ����
							 count3 = Integer.parseInt(gd.getString(i, "fen3"));
							 count4 = Integer.parseInt(gd.getString(i, "fen4"));
							 count5 = Integer.parseInt(gd.getString(i, "fen5"));
							// listionRate=(count3+count4+count5)*100/counts; 
							 listionRate=Integer.parseInt(new BigDecimal(Double.parseDouble((count3+count4+count5)+"")*100/counts).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
							if(listionRate>=80){//Ч���ǿɱ�֤����
								hour1 += 0.5;
							 } else if(listionRate>=60 && listionRate<80){//Ч���ǻ���������
								 hour2 += 0.5;
							 } else if(listionRate>=30 && listionRate<60){//Ч������ʱ������
								 hour3 += 0.5;
							 } else if(listionRate<30){//Ч���ǲ�������
								 hour4 += 0.5;
							 }
						}
						
					}
		}
		
		System.out.println("ykz"+headname);
		System.out.println("state"+ state);
		System.out.println("hour1"+ hour1);
		System.out.println("hour2"+ hour2);
		System.out.println("hour3"+ hour3);
		System.out.println("hour4"+ hour4);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
}
