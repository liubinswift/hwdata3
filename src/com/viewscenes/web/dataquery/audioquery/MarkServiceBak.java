package com.viewscenes.web.dataquery.audioquery;

import java.util.ArrayList;

import org.jmask.web.controller.EXEException;

import com.viewscenes.bean.RadioMarkZstViewBean;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.dao.database.DbComponent.DbQuickExeSQL;
import com.viewscenes.pub.GDSet;
import com.viewscenes.pub.GDSetException;
import com.viewscenes.util.StringTool;
import com.viewscenes.web.common.Common;

import flex.messaging.io.amf.ASObject;

/**
 * 打分服务类
 * 
 * @author thinkpad
 * 
 */
public class MarkServiceBak{
	/**
	 * 收听效果打分入库
	 * 
	 * @param request
	 * @param opDetail
	 * @return
	 */
	public static Object setMark(RadioMarkZstViewBean radioMarkZstViewBean) {


		StringBuffer sql = new StringBuffer();
    try{
		 sql.append(" insert into radio_mark_zst_view_tab(mark_id, mark_user, mark_datetime, head_code,equ_code, frequency, runplan_id, counti, ");
		 sql.append(" counto, counts, description, mark_type, edit_user, unit,mark_file_url, file_name, file_length, record_start_time, ");
		 sql.append(" record_end_time, station_id, station_name, headname, languagename1, play_time, task_id, task_name, level_value, ");
		 sql.append(" fm_value, am_value,offset_value,remark) ");
		 sql.append(" values(radio_mark_zst_seq.nextval,'"
		 + radioMarkZstViewBean.getMark_user() + "',sysdate,'"
		 + radioMarkZstViewBean.getHead_code() + "','"
		 + radioMarkZstViewBean.getEqu_code() + "', '"
		 + radioMarkZstViewBean.getFrequency() + "', '"
		 + radioMarkZstViewBean.getRunplan_id() + "', '"
		 + radioMarkZstViewBean.getCounti() + "', ");
		 sql.append(" '" + radioMarkZstViewBean.getCounto() + "', '"
		 + radioMarkZstViewBean.getCounts() + "', '"
		 + radioMarkZstViewBean.getDescription() + "', '"
		 + radioMarkZstViewBean.getMark_type() + "','"
		 + radioMarkZstViewBean.getEdit_user() + "', '"
		 + radioMarkZstViewBean.getUnit() + "','"
		 + radioMarkZstViewBean.getMark_file_url() + "','"
		 + radioMarkZstViewBean.getFile_name() + "','"
		 + radioMarkZstViewBean.getFile_length() + "', '"
		 + radioMarkZstViewBean.getRecord_start_time().substring(11,radioMarkZstViewBean.getRecord_start_time().length())
		 +"',");
		 sql.append("'" + radioMarkZstViewBean.getRecord_end_time().substring(11,radioMarkZstViewBean.getRecord_end_time().length())
		 + "','"
		 + radioMarkZstViewBean.getStation_id() + "','"
		 + radioMarkZstViewBean.getStation_name() + "', '"
		 + radioMarkZstViewBean.getHeadname() + "', '"
		 + radioMarkZstViewBean.getLanguagename1() + "','"
		 + radioMarkZstViewBean.getPlay_time() + "','"
		 + radioMarkZstViewBean.getTask_id() + "', '"
		 + radioMarkZstViewBean.getTask_name() + "',  '"
		 + StringTool.setDefaultValue(radioMarkZstViewBean.getLevel_value()) + "', ");
		 sql.append(" '" + StringTool.setDefaultValue(radioMarkZstViewBean.getFm_value()) + "', '"
		 + StringTool.setDefaultValue(radioMarkZstViewBean.getAm_value()) + "','"
		 + StringTool.setDefaultValue(radioMarkZstViewBean.getOffeset_value()) + "', '"
		 + radioMarkZstViewBean.getRemark() + "') ");

		
		 DbComponent.exeUpdate(sql.toString());

		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("", "[站点:"
					+ radioMarkZstViewBean.getHead_code() + "]打分失败|原因:"
					+ e.getMessage(), radioMarkZstViewBean);
		}

		return "打分完成";
	}

