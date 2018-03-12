package com.viewscenes.util.business;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.util.business.SiteRunplanUtil;
import com.viewscenes.util.business.SiteVersionUtil;


/**
 * ¼���ļ���� ����̨����Ŀ������ͼ���Ͳ�ѯ���ܳ����ࡣ
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class RecordRunplanInfoUtil {
  public static int stationNamePos = 0;
  public static int programNamePos = 1;
  public static int runplanTypeIdPos = 2;
  public static int runplanIdPos = 3;

  private boolean isCenter = true;
  private Connection conn = null;

  //������ͼֱ����ȡ��
  private String runSql =
      " select run.station_id,sta.name,run.program_id,pro.program_name," +
      " run.runplan_type_id,run.runplan_id " +
      " from zres_runplan_tab run,RES_TRANSMIT_STATION_TAB sta, ZDIC_PROGRAM_NAME_TAB pro" +
      " where run.station_id=sta.station_id and run.program_id=pro.program_id and run.runplan_id=?";
  private DbComponent.DbQuickExeSQL runStat = null;

  //�����������ͼ��ѯ��
  private String taskSql =
      " select run.station_id,sta.name,run.program_id,pro.program_name,run.runplan_type_id,run.runplan_id " +
      " from RADIO_UNIFY_TASK_TAB task,zres_runplan_tab run," +
      " RES_TRANSMIT_STATION_TAB sta, ZDIC_PROGRAM_NAME_TAB pro" +
      " where task.task_id = ? and task.runplan_id= run.runplan_id " +
      " and run.station_id=sta.station_id and run.program_id=pro.program_id";
  private DbComponent.DbQuickExeSQL taskStat = null;

  //������ͼ�й�����ȡ��
  //��������
  private String baseSql =
      " select run.station_id,sta.name,run.program_id,pro.program_name,run.runplan_type_id,run.runplan_id   " +
      " from zres_runplan_tab run,RES_TRANSMIT_STATION_TAB sta,ZDIC_PROGRAM_NAME_TAB pro" +
      " where run.station_id=sta.station_id and run.program_id=pro.program_id " +
      " and run.is_delete=0 and run.is_confirm=1 and run.timespan_type_id=1 " +
      " and run.FREQ=? and valid_start_time<=? and valid_end_time>=?  " +
      //������Ϊall���� javaʱ��-1
      " and (run.weekday='all' or run.weekday=?) " +
      //��<1�Ŵ�ͷ�Ľᣬ��>1�ſ�����<2�� ��>2��
      " and ( (start_time<? and end_time>?) or (start_time<? and end_time>?) ) ";

  //�ɼ�����������
  private String type101Sql = baseSql +
      " and run.station_id in ( select distinct station_id from res_monitor_station_tab " +
      " where head_id in (select head_id from res_headend_tab  " +
      " where is_delete = 0 and code in (?) ))  ";
  private DbComponent.DbQuickExeSQL type101Stat = null;

  private String type102Sql = null;
  private DbComponent.DbQuickExeSQL type102Stat = null;

  public RecordRunplanInfoUtil() {
    //���ô��룬ֻ�Ǳ����˴��������ظ����޸�ʱҪͬʱ�޸Ĵ˴���
    SiteRunplanUtil.getRunplanSqlByCode(null);

    /**
     * ң��վ��Դ����
     */
    isCenter = SystemIdUtil.isCenter();
    //ң��վ��������
    //���Ĳ�ֱ�
    if (isCenter) {
      type102Sql = baseSql +
	  " and runplan_id in (select distinct runplan.runplan_id " +
	  " from zres_runplan_tab runplan,ZDIC_RUNPLAN_STATION_TAB runsta " +
	  " where runplan.station_id=runsta.station_id and runplan.band=runsta.band " +
	  " and runsta.effect_ids like ? and  (runplan.direction=runsta.direction " +
	  " or runplan.direction+360=runsta.direction " +
	  " or runplan.direction=runsta.direction+360) )";
    }
    else { //ֱ��̨�����ֶΡ�
      type102Sql = baseSql + " and mon_area like ? ";
    }

    type101Sql += " order by run.runplan_id desc ";
    type102Sql += " order by run.runplan_id desc ";
  }

  /**
   * ��Դ��ʼ��������
   * @throws Exception
   */
  public void initResource() throws Exception {
    DbComponent db = new DbComponent();
    this.runStat = db.new DbQuickExeSQL(runSql);
    this.taskStat = db.new DbQuickExeSQL(taskSql);
    this.type101Stat = db.new DbQuickExeSQL(type101Sql);
    this.type102Stat = db.new DbQuickExeSQL(type102Sql);
    this.runStat.getConnect();
    this.taskStat.getConnect();
    this.type101Stat.getConnect();
    this.type102Stat.getConnect();
  }

  /**
   * ��Դ�رգ�����
   */
  public void releaseResource() {
    //�ر������ѯ��
    if (this.runStat != null) {
      try {
	this.runStat.closeConnect();
      }
      catch (Exception ex) {
      }
    }

    if (this.taskStat != null) {
      try {
	this.taskStat.closeConnect();
      }
      catch (Exception ex) {
      }
    }
    if (this.type101Stat != null) {
      try {
	this.type101Stat.closeConnect();
      }

      catch (Exception ex) {
      }
    }
    if (this.type102Stat != null) {
      try {
	this.type102Stat.closeConnect();
      }
      catch (Exception ex) {
      }
    }
    //�ر����ӡ�
    if (this.conn != null) {
      try {
	this.conn.close();
      }
      catch (Exception ex) {
      }
    }
  }

  /**
   * ����¼��������Ӧ��Ϣ��ȡ��Ӧ̨������Ŀ���Ĺ����ࡣ
   * Ҫ���ô˷�������Ҫ��ѭ��Ʋ���ʹ��,�Ա㹲����Դ
   * @param taskId String
   * @param headCode String
   * @param freq String
   * @param startRecordTime String
   * @param endRecordTime String
   * @return String[] 0 ̨����1 ��Ŀ���� 2 ����ͼ����id�� 3����ͼid
   */
  public String[] getRunplanInfoFunc(String runId, String taskId,
				     String headCode, String freq,
				     String startRecordTime,
				     String endRecordTime) {
    /**
     * �ȸ���runplan_id������ͼ����ȡ��Ӧ��Ϣ��
     */
    if (runId != null) {
      ResultSet rs = null;
      try {
	this.runStat.setString(1, runId);
	rs = this.runStat.exeQuery();
	if (rs.next()) {
	  return getRetArrayFromRs(rs);
	}
      }
      catch (Exception ex) {
	ex.printStackTrace();
      }
      finally {
	if (rs != null) {
	  try {
	    rs.close();
	  }
	  catch (SQLException ex1) {
	  }
	}
      }
    }

    /**
     * ������ݷ��ص�����id---runplan_id������ͼ����ȡ��Ӧ��Ϣ��
     */
    if (taskId != null&&taskId!="") {

    ResultSet rs = null;
      try {
	this.taskStat.setString(1, taskId);
	rs = this.taskStat.exeQuery();
	if (rs.next()) {
	  return getRetArrayFromRs(rs);
	}
      }
      catch (Exception ex) {
	ex.printStackTrace();
      }
      finally {
	if (rs != null) {
	  try {
	    rs.close();
	  }
	  catch (SQLException ex1) {
	  }
	}
      }
    }

    /**
     * �����ȡ������������ͼ�й�����
     */
    String headType = SiteVersionUtil.getSiteType(headCode);
    //�ɼ������
    DbComponent.DbQuickExeSQL stat = type101Stat;
    String headParam = SiteVersionUtil.getSiteOriCode(headCode);
    //ң��վ���
    if ("102".equalsIgnoreCase(headType)) {
      stat = type102Stat;
      //���Ļ���ֱ��̨
      if (isCenter) {
	headParam = "%," + SiteVersionUtil.getSiteHeadId(headCode) + ",%";
      }
      else {
	headParam = "%" + SiteVersionUtil.getSiteName(headCode) + "%";
      }
    }

    ResultSet rs = null;
    try {
      int i = 1;
      //Ƶ��
      stat.setString(i++, freq);
      //��Ч��
//      stat.setTimestamp(i++, StringDateUtil.getTimestamp(startRecordTime));
//     stat.setTimestamp(i++, StringDateUtil.getTimestamp(endRecordTime));
      stat.setString(i++, startRecordTime);
      stat.setString(i++, endRecordTime);

      //������
      Calendar timeCal = Calendar.getInstance();
      timeCal.setTime(StringDateUtil.string2Date(startRecordTime));
      int timeWeek = timeCal.get(Calendar.DAY_OF_WEEK) - 1;
      stat.setString(i++, timeWeek + "");

      /**
       * ����ʱ��
       */
      long timeBegin1; //1��ʼ
      long timeEnd1; //1����
      long timeBegin2; //2��ʼ
      long timeEnd2; //2����

      //��ʼ¼��ʱ��
      timeCal.setTime(StringDateUtil.string2Date(startRecordTime));
      timeCal.set(Calendar.YEAR, 2000);
      timeCal.set(Calendar.MONTH, 0);
      timeCal.set(Calendar.DAY_OF_MONTH, 1);
      timeBegin1 = timeCal.getTimeInMillis();
      timeCal.set(Calendar.DAY_OF_MONTH, 2);
      timeBegin2 = timeCal.getTimeInMillis();

      //����¼��ʱ��
      timeCal.setTime(StringDateUtil.string2Date(endRecordTime));
      timeCal.set(Calendar.YEAR, 2000);
      timeCal.set(Calendar.MONTH, 0);
      timeCal.set(Calendar.DAY_OF_MONTH, 1);
      timeEnd1 = timeCal.getTimeInMillis();
      timeCal.set(Calendar.DAY_OF_MONTH, 2);
      timeEnd2 = timeCal.getTimeInMillis();

      //����
      stat.setTimestamp(i++, new Timestamp(timeEnd1));
      stat.setTimestamp(i++, new Timestamp(timeBegin1));
      stat.setTimestamp(i++, new Timestamp(timeEnd2));
      stat.setTimestamp(i++, new Timestamp(timeBegin2));
      //վ������
      stat.setString(i++, headParam);

      rs = stat.exeQuery();
      if (rs.next()) {
	return getRetArrayFromRs(rs);
      }
    }
    catch (Exception ex2) {
      ex2.printStackTrace();
    }
    finally {
      if (rs != null) {
	try {
	  rs.close();
	}
	catch (SQLException ex1) {
	}
      }
    }

    //�գ����ڹ㲥����
    String[] nullArray = {
	"", "", "", ""};
    return nullArray;
  }

  /**
   * ���ڵ�����¼����Ϣ��ѯ������ֱ���ṩ��ѯ������Լ�����ʹ�õ�ʾ����
   * @param taskId String
   * @param headCode String
   * @param freq String
   * @param startRecordTime String
   * @param endRecordTime String
   * @return String[]
   */
  public String[] getRunplanInfo(String runId, String taskId, String headCode,
				 String freq, String startRecordTime,
				 String endRecordTime) {
    try {
      this.initResource();
      return this.getRunplanInfoFunc(runId, taskId, headCode, freq,
				     startRecordTime, endRecordTime);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      String[] nullArray = {
	  "", "", "", ""};
      return nullArray;
    }
    finally {
      this.releaseResource();
    }
  }

  private String[] getRetArrayFromRs(ResultSet rs) throws Exception {
    String[] staProName = new String[4];
    staProName[stationNamePos] = rs.getString(2);
    staProName[programNamePos] = rs.getString(4);
    staProName[runplanTypeIdPos] = rs.getString(5);
    staProName[runplanIdPos] = rs.getString(6);
    return staProName;
  }

  protected void finalize() {
    this.releaseResource();
    try {
      super.finalize();
    }
    catch (Throwable ex) {
      ex.printStackTrace();
    }
  }

  public static void testGetInfo() {
    RecordRunplanInfoUtil recordUtil = new RecordRunplanInfoUtil();
    String taskId = "508111695";
    String headCode = SiteVersionUtil.getHeadCodeById("100400027");
    String freq = "6280";
    String startRecordTime = "2006-10-1 7:02:01";
    String endRecordTime = "2006-10-1 7:02:01";
    System.out.println(recordUtil.getRunplanInfo(null, taskId, headCode, freq,
						 startRecordTime, endRecordTime));
  }

  public static void main(String[] args) {
    testGetInfo();
  }
}
