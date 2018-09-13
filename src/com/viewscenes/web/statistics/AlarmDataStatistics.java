package com.viewscenes.web.statistics;

import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.jdom.Element;

import com.viewscenes.bean.ReportBean;
import com.viewscenes.bean.ZRST_REP_ABNORMAL_BEAN;
import com.viewscenes.dao.XmlReader;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.printexcel.JExcel;
import com.viewscenes.pub.GDSet;
import com.viewscenes.pub.GDSetException;
import com.viewscenes.util.StringTool;
import com.viewscenes.web.Daoutil.ReportUtil;

public class AlarmDataStatistics {

	/**
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SecurityException
	 * @throws GDSetException
	 * @throws DbException
	 *             ************************************************
	 * 
	 * @Title: doReport
	 * 
	 * @Description: TODO(异态统计)
	 * 
	 * @param @param starttime
	 * @param @param endtime
	 * @param @param type
	 * @param @param report_type
	 * @param @return 设定文件
	 * 
	 * @author 刘斌
	 * 
	 * @return ArrayList 返回类型
	 * 
	 * @throws
	 * 
	 ************************************************ 
	 */
	public static boolean doReport(String starttime, String endtime,
			String type, String report_type, String user_name)
			throws DbException, GDSetException, SecurityException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException, IllegalArgumentException,
			InvocationTargetException {

		String totalsql = "select t.*,(t.endtime-t.starttime)*24*60 countminues,tt.name abnormal_name ,to_char(t.starttime,'hh24:mi:ss') || '-' || to_char(endtime,'hh24:mi:ss') abnormaltime from radio_abnormal_tab t,dic_abnormal_type_tab tt"
				+ " where t.abnormal_date>=to_date('"
				+ starttime
				+ "','yyyy-mm-dd hh24:mi:ss') and t.abnormal_date<=to_date('"
				+ endtime
				+ "','yyyy-mm-dd hh24:mi:ss') "
				+ "and t.type="
				+ type
				+ "   and t.abnormal_type=tt.id  and t.audit_person is not null  order by t.frequency, t.abnormal_date ,  t.language_name,  t.station_name,  t.play_time ,t.endtime";
		ArrayList descList = new ArrayList();
		GDSet gd = DbComponent.Query(totalsql);
		String report_id = ReportUtil.getReportId();
		int rowCount = 0;
		ZRST_REP_ABNORMAL_BEAN flagbean = null;
		String start_time = "";
		String end_time = "";
		for (int i = 0; i < gd.getRowCount(); i++) {
			flagbean = new ZRST_REP_ABNORMAL_BEAN();
			flagbean.setAbnormal_date(gd.getString(i, "abnormal_date")
					.substring(0, 10));
			flagbean.setAbnormal_type(gd.getString(i, "abnormal_name"));
			flagbean.setBroadcasting_organizations(gd.getString(i,
					"broadcasting_organizations"));
			start_time = gd.getString(i, "starttime");
			end_time = gd.getString(i, "endtime");
			flagbean.setAbnormal_time(start_time.substring(11,19)+"-"+end_time.substring(11,19));
			flagbean.setCount_minues(String.format("%.2f", Double
					.parseDouble(gd.getString(i, "countminues"))));
			flagbean.setFrequency(gd.getString(i, "frequency"));
			flagbean.setGet_type(gd.getString(i, "get_type")
					.equals("1") ? "实时" : "录音");
			flagbean.setLanguage(gd.getString(i, "language_name"));
			flagbean.setPlay_time(gd.getString(i, "play_time"));
			flagbean.setPower(gd.getString(i, "power"));
			flagbean.setRemark(gd.getString(i, "remark"));
			flagbean.setRemote_station(gd
					.getString(i, "remote_station"));
			flagbean.setCollection_point(gd.getString(i,
					"collection_point"));
			flagbean.setReport_id(report_id);
			flagbean.setSend_city(gd.getString(i, "send_city"));
			flagbean.setSend_county(gd.getString(i, "send_county"));
			flagbean.setStation_name(gd.getString(i, "station_name"));
			flagbean.setTran_no(gd.getString(i, "tran_no"));
			flagbean.setType(gd.getString(i, "type"));
			descList.add(flagbean);
//			if (i == 0) {
//				flagbean.setAbnormal_date(gd.getString(i, "abnormal_date")
//						.substring(0, 10));
//				start_time = gd.getString(i, "starttime");
//				end_time = gd.getString(i, "endtime");
//				System.out.println(start_time);
//				flagbean.setAbnormal_type(gd.getString(i, "abnormal_name"));
//				flagbean.setBroadcasting_organizations(gd.getString(i,
//						"broadcasting_organizations"));
//				flagbean.setCount_minues(String.format("%.2f", Double
//						.parseDouble(gd.getString(i, "countminues"))));
//				flagbean.setFrequency(gd.getString(i, "frequency"));
//				flagbean
//						.setGet_type(gd.getString(i, "get_type").equals("1") ? "实时"
//								: "录音");
//				flagbean.setLanguage(gd.getString(i, "language_name"));
//				flagbean.setPlay_time(gd.getString(i, "play_time"));
//				flagbean.setPower(gd.getString(i, "power"));
//				flagbean.setRemark(gd.getString(i, "remark"));
//				flagbean.setRemote_station(gd.getString(i, "remote_station"));
//				flagbean.setReport_id(report_id);
//				flagbean.setSend_city(gd.getString(i, "send_city"));
//				flagbean.setSend_county(gd.getString(i, "send_county"));
//				flagbean.setStation_name(gd.getString(i, "station_name"));
//				flagbean.setCollection_point(gd
//						.getString(i, "collection_point"));
//				flagbean.setTran_no(gd.getString(i, "tran_no"));
//				flagbean.setType(gd.getString(i, "type"));
//
//			} else {
//				if (gd.getString(i, "frequency")
//						.equals(flagbean.getFrequency())
//						&& gd.getString(i, "play_time").equals(
//								flagbean.getPlay_time())
//						&& gd.getString(i, "language_name").equals(
//								flagbean.getLanguage())
//						&& gd.getString(i, "station_name").equals(
//								flagbean.getStation_name())
//						&& gd.getString(i, "abnormal_date").substring(0, 10)
//								.equals(flagbean.getAbnormal_date())
//
//				) {
//					flagbean.setRemote_station(StringTool
//							.delDuplicateStr(flagbean.getRemote_station() + ","
//									+ gd.getString(i, "remote_station")));
//					flagbean
//							.setCollection_point(StringTool
//									.delDuplicateStr(flagbean
//											.getCollection_point()
//											+ ","
//											+ gd.getString(i,
//													"collection_point")));
//					flagbean.setCount_minues(String.format("%.2f",
//							Double.parseDouble(flagbean.getCount_minues())
//									+ Double.parseDouble(gd.getString(i,
//											"countminues"))));
//					flagbean.setRemark(flagbean.getRemark()
//							+ gd.getString(i, "remark"));
//
//					if (start_time.compareTo(gd.getString(i, "starttime")) != -1) {
//						start_time = gd.getString(i, "starttime");
//					}
//					if (end_time.compareTo(gd.getString(i, "endtime")) != 1) {
//						end_time = gd.getString(i, "endtime");
//					}
//				} else {
//					System.out.println("start_time: " + start_time);
//					flagbean.setAbnormal_time(start_time.substring(11,19)+"-"+end_time.substring(11,19));
//					descList.add(flagbean);
//					flagbean = new ZRST_REP_ABNORMAL_BEAN();
//					flagbean.setAbnormal_date(gd.getString(i, "abnormal_date")
//							.substring(0, 10));
//					flagbean.setAbnormal_type(gd.getString(i, "abnormal_name"));
//					flagbean.setBroadcasting_organizations(gd.getString(i,
//							"broadcasting_organizations"));
//					start_time = gd.getString(i, "starttime");
//					end_time = gd.getString(i, "endtime");
//					flagbean.setCount_minues(String.format("%.2f", Double
//							.parseDouble(gd.getString(i, "countminues"))));
//					flagbean.setFrequency(gd.getString(i, "frequency"));
//					flagbean.setGet_type(gd.getString(i, "get_type")
//							.equals("1") ? "实时" : "录音");
//					flagbean.setLanguage(gd.getString(i, "language_name"));
//					flagbean.setPlay_time(gd.getString(i, "play_time"));
//					flagbean.setPower(gd.getString(i, "power"));
//					flagbean.setRemark(gd.getString(i, "remark"));
//					flagbean.setRemote_station(gd
//							.getString(i, "remote_station"));
//					flagbean.setCollection_point(gd.getString(i,
//							"collection_point"));
//					flagbean.setReport_id(report_id);
//					flagbean.setSend_city(gd.getString(i, "send_city"));
//					flagbean.setSend_county(gd.getString(i, "send_county"));
//					flagbean.setStation_name(gd.getString(i, "station_name"));
//					flagbean.setTran_no(gd.getString(i, "tran_no"));
//					flagbean.setType(gd.getString(i, "type"));
//				}
//
//			}
//			if (i == (gd.getRowCount() - 1)) {
//				flagbean.setAbnormal_time(start_time.substring(11,19)+"-"+end_time.substring(11,19));
//				descList.add(flagbean);
//
//			}
		}

		ReportBean reportBean = new ReportBean();
		reportBean.setReport_id(report_id);
		reportBean.setReport_date(StringTool.Date2String(new Date()));
		reportBean.setReport_type(report_type);
		reportBean.setPeriod_type("");
		reportBean.setStart_datetime(starttime);
		reportBean.setEnd_datetime(endtime);
		reportBean.setCharacter("");
		reportBean.getData_num();

		reportBean.setImport_status("1");
		reportBean.setImport_datetime(StringTool.Date2String(new Date()));
		reportBean.setImport_user(user_name);
		reportBean.setModify_status("1");
		reportBean.setModify_user("");
		reportBean.setModify_datetime("");

		reportBean.setVerify_status("");
		reportBean.setVerify_user("");
		reportBean.setVerify_datetime("");
		reportBean.setAuthentic_status("");
		reportBean.setAuthentic_user("");
		reportBean.setAuthentic_datetime("");
		reportBean.setRemark("");
		boolean boo = ReportUtil.insertReportByReportBean(reportBean, descList,
				"ZRST_REP_ABNORMAL_TAB",
				"com.viewscenes.bean.ZRST_REP_ABNORMAL_BEAN");

		return boo;
	}

