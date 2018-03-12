package com.viewscenes.web.common;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.jmask.web.controller.EXEException;

import com.viewscenes.bean.DicStateBean;
import com.viewscenes.bean.NotificationBean;
import com.viewscenes.bean.ResAntennaBean;
import com.viewscenes.bean.ResCityBean;
import com.viewscenes.bean.ResHeadendBean;
import com.viewscenes.bean.RunplanTypeBean;
import com.viewscenes.bean.SeasonBean;
import com.viewscenes.bean.ShiftLogBean;
import com.viewscenes.bean.ZdicLanguageBean;
import com.viewscenes.bean.dicManager.ServiceAreaBean;
import com.viewscenes.bean.runplan.RunplanBean;
import com.viewscenes.dao.XmlReader;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.pub.GDSet;
import com.viewscenes.pub.GDSetException;
import com.viewscenes.sys.SystemTableCache;
import com.viewscenes.sys.TableInfoCache;
import com.viewscenes.util.ChineseUtil;
import com.viewscenes.util.LogTool;
import com.viewscenes.util.StringTool;
import com.viewscenes.util.business.ReceiverListGetUtil;
import com.viewscenes.util.business.SimplePing;
import com.viewscenes.web.sysmgr.user.PubUserManagerServiceDao;

import flex.messaging.io.amf.ASObject;

public class Common {

    public static String cjdHeadCode="";//存放前端表采集点的所有code
    public static String ykzHeadCode="";//存放前端表遥控站的所有code
    public static ArrayList<Object> headendListDistinct;
    /**
     * 获取系统公共数据
     * @detail  大洲、站点、
     * @method
     * @param obj
     * @return
     * @return  Object
     * @author  zhaoyahui
     * @version 2012-7-19 上午10:30:17
     */
    public Object getConfigData(ASObject obj){
        ArrayList list = new ArrayList();
        ArrayList<DicStateBean> stateList = (ArrayList<DicStateBean>)getStateList(new ASObject());
        ArrayList<ResHeadendBean> headList = (ArrayList<ResHeadendBean>)getHeadend(new ASObject());
        HashMap<String,String> hm = new HashMap<String,String>();
        for(int j=0;j<stateList.size();j++){
            hm.put(stateList.get(j).getState(),stateList.get(j).getState_name());
        }
        for(int i=0;i<headList.size();i++){
            ResHeadendBean headbean = headList.get(i);
            headbean.setState_name((hm.get(headbean.getState()) == null?"":hm.get(headbean.getState())));
            headList.set(i,headbean);
        }
        list.add(stateList);
        list.add(headList);
        Collections.sort(headList,new HeadendSort(true));
        return list;
    }

    /**
     * 获取前端列表,返回的站点名称不带AB
     * <p>class/function:com.viewscenes.web.common
     * <p>explain:
     * <p>author-date:刘斌
     * @param:
     * @return:
     */
    public static String getHeadendnameNOABByCode(String headcode){

        ArrayList list = queryDataForCacheMap(headcode,SystemTableCache.RES_HEAD_END_TAB);
        ResHeadendBean head=(ResHeadendBean) list.get(0);
        if(headcode.endsWith("A")||headcode.endsWith("B")||headcode.endsWith("C")||headcode.endsWith("D")||headcode.endsWith("E")||headcode.endsWith("F")||headcode.endsWith("G"))
        {
            return head.getShortname().substring(0, head.getShortname().length()-1);
        }
        else
            return head.getShortname();
    }
    /**
     * 获取前端列表,不传headCode查询全部
     * <p>class/function:com.viewscenes.web.common
     * <p>explain:
     * <p>author-date:谭长伟 2012-7-17
     * @param:
     * @return:
     */
    public static Object getHeadendList(ASObject obj){
        ArrayList list = new ArrayList();
        String headCode = (String)obj.get("headCode");

        list = queryDataForCacheMap(headCode,SystemTableCache.RES_HEAD_END_TAB);
        return list;
    }
    /**
     * 获取前端列表,不传headCode查询全部
     * <p>class/function:com.viewscenes.web.common
     * <p>explain:
     * <p>author-date:谭长伟 2012-7-17
     * @param:
     * @return:
     */
    public static String getHeadendnameByCode(String headcode){


        ArrayList list = queryDataForCacheMap(headcode,SystemTableCache.RES_HEAD_END_TAB);
        ResHeadendBean head=(ResHeadendBean) list.get(0);
        return head.getShortname();
    }

    /**
     * 获取服务区列表,不传id查询全部
     * <p>class/function:com.viewscenes.web.common
     * <p>explain:
     * <p>author-date:张文2012-10-31
     * @param:
     * @return:
     */
    public Object getServiceList(ASObject obj){
        ArrayList list = new ArrayList();
        String id = (String)obj.get("id");

        list = queryDataForCacheMap(id,SystemTableCache.DIC_SERVICESAREA_TAB);

        return list;
    }

    public static ResHeadendBean getHeadendBeanByCode(String headcode){
        ArrayList list = queryDataForCacheMap(headcode,SystemTableCache.RES_HEAD_END_TAB);
        ResHeadendBean head=(ResHeadendBean) list.get(0);
        return head;
    }


    public static ResHeadendBean getHeadendListById(String head_id){
        ArrayList<ResHeadendBean> list = queryDataForCacheMap("",SystemTableCache.RES_HEAD_END_TAB);
        for(int i=0;i<list.size();i++){
            ResHeadendBean headendBean = list.get(i);
            return headendBean.getHead_id().equals(head_id)? headendBean:null;
        }
        return null;
    }

