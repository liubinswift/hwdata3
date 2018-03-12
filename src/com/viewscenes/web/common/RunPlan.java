package com.viewscenes.web.common;

import java.util.Vector;

import com.viewscenes.pub.*;
import com.viewscenes.util.StringTool;
import com.viewscenes.dao.DAOCondition;
import java.util.Calendar;
import com.viewscenes.web.Daoutil.SiteVersionUtil;
import com.viewscenes.web.Daoutil.SystemIdUtil;
import com.viewscenes.dao.DAOOperator;
import com.viewscenes.dao.DaoFactory;
import com.viewscenes.web.Daoutil.SiteRunplanUtil;



/**
 * <p>Title: ����ͼ����</p>
 * <p>Description: ����ͼ������ع��ܲ�ѯ</p>
 * <p>Copyright: Copyright (c) 2012</p>
 * <p>Company</p>
 * @author 
 * @version 1.0
 */

public class RunPlan {

  public RunPlan() {
  }

  /**
   * ��ȡ��Ŀʱ��
   * @param stationID ����̨���
   * @param checkDate �������
   * @param runplanType ����ͼ����
   * @param programType ��Ŀ����
   * @return 0:��Ŀʱ��,1:��ĿƵ����
   */

  public static long[] getProgramLengths(String stationID, String checkDate,
                                         String runplanType, String programType) {
    long returnValue[] = {
        0, 0};

    Vector tsgVector = RunPlan.getTimeSpanGroups("", stationID, checkDate, false,
                                                 "", "", runplanType,
                                                 programType);
    TimeSpanGroup tsitem;
    String frequency = "";
    for (int i = 0; i < tsgVector.size(); i++) {
      tsitem = (TimeSpanGroup) tsgVector.get(i);
      returnValue[0] += tsitem.getTotalInterval(1);
      if (!frequency.equalsIgnoreCase(tsitem.getRunPlanInfo().getFrequency())) {
        frequency = tsitem.getRunPlanInfo().getFrequency();
        returnValue[1]++;
      }
    }
    return returnValue;

  }

  public static long getProgramLength(String headendCode, String stationID,
                                      String frequency, String startDate,
                                      String endDate) {
    long returnValue = 0;
    long startTime, endTime;
    startTime = StringTool.stringToDate(startDate).getTime() / 1000;
    endTime = StringTool.stringToDate(endDate).getTime() / 1000;
    if (endTime > startTime) {
      returnValue = endTime - startTime;

      long programLength = 0;
      String startDay, checkDay, endDay;
      long checkTime;
      Vector tsgVector;
      TimeSpanGroup tsitem;
      TimeSpan beginTimeSpan = null;
      TimeSpan endTimeSpan = null;
      int i, j;

      if (!startDate.substring(11).equalsIgnoreCase("00:00:00")) {
        beginTimeSpan = new TimeSpan("00:00:00", startDate.substring(11), "2");
        //if(!endDate.substring(11).equalsIgnoreCase("00:00:00"))
      }
      endTimeSpan = new TimeSpan(endDate.substring(11), "00:00:00", "2");

      startDay = startDate.substring(0, 10) + " 00:00:00";
      endDay = endDate.substring(0, 10) + " 00:00:00";
      checkDay = startDay;
      while (checkDay.compareTo(endDay) <= 0) {
        tsgVector = RunPlan.getTimeSpanGroups(headendCode, stationID, checkDay, false,
                                              frequency, "", "", "");
        for (i = 0; i < tsgVector.size(); i++) {
          tsitem = (TimeSpanGroup) tsgVector.get(i);
          if (checkDay.equalsIgnoreCase(startDay) && beginTimeSpan != null) {
            tsitem.add(beginTimeSpan);
          }
          if (checkDay.equalsIgnoreCase(endDay) && endTimeSpan != null) {
            tsitem.add(endTimeSpan);
          }
          programLength += tsitem.getTotalInterval(1);
        }
        //System.out.print("\n "+tsgVector.toString()+"\nProgamLength:"+programLength+"\n");
        checkTime = StringTool.stringToDate(checkDay).getTime();
        checkDay = StringTool.Date2String(new java.util.Date(checkTime +
            24 * 3600 * 1000));
      }
      if (programLength > 0 && programLength < returnValue) {
        returnValue = programLength;
      }
    }
    return returnValue;
  }

