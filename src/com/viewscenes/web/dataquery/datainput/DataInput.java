package com.viewscenes.web.dataquery.datainput;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Element;
import org.jmask.web.controller.EXEException;

import com.viewscenes.bean.DataInputBean;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.printexcel.JExcel;
import com.viewscenes.pub.GDSet;
import com.viewscenes.pub.GDSetException;
import com.viewscenes.util.StringTool;

public class DataInput {

	public Object queryData(DataInputBean data){
		ArrayList<DataInputBean> list = null;
		StringBuffer sql = new StringBuffer(" select * from res_datainput_tab t ");
		sql.append(" where 1=1 ");
		//sql.append(" and t.data_id = ");
		if (!data.getData_type().equals(""))
			sql.append(" and t.data_type = '"+data.getData_type()+"' ");
		if (!data.getFreq().equals(""))
			sql.append(" and t.freq = "+data.getFreq()+" ");
		if (data.getLanguage_id() !=null && (!data.getLanguage_id().equals("") && !data.getLanguage_id().equals("全部")))
			sql.append(" and t.language_id = "+data.getLanguage_id()+" ");
		//国际台 Station_name字段为发射台
		if (data.getData_type().equals("1")){
			if (data.getStation_id() !=null && (!data.getStation_id().equals("") &&  !data.getStation_name().equals("全部"))){
				sql.append(" and t.station_id = "+data.getStation_id()+" ");
			if (!data.getPower().equals(""))
				sql.append(" and t.power like '%"+data.getPower()+"%' ");
			if (!data.getDirection().equals(""))
				sql.append(" and t.direction like '%"+data.getDirection()+"%' ");
			}
		//海外落地 Station_name字段为转播机构
		}else{
			if (data.getStation_id() !=null && !data.getStation_name().equals(""))
				sql.append(" and t.station_name like '%"+data.getStation_name()+"%' ");
		}
		
		if (!data.getService_area().equals(""))
			sql.append(" and t.service_area like '%"+data.getService_area()+"%' ");
		if (!data.getCiraf().equals(""))
			sql.append(" and t.ciraf like '%"+data.getCiraf()+"%' ");
		if (data.getReceive_country() !=null && (!data.getReceive_country().equals("")) &&  !data.getReceive_country().equals("全部"))
			sql.append(" and t.receive_country like '%"+data.getReceive_country()+"%' ");
		if (data.getReceive_city() != null && (!data.getReceive_city().equals("")) &&  !data.getReceive_city().equals("全部"))
			sql.append(" and t.receive_city like '%"+data.getReceive_city()+"%' ");
		if (!data.getDatasource().equals(""))
			sql.append(" and t.datasource = '"+data.getDatasource()+"' ");
		
//		sql.append(" and  t.end_time >= to_date('2000-01-01 "+data.getStart_time()+":00','yyyy-mm-dd hh24:mi:ss') ");
//		if(data.getEnd_time().equals("00:00")){
//			sql.append(" and t.start_time <= to_date('2000-01-02 "+data.getEnd_time()+":00','yyyy-mm-dd hh24:mi:ss') ");
//		}else{
//			sql.append(" and t.start_time <= to_date('2000-01-01 "+data.getEnd_time()+":00','yyyy-mm-dd hh24:mi:ss') ");
//		}
//		sql.append(" and t.receive_date = to_date('"+data.getReceive_date()+"','yyyy-mm-dd hh24:mi:ss') ");
//		sql.append(" and t.receive_time = '"+data.getReceive_time()+"' ");
		if (data.getProgram_type() != null && !data.getProgram_type().equals("") && !data.getProgram_type().equals("全部"))
			sql.append(" and t.program_type = '"+data.getProgram_type()+"' ");
		if (!data.getField_strength().equals(""))
			sql.append(" and t.field_strength like '%"+data.getField_strength()+"%' ");
		if (!data.getS().equals(""))
			sql.append(" and t.s = "+data.getS()+" ");
		if (!data.getI().equals(""))
			sql.append(" and t.i = "+data.getI()+" ");
		if (!data.getO().equals(""))
			sql.append(" and t.o = "+data.getO()+" ");
		if (!data.getLevel_value().equals(""))
			sql.append(" and t.level_value = '"+data.getLevel_value()+"' ");
		if (!data.getRemark().equals(""))
			sql.append(" and t.remark like '%"+data.getRemark()+"%' ");
		sql.append(" order by t.receive_date desc ");
		try {
			GDSet set = DbComponent.Query(sql.toString());
			if (set != null){
				list = new ArrayList<DataInputBean>();
				for(int i=0;i<set.getRowCount();i++){
					DataInputBean _data = new DataInputBean();
					_data.setData_id(set.getString(i, "data_id"));
					_data.setData_type(set.getString(i, "data_type"));
					_data.setFreq(set.getString(i, "freq"));
					_data.setLanguage_id(set.getString(i, "language_id"));
					_data.setLanguage_name(set.getString(i, "language_name"));
					_data.setStation_id(set.getString(i, "station_id"));
					_data.setStation_name(set.getString(i, "station_name"));
					_data.setPower(set.getString(i, "power"));
					_data.setDirection(set.getString(i, "direction"));
					_data.setService_area(set.getString(i, "service_area"));
					_data.setCiraf(set.getString(i, "ciraf"));
					_data.setReceive_country(set.getString(i, "receive_country"));
					_data.setReceive_city(set.getString(i, "receive_city"));
					_data.setDatasource(set.getString(i, "datasource"));
					String _start_time=set.getString(i, "start_time");
					String _end_time=set.getString(i, "end_time");
					if(_start_time==null||_start_time.length()<19){
						_data.setStart_time(_start_time);
					}else{
						_data.setStart_time(_start_time.substring(11,16));
					}
					if(_end_time==null||_end_time.length()<19){
						_data.setEnd_time(_end_time);
					}else{
						_data.setEnd_time(_end_time.substring(11,16));
					}
					String _receive_date=set.getString(i, "receive_date");
					if(_receive_date==null||_receive_date.length()<19){
						_data.setReceive_date(_receive_date);
					}else{
						_data.setReceive_date(_receive_date.substring(0,10));
					}
					
					_data.setReceive_time(set.getString(i, "receive_time"));
					_data.setProgram_type(set.getString(i, "program_type"));
					_data.setField_strength(set.getString(i, "field_strength"));
					_data.setS(set.getString(i, "s"));
					_data.setI(set.getString(i, "i"));
					_data.setO(set.getString(i, "o"));
					_data.setLevel_value(set.getString(i, "level_value"));
					_data.setRemark(set.getString(i, "remark"));
					list.add(_data);
				}
			}
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("", "资料查询错误:"+e.getMessage(), data);
		} catch (GDSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("", "资料查询错误:"+e.getMessage(), data);
		}

		return list;
	}
	
