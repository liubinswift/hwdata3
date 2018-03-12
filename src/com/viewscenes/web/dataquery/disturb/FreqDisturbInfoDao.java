package com.viewscenes.web.dataquery.disturb;

import org.jmask.web.controller.EXEException;

import com.viewscenes.bean.runplan.DisRunplanBean;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.pub.GDSet;
import com.viewscenes.util.StringTool;

import flex.messaging.io.amf.ASObject;

/**
 * 
 * 运行图 与数据库交互
 * @author 左手边ジ幸福
 *
 */
public class FreqDisturbInfoDao {
	/**
	 * 查询运行图
	 * @param bean
	 * @return ASObject
	 * @throws Exception
	 */
	public ASObject queryRunplan(DisRunplanBean bean) throws Exception{
		String runplan_id = bean.getRunplan_id();   //运行图id
		String transmiter_no = bean.getTransmiter_no();  //发射机号
		String freq =bean.getFreq();      //频率
		String language_id = bean.getLanguage(); //语言id
		String station_id = bean.getStation_id();    //发射台id
		String start_time = bean.getStart_time(); //播音开始时间
		String end_time = bean.getEnd_time();   //播音结束时间
		String valid_start_time = bean.getValid_start_time(); //启用期
		String valid_end_time = bean.getValid_end_time();  //停用期
		String redisseminators=bean.getRedisseminators();//转播机构
		String disturb=bean.getDisturb();
		String type=bean.getType();
		String receive_station=bean.getReceive_station();
		StringBuffer sqlbuffer =new StringBuffer("select zrt.*,rct.city as sencity ") ;
		sqlbuffer.append(" from zres_runplan_disturb_tab zrt,res_city_tab rct ");
		sqlbuffer.append(" where zrt.is_delete=0  and zrt.sencity_id=rct.id(+) ");
		if(runplan_id!=null&&!runplan_id.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.runplan_id='"+runplan_id+"'");
		}
		if(station_id!=null && !station_id.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.station_id='"+station_id+"'");
		}
		
		if(transmiter_no!=null && !transmiter_no.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.transmiter_no like '%"+transmiter_no+"%'");
		}
		if(freq!=null && !freq.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.freq='"+freq+"'");
		}
		if(language_id!=null && !language_id.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.language='"+language_id+"'");
		}
		if(redisseminators!=null&&redisseminators.length()!=0){
			sqlbuffer.append(" and zrt.redisseminators like '%"+redisseminators+"%'");
		}
		if(type!=null&&type.length()!=0){
			sqlbuffer.append(" and zrt.type="+type);
		}
		if(disturb!=null&&disturb.length()!=0){
			sqlbuffer.append(" and zrt.disturb like '%"+disturb+"%'");
		}
//		if(start_time!=null && !start_time.equalsIgnoreCase("")){
//			sqlbuffer.append(" and zrt.start_time >= to_char('"+start_time+"')");
//		}
//		if(end_time!=null && !end_time.equalsIgnoreCase("")){
//			sqlbuffer.append(" and zrt.end_time <= to_char('"+end_time+"')");
//		}
		if(valid_start_time!=null&&!valid_start_time.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.insert_time >= to_date('"+valid_start_time+"','yyyy-MM-dd HH24:MI:SS')");
		}
		if(valid_end_time!=null&&!valid_end_time.equalsIgnoreCase("")){
			sqlbuffer.append(" and  zrt.insert_time <= to_date('"+valid_end_time+"','yyyy-MM-dd HH24:MI:SS')");
		}
//		if(receive_station!=null&&!receive_station.equalsIgnoreCase("")){
//			
//		}
		sqlbuffer.append("  order by zrt.station_name ");
		ASObject resObj=StringTool.pageQuerySql(sqlbuffer.toString(), bean);
		return resObj;
	}
	/**
	 * 修改运行图
	 * @param bean
	 * @return
	 */
	public Object updateRunplan(DisRunplanBean bean){
		String res="修改干扰信息成功!";
		String runplan_id = bean.getRunplan_id();   //运行图id
		String station_id = bean.getStation_id();    //发射台id
		String sentcity_id = bean.getSencity_id();
		String transmiter_no = bean.getTransmiter_no();  //发射机号
		String freq =bean.getFreq();      //频率
		String language_id = bean.getLanguage(); //语言
		String start_time = bean.getStart_time(); //播音开始时间
		String end_time = bean.getEnd_time();   //播音结束时间
		String valid_start_time = bean.getValid_start_time(); //启用期
		String valid_end_time = bean.getValid_end_time();  //停用期
		String disturb=bean.getDisturb();
		String receive_station=bean.getReceive_station();
		StringBuffer sbf = new StringBuffer("update zres_runplan_disturb_tab set ");
		sbf.append(" station_id='"+station_id+"',sencity_id='"+sentcity_id+"',TRANSMITER_NO='"+transmiter_no+"',FREQ='"+freq+"',");
		sbf.append("START_TIME='"+start_time+"',END_TIME='"+end_time+"',");
		sbf.append("LANGUAGE='"+language_id+"',");
		sbf.append("VALID_START_TIME=to_date('"+valid_start_time+"','yyyy-MM-dd HH24:MI:SS'),VALID_END_TIME=to_date('"+valid_end_time+"','yyyy-MM-dd HH24:MI:SS'),");
		sbf.append("IS_DELETE=0 ,DISTURB= '"+disturb+"',receive_station='"+bean.getReceive_station()+"' where disrun_id="+bean.getDisrun_id());
		try {
			DbComponent.exeUpdate(sbf.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return new EXEException("",e.getMessage(),"");
		}
		return res;
	}
	public Object addRunplan(DisRunplanBean bean){
		StringBuffer sqlBuffer=new StringBuffer();
//		String selectSql="select * from zres_runplan_disturb_tab where runplan_id ="+bean.getRunplan_id();
		sqlBuffer.append("insert into zres_runplan_disturb_tab (disrun_id,runplan_id,station_id,station_name,sencity_id,")
			.append("transmiter_no,freq,language,start_time,end_time,valid_start_time,valid_end_time,redisseminators,type,disturb,is_delete,receive_station,insert_time) values(")
			.append("zres_runplan_disturb_seq.nextval,").append("'"+bean.getRunplan_id()+"',").append(" '"+bean.getStation_id()+"', ").append(" '"+bean.getStation_name()+"', ").append(" '"+bean.getSencity_id()+"', ")
			.append(" '"+bean.getTransmiter_no()+"', ").append(" '"+bean.getFreq()+"', ").append(" '"+bean.getLanguage()+"', ")
			.append(" '"+bean.getStart_time()+"', ").append(" '"+bean.getEnd_time()+"', ").append("to_date('"+bean.getValid_start_time()+"','yyyy-MM-dd HH24:MI:SS'),")
			.append(" to_date('"+bean.getValid_end_time()+"','yyyy-MM-dd HH24:MI:SS'), ").append(" '"+bean.getRedisseminators()+"', ").append(" '"+bean.getType()+"', ").append(" '"+bean.getDisturb()+"',0").append(" ,'"+bean.getReceive_station()+"',sysdate)");
		try {
//			GDSet set=DbComponent.Query(selectSql);
//			if(set.getRowCount()>0){
//				return new EXEException("", "该运行图已添加干扰信息", null);
//			}
			DbComponent.exeUpdate(sqlBuffer.toString());
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("", "资料录入错误:"+e.getMessage(), bean);			
		}
		
		return "资料录入成功";
	}
	public Object deleteDisturb(DisRunplanBean bean){
		String sql="delete  zres_runplan_disturb_tab  where disrun_id ="+bean.getDisrun_id();
		try {
			DbComponent.exeUpdate(sql);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("", "后台错误:"+e.getMessage(), null);
		}
		return "";
	}
}
