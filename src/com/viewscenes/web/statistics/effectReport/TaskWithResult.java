package com.viewscenes.web.statistics.effectReport;

import com.viewscenes.bean.report.ZrstRepEffectStatisticsBean;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.pub.GDSet;
import flex.messaging.io.amf.ASObject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author liubin11
 * @version V1.0
 * @Package ${package_name}
 * @Description: (多任务处理统计)
 * @date 16-10-25 上午10:09
 */
public class TaskWithResult implements Callable<LinkedHashMap> {
        private String starttime;
        private String endtime;
        private String my_report_type;
        private String report_type;
        private ASObject asobj;
        private String report_id;
        private String type;

        public TaskWithResult(String starttime, String endtime, String my_report_type, String report_type, ASObject asobj, String report_id,String type) {
            this.starttime=starttime;
            this.endtime=endtime;
            this.my_report_type=my_report_type;
            this.report_type=report_type;
            this.asobj=asobj;
            this.report_id=report_id;
            this.type=type;

        }

        /**
         * 任务的具体过程，一旦任务传给ExecutorService的submit方法，则该方法自动在一个线程上执行。
         *
         * @return
         * @throws Exception
         */
        public LinkedHashMap call() throws Exception {
          if(this.type.equals("11"))//国际台广播效果统计表 或者海外落地效果统计
          {
              return  statisticsReportCondition11(starttime, endtime, my_report_type,report_type, asobj, report_id);
          }else if(this.type.equals("21"))//发射台总体播出效果统计1
          {
              return  statisticsReportCondition21(starttime, endtime, my_report_type,report_type, asobj, report_id);
          }else if(this.type.equals("22"))//22：发射台总体播出效果统计2；
          {
              return  statisticsReportCondition22(starttime, endtime, my_report_type, report_type, asobj, report_id);
          }else if(this.type.equals("23"))//23：发射台总体播出效果统计3；
          {
              return  statisticsReportCondition23(starttime, endtime, my_report_type, report_type, asobj, report_id);
          }
          else if(this.type.equals("31"))//31：语言总体播出效果统计1；
          {
              return  statisticsReportCondition31(starttime, endtime, my_report_type, report_type, asobj, report_id);
          }
          else if(this.type.equals("32"))//32：语言总体播出效果统计2；
          {
              return  statisticsReportCondition32(starttime, endtime, my_report_type, report_type, asobj, report_id);
          }else if(this.type.equals("41"))//41：各地区、各大洲、可听率统计；
          {
              return  statisticsReportCondition41(starttime, endtime, my_report_type, report_type, asobj, report_id);
          }
          else if(this.type.equals("51"))//51：各遥控站、各地区、各大洲、可保证收听频时统计；
          {
              return  statisticsReportCondition51(starttime, endtime, my_report_type, report_type, asobj, report_id);
          }
          else if(this.type.equals("61"))////61：各月可听率对比；
          {
              return  statisticsReportCondition61(starttime, endtime, my_report_type, report_type, asobj, report_id);
          }
          else if(this.type.equals("71"))//71：频率平均可听率统计表
          {
              return  statisticsReportCondition71(starttime, endtime, my_report_type, report_type, asobj, report_id);
          }
            return  null;
        }
    /**
     * 频率平均可听率统计表
     * @detail
     * @method
     * @param starttime
     * @param endtime
     * @param my_report_type
     * @param report_type
     * @param asobj
     * @return
     * @throws Exception
     * @return  HashMap
     * @author  zhaoyahui
     * @version 2013-1-10 下午07:32:41
     */
    public LinkedHashMap statisticsReportCondition71(String start, String end,String my_report_type,String report_type,ASObject asobj,String report_id) throws Exception{
        String headcodes = (String)asobj.get("headcodes");
        LinkedHashMap<String,ZrstRepEffectStatisticsBean> map = new LinkedHashMap<String,ZrstRepEffectStatisticsBean>();
        List<KeyValueForDate> list = getKeyValueForDate(start,end);
        for(int key=0;key<list.size();key++){
        	KeyValueForDate keyValue =list.get(key);
        	String sql  = "";
            if(my_report_type.equals("1")){//国际台
                sql = "select runplan.start_time ||'-'|| runplan.end_time as play_time,lan.language_name, runplan.freq,station.name stationname,runplan.direction," +
                        " runplan.power, head.code  headcode,head.service_name service_name_headend,head.type_id, mark.headname as shortname,mark.counto,runplan.service_area," +
                        " runplan.valid_start_time ,runplan.valid_end_time, dis.runplan_id as dis_runplan_id,dis.disturb " +
                        " from radio_mark_view_xiaohei mark," +
                        //	" radio_stream_result_tab record," +
                        " zres_runplan_chaifen_tab runplan," +
                        " zdic_language_tab lan ," +
                        " res_transmit_station_tab station," +
                        " res_headend_tab head," +
                        " (select * from zres_runplan_disturb_tab where  valid_end_time>to_date('"+keyValue.getEndDate()+"','yyyy-mm-dd hh24:mi:ss')) dis  "+
                        //" zres_runplan_disturb_tab dis " +
                        " where mark.runplan_id=runplan.runplan_id and  runplan.language_id=lan.language_id and mark.counto is not null and mark.counti is not null and mark.counts is not null " +
                        " and runplan.station_id=station.station_id and head.code=mark.head_code and head.is_delete =0  " +
                        " and mark.report_type=1 " +//效果录音
                        //" and runplan.is_delete=0 " +
                        " and runplan.valid_end_time>=to_date('"+keyValue.getEndDate()+"','yyyy-mm-dd hh24:mi:ss') "+

                        //	" and record.is_delete=0 " +
                        " and runplan.parent_id = dis.runplan_id(+) " +
                        //" and dis.is_delete=0 " +
                        " and runplan.runplan_type_id='"+my_report_type+"'"+
                        " and mark.start_datetime>=to_date('"+keyValue.getStartDate()+"','yyyy-mm-dd hh24:mi:ss')" +
                        " and mark.end_datetime<=to_date('"+keyValue.getEndDate()+"','yyyy-mm-dd hh24:mi:ss') ";
            } else if(my_report_type.equals("2")){//海外落地
                sql = "select runplan.start_time ||'-'|| runplan.end_time as play_time,lan.language_name, runplan.freq ,runplan.direction,runplan.redisseminators," +
                        " runplan.power, head.code  headcode,head.service_name service_name_headend,head.type_id, mark.headname as shortname,mark.counto,runplan.service_area," +
                        " runplan.valid_start_time ,runplan.valid_end_time, dis.runplan_id as dis_runplan_id,dis.disturb " +
                        " from radio_mark_view_xiaohei mark," +
                        //	" radio_stream_result_tab record," +
                        " zres_runplan_chaifen_tab runplan," +
                        " zdic_language_tab lan ," +
                        " res_headend_tab head," +
                        " zres_runplan_disturb_tab dis " +
                        " where mark.runplan_id=runplan.runplan_id and  runplan.language_id=lan.language_id and mark.counto is not null and mark.counti is not null and mark.counts is not null " +
                        " and head.code=mark.head_code" +
                        " and mark.report_type=1 " +//效果录音
                        //" and runplan.is_delete=0 " +
                        //" and runplan.valid_end_time>=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') "+

                        //" and record.is_delete=0 " +
                        " and runplan.parent_id = dis.runplan_id(+) " +
                        //" and dis.is_delete=0 " +
                        " and runplan.runplan_type_id='"+my_report_type+"'"+
                        " and mark.start_datetime>=to_date('"+keyValue.getStartDate()+"','yyyy-mm-dd hh24:mi:ss')" +
                        " and mark.end_datetime<=to_date('"+keyValue.getEndDate()+"','yyyy-mm-dd hh24:mi:ss') ";
            }
//    		 if(headcodes!=null&&!headcodes.equalsIgnoreCase("")){
//    			 sql+="and '"+headcodes.substring(0, headcodes.length()-1)+"' like '%'||head.code||'%' ";
//    		 }
            if(headcodes!=null&&!headcodes.equalsIgnoreCase("")){
                if(headcodes.split(",").length>1){
                    String[] ss = headcodes.split(",");
                    String newsql="";
                    for(int m=0;m<ss.length;m++){
                        newsql+=" mark.HEAD_CODE like '%"+ss[m]+"%' or";
                    }
                    sql+=" and ("+newsql.substring(0, newsql.length()-2)+")";
                }else{
                    //sql+="and '"+headcodes.substring(0, headcodes.length()-1)+"' like '%'||head.code||'%' ";
                    sql+=" and mark.HEAD_CODE like '%"+headcodes.substring(0, headcodes.length()-1)+"%'";
                }

            }
            sql+=" order by mark.headname,runplan.freq ";
            GDSet gd = DbComponent.Query(sql);
            for(int i=0;i<gd.getRowCount();i++){
                //备注信息由同邻频干扰情况信息表代入，对于可听率低于60%未代入同邻频干扰信息的需要在备注信息中直接加上信号弱，杂音大
                String dis_runplan_id = gd.getString(i, "dis_runplan_id");//干扰信息表运行图id
                String disturb = gd.getString(i, "disturb");//干扰信息
                String play_time = gd.getString(i, "play_time");
                String language_name = gd.getString(i, "language_name");
                String freq = gd.getString(i, "freq");
                String stationname = "";
                String valid_start_time = gd.getString(i, "valid_start_time");
                String valid_end_time = gd.getString(i, "valid_end_time");
                String runplan_type_id = "";//1：国际台运行图2：海外落地

                if(my_report_type.equals("2")){
                    String redisseminators = gd.getString(i, "redisseminators");//转播机构
                    if(redisseminators.equals("")){
                        redisseminators = " ";
                    }
                    stationname = redisseminators;
                    runplan_type_id = "2";
                } else{
                    stationname = gd.getString(i, "stationname");
                    runplan_type_id = "1";
                }
                String direction = gd.getString(i, "direction");
                String power = gd.getString(i, "power");
                String counto = gd.getString(i, "counto");
                String service_area = gd.getString(i, "service_area");
                String type_id = gd.getString(i, "type_id");
                int countoNum = Integer.parseInt(counto);
                double timeLength = 0.5;
                String[] service_areaArr = service_area.split(",");
                for(int ai=0;ai<service_areaArr.length;ai++){
                    service_area = service_areaArr[ai];
                    String mapKey = play_time+language_name+freq+stationname+direction+power+service_area;
                    if(map.get(mapKey) == null){
                        ZrstRepEffectStatisticsBean detailBean = new ZrstRepEffectStatisticsBean();
                        detailBean.setPlay_time(play_time);
                        detailBean.setLanguage_name(language_name);
                        detailBean.setFreq(freq);
                        detailBean.setTransmit_station(stationname);
                        detailBean.setRunplan_type_id_temp(runplan_type_id);
                        detailBean.setValid_start_time_temp(valid_start_time);
                        detailBean.setValid_end_time_temp(valid_end_time);
                        detailBean.setTransmit_direction(direction);
                        detailBean.setTransmit_power(power);
                        detailBean.setService_area(service_area);
//    					 detailBean.setReceive_name(shortname);
//    					 detailBean.setReceive_code(headcode);

                        int fenCount0 = countoNum==0?1:0;
                        int fenCount1 = countoNum==1?1:0;
                        int fenCount2 = countoNum==2?1:0;
                        int fenCount3 = countoNum==3?1:0;
                        int fenCount4 = countoNum==4?1:0;
                        int fenCount5 = countoNum==5?1:0;
                        double fen0 = countoNum==0?timeLength:0;
                        double fen1 = countoNum==1?timeLength:0;
                        double fen2 = countoNum==2?timeLength:0;
                        double fen3 = countoNum==3?timeLength:0;
                        double fen4 = countoNum==4?timeLength:0;
                        double fen5 = countoNum==5?timeLength:0;
                        detailBean.setReceive_count(timeLength+"_1");
                        //暂时存放 播音时长_打分的次数
                        detailBean.setFen0(fen0+"_"+fenCount0);
                        detailBean.setFen1(fen1+"_"+fenCount1);
                        detailBean.setFen2(fen2+"_"+fenCount2);
                        detailBean.setFen3(fen3+"_"+fenCount3);
                        detailBean.setFen4(fen4+"_"+fenCount4);
                        detailBean.setFen5(fen5+"_"+fenCount5);
                        detailBean.setAverage_listens(new BigDecimal(Double.parseDouble((fenCount3+fenCount4+fenCount5)+"")*100/1).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
                        if(dis_runplan_id.equals("")){
                            if(Float.valueOf(new BigDecimal(Double.parseDouble((fenCount3+fenCount4+fenCount5)+"")*100/1).setScale(1,BigDecimal.ROUND_HALF_UP)+"")<60){
                                disturb = "信号弱，杂音大";
                            }
                        }
                        detailBean.setBak(disturb);
                        detailBean.setSub_report_type("71");
                        detailBean.setReport_type(my_report_type);
                        detailBean.setReport_id(report_id);
                        map.put(mapKey, detailBean);
                    } else{
                        ZrstRepEffectStatisticsBean detailBean = (ZrstRepEffectStatisticsBean)map.get(mapKey);
                        double receive_length = Double.parseDouble(detailBean.getReceive_count().split("_")[0]);
                        int receive_count = Integer.parseInt(detailBean.getReceive_count().split("_")[1]);
                        receive_length += timeLength;
                        receive_count++;
                        detailBean.setReceive_count(receive_length+"_"+receive_count);
                        double fen0 = Double.parseDouble(detailBean.getFen0().split("_")[0])+(countoNum==0?timeLength:0);
                        double fen1 = Double.parseDouble(detailBean.getFen1().split("_")[0])+(countoNum==1?timeLength:0);
                        double fen2 = Double.parseDouble(detailBean.getFen2().split("_")[0])+(countoNum==2?timeLength:0);
                        double fen3 = Double.parseDouble(detailBean.getFen3().split("_")[0])+(countoNum==3?timeLength:0);
                        double fen4 = Double.parseDouble(detailBean.getFen4().split("_")[0])+(countoNum==4?timeLength:0);
                        double fen5 = Double.parseDouble(detailBean.getFen5().split("_")[0])+(countoNum==5?timeLength:0);

                        int fenCount0 = Integer.parseInt(detailBean.getFen0().split("_")[1])+(countoNum==0?1:0);
                        int fenCount1 = Integer.parseInt(detailBean.getFen1().split("_")[1])+(countoNum==1?1:0);
                        int fenCount2 = Integer.parseInt(detailBean.getFen2().split("_")[1])+(countoNum==2?1:0);
                        int fenCount3 = Integer.parseInt(detailBean.getFen3().split("_")[1])+(countoNum==3?1:0);
                        int fenCount4 = Integer.parseInt(detailBean.getFen4().split("_")[1])+(countoNum==4?1:0);
                        int fenCount5 = Integer.parseInt(detailBean.getFen5().split("_")[1])+(countoNum==5?1:0);

                        detailBean.setFen0(fen0+"_"+fenCount0);
                        detailBean.setFen1(fen1+"_"+fenCount1);
                        detailBean.setFen2(fen2+"_"+fenCount2);
                        detailBean.setFen3(fen3+"_"+fenCount3);
                        detailBean.setFen4(fen4+"_"+fenCount4);
                        detailBean.setFen5(fen5+"_"+fenCount5);
                        detailBean.setAverage_listens(new BigDecimal(Double.parseDouble((fenCount3+fenCount4+fenCount5)+"")*100/receive_count).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
                        if(dis_runplan_id.equals("")){
                            if(Float.valueOf(new BigDecimal(Double.parseDouble((fenCount3+fenCount4+fenCount5)+"")*100/receive_count).setScale(1,BigDecimal.ROUND_HALF_UP)+"")<60){
                                disturb = "信号弱，杂音大";
                            }
                        }
                        detailBean.setBak(disturb);
                        map.put(mapKey, detailBean);
                    }
                }
            }
        }

        
        for(String o:map.keySet()){
            ZrstRepEffectStatisticsBean bean = map.get(o);
            //可听度的满意程度中若“5”栏中填8，则表示有80—89%的时间可听度达到5分；“4”栏中填1，则表示有10—19%的时间可听度达到4分。
            //0分的播音时间除以总时间
            bean.setFen0(Math.floor(Double.parseDouble(bean.getFen0().split("_")[0])*10/Double.parseDouble(bean.getReceive_count().split("_")[0]))+"");
            bean.setFen1(Math.floor(Double.parseDouble(bean.getFen1().split("_")[0])*10/Double.parseDouble(bean.getReceive_count().split("_")[0]))+"");
            bean.setFen2(Math.floor(Double.parseDouble(bean.getFen2().split("_")[0])*10/Double.parseDouble(bean.getReceive_count().split("_")[0]))+"");
            bean.setFen3(Math.floor(Double.parseDouble(bean.getFen3().split("_")[0])*10/Double.parseDouble(bean.getReceive_count().split("_")[0]))+"");
            bean.setFen4(Math.floor(Double.parseDouble(bean.getFen4().split("_")[0])*10/Double.parseDouble(bean.getReceive_count().split("_")[0]))+"");
            bean.setFen5(Math.floor(Double.parseDouble(bean.getFen5().split("_")[0])*10/Double.parseDouble(bean.getReceive_count().split("_")[0]))+"");
            bean.setReceive_count(bean.getReceive_count().split("_")[1]);
            map.put((String)o, bean);


//			//备注信息由同邻频干扰情况信息表代入，对于可听率低于60%未代入同邻频干扰信息的需要在备注信息中直接加上信号弱，杂音大
//			//根据频率、播音时间、有效开始时间、有效结束时间、发射台(转播机构)
//			String sqlsome = "select disturb from zres_runplan_disturb_tab where " +
//							" freq='"+bean.getFreq()+"'" +
//							" and language='"+bean.getLanguage_name()+"' " +
//							" and start_time || '-' || end_time='"+bean.getPlay_time()+"'" +
//							" and valid_start_time='"+bean.getValid_start_time_temp()+"' " +
//							" and valid_end_time='"+bean.getValid_end_time_temp()+"' " +
//							" and is_delete=0";
//			String subSql = "";
//			if(bean.getRunplan_type_id_temp().equals("1")){
//				subSql = " and station_name = '"+bean.getTransmit_station()+"'";
//			} else if(bean.getRunplan_type_id_temp().equals("2")){
//				subSql = " and redisseminators = '"+bean.getTransmit_station()+"'";
//			}
//			sqlsome += subSql;
//			String disturb = "";
//			 GDSet gds = DbComponent.Query(sqlsome);
//			 if(gds.getRowCount()>0){
//				 disturb = gds.getString(0, "disturb");
//				 bean.setBak(disturb);
//			 } else{
//				 if(Integer.parseInt(bean.getAverage_listens())<60){
//					 bean.setBak("信号弱，杂音大");
//				 }
//			 }
//			 map.put((String)o, bean);
        }
        return map;
    }

    /**
     * 各月可听率对比  ------------由于sql语句执行太慢多加了个发射台的条件
     * @detail
     * @method
     * @param starttime
     * @param endtime
     * @param my_report_type
     * @param report_type
     * @param asobj
     * @return
     * @throws Exception
     * @return  HashMap
     * @author  zhaoyahui
     * @version 2013-1-10 下午07:32:41
     */
    public LinkedHashMap statisticsReportCondition61(String start, String end,String my_report_type,String report_type,ASObject asobj,String report_id) throws Exception{
        String headcodes = (String)asobj.get("headcodes");
        LinkedHashMap<String,String> mapHead = new LinkedHashMap<String,String>();
        LinkedHashMap<String,String> mapState = new LinkedHashMap<String,String>();
        LinkedHashMap<String,String> mapArea = new LinkedHashMap<String,String>();
        LinkedHashMap<String,String> mapTotal = new LinkedHashMap<String,String>();
        LinkedHashMap<String,ZrstRepEffectStatisticsBean> map = new LinkedHashMap<String,ZrstRepEffectStatisticsBean>();
        List<KeyValueForDate> list = getKeyValueForDate(start,end);
        for(int key=0;key<list.size();key++){
        	KeyValueForDate keyValue =list.get(key);
        	  String valid_end_timeString="";
              if(my_report_type.equals("1")){//国际台
                  valid_end_timeString=" and runplan.valid_end_time>=to_date('"+keyValue.getEndDate()+"','yyyy-mm-dd hh24:mi:ss') ";
              }
              String sql = "select  " +
                      "  head.code  headcode,head.service_name service_name_headend,head.type_id, mark.headname as shortname,mark.counto,runplan.service_area,state.state_name,mark.start_datetime " +
                      " from radio_mark_view_xiaohei mark," +
                      //	" radio_stream_result_tab record," +
                      " zres_runplan_chaifen_tab runplan," +
                      " res_headend_tab head," +
                      " dic_state_tab            state" +
                      " where mark.runplan_id=runplan.runplan_id and mark.counto is not null and mark.counti is not null and mark.counts is not null " +
                      " and head.code=mark.head_code and head.is_delete =0   " +
                      " and mark.report_type=1 " +//效果录音
                      //" and runplan.is_delete=0 " +
                      valid_end_timeString+

                      //	" and record.is_delete=0 " +
                      " and runplan.runplan_type_id='"+my_report_type+"'"+
                      " and state.state = head.state" +
                      " and mark.start_datetime>=to_date('"+keyValue.getStartDate()+"','yyyy-mm-dd hh24:mi:ss')" +
                      " and mark.end_datetime<=to_date('"+keyValue.getEndDate()+"','yyyy-mm-dd hh24:mi:ss') order by mark.headname ";
              GDSet gd = DbComponent.Query(sql);
              for(int i=0;i<gd.getRowCount();i++){
                  String service_area = gd.getString(i, "service_name_headend");
                  String state_name = gd.getString(i, "state_name");
                  String start_datetime = gd.getString(i, "start_datetime");
                  String monthStr = Integer.parseInt(start_datetime.substring(5,7))+"月";
                  String type_id = gd.getString(i, "type_id");
                  String headcode = gd.getString(i, "headcode");
                  String shortname = gd.getString(i, "shortname");
                  if(service_area == null || service_area.equals("")){
                      throw new Exception(shortname+"的服务区为空，请先设置");
                  }
                  if("102".equals(type_id) && ( headcode.endsWith("A") || headcode.endsWith("B")||headcode.endsWith("C") || headcode.endsWith("D")|| headcode.endsWith("E") || headcode.endsWith("F") || headcode.endsWith("G")  )){
                      headcode = headcode.substring(0, headcode.length()-1);
                  }
                  if("102".equals(type_id) && ( shortname.endsWith("A") || shortname.endsWith("B")||shortname.endsWith("C") || shortname.endsWith("D") || shortname.endsWith("E")|| shortname.endsWith("F")|| shortname.endsWith("G"))){
                      shortname = shortname.substring(0, shortname.length()-1);
                  }
                  if(headcodes == null || headcodes.equals("") || headcodes.indexOf(headcode+",")>=0){
                  } else{
                      continue;
                  }
                  String counto = gd.getString(i, "counto");
                  int countoNum = Integer.parseInt(counto);
                  String transmit_station_listens = "";//语言_>=3分_总次数_可听率%  例如：德_88_123_78,2032_23_423_28
                  if(mapHead.get(shortname) == null){
                      int big3Count = (countoNum>=3?1:0);
                      String needStr = monthStr+"_"+big3Count+"_1_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+",";
                      mapHead.put(shortname, needStr);
                  } else{
                      String newHeadValue = "";
                      if((mapHead.get(shortname)).indexOf(monthStr+"_")<0){
                          newHeadValue = mapHead.get(shortname)+monthStr+"_0_0_0,";
                          mapHead.put(shortname, newHeadValue);
                          newHeadValue = "";
                      }
                      String[] headStationArr = (mapHead.get(shortname)).split(",");
                      for(String headValue:headStationArr){
                          String[] headValueArr = headValue.split("_");
                          if(headValueArr[0].equals(monthStr)){
                              int big3Count = (countoNum>=3?1:0)+Integer.parseInt(headValueArr[1]);
                              int totalCount = Integer.parseInt(headValueArr[2])+1;
                              newHeadValue += monthStr+"_"+big3Count+"_"+totalCount+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+",";
                          } else{
                              int big3Count = (countoNum>=3?1:0);
                              if((mapHead.get(shortname)).indexOf(monthStr+"_")>-1){//如果有这个，此次把数据从新放回去
                                  newHeadValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
                              } else{//如果没有，加入新的
//      										 newHeadValue += monthStr+"_"+big3Count+"_1_"+(big3Count*100/1)+",";
                              }
                          }
                      }
                      mapHead.put(shortname, newHeadValue);
                  }
                  String[] service_areaArr = service_area.split(",");
                  for(int ai=0;ai<service_areaArr.length;ai++){
                      service_area = service_areaArr[ai];
                      if(mapArea.get(service_area) == null){//地区
                          int big3Count = (countoNum>=3?1:0);
                          mapArea.put(service_area, monthStr+"_"+big3Count+"_1_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+",");
                      } else{
                          String newHeadValue = "";
                          if((mapArea.get(service_area)).indexOf(monthStr+"_")<0){
                              newHeadValue = mapArea.get(service_area)+monthStr+"_0_0_0,";
                              mapArea.put(service_area, newHeadValue);
                              newHeadValue = "";
                          }
                          String[] headStationArr = (mapArea.get(service_area)).split(",");
                          for(String headValue:headStationArr){
                              String[] headValueArr = headValue.split("_");
                              if(headValueArr[0].equals(monthStr)){
                                  int big3Count = (countoNum>=3?1:0)+Integer.parseInt(headValueArr[1]);
                                  int totalCount = Integer.parseInt(headValueArr[2])+1;
                                  newHeadValue += monthStr+"_"+big3Count+"_"+totalCount+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+",";
                              } else{
                                  int big3Count = (countoNum>=3?1:0);
                                  if((mapArea.get(service_area)).indexOf(monthStr+"_")>-1){
                                      newHeadValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
                                  } else{
//      											 newHeadValue += monthStr+"_"+big3Count+"_1_"+(big3Count*100/1)+",";
                                  }
                              }
                          }
                          mapArea.put(service_area, newHeadValue);
                      }
                  }
                  if(mapState.get(state_name) == null){//大洲
                      int big3Count = (countoNum>=3?1:0);
                      mapState.put(state_name, monthStr+"_"+big3Count+"_1_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+",");
                  } else{
                      String newHeadValue = "";
                      if((mapState.get(state_name)).indexOf(monthStr+"_")<0){
                          newHeadValue = mapState.get(state_name)+monthStr+"_0_0_0,";
                          mapState.put(state_name, newHeadValue);
                          newHeadValue = "";
                      }
                      String[] headStationArr = (mapState.get(state_name)).split(",");
                      for(String headValue:headStationArr){
                          String[] headValueArr = headValue.split("_");
                          if(headValueArr[0].equals(monthStr)){
                              int big3Count = (countoNum>=3?1:0)+Integer.parseInt(headValueArr[1]);
                              int totalCount = Integer.parseInt(headValueArr[2])+1;
                              newHeadValue += monthStr+"_"+big3Count+"_"+totalCount+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+",";
                          } else{
                              int big3Count = (countoNum>=3?1:0);
                              if((mapState.get(state_name)).indexOf(monthStr+"_")>-1){
                                  newHeadValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
                              } else{
//      										 newHeadValue += monthStr+"_"+big3Count+"_1_"+(big3Count*100/1)+",";
                              }
                          }
                      }
                      mapState.put(state_name, newHeadValue);
                  }

                  if(mapTotal.get("全球") == null){//全部
                      int big3Count = (countoNum>=3?1:0);
                      mapTotal.put("全球", monthStr+"_"+big3Count+"_1_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+",");
                  } else{
                      String newHeadValue = "";
                      if((mapTotal.get("全球")).indexOf(monthStr+"_")<0){
                          newHeadValue = mapTotal.get("全球")+monthStr+"_0_0_0,";
                          mapTotal.put("全球", newHeadValue);
                          newHeadValue = "";
                      }
                      String[] headStationArr = (mapTotal.get("全球")).split(",");
                      for(String headValue:headStationArr){
                          String[] headValueArr = headValue.split("_");
                          if(headValueArr[0].equals(monthStr)){
                              int big3Count = (countoNum>=3?1:0)+Integer.parseInt(headValueArr[1]);
                              int totalCount = Integer.parseInt(headValueArr[2])+1;
                              newHeadValue += monthStr+"_"+big3Count+"_"+totalCount+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+",";
                          } else{
                              int big3Count = (countoNum>=3?1:0);
                              if((mapTotal.get("全球")).indexOf(monthStr+"_")>-1){
                                  newHeadValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
                              } else{
//      										 newHeadValue += monthStr+"_"+big3Count+"_1_"+(big3Count*100/1)+",";
                              }
                          }
                      }
                      mapTotal.put("全球", newHeadValue);
                  }
              }
        }


        for(Object o:mapHead.keySet()){
            String val = mapHead.get(o);
            ZrstRepEffectStatisticsBean  bean = new ZrstRepEffectStatisticsBean();
            bean.setReceive_name(o.toString());
            String[] monthArr = val.substring(0, val.length()-1).split(",");
            HashMap<String,String> tempMap = new HashMap<String,String>();
            for(String mons:monthArr){
//				if(mons.split("_")[3].equals("0"))
//					continue;
                tempMap.put(mons.split("_")[0], mons.split("_")[3]);
            }
            String month_listens = (tempMap.get("1月")==null?"$":tempMap.get("1月"))+"_"+(tempMap.get("2月")==null?"$":tempMap.get("2月"))+"_"+(tempMap.get("3月")==null?"$":tempMap.get("3月"))+"_"
                    +(tempMap.get("4月")==null?"$":tempMap.get("4月"))+"_"+(tempMap.get("5月")==null?"$":tempMap.get("5月"))+"_"+(tempMap.get("6月")==null?"$":tempMap.get("6月"))+"_"
                    +(tempMap.get("7月")==null?"$":tempMap.get("7月"))+"_"+(tempMap.get("8月")==null?"$":tempMap.get("8月"))+"_"+(tempMap.get("9月")==null?"$":tempMap.get("9月"))+"_"
                    +(tempMap.get("10月")==null?"$":tempMap.get("10月"))+"_"+(tempMap.get("11月")==null?"$":tempMap.get("11月"))+"_"+(tempMap.get("12月")==null?"$":tempMap.get("12月"));
            bean.setMonth_listens(month_listens);
            bean.setSub_report_type("61");
            bean.setReport_type(my_report_type);
            bean.setReport_id(report_id);
            map.put((String)o, bean);

        }

        for(Object o:mapArea.keySet()){
            String val = mapArea.get(o);
            ZrstRepEffectStatisticsBean  bean = new ZrstRepEffectStatisticsBean();
            bean.setService_area(o.toString());
            String[] monthArr = val.substring(0, val.length()-1).split(",");
            HashMap<String,String> tempMap = new HashMap<String,String>();
            for(String mons:monthArr){
//				if(mons.split("_")[3].equals("0"))
//					continue;
                tempMap.put(mons.split("_")[0], mons.split("_")[3]);
            }
            String month_listens = (tempMap.get("1月")==null?"$":tempMap.get("1月"))+"_"+(tempMap.get("2月")==null?"$":tempMap.get("2月"))+"_"+(tempMap.get("3月")==null?"$":tempMap.get("3月"))+"_"
                    +(tempMap.get("4月")==null?"$":tempMap.get("4月"))+"_"+(tempMap.get("5月")==null?"$":tempMap.get("5月"))+"_"+(tempMap.get("6月")==null?"$":tempMap.get("6月"))+"_"
                    +(tempMap.get("7月")==null?"$":tempMap.get("7月"))+"_"+(tempMap.get("8月")==null?"$":tempMap.get("8月"))+"_"+(tempMap.get("9月")==null?"$":tempMap.get("9月"))+"_"
                    +(tempMap.get("10月")==null?"$":tempMap.get("10月"))+"_"+(tempMap.get("11月")==null?"$":tempMap.get("11月"))+"_"+(tempMap.get("12月")==null?"$":tempMap.get("12月"));
            bean.setMonth_listens(month_listens);
            bean.setSub_report_type("61");
            bean.setReport_type(my_report_type);
            bean.setReport_id(report_id);
            map.put((String)o, bean);
        }
        for(Object o:mapState.keySet()){
            String val = mapState.get(o);
            ZrstRepEffectStatisticsBean  bean = new ZrstRepEffectStatisticsBean();
            bean.setState_name(o.toString());
            String[] monthArr = val.substring(0, val.length()-1).split(",");
            HashMap<String,String> tempMap = new HashMap<String,String>();
            for(String mons:monthArr){
//				if(mons.split("_")[3].equals("0"))
//					continue;
                tempMap.put(mons.split("_")[0], mons.split("_")[3]);
            }
            String month_listens = (tempMap.get("1月")==null?"$":tempMap.get("1月"))+"_"+(tempMap.get("2月")==null?"$":tempMap.get("2月"))+"_"+(tempMap.get("3月")==null?"$":tempMap.get("3月"))+"_"
                    +(tempMap.get("4月")==null?"$":tempMap.get("4月"))+"_"+(tempMap.get("5月")==null?"$":tempMap.get("5月"))+"_"+(tempMap.get("6月")==null?"$":tempMap.get("6月"))+"_"
                    +(tempMap.get("7月")==null?"$":tempMap.get("7月"))+"_"+(tempMap.get("8月")==null?"$":tempMap.get("8月"))+"_"+(tempMap.get("9月")==null?"$":tempMap.get("9月"))+"_"
                    +(tempMap.get("10月")==null?"$":tempMap.get("10月"))+"_"+(tempMap.get("11月")==null?"$":tempMap.get("11月"))+"_"+(tempMap.get("12月")==null?"$":tempMap.get("12月"));
            bean.setMonth_listens(month_listens);
            bean.setSub_report_type("61");
            bean.setReport_type(my_report_type);
            bean.setReport_id(report_id);
            map.put((String)o, bean);
        }
        for(Object o:mapTotal.keySet()){
            String val = mapTotal.get(o);
            ZrstRepEffectStatisticsBean  bean = new ZrstRepEffectStatisticsBean();
            String[] monthArr = val.substring(0, val.length()-1).split(",");
            HashMap<String,String> tempMap = new HashMap<String,String>();
            for(String mons:monthArr){
//				if(mons.split("_")[3].equals("0"))
//					continue;
                tempMap.put(mons.split("_")[0], mons.split("_")[3]);
            }
            String month_listens = (tempMap.get("1月")==null?"$":tempMap.get("1月"))+"_"+(tempMap.get("2月")==null?"$":tempMap.get("2月"))+"_"+(tempMap.get("3月")==null?"$":tempMap.get("3月"))+"_"
                    +(tempMap.get("4月")==null?"$":tempMap.get("4月"))+"_"+(tempMap.get("5月")==null?"$":tempMap.get("5月"))+"_"+(tempMap.get("6月")==null?"$":tempMap.get("6月"))+"_"
                    +(tempMap.get("7月")==null?"$":tempMap.get("7月"))+"_"+(tempMap.get("8月")==null?"$":tempMap.get("8月"))+"_"+(tempMap.get("9月")==null?"$":tempMap.get("9月"))+"_"
                    +(tempMap.get("10月")==null?"$":tempMap.get("10月"))+"_"+(tempMap.get("11月")==null?"$":tempMap.get("11月"))+"_"+(tempMap.get("12月")==null?"$":tempMap.get("12月"));
            bean.setMonth_listens(month_listens);
            bean.setSub_report_type("61");
            bean.setReport_type(my_report_type);
            bean.setReport_id(report_id);
            map.put((String)o, bean);
        }
        return map;
    }
    /**
     * 各遥控站、各地区、各大洲、可保证收听频时统计   ------------由于sql语句执行太慢多加了个发射台的条件
     * @detail
     * @method
     * @param starttime
     * @param endtime
     * @param my_report_type
     * @param report_type
     * @param asobj
     * @return
     * @throws Exception
     * @return  HashMap
     * @author  zhaoyahui
     * @version 2013-1-10 下午07:32:41
     */
    public LinkedHashMap statisticsReportCondition51(String start, String end,String my_report_type,String report_type,ASObject asobj,String report_id) throws Exception{
        LinkedHashMap<String,ZrstRepEffectStatisticsBean> map = new LinkedHashMap<String,ZrstRepEffectStatisticsBean>();
        String headcodes = (String)asobj.get("headcodes");
        LinkedHashMap<String,double[]> mapStationFinal = new LinkedHashMap<String,double[]>();
        LinkedHashMap<String,double[]> mapAreaFinal = new LinkedHashMap<String,double[]>();
        LinkedHashMap<String,double[]> mapStateFinal = new LinkedHashMap<String,double[]>();
        LinkedHashMap<String,double[]> mapTotalFinal = new LinkedHashMap<String,double[]>();

        LinkedHashMap<String,HashMap> mapHead = new LinkedHashMap<String,HashMap>();
        LinkedHashMap<String,HashMap> mapArea = new LinkedHashMap<String,HashMap>();
        LinkedHashMap<String,HashMap> mapState = new LinkedHashMap<String,HashMap>();
        LinkedHashMap<String,HashMap> mapTotal = new LinkedHashMap<String,HashMap>();
        List<KeyValueForDate> list = getKeyValueForDate(start,end);
        for(int key=0;key<list.size();key++){
        	KeyValueForDate keyValue =list.get(key);
        	String valid_end_timeString="";
            if(my_report_type.equals("1")){//国际台
                valid_end_timeString=" and runplan.valid_end_time>=to_date('"+keyValue.getEndDate()+"','yyyy-mm-dd hh24:mi:ss') ";
            }
            String sql = "select   runplan.start_time ||'-'|| runplan.end_time as play_time," +
                    "  head.code  headcode,head.service_name service_name_headend,head.type_id, mark.headname as shortname,mark.counto,runplan.service_area,state.state_name, mark.file_length,runplan.freq freq " +
                    " from radio_mark_view_xiaohei mark," +
                    //	" radio_stream_result_tab record," +
                    " zres_runplan_chaifen_tab runplan," +
                    " res_headend_tab head," +
                    " dic_state_tab            state" +
                    " where mark.runplan_id=runplan.runplan_id and mark.counto is not null and mark.counti is not null and mark.counts is not null " +
                    " and head.code=mark.head_code and head.is_delete =0  " +
                    " and mark.report_type=1 " +//效果录音
                    //" and runplan.is_delete=0 " +
                    valid_end_timeString +

                    //" and record.is_delete=0 " +
                    " and runplan.runplan_type_id='"+my_report_type+"'"+
                    " and state.state = head.state " +
                    " and mark.start_datetime>=to_date('"+keyValue.getStartDate()+"','yyyy-mm-dd hh24:mi:ss')" +
                    " and mark.end_datetime<=to_date('"+keyValue.getEndDate()+"','yyyy-mm-dd hh24:mi:ss')  ";
        
            if(headcodes!=null&&!headcodes.equalsIgnoreCase("")){
                //	sql+=" and '"+headcodes.substring(0, headcodes.length()-1)+"' like '%'||mark.HEAD_CODE||'%' ";
                if(headcodes.split(",").length>1){
                    String[] ss = headcodes.split(",");
                    String newsql="";
                    for(int m=0;m<ss.length;m++){
                        newsql+=" mark.HEAD_CODE like '%"+ss[m]+"%' or";
                    }
                    sql+=" and ("+newsql.substring(0, newsql.length()-2)+")";
                }else{
                    //sql+="and '"+headcodes.substring(0, headcodes.length()-1)+"' like '%'||head.code||'%' ";
                    sql+=" and mark.HEAD_CODE like '%"+headcodes.substring(0, headcodes.length()-1)+"%'";
                }

            }
            sql+=" order by mark.headname ";
            GDSet gd = DbComponent.Query(sql);


            /**
             * 　　1）可听率：可听度大于等于3分次数与总收听次数之比，用百分数表示；
             　　　			2）效果与可听率的关系：可听率>=80%，效果是可保证收听， 80%>可听率>=60%，效果是基本可收听，60%>可听率>=30%，效果是有时可收听，可听率<30%，效果是不能收听；
             */
            for(int i=0;i<gd.getRowCount();i++){
                String play_time = gd.getString(i, "play_time");
//    			 double timeLength = StringTool.getPlayTimeLength("2000-01-01 "+play_time.split("-")[1]+":00", "2000-01-01 "+play_time.split("-")[0]+":00");
                double timeLength = 0.5;
                String service_area = gd.getString(i, "service_name_headend");
                String state_name = gd.getString(i, "state_name");
                String freq = gd.getString(i, "freq");
                String[] file_lengths = gd.getString(i, "file_length").split(":");//00:00:00
                String type_id = gd.getString(i, "type_id");
                String headcode = gd.getString(i, "headcode");
                String shortname = gd.getString(i, "shortname");
                if(service_area == null || service_area.equals("")){
                    throw new Exception(shortname+"的服务区为空，请先设置");
                }
                if(service_area == null || service_area.equals("")){
                    throw new Exception(shortname+"的服务区为空，请先设置");
                }
                if("102".equals(type_id) && ( headcode.endsWith("A") || headcode.endsWith("B")||headcode.endsWith("C") || headcode.endsWith("D")|| headcode.endsWith("E") || headcode.endsWith("F") || headcode.endsWith("G")  )){
                    headcode = headcode.substring(0, headcode.length()-1);
                    // shortname = shortname.substring(0, shortname.length()-1);
                }
                if("102".equals(type_id) && ( shortname.endsWith("A") || shortname.endsWith("B")||shortname.endsWith("C") || shortname.endsWith("D") || shortname.endsWith("E")|| shortname.endsWith("F")|| shortname.endsWith("G"))){
                    shortname = shortname.substring(0, shortname.length()-1);
                }
                if(headcodes == null || headcodes.equals("") || headcodes.indexOf(headcode+",")>=0){
                } else{
                    continue;
                }
                String counto = gd.getString(i, "counto");
                int countoNum = Integer.parseInt(counto);

                if(mapHead.get(freq+"_"+shortname) == null){
                    int big3Count = (countoNum>=3?1:0);
                    float listionRate = Float.valueOf(new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
                    HashMap valMap = new HashMap();
                    valMap.put("big3Count", big3Count);//大于3分次数
                    valMap.put("totalCount", 1);//总共次数
                    valMap.put("listionRate", listionRate);//可听率
                    valMap.put("timeLength", timeLength);//时长
                    valMap.put("type", "");//类型 国内直属 国内地方 海外直属 海外转播
                    mapHead.put(freq+"_"+shortname, valMap);
                } else{
                    HashMap valMap = mapHead.get(freq+"_"+shortname);
                    int big3Count = (countoNum>=3?1:0) + (Integer)valMap.get("big3Count");
                    int totalCount = 1 + (Integer)valMap.get("totalCount");
                    float listionRate = Float.valueOf(new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
                    double timeLengthTemp = (Double)valMap.get("timeLength") + timeLength;
                    valMap.put("big3Count", big3Count);//大于3分次数
                    valMap.put("totalCount", totalCount);//总共次数
                    valMap.put("listionRate", listionRate);//可听率
                    valMap.put("timeLength", timeLengthTemp);//时长
                    mapHead.put(freq+"_"+shortname, valMap);
                }
                String[] service_areaArr = service_area.split(",");
                for(int ai=0;ai<service_areaArr.length;ai++){
                    service_area = service_areaArr[ai];
                    if(mapArea.get(freq+"_"+service_area) == null){
                        int big3Count = (countoNum>=3?1:0);
                        float listionRate = Float.valueOf(new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
                        HashMap valMap = new HashMap();
                        valMap.put("big3Count", big3Count);//大于3分次数
                        valMap.put("totalCount", 1);//总共次数
                        valMap.put("listionRate", listionRate);//可听率
                        valMap.put("timeLength", timeLength);//时长
                        valMap.put("type", "");//类型 国内直属 国内地方 海外直属 海外转播
                        mapArea.put(freq+"_"+service_area, valMap);
                    } else{
                        HashMap valMap = mapArea.get(freq+"_"+service_area);
                        int big3Count = (countoNum>=3?1:0) + (Integer)valMap.get("big3Count");
                        int totalCount = 1 + (Integer)valMap.get("totalCount");
                        float listionRate = Float.valueOf(new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
                        double timeLengthTemp = (Double)valMap.get("timeLength") + timeLength;
                        valMap.put("big3Count", big3Count);//大于3分次数
                        valMap.put("totalCount", totalCount);//总共次数
                        valMap.put("listionRate", listionRate);//可听率
                        valMap.put("timeLength", timeLengthTemp);//时长
                        mapArea.put(freq+"_"+service_area, valMap);
                    }
                }
                if(mapState.get(freq+"_"+state_name) == null){
                    int big3Count = (countoNum>=3?1:0);
                    float listionRate = Float.valueOf(new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
                    HashMap valMap = new HashMap();
                    valMap.put("big3Count", big3Count);//大于3分次数
                    valMap.put("totalCount", 1);//总共次数
                    valMap.put("listionRate", listionRate);//可听率
                    valMap.put("timeLength", timeLength);//时长
                    valMap.put("type", "");//类型 国内直属 国内地方 海外直属 海外转播
                    mapState.put(freq+"_"+state_name, valMap);
                } else{
                    HashMap valMap = mapState.get(freq+"_"+state_name);
                    int big3Count = (countoNum>=3?1:0) + (Integer)valMap.get("big3Count");
                    int totalCount = 1 + (Integer)valMap.get("totalCount");
                    float listionRate = Float.valueOf(new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
                    double timeLengthTemp = (Double)valMap.get("timeLength") + timeLength;
                    valMap.put("big3Count", big3Count);//大于3分次数
                    valMap.put("totalCount", totalCount);//总共次数
                    valMap.put("listionRate", listionRate);//可听率
                    valMap.put("timeLength", timeLengthTemp);//时长
                    mapState.put(freq+"_"+state_name, valMap);
                }

                if(mapTotal.get(freq+"_"+"全球") == null){
                    int big3Count = (countoNum>=3?1:0);
                    float listionRate = Float.valueOf(new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
                    HashMap valMap = new HashMap();
                    valMap.put("big3Count", big3Count);//大于3分次数
                    valMap.put("totalCount", 1);//总共次数
                    valMap.put("listionRate", listionRate);//可听率
                    valMap.put("timeLength", timeLength);//时长
                    valMap.put("type", "");//类型 国内直属 国内地方 海外直属 海外转播
                    mapTotal.put(freq+"_"+"全球", valMap);
                } else{
                    HashMap valMap = mapTotal.get(freq+"_"+"全球");
                    int big3Count = (countoNum>=3?1:0) + (Integer)valMap.get("big3Count");
                    int totalCount = 1 + (Integer)valMap.get("totalCount");
                    float listionRate = Float.valueOf(new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
                    double timeLengthTemp = (Double)valMap.get("timeLength") + timeLength;
                    valMap.put("big3Count", big3Count);//大于3分次数
                    valMap.put("totalCount", totalCount);//总共次数
                    valMap.put("listionRate", listionRate);//可听率
                    valMap.put("timeLength", timeLengthTemp);//时长
                    mapTotal.put(freq+"_"+"全球", valMap);
                }

            }
        }

        //取同一个频率不同站点的可听率，如果符合，相加时长
        for(String o:mapHead.keySet()){
            String freq_shortname = o; 		// Map的键
//			System.out.println("freq_shortname1=="+freq_shortname);
            String shortnameval = freq_shortname.split("_")[1];
            HashMap valMap = mapHead.get(o);   		// Map的值
            float listionRate = (Float) valMap.get("listionRate");
            double timeLengthVal = (Double)valMap.get("timeLength");
            double[] vals = {0,0,0,0,0};
            if(mapStationFinal.get(shortnameval) != null){
                vals = mapStationFinal.get(shortnameval);
            }

            if(listionRate>=80){//效果是可保证收听
                vals[0] += timeLengthVal;
            } else if(listionRate>=60 && listionRate<80){//效果是基本可收听
                vals[1] += timeLengthVal;
            } else if(listionRate>=30 && listionRate<60){//效果是有时可收听
                vals[2] += timeLengthVal;
            } else if(listionRate<30){//效果是不能收听
                vals[3] += timeLengthVal;
            }
            mapStationFinal.put(shortnameval, vals);
        }

        for(String o:mapArea.keySet()){
            String freq_shortname = o; 		// Map的键
//			System.out.println("freq_shortname=="+freq_shortname);
            String shortnameval = freq_shortname.split("_")[1];
            HashMap valMap = mapArea.get(o);   		// Map的值
            float listionRate = (Float)valMap.get("listionRate");
            double timeLengthVal = (Double)valMap.get("timeLength");
            double[] vals = {0,0,0,0,0};
            if(mapAreaFinal.get(shortnameval) != null){
                vals = mapAreaFinal.get(shortnameval);
            }

            if(listionRate>=80){//效果是可保证收听
                vals[0] += timeLengthVal;
            } else if(listionRate>=60 && listionRate<80){//效果是基本可收听
                vals[1] += timeLengthVal;
            } else if(listionRate>=30 && listionRate<60){//效果是有时可收听
                vals[2] += timeLengthVal;
            } else if(listionRate<30){//效果是不能收听
                vals[3] += timeLengthVal;
            }
            mapAreaFinal.put(shortnameval, vals);
        }

        for(String o:mapState.keySet()){
            String freq_shortname = o; 		// Map的键
            String shortnameval = freq_shortname.split("_")[1];
            HashMap valMap = mapState.get(o);   		// Map的值
            float listionRate = (Float)valMap.get("listionRate");
            double timeLengthVal = (Double)valMap.get("timeLength");
            double[] vals = {0,0,0,0,0};
            if(mapStateFinal.get(shortnameval) != null){
                vals = mapStateFinal.get(shortnameval);
            }

            if(listionRate>=80){//效果是可保证收听
                vals[0] += timeLengthVal;
            } else if(listionRate>=60 && listionRate<80){//效果是基本可收听
                vals[1] += timeLengthVal;
            } else if(listionRate>=30 && listionRate<60){//效果是有时可收听
                vals[2] += timeLengthVal;
            } else if(listionRate<30){//效果是不能收听
                vals[3] += timeLengthVal;
            }
            mapStateFinal.put(shortnameval, vals);
        }

        for(String o:mapTotal.keySet()){
            String freq_shortname = o; 		// Map的键
            String shortnameval = freq_shortname.split("_")[1];
            HashMap valMap = mapTotal.get(o);   		// Map的值
            float listionRate = (Float)valMap.get("listionRate");
            double timeLengthVal = (Double)valMap.get("timeLength");
            double[] vals = {0,0,0,0,0};
            if(mapTotalFinal.get(shortnameval) != null){
                vals = mapTotalFinal.get(shortnameval);
            }

            if(listionRate>=80){//效果是可保证收听
                vals[0] += timeLengthVal;
            } else if(listionRate>=60 && listionRate<80){//效果是基本可收听
                vals[1] += timeLengthVal;
            } else if(listionRate>=30 && listionRate<60){//效果是有时可收听
                vals[2] += timeLengthVal;
            } else if(listionRate<30){//效果是不能收听
                vals[3] += timeLengthVal;
            }
            mapTotalFinal.put(shortnameval, vals);
        }

        for(String o:mapStationFinal.keySet()){
            double[] valNum = mapStationFinal.get(o);
            ZrstRepEffectStatisticsBean bean = new ZrstRepEffectStatisticsBean();
            bean.setReceive_name(o);
            bean.setReceive_listens(valNum[0]+"_"+valNum[1]+"_"+valNum[2]+"_"+valNum[3]+"_"+(valNum[0]+valNum[1]+valNum[2]+valNum[3]));
            bean.setSub_report_type("51");
            bean.setReport_type(my_report_type);
            bean.setReport_id(report_id);
            map.put((String)o, bean);
        }

        for(String o:mapAreaFinal.keySet()){
            double[] valNum = mapAreaFinal.get(o);
            ZrstRepEffectStatisticsBean bean = new ZrstRepEffectStatisticsBean();
            bean.setService_area(o);
            bean.setReceive_listens(valNum[0]+"_"+valNum[1]+"_"+valNum[2]+"_"+valNum[3]+"_"+(valNum[0]+valNum[1]+valNum[2]+valNum[3]));
            bean.setSub_report_type("51");
            bean.setReport_type(my_report_type);
            bean.setReport_id(report_id);
            map.put((String)o, bean);
        }

        for(String o:mapStateFinal.keySet()){
            double[] valNum = mapStateFinal.get(o);
            ZrstRepEffectStatisticsBean bean = new ZrstRepEffectStatisticsBean();
            bean.setState_name(o);
            bean.setReceive_listens(valNum[0]+"_"+valNum[1]+"_"+valNum[2]+"_"+valNum[3]+"_"+(valNum[0]+valNum[1]+valNum[2]+valNum[3]));
            bean.setSub_report_type("51");
            bean.setReport_type(my_report_type);
            bean.setReport_id(report_id);
            map.put((String)o, bean);
        }

        for(String o:mapTotalFinal.keySet()){
            double[] valNum = mapTotalFinal.get(o);
            ZrstRepEffectStatisticsBean bean = new ZrstRepEffectStatisticsBean();
            bean.setReceive_listens(valNum[0]+"_"+valNum[1]+"_"+valNum[2]+"_"+valNum[3]+"_"+(valNum[0]+valNum[1]+valNum[2]+valNum[3]));
            bean.setSub_report_type("51");
            bean.setReport_type(my_report_type);
            bean.setReport_id(report_id);
            map.put((String)o, bean);
        }
        return map;
    }
    /**
     * 各地区、各大洲、可听率统计 ------------由于sql语句执行太慢多加了个发射台的条件
     * @detail
     * @method
     * @param starttime
     * @param endtime
     * @param my_report_type
     * @param report_type
     * @param asobj
     * @return
     * @throws Exception
     * @return  HashMap
     * @author  zhaoyahui
     * @version 2013-1-10 下午07:32:41
     */
    public LinkedHashMap statisticsReportCondition41(String start, String end,String my_report_type,String report_type,ASObject asobj,String report_id) throws Exception{
        LinkedHashMap<String,ZrstRepEffectStatisticsBean> map = new LinkedHashMap<String,ZrstRepEffectStatisticsBean>();
        String headcodes = (String)asobj.get("headcodes");
        LinkedHashMap<String,String> mapState = new LinkedHashMap<String,String>();
        LinkedHashMap<String,String> mapArea = new LinkedHashMap<String,String>();
        LinkedHashMap<String,String> mapTotal = new LinkedHashMap<String,String>();
        List<KeyValueForDate> list = getKeyValueForDate(start,end);
        for(int key=0;key<list.size();key++){
        	KeyValueForDate keyValue =list.get(key);
        	 String valid_end_timeString="";
             if(my_report_type.equals("1")){//国际台
                 valid_end_timeString=" and runplan.valid_end_time>=to_date('"+keyValue.getEndDate()+"','yyyy-mm-dd hh24:mi:ss') ";
             }
             String sql = "select  " +
                     "  mark.counto,runplan.service_area,state.state_name,head.service_name service_name_headend " +
                     " from radio_mark_view_xiaohei mark," +
                     //	" radio_stream_result_tab record," +
                     " zres_runplan_chaifen_tab runplan," +
                     " res_headend_tab head," +
                     " dic_state_tab state" +
                     " where  mark.runplan_id=runplan.runplan_id and mark.counto is not null and mark.counti is not null and mark.counts is not null " +
                     " and head.code=mark.head_code and head.is_delete =0  " +
                     " and mark.report_type=1 " +//效果录音
                     //" and runplan.is_delete=0 " +

                     valid_end_timeString +

                     //" and record.is_delete=0 " +
                     " and runplan.runplan_type_id='"+my_report_type+"' "+
                     " and state.state = head.state" +
                     " and mark.start_datetime>=to_date('"+keyValue.getStartDate()+"','yyyy-mm-dd hh24:mi:ss')" +
                     " and mark.end_datetime<=to_date('"+keyValue.getEndDate()+"','yyyy-mm-dd hh24:mi:ss')  ";
             if(headcodes!=null&&!headcodes.equalsIgnoreCase("")){
                 //	sql+=" and '"+headcodes.substring(0, headcodes.length()-1)+"' like '%'||mark.HEAD_CODE||'%' ";
                 if(headcodes.split(",").length>1){
                     String[] ss = headcodes.split(",");
                     String newsql="";
                     for(int m=0;m<ss.length;m++){
                         newsql+=" mark.HEAD_CODE like '%"+ss[m]+"%' or";
                     }
                     sql+=" and ("+newsql.substring(0, newsql.length()-2)+")";
                 }else{
                     //sql+="and '"+headcodes.substring(0, headcodes.length()-1)+"' like '%'||head.code||'%' ";
                     sql+=" and mark.HEAD_CODE like '%"+headcodes.substring(0, headcodes.length()-1)+"%'";
                 }

             }
             sql+=" order by state.state_name";
             GDSet gd = DbComponent.Query(sql);
             for(int i=0;i<gd.getRowCount();i++){
                 String state_name = gd.getString(i, "state_name");
                 String service_area = gd.getString(i, "service_name_headend");
//     					 String stationname = gd.getString(i, "stationname");
                 String counto = gd.getString(i, "counto");
                 int countoNum = Integer.parseInt(counto);
                 //计算大洲的收听次数
                 if(mapState.get("temp") == null){
                     int big3Count = (countoNum>=3?1:0);
                     mapState.put("temp", state_name+"_"+big3Count+"_1_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+",");
                 } else{
                     String newHeadValue = "";
                     if((mapState.get("temp")).indexOf(state_name+"_")<0){
                         newHeadValue = mapState.get("temp")+state_name+"_0_0_0,";
                         mapState.put("temp", newHeadValue);
                         newHeadValue = "";
                     }
                     String[] headStationArr = (mapState.get("temp")).split(",");
                     for(String headValue:headStationArr){
                         String[] headValueArr = headValue.split("_");
                         if(headValueArr[0].equals(state_name)){
                             int big3Count = (countoNum>=3?1:0)+Integer.parseInt(headValueArr[1]);
                             int totalCount = Integer.parseInt(headValueArr[2])+1;

                             newHeadValue += state_name+"_"+big3Count+"_"+totalCount+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+",";
                         } else{
                             if((mapState.get("temp")).indexOf(state_name+"_")>-1){
                                 newHeadValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
                             } else{
                             }
                         }
                     }
                     mapState.put("temp", newHeadValue);
                 }
                 //计算地区的收听次数
                 String[] service_areaArr = service_area.split(",");
                 for(int ai=0;ai<service_areaArr.length;ai++){
                     service_area = service_areaArr[ai];
                     if(mapArea.get("temp") == null){
                         int big3Count = (countoNum>=3?1:0);
                         mapArea.put("temp", service_area+"_"+big3Count+"_1_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+",");
                     } else{
                         String newHeadValue = "";
                         if((mapArea.get("temp")).indexOf(service_area+"_")<0){
                             newHeadValue = mapArea.get("temp")+service_area+"_0_0_0,";
                             mapArea.put("temp", newHeadValue);
                             newHeadValue = "";
                         }
                         String[] headStationArr = (mapArea.get("temp")).split(",");
                         for(String headValue:headStationArr){
                             String[] headValueArr = headValue.split("_");
                             if(headValueArr[0].equals(service_area)){
                                 int big3Count = (countoNum>=3?1:0)+Integer.parseInt(headValueArr[1]);
                                 int totalCount = Integer.parseInt(headValueArr[2])+1;
                                 newHeadValue += service_area+"_"+big3Count+"_"+totalCount+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+",";
                             } else{
                                 if((mapArea.get("temp")).indexOf(service_area+"_")>-1){
                                     newHeadValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
                                 } else{
                                 }
                             }
                         }
                         mapArea.put("temp", newHeadValue);
                     }
                 }
                 //计算全部的收听次数
                 if(mapTotal.get("temp") == null){
                     int big3Count = (countoNum>=3?1:0);
                     mapTotal.put("temp", "总计_"+big3Count+"_1_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP));
                 } else{
                     String newHeadValue = "";
                     String[] totalValArr = (mapTotal.get("temp")).split("_");
                     int big3Count = (countoNum>=3?1:0)+Integer.parseInt(totalValArr[1]);
                     int totalCount = Integer.parseInt(totalValArr[2])+1;
                     newHeadValue = "总计_"+big3Count+"_"+totalCount+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+",";
                     mapTotal.put("temp", newHeadValue);
                 }
             }
        }
        
        ZrstRepEffectStatisticsBean  bean = new ZrstRepEffectStatisticsBean();
        String traVal = "";
        if(mapArea.get("temp") != null && mapState.get("temp")!=null && mapTotal.get("temp")!=null){
            traVal += mapArea.get("temp");
            traVal += mapState.get("temp");
            traVal += mapTotal.get("temp");
            bean.setAll_listens(traVal);
            bean.setSub_report_type("41");
            bean.setReport_type(my_report_type);
            bean.setReport_id(report_id);
            map.put("temp", bean);
        }
        return map;
    }
    /**
     * 语言总体播出效果统计2
     * @detail
     * @method
     * @param starttime
     * @param endtime
     * @param my_report_type
     * @param report_type
     * @param asobj
     * @return
     * @throws Exception
     * @return  HashMap
     * @author  zhaoyahui
     * @version 2013-1-10 下午07:32:41
     */
    public LinkedHashMap statisticsReportCondition32(String start, String end,String my_report_type,String report_type,ASObject asobj,String report_id) throws Exception{
        LinkedHashMap<String,ZrstRepEffectStatisticsBean> map = new LinkedHashMap<String,ZrstRepEffectStatisticsBean>();
        String headcodes = (String)asobj.get("headcodes");
        LinkedHashMap<String,String> mapStation = new LinkedHashMap<String,String>();
        LinkedHashMap<String,String> mapState = new LinkedHashMap<String,String>();
        LinkedHashMap<String,String> mapArea = new LinkedHashMap<String,String>();
        LinkedHashMap<String,String> mapTotal = new LinkedHashMap<String,String>();
        String allHead = "";
        String allArea = "";
        String allState = "";
        List<KeyValueForDate> list = getKeyValueForDate(start,end);
        for(int key=0;key<list.size();key++){
        	KeyValueForDate keyValue =list.get(key);
        	 String valid_end_timeString="";
             if(my_report_type.equals("1")){//国际台
                 valid_end_timeString=" and runplan.valid_end_time>=to_date('"+keyValue.getEndDate()+"','yyyy-mm-dd hh24:mi:ss') ";
             }
             String sql = "select  " +
                     "  head.code  headcode,head.service_name service_name_headend,head.type_id, mark.headname as shortname,mark.counto,runplan.service_area,state.state_name,lan.language_name" +
                     " from radio_mark_view_xiaohei mark," +
                     //" radio_stream_result_tab record," +
                     " zres_runplan_chaifen_tab runplan," +
                     " res_headend_tab head," +
                     " zdic_language_tab lan ," +
                     " dic_state_tab            state" +
                     " where mark.runplan_id=runplan.runplan_id and mark.counto is not null and mark.counti is not null and mark.counts is not null " +
                     " and head.code=mark.head_code and head.is_delete =0  " +
                     " and mark.report_type=1 " +//效果录音
                     //" and runplan.is_delete=0 " +
                     valid_end_timeString +

                     //	" and record.is_delete=0 " +
                     " and runplan.runplan_type_id='"+my_report_type+"'"+
                     " and state.state = head.state" +
                     " and  runplan.language_id=lan.language_id " +
                     " and mark.start_datetime>=to_date('"+keyValue.getStartDate()+"','yyyy-mm-dd hh24:mi:ss')" +
                     " and mark.end_datetime<=to_date('"+keyValue.getEndDate()+"','yyyy-mm-dd hh24:mi:ss')  ";
             if(headcodes!=null&&!headcodes.equalsIgnoreCase("")){
                 //	sql+=" and '"+headcodes.substring(0, headcodes.length()-1)+"' like '%'||head.code||'%' ";
                 if(headcodes.split(",").length>1){
                     String[] ss = headcodes.split(",");
                     String newsql="";
                     for(int m=0;m<ss.length;m++){
                         newsql+=" head.code like '%"+ss[m]+"%' or";
                     }
                     sql+=" and ("+newsql.substring(0, newsql.length()-2)+")";
                 }else{
                     //sql+="and '"+headcodes.substring(0, headcodes.length()-1)+"' like '%'||head.code||'%' ";
                     sql+=" and head.code like '%"+headcodes.substring(0, headcodes.length()-1)+"%'";
                 }
             }
             sql+=" order by head.code,lan.language_name ";
             GDSet gd = DbComponent.Query(sql);
             for(int i=0;i<gd.getRowCount();i++){
                 String type_id = gd.getString(i, "type_id");
                 String headcode = gd.getString(i, "headcode");
                 String shortname = gd.getString(i, "shortname");
                 String service_area = gd.getString(i, "service_name_headend");
                 if(service_area == null || service_area.equals("")){
                     throw new Exception(shortname+"的服务区为空，请先设置");
                 }
                 String state_name = gd.getString(i, "state_name");
                 if("102".equals(type_id) && ( headcode.endsWith("A") || headcode.endsWith("B")||headcode.endsWith("C") || headcode.endsWith("D")|| headcode.endsWith("E") || headcode.endsWith("F") || headcode.endsWith("G")  )){
                     headcode = headcode.substring(0, headcode.length()-1);
                     // shortname = shortname.substring(0, shortname.length()-1);
                 }
                 if("102".equals(type_id) && ( shortname.endsWith("A") || shortname.endsWith("B")||shortname.endsWith("C") || shortname.endsWith("D") || shortname.endsWith("E")|| shortname.endsWith("F")|| shortname.endsWith("G"))){
                     shortname = shortname.substring(0, shortname.length()-1);
                 }
                 //收测站点_可听率_大于3分次数_总次数
                 if(allHead.indexOf(shortname+"_A_A_A,")<0){
                     allHead += shortname+"_A_A_A,";
                 }
                 if(allArea.indexOf(","+service_area+"_A_A_A,")<0){
                     allArea += service_area+"_A_A_A,";
                 }
                 if(allState.indexOf(state_name+"_A_A_A,")<0){
                     allState += state_name+"_A_A_A,";
                 }
             }
             
             for(int i=0;i<gd.getRowCount();i++){
                 String service_area = gd.getString(i, "service_name_headend");
                 String state_name = gd.getString(i, "state_name");
                 String language_name = gd.getString(i, "language_name");

                 String type_id = gd.getString(i, "type_id");
                 String headcode = gd.getString(i, "headcode");
                 String shortname = gd.getString(i, "shortname");
                 if("102".equals(type_id) && ( headcode.endsWith("A") || headcode.endsWith("B")||headcode.endsWith("C") || headcode.endsWith("D")|| headcode.endsWith("E") || headcode.endsWith("F") || headcode.endsWith("G")  )){
                     headcode = headcode.substring(0, headcode.length()-1);
                     // shortname = shortname.substring(0, shortname.length()-1);
                 }
                 if("102".equals(type_id) && ( shortname.endsWith("A") || shortname.endsWith("B")||shortname.endsWith("C") || shortname.endsWith("D") || shortname.endsWith("E")|| shortname.endsWith("F")|| shortname.endsWith("G"))){
                     shortname = shortname.substring(0, shortname.length()-1);
                 }
                 if(headcodes == null || headcodes.equals("") || headcodes.indexOf(headcode+",")>=0){
                 } else{
                     continue;
                 }
                 String counto = gd.getString(i, "counto");
                 int countoNum = Integer.parseInt(counto);
                 if(mapStation.get(language_name) == null){
                     String needStr = allHead;
                     mapStation.put(language_name, needStr);
                 } else{

                 }
                 //收测站点_可听率_大于3分次数_总次数
                 String newHeadendValue = "";
                 String[] headendStationArr = (mapStation.get(language_name)).split(",");
                 for(String headValue:headendStationArr){
                     String[] headValueArr = headValue.split("_");
                     if(headValueArr.length>2)
                     {
                     int big3Count = (countoNum>=3?1:0)+ Integer.parseInt(  (headValueArr[2].equals("A")?"0":headValueArr[2]) );
                     int totalCount = 1 + Integer.parseInt(  (headValueArr[3].equals("A")?"0":headValueArr[3]) );
                     if(headValueArr[0].equals(shortname)){
                         newHeadendValue += shortname+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+"_"+big3Count+"_"+totalCount+",";
                     } else{
                         if((mapStation.get(language_name)).indexOf(shortname+"_")>-1){
                             newHeadendValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
                         } else{
                             newHeadendValue += shortname+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+"_"+big3Count+"_1,";

                         }
                     }
                     }
                 }
                 mapStation.put(language_name, newHeadendValue);

                 //计算大洲的收听次数
                 if(mapState.get(language_name) == null){
                     String needStr = allState;
                     mapState.put(language_name, needStr);
                 } else{
                 }
                 String newSatateValue = "";
                 String[] stateStationArr = (mapState.get(language_name)).split(",");
                 for(String headValue:stateStationArr){
                     String[] headValueArr = headValue.split("_");
                     if(headValueArr.length>2)
                     {
     	                int big3Count = (countoNum>=3?1:0)+ Integer.parseInt(  (headValueArr[2].equals("A")?"0":headValueArr[2]) );
     	                int totalCount = 1 + Integer.parseInt(  (headValueArr[3].equals("A")?"0":headValueArr[3]) );
     	                if(headValueArr[0].equals(state_name)){
     	                    newSatateValue += state_name+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+"_"+big3Count+"_"+totalCount+",";
     	                } else{
     	                    if((mapState.get(language_name)).indexOf(state_name+"_")>-1){
     	                        newSatateValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
     	                    } else{
     	                        newSatateValue += state_name+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+"_"+big3Count+"_1,";
     	                    }
     	                }
                     }
                 }
                 mapState.put(language_name, newSatateValue);
                 //计算地区的收听次数
                 String[] service_areaArr = service_area.split(",");
                 for(int ai=0;ai<service_areaArr.length;ai++){
                     service_area = service_areaArr[ai];
                     if(mapArea.get(language_name) == null){
                         String needStr = allArea;

                         mapArea.put(language_name, needStr);
                     } else{
                     }
                     String newAreaValue = "";
                     String[] areaStationArr = (mapArea.get(language_name)).split(",");
                     for(String headValue:areaStationArr){
                         String[] headValueArr = headValue.split("_");
                         if(headValueArr.length>2)
                         {
     	                    int big3Count = (countoNum>=3?1:0)+ Integer.parseInt(  (headValueArr[2].equals("A")?"0":headValueArr[2]) );
     	                    int totalCount = 1 + Integer.parseInt(  (headValueArr[3].equals("A")?"0":headValueArr[3]) );
     	                    if(headValueArr[0].equals(service_area)){
     	                        newAreaValue += service_area+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+"_"+big3Count+"_"+totalCount+",";
     	                    } else{
     	                        if((mapArea.get(language_name)).indexOf(service_area+"_")>-1){
     	                            newAreaValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
     	                        } else{
     	                            newAreaValue += service_area+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+"_"+big3Count+"_1,";
     	
     	                        }
     	                    }
                         }
                     }

                     mapArea.put(language_name, newAreaValue);
                 }
                 //计算全部的收听次数
                 int big3Count = (countoNum>=3?1:0);
                 if(mapTotal.get(language_name) == null){
                     String needStr = "全球_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+"_"+big3Count+"_1";
                     mapTotal.put(language_name, needStr);
                 } else{
                     int big3CountT = (countoNum>=3?1:0)+ Integer.parseInt(mapTotal.get(language_name).split("_")[2]);
                     int totalCountT = 1 + Integer.parseInt(mapTotal.get(language_name).split("_")[3]);

                     mapTotal.put(language_name, "全球_"+new BigDecimal((Double.parseDouble(big3CountT+"")*100/totalCountT)).setScale(1,BigDecimal.ROUND_HALF_UP)+"_"+big3CountT+"_"+totalCountT);
                 }


             }
        }

        for(String o:mapStation.keySet()){
            String val = mapStation.get(o);
            ZrstRepEffectStatisticsBean  bean = new ZrstRepEffectStatisticsBean();
            bean.setLanguage_name(o.toString());
            String traVal = val;
            traVal += mapArea.get(o);
            traVal += mapState.get(o);
            traVal += mapTotal.get(o);
//			traVal = traVal.substring(0, traVal.length()-1);
            bean.setReceive_name_total_hours(traVal);
            bean.setSub_report_type("32");
            bean.setReport_type(my_report_type);
            bean.setReport_id(report_id);
            map.put((String)o, bean);

        }
        return map;
    }
    /**
     * 语言总体播出效果统计1
     * @detail
     * @method
     * @param starttime
     * @param endtime
     * @param my_report_type
     * @param report_type
     * @param asobj
     * @return
     * @throws Exception
     * @return  HashMap
     * @author  zhaoyahui
     * @version 2013-1-10 下午07:32:41
     */
    public LinkedHashMap statisticsReportCondition31(String start, String end,String my_report_type,String report_type,ASObject asobj,String report_id) throws Exception{
        String headcodes = (String)asobj.get("headcodes");
        LinkedHashMap<String,ZrstRepEffectStatisticsBean> map = new LinkedHashMap<String,ZrstRepEffectStatisticsBean>();
        LinkedHashMap<String,String> mapHead = new LinkedHashMap<String,String>();
        LinkedHashMap<String,String> mapState = new LinkedHashMap<String,String>();
        LinkedHashMap<String,String> mapArea = new LinkedHashMap<String,String>();
        LinkedHashMap<String,String> mapTotal = new LinkedHashMap<String,String>();
        List<KeyValueForDate> list = getKeyValueForDate(start,end);
        for(int key=0;key<list.size();key++){
        	KeyValueForDate keyValue =list.get(key);
            String valid_end_timeString="";
            if(my_report_type.equals("1")){//国际台
                valid_end_timeString=" and runplan.valid_end_time>=to_date('"+keyValue.getEndDate()+"','yyyy-mm-dd hh24:mi:ss') ";
            }
            String sql = "select  " +
                    "  head.code  headcode,head.service_name service_name_headend,head.type_id, mark.headname as shortname,mark.counto,runplan.service_area,state.state_name ,lan.language_name" +
                    " from radio_mark_view_xiaohei mark," +
                    //" radio_stream_result_tab record," +
                    " zres_runplan_chaifen_tab runplan," +
                    " res_headend_tab head," +
                    " zdic_language_tab lan ," +
                    " dic_state_tab            state" +
                    " where mark.runplan_id=runplan.runplan_id and mark.counto is not null and mark.counti is not null and mark.counts is not null " +
                    " and head.code=mark.head_code and head.is_delete =0  " +
                    " and  runplan.language_id=lan.language_id " +
                    " and mark.report_type=1 " +//效果录音
                    //" and runplan.is_delete=0 " +

                    valid_end_timeString +


                    //	" and record.is_delete=0 " +
                    " and runplan.runplan_type_id='"+my_report_type+"'"+
                    " and state.state = head.state" +
                    " and mark.start_datetime>=to_date('"+keyValue.getStartDate()+"','yyyy-mm-dd hh24:mi:ss')" +
                    " and mark.end_datetime<=to_date('"+keyValue.getEndDate()+"','yyyy-mm-dd hh24:mi:ss') order by head.code,lan.language_name ";
            GDSet gd = DbComponent.Query(sql);
            for(int i=0;i<gd.getRowCount();i++){
                String language_name = gd.getString(i, "language_name");
                String service_area = gd.getString(i, "service_name_headend");
                String state_name = gd.getString(i, "state_name");
                String type_id = gd.getString(i, "type_id");
                String headcode = gd.getString(i, "headcode");
                String shortname = gd.getString(i, "shortname");
                if(service_area == null || service_area.equals("")){
                    throw new Exception(shortname+"的服务区为空，请先设置");
                }
                if("102".equals(type_id) && ( headcode.endsWith("A") || headcode.endsWith("B")||headcode.endsWith("C") || headcode.endsWith("D")|| headcode.endsWith("E") || headcode.endsWith("F") || headcode.endsWith("G")  )){
                    headcode = headcode.substring(0, headcode.length()-1);
                }
                if("102".equals(type_id) && ( shortname.endsWith("A") || shortname.endsWith("B")||shortname.endsWith("C") || shortname.endsWith("D") || shortname.endsWith("E")|| shortname.endsWith("F")|| shortname.endsWith("G"))){
                    shortname = shortname.substring(0, shortname.length()-1);
                }
                if(headcodes == null || headcodes.equals("") || headcodes.indexOf(headcode+",")>=0){
                } else{
                    continue;
                }
                String counto = gd.getString(i, "counto");
                int countoNum = Integer.parseInt(counto);
                String transmit_station_listens = "";//语言_>=3分_总次数_可听率%  例如：德_88_123_78,2032_23_423_28
                if(mapHead.get(shortname) == null){
                    int big3Count = (countoNum>=3?1:0);
                    String needStr = language_name+"_"+big3Count+"_1_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+",";
                    mapHead.put(shortname, needStr);
                } else{
                    String newHeadValue = "";
                    if((mapHead.get(shortname)).indexOf(language_name+"_")<0){
                        newHeadValue = mapHead.get(shortname)+language_name+"_0_0_0,";
                        mapHead.put(shortname, newHeadValue);
                        newHeadValue = "";
                    }
                    String[] headStationArr = (mapHead.get(shortname)).split(",");
                    for(String headValue:headStationArr){
                        String[] headValueArr = headValue.split("_");
                        if(headValueArr[0].equals(language_name)){
                            int big3Count = (countoNum>=3?1:0)+Integer.parseInt(headValueArr[1]);
                            int totalCount = Integer.parseInt(headValueArr[2])+1;
                            newHeadValue += language_name+"_"+big3Count+"_"+totalCount+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+",";
                        } else{
                            int big3Count = (countoNum>=3?1:0);
                            if((mapHead.get(shortname)).indexOf(language_name+"_")>-1){//如果有这个语言，此次把数据从新放回去
                                newHeadValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
                            } else{//如果没有，加入新的
                            }
                        }
                    }
                    mapHead.put(shortname, newHeadValue);
                }

                String[] service_areaArr = service_area.split(",");
                for(int ai=0;ai<service_areaArr.length;ai++){
                    service_area = service_areaArr[ai];
                    if(mapArea.get(service_area) == null){//地区
                        int big3Count = (countoNum>=3?1:0);
                        mapArea.put(service_area, language_name+"_"+big3Count+"_1_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+",");
                    } else{
                        String newHeadValue = "";
                        if((mapArea.get(service_area)).indexOf(language_name+"_")<0){
                            newHeadValue = mapArea.get(service_area)+language_name+"_0_0_0,";
                            mapArea.put(service_area, newHeadValue);
                            newHeadValue = "";
                        }
                        String[] headStationArr = (mapArea.get(service_area)).split(",");
                        for(String headValue:headStationArr){
                            String[] headValueArr = headValue.split("_");
                            if(headValueArr[0].equals(language_name)){
                                int big3Count = (countoNum>=3?1:0)+Integer.parseInt(headValueArr[1]);
                                int totalCount = Integer.parseInt(headValueArr[2])+1;
                                newHeadValue += language_name+"_"+big3Count+"_"+totalCount+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+",";
                            } else{
                                int big3Count = (countoNum>=3?1:0);
                                if((mapArea.get(service_area)).indexOf(language_name+"_")>-1){
                                    newHeadValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
                                } else{
                                }
                            }
                        }
                        mapArea.put(service_area, newHeadValue);
                    }
                }
                if(mapState.get(state_name) == null){//大洲
                    int big3Count = (countoNum>=3?1:0);
                    mapState.put(state_name, language_name+"_"+big3Count+"_1_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+",");
                } else{
                    String newHeadValue = "";
                    if((mapState.get(state_name)).indexOf(language_name+"_")<0){
                        newHeadValue = mapState.get(state_name)+language_name+"_0_0_0,";
                        mapState.put(state_name, newHeadValue);
                        newHeadValue = "";
                    }
                    String[] headStationArr = (mapState.get(state_name)).split(",");
                    for(String headValue:headStationArr){
                        String[] headValueArr = headValue.split("_");
                        if(headValueArr[0].equals(language_name)){
                            int big3Count = (countoNum>=3?1:0)+Integer.parseInt(headValueArr[1]);
                            int totalCount = Integer.parseInt(headValueArr[2])+1;
                            newHeadValue += language_name+"_"+big3Count+"_"+totalCount+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+",";
                        } else{
                            int big3Count = (countoNum>=3?1:0);
                            if((mapState.get(state_name)).indexOf(language_name+"_")>-1){
                                newHeadValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
                            } else{
                            }
                        }
                    }
                    mapState.put(state_name, newHeadValue);
                }

                if(mapTotal.get("全球") == null){//全部
                    int big3Count = (countoNum>=3?1:0);
                    mapTotal.put("全球", language_name+"_"+big3Count+"_1_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+",");
                } else{
                    String newHeadValue = "";
                    if((mapTotal.get("全球")).indexOf(language_name+"_")<0){
                        newHeadValue = mapTotal.get("全球")+language_name+"_0_0_0,";
                        mapTotal.put("全球", newHeadValue);
                        newHeadValue = "";
                    }
                    String[] headStationArr = (mapTotal.get("全球")).split(",");
                    for(String headValue:headStationArr){
                        String[] headValueArr = headValue.split("_");
//    							 if(headValue.indexOf("___")>0){
//    								 newHeadValue = headValue;
//    							 } else{
                        if(headValueArr[0].equals(language_name)){
                            int big3Count = (countoNum>=3?1:0)+Integer.parseInt(headValueArr[1]);
                            int totalCount = Integer.parseInt(headValueArr[2])+1;
                            newHeadValue += language_name+"_"+big3Count+"_"+totalCount+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+",";
                        } else{
                            int big3Count = (countoNum>=3?1:0);
                            if((mapTotal.get("全球")).indexOf(language_name+"_")>-1){
                                newHeadValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
                            } else{
                            }
                        }
//    							 }
                    }
                    mapTotal.put("全球", newHeadValue);
                }
            }
        }
        //发射台_>=3分_总次数_可听率%  例如：2022_88_123_78,2032_23_423_28
        for(Object o:mapHead.keySet()){
            String val = mapHead.get(o);
            ZrstRepEffectStatisticsBean  bean = new ZrstRepEffectStatisticsBean();
            bean.setReceive_name(o.toString());
            bean.setLanguage_name_listens(val.substring(0, val.length()-1));
            bean.setSub_report_type("31");
            bean.setReport_type(my_report_type);
            bean.setReport_id(report_id);
            map.put((String)o, bean);

        }
        for(Object o:mapArea.keySet()){
            String val = mapArea.get(o);
            ZrstRepEffectStatisticsBean  bean = new ZrstRepEffectStatisticsBean();
            bean.setService_area(o.toString());
            bean.setLanguage_name_listens(val.substring(0, val.length()-1));
            bean.setSub_report_type("31");
            bean.setReport_type(my_report_type);
            bean.setReport_id(report_id);
            map.put((String)o, bean);
        }
        for(Object o:mapState.keySet()){
            String val = mapState.get(o);
            ZrstRepEffectStatisticsBean  bean = new ZrstRepEffectStatisticsBean();
            bean.setState_name(o.toString());
            bean.setLanguage_name_listens(val.substring(0, val.length()-1));
            bean.setSub_report_type("31");
            bean.setReport_type(my_report_type);
            bean.setReport_id(report_id);
            map.put((String)o, bean);
        }
        for(Object o:mapTotal.keySet()){
            String val = mapTotal.get(o);
            ZrstRepEffectStatisticsBean  bean = new ZrstRepEffectStatisticsBean();
            bean.setLanguage_name_listens(val.substring(0, val.length()-1));
            bean.setSub_report_type("31");
            bean.setReport_type(my_report_type);
            bean.setReport_id(report_id);
            map.put((String)o, bean);
        }
        return map;
    }
    /**
     * 发射台总体播出效果统计3
     * @detail
     * @method
     * @param starttime
     * @param endtime
     * @param my_report_type
     * @param report_type
     * @param asobj
     * @return
     * @throws Exception
     * @return  HashMap
     * @author  zhaoyahui
     * @version 2013-1-10 下午07:32:41
     */
    public LinkedHashMap statisticsReportCondition23(String starttime, String endtime,String my_report_type,String report_type,ASObject asobj,String report_id) throws Exception{
        asobj.put("rule", "3");
        LinkedHashMap<String,double[]> mapStation = new LinkedHashMap<String,double[]>();
        LinkedHashMap map = statisticsReportCondition11(starttime, endtime, my_report_type,report_type, asobj, report_id);
        LinkedHashMap<String,double[]> mapProgram = new LinkedHashMap<String,double[]>();
        for(Object o:map.keySet()){
            ZrstRepEffectStatisticsBean bean = (ZrstRepEffectStatisticsBean)map.get(o);   // Map的值
            String stationname = bean.getTransmit_station();
            if(stationname.equals("")){
                continue;
            }
            float listionRate = Float.valueOf(bean.getListen());
            
            double timeLengthVal = Integer.parseInt(bean.getReceive_count())*0.5;
            double[] vals = {0,0,0,0};
            double[] vals1 = {0,0,0,0};
            if(mapStation.get(stationname) == null){

            } else{
                vals =  mapStation.get(stationname);
            }
            if(listionRate>=80){//效果是可保证收听
                vals[0] += timeLengthVal;
            } else if(listionRate>=60 && listionRate<80){//效果是基本可收听
                vals[1] += timeLengthVal;
            } else if(listionRate>=30 && listionRate<60){//效果是有时可收听
                vals[2] += timeLengthVal;
            } else if(listionRate<30){//效果是不能收听
                vals[3] += timeLengthVal;
//				 LogTool.fatal(stationname+">>>>>>>>>>>>>>>>>>>>>>>>>>>>"+bean.getListen()+"<<<<<<<<<<<<<<<<<<<"+timeLengthVal);
            }
            mapStation.put(stationname, vals);


            String program_type = bean.getAll_listens();
            if(program_type.equals("")){
                continue;
            } else if("1".equals(program_type)){
                program_type = "海外转播";
            } else if("2".equals(program_type)){
                program_type = "国内直属";
            } else if("3".equals(program_type)){
                program_type = "国外直属";
            } else if("4".equals(program_type)){
                program_type = "国内地方";
            }
            listionRate = Float.valueOf(bean.getListen());
            timeLengthVal = Integer.parseInt(bean.getReceive_count())*0.5;
            // vals[0] = vals[1] = vals[2] = vals[3] = 0;
            if(mapProgram.get(program_type) == null){

            } else{
                vals1 =  mapProgram.get(program_type);
            }
            if(listionRate>=80){//效果是可保证收听
                vals1[0] += timeLengthVal;
            } else if(listionRate>=60 && listionRate<80){//效果是基本可收听
                vals1[1] += timeLengthVal;
            } else if(listionRate>=30 && listionRate<60){//效果是有时可收听
                vals1[2] += timeLengthVal;
            } else if(listionRate<30){//效果是不能收听
                vals1[3] += timeLengthVal;
//				 LogTool.fatal(stationname+">>>>>>>>>>>>>>>>>>>>>>>>>>>>"+bean.getListen()+"<<<<<<<<<<<<<<<<<<<"+timeLengthVal);
            }
            mapProgram.put(program_type, vals1);
        }







        double[] totals = {0,0,0,0};
        LinkedHashMap resmap = new LinkedHashMap();
        for(String o:mapStation.keySet()){
            double[] valNum = mapStation.get(o);
            ZrstRepEffectStatisticsBean bean = new ZrstRepEffectStatisticsBean();
            bean.setTransmit_station(o);
            bean.setReceive_listens(valNum[0]+"_"+valNum[1]+"_"+valNum[2]+"_"+valNum[3]);
            bean.setSub_report_type("23");
            bean.setReport_type(my_report_type);
            bean.setReport_id(report_id);
            resmap.put((String)o, bean);
            totals[0] += valNum[0];
            totals[1] += valNum[1];
            totals[2] += valNum[2];
            totals[3] += valNum[3];
        }
        for(String o:mapProgram.keySet()){
            double[] valNum = mapProgram.get(o);
            ZrstRepEffectStatisticsBean bean = new ZrstRepEffectStatisticsBean();
            bean.setTransmit_station(o);
            bean.setReceive_listens(valNum[0]+"_"+valNum[1]+"_"+valNum[2]+"_"+valNum[3]);
            bean.setSub_report_type("23");
            bean.setReport_type(my_report_type);
            bean.setReport_id(report_id);
            resmap.put((String)o, bean);
        }
        if((totals[0]+totals[0]+totals[1]+totals[2]+totals[3])>0){
            ZrstRepEffectStatisticsBean bean = new ZrstRepEffectStatisticsBean();
            bean.setTransmit_station("总  计");
            bean.setReceive_listens(totals[0]+"_"+totals[1]+"_"+totals[2]+"_"+totals[3]);
            bean.setSub_report_type("23");
            bean.setReport_type(my_report_type);
            bean.setReport_id(report_id);
            resmap.put("总  计", bean);
        }
        return resmap;
    }
    /**
     * 国际台广播效果统计表
     * @detail
     * @method
     * @param starttime
     * @param endtime
     * @param my_report_type
     * @param report_type
     * @param asobj
     * @return
     * @throws Exception
     * @return  HashMap
     * @author  zhaoyahui
     * @version 2013-1-10 下午07:32:41
     */
    public LinkedHashMap statisticsReportCondition11(String start, String end,String my_report_type,String report_type,ASObject asobj,String report_id) throws Exception{
        String headcodes = (String)asobj.get("headcodes");
        String rule = (String)asobj.get("rule");//1、按播音时间统计 2、按半小时统计 3、按一小时统计
//		String headcodes = null;//test
        LinkedHashMap map = new LinkedHashMap();
        List<KeyValueForDate> list = getKeyValueForDate(start,end);
        for(int key=0;key<list.size();key++){
        	KeyValueForDate keyValue =list.get(key);
        	 String sql  = "";
             if(my_report_type.equals("1")){//国际台
                 sql = "select runplan.runplan_id,runplan.start_time ||'-'|| runplan.end_time as play_time,lan.language_name, runplan.freq,station.name stationname,runplan.direction," +
                         " runplan.power, head.code  headcode,head.service_name service_name_headend,head.type_id, mark.headname as shortname," +
                         " mark.counto,runplan.service_area,runplan.valid_start_time ,runplan.valid_end_time,mark.start_datetime," +
                         " dis.runplan_id as dis_runplan_id,dis.disturb,runplan.program_type  " +
                         " from radio_mark_view_xiaohei mark," +
                         //	" radio_stream_result_tab record," +
                         " zres_runplan_chaifen_tab runplan," +
                         " zdic_language_tab lan ," +
                         " res_transmit_station_tab station," +
                         " res_headend_tab head," +
                         " (select * from zres_runplan_disturb_tab where  valid_end_time>to_date('"+keyValue.getEndDate()+"','yyyy-mm-dd hh24:mi:ss')) dis  "+
                         //" zres_runplan_disturb_tab dis " +
                         " where mark.runplan_id=runplan.runplan_id and mark.counto is not null and mark.counti is not null and mark.counts is not null and  runplan.language_id=lan.language_id " +
                         " and runplan.station_id=station.station_id and head.is_delete =0 and head.code=mark.head_code" +
                         " and mark.report_type=1 " +//效果录音
                         //	" and mark.is_delete=0 " +
                         //" and runplan.is_delete=0 " +
                         " and runplan.valid_end_time>=to_date('"+keyValue.getEndDate()+"','yyyy-mm-dd hh24:mi:ss') "+
                         " and runplan.parent_id = dis.runplan_id(+) " +
                         //" and dis.is_delete=0 " +
                         " and runplan.runplan_type_id='"+my_report_type+"'"+
                         " and mark.start_datetime>=to_date('"+keyValue.getStartDate()+"','yyyy-mm-dd hh24:mi:ss')" +
                         " and mark.end_datetime<=to_date('"+keyValue.getEndDate()+"','yyyy-mm-dd hh24:mi:ss') ";
                 if(headcodes!=null&&!headcodes.equalsIgnoreCase("")){
                     //sql+=" and '"+headcodes.substring(0, headcodes.length()-1)+"' like '%'||mark.HEAD_CODE||'%'  ";
                     if(headcodes.split(",").length>1){
                         String[] ss = headcodes.split(",");
                         String newsql="";
                         for(int m=0;m<ss.length;m++){
                             newsql+=" mark.HEAD_CODE like '%"+ss[m]+"%' or";
                         }
                         sql+=" and ("+newsql.substring(0, newsql.length()-2)+")";
                     }else{
                         sql+=" and mark.HEAD_CODE like '%"+headcodes.substring(0, headcodes.length()-1)+"%'";
                     }

                 }

                 sql+=" order by head.shortname,station.name,runplan.freq";
             } else if(my_report_type.equals("2")){//海外落地
                 sql = "select runplan.runplan_id,runplan.start_time ||'-'|| runplan.end_time as play_time,lan.language_name, runplan.freq,runplan.direction,runplan.redisseminators," +
                         " runplan.power, head.code  headcode,head.service_name service_name_headend,head.type_id, mark.HEADNAME shortname," +
                         " mark.counto,runplan.service_area,runplan.valid_start_time ,runplan.valid_end_time,mark.start_datetime," +
                         " dis.runplan_id as dis_runplan_id,dis.disturb,runplan.program_type  " +
                         " from radio_mark_view_xiaohei mark," +
                         //" radio_stream_result_tab record," +
                         " zres_runplan_chaifen_tab runplan," +
                         " zdic_language_tab lan ," +
                         " res_headend_tab head," +
                         " (select * from zres_runplan_disturb_tab where  valid_end_time>to_date('"+keyValue.getEndDate()+"','yyyy-mm-dd hh24:mi:ss')) dis  "+
                         //" zres_runplan_disturb_tab dis " +
                         " where mark.runplan_id=runplan.runplan_id and mark.counto is not null and mark.counti is not null and mark.counts is not null and  runplan.language_id=lan.language_id " +
                         "  and head.code=mark.head_code and head.is_delete =0  " +
                         " and mark.report_type=1 " +//效果录音
                         //	" and mark.is_delete=0 " +
                         //" and runplan.is_delete=0 " +
                         //" and runplan.valid_end_time>=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') "+

                         " and runplan.parent_id = dis.runplan_id(+) " +
                         //" and dis.is_delete=0 " +
                         " and runplan.runplan_type_id='"+my_report_type+"'"+
                         " and mark.start_datetime>=to_date('"+keyValue.getStartDate()+"','yyyy-mm-dd hh24:mi:ss')" +
                         " and mark.end_datetime<=to_date('"+keyValue.getEndDate()+"','yyyy-mm-dd hh24:mi:ss') ";
                 if(headcodes!=null&&!headcodes.equalsIgnoreCase("")){
                     //sql+=" and '"+headcodes.substring(0, headcodes.length()-1)+"' like '%'||mark.HEAD_CODE||'%'  ";
                     if(headcodes.split(",").length>1){
                         String[] ss = headcodes.split(",");
                         String newsql="";
                         for(int m=0;m<ss.length;m++){
                             newsql+=" mark.HEAD_CODE like '%"+ss[m]+"%' or";
                         }
                         sql+=" and ("+newsql.substring(0, newsql.length()-2)+")";
                     }else{
                         //sql+="and '"+headcodes.substring(0, headcodes.length()-1)+"' like '%'||head.code||'%' ";
                         sql+=" and mark.HEAD_CODE like '%"+headcodes.substring(0, headcodes.length()-1)+"%'";
                     }

                 }
                 sql+=" order by head.shortname,runplan.freq";
             }

             GDSet gd = DbComponent.Query(sql);
            
             for(int i=0;i<gd.getRowCount();i++){
                 String program_type = gd.getString(i, "program_type");//节目类型
                 //备注信息由同邻频干扰情况信息表代入，对于可听率低于60%未代入同邻频干扰信息的需要在备注信息中直接加上信号弱，杂音大
                 String dis_runplan_id = gd.getString(i, "dis_runplan_id");//干扰信息表运行图id
                 String runplan_id = gd.getString(i, "runplan_id");//运行图id
                 String disturb = gd.getString(i, "disturb");//干扰信息
                 String play_time = gd.getString(i , "play_time");
                 String start_datetime = gd.getString(i, "start_datetime");
                 if(!rule.equals("1")){
                     play_time = getPlayTime(rule,start_datetime);
                 }
                 String language_name = gd.getString(i, "language_name");
                 String freq = gd.getString(i, "freq");
                 String valid_start_time = gd.getString(i, "valid_start_time");
                 String valid_end_time = gd.getString(i, "valid_end_time");
                 String stationname = "";
                 String runplan_type_id = "";//1：国际台运行图2：海外落地
                 if(my_report_type.equals("2")){
                     String redisseminators = gd.getString(i, "redisseminators");//转播机构
                     if(redisseminators.equals("")){
                         redisseminators = " ";
                     }
                     stationname = redisseminators;
                     runplan_type_id = "2";
                 } else{
                     stationname = gd.getString(i, "stationname");
                     runplan_type_id = "1";
                 }
                 String direction = gd.getString(i, "direction");
                 String power = gd.getString(i, "power");
                 String type_id = gd.getString(i, "type_id");
                 String headcode = gd.getString(i, "headcode");
                 String shortname = gd.getString(i, "shortname");
                 if("102".equals(type_id) && ( headcode.endsWith("A") || headcode.endsWith("B")||headcode.endsWith("C") || headcode.endsWith("D")|| headcode.endsWith("E") || headcode.endsWith("F") || headcode.endsWith("G")  )){
                     headcode = headcode.substring(0, headcode.length()-1);
                     // shortname = shortname.substring(0, shortname.length()-1);
                 }
                 if("102".equals(type_id) && ( shortname.endsWith("A") || shortname.endsWith("B")||shortname.endsWith("C") || shortname.endsWith("D") || shortname.endsWith("E")|| shortname.endsWith("F")|| shortname.endsWith("G"))){
                     shortname = shortname.substring(0, shortname.length()-1);
                 }
                 if(headcodes == null || headcodes.equals("") || headcodes.indexOf(headcode+",")>=0){
                 } else{
                     continue;
                 }
                 String counto = gd.getString(i, "counto");
                 String service_area = gd.getString(i, "service_area");
                 if(service_area == null || service_area.equals("")){
                     throw new Exception(shortname+"的服务区为空，请先设置");
                 }
                 int countoNum = Integer.parseInt(counto);
                 String[] service_areaArr = service_area.split(",");
                 for(int ai=0;ai<service_areaArr.length;ai++){
                     service_area = service_areaArr[ai];
                     //这里需要考虑代播的情况。
                     String mapKey = headcode+play_time+language_name+freq+stationname+direction+power+service_area;
                     //String mapKey = headcode+play_time+language_name+freq+direction+power+service_area;

                     ZrstRepEffectStatisticsBean allBean = (ZrstRepEffectStatisticsBean)map.get(headcode+"总体效果");
                     if(allBean == null){
                         double fen0 = countoNum==0?1:0;
                         double fen1 = countoNum==1?1:0;
                         double fen2 = countoNum==2?1:0;
                         double fen3 = countoNum==3?1:0;
                         double fen4 = countoNum==4?1:0;
                         double fen5 = countoNum==5?1:0;
                         allBean = new ZrstRepEffectStatisticsBean();
                         allBean.setReceive_name(shortname);
                         allBean.setReceive_code(headcode);
                         allBean.setPlay_time("总体效果");
                         allBean.setReceive_count("1");
                         allBean.setFen0(fen0+"");
                         allBean.setFen1(fen1+"");
                         allBean.setFen2(fen2+"");
                         allBean.setFen3(fen3+"");
                         allBean.setFen4(fen4+"");
                         allBean.setFen5(fen5+"");
                         allBean.setListen(new BigDecimal((fen3+fen4+fen5)*100/1).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
                         allBean.setListen_middle(countoNum+"");//第一次
                         allBean.getListen_middleList().add(countoNum);
                         allBean.setBak("");
                         allBean.setSub_report_type("11");
                         allBean.setReport_type(my_report_type);
                         allBean.setReport_id(report_id);
                         map.put(headcode+"总体效果", allBean);
                     } else{
                         int receive_count_all = Integer.parseInt(allBean.getReceive_count());
                         receive_count_all++;
                         allBean.setReceive_count(receive_count_all+"");

                         double fen0_all = countoNum==0?Double.parseDouble(allBean.getFen0())+1:Double.parseDouble(allBean.getFen0());
                         double fen1_all = countoNum==1?Double.parseDouble(allBean.getFen1())+1:Double.parseDouble(allBean.getFen1());
                         double fen2_all = countoNum==2?Double.parseDouble(allBean.getFen2())+1:Double.parseDouble(allBean.getFen2());
                         double fen3_all = countoNum==3?Double.parseDouble(allBean.getFen3())+1:Double.parseDouble(allBean.getFen3());
                         double fen4_all = countoNum==4?Double.parseDouble(allBean.getFen4())+1:Double.parseDouble(allBean.getFen4());
                         double fen5_all = countoNum==5?Double.parseDouble(allBean.getFen5())+1:Double.parseDouble(allBean.getFen5());
                         allBean.setFen0(fen0_all+"");
                         allBean.setFen1(fen1_all+"");
                         allBean.setFen2(fen2_all+"");
                         allBean.setFen3(fen3_all+"");
                         allBean.setFen4(fen4_all+"");
                         allBean.setFen5(fen5_all+"");
                         allBean.setListen(new BigDecimal((fen3_all+fen4_all+fen5_all)*100/receive_count_all).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
                         allBean.getListen_middleList().add(countoNum);
                         ArrayList mList_all = allBean.getListen_middleList();
                         Collections.sort(mList_all);
                         int middleNum_all = (int) Math.ceil(mList_all.size()/2);
                         allBean.setListen_middle(mList_all.get(middleNum_all)+"");
                         allBean.setListen_middleList(mList_all);

                     }
                     map.put(headcode+"总体效果", allBean);

                     if(map.get(mapKey) == null){
                         ZrstRepEffectStatisticsBean detailBean = new ZrstRepEffectStatisticsBean();
                         detailBean.setPlay_time(play_time);
                         detailBean.setLanguage_name(language_name);
                         detailBean.setFreq(freq);
                         detailBean.setRunplan_id(runplan_id);
                         detailBean.setTransmit_station(stationname);
                         detailBean.setRunplan_type_id_temp(runplan_type_id);
                         detailBean.setTransmit_direction(direction);
                         detailBean.setTransmit_power(power);
                         detailBean.setService_area(service_area);
                         detailBean.setReceive_name(shortname);
                         detailBean.setReceive_code(headcode);
                         detailBean.setValid_start_time_temp(valid_start_time);
                         detailBean.setValid_end_time_temp(valid_end_time);
                         detailBean.setAll_listens(program_type);//在11类型下 这个字段暂时用来存放节目类型 节目类别1:海外转播,2:国内直属,3:国外直属,4:国内地方

                         double fen0 = countoNum==0?1:0;
                         double fen1 = countoNum==1?1:0;
                         double fen2 = countoNum==2?1:0;
                         double fen3 = countoNum==3?1:0;
                         double fen4 = countoNum==4?1:0;
                         double fen5 = countoNum==5?1:0;
                         detailBean.setReceive_count("1");
                         detailBean.setFen0(fen0+"");
                         detailBean.setFen1(fen1+"");
                         detailBean.setFen2(fen2+"");
                         detailBean.setFen3(fen3+"");
                         detailBean.setFen4(fen4+"");
                         detailBean.setFen5(fen5+"");
                         detailBean.setListen(new BigDecimal((fen3+fen4+fen5)*100/1).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
                         detailBean.setListen_middle(countoNum+"");//第一次
                         detailBean.getListen_middleList().add(countoNum);
                         if(dis_runplan_id.equals("")){
                             if((fen3+fen4+fen5)*100/1<60){
                                 disturb = "信号弱，杂音大";
                             }
                         }
                         detailBean.setBak(disturb);
                         detailBean.setSub_report_type("11");
                         detailBean.setReport_type(my_report_type);
                         detailBean.setReport_id(report_id);
                         map.put(mapKey, detailBean);


                     } else{
                         ZrstRepEffectStatisticsBean detailBean = (ZrstRepEffectStatisticsBean)map.get(mapKey);
                         int receive_count = Integer.parseInt(detailBean.getReceive_count());
                         receive_count++;
                         detailBean.setReceive_count(receive_count+"");

                         double fen0 = countoNum==0?Double.parseDouble(detailBean.getFen0())+1:Double.parseDouble(detailBean.getFen0());
                         double fen1 = countoNum==1?Double.parseDouble(detailBean.getFen1())+1:Double.parseDouble(detailBean.getFen1());
                         double fen2 = countoNum==2?Double.parseDouble(detailBean.getFen2())+1:Double.parseDouble(detailBean.getFen2());
                         double fen3 = countoNum==3?Double.parseDouble(detailBean.getFen3())+1:Double.parseDouble(detailBean.getFen3());
                         double fen4 = countoNum==4?Double.parseDouble(detailBean.getFen4())+1:Double.parseDouble(detailBean.getFen4());
                         double fen5 = countoNum==5?Double.parseDouble(detailBean.getFen5())+1:Double.parseDouble(detailBean.getFen5());
                         detailBean.setFen0(fen0+"");
                         detailBean.setFen1(fen1+"");
                         detailBean.setFen2(fen2+"");
                         detailBean.setFen3(fen3+"");
                         detailBean.setFen4(fen4+"");
                         detailBean.setFen5(fen5+"");
                         detailBean.setListen(new BigDecimal((fen3+fen4+fen5)*100/receive_count).setScale(1,BigDecimal.ROUND_HALF_UP)+"");
                         if(dis_runplan_id.equals("")){
                             if((fen3+fen4+fen5)*100/receive_count<60){
                                 disturb = "信号弱，杂音大";
                             }else disturb = "";
                         }
                         detailBean.getListen_middleList().add(countoNum);
                         ArrayList mList = detailBean.getListen_middleList();
                         Collections.sort(mList);
                         int middleNum = (int) Math.ceil(mList.size()/2);
                         detailBean.setBak(disturb);
                         detailBean.setListen_middle(mList.get(middleNum)+"");
                         detailBean.setListen_middleList(mList);
                         map.put(mapKey, detailBean);


                     }


                 }
             }
        }
       
//				//备注信息由同邻频干扰情况信息表代入，对于可听率低于60%未代入同邻频干扰信息的需要在备注信息中直接加上信号弱，杂音大
//				for(Object o:map.keySet()){
//					ZrstRepEffectStatisticsBean detailBean = (ZrstRepEffectStatisticsBean)map.get(o);
//					if(detailBean.getPlay_time().equals("总体效果")){
//						continue;
//					}
//					//根据频率、播音时间、有效开始时间、有效结束时间、发射台(转播机构)
//					String sqlsome = "select disturb from zres_runplan_disturb_tab where " +
//									" freq='"+detailBean.getFreq()+"'" +
//									" and language='"+detailBean.getLanguage_name()+"' " +
//									" and start_time || '-' || end_time='"+detailBean.getPlay_time()+"'" +
//									" and valid_start_time='"+detailBean.getValid_start_time_temp()+"' " +
//									" and valid_end_time='"+detailBean.getValid_end_time_temp()+"' " +
//									" and is_delete=0";
//					String subSql = "";
//					if(detailBean.getRunplan_type_id_temp().equals("1")){
//						subSql = " and station_name = '"+detailBean.getTransmit_station()+"'";
//					} else if(detailBean.getRunplan_type_id_temp().equals("2")){
//						subSql = " and redisseminators = '"+detailBean.getTransmit_station()+"'";
//					}
//					sqlsome += subSql;
//					String disturb = "";
//					 GDSet gds = DbComponent.Query(sqlsome);
//					 if(gds.getRowCount()>0){
//						 disturb = gds.getString(0, "disturb");
//						 detailBean.setBak(disturb);
//					 } else{
//						 if(Integer.parseInt(detailBean.getListen())<60){
//							 detailBean.setBak("信号弱，杂音大");
//						 }
//					 }
//					 map.put(o, detailBean);
//				}

        return map;
    }
    /**
     * 发射台总体播出效果统计1
     * @detail
     * @method
     * @param starttime
     * @param endtime
     * @param my_report_type
     * @param report_type
     * @param asobj
     * @return
     * @throws Exception
     * @return  HashMap
     * @author  zhaoyahui
     * @version 2013-1-10 下午07:32:41
     */
    public LinkedHashMap statisticsReportCondition21(String start, String end,String my_report_type,String report_type,ASObject asobj,String report_id) throws Exception{
        String headcodes = (String)asobj.get("headcodes");
//		String headcodes = null;//test
        LinkedHashMap<String,ZrstRepEffectStatisticsBean> map = new LinkedHashMap<String,ZrstRepEffectStatisticsBean>();
        List<KeyValueForDate> list = getKeyValueForDate(start,end);
        LinkedHashMap<String,String> mapHead = new LinkedHashMap<String,String>();
        LinkedHashMap<String,String> mapState = new LinkedHashMap<String,String>();
        LinkedHashMap<String,String> mapArea = new LinkedHashMap<String,String>();
        LinkedHashMap<String,String> mapTotal = new LinkedHashMap<String,String>();
        for(int key=0;key<list.size();key++){
        	KeyValueForDate keyValue =list.get(key);
        	  String sql  = "";
              if(my_report_type.equals("1")){//国际台
                  sql = "select  " +
                          "  head.code  headcode,head.service_name service_name_headend,head.type_id, mark.headname as shortname,mark.counto,runplan.service_area,state.state_name,station.name stationname,runplan.redisseminators " +
                          " from radio_mark_view_xiaohei mark," +
                          //	" radio_stream_result_tab record," +
                          " zres_runplan_chaifen_tab runplan," +
                          " res_headend_tab head," +
                          " res_transmit_station_tab station," +
                          " dic_state_tab            state" +
                          " where mark.runplan_id=runplan.runplan_id  and mark.counto is not null and mark.counti is not null and mark.counts is not null " +
                          " and runplan.station_id=station.station_id and head.is_delete =0  and head.code=mark.head_code" +
                          " and mark.report_type=1 " +//效果录音
                          //	" and mark.is_delete=0 " +
                          //" and runplan.is_delete=0 " +
                          " and runplan.valid_end_time>=to_date('"+keyValue.getEndDate()+"','yyyy-mm-dd hh24:mi:ss') "+
                          " and runplan.runplan_type_id='"+my_report_type+"'"+
                          " and state.state = head.state" +
                          " and mark.start_datetime>=to_date('"+keyValue.getStartDate()+"','yyyy-mm-dd hh24:mi:ss')" +
                          " and mark.end_datetime<=to_date('"+keyValue.getEndDate()+"','yyyy-mm-dd hh24:mi:ss') order by head.code,stationname ";
              } else if(my_report_type.equals("2")){//海外落地
                  sql = "select  " +
                          "  head.code  headcode,head.service_name service_name_headend,head.type_id, mark.headname as shortname,mark.counto,runplan.service_area,state.state_name,runplan.redisseminators " +
                          " from radio_mark_view_xiaohei mark," +
                          //" radio_stream_result_tab record," +
                          " zres_runplan_chaifen_tab runplan," +
                          " res_headend_tab head," +
                          " dic_state_tab            state" +
                          " where mark.runplan_id=runplan.runplan_id and mark.counto is not null and mark.counti is not null and mark.counts is not null " +
                          " and head.code=mark.head_code and head.is_delete =0 " +
                          " and mark.report_type=1 " +//效果录音
                          //" and runplan.is_delete=0 " +
                          //" and runplan.valid_end_time>=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') "+

                          //" and record.is_delete=0 " +
                          " and runplan.runplan_type_id='"+my_report_type+"'"+
                          " and state.state = head.state" +
                          " and mark.start_datetime>=to_date('"+keyValue.getStartDate()+"','yyyy-mm-dd hh24:mi:ss')" +
                          " and mark.end_datetime<=to_date('"+keyValue.getEndDate()+"','yyyy-mm-dd hh24:mi:ss') order by head.code ";
              }
              GDSet gd = DbComponent.Query(sql);  
              for(int i=0;i<gd.getRowCount();i++){
                  String service_area = gd.getString(i, "service_name_headend");
                  String state_name = gd.getString(i, "state_name");
                  String stationname = "";

                  if(my_report_type.equals("2")){
                      String redisseminators = gd.getString(i, "redisseminators");//转播机构
                      if(redisseminators.equals("")){
                          redisseminators = " ";
                      }
                      stationname = redisseminators;
                  } else{
                      stationname = gd.getString(i, "stationname");
                  }

                  String type_id = gd.getString(i, "type_id");
                  String headcode = gd.getString(i, "headcode");
                  String shortname = gd.getString(i, "shortname");
                  if(service_area == null || service_area.equals("")){
                      throw new Exception(shortname+"的服务区为空，请先设置");
                  }
                  if("102".equals(type_id) && ( headcode.endsWith("A") || headcode.endsWith("B")||headcode.endsWith("C") || headcode.endsWith("D")|| headcode.endsWith("E") || headcode.endsWith("F") || headcode.endsWith("G")  )){
                      headcode = headcode.substring(0, headcode.length()-1);
                      // shortname = shortname.substring(0, shortname.length()-1);
                  }
                  if("102".equals(type_id) && ( shortname.endsWith("A") || shortname.endsWith("B")||shortname.endsWith("C") || shortname.endsWith("D") || shortname.endsWith("E")|| shortname.endsWith("F")|| shortname.endsWith("G"))){
                      shortname = shortname.substring(0, shortname.length()-1);
                  }
                  if(headcodes == null || headcodes.equals("") || headcodes.indexOf(headcode+",")>=0){
                  } else{
                      continue;
                  }
                  String counto = gd.getString(i, "counto");
                  int countoNum = Integer.parseInt(counto);
                  String transmit_station_listens = "";//发射台_>=3分_总次数_可听率%  例如：2022_88_123_78,2032_23_423_28
                  if(mapHead.get(shortname) == null){
                      int big3Count = (countoNum>=3?1:0);
                      String needStr = stationname+"_"+big3Count+"_1_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+",";
                      mapHead.put(shortname, needStr);
                  } else{
                      String newHeadValue = "";
                      if((mapHead.get(shortname)).indexOf(stationname+"_")<0){
                          newHeadValue = mapHead.get(shortname)+stationname+"_0_0_0,";
                          mapHead.put(shortname, newHeadValue);
                          newHeadValue = "";
                      }
                      String[] headStationArr = (mapHead.get(shortname)).split(",");
                      for(String headValue:headStationArr){
                          String[] headValueArr = headValue.split("_");
                          if(headValueArr[0].equals(stationname)){
                              int big3Count = (countoNum>=3?1:0)+Integer.parseInt(headValueArr[1]);
                              int totalCount = Integer.parseInt(headValueArr[2])+1;
                              newHeadValue += stationname+"_"+big3Count+"_"+totalCount+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+",";
                          } else{
                              int big3Count = (countoNum>=3?1:0);
                              if((mapHead.get(shortname)).indexOf(stationname+"_")>-1){//如果有这个发射台，此次把数据从新放回去
                                  newHeadValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
                              } else{//如果没有，加入新的
                              }
                          }
//      							 }
                      }
                      mapHead.put(shortname, newHeadValue);
                  }
                  String[] service_areaArr = service_area.split(",");
                  for(int ai=0;ai<service_areaArr.length;ai++){
                      service_area = service_areaArr[ai];
                      if(mapArea.get(service_area) == null){//地区
                          int big3Count = (countoNum>=3?1:0);
                          mapArea.put(service_area, stationname+"_"+big3Count+"_1_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+",");
                      } else{
                          String newHeadValue = "";
                          if((mapArea.get(service_area)).indexOf(stationname+"_")<0){
                              newHeadValue = mapArea.get(service_area)+stationname+"_0_0_0,";
                              mapArea.put(service_area, newHeadValue);
                              newHeadValue = "";
                          }
                          String[] headStationArr = (mapArea.get(service_area)).split(",");
                          for(String headValue:headStationArr){
                              String[] headValueArr = headValue.split("_");
                              //							 if(headValue.indexOf("___")>0){
                              //								 newHeadValue = headValue;
                              //							 } else{
                              if(headValueArr[0].equals(stationname)){
                                  int big3Count = (countoNum>=3?1:0)+Integer.parseInt(headValueArr[1]);
                                  int totalCount = Integer.parseInt(headValueArr[2])+1;
                                  newHeadValue += stationname+"_"+big3Count+"_"+totalCount+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+",";
                              } else{
                                  int big3Count = (countoNum>=3?1:0);
                                  if((mapArea.get(service_area)).indexOf(stationname+"_")>-1){
                                      newHeadValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
                                  } else{
                                  }
                              }
                              //							 }
                          }
                          mapArea.put(service_area, newHeadValue);
                      }
                  }
                  if(mapState.get(state_name) == null){//大洲
                      int big3Count = (countoNum>=3?1:0);
                      mapState.put(state_name, stationname+"_"+big3Count+"_1_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+",");
                  } else{
                      String newHeadValue = "";
                      if((mapState.get(state_name)).indexOf(stationname+"_")<0){
                          newHeadValue = mapState.get(state_name)+stationname+"_0_0_0,";
                          mapState.put(state_name, newHeadValue);
                          newHeadValue = "";
                      }
                      String[] headStationArr = (mapState.get(state_name)).split(",");
                      for(String headValue:headStationArr){
                          String[] headValueArr = headValue.split("_");
//      							 if(headValue.indexOf("___")>0){
//      								 newHeadValue = headValue;
//      							 } else{
                          if(headValueArr[0].equals(stationname)){
                              int big3Count = (countoNum>=3?1:0)+Integer.parseInt(headValueArr[1]);
                              int totalCount = Integer.parseInt(headValueArr[2])+1;
                              newHeadValue += stationname+"_"+big3Count+"_"+totalCount+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+",";
                          } else{
                              int big3Count = (countoNum>=3?1:0);
                              if((mapState.get(state_name)).indexOf(stationname+"_")>-1){
                                  newHeadValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
                              } else{
                              }
                          }
                      }
//      						 }
                      mapState.put(state_name, newHeadValue);
                  }

                  if(mapTotal.get("全球") == null){//全部
                      int big3Count = (countoNum>=3?1:0);
                      mapTotal.put("全球", stationname+"_"+big3Count+"_1_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+",");
                  } else{
                      String newHeadValue = "";
                      if((mapTotal.get("全球")).indexOf(stationname+"_")<0){
                          newHeadValue = mapTotal.get("全球")+stationname+"_0_0_0,";
                          mapTotal.put("全球", newHeadValue);
                          newHeadValue = "";
                      }
                      String[] headStationArr = (mapTotal.get("全球")).split(",");
                      for(String headValue:headStationArr){
                          String[] headValueArr = headValue.split("_");
//      							 if(headValue.indexOf("___")>0){
//      								 newHeadValue = headValue;
//      							 } else{
                          if(headValueArr[0].equals(stationname)){
                              int big3Count = (countoNum>=3?1:0)+Integer.parseInt(headValueArr[1]);
                              int totalCount = Integer.parseInt(headValueArr[2])+1;
                              newHeadValue += stationname+"_"+big3Count+"_"+totalCount+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+",";
                          } else{
                              int big3Count = (countoNum>=3?1:0);
                              if((mapTotal.get("全球")).indexOf(stationname+"_")>-1){
                                  newHeadValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
                              } else{
                              }
                          }
//      							 }
                      }
                      mapTotal.put("全球", newHeadValue);
                  }
              }
        }
      
        //发射台_>=3分_总次数_可听率%  例如：2022_88_123_78,2032_23_423_28
        for(Object o:mapHead.keySet()){
            String val = mapHead.get(o);
            ZrstRepEffectStatisticsBean  bean = new ZrstRepEffectStatisticsBean();
            bean.setReceive_name(o.toString());
            bean.setTransmit_station_listens(val.substring(0, val.length()-1));
            bean.setSub_report_type("21");
            bean.setReport_type(my_report_type);
            bean.setReport_id(report_id);
            map.put((String)o, bean);

        }
        for(Object o:mapArea.keySet()){
            String val = mapArea.get(o);
            ZrstRepEffectStatisticsBean  bean = new ZrstRepEffectStatisticsBean();
            bean.setService_area(o.toString());
            bean.setTransmit_station_listens(val.substring(0, val.length()-1));
            bean.setSub_report_type("21");
            bean.setReport_type(my_report_type);
            bean.setReport_id(report_id);
            map.put((String)o, bean);
        }
        for(Object o:mapState.keySet()){
            String val = mapState.get(o);
            ZrstRepEffectStatisticsBean  bean = new ZrstRepEffectStatisticsBean();
            bean.setState_name(o.toString());
            bean.setTransmit_station_listens(val.substring(0, val.length()-1));
            bean.setSub_report_type("21");
            bean.setReport_type(my_report_type);
            bean.setReport_id(report_id);
            map.put((String)o, bean);
        }
        for(Object o:mapTotal.keySet()){
            String val = mapTotal.get(o);
            ZrstRepEffectStatisticsBean  bean = new ZrstRepEffectStatisticsBean();
            bean.setTransmit_station_listens(val.substring(0, val.length()-1));
            bean.setSub_report_type("21");
            bean.setReport_type(my_report_type);
            bean.setReport_id(report_id);
            map.put((String)o, bean);
        }
        return map;
    }

    /**
     * 发射台总体播出效果统计2
     * @detail
     * @method
     * @param starttime
     * @param endtime
     * @param my_report_type
     * @param report_type
     * @param asobj
     * @return
     * @throws Exception
     * @return  HashMap
     * @author  zhaoyahui
     * @version 2013-1-10 下午07:32:41
     */
    public LinkedHashMap statisticsReportCondition22(String start, String end,String my_report_type,String report_type,ASObject asobj,String report_id) throws Exception{
        LinkedHashMap<String,ZrstRepEffectStatisticsBean> map = new LinkedHashMap<String,ZrstRepEffectStatisticsBean>();
        String headcodes = (String)asobj.get("headcodes");
        LinkedHashMap<String,String> mapStation = new LinkedHashMap<String,String>();
        LinkedHashMap<String,String> mapState = new LinkedHashMap<String,String>();
        LinkedHashMap<String,String> mapArea = new LinkedHashMap<String,String>();
        LinkedHashMap<String,String> mapTotal = new LinkedHashMap<String,String>();
        String allHead = "";
        String allArea = "";
        String allState = "";
        List<KeyValueForDate> list = getKeyValueForDate(start,end);
        for(int key=0;key<list.size();key++){
        	KeyValueForDate keyValue =list.get(key);
        	 
            String sql  = "";
            String order="";
            if(my_report_type.equals("1")){//国际台
                order=" order by head.code,stationname ";
                sql = "select  " +
                        "  head.code  headcode,head.service_name service_name_headend,head.type_id, mark.headname as shortname,mark.counto,runplan.service_area,state.state_name,station.name stationname,runplan.redisseminators" +
                        " from radio_mark_view_xiaohei mark," +
                        //" radio_stream_result_tab record," +
                        " zres_runplan_chaifen_tab runplan," +
                        " res_headend_tab head," +
                        " res_transmit_station_tab station," +
                        " dic_state_tab            state" +
                        " where mark.runplan_id=runplan.runplan_id and mark.counto is not null and mark.counti is not null and mark.counts is not null " +
                        " and runplan.station_id=station.station_id and head.is_delete =0  and head.code=mark.head_code" +
                        " and mark.report_type=1 " +//效果录音
                        //" and record.is_delete=0 " +
                        //" and runplan.is_delete=0 " +
                        " and runplan.valid_end_time>=to_date('"+keyValue.getEndDate()+"','yyyy-mm-dd hh24:mi:ss') "+

                        " and runplan.runplan_type_id='"+my_report_type+"'"+
                        " and state.state = head.state" +
                        " and mark.start_datetime>=to_date('"+keyValue.getStartDate()+"','yyyy-mm-dd hh24:mi:ss')" +
                        " and mark.end_datetime<=to_date('"+keyValue.getEndDate()+"','yyyy-mm-dd hh24:mi:ss') ";
            } else if(my_report_type.equals("2")){//海外落地
                order=" order by head.code,redisseminators ";
                sql = "select  " +
                        "  head.code  headcode,head.service_name service_name_headend,head.type_id, mark.headname as shortname,mark.counto,runplan.service_area,state.state_name,runplan.redisseminators" +
                        " from radio_mark_view_xiaohei mark," +
                        //" radio_stream_result_tab record," +
                        " zres_runplan_chaifen_tab runplan," +
                        " res_headend_tab head," +
                        " dic_state_tab            state" +
                        " where mark.runplan_id=runplan.runplan_id and mark.counto is not null and mark.counti is not null and mark.counts is not null " +
                        " and head.code=mark.head_code and head.is_delete =0  " +
                        " and mark.report_type=1 " +//效果录音
                        //" and runplan.is_delete=0 " +
                        //" and runplan.valid_end_time>=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss') "+

                        //	" and record.is_delete=0 " +
                        " and runplan.runplan_type_id='"+my_report_type+"'"+
                        " and state.state = head.state" +
                        " and mark.start_datetime>=to_date('"+keyValue.getStartDate()+"','yyyy-mm-dd hh24:mi:ss')" +
                        " and mark.end_datetime<=to_date('"+keyValue.getEndDate()+"','yyyy-mm-dd hh24:mi:ss') ";
            }
            if(headcodes!=null&&!headcodes.equalsIgnoreCase("")){
                //	sql+=" and '"+headcodes.substring(0, headcodes.length()-1)+"' like '%'||head.code||'%' ";
                if(headcodes.split(",").length>1){
                    String[] ss = headcodes.split(",");
                    String newsql="";
                    for(int m=0;m<ss.length;m++){
                        newsql+=" head.code like '%"+ss[m]+"%' or";
                    }
                    sql+=" and ("+newsql.substring(0, newsql.length()-2)+")";
                }else{
                    //sql+="and '"+headcodes.substring(0, headcodes.length()-1)+"' like '%'||head.code||'%' ";
                    sql+=" and head.code like '%"+headcodes.substring(0, headcodes.length()-1)+"%'";
                }

            }
            sql+=order;
            GDSet gd = DbComponent.Query(sql);
            for(int i=0;i<gd.getRowCount();i++){
                String type_id = gd.getString(i, "type_id");
                String headcode = gd.getString(i, "headcode");
                String shortname = gd.getString(i, "shortname");
                String service_area = gd.getString(i, "service_name_headend");
                if(service_area == null || service_area.equals("")){
                    throw new Exception(shortname+"的服务区为空，请先设置");
                }
                String state_name = gd.getString(i, "state_name");
                if("102".equals(type_id) && ( headcode.endsWith("A") || headcode.endsWith("B")||headcode.endsWith("C") || headcode.endsWith("D")|| headcode.endsWith("E") || headcode.endsWith("F") || headcode.endsWith("G")  )){
                    headcode = headcode.substring(0, headcode.length()-1);
                    // shortname = shortname.substring(0, shortname.length()-1);
                }
                if("102".equals(type_id) && ( shortname.endsWith("A") || shortname.endsWith("B")||shortname.endsWith("C") || shortname.endsWith("D") || shortname.endsWith("E")|| shortname.endsWith("F")|| shortname.endsWith("G"))){
                    shortname = shortname.substring(0, shortname.length()-1);
                }
                //收测站点_可听率_大于3分次数_总次数
                if(allHead.indexOf(shortname+"_A_A_A,")<0){
                    allHead += shortname+"_A_A_A,";
                }
                if(allArea.indexOf(","+service_area+"_A_A_A,")<0){
                    allArea += service_area+"_A_A_A,";
                }

                if(allState.indexOf(state_name+"_A_A_A,")<0){
                    allState += state_name+"_A_A_A,";
                }
            }

            for(int i=0;i<gd.getRowCount();i++){
                String service_area = gd.getString(i, "service_name_headend");
                String state_name = gd.getString(i, "state_name");
                String stationname = "";

                if(my_report_type.equals("2")){
                    String redisseminators = gd.getString(i, "redisseminators");//转播机构
                    if(redisseminators.equals("")){
                        redisseminators = " ";
                    }
                    stationname = redisseminators;
                } else{
                    stationname = gd.getString(i, "stationname");
                }

                String type_id = gd.getString(i, "type_id");
                String headcode = gd.getString(i, "headcode");
                String shortname = gd.getString(i, "shortname");
                if("102".equals(type_id) && ( headcode.endsWith("A") || headcode.endsWith("B")||headcode.endsWith("C") || headcode.endsWith("D")|| headcode.endsWith("E") || headcode.endsWith("F") || headcode.endsWith("G")  )){
                    headcode = headcode.substring(0, headcode.length()-1);
                    // shortname = shortname.substring(0, shortname.length()-1);
                }
                if("102".equals(type_id) && ( shortname.endsWith("A") || shortname.endsWith("B")||shortname.endsWith("C") || shortname.endsWith("D") || shortname.endsWith("E")|| shortname.endsWith("F")|| shortname.endsWith("G"))){
                    shortname = shortname.substring(0, shortname.length()-1);
                }
                if(headcodes == null || headcodes.equals("") || headcodes.indexOf(headcode+",")>=0){
                } else{
                    continue;
                }
                String counto = gd.getString(i, "counto");
                int countoNum = Integer.parseInt(counto);
//    					 String needStr = stationname+"_"+big3Count+"_1_"+(big3Count*100/1)+",";
                if(mapStation.get(stationname) == null){
                    String needStr = allHead;
                    mapStation.put(stationname, needStr);
                } else{

                }
                //收测站点_可听率_大于3分次数_总次数
                String newHeadendValue = "";
                String[] headendStationArr = (mapStation.get(stationname)).split(",");
                for(String headValue:headendStationArr){
                    String[] headValueArr = headValue.split("_");
                    if(headValueArr.length>2)
                    {
    	                int big3Count = (countoNum>=3?1:0)+ Integer.parseInt(  (headValueArr[2].equals("A")?"0":headValueArr[2]) );
    	                int totalCount = 1 + Integer.parseInt(  (headValueArr[3].equals("A")?"0":headValueArr[3]) );
    	                if(headValueArr[0].equals(shortname)){
    	                    newHeadendValue += shortname+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+"_"+big3Count+"_"+totalCount+",";
    	                } else{
    	                    if((mapStation.get(stationname)).indexOf(shortname+"_")>-1){
    	                        newHeadendValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
    	                    } else{
    	                        newHeadendValue += shortname+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+"_"+big3Count+"_1,";
    	
    	                    }
    	                }
                    }
                }
                mapStation.put(stationname, newHeadendValue);

                //计算大洲的收听次数
                if(mapState.get(stationname) == null){
                    String needStr = allState;
                    mapState.put(stationname, needStr);
                } else{
                }
                String newSatateValue = "";
                String[] stateStationArr = (mapState.get(stationname)).split(",");
                for(String headValue:stateStationArr){
                    String[] headValueArr = headValue.split("_");
                    if(headValueArr.length>2)
                    {
    	                int big3Count = (countoNum>=3?1:0)+ Integer.parseInt(  (headValueArr[2].equals("A")?"0":headValueArr[2]) );
    	                int totalCount = 1 + Integer.parseInt(  (headValueArr[3].equals("A")?"0":headValueArr[3]) );
    	                if(headValueArr[0].equals(state_name)){
    	                    newSatateValue += state_name+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+"_"+big3Count+"_"+totalCount+",";
    	                } else{
    	                    if((mapState.get(stationname)).indexOf(state_name+"_")>-1){
    	                        newSatateValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
    	                    } else{
    	                        newSatateValue += state_name+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+"_"+big3Count+"_1,";
    	
    	                    }
    	                }
                    }
                }
                mapState.put(stationname, newSatateValue);
                //计算地区的收听次数
                String[] service_areaArr = service_area.split(",");
                for(int ai=0;ai<service_areaArr.length;ai++){
                    service_area = service_areaArr[ai];
                    if(mapArea.get(stationname) == null){
                        String needStr = allArea;

                        mapArea.put(stationname, needStr);
                    } else{
                    }
                    String newAreaValue = "";
                    String[] areaStationArr = (mapArea.get(stationname)).split(",");
                    for(String headValue:areaStationArr){
                        String[] headValueArr = headValue.split("_");
                        if(headValueArr.length>2)
                        {
    	                    int big3Count = (countoNum>=3?1:0)+ Integer.parseInt(  (headValueArr[2].equals("A")?"0":headValueArr[2]) );
    	                    int totalCount = 1 + Integer.parseInt(  (headValueArr[3].equals("A")?"0":headValueArr[3]) );
    	                    if(headValueArr[0].equals(service_area)){
    	                        newAreaValue += service_area+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/totalCount)).setScale(1,BigDecimal.ROUND_HALF_UP)+"_"+big3Count+"_"+totalCount+",";
    	                    } else{
    	                        if((mapArea.get(stationname)).indexOf(service_area+"_")>-1){
    	                            newAreaValue += headValueArr[0]+"_"+headValueArr[1]+"_"+headValueArr[2]+"_"+headValueArr[3]+",";
    	                        } else{
    	                            newAreaValue += service_area+"_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+"_"+big3Count+"_1,";
    	
    	                        }
    	                    }
                        }
                    }

                    mapArea.put(stationname, newAreaValue);
                }
                //计算全部的收听次数
                int big3Count = (countoNum>=3?1:0);
                if(mapTotal.get(stationname) == null){
                    String needStr = "全球_"+new BigDecimal((Double.parseDouble(big3Count+"")*100/1)).setScale(1,BigDecimal.ROUND_HALF_UP)+"_"+big3Count+"_1";
                    mapTotal.put(stationname, needStr);
                } else{
                    int big3CountT = (countoNum>=3?1:0)+ Integer.parseInt(mapTotal.get(stationname).split("_")[2]);
                    int totalCountT = 1 + Integer.parseInt(mapTotal.get(stationname).split("_")[3]);

                    mapTotal.put(stationname, "全球_"+new BigDecimal((Double.parseDouble(big3CountT+"")*100/totalCountT)).setScale(1,BigDecimal.ROUND_HALF_UP)+"_"+big3CountT+"_"+totalCountT);
                }


            }
        }
        boolean temp = false;
        for(String o:mapStation.keySet()){
            String val = mapStation.get(o);
            ZrstRepEffectStatisticsBean  bean = new ZrstRepEffectStatisticsBean();
            bean.setTransmit_station(o.toString());
            String traVal = val;
            traVal +=  mapArea.get(o);
            traVal +=  mapState.get(o);
            traVal +=  mapTotal.get(o);
            bean.setReceive_name_total_hours(traVal);
            bean.setSub_report_type("22");
            bean.setReport_type(my_report_type);
            bean.setReport_id(report_id);
            map.put((String)o, bean);
            temp = true;
        }
        return map;
    }
    /**
     * type: 1、按播音时间统计 2、按半小时统计 3、按一小时统计
     * recordTime 录音文件开始时间
     * @detail
     * @method
     * @param type
     * @return
     * @return  String
     * @author  zhaoyahui
     * @version 2013-6-3 上午10:37:00
     */
    public String getPlayTime(String type,String recordTime){
        String play_time = "";
        String hour = recordTime.substring(11,13);
        if(type.equals("2")){
            String minute = recordTime.substring(14,16);
            int min = Integer.parseInt(minute);
            if(min<30){
                play_time = hour + ":00-"+hour+":30";
            } else{
                if(hour.equals("23")){
                    play_time = "23:30-00:00";
                } else{
                    int otherHour = Integer.parseInt(hour)+1;
                    String otherHourStr = otherHour+"";
                    if(otherHour<10){
                        otherHourStr = "0"+otherHour;
                    }
                    play_time = hour + ":30-"+otherHourStr+":00";
                }
            }
        } else if(type.equals("3")){
            if(hour.equals("23")){
                play_time = "23:00-00:00";
            } else{
                int otherHour = Integer.parseInt(hour)+1;
                String otherHourStr = otherHour+"";
                if(otherHour<10){
                    otherHourStr = "0"+otherHour;
                }
                play_time = hour + ":00-"+otherHourStr+":00";
            }
        }
        return play_time;
    }
    

    public static void main(String[] args) throws Exception {

        String str = "2014-09-01 00:00:00";
        String end = "2015-09-30 23:59:59";
        List<KeyValueForDate> list = getKeyValueForDate(str,end);
        System.out.println("开始日期--------------结束日期");
        for(KeyValueForDate date : list){
            System.out.println(date.getStartDate()+"-----"+date.getEndDate());
        }
    }
    public static List<KeyValueForDate> getKeyValueForDate(String startDate,String endDate) {
    	 
        List<KeyValueForDate> list = new ArrayList<KeyValueForDate>();
        KeyValueForDate keyValue =new KeyValueForDate();
        keyValue.setStartDate(startDate);
        keyValue.setEndDate(endDate);
        list.add(keyValue);
        return list ;
//        List<KeyValueForDate> list =null;
//        try {
//            list = new ArrayList<KeyValueForDate>();
//            Date start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDate);// 定义起始日期
//            Date end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDate);// 定义结束日期
//            
//            Date monthSD = new SimpleDateFormat("yyyy-MM").parse(startDate);
//            Date monthEd = new SimpleDateFormat("yyyy-MM").parse(endDate);
//            Calendar sc = Calendar.getInstance();//定义日期实例
//            sc.setTime(monthSD);//设置日期起始时间
//            KeyValueForDate keyValue =null;
//            while(sc.getTime().before(end)){//判断是否到结束日期
//            	keyValue =new KeyValueForDate();
//            	SimpleDateFormat desc = new SimpleDateFormat("yyyy-MM-dd");
//            	  Calendar calendar = Calendar.getInstance();   
//            	  calendar.setTime(sc.getTime());
//            	    
//            	if(sc.getTime().equals(start)||sc.getTime().before(start))
//            	{
//            		keyValue.setStartDate(startDate);
//            		 calendar.set(Calendar.DAY_OF_MONTH, calendar     
//             	            .getActualMaximum(Calendar.DAY_OF_MONTH)); 
//            		 if(calendar.getTime().after(end))
//            		 {
//            			 keyValue.setEndDate(endDate); 
//            		 }else {
//            		   keyValue.setEndDate(desc.format(calendar.getTime())+" 23:59:59"); 
//            		 }
//           
//            	}else{
//            		 calendar.set(Calendar.DAY_OF_MONTH, calendar     
//             	            .getActualMinimum(Calendar.DAY_OF_MONTH)); 
//            		keyValue.setStartDate(desc.format(calendar.getTime())+" 00:00:00");
//            		 calendar.set(Calendar.DAY_OF_MONTH, calendar     
//              	            .getActualMaximum(Calendar.DAY_OF_MONTH));
//            		 if(calendar.getTime().after(end))
//            		 {
//            			 keyValue.setEndDate(endDate); 
//            		 }else{
//            		   keyValue.setEndDate(desc.format(calendar.getTime())+" 23:59:59");
//            		 }
//            	}
//            	sc.add(Calendar.MONTH, 1);//进行当前日期月份加1
//            	list.add(keyValue);
//
//            }
//        } catch (ParseException e) {
//            return null;
//        }
//
//        return list;
    }


}
