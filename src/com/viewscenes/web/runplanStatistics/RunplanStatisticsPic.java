package com.viewscenes.web.runplanStatistics;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.jmask.web.controller.EXEException;

import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.pub.GDSet;
import com.viewscenes.util.LogTool;
import com.viewscenes.util.StringTool;

import flex.messaging.io.amf.ASObject;

public class RunplanStatisticsPic {

	
	public Object statisticsInfo(ASObject obj){
		int db_freqs=0;//�̲�Ƶ����
		int zb_freqs=0;//�в�Ƶ����
		int tp_freqs=0;//��Ƶ Ƶ����
		String db_ids="";//���ж̲�����ͼid
		String zb_ids="";//�����в�����ͼid
		String tp_ids="";//���е�Ƶ����ͼid
		ArrayList list = new ArrayList();
		String sql = "select t.runplan_id,t.freq from zres_runplan_tab t where t.valid_end_time>=sysdate and t.is_delete=0";
		try {
			GDSet gd = DbComponent.Query(sql);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					if(Integer.parseInt(gd.getString(i, "freq"))>=2300&&Integer.parseInt(gd.getString(i, "freq"))<=26100){
						db_freqs++;
						db_ids+=gd.getString(i, "runplan_id")+",";
					}
					if(Integer.parseInt(gd.getString(i, "freq"))>=531&&Integer.parseInt(gd.getString(i, "freq"))<=1602){
						zb_freqs++;
						zb_ids+=gd.getString(i, "runplan_id")+",";
					}
					if(Integer.parseInt(gd.getString(i, "freq"))>=50000&&Integer.parseInt(gd.getString(i, "freq"))<=200000){
						tp_freqs++;
						tp_ids+=gd.getString(i, "runplan_id")+",";
					}
				}
			}
			ASObject asobj = new ASObject();
			asobj.put("label", "�̲�");
			asobj.put("value", db_freqs);
		//	asobj.put("ids", db_ids);
			list.add(asobj);
			ASObject asobj1 = new ASObject();
			asobj1.put("label", "�в�");
			asobj1.put("value", zb_freqs);
		//	asobj1.put("color", "white");
			//asobj1.put("ids", zb_ids);
			list.add(asobj1);
			ASObject asobj2 = new ASObject();
			asobj2.put("label", "��Ƶ");
			asobj2.put("value", tp_freqs);
			//asobj2.put("color", "yellow");
		//	asobj2.put("ids", tp_ids);
			list.add(asobj2);
		
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * ͳ�������ղ�Ƶ����
	 * @param obj
	 * @return
	 */
	public Object statisticsInfozl(ASObject obj){
		int db_freqs=0;//�̲�Ƶ����
		int zb_freqs=0;//�в�Ƶ����
		int tp_freqs=0;//��Ƶ Ƶ����
		String db_ids="";//���ж̲�����ͼid
		String zb_ids="";//�����в�����ͼid
		String tp_ids="";//���е�Ƶ����ͼid
		ArrayList list = new ArrayList();
		String sql = "select t.runplan_id,t.freq from zres_runplan_tab t where t.valid_end_time>=sysdate and t.is_delete=0 and t.mon_area is not null";
		try {
			GDSet gd = DbComponent.Query(sql);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					if(Integer.parseInt(gd.getString(i, "freq"))>=2300&&Integer.parseInt(gd.getString(i, "freq"))<=26100){
						db_freqs++;
						db_ids+=gd.getString(i, "runplan_id")+",";
					}
					if(Integer.parseInt(gd.getString(i, "freq"))>=531&&Integer.parseInt(gd.getString(i, "freq"))<=1602){
						zb_freqs++;
						zb_ids+=gd.getString(i, "runplan_id")+",";
					}
					if(Integer.parseInt(gd.getString(i, "freq"))>=50000&&Integer.parseInt(gd.getString(i, "freq"))<=200000){
						tp_freqs++;
						tp_ids+=gd.getString(i, "runplan_id")+",";
					}
				}
			}
			ASObject asobj = new ASObject();
			asobj.put("label", "�̲�");
			asobj.put("value", db_freqs);
		//	asobj.put("ids", db_ids);
			list.add(asobj);
			ASObject asobj1 = new ASObject();
			asobj1.put("label", "�в�");
			asobj1.put("value", zb_freqs);
		//	asobj1.put("color", "white");
			//asobj1.put("ids", zb_ids);
			list.add(asobj1);
			ASObject asobj2 = new ASObject();
			asobj2.put("label", "��Ƶ");
			asobj2.put("value", tp_freqs);
			//asobj2.put("color", "yellow");
		//	asobj2.put("ids", tp_ids);
			list.add(asobj2);
		
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * ͳ��Ч���ղ�Ƶ����
	 * @param obj
	 * @return
	 */
	public Object statisticsInfoxg(ASObject obj){
		int db_freqs=0;//�̲�Ƶ����
		int zb_freqs=0;//�в�Ƶ����
		int tp_freqs=0;//��Ƶ Ƶ����
		String db_ids="";//���ж̲�����ͼid
		String zb_ids="";//�����в�����ͼid
		String tp_ids="";//���е�Ƶ����ͼid
		ArrayList list = new ArrayList();
		String sql = "select t.runplan_id,t.freq from zres_runplan_tab t where t.valid_end_time>=sysdate and t.is_delete=0 and t.xg_mon_area is not null";
		try {
			GDSet gd = DbComponent.Query(sql);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					if(Integer.parseInt(gd.getString(i, "freq"))>=2300&&Integer.parseInt(gd.getString(i, "freq"))<=26100){
						db_freqs++;
						db_ids+=gd.getString(i, "runplan_id")+",";
					}
					if(Integer.parseInt(gd.getString(i, "freq"))>=531&&Integer.parseInt(gd.getString(i, "freq"))<=1602){
						zb_freqs++;
						zb_ids+=gd.getString(i, "runplan_id")+",";
					}
					if(Integer.parseInt(gd.getString(i, "freq"))>=50000&&Integer.parseInt(gd.getString(i, "freq"))<=200000){
						tp_freqs++;
						tp_ids+=gd.getString(i, "runplan_id")+",";
					}
				}
			}
			ASObject asobj = new ASObject();
			asobj.put("label", "�̲�");
			asobj.put("value", db_freqs);
		//	asobj.put("ids", db_ids);
			list.add(asobj);
			ASObject asobj1 = new ASObject();
			asobj1.put("label", "�в�");
			asobj1.put("value", zb_freqs);
		//	asobj1.put("color", "white");
			//asobj1.put("ids", zb_ids);
			list.add(asobj1);
			ASObject asobj2 = new ASObject();
			asobj2.put("label", "��Ƶ");
			asobj2.put("value", tp_freqs);
			//asobj2.put("color", "yellow");
		//	asobj2.put("ids", tp_ids);
			list.add(asobj2);
		
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * ������ͼ��ͳ�Ƶ�ǰÿ�����Ե�Ƶ����
	 * @param obj
	 * @return
	 */
	public Object statisticsInfoLanguage(ASObject obj){
		ArrayList list = new ArrayList();
		ASObject resobj = new ASObject();
		String xml1 ="<chart yAxisName='Ƶ����' xAxisName='����' caption='��������Ƶ����ͳ��' baseFontSize='15' showBorder='1' imageSave='1'>";
		String xml2 ="<chart yAxisName='Ƶ����' xAxisName='����' caption='�����ղ�����Ƶ����ͳ��' baseFontSize='15' showBorder='1' imageSave='1'>";
		String xml3 ="<chart yAxisName='Ƶ����' xAxisName='����' caption='Ч���ղ�����Ƶ����ͳ��' baseFontSize='15' showBorder='1' imageSave='1'>";
		String runplan_type = (String) obj.get("runplan_type");
		String codes = (String) obj.get("codes");
		String unit = (String) obj.get("unit");
		String subsql = "";
		String zl_subsql = " and t.mon_area is not null ";
		String xg_subsql = " and t.xg_mon_area is not null ";
		String sqlTran = "select language_name from zdic_language_tab order by language_name ";
		if(unit!=null&&!unit.equalsIgnoreCase("ȫ��")){
			String[] s = unit.split("-");
			subsql+=" and to_date(start_time || '00','hh24:mi:ss') >= to_date('"+s[0]+":00','hh24:mi:ss') " +
				 " and to_date(t.start_time || '00','hh24:mi:ss')<to_date('"+s[1]+":00','hh24:mi:ss')"; 
		}else{
//			sql+=" and to_char(sysdate,'hh24:mi:ss') between t.start_time || ':00' and t.end_time || ':00' ";
		}
		
		if(codes!=null&&!codes.equalsIgnoreCase("")){
			if(codes.indexOf(",")>=0){
				 String[] s = codes.split(",");
				 String ss="";
				 String sss="";
				 for(int i=0;i<s.length;i++){
					 ss+=" t.mon_area like '%"+s[i]+"%' or ";
					 sss+=" t.xg_mon_area like '%"+s[i]+"%' or ";
				 }
				 subsql+=" and (("+ss.substring(0, ss.length()-3)+") or ("+sss.substring(0, sss.length()-3)+"))";
			 }else{
				 subsql+=" and (t.mon_area like '%"+codes+"%' or t.xg_mon_area like '%"+codes+"%' )";
			 }
		}

		String sqlend = "   and t.runplan_type_id = '"+runplan_type+"' " +
		" group by t.language_id,zlt.language_name,t.freq) tab group by tab.language_id,tab.language_name";
		String sql = "select tab.language_id,tab.language_name,count(*) as count from ( " +
		" select t.language_id,zlt.language_name,t.freq  " +
		"   from zres_runplan_tab t, zdic_language_tab zlt " +
		"  where t.is_delete = 0 " +
		"    and t.language_id is not null " +
		"    and t.language_id = zlt.language_id" +
		"    and t.valid_end_time >= sysdate ";
		String sql1 = sql + subsql + sqlend+" order by tab.language_name ";
		try {
			GDSet gdTran = DbComponent.Query(sqlTran);
			ArrayList<String> sortlist = new ArrayList<String>();
			HashMap<String,String> map = new HashMap<String,String>();
			for(int m=0;m<gdTran.getRowCount();m++){
				map.put(gdTran.getString(m, "language_name"), "0");
				sortlist.add(gdTran.getString(m, "language_name"));
			}
			
			GDSet gd = DbComponent.Query(sql1);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
				//	map.put(gd.getString(i, "language_name"), gd.getString(i, "count"));
					if(gd.getString(i, "language_name").indexOf("��Ѷ�㲥")>0){
						continue;
					}
					xml1+="<set label='"+gd.getString(i, "language_name")+"' value='"+gd.getString(i, "count")+"'/>\r";
				}
				//Collections.sort(sortlist);
			} else {
			}
//			for(int k=0;k<sortlist.size();k++){
//				xml1+="<set label='"+sortlist.get(k)+"' value='"+map.get(sortlist.get(k))+"'/>\r";
//			}
			xml1+="</chart>";

