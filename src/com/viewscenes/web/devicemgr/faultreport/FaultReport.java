package com.viewscenes.web.devicemgr.faultreport;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbException;

import com.viewscenes.printexcel.JExcel;
import com.viewscenes.pub.GDSet;
import com.viewscenes.util.LogTool;

import flex.messaging.io.amf.ASObject;
import com.viewscenes.util.StringTool;

import org.jdom.Element;
import org.jmask.web.controller.EXEException;

public class FaultReport {
	public FaultReport() {

	}

	public Object getAlert_ManageList(ASObject obj) {

		ASObject resObj;
		String type_id = (String) obj.get("type_id");
		String starttime = (String) obj.get("starttime");
		String enddate = (String) obj.get("enddate");
		String isresumed = (String) obj.get("isresumed");
		String ishandled = (String) obj.get("ishandled");
		String head_name = "'"+((String)obj.get("head_name")).replaceAll(",", "','")+"'";
		String etype = (String) obj.get("etype");

		String tablename = "";

		tablename = "radio_equ_alarm_tab";

		String sql = "";
		sql += "select a.*,b.shortname,b.ip ,b.is_online from "
				+ tablename
				+ " a,res_headend_tab b  where b.is_delete=0 and a.head_id=b.head_id and b.type_id='"
				+ type_id + "'";

		if (isresumed.length() > 0)
			sql += " and is_resume='" + isresumed + "' ";

		if (ishandled.length() > 0)
			sql += " and is_handle='" + ishandled + "' ";

		if (etype.length() > 0)
			sql += " and type='" + etype + "' ";

		if (starttime.length() > 0)
			sql += " and alarm_datetime >= to_date('" + starttime + "')";

		if (enddate.length() > 0)
			sql += " and alarm_datetime <= to_date('" + enddate + "') ";

		if (head_name.length() > 2) {
			sql += " and b.shortname in(" + head_name + ")";

		}

		sql += " order by alarm_datetime desc ";

		try {
			resObj = StringTool.pageQuerySql(sql, obj);
		} catch (Exception e) {
			LogTool.fatal(e);
			return new EXEException("", e.getMessage(), "");
		}

		return resObj;
	}

	/**
	 * 
	 * <p>
	 * class/function:com.viewscenes.web.sysmgr.dicManager
	 * <p>
	 * explain:删除前端数据，如果添加出错会返回错误信息
	 * <p>
	 * author-date:张文 2012-7-26
	 * 
	 * @param:
	 * @return:
	 */
	public Object delFaultList(ASObject asObj) {

		String dellist = (String) asObj.get("dellist");

		String tablename = "";

		tablename = "radio_equ_alarm_tab";
		String sql = "";

		try {

			sql = "delete  from " + tablename + "  where alarm_id in("
					+ dellist + ")";

			DbComponent.exeUpdate(sql);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("", "后台错误!", null);
		}
		return " ";
	}
	/**
	 * 添加故障上报信息
	 * @param obj
	 * @return
	 */
	public Object insertFault(ASObject obj){
		String head_id=(String)obj.get("head_id");
		String equ_code=(String)obj.get("equ_code");
		String origin_alarmid=(String)obj.get("origin_alarmid");
		String alarm_datetime=(String)obj.get("alarm_datetime");
		String resume_datetime=(String)obj.get("resume_datetime");
		String is_resume=(String)obj.get("is_resume");
		String type=(String)obj.get("type");
		String reason=(String)obj.get("reason");
		String description=(String)obj.get("description");
		String outputlinelevel=(String)obj.get("outputlinelevel");
		String inputlinelevel=(String)obj.get("inputlinelevel");
		String linefrequency=(String)obj.get("linefrequency");
		String batterylevel=(String)obj.get("batterylevel");
		String upsstatus=(String)obj.get("upsstatus");
		String report_type=(String)obj.get("report_type");
		String is_handle=(String)obj.get("is_handle");
		String handle_datetime=(String)obj.get("handle_datetime");
		String operator=(String)obj.get("operator");
		String remark=(String)obj.get("remark");
		String head_code=(String)obj.get("head_code");
		if(origin_alarmid==null){
			origin_alarmid="";
		}
		StringBuffer sf=new StringBuffer();
		sf.append("insert into radio_equ_alarm_tab (alarm_id,head_id,equ_code,origin_alarmid,alarm_datetime,resume_datetime,");
		sf.append("is_resume,type,description,reason,outputlinelevel,inputlinelevel,linefrequency,batterylevel,");
		sf.append("upsstatus,report_type,is_handle,handle_datetime,operator,remark,is_delete,head_code) values (report_data_seq.nextval   ,");
		sf.append(head_id);
		sf.append(", '").append(equ_code);
		sf.append("', '").append(origin_alarmid);
		sf.append("',  to_date('").append(alarm_datetime).append("','yyyy-mm-dd hh24:mi:ss')");
		sf.append(", to_date('").append(resume_datetime).append("','yyyy-mm-dd hh24:mi:ss')");
		sf.append(", '").append(is_resume);
		sf.append("', '").append(type);
		sf.append("', '").append(description);
		sf.append("', '").append(reason);		
		sf.append("', '").append(outputlinelevel);
		sf.append("', '").append(inputlinelevel);
		sf.append("', '").append(linefrequency);
		sf.append("', '").append(batterylevel);
		sf.append("', '").append(upsstatus);
		sf.append("', '").append(report_type);
		sf.append("', '").append(is_handle);
		sf.append("', to_date('").append(handle_datetime).append("','yyyy-mm-dd hh24:mi:ss')");
		sf.append(", '").append(operator);
		sf.append("', '").append(remark);
		sf.append("',0,'").append(head_code);
		sf.append("')");
		try {
			DbComponent.exeUpdate(sf.toString());
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("", ","+e.getMessage(), "");
		}
		catch (Exception e) {
			e.printStackTrace();
			return new EXEException("","后台错误!",null);
		}
        
		return "添加故障信息成功";
	}

