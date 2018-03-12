package com.viewscenes.util.business;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPFile;
import java.util.StringTokenizer;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

import java.io.InputStream;

import java.io.*;
import org.apache.tools.zip.ZipOutputStream;
import org.apache.tools.zip.ZipEntry;

/**
 *
 * �����ṩ����ftpЭ���ϴ��������ļ��ķ���
 *
 * @author MingGAO
 * @version 0.5.0 2005-8-30
 *
 */
public class FtpTransmitUtil {

  private String serverUrl = null;
  private String user = null;
  private String password = null;
  private FTPClient ftp = null;

  public FtpTransmitUtil(String server_url, String user, String password) {
    this.serverUrl = server_url;
    this.user = user;
    this.password = password;
    this.ftp = new FTPClient();
  }

  /**
   * ������
   *
   * @throws Exception
   */
  public void openConnection() throws Exception {
    try {
      int reply = -1;
      ftp.connect(this.serverUrl);
      reply = ftp.getReplyCode();
      if (!FTPReply.isPositiveCompletion(reply)) {
        ftp.disconnect();
        return;
      }
      ftp.login(this.user, this.password);
    } catch (Exception e) {
      if (ftp.isConnected()) {
        try {
          ftp.disconnect();
        } catch (IOException ioe) {
        }
      }
      throw e;
    }
  }

  /**
   * �ر�����
   *
   * @throws Exception
   */
  public void closeConnection() throws Exception {
    try {
      ftp.logout();
    } catch (Exception e) {
      throw e;
    } finally {
      if (ftp.isConnected()) {
        try {
          ftp.disconnect();
        } catch (IOException ioe) {
          // do nothing
        }
      }
    }
  }

  /**
   * �ϴ��ļ���Զ�̷�����
   * @param local_file_name
   *            ���ϴ���Ŀ¼���ļ�
   * @throws Exception
   */
  public void uploadFile(File file) throws
      Exception {
    try {
      FileInputStream fi = new FileInputStream(file);
      BufferedInputStream bi = new BufferedInputStream(fi);
      String fileName = null;
      String local_file_name = file.getName();
      int pos = local_file_name.lastIndexOf("\\");
      if (pos == -1) {
        fileName = local_file_name;
      } else {
        fileName = local_file_name.substring(pos + 1, local_file_name
                                             .length());
      }
      String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
      String filePath = "report";
      if(fileType.equalsIgnoreCase("jpg") || fileType.equalsIgnoreCase("jpeg")
         || fileType.equalsIgnoreCase("gif")) {
        filePath = "picture";
      } else if(fileType.equalsIgnoreCase("htm") || fileType.equalsIgnoreCase("html")
                || fileType.equalsIgnoreCase("swf")) {
        filePath = "report";
      } else {
        filePath = "rpt";
      }

      if(fileType.equalsIgnoreCase("jpg") || fileType.equalsIgnoreCase("jpeg")
         || fileType.equalsIgnoreCase("gif") || fileType.equalsIgnoreCase("swf")) {
        ftp.setFileType(FTP.IMAGE_FILE_TYPE);
      } else if(fileType.equalsIgnoreCase("htm") || fileType.equalsIgnoreCase("html")
                || fileType.equalsIgnoreCase("jsp")) {
        ftp.setFileType(FTP.ASCII_FILE_TYPE);
      } else if(fileType.equalsIgnoreCase("txt")){
    	  ftp.setFileType(FTP.BINARY_FILE_TYPE);
      } else {
    	  throw new Exception("�ļ����ʹ���");
      }
      ftp.changeWorkingDirectory(filePath);
      ftp.storeFile(fileName, bi);
      bi.close();
    } catch (Exception e) {
      if (ftp.isConnected()) {
        try {
          ftp.disconnect();
        }
        catch (IOException ioe) {
          // do nothing
        }
      }
      throw e;
    }
  }

