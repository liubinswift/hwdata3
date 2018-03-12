package com.viewscenes.dao.innerdao;

import java.sql.*;
import com.viewscenes.dao.DAOOperator;
import com.viewscenes.pub.GDSet;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.innerdao.SQLGenerator;
import com.viewscenes.dao.XmlReader;
import com.viewscenes.pub.GDSetTool;
import com.viewscenes.pub.*;
import com.viewscenes.dao.cache.DAOCacheComponent;




/**
 * <p>ͨ�ö���� </p>

 * <p>���ݴ����ͨ�����ݶ���ʵ�ֶԵ������ɾ�Ĳ����</p>

 */

public class DAOComponent

    extends DAOOperator {

  //sql�������

  final static int _INSERT = 0;

  final static int _UPDATE = 1;

  final static int _DELETE = 2;

  //����

  long _key;

  //���ݿ�����

  short _dbname;

  //���ݿ�����--ORACLE

  final static short _ORACLE = 1;

  /**
   * �õ���ǰ�����ݿ�����

   * @return

   */

  private short getDbName() {

    return _dbname;

  }

  /**
   * ���õ�ǰ�����ݿ�����

   * @return

   */

  private void setDbName(short dbname) {

    _dbname = dbname;

  }

  /**
   * ���루�Զ����������ģ�֧�ֶ��в��룩���õ�һ������key

   * @param data

   * <pre>

   *     data����Ϊnull���

   *     ���뱣֤data.getRow(0)��data.getTableName()��Ϊ��

   * </pre>

   * @param key[OUT] ����

   * key�ĸ��������data������Ҫһ��

   * return

   * ���������

   */

  public int[] Insert(GDSet data, long[] key) throws DbException {

    return Insert(data, key, true);

  }

  /**
   * ����(������������֧�ֶ���)

   * @param data

   * <pre>

   *     data����Ϊnull���

   *     ���뱣֤data.getRow(0)��data.getTableName()��Ϊ��

   * </pre>

   * return

   * ���������

   */

  public int[] Insert(GDSet data) throws DbException {

    return Insert(data, null, false);

  }

  /**
   * ����

   * @param data

   * @param key ���ص�����

   * @param autoCreate �Ƿ��ò�����������

   * <pre>

   *     true:������������

   *     false:����Ҫ��������

   * </pre>

   * @return

   * @throws DbException

   */

  private int[] Insert(GDSet data, long[] key, boolean autoCreate) throws

      DbException {

    DbComponent db = new DbComponent();

    //�õ��������������

    String[] sql = getSqlArray(data, _INSERT, autoCreate, key);

    //ִ��

    int[] ret = db.exeBatch(sql);

    //����ɹ���ˢ�»���

    if (IsDirty(ret)) {

      setDirty(data.getTableName());

    }

    return ret;

  }

  /**
   * ���£�֧�ֶ�����¼���£��������ֵʧ�ܣ�����������ع���

   * @param data

   * <pre>

   * ���µ����ݣ�������Ϊ������������������ݱ������������

   *     data����Ϊnull���

   *     ���뱣֤data.getRow(0)��data.getTableName()��Ϊ��

   * </pre>

   * @return

   * ���µ�����

   * @throws DbException

   */

  public int[] Update(GDSet data) throws DbException {

    DbComponent db = new DbComponent();

    //�õ��������������

    String[] sql = getSqlArray(data, _UPDATE, false, null);

    //ִ��

    int[] ret = db.exeBatch(sql);

    //����ɹ���ˢ�»���

    if (IsDirty(ret)) {

      setDirty(data.getTableName());

    }

    return ret;

  }

  /**
   * ɾ��

   * �������ֵʧ�ܣ�����������ع�

   * @param data

   * <pre>

   * ɾ�������ݣ�����������ݲ�������,���Դ������������Ϊɾ��������

   *     data����Ϊnull���

   *     ���뱣֤data.getRow(0)��data.getTableName()��Ϊ��

   * </pre>

   * @return

   * ɾ��������

   */

  public int[] Delete(GDSet data) throws DbException {

    DbComponent db = new DbComponent();

    //�õ�ɾ�����

    String[] sql = getSqlArray(data, _DELETE, false, null);

    //�����ݿ�ִ��sql���

    int[] ret = db.exeBatch(sql);

    //����ɹ���ˢ�»���

    if (IsDirty(ret)) {

      setDirty(data.getTableName());

    }

    return ret;

  }

  /**
   * �����ѯ

   * @param field  �����ֶ�����

   *     ��ʽΪ��"a,b,c"

   * ֧��count(*)��*

   * @param condition ��ѯ����

   * condition�ĸ�ʽΪ

   * <pre>

   *     ����5�еĶ�ά���ݼ�

   *     �����趨TableName

   *     �б���̶�Ϊ��

   *         field����������

   *         type����������(VARCHAR,NUMBER,DATE��

   *         operator��������

   *         value��ֵ(�����DATE���ͣ����ʽ�̶�Ϊ:'YYYY-MM-DD HH24:MI:SS',����ʡ�Ժ����'HH24:MI:SS')

   *         between����ǰһ����ѯ����֮��Ĺ�ϵ����һ����¼Ĭ��Ϊand

   * </pre>

   * @return ��ѯ���

   * �����ѯ��¼Ϊ�գ���GDSet��getRowSizeΪ 0

   */

  public GDSet Query(String field, GDSet condition) throws DbException {

    return Query(field, condition, -1, 0);

  }

  /**
   * �����ѯ

   * @param field

   *֧��count(*)��*

   * @param condition

   * null:��ʾû�в�ѯ����

   * condition�ĸ�ʽΪ

   *     ����5�еĶ�ά���ݼ�

   *     �����趨TableName

   *     �б���̶�Ϊ��

   *         field����������

   *         type����������(VARCHAR,NUMBER,DATE��

   *         operator��������

   *         value��ֵ(�����DATE���ͣ����ʽ�̶�Ϊ:'YYYY-MM-DD HH24:MI:SS',����ʡ�Ժ����'HH24:MI:SS')

   *         between����ǰһ����ѯ����֮��Ĺ�ϵ����һ����¼Ĭ��Ϊand

   *  @param start ��ʼ�У���һ����0�п�ʼ��

   *    -1��ʾȫ����ѯ

   *  @param end ������

   * ���ǰ10�У�start =0,end =10

   * @return String

   */

  public GDSet Query(String field, GDSet condition, int start, int end) throws
      DbException {

    //��ϲ�ѯ��sql���

    SQLGenerator sg = new SQLGenerator();

    String sql = sg.generateQuerySQL(field, condition);

    DbComponent db = new DbComponent();

    //�Ż���ҳ��ѯ���

    String s = OptimizePageSQL(sql, start, end);

    //�����ݿ�ִ��sql���

    GDSet dataset = db.Query(s);
    if (dataset != null) {
      dataset.setTableName(condition.getTableName());

      //��rsת����GDSet����

    }
    return dataset;

  }

//{{ Added by ld 2003-12-10
  public GDSet Query(String field, String singlesql, String tablename,
                     int start, int end) throws DbException {
    if (tablename == null || tablename.equalsIgnoreCase("")) {
      throw new DbException("����tablenameΪ��");
    }
    if (singlesql == null || singlesql.equalsIgnoreCase("")) {
      throw new DbException("����singlesqlΪ��");
    }

    DbComponent db = new DbComponent();

    //�Ż���ҳ��ѯ���
    String s = OptimizePageSQL(singlesql, start, end);
    System.out.println(s);
    //�����ݿ�ִ��sql���
    GDSet dataset = db.Query(s);
    if (dataset != null) {
      dataset.setTableName(tablename);

      //��rsת����GDSet����
    }
    return dataset;
  }

//}}

  /**
   * ��ҳ�Ĳ�ѯ����Ż�

   * ������ݿ���ORACLE������rownum�����з�ҳ��ѯ

   * @param sql

   * @param start

   * @param end

   * @return

   */

  private String OptimizePageSQL(String sql, int start, int end) {

    String s = null;

    String name = XmlReader.getAttrValue("dbconfig", "para", "optimize");

    //�����oracle,��rownum����λ

    if (start != -1 && name.equals("ORACLE")) {

      setDbName(_ORACLE);

      s = "select * from (select rownum ora_nc,tab.* from (" + sql +

          ")tab) where ora_nc between " + start + " and " + end;

    }
    else {
      s = sql;
    }

    return s;

  }

  /**
   * �Ƿ�ʹ����ORACLE��ROWNUM�����Ż�

   * @return

   */

  private boolean UseOraRowNum() {

    if (getDbName() == _ORACLE) {

      return true;

    }

    return false;

  }

  /**
   * �Ƿ��ѯ���е�ֵ

   * @param start

   * @return

   * true:�����е�ֵ

   * false:��range��ѯ

   */

  private boolean IsQueryAll(int start) {

    if (start == -1) {

      return true;

    }

    return false;

  }

  /**
   * ��rsת��GDSet����,GDSet��range��start��end֮��

   * @return

   */

  private GDSet getGDSet(ResultSet rs, GDSet condition, int start, int end) throws
      DbException {

    GDSet set = null;

    try {

      set = new GDSet(condition.getTableName());

      //�����ѯȫ������ʹ����oracle��rownum,�򷵻�ȫ��

      if (IsQueryAll(start) || UseOraRowNum()) {

        try {
            GDSetTool.addResultSet(set, rs);
        } catch (GDSetException ex1) {
        }

      }

      else { //���򣬷���ָ����Χ�ļ���

        GDSetTool.addResultSet(set, rs, start, end);

      }

      //���ora_nc���ڣ���ȥ��ora_nc����

      removeNcColumn(set);

    }

    catch (GDSetException ex) {

      throw new DbException("DAOComponent getGDSet: GDSet ����", ex);

    }

    return set;

  }

  /**
   * ȥ��ora_nc����(ora_nc��oracle��rownum�ı���)

   * @param set

   * @throws DbException

   */

  private void removeNcColumn(GDSet set) throws DbException {

    try {

      //�������ֵ����rownum��ֵ����ȥ������

      if (set.getColumnIndex("ora_nc") != -1) {

        set.removeColumn("ora_nc");

      }

    }

    catch (GDSetException ex) {

      throw new DbException("DAOComponent removeOraRowNum: GDSet ����", ex);

    }

  }

  /**
   * insert,update��deleteʱ�ѻ���ı�ʾ����

   * @param tab

   */

  private void setDirty(String tab) {

    DAOCacheComponent dao = new DAOCacheComponent();

    dao.setDirty(tab);

  }

  /**
   * ����data�õ�һ��sql���

   * @param data

   * @param keyCreate �����Ƿ��Զ�����

   * true:�Զ�����

   * false:���Զ�����

   * @param type sql�������

   * @return

   */

  private String[] getSqlArray(GDSet data, int type, boolean keyCreate,
                               long[] key) throws

      DbException {

    String s[] = new String[data.getRowCount()];

    try {

      for (int i = 0; i < data.getRowCount(); i++) {

        //�õ�sql

        GDSet sub = data.getSubGDSet(i, i + 1);

        s[i] = getSql(sub, type, keyCreate);

        //����ǲ�����䲢���������Զ����ɣ����¼����ֵ

        if (type == _INSERT && key != null) {

          key[i] = _key;

        }

      }

    }

    catch (GDSetException ex) {

      throw new DbException("DAOComponent getSqlArray:GDSet����" + ex);

    }

    return s;

  }

  /**
   * ���ݵ�����¼��data�õ�һ��sql

   * @param data

   * @param type ������ͣ�_INSERT��_UPDATE��_DELETE)

   * @param keyCreate

   * ֻ��type = _INSERT��Ч����ʾ�Ƿ���Ҫ������������

   * @return

   * @throws DbException

   */

  private String getSql(GDSet data, int type, boolean keyCreate) throws
      DbException {

    String sql = null;

    SQLGenerator sg = new SQLGenerator();

    if (type == _INSERT) {

      sql = sg.generateInsertSQL(data, keyCreate);

      _key = Long.parseLong(sg.getKey());

    }

    if (type == _UPDATE) {

      sql = sg.generateUpdateSQL(data);

    }

    if (type == _DELETE) {

      sql = sg.generateDeleteSQL(data);

    }

    return sql;

  }

  /**
   * �������ɾ��

   * �������ֵʧ�ܣ�����������ع�

   * @param condition

   * <pre>

   * @param condition

   * condition�ĸ�ʽΪ

   *     ����5�еĶ�ά���ݼ�

   *     �����趨TableName

   *     �б���̶�Ϊ��

   *         field����������

   *         type����������(VARCHAR,NUMBER,DATE��

   *         operator��������

   *         value��ֵ(�����DATE���ͣ����ʽ�̶�Ϊ:'YYYY-MM-DD HH24:MI:SS',����ʡ�Ժ����'HH24:MI:SS')

   *         between����ǰһ����ѯ����֮��Ĺ�ϵ����һ����¼Ĭ��Ϊand

   * </pre>

   * @return

   * ɾ��������

   */

  public int[] DeleteX(GDSet condition) throws DbException {

    //���ɾ����sql���

    SQLGenerator sg = new SQLGenerator();

    String[] sql = new String[1];

    sql[0] = sg.generateDeleteXSQL(condition);

    //�����ݿ�ִ��sql���

    DbComponent db = new DbComponent();

    int[] ret = db.exeBatch(sql);

    //��������ɹ���ˢ�»���

    if (IsDirty(ret)) {

      setDirty(condition.getTableName());

    }

    return ret;

  }

  /**
   * �Ƿ���Ҫ�������־

   * @param ret

   * @return

   * true:��Ҫ�������־

   * false:����Ҫ�������־

   */

  private boolean IsDirty(int[] ret) {

    for (int i = 0; i < ret.length; i++) {

      if (ret[i] > 0) {

        return true;

      }

    }

    return false;

  }

  /**
   * �����������£�֧�ֶ�����¼���£��������ֵʧ�ܣ�����������ع���

   * @param data����������������

   * <pre>

   * ���µ�����

   *     data����Ϊnull���

   *     ���뱣֤data.getRow(0)��data.getTableName()��Ϊ��

   * </pre>

   * @param con

   * ��ʾ���µ�����

   * ֻ��Ҫ�趨column�����Լ���

   * @return

   * ���µ�����

   * @throws DbException

   */

  public int[] UpdateX(GDSet data, GDSet con) throws DbException {

    DbComponent db = new DbComponent();

    SQLGenerator sg = new SQLGenerator();

    String sql[] = new String[data.getRowCount()];

    int[] ret = null;

    try {

      for (int i = 0; i < data.getRowCount(); i++) {

        //�õ�sql

        GDSet sub = data.getSubGDSet(i, i + 1);
        //change by xyw for multi line update
        //sql[i] = sg.generateUpdateXSQL(data, con);
        sql[i] = sg.generateUpdateXSQL(sub, con);

      }

      //ִ��

      ret = db.exeBatch(sql);

      //����ɹ���ˢ�»���

      if (IsDirty(ret)) {

        setDirty(data.getTableName());

      }

    }

    catch (GDSetException ex) {

      throw new DbException("DAOComponent UpdateX:GDSet����" + ex);

    }

    return ret;

  }

  /**
   * ������������

   * @param content ���µ����ݣ�����������������

   * @param con ���µ�����

   * @return

   * @throws DbException

   */

  public int[] UpdatebyCon(GDSet content, GDSet con) throws DbException {

    DbComponent db = new DbComponent();

    SQLGenerator sg = new SQLGenerator();

    String sql[] = new String[1];

    int[] ret = null;

    //�õ�sql

    sql[0] = sg.generateUpdatebyConSQL(content, con);

    //ִ��

    ret = db.exeBatch(sql);

    //����ɹ���ˢ�»���

    if (IsDirty(ret)) {

      setDirty(content.getTableName());

    }

    return ret;

  }

}
