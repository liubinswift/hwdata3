package com.viewscenes.web.dataquery.originalEffect;

import com.viewscenes.bean.report.OriginalEffectBean;
import com.viewscenes.printexcel.JExcel;
import com.viewscenes.util.LogTool;
import com.viewscenes.util.StringTool;
import flex.messaging.io.amf.ASObject;
import jxl.Workbook;
import jxl.write.*;
import org.jdom.Element;
import org.jmask.web.controller.EXEException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.ArrayList;

public class OriginalEffectQuery {
	/**
	 * 效果原始记录查询
	 * @detail  
	 * @method  
	 * @param asobj
	 * @return 
	 * @return  Object  
	 * @author  zhaoyahui
	 * @version 2013-2-26 下午05:40:34
	 */
	public Object queryOriginalEffect(ASObject asobj){
		String queryType = (String)asobj.get("queryType");//1、遥控站收测；2、采集点收测；3、互换资料；4、反馈收测；5、实地收测
		try{
			OriginalEffectBean bean = (OriginalEffectBean)asobj.get("bean");
			if(queryType.equals("1") || queryType.equals("2")){
				return queryOriginalEffectByRunplan(asobj);
			} else if(queryType.equals("3") || queryType.equals("4") || queryType.equals("5") ){
				return queryOriginalEffectByDateinput(asobj);
			}
		} catch (Exception e) {
			LogTool.fatal(e);
			return new EXEException("","效果原始记录查询异常："+e.getMessage(),"");
		}
		return new EXEException("","效果原始记录查询异常：查询类型不正确","");
	}
	/**
	 * 查询收测方式（互换资料、反馈收测、实地收测)的数据
	 * @detail  
	 * @method  
	 * @param asobj
	 * @return 
	 * @return  Object  
	 * @author  zhaoyahui
	 * @version 2013-2-27 上午11:22:24
	 */
	public Object queryOriginalEffectByDateinput(ASObject asobj) throws Exception{
		String beginTime = (String)asobj.get("beginTime");
		String start="2000-01-01 "+beginTime.split(" ")[1];
		String endTime = (String)asobj.get("endTime");
		String end = "2000-01-01 "+endTime.split(" ")[1];
		OriginalEffectBean bean = (OriginalEffectBean)asobj.get("bean");
		String queryType = (String)asobj.get("queryType");//1、遥控站收测；2、采集点收测；3、互换资料；4、反馈收测；5、实地收测
		if(queryType.equals("3")){
			queryType = "互换资料";
		} else if(queryType.equals("4")){
			queryType = "反馈收测";
		} else if(queryType.equals("5")){
			queryType = "实地收测";
		}

		String sql = "select data_type,freq,language_name,station_name,power,direction,service_area,ciraf,receive_country," +
				" receive_city,datasource,receive_date,receive_time,program_type,level_value,remark,s,i,o,START_TIME,END_TIME " +//START_TIME,END_TIME为后加为播音时间做准备
				" from RES_DATAINPUT_TAB input " +
				" where "+
				" input.datasource='"+queryType+"' " +
				" and end_time>=to_date('"+start+"','yyyy-mm-dd hh24:mi:ss')" +
				" and  start_time<=to_date('"+end+"','yyyy-mm-dd hh24:mi:ss') order by station_name,freq";
		 ASObject resobj =  StringTool.pageQuerySql(sql, asobj);
		 ArrayList reslist = (ArrayList)resobj.get("resultList");
		 ArrayList list = new ArrayList();
		for (int i = 0; i <reslist.size(); i++) {
			OriginalEffectBean newbean = new OriginalEffectBean();
			ASObject obj = (ASObject)reslist.get(i);
			String Play_time= (String)obj.get("start_time");
			newbean.setPlay_time(Play_time.split(" ")[1]);
			newbean.setLanguage_name((String)obj.get("language_name"));
			newbean.setFreq((String)obj.get("freq"));
			newbean.setTransmit_name((String)obj.get("station_name"));
			newbean.setTransmit_direction((String)obj.get("direction"));
			newbean.setTransmit_power((String)obj.get("power"));
			newbean.setService_area((String)obj.get("service_area"));
			newbean.setCiraf((String)obj.get("ciraf"));
			String Receive_date= (String)obj.get("receive_date");
			newbean.setReceive_date(Receive_date.split(" ")[0]);
			newbean.setReceive_time((String)obj.get("receive_time"));
			newbean.setReceive_station_yaokong("");
			newbean.setReceive_station_caiji("");
			newbean.setFens((String)obj.get("s"));
			newbean.setFeni((String)obj.get("i"));
			newbean.setFeno((String)obj.get("o"));
			newbean.setLevel((String)obj.get("level_value"));
			newbean.setBak((String)obj.get("remark"));
			newbean.setTransmit_country((String)obj.get("receive_country"));
			newbean.setTransmit_city((String)obj.get("receive_city"));
			newbean.setRedisseminators((String)obj.get("station_name"));
			newbean.setType(bean.getType());
			list.add(newbean);
		}
		resobj.put("resultList",list);
		return resobj;
	}
	
