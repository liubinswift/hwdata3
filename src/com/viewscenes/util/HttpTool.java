package com.viewscenes.util;



import java.net.*;

import java.io.*;



public class HttpTool {



  public HttpTool() {

  }


  /**

   * ʵ��HTTP Get����������һ�����ӣ���html��Ϊ�ַ������ء���ʧ�ܷ��س���Ϊ0���ַ���

   * @param sUrl

   * @return

   */

  public static String HttpGet(String sUrl){

    String sContent = "";

    HttpURLConnection hc=null;

    try {

        //open a http conneciton

        URL url = new URL(sUrl);

        hc = (HttpURLConnection)url.openConnection();
        hc.setRequestMethod("POST");

        InputStream instream = hc.getInputStream();



        //write to buffer

        byte[] buffer = new byte[4096];

        byte[] sBuffer = new byte[4096];

        int nCount = 0;

        int nRead = 0;

        while (nRead>-1){

          nRead = instream.read(buffer);

          String temp = null;

          if (nRead>-1){

            if ((nCount+nRead)>=sBuffer.length){

              byte[] sNewBuffer = new byte[sBuffer.length*2];

              System.arraycopy(sBuffer,0,sNewBuffer,0,nCount);

              System.arraycopy(buffer,0,sNewBuffer,nCount,nRead);

              nCount += nRead;

              sBuffer = sNewBuffer;

            }

            else{

              System.arraycopy(buffer,0,sBuffer,nCount,nRead);

              nCount += nRead;

            }

          }

        }

        sContent = new String(sBuffer,0,nCount);

    }

    catch (Exception e){

      LogTool.debug(e);

      sContent = "";

    }

    finally{

      if (hc!=null)

        hc.disconnect();

    }

    return sContent;

  }

  /**
   * ʵ��HTTP Get����������һ�����ӣ���html��Ϊ�ַ������ء���ʧ�ܷ��س���Ϊ0���ַ���
   * @param sUrl
   * @return
   */

  public static String HttpGetString(String sUrl,String charCodeName) throws HttpFileNotFoundException, HttpNetworkFailException,UnsupportedEncodingException{
    byte[] buffer = HttpGetBytes(sUrl);
    String sContent = new String(buffer, 0, buffer.length, charCodeName);
    return sContent;
  }

  public static String HttpGetString(String sUrl) throws HttpFileNotFoundException, HttpNetworkFailException{
    byte[] buffer = HttpGetBytes(sUrl);
    String sContent = null;
    try {
      sContent = new String(buffer, 0, buffer.length, "GB2312");
    }
    catch (UnsupportedEncodingException ex) {
      ex.printStackTrace();
    }
    return sContent;
  }


    /**
     * ʵ��HTTP Get����������һ�����ӣ�����byte���顣��ʧ�ܷ��س���Ϊ0������
     * @param sUrl http url
     * @return byte����
     */

    public static byte[] HttpGetBytes(String sUrl) throws HttpFileNotFoundException, HttpNetworkFailException{
      byte[] content = null;
      HttpURLConnection hc=null;

          try {
            //open a http conneciton
            URL url = new URL(sUrl);
            hc = (HttpURLConnection) url.openConnection();
            //��÷���code,������404��˵���ļ�������
            int resCode = hc.getResponseCode();
            if (resCode==404)
              throw new HttpFileNotFoundException(sUrl);

            //������ݳ���
            int length = hc.getContentLength();
            if (length < 0) {
              throw new HttpNetworkFailException(sUrl);
            }

            //��ȡ���ݵ�����
            content = new byte[length];
            InputStream instream = hc.getInputStream();
            //write to buffer
            byte[] buffer = new byte[4096];
            int nCount = 0;
            int nRead = instream.read(buffer);
            while (nRead > -1) {
                System.arraycopy(buffer, 0, content, nCount, nRead);
                nCount += nRead;
                nRead = instream.read(buffer);
            }
          }
          catch (IOException ex) {
            throw new HttpNetworkFailException(ex);
          }
      finally{
        if (hc!=null)
          hc.disconnect();
      }
      return content;
    }

    /**
     * ʵ��HTTP Get����������һ�����ӣ�����byte���顣��ʧ�ܷ��س���Ϊ0������
     * @param sUrl http url
     * @return byte����
     */

    public static byte[] HttpPostBytes(String sUrl,String postContent) throws HttpFileNotFoundException, HttpNetworkFailException{
      byte[] content = null;
      HttpURLConnection hc=null;

          try {
            //open a http conneciton
            URL url = new URL(sUrl);
            hc = (HttpURLConnection) url.openConnection();
            hc.setRequestMethod("POST");
            OutputStream os = hc.getOutputStream();
            os.write(postContent.getBytes("GB2312"));
            //��÷���code,������404��˵���ļ�������
            int resCode = hc.getResponseCode();
            if (resCode==404)
              throw new HttpFileNotFoundException(sUrl);

            //������ݳ���
            int length = hc.getContentLength();
            if (length < 0) {
              throw new HttpNetworkFailException(sUrl);
            }

            //��ȡ���ݵ�����
            content = new byte[length];
            InputStream instream = hc.getInputStream();
            //write to buffer
            byte[] buffer = new byte[4096];
            int nCount = 0;
            int nRead = instream.read(buffer);
            while (nRead > -1) {
                System.arraycopy(buffer, 0, content, nCount, nRead);
                nCount += nRead;
                nRead = instream.read(buffer);
            }
          }
          catch (IOException ex) {
            throw new HttpNetworkFailException(ex);
          }
      finally{
        if (hc!=null)
          hc.disconnect();
      }
      return content;
    }


    public static void HttpGetFile(String sUrl, String fileName) throws
        HttpFileNotFoundException, HttpNetworkFailException, UtilException {
      HttpURLConnection hc = null;

      try {
        File file = new File(fileName);
        if (file.exists())
          file.delete();
        //open a http conneciton
        URL url = new URL(sUrl);
        hc = (HttpURLConnection) url.openConnection();
        //��÷���code,������404��˵���ļ�������
        int resCode = hc.getResponseCode();
        if (resCode == 404)
          throw new HttpFileNotFoundException(sUrl);


        //��ȡ���ݵ�����
        InputStream instream = hc.getInputStream();
        //write to buffer
        byte[] buffer = new byte[40960];
        int nCount = 0;
        int nRead = instream.read(buffer);
        while (nRead > -1) {
          FileTool.writeFile(fileName, buffer, 0, nRead, true);
          nCount += nRead;
          nRead = instream.read(buffer);
        }
      }
      catch (IOException ex) {
        File f = new File(fileName);
        f.delete();
        throw new HttpNetworkFailException(ex);
      }
      finally {
        if (hc != null)
          hc.disconnect();
      }

    }


}