  /**
   * ������Ч������
   * @param timeSpan
   * @param rvStartTime
   * @param rvEndTime
   * @param startInMiddle
   * @param endInMiddle
   * @return
   */
  private static TimeSpanGroup processMiddleHour(TimeSpan timeSpan,
                                                 long rvStartTime,
                                                 long rvEndTime,
                                                 boolean startInMiddle,
                                                 boolean endInMiddle) {
    TimeSpanGroup returnValue = new TimeSpanGroup();

    long type = timeSpan.getType();
    TimeSpan validItem = new TimeSpan();

    //delete through dayitem if valid start from today
    if (timeSpan.getStartTime() >= timeSpan.getEndTime() && startInMiddle) {
      timeSpan.setEndTime(0);
    }
    returnValue.add(timeSpan);

    //trim start
    if (startInMiddle) {
      validItem.setStartTime(0);
      validItem.setEndTime(rvStartTime);
      validItem.setProgramInfo(timeSpan.getProgramInfo());
      if (type == 1) {
        validItem.setType(2);
        returnValue.add(validItem);
      }
      else {
        validItem.setType(1);
        returnValue.add(validItem, true);
      }
    }
    //trim end
    if (endInMiddle) {
      validItem.setStartTime(rvEndTime);
      validItem.setEndTime(0);
      validItem.setProgramInfo(timeSpan.getProgramInfo());
      if (type == 1) {
        validItem.setType(2);
        returnValue.add(validItem);
      }
      else {
        validItem.setType(1);
        returnValue.add(validItem, true);
      }
    }
    int i = 0;
    while (i < returnValue.getTimeSpanNum(0)) {
      if (returnValue.get(i).getType() != type) {
        returnValue.remove(i);
      }
      else {
        i++;
      }
    }
    return returnValue;
  }

