package com.viewscenes.web.dataquery.disturb;

import java.util.ArrayList;

import org.jmask.web.controller.EXEException;

import com.viewscenes.bean.runplan.DisRunplanBean;
import com.viewscenes.util.StringTool;

import flex.messaging.io.amf.ASObject;

/**
 * ͬ��Ƶ������Ϣ��ѯ
 * @author ���ֱߥ��Ҹ�
 *
 */
public class FreqDisturbInfo {
	private FreqDisturbInfoDao grldao = new FreqDisturbInfoDao();
	/**
	 * 
	 *  ��ѯrunplanbean����
	 * 
	 */
	public Object queryRunplan(DisRunplanBean bean){
		
		ASObject resObj;
		ArrayList result;
		try {
			 resObj = grldao.queryRunplan(bean);
			 ArrayList list =(ArrayList) resObj.get("resultList") ;//ȡ������Ľ����
			 String classpath = DisRunplanBean.class.getName();
			 result = StringTool.convertFlexToJavaList(list, classpath);//����������ת����bean���ٷŻؽ�����С�
			 resObj.put("resultList", result);
		} catch (Exception e) {
			e.printStackTrace();
			EXEException exe = new EXEException("", 
					e.getMessage(), "");
			return exe ;
		}
		
		return resObj;
	}
	/**
	 * 
	 * �޸�����
	 * @param bean
	 * @return
	 */
	public Object updateRunplan(DisRunplanBean bean){
		return grldao.updateRunplan(bean);
	}
	/**
	 * �������
	 * @param bean
	 * @return
	 */
	public Object insertRunPlan(DisRunplanBean bean){
		return grldao.addRunplan(bean);
	}
	public Object deleteDisturb(DisRunplanBean bean){
		return grldao.deleteDisturb(bean);
	}
}
