package com.viewscenes.web.log;

import java.io.OutputStream;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.jdom.Element;
import org.jmask.web.controller.EXEException;
import com.viewscenes.bean.ShiftLogBean;
import com.viewscenes.bean.zrst_rep_quality_tab;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.printexcel.JExcel;
import com.viewscenes.pub.GDSet;
import com.viewscenes.pub.GDSetException;
import com.viewscenes.util.StringTool;

import flex.messaging.io.amf.ASObject;


public class ShiftLog {
	/**
	 * ************************************************
	* @Title: getLogList
	* @Description: TODO(������Ч�ڲ�ѯ���Ӱ���־)
	* @param @param ShiftLogBean
	* @param @return    �趨�ļ�
	* @author  ����
	* @return Object    ��������
	* @throws
	
	************************************************
	 */
	    public Object getLogList(ShiftLogBean obj) {
	    	ASObject objRes = new ASObject();
	         String valid_start_datetime =obj.getStart_time();
	         String valid_end_datetime = obj.getEnd_time();
	    
	           ArrayList<ShiftLogBean> list = new ArrayList();
	           GDSet gdSet = null;
	           GDSet gdSet2 = null;
		      

	       String loglistsql="select  t.id, t.from_userid,   t.create_time, t.description, t.start_time, t.end_time, t.from_username,  t.douser_name " +
	       		" from RES_SHIFT_LOG_TAB t" +
	       		" where  t.create_time>=to_date('"+valid_start_datetime+"','yyyy-mm-dd hh24:mi:ss') and   t.create_time <=to_date('"+valid_end_datetime+"','yyyy-mm-dd hh24:mi:ss') and t.is_delete=0  " +
	       		"order by t.create_time desc ";
	        	String totalsql="select count(*) count" +
	       		" from RES_SHIFT_LOG_TAB t" +
	       		" where  t.create_time>=to_date('"+valid_start_datetime+"','yyyy-mm-dd hh24:mi:ss') and   t.create_time <=to_date('"+valid_end_datetime+"','yyyy-mm-dd hh24:mi:ss') and t.is_delete=0  " ;
		       	
	         try {
	        	
	        	  Integer startRow = (Integer)obj.getStartRow();
     			  Integer endRow = (Integer)obj.getEndRow();
	        		gdSet = DbComponent.Query(StringTool.pageSql(loglistsql.toString(),startRow,endRow));
					gdSet2= DbComponent.Query(totalsql);
				   for (int i = 0; i < gdSet.getRowCount(); i++) {
					   ShiftLogBean task=new ShiftLogBean();
					    task.setId(gdSet.getString(i, "id"));
                        task.setStart_time(gdSet.getString(i, "start_time"));
					    task.setCreate_time(gdSet.getString(i, "create_time"));
					    task.setDescription(gdSet.getString(i, "description"));
					    task.setFrom_userid(gdSet.getString(i, "from_userid"));
					    task.setFrom_username(gdSet.getString(i, "from_username"));
					    task.setEnd_time(gdSet.getString(i, "end_time"));
					    task.setDouser_name(gdSet.getString(i, "douser_name"));
			          
					    list.add(task); 
			         }
				   
				   
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new EXEException("", "��ѯ���Ӱ���־��Ϣʧ�ܣ�"+ e.getMessage(),null);
			} catch (GDSetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new EXEException("", "��ѯ���Ӱ���־��Ϣʧ�ܣ�"+ e.getMessage(),null);
			}
			objRes.put("resultList", list);
			try {
				objRes.put("resultTotal", gdSet2.getString(0, "count"));
			} catch (GDSetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new EXEException("", "��ѯ���Ӱ���־��Ϣʧ�ܣ�"+ e.getMessage(),null);
			}
	         return objRes;
	    }
	    /**
         * ************************************************
        
        * @Title: 
        
        * @Description: TODO(ɾ�����Ӱ���־����)
        
        * @param @param msg
        * @param @return
        * @param @throws DbException
        * @param @throws GDSetException    �趨�ļ�
        
        * @author  ����
        
        * @return String    ��������
        
        * @throws
        
        ************************************************
         */
public Object delLogList(ArrayList logList) {

   String log_ids="";
    for (int i = 0; i < logList.size(); i++) {
    	ShiftLogBean log=(ShiftLogBean) logList.get(i);
        String log_id =log.getId() ;
       
        	log_ids+=log_id+",";
       }
    log_ids=log_ids.substring(0,log_ids.length()-1);
	String sql = "update RES_SHIFT_LOG_TAB t set t.is_delete=1 where t.id in("+ log_ids + ")";
	                            
	   try {
		DbComponent.exeUpdate(sql.toString());
	} catch (DbException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return new EXEException("", "ɾ�����Ӱ���־ʧ�ܣ�"+ e.getMessage(),null);
	}

  return "";
  }

/**
 * ************************************************

* @Title: 

* @Description: TODO(�������޸Ľ��Ӱ���־����)

* @param @param msg
* @param @return
* @param @throws DbException
* @param @throws GDSetException    �趨�ļ�

* @author  ����

* @return String    ��������

* @throws

************************************************
 */
public Object addAndUpdateLogList(ShiftLogBean bean) {
		
	 String id=bean.getId();
	 String from_userid=bean.getFrom_userid();
	 String description=bean.getDescription();
	 String from_username=bean.getFrom_username();
	 String starttime=bean.getStart_time();
	 String endtime=bean.getEnd_time();
	 String douser_name=bean.getDouser_name();
	 String sql ="";
       if(id.length()!=0)
       {	  
    		   sql=" update  RES_SHIFT_LOG_TAB t set t.from_userid='" +from_userid+
   	   		"' ,t.create_time=sysdate"+
   	   		",t.from_username='" +from_username+
   	   		"' ,t.description= '"+description+
   	   		"' ,start_time= '"+starttime+
   	   		"' ,end_time= '"+endtime+
   	   		"' where t.id="+id;


       }else 
       {
    	   sql="insert into RES_SHIFT_LOG_TAB(id,from_userid,create_time,description,start_time,end_time,from_username)" +
    	   		" values(RES_RESOURSE_SEQ.Nextval,'"+from_userid+"',sysdate"
    	   		+",'"+description+"','"+starttime+"','"+endtime+"','"+from_username+"')";
       }
                        
		try {
		    DbComponent.exeUpdate(sql.toString());
		} catch (DbException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return new EXEException("", "���½��Ӱ���־ʧ�ܣ�"+ e.getMessage(),null);
		}
		
		return "���½��Ӱ���־�ɹ���";
}
public Object updateLogDouser(ShiftLogBean bean) {
	
	 String id=bean.getId();

	 String douser_name=bean.getDouser_name();
	 String sql ="";

   		   sql=" update  RES_SHIFT_LOG_TAB t set" +
  	   		" douser_name= '"+douser_name+
  	   		"' where t.id="+id;             
		try {
		    DbComponent.exeUpdate(sql.toString());
		} catch (DbException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return new EXEException("", "���½��Ӱ���־ʧ�ܣ�"+ e.getMessage(),null);
		}
		
		return "���½��Ӱ���־�ɹ���";
}
/**
 * ************************************************

* @Title: doExcel

* @Description: TODO(����������־)

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
			
			String starttime = root.getChildText("starttime");
			String endtime = root.getChildText("endtime");

			String fileName="���Ӱ���Ϣ��ϸ";
			if(starttime.equals(endtime)){
				fileName = "���Ӱ���Ϣ"+starttime+"��ϸ";
			} else 
			{
				fileName = "���Ӱ���Ϣ("+starttime.substring(0, 11)+"��"+endtime.substring(0, 11)+")��ϸ";
			} 
			String descSql="select * from RES_SHIFT_LOG_TAB t where t.is_delete=0 and " +
					" t.create_time>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss')and " +
							"t.create_time<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') order by t.create_time desc  ";
			GDSet gdSet=DbComponent.Query(descSql);
		
			JExcel jExcel = new JExcel();
			OutputStream outputStream = response.getOutputStream();
			response.setContentType("application/vnd.ms-excel");
			response.reset();
			response.setHeader("Location", "Export.xls");
			response.setHeader("Expires", "0");
            response.setHeader("Content-disposition", "attachment; filename="+new String((fileName).getBytes("GBK"),"ISO-8859-1")+".xls");
	        
			WritableWorkbook wwb =Workbook.createWorkbook(outputStream);
			int sheetNum = 0;
			WritableSheet ws =  null;
			ws = wwb.createSheet("���Ӱ���Ϣ��ϸ", wwb.getNumberOfSheets()+1);//��һ
		
		    WritableFont wf = new WritableFont(WritableFont.createFont("����"), 10,WritableFont.BOLD, false);
			WritableCellFormat wcfF = new WritableCellFormat(wf);
		    wcfF.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);//���߿� 
            wcfF.setWrap(true);//�Զ�����
             // ��ˮƽ���뷽ʽָ��Ϊ�����
             wcfF.setAlignment(jxl.format.Alignment.CENTRE);
             // �Ѵ�ֱ���뷽ʽָ��Ϊ���ж���
             wcfF.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
             //��������

             ws.addCell(new Label(0,1,"¼����",wcfF));
             ws.addCell(new Label(1,1,"¼������",wcfF));
             ws.addCell(new Label(2,1,"���Ӱ࿪ʼ����",wcfF));
             ws.addCell(new Label(3,1,"���Ӱ��������",wcfF));
             ws.addCell(new Label(4,1,"������",wcfF));
             ws.addCell(new Label(5,1,"��ϸ",wcfF));
             ws.mergeCells(0,0,5,0);
             
      
             wf = new WritableFont(WritableFont.createFont("����"), 12,WritableFont.BOLD, false);
				 wcfF = new WritableCellFormat(wf);
				 wcfF.setAlignment(jxl.format.Alignment.CENTRE);
             // �Ѵ�ֱ���뷽ʽָ��Ϊ���ж���
             wcfF.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
         
             ws.addCell(new Label(0, 0, "���Ӱ���Ϣ��ϸ",wcfF));
           
             ws.setRowView(0, 740);//���õ�һ�и߶�
             ws.setRowView(1, 740);

             ws.setColumnView(0, 20);
             ws.setColumnView(1, 30);
             ws.setColumnView(2, 30);
             ws.setColumnView(3, 30);
             ws.setColumnView(4, 20);
             ws.setColumnView(5, 70);

         	WritableFont wf1 = new WritableFont(WritableFont.createFont("����"), 10,WritableFont.NO_BOLD, false);
            //����CellFormat
            jxl.write.WritableCellFormat wcfF2 = new jxl.write.WritableCellFormat(wf1);
            // ��ˮƽ���뷽ʽָ��Ϊ�����
            wcfF2.setAlignment(jxl.format.Alignment.CENTRE);
            // �Ѵ�ֱ���뷽ʽָ��Ϊ���ж���
            wcfF2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
            wcfF2.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); 
            int flag=0;
           
			for(int i=0;i<gdSet.getRowCount();i++){
        	
	        	int curRow = ws.getRows();
	        	ws.addCell(new Label(0,curRow, gdSet.getString(i, "from_username"),wcfF2));
	        	ws.addCell(new Label(1,curRow, gdSet.getString(i, "create_time"),wcfF2));
	        	ws.addCell(new Label(2,curRow, gdSet.getString(i, "start_time"),wcfF2));
	        	ws.addCell(new Label(3,curRow, gdSet.getString(i, "end_time"),wcfF2));
	         	ws.addCell(new Label(4,curRow, gdSet.getString(i, "douser_name"),wcfF2));
	         	ws.addCell(new Label(5,curRow, gdSet.getString(i, "description"),wcfF2));
	

		    }
	
			
				wwb.write();
		        wwb.close();
		        outputStream.close();



		} 
}


