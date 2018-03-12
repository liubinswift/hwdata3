package com.viewscenes.web.dataquery.alarminput;

import java.util.ArrayList;

import org.jmask.web.controller.EXEException;

import com.viewscenes.bean.RadioAbnormalBean;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.pub.GDSet;
import com.viewscenes.pub.GDSetException;
import com.viewscenes.util.StringTool;

import flex.messaging.io.amf.ASObject;

public class AlarmInput {
	public Object queryData(RadioAbnormalBean data){
		ArrayList<RadioAbnormalBean> list = null;
		
		
	       
		StringBuffer sql = new StringBuffer(" select * from radio_abnormal_tab t ");
		sql.append(" where 1=1  ");
		if (!StringTool.isNull(data.getSend_county()))
			sql.append(" and t.send_county like '%"+data.getSend_county()+"%' ");
		if (!StringTool.isNull(data.getSend_city()))
			sql.append(" and t.send_city like '%"+data.getSend_city()+"%' ");
		if (!StringTool.isNull(data.getBroadcasting_organizations()))
			sql.append(" and t.broadcasting_organizations  like '%"+data.getBroadcasting_organizations()+"%' ");
		if (!StringTool.isNull(data.getRemote_station()))
			sql.append(" and t.remote_station like '%"+data.getRemote_station()+"%' ");
		if (!StringTool.isNull(data.getCollection_point()))
			sql.append(" and t.collection_point like '%"+data.getCollection_point()+"%' ");
		if (!StringTool.isNull(data.getFrequency()))
			sql.append(" and t.frequency = '"+data.getFrequency()+"' ");
		if (!StringTool.isNull(data.getLanguage_id()))
			sql.append(" and t.language_id = '"+data.getLanguage_id()+"' ");
//		if (!StringTool.isNull(data.getLanguage_name()))
//			sql.append(" and  '"+data.getLanguage_name()+"' ");
		if (!StringTool.isNull(data.getStation_id()))
			sql.append(" and t.station_id = '"+data.getStation_id()+"' ");
//		if (!StringTool.isNull(data.getStation_name()))
//			sql.append(" and  '"+data.getStation_name()+"', ");
		if (!StringTool.isNull(data.getTran_no()))
			sql.append(" and t.tran_no = '"+data.getTran_no()+"' ");
		if (!StringTool.isNull(data.getPower()))
			sql.append(" and t.power = '"+data.getPower()+"' ");
		if (!StringTool.isNull(data.getGet_type()))
			sql.append(" and t.get_type = '"+data.getGet_type()+"' ");
		if (!StringTool.isNull(data.getAbnormal_type()))
			sql.append(" and t.abnormal_type = '"+data.getAbnormal_type()+"' ");
		if (!StringTool.isNull(data.getAbnormal_date())&&!StringTool.isNull(data.getEnd_date())){
			sql.append(" and t.abnormal_date >= to_date('"+data.getAbnormal_date()+"','yyyy-mm-dd') ");
			sql.append(" and t.abnormal_date <= to_date('"+data.getEnd_date()+"','yyyy-mm-dd') ");
		}
//		if (!StringTool.isNull(data.getStarttime()) && !StringTool.isNull(data.getEndtime())){
//				sql.append(" and ( t.starttime >= to_date('"+data.getStarttime()+"','yyyy-mm-dd hh24:mi:ss') ");
//				sql.append(" and t.endtime <= to_date('"+data.getEndtime()+"','yyyy-mm-dd hh24:mi:ss')) ");
//			
//		}else{
//			return new EXEException("", "异态开始时间或结束时间为空\n异态开始时间："+data.getStarttime()+"\n 异态结束时间:"+data.getEndtime(), "异态开始时间："+data.getStarttime()+"\n 异态结束时间:"+data.getEndtime());
//		}
		
		if (!StringTool.isNull(data.getType()))
			sql.append(" and t.type = '"+data.getType()+"' ");
		if (!StringTool.isNull(data.getRemark()))
			sql.append(" and t.remark like '%"+data.getRemark()+"%' ");
//		if (!StringTool.isNull(data.getPlay_time()))
//			sql.append(" '"+data.getPlay_time()+"', ");
		if (!StringTool.isNull(data.getIs_proofread()))
			sql.append(" and t.is_proofread = '"+data.getIs_proofread()+"' ");
		if (!StringTool.isNull(data.getIs_audit()))
			sql.append(" and t.is_audit = '"+data.getIs_audit()+"' ");
		if (!StringTool.isNull(data.getGet_type()))
			sql.append(" and t.get_type = '"+data.getGet_type()+"' ");
		sql.append(" order by abnormal_date desc ");
		
		try {
			GDSet set = DbComponent.Query(sql.toString());
			if (set != null){
				list = new ArrayList<RadioAbnormalBean>();
				for(int i=0;i<set.getRowCount();i++){
					RadioAbnormalBean rabBean = new RadioAbnormalBean();
					rabBean.setId(set.getString(i, "id"));
					rabBean.setSend_county(set.getString(i, "send_county"));
					rabBean.setSend_city(set.getString(i, "send_city"));
					rabBean.setBroadcasting_organizations(set.getString(i, "broadcasting_organizations"));
					rabBean.setRemote_station(set.getString(i, "remote_station"));
					rabBean.setCollection_point(set.getString(i, "collection_point"));
					rabBean.setFrequency(set.getString(i, "frequency"));
					rabBean.setLanguage_id(set.getString(i, "language_id"));
					rabBean.setLanguage_name(set.getString(i, "language_name"));
					rabBean.setStation_id(set.getString(i, "station_id"));
					rabBean.setStation_name(set.getString(i, "station_name"));
					rabBean.setTran_no(set.getString(i, "tran_no"));
					rabBean.setPower(set.getString(i, "power"));
					rabBean.setGet_type(set.getString(i, "get_type"));
					rabBean.setAbnormal_type(set.getString(i, "abnormal_type"));
					rabBean.setAbnormal_date(set.getString(i, "abnormal_date"));
					rabBean.setStarttime(set.getString(i, "starttime"));
					rabBean.setEndtime(set.getString(i, "endtime"));
					rabBean.setType(set.getString(i, "type"));
					rabBean.setRemark(set.getString(i, "remark"));
					rabBean.setPlay_time(set.getString(i, "play_time"));
					rabBean.setIs_proofread(set.getString(i, "is_proofread"));
					rabBean.setIs_audit(set.getString(i, "is_audit"));
				    rabBean.setInsert_person(set.getString(i, "insert_person"));
				    rabBean.setProof_person(set.getString(i, "proof_person"));
				    rabBean.setAudit_person(set.getString(i, "audit_person"));
					list.add(rabBean);
				}
			}
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("", "异态查询错误:"+e.getMessage(), data);
		} catch (GDSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("", "异态查询错误:"+e.getMessage(), data);
		}

		return list;
	}
	
