package com.viewscenes.web.sysmgr.user;

import java.util.ArrayList;
import java.util.List;

import com.viewscenes.web.sysmgr.user.PubRoleBean;
import com.viewscenes.web.sysmgr.user.PubUserBean;
import com.viewscenes.web.sysmgr.user.PubUserManagerService;

import flex.messaging.io.amf.ASObject;

public class PubUserManager {

	private PubUserManagerService userManagerService = new PubUserManagerService();
	/**
	 * 
	 * <p>class/function:com.viewscenes.framework.service.pub
	 * <p>explain:��ȡ��ɫ�б�ȡ��ָ����ɫ�봫�� ID,�����ӳ���᷵�ش�����Ϣ
	 * <p>author-date:̷��ΰ 2012-2-28
	 * @param:
	 * @return:
	 */
	public ArrayList<Object> getRoleList(ASObject obj){
		String roleId = (String)obj.get("roleId");
		return userManagerService.getRoleList(roleId);
	}
	
	/**
	 * 
	 * <p>class/function:com.viewscenes.framework.service.pub
	 * <p>explain:��ӽ�ɫ�������ӳ���᷵�ش�����Ϣ
	 * <p>author-date:̷��ΰ 2012-2-28
	 * @param:
	 * @return:
	 */
	public ArrayList<Object> addRole(ASObject asobj){
		PubRoleBean pubRoleBean = new PubRoleBean();
		pubRoleBean.setName((String)asobj.get("name"));
		pubRoleBean.setPriority((String)asobj.get("priority"));
		pubRoleBean.setDescription((String)asobj.get("description"));
		return userManagerService.addRole(pubRoleBean);
	}
	
	/**
	 * 
	 * <p>class/function:com.viewscenes.framework.service.pub
	 * <p>explain:�޸Ľ�ɫ��Ϣ������޸ĳ���᷵�ش�����Ϣ
	 * <p>author-date:̷��ΰ 2012-2-28
	 * @param:
	 * @return:
	 */
	public ArrayList<Object> updateRole(PubRoleBean pubRoleBean){
		return userManagerService.updateRole(pubRoleBean);
	}
	
	/**
	 * 
	 * <p>class/function:com.viewscenes.framework.service.pub
	 * <p>explain:ɾ����ɫ��Ϣ,���ɾ������᷵�ش�����Ϣ
	 * <p>author-date:̷��ΰ 2012-2-28
	 * @param:
	 * @return:
	 */
	public ArrayList<Object> delRoleById(ASObject obj){
		String roleId = (String)obj.get("roleId");
		return userManagerService.delRoleById(roleId);
	}
	
	/**
	 * 
	 * <p>class/function:com.viewscenes.framework.service.pub
	 * <p>explain:���ݽ�ɫIDȡ���û�����ɫ��Ϣ
	 * <p>author-date:̷��ΰ 2012-2-28
	 * @param:
	 * @return:
	 */
	public ArrayList<Object> getUserListByRoleId(PubRoleBean obj){
		String roleId = obj.getRole_id();
		
		return userManagerService.getUserListByRoleId(roleId);
	}
	
	/**
	 * 
	 * <p>class/function:com.viewscenes.framework.service.pub
	 * <p>explain:���ݽ�ɫIDȡ�ý�ɫȨ����Ϣ
	 * <p>author-date:̷��ΰ 2012-2-28
	 * @param:
	 * @return:
	 */
	public ArrayList<Object> getFuncListByRoleId(ASObject obj){
		String roleId = (String)obj.get("roleId");
		return userManagerService.getFuncListByRoleId(roleId);
	}
	
