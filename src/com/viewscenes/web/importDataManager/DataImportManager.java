package com.viewscenes.web.importDataManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.jmask.web.controller.EXEException;

import com.viewscenes.bean.ImportFileBean;
import com.viewscenes.bean.NotificationBean;
import com.viewscenes.dao.XmlReader;
import com.viewscenes.dao.XmlReaderDeviceConfig;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.pub.GDSet;
import com.viewscenes.pub.GDSetException;
import com.viewscenes.sys.SystemConfig;
import com.viewscenes.util.LogTool;
import com.viewscenes.util.StringTool;
import com.viewscenes.web.Daoutil.StringDateUtil;

import flex.messaging.io.amf.ASObject;

/**
 * *************************************
 * 
 * ��Ŀ���ƣ�hwdata �����ƣ�DataImportManager �������� �����ֶ������������ݡ� �����ˣ����� ����ʱ�䣺2013-1-8
 * ����03:04:05 �޸��ˣ����� �޸�ʱ�䣺2013-1-8 ����03:04:05 �޸ı�ע��
 * 
 * @version
 * 
 *************************************** 
 */
public class DataImportManager {

	/**
	 * ************************************************
	 * 
	 * @Title: getLogList
	 * @Description: TODO(������Ч�ڲ�ѯ֪ͨ)
	 * @param @param NotificationBean
	 * @param @return �趨�ļ�
	 * @author ����
	 * @return Object ��������
	 * @throws
	 * 
	 ************************************************ 
	 */
	public Object getFileList(ImportFileBean obj) {
		ASObject objRes = new ASObject();
		String datetime = obj.getDate();// ��Ч��ʼʱ��
		ArrayList<ImportFileBean> list = new ArrayList();
		
		// 2013-12-13
		String path = SystemConfig.getVideo_location();
		//�����linuxϵͳ�������path�̶�
		path="/root/video/";
		String path2=path;
		Calendar cal = Calendar.getInstance();// ʹ��������
		try {
			cal.setTime(StringDateUtil.getDateTime(datetime + " 00:00:00"));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return new EXEException("", "��ѯ�ļ�ʧ�ܣ�������������", null);
		}
		int year = cal.get(Calendar.YEAR);// �õ���
		int month = cal.get(Calendar.MONTH) + 1;// �õ��£���Ϊ��0��ʼ�ģ�����Ҫ��1
		int day = cal.get(Calendar.DAY_OF_MONTH);// �õ���
		// fileName=
		// "d://video_location_down//"+"//"+year+"//"+month+"//"+day+"//"+fileName;
		path = path  + year + "/" + month + "/" + day;

		File pathfile = new File(path);
		ArrayList<File> txtlist = new ArrayList<File>();
		if(pathfile.exists())
		{
			File[] filelist = pathfile.listFiles();
		
			for (int i = 0; i < filelist.length; i++) {
				if (!filelist[i].isDirectory()
						&& filelist[i].getName().endsWith(".txt")) {
					txtlist.add(filelist[i]);
				}
		     }
		}
	     //��ѯ���ڵĴ�����ݡ�
		path2=path2+year+(month<10?"0"+month:month)+(day<10?"0"+day:day);

		File pathfile2 = new File(path2);
		if(pathfile2.exists())
		{
		File[] filelist2 = pathfile2.listFiles();
		
		for (int i = 0; i < filelist2.length; i++) {
			if (!filelist2[i].isDirectory()
					&& filelist2[i].getName().endsWith(".sql")) {
				txtlist.add(filelist2[i]);
			}
		}
		}
		GDSet set=null;
		for (int i = 0; i < txtlist.size(); i++) {

			File txtfile = (File) txtlist.get(i);
			ImportFileBean file = new ImportFileBean();
			String sqlString="select flag from res_importdata_tab where file_name='"+txtfile.getName()+"' and file_date=to_date('"+datetime+"','yyyy-mm-dd')";
			try {
				GDSet set2=DbComponent.Query(sqlString);
				if(set2.getRowCount()>0)
				{
					if(set2.getString(0, "flag").equals("1")){
						file.setFlag("�ɹ�����");
					}
					if(set2.getString(0, "flag").equals("0")){
						file.setFlag("����ʧ��");
					}
				}else 
				{
					file.setFlag("δ����");
				}
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				file.setFlag("δ����");
			} catch (GDSetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				file.setFlag("δ����");
			}
			file.setDate(datetime);
			file.setFile_path(txtfile.getAbsolutePath());
			file.setFile_name(txtfile.getName());
			file.setFile_size(txtfile.length() / 1024 + "");
		
			list.add(file);
		}
		
		
		objRes.put("resultList", list);
		objRes.put("resultTotal", list.size());
		return objRes;
	}

