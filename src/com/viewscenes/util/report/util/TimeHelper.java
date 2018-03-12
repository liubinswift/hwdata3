package com.viewscenes.util.report.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.viewscenes.util.StringTool;

/**
 * ʱ�䴦����
 * ʱ�䣺2009-06-10
 *
 */
public class TimeHelper {

    /**
     * ʱ����ƫ�������˴��趨Ϊ�й����ڵ�ʱ������������
     */
    private final String timeZoneOffSet = "GMT+08:00";
    public final static long oneDayValue=24*60*60*1000;
    public final static long oneHourValue=60*60*1000;

    public final static String TimeShowSpliter="��";                   //����֮�����ʾ�õķָ���
    public final static String TimeHiddenSpliter="~";                  //����֮��Ĵ����õķָ���

    public final static int TimeInLong$MicroSecond=8;                  //1143120714484000   ΢��
    public final static int TimeInLong$MilliSecond=0;                  //1143120714484      ����
    public final static int TimeInLong$Second=1;                       //1143120714         ��
    public final static int TimeInLong$YYYYMMDDHHMMSS=2;               //20060323213154
    public final static int TimeInLong$YYYYMMDDHHMM=3;                 //200603232131
    public final static int TimeInLong$YYYYMMDDHH=4;                   //2006032321
    public final static int TimeInLong$YYYYMMDD=5;                     //20060323
    public final static int TimeInLong$YYYYMM=6;                       //200603
    public final static int TimeInLong$YYYY=7;                         //2006



    public final static int TimeInString$YYYYcnMMcnDDcnHHcnMMcnSScn=20;  //2006��03��08��09ʱ09��09��
    public final static int TimeInString$YYYYcnmMcndDcnhHcnmMcnsScn=21;  //2006��3��8��9ʱ9��9��
    public final static int TimeInString$YYYYcnMMcnDDcnHHcnMMcn=22;      //2006��03��08��09ʱ09��
    public final static int TimeInString$YYYYcnmMcndDcnhHcnmMcn=23;      //2006��3��8��9ʱ9��
    public final static int TimeInString$YYYYcnMMcnDDcnHHcn=24;          //2006��03��08��09ʱ
    public final static int TimeInString$YYYYcnmMcndDcnhHcn=25;          //2006��3��8��9ʱ
    public final static int TimeInString$YYYYcnMMcnDDcn=26;              //2006��03��08��
    public final static int TimeInString$YYYYcnmMcndDcn=27;              //2006��3��8��
    public final static int TimeInString$YYYYcnMMcn=28;                  //2006��03��
    public final static int TimeInString$YYYYcnmMcn=29;                  //2006��3��
    public final static int TimeInString$YYYYcn=30;                      //2006��

    public final static int TimeInString$YYYY_MM_DD_HH_MM_SS=40;         //2006-03-08 09:06:28
    public final static int TimeInString$YYYY_MM_DD_HH_MM_SS_MS=52;      //2006-03-08 09:06:28:109
    public final static int TimeInString$YYYY_mM_dD_hH_mM_sS=41;         //2006-3-8 9:06:28
    public final static int TimeInString$YYYY_MM_DD_HH_MM=42;            //2006-03-08 09:06
    public final static int TimeInString$YYYY_mM_dD_hH_mM=43;            //2006-3-8 9:06
    public final static int TimeInString$YYYY_MM_DD_HH=44;               //2006-03-08 09
    public final static int TimeInString$YYYY_mM_dD_hH=45;               //2006-3-8 9
    public final static int TimeInString$YYYY_MM_DD=46;                  //2006-03-08
    public final static int TimeInString$YYYY_mM_dD=47;                  //2006-3-8
    public final static int TimeInString$YYYY_MM=48;                     //2006-03
    public final static int TimeInString$YYYY_mM=49;                     //2006-3
    public final static int TimeInString$YYYY=50;                        //2006
    public final static int TimeInString$CYYMMDDHHMMSSTTT=51;            //1060312123456000

    public final static int TimeUnitInt$YEAR=60;                    //2006
    public final static int TimeUnitInt$MONTH=61;                   //11
    public final static int TimeUnitInt$DATE=62;                    //8
    public final static int TimeUnitInt$HOUR=63;                    //23
    public final static int TimeUnitInt$MINUTE=64;                  //56
    public final static int TimeUnitInt$SECOND=65;                  //59


//    private Calendar startDate=Calendar.getInstance();
//    private Calendar endDate=Calendar.getInstance();

    private Calendar MyDate=Calendar.getInstance();   //�Լ������ʱ�䣬���д���

    private int startHourOffset=0;   //��ʼ���ڵ�Сʱƫ����  �����±���Сʱƫ����(��ʼ�ͽ���)��ΪstartHourOffset
    private int endHourOffset=0;     //�������ڵ�Сʱƫ����

    //    private String dateFromToStr="";
    private String timeTypeStr="";

    /**
     * ������
     */
    private GregorianCalendar calender;

