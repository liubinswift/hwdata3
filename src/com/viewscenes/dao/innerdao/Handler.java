package com.viewscenes.dao.innerdao;

import com.viewscenes.dao.database.DbException;



/**
 * �������������ӿ�

 * <p>Title: </p>

 * <p>Description: </p>

 * <p>Copyright: Copyright (c) 2003</p>

 * <p>Company: </p>

 * @author not attributable

 * @version 1.0

 */

public interface Handler {

  /**
   * ���ݲ�ͬ�Ĳ��������sql���ʽ

   * @param con ��������

   * @param sql [out]sql����

   * @throws DbException

   */

  public void execute(Condition con, SqlObject sql) throws DbException;

}
