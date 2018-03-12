package com.viewscenes.web.dicManager;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Element;
import org.jmask.web.controller.EXEException;

import com.viewscenes.bean.dicManager.StationBean;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.printexcel.JExcel;
import com.viewscenes.pub.GDSet;
import com.viewscenes.pub.GDSetException;
import com.viewscenes.sys.TableInfoCache;
import com.viewscenes.util.StringTool;

import flex.messaging.io.amf.ASObject;

public class StationManager {

	/**
	 * 查询发射台信息
	 * @param bean
	 * @return
	 */
	public Object queryStation(StationBean bean){
		ASObject resObj=null;
		String name = bean.getName();
		String sql="select * from res_transmit_station_tab where is_delete=0";
		if(name!=null&&!name.equalsIgnoreCase("")){
			sql+=" and name like '%"+name+"%'";
		}
		sql+="order by name";
		try {
			resObj = StringTool.pageQuerySql(sql, bean);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("",e.getMessage(),"");
		}
		return resObj;
	}
	 public Object queryAllStation(ASObject obj){
		 ArrayList<Object> list=null;
		 String name=StationBean.class.getName();
		 String sql="select * from res_transmit_station_tab where is_delete=0";
		 try {
			GDSet set=DbComponent.Query(sql);
			 list=StringTool.converGDSetToBeanList(set, name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("",e.getMessage(),"");
		}
		 return	list;
	 }
	/**
	 * 添加数据入库 
	 * @param bean
	 * @return
	 */
	public Object addStation(StationBean bean){
		String name = bean.getName();
		String code = bean.getCode();
		String country = bean.getCountry();
		String province = bean.getProvince();
		String city = bean.getCity();
		String county = bean.getCounty();
		String address = bean.getAddress();
		String longitude = bean.getLongitude();
		String latitude = bean.getLatitude();
		String station_type=bean.getStation_type();
		String broadcast_direction= bean.getBroadcast_direction();
		try {
			String station_id=getStationNextVal();
			StringBuffer sql=new StringBuffer("insert into res_transmit_station_tab (station_id,name,code,country,province,");
            sql.append("city,county,address,longitude,latitude,station_type,broadcast_direction,is_delete)");
            sql.append(" values("+station_id+",'"+name+"','"+code+"','"+country+"','"+province+"','"+city+"',");
            sql.append("'"+county+"','"+address+"','"+longitude+"','"+latitude+"','"+station_type+"','"+broadcast_direction+"',0)");

			DbComponent.exeUpdate(sql.toString());
			bean.setStation_id(station_id);
			 TableInfoCache as =new TableInfoCache();
  		     as.refreshTableMap("res_transmit_station_tab",bean,bean.getStation_id(),"1");
		} catch (Exception e) {
			e.printStackTrace();
			return new EXEException("", "后台错误!", null);
		}    
		return "";
	}
	/**
	 * 修改发射台信息
	 * @param bean
	 * @return
	 */
	public Object updateStationInfo(StationBean bean){
		String station_id = bean.getStation_id();
		String name = bean.getName();
		String code = bean.getCode();
		String country = bean.getCountry();
		String province = bean.getProvince();
		String city = bean.getCity();
		String county = bean.getCounty();
		String address = bean.getAddress();
		String longitude = bean.getLongitude();
		String latitude = bean.getLatitude();
		String station_type=bean.getStation_type();
		String broadcast_direction= bean.getBroadcast_direction();
		StringBuffer sql=new StringBuffer("update res_transmit_station_tab set name='"+name+"',code='"+code+"',country='"+country+"',province='"+province+"', ");
		             sql.append("city='"+city+"',county='"+county+"',address='"+address+"',longitude='"+longitude+"',latitude='"+latitude+"',broadcast_direction='"+broadcast_direction+"',station_type='"+station_type+"' where station_id='"+station_id+"'");
		try {
			DbComponent.exeUpdate(sql.toString());
			 TableInfoCache as =new TableInfoCache();
  		     as.refreshTableMap("res_transmit_station_tab",bean,bean.getStation_id(),"2");

		} catch (Exception e) {
			e.printStackTrace();
			return new EXEException("", "后台错误!", null);
		}
		
	    return "";
	}
	/**
	 * 删除发射台信息
	 * @param obj
	 * @return
	 */
	public String delStation(ASObject obj){
		String res="成功删除发射台信息！";
		String ids = (String) obj.get("ids");
		String sql = "update res_transmit_station_tab set is_delete=1 where station_id in ("+ids+")";
		try {
			DbComponent.exeUpdate(sql);
			StationBean bean=new StationBean();
			 TableInfoCache as =new TableInfoCache();
				String[] delArr = ids.split(",");
				 for(int i=0;i<delArr.length;i++){
					if(delArr[i].length()>3){
						int n=delArr[i].split("'").length;
						if(n>1){
							bean.setStation_id(delArr[i].split("'")[1]);
						}else{
							bean.setStation_id(delArr[i]);
						}
						 as.refreshTableMap("res_transmit_station_tab",bean,bean.getStation_id(),"3"); 
					}
				}
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res="删除发射台信息异常："+e.getMessage();
		}
		return res;
	}
	
	/**
	 * 导出发射台信息信息
	 * @param msg
	 * @param request
	 * @param response
	 */
	public void getStationExcel(String msg,HttpServletRequest request,HttpServletResponse response){
		Element root = StringTool.getXMLRoot(msg);
		HashMap<String,String> map=new HashMap<String, String>();

		String fileName="发射台信息";
		String downFileName = "发射台信息";
		String name=root.getChildText("name");
	
		String sql="select * from res_transmit_station_tab where is_delete=0 ";
		if(name!=null&&!name.equalsIgnoreCase("")){
			sql+=" and name like '%"+name+"%'";
		}
		sql+="order by name";

		try {
			JExcel jExcel=new JExcel();
			downFileName=jExcel.openDocument();
			jExcel.WorkBookGetSheet(0);
			GDSet gd=DbComponent.Query(sql);
			int n=gd.getRowCount();
			if(n>0){
				for(int i=0;i<n;i++){
					jExcel.addDate(0, i+1,gd.getString(i, "name"),jExcel.dateCellFormat);
					jExcel.addDate(1, i+1," "+gd.getString(i, "code").toString(),jExcel.dateCellFormat);
					jExcel.addDate(2, i+1,gd.getString(i, "broadcast_direction"),jExcel.dateCellFormat);
					jExcel.addDate(3, i+1,gd.getString(i, "station_type"),jExcel.dateCellFormat);
					jExcel.addDate(4, i+1,gd.getString(i, "country"),jExcel.dateCellFormat);
					jExcel.addDate(5, i+1,gd.getString(i, "province"),jExcel.dateCellFormat);
					jExcel.addDate(6, i+1,gd.getString(i, "city"),jExcel.dateCellFormat);
					jExcel.addDate(7, i+1,gd.getString(i, "county"),jExcel.dateCellFormat);
					jExcel.addDate(8, i+1,gd.getString(i, "address"),jExcel.dateCellFormat);
					jExcel.addDate(9, i+1,gd.getString(i, "longitude"),jExcel.dateCellFormat);
					jExcel.addDate(10, i+1,gd.getString(i, "latitude"),jExcel.dateCellFormat);	
					
				}
			}
			jExcel.getWorkSheet().setRowView(0, 600);
			jExcel.getWorkSheet().setColumnView(0, 30);
	    	jExcel.getWorkSheet().setColumnView(1, 30);
	    	jExcel.getWorkSheet().setColumnView(2, 30);	
	    	jExcel.getWorkSheet().setColumnView(3, 30);
	    	jExcel.getWorkSheet().setColumnView(4, 30);
	    	jExcel.getWorkSheet().setColumnView(5, 30);
			jExcel.getWorkSheet().setColumnView(6, 30);
	    	jExcel.getWorkSheet().setColumnView(7, 30);
	    	jExcel.getWorkSheet().setColumnView(8, 50);	
	    	jExcel.getWorkSheet().setColumnView(9, 30);
	    	jExcel.getWorkSheet().setColumnView(10, 30);
	    	
	    	
			jExcel.addDate(0, 0,"发射台名称",jExcel.dateTITLEFormat);
			jExcel.addDate(1, 0,"代号",jExcel.dateTITLEFormat);
			jExcel.addDate(2, 0,"播向区",jExcel.dateTITLEFormat);
			jExcel.addDate(3, 0,"发射台所属",jExcel.dateTITLEFormat);
			jExcel.addDate(4, 0,"国家",jExcel.dateTITLEFormat);
			jExcel.addDate(5, 0,"省",jExcel.dateTITLEFormat);
			jExcel.addDate(6, 0,"市",jExcel.dateTITLEFormat);
			jExcel.addDate(7, 0,"县",jExcel.dateTITLEFormat);
			jExcel.addDate(8, 0,"地址",jExcel.dateTITLEFormat);
			jExcel.addDate(9, 0,"经度",jExcel.dateTITLEFormat);
			jExcel.addDate(10, 0,"纬度",jExcel.dateTITLEFormat);
			
			jExcel.getWorkSheet().setName("发射台信息");
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
	private static String getStationNextVal() throws DbException, GDSetException{
		
		String sql = " select RES_RESOURSE_SEQ.nextval val from dual ";
		
		
		GDSet set = DbComponent.Query(sql);
		
		String val = set.getString(0, "val");
		
		return val;
	}
}