	/**
	 * 收听效果批量打分
	 * 
	 * @param request
	 * @param opDetail
	 * @return
	 */
//	 public static void setMark(ArrayList<RadioMarkZstViewBean> list) {
//		 if (list == null) return ;
//		 for(int i=0;i<list.size();i++){
//			 RadioMarkZstViewBean rmzvb = list.get(i);
//			 setMark(rmzvb);
//		 }
//			
//	 }

	/**
	 * 查询今日当前选择站点的打分记录列表
	 * <p>
	 * class/function:com.viewscenes.web.online
	 * <p>
	 * explain:
	 * <p>
	 * author-date:谭长伟 2012-7-11
	 * 
	 * @param:
	 * @return:
	 */
	public Object getMarkList(ASObject obj) {

		String headCode = (String) obj.get("headCode");
		String markType = (String) obj.get("markType");// 打分类型 1:实时2:录音
		ArrayList list = new ArrayList();

		StringBuffer sql = new StringBuffer();
		sql
				.append(" select mark_id,mark_user,mark_datetime, head_code,equ_code,frequency,runplan_id,counti,counto,counts,description, ");
		sql
				.append("  mark_type,edit_user,unit,mark_file_url,file_name,file_length,record_start_time,record_end_time,station_id,station_name, ");
		sql
				.append("  headname,play_time,task_id,task_name,level_value,fm_value,am_value,offset_value, ");
		sql
				.append(" remark,asr_type,result_type,status,wavelen,musicratio,noiseratio,speechlen,totalcm,audibilityscore, ");
		sql
				.append(" audibilityconfidence,channelname,channelnameconfidence,programname,programnameconfidence,languagename1,languagename2, ");
		sql
				.append(" languagename3,languagename4,languagename5,languageconfidence1,languageconfidence2,languageconfidence3,languageconfidence4,languageconfidence5 ");
		sql.append("  from radio_mark_zst_view_tab t ");
		sql.append(" where 1=1 ");
		sql.append(" and t.mark_datetime >= trunc(sysdate) ");

		if (markType != null && !markType.equals(""))
			sql.append(" and t.mark_type = '" + markType + "' ");

		if (headCode != null && !headCode.equals(""))
			sql.append(" and t.head_code = '" + headCode + "' ");

		sql.append(" order by t.mark_datetime desc ");

		GDSet set = null;

		try {
			set = DbComponent.Query(sql.toString());

			list = set2MarkBean(set);

		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("", "[站点:" + headCode
					+ "]查询打分列表失败,数据库异常|原因:" + e.getMessage(), "");
		} catch (GDSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("", "[站点:" + headCode
					+ "]查询打分列表失败,未知的列字段|原因:" + e.getMessage(), "");
		}

		return list;

	}

	/**
	 * 删除打分记录
	 * <p>
	 * class/function:com.viewscenes.web.online
	 * <p>
	 * explain:
	 * <p>
	 * author-date:谭长伟 2012-7-11
	 * 
	 * @param:
	 * @return:
	 */
	public Object delMark(RadioMarkZstViewBean rmzvb) {
		String sql = " delete radio_mark_zst_view_tab where mark_id = '"
				+ rmzvb.getMark_id() + "'";

		try {
			DbComponent.exeUpdate(sql);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("", "[站点:" + rmzvb.getHead_code()
					+ "]删除打分记录失败|原因:" + e.getMessage(), "");
		}
		return "删除打分记录成功";
	}

