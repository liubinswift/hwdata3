package com.viewscenes.axis.asr;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import cn.com.pattek.pesb.client.PESBClient;

import com.viewscenes.bean.ASRCmdBean;
import com.viewscenes.bean.ASRResBean;
import com.viewscenes.bean.RadioMarkZstViewBean;
import com.viewscenes.bean.ResHeadendBean;
import com.viewscenes.bean.runplan.RunplanBean;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.pub.GDSetException;
import com.viewscenes.sys.SystemConfig;
import com.viewscenes.util.LogTool;
import com.viewscenes.util.StringTool;
import com.viewscenes.web.common.Common;


/**
 * 中科博时 语音识别接口调用
 * 
 * 接口要求：
 * jdk 1.6以上
 * 
 * 
 * @author thinkpad
 * 
 */
public class ASRClient{
	public ASRClient() {

	}
	


	

	/**
	 * 语音识别接口
	 */
	public   ASRResBean exucuteTask(ASRCmdBean asrCmdBean) {
		ASRResBean asrResBean = new ASRResBean();
		String result = "";
		

		String xml = getCmdXml(asrCmdBean);

		String taskId = "";
		PESBClient pesbClient = new PESBClient();
		try {

		    LogTool.info("asr","发送给语音识别系统XML:\n"+xml);
		    String taskXml = pesbClient.exucuteTask(xml);
		    if (taskXml.equals("-100")){
		    	LogTool.info("asr","无法连接到语音识别系统,请检查网络...\n"+xml);
		    	asrResBean.setTaskStatus("0000");
				asrResBean.setErrorMessage("无法连接到语音识别系统,请检查网络...");
		    	return asrResBean;
		    }
		    Element root = StringTool.getXMLRoot(taskXml);
			taskId = root.getChild("task").getAttributeValue("id");
			LogTool.info("asr","语音识别系统返回的处理任务ID="+taskId);
		} catch (Exception e) {
			e.printStackTrace(); 
			asrResBean.setTaskStatus("0000");
			asrResBean.setErrorMessage("无法连接到语音识别系统,请检查网络...");
			return asrResBean;
		}
		//result  =  pesbClient.queryResult(taskId);
	    LogTool.info("asr","语音识别系统处理结果XML:\n"+result);
		asrResBean = getResXml(result);
		return asrResBean;

		

//		return asrResBean;
	}

	/**
	 *  语音识别接口发送参数方法
	 * <p>class/function:com.viewscenes.axis
	 * <p>explain:
	 * <p>author-date:谭长伟 2012-7-16
	 * @param:
	 * @return:
	 */
	private   String getCmdXml(ASRCmdBean asrCmdBean) {

		String result = "";

		ArrayList list = new ArrayList();

		Element tasks = new Element("tasks");

		tasks.addAttribute(new Attribute("type", asrCmdBean.getType()));

		tasks.addAttribute(new Attribute("wfType", asrCmdBean.getWfType()));

		Element task = new Element("task");

		task.addAttribute(new Attribute("id", ""));

		String filepath = asrCmdBean.getFile();
		filepath = filepath.replace(SystemConfig.getVideoUrl(), SystemConfig.getVideo_location());
//		filepath = filepath.replace(SystemConfig.getLocVideoUrl(), SystemConfig.getVideo_location());
		Element e_file = new Element("file");
		e_file.addContent(filepath);

		Element e_fileStartTime = new Element("fileStartTime");
		e_fileStartTime.addContent(asrCmdBean.getFileStartTime());

		
		
		Element e_fileEndTime = new Element("fileEndTime");
		e_fileEndTime.addContent(asrCmdBean.getFileEndTime());

		Element e_taskStartTime = new Element("taskStartTime");
		e_taskStartTime.addContent(asrCmdBean.getTaskStartTime());

		Element e_taskEndTime = new Element("taskEndTime");
		e_taskEndTime.addContent(asrCmdBean.getTaskEndTime());

		Element e_freq = new Element("freq");
		e_freq.addContent(asrCmdBean.getFreq());
		
		Element e_language = new Element("language");
		e_language.addContent(asrCmdBean.getLanguage());
		
		Element e_collectMethod = new Element("collectMethod");
		e_collectMethod.addContent(asrCmdBean.getCollectMethod());

		Element e_collectChannel = new Element("collectChannel");
		e_collectChannel.addContent(asrCmdBean.getCollectChannel());

		task.addChild(e_file);

		task.addChild(e_fileStartTime);

		task.addChild(e_fileEndTime);

		task.addChild(e_taskStartTime);

		task.addChild(e_taskEndTime);

		task.addChild(e_freq);
		
		task.addChild(e_language);
		
		task.addChild(e_collectMethod);

		task.addChild(e_collectChannel);

		list.add(task);

		Document doc = new Document(tasks);

		Element rootNode = doc.getRootElement();

		rootNode.setChildren(list);

		XMLOutputter out = new XMLOutputter("  ", true, "GB2312");

		StringWriter sw = new StringWriter();

		try {

			out.output(doc, sw);

		} catch (IOException ex2) {

			ex2.printStackTrace();
		}
		result = sw.toString();

//		System.out.println(result);

		return result;
	}