			sortlist = new ArrayList<String>();
			map = new HashMap<String,String>();
			for(int m=0;m<gdTran.getRowCount();m++){
				map.put(gdTran.getString(m, "language_name"), "0");
				sortlist.add(gdTran.getString(m, "language_name"));
			}
			sql1 = sql + subsql + zl_subsql + sqlend+" order by tab.language_name ";
			gd = DbComponent.Query(sql1);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					//map.put(gd.getString(i, "language_name"), gd.getString(i, "count"));
					if(gd.getString(i, "language_name").equalsIgnoreCase("������Ѷ�㲥(��)")){
						continue;
					}
					xml2+="<set label='"+gd.getString(i, "language_name")+"' value='"+gd.getString(i, "count")+"'/>\r";
				}
			} else {
			}
//			Collections.sort(sortlist);
//			for(int k=0;k<sortlist.size();k++){
//				xml2+="<set label='"+sortlist.get(k)+"' value='"+map.get(sortlist.get(k))+"'/>\r";
//			}
			xml2+="</chart>";

			sortlist = new ArrayList<String>();
			map = new HashMap<String,String>();
			for(int m=0;m<gdTran.getRowCount();m++){
				map.put(gdTran.getString(m, "language_name"), "0");
				sortlist.add(gdTran.getString(m, "language_name"));
			}
			sql1 = sql + subsql + xg_subsql + sqlend+" order by tab.language_name ";
			gd = DbComponent.Query(sql1);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					//map.put(gd.getString(i, "language_name"), gd.getString(i, "count"));
					if(gd.getString(i, "language_name").equalsIgnoreCase("������Ѷ�㲥(��)")){
						continue;
					}
					xml3+="<set label='"+gd.getString(i, "language_name")+"' value='"+gd.getString(i, "count")+"'/>\r";
				}
			} else {
			}
