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
 * ���̨����ͼ��
 * @author leeo
 *
 */
public class WGTRunplan {
	
	/**
	 * �������ͼ����
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
	    * ��ѯ����ͼ
	    * @param bean
	    * @return
	    */
		public Object queryRunplan(WGTRunplanBean bean){
			WGTRunplanDAO wgtdao = new WGTRunplanDAO();
			ASObject resObj;
			ArrayList result;
			try {
				 resObj = wgtdao.queryRunplan(bean);
				 ArrayList list =(ArrayList) resObj.get("resultList") ;//ȡ������Ľ����
				 String classpath = WGTRunplanBean.class.getName();
				 result = StringTool.convertFlexToJavaList(list, classpath);//����������ת����bean���ٷŻؽ�����С�
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
		 * �޸�����ͼ
		 * @param bean
		 * @return
		 */
		public Object updateRunplan(WGTRunplanBean bean){
			WGTRunplanDAO wgtdao = new WGTRunplanDAO();
			String res="�޸�����ͼ�ɹ�!";
			String runplan_id = bean.getRunplan_id();   //����ͼid
			//String runplan_type_id = bean.getRunplan_type_id(); //����ͼ����id
			String broadcast_country = bean.getBroadcast_country();//��������
			String broadcast_station = bean.getBroadcast_station();//������̨
			String launch_country = bean.getLaunch_country();//�������
			String station_id = bean.getStation_id();    //����̨id
			String station_name = bean.getStation_name(); //����̨
			String sentcity_id = bean.getSentcity_id();  //�������id
			String transmiter_no = bean.getTransmiter_no();  //�������
			String freq =bean.getFreq();      //Ƶ��
			String antenna = bean.getAntenna(); //���ߺ�
			String antennatype = bean.getAntennatype();//���߳�ʽ
			String direction = bean.getDirection(); //����
			String language_id = bean.getLanguage_id(); //����id
			String power = bean.getPower();  //����
			String service_area = bean.getService_area(); //������
			String ciraf = bean.getCiraf(); // CIRAF��
			String start_time = bean.getStart_time(); //������ʼʱ��
			String end_time = bean.getEnd_time();   //��������ʱ��
			String mon_area = bean.getMon_area();    //ң��վ�ղ�
			String rest_datetime = bean.getRest_datetime(); // //��Ϣ����
			String rest_time = bean.getRest_time(); //��Ϣʱ��
			String valid_start_time = bean.getValid_start_time(); //������
			String valid_end_time = bean.getValid_end_time();  //ͣ����
			String remark = bean.getRemark();//��ע
			String season_id = bean.getSeason_id();//���ڴ���
			String input_person = bean.getInput_person(); //¼����
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
		 * ɾ������ͼ
		 * @param ids
		 * @return
		 */
		public Object delRunplan(String ids){
			String message="ɾ������ͼ�ɹ�!";
			String sql="update zres_runplan_tab set is_delete=1 where runplan_type_id=3 and runplan_id in("+ids+")";
			try {
				DbComponent.exeUpdate(sql);
			} catch (Exception e) {
				e.printStackTrace();
				return new EXEException("","ɾ������ͼ�쳣"+e.getMessage(),"");
			}
			return message;
		}
}
