package com.viewscenes.web.runplanManager.gjtRunplan;

import com.viewscenes.bean.runplan.GJTRunplanBean;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.pub.GDSet;
import com.viewscenes.pub.GDSetException;
import com.viewscenes.util.StringTool;

import flex.messaging.io.amf.ASObject;

public class GJTRunplanDAO {

	public ASObject queryRunplan(GJTRunplanBean bean) throws Exception{
		String runplan_id = bean.getRunplan_id();   //运行图id
		String runplan_type_id = bean.getRunplan_type_id(); //运行图类型id
		String station_id = bean.getStation_id();    //发射台id
		String station_name = bean.getStation_name(); //发射台
		//String sentcity_id = bean.getSentcity_id();  //发射城市id
		String transmiter_no = bean.getTransmiter_no();  //发射机号
		String freq =bean.getFreq();      //频率
		String antenna = bean.getAntenna(); //天线号
		String antennatype = bean.getAntennatype();//天线程式
		String direction = bean.getDirection(); //方向
		String language_id = bean.getLanguage_id(); //语言id
		String power = bean.getPower();  //功率
		String seasonCode = bean.getSeason_id();//;季节代号
		String program_type_id = bean.getProgram_type_id(); //节目类型id
		String service_area = bean.getService_area(); //服务区
		String ciraf = bean.getCiraf(); // CIRAF区
		String satellite_channel = bean.getSatellite_channel();//国际卫星通道
		String start_time = bean.getStart_time(); //播音开始时间
		String end_time = bean.getEnd_time();   //播音结束时间
		String mon_area = bean.getMon_area();    //质量收测
	
		String xg_mon_area = bean.getXg_mon_area();//效果收测
		String remark = bean.getRemark();//备注
     	String rest_datetime = bean.getRest_datetime(); // //休息日期当作前台查询的有效日期
//		String rest_time = bean.getRest_time(); //休息时间
		String valid_start_time = bean.getValid_start_time(); //启用期
		String valid_end_time = bean.getValid_end_time();  //停用期
		
		StringBuffer sqlbuffer =new StringBuffer("select zrt.*,zlt.language_name as language ") ;
		sqlbuffer.append(" from zres_runplan_tab zrt,zdic_language_tab zlt,res_transmit_station_tab rat ");
		sqlbuffer.append(" where zrt.is_delete=0 and zrt.station_id=rat.station_id(+) and zrt.language_id=zlt.language_id(+)   ");
		if(runplan_id!=null&&!runplan_id.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.runplan_id='"+runplan_id+"'");
		}
		if(runplan_type_id!=null && !runplan_type_id.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.runplan_type_id='"+runplan_type_id+"'");
		}
		if(station_id!=null && !station_id.equalsIgnoreCase("")){
			sqlbuffer.append(" and (zrt.station_id='"+station_id+"' or zrt.station_name like '%"+station_name+"%')");
		}	
		if(seasonCode!=null&&!seasonCode.equalsIgnoreCase("")){//根据季节代号的有效期查询运行图
			sqlbuffer.append(" and zrt.season_id like '"+seasonCode+"' ");
//			GDSet gd1=getTimeBySeason(seasonCode);
//			if(gd1.getRowCount()>0){
//				sqlbuffer.append(" and zrt.valid_start_time>=to_date('"+gd1.getString(0, "start_time")+"','yyyy-mm-dd hh24:mi:ss') ");
//				sqlbuffer.append(" and zrt.valid_start_time<=to_date('"+gd1.getString(0, "end_time")+"','yyyy-mm-dd hh24:mi:ss') ");
//			}
		}
		if(transmiter_no!=null && !transmiter_no.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.transmiter_no like'%"+transmiter_no+"%'");
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
		if(program_type_id!=null && !program_type_id.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.program_type_id="+program_type_id+"");
		}
		if(service_area!=null && !service_area.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.service_area='"+service_area+"'");
		}
		if(ciraf!=null && !ciraf.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.ciraf='"+ciraf+"'");
		}
		if(satellite_channel!=null && !satellite_channel.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.satellite_channel='"+satellite_channel+"'");
		}
		if(start_time!=null && !start_time.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.start_time=to_char('"+start_time+"')");
		}
		if(end_time!=null && !end_time.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.end_time=to_char('"+end_time+"')");
		}
		
		if(remark!=null&&!remark.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.remark like '%"+remark+"%'");
		}
//		if((mon_area!=null && !mon_area.equalsIgnoreCase(""))&&(xg_mon_area!=null && !xg_mon_area.equalsIgnoreCase(""))){
//			sqlbuffer.append(" and (zrt.mon_area like '%"+mon_area+"%'");
//			sqlbuffer.append(" or zrt.xg_mon_area like '%"+xg_mon_area+"%')");
//		}else{
			if(mon_area!=null && !mon_area.equalsIgnoreCase("")){
				if(mon_area.indexOf(",")>0){
					String[] s=mon_area.split(",");
					StringBuffer temp = new StringBuffer("(");
					for(int i=0;i<s.length;i++){
						temp.append(" zrt.mon_area like '%"+s[i]+"%' or");
					}
					sqlbuffer.append(" and "+temp.toString().substring(0, temp.toString().length()-2)+")");
				}else{
					sqlbuffer.append(" and zrt.mon_area like '%"+mon_area+"%'");
				}
				
			}
			if(xg_mon_area!=null && !xg_mon_area.equalsIgnoreCase("")){
				if(xg_mon_area.indexOf(",")>0){
					String[] s=xg_mon_area.split(",");
					StringBuffer temp = new StringBuffer("(");
					for(int i=0;i<s.length;i++){
						temp.append(" zrt.xg_mon_area like '%"+s[i]+"%' or");
					}
					sqlbuffer.append(" and "+temp.toString().substring(0, temp.toString().length()-2)+")");
				}else{
					sqlbuffer.append(" and zrt.xg_mon_area like '%"+xg_mon_area+"%'");
				}
				
			}
//		}
		
		
	
//		if(rest_datetime!=null && !rest_datetime.equalsIgnoreCase("")){
//			sqlbuffer.append(" and zrt.rest_datetime=to_date('"+rest_datetime+"','yyyy-MM-dd HH24:MI:SS')");
//		}
//		if(rest_time!=null && !rest_time.equalsIgnoreCase("")){
//			sqlbuffer.append(" and zrt.rest_time=to_char('"+rest_time+"')");
//		}
			if(rest_datetime!=null&&!rest_datetime.equals(""))
			{
				sqlbuffer.append(" and zrt.valid_start_time<=to_date('"+rest_datetime+"','yyyy-MM-dd')");
				sqlbuffer.append(" and zrt.valid_end_time>=to_date('"+rest_datetime+"','yyyy-MM-dd') ");
				
				
			}else
			{
					if(valid_start_time!=null && !valid_start_time.equalsIgnoreCase("")){
						sqlbuffer.append(" and ((zrt.valid_start_time>=to_date('"+valid_start_time+"','yyyy-MM-dd HH24:MI:SS')");
						if(valid_end_time!=null && !valid_end_time.equalsIgnoreCase("")){
							sqlbuffer.append(" and zrt.valid_start_time<=to_date('"+valid_end_time+"','yyyy-MM-dd HH24:MI:SS'))"); 
						 }else
						 {
							 sqlbuffer.append("))"); 
						 }
							
					}
					if(valid_end_time!=null && !valid_end_time.equalsIgnoreCase("")){
						if(valid_start_time!=null && !valid_start_time.equalsIgnoreCase(""))
						{
						  sqlbuffer.append(" or (zrt.valid_end_time>=to_date('"+valid_start_time+"','yyyy-MM-dd HH24:MI:SS')");
						  sqlbuffer.append(" and zrt.valid_end_time<=to_date('"+valid_end_time+"','yyyy-MM-dd HH24:MI:SS'))) ");
						   	
						}else 
						{
						  sqlbuffer.append(" and ((zrt.valid_end_time<=to_date('"+valid_end_time+"','yyyy-MM-dd HH24:MI:SS'))) ");
					   }
					}
			}
//		//新要求，田雅静提出如果开始时间和结束时间都为空，需要让有效期开始时间小于当前时间。
//		if((valid_start_time==null ||valid_start_time.equalsIgnoreCase(""))&&(valid_end_time==null ||valid_end_time.equalsIgnoreCase(""))){
//			sqlbuffer.append(" and zrt.valid_start_time<=sysdate ");	
//		}
		sqlbuffer.append(" order by zrt.station_name,zrt.xg_mon_area,zrt.mon_area asc ");
		ASObject resObj=StringTool.pageQuerySql(sqlbuffer.toString(), bean);
		return resObj;
	}
	/**
	 * 运行图添加入库
	 * @param bean
	 * @return
	 * @throws DbException
	 * @throws GDSetException 
	 * 修改人：刘斌
	 * 修改逻辑：如果添加的运行图在同邻频干扰信息表中有符合条件的运行图，直接把此运行图也添加到同邻频干扰信息表中。
	 * 修改日期：2014.9.25
	 */
	public String addRunplan(GJTRunplanBean bean) throws DbException, GDSetException{
		String res="添加运行图成功!";
		String input_person = bean.getInput_person()==null?"":bean.getInput_person();//录入人
		String runplan_type_id = bean.getRunplan_type_id()==null?"":bean.getRunplan_type_id(); //运行图类型id
		String station_id = bean.getStation_id()==null?"":bean.getStation_id();    //发射台id
		String station_name = bean.getStation_name()==null?"":bean.getStation_name(); //发射台
		//String sentcity_id = bean.getSentcity_id()==null?"":bean.getSentcity_id();  //发射城市id
		String transmiter_no = bean.getTransmiter_no()==null?"":bean.getTransmiter_no();  //发射机号
		String freq =bean.getFreq()==null?"":bean.getFreq();      //频率
		String antenna = bean.getAntenna()==null?"":bean.getAntenna(); //天线号
		String antennatype = bean.getAntennatype()==null?"":bean.getAntennatype();//天线程式
		String direction = bean.getDirection()==null?"":bean.getDirection(); //方向
		String language_id = bean.getLanguage_id()==null?"":bean.getLanguage_id(); //语言id
		String power = bean.getPower()==null?"":bean.getPower();  //功率
		String program_type_id = bean.getProgram_type_id()==null?"":bean.getProgram_type_id(); //节目类型id
		String program_type = bean.getProgram_type()==null?"":bean.getProgram_type(); //节目类型
		String service_area = bean.getService_area()==null?"":bean.getService_area(); //服务区
		String ciraf = bean.getCiraf()==null?"":bean.getCiraf(); // CIRAF区
		String satellite_channel = bean.getSatellite_channel()==null?"":bean.getSatellite_channel();//国际卫星通道
		String start_time = bean.getStart_time()==null?"":bean.getStart_time(); //播音开始时间
		String end_time = bean.getEnd_time()==null?"":bean.getEnd_time();   //播音结束时间
		String mon_area =  bean.getMon_area()==null?"":bean.getMon_area();    //质量收测站点
		String xg_mon_area = bean.getXg_mon_area()==null?"":bean.getXg_mon_area();//效果收测站点
		String rest_datetime = bean.getRest_datetime()==null?"":bean.getRest_datetime(); // //休息日期
		String valid_start_time = bean.getValid_start_time()==null?"":bean.getValid_start_time(); //启用期
		String valid_end_time = bean.getValid_end_time()==null?"":bean.getValid_end_time();  //停用期
		String season_id = bean.getSeason_id()==null?"":bean.getSeason_id();//季节代号
		String remark = bean.getRemark()==null?"":bean.getRemark();//备注
		String weekday = bean.getWeekday()==null?"":bean.getWeekday();//周设置
		
		
		//这里导入运行图的时候需要判断，如果表中有此运行图实行更新，如果没有新增
		//判断条件为：频率，播音时间，服务区，发射台，方向，功率，语言，季节代号。
		String isExistsql="select t.runplan_id from zres_runplan_tab t  " +
				"　where t.freq="+freq+"　and t.language_id="+language_id+"　and  t.start_time='"+start_time+
				"'　and  t.end_time='"+end_time+"'　and t.service_area='"+service_area+"'　and t.power='"+power+"'　and t.direction='"+direction +
				"'　and t.season_id='"+season_id+"'　and t.valid_end_time>sysdate　order by t.runplan_id desc ";
		//GDSet gd= DbComponent.Query(isExistsql);
		StringBuffer addsql=null;
//		if(gd.getRowCount()>0)//说明有，执行更新。
//		{
//			String runplan_id=gd.getString(0, "runplan_id");
//			addsql = new StringBuffer(" update zres_runplan_tab ");
//            addsql.append(" set station_id="+station_id+",station_name='"+station_name+"',transmiter_no='"+transmiter_no+"',freq='"+freq+"',runplan_type_id='"+runplan_type_id+"',direction=");
//            addsql.append("'"+direction+"',antenna='"+antenna+"',antennatype='"+antennatype+"',start_time='"+start_time+"',end_time='"+end_time+"',program_type_id='"+program_type_id+"',program_type='"+program_type+"',language_id=");
//            addsql.append("'"+language_id+"',season_id='"+season_id+"',power='"+power+"',service_area='"+service_area+"',ciraf='"+ciraf+"',satellite_channel='"+satellite_channel+"',remark='"+remark+"',rest_datetime='"+rest_datetime+"',valid_start_time=");
//            addsql.append("'"+valid_start_time+"',valid_end_time='"+valid_end_time+"',store_datetime=sysdate,input_person='"+input_person+"',is_delete=0,mon_area=upper('"+mon_area+"'),xg_mon_area=upper('"+xg_mon_area+"'),weekday='"+weekday+"'");
//            addsql.append(" where runplan_id= "+runplan_id);
//       	 DbComponent.exeUpdate(addsql.toString());
//        	
//		}else //没有插入操作。
		{
			addsql = new StringBuffer("insert into zres_runplan_tab (runplan_id,station_id,station_name,transmiter_no,freq,runplan_type_id,");
            addsql.append("direction,antenna,antennatype,start_time,end_time,program_type_id,program_type,language_id,season_id,power,service_area,");
            addsql.append("ciraf,satellite_channel,remark,rest_datetime,valid_start_time,valid_end_time,store_datetime,input_person,is_delete,mon_area,xg_mon_area,weekday)");
            addsql.append(" values(zres_runplan_seq.nextval,'"+station_id+"','"+station_name+"','"+transmiter_no+"','"+freq+"','"+runplan_type_id+"',");
            addsql.append("'"+direction+"','"+antenna+"','"+antennatype+"','"+start_time+"','"+end_time+"','"+program_type_id+"','"+program_type+"',");
            addsql.append("'"+language_id+"','"+season_id+"','"+power+"','"+service_area+"','"+ciraf+"','"+satellite_channel+"','"+remark+"','"+rest_datetime+"',");
            addsql.append("'"+valid_start_time+"','"+valid_end_time+"',sysdate,'"+input_person+"',0,upper('"+mon_area+"'),upper('"+xg_mon_area+"'),'"+weekday+"')");
       	    DbComponent.exeUpdate(addsql.toString());
            //在插入新的运行图的时候需要判断是否符合同邻频干扰信息，如果符合需要增加一个信息到同邻频干扰信息表中。
        	String isDisturbsql=" select t.runplan_id from zres_runplan_tab t,zdic_language_tab lan where t.station_id="+station_id+
        			" and t.transmiter_no='"+transmiter_no+"' and t.freq='"+freq+"' and lan.language_id= "+language_id+
        			"  and  t.season_id='"+season_id+"' "+
        			" and t.language_id=lan.language_id and t.valid_end_time>sysdate and t.is_delete=0 and t.runplan_type_id=1 " +
        			" and t.runplan_id not in(select dis.runplan_id from zres_runplan_disturb_tab dis where dis.is_delete=0 ) order by t.runplan_id desc " ;
	        GDSet set= DbComponent.Query(isDisturbsql);
	        if(set.getRowCount()>0)//说明有，执行更新。
	        {
//	        	String runplan_id=set.getString(0, "runplan_id");	
//	        	String insertDisturb="insert into zres_runplan_disturb_tab (disrun_id, runplan_id,  station_id,  station_name, sencity_id,  transmiter_no,freq, language," +
//	        			"   start_time,  end_time,  valid_start_time, valid_end_time,  redisseminators, type, disturb,is_delete, receive_station, insert_time)" +
//	        			"  select  zres_runplan_disturb_seq.nextval,run.runplan_id,dis.station_id,dis.station_name,dis.sencity_id,dis.transmiter_no," +
//	        			"  dis.freq,dis.language,dis.start_time,dis.end_time,dis.valid_start_time,dis.valid_end_time,dis.redisseminators, dis.type,dis.disturb,dis.is_delete,run.xg_mon_area,sysdate" +
//	        			"  from zres_runplan_disturb_tab dis,zres_runplan_tab run,zdic_language_tab lang where dis.station_id=run.station_id" +
//	        			"  and dis.transmiter_no=run.transmiter_no and dis.language=lang.language_name and lang.language_id=run.language_id and  run.valid_end_time>sysdate and dis.is_delete=0" +
//	        			"  and run.runplan_type_id=1 and run.runplan_id="+runplan_id;
//	        	DbComponent.exeUpdate(insertDisturb);
	        }
		}
	
		return res;
	}
	/**
	 * 根据季节代号查询有效时间
	 * @param seasonCode
	 * @return
	 */
	public GDSet getTimeBySeason(String seasonCode){
		String sql="select t.start_time,t.end_time from dic_season_tab t where t.code like '%"+seasonCode+"%'";
		GDSet gd = null;
		try {
			gd = DbComponent.Query(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return gd;
	}
}
