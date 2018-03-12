package com.viewscenes.util.business.runplan;

/**
 * *************************************   
*    
* ��Ŀ���ƣ�hwdata   
* �����ƣ�TimeSpan   
* ��������   
* �����ˣ�����
* ����ʱ�䣺2013-3-11 ����03:45:16   
* �޸��ˣ�����
* �޸�ʱ�䣺2013-3-11 ����03:45:16   
* �޸ı�ע��   
* @version    
*    
***************************************
 */

public class TimeSpan {
  private long startTime; //��ʼʱ�� (��)
  private long endTime; //����ʱ�� (��)
  private long type; // ����ͼ���� 1:����,2:����,3:ͣ��
  private ProgramInfo programInfo;

  /**
   * ���캯��
   */
  public TimeSpan() {
    startTime = 0;
    endTime = 0;
    type = 0;
    programInfo = null;
  }

  /**
   * ���캯��
   * @param startTime (��)
   * @param endTime (��)
   * @param type 1:����,2:����
   */
  public TimeSpan(long startTime, long endTime, long type) {
    setStartTime(startTime);
    setEndTime(endTime);
    setType(type);
    programInfo = null;
  }

  public TimeSpan(long startTime, long endTime, long type,
		  ProgramInfo programInfo) {
    setStartTime(startTime);
    setEndTime(endTime);
    setType(type);
    setProgramInfo(programInfo);
  }

  /**
   * ���캯��
   * @param startTime  HH24:MI:SS
   * @param endTime HH24:MI:SS
   * @param type 1:����,2:����
   */
  public TimeSpan(String startTime, String endTime, String type) {
    setStartTime(startTime);
    setEndTime(endTime);
    setType(type);
  }

  public TimeSpan(String startTime, String endTime, String type,
		  ProgramInfo programInfo) {
    setStartTime(startTime);
    setEndTime(endTime);
    setType(type);
    setProgramInfo(programInfo);
  }

  /**
   * ���ؿ�ʼʱ��
   * @return (��)
   */
  public long getStartTime() {
    return startTime;
  }

  /**
   * ���ÿ�ʼʱ��
   * @param startTime (��)
   */
  public void setStartTime(long startTime) {
    if (startTime >= 24 * 3600 - 1 || startTime < 0) {
      startTime = 0;
    }
    this.startTime = startTime;
  }

  /**
   * ���ÿ�ʼʱ��
   * @param startStr HH24:MI:SS
   */
  public void setStartTime(String startStr) {
    this.startTime = parse(startStr);
    if (startTime >= 24 * 3600 - 1 || startTime < 0) {
      startTime = 0;
    }
  }

  /**
   * ��ȡ����ʱ��
   * @return (��)
   */
  public long getEndTime() {
    return endTime;
  }

  /**
   * ���ý���ʱ��
   * @param endTime (��)
   */
  public void setEndTime(long endTime) {
    if (endTime >= 24 * 3600 - 1 || endTime <= 0) {
      endTime = 24 * 3600;
    }
    this.endTime = endTime;
  }

  /**
   * ���ý���ʱ��
   * @param endStr HH24:MI:SS
   */
  public void setEndTime(String endStr) {
    this.endTime = parse(endStr);
    if (endTime >= 24 * 3600 - 1 || endTime <= 0) {
      endTime = 24 * 3600;
    }
  }

  /**
   * ������ʱ��
   * @return (��)
   */
  public long getInterval() {
    long returnValue = endTime - startTime;
    if (returnValue < 0) {
      returnValue += 24 * 3600;
    }
    return returnValue;
  }

  /**
   * ��������ͼ����
   * @return 1:����,2:����
   */
  public long getType() {
    return type;
  }

  /**
   * ��������ͼ����
   * @param type 1:����,2:����
   */
  public void setType(long type) {
    this.type = type;
  }

  /**
   * ��������ͼ����
   * @param type 1:����,2:����
   */
  public void setType(String type) {
    try {
      this.type = new Long(type).longValue();
    }
    catch (Exception e) {
      e.printStackTrace();
      this.type = 0;
    }
  }

  public ProgramInfo getProgramInfo() {
    return programInfo;
  }

  public void setProgramInfo(ProgramInfo programInfo) {
    this.programInfo = programInfo;
  }

  /**
   * ���ʱ���ʽ
   * @param str  HH24:MI:SS
   * @return �ɹ����ع淶�� HH24:MI:SS ��ʽ,���򷵻� NULL
   */
  public static String checkParse(String str) {
    return checkParse(str, false, false);
  }

