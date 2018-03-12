package com.viewscenes.web.devicemgr.equmaintain;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.viewscenes.bean.devicemgr.RadioEquBean;
import com.viewscenes.dao.database.DbComponent;

import com.viewscenes.printexcel.JExcel;
import com.viewscenes.pub.GDSet;
import com.viewscenes.sys.SystemConfig;
import com.viewscenes.util.LogTool;

import edu.emory.mathcs.backport.java.util.Arrays;
import flex.messaging.io.amf.ASObject;
import com.viewscenes.util.StringTool;
import com.viewscenes.web.devicemgr.equinstall.EquInstallManager;

import org.jdom.Element;
import org.jmask.web.controller.EXEException;

public class EquMaintain {
	public EquMaintain() {
		
    }
	
	public Object getEquManageList(ASObject obj){
		
		
	    ASObject resObj;
	    String type_id = (String)obj.get("type_id");
	 
	    String head_name = "'"+((String)obj.get("head_name")).replaceAll(",", "','")+"'";
	    String starttime = (String)obj.get("starttime");
	    String enddate = (String)obj.get("enddate");
	
	    
	    String tablename="RADIO_EQU_MAIN_TAB";

        String sql= "";
            sql +=  "select t.* ,h.country ,d.state_name from RADIO_EQU_MAIN_TAB t,res_headend_tab h,dic_state_tab d where  t.head_code=h.code and h.state=d.state and h.type_id = '"+type_id+"'" ;

//        if(isresumed.length()>0)
//        	 sql += " and is_resume='"+isresumed+"' ";
//        
//        if(ishandled.length()>0)
//        	sql += " and is_handle='"+ishandled+"' ";
        
     

//        if(etype.length()>0)
//        	sql +=" and type='"+etype+"' ";
        
        if(starttime.length()>0)
            sql +=" and MAIN_DATETIME >= to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss')";

        if(enddate.length()>0)
            sql +=" and MAIN_DATETIME <= to_date('"+enddate+"','yyyy-mm-dd hh24:mi:ss')";

        if(head_name.length()>2)
        {
        
        	
        	 sql +=" and head_name in( "+head_name+")";

        }

         sql +=" order by MAIN_DATETIME desc ";
        
        try {
			resObj = StringTool.pageQuerySql(sql, obj);
		} catch (Exception e) {
			LogTool.fatal(e);
			return new EXEException("",e.getMessage(),"");
		}
    	
    	return resObj;
   }
	public Object queryEquManageList(ASObject obj){
		
		
	    ASObject resObj;
	    String type_id = (String)obj.get("type_id");
	 
	    String head_name = "'"+((String)obj.get("head_name")).replaceAll(",", "','")+"'";
	    String starttime = (String)obj.get("starttime");
	    String enddate = (String)obj.get("enddate");
	    String equ_id=(String)obj.get("equ_id");
	    

        String sql= "";
            sql +=  "select t.*,h.country ,d.state_name from RADIO_EQU_MAIN_TAB t,res_headend_tab h ,dic_state_tab d where  t.head_code=h.code and h.state=d.state and  h.type_id = '"+type_id+"'" ;

//        if(isresumed.length()>0)
//        	 sql += " and is_resume='"+isresumed+"' ";
//        
//        if(ishandled.length()>0)
//        	sql += " and is_handle='"+ishandled+"' ";
        
     

//        if(etype.length()>0)
//        	sql +=" and type='"+etype+"' ";
        
        if(starttime.length()>0)
            sql +=" and t.start_time >= to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss')";

        if(enddate.length()>0)
            sql +=" and t.end_time <= to_date('"+enddate+"','yyyy-mm-dd hh24:mi:ss')";

        if(head_name.length()>2)
        {
        
        	
        	 sql +=" and head_name in( "+head_name+")";

        }
        if(equ_id!=null&&!equ_id.equals("全部")){
        	sql+=" and equ_id ='"+equ_id+"'";
        }
         sql +=" order by MAIN_DATETIME desc ";
        
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
	 * <p>explain:删除前端数据，如果添加出错会返回错误信息
	 * <p>author-date:张文 2012-7-26
	 * @param:
	 * @return:
	 */
    public Object delEquList(ASObject asObj){
    	 
		String dellist=(String)asObj.get("dellist");
	
		String tablename="";
        
        tablename="RADIO_EQU_MAIN_TAB";
        String sql ="";
   
		try{
			
			sql = "delete  from " + tablename + "  where id in("+dellist+")";
			
			DbComponent.exeUpdate(sql);
			
		 }catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("","后台错误!",null);
		}
		 return " ";
	}

    
    /**
	 * 添加数据入库 
	 * @param bean
	 * @return
	 * @throws Exception 
	 */
	public Object addEqu(RadioEquBean bean){
		String res="添加设备维护信息成功!";
		String id = bean.getId();
		String head_id = bean.getHead_id();
		String installation = bean.getInstallation();
		String use_case = bean.getUse_case();
		
		String fault = bean.getFault();
		String replacement = bean.getReplacement();
		String maintain = bean.getMaintain();
		String equ_number = bean.getEqu_number();
		
		String equ_id = bean.getEqu_id();
		String head_code = bean.getHead_code();
		String main_datetime = bean.getMain_datetime();
		String head_name=bean.getHead_name();
		String pic_path="uploadpicture/"+bean.getPic_path();

		
		String  sql="insert into RADIO_EQU_MAIN_TAB (id,head_id,installation,use_case,fault,replacement,maintain,equ_number,equ_id,head_code," +
				"main_datetime,head_name,pic_path) values(SEC_SEQ.nextval,'"+head_id+"','"+installation+"','"+use_case+"','"+fault+"'," +
						"'"+replacement+"','"+maintain+"' ,'"+equ_number+"','"+equ_id+"','"+head_code+"',to_date('"+main_datetime+"','yyyy-mm-dd hh24:mi:ss'),'"+head_name+"','"+pic_path+"')";
		try {
			DbComponent.exeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return new EXEException("","添加设备维护信息异常："+e.getMessage(),"");
		}
		
		return res;
	}
	public Object insertEqu(ASObject obj){
		String res="添加设备维护信息成功!";
		
		String head_id = (String)obj.get("head_id");
//		String installation = (String)obj.get("head_id");
//		String use_case = (String)obj.get("head_id");
		
		String fault =(String)obj.get("fault");
//		String replacement = (String)obj.get("head_id");
		String maintain = (String)obj.get("maintain");
//		String equ_number =(String)obj.get("head_id");;
		
		String equ_id = (String)obj.get("equ_id");
		String head_code = (String)obj.get("head_code");
//		String main_datetime = (String)obj.get("head_id");
		String head_name=(String)obj.get("head_name");
//		String pic_path="uploadpicture/"+bean.getPic_path();
		String start_time=(String)obj.get("start_time");
		String end_time=(String)obj.get("end_time");
		String person=(String)obj.get("person");
		String resault=(String)obj.get("resault");
		String record_path=(String)obj.get("record_path");
		String old_equ=(String)obj.get("old_equ");
		String old_place=(String)obj.get("old_place");
		String old_status=(String)obj.get("old_status");
		String new_equ=(String)obj.get("new_equ");
		String new_from=(String)obj.get("new_from");
		String mail_order=(String)obj.get("mail_order");
		String mail_time=(String)obj.get("mail_time");
		String  sql="insert into RADIO_EQU_MAIN_TAB (id,head_id,fault,maintain,equ_id,head_code," +
				"start_time,end_time,head_name,person,resault,record_path,old_equ,old_place,old_status" +
				",new_equ,new_from,mail_order,mail_time) values(SEC_SEQ.nextval,'"+head_id+"','"+fault+"','" +
				maintain+"','"+equ_id+"','"+head_code+"',to_date('"+start_time+"','yyyy-mm-dd hh24:mi:ss'),to_date('"+end_time+"','yyyy-mm-dd hh24:mi:ss'),'"+head_name+
				"','"+person+"','"+resault+"','"+record_path+"','"+old_equ+"','"+old_place+"','"+old_status+"','"+
				new_equ+"','"+new_from+"','";
		
		if(new_from!=null&&new_from.equals("0")){
			sql+=mail_order+"',to_date('"+mail_time+"','yyyy-mm-dd hh24:mi:ss'))";
		}else{
			sql+="',null)";
		}
		try {
			String[] sqls=new String[3];
			String id=EquInstallManager.getEquInstallId(head_name, old_equ, equ_id);
			String id1=EquInstallManager.getEquInstallId(head_name, new_equ, equ_id);
			if(id!=null&&id.length()>0){
				sqls[0]=EquInstallManager.getUpdateEquSql(id, old_status, old_place, "", "",head_name);
			}else{
				sqls[0]=EquInstallManager.getInsertEquSql(head_name, head_id, old_equ, equ_id, old_status, old_place, "", "");
			}
			if(id1!=null&&id1.length()>0){
				sqls[1]=EquInstallManager.getUpdateEquSql(id1, "0", "0", mail_time, mail_order,head_name);
			}else{
				sqls[1]=EquInstallManager.getInsertEquSql(head_name, head_id, new_equ, equ_id, "0", "0", mail_time, mail_order);
			}
			sqls[2]=sql;
			DbComponent.exeBatch(sqls);
		} catch (Exception e) {
			e.printStackTrace();
			return new EXEException("","添加设备维护信息异常："+e.getMessage(),"");
		}
		
		return res;
	}
	/**
	 * 修改数据入库 
	 * @param bean
	 * @return
	 * @throws Exception 
	 */
	public Object updatEqu(RadioEquBean bean){
		String res="修改设备维护信息成功!";
		String id = bean.getId();
		String head_id = bean.getHead_id();
		String installation = bean.getInstallation();
		String use_case = bean.getUse_case();
		
		String fault = bean.getFault();
		String replacement = bean.getReplacement();
		String maintain = bean.getMaintain();
		String equ_number = bean.getEqu_number();
		
		String equ_id = bean.getEqu_id();
		String head_code = bean.getHead_code();
		String main_datetime = bean.getMain_datetime();
		String pic_path="uploadpicture/"+bean.getPic_path();
		String  sql="update RADIO_EQU_MAIN_TAB set head_id='"+head_id+"',installation = '"+installation+"',use_case='"+use_case+"',fault='"+fault+"'," +
				"replacement='"+replacement+"',maintain='"+maintain+"' ,equ_number='"+equ_number+"', equ_id='"+equ_id+"'," +
				"head_code='"+head_code+"',main_datetime=to_date('"+main_datetime+"','yyyy-mm-dd hh24:mi:ss'),pic_path='"+pic_path+"'  where id ='"+id+"'";
		try {
			DbComponent.exeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return new EXEException("","修改设备维护信息异常："+e.getMessage(),"");
		}
		
		return res;
	}
	public Object updatEquBean(RadioEquBean bean){
		String res="修改设备维护信息成功!";
		String part="";
		String head_id = bean.getHead_id();

		
		String fault =bean.getFault();
		String maintain = bean.getMaintain();
		
		String equ_id = bean.getEqu_id();
		String head_code = bean.getHead_code();
		String head_name=bean.getHead_name();
		String start_time=bean.getStart_time();
		String end_time=bean.getEnd_time();
		String person=bean.getPerson();
		String resault=bean.getResault();
		String record_path=bean.getRecord_path();
		String old_equ=bean.getOld_equ();
		String old_place=bean.getOld_place();
		String old_status=bean.getOld_status();
		String new_equ=bean.getNew_equ();
		String new_from=bean.getNew_from();
		String mail_order=bean.getMail_order();
		String mail_time=bean.getMail_time();
		if(new_equ!=null&&new_equ.equals("0")){
			part+="',mail_order='"+mail_order+"',mail_time=to_date('"+mail_time+"','yyyy-mm-dd hh24:mi:ss') ";
		}else {
			part+="',mail_order='',mail_time =null ";
		}
		String  sql="update RADIO_EQU_MAIN_TAB set head_id='"+head_id+"',fault='"+fault+"'," +
				"maintain='"+maintain+"' ,person='"+person+"', equ_id='"+equ_id+"'," +
				"head_code='"+head_code+"',start_time=to_date('"+start_time+"','yyyy-mm-dd hh24:mi:ss')," +
				"end_time=to_date('"+end_time+"','yyyy-mm-dd hh24:mi:ss'),resault='"+resault+"'  " +
				", record_path='"+record_path+"',old_equ='"+old_equ+"',old_place='"+old_place+"',old_status='"+old_status+
				"',new_equ='"+new_equ+"',new_from='"+new_from+part+"where id ='"+bean.getId()+"'";
		try {
			String[] sqls=new String[3];
			String status="3";
			if(old_status.equals("0")){
				status="2";
			}
			String id=EquInstallManager.getEquInstallId(head_name, old_equ, equ_id);
			String id1=EquInstallManager.getEquInstallId(head_name, new_equ, equ_id);
			if(id==null||id.length()==0){
				sqls[0]=EquInstallManager.getInsertEquSql(head_name, head_id, old_equ, equ_id, status, old_place, "", "");
			}else{
				sqls[0]=EquInstallManager.getUpdateEquSql(id, status, old_place, "", "",head_name);
			}
			if(id1==null||id1.length()==0){
				sqls[1]=EquInstallManager.getInsertEquSql(head_name, head_id, new_equ, equ_id, "0", "0", mail_time, mail_order);
			}else{
				sqls[1]=EquInstallManager.getUpdateEquSql(id1, "0", "0", mail_time, mail_order,head_name);
			}
			sqls[2]=sql;
			DbComponent.exeBatch(sqls);
		} catch (Exception e) {
			e.printStackTrace();
			return new EXEException("","修改设备维护信息异常："+e.getMessage(),"");
		}
		
		return res;
	}
	/**
	 * 导出Excel
	 * @param msg
	 * @param request
	 * @param response
	 */
	public void doExcel(String msg,HttpServletRequest request,HttpServletResponse response){
		Element root = StringTool.getXMLRoot(msg);
		ArrayList<String> equList=new ArrayList<String>();
		HashMap<String, Integer> equMap=new HashMap<String, Integer>();
		int sum=0;
        String fileName="设备维护";
		String downFileName = "设备维护";
		
		 String type_id = root.getChildText("type_id");
		 String head_id = root.getChildText("head_id");
		 String starttime = root.getChildText("starttime");
		 String enddate =root.getChildText("enddate");
		 String equ_id=root.getChildText("equ_id");
		    
		 String tablename="";
	      
	    tablename="RADIO_EQU_MAIN_TAB";

	    String sql= "";
	            sql +=  "select a.* ,b.country,d.state_name from "+tablename+" a,res_headend_tab b ,dic_state_tab d where b.is_delete=0 and a.head_id=b.head_id and b.state=d.state and b.type_id='"+type_id+"'" ;

	        if(starttime.length()>0)
	            sql +=" and start_time >= to_date('"+starttime+"')";

	        if(enddate.length()>0)
	            sql +=" and end_time <= to_date('"+enddate+"') ";

	        if(head_id.length()>0)
	        {
	        	 sql +=" and a.head_code in("+head_id+")";

	        }
	        if(equ_id!=null&&!equ_id.equals("全部")){
	        	sql+=" and equ_id ='"+equ_id+"'";
	        }
	         sql +=" order by head_name,equ_id desc ";

		
        try {
        	JExcel jExcel = new JExcel();
			downFileName=jExcel.openDocument();
			jExcel.WorkBookGetSheet(0);
			GDSet gd = DbComponent.Query(sql);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					jExcel.addDate(0, i+1,gd.getString(i, "head_name"),jExcel.dateCellFormat);
					jExcel.addDate(1, i+1,gd.getString(i, "head_code"),jExcel.dateCellFormat);
					jExcel.addDate(2, i+1,gd.getString(i, "country"),jExcel.dateCellFormat);
					jExcel.addDate(3, i+1,gd.getString(i, "state_name"),jExcel.dateCellFormat);
					jExcel.addDate(4, i+1,gd.getString(i, "fault"),jExcel.dateCellFormat);
					String equId=gd.getString(i, "equ_id");
					if(equId==null||equId.length()==0){
						jExcel.addDate(5, i+1,"未更换",jExcel.dateCellFormat);
					}else{
						jExcel.addDate(5, i+1,equId,jExcel.dateCellFormat);
						if(equMap.get(equId)==null){
							equList.add(equId);
							equMap.put(equId, 1);
							sum++;
						}else{
							int a=equMap.get(equId);
							a++;
							sum++;
							equMap.put(equId, a);
						}
					}
					
					jExcel.addDate(6, i+1,gd.getString(i, "person"),jExcel.dateCellFormat);
					jExcel.addDate(7, i+1,gd.getString(i, "maintain"),jExcel.dateCellFormat);
					jExcel.addDate(8, i+1,gd.getString(i, "start_time"),jExcel.dateCellFormat);
					jExcel.addDate(9, i+1,gd.getString(i, "end_time"),jExcel.dateCellFormat);
					jExcel.addDate(10, i+1,gd.getString(i, "resault"),jExcel.dateCellFormat);
					jExcel.getWorkSheet().setRowView(i+1, 600);
				}
			
			}
			jExcel.getWorkSheet().setRowView(0, 600);
			jExcel.getWorkSheet().setColumnView(0, 15);
	    	jExcel.getWorkSheet().setColumnView(1, 20);
	    	jExcel.getWorkSheet().setColumnView(2, 20);	
	    	jExcel.getWorkSheet().setColumnView(3, 20);
	    	jExcel.getWorkSheet().setColumnView(4, 20);
	    	jExcel.getWorkSheet().setColumnView(5, 20);	
	    	jExcel.getWorkSheet().setColumnView(6, 20);	
	    	jExcel.getWorkSheet().setColumnView(7, 20);	
	    	jExcel.getWorkSheet().setColumnView(8, 30);
	    	jExcel.getWorkSheet().setColumnView(9, 30);
	    	jExcel.getWorkSheet().setColumnView(10, 50);
	    	
