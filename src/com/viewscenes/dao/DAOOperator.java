package com.viewscenes.dao;

import com.viewscenes.pub.GDSet;
import com.viewscenes.dao.database.DbException;



/**
 * �����ṩ���ݲ�������

 */

public class DAOOperator {

  /**
   * ���루�Զ����������ģ�֧�ֶ��в��룩���õ�һ������key

   * @param data

   * <pre>

   *     data����Ϊnull���

   *     ���뱣֤data.getRow(0)��data.getTableName()��Ϊ��

   * </pre>

   * @param key[OUT] ����

   * key�ĸ��������data������Ҫһ��

   */

  public int[] Insert(GDSet data, long[] key) throws DbException {

    return null;

  }

  /**
   * ����(������������֧�ֶ���)

   * @param data

   * <pre>

   *     data����Ϊnull���

   *     ���뱣֤data.getRow(0)��data.getTableName()��Ϊ��

   * </pre>

   */

  public int[] Insert(GDSet data) throws DbException {

    return null;

  }

  /**
   * ���£�Ŀǰ֧�ֵ�����¼���£��������ֵʧ�ܣ�����������ع���

   * @param data

   * <pre>

   * ���µ����ݣ�������Ϊ������������������ݱ������������

   *     data����Ϊnull���

   *     ���뱣֤data.getRow(0)��data.getTableName()��Ϊ��

   * </pre>

   * @return

   * @throws DbException

   */

  public int[] Update(GDSet data) throws DbException {

    return null;

  }

  /**
   * ɾ����Ŀǰ֧�ֵ�����¼���£�

   * �������ֵʧ�ܣ�����������ع�

   * @param data

   * <pre>

   * ɾ�������ݣ�����������ݲ�������,���Դ������������Ϊɾ��������

   *     data����Ϊnull���

   *     ���뱣֤data.getRow(0)��data.getTableName()��Ϊ��

   * </pre>

   * @return

   */

  public int[] Delete(GDSet data) throws DbException {

    return null;

  }

  /**
   * ��ѯ(��������Ϊ�����Ҳ����Ϊ�߼���

   * @param field

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

   * @return String

   */

  public GDSet Query(String field, GDSet condition) throws DbException {

    return null;

  }

  /**
   * ��ҳ��ѯ(��������Ϊ�����Ҳ����Ϊ�߼���

   * @param field

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

   *  @param start ��ʼ�У���һ����0�п�ʼ��

   *    -1��ʾȫ����ѯ

   *  @param end ������

   * ���ǰ10�У�start =0,end =10

   * @return String

   */

  public GDSet Query(String field, GDSet condition, int start, int end) throws
      DbException {

    return null;

  }

//{{ Added by ld 2002-12-10
  public GDSet Query(String field, String singlesql, String tablename,
                     int start, int end) throws DbException {
    return null;
  }

//}}

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

    return null;

  }

  /**
   * �����������£�֧�ֶ�����¼���£��������ֵʧ�ܣ�����������ع���

   * @param data

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

    return null;

  }

  /**
   *

   * @param content

   * @param con

   * @return

   * @throws DbException

   */

  public int[] UpdatebyCon(GDSet content, GDSet con) throws DbException {

    return null;

  }

}
