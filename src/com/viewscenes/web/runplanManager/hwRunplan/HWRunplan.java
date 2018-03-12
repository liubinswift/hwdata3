package com.viewscenes.web.runplanManager.hwRunplan;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Element;
import org.jmask.web.controller.EXEException;

import com.viewscenes.bean.runplan.GJTLDRunplanBean;
import com.viewscenes.bean.runplan.GJTRunplanBean;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.printexcel.JExcel;
import com.viewscenes.pub.GDSet;
import com.viewscenes.pub.GDSetException;
import com.viewscenes.pub.GDSetTool;
import com.viewscenes.util.StringTool;
import com.viewscenes.web.common.Common;
import com.viewscenes.web.runplanManager.gjtRunplan.GJTRunplanDAO;

import flex.messaging.io.amf.ASObject;

public class HWRunplan {
	
	
	/**
	 * 添加运行图操作
	 * @param bean
	 * @return
	 */
	public Object addRunplan(GJTLDRunplanBean bean){
		String message="";
		try {
			HWRunplanDAO grldao = new HWRunplanDAO();
			message=grldao.addRunplan(bean);
		} catch (Exception e) {
			e.printStackTrace();
			return new EXEException("", e.getMessage(), "");
		}
		return message;
	}

	 /**
	    * 查询运行图
	    * @param bean
	    * @return
	    */
		public Object queryRunplan(GJTLDRunplanBean bean){
			HWRunplanDAO hwdao = new HWRunplanDAO();
			ASObject resObj;
			ArrayList result = new ArrayList();
			try {
				 resObj = hwdao.queryRunplan(bean);
				 ArrayList list =(ArrayList) resObj.get("resultList") ;//取出对象的结果集
				 if(list.size()>0){
					 for(int i=0;i<list.size();i++){
						 String classpath = GJTLDRunplanBean.class.getName();
						 GJTLDRunplanBean bean1 = (GJTLDRunplanBean) StringTool.convertObjToBean((ASObject) list.get(i),classpath);
						// GJTRunplanBean bean1 = (GJTRunplanBean) list.get(i);
						 if(bean1.getMon_area()!=null&&!bean1.getMon_area().equalsIgnoreCase("")){
							 String mon_area = bean1.getMon_area();
							 String[] s = mon_area.split(",");
							 if(s.length>0){
								 String mon_name="";
								 for(int j=0;j<s.length;j++){
									 mon_name+=Common.getShortNameByCode(s[j])+",";
								 }
								 bean1.setMon_area_name(mon_name.substring(0, mon_name.length()-1));
							 } 
						 }
						 if(bean1.getXg_mon_area()!=null&&!bean1.getXg_mon_area().equalsIgnoreCase("")){
							 String xg_mon_area = bean1.getXg_mon_area();
							 String[] s = xg_mon_area.split(",");
							 if(s.length>0){
								 String xg_mon_name="";
								 for(int j=0;j<s.length;j++){
									 xg_mon_name+=Common.getShortNameByCode(s[j])+",";
								 }
								 bean1.setXg_mon_area_name(xg_mon_name.substring(0, xg_mon_name.length()-1));
							 }
						 }
						 result.add(bean1);
					 }
					 resObj.put("resultList", result);
				 }
//				 String classpath = GJTLDRunplanBean.class.getName();
//				 result = StringTool.convertFlexToJavaList(list, classpath);//将对象类型转换成bean，再放回结果集中。
//				 resObj.put("resultList", result);
			} catch (Exception e) {
				e.printStackTrace();
				EXEException exe = new EXEException("", 
						e.getMessage(), "");
				return exe ;
			}
			
			return resObj;
		}
		
