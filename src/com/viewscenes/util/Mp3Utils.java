package com.viewscenes.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Header;

/**
 * mp3�ļ������߰�
 * @author thinkpad
 *
 */
public class Mp3Utils {

	/**
	 * ��ȡmp3�ļ��Ĳ�������ʱ��,��λ����
	 * <p>class/function:com.viewscenes.util
	 * <p>explain:
	 * <p>author-date:̷��ΰ 2012-9-15
	 * @param:
	 * @return:
	 */
	public static int getMp3Duration(File mp3File){
		int sec  =0;
		if (mp3File == null)
			return sec;
		
		String name = mp3File.getName();
		if (!name.endsWith(".mp3"))
			return sec;
		
		FileInputStream fis;
		try {
			fis = new FileInputStream(mp3File);
			int b=fis.available();
			Bitstream bt=new Bitstream(fis);
			Header h=bt.readFrame();
			int time=(int) h.total_ms(b);
//			sec = time/1000;
			//����������ϵͳ������mp3�ļ���ѹ��������46���¼���ڽ�����ʱ��ֻ�ܽ�����23�룬��ʱû��ʲô�õĽ���취
			//502 �Ǹ���time�Ͳ�ѯ¼��ʱ�����ó��������һ��������mp3����time/1000���ص�ʱ������ȷ��
			sec = time/502;
			
			return sec;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BitstreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sec;
		
	}
}
