package com.viewscenes.dao.cache;

import java.util.*;

import org.jdom.*;
import com.viewscenes.dao.DAOOperator;
import com.viewscenes.dao.XmlReader;
import com.viewscenes.pub.GDSet;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.pub.GDSetException;
import com.viewscenes.dao.innerdao.Common;
import com.viewscenes.util.LogTool;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.cache.CacheThread;



/**
 * ���ݿ⻺����ʲ���

 * query:��ѯ����

 * hitCache:�Ƿ����л���

 */

public class DAOCacheComponent

    extends DAOOperator {

  /**
   * ����CacheRowset

   */



  //CacheObject�ͱ�����Map����֤���԰�ȫ��

  private static Map _map = Collections.synchronizedMap(new HashMap());

  //������������ϵ��ӳ���,���������䶯���ҳ���Ӧ�Ļ��������

  //keyΪ���������objectΪArrayList:��¼��Ӧ�Ļ������

  private static Map _tabNameMap = Collections.synchronizedMap(new HashMap());

  static String[] _tabName;

  static {

    //���������ļ�

    getCacheTab();

    //���ػ�����������ϵ��ӳ���

    loadTabMap();

    //�������ݵ�����

    load();

    //����ˢ���߳�

    new CacheThread().start();

  }

  /**
   * �õ����л���ı���

   */

  private static void getCacheTab() {

    try {
      Element ele = XmlReader.getConfigItem("cachconfig");

      List list = ele.getChildren();

      int size = list.size();

      _tabName = new String[size];

      for (int i = 0; i < size; i++) {

        Element sub = (Element) list.get(i);

        _tabName[i] = sub.getAttributeValue("table");

      }
    }
    catch (Exception e) {
      LogTool.debug(e);
    }

  }

  /**
   * �����ѯ

   * @param field

   * "*"��ѯȫ��

   * @param condition

   * condition�ĸ�ʽΪ

   *     �����趨TableName

   *     1��5�еĶ�ά���ݼ�(��ʱ֧��һ��������

   *     �б���̶�Ϊ��

   *         field����������

   *         type����������(VARCHAR,NUMBER,DATE��

   *               ����ֻ֧��VARCHAR,NUMBER

   *         operator��������(����ֻ֧��=)

   *         value��ֵ

   *         between����ǰһ����ѯ����֮��Ĺ�ϵ����һ����¼Ĭ��Ϊand

   * @return String

   */

  public GDSet Query(String field, GDSet condition) throws DbException {

    //assert (field != null && !field.equals("") && condition != null):"����Ϊ��";

    GDSet gd = null;

    String tname = condition.getTableName();

    //�õ���Ļ������

    CacheObject obj = getCatchObject(tname);

    GDSet gdData = obj.getData();

    try {

      gd = createGDStruct(gdData, field);

      //���rsΪ�գ������̷���

      if (gdData.getRowCount() == 0) {

        return gd;

      }

      //��ѯ����������center_id=5��name="����ܾ�"

      //center_id����������������ѯ��name���������������gdһ��

      String fd = getConditionField(condition);

      //��������

      if (obj.hitIndex(fd)) {

        getCachebyIndex(condition, obj, gd);

        return gd;

      }

      //����������

      getCache(condition, gdData, gd);

    }

    catch (GDSetException ex) {

      throw new DbException("DAOCacheComponent Query:GDSet�Ĵ���", ex);

    }

    return gd;

  }

  public GDSet Query(String field, GDSet condition, int start, int end) throws

      DbException {

    //assert (field != null && !field.equals("") && condition != null):"����Ϊ��";

    //assert (start >= -1 && end >= 0 && start < end):

    //"start��end����Ҫ����0,start����<end";

    try {

      return Query(field, condition).getSubGDSet(start, end);

    }

    catch (GDSetException ex) {

      throw new DbException("DAOCacheComponent Query:GDSet�Ĵ���", ex);

    }

  }

  /**
   * �����ݿ�����װ�ص�����

   */

  public static void load() {

    if (_tabName == null) {

      return;

    }

    for (int i = 0; i < _tabName.length; i++) {

      DB2Cache(_tabName[i]);

    }

  }

  /**
   * �õ�������map

   * @return

   */

  protected Map getMap() {

    return _map;

  }

  /**
   *  �����Ƿ�����

   * @param tname

   * @return

   */

  public boolean hitCache(String tname) {

    if (_map == null || _map.get(tname) == null) {

      return false;

    }

    return true;

  }

  /**
   * ȡ��GDSetĳ��ָ����ĳЩ�ֶ�ֵ����ϳ����鷵��

   * @param gdData ����Դ

   * @param Column ָ�����ص��ֶ���

   * @param row ��λgdData����

   * @return

   * @throws DbException

   */

  private String[] getRow(GDSet gdData, String[] Column, int row) throws
      DbException {

    //assert Column != null:"DAOCacheComponent getRow:Column ����Ϊnull";

    String[] value = new String[Column.length];

    String curField = null;

    try {

      for (int i = 0; i < Column.length; i++) {

        curField = Column[i];

        value[i] = gdData.getString(row, Column[i]);

      }

    }

    catch (GDSetException ex) {

      throw new DbException("DAOCacheComponent getRow: " + curField + "ȡ��������",
                            ex);

    }

    return value;

  }

  /**
   * ���ز�ѯ�ֶ�����

   * @param gdData

   * @param field

   * @return

   */

  private String[] getFieldAry(GDSet gdData, String field) {

    String[] fieldArray = null;

    //�����ѯȫ�����򷵻ظñ��ȫ���ֶ�

    if (field.equals("*")) {

      fieldArray = gdData.getAllColumnName();

    }

    else {

      fieldArray = Common.Field2Array(field);

    }

    return fieldArray;

  }

  /**
   * ����һ����_map��

   * ���������ļ�cache.property�õ�����������ݿ���Դ��Ĺ�ϵ

   * @param tab ����

   */

  protected static synchronized void DB2Cache(String tab) {

    DbComponent db = new DbComponent();

    String sql = XmlReader.getNextAttrValue("cachconfig", "table", tab, "sql");

    try {

      GDSet gdData = db.Query(sql);

      CacheObject obj = new CacheObject(gdData, tab);

      _map.put(tab, obj);

    }

    catch (DbException ex) {

      ex.printStackTrace();

    }

  }

  /**
   * �õ����е�CacheObject

   * @param tname

   * @return

   */

  private CacheObject getCatchObject(String tname) {

    //����û������ݲ����ڻ�Ϊdirty,��reload ����

    if (_map == null || _map.get(tname) == null

        || ( (CacheObject) _map.get(tname)).IsDirty()) {

      DB2Cache(tname);

    }

    return (CacheObject) _map.get(tname);

  }

  /**
   * �����ɾ�ģ�����ñ��־λΪdirty

   * @param tab

   */

  public void setDirty(String tab) {

    ArrayList list = (ArrayList) _tabNameMap.get(tab);

    if (list == null) {
      return;
    }

    for (int i = 0; i < list.size(); i++)

    {

      CacheObject cache = (CacheObject) _map.get(list.get(i));

      if (cache != null) {

        cache.setDirty();

      }

    }

  }

  /**
   * ��������û���

   * @param condition ��ѯ����

   * @param obj �������

   * @param gd[out] ���ص�GDSet

   * @return

   * @throws DbException

   */

  private void getCachebyIndex(GDSet condition, CacheObject obj, GDSet gd) throws
      DbException {

    try {

      LogTool.debug("hit index");

      //�õ���ѯ���������Ժ�ֵ

      String field = condition.getString(0, "field");

      String value = condition.getString(0, "value");

      //��������������������

      GDSet data = obj.getDataByIndex(field, value);

      //���û���ҵ���¼����ֱ�ӷ���

      if (data == null) {

        return;

      }

      String[] row = getRow(data, gd.getAllColumnName(), 0);

      gd.addRow(row);

    }

    catch (GDSetException ex) {

      throw new DbException("DAOCacheComponent getCachebyIndex:GDSet�Ĵ���", ex);

    }

  }

  /**
   * ��������õ����ϲ�ѯ�����ļ�¼

   * @param retfdArray ���ص��ֶ�����

   * @param conField ��ѯ�����ֶ�

   * @param conValue ��ѯ����ֵ

   * @param gdData

   * @param gd ���ص�GDSet

   * @throws DbException

   */

  private void getCache(GDSet condition, GDSet srcData, GDSet gd) throws
      DbException {

    LogTool.debug("not hit Index");

    try {

      for (int i = 0; i < srcData.getRowCount(); i++) {

        //�����������������ϸ��м�¼

        if (gdFillCondition(condition, srcData, i)) {

          String[] row = getRow(srcData, gd.getAllColumnName(), i);

          gd.addRow(row);

        }

      }

    }

    catch (GDSetException ex) {

      throw new DbException("DAOCacheComponent getHitCache:GDSet�Ĵ���", ex);

    }

  }

  /**
   * ��ǰgd�Ƿ������ѯ����

   * @param condition

   * @param gdData

   * @return

   * true:�ɹ�

   * false:ʧ��

   */

  private boolean gdFillCondition(GDSet condition, GDSet gdData, int curRow) throws
      DbException {

    //���û�в�ѯ�������򷵻���

    if (condition.getRowCount() == 0) {

      return true;

    }

    try {

      //�õ���ѯ�����ĸ�����

      String field = condition.getString(0, "field");

      String value

          = condition.getString(0, "value");

      if (gdData.getString(curRow, field).equals(value)) {

        return true;

      }

      else {

        return false;

      }

    }

    catch (GDSetException ex) {

      throw new DbException("DAOCacheComponent fillCondition:GDSet�Ĵ���", ex);

    }

  }

  /**
   * �õ���ѯ�������ֶε�ֵ

   * @param condition

   * @return

   * @throws DbException

   */

  private String getConditionField(GDSet condition) throws DbException {

    try {

      if (condition.getRowCount() == 0) {

        return "";

      }

      return condition.getString(0, "field");

    }

    catch (GDSetException ex) {

      throw new DbException("DAOCacheComponent getField:GDSet�Ĵ���", ex);

    }

  }

  /**
   * ��fieldΪ�б��⹹��һ��GDSet,���fieldΪ*,����һ���ṹ��srcGdһ����GDSet

   * @param srcGd Դ

   * @param field �ֶ�������","�ָ�

   * @return

   */

  private GDSet createGDStruct(GDSet srcGd, String field) throws GDSetException {

    GDSet gd = null;

    //���fieldΪ*,����һ���ṹ��srcGdһ����GDSet

    if (field.equals("*")) {

      gd = srcGd.getSubGDSet(0, 0);

    }

    else {

      //��fieldΪ�б��⹹��һ��GDSet

      String[] fieldArray = Common.Field2Array(field);

      gd = new GDSet(srcGd.getTableName(), fieldArray);

    }

    return gd;

  }

  /**
   * ���ػ�����������ϵ��ӳ���

   * keyΪ���������objectΪArrayList:��¼��Ӧ�Ļ������

   */

  private static void loadTabMap() {

    List list = XmlReader.getConfigItem("cachconfig").getChildren();

    for (int i = 0; i < list.size(); i++) {

      Element ele = (Element) list.get(i);

      String cacheTab = ele.getAttributeValue("table");

      String[] phyTabs = Common.Field2Array(ele.getAttributeValue("alltable"));

      for (int j = 0; j < phyTabs.length; j++) {

        ArrayList ary = (ArrayList) _tabNameMap.get(phyTabs[j]);

        if (ary == null) {

          ary = new ArrayList();

        }

        ary.add(cacheTab);

        _tabNameMap.put(phyTabs[j], ary);

      }

    }

  }

}
