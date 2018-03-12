package com.viewscenes.sys;

import java.text.*;
import java.util.*;

import com.viewscenes.dao.*;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.pub.*;

public final class Operation_log {

  private static final String OPERATION_TAB = "sec_operation_log_tab";
  private static final String USER_TAB = "sec_user_tab";
  private static final String OPERATION_DETAIL_TAB = "sec_operation_detail_tab";
  private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

  public Operation_log() {
  }

  /**
   * ��¼������Ϣ
   * @param String ����ԱID ����Ϊnull
   * @param String ����ID ����Ϊnull
   * @param String �����ľ�������������ĸ��ɼ��㷢��ʲô���񣬻��������ͼʱ���Ǹ�����̨��ӵ�����ͼ�ȵȾ������������ܳ���200�֣���
   * @return boolean false:�����־ʧ�� ; true:�����־�ɹ�
   * @throws Exception
   */
  public static boolean writeOperLog(String user_id, String detail_id,
                                     String description) throws Exception {
    boolean result = false;

    //���幫������
    int rowCount = 0;

    //��ȡ��ǰʱ��
    String now_datetime = getNowDatetime();

    //�������û�����
    String user_name = null;
    if (user_id != null) {
      user_name = id2name("user_id", user_id, "user_name", USER_TAB);
    }

    //��øò����ľ�������
    String detail_name = null;
    if (detail_id != null) {
      detail_name = id2name("detail_id", detail_id, "operation_name",
                            OPERATION_DETAIL_TAB);
    }
     String sql="insert into "+OPERATION_TAB+"(log_id,user_id,user_name,operation_id,operation_name,log_datetime," +
		"description)values( sec_seq.nextval,"+user_id+",'"+user_name+"',"+detail_id+",'"+detail_name+"',to_date('"+now_datetime
		+"','yyyy-mm-dd hh24:mi:ss'),'"+description+"')";
    


    return DbComponent.exeUpdate(sql);
  }

  /**
   * ����id��ȡname
   * @param String id���ڱ��е��ֶ���
   * @param String id��ֵ
   * @param String name���ڱ��е��ֶ���
   * @param String ����
   * @param String ��ǰʱ��
   * @return String name �����쳣ʱ����nameΪ""
   * @throws Exception
   */
  private static String id2name(String id_row_name, String id_value,
                                String name_row_name, String table_name) throws
      Exception {
 
    GDSet data_user_name = null;
    String name = null;

   String sql="select "+name_row_name+" from  "+table_name +" where "+id_row_name+"="+id_value;
  
    data_user_name = DbComponent.Query(sql);
    int rowCount = data_user_name.getRowCount();
    if (rowCount == 0) {
      return null;
    }
    else {
      name = data_user_name.getString(0, name_row_name);
    }
    return name;
  }

  /**
   * ��ȡ��ǰʱ��
   * @return String eg. 2008-01-23 15:01:00
   */
  public static String getNowDatetime() {
    //��ȡ��ǰʱ��
    Calendar now;
    java.util.Date now_time;
    SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT,
        Locale.getDefault());
    String now_datetime = "";
    now = Calendar.getInstance();
    now_time = now.getTime();
    now_datetime = formatter.format(now_time);
    return now_datetime;
  }

}
