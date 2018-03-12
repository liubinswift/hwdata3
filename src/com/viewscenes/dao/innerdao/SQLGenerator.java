package com.viewscenes.dao.innerdao;



import com.viewscenes.pub.*;

import com.viewscenes.dao.database.*;

import org.jdom.*;

import com.viewscenes.dao.*;



/**

 * sql���������

 *

 * ���ݴ����ͨ�����ݶ�������sql���

 */

public class SQLGenerator {



  final static String _alias = "tab.";

  private String _key = "-1";



  /**

   * �õ������������

   * @param data �����һ������(����������

   *     data����Ϊnull���

   *     ���뱣֤data.getRow(0)��data.getTableName()��Ϊ��

   * @param keyCreate �Ƿ��ò�����������

   *     true:������������

   *     false:����Ҫ��������

   * @return String

   */

  public String generateInsertSQL(GDSet data, boolean keyCreate) throws DbException {

    String field = "";

    String value = "";



    String sql = null;

    String key = "";

    try {

      //�õ�����ֵ

      String tab = data.getTableName();

      if (keyCreate == true) {

        key = Common.getKeyName(tab);



      }



      for (int i = 0; i < data.getColumnCount(); i++) {

        //���data��������,��������ֻ��""

        if (key.equals(data.getColumnName(i)) && data.getString(0,i).equals("")) {

          continue;

        }



        field += data.getColumnName(i) + ",";

        value += "'" + data.getString(0, i) + "',";

      }



      //���������Ҫ����

      if (keyCreate == true) {

        if(data.getColumnIndex(key) == -1 || (data.getColumnIndex(key)!= -1 && data.getString(0,key).equals("")))

        {

          DbComponent db = new DbComponent(null);

          String keyValue = _key = db.getKey(tab);



          field += key + ",";

          value += keyValue + ",";

        }

      }



      sql = "INSERT INTO " + data.getTableName() + "(" +

          field.substring(0, field.length() - 1) + ") VALUES(" +

          value.substring(0, value.length() - 1) + ")";

    }

    catch (GDSetException ex) {

      throw new DbException("SQLGenerator generateInsertSQL:", ex);

    }



    return sql;

  }



  /**

   * �õ������������

   * @param data ���µ����ݣ�������Ϊ������������������ݱ������������

   *     data����Ϊnull���

   *     ���뱣֤data.getRow(0)��data.getTableName()��Ϊ��

   * @return String

   */

  public String generateUpdateSQL(GDSet data) throws DbException {

    String field = "";

    String value = "";

    String prefix = "";

    String suffix = "";

    String sql = null;

    boolean hasKey = false;

    String key = "";

    try {

      for (int i = 0; i < data.getColumnCount(); i++) {

        field = data.getColumnName(i);

        value = data.getString(0, i);



        //�����ֵ���������������ȡ��suffix����Ϊ��������

        key = Common.getKeyName(data.getTableName());

        if (key.equals(field)) {

          suffix = field + "='" + value + "'";

          hasKey = true; //��������

        }

        else {

          prefix += field + "='" + value + "',";

        }

      }



      if (!hasKey) {

        throw new DbException("data���Ҳ�������,��" + data.getTableName() + "������Ϊ" + key);

      }



      sql = "UPDATE " + data.getTableName() + " SET " +

          prefix.substring(0, prefix.length() - 1) + "  WHERE " + suffix;

    }

    catch (GDSetException ex) {

      throw new DbException("ͨ�����ݼ��ϴ���", ex);

    }



    return sql;



  }



  /**

   * field�Ƿ���GDSet���б�����

   * @param gd

   * @param field

   * @return

   */

  private boolean GDColContain(GDSet gd, String field) {

    String[] col = gd.getAllColumnName();

    for (int i = 0; i < col.length; i++) {

      if (col[i].equals(field)) {

        return true;

      }

    }

    return false;

  }



  /**

   * �õ�����ɾ�����

   * @param data ɾ�������ݣ�����������ݲ�������,���Դ������������Ϊɾ��������

   *     data����Ϊnull���

   *     ���뱣֤data.getRow(0)��data.getTableName()��Ϊ��

   * @return String

   */

