package com.viewscenes.dao.cache;

import java.util.*;
import com.viewscenes.util.LogTool;
import com.viewscenes.dao.XmlReader;


/**
 * ˢ�»����߳�

 * <p>Title: </p>

 * <p>Description: </p>

 * <p>Copyright: Copyright (c) 2003</p>

 * <p>Company: </p>

 * @author not attributable

 * @version 1.0

 */

public class CacheThread

    extends Thread {

  DAOCacheComponent dao = new DAOCacheComponent();

  public void run() {

    try {

      while (true) {

        sleep(getFreshInterval() * 1000);

        //�õ����л����map

        Map map = dao.getMap();

        Set key = map.keySet();

        Iterator iter = key.iterator();

        while (iter.hasNext()) {

          String tab = (String) iter.next();

          freshCache(tab, map);

        }

      }

    }
    catch (Exception ex) {

      LogTool.output("Cache Fresh Thread exit!");

      ex.printStackTrace();

    }

  }

  /**
   * �õ�����ˢ��ʱ����

   * @return

   */

  private long getFreshInterval() {

    String time = XmlReader.getAttrValue("dbconfig", "para", "freshtime");

    return Long.parseLong(time);

  }

  /**
   * ���»���

   * @param tab

   * @param map

   */

  private void freshCache(String tab, Map map) {

    CacheObject cache = (CacheObject) map.get(tab);

    if (cache.IsDirty()) {

      dao.DB2Cache(tab);

      LogTool.debug("Cache change:" + tab);

    }

  }

}
