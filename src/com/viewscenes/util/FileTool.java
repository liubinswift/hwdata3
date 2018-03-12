package com.viewscenes.util;

import java.io.*;
import org.jdom.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.*;

/**
 * 
 * <p>
 * ����: �ļ���������
 * </p>
 * <p>
 * Ŀ��: �ṩһЩ���õ��ļ���Щ����
 * </p>
 * <p>
 * ��֪����: �������ܵĿ��ǣ������ߵ��ļ���д����û��ʹ��ͬ������ �ڶ��̵߳�Ӧ���е��ô˹���д��ͬһ�ļ�ʱ������ʹ��ͬ�����ƣ������п��ܷ�����ͻ
 * </p>
 * <p>
 * �޸���ʷ: 2005-9 �¸� ����
 * </p>
 * ��ע:
 * 
 */
public class FileTool {
	final static String DEFAULT_CHAR_CODE = "GB2312";

	public FileTool() {
	}

	/**
	 * д���ַ������ļ���ʹ��׷��д�ķ�ʽ�������ļ��Ѿ����ڣ���������ӵ��ļ�ĩβ�� ��ָ�����ļ���Ŀ¼�����ڣ����������Զ���������ļ���Ŀ¼
	 * 
	 * @param fileName
	 *            Ҫд����Ϣ���ļ���
	 * @param fileString
	 *            Ҫд���ļ�������
	 * @throws UtilException
	 *             ��д������׳����쳣
	 */
	public static void writeFile(String fileName, String fileString)
			throws UtilException {
		try {
			byte[] buffer = fileString.getBytes(DEFAULT_CHAR_CODE);
			writeFile(fileName, buffer, 0, buffer.length, false);
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
	}

	public static Object file2Object(String fileName) {
		FileInputStream in = null;
		ObjectInputStream objIn = null;
		Object obj = null;
		try {
			in = new FileInputStream(fileName);
			objIn = new ObjectInputStream(in);
			obj = objIn.readObject();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return obj;
	}

	public static void writeFile(String fileName, Object obj)
			throws UtilException {
		FileOutputStream out = null;
		// ZipOutputStream zipOut = null;
		ObjectOutputStream objOut = null;

		try {
			out = new FileOutputStream(fileName);
			// zipOut = new ZipOutputStream(out);
			objOut = new ObjectOutputStream(out);
			objOut.writeObject(obj);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				objOut.close();
			} catch (IOException ex1) {
			}
			/*
			 * try { zipOut.close(); } catch (IOException ex1) { }
			 */try {
				out.close();
			} catch (IOException ex1) {
			}
		}
	}

	/**
	 * д����������鵽�ļ��У�ʹ��׷��д�ķ�ʽ�������ļ��Ѿ����ڣ���������ӵ��ļ�ĩβ ��ָ�����ļ���Ŀ¼�����ڣ����������Զ���������ļ���Ŀ¼
	 * 
	 * @param fileName
	 *            �ļ�ȫ·����
	 * @param buffer
	 *            ����������
	 * @throws UtilException
	 *             ��д������׳����쳣
	 */
	public static void writeFile(String fileName, byte[] buffer)
			throws UtilException {
		writeFile(fileName, buffer, 0, buffer.length, true);
	}

	/**
	 * ���ַ������µ��ļ������ļ��Ѿ�������ʹ�������ݸ���ԭ�����ݡ�
	 * 
	 * @param fileName
	 *            Ҫд����Ϣ���ļ���
	 * @param fileString
	 *            Ҫд���ļ�������
	 * @throws UtilException
	 *             д��ʧ���׳����쳣
	 */
	public static void updateFile(String fileName, String fileString)
			throws UtilException {

		try {
			// byte[] buffer = fileString.getBytes(DEFAULT_CHAR_CODE);
			byte[] buffer = fileString.getBytes("UTF-8");
			writeLog(fileName, buffer, 0, buffer.length, true);
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * �ö��������ݸ����ļ������ļ��Ѿ�������ʹ�������ݸ���ԭ�����ݡ�
	 * 
	 * @param fileName
	 *            Ҫд����Ϣ���ļ���
	 * @param buffer
	 *            Ҫд���ļ�������
	 * @throws UtilException
	 *             д��ʧ���׳����쳣
	 */
	public static void updateFile(String fileName, byte[] buffer)
			throws UtilException {
		writeFile(fileName, buffer, 0, buffer.length, true);
	}

	/**
	 * ����дString���ļ���,��ȡ׷��д�ķ�ʽ���÷����ڼ�¼��־��ʹ��
	 * 
	 * @param fileName
	 *            Ҫд����Ϣ���ļ���
	 * @param buffer
	 *            Ҫд���ļ�������
	 * @param off
	 *            ����д�����ʼλ��
	 * @param len
	 *            д��ĳ���
	 * @param bAppend
	 *            �Ƿ�׷��д
	 * @throws UtilException
	 *             д���ļ����������׳��쳣
	 */
	public static void writeLog(String fileName, byte[] buffer, int off,
			int len, boolean bAppend) throws UtilException {
		FileOutputStream fos = null;
		try {
			File file = new File(fileName);

			// ���ļ�����������Ҫ�ȴ����ļ�����Ŀ¼
			if (!file.exists()) {
				// ����ɾ������90��ǰ����־��
				Date d = new Date();
				d.setDate(d.getDate() - 90);

				File allFile = file.getParentFile().getParentFile();
				if (allFile.exists()) {
					File[] listFile = allFile.listFiles();
					for (int i = 0; i < listFile.length; i++) {
						File deleFile = listFile[i];
						if (deleFile.lastModified() < d.getTime()) {
							if (deleFile.isDirectory()) {
								try {

									deleteFolder(deleFile);
								} catch (Exception e) {
									e.printStackTrace();
								}

							}
						}
					}
				}

				makeDirectory(file.getParent());
			}
			// д���ļ�
			fos = new FileOutputStream(fileName, bAppend);

			fos.write(buffer, off, len);
		} catch (IOException e) {
			throw new UtilException("д�ļ�����:" + e.getMessage(), e);
		} finally {
			try {
				if (fos != null)
					fos.close();
			} catch (IOException ex) {
				throw new UtilException("�޷��ر�FileOutputStream"
						+ ex.getMessage(), ex);
			}
		}
	}

	/**
	 * ����������ʽд���ļ�,��ȡ׷��д�ķ�ʽ,��д����ļ���С������
	 * 
	 * @param fileName
	 *            Ҫд����Ϣ���ļ���
	 * @param buffer
	 *            Ҫд���ļ�������
	 * @param off
	 *            ����д�����ʼλ��
	 * @param len
	 *            д��ĳ���
	 * @param bAppend
	 *            �Ƿ�׷��д
	 * @throws UtilException
	 *             д���ļ����������׳��쳣
	 */
	public static void writeFile(String fileName, byte[] buffer, int off,
			int len, boolean bAppend) throws UtilException {
		FileOutputStream fos = null;
		try {
			File file = new File(fileName);

			// ���ļ�����������Ҫ�ȴ����ļ�����Ŀ¼
			if (!file.exists()) {
				makeDirectory(file.getParent());
			}
			// д���ļ�
			fos = new FileOutputStream(fileName, bAppend);

			fos.write(buffer, off, len);
			
		} catch (IOException e) {
			throw new UtilException("д�ļ�����:" + e.getMessage(), e);
		} finally {
			try {
				if (fos != null)
					fos.close();
			} catch (IOException ex) {
				throw new UtilException("�޷��ر�FileOutputStream"
						+ ex.getMessage(), ex);
			}
		}
	}

	/**
	 * ɾ��Ŀ¼�������ݹ����
	 */
	private static void deleteFolder(File dir) {
		File fileList[] = dir.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			if (fileList[i].isDirectory()) {
				deleteFolder(fileList[i]);
			} else {
				fileList[i].delete();
			}
		}
		dir.delete();
	}

	/**
	 * �ݹ鴴���ļ�Ŀ¼
	 * 
	 * @param pathName
	 *            ����·������
	 */
	private static void makeDirectory(String pathName) {
		if (pathName == null)
			return;
		File file = new File(pathName);
		if (!file.exists()) {
			if (file.getParent() != null)
				makeDirectory(file.getParent());
			file.mkdirs();
		}
	}

	/**
	 * ����дString��Exception��StackTrace���ļ���,��ȡ׷��д�ķ�ʽ
	 * 
	 * @param fileName
	 *            Ҫд����Ϣ���ļ���
	 * @param fileString
	 *            Ҫд���ļ�������
	 * @param exp
	 *            Ҫд���ļ����쳣
	 * @throws UtilException
	 */

	public static void writeStackTrace(String fileName, String fileString,
			Exception exp) throws UtilException {

		FileOutputStream fos = null;
		PrintWriter pw = null;
		try {
			fileString += "\r\n";
			File file = new File(fileName);
			if (!file.exists()) {
				if (file.getParent() != null)
					new File(file.getParent()).mkdirs();
			}
			fos = new FileOutputStream(fileName, true); // ����Ϊ׷��д�ķ�ʽ
			byte[] b = fileString.getBytes("GB2312");
			fos.write(b);
			pw = new PrintWriter(fos);
			exp.printStackTrace(pw);
		} catch (IOException e) {
			throw new UtilException("��¼��������Ϣʱд�ļ�����", e);
		} finally {
			try {
				if (pw != null)
					pw.close();
				fos.close();
			} catch (IOException ex) {
				throw new UtilException("�޷��ر�FileOutputStream", ex);
			}
		}
	}

	/**
	 * ���ļ���ȡ��byte������
	 * 
	 * @param fileName
	 *            �ļ�ȫ·����
	 * @return ����ļ����������ݵ�byte����
	 * @throws UtilException
	 *             ��ȡ�ļ����������׳��쳣
	 */

	public static byte[] readFile(String fileName) throws UtilException {

		byte buffer[] = null;
		FileInputStream fin = null;

		try {
			File file = new File(fileName);
			if (!file.exists()) {
				throw new UtilException("��ȡ�ļ�ʧ�ܣ��ļ�" + fileName + "������");
			}

			long len = file.length();
			buffer = new byte[(int) len];
			fin = new FileInputStream(file);

			int r = fin.read(buffer);
			if (r != len) {
				throw new UtilException("�޷���ȡȫ���ļ�, ��ȡ�ĳ���" + r + " != �ļ��ĳ���"
						+ len);
			}
		} catch (IOException ex) {
			throw new UtilException("��ȡ�ļ�ʧ��" + ex.getMessage(), ex);
		}

		finally {
			try {
				if (fin != null) {
					fin.close();
				}
			} catch (IOException ex) {
				throw new UtilException("�޷��ر�FileInputtStream:"
						+ ex.getMessage(), ex);
			}
		}
		return buffer;
	}

	/**
	 * ��ȡ�ı��ļ����ַ�����Ĭ���ַ�����ΪGB2312
	 * 
	 * @param fileName
	 *            �ļ�ȫ·����
	 * @return �ı��ַ���
	 * @throws UtilException
	 *             ����ȡʧ���׳����쳣
	 */
	public static String FileToString(String fileName) {

		String str = null;
		try {
			byte buffer[] = readFile(fileName);
			str = new String(buffer, DEFAULT_CHAR_CODE);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return str;
	}

	/**
	 * ����ļ��������ʱ��
	 * 
	 * @param fileName
	 *            �ļ�·����
	 * @return ����ʱ��
	 */
	public static long getFileModifyTime(String fileName) {
		if (fileName == null)
			return 0;
		File file = new File(fileName);
		return file.lastModified();
	}

	/**
	 * 
	 * load xml file
	 * 
	 * @param fileName
	 * 
	 * @return
	 * 
	 * @throws UtilException
	 */

	public static Element loadXMLFile(String fileName) throws UtilException {

		String str_msg = null;

		str_msg = FileTool.FileToString(fileName);

		if (str_msg == null) {

			throw new UtilException("Load xml file failed! "

			+ "Can't load file content from "

			+ fileName);

		}

		Element root = null;

		// parse xml config file;

		try {

			StringReader read = new StringReader(str_msg);

			org.jdom.input.SAXBuilder builder = new org.jdom.input.SAXBuilder(
					false);

			Document doc = builder.build(read);

			// get root element

			root = doc.getRootElement();

		}

		catch (JDOMException ex) {

			throw new UtilException("Parse xml error:" + ex.getMessage(), ex);

		}

		return root;

	}

	public static void appendFile(String fileName, String appendFileName,
			int off) throws UtilException {
		FileOutputStream fos = null;
		FileInputStream fis = null;
		try {
			File file = new File(fileName);

			// ���ļ�����������Ҫ�ȴ����ļ�����Ŀ¼
			if (!file.exists()) {
				makeDirectory(file.getParent());
			}
			// д���ļ�
			fos = new FileOutputStream(fileName, true);
			fis = new FileInputStream(appendFileName);
			byte[] buffer = new byte[40960];
			int nRead = fis.read(buffer, 0, 40960);
			while (nRead > -1) {
				fos.write(buffer, off, nRead - off);
				nRead = fis.read(buffer);
				off = 0;
			}
		} catch (IOException e) {
			throw new UtilException("��д�ļ�����:" + e.getMessage(), e);
		} finally {
			try {
				if (fos != null)
					fos.close();
				if (fis != null)
					fis.close();
			} catch (IOException ex) {
				throw new UtilException("�޷��ر��ļ���" + ex.getMessage(), ex);
			}
		}
	}

}
