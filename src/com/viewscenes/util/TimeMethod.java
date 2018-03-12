package com.viewscenes.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.viewscenes.util.business.runplan.TimeSpan;
import com.viewscenes.web.Daoutil.StringDateUtil;



/**
 * <p>Title:TimeMethod</p>
 * <p>Description: �й�ʱ���һЩ������</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author zr
 * @version 1.0
 */
public class TimeMethod {
  public TimeMethod() {
  }

  public final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
  public final static String TIME_FORMAT = "HH:mm:ss";

  /**
   * ����һ����ʾʱ��String��Сʱ��ֵΪint������13:15��������13
   * @param
   * @return
   */
  public static int get_hour(String time) {
    int hour = (new Integer(time.substring(0, time.indexOf(":")))).intValue();
    return hour;
  }

  /**
   * �Ƚ���������ʱ��(00:00:00)�ַ����Ĵ�С,�����һ�����ڵڶ����ͷ���true������false
   * ���ȼ������ʱ����ַ����Ƿ�Ϸ�����һ�����Ϸ��ľͷ���false
   * @param
   * @return
   */
  public static boolean compare_time(String[] time) {
    //false������25��00��00 \u2014\u2014 01��00��00  ����������ĸ����Ч�ַ����ͷ���null ��
    //true  �������25��00��00������������������ĸ����Ч�ַ����ͷ���null ��
    String start_time = null;
    String end_time = null;
    try {
      start_time = TimeSpan.checkParse(time[0], false);
      end_time = TimeSpan.checkParse(time[1], false);
      if ( (start_time == null) || (end_time == null)) {
	return false;
      }
      long start = TimeSpan.parse(start_time, false);
      long end = TimeSpan.parse(end_time, false);
      //��Ϊ�п�����������������ʱ���в�������ֹʱ��Ĵ�С����
      //if (start>=end) return false;
    }
    catch (Exception e) {}
    time[0] = start_time;
    time[1] = end_time;
    return true;
  }

  /**
   * �Ƚ���������ʱ��(00:00:00)�ַ����Ĵ�С,�����һ�����ڵڶ����ͷ���true������false
   * ���ȼ������ʱ����ַ����Ƿ�Ϸ�����һ�����Ϸ��ľͷ���false
   * @param
   * @return true ��ʼʱ����ڵ��ڽ���ʱ�䣻���򷵻�false
   */
  public static boolean compare_time(String start_time, String end_time) {
    //����25��00��00 >>> 01��00��00  ����������ĸ����Ч�ַ����ͻὫ��Ӧλ���Ժ��λ��Ϊ0��
    //�����ʽ��ȫ�������Ϊ��0
    long start = 0;
    long end = 0;
    try {
      start = TimeSpan.parse(start_time);
      end = TimeSpan.parse(end_time);
    }
    catch (Exception e) {}
    if (start >= end) {
      return true;
    }
    return false;
  }
  /**
 * @throws Exception 
   * ************************************************
  
  * @Title: getTimeArrayFromStartToEnd
  
  * @Description: TODO(����ʱ��εõ�����������������ʱ��)
  
  * @param @param start_time
  * @param @param end_time
  * @param @return    �趨�ļ�
  
  * @author  ����
  
  * @return boolean    ��������
  
  * @throws
  
  ************************************************
   */
  public static String[] getTimeArrayFromStartToEnd(String start_time, String end_time,String SimpleDateFormat) throws Exception
  {
	  if(compare_date(start_time,end_time))
	  {
		  Date start=StringDateUtil.string2Date(start_time,SimpleDateFormat);
		  Date end=StringDateUtil.string2Date(end_time,SimpleDateFormat);
		  int days=(int)((end.getTime()-start.getTime())/86400000l)+1;
		  String[] times=new String[days];
		  int i=0;
		  Calendar calendar=new GregorianCalendar();
		  while(end.after(start))
		  {
			  times[i]= StringDateUtil.date2String(start,SimpleDateFormat);
			  calendar.setTime(start);
			  calendar.add(calendar.DATE, 1);
			  start=calendar.getTime();
			  i++;
		  }
		  times[i]= StringDateUtil.date2String(end,SimpleDateFormat) ;
		  return times;  
	  }
	  else
	  {
		  return null;
	  }
  }
  /**
   * �������ʱ��(00:00:00)�ַ����ĸ�ʽ�Ƿ���ȷ
   * @param
   * @return boolean true:yes  false:no
   */
  public static boolean checkParse(String time) {
    String a = TimeSpan.checkParse(time, false);
    if (a == null) {
      return false;
    }
    return true;
  }

  /**
   * �Ƚ�����date(2004-06-02 13:15:24 )�ַ����Ĵ�С,�����һ�����ڵڶ����ͷ���true������false
   * ��������������У����������������ڵĸ�ʽ�����⣬��ôDateA��DateB������null������Ҳ������trueֵ
   * @param dateA ��ʼʱ��
   * @param dateB ����ʱ��
   * @return
   */
  public static boolean compare_date(String dateA, String dateB) {
    boolean result = true;
    Date DateA = stringToDate(dateA);
    Date DateB = stringToDate(dateB);
    if ( (DateA != null) && (DateB != null)) {
      try {
	result = DateA.after(DateB);
      }
      catch (Exception e) {}
    }
    return result;
  }

