package com.viewscenes.dao.innerdao;

import com.viewscenes.dao.database.DbException;
import com.viewscenes.pub.GDSet;
import com.viewscenes.pub.GDSetTool;
import com.viewscenes.pub.GDSetException;


/**
 * ���Ų�����������

 * <p>Title: </p>

 * <p>Description: </p>

 * <p>Copyright: Copyright (c) 2003</p>

 * <p>Company: </p>

 * @author not attributable

 * @version 1.0

 */

public class HandlerBracket
    implements Handler {

  /**
   * ����()�Ӿ�

   * @param con ��������

   * @param sql Sql����

   * @throws DbException

   */

  public void execute(Condition con, SqlObject sql) throws DbException {

    String ret = " " + con.getBetween() + handleSubString(con.getValue());

    sql.setSql(ret);

  }

  /**
   * ����()���Ӿ�

   * @param value ֵ����ʾһ��GDSet���ַ���������DAOCondition��parseString�ĸ�ʽ

   * @return sql���

   * @throws DbException

   */

  protected String handleSubString(String value) throws DbException {

    String con = null;

    try {

      //��GDSet���ַ���ת��GDSet�Ķ���

      GDSet dao_con = GDSetTool.parseStringToGDSet(value);

      //�ѱ�ʾ������GDSetͨ������������ת���������ʽ

      ConBaseFilter filter = FilterFactory.create("", dao_con);

      con = " ( " + filter.getCondition() + ")";

      ;

    }
    catch (GDSetException ex) {

      throw new DbException("bracketHandler handleSubString:GDSet����", ex);

    }

        return con;

  }

}
