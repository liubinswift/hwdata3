package com.viewscenes.web.Daoutil;

import java.net.*;
import java.sql.*;
import java.util.*;
import java.util.Map.*;

import org.jdom.*;
import org.jdom.input.*;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.pub.GDSet;
import com.viewscenes.web.Daoutil.ModuleException;
import com.viewscenes.web.Daoutil.SiteRunplanUtil;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.pub.GDSetException;


/**
 * <p>Title: վ��汾��顣</p>
 * ע�Ȿ��jspҳ����ʹ�ñȽ϶ࡣ����Ըߣ���ʱ���������ܹ��û������
 * ʹ��jspҳ���У�ʵʱ������ʵʱ����Jspҳ�档
 * <p>Description: վ��汾�ŵ���ȡ:
 * ���Դ����ݿ�����ȡ������Դ�����йء�
 * Ҳ������deviceconfig.xml�е�ֵ��</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author lixuefeng
 * @version 1.0
 */

public class SiteVersionUtil {
  private static Hashtable DEFAULT_PARAM_IDS = null;

//  private static HashMap SITECODE_VERTYPES = null;
  private static String[][] CENTER_LIST = null;
  private static HashMap CENTER_IDNAMES = null;
  private static HashMap CENTER_IDCODES = null;

//  private static HashMap SITEID_CODES = null;
  private static int siteInfoSize = 7;

  public SiteVersionUtil() {
  }

  /**
   * վ����Ϣ�ڴ�����ࡣ
   */
//  public static synchronized void loadSiteInfo() {
//    SITECODE_VERTYPES = new HashMap();
//    SITEID_CODES = new HashMap();
//    String[] value = null; //0 version, 1 type_id, 2 shortname, 3 center_id ---->��д��code
//    DbComponent db = (DbComponent) DaoFactory.create(DaoFactory.DB_OBJECT);
//    //��д��������ϸ��Ϣ�Ķ��ա�
//    String taskCode =
//	"select upper(code) code,version,type_id,shortname,center_id,head_id,code site_code," +
//	"manufacturer,ip,is_online  from res_headend_tab where is_delete=0 ";
//    ResultSet resultCode = null;
//    //��д��������ϸ��Ϣ�Ķ��ա�
//    try {
//      resultCode = db.exeQuery(taskCode);
//      while (resultCode.next()) {
//	value = new String[siteInfoSize];
//	value[0] = resultCode.getString("version");
//	value[1] = resultCode.getString("type_id");
//	value[2] = resultCode.getString("shortname");
//	value[3] = resultCode.getString("center_id");
//	value[4] = resultCode.getString("head_id");
//	value[5] = resultCode.getString("site_code");
//	value[6] = resultCode.getString("manufacturer");
//	value[7] = resultCode.getString("ip");
//	value[8] = resultCode.getString("is_online");
//	SITECODE_VERTYPES.put(resultCode.getString("code"), value);
//
//	String headId = resultCode.getString("head_id");
//	String siteCode = resultCode.getString("site_code");
//	SITEID_CODES.put(headId, siteCode);
//      }
//      System.out.println("վ��ӳ�仺�����ݼ��سɹ���");
//    }
//    catch (Exception ex2) {
//      System.out.println("��ѯվ����Ϣ����");
//      ex2.printStackTrace();
//    }
//    finally {
//      if (resultCode != null) {
//	try {
//	  resultCode.close();
//	}
//	catch (Exception ex) {
//	}
//      }
//    }
//  }

  /**
   * ��ʼ������idӳ���ڴ�����ࡣ
   */
  public static synchronized void loadDefaulParamId() {
    if (DEFAULT_PARAM_IDS == null) {
      DEFAULT_PARAM_IDS = new Hashtable();
    }

    String infoHead = null;

    String task =
        "select *  from RADIO_EQU_INIT_CONFIG_TAB";
    GDSet result = null;
    try {
      result = DbComponent.Query(task);
     for(int i=0;i<result.getRowCount();i++) {
        //���ݽṹ����ز�Ϊ�ա�
        String version = result.getString(i,"VERSION");
        if (version != null) {
          String[] vers = version.split(",");
          if (vers != null) {
            for (int vi = 0; vi < vers.length; vi++) {
              infoHead = result.getString(i,"CENTER_ID") + "_" +
                  vers[vi] + "_" + result.getString(i,"HEAD_TYPE_ID") + "_" +
                  result.getString(i,"MANUFACTURER");
              DEFAULT_PARAM_IDS.put(infoHead,
                                    result.getString(i,"PARAM_ID"));
            }
          }
        }
      }
    }
    catch (Exception ex2) {
      System.out.println("��ѯ�豸��ʼ������Ϣ����");
      ex2.printStackTrace();
    }


  }

