package com.viewscenes.web.sysmgr.dicManager;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Element;
import org.jmask.web.controller.EXEException;

import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbException;

import com.viewscenes.printexcel.JExcel;
import com.viewscenes.pub.GDSet;
import com.viewscenes.pub.GDSetException;
import com.viewscenes.util.StringTool;
import com.viewscenes.web.sysmgr.dicManager.DicDao;
import com.viewscenes.web.sysmgr.dicManager.DicService;
import com.viewscenes.bean.ResHeadendBean;
import com.viewscenes.sys.TableInfoCache;

import flex.messaging.io.amf.ASObject;

public class HeadendTab {

	/**
	 * 
	 * <p>
	 * class/function:com.viewscenes.web.sysmgr.dicManager
	 * <p>
	 * explain:查询前端数据 ，如果查询出错会返回错误信息
	 * <p>
	 * author-date:张文 2012-7-26
	 * 
	 * @param:
	 * @return:
	 */

	public Object getHeadend(ASObject obj) {
		return new DicService().getHeadend(obj);
	}
	/**
	 * 添加站点 遥控站一次添加4个(A,B,C,D);
	 * @param obj
	 * @return
	 */
	public Object insertHeadend(ASObject obj){
		ArrayList<Object> list = new ArrayList<Object>();
		String type_id = (String) obj.get("type_id");
		String version = (String) obj.get("version");
		try {
//			if(type_id.equals("102")&&version.equals("V8")){
//				String code=(String)obj.get("code");
//				String shortname=(String)obj.get("shortname");
//				obj.put("code", code+"A");
//				obj.put("shortname", shortname+"A");
//				obj.put("manufacturer", "圣世祺_遥控站_NI_V8");
//				AddHead(obj);
//				obj.put("code", code+"B");
//				obj.put("shortname", shortname+"B");
//				obj.put("manufacturer", "圣世祺_遥控站_FHD_V8");
//				AddHead(obj);
//				obj.put("code", code+"C");
//				obj.put("shortname", shortname+"C");
//				obj.put("manufacturer", "圣世祺_遥控站_713_V8");
//				AddHead(obj);
//				obj.put("code", code+"D");
//				obj.put("shortname", shortname+"D");
//				obj.put("manufacturer", "圣世祺_遥控站_NI_V8");
//				AddHead(obj);
//			}else{
				
				AddHead(obj);
			//}

		} catch (DbException e) {
			e.printStackTrace();
			EXEException ex = new EXEException("", "," + e.getMessage(), "");
			list.add(ex);
		} catch (Exception e) {
			e.printStackTrace();
			return new EXEException("", "后台错误!", null);
		}
		return list;
	}
	/**
	 * 
	 * <p>
	 * class/function:com.viewscenes.web.sysmgr.dicManager
	 * <p>
	 * explain:添加前端数据，如果添加出错会返回错误信息
	 * <p>
	 * author-date:张文 2012-7-26
	 * 
	 * @param:
	 * @return:
	 */