		/**
		 * 修改运行图
		 * @param bean
		 * @return
		 */
		public Object updateRunplan(GJTLDRunplanBean bean){
			HWRunplanDAO hwdao = new HWRunplanDAO();
			String res="修改运行图成功!";
			String runplan_id = bean.getRunplan_id();   //运行图id
			String launch_country = bean.getLaunch_country();
			String sentcity_id = bean.getSentcity_id();  //发射城市id
			String redisseminators = bean.getRedisseminators();//转播机构
			String freq =bean.getFreq();      //频率
			String band = bean.getBand();  //波段
			String direction = bean.getDirection(); //方向
			String language_id = bean.getLanguage_id(); //语言id
			String power = bean.getPower();  //功率
			String service_area = bean.getService_area(); //服务区
			String start_time = bean.getStart_time(); //播音开始时间
			String end_time = bean.getEnd_time();   //播音结束时间
			//String local_time = bean.getLocal_time();//当地时间
			String summer = bean.getSummer();//季节类型
			String mon_area = bean.getMon_area();    //质量收测站点
			String xg_mon_area = bean.getXg_mon_area();//效果收测站点
			String rest_datetime = bean.getRest_datetime(); // //休息日期
			String valid_start_time = bean.getValid_start_time(); //启用期
			String valid_end_time = bean.getValid_end_time();  //停用期
			String remark = bean.getRemark();//备注
			String input_person = bean.getInput_person(); //录入人
			String weekday = bean.getWeekday();//周设置
			String[] times=Common.getTimeDif(start_time, end_time, launch_country);
			StringBuffer sbf = new StringBuffer("update zres_runplan_tab set ");
			sbf.append(" LAUNCH_COUNTRY='"+launch_country+"',sentcity_id='"+sentcity_id+"',REDISSEMINATORS='"+redisseminators+"',FREQ='"+freq+"',band='"+band+"',");
			sbf.append("START_TIME='"+start_time+"',END_TIME='"+end_time+"',LOCAL_START_TIME='"+times[0]+"',LOCAL_END_TIME='"+times[1]+"',SUMMER='"+summer+"',");
			sbf.append("LANGUAGE_ID='"+language_id+"',DIRECTION='"+direction+"',POWER='"+power+"',SERVICE_AREA='"+service_area+"',weekday='"+weekday+"',");
			sbf.append("REMARK='"+remark+"',MON_AREA='"+mon_area+"',xg_mon_area='"+xg_mon_area+"',REST_DATETIME='"+rest_datetime+"',");
			sbf.append("VALID_START_TIME='"+valid_start_time+"',VALID_END_TIME='"+valid_end_time+"',STORE_DATETIME=sysdate,INPUT_PERSON='"+input_person+"',");
			sbf.append("IS_DELETE=0 where runplan_id='"+runplan_id+"' and RUNPLAN_TYPE_ID=2");
			try {
				DbComponent.exeUpdate(sbf.toString());
			} catch (Exception e) {
				e.printStackTrace();
				return new EXEException("",e.getMessage(),"");
			}
			return res;
		}
		
		/**
		 * 批量修改运行图
		 * @param obj
		 * @return
		 */
		public Object batchupdateRunplan(ASObject obj){
			String res="批量修改运行图成功!";
			StringBuffer sql=new StringBuffer("update zres_runplan_tab t set ");
			String code=(String) obj.get("code");
			String seasonType = (String) obj.get("seasonType");
			if(obj.get("valid_starttime")!=null&&!obj.get("valid_starttime").toString().equalsIgnoreCase("")){
				sql.append(" VALID_START_TIME='"+obj.get("valid_starttime").toString()+"',");
			}
			if(obj.get("valid_endtime")!=null&&!obj.get("valid_endtime").toString().equalsIgnoreCase("")){
				sql.append(" VALID_END_TIME='"+obj.get("valid_endtime").toString()+"',");
			}
			sql.append(" STORE_DATETIME=sysdate,INPUT_PERSON='"+obj.get("user_name").toString()+"'");
			sql.append(" where runplan_type_id=2 ");
			if(code!=null&&!code.equalsIgnoreCase("")){
				if(code.indexOf(",")>0){
					String[] s=code.toString().split(",");
					StringBuffer temp = new StringBuffer("(");
					for(int i=0;i<s.length;i++){
						temp.append(" mon_area||','||xg_mon_area like '%"+s[i]+"%' or");
					}
					sql.append(" and "+temp.toString().substring(0, temp.toString().length()-2)+")");
				}else{
					sql.append(" and mon_area||','||xg_mon_area like '%"+code+"%'");
				}
			}
			if(seasonType!=null&&!seasonType.equalsIgnoreCase("")){
				sql.append(" and summer='"+seasonType+"'");
			}
			
			try {
				DbComponent.exeUpdate(sql.toString());
			} catch (Exception e) {
				e.printStackTrace();
				return new EXEException("",e.getMessage(),"");
			}
			return res;
		}
		
