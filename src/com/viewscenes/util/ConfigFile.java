package com.viewscenes.util;



import java.io.*;

import java.util.*;



/**

 * <p>Title: </p>

 * <p>Description: </p>

 * <p>Copyright: Copyright (c) 2002</p>

 * <p>Company: </p>

 * @author dyt

 * @version 1.0

 */



public class ConfigFile {



  String _fileName;



  HashMap map = null;



  public ConfigFile(String fileName) {

    _fileName = fileName;

    map = null;

  }



  /**

   * ���������ļ���(ȱʡΪc:\MonWebConfig.sys)

   * @param fileName

   */

  public synchronized void setFileName(String fileName) {

    _fileName = fileName;

    map = null;

  }



  /**

   * �������ļ��л�ȡ����ֵ

   * @param name

   * @return

   */

  public synchronized String getPara(String name) throws ConfigFileException {

    try {

      //{{add by 2002-10-26

      name = name.toLowerCase();

      if (map == null) {

        getConfig();

      }



      if (map == null) {

        throw new ConfigFileException("getConfig Null Property's Pointer");

      }



      Object obj = map.get(name);



      if (obj == null) {

        throw new ConfigFileException("Can't find the paraname:" + name);

      }



      return (String) obj;

    }

    catch (ConfigFileException e) {

      throw e;

    }

    catch (Exception e) {

      throw new ConfigFileException(e.getMessage(), e);

    }

  }



  /**

   * ��ȡ�������ݵ�map��

   * @return

   * @throws ConfigFileException

   */



  public synchronized boolean getConfig() throws

      ConfigFileException {

    //���hashmap�Ѿ��������ļ����룬���ٶ�

    if (map != null) {

      return true;

    }



    BufferedReader in = null;

    try {

      //new һ��hashmap

      map = new HashMap();

      //���ļ�����DataInputStream



      Class cls = this.getClass(); //Class.forName("com.viewscenes.util");



      in = new BufferedReader(new InputStreamReader(

          cls.getClassLoader().getResourceAsStream(_fileName)));



      String s = null;

      //ѭ��ȡ�ļ���ÿһ��,��null��ʾ�ļ�����

      while ( (s = in.readLine()) != null) {

        s = s.trim();

        if (s.startsWith("//")) { // ȥ��ע����

          continue;

        }



        //���������'=',���=ǰ���ִ���Ϊmap��index,=�����Ϊmap��ֵ

        int n = s.indexOf('=');

        if (n != -1) {

          map.put(s.substring(0, n).trim(), s.substring(n + 1).trim()); // modified by dyt 2002-11-04

        }

      }

    }

    catch (Exception e) {

      throw new ConfigFileException(e.getMessage(), e);

    }

    finally {

      try {

        if (in != null) {

          in.close();

        }

      }

      catch (Exception e) {

        throw new ConfigFileException(e.getMessage(), e);

      }

    }

    return true;

  }

}

