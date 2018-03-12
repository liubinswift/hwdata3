package com.viewscenes.dao.innerdao;

import java.util.*;
import com.viewscenes.pub.GDSet;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.pub.GDSetException;
import com.viewscenes.dao.ConditionColumn;
import com.viewscenes.dao.Operator;



/**
 * ��������Ĺ�����

 * �ѱ�ʾ������GDSet����ֽ�ɱ�������Ҫ���ص��ֶ�������ѯ����

 * <p>Title: </p>

 * <p>Description: </p>

 * <p>Copyright: Copyright (c) 2003</p>

 * <p>Company: </p>

 * @author not attributable

 * @version 1.0

 */

public class ConBaseFilter {

  protected String _tabName; //����

  protected String _field; //�ֶ�

  protected String _condition = " "; //����

  private static Map _map;

  static {

    _map = new HashMap();

    //���ز�����������

    Handler orderHandler = new HandlerOrder();

    Handler comHandler = new HandlerCommon();

    Handler inHandler = new HandlerIn();

    Handler braHandler = new HandlerBracket();

    Handler groupHandler = new HandlerGroup();

    _map.put(Operator._IN, new HandlerIn());

    _map.put(Operator._NOTIN, new HandlerIn());

    _map.put(Operator._ORDERBY, orderHandler);

    _map.put(Operator._GREAT, comHandler);

    _map.put(Operator._GREATEQUS, comHandler);

    _map.put(Operator._EQUAL, comHandler);

    _map.put(Operator._LESS, comHandler);

    _map.put(Operator._LESSEQUS, comHandler);

    _map.put(Operator._UNEQUAL, comHandler);

    _map.put(Operator._UNEQUAL1, comHandler);

    _map.put(Operator._LIKE, comHandler);

    _map.put(Operator._NOTLIKE, comHandler); //add by xyw

    _map.put(Operator._BRACKET, braHandler);

    _map.put(Operator._GROUP, groupHandler);

  }

  public ConBaseFilter() {

  }

  public ConBaseFilter(String field, GDSet condition) throws DbException {

    String tab = condition.getTableName();

    //���ø�������

    setField(field);

    setTabName(tab);

    setCondition(condition);

  }

  /**
   * ���ò�ѯ����������

   * @param condition

   * @throws DbException

   */

  private void setCondition(GDSet condition) throws DbException {

    _condition = combinCondition(condition);

  }

  /**
   * �����ֶε�����

   * @param field

   */

  private void setField(String field) {

    _field = field;

  }

  /**
   * ���ñ���������

   * @param tabName

   */

  private void setTabName(String tabName) {

    _tabName = tabName;

  }

  /**
   * �õ�����

   * @return

   */

  public String getCondition() {

    return _condition.trim();

  }

  /**
   * �õ�ѡ���ֶ�

   * @return

   */

  public String getField() {

    return _field;

  }

  /**
   * �õ�����

   * @return

   */

  public String getTabName() {

    return _tabName;

  }

  /**
   * �õ���i�е�fieldֵ

   * @param condition

   * @param conditioni ������

   * @return �ֶ�ֵ

   * @throws GDSetException

   */

  protected String getConColumn(GDSet condition, int i) throws GDSetException {

    return condition.getString(i, ConditionColumn.FIELD);

  }

  /**
   * �õ���i�е�typeֵ

   * @param condition

   * @param conditioni ������

   * @return �ֶ�ֵ

   * @throws GDSetException

   */

  protected String getConType(GDSet condition, int i) throws GDSetException {

    return condition.getString(i, ConditionColumn.TYPE);

  }

  /**
   * �õ���i�е�OPERATORֵ

   * @param condition

   * @param conditioni ������

   * @return �ֶ�ֵ

   * @throws GDSetException

   */

  protected String getConOperator(GDSet condition, int i) throws GDSetException {

    return condition.getString(i, ConditionColumn.OPERATOR).trim();

  }

  /**
   * �õ���i�е�VALUEֵ

   * @param condition

   * @param conditioni ������

   * @return �ֶ�ֵ

   * @throws GDSetException

   */

  protected String getConValue(GDSet condition, int i) throws GDSetException {

    return condition.getString(i, ConditionColumn.VALUE);

  }

  /**
   * �õ���i�е�BETWEENֵ

   * @param condition

   * @param conditioni ������

   * @return �ֶ�ֵ

   * @throws GDSetException

   */

  protected String getConBetwwen(GDSet condition, int i) throws GDSetException {

    return condition.getString(i, ConditionColumn.BETWEEN);

  }

  /**
   * ��ϲ�ѯ����

   * @param condition

   * @return

   */

  protected String combinCondition(GDSet condition) throws DbException {

    try {

      SqlObject sqlObj = new SqlObject();

      for (int i = 0; i < condition.getRowCount(); i++) {

        Condition subCon = new Condition();

        subCon.setColumn(getConColumn(condition, i));

        subCon.setType(getConType(condition, i));

        subCon.setOper(getConOperator(condition, i));

        subCon.setValue(getConValue(condition, i));

        subCon.setBetween(getConBetwwen(condition, i));

        String oper = getConOperator(condition, i);

        getHandler(oper).execute(subCon, sqlObj);

      }

      return sqlObj.toString();

    }
    catch (GDSetException ex) {

      throw new DbException("ConBaseFilter combinCondition", ex);

    }

  }

  /**
   * �õ���������Ӧ�Ĵ�����

   * @param oper ������������

   * @return

   */

  private Handler getHandler(String oper) throws DbException {

    Handler hander = (Handler) _map.get(oper);

    if (hander == null) {

      throw new DbException("ConBaseFilter getHandler:�������" + oper + "������");
    }

    return hander;

  }

}
