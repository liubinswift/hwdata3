package com.viewscenes.dao;

import com.viewscenes.dao.database.DbComponent;



/**
 * �ͻ��˵������ݿ⹤��

 */

public class DaoFactory {

  public static final short DAO_OBJECT = 0;

  public static final short DB_OBJECT = 1;

  /**
   * �õ����ݿ�ʵ��

   * DAO_OBJECT:�������ݷ��ʽӿڣ�����DAOComponent

   * DB_OBJECT:�������ݿ�ײ�ӿڣ�����DbComponent

   * @return

   */

  public synchronized static Object create(short name) {
    System.gc();
    if (name == DAO_OBJECT) {
      return new DaoProxy();
    }
    else if (name == DB_OBJECT) {
      return new DbComponent();
    }
    return null;
  }
}
