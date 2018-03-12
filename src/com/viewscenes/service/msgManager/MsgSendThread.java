/**
 * 日期：2012-2-1
 * 创建者：wxb
 */
package com.viewscenes.service.msgManager;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.viewscenes.dao.XmlReader;
import com.viewscenes.dao.XmlReaderDeviceConfig;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.pub.GDSet;
import com.viewscenes.service.ISMSBean;
import com.viewscenes.util.LogTool;

/**
 * @作者： wxb
 * @时间：2012-2-1
 * 
 * 短信发送线程，发送短信能且只能通过该线程进行发送
 * 
 */
public class MsgSendThread extends Thread {
	public static String startup="0";
	public static String port="1";
	public static String baudrate="9600";
	public static int threadSleepTime=10;
	public static boolean start=false;
	/**
	 * 
	 */
	public MsgSendThread() {
		// TODO Auto-generated constructor stub
		try {
			URL configFile = XmlReaderDeviceConfig.class.getClassLoader().getResource("daoconfig.xml");	 
			SAXBuilder builder = new SAXBuilder(false);
		    Document doc = builder.build(configFile);	    
		    Element root = doc.getRootElement();
		    Element e = root.getChild("SMSConfig");
		    startup = e.getAttributeValue("startup");
		    port = e.getAttributeValue("startup");
		    baudrate = e.getAttributeValue("startup");
		    threadSleepTime = Integer.parseInt(e.getAttributeValue("threadSleepTime"));
		    if(startup.equalsIgnoreCase("1")){
		    	start =true;
		    	LogTool.debug("sms","短信平台启动>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		    }
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	public void run(){
		
		while(start){
			try{
				if(MsgManager.size() > 0){
					ISMSBean bean = MsgManager.getMsg();
					
					//发送短信
					this.sendMsg(bean);
					
				}else{
					sleep(threadSleepTime*1000);
					checkSmsSend();
				}
			}catch(Exception e){
				LogTool.fatal(e);
			}
				
		}
	}
	
	public void checkSmsSend(){
		StringBuffer sbf = new StringBuffer("select sms.*,usert.user_name from SMS_SENDSTATE_TAB sms,SEC_USER_TAB usert ");
		sbf.append(" where sms.userid=usert.user_id and state=0 order by id");
		try {
			GDSet gd = DbComponent.Query(sbf.toString());
			for(int i=0;i<gd.getRowCount();i++){
				String id = gd.getString(i, "id");
				String userid = gd.getString(i, "userid");
				String user_name = gd.getString(i, "user_name");
				String sms = gd.getString(i, "sms");
				String mobilephone = gd.getString(i, "mobilephone");
				ISMSBean bean = new ISMSBean(id, userid,user_name,sms,mobilephone);							
				MsgManager.addMsg(bean);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public int sendMsg(ISMSBean bean){
		int res = 0;
		
		int times = 0;
		
		String comment = bean.getSms();  //短信內容
		String id = bean.getId();
		if (comment.length() > 220) {
			LogTool.debug("sms", "需要发送的短息长度大于220>>>>>>>>>>>>>>>");
			if (comment.length() % 220 != 0) {
				times = comment.length() / 220 + 1;// 超过个数分割信息
			} else {
				times = comment.length() / 220;
			}
			LogTool.debug("sms", "该条短息拆分成" + times + "条发送>>>>>>>>>>>>>>>");
			if (SMSManager.checkSMSCatConnect()) {
				LogTool.debug("sms", "连接短信猫成功，准备发送>>>>>>>>>>>>>>>");
				
				for (int i = 0; i < times; i++) {
						String tel = bean.getMobilephone();
						String userName = bean.getUser_name();
						if (i == times - 1) {
							res = SMSManager.sendMsg(tel, comment.substring((i + 1) * 220,comment.length()));
							if (res == 1) {
								LogTool.debug("sms","发送成功一条短信>>>>>>>>>>>>>>>>>>>手机号码="+tel);
								updateData(id,"1",userName,tel);
								// bean.insertSendMsg(bean.getAlarm_id(),
								// bean.getUserName()[j], bean.getTel()[j],"2");
							} else {
								LogTool.debug("sms","发送失败一条短信>>>>>>>>>>>>>>>>>>>手机号码="+tel);
								updateData(id,"2",userName,tel);
							}
						} else {
							res = SMSManager.sendMsg(tel, comment.substring(0, (i + 1) * 220));
							if (res == 1) {
								LogTool.debug("sms","发送成功一条短信>>>>>>>>>>>>>>>>>>>手机号码="+tel);
								updateData(id,"1",userName,tel);
							} else {
								LogTool.debug("sms", "发送失败一条短信>>>>>>>>>>>>>>>>>>>手机号码="+tel);
								updateData(id,"2",userName,tel);
							}
						}
				}
			}
			SMSManager.closeSMSCatConnect();
		}else{
			LogTool.debug("sms","短信长度小于220，直接开始发送");
			if(SMSManager.checkSMSCatConnect()){
					String tel = bean.getMobilephone();
					String userName = bean.getUser_name();
					res = SMSManager.sendMsg(tel,comment);
					if(res == 1){
						LogTool.debug("sms","发送成功一条短信>>>>>>>>>>>>>>>>>>>手机号码="+tel);
						updateData( id,"1",userName,tel);
					}else{
						LogTool.debug("sms","发送失败一条短信>>>>>>>>>>>>>>>>>>>手机号码="+tel);
						updateData(id,"2",userName,tel);
					}
			}
			LogTool.debug("sms","本次短信发送完成，关闭短信猫连接>>>>>>>>>>>>>>>>>>");
			SMSManager.closeSMSCatConnect();
		}		
		
		return res;
	}
	public void updateData(String id,String state,String username,String tel){
		StringBuffer sbf = new StringBuffer("update SMS_SENDSTATE_TAB set state='");
		sbf.append(state);
		sbf.append("',send_time=");
		sbf.append("sysdate");
		sbf.append(" where id='");
		sbf.append(id);
		sbf.append("'");
		try {
			DbComponent.exeUpdate(sbf.toString());
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
