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
 * ��λ����Ϣά����
 * @author ������
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
		int aCount=0;//��ӳɹ��ļ���
		int aNum=0;//���ʧ�ܵļ���
		int uCount=0;//�޸ĳɹ��ļ���
		int uNum=0;//�޸�ʧ�ܵļ���
		ArrayList addList = (ArrayList) obj.get("add");
		ArrayList updateList = (ArrayList) obj.get("update");
		try{
		 if(addList.size()>0){
			for(int i=0;i<addList.size();i++){
				AzimuthBean bean = (AzimuthBean) addList.get(i);
				if( addAzimuth(bean)) aCount++;
				else aNum++;
			}
			res+="�ɹ����"+aCount+"����λ����Ϣ,ʧ��"+aNum+"��\r";
		 }
		 if(updateList.size()>0){
			for(int j=0;j<updateList.size();j++){
				AzimuthBean bean = (AzimuthBean) updateList.get(j);
				if(updateAzimuthinfo(bean)) uCount++;
				else uNum++;
			}
			res+="�ɹ��޸�"+uCount+"����λ����Ϣ,ʧ��"+uNum+"��\r";
		 }
		}catch(Exception e){
			if(e.getMessage().indexOf("Υ��ΨһԼ������")>=0){
				res="��λ����Ϣ�ظ���";
			}else
			res=e.getMessage();
		}
		return res;
	}
	
	/**
	 * ��ӷ�λ����Ϣ������� 
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
	 * �޸ķ�λ����Ϣ
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
	 * ɾ����λ����Ϣ
	 * @param codes
	 * @return
	 */
	public Object delAzimuth(String id){
		String message="ɾ����λ����Ϣ�ɹ�!";
		String sql="delete  from dic_station_city_rel_tab where  id in("+id+")";
		try {
			DbComponent.exeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return new EXEException("","ɾ����λ����Ϣ�쳣"+e.getMessage(),"");
		}
		return message;
	}
	
	/**
	 * ����ͼ���빦��
	 * @param obj
	 * @return
	 * @throws IOException 
	 * @throws BiffException 
	 */
	
	public Object importExcel(ASObject obj){
		String res="��λ����Ϣ����ɹ�!";
		//DbComponent.temp=0;
		String file_name = (String) obj.get("file_name");//��Ҫ������ļ���
		try{
		    String path="C:\\runplan\\"+file_name+"";
		    if(!new File(path).exists()){
			  res="�ļ�·��������,���ϴ����ٵ���!";
			  return res;
		    }
		Workbook book = Workbook.getWorkbook(new File(path));
		String[] sheets=book.getSheetNames();
		for(int k=0;k<sheets.length;k++){
			ArrayList list = new ArrayList();//�������ͼ�����list 
			if(sheets[k].equalsIgnoreCase("")) continue;
		    String station_name = sheets[k];
			Sheet sheet = book.getSheet(sheets[k]);
			int rowNum = sheet.getRows();   //�õ�������  
			int colNum = sheet.getColumns();//�õ�������
		
	        for(int i=1;i<rowNum;i++){
	          AzimuthBean bean = new AzimuthBean();
	      	  for(int j=1;j<colNum;j++){
	      		  bean.setStation_name(station_name);
	      		  if(j==1){//����
	      			  bean.setCity_name(sheet.getCell(j, i).getContents());
	      		  }
	      		  if(j==2){//��Բ����
	      			bean.setCircle_distance(sheet.getCell(j, i).getContents());
	      		  }
	      		  if(j==3){//��λ��
	      			bean.setAzimuth(sheet.getCell(j, i).getContents());
	      		  }
	      		  if(j==4){//����
	      			  bean.setLongitude(sheet.getCell(j, i).getContents());
	      		  }
	      		  if(j==5){//γ��
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
		return new EXEException("","��λ����Ϣ�����쳣��"+e.getMessage(),"");
	}
	   return res;
	}
}