	/**
	 * 获取从当前时间向前推一周该站点录音的打分和受干扰情况
	 * <p>
	 * class/function:com.viewscenes.web.online.onlineListen
	 * <p>
	 * explain:
	 * <p>
	 * author-date:谭长伟 2012-8-10
	 * 
	 * @param:
	 * @return:
	 */
	public Object getMarkByPreWeek(ASObject obj) {
		ArrayList<RadioMarkZstViewBean> list = new ArrayList<RadioMarkZstViewBean>();

		String headCode = (String) obj.get("headCode");
		String station_id = (String) obj.get("station_id");
		String freq = (String) obj.get("freq");
		int mark_type = (Integer) obj.get("mark_type");

		String sql = " select trunc(t.mark_datetime) mark_datetime,avg(counti) counti,avg(counts) counts,avg(counto) counto  from radio_mark_zst_view_tab t ";
		sql += " where t.head_code = '" + headCode + "' ";
		sql += " and t.frequency = " + freq + " ";
		if (station_id != null && !station_id.equals(""))
			sql += " and t.station_id = " + station_id + " ";
		sql += " and t.mark_type = "+mark_type+" ";
		sql += " and t.mark_datetime between trunc(sysdate-7) and sysdate ";
		sql += " group by trunc(t.mark_datetime) ";
		sql += " order by trunc(t.mark_datetime) desc ";

		GDSet set = null;
		try {
			set = DbComponent.Query(sql);

			for (int i = 0; i < set.getRowCount(); i++) {
				String mark_datetime = set.getString(i, "mark_datetime");
				String counti = set.getString(i, "counti");
				String counts = set.getString(i, "counts");
				String counto = set.getString(i, "counto");

				RadioMarkZstViewBean rmvb = new RadioMarkZstViewBean();
				rmvb.setMark_datetime(mark_datetime.substring(5, 10));
				rmvb.setCounti(counti);
				rmvb.setCounto(counto);
				rmvb.setCounts(counts);

				list.add(rmvb);
			}
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("", "[站点:" + headCode
					+ "]查询一周打分列表失败,数据库异常|原因:" + e.getMessage(), "");
		} catch (GDSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("", "[站点:" + headCode
					+ "]查询一周打分列表失败,未知的列字段|原因:" + e.getMessage(), "");
		}

		return list;
	}

