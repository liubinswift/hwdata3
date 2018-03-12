package com.viewscenes.web.sysmgr.dicManager;



import org.jmask.web.controller.EXEException;

import com.viewscenes.bean.ZdicLanguageBean;

import com.viewscenes.util.LogTool;
import com.viewscenes.util.StringTool;

import flex.messaging.io.amf.ASObject;
public class DicService {
	public DicService() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 
	 * <p>class/function:com.viewscenes.web.sysmgr.dicManager
	 * <p>explain:查询大洲，如果添加出错会返回错误信息
	 * <p>author-date:张文 2012-7-26
	 * @param:
	 * @return:
	 */
	public Object getState(ASObject obj){
		    ASObject resObj;
	        
	        String sql = "select * from dic_state_tab order by state_name desc ";
	        
	        try {
				resObj = StringTool.pageQuerySql(sql, obj);
			} catch (Exception e) {
				LogTool.fatal(e);
				return new EXEException("",e.getMessage(),"");
			}
	    	
	    	return resObj;
	}
	
	/**
	 * 
	 * <p>class/function:com.viewscenes.web.sysmgr.dicManager
	 * <p>explain:查询前端数据 ，如果查询出错会返回错误信息
	 * <p>author-date:张文 2012-7-26
	 * @param:
	 * @return:
	 */
	public Object getHeadend(ASObject obj){
		    ASObject resObj;
		    String type_id = (String)obj.get("type_id");
		    String is_online = (String)obj.get("is_online");
		    String shortname = (String)obj.get("shortname");
		    String code = (String)obj.get("code");
		    String state=(String)obj.get("state");
		    
//	        String sql = "select distinct * from (select decode(t.type_id||t.version, '102V8', substr(t.code, 0, length(t.code)-1),t.code) code,com_id,com_protocol,ip," +
//	        			"longitude,latitude,comphone,site,address,site_status,com_status,fault_status,"+
//	        			"station_name,descript,state,country,version,occur_time,altitude,summertime,ciraf,person,"+
//	        			"person_phone,principal,principal_phone,time_diff,default_language,x,y,url,is_delete,type_id,is_online,decode(t.type_id||t.version, '102V8','',t.manufacturer) manufacturer," +
//	        			"decode(t.type_id||t.version, '102V8', substr(t.shortname, 0, length(t.shortname)-1),t.shortname) shortname,post,service_name "+ 
//	        			" from res_headend_tab t ) where is_delete=0";
		    String sql = " select * from res_headend_tab where is_delete=0";
	        if(type_id.length()>0)
	        	 sql += " and type_id  in('"+type_id+"') ";
	        
	        if(shortname.length()>0)
	        	sql += " and shortname  like ('%"+shortname+"%')";
	        
	        if(code.length()>0)
	        	sql += " and code like ('%"+code.toUpperCase()+"%')";
	        if(is_online.length()>0){
	            sql +=" and is_online ='"+is_online+"'";
	        }
	        if(state.length()>0){
	        	sql+=" and state = '"+state+"'";
	        }
	        sql +=" order by code  ";
	        try {
				resObj = StringTool.pageQuerySql(sql, obj);
			} catch (Exception e) {
				LogTool.fatal(e);
				return new EXEException("",e.getMessage(),"");
			}
	    	
	    	return resObj;
	}
	
	
	
	/**
	 * 
	 * <p>class/function:com.viewscenes.web.sysmgr.dicManager
	 * <p>explain:查询语言，如果添加出错会返回错误信息
	 * <p>author-date:张文 2012-7-26
	 * @param:
	 * @return:
	 */
	public Object getLanguage(ZdicLanguageBean bean){
		    ASObject resObj;
	        
	        String sql = "select * from zdic_language_tab where is_delete =0  order by language_name desc ";
	        
	        try {
				resObj = StringTool.pageQuerySql(sql, bean);
			} catch (Exception e) {
				LogTool.fatal(e);
				return new EXEException("",e.getMessage(),"");
			}
	    	
	    	return resObj;
	}

//	public Object getHeadendAll(ASObject obj) {
//		ArrayList<Object> list=new ArrayList<Object>();
//		String type_id = (String)obj.get("type_id");
//	    String is_online = (String)obj.get("is_online");
//	    String site = (String)obj.get("site");
//	    String code = (String)obj.get("code");
//	    String state=(String)obj.get("state");
//	    
//        StringBuffer sf = new StringBuffer();
//        sf.append("select * from res_headend_tab where is_delete=0");
//        
//        if(type_id.length()>0)
//        	 sf.append(" and type_id  in('").append(type_id).append("') ");
//        if(code.length()>0)
//        	sf.append(" and code  like ('%").append(code.toUpperCase()).append("%')");
//        if(is_online.length()>0){
//            sf.append(" and is_online = '").append(is_online).append("'");
//        }
//        if(site.length()>0)
//        	sf.append(" and site  like ('%").append(site).append("%')");
//        if(state.length()>0){
//        	sf.append(" and state = '").append(state).append("'");
//        }
//        sf.append(" order by shortname desc ");
//        String sql=sf.toString();
//        String classPath=ResHeadendBean.class.getName();
//        try {
//			GDSet set=DbComponent.Query(sql);
//			list=StringTool.converGDSetToBeanList(set, classPath);
//		} catch (Exception e) {
//			LogTool.fatal(e);;
//			return new EXEException("",e.getMessage(),"");
//		}
//        
//		return list;
//	}
	
}