//			Collections.sort(sortlist);
//			for(int k=0;k<sortlist.size();k++){
//				xml3+="<set label='"+sortlist.get(k)+"' value='"+map.get(sortlist.get(k))+"'/>\r";
//			}
			xml3+="</chart>";
		} catch (Exception e) {
			LogTool.fatal(e);
			return new EXEException("",e.getMessage(),"");
		}
		
		resobj.put("xml1", xml1);
		resobj.put("xml2", xml2);
		resobj.put("xml3", xml3);
		return resobj;
	}
	public Object statisticsInfoLanguage1(ASObject obj){
		ArrayList list = new ArrayList();
		String xml ="<chart yAxisName='Ƶ����' xAxisName='����' caption='����Ƶ����ͳ��' baseFontSize='15' showBorder='1' imageSave='1'>";
		String runplan_type = (String) obj.get("runplan_type");
		String codes = (String) obj.get("codes");
		String unit = (String) obj.get("unit");
		String sql="select t.language_id,zlt.language_name, count(*) as count from zres_runplan_tab t, zdic_language_tab zlt " +
				"where t.is_delete=0 and t.language_id is not null and t.language_id = zlt.language_id and t.valid_end_time>=sysdate and t.runplan_type_id='"+runplan_type+"' ";
        if(unit!=null&&!unit.equalsIgnoreCase("ȫ��")){
        	String[] s = unit.split("-");
			sql+=" and to_date(start_time || '00','hh24:mi:ss') >= to_date('"+s[0]+":00','hh24:mi:ss') " +
				 " and to_date(t.start_time || '00','hh24:mi:ss')<to_date('"+s[1]+":00','hh24:mi:ss')"; 
		}else{
			//sql+=" and to_char(sysdate,'hh24:mi:ss') between t.start_time || ':00' and t.end_time || ':00' ";
		}
		if(codes!=null&&!codes.equalsIgnoreCase("")){
			if(codes.indexOf(",")>=0){
				 String[] s = codes.split(",");
				 String ss="";
				 String sss="";
				 for(int i=0;i<s.length;i++){
					 ss+=" t.mon_area like '%"+s[i]+"%' or ";
					 sss+=" t.xg_mon_area like '%"+s[i]+"%' or ";
				 }
				 sql+=" and (("+ss.substring(0, ss.length()-3)+") or ("+sss.substring(0, sss.length()-3)+"))";
			 }else{
				 sql+=" and (t.mon_area like '%"+codes+"%' or t.xg_mon_area like '%"+codes+"%' )";
			 }
		}
		sql+=" group by t.language_id,zlt.language_name";
		try {
			GDSet gd = DbComponent.Query(sql);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					xml+="<set label='"+gd.getString(i, "language_name")+"' value='"+gd.getString(i, "count")+"'/>\r";
//					ASObject asobj = new ASObject();
//					asobj.put("label", gd.getString(i, "language_name"));
//					asobj.put("value", gd.getString(i, "count"));
//					asobj.put("value1", "20");
//				//	asobj.put("link", "javascript:alert('id\r \n \r')");
//					list.add(asobj);
				}
			} else {
//				resobj.put("xml1", "��ͳ�����ݣ�");
			}
			xml+="</chart>";
		} catch (Exception e) {
			LogTool.fatal(e);
			return new EXEException("",e.getMessage(),"");
		}
		
		return xml;
	}
	
	/**
	 *  ������ͼ����ȡ����ǰ������Ч������,Ȼ��������Լ����Ӧ��Ƶʱ��
	 * @param obj
	 * @return
	 */
	public Object statisticsInfoLanguageHours(ASObject obj){
		ASObject resobj = new ASObject();
		String xml1 ="<chart yAxisName='Ƶʱ��  ��λ��(Сʱ)' xAxisName='����' caption='��������Ƶʱ��ͳ��' baseFontSize='15' showBorder='1' imageSave='1'>";
		String xml2 ="<chart yAxisName='Ƶʱ��  ��λ��(Сʱ)' xAxisName='����' caption='��������Ƶʱ��ͳ��' baseFontSize='15' showBorder='1' imageSave='1'>";
		String xml3 ="<chart yAxisName='Ƶʱ��  ��λ��(Сʱ)' xAxisName='����' caption='Ч������Ƶʱ��ͳ��' baseFontSize='15' showBorder='1' imageSave='1'>";
		String runplan_type = (String) obj.get("runplan_type");
		String codes = (String) obj.get("codes");
		String unit = (String) obj.get("unit");

		String subsql = "";
		String zl_subsql = " and t.mon_area is not null ";
		String xg_subsql = " and t.xg_mon_area is not null ";
		String sqlTran = "select language_name from zdic_language_tab ";
		String sql="select distinct t.language_id,zlt.language_name from zres_runplan_tab t, zdic_language_tab zlt " +
				"where t.is_delete=0 and t.language_id is not null and t.language_id = zlt.language_id and t.valid_end_time>=sysdate and t.runplan_type_id='"+runplan_type+"' ";
        if(unit!=null&&!unit.equalsIgnoreCase("ȫ��")){
        	String[] s = unit.split("-");
			sql+=" and to_date(start_time || '00','hh24:mi:ss') >= to_date('"+s[0]+":00','hh24:mi:ss') " +
				 " and to_date(t.start_time || '00','hh24:mi:ss')<to_date('"+s[1]+":00','hh24:mi:ss')"; 
		}else{
			//sql+=" and to_char(sysdate,'hh24:mi:ss') between t.start_time || ':00' and t.end_time || ':00' ";
		}
		if(codes!=null&&!codes.equalsIgnoreCase("")){
			if(codes.indexOf(",")>=0){
				 String[] s = codes.split(",");
				 String ss="";
				 String sss="";
				 for(int i=0;i<s.length;i++){
					 ss+=" t.mon_area like '%"+s[i]+"%' or ";
					 sss+=" t.xg_mon_area like '%"+s[i]+"%' or ";
				 }
				 subsql+=" and (("+ss.substring(0, ss.length()-3)+") or ("+sss.substring(0, sss.length()-3)+"))";
			 }else{
				 subsql+=" and (t.mon_area like '%"+codes+"%' or t.xg_mon_area like '%"+codes+"%' )";
			 }
		}
		String sql1 = sql + subsql ;
		try {
			GDSet gdTran = DbComponent.Query(sqlTran);
			ArrayList<String> sortlist = new ArrayList<String>();
			HashMap<String,Double> map = new HashMap<String,Double>();
			for(int m=0;m<gdTran.getRowCount();m++){
				map.put(gdTran.getString(m, "language_name"), 0.0);
				sortlist.add(gdTran.getString(m, "language_name"));
			}
			
			GDSet gd = DbComponent.Query(sql1);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					double value = statisticsHoursByLanguage(gd.getString(i, "language_id"),obj);
					map.put(gd.getString(i, "language_name"), value);
				}
			} else {
			}
			Collections.sort(sortlist);
			for(int k=0;k<sortlist.size();k++){
				xml1+="<set label='"+sortlist.get(k)+"' value='"+map.get(sortlist.get(k))+"'/>\r";
			}
			xml1 = xml1.replaceAll("value='0.0'", "value='0'");
			xml1+="</chart>";

			sortlist = new ArrayList<String>();
			map = new HashMap<String,Double>();
			for(int m=0;m<gdTran.getRowCount();m++){
				map.put(gdTran.getString(m, "language_name"), 0.0);
				sortlist.add(gdTran.getString(m, "language_name"));
			}
			sql1 = sql + subsql + zl_subsql;
			obj.put("record_type", "1");
			gd = DbComponent.Query(sql1);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					double value = statisticsHoursByLanguage(gd.getString(i, "language_id"),obj);
					map.put(gd.getString(i, "language_name"), value);
				}
			} else {
			}
			Collections.sort(sortlist);
			for(int k=0;k<sortlist.size();k++){
				xml2+="<set label='"+sortlist.get(k)+"' value='"+map.get(sortlist.get(k))+"'/>\r";
			}
			xml2 = xml2.replaceAll("value='0.0'", "value='0'");
			xml2+="</chart>";

			sortlist = new ArrayList<String>();
			map = new HashMap<String,Double>();
			for(int m=0;m<gdTran.getRowCount();m++){
				map.put(gdTran.getString(m, "language_name"), 0.0);
				sortlist.add(gdTran.getString(m, "language_name"));
			}
			sql1 = sql + subsql + xg_subsql;
			obj.put("record_type", "2");
			gd = DbComponent.Query(sql1);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					double value = statisticsHoursByLanguage(gd.getString(i, "language_id"),obj);
					map.put(gd.getString(i, "language_name"), value);
				}
			} else {
			}
			Collections.sort(sortlist);
			for(int k=0;k<sortlist.size();k++){
				xml3+="<set label='"+sortlist.get(k)+"' value='"+map.get(sortlist.get(k))+"'/>\r";
			}
			xml3 = xml3.replaceAll("value='0.0'", "value='0'");
			xml3+="</chart>";
		} catch (Exception e) {
			LogTool.fatal(e);
			return new EXEException("",e.getMessage(),"");
		}

		resobj.put("xml1", xml1);
		resobj.put("xml2", xml2);
		resobj.put("xml3", xml3);
		return resobj;
	}
	/**
	 * �������Դ�����ͼ��ͳ��ÿ�����Զ�Ӧ��Ƶʱ��
	 * @return
	 */
	public double statisticsHoursByLanguage(String language_id,ASObject obj){
		String runplan_type = (String) obj.get("runplan_type");
		String codes = (String) obj.get("codes");
		String unit = (String) obj.get("unit");
		String record_type = (String) obj.get("record_type");//1���� 2Ч��
		String zl_subsql = " and t.mon_area is not null ";
		String xg_subsql = " and t.xg_mon_area is not null ";
		double value=0.0;
		String sql="select t.start_time,t.end_time from zres_runplan_tab t" +
		            " where t.is_delete=0 and t.language_id is not null  and t.valid_end_time>=sysdate and t.runplan_type_id='"+runplan_type+"' ";
		  if(unit!=null&&!unit.equalsIgnoreCase("ȫ��")){
	        	String[] s = unit.split("-");
				sql+=" and to_date(start_time || '00','hh24:mi:ss') >= to_date('"+s[0]+":00','hh24:mi:ss') " +
					 " and to_date(t.start_time || '00','hh24:mi:ss')<to_date('"+s[1]+":00','hh24:mi:ss')"; 
			}else{
				//sql+=" and to_char(sysdate,'hh24:mi:ss') between t.start_time || ':00' and t.end_time || ':00' ";
			}
			if(codes!=null&&!codes.equalsIgnoreCase("")){
				if(codes.indexOf(",")>=0){
					 String[] s = codes.split(",");
					 String ss="";
					 String sss="";
					 for(int i=0;i<s.length;i++){
						 ss+=" t.mon_area like '%"+s[i]+"%' or ";
						 sss+=" t.xg_mon_area like '%"+s[i]+"%' or ";
					 }
					 sql+=" and (("+ss.substring(0, ss.length()-3)+") or ("+sss.substring(0, sss.length()-3)+"))";
				 }else{
					 sql+=" and (t.mon_area like '%"+codes+"%' or t.xg_mon_area like '%"+codes+"%' )";
				 }
			}
			if(language_id!=null&&!language_id.equalsIgnoreCase("")){
				sql+=" and t.language_id='"+language_id+"'";
			}
			if(record_type !=null && record_type.equals("1")){
				sql += zl_subsql;
			} else if(record_type !=null && record_type.equals("2")){
				sql += xg_subsql;
			}
			try{
				GDSet gd = DbComponent.Query(sql);
				if(gd.getRowCount()>0){
					for(int i=0;i<gd.getRowCount();i++){
						String start_time = gd.getString(i, "start_time");
						String end_time = gd.getString(i, "end_time");
						long time = 0;
						time = StringTool.getTimeSecond(start_time.trim()+":00",end_time.trim()+":00");
	                    double hours = time * 1.0 / 3600;
	                    hours = StringTool.formatDouble(hours,2);
	                    value+=hours;
					}
				}
			}catch(Exception e){
				
			}
			
		return value;
	}

	
	/**
	 * ������ͼ��ͳ�Ƶ�ǰÿ������̨��Ƶ����
	 * @param obj
	 * @return
	 */
	public Object statisticsInfoStation(ASObject obj){
		ASObject resobj = new ASObject();
		String xml1 ="<chart yAxisName='Ƶ����' xAxisName='����̨' caption='��������̨Ƶ����ͳ��' baseFontSize='15' showBorder='1' imageSave='1'>";
		String xml2 ="<chart yAxisName='Ƶ����' xAxisName='����̨' caption='�����ղⷢ��̨Ƶ����ͳ��' baseFontSize='15' showBorder='1' imageSave='1'>";
		String xml3 ="<chart yAxisName='Ƶ����' xAxisName='����̨' caption='Ч���ղⷢ��̨Ƶ����ͳ��' baseFontSize='15' showBorder='1' imageSave='1'>";
		String runplan_type = (String) obj.get("runplan_type");
		String codes = (String) obj.get("codes");
		String unit = (String) obj.get("unit");
		String subsql = "";
		String zl_subsql = " and t.mon_area is not null ";
		String xg_subsql = " and t.xg_mon_area is not null ";
		String sqlTran = "select distinct name from res_transmit_station_tab where is_delete=0 order by name ";
		if(unit!=null&&!unit.equalsIgnoreCase("ȫ��")){
			String[] s = unit.split("-");
			subsql+=" and to_date(start_time || '00','hh24:mi:ss') >= to_date('"+s[0]+":00','hh24:mi:ss') " +
				 " and to_date(t.start_time || '00','hh24:mi:ss')<to_date('"+s[1]+":00','hh24:mi:ss')"; 
		}else{
//			sql+=" and to_char(sysdate,'hh24:mi:ss') between t.start_time || ':00' and t.end_time || ':00' ";
		}
		
		if(codes!=null&&!codes.equalsIgnoreCase("")){
			if(codes.indexOf(",")>=0){
				 String[] s = codes.split(",");
				 String ss="";
				 String sss="";
				 for(int i=0;i<s.length;i++){
					 ss+=" t.mon_area like '%"+s[i]+"%' or ";
					 sss+=" t.xg_mon_area like '%"+s[i]+"%' or ";
				 }
				 subsql+=" and (("+ss.substring(0, ss.length()-3)+") or ("+sss.substring(0, sss.length()-3)+"))";
			 }else{
				 subsql+=" and (t.mon_area like '%"+codes+"%' or t.xg_mon_area like '%"+codes+"%' )";
			 }
		}

		String sqlend = "   and t.runplan_type_id = '"+runplan_type+"' " +
		" group by t.station_id, rst.name,t.freq) tab group by tab.station_id,tab.name";
		String sql = "select tab.station_id,tab.name,count(*) as count from ( " +
		" select t.station_id, rst.name,t.freq  " +
		"   from zres_runplan_tab t, res_transmit_station_tab rst " +
		"  where t.is_delete = 0 " +
		"    and t.station_id is not null " +
		"    and t.station_id = rst.station_id " +
		"    and t.valid_end_time >= sysdate ";
		String sql1 = sql + subsql + sqlend+"  order by tab.name";
		try {
			GDSet gdTran = DbComponent.Query(sqlTran);
			ArrayList<String> sortlist = new ArrayList<String>();
			HashMap<String,String> map = new HashMap<String,String>();
			for(int m=0;m<gdTran.getRowCount();m++){
				map.put(gdTran.getString(m, "name"), "0");
				sortlist.add(gdTran.getString(m, "name"));
			}
			
			GDSet gd = DbComponent.Query(sql1);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					map.put(gd.getString(i, "name"), gd.getString(i, "count"));
				}
			} else {
			}
			Collections.sort(sortlist);
			for(int k=0;k<sortlist.size();k++){
				xml1+="<set label='"+sortlist.get(k)+"' value='"+map.get(sortlist.get(k))+"'/>\r";
			}
			xml1+="</chart>";

			sortlist = new ArrayList<String>();
			map = new HashMap<String,String>();
			for(int m=0;m<gdTran.getRowCount();m++){
				map.put(gdTran.getString(m, "name"), "0");
				sortlist.add(gdTran.getString(m, "name"));
			}
			sql1 = sql + subsql + zl_subsql + sqlend;
			gd = DbComponent.Query(sql1);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					map.put(gd.getString(i, "name"), gd.getString(i, "count"));
				}
			} else {
			}
			Collections.sort(sortlist);
			for(int k=0;k<sortlist.size();k++){
				xml2+="<set label='"+sortlist.get(k)+"' value='"+map.get(sortlist.get(k))+"'/>\r";
			}
			xml2+="</chart>";

			sortlist = new ArrayList<String>();
			map = new HashMap<String,String>();
			for(int m=0;m<gdTran.getRowCount();m++){
				map.put(gdTran.getString(m, "name"), "0");
				sortlist.add(gdTran.getString(m, "name"));
			}
			sql1 = sql + subsql + xg_subsql + sqlend;
			gd = DbComponent.Query(sql1);

			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					map.put(gd.getString(i, "name"), gd.getString(i, "count"));
				}
			} else {
			}
			Collections.sort(sortlist);
			for(int k=0;k<sortlist.size();k++){
				xml3+="<set label='"+sortlist.get(k)+"' value='"+map.get(sortlist.get(k))+"'/>\r";
			}
			xml3+="</chart>";
		} catch (Exception e) {
			LogTool.fatal(e);
			return new EXEException("",e.getMessage(),"");
		}
		
		resobj.put("xml1", xml1);
		resobj.put("xml2", xml2);
		resobj.put("xml3", xml3);
		return resobj;
	}
	
	/**
	 * ������ͼ����ȡ����ǰ������Ч�ķ���̨,Ȼ����ݷ���̨�����Ӧ��Ƶʱ��
	 * @param obj
	 * @return
	 */
	public Object statisticsInfoStationHours(ASObject obj){
		ASObject resobj = new ASObject();
		String xml1 ="<chart yAxisName='Ƶʱ�� ��λ��(Сʱ)' xAxisName='����̨' caption='��������̨Ƶʱ��ͳ��'  baseFontSize='15' showBorder='1' imageSave='1'>";
		String xml2 ="<chart yAxisName='Ƶʱ��  ��λ��(Сʱ)' xAxisName='����̨' caption='��������̨Ƶʱ��ͳ��' baseFontSize='15' showBorder='1' imageSave='1'>";
		String xml3 ="<chart yAxisName='Ƶʱ��  ��λ��(Сʱ)' xAxisName='����̨' caption='Ч������̨Ƶʱ��ͳ��' baseFontSize='15' showBorder='1' imageSave='1'>";
		String runplan_type = (String) obj.get("runplan_type");
		String codes = (String) obj.get("codes");
		String unit = (String) obj.get("unit");
		String zl_subsql = " and t.mon_area is not null ";
		String xg_subsql = " and t.xg_mon_area is not null ";
		String sql = "select distinct t.station_id,rst.name from zres_runplan_tab t,res_transmit_station_tab rst" +
				" where t.is_delete=0 and t.station_id is not null and t.station_id=rst.station_id(+) " +
				"and t.valid_end_time>=sysdate and t.runplan_type_id='"+runplan_type+"'";
		String sqlTran = "select distinct name from res_transmit_station_tab where is_delete=0 order by name ";
		if(unit!=null&&!unit.equalsIgnoreCase("ȫ��")){
			String[] s = unit.split("-");
			sql+=" and to_date(t.start_time || '00','hh24:mi:ss') >= to_date('"+s[0]+":00','hh24:mi:ss') " +
			 " and to_date(t.start_time || '00','hh24:mi:ss')<to_date('"+s[1]+":00','hh24:mi:ss')"; 
		}else{
			//sql+=" and to_char(sysdate,'hh24:mi:ss') between t.start_time || ':00' and t.end_time || ':00' ";
		}
		if(codes!=null&&!codes.equalsIgnoreCase("")){
			if(codes.indexOf(",")>=0){
				 String[] s = codes.split(",");
				 String ss="";
				 String sss="";
				 for(int i=0;i<s.length;i++){
					 ss+=" t.mon_area like '%"+s[i]+"%' or ";
					 sss+=" t.xg_mon_area like '%"+s[i]+"%' or ";
				 }
				 sql+=" and (("+ss.substring(0, ss.length()-3)+") or ("+sss.substring(0, sss.length()-3)+"))";
			 }else{
				 sql+=" and (t.mon_area like '%"+codes+"%' or t.xg_mon_area like '%"+codes+"%' )";
			 }
		}
		try {
			GDSet gdTran = DbComponent.Query(sqlTran);
			ArrayList<String> sortlist = new ArrayList<String>();
			HashMap<String,Double> map = new HashMap<String,Double>();
			for(int m=0;m<gdTran.getRowCount();m++){
				map.put(gdTran.getString(m, "name"), 0.0);
				sortlist.add(gdTran.getString(m, "name"));
			}
			
			String sql1 = sql;
			GDSet gd1 = DbComponent.Query(sql1);
			if(gd1.getRowCount()>0){
				for(int j=0;j<gd1.getRowCount();j++){
					double value=statisticsHoursByStation(gd1.getString(j, "station_id"),obj);
					map.put(gd1.getString(j, "name"), value);
					
				}
			} else {
			}
			Collections.sort(sortlist);
			for(int k=0;k<sortlist.size();k++){
				xml1+="<set label='"+sortlist.get(k)+"' value='"+map.get(sortlist.get(k))+"'/>\r";
			}
			xml1 = xml1.replaceAll("value='0.0'", "value='0'");
			xml1+="</chart>";

			sortlist = new ArrayList<String>();
			map = new HashMap<String,Double>();
			for(int m=0;m<gdTran.getRowCount();m++){
				map.put(gdTran.getString(m, "name"), 0.0);
				sortlist.add(gdTran.getString(m, "name"));
			}
			sql1 = sql + zl_subsql;
			obj.put("record_type", "1");
			gd1 = DbComponent.Query(sql1);
			if(gd1.getRowCount()>0){
				for(int j=0;j<gd1.getRowCount();j++){
					double value=statisticsHoursByStation(gd1.getString(j, "station_id"),obj);
					map.put(gd1.getString(j, "name"), value);
					
				}
			} else {
			}
			Collections.sort(sortlist);
			for(int k=0;k<sortlist.size();k++){
				xml2+="<set label='"+sortlist.get(k)+"' value='"+map.get(sortlist.get(k))+"'/>\r";
			}
			xml2 = xml2.replaceAll("value='0.0'", "value='0'");
			xml2+="</chart>";

			sortlist = new ArrayList<String>();
			map = new HashMap<String,Double>();
			for(int m=0;m<gdTran.getRowCount();m++){
				map.put(gdTran.getString(m, "name"), 0.0);
				sortlist.add(gdTran.getString(m, "name"));
			}
			sql1 = sql + xg_subsql;
			obj.put("record_type", "2");
			gd1 = DbComponent.Query(sql1);
			if(gd1.getRowCount()>0){
				for(int j=0;j<gd1.getRowCount();j++){
					double value=statisticsHoursByStation(gd1.getString(j, "station_id"),obj);
					map.put(gd1.getString(j, "name"), value);
					
				}
			} else {
			}
			Collections.sort(sortlist);
			for(int k=0;k<sortlist.size();k++){
				xml3+="<set label='"+sortlist.get(k)+"' value='"+map.get(sortlist.get(k))+"'/>\r";
			}
			xml3 = xml3.replaceAll("value='0.0'", "value='0'");
			xml3+="</chart>";
		} catch (Exception e) {
			LogTool.fatal(e);
			return new EXEException("",e.getMessage(),"");
		}

		resobj.put("xml1", xml1);
		resobj.put("xml2", xml2);
		resobj.put("xml3", xml3);
		
		return resobj;
	}
	
	/**
	 * ������ͼ��ͳ�Ƶ�ǰÿ��ת��������Ƶ����
	 * @param obj
	 * @return
	 */
	public Object statisticsInfoRedisseminators(ASObject obj){
		ASObject resobj = new ASObject();
		String xml1 ="<chart yAxisName='Ƶ����' xAxisName='ת������' caption='����ת������Ƶ����ͳ��' baseFontSize='15' showBorder='1' imageSave='1'>";
		String xml2 ="<chart yAxisName='Ƶ����' xAxisName='ת������' caption='�����ղ�ת������Ƶ����ͳ��' baseFontSize='15' showBorder='1' imageSave='1'>";
		String xml3 ="<chart yAxisName='Ƶ����' xAxisName='ת������' caption='Ч���ղ�ת������Ƶ����ͳ��' baseFontSize='15' showBorder='1' imageSave='1'>";
		String runplan_type = (String) obj.get("runplan_type");
		String codes = (String) obj.get("codes");
		String unit = (String) obj.get("unit");
		String subsql = "";
		String zl_subsql = " and t.mon_area is not null ";
		String xg_subsql = " and t.xg_mon_area is not null ";
		if(unit!=null&&!unit.equalsIgnoreCase("ȫ��")){
			String[] s = unit.split("-");
			subsql+=" and to_date(start_time || '00','hh24:mi:ss') >= to_date('"+s[0]+":00','hh24:mi:ss') " +
				 " and to_date(t.start_time || '00','hh24:mi:ss')<to_date('"+s[1]+":00','hh24:mi:ss')"; 
		}else{
//			sql+=" and to_char(sysdate,'hh24:mi:ss') between t.start_time || ':00' and t.end_time || ':00' ";
		}
		
		if(codes!=null&&!codes.equalsIgnoreCase("")){
			if(codes.indexOf(",")>=0){
				 String[] s = codes.split(",");
				 String ss="";
				 String sss="";
				 for(int i=0;i<s.length;i++){
					 ss+=" t.mon_area like '%"+s[i]+"%' or ";
					 sss+=" t.xg_mon_area like '%"+s[i]+"%' or ";
				 }
				 subsql+=" and (("+ss.substring(0, ss.length()-3)+") or ("+sss.substring(0, sss.length()-3)+"))";
			 }else{
				 subsql+=" and (t.mon_area like '%"+codes+"%' or t.xg_mon_area like '%"+codes+"%' )";
			 }
		}

		String sqlend = "   and t.runplan_type_id = '"+runplan_type+"' " +
		" group by t.redisseminators,t.freq) tab group by tab.redisseminators";
		String sql = "select tab.redisseminators,count(*) as count from ( " +
		" select t.redisseminators,t.freq  " +
		"   from zres_runplan_tab t" +
		"  where t.is_delete = 0 " +
		"    and t.valid_end_time >= sysdate ";
		String sql1 = sql + subsql + sqlend;
		try {
			GDSet gd = DbComponent.Query(sql1);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					xml1+="<set label='"+gd.getString(i, "redisseminators")+"' value='"+gd.getString(i, "count")+"'/>\r";
//					ASObject asobj = new ASObject();
//					asobj.put("label", gd.getString(i, "name"));
//					asobj.put("value", gd.getString(i, "count"));
//				//	asobj.put("link", "javascript:alert('id\r \n \r')");
//					list.add(asobj);
				}
			} else {
				resobj.put("xml1", "��ͳ�����ݣ�");
				return resobj;
			}
			xml1+="</chart>";

			sql1 = sql + subsql + zl_subsql + sqlend;
			gd = DbComponent.Query(sql1);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					xml2+="<set label='"+gd.getString(i, "redisseminators")+"' value='"+gd.getString(i, "count")+"'/>\r";
				}
			} else {
			}
			xml2+="</chart>";
			
			sql1 = sql + subsql + xg_subsql + sqlend;
			gd = DbComponent.Query(sql1);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					xml3+="<set label='"+gd.getString(i, "redisseminators")+"' value='"+gd.getString(i, "count")+"'/>\r";
				}
			} else {
			}
			xml3+="</chart>";
		} catch (Exception e) {
			LogTool.fatal(e);
			return new EXEException("",e.getMessage(),"");
		}
		
		resobj.put("xml1", xml1);
		resobj.put("xml2", xml2);
		resobj.put("xml3", xml3);
		return resobj;
	}
	
	/**
	 * ������ͼ����ȡ����ǰ������Ч��ת������,Ȼ�����ת�����������Ӧ��Ƶʱ��
	 * @param obj
	 * @return
	 */
	public Object statisticsInfoRedisseminatorsHours(ASObject obj){
		ASObject resobj = new ASObject();
		String xml1 ="<chart yAxisName='Ƶʱ�� ��λ��(Сʱ)' xAxisName='ת������' caption='����ת������Ƶʱ��ͳ��'  baseFontSize='15' showBorder='1' imageSave='1'>";
		String xml2 ="<chart yAxisName='Ƶʱ��  ��λ��(Сʱ)' xAxisName='ת������' caption='����ת������Ƶʱ��ͳ��' baseFontSize='15' showBorder='1' imageSave='1'>";
		String xml3 ="<chart yAxisName='Ƶʱ��  ��λ��(Сʱ)' xAxisName='ת������' caption='Ч��ת������Ƶʱ��ͳ��' baseFontSize='15' showBorder='1' imageSave='1'>";
		String runplan_type = (String) obj.get("runplan_type");
		String codes = (String) obj.get("codes");
		String unit = (String) obj.get("unit");
		String zl_subsql = " and t.mon_area is not null ";
		String xg_subsql = " and t.xg_mon_area is not null ";
		String sql = "select distinct t.redisseminators from zres_runplan_tab t" +
				" where t.is_delete=0 and t.redisseminators is not null " +
				"and t.valid_end_time>=sysdate and t.runplan_type_id='"+runplan_type+"'";
		
		if(unit!=null&&!unit.equalsIgnoreCase("ȫ��")){
			String[] s = unit.split("-");
			sql+=" and to_date(t.start_time || '00','hh24:mi:ss') >= to_date('"+s[0]+":00','hh24:mi:ss') " +
			 " and to_date(t.start_time || '00','hh24:mi:ss')<to_date('"+s[1]+":00','hh24:mi:ss')"; 
		}else{
			//sql+=" and to_char(sysdate,'hh24:mi:ss') between t.start_time || ':00' and t.end_time || ':00' ";
		}
		if(codes!=null&&!codes.equalsIgnoreCase("")){
			if(codes.indexOf(",")>=0){
				 String[] s = codes.split(",");
				 String ss="";
				 String sss="";
				 for(int i=0;i<s.length;i++){
					 ss+=" t.mon_area like '%"+s[i]+"%' or ";
					 sss+=" t.xg_mon_area like '%"+s[i]+"%' or ";
				 }
				 sql+=" and (("+ss.substring(0, ss.length()-3)+") or ("+sss.substring(0, sss.length()-3)+"))";
			 }else{
				 sql+=" and (t.mon_area like '%"+codes+"%' or t.xg_mon_area like '%"+codes+"%' )";
			 }
		}
		try {
			String sql1 = sql;
			GDSet gd1 = DbComponent.Query(sql1);
			if(gd1.getRowCount()>0){
				for(int j=0;j<gd1.getRowCount();j++){
					double value=statisticsHoursByRedisseminators(gd1.getString(j, "redisseminators"),obj);
					xml1+="<set label='"+gd1.getString(j, "redisseminators")+"' value='"+value+"'/>\r";
//					ASObject asobj = new ASObject();
//					asobj.put("label", gd1.getString(j, "name"));
//					asobj.put("value", value);
//					list.add(asobj);
				}
			} else {
				return "��ͳ�����ݣ�";
			}
			xml1+="</chart>";

			sql1 = sql + zl_subsql;
			obj.put("record_type", "1");
			gd1 = DbComponent.Query(sql1);
			if(gd1.getRowCount()>0){
				for(int j=0;j<gd1.getRowCount();j++){
					double value=statisticsHoursByRedisseminators(gd1.getString(j, "redisseminators"),obj);
					xml2+="<set label='"+gd1.getString(j, "redisseminators")+"' value='"+value+"'/>\r";
				}
			} else {
			}
			xml2+="</chart>";

			sql1 = sql + xg_subsql;
			obj.put("record_type", "2");
			gd1 = DbComponent.Query(sql1);
			if(gd1.getRowCount()>0){
				for(int j=0;j<gd1.getRowCount();j++){
					double value=statisticsHoursByRedisseminators(gd1.getString(j, "redisseminators"),obj);
					xml3+="<set label='"+gd1.getString(j, "redisseminators")+"' value='"+value+"'/>\r";
				}
			} else {
			}
			xml3+="</chart>";
		} catch (Exception e) {
			LogTool.fatal(e);
			return new EXEException("",e.getMessage(),"");
		}

		resobj.put("xml1", xml1);
		resobj.put("xml2", xml2);
		resobj.put("xml3", xml3);
		
		return resobj;
	}
	
	/**
	 * ����ÿ������̨��Ƶʱ��
	 * @param station_id
	 * @param obj
	 * @return
	 */
	public double statisticsHoursByStation(String station_id,ASObject obj){
		double value=0.0;
		String runplan_type = (String) obj.get("runplan_type");
		String codes = (String) obj.get("codes");
		String unit = (String) obj.get("unit");
		String record_type = (String) obj.get("record_type");//1���� 2Ч��
		String zl_subsql = " and t.mon_area is not null ";
		String xg_subsql = " and t.xg_mon_area is not null ";
		String sql="select t.station_id,rst.name,t.start_time,t.end_time from zres_runplan_tab t, res_transmit_station_tab rst " +
		           "where t.is_delete=0 and t.station_id is not null and t.station_id = rst.station_id and t.valid_end_time>=sysdate  and t.runplan_type_id='"+runplan_type+"' ";
		if(unit!=null&&!unit.equalsIgnoreCase("ȫ��")){
			String[] s = unit.split("-");
			sql+=" and to_date(start_time || '00','hh24:mi:ss') >= to_date('"+s[0]+":00','hh24:mi:ss') " +
				 " and to_date(t.start_time || '00','hh24:mi:ss')<to_date('"+s[1]+":00','hh24:mi:ss')"; 
		}else{
			//sql+=" and to_char(sysdate,'hh24:mi:ss') between t.start_time || ':00' and t.end_time || ':00' ";
		}
		if(codes!=null&&!codes.equalsIgnoreCase("")){
			if(codes.indexOf(",")>=0){
				 String[] s = codes.split(",");
				 String ss="";
				 String sss="";
				 for(int i=0;i<s.length;i++){
					 ss+=" t.mon_area like '%"+s[i]+"%' or ";
					 sss+=" t.xg_mon_area like '%"+s[i]+"%' or ";
				 }
				 sql+=" and (("+ss.substring(0, ss.length()-3)+") or ("+sss.substring(0, sss.length()-3)+"))";
			 }else{
				 sql+=" and (t.mon_area like '%"+codes+"%' or t.xg_mon_area like '%"+codes+"%' )";
			 }
		}
		if(station_id!=null&&!station_id.equalsIgnoreCase("")){
			sql+=" and t.station_id='"+station_id+"'";
		}
		if(record_type !=null && record_type.equals("1")){
			sql += zl_subsql;
		} else if(record_type !=null && record_type.equals("2")){
			sql += xg_subsql;
		}
		try{
			GDSet gd = DbComponent.Query(sql);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					String start_time = gd.getString(i, "start_time");
					String end_time = gd.getString(i, "end_time");
					long time = 0;
					time = StringTool.getTimeSecond(start_time.trim()+":00",end_time.trim()+":00");
                    double hours = time * 1.0 / 3600;
                    hours = StringTool.formatDouble(hours,2);
                    value+=hours;
				}
			}
		}catch(Exception e){
			
		}
		
		return value;
	}
	
	/**
	 * ����ÿ��ת��������Ƶʱ��
	 * @param station_id
	 * @param obj
	 * @return
	 */
	public double statisticsHoursByRedisseminators(String redisseminators,ASObject obj){
		double value=0.0;
		String runplan_type = (String) obj.get("runplan_type");
		String codes = (String) obj.get("codes");
		String unit = (String) obj.get("unit");
		String record_type = (String) obj.get("record_type");//1���� 2Ч��
		String zl_subsql = " and t.mon_area is not null ";
		String xg_subsql = " and t.xg_mon_area is not null ";
		String sql="select t.redisseminators,t.start_time,t.end_time from zres_runplan_tab t" +
		           " where t.is_delete=0 and t.redisseminators is not null  and t.valid_end_time>=sysdate  and t.runplan_type_id='"+runplan_type+"' ";
		if(unit!=null&&!unit.equalsIgnoreCase("ȫ��")){
			String[] s = unit.split("-");
			sql+=" and to_date(start_time || '00','hh24:mi:ss') >= to_date('"+s[0]+":00','hh24:mi:ss') " +
				 " and to_date(t.start_time || '00','hh24:mi:ss')<to_date('"+s[1]+":00','hh24:mi:ss')"; 
		}else{
			//sql+=" and to_char(sysdate,'hh24:mi:ss') between t.start_time || ':00' and t.end_time || ':00' ";
		}
		if(codes!=null&&!codes.equalsIgnoreCase("")){
			if(codes.indexOf(",")>=0){
				 String[] s = codes.split(",");
				 String ss="";
				 String sss="";
				 for(int i=0;i<s.length;i++){
					 ss+=" t.mon_area like '%"+s[i]+"%' or ";
					 sss+=" t.xg_mon_area like '%"+s[i]+"%' or ";
				 }
				 sql+=" and (("+ss.substring(0, ss.length()-3)+") or ("+sss.substring(0, sss.length()-3)+"))";
			 }else{
				 sql+=" and (t.mon_area like '%"+codes+"%' or t.xg_mon_area like '%"+codes+"%' )";
			 }
		}
		if(redisseminators!=null&&!redisseminators.equalsIgnoreCase("")){
			sql+=" and t.redisseminators like '%"+redisseminators+"%'";
		}
		if(record_type !=null && record_type.equals("1")){
			sql += zl_subsql;
		} else if(record_type !=null && record_type.equals("2")){
			sql += xg_subsql;
		}
		try{
			GDSet gd = DbComponent.Query(sql);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					String start_time = gd.getString(i, "start_time");
					String end_time = gd.getString(i, "end_time");
					long time = 0;
					time = StringTool.getTimeSecond(start_time.trim()+":00",end_time.trim()+":00");
                    double hours = time * 1.0 / 3600;
                    hours = StringTool.formatDouble(hours,2);
                    value+=hours;
				}
			}
		}catch(Exception e){
			
		}
		
		return value;
	}
	
}
