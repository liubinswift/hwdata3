package com.viewscenes.timeTask;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;

import java.util.Date;

import com.viewscenes.bean.ReportBean;



public class AlarmClock 
{

    private final Scheduler scheduler = new Scheduler();//调度器
    private final SimpleDateFormat dateFormat =
        new SimpleDateFormat("dd MMM yyyy HH:mm:ss.SSS");
    private final int hourOfDay, minute, second;//每天触发的时间点 

    public AlarmClock(int hourOfDay, int minute, int second) 
    {
        this.hourOfDay = hourOfDay;
        this.minute = minute;
        this.second = second;
    }

    public void start() 
    {
        scheduler.schedule(new SchedulerTask() 
        {
            public void run() 
            {
                soundAlarm();
            }
            private void soundAlarm()
            {
                System.out.println("Wake up! " +
                    "It's " + dateFormat.format(new Date()));
                // 这里添加每个人需要定时计算的方法就可以了。
                /**
                 * 指标每天统计
                 */
                QualityDataStatistics();
                /**
                 * 异态每天统计
                 */
                YiTaiDataStatistics();
            }
        }, new DailyIterator(hourOfDay, minute, second));//通过迭代器模式迭代遍历得到后面一系列的时间点
    }
/**
 * ************************************************

* @Title: QualityDataStatistics

* @Description: TODO(指标数据计算)

* @param     设定文件

* @author  刘斌

* @return void    返回类型

* @throws

************************************************
 */
    public static void QualityDataStatistics()
    {
    	System.out.println("OK++++++++++++++++++++==");
    }
   /**
    * ************************************************
   
   * @Title: YiTaiDataStatistics
   
   * @Description: TODO(异态每天统计)
   
   * @param     设定文件
   
   * @author  刘斌
   
   * @return void    返回类型
   
   * @throws
   
   ************************************************
    */
    public static void YiTaiDataStatistics()
    {
    	System.out.println("OK++++++++++++++++++++==");
    } 
    public static void main(String[] args) 
    {
       AlarmClock alarmClock = new AlarmClock(16, 46, 0);
//       alarmClock.start();
    	// AlarmClock alarmClock = new AlarmClock(hourOfDay, hourOfDay, hourOfDay);
       ReportBean a=new ReportBean();
       a.setReport_id("110");
       
       Method[] methods = null;
	try {
		try {
			methods = Class.forName("com.viewscenes.bean.ZRST_REP_ABNORMAL_BEAN").newInstance().getClass().getDeclaredMethods();
			   
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} catch (SecurityException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (ClassNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}//根据Class对象获得属性 私有的也可以获得
    Class cl;
	try {
		cl = Class.forName("com.viewscenes.bean.ZRST_REP_ABNORMAL_BEAN");
		   try {
			Field[] Fields=cl.newInstance().getClass().getFields();
			 for(Field f:Fields)	
			 {
				 System.out.println(f.getName());
			 }
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//根据Class对象获得属性 私有的也可以获得
		   
	} catch (ClassNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
    
 
       Field[]  aaaFields=a.getClass().getFields();
       for(Method me:methods)			{	
    	  
    	   try {
    		 //  System.out.println(me.getName());
			//System.out.println(me.invoke(a, new Object[]{}));
		
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	
    	   
    	   }
    
      
    }
}
