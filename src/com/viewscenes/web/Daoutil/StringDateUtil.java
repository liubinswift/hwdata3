package com.viewscenes.web.Daoutil;

import java.text.*;
import java.util.*;

/**
 * <p>Title: ʱ�����ַ���������</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class StringDateUtil {
  public final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
  public final static String TIME_FORMAT = "HH:mm:ss";
  public final static String CAL_FORMAT  = "yyyy-MM-dd";

  public StringDateUtil() {
  }

  /**
   * ʱ���ַ�����Date����.
   * @param timeStr String
   * @return Date
   */
  public static Date string2Date(String dateStr) throws Exception {
    Date strDate = null;
    if(dateStr==null || dateStr.length()==0){
      return strDate;
    }

    /**
     * ��Ҫ��֤ʱ���ʽ��ȷ���Ժ������䡣
     */
    try {
      SimpleDateFormat d = new SimpleDateFormat(DATE_FORMAT);
      ParsePosition pos = new ParsePosition(0);
      strDate = d.parse(dateStr, pos);
    }
    catch (Exception ex) {
      throw new Exception("�ַ�תʱ���ʽ����" + ex.getMessage(), ex);
    }
    return strDate;
  }
  /**
   * ʱ���ַ�����Date����.
   * @param timeStr String
   * @return Date
   */
  public static Date string2Date(String dateStr,String SimpleDateFormat) throws Exception {
    Date strDate = null;
    if(dateStr==null || dateStr.length()==0){
      return strDate;
    }

    /**
     * ��Ҫ��֤ʱ���ʽ��ȷ���Ժ������䡣
     */
    try {
      SimpleDateFormat d = new SimpleDateFormat(SimpleDateFormat);
      ParsePosition pos = new ParsePosition(0);
      strDate = d.parse(dateStr, pos);
    }
    catch (Exception ex) {
      throw new Exception("�ַ�תʱ���ʽ����" + ex.getMessage(), ex);
    }
    return strDate;
  }
  
  /**
   * Date���͸�ʽ�����ַ���.
   * @param timeStr String
   * @return Date
   */
  public static String date2String(Date strDate) throws Exception {
    if(strDate==null){
      strDate = new Date();
    }
    String dateStr = null;
    /**
     * ��Ҫ��֤ʱ���ʽ��ȷ���Ժ������䡣
     */
    try {
      SimpleDateFormat d = new SimpleDateFormat(DATE_FORMAT);
      dateStr = d.format(strDate);
    }
    catch (Exception ex) {
      throw new Exception("ʱ��ת�ַ���ʽ����" + ex.getMessage(), ex);
    }
    return dateStr;
  }
  /**
   * Date���͸�ʽ�����ַ���.
   * @param timeStr String
   * @return Date
   */
  public static String date2String(Date strDate,String SimpleDateFormat) throws Exception {
    if(strDate==null){
      strDate = new Date();
    }
    String dateStr = null;
    /**
     * ��Ҫ��֤ʱ���ʽ��ȷ���Ժ������䡣
     */
    try {
      SimpleDateFormat d = new SimpleDateFormat(SimpleDateFormat);
      dateStr = d.format(strDate);
    }
    catch (Exception ex) {
      throw new Exception("ʱ��ת�ַ���ʽ����" + ex.getMessage(), ex);
    }
    return dateStr;
  }

  /**
   * ����ַ����Ƿ�ʱ�ڸ�ʽ.
   * @param dateStr String
   * @return Date null����,���򷵻���ȷ������.
   */
  public static Date checkString(String dateStr) {
    Date strDate = null;
    try {
      strDate = string2Date(dateStr);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
    return strDate;
  }

  /**
   * ����ʱ���ַ����õ�ʱ��Timestamp����"2004-01-02 4:45:34"����ת����ͬ�����ڵ�Date
   * @param dateStr String
   * @throws SysDateException
   * @return Date
   */
  public static java.sql.Timestamp getTimestamp(String timeStr) throws
      Exception {
    try {
      return new java.sql.Timestamp(getDateTime(timeStr).getTime());
    }
    catch (Exception e) {
      throw new Exception("�ַ�תʱ���ʽ����" + e.getMessage(), e);
    }
  }

  /**
   * ����ʱ���ַ����õ�ʱ��Date����"2004-01-02 4:45:34"����ת����ͬ�����ڵ�Date
   * @param dateStr String
   * @throws SysDateException
   * @return Date
   */
  public static Date getDateTime(String dateTimeStr) throws Exception {
    try {
      if (dateTimeStr == null) {
        return null;
      }
      SimpleDateFormat sd = new SimpleDateFormat(DATE_FORMAT);
      return sd.parse(dateTimeStr);
    }
    catch (Exception e) {
      throw e;
    }
  }
  
  public static Date getDayTime(String dateTimeStr) throws Exception {
	    try {
	      if (dateTimeStr == null) {
	        return null;
	      }
	      SimpleDateFormat sd = new SimpleDateFormat(CAL_FORMAT);
	      return sd.parse(dateTimeStr);
	    }
	    catch (Exception e) {
	      throw e;
	    }
  }

  public static String getTimeHour(Date time) throws Exception {
    try {
      Calendar cd = Calendar.getInstance();
      cd.setTime(time);
      String hour = String.valueOf(cd.get(Calendar.HOUR_OF_DAY));
      if (hour.length() == 1) {
        hour = "0" + hour;
      }
      return hour;

    }
    catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public static String getTimeMin(Date time) throws Exception {
    try {
      Calendar cd = Calendar.getInstance();
      cd.setTime(time);
      String min = String.valueOf(cd.get(Calendar.MINUTE));
      if (min.length() == 1) {
        min = "0" + min;
      }
      return min;

    }
    catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public static String getTimeSec(Date time) throws Exception {
    try {
      Calendar cd = Calendar.getInstance();
      cd.setTime(time);
      String sec = String.valueOf(cd.get(Calendar.SECOND));
      if (sec.length() == 1) {
        sec = "0" + sec;
      }
      return sec;

    }
    catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public static String getTimeYear(Date time) throws Exception {
    try {
      Calendar cd = Calendar.getInstance();
      cd.setTime(time);
      String year = String.valueOf(cd.get(Calendar.YEAR));
      return year;

    }
    catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  /**
   * ��٣�����ԭ����getTimeMonth����(��Date�����е��·�ֵת�����ַ����͵��·�ֵ)���������������⣺
   * ��1����Calendar.MONTH����ֵΪ�·�ֵ��1��������0����1�·ݣ�����9����10�·ݣ�����Ӧ��ʹ��Calendar.MONTH < 9�ж��·�ֵ�Ƿ�С��10�·�
   * ��2�������ڴ��ڵ���10�·ݵĴ���û�ܽ��м�1����������11�·ݱ������10�·�
   * @param time Date
   * @throws Exception
   * @return String
   */
  public static String getTimeMonth(Date time) throws Exception {
    try {
      Calendar cd = Calendar.getInstance();
      cd.setTime(time);
      int m = cd.get(Calendar.MONTH)+ 1;
      String month = m < 10 ? "0" + m : "" + m;
      return month;
    }
    catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public static String getTimeDay(Date time) throws Exception {
    try {
      Calendar cd = Calendar.getInstance();
      cd.setTime(time);
      String day = String.valueOf(cd.get(Calendar.DAY_OF_MONTH));
      if (day.length() == 1) {
        day = "0" + day;
      }
      return day;

    }
    catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  /**
   * ����A�Ƿ�����B
   * �Ƚ�����date(2004-06-02 13:15:24 )�ַ����Ĵ�С,�����һ�����ڵڶ����ͷ���true������false
   * ��������������У����������������ڵĸ�ʽ�����⣬��ôDateA��DateB������null������Ҳ������trueֵ
   * @param dateA ��ʼʱ��
   * @param dateB ����ʱ��
   * @return
   */
  public static boolean isAfter(String dateAStr, String dateBStr) throws
      Exception {
    boolean result = true;
    Date dateA = string2Date(dateAStr);
    Date dateB = string2Date(dateBStr);
    if ( (dateA != null) && (dateB != null)) {
      try {
        result = dateA.after(dateB);
      }
      catch (Exception e) {}
    }
    return result;
  }

  /**
   * ����ʱ��A�Ƿ���ʱ��B��ʱ��C���м䣨�������ڣ���
   * @param dateAStr String
   * @param dateBStr String
   * @param dateCStr String
   * @throws Exception
   * @return boolean
   */
  public static boolean isMiddle(String dateAStr, String dateBStr,
                                 String dateCStr) throws Exception {
    boolean result = true;
    Date dateA = string2Date(dateAStr);
    Date dateB = string2Date(dateBStr);
    Date dateC = string2Date(dateCStr);
    if ( (dateA != null) && (dateB != null) && dateC != null) {
      try {
        if(dateA.after(dateB) && dateC.after(dateA) ){
          result = true;
        }
        else{
          result = false;
        }
      }
      catch (Exception e) {}
    }
    return result;

  }

  /**
   * ��٣�����ԭ����getTimeMonth����(��Date�����е��·�ֵת�����ַ����͵��·�ֵ)���������������⣺
   * ��1����Calendar.MONTH����ֵΪ�·�ֵ��1��������0����1�·ݣ�����9����10�·ݣ�����Ӧ��ʹ��Calendar.MONTH < 9�ж��·�ֵ�Ƿ�С��10�·�
   * ��2�������ڴ��ڵ���10�·ݵĴ���û�ܽ��м�1����������11�·ݱ������10�·�
   * @param time Date
   * @throws Exception
   * @return String
   */
  public static String getTimeMonthLG(Date time){
    Calendar cd = Calendar.getInstance();
    cd.setTime(time);
    int m = cd.get(Calendar.MONTH)+ 1;
    String month = m < 10 ? "0" + m : "" + m;
    return month;
  }

}
