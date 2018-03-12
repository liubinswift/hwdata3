package com.viewscenes.service.msgManager;


public class smsdll {
	
	static { 
//		System.setProperty("java.library.path", "E://dll");
		System.loadLibrary("smsdll"); 
	} 
	
	/**
	 * 
	 * @param Com_Port  串口号（0为红外接口，1,2,3,...为串口）
	 * @param Com_BaudRate  波特率
	 * @param Mobile_Type   返回终端型号
	 * @return  连接终端失败；连接终端成功
	 */
	public native static String SmsConnection(int Com_Port,int Com_BaudRate); 
	
	
	/**
	 * 
	 * @param Sms_TelNum   发送给的终端号码
	 * @param Sms_Text   发送的短信内容
	 * @return   0： 发送短信失败；1：发送短信成功
	 */
	public native static int SmsSend(String Sms_TelNum,String Sms_Text); 
	
	
	/**
	 * 
	 * @param Sms_Type  短信类型(0：未读短信；1：已读短信；2：待发短信；3：已发短信；4：全部短信)
	 * @param Sms_Text  在java中暂时不需要，由C++调用取得
	 * @return   短信与短信之前用"|"符号作为分隔符,每条短信中间的各字段用"#"符号作为分隔符
	 */
	public native static String SmsReceive(String Sms_Type,String Sms_Text); 
	
	/**
	 * 
	 * @param Sms_Index   短信的索引号
	 * @return
	 */
	public native static int SmsDelete(String Sms_Index); 
	
	/**
	 * 检测连接的终端是否支持自动收发短信功能
	 * @return  0：不支持；1：支持
	 */
	public native static int SmsAutoFlag();
	
	/**
	 * 查询是否收到新的短信息
	 * @return  0：未收到；1：收到
	 */
	public native static int SmsNewFlag();
	
	/**
	 * 断开终端与串口的连接
	 * @return 0:成功  1：失败
	 */
	public native static int SmsDisconnection();
	
	public static String smsdllconnection(int Port,int BaudRate)
	{
		return SmsConnection(Port,BaudRate);
//		if(SmsConnection(Port,BaudRate)==1)
//			return "短信猫连接成功";
////		else if(SmsConnection(Port,BaudRate)==0)
////			return "短信猫连接失败或者欠费，请查看是否欠费";
//		return "短信猫连接失败或者欠费，请查看是否欠费";
	}
	
	public static void main(String[] args) 
	{ 
		smsdll test = new smsdll();
		//System.out.println("我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人1".length());
		
//		int connectresult = SmsConnection(3, 9600);
		System.out.println(SmsConnection(5, 9600)+"asdf");
		String tel[] = new String[3];
		tel[0]="13810610834";
		//tel[1]="18701360755";
		//tel[0]="15810594746";
		for(int i=0;i<1;i++)
		{
			System.out.println(SmsSend(tel[i], "我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人1"));
		}
		System.out.println(SmsReceive("4",""));
		System.out.println(SmsAutoFlag());
		System.out.println(SmsNewFlag());
		System.out.println(SmsDisconnection());
//		System.out.println(SmsDelete("1"));
	} 
	
}
