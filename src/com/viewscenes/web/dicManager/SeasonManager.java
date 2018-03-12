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
	 * ��ѯ������Ϣ
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
		int aCount=0;//��ӳɹ��ļ���
		int aNum=0;//���ʧ�ܵļ���
		int uCount=0;//�޸ĳɹ��ļ���
		int uNum=0;//�޸�ʧ�ܵļ���
		ArrayList addList = (ArrayList) obj.get("add");
		ArrayList updateList = (ArrayList) obj.get("update");
		try{
		 if(addList.size()>0){
			for(int i=0;i<addList.size();i++){
				SeasonBean bean = (SeasonBean) addList.get(i);
				if( addSeason(bean)) aCount++;
				else aNum++;
			}
			res+="�ɹ����"+aCount+"�����ڴ�����Ϣ,ʧ��"+aNum+"��\r";
		 }
		 if(updateList.size()>0){
			for(int j=0;j<updateList.size();j++){
				SeasonBean bean = (SeasonBean) updateList.get(j);
				if(updateSeasoninfo(bean)) uCount++;
				else uNum++;
			}
			res+="�ɹ��޸�"+uCount+"�����ڴ�����Ϣ,ʧ��"+uNum+"��\r";
		 }
		}catch(Exception e){
			if(e.getMessage().indexOf("Υ��ΨһԼ������")>=0){
				res="���ڴ����ظ���";
			}else
			res=e.getMessage();
		}
		return res;
	}
	
	/**
	 * ���������� 
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
			 is_now=0;//������ڽ���ʱ�����ڵ�ǰʱ����Ϊ��ǰ��is_now��Ϊ0
		}else{//������ڽ���ʱ�����ڵ�ǰʱ����Ϊ��ȥ��is_now��Ϊ1
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
	 * �޸ķ���̨��Ϣ
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
			 is_now=0;//������ڽ���ʱ�����ڵ�ǰʱ����Ϊ��ǰ��is_now��Ϊ0
		}else{//������ڽ���ʱ�����ڵ�ǰʱ����Ϊ��ȥ��is_now��Ϊ1
			is_now=1;
		}
		StringBuffer sql=new StringBuffer("update dic_season_tab set start_time='"+start_time+"',end_time='"+end_time+"',is_now="+is_now+" where code='"+code+"'");
		if(DbComponent.exeUpdate(sql.toString()))
		flag=true;
		else flag=false;
	    return flag;
	}
	/**
	 * ɾ�����ڴ�����Ϣ
	 * @param codes
	 * @return
	 */
	public Object delSeason(String codes){
		String message="ɾ�����ڴ�����Ϣ�ɹ�!";
		String sql="delete  from dic_season_tab where  code in("+codes+")";
		try {
			DbComponent.exeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return new EXEException("","ɾ�����ڴ�����Ϣ�쳣"+e.getMessage(),"");
		}
		return message;
	}
}
