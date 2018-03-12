package com.viewscenes.dao;



import java.net.*;
import java.util.*;

import org.jdom.*;
import org.jdom.input.*;



public class XmlReaderDeviceConfig {



  private static Element root;

  static {

    try {

      URL configFile = XmlReaderDeviceConfig.class.getClassLoader().getResource(

          "deviceconfig.xml");



      //assert configFile != null:"XmlReader�������ļ�daoconfig.xml����";

      SAXBuilder builder = new SAXBuilder(false);

      Document doc = builder.build(configFile);



      //assert doc != null:"XmlReader�������ļ�daoconfig.xml����";

      root = doc.getRootElement();


    }

    catch (JDOMException ex) {

      ex.printStackTrace();

    }



  }



  /**

   * �ⲿ������øú�������Լ���������

   * @param itemName �Լ�������Ԫ�ص�����

   * @return ������Ԫ�أ���Ϊnull

   */

  public static Element getConfigItem(String itemName) {

    //assert itemName != null:"XmlReader getConfigItem:getConfigItem�Ĳ�����null";



    Element e = root.getChild(itemName);

    //assert e != null:"���ڵ�Ϊ��";

    return e;

  }



  /**

   * �õ�ָ���ӽڵ����һ���ӽڵ�����ֵ�ĸ�Ԫ��

   * @param itemName ��Ԫ�ص����ƣ�������root��ֱ����һ��

   * @param atrrName ��������

   * @param atrrValue ����ֵ

   * @return null,û�и�Ԫ��

   */

  public static Element getItem(String itemName, String atrrName, String atrrValue) {

    //assert (itemName != null && atrrName != null && atrrValue != null)

    //:"XmlReader getItem:getConfigItem�Ĳ�����null";



    atrrValue = atrrValue.toLowerCase();

    Element ele = getConfigItem(itemName);

    List conlist = ele.getChildren();

    for (int i = 0; i < conlist.size(); i++) {

      Element sub = (Element) conlist.get(i);

      String tem = sub.getAttributeValue(atrrName);

      if (sub.getAttributeValue(atrrName).equals(atrrValue)) {

        return sub;

      }

    }

    return null;

  }



  /**

   * �õ�ָ���ӽڵ����һ��ָ���ڵ��ָ�����Ե�����ֵ

   * ��õ�root��һ�����ӽڵ�ΪitemName,���ӽڵ����һ��ΪΪchildName,������ΪatrrName������

   * @param itemName ��Ԫ�ص����ƣ�������root��ֱ����һ��

   * @param childName �ӽڵ�����

   * @param atrrName  ��������

   * @return

   */

  public static String getAttrValue(String itemName, String childName, String atrrName) {

    //assert (itemName != null && childName != null && atrrName != null)

    //:"XmlReader getItem:getAttrValue�Ĳ�����null";



    atrrName = atrrName.toLowerCase();

    Element ele = XmlReaderDeviceConfig.getConfigItem(itemName);

    Element sub = ele.getChild(childName);

    String para = sub.getAttributeValue(atrrName);

    //assert para != null:"XmlReader getAttrValue:�Ҳ�������:"+atrrName;

    return para;

  }

  /**

   * �õ�ָ���ӽڵ����һ��ָ���ڵ��ָ�����Ե�����ֵ

   * ��õ�root��һ�����ӽڵ�ΪitemName,���ӽڵ����һ��ΪΪchildName,������ΪatrrName������

   * @param itemName ��Ԫ�ص����ƣ�������root��ֱ����һ��

   * @param childName �ӽڵ�����

   * @param atrrName  ��������

   * @return

   */

  public static List getAttrValueList(String itemName, String childName,String atrrName,String atrrName2) {

    //assert (itemName != null && childName != null && atrrName != null)

    //:"XmlReader getItem:getAttrValue�Ĳ�����null";

    Element e = root.getChild(itemName);
    List list = e.getChildren(childName);
    System.out.println(list.size());
    List iplist=new ArrayList();
    for(int i=0;i<list.size();i++)
    {
          List li=new ArrayList();
          Element el=(Element)list.get(i) ;
          String value=el.getAttributeValue(atrrName);
          String info=el.getAttributeValue(atrrName2);
          li.add(value);
           li.add(info);
          iplist.add(li);
    }

    return iplist;

  }

  public static List getAttrValueList(String itemName, String childName,String atrrName,String atrrName2,String atrrName3,String atrrName4) {

    //assert (itemName != null && childName != null && atrrName != null)

    //:"XmlReader getItem:getAttrValue�Ĳ�����null";

    Element e = root.getChild(itemName);
    List list = e.getChildren(childName);
    System.out.println(list.size());
    List iplist=new ArrayList();
    for(int i=0;i<list.size();i++)
    {
          List li=new ArrayList();
          Element el=(Element)list.get(i) ;
          String value=el.getAttributeValue(atrrName);
          String info=el.getAttributeValue(atrrName2);
          String info2=el.getAttributeValue(atrrName3);
          String info3=el.getAttributeValue(atrrName4);
          li.add(value);
          li.add(info);
          li.add(info2);
          li.add(info3);
          iplist.add(li);
    }

    return iplist;

  }

  /**

   * �õ�ָ������ֵ

   * @param itemName ��Ԫ�ص����ƣ�������root��ֱ����һ��

   * @param atrrName ��������

   * @param atrrValue ����ֵ

   * @param nextAtrName ��ѯ����������

   * @return

   */

  public static String getNextAttrValue(String itemName, String atrrName, String atrrValue,

                                        String nextAtrName) {

    //assert (itemName != null && atrrName != null && atrrValue != null && nextAtrName != null)

    //:"XmlReader getItem:getNextAttrValue�Ĳ�����null";



    atrrValue = atrrValue.toLowerCase();

    Element ele = XmlReaderDeviceConfig.getItem(itemName, atrrName, atrrValue);

    //assert ele != null:"XmlReader getNextAttrValue:�ڵ�"+itemName + "������" + atrrName + "�Ҳ���" +

        //atrrValue;



    String indexs = ele.getAttributeValue(nextAtrName);

    //assert indexs != null:"XmlReader getNextAttrValue:�Ҳ����ڵ�"+itemName + "������" + nextAtrName;

    return indexs;

  }

}