	/**
	 * 查询收测方式（遥控站收测、采集点收测)的数据
	 * @detail  
	 * @method  
	 * @param asobj
	 * @return 
	 * @return  Object  
	 * @author  zhaoyahui
	 * @version 2013-2-27 上午11:22:53
	 */
	public Object queryOriginalEffectByRunplan(ASObject asobj) throws Exception{
		String beginTime = (String)asobj.get("beginTime");
		String endTime = (String)asobj.get("endTime");
		String headcodes = (String)asobj.get("headcodes");
		String queryType = (String)asobj.get("queryType");//1、遥控站收测；2、采集点收测；3、互换资料；4、反馈收测；5、实地收测
		String subSql = "";
//		if(!headcodes.equals("")){
//			String[] headcodeArr = headcodes.split(",");
//			for(int i=0;i<headcodeArr.length;i++){
//				if(queryType.equals("1")){
//					subSql += "'"+headcodeArr[i] + "A','"+headcodeArr[i] + "B',";
//				} else if(queryType.equals("2")){
//					subSql += "'"+headcodeArr[i] + "',";
//				}
//			}
//		}
//		if(!subSql.equals("")){
//			subSql = subSql.substring(0, subSql.length()-1);
//			subSql = " and head.shortname in ("+subSql+") ";
//		}
		if(headcodes!=null&&!headcodes.equalsIgnoreCase("")){
			if(headcodes.split(",").length>1){
				String[] ss = headcodes.split(",");
				String newsql="";
				for(int m=0;m<ss.length;m++){
					newsql+=" head.code like '%"+ss[m]+"%' or";
				}
				subSql+=" and ("+newsql.substring(0, newsql.length()-2)+")";
			}else{
				//sql+="and '"+headcodes.substring(0, headcodes.length()-1)+"' like '%'||head.code||'%' ";
				subSql+=" and head.code  like '%"+headcodes+"%'";
			}
			
		}
//		if(!headcodes.equals("")){
//			subSql = " and head.code in ('"+headcodes+"') ";
//		}
		OriginalEffectBean bean = (OriginalEffectBean)asobj.get("bean");
		if(queryType.equals("1")){
			subSql += " and head.type_id = '102'";
		} else if( queryType.equals("2") ){
			subSql += " and head.type_id = '101'";
		}
		String sql = "select runplan.start_time ||'-'|| runplan.end_time as play_time,lan.language_name, runplan.freq, decode(runplan.runplan_type_id,'1',station.name ,runplan.redisseminators )as transmit_name,head.type_id," +
				" runplan.direction transmit_direction,runplan.ciraf,record.start_datetime,record.level_value,runplan.launch_country,city.city,runplan.redisseminators," +
				" runplan.power transmit_power,runplan.start_time runplan_play_time, head.code  headcode, head.shortname receive_station,mark.counts,mark.counti,mark.counto,head.service_name" +
				" from RADIO_MARK_ZST_VIEW_TAB mark," +
				" radio_stream_result_tab record," +
				" zres_runplan_chaifen_tab runplan," +
				" zdic_language_tab lan ," +
				" res_transmit_station_tab station," +
				" res_headend_tab head," +
				" res_city_tab city "+
				" where mark.mark_file_url=record.url and mark.runplan_id=runplan.runplan_id and  runplan.language_id=lan.language_id " +
				" and runplan.station_id=station.station_id(+) and head.head_id=record.head_id" +
				" and runplan.sentcity_id=city.id(+) "+
				" and record.report_type=1 " +//效果录音
				" and record.is_delete=0 " +
				" and runplan.runplan_type_id = '"+bean.getType()+"'"+
				" and record.start_datetime>=to_date('"+beginTime+"','yyyy-mm-dd hh24:mi:ss')" +
				subSql +
				" and   record.start_datetime<=to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss') order by to_char(start_datetime,'yyyy-mm-dd'),headcode,play_time,runplan.freq, station.name,lan.language_name";
		ASObject resobj =  StringTool.pageQuerySql(sql, asobj);
		 ArrayList reslist = (ArrayList)resobj.get("resultList");

		 ArrayList list = new ArrayList();
		for (int i = 0; i < reslist.size(); i++) {
			OriginalEffectBean newbean = new OriginalEffectBean();
			ASObject obj = (ASObject)reslist.get(i);
			newbean.setPlay_time((String)obj.get("runplan_play_time"));
			newbean.setLanguage_name((String)obj.get("language_name"));
			newbean.setFreq((String)obj.get("freq"));
			newbean.setTransmit_name((String)obj.get("transmit_name"));
			newbean.setTransmit_direction((String)obj.get("transmit_direction"));
			newbean.setTransmit_power((String)obj.get("transmit_power"));
			newbean.setService_area((String)obj.get("service_name"));
			newbean.setCiraf((String)obj.get("ciraf"));
			newbean.setReceive_date(((String)obj.get("start_datetime")).split(" ")[0]);
			newbean.setReceive_time(((String)obj.get("start_datetime")).split(" ")[1]);
			if(((String)obj.get("type_id")).equals("102")){
				String name = "";
				if( ((String)obj.get("receive_station")).endsWith("A") || ((String)obj.get("receive_station")).endsWith("B")
						|| ((String)obj.get("receive_station")).endsWith("C")|| ((String)obj.get("receive_station")).endsWith("D")
						|| ((String)obj.get("receive_station")).endsWith("E")|| ((String)obj.get("receive_station")).endsWith("F")
						|| ((String)obj.get("receive_station")).endsWith("G") ){
					name = ((String)obj.get("receive_station")).substring(0, ((String)obj.get("receive_station")).length()-1);
				} else{
					name =(String)obj.get("receive_station");
				}
				newbean.setReceive_station_yaokong(name);
			}  else if(((String)obj.get("type_id")).equals("101")){
				newbean.setReceive_station_caiji((String)obj.get("receive_station"));
			}
			newbean.setFens((String)obj.get("counts"));
			newbean.setFeni((String)obj.get("counti"));
			newbean.setFeno((String)obj.get("counto"));
			newbean.setLevel((String)obj.get("level_value"));
			newbean.setBak("");
			newbean.setTransmit_country((String)obj.get("launch_country"));
			newbean.setTransmit_city((String)obj.get("city"));
			newbean.setRedisseminators((String)obj.get("redisseminators"));
			newbean.setType(bean.getType());
			list.add(newbean);
		}
		resobj.put("resultList",list);
		return resobj;
	}
	