  /**
   * ��StringתΪDate
   * @param strDate ʱ���ַ���
   * @return ����
   */
  public static Date stringToDate(String strDate) {
    try {
      SimpleDateFormat d = new SimpleDateFormat(DATE_FORMAT);
      ParsePosition pos = new ParsePosition(0);
      Date currentTime = d.parse(strDate, pos);
      return currentTime;
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * ������ڵ�����
   * @param
   * @return
   */
  public static String get_nowdate() {
    Calendar now = Calendar.getInstance();
    java.util.Date now_time = now.getTime();
    SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT,
	Locale.getDefault());
    String now_datetime = formatter.format(now_time);
    return now_datetime;
  }
  /**
   * ���ݿ�ʼʱ�����ʱ������ܼ���Ӧ���������ڡ�
   * @weekday  	all	ÿ��
					0	����
					1	��һ
					2	�ܶ�
					3	����
					4	����
					5	����
					6	����
   * @return String[]
   */
	public static ArrayList getAllDataWeekdayFromStartTimeAndEndtime(String starttime,String endtime ,String weekday) {
	  ArrayList alltime=new ArrayList();
	Date startDate=StringTool.stringToDate(starttime);
	Date endDate=StringTool.stringToDate(endtime);
	Calendar cal=Calendar.getInstance();
	cal.setTime(startDate);
    String week= (cal.get(Calendar.DAY_OF_WEEK)-1)+"";
    if(weekday.equals("all"))
    {
    	
    }
	  if(week.compareTo(weekday)<0)//��ǰ����С��ѭ���Ļ����ܿ���ִ��һ��
	    {
	    	startDate.setDate(startDate.getDate()+(Integer.parseInt(weekday)-Integer.parseInt(week)));
	    	alltime.add(StringTool.Date2String(startDate).substring(0,11));
	    }else
	    {
	    	startDate.setDate(startDate.getDate()+(Integer.parseInt(weekday)-Integer.parseInt(week)+7));
	    	alltime.add(StringTool.Date2String(startDate).substring(0,11));	
	    }
    while(startDate.getTime()<=endDate.getTime())
    {
    	startDate.setDate(startDate.getDate()+7);
    	if(startDate.getTime()<=endDate.getTime())
    	{
    		alltime.add(StringTool.Date2String(startDate).substring(0,11));		
    	}
    		
   }

    return alltime;
  }
  /**
   * ���ݿ�ʼʱ�����ʱ��,����ʼʱ�����ʱ����㵥Ԫ30����Ϊһ��Ԫ��
   * @weekday  	all	ÿ��
					0	����
					1	��һ
					2	�ܶ�
					3	����
					4	����
					5	����
					6	����
   * @return String[]
   */
  public static ArrayList getUnitTime30(String startdate,String enddate,String starttime,String endtime ,String weekday) {
	  ArrayList alltime=new ArrayList();
	    ArrayList list=getAllDataWeekdayFromStartTimeAndEndtime(startdate,enddate,weekday);  
	    String tempDate="";
	    String tempTime="";
	    ArrayList hourList=new ArrayList();
	   String[] split= starttime.split(":");
	    int shour=Integer.parseInt(split[0]);
	    int sminutes=Integer.parseInt(split[1]);
	    int ssecond=Integer.parseInt(split[2]);
	    String[] eplit= endtime.split(":");
	    int ehour=Integer.parseInt(eplit[0]);
	    int dminutes=Integer.parseInt(eplit[1]);
	    int dsecond=Integer.parseInt(eplit[2]);
	    if(sminutes>=30&&sminutes<59)
	    {
	    	hourList.add(shour+":00:00");
	    }else
	    {
	    	
	    	hourList.add(shour+":00:00");
	    	if(shour!=ehour)
	    	{
	    	hourList.add(shour+":30:00");
	    	}
	    }
	    shour++;
	    while(shour<ehour)
	    {
	    	hourList.add(shour+":00:00");
	    	hourList.add(shour+":30:00");
	    	shour++;
	    }
	    
	    if(shour==ehour){
	    if(dminutes>=30&&dminutes<59)
	    {
	    	hourList.add(ehour+":00:00");
	    	hourList.add(ehour+":30:00");
	    }else if(dminutes>0&&dsecond>0)
	    {
	    	hourList.add(ehour+":00:00");
	    }
	    }
	    for(int i=0;i<list.size();i++)
	    {
	    	 tempDate=(String) list.get(i);
	    	 for(int k=0;k<hourList.size();k++)
	    	 {
	    		
	    		 tempTime=(String) hourList.get(k);
	    		
	    		 alltime.add(tempDate+" "+tempTime);
	    	 }
	    	 
	    }

    return alltime;
  }

  public static void main(String[] args)
  {
	 
	 try {
		String[] times= TimeMethod.getTimeArrayFromStartToEnd("2011-12-21", "2012-01-12","yyyy-MM-dd");
		for(int i=0;i<times.length;i++)
		{
			
			System.out.println(times[i]+"="+times[i].substring(8,times[i].length()));
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		    
  }
}