	public Object updateData(DataInputBean data){
		
		StringBuffer sql  = new StringBuffer(" update res_datainput_tab t ");
		sql.append(" set data_type = '"+data.getData_type()+"', ");
		sql.append(" freq = "+data.getFreq()+", ");
		sql.append(" language_id = "+data.getLanguage_id()+", ");
		sql.append(" language_name = '"+data.getLanguage_name()+"', ");
		sql.append(" station_id = '"+data.getStation_id()+"', ");
		sql.append(" station_name = '"+data.getStation_name()+"', ");
		sql.append(" power = '"+data.getPower()+"', ");
		sql.append(" direction = '"+data.getDirection()+"', ");
		sql.append(" service_area = '"+data.getService_area()+"', ");
		sql.append(" ciraf = '"+data.getCiraf()+"', ");
		sql.append(" receive_country = '"+data.getReceive_country()+"', ");
		sql.append(" receive_city = '"+data.getReceive_city()+"', ");
		sql.append(" datasource = '"+data.getDatasource()+"', ");
		sql.append(" start_time = '2000-01-01 "+data.getStart_time()+":00', ");
		if(data.getEnd_time().equals("00:00")){
			sql.append(" end_time = '2000-01-02 "+data.getEnd_time()+":00', ");
		}else{
			sql.append(" end_time = '2000-01-01 "+data.getEnd_time()+":00', ");
		}
		sql.append(" receive_date = '"+data.getReceive_date()+"', ");
		sql.append(" receive_time = '"+data.getReceive_time()+"', ");
		sql.append(" program_type = '"+data.getProgram_type()+"', ");
		sql.append(" field_strength = '"+data.getField_strength()+"', ");
		sql.append(" s = '"+data.getS()+"', ");
		sql.append(" i = '"+data.getI()+"', ");
		sql.append(" o = '"+data.getO()+"', ");
		sql.append(" level_value = '"+data.getLevel_value()+"', ");
		sql.append(" remark = '"+data.getRemark()+"' ");
		sql.append("  where t.data_id =  '"+data.getData_id()+"' ");
		       
		       
		try {
			DbComponent.exeUpdate(sql.toString());
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("", "资料修改失败:"+e.getMessage(), data);
		}    
		
		return "资料修改成功";
	}
	
