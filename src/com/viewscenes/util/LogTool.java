package com.viewscenes.util;



import com.viewscenes.util.log.*;



/**

 *

 * <p>Title: ��־����</p>

 * <p>Description: ��־���߿ɼ�¼�ַ������쳣����¼����Ϣ��Ϊ5�����𣬰���

 * �ӵ͵��ߵ�˳��ֱ���debug, info, warning, fatal, output��debug�����¼

 * ϵͳ������Ϣ��info�����¼�ճ���ʾ��Ϣ��warning�����¼һ�������Ϣ��fatal

 * �����¼���ص�ϵͳ����output��������ǿ�Ƽ�¼��Ϣ��������������Ҫ���鲻Ҫ

 * �ڳ�����ʹ��</p>

 * <p>�������ļ��п������ö��logger��ÿ��logger����ʹ�ò�ͬ�ķ�ʽ��¼��־����

 * ʹ����־���߼�¼��־ʱ������ֱ��ʹ��Ĭ�ϵ�logger��¼��־��Ҳ����ָ��logger��

 * ����ʹ���ض���logger�����ָ����logger�����ڣ���־����ʹ��Ĭ�ϵ�logger��¼��

 * Ϣ��������ÿ���������ʹ�ø��Ե�logger����¼��Ϣ�������Ϳ�������ϵͳ����־��Ϣ

 * ���ࡣ

 * </p>

 * <p>Copyright: Copyright (c) 2003</p>

 * <p>Company: </p>

 * @author chengang

 * @version 1.0

 */

public class LogTool {

  private static String configPath = "logconfig.xml";


  private static LogManager manager = new LogManager(configPath);

  protected synchronized static LogManager getDBPoolManager() {

    if (manager == null) {

      manager = new LogManager(configPath);

    }

    return manager;

  }



  protected synchronized static Logger getLogger() {

    if (manager == null) {

      manager = new LogManager(configPath);

    }

    Logger log = null;

    try {

      log = manager.getLogger();

    }

    catch (Exception ex) {

    }

    return log;

  }



  protected synchronized static Logger getLogger(String loggerName) {

    if (manager == null) {

      manager = new LogManager(configPath);

    }

    Logger log = null;

    try {

      log = manager.getLogger(loggerName);

    }

    catch (Exception ex) {

    }

    return log;

  }



  protected synchronized static void log(String msg, int level) {

    getLogger().log(msg, level);

  }



  /**

   * ʹ��Ĭ�ϵ�logger��¼Debug�������־

   * @param msg

   */

  public synchronized static void debug(String msg) {
    getLogger().log(msg, LogManager.LEVEL_DEBUG);
  }



  /**

   * ʹ��Ĭ�ϵ�logger��¼Info�������־

   * @param msg

   */

  public synchronized static void info(String msg) {

    getLogger().log(msg, LogManager.LEVEL_INFO);

  }



  /**

   * ʹ��Ĭ�ϵ�logger��¼warning�������־

   * @param msg

   */

  public synchronized static void warning(String msg) {

    getLogger().log(msg, LogManager.LEVEL_WARNING);

  }



  /**

   * ʹ��Ĭ�ϵ�logger��¼fatal�������־

   * @param msg

   */

  public synchronized static void fatal(String msg) {

    getLogger().log(msg, LogManager.LEVEL_FATAL);

  }



  /**

   * ʹ��Ĭ�ϵ�loggerǿ�������־

   * @param msg

   */

  public synchronized static void output(String msg) {

    getLogger().log(msg, LogManager.LEVEL_OUTPUT);

  }



  protected synchronized static void log(Exception e, int level) {

    getLogger().log(e, level);

  }



  /**

   * ʹ��Ĭ�ϵ�logger���Info�����Exception��Ϣ

   * @param e

   */

  public synchronized static void debug(Exception e) {

    getLogger().log(e, LogManager.LEVEL_DEBUG);

  }



