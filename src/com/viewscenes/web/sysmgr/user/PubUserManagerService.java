package com.viewscenes.web.sysmgr.user;

import java.util.ArrayList;
import java.util.List;

import org.jmask.web.controller.EXEException;

import com.viewscenes.dao.database.DbException;
import com.viewscenes.web.sysmgr.user.PubUserManagerServiceDao;
import com.viewscenes.web.sysmgr.user.PubFuncBean;
import com.viewscenes.web.sysmgr.user.PubRoleBean;
import com.viewscenes.web.sysmgr.user.PubUserBean;
import com.viewscenes.pub.GDSet;
import com.viewscenes.pub.GDSetException;
import com.viewscenes.sys.MD5;
import com.viewscenes.util.LogTool;

import flex.messaging.io.amf.ASObject;

public class PubUserManagerService {

	/**
	 * 
	 * <p>class/function:com.viewscenes.framework.service.pub
	 * <p>explain:��ȡ��ɫ�б�ȡ��ָ����ɫ�봫�� ID,�����ӳ���᷵�ش�����Ϣ
	 * <p>author-date:̷��ΰ 2012-2-28
	 * @param:
	 * @return:
	 */
	public ArrayList<Object> getRoleList(String roleId){
		ArrayList<Object> list = new ArrayList<Object>();
		try {
			GDSet set = PubUserManagerServiceDao.getRoleSet(roleId);
			
			for(int i = 0 ;i < set.getRowCount(); i++){
				PubRoleBean roleBean = new PubRoleBean();
				roleBean.setRole_id(set.getString(i, "role_id"));
				roleBean.setName(set.getString(i, "name"));
				roleBean.setDescription(set.getString(i, "description"));
				roleBean.setPriority(set.getString(i, "priority"));
				list.add(roleBean);
			}
			
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			EXEException ex =  new EXEException("", "error:��ȡ��ɫ�б����,"+e.getMessage(), "");
			list.add(ex);
		} catch (GDSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			EXEException ex =  new EXEException("", "error:��ȡ��ɫ�б����,"+e.getMessage(), "");
			list.add(ex);
		}
		
		return list;
	}
	
	/**
	 * 
	 * <p>class/function:com.viewscenes.framework.service.pub
	 * <p>explain:��ӽ�ɫ�������ӳ���᷵�ش�����Ϣ
	 * <p>author-date:̷��ΰ 2012-2-28
	 * @param:
	 * @return:
	 */
	public ArrayList<Object> addRole(PubRoleBean pubRoleBean){
		ArrayList<Object> list =  new ArrayList<Object>();
		try {
			
			
			String roleName = pubRoleBean.getName();
			
			boolean has = PubUserManagerServiceDao.isExistRole(pubRoleBean);
			
			if (has){
				EXEException ex =  new EXEException("", "error:�ý�ɫ�����Ѵ���", "");
				list.add(ex);
				return list;
			}
			PubUserManagerServiceDao.addRole(pubRoleBean);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			EXEException ex =  new EXEException("", "error:��ӽ�ɫ����["+pubRoleBean.toString()+"]����,"+e.getMessage(), "");
			list.add(ex);
		}
		
		return list;
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
		ArrayList<Object> list =  new ArrayList<Object>();
		
		try {
			
			boolean has = PubUserManagerServiceDao.isExistRole(pubRoleBean);
			
			if (has){
				EXEException ex =  new EXEException("", "error:�ý�ɫ�����Ѵ���", "");
				list.add(ex);
				return list;
			}
			
			PubUserManagerServiceDao.updateRole(pubRoleBean);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			EXEException ex =  new EXEException("", "error:�޸Ľ�ɫ����["+pubRoleBean.toString()+"]����,"+e.getMessage(), "");
			list.add(ex);
		}
		
		return list;
	}
	