  /**
   * ��ȡ����ͼ��ѯ����
   * @param headCode
   * @param stationID
   * @param checkTime
   * @param matchTime
   * @param frequency
   * @param band
   * @param runPlanType
   * @param programType
   * @return
   */
  public static DAOCondition getRunPlanCondition(String headCode,
                                                 String stationID,
                                                 String checkTime,
                                                 boolean matchTime,
                                                 boolean matchWeek,
                                                 String frequency, String band,
                                                 String runPlanType,
                                                 String programType) {
    DAOCondition returnValue = null;
    try {
      if (headCode == null) {
        headCode = "";
      }
      if (stationID == null) {
        stationID = "";
      }
      if (checkTime == null) {
        checkTime = "";
      }
      if (frequency == null) {
        frequency = "";
      }
      if (band == null) {
        band = "";
      }
      if (runPlanType == null) {
        runPlanType = "";
      }
      if (programType == null) {
        programType = "";

      }
      returnValue = new DAOCondition("zres_runplan_gb_view");
      returnValue.addCondition("is_delete", "NUMBER", "=", "0");
      returnValue.addCondition("is_predefine", "NUMBER", "=", "0");
      returnValue.addCondition("is_confirm", "NUMBER", "=", "1");
      returnValue.addCondition("is_special", "NUMBER", "=", "0");
      returnValue.addCondition("timespan_type_id", "NUMBER", "=", "1");
//      returnValue.addCondition("broadcast_type_id", "NUMBER", "=", "3");

      DAOCondition scondition;

      if (checkTime.length() > 0) {
        Calendar nowCal = Calendar.getInstance();
        nowCal.setTime(StringTool.stringToDate(checkTime));
        String weekday = new Integer(nowCal.get(Calendar.DAY_OF_WEEK) - 1).
            toString();
        int prvWeek = nowCal.get(Calendar.DAY_OF_WEEK) - 2;
        if (prvWeek < 0) {
          prvWeek = 6;
        }
        String prvweekday = new Integer(prvWeek).toString();
        String beginDate = checkTime.substring(0, 10) + " 00:00:00";
        String endDate = checkTime.substring(0, 10) + " 23:59:59";

        if (matchTime) { //��ȡ�ƶ�ʱ�̵�
          //��Ч��
          returnValue.addCondition("valid_startdatetime", "DATE", "<=",
                                   checkTime);
          returnValue.addCondition("valid_enddatetime", "DATE", ">", checkTime);
          //�����ʱ���в���������ͼ�У�����ʱ�䡢ͣ��ʱ���Ѿ�ת��������ʽ��
          String checkTime1 = "2000-01-01 " + checkTime.substring(11);
          String checkTime2 = "2000-01-02 " + checkTime.substring(11);
          DAOCondition scon = new DAOCondition("zres_runplan_gb_view");

          DAOCondition sscon = new DAOCondition("zres_runplan_gb_view");
          sscon.addCondition("start_time", "DATE", "<=", checkTime1, "and");
          sscon.addCondition("end_time", "DATE", ">=", checkTime1, "and");
          scon.addCondition("", "", "()", sscon.toString(), "or");

          sscon = new DAOCondition("zres_runplan_gb_view");
          sscon.addCondition("start_time", "DATE", "<=", checkTime2, "and");
          sscon.addCondition("end_time", "DATE", ">=", checkTime2, "and");
          scon.addCondition("", "", "()", sscon.toString(), "or");

          returnValue.addCondition("", "", "()", scon.toString());
        }
        else { //��ȡ�ƶ����ڵ�
          returnValue.addCondition("valid_startdatetime", "DATE", "<=", endDate);
          returnValue.addCondition("valid_enddatetime", "DATE", ">", beginDate);
        }

        if (matchWeek) {
          //��ѯ����Ч��
          scondition = new DAOCondition("zres_runplan_gb_view");
          scondition.addCondition("weekdays", "VARCHAR", "=", "all", "or");
          scondition.addCondition("weekdays", "VARCHAR", "=", weekday, "or");
          scondition.addCondition("weekdays", "VARCHAR", "=", prvweekday, "or");
          returnValue.addCondition("", "", "()", scondition.toString());
        }
      }

      if (frequency.length() > 0) {
        returnValue.addCondition("frequency", "NUMBER", "=", frequency);
      }
      if (band.length() > 0) {
        returnValue.addCondition("band", "NUMBER", "=", band);
      }
      //ƥ������ͼ����
      if (runPlanType.length() > 0) {
        scondition = new DAOCondition("zres_runplan_gb_view");
        String[] runplanArray = runPlanType.split(",");
        for (int i = 0; i < runplanArray.length; i++) {
          if (runplanArray[i].trim().length() > 0) {
            scondition.addCondition("runplan_type_id", "NUMBER", "=",
                                    runplanArray[i].trim(), "or");
          }
        }
        returnValue.addCondition("", "", "()", scondition.toString());
      }
      //ƥ���Ŀ����
      if (programType.length() > 0) {
        scondition = new DAOCondition("zres_runplan_gb_view");
        String[] programArray = programType.split(",");
        for (int i = 0; i < programArray.length; i++) {
          if (programArray[i].trim().length() > 0) {
            scondition.addCondition("program_type_id", "NUMBER", "=",
                                    programArray[i].trim(), "or");
          }
        }
        returnValue.addCondition("", "", "()", scondition.toString());
      }

      if (stationID.length() > 0) { //ƥ�䷢��̨
        returnValue.addCondition("station_id", "NUMBER", "=", stationID);
      }
      else if (headCode.length() > 0) { //ƥ��ǰ�˵�
        //ͳһ�߼���ң��վ��mon_area�����ɼ��㣨����̨������lixuefeng2005-12-30
        scondition = new DAOCondition("zres_runplan_gb_view");
        String siteType = SiteVersionUtil.getSiteType(headCode);
        String headendID = SiteVersionUtil.getSiteHeadId(headCode);
        if (siteType.equalsIgnoreCase("101")) {
          //������
          DAOCondition s11condition = new DAOCondition(
              "res_monitor_station_tab");
          s11condition.addCondition("head_id", "NUMBER", "=", headendID);
          DAOCondition s12condition = new DAOCondition(
              "res_transmit_station_tab");
          s12condition.addCondition("is_delete", "NUMBER", "=", "0");
          s12condition.addCondition("station_id", "NUMBER", "in",
                                    s11condition.toString());

          scondition.addCondition("station_id", "NUMBER", "in",
                                  s12condition.toString());

          returnValue.addCondition("", "", "()", scondition.toString());
        }
        else {
          //ң��վ,���ĸ��ݲ�ֱ���������mon_area�ֶΡ�
          if (SystemIdUtil.getLocalCenterId().equalsIgnoreCase("100")) {
            //������
            DAOCondition s11condition = new DAOCondition(
                "zres_runplan_station_view");
            s11condition.addCondition("effect_ids", "VARCHAR", "like",
                                      "%" + headendID + "%");
            scondition.addCondition("runplan_id", "NUMBER", "in",
                                    s11condition.toString());
            returnValue.addCondition("", "", "()", scondition.toString());
          }
          else {
            String headName = SiteVersionUtil.getSiteName(headCode);
            scondition.addCondition("mon_area", "VARCHAR", "like",
                                    "%" + headName + "%");
            returnValue.addCondition("", "", "()", scondition.toString());
          }
        }
      }

    }

    catch (Exception e) {
      e.printStackTrace();
    }

    return returnValue;
  }

