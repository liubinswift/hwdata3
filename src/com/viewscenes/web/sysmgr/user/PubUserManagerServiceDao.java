package com.viewscenes.web.sysmgr.user;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.web.sysmgr.user.PubRoleBean;
import com.viewscenes.web.sysmgr.user.PubUserBean;
import com.viewscenes.pub.GDSet;
import com.viewscenes.pub.GDSetException;
import com.viewscenes.sys.MD5;
import com.viewscenes.util.StringTool;

public class PubUserManagerServiceDao {
	
private static MD5 md5;
	
	static{
		md5 = new MD5();
	}

	/**
	 * <p>class/function:com.viewscenes.database.service.pub
	 * <p>explain:��ѯ��ɫ�б���Ϣ,roleΪ�ղ�ѯ����,��Ϊ�ղ�ѯָ����ɫ
	 * <p>author-date:����2012-7-27
	 * @param:
	 * @return:
	 * @throws DbException 
	 */
	public static GDSet getRoleSet(String roleId) throws DbException{

		GDSet gdSet = null;
		
		StringBuffer sql = new StringBuffer(" select * from sec_role_tab t  ");

		if (roleId != null && roleId.equals(""))
			
			sql.append("  where t.role_id = "+roleId+" ");
		
		sql.append("  order by name asc ");
			
		gdSet = DbComponent.Query(sql.toString());
			
		return gdSet;

	}
	
	/**
	 * <p>class/function:com.viewscenes.database.service.pub
	 * <p>explain:���һ����ɫ,��ӳɹ�����true,���򷵻�false
	 * <p>author-date:����2012-7-27
	 * @param:
	 * @return:
	 * @throws DbException 
	 */
	public static boolean addRole(PubRoleBean pubRoleBean) throws DbException{
		
		boolean ret = false;
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" insert into sec_role_tab(role_id, name, description, priority) ");
		
		sql.append(" values (SEC_SEQ.nextval, '"+pubRoleBean.getName()+"', '"+pubRoleBean.getDescription()+"', '"+pubRoleBean.getPriority()+"') ");
		
		ret = DbComponent.exeUpdate(sql.toString());
		
