/**
 * 日期：2012-2-1
 * 创建者：wxb
 */
package com.viewscenes.service.msgManager;

import java.util.ArrayList;

import com.viewscenes.service.ISMSBean;

/**
 * @作者： wxb
 * @时间：2012-2-1
 * 短信发送管理类，由于短信猫只支持串行发送，多次重复连接短信猫会导致短信猫异常
 * 所以 需要使用短信猫发送短信均需通过该类来发送
 */
public class MsgManager {
	
	public  static ArrayList msgList = new ArrayList();

	/**
	 * 
	 */
	public MsgManager() {
		// TODO Auto-generated constructor stub
	}
	
	public synchronized static void addMsg(ISMSBean bean){
		msgList.add(bean);
	}
	
	/**
	 * 从msgList中获取一条短信，并从msgList中删除
	 * 时间：2012-2-1
	 * 作者：wxb
	 * @return
	 */
	public synchronized static ISMSBean getMsg(){
		ISMSBean bean = null;
		
		if(msgList.size()>0){
			bean = (ISMSBean)msgList.get(0);
			msgList.remove(0);
		}
		return bean;
	}
	
	public static int size(){
		return msgList.size();
	}

}
