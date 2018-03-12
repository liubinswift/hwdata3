package com.viewscenes.web.sysmgr.dicManager;

import com.viewscenes.pub.GDSet;
import com.viewscenes.pub.GDSetException;
import com.viewscenes.sys.TableInfoCache;
import com.viewscenes.web.sysmgr.dicManager.DicDao;
import com.viewscenes.web.sysmgr.dicManager.DicService;

import org.jmask.web.controller.EXEException;

import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Element;
import com.viewscenes.printexcel.JExcel;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import com.viewscenes.util.StringTool;

import flex.messaging.io.amf.ASObject;
import com.viewscenes.bean.ZdicLanguageBean;


public class LanguageTab {

	
	/**
	 * 
	 * <p>class/function:com.viewscenes.web.sysmgr.dicManager
	 * <p>explain:查询语言数据 ，如果添加出错会返回错误信息
	 * <p>author-date:张文 2012-7-26
	 * @param:
	 * @return:
	 */
	
    public Object getLanguage(ZdicLanguageBean bean){

    	return new DicService().getLanguage(bean);
    }
    
    
    /**
	 * 
	 * <p>class/function:com.viewscenes.web.sysmgr.dicManager
	 * <p>explain:提交更新语言数据，如果添加出错会返回错误信息
	 * <p>author-date:张文 2012-7-26
	 * @param:
	 * @return:
     * @throws GDSetException 
     * @throws DbException 
	 */

	 
	public Object updateLanguange(ASObject asObj) throws ParseException, DbException, GDSetException{
		
		
		String insertSql ="";
		String updateSql ="";
		ArrayList insertIdList =  new ArrayList();
		ArrayList updateIdList =  new ArrayList();
		insertIdList=(ArrayList)asObj.get("insert");
		updateIdList=(ArrayList)asObj.get("update");
		int iNum = 0;
        int uNum = 0;
       
       for(int i=0;i<insertIdList.size();i++){
    	     ASObject resultStat = (ASObject)insertIdList.get(i);
    	     String language_id = (String)resultStat.get("language_id");
             String language_name = (String)resultStat.get("language_name");
             String broadcast_direction = (String)resultStat.get("broadcast_direction");
             String val = getLanguageNextVal();
                 insertSql = "insert into zdic_language_tab(language_id, language_name,broadcast_direction) " +
            				 "values('"+val+"','"+language_name+"','"+broadcast_direction+"')";
          
         		
            try{
         	 DbComponent.exeUpdate(insertSql);
         	ZdicLanguageBean ZdicLanguageBean = new ZdicLanguageBean();
         	ZdicLanguageBean.setLanguage_id(val);
         	ZdicLanguageBean.setLanguage_name(language_name);
         	ZdicLanguageBean.setBroadcast_direction(broadcast_direction);
  		     TableInfoCache as =new TableInfoCache();
  		     as.refreshTableMap("zdic_language_tab",ZdicLanguageBean,ZdicLanguageBean.getLanguage_id(),"1");
            }
            catch (DbException e) {
    			e.printStackTrace();
    			return new EXEException("","数据库查询错误!",null);
    		}
    		catch (Exception e) {
    			e.printStackTrace();
    			return new EXEException("","后台错误!",null);
    		}
             iNum++;
         }
       
       for(int i=0;i<updateIdList.size();i++){
    	   
    	   ASObject resultStat = (ASObject)updateIdList.get(i);
    	   String language_id = (String)resultStat.get("language_id");
  	       String language_name = (String)resultStat.get("language_name");
  	     String broadcast_direction = (String)resultStat.get("broadcast_direction");
        
           
           updateSql = " update  zdic_language_tab set Language_name='"+language_name+"', broadcast_direction='"+broadcast_direction+"' where Language_id='"+language_id+"'";

           
           try{
           	 DbComponent.exeUpdate(updateSql);
           	 
           	ZdicLanguageBean ZdicLanguageBean = new ZdicLanguageBean();
         	ZdicLanguageBean.setLanguage_id(language_id);
         	ZdicLanguageBean.setLanguage_name(language_name);
        	ZdicLanguageBean.setBroadcast_direction(broadcast_direction);
  		     TableInfoCache as =new TableInfoCache();
  		     as.refreshTableMap("zdic_language_tab",ZdicLanguageBean,ZdicLanguageBean.getLanguage_id(),"2");
              }
              catch (DbException e) {
      			e.printStackTrace();
      			return new EXEException("","数据库查询错误!",null);
      		}
      		catch (Exception e) {
      			e.printStackTrace();
      			return new EXEException("","后台错误!",null);
      		}
           uNum++;
       }
       
       ArrayList resultList=new ArrayList();
       resultList.add(iNum);
       resultList.add(uNum);
       return resultList;
	}
	
	
	 /**
	 * 
	 * <p>class/function:com.viewscenes.web.sysmgr.dicManager
	 * <p>explain:删除语言数据，如果添加出错会返回错误信息
	 * <p>author-date:张文 2012-7-26
	 * @param:
	 * @return:
	 */
    public Object deleteLanguage(ASObject asObj){
    	DicDao asDao = new DicDao();
		String dellist=(String)asObj.get("dellist");
		
		try{
			asDao.deleteLanguage(dellist);
			ZdicLanguageBean ZdicLanguageBean = new ZdicLanguageBean();
			 TableInfoCache as =new TableInfoCache();
			String[] delArr = dellist.split(",");
			 for(int i=0;i<delArr.length;i++){
				if(delArr[i].length()>3){
					 ZdicLanguageBean.setLanguage_id(delArr[i].split("'")[1]);
					 as.refreshTableMap("zdic_language_tab",ZdicLanguageBean,ZdicLanguageBean.getLanguage_name(),"3"); 
				}
			}
		 }catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("","后台错误!",null);
		}
		 return " ";
	}
    
    /**
	 * 
	 * <p>class/function:com.viewscenes.database.service.usermanager
	 * <p>explain:取得添加大洲的序列
	 * <p>author-date:谭长伟 2012-3-3
	 * @param:
	 * @return:
	 */
	private static String getLanguageNextVal() throws DbException, GDSetException{
		
		String sql = " select SEC_SEQ.nextval val from dual ";
		
		
		GDSet set = DbComponent.Query(sql);
		
		String val = set.getString(0, "val");
		
		return val;
	}
	

		/**
		 * 导出发射台信息信息
		 * @param msg
		 * @param request
		 * @param response
		 */
		public void getLanguageExcel(String msg,HttpServletRequest request,HttpServletResponse response){
			Element root = StringTool.getXMLRoot(msg);
			HashMap<String,String> map=new HashMap<String, String>();

			String fileName="语言信息";
			String downFileName = "语言信息";
			String name=root.getChildText("name");
		
			String sql = "select * from zdic_language_tab where is_delete =0  order by language_name desc ";
	        
		

			try {
				JExcel jExcel=new JExcel();
				downFileName=jExcel.openDocument();
				jExcel.WorkBookGetSheet(0);
				GDSet gd=DbComponent.Query(sql);
				int n=gd.getRowCount();
				if(n>0){
					for(int i=0;i<n;i++){
						jExcel.addDate(0, i+1,gd.getString(i, "language_id"),jExcel.dateCellFormat);
						jExcel.addDate(1, i+1,gd.getString(i, "language_name"),jExcel.dateCellFormat);
						jExcel.addDate(2, i+1,gd.getString(i, "broadcast_direction"),jExcel.dateCellFormat);
										
					}
				}
				jExcel.getWorkSheet().setRowView(0, 600);
				jExcel.getWorkSheet().setColumnView(0, 30);
		    	jExcel.getWorkSheet().setColumnView(1, 80);
		    	jExcel.getWorkSheet().setColumnView(2, 30);	
		 
		    	
		    	
				jExcel.addDate(0, 0,"id",jExcel.dateTITLEFormat);
				jExcel.addDate(1, 0,"语言",jExcel.dateTITLEFormat);
				jExcel.addDate(2, 0,"播向区",jExcel.dateTITLEFormat);
				
				jExcel.getWorkSheet().setName("语言信息");
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
	
	/**
	 * <p>class/function:com.viewscenes.framework.service.report.am
	 * <p>explain:导出语言报表(处理非模板Excel的情况)
	 * <p>author-date:常海涛 Feb 28, 2012
	 * @param:
	 * @return:
	 */
//	public void getLanguageTxt(String msg, HttpServletRequest request,
//			HttpServletResponse response){
//		
//		String settabname="zdic_language_tab";
//		String setcolname ="";
//		String setvalue ="";
//		String sql="";
//		ZdicLanguageBean conditonbean = new ZdicLanguageBean();//定义条件bean
//		conditonbean.setStartRow(1);
//		conditonbean.setEndRow(400);
//		ArrayList<ASObject> languageList = new ArrayList<ASObject>();
//		HashMap obj=(HashMap)new DicService().getLanguage(conditonbean);
//		if(obj.get("resultList") instanceof ArrayList){//如果返回正常的结果集
//		  languageList=(ArrayList)obj.get("resultList");
//		
//			 for (int i = 0; i < languageList.size(); i++) {
//				 ASObject obj1= (ASObject)languageList.get(i);
//				 Iterator it = obj1.entrySet().iterator();
//				 String sKey="";
//				 String sValue="";
//				 setcolname ="";
//				 setvalue ="";
//				 while (it.hasNext()) {
//					  Map.Entry entry = (Map.Entry) it.next();
//					   sKey = (String) entry.getKey();
//					   sValue = (String) entry.getValue();
//					   if(sKey.equals("ora_rc")){
//						  
//					   }else{
//							 setcolname += sKey+',';
//							 setvalue += sValue+','; 
//						
//					   }
//				 }
//				 
//				 
//				 if(setcolname.equals("")){
//					 
//				 }else{
//					
//					setcolname = setcolname.substring(0,setcolname.length()-1);
//					setvalue = setvalue.substring(0,setvalue.length()-1);
//				   sql +="insert into "+settabname+"("+setcolname+") values("+setvalue+")"+ "\r\n";
//				 }
//          }
//			
//			 
//	    }
//    	 try {
//			ReadWriteFile.writeTxtFile(sql,"d:/test.txt");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	
//	}
	
	
//	public void getLanguageTxt(String msg, HttpServletRequest request,
//			HttpServletResponse response){
//		String sql = "select * from zdic_language_tab where is_delete =0  order by language_name desc ";
//		String tab = "zdic_language_tab";
//		Txt Txtt = new Txt();
//		 try {
//			Txtt.getTxt(sql,tab,"语言");
//		} catch (DbException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (GDSetException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