  public static void main(String[] args) throws Exception {


//    FtpTransmitUtil util = new FtpTransmitUtil("10.10.2.80", "Anonymous", null);
 FtpTransmitUtil util = new FtpTransmitUtil("10.10.2.80", "user", "user");
// util.openConnection();
// util.downloadFileFromFtp("D:\\video_location\\", "Btest_500018630_20100523131013_20100523131113_97400_R1.mp3");
// util.closeConnection();
    try {
      util.openConnection();
     util.uploadFile(new File("D:\\test1.txt"));
// util.downloadFtpFile("c:\\","ftp://10.10.5.35//20091209/OAS06/������̨�ṩ_OAS06_����_15130kHz_��_564_20091209__114702_�ճ�.067");
	util.closeConnection();
    } catch (Exception e) {
      e.printStackTrace();
      try {
        util.closeConnection();
      } catch (Exception ex) {
      }
    }

  }
  /**
   * ��Զ�̷������������ļ�����ɿ������������ļ���
   *
   * @param local_dir           ����Ŀ¼
   * @param remote_file_name          Զ���ļ�����׺
   * @throws Exception
   */
  public void downloadFileFromFtp(String local_dir, String fileName) throws Exception {
    try {

          FileOutputStream fo = new FileOutputStream(local_dir
              + fileName);
          BufferedOutputStream bo = new BufferedOutputStream(fo);
           Thread.currentThread().sleep(10000); // ��ʱ1��
         try
         {
             ftp.retrieveFile(fileName, bo); //��FTP�����ط����������ļ�
             //ftp.deleteFile(fileName); //��FTP��ɾ���������ļ�
         }catch(Exception e)
         {

         }
          bo.flush();
          bo.close();


    } catch (Exception e) {
      if (ftp.isConnected()) {
        try {
          ftp.disconnect();
        }
        catch (IOException ioe) {
          // do nothing
        }
      }
      throw e;
    }
  }
  /**
   * ��Զ�̷������������ļ�����ɿ������������ļ���
   *
   * @param local_dir           ����Ŀ¼
   * @param remote_file_name          Զ���ļ�����׺
   * @throws Exception
   */
  public void downloadFile(String local_dir, String remote_fileSuffix) throws Exception {
    try {
      String fileName = null;

      FTPFile[] files = ftp.listFiles();

      for (int i = 0; i < files.length; i++) {
        fileName = files[i].getName();
        if (fileName.endsWith(remote_fileSuffix)) {
          FileOutputStream fo = new FileOutputStream(local_dir + "\\"
              + fileName);
          BufferedOutputStream bo = new BufferedOutputStream(fo);

          ftp.retrieveFile(fileName, bo); //��FTP�����ط����������ļ�
          //ftp.deleteFile(fileName); //��FTP��ɾ���������ļ�
          bo.flush();
          bo.close();
        }
      }
    } catch (Exception e) {
      if (ftp.isConnected()) {
        try {
          ftp.disconnect();
        }
        catch (IOException ioe) {
          // do nothing
        }
      }
      throw e;
    }
  }
  /**
    * ��Զ�̷������������ļ���
    *
    * @param local_dir           ����Ŀ¼
    * @param remote_file_name          Զ���ļ���
    * @throws Exception
    */
   public boolean downloadFtpFile(HttpServletRequest request, HttpServletResponse response, String remote_fileSuffix) throws Exception {
     try {
       String fileName = null;
       ArrayList fileList=new ArrayList();
      if(remote_fileSuffix.equals("all"))
      {
      String tmpFile="c:\\ftpDowndAll";
      String tmpZip="c:\\all.zip";
      File f=new File(tmpFile);
      File zf=new File(tmpZip);

      fileList = (ArrayList) request.getSession().getAttribute("allFile");

    OutputStream fo =response.getOutputStream();
        for(int k=0;k<fileList.size();k++)
        {
           String strArray[]=fileList.get(k).toString().split("/");
           for (int i = 4; i < strArray.length - 1; i++) {
           ftp.cwd(strArray[i]);
           }
        fileName= strArray[strArray.length-1];

        String str="";
       ftp.setControlEncoding("GBK");
        str = fileName;

       fileName = fileName+".mp3";

       response.setContentType("application/x-msdownload");
       response.setHeader("Content-Disposition","attachment;filename=all.zip");
       str=new String(str.getBytes("GBK"),"ISO-8859-1");
       if(!f.exists())
       f.mkdir();
       FileOutputStream lo = new FileOutputStream(f+"\\"
            + fileName);

        BufferedOutputStream bo = new BufferedOutputStream(lo);

       ftp.retrieveFile(str, bo);
       bo.flush();
       bo.close();

       lo.flush();
       lo.close();
        //��FTP�����ط����������ļ�
       //ftp.deleteFile(fileName); //��FTP��ɾ���������ļ�
      }
      System.out.println("�������-------");
      this.zip(tmpFile,tmpZip);



      if (f.isDirectory()) {
        File[] files = f.listFiles();
        for (int i = 0; i < files.length; i++) {
          files[i].delete();
        }

      }else
      {
        System.out.println("�����ļ��� !");
      }


    InputStream is = new FileInputStream(zf);
    BufferedInputStream bis = new BufferedInputStream(is);

    byte [] buf = new byte [1024];

       int length = 0;
      while ((length = bis.read(buf)) > 0) {

                  fo.write(buf, 0, length);

        }
//     File fff=new File("c:\\all.zip");
//     try
//     {
//       fff.deleteOnExit();
//
//     }
//     catch (Exception e) {
//       System.out.println("���������!");
//       e.printStackTrace();
//     }

        fo.flush();
        fo.close();
        bis.close();
        is.close();
        fo=null;

      }else
      {
       String strArray[]=remote_fileSuffix.split("/");
       for(int i=4;i<strArray.length-1;i++)
       {
         ftp.cwd(strArray[i]);
       }
         fileName= strArray[strArray.length-1];

         String str="";
        ftp.setControlEncoding("GBK");

      str = fileName;

        fileName = fileName+".mp3";
        response.setContentType("application/x-msdownload");
        response.setHeader("Content-Disposition","attachment;filename="+new String(fileName.getBytes("GBK"),"ISO-8859-1"));
        File file=new File("c:\\");
        OutputStream fo =response.getOutputStream();
        str=new String(str.getBytes("GBK"),"ISO-8859-1");
        ftp.retrieveFile(str, fo);
          //��FTP�����ط����������ļ�
        //ftp.deleteFile(fileName); //��FTP��ɾ���������ļ�
        fo.flush();
        fo.close();
       }
     } catch (Exception e) {
       if (ftp.isConnected()) {
         try {
           ftp.disconnect();
         }
         catch (IOException ioe) {
           // do nothing
         }
         return false;
       }
       throw e;
     }
     return true;
   }

