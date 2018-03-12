package com.viewscenes.sys;

import java.util.HashMap;
import java.util.Map;

/**
 * ���ϵͳ����Ĺ����࣬map���Ա�����Ϊ����ÿ�ű��mapΪֵ��
 * @author ������
 * @since 2012/07/13
 */
public class SystemTableCache {
	
	  public static final String RES_HEAD_END_TAB = "res_headend_tab";//ǰ�˱�
	  public static final String RES_CITY_TAB = "res_city_tab";		  //������б�
	  public static final String DIC_STATE_TAB = "dic_state_tab";	  //���ޱ�	
	  public static final String RES_TRANSMIT_STATION_TAB = "res_transmit_station_tab";	//����̨
	  public static final String ZDIC_LANGUAGE_TAB = "zdic_language_tab";//����
	  public static final String DIC_SERVICESAREA_TAB = "dic_servicesarea_tab";//������
	  
	  /**
	   * ��������MAP
	   * ���ݽṹ:cachedTableMapMap{key,Map{key,Object}}
	   * 
	   */
	  public static HashMap<String,Map<String,Object>> cachedTableMapMap = new HashMap<String,Map<String,Object>>();
	  
	  public SystemTableCache() {

	  }
	  public static void setTableMap(String tableName, Map<String,Object> map){
		  synchronized (cachedTableMapMap){
			  cachedTableMapMap.put(tableName,map);
		  }
	  }

	  public static Map<String,Object> getDataMap(String tableName){
	    return cachedTableMapMap.get(tableName);
	  }
}