  /**
   * վ����Ϣ��ȡ�ࡣ
   * @param siteCode String
   * @return String[]
   */
  public static String[] getSiteInfoByCode(String siteCode) {

    //��д��������ϸ��Ϣ�Ķ��ա�
    String taskCode =
        "select version,type_id,shortname,head_id,code site_code," +
        "manufacturer,ip  from res_headend_tab where is_delete=0 and upper(code) ='" +
        siteCode.toUpperCase() + "' ";
    GDSet resultCode = null;
    //��д��������ϸ��Ϣ�Ķ��ա�
    try {
      resultCode = DbComponent.Query(taskCode);
      if (resultCode.getRowCount()>0) {
        String[] siteInfo = new String[siteInfoSize];
        siteInfo[0] = resultCode.getString(0,"version");
        siteInfo[1] = resultCode.getString(0,"type_id");
        siteInfo[2] = resultCode.getString(0,"shortname");
        siteInfo[3] = resultCode.getString(0,"head_id");
        siteInfo[4] = resultCode.getString(0,"site_code");
        siteInfo[5] = resultCode.getString(0,"manufacturer");
        siteInfo[6] = resultCode.getString(0,"ip");
        return siteInfo;
      }
    }
    catch (Exception ex2) {
      System.out.println("��ѯվ����Ϣ����");
      ex2.printStackTrace();
    }

    return new String[siteInfoSize];
  }

  /**
   * ����վ�����õ��汾�š����� 5 6 7��
   * @param siteCode
   * @return
   * @throws java.lang.Exception
   */
  public static int getSiteVersion(String siteCode) {
    int siteVer = 5; //4,5,6,7������Ӧ�汾�š�
    if (siteCode != null) {
      String version = getSiteInfoByCode(siteCode)[0];
      if (version != null) {
        siteVer = new Integer(version.substring(1)).intValue();
      }
    }
    return siteVer;
  }

  /**
   * ����վ�����õ��汾�š����� 5 6 7��
   * @param siteCode
   * @return
   * @throws java.lang.Exception
   */
  public static Integer getSiteVerInteger(String siteCode) {
    Integer siteVer = new Integer(5); //4,5,6,7������Ӧ�汾�š�
    if (siteCode != null) {
      String version = getSiteInfoByCode(siteCode)[0];
      if (version != null) {
        siteVer = new Integer(version.substring(1));
      }
    }
    return siteVer;
  }

  /**
   * ����վ�����õ��汾�š�V5 V6 V7��
   * @param siteCode
   * @return
   * @throws java.lang.Exception
   */
  public static String getSiteVerStr(String siteCode) {
    String siteVer = "V5";
    if (siteCode != null) {
      siteVer = getSiteInfoByCode(siteCode)[0];
    }
    if (siteVer == null) {
      siteVer = "V5";
    }
    return siteVer.toUpperCase();
  }

  /**
   * ����վ�����õ� վ������ 101�ɼ��� 102 ң��վ��
   * @param siteCode
   * @return
   * @throws java.lang.Exception
   */
  public static String getSiteType(String siteCode) {
    String siteType = "";
    if (siteCode != null) {
      siteType = getSiteInfoByCode(siteCode)[1];
    }
    if (siteType == null) {
      siteType = "";
    }
    return siteType;
  }

  /**
   * ����վ�����õ�  վ�����ơ�
   * @param siteCode
   * @return
   * @throws java.lang.Exception
   */
  public static String getSiteName(String siteCode) {
    String siteName = "";
    if (siteCode != null) {
      siteName = getSiteInfoByCode(siteCode)[2];
    }
    if (siteName == null) {
      siteName = "";
    }
    return siteName;
  }

  /**
   * ����վ�����õ�  վ������̨��
   * @param siteCode
   * @return
   * @throws java.lang.Exception
   */
//  public static String getSiteCenId(String siteCode) {
//    String siteCenId = "100";
//    if (siteCode != null) {
//      siteCenId = getSiteInfoByCode(siteCode)[3];
//    }
//    if (siteCenId == null) {
//      siteCenId = "100";
//    }
//    return siteCenId;
//  }