		return ret;  
	}
	
	/**
	 * <p>class/function:com.viewscenes.database.service.pub
	 * <p>explain:�޸Ľ�ɫ��Ϣ,�ɹ�����true,���򷵻�false
	 * <p>author-date:����2012-7-27
	 * @param:
	 * @return:
	 * @throws DbException 
	 */
	public static boolean updateRole(PubRoleBean pubRoleBean) throws DbException{
		
		boolean ret = false;
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" update sec_role_tab ");
		
		sql.append(" set name        = '"+pubRoleBean.getName()+"', ");
		
		sql.append(" description = '"+pubRoleBean.getDescription()+"', ");
		
		sql.append(" priority    = '"+pubRoleBean.getPriority()+"' ");
		
		sql.append(" where role_id = "+pubRoleBean.getRole_id()+" ");
	
		ret = DbComponent.exeUpdate(sql.toString());
		       
		return ret;      
		       
		 
	}
	
	/**
	 * <p>class/function:com.viewscenes.database.service.pub
	 * <p>explain:ɾ��һ��ָ��ID�Ľ�ɫ
	 * <p>author-date:����2012-7-27
	 * @param:
	 * @return:
	 * @throws DbException 
	 */
	public static int[] delRoleById(String roleId) throws DbException{
		
		int ret[];
		
		StringBuffer sqlR = new StringBuffer();
		StringBuffer sqlRU = new StringBuffer();
		StringBuffer sqlRF = new StringBuffer();
		StringBuffer sqlU = new StringBuffer();
		
		//1.ɾ����ɫ
		sqlR.append(" delete from  sec_role_tab  where role_id = "+roleId+" ");
		
		//2.ɾ���û�
		sqlU.append(" delete from sec_user_tab u where u.user_id in (select user_id from sec_user_role_rel_tab rel where role_id = "+roleId+") ");
		
		//3.ɾ�����û��Ĺ�����ϵ
		sqlRU.append(" delete from  sec_user_role_rel_tab where role_id = "+roleId+" ");
		
		//4.ɾ����Ȩ�޵Ĺ�����ϵ
		sqlRF.append(" delete from sec_role_operation_rel_tab  where role_id = "+roleId+" ");
		
		//ע��ɾ��˳��2������3�ߵ�
		
		String[] sqls = new String[4];
		sqls[0] = sqlR.toString();
		sqls[1] = sqlU.toString();
		sqls[2] = sqlRU.toString();
		sqls[3] = sqlRF.toString();

		ret = DbComponent.exeBatch(sqls);
		
		return ret;
		
	}
	
	/**
	 * <p>class/function:com.viewscenes.database.service.pub
	 * <p>explain:��ѯָ���Ľ�ɫ�Ƿ����
	 * <p>author-date:����2012-7-27
	 * @param:
	 * @return:
	 * @throws DbException 
	 * @throws GDSetException 
	 */
	public static boolean isExistRole(PubRoleBean roleBean) throws DbException, GDSetException{
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select * from sec_role_tab t where t.name = '"+roleBean.getName()+"' ");
		
		GDSet set = null;
		
		set = DbComponent.Query(sql.toString());
		
		String role_id = "";
		
		if (set.getRowCount()>0){
			
			role_id = set.getString(0, "role_id");
			
			return role_id.equals(roleBean.getRole_id())?false:true;
			
		}else{
			return false;
		}
		
	}
	
	
	
	
	
	/**
	 * <p>class/function:com.viewscenes.database.service.pub
	 * <p>explain:���ݽ�ɫID��ѯ�û��б�
	 * <p>author-date:����2012-7-27
	 * @param:
	 * @return:
	 * @throws DbException 
	 */
	public static GDSet getUserSetByRoleId(String roleId) throws DbException {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select u.*,r.role_id,r.name roleName,r.priority ");
		
		sql.append(" from sec_user_tab  u, sec_user_role_rel_tab rel,sec_role_tab r ");
		
		sql.append(" where rel.user_id = u.user_id ");
		
		sql.append(" and rel.role_id = "+roleId+" ");
		
		sql.append(" and rel.role_id = r.role_id ");
			
		GDSet gdSet = null;
	
		gdSet = DbComponent.Query(sql.toString());
			
		
		return gdSet;
		
	}
	/**
	 * ��������Աֱ��ȡ��ȫ��Ȩ��
	 * @detail  
	 * @method  
	 * @param broadcast_type
	 * @return
	 * @throws DbException 
	 * @return  GDSet  
	 * @author  zhaoyahui
	 * @version 2013-3-14 ����10:38:45
	 */
	public static GDSet getFuncSetByAdmin(String... broadcast_type) throws DbException{
		
		StringBuffer sql = new StringBuffer();
		String typeid = "0";
		if(broadcast_type.length>0){
			typeid = broadcast_type[0];
        }
		sql.append(" select f.* from sec_operation_tab f ");
		sql.append(" where f.broadcast_type = "+typeid  );
		sql.append(" and f.show_flag = 1"  );
		sql.append(" order by f.OPERATION_ID");
		
		GDSet gdSet = null;
		
		gdSet = DbComponent.Query(sql.toString());
		
		return gdSet;
 
	}
	/**
	 * <p>class/function:com.viewscenes.database.service.pub
	 * <p>explain:����ָ����ɫID��Ȩ���б�
	 * <p>author-date:����2012-7-27
	 * @param:
	 * @return:
	 * @throws DbException 
	 */
	public static GDSet getFuncSetByRoleId(String roleId,String... broadcast_type) throws DbException{
	
		StringBuffer sql = new StringBuffer();
		String typeid = "1";
		if(broadcast_type.length>0){
			typeid = broadcast_type[0];
        }
		sql.append(" select f.* from sec_operation_tab f,sec_role_operation_rel_tab rf,sec_role_tab r ");
		sql.append(" where f.OPERATION_ID = rf.OPERATION_ID ");
		sql.append(" and rf.role_id = r.role_id ");
		sql.append(" and f.broadcast_type = "+typeid  );
		sql.append(" and f.show_flag = 1"  );
		sql.append(" and r.role_id = "+roleId+"  order by f.OPERATION_ID");
		
		GDSet gdSet = null;
		
		gdSet = DbComponent.Query(sql.toString());
		
		return gdSet;
 
	}
	
	
	/**
	 * <p>class/function:com.viewscenes.database.service.pub
	 * <p>explain:����ָ����ɫID��Ȩ���б�
	 * <p>author-date:����2012-7-27
	 * @param:
	 * @return:
	 * @throws DbException 
	 */
	public static GDSet getCustByUserId(String user_id) throws DbException{
	
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select f.* from sec_operation_tab f,sec_user_operation_custom_tab sf,sec_user_tab s ");
		sql.append(" where f.OPERATION_ID = sf.OPERATION_ID ");
		sql.append(" and sf.user_id = s.user_id ");
		sql.append(" and s.user_id = "+user_id+"  order by f.OPERATION_ID");
		
		GDSet gdSet = null;
		
		gdSet = DbComponent.Query(sql.toString());
		
		return gdSet;
 
	}
	
	/**
	 * 
	 * <p>class/function:com.viewscenes.database.service.usermanager
	 * <p>explain:ȡ������û�ʱ�û�������
	 * <p>author-date:����2012-7-23
	 * @param:
	 * @return:
	 */
	private static String getUserNextVal() throws DbException, GDSetException{
		
		String sql = " select SEC_SEQ.nextval val from dual ";
		
		GDSet set = DbComponent.Query(sql);
		
		String val = set.getString(0, "val");
		
		return val;
	}
	
	/**
	 * <p>class/function:com.viewscenes.database.service.pub
	 * <p>explain:���һ���û�������û���ӳɹ�����true,���򷵻�false;
	 * <p>author-date:����2012-7-27
	 * @param:
	 * @return:����û��Ƿ�ɹ�
	 * @throws DbException 
	 */
	public static int[] addUser(PubUserBean pubUserBean) throws Exception{
		
		int[] ret ;
		
		
		String val = getUserNextVal();
		
		StringBuffer sql = new StringBuffer();
		
		StringBuffer sqlUR = new StringBuffer();
		
		sqlUR.append(" insert into sec_user_role_rel_tab(user_id,role_id) values("+val+","+pubUserBean.getRoleId()+")");
		
		sql.append(" insert into sec_user_tab(user_id, user_code, user_name, user_password, sex, telephone, address, email, description, age, mobilephone, post) ");
		// start_time, end_time, priority
		sql.append(" values("+val+", '"+pubUserBean.getUserCode()+"', '"+pubUserBean.getUserName()+"', '"+md5.getMD5ofStr(pubUserBean.getUserPassword())+"', "+pubUserBean.getSex()+", '"+pubUserBean.getTelephone()+"'," +
				" '"+pubUserBean.getAddress()+"', '"+pubUserBean.getEmail()+"', '"+pubUserBean.getDescription()+"', '"+pubUserBean.getAge()+"', '"+pubUserBean.getMobilephone()+"', '"+pubUserBean.getPost()+"') ");		
		
		//, to_date('"+pubUserBean.getStart_time()+"','yyyy-mm-dd hh24:mi:ss'), to_date('"+pubUserBean.getEnd_time()+"','yyyy-mm-dd HH24:mi:ss'), '"+pubUserBean.getPriority()+"'
		String[] sqls = new String[2];
		
		sqls[0] = sqlUR.toString();
		sqls[1] = sql.toString();
		
		ret = DbComponent.exeBatch(sqls);
		int[] res = new int[1];
		res[0] = Integer.parseInt(val);
		return res;
		  
		
		  

	}
	
	/**
	 * <p>class/function:com.viewscenes.database.service.pub
	 * <p>explain:�޸�һ���û���Ϣ������û��޸ĳɹ�����true,���򷵻�false
	 * <p>author-date:����2012-7-27
	 * @param:
	 * @return:�޸��û��Ƿ�ɹ�
	 * @throws DbException 
	 */
	public static boolean updateUser(PubUserBean pubUserBean) throws DbException{
		
		boolean ret = false;
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" update sec_user_tab ");
		sql.append(" set user_code = '"+pubUserBean.getUserCode()+"', ");
		sql.append(" user_name = '"+pubUserBean.getUserName()+"', ");
		if(pubUserBean.getUserPassword()!=""&&pubUserBean.getUserPassword().length()!=0){
			sql.append(" user_password = '"+md5.getMD5ofStr(pubUserBean.getUserPassword())+"', ");
		}
		sql.append(" sex = "+pubUserBean.getSex()+", ");
		sql.append(" telephone = '"+pubUserBean.getTelephone()+"', ");
		sql.append(" address = '"+pubUserBean.getAddress()+"', ");
		sql.append(" email = '"+pubUserBean.getEmail()+"', ");
