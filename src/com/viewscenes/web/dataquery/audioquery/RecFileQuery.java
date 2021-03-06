package com.viewscenes.web.dataquery.audioquery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.jmask.web.controller.EXEException;

import com.viewscenes.bean.RadioMarkZstViewBean;
import com.viewscenes.bean.RadioStreamResultBean;
import com.viewscenes.bean.FileRetrieveResult;
import com.viewscenes.bean.runplan.RunplanBean;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.pub.GDSet;
import com.viewscenes.pub.GDSetException;
import com.viewscenes.util.StringTool;
import com.viewscenes.util.business.SiteVersionUtil;

import flex.messaging.io.amf.ASObject;

/**
 * 录音文件数据查询
 * @author thinkpad
 * 10.15.6.11/12
 */
public class RecFileQuery {
	 public static Map<String , String> userMap ; 
	 static  
	    {  
		 userMap = new HashMap<String, String>();  
		 userMap.put("12762", "望开力");  
		 userMap.put("12766", "许涛");  
		 userMap.put("12767", "于洁");  
		 userMap.put("12769", "田雅静");  
		 userMap.put("12771", "张华斌");  
		 userMap.put("12770", "张嘉");  
		 userMap.put("12774", "甘宁");  
		 userMap.put("12781", "lipeng");  
		 userMap.put("12860", "test");  
		 userMap.put("221", "admin");  
		 userMap.put("12760", "张锦利");  
		 userMap.put("12960", "李珊");  
		 userMap.put("12802", "zhzbs");  
		 userMap.put("12920", "杜冉");  
		 userMap.put("41142", "xt");  
		 userMap.put("33000", "aaaaaa");  
		 userMap.put("34460", "xiamen");  
		 userMap.put("41100", "zbs");  
		 userMap.put("41140", "db");  
		 userMap.put("13364", "张波");  
		 userMap.put("41143", "tyj");  
		 userMap.put("41144", "wkl");  
		 userMap.put("41145", "zyq");  
		 userMap.put("41146", "zjl");  
		 userMap.put("13365", "许振");  
		 userMap.put("13369", "马丽");  
		 userMap.put("41150", "yj");  
		 userMap.put("41151", "dr");  
		 userMap.put("41222", "taishun");  
		 userMap.put("41224", "713");  
		 userMap.put("13370", "寇金涛");  
		 userMap.put("68720", "ts");  
		 userMap.put("72125", "guojitai");  
		 userMap.put("85124", "wbj");  
		 userMap.put("13300", "甄亚卿");  
		 userMap.put("13301", "彭洋");  
		 userMap.put("13371", "王洪波");  
		 userMap.put("13372", "白艳玲");  
		 userMap.put("13373", "罗立川");  
		 userMap.put("13375", "侯丽利");  
		 userMap.put("13376", "王宝君");  
		 userMap.put("13377", "马玉红");  
		 userMap.put("13361", "闫迪");  
		 userMap.put("13140", "邓柏");  
		 userMap.put("67660", "fyx");  
		 userMap.put("41147", "ls");  
		 userMap.put("41223", "jinshi");  
		 userMap.put("80205", "duoxindao");  
		 userMap.put("80265", "292");  
		 userMap.put("80267", "293");  
		 userMap.put("81408", "gn");  
		 userMap.put("81564", "juxing");  
		 userMap.put("85632", "mzx");  
		 userMap.put("85633", "py");  
		 userMap.put("85634", "hll");  
		 userMap.put("85635", "ml");  
		 userMap.put("85636", "wj");  
		 userMap.put("85637", "kjt");  
		 userMap.put("85638", "byl");  
		 userMap.put("85639", "whb");  
		 userMap.put("85640", "myh");  
		 userMap.put("85641", "ll");
		 userMap.put("85642", "ycm");  
		 userMap.put("13340", "陈涛");  
		 userMap.put("13360", "王晶");  
		 userMap.put("13362", "郭冀明");  
		 userMap.put("13363", "周悦");  
		 userMap.put("13366", "胡婧");  
		 userMap.put("13367", "杨春梅");  
		 userMap.put("13368", "崔梦月");  
		 userMap.put("13374", "莫志贤");  
		 userMap.put("13378", "廖文华");  
		 
	    } 

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
			String headType=(String)obj.get("headtype");
			String station=(String)obj.get("station");
			String language=(String)obj.get("language");
			String type = (String) obj.get("runplanType");//节目类型 ：国际台或者海外落地
			String sql = "";
			sql = " select t.task_id,t.equ_code,t.frequency,result_id,t.band task_band,start_datetime,end_datetime,t.frequency task_freq,filename,filesize, t.head_id, ";
			sql += " t.url,report_type,is_stored,t.store_datetime task_store_datetime,mark_file_name,t.equ_code task_equcode,run.*, " ;
			sql += " mark_id, mark_user, mark_datetime, head_code, counti, counto, counts, description, mark_type, edit_user, " ;
			sql += " unit, mark_file_url, file_name, file_length, record_start_time, record_end_time, hd.shortname headname, ";
			sql += " play_time, task_name, t.level_value, fm_value, am_value,  mark.remark mark_remark  ";
			sql += " from radio_stream_result_tab t,radio_mark_zst_view_tab mark, ";
			sql += " (select run.*, p.program_name ,lan.language_name";
			sql += " from zres_runplan_chaifen_tab run, zdic_language_tab lan, zdic_program_name_tab p ";
			sql += " where run.language_id = lan.language_id(+) ";
			sql += " and run.program_id = p.program_id(+) ";
		//	sql += " and sysdate between run.valid_start_time and run.valid_end_time ";
			sql += " ) run, res_headend_tab hd ";
			sql += " where t.head_id = hd.head_id ";
			sql += " and t.url = mark.mark_file_url ";
			sql += " and t.runplan_id = run.runplan_id(+) ";
			sql += " and t.is_delete = 0 ";
			if(headCode!=null&&headCode.length()!=0){
				String headids = SiteVersionUtil.getSiteHeadIdsByCodeNoAB(headCode);
		       	sql += " and t.head_id in("+headids+") ";
			}
	       	if (freq != null && !freq.equals(""))
	       		sql += " and t.frequency = "+freq+" ";
			if(recType!=null&&!recType.equals("")){
				if(recType.equals("-1")){
					sql+=" and t.report_type in (0,1)";
				}else{
					sql+=" and t.report_type="+recType+" ";
				}
			}
			if (dateType.equals("byDate")){
//				sql += " and to_date('"+date+"','yyyy-mm-dd hh24:mi:ss') between t.start_datetime and t.end_datetime ";
				sql += " and t.start_datetime >= to_date('"+date+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss') ";
				sql += "  and t.end_datetime <= to_date('"+date+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') ";
				
	           
			}else{
				sql += " and (to_date('"+startDateTime+"','yyyy-mm-dd hh24:mi:ss') <= t.start_datetime and to_date('"+endDateTime+"','yyyy-mm-dd hh24:mi:ss') >= t.end_datetime) ";
			}
			if(headType!=null&&headType.length()!=0){
				sql+=" and hd.type_id="+headType;
			}
			if(station!=null&&station.length()!=0){
				sql+=" and run.station_id='"+station+"'";
			}
			if(language!=null&&language.length()!=0){
				sql+=" and run.language_name='"+language+"'";
			}
			if(type!=null&&!type.equalsIgnoreCase("")){
				sql+=" and run.runplan_type_id='"+type+"'";
			}
			//是否处理
			if (handle != null && handle.equals("0")){//or AUDIBILITYCONFIDENCE <100
				sql += " and (mark_id = null  or (counts is null or counti is null or counto is null )) ";
			}else if (handle !=null && handle.equals("1")){
				sql += " and (counts is not null and counti is not null and counto is not null) ";
			}
			sql += " and t.is_delete = 0 order by t.start_datetime asc ";
			
			
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
//					File file=new File(url);
//					if(file.exists()){
//						System.out.println("file exist");
//					}else{
//						System.out.println("file not exist");
//					}
					String report_type = (String)rowObj.get("report_type");
					String is_stored = (String)rowObj.get("is_stored");
//					String is_delete = (String)rowObj.get("is_delete");
					String store_datetime = (String)rowObj.get("task_store_datetime");
					String mark_file_name = (String)rowObj.get("mark_file_name");
					String runplan_id = (String)rowObj.get("runplan_id");
					String equ_code = (String)rowObj.get("equ_code");
					String runplan_type_id = (String)rowObj.get("runplan_type_id");
					