  /**
   * ���ʱ���ʽ
   * @param str  HH24:MI:SS
   * @param overDays true:�������,false:24Сʱ����
   * @return �ɹ����ع淶�� HH24:MI:SS ��ʽ,���򷵻� NULL
   */
  public static String checkParse(String str, boolean overDays) {
    return checkParse(str, overDays, false);
  }

  /**
   * ���ʱ���ʽ
   * @param str  HH24:MI:SS
   * @param overDays true:�������,false:24Сʱ����
   * @param enableNegative true:������,false:����ȡ��
   * @return �ɹ����ع淶�� HH24:MI:SS ��ʽ,���򷵻� NULL
   */
  public static String checkParse(String str, boolean overDays,
				  boolean enableNegative) {
    String returnValue = null;
    try {
      long hour = 0;
      long min = 0;
      long sec = 0;
      boolean nagativeValue = false;
      String val[] = str.split(":");
      if (val.length > 0 && val[0].length() > 0) {
	if (val[0].charAt(0) == '-') {
	  nagativeValue = true;
	  hour = new Long(val[0].substring(1)).longValue();
	}
	else {
	  hour = new Long(val[0]).longValue();
	}
      }
      if (val.length > 1 && val[1].length() > 0) {
	min = new Long(val[1]).longValue();
      }
      if (val.length > 2 && val[2].length() > 0) {
	sec = new Long(val[2]).longValue();
      }
      if (sec > 59) {
	min += sec / 60;
	sec %= 60;
      }
      if (min > 59) {
	hour += min / 60;
	min %= 60;
      }
      if (sec < 0) {
	sec = 0;
      }
      if (min < 0) {
	min = 0;
      }
      long parseVal = hour * 3600 + min * 60 + sec;
      if (nagativeValue && enableNegative) {
	parseVal *= -1;
      }
      returnValue = format(parseVal, overDays, enableNegative);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return returnValue;
  }

  /**
   * ����ʱ�䴮,����,24Сʱ��
   * @param str HH24:MI:SS
   * @return (��)
   */
  public static long parse(String str) {
    return parse(str, false, false);
  }

  /**
   * ����ʱ�䴮,����
   * @param str HH24:MI:SS
   * @return (��)
   */
  public static long parse(String str, boolean overDays) {
    return parse(str, overDays, false);
  }

  /**
   * ����ʱ�䴮
   * @param str HH24:MI:SS
   * @param overDays true:�������,false:24Сʱ����
   * @param enableNegative true:������,false:����ȡ��
   * @return (��)
   */
  public static long parse(String str, boolean overDays, boolean enableNegative) {
    long returnValue = 0;
    long hour = 0;
    long min = 0;
    long sec = 0;
    boolean nagativeValue = false;
    try {
      String val[] = str.split(":");
      if (val.length > 0 && val[0].length() > 0) {
	if (val[0].charAt(0) == '-') {
	  nagativeValue = true;
	  hour = new Long(val[0].substring(1)).longValue();
	}
	else {
	  hour = new Long(val[0]).longValue();
	}
      }
      if (val.length > 1 && val[1].length() > 0) {
	min = new Long(val[1]).longValue();
      }
      if (val.length > 2 && val[2].length() > 0) {
	sec = new Long(val[2]).longValue();
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    if (sec > 59) {
      min += sec / 60;
      sec %= 60;
    }
    if (min > 59) {
      hour += min / 60;
      min %= 60;
    }
    if (sec < 0) {
      sec = 0;
    }
    if (min < 0) {
      min = 0;
    }
    if (!overDays && (hour > 24 || min != 0 || sec != 0)) {
      hour %= 24;
    }
    returnValue = hour * 3600 + min * 60 + sec;
    if (nagativeValue && enableNegative) {
      returnValue *= -1;

    }
    return returnValue;
  }

  /**
   * ��ʽ���ַ���,����,24Сʱ��
   * @param val (��)
   * @return HH24:MI:SS
   */
  public static String format(long val) {
    return format(val, false, false);
  }

  /**
   * ��ʽ���ַ���,����
   * @param val (��)
   * @return HH24:MI:SS
   */
  public static String format(long val, boolean overDays) {
    return format(val, overDays, false);
  }

  /**
   * ��ʽ���ַ���
   * @param val (��)
   * @param overDays true:�������,false:24Сʱ����
   * @param enableNegative true:������,false:����ȡ��
   * @return HH24:MI:SS
   */
  public static String format(long val, boolean overDays,
			      boolean enableNegative) {
    String returnValue;
    long tmpVal;
    tmpVal = Math.abs(val / 3600);
    if (!overDays) {
      tmpVal %= 24;
    }
    String tmpStr = new Long(tmpVal).toString();
    if (tmpStr.length() < 2) {
      tmpStr = "0" + tmpStr;
    }

    returnValue = tmpStr + ":";
    tmpVal = Math.abs( (val % 3600) / 60);
    tmpStr = new Long(tmpVal).toString();
    if (tmpStr.length() < 2) {
      tmpStr = "0" + tmpStr;
    }
    returnValue += tmpStr + ":";

    tmpVal = Math.abs(val % 60);
    tmpStr = new Long(tmpVal).toString();
    if (tmpStr.length() < 2) {
      tmpStr = "0" + tmpStr;
    }
    returnValue += tmpStr;

    if (val < 0 && enableNegative) {
      returnValue = "-" + returnValue;

    }
    return returnValue;
  }

  public static boolean checkConflict(String startA, String endA, String startB,
				      String endB) {
    return checkConflict(startA, endA, "all", startB, endB, "all");
  }

  /**
   * �����ʱ����Ƿ��г�ͻ
   * @param sa
   * @param ea
   * @param sb
   * @param eb
   * @return
   */
  public static boolean checkConflict(String startA, String endA,
				      String weekDayA, String startB,
				      String endB, String weekDayB) {
    boolean returnValue = false;
    long sa = parse(startA);
    long ea = parse(endA);
    long sb = parse(startB);
    long eb = parse(endB);
    long wa = 0;
    long wb = 0;
    if (weekDayA.equalsIgnoreCase("all")) {
      wa = 8;
    }
    else {
      wa = new Long(weekDayA).longValue();
    }
    if (weekDayB.equalsIgnoreCase("all")) {
      wb = 8;
    }
    else {
      wb = new Long(weekDayB).longValue();
    }

    if (wa != 8 && wb != 8) {
      //�κ�һ��������ÿ�죬ֱ�ӱȽ�
      if (wa == 6 && wb == 0) {
	//��������������
	wb = 7;
      }
      if (wb == 6 && wa == 0) {
	//��������������
	wa = 7;
      }
      //�������һ�죬��Ȼ����ͻ
      if (Math.abs(wa - wb) > 1) {
	return false;
      }
      //��ȡ����ʱ���
      if (ea <= sa) {
	ea += 24 * 3600;
      }
      if (eb <= sb) {
	eb += 24 * 3600;
	//������ļ�24Сʱ
      }
      if (wa < wb) {
	sb += 24 * 3600;
	eb += 24 * 3600;
      }
      else if (wa > wb) {
	sa += 24 * 3600;
	ea += 24 * 3600;
      }
    }

    int sPos, ePos;
    sPos = isSecondBetween(sa, sb, eb, false);
    ePos = isSecondBetween(ea, sb, eb, true);

    if (sa < ea) {
      if (sb < eb) {
	//���ݻ򽻲�
	if (sPos == 0 || ePos == 0 || (sPos < 0 && ePos > 0)) {
	  returnValue = true;
	}
      }
      else {
	//���������
	if (sPos == 0 || ePos == 0) {
	  returnValue = true;
	}
      }
    }
    else {
      if (sb < eb) {
	//���ݻ򽻲�
	if (sPos <= 0 || ePos >= 0) {
	  returnValue = true;
	}
      }
      else {
	//������,һ����ͻ
	returnValue = true;
      }
    }
    return returnValue;
  }

  /**
   * ���һ��ʱ���Ƿ���ʱ�η�Χ��
   * @param checkSecond
   * @param startSecond
   * @param endSecond
   * @return
   */
  public static int isSecondBetween(long checkSecond, long startSecond,
				    long endSecond, boolean isCheckEnd) {
    int returnValue = 0;
    if (startSecond < endSecond) {
      if (isCheckEnd) {
	if (checkSecond <= startSecond) {
	  returnValue = -1;
	}
	if (checkSecond > endSecond) {
	  returnValue = 1;
	}
      }
      else {
	if (checkSecond < startSecond) {
	  returnValue = -1;
	}
	if (checkSecond >= endSecond) {
	  returnValue = 1;
	}
      }
    }
    else {
      if (isCheckEnd) {
	if (checkSecond > endSecond && checkSecond <= startSecond) {
	  returnValue = -1;
	}
      }
      else {
	if (checkSecond >= endSecond && checkSecond < startSecond) {
	  returnValue = -1;
	}
      }
    }
    return returnValue;
  }

  /**
   * ת��Ϊ�����ַ���
   * @return  HH24:MI:SS - HH24:MI:SS [1/2]
   */
  public String toString() {
    String returnValue = "";
    returnValue = format(startTime) + " - " + format(endTime) + " [" + type +
	"]\n";
    return returnValue;
  }

}