//		sql.append(" start_time = to_date('"+pubUserBean.getStart_time()+"','yyyy-mm-dd hh24:mi:ss'), ");
//		sql.append(" end_time = to_date('"+pubUserBean.getEnd_time()+"','yyyy-mm-dd hh24:mi:ss'), ");
		sql.append(" description = '"+pubUserBean.getDescription()+"', ");
		sql.append(" age = '"+pubUserBean.getAge()+"', ");
		sql.append(" mobilephone = '"+pubUserBean.getMobilephone()+"', ");
		sql.append(" post = '"+pubUserBean.getPost()+"' ");
		sql.append(" where user_id = "+pubUserBean.getUserId()+" ");

		ret = DbComponent.exeUpdate(sql.toString());       
		       
		return ret;      
	}
	
	/**
	 * <p>class/function:com.viewscenes.database.service.pub
	 * <p>explain:ɾ��һ���û���Ϣ�����ɾ���ɹ�����true,���򷵻�false
	 * <p>author-date:����2012-7-27
	 * @param:
	 * @return:ɾ���û��Ƿ�ɹ�
	 * @throws DbException 
	 */
	public static boolean delUserById(String userId) throws DbException{
		boolean ret = false;
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" delete from  sec_user_tab  where user_id = "+userId+" ");
		
		ret = DbComponent.exeUpdate(sql.toString()); 
		
		return ret;
	}
	
	/**
	 * <p>class/function:com.viewscenes.database.service.pub
	 * <p>explain:��ѯָ�����û��Ƿ��Ѵ���,���ڷ���true,���򷵻�false
	 * <p>author-date:����2012-7-27
	 * @param:
	 * @return:�û��Ƿ����
	 * @throws DbException 
	 * @throws GDSetException 
	 */
	public static boolean isExistUser(PubUserBean pubUserBean) throws DbException, GDSetException{
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select * from sec_user_tab    t where t.user_name = '"+pubUserBean.getUserName()+"' ");
		
		GDSet set = null;
		
		set = DbComponent.Query(sql.toString());
		
		String user_id = "";
		
		if (set.getRowCount()>0){
			
			user_id = set.getString(0, "user_id");
			
			return user_id.equals(pubUserBean.getUserId())?false:true;
			
		}else{
			return false;
		}
		
	}
	
	/**
	 * <p>class/function:com.viewscenes.database.service.pub
	 * <p>explain:ȡ������Ȩ�޽����
	 * <p>author-date:����2012-7-27
	 * @param:
	 * @return:
	 */
	public static GDSet getAllFuncSet() throws DbException{
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select * from sec_operation_tab where broadcast_type = 1 and show_flag = 1  order by OPERATION_ID ");
		
		GDSet set = null;
		
		set = DbComponent.Query(sql.toString());
		
		return set;
	}
	
	
	/**
	 * <p>class/function:com.viewscenes.database.service.pub
	 * <p>explain:Ϊ�û����ģ��
	 * <p>author-date:���� 2012-8-19
	 * @param:
	 * @return:
	 * @throws DbException 
	 */
	public static int[] addUserCust(String user_id,List<String> funcList) throws DbException{

		int[] ret ;
		
		StringBuffer delSql = new StringBuffer(" delete from sec_user_operation_custom_tab where user_id = "+user_id+" ");

		String[] sqls = new String[funcList.size()+1];

		int i = 0;
		
		sqls[i] = delSql.toString();
		
		for(Iterator<String> it=funcList.iterator(); it.hasNext();){
			
			String nodeText = (String)it.next();
			
			StringBuffer inSql = new StringBuffer();
			
			inSql.append(" insert into sec_user_operation_custom_tab(user_id, OPERATION_ID)  ");
			
			inSql.append(" values("+user_id+", "+nodeText+")  ");
			
			sqls[++i] = inSql.toString();
		}
			
		ret = DbComponent.exeBatch(sqls);
	
		return ret;
	
	}
	
	
	/**
	 * <p>class/function:com.viewscenes.database.service.pub
	 * <p>explain:��ȡ��ɫȨ����
	 * <p>author-date:����2012-2-28
	 * @param:
	 * @return:
	 * @throws Exception 
	 */
	public static String getCustTreeByUserId(String user_id,String...strings) throws Exception{
		
		String result = "";
		GDSet allFuncSet=null;
		if(strings!=null&&strings.length>0){
			allFuncSet=getFuncSetByRoleId(strings[0]);
		}else{
			allFuncSet = getAllFuncSet();
		}
		
		GDSet roleFuncSet =  getCustByUserId(user_id);
		
		ArrayList list = new ArrayList();

		Document doc = StringTool.getXmlMsg();

		Element rootNode = doc.getRootElement();
		
		Element root = new Element("tree");
		root.addAttribute(new Attribute("text",""));
		
		Element title = new Element("node");
		title.addAttribute(new Attribute("text","���ƹ����б�"));
		
		ArrayList rootList = new ArrayList();
		
		HashMap nodeMap = new HashMap();
		HashMap relMap = new HashMap();
		
		for(int i=0 ; i<roleFuncSet.getRowCount(); i++){
			String funcId = roleFuncSet.getString(i, "OPERATION_ID");
			relMap.put(funcId, "1");
		}
		
		for(int i=0 ; i<allFuncSet.getRowCount(); i++){
			Element node = new Element("node");
			
			node.addAttribute(new Attribute("text",allFuncSet.getString(i, "OPERATION_NAME")));
			
			String funcId = allFuncSet.getString(i, "OPERATION_ID");
			node.addAttribute(new Attribute("id",funcId));
			
			String val = (String)relMap.get(funcId);
			if(val == null){
				val = "0";
			}
			 node.addAttribute(new Attribute("data",val));
			 
			 nodeMap.put(funcId, node);
			 
			 String PARENT_OPERATION_ID = allFuncSet.getString(i, "PARENT_OPERATION_ID");
			 if(PARENT_OPERATION_ID.equals("0")){
				 rootList.add(node);
			 }else{
				 String parentFuncId = allFuncSet.getString(i, "PARENT_OPERATION_ID");
				 Element parentNode = (Element)nodeMap.get(parentFuncId);
				 if(parentNode != null){
					 List childList = parentNode.getChildren();
					 childList.add(node);
				 }
			 }
		}
		
		for(int i=0; i<rootList.size(); i++){
			Element treeNode = (Element)rootList.get(i);
			CheckFunctionNodeData(treeNode);
		}
		
		title.setChildren(rootList);
		ArrayList tList = new ArrayList();
		tList.add(title);
		root.setChildren(tList);
		
		list.add(root);
		
		rootNode.setChildren(list);

		XMLOutputter out = new XMLOutputter("  ", true, "GB2312");

		StringWriter sw = new StringWriter();
		
		try {

			out.output(doc, sw);

		} catch (IOException ex2) {

			ex2.printStackTrace();
		}
		result = sw.toString();
//		}catch(Exception e){
//			e.printStackTrace();
//			return new EXEException("", "��ȡȨ���б�ʧ��,"+e.getMessage(), "");
//		}
		
		

		return result;
	}
	
	
	/**
	 * <p>class/function:com.viewscenes.database.service.pub
	 * <p>explain:Ϊָ����ɫ������Ȩ
	 * <p>author-date:����2012-7-27
	 * @param:
	 * @return:
	 * @throws DbException 
	 */
	public static int[] grantFuncByRoleId(String roleId,List<String> funcList) throws DbException{

		int[] ret ;
		
		StringBuffer delSql = new StringBuffer(" delete sec_role_operation_rel_tab where Role_id='"+roleId+"' " +
          		" and sec_role_operation_rel_tab.operation_id in " +
          		"(select sec_operation_tab.operation_id from sec_operation_tab where sec_operation_tab.broadcast_type=1) ");

		String[] sqls = new String[funcList.size()+1];

		int i = 0;
		
		sqls[i] = delSql.toString();
		
		for(Iterator<String> it=funcList.iterator(); it.hasNext();){
			
			String nodeText = (String)it.next();
			
			StringBuffer inSql = new StringBuffer();
			
			inSql.append(" insert into sec_role_operation_rel_tab(role_id, OPERATION_ID)  ");
			
			inSql.append(" values("+roleId+", "+nodeText+")  ");
			
			sqls[++i] = inSql.toString();
		}
			
		ret = DbComponent.exeBatch(sqls);
	
		return ret;
	
	}
	
	/**
	 * <p>class/function:com.viewscenes.database.service.pub
	 * <p>explain:��ȡ��ɫȨ����
	 * <p>author-date:����2012-2-28
	 * @param:
	 * @return:
	 * @throws Exception 
	 */
	public static String getFuncTreeByRoleId(String roleId) throws Exception{
		
		String result = "";
		
		GDSet allFuncSet = getAllFuncSet();
		
		GDSet roleFuncSet = getFuncSetByRoleId(roleId);
		
		ArrayList list = new ArrayList();

		Document doc = StringTool.getXmlMsg();

		Element rootNode = doc.getRootElement();
		
		Element root = new Element("tree");
		root.addAttribute(new Attribute("text",""));
		
		Element title = new Element("node");
		title.addAttribute(new Attribute("text","�����б�"));
		
		ArrayList rootList = new ArrayList();
		
		HashMap nodeMap = new HashMap();
		HashMap relMap = new HashMap();
		
		for(int i=0 ; i<roleFuncSet.getRowCount(); i++){
			String funcId = roleFuncSet.getString(i, "OPERATION_ID");
			relMap.put(funcId, "1");
		}
		
		for(int i=0 ; i<allFuncSet.getRowCount(); i++){
			Element node = new Element("node");
			
			node.addAttribute(new Attribute("text",allFuncSet.getString(i, "OPERATION_NAME")));
			
			String funcId = allFuncSet.getString(i, "OPERATION_ID");
			node.addAttribute(new Attribute("id",funcId));
			
			String val = (String)relMap.get(funcId);
			if(val == null){
				val = "0";
			}
			 node.addAttribute(new Attribute("data",val));
			 
			 nodeMap.put(funcId, node);
			 
			 String PARENT_OPERATION_ID = allFuncSet.getString(i, "PARENT_OPERATION_ID");
			 if(PARENT_OPERATION_ID.equals("0")){
				 rootList.add(node);
			 }else{
				 String parentFuncId = allFuncSet.getString(i, "PARENT_OPERATION_ID");
				 Element parentNode = (Element)nodeMap.get(parentFuncId);
				 if(parentNode != null){
					 List childList = parentNode.getChildren();
					 childList.add(node);
				 }
			 }
		}
		
		for(int i=0; i<rootList.size(); i++){
			Element treeNode = (Element)rootList.get(i);
			CheckFunctionNodeData(treeNode);
		}
		
		title.setChildren(rootList);
		ArrayList tList = new ArrayList();
		tList.add(title);
		root.setChildren(tList);
		
		list.add(root);
		
		rootNode.setChildren(list);

		XMLOutputter out = new XMLOutputter("  ", true, "GB2312");

		StringWriter sw = new StringWriter();
		
		try {

			out.output(doc, sw);

		} catch (IOException ex2) {

			ex2.printStackTrace();
		}
		result = sw.toString();
//		}catch(Exception e){
//			e.printStackTrace();
//			return new EXEException("", "��ȡȨ���б�ʧ��,"+e.getMessage(), "");
//		}
		
		

		return result;
	}
	
    private static void CheckFunctionNodeData(Element funcNode) {
        List children = funcNode.getChildren();

        if (children.size() == 0){
        	String dataValue = funcNode.getAttributeValue("data");
        	if(Integer.parseInt(dataValue) == 1){
        		Attribute attr = funcNode.getAttribute("checked");
                if(attr != null){
                	 attr.setValue("1");
                }else{
            		funcNode.addAttribute("checked","1");
                }
        	}else if(Integer.parseInt(dataValue) == 0){
        		Attribute attr = funcNode.getAttribute("checked");
        		if(attr != null){
               	 attr.setValue("0");
               }else{
           		funcNode.addAttribute("checked","0");
               }
        	}
        	else{
        		Attribute attr = funcNode.getAttribute("checked");
        		if(attr != null){
               	 attr.setValue("2");
               }else{
           		funcNode.addAttribute("checked","2");
               }
	    	}
            return;
        }

        for (int i = 0; i < children.size(); i++) {

            Element node = (Element) children.get(i);

            CheckFunctionNodeData(node);
        }
        int enableCount = 0;

        int disableCount = 0;

        for (int i = 0; i < children.size(); i++) {

            Element node = (Element) children.get(i);

            String data = node.getAttributeValue("checked");

            if (data.equals("1"))

                enableCount++;

            else if (data.equals("0"))

                disableCount++;
        }
        if (enableCount == children.size()) {

            Attribute attr = funcNode.getAttribute("checked");
            if(attr != null){
            	 attr.setValue("1");
            }else{
        		funcNode.addAttribute("checked","1");
            }

        } else if (disableCount == children.size()) {

            Attribute attr = funcNode.getAttribute("checked");
            if(attr != null){
            	attr.setValue("0");
            }else{
         		funcNode.addAttribute("checked","0");
            }
        } else {
            Attribute attr = funcNode.getAttribute("checked");
            if(attr != null){
            	attr.setValue("2");
            }else{
        		funcNode.addAttribute("checked","2");
            }
        }


    }
    
    /**
     * 
     * <p>class/function:com.viewscenes.database.service.usermanager/getUserByUserName
     * <p>explain:�����û�����ȡ�û�������Ϣ
     * <p>ʱ�䣺2012-3-1
     * <p>���ߣ�wxb
     * @param userName
     * @throws DbException 
     */
    public static GDSet getUserByUserName(String userName) throws DbException{
    	String sql = "select u.*,r.role_id,r.name role_name,r.priority " +
    			" from sec_user_tab u, sec_user_role_rel_tab rel,sec_role_tab r " +
    			" where rel.user_id = u.user_id " +
    			" and rel.role_id = r.role_id " +
    			" and u.user_name='"+userName+"'";
    	
    	return DbComponent.Query(sql);
    	
    	
    }

}
