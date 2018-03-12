package com.viewscenes.web.devicemgr.equmaintain;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


import org.jdom.Element;
import org.jmask.web.controller.EXEException;

import com.viewscenes.bean.EquipmentMaintenanceBean;
import com.viewscenes.dao.XmlReader;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.printexcel.JExcel;
import com.viewscenes.pub.GDSet;
import com.viewscenes.pub.GDSetException;
import com.viewscenes.util.StringTool;

import flex.messaging.io.amf.ASObject;

public class EquipmentMaintenance{
	/**
	 * @throws Exception 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws SecurityException 
	 * @throws GDSetException 
	 * @throws DbException 
	 * ************************************************
	
	* @Title: doReport
	
	* @Description: TODO(վ������ͳ��)
	
	* @param @param starttime
	* @param @param endtime
	* @param @param type 
	* @param @param report_type
	* @param @return    �趨�ļ�
	
	* @author  ����
	
	* @return ArrayList    ��������
	
	* @throws
	
	************************************************
	 */
	 public Object doReport(ASObject obj) throws Exception {
		    ASObject resObj;
		    
		    String head_name = "'"+((String)obj.get("head_name")).replaceAll(",", "','")+"'";
		    String starttime = (String)obj.get("starttime");
		    String endtime = (String)obj.get("enddate");
	       String totalsql="select DATA_ID,REPORT_ID,HEAD_NAME,HEAD_CODE,   to_char(STARTTIME,'yyyy-mm-dd') STARTTIME," +
	       		" to_char(ENDTIME,'yyyy-mm-dd') ENDTIME,COUNT,TYPE,TYPE_NAME,HANDLE,HANDLE2,REMARK,IS_DELETE from zrst_rep_equipment_tab t  where is_delete=0 and ((t.starttime<=to_date('"+endtime+"','yyyy-mm-dd') and t.starttime>=to_date('"+starttime+"','yyyy-mm-dd')) or (t.endtime>=to_date('"+starttime+"','yyyy-mm-dd') and t.endtime<=to_date('"+endtime+"','yyyy-mm-dd')))";	        
	      if(head_name.length()>2)
	      {
	    	  totalsql=totalsql+" and head_name in("+head_name+")";
	      }
	      totalsql=totalsql+"order by ENDTIME desc ";
	       resObj = StringTool.pageQuerySql(totalsql, obj);
 
   return resObj;
}
/**
 * ************************************************

* @Title: delEquList

* @Description: TODO(ɾ��)

* @param @param asObj
* @param @return    �趨�ļ�

* @author  ����

* @return Object    ��������

* @throws

************************************************
 */
		public Object del(String ids){
			String message="ɾ��վ��������Ϣ�ɹ�!";
			String sql="update zrst_rep_equipment_tab set is_delete=1 where  data_id in("+ids+")";
			try {
				DbComponent.exeUpdate(sql);
			} catch (Exception e) {
				e.printStackTrace();
				return new EXEException("","ɾ��վ��������Ϣ�쳣"+e.getMessage(),"");
			}
			return message;
		}
	 /**
	  * ************************************************
	 
	 * @Title: add
	 
	 * @Description: TODO(���վ������ά��)
	 
	 * @param @param bean
	 * @param @return    �趨�ļ�
	 
	 * @author  ����
	 
	 * @return Object    ��������
	 
	 * @throws
	 
	 ************************************************
	  */
	 public Object add(EquipmentMaintenanceBean bean){
			String res="���վ��������Ϣ�ɹ�!";
			String DATA_ID = bean.getData_id();
			
			String headendname=bean.getHead_name();
					
			if(headendname.endsWith("A")||headendname.endsWith("B")||headendname.endsWith("C")||headendname.endsWith("D")||headendname.endsWith("E")||headendname.endsWith("F")||headendname.endsWith("G"))
				headendname = headendname.substring(0, headendname.length()-1);
								

			String HEAD_CODE=bean.getHead_code();
			String STARTTIME=bean.getStarttime();
			String ENDTIME=bean.getEndtime();
			String COUNT=bean.getCount();
			String TYPE=bean.getType();
			String TYPE_NAME=bean.getType_name();
			String HANDLE=bean.getHandle();
			String HANDLE2=bean.getHandle2();
			String REMARK=bean.getRemark();
			String equ=bean.getEqu();
			String advice=bean.getAdvice();

			
			String  sql="insert into zrst_rep_equipment_tab (DATA_ID,REPORT_ID,HEAD_NAME,HEAD_CODE,STARTTIME,ENDTIME,COUNT,TYPE,TYPE_NAME,HANDLE,HANDLE2,REMARK,IS_DELETE,equ,advice)" +
					" values(SEC_SEQ.nextval,null,'"+headendname+"','"+HEAD_CODE+"','"+STARTTIME+"'," +
							"'"+ENDTIME+"',"+COUNT+" ,'"+TYPE+"','"+TYPE_NAME+"','"+HANDLE+"','"+HANDLE2+"','"+REMARK+"',0,'"+equ+"','"+advice+"')";
			try {
				DbComponent.exeUpdate(sql);
			} catch (Exception e) {
				e.printStackTrace();
				return new EXEException("","���վ��������Ϣ�쳣��"+e.getMessage(),"");
			}
			
			return res;
		}
		
/**
 * ************************************************

* @Title: doExcel

 /**
	  * ************************************************

	 * @Title: doExcel

	 * @Description: TODO(�������̨����)

	 * @param @param msg
	 * @param @param request
	 * @param @param response
	 * @param @throws Exception    �趨�ļ�

	 * @author  ����

	 * @return void    ��������

	 * @throws

	 ************************************************
	  */
	 	 public void doExcel(String msg,HttpServletRequest request,HttpServletResponse response) throws Exception{
	 			Element root = StringTool.getXMLRoot(msg);
	 			String head_names = "'"+(root.getChildText("head_name")).replaceAll(",", "','")+"'";
	 			String starttime = root.getChildText("starttime");
	 			String endtime = root.getChildText("endtime");

		       String totalsql="select DATA_ID,REPORT_ID,HEAD_NAME,HEAD_CODE,   to_char(STARTTIME,'yyyy-mm-dd') STARTTIME," +
		       		" to_char(ENDTIME,'yyyy-mm-dd') ENDTIME,COUNT,TYPE,TYPE_NAME,HANDLE,HANDLE2,REMARK,IS_DELETE ,equ,advice from zrst_rep_equipment_tab t  where is_delete=0 and ((t.starttime<=to_date('"+endtime+"','yyyy-mm-dd') and t.starttime>=to_date('"+starttime+"','yyyy-mm-dd')) or (t.endtime>=to_date('"+starttime+"','yyyy-mm-dd') and t.endtime<=to_date('"+endtime+"','yyyy-mm-dd')))";	        
		      if(head_names.length()>2)
		      {
		    	  totalsql=totalsql+" and head_name in("+head_names+")";
		      }
		      totalsql=totalsql+"order by ENDTIME desc ";

	 			String fileName="����վ������ͳ��";
	 			String downFileName="����վ������ͳ��";
	 			if(starttime.substring(0,10).equals(endtime.substring(0, 10))){
	 				downFileName=fileName = "����վ��"+starttime+"������ͳ��";
	 			} else 
	 			{
	 				downFileName=fileName = "����վ��("+starttime+"��"+endtime+")������ͳ��";
	 			} 
	 			 try {
	 	        	JExcel jExcel = new JExcel();
	 	        	downFileName=jExcel.openDocument();
	 				jExcel.WorkBookGetSheet(0);
	 				GDSet gd = DbComponent.Query(totalsql);
	 				if(gd.getRowCount()>0){
	 					for(int i=0;i<gd.getRowCount();i++){
	 						jExcel.addDate(0, i+1,gd.getString(i, "HEAD_NAME"),jExcel.dateCellFormat);
	 						jExcel.addDate(1, i+1,gd.getString(i, "HEAD_CODE"),jExcel.dateCellFormat);
	 						jExcel.addDate(2, i+1,gd.getString(i, "STARTTIME"),jExcel.dateCellFormat);
	 						jExcel.addDate(3, i+1,gd.getString(i, "ENDTIME"),jExcel.dateCellFormat);
	 						jExcel.addDate(4, i+1,gd.getString(i, "COUNT"),jExcel.dateCellFormat);
	 						jExcel.addDate(5, i+1,gd.getString(i, "TYPE"),jExcel.dateCellFormat);
	 						jExcel.addDate(6, i+1,gd.getString(i, "equ"),jExcel.dateCellFormat);				
	 						jExcel.addDate(7, i+1,gd.getString(i, "TYPE_NAME"),jExcel.dateCellFormat);
	 						jExcel.addDate(8, i+1,gd.getString(i, "HANDLE"),jExcel.dateCellFormat);
	 						jExcel.addDate(9, i+1,gd.getString(i, "advice"),jExcel.dateCellFormat);
	 						jExcel.addDate(10, i+1,gd.getString(i, "REMARK"),jExcel.dateCellFormat);
	 						jExcel.getWorkSheet().setRowView(i+1, 600);
	 					}
	 				
	 				}
	 				jExcel.getWorkSheet().setRowView(0, 600);
	 				jExcel.getWorkSheet().setColumnView(0, 15);
	 		    	jExcel.getWorkSheet().setColumnView(1, 10);
	 		    	jExcel.getWorkSheet().setColumnView(2, 15);	
	 		    	jExcel.getWorkSheet().setColumnView(3, 15);
	 		    	jExcel.getWorkSheet().setColumnView(4, 15);
	 		    	jExcel.getWorkSheet().setColumnView(5, 20);	
	 		    	jExcel.getWorkSheet().setColumnView(6, 40);	
	 		    	jExcel.getWorkSheet().setColumnView(7, 40);	
	 		    	jExcel.getWorkSheet().setColumnView(8, 40);
	 		    	jExcel.getWorkSheet().setColumnView(9, 40);
	 		    	jExcel.getWorkSheet().setColumnView(9, 40);
	 		    	
	 		    	jExcel.addDate(0, 0,"վ������",jExcel.dateTITLEFormat);
	 		    	jExcel.addDate(1, 0,"վ��CODE",jExcel.dateTITLEFormat);
	 		    	jExcel.addDate(2, 0,"���Ͽ�ʼʱ��",jExcel.dateTITLEFormat);
	 		    	jExcel.addDate(3, 0,"���Ͻ���ʱ��",jExcel.dateTITLEFormat);
	 		    	jExcel.addDate(4, 0,"�ۼ�ʱ��(��)",jExcel.dateTITLEFormat);
	 		    	jExcel.addDate(5, 0,"��������",jExcel.dateTITLEFormat);
	 		    	jExcel.addDate(6, 0,"�����豸",jExcel.dateTITLEFormat);
	 		    	jExcel.addDate(7, 0,"�������",jExcel.dateTITLEFormat);
	 		    	jExcel.addDate(8, 0,"�������",jExcel.dateTITLEFormat);
	 		    	jExcel.addDate(9, 0,"����",jExcel.dateTITLEFormat);
	 		    	jExcel.addDate(10, 0,"��ע",jExcel.dateTITLEFormat);
	 		  
	 		    	jExcel.getWorkSheet().setName("����վ������ͳ��");
	 				jExcel.saveDocument();
	 		    	response.setContentType("application/vnd.ms-excel");
	 		        response.setHeader("Location", "Export.xls");
	 		        response.setHeader("Expires", "0");
	 		        response.setHeader("Content-Disposition",new String(("filename="+fileName).getBytes("GBK"),"ISO-8859-1")+".xls");
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
	 				
	 			} catch (Exception e) {
	 				// TODO Auto-generated catch block
	 				e.printStackTrace();
	 			}
	 	        
	 			
	 		}


	 		
	public static String getNewString(String oldString,String findstring,String value1,String value2)
	 {
		 String newStr="";
		 if(oldString.indexOf(findstring)!=-1)
		 {
			 String[] type=oldString.split("&&");
			 for(int i=0;i<type.length;i++)
			 {
				 String[] times=type[i].split(",");
				 if(times.length==3)
				 {
					  if(times[0].equals(findstring))
					  {
						  String count=(Integer.parseInt(times[1])+Integer.parseInt(value1))+"";
							 String time=(Double.parseDouble(times[2])+Double.parseDouble(value2))+"";
							 newStr+=findstring+","+count+","+time+"&&";
					  }else 
					  {
						  newStr+=type[i].toString()+"&&";
					  }
				 }else if(times.length==2)
				 {
					
					 String count=(Integer.parseInt(times[0])+Integer.parseInt(value1))+"";
					 String time=(Double.parseDouble(times[1])+Double.parseDouble(value2))+"";
					 newStr+=count+","+time; 
				 }
		
			 }
		 }else
		 {
			 return oldString;
		 }
		 return newStr;
	 }
	 public static  void addcell(WritableSheet ws2, String key, String value,WritableCellFormat wcfF2) throws RowsExceededException, WriteException
	 {
		 int curRow = ws2.getRows();
        	ws2.addCell(new Label(0,curRow, key,wcfF2));
			String[] type=value.split("&&");
			 for(int i=0;i<type.length;i++)
			 {
				 
				 String[] times=type[i].split(",");
					 if(i==type.length-1)
					 {
						 ws2.addCell(new Label(i*2+1,curRow, times[0],wcfF2));
						 ws2.addCell(new Label(i*2+2,curRow, times[1],wcfF2));
					 }else
					 {
							 ws2.addCell(new Label(i*2+1,curRow, times[1],wcfF2));
							 ws2.addCell(new Label(i*2+2,curRow, times[2],wcfF2)); 
					 }
				 
			 }
		
	 }
	/**************************************************
	
	 * @Title: main
	
	 * @Description: TODO(������һ�仰�����������������)
	
	 * @param @param args    �趨�ļ�
	
	 * @author  ����
	
	 * @return void    ��������
	
	 * @throws
	
	 *************************************************/

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String sb2 = " �����ɶ���   http://10.15.6.12:8000/video/OAS07A_13869_20120918125457_20120918125527_639_R1.mp3";
        // http://10.15.6.12:8000/video/OAS07A_13860_20120918113913_20120918114008_97400_R1.zip
       String sb3 =  sb2.replaceAll("http:([\\S\\s]+?)video/", "http://10.15.6.122:8000/video/");	//���˿��滻Ϊ"$"
       System.out.println(sb3);
      // XmlReader.getAttrValue(itemName, childName, atrrName)
       String ip =XmlReader.getAttrValue("HttpIp", "ip", "value");
       System.out.println(ip);
	}

	
}
