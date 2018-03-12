package com.viewscenes.sys;

import java.util.HashMap;


import com.viewscenes.pub.GDSet;
import com.viewscenes.pub.GDSetException;

import com.viewscenes.dao.database.*;



public class SystemConfig {

    public static String localSystemCode = "900000X20";

    private static HashMap<String,String> paramConfigMap = new HashMap<String,String>();
    
    public static void loadConfig(){
    	
    	String sql = "select * from sys_configuration_tab t";
    	GDSet set = null;
    	try {
			 set = DbComponent.Query(sql);
			 
			 for(int i=0;i<set.getRowCount();i++){
				 String name = set.getString(i, "param_name");
				 String value = set.getString(i, "param_value");
				 
				 paramConfigMap.put(name, value);
				 
			 }
			
			
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GDSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    //45��¼���ļ����·��
    public static String getRecord45SecPath(){
    	return paramConfigMap.get("record_45sec_path");
    }
  //ftpĿ¼
    public static String getVideo_location(){
    	return paramConfigMap.get("video_location");
    }
    //45��¼���ļ��Ų���URL
    public static String getRecord45SecUrl(){
    	return paramConfigMap.get("record_45sec_url");
    } 
    //¼���ļ����ŵ�ַ
    public static String getVideoUrl(){
    	return paramConfigMap.get("video_url");
    }
    
    //�ϱ���ַ
    public static String getUploadDataUrl(){
    	return paramConfigMap.get("upload_data_url");
    }
    
    //¼���ļ����·��
    public static String getVideoPath(){
    	return paramConfigMap.get("video_location");
    }
    
    //ftp��·��
    public static String getFtpUrl(){
    	return paramConfigMap.get("ftp");
    }
    //�豸ά��¼���ļ�·��
    public static String getEquRecordPath(){
    	return paramConfigMap.get("equ_record_path");
    }
}
