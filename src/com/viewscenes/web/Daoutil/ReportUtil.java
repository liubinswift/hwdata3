package com.viewscenes.web.Daoutil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;

import org.jmask.web.controller.EXEException;

import com.viewscenes.bean.ReportBean;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.pub.GDSet;
import com.viewscenes.pub.GDSetException;
import com.viewscenes.util.StringTool;
import com.viewscenes.web.statistics.AlarmDataStatistics;
import com.viewscenes.web.statistics.EffectSummaryStatistics;
import com.viewscenes.web.statistics.QualityDataStatistics;
import com.viewscenes.web.statistics.effectReport.EffectReportStatistics;
import com.viewscenes.web.statistics.effectReport.FreqEffectReportStatistics;
import com.viewscenes.web.statistics.effectReport.TimeEffectReportStatistics;


import flex.messaging.io.amf.ASObject;


/**
 * *************************************   
*    
* 项目名称：hwdata   
* 类名称：ReportUtil   
* 类描述：   与报表相关的工具类
* 创建人：刘斌
* 创建时间：2012-12-14 上午11:00:58   
* 修改人：刘斌
* 修改时间：2012-12-14 上午11:00:58   
* 修改备注：   
* @version   1.0 
*    
***************************************
 */
public class ReportUtil {
	