	public void doExcel(String msg,HttpServletRequest request,HttpServletResponse response) throws Exception{
		Element root = StringTool.getXMLRoot(msg);
		String programType = root.getChildText("programType");
		String queryType = root.getChildText("queryType");
		String beginTime = root.getChildText("beginTime");
		String endTime = root.getChildText("endTime");
		String headcodes = root.getChildText("headcodes");

		String fileName="海外站点效果原始记录表";
		ASObject obj = new ASObject();
		obj.put("beginTime", beginTime);
		obj.put("endTime", endTime);
		obj.put("queryType", queryType);
		obj.put("headcodes", headcodes);
		obj.put("startRow", 1);
		obj.put("endRow", 100000);
		OriginalEffectBean oldbean = new OriginalEffectBean();
		oldbean.setType(programType);
		obj.put("bean", oldbean);
		ASObject resobj = (ASObject) queryOriginalEffect(obj);
		ArrayList list = (ArrayList)resobj.get("resultList");
		JExcel jExcel = new JExcel();
		
		OutputStream outputStream = response.getOutputStream();
		response.setContentType("application/vnd.ms-excel");
		response.reset();
		response.setHeader("Location", "Export.xls");
		response.setHeader("Expires", "0");
        response.setHeader("Content-disposition", "attachment; filename="+new String((fileName).getBytes("GBK"),"ISO-8859-1")+".xls");
        
		WritableWorkbook wwb =Workbook.createWorkbook(outputStream);
		int sheetNum = 0;
		
		 WritableFont wfTitle = new WritableFont(WritableFont.createFont("黑体"), 12,WritableFont.BOLD, false);
         WritableCellFormat wcfFTitle = new WritableCellFormat(wfTitle);
		wcfFTitle.setAlignment(jxl.format.Alignment.CENTRE);
		wcfFTitle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			 
		WritableFont wfHead = new WritableFont(WritableFont.createFont("黑体"), 10,WritableFont.BOLD, false);
		WritableCellFormat wcfFHead = new WritableCellFormat(wfHead);
		wcfFHead.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);//带边框 
		wcfFHead.setWrap(true);//自动换行
         //用于Number的格式
         //jxl.write.NumberFormat nf = new jxl.write.NumberFormat("0.00"); 
         //jxl.write.WritableCellFormat priceformat = new jxl.write.WritableCellFormat(nf);
         // 把水平对齐方式指定为左对齐
		wcfFHead.setAlignment(jxl.format.Alignment.CENTRE);
         // 把垂直对齐方式指定为居中对齐
		wcfFHead.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		
		WritableFont wf1 = new WritableFont(WritableFont.createFont("宋体"), 10,WritableFont.NO_BOLD, false);
        jxl.write.WritableCellFormat wcfF2 = new jxl.write.WritableCellFormat(wf1);
        wcfF2.setAlignment(jxl.format.Alignment.CENTRE);
        wcfF2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        wcfF2.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); 
        

		for(int i=0;i<list.size();i++){
			OriginalEffectBean bean = (OriginalEffectBean)list.get(i);
			WritableSheet ws =  null;
			if(bean.getType().equals("1")){//　　　国际台节目原始记录表
				if(wwb.getNumberOfSheets() == 0){
					ws = wwb.createSheet("国际台节目原始记录", wwb.getNumberOfSheets()+1);
				    
                     //设置列名
                     ws.addCell(new Label(0,1,"语言",wcfFHead));
                     ws.addCell(new Label(1,1,"频率",wcfFHead));
                     ws.addCell(new Label(2,1,"播音时间",wcfFHead));
                     ws.addCell(new Label(3,1,"发射台",wcfFHead));
                     ws.addCell(new Label(4,1,"发射方向",wcfFHead));
                     ws.addCell(new Label(5,1,"发射功率",wcfFHead));
                     ws.addCell(new Label(6,1,"服务区",wcfFHead));
                     ws.addCell(new Label(7,1,"CIRAF区",wcfFHead));
                     ws.addCell(new Label(8,1,"收测日期",wcfFHead));
                     ws.addCell(new Label(9,1,"收测时间",wcfFHead));
                     ws.addCell(new Label(10,1,"遥控站",wcfFHead));
                     ws.addCell(new Label(11,1,"S",wcfFHead));
                     ws.addCell(new Label(12,1,"I",wcfFHead));
                     ws.addCell(new Label(13,1,"O",wcfFHead));
                     ws.addCell(new Label(14,1,"电平",wcfFHead));
                     ws.addCell(new Label(15,1,"备注",wcfFHead));

                     ws.mergeCells(0,0,15,0);
                    
                     ws.addCell(new Label(0, 0, "国际台节目原始记录表",wcfFTitle));
                     ws.setRowView(0, 740);//设置第一行高度
                     ws.setRowView(1, 740);
                     ws.setColumnView(0, 5);
                     ws.setColumnView(1, 7);
                     ws.setColumnView(2, 13);
                     ws.setColumnView(3, 9);
                     ws.setColumnView(4, 5);
                     ws.setColumnView(5, 5);
                     ws.setColumnView(6, 11);
                     ws.setColumnView(7, 11);
                     ws.setColumnView(8, 13);
                     ws.setColumnView(9, 13);
                     ws.setColumnView(10, 13);
                     ws.setColumnView(11, 5);
                     ws.setColumnView(12, 5);
                     ws.setColumnView(13, 5);
                     ws.setColumnView(14, 5);
                     ws.setColumnView(15, 6);
                     ws.setColumnView(16, 28);
                     
				} else  {
					ws = wwb.getSheet("国际台节目原始记录"); 
				}
				int curRow = ws.getRows();
				ws.addCell(new Label(0,curRow, bean.getLanguage_name(),wcfF2));
				ws.addCell(new Label(1,curRow, bean.getFreq(),wcfF2));
				ws.addCell(new Label(2,curRow, bean.getPlay_time(),wcfF2));
				ws.addCell(new Label(3,curRow, bean.getTransmit_name(),wcfF2));
				ws.addCell(new Label(4,curRow, bean.getTransmit_direction(),wcfF2));
				ws.addCell(new Label(5,curRow, bean.getTransmit_power(),wcfF2));
				ws.addCell(new Label(6,curRow, bean.getService_area(),wcfF2));
				ws.addCell(new Label(7,curRow, bean.getCiraf(),wcfF2));
				ws.addCell(new Label(8,curRow, bean.getReceive_date(),wcfF2));
				ws.addCell(new Label(9,curRow, bean.getReceive_time(),wcfF2));
				ws.addCell(new Label(10,curRow, bean.getReceive_station_yaokong(),wcfF2));
				ws.addCell(new Label(11,curRow, bean.getFens(),wcfF2));
				ws.addCell(new Label(12,curRow, bean.getFeni(),wcfF2));
				ws.addCell(new Label(13,curRow, bean.getFeno(),wcfF2));
				ws.addCell(new Label(14,curRow, bean.getLevel(),wcfF2));
				ws.addCell(new Label(15,curRow, bean.getBak(),wcfF2));

			} else if(bean.getType().equals("2")){//　　　海外落地节目原始记录表
				if(wwb.getNumberOfSheets() == 0){
					ws = wwb.createSheet("海外落地节目原始记录表", wwb.getNumberOfSheets()+1);
				    
                     //设置列名
                     ws.addCell(new Label(0,1,"语言",wcfFHead));
                     ws.addCell(new Label(1,1,"频率",wcfFHead));
                     ws.addCell(new Label(2,1,"播音时间",wcfFHead));
                     ws.addCell(new Label(3,1,"发射国家",wcfFHead));
                     ws.addCell(new Label(4,1,"发射城市",wcfFHead));
                     ws.addCell(new Label(5,1,"转播机构",wcfFHead));
                     ws.addCell(new Label(6,1,"发射功率",wcfFHead));
                     ws.addCell(new Label(7,1,"服务区",wcfFHead));
                     ws.addCell(new Label(8,1,"收测日期",wcfFHead));
                     ws.addCell(new Label(9,1,"收测时间",wcfFHead));
                     ws.addCell(new Label(10,1,"遥控站",wcfFHead));
                     ws.addCell(new Label(11,1,"采集点",wcfFHead));
                     ws.addCell(new Label(12,1,"S",wcfFHead));
                     ws.addCell(new Label(13,1,"I",wcfFHead));
                     ws.addCell(new Label(14,1,"O",wcfFHead));
                     ws.addCell(new Label(15,1,"电平",wcfFHead));
                     ws.addCell(new Label(16,1,"备注",wcfFHead));

                     ws.mergeCells(0,0,16,0);
                    
                     ws.addCell(new Label(0, 0, "海外落地节目原始记录表",wcfFTitle));
                     ws.setRowView(0, 740);//设置第一行高度
                     ws.setRowView(1, 740);
                     ws.setColumnView(0, 5);
                     ws.setColumnView(1, 7);
                     ws.setColumnView(2, 13);
                     ws.setColumnView(3, 9);
                     ws.setColumnView(4, 5);
                     ws.setColumnView(5, 5);
                     ws.setColumnView(6, 11);
                     ws.setColumnView(7, 6);
                     ws.setColumnView(8, 6);
                     ws.setColumnView(9, 11);
                     ws.setColumnView(10, 13);
                     ws.setColumnView(11, 13);
                     ws.setColumnView(12, 13);
                     ws.setColumnView(13, 13);
                     ws.setColumnView(14, 5);
                     ws.setColumnView(15, 6);
                     ws.setColumnView(16, 28);
                     
				} else  {
					ws = wwb.getSheet("海外落地节目原始记录表"); 
				}
				int curRow = ws.getRows();
				ws.addCell(new Label(0,curRow, bean.getLanguage_name(),wcfF2));
				ws.addCell(new Label(1,curRow, bean.getFreq(),wcfF2));
				ws.addCell(new Label(2,curRow, bean.getPlay_time(),wcfF2));
				ws.addCell(new Label(3,curRow, bean.getTransmit_country(),wcfF2));
				ws.addCell(new Label(4,curRow, bean.getTransmit_city(),wcfF2));
				ws.addCell(new Label(5,curRow, bean.getRedisseminators(),wcfF2));
				ws.addCell(new Label(6,curRow, bean.getTransmit_power(),wcfF2));
				ws.addCell(new Label(7,curRow, bean.getService_area(),wcfF2));
				ws.addCell(new Label(8,curRow, bean.getReceive_date(),wcfF2));
				ws.addCell(new Label(9,curRow, bean.getReceive_time(),wcfF2));
				ws.addCell(new Label(10,curRow, bean.getReceive_station_yaokong(),wcfF2));
				ws.addCell(new Label(11,curRow, bean.getReceive_station_caiji(),wcfF2));
				ws.addCell(new Label(12,curRow, bean.getFens(),wcfF2));
				ws.addCell(new Label(13,curRow, bean.getFeni(),wcfF2));
				ws.addCell(new Label(14,curRow, bean.getFeno(),wcfF2));
				ws.addCell(new Label(15,curRow, bean.getLevel(),wcfF2));
				ws.addCell(new Label(16,curRow, bean.getBak(),wcfF2));
			}
		
		}
	    	
			wwb.write();
	        wwb.close();
	        outputStream.close();
	}
	/**     
	 * @detail  
	 * @method  
	 * @param args 
	 * @return  void  
	 * @author  zhaoyahui
	 * @version 2013-2-26 下午05:16:07
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
