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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class EffectReportStatistics {

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
        ExecutorService executor = Executors.newCachedThreadPool();
        List<Future> futureList =new ArrayList();

		for(int i=0;i<subReportTypeArr.length;i++){
                Future<LinkedHashMap> future =executor.submit(new TaskWithResult(starttime, endtime, my_report_type,report_type, asobj, report_id,subReportTypeArr[i]));
                futureList.add(future);
//                for(Object o:map.keySet()){
//					descList.add(map.get(o));   // Map��ֵ
//				}
		}
		for(int k=0;k<futureList.size();k++)
        {
            LinkedHashMap map = (LinkedHashMap) futureList.get(k).get();
            for(Object o:map.keySet()){
					descList.add(map.get(o));   // Map��ֵ
		    }
        }

//		ZrstRepEffectStatisticsBean detailBean = new ZrstRepEffectStatisticsBean();

		ReportBean reportBean = ReportUtil.getBasicStatisticsReportBean(report_id, starttime, endtime, report_type, user_name);

		boolean boo =ReportUtil.insertReportByReportBean(reportBean,  descList, "ZRST_REP_EFFECT_STATISTICS_TAB", "com.viewscenes.bean.report.ZrstRepEffectStatisticsBean");
        System.out.println("\r<br>ͳ��Ч������ִ�к�ʱ : "+(System.currentTimeMillis()-a)/1000f+" �� ");
		return boo;

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
	 * @param
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
							float listionRate=0;
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
									 listionRate=Float.parseFloat(new BigDecimal((Double.parseDouble((count3+count4+count5)+"")*100/counts)).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
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
									 listionRate=Float.parseFloat(new BigDecimal((Double.parseDouble((count3+count4+count5)+"")*100/counts)).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
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
								 listionRate=Float.parseFloat(new BigDecimal((Double.parseDouble((count3+count4+count5)+"")*100/counts)).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
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
	 * @param
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
							float listionRate=0;
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
									 listionRate=Float.parseFloat(new BigDecimal(Double.parseDouble((count3+count4+count5)+"")*100/counts).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
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
									 listionRate=Float.parseFloat(new BigDecimal(Double.parseDouble((count3+count4+count5)+"")*100/counts).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
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
								 listionRate=Float.parseFloat(new BigDecimal(Double.parseDouble((count3+count4+count5)+"")*100/counts).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
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
        WritableCellFormat wcfF2 = new WritableCellFormat(wf1);
        wcfF2.setAlignment(jxl.format.Alignment.CENTRE);
        wcfF2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        wcfF2.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);

        WritableCellFormat wcfF3 = new WritableCellFormat(wf1);
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
				ws.addCell(new jxl.write.Number(15,nextRow, Float.parseFloat(totalBean.getListen()),wcfF2));
				ws.addCell(new jxl.write.Number(16,nextRow, Float.parseFloat(totalBean.getListen_middle()),wcfF2));
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
					if(Float.parseFloat(bean.getListen())<60){
						ws.addCell(new jxl.write.Number(15,curRow, Float.parseFloat(bean.getListen()),wcfF3));
					}else{
						ws.addCell(new jxl.write.Number(15,curRow, Float.parseFloat(bean.getListen()),wcfF2));
					}
				}else{
					ws.addCell(new jxl.write.Number(15,curRow, Float.parseFloat(bean.getListen()),wcfF2));
				}


				ws.addCell(new jxl.write.Number(16,curRow, Float.parseFloat(bean.getListen_middle()),wcfF2));
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
 								 ws.addCell(new jxl.write.Number((Integer)stationTotal1Map.get(ones.split("_")[0]), currRow,Integer.parseInt(ones.split("_").length==4?ones.split("_")[1]:"0"),wcfF2));
 							 }
 							 else if((Integer)stationTotal1Map.get(ones.split("_")[0])+1 == yy){
 								 ws.addCell(new jxl.write.Number((Integer)stationTotal1Map.get(ones.split("_")[0])+1, currRow, Integer.parseInt(ones.split("_").length==4?ones.split("_")[2]:"0"),wcfF2));
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

						if(Float.parseFloat(receiveVal[1].equals("A")?"100":receiveVal[1])<60){
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
 								 ws.addCell(new jxl.write.Number((Integer)languageTotal1Map.get(ones.split("_")[0]), currRow,Integer.parseInt(ones.split("_").length==4?ones.split("_")[1]:"0"),wcfF2));
 							 }
 							 else if((Integer)languageTotal1Map.get(ones.split("_")[0])+1 == yy){
 								 ws.addCell(new jxl.write.Number((Integer)languageTotal1Map.get(ones.split("_")[0])+1, currRow, Integer.parseInt(ones.split("_").length==4?ones.split("_")[2]:"0"),wcfF2));
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
					if(Float.parseFloat(receiveVal[1].equals("A")?"100":receiveVal[1])<60){
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
		                 ws.addCell(new jxl.write.Number(1,curRow,Integer.parseInt(oneValArr[1]),wcfF2));
		                 ws.addCell(new jxl.write.Number(2,curRow,Integer.parseInt(oneValArr[2]),wcfF2));
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

					if(Float.parseFloat(bean.getAverage_listens())>=80){
						ws.addCell(new Label(15,ws.getRows()-1, "�ɱ�֤����",wcfF2));
					}
					if(Float.parseFloat(bean.getAverage_listens())>=60&&Float.parseFloat(bean.getAverage_listens())<80){
						ws.addCell(new Label(15,ws.getRows()-1, "����������",wcfF2));
					}
					if(Float.parseFloat(bean.getAverage_listens())>=30&&Float.parseFloat(bean.getAverage_listens())<60){
						ws.addCell(new Label(15,ws.getRows()-1, "��ʱ������",wcfF2));
					}
					if(Float.parseFloat(bean.getAverage_listens())<30){
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
        WritableCellFormat wcfF2 = new WritableCellFormat(wf1);
        wcfF2.setAlignment(jxl.format.Alignment.CENTRE);
        wcfF2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        wcfF2.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);

        WritableCellFormat wcfF3 = new WritableCellFormat(wf1);
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
				ws.addCell(new jxl.write.Number(14,nextRow, Float.parseFloat(totalBean.getListen()),wcfF2));
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
					if(Float.parseFloat(bean.getListen())<60){
						ws.addCell(new jxl.write.Number(14,curRow, Float.parseFloat(bean.getListen()),wcfF3));
					}else{
						ws.addCell(new jxl.write.Number(14,curRow, Float.parseFloat(bean.getListen()),wcfF2));
					}
				}else{
					ws.addCell(new jxl.write.Number(14,curRow, Float.parseFloat(bean.getListen()),wcfF2));
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
 								 ws.addCell(new jxl.write.Number((Integer)stationTotal1Map.get(ones.split("_")[0]), currRow,Integer.parseInt(ones.split("_").length==4?ones.split("_")[1]:"0"),wcfF2));
 							 }
 							 else if((Integer)stationTotal1Map.get(ones.split("_")[0])+1 == yy){
 								 ws.addCell(new jxl.write.Number((Integer)stationTotal1Map.get(ones.split("_")[0])+1, currRow, Integer.parseInt(ones.split("_").length==4?ones.split("_")[2]:"0"),wcfF2));
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
					if(Float.parseFloat(receiveVal[1].equals("A")?"100":receiveVal[1])<60){
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
 								 ws.addCell(new jxl.write.Number((Integer)languageTotal1Map.get(ones.split("_")[0]), currRow,Integer.parseInt(ones.split("_").length==4?ones.split("_")[1]:"0"),wcfF2));
 							 }
 							 else if((Integer)languageTotal1Map.get(ones.split("_")[0])+1 == yy){
 								 ws.addCell(new jxl.write.Number((Integer)languageTotal1Map.get(ones.split("_")[0])+1, currRow, Integer.parseInt(ones.split("_").length==4?ones.split("_")[2]:"0"),wcfF2));
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
					if(Float.parseFloat(receiveVal[1].equals("A")?"100":receiveVal[1])<60){
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
		                 ws.addCell(new jxl.write.Number(1,curRow,Integer.parseInt(oneValArr[1]),wcfF2));
		                 ws.addCell(new jxl.write.Number(2,curRow,Integer.parseInt(oneValArr[2]),wcfF2));
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
					ws.addCell(new jxl.write.Number(5,ws.getRows()-1, Integer.parseInt(bean.getReceive_count()),wcfF2));
					ws.addCell(new Label(6,ws.getRows()-1, bean.getFen5(),wcfF2));
					ws.addCell(new Label(7,ws.getRows()-1, bean.getFen4(),wcfF2));
					ws.addCell(new Label(8,ws.getRows()-1,  bean.getFen3(),wcfF2));
					ws.addCell(new Label(9,ws.getRows()-1, bean.getFen2(),wcfF2));
					ws.addCell(new Label(10,ws.getRows()-1, bean.getFen1(),wcfF2));
					ws.addCell(new Label(11,ws.getRows()-1, bean.getFen0(),wcfF2));
					ws.addCell(new Label(12,ws.getRows()-1, bean.getAverage_listens(),wcfF2));
					if(Float.parseFloat(bean.getAverage_listens())>=80){
						ws.addCell(new Label(13,ws.getRows()-1, "�ɱ�֤����",wcfF2));
					}
					if(Float.parseFloat(bean.getAverage_listens())>=60&&Float.parseFloat(bean.getAverage_listens())<80){
						ws.addCell(new Label(13,ws.getRows()-1, "����������",wcfF2));
					}
					if(Float.parseFloat(bean.getAverage_listens())>=30&&Float.parseFloat(bean.getAverage_listens())<60){
						ws.addCell(new Label(13,ws.getRows()-1, "��ʱ������",wcfF2));
					}
					if(Float.parseFloat(bean.getAverage_listens())<30){
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
     * @param
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
			WritableFont wfc = new WritableFont(WritableFont.ARIAL,10,WritableFont.NO_BOLD,false,UnderlineStyle.NO_UNDERLINE, Colour.RED);
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
            WritableCellFormat wcfF2 = new WritableCellFormat(wf1);
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
        WritableCellFormat wcfF2 = new WritableCellFormat(wf1);
        wcfF2.setAlignment(jxl.format.Alignment.CENTRE);
        wcfF2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        wcfF2.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);

        WritableCellFormat wcfF3 = new WritableCellFormat(wf1);
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
	 * @detail  
	 * @method  
	 * @param args 
	 * @return  void  
	 * @author  zhaoyahui
	 * @version 2013-1-6 ����03:10:29
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EffectReportStatistics s=new EffectReportStatistics();
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
						float listionRate=0;
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
								 listionRate=Float.parseFloat(new BigDecimal(Double.parseDouble((count3+count4+count5)+"")*100/counts).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
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
								 listionRate=Float.parseFloat(new BigDecimal(Double.parseDouble((count3+count4+count5)+"")*100/counts).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
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
							 listionRate=Float.parseFloat(new BigDecimal(Double.parseDouble((count3+count4+count5)+"")*100/counts).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
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
		
	
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
}