  /**

   * ʹ��Ĭ�ϵ�logger���Debug�����Exception��Ϣ

   * @param e

   */

  public synchronized static void info(Exception e) {

    getLogger().log(e, LogManager.LEVEL_INFO);

  }



  /**

   * ʹ��Ĭ�ϵ�logger���Warning�����Exception��Ϣ

   * @param e

   */

  public synchronized static void warning(Exception e) {

    getLogger().log(e, LogManager.LEVEL_WARNING);

  }



  /**

   * ʹ��Ĭ�ϵ�logger���Fatal�����Exception��Ϣ

   * @param e

   */

  public synchronized static void fatal(Exception e) {

    getLogger().log(e, LogManager.LEVEL_FATAL);

  }



  /**

   * ʹ��Ĭ�ϵ�loggerǿ�����Exception��Ϣ

   * @param e

   */

  public synchronized static void output(Exception e) {

    getLogger().log(e, LogManager.LEVEL_OUTPUT);

  }



  protected synchronized static void log(String loggerName, Exception e,

                                         int level) {

    getLogger(loggerName).log(e, level);

  }



  /**

   * ʹ��ָ����logger��¼Debug�����Exception��Ϣ

   * @param loggerName logger������

   * @param e

   */

  public synchronized static void debug(String loggerName, Exception e) {

    getLogger(loggerName).log(e, LogManager.LEVEL_DEBUG);

  }



  /**

   * ʹ��ָ����logger��¼Info�����Exception��Ϣ

   * @param loggerName logger������

   * @param e

   */

  public synchronized static void info(String loggerName, Exception e) {

    getLogger(loggerName).log(e, LogManager.LEVEL_INFO);

  }



  /**

   * ʹ��ָ����logger��¼Warning�����Exception��Ϣ

   * @param loggerName logger������

   * @param e

   */

  public synchronized static void warning(String loggerName, Exception e) {

    getLogger(loggerName).log(e, LogManager.LEVEL_WARNING);

  }



  /**

   * ʹ��ָ����logger��¼Fatal�����Exception��Ϣ

   * @param loggerName logger������

   * @param e

   */

  public synchronized static void fatal(String loggerName, Exception e) {

    getLogger(loggerName).log(e, LogManager.LEVEL_FATAL);

  }



  /**

   * ʹ��ָ����loggerǿ�����Exception��Ϣ

   * @param loggerName logger������

   * @param e

   */

  public synchronized static void output(String loggerName, Exception e) {

    getLogger(loggerName).log(e, LogManager.LEVEL_OUTPUT);

  }



  protected synchronized static void log(String loggerName, String msg,

                                         int level) {

    getLogger(loggerName).log(msg, level);

  }



  /**

   * ʹ��ָ����logger��¼Debug�������Ϣ

   * @param msg

   */

  public synchronized static void debug(String loggerName, String msg) {

    getLogger(loggerName).log(msg, LogManager.LEVEL_DEBUG);

  }



  /**

   * ʹ��ָ����logger��¼Info�������Ϣ

   * @param msg

   */

  public synchronized static void info(String loggerName, String msg) {

    getLogger(loggerName).log(msg, LogManager.LEVEL_INFO);

  }



  /**

   * ʹ��ָ����logger��¼Warning�������Ϣ

   * @param msg

   */

  public synchronized static void warning(String loggerName, String msg) {

    getLogger(loggerName).log(msg, LogManager.LEVEL_WARNING);

  }



  /**

   * ʹ��ָ����logger��¼Fatal�������Ϣ

   * @param msg

   */

  public synchronized static void fatal(String loggerName, String msg) {

    getLogger(loggerName).log(msg, LogManager.LEVEL_FATAL);

  }



  /**

   * ʹ��ָ����loggerǿ�������Ϣ

   * @param msg

   */

  public synchronized static void output(String loggerName, String msg) {

    getLogger(loggerName).log(msg, LogManager.LEVEL_OUTPUT);

  }

}
