package com.viewscenes.dao.logic;

import java.util.*;

import org.jdom.*;
import com.viewscenes.dao.innerdao.DAOComponent;
import com.viewscenes.dao.XmlReader;


public class LogicFactory {

  public static DAOComponent create(String tab) {

    if (operContains("task", tab)) {

      return new Task();
    }

    if (operContains("role", tab)) {

      return new Role();
    }

    if (operContains("roleuser", tab)) {

      return new UserRole();
    }

    if (operContains("channel", tab)) {

      return new Channel();
    }

    return new DAOComponent();

  }

  /**
   * �жϱ��Ƿ�����caption��Ĳ���

   * @param caption �������͵�����

   * @param tabName ����

   * @return

   * true:���ڴ˲���

   * false:�����ڴ˲���

   */

  private static boolean operContains(String caption, String tabName) {

    String lowerTab = tabName.toLowerCase();

    Element ele = XmlReader.getConfigItem("updateconfig");

    Element task_ele = ele.getChild(caption);

    List total_tab = task_ele.getChildren();

    for (int i = 0; i < total_tab.size(); i++) {

      Element tab = (Element) total_tab.get(i);

      String tname = tab.getAttributeValue("table");

      if (tname != null && tname.equals(lowerTab)) {

	return true;

      }

    }

    return false;

  }

}
