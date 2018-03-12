package com.viewscenes.web.runplanManager.wgtRunplan;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.viewscenes.bean.runplan.WGTRunplanBean;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.pub.GDSet;
import com.viewscenes.util.StringTool;
import com.viewscenes.web.common.Common;

import flex.messaging.io.amf.ASObject;

public class WGTRunplanDAO {
	
	/**
	 * 外国台运行图添加入库
	 * @param bean
	 * @return
	 * @throws DbException
	 */
	public String addRunplan(WGTRunplanBean bean) throws DbException{
		String res="添加运行图成功!";
		String input_person = bean.getInput_person();//录入人
		String runplan_type_id = bean.getRunplan_type_id(); //运行图类型id
		String broadcast_country = bean.getBroadcast_country();//播音国家
		String broadcast_station = bean.getBroadcast_station();//播音电台
		String launch_country = bean.getLaunch_country();    //发射国家
		String sentcity_id = bean.getSentcity_id();  //发射城市id
		
		String station_id = bean.getStation_id();    //发射台id
		String station_name = bean.getStation_name(); //发射台
		String transmiter_no = bean.getTransmiter_no();  //发射机号
	    String seadon_id = bean.getSeason_id();//季节代号
		String freq =bean.getFreq();      //频率
		String antenna = bean.getAntenna(); //天线号
		String antennatype = bean.getAntennatype();//天线程式
		String language_id = bean.getLanguage_id();//语言id
		String direction = bean.getDirection(); //方向
		String power = bean.getPower();  //功率
		String service_area = bean.getService_area(); //服务区
		String ciraf = bean.getCiraf(); // CIRAF区
		String rest_datetime = bean.getRest_datetime(); // //休息日期
		String rest_time = bean.getRest_time();//休息时间
		String start_time = bean.getStart_time(); //播音开始时间
		String end_time = bean.getEnd_time();   //播音结束时间
		String mon_area = bean.getMon_area();    //遥控站收测
		String valid_start_time = bean.getValid_start_time(); //启用期
		String valid_end_time = bean.getValid_end_time();  //停用期
		String[] times = getTimeDif(start_time,end_time,mon_area);//根据时差计算当地时间
		String remark = bean.getRemark();//备注
		StringBuffer addsql = new StringBuffer("insert into zres_runplan_tab (runplan_id,launch_country,broadcast_country,broadcast_station,sentcity_id,station_id,station_name,transmiter_no," +
				                  "season_id,freq,antenna,antennatype,runplan_type_id,");
		             addsql.append("language_id,direction,power,service_area,ciraf,start_time,end_time,rest_datetime,rest_time,");
		             addsql.append("remark,valid_start_time,valid_end_time,store_datetime,input_person,is_delete,mon_area,local_start_time,local_end_time)");
		             addsql.append(" values(zres_runplan_seq.nextval,'"+launch_country+"','"+broadcast_country+"','"+broadcast_station+"','"+sentcity_id+"','"+station_id+"','"+station_name+"','"+transmiter_no+"'," +
		             		      "'"+seadon_id+"','"+freq+"','"+antenna+"','"+antennatype+"','"+runplan_type_id+"',");
		             addsql.append("'"+language_id+"','"+direction+"','"+power+"','"+service_area+"','"+ciraf+"','"+start_time+"','"+end_time+"','"+rest_datetime+"','"+rest_time+"',");
		             addsql.append("'"+remark+"','"+valid_start_time+"','"+valid_end_time+"',sysdate,'"+input_person+"',0,'"+mon_area+"','"+times[0]+"','"+times[1]+"')");
		 DbComponent.exeUpdate(addsql.toString());
		return res;
	}
	
