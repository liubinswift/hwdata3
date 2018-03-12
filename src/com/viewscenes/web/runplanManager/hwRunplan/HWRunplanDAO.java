package com.viewscenes.web.runplanManager.hwRunplan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.viewscenes.bean.runplan.GJTLDRunplanBean;
import com.viewscenes.bean.runplan.GJTRunplanBean;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.pub.GDSet;
import com.viewscenes.util.StringTool;
import com.viewscenes.web.common.Common;

import flex.messaging.io.amf.ASObject;

public class HWRunplanDAO {
	
	
	/**
	 * ����ͼ������
	 * @param bean
	 * @return
	 * @throws DbException
	 */
	public String addRunplan(GJTLDRunplanBean bean) throws DbException{
		String res="�������ͼ�ɹ�!";
		String input_person = bean.getInput_person();//¼����
		String runplan_type_id = bean.getRunplan_type_id(); //����ͼ����id
		String launch_country = bean.getLaunch_country();    //�������
		String sentcity_id = bean.getSentcity_id();  //�������id
		String redisseminators = bean.getRedisseminators();  //ת������
		String freq =bean.getFreq();      //Ƶ��
		String band =bean.getBand();      //����
		String language_id = bean.getLanguage_id();//����id
		String direction = bean.getDirection(); //����
		
		String power = bean.getPower();  //����
		String service_area = bean.getService_area(); //������
		//String local_time = bean.getLocal_time();//����ʱ��
		String rest_datetime = bean.getRest_datetime(); // //��Ϣ����
	    String summer = bean.getSummer();//����ʱ
//	    String summer_starttime = bean.getSummer_starttime();//����ʱ��ʼʱ��
//	    String summer_endtime = bean.getSummer_endtime();//����ʱ����ʱ��
		String start_time = bean.getStart_time(); //������ʼʱ��
		String end_time = bean.getEnd_time();   //��������ʱ��
		String mon_area = bean.getMon_area();    //�����ղ�վ��
		String xg_mon_area = bean.getXg_mon_area();//Ч���ղ�վ��
		String valid_start_time = bean.getValid_start_time(); //������
		String valid_end_time = bean.getValid_end_time();  //ͣ����
		String remark = bean.getRemark();//��ע
		String weekday = bean.getWeekday();//������
		String[] times = Common.getTimeDif(start_time,end_time,launch_country);//����ʱ����㵱��ʱ��
		StringBuffer addsql = new StringBuffer("insert into zres_runplan_tab (runplan_id,launch_country,sentcity_id,redisseminators,freq,band,runplan_type_id,");
		             addsql.append("language_id,direction,start_time,end_time,power,service_area,local_start_time,local_end_time,rest_datetime,weekday,");
		             addsql.append("summer,remark,valid_start_time,valid_end_time,store_datetime,input_person,is_delete,mon_area,xg_mon_area)");
		             addsql.append(" values(zres_runplan_seq.nextval,'"+launch_country+"','"+sentcity_id+"','"+redisseminators+"','"+freq+"','"+band+"','"+runplan_type_id+"',");
		             addsql.append("'"+language_id+"','"+direction+"','"+start_time+"','"+end_time+"','"+power+"','"+service_area+"','"+times[0]+"','"+times[1]+"','"+rest_datetime+"','"+weekday+"',");
		             addsql.append("'"+summer+"','"+remark+"',");
		             addsql.append("'"+valid_start_time+"','"+valid_end_time+"',sysdate,'"+input_person+"',0,'"+mon_area+"','"+xg_mon_area+"')");
		 DbComponent.exeUpdate(addsql.toString());
		return res;
	}

