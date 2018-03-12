package com.viewscenes.web.dicManager;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.jmask.web.controller.EXEException;

import com.viewscenes.bean.dicManager.AzimuthBean;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.util.StringTool;

import flex.messaging.io.amf.ASObject;

/**
 * 方位角信息维护类
 * @author 王福祥
 * @date 2012/12/14
 */
public class AzimuthManager {
	
	public Object queryAzimuth(AzimuthBean bean){
		ASObject resObj=null;
		String station_name=bean.getStation_name();
		String city = bean.getCity_name();
		String sql="select * from dic_station_city_rel_tab where 1=1";
		if(station_name!=null&&!station_name.equalsIgnoreCase("")){
			sql+=" and station_name like '%"+station_name+"%'";
		}
		if(city!=null&&!city.equalsIgnoreCase("")){
			sql+=" and city_name like '%"+city+"%'";
		}
		sql+=" order by station_name";
		try {
			resObj = StringTool.pageQuerySql(sql, bean);
		} catch (Exception e) {
			e.printStackTrace();
			return new EXEException("",e.getMessage(),"");
		}
		return resObj;
	}

	public String updateAzimuth(ASObject obj){
		String res="";
		int aCount=0;//添加成功的计数
		int aNum=0;//添加失败的计数
		int uCount=0;//修改成功的计数
		int uNum=0;//修改失败的计数
		ArrayList addList = (ArrayList) obj.get("add");
		ArrayList updateList = (ArrayList) obj.get("update");
		try{
		 if(addList.size()>0){
			for(int i=0;i<addList.size();i++){
				AzimuthBean bean = (AzimuthBean) addList.get(i);
				if( addAzimuth(bean)) aCount++;
				else aNum++;
			}
			res+="成功添加"+aCount+"条方位角信息,失败"+aNum+"条\r";
		 }
		 if(updateList.size()>0){
			for(int j=0;j<updateList.size();j++){
				AzimuthBean bean = (AzimuthBean) updateList.get(j);
				if(updateAzimuthinfo(bean)) uCount++;
				else uNum++;
			}
			res+="成功修改"+uCount+"条方位角信息,失败"+uNum+"条\r";
		 }
		}catch(Exception e){
			if(e.getMessage().indexOf("违反唯一约束条件")>=0){
				res="方位角信息重复！";
			}else
			res=e.getMessage();
		}
		return res;
	}
	
	/**
	 * 添加方位角信息数据入库 
	 * @param bean
	 * @return
	 * @throws Exception 
	 */
	public Boolean addAzimuth(AzimuthBean bean) throws Exception{
		Boolean flag=false;
		String station_name = bean.getStation_name();
		String city_name = bean.getCity_name();
		String circle_distance = bean.getCircle_distance();
		String azimuth = bean.getAzimuth();
		String longitude = bean.getLongitude();
		String latitude = bean.getLatitude();
		StringBuffer sql1= new StringBuffer();
		sql1.append("insert into dic_station_city_rel_tab (id,station_name,city_name,circle_distance,azimuth,longitude,latitude)");
		sql1.append(" values(ALL_DIC_SEQ.nextval,'"+station_name+"','"+city_name+"','"+circle_distance+"','"+azimuth+"','"+longitude+"','"+latitude+"')");
		if(DbComponent.exeUpdate(sql1.toString()))
			 flag=true;
		else flag=false;
		return flag;
	}
	/**
	 * 修改方位角信息
	 * @param bean
	 * @return
	 * @throws Exception 
	 */
	public Boolean updateAzimuthinfo(AzimuthBean bean) throws Exception{
		Boolean flag=false;
		String id = bean.getId();
		String station_name = bean.getStation_name();
		String city_name = bean.getCity_name();
		String circle_distance = bean.getCircle_distance();
		String azimuth = bean.getAzimuth();
		String longitude = bean.getLongitude();
		String latitude = bean.getLatitude();
		StringBuffer sql=new StringBuffer("update dic_station_city_rel_tab set station_name='"+station_name+"',city_name='"+city_name+"',circle_distance='"+circle_distance+"', " +
				                           "azimuth='"+azimuth+"',longitude='"+longitude+"',latitude='"+latitude+"' " +
				                           "where id='"+id+"'");
		if(DbComponent.exeUpdate(sql.toString()))
		flag=true;
		else flag=false;
	    return flag;
	}
	/**
	 * 删除方位角信息
	 * @param codes
	 * @return
	 */
	public Object delAzimuth(String id){
		String message="删除方位角信息成功!";
		String sql="delete  from dic_station_city_rel_tab where  id in("+id+")";
		try {
			DbComponent.exeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return new EXEException("","删除方位角信息异常"+e.getMessage(),"");
		}
		return message;
	}
	
	/**
	 * 运行图导入功能
	 * @param obj
	 * @return
	 * @throws IOException 
	 * @throws BiffException 
	 */
	
	public Object importExcel(ASObject obj){
		String res="方位角信息导入成功!";
		//DbComponent.temp=0;
		String file_name = (String) obj.get("file_name");//需要导入的文件名
		try{
		    String path="C:\\runplan\\"+file_name+"";
		    if(!new File(path).exists()){
			  res="文件路径不存在,请上传后再导入!";
			  return res;
		    }
		Workbook book = Workbook.getWorkbook(new File(path));
		String[] sheets=book.getSheetNames();
		for(int k=0;k<sheets.length;k++){
			ArrayList list = new ArrayList();//存放运行图对象的list 
			if(sheets[k].equalsIgnoreCase("")) continue;
		    String station_name = sheets[k];
			Sheet sheet = book.getSheet(sheets[k]);
			int rowNum = sheet.getRows();   //得到总行数  
			int colNum = sheet.getColumns();//得到总列数
		
	        for(int i=1;i<rowNum;i++){
	          AzimuthBean bean = new AzimuthBean();
	      	  for(int j=1;j<colNum;j++){
	      		  bean.setStation_name(station_name);
	      		  if(j==1){//城市
	      			  bean.setCity_name(sheet.getCell(j, i).getContents());
	      		  }
	      		  if(j==2){//大圆距离
	      			bean.setCircle_distance(sheet.getCell(j, i).getContents());
	      		  }
	      		  if(j==3){//方位角
	      			bean.setAzimuth(sheet.getCell(j, i).getContents());
	      		  }
	      		  if(j==4){//经度
	      			  bean.setLongitude(sheet.getCell(j, i).getContents());
	      		  }
	      		  if(j==5){//纬度
	      			  bean.setLatitude(sheet.getCell(j, i).getContents());
	      		  }
	          }
	      	list.add(bean);
	       }
	       for(int m=0;m<list.size();m++){
				this.addAzimuth((AzimuthBean)list.get(m));
	        }
		}
	}catch(Exception e){
		e.printStackTrace();
		return new EXEException("","方位角信息导入异常："+e.getMessage(),"");
	}
	   return res;
	}
}