	/**
	 * 
	 * <p>class/function:com.viewscenes.framework.service.pub
	 * <p>explain:ɾ����ɫ��Ϣ,���ɾ������᷵�ش�����Ϣ
	 * <p>author-date:̷��ΰ 2012-2-28
	 * @param:
	 * @return:
	 */
	public ArrayList<Object> delRoleById(String roleId){
		ArrayList<Object> list =  new ArrayList<Object>();
		try {
			PubUserManagerServiceDao.delRoleById(roleId);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			EXEException ex =  new EXEException("", "error:ɾ����ɫ����ID="+roleId+"����,"+e.getMessage(), "");
			list.add(ex);
		}
		return list;
	}
	
	/**
	 * 
	 * <p>class/function:com.viewscenes.framework.service.pub
	 * <p>explain:���ݽ�ɫIDȡ���û�����ɫ��Ϣ
	 * <p>author-date:̷��ΰ 2012-2-28
	 * @param:
	 * @return:
	 */
	public ArrayList<Object> getUserListByRoleId(String roleId){
		ArrayList<Object> list =  new ArrayList<Object>();
		try {
			GDSet set = PubUserManagerServiceDao.getUserSetByRoleId(roleId);
			
			for(int i = 0 ;i < set.getRowCount();i++){
				PubUserBean userBean = new PubUserBean();
				userBean.setUserId(set.getString(i, "user_id"));
				userBean.setUserCode(set.getString(i, "user_code"));
				userBean.setUserName(set.getString(i, "user_name"));
				userBean.setSex(set.getString(i, "sex"));
				userBean.setTelephone(set.getString(i, "telephone"));
				userBean.setAddress(set.getString(i, "address"));
				userBean.setEmail(set.getString(i, "email"));
//				userBean.setStart_time(set.getString(i, "start_time"));
//				userBean.setEnd_time(set.getString(i, "end_time"));
				userBean.setDescription(set.getString(i, "description"));
				userBean.setAge(set.getString(i, "age"));
				userBean.setMobilephone(set.getString(i, "mobilephone"));
				userBean.setPost(set.getString(i, "post"));
				userBean.setRoleId(set.getString(i, "role_id"));
				userBean.setRoleName(set.getString(i, "roleName"));
				userBean.setPriority(set.getString(i, "priority"));
				
				list.add(userBean);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			EXEException ex =  new EXEException("", "error:��ȡ��ɫ����ID="+roleId+"���û���Ϣ����,"+e.getMessage(), "");
			list.add(ex);
		}
		return list;
	}
	
	/**
	 * 
	 * <p>class/function:com.viewscenes.framework.service.pub
	 * <p>explain:���ݽ�ɫIDȡ�ý�ɫȨ����Ϣ
	 * <p>author-date:̷��ΰ 2012-2-28
	 * @param:
	 * @return:
	 */
	public ArrayList<Object> getFuncListByRoleId(String roleId){
		ArrayList<Object> list =  new ArrayList<Object>();
		try {
			
			GDSet set = PubUserManagerServiceDao.getFuncSetByRoleId(roleId);
			for(int i = 0 ;i < set.getRowCount();i++){
				PubFuncBean pubFuncBean = new PubFuncBean();
				pubFuncBean.setFunc_id(set.getString(i, "OPERATION_ID"));
//				pubFuncBean.setCode(set.getString(i, "code"));
				pubFuncBean.setName(set.getString(i, "OPERATION_name"));
//				pubFuncBean.setLevels(set.getString(i, "levels"));
//				pubFuncBean.setOrders(set.getString(i, "orders"));
				pubFuncBean.setParent_func_id(set.getString(i, "PARENT_OPERATION_ID"));
//				pubFuncBean.setShow_flag(set.getString(i, "showFlag"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			EXEException ex =  new EXEException("", "error:��ȡ��ɫ����ID="+roleId+"��Ȩ����Ϣ����,"+e.getMessage(), "");
			list.add(ex);
		} 
		return list;
	}
	
	/**
	 * 
	 * <p>class/function:com.viewscenes.framework.service.pub
	 * <p>explain:����û���Ϣ
	 * <p>author-date:̷��ΰ 2012-2-28
	 * @param:
	 * @return:
	 */
	public Object addUser(PubUserBean pubUserBean){
		
		String newid = "";
		try {
			
			boolean has = PubUserManagerServiceDao.isExistUser(pubUserBean);
			
			if (has){
				EXEException ex =  new EXEException("", "error:���û������Ѵ���", "");
//				list.add(ex);
				return ex;
			}
			
			int[] res = PubUserManagerServiceDao.addUser(pubUserBean);
			newid = res[0]+"";
		} catch (Exception e) {
			LogTool.fatal(e);
			EXEException ex =  new EXEException("", "error:����û�["+pubUserBean.toString()+"]����,"+e.getMessage(), "");
//			list.add(ex);
			return ex;
		}
		return newid;
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
		ArrayList<Object> list =  new ArrayList<Object>();
		try {
			
			String userName = pubUserBean.getUserName();
			
			boolean has = PubUserManagerServiceDao.isExistUser(pubUserBean);
			
			if (has){
				EXEException ex =  new EXEException("", "error:���û������Ѵ���", "");
				list.add(ex);
				return list;
			}
			
			PubUserManagerServiceDao.updateUser(pubUserBean);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			EXEException ex =  new EXEException("", "error:�޸��û�["+pubUserBean.toString()+"]����,"+e.getMessage(), "");
			list.add(ex);
		}
		return list;
	}
	
	/**
	 * 
	 * <p>class/function:com.viewscenes.framework.service.pub
	 * <p>explain:ɾ���û�
	 * <p>author-date:̷��ΰ 2012-2-28
	 * @param:
	 * @return:
	 */
	public ArrayList<Object> delUserById(String userId){
		ArrayList<Object> list =  new ArrayList<Object>();
		try {
			PubUserManagerServiceDao.delUserById(userId);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			EXEException ex =  new EXEException("", "error:ɾ���û�ID=["+userId+"]����,"+e.getMessage(), "");
			list.add(ex);
		}
		return list;
	}
	public ArrayList<Object> addUserCustByRoleId(String userId,String roleId){
		ArrayList<Object> list =  new ArrayList<Object>();
			try {
				GDSet set=PubUserManagerServiceDao.getFuncSetByRoleId(roleId);
				List<String> funcList=new ArrayList<String>();
				for(int i = 0 ;i < set.getRowCount();i++){
					funcList.add(set.getString(i, "OPERATION_ID"));
				}
				addUserCust(userId, funcList);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				EXEException ex =  new EXEException("", "error:Ϊ�û�ID=["+userId+"]��Ȩ����,"+e.getMessage(), "");
				list.add(ex);
			}
		return list;
	}
	
	/**
	 * 
	 * <p>class/function:com.viewscenes.framework.service.pub
	 * <p>explain:Ϊ�û����ģ��
	 * <p>author-date:���� 2012-8-19
	 * @param:
	 * @return:
	 */
	public ArrayList<Object> addUserCust(String user_id,List<String> funcList){
		ArrayList<Object> list =  new ArrayList<Object>();
		try {
			PubUserManagerServiceDao.addUserCust(user_id,funcList);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			EXEException ex =  new EXEException("", "error:Ϊ�û�ID=["+user_id+"]��Ȩ����,"+e.getMessage(), "");
			list.add(ex);
		}
		return list;
	}
	
	/**
	 * 
	 * <p>class/function:com.viewscenes.framework.service.pub
	 * <p>explain:��ȡ��ɫȨ����
	 * <p>author-date:̷��ΰ 2012-2-28
	 * @param:
	 * @return:
	 */
	public ArrayList<Object> getCustTreeByUserId(String user_id,String ...funcList){
		ArrayList<Object> list =  new ArrayList<Object>();
		String role_id=null;
		if(funcList!=null&&funcList.length>0){
			role_id=funcList[0];
		}
		try {
			String treeXML = PubUserManagerServiceDao.getCustTreeByUserId(user_id,role_id);
			list.add(treeXML);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
			EXEException ex =  new EXEException("", "error:��ȡ��ɫID="+user_id+"Ȩ���б����,"+e.getMessage(), "");
			list.add(ex);
		}
		
		return list;
	}
	
	/**
	 * 
	 * <p>class/function:com.viewscenes.framework.service.pub
	 * <p>explain:Ϊ��ɫ��Ȩ
	 * <p>author-date:̷��ΰ 2012-2-28
	 * @param:
	 * @return:
	 */
	public ArrayList<Object> grantFuncByRoleId(String roleId,List<String> funcList){
		ArrayList<Object> list =  new ArrayList<Object>();
		try {
			PubUserManagerServiceDao.grantFuncByRoleId(roleId,funcList);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			EXEException ex =  new EXEException("", "error:Ϊ��ɫID=["+roleId+"]��Ȩ����,"+e.getMessage(), "");
			list.add(ex);
		}
		return list;
	}
	
	/**
	 * 
	 * <p>class/function:com.viewscenes.framework.service.pub
	 * <p>explain:��ȡ��ɫȨ����
	 * <p>author-date:̷��ΰ 2012-2-28
	 * @param:
	 * @return:
	 */
	public ArrayList<Object> getFuncTreeByRoleId(String roleId){
		ArrayList<Object> list =  new ArrayList<Object>();
		
		try {
			String treeXML = PubUserManagerServiceDao.getFuncTreeByRoleId(roleId);
			list.add(treeXML);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
			EXEException ex =  new EXEException("", "error:��ȡ��ɫID="+roleId+"Ȩ���б����,"+e.getMessage(), "");
			list.add(ex);
		}
		
		return list;
	}

	/**
	 * <p>class/function:com.viewscenes.framework.service.pub/login
	 * <p>explain:�û���¼��֤
	 * <p>ʱ�䣺2012-3-1
	 * <p>���ߣ�wxb
	 * @param obj
	 * @return
	 */
	public Object login(ASObject obj) {
		// TODO Auto-generated method stub
		
		String userName = (String)obj.get("userName");
		String password = (String)obj.get("password");
		
		try {
			
			PubUserBean user = new PubUserBean();
			
			GDSet gd = PubUserManagerServiceDao.getUserByUserName(userName);
			
			if(gd.getRowCount() <=0 ){
				return new EXEException("","�û�������","");
			}else{
				
				
				
				String pw = gd.getString(0, "user_password");
				
				MD5 md5 = new MD5();
				String md5OfPw = md5.getMD5ofStr(password);
				
				if(md5OfPw.equals(pw)){
					user.setUserId(gd.getString(0, "user_id"));
					user.setUserCode(gd.getString(0, "user_code"));
					user.setUserName(gd.getString(0, "user_name"));
					user.setUserPassword(gd.getString(0, "user_password"));
					user.setAge(gd.getString(0, "age"));
					user.setSex(gd.getString(0, "sex"));
					user.setMobilephone(gd.getString(0, "mobilephone"));
					user.setTelephone(gd.getString(0, "telephone"));
					user.setAddress(gd.getString(0, "address"));
					user.setPost(gd.getString(0, "post"));
					user.setDescription(gd.getString(0, "description"));
					user.setEmail(gd.getString(0, "email"));
					user.setPriority(gd.getString(0, "priority"));
					user.setRoleId(gd.getString(0, "role_id"));
					user.setRoleName(gd.getString(0, "role_name"));
					
				}else{
					return new EXEException("","�������","");
				}
			}
			
			OnlineUserManager.addOnlineUser(user);
			return user;
			
		} catch (DbException e) {
			LogTool.fatal(e);
			return new EXEException("", e.getMessage(), "");
		} catch (Exception e) {
			LogTool.fatal(e);
			return new EXEException("", "��֤�û����ݹ����г���", "");
		}
		
	}

}