	public Object updateData(RadioAbnormalBean data){
		StringBuffer sql  = new StringBuffer(" update radio_abnormal_tab t ");
		sql.append(" set send_county = '"+data.getSend_county()+"', ");
		sql.append(" send_city = '"+data.getSend_city()+"', ");
		sql.append(" broadcasting_organizations = '"+data.getBroadcasting_organizations()+"', ");
		sql.append(" remote_station = '"+data.getRemote_station()+"', ");
		sql.append(" collection_point = '"+data.getCollection_point()+"', ");
		sql.append(" frequency = '"+data.getFrequency()+"', ");
		sql.append(" language_id = '"+data.getLanguage_id()+"', ");
		sql.append(" language_name = '"+data.getLanguage_name()+"', ");
		sql.append(" station_id = '"+data.getStation_id()+"', ");
		sql.append(" station_name = '"+data.getStation_name()+"', ");
		sql.append(" tran_no = '"+data.getTran_no()+"', ");
		sql.append(" power = '"+data.getPower()+"', ");
		sql.append(" get_type = '"+data.getGet_type()+"', ");
		sql.append(" abnormal_type = '"+data.getAbnormal_type()+"', ");
		sql.append(" abnormal_date = to_date('"+data.getAbnormal_date()+"','yyyy-mm-dd hh24:mi:ss'), ");
		
		
		
		sql.append(" starttime = to_date('"+data.getStarttime()+"','yyyy-mm-dd hh24:mi:ss'), ");
		sql.append(" endtime = to_date('"+data.getEndtime()+"','yyyy-mm-dd hh24:mi:ss'), ");
		
		sql.append(" type = '"+data.getType()+"', ");
		sql.append(" remark = '"+data.getRemark()+"', ");
		sql.append(" play_time = '"+data.getPlay_time()+"', ");
		sql.append(" is_proofread = '"+(data.getIs_proofread().equals("")?"0":data.getIs_proofread())+"', ");
		sql.append(" is_audit = '"+(data.getIs_audit().equals("")?"0":data.getIs_audit())+"' ");
		sql.append(" where id = '"+data.getId()+"' ");
		
		   

		try {
			DbComponent.exeUpdate(sql.toString());
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("", "异态修改失败:"+e.getMessage(), data);
		}    
		
		return "异态修改成功";
	}
	