	/**
	 * 修改打分记录
	 * <p>
	 * class/function:com.viewscenes.web.online.onlineListen
	 * <p>
	 * explain:
	 * <p>
	 * author-date:谭长伟 2012-8-22
	 * 
	 * @param:
	 * @return:
	 */
	public Object editMark(RadioMarkZstViewBean rmzvBean) {
		
		if (rmzvBean == null)
			return new EXEException("", "修改打分失败,打分对象为null|原因:", "");
		StringBuffer sql = new StringBuffer();
		sql.append(" update radio_mark_zst_view_tab ");
		sql.append(" set mark_user = '" + rmzvBean.getMark_user() + "' ");
		sql.append(" ,mark_datetime = sysdate ");
		sql.append(" ,head_code = '"	+ rmzvBean.getHead_code() + "' ");
		if (rmzvBean.getEqu_code() != null)
			sql.append(" ,equ_code = '" + rmzvBean.getEqu_code() + "' ");
		if (rmzvBean.getFrequency() != null)
			sql.append(" ,frequency = '"	+ rmzvBean.getFrequency() + "' ");
		if (rmzvBean.getRunplan_id() != null)
			sql.append(" ,runplan_id = '" + rmzvBean.getRunplan_id()	+ "'");
		if (rmzvBean.getCounti() != null)
			sql.append(" ,counti = '" + rmzvBean.getCounti() + "'  ");
		if (rmzvBean.getCounto() != null)
			sql.append(" ,counto = '" + rmzvBean.getCounto() + "'");
		if (rmzvBean.getCounts() != null)
			sql.append(" ,counts = '" + rmzvBean.getCounts() + "'  ");
		if (rmzvBean.getDescription() != null)
			sql.append(" ,description = '" + rmzvBean.getDescription() + "'");
		if (rmzvBean.getMark_type() != null)
			sql.append(" ,mark_type = '" + rmzvBean.getMark_type() + "'  ");
		if (rmzvBean.getEdit_user() != null)
			sql.append(" ,edit_user = '" + rmzvBean.getEdit_user() + "'");
		if (rmzvBean.getUnit() != null)
			sql.append(" ,unit = '" + rmzvBean.getUnit() + "'  ");

		if (rmzvBean.getFile_length() != null)
			sql.append(" ,file_length = '" + rmzvBean.getFile_length() + "' ");
		if (rmzvBean.getRecord_start_time() != null)
			if(rmzvBean.getRecord_start_time().length()==19)
			{
				String start_time=rmzvBean.getRecord_start_time().substring(11,rmzvBean.getRecord_start_time().length());
				sql.append(" ,record_start_time = '" + start_time + "' ");
			}
	
		if (rmzvBean.getRecord_end_time() != null)
		{
			if(rmzvBean.getRecord_end_time().length()==19)
			{
				String end_time=rmzvBean.getRecord_end_time().substring(11,rmzvBean.getRecord_end_time().length());
				sql.append(" ,record_end_time = '" + end_time+ "' ");
			}
	
		}
		if (rmzvBean.getStation_id() != null)
			sql.append(" ,station_id = '" + rmzvBean.getStation_id() + "' ");
		if (rmzvBean.getStation_name() != null)
			sql.append(" ,station_name = '" + rmzvBean.getStation_name() + "' ");
		if (rmzvBean.getHeadname() != null)
			sql.append(" ,headname = '" +Common.getHeadendnameNOABByCode(rmzvBean.getHead_code()) + "' ");
		if (rmzvBean.getPlay_time() != null)
			sql.append(" ,play_time = '" + rmzvBean.getPlay_time() + "' ");
		if (rmzvBean.getTask_id() != null)
			sql.append(" ,task_id = '" + rmzvBean.getTask_id() + "' ");
		if (rmzvBean.getTask_name() != null)
			sql.append(" ,task_name = '" + rmzvBean.getTask_name()+ "' ");
		if (rmzvBean.getLevel_value() != null)
			sql.append(" ,level_value = '" + rmzvBean.getLevel_value() + "' ");
		if (rmzvBean.getFm_value() != null)
			sql.append(" ,fm_value = '" + rmzvBean.getFm_value() + "' ");
		if (rmzvBean.getAm_value() != null)
			sql.append(" ,am_value = '" + rmzvBean.getAm_value() + "' ");
		if (rmzvBean.getOffeset_value() != null)
			sql.append(" ,offset_value = '" + rmzvBean.getOffeset_value()	+ "' ");
		if (rmzvBean.getRemark() != null)
			sql.append(" ,remark = '" + rmzvBean.getRemark() + "' ");
		if (rmzvBean.getAsr_type() != null)
			sql.append(" ,asr_type = '" + rmzvBean.getAsr_type() + "' ");
		if (rmzvBean.getResult_type() != null)
			sql.append(" ,result_type = '" + rmzvBean.getResult_type() + "' ");
		if (rmzvBean.getStatus() != null)
			sql.append(" ,status = '" + rmzvBean.getStatus() + "' ");
		if (rmzvBean.getWavelen() != null)
			sql.append(" ,wavelen = '" + rmzvBean.getWavelen() + "' ");
		if (rmzvBean.getMusicratio() != null)
			sql.append(" ,musicratio = '" + rmzvBean.getMusicratio() + "' ");
		if (rmzvBean.getNoiseratio() != null)
			sql.append(" ,noiseratio = '" + rmzvBean.getNoiseratio() + "' ");
		if (rmzvBean.getSpeechlen() != null)
			sql.append(" ,speechlen = '" + rmzvBean.getSpeechlen() + "' ");
		if (rmzvBean.getTotalcm() != null)
			sql.append(" ,totalcm = '" + rmzvBean.getTotalcm() + "' ");
		if (rmzvBean.getAudibilityscore() != null)
			sql.append(" ,audibilityscore = '" + rmzvBean.getAudibilityscore()+ "' ");
		if (rmzvBean.getAudibilityconfidence() != null)
			sql.append(" ,audibilityconfidence = '" + rmzvBean.getAudibilityconfidence() + "' ");
		if (rmzvBean.getChannelname() != null)
			sql.append(" ,channelname = '" + rmzvBean.getChannelname() + "' ");
		if (rmzvBean.getChannelnameconfidence() != null)
			sql.append(" ,channelnameconfidence = '"	+ rmzvBean.getChannelnameconfidence() + "' ");
		if (rmzvBean.getProgramname() != null)
			sql.append(" ,programname = '" + rmzvBean.getProgramname() + "' ");
		if (rmzvBean.getProgramnameconfidence() != null)
			sql.append(" ,programnameconfidence = '"	+ rmzvBean.getProgramnameconfidence() + "' ");
		if (rmzvBean.getLanguagename1() != null)
			sql.append(" ,languagename1 = '" + rmzvBean.getLanguagename1() + "' ");
		if (rmzvBean.getLanguagename2() != null)
			sql.append(" ,languagename2 = '" + rmzvBean.getLanguagename2() + "' ");
		if (rmzvBean.getLanguagename3() != null)
			sql.append(" ,languagename3 = '" + rmzvBean.getLanguagename3() + "' ");
		if (rmzvBean.getLanguagename4() != null)
			sql.append(" ,languagename4 = '" + rmzvBean.getLanguagename4() + "' ");
		if (rmzvBean.getLanguagename5() != null)
			sql.append(" ,languagename5 = '" + rmzvBean.getLanguagename5() + "' ");
		if (rmzvBean.getLanguageconfidence1() != null)
			sql.append(" ,languageconfidence1 = '" + rmzvBean.getLanguageconfidence1() + "' ");
		if (rmzvBean.getLanguageconfidence2() != null)
			sql.append(" ,languageconfidence2 = '" + rmzvBean.getLanguageconfidence2() + "' ");
		if (rmzvBean.getLanguageconfidence3() != null)
			sql.append(" ,languageconfidence3 = '" + rmzvBean.getLanguageconfidence3() + "' ");
		if (rmzvBean.getLanguageconfidence4() != null)
			sql.append(" ,languageconfidence4 = '" + rmzvBean.getLanguageconfidence4() + "' ");
		if (rmzvBean.getLanguageconfidence5() != null)
			sql.append(" ,languageconfidence5 = '" + rmzvBean.getLanguageconfidence5() + "' ");
		sql.append(" where mark_id = '" + rmzvBean.getMark_id() + "' ");

		try {
			DbComponent.exeUpdate(sql.toString());
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("", "[站点:" + rmzvBean.getHead_code()
					+ "]修改打分失败,数据库错误|原因:" + e.getMessage(), "");
		}

		return "修改打分记录成功";
	}

