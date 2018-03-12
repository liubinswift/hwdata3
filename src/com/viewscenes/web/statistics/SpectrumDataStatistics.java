package com.viewscenes.web.statistics;

import java.util.ArrayList;

import com.viewscenes.bean.SpectrumBean;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.pub.GDSet;
import com.viewscenes.pub.GDSetException;

import flex.messaging.io.amf.ASObject;

/**
 * *************************************   
*    
* 项目名称：hwdata   
* 类名称：SpectrumDataStatistics   
* 类描述：   用来统计频谱数据.
* 创建人：刘斌
* 创建时间：2013-3-13 下午03:07:08   
* 修改人：刘斌
* 修改时间：2013-3-13 下午03:07:08   
* 修改备注：   
* @version    
*    
***************************************
 */
public class SpectrumDataStatistics {
	  public static Object spectrumDataStatistics(ASObject obj)
	  {
		  ASObject objRes = new ASObject();
		  ArrayList asObject=new ArrayList();
		    String reportType = (String)obj.get("head_type");//
	String starttime = (String)obj.get("date")+" 00:00:00";//开始时间
	String endtime =  (String)obj.get("date")+" 23:59:59";//结束时间
	String type =  (String)obj.get("type");//统计类型：按频率统计，按发射台统计。
	String headnames=(String)obj.get("headnames");
	String flag="";
	 if(type.equals("0"))
	 {
	    	  flag=  " order by t.station_name ,INPUT_DATETIME asc ";
	 }else
	 {

	    	  flag=  " order by t.freq ,INPUT_DATETIME asc ";
	 }
	 if(headnames!=null&&headnames.length()>0)
	 {
		 headnames=" and shortname in('"+headnames.replaceAll(",", "','")+"')";
	 }else
	 {
		 headnames="";
	 }
	String sql="select * from RADIO_SPECTRUM_STAT_TAB t where  " +
			" INPUT_DATETIME>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss') " +
			"and INPUT_DATETIME<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') " +
			"and HEADTYPE="+reportType+headnames+flag;
	try {
				GDSet gd = DbComponent.Query(sql);
				String flagStation_name="";
				String flagFreq="";
				SpectrumBean bean=new SpectrumBean();
				for(int i=0;i<gd.getRowCount();i++)
				{
					if(type.equals("0"))//发射台统计
					{
						 if(!flagStation_name.equals(gd.getString(i, "station_name"))&&i!=0)
							{
								asObject.add(bean);
								bean=new SpectrumBean();		
							}
					bean.setStation_name(gd.getString(i, "station_name"));
					//bean.setFreq(gd.getString(i, "freq"));
					setValue(bean,gd.getString(i, "shortname"),gd.getString(i, "language_name"),gd.getString(i, "input_datetime"));
					flagStation_name=gd.getString(i, "station_name");
					}else//按频率统计
					{
						 if(!flagFreq.equals(gd.getString(i, "freq"))&&i!=0)
							{
								asObject.add(bean);
								bean=new SpectrumBean();		
							}
					//bean.setStation_name(gd.getString(i, "station_name"));
					bean.setFreq(gd.getString(i, "freq"));
					setValue(bean,gd.getString(i, "shortname"),gd.getString(i, "language_name"),gd.getString(i, "input_datetime"));
					flagFreq=gd.getString(i, "freq");
					}
					    
				}
				if(gd.getRowCount()>0)
				{
				  asObject.add(bean);
				}
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (GDSetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			objRes.put("resultList", asObject);
			
			objRes.put("resultTotal", asObject.size());
	return objRes;
	   }
/**
 * ************************************************

* @Title: setValue

* @Description: TODO(設定值)

* @param @param bean
* @param @param string
* @param @param string2    设定文件

* @author  刘斌

* @return void    返回类型

* @throws

************************************************
 */
	private static void setValue(SpectrumBean bean, String head_name,
			String language_name,String check_time) {
		// TODO Auto-generated method stub
		String unit=check_time.substring(11, 13);
		String minutes=check_time.substring(14, 16);
		
		if(unit.equals("00"))
		{
		if(minutes.compareTo("30")<0)
			{
				bean.setTime0(head_name+"["+language_name+"]|无");
			}else
			{
				if(bean.getTime0()!=null&&!bean.getTime0().equals(""))
				{
					
					bean.setTime0(bean.getTime0().substring(0,bean.getTime0().length()-2)+"|"+head_name+"["+language_name+"]");	
				}else
				{
					bean.setTime0("无"+"|"+head_name+"["+language_name+"]");
				}
			}
		}else if(unit.equals("01"))
		{
		if(minutes.compareTo("30")<0)
			{
				bean.setTime1(head_name+"["+language_name+"]|无");
			}else
			{
				if(bean.getTime1()!=null&&!bean.getTime1().equals(""))
				{
					bean.setTime1(bean.getTime1().substring(0,bean.getTime1().length()-2)+"|"+head_name+"["+language_name+"]");	
				}else
				{
					bean.setTime1("无"+"|"+head_name+"["+language_name+"]");
				}
			}
		}else if(unit.equals("02"))
		{
		if(minutes.compareTo("30")<0)
			{
				bean.setTime2(head_name+"["+language_name+"]|无");
			}else
			{
				if(bean.getTime2()!=null&&!bean.getTime2().equals(""))
				{
					bean.setTime2(bean.getTime2().substring(0,bean.getTime2().length()-2)+"|"+head_name+"["+language_name+"]");	
				}else
				{
					bean.setTime2("无"+"|"+head_name+"["+language_name+"]");
				}
			}
		}else if(unit.equals("03"))
		{
		if(minutes.compareTo("30")<0)
			{
				bean.setTime3(head_name+"["+language_name+"]|无");
			}else
			{
				if(bean.getTime3()!=null&&!bean.getTime3().equals(""))
				{
					bean.setTime3(bean.getTime3().substring(0,bean.getTime3().length()-2)+"|"+head_name+"["+language_name+"]");	
				}else
				{
					bean.setTime3("无"+"|"+head_name+"["+language_name+"]");
				}
			}
		}else if(unit.equals("04"))
		{
		if(minutes.compareTo("30")<0)
			{
				bean.setTime4(head_name+"["+language_name+"]|无");
			}else
			{
				if(bean.getTime4()!=null&&!bean.getTime4().equals(""))
				{
					bean.setTime4(bean.getTime4().substring(0,bean.getTime4().length()-2)+"|"+head_name+"["+language_name+"]");	
				}else
				{
					bean.setTime4("无"+"|"+head_name+"["+language_name+"]");
				}
			}
		}else if(unit.equals("05"))
		{
		if(minutes.compareTo("30")<0)
			{
				bean.setTime5(head_name+"["+language_name+"]|无");
			}else
			{
				if(bean.getTime5()!=null&&!bean.getTime5().equals(""))
				{
					bean.setTime5(bean.getTime5().substring(0,bean.getTime5().length()-2)+"|"+head_name+"["+language_name+"]");	
				}else
				{
					bean.setTime6("无"+"|"+head_name+"["+language_name+"]");
				}
			}
		}else if(unit.equals("06"))
		{
		if(minutes.compareTo("30")<0)
			{
				bean.setTime6(head_name+"["+language_name+"]|无");
			}else
			{
				if(bean.getTime6()!=null&&!bean.getTime6().equals(""))
				{
					bean.setTime6(bean.getTime6().substring(0,bean.getTime6().length()-2)+"|"+head_name+"["+language_name+"]");	
				}else
				{
					bean.setTime6("无"+"|"+head_name+"["+language_name+"]");
				}
			}
		}
		else if(unit.equals("07"))
		{
			if(minutes.compareTo("30")<0)
			{
				bean.setTime7(head_name+"["+language_name+"]|无");
			}else
			{
				if(bean.getTime7()!=null&&!bean.getTime7().equals(""))
				{
					bean.setTime7(bean.getTime7().substring(0,bean.getTime7().length()-2)+"|"+head_name+"["+language_name+"]");	
				}else
				{
					bean.setTime7("无"+"|"+head_name+"["+language_name+"]");
				}
			}
		}else if(unit.equals("08"))
		{
		if(minutes.compareTo("30")<0)
			{
				bean.setTime8(head_name+"["+language_name+"]|无");
			}else
			{
				if(bean.getTime8()!=null&&!bean.getTime8().equals(""))
				{
					bean.setTime8(bean.getTime8().substring(0,bean.getTime8().length()-2)+"|"+head_name+"["+language_name+"]");	
				}else
				{
					bean.setTime8("无"+"|"+head_name+"["+language_name+"]");
				}
			}
		}else if(unit.equals("09"))
		{
		if(minutes.compareTo("30")<0)
			{
				bean.setTime9(head_name+"["+language_name+"]|无");
			}else
			{
				if(bean.getTime9()!=null&&!bean.getTime9().equals(""))
				{
					bean.setTime9(bean.getTime9().substring(0,bean.getTime9().length()-2)+"|"+head_name+"["+language_name+"]");	
				}else
				{
					bean.setTime9("无"+"|"+head_name+"["+language_name+"]");
				}
			}
		}else if(unit.equals("10"))
		{
		if(minutes.compareTo("30")<0)
			{
				bean.setTime10(head_name+"["+language_name+"]|无");
			}else
			{
				if(bean.getTime10()!=null&&!bean.getTime10().equals(""))
				{
					bean.setTime10(bean.getTime10().substring(0,bean.getTime10().length()-2)+"|"+head_name+"["+language_name+"]");	
				}else
				{
					bean.setTime10("无"+"|"+head_name+"["+language_name+"]");
				}
			}
		}else if(unit.equals("11"))
		{
		if(minutes.compareTo("30")<0)
			{
				bean.setTime11(head_name+"["+language_name+"]|无");
			}else
			{
				if(bean.getTime11()!=null&&!bean.getTime11().equals(""))
				{
					bean.setTime11(bean.getTime11().substring(0,bean.getTime11().length()-2)+"|"+head_name+"["+language_name+"]");	
				}else
				{
					bean.setTime11("无"+"|"+head_name+"["+language_name+"]");
				}
			}
		}else if(unit.equals("12"))
		{
		if(minutes.compareTo("30")<0)
			{
				bean.setTime12(head_name+"["+language_name+"]|无");
			}else
			{
				if(bean.getTime12()!=null&&!bean.getTime12().equals(""))
				{
					bean.setTime12(bean.getTime12().substring(0,bean.getTime12().length()-2)+"|"+head_name+"["+language_name+"]");	
				}else
				{
					bean.setTime12("无"+"|"+head_name+"["+language_name+"]");
				}
			}
		}else if(unit.equals("13"))
		{
		if(minutes.compareTo("30")<0)
			{
				bean.setTime13(head_name+"["+language_name+"]|无");
			}else
			{
				if(bean.getTime13()!=null&&!bean.getTime13().equals(""))
				{
					bean.setTime13(bean.getTime13().substring(0,bean.getTime13().length()-2)+"|"+head_name+"["+language_name+"]");	
				}else
				{
					bean.setTime13("无"+"|"+head_name+"["+language_name+"]");
				}
			}
		}else if(unit.equals("14"))
		{
		if(minutes.compareTo("30")<0)
			{
				bean.setTime14(head_name+"["+language_name+"]|无");
			}else
			{
				if(bean.getTime14()!=null&&!bean.getTime14().equals(""))
				{
					bean.setTime14(bean.getTime14().substring(0,bean.getTime14().length()-2)+"|"+head_name+"["+language_name+"]");	
				}else
				{
					bean.setTime14("无"+"|"+head_name+"["+language_name+"]");
				}
			}
		}else if(unit.equals("15"))
		{
		if(minutes.compareTo("30")<0)
			{
				bean.setTime15(head_name+"["+language_name+"]|无");
			}else
			{
				if(bean.getTime15()!=null&&!bean.getTime15().equals(""))
				{
					bean.setTime15(bean.getTime15().substring(0,bean.getTime15().length()-2)+"|"+head_name+"["+language_name+"]");	
				}else
				{
					bean.setTime15("无"+"|"+head_name+"["+language_name+"]");
				}
			}
		}else if(unit.equals("16"))
		{
		if(minutes.compareTo("30")<0)
			{
				bean.setTime16(head_name+"["+language_name+"]|无");
			}else
			{
				if(bean.getTime16()!=null&&!bean.getTime16().equals(""))
				{
					bean.setTime16(bean.getTime16().substring(0,bean.getTime16().length()-2)+"|"+head_name+"["+language_name+"]");	
				}else
				{
					bean.setTime16("无"+"|"+head_name+"["+language_name+"]");
				}
			}
		}else if(unit.equals("17"))
		{
		if(minutes.compareTo("30")<0)
			{
				bean.setTime17(head_name+"["+language_name+"|无");
			}else
			{
				if(bean.getTime17()!=null&&!bean.getTime17().equals(""))
				{
					bean.setTime17(bean.getTime17().substring(0,bean.getTime17().length()-2)+"|"+head_name+"["+language_name+"]");	
				}else
				{
					bean.setTime17("无"+"|"+head_name+"["+language_name+"]");
				}
			}
		}else if(unit.equals("18"))
		{
		if(minutes.compareTo("30")<0)
			{
				bean.setTime18(head_name+"["+language_name+"]|无");
			}else
			{
				if(bean.getTime18()!=null&&!bean.getTime18().equals(""))
				{
					bean.setTime18(bean.getTime18().substring(0,bean.getTime18().length()-2)+"|"+head_name+"["+language_name+"]");	
				}else
				{
					bean.setTime18("无"+"|"+head_name+"["+language_name+"]");
				}
			}
		}else if(unit.equals("19"))
		{
		if(minutes.compareTo("30")<0)
			{
				bean.setTime19(head_name+"["+language_name+"]|无");
			}else
			{
				if(bean.getTime19()!=null&&!bean.getTime19().equals(""))
				{
					bean.setTime19(bean.getTime19().substring(0,bean.getTime19().length()-2)+"|"+head_name+"["+language_name+"]");	
				}else
				{
					bean.setTime19("无"+"|"+head_name+"["+language_name+"]");
				}
			}
		}else if(unit.equals("20"))
		{
		if(minutes.compareTo("30")<0)
			{
				bean.setTime20(head_name+"["+language_name+"]|无");
			}else
			{
				if(bean.getTime20()!=null&&!bean.getTime20().equals(""))
				{
					bean.setTime20(bean.getTime20().substring(0,bean.getTime20().length()-2)+"|"+head_name+"["+language_name+"]");	
				}else
				{
					bean.setTime20("无"+"|"+head_name+"["+language_name+"]");
				}
			}
		}else if(unit.equals("21"))
		{
		if(minutes.compareTo("30")<0)
			{
				bean.setTime21(head_name+"["+language_name+"]|无");
			}else
			{
				if(bean.getTime21()!=null&&!bean.getTime21().equals(""))
				{
					bean.setTime21(bean.getTime21().substring(0,bean.getTime21().length()-2)+"|"+head_name+"["+language_name+"]");	
				}else
				{
					bean.setTime21("无"+"|"+head_name+"["+language_name+"]");
				}
			}
		}else if(unit.equals("22"))
		{
		if(minutes.compareTo("30")<0)
			{
				bean.setTime22(head_name+"["+language_name+"]|无");
			}else
			{
				if(bean.getTime22()!=null&&!bean.getTime22().equals(""))
				{
					bean.setTime22(bean.getTime22().substring(0,bean.getTime22().length()-2)+"|"+head_name+"["+language_name+"]");	
				}else
				{
					bean.setTime22("无"+"|"+head_name+"["+language_name+"]");
				}
			}
		}else if(unit.equals("23"))
		{
		if(minutes.compareTo("30")<0)
			{
				bean.setTime23(head_name+"["+language_name+"]|无");
			}else
			{
				if(bean.getTime23()!=null&&!bean.getTime23().equals(""))
				{
					bean.setTime23(bean.getTime23().substring(0,bean.getTime23().length()-2)+"|"+head_name+"["+language_name+"]");	
				}else
				{
					bean.setTime23("无"+"|"+head_name+"["+language_name+"]");
				}
			}
		}
	}
	public static void main(String[] args)
	{
		String check_time="2013-01-02 01:02:33";
		String unit=check_time.substring(11, 13);
		String minutes=check_time.substring(14, 16);
		System.out.println(unit+" "+minutes);
		System.out.println("02".compareTo("02"));
		
	}
}