	/**
	 * ************************************************
	 * 
	 * @Title: doExcel
	 * 
	 * @Description: TODO(导出外国台报表)
	 * 
	 * @param @param msg
	 * @param @param request
	 * @param @param response
	 * @param @throws Exception 设定文件
	 * 
	 * @author 刘斌
	 * 
	 * @return void 返回类型
	 * 
	 * @throws
	 * 
	 ************************************************ 
	 */
	public void doExcelHwld(String msg, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Element root = StringTool.getXMLRoot(msg);
		String reportId = root.getChildText("reportId");
		String starttime = root.getChildText("starttime");
		String endtime = root.getChildText("endtime");

		String fileName = "海外落地节目异态情况统计表";
		String firstName = "";
		if (starttime.substring(0, 10).equals(endtime.substring(0, 10))) {
			firstName += " " + endtime.substring(0, 10) + "日数据";
		} else {
			firstName += " 自(" + starttime.substring(0, 10) + "到"
					+ endtime.substring(0, 10) + ")日数据";
		}

		String descSql = "select *　　from  zrst_rep_abnormal_tab t where report_id="
				+ reportId
				+ "   order by t.frequency,t.station_name,t.language,t.abnormal_date,t.play_time asc   ";
		GDSet gdSet = DbComponent.Query(descSql);
		String classpath = ZRST_REP_ABNORMAL_BEAN.class.getName();
		ArrayList list = StringTool.converGDSetToBeanList(gdSet, classpath);
		JExcel jExcel = new JExcel();
		OutputStream outputStream = response.getOutputStream();
		response.setContentType("application/vnd.ms-excel");
		response.reset();
		response.setHeader("Location", "Export.xls");
		response.setHeader("Expires", "0");
		response
				.setHeader("Content-disposition", "attachment; filename="
						+ new String((fileName).getBytes("GBK"), "ISO-8859-1")
						+ ".xls");

		WritableWorkbook wwb = Workbook.createWorkbook(outputStream);
		int sheetNum = 0;
		// 子报表类型
		// 11：国际台广播效果统计；21：发射台总体播出效果统计1；22：发射台总体播出效果统计2；23：发射台总体播出效果统计3；31：语言总体播出效果统计1；
		WritableSheet ws = null;
		WritableSheet ws2 = null;
		ws = wwb.createSheet("异态汇总表", wwb.getNumberOfSheets() + 1);// 表一

		ws2 = wwb.createSheet("异态类型汇总表", wwb.getNumberOfSheets() + 2);// 表二
		WritableFont wf = new WritableFont(WritableFont.createFont("黑体"), 10,
				WritableFont.BOLD, false);
		WritableCellFormat wcfF = new WritableCellFormat(wf);
		wcfF.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);// 带边框
		wcfF.setWrap(true);// 自动换行
		// 把水平对齐方式指定为左对齐
		wcfF.setAlignment(jxl.format.Alignment.CENTRE);
		// 把垂直对齐方式指定为居中对齐
		wcfF.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		// 设置列名

