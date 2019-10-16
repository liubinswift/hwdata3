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
 * 项目名称：hwdata 类名称：DataImportManager 类描述： 用于手动导入外网数据。 创建人：刘斌 创建时间：2013-1-8
 * 下午03:04:05 修改人：刘斌 修改时间：2013-1-8 下午03:04:05 修改备注：
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
	 * @Description: TODO(根据有效期查询通知)
	 * @param @param NotificationBean
	 * @param @return 设定文件
	 * @author 刘斌
	 * @return Object 返回类型
	 * @throws
	 * 
	 ************************************************ 
	 */
	public Object getFileList(ImportFileBean obj) {
		ASObject objRes = new ASObject();
		String datetime = obj.getDate();// 有效开始时间
		ArrayList<ImportFileBean> list = new ArrayList();
		
		// 2013-12-13
		String path = SystemConfig.getVideo_location();
		//如果是linux系统，这里的path固定
		path="/root/video/";
		String path2=path;
		Calendar cal = Calendar.getInstance();// 使用日历类
		try {
			cal.setTime(StringDateUtil.getDateTime(datetime + " 00:00:00"));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return new EXEException("", "查询文件失败，传入日期有误！", null);
		}
		int year = cal.get(Calendar.YEAR);// 得到年
		int month = cal.get(Calendar.MONTH) + 1;// 得到月，因为从0开始的，所以要加1
		int day = cal.get(Calendar.DAY_OF_MONTH);// 得到天
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
	     //查询二期的打分数据。
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
						file.setFlag("成功导入");
					}
					if(set2.getString(0, "flag").equals("0")){
						file.setFlag("导入失败");
					}
				}else 
				{
					file.setFlag("未导入");
				}
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				file.setFlag("未导入");
			} catch (GDSetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				file.setFlag("未导入");
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
	 * @Description: TODO(根据前台的文件名下发导入命令)
	 * 
	 * @param @param msg
	 * @param @return
	 * @param @throws DbException
	 * @param @throws GDSetException 设定文件
	 * 
	 * @author 刘斌
	 * 
	 * @return String 返回类型
	 * 
	 * @throws
	 * 
	 ************************************************ 
	 */
	public Object importData(ArrayList fileList) {
         String returnstr="";
		for (int i = 0; i < fileList.size(); i++) {
			ImportFileBean bean = (ImportFileBean) fileList.get(i);
			String encoding = "UTF-8"; // 字符编码(可解决中文乱码问题 )
		
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
						return "文件"+bean.getFile_name()+"没找到！\r\n";
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						return "文件"+bean.getFile_name()+"没找到！\r\n";
					}
					
					String lineTXT = null;
					//int count=0;
					//String[] sql=new String[100];
					//如果是重复导入，先删除原来已经导入的数据。
					String filename=file.getName();
					 if(filename.indexOf("~")!=-1)
					 {
						String  times=filename.substring(filename.indexOf("(")+1,filename.indexOf(")"));
						 String starttime=times.split("~")[0];
						 String endtime=times.split("~")[1];
						 /*
						  *     <para name="设备报警" table="radio_equ_alarm_tab" sql="select * from radio_equ_alarm_tab t where t.alarm_datetime&gt;=sysdate-1 and t.alarm_datetime&lt;sysdate" />
						       <para name="指标报警" table="radio_abnormal_tab"  sql="select * from radio_abnormal_tab t where t.abnormal_date&gt;=sysdate-1 and t.abnormal_date&lt;sysdate"/>
						       <para name="录音打分" table="radio_mark_zst_view_tab"  sql="select * from radio_mark_zst_view_tab t where t.mark_datetime&gt;=sysdate-1 and t.mark_datetime&lt;sysdate" />
						       <!--  para name="指标数据" table="radio_quality_result_tab"  sql="  select * from radio_quality_result_tab t where t.check_datetime&gt;=sysdate-1 and t.check_datetime&lt;sysdate" /-->
						       <para name="频谱数据" table="radio_spectrum_result_tab"  sql="  select * from radio_spectrum_result_tab t where t.check_datetime&gt;=sysdate-1 and t.check_datetime&lt;sysdate" />
						       <!--  para name="频偏数据" table="radio_offset_result_tab"  sql="  select * from radio_offset_result_tab t where t.check_datetime&gt;=sysdate-1 and t.check_datetime&lt;sysdate" /-->
						       <para name="录音数据" table="radio_stream_result_tab"  sql="   select * from radio_stream_result_tab t where t.start_datetime&gt;=sysdate-1 and t.start_datetime&lt;sysdate" />
						      
						  */
						String sql=""; 
					   if(filename.indexOf("录音文件数据")!=-1||filename.indexOf("录音数据")!=-1)	 
					   {
							sql="delete from radio_stream_result_tab t where  t.url not like '%五七三台提供%'  and start_datetime>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss') and start_datetime<to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') ";
								 
					   }else   if(filename.indexOf("录音打分数据")!=-1||filename.indexOf("录音打分")!=-1)	 
					   {
						   sql="delete from radio_mark_zst_view_tab t where t.mark_file_url not like '%五七三台提供%'  and t.mark_datetime>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss') and t.mark_datetime<to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') ";
							 
					   }
					   else   if(filename.indexOf("设备报警")!=-1)	 
					   {
							sql="delete from radio_equ_alarm_tab t where alarm_datetime>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss') and alarm_datetime<to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') ";
								 
					   }
					   else   if(filename.indexOf("指标报警")!=-1)	 
					   {
						   sql="delete from radio_abnormal_tab t where abnormal_date>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss') and abnormal_date<to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') ";
								 
					   }
					   else   if(filename.indexOf("频谱数据")!=-1)	 
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
				
					if(filename.indexOf("录音文件数据")!=-1||filename.indexOf("录音打分数据")!=-1
							||filename.indexOf("录音数据")!=-1||filename.indexOf("录音打分")!=-1	
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
			
			//然后更新数据库 。
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
				LogTool.debug("importlog","文件"+bean.getFile_name()+"没找到！");
				returnstr+="文件"+bean.getFile_name()+"没找到！\r\n";
			}
			
		}
		if(returnstr.length()==0)
		{
			returnstr="数据导入成功！";
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