	/**
	 * ************************************************
	 * 
	 * @Title:
	 * 
	 * @Description: TODO(����ǰ̨���ļ����·���������)
	 * 
	 * @param @param msg
	 * @param @return
	 * @param @throws DbException
	 * @param @throws GDSetException �趨�ļ�
	 * 
	 * @author ����
	 * 
	 * @return String ��������
	 * 
	 * @throws
	 * 
	 ************************************************ 
	 */
	public Object importData(ArrayList fileList) {
         String returnstr="";
		for (int i = 0; i < fileList.size(); i++) {
			ImportFileBean bean = (ImportFileBean) fileList.get(i);
			String encoding = "UTF-8"; // �ַ�����(�ɽ�������������� )
		
			File file = new File(bean.getFile_path());
			String datestart=bean.getDate();
			String datesend=bean.getDate().replaceAll("00:00:00", "23:59:59");
			
			if (file.isFile() && file.exists()) {
				InputStreamReader read;
				BufferedReader bufferedReader;
				if(file.getName().equals("YPWJDFB.sql"))
				{
					encoding="GBK";
				}
					try {
						read = new InputStreamReader(
								new FileInputStream(file), encoding);
						 bufferedReader = new BufferedReader(read);
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						return "�ļ�"+bean.getFile_name()+"û�ҵ���\r\n";
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						return "�ļ�"+bean.getFile_name()+"û�ҵ���\r\n";
					}
					
					String lineTXT = null;
					//int count=0;
					//String[] sql=new String[100];
					//������ظ����룬��ɾ��ԭ���Ѿ���������ݡ�
					String filename=file.getName();
					 if(filename.indexOf("~")!=-1)
					 {
						String  times=filename.substring(filename.indexOf("(")+1,filename.indexOf(")"));
						 String starttime=times.split("~")[0];
						 String endtime=times.split("~")[1];
						 /*
						  *     <para name="�豸����" table="radio_equ_alarm_tab" sql="select * from radio_equ_alarm_tab t where t.alarm_datetime&gt;=sysdate-1 and t.alarm_datetime&lt;sysdate" />
						       <para name="ָ�걨��" table="radio_abnormal_tab"  sql="select * from radio_abnormal_tab t where t.abnormal_date&gt;=sysdate-1 and t.abnormal_date&lt;sysdate"/>
						       <para name="¼�����" table="radio_mark_zst_view_tab"  sql="select * from radio_mark_zst_view_tab t where t.mark_datetime&gt;=sysdate-1 and t.mark_datetime&lt;sysdate" />
						       <!--  para name="ָ������" table="radio_quality_result_tab"  sql="  select * from radio_quality_result_tab t where t.check_datetime&gt;=sysdate-1 and t.check_datetime&lt;sysdate" /-->
						       <para name="Ƶ������" table="radio_spectrum_result_tab"  sql="  select * from radio_spectrum_result_tab t where t.check_datetime&gt;=sysdate-1 and t.check_datetime&lt;sysdate" />
						       <!--  para name="Ƶƫ����" table="radio_offset_result_tab"  sql="  select * from radio_offset_result_tab t where t.check_datetime&gt;=sysdate-1 and t.check_datetime&lt;sysdate" /-->
						       <para name="¼������" table="radio_stream_result_tab"  sql="   select * from radio_stream_result_tab t where t.start_datetime&gt;=sysdate-1 and t.start_datetime&lt;sysdate" />
						      
						  */
						String sql=""; 
					   if(filename.indexOf("¼���ļ�����")!=-1||filename.indexOf("¼������")!=-1)	 
					   {
							sql="delete from radio_stream_result_tab t where  t.url not like '%������̨�ṩ%'  and start_datetime>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss') and start_datetime<to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') ";
								 
					   }else   if(filename.indexOf("¼���������")!=-1||filename.indexOf("¼�����")!=-1)	 
					   {
						   sql="delete from radio_mark_zst_view_tab t where t.mark_file_url not like '%������̨�ṩ%'  and t.mark_datetime>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss') and t.mark_datetime<to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') ";
							 
					   }
					   else   if(filename.indexOf("�豸����")!=-1)	 
					   {
							sql="delete from radio_equ_alarm_tab t where alarm_datetime>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss') and alarm_datetime<to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') ";
								 
					   }
					   else   if(filename.indexOf("ָ�걨��")!=-1)	 
					   {
						   sql="delete from radio_abnormal_tab t where abnormal_date>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss') and abnormal_date<to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') ";
								 
					   }
					   else   if(filename.indexOf("Ƶ������")!=-1)	 
					   {
						   sql="delete from radio_spectrum_stat_tab t where input_datetime>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss') and input_datetime<to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') ";
									 
					   }else if (filename.indexOf("YPWJDFB")!=-1) {
						   sql="delete from ypwjdfb t where shoutsj>=to_date('"+datestart+"','yyyy-mm-dd hh24:mi:ss') and shoutsj<to_date('"+datesend+"','yyyy-mm-dd hh24:mi:ss') ";
						
					}
					  if(sql.length()>1)
					  {
						  try {
							DbComponent.exeUpdate(sql);
						} catch (DbException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}  
					  }
						   
					 }
				
					if(filename.indexOf("¼���ļ�����")!=-1||filename.indexOf("¼���������")!=-1
							||filename.indexOf("¼������")!=-1||filename.indexOf("¼�����")!=-1	
					)
					{
						
						  String ip =XmlReader.getAttrValue("HttpIp", "ip", "value");
						   
							
						  try {
							  String url=SystemConfig.getVideoUrl();
								while ((lineTXT = bufferedReader.readLine()) != null) {
									//sql[count]=lineTXT;
									try {
										
										 lineTXT =  lineTXT.replaceAll("http:([\\S\\s]+?)video/", url);	
										DbComponent.exeUpdate(lineTXT);
									} catch (DbException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										continue ;
									}

								}
							}
						     catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							
							}
					
					   
					}else 
					{
						try {
							while ((lineTXT = bufferedReader.readLine()) != null) {
								//sql[count]=lineTXT;
								try {
									if(lineTXT.endsWith("$"))
									{
										lineTXT=lineTXT.substring(0,lineTXT.length()-1);
									}
									
									DbComponent.exeUpdate(lineTXT);
								} catch (DbException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									continue ;
								}

							}
						}
					     catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						
						}

					}
					try {
						read.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						
					}
			
			//Ȼ��������ݿ� ��
			String importsql="	insert into res_importdata_tab(id,FILE_NAME,FILE_PATH,FILE_DATE,FILE_SIZE,FLAG) " +
					"values(res_resourse_seq.nextval,'"+bean.getFile_name()+"','"+file.getAbsolutePath()+"','"+bean.getDate()
					+"','"+bean.getFile_size()
					+"',1)";
			try {
				DbComponent.exeUpdate(importsql);
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
			} else {
				LogTool.debug("importlog","�ļ�"+bean.getFile_name()+"û�ҵ���");
				returnstr+="�ļ�"+bean.getFile_name()+"û�ҵ���\r\n";
			}
			
		}
		if(returnstr.length()==0)
		{
			returnstr="���ݵ���ɹ���";
		}
		return  returnstr;
		
	}
public static void main(String args[])
{
   String filename="dfjldjlj(2012~2013)";
	 filename=filename.substring(filename.indexOf("(")+1,filename.indexOf(")"));
	 System.out.println(filename);
}
}