   /**
    * ���ܣ��� sourceDir Ŀ¼�µ������ļ����� zip ��ʽ��ѹ��������Ϊָ�� zip �ļ� create date:2009- 6- 9
    * author:Administrator
    *
    * @param sourceDir
    * @param zipFile
    *             ��ʽ�� E:\\stu \\zipFile.zip ע�⣺���� zipFile ���Ǵ�����ַ���ֵ��
    *             �� "E:\\stu \\" ���� "E:\\stu "
    *             ��� E ���Ѿ����� stu ����ļ��еĻ�����ô�ͻ���� java.io.FileNotFoundException: E:\stu
    *             ( �ܾ����ʡ� ) ����쳣������Ҫע����ȷ���ε��ñ�����Ŷ
    *
    */

    public static void zip(String sourceDir, String zipFile) {

       try {

          OutputStream os = new FileOutputStream(zipFile);
           BufferedOutputStream bos = new BufferedOutputStream(os);
           ZipOutputStream zos = new ZipOutputStream(bos);

           File file = new File(sourceDir);

           String basePath = null ;

          if (file.isDirectory()) {
              basePath = file.getPath();
           } else {
              basePath = file.getParent();
           }

           zipFile (file, basePath, zos);
        zos.closeEntry();
        zos.close();
     } catch (Exception e) {
        // TODO Auto-generated catch block
           e.printStackTrace();
       }


    }

//    /**
//      *
//      * create date:2009- 6- 9 author:Administrator
//      *
//      * @param source
//      * @param basePath
//      * @param zos
//      * @throws IOException
//      */
//
    private static void zipFile(File source, String basePath,

           ZipOutputStream zos) {

       File[] files = new File[0];
       if (source.isDirectory()) {
       files = source.listFiles();

       } else {
           files = new File[1];
           files[0] = source;

       }



       String pathName;

       byte [] buf = new byte [1024];

       int length = 0;

       try {

           for (int i=0;i<files.length;i++) {
             File file=files[i];
              if (file.isDirectory()) {

                  pathName = file.getPath().substring(basePath.length() + 1)

                         + "/" ;

                  zos.putNextEntry( new ZipEntry(pathName));

                  zipFile (file, basePath, zos);

              } else {

                  pathName = file.getPath().substring(basePath.length() + 1);
                  InputStream is = new FileInputStream(file);
                  BufferedInputStream bis = new BufferedInputStream(is);
                  zos.putNextEntry( new ZipEntry(pathName));

                  while ((length = bis.read(buf)) > 0) {

                     zos.write(buf, 0, length);

                  }

                  is.close();

              }

           }

       } catch (Exception e) {

           // TODO Auto-generated catch block

           e.printStackTrace();

       }



    }



}
