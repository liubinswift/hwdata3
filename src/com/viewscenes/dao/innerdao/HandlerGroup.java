package com.viewscenes.dao.innerdao;

import com.viewscenes.dao.database.DbException;



/**
 * group by�������Ĵ�����

 * <p>Title: </p>

 * <p>Description: </p>

 * <p>Copyright: Copyright (c) 2003</p>

 * <p>Company: </p>

 * @author not attributable

 * @version 1.0

 */

public class HandlerGroup

    implements Handler {

  /**
   * ����sql�Ӿ�

   * @param con Condition����

   * @param sql SqlObject����

   * @throws DbException

   */

  public void execute(Condition con, SqlObject sql) throws DbException {

    String oper = con.getOper();

    String col = con.getColumn();

    String condition = " " + oper + " " + col;

    sql.setGroup_sql(condition);

  }

}
