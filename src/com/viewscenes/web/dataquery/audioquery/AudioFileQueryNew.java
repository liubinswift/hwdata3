package com.viewscenes.web.dataquery.audioquery;

import com.viewscenes.axis.asr.ASRClient;
import com.viewscenes.bean.*;
import com.viewscenes.bean.runplan.RunplanBean;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.pub.GDSet;
import com.viewscenes.pub.GDSetException;
import com.viewscenes.sys.SystemConfig;
import com.viewscenes.util.Mp3Utils;
import com.viewscenes.util.StringTool;
import com.viewscenes.util.business.SiteVersionUtil;
import flex.messaging.io.amf.ASObject;
import org.jmask.web.controller.EXEException;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * 录音文件数据查询
 * @author thinkpad
 * 10.15.6.11/12
 */
public class AudioFileQueryNew {

	/**
	 * 在数据库中查询录音文件及打分情况
	 * 包括效果录音(效果录音每半个小时只取其中一条)和质量录音
	 * <p>class/function:com.viewscenes.web.daily.queryData
	 * <p>explain:
	 * <p>author-date:谭长伟 2012-8-14
	 * @param:
	 * @return:
	 */
	public Object recFileQuery(ASObject obj){
		ArrayList<RadioStreamResultBean> list = new ArrayList<RadioStreamResultBean>();
		String recType = (String)obj.get("recType");// 录音类型 quality:质量,显示全部录音;effect:效果
		String headCode = (String)obj.get("headCode");
		String freq = (String)obj.get("freq");
		String dateType = (String)obj.get("dateType");
		String date = (String)obj.get("date");
		String startDateTime = (String)obj.get("startDateTime");
		String endDateTime = (String)obj.get("endDateTime");
		String handle = (String)obj.get("handle");//是否处理：1-:全部,0:未处理(未打过分的录音或打过分置信度<100为未处理)，已处理
		String headType=(String)obj.get("headType");
		String station=(String)obj.get("station");
		String language=(String)obj.get("language");
		String type = (String) obj.get("type");//节目类型 ：国际台或者海外落地
		String sql = "";



        sql ="select   task_id ,equ_code ,frequency , stream_id result_id,stream_start_datetime start_datetime,stream_end_datetime end_datetime," +
                " FILENAME, head_id ,head_name headname ,play_url url, report_type  ,mark_id  ,mark_user , mark_datetime,  head_code ," +
                "counti, counto, counts, description,  mark_type, edit_user,mark_UNIT unit,record_start_time, record_end_time," +
                "play_time ,LEVEL_VALUE level_value,FM_MODULATION fm_value,AM_MODULATION am_value,  remark mark_remark from radio_stream_mark_result_tab " ;

		if(headCode!=null&&headCode.length()!=0){
			String headids = SiteVersionUtil.getSiteHeadIdsByCodeNoAB(headCode);
	       	sql += " and head_id in("+headids+") ";
		}
       	if (freq != null && !freq.equals(""))
       		sql += " and frequency = "+freq+" ";
		if(recType!=null&&!recType.equals("")){
			if(recType.equals("-1")){
				sql+=" and report_type in (0,1)";
			}else{
				sql+=" and report_type="+recType+" ";
			}
		}
		if (dateType.equals("byDate")){
//			sql += " and to_date('"+date+"','yyyy-mm-dd hh24:mi:ss') between t.start_datetime and t.end_datetime ";
			sql += " and stream_start_datetime >= to_date('"+date+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss') ";
			sql += "  and stream_end_datetime <= to_date('"+date+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') ";
		}else{
			sql += " and (to_date('"+startDateTime+"','yyyy-mm-dd hh24:mi:ss') <= stream_start_datetime and to_date('"+endDateTime+"','yyyy-mm-dd hh24:mi:ss') >= stream_end_datetime) ";
		}
		if(headType!=null&&headType.length()!=0){
			sql+=" and head_type_id="+headType;
		}
		if(station!=null&&station.length()!=0){
			sql+=" and station_id='"+station+"'";
		}
		if(language!=null&&language.length()!=0){
			sql+=" and language_name='"+language+"'";
		}
		if(type!=null&&!type.equalsIgnoreCase("")){
			sql+=" and runplan_type_id='"+type+"'";
		}
		//是否处理
		if (handle != null && handle.equals("0")){
			sql += " and counts is null   ";
		}else if (handle !=null && handle.equals("1")){
			sql += " and counts is not null ";
		}
		sql += " order by stream_start_datetime desc ";
		
		
		ASObject resultObj = null;
		try {
			
			resultObj = StringTool.pageQuerySql(sql, obj);
			//效果录音每半个小时只取其中一条,
			//map用来记录效果录音是是否取过,
			//只做标记使用.
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			
			ArrayList<ASObject> resultList = (ArrayList)resultObj.get("resultList");
			for(int i=0;i<resultList.size();i++){
				ASObject rowObj = (ASObject)resultList.get(i);
				
				String result_id = (String)rowObj.get("result_id");
				String band = (String)rowObj.get("task_band");
				String task_id = (String)rowObj.get("task_id");
				String start_datetime = (String)rowObj.get("start_datetime");
				String end_datetime = (String)rowObj.get("end_datetime");
				String frequency = (String)rowObj.get("task_freq");
				String filename = (String)rowObj.get("filename");
				String filesize = (String)rowObj.get("filesize");
				String head_id = (String)rowObj.get("head_id");
				String head_name=(String)rowObj.get("headname");
				String url = (String)rowObj.get("url");

				String report_type = (String)rowObj.get("report_type");
				String is_stored = (String)rowObj.get("is_stored");
//				String is_delete = (String)rowObj.get("is_delete");
				String store_datetime = (String)rowObj.get("task_store_datetime");
				String mark_file_name = (String)rowObj.get("mark_file_name");
				String runplan_id = (String)rowObj.get("runplan_id");
				String equ_code = (String)rowObj.get("equ_code");
				String runplan_type_id = (String)rowObj.get("runplan_type_id");
				RadioStreamResultBean rsrb = new  RadioStreamResultBean();
				//是运行图任务，把运行图取回
				if (!runplan_id.equals("")){
					RunplanBean  runplanBean = new RunplanBean();
					
					runplanBean.setRunplan_id((String)rowObj.get("runplan_id"));
					runplanBean.setRunplan_type_id((String)rowObj.get("runplan_type_id"));
					runplanBean.setStation_id((String)rowObj.get("station_id"));
					runplanBean.setTransmiter_no((String)rowObj.get("transmiter_no"));
					runplanBean.setFreq((String)rowObj.get("freq"));
					runplanBean.setValid_start_time((String)rowObj.get("valid_start_time"));
					runplanBean.setValid_end_time((String)rowObj.get("valid_end_time"));
					runplanBean.setDirection((String)rowObj.get("direction"));
					runplanBean.setPower((String)rowObj.get("power"));
					runplanBean.setService_area((String)rowObj.get("service_area"));
					runplanBean.setAntennatype((String)rowObj.get("antennatype"));
					runplanBean.setRest_datetime((String)rowObj.get("rest_datetime"));
					runplanBean.setRest_time((String)rowObj.get("rest_time"));
					runplanBean.setSentcity_id((String)rowObj.get("sentcity_id"));
					runplanBean.setStart_time((String)rowObj.get("start_time"));
					runplanBean.setEnd_time((String)rowObj.get("end_time"));
					runplanBean.setSatellite_channel((String)rowObj.get("satellite_channel"));
					runplanBean.setStore_datetime((String)rowObj.get("store_datetime"));
					runplanBean.setProgram_type_id((String)rowObj.get("program_type_id"));
					runplanBean.setLanguage_id((String)rowObj.get("language_id"));
					runplanBean.setWeekday((String)rowObj.get("weekday"));
					runplanBean.setInput_person((String)rowObj.get("input_person"));
					runplanBean.setRevise_person((String)rowObj.get("revise_person"));
					runplanBean.setRemark((String)rowObj.get("remark"));
					runplanBean.setProgram_id((String)rowObj.get("program_id"));
				  	runplanBean.setMon_area((String)rowObj.get("mon_area"));
				  	runplanBean.setBand((String)rowObj.get("band"));
				  	runplanBean.setProgram_type((String)rowObj.get("program_type"));
				  	runplanBean.setRedisseminators((String)rowObj.get("redisseminators"));
				  	runplanBean.setLocal_time((String)rowObj.get("local_time"));
				  	runplanBean.setSummer((String)rowObj.get("summer"));
				  	runplanBean.setSummer_starttime((String)rowObj.get("summer_starttime"));
				  	runplanBean.setSummer_endtime((String)rowObj.get("summer_endtime"));
				  	runplanBean.setSeason_id((String)rowObj.get("season_id"));
				  	runplanBean.setAntenna((String)rowObj.get("antenna"));
				  	if(runplan_type_id.equals("1")){
				  		runplanBean.setStation_name((String)rowObj.get("station_name"));
					}else{
						runplanBean.setStation_name((String)rowObj.get("redisseminators"));
						
					}
//				  	runplanBean.setStation_name((String)rowObj.get("station_name"));
				  	runplanBean.setCiraf((String)rowObj.get("ciraf"));
//				  	runplanBean.setShortname((String)rowObj.get("shortname"));
//				  	runplanBean.setSendcity((String)rowObj.get("sendcity"));
			  	    runplanBean.setLanguage_name((String)rowObj.get("language_name"));
				  	runplanBean.setProgram_name((String)rowObj.get("program_name"));
			
//				  	runplanBean.setType((String)rowObj.get("type"));
//				  	runplanBean.setRunplanType((String)rowObj.get("runplanType"));
				  	rsrb.setRunplanBean(runplanBean);
				}
				String mark_id = (String)rowObj.get("mark_id");
				//该录音打过分，取回
				if (!mark_id.equals("")){
					RadioMarkZstViewBean rmzvb = new RadioMarkZstViewBean();

					rmzvb.setMark_id((String)rowObj.get("mark_id"));
					rmzvb.setMark_user((String)rowObj.get("mark_user"));
					rmzvb.setMark_datetime((String)rowObj.get("mark_datetime"));
					rmzvb.setHead_code((String)rowObj.get("head_code"));
					rmzvb.setEqu_code((String)rowObj.get("equ_code"));
					rmzvb.setFrequency((String)rowObj.get("frequency"));
					rmzvb.setRunplan_id((String)rowObj.get("runplan_id"));
					rmzvb.setCounti((String)rowObj.get("counti"));
					rmzvb.setCounto((String)rowObj.get("counto"));
					rmzvb.setCounts((String)rowObj.get("counts"));
					rmzvb.setDescription((String)rowObj.get("description"));
					rmzvb.setMark_type((String)rowObj.get("mark_type"));
					rmzvb.setEdit_user((String)rowObj.get("edit_user"));
					rmzvb.setUnit((String)rowObj.get("unit"));
					rmzvb.setMark_file_url((String)rowObj.get("mark_file_url"));
					rmzvb.setFile_name((String)rowObj.get("file_name"));
					rmzvb.setFile_length((String)rowObj.get("file_length"));
					rmzvb.setRecord_start_time((String)rowObj.get("record_start_time"));
					rmzvb.setRecord_end_time((String)rowObj.get("record_end_time"));
					rmzvb.setStation_id((String)rowObj.get("station_id"));
					if(runplan_type_id.equals("1")){
						rmzvb.setStation_name((String)rowObj.get("station_name"));
					}else{
						rmzvb.setStation_name((String)rowObj.get("redisseminators"));
					}
					
					rmzvb.setHeadname((String)rowObj.get("headname"));
					rmzvb.setPlay_time((String)rowObj.get("play_time"));
					rmzvb.setTask_id((String)rowObj.get("task_id"));
					rmzvb.setTask_name((String)rowObj.get("task_name"));
					rmzvb.setLevel_value((String)rowObj.get("level_value"));
					rmzvb.setFm_value((String)rowObj.get("fm_value"));
					rmzvb.setAm_value((String)rowObj.get("am_value"));
					rmzvb.setOffeset_value((String)rowObj.get("offeset_value"));
					rmzvb.setRemark((String)rowObj.get("mark_remark"));
					
					rmzvb.setAsr_type((String)rowObj.get("asr_type"));
					rmzvb.setResult_type((String)rowObj.get("result_type"));
					rmzvb.setWavelen((String)rowObj.get("wavelen"));
					rmzvb.setMusicratio((String)rowObj.get("musicratio"));
					rmzvb.setNoiseratio((String)rowObj.get("noiseratio"));
					rmzvb.setSpeechlen((String)rowObj.get("speechlen"));
					rmzvb.setStatus((String)rowObj.get("status"));
					rmzvb.setTotalcm((String)rowObj.get("totalcm"));
					rmzvb.setAudibilityscore((String)rowObj.get("audibilityscore"));
					rmzvb.setAudibilityconfidence((String)rowObj.get("audibilityconfidence"));
					rmzvb.setChannelname((String)rowObj.get("channelname"));
					rmzvb.setChannelnameconfidence((String)rowObj.get("channelnameconfidence"));
					rmzvb.setProgramname((String)rowObj.get("programname"));
					rmzvb.setProgramnameconfidence((String)rowObj.get("programnameconfidence"));
					rmzvb.setLanguageconfidence1((String)rowObj.get("languageconfidence1"));
					rmzvb.setLanguageconfidence2((String)rowObj.get("languageconfidence2"));
					rmzvb.setLanguageconfidence3((String)rowObj.get("languageconfidence3"));
					rmzvb.setLanguageconfidence4((String)rowObj.get("languageconfidence4"));
					rmzvb.setLanguageconfidence5((String)rowObj.get("languageconfidence5"));
					rmzvb.setLanguagename1((String)rowObj.get("languagename1"));
					rmzvb.setLanguagename2((String)rowObj.get("languagename2"));
					rmzvb.setLanguagename3((String)rowObj.get("languagename3"));
					rmzvb.setLanguagename4((String)rowObj.get("languagename4"));
					rmzvb.setLanguagename5((String)rowObj.get("languagename5"));
					rsrb.setRadioMarkZstViewBean(rmzvb);
				}
				rsrb.setHead_name(head_name);
				rsrb.setResult_id(result_id);
				rsrb.setBand(band);
				rsrb.setTask_id(task_id);
				rsrb.setStart_datetime(start_datetime);
				rsrb.setEnd_datetime(end_datetime);
				rsrb.setFrequency(frequency);
				rsrb.setFilename(filename);
				rsrb.setFilesize(filesize);
				rsrb.setHead_id(head_id);
				rsrb.setUrl(url);
				rsrb.setReport_type(report_type);
				rsrb.setIs_stored(is_stored);
				rsrb.setStore_datetime(store_datetime);
				rsrb.setMark_file_name(mark_file_name);
				rsrb.setRunplan_id(runplan_id);
				rsrb.setEqu_code(equ_code);
				list.add(rsrb);
			}
			
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("", "[站点:" + headCode + "]查询录音失败|"
					+ e.getMessage(), "");
		} catch (GDSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("", "[站点:" + headCode + "]查询录音失败|"
					+ e.getMessage(), "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("", "[站点:" + headCode + "]查询录音失败|"
					+ e.getMessage(), "");
		}
		resultObj.put("resultList",list);
		return resultObj;
	}
	public Object deleteAudioFile(ASObject object){
		String dellist=(String)object.get("dellist");
		String sql="update radio_stream_result_tab  set is_delete=1 where result_id in ("+dellist+")";
		try{
			DbComponent.exeUpdate(sql);
		 }catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("","后台错误!",null);
		}
		 return " ";
	}
	/**
	 * 收听效果打分入库
	 * 
	 * @param
	 * @param
	 * @return
	 */
	public  Object setMark(RadioMarkZstViewBean radioMarkZstViewBean) {

		String s = "select radio_mark_zst_seq.nextval val from dual ";
		String val = "";
		try {
			GDSet set = DbComponent.Query(s);
			val = set.getString(0, "val");
		} catch (DbException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (GDSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StringBuffer sql = new StringBuffer();


		sql.append(" insert into radio_mark_zst_view_tab(mark_id, mark_user, mark_datetime, head_code,equ_code, frequency, runplan_id, counti, ");
		sql.append(" counto, counts, description, mark_type, edit_user, unit,mark_file_url, file_name, file_length, record_start_time, ");
		sql.append(" record_end_time, station_id, station_name, headname,  play_time, task_id, task_name, level_value, ");
		sql.append(" fm_value, am_value,offset_value,remark,asr_type, result_type, status, wavelen, musicratio, noiseratio, ");
		sql.append(" speechlen, totalcm, audibilityscore, audibilityconfidence, channelname, channelnameconfidence, programname,");
		sql.append(" programnameconfidence, languagename1, languagename2, languagename3, languagename4, languagename5, ");
		sql.append(" languageconfidence1, languageconfidence2, languageconfidence3, languageconfidence4, languageconfidence5) ");
		sql.append(" values(?,?,sysdate,?,?, ?,?,?, ");
		sql.append(" ?,?,?,?,?,?,?,?,?, ?, ");
		sql.append(" ?,?,?,?,?,?,?,?, ");
		sql.append(" ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");

		DbComponent db = new DbComponent();
		DbComponent.DbQuickExeSQL prepExeSQL = db.new DbQuickExeSQL(sql.toString());

		try {
			prepExeSQL.getConnect();			
			prepExeSQL.setString(1, val);
			prepExeSQL.setString(2, radioMarkZstViewBean.getMark_user());
			prepExeSQL.setString(3, radioMarkZstViewBean.getHead_code());
			prepExeSQL.setString(4, radioMarkZstViewBean.getEqu_code());
			prepExeSQL.setString(5, radioMarkZstViewBean.getFrequency());
			prepExeSQL.setString(6, setDefaultValue(radioMarkZstViewBean.getRunplan_id()));
			prepExeSQL.setString(7, radioMarkZstViewBean.getCounti());
			prepExeSQL.setString(8, radioMarkZstViewBean.getCounto());
			prepExeSQL.setString(9, radioMarkZstViewBean.getCounts());
			prepExeSQL.setString(10, radioMarkZstViewBean.getDescription());
			prepExeSQL.setString(11, radioMarkZstViewBean.getMark_type());
			prepExeSQL.setString(12, radioMarkZstViewBean.getEdit_user());
			prepExeSQL.setString(13, radioMarkZstViewBean.getUnit());
			prepExeSQL.setString(14, radioMarkZstViewBean.getMark_file_url());
			prepExeSQL.setString(15, radioMarkZstViewBean.getFile_name());
			prepExeSQL.setString(16, radioMarkZstViewBean.getFile_length());

			prepExeSQL.setString(17, getDateString(radioMarkZstViewBean.getRecord_start_time()));
			prepExeSQL.setString(18, getDateString(radioMarkZstViewBean.getRecord_end_time()));
			prepExeSQL.setString(19, setDefaultValue(radioMarkZstViewBean.getStation_id()));
			prepExeSQL.setString(20, setDefaultValue(radioMarkZstViewBean.getStation_name()));
			String headendname = radioMarkZstViewBean.getHeadname();
			//if (radioMarkZstViewBean.getTask_id().equals("102"))
			 if(headendname.endsWith("A")||headendname.endsWith("B")||headendname.endsWith("C")||headendname.endsWith("D")||headendname.endsWith("E")||headendname.endsWith("F")||headendname.endsWith("G"))
					headendname = headendname.substring(0, headendname.length()-1);
			//headendname = headendname.endsWith("A")?headendname.substring(0, headendname.length()-1):(headendname.endsWith("B")?headendname.substring(0, headendname.length()-1):(headendname.endsWith("C")?headendname.substring(0, headendname.length()-1):(headendname.endsWith("D")?headendname.substring(0, headendname.length()-1):headendname);
			prepExeSQL.setString(21, headendname);
			prepExeSQL.setString(22, radioMarkZstViewBean.getPlay_time().equals("-")?"":radioMarkZstViewBean.getPlay_time());
			prepExeSQL.setString(23, radioMarkZstViewBean.getTask_id());
			prepExeSQL.setString(24, radioMarkZstViewBean.getTask_name());
			prepExeSQL.setString(25, setDefaultValue(radioMarkZstViewBean.getLevel_value()));
			prepExeSQL.setString(26, setDefaultValue(radioMarkZstViewBean.getFm_value()));
			prepExeSQL.setString(27, setDefaultValue(radioMarkZstViewBean.getAm_value()));
			prepExeSQL.setString(28, setDefaultValue(radioMarkZstViewBean.getOffeset_value()));
			prepExeSQL.setString(29, radioMarkZstViewBean.getRemark());
			prepExeSQL.setString(30, radioMarkZstViewBean.getAsr_type());
			prepExeSQL.setString(31, radioMarkZstViewBean.getResult_type());
			prepExeSQL.setString(32, radioMarkZstViewBean.getStatus());
			prepExeSQL.setString(33, radioMarkZstViewBean.getWavelen());
			prepExeSQL.setString(34, radioMarkZstViewBean.getMusicratio());
			prepExeSQL.setString(35, radioMarkZstViewBean.getNoiseratio());
			prepExeSQL.setString(36, radioMarkZstViewBean.getSpeechlen());
			prepExeSQL.setString(37, radioMarkZstViewBean.getTotalcm());
			prepExeSQL.setString(38, radioMarkZstViewBean.getAudibilityscore());
			prepExeSQL.setString(39, radioMarkZstViewBean.getAudibilityconfidence());
			prepExeSQL.setString(40, radioMarkZstViewBean.getChannelname());
			prepExeSQL.setString(41, radioMarkZstViewBean.getChannelnameconfidence());
			prepExeSQL.setString(42, radioMarkZstViewBean.getProgramname());
			prepExeSQL.setString(43, radioMarkZstViewBean.getProgramnameconfidence());
			prepExeSQL.setString(44, radioMarkZstViewBean.getLanguagename1());
			prepExeSQL.setString(45, radioMarkZstViewBean.getLanguagename2());
			prepExeSQL.setString(46, radioMarkZstViewBean.getLanguagename3());
			prepExeSQL.setString(47, radioMarkZstViewBean.getLanguagename4());
			prepExeSQL.setString(48, radioMarkZstViewBean.getLanguagename5());
			prepExeSQL.setString(49, radioMarkZstViewBean.getLanguageconfidence1());
			prepExeSQL.setString(50, radioMarkZstViewBean.getLanguageconfidence2());
			prepExeSQL.setString(51, radioMarkZstViewBean.getLanguageconfidence3());
			prepExeSQL.setString(52, radioMarkZstViewBean.getLanguageconfidence4());
			prepExeSQL.setString(53, radioMarkZstViewBean.getLanguageconfidence5());
			prepExeSQL.exeSQL();

		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("", "[站点:"
					+ radioMarkZstViewBean.getHead_code() + "]打分失败|原因:"
					+ e.getMessage(), radioMarkZstViewBean);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("", "[站点:"
					+ radioMarkZstViewBean.getHead_code() + "]打分失败|原因:"
					+ e.getMessage(), radioMarkZstViewBean);
		} finally {
			prepExeSQL.closeConnect();
		}

		return "打分完成,ID="+val;
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
	public Object updateMark(RadioMarkZstViewBean rmzvBean) {
		
		if (rmzvBean == null)
			return new EXEException("", "修改打分失败,打分对象为null|原因:", "");
		StringBuffer sql = new StringBuffer();
		sql.append(" update radio_mark_zst_view_tab ");
		sql.append(" set mark_user = '" + rmzvBean.getMark_user() + "' ");
		sql.append(" ,mark_datetime = to_date('" + rmzvBean.getMark_datetime() + "','yyyy-mm-dd hh24:mi:ss') ");
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
		if (rmzvBean.getMark_file_url() != null)
			sql.append(" ,mark_file_url = '" + rmzvBean.getMark_file_url() + "' ");
		if (rmzvBean.getFile_name() != null)
			sql.append(" ,file_name = '" + rmzvBean.getFile_name() + "' ");
		if (rmzvBean.getFile_length() != null)
			sql.append(" ,file_length = '" + rmzvBean.getFile_length() + "' ");
		if (rmzvBean.getRecord_start_time() != null)
			sql.append(" ,record_start_time = '" + rmzvBean.getRecord_start_time() + "' ");
		if (rmzvBean.getRecord_end_time() != null)
			sql.append(" ,record_end_time = '" + rmzvBean.getRecord_end_time()+ "' ");
		if (rmzvBean.getStation_id() != null)
			sql.append(" ,station_id = '" + rmzvBean.getStation_id() + "' ");
		if (rmzvBean.getStation_name() != null)
			sql.append(" ,station_name = '" + rmzvBean.getStation_name() + "' ");
		if (rmzvBean.getHeadname() != null)
			sql.append(" ,headname = '" + rmzvBean.getHeadname() + "' ");
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
		sql.append(" where mark_id = '" + rmzvBean.getMark_id() + "' and head_code = '"	+ rmzvBean.getHead_code() + "' ");

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
	 * 向语音识别发接口
	 * <p>class/function:com.viewscenes.web.online
	 * <p>explain:
	 * <p>author-date:谭长伟 2012-7-13
	 * @param:
	 * @return:
	 */
	public Object sendCmdASRClient(ASObject obj){
		String url = (String)obj.get("url");
		ResHeadendBean headendBean = (ResHeadendBean)obj.get("headend");
		String collectChannel = (String)obj.get("collectChannel");
		String language = (String)obj.get("language");
		String freq = (String)obj.get("freq");
		String start_datetime = (String)obj.get("start_datetime");
		String end_datetime = (String)obj.get("end_datetime");
		ASRClient client = new ASRClient();
		
		//为null代表是45秒录音语音识别
		if (start_datetime == null || end_datetime == null){
			String path = SystemConfig.getRecord45SecPath();
			String address = StringTool.getIpAndPortByUrl(url);
			String fileName = get45SecFileName(address.split(":")[0],Integer.parseInt(address.split(":")[1]));
			File mp3File = new File(path+fileName);
			if (mp3File.exists()){
				
				Date sdate = new Date(mp3File.lastModified());
				//当前读取的本地的录音文件已超过当前时间60秒，而新的录音文件还没有生成
				if (System.currentTimeMillis() - sdate.getTime() > 90*1000){
					return new EXEException("", "录音文件："+path+fileName+"太旧,请稍后再试", "");
				}
				
				int sec = Mp3Utils.getMp3Duration(mp3File);
				
				if (sec == 0)
					return new EXEException("", "录音文件时间读取出错,录音文件时间为:"+sec, "");
				
				if (sec < 45)
					return new EXEException("", "录音时间不足45秒,无法发送给语音识别系统,请稍后再试", "");
				
				
				Date edate = new Date(sdate.getTime() + sec * 1000);
				
				ASRCmdBean asrCmdBean = new ASRCmdBean();
				String fileStartTime = StringTool.Date2String(sdate);
				String fileEndTime = StringTool.Date2String(edate);
				
				
				asrCmdBean.setType("TaskStatus");
				asrCmdBean.setWfType("BC573");
				String filepath = SystemConfig.getRecord45SecUrl() + fileName;
				asrCmdBean.setFile(filepath);
				
				asrCmdBean.setFileStartTime(fileStartTime);
				asrCmdBean.setFileEndTime(fileEndTime);
				
				asrCmdBean.setTaskStartTime(fileStartTime);
				asrCmdBean.setTaskEndTime(fileEndTime);
				
				asrCmdBean.setLanguage(language);
				asrCmdBean.setFreq(freq);
				
				asrCmdBean.setCollectChannel(collectChannel);
				asrCmdBean.setCollectMethod(headendBean.getType_id().equalsIgnoreCase("101")?"采集点实时":"遥控站实时");
					
				ASRResBean asrResBean = client.exucuteTask(asrCmdBean);
				
				return asrResBean;
			}
		//用户手动进行语音识别	
		}else{
			ASRCmdBean asrCmdBean = new ASRCmdBean();
			
			
			asrCmdBean.setType("TaskStatus");
			asrCmdBean.setWfType("BC573");
			asrCmdBean.setFile(url);
			
			asrCmdBean.setFileStartTime(start_datetime);
			asrCmdBean.setFileEndTime(end_datetime);
			
			asrCmdBean.setTaskStartTime(start_datetime);
			asrCmdBean.setTaskEndTime(end_datetime);
			
			asrCmdBean.setLanguage(language);
			asrCmdBean.setFreq(freq);
			
			asrCmdBean.setCollectChannel(collectChannel);
			asrCmdBean.setCollectMethod(headendBean.getType_id().equalsIgnoreCase("101")?"采集点实时":"遥控站实时");
				
			ASRResBean asrResBean = client.exucuteTask(asrCmdBean);
			
			return asrResBean;
		}
		
		
		return new EXEException("", "没有语音识别可用的文件,请稍后再试", "");
		
	}
	public static String get45SecFileName(String ip,int port){
		String fileName = ip + "_" + port+".mp3";
		return fileName;
	}
	private String setDefaultValue(String value){
		if(value==null){
			return "";
		}
		return value;
	}
	private String getDateString(String date){
		if(date==null||date.length()==0){
			return "00:00:00";
		}
		int n=date.length();
		return date.substring(n-8, n);
	}
}