	/**
	 * 
	 * <p>class/function:com.viewscenes.framework.service.pub
	 * <p>explain:����û���Ϣ
	 * <p>author-date:̷��ΰ 2012-2-28
	 * @param:
	 * @return:
	 */
	public Object addUser(ASObject asobj){
		PubUserBean pubUserBean = new PubUserBean();
		pubUserBean.setUserCode((String)asobj.get("userCode"));
		pubUserBean.setUserName((String)asobj.get("userName"));
		pubUserBean.setUserPassword((String)asobj.get("password"));
		pubUserBean.setSex((String)asobj.get("sex"));
		pubUserBean.setTelephone((String)asobj.get("telephone"));
		pubUserBean.setAddress((String)asobj.get("address"));
		pubUserBean.setEmail((String)asobj.get("email"));
		pubUserBean.setDescription((String)asobj.get("description"));
		pubUserBean.setPost((String)asobj.get("post"));
		pubUserBean.setTelephone((String)asobj.get("telephone"));
		pubUserBean.setAge((String)asobj.get("age"));
		pubUserBean.setMobilephone((String)asobj.get("mobilephone"));
		pubUserBean.setRoleId((String)asobj.get("role_id"));
		return userManagerService.addUser(pubUserBean);
	}
	/**
	 * ����û�������Ȩ��
	 * @param asobj
	 * @return
	 */
	public Object addUserWithCust(ASObject asobj){
		PubUserBean pubUserBean = new PubUserBean();
		pubUserBean.setUserCode((String)asobj.get("userCode"));
		pubUserBean.setUserName((String)asobj.get("userName"));
		pubUserBean.setUserPassword((String)asobj.get("password"));
		pubUserBean.setSex((String)asobj.get("sex"));
		pubUserBean.setTelephone((String)asobj.get("telephone"));
		pubUserBean.setAddress((String)asobj.get("address"));
		pubUserBean.setEmail((String)asobj.get("email"));
		pubUserBean.setDescription((String)asobj.get("description"));
		pubUserBean.setPost((String)asobj.get("post"));
		pubUserBean.setTelephone((String)asobj.get("telephone"));
		pubUserBean.setAge((String)asobj.get("age"));
		pubUserBean.setMobilephone((String)asobj.get("mobilephone"));
		pubUserBean.setRoleId((String)asobj.get("role_id"));
		
		Object obj= userManagerService.addUser(pubUserBean);
		if(obj instanceof Integer){
			String user_id=obj.toString();
			String role_id=(String)asobj.get("role_id");
			ArrayList<Object> list=userManagerService.addUserCustByRoleId(user_id, role_id);
			if(list.size()>0){
				return list;
			}
		}
		return obj;
	}
	/**
	 * 
	 * <p>class/function:com.viewscenes.framework.service.pub
	 * <p>explain:�޸Ľ�ɫ
	 * <p>author-date:̷��ΰ 2012-2-28
	 * @param:
	 * @return:
	 */
	public ArrayList<Object> updateUser(PubUserBean pubUserBean){
		
		return userManagerService.updateUser(pubUserBean);
		
		
	}
	
	/**
	 * 
	 * <p>class/function:com.viewscenes.framework.service.pub
	 * <p>explain:ɾ���û�
	 * <p>author-date:̷��ΰ 2012-2-28
	 * @param:
	 * @return:
	 */
	public ArrayList<Object> delUserById(ASObject obj){
		String userId = (String)obj.get("userId");
		return userManagerService.delUserById(userId);
	}
	
	

	/**
	 * 
	 * <p>class/function:com.viewscenes.framework.service.pub
	 * <p>explain:Ϊ�û����ģ��
	 * <p>author-date:���� 2012-8-19
	 * @param:
	 * @return:
	 */
	public ArrayList<Object> addUserCust(ASObject asObj){
		String user_id = (String)asObj.get("user_id");
		List<ASObject> listObj = (List<ASObject>)asObj.get("list");
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < listObj.size(); i++) {
			ASObject as = (ASObject)listObj.get(i);
			list.add(as.get("id").toString());
		}
		return userManagerService.addUserCust(user_id,list);
	}
	
	
	/**
	 * 
	 * <p>class/function:com.viewscenes.framework.service.pub
	 * <p>explain:��ȡ�û�ģ��Ȩ����
	 * <p>author-date:̷��ΰ 2012-2-28
	 * @param:
	 * @return:
	 */
	public ArrayList<Object> getCustTreeByUserId(ASObject obj){
		String user_id = (String)obj.get("user_id");
		String role_id=(String) obj.get("role_id");
		return userManagerService.getCustTreeByUserId(user_id,role_id);
	}
	
	/**
	 * 
	 * <p>class/function:com.viewscenes.framework.service.pub
	 * <p>explain:Ϊ��ɫ��Ȩ
	 * <p>author-date:̷��ΰ 2012-2-28
	 * @param:
	 * @return:
	 */
	public ArrayList<Object> grantFuncByRoleId(ASObject asObj){
		String role_id = (String)asObj.get("role_id");
		List<ASObject> listObj = (List<ASObject>)asObj.get("list");
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < listObj.size(); i++) {
			ASObject as = (ASObject)listObj.get(i);
			list.add(as.get("id").toString());
		}
		return userManagerService.grantFuncByRoleId(role_id,list);
	}
	
	/**
	 * 
	 * <p>class/function:com.viewscenes.framework.service.pub
	 * <p>explain:��ȡ��ɫȨ����
	 * <p>author-date:̷��ΰ 2012-2-28
	 * @param:
	 * @return:
	 */
	public ArrayList<Object> getFuncTreeByRoleId(PubRoleBean obj){
		String roleId = obj.getRole_id();
		return userManagerService.getFuncTreeByRoleId(roleId);
	}
	/**
	 * 
	 * <p>class/function:com.viewscenes.framework.manager.appManagement.PubUserManager.java/login
	 * <p>explain:
	 * <p>ʱ�䣺2012-3-6
	 * <p>���ߣ�wxb
	 * @param obj
	 * @return
	 */
	public Object login(ASObject obj){
		return userManagerService.login(obj);
	}
	
}