  /**
   * ����ָ��ǰ��,�ƶ�ʱ��,�ƶ�Ƶ�ʵĽ�Ŀ��Ϣ
   * @param headCode
   * @param checkTime
   * @param frequency �����򷵻ظ�ʱ�����пɼ��Ƶ�ʵ���Ϣ
   * @return
   */

  public static Vector getProgramInfos(String headCode, String checkTime,
                                       String frequency) {
    return getProgramInfos(headCode, "", checkTime, true, frequency, "", "", "");
  }

  /**
   * ����ƥ��Ľ�Ŀ��Ϣ
   * @param headCode ǰ�˴���
   * @param checkTime ���ʱ��
   * @param frequency Ƶ��
   * @return
   */
  public static Vector getProgramInfos(String headCode, String StationID,
                                       String checkTime, boolean matchTime,
                                       String frequency, String band,
                                       String runplanType, String programType) {
    Vector returnValue = new Vector();
    Vector tsVector = getTimeSpanGroups(headCode, StationID, checkTime,
                                        matchTime, frequency, band, runplanType,
                                        programType);
    TimeSpanGroup tsGroup;
    TimeSpan timeSpan;
    long alarmTime = TimeSpan.parse(checkTime.substring(11)); //get rid of "yyyy-mm-dd "
    int i, j;
    for (i = 0; i < tsVector.size(); i++) {
      tsGroup = (TimeSpanGroup) tsVector.get(i);
      for (j = 0; j < tsGroup.getTimeSpanNum(0); j++) {
        timeSpan = tsGroup.get(j);
        if (timeSpan.getType() == 1) { //ֻȡ��������,���ƥ��ʱ���򻹼��ʱ��
          if (!matchTime ||
              (timeSpan.getStartTime() < timeSpan.getEndTime() &&
               alarmTime >= timeSpan.getStartTime() &&
               alarmTime < timeSpan.getEndTime())
              ||
              (timeSpan.getStartTime() > timeSpan.getEndTime() &&
               (alarmTime >= timeSpan.getStartTime() ||
                alarmTime < timeSpan.getEndTime()))
              ) {
            //System.out.print("\nadd to output "+timeSpan.getProgramInfo().getFrequency());
            returnValue.add(timeSpan.getProgramInfo());
            break;
          }
        }
      }

    }
    return returnValue;
  }