	/**
	 * 修改故障信息
	 * @param obj
	 * @return
	 */
	public Object updateFault(ASObject obj){
		String alarm_id=(String)obj.get("alarm_id");
		String head_id=(String)obj.get("head_id");
		String equ_code=(String)obj.get("equ_code");
		String origin_alarmid=(String)obj.get("origin_alarmid");
		String alarm_datetime=(String)obj.get("alarm_datetime");
		String resume_datetime=(String)obj.get("resume_datetime");
		String is_resume=(String)obj.get("is_resume");
		String type=(String)obj.get("type");
		String reason=(String)obj.get("reason");
		String description=(String)obj.get("description");
		String outputlinelevel=(String)obj.get("outputlinelevel");
		String inputlinelevel=(String)obj.get("inputlinelevel");
		String linefrequency=(String)obj.get("linefrequency");
		String batterylevel=(String)obj.get("batterylevel");
		String upsstatus=(String)obj.get("upsstatus");
		String report_type=(String)obj.get("report_type");
		String is_handle=(String)obj.get("is_handle");
		String handle_datetime=(String)obj.get("handle_datetime");
		String operator=(String)obj.get("operator");
		String remark=(String)obj.get("remark");
		String head_code=(String)obj.get("head_code");
		StringBuffer sf=new StringBuffer();
		sf.append("update radio_equ_alarm_tab set head_id='").append(head_id);
		sf.append("', equ_code ='").append(equ_code);
		sf.append("', origin_alarmid ='").append(origin_alarmid);
		sf.append("', alarm_datetime = to_date('").append(alarm_datetime).append("','yyyy-mm-dd hh24:mi:ss')");
		sf.append(", resume_datetime =to_date('").append(resume_datetime).append("','yyyy-mm-dd hh24:mi:ss')");
		sf.append(", is_resume ='").append(is_resume);
		sf.append("', type ='").append(type);
		sf.append("', reason ='").append(reason);
		sf.append("', description ='").append(description);
		sf.append("', outputlinelevel ='").append(outputlinelevel);
		sf.append("', inputlinelevel ='").append(inputlinelevel);
		sf.append("', linefrequency ='").append(linefrequency);
		sf.append("', batterylevel ='").append(batterylevel);
		sf.append("', upsstatus ='").append(upsstatus);
		sf.append("', report_type ='").append(report_type);
		sf.append("', is_handle ='").append(is_handle);
		sf.append("', handle_datetime =to_date('").append(handle_datetime).append("','yyyy-mm-dd hh24:mi:ss')");
		sf.append(", operator ='").append(operator);
		sf.append("', remark ='").append(remark);
		sf.append("', head_code ='").append(head_code);
		sf.append("' where alarm_id =").append(alarm_id);
		try {
			DbComponent.exeUpdate(sf.toString());
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("", ","+e.getMessage(), "");
		}
		catch (Exception e) {
			e.printStackTrace();
			return new EXEException("","后台错误!",null);
		}
        
		return "修改故障信息成功";
	}
	/**
	 * 导出Excel
	 * 
	 * @param msg
	 * @param request
	 * @param response
	 */
	public void doExcel(String msg, HttpServletRequest request,
			HttpServletResponse response) {
		Element root = StringTool.getXMLRoot(msg);

		String fileName = "故障上报管理";
		String downFileName = "故障上报管理";
		String type_id = root.getChildText("type_id");
		String head_name = root.getChildText("head_name");
		String starttime = root.getChildText("starttime");
		String enddate = root.getChildText("enddate");
		String isresumed = root.getChildText("isresumed");
		String ishandled = root.getChildText("ishandled");
		String etype = root.getChildText("etype");
		String tablename = "";

		tablename = "radio_equ_alarm_tab";

		String sql = "";
		sql += "select a.*,b.shortname,b.ip,b.is_online from "
				+ tablename
				+ " a,res_headend_tab b  where b.is_delete=0 and a.head_id=b.head_id and b.type_id='"
				+ type_id + "'";

		if (isresumed.length() > 0)
			sql += " and is_resume='" + isresumed + "' ";

		if (ishandled.length() > 0)
			sql += " and is_handle='" + ishandled + "' ";

		if (etype.length() > 0)
			sql += " and type='" + etype + "' ";

		if (starttime.length() > 0)
			sql += " and alarm_datetime >= to_date('" + starttime + "')";

		if (enddate.length() > 0)
			sql += " and alarm_datetime <= to_date('" + enddate + "') ";

		if (head_name.length() > 0) {
			sql += " and b.shortname in(" + head_name + ")";

		}

		sql += " order by alarm_datetime desc ";

		try {
			JExcel jExcel = new JExcel();
			downFileName = jExcel.openDocument();
			jExcel.WorkBookGetSheet(0);
			GDSet gd = DbComponent.Query(sql);
			if (gd.getRowCount() > 0) {
				for (int i = 0; i < gd.getRowCount(); i++) {
					jExcel.addDate(0, i + 1, gd.getString(i, "shortname"),
							jExcel.dateCellFormat);
					jExcel.addDate(1, i + 1, gd.getString(i, "head_code"),
							jExcel.dateCellFormat);
					if (gd.getString(i, "is_online").equals("1")) {
						jExcel.addDate(2, i + 1, "在线", jExcel.dateCellFormat);
					} else {
						jExcel.addDate(2, i + 1, "不在线", jExcel.dateCellFormat);
					}
					jExcel.addDate(3, i + 1, getFaultName(gd.getString(i, "type")),
							jExcel.dateCellFormat);
					jExcel.addDate(4, i + 1, gd.getString(i, "alarm_datetime"),
							jExcel.dateCellFormat);
					jExcel.addDate(5, i + 1,
							gd.getString(i, "resume_datetime"),
							jExcel.dateCellFormat);
					jExcel.addDate(6, i + 1, gd.getString(i, "reason"),
							jExcel.dateCellFormat);

					jExcel.getWorkSheet().setRowView(i + 1, 600);
				}

			}
			jExcel.getWorkSheet().setRowView(0, 600);
			jExcel.getWorkSheet().setColumnView(0, 10);
			jExcel.getWorkSheet().setColumnView(1, 10);
			jExcel.getWorkSheet().setColumnView(2, 10);
			jExcel.getWorkSheet().setColumnView(3, 30);
			jExcel.getWorkSheet().setColumnView(4, 30);
			jExcel.getWorkSheet().setColumnView(5, 30);
			jExcel.getWorkSheet().setColumnView(6, 15);
			if (type_id.equals("102")) {
				jExcel.addDate(0, 0, "遥控站", jExcel.dateTITLEFormat);
			} else {
				jExcel.addDate(0, 0, "采集点", jExcel.dateTITLEFormat);
			}
			jExcel.addDate(1, 0, "站点代码", jExcel.dateTITLEFormat);
			jExcel.addDate(2, 0, "是否在线", jExcel.dateTITLEFormat);
			jExcel.addDate(3, 0, "报警类别", jExcel.dateTITLEFormat);
			jExcel.addDate(4, 0, "故障开始时间", jExcel.dateTITLEFormat);
			jExcel.addDate(5, 0, "故障恢复时间", jExcel.dateTITLEFormat);
			jExcel.addDate(6, 0, "备注", jExcel.dateTITLEFormat);

			jExcel.getWorkSheet().setName("故障上报管理");
			jExcel.saveDocument();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Location", "Export.xls");
			response.setHeader("Expires", "0");
			response.setHeader("Content-Disposition", new String(
					("filename=" + fileName).getBytes("GBK"), "ISO-8859-1")
					+ ".xls");
			OutputStream outputStream = response.getOutputStream();
			InputStream inputStream = new FileInputStream(downFileName);
			byte[] buffer = new byte[1024];
			int i = -1;
			while ((i = inputStream.read(buffer)) != -1) {
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
	private String getFaultName(String  s){
		if(s.equals("1"))
			return "供电异常报警[UPS]";
		if(s.equals("2"))
			return "接收机异常报警";
		if(s.equals("3"))
			return "调制度卡异常报警";
		if(s.equals("4"))
			return "调幅度卡报警";
		if(s.equals("5"))
			return "语音压缩卡异常报警";
		if(s.equals("6"))
			return "频偏卡报警";
		if(s.equals("7"))
			return "电池电压低系统关机";
		return "未知异常报警";
	}

}
