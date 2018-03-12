package com.viewscenes.web.dicManager;

import java.util.ArrayList;
import java.util.Date;

import org.jmask.web.controller.EXEException;

import com.viewscenes.bean.SeasonBean;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.util.StringTool;
import com.viewscenes.util.TimeMethod;

import flex.messaging.io.amf.ASObject;

public class SeasonManager {
	/**
	 * 查询季节信息
	 * @param bean
	 * @return
	 */
	public Object querySeason(SeasonBean bean){
		ASObject resObj=null;
		String code = bean.getCode();
		String sql="select * from dic_season_tab where 1=1";
		if(code!=null&&!code.equalsIgnoreCase("")){
			sql+=" and code like '%"+code+"%'";
		}
		sql+=" order by code";
		try {
			resObj = StringTool.pageQuerySql(sql, bean);
		} catch (Exception e) {
			e.printStackTrace();
			return new EXEException("",e.getMessage(),"");
		}
		return resObj;
	}
	
	public String updateSeason(ASObject obj){
		String res="";
		int aCount=0;//添加成功的计数
		int aNum=0;//添加失败的计数
		int uCount=0;//修改成功的计数
		int uNum=0;//修改失败的计数
		ArrayList addList = (ArrayList) obj.get("add");
		ArrayList updateList = (ArrayList) obj.get("update");
		try{
		 if(addList.size()>0){
			for(int i=0;i<addList.size();i++){
				SeasonBean bean = (SeasonBean) addList.get(i);
				if( addSeason(bean)) aCount++;
				else aNum++;
			}
			res+="成功添加"+aCount+"条季节代号信息,失败"+aNum+"条\r";
		 }
		 if(updateList.size()>0){
			for(int j=0;j<updateList.size();j++){
				SeasonBean bean = (SeasonBean) updateList.get(j);
				if(updateSeasoninfo(bean)) uCount++;
				else uNum++;
			}
			res+="成功修改"+uCount+"条季节代号信息,失败"+uNum+"条\r";
		 }
		}catch(Exception e){
			if(e.getMessage().indexOf("违反唯一约束条件")>=0){
				res="季节代号重复！";
			}else
			res=e.getMessage();
		}
		return res;
	}
	
	/**
	 * 添加数据入库 
	 * @param bean
	 * @return
	 * @throws Exception 
	 */
	public Boolean addSeason(SeasonBean bean) throws Exception{
		Boolean flag=false;
		String code = bean.getCode();
		String start_time = bean.getStart_time();
		String end_time = bean.getEnd_time();
		int is_now=0;
		if(TimeMethod.compare_date(end_time, StringTool.date2String(new Date()))){
			 is_now=0;//如果季节结束时间晚于当前时间视为当前将is_now置为0
		}else{//如果季节结束时间早于当前时间视为过去将is_now置为1
			is_now=1;
		}
		StringBuffer sql=new StringBuffer("insert into dic_season_tab (code,start_time,end_time,is_now)");
		             sql.append(" values('"+code+"','"+start_time+"','"+end_time+"',"+is_now+")");
		if(DbComponent.exeUpdate(sql.toString()))
			flag=true;
		else flag=false;
		return flag;
	}
	/**
	 * 修改发射台信息
	 * @param bean
	 * @return
	 * @throws Exception 
	 */
	public Boolean updateSeasoninfo(SeasonBean bean) throws Exception{
		Boolean flag=false;
		String code = bean.getCode();
		String start_time = bean.getStart_time();
		String end_time = bean.getEnd_time();
		int is_now=0;
		if(TimeMethod.compare_date(end_time, StringTool.date2String(new Date()))){
			 is_now=0;//如果季节结束时间晚于当前时间视为当前将is_now置为0
		}else{//如果季节结束时间早于当前时间视为过去将is_now置为1
			is_now=1;
		}
		StringBuffer sql=new StringBuffer("update dic_season_tab set start_time='"+start_time+"',end_time='"+end_time+"',is_now="+is_now+" where code='"+code+"'");
		if(DbComponent.exeUpdate(sql.toString()))
		flag=true;
		else flag=false;
	    return flag;
	}
	/**
	 * 删除季节代号信息
	 * @param codes
	 * @return
	 */
	public Object delSeason(String codes){
		String message="删除季节代号信息成功!";
		String sql="delete  from dic_season_tab where  code in("+codes+")";
		try {
			DbComponent.exeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return new EXEException("","删除季节代号信息异常"+e.getMessage(),"");
		}
		return message;
	}
}
