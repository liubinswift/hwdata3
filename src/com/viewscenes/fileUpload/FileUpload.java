package com.viewscenes.fileUpload;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class FileUpload extends HttpServlet {
	
	public Hashtable sampleFile = new Hashtable();

	private String uploadPath = "c:\\runplan\\"; // �����ļ����ϴ�·��
	private int MAXFILESIZE = 1024 * 1024 * 1024; // �����ļ����ϴ���С

	public FileUpload() {
		super();
	}
	public void destroy() {
		super.destroy();
	}

	// ������
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		// �����ļ����������� |Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(4096); // ���������С�Ŵ��̣���������ڴ���
		ServletFileUpload upload = new ServletFileUpload(factory); // Create a new file upload handler
		upload.setSizeMax(MAXFILESIZE);
		try {
			List fileItems = upload.parseRequest(request); // Parse the request,have a List of file items that you need to process
			Iterator iter = fileItems.iterator();
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				// ���item.isFormFieldΪtrue,�������򣬷���Ϊfile��Field��2�֣�һ���Ǽ���һ����file��
				if (!item.isFormField()) {
					// ����ļ����������ļ�����չ��
					String name = item.getName();
					try {
						String fileName = uploadPath + name;
						File file = new File(fileName);

					     //���ļ�����������Ҫ�ȴ����ļ�����Ŀ¼
					     if (!file.exists()) {
					       makeDirectory(file.getParent());
					      }
						item.write(file);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
	}
	  /**
	   * �ݹ鴴���ļ�Ŀ¼
	   * @param pathName ����·������
	   */
	  private static void makeDirectory(String pathName){
	    if (pathName==null)
	      return;
	    File file = new File(pathName);
	    if (!file.exists()) {
	      if (file.getParent() != null)
	        makeDirectory(file.getParent());
	      file.mkdirs();
	    }
	  }
	/**
	 * Handles the HTTP <code>GET</code> method.
	 * 
	 * @param request
	 *            servlet����
	 * @param response
	 *            servlet��Ӧ
	 */
	// �����GET���������doGet������
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 * 
	 * @param request
	 *            servlet����
	 * @param response
	 *            servlet��Ӧ
	 */
	// �����POST���������doPost����
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
	
	/**
	 * Returns a short description of the servlet.
	 */
	public String getServletInfo() {
		return "ServletName: FileUploaded. extends: HttpServlet.";
	}



	
	public void addSampleFile(String key, String filepath) {
		sampleFile.put(key, filepath);
	}
}
