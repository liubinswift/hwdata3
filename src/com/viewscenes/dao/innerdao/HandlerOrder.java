package com.viewscenes.dao.innerdao;

import com.viewscenes.dao.database.DbException;



/**
 * order by������������

 * <p>Title: </p>

 * <p>Description: </p>

 * <p>Copyright: Copyright (c) 2003</p>

 * <p>Company: </p>

 * @author not attributable

 * @version 1.0

 */

public class HandlerOrder

    implements Handler {

  /**
   * ����oder by�Ӿ�

   * @param con Condition����

   * @param sql SqlObject����

   * @throws DbException

   */

  public void execute(Condition con, SqlObject sql) throws DbException {

    String order = con.getColumn() + " " + con.getValue();

    //����sql�����order�Ӿ�

    sql.setOrder_sql(order);

  }

}
