package com.viewscenes.util;

import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Date;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author wangyang
 * @version 1.0
 */

public class LongCalendar {

  /**
   * ���ƣ���õ�ǰ���������ڷ���
   * ���ܣ����ϵͳ��ʹ�ó����α�ʾ������
   * ���������
   * ���ز�����long:��ʾ�����ڳ�����ֵ
   */

  public static long getLongCalendar() {
    try {
      long longCalendar = 0;
      //��õ�ǰ����
      Calendar cldCurrent = Calendar.getInstance();
      //���������
      String strYear  = String.valueOf(cldCurrent.get(Calendar.YEAR));
      String strMonth = String.valueOf(cldCurrent.get(Calendar.MONTH));
      String strDate  = String.valueOf(cldCurrent.get(Calendar.DATE));
      //�����ʽ
      if (strMonth.length() < 2) {
        strMonth = "0" + strMonth;
      }
      if (strDate.length() < 2) {
        strDate = "0" + strDate;
      }

      //��Ͻ��
      longCalendar = Long.parseLong(strYear + strMonth + strDate);

    //ϵͳĬ���·ݼ�һ
      longCalendar+=100L;

      //�����ϳ�ʼ�������Ļ���������
      return longCalendar;
    } catch(Exception Exp) {
      return 0;
    }
  }

  public static long getLongTime() {
    try {
      long longCalendar = 0;

      //��õ�ǰ����
      Calendar cldCurrent = Calendar.getInstance();

      //���������
      String strYear  = String.valueOf(cldCurrent.get(Calendar.YEAR));
      String strMonth = String.valueOf(cldCurrent.get(Calendar.MONTH)+1);
      String strDate  = String.valueOf(cldCurrent.get(Calendar.DATE));
      String strHour  = String.valueOf(cldCurrent.get(Calendar.HOUR));
      String strAM_PM = String.valueOf(cldCurrent.get(Calendar.AM_PM));
      String strMinute= String.valueOf(cldCurrent.get(Calendar.MINUTE));
      String strSecond= String.valueOf(cldCurrent.get(Calendar.SECOND));

          //��ʱ��ת��Ϊ24Сʱ��
      //strAM_PM=="1",��ʾ��ǰʱ�������磬����strHour��Ҫ��12
      if(strAM_PM.equals("1")){
        strHour=String.valueOf(Long.parseLong(strHour)+12);
      }

          //�����ʽ
      if (strMonth.length() < 2) {
        strMonth = "0" + strMonth;
      }
      if (strDate.length() < 2) {
        strDate = "0" + strDate;
      }
      if (strHour.length() < 2) {
        strHour = "0" + strHour;
      }
      if (strMinute.length() < 2) {
        strMinute = "0" + strMinute;
      }
      if (strSecond.length() < 2) {
        strSecond = "0" + strSecond;
      }
      //��Ͻ��
      longCalendar = Long.parseLong(strYear + strMonth + strDate + strHour + strMinute + strSecond );

      //�����ϳ�ʼ�������Ļ���������
      return longCalendar;
    } catch(Exception Exp) {
      return 0;
    }
  }

  /**
   * ���ƣ�getCurrentDateTime
   * ���ܣ��õ�ϵͳ��ǰʱ����ʾ����ʽ��yyyymmddhhmmss
   * ���������String:ϵͳ��ǰ����(��ʽ��yyyymmdd)
   * ���ز�����long:ϵͳ��ǰ����ʱ��
   */

  public static String getCurrentDateTimeByParaT(int intDate){
    String strCalendar = "";
    //��õ�ǰ����
    Calendar cldCurrent = Calendar.getInstance();

    //���������
    String strYear  = String.valueOf(cldCurrent.get(Calendar.YEAR));
    String strMonth = String.valueOf(cldCurrent.get(Calendar.MONTH)+1);
    cldCurrent.set(Calendar.DATE,Calendar.DATE+intDate);
    String strDate  = String.valueOf(cldCurrent.DATE);

    String strHour  = String.valueOf(cldCurrent.get(Calendar.HOUR));
    String strAM_PM = String.valueOf(cldCurrent.get(Calendar.AM_PM));
    String strMinute= String.valueOf(cldCurrent.get(Calendar.MINUTE));
    String strSecond= String.valueOf(cldCurrent.get(Calendar.SECOND));

        //��ʱ��ת��Ϊ24Сʱ��
    //strAM_PM=="1",��ʾ��ǰʱ�������磬����strHour��Ҫ��12
    if(strAM_PM.equals("1")){
      strHour=String.valueOf(Long.parseLong(strHour)+12);
    }

    //�����ʽ
    if (strMonth.length() < 2) {
      strMonth = "0" + strMonth;
    }
    if (strDate.length() < 2) {
      strDate = "0" + strDate;
    }
    if (strHour.length() < 2) {
      strHour = "0" + strHour;
    }
    if (strMinute.length() < 2) {
      strMinute = "0" + strMinute;
    }
    if (strSecond.length() < 2) {
      strSecond = "0" + strSecond;
    }
    //��Ͻ��
    strCalendar = strYear +"-"+ strMonth +"-"+ strDate +" "+ strHour +":"+ strMinute +":"+ strSecond ;


    return strCalendar;
  }

  public static String getCurrentDateTimeByPara(int intDate){
          GregorianCalendar worldTour = new GregorianCalendar();
          worldTour.add(GregorianCalendar.DATE, intDate);
          Date d = worldTour.getTime();
          SimpleDateFormat df=new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
          return df.format(d);
      }

  public static void main(String [] args){
    //��õ�ǰ���ڣ�������
    System.out.println(LongCalendar.getLongCalendar());
    //��õ�ǰʱ�䣬������ʱ����
    System.out.println(LongCalendar.getLongTime());
    //���ݲ�����õ�ǰ����ʱ����������ʱ��
    System.out.println(LongCalendar.getCurrentDateTimeByPara(-2));
  }

}