	/**
	 *  ��ѯ����ͼ
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public ASObject queryRunplan(GJTLDRunplanBean bean) throws Exception{
		
		String runplan_id = bean.getRunplan_id();//����ͼid
		String runplan_type_id = bean.getRunplan_type_id();//����ͼ����id
		String launch_country = bean.getLaunch_country();//�������
		String sentcity_id = bean.getSentcity_id();//�������id
		String redisseminators = bean.getRedisseminators();//ת������
		String freq =bean.getFreq();      //Ƶ��
		String language_id = bean.getLanguage_id();//����id
		String service_area = bean.getService_area(); //������
		String start_time = bean.getStart_time();
		String end_time = bean.getEnd_time();
		String mon_area = bean.getMon_area();
		String xg_mon_area = bean.getXg_mon_area();
		String summer = bean.getSummer();
		String valid_start_time = bean.getValid_start_time(); //������
		String valid_end_time = bean.getValid_end_time();  //ͣ����
	    StringBuffer sqlbuffer=new StringBuffer("select zrt.*,zlt.language_name language,rct.city as sentcity ") ;
		sqlbuffer.append(" from zres_runplan_tab zrt,zdic_language_tab zlt,res_city_tab rct ");
		sqlbuffer.append(" where   zrt.is_delete=0 and zrt.language_id=zlt.language_id and zrt.sentcity_id=rct.id  ");
		if(!runplan_id.equalsIgnoreCase("")&&runplan_id!=null){
			sqlbuffer.append(" and zrt.runplan_id='"+runplan_id+"'");
		}
		if(runplan_type_id!=null && !runplan_type_id.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.runplan_type_id='"+runplan_type_id+"'");
		}
		if(launch_country!=null&&!launch_country.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.launch_country='"+launch_country+"'");
		}
		if(sentcity_id!=null && !sentcity_id.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.sentcity_id='"+sentcity_id+"'");
		}
		if(redisseminators!=null && !redisseminators.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.redisseminators='"+redisseminators+"'");
		}
		if(freq!=null && !freq.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.freq='"+freq+"'");
		}
		if(language_id!=null && !language_id.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.language_id="+language_id+"");
		}
		if(service_area!=null && !service_area.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.service_area='"+service_area+"'");
		}
		if(start_time!=null && !start_time.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.start_time=to_char('"+start_time+"')");
		}
		if(end_time!=null && !end_time.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.end_time=to_char('"+end_time+"')");
		}
	
		if(valid_start_time!=null && !valid_start_time.equalsIgnoreCase("")){
			sqlbuffer.append(" and (zrt.valid_start_time>=to_date('"+valid_start_time+"','yyyy-MM-dd HH24:MI:SS')");
			if(valid_end_time!=null && !valid_end_time.equalsIgnoreCase("")){
				sqlbuffer.append(" and zrt.valid_start_time<=to_date('"+valid_end_time+"','yyyy-MM-dd HH24:MI:SS'))"); 
			 }else
			 {
				 sqlbuffer.append(")"); 
			 }
		}
		if(valid_end_time!=null && !valid_end_time.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.valid_end_time>=to_date('"+valid_end_time+"','yyyy-MM-dd HH24:MI:SS')");
		}
//		//��Ҫ�����ž���������ʼʱ��ͽ���ʱ�䶼Ϊ�գ���Ҫ����Ч�ڿ�ʼʱ��С�ڵ�ǰʱ�䡣
//		if((valid_start_time==null ||valid_start_time.equalsIgnoreCase(""))&&(valid_end_time==null ||valid_end_time.equalsIgnoreCase(""))){
//			sqlbuffer.append(" and zrt.valid_start_time<=sysdate ");	
//		}
			if(summer!=null && !summer.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.summer like '%"+summer+"%'");
		}
		if(mon_area!=null && !mon_area.equalsIgnoreCase("")){
			if(xg_mon_area.indexOf(",")>0){
				String[] s=xg_mon_area.split(",");
				StringBuffer temp = new StringBuffer("(");
				for(int i=0;i<s.length;i++){
					temp.append(" zrt.mon_area like '%"+s[i]+"%' or");
				}
				sqlbuffer.append(" and "+temp.toString().substring(0, temp.toString().length()-2)+")");
			}else{
				sqlbuffer.append(" and zrt.mon_area like '%"+mon_area+"%'");
			}
			
		}
		if(xg_mon_area!=null && !xg_mon_area.equalsIgnoreCase("")){
			if(xg_mon_area.indexOf(",")>0){
				String[] s=xg_mon_area.split(",");
				StringBuffer temp = new StringBuffer("(");
				for(int i=0;i<s.length;i++){
					temp.append(" zrt.xg_mon_area like '%"+s[i]+"%' or");
				}
				sqlbuffer.append(" and "+temp.toString().substring(0, temp.toString().length()-2)+")");
			}else{
				sqlbuffer.append(" and zrt.xg_mon_area like '%"+xg_mon_area+"%'");
			}
			
		}

		sqlbuffer.append(" order by zrt.xg_mon_area,zrt.mon_area,zrt.redisseminators ");
		ASObject resObj=StringTool.pageQuerySql(sqlbuffer.toString(), bean);
		return resObj;
	}

}