    /**
     * 获取前端列表,按HEAD_ID查询
     * <p>class/function:com.viewscenes.web.common
     * <p>explain:
     * <p>author-date:谭长伟 2012-7-17
     * @param:
     * @return:
     */
    public static Object getHeadendListById(ASObject obj){
        String head_id = (String)obj.get("head_id");
        return getHeadendListById(head_id);
    }

    /**
     * 获取城市列表,不传city_id查询全部
     * <p>class/function:com.viewscenes.web.common
     * <p>explain:
     * <p>author-date:谭长伟 2012-7-17
     * @param:
     * @return:
     */
    public Object getCityList(ASObject obj){
        ArrayList list = new ArrayList();
        String city_id = (String)obj.get("city_id");

        list = queryDataForCacheMap(city_id,SystemTableCache.RES_CITY_TAB);
        Collections.sort(list,new SortCommon());
        return list;
    }


    /**
     * 获取大洲列表
     * <p>class/function:com.viewscenes.web.common
     * <p>explain:
     * <p>author-date:谭长伟 2012-7-17
     * @param:
     * @return:
     */
    public Object getStateList(ASObject obj){
        ArrayList list = new ArrayList();
        String state_id = (String)obj.get("state_id");

        list = queryDataForCacheMap(state_id,SystemTableCache.DIC_STATE_TAB);
        Collections.sort(list,new SortCommon());
        return list;
    }


    /**
     * 发射台名称列表( 只包含名称)
     * <p>class/function:com.viewscenes.web.common
     * <p>explain:
     * <p>author-date:谭长伟 2012-7-19
     * @param:
     * @return:
     */
    public Object getStationList(){
        ArrayList list = new ArrayList();
        list = queryDataForCacheMap("",SystemTableCache.RES_TRANSMIT_STATION_TAB);
        Collections.sort(list,new SortCommon());
        return list;
    }


    /**
     * 获取运行图类型
     * <p>class/function:com.viewscenes.web.common
     * <p>explain:
     * <p>author-date:谭长伟 2012-7-19
     * @param:
     * @return:
     */
    public Object getRunplanType(){
        ArrayList list = new ArrayList();
        RunplanTypeBean bean = new RunplanTypeBean();
        bean.setRunplanTypeId("1");
        bean.setRunplanType("国际台运行图");

        RunplanTypeBean bean1 = new RunplanTypeBean();
        bean1.setRunplanTypeId("2");
        bean1.setRunplanType("国际台海外落地运行图");

        RunplanTypeBean bean2 = new RunplanTypeBean();
        bean2.setRunplanTypeId("3");
        bean2.setRunplanType("外国台运行图");

        list.add(bean);
        list.add(bean1);
        list.add(bean2);

        return list;
    }

    /**
     * 语言列表
     * <p>class/function:com.viewscenes.web.common
     * <p>explain:
     * <p>author-date:谭长伟 2012-7-19
     * @param:
     * @return:
     */
    public Object getLanguageList(ASObject obj){
        ArrayList list = new ArrayList();
        String language_id = (String)obj.get("language_id");
        list = queryDataForCacheMap(language_id,SystemTableCache.ZDIC_LANGUAGE_TAB);
        Collections.sort(list,new SortCommon());
        return list;
    }
    public Object getLanguageList( ){
        ArrayList list = new ArrayList();

        list = queryDataForCacheMap("",SystemTableCache.ZDIC_LANGUAGE_TAB);
        Collections.sort(list,new SortCommon());
        return list;
    }

    /**
     * 服务区列表
     * <p>class/function:com.viewscenes.web.common
     * <p>explain:
     * <p>author-date:王福祥2012-12-19
     * @param:
     * @return:
     */
    public Object getServiceAreaList(ASObject obj){
        ArrayList list = new ArrayList();
        String id = (String)obj.get("id");
        list = queryDataForCacheMap(id,SystemTableCache.DIC_SERVICESAREA_TAB);
        Collections.sort(list,new SortCommon());
        return list;
    }

