package com.viewscenes.web.statistics.freq;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.viewscenes.bean.EffectStatCommonBean;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.printexcel.ExcelTitle;
import com.viewscenes.printexcel.JExcel;
import com.viewscenes.pub.GDSet;
import com.viewscenes.pub.GDSetException;
import com.viewscenes.util.StringTool;

import flex.messaging.io.amf.ASObject;

public class EffectStatByFreq {

	
	public Object statEffectByFreq(ASObject obj){
		
		ASObject resultObj = new ASObject();
		ArrayList<EffectStatCommonBean> resultList = new ArrayList<EffectStatCommonBean>();
		String xml ="<chart yAxisName='可听率 单位:(%)' xAxisName='频率' caption='基于频率的效果统计可听率' baseFontSize='15' showBorder='1' imageSave='1'>";
//		String[] statTypes = new String[]{"playTime","half","hour"};//播音时段统计;半小时统计;整小时统计;
		
		//资料来源
		String datasource = (String)obj.get("datasource");	
		//运行图类型
		String runplanType = (String)obj.get("runplanType");
		//频率
		String freq = (String)obj.get("freq");
		//收测地区
		String receiveArea = (String)obj.get("receiveArea");
		//语言
		String language_id = (String)obj.get("language_id");
		//发射台  运行类型为:国际台时 采用该字段条件
		String station_id = (String)obj.get("station_id");
		//转播机构  运行图类型为：海外落地时 采用该字段条件
		String station_name = (String)obj.get("station_name");
		//收测时间从
		String receive_time_start = (String)obj.get("receive_time_start");
		//收测时间到
		String receive_time_end = (String)obj.get("receive_time_end");
		//统计方式:半小时，整小时，播音时间
		String statType = (String)obj.get("statType");
		
		//分别用数级中的不同位置代表用户输入的不同查询条件
		String[] group = new String[5];
		
		
		StringBuffer sql = null;
		//站点收测统计
		if (datasource.equals("1")){
			sql = new StringBuffer();
			sql.append("  select distinct t.frequency,run.language_name, ");
			sql.append("  t.station_name,run.service_area,trunc(t.mark_datetime) mark_datetime,counto,run.start_time,run.end_time, zrdt.disturb,t.level_value ,t.play_time");
			sql.append("  from radio_mark_zst_view_tab t,zres_runplan_disturb_tab zrdt, ");
			sql.append("  (select distinct t.*, lan.language_name ");
			sql.append("  from zres_runplan_chaifen_tab t, ");
			sql.append("  (select * from zdic_language_tab where is_delete = 0) lan ");
			sql.append("  where 1 = 1 ");
			//sql.append("  and t.language_id = lan.language_id(+)) run  ");
			sql.append("  and t.language_id = lan.language_id) run  ");
			//sql.append("  where t.runplan_id = run.runplan_id(+) and run.parent_id=zrdt.runplan_id(+) and t.counto is not null and t.counti is not null and t.counts is not null ");
			
			sql.append("  where t.runplan_id = run.runplan_id and run.parent_id=zrdt.runplan_id and t.counto is not null and t.counti is not null and t.counts is not null ");
			sql.append("  and run.runplan_type_id = "+runplanType+" ");
			
			if (!StringTool.isNull(freq)){
				group[0] = "0";
				sql.append("  and t.frequency = "+freq+" ");
			}
			if (!StringTool.isNull(receiveArea)){
				group[1] = "1";
				sql.append("  and (run.service_area like '%"+receiveArea+"%' ) ");//or run.service_area like '%U%'
			}
			if (!StringTool.isNull(language_id)){
				group[2] = "2";
				sql.append("  and run.language_id = "+language_id+" ");
			}
			//海外落地运行图
			if (runplanType.equals("2")){
			
				if (!StringTool.isNull(station_name)){
					group[3] = "3";
					sql.append("  and t.station_name = '"+station_name+"' ");
				}
			//国际台运行图	
			}else {
				if (!StringTool.isNull(station_id)){
					group[3] = "3";
					sql.append("  and t.station_id = "+station_id+" ");
				}
			}
			
			if (!StringTool.isNull(receive_time_start) && !StringTool.isNull(receive_time_end)){
				group[4] = "4";
				sql.append("  and t.mark_datetime >= to_date('"+receive_time_start+" 00:00:00','yyyy-mm-dd hh24:mi:ss') and t.mark_datetime <= to_date('"+receive_time_end+" 23:59:59','yyyy-mm-dd hh24:mi:ss') ");
			}
			sql.append("  order by t.frequency,language_name,t.station_name,start_time ");
		}else{
			//资料录入
			sql = new StringBuffer();
			sql.append(" select distinct t.freq frequency,t.language_name, ");
			sql.append(" t.station_name,t.service_area,trunc(t.receive_date) mark_datetime,t.o counto,t.start_time,t.end_time,'' as disturb,t.level_value,t.receive_time ");
			sql.append(" from res_datainput_tab t ");
			sql.append("  where 1 = 1 ");
			
			if (!StringTool.isNull(freq)){
				group[0] = "0";
				sql.append("  and t.freq = "+freq+" ");
			}
			if (!StringTool.isNull(receiveArea)){
				group[1] = "1";
				sql.append("  and (t.service_area like '%"+receiveArea+"%' ) ");//or run.service_area like '%U%'
			}
			if (!StringTool.isNull(language_id)){
				group[2] = "2";
				sql.append("  and t.language_id = "+language_id+" ");
			}
			
			//海外落地运行图
			if (runplanType.equals("2")){
			
				if (!StringTool.isNull(station_name)){
					group[3] = "3";
					sql.append("  and t.station_name = "+station_name+" ");
				}
			//国际台运行图	
			}else {
				if (!StringTool.isNull(station_id)){
					group[3] = "3";
					sql.append("  and t.station_id = "+station_id+" ");
				}
			}
			
			if (!StringTool.isNull(receive_time_start) && !StringTool.isNull(receive_time_end)){
				group[4] = "4";
				sql.append("  and t.receive_date >= to_date('"+receive_time_start+" 00:00:00','yyyy-mm-dd hh24:mi:ss') and t.receive_date <= to_date('"+receive_time_end+" 23:59:59','yyyy-mm-dd hh24:mi:ss') ");
			}
			
			sql.append("  order by t.freq,language_name,t.station_name ");
			
		}

			
		try {
			GDSet set = null;
			set = DbComponent.Query(sql.toString());
			HashMap<String,ArrayList<EffectStatCommonBean>> groupMap = new HashMap<String,ArrayList<EffectStatCommonBean>>();
			ArrayList<String> keyList=new ArrayList<String>();
			String key = "";
			//1.分组存放数据。
			for(int i=0;i<set.getRowCount();i++){
				EffectStatCommonBean result = new EffectStatCommonBean();
				String _freq = set.getString(i, "frequency");
				String _language_name = set.getString(i, "language_name");
				String _station_name = set.getString(i, "station_name");
				String _service_area = set.getString(i, "service_area");
				String _mark_datetime = set.getString(i, "mark_datetime");
				String _counto = set.getString(i, "counto");
				String _start_time = set.getString(i, "start_time");
				String _end_time = set.getString(i, "end_time");
				String _disturb = set.getString(i, "disturb");
				String _levelValue = set.getString(i, "level_value");
				String _play_time="";
				if(datasource.equals("1")){
					_play_time=set.getString(i, "play_time");
				}else{
					String receive_time=set.getString(i, "receive_time");
					if(receive_time!=null&&!receive_time.equalsIgnoreCase("")){
						int n=Integer.parseInt(receive_time.split(":")[0]);
						_play_time=n+":00-"+(n+1)+":00";
					}else{
						
						_play_time="00:00";
						
					}
					
				}
				key = _freq+_language_name+_start_time+_station_name+_play_time;
				
				result.setFreq(_freq);
				result.setLanguage_name(_language_name);
				result.setStation_name(_station_name);
				result.setRebroadcastorg(_station_name);
				result.setReceivearea(_service_area);
				result.setReceivedate(_play_time);
				result.setO(_counto);
				result.setStart_time(_start_time);
				result.setEnd_time(_end_time);
				result.setDisturb(_disturb);
				//等于空或者小于0默认为0
				if(_levelValue.equals("")||Double.valueOf(_levelValue)<0)
				{
					result.setLevel_value(0);
				}else 
				{
					result.setLevel_value(Double.valueOf(_levelValue));
				}
				//result.setLevel_value(Double.valueOf(_levelValue.equals("")?"0":_levelValue));
				//没有选择任何统计条件,随机为每条数据生成一个ID
				
				ArrayList<EffectStatCommonBean> list = new ArrayList<EffectStatCommonBean>();
				if (!statType.equals("playTime")){
					String[] ret = StringTool.getPlayTimeList(result.getStart_time(), result.getEnd_time(), statType.equals("half")?0:1);
					if (ret != null)
						for(int j=0;j<ret.length;j++){
							EffectStatCommonBean tmp = result.clone();
							tmp.setStart_time(ret[j].split("-")[0]);
							tmp.setEnd_time(ret[j].split("-")[1]);
							list.add(tmp);
						}
				}else{
					list.add(result);
				}
				
				//按照分组的键值进行分组存放
				if (!keyList.contains(key)){
					keyList.add(key);
					groupMap.put(key, list);
				}else{
					ArrayList<EffectStatCommonBean> _list = groupMap.get(key);
					_list.addAll(list);
					groupMap.put(key, _list);
				}
				
			}
			
			for(String o : keyList){ 
				ArrayList<EffectStatCommonBean> list = groupMap.get(o);
				//记录不同分数(5,4,3,2,1)的总次数
				int count1 = 0;
				int count2 = 0;
				int count3 = 0;
				int count4 = 0;
				int count5 = 0;
				
				//总收测次数
				int count = list.size();
				//不同分数的可听度满意程度
				//(可听度的满意程度中若“5”栏中填8，则表示有80—89%的时间可听度达到5分；“4”栏中填1，则表示有10—19%的时间可听度达到4分。)
				int audibility1 = 0;
				int audibility2 = 0;
				int audibility3 = 0;
				int audibility4 = 0;
				int audibility5 = 0;
				//可听度中值
				int audibilityMiddleValue = 0;
				//可听率(可听度大于等于3分次数与总收听次数之比)
				int audible = 0;
				//电平中值
				double levelMiddleValue = 0;
				//电平中值总和
				double levelSum = 0;
				for(int i=0;i<list.size();i++){
					EffectStatCommonBean result = list.get(i);
					result.getStart_time();
					result.getEnd_time();
					//5，4，3，2，1分值的次数
					int oo =0;
					if(result.getO()!=null&&!result.getO().equalsIgnoreCase("")){
						 oo = Integer.valueOf(result.getO());
					}
					//int oo = Integer.valueOf(result.getO());
					switch(oo){
						case 1:count1++; break;
						case 2:count2++; break;
						case 3:count3++; break;
						case 4:count4++; break;
						case 5:count5++; break;
						default:;
					}
					levelSum += Double.valueOf(result.getLevel_value());
				}
				if (count >0){
					//1.计算可听度满意度
					audibility1 = (int)(((double)count1/count)*10);
					audibility2 = (int)(((double)count2/count)*10);
					audibility3 = (int)(((double)count3/count)*10);
					audibility4 = (int)(((double)count4/count)*10);
					audibility5 = (int)(((double)count5/count)*10);
					
					//2.计算可听度中值
					int t1 = count/2;
					int t2 = count%2;
					//证明记录总条数为奇数,中间值(t1+t2),否则取t1与t1+1之和的中间值
					if (t2 ==1){
						EffectStatCommonBean tmp = list.get(t1+t2-1);
						
						if(tmp.getO()!=null&&!tmp.getO().equalsIgnoreCase("")){
							audibilityMiddleValue = Integer.valueOf(tmp.getO());
						}else{
							audibilityMiddleValue = 0;
						}
					}else{
						EffectStatCommonBean tmp1 = list.get(t1-1);
						EffectStatCommonBean tmp2 = list.get(t1);
						int a = Integer.valueOf(tmp1.getO());
						int b = Integer.valueOf(tmp2.getO());
						audibilityMiddleValue = (a+b)/2;
					}
					//3.计算可听率 可听度大于等于3分次数与总收听次数之比，用百分数表示
					audible = (Integer)(count3 + count4 + count5)*100/count;
//					if(audible==1.0){
//						audible=100;
//					}
					EffectStatCommonBean resultBean = list.get(0);
					resultBean.setAudibility1(audibility1);
					resultBean.setAudibility2(audibility2);
					resultBean.setAudibility3(audibility3);
					resultBean.setAudibility4(audibility4);
					resultBean.setAudibility5(audibility5);
					resultBean.setAudibilityMiddleValue(audibilityMiddleValue);
					resultBean.setAudible(audible);
					resultBean.setReceivecount(count);
					
					//4.添加备注信息
					//备注信息由同邻频干扰情况信息表代入，对于可听率低于60%未代入同邻频干扰信息的需要在备注信息中直接加上信号弱，杂音大
					if (audible*100 <60 && resultBean.getDisturb().equals("")){
						resultBean.setDisturb("信号弱，杂音大");
					}
					//5.计算电平中值,取记录平均值
					DecimalFormat formart=new DecimalFormat("#");
					levelMiddleValue = Double.parseDouble(formart.format(levelSum/count));
					levelMiddleValue = (double)Math.round(levelMiddleValue*100)/100;
					resultBean.setLevel_value(levelMiddleValue);
					resultList.add(resultBean);
					xml+="<set label='"+resultBean.getFreq()+"' value='"+audible+"'/>\r";
				}
				
			} 

			 xml+="</chart>";
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GDSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		resultObj.put("xml", xml);
		resultObj.put("result", resultList);
		return resultObj;
	       
	}
	
	
	
	 /**
	 02
	    * @param raidus 单位米
	 03
	    * return minLat,minLng,maxLat,maxLng
	 04
	    */
	 
	   public static double[] getAround(double lat, double lon, int raidus) {
	 
	  
	 
	       Double latitude = lat;
	 
	       Double longitude = lon;
	 
	  
	 
	       Double degree = (24901 * 1609) / 360.0;
	 
	       double raidusMile = raidus;
	 
	  
	 
	       Double dpmLat = 1 / degree;
	 
	       Double radiusLat = dpmLat * raidusMile;
	 
	       Double minLat = latitude - radiusLat;
	 
	       Double maxLat = latitude + radiusLat;
	 
	  
	 
	       Double mpdLng = degree * Math.cos(latitude * (Math.PI / 180));
	 
	       Double dpmLng = 1 / mpdLng;
	 
	       Double radiusLng = dpmLng * raidusMile;
	 
	       Double minLng = longitude - radiusLng;
	 
	       Double maxLng = longitude + radiusLng;
	 
	       return new double[]{minLat, minLng, maxLat, maxLng};
	 
	   }

	 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		double[] d = getAround(40.0557,116.4125,800);
		int a = 7;
		int b = 8;
		int aa = 2/2;
		
		System.out.println(aa);
//		StringTool.getPlayTimeList("2200","0130",1);
		
	}
	
	
	
	
	
	public void toExcel(HttpServletRequest request,HttpServletResponse response){
		JExcel jExcel = new JExcel();
		ExcelTitle et1 = new ExcelTitle("ID");
		ExcelTitle et1_1 = new ExcelTitle("id_1");
		ExcelTitle et1_2 = new ExcelTitle("id_2",jExcel.titleCellFormat);
		ExcelTitle et2 = new ExcelTitle("name",jExcel.titleCellFormat);
		
		try {
			et1.setSubTitles(new ExcelTitle[]{et1_1,et1_2});
			
			ExcelTitle[] ets = new ExcelTitle[]{et1,et2};
			jExcel.openDocument();
			jExcel.setFilename("sb");
			
			jExcel.setTitle(0, 2, ets);
			jExcel.saveDocument();
			
			jExcel.postExcel(request, response);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		
	}

}
