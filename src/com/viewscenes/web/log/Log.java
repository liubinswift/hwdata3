package com.viewscenes.web.log;

import java.util.ArrayList;

import org.jmask.web.controller.EXEException;


import com.viewscenes.bean.NotificationBean;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.pub.GDSet;
import com.viewscenes.pub.GDSetException;
import com.viewscenes.util.StringTool;

import flex.messaging.io.amf.ASObject;


public class Log {
	/**
	 * ************************************************
	* @Title: getLogList
	* @Description: TODO(根据有效期查询通知)
	* @param @param NotificationBean
	* @param @return    设定文件
	* @author  刘斌
	* @return Object    返回类型
	* @throws
	
	************************************************
	 */
	    public Object getLogList(NotificationBean obj) {
	    	ASObject objRes = new ASObject();
	         String valid_start_datetime =obj.getStart_time();//有效开始时间
	         String valid_end_datetime = obj.getEnd_time();//有效结束时间
	    
	           ArrayList<NotificationBean> list = new ArrayList();
	           GDSet gdSet = null;
	           GDSet gdSet2 = null;
		      

	       String loglistsql="select t.id,t.from_userid,t.create_time,t.to_userid,t.check_userid ," +
	       		"t.description,substr(t.start_time,0,10) start_time,substr(t.end_time,0,10) end_time,t.from_username,t.to_username " +
	       		" from res_natification_tab t" +
	       		" where  t.create_time>to_date('"+valid_start_datetime+"','yyyy-mm-dd hh24:mi:ss') and   t.create_time <to_date('"+valid_end_datetime+"','yyyy-mm-dd hh24:mi:ss') and t.is_delete=0  " +
	       		"order by t.create_time desc ";
	        	String totalsql="select count(*) count" +
	       		" from res_natification_tab t" +
	       		" where  t.create_time>to_date('"+valid_start_datetime+"','yyyy-mm-dd hh24:mi:ss') and   t.create_time <to_date('"+valid_end_datetime+"','yyyy-mm-dd hh24:mi:ss') and t.is_delete=0  ";	      
	       
	         try {
	        	
	        	  Integer startRow = (Integer)obj.getStartRow();
     			  Integer endRow = (Integer)obj.getEndRow();
	        		gdSet = DbComponent.Query(StringTool.pageSql(loglistsql.toString(),startRow,endRow));
					gdSet2= DbComponent.Query(totalsql);
				   for (int i = 0; i < gdSet.getRowCount(); i++) {
					   NotificationBean task=new NotificationBean();
					    task.setId(gdSet.getString(i, "id"));
					    task.setCheck_userid(gdSet.getString(i, "check_userid"));
					    task.setCreate_time(gdSet.getString(i, "create_time"));
					    task.setDescription(gdSet.getString(i, "description"));
					    task.setEnd_time(gdSet.getString(i, "end_time"));
					    task.setFrom_userid(gdSet.getString(i, "from_userid"));
					    task.setStart_time(gdSet.getString(i, "start_time"));
					    task.setTo_userid(gdSet.getString(i, "to_userid"));
					    task.setFrom_username(gdSet.getString(i, "from_username"));
					    task.setTo_username(gdSet.getString(i, "to_username"));
					    list.add(task); 
			         }
				   
				   
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new EXEException("", "查询通知信息失败！"+ e.getMessage(),null);
			} catch (GDSetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new EXEException("", "查询通知信息失败！"+ e.getMessage(),null);
			}
			objRes.put("resultList", list);
			try {
				objRes.put("resultTotal", gdSet2.getString(0, "count"));
			} catch (GDSetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new EXEException("", "查询通知信息失败！"+ e.getMessage(),null);
			}
	         return objRes;
	    }
	    /**
         * ************************************************
        
        * @Title: 
        
        * @Description: TODO(删除通知功能)
        
        * @param @param msg
        * @param @return
        * @param @throws DbException
        * @param @throws GDSetException    设定文件
        
        * @author  刘斌
        
        * @return String    返回类型
        
        * @throws
        
        ************************************************
         */
public Object delLogList(ArrayList logList) {

   String log_ids="";
    for (int i = 0; i < logList.size(); i++) {
    	NotificationBean log=(NotificationBean) logList.get(i);
        String log_id =log.getId() ;
       
        	log_ids+=log_id+",";
       }
    log_ids=log_ids.substring(0,log_ids.length()-1);
	String sql = "update res_natification_tab t set t.is_delete=1 where t.id in("+ log_ids + ")";
	                            
	   try {
		DbComponent.exeUpdate(sql.toString());
	} catch (DbException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return new EXEException("", "删除通知信息失败！"+ e.getMessage(),null);
	}

  return "";
  }
/**
 * ************************************************

* @Title: 

* @Description: TODO(有人看过通知后更新通知功能)

* @param @param msg
* @param @return
* @param @throws DbException
* @param @throws GDSetException    设定文件

* @author  刘斌

* @return String    返回类型

* @throws

************************************************
 */
public Object updateLogList(ArrayList logList) {
		
		String log_ids="";
		String user_id="";
		for (int i = 0; i < logList.size(); i++) {
		NotificationBean log=(NotificationBean) logList.get(i);
		
		String log_id =log.getId() ;
		
			log_ids+=log_id+",";
			user_id=log.getCheck_userid();
		}
		log_ids=log_ids.substring(0,log_ids.length()-1);
		String sql = "update res_natification_tab t set t.check_userid=check_userid||',"+user_id+"' where t.id in("+ log_ids + ")";
		                        
		try {
		DbComponent.exeUpdate(sql.toString());
		} catch (DbException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return new EXEException("", "更新通知信息失败！"+ e.getMessage(),null);
		}
		
		return "";
		}

/**
 * ************************************************

* @Title: 

* @Description: TODO(删除通知功能)

* @param @param msg
* @param @return
* @param @throws DbException
* @param @throws GDSetException    设定文件

* @author  刘斌

* @return String    返回类型

* @throws

************************************************
 */
public Object addAndUpdateLogList(NotificationBean bean) {
		
	 String id=bean.getId();
	 String from_userid=bean.getFrom_userid();
	 String to_userid=bean.getTo_userid();
	 String check_userid=bean.getCheck_userid();
	 String description=bean.getDescription();
	 String start_time=bean.getStart_time();
	 String end_time=bean.getEnd_time();
	 String to_username=bean.getTo_username();
	 String from_username=bean.getFrom_username();
	 String msg_checked = bean.getMsg_checked();//是否发送短信
	 String sql ="";
       if(id.length()!=0)
       {
    	   sql=" update  res_natification_tab t set t.from_userid='" +from_userid+
    	   		"' ,t.create_time=sysdate"+
    	   		",t.to_userid='" +to_userid+
    	   		"',t.to_username='" +to_username+
    	   		"',t.from_username='" +from_username+
    	   		"' ,t.check_userid='" +check_userid+
    	   		"' ,t.description= '"+description+"',t.start_time= '"+start_time+"',t.end_time='"+end_time+"'"
    	   		+" where t.id="+id;


       }else 
       {
    	   sql="insert into res_natification_tab(id,from_userid,create_time,to_userid," +
    	   		"check_userid,description,start_time,end_time,from_username,to_username)" +
    	   		" values(RES_RESOURSE_SEQ.Nextval,'"+from_userid+"',sysdate,'"
    	   		+to_userid+"','"+check_userid+"','"+description+"','"+start_time+"','"+end_time+"','"+from_username+"','"+to_username+"')";
       }
       //发送短信相关
        if(msg_checked.equalsIgnoreCase("true")){
        	StringBuffer sbf = new StringBuffer("select * from SEC_USER_TAB where user_id in(");
        	sbf.append("select user_id from SEC_USER_ROLE_REL_TAB where role_id in(");
        	sbf.append("select role_id from SEC_ROLE_TAB where role_id in(");
        	sbf.append(to_userid);
        	sbf.append(")");
        	sbf.append(")");
        	sbf.append(")");
        	try {
				GDSet gd = DbComponent.Query(sbf.toString());
				for(int i=0;i<gd.getRowCount();i++){
					StringBuffer sbf2 = new StringBuffer("insert into SMS_SENDSTATE_TAB(id,userid,sms,state,mobilephone)values(");
					sbf2.append("RES_RESOURSE_SEQ.Nextval,'");
					sbf2.append(gd.getString(i, "user_id"));
					sbf2.append("','");
					sbf2.append(description);
					sbf2.append("','0','");
					sbf2.append(gd.getString(i, "mobilephone"));
					sbf2.append("')");
					DbComponent.exeUpdate(sbf2.toString());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new EXEException("", "短信入库失败！"+ e.getMessage(),null);
			}
        }
		try {
		    DbComponent.exeUpdate(sql.toString());
		} catch (DbException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return new EXEException("", "更新通知信息失败！"+ e.getMessage(),null);
		}
		
		return "更新通知信息成功！";
}

}


