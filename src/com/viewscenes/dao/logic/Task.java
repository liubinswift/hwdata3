package com.viewscenes.dao.logic;

import java.util.*;

import org.jdom.*;
import com.viewscenes.dao.*;
import com.viewscenes.dao.database.*;
import com.viewscenes.dao.innerdao.*;
import com.viewscenes.pub.*;

public class Task
    extends DAOComponent {

  /**
   * ���루�Զ����������ģ�֧�ֶ��в��룩���õ�һ������key

   */

  public int[] Insert(GDSet data, long[] key) throws DbException {

    int[] row = {
        1, 1};

    long[] temkey = new long[data.getRowCount()];

    beforeInsert(data, temkey);

    commonInsert(data, temkey);

    //���key��Ϊ�գ�����������鸴��

    if (key != null) {

      System.arraycopy(temkey, 0, key, 0, temkey.length);

    }

    return row;

  }

  /**
   * ����

   * @param data

   * @param key

   * @throws DbException

   */

  private int[] commonInsert(GDSet data, long[] key) throws DbException {

    try {

      GDSet ret = data.getSubGDSet(0, data.getRowCount());

      String[] skey = long2String(key);

      if (ret.getColumnIndex("task_id") == -1) {

        ret.addColumn("task_id", "task_id", Column.COLUMN_TYPE_LONG);

      }

      ret.updateColumnData("task_id", 0, skey);

      DAOComponent dao = new DAOComponent();

      return dao.Insert(ret);

    }
    catch (GDSetException ex) {

      throw new DbException("DAOLogicComponent commonInsert:GDSet����", ex);

    }

  }

  /**
   * �����ǰ����

   * ����mon_task_tab�в����¼���õ�����

   * @param data

   * @return

   * @throws DbException

   */

  private int[] beforeInsert(GDSet data, long[] key) throws DbException {

    String tab = data.getTableName();

    String type = getType("task", tab);

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

      DAOComponent dao = new DAOComponent();

      return dao.Insert(set, key);

    }
    catch (GDSetException ex) {

      throw new DbException("DAOLogicComponent handleBeforeTask:GDSet����", ex);

    }

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
   * �õ�����������

   * @param caption �������͵�����

   * @param tabName ����

   * @return

   * null:������caption��Ĳ���

   * String:��tab��Ӧ��type

   */

  private String getType(String caption, String tabName) {

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

}
