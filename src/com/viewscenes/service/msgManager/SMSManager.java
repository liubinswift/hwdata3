/**
 * 日期：2012-2-1
 * 创建者：wxb
 */
package com.viewscenes.service.msgManager;

/**
 * @作者： wxb
 * @时间：2012-2-1
 * 短信猫管理类
 * 
 */
public class SMSManager {

	/**
	 * 
	 */
	public SMSManager() {
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * 发送短信0： 发送短信失败；1：发送短信成功
	 * @param telephone
	 * @param tel_text
	 * @return
	 */
	public static synchronized int sendMsg(String telephone,String tel_text){
		int sendStatus = 0;			
		sendStatus = com.viewscenes.service.msgManager.smsdll.SmsSend(telephone,tel_text);				
		return sendStatus;
	}
	/**
	 * 检查短信猫是否连接成功
	 * @return
	 */
	public static boolean checkSMSCatConnect(){
		String connectresult = smsdll.smsdllconnection(Integer.parseInt(MsgSendThread.port), Integer.parseInt(MsgSendThread.baudrate));  //先与短信猫建立连接
		if(connectresult.indexOf("失败")<0){	
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 断开短信猫的连接
	 */
	public static  void closeSMSCatConnect(){
		smsdll.SmsDisconnection();  //断开短信猫连接
	}

}