  /**
   * ����վ�����õ�  վ��Id��
   * @param siteCode
   * @return
   * @throws java.lang.Exception
   */
  public static String getSiteHeadId(String siteCode) {
    String siteId = "";
    if (siteCode != null) {
      siteId = getSiteInfoByCode(siteCode)[3];
    }
    if (siteId == null) {
      siteId = "";
    }
    return siteId;
  }

  /**
   * ����վ��������� �õ�  ���ݿ���վ��ԭ���롣
   * @param siteCode
   * @return
   * @throws java.lang.Exception
   */
  public static String getSiteOriCode(String siteCode) {
    String siteOriCode = "";
    if (siteCode != null) {
      siteOriCode = getSiteInfoByCode(siteCode)[4];
    }
    if (siteOriCode == null) {
      siteOriCode = "";
    }
    return siteOriCode;
  }

  /**
   * ����վ��������� �õ�  ���ݿ���վ�㳧�̡�
   * @param siteCode
   * @return
   * @throws java.lang.Exception
   */
  public static String getSiteManu(String siteCode) {
    String siteManu = "";
    if (siteCode != null) {
      siteManu = getSiteInfoByCode(siteCode)[5];
    }
    if (siteManu == null) {
      siteManu = "";
    }
    return siteManu;
  }

  /**
   * ����̨id��վ��汾��equ_init_parm.properties��ȡĬ�ϲ���id��
   * @param centerId String
   * @param siteCode String
   * @return String
   */
  public static String getDefaultParamId(String centerId, String siteCode) {
    String paramId = "0";
    if (DEFAULT_PARAM_IDS == null) {
      loadDefaulParamId();
    }
    if (DEFAULT_PARAM_IDS != null) {
      if (siteCode != null && siteCode.length() > 0) {
        String[] siteInfo = getSiteInfoByCode(siteCode);
        paramId = (String) DEFAULT_PARAM_IDS.get(centerId + "_" + siteInfo[0] +
                                                 "_" + siteInfo[1] + "_" +
                                                 siteInfo[6]);
      }
    }
    return paramId;
  }

  /**
   * ��ȡվ��  ��Ӧ ����ͼƵ�����顣
   * @param siteCode String
   * @param band String
   * @throws ModuleException
   * @return String[]
   */
  public static String[] getSiteFreqs(String siteCode) throws
      ModuleException {
    String[] siteFreqs = null;
    if (siteCode != null && siteCode.length() > 0) {
      StringBuffer sql = new StringBuffer(" SELECT distinct frequency ")
          .append("  FROM zres_runplan_view tab where 1=1 ")
          .append(SiteRunplanUtil.getRunplanSqlByCode(siteCode))
          .append(" and VALID_END_TIME >=sysdate and ")
          .append(" is_predefine =0 AND is_confirm =1  ")
          .append(" and is_delete = 0  and timespan_type_id=1 ")
          .append("  order by frequency ");

      GDSet result = null;
      try {
        result = DbComponent.Query(sql.toString());

        int rowCount = result.getRowCount();
        if (rowCount > 0) {
          siteFreqs = new String[rowCount];
          for (int i = 0; i < rowCount; i++) {
            String freq = result.getString(i, "frequency");
            siteFreqs[i] = freq;
          }
        }
      }
      catch (DbException ex2) {
        System.out.println("��ѯƵ����Ϣ����");
        ex2.printStackTrace();
      }
      catch (GDSetException ex) {
        throw new ModuleException("��ѯƵ����Ϣ����", ex);
      }


    }
    return siteFreqs;
  }

  /**
   *
   * @param siteCode
   * @return
   * @throws java.lang.Exception
   */
  private static int getSiteVersion_lxfplay(String siteCode) throws
      Exception {
    int siteVer = 0; //4,5,6,7������Ӧ�汾�š�
    URL deviceConfig = SiteVersionUtil.class.getClassLoader().getResource(
        "deviceconfig.xml");
    SAXBuilder builder = new SAXBuilder(false);
    Document doc = builder.build(deviceConfig);
    Element root = doc.getRootElement();
    Element e = root.getChild("innerdevice");
    List devlist = e.getChildren();
    for (int i = 0; i < devlist.size(); i++) {
      Element sub = (Element) devlist.get(i);
      String tem = sub.getAttributeValue("code");
      if (tem.equalsIgnoreCase(siteCode)) {
        String siteType = sub.getAttributeValue("type");
        if (siteType.equalsIgnoreCase("radio7")) {
          siteVer = 7;
          return siteVer;
        }
        else if (siteType.equalsIgnoreCase("radio6")) {
          siteVer = 6;
          return siteVer;
        }
      }
    }
    return siteVer;
  }