	/**
	 * 根据发射国家计算当地时差
	 * @author 王福祥
	 * @param startTime
	 * @param endTime
	 * @param launch_country
	 * @return
	 */
	public  String[] getTimeDif(String startTime,String endTime,String mon_area){
		String[] s=new String[2];
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");     
		String sql=" select time_diff from res_headend_tab where time_diff is not null  ";
		if(mon_area!=null&&!mon_area.equalsIgnoreCase("")){
			sql+=" and code like '%"+mon_area+"%'";
		}else{
			s[0]=startTime;
			s[1]=endTime;
			return s;
		}
		double moveut=0; //时差
		try {
			Date date1=sdf.parse(startTime);
			Date date2=sdf.parse(endTime);
			GDSet gd = DbComponent.Query(sql);
			if(gd.getRowCount()>0){
				moveut = Double.parseDouble(gd.getString(0, "time_diff"));
				long time1=(long) (date1.getTime()+(moveut*3600*1000));
				long time2=(long) (date2.getTime()+(moveut*3600*1000));
				date1.setTime(time1);
				date2.setTime(time2);
				s[0]=sdf.format(date1);
				s[1]=sdf.format(date2);
			}else{
				s[0]=startTime;
				s[1]=endTime;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return s;
	}
/**
 * 外国台运行图查询
 * @param bean
 * @return
 * @throws Exception
 */
	public ASObject queryRunplan(WGTRunplanBean bean) throws Exception{
		String runplan_id = bean.getRunplan_id();   //运行图id
		String runplan_type_id = bean.getRunplan_type_id(); //运行图类型id
		String broadcast_country = bean.getBroadcast_country();//播音国家
		String broadcast_station = bean.getBroadcast_station();//播音电台
		String launch_country = bean.getLaunch_country();//发射国家
		String sentcity_id = bean.getSentcity_id();  //发射城市id
		String station_id = bean.getStation_id();    //发射台id
		String station_name = bean.getStation_name(); //发射台
		String transmiter_no = bean.getTransmiter_no();  //发射机号
		String season_id = bean.getSeason_id();//季节代号
		String freq =bean.getFreq();      //频率
		String antenna = bean.getAntenna(); //天线号
		String antennatype = bean.getAntennatype();//天线程式
		String direction = bean.getDirection(); //方向
		String language_id = bean.getLanguage_id(); //语言id
		String power = bean.getPower();  //功率
		String service_area = bean.getService_area();//服务区
		String ciraf = bean.getCiraf(); // CIRAF区
		String start_time = bean.getStart_time(); //播音开始时间
		String end_time = bean.getEnd_time();   //播音结束时间
		String mon_area = bean.getMon_area();    //遥控站收测
		String rest_datetime = bean.getRest_datetime(); // //休息日期
		String rest_time = bean.getRest_time(); //休息时间
		String valid_start_time = bean.getValid_start_time(); //启用期
		String valid_end_time = bean.getValid_end_time();  //停用期
		StringBuffer sqlbuffer =new StringBuffer("select zrt.*,zlt.language_name as language,rct.city as sentcity ") ;
		sqlbuffer.append(" from zres_runplan_tab zrt,zdic_language_tab zlt,res_transmit_station_tab rat,res_city_tab rct ");
		sqlbuffer.append(" where zrt.is_delete=0 and zrt.station_id=rat.station_id(+) and zrt.language_id=zlt.language_id(+) and zrt.sentcity_id=rct.id(+) ");
		if(runplan_id!=null&&!runplan_id.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.runplan_id='"+runplan_id+"'");
		}
		if(runplan_type_id!=null && !runplan_type_id.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.runplan_type_id='"+runplan_type_id+"'");
		}
		if(launch_country!=null&&!launch_country.equalsIgnoreCase("")){
			sqlbuffer.append("and zrt.launch_country like '%"+launch_country+"%'");
		}
		if(broadcast_country!=null&&!broadcast_country.equalsIgnoreCase("")){
			sqlbuffer.append(" and broadcast_country like '"+broadcast_country+"'");
		}
		if(broadcast_station!=null&&!broadcast_station.equalsIgnoreCase("")){
			sqlbuffer.append(" broadcast_station like '%"+broadcast_station+"%'");
		}
		if(station_id!=null && !station_id.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.station_id='"+station_id+"'");
		}
		
		if(station_name!=null && !station_name.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.station_name='"+station_name+"'");
		}
		if(sentcity_id!=null && !sentcity_id.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.sentcity_id='"+sentcity_id+"'");
		}
		if(transmiter_no!=null && !transmiter_no.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.transmiter_no='"+transmiter_no+"'");
		}
		if(freq!=null && !freq.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.freq='"+freq+"'");
		}
		if(antenna!=null && !antenna.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.antenna='"+antenna+"'");
		}
		if(antennatype!=null && !antennatype.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.antennatype='"+antennatype+"'");
		}
		if(direction!=null && !direction.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.direction='"+direction+"'");
		}
		if(language_id!=null && !language_id.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.language_id="+language_id+"");
		}
		if(power!=null &&!power.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.power="+power+"");
		}
		if(service_area!=null && !service_area.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.service_area='"+service_area+"'");
		}
		if(ciraf!=null && !ciraf.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.ciraf='"+ciraf+"'");
		}
		if(start_time!=null && !start_time.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.start_time=to_char('"+start_time+"')");
		}
		if(end_time!=null && !end_time.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.end_time=to_char('"+end_time+"')");
		}
		if(mon_area!=null && !mon_area.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.mon_area like '%"+mon_area+"%'");
		}
		if(rest_datetime!=null && !rest_datetime.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.rest_datetime=to_date('"+rest_datetime+"','yyyy-MM-dd HH24:MI:SS')");
		}
		if(rest_time!=null && !rest_time.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.rest_time=to_char('"+rest_time+"')");
		}
	
		ASObject resObj=StringTool.pageQuerySql(sqlbuffer.toString(), bean);
		return resObj;
	}
}