	/**
	 * 将set 保存到bean中,然后返回BEAN LIST
	 * <p>
	 * class/function:com.viewscenes.web.online.onlineListen
	 * <p>
	 * explain:
	 * <p>
	 * author-date:谭长伟 2012-8-10
	 * 
	 * @param:
	 * @return:
	 * @throws GDSetException
	 */
	private ArrayList<RadioMarkZstViewBean> set2MarkBean(GDSet set)
			throws GDSetException {
		ArrayList<RadioMarkZstViewBean> list = new ArrayList<RadioMarkZstViewBean>();
		if (set == null)
			return list;

		for (int i = 0; i < set.getRowCount(); i++) {

			RadioMarkZstViewBean rmzvb = new RadioMarkZstViewBean();

			rmzvb.setMark_id(set.getString(i, "mark_id"));
			rmzvb.setMark_user(set.getString(i, "mark_user"));
			rmzvb.setMark_datetime(set.getString(i, "mark_datetime"));
			rmzvb.setHead_code(set.getString(i, "head_code"));
			rmzvb.setEqu_code(set.getString(i, "equ_code"));
			rmzvb.setFrequency(set.getString(i, "frequency"));
			rmzvb.setRunplan_id(set.getString(i, "runplan_id"));
			rmzvb.setCounti(set.getString(i, "counti"));
			rmzvb.setCounto(set.getString(i, "counto"));
			rmzvb.setCounts(set.getString(i, "counts"));
			rmzvb.setDescription(set.getString(i, "description"));
			rmzvb.setMark_type(set.getString(i, "mark_type"));
			rmzvb.setEdit_user(set.getString(i, "edit_user"));
			rmzvb.setUnit(set.getString(i, "unit"));
			rmzvb.setMark_file_url(set.getString(i, "mark_file_url"));
			rmzvb.setFile_name(set.getString(i, "file_name"));
			rmzvb.setFile_length(set.getString(i, "file_length"));
			rmzvb.setRecord_start_time(set.getString(i, "record_start_time"));
			rmzvb.setRecord_end_time(set.getString(i, "record_end_time"));
			rmzvb.setStation_id(set.getString(i, "station_id"));
			rmzvb.setStation_name(set.getString(i, "station_name"));
			rmzvb.setHeadname(Common.getHeadendnameNOABByCode(set.getString(i, "head_code")));
			// rmzvb.set`Language_name(set.getString(i, "language_name"));
			rmzvb.setPlay_time(set.getString(i, "play_time"));
			rmzvb.setTask_id(set.getString(i, "task_id"));
			rmzvb.setTask_name(set.getString(i, "task_name"));
			rmzvb.setLevel_value(set.getString(i, "level_value"));
			rmzvb.setFm_value(set.getString(i, "fm_value"));
			rmzvb.setAm_value(set.getString(i, "am_value"));
			rmzvb.setOffeset_value(set.getString(i, "offset_value"));
			rmzvb.setRemark(set.getString(i, "remark"));

			rmzvb.setAsr_type(set.getString(i, "asr_type"));
			rmzvb.setResult_type(set.getString(i, "result_type"));
			rmzvb.setWavelen(set.getString(i, "wavelen"));
			rmzvb.setMusicratio(set.getString(i, "musicratio"));
			rmzvb.setNoiseratio(set.getString(i, "noiseratio"));
			rmzvb.setSpeechlen(set.getString(i, "speechlen"));
			rmzvb.setStatus(set.getString(i, "status"));
			rmzvb.setTotalcm(set.getString(i, "totalcm"));
			rmzvb.setAudibilityscore(set.getString(i, "audibilityscore"));
			rmzvb.setAudibilityconfidence(set.getString(i,
					"audibilityconfidence"));
			rmzvb.setChannelname(set.getString(i, "channelname"));
			rmzvb.setChannelnameconfidence(set.getString(i,
					"channelnameconfidence"));
			rmzvb.setProgramname(set.getString(i, "programname"));
			rmzvb.setProgramnameconfidence(set.getString(i,
					"programnameconfidence"));
			rmzvb.setLanguageconfidence1(set
					.getString(i, "languageconfidence1"));
			rmzvb.setLanguageconfidence2(set
					.getString(i, "languageconfidence2"));
			rmzvb.setLanguageconfidence3(set
					.getString(i, "languageconfidence3"));
			rmzvb.setLanguageconfidence4(set
					.getString(i, "languageconfidence4"));
			rmzvb.setLanguageconfidence5(set
					.getString(i, "languageconfidence5"));
			rmzvb.setLanguagename1(set.getString(i, "languagename1"));
			rmzvb.setLanguagename2(set.getString(i, "languagename2"));
			rmzvb.setLanguagename3(set.getString(i, "languagename3"));
			rmzvb.setLanguagename4(set.getString(i, "languagename4"));
			rmzvb.setLanguagename5(set.getString(i, "languagename5"));
			list.add(rmzvb);
		}

		return list;
	}
	public static void main(String[] args)
	{
		String time ="2012-01-02 22:00:22";
		System.out.print(time.substring(11,time.length()));
	}
}