					//效果录音每半个小时只需要显示一条,质量无限制
//					if (recType.equalsIgnoreCase("effect")){
//						Date d = StringTool.stringToDate(start_datetime);
//						int year = d.getYear();
//						int month = d.getMonth();
//						int day = d.getDate();
//						int hour = d.getHours();
//						int min = d.getMinutes();
//						
//						if (min >30)
//							min = 30;
//						else
//							min = 0;
//						
//						String key = year + month + day +hour + min + ""; 
//						
//						Object  o = map.get(key);
//						
//						if (o == null){
//							map.put(key, 1);
//						}else{
//							continue;
//						}
//						
//					}
					
					
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
//					  	runplanBean.setStation_name((String)rowObj.get("station_name"));
					  	runplanBean.setCiraf((String)rowObj.get("ciraf"));
//					  	runplanBean.setShortname((String)rowObj.get("shortname"));
//					  	runplanBean.setSendcity((String)rowObj.get("sendcity"));
				  	    runplanBean.setLanguage_name((String)rowObj.get("language_name"));
					  	runplanBean.setProgram_name((String)rowObj.get("program_name"));
				
//					  	runplanBean.setType((String)rowObj.get("type"));
//					  	runplanBean.setRunplanType((String)rowObj.get("runplanType"));
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
						//更新人处理
						String edit_user = (String)rowObj.get("edit_user");
						if(edit_user!=null&&!"".equals(edit_user)){
							if(userMap.get(edit_user)!=null){
								edit_user = userMap.get(edit_user);
							}
						}
						rmzvb.setEdit_user(edit_user);
						rmzvb.setUnit((String)rowObj.get("unit"));
						String mark_file_url  = (String)rowObj.get("mark_file_url");
						if(mark_file_url!=null&&mark_file_url.indexOf("10.15.5.15")!=-1){
							mark_file_url= mark_file_url.replace("10.15.5.15", "10.15.5.31");
						}
						rmzvb.setMark_file_url(mark_file_url);
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
//						rmzvb.setLanguage_name((String)rowObj.get("language_name"));
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
					if(url!=null&&url.indexOf("10.15.5.15")!=-1){
						url= url.replace("10.15.5.15", "10.15.5.31");
					}
					rsrb.setUrl(url);
					rsrb.setReport_type(report_type);
					rsrb.setIs_stored(is_stored);
					rsrb.setStore_datetime(store_datetime);
					rsrb.setMark_file_name(mark_file_name);
					rsrb.setRunplan_id(runplan_id);
					rsrb.setEqu_code(equ_code);
					rsrb.setFmModulation((String)rowObj.get("fm_value"));
					rsrb.setAmModulation((String)rowObj.get("am_value"));
					rsrb.setLevelValue((String)rowObj.get("level_value"));
					
					
					
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
		  public static boolean isInteger(String str) {  
		        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
		        return pattern.matcher(str).matches();  
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
	 * 向语音识别发接口
	 * <p>class/function:com.viewscenes.web.online
	 * <p>explain:
	 * <p>author-date:谭长伟 2012-7-13
	 * @param:
	 * @return:
	 */
//	public Object sendCmdASRClient(ASObject obj){
//		RadioStreamResultBean radioStremResultBean = (RadioStreamResultBean)obj.get("radioStremResultBean");
//		ResHeadendBean headendBean = (ResHeadendBean)obj.get("headend");
////		RunplanBean runplanBean = (RunplanBean)obj.get("runplanBean");
//		
//		if (radioStremResultBean != null){
//			
//			ASRCmdBean asrCmdBean = new ASRCmdBean();
//			
//			asrCmdBean.setType("TaskStatus");
//			asrCmdBean.setWfType("BC573");
//			asrCmdBean.setFile(radioStremResultBean.getUrl());
//			
//			asrCmdBean.setFileStartTime(radioStremResultBean.getStart_datetime());
//			asrCmdBean.setFileEndTime(radioStremResultBean.getEnd_datetime());
//			
//			asrCmdBean.setTaskStartTime(radioStremResultBean.getStart_datetime());
//			asrCmdBean.setTaskEndTime(radioStremResultBean.getEnd_datetime());
//			
//			
//			asrCmdBean.setFreq(radioStremResultBean.getFrequency());
//			
//			String collectChannel = "";
//			String language = "";
//			RunplanBean runplanBean = radioStremResultBean.getRunplanBean();
//			//只有国际台运行图有通道号
//			if (runplanBean != null && runplanBean.getRunplan_type_id().equals("1")){
//				collectChannel = runplanBean.getSatellite_channel();
//				language = runplanBean.getLanguage_name();
//			}
//			asrCmdBean.setCollectChannel(collectChannel);
//			asrCmdBean.setLanguage(language);
//			
//			asrCmdBean.setCollectMethod(headendBean.getType_id().equalsIgnoreCase("101")?"采集点实时":"遥控站实时");
//				
//			ASRResBean asrResBean = ASRClient.exucuteTask(asrCmdBean);
//			
//			return asrResBean;
//		}
//		return new EXEException("", "没有语音识别可用的文件,请稍后再试", "");
//		
//	}
	
	/**
	 * 批量删除录音文件
	 * 操作项：1.删除打分数据、2.删除录音数据、3.删除物理录音文件
	 */
	public Object delSelectFile(ASObject obj){
		
		String ids = (String) obj.get("ids");
		String[] sqls =  new String[2];
	
		sqls[0] = " delete from radio_mark_zst_view_tab t where t.mark_file_url in(select url from " +
					"	radio_stream_result_tab radio where radio.result_id in("+ids+"))";
			
		sqls[1] = "update radio_stream_result_tab set is_delete=1 where result_id in( "+ids+") ";
	
		
		try {
			//删除打分、录音记录
			DbComponent.exeBatch(sqls);
			//删除物理文件
//			String querySql="select url from radio_stream_result_tab where result_id in( "+ids+")";
//	       GDSet set = DbComponent.Query(querySql);
//			
//			for(int i=0;i<set.getRowCount();i++){
//				 
//				 Zip.delFileFromFtp((String)set.getString(i, "url"));
//			}
			
			
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return new EXEException("", "删除录音文件异常,原因:"+e.getMessage(), "");
		} 
		
		return "删除录音文件成功";
		
	}
	/**
	 * 删除录音文件
	 * 操作项：1.删除打分数据、2.删除录音数据、3.删除物理录音文件
	 */
	public Object delRecFile(RadioStreamResultBean rsrb){
		
		String result_id = rsrb.getResult_id();
		String[] sqls =  new String[1];
		if (rsrb.getRadioMarkZstViewBean()!=null){
			String mark_id = rsrb.getRadioMarkZstViewBean().getMark_id();
			if (mark_id.equals(""))
				mark_id = "0";
			String marksql = " delete from radio_mark_zst_view_tab where mark_id = "+mark_id+" ";
			sqls = new String[2];
			sqls[0] = marksql;
		}
		
		String recsql = "update radio_stream_result_tab set is_delete=1 where result_id = "+result_id+" ";
		if (sqls.length == 2)
			sqls[1] = recsql;
		else
			sqls[0] = recsql;
		
		try {
			//删除打分、录音记录
			DbComponent.exeBatch(sqls);
			//删除物理文件
			//Zip.delFileFromFtp(rsrb.getUrl());
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return new EXEException("", "删除录音文件异常,原因:"+e.getMessage(), "");
		}
		
		return "删除录音文件成功";
		
	}
	public static void main(String[] args){
//		XmlReader xml = new XmlReader();
//		System.out.println(xml.getConfigItem("RunAt").getText());
		System.out.println(RecFileQuery.isInteger("qwe123"));
	}
	
	
	/**
	 * 获取未打分录音文件列表，
	 * maxCount: 获取的记录数
	 */
	public static synchronized ArrayList<FileRetrieveResult> getNotMarkRecordFileList(int maxCount){
		maxCount = maxCount < 1?500:maxCount;
		ArrayList<FileRetrieveResult> list = new ArrayList<FileRetrieveResult>();
		String sql = " select t.task_id,hd.code,t.url,t.start_datetime,t.end_datetime,t.frequency,t.report_type,t.filename,mark.mark_id,t.receive_type ";
		   sql +=    " from radio_stream_result_tab t, ";
		   sql +=    " radio_mark_zst_view_tab mark, ";
		   sql +=    " (select run.* from zres_runplan_tab run, zdic_language_tab lan ";
		   sql +=    " where run.is_delete =0 and run.language_id = lan.language_id(+) ";
		   sql +=    " and sysdate between run.valid_start_time and run.valid_end_time ";
		   sql +=    " ) run, ";
		   sql +=    " res_headend_tab hd ";
		   sql +=    " where t.head_id = hd.head_id ";
		   sql +=    " and t.start_datetime>sysdate-32 ";//七天以前的数据不进行语音识别了。
		   
		   sql +=    " and t.url = mark.mark_file_url(+) ";
		   sql +=    " and t.runplan_id = run.runplan_id(+) ";
		   sql +=    " and mark.mark_id is null ";
		   sql +=    " and t.is_delete = 0 ";
		   
		   sql +=    " order by t.start_datetime desc ";
		   sql= StringTool.pageSql(sql, 1, maxCount);
		
		try {
			GDSet set = DbComponent.Query(sql);
			
			for(int i=0;i<set.getRowCount();i++){
				FileRetrieveResult  result = new FileRetrieveResult();
				String task_id = (String)set.getString(i, "task_id");
				String code = (String)set.getString(i, "code");
				String url = (String)set.getString(i, "url");
				String start_datetime = (String)set.getString(i, "start_datetime");
				String end_datetime = (String)set.getString(i, "end_datetime");
				String frequency = (String)set.getString(i, "frequency");
				String report_type = (String)set.getString(i, "report_type");
				String mark_id = (String)set.getString(i, "mark_id");
				String filename = (String)set.getString(i, "filename");
				String receive_type = (String)set.getString(i, "receive_type");
				
				result.setTaskId(task_id);
				result.setHeadCode(code);
				result.setUrl(url);
				result.setStartDatetime(start_datetime);
				result.setEndDatetime(end_datetime);
				result.setFreq(frequency);
				result.setRecType(report_type);
				result.setFileName(filename);
				result.setReveiceType(receive_type);
				list.add(result);
			}
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GDSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   
		   
		return list;
	}
}
