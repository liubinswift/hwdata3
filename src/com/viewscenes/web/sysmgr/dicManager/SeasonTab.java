package com.viewscenes.web.sysmgr.dicManager;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Element;
import org.jmask.web.controller.EXEException;

import com.viewscenes.bean.ZdicSeasonBean;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.printexcel.JExcel;
import com.viewscenes.pub.GDSet;
import com.viewscenes.pub.GDSetException;
import com.viewscenes.util.LogTool;
import com.viewscenes.util.StringTool;

import flex.messaging.io.amf.ASObject;

public class SeasonTab {
	
	private static final ZdicSeasonBean bean = null;

	/**
	 * 
	 * <p>class/function:com.viewscenes.web.sysmgr.dicManager
	 * <p>explain:查询季节数据 ，如果添加出错会返回错误信息
	 * <p>author-date:张文 2012-7-26
	 * @param:
	 * @return:
	 */
	
    public Object getSeason(ZdicSeasonBean bean){

    	return getSeasonlist(bean);
    }

    public Object getSeasonlist(ZdicSeasonBean bean){
	    ASObject resObj;
        String code=bean.getCode();
        String sql="select * from dic_season_tab where 1=1";
		if(code!=null&&!code.equalsIgnoreCase("")){
			sql+=" and code like '%"+code+"%'";
		}
		sql+=" order by  is_now asc,end_time desc"; 
        try {
			resObj = StringTool.pageQuerySql(sql, bean);
		} catch (Exception e) {
			LogTool.fatal(e);
			return new EXEException("",e.getMessage(),"");
		}
    	
    	return resObj;
    }
    
    /**
	 * 
	 * <p>class/function:com.viewscenes.web.sysmgr.dicManager
	 * <p>explain:添加季节数据，如果添加出错会返回错误信息
	 * <p>author-date:张文 2012-7-26
	 * @param:
	 * @return:
	 */
	 
    public ArrayList<Object>AddSeasonIs(ZdicSeasonBean bean){
		ArrayList<Object> list =  new ArrayList<Object>();
		try {
			
			
			String code = bean.getCode();
			
			boolean has = isExistCode(bean);
			
			if (has){
				EXEException ex =  new EXEException("", "error:该code已存在,请重新输入", "");
				list.add(ex);
				return list;
			}
			AddSeason(bean);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			EXEException ex =  new EXEException("", "error:添加季节对象["+bean.toString()+"]出错,"+e.getMessage(), "");
			list.add(ex);
		}
		
		return list;
	}
    
	public static boolean isExistCode(ZdicSeasonBean SeasonBean) throws DbException, GDSetException{
			
			StringBuffer sql = new StringBuffer();
			
			sql.append(" select * from dic_season_tab t where t.code = '"+SeasonBean.getCode()+"' ");
			
			GDSet set = null;
			
			set = DbComponent.Query(sql.toString());
			
			
			if (set.getRowCount()>0){
				
				return true;
				
			}else{
				return false;
			}
			
		}
    