	public Object addData(RadioAbnormalBean data){
		
		StringBuffer sql = new StringBuffer(" insert into radio_abnormal_tab t ");
		sql.append(" (id,send_county,send_city,broadcasting_organizations,remote_station,collection_point,frequency,language_id,language_name, ");
		sql.append(" station_id,station_name,tran_no,power,get_type,abnormal_type,abnormal_date,starttime,endtime,type,remark,play_time,insert_person,proof_person,audit_person,is_proofread,is_audit) ");
		sql.append(" values ");
		sql.append(" (radio_abnormal_seq.nextval, ");
		sql.append("  '"+data.getSend_county()+"', ");
		sql.append(" '"+data.getSend_city()+"', ");
		sql.append(" '"+data.getBroadcasting_organizations()+"', ");
		sql.append(" '"+data.getRemote_station()+"', ");
		sql.append(" '"+data.getCollection_point()+"', ");
		sql.append(" '"+data.getFrequency()+"', ");
		sql.append(" '"+data.getLanguage_id()+"', ");
		sql.append(" '"+data.getLanguage_name()+"', ");
		sql.append(" '"+data.getStation_id()+"', ");
		sql.append(" '"+data.getStation_name()+"', ");
		sql.append(" '"+data.getTran_no()+"', ");
		sql.append(" '"+data.getPower()+"', ");
		sql.append(" '"+data.getGet_type()+"', ");
		sql.append(" '"+data.getAbnormal_type()+"', ");
		sql.append(" to_date('"+data.getAbnormal_date()+"','yyyy-mm-dd hh24:mi:ss'), ");
		
		if (!StringTool.isNull(data.getStarttime()) && !StringTool.isNull(data.getEndtime())){
			sql.append(" to_date('"+data.getStarttime()+"','yyyy-mm-dd hh24:mi:ss'), ");
			sql.append(" to_date('"+data.getEndtime()+"','yyyy-mm-dd hh24:mi:ss'), ");
			
		}else{
			return new EXEException("", "异态开始时间或结束时间为空\n异态开始时间："+data.getStarttime()+"\n 异态结束时间:"+data.getEndtime(), "异态开始时间："+data.getStarttime()+"\n 异态结束时间:"+data.getEndtime());
		}
		
		sql.append(" '"+data.getType()+"', ");
		sql.append(" '"+data.getRemark()+"', ");
		sql.append(" '"+data.getPlay_time()+"', ");
		sql.append("'").append(data.getInsert_person()).append("',");
		sql.append("'").append(data.getProof_person()).append("',");
		sql.append("'").append(data.getAudit_person()).append("',");
		sql.append(" '"+(data.getIs_proofread().equals("")?"0":data.getIs_proofread())+"', ");
		sql.append("  '"+(data.getIs_audit().equals("")?"0":data.getIs_audit())+"') ");
		
   
		try {
			DbComponent.exeUpdate(sql.toString());
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("", "异态录入错误:"+e.getMessage(), data);
			
		}
		
		return "异态录入成功";
	}
	
	public Object delData(ArrayList<RadioAbnormalBean> list){
		
		if (list != null){
			String[] sqls = new String[list.size()];
			for(int i=0;i<list.size();i++){
				
				RadioAbnormalBean data = list.get(i);
				String sql = " delete from radio_abnormal_tab t where t.id =  "+ data.getId();
				sqls[i] = sql;
			}
			
			try {
				DbComponent.exeBatch(sqls);
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new EXEException("", "异态删除失败:"+e.getMessage(), sqls);
			}
		}
		return "异态删除成功";
	}
	
	/**
	 * 校对
	 * <p>class/function:com.viewscenes.web.dataquery.alarminput
	 * <p>explain:
	 * <p>author-date:谭长伟 2012-12-26
	 * @param:
	 * @return:
	 */
	public Object proofreadData(ASObject obj){
		ArrayList<RadioAbnormalBean> list =(ArrayList<RadioAbnormalBean>)obj.get("selectArr");
		String proof_person=(String)obj.get("proof_person");
		String part="";
		if(proof_person!=null&&proof_person.length()!=0){
			part=" , proof_person = '"+proof_person+"'";
		}
		if (list != null){
			String[] sqls = new String[list.size()];
			for(int i=0;i<list.size();i++){
				
				RadioAbnormalBean data = list.get(i);
				String sql = " update radio_abnormal_tab t set is_proofread  = '1' " +part+
						"where id =  "+ data.getId();
				sqls[i] = sql;
			}
			
			try {
				DbComponent.exeBatch(sqls);
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new EXEException("", "异态校对失败:"+e.getMessage(), sqls);
			}
		}
		return "异态校对成功";
	}
	
	
	/**
	 * 审核
	 * <p>class/function:com.viewscenes.web.dataquery.alarminput
	 * <p>explain:
	 * <p>author-date:谭长伟 2012-12-26
	 * @param:
	 * @return:
	 */
	public Object auditData(ASObject obj){
		ArrayList<RadioAbnormalBean> list =(ArrayList<RadioAbnormalBean>)obj.get("selectArr");
		String audit_person=(String)obj.get("audit_person");
		String part="";
		if(audit_person!=null&&audit_person.length()!=0){
			part=" , audit_person = '"+audit_person+"'";
		}
		if (list != null){
			String[] sqls = new String[list.size()];
			for(int i=0;i<list.size();i++){
				
				RadioAbnormalBean data = list.get(i);
				String sql = " update radio_abnormal_tab t set is_audit  = '1' " +part+
						"where id =  "+ data.getId();
				sqls[i] = sql;
			}
			
			try {
				DbComponent.exeBatch(sqls);
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new EXEException("", "异态审核失败:"+e.getMessage(), sqls);
			}
		}
		return "异态审核成功";
	}
}