    /**
     * ���캯����
     * @deprecated
     */
    public TimeHelper(Date date){
        this.calender=new GregorianCalendar();
        this.calender.setTime(date);
        this.buildTimeZone();
        MyDate.setTime(date);
    }
    /**
     * ���캯����
     */
    public TimeHelper(String timeStr){
        this.calender=new GregorianCalendar();
        try {
            if (timeStr != null) {
                if(StringTool.isNumeric(timeStr.replaceAll("/", "").replaceAll(" ", "")
                        .replaceAll(":", "").replaceAll("-", "").replaceAll("��", "").replaceAll("��", "")
                        .replaceAll("��", "").replaceAll("��", "").replaceAll("��", "").replaceAll("��", "")
                        .replaceAll("ʱ", "").replace(".", ""))){
                    if (timeStr.length() == 10) {
                        if (timeStr.indexOf("-") > 0) {
                            ConTimeHelper(timeStr, DateStyle.TO_DAY);
                        } else if (timeStr.indexOf("/") > 0) {
                            ConTimeHelper(timeStr, DateStyle.ORACLE_DAY);
                        }
                    } else if (timeStr.length() == 11) {
                        ConTimeHelper(timeStr, DateStyle.lOCAL_TO_DAY);
                    } else if (timeStr.length() == 19 && (timeStr.indexOf("/")>=3||timeStr.indexOf("-") > 3)) {
                        if (timeStr.indexOf("-") > 0) {
                            ConTimeHelper(timeStr, DateStyle.TO_SECOND);
                        } else if (timeStr.indexOf("/") > 0) {
                            ConTimeHelper(timeStr, DateStyle.ORACLE_SECOND);
                        }
                    } else if (timeStr.length() == 21&&timeStr.indexOf("��")>0) {
                        ConTimeHelper(timeStr, DateStyle.lOCAL_TO_SECOND);
                    }else{
                        if (timeStr.indexOf("/") > 0) {
                            String[] sdate = timeStr.substring(0,timeStr.indexOf(" ")).split("/");
                            String[] stime = null;
                            if (timeStr.indexOf(" ") > 0) {
//							timeStr = timeStr.substring(0, timeStr.indexOf(" "));
                                stime = timeStr.substring(
                                        timeStr.indexOf(" ") + 1,timeStr.indexOf(".0")>0?timeStr.indexOf(".0"):timeStr.length()).split(":");
                                if (stime.length == 1) {
                                    stime = new String[3];
                                    stime[0]="0";
                                    stime[1]="0";
                                    stime[2]="0";
                                }
                            }
                            if (sdate[0].length() == 4) {
                                MyDate.set(Integer.parseInt(sdate[0]), Integer
                                        .parseInt(sdate[1]) - 1, Integer
                                        .parseInt(sdate[2]), Integer
                                        .parseInt(stime[0]), Integer
                                        .parseInt(stime[1]), Integer
                                        .parseInt(stime[2]));
                                this.calender.setTimeInMillis(MyDate
                                        .getTimeInMillis());
                            } else if (sdate[2].length() == 4) {
                                MyDate.set(Integer.parseInt(sdate[2]) , Integer
                                        .parseInt(sdate[0])- 1, Integer
                                        .parseInt(sdate[1]), Integer
                                        .parseInt(stime[0]), Integer
                                        .parseInt(stime[1]), Integer
                                        .parseInt(stime[2]));
                                this.calender.setTimeInMillis(MyDate
                                        .getTimeInMillis());
                            }
                            this.buildTimeZone();
                        }
                    }
                }}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * ���캯����
     *
     * @param timeStr
     *            ʱ���ַ�����
     * @param pattern
     *            ʱ���ַ�������Ӧ�ĸ�ʽ������2004-08-04 12:00:00 ��Ӧyyyy-MM-dd hh-mm-ss;
     *            2004��08��04�� 12��00��00�� ��Ӧyyyy��MM��dd�� hh��mm��ss��; 20040804120000
     *            ��ӦyyyyMMddhhmmss; ʾ����PortalDate date=new PortalDate(
     *            "2004-08-04 12:00:00","yyyy-MM-dd hh-mm-ss");
     *            date.toLocStdDateString();//�õ�2004��08��04�� 12��00��00��
     * @throws ParseException
     * @deprecated
     *
     */
    public TimeHelper(String timeStr, String pattern) throws Exception {
        ConTimeHelper(timeStr,pattern);
    }

    public TimeHelper(long ltime){
        this.calender=new GregorianCalendar();
        this.calender.setTimeInMillis(ltime);
        this.buildTimeZone();
        MyDate.setTimeInMillis(ltime);
    }
    public TimeHelper(){
        this.calender=new GregorianCalendar();
    }
    private void ConTimeHelper(String timeStr, String pattern) throws Exception {
        DateFormat df = new SimpleDateFormat(pattern);
        df.parse(timeStr);
        this.calender = (GregorianCalendar) df.getCalendar();
        this.buildTimeZone();
        MyDate.setTimeInMillis(this.calender.getTimeInMillis());

    }
    /**
     * ��ȡ��ǰʱ��
     * ʱ��:2006-03-24
     *
     * @return String
     */
    public String getNow(int TimeFormat) {
        Calendar nowdate=Calendar.getInstance();
        nowdate.setTimeInMillis(System.currentTimeMillis());
        return getTime(nowdate,TimeFormat);
    }
    /**
     * ��ȡ����0���ʱ��ֵ
     * ʱ��:2006-04-20
     *
     * @return String
     */
    public String getTodayZeroHour(int TimeFormat) {
//    	Calendar nowdate=Calendar.getInstance();
//    	nowdate.setTimeInMillis(System.currentTimeMillis());
//    	setMyDate(System.currentTimeMillis()+"",TimeInLong$MilliSecond);
        MyDate.clear();
        MyDate.setTimeInMillis(System.currentTimeMillis());
        String today=getMyDate(TimeInString$YYYY_MM_DD);
        setMyDate(today,TimeInString$YYYY_MM_DD);
        return getMyDate(TimeFormat);
    }
    /**
     * ��ȡ���ܵ�һ��0���ʱ��ֵ
     * ʱ��:2006-04-20
     *
     * @return String
     */
    public String getSundayZeroHour(int TimeFormat) {
        setMyDate(System.currentTimeMillis()+"",TimeInLong$MilliSecond);
        MyDate.clear();
        MyDate.setTimeInMillis(System.currentTimeMillis());

        int isweek=MyDate.get(Calendar.DAY_OF_WEEK)-1;
        long longWeek=MyDate.getTimeInMillis()-isweek*TimeHelper.oneDayValue;

        MyDate.clear();
        MyDate.setTimeInMillis(longWeek);

        String strweek=getMyDate(TimeInString$YYYY_MM_DD);
        setMyDate(strweek,TimeInString$YYYY_MM_DD);
        return getMyDate(TimeFormat);
    }
    /**
     * ��ȡtimehelper�ڲ������ı��ܵ�һ���ʱ��ֵ
     */
    public String getWeekZeroTime(int TimeFormat) {
        int isweek=getWeekOfDate()-1;
        long longWeek=MyDate.getTimeInMillis()-isweek*TimeHelper.oneDayValue;

        MyDate.clear();
        MyDate.setTimeInMillis(longWeek);

        String strweek=getMyDate(TimeInString$YYYY_MM_DD);
        setMyDate(strweek,TimeInString$YYYY_MM_DD);
        return getMyDate(TimeFormat);
    }
    /**
     * ��ȡtimehelper�ڲ������ı��ܵ������ʱ��ֵ
     */
    public String getWeekSunTime(int TimeFormat) {
        int isweek=getWeekOfDate()-1;
        long longWeek=MyDate.getTimeInMillis()-isweek*TimeHelper.oneDayValue
                +6*TimeHelper.oneDayValue;

        MyDate.clear();
        MyDate.setTimeInMillis(longWeek);

        String strweek=getMyDate(TimeInString$YYYY_MM_DD);
        setMyDate(strweek,TimeInString$YYYY_MM_DD);
        return getMyDate(TimeFormat);
    }
    /**
     * ��ȡ���µ�һ��0���ʱ��ֵ
     * ʱ��:2006-04-20
     *
     * @return String
     */
    public String getMonthZeroHour(int TimeFormat) {
        MyDate.clear();
        MyDate.setTimeInMillis(System.currentTimeMillis());

        int month=MyDate.get(Calendar.MONTH);
        int year=MyDate.get(Calendar.YEAR);
        MyDate.clear();
        MyDate.set(year, month, 1);
        return getMyDate(TimeFormat);
    }
    /**
     * ��ȡ���µ�һ��0���ʱ��ֵ
     * ʱ��:2006-04-20
     *
     * @return String
     */
    public String getNextMonthZeroHour(String timeMilliSecond,int TimeFormat) {
        MyDate.clear();
        MyDate.setTimeInMillis(Long.parseLong(timeMilliSecond));

        int month=MyDate.get(Calendar.MONTH);
        int year=MyDate.get(Calendar.YEAR);

        if(month>=11){
            year=MyDate.get(Calendar.YEAR)+1;
            month=-1;
        }
        MyDate.clear();
        MyDate.set(year, month+1, 1);

        return getMyDate(TimeFormat);
    }
    /**
     * ��ȡ��ǰ�µ����µ�һ��0���ʱ��ֵ
     * ʱ��:2006-04-20
     *
     * @return String
     */
    public String getLastMonthZeroHour(int TimeFormat) {
//    	Calendar nowdate=Calendar.getInstance();
//    	nowdate.setTimeInMillis(System.currentTimeMillis());
//    	setMyDate(System.currentTimeMillis()+"",TimeInLong$MilliSecond);
        MyDate.clear();
        MyDate.setTimeInMillis(System.currentTimeMillis());
//    	MyDate.set(Calendar.DAY_OF_MONTH,1);


        int month=MyDate.get(Calendar.MONTH);
        int year=MyDate.get(Calendar.YEAR);
//    	end.set(year, month, 1);

        if(month<1){
            year=MyDate.get(Calendar.YEAR)-1;
            month=12;
        }
        MyDate.clear();
        MyDate.set(year, month-1, 1);

        return getMyDate(TimeFormat);
    }
    /**
     * Ϊʱ��ؼ�����startDate��ǰһ���ʱ��
     * ʱ��:2006-03-24
     *
     * @return String
     */
    public String getLastDay() {
        Calendar start=Calendar.getInstance();
        start.setTimeInMillis(MyDate.getTimeInMillis()-oneDayValue);

        return getStringTime(start,TimeInString$YYYY_MM_DD);
    }
    /**
     * Ϊʱ��ؼ�����endDate����һ���ʱ��
     * ʱ��:2006-03-24
     *
     * @return String
     */
    public String getNextDay() {
        Calendar start=Calendar.getInstance();
        start.setTimeInMillis(MyDate.getTimeInMillis()+oneDayValue);

        return getStringTime(start,TimeInString$YYYY_MM_DD);
    }
    /**
     * Ϊʱ��ؼ�����endDate��ĳһ���ʱ��
     * ʱ��:2006-03-24
     * @param intDay ����
     * @return String
     */
    public String getDay(int intDay) {
        Calendar start=Calendar.getInstance();
        start.setTimeInMillis(MyDate.getTimeInMillis()+intDay*oneDayValue);

        return getStringTime(start,TimeInString$YYYY_MM_DD);
    }
    /**
     * Ϊʱ��ؼ�����startDate��ǰ�����ʱ��
     * ʱ��:2007-04-10
     *
     * @return String
     */
    public String getBeforeYesderday() {
        Calendar start=Calendar.getInstance();
        start.setTimeInMillis(MyDate.getTimeInMillis()-oneDayValue*2);

        return getStringTime(start,TimeInString$YYYY_MM_DD);
    }
    /**
     * Ϊʱ��ؼ�����startDate�ĸ����ʱ��
     * ʱ��:2006-03-24
     *
     * @return String
     */
    public String getThisDay() {
        Calendar start=Calendar.getInstance();
        start.setTimeInMillis(this.calender.getTimeInMillis());
        return getStringTime(start,TimeInString$YYYY_MM_DD);
    }
    /**
     * ��ȡָ��ʱ��timeMilliSecond��ǰһ������ʱ��
     * ʱ��:2006-04-27
     *
     * @return String
     */
    public static long getLastDay(String timeMilliSecond) {
        Calendar rtnDate=Calendar.getInstance();
        rtnDate.setTimeInMillis(Long.parseLong(timeMilliSecond)-oneDayValue);
        rtnDate.set(Calendar.HOUR_OF_DAY,0);
        rtnDate.set(Calendar.MINUTE,0);
        rtnDate.set(Calendar.SECOND,0);
        rtnDate.set(Calendar.MILLISECOND,0);

//    	Calendar rtnDate=Calendar.getInstance();
//    	rtnDate.set(tempDate.get(Calendar.YEAR),tempDate.get(Calendar.MONTH),tempDate.get(Calendar.DAY_OF_MONTH));

        return rtnDate.getTimeInMillis();
    }
    /**
     * ��ȡָ��ʱ��timeMilliSecond��ǰһ�ܵ����ʱ��
     * ʱ��:2006-04-27
     *
     * @return String
     */
    public static long getLastWeek(String timeMilliSecond) {
        Calendar rtnDate=Calendar.getInstance();
        rtnDate.setTimeInMillis(Long.parseLong(timeMilliSecond)-7*oneDayValue);
        rtnDate.set(Calendar.HOUR_OF_DAY,0);
        rtnDate.set(Calendar.MINUTE,0);
        rtnDate.set(Calendar.SECOND,0);
        rtnDate.set(Calendar.MILLISECOND,0);

        int firstday=rtnDate.getFirstDayOfWeek();
        int dis=rtnDate.get(Calendar.DAY_OF_WEEK)-firstday;

//    	Calendar rtnDate=Calendar.getInstance();
//    	rtnDate.set(tempDate.get(Calendar.YEAR),tempDate.get(Calendar.MONTH),tempDate.get(Calendar.DAY_OF_MONTH));

        return rtnDate.getTimeInMillis()-dis*oneDayValue;
    }
    /**
     * ��ȡָ��ʱ��timeMilliSecond��ǰһ�µ����ʱ��
     * ʱ��:2006-04-27
     *
     * @return String
     */
    public static long getLastMonth(String timeMilliSecond) {
        Calendar rtnDate=Calendar.getInstance();
        rtnDate.setTimeInMillis(Long.parseLong(timeMilliSecond));
//    	rtnDate.set(Calendar.DAY_OF_MONTH,1);
//    	rtnDate.set(Calendar.HOUR_OF_DAY,0);
//    	rtnDate.set(Calendar.MINUTE,0);
//    	rtnDate.set(Calendar.SECOND,0);
//    	rtnDate.set(Calendar.MILLISECOND,0);

        int month=rtnDate.get(Calendar.MONTH);
        int year=rtnDate.get(Calendar.YEAR);
//    	end.set(year, month, 1);

        if(month<1){
            year=rtnDate.get(Calendar.YEAR)-1;
            month=12;
        }
        rtnDate.clear();
        rtnDate.set(year, month-1, 1);

//    	int firstday=rtnDate.getFirstDayOfWeek();
//    	int dis=rtnDate.get(Calendar.DAY_OF_WEEK)-firstday;

//    	Calendar rtnDate=Calendar.getInstance();
//    	rtnDate.set(tempDate.get(Calendar.YEAR),tempDate.get(Calendar.MONTH),tempDate.get(Calendar.DAY_OF_MONTH));

        return rtnDate.getTimeInMillis();
    }

    /**
     * Ϊʱ��ؼ�����startDate��ǰһ�ܵ�ʱ��
     * ʱ��:2006-03-24
     *
     * @return String
     */
    public String getLastWeek() {
        Calendar start=Calendar.getInstance();
        int dayofweek=MyDate.get(Calendar.DAY_OF_WEEK);

        start.setTimeInMillis(MyDate.getTimeInMillis()-(dayofweek+6)*oneDayValue);

        return getStringTime(start,TimeInString$YYYY_MM_DD);
    }
    /**
     * Ϊʱ��ؼ�����startDate��ǰ���ܵ�ʱ��
     * ʱ��:2007-02-06
     * @return String
     */
    public String getBeforeWeek() {
        Calendar start=Calendar.getInstance();
        int dayofweek=MyDate.get(Calendar.DAY_OF_WEEK);
        start.setTimeInMillis(MyDate.getTimeInMillis()-oneDayValue*(dayofweek+13));

        return getStringTime(start,TimeInString$YYYY_MM_DD);
    }
    /**
     * Ϊʱ��ؼ�����endDate����һ�ܵ�ʱ��
     * ʱ��:2006-03-24
     *
     * @return String
     */
    public String getNextWeek() {
        Calendar start=Calendar.getInstance();
        int dayofweek=MyDate.get(Calendar.DAY_OF_WEEK);

        start.setTimeInMillis(MyDate.getTimeInMillis()+(1-dayofweek)*oneDayValue);

        return getStringTime(start,TimeInString$YYYY_MM_DD);
    }
    /**
     * Ϊʱ��ؼ�����startDate����һ�µ�ʱ��
     * ʱ��:2006-03-24
     *
     * @return String
     */
    public String getLastMonth() {
        Calendar start=Calendar.getInstance();
        int month=MyDate.get(Calendar.MONTH);
        int year=MyDate.get(Calendar.YEAR);

        if(month<1){
            year=MyDate.get(Calendar.YEAR)-1;
            month=12;
        }
        start.set(year, month-1, 1);


        return getStringTime(start,TimeInString$YYYY_MM_DD);
    }
    /**
     * Ϊʱ��ؼ�����startDate��ǰһ���ʱ��
     * ʱ��:2006-03-24
     *
     * @return String
     */
    public String getLastYear() {
        int year=MyDate.get(Calendar.YEAR);
        Calendar start=Calendar.getInstance();
        start.set(year-1, 0, 1);

        return getStringTime(start,TimeInString$YYYY_MM_DD);
    }

    /**
     * Ϊʱ��ؼ�����startDate����һ���ʱ��
     * ʱ��:2006-03-24
     *
     * @return String
     */
    public String getNextYear() {
        int year=MyDate.get(Calendar.YEAR);
        Calendar start=Calendar.getInstance();
        start.set(year+1, 0, 1);
        return getStringTime(start,TimeInString$YYYY_MM_DD);
    }

    /**
     * Ϊʱ��ؼ�����endDate����һ�µ�ʱ��
     * ʱ��:2006-03-24
     *
     * @return String
     */
    public String getNextMonth() {
        Calendar start=Calendar.getInstance();
        int month=MyDate.get(Calendar.MONTH);
        int year=MyDate.get(Calendar.YEAR);

        if(month>=11){
            year=MyDate.get(Calendar.YEAR)+1;
            month=-1;
        }
        start.set(year, month+1, 1);

        return getStringTime(start,TimeInString$YYYY_MM_DD);
    }


    public Calendar getMyDateWithHourOffset() {
        Calendar newDate=Calendar.getInstance();
        newDate.setTimeInMillis(MyDate.getTimeInMillis()+endHourOffset*oneHourValue);
        return newDate;
    }
    public String getMyDateWithHourOffset(int TimeFormat) {
        Calendar newDate=Calendar.getInstance();
        String millis=""+MyDate.getTimeInMillis();
        millis=millis.substring(0,10)+"000";
        newDate.setTimeInMillis(Long.parseLong(millis)+endHourOffset*oneHourValue);
        return getTime(newDate,TimeFormat);
    }

    public Calendar getMyDate() {
        return MyDate;
    }
    public void setMyDate(Calendar MyDate) {
        this.MyDate = MyDate;
    }
    public int getEndHourOffset() {
        return endHourOffset;
    }
    public void setEndHourOffset(int endHourOffset) {
        this.endHourOffset = endHourOffset;
    }
    public int getStartHourOffset() {
        return startHourOffset;
    }
    public void setStartHourOffset(int startHourOffset) {
        this.startHourOffset = startHourOffset;
    }

    public void setMyDate(String myDate, String pattern) throws Exception {
        DateFormat df = new SimpleDateFormat(pattern);
        df.parse(myDate);
        this.calender = (GregorianCalendar) df.getCalendar();
        this.buildTimeZone();
    }
    public String getMyDate(String pattern) throws Exception {
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(this.calender.getTime());
    }

    public void setMyDate(String myDate,int TimeFormat) {
        String mydate=new String(myDate);
        String[] timespace=null;
        String[] timesminus=null;
        String[] timescolon=null;
        String replacestr=null;
        switch(TimeFormat) {
            case TimeInString$CYYMMDDHHMMSSTTT:
                TimeFormat=TimeInString$YYYY_MM_DD_HH_MM_SS;
                replacestr="20"+mydate.substring(1,3);
                replacestr=replacestr+"-"+mydate.substring(3,5);
                replacestr=replacestr+"-"+mydate.substring(5,7);
                replacestr=replacestr+" "+mydate.substring(7,9);
                replacestr=replacestr+":"+mydate.substring(9,11);
                replacestr=replacestr+":"+mydate.substring(11,13);
                mydate=replacestr;
                mydate=mydate.trim();
                break;
            case TimeInString$YYYYcnMMcnDDcnHHcnMMcnSScn:
                TimeFormat=TimeInString$YYYY_MM_DD_HH_MM_SS;
                mydate=mydate.replace('��', '-');
                mydate=mydate.replace('��', '-');
                mydate=mydate.replace('��', ' ');
                mydate=mydate.replace('ʱ', ':');
                mydate=mydate.replace('��', ':');
                mydate=mydate.replace('��', ' ');
                mydate=mydate.trim();
                break;
            case TimeInString$YYYYcnmMcndDcnhHcnmMcnsScn:
                TimeFormat=TimeInString$YYYY_mM_dD_hH_mM_sS;
                mydate=mydate.replace('��', '-');
                mydate=mydate.replace('��', '-');
                mydate=mydate.replace('��', ' ');
                mydate=mydate.replace('ʱ', ':');
                mydate=mydate.replace('��', ':');
                mydate=mydate.replace('��', ' ');
                mydate=mydate.trim();
                break;
            case TimeInString$YYYYcnMMcnDDcnHHcnMMcn:
                TimeFormat=TimeInString$YYYY_MM_DD_HH_MM;
                mydate=mydate.replace('��', '-');
                mydate=mydate.replace('��', '-');
                mydate=mydate.replace('��', ' ');
                mydate=mydate.replace('ʱ', ':');
                mydate=mydate.replace('��', ' ');
                mydate=mydate.trim();
                break;
            case TimeInString$YYYYcnmMcndDcnhHcnmMcn:
                TimeFormat=TimeInString$YYYY_mM_dD_hH_mM;
                mydate=mydate.replace('��', '-');
                mydate=mydate.replace('��', '-');
                mydate=mydate.replace('��', ' ');
                mydate=mydate.replace('ʱ', ':');
                mydate=mydate.replace('��', ' ');
                mydate=mydate.trim();
                break;
            case TimeInString$YYYYcnMMcnDDcnHHcn:
                TimeFormat=TimeInString$YYYY_MM_DD_HH;
                mydate=mydate.replace('��', '-');
                mydate=mydate.replace('��', '-');
                mydate=mydate.replace('��', ' ');
                mydate=mydate.replace('ʱ', ' ');
                mydate=mydate.trim();
                break;
            case TimeInString$YYYYcnmMcndDcnhHcn:
                TimeFormat=TimeInString$YYYY_mM_dD_hH;
                mydate=mydate.replace('��', '-');
                mydate=mydate.replace('��', '-');
                mydate=mydate.replace('��', ' ');
                mydate=mydate.replace('ʱ', ' ');
                mydate=mydate.trim();
                break;
            case TimeInString$YYYYcnMMcnDDcn:
                TimeFormat=TimeInString$YYYY_MM_DD;
                mydate=mydate.replace('��', '-');
                mydate=mydate.replace('��', '-');
                mydate=mydate.replace('��', ' ');
                mydate=mydate.trim();
                break;
            case TimeInString$YYYYcnmMcndDcn:
                TimeFormat=TimeInString$YYYY_mM_dD;
                mydate=mydate.replace('��', '-');
                mydate=mydate.replace('��', '-');
                mydate=mydate.replace('��', ' ');
                mydate=mydate.trim();
                break;
            case TimeInString$YYYYcnMMcn:
                TimeFormat=TimeInString$YYYY_MM;
                mydate=mydate.replace('��', '-');
                mydate=mydate.replace('��', ' ');
                mydate=mydate.trim();
                break;
            case TimeInString$YYYYcnmMcn:
                TimeFormat=TimeInString$YYYY_mM;
                mydate=mydate.replace('��', '-');
                mydate=mydate.replace('��', ' ');
                mydate=mydate.trim();
                break;
            case TimeInString$YYYYcn:
                TimeFormat=TimeInString$YYYY;
                mydate=mydate.substring(0, 4);
                break;
        }
        MyDate.clear();
        try {
            switch(TimeFormat) {
                case TimeInLong$MilliSecond:
                    MyDate.setTimeInMillis(Long.parseLong(mydate));
                    break;
                case TimeInLong$MicroSecond:
                    MyDate.setTimeInMillis(Long.parseLong(mydate)/1000);
                    break;
                case TimeInLong$Second:
                    MyDate.setTimeInMillis(Long.parseLong(mydate)*1000);
                    break;
                case TimeInLong$YYYYMMDDHHMMSS:
                    MyDate.set(Integer.parseInt(mydate.substring(0, 4)),
                            Integer.parseInt(mydate.substring(4, 6))-1,
                            Integer.parseInt(mydate.substring(6, 8)),
                            Integer.parseInt(mydate.substring(8, 10)),
                            Integer.parseInt(mydate.substring(10, 12)),
                            Integer.parseInt(mydate.substring(12, 14)));
                    break;
                case TimeInLong$YYYYMMDDHHMM:
                    MyDate.set(Integer.parseInt(mydate.substring(0, 4)),
                            Integer.parseInt(mydate.substring(4, 6))-1,
                            Integer.parseInt(mydate.substring(6, 8)),
                            Integer.parseInt(mydate.substring(8, 10)),
                            Integer.parseInt(mydate.substring(10, 12)));
                    break;
                case TimeInLong$YYYYMMDDHH:
                    MyDate.set(Integer.parseInt(mydate.substring(0, 4)),
                            Integer.parseInt(mydate.substring(4, 6))-1,
                            Integer.parseInt(mydate.substring(6, 8)),
                            Integer.parseInt(mydate.substring(8, 10)),
                            0);
                    break;
                case TimeInLong$YYYYMMDD:
                    MyDate.set(Integer.parseInt(mydate.substring(0, 4)),
                            Integer.parseInt(mydate.substring(4, 6))-1,
                            Integer.parseInt(mydate.substring(6, 8)),
                            0,0);
                    break;
                case TimeInLong$YYYYMM:
                    MyDate.set(Integer.parseInt(mydate.substring(0, 4)),
                            Integer.parseInt(mydate.substring(4, 6))-1,
                            1);
                    break;
                case TimeInLong$YYYY:
                    MyDate.set(Integer.parseInt(mydate.substring(0, 4)),
                            0,1);
                    break;

                case TimeInString$YYYY_MM_DD_HH_MM_SS:
                    MyDate.set(Integer.parseInt(mydate.substring(0, 4)),
                            Integer.parseInt(mydate.substring(5, 7))-1,
                            Integer.parseInt(mydate.substring(8, 10)),
                            Integer.parseInt(mydate.substring(11, 13)),
                            Integer.parseInt(mydate.substring(14, 16)),
                            Integer.parseInt(mydate.substring(17, 19)));
                    break;
                case TimeInString$YYYY_mM_dD_hH_mM_sS:
                    timespace=mydate.split(" ");
                    timesminus=timespace[0].split("-");
                    timescolon=timespace[1].split(":");
                    MyDate.set(Integer.parseInt(timesminus[0]),
                            Integer.parseInt(timesminus[1])-1,
                            Integer.parseInt(timesminus[2]),
                            Integer.parseInt(timescolon[0]),
                            Integer.parseInt(timescolon[1]),
                            Integer.parseInt(timescolon[2]));
                    break;
                case TimeInString$YYYY_MM_DD_HH_MM:
                    MyDate.set(Integer.parseInt(mydate.substring(0, 4)),
                            Integer.parseInt(mydate.substring(5, 7))-1,
                            Integer.parseInt(mydate.substring(8, 10)),
                            Integer.parseInt(mydate.substring(11, 13)),
                            Integer.parseInt(mydate.substring(14, 16)));
                    break;
                case TimeInString$YYYY_mM_dD_hH_mM:
                    timespace=mydate.split(" ");
                    timesminus=timespace[0].split("-");
                    timescolon=timespace[1].split(":");
                    MyDate.set(Integer.parseInt(timesminus[0]),
                            Integer.parseInt(timesminus[1])-1,
                            Integer.parseInt(timesminus[2]),
                            Integer.parseInt(timescolon[0]),
                            Integer.parseInt(timescolon[1]));
                    break;
                case TimeInString$YYYY_MM_DD_HH:
                    MyDate.set(Integer.parseInt(mydate.substring(0, 4)),
                            Integer.parseInt(mydate.substring(5, 7))-1,
                            Integer.parseInt(mydate.substring(8, 10)),
                            Integer.parseInt(mydate.substring(11, 13)),
                            0);
                    break;
                case TimeInString$YYYY_mM_dD_hH:
                    timespace=mydate.split(" ");
                    timesminus=timespace[0].split("-");
                    timescolon=timespace[1].split(":");
                    MyDate.set(Integer.parseInt(timesminus[0]),
                            Integer.parseInt(timesminus[1])-1,
                            Integer.parseInt(timesminus[2]),
                            Integer.parseInt(timescolon[0]),
                            0);
                    break;
                case TimeInString$YYYY_MM_DD:
                    MyDate.set(Integer.parseInt(mydate.substring(0, 4)),
                            Integer.parseInt(mydate.substring(5, 7))-1,
                            Integer.parseInt(mydate.substring(8, 10)));
                    break;
                case TimeInString$YYYY_mM_dD:
                    timesminus=mydate.split("-");
                    MyDate.set(Integer.parseInt(timesminus[0]),
                            Integer.parseInt(timesminus[1])-1,
                            Integer.parseInt(timesminus[2]));
                    break;
                case TimeInString$YYYY_MM:
                    MyDate.set(Integer.parseInt(mydate.substring(0, 4)),
                            Integer.parseInt(mydate.substring(5, 7))-1,
                            1);
                    break;
                case TimeInString$YYYY_mM:
                    timesminus=mydate.split("-");
                    MyDate.set(Integer.parseInt(timesminus[0]),
                            Integer.parseInt(timesminus[1])-1,
                            1);
                    break;
                case TimeInString$YYYY:
                    MyDate.set(Integer.parseInt(mydate),
                            0,
                            1);
                    break;
                default:
                    ;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String getMyDate(int TimeFormat) {
        return getTime(MyDate,TimeFormat);
    }

    public String getMyDate(String myDate,int inTimeFormat,int outTimeFormat) {
        setMyDate(myDate,inTimeFormat);
        return getTime(MyDate,outTimeFormat);
    }

    public String getTime(Calendar thisDate,int TimeFormat) {
        switch(TimeFormat) {
            case TimeInString$CYYMMDDHHMMSSTTT:
                return "1"+String.valueOf(thisDate.get(Calendar.YEAR)).substring(2,4)
                        +((thisDate.get(Calendar.MONTH)+1)<10?"0"+(thisDate.get(Calendar.MONTH)+1):""+(thisDate.get(Calendar.MONTH)+1))
                        +(thisDate.get(Calendar.DATE)<10?"0"+thisDate.get(Calendar.DATE):""+thisDate.get(Calendar.DATE))
                        +(thisDate.get(Calendar.HOUR_OF_DAY)<10?"0"+thisDate.get(Calendar.HOUR_OF_DAY):""+thisDate.get(Calendar.HOUR_OF_DAY))
                        +(thisDate.get(Calendar.MINUTE)<10?"0"+thisDate.get(Calendar.MINUTE):""+thisDate.get(Calendar.MINUTE))
                        +(thisDate.get(Calendar.SECOND)<10?"0"+thisDate.get(Calendar.SECOND):""+thisDate.get(Calendar.SECOND))+"000";
            case TimeInString$YYYY_MM_DD_HH_MM_SS:
                return ""+thisDate.get(Calendar.YEAR)+"-"
                        +((thisDate.get(Calendar.MONTH)+1)<10?"0"+(thisDate.get(Calendar.MONTH)+1):""+(thisDate.get(Calendar.MONTH)+1))+"-"
                        +(thisDate.get(Calendar.DATE)<10?"0"+thisDate.get(Calendar.DATE):""+thisDate.get(Calendar.DATE))+" "
                        +(thisDate.get(Calendar.HOUR_OF_DAY)<10?"0"+thisDate.get(Calendar.HOUR_OF_DAY):""+thisDate.get(Calendar.HOUR_OF_DAY))+":"
                        +(thisDate.get(Calendar.MINUTE)<10?"0"+thisDate.get(Calendar.MINUTE):""+thisDate.get(Calendar.MINUTE))+":"
                        +(thisDate.get(Calendar.SECOND)<10?"0"+thisDate.get(Calendar.SECOND):""+thisDate.get(Calendar.SECOND));
            case TimeInString$YYYY_MM_DD_HH_MM_SS_MS:
                return ""+thisDate.get(Calendar.YEAR)+"-"
                        +((thisDate.get(Calendar.MONTH)+1)<10?"0"+(thisDate.get(Calendar.MONTH)+1):""+(thisDate.get(Calendar.MONTH)+1))+"-"
                        +(thisDate.get(Calendar.DATE)<10?"0"+thisDate.get(Calendar.DATE):""+thisDate.get(Calendar.DATE))+" "
                        +(thisDate.get(Calendar.HOUR_OF_DAY)<10?"0"+thisDate.get(Calendar.HOUR_OF_DAY):""+thisDate.get(Calendar.HOUR_OF_DAY))+":"
                        +(thisDate.get(Calendar.MINUTE)<10?"0"+thisDate.get(Calendar.MINUTE):""+thisDate.get(Calendar.MINUTE))+":"
                        +(thisDate.get(Calendar.SECOND)<10?"0"+thisDate.get(Calendar.SECOND):""+thisDate.get(Calendar.SECOND))+"."
                        +(thisDate.get(Calendar.MILLISECOND)<10?"0"+thisDate.get(Calendar.MILLISECOND):""+thisDate.get(Calendar.MILLISECOND));
            case TimeInString$YYYY_mM_dD_hH_mM_sS:
                return ""+thisDate.get(Calendar.YEAR)+"-"+(thisDate.get(Calendar.MONTH)+1)+"-"
                        +thisDate.get(Calendar.DATE)+" "+thisDate.get(Calendar.HOUR_OF_DAY)+":"
                        +thisDate.get(Calendar.MINUTE)+":"+thisDate.get(Calendar.SECOND);
            case TimeInString$YYYY_MM_DD_HH_MM:
                return ""+thisDate.get(Calendar.YEAR)+"-"
                        +((thisDate.get(Calendar.MONTH)+1)<10?"0"+(thisDate.get(Calendar.MONTH)+1):""+(thisDate.get(Calendar.MONTH)+1))+"-"
                        +(thisDate.get(Calendar.DATE)<10?"0"+thisDate.get(Calendar.DATE):""+thisDate.get(Calendar.DATE))+" "
                        +(thisDate.get(Calendar.HOUR_OF_DAY)<10?"0"+thisDate.get(Calendar.HOUR_OF_DAY):""+thisDate.get(Calendar.HOUR_OF_DAY))+":"
                        +(thisDate.get(Calendar.MINUTE)<10?"0"+thisDate.get(Calendar.MINUTE):""+thisDate.get(Calendar.MINUTE));
            case TimeInString$YYYY_mM_dD_hH_mM:
                return ""+thisDate.get(Calendar.YEAR)+"-"+(thisDate.get(Calendar.MONTH)+1)+"-"
                        +thisDate.get(Calendar.DATE)+" "+thisDate.get(Calendar.HOUR_OF_DAY)+":"
                        +thisDate.get(Calendar.MINUTE);
            case TimeInString$YYYY_MM_DD_HH:
                return ""+thisDate.get(Calendar.YEAR)+"-"
                        +((thisDate.get(Calendar.MONTH)+1)<10?"0"+(thisDate.get(Calendar.MONTH)+1):""+(thisDate.get(Calendar.MONTH)+1))+"-"
                        +(thisDate.get(Calendar.DATE)<10?"0"+thisDate.get(Calendar.DATE):""+thisDate.get(Calendar.DATE))+" "
                        +(thisDate.get(Calendar.HOUR_OF_DAY)<10?"0"+thisDate.get(Calendar.HOUR_OF_DAY):""+thisDate.get(Calendar.HOUR_OF_DAY));
            case TimeInString$YYYY_mM_dD_hH:
                return ""+thisDate.get(Calendar.YEAR)+"-"+(thisDate.get(Calendar.MONTH)+1)+"-"
                        +thisDate.get(Calendar.DATE)+" "+thisDate.get(Calendar.HOUR_OF_DAY);
            case TimeInString$YYYY_MM_DD:
                return ""+thisDate.get(Calendar.YEAR)+"-"
                        +((thisDate.get(Calendar.MONTH)+1)<10?"0"+(thisDate.get(Calendar.MONTH)+1):""+(thisDate.get(Calendar.MONTH)+1))+"-"
                        +(thisDate.get(Calendar.DATE)<10?"0"+thisDate.get(Calendar.DATE):""+thisDate.get(Calendar.DATE));
            case TimeInString$YYYY_mM_dD:
                return ""+thisDate.get(Calendar.YEAR)+"-"+(thisDate.get(Calendar.MONTH)+1)+"-"
                        +thisDate.get(Calendar.DATE);
            case TimeInString$YYYY_MM:
                return ""+thisDate.get(Calendar.YEAR)+"-"
                        +((thisDate.get(Calendar.MONTH)+1)<10?"0"+(thisDate.get(Calendar.MONTH)+1):""+(thisDate.get(Calendar.MONTH)+1));
            case TimeInString$YYYY_mM:
                return ""+thisDate.get(Calendar.YEAR)+"-"+(thisDate.get(Calendar.MONTH)+1);
            case TimeInString$YYYY:
                return ""+thisDate.get(Calendar.YEAR);

            case TimeInString$YYYYcnMMcnDDcnHHcnMMcnSScn:
                return ""+thisDate.get(Calendar.YEAR)+"��"
                        +((thisDate.get(Calendar.MONTH)+1)<10?"0"+(thisDate.get(Calendar.MONTH)+1):""+(thisDate.get(Calendar.MONTH)+1))+"��"
                        +(thisDate.get(Calendar.DATE)<10?"0"+thisDate.get(Calendar.DATE):""+thisDate.get(Calendar.DATE))+"��"
                        +(thisDate.get(Calendar.HOUR_OF_DAY)<10?"0"+thisDate.get(Calendar.HOUR_OF_DAY):""+thisDate.get(Calendar.HOUR_OF_DAY))+"ʱ"
                        +(thisDate.get(Calendar.MINUTE)<10?"0"+thisDate.get(Calendar.MINUTE):""+thisDate.get(Calendar.MINUTE))+"��"
                        +(thisDate.get(Calendar.SECOND)<10?"0"+thisDate.get(Calendar.SECOND):""+thisDate.get(Calendar.SECOND))+"��";
            case TimeInString$YYYYcnmMcndDcnhHcnmMcnsScn:
                return ""+thisDate.get(Calendar.YEAR)+"��"+(thisDate.get(Calendar.MONTH)+1)+"��"
                        +thisDate.get(Calendar.DATE)+"��"+thisDate.get(Calendar.HOUR_OF_DAY)+"ʱ"
                        +thisDate.get(Calendar.MINUTE)+"��"+thisDate.get(Calendar.SECOND)+"��";
            case TimeInString$YYYYcnMMcnDDcnHHcnMMcn:
                return ""+thisDate.get(Calendar.YEAR)+"��"
                        +((thisDate.get(Calendar.MONTH)+1)<10?"0"+(thisDate.get(Calendar.MONTH)+1):""+(thisDate.get(Calendar.MONTH)+1))+"��"
                        +(thisDate.get(Calendar.DATE)<10?"0"+thisDate.get(Calendar.DATE):""+thisDate.get(Calendar.DATE))+"��"
                        +(thisDate.get(Calendar.HOUR_OF_DAY)<10?"0"+thisDate.get(Calendar.HOUR_OF_DAY):""+thisDate.get(Calendar.HOUR_OF_DAY))+"ʱ"
                        +(thisDate.get(Calendar.MINUTE)<10?"0"+thisDate.get(Calendar.MINUTE):""+thisDate.get(Calendar.MINUTE))+"��";
            case TimeInString$YYYYcnmMcndDcnhHcnmMcn:
                return ""+thisDate.get(Calendar.YEAR)+"��"+(thisDate.get(Calendar.MONTH)+1)+"��"
                        +thisDate.get(Calendar.DATE)+"��"+thisDate.get(Calendar.HOUR_OF_DAY)+"ʱ"
                        +thisDate.get(Calendar.MINUTE)+"��";
            case TimeInString$YYYYcnMMcnDDcnHHcn:
                return ""+thisDate.get(Calendar.YEAR)+"��"
                        +((thisDate.get(Calendar.MONTH)+1)<10?"0"+(thisDate.get(Calendar.MONTH)+1):""+(thisDate.get(Calendar.MONTH)+1))+"��"
                        +(thisDate.get(Calendar.DATE)<10?"0"+thisDate.get(Calendar.DATE):""+thisDate.get(Calendar.DATE))+"��"
                        +(thisDate.get(Calendar.HOUR_OF_DAY)<10?"0"+thisDate.get(Calendar.HOUR_OF_DAY):""+thisDate.get(Calendar.HOUR_OF_DAY))+"ʱ";
            case TimeInString$YYYYcnmMcndDcnhHcn:
                return ""+thisDate.get(Calendar.YEAR)+"��"+(thisDate.get(Calendar.MONTH)+1)+"��"
                        +thisDate.get(Calendar.DATE)+"��"+thisDate.get(Calendar.HOUR_OF_DAY)+"ʱ";
            case TimeInString$YYYYcnMMcnDDcn:
                return ""+thisDate.get(Calendar.YEAR)+"��"
                        +((thisDate.get(Calendar.MONTH)+1)<10?"0"+(thisDate.get(Calendar.MONTH)+1):""+(thisDate.get(Calendar.MONTH)+1))+"��"
                        +(thisDate.get(Calendar.DATE)<10?"0"+thisDate.get(Calendar.DATE):""+thisDate.get(Calendar.DATE))+"��";
            case TimeInString$YYYYcnmMcndDcn:
                return ""+thisDate.get(Calendar.YEAR)+"��"+(thisDate.get(Calendar.MONTH)+1)+"��"
                        +thisDate.get(Calendar.DATE)+"��";
            case TimeInString$YYYYcnMMcn:
                return ""+thisDate.get(Calendar.YEAR)+"��"
                        +((thisDate.get(Calendar.MONTH)+1)<10?"0"+(thisDate.get(Calendar.MONTH)+1):""+(thisDate.get(Calendar.MONTH)+1))+"��";
            case TimeInString$YYYYcnmMcn:
                return ""+thisDate.get(Calendar.YEAR)+"��"+(thisDate.get(Calendar.MONTH)+1)+"��";
            case TimeInString$YYYYcn:
                return ""+thisDate.get(Calendar.YEAR)+"��";

//	      case TimeInLong$MilliSecond:
//	    	  return thisDate.getTimeInMillis()+"";
//	      case TimeInLong$Second:
//	    	  return thisDate.getTimeInMillis()/1000+"";
//	      case TimeInLong$YYYYMMDDHHMMSS:
//	    	  return ""+thisDate.get(Calendar.YEAR)+(thisDate.get(Calendar.MONTH)+1)
//	    			  +thisDate.get(Calendar.DATE)+thisDate.get(Calendar.HOUR_OF_DAY)
//	    			  +thisDate.get(Calendar.MINUTE)+thisDate.get(Calendar.SECOND);
//	      case TimeInLong$YYYYMMDDHHMM:
//	    	  return ""+thisDate.get(Calendar.YEAR)+(thisDate.get(Calendar.MONTH)+1)
//	    			  +thisDate.get(Calendar.DATE)+thisDate.get(Calendar.HOUR_OF_DAY)
//	    			  +thisDate.get(Calendar.MINUTE);
//	      case TimeInLong$YYYYMMDDHH:
//	    	  return ""+thisDate.get(Calendar.YEAR)+(thisDate.get(Calendar.MONTH)+1)
//	    			  +thisDate.get(Calendar.DATE)+thisDate.get(Calendar.HOUR_OF_DAY);
//	      case TimeInLong$YYYYMMDD:
//	    	  return ""+thisDate.get(Calendar.YEAR)+(thisDate.get(Calendar.MONTH)+1)
//	    			  +thisDate.get(Calendar.DATE);
//	      case TimeInLong$YYYYMM:
//	    	  return ""+thisDate.get(Calendar.YEAR)+(thisDate.get(Calendar.MONTH)+1);
//	      case TimeInLong$YYYY:
//	    	  return ""+thisDate.get(Calendar.YEAR);
            default:
                return getLongTime(thisDate,TimeFormat)+"";
        }
    }

    public long getLongTime(Calendar thisDate,int TimeFormat) {
        DecimalFormat df = new DecimalFormat("00");
        switch(TimeFormat) {
            case TimeInString$CYYMMDDHHMMSSTTT:
                return Long.parseLong("1"+String.valueOf(thisDate.get(Calendar.YEAR)).substring(2,4)
                        +((thisDate.get(Calendar.MONTH)+1)<10?"0"+(thisDate.get(Calendar.MONTH)+1):""+(thisDate.get(Calendar.MONTH)+1))
                        +(thisDate.get(Calendar.DATE)<10?"0"+thisDate.get(Calendar.DATE):""+thisDate.get(Calendar.DATE))
                        +(thisDate.get(Calendar.HOUR_OF_DAY)<10?"0"+thisDate.get(Calendar.HOUR_OF_DAY):""+thisDate.get(Calendar.HOUR_OF_DAY))
                        +(thisDate.get(Calendar.MINUTE)<10?"0"+thisDate.get(Calendar.MINUTE):""+thisDate.get(Calendar.MINUTE))
                        +(thisDate.get(Calendar.SECOND)<10?"0"+thisDate.get(Calendar.SECOND):""+thisDate.get(Calendar.SECOND))+"000");
            case TimeInLong$MilliSecond:
                return thisDate.getTimeInMillis();
            case TimeInLong$MicroSecond:
                return thisDate.getTimeInMillis()*1000;
            case TimeInLong$Second:
                return thisDate.getTimeInMillis()/1000;
            case TimeInLong$YYYYMMDDHHMMSS:
                return Long.parseLong(""+thisDate.get(Calendar.YEAR)+df.format((thisDate.get(Calendar.MONTH)+1))
                        +df.format(thisDate.get(Calendar.DATE))+df.format(thisDate.get(Calendar.HOUR_OF_DAY))
                        +df.format(thisDate.get(Calendar.MINUTE))+df.format(thisDate.get(Calendar.SECOND))
                );
            case TimeInLong$YYYYMMDDHHMM:
                return Long.parseLong(""+thisDate.get(Calendar.YEAR)+df.format((thisDate.get(Calendar.MONTH)+1))
                        +df.format(thisDate.get(Calendar.DATE))+df.format(thisDate.get(Calendar.HOUR_OF_DAY))
                        +df.format(thisDate.get(Calendar.MINUTE))
                );
            case TimeInLong$YYYYMMDDHH:
                return Long.parseLong(""+thisDate.get(Calendar.YEAR)+df.format((thisDate.get(Calendar.MONTH)+1))
                        +df.format(thisDate.get(Calendar.DATE))+df.format(thisDate.get(Calendar.HOUR_OF_DAY))
                );
            case TimeInLong$YYYYMMDD:
                return Long.parseLong(""+thisDate.get(Calendar.YEAR)+df.format((thisDate.get(Calendar.MONTH)+1))
                        +df.format(thisDate.get(Calendar.DATE))
                );
            case TimeInLong$YYYYMM:
                return Long.parseLong(""+thisDate.get(Calendar.YEAR)+df.format((thisDate.get(Calendar.MONTH)+1))
                );
            case TimeInLong$YYYY:
                return Long.parseLong(""+thisDate.get(Calendar.YEAR)
                );
            default:
                return 0;
        }
    }

    public String getStringTime(Calendar thisDate,int TimeFormat) {

        switch(TimeFormat) {
            case TimeInString$CYYMMDDHHMMSSTTT:
                return "1"+String.valueOf(thisDate.get(Calendar.YEAR)).substring(2,4)
                        +((thisDate.get(Calendar.MONTH)+1)<10?"0"+(thisDate.get(Calendar.MONTH)+1):""+(thisDate.get(Calendar.MONTH)+1))
                        +(thisDate.get(Calendar.DATE)<10?"0"+thisDate.get(Calendar.DATE):""+thisDate.get(Calendar.DATE))
                        +(thisDate.get(Calendar.HOUR_OF_DAY)<10?"0"+thisDate.get(Calendar.HOUR_OF_DAY):""+thisDate.get(Calendar.HOUR_OF_DAY))
                        +(thisDate.get(Calendar.MINUTE)<10?"0"+thisDate.get(Calendar.MINUTE):""+thisDate.get(Calendar.MINUTE))
                        +(thisDate.get(Calendar.SECOND)<10?"0"+thisDate.get(Calendar.SECOND):""+thisDate.get(Calendar.SECOND))+"000";
            case TimeInString$YYYY_MM_DD_HH_MM_SS:
                return ""+thisDate.get(Calendar.YEAR)+"-"
                        +((thisDate.get(Calendar.MONTH)+1)<10?"0"+(thisDate.get(Calendar.MONTH)+1):""+(thisDate.get(Calendar.MONTH)+1))+"-"
                        +(thisDate.get(Calendar.DATE)<10?"0"+thisDate.get(Calendar.DATE):""+thisDate.get(Calendar.DATE))+" "
                        +(thisDate.get(Calendar.HOUR_OF_DAY)<10?"0"+thisDate.get(Calendar.HOUR_OF_DAY):""+thisDate.get(Calendar.HOUR_OF_DAY))+":"
                        +(thisDate.get(Calendar.MINUTE)<10?"0"+thisDate.get(Calendar.MINUTE):""+thisDate.get(Calendar.MINUTE))+":"
                        +(thisDate.get(Calendar.SECOND)<10?"0"+thisDate.get(Calendar.SECOND):""+thisDate.get(Calendar.SECOND));
            case TimeInString$YYYY_mM_dD_hH_mM_sS:
                return ""+thisDate.get(Calendar.YEAR)+"-"+(thisDate.get(Calendar.MONTH)+1)+"-"
                        +thisDate.get(Calendar.DATE)+" "+thisDate.get(Calendar.HOUR_OF_DAY)+":"
                        +thisDate.get(Calendar.MINUTE)+":"+thisDate.get(Calendar.SECOND);
            case TimeInString$YYYY_MM_DD_HH_MM:
                return ""+thisDate.get(Calendar.YEAR)+"-"
                        +((thisDate.get(Calendar.MONTH)+1)<10?"0"+(thisDate.get(Calendar.MONTH)+1):""+(thisDate.get(Calendar.MONTH)+1))+"-"
                        +(thisDate.get(Calendar.DATE)<10?"0"+thisDate.get(Calendar.DATE):""+thisDate.get(Calendar.DATE))+" "
                        +(thisDate.get(Calendar.HOUR_OF_DAY)<10?"0"+thisDate.get(Calendar.HOUR_OF_DAY):""+thisDate.get(Calendar.HOUR_OF_DAY))+":"
                        +(thisDate.get(Calendar.MINUTE)<10?"0"+thisDate.get(Calendar.MINUTE):""+thisDate.get(Calendar.MINUTE));
            case TimeInString$YYYY_mM_dD_hH_mM:
                return ""+thisDate.get(Calendar.YEAR)+"-"+(thisDate.get(Calendar.MONTH)+1)+"-"
                        +thisDate.get(Calendar.DATE)+" "+thisDate.get(Calendar.HOUR_OF_DAY)+":"
                        +thisDate.get(Calendar.MINUTE);
            case TimeInString$YYYY_MM_DD_HH:
                return ""+thisDate.get(Calendar.YEAR)+"-"
                        +((thisDate.get(Calendar.MONTH)+1)<10?"0"+(thisDate.get(Calendar.MONTH)+1):""+(thisDate.get(Calendar.MONTH)+1))+"-"
                        +(thisDate.get(Calendar.DATE)<10?"0"+thisDate.get(Calendar.DATE):""+thisDate.get(Calendar.DATE))+" "
                        +(thisDate.get(Calendar.HOUR_OF_DAY)<10?"0"+thisDate.get(Calendar.HOUR_OF_DAY):""+thisDate.get(Calendar.HOUR_OF_DAY));
            case TimeInString$YYYY_mM_dD_hH:
                return ""+thisDate.get(Calendar.YEAR)+"-"+(thisDate.get(Calendar.MONTH)+1)+"-"
                        +thisDate.get(Calendar.DATE)+" "+thisDate.get(Calendar.HOUR_OF_DAY);
            case TimeInString$YYYY_MM_DD:
                return ""+thisDate.get(Calendar.YEAR)+"-"
                        +((thisDate.get(Calendar.MONTH)+1)<10?"0"+(thisDate.get(Calendar.MONTH)+1):""+(thisDate.get(Calendar.MONTH)+1))+"-"
                        +(thisDate.get(Calendar.DATE)<10?"0"+thisDate.get(Calendar.DATE):""+thisDate.get(Calendar.DATE));
            case TimeInString$YYYY_mM_dD:
                return ""+thisDate.get(Calendar.YEAR)+"-"+(thisDate.get(Calendar.MONTH)+1)+"-"
                        +thisDate.get(Calendar.DATE);
            case TimeInString$YYYY_MM:
                return ""+thisDate.get(Calendar.YEAR)+"-"
                        +((thisDate.get(Calendar.MONTH)+1)<10?"0"+(thisDate.get(Calendar.MONTH)+1):""+(thisDate.get(Calendar.MONTH)+1));
            case TimeInString$YYYY_mM:
                return ""+thisDate.get(Calendar.YEAR)+"-"+(thisDate.get(Calendar.MONTH)+1);
            case TimeInString$YYYY:
                return ""+thisDate.get(Calendar.YEAR);

            case TimeInString$YYYYcnMMcnDDcnHHcnMMcnSScn:
                return ""+thisDate.get(Calendar.YEAR)+"��"
                        +((thisDate.get(Calendar.MONTH)+1)<10?"0"+(thisDate.get(Calendar.MONTH)+1):""+(thisDate.get(Calendar.MONTH)+1))+"��"
                        +(thisDate.get(Calendar.DATE)<10?"0"+thisDate.get(Calendar.DATE):""+thisDate.get(Calendar.DATE))+"��"
                        +(thisDate.get(Calendar.HOUR_OF_DAY)<10?"0"+thisDate.get(Calendar.HOUR_OF_DAY):""+thisDate.get(Calendar.HOUR_OF_DAY))+"ʱ"
                        +(thisDate.get(Calendar.MINUTE)<10?"0"+thisDate.get(Calendar.MINUTE):""+thisDate.get(Calendar.MINUTE))+"��"
                        +(thisDate.get(Calendar.SECOND)<10?"0"+thisDate.get(Calendar.SECOND):""+thisDate.get(Calendar.SECOND))+"��";
            case TimeInString$YYYYcnmMcndDcnhHcnmMcnsScn:
                return ""+thisDate.get(Calendar.YEAR)+"��"+(thisDate.get(Calendar.MONTH)+1)+"��"
                        +thisDate.get(Calendar.DATE)+"��"+thisDate.get(Calendar.HOUR_OF_DAY)+"ʱ"
                        +thisDate.get(Calendar.MINUTE)+"��"+thisDate.get(Calendar.SECOND)+"��";
            case TimeInString$YYYYcnMMcnDDcnHHcnMMcn:
                return ""+thisDate.get(Calendar.YEAR)+"��"
                        +((thisDate.get(Calendar.MONTH)+1)<10?"0"+(thisDate.get(Calendar.MONTH)+1):""+(thisDate.get(Calendar.MONTH)+1))+"��"
                        +(thisDate.get(Calendar.DATE)<10?"0"+thisDate.get(Calendar.DATE):""+thisDate.get(Calendar.DATE))+"��"
                        +(thisDate.get(Calendar.HOUR_OF_DAY)<10?"0"+thisDate.get(Calendar.HOUR_OF_DAY):""+thisDate.get(Calendar.HOUR_OF_DAY))+"ʱ"
                        +(thisDate.get(Calendar.MINUTE)<10?"0"+thisDate.get(Calendar.MINUTE):""+thisDate.get(Calendar.MINUTE))+"��";
            case TimeInString$YYYYcnmMcndDcnhHcnmMcn:
                return ""+thisDate.get(Calendar.YEAR)+"��"+(thisDate.get(Calendar.MONTH)+1)+"��"
                        +thisDate.get(Calendar.DATE)+"��"+thisDate.get(Calendar.HOUR_OF_DAY)+"ʱ"
                        +thisDate.get(Calendar.MINUTE)+"��";
            case TimeInString$YYYYcnMMcnDDcnHHcn:
                return ""+thisDate.get(Calendar.YEAR)+"��"
                        +((thisDate.get(Calendar.MONTH)+1)<10?"0"+(thisDate.get(Calendar.MONTH)+1):""+(thisDate.get(Calendar.MONTH)+1))+"��"
                        +(thisDate.get(Calendar.DATE)<10?"0"+thisDate.get(Calendar.DATE):""+thisDate.get(Calendar.DATE))+"��"
                        +(thisDate.get(Calendar.HOUR_OF_DAY)<10?"0"+thisDate.get(Calendar.HOUR_OF_DAY):""+thisDate.get(Calendar.HOUR_OF_DAY))+"ʱ";
            case TimeInString$YYYYcnmMcndDcnhHcn:
                return ""+thisDate.get(Calendar.YEAR)+"��"+(thisDate.get(Calendar.MONTH)+1)+"��"
                        +thisDate.get(Calendar.DATE)+"��"+thisDate.get(Calendar.HOUR_OF_DAY)+"ʱ";
            case TimeInString$YYYYcnMMcnDDcn:
                return ""+thisDate.get(Calendar.YEAR)+"��"
                        +((thisDate.get(Calendar.MONTH)+1)<10?"0"+(thisDate.get(Calendar.MONTH)+1):""+(thisDate.get(Calendar.MONTH)+1))+"��"
                        +(thisDate.get(Calendar.DATE)<10?"0"+thisDate.get(Calendar.DATE):""+thisDate.get(Calendar.DATE))+"��";
            case TimeInString$YYYYcnmMcndDcn:
                return ""+thisDate.get(Calendar.YEAR)+"��"+(thisDate.get(Calendar.MONTH)+1)+"��"
                        +thisDate.get(Calendar.DATE)+"��";
            case TimeInString$YYYYcnMMcn:
                return ""+thisDate.get(Calendar.YEAR)+"��"
                        +((thisDate.get(Calendar.MONTH)+1)<10?"0"+(thisDate.get(Calendar.MONTH)+1):""+(thisDate.get(Calendar.MONTH)+1))+"��";
            case TimeInString$YYYYcnmMcn:
                return ""+thisDate.get(Calendar.YEAR)+"��"+(thisDate.get(Calendar.MONTH)+1)+"��";
            case TimeInString$YYYYcn:
                return ""+thisDate.get(Calendar.YEAR)+"��";

            default:
                return "";
        }
    }




    /**
     * ����ʱ������
     *
     */
    private void buildTimeZone() {
        this.calender.setTimeZone(TimeZone.getTimeZone(timeZoneOffSet));
    }

    /** ���Է���������************************************************************************ */
    /**
     * ��ȡһ���еĵڼ���ȡֵ��Χ1-31��
     *
     * @return Returns the date.
     */
    public int getDate() {
        return this.calender.get(Calendar.DATE);
    }

    /**
     * ȡһ�����ڵĵڼ��졣 ��ĩΪһ�����ڵĵ�һ�죻
     *
     * @return
     */
    public int getDay() {
        return this.calender.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * ��ȡСʱ����Χ0-23��
     *
     * @return Returns the hours.
     */
    public int getHours() {
        return this.calender.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * ��ȡ���ӣ���Χ0-59��
     *
     * @return Returns the minutes.
     */
    public int getMinutes() {
        return this.calender.get(Calendar.MINUTE);
    }

    /**
     * ��ȡ�·ݣ���Χ1-12��
     *
     * @return Returns the month.
     */
    public int getMonth() {
        return this.calender.get(Calendar.MONTH) + 1;
    }

    /**
     * ��ȡ�룬��Χ0-59��
     *
     * @return Returns the seconds.
     */
    public int getSeconds() {
        return this.calender.get(Calendar.SECOND);
    }

    /**
     * ��ȡ�꣬��Χ1900-9999��
     *
     * @return Returns the year.
     */
    public int getYear() {
        return this.calender.get(Calendar.YEAR);
    }

    /**
     * ��ȡ������ʱ�����е�һ��ֵ��
     *
     * @return
     */
    public int get(String style) {
        return Integer.parseInt(this.toString(style));
    }
    public void setYear(int year,int month,int date){
        this.calender.set(year,month-1,date);
    }
    public void setHour(int hour,int minute,int second){
        this.calender.set(getYear(),getMonth()-1,getDate(),hour,minute,second);
    }

    /**
     * ��ȡ��������1970-1-1 00:00:00��ָ��ʱ��ĺ�����
     *
     * @return ����
     */
    /** �������Է���������************************************************************************ */

    /**
     * ��ȡʱ�������ֵ��
     */
    public long getValue() {
        return this.calender.getTimeInMillis();
    }

    /**
     * ��ȡһ�������ķǱ��ػ�ʱ���ַ���ֵ�� *
     *
     * @param style
     *            Ҫ���ʱ��Ľ�ֹ��,��ȡֵ��ΧΪ
     *            <b>yyyy-mm-dd �� </b>��
     * @return ʱ����ַ�����
     */
    public String toStdString(String style) {
        return this.toString(style);
    }

    /**
     * ��ȡһ�������ı��ػ�ʱ���ַ���ֵ��
     *
     * @param outScope
     *            Ҫ���ʱ��Ľ�ֹ��,��ȡֵ��ΧΪ
     *            <b>yyyy-mm-dd �� </b>��
     * @return ʱ����ַ�����
     */
    public String toLocStdString(String style) {
        return this.toString(style);
    }

    /**
     * �൱ǰ��������������
     *
     * @param days
     */
    public void addDay(int days) {
        this.calender.add(Calendar.DATE, days);
        MyDate.add(Calendar.DATE, days);
    }


    /**
     *
     * ���������field��λ��������amount��ֵ�γ�һ���µ����ڡ�
     * @param field �μ�Calendar�������
     */
    public void add(int field,int amount){
        this.calender.add(field,amount);
        MyDate.add(field,amount);
    }

    /**
     * ���л�ʱ��Ϊ��������������ʱ��2004-4-5 0:12:00 �Ϳ��Եõ� ����20040405001200�� *
     *
     * @param Ҫ���ʱ��Ľ�ֹ��,��ȡֵ��ΧΪ
     *            <b>yyyy-mm-dd �� </b>��
     * @return ���л���ʱ��������
     */
    public long getSeriateValue(String style) {
        return Long.parseLong(this.toString(style));
    }

    /**
     * �������ַ�����
     *
     * @param pattern
     *            ��������ڸ�ʽ��
     * @return
     */
    public String toString(String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(this.calender.getTime());
    }



    /**
     * ��ȡ��ǰ�·ݵĵ�һ���ʱ�䡣
     *
     * @return
     */
//    public synchronized static TimeHelper getFirstDayofThisMonth() {
//        TimeHelper date = TimeHelper.Now();
//        date.calender.set(date.getYear(), date.getMonth()-1, 1,0,0,0);
//        return date;
//    }

    /**
     * ��ȡ��ǰ�·ݵ����һ���ʱ�䡣
     *
     * @return
     */
//    public synchronized static TimeHelper getLastDayofThisMonth() {
//        TimeHelper date = TimeHelper.Now();
//        int date2 = 31;
//        int month = date.getMonth();
//        if (month == 2) {
//            date2 = 28;
//            if (date.calender.isLeapYear(date.getYear())) {
//                date2 = 29;
//            }
//        }
//        else if (month == 4 || month == 6 || month == 9 || month == 11) {
//            date2 = 30;
//        }
//        date.calender.set(date.getYear(), month-1, date2,0,0,0);
//        return date;
//    }

    /**
     * ��ָ����ʱ��������
     * @param dateValue ָ����ʱ�������ֵ��
     * @return ������
     */
    public int distanceDayFrom(long dateValue){
        long dintanceValue=this.getValue()-dateValue;
        int distanceDay=(int)(dintanceValue/oneDayValue);
        return distanceDay;

    }

    /**
     * ��ȡ��ʱ���ڴ����еĵڼ��ܡ�
     * @return
     */
    public int getWeekOfYear(){
        return this.calender.get(Calendar.WEEK_OF_YEAR);
    }

    public static void main(String[] args) {
        try {
            TimeHelper date = new TimeHelper("2004040412", "yyyyMMddHH");
//            System.out.println(date.toString(DateStyle.lOCAL_TO_HOUR));
            System.out.println(date.toString("HH:mm:ss"));

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * ����ת������Ȼ������������3661�����Ȼ������1Сʱ1��1�롣
     * @param ms  ����
     * @param isShowZero �Ƿ���ʾΪ0.
     *            �棺0�룻�٣�����1��
     * @return ����ʱ����֤���ظ�ʱ��ֵ
     */
    public static String getTimeExpression(long ms,boolean isShowZero) {
        long day;
        long hour;
        long minutes;
        long second;
        long ONE_SECOND = 1000;

        long ONE_MINUTE = ONE_SECOND * 60;
        long ONE_HOUR = ONE_MINUTE * 60;
        long ONE_DAY = ONE_HOUR * 24;

        day = (long) (ms / ONE_DAY);
        long leftHour = ms - ONE_DAY * day;

        hour = (long) (leftHour / ONE_HOUR);
        long leftMinutes = leftHour - hour * ONE_HOUR;

        minutes = (long) (leftMinutes / ONE_MINUTE);
        long leftSecond = (long) (leftMinutes - minutes * ONE_MINUTE);

        second = (long) (leftSecond / ONE_SECOND);
        StringBuffer exp = new StringBuffer();
        if (day != 0) {
            exp.append(day + "��");
        }
        if (hour != 0) {
            exp.append(hour + "Сʱ");
        }
        if (minutes != 0) {
            exp.append(minutes + "��");
        }
        if (second != 0) {
            exp.append(second + "��");
        } else {
            if (exp.length() == 0) {
                if(isShowZero){
                    exp.append("0��");
                }else{
                    exp.append("����1��");
                }
            }
        }
        return exp.toString();
    }

//	/**
//	 * 
//	 * @param TimeFormat ʱ���ʽ
//	 * @return ����������ڼ�1��,һ���������ܵ����һ��
//	 */
//    public String getEndWeekDateWithHourOffset(int TimeFormat) {
//    	Calendar newDate=Calendar.getInstance();
//    	newDate.setTimeInMillis(endDate.getTimeInMillis()+endHourOffset*oneHourValue - oneHourValue*24);
//		return getTime(newDate,TimeFormat);
//	}

    /**
     * ��������ʱ����������
     * ע�⣺��ʼʱ�� ����ʱ�� ����ȡ��
     * ��: time = new TimeHelper(time.getThisDay());
     */
    public static long betweenday(TimeHelper time1,TimeHelper time2){
        long dtime=0;
//		time1= new TimeHelper(time1.getThisDay());
//		time2= new TimeHelper(time2.getThisDay());
        if(time1.calender.after(time2.calender)){
            dtime=time1.getValue()-time2.getValue();
        }else{
            dtime=time2.getValue()-time1.getValue();
        }
        return dtime;
    }
    /**
     * ���ؽ������ܼ�
     * ������1����һ��2���ܶ���3��������,������7
     */
    public int  getWeekOfData(){
        return this.calender.get(Calendar.DAY_OF_WEEK);
    }
    /**
     * ���ؽ������ܼ�
     * ������7����һ��1���ܶ���2��������,������6
     */
    public int  getWeekOfDate(){
        int i=this.calender.get(Calendar.DAY_OF_WEEK);
        if(i==1) i=8;
        return i-1;
    }

    public String getTimeTypeStr() {
        return timeTypeStr;
    }
    public void setTimeTypeStr(String timeTypeStr) {
        this.timeTypeStr = timeTypeStr;
    }
//------------------------------ ��ʿ����� -------------------------------------------------
    /**
     * ���������ת����(ʱ:��:��)�ĸ�ʽ
     * @param ms ������
     * @return
     */
    public String millisecond2Date(long ms){
        int ss = 1000 ; //��
        int mi = ss*60; //��
        int hh = mi*60; //Сʱ
        long hour = ms/hh; //������Сʱ
        long minute = (ms - hour*hh)/mi; //�����ķ�
        long second = (ms - hour*hh - minute*mi)/ss; //��������
        if(ms <0){
            return String.valueOf(ms);
        }
        return (hour<10?"0"+hour:hour)+":"+(minute<10?"0"+minute:minute)+":"+(second<10?"0"+second:second);
    }
}
