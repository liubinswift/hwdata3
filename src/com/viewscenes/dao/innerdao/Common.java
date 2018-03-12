package com.viewscenes.dao.innerdao;



import java.sql.*;

import java.util.*;



import com.viewscenes.dao.database.DbException;

import com.viewscenes.dao.*;

import com.viewscenes.pub.*;

import org.jdom.*;



/**

 * <p>Title: </p>

 * <p>Description: </p>

 * <p>Copyright: Copyright (c) 2003</p>

 * <p>Company: </p>

 * @author not attributable

 * @version 1.0

 */



public class Common {



  public static void print(String s) {

    System.out.println(s);

  }



  /**

   * �õ��������������

   * �������ļ�TabProperty�м�¼������������ӳ���ϵ

   * @return

   */

  public static String getKeyName(String tab) throws DbException {

    Element ele = XmlReader.getItem("keyconfig", "table", tab);

    if (ele == null) {

      throw new DbException("Common getKeyName:daoconfig.xml�ļ���keyconfig�ڵ����Ҳ���" + tab);

    }



    String indexs = ele.getAttributeValue("key");

    if (indexs == null) {

      throw new DbException("Common getKeyName:daoconfig.xml�ļ���keyconfig�ڵ��" + tab + "���Ҳ���key��ֵ");

    }



    return indexs;

  }



  /**

   * ��ʾ���ݼ�

   * @param cs

   */

  public static void displayRs(ResultSet cs) {

    try {

      ResultSetMetaData reMeta = cs.getMetaData(); //�õ��ֶθ�������

      int columnCount = reMeta.getColumnCount();

      String columnName = null;

      while (cs.next()) {

        for (int i = 1; i <= columnCount; i++) { //ע��!!! ��1��ʼ

          if (cs.getObject(i) != null) {

            System.out.print(cs.getObject(i).toString() + " ");

          }

        }

        System.out.println("\r");

      }

    }

    catch (SQLException ex) {

      ex.printStackTrace();

    }

  }



  public static void displayGDSet(GDSet gd) {

    try {

      for (int j = 0; j < gd.getRowCount(); j++) {

        String[] ss = gd.getRow(j);

        for (int k = 0; k < ss.length; k++) {

          System.out.print(ss[k] + " ");

        }

        System.out.println("");

      }

    }

    catch (GDSetException ex) {

    }

  }



  public static GDSet getGDHead(String tname) {

    GDSet gd = null;

    try {

      String f[] = {

          "field", "type", "operator", "value", "between"};

      gd = new GDSet(tname, f);

    }

    catch (GDSetException ex) {

      ex.printStackTrace();

    }

    return gd;

  }



  /**

   * ���ֶ����Ʒָ����ֶ�����

   * a,b,c->array[0]=a;array[1]=b;array[2]=c

   * @param field

   * @return

   */

  public static String[] Field2Array(String field) {

    StringTokenizer st = new StringTokenizer(field, ",");

    String[] s = new String[st.countTokens()];

    int i = 0;

    while (st.hasMoreTokens()) {

      s[i++] = st.nextToken().trim();

    }

    return s;

  }



  /**

   * ���ֶ����Ʒָ����ֶ�����

   * @param field

   * @return

   */

  public static String[] Field2Array(String field, String delim) {

    StringTokenizer st = new StringTokenizer(field, delim);

    String[] s = new String[st.countTokens()];

    int i = 0;

    while (st.hasMoreTokens()) {

      s[i++] = st.nextToken().trim();

    }

    return s;

  }



  /**

   * ���˿�ʼΪand��or���ַ���

   * @param sql

   * @return

   */

  public static String FilStartOper(String sql) {

    int i = 0,found = 0;

    String ret = sql.trim();

    String temp = ret.toLowerCase();

    String[] operAry = {

        "and ", "or "};



    for (i = 0; i < operAry.length; i++) {

      if (temp.startsWith(operAry[i])) {

        found = 1;

        break;

      }

    }



    if(found == 1)

      ret = ret.substring(operAry[i].length());



    return ret;

  }



  /**

   * �ַ����Ƿ����ָ��������

   * @param oper

   * @param sql

   * @return

   */

  public static boolean OperContains(String oper, String sql) {

    String temp = sql.trim().toLowerCase();



    if (temp.startsWith(oper)) {

      return true;

    }



    return false;

  }



  /**

   * ���sql

   * ���sql��""��group by,order by�Ӿ䣬��������ϲ�����oper����������ϲ�����oper

   * @param sql

   * @param oper

   * @return

   */

  public static String combineSQL(String sql, String oper) {

    boolean EmptyCondition = sql.equals("");

    boolean containStartOrder = Common.OperContains("order", sql);

    boolean containStartGroup = Common.OperContains("group", sql);



    String cond = null;

    if (EmptyCondition || containStartOrder || containStartGroup) {

      cond = " " + sql;

    }

    else {

      cond = " " + oper + " " + sql;



    }

    return cond;

  }



}
