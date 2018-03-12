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
//		titleWf = new WritableFont(WritableFont.createFont("����"), 14,WritableFont.BOLD, false);
//	    titleWcf = new WritableCellFormat(titleWf);
//	    
//	    bodyWf = new WritableFont(WritableFont.createFont("����"), 10,WritableFont.NO_BOLD, false);
//		bodyWcf = new WritableCellFormat(bodyWf);
//		
//		try {
//			titleWcf.setAlignment(jxl.format.Alignment.CENTRE);
//			titleWcf.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
//			titleWcf.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); 
//			
//			
//			
//            // ��ˮƽ���뷽ʽָ��Ϊ�����
//			bodyWcf.setAlignment(jxl.format.Alignment.CENTRE);
//            // �Ѵ�ֱ���뷽ʽָ��Ϊ���ж���
//			bodyWcf.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
//			bodyWcf.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); 
//		} catch (WriteException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//	}
	
	
	//ͳ������
	private  final String A = "a";
	private  final String B = "b";
	private  final String C = "c";
	/**
	 * 6.1.6.6ң��վΪ������Ч��ͳ�ƹ���
	 * <p>class/function:com.viewscenes.web.statistics.ykz
	 * <p>explain:
	 * <p>author-date:̷��ΰ 2013-2-26
	 * @param:
	 * @return:
	 */
	public Object statEffectByYkz(ASObject obj){
		
		ASObject resultObj = new ASObject();
		resultObj.put(A, new ArrayList<EffectStatCommonBean>());
		resultObj.put(B, new ArrayList<EffectStatCommonBean>());
		resultObj.put(C, new ArrayList<EffectStatCommonBean>());
		String xml ="<chart yAxisName='������ ��λ:(%)' xAxisName='ң��վ' caption='����ң��վ��Ч��ͳ�ƿ�����' baseFontSize='15' showBorder='1' imageSave='1'>";
//		ArrayList<EffectStatCommonBean> resultList = new ArrayList<EffectStatCommonBean>();
		//ң��վ
		String headcode = (String)obj.get("headcode");	
		String startDate = (String)obj.get("startDate");	
		String endDate = (String)obj.get("endDate");	
		//ͳ������	�Աȣ�ͳ��
		String statType = (String)obj.get("statType");
		//a:����ң��վͳ��,b:����ң��վ����ͳ��,c:����ң��վ����̨ͳ��
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
			
			//ң��վ
			HashMap<String,ArrayList<EffectStatCommonBean>> headnameMap = new HashMap<String,ArrayList<EffectStatCommonBean>>();
			ArrayList<String> AList=new ArrayList<String>();
			//����
			HashMap<String,ArrayList<EffectStatCommonBean>> languageMap = new HashMap<String,ArrayList<EffectStatCommonBean>>();
			ArrayList<String> BList=new ArrayList<String>();
			//����̨
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
			
			//�������ͳ������
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
					//�ڶԱ�ҳ�棬����ͳ�����ݽ��м�����
					if (o.toString().indexOf(A)>-1 && statType.equals("�Ա�")){
						continue;
					//��ͳ��ҳ�棬���ԶԱ����ݽ��м�����
					}else if ((o.toString().indexOf(B)> -1 || o.toString().indexOf(C)> -1) && statType.equals("ͳ��"))
						continue;
					//Ƶ������
					int freqcount = 0;
					HashMap<String,String> freqMap = new HashMap<String,String>();
					
					//Ƶʱ��
					float freqtime = 0;
					
					//�ɱ�֤����Ƶʱ��
					float goodfreqtime = 0;
					//ң��վƵ������map
					HashMap ykzfeqmap = new HashMap();
					//ң��վƵʱmap
					HashMap ykztimemap = new HashMap();
					//����Ƶ������map
					HashMap languagefeqmap = new HashMap();
					//����̨Ƶ������map
					HashMap stationfeqmap = new HashMap();
					
					//��¼��ͬ����(5,4,3,2,1)���ܴ���
					int count1 = 0;
					int count2 = 0;
					int count3 = 0;
					int count4 = 0;
					int count5 = 0;
					
					//���ղ����
					int receivecount = list.size();
					//��ͬ�����Ŀ���������̶�
					//(�����ȵ�����̶�������5��������8�����ʾ��80��89%��ʱ������ȴﵽ5�֣���4��������1�����ʾ��10��19%��ʱ������ȴﵽ4�֡�)
					int audibility1 = 0;
					int audibility2 = 0;
					int audibility3 = 0;
					int audibility4 = 0;
					int audibility5 = 0;
	
					//������(�����ȴ��ڵ���3�ִ���������������֮��)
					int audible = 0;
					//�ϲ��������, ���ŷָ�
					String language = "";
					//�ϲ���ķ���̨, ���ŷָ�
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
						
						//1.ң��վ
						if (aKey.equals(o)){
							//�ϲ�����
							if (language.indexOf(result.getLanguage_name()) == -1)
								language += result.getLanguage_name()+",";
							
							//�ϲ�����̨
							if (stationName.indexOf(result.getStation_name()) == -1)
								stationName += result.getStation_name()+",";
						}
						
						//2.ң��վ����
						if (bKey.equals(o)){
							//�ϲ�����̨
							if (stationName.indexOf(result.getStation_name()) == -1)
								stationName += result.getStation_name()+",";
						}
						
						//3.ң��վ����̨
						if (cKey.equals(o)){
							//�ϲ�����
							if (language.indexOf(result.getLanguage_name()) == -1)
								language += result.getLanguage_name()+",";
						}
						
						ykztimemap.put(result.getStart_time()+"-"+result.getEnd_time()+"-"+result.getMark_datetime().split(" ")[0]+"-"+result.getFreq()+"-"+result.getStation_name(),result.getStart_time()+"-"+result.getEnd_time()+"-"+result.getMark_datetime().split(" ")[0]+"-"+result.getFreq()+"-"+result.getStation_name() );
//						String stime = result.getStart_time();
//						String etime = result.getEnd_time();
						//5��4��3��2��1��ֵ�Ĵ���
						int oo = Integer.valueOf(result.getO());
						switch(oo){
							case 1:count1++; break;
							case 2:count2++; break;
							case 3:count3++; break;
							case 4:count4++; break;
							case 5:count5++; break;
							default:;
						}
						
						//����Ƶ������
						if (freqMap.get(result.getFreq())==null){
							ykzfeqmap.put(result.getFreq(), result.getFreq());						
							//freqcount++;
						}else							
							freqMap.put(result.getFreq(), "1");
						
						
						
//						//����Ƶʱ��	
//						String a = "2000-01-01 ";
//						String b = "2000-01-02 ";
//						int tmps = Integer.valueOf(stime.split(":")[0])*60+Integer.valueOf(stime.split(":")[1]);
//						int tmpe = Integer.valueOf(etime.split(":")[0])*60+Integer.valueOf(etime.split(":")[1]);
//						Date _etime = null;
//						Date _stime = null;
//						//��ʼʱ����ڽ���ʱ��,������
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
					//����Ƶʱ��	
					Iterator iter = ykztimemap.entrySet().iterator();
					while (iter.hasNext()) {
					   Map.Entry entry = (Map.Entry) iter.next();
					   String stime = entry.getKey().toString().split("-")[0];
					   String etime = entry.getKey().toString().split("-")[1];
					 //����Ƶʱ��	
						String a = "2000-01-01 ";
						String b = "2000-01-02 ";
						int tmps = Integer.valueOf(stime.split(":")[0])*60+Integer.valueOf(stime.split(":")[1]);
						int tmpe = Integer.valueOf(etime.split(":")[0])*60+Integer.valueOf(etime.split(":")[1]);
						Date _etime = null;
						Date _stime = null;
						//��ʼʱ����ڽ���ʱ��,������
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
						//1.��������������
						audibility1 = (int)(((double)count1/receivecount)*10);
						audibility2 = (int)(((double)count2/receivecount)*10);
						audibility3 = (int)(((double)count3/receivecount)*10);
						audibility4 = (int)(((double)count4/receivecount)*10);
						audibility5 = (int)(((double)count5/receivecount)*10);
						
						//3.���������
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
							
							//Ч��������ʵĹ�ϵ��������>=80%��Ч���ǿɱ�֤����
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
							
							//Ч��������ʵĹ�ϵ��������>=80%��Ч���ǿɱ�֤����
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
							
							//Ч��������ʵĹ�ϵ��������>=80%��Ч���ǿɱ�֤����
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
        	xml="û��ͳ������";
        }
        resultObj.put("xml", xml);
		return resultObj;
	}
	
	/**
	 * 6.1.6.7ң��վ�ղ�����Ϊ������Ч��ͳ�ƹ���
	 * <p>class/function:com.viewscenes.web.statistics.ykz
	 * <p>explain:
	 * <p>author-date:̷��ΰ 2013-2-26
	 * @param:
	 * @return:
	 */
	public Object statEffectByYkzLanguage(ASObject obj){
		
		ASObject resultObj = new ASObject();
		resultObj.put(A, new ArrayList<EffectStatCommonBean>());
		resultObj.put(B, new ArrayList<EffectStatCommonBean>());
		resultObj.put(C, new ArrayList<EffectStatCommonBean>());
		String xml ="<chart yAxisName='������ ��λ:(%)' xAxisName='����' caption='�������Ե�Ч��ͳ�ƿ�����' baseFontSize='15' showBorder='1' imageSave='1'>";
//		ArrayList<EffectStatCommonBean> resultList = new ArrayList<EffectStatCommonBean>();
		//ң��վ
		String headcode = (String)obj.get("headcode");	
		//���� ���ж��
		String language_ids = (String)obj.get("language_ids");	
		
		String startDate = (String)obj.get("startDate");	
		String endDate = (String)obj.get("endDate");	
		//ͳ������	�Աȣ�ͳ��
		String statType = (String)obj.get("statType");
		//a:����ң��վ�ղ�����ͳ��,b:�������Է�����ͳ��,c:��������ң��վͳ��
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
			
			//����
			HashMap<String,ArrayList<EffectStatCommonBean>> languageMap = new HashMap<String,ArrayList<EffectStatCommonBean>>();
			//������
			HashMap<String,ArrayList<EffectStatCommonBean>> serviceareaMap = new HashMap<String,ArrayList<EffectStatCommonBean>>();
			//ң��վ
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
			
			//�������ͳ������
			ArrayList<HashMap<String,ArrayList<EffectStatCommonBean>>> mapList = new ArrayList<HashMap<String,ArrayList<EffectStatCommonBean>>>();
			mapList.add(languageMap);
			mapList.add(serviceareaMap);
			mapList.add(headnameMap);
			
			for(int ia=0;ia<mapList.size();ia++){
			
				for(Object o : mapList.get(ia).keySet()){ 
					ArrayList<EffectStatCommonBean> list = mapList.get(ia).get(o);
					
					//�ڶԱ�ҳ�棬����ͳ�����ݽ��м�����
					if (o.toString().indexOf(A)>-1 && statType.equals("�Ա�")){
						continue;
					//��ͳ��ҳ�棬���ԶԱ����ݽ��м�����
					}else if ((o.toString().indexOf(B)> -1 || o.toString().indexOf(C)> -1) && statType.equals("ͳ��"))
						continue;
					
					//Ƶ������
					int freqcount = 0;
					HashMap<String,String> freqMap = new HashMap<String,String>();
					
					//Ƶʱ��
					float freqtime = 0;
					
					//�ɱ�֤����Ƶʱ��
					float goodfreqtime = 0;
					
					//��¼��ͬ����(5,4,3,2,1)���ܴ���
					int count1 = 0;
					int count2 = 0;
					int count3 = 0;
					int count4 = 0;
					int count5 = 0;
					
					//���ղ����
					int receivecount = list.size();
					//��ͬ�����Ŀ���������̶�
					//(�����ȵ�����̶�������5��������8�����ʾ��80��89%��ʱ������ȴﵽ5�֣���4��������1�����ʾ��10��19%��ʱ������ȴﵽ4�֡�)
					int audibility1 = 0;
					int audibility2 = 0;
					int audibility3 = 0;
					int audibility4 = 0;
					int audibility5 = 0;
	
					//������(�����ȴ��ڵ���3�ִ���������������֮��)
					int audible = 0;
					
					//�ϲ���ķ�����, ���ŷָ�
					String servicearea = "";
					//�ϲ���ķ���̨, ���ŷָ�
					String stationName = "";
					//�ϲ����վ��, ���ŷָ�
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
						
						//1.ң��վ�ղ�����
						if (aKey.equals(o)){
							//�ϲ�������
							if (servicearea.indexOf(result.getService_area()) == -1)
								servicearea += result.getService_area()+",";
							
							//�ϲ�����̨
							if (stationName.indexOf(result.getStation_name()) == -1)
								stationName += result.getStation_name()+",";
							
							//�ϲ�վ��
							if (headname.indexOf(result.getHeadname()) == -1)
								headname += result.getHeadname()+",";
						}
						
						//2.���Է�����ͳ��
						if (bKey.equals(o)){
							//�ϲ�վ��
							if (headname.indexOf(result.getHeadname()) == -1)
								headname += result.getHeadname()+",";
						}
						
						//3.����ң��վͳ��
						if (cKey.equals(o)){
							//�ϲ�������
							if (servicearea.indexOf(result.getService_area()) == -1)
								servicearea += result.getService_area()+",";
						}
						
						
						
						
						
						
						
						String stime = result.getStart_time();
						String etime = result.getEnd_time();
						//5��4��3��2��1��ֵ�Ĵ���
						int oo = Integer.valueOf(result.getO());
						switch(oo){
							case 1:count1++; break;
							case 2:count2++; break;
							case 3:count3++; break;
							case 4:count4++; break;
							case 5:count5++; break;
							default:;
						}
						
						//����Ƶ������
						if (freqMap.get(result.getFreq())==null)
							freqcount++;
						else
							freqMap.put(result.getFreq(), "1");
						
						
						
						//����Ƶʱ��	
						String a = "2000-01-01 ";
						String b = "2000-01-02 ";
						int tmps = Integer.valueOf(stime.split(":")[0])*60+Integer.valueOf(stime.split(":")[1]);
						int tmpe = Integer.valueOf(etime.split(":")[0])*60+Integer.valueOf(etime.split(":")[1]);
						Date _etime = null;
						Date _stime = null;
						//��ʼʱ����ڽ���ʱ��,������
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
						//1.��������������
						audibility1 = (int)(((double)count1/receivecount)*10);
						audibility2 = (int)(((double)count2/receivecount)*10);
						audibility3 = (int)(((double)count3/receivecount)*10);
						audibility4 = (int)(((double)count4/receivecount)*10);
						audibility5 = (int)(((double)count5/receivecount)*10);
						
						//3.���������
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
							
							//Ч��������ʵĹ�ϵ��������>=80%��Ч���ǿɱ�֤����
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
							
							//Ч��������ʵĹ�ϵ��������>=80%��Ч���ǿɱ�֤����
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
							
							//Ч��������ʵĹ�ϵ��������>=80%��Ч���ǿɱ�֤����
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
	      xml="û��ͳ������";
        }
	    resultObj.put("xml", xml);
		return resultObj;
	}
	
	
	/**
	 * 6.1.6.8ң��վ�ղⷢ��̨Ϊ������Ч��ͳ�ƹ���
	 * <p>class/function:com.viewscenes.web.statistics.ykz
	 * <p>explain:
	 * <p>author-date:̷��ΰ 2013-2-26
	 * @param:
	 * @return:statEffectByYkzLanguage
	 */
	public Object statEffectByYkzStation(ASObject obj){
		ASObject resultObj = new ASObject();
		resultObj.put(A, new ArrayList<EffectStatCommonBean>());
		resultObj.put(B, new ArrayList<EffectStatCommonBean>());
		resultObj.put(C, new ArrayList<EffectStatCommonBean>());
		String xml ="<chart yAxisName='������ ��λ:(%)' xAxisName='����̨' caption='���ڷ���̨��Ч��ͳ�ƿ�����' baseFontSize='15' showBorder='1' imageSave='1'>";
		//ң��վ
		String headcode = (String)obj.get("headcode");	
		//���� ���ж��
		String station_ids = (String)obj.get("station_ids");	
		String startDate = (String)obj.get("startDate");	
		String endDate = (String)obj.get("endDate");	
		//ͳ������	�Աȣ�ͳ��
		String statType = (String)obj.get("statType");
		//a:����ң��վ����̨ͳ��,b:���ݷ���̨������ͳ��,c:���ݷ���̨ң��վͳ��
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
			//����̨
			HashMap<String,ArrayList<EffectStatCommonBean>> stationMap = new HashMap<String,ArrayList<EffectStatCommonBean>>();
			//������
			HashMap<String,ArrayList<EffectStatCommonBean>> serviceareaMap = new HashMap<String,ArrayList<EffectStatCommonBean>>();
			//ң��վ
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
			//�������ͳ������
			ArrayList<HashMap<String,ArrayList<EffectStatCommonBean>>> mapList = new ArrayList<HashMap<String,ArrayList<EffectStatCommonBean>>>();
			mapList.add(stationMap);
			mapList.add(serviceareaMap);
			mapList.add(headnameMap);
			
			
			for(int ia=0;ia<mapList.size();ia++){
				
				//�ֱ�ͳ�Ʋ�ͬ���ͽ��
				for(Object o : mapList.get(ia).keySet()){ 
					ArrayList<EffectStatCommonBean> list = mapList.get(ia).get(o);
					
					//�ڶԱ�ҳ�棬����ͳ�����ݽ��м�����
					if (o.toString().indexOf(A)>-1 && statType.equals("�Ա�")){
						continue;
					//��ͳ��ҳ�棬���ԶԱ����ݽ��м�����
					}else if ((o.toString().indexOf(B)> -1 || o.toString().indexOf(C)> -1) && statType.equals("ͳ��"))
						continue;
					
					
					//Ƶ������
					int freqcount = 0;
					HashMap<String,String> freqMap = new HashMap<String,String>();
					
					//Ƶʱ��
					float freqtime = 0;
					
					//�ɱ�֤����Ƶʱ��
					float goodfreqtime = 0;
					
					//��¼��ͬ����(5,4,3,2,1)���ܴ���
					int count1 = 0;
					int count2 = 0;
					int count3 = 0;
					int count4 = 0;
					int count5 = 0;
					
					//���ղ����
					int receivecount = list.size();
					//��ͬ�����Ŀ���������̶�
					//(�����ȵ�����̶�������5��������8�����ʾ��80��89%��ʱ������ȴﵽ5�֣���4��������1�����ʾ��10��19%��ʱ������ȴﵽ4�֡�)
					int audibility1 = 0;
					int audibility2 = 0;
					int audibility3 = 0;
					int audibility4 = 0;
					int audibility5 = 0;
	
					//������(�����ȴ��ڵ���3�ִ���������������֮��)
					int audible = 0;
					
					//�ϲ���ķ�����, ���ŷָ�
					String servicearea = "";
					//�ϲ����ң��վ, ���ŷָ�
					String headname = "";
					//�ϲ���ķ���̨, ���ŷָ�
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
						
						
						//1.ң��վ����̨ͳ��
						if (aKey.equals(o)){
							//�ϲ�������
							if (servicearea.indexOf(result.getService_area()) == -1)
								servicearea += result.getService_area()+",";
							
							//�ϲ�վ��
							if (headname.indexOf(result.getHeadname()) == -1)
								headname += result.getHeadname()+",";
						}
						
						//2.���ݷ���̨������ͳ��
						if (bKey.equals(o)){
							//�ϲ�վ��
							if (headname.indexOf(result.getHeadname()) == -1)
								headname += result.getHeadname()+",";
						}
						
						//3.���ݷ���̨ң��վͳ��
						if (cKey.equals(o)){
							//�ϲ�������
							if (servicearea.indexOf(result.getService_area()) == -1)
								servicearea += result.getService_area()+",";
						}
						
//						//�ϲ�����̨
//						if (station.indexOf(result.getStation_name()) == -1)
//							station += result.getStation_name()+",";
						
						
						
						String stime = result.getStart_time();
						String etime = result.getEnd_time();
						//5��4��3��2��1��ֵ�Ĵ���
						int oo = Integer.valueOf(result.getO());
						switch(oo){
							case 1:count1++; break;
							case 2:count2++; break;
							case 3:count3++; break;
							case 4:count4++; break;
							case 5:count5++; break;
							default:;
						}
						
						//����Ƶ������
						if (freqMap.get(result.getFreq())==null)
							freqcount++;
						else
							freqMap.put(result.getFreq(), "1");
						
						
						
						//����Ƶʱ��	
						String a = "2000-01-01 ";
						String b = "2000-01-02 ";
						int tmps = Integer.valueOf(stime.split(":")[0])*60+Integer.valueOf(stime.split(":")[1]);
						int tmpe = Integer.valueOf(etime.split(":")[0])*60+Integer.valueOf(etime.split(":")[1]);
						Date _etime = null;
						Date _stime = null;
						//��ʼʱ����ڽ���ʱ��,������
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
						//1.��������������
						audibility1 = (int)(((double)count1/receivecount)*10);
						audibility2 = (int)(((double)count2/receivecount)*10);
						audibility3 = (int)(((double)count3/receivecount)*10);
						audibility4 = (int)(((double)count4/receivecount)*10);
						audibility5 = (int)(((double)count5/receivecount)*10);
						
						//3.���������
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
							
							//Ч��������ʵĹ�ϵ��������>=80%��Ч���ǿɱ�֤����
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
							
							//Ч��������ʵĹ�ϵ��������>=80%��Ч���ǿɱ�֤����
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
							
							//Ч��������ʵĹ�ϵ��������>=80%��Ч���ǿɱ�֤����
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
	      xml="û��ͳ������";
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
		
		
		
		
		ExcelTitle id = new ExcelTitle("���",10);
		ExcelTitle stationtitle = new ExcelTitle("ң��վ",10);
		ExcelTitle freqcounttitle = new ExcelTitle("Ƶ����",10);
		ExcelTitle freqtimetitle = new ExcelTitle("Ƶʱ��",10);
		ExcelTitle languagetitle = new ExcelTitle("����",10);
		ExcelTitle stationTitile = new ExcelTitle("����̨",10);
		ExcelTitle receivecounttitle = new ExcelTitle("�ղ����",10);
		ExcelTitle goodfreqcounttitle = new ExcelTitle("�ɱ�֤����Ƶʱ��",20);
		ExcelTitle ketinglvtitle = new ExcelTitle("������%",10);
		ExcelTitle manyidutitle = new ExcelTitle("����������̶�",30);
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
			
			//��ͷ
			ArrayList<ExcelTitle[]> titles = new ArrayList<ExcelTitle[]>(3);
			titles.add(ets_ykz);
			titles.add(ets_ykz_lang);
			titles.add(ets_ykz_station);
			
			//������������ͷ
			String[] sheetNames = new String[]{"����ң��վͳ��","����ң��վ����ͳ��","����ң��վ����̨ͳ��"};
			ArrayList[] resultList = new ArrayList[]{aList,bList,cList};;
			
			
			JExcel jExcel = new JExcel();
			jExcel.openDocument();
			//�ļ�����
			jExcel.setFilename(sheetNames[0]);
			for(int sheet = 0;sheet<resultList.length;sheet++){
				
				//�ı乤���� 
				jExcel.WorkBookGetSheet(sheet);
				//����������
				jExcel.getWorkSheet().setName(sheetNames[sheet]);
				
				//������
				jExcel.setTitle(0, 2, titles.get(sheet));
				
				//��ͷ
				jExcel.mergeCells(0, 0, sheet==0?13:12, 1);
				//����
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
		
		
		
		
		ExcelTitle id = new ExcelTitle("���",10);
		ExcelTitle languagetitle = new ExcelTitle("����",10);
		ExcelTitle freqcounttitle = new ExcelTitle("Ƶ����",10);
		ExcelTitle freqtimetitle = new ExcelTitle("Ƶʱ��",10);
		ExcelTitle servicetitle = new ExcelTitle("������",10);
		ExcelTitle stationtitile = new ExcelTitle("����̨",10);
		ExcelTitle headendtitile = new ExcelTitle("ң��վ",10);
		ExcelTitle receivecounttitle = new ExcelTitle("�ղ����",10);
		ExcelTitle goodfreqcounttitle = new ExcelTitle("�ɱ�֤����Ƶʱ��",20);
		ExcelTitle ketinglvtitle = new ExcelTitle("������%",10);
		ExcelTitle manyidutitle = new ExcelTitle("����������̶�",30);
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
			
			//��ͷ
			ArrayList<ExcelTitle[]> titles = new ArrayList<ExcelTitle[]>(3);
			titles.add(ets_lang);
			titles.add(ets_lang_service);
			titles.add(ets_lang_headend);
			
			//������������ͷ
			String[] sheetNames = new String[]{"��������ͳ��","�������Է�����ͳ��","��������ң��վͳ��"};
			ArrayList[] resultList = new ArrayList[]{aList,bList,cList};;
			
			
			JExcel jExcel = new JExcel();
			jExcel.openDocument();
			//�ļ�����
			jExcel.setFilename(sheetNames[0]);
			for(int sheet = 0;sheet<resultList.length;sheet++){
				
				//�ı乤���� 
				jExcel.WorkBookGetSheet(sheet);
				//����������
				jExcel.getWorkSheet().setName(sheetNames[sheet]);
				
				//������
				jExcel.setTitle(0, 2, titles.get(sheet));
				
				//��ͷ
				jExcel.mergeCells(0, 0, sheet==0?14:12, 1);
				//����
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
		
		
		
		
		ExcelTitle id = new ExcelTitle("���",10);
		ExcelTitle languagetitle = new ExcelTitle("����",10);
		ExcelTitle freqcounttitle = new ExcelTitle("Ƶ����",10);
		ExcelTitle freqtimetitle = new ExcelTitle("Ƶʱ��",10);
		ExcelTitle servicetitle = new ExcelTitle("������",10);
		ExcelTitle stationtitile = new ExcelTitle("����̨",10);
		ExcelTitle headendtitile = new ExcelTitle("ң��վ",10);
		ExcelTitle receivecounttitle = new ExcelTitle("�ղ����",10);
		ExcelTitle goodfreqcounttitle = new ExcelTitle("�ɱ�֤����Ƶʱ��",20);
		ExcelTitle ketinglvtitle = new ExcelTitle("������%",10);
		ExcelTitle manyidutitle = new ExcelTitle("����������̶�",30);
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
			
			//��ͷ
			ArrayList<ExcelTitle[]> titles = new ArrayList<ExcelTitle[]>(3);
			titles.add(ets_station);
			titles.add(ets_station_service);
			titles.add(ets_station_headend);
			
			//������������ͷ
			String[] sheetNames = new String[]{"���ݷ���̨ͳ��","���ݷ���̨������ͳ��","���ݷ���̨ң��վͳ��"};
			ArrayList[] resultList = new ArrayList[]{aList,bList,cList};;
			
			
			JExcel jExcel = new JExcel();
			jExcel.openDocument();
			//�ļ�����
			jExcel.setFilename(sheetNames[0]);
			for(int sheet = 0;sheet<resultList.length;sheet++){
				
				//�ı乤���� 
				jExcel.WorkBookGetSheet(sheet);
				//����������
				jExcel.getWorkSheet().setName(sheetNames[sheet]);
				
				//������
				jExcel.setTitle(0, 2, titles.get(sheet));
				
				//��ͷ
				jExcel.mergeCells(0, 0, sheet==0?14:12, 1);
				//����
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