	 public  static ASObject getReportBean(String starttime,String endtime,String reprottype ) throws Exception
	  {
		  ASObject objRes = new ASObject();
		 
		  ReportBean reportBean=null;
	    if(starttime==null||endtime==null||starttime.length()==0||endtime.length()==0)
	    {
	    	throw new Exception("查询报表的日期格式有误！");
	    }
	
	    String  report_sql="     SELECT t.*,tt.tab_name FROM zrst_rep_tab t,dic_report_type_tab tt" +
	    		"  WHERE  t.report_type=tt.id and report_type = "+reprottype+
        " and START_DATETIME = TO_DATE('"+starttime+"','yyyy-mm-dd HH24:MI:SS')"+
	" and END_DATETIME = TO_DATE('"+endtime+"','yyyy-mm-dd HH24:MI:SS') order by report_date desc";

		GDSet gdSet = null;
       
			gdSet= DbComponent.Query(report_sql);
			if(gdSet.getRowCount()==0){
				
				return objRes;
			}else{
				reportBean=new ReportBean();
               ArrayList list =new ArrayList();//报表详细Bean
				for(int i=0;i<gdSet.getRowCount();i++){
					 reportBean.setReport_id(gdSet.getString(i, "report_id"));
					 reportBean.setReport_date(gdSet.getString(i, "report_date"));
					 reportBean.setReport_type(gdSet.getString(i, "report_type"));
					 reportBean.setPeriod_type(gdSet.getString(i, "PERIOD_TYPE"));
					 reportBean.setStart_datetime(gdSet.getString(i, "START_DATETIME"));
					 reportBean.setEnd_datetime(gdSet.getString(i, "END_DATETIME"));
					 reportBean.setCharacter(gdSet.getString(i, "CHARACTER"));
					 reportBean.setData_num(gdSet.getString(i, "DATA_NUM"));
					 reportBean.setModify_status(gdSet.getString(i, "MODIFY_STATUS"));
					 reportBean.setModify_user(gdSet.getString(i, "MODIFY_USER"));
					 reportBean.setModify_datetime(gdSet.getString(i, "MODIFY_DATETIME"));
					 reportBean.setImport_status(gdSet.getString(i, "IMPORT_STATUS"));
					 reportBean.setImport_user(gdSet.getString(i, "IMPORT_USER"));
					 reportBean.setImport_datetime(gdSet.getString(i, "IMPORT_DATETIME"));
					 reportBean.setVerify_status(gdSet.getString(i, "VERIFY_STATUS"));
					 reportBean.setVerify_user(gdSet.getString(i, "VERIFY_USER"));
					 reportBean.setVerify_datetime(gdSet.getString(i, "VERIFY_DATETIME"));
					 reportBean.setAuthentic_status(gdSet.getString(i, "AUTHENTIC_STATUS"));
					 reportBean.setAuthentic_user(gdSet.getString(i, "AUTHENTIC_USER"));
					 reportBean.setAuthentic_datetime(gdSet.getString(i, "AUTHENTIC_DATETIME"));
					 reportBean.setRemark(gdSet.getString(i, "REMARK"));
					 objRes.put("bean", reportBean);
					 objRes.put("table", gdSet.getString(i, "tab_name"));
					 return objRes;
				}
				  
			}
			 return objRes;
	  }
	/**
	 * ************************************************
	
	* @Title: getReportIdFromStarttimeEndtime
	
	* @Description: TODO(根据开始时间，结束时间，报表类型查询报表索引)
	
	* @param @param starttime
	* @param @param endtime
	* @param @param reprottype
	* @param @return
	* @param @throws Exception    设定文件
	
	* @author  刘斌
	
	* @return ReportBean    返回类型
	
	* @throws
	
	************************************************
	 */
	  public  static ASObject getReportFromStarttimeEndtime(String starttime,String endtime,String reprottype ) throws Exception
	  {
		  ASObject objRes = new ASObject();
		 
			  ASObject asobj = getReportBean(starttime,endtime,reprottype);

			  ReportBean reportBean= (ReportBean)asobj.get("bean");
			if(reportBean == null){
				
				return null;
			}else{
				 objRes.put("reportBean", reportBean);
                String report_id=reportBean.getReport_id();
                String tablename=(String)asobj.get("table");//报表详细表名
                if(tablename.equalsIgnoreCase("zrst_rep_effect_statistics_tab")){
                	EffectReportStatistics effectStat = new EffectReportStatistics();
                	ASObject myobj = effectStat.queryDeatiReport(report_id,reportBean.getReport_type());
                	reportBean.setData_num((Integer)myobj.get("resultTotal")+"");
                	objRes.put("reportBean", reportBean);
					objRes.put("reportDesc",myobj);
					
                } else{
					String descSql="select *　　from "+tablename+"  where report_id="+report_id;
					if(tablename.equalsIgnoreCase("zrst_rep_abnormal_tab")){
						descSql+=" order by abnormal_date";
					}
					if(tablename.equalsIgnoreCase("zrst_rep_effect_summary_tab")){
						descSql+=" order by service_area,station_name,language_name,play_time,frequency";
					}
					ASObject as=new ASObject();
					as.put("startRow", 1);
					as.put("endRow", 10000);
					objRes.put("reportDesc",StringTool.pageQuerySql(descSql,as));
                }
				  
			}
       return objRes;
	  }
	  /**
	 * @throws GDSetException 
	 * @throws DbException 
	   * ************************************************
	  
	  * @Title: deleteReportByReportBean
	  
	  * @Description: TODO(根据前台传入的ReportBean删除报表，包括删除索引和详细数据)
	  
	  * @param @param reportBean
	  * @param @return    设定文件
	  
	  * @author  刘斌
	  
	  * @return Boolean    返回类型
	  
	  * @throws
	  
	  ************************************************
	   */
	  public static Boolean deleteReportByReportBean( ReportBean reportBean) throws DbException, GDSetException{

			GDSet gdSet = null;
		
		    String getReportDescTable="select t.tab_name from dic_report_type_tab t where t.id="+reportBean.getReport_id();
		    gdSet= DbComponent.Query(getReportDescTable);
		    String tableName=gdSet.getString(0, "tab_name");
		    String[] delesql=new String[2];
		    delesql[0]="delete "+tableName+" where report_id = "+reportBean.getReport_id();
		    delesql[1] = "delete zrst_rep_tab where report_id = "+reportBean.getReport_id();
		   
		    DbComponent.exeBatch(delesql);
		    return true;

		}
	  /**
		 * @throws GDSetException 
		 * @throws DbException 
		   * ************************************************
		  
		  * @Title: deleteReportByReportBean
		  
		  * @Description: TODO(根据前台传入的ReportBean删除报表，包括删除索引和详细数据)
		  
		  * @param @param reportBean
		  * @param @return    设定文件
		  
		  * @author  刘斌
		  
		  * @return Boolean    返回类型
		  
		  * @throws
		  
		  ************************************************
		   */
		  public static Boolean deleteReportByReportStarttimeEndtimeReporttype( String starttime,String endtime,String reporttype) throws DbException, GDSetException{

				GDSet gdSet = null;
			    String reportids="  SELECT t.report_id FROM zrst_rep_tab t,dic_report_type_tab tt" +
			    		"  WHERE  t.report_type=tt.id and report_type =" +reporttype+
			    		"  and START_DATETIME = TO_DATE('"+starttime+"','yyyy-mm-dd HH24:MI:SS')" +
			    		" and END_DATETIME = TO_DATE('"+endtime+"','yyyy-mm-dd HH24:MI:SS')";
			    String getReportDescTable="select t.tab_name from dic_report_type_tab t where t.id="+reporttype;
			    gdSet= DbComponent.Query(getReportDescTable);
			    String tableName=gdSet.getString(0, "tab_name");
			    String[] delesql=new String[2];
			    delesql[0]="delete "+tableName+" where report_id  in ("+reportids+")";
			    delesql[1] = "delete zrst_rep_tab where report_id in("+reportids+")";
			   
			    DbComponent.exeBatch(delesql);
			    return true;

			}
	  /**
	   * ************************************************
	  
	  * @Title: updateReportByReportBean
	  
	  * @Description: TODO(根据前台传入的ReportBean更新报表)
	  
	  * @param @param reportBean
	  * @param @return
	  * @param @throws DbException
	  * @param @throws GDSetException    设定文件
	  
	  * @author  刘斌
	  
	  * @return Boolean    返回类型
	  
	  * @throws
	  
	  ************************************************
	   */
	  public static Boolean updateReportByReportBean( ReportBean reportBean) throws DbException, GDSetException{

			GDSet gdSet = null;
		    String sql="update zrst_rep_tab set " +
					"REPORT_DATE='"+reportBean.getReport_date()+"'"+
					",REPORT_TYPE='"+reportBean.getReport_type()+"'"+
					",PERIOD_TYPE='"+reportBean.getPeriod_type()+"'"+
					",START_DATETIME='"+reportBean.getStart_datetime()+"'"+
					",END_DATETIME='"+reportBean.getEnd_datetime()+"'"+
					",CHARACTER='"+reportBean.getCharacter()+"'"+
					",DATA_NUM='"+reportBean.getData_num()+"'"+
					",MODIFY_STATUS='"+reportBean.getModify_status()+"'"+
					",MODIFY_USER='"+reportBean.getModify_user()+"'"+
					",MODIFY_DATETIME='"+reportBean.getModify_datetime()+"'"+
					",IMPORT_STATUS='"+reportBean.getImport_status()+"'"+
					",IMPORT_USER='"+reportBean.getImport_user()+"'"+
					",IMPORT_DATETIME='"+reportBean.getImport_datetime()+"'"+
					",VERIFY_STATUS='"+reportBean.getVerify_status()+"'"+
					",VERIFY_USER='"+reportBean.getVerify_user()+"'"+
					",VERIFY_DATETIME='"+reportBean.getVerify_datetime()+"'"+
					",AUTHENTIC_STATUS='"+reportBean.getAuthentic_status()+"'"+
					",AUTHENTIC_USER='"+reportBean.getAuthentic_user()+"'"+
					",AUTHENTIC_DATETIME='"+reportBean.getAuthentic_datetime()+"'"+
					",REMARK ='"+reportBean.getRemark()+"'"+
					"  where report_id="+reportBean.getReport_id();
		    DbComponent.exeUpdate(sql);
		    
		    return true;

		}
	  /**
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws SecurityException 
	   * ************************************************
	  * @Title: insertReportByReportBean
	  * @Description: TODO(根据报表索引bean，报表详细beanlist，报表的详细表名,报表详细bean路径)
	  * 注意:需要bean的第一个属性为报表的主键.
	  * @param @param reportBean
	  * @param @param bean
	  * @param @param tableName
	  * @param @return
	  * @param @throws DbException
	  * @param @throws GDSetException    设定文件
	  * @author  刘斌
	  * @return Boolean    返回类型
	  * @throws
	  ************************************************
	   */
	  public static Boolean insertReportByReportBean( ReportBean reportBean,ArrayList list,String tableName,String beanName) throws DbException, GDSetException, SecurityException, InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException{

			String[] sql=new String[list.size()+1];
		     sql[0]="insert into zrst_rep_tab(report_id,REPORT_DATE,REPORT_TYPE,PERIOD_TYPE,START_DATETIME,END_DATETIME,CHARACTER, " +
		    "DATA_NUM,MODIFY_STATUS,MODIFY_USER,MODIFY_DATETIME,IMPORT_STATUS,IMPORT_USER,IMPORT_DATETIME,VERIFY_STATUS,VERIFY_USER,"+
		    "VERIFY_DATETIME,AUTHENTIC_STATUS,AUTHENTIC_USER,AUTHENTIC_DATETIME,REMARK) values( '"+
		             reportBean.getReport_id()+
		            "',to_date('"+reportBean.getReport_date()+
					"','yyyy-mm-dd hh24:mi:ss'),'"+reportBean.getReport_type()+
					"','"+reportBean.getPeriod_type()+
					"',to_date('"+reportBean.getStart_datetime()+
					"','yyyy-mm-dd hh24:mi:ss'),to_date('"+reportBean.getEnd_datetime()+
					"','yyyy-mm-dd hh24:mi:ss'),'"+reportBean.getCharacter()+
					"','"+list.size()+
					"','"+reportBean.getModify_status()+
					"','"+reportBean.getModify_user()+
					"','"+reportBean.getModify_datetime()+
					"','"+reportBean.getImport_status()+
					"','"+reportBean.getImport_user()+
					"',to_date('"+reportBean.getImport_datetime()+
					"','yyyy-mm-dd hh24:mi:ss'),'"+reportBean.getVerify_status()+
					"','"+reportBean.getVerify_user()+
					"','"+reportBean.getVerify_datetime()+
					"','"+reportBean.getAuthentic_status()+
					"','"+reportBean.getAuthentic_user()+
					"','"+reportBean.getAuthentic_datetime()+
					"','"+reportBean.getRemark()+"')";
		     Class cl = Class.forName(beanName);
		     
	          Field[] Fields=cl.newInstance().getClass().getDeclaredFields();//根据Class对象获得属性 私有的也可以获得
	        
	          String insertsql="insert into "+tableName+"(";
	          for(Field field:Fields)	
	          {
	        	  String fieldName=field.getName();
	        	  if(fieldName.endsWith("_temp")){
	        		  continue;
	        	  }
	        	  if(!field.getType().toString().equals("class java.util.ArrayList")){

		        	  insertsql+=fieldName+",";
	        	  }

	        	   
	          }
	          insertsql=insertsql.substring(0,insertsql.length()-1)+") values(";
	         String  valuessql="";
		    for(int i=0;i<list.size();i++)
		    {
		    	
		    	Method[] methods= (Class.forName(beanName).cast(list.get(i))).getClass().getDeclaredMethods();//根据Class对象方法
		    
		      for(int k=0;k<Fields.length;k++)	
	          {
		    	  Field field=Fields[k];
		    	  if(!field.getType().toString().equals("class java.util.ArrayList")){
		    	  } else{
	            	  continue;
		    	  }
		    	  if(field.getName().endsWith("_temp")){
	        		  continue;
	        	  }
	        	  String fieldName=field.getName();
	        	  fieldName = StringTool.firstLetterToUpper(fieldName);
	              if(k==0)
	              {
	            	  valuessql="report_data_seq.nextval,'" ;
	            	  continue;
	              }
	              
	        	  for(Method me:methods)
		        	  {
		        	    if(me.getName().equals("get"+fieldName))
		        	    {
		        	    	valuessql+=me.invoke((Class.forName(beanName).cast(list.get(i))), new Object[]{})+"','";
		        	    }
		        	  }
	              
	        	 
	          }
		     
		      sql[i+1]=insertsql+valuessql.substring(0,valuessql.length()-2)+")"; 
		    }
		    DbComponent.exeBatch(sql);
		    return true;

		}
	  public static Object operationReport(ASObject obj)
	  {
		  ASObject asObject=null;
		    String reportType = (String)obj.get("reportType");//。
	         String starttime = (String)obj.get("start_datetime")+" 00:00:00";//开始时间
	         String endtime =  (String)obj.get("end_datetime")+" 23:59:59";//结束时间
	         String handlemethod =  (String)obj.get("handlemethod");//结束时间
	         ReportBean  reportBean=(ReportBean)obj.get("reportBean");
	         String user_name =  (String)obj.get("user_name");//结束时间
	         ASObject queryParam= (ASObject)obj.get("queryParam");//查询参数
	         try{
	         if(handlemethod.equals("statistic"))//统计报表。
	         {
	        	 //先删除已经有的报表。
	        	 deleteReportByReportStarttimeEndtimeReporttype(starttime,endtime,reportType);
	        	//这里需要每个人写自己的统计方法，或者调用自己的统计方法。
	        	 if(reportType.equals("1001"))//海外异态
	        	 {
	        		 AlarmDataStatistics.doReport(starttime, endtime, "1", "1001", user_name);
	        	 }else if(reportType.equals("1002"))//海外落地异态
	        	 {
	        		 AlarmDataStatistics.doReport(starttime, endtime, "2", "1002", user_name);
	        	 }else if(reportType.equals("1003"))//国际台效果报告统计
	        	 {
	        		 EffectReportStatistics effectStat = new EffectReportStatistics();
	        		 effectStat.statisticsReport(starttime, endtime, "1", "1003", user_name,queryParam);
	        	 }else if(reportType.equals("1004"))//海外落地效果报告统计
	        	 {
	        		 EffectReportStatistics effectStat = new EffectReportStatistics();
	        		 effectStat.statisticsReport(starttime, endtime, "2", "1004", user_name,queryParam);
	        	 }
	        	 else if(reportType.equals("1005"))//遥控站指标测量数据统计
	        	 {
	        		 QualityDataStatistics.doReport(starttime, endtime, "1005", user_name,queryParam);
	        	 }
	        	 else if(reportType.equals("1006"))//遥控站频段效果统计
	        	 {
	        		 FreqEffectReportStatistics.statisticsReport(starttime, endtime, "1006", user_name,queryParam);
	        	 }
	        	 else if(reportType.equals("1007"))//遥控站时段效果统计
	        	 {
	        		 TimeEffectReportStatistics.statisticsReport(starttime, endtime, "1007", user_name,queryParam);
	        	 }
	        	 else if(reportType.equals("1008"))//国际台效果汇总数据统计
	        	 {
	        		 EffectSummaryStatistics.doReport(starttime, endtime, "1008", user_name,queryParam);
	        	 }
	        	 else if(reportType.equals("1009"))//海外落地效果汇总数据统计
	        	 {
	        		 EffectSummaryStatistics.doReport(starttime, endtime, "1009", user_name,queryParam);
	        	 } 
	        	 
	         }else   if(handlemethod.equals("verify"))//校对报表。
	         {
	        	
	        	 updateReportByReportBean(reportBean);
	         }else   if(handlemethod.equals("audit"))// 审核报表。
	         {
	        	
	        	 updateReportByReportBean(reportBean);
	         }

	        	 asObject= getReportFromStarttimeEndtime(starttime,endtime,reportType);
	         
	         } catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return new EXEException("", "处理报表有误，原因：！"+ e.getMessage(),null);
				}
		 
