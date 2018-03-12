package com.viewscenes.dao.innerdao;

import com.viewscenes.dao.database.DbException;
import com.viewscenes.pub.GDSet;
import com.viewscenes.pub.GDSetTool;
import com.viewscenes.pub.GDSetException;



/**
 * In�������Ĵ�����

 * <p>Title: </p>

 * <p>Description: </p>

 * <p>Copyright: Copyright (c) 2003</p>

 * <p>Company: </p>

 * @author not attributable

 * @version 1.0

 */

public class HandlerIn
    implements Handler {

  /**
   * ����in�Ӿ�

   * @param con Condition����

   * @param sql SqlObject����

   * @throws DbException

   */

  public void execute(Condition con, SqlObject sql) throws DbException {

    //add by xyw for diffrent field name match such as head_code:code
    String field = con.getColumn();
    int start = field.indexOf(":");
    String infield = field;
    if (start > 0) {
      infield = field.substring(start + 1);
      field = field.substring(0, start);
    }

    String ret = " " + con.getBetween() + " " + field + " " + con.getOper();

    ret += handleSubString(con.getValue(), infield);

    sql.setSql(ret);

  }

  /**
   * ����In�����value���ִ�

   * @param value ֵ����ʾһ��GDSet���ַ���������DAOCondition��parseString�ĸ�ʽ

   * @param field �ֶ���

   * @return sql���

   * @throws DbException

   */

  protected String handleSubString(String value, String field) throws
      DbException {

    String con = null;

    try {

      //�ɱ�ʾ������GDSet���ַ���ת��GDSet�Ķ���

      GDSet dao_con = GDSetTool.parseStringToGDSet(value);

      //��SQL��������in���Ӿ�����

      SQLGenerator gener = new SQLGenerator();

      con = " (" + gener.generateQuerySQL(getField(field), dao_con) + ") ";

    }
    catch (GDSetException ex) {

      throw new DbException("InHandler handleSubString:GDSet����", ex);

    }

    return con;

  }

  /**
   * ����ֶ����԰�����ı�������ѱ���ȥ��

   * ch.ch_id->ch_id

   * @param field

   * @return

   */

  private String getField(String field)

  {

    int start = 0;

    if ( (start = field.indexOf(".")) != -1) {

      return field.substring(start + 1);
    }

    return field;

  }

}