  private static void LoadCenters() {
    if (CENTER_LIST == null || CENTER_IDNAMES == null || CENTER_IDCODES == null) {

      GDSet data1 = null;
      try {
          String sql=" select center_id,name,code from res_center_tab  where center_id!=100 order by center_id asc";

        data1 = DbComponent.Query(sql);
        int rowCount1 = data1.getRowCount();
        if (rowCount1 >= 0) {
          CENTER_LIST = new String[2][rowCount1];
          CENTER_IDNAMES = new HashMap();
          CENTER_IDCODES = new HashMap();
          for (int i = 0; i < rowCount1; i++) {
            try {
              CENTER_LIST[0][i] = data1.getString(i, "center_id");
              CENTER_LIST[1][i] = data1.getString(i, "name");
              CENTER_IDNAMES.put(data1.getString(i, "center_id"),
                                 data1.getString(i, "name"));
              CENTER_IDCODES.put(data1.getString(i, "center_id"),
                                 data1.getString(i, "code"));
            }
            catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  /**
   * ���������б�. λ��0�̶������ĵģ������ȡ���ĵĴ�1��ʼ��
   * @param
   * @return
   */
  public static String[][] get_center_list() {
    LoadCenters();
    return CENTER_LIST;
  }

  /** add by kim 2006-3-21
   * ���ݿ�Ĳ�ѯ���ת��Hashtable[]�д�ţ��������
   * @param
   * @return
   */
  public static HashMap[] ResultSet2Hashtable(ResultSet ret) throws
      SQLException {
    HashMap[] hb = null;
    /********δ��ɱ��� kim***************/
    if (ret != null) {
      ResultSetMetaData md = ret.getMetaData();
      int nColumnCount = md.getColumnCount();
      if (nColumnCount <= 0) {
        return null;
      }
      String[] szColnmnName = new String[nColumnCount];
      for (int it = 0; it < nColumnCount; it++) {
        szColnmnName[it] = md.getColumnName(it + 1);
      }
      int row = 0;
      ret.beforeFirst();
      while (ret.next()) {
        row++;
      }
      ret.first();
      if (row <= 0) {
        return null;
      }
      hb = new HashMap[row];
      for (int i = 0; i < row; i++) {
        hb[i] = new HashMap();
        for (int k = 0; k < szColnmnName.length; k++) {
          String stmp = ret.getString(szColnmnName[k]);
          if (stmp == null) {
            stmp = "";
          }
          hb[i].put(szColnmnName[k], stmp);
        }
        ret.next();
      }
    }
    return hb;
  }

  public static String getCenNameById(String center_id) {
    LoadCenters();
    String center_name = "";
    if (CENTER_IDNAMES != null && center_id != null &&
        center_id.length() > 0) {
      center_name = (String) CENTER_IDNAMES.get(center_id);
    }
    return center_name;
  }

  public static String getCenCodeById(String center_id) {
    LoadCenters();
    String center_code = "";
    if (CENTER_IDCODES != null && center_id != null &&
        center_id.length() > 0) {
      center_code = (String) CENTER_IDCODES.get(center_id);
    }
    return center_code;
  }

  /**
   * վ��id_code��Ϣ�ڴ�����ࡣ
   */
  public static String getHeadCodeById(String headId) {

    //��д��������ϸ��Ϣ�Ķ��ա�
    String taskCode =
        "select code from res_headend_tab where is_delete=0 and head_id =" + headId;
    GDSet resultCode = null;
    try {
      resultCode = DbComponent.Query(taskCode);
      if (resultCode.getRowCount()>0) {
        return resultCode.getString(0,"code");
      }
    }
    catch (Exception ex2) {
      System.out.println("��ѯվ����Ϣ����");
      ex2.printStackTrace();
    }

    return "";
  }

}
