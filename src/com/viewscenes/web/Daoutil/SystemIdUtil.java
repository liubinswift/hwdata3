package com.viewscenes.web.Daoutil;

import java.sql.*;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.pub.GDSet;



/**
 * ��ǰϵͳid�ж��ࡣ
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class SystemIdUtil {
 public static String CENTER_ID = null;
 //������������idʱʹ�á�
 public static String CENTER_ID_FORTASK = null;
  public static String CENTER_NAME=null;
  public static String CENTER_CODE=null;
  public SystemIdUtil() {
  }

  /**
   * �жϱ�ǰϵͳ�Ƿ����ó���������ϵͳ��
   * @return boolean
   */
  public static boolean isCenter() {
    boolean isCenter = true;
    String locCenterId = getLocalCenterId();
    if (!"100".equalsIgnoreCase(locCenterId)) {
      isCenter = false;
    }
    return isCenter;
  }

  /**
   * center_id��100���������������ġ�
   * ����������ֱ��̨ϵͳ��
   * @return String
   */
  public synchronized static String getLocalCenterId() {
    if (CENTER_ID == null) {

      String task =
          "select param_value from  sys_configuration_tab where param_name='local_center_id' ";
      GDSet result = null;
      try {
        result = DbComponent.Query(task);
        if (result.getRowCount()>0) {
          CENTER_ID = result.getString(0,"param_value");
        }
      }
      catch (Exception ex2) {
        System.out.println("��ѯϵͳ����̨id��Ϣ����");
        ex2.printStackTrace();
      }

    }
    return CENTER_ID;
  }
  /**
   * ȡ�õ�ǰ���ݿ����ӵ�ֱ��̨����
   * @return
   */
  public synchronized static String getLocalCenterName() {
      if (CENTER_NAME == null) {

        String task =
            "select name,center_id,code from res_center_tab t where t.center_id= (select param_value from  sys_configuration_tab where param_name='local_center_id') ";
        GDSet result = null;
        try {
          result = DbComponent.Query(task);
          if (result.getRowCount()>0) {
            CENTER_ID = result.getString(0,"center_id");
            if ("100".equalsIgnoreCase(CENTER_ID)) {
            	CENTER_ID_FORTASK="1000";
              }else
              {
            	  CENTER_ID_FORTASK=CENTER_ID;
              }
            CENTER_NAME = result.getString(0,"name");
            CENTER_CODE = result.getString(0,"code");
          }
        }
        catch (Exception ex2) {
          System.out.println("��ѯϵͳ����̨id��Ϣ����");
          ex2.printStackTrace();
        }

      }
      return CENTER_NAME;
    }
  public static void main(String[] args) {
    SystemIdUtil systemIdUtil1 = new SystemIdUtil();
  }

}
