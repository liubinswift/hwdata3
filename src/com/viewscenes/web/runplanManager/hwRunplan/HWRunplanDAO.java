package com.viewscenes.web.runplanManager.hwRunplan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.viewscenes.bean.runplan.GJTLDRunplanBean;
import com.viewscenes.bean.runplan.GJTRunplanBean;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.pub.GDSet;
import com.viewscenes.util.StringTool;
import com.viewscenes.web.common.Common;

import flex.messaging.io.amf.ASObject;

public class HWRunplanDAO {
	
	
	/**
	 * 运行图添加入库
	 * @param bean
	 * @return
	 * @throws DbException
	 */
	public String addRunplan(GJTLDRunplanBean bean) throws DbException{
		String res="添加运行图成功!";
		String input_person = bean.getInput_person();//录入人
		String runplan_type_id = bean.getRunplan_type_id(); //运行图类型id
		String launch_country = bean.getLaunch_country();    //发射国家
		String sentcity_id = bean.getSentcity_id();  //发射城市id
		String redisseminators = bean.getRedisseminators();  //转播机构
		String freq =bean.getFreq();      //频率
		String band =bean.getBand();      //波段
		String language_id = bean.getLanguage_id();//语言id
		String direction = bean.getDirection(); //方向
		
		String power = bean.getPower();  //功率
		String service_area = bean.getService_area(); //服务区
		//String local_time = bean.getLocal_time();//当地时间
		String rest_datetime = bean.getRest_datetime(); // //休息日期
	    String summer = bean.getSummer();//夏令时
//	    String summer_starttime = bean.getSummer_starttime();//夏令时开始时间
//	    String summer_endtime = bean.getSummer_endtime();//夏令时结束时间
		String start_time = bean.getStart_time(); //播音开始时间
		String end_time = bean.getEnd_time();   //播音结束时间
		String mon_area = bean.getMon_area();    //质量收测站点
		String xg_mon_area = bean.getXg_mon_area();//效果收测站点
		String valid_start_time = bean.getValid_start_time(); //启用期
		String valid_end_time = bean.getValid_end_time();  //停用期
		String remark = bean.getRemark();//备注
		String weekday = bean.getWeekday();//周设置
		String[] times = Common.getTimeDif(start_time,end_time,launch_country);//根据时差计算当地时间
		StringBuffer addsql = new StringBuffer("insert into zres_runplan_tab (runplan_id,launch_country,sentcity_id,redisseminators,freq,band,runplan_type_id,");
		             addsql.append("language_id,direction,start_time,end_time,power,service_area,local_start_time,local_end_time,rest_datetime,weekday,");
		             addsql.append("summer,remark,valid_start_time,valid_end_time,store_datetime,input_person,is_delete,mon_area,xg_mon_area)");
		             addsql.append(" values(zres_runplan_seq.nextval,'"+launch_country+"','"+sentcity_id+"','"+redisseminators+"','"+freq+"','"+band+"','"+runplan_type_id+"',");
		             addsql.append("'"+language_id+"','"+direction+"','"+start_time+"','"+end_time+"','"+power+"','"+service_area+"','"+times[0]+"','"+times[1]+"','"+rest_datetime+"','"+weekday+"',");
		             addsql.append("'"+summer+"','"+remark+"',");
		             addsql.append("'"+valid_start_time+"','"+valid_end_time+"',sysdate,'"+input_person+"',0,'"+mon_area+"','"+xg_mon_area+"')");
		 DbComponent.exeUpdate(addsql.toString());
		return res;
	}

	/**
	 *  查询运行图
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public ASObject queryRunplan(GJTLDRunplanBean bean) throws Exception{
		
		String runplan_id = bean.getRunplan_id();//运行图id
		String runplan_type_id = bean.getRunplan_type_id();//运行图类型id
		String launch_country = bean.getLaunch_country();//发射国家
		String sentcity_id = bean.getSentcity_id();//发射城市id
		String redisseminators = bean.getRedisseminators();//转播机构
		String freq =bean.getFreq();      //频率
		String language_id = bean.getLanguage_id();//语言id
		String service_area = bean.getService_area(); //服务区
		String start_time = bean.getStart_time();
		String end_time = bean.getEnd_time();
		String mon_area = bean.getMon_area();
		String xg_mon_area = bean.getXg_mon_area();
		String summer = bean.getSummer();
		String valid_start_time = bean.getValid_start_time(); //启用期
		String valid_end_time = bean.getValid_end_time();  //停用期
	    StringBuffer sqlbuffer=new StringBuffer("select zrt.*,zlt.language_name language,rct.city as sentcity ") ;
		sqlbuffer.append(" from zres_runplan_tab zrt,zdic_language_tab zlt,res_city_tab rct ");
		sqlbuffer.append(" where   zrt.is_delete=0 and zrt.language_id=zlt.language_id and zrt.sentcity_id=rct.id  ");
		if(!runplan_id.equalsIgnoreCase("")&&runplan_id!=null){
			sqlbuffer.append(" and zrt.runplan_id='"+runplan_id+"'");
		}
		if(runplan_type_id!=null && !runplan_type_id.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.runplan_type_id='"+runplan_type_id+"'");
		}
		if(launch_country!=null&&!launch_country.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.launch_country='"+launch_country+"'");
		}
		if(sentcity_id!=null && !sentcity_id.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.sentcity_id='"+sentcity_id+"'");
		}
		if(redisseminators!=null && !redisseminators.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.redisseminators='"+redisseminators+"'");
		}
		if(freq!=null && !freq.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.freq='"+freq+"'");
		}
		if(language_id!=null && !language_id.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.language_id="+language_id+"");
		}
		if(service_area!=null && !service_area.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.service_area='"+service_area+"'");
		}
		if(start_time!=null && !start_time.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.start_time=to_char('"+start_time+"')");
		}
		if(end_time!=null && !end_time.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.end_time=to_char('"+end_time+"')");
		}
	
		if(valid_start_time!=null && !valid_start_time.equalsIgnoreCase("")){
			sqlbuffer.append(" and (zrt.valid_start_time>=to_date('"+valid_start_time+"','yyyy-MM-dd HH24:MI:SS')");
			if(valid_end_time!=null && !valid_end_time.equalsIgnoreCase("")){
				sqlbuffer.append(" and zrt.valid_start_time<=to_date('"+valid_end_time+"','yyyy-MM-dd HH24:MI:SS'))"); 
			 }else
			 {
				 sqlbuffer.append(")"); 
			 }
		}
		if(valid_end_time!=null && !valid_end_time.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.valid_end_time>=to_date('"+valid_end_time+"','yyyy-MM-dd HH24:MI:SS')");
		}
//		//新要求，田雅静提出如果开始时间和结束时间都为空，需要让有效期开始时间小于当前时间。
//		if((valid_start_time==null ||valid_start_time.equalsIgnoreCase(""))&&(valid_end_time==null ||valid_end_time.equalsIgnoreCase(""))){
//			sqlbuffer.append(" and zrt.valid_start_time<=sysdate ");	
//		}
			if(summer!=null && !summer.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.summer like '%"+summer+"%'");
		}
		if(mon_area!=null && !mon_area.equalsIgnoreCase("")){
			if(xg_mon_area.indexOf(",")>0){
				String[] s=xg_mon_area.split(",");
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

		sqlbuffer.append(" order by zrt.xg_mon_area,zrt.mon_area,zrt.redisseminators ");
		ASObject resObj=StringTool.pageQuerySql(sqlbuffer.toString(), bean);
		return resObj;
	}

}
