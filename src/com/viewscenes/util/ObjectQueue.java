package com.viewscenes.util;

import java.util.*;
import java.io.*;
import com.viewscenes.sys.SystemCache;

/**
 *
 * <p>Title: �������</p>
 * <p>Description: ��Ŷ���Ķ���</p>
 * <p> </p>
 * @�¸�
 * @version 1.0
 */
public class ObjectQueue {

  final static int DEFAULT_MAX_COUNT = 8000;
  final static int DEFAULT_ALARM_COUNT = 1500;
  //���е����ݽṹ
  ArrayList objList;
  //���е���������������������������Ϣ
  int maxCount = DEFAULT_MAX_COUNT;
  //���еı������ޣ������������������־�м�¼����
  int alarmCount = DEFAULT_ALARM_COUNT;
  //���е����ƣ���������־���������ĸ������������Ϣ
  String queueName = "��Ϣ";
  public ObjectQueue() {
    objList = new ArrayList(maxCount);
  }

  public ObjectQueue(String name, int maxCount, int alarmCount){
    this.queueName = name;
    this.maxCount = maxCount;
    this.alarmCount = alarmCount;
    objList = new ArrayList(maxCount);
  }

  /**
   * ����Ϣ�������һ����Ϣ���������е���Ϣ�����Ѿ������˾������޾���ʾ�澯��Ϣ
   * @param msg
   */
  public synchronized void add(Object obj){
    if (objList.size()<maxCount){
      objList.add(obj);
      //writeObjectQueue();//д���л��ļ�
      notify();
//      if (objList.size() > alarmCount) {
//        LogTool.warning("���棺"+queueName+"�����е�δ������Ϣ�����Ѿ��ﵽ��" + objList.size() + "����");
//      }
    }
    else{
      LogTool.fatal("���棺"+queueName+"�����е�δ������Ϣ�����ﵽ��" + objList.size() + "�����Ѿ�������ϵͳ���������������Ϣ��������");
    }
  }

  /**
   * �Ӷ�����ȡ��һ�����󣬶���ȡ����ö��󼴴Ӷ�����ɾ��
   * @return
   */
  public synchronized Object get(){
    Object obj = null;
    if (objList.size()>0){
      obj = objList.get(0);
      objList.remove(0);
      //writeObjectQueue();//д���л��ļ�

      if (objList.size() % 5 ==0)
        LogTool.output(queueName+"�����л�ʣ��"+objList.size()+"��δ������Ϣ��");
    }
    else{
      try {
        wait();
      }
      catch (InterruptedException ex) {
      }
    }
    return obj;
  }

  public void removeAll(){
    objList.removeAll(objList);
  }
  /**
   * ��õ�ǰ�����ж���ĸ���
   * @return ����ĸ���
   *
   */
  public int getQuerySize(){
    return objList.size();
  }

  /**
   * ��ǰ���������л���Ӳ����
   *
   */
  public synchronized void writeObjectQueue(){
    ObjectOutputStream out = null;
    try{
      out = new ObjectOutputStream(new FileOutputStream(
          SystemCache.objectqueuepath));
      out.writeObject(objList);
    }catch(Exception ex){
      LogTool.fatal("���л����г���", ex);
    }finally{
      try{
        out.close();
      }catch(Exception ex){
        LogTool.fatal("�ر����л�������", ex);
      }
    }
  }

  /**
  * ��ǰ���������л���Ӳ����
  *
  */
 public synchronized void readObjectQueue(){
   ObjectInputStream in = null;
   try {
     File file= new File(SystemCache.objectqueuepath);
     if (file.exists()){
       in = new ObjectInputStream(new FileInputStream(
           SystemCache.objectqueuepath));
       objList = (ArrayList) in.readObject();
     }
   }
   catch (Exception ex) {
//     LogTool.fatal("read���л����г���", ex);
   }
   finally {
     try {
       in.close();
     }
     catch (Exception ex) {
       LogTool.fatal("�ر�read���л�������", ex);
     }
   }
 }



}
