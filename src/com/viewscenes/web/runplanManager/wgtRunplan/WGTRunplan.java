package com.viewscenes.web.runplanManager.wgtRunplan;

import java.util.ArrayList;

import org.jmask.web.controller.EXEException;

import com.viewscenes.bean.runplan.GJTRunplanBean;
import com.viewscenes.bean.runplan.WGTRunplanBean;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.util.StringTool;
import com.viewscenes.web.common.Common;
import com.viewscenes.web.runplanManager.gjtRunplan.GJTRunplanDAO;
import com.viewscenes.web.runplanManager.wgtRunplan.WGTRunplanDAO;

import flex.messaging.io.amf.ASObject;
/**
 * 外国台运行图类
 * @author leeo
 *
 */
public class WGTRunplan {
	
	/**
	 * 添加运行图操作
	 * @param bean
	 * @return
	 */
	public Object addRunplan(WGTRunplanBean bean){
		String message="";
		try {
			WGTRunplanDAO wgtdao = new WGTRunplanDAO();
			message=wgtdao.addRunplan(bean);
		} catch (Exception e) {
			e.printStackTrace();
			return new EXEException("", e.getMessage(), "");
		}
		return message;
	}
	

	
	 /**
	    * 查询运行图
	    * @param bean
	    * @return
	    */
		public Object queryRunplan(WGTRunplanBean bean){
			WGTRunplanDAO wgtdao = new WGTRunplanDAO();
			ASObject resObj;
			ArrayList result;
			try {
				 resObj = wgtdao.queryRunplan(bean);
				 ArrayList list =(ArrayList) resObj.get("resultList") ;//取出对象的结果集
				 String classpath = WGTRunplanBean.class.getName();
				 result = StringTool.convertFlexToJavaList(list, classpath);//将对象类型转换成bean，再放回结果集中。
				 resObj.put("resultList", result);
			} catch (Exception e) {
				e.printStackTrace();
				EXEException exe = new EXEException("", 
						e.getMessage(), "");
				return exe ;
			}
			
			return resObj;
		}
		
		/**
		 * 修改运行图
		 * @param bean
		 * @return
		 */
		public Object updateRunplan(WGTRunplanBean bean){
			WGTRunplanDAO wgtdao = new WGTRunplanDAO();
			String res="修改运行图成功!";
			String runplan_id = bean.getRunplan_id();   //运行图id
			//String runplan_type_id = bean.getRunplan_type_id(); //运行图类型id
			String broadcast_country = bean.getBroadcast_country();//播音国家
			String broadcast_station = bean.getBroadcast_station();//播音电台
			String launch_country = bean.getLaunch_country();//发射国家
			String station_id = bean.getStation_id();    //发射台id
			String station_name = bean.getStation_name(); //发射台
			String sentcity_id = bean.getSentcity_id();  //发射城市id
			String transmiter_no = bean.getTransmiter_no();  //发射机号
			String freq =bean.getFreq();      //频率
			String antenna = bean.getAntenna(); //天线号
			String antennatype = bean.getAntennatype();//天线程式
			String direction = bean.getDirection(); //方向
			String language_id = bean.getLanguage_id(); //语言id
			String power = bean.getPower();  //功率
			String service_area = bean.getService_area(); //服务区
			String ciraf = bean.getCiraf(); // CIRAF区
			String start_time = bean.getStart_time(); //播音开始时间
			String end_time = bean.getEnd_time();   //播音结束时间
			String mon_area = bean.getMon_area();    //遥控站收测
			String rest_datetime = bean.getRest_datetime(); // //休息日期
			String rest_time = bean.getRest_time(); //休息时间
			String valid_start_time = bean.getValid_start_time(); //启用期
			String valid_end_time = bean.getValid_end_time();  //停用期
			String remark = bean.getRemark();//备注
			String season_id = bean.getSeason_id();//季节代号
			String input_person = bean.getInput_person(); //录入人
			String[] times=wgtdao.getTimeDif(start_time, end_time, mon_area);
			StringBuffer sbf = new StringBuffer("update zres_runplan_tab set broadcast_country='"+broadcast_country+"',broadcast_station='"+broadcast_station+"',launch_country='"+launch_country+"', ");
			sbf.append(" station_id='"+station_id+"',STATION_NAME='"+station_name+"',sentcity_id='"+sentcity_id+"',TRANSMITER_NO='"+transmiter_no+"',FREQ='"+freq+"',");
			sbf.append("ANTENNA='"+antenna+"',ANTENNATYPE='"+antennatype+"',START_TIME='"+start_time+"',END_TIME='"+end_time+"',LOCAL_START_TIME='"+times[0]+"',LOCAL_END_TIME='"+times[1]+"',");
			sbf.append("LANGUAGE_ID='"+language_id+"',DIRECTION='"+direction+"',POWER='"+power+"',SERVICE_AREA='"+service_area+"',CIRAF='"+ciraf+"',");
			sbf.append("REMARK='"+remark+"',MON_AREA='"+mon_area+"',REST_DATETIME='"+rest_datetime+"',rest_time='"+rest_time+"',");
			sbf.append("VALID_START_TIME='"+valid_start_time+"',VALID_END_TIME='"+valid_end_time+"',STORE_DATETIME=sysdate,SEASON_ID='"+season_id+"',INPUT_PERSON='"+input_person+"',");
			sbf.append("IS_DELETE=0 where runplan_id='"+runplan_id+"' and RUNPLAN_TYPE_ID=3");
			try {
				DbComponent.exeUpdate(sbf.toString());
			} catch (Exception e) {
				e.printStackTrace();
				return new EXEException("",e.getMessage(),"");
			}
			return res;
		}
		
		/**
		 * 删除运行图
		 * @param ids
		 * @return
		 */
		public Object delRunplan(String ids){
			String message="删除运行图成功!";
			String sql="update zres_runplan_tab set is_delete=1 where runplan_type_id=3 and runplan_id in("+ids+")";
			try {
				DbComponent.exeUpdate(sql);
			} catch (Exception e) {
				e.printStackTrace();
				return new EXEException("","删除运行图异常"+e.getMessage(),"");
			}
			return message;
		}
}