		ws.addCell(new Label(0, 1, "频率(KHz)", wcfF));
		ws.addCell(new Label(1, 1, "播音时间", wcfF));
		ws.addCell(new Label(2, 1, "语言", wcfF));
		ws.addCell(new Label(3, 1, "发射国家", wcfF));
		ws.addCell(new Label(4, 1, "发射城市", wcfF));
		ws.addCell(new Label(5, 1, "转播机构", wcfF));
		ws.addCell(new Label(6, 1, "收测遥控站", wcfF));
		ws.addCell(new Label(7, 1, "收测采集点", wcfF));
		ws.addCell(new Label(8, 1, "异态现象", wcfF));
		ws.addCell(new Label(9, 1, "异态日期", wcfF));
		ws.addCell(new Label(10, 1, "异态发生时间", wcfF));
		ws.addCell(new Label(11, 1, "异态时间(分)", wcfF));
		ws.addCell(new Label(12, 1, "备注", wcfF));
		ws.addCell(new Label(13, 1, "总次数", wcfF));
		ws.addCell(new Label(14, 1, "异态总时间(分)", wcfF));
		// ws.addCell(new Label(14,1,"实时/录音",wcfF));
		String typesql = "select name from dic_abnormal_type_tab t order by t.id  ";
		GDSet set = DbComponent.Query(typesql);
		ws2.mergeCells(0, 1, 0, 2);// 合并国外发射台。
		ws2.addCell(new Label(0, 1, "转播机构", wcfF));
		ws2.setColumnView(0, 14);
		String alltype = "";
		for (int t = 0; t < set.getRowCount(); t++) {
			ws2.mergeCells(t * 2 + 1, 1, t * 2 + 2, 1);// 合并类型
			ws2
					.addCell(new Label(t * 2 + 1, 1, set.getString(t, "name"),
							wcfF));
			ws2.addCell(new Label(t * 2 + 1, 2, "异态次数", wcfF));
			ws2.addCell(new Label(t * 2 + 2, 2, "异态时间(分)", wcfF));
			ws2.setColumnView(t * 2 + 1, 13);
			ws2.setColumnView(t * 2 + 2, 15);
			alltype += set.getString(t, "name") + ",0,0&&";
		}
		alltype = alltype + "0,0";

		ws2.mergeCells(set.getRowCount() * 2 + 1, 1, set.getRowCount() * 2 + 1,
				2);// 合并总次数。
		ws2.addCell(new Label(set.getRowCount() * 2 + 1, 1, "总次数", wcfF));
		ws2.mergeCells(set.getRowCount() * 2 + 2, 1, set.getRowCount() * 2 + 2,
				2);// 合并总时间。
		ws2.addCell(new Label(set.getRowCount() * 2 + 2, 1, "总时间", wcfF));
		ws2.setColumnView(set.getRowCount() * 2 + 1, 13);
		ws2.setColumnView(set.getRowCount() * 2 + 2, 13);
		ws2.mergeCells(0, 0, set.getRowCount() * 2 + 2, 0);

		HashMap map = new HashMap();// key为发射台
		map.put("总计", alltype);
		for (int i = 0; i < list.size(); i++) {
			ZRST_REP_ABNORMAL_BEAN bean = (ZRST_REP_ABNORMAL_BEAN) list.get(i);
			if (map.get(bean.getBroadcasting_organizations()) != null) {
				String value = (String) map.get(bean
						.getBroadcasting_organizations());
				map.put(bean.getBroadcasting_organizations(), this
						.getNewString(value, bean.getAbnormal_type(), "1", bean
								.getCount_minues()));
				map.put("总计", this.getNewString((String) map.get("总计"), bean
						.getAbnormal_type(), "1", bean.getCount_minues()));
			} else {
				map.put(bean.getBroadcasting_organizations(), this
						.getNewString(alltype, bean.getAbnormal_type(), "1",
								bean.getCount_minues()));
				map.put("总计", this.getNewString((String) map.get("总计"), bean
						.getAbnormal_type(), "1", bean.getCount_minues()));
			}
		}

