package com.viewscenes.fileUpload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class PictureUpload extends HttpServlet
{

    // private String uploadPath = "D:\\upload\\";
    private String path = "file_path.properties";
    private String skStr = "";
    private String uploadPath = "";
    private int maxPostSize = 1000 * 1024 * 1024;

    /**
	 * Handles the HTTP <code>GET</code> method.
	 * 
	 * @param request
	 *            servlet请求
	 * @param response
	 *            servlet响应
	 */
	// 如果是GET请求，则调用doGet方法。
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 * 
	 * @param request
	 *            servlet请求
	 * @param response
	 *            servlet响应
	 */
	// 如果是POST请求，则调用doPost方法
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
    public void processRequest(HttpServletRequest req, HttpServletResponse res) throws ServletException,
            IOException
    {
        System.out.println("========================打印请求信息部分开始========================================");
        System.out.println(req.toString());
        System.out.println(req.getContextPath());
        System.out.println(req.getLocalAddr());
        System.out.println(req.getLocalName());
        System.out.println(req.getLocalPort());
        System.out.println(req.getProtocol());
        System.out.println(req.getQueryString());
        System.out.println(req.getRemoteAddr());
        System.out.println(req.getRemotePort());
        System.out.println(req.getRequestURI());

        System.out.println("========================打印请求信息部分结束========================================");
        
        String filePathaa = this.getServletConfig().getServletContext().getRealPath("/");

//        Properties p = loadProperties(path);
//
//        uploadPath = p.getProperty("filepath");
       // System.out.println(uploadPath);
        res.setContentType("text/html;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");

        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(1024*20);

        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setSizeMax(maxPostSize);
        try
        {
            List fileItems = upload.parseRequest(req);
            Iterator iter = fileItems.iterator();
            while (iter.hasNext())
            {
                FileItem item = (FileItem) iter.next();
                if (!item.isFormField())
                {
                    String name = item.getName();

                    try
                    {
                        File skFile = new File(filePathaa +"\\uploadpicture\\"+ name);
                        if (skFile.exists())
                        {
                            skFile.delete();
                            item.write(new File(filePathaa  +"\\uploadpicture\\"+ name));

                        }
                        else
                        {
                            item.write(new File(filePathaa  +"\\uploadpicture\\"+ name));
                        }

                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        } catch (FileUploadException e)
        {
            e.printStackTrace();
        }

    }

    public Properties loadProperties(String path) throws IOException
    {

        InputStream in = this.getClass().getResourceAsStream(path);
        //

        Properties p = new Properties();

        p.load(in);
        in.close();
        return p;
    }

}