		/**
		 * 删除运行图
		 * @param ids
		 * @return
		 */
		public Object delRunplan(String ids){
			String message="删除运行图成功!";
			String sql="update zres_runplan_tab set is_delete=1 where runplan_type_id=2 and runplan_id in("+ids+")";
			try {
				DbComponent.exeUpdate(sql);
			} catch (Exception e) {
				e.printStackTrace();
				return new EXEException("","删除运行图异常"+e.getMessage(),"");
			}
			return message;
		}
		/**
		 * 根据北京时间和时差计算国外的当地时间
		 * @param time
		 * @return
		 */
		public String calculateMoveut(String city){
			String s="";
			String sql="select t.moveut from res_city_tab t ,res_headend_tab t1 where t1.shortname like '%'||t.city||'%'";
			try {
				DbComponent.Query(sql);
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return s;
		}
		
		public void doExcel(String msg,HttpServletRequest request,HttpServletResponse response) throws GDSetException{
			Element root = StringTool.getXMLRoot(msg);
			String runplan_type_id = root.getChildText("runplan_type");
			String country = root.getChildText("country");
			String freq = root.getChildText("freq");
			String sendcity = root.getChildText("sendcity");
			String redisseminators = root.getChildText("redisseminators");
			String language_id = root.getChildText("language_id");
			String season = root.getChildText("season");
			String power = root.getChildText("power");
			String service_area = root.getChildText("service_area");
			String direction = root.getChildText("direction");
			String mon_area = root.getChildText("mon_area");
			String xg_mon_area = root.getChildText("xg_mon_area");
			String valid_start_time = root.getChildText("valid_start_time");
			String valid_end_time = root.getChildText("valid_end_time");
			String sql="select t.launch_country,rct.city,t.redisseminators,t.freq,zlt.language_name,t.direction,t.power," +
					"t.service_area,t.rest_datetime,t.summer,t.start_time,t.end_time,t.local_start_time,t.local_end_time," +
					"valid_start_time,valid_end_time,xg_mon_area,mon_area,remark" +
					" from zres_runplan_tab t,zdic_language_tab zlt,res_city_tab rct where t.is_delete=0 and t.sentcity_id=rct.id and runplan_type_id='"+runplan_type_id+"'  and  t.language_id=zlt.language_id  ";
			if(freq!=null && !freq.equalsIgnoreCase("")){
				sql+=" and t.freq='"+freq+"'";
			}
			if(country!=null&&!country.equalsIgnoreCase("")){
				sql+=" and t.launch_country='"+country+"'";
			}
			if(sendcity!=null && !sendcity.equalsIgnoreCase("")){
				sql+=" and t.sentcity_id='"+sendcity+"'";
			}
			if(redisseminators!=null && !redisseminators.equalsIgnoreCase("")){
				sql+=" and t.redisseminators like '%"+redisseminators+"%'";
			}
			if(language_id!=null&&!language_id.equalsIgnoreCase("")){
				sql+=" and t.language_id="+language_id+"";
			}
			if(power!=null &&!power.equalsIgnoreCase("")){
				sql+=" and t.power="+power+"";
			}
			if(season!=null && !season.equalsIgnoreCase("")){
				sql+=" and t.summer like '%"+season+"%'";
			}
			if(service_area!=null && !service_area.equalsIgnoreCase("")){
				sql+=" and t.service_area like '%"+service_area+"%'";
			}
			if(direction!=null && !direction.equalsIgnoreCase("")){
				sql+=" and t.direction='"+direction+"'";
			}
			if(valid_start_time!=null && !valid_start_time.equalsIgnoreCase("")){
				sql+=" and (t.valid_start_time>=to_date('"+valid_start_time+"','yyyy-MM-dd HH24:MI:SS') and t.valid_start_time<=to_date('"+valid_end_time+"','yyyy-MM-dd HH24:MI:SS'))";
			}
			if(valid_end_time!=null && !valid_end_time.equalsIgnoreCase("")){
				sql+=" and t.valid_end_time>=to_date('"+valid_end_time+"','yyyy-MM-dd HH24:MI:SS')";
			}
//			//新要求，田雅静提出如果开始时间和结束时间都为空，需要让有效期开始时间小于当前时间。
//			if((valid_start_time==null ||valid_start_time.equalsIgnoreCase(""))&&(valid_end_time==null ||valid_end_time.equalsIgnoreCase(""))){
//				sql+=" and t.valid_start_time<=sysdate ";	
//			}
			if(mon_area!=null && !mon_area.equalsIgnoreCase("")){
				if(mon_area.indexOf(",")>0){
					String[] s=mon_area.split(",");
					StringBuffer temp = new StringBuffer("(");
					for(int i=0;i<s.length;i++){
						temp.append(" t.mon_area like '%"+s[i]+"%' or");
					}
					sql+=" and "+temp.toString().substring(0, temp.toString().length()-2)+")";
				}else{
					sql+=" and t.mon_area like '%"+mon_area+"%'";
				}
				
			}
			if(xg_mon_area!=null && !xg_mon_area.equalsIgnoreCase("")){
				if(xg_mon_area.indexOf(",")>0){
					String[] s=xg_mon_area.split(",");
					StringBuffer temp = new StringBuffer("(");
					for(int i=0;i<s.length;i++){
						temp.append(" t.xg_mon_area like '%"+s[i]+"%' or");
					}
					sql+=" and "+temp.toString().substring(0, temp.toString().length()-2)+")";
				}else{
					sql+=" and t.xg_mon_area like '%"+xg_mon_area+"%'";
				}
				
			}
			sql+=" order by t.xg_mon_area,t.mon_area,redisseminators,service_area,t.start_time||'-'||t.end_time,freq,language_name";
				
			String fileName = "海外落地运行图";
			String downFileName = "";
			GDSet gd=null;
			try {
				gd= DbComponent.Query(sql,GDSetTool.Return_ArrayListRow);
				JExcel jExcel = new JExcel();
				downFileName=jExcel.openDocument();
				jExcel.WorkBookGetSheet(0);
				for(int i=0;i<gd.getRowCount();i++){
					for(int j=0;j<19;j++){
						jExcel.addDate(j, i+1,(String)gd.getCell(i, j),jExcel.dateCellFormat);
						jExcel.getWorkSheet().setRowView(i+1, 500);
					}
				}
				jExcel.getWorkSheet().setColumnView(0, 20);
		    	jExcel.getWorkSheet().setColumnView(1, 10);
		    	jExcel.getWorkSheet().setColumnView(2, 15);	
		    	jExcel.getWorkSheet().setColumnView(3, 10);
		    	jExcel.getWorkSheet().setColumnView(4, 10);
		    	jExcel.getWorkSheet().setColumnView(5, 10);	
		    	jExcel.getWorkSheet().setColumnView(6, 10);	
		    	jExcel.getWorkSheet().setColumnView(7, 10);	
		    	jExcel.getWorkSheet().setColumnView(8, 10);
		    	jExcel.getWorkSheet().setColumnView(9, 10);
		    	jExcel.getWorkSheet().setColumnView(10, 10);
		    	jExcel.getWorkSheet().setColumnView(11, 10);
		    	jExcel.getWorkSheet().setColumnView(12, 20);
		    	jExcel.getWorkSheet().setColumnView(13, 10);	
		    	jExcel.getWorkSheet().setColumnView(14, 10);
		    	jExcel.getWorkSheet().setColumnView(15, 20);
		    	jExcel.getWorkSheet().setColumnView(16, 20);	
		    	jExcel.getWorkSheet().setColumnView(17, 20);	
		    	jExcel.getWorkSheet().setColumnView(18, 10);	
		    	
		    	jExcel.addDate(0, 0,"发射国家",jExcel.dateTITLEFormat);
		    	jExcel.addDate(1, 0,"发射城市",jExcel.dateTITLEFormat);
		    	jExcel.addDate(2, 0,"转播机构",jExcel.dateTITLEFormat);
		    	jExcel.addDate(3, 0,"频率(KHZ)",jExcel.dateTITLEFormat);
		    	jExcel.addDate(4, 0,"语言",jExcel.dateTITLEFormat);
		    	jExcel.addDate(5, 0,"方向",jExcel.dateTITLEFormat);
		    	jExcel.addDate(6, 0,"功率",jExcel.dateTITLEFormat);
		    	jExcel.addDate(7, 0,"服务区",jExcel.dateTITLEFormat);
		    	jExcel.addDate(8, 0,"播音日期",jExcel.dateTITLEFormat);
		    	jExcel.addDate(9, 0,"季节类型",jExcel.dateTITLEFormat);
		    	jExcel.addDate(10, 0,"开始时间",jExcel.dateTITLEFormat);
		    	jExcel.addDate(11, 0,"结束时间",jExcel.dateTITLEFormat);
		    	jExcel.addDate(12, 0,"当地开始时间",jExcel.dateTITLEFormat);
		    	jExcel.addDate(13, 0,"当地结束时间",jExcel.dateTITLEFormat);
		    	jExcel.addDate(14, 0,"启用期",jExcel.dateTITLEFormat);
		    	jExcel.addDate(15, 0,"停用期",jExcel.dateTITLEFormat);
		    	jExcel.addDate(16, 0,"效果收测站点",jExcel.dateTITLEFormat);
		    	jExcel.addDate(17, 0,"质量收测站点",jExcel.dateTITLEFormat);
		    	jExcel.addDate(18, 0,"备注",jExcel.dateTITLEFormat);
		    	jExcel.getWorkSheet().setRowView(0, 500);
		    	jExcel.saveDocument();
		    	response.setContentType("application/vnd.ms-excel");
		        response.setHeader("Location", "Export.xls");
		        response.setHeader("Expires", "0");
		        response.setHeader("Content-Disposition",new String(("filename="+fileName).getBytes("GBK"),"ISO-8859-1")+".xls");
		        OutputStream outputStream = response.getOutputStream();
		        InputStream inputStream = new FileInputStream(downFileName);
		        byte[] buffer = new byte[1024];
		        int i = -1;
		        while ( (i = inputStream.read(buffer)) != -1) {
		          outputStream.write(buffer, 0, i);
		        }
		        outputStream.flush();
		        outputStream.close();
		        outputStream = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
}
