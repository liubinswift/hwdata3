/**
 * ���ڣ�2012-2-1
 * �����ߣ�wxb
 */
package com.viewscenes.service.msgManager;

/**
 * @���ߣ� wxb
 * @ʱ�䣺2012-2-1
 * ����è������
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
	 * ���Ͷ���0�� ���Ͷ���ʧ�ܣ�1�����Ͷ��ųɹ�
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
	 * ������è�Ƿ����ӳɹ�
	 * @return
	 */
	public static boolean checkSMSCatConnect(){
		String connectresult = smsdll.smsdllconnection(Integer.parseInt(MsgSendThread.port), Integer.parseInt(MsgSendThread.baudrate));  //�������è��������
		if(connectresult.indexOf("ʧ��")<0){	
			return true;
		}else{
			return false;
		}
	}
	/**
	 * �Ͽ�����è������
	 */
	public static  void closeSMSCatConnect(){
		smsdll.SmsDisconnection();  //�Ͽ�����è����
	}

}
