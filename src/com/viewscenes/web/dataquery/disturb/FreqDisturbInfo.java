package com.viewscenes.web.dataquery.disturb;

import java.util.ArrayList;

import org.jmask.web.controller.EXEException;

import com.viewscenes.bean.runplan.DisRunplanBean;
import com.viewscenes.util.StringTool;

import flex.messaging.io.amf.ASObject;

/**
 * 同邻频干扰信息查询
 * @author 左手边ジ幸福
 *
 */
public class FreqDisturbInfo {
	private FreqDisturbInfoDao grldao = new FreqDisturbInfoDao();
	/**
	 * 
	 *  查询runplanbean对象
	 * 
	 */
	public Object queryRunplan(DisRunplanBean bean){
		
		ASObject resObj;
		ArrayList result;
		try {
			 resObj = grldao.queryRunplan(bean);
			 ArrayList list =(ArrayList) resObj.get("resultList") ;//取出对象的结果集
			 String classpath = DisRunplanBean.class.getName();
			 result = StringTool.convertFlexToJavaList(list, classpath);//将对象类型转换成bean，再放回结果集中。
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
	 * 修改数据
	 * @param bean
	 * @return
	 */
	public Object updateRunplan(DisRunplanBean bean){
		return grldao.updateRunplan(bean);
	}
	/**
	 * 添加数据
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
