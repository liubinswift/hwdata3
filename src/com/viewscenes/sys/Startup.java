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
	   * 系统启动前先判断是中心系统还是直属台系统。
	   * 
	   */
	  TableInfoCache s = new TableInfoCache();

	  //加载站点code信息
      Common com = new Common();
      com.getHeadCode();
	  /**
	   * 加载系统相关路径配置信息
	   */
	  SystemConfig.loadConfig();
	  //加载短信平台线程,启动状态查看SMSConfig中的startup值。
	  MsgSendThread ms = new MsgSendThread();
	  ms.start();
	  
  }
}
