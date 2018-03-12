package com.viewscenes.dao.logic;

import java.util.*;

import org.jdom.*;
import com.viewscenes.dao.*;
import com.viewscenes.dao.database.*;
import com.viewscenes.dao.innerdao.*;
import com.viewscenes.pub.*;

/**
 * �߼���Ĳ���

 * �������Ĳ���

 * <p>Title: </p>

 * <p>Description: </p>

 * <p>Copyright: Copyright (c) 2003</p>

 * <p>Company: </p>

 * @author not attributable

 * @version 1.0

 */

public class DAOLogicComponent {

  DAOComponent dao = new DAOComponent();

  /**
   * �жϱ��Ƿ�����caption��Ĳ���

   * @param caption �������͵�����

   * @param tabName ����

   * @return

   * null:������caption��Ĳ���

   * String:��tab��Ӧ��type

   */

  private String operContains(String caption, String tabName) {

    String lowerTab = tabName.toLowerCase();

    Element ele = XmlReader.getConfigItem("updateconfig");

    Element task_ele = ele.getChild(caption);

    List total_tab = task_ele.getChildren();

    for (int i = 0; i < total_tab.size(); i++) {

      Element tab = (Element) total_tab.get(i);

      String tname = tab.getAttributeValue("table");

      if (tname == null) {

	return null; //�����ڸ������

      }

      if (tname.equals(lowerTab)) {

	return tab.getAttributeValue("type");

      }

    }

    return null;

  }

  /**
   * ���루�Զ����������ģ�֧�ֶ��в��룩���õ�һ������key

   */

  public void Insert(GDSet data, long[] key) throws DbException {

    beforeInsert(data, key);

    commonInsert(data, key);

    afterInsert(data);

  }

  /**
   * ����

   * @param data

   * @param key

   * @throws DbException

   */

  private void commonInsert(GDSet data, long[] key) throws DbException {

    String tab = data.getTableName();

    //�������������������(task_id)���м��뵽��¼��

    if (operContains("task", tab) != null) {

      heandleTask(data, key);

    }
    else { //һ���Ĳ���

      dao.Insert(data, key);

    }

  }

  /**
   * �����ǰ����

   * @param data

   * @return

   * @throws DbException

   */

  private void beforeInsert(GDSet data, long[] key) throws DbException {

    String tab = data.getTableName();

    String type = null;

    //�������ı��������������mon_task_tab�в����¼���õ�����

    if ( (type = operContains("task", tab)) != null) {

      handleBeforeTask(type, key);

    }

  }

  /**
   * ����ĺ����

   * @param data

   */

  private void afterInsert(GDSet data) {

  }

  /**
   * ��long������ת���ַ�������

   * @param key

   * @return

   */

  private String[] long2String(long[] key) {

    String[] s = new String[key.length];

    for (int i = 0; i < s.length; i++) {

      s[i] = String.valueOf(key[i]);

    }

    return s;

  }

  /**
   * ���������Ļ���,����������

   * @param type ���������

   * @param key[out] ����

   * @return

   * @throws DbException

   */

  private void handleBeforeTask(String type, long[] key) throws DbException {

    try {

      String[] field = {

	  "type"};

      GDSet set = new GDSet("mon_task_tab", field);

      String rec[] = {

	  type};

      //����,�õ�����

      for (int i = 0; i < key.length; i++) {

	set.addRow(rec);

      }

      dao.Insert(set, key);

    }
    catch (GDSetException ex) {

      throw new DbException("DAOLogicComponent handleBeforeTask:GDSet����", ex);

    }

  }

  /**
   * ��������

   * @param data

   * @param key

   * @throws DbException

   */

  private void heandleTask(GDSet data, long[] key) throws DbException {

    try {

      GDSet ret = data.getSubGDSet(0, data.getRowCount());

      String[] skey = long2String(key);

      ret.addColumn("task_id", "task_id", Column.COLUMN_TYPE_LONG, skey);

      dao.Insert(ret);

    }
    catch (GDSetException ex) {

      throw new DbException("DAOLogicComponent commonInsert:GDSet����", ex);

    }

  }

}
