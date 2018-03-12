package com.viewscenes.sys;

import java.util.*;
import javax.servlet.http.*;

import org.jdom.*;

import com.viewscenes.util.FileTool;
import com.viewscenes.util.LogTool;
import com.viewscenes.util.XMLConfigFile;


public class SystemSynchronizer
    {

    public static HashMap serverMap = new HashMap();
    static List syncUrlList = null;
    static String localMsgCode = null;
    static String localIP = null;
    static String localName = null;
    static HashMap headUserInfoMap = new HashMap();
    static boolean bCheckHeadUser = false;
    static long checkHeadUserInterval = 5*60*1000;

    static long userValidTime = 60*60*100;

    public SystemSynchronizer() {
    }


    public static synchronized void initServerUrlList() {
        if (syncUrlList == null) {
            try {
                String configFilePath = XMLConfigFile.getConfigFilePath("appserver.xml");
                Element root = FileTool.loadXMLFile(configFilePath);
                Element local = root.getChild("localmsgcode");
                localMsgCode = local.getText();

                local = root.getChild("localip");
                if (local!=null)
                  localIP = local.getText();

                local = root.getChild("localname");
                if (local!=null)
                  localName = local.getText();

                local = root.getChild("onlineuser");
                if (local!=null){
                  String validtime = (String)local.getAttributeValue("validtime");

                  try {
                    userValidTime = Long.parseLong(validtime)*1000;
                  }
                  catch (NumberFormatException ex1) {
                    userValidTime = 60*60*1000;
                  }
                }

                local = root.getChild("checkheaduser");
                if (local!=null){
                  String enable = (String)local.getAttributeValue("enable");
                  String interval = (String)local.getAttributeValue("interval");

                    if ((enable!=null)&&(!enable.equals("0"))){
                      bCheckHeadUser = true;
                    }
                    else{
                      bCheckHeadUser = false;
                    }

                  try {
                    checkHeadUserInterval = Long.parseLong(interval)*1000;
                  }
                  catch (NumberFormatException ex1) {
                    checkHeadUserInterval = 5*60*1000;
                  }
                }

                List l = root.getChildren("server");
                syncUrlList = new LinkedList();
                serverMap = new HashMap();
                if (l != null) {
                    for (int i = 0; i < l.size(); i++) {
                        Element sub = (Element) l.get(i);
                        syncUrlList.add(sub.getAttributeValue("syncurl"));
                        serverMap.put(sub.getAttributeValue("code"), sub.getAttributeValue("url"));
                    }
                }
            }
            catch (Exception ex) {
                LogTool.warning(ex);
            }
        }
    }

    public static boolean getCheckHeadUser() {
        if (syncUrlList == null) {
            initServerUrlList();
        }
        return bCheckHeadUser;
    }

    public static long getCheckHeadUserInterval() {
        if (syncUrlList == null) {
            initServerUrlList();
        }
        return checkHeadUserInterval;
    }

    public static List getServerList() {
        if (syncUrlList == null) {
            initServerUrlList();
        }
        return syncUrlList;
    }

    public static String getLocalMsgCode() {
        if (localMsgCode == null) {
            initServerUrlList();
        }
        return localMsgCode;
    }

    public static String getUrl(String msgcode) {
        return (String) serverMap.get(msgcode);
    }

}
