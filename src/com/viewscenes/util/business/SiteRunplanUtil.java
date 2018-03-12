package com.viewscenes.util.business;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.pub.GDSet;
import com.viewscenes.util.StringTool;
import com.viewscenes.util.business.runplan.ProgramInfo;
import com.viewscenes.util.business.runplan.RunPlan;


public class SiteRunplanUtil {
  public SiteRunplanUtil() {
  }

  /**
   * ����վ����ȡ ����ͼ����sql�� 
   * �Ҽ����Ķ��ĵ����ˣ����������п���û���ù��õġ�ά����ע�⡣
   *
   * �ɼ���ӹ�����ȡ��
   * ң��վֱ��̨������ͼmon_areaȡ�����ĸ��ݲ�ֱ���ȡ��
   *
   * @param hide_code String �������Ĳ���ֻ��һ��վ�㡣
   * @return String
   */
  public static String getRunplanSqlByCode(String head_code) {
    String runSql =
        " and tab.station_id in ( select station_id from res_monitor_station_tab " +
        " where head_id in (select head_id from res_headend_tab  " +
        " where is_delete = 0 and code in ('" + head_code + "') ) and center_id in (select center_id" +
        		" from res_headend_tab where is_delete = 0 and code in ('" + head_code + "')))";
    String siteType = "101";
    String codeName = "";
    String headId = "";
    if (head_code != null && head_code.length() > 0) {
      head_code = SiteVersionUtil.getSiteOriCode(head_code);
      headId = SiteVersionUtil.getSiteHeadId(head_code);
      siteType = SiteVersionUtil.getSiteType(head_code);
      codeName = SiteVersionUtil.getSiteName(head_code);
    }
    /**
     * �ɼ���ӹ�����ȡ��
     * ң��վ��ֱ��̨����ͼmon_areaȡ�����Ĳ�ֱ�������
     */
    if (siteType.equalsIgnoreCase("102")) {
      if (SystemIdUtil.getLocalCenterId().equalsIgnoreCase("100")) {
        /**
         * ���ݲ�ֱ������ҡ�
         */
        runSql = " and runplan_id in (select runplan.runplan_id from zres_runplan_tab runplan,ZDIC_RUNPLAN_STATION_TAB runsta " +
            " where runplan.station_id=runsta.station_id and runplan.band=runsta.band and runsta.effect_ids like '%" +
            headId + "%' " +
            " and (runplan.direction=runsta.direction or runplan.direction+360=runsta.direction or runplan.direction=runsta.direction+360) )";
      }
      else {
        runSql = " and ','||mon_area||',' like '%," + codeName + ",%' ";
      }
    }
    return runSql;
  }