		ws.mergeCells(0, 0, 14, 0);
		wf = new WritableFont(WritableFont.createFont("黑体"), 12,
				WritableFont.BOLD, false);
		wcfF = new WritableCellFormat(wf);
		wcfF.setAlignment(jxl.format.Alignment.CENTRE);
		// 把垂直对齐方式指定为居中对齐
		wcfF.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);

		ws.addCell(new Label(0, 0, "海外落地异态情况统计表 " + firstName, wcfF));
		ws2.addCell(new Label(0, 0, "海外落地异态类型汇总表" + firstName, wcfF));
		ws.setRowView(0, 740);// 设置第一行高度
		ws.setRowView(1, 740);
		ws2.setRowView(0, 740);// 设置第一行高度
		ws2.setRowView(1, 740);
		ws.setColumnView(0, 13);
		ws.setColumnView(1, 13);
		ws.setColumnView(2, 7);
		ws.setColumnView(3, 9);
		ws.setColumnView(4, 13);
		ws.setColumnView(5, 20);
		ws.setColumnView(6, 20);
		ws.setColumnView(7, 20);
		ws.setColumnView(8, 15);
		ws.setColumnView(9, 13);
		ws.setColumnView(10, 30);
		ws.setColumnView(11, 13);
		ws.setColumnView(12, 13);
		ws.setColumnView(13, 10);
		ws.setColumnView(14, 10);
		// ws.setColumnView(14, 10);

		WritableFont wf1 = new WritableFont(WritableFont.createFont("宋体"), 10,
				WritableFont.NO_BOLD, false);
		// 设置CellFormat
		jxl.write.WritableCellFormat wcfF2 = new jxl.write.WritableCellFormat(
				wf1);
		// 把水平对齐方式指定为左对齐
		wcfF2.setAlignment(jxl.format.Alignment.CENTRE);
		// 把垂直对齐方式指定为居中对齐
		wcfF2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		wcfF2.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
		Set mapset = map.keySet();

		Iterator it = mapset.iterator();
		// 总计放最后加上
		String zongjikey = "";
		String zongjivalue = "";
		while (it.hasNext()) {
			String key = (String) it.next();
			String value = (String) map.get(key);
			if (!key.equals("总计")) {
				this.addcell(ws2, key, value, wcfF2);
			} else {
				zongjikey = key;
				zongjivalue = value;
			}

		}
		this.addcell(ws2, zongjikey, zongjivalue, wcfF2);
		ZRST_REP_ABNORMAL_BEAN bean_flag = new ZRST_REP_ABNORMAL_BEAN();
		for (int i = 0; i < list.size(); i++) {
			ZRST_REP_ABNORMAL_BEAN bean = (ZRST_REP_ABNORMAL_BEAN) list.get(i);
			if (i == 0) {
				bean_flag = bean;
				continue;
			}
			if (bean_flag.getFrequency().equals(bean.getFrequency())
					&& bean_flag.getPlay_time().equals(bean.getPlay_time())
					&& bean_flag.getLanguage().equals(bean.getLanguage())
					&& bean_flag.getSend_county().equals(bean.getSend_county())
					&& bean_flag.getSend_city().equals(bean.getSend_city())
					&& i != 0) {
				if (!bean_flag.getBroadcasting_organizations().contains(
						bean.getBroadcasting_organizations())) {
					bean_flag.setBroadcasting_organizations(bean_flag
							.getBroadcasting_organizations()
							+ "," + bean.getBroadcasting_organizations());// 合并站点
				}
				bean_flag.setAbnormal_type(bean_flag.getAbnormal_type() + ","
						+ bean.getAbnormal_type());// 异态现象
				bean_flag.setAbnormal_date(bean_flag.getAbnormal_date() + ","
						+ bean.getAbnormal_date());// 异态日期
				bean_flag.setAbnormal_time(bean_flag.getAbnormal_time() + ","
						+ bean.getAbnormal_time());// 异态时间
				bean_flag.setCount_minues(bean_flag.getCount_minues() + ","
						+ bean.getCount_minues());// 分钟
			} else {
				int curRow = ws.getRows();
				String[] ABNORMAL_TYPES = StringTool.getArrayByStringWithSpit(
						bean_flag.getAbnormal_type(), ",");
				if (ABNORMAL_TYPES.length > 1)
					ws.mergeCells(0, curRow, 0, curRow + ABNORMAL_TYPES.length
							- 1);
				ws
						.addCell(new Label(0, curRow, bean_flag.getFrequency(),
								wcfF2));
				if (ABNORMAL_TYPES.length > 1)
					ws.mergeCells(1, curRow, 1, curRow + ABNORMAL_TYPES.length
							- 1);
				ws
						.addCell(new Label(1, curRow, bean_flag.getPlay_time(),
								wcfF2));
				if (ABNORMAL_TYPES.length > 1)
					ws.mergeCells(2, curRow, 2, curRow + ABNORMAL_TYPES.length
							- 1);
				ws
						.addCell(new Label(2, curRow, bean_flag.getLanguage(),
								wcfF2));
				if (ABNORMAL_TYPES.length > 1)
					ws.mergeCells(3, curRow, 3, curRow + ABNORMAL_TYPES.length
							- 1);
				ws.addCell(new Label(3, curRow, bean_flag.getSend_county(),
						wcfF2));
				if (ABNORMAL_TYPES.length > 1)
					ws.mergeCells(4, curRow, 4, curRow + ABNORMAL_TYPES.length
							- 1);
				ws.addCell(new Label(4, curRow, bean_flag.getSend_city(),
								wcfF2));
				if (ABNORMAL_TYPES.length > 1)
					ws.mergeCells(5, curRow, 5, curRow + ABNORMAL_TYPES.length
							- 1);
				ws.addCell(new Label(5, curRow, bean_flag
						.getBroadcasting_organizations(), wcfF2));
				if (ABNORMAL_TYPES.length > 1) {
					ws.mergeCells(6, curRow, 6, curRow + ABNORMAL_TYPES.length
							- 1);
					ws.mergeCells(7, curRow, 7, curRow + ABNORMAL_TYPES.length
							- 1);
				}
					ws.addCell(new Label(6, curRow, bean_flag
							.getRemote_station(), wcfF2));
					ws.addCell(new Label(7, curRow, bean_flag.getCollection_point(), wcfF2));
				
				for (int k = 0; k < ABNORMAL_TYPES.length; k++) {
					ws.addCell(new Label(8, curRow + k, ABNORMAL_TYPES[k],
							wcfF2));
				}
				String[] ABNORMAL_DATES = StringTool.getArrayByStringWithSpit(
						bean_flag.getAbnormal_date(), ",");

				for (int k = 0; k < ABNORMAL_DATES.length; k++) {
					ws.addCell(new Label(9, curRow + k, ABNORMAL_DATES[k]
							.substring(0, 10), wcfF2));
				}
				String[] ABNORMAL_TIMES = StringTool.getArrayByStringWithSpit(
						bean_flag.getAbnormal_time(), ",");
				for (int k = 0; k < ABNORMAL_TIMES.length; k++) {
					ws.addCell(new Label(10, curRow + k, ABNORMAL_TIMES[k],
							wcfF2));
				}
				String[] COUNT_MINUES = StringTool.getArrayByStringWithSpit(
						bean_flag.getCount_minues(), ",");
				double count = StringTool.getCountByStringWithSpit(bean_flag
						.getCount_minues(), ",");

				for (int k = 0; k < COUNT_MINUES.length; k++) {
					ws
							.addCell(new Label(11, curRow + k, format(COUNT_MINUES[k]),
									wcfF2));
				}
				ws.addCell(new Label(12, curRow, bean_flag.getRemark(), wcfF2));
				if (ABNORMAL_TYPES.length > 1)
					ws.mergeCells(13, curRow, 13, curRow
							+ ABNORMAL_TYPES.length - 1);
				ws.addCell(new Label(13, curRow, ABNORMAL_TYPES.length + "",
						wcfF2));
				if (ABNORMAL_TYPES.length > 1)
					ws.mergeCells(14, curRow, 14, curRow
							+ ABNORMAL_TYPES.length - 1);
				ws.addCell(new Label(14, curRow, format(count + ""), wcfF2));
				// if(ABNORMAL_TYPES.length>1)
				// ws.mergeCells(14,curRow,14,curRow+ABNORMAL_TYPES.length-1);
				// ws.addCell(new Label(14,curRow,
				// bean_flag.getGet_type(),wcfF2));
				bean_flag = bean;
			}
		}
		if (list.size() == 1)// 只有一条
		{
			ws.addCell(new Label(0, 2, bean_flag.getFrequency(), wcfF2));
			ws.addCell(new Label(1, 2, bean_flag.getPlay_time(), wcfF2));
			ws.addCell(new Label(2, 2, bean_flag.getLanguage(), wcfF2));
			ws.addCell(new Label(3, 2, bean_flag.getSend_county(), wcfF2));
			ws.addCell(new Label(4, 2, bean_flag.getSend_city(), wcfF2));
			ws.addCell(new Label(5, 2, bean_flag
					.getBroadcasting_organizations(), wcfF2));
			ws.addCell(new Label(6, 2, bean_flag.getRemote_station(), wcfF2));
			ws.addCell(new Label(7, 2, bean_flag.getCollection_point(), wcfF2));
			ws.addCell(new Label(8, 2, bean_flag.getAbnormal_type(), wcfF2));
			ws.addCell(new Label(9, 2, bean_flag.getAbnormal_date(), wcfF2));
			ws.addCell(new Label(10, 2, bean_flag.getAbnormal_time(), wcfF2));
			ws.addCell(new Label(11, 2, format(bean_flag.getCount_minues()), wcfF2));
			ws.addCell(new Label(12, 2, bean_flag.getRemark(), wcfF2));
			ws.addCell(new Label(13, 2, "1", wcfF2));
			ws.addCell(new Label(14, 2, format(bean_flag.getCount_minues()), wcfF2));
			// ws.addCell(new Label(14,2, bean_flag.getGet_type(),wcfF2));
		} else if (list.size() != 0)// 最后一条也要加上
		{
			int curRow = ws.getRows();
			String[] ABNORMAL_TYPES = StringTool.getArrayByStringWithSpit(
					bean_flag.getAbnormal_type(), ",");
			if (ABNORMAL_TYPES.length > 1)
				ws.mergeCells(0, curRow, 0, curRow + ABNORMAL_TYPES.length - 1);
			ws.addCell(new Label(0, curRow, bean_flag.getFrequency(), wcfF2));
			if (ABNORMAL_TYPES.length > 1)
				ws.mergeCells(1, curRow, 1, curRow + ABNORMAL_TYPES.length - 1);
			ws.addCell(new Label(1, curRow, bean_flag.getPlay_time(), wcfF2));
			if (ABNORMAL_TYPES.length > 1)
				ws.mergeCells(2, curRow, 2, curRow + ABNORMAL_TYPES.length - 1);
			ws.addCell(new Label(2, curRow, bean_flag.getLanguage(), wcfF2));
			if (ABNORMAL_TYPES.length > 1)
				ws.mergeCells(3, curRow, 3, curRow + ABNORMAL_TYPES.length - 1);
			ws.addCell(new Label(3, curRow, bean_flag.getSend_county(), wcfF2));
			if (ABNORMAL_TYPES.length > 1)
				ws.mergeCells(4, curRow, 4, curRow + ABNORMAL_TYPES.length - 1);
			ws.addCell(new Label(4, curRow, bean_flag.getSend_city(), wcfF2));
			if (ABNORMAL_TYPES.length > 1)
				ws.mergeCells(5, curRow, 5, curRow + ABNORMAL_TYPES.length - 1);
			ws.addCell(new Label(5, curRow, bean_flag
					.getBroadcasting_organizations(), wcfF2));
			if (ABNORMAL_TYPES.length > 1) {
				ws.mergeCells(6, curRow, 6, curRow + ABNORMAL_TYPES.length - 1);
				ws.mergeCells(7, curRow, 7, curRow + ABNORMAL_TYPES.length - 1);
			}
				ws.addCell(new Label(6, curRow, bean_flag.getRemote_station(),
						wcfF2));
				ws.addCell(new Label(7, curRow,
						bean_flag.getCollection_point(), wcfF2));
			
			for (int k = 0; k < ABNORMAL_TYPES.length; k++) {
				ws.addCell(new Label(8, curRow + k, ABNORMAL_TYPES[k], wcfF2));
			}
			String[] ABNORMAL_DATES = StringTool.getArrayByStringWithSpit(
					bean_flag.getAbnormal_date(), ",");

			for (int k = 0; k < ABNORMAL_DATES.length; k++) {
				ws.addCell(new Label(9, curRow + k, ABNORMAL_DATES[k]
						.substring(0, 10), wcfF2));
			}
			String[] ABNORMAL_TIMES = StringTool.getArrayByStringWithSpit(
					bean_flag.getAbnormal_time(), ",");
			for (int k = 0; k < ABNORMAL_TIMES.length; k++) {
				ws.addCell(new Label(10, curRow + k, ABNORMAL_TIMES[k], wcfF2));
			}
			String[] COUNT_MINUES = StringTool.getArrayByStringWithSpit(
					bean_flag.getCount_minues(), ",");
			double count = StringTool.getCountByStringWithSpit(bean_flag
					.getCount_minues(), ",");

			for (int k = 0; k < COUNT_MINUES.length; k++) {
				ws.addCell(new Label(11, curRow + k, format(COUNT_MINUES[k]), wcfF2));
			}
			ws.addCell(new Label(12, curRow, bean_flag.getRemark(), wcfF2));
			if (ABNORMAL_TYPES.length > 1)
				ws.mergeCells(13, curRow, 13, curRow + ABNORMAL_TYPES.length
						- 1);
			ws.addCell(new Label(13, curRow, ABNORMAL_TYPES.length + "",
							wcfF2));
			if (ABNORMAL_TYPES.length > 1)
				ws.mergeCells(14, curRow, 14, curRow + ABNORMAL_TYPES.length
						- 1);
			ws.addCell(new Label(14, curRow, format(count + ""), wcfF2));
			// if(ABNORMAL_TYPES.length>1)
			// ws.mergeCells(14,curRow,14,curRow+ABNORMAL_TYPES.length-1);
			// ws.addCell(new Label(14,curRow, bean_flag.getGet_type(),wcfF2));

		}

		String[] ss = zongjivalue.split("&&");
		String[] sum = ss[ss.length - 1].split(",");
		int n = ws.getRows();
		ws.addCell(new Label(0, n, "总计", wcfF2));
		ws.addCell(new Label(1, n, "", wcfF2));
		ws.addCell(new Label(2, n, "", wcfF2));
		ws.addCell(new Label(3, n, "", wcfF2));
		ws.addCell(new Label(4, n, "", wcfF2));
		ws.addCell(new Label(5, n, "", wcfF2));
		ws.addCell(new Label(6, n, "", wcfF2));
		ws.addCell(new Label(7, n, "", wcfF2));
		ws.addCell(new Label(8, n, "", wcfF2));
		ws.addCell(new Label(9, n, "", wcfF2));
		ws.addCell(new Label(10, n, "", wcfF2));
		ws.addCell(new Label(11, n, "", wcfF2));
		ws.addCell(new Label(12, n, "", wcfF2));
		ws.addCell(new Label(13, n, sum[0], wcfF2));
		ws.addCell(new Label(14, n, format(sum[1]), wcfF2));
		wwb.write();
		wwb.close();
		outputStream.close();

	}

	/**
	 * ************************************************
	 * 
	 * @Title: doExcel
	 * 
	 * @Description: TODO(导出外国台报表)
	 * 
	 * @param @param msg
	 * @param @param request
	 * @param @param response
	 * @param @throws Exception 设定文件
	 * 
	 * @author 刘斌
	 * 
	 * @return void 返回类型
	 * 
	 * @throws
	 * 
	 ************************************************ 
	 */
	public void doExcel(String msg, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Element root = StringTool.getXMLRoot(msg);
		String reportId = root.getChildText("reportId");
		String starttime = root.getChildText("starttime");
		String endtime = root.getChildText("endtime");

		String fileName = "国外发射台异态情况统计表";
		String firstName = "";
		if (starttime.substring(0, 10).equals(endtime.substring(0, 10))) {
			firstName += " " + starttime.substring(0, 10) + "日数据";
		} else {
			firstName += " 自(" + starttime.substring(0, 10) + "到"
					+ endtime.substring(0, 10) + ")日数据";
		}
		String descSql = "select *　　from  zrst_rep_abnormal_tab t where report_id="
				+ reportId
				+ "   order by t.abnormal_date,t.frequency,t.station_name,t.language,t.play_time    ";
		GDSet gdSet = DbComponent.Query(descSql);
		String classpath = ZRST_REP_ABNORMAL_BEAN.class.getName();
		ArrayList list = StringTool.converGDSetToBeanList(gdSet, classpath);
		JExcel jExcel = new JExcel();
		OutputStream outputStream = response.getOutputStream();
		response.setContentType("application/vnd.ms-excel");
		response.reset();
		response.setHeader("Location", "Export.xls");
		response.setHeader("Expires", "0");
		response
				.setHeader("Content-disposition", "attachment; filename="
						+ new String((fileName).getBytes("GBK"), "ISO-8859-1")
						+ ".xls");

		WritableWorkbook wwb = Workbook.createWorkbook(outputStream);
		int sheetNum = 0;
		// 子报表类型
		// 11：国际台广播效果统计；21：发射台总体播出效果统计1；22：发射台总体播出效果统计2；23：发射台总体播出效果统计3；31：语言总体播出效果统计1；
		WritableSheet ws = null;
		WritableSheet ws2 = null;

		ws = wwb.createSheet("异态汇总表", wwb.getNumberOfSheets() + 1);// 表一

		ws2 = wwb.createSheet("异态类型汇总表", wwb.getNumberOfSheets() + 2);// 表二
		WritableFont wf = new WritableFont(WritableFont.createFont("黑体"), 10,
				WritableFont.BOLD, false);
		WritableCellFormat wcfF = new WritableCellFormat(wf);
		wcfF.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);// 带边框
		wcfF.setWrap(true);// 自动换行
		// 把水平对齐方式指定为左对齐
		wcfF.setAlignment(jxl.format.Alignment.CENTRE);
		// 把垂直对齐方式指定为居中对齐
		wcfF.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		// 设置列名

		ws.addCell(new Label(0, 1, "频率(KHz)", wcfF));
		ws.addCell(new Label(1, 1, "播音时间", wcfF));
		ws.addCell(new Label(2, 1, "语言", wcfF));
		ws.addCell(new Label(3, 1, "转播台", wcfF));
		ws.addCell(new Label(4, 1, "收测地点", wcfF));
		ws.addCell(new Label(5, 1, "异态现象", wcfF));
		ws.addCell(new Label(6, 1, "异态日期", wcfF));
		ws.addCell(new Label(7, 1, "异态发生时间", wcfF));
		ws.addCell(new Label(8, 1, "异态时间(分)", wcfF));
		ws.addCell(new Label(9, 1, "总次数", wcfF));
		ws.addCell(new Label(10, 1, "异态总时间(分)", wcfF));
		// ws.addCell(new Label(11,1,"实时/录音",wcfF));
		String typesql = "select name from dic_abnormal_type_tab t order by t.id  ";
		GDSet set = DbComponent.Query(typesql);
		ws2.mergeCells(0, 1, 0, 2);// 合并国外发射台。
		ws2.addCell(new Label(0, 1, "国外发射台", wcfF));
		ws2.setColumnView(0, 13);
		String alltype = "";
		for (int t = 0; t < set.getRowCount(); t++) {
			ws2.mergeCells(t * 2 + 1, 1, t * 2 + 2, 1);// 合并类型
			ws2
					.addCell(new Label(t * 2 + 1, 1, set.getString(t, "name"),
							wcfF));
			ws2.addCell(new Label(t * 2 + 1, 2, "异态次数", wcfF));
			ws2.addCell(new Label(t * 2 + 2, 2, "异态时间(分)", wcfF));
			ws2.setColumnView(t * 2 + 1, 13);
			ws2.setColumnView(t * 2 + 2, 15);
			alltype += set.getString(t, "name") + ",0,0&&";
		}
		alltype = alltype + "0,0";

		ws2.mergeCells(set.getRowCount() * 2 + 1, 1, set.getRowCount() * 2 + 1,
				2);// 合并总次数。
		ws2.addCell(new Label(set.getRowCount() * 2 + 1, 1, "总次数", wcfF));
		ws2.mergeCells(set.getRowCount() * 2 + 2, 1, set.getRowCount() * 2 + 2,
				2);// 合并总时间。
		ws2.addCell(new Label(set.getRowCount() * 2 + 2, 1, "总时间", wcfF));
		ws2.setColumnView(set.getRowCount() * 2 + 1, 13);
		ws2.setColumnView(set.getRowCount() * 2 + 2, 13);
		ws2.mergeCells(0, 0, set.getRowCount() * 2 + 2, 0);
		int stationCountNumer = 0;// 每个发射台总次数
		double stationcountTime = 0;// 每个发射台总时间
		int CountNumer = 0;// 所有发射台总次数
		double countTime = 0;// 所有发射台总时间
		HashMap map = new HashMap();// key为发射台
		map.put("总计", alltype);
		for (int i = 0; i < list.size(); i++) {
			ZRST_REP_ABNORMAL_BEAN bean = (ZRST_REP_ABNORMAL_BEAN) list.get(i);
			if (map.get(bean.getStation_name()) != null) {
				String value = (String) map.get(bean.getStation_name());
				map.put(bean.getStation_name(), this.getNewString(value, bean
						.getAbnormal_type(), "1", bean.getCount_minues()));
				map.put("总计", this.getNewString((String) map.get("总计"), bean
						.getAbnormal_type(), "1", bean.getCount_minues()));
			} else {
				map.put(bean.getStation_name(), this.getNewString(alltype, bean
						.getAbnormal_type(), "1", bean.getCount_minues()));
				map.put("总计", this.getNewString((String) map.get("总计"), bean
						.getAbnormal_type(), "1", bean.getCount_minues()));
			}
		}

		ws.mergeCells(0, 0, 11, 0);
		wf = new WritableFont(WritableFont.createFont("黑体"), 12,
				WritableFont.BOLD, false);
		wcfF = new WritableCellFormat(wf);
		wcfF.setAlignment(jxl.format.Alignment.CENTRE);
		// 把垂直对齐方式指定为居中对齐
		wcfF.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);

		ws.addCell(new Label(0, 0, "国外发射台异态情况统计表  " + firstName, wcfF));
		ws2.addCell(new Label(0, 0, "国外发射台异态情况统计表  " + firstName, wcfF));
		ws.setRowView(0, 740);// 设置第一行高度
		ws.setRowView(1, 740);
		ws2.setRowView(0, 740);// 设置第一行高度
		ws2.setRowView(1, 740);
		ws.setColumnView(0, 13);
		ws.setColumnView(1, 13);
		ws.setColumnView(2, 7);
		ws.setColumnView(3, 9);
		ws.setColumnView(4, 13);
		ws.setColumnView(5, 20);
		ws.setColumnView(6, 20);
		ws.setColumnView(7, 30);
		ws.setColumnView(8, 13);
		ws.setColumnView(9, 13);
		ws.setColumnView(10, 13);
		// ws.setColumnView(11, 13);

		WritableFont wf1 = new WritableFont(WritableFont.createFont("宋体"), 10,
				WritableFont.NO_BOLD, false);
		// 设置CellFormat
		jxl.write.WritableCellFormat wcfF2 = new jxl.write.WritableCellFormat(
				wf1);
		// 把水平对齐方式指定为左对齐
		wcfF2.setAlignment(jxl.format.Alignment.CENTRE);
		// 把垂直对齐方式指定为居中对齐
		wcfF2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		wcfF2.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
		Set mapset = map.keySet();

		Iterator it = mapset.iterator();
		// 总计放最后加上
		String zongjikey = "";
		String zongjivalue = "";
		while (it.hasNext()) {
			String key = (String) it.next();
			String value = (String) map.get(key);

			if (!key.equals("总计")) {
				this.addcell(ws2, key, value, wcfF2);
			} else {
				zongjikey = key;
				zongjivalue = value;
			}

		}
		this.addcell(ws2, zongjikey, zongjivalue, wcfF2);
		ZRST_REP_ABNORMAL_BEAN bean_flag = new ZRST_REP_ABNORMAL_BEAN();
		for (int i = 0; i < list.size(); i++) {
			ZRST_REP_ABNORMAL_BEAN bean = (ZRST_REP_ABNORMAL_BEAN) list.get(i);
			if (i == 0) {
				bean_flag = bean;
				continue;
			}
			if (bean_flag.getFrequency().equals(bean.getFrequency())
					&& bean_flag.getPlay_time().equals(bean.getPlay_time())
					&& bean_flag.getLanguage().equals(bean.getLanguage())
					&& bean_flag.getStation_name().equals(
							bean.getStation_name()) && i != 0) {
				if (!bean_flag.getRemote_station().contains(
						bean.getRemote_station())) {
					bean_flag.setRemote_station(bean_flag.getRemote_station()
							+ "," + bean.getRemote_station());// 合并站点
				}

				bean_flag.setAbnormal_type(bean_flag.getAbnormal_type() + ","
						+ bean.getAbnormal_type());// 异态现象
				bean_flag.setAbnormal_date(bean_flag.getAbnormal_date() + ","
						+ bean.getAbnormal_date());// 异态日期
				bean_flag.setAbnormal_time(bean_flag.getAbnormal_time() + ","
						+ bean.getAbnormal_time());// 异态时间
				bean_flag.setCount_minues(bean_flag.getCount_minues() + ","
						+ bean.getCount_minues());// 分钟
			} else {
				int curRow = ws.getRows();
				String[] ABNORMAL_TYPES = StringTool.getArrayByStringWithSpit(
						bean_flag.getAbnormal_type(), ",");
				if (ABNORMAL_TYPES.length > 1)
					ws.mergeCells(0, curRow, 0, curRow + ABNORMAL_TYPES.length
							- 1);
				ws
						.addCell(new Label(0, curRow, bean_flag.getFrequency(),
								wcfF2));
				if (ABNORMAL_TYPES.length > 1)
					ws.mergeCells(1, curRow, 1, curRow + ABNORMAL_TYPES.length
							- 1);
				ws
						.addCell(new Label(1, curRow, bean_flag.getPlay_time(),
								wcfF2));
				if (ABNORMAL_TYPES.length > 1)
					ws.mergeCells(2, curRow, 2, curRow + ABNORMAL_TYPES.length
							- 1);
				ws
						.addCell(new Label(2, curRow, bean_flag.getLanguage(),
								wcfF2));
				if (ABNORMAL_TYPES.length > 1)
					ws.mergeCells(3, curRow, 3, curRow + ABNORMAL_TYPES.length
							- 1);
				ws.addCell(new Label(3, curRow, bean_flag.getStation_name(),
						wcfF2));
				if (ABNORMAL_TYPES.length > 1)
					ws.mergeCells(4, curRow, 4, curRow + ABNORMAL_TYPES.length
							- 1);
				ws.addCell(new Label(4, curRow, bean_flag.getRemote_station(),
						wcfF2));
				for (int k = 0; k < ABNORMAL_TYPES.length; k++) {
					ws.addCell(new Label(5, curRow + k, ABNORMAL_TYPES[k],
							wcfF2));
				}
				// ws.addCell(new Label(5,curRow,
				// bean_flag.getAbnormal_type(),wcfF2));
				String[] ABNORMAL_DATES = StringTool.getArrayByStringWithSpit(
						bean_flag.getAbnormal_date(), ",");

				for (int k = 0; k < ABNORMAL_DATES.length; k++) {
					ws.addCell(new Label(6, curRow + k, ABNORMAL_DATES[k]
							.substring(0, 10), wcfF2));
				}
				// ws.addCell(new Label(6,curRow,
				// bean_flag.getAbnormal_date(),wcfF2));
				String[] ABNORMAL_TIMES = StringTool.getArrayByStringWithSpit(
						bean_flag.getAbnormal_time(), ",");
				for (int k = 0; k < ABNORMAL_TIMES.length; k++) {
					ws.addCell(new Label(7, curRow + k, ABNORMAL_TIMES[k],
							wcfF2));
				}
				// ws.addCell(new Label(7,curRow,
				// bean_flag.getAbnormal_time(),wcfF2));
				String[] COUNT_MINUES = StringTool.getArrayByStringWithSpit(
						bean_flag.getCount_minues(), ",");
				double count = StringTool.getCountByStringWithSpit(bean_flag
						.getCount_minues(), ",");

				for (int k = 0; k < COUNT_MINUES.length; k++) {
					ws.addCell(new Label(8, curRow + k, format(COUNT_MINUES[k]),
									wcfF2));
				}
				// ws.addCell(new Label(8,curRow,
				// bean_flag.getCount_minues(),wcfF2));
				if (ABNORMAL_TYPES.length > 1)
					ws.mergeCells(9, curRow, 9, curRow + ABNORMAL_TYPES.length
							- 1);
				ws.addCell(new Label(9, curRow, ABNORMAL_TYPES.length + "",
						wcfF2));
				if (ABNORMAL_TYPES.length > 1)
					ws.mergeCells(10, curRow, 10, curRow
							+ ABNORMAL_TYPES.length - 1);
				ws.addCell(new Label(10, curRow, format(count+""), wcfF2));
				// if(ABNORMAL_TYPES.length>1)
				// ws.mergeCells(11,curRow,11,curRow+ABNORMAL_TYPES.length-1);
				// ws.addCell(new Label(11,curRow,
				// bean_flag.getGet_type(),wcfF2));
				bean_flag = bean;
			}
		}
		if (list.size() == 1)// 只有一条
		{
			ws.addCell(new Label(0, 2, bean_flag.getFrequency(), wcfF2));
			ws.addCell(new Label(1, 2, bean_flag.getPlay_time(), wcfF2));
			ws.addCell(new Label(2, 2, bean_flag.getLanguage(), wcfF2));
			ws.addCell(new Label(3, 2, bean_flag.getStation_name(), wcfF2));
			System.out.println(bean_flag.getRemote_station());
			ws.addCell(new Label(4, 2, bean_flag.getRemote_station(), wcfF2));
			ws.addCell(new Label(5, 2, bean_flag.getAbnormal_type(), wcfF2));
			ws.addCell(new Label(6, 2, bean_flag.getAbnormal_date(), wcfF2));
			ws.addCell(new Label(7, 2, bean_flag.getAbnormal_time(), wcfF2));
			ws.addCell(new Label(8, 2, format(bean_flag.getCount_minues()), wcfF2));
			ws.addCell(new Label(9, 2, "1", wcfF2));
			ws.addCell(new Label(10, 2, format(bean_flag.getCount_minues()), wcfF2));
			// ws.addCell(new Label(11,2, bean_flag.getGet_type(),wcfF2));
		} else// 最后一条也要加上
		{
			int curRow = ws.getRows();
			String[] ABNORMAL_TYPES = null;
			if (bean_flag.getAbnormal_type() != null) {
				ABNORMAL_TYPES = StringTool.getArrayByStringWithSpit(bean_flag
						.getAbnormal_type(), ",");
				if (ABNORMAL_TYPES.length > 1)
					ws.mergeCells(0, curRow, 0, curRow + ABNORMAL_TYPES.length
							- 1);
				ws
						.addCell(new Label(0, curRow, bean_flag.getFrequency(),
								wcfF2));
				if (ABNORMAL_TYPES.length > 1)
					ws.mergeCells(1, curRow, 1, curRow + ABNORMAL_TYPES.length
							- 1);
				ws
						.addCell(new Label(1, curRow, bean_flag.getPlay_time(),
								wcfF2));
				if (ABNORMAL_TYPES.length > 1)
					ws.mergeCells(2, curRow, 2, curRow + ABNORMAL_TYPES.length
							- 1);
				ws
						.addCell(new Label(2, curRow, bean_flag.getLanguage(),
								wcfF2));
				if (ABNORMAL_TYPES.length > 1)
					ws.mergeCells(3, curRow, 3, curRow + ABNORMAL_TYPES.length
							- 1);
				ws.addCell(new Label(3, curRow, bean_flag.getStation_name(),
						wcfF2));
				if (ABNORMAL_TYPES.length > 1)
					ws.mergeCells(4, curRow, 4, curRow + ABNORMAL_TYPES.length
							- 1);
				ws.addCell(new Label(4, curRow, bean_flag.getRemote_station(),
						wcfF2));
				for (int k = 0; k < ABNORMAL_TYPES.length; k++) {
					ws.addCell(new Label(5, curRow + k, ABNORMAL_TYPES[k],
							wcfF2));
				}
				// ws.addCell(new Label(5,curRow,
				// bean_flag.getAbnormal_type(),wcfF2));
				String[] ABNORMAL_DATES = StringTool.getArrayByStringWithSpit(
						bean_flag.getAbnormal_date(), ",");

				for (int k = 0; k < ABNORMAL_DATES.length; k++) {
					ws.addCell(new Label(6, curRow + k, ABNORMAL_DATES[k]
							.substring(0, 10), wcfF2));
				}
				// ws.addCell(new Label(6,curRow,
				// bean_flag.getAbnormal_date(),wcfF2));
				String[] ABNORMAL_TIMES = StringTool.getArrayByStringWithSpit(
						bean_flag.getAbnormal_time(), ",");
				for (int k = 0; k < ABNORMAL_TIMES.length; k++) {
					ws.addCell(new Label(7, curRow + k, ABNORMAL_TIMES[k],
							wcfF2));
				}
				// ws.addCell(new Label(7,curRow,
				// bean_flag.getAbnormal_time(),wcfF2));
				String[] COUNT_MINUES = StringTool.getArrayByStringWithSpit(
						bean_flag.getCount_minues(), ",");
				double count = StringTool.getCountByStringWithSpit(bean_flag
						.getCount_minues(), ",");

				for (int k = 0; k < COUNT_MINUES.length; k++) {
					ws
							.addCell(new Label(8, curRow + k,format(COUNT_MINUES[k]),
									wcfF2));
				}
				// ws.addCell(new Label(8,curRow,
				// bean_flag.getCount_minues(),wcfF2));
				if (ABNORMAL_TYPES.length > 1)
					ws.mergeCells(9, curRow, 9, curRow + ABNORMAL_TYPES.length
							- 1);
				ws.addCell(new Label(9, curRow, ABNORMAL_TYPES.length + "",
						wcfF2));
				if (ABNORMAL_TYPES.length > 1)
					ws.mergeCells(10, curRow, 10, curRow
							+ ABNORMAL_TYPES.length - 1);
				ws.addCell(new Label(10, curRow, format(count+""), wcfF2));
				// if(ABNORMAL_TYPES.length>1)
				// ws.mergeCells(11,curRow,11,curRow+ABNORMAL_TYPES.length-1);
				// ws.addCell(new Label(11,curRow,
				// bean_flag.getGet_type(),wcfF2));
			}

		}
		String[] ss = zongjivalue.split("&&");
		String[] sum = ss[ss.length - 1].split(",");
		int n = ws.getRows();
		ws.addCell(new Label(0, n, "总计", wcfF2));
		ws.addCell(new Label(1, n, "", wcfF2));
		ws.addCell(new Label(2, n, "", wcfF2));
		ws.addCell(new Label(3, n, "", wcfF2));
		ws.addCell(new Label(4, n, "", wcfF2));
		ws.addCell(new Label(5, n, "", wcfF2));
		ws.addCell(new Label(6, n, "", wcfF2));
		ws.addCell(new Label(7, n, "", wcfF2));
		ws.addCell(new Label(8, n, "", wcfF2));
		ws.addCell(new Label(9, n, sum[0], wcfF2));
		ws.addCell(new Label(10, n, format(sum[1]), wcfF2));
		wwb.write();
		wwb.close();
		outputStream.close();

	}

	public static String getNewString(String oldString, String findstring,
			String value1, String value2) {
		String newStr = "";
		if (oldString.indexOf(findstring) != -1) {
			String[] type = oldString.split("&&");
			for (int i = 0; i < type.length; i++) {
				String[] times = type[i].split(",");
				if (times.length == 3) {
					if (times[0].equals(findstring)) {
						String count = (Integer.parseInt(times[1]) + Integer
								.parseInt(value1))
								+ "";
						String time = (Double.parseDouble(times[2]) + Double
								.parseDouble(value2))
								+ "";
						newStr += findstring + "," + count + "," + time + "&&";
					} else {
						newStr += type[i].toString() + "&&";
					}
				} else if (times.length == 2) {

					String count = (Integer.parseInt(times[0]) + Integer
							.parseInt(value1))
							+ "";
					String time = (Double.parseDouble(times[1]) + Double
							.parseDouble(value2))
							+ "";
					newStr += count + "," + time;
				}

			}
		} else {
			return oldString;
		}
		return newStr;
	}

	public static void addcell(WritableSheet ws2, String key, String value,
			WritableCellFormat wcfF2) throws RowsExceededException,
			WriteException {
		int curRow = ws2.getRows();
		ws2.addCell(new Label(0, curRow, key, wcfF2));
		String[] type = value.split("&&");
		for (int i = 0; i < type.length; i++) {

			String[] times = type[i].split(",");
			if (i == type.length - 1) {
				ws2.addCell(new Label(i * 2 + 1, curRow, times[0], wcfF2));
				ws2.addCell(new Label(i * 2 + 2, curRow, format(times[1]), wcfF2));
			} else {
				ws2.addCell(new Label(i * 2 + 1, curRow, times[1], wcfF2));
				ws2.addCell(new Label(i * 2 + 2, curRow, format(times[2]), wcfF2));
			}

		}

	}
	private static String format(String str){
		if(!StringTool.isNumeric(str)){
			return "";
		}
		double number=Double.parseDouble(str);
		return StringTool.formatDouble(number, 2)+"";
	}
	/**************************************************
	 * 
	 * @Title: main
	 * 
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * 
	 * @param @param args 设定文件
	 * 
	 * @author 刘斌
	 * 
	 * @return void 返回类型
	 * 
	 * @throws
	 *************************************************/

	public static void main(String[] args) {
		DecimalFormat df = new DecimalFormat("0.00");
		double d=343434.453245;
		String result = df.format(d);
		System.out.println("=========="+result);
		// TODO Auto-generated method stub
//		String sb2 = " 豆腐干豆腐   http://10.15.6.12:8000/video/OAS07A_13869_20120918125457_20120918125527_639_R1.mp3";
//		// http://10.15.6.12:8000/video/OAS07A_13860_20120918113913_20120918114008_97400_R1.zip
//		String sb3 = sb2.replaceAll("http:([\\S\\s]+?)video/",
//				"http://10.15.6.122:8000/video/"); // 将端口替换为"$"
//		System.out.println(sb3);
//		// XmlReader.getAttrValue(itemName, childName, atrrName)
//		String ip = XmlReader.getAttrValue("HttpIp", "ip", "value");
//		System.out.println(ip);
	}

}
