package com.viewscenes.web.runplanManager.gjtRunplan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.jdom.Element;
import org.jmask.web.controller.EXEException;

import com.viewscenes.bean.runplan.GJTRunplanBean;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.pub.GDSet;
import com.viewscenes.pub.GDSetException;
import com.viewscenes.pub.GDSetTool;
import com.viewscenes.printexcel.JExcel;
import com.viewscenes.util.StringTool;
import com.viewscenes.web.common.Common;
import com.viewscenes.web.runplanManager.gjtRunplan.GJTRunplanDAO;

import flex.messaging.io.amf.ASObject;

/**
 * 国际台运行图类
 * @author leeo
 *
 */
public class GJTRunplan {
	
	
	/**
	 * 添加运行图操作
	 * @param bean
	 * @return
	 */
	public Object addRunplan(GJTRunplanBean bean){
		String message="";
		try {
			GJTRunplanDAO grldao = new GJTRunplanDAO();
			message=grldao.addRunplan(bean);
		} catch (Exception e) {
			e.printStackTrace();
			return new EXEException("", e.getMessage(), "");
		}
		return message;
	}
	
	
   /**
    * 查询运行图
    * @param bean
    * @return
    */
	public Object queryRunplan(GJTRunplanBean bean){
		GJTRunplanDAO grldao = new GJTRunplanDAO();
		ArrayList result = new ArrayList();
		ASObject resObj;
		try {
			 resObj = grldao.queryRunplan(bean);
			 ArrayList list =(ArrayList) resObj.get("resultList") ;//取出对象的结果集
			 if(list.size()>0){
				 for(int i=0;i<list.size();i++){
					 String classpath = GJTRunplanBean.class.getName();
					 GJTRunplanBean bean1 = (GJTRunplanBean) StringTool.convertObjToBean((ASObject) list.get(i),classpath);
					// GJTRunplanBean bean1 = (GJTRunplanBean) list.get(i);
					 if(bean1.getMon_area()!=null&&!bean1.getMon_area().equalsIgnoreCase("")){
						 String mon_area = bean1.getMon_area();
						 String[] s = mon_area.split(",");
						 if(s.length>0){
							 String mon_name="";
							 for(int j=0;j<s.length;j++){
								 mon_name+=Common.getShortNameByCode(s[j])+",";
							 }
							 bean1.setMon_area_name(mon_name.substring(0, mon_name.length()-1));
						 }
					 }
					 if(bean1.getXg_mon_area()!=null&&!bean1.getXg_mon_area().equalsIgnoreCase("")){
						 String xg_mon_area = bean1.getXg_mon_area();
						 String[] s = xg_mon_area.split(",");
						 if(s.length>0){
							 String xg_mon_name="";
							 for(int j=0;j<s.length;j++){
								 xg_mon_name+=Common.getShortNameByCode(s[j])+",";
							 }
							 bean1.setXg_mon_area_name(xg_mon_name.substring(0, xg_mon_name.length()-1));
						 }
					 }
					 result.add(bean1);
				 }
				 resObj.put("resultList", result);
			 }
//			 String classpath = GJTRunplanBean.class.getName();
//			 result = StringTool.convertFlexToJavaList(list, classpath);//将对象类型转换成bean，再放回结果集中。
//			 resObj.put("resultList", result);
		} catch (Exception e) {
			e.printStackTrace();
			EXEException exe = new EXEException("", 
					e.getMessage(), "");
			return exe ;
		}
		
		return resObj;
	}
	/**
	 * 修改运行图
	 * @param bean
	 * @return
	 */
	public Object updateRunplan(GJTRunplanBean bean){
		String res="修改运行图成功!";
		//String[] sqls = new String[2];
		String runplan_id = bean.getRunplan_id();   //运行图id
		//String runplan_type_id = bean.getRunplan_type_id(); //运行图类型id
		String station_id = bean.getStation_id();    //发射台id
		String station_name = bean.getStation_name(); //发射台
		String sentcity_id = bean.getSentcity_id();  //发射城市id
		String transmiter_no = bean.getTransmiter_no();  //发射机号
		String freq =bean.getFreq();      //频率
		String antenna = bean.getAntenna(); //天线号
		String antennatype = bean.getAntennatype();//天线程式
		String direction = bean.getDirection(); //方向
		String language_id = bean.getLanguage_id(); //语言id
		String power = bean.getPower();  //功率
		String program_type_id = bean.getProgram_type_id(); //节目类型id
		String program_type = bean.getProgram_type(); //节目类型
		String service_area = bean.getService_area(); //服务区
		String ciraf = bean.getCiraf(); // CIRAF区
		String satellite_channel = bean.getSatellite_channel();//国际卫星通道
		String start_time = bean.getStart_time(); //播音开始时间
		String end_time = bean.getEnd_time();   //播音结束时间
		String mon_area = bean.getMon_area();    //质量收测站点
		String xg_mon_area = bean.getXg_mon_area();//效果收测站点
		String rest_datetime = bean.getRest_datetime(); // //休息日期
	//	String rest_time = bean.getRest_time(); //休息时间
		String valid_start_time = bean.getValid_start_time(); //启用期
		String valid_end_time = bean.getValid_end_time();  //停用期
		String remark = bean.getRemark();//备注
		String season_id = bean.getSeason_id();//季节代号
		String input_person = bean.getInput_person(); //录入人
		String weekday = bean.getWeekday();//周设置
		StringBuffer sbf = new StringBuffer("update zres_runplan_tab set ");
		sbf.append(" station_id='"+station_id+"',STATION_NAME='"+station_name+"',TRANSMITER_NO='"+transmiter_no+"',FREQ='"+freq+"',");
		sbf.append("ANTENNA='"+antenna+"',ANTENNATYPE='"+antennatype+"',START_TIME='"+start_time+"',END_TIME='"+end_time+"',PROGRAM_TYPE_ID='"+program_type_id+"',program_type='"+program_type+"',");
		sbf.append("LANGUAGE_ID='"+language_id+"',DIRECTION='"+direction+"',POWER='"+power+"',SERVICE_AREA='"+service_area+"',CIRAF='"+ciraf+"',weekday='"+weekday+"',");
		sbf.append("SATELLITE_CHANNEL='"+satellite_channel+"',REMARK='"+remark+"',MON_AREA='"+mon_area+"',xg_mon_area='"+xg_mon_area+"',REST_DATETIME='"+rest_datetime+"',");
		sbf.append("VALID_START_TIME='"+valid_start_time+"',VALID_END_TIME='"+valid_end_time+"',STORE_DATETIME=sysdate,SEASON_ID='"+season_id+"',INPUT_PERSON='"+input_person+"',");
		sbf.append("IS_DELETE=0 where runplan_id='"+runplan_id+"' and RUNPLAN_TYPE_ID=1");
		String delsql=" update zres_runplan_chaifen_tab t set t.is_delete=1 where t.parent_id='"+runplan_id+"'";
		//sqls[0]=sbf.toString();
		//sqls[1]=delsql;
		try {
			//DbComponent.exeBatch(sqls);
			DbComponent.exeUpdate(sbf.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return new EXEException("",e.getMessage(),"");
		}
		return res;
	}
	/**
	 * 批量修改运行图
	 * @param obj
	 * @return
	 */
	public Object batchupdateRunplan(ASObject obj){
		String res="批量修改运行图成功!";
		StringBuffer sql=new StringBuffer("update zres_runplan_tab set ");
		if(obj.get("station_id1")!=null&&!obj.get("station_id1").toString().equalsIgnoreCase("")){
			sql.append(" station_id='"+obj.get("station_id1").toString()+"',");
			sql.append("STATION_NAME='"+obj.get("station_name1").toString()+"',");
		}
		if(obj.get("transmitor_no1")!=null&&!obj.get("transmitor_no1").toString().equalsIgnoreCase("")){
			sql.append(" TRANSMITER_NO='"+obj.get("transmitor_no1").toString()+"',");
		}
		if(obj.get("freq1")!=null&&!obj.get("freq1").toString().equalsIgnoreCase("")){
			sql.append(" freq='"+obj.get("freq1").toString()+"',");
		}
		if(obj.get("valid_starttime")!=null&&!obj.get("valid_starttime").toString().equalsIgnoreCase("")){
			sql.append(" VALID_START_TIME='"+obj.get("valid_starttime").toString()+"',");
		}
		if(obj.get("valid_endtime")!=null&&!obj.get("valid_endtime").toString().equalsIgnoreCase("")){
			sql.append(" VALID_END_TIME='"+obj.get("valid_endtime").toString()+"',");
		}
		sql.append(" STORE_DATETIME=sysdate,INPUT_PERSON='"+obj.get("user_name").toString()+"'");
		sql.append(" where runplan_type_id=1 ");
		if(obj.get("season")!=null&&!obj.get("season").toString().equalsIgnoreCase("")){
			sql.append(" and SEASON_ID='"+obj.get("season").toString()+"'");
		}
		if(obj.get("station_id")!=null&&!obj.get("station_id").toString().equalsIgnoreCase("")){
			sql.append("  and station_id='"+obj.get("station_id").toString()+"'");
		}
		if(obj.get("transmitor_no")!=null&&!obj.get("transmitor_no").toString().equalsIgnoreCase("")){
			sql.append(" and TRANSMITER_NO='"+obj.get("transmitor_no").toString()+"'");
		}
		if(obj.get("freq")!=null&&!obj.get("freq").toString().equalsIgnoreCase("")){
			sql.append(" and freq='"+obj.get("freq").toString()+"'");
		}
		if(obj.get("starttime")!=null&&!obj.get("starttime").toString().equalsIgnoreCase("")){
			sql.append(" and start_time='"+obj.get("starttime").toString()+"'");
		}
		if(obj.get("endtime")!=null&&!obj.get("endtime").toString().equalsIgnoreCase("")){
			sql.append(" and end_time='"+obj.get("endtime").toString()+"'");
		}
		try {
			DbComponent.exeUpdate(sql.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return new EXEException("",e.getMessage(),"");
		}
		return res;
	}
	
	/**
	 * 删除运行图
	 * @param ids
	 * @return
	 */
	public Object delRunplan(String ids){
		String message="删除运行图成功!";
		String sql="update zres_runplan_tab set is_delete=1 where runplan_type_id=1 and runplan_id in("+ids+")";
		try {
			DbComponent.exeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return new EXEException("","删除运行图异常"+e.getMessage(),"");
		}
		return message;
	}
	/**
	 * 获取站点信息这里将带AB的站点合并为一个站点
	 * @return
	 */
	public Object getHeadendList(ASObject asobj){
		ArrayList list = new ArrayList();
		String headtype = (String) asobj.get("type_id");
		String state = (String) asobj.get("state");
		StringBuffer sql = new StringBuffer("select distinct (decode(upper(t.type_id||t.version),upper('102V8'),substr(t.shortname, 0, length(t.shortname) - 1),t.shortname)) as shortname,");
		             sql.append(" decode(upper(t.type_id||t.version),upper('102V8'),substr(t.code, 0, length(t.code) - 1),t.code) as code from res_headend_tab t where t.is_delete = 0  ");
		if(!headtype.equalsIgnoreCase("all")){
			sql.append(" and type_id='"+headtype+"'");
		}
		if(!state.equalsIgnoreCase("100")&&!state.equalsIgnoreCase("all")){
			sql.append(" and state="+state+"");
		}
		sql.append("   order by code");
		try {
			GDSet gd = DbComponent.Query(sql.toString());
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					ASObject obj = new ASObject();
					obj.put("value", gd.getString(i, "code"));
					obj.put("label", gd.getString(i, "shortname")+"["+gd.getString(i, "code")+"]");
					obj.put("selected", false);
					list.add(obj);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	/**
	 * 导出Excel
	 * @param msg
	 * @param request
	 * @param response
	 * @throws GDSetException 
	 */
	public void doExcel(String msg,HttpServletRequest request,HttpServletResponse response) throws GDSetException{
		Element root = StringTool.getXMLRoot(msg);
		String runplan_type_id = root.getChildText("runplan_type");
		String station_id = root.getChildText("station_id");
		String transmiter_no = root.getChildText("transmiter_no");
		String freq = root.getChildText("freq");
		String antenna = root.getChildText("antenna");
		String antennatype = root.getChildText("antennatype");
		String program_type = root.getChildText("program_type");
		String program_type_id = root.getChildText("program_type_id");
		String language_id = root.getChildText("language_id");
		String direction = root.getChildText("direction");
		String power = root.getChildText("power");
		String service_area = root.getChildText("service_area");
		String ciraf = root.getChildText("ciraf");
		String season_id = root.getChildText("season_id");
		String mon_area = root.getChildText("mon_area");
		String xg_mon_area = root.getChildText("xg_mon_area");
		String valid_start_time = root.getChildText("valid_start_time");
		String valid_end_time = root.getChildText("valid_end_time");
		String sql="select start_time||'-'||end_time as time,service_area,freq,station_name,power,direction,zlt.language_name,transmiter_no,antenna,antennatype,'' as itu,satellite_channel,xg_mon_area,mon_area,program_type," +
				"ciraf,valid_start_time,valid_end_time,season_id " +
				" from zres_runplan_tab t,zdic_language_tab zlt where t.is_delete=0 and runplan_type_id='"+runplan_type_id+"'  and  t.language_id=zlt.language_id  ";
		if(station_id!=null && !station_id.equalsIgnoreCase("")){
			sql+=" and station_id='"+station_id+"' ";
		}	
		if(season_id!=null&&!season_id.equalsIgnoreCase("")){//根据季节代号的有效期查询运行图
			GDSet gd1=Common.getTimeBySeason(season_id);
			if(gd1.getRowCount()>0){
				sql+=" and valid_start_time>=to_date('"+gd1.getString(0, "start_time")+"','yyyy-mm-dd hh24:mi:ss') ";
				sql+=" and valid_start_time<=to_date('"+gd1.getString(0, "end_time")+"','yyyy-mm-dd hh24:mi:ss') " ;
			}
		}
		if(transmiter_no!=null && !transmiter_no.equalsIgnoreCase("")){
			sql+=" and transmiter_no='"+transmiter_no+"'";
		}
		if(freq!=null && !freq.equalsIgnoreCase("")){
			sql+=" and freq='"+freq+"'";
		}
		if(antenna!=null && !antenna.equalsIgnoreCase("")){
			sql+=" and antenna='"+antenna+"'";
		}
		if(antennatype!=null && !antennatype.equalsIgnoreCase("")){
			sql+=" and antennatype='"+antennatype+"'";
		}
		if(direction!=null && !direction.equalsIgnoreCase("")){
			sql+=" and direction='"+direction+"'";
		}
		if(language_id!=null && !language_id.equalsIgnoreCase("")){
			sql+=" and t.language_id="+language_id+"";
		}
		if(power!=null &&!power.equalsIgnoreCase("")){
			sql+=" and power="+power+"";
		}
		if(program_type_id!=null && !program_type_id.equalsIgnoreCase("")){
			sql+=" and program_type_id="+program_type_id+"";
		}
		if(service_area!=null && !service_area.equalsIgnoreCase("")){
			sql+=" and service_area='"+service_area+"'";
		}
		if(ciraf!=null && !ciraf.equalsIgnoreCase("")){
			sql+=" and ciraf='"+ciraf+"'";
		}
		if(valid_start_time!=null && !valid_start_time.equalsIgnoreCase("")){
			sql+=" and (t.valid_start_time>=to_date('"+valid_start_time+"','yyyy-MM-dd HH24:MI:SS') and t.valid_start_time<=to_date('"+valid_end_time+"','yyyy-MM-dd HH24:MI:SS'))";
		}
		if(valid_end_time!=null && !valid_end_time.equalsIgnoreCase("")){
			sql+=" and t.valid_end_time>=to_date('"+valid_end_time+"','yyyy-MM-dd HH24:MI:SS')";
		}
//		//新要求，田雅静提出如果开始时间和结束时间都为空，需要让有效期开始时间小于当前时间。
//		if((valid_start_time==null ||valid_start_time.equalsIgnoreCase(""))&&(valid_end_time==null ||valid_end_time.equalsIgnoreCase(""))){
//			sql+=" and t.valid_start_time<=sysdate ";	
//		}
//		if((mon_area!=null && !mon_area.equalsIgnoreCase(""))&&(xg_mon_area!=null && !xg_mon_area.equalsIgnoreCase(""))){
//			sql+=" and (zrt.mon_area like '%"+mon_area+"%'");
//			sqlbuffer.append(" or zrt.xg_mon_area like '%"+xg_mon_area+"%')");
//		}else{
//			if(mon_area!=null && !mon_area.equalsIgnoreCase("")){
//				sqlbuffer.append(" and zrt.mon_area like '%"+mon_area+"%'");
//			}
//			if(xg_mon_area!=null && !xg_mon_area.equalsIgnoreCase("")){
//				sqlbuffer.append(" and zrt.xg_mon_area like '%"+xg_mon_area+"%'");
//			}
//		}
		if(mon_area!=null && !mon_area.equalsIgnoreCase("")){
			if(mon_area.indexOf(",")>0){
				String[] s=mon_area.split(",");
				StringBuffer temp = new StringBuffer("(");
				for(int i=0;i<s.length;i++){
					temp.append(" t.mon_area like '%"+s[i]+"%' or");
				}
				sql+=" and "+temp.toString().substring(0, temp.toString().length()-2)+")";
			}else{
				sql+=" and t.mon_area like '%"+mon_area+"%'";
			}
			
		}
		if(xg_mon_area!=null && !xg_mon_area.equalsIgnoreCase("")){
			if(xg_mon_area.indexOf(",")>0){
				String[] s=xg_mon_area.split(",");
				StringBuffer temp = new StringBuffer("(");
				for(int i=0;i<s.length;i++){
					temp.append(" t.xg_mon_area like '%"+s[i]+"%' or");
				}
				sql+=" and "+temp.toString().substring(0, temp.toString().length()-2)+")";
			}else{
				sql+=" and t.xg_mon_area like '%"+xg_mon_area+"%'";
			}
			
		}
		sql+=" order by t.xg_mon_area,t.mon_area  ,station_name,service_area,time,freq,language_name";
		String fileName = "国际台运行图";
		String downFileName = "";
		GDSet gd=null;
		try {
			gd= DbComponent.Query(sql,GDSetTool.Return_ArrayListRow);
			JExcel jExcel = new JExcel();
			downFileName=jExcel.openDocument();
			jExcel.WorkBookGetSheet(0);
			for(int i=0;i<gd.getRowCount();i++){
				for(int j=0;j<19;j++){
					jExcel.addDate(j, i+1,(String)gd.getCell(i, j),jExcel.dateCellFormat);
					jExcel.getWorkSheet().setRowView(i+1, 500);
				}
			}
			jExcel.getWorkSheet().setColumnView(0, 20);
	    	jExcel.getWorkSheet().setColumnView(1, 10);
	    	jExcel.getWorkSheet().setColumnView(2, 15);	
	    	jExcel.getWorkSheet().setColumnView(3, 10);
	    	jExcel.getWorkSheet().setColumnView(4, 10);
	    	jExcel.getWorkSheet().setColumnView(5, 10);	
	    	jExcel.getWorkSheet().setColumnView(6, 10);	
	    	jExcel.getWorkSheet().setColumnView(7, 10);	
	    	jExcel.getWorkSheet().setColumnView(8, 10);
	    	jExcel.getWorkSheet().setColumnView(9, 10);
	    	jExcel.getWorkSheet().setColumnView(10, 10);
	    	jExcel.getWorkSheet().setColumnView(11, 10);
	    	jExcel.getWorkSheet().setColumnView(12, 20);
	    	jExcel.getWorkSheet().setColumnView(13, 20);	
	    	jExcel.getWorkSheet().setColumnView(14, 10);
	    	jExcel.getWorkSheet().setColumnView(15, 10);
	    	jExcel.getWorkSheet().setColumnView(16, 20);	
	    	jExcel.getWorkSheet().setColumnView(17, 20);	
	    	jExcel.getWorkSheet().setColumnView(18, 10);	
	    	
	    	jExcel.addDate(0, 0,"播音时间",jExcel.dateTITLEFormat);
	    	jExcel.addDate(1, 0,"服务区",jExcel.dateTITLEFormat);
	    	jExcel.addDate(2, 0,"频率(KHZ)",jExcel.dateTITLEFormat);
	    	jExcel.addDate(3, 0,"发射台",jExcel.dateTITLEFormat);
	    	jExcel.addDate(4, 0,"功率",jExcel.dateTITLEFormat);
	    	jExcel.addDate(5, 0,"方向",jExcel.dateTITLEFormat);
	    	jExcel.addDate(6, 0,"语言",jExcel.dateTITLEFormat);
	    	jExcel.addDate(7, 0,"发射机号",jExcel.dateTITLEFormat);
	    	jExcel.addDate(8, 0,"天线号",jExcel.dateTITLEFormat);
	    	jExcel.addDate(9, 0,"天线程式",jExcel.dateTITLEFormat);
	    	jExcel.addDate(10, 0,"ITU",jExcel.dateTITLEFormat);
	    	jExcel.addDate(11, 0,"通道",jExcel.dateTITLEFormat);
	    	jExcel.addDate(12, 0,"效果收测站点",jExcel.dateTITLEFormat);
	    	jExcel.addDate(13, 0,"质量收测站点",jExcel.dateTITLEFormat);
	    	jExcel.addDate(14, 0,"节目类别",jExcel.dateTITLEFormat);
	    	jExcel.addDate(15, 0,"CIRAF区",jExcel.dateTITLEFormat);
	    	jExcel.addDate(16, 0,"启用期",jExcel.dateTITLEFormat);
	    	jExcel.addDate(17, 0,"停用期",jExcel.dateTITLEFormat);
	    	jExcel.addDate(18, 0,"季节代号",jExcel.dateTITLEFormat);
	    	jExcel.getWorkSheet().setRowView(0, 500);
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
			e.printStackTrace();
		}
	}
	/**
	 * 运行图导入功能
	 * @param obj
	 * @return
	 * @throws IOException 
	 * @throws BiffException 
	 */
	
	public Object importExcel(ASObject obj){
		String res="运行图导入成功!";
		
		String input_person = (String) obj.get("input_person");//导入人
		String runplan_type_id = (String) obj.get("runplan_type_id");//运行图类型
		String file_name = (String) obj.get("file_name");//需要导入的文件名
		GJTRunplanDAO grldao = new GJTRunplanDAO();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try{
		String path="C:\\runplan\\"+file_name+"";
		if(!new File(path).exists()){
			res="文件路径不存在,请上传后再导入!";
			return res;
		}
		Workbook book = Workbook.getWorkbook(new File(path));
		Sheet sheet = book.getSheet(0);
		int rowNum = sheet.getRows();   //得到总行数  
		int colNum = sheet.getColumns();//得到总列数
		ArrayList list = new ArrayList();//存放运行图对象的list 
		
        for(int i=1;i<rowNum;i++){
          GJTRunplanBean bean = new GJTRunplanBean();
      	  for(int j=0;j<colNum;j++){
      		  if(j==0){//播音时间
      			if(sheet.getCell(j,i).getContents()!=null&&!sheet.getCell(j,i).getContents().equalsIgnoreCase("")){
      				String[] times =sheet.getCell(j,i).getContents().split("-");
      				if(times.length==2)
      				{
      					if(times[0].length()==4)
      					{
      						times[0]=times[0].substring(0, 2)+":"+times[0].substring(2, 4);
      					}
      					if(times[1].length()==4)
      					{
      						times[1]=times[1].substring(0, 2)+":"+times[1].substring(2, 4);
      					}
      				}
          			bean.setStart_time(times[0].toString());
          			bean.setEnd_time(times[1].toString());
      			}
      		  }
      		  if(j==1){//服务区
      			  bean.setService_area(sheet.getCell(j,i).getContents());
      		  }
      		  if(j==2){//频率
      			  bean.setFreq(sheet.getCell(j,i).getContents());
      		  }
      		  if(j==3){//发射台
    			  bean.setStation_name(sheet.getCell(j,i).getContents());
    			  bean.setStation_id(Common.getStationIDByName(sheet.getCell(j,i).getContents()));
    		  }
      		  if(j==4){//功率
    			  bean.setPower(sheet.getCell(j,i).getContents());
    		  }
      		  if(j==5){//方向
    			  bean.setDirection(sheet.getCell(j,i).getContents());
    		  }
      		  if(j==6){//语言
    			  bean.setLanguage(sheet.getCell(j,i).getContents());
    			  bean.setLanguage_id(Common.getLanguageIDByName(sheet.getCell(j,i).getContents()));
    		  }
      		  if(j==7){//发射机号
    			  bean.setTransmiter_no(sheet.getCell(j,i).getContents());
    		  }
      		  if(j==8){//天线号
    			  bean.setAntenna(sheet.getCell(j,i).getContents());
    		  }
      		  if(j==9){//天线程式
      			bean.setAntennatype(sheet.getCell(j,i).getContents());
      		  }
      		  if(j==10){//ITU
      			 // bean.set
      		  }
      		  if(j==11){//通道
    			  bean.setSatellite_channel(sheet.getCell(j,i).getContents());
    		  }
      		  if(j==12){//效果收测站点
      			bean.setXg_mon_area(sheet.getCell(j,i).getContents());
      			  
    		  }
      		  if(j==13){//质量收测站点
      			bean.setMon_area(sheet.getCell(j,i).getContents());
    		  }
      		  if(j==14){//节目类型
    			  bean.setProgram_type(sheet.getCell(j,i).getContents());
    			  if(sheet.getCell(j,i).getContents().equalsIgnoreCase("海外转播")){
    				  bean.setProgram_type_id("1");
    			  }
    			  if(sheet.getCell(j,i).getContents().equalsIgnoreCase("国内直属")){
    				  bean.setProgram_type_id("2");
    			  }
    			  if(sheet.getCell(j,i).getContents().equalsIgnoreCase("国外直属")){
    				  bean.setProgram_type_id("3");
    			  }
    			  if(sheet.getCell(j,i).getContents().equalsIgnoreCase("国内地方")){
    				  bean.setProgram_type_id("4");
    			  }
    			  
    		  }
      		  if(j==15){
    			  bean.setCiraf(sheet.getCell(j,i).getContents());
    		  }
//      		  if(j==16){
//    			 bean.setValid_start_time(sheet.getCell(j,i).getContents().replace("/", "-")+" "+sheet.getCell(j+1,i).getContents()+":00");
//    		  }
//      		  if(j==17){
//      			  continue;
//      		  }
      		  if(j==16){
     			 bean.setValid_start_time(sheet.getCell(j,i).getContents());
     		  }
//      		  if(j==18){
//    			  bean.setValid_end_time( sheet.getCell(j,i).getContents().replace("/", "-")+" "+sheet.getCell(j+1,i).getContents()+":00");
//    		  }
      		 if(j==17){
   			  bean.setValid_end_time( sheet.getCell(j,i).getContents());
   		  }
//      		  if(j==19){
//      			 continue;
//      		  }
//    		  if(j==20){
//      			  bean.setSeason_id(sheet.getCell(j,i).getContents());
//      		  }
      		if(j==18){
			  bean.setSeason_id(sheet.getCell(j,i).getContents());
		  }
    		  bean.setInput_person(input_person);
    		  bean.setRunplan_type_id(runplan_type_id);
      	  }
      	 String mon_area="";
      	 //if(bean.getXg_mon_area().equalsIgnoreCase("")){//如果效果收测站点有就不需要算了。
      	  
      		 mon_area=calculateWebsite(bean);
      		 if(mon_area!=null&&!mon_area.equalsIgnoreCase("")){
      			 bean.setRemark("自动计算的站点");
      		 }
          	 bean.setXg_mon_area(mon_area); 
          	
      	// }
      	
      	 list.add(bean);
        }
        for(int k=0;k<list.size();k++){
			grldao.addRunplan((GJTRunplanBean)list.get(k));
        }
	}catch(Exception e){
		e.printStackTrace();
		return new EXEException("","运行图导入异常："+e.getMessage(),"");
	}
		return res;
	}
	/**
	 * 根据运行图信息自动算出收测站点
	 * @return
	 */
	public String calculateWebsite(GJTRunplanBean bean){
		String mon_area="";
		String service_area=bean.getService_area();//服务区
		String language_name = bean.getLanguage();//语言
		String station_name = bean.getStation_name();//发射台名称
		String antenna = bean.getAntenna();//天线号
		String direction = bean.getDirection();//方向
		String[] range = getAntennaRange(station_name,antenna,direction);
		bean.setAntennatype(range[2]);
		//这里需要判断如果原有的站点需要保留
		mon_area = getMon_Area( bean.getXg_mon_area(),range,station_name,service_area,language_name);
	
		return mon_area;
		
	}
	/*
	 * 根据运行图的发射台，天线号，方向计算天线的收测范围
	 * 计算方法为：首先根据发射台和天线号在res_antenna_tab表找到对应的天线程式
	 *         再根据天线程式去dic_antenna_parameter_tab表找到对应的主
	 *         瓣宽度(antenna_width字段)，然后再将主瓣宽度值除2得到的正负值
	 *         加方向值就是这个天线收测的范围。（例如主瓣宽度值为40，那么除2就是20，如果
	 *         运行图的方向为120那么得到的范围就是100-140）
	 */
	public String[] getAntennaRange(String station,String antenna,String direction){
		String[] s = new String[3];
		String antenna_width="";
		String sql="select dapt.antenna_width,dapt.antenna_mode from res_antenna_tab rat,dic_antenna_parameter_tab dapt where rat.antenna_mode=dapt.antenna_mode ";
		if((station!=null&&!station.equalsIgnoreCase(""))&&(antenna!=null&&!antenna.equalsIgnoreCase(""))){
			sql+=" and rat.station_name='"+station+"' and rat.antenna_no='"+antenna+"'";
		}
		try {
			GDSet gd = DbComponent.Query(sql);
			if(gd.getRowCount()>0){
				antenna_width=gd.getString(0, "antenna_width");
				s[0]=String.valueOf(Double.parseDouble(direction)-Double.parseDouble(antenna_width)/2);
				s[1] =String.valueOf(Double.parseDouble(direction)+Double.parseDouble(antenna_width)/2);
			    s[2]=gd.getString(0, "antenna_mode");
			}else{//如果找不到天线程式对应的主瓣宽度，天线收测范围的默认值给0-0
				s[0]="0";
				s[1]="0";
				s[2]="";
			}
			
		} catch (Exception e) {
			s[0]="0";
			s[1]="0";
			s[2]="";
			e.printStackTrace();
		}
		return s;
	}
	public static void main(String[] args)
	{
		GJTRunplan p=new GJTRunplan();
		String[] range =new String[2];
		range[0]="100";
		range[1]="200";

		System.out.println(	p.getMon_Area("aaa",range, "564", "西亚.北非", "英"));
		String[] times ="0000-0100".split("-");
			if(times.length==2)
			{
				if(times[0].length()==4)
				{
					times[0]=times[0].substring(0, 2)+":"+times[0].substring(2, 4);
				}
				if(times[1].length()==4)
				{
					times[1]=times[1].substring(0, 2)+":"+times[1].substring(2, 4);
				}
			}
			System.out.println(times[0]+"-"+times[1]);
	}
	/**
	 * 根据发射台和方位角去dic_station_city_rel_tab表取城市。（具体计算方法为：只要方位角值在天线收测范围内对应的城市，
	 * 都认为是可以收测的。）然后再根据城市去res_headend_tab表得到相对应的站点code(这里城市名称即是站点名称)
	 * @param range
	 * @param station
	 * @param service
	 * @param language
	 * @return
	 */
	public String getMon_Area(String mon_area_old,String[] range,String station,String service,String language){
		String mon_area="";
		/**
		 * 根据田雅静要求，这里不进行采集点的分配
		 * 张嘉要求计算不考虑二期站点 20171026
		 */
		String ss="";
		String sql="select distinct decode(res.type_id||res.version, '102V8',substr(res.code, 0, length(res.code) - 1),res.code) as code" +
				" from res_headend_tab res,dic_station_city_rel_tab t " +
				"where res.is_delete=0 and res.type_id =102  and res.version='V8'  and res.shortname like '%'||t.city_name||'%' " +
				" and res.default_language like '%"+language+"%'  and t.station_name like '%"+station+"%' " +
				" and  t.azimuth >"+range[0]+" and t.azimuth < "+range[1]+" ";
		String[] s = null;
		if(service.indexOf(",")>0)
		{
			s = new String[service.split(",").length];
			s = service.split(",");
		}else if(service.indexOf(".")>0)
		{
			s = new String[service.split("\\.").length];
			s = service.split("\\.");
		}
		if(s!=null){
			
			for(int i=0;i<s.length;i++){
				ss+="'%'||res.service_name||'%' like '%"+s[i]+"%' or ";
			}
			sql+=" and ("+ss.substring(0, ss.length()-3)+")";
		}else{
			sql+=" and '%'||res.service_name||'%' like '%"+service+"%' ";
		}
		try {
			GDSet gd = DbComponent.Query(sql);
			if(gd.getRowCount()>0){
				for(int i=0;i<gd.getRowCount();i++){
					if(mon_area_old.indexOf(gd.getString(i, "code"))==-1)
					{
				     	mon_area+=gd.getString(i, "code")+",";
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(mon_area_old!=null&&!mon_area_old.equals(""))
		{
			mon_area+=mon_area_old;
		}else
		{
			if(mon_area.length()>0){
				mon_area=mon_area.substring(0, mon_area.length()-1);
			}	
		}
	     if(mon_area.trim().endsWith(","))
	     {
	    	 mon_area=mon_area.substring(0, mon_area.length()-1); 
	     }
		return mon_area;
	}
	/**
	 * 将中文服务区名称转换成英文名称
	 * @param name
	 * @return
	 */
	public String getEnglishName(String name){
		String eng_name="";
		String sql="select * from dic_servicesarea_tab where chinese_name like '%"+name+"%'";
		
		try {
			GDSet gd = DbComponent.Query(sql);
			if(gd.getRowCount()>0){
				eng_name = gd.getString(0, "english_name");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return eng_name;
	}
	
}