	public Object addData(DataInputBean data){
		
		StringBuffer sql = new StringBuffer(" insert into res_datainput_tab ");
		sql.append(" (data_id,data_type,freq,language_id,language_name,station_id,station_name,power,direction,service_area,ciraf,receive_country, ");
		sql.append(" receive_city,datasource,start_time,end_time,receive_date,receive_time,program_type,field_strength,s,i,o,level_value,remark) ");
		sql.append(" values( ");
		sql.append(" res_datainput_seq.nextval, ");
		sql.append(" '"+data.getData_type()+"', ");
		sql.append(" '"+data.getFreq()+"', ");
		sql.append(" '"+data.getLanguage_id()+"', ");
		sql.append(" '"+data.getLanguage_name()+"', ");
		sql.append(" '"+data.getStation_id()+"', ");
		sql.append(" '"+data.getStation_name()+"', ");
		sql.append(" '"+data.getPower()+"', ");
		sql.append(" '"+data.getDirection()+"', ");
		sql.append(" '"+data.getService_area()+"', ");
		sql.append(" '"+data.getCiraf()+"', ");
		sql.append(" '"+data.getReceive_country()+"', ");
		sql.append(" '"+data.getReceive_city()+"', ");
		sql.append(" '"+data.getDatasource()+"', ");
		sql.append(" '2000-01-01 "+data.getStart_time()+":00', ");
		if(data.getEnd_time().equals("00:00")){
			sql.append(" '2000-01-02 "+data.getEnd_time()+":00', ");
		}else{
			sql.append(" '2000-01-01 "+data.getEnd_time()+":00', ");
		}
		sql.append(" trunc(to_date('"+data.getReceive_date()+"','yyyy-mm-dd hh24:mi:ss')), ");
		sql.append(" '"+data.getReceive_time()+"', ");
		sql.append(" '"+data.getProgram_type()+"', ");
		sql.append(" '"+data.getField_strength()+"', ");
		sql.append(" '"+data.getS()+"', ");
		sql.append(" '"+data.getI()+"', ");
		sql.append(" '"+data.getO()+"', ");
		sql.append(" '"+data.getLevel_value()+"', ");
		sql.append(" '"+data.getRemark()+"' ) ");
   
		try {
			DbComponent.exeUpdate(sql.toString());
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("", "资料录入错误:"+e.getMessage(), data);
			
		}
		
		return "资料录入成功";
	}
	
	public Object delData(ArrayList<DataInputBean> list){
		
		if (list != null){
			String[] sqls = new String[list.size()];
			for(int i=0;i<list.size();i++){
				
				DataInputBean data = list.get(i);
				String sql = " delete from res_datainput_tab t where t.data_id =  "+ data.getData_id();
				sqls[i] = sql;
			}
			
			try {
				DbComponent.exeBatch(sqls);
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new EXEException("", "资料删除失败:"+e.getMessage(), sqls);
			}
		}
		return "资料删除成功";
	}
	
	public Object proofreadData(){
		return null;
	}
	
	public Object auditData(){
		return null;
	}
	
