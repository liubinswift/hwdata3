package com.viewscenes.dao;

import com.viewscenes.dao.cache.DAOCacheComponent;
import com.viewscenes.pub.GDSet;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.dao.innerdao.DAOComponent;
import com.viewscenes.dao.logic.LogicFactory;
import com.viewscenes.util.LogTool;
import com.viewscenes.pub.GDSetException;



/**
 * <p>Title: </p>
 * <p>���������������ֲ������в���</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DaoProxy

    extends DAOOperator {

  private DAOCacheComponent cache = new DAOCacheComponent(); //����

  /**
   * ���루�Զ����������ģ�֧�ֶ��в��룩���õ�һ������key
   * �����߼���������
   */

  public int[] Insert(GDSet data, long[] key) throws DbException {

    checkData(data, "insert");

    //assert (key == null || key.length == data.getRowCount()):"DaoProxy Insert:key�ĸ�����data�ĸ�����һ��";

    DAOComponent dao = LogicFactory.create(data.getTableName());

    return dao.Insert(data, key);

  }

  /**
   * ����(������������֧�ֶ���)
   * �������ݿⲿ������
   */

  public int[] Insert(GDSet data) throws DbException {

    checkData(data, "insert");

    DAOComponent dao = LogicFactory.create(data.getTableName());

    return dao.Insert(data);

  }

  /**
   * ����
   * �������ݿⲿ������
   */

  public int[] Update(GDSet data) throws DbException {

    checkData(data, "update");

    DAOComponent dao = LogicFactory.create(data.getTableName());

    return dao.Update(data);

  }

  /**
   * ������������
   * �������ݿⲿ������
   */

  public int[] UpdateX(GDSet data, GDSet con) throws DbException {

    checkData(data, "UpdateX");

    //assert (con != null):"UpdateX:conΪ�գ�";

    DAOComponent dao = LogicFactory.create(data.getTableName());

    return dao.UpdateX(data, con);

  }

  /**
   * ������������
   * @param content(�������������
   * @param con�����Բ�����������
   * @return
   * @throws DbException
   */

  public int[] UpdatebyCon(GDSet content, GDSet con) throws DbException {

    checkData(content, "UpdatebyCon");

    //assert (con.getRowCount() > 0):"DaoProxy UpdatebyCon:dataû�м�¼��";

    DAOComponent dao = LogicFactory.create(content.getTableName());

    return dao.UpdatebyCon(content, con);

  }

  /**
   * ɾ��
   * �������ݿⲿ��ɾ��
   * @param data
   * @return
   * @throws DbException
   */

  public int[] Delete(GDSet data) throws DbException {

    checkData(data, "delete");

    DAOComponent dao = LogicFactory.create(data.getTableName());

    return dao.Delete(data);

  }

  /**
   * ��ѯ
   * �����û����ѯ����ֱ�Ӳ�ѯ���ݿ�
   * @param field �����ֶ���
   * @param condition ��ѯ����
   * @return
   * @throws DbException
   */

  public GDSet Query(String field, GDSet condition) throws DbException {

    checkGDSetData(condition, "Query");

    if (UseCacheStragy(field, condition)) {

      LogTool.debug("Use Cache");

      return cache.Query(field, condition);

    }

    DAOComponent dao = LogicFactory.create(condition.getTableName());

    return dao.Query(field, condition);

  }

  /**
   * ��ҳ��ѯ
   * �����û����ѯ����ֱ�Ӳ�ѯ���ݿ�
   * @param field �����ֶ���
   * @param condition ��ѯ����
   * @param start ��ʼ������0Ϊ��һ�У�
   * @param end ��������
   * @return
   * @throws DbException
   */

  public GDSet Query(String field, GDSet condition, int start, int end) throws

      DbException {

    checkGDSetData(condition, "Query");

    //assert (start >= -1 && end >= 0 && start < end):"DaoProxy Query: start��end����Ҫ����0,start����<end";

    //���ֻ��һ����ѯ�������ұ��ڻ����У���ӻ�����ȡ

    if (UseCacheStragy(field, condition)) {

      LogTool.debug("Use Cache");

      return cache.Query(field, condition, start, end);

    }

    DAOComponent dao = LogicFactory.create(condition.getTableName());

    return dao.Query(field, condition, start, end);

  }

//{{ Added by ld 2003-12-10
  public GDSet Query(String field, String singlesql, String tablename,
                     int start, int end) throws DbException {

    DAOComponent dao = LogicFactory.create(tablename);

    return dao.Query(field, singlesql, tablename, start, end);
  }

//}}

  /**
   * �Ƿ�ʹ�û���
   * ʹ�û�����ԣ����ڻ���,�����ļ�ָ��ʹ�û��棬ֻ��һ����ѯ���������Ҳ�������=
   * @return true:ʹ�û���
   * false:��ʹ�û���
   */

  private boolean UseCacheStragy(String field, GDSet condition) throws

      DbException {

    String tab = condition.getTableName();

    String para = XmlReader.getAttrValue("dbconfig", "para", "usecache");

    final boolean xmlUseCache = para.equals("1");

    final boolean conditionRange = condition.getRowCount() <= 1;

    final boolean funDefine = !field.equals("count(*)");

    final boolean hitCache = cache.hitCache(tab);

    final boolean EqualsContains = IsEqualsContains(condition);

    //���ֻ��һ����ѯ�������ұ��ڻ����У���ӻ�����ȡ

    if (xmlUseCache && conditionRange && funDefine && hitCache &&
        EqualsContains) {

      return true;

    }

    return false;

  }

  /**
   * ɾ����Ŀǰ֧�ֵ�����¼���£�
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
   */

  public int[] DeleteX(GDSet condition) throws DbException {

    checkGDSetData(condition, "DeleteX");

    DAOComponent dao = LogicFactory.create(condition.getTableName());

    return dao.DeleteX(condition);

  }

  /**
   * �������Ƿ�=������
   * @param condition
   * @return
   * true:��������=
   * false:����������=
   */

  private boolean IsEqualsContains(GDSet condition) throws DbException {

    try {

      for (int i = 0; i < condition.getRowCount(); i++) {

        String oper = condition.getString(i, "operator");

        if (oper.equals(Operator._EQUAL)) {

          return true;

        }

      }

      return false;

    }

    catch (GDSetException ex) {

      throw new DbException("DaoProxy IsEqualsContains: GDSet���� ", ex);

    }

  }

  /**
   * ���data����ȷ��
   * data����Ϊnull,������Ϊnull,����������Ϊ0��
   * @param method ������
   * @param data
   * @return
   */

  private void checkData(GDSet data, String method) {

    String cls = "DAOProxy ";

    checkGDSetData(data, method);

    //assert (data.getRowCount() > 0):method + ":dataû�м�¼��";

  }

  private void checkGDSetData(GDSet data, String method) {

    String cls = "DAOProxy ";

    //assert (data != null):method + ":dataû�м�¼��";

    //assert (data.getTableName() != null && !data.getTableName().equals("")):method + ":��������Ϊ�գ�";

  }

}