	public void AddHead(ASObject asObj) throws Exception {
		
		StringBuffer insertSql = new StringBuffer();
		String val = getHeadNextVal();
		String code = (String) asObj.get("code");
		String shortname = (String) asObj.get("shortname");
		String com_id = (String) asObj.get("com_id");
		String com_protocol = (String) asObj.get("com_protocol");
		String ip = (String) asObj.get("ip");
		String longitude = (String) asObj.get("longitude");
		String latitude = (String) asObj.get("latitude");
		String comphone = (String) asObj.get("comphone");
		String site = (String) asObj.get("site");
		String address = (String) asObj.get("address");
		String site_status = (String) asObj.get("site_status");
		
		String com_status = (String) asObj.get("com_status");
		String fault_status = (String) asObj.get("fault_status");
		String station_name = (String) asObj.get("station_name");
		String descript = (String) asObj.get("descript");
		String state = (String) asObj.get("state");

		String country = (String) asObj.get("country");
		String version = (String) asObj.get("version");
		String occur_time = (String) asObj.get("occur_time");
		String altitude = (String) asObj.get("altitude");
		String summertime=(String)asObj.get("summertime");
//		String summertime_begin = (String) asObj.get("summertime_begin");
//
//		String summertime_end = (String) asObj.get("summertime_end");
		String ciraf = (String) asObj.get("ciraf");
		String person = (String) asObj.get("person");
		String person_phone = (String) asObj.get("person_phone");
		String principal = (String) asObj.get("principal");
		String principal_phone = (String) asObj.get("principal_phone");
		String time_diff = (String) asObj.get("time_diff");
		String default_language = (String) asObj.get("default_language");
		String x = (String) asObj.get("x");

		String y = (String) asObj.get("y");
		String url = (String) asObj.get("url");
//		String is_delete = (String) asObj.get("is_delete");

		String type_id = (String) asObj.get("type_id");
		String is_online = (String) asObj.get("is_online");
		String manufacturer = (String) asObj.get("manufacturer");
		String post = (String) asObj.get("post");
		String service_name = (String) asObj.get("service_name");
		service_name=service_name.replace(',', '.');
		insertSql
				.append(
						"insert into res_headend_tab(head_id,code,com_id,com_protocol,ip,longitude,latitude,comphone,site,address,site_status,com_status,fault_status,")
				.append(
						"station_name,descript,state,country,version,occur_time,altitude,summertime,ciraf,person,")
				.append(
						"person_phone,principal,principal_phone,time_diff,default_language,x,y,url,is_delete,type_id,is_online,manufacturer,shortname,post,service_name) ")
				.append("values(").append(val).append(",'").append(
						code.toUpperCase()).append("','").append(com_id)
				.append("','").append(com_protocol).append("','").append(ip)
				.append("','").append(longitude).append("','").append(latitude)
				.append("','").append(comphone).append("',").append("'")
				.append(site).append("','").append(address).append("','")
				.append(site_status).append("','").append(com_status).append(
						"','").append(fault_status).append("','").append(
						station_name).append("','").append(descript).append(
						"','").append(state).append("',").append("'").append(
						country).append("','").append(version).append(
						"',to_date('").append(occur_time).append(
						"','yyyy-mm-dd hh24:mi:ss'),'").append(altitude)
				.append("','").append(summertime)
				.append("','")
				.append(ciraf).append("','").append(person).append("',")
				.append("'").append(person_phone).append("','").append(
						principal).append("','").append(principal_phone)
				.append("','").append(time_diff).append("','").append(
						default_language).append("','").append(x).append("','")
				.append(y).append("','").append(url).append("',").append("'")
				.append(0).append("','").append(type_id).append("','")
				.append(is_online).append("','").append(manufacturer).append(
						"','").append(shortname).append("','").append(post)
				.append("','").append(service_name + "')");

		
			DbComponent.exeUpdate(insertSql.toString());
			ResHeadendBean ResHeadendBean = new ResHeadendBean();
			ResHeadendBean.setHead_id(val);
			ResHeadendBean.setCode(code);
			ResHeadendBean.setShortname(shortname);
			ResHeadendBean.setManufacturer(manufacturer);
			ResHeadendBean.setIs_online(is_online.toString());
			ResHeadendBean.setType_id(type_id.toString());
			ResHeadendBean.setUrl(url);
			ResHeadendBean.setState_name(station_name);
			ResHeadendBean.setCom_id(com_id);
			ResHeadendBean.setCom_protocol(com_protocol);
			ResHeadendBean.setIp(ip);
			ResHeadendBean.setLongitude(longitude);
			ResHeadendBean.setLatitude(latitude);
			ResHeadendBean.setComphone(comphone);
			ResHeadendBean.setSite(site);
			ResHeadendBean.setAddress(address);
			ResHeadendBean.setSite_status(site_status);
			ResHeadendBean.setCom_status(com_status);
			ResHeadendBean.setFault_status(fault_status);
			ResHeadendBean.setStation_name(station_name);
			ResHeadendBean.setDescript(descript);
			ResHeadendBean.setState(state);
			ResHeadendBean.setCountry(country);
			ResHeadendBean.setOccur_time(occur_time);
			ResHeadendBean.setAltitude(altitude);
			
			ResHeadendBean.setSummertime(summertime);
			ResHeadendBean.setCiraf(ciraf);
			ResHeadendBean.setPerson(person);
			ResHeadendBean.setPerson_phone(person_phone);
			ResHeadendBean.setPrincipal(principal);
			ResHeadendBean.setPrincipal_phone(principal_phone);
			ResHeadendBean.setTime_diff(time_diff);
			ResHeadendBean.setIs_delete("0");
			ResHeadendBean.setDefault_language(default_language);
			ResHeadendBean.setX(x);
			ResHeadendBean.setY(y);
			ResHeadendBean.setVersion(version);
			ResHeadendBean.setVersion(post);
			ResHeadendBean.setVersion(service_name);
			TableInfoCache as = new TableInfoCache();
			as.refreshTableMap("res_headend_tab", ResHeadendBean,
					ResHeadendBean.getCode(), "1");
	}
	/**
	 * 修改站点 遥控站一次修改4个(A,B,C,D);
	 * @param obj
	 * @return
	 */
	public Object updateHeadend(ASObject obj){
		ArrayList<Object> list = new ArrayList<Object>();
		String type_id = (String) obj.get("type_id");
		String version = (String) obj.get("version");
		try {
//			if(type_id.equals("102")&&version.equals("V8")){
//				String code=(String)obj.get("code");
//				String shortname=(String)obj.get("shortname");
//				obj.put("code", code+"A");
//				obj.put("shortname", shortname+"A");
//				obj.put("manufacturer", "圣世祺_遥控站_NI_V8");
//				updateHead(obj);
//				obj.put("code", code+"B");
//				obj.put("shortname", shortname+"B");
//				obj.put("manufacturer", "圣世祺_遥控站_FHD_V8");
//				updateHead(obj);
//				obj.put("code", code+"C");
//				obj.put("shortname", shortname+"C");
//				obj.put("manufacturer", "圣世祺_遥控站_713_V8");
//				updateHead(obj);
//				obj.put("code", code+"D");
//				obj.put("shortname", shortname+"D");
//				obj.put("manufacturer", "圣世祺_遥控站_NI_V8");
//				updateHead(obj);
//			}else{			
				updateHead(obj);
		//	}

		} catch (DbException e) {
			e.printStackTrace();
			EXEException ex = new EXEException("", "," + e.getMessage(), "");
			list.add(ex);
		} catch (Exception e) {
			e.printStackTrace();
			return new EXEException("", "后台错误!", null);
		}
		return list;
	}
	/**
	 * 
	 * <p>
	 * class/function:com.viewscenes.web.sysmgr.dicManager
	 * <p>
	 * explain:更新前端数据，如果添加出错会返回错误信息
	 * <p>
	 * author-date:张文 2012-7-26
	 * 
	 * @param:
	 * @return:
	 * @throws Exception 
	 */