	/**
	 * 语音识别返回XML解析方法
	 * <p>class/function:com.viewscenes.axis
	 * <p>explain:
	 * <p>author-date:谭长伟 2012-7-16
	 * @param:
	 * @return:
	 */
	private   ASRResBean getResXml(String xml){
//		System.out.println("返回结果:"+xml);
		ASRResBean asrResBean = new ASRResBean();
		Element root = StringTool.getXMLRoot(xml);
		String type = root.getChild("Type").getText();
		String orderId = root.getChild("OrderID").getText();
		asrResBean.setType(type);
		asrResBean.setOrderID(orderId);
		
		Element subEl = root.getChild("TimePeriod");
		
		String timePeriod_type = subEl.getAttribute("type").getValue();
		asrResBean.setTimePeriodType(timePeriod_type);
		
		String status = subEl.getChild("Status").getText();
		asrResBean.setStatus(status);
		String file = subEl.getChild("File").getText();
		asrResBean.setFile(file);
		String StartTime = subEl.getChild("StartTime").getText();
		asrResBean.setStartTime(StartTime);
		String EndTime = subEl.getChild("EndTime").getText();
		asrResBean.setEndTime(EndTime);
		String AudibilityScore = subEl.getChild("AudibilityScore").getText();
		asrResBean.setAudibilityScore(AudibilityScore);
		String AudibilityConfidence = subEl.getChild("AudibilityConfidence").getText();
		asrResBean.setAudibilityConfidence(AudibilityConfidence);
		String ChannelName = subEl.getChild("ChannelName").getText();
		asrResBean.setChannelName(ChannelName);
		String ChannelNameConfidence = subEl.getChild("ChannelNameConfidence").getText();
		asrResBean.setChannelNameConfidence(ChannelNameConfidence);
		String ProgramName = subEl.getChild("ProgramName").getText();
		asrResBean.setProgramName(ProgramName);
		String ProgramNameConfidence = subEl.getChild("ProgramNameConfidence").getText();
		asrResBean.setProgramNameConfidence(ProgramNameConfidence);
		String LanguageName1 = subEl.getChild("LanguageName1").getText();
		asrResBean.setLanguageName1(LanguageName1);
		String LanguageName2 = subEl.getChild("LanguageName2").getText();
		asrResBean.setLanguageName2(LanguageName2);
		String LanguageName3 = subEl.getChild("LanguageName3").getText();
		asrResBean.setLanguageName3(LanguageName3);
		String LanguageName4 = subEl.getChild("LanguageName4").getText();
		asrResBean.setLanguageName4(LanguageName4);
		String LanguageName5 = subEl.getChild("LanguageName5").getText();
		asrResBean.setLanguageName5(LanguageName5);
		String LanguageConfidence1 = subEl.getChild("LanguageConfidence1").getText();
		asrResBean.setLanguageConfidence1(LanguageConfidence1);
		String LanguageConfidence2 = subEl.getChild("LanguageConfidence2").getText();
		asrResBean.setLanguageConfidence2(LanguageConfidence2);
		String LanguageConfidence3 = subEl.getChild("LanguageConfidence3").getText();
		asrResBean.setLanguageConfidence3(LanguageConfidence3);
		String LanguageConfidence4 = subEl.getChild("LanguageConfidence4").getText();
		asrResBean.setLanguageConfidence4(LanguageConfidence4);
		String LanguageConfidence5 = subEl.getChild("LanguageConfidence5").getText();
		asrResBean.setLanguageConfidence5(LanguageConfidence5);
		
		return asrResBean;
		
	}
	
	
	/**
	 * 取得接收机类型
	 * <p>class/function:com.viewscenes.axis.asr
	 * <p>explain:
	 * <p>author-date:谭长伟 2012-11-5
	 * @param:
	 * @return:
	 */
	public  String getReceiver(ResHeadendBean headendBean){
		String receiverType = "";
		String[] receiverTypes = new String[]{"545","NI","713"};
		for(int i = 0 ;i < receiverTypes.length; i++ ){
			if (headendBean.getManufacturer().toUpperCase().indexOf(receiverTypes[i]) != -1){
				receiverType = receiverTypes[i];
				break;
			}
		}
		if (receiverType.equals(""))
			receiverType = receiverTypes[0];
		return receiverType;
	}
	
	
	
}