		return asObject;
	  }
	    public static String getReportId() {
	        String reportId = "";
	        String sql="select report_seq.nextval report_id from dual";
	        try {
	            GDSet res = DbComponent.Query(sql);
	            for (int i = 0; i < res.getRowCount(); i++) {
	                reportId = res.getString(i, "report_id") ;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	        return reportId;
	    }
	    public static void main(String[] args)
	    {
	    	Class cl;
	    	try {
	    		cl = Class.forName("com.viewscenes.bean.ZRST_REP_ABNORMAL_BEAN");
	    		   try {
	    			Field[] Fields;
					try {
						Fields = cl.newInstance().getClass().getDeclaredFields();
						Method[] meths=cl.newInstance().getClass().getDeclaredMethods();
		    			 for(int i=0;i<Fields.length;i++)	
		    			 {
		    				 System.out.println("=="+Fields[i].getName());
		    			 }
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    		
	    		} catch (SecurityException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		} 
	    	} catch (ClassNotFoundException e1) {
	    		// TODO Auto-generated catch block
	    		e1.printStackTrace();
	    	}
	    }
	    
	    /**
	     * 取得统计报表时用到的报表bean
	     * @detail  
	     * @method  
	     * @param report_id
	     * @param starttime
	     * @param endtime
	     * @param report_type
	     * @param user_name
	     * @return 
	     * @return  ReportBean  
	     * @author  zhaoyahui
	     * @version 2013-1-6 下午03:27:06
	     */
	    public static ReportBean getBasicStatisticsReportBean(String report_id,String starttime,String endtime,String report_type,String user_name){
	    	ReportBean reportBean = new ReportBean();
			reportBean.setReport_id(report_id);
			reportBean.setReport_date(StringTool.Date2String(new Date()));
			reportBean.setReport_type(report_type);
			reportBean.setPeriod_type("");
			reportBean.setStart_datetime(starttime);
			reportBean.setEnd_datetime(endtime);
			reportBean.setCharacter("");
			reportBean.getData_num();

			reportBean.setImport_status("1");
			reportBean.setImport_datetime(StringTool.Date2String(new Date()));
			reportBean.setImport_user(user_name);
			reportBean.setModify_status("1");
			reportBean.setModify_user("");
			reportBean.setModify_datetime("");

			reportBean.setVerify_status("");
			reportBean.setVerify_user("");
			reportBean.setVerify_datetime("");
			reportBean.setAuthentic_status("");
			reportBean.setAuthentic_user("");
			reportBean.setAuthentic_datetime("");
			reportBean.setRemark("");
			
			return reportBean;
	    }
}