  /**
   * ���ݲ���ʱ������������ͼ��ѯ������
   * @param playTime String ����ʱ��������
   * @param isGb boolean �Ƿ�㲥����zres_runplan_tab//zres_runplan_gb_view������
   * @return String ʵ�ʵ�ʱ��sql��
   */
  public static String getRunplanSqlByTime(String playTime, boolean isGb) {
    StringBuffer runTimeSql = new StringBuffer();
    //zres_runplan_tabֱ�ӵ��á�
    String weekDayFieName = "weekday";
    String validBeginTimeFieName = "valid_start_time";
    String validEndTimeFieName = "valid_end_time";
    String startTimeFieName = "start_time";
    String endTimeFieName = "end_time";
    //zres_runplan_gb_view���á�
    //��Ϊԭ�������õ���res_runplan_tab��������������ͼ���Ա������䶯̫��
    if (isGb) {
      weekDayFieName = "weekdays";
      validBeginTimeFieName = "valid_startdatetime";
      validEndTimeFieName = "valid_enddatetime";
      startTimeFieName = "starttime";
      endTimeFieName = "endtime";
    }

    //������
    Calendar nowCal = Calendar.getInstance();
    try {
      nowCal.setTime(StringDateUtil.string2Date(playTime));
      int nowWeek = nowCal.get(Calendar.DAY_OF_WEEK);
      runTimeSql
          .append(" and ( ").append(weekDayFieName)
          .append(" = 'all' or  ").append(weekDayFieName)
          .append("=' ").append(nowWeek-1).append("') ");
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

    if (playTime != null && playTime.length() > 0) {
      //��Ч��
      runTimeSql.append(" and  ").append(validBeginTimeFieName)
          .append("<='").append(playTime)
          .append("' and  ").append(validEndTimeFieName)
          .append(">= '").append(playTime).append("' ");

      //����ʱ�䡣
      String playTime1 = "2000-01-01 " + playTime.substring(11);
      String playTime2 = "2000-01-02 " + playTime.substring(11);
      //1��
      runTimeSql.append(" and ( (  ").append(startTimeFieName)
          .append("<='").append(playTime1)
          .append("' and  ").append(endTimeFieName)
          .append(">= '").append(playTime1).append("' ");
      //2��
      runTimeSql.append(" ) or (  ").append(startTimeFieName)
          .append("<='").append(playTime2)
          .append("' and  ").append(endTimeFieName)
          .append(">= '").append(playTime2).append("' ) ) ");
    }
    return runTimeSql.toString();
  }

  /**
   * ����Ƶ��������Ρ�
   * @param freq String
   */
  public static String getBandFromFreq(String freq) {
    String band = "";
    if (freq != null && freq.length() > 0) { //��Ƶ�ʼ��㲨��
      float fFreq = new Float(freq).floatValue();
      if (fFreq >= 531 && fFreq <= 1602) { //�в�
        band = "1";
      }
      else if (fFreq >= 2300 && fFreq <= 26100) { //�̲�
        band = "0";
      }
      else if (fFreq >= 87000 && fFreq <= 108000) { //��Ƶ
        band = "2";
      }
    }
    return band;
  }

  public static String[] getFreqRangeByBand(String band) {
    if (band == null && "".equalsIgnoreCase(band)) {
      return null;
    }

    String[] freqRange = new String[2];
    //�̲�
    if ("0".equalsIgnoreCase(band)) {
      freqRange[0] = "2300";
      freqRange[1] = "26100";
    }
    //�в�
    else if ("1".equalsIgnoreCase(band)) {
      freqRange[0] = "531";
      freqRange[1] = "1602";
    }
    //��Ƶ
    else if ("2".equalsIgnoreCase(band)) {
      freqRange[0] = "87000";
      freqRange[1] = "108000";
    }
    return freqRange;
  }

  /**
   * �ж��Ƿ���ЧƵ�ʡ�
   * @param freq String
   */
  public static boolean isValidFreq(String freq) {
    boolean isValid = false;
    if (getBandFromFreq(freq).length() > 0) {
      isValid = true;
    }
    return isValid;
  }

  /**
  * ����ѡ��վ���еĵ�ǰ��Ч������ͼ
  * frequency[0]Ϊrunplan_id;
  * frequency[1]Ϊ����̨���ƣ�
  * frequency[2]ΪƵ�ʣ�
  * frequency[3]Ϊ��Ŀ��
  * frequency[4]Ϊ�Ƿ�����ʱ����ͼ "(��ʱ)"����""��
  * @param
  * @return
  */
 public static String[][] getFrequencylistByHeadCode(String head_code) throws
     Exception {
   String[][] frequency_list = null;
   String sql = "";
   String is_temporary;
   Calendar now = Calendar.getInstance();
   java.util.Date now_time = now.getTime();
   SimpleDateFormat formatter = new SimpleDateFormat(
       "yyyy-MM-dd HH:mm:ss",
       Locale.getDefault());
   String now_datetime = formatter.format(now_time);

   String siteRunAssoWhere = SiteRunplanUtil.getRunplanSqlByCode(head_code);
   sql =
       "select distinct(runplan_id) ,frequency,program_name,is_temporary,station_name as name " +
       " from zres_runplan_gb_view " +
       " where is_delete=0 and is_predefine=0 and is_confirm=1 and is_special=0 and timespan_type_id = 1 " +
       " and valid_startdatetime<='" + now_datetime +
       "' and valid_enddatetime>= '" + now_datetime + "' " + siteRunAssoWhere +
       " order by program_name , frequency ";


   GDSet data = null;
   try {
     data = DbComponent.Query(sql);
     int m = data.getRowCount();
     if (m > 0) {
       frequency_list = new String[5][m];
       for (int i = 0; i < m; i++) {
         frequency_list[0][i] = data.getString(i, "runplan_id");
         frequency_list[1][i] = data.getString(i, "name");
         frequency_list[2][i] = data.getString(i, "frequency");
         frequency_list[3][i] = data.getString(i, "program_name");
         is_temporary = data.getString(i, "is_temporary");
         if (is_temporary.equalsIgnoreCase("1")) {
           frequency_list[4][i] = "(��ʱ)";
         }
         else {
           frequency_list[4][i] = "";
         }
       }
     }
   }
   catch (Exception ex) {
     ex.printStackTrace();
   }
   return frequency_list;
 }
 /**
   * ��ȡվ����Ϣ
   * @param request
   * @param opDetail
   * @return
   */
  public static String[][] getHeadendInfo(String head_code) {

   StringBuffer opDetail=new StringBuffer();
   String[][] returnArray=null;
    try {

      String headendCode = head_code; //ǰ�˴���
      String headendName = "";//ǰ������

      String runplanType = "";
      //վ����벻Ϊ��
      if (headendCode != null && headendCode.length() > 0) {

       // headendName = SiteVersionUtil.getSiteName(headendCode);
       //2004��11��23�̴��ͣ�С��˵�ɼ���ֻ��ʾ�в��͵�Ƶ������ʾ�̲���ֱ��̨����ʾ
        runplanType = SiteVersionUtil.getSiteType(headendCode);


        //��ȡƵ���б�
        Vector progVector = new Vector();
       // Vector progVector1 = new Vector();
        Calendar nowCal = Calendar.getInstance();
        String nowDate = StringTool.Date2String(nowCal.getTime());
        Vector programVector = null;
        Vector programVector1 = null;
        if (runplanType.equalsIgnoreCase("102")) { //ң��վ
          programVector = RunPlan.getProgramInfos(headendCode, nowDate, null);
        }
        if (runplanType.equalsIgnoreCase("101")) { //�ɼ���
          programVector = RunPlan.getProgramInfos(headendCode, "",
                                                  nowDate, true,
                                                  "", "", "1", "");



        }
        ProgramInfo pinfo;
        String programStr;
        ProgramInfo pinfo1;

        //������ʾ��ʽ2004-11-13�̴����޸�ȥ����Դ + "/" + pinfo.getAuthor()
        for (int i = 0; i < programVector.size(); i++) {
            if(returnArray==null)
            {
                returnArray = new String[3][programVector.size()];
            }
          pinfo = (ProgramInfo) programVector.get(i);
          //���Ƶ��
          progVector.add(pinfo.getFrequency());
          returnArray[0][i]=pinfo.getFrequency();
          //��ӽ�Ŀ��Ϣ
//	  programStr = "";
//	  if (!pinfo.getBroadCast().equalsIgnoreCase("1")) {
//	    programStr += pinfo.getProgramName();
//	  }
          //��ӽ�Ŀ����
          programStr = pinfo.getProgramName() + " [";
          //���վ������
          programStr += pinfo.getStationName();
          if (pinfo.getRunplanType().compareTo("2") > 0) {
            //��ӽ�Ŀ��������
            programStr += "/" + pinfo.getTypeName();
            if (pinfo.getRunplanType().equalsIgnoreCase("5") &&
                pinfo.getCoverObject().length() > 0 &&
                !pinfo.getCoverObject().equalsIgnoreCase("null")) {
              programStr += "(" + pinfo.getCoverObject() + ")";
            }
          }
          programStr += "]";
          progVector.add(programStr);

          //�������ͼId
          String runplanId = pinfo.getRunplanID();
          progVector.add(runplanId);
          returnArray[1][i]=programStr;
            returnArray[2][i]=pinfo.getRunplanID();
        }

      }
      else {
        opDetail.append("δ���ҵ�ָ��վ��");
         return null;
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      opDetail.setLength(0);
      opDetail.append("���������� - " + e.toString());
    }
    return returnArray;
  }
  /**
     * ��ȡվ����Ϣ
     * @param request
     * @param opDetail
     * @return
     */
    public static String[][] getHeadendInfo(String head_code,String runplan_type) {

     StringBuffer opDetail=new StringBuffer();
     String[][] returnArray=null;
      try {

        String headendCode = head_code; //ǰ�˴���
        String headendName = "";//ǰ������

        String runplanType = "";
        //վ����벻Ϊ��
        if (headendCode != null && headendCode.length() > 0) {

         // headendName = SiteVersionUtil.getSiteName(headendCode);
         //2004��11��23�̴��ͣ�С��˵�ɼ���ֻ��ʾ�в��͵�Ƶ������ʾ�̲���ֱ��̨����ʾ
          runplanType = SiteVersionUtil.getSiteType(headendCode);


          //��ȡƵ���б�
          Vector progVector = new Vector();
         // Vector progVector1 = new Vector();
          Calendar nowCal = Calendar.getInstance();
          String nowDate = StringTool.Date2String(nowCal.getTime());
          Vector programVector = null;
          Vector programVector1 = null;
          if (runplanType.equalsIgnoreCase("102")) { //ң��վ
              if(runplan_type.equals("0"))
              {
                  runplan_type="";
              }
              programVector = RunPlan.getProgramInfos(headendCode, "",
                                                      nowDate, true,
                                                      "", "", runplan_type, "");

          }

          if (runplanType.equalsIgnoreCase("101")) { //�ɼ���
              if(runplan_type.equals("1")||runplan_type.equals("0"))
              {
                  programVector = RunPlan.getProgramInfos(headendCode, "",
                                                          nowDate, true,
                                                          "", "", "1", "");
              }
              if(runplan_type.equals("2")||runplan_type.equals("0"))
              {
                  programVector1 = RunPlan.getProgramInfos(headendCode, "",
                          nowDate, true, "", "", "2", "");
              }
          programVector.add(programVector1);

          }
          ProgramInfo pinfo;
          String programStr;
          ProgramInfo pinfo1;

          //������ʾ��ʽ2004-11-13�̴����޸�ȥ����Դ + "/" + pinfo.getAuthor()
          for (int i = 0; i < programVector.size(); i++) {
              if(returnArray==null)
              {
                  returnArray = new String[3][programVector.size()];
              }
            pinfo = (ProgramInfo) programVector.get(i);
            //���Ƶ��
            progVector.add(pinfo.getFrequency());
            returnArray[0][i]=pinfo.getFrequency();
            //��ӽ�Ŀ��Ϣ
//	  programStr = "";
//	  if (!pinfo.getBroadCast().equalsIgnoreCase("1")) {
//	    programStr += pinfo.getProgramName();
//	  }
            //��ӽ�Ŀ����
            programStr = pinfo.getProgramName() + " [";
            //���վ������
            programStr += pinfo.getStationName();
            if (pinfo.getRunplanType().compareTo("2") > 0) {
              //��ӽ�Ŀ��������
              programStr += "/" + pinfo.getTypeName();
              if (pinfo.getRunplanType().equalsIgnoreCase("5") &&
                  pinfo.getCoverObject().length() > 0 &&
                  !pinfo.getCoverObject().equalsIgnoreCase("null")) {
                programStr += "(" + pinfo.getCoverObject() + ")";
              }
            }
            programStr += "]";
            progVector.add(programStr);

            //�������ͼId
            String runplanId = pinfo.getRunplanID();
            progVector.add(runplanId);
            returnArray[1][i]=programStr;
              returnArray[2][i]=pinfo.getRunplanID();
          }

        }
        else {
          opDetail.append("δ���ҵ�ָ��վ��");
           return null;
        }
      }
      catch (Exception e) {

        opDetail.setLength(0);
        opDetail.append("���������� - " + e.toString());
      }
      return returnArray;
    }
//����unit,headcode,ruplantype�õ�վ�������ͼsql���
    public static String runplanSql(String head_code,String runplan_type,String nowTime) throws Exception {
        if (head_code == null) {
     head_code = "";
   }
   String runplanConSql = " ";
   if (runplan_type != null && !runplan_type.equalsIgnoreCase("0")) {
     runplanConSql = " and RUNPLAN_TYPE_ID in ( " +runplan_type+ ")";
   }


   /**
    * �����������㡣
    */
   int nowWeek = 0;
   Calendar nowCal = Calendar.getInstance();
   try {
     nowCal.setTime(StringDateUtil.string2Date(nowTime));
     //2006-11-02 16:16:20

     //������ʱ����֤�����м�������.
     nowCal.set(Calendar.MINUTE, 29);
     nowCal.set(Calendar.SECOND, 0);
     nowWeek = nowCal.get(Calendar.DAY_OF_WEEK) - 1;
   }
   catch (Exception ex) {
     throw new Exception("ʱ���ʽ�쳣!", ex);
   }

   /**
    * ����ʱ���������㡣
    */
   Calendar unitCal = (Calendar) (nowCal.clone());
   String unitBegin1 = null; //��Ԫ��ʼ
   String unitEnd1 = null; //��Ԫ����
   String unitBegin2 = null; //��Ԫ��ʼ
   String unitEnd2 = null; //��Ԫ����

   //���ȡ��ǰʱ�䵥Ԫ��
   try {
     //��Ԫ������
     unitCal.set(Calendar.YEAR, 2000);
     unitCal.set(Calendar.MONTH, 0);
     unitCal.set(Calendar.MINUTE, 0);
     unitCal.set(Calendar.DAY_OF_MONTH, 1);
     unitCal.set(Calendar.SECOND, 0);

     //��һ�쿪ʼʱ��
     unitBegin1 = StringDateUtil.date2String(unitCal.getTime());
     //��1Сʱ����һ�����ʱ��
     unitCal.add(Calendar.HOUR, 1);
     unitEnd1 = StringDateUtil.date2String(unitCal.getTime());

     //�ӵ�һ�����ʱ�䣬��1�죬�õڶ������ʱ��
     unitCal.add(Calendar.DAY_OF_MONTH, 1);
     unitEnd2 = StringDateUtil.date2String(unitCal.getTime());
     //��1Сʱ���õڶ��쿪ʼʱ�䡣
     unitCal.add(Calendar.HOUR, -1);
     unitBegin2 = StringDateUtil.date2String(unitCal.getTime());
     //setTime���ԡ�
   }
   catch (Exception ex1) {
     throw new Exception("ʱ���ʽ�쳣!", ex1);
   }

   //��Ч��
   try {
     nowTime = StringDateUtil.date2String(new Date(nowCal.getTimeInMillis()));
   }
   catch (Exception ex2) {
     ex2.printStackTrace();
   }

   /**
    * ����ʱ��Ŀ۳���
    * ����Ժ�һ��Ҫ�ӣ�ֻ��ͨ��GDSet���ڴ�������ؽ����
    * ���Բο�����ͼ��������ͼ�߼���
    */

   //���ݿ����

   String tab="";
   String runplan_id="";
   //runplan_type=0 ����õ������������е��������ݡ�
   String sql="";
//   if(runplan_type.equals("0"))
//   {
     sql =
  new StringBuffer("SELECT runplan_id,runplan_type_id,station.name as station_name,")
  .append("freq as frequency,program_name,transmiter_no,")
  .append("to_char(tab.START_TIME,'hh24:mi:ss')  as starttime,")
  .append("to_char(tab.END_TIME,'hh24:mi:ss')  as endtime,")
  .append(" substr('").append(unitBegin1)
  //.append("',11,6) || '~' || substr('").append(unitEnd1)
  .append("',11,6)  as unit ")

  .append(" FROM zres_runplan_tab tab,")
  .append(" zdic_program_name_tab programname,")
  .append(" res_transmit_station_tab station ")
  .append(" WHERE tab.station_id=station.station_id   ")
  .append(" and tab.program_id=programname.program_id  and timespan_type_id=1 and ")
  .append(" (weekday ='all' or weekday='").append(nowWeek).append("') ")
  .append("and valid_start_time <= TO_DATE ('").append(nowTime)
  .append("','YYYY-MM-DD HH24:MI:SS' ) and valid_end_time >= TO_DATE ('")
  .append(nowTime).append("','YYYY-MM-DD HH24:MI:SS' ) ")
  .append(" AND is_predefine = '0' AND is_confirm = '1' ")
  .append(SiteRunplanUtil.getRunplanSqlByCode(head_code))
  .append(" and tab.is_delete =0 ").append(runplanConSql)
  //��ʼʱ�� С�� ��Ԫ������ ����ʱ�� ���� ��Ԫ��ʼ
  .append(" and is_special=0 ")
  .append(" and ((start_time< TO_DATE ('").append(unitEnd1)
  .append("','YYYY-MM-DD HH24:MI:SS' )   and end_time>TO_DATE ('")
  .append(unitBegin1).append("','YYYY-MM-DD HH24:MI:SS' ) ) ")
  .append(" or ((start_time<TO_DATE ('")
  .append(unitEnd2).append("','YYYY-MM-DD HH24:MI:SS' ) ")
  .append(" and end_time>TO_DATE ('").append(unitBegin2)
  .append("','YYYY-MM-DD HH24:MI:SS' ) ))) ")
  
  
  .append(" and runplan_type_id||station.name||freq||program_name||transmiter_no not in(")
  .append("select runplan_type_id||station.name||freq||program_name||transmiter_no from zres_runplan_tab tab ,zdic_program_name_tab programe,")
  .append("res_transmit_station_tab station ")
  .append(" WHERE tab.station_id=station.station_id   ")
  .append(" and tab.program_id=programname.program_id  and timespan_type_id!=1 and ")
  .append(" (weekday ='all' or weekday='").append(nowWeek).append("') ")
  .append("and valid_start_time <= TO_DATE ('").append(nowTime)
  .append("','YYYY-MM-DD HH24:MI:SS' ) and valid_end_time >= TO_DATE ('")
  .append(nowTime).append("','YYYY-MM-DD HH24:MI:SS' ) ")
  .append(" AND is_predefine = '0' AND is_confirm = '1' ")
  .append(SiteRunplanUtil.getRunplanSqlByCode(head_code))
  .append(" and tab.is_delete =0 ").append(runplanConSql)
  //��ʼʱ�� С�� ��Ԫ������ ����ʱ�� ���� ��Ԫ��ʼ
  .append(" and is_special=0 ")
  .append(" and ((start_time< TO_DATE ('").append(unitEnd1)
  .append("','YYYY-MM-DD HH24:MI:SS' )   and end_time>TO_DATE ('")
  .append(unitBegin1).append("','YYYY-MM-DD HH24:MI:SS' ) ) ")
  .append(" or ((start_time<TO_DATE ('")
  .append(unitEnd2).append("','YYYY-MM-DD HH24:MI:SS' ) ")
  .append(" and end_time>TO_DATE ('").append(unitBegin2)
  .append("','YYYY-MM-DD HH24:MI:SS' ) ))) ")
  .append(" and to_date('2000-01-01 '||'").append(unitBegin1.substring(11, 16))
  .append(":00','yyyy-mm-dd hh24:mi:ss') between start_time and end_time )")
  .toString();
    

       //��������ĵ�½���������,ֱ��̨��½�Ͳ���
 if(SystemIdUtil.isCenter())
 {
  sql+=  new StringBuffer(" union SELECT runplan_id,runplan_type_id,station.name as station_name,")
  .append("freq as frequency,program_name,transmiter_no,")
  .append("to_char(tab.START_TIME,'hh24:mi:ss')  as starttime,")
  .append("to_char(tab.END_TIME,'hh24:mi:ss')  as endtime,")
  .append(" substr('").append(unitBegin1)
  //.append("',11,6) || '~' || substr('").append(unitEnd1)
  .append("',11,6)  as unit ")

  .append(" FROM zres_center_runplan_tab tab,")
  .append(" zdic_program_name_tab programname,")
  .append(" res_transmit_station_tab station ")
  .append(" WHERE tab.station_id=station.station_id   and timespan_type_id=1 ")
  .append(" and tab.program_id=programname.program_id and ")
  .append(" (weekday ='all' or weekday='").append(nowWeek).append("') ")
  .append("and valid_start_time <= TO_DATE ('").append(nowTime)
  .append("','YYYY-MM-DD HH24:MI:SS' ) and valid_end_time >= TO_DATE ('")
  .append(nowTime).append("','YYYY-MM-DD HH24:MI:SS' ) ")
  .append(" AND is_predefine = '0' AND is_confirm = '1' ")
  .append(" and tab.mon_area like '%"+SiteVersionUtil.getSiteName(head_code)+"%'")
  .append(" and tab.is_delete =0 ").append(runplanConSql)
  //��ʼʱ�� С�� ��Ԫ������ ����ʱ�� ���� ��Ԫ��ʼ
  .append(" and is_special=0 ")
  .append(" and ((start_time< TO_DATE ('").append(unitEnd1)
  .append("','YYYY-MM-DD HH24:MI:SS' )   and end_time>TO_DATE ('")
  .append(unitBegin1).append("','YYYY-MM-DD HH24:MI:SS' ) ) ")
  .append(" or ((start_time<TO_DATE ('")
  .append(unitEnd2).append("','YYYY-MM-DD HH24:MI:SS' ) ")
  .append(" and end_time>TO_DATE ('").append(unitBegin2)
  .append("','YYYY-MM-DD HH24:MI:SS' ) ))) ")
 
  
  
  .append(" and runplan_type_id||station.name||freq||program_name||transmiter_no not in(")
  .append("select runplan_type_id||station.name||freq||program_name||transmiter_no from zres_center_runplan_tab tab ,zdic_program_name_tab programe,")
  .append("res_transmit_station_tab station ")
  .append(" WHERE tab.station_id=station.station_id   ")
  .append(" and tab.program_id=programname.program_id  and timespan_type_id!=1 and ")
  .append(" (weekday ='all' or weekday='").append(nowWeek).append("') ")
  .append("and valid_start_time <= TO_DATE ('").append(nowTime)
  .append("','YYYY-MM-DD HH24:MI:SS' ) and valid_end_time >= TO_DATE ('")
  .append(nowTime).append("','YYYY-MM-DD HH24:MI:SS' ) ")
  .append(" AND is_predefine = '0' AND is_confirm = '1' ")
  .append(SiteRunplanUtil.getRunplanSqlByCode(head_code))
  .append(" and tab.is_delete =0 ").append(runplanConSql)
  //��ʼʱ�� С�� ��Ԫ������ ����ʱ�� ���� ��Ԫ��ʼ
  .append(" and is_special=0 ")
  .append(" and ((start_time< TO_DATE ('").append(unitEnd1)
  .append("','YYYY-MM-DD HH24:MI:SS' )   and end_time>TO_DATE ('")
  .append(unitBegin1).append("','YYYY-MM-DD HH24:MI:SS' ) ) ")
  .append(" or ((start_time<TO_DATE ('")
  .append(unitEnd2).append("','YYYY-MM-DD HH24:MI:SS' ) ")
  .append(" and end_time>TO_DATE ('").append(unitBegin2)
  .append("','YYYY-MM-DD HH24:MI:SS' ) ))) ")
  .append(" and to_date('2000-01-01 '||'").append(unitBegin1.substring(11, 16))
  .append(":00','yyyy-mm-dd hh24:mi:ss') between start_time and end_time )")
  .toString();
  

   }
   
   sql+=" order by RUNPLAN_TYPE_ID,station_name,program_name,frequency  asc";
   return sql;
    }

  public static void main(String[] args) {
    String str="aaaa\raaaaaa";
    System.out.println(str);
  }

}
