package com.viewscenes.dao.cache;

import java.util.*;
import com.viewscenes.pub.GDSet;
import com.viewscenes.dao.XmlReader;
import com.viewscenes.dao.innerdao.Common;
import com.viewscenes.pub.GDSetException;



/**
 * �������

 * ������ResultSet�洢�����ݺʹ洢������keysMap

 * <p>Title: </p>

 * <p>Description: </p>

 * <p>Copyright: Copyright (c) 2003</p>

 * <p>Company: </p>

 * @author not attributable

 * @version 1.0

 */

public class CacheObject {

  private GDSet _gd; //��������

  private HashMap keysMap; //key:ÿ�ű��������1������ object:keyToIndexMap

  private String[] keysArray; //��¼�ñ������ֵ

  private HashMap[] keyToIndexMap; //��¼��������ֵ���кŵĹ�ϵ

  private String tabName;

  //���־�����insert,update,delete�ı����ݿ⣬�����ô�ֵΪ�࣬CacheThread����ʱˢ��

  private boolean _dirty;

  public CacheObject(GDSet gd, String tabName) {

    this._gd = gd;

    this.tabName = tabName;

    try {

      //�����������кŹ�ϵ

      loadKeysArray();

      loadIndexMap();

      loadKeysMap();

    }
    catch (Exception ex) {

      ex.printStackTrace();

    }

  }

  /**
   * �������־

   */

  public void setDirty() {

    _dirty = true;

  }

  /**
   * ������־

   */

  public void clearDirty() {

    _dirty = false;

  }

  /**
   * �Ƿ�Ϊ��

   * @return

   */

  public boolean IsDirty() {

    return _dirty;

  }

  /**
   * �õ���������

   * @return

   */

  public GDSet getData() {

    return _gd;

  }

  /**
   * ��������

   */

  private void loadKeysArray() {

    //�õ��������

    String indexs = XmlReader.getNextAttrValue("cachconfig", "table", tabName,
                                               "index");

    keysArray = Common.Field2Array(indexs);

  }

  /**
   * ����loadKeysMap������������ֵ(keysArray)������ֵ���кŵĹ�ϵmap(keyToIndexMap)

   */

  private void loadKeysMap() {

    keysMap = new HashMap();

    for (int i = 0; i < keysArray.length; i++) {

      keysMap.put(keysArray[i], keyToIndexMap[i]);

    }

  }

  /**
   * ����keyToIndexMap��������ֵ���кŵĹ�ϵ

   */

  private void loadIndexMap() {

    //assert _gd != null:"data����Ϊnull";

    int indexSize = keysArray.length;

    //ÿ��������Ӧһ��hashMap,��hashMap��¼rs�и�������ֵ���кŵĶ�Ӧ��ϵ

    keyToIndexMap = newHashMapArray(indexSize);

    int row = 0;

    try {

      for (int i = 0; i < _gd.getRowCount(); i++) {

        for (int j = 0; j < indexSize; j++) {

          String indexValue = _gd.getString(i, keysArray[j]);

          keyToIndexMap[j].put(indexValue, new Integer(row));

        }

        row++;

      }

    }
    catch (GDSetException ex) {

      ex.printStackTrace();

    }

  }

  /**
   * �Ƿ���������

   * @param index

   * @return

   */

  public boolean hitIndex(String index) {

    if (keysMap.get(index) == null) {

      return false;

    }

    return true;

  }

  /**
   * ���������õ����е�����

   * @param index

   * @return

   * null:û������

   */

  public GDSet getDataByIndex(String index, String value) {

    int row = 0;

    try {

      if ( (row = getRowByIndex(index, value)) != -1) {

        return _gd.getSubGDSet(row, row + 1);

      }

    }
    catch (GDSetException ex) {

      ex.printStackTrace();

    }

    return null;

  }

  /**
   * ���������õ����е��к�

   * @param index

   * @return

   */

  private int getRowByIndex(String index, String value) {

    HashMap map = (HashMap) keysMap.get(index);

    if (map == null) {

      return -1;

    }

    Integer row = (Integer) map.get(value);

    if (row == null) {

      return -1;

    }

    return row.intValue();

  }

  /**
   * ����HashMap�������鼰��ʼ��

   * @param size

   * @return

   */

  private HashMap[] newHashMapArray(int size) {

    HashMap[] keyToIndexMap = new HashMap[size];

    for (int i = 0; i < keyToIndexMap.length; i++) {

      keyToIndexMap[i] = new HashMap();

    }

    return keyToIndexMap;

  }

}
