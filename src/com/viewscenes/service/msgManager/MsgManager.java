/**
 * ���ڣ�2012-2-1
 * �����ߣ�wxb
 */
package com.viewscenes.service.msgManager;

import java.util.ArrayList;

import com.viewscenes.service.ISMSBean;

/**
 * @���ߣ� wxb
 * @ʱ�䣺2012-2-1
 * ���ŷ��͹����࣬���ڶ���èֻ֧�ִ��з��ͣ�����ظ����Ӷ���è�ᵼ�¶���è�쳣
 * ���� ��Ҫʹ�ö���è���Ͷ��ž���ͨ������������
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
	 * ��msgList�л�ȡһ�����ţ�����msgList��ɾ��
	 * ʱ�䣺2012-2-1
	 * ���ߣ�wxb
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
