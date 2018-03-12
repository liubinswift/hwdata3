package com.viewscenes.web.runplanManager.gjtRunplan;

import com.viewscenes.bean.runplan.GJTRunplanBean;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.pub.GDSet;
import com.viewscenes.pub.GDSetException;
import com.viewscenes.util.StringTool;

import flex.messaging.io.amf.ASObject;

public class GJTRunplanDAO {

	public ASObject queryRunplan(GJTRunplanBean bean) throws Exception{
		String runplan_id = bean.getRunplan_id();   //����ͼid
		String runplan_type_id = bean.getRunplan_type_id(); //����ͼ����id
		String station_id = bean.getStation_id();    //����̨id
		String station_name = bean.getStation_name(); //����̨
		//String sentcity_id = bean.getSentcity_id();  //�������id
		String transmiter_no = bean.getTransmiter_no();  //�������
		String freq =bean.getFreq();      //Ƶ��
		String antenna = bean.getAntenna(); //���ߺ�
		String antennatype = bean.getAntennatype();//���߳�ʽ
		String direction = bean.getDirection(); //����
		String language_id = bean.getLanguage_id(); //����id
		String power = bean.getPower();  //����
		String seasonCode = bean.getSeason_id();//;���ڴ���
		String program_type_id = bean.getProgram_type_id(); //��Ŀ����id
		String service_area = bean.getService_area(); //������
		String ciraf = bean.getCiraf(); // CIRAF��
		String satellite_channel = bean.getSatellite_channel();//��������ͨ��
		String start_time = bean.getStart_time(); //������ʼʱ��
		String end_time = bean.getEnd_time();   //��������ʱ��
		String mon_area = bean.getMon_area();    //�����ղ�
	
		String xg_mon_area = bean.getXg_mon_area();//Ч���ղ�
		String remark = bean.getRemark();//��ע
     	String rest_datetime = bean.getRest_datetime(); // //��Ϣ���ڵ���ǰ̨��ѯ����Ч����
//		String rest_time = bean.getRest_time(); //��Ϣʱ��
		String valid_start_time = bean.getValid_start_time(); //������
		String valid_end_time = bean.getValid_end_time();  //ͣ����
		
		StringBuffer sqlbuffer =new StringBuffer("select zrt.*,zlt.language_name as language ") ;
		sqlbuffer.append(" from zres_runplan_tab zrt,zdic_language_tab zlt,res_transmit_station_tab rat ");
		sqlbuffer.append(" where zrt.is_delete=0 and zrt.station_id=rat.station_id(+) and zrt.language_id=zlt.language_id(+)   ");
		if(runplan_id!=null&&!runplan_id.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.runplan_id='"+runplan_id+"'");
		}
		if(runplan_type_id!=null && !runplan_type_id.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.runplan_type_id='"+runplan_type_id+"'");
		}
		if(station_id!=null && !station_id.equalsIgnoreCase("")){
			sqlbuffer.append(" and (zrt.station_id='"+station_id+"' or zrt.station_name like '%"+station_name+"%')");
		}	
		if(seasonCode!=null&&!seasonCode.equalsIgnoreCase("")){//���ݼ��ڴ��ŵ���Ч�ڲ�ѯ����ͼ
			sqlbuffer.append(" and zrt.season_id like '"+seasonCode+"' ");
//			GDSet gd1=getTimeBySeason(seasonCode);
//			if(gd1.getRowCount()>0){
//				sqlbuffer.append(" and zrt.valid_start_time>=to_date('"+gd1.getString(0, "start_time")+"','yyyy-mm-dd hh24:mi:ss') ");
//				sqlbuffer.append(" and zrt.valid_start_time<=to_date('"+gd1.getString(0, "end_time")+"','yyyy-mm-dd hh24:mi:ss') ");
//			}
		}
		if(transmiter_no!=null && !transmiter_no.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.transmiter_no like'%"+transmiter_no+"%'");
		}
		if(freq!=null && !freq.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.freq='"+freq+"'");
		}
		if(antenna!=null && !antenna.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.antenna='"+antenna+"'");
		}
		if(antennatype!=null && !antennatype.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.antennatype='"+antennatype+"'");
		}
		if(direction!=null && !direction.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.direction='"+direction+"'");
		}
		if(language_id!=null && !language_id.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.language_id="+language_id+"");
		}
		if(power!=null &&!power.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.power="+power+"");
		}
		if(program_type_id!=null && !program_type_id.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.program_type_id="+program_type_id+"");
		}
		if(service_area!=null && !service_area.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.service_area='"+service_area+"'");
		}
		if(ciraf!=null && !ciraf.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.ciraf='"+ciraf+"'");
		}
		if(satellite_channel!=null && !satellite_channel.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.satellite_channel='"+satellite_channel+"'");
		}
		if(start_time!=null && !start_time.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.start_time=to_char('"+start_time+"')");
		}
		if(end_time!=null && !end_time.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.end_time=to_char('"+end_time+"')");
		}
		
		if(remark!=null&&!remark.equalsIgnoreCase("")){
			sqlbuffer.append(" and zrt.remark like '%"+remark+"%'");
		}
//		if((mon_area!=null && !mon_area.equalsIgnoreCase(""))&&(xg_mon_area!=null && !xg_mon_area.equalsIgnoreCase(""))){
//			sqlbuffer.append(" and (zrt.mon_area like '%"+mon_area+"%'");
//			sqlbuffer.append(" or zrt.xg_mon_area like '%"+xg_mon_area+"%')");
//		}else{
			if(mon_area!=null && !mon_area.equalsIgnoreCase("")){
				if(mon_area.indexOf(",")>0){
					String[] s=mon_area.split(",");
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
//		}
		
		
	
//		if(rest_datetime!=null && !rest_datetime.equalsIgnoreCase("")){
//			sqlbuffer.append(" and zrt.rest_datetime=to_date('"+rest_datetime+"','yyyy-MM-dd HH24:MI:SS')");
//		}
//		if(rest_time!=null && !rest_time.equalsIgnoreCase("")){
//			sqlbuffer.append(" and zrt.rest_time=to_char('"+rest_time+"')");
//		}
			if(rest_datetime!=null&&!rest_datetime.equals(""))
			{
				sqlbuffer.append(" and zrt.valid_start_time<=to_date('"+rest_datetime+"','yyyy-MM-dd')");
				sqlbuffer.append(" and zrt.valid_end_time>=to_date('"+rest_datetime+"','yyyy-MM-dd') ");
				
				
			}else
			{
					if(valid_start_time!=null && !valid_start_time.equalsIgnoreCase("")){
						sqlbuffer.append(" and ((zrt.valid_start_time>=to_date('"+valid_start_time+"','yyyy-MM-dd HH24:MI:SS')");
						if(valid_end_time!=null && !valid_end_time.equalsIgnoreCase("")){
							sqlbuffer.append(" and zrt.valid_start_time<=to_date('"+valid_end_time+"','yyyy-MM-dd HH24:MI:SS'))"); 
						 }else
						 {
							 sqlbuffer.append("))"); 
						 }
							
					}
					if(valid_end_time!=null && !valid_end_time.equalsIgnoreCase("")){
						if(valid_start_time!=null && !valid_start_time.equalsIgnoreCase(""))
						{
						  sqlbuffer.append(" or (zrt.valid_end_time>=to_date('"+valid_start_time+"','yyyy-MM-dd HH24:MI:SS')");
						  sqlbuffer.append(" and zrt.valid_end_time<=to_date('"+valid_end_time+"','yyyy-MM-dd HH24:MI:SS'))) ");
						   	
						}else 
						{
						  sqlbuffer.append(" and ((zrt.valid_end_time<=to_date('"+valid_end_time+"','yyyy-MM-dd HH24:MI:SS'))) ");
					   }
					}
			}
//		//��Ҫ�����ž���������ʼʱ��ͽ���ʱ�䶼Ϊ�գ���Ҫ����Ч�ڿ�ʼʱ��С�ڵ�ǰʱ�䡣
//		if((valid_start_time==null ||valid_start_time.equalsIgnoreCase(""))&&(valid_end_time==null ||valid_end_time.equalsIgnoreCase(""))){
//			sqlbuffer.append(" and zrt.valid_start_time<=sysdate ");	
//		}
		sqlbuffer.append(" order by zrt.station_name,zrt.xg_mon_area,zrt.mon_area asc ");
		ASObject resObj=StringTool.pageQuerySql(sqlbuffer.toString(), bean);
		return resObj;
	}
	/**
	 * ����ͼ������
	 * @param bean
	 * @return
	 * @throws DbException
	 * @throws GDSetException 
	 * �޸��ˣ�����
	 * �޸��߼��������ӵ�����ͼ��ͬ��Ƶ������Ϣ�����з�������������ͼ��ֱ�ӰѴ�����ͼҲ��ӵ�ͬ��Ƶ������Ϣ���С�
	 * �޸����ڣ�2014.9.25
	 */
	public String addRunplan(GJTRunplanBean bean) throws DbException, GDSetException{
		String res="�������ͼ�ɹ�!";
		String input_person = bean.getInput_person()==null?"":bean.getInput_person();//¼����
		String runplan_type_id = bean.getRunplan_type_id()==null?"":bean.getRunplan_type_id(); //����ͼ����id
		String station_id = bean.getStation_id()==null?"":bean.getStation_id();    //����̨id
		String station_name = bean.getStation_name()==null?"":bean.getStation_name(); //����̨
		//String sentcity_id = bean.getSentcity_id()==null?"":bean.getSentcity_id();  //�������id
		String transmiter_no = bean.getTransmiter_no()==null?"":bean.getTransmiter_no();  //�������
		String freq =bean.getFreq()==null?"":bean.getFreq();      //Ƶ��
		String antenna = bean.getAntenna()==null?"":bean.getAntenna(); //���ߺ�
		String antennatype = bean.getAntennatype()==null?"":bean.getAntennatype();//���߳�ʽ
		String direction = bean.getDirection()==null?"":bean.getDirection(); //����
		String language_id = bean.getLanguage_id()==null?"":bean.getLanguage_id(); //����id
		String power = bean.getPower()==null?"":bean.getPower();  //����
		String program_type_id = bean.getProgram_type_id()==null?"":bean.getProgram_type_id(); //��Ŀ����id
		String program_type = bean.getProgram_type()==null?"":bean.getProgram_type(); //��Ŀ����
		String service_area = bean.getService_area()==null?"":bean.getService_area(); //������
		String ciraf = bean.getCiraf()==null?"":bean.getCiraf(); // CIRAF��
		String satellite_channel = bean.getSatellite_channel()==null?"":bean.getSatellite_channel();//��������ͨ��
		String start_time = bean.getStart_time()==null?"":bean.getStart_time(); //������ʼʱ��
		String end_time = bean.getEnd_time()==null?"":bean.getEnd_time();   //��������ʱ��
		String mon_area =  bean.getMon_area()==null?"":bean.getMon_area();    //�����ղ�վ��
		String xg_mon_area = bean.getXg_mon_area()==null?"":bean.getXg_mon_area();//Ч���ղ�վ��
		String rest_datetime = bean.getRest_datetime()==null?"":bean.getRest_datetime(); // //��Ϣ����
		String valid_start_time = bean.getValid_start_time()==null?"":bean.getValid_start_time(); //������
		String valid_end_time = bean.getValid_end_time()==null?"":bean.getValid_end_time();  //ͣ����
		String season_id = bean.getSeason_id()==null?"":bean.getSeason_id();//���ڴ���
		String remark = bean.getRemark()==null?"":bean.getRemark();//��ע
		String weekday = bean.getWeekday()==null?"":bean.getWeekday();//������
		
		
		//���ﵼ������ͼ��ʱ����Ҫ�жϣ���������д�����ͼʵ�и��£����û������
		//�ж�����Ϊ��Ƶ�ʣ�����ʱ�䣬������������̨�����򣬹��ʣ����ԣ����ڴ��š�
		String isExistsql="select t.runplan_id from zres_runplan_tab t  " +
				"��where t.freq="+freq+"��and t.language_id="+language_id+"��and  t.start_time='"+start_time+
				"'��and  t.end_time='"+end_time+"'��and t.service_area='"+service_area+"'��and t.power='"+power+"'��and t.direction='"+direction +
				"'��and t.season_id='"+season_id+"'��and t.valid_end_time>sysdate��order by t.runplan_id desc ";
		//GDSet gd= DbComponent.Query(isExistsql);
		StringBuffer addsql=null;
//		if(gd.getRowCount()>0)//˵���У�ִ�и��¡�
//		{
//			String runplan_id=gd.getString(0, "runplan_id");
//			addsql = new StringBuffer(" update zres_runplan_tab ");
//            addsql.append(" set station_id="+station_id+",station_name='"+station_name+"',transmiter_no='"+transmiter_no+"',freq='"+freq+"',runplan_type_id='"+runplan_type_id+"',direction=");
//            addsql.append("'"+direction+"',antenna='"+antenna+"',antennatype='"+antennatype+"',start_time='"+start_time+"',end_time='"+end_time+"',program_type_id='"+program_type_id+"',program_type='"+program_type+"',language_id=");
//            addsql.append("'"+language_id+"',season_id='"+season_id+"',power='"+power+"',service_area='"+service_area+"',ciraf='"+ciraf+"',satellite_channel='"+satellite_channel+"',remark='"+remark+"',rest_datetime='"+rest_datetime+"',valid_start_time=");
//            addsql.append("'"+valid_start_time+"',valid_end_time='"+valid_end_time+"',store_datetime=sysdate,input_person='"+input_person+"',is_delete=0,mon_area=upper('"+mon_area+"'),xg_mon_area=upper('"+xg_mon_area+"'),weekday='"+weekday+"'");
//            addsql.append(" where runplan_id= "+runplan_id);
//       	 DbComponent.exeUpdate(addsql.toString());
//        	
//		}else //û�в��������
		{
			addsql = new StringBuffer("insert into zres_runplan_tab (runplan_id,station_id,station_name,transmiter_no,freq,runplan_type_id,");
            addsql.append("direction,antenna,antennatype,start_time,end_time,program_type_id,program_type,language_id,season_id,power,service_area,");
            addsql.append("ciraf,satellite_channel,remark,rest_datetime,valid_start_time,valid_end_time,store_datetime,input_person,is_delete,mon_area,xg_mon_area,weekday)");
            addsql.append(" values(zres_runplan_seq.nextval,'"+station_id+"','"+station_name+"','"+transmiter_no+"','"+freq+"','"+runplan_type_id+"',");
            addsql.append("'"+direction+"','"+antenna+"','"+antennatype+"','"+start_time+"','"+end_time+"','"+program_type_id+"','"+program_type+"',");
            addsql.append("'"+language_id+"','"+season_id+"','"+power+"','"+service_area+"','"+ciraf+"','"+satellite_channel+"','"+remark+"','"+rest_datetime+"',");
            addsql.append("'"+valid_start_time+"','"+valid_end_time+"',sysdate,'"+input_person+"',0,upper('"+mon_area+"'),upper('"+xg_mon_area+"'),'"+weekday+"')");
       	    DbComponent.exeUpdate(addsql.toString());
            //�ڲ����µ�����ͼ��ʱ����Ҫ�ж��Ƿ����ͬ��Ƶ������Ϣ�����������Ҫ����һ����Ϣ��ͬ��Ƶ������Ϣ���С�
        	String isDisturbsql=" select t.runplan_id from zres_runplan_tab t,zdic_language_tab lan where t.station_id="+station_id+
        			" and t.transmiter_no='"+transmiter_no+"' and t.freq='"+freq+"' and lan.language_id= "+language_id+
        			"  and  t.season_id='"+season_id+"' "+
        			" and t.language_id=lan.language_id and t.valid_end_time>sysdate and t.is_delete=0 and t.runplan_type_id=1 " +
        			" and t.runplan_id not in(select dis.runplan_id from zres_runplan_disturb_tab dis where dis.is_delete=0 ) order by t.runplan_id desc " ;
	        GDSet set= DbComponent.Query(isDisturbsql);
	        if(set.getRowCount()>0)//˵���У�ִ�и��¡�
	        {
//	        	String runplan_id=set.getString(0, "runplan_id");	
//	        	String insertDisturb="insert into zres_runplan_disturb_tab (disrun_id, runplan_id,  station_id,  station_name, sencity_id,  transmiter_no,freq, language," +
//	        			"   start_time,  end_time,  valid_start_time, valid_end_time,  redisseminators, type, disturb,is_delete, receive_station, insert_time)" +
//	        			"  select  zres_runplan_disturb_seq.nextval,run.runplan_id,dis.station_id,dis.station_name,dis.sencity_id,dis.transmiter_no," +
//	        			"  dis.freq,dis.language,dis.start_time,dis.end_time,dis.valid_start_time,dis.valid_end_time,dis.redisseminators, dis.type,dis.disturb,dis.is_delete,run.xg_mon_area,sysdate" +
//	        			"  from zres_runplan_disturb_tab dis,zres_runplan_tab run,zdic_language_tab lang where dis.station_id=run.station_id" +
//	        			"  and dis.transmiter_no=run.transmiter_no and dis.language=lang.language_name and lang.language_id=run.language_id and  run.valid_end_time>sysdate and dis.is_delete=0" +
//	        			"  and run.runplan_type_id=1 and run.runplan_id="+runplan_id;
//	        	DbComponent.exeUpdate(insertDisturb);
	        }
		}
	
		return res;
	}
	/**
	 * ���ݼ��ڴ��Ų�ѯ��Чʱ��
	 * @param seasonCode
	 * @return
	 */
	public GDSet getTimeBySeason(String seasonCode){
		String sql="select t.start_time,t.end_time from dic_season_tab t where t.code like '%"+seasonCode+"%'";
		GDSet gd = null;
		try {
			gd = DbComponent.Query(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return gd;
	}
}