	public Object AddSeason(ZdicSeasonBean bean) throws Exception{
		     String insertSql ="";
		   
             String code = bean.getCode().toUpperCase();
             String start_time = bean.getStart_time();
             String end_time = bean.getEnd_time();
             String  is_now = bean.getIs_now();
             
             
            insertSql = "insert into dic_season_tab(code,start_time,end_time,is_now) " +
            				 "values('"+code+"','"+start_time+"','"+end_time+"','"+is_now+"')";
            String sql="select * from dic_season_tab where code='"+code+"'";
            String upSql= "update dic_season_tab set is_now = 1";
            try{
            	GDSet gd=DbComponent.Query(sql);
            	if(gd.getRowCount()>0){
            		System.out.println(gd.getString(0, "code"));
            		return new EXEException("","季节CODE已存在，请重新填写","");
            	}
            	if(is_now.equals("0")){
            		DbComponent.exeUpdate(upSql);
            	}
            	DbComponent.exeUpdate(insertSql);
            	
            }
            catch (DbException e) {
            	e.printStackTrace();
    			return  new EXEException("", ","+e.getMessage(), "");
    		}
    		catch (Exception e) {
    			e.printStackTrace();
    			return new EXEException("","后台错误!",null);
    		}
            
    		return "";
	}
	
	
	  /**
	 * 
	 * <p>class/function:com.viewscenes.web.sysmgr.dicManager
	 * <p>explain:删除季节数据，如果添加出错会返回错误信息
	 * <p>author-date:张文 2012-7-26
	 * @param:
	 * @return:
	 */
    public Object deleteSeason(ASObject asObj){
    	
		String dellist=(String)asObj.get("dellist");
		
		try{
			System.out.println("dellist="+dellist);
//			delSeason(dellist);
			String sql = "delete from dic_season_tab where code in("+dellist+")";
			DbComponent.exeUpdate(sql);
		 }catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("","后台错误!",null);
		}
		 return " ";
	}
    
    
    /**
	 * 
	 * <p>class/function:com.viewscenes.web.sysmgr.dicManager
	 * <p>explain:删除数据，如果添加出错会返回错误信息
	 * <p>author-date:张文 2012-7-26
	 * @param:
	 * @return:
	 */
	
	public boolean delSeason(String dellist) throws Exception {
		String sql ="";
		boolean df=false;
		
		sql = "delete from dic_season_tab where code in("+dellist+")";
		
		df=DbComponent.exeUpdate(sql);
		return df;
	}
	
	
	/**
	 * 
	* <p>class/function:com.viewscenes.web.sysmgr.dicManager
	 * <p>explain:修改季节信息，如果修改出错会返回错误信息
	 * <p>author-date:谭长伟 2012-2-28
	 * @param:
	 * @return:
	 */
	public Object updateSeason(ZdicSeasonBean bean) throws Exception{
	     ArrayList<Object> list =  new ArrayList<Object>();
	     String updateSql ="";
	   
        String code = bean.getCode().toUpperCase();
        String start_time = bean.getStart_time();
        String end_time = bean.getEnd_time();
        String  is_now = bean.getIs_now();
        
        
        updateSql = "update  dic_season_tab set start_time ='"+start_time+"'," +
            		"end_time='"+end_time+"',is_now='"+is_now+"' where code = '"+code+"'";
     
       try{
    	 DbComponent.exeUpdate(updateSql);
    	 
       }
       catch (DbException e) {
       	e.printStackTrace();
			EXEException ex =  new EXEException("", ","+e.getMessage(), "");
			list.add(ex);
		}
		catch (Exception e) {
			e.printStackTrace();
			return new EXEException("","后台错误!",null);
		}
       
		return list;
   }
	/**
	 * 导出季节信息
	 * @param msg
	 * @param request
	 * @param response
	 */
	public void getSeasonExcel(String msg,HttpServletRequest request,HttpServletResponse response){
		Element root = StringTool.getXMLRoot(msg);
		String fileName="季节代号信息";
		String downFileName = "季节代号信息";
		String code=root.getChildText("code");
//		String contry=root.getChildText("contry");
//		String ciraf=root.getChildText("ciraf");
		StringBuffer bf=new StringBuffer();
		bf.append("select * from dic_season_tab where 1=1 ");
		
		if(code!=null&&!code.equalsIgnoreCase("")){
			bf.append(" and code like ('%").append(code).append("%')");
		}
		
		bf.append(" order by end_time desc");
		try {
			JExcel jExcel=new JExcel();
			downFileName=jExcel.openDocument();
			jExcel.WorkBookGetSheet(0);
			GDSet gd=DbComponent.Query(bf.toString());
			int n=gd.getRowCount();
			if(n>0){
				for(int i=0;i<n;i++){
					jExcel.addDate(0, i+1,gd.getString(i, "code"),jExcel.dateCellFormat);
					jExcel.addDate(1, i+1,gd.getString(i, "start_time"),jExcel.dateCellFormat);
					jExcel.addDate(2, i+1,gd.getString(i, "end_time"),jExcel.dateCellFormat);
					jExcel.addDate(3, i+1,toIs_now(gd.getString(i, "is_now")),jExcel.dateCellFormat);
				
								
				}
			}
			jExcel.getWorkSheet().setRowView(0, 220);
			jExcel.getWorkSheet().setColumnView(0, 30);
	    	jExcel.getWorkSheet().setColumnView(1, 60);
	    	jExcel.getWorkSheet().setColumnView(2, 60);	
	    	jExcel.getWorkSheet().setColumnView(3, 30);
	    
	    	
	    	
			jExcel.addDate(0, 0,"季节代号",jExcel.dateTITLEFormat);
			jExcel.addDate(1, 0,"开始时间",jExcel.dateTITLEFormat);
			jExcel.addDate(2, 0,"结束时间",jExcel.dateTITLEFormat);
			jExcel.addDate(3, 0,"是否当前",jExcel.dateTITLEFormat);
		
			
			jExcel.getWorkSheet().setName("季节代号信息");
			jExcel.saveDocument();
			
			response.setContentType("application/vnd.ms-excel");
	        response.setHeader("Location", "Export.xls");
	        response.setHeader("Expires", "0");
	        response.setHeader("Content-Disposition", "attachment; filename="+new String((fileName).getBytes("GBK"),"ISO-8859-1")+".xls");
	        OutputStream outputStream = response.getOutputStream();
	        InputStream inputStream = new FileInputStream(downFileName);
	        byte[] buffer = new byte[1024];
	        int i = -1;
	        while ( (i = inputStream.read(buffer)) != -1) {
	          outputStream.write(buffer, 0, i);
	        }
	        outputStream.flush();
	        outputStream.close();
	        outputStream = null;
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private String toIs_now(String str){
		if(str.equals("0")){
			return "当前";
		}else{
			return "不当前";
		}
	}
}