	    	jExcel.addDate(0, 0,"站点名称",jExcel.dateTITLEFormat);
	    	jExcel.addDate(1, 0,"站点CODE",jExcel.dateTITLEFormat);
	    	jExcel.addDate(2, 0,"国家",jExcel.dateTITLEFormat);
	    	jExcel.addDate(3, 0,"大洲",jExcel.dateTITLEFormat);
	    	jExcel.addDate(4, 0,"故障情况",jExcel.dateTITLEFormat);
	    	jExcel.addDate(5, 0,"更换设备",jExcel.dateTITLEFormat);
	    	jExcel.addDate(6, 0,"维护人",jExcel.dateTITLEFormat);
	    	jExcel.addDate(7, 0,"维护类型",jExcel.dateTITLEFormat);
	    	jExcel.addDate(8, 0,"故障开始时间",jExcel.dateTITLEFormat);
	    	jExcel.addDate(9, 0,"故障结束时间",jExcel.dateTITLEFormat);
	    	jExcel.addDate(10, 0,"处理结果",jExcel.dateTITLEFormat);
	    	int n=gd.getRowCount()+1;
	    	jExcel.addDate(0, n,"更换设备次数",jExcel.dateCellFormat);
	    	int m=equList.size();
	    	for(int i=0;i<m;i++){
	    		jExcel.addDate(i+1, n,equList.get(i)+":"+equMap.get(equList.get(i))+"次",jExcel.dateCellFormat);
	    	}
	    	jExcel.addDate(m+1, n,"总计:"+sum+"次",jExcel.dateCellFormat);
	    	jExcel.getWorkSheet().setName("设备维护");
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
	public Object queryEquPath(ASObject obj){
		ArrayList list=new ArrayList();
		String record=(String)obj.get("record");
		String str=SystemConfig.getEquRecordPath();
		File file=new File(str);
		String[] ss=file.list();
		if(ss==null||ss.length==0){
			return list;
		}
		Arrays.sort(ss);
		for(String s:ss){
			
			if((record==null||record.length()==0||s.contains(record))&&s.endsWith(".mp3")){
				ASObject aso=new ASObject();
				aso.put("record_name", str+"/"+s);
				list.add(aso);
			}
		}
		return list;
	}
}