    /**
     * 节目类型信息
     * @return
     * @author 王福祥
     * @date 2012/09/06
     */
    public Object getProgramType(){
        ArrayList list = new ArrayList();
        String sql="select * from dic_runplan_type_tab";
        try {
            GDSet gd = DbComponent.Query(sql);
            if(gd.getRowCount()>0){
                for(int i=0;i<gd.getRowCount();i++){
                    RunplanTypeBean bean = new RunplanTypeBean();
                    bean.setRunplanTypeId(gd.getString(i, "runplan_type_id"));
                    bean.setRunplanType(gd.getString(i, "type"));
                    list.add(bean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    /**
     * 季节代号
     * @return list
     * @author 王福祥
     * @date 2012/09/06
     */
    public Object getSeasonList(){
        ArrayList list = new ArrayList();
        String sql="select * from dic_season_tab order by  is_now ,end_time desc  ";
        try {
            GDSet gd = DbComponent.Query(sql);
            if(gd.getRowCount()>0){
                for(int i=0;i<gd.getRowCount();i++){
                    SeasonBean bean = new SeasonBean();
                    bean.setCode(gd.getString(i, "code"));
                    bean.setStart_time(gd.getString(i, "start_time"));
                    bean.setEnd_time(gd.getString(i, "end_time"));
                    bean.setIs_now(gd.getString(i, "is_now"));
                    list.add(bean);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return list;
    }
    /**
     * 获得国家列表
     * @return
     *  @author 王福祥
     * @date 2012/09/07
     */
    public Object getCountryList(){
        ArrayList list = new ArrayList();
        String sql="select distinct(t.contry) as country from res_city_tab t order by contry";
        try {
            GDSet gd = DbComponent.Query(sql);
            if(gd.getRowCount()>0){
                for(int i=0;i<gd.getRowCount();i++){

                    list.add(gd.getString(i, "country"));
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Collections.sort(list,new SortCommon());
        return list;
    }
    /**
     * 通过大洲获得国家列表(无大洲信息返回全部国家）
     * @return
     *  @author 贾朝辉
     * @date 2013/01/09
     */
    public Object getCountryList(ASObject obj){
        ArrayList list = new ArrayList();
        String subSql=" where 1=1 ";
        String continents_id=(String)obj.get("continents_id");
        if(continents_id!=null&&continents_id.length()!=0){
            subSql += " and t.continents_id = "+continents_id;
        }
        String sql="select distinct(t.contry) as country  from res_city_tab t"+subSql;
        sql+=" order by t.contry  ";
        try {
            GDSet gd = DbComponent.Query(sql);
            if(gd.getRowCount()>0){
                for(int i=0;i<gd.getRowCount();i++){
                    list.add(gd.getString(i, "country"));
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Collections.sort(list,new SortCommon());
        return list;
    }
    /**
     * 根据国家名返回相应信息（默认返回空）
     * @param obj
     * @return
     */
    public ArrayList<Object> getContinentsIdByCountry(ASObject obj){
        String country=(String)obj.get("country");
        ArrayList<Object> result=new ArrayList<Object>();
        if(country!=null&&country.length()!=0){
            String sql="select distinct(t.contry) ,continents_id ,capital ,default_language from res_city_tab t where contry ='"+country+"'";
            try {
                GDSet gd=DbComponent.Query(sql);
                if(gd.getRowCount()>0){
                    result.add(Integer.parseInt(gd.getString(0, 1)));
                    result.add(gd.getString(0, 2));
                    result.add(gd.getString(0, 3));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return result;
    }
    /**
     * 根据发射台名称 ，方向，天线号自动获取天线程式
     * @author 王福祥
     * @date 2012/09/13
     * @param obj
     * @return
     */
    public Object getAntennaType(ASObject obj){
        String station_name = (String) obj.get("station_name");
        String direction = (String) obj.get("direction");
        String antenna = (String) obj.get("antenna");
        String antennaType="";
        String sql = "select antenna_mode from res_antenna_tab where station_name='"+station_name+"' and direction='"+direction+"' and antenna_no='"+antenna+"'";
        try {
            GDSet gd = DbComponent.Query(sql);
            if(gd.getRowCount()>0){
                antennaType = gd.getString(0, "antenna_mode");
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new EXEException("","根据天线号获取天线程式异常："+e.getMessage(),"");
        }

        return antennaType;
    }

    /**
     * 获取指定表名的缓存,key为空,返回全部值
     * <p>class/function:com.viewscenes.web.common
     * <p>explain:
     * <p>author-date:谭长伟 2012-7-17
     * @param:
     * @return:
     */
    public static  ArrayList queryDataForCacheMap(String key,String tableName){
        ArrayList list = new ArrayList();
        Map<String,Object> map = SystemTableCache.getDataMap(tableName);

        if (key != null && !key.equals("")){
            Object tBean = (Object)map.get(key);

            list.add(tBean);

        }else{
            Set<String> set = map.keySet();

            TreeMap treemap = new TreeMap(map);
            Set mappings = treemap.entrySet();
//		      System.out.println("object \t\t\tkey\t\tvalue"); 
            //iterate through mappings and print content
            for (Iterator i = mappings.iterator(); i.hasNext();) {
                Map.Entry me = (Map.Entry)i.next();
//		         Object ok = me.getKey(); 
                Object ov = me.getValue();
                list.add(ov);
            }

//			for(Iterator<String> it = set.iterator(); it.hasNext();){
//				
//				String _key = it.next();
//				
//				T tBean = (T)map.get(_key);
//				
//				list.add(tBean);
//			}
        }
        return list;
    }

    /**
     * 根据前端code返回此站点对应的 运行图。
     * <p>class/function:com.viewscenes.web.common
     * <p>explain:
     * <p>author-date:刘斌  2012-7-17
     * @param:
     * @return:
     */
    public Object getRunplanListByHeadCode(ASObject obj){
        ArrayList<RunplanBean> list = new ArrayList<RunplanBean>();
        String stationCode = (String)obj.get("stationCode");
        String sql="select t.runplan_id,t.runplan_type_id,t.station_id,t.transmiter_no,t.freq,t.valid_start_time," +
                "t.valid_end_time,t.direction,t.power,t.service_area,t.antennatype,t.rest_datetime,t.rest_time,t.sentcity_id,t.start_time," +
                "t.end_time,t.satellite_channel,t.store_datetime,t.program_type_id,t.language_id," +
                "t.weekday,t.input_person,t.revise_person,t.remark,t.program_id,t.mon_area," +
                "t.band,t.program_type,t.redisseminators,t.local_time,t.summer,t.summer_starttime," +
                "t.summer_endtime,t.season_id,t.antenna,t.station_name,t.ciraf," +
                "head.shortname,city.city sendcity,lang.language_name,pro.program_name,runtype.type,decode(t.runplan_type_id,1,'国际台运行图',2,'海外落地',3,'外国台运行图') runplanType" +
                " from zres_runplan_tab t,res_headend_tab head ,res_city_tab city ," +
                " zdic_language_tab lang,zdic_program_name_tab pro ,dic_runplan_type_tab runtype " +
                "where t.is_delete = 0 and t.mon_area like '%"+stationCode+"%'" +
                " and head.code='"+stationCode+"'  and t.sentcity_id=city.id" +
                " and t.language_id=lang.language_id" +
                " and t.program_id=pro.program_id(+)  and t.program_type=runtype.runplan_type_id" +
                " and t.valid_end_time>sysdate order by t.runplan_type_id asc  ";
        GDSet gdSet = null;
        try {
            gdSet = DbComponent.Query(sql);
            for (int i = 0; i < gdSet.getRowCount(); i++) {
                RunplanBean runplanBean=new RunplanBean();
                runplanBean.setRunplan_id(gdSet.getString(i, "runplan_id"));
                runplanBean.setRunplan_type_id(gdSet.getString(i, "runplan_type_id"));
                runplanBean.setStation_id(gdSet.getString(i, "station_id"));
                runplanBean.setTransmiter_no(gdSet.getString(i,"transmiter_no"));
                runplanBean.setFreq(gdSet.getString(i, "freq"));
                runplanBean.setValid_start_time(gdSet.getString(i, "valid_start_time"));
                runplanBean.setValid_end_time(gdSet.getString(i, "valid_end_time"));
                runplanBean.setDirection(gdSet.getString(i, "direction"));
                runplanBean.setPower(gdSet.getString(i, "power"));
                runplanBean.setService_area(gdSet.getString(i, "service_area"));
                runplanBean.setAntennatype(gdSet.getString(i, "antennatype"));
                runplanBean.setRest_datetime(gdSet.getString(i, "rest_datetime"));
                runplanBean.setRest_time(gdSet.getString(i, "rest_time"));
                runplanBean.setSentcity_id(gdSet.getString(i, "sentcity_id"));
                runplanBean.setStart_time(gdSet.getString(i, "start_time"));
                runplanBean.setEnd_time(gdSet.getString(i, "end_time"));
                runplanBean.setSatellite_channel(gdSet.getString(i, "satellite_channel"));
                runplanBean.setStore_datetime(gdSet.getString(i, "store_datetime"));
                runplanBean.setProgram_type_id(gdSet.getString(i, "program_type_id"));
                runplanBean.setLanguage_id(gdSet.getString(i, "language_id"));
                runplanBean.setWeekday(gdSet.getString(i, "weekday"));
                runplanBean.setInput_person(gdSet.getString(i, "input_person"));
                runplanBean.setRevise_person(gdSet.getString(i, "revise_person"));
                runplanBean.setRemark(gdSet.getString(i, "remark"));
                runplanBean.setProgram_id(gdSet.getString(i, "program_id"));
                runplanBean.setMon_area(gdSet.getString(i, "mon_area"));
                runplanBean.setBand(gdSet.getString(i, "band"));
                runplanBean.setProgram_type(gdSet.getString(i, "program_type"));
                runplanBean.setRedisseminators(gdSet.getString(i, "redisseminators"));
                runplanBean.setLocal_time(gdSet.getString(i, "local_time"));
                runplanBean.setSummer(gdSet.getString(i, "summer"));
                runplanBean.setSummer_starttime(gdSet.getString(i, "summer_starttime"));
                runplanBean.setSummer_endtime(gdSet.getString(i, "summer_endtime"));
                runplanBean.setSeason_id(gdSet.getString(i, "season_id"));
                runplanBean.setAntenna(gdSet.getString(i, "antenna"));
                runplanBean.setStation_name(gdSet.getString(i, "station_name"));
                runplanBean.setCiraf(gdSet.getString(i, "ciraf"));
                runplanBean.setShortname(gdSet.getString(i, "shortname"));
                runplanBean.setSendcity(gdSet.getString(i, "sendcity"));
                runplanBean.setLanguage_name(gdSet.getString(i, "language_name"));
                runplanBean.setProgram_name(gdSet.getString(i, "program_name"));
                runplanBean.setType(gdSet.getString(i, "type"));
                runplanBean.setRunplanType(gdSet.getString(i, "runplanType"));

                list.add(runplanBean);

            }
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new EXEException("", "[站点:" + stationCode
                    + "]查询运行图信息实失败|" + e.getMessage(),
                    null);
        } catch (GDSetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new EXEException("", "[站点:" + stationCode
                    + "]查询运行图信息实失败|" + e.getMessage(),
                    null);
        }


        return list;

    }

    /**
     * 获取接收机列表
     * @detail
     * @method
     * @return
     * @return  Object
     * @author  zhaoyahui
     * @version 2012-8-10 上午10:04:15
     */
    public Object getEquList(String headendCode){
        Vector equlist = new Vector();
        try {
            equlist = ReceiverListGetUtil.getReceiverList(headendCode, true);
        } catch (Exception e) {
            LogTool.fatal("查询接收机错误:", e);
            return new EXEException("","查询接收机错误","");
        }

        ArrayList equList = new ArrayList();
        for (int j = 0; j < equlist.size(); j = j + 2) {
            ASObject equi = new ASObject();
            equi.put("label",  (String) equlist.get(j + 1));
            equi.put("data",  (String) equlist.get(j));
            equList.add(equi);
        }

        return equList;
    }


    public Object getHeadend(ASObject obj){
        ArrayList list = new ArrayList();
        String sql="select * from res_headend_tab t where t.is_delete=0 order by t.state, t.shortname asc";
        try {
            GDSet gd=DbComponent.Query(sql);
            list= TableInfoCache.gdSetToPojoList(gd, ResHeadendBean.class.getName());
//			if(gd.getRowCount()>0){
//				for(int i=0;i<gd.getRowCount();i++){
//					ResHeadendBean bean = new ResHeadendBean();
//					bean.setType_id(gd.getString(i, "type_id"));
//					bean.setHead_id(gd.getString(i, "head_id"));
//					bean.setCode(gd.getString(i, "code"));
//					bean.setShortname(gd.getString(i, "shortname"));
//					bean.setIs_online(gd.getString(i, "is_online"));
//				//	bean.setCom_id(gd.getString(i, "com_id"));
//				//	bean.setCom_protocol(gd.getString(i, "com_protocol"));
//					bean.setIp(gd.getString(i, "ip"));
//				//	bean.setLatitude(gd.getString(i, "latitude"));
//				//	bean.setLongitude(gd.getString(i, "longitude"));
//				//	bean.setComphone(gd.getString(i, "comphone"));
//				//	bean.setSite(gd.getString(i, "site"));
//					bean.setAddress(gd.getString(i, "address"));
//					bean.setSite_status(gd.getString(i, "site_status"));
//			//		bean.setCom_status(gd.getString(i, "com_status"));
//			//		bean.setFault_status(gd.getString(i, "fault_status"));
//				//	bean.setStation_name(gd.getString(i, "station_name"));
//					 list.add(bean);
//				}
//			}
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new EXEException("",e.getMessage(),"");
        }

        return list;
    }

    /**
     * 根据发射台名称查询发射台id
     * @param stationName
     * @return
     */
    public static String getStationIDByName(String stationName){
        String station_id="";
        String sql="select station_id from res_transmit_station_tab where name like '%"+stationName+"%' and is_delete=0 ";
        try {
            GDSet gd = DbComponent.Query(sql);
            if(gd.getRowCount()>0){
                station_id = gd.getString(0, "station_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return station_id;

    }
    /**
     * 根据语言名称查询id
     * @param name
     * @return
     */
    public static String getLanguageIDByName(String name){
        String language_id="";
        String sql="select language_id from zdic_language_tab where language_name='"+name+"' and is_delete=0 ";
        try {
            GDSet gd = DbComponent.Query(sql);
            if(gd.getRowCount()>0){
                language_id=gd.getString(0, "language_id");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return language_id;
    }
    /**
     * 根据用户 id查询是否在页面进行通知的显示。
     * @param
     * @return
     */
    public Object getMessageData(ASObject obj){
        String user_id = (String)obj.get("userid");
        String sql="select t.* from res_natification_tab t,sec_user_tab us,sec_user_role_rel_tab ro" +
                " where t.to_userid like '%'||ro.role_id||'%'and us.user_id=ro.user_id " +
                " and t.is_delete=0 and us.user_id="+user_id+" and t.end_time>sysdate " +
                "  and (instr(t.check_userid, us.user_id) < 1 or t.check_userid is null ) order by t.id asc ";
        ArrayList<NotificationBean> list = new ArrayList();
        GDSet gdSet;
        try {
            gdSet = DbComponent.Query(sql);
            for (int i = 0; i < gdSet.getRowCount(); i++) {
                NotificationBean task=new NotificationBean();
                task.setId(gdSet.getString(i, "id"));
                task.setCheck_userid(gdSet.getString(i, "check_userid"));
                task.setCreate_time(gdSet.getString(i, "create_time"));
                task.setDescription(gdSet.getString(i, "description"));
                task.setEnd_time(gdSet.getString(i, "end_time"));
                task.setFrom_userid(gdSet.getString(i, "from_userid"));
                task.setStart_time(gdSet.getString(i, "start_time"));
                task.setTo_userid(gdSet.getString(i, "to_userid"));
                task.setFrom_username(gdSet.getString(i, "from_username"));
                task.setTo_username(gdSet.getString(i, "to_username"));
                list.add(task);
            }
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new EXEException("", "查询通知信息失败！"+ e.getMessage(),null);
        } catch (GDSetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new EXEException("", "查询通知信息失败！"+ e.getMessage(),null);
        }

        return list;
    }
    /**
     * 根据用户 id查询是否在页面进行通知的显示。
     * @param
     * @return
     */
    public Object getShiftLogData(ASObject obj){

        String sql=" select * from res_shift_log_tab t where t.end_time>sysdate and t.douser_name is null order by t.id  ";
        ArrayList list = new ArrayList();
        GDSet gdSet;
        try {
            gdSet = DbComponent.Query(sql);
            for (int i = 0; i < gdSet.getRowCount(); i++) {
                ShiftLogBean task=new ShiftLogBean();
                task.setId(gdSet.getString(i, "id"));
                task.setCreate_time(gdSet.getString(i, "create_time"));
                task.setDescription(gdSet.getString(i, "description"));
                task.setDouser_name(gdSet.getString(i, "douser_name"));
                task.setEnd_time(gdSet.getString(i, "end_time"));
                task.setFrom_username(gdSet.getString(i, "from_username"));
                task.setStart_time(gdSet.getString(i, "start_time"));
                list.add(task);
            }
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new EXEException("", "查询交接班信息失败！"+ e.getMessage(),null);
        } catch (GDSetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new EXEException("", "查询交接班信息失败！"+ e.getMessage(),null);
        }

        return list;
    }
    /**
     * 将遥控站和采集点的code存放到公共变量里面
     * @author 王福祥
     * @date 2012/11/15
     */
    public void getHeadCode(){
        String sql="select type_id,code from res_headend_tab where is_delete=0 order by code ";
        try {
            GDSet gd = DbComponent.Query(sql);
            if(gd.getRowCount()>0){
                for(int i=0;i<gd.getRowCount();i++){
                    if(gd.getString(i, "type_id").equalsIgnoreCase("101")){
                        cjdHeadCode+=gd.getString(i, "code")+",";
                    }else{
                        ykzHeadCode+=gd.getString(i, "code")+",";
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    /**add by 王福祥
     * @date 2012/12/13
     * 为其它模块提供查询运行图信息
     * @param bean
     * @return
     */
    public Object getAllRunplan(RunplanBean bean){
        ASObject resObj=null;
        ArrayList result;
        String freq = bean.getFreq();
        String starttime = bean.getStart_time();
        String endtime = bean.getEnd_time();
        String headendCode = bean.getShortname();//暂时用这个字段传递code，关联查询mon_area和xg_mon_area
        String runplanType=bean.getRunplan_type_id();
        String sql="select  t.runplan_id,t.runplan_type_id,t.station_id,t.transmiter_no,t.freq,t.valid_start_time,t.valid_end_time,t.direction," +
                "t.power,t.service_area,t.antennatype,t.rest_datetime,t.rest_time,t.sentcity_id,t.start_time,t.end_time,t.satellite_channel,t.store_datetime," +
                "t.program_type_id,t.language_id,t.weekday,t.input_person,t.revise_person,t.remark,t.program_id,t.mon_area,t.is_delete,t.band,t.program_type," +
                "t.redisseminators,t.local_start_time,t.summer,t.summer_starttime,t.summer_endtime,t.season_id,t.antenna,t.station_name,t.ciraf," +
                "decode(t.launch_country,'',c.contry,t.launch_country) launch_country,t.broadcast_country,t.broadcast_station,t.xg_mon_area,t.disturb,t.local_end_time"+
                ",decode(t.runplan_type_id ,1,'国际台运行图',2,'海外落地运行图',3,'外国台运行图') as runplantype," +
                "zlt.language_name ,c.city sendcity ,c.contry from zres_runplan_tab t ,zdic_language_tab zlt,res_city_tab c " +
                "where t.is_delete = 0 and t.language_id=zlt.language_id and t.sentcity_id=c.id(+) and  t.valid_end_time>=sysdate and t.valid_start_time <=sysdate and t.is_delete=0";
        if(freq!=null&&!freq.equalsIgnoreCase("")){
            sql+=" and t.freq="+freq+"";
        }
        if(runplanType!=null&&!runplanType.equals("")){
            sql+=" and t.runplan_type_id='"+runplanType+"'";
        }
        if(headendCode!=null&&!headendCode.equals("")){
            sql+=" and (t.mon_area like '%"+headendCode+"%' or t.xg_mon_area like '%"+headendCode+"%') ";
        }
        if(!starttime.equals("") && !endtime.equals("")){
            sql +=  " and to_char(t.start_time) <= to_char('"+endtime+"') ";
            sql +=  " and to_char(t.end_time ) >= to_char('"+starttime+"') ";
        }

        sql += " order by t.freq";
        try {
            resObj=StringTool.pageQuerySql(sql, bean);

        } catch (Exception e) {
            LogTool.fatal(e);
            return new EXEException("", e.getMessage(), "");
        }

        return resObj;
    }
    /**
     * add by 王福祥
     * @date 2012/12/19
     * 从当前有效的运行图中读取所有的播音时段
     * @return
     */
    public Object getBroadcastTime(String runplan_type){
        ArrayList list = new ArrayList();
        String sql="select distinct(t.start_time||'-'||t.end_time) as broadcasttime from zres_runplan_tab t where t.is_delete=0 and t.valid_end_time>=sysdate and t.runplan_type_id='"+runplan_type+"' order by broadcasttime";
        try {
            GDSet gd = DbComponent.Query(sql);
            if(gd.getRowCount()>0){
                for(int i=0;i<gd.getRowCount();i++){
                    list.add(gd.getString(i, "broadcasttime"));

                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 查询站点CODE和名称 ，AB对应的站点用一个表示
     * @detail
     * @method
     * @param
     * @return
     * @return  Object
     * @author  zhaoyahui
     * @version 2012-9-27 下午03:29:01
     */
    public Object getHeadendListDistinct(ResHeadendBean bean){
        String headtype = bean.getType_id();

        StringBuffer sql = new StringBuffer("select distinct x.* from (");
        sql.append(" select   (decode(t.type_id||t.version, '102V8', substr(t.shortname, 0, length(t.shortname) - 1), t.shortname)) as shortname_noab  ,");
        sql.append(" 		          t.state,t.type_id,t.country,");
        sql.append(" 		          state.state_name,");
        sql.append(" 		          decode(t.type_id||t.version, '102V8', substr(t.code, 0, length(t.code) - 1), t.code) as code_noab");
        sql.append(" 		  from res_headend_tab t, dic_state_tab state");
        sql.append(" 		 where t.is_delete = 0");
        sql.append(" 		   and t.state = state.state");
        sql.append(" 		 order by t.code ");
        sql.append(" 		 ) x order by x.code_noab ");

//		StringBuffer sql = new StringBuffer("select distinct (decode(t.type_id,102,substr(t.shortname, 0, length(t.shortname) - 1),t.shortname)) as shortname_noab,t.*,");
//					sql.append(" state.state_name ,");
//		             sql.append(" decode(t.type_id,102,substr(t.code, 0, length(t.code) - 1),t.code) as code_noab from res_headend_tab t,dic_state_tab state  where t.is_delete = 0  and  t.state=state.state");
        if(headtype != null && !headtype.equalsIgnoreCase("") && !headtype.equalsIgnoreCase("all")){
            sql.append(" and type_id='"+headtype+"'");
        }
        try {
            GDSet gd = DbComponent.Query(sql.toString());


            headendListDistinct = TableInfoCache.gdSetToPojoList(gd, ResHeadendBean.class.getName());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new EXEException("",e.getMessage(),"");
        }
        //	Collections.sort(headendListDistinct,new HeadendSort(false));
        return headendListDistinct;
    }

    /**
     * 查询用户可以使用的功能菜单
     * @detail
     * @method
     * @param obj
     * @return
     * @return  Object
     * @author  zhaoyahui
     * @version 2012-8-28 下午03:54:26
     */
    public Object getUserMenu(ASObject obj){

        String userName = (String)obj.get("userName");
        String roleId = (String)obj.get("roleId");
        String broadcast_type = (String)obj.get("broadcast_type");
        if(broadcast_type == null)
            broadcast_type = "0";
        try {
            GDSet gd =  null;
            if(userName.equalsIgnoreCase("admin")){
                gd = PubUserManagerServiceDao.getFuncSetByAdmin(broadcast_type);
            } else{
                gd = PubUserManagerServiceDao.getFuncSetByRoleId(roleId,broadcast_type);
            }

            ArrayList rootList = new ArrayList();

            Map nodeMap = new HashMap();
            for(int i=0; i<gd.getRowCount(); i++){
                String funcId = gd.getString(i, "operation_id");
                String name = gd.getString(i, "operation_name");
                String levels = "1";
                if(Integer.parseInt(funcId)<100){
                    levels = "0";
                }
                String parentFuncId = gd.getString(i, "parent_operation_id");
                String showFlag = gd.getString(i, "show_flag");
                String showName = gd.getString(i, "show_name");
                String classPath = gd.getString(i, "class_path");
                String iconSource = gd.getString(i, "icon_source");
                String fastIconSource = gd.getString(i, "fast_icon_source");
                String funcType = gd.getString(i, "func_type");

                if(!funcType.equals("1")){ //不是菜单功能
                    continue;
                }
                if("0".equals(showFlag))
                    continue;

                Element node = new Element("node");
                node.addAttribute(new Attribute("id",funcId));

                node.addAttribute(new Attribute("label",name));

                node.addAttribute(new Attribute("showLabel",showName));

                if(classPath != null && !classPath.equals("")){
                    node.addAttribute(new Attribute("classPath",classPath));
                }
                node.addAttribute(new Attribute("source",iconSource));

                node.addAttribute(new Attribute("fastSource",fastIconSource));

                nodeMap.put(funcId, node);

                if(nodeMap.containsKey(parentFuncId)){
                    Element parentNode = (Element)nodeMap.get(parentFuncId);

                    List childList = parentNode.getChildren();

                    childList.add(node);
                }
                if(levels.equals("0")){
                    rootList.add(node);
                }
            }

            Element root1 = new Element("menu");
            root1.addAttribute("id", "0");
            Document doc = new Document(root1);

            Element root = doc.getRootElement();

            root.setChildren(rootList);

            XMLOutputter out = new XMLOutputter("  ", true, "GB2312");

            StringWriter sw = new StringWriter();

            try {

                out.output(doc, sw);

            } catch (IOException ex2) {

                ex2.printStackTrace();
            }
            System.out.println(sw.toString());
            return sw.toString();


        } catch (Exception e) {
            LogTool.fatal(e);

            return new EXEException("","读取用户权限异常"+e.getMessage(),"");
        }

    }
    /**
     * ************************************************

     * @Title: checkSystemRunning

     * @Description: TODO(网络运行状态监测)

     * @param @param obj
     * @param @return    设定文件

     * @author  刘斌

     * @return Object    返回类型

     * @throws

     ************************************************
     */
    public Object checkSystemRunning(ASObject obj) {
        String str="";
        List list=XmlReader.getAttrValueList("NetCheck", "para", "label","ip", "key");
        for(int i=0;i<list.size();i++)
        {
            List list2= (List) list.get(i);
            String label=list2.get(0).toString();
            String ip= list2.get(1).toString();
            String key= list2.get(2).toString();
            if (SimplePing.ping(ip)) { // 联通
                str+=label+",1"+"&&";

            }
            else {
                str+=label+",0"+"&&";
            }
        }
        return str;
    }



    /**
     * 可对有AB的站点和没有AB的站点进行排序
     * hasAB:是否有AB站点
     * @author thinkpad
     *
     */
    static class HeadendSort extends SortCommon{
        boolean hasAB = false;
        public HeadendSort(boolean hasAB){
            this.hasAB =  hasAB;
        }
        @Override
        public int compare(Object o1, Object o2) {
            // TODO Auto-generated method stub

            ResHeadendBean s1 = (ResHeadendBean) o1;
            ResHeadendBean s2 = (ResHeadendBean) o2;
            if (hasAB){
                return ChineseUtil.cn2py(s1.getShortname().substring(0,1)).compareTo(ChineseUtil.cn2py(s2.getShortname().substring(0,1)));

            }else{
                return ChineseUtil.cn2py(s1.getShortname_noab().substring(0,1)).compareTo(ChineseUtil.cn2py(s2.getShortname_noab().substring(0,1)));

            }
        }


    }

    /**
     * 支持发射台、语言、大州、服务区、城市排序
     * @author thinkpad
     *
     */
    static class SortCommon implements Comparator{

        @Override
        public int compare(Object o1, Object o2) {
            // TODO Auto-generated method stub

            if (o1 instanceof ServiceAreaBean && o2 instanceof ServiceAreaBean){
                ServiceAreaBean s1 = (ServiceAreaBean) o1;
                ServiceAreaBean s2 = (ServiceAreaBean) o2;

                return ChineseUtil.cn2py(s1.getChinese_name().substring(0,1)).compareTo(ChineseUtil.cn2py(s2.getChinese_name().substring(0,1)));
            }else if (o1 instanceof DicStateBean && o2 instanceof DicStateBean){
                DicStateBean s1 = (DicStateBean) o1;
                DicStateBean s2 = (DicStateBean) o2;

                return ChineseUtil.cn2py(s1.getState_name()).compareTo(ChineseUtil.cn2py(s2.getState_name()));
            }else if (o1 instanceof ResAntennaBean && o2 instanceof ResAntennaBean){
                ResAntennaBean s1 = (ResAntennaBean) o1;
                ResAntennaBean s2 = (ResAntennaBean) o2;

                return ChineseUtil.cn2py(s1.getAntenna_no()).compareTo(ChineseUtil.cn2py(s2.getAntenna_no()));
            }else if (o1 instanceof ResCityBean && o2 instanceof ResCityBean){
                ResCityBean s1 = (ResCityBean) o1;
                ResCityBean s2 = (ResCityBean) o2;

                return ChineseUtil.cn2py(s1.getContry().substring(0,1)).compareTo(ChineseUtil.cn2py(s2.getContry().substring(0,1)));
            }else if (o1 instanceof ZdicLanguageBean && o2 instanceof ZdicLanguageBean){
                ZdicLanguageBean s1 = (ZdicLanguageBean) o1;
                ZdicLanguageBean s2 = (ZdicLanguageBean) o2;

                return ChineseUtil.cn2py(s1.getLanguage_name().substring(0,1)).compareTo(ChineseUtil.cn2py(s2.getLanguage_name().substring(0,1)));
            }else if (o1 instanceof com.viewscenes.bean.dicManager.StationBean && o2 instanceof com.viewscenes.bean.dicManager.StationBean){
                com.viewscenes.bean.dicManager.StationBean s1 = (com.viewscenes.bean.dicManager.StationBean) o1;
                com.viewscenes.bean.dicManager.StationBean s2 = (com.viewscenes.bean.dicManager.StationBean) o2;

                return ChineseUtil.cn2py(s1.getName().substring(0,1)).compareTo(ChineseUtil.cn2py(s2.getName().substring(0,1)));
            }

            return 0;
        }

    }

    public static GDSet getTimeBySeason(String seasonCode){
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
    /**
     * 根据站点code获取站点名称
     * @param code
     * @return
     */
    public static String getShortNameByCode(String code){
        String shortName="";
        String sql="select distinct (decode(t.type_id || t.version,'102V8',substr(t.shortname, 0, length(t.shortname) - 1),t.shortname)) as shortname " +
                "from res_headend_tab t where is_delete=0 and upper(t.code) like upper('%"+code+"%')";
        try {
            GDSet gd = DbComponent.Query(sql);
            if(gd.getRowCount()>0){
                shortName = gd.getString(0, "shortname");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shortName;

    }

    /**
     * 根据发射国家计算当地时差
     * @author 王福祥
     * @param startTime
     * @param endTime
     * @param launch_country
     * @return
     */
    public static String[] getTimeDif(String startTime,String endTime,String launch_country){
        String[] s=new String[2];
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String sql=" select moveut from res_city_tab where moveut is not null and contry like '%"+launch_country+"%' ";
        double moveut=0; //时差
        try {
            Date date1=sdf.parse(startTime);
            Date date2=sdf.parse(endTime);
            GDSet gd = DbComponent.Query(sql);
            if(gd.getRowCount()>0){
                moveut = Double.parseDouble(gd.getString(0, "moveut"));
                long time1=(long) (date1.getTime()+(moveut*3600*1000));
                long time2=(long) (date2.getTime()+(moveut*3600*1000));
                date1.setTime(time1);
                date2.setTime(time2);
                s[0]=sdf.format(date1);
                s[1]=sdf.format(date2);
            }else{
                s[0]=startTime;
                s[1]=endTime;
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return s;
    }
    /**
     * 查询转播机构信息
     * @return
     */
    public ArrayList queryRedisseminators(ASObject obj){
        ArrayList list = new ArrayList();
        String sql=" select t.redisseminators from dic_redisseminators_tab t order by t.redisseminators ";
        try {
            GDSet gd = DbComponent.Query(sql);
            if(gd.getRowCount()>0){
                for(int i=0;i<gd.getRowCount();i++){
                    list.add(gd.getString(i, "redisseminators"));
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return list;
    }

}
