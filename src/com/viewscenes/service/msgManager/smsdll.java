package com.viewscenes.service.msgManager;


public class smsdll {
	
	static { 
//		System.setProperty("java.library.path", "E://dll");
		System.loadLibrary("smsdll"); 
	} 
	
	/**
	 * 
	 * @param Com_Port  ���ںţ�0Ϊ����ӿڣ�1,2,3,...Ϊ���ڣ�
	 * @param Com_BaudRate  ������
	 * @param Mobile_Type   �����ն��ͺ�
	 * @return  �����ն�ʧ�ܣ������ն˳ɹ�
	 */
	public native static String SmsConnection(int Com_Port,int Com_BaudRate); 
	
	
	/**
	 * 
	 * @param Sms_TelNum   ���͸����ն˺���
	 * @param Sms_Text   ���͵Ķ�������
	 * @return   0�� ���Ͷ���ʧ�ܣ�1�����Ͷ��ųɹ�
	 */
	public native static int SmsSend(String Sms_TelNum,String Sms_Text); 
	
	
	/**
	 * 
	 * @param Sms_Type  ��������(0��δ�����ţ�1���Ѷ����ţ�2���������ţ�3���ѷ����ţ�4��ȫ������)
	 * @param Sms_Text  ��java����ʱ����Ҫ����C++����ȡ��
	 * @return   ���������֮ǰ��"|"������Ϊ�ָ���,ÿ�������м�ĸ��ֶ���"#"������Ϊ�ָ���
	 */
	public native static String SmsReceive(String Sms_Type,String Sms_Text); 
	
	/**
	 * 
	 * @param Sms_Index   ���ŵ�������
	 * @return
	 */
	public native static int SmsDelete(String Sms_Index); 
	
	/**
	 * ������ӵ��ն��Ƿ�֧���Զ��շ����Ź���
	 * @return  0����֧�֣�1��֧��
	 */
	public native static int SmsAutoFlag();
	
	/**
	 * ��ѯ�Ƿ��յ��µĶ���Ϣ
	 * @return  0��δ�յ���1���յ�
	 */
	public native static int SmsNewFlag();
	
	/**
	 * �Ͽ��ն��봮�ڵ�����
	 * @return 0:�ɹ�  1��ʧ��
	 */
	public native static int SmsDisconnection();
	
	public static String smsdllconnection(int Port,int BaudRate)
	{
		return SmsConnection(Port,BaudRate);
//		if(SmsConnection(Port,BaudRate)==1)
//			return "����è���ӳɹ�";
////		else if(SmsConnection(Port,BaudRate)==0)
////			return "����è����ʧ�ܻ���Ƿ�ѣ���鿴�Ƿ�Ƿ��";
//		return "����è����ʧ�ܻ���Ƿ�ѣ���鿴�Ƿ�Ƿ��";
	}
	
	public static void main(String[] args) 
	{ 
		smsdll test = new smsdll();
		//System.out.println("�����й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й���1".length());
		
//		int connectresult = SmsConnection(3, 9600);
		System.out.println(SmsConnection(5, 9600)+"asdf");
		String tel[] = new String[3];
		tel[0]="13810610834";
		//tel[1]="18701360755";
		//tel[0]="15810594746";
		for(int i=0;i<1;i++)
		{
			System.out.println(SmsSend(tel[i], "�����й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й��������й���1"));
		}
		System.out.println(SmsReceive("4",""));
		System.out.println(SmsAutoFlag());
		System.out.println(SmsNewFlag());
		System.out.println(SmsDisconnection());
//		System.out.println(SmsDelete("1"));
	} 
	
}
