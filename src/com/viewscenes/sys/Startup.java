package com.viewscenes.sys;



//import com.novel_tongfang.mon.framework.task.TaskManager;
import com.viewscenes.service.msgManager.MsgSendThread;
import com.viewscenes.sys.TableInfoCache;
import com.viewscenes.sys.SystemTableCache;
import com.viewscenes.web.common.Common;
public class Startup {
  public static void main(String[] args) {
    initService();
  }

  public static void initService(){
	  /**
	   * ϵͳ����ǰ���ж�������ϵͳ����ֱ��̨ϵͳ��
	   * 
	   */
	  TableInfoCache s = new TableInfoCache();

	  //����վ��code��Ϣ
      Common com = new Common();
      com.getHeadCode();
	  /**
	   * ����ϵͳ���·��������Ϣ
	   */
	  SystemConfig.loadConfig();
	  //���ض���ƽ̨�߳�,����״̬�鿴SMSConfig�е�startupֵ��
	  MsgSendThread ms = new MsgSendThread();
	  ms.start();
	  
  }
}