	private  void updateHead(ASObject asObj) throws Exception {
		
		String updateSql = "";
		String head_id = (String) asObj.get("head_id");
		String code = (String) asObj.get("code");
		String shortname = (String) asObj.get("shortname");
		String com_id = (String) asObj.get("com_id");
		String com_protocol = (String) asObj.get("com_protocol");
		String ip = (String) asObj.get("ip");
		String longitude = (String) asObj.get("longitude");
		String latitude = (String) asObj.get("latitude");
		String comphone = (String) asObj.get("comphone");
		String site = (String) asObj.get("site");
		String address = (String) asObj.get("address");
		String site_status = (String) asObj.get("site_status");
		String com_status = (String) asObj.get("com_status");
		String fault_status = (String) asObj.get("fault_status");
		String station_name = (String) asObj.get("station_name");
		String descript = (String) asObj.get("descript");
		String state = (String) asObj.get("state");
		if (state != "")
			Integer.valueOf(state);
		String country = (String) asObj.get("country");
		String version = (String) asObj.get("version");
		String occur_time = (String) asObj.get("occur_time");
		String altitude = (String) asObj.get("altitude");
		String summertime=(String)asObj.get("summertime");
//		String summertime_begin = (String) asObj.get("summertime_begin");
//
//		String summertime_end = (String) asObj.get("summertime_end");
		String ciraf = (String) asObj.get("ciraf");
		String person = (String) asObj.get("person");
		String person_phone = (String) asObj.get("person_phone");
		String principal = (String) asObj.get("principal");
		String principal_phone = (String) asObj.get("principal_phone");
		String time_diff = (String) asObj.get("time_diff");
		String default_language = (String) asObj.get("default_language");
		String x = (String) asObj.get("x");
		String y = (String) asObj.get("y");
		String url = (String) asObj.get("url");
	
		String type_id = (String) asObj.get("type_id");
		if (type_id != "")
			Integer.valueOf(type_id);
		String is_online = (String) asObj.get("is_online");
		if (is_online != "")
			Integer.valueOf(is_online);
		String manufacturer = (String) asObj.get("manufacturer");
		String post = (String) asObj.get("post");
		String service_name = (String) asObj.get("service_name");
		service_name=service_name.replace(',', '.');
		updateSql = "update res_headend_tab set " 
				+ "shortname='" + shortname + "',com_id='" + com_id
				+ "',com_protocol='" + com_protocol + "', ip='" + ip
				+ "', longitude='" + longitude + "'," + "latitude='" + latitude
				+ "',comphone='" + comphone + "',site='" + site + "',address='"
				+ address + "',site_status='" + site_status + "',com_status='"
				+ com_status + "'," + "fault_status='" + fault_status
				+ "',station_name='" + station_name + "',descript='" + descript
				+ "',state='" + state + "',country='" + country + "',version='"
				+ version + "'," + "occur_time=to_date('" + occur_time
				+ "','yyyy-mm-dd hh24:mi:ss'),altitude='" + altitude
				+ "',summertime='" + summertime
				+ "',ciraf='" + ciraf
				+ "',person='" + person + "',person_phone='" + person_phone
				+ "'," + "principal='" + principal + "',principal_phone='"
				+ principal_phone + "',time_diff='" + time_diff
				+ "',default_language='" + default_language + "'," + "x='" + x
				+ "',y='" + y + "',url='" + url 
				+ "',type_id='" + type_id + "',is_online='" + is_online
				+ "',manufacturer='" + manufacturer + "'," + "post='" + post
				+ "',service_name='" + service_name + "' where code ='"
				+ code.toUpperCase() + "'";
		
			DbComponent.exeUpdate(updateSql);
			ResHeadendBean ResHeadendBean = new ResHeadendBean();
			ResHeadendBean.setHead_id(head_id);
			ResHeadendBean.setCode(code);
			ResHeadendBean.setShortname(shortname);
			ResHeadendBean.setManufacturer(manufacturer);
			ResHeadendBean.setIs_online(is_online);
			ResHeadendBean.setType_id(type_id);
			ResHeadendBean.setUrl(url);
			ResHeadendBean.setState_name(station_name);
			ResHeadendBean.setCom_id(com_id);
			ResHeadendBean.setCom_protocol(com_protocol);
			ResHeadendBean.setIp(ip);
			ResHeadendBean.setLongitude(longitude);
			ResHeadendBean.setLatitude(latitude);
			ResHeadendBean.setComphone(comphone);
			ResHeadendBean.setSite(site);
			ResHeadendBean.setAddress(address);
			ResHeadendBean.setSite_status(site_status);
			ResHeadendBean.setCom_status(com_status);
			ResHeadendBean.setFault_status(fault_status);
			ResHeadendBean.setStation_name(station_name);
			ResHeadendBean.setDescript(descript);
			ResHeadendBean.setState(state);
			ResHeadendBean.setCountry(country);
			ResHeadendBean.setOccur_time(occur_time);
			ResHeadendBean.setAltitude(altitude);
			ResHeadendBean.setSummertime(summertime);
			ResHeadendBean.setCiraf(ciraf);
			ResHeadendBean.setPerson(person);
			ResHeadendBean.setPerson_phone(person_phone);
			ResHeadendBean.setPrincipal(principal);
			ResHeadendBean.setPrincipal_phone(principal_phone);
			ResHeadendBean.setTime_diff(time_diff);
			ResHeadendBean.setDefault_language(default_language);
			ResHeadendBean.setX(x);
			ResHeadendBean.setY(y);
			ResHeadendBean.setVersion(version);
			ResHeadendBean.setVersion(post);
			ResHeadendBean.setVersion(service_name);
			TableInfoCache as = new TableInfoCache();
			as.refreshTableMap("res_headend_tab", ResHeadendBean,
					ResHeadendBean.getCode(), "2");

		
		
	}