	public void doExel(String msg, HttpServletRequest request,
			HttpServletResponse response){
		Element root = StringTool.getXMLRoot(msg);
		String fileName = "国际台资料信息";
		String downFileName = "国际台资料信息";
		int m=0;
		String data_type=root.getChildText("data_type");
		
		String freq=root.getChildText("freq");
		String language_id=root.getChildText("language_id");
		String station_id=root.getChildText("station_id");
		String station_name=root.getChildText("station_name");
		String service_area=root.getChildText("service_area");
		
		String ciraf=root.getChildText("ciraf");
		String receive_country=root.getChildText("receive_country");
		String receive_city=root.getChildText("receive_city");
		String datasource=root.getChildText("datasource");
		String program_type=root.getChildText("program_type");
		String field_strength=root.getChildText("field_strength");
		
		String s=root.getChildText("s");
		String i=root.getChildText("i");
		String o=root.getChildText("o");
		String level_value=root.getChildText("level_value");
		String remark=root.getChildText("remark");
		String power=root.getChildText("power");
		String direction=root.getChildText("direction");
		StringBuffer sql = new StringBuffer(" select t.* ,d.type from res_datainput_tab t ,dic_runplan_type_tab d");
		sql.append(" where t.program_type= d.runplan_type_id");
		if (data_type!=null&&!data_type.equals(""))
			sql.append(" and t.data_type = '"+data_type+"' ");
		if (!freq.equals(""))
			sql.append(" and t.freq = "+freq+" ");
		if (language_id !=null && (!language_id.equals("") && !language_id.equals("全部")))
			sql.append(" and t.language_id = "+language_id+" ");
		//国际台 Station_name字段为发射台
		if (data_type.equals("1")){
			m=2;
			if (station_id !=null && (!station_id.equals("") &&  !station_name.equals("全部"))){
				sql.append(" and t.station_id = "+station_id+" ");
			if (!power.equals(""))
				sql.append(" and t.power like '%"+power+"%' ");
			if (!direction.equals(""))
				sql.append(" and t.direction like '%"+direction+"%' ");
			}
		//海外落地 Station_name字段为转播机构
		}else{
			fileName = "海外落地资料信息";
			downFileName = "海外落地资料信息";
			if (station_id !=null && !station_name.equals(""))
				sql.append(" and t.station_name like '%"+station_name+"%' ");
		}
		
		if (!service_area.equals(""))
			sql.append(" and t.service_area like '%"+service_area+"%' ");
		if (!ciraf.equals(""))
			sql.append(" and t.ciraf like '%"+ciraf+"%' ");
		if (receive_country !=null && (!receive_country.equals("")) &&  !receive_country.equals("全部"))
			sql.append(" and t.receive_country like '%"+receive_country+"%' ");
		if (receive_city != null && (!receive_city.equals("")) &&  !receive_city.equals("全部"))
			sql.append(" and t.receive_city like '%"+receive_city+"%' ");
		if (!datasource.equals(""))
			sql.append(" and t.datasource = '"+datasource+"' ");
		if (program_type != null && !program_type.equals("") && !program_type.equals("全部"))
			sql.append(" and t.program_type = '"+program_type+"' ");
		if (!field_strength.equals(""))
			sql.append(" and t.field_strength like '%"+field_strength+"%' ");
		if (!s.equals(""))
			sql.append(" and t.s = "+s+" ");
		if (!i.equals(""))
			sql.append(" and t.i = "+i+" ");
		if (!o.equals(""))
			sql.append(" and t.o = "+o+" ");
		if (!level_value.equals(""))
			sql.append(" and t.level_value = '"+level_value+"' ");
		if (!remark.equals(""))
			sql.append(" and t.remark like '%"+remark+"%' ");
		sql.append(" order by t.receive_date desc ");
		try {
			JExcel jExcel=new JExcel();
			downFileName = jExcel.openDocument();
			jExcel.WorkBookGetSheet(0);
			GDSet gd = DbComponent.Query(sql.toString());
			int n=gd.getRowCount();
			if(n>0){
				for(int j=0;j<n;j++){
					jExcel.addDate(0, j+1, gd.getString(j, "freq"), jExcel.dateCellFormat);
					jExcel.addDate(1, j+1, gd.getString(j, "start_time"), jExcel.dateCellFormat);
					jExcel.addDate(2, j+1, gd.getString(j, "end_time"), jExcel.dateCellFormat);
					jExcel.addDate(3, j+1, gd.getString(j, "language_name"), jExcel.dateCellFormat);
					if (data_type.equals("1")){
						
						jExcel.addDate(4, j+1, gd.getString(j, "station_name"), jExcel.dateCellFormat);			
						jExcel.addDate(5, j+1, gd.getString(j, "power"), jExcel.dateCellFormat);
						jExcel.addDate(6, j+1, gd.getString(j, "direction"), jExcel.dateCellFormat);
					}else{
						jExcel.addDate(4, j+1, gd.getString(j, "station_name"), jExcel.dateCellFormat);
					}
					jExcel.addDate(5+m, j+1, gd.getString(j, "service_area"), jExcel.dateCellFormat);
					jExcel.addDate(6+m, j+1, gd.getString(j, "ciraf"), jExcel.dateCellFormat);
					if(gd.getString(j, "receive_country").equals("全部")){
						jExcel.addDate(7+m, j+1, "", jExcel.dateCellFormat);
					}else{
						jExcel.addDate(7+m, j+1, gd.getString(j, "receive_country"), jExcel.dateCellFormat);
					}
					if(gd.getString(j, "receive_city").equals("全部")){
						jExcel.addDate(8+m, j+1, "", jExcel.dateCellFormat);
					}else{
						jExcel.addDate(8+m, j+1, gd.getString(j, "receive_city"), jExcel.dateCellFormat);
					}
					jExcel.addDate(9+m, j+1, gd.getString(j, "datasource"), jExcel.dateCellFormat);
					jExcel.addDate(10+m, j+1, gd.getString(j, "type"), jExcel.dateCellFormat);
					jExcel.addDate(11+m, j+1, gd.getString(j, "receive_date"), jExcel.dateCellFormat);
					jExcel.addDate(12+m, j+1, gd.getString(j, "s"), jExcel.dateCellFormat);
					jExcel.addDate(13+m, j+1, gd.getString(j, "i"), jExcel.dateCellFormat);
					jExcel.addDate(14+m, j+1, gd.getString(j, "o"), jExcel.dateCellFormat);
					jExcel.addDate(15+m, j+1, gd.getString(j, "field_strength"), jExcel.dateCellFormat);
					jExcel.addDate(16+m, j+1, gd.getString(j, "remark"), jExcel.dateCellFormat);
   
				}
			}
			jExcel.getWorkSheet().setRowView(0, 600);
			jExcel.getWorkSheet().setColumnView(0, 20);
			jExcel.getWorkSheet().setColumnView(1, 20);
			jExcel.getWorkSheet().setColumnView(2, 20);
			jExcel.getWorkSheet().setColumnView(3, 20);
			jExcel.getWorkSheet().setColumnView(4, 20);
			jExcel.getWorkSheet().setColumnView(5, 20);
			jExcel.getWorkSheet().setColumnView(6, 20);
			jExcel.getWorkSheet().setColumnView(7, 20);
			jExcel.getWorkSheet().setColumnView(8, 20);
			jExcel.getWorkSheet().setColumnView(9, 20);
			jExcel.getWorkSheet().setColumnView(10, 20);
			jExcel.getWorkSheet().setColumnView(11, 20);
			jExcel.getWorkSheet().setColumnView(12, 20);
			jExcel.getWorkSheet().setColumnView(13, 20);
			jExcel.getWorkSheet().setColumnView(14, 20);
			jExcel.getWorkSheet().setColumnView(15, 20);
			jExcel.getWorkSheet().setColumnView(16, 20);
			jExcel.getWorkSheet().setColumnView(17, 20);
			jExcel.getWorkSheet().setColumnView(18, 20);
			jExcel.addDate(0, 0, "频率(kHz)", jExcel.dateTITLEFormat);
			jExcel.addDate(1, 0, "开播时间", jExcel.dateTITLEFormat);
			jExcel.addDate(2, 0, "停播时间", jExcel.dateTITLEFormat);
			jExcel.addDate(3, 0, "节目", jExcel.dateTITLEFormat);
			if (data_type.equals("1")){
				jExcel.addDate(4, 0, "发射台", jExcel.dateTITLEFormat);			
				jExcel.addDate(5, 0, "发射功率", jExcel.dateTITLEFormat);
				jExcel.addDate(6, 0, "发射方向", jExcel.dateTITLEFormat);
			}else{
				jExcel.addDate(4, 0, "转播机构", jExcel.dateTITLEFormat);
			}
			jExcel.addDate(5+m, 0, "服务区", jExcel.dateTITLEFormat);
			jExcel.addDate(6+m, 0, "CIRAF区", jExcel.dateTITLEFormat);
			jExcel.addDate(7+m, 0, "收测国家", jExcel.dateTITLEFormat);
			jExcel.addDate(8+m, 0, "收测城市", jExcel.dateTITLEFormat);
			jExcel.addDate(9+m, 0, "资料来源", jExcel.dateTITLEFormat);
			jExcel.addDate(10+m, 0, "节目类别", jExcel.dateTITLEFormat);
			jExcel.addDate(11+m, 0, "收测日期", jExcel.dateTITLEFormat);
			jExcel.addDate(12+m, 0, "S", jExcel.dateTITLEFormat);
			jExcel.addDate(13+m, 0, "I", jExcel.dateTITLEFormat);
			jExcel.addDate(14+m, 0, "O", jExcel.dateTITLEFormat);
			jExcel.addDate(15+m, 0, "场强", jExcel.dateTITLEFormat);
			jExcel.addDate(16+m, 0, "备注", jExcel.dateTITLEFormat);
			jExcel.getWorkSheet().setName(fileName);
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
			int j = -1;
			while ((j = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, j);
			}
			outputStream.flush();
			outputStream.close();
			outputStream = null;
			
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
