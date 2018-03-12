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
 * ����̨����ͼ��
 * @author leeo
 *
 */
public class GJTRunplan {
	
	
	/**
	 * �������ͼ����
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
    * ��ѯ����ͼ
    * @param bean
    * @return
    */
	public Object queryRunplan(GJTRunplanBean bean){
		GJTRunplanDAO grldao = new GJTRunplanDAO();
		ArrayList result = new ArrayList();
		ASObject resObj;
		try {
			 resObj = grldao.queryRunplan(bean);
			 ArrayList list =(ArrayList) resObj.get("resultList") ;//ȡ������Ľ����
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
//			 result = StringTool.convertFlexToJavaList(list, classpath);//����������ת����bean���ٷŻؽ�����С�
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
	 * �޸�����ͼ
	 * @param bean
	 * @return
	 */
	public Object updateRunplan(GJTRunplanBean bean){
		String res="�޸�����ͼ�ɹ�!";
		//String[] sqls = new String[2];
		String runplan_id = bean.getRunplan_id();   //����ͼid
		//String runplan_type_id = bean.getRunplan_type_id(); //����ͼ����id
		String station_id = bean.getStation_id();    //����̨id
		String station_name = bean.getStation_name(); //����̨
		String sentcity_id = bean.getSentcity_id();  //�������id
		String transmiter_no = bean.getTransmiter_no();  //�������
		String freq =bean.getFreq();      //Ƶ��
		String antenna = bean.getAntenna(); //���ߺ�
		String antennatype = bean.getAntennatype();//���߳�ʽ
		String direction = bean.getDirection(); //����
		String language_id = bean.getLanguage_id(); //����id
		String power = bean.getPower();  //����
		String program_type_id = bean.getProgram_type_id(); //��Ŀ����id
		String program_type = bean.getProgram_type(); //��Ŀ����
		String service_area = bean.getService_area(); //������
		String ciraf = bean.getCiraf(); // CIRAF��
		String satellite_channel = bean.getSatellite_channel();//��������ͨ��
		String start_time = bean.getStart_time(); //������ʼʱ��
		String end_time = bean.getEnd_time();   //��������ʱ��
		String mon_area = bean.getMon_area();    //�����ղ�վ��
		String xg_mon_area = bean.getXg_mon_area();//Ч���ղ�վ��
		String rest_datetime = bean.getRest_datetime(); // //��Ϣ����
	//	String rest_time = bean.getRest_time(); //��Ϣʱ��
		String valid_start_time = bean.getValid_start_time(); //������
		String valid_end_time = bean.getValid_end_time();  //ͣ����
		String remark = bean.getRemark();//��ע
		String season_id = bean.getSeason_id();//���ڴ���
		String input_person = bean.getInput_person(); //¼����
		String weekday = bean.getWeekday();//������
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
	 * �����޸�����ͼ
	 * @param obj
	 * @return
	 */
	public Object batchupdateRunplan(ASObject obj){
		String res="�����޸�����ͼ�ɹ�!";
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
	 * ɾ������ͼ
	 * @param ids
	 * @return
	 */
	public Object delRunplan(String ids){
		String message="ɾ������ͼ�ɹ�!";
		String sql="update zres_runplan_tab set is_delete=1 where runplan_type_id=1 and runplan_id in("+ids+")";
		try {
			DbComponent.exeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return new EXEException("","ɾ������ͼ�쳣"+e.getMessage(),"");
		}
		return message;
	}
	/**
	 * ��ȡվ����Ϣ���ｫ��AB��վ��ϲ�Ϊһ��վ��
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
	 * ����Excel
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
		if(season_id!=null&&!season_id.equalsIgnoreCase("")){//���ݼ��ڴ��ŵ���Ч�ڲ�ѯ����ͼ
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
//		//��Ҫ�����ž���������ʼʱ��ͽ���ʱ�䶼Ϊ�գ���Ҫ����Ч�ڿ�ʼʱ��С�ڵ�ǰʱ�䡣
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
		String fileName = "����̨����ͼ";
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
	    	
	    	jExcel.addDate(0, 0,"����ʱ��",jExcel.dateTITLEFormat);
	    	jExcel.addDate(1, 0,"������",jExcel.dateTITLEFormat);
	    	jExcel.addDate(2, 0,"Ƶ��(KHZ)",jExcel.dateTITLEFormat);
	    	jExcel.addDate(3, 0,"����̨",jExcel.dateTITLEFormat);
	    	jExcel.addDate(4, 0,"����",jExcel.dateTITLEFormat);
	    	jExcel.addDate(5, 0,"����",jExcel.dateTITLEFormat);
	    	jExcel.addDate(6, 0,"����",jExcel.dateTITLEFormat);
	    	jExcel.addDate(7, 0,"�������",jExcel.dateTITLEFormat);
	    	jExcel.addDate(8, 0,"���ߺ�",jExcel.dateTITLEFormat);
	    	jExcel.addDate(9, 0,"���߳�ʽ",jExcel.dateTITLEFormat);
	    	jExcel.addDate(10, 0,"ITU",jExcel.dateTITLEFormat);
	    	jExcel.addDate(11, 0,"ͨ��",jExcel.dateTITLEFormat);
	    	jExcel.addDate(12, 0,"Ч���ղ�վ��",jExcel.dateTITLEFormat);
	    	jExcel.addDate(13, 0,"�����ղ�վ��",jExcel.dateTITLEFormat);
	    	jExcel.addDate(14, 0,"��Ŀ���",jExcel.dateTITLEFormat);
	    	jExcel.addDate(15, 0,"CIRAF��",jExcel.dateTITLEFormat);
	    	jExcel.addDate(16, 0,"������",jExcel.dateTITLEFormat);
	    	jExcel.addDate(17, 0,"ͣ����",jExcel.dateTITLEFormat);
	    	jExcel.addDate(18, 0,"���ڴ���",jExcel.dateTITLEFormat);
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
	 * ����ͼ���빦��
	 * @param obj
	 * @return
	 * @throws IOException 
	 * @throws BiffException 
	 */
	
	public Object importExcel(ASObject obj){
		String res="����ͼ����ɹ�!";
		
		String input_person = (String) obj.get("input_person");//������
		String runplan_type_id = (String) obj.get("runplan_type_id");//����ͼ����
		String file_name = (String) obj.get("file_name");//��Ҫ������ļ���
		GJTRunplanDAO grldao = new GJTRunplanDAO();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try{
		String path="C:\\runplan\\"+file_name+"";
		if(!new File(path).exists()){
			res="�ļ�·��������,���ϴ����ٵ���!";
			return res;
		}
		Workbook book = Workbook.getWorkbook(new File(path));
		Sheet sheet = book.getSheet(0);
		int rowNum = sheet.getRows();   //�õ�������  
		int colNum = sheet.getColumns();//�õ�������
		ArrayList list = new ArrayList();//�������ͼ�����list 
		
        for(int i=1;i<rowNum;i++){
          GJTRunplanBean bean = new GJTRunplanBean();
      	  for(int j=0;j<colNum;j++){
      		  if(j==0){//����ʱ��
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
      		  if(j==1){//������
      			  bean.setService_area(sheet.getCell(j,i).getContents());
      		  }
      		  if(j==2){//Ƶ��
      			  bean.setFreq(sheet.getCell(j,i).getContents());
      		  }
      		  if(j==3){//����̨
    			  bean.setStation_name(sheet.getCell(j,i).getContents());
    			  bean.setStation_id(Common.getStationIDByName(sheet.getCell(j,i).getContents()));
    		  }
      		  if(j==4){//����
    			  bean.setPower(sheet.getCell(j,i).getContents());
    		  }
      		  if(j==5){//����
    			  bean.setDirection(sheet.getCell(j,i).getContents());
    		  }
      		  if(j==6){//����
    			  bean.setLanguage(sheet.getCell(j,i).getContents());
    			  bean.setLanguage_id(Common.getLanguageIDByName(sheet.getCell(j,i).getContents()));
    		  }
      		  if(j==7){//�������
    			  bean.setTransmiter_no(sheet.getCell(j,i).getContents());
    		  }
      		  if(j==8){//���ߺ�
    			  bean.setAntenna(sheet.getCell(j,i).getContents());
    		  }
      		  if(j==9){//���߳�ʽ
      			bean.setAntennatype(sheet.getCell(j,i).getContents());
      		  }
      		  if(j==10){//ITU
      			 // bean.set
      		  }
      		  if(j==11){//ͨ��
    			  bean.setSatellite_channel(sheet.getCell(j,i).getContents());
    		  }
      		  if(j==12){//Ч���ղ�վ��
      			bean.setXg_mon_area(sheet.getCell(j,i).getContents());
      			  
    		  }
      		  if(j==13){//�����ղ�վ��
      			bean.setMon_area(sheet.getCell(j,i).getContents());
    		  }
      		  if(j==14){//��Ŀ����
    			  bean.setProgram_type(sheet.getCell(j,i).getContents());
    			  if(sheet.getCell(j,i).getContents().equalsIgnoreCase("����ת��")){
    				  bean.setProgram_type_id("1");
    			  }
    			  if(sheet.getCell(j,i).getContents().equalsIgnoreCase("����ֱ��")){
    				  bean.setProgram_type_id("2");
    			  }
    			  if(sheet.getCell(j,i).getContents().equalsIgnoreCase("����ֱ��")){
    				  bean.setProgram_type_id("3");
    			  }
    			  if(sheet.getCell(j,i).getContents().equalsIgnoreCase("���ڵط�")){
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
      	 //if(bean.getXg_mon_area().equalsIgnoreCase("")){//���Ч���ղ�վ���оͲ���Ҫ���ˡ�
      	  
      		 mon_area=calculateWebsite(bean);
      		 if(mon_area!=null&&!mon_area.equalsIgnoreCase("")){
      			 bean.setRemark("�Զ������վ��");
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
		return new EXEException("","����ͼ�����쳣��"+e.getMessage(),"");
	}
		return res;
	}
	/**
	 * ��������ͼ��Ϣ�Զ�����ղ�վ��
	 * @return
	 */
	public String calculateWebsite(GJTRunplanBean bean){
		String mon_area="";
		String service_area=bean.getService_area();//������
		String language_name = bean.getLanguage();//����
		String station_name = bean.getStation_name();//����̨����
		String antenna = bean.getAntenna();//���ߺ�
		String direction = bean.getDirection();//����
		String[] range = getAntennaRange(station_name,antenna,direction);
		bean.setAntennatype(range[2]);
		//������Ҫ�ж����ԭ�е�վ����Ҫ����
		mon_area = getMon_Area( bean.getXg_mon_area(),range,station_name,service_area,language_name);
	
		return mon_area;
		
	}
	/*
	 * ��������ͼ�ķ���̨�����ߺţ�����������ߵ��ղⷶΧ
	 * ���㷽��Ϊ�����ȸ��ݷ���̨�����ߺ���res_antenna_tab���ҵ���Ӧ�����߳�ʽ
	 *         �ٸ������߳�ʽȥdic_antenna_parameter_tab���ҵ���Ӧ����
	 *         ����(antenna_width�ֶ�)��Ȼ���ٽ�������ֵ��2�õ�������ֵ
	 *         �ӷ���ֵ������������ղ�ķ�Χ��������������ֵΪ40����ô��2����20�����
	 *         ����ͼ�ķ���Ϊ120��ô�õ��ķ�Χ����100-140��
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
			}else{//����Ҳ������߳�ʽ��Ӧ�������ȣ������ղⷶΧ��Ĭ��ֵ��0-0
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

		System.out.println(	p.getMon_Area("aaa",range, "564", "����.����", "Ӣ"));
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
	 * ���ݷ���̨�ͷ�λ��ȥdic_station_city_rel_tab��ȡ���С���������㷽��Ϊ��ֻҪ��λ��ֵ�������ղⷶΧ�ڶ�Ӧ�ĳ��У�
	 * ����Ϊ�ǿ����ղ�ġ���Ȼ���ٸ��ݳ���ȥres_headend_tab��õ����Ӧ��վ��code(����������Ƽ���վ������)
	 * @param range
	 * @param station
	 * @param service
	 * @param language
	 * @return
	 */
	public String getMon_Area(String mon_area_old,String[] range,String station,String service,String language){
		String mon_area="";
		/**
		 * �������ž�Ҫ�����ﲻ���вɼ���ķ���
		 * �ż�Ҫ����㲻���Ƕ���վ�� 20171026
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
	 * �����ķ���������ת����Ӣ������
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