  /**
   * ��ȡָ��������
   * @param headCode
   * @param stationID
   * @param checkTime
   * @param matchTime
   * @param frequency
   * @param band
   * @param runplanType
   * @param programType
   * @return
   */
  public static Vector getTimeSpanGroups(String headCode, String stationID,
                                         String checkTime, boolean matchTime,
                                         String frequency, String band,
                                         String runplanType, String programType) {
    Vector returnValue = new Vector();
    try {
      String beginDate = checkTime.substring(0, 10) + " 00:00:00";
      String endDate = checkTime.substring(0, 10) + " 23:59:59";

      Calendar nowCal = Calendar.getInstance();
      nowCal.setTime(StringTool.stringToDate(checkTime));
      String weekday = new Integer(nowCal.get(Calendar.DAY_OF_WEEK) - 1).
          toString();
      int prvWeek = nowCal.get(Calendar.DAY_OF_WEEK) - 2;
      if (prvWeek < 0) {
        prvWeek = 6;
      }
      String prvweekday = new Integer(prvWeek).toString();

      DAOOperator d = (DAOOperator) DaoFactory.create(DaoFactory.DAO_OBJECT);
      DAOCondition condition = getRunPlanCondition(headCode, stationID,
          checkTime, matchTime, true, frequency, band, runplanType, programType);
      if (condition != null) {
        condition.addCondition(
            "frequency,transmiter_no,is_temporary," +
            "runplan_id,weekdays desc,timespan_type_id,is_broadcast", "",
            "order by", "");
        GDSet result_set = d.Query("*", condition);

        condition = new DAOCondition("res_transmit_station_type_tab");
        GDSet type_set = d.Query("*", condition);

        int i, j;
        //long totalLength = 0;
        TimeSpan timeSpan;
        TimeSpanGroup tsGroup = null;
        TimeSpanGroup validGroup;

        RunPlanInfo runPlanInfo = null;
        ProgramInfo programInfo = null;

        //����ͼ��Ч���Լ�������Ч��ʶ
        String rvStartDate = "";
        String rvEndDate = "";
        boolean startInMiddle = false;
        boolean endInMiddle = false;

        //Ψһ�Ա�־
        String curRunplanID = "";
        String curFrequency = "";
        String curTransmitter = "";

        boolean overWrite = false;
        boolean validSpan = false;
        for (i = 0; i <= result_set.getRowCount(); i++) {
          //��������
          if (i == result_set.getRowCount()) {
            if (i != 0) {
              returnValue.add(tsGroup);
            }
            break;
          }
          //������Դ��Ƶ������ŷ���
          if (!curFrequency.equalsIgnoreCase(result_set.getString(i,
              "frequency")) ||
              !curTransmitter.equalsIgnoreCase(result_set.getString(i,
              "transmiter_no"))) {
            //���֮ǰ�ļ�¼
            if (i > 0) {
              returnValue.add(tsGroup);
              //��¼���б�־
            }
            curFrequency = result_set.getString(i, "frequency");
            curTransmitter = result_set.getString(i, "transmiter_no");

            //�µ���
            tsGroup = new TimeSpanGroup();

            runPlanInfo = new RunPlanInfo();
            //curRunplanID = result_set.getString(i, "runplan_id");
            runPlanInfo.setRunplanID(result_set.getString(i, "runplan_id"));
            runPlanInfo.setRunplanType(result_set.getString(i,
                "runplan_type_id"));
            runPlanInfo.setTypeName(result_set.getString(i, "type"));
            runPlanInfo.setTemporary(result_set.getString(i, "is_temporary"));
            runPlanInfo.setFrequency(result_set.getString(i, "frequency"));
            runPlanInfo.setTransmitter(result_set.getString(i, "transmiter_no"));
            runPlanInfo.setPower(result_set.getString(i, "power"));
            runPlanInfo.setStationID(result_set.getString(i, "station_id"));
            runPlanInfo.setStationName(result_set.getString(i, "station_name"));
            runPlanInfo.setValidstarttime(StringTool.stringToDate(result_set.
                getString(i, "valid_startdatetime")).getTime());
            runPlanInfo.setValidendtime(StringTool.stringToDate(result_set.
                getString(i, "valid_enddatetime")).getTime());

            if (runPlanInfo.getStationID().length() > 0 &&
                type_set.getRowCount() > 0) {
              for (j = 0; j < type_set.getRowCount(); j++) {
                if (type_set.getString(j, "station_id").equalsIgnoreCase(
                    runPlanInfo.getStationID())) {
                  runPlanInfo.setStationType(type_set.getString(j,
                      "station_type_id"));
                  break;
                }
              }
            }
            tsGroup.setRunPlanInfo(runPlanInfo);
          }
          /*
              //��ȡʱ�θ��Ǳ�־,��ʱ�����ճ�,ָ�����ڸ���ÿ��
           if (result_set.getString(i, "is_temporary").equalsIgnoreCase("1")||
              !result_set.getString(i, "weekdays").equalsIgnoreCase("all"))
            overWrite = true;
              else//���ޡ�ͣ�����ǲ���
            overWrite = false;
           */
          //������ͻ,���Ǻ���ĸ���ǰ���,����ʱ�����ճ�,ָ�����ڸ���ÿ��,���ޡ�ͣ�����ǲ���,�����ĸ���δ������
          overWrite = true;

          //runplanID �仯,������Ϣ
          if (!curRunplanID.equalsIgnoreCase(result_set.getString(i,
              "runplan_id"))) {
            curRunplanID = result_set.getString(i, "runplan_id");
            rvStartDate = result_set.getString(i, "valid_startdatetime");
            rvEndDate = result_set.getString(i, "valid_enddatetime");
            //�����Ч���Ƿ�����ѡ�����м�
            if (rvStartDate.compareTo(beginDate) > 0) {
              startInMiddle = true;
            }
            else {
              startInMiddle = false;
            }
            if (rvEndDate.compareTo(endDate) < 0) {
              endInMiddle = true;
            }
            else {
              endInMiddle = false;
            }
            runPlanInfo = new RunPlanInfo();
            runPlanInfo.setRunplanID(result_set.getString(i, "runplan_id"));
            runPlanInfo.setRunplanType(result_set.getString(i,
                "runplan_type_id"));
            runPlanInfo.setTypeName(result_set.getString(i, "type"));
            runPlanInfo.setTemporary(result_set.getString(i, "is_temporary"));
            runPlanInfo.setFrequency(result_set.getString(i, "frequency"));
            runPlanInfo.setTransmitter(result_set.getString(i, "transmiter_no"));
            runPlanInfo.setPower(result_set.getString(i, "power"));
            runPlanInfo.setStationID(result_set.getString(i, "station_id"));
            runPlanInfo.setStationName(result_set.getString(i, "station_name"));
            runPlanInfo.setValidstarttime(StringTool.stringToDate(result_set.
                getString(i, "valid_startdatetime")).getTime());
            runPlanInfo.setValidendtime(StringTool.stringToDate(result_set.
                getString(i, "valid_enddatetime")).getTime());

            if (runPlanInfo.getStationID().length() > 0 &&
                type_set.getRowCount() > 0) {
              for (j = 0; j < type_set.getRowCount(); j++) {
                if (type_set.getString(j,
                                       "station_id").equalsIgnoreCase(
                    runPlanInfo.getStationID())) {
                  runPlanInfo.setStationType(type_set.getString(j,
                      "station_type_id"));
                  break;
                }
              }
            }
          }

          validSpan = true;
          timeSpan = new TimeSpan();
          timeSpan.setType(new Long(result_set.getString(i, "timespan_type_id")).
                           longValue());
          timeSpan.setStartTime(result_set.getString(i, "starttime"));
          timeSpan.setEndTime(result_set.getString(i, "endtime"));

          if (result_set.getString(i, "weekdays").equalsIgnoreCase(prvweekday)) {
            if (timeSpan.getEndTime() <= timeSpan.getStartTime()) {
              timeSpan.setStartTime(0);
            }
            else {
              validSpan = false;
            }
          }
          if (result_set.getString(i, "weekdays").equalsIgnoreCase(weekday)) {
            if (timeSpan.getEndTime() <= timeSpan.getStartTime()) {
              timeSpan.setEndTime(0);
            }
          }

          if (validSpan) {

            programInfo = new ProgramInfo(runPlanInfo);
            programInfo.setProgramType(result_set.getString(i,
                "program_type_id"));
            programInfo.setProgramName(result_set.getString(i, "program_name"));
            programInfo.setRunplanID(result_set.getString(i, "runplan_id"));
            programInfo.setBroadCast(result_set.getString(i, "is_broadcast"));
            programInfo.setCoverObject(result_set.getString(i, "cover_object"));
            programInfo.setServiceArea(result_set.getString(i, "service_area"));
            programInfo.setAntenna(result_set.getString(i, "antenna"));
            programInfo.setAntennaType(result_set.getString(i, "antenna_type"));
            programInfo.setLanguage(result_set.getString(i, "language_name"));
            programInfo.setDirection(result_set.getString(i, "direction"));
            programInfo.setBroadType(result_set.getString(i,
                "broadcast_type_id"));
            programInfo.setWeekDays(result_set.getString(i,
                "weekdays"));
            timeSpan.setProgramInfo(programInfo);

            //��Ч�����м�,�����и�
            if (startInMiddle || endInMiddle) {
              validGroup = processMiddleHour(timeSpan,
                                             TimeSpan.parse(rvStartDate.
                  substring(11)),
                                             TimeSpan.parse(rvEndDate.substring(
                  11)), startInMiddle, endInMiddle);
              for (j = 0; j < validGroup.getTimeSpanNum(0); j++) {
                tsGroup.add(validGroup.get(j), overWrite);
              }
            }
            else {
              tsGroup.add(timeSpan, overWrite);
            }
          }
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }

    return returnValue;
  }

  /**
   * ��ȡ��������ֵ
   * @param headendID ǰ��ID
   * @param equCode ���ջ�����
   * @param frequency Ƶ��
   * @return
   */
  public static GDSet getAlarmInfo(String headendID, String equCode,
                                   String frequency) {
    String band = SiteRunplanUtil.getBandFromFreq(frequency);
    return getAlarmInfo(headendID, equCode, band, frequency, null);
  }

  /**
   * ��ѯ��������ֵ
   * @param headendID ǰ��ID
   * @param equCode   �豸���
   * @param band ����
   * @param frequency Ƶ��
   * @param paramID �ɷ��ض�Ӧ�Ĳ���ID
   * @return
   */
  public static GDSet getAlarmInfo(String headendID, String equCode,
                                   String band, String frequency,
                                   String[] paramID) {
    GDSet result_set = null;
    try {
      long desireMatch = 0;
      if (headendID.length() > 0) {
        if (equCode.length() > 0) {
          if (band.length() > 0) {
            if (frequency.length() > 0) {
              desireMatch = 7;
            }
            else {
              desireMatch = 6;
            }
          }
          else {
            desireMatch = 5;
          }
        }
        else {
          if (band.length() > 0) {
            if (frequency.length() > 0) {
              desireMatch = 4;
            }
            else {
              desireMatch = 3;
            }
          }
          else {
            desireMatch = 2;
          }
        }
      }
      else {
        desireMatch = 1;
      }

      long matchNum = 0;
      long lastMatchNum = 0;

      DAOOperator d = (DAOOperator) DaoFactory.create(DaoFactory.DAO_OBJECT);
      DAOCondition condition = new DAOCondition("radio_quality_alarm_param_tab");
      result_set = d.Query("*", condition);
      String pmHead;
      int matchRow = 0;
      String pmEqu;
      String pmBand;
      String pmFreq;

      long headMatch;
      long equMatch;
      long bandMatch;
      long freqMatch;

      while (result_set.getRowCount() > matchRow) {
        matchNum = 0;
        headMatch = 0;
        equMatch = 0;
        bandMatch = 0;
        freqMatch = 0;
        pmHead = result_set.getString(matchRow, "head_id");
        pmEqu = result_set.getString(matchRow, "equ_code");
        pmBand = result_set.getString(matchRow, "band");
        pmFreq = result_set.getString(matchRow, "frequency");

        if (pmHead.equalsIgnoreCase(headendID)) {
          headMatch = 2;
        }
        else if (pmHead.length() == 0) {
          headMatch = 1;
        }
        else {
          headMatch = 0;
        }
        if (pmEqu.equalsIgnoreCase(equCode)) {
          equMatch = 2;
        }
        else if (pmEqu.equalsIgnoreCase("All")) {
          equMatch = 1;
        }
        else {
          equMatch = 0;
        }
        if (pmBand.equalsIgnoreCase(band)) {
          bandMatch = 2;
        }
        else if (pmBand.equalsIgnoreCase("9")) {
          bandMatch = 1;
        }
        else {
          bandMatch = 0;
        }
        if (pmFreq.equalsIgnoreCase(frequency)) {
          freqMatch = 2;
        }
        else if (pmFreq.equalsIgnoreCase("0")) {
          freqMatch = 1;
        }
        else {
          freqMatch = 0;
        }

        if (headMatch == 2) {
          if (equMatch == 2) {
            if (bandMatch == 2) {
              if (freqMatch == 2) {
                matchNum = 7;
              }
              else if (freqMatch == 1) {
                matchNum = 6;
              }
              //else no match
            }
            else if (bandMatch == 1) {
              matchNum = 5;
            }
            //else no match
          }
          else if (equMatch == 1) {
            if (bandMatch == 2) {
              if (freqMatch == 2) {
                matchNum = 4;
              }
              else if (freqMatch == 1) {
                matchNum = 3;
              }
              //else no match
            }
            else if (bandMatch == 1) {
              matchNum = 2;
            }
            //else no match
          }
        }
        else if (headMatch == 1) {
          matchNum = 1;
        }

        if (matchNum >= lastMatchNum) {
          lastMatchNum = matchNum;
          if (matchRow > 0) {
            result_set.removeRow(0);
          }
        }
        else {
          result_set.removeRow(matchRow);
        }
        matchRow = 1;
      }
      if (lastMatchNum == desireMatch && paramID != null && paramID.length > 0) {
        paramID[0] = result_set.getString(0, "param_id");
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }

    return result_set;
  }

}
