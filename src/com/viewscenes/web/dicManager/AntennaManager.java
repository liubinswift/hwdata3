package com.viewscenes.web.dicManager;

import java.util.ArrayList;

import org.jmask.web.controller.EXEException;

import com.viewscenes.bean.dicManager.AntennaBean;
import com.viewscenes.bean.runplan.GJTRunplanBean;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.util.StringTool;

import flex.messaging.io.amf.ASObject;
/**
 * ���߲�����ά����
 * @author leeo
 *
 */
public class AntennaManager {
	
	/**
	 * ������߲�����Ϣ
	 * @param bean
	 * @return
	 */
	public Object addAntenna(AntennaBean bean){
		String res="������߲�����Ϣ�ɹ�!";
		String station_name = bean.getStation_name();
		String antenna_no = bean.getAntenna_no();
		String antenna_mode = bean.getAntenna_mode();
		String itu = bean.getItu();
		String direction = bean.getDirection();
		String service_area = bean.getService_area();
		String ciraf = bean.getCiraf();
		String address = bean.getAddress();
		String shiypd = bean.getShiypd();
		String remark = bean.getRemark();
		String sql="insert into res_antenna_tab (id,station_name,antenna_no,antenna_mode,itu,direction,service_area,address,ciraf,shiypd,remark)" +
				" values(RES_RESOURSE_SEQ.nextval,'"+station_name+"','"+antenna_no+"','"+antenna_mode+"','"+itu+"','"+direction+"','"+service_area+"'," +
				"'"+address+"','"+ciraf+"','"+shiypd+"','"+remark+"')";
		try {
			DbComponent.exeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return new EXEException("","������߲�����Ϣ�쳣��"+e.getMessage(),"");
		}
		
		return res;
	}
	/**
	 * ��ѯ��Ϣ
	 * @param bean
	 * @return
	 */
	public Object queryAntenna(AntennaBean bean){
		String station_name = bean.getStation_name();
		String antenna_no = bean.getAntenna_no();
		String antenna_mode = bean.getAntenna_mode();
		String itu = bean.getItu();
		String direction = bean.getDirection();
		String service_area = bean.getService_area();
		ASObject resObj=null;
		ArrayList result;
		String sql="select * from res_antenna_tab t where 1=1 ";
		if(station_name!=null&&!station_name.equalsIgnoreCase("ȫ��")){
			sql+=" and station_name='"+station_name+"'";
		}
		if(antenna_no!=null&&!antenna_no.equalsIgnoreCase("")){
			sql+=" and antenna_no='"+antenna_no+"'";
		}
		if(antenna_mode!=null&&!antenna_mode.equalsIgnoreCase("")){
			sql+=" and antenna_mode='"+antenna_mode+"'";
		}
		if(itu!=null&&!itu.equalsIgnoreCase("")){
			sql+=" and itu='"+itu+"'";
		}
		if(direction!=null&&!direction.equalsIgnoreCase("")){
			sql+=" and direction='"+direction+"'";
		}
		if(service_area!=null&&!service_area.equalsIgnoreCase("")){
			sql+=" and service_area like '%"+service_area+"%'";
		}
		sql+=" order by id desc ";
		try {
			 resObj=StringTool.pageQuerySql(sql.toString(), bean);
			 ArrayList list =(ArrayList) resObj.get("resultList") ;//ȡ������Ľ����
			 String classpath = AntennaBean.class.getName();
			 result = StringTool.convertFlexToJavaList(list, classpath);//����������ת����bean���ٷŻؽ�����С�
			 resObj.put("resultList", result);
		} catch (Exception e) {
			e.printStackTrace();
			return new EXEException("","��ѯ���߲�����Ϣ�쳣��"+e.getMessage(),"");
		}
		return resObj;
	}
	/**
	 * �޸����߲�����Ϣ
	 * @param bean
	 * @return
	 */
	public Object updateAntenna(AntennaBean bean){
		String res="�޸����߲�����Ϣ�ɹ�!";
		String id = bean.getId();
		String station_name = bean.getStation_name();
		String antenna_no = bean.getAntenna_no();
		String antenna_mode = bean.getAntenna_mode();
		String itu = bean.getItu();
		String direction = bean.getDirection();
		String service_area = bean.getService_area();
		String ciraf = bean.getCiraf();
		String address = bean.getAddress();
		String shiypd = bean.getShiypd();
		String remark = bean.getRemark();
		StringBuffer sbf = new StringBuffer(" update res_antenna_tab set station_name='"+station_name+"',antenna_no='"+antenna_no+"',");
		             sbf.append(" antenna_mode='"+antenna_mode+"',itu='"+itu+"',direction='"+direction+"',service_area='"+service_area+"',");
		             sbf.append("ciraf='"+ciraf+"',address='"+address+"',shiypd='"+shiypd+"',remark='"+remark+"' where id='"+id+"'");
		try {
			DbComponent.exeUpdate(sbf.toString()) ;
		} catch (Exception e) {
			e.printStackTrace();
			return new EXEException("","�޸����߲�������Ϣ�쳣��"+e.getMessage(),"");
		}          
		return res;
	}
	
	/**
	 * ɾ�����߲�����Ϣ
	 * @param ids
	 * @return
	 */
	public Object delAntenna(String ids){
		String message="ɾ�����߲�����Ϣ�ɹ�!";
		String sql="delete from res_antenna_tab where id in("+ids+")";
		try {
			DbComponent.exeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return new EXEException("","ɾ�����߲�����Ϣ�쳣"+e.getMessage(),"");
		}
		return message;
	}

}
