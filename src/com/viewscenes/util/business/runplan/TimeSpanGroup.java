package com.viewscenes.util.business.runplan;

import java.util.*;
/**
 * <p>Title: ����ͼʱ�������</p>
 * <p>Description: ������ͼʱ������Լ�����ͼ��Ϣ�ķ�װ</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: ����ͬ��</p>
 * @author ������
 * @version 1.0
 */

public class TimeSpanGroup {
  private Vector spanVector;
  private RunPlanInfo runPlanInfo;

  /**
   * ���캯��
   */
  public TimeSpanGroup() {
    spanVector = new Vector();
    runPlanInfo = null;
  }

  /**
   * ת��Ϊ�ִ�
   * @return
   */
  public String toString() {
    String returnValue = "";
    for (int i = 0; i < spanVector.size(); i++) {
      returnValue += i + ". " + this.get(i).toString();
    }
    return returnValue;
  }

  /**
   * �������
   */
  public void removeAll() {
    spanVector.clear();
  }

  /**
   * ����ں���ʱ���
   * @param runSpan ʱ���TimeSpan
   */
  public void add(TimeSpan runSpan) {
    add(runSpan, false);
  }

  /**
   * ȥ��ָ��ʱ���
   * @param index
   */
  public void remove(int index) {
    if (index < spanVector.size()) {
      spanVector.remove(index);
    }
  }

  /**
   * ����ʱ��,�Կ���ʱ���Ȳ�ֺ����
   * @param newItem ʱ���TimeSpan
   * @param overWrite ���Ǳ�־ true:����,false:�ں�
   */
  public void add(TimeSpan newItem, boolean overWrite) {
    TimeSpan addedItem;
    if (newItem.getProgramInfo() != null) {
      addedItem = new TimeSpan(newItem.getStartTime(), newItem.getEndTime(),
			       newItem.getType(),
			       (ProgramInfo) newItem.getProgramInfo().clone());
    }
    else {
      addedItem = new TimeSpan(newItem.getStartTime(), newItem.getEndTime(),
			       newItem.getType(), newItem.getProgramInfo());
      //split two parts
    }
    if (addedItem.getStartTime() >= addedItem.getEndTime()) {
      TimeSpan item = new TimeSpan(0, addedItem.getEndTime(), addedItem.getType(),
				   addedItem.getProgramInfo());
      addItem(item, overWrite);
      addedItem.setEndTime(24 * 3600);
      addItem(addedItem, overWrite);
    }
    else {
      addItem(addedItem, overWrite);

      //
    }
  }

  /**
   * ����һ��ʱ���
   * @param newItem ʱ���TimeSpan
   * @param overWrite ���Ǳ�־ true:����,false:�ں�
   */
  private void addItem(TimeSpan newItem, boolean overWrite) {
    TimeSpan item;
    TimeSpan sitem;
    int i;
    for (i = 0; i < spanVector.size(); i++) {
      item = this.get(i);
      //compare to the next item;
      if (newItem.getStartTime() >= item.getEndTime()) {
	continue;
      }
      //insert new item in the early most
      if (newItem.getEndTime() < item.getStartTime()) {
	spanVector.insertElementAt(newItem, i);
	break;
      }
      //split new item, continue using left parts
      if (newItem.getStartTime() < item.getStartTime()) {
	sitem = new TimeSpan(newItem.getStartTime(), item.getStartTime(),
			     newItem.getType(), newItem.getProgramInfo());
	spanVector.insertElementAt(sitem, i);
	newItem.setStartTime(item.getStartTime());
	continue;
      }
      //split exist item
      if (newItem.getStartTime() > item.getStartTime()) {
	sitem = new TimeSpan(item.getStartTime(), newItem.getStartTime(),
			     item.getType(), item.getProgramInfo());
	spanVector.insertElementAt(sitem, i);
	item.setStartTime(newItem.getStartTime());
	continue;
      }

      //split exist item
      if (newItem.getEndTime() < item.getEndTime()) {
	//create left item
	sitem = new TimeSpan(newItem.getEndTime(), item.getEndTime(),
			     item.getType(), item.getProgramInfo());
	spanVector.insertElementAt(sitem, i + 1);
	//shorten exist item end
	item.setEndTime(newItem.getEndTime());
      }
      //common part combination
      if (newItem.getType() > item.getType() || overWrite) {
	item.setType(newItem.getType());
	item.setProgramInfo(newItem.getProgramInfo());
      }

      //split new item, continue using left parts
      if (newItem.getEndTime() > item.getEndTime()) {
	newItem.setStartTime(item.getEndTime());
	continue;
      }
      else {
	//no block left
	break;
      }
    }
    //add to the last
    if (i >= spanVector.size()) {
      spanVector.add(newItem);
    }
    compressItems();
  }

  /**
   * ѹ��ǰ�����ӵ�ʱ���
   */
  private void compressItems() {
    TimeSpan item;
    TimeSpan itemNext;
    int i = 0;
    while (i < spanVector.size() - 1) {
      item = this.get(i);
      itemNext = this.get(i + 1);
      if (
	  item.getEndTime() == itemNext.getStartTime() &&
	  item.getType() == itemNext.getType()
	  &&
	  (
	  (
	  item.getProgramInfo() != null && itemNext.getProgramInfo() != null
	  &&
	  (item.getProgramInfo() == itemNext.getProgramInfo() ||
	   item.getProgramInfo().equals(itemNext.getProgramInfo()))
	  )
	  ||
	  (item.getProgramInfo() == null && itemNext.getProgramInfo() == null)
	  )
	  ) {
	item.setEndTime(itemNext.getEndTime());
	spanVector.remove(i + 1);
      }
      else {
	i++;
      }
    }
  }

  /**
   * ���һ��ʱ���
   * @param index ����
   * @return  ʱ���TimeSpan
   */
  public TimeSpan get(int index) {
    TimeSpan returnValue;
    if (index < spanVector.size()) {
      returnValue = (TimeSpan) spanVector.get(index);
    }
    else {
      returnValue = new TimeSpan();
    }
    return returnValue;
  }

  /**
   * ��ȡʱ��θ���
   * @param type ����ͼ����, 0:ȫ��,1:����,2:����
   * @return
   */
  public int getTimeSpanNum(long type) {
    int returnValue = 0;
    if (type == 0) {
      returnValue = spanVector.size();
    }
    else {
      for (int i = 0; i < spanVector.size(); i++) {
	if (this.get(i).getType() == type) {
	  returnValue++;
	}
      }
    }
    return returnValue;
  }

  /**
   * ��ȡ��ʱ��
   * @param type ����ͼ����, 0:ȫ��, 1:����, 2:����
   * @return (��)
   */
  public long getTotalInterval(long type) {
    long returnValue = 0;
    TimeSpan item;
    for (int i = 0; i < spanVector.size(); i++) {
      if (type == 0 || this.get(i).getType() == type) {
	returnValue += this.get(i).getInterval();
      }
    }
    return returnValue;
  }

  /**
   * ��ȡ����ͼ��Ŀ��Ϣ
   * @return ProgramInfo
   */
  public RunPlanInfo getRunPlanInfo() {
    return runPlanInfo;
  }

  /**
   * ��������ͼ��Ŀ��Ϣ
   * @param programInfo ProgramInfo
   */
  public void setRunPlanInfo(RunPlanInfo runPlanInfo) {
    this.runPlanInfo = runPlanInfo;
  }

}
