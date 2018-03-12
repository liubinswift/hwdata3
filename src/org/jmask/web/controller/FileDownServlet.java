package org.jmask.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jdom.Element;
import org.jmask.web.util.ClassRegister;
import org.jmask.web.util.SessionHelper;

import com.viewscenes.util.LogTool;
import com.viewscenes.util.StringTool;


/**
 *
 * <p>Title: �ļ��������</p>
 *
 * <p>Description: ���������û��������е�ִ�з�����ִ�з�����ã���������ػ��������������EXCEL�Ĳ���</p>
 *
 * <p>����Ļ���Ϊjavax.servlet.http.HttpServlet��</p>
 *
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: Viewscenes</p>
 *
 * @author not attributable
 * @version 1.0
 */

public class FileDownServlet extends HttpServlet {

	static final private String CONTENT_TYPE = "text/xml; charset=GB2312";

	static final private String REQUEST_TYPE = "UTF-8";

        /**
         * �޲����Ĺ��췽�������û���Ĺ��캯��
         */
        public FileDownServlet() {

		super();
	}


        /**
         * ���෽�����ع������û���destroy()������ִ������������ص��������
         */
        public void destroy() {

		super.destroy();

	}


        public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
                    doPost(request,response);
	}

        /**
         * doPost����
         * �÷������ڽ����û���POST��������ʱ������ҳ����POST��ʽ��������ύ������ϵͳ֧�ֵ�ҵ����ʷ�ʽ��
         * @param request HttpServletRequest
         * @param response HttpServletResponse
         * @throws ServletException
         * @throws IOException
         */
        public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

                processMessage(request, response);

	}




        /**
         * ���ݸ�����XML��Ϣ���Ը����ķ���ִ�з�����á�
         * @param msg String XML��Ϣ`````````````````````````````````````````````````````````````````````````````````````````````` 
         * @param session HttpSession session
         * @return String ���ý��XML��Ϣ
         */
        public void processMessage(HttpServletRequest request,HttpServletResponse response){
            response.setCharacterEncoding("GBK");
            String msg="";
            try {
                        msg = new String( request.getParameter("msg").getBytes("8859_1"), "UTF-8").trim();
						if(msg.indexOf("??")>-1){
							msg = request.getParameter("msg");
						}
                    } catch (UnsupportedEncodingException ex1) {
                    	ex1.printStackTrace();
        }

            if (msg == null)
                return;

            LogTool.debug(msg);

            Element root = StringTool.getXMLRoot(msg);

            String className = root.getAttributeValue("type");

	    String func = root.getAttributeValue("function");

            String log = root.getAttributeValue("log"); //��¼������־

            HttpSession session  = SessionHelper.getSession(request);

	    String[] paramTypeArr = ClassRegister.getMethodParaType(className, func);

            int paramTypeArrLength = paramTypeArr.length;


            Class cla = null;

            Object obj = null;

            Method method = null;

            try {

                cla = Class.forName(className);

                obj = cla.newInstance();

                if(paramTypeArrLength == 0 || paramTypeArr == null){//�����޲���

                    method = (Method)cla.getMethod(func,new Class[] {});

                    method.invoke(obj, new Object[] {});

                }else if(paramTypeArrLength == 1
                         && (paramTypeArr[0].equals("java.lang.String"))){

                    method = (Method)cla.getMethod(func,new Class[] {String.class});

                    method.invoke(obj, new Object[] {msg});

                } else if (paramTypeArrLength == 2
                           && (paramTypeArr[0].equals("java.lang.String"))
                           && (paramTypeArr[1].equals("javax.servlet.http.HttpSession"))){

                    method = (Method)cla.getMethod(func,new Class[] {String.class, HttpSession.class});

                    method.invoke(obj, new Object[] {msg, session});

                } else if (paramTypeArrLength == 3
                           && (paramTypeArr[0].equals("java.lang.String"))
                           && (paramTypeArr[1].equals("javax.servlet.http.HttpServletRequest"))
                           && (paramTypeArr[2].equals("javax.servlet.http.HttpServletResponse"))){

                    method = (Method)cla.getMethod(func,new Class[] {String.class,HttpServletRequest.class,HttpServletResponse.class});

                    method.invoke(obj, new Object[] {msg, request,response});

                } else if (paramTypeArrLength == 4
                           && (paramTypeArr[0].equals("java.lang.String"))
                           && (paramTypeArr[1].equals("javax.servlet.http.HttpServletRequest"))
                           && (paramTypeArr[2].equals("javax.servlet.http.HttpServletResponse"))
                           && (paramTypeArr[3].equals("javax.servlet.http.HttpSession"))){

                    method = (Method)cla.getMethod(func,new Class[] {String.class,HttpServletRequest.class,HttpServletResponse.class,HttpSession.class});

                    method.invoke(obj, new Object[] {msg, request,response,session});

                }

                String result="<?xml version=\"1.0\" encoding=\"GB2312\"?><Msg return=\"1\"></Msg>";

                //��־��¼���� �û����ܲ�����־
//            if (log!= null  && !log.equals("")){
//
//                String ip = new com.viewscenes.util.WebCacheBypass(request).getRemoteAddr();
//
//                String logSql = UserLog.insertUserLog(msg,result, ip,session);
//                if (!logSql.equalsIgnoreCase(""))
//                        JMaskServlet.userLogQueue.add(logSql);
//            }



            } catch (Exception e) {
            	e.printStackTrace();
                LogTool.warning("FileDownSerlet:processMessage()::" + e.getClass().getName() + "--" + e.toString() );
                System.out.println(e.getClass());
                PrintWriter out = null;
                try {
                    out = response.getWriter();
                } catch (IOException ex) {
                }
                out.println("<script language='javascript'>;");
                out.println("alert('�Բ���,�������س����쳣!')");
                out.println("window.history.back()");
                out.println("</script>");

            }

        }


        public void init() throws ServletException {
	}

}