  public String generateDeleteSQL(GDSet data) throws DbException {

    String field = "";

    String value = "";

    String s = "";

    String sql = null;

    String key = null;

    try {

      for (int i = 0; i < data.getColumnCount(); i++) {

        field = data.getColumnName(i);



        //����������ݲ�������,���Դ������������Ϊɾ������

        value = data.getString(0, i);

        s += " and " + field + "='" + value + "' ";



        //�����ֵ���������������ȡ��suffix����Ϊɾ������

        key = Common.getKeyName(data.getTableName());

        if (key.equalsIgnoreCase(field)) {

          value = data.getString(0, i);

          s = " and " + field + "='" + value + "'";

          break;

        }

      }



      sql = "DELETE " + data.getTableName() + "  WHERE 1=1 " + s;

    }

    catch (GDSetException ex) {

      throw new DbException("ͨ�����ݼ��ϴ���", ex);

    }



    return sql;



  }



  /**

   * �õ�������ѯ���

   * @param field (*:��ʾ����ȫ���ֶ�)

   * @param condition

   * û�в�ѯ������ֻ�����

   * condition�ĸ�ʽΪ

   *     ����5�еĶ�ά���ݼ�

   *     �б���̶�Ϊ��

   *         field����������

   *         type����������(VARCHAR,NUMBER,DATE��

   *         operator��������

   *         value��ֵ(�����DATE���ͣ����ʽ�̶�Ϊ:'YYYY-MM-DD HH24:MI:SS',����ʡ�Ժ����'HH24:MI:SS')

   *         between����ǰһ����ѯ����֮��Ĺ�ϵ����һ����¼Ĭ��Ϊand

   * @return String

   */

  public String generateQuerySQL(String field, GDSet condition) throws DbException {

    ConBaseFilter filter = FilterFactory.create(field, condition);

    String cond = getCondition(filter);

    String con = "SELECT " + filter.getField() + " FROM " + filter.getTabName() + cond;



    return con;

  }



  private String getCondition(ConBaseFilter filter) {

    String con = filter.getCondition();

    return Common.combineSQL(con, "WHERE");

  }



  public String getKey() {

    return _key;

  }



  /**

   * �õ�ɾ�����

   * @param data

   * <pre>

   * data�ĸ�ʽΪ

   *     ����5�еĶ�ά���ݼ�

   *     �����趨TableName

   *     �б���̶�Ϊ��

   *         field����������

   *         type����������(VARCHAR,NUMBER,DATE��

   *         operator��������

   *         value��ֵ(�����DATE���ͣ����ʽ�̶�Ϊ:'YYYY-MM-DD HH24:MI:SS',����ʡ�Ժ����'HH24:MI:SS')

   *         between����ǰһ����ѯ����֮��Ĺ�ϵ����һ����¼Ĭ��Ϊand

   * @return String

   */

  public String generateDeleteXSQL(GDSet data) throws DbException {

    ConBaseFilter filter = FilterFactory.create("", data);

    String cond = getCondition(filter);

    String con = "DELETE  FROM " + filter.getTabName() + cond;



    return con;

  }



  /**

   * �õ�����������䣨����������������������,���������͸������ݵ����ݾ���data�У�

   * @param data ���µ�����

   * @param con ���µ�����(ֻ�涨�ֶ�)

   * @return ���µ����

   * @throws DbException

   */

  public String generateUpdateXSQL(GDSet data, GDSet con) throws DbException {

    String field = "";

    String value = "";

    String prefix = "";

    String suffix = "";

    String sql = null;

    String key = "";

    try {

      for (int i = 0; i < data.getColumnCount(); i++) {

        field = data.getColumnName(i);

        value = data.getString(0, i);



        //������ֶ��Ǹ�������

        if (GDColContain(con, field)) {

          suffix += field + "='" + value + "' and ";

        }

        else {

          prefix += field + "='" + value + "',";

        }

      }



      sql = "UPDATE " + data.getTableName() + " SET " +

          prefix.substring(0, prefix.length() - 1) + "  WHERE " +

          suffix.substring(0, suffix.length() - 4);

    }

    catch (GDSetException ex) {

      throw new DbException("ͨ�����ݼ��ϴ���", ex);

    }



    return sql;



  }



  /**

   * ������������

   * @param content ��������

   * @param condition ��������

   * @return

   * @throws DbException

   */

  public String generateUpdatebyConSQL(GDSet content, GDSet condition) throws DbException {

    ConBaseFilter filter = FilterFactory.create("", condition);

    StringBuffer prefix = new StringBuffer();

    String sql = "";

    try {

      for (int i = 0; i < content.getColumnCount(); i++) {

        prefix.append(content.getColumnName(i));

        prefix.append("='").append(content.getString(0, i)).append("',");

      }



      sql = "UPDATE " + content.getTableName() + " SET " +

          prefix.toString().substring(0, prefix.length() - 1) + getCondition(filter);

    }

    catch (GDSetException ex) {

      throw new DbException("ͨ�����ݼ��ϴ���", ex);

    }



    return sql;

  }



}