	/**
	 * 
	 * <p>
	 * class/function:com.viewscenes.web.sysmgr.dicManager
	 * <p>
	 * explain:删除前端数据，如果添加出错会返回错误信息
	 * <p>
	 * author-date:张文 2012-7-26
	 * 
	 * @param:
	 * @return:
	 */
	public Object deleteHead(ASObject asObj) {
		DicDao asDao = new DicDao();
		String dellist = (String) asObj.get("dellist");

		try {
			System.out.println("dellist=" + dellist);
			asDao.deleteHead(dellist);
			dellist=dellist.replaceAll("V8", "");
			ResHeadendBean ResHeadendBean = new ResHeadendBean();
			TableInfoCache as = new TableInfoCache();
			String[] delArr = dellist.split(",");
			for (int i = 0; i < delArr.length; i++) {
				ResHeadendBean.setCode(delArr[i].split("'")[1]);
				as.refreshTableMap("res_headend_tab", ResHeadendBean,
						ResHeadendBean.getCode(), "3");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("", "后台错误!", null);
		}
		return " ";
	}

	/**
	 * 
	 * <p>
	 * class/function:com.viewscenes.database.service.usermanager
	 * <p>
	 * explain:取得添加前端的序列
	 * <p>
	 * author-date:谭长伟 2012-3-3
	 * 
	 * @param:
	 * @return:
	 */
	private static String getHeadNextVal() throws DbException, GDSetException {

		String sql = " select RES_RESOURSE_SEQ.nextval val from dual ";

		GDSet set = DbComponent.Query(sql);

		String val = set.getString(0, "val");

		return val;
	}

	public void getHeadendExcel(String msg, HttpServletRequest request,
			HttpServletResponse response) {
		Element root = StringTool.getXMLRoot(msg);
		String fileName = "站点信息";
		String downFileName = "站点信息";
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("0", "亚洲");
		map.put("1", "欧洲");
		map.put("2", "非洲");
		map.put("3", "北美洲");
		map.put("4", "南美洲");
		map.put("5", "大洋洲");
		map.put("6", "南极洲");
		String type_id = root.getChildText("sType");
		String is_online = root.getChildText("runType");
		String shortname = root.getChildText("shortname");
		String code = root.getChildText("code");
		String codes = root.getChildText("codes");
		String state = root.getChildText("state");
		String sql = "select * from res_headend_tab  where is_delete=0";
		if(codes.length()>0){
			//sql +=" and decode(type_id||version, '102V8', substr(code, 0, length(code)-1),code) in ("+codes+")";		
		
			sql +=" and code in ("+codes+")";		
			
		}else{
			if (type_id.length() > 0)
				sql += " and type_id  =" + type_id ;
		
			if (is_online.length() > 0) {
				sql += " and is_online ='" + is_online + "'";
			}
			if (shortname.length() > 0)
				sql += " and shortname like ('%" + shortname + "%')";
			if (state.length() > 0) {
				sql += " and state = '" + state + "'";
			}
		}
		sql += " order by shortname desc ";
		try {
			JExcel jExcel = new JExcel();
			downFileName = jExcel.openDocument();
			jExcel.WorkBookGetSheet(0);
			GDSet gd = DbComponent.Query(sql);
			int n = gd.getRowCount();
			if (n > 0) {
				for (int i = 0; i < n; i++) {
					jExcel.addDate(0, i + 1, gd.getString(i, "code"),
							jExcel.dateCellFormat);
					jExcel.addDate(1, i + 1, gd.getString(i, "shortname"),
							jExcel.dateCellFormat);
					jExcel.addDate(2, i + 1, gd.getString(i, "country"),
							jExcel.dateCellFormat);
					jExcel.addDate(3, i + 1, map.get(gd.getString(i, "state")),
							jExcel.dateCellFormat);
					jExcel.addDate(4, i + 1, gd.getString(i, "service_name"),
							jExcel.dateCellFormat);
					jExcel.addDate(5, i + 1, gd.getString(i, "address"),
							jExcel.dateCellFormat);
					jExcel.addDate(6, i + 1, gd.getString(i, "longitude"),
							jExcel.dateCellFormat);
					jExcel.addDate(7, i + 1, gd.getString(i, "latitude"),
							jExcel.dateCellFormat);
					jExcel.addDate(8, i + 1, gd.getString(i, "altitude"),
							jExcel.dateCellFormat);
					jExcel.addDate(9, i + 1, gd.getString(i, "x"),
							jExcel.dateCellFormat);
					jExcel.addDate(10, i + 1, gd.getString(i, "y"),
							jExcel.dateCellFormat);
					jExcel.addDate(11, i + 1, gd.getString(i,
							"default_language"), jExcel.dateCellFormat);
					jExcel.addDate(12, i + 1, gd.getString(i, "ciraf"),
							jExcel.dateCellFormat);
					jExcel.addDate(13, i + 1, gd.getString(i, "time_diff"),
							jExcel.dateCellFormat);
					jExcel.addDate(14, i + 1, gd.getString(i, "comphone"));
					jExcel.addDate(15, i + 1, gd.getString(i, "person"),
							jExcel.dateCellFormat);
					jExcel.addDate(16, i + 1, gd.getString(i, "person_phone"));
					jExcel.addDate(17, i + 1, gd.getString(i, "principal"),
							jExcel.dateCellFormat);
					jExcel.addDate(18, i + 1, gd.getString(i, "post"),
							jExcel.dateCellFormat);
					String phone=gd.getString(i, "principal_phone");
					String[] ss=getPhoneNumbers(phone);
					jExcel.addDate(19, i + 1, ss[0]);
					jExcel.addDate(20, i + 1, ss[1]);
					jExcel.addDate(21, i + 1, ss[2]);
					jExcel.addDate(22, i + 1, gd.getString(i, "url"),
							jExcel.dateCellFormat);
					if (gd.getString(i, "com_id").equals("1")) {
						jExcel.addDate(23, i + 1, "宽带", jExcel.dateCellFormat);
					} else {
						jExcel.addDate(23, i + 1, "拨号", jExcel.dateCellFormat);
					}
					jExcel.addDate(24, i + 1, gd.getString(i, "com_protocol"),
							jExcel.dateCellFormat);
					jExcel.addDate(25, i + 1, gd.getString(i, "ip"),
							jExcel.dateCellFormat);
					if(gd.getString(i, "site_status").equals("0")){
						jExcel.addDate(26, i + 1, "正常",
								jExcel.dateCellFormat);
					}else{
						jExcel.addDate(26, i + 1, "异常",
								jExcel.dateCellFormat);
					}
					
//					jExcel.addDate(27, i + 1, gd.getString(i, "com_status"),
//							jExcel.dateCellFormat);
//					jExcel.addDate(28, i + 1, gd.getString(i, "fault_status"),
//							jExcel.dateCellFormat);
//					if (gd.getString(i, "is_delete").equals("0")) {
//						jExcel.addDate(29, i + 1, "否", jExcel.dateCellFormat);
//					} else {
//						jExcel.addDate(29, i + 1, "是", jExcel.dateCellFormat);
//					}
					if (gd.getString(i, "type_id").equals("101")) {
						jExcel.addDate(27, i + 1, "采集点", jExcel.dateCellFormat);
					} else {
						jExcel.addDate(27, i + 1, "遥控站", jExcel.dateCellFormat);
					}
					if (gd.getString(i, "is_online").equals("1")) {
						jExcel.addDate(28, i + 1, "在线", jExcel.dateCellFormat);
					} else {
						jExcel.addDate(28, i + 1, "不在线", jExcel.dateCellFormat);
					}
					if(gd.getString(i,"summertime").equals("0")){
						jExcel.addDate(29, i + 1, "无", jExcel.dateCellFormat);
					}else{
						jExcel.addDate(29, i + 1, "有", jExcel.dateCellFormat);
					}
//					jExcel.addDate(33, i + 1,
//							gd.getString(i, "summertime_end"),
//							jExcel.dateCellFormat);
					jExcel.addDate(30, i + 1, gd.getString(i, "manufacturer"),
							jExcel.dateCellFormat);
					jExcel.addDate(31, i + 1, gd.getString(i, "version"),
							jExcel.dateCellFormat);
					jExcel.addDate(32, i + 1, gd.getString(i, "descript"),
							jExcel.dateCellFormat);
					jExcel.addDate(33, i + 1, gd.getString(i, "head_id"),
							jExcel.dateCellFormat);
				}
			}
			jExcel.getWorkSheet().setRowView(0, 600);
			jExcel.getWorkSheet().setColumnView(0, 20);
			jExcel.getWorkSheet().setColumnView(1, 20);
			jExcel.getWorkSheet().setColumnView(2, 20);
			jExcel.getWorkSheet().setColumnView(3, 20);
			jExcel.getWorkSheet().setColumnView(4, 20);
			jExcel.getWorkSheet().setColumnView(5, 20);
			jExcel.getWorkSheet().setColumnView(6, 20);
			jExcel.getWorkSheet().setColumnView(7, 20);
			jExcel.getWorkSheet().setColumnView(8, 20);
			jExcel.getWorkSheet().setColumnView(9, 20);
			jExcel.getWorkSheet().setColumnView(10, 20);
			jExcel.getWorkSheet().setColumnView(11, 20);
			jExcel.getWorkSheet().setColumnView(12, 20);
			jExcel.getWorkSheet().setColumnView(13, 20);
			jExcel.getWorkSheet().setColumnView(14, 20);
			jExcel.getWorkSheet().setColumnView(15, 20);
			jExcel.getWorkSheet().setColumnView(16, 20);
			jExcel.getWorkSheet().setColumnView(17, 20);
			jExcel.getWorkSheet().setColumnView(18, 20);
			jExcel.getWorkSheet().setColumnView(19, 20);
			jExcel.getWorkSheet().setColumnView(20, 20);
			jExcel.getWorkSheet().setColumnView(21, 20);
			jExcel.getWorkSheet().setColumnView(22, 20);
			jExcel.getWorkSheet().setColumnView(23, 20);
			jExcel.getWorkSheet().setColumnView(24, 20);
			jExcel.getWorkSheet().setColumnView(25, 20);
			jExcel.getWorkSheet().setColumnView(26, 20);
			jExcel.getWorkSheet().setColumnView(27, 20);
			jExcel.getWorkSheet().setColumnView(28, 20);
			jExcel.getWorkSheet().setColumnView(29, 20);
			jExcel.getWorkSheet().setColumnView(30, 20);
			jExcel.getWorkSheet().setColumnView(31, 20);
			jExcel.getWorkSheet().setColumnView(32, 20);
			jExcel.getWorkSheet().setColumnView(33, 20);

			jExcel.addDate(0, 0, "站点代码", jExcel.dateTITLEFormat);
			jExcel.addDate(1, 0, "站点名称", jExcel.dateTITLEFormat);
			jExcel.addDate(2, 0, "国家", jExcel.dateTITLEFormat);
			jExcel.addDate(3, 0, "大洲", jExcel.dateTITLEFormat);
			jExcel.addDate(4, 0, "服务区", jExcel.dateTITLEFormat);
			jExcel.addDate(5, 0, "地址", jExcel.dateTITLEFormat);
			jExcel.addDate(6, 0, "经度", jExcel.dateTITLEFormat);
			jExcel.addDate(7, 0, "纬度", jExcel.dateTITLEFormat);
			jExcel.addDate(8, 0, "海拔", jExcel.dateTITLEFormat);
			jExcel.addDate(9, 0, "X", jExcel.dateTITLEFormat);
			jExcel.addDate(10, 0, "Y", jExcel.dateTITLEFormat);
			jExcel.addDate(11, 0, "默认语言", jExcel.dateTITLEFormat);
			jExcel.addDate(12, 0, "CIRAF区", jExcel.dateTITLEFormat);
			jExcel.addDate(13, 0, "时差", jExcel.dateTITLEFormat);
			jExcel.addDate(14, 0, "拨号电话", jExcel.dateTITLEFormat);
			jExcel.addDate(15, 0, "维护人", jExcel.dateTITLEFormat);
			jExcel.addDate(16, 0, "维护人电话", jExcel.dateTITLEFormat);
			jExcel.addDate(17, 0, "联系人", jExcel.dateTITLEFormat);
			jExcel.addDate(18, 0, "联系人职务", jExcel.dateTITLEFormat);
			jExcel.addDate(19, 0, "联系人办公电话", jExcel.dateTITLEFormat);
			jExcel.addDate(20, 0, "联系人手机", jExcel.dateTITLEFormat);
			jExcel.addDate(21, 0, "联系人家庭电话", jExcel.dateTITLEFormat);
			jExcel.addDate(22, 0, "下发地址", jExcel.dateTITLEFormat);
			jExcel.addDate(23, 0, "通信类型", jExcel.dateTITLEFormat);
			jExcel.addDate(24, 0, "通信协议", jExcel.dateTITLEFormat);
			jExcel.addDate(25, 0, "IP地址", jExcel.dateTITLEFormat);
			jExcel.addDate(26, 0, "站点状态", jExcel.dateTITLEFormat);
//			jExcel.addDate(27, 0, "通信状态", jExcel.dateTITLEFormat);
//			jExcel.addDate(28, 0, "失败状态", jExcel.dateTITLEFormat);
//			jExcel.addDate(29, 0, "是否删除", jExcel.dateTITLEFormat);
			jExcel.addDate(27, 0, "站点类型", jExcel.dateTITLEFormat);
			jExcel.addDate(28, 0, "在线是否", jExcel.dateTITLEFormat);
			jExcel.addDate(29, 0, "是否有夏令时", jExcel.dateTITLEFormat);
//			jExcel.addDate(33, 0, "夏令时结束时间", jExcel.dateTITLEFormat);
			jExcel.addDate(30, 0, "制造厂商", jExcel.dateTITLEFormat);
			jExcel.addDate(31, 0, "版本", jExcel.dateTITLEFormat);
			jExcel.addDate(32, 0, "描述", jExcel.dateTITLEFormat);
			jExcel.addDate(33, 0, "ID", jExcel.dateTITLEFormat);

			jExcel.getWorkSheet().setName("站点信息");
			jExcel.saveDocument();

			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Location", "Export.xls");
			response.setHeader("Expires", "0");
			response.setHeader("Content-Disposition", new String(
					("filename=" + fileName).getBytes("GBK"), "ISO-8859-1")
					+ ".xls");
			OutputStream outputStream = response.getOutputStream();
			InputStream inputStream = new FileInputStream(downFileName);
			byte[] buffer = new byte[1024];
			int i = -1;
			while ((i = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, i);
			}
			outputStream.flush();
			outputStream.close();
			outputStream = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ArrayList<String> getHeadendCodes(){
		ArrayList<String> list=new ArrayList<String>();
		String sql="select code from res_headend_tab order by code";
		try {
			GDSet gd=DbComponent.Query(sql);
			int n=gd.getRowCount();
			if(n>0){
				for(int i=0;i<n;i++){
					list.add(gd.getString(i, "code"));
				}
			}
		} catch (DbException e) {
			e.printStackTrace();
		} catch (GDSetException e) {			
			e.printStackTrace();
		}
		return list;
	}
	private String[] getPhoneNumbers(String phone ){
		String[] strs={"","",""};;
		String[] ss= phone.split(",");
		int n=ss.length;
		if(n>3){
			n=3;
		}
		for(int i=0;i<n;i++){
			strs[i]=ss[i];
		}
		return strs;
	}
}
