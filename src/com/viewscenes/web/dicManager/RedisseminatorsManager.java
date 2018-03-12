package com.viewscenes.web.dicManager;

import java.util.ArrayList;

import org.jmask.web.controller.EXEException;

import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.pub.GDSet;

import flex.messaging.io.amf.ASObject;

/**ת������ά����
 * @date 2013/01/21
 * @author leeo
 *
 */
public class RedisseminatorsManager {
 /**
  * �����Ϣ
  * @param obj
  * @return
  */
	public Object addRedisseminators(ASObject obj){
		String res="���ת��������Ϣ�ɹ�!";
		String redisseminators = (String) obj.get("redisseminators");
		String country = (String) obj.get("country");
		String sql="insert into dic_redisseminators_tab (id,redisseminators,country)" +
				" values(RES_RESOURSE_SEQ.nextval,'"+redisseminators+"','"+country+"')";
		try {
			DbComponent.exeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return new EXEException("","���ת��������Ϣ�쳣��"+e.getMessage(),"");
		}
		return res;
	}
	/**
	 * ��ѯ��Ϣ
	 * @param obj
	 * @return
	 */
	public Object queryRedisseminators(ASObject obj){
		ArrayList list = new ArrayList();
		String redisseminators = (String) obj.get("redisseminators");
		String country = (String) obj.get("country");
		String sql=" select * from dic_redisseminators_tab where 1=1";
		if(redisseminators!=null&&!redisseminators.equalsIgnoreCase("")){
			sql+=" and redisseminators like '%"+redisseminators+"%'";
		}
		if(country!=null&&!country.equalsIgnoreCase("")){
			sql+=" and country='"+country+"'";
		}
		sql+=" order by  country,redisseminators ";
		try {
			GDSet gd = DbComponent.Query(sql);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					ASObject obj1 = new ASObject();
					obj1.put("id", gd.getString(i, "id")) ;
					obj1.put("redisseminators", gd.getString(i, "redisseminators")) ;
					obj1.put("country", gd.getString(i, "country"));
					obj1.put("isSelected", "false");
					list.add(obj1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	/**
	 * �޸���Ϣ
	 * @return
	 */
	public Object updateRedisseminatorsList(ASObject obj){
		String res="�޸�ת��������Ϣ�ɹ�!";
		ArrayList<ASObject> list=(ArrayList<ASObject>)obj.get("list");
		try {
			for(int i=0;i<list.size();i++){
				updateRedisseminators(list.get(i));
			}
		} catch (DbException e) {
			e.printStackTrace();
			return new EXEException("","�޸�ת��������Ϣ�쳣��"+e.getMessage(),"");
		}
		return res;
	}
	/**
	 * �޸���Ϣ
	 * @return
	 * @throws DbException 
	 */
	public void updateRedisseminators(ASObject obj) throws DbException{
		String id = (String) obj.get("id");
		String redisseminators = (String) obj.get("redisseminators");
		String country = (String) obj.get("country");
		String sql="update dic_redisseminators_tab set redisseminators='"+redisseminators+"',country='"+country+"' where id='"+id+"'";
		DbComponent.exeUpdate(sql.toString()) ;
	
		
	}
	/**
	 * ɾ�����߲�����Ϣ
	 * @param ids
	 * @return
	 */
	public Object delRedisseminators(String ids){
		String message="ɾ��ת��������Ϣ�ɹ�!";
		String sql="delete from dic_redisseminators_tab where id in("+ids+")";
		try {
			DbComponent.exeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return new EXEException("","ɾ��ת��������Ϣ�쳣"+e.getMessage(),"");
		}
		return message;
	}
}
