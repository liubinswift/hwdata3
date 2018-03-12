package com.viewscenes.util.report.util;

public final class Encoding {
	public static String strTemp ="Ϊ������ʱ����";
	/**
	 * ��url��ת�����ĵ��ַ�ʹ������ASCII�룬��ֹ����
	 * ����%A4 %3F
	 * Ӣ���ַ���ת��
	 * @author: liqing
	 * @param url
	 * @return String
	 */
    public static String strTOascii(String url) {
		byte[] bytes = null;
		String ascii = "";
		try {
			for (int i = 0; i < url.length(); i++) {
				bytes = (String.valueOf(url.toCharArray()[i])).getBytes();
				if (bytes.length == 1) { // Ӣ���ַ�
					ascii = ascii + (new String(bytes));
				}
				if (bytes.length == 2) { // �����ַ�
					ascii += "%"
							+ Integer.toHexString(bytes[0]).toUpperCase()
									.substring(6, 8);
					ascii += "%"
							+ Integer.toHexString(bytes[1]).toUpperCase()
									.substring(6, 8);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ascii;
	}
    
	/**
	 * ת��url�������ַ�(�Էǰ�ȫ�ַ�����ת��: <>\\#%{}|^~[]`")          
	 * @param url
	 *            Ҫת����url
	 * @return
	 */
	public static String getURL2Str(String url) {
		String changeUrl = url;
		if (changeUrl != null) {
			try {
				// �������replaceAll �ĵ�һ��parameter������ regular expression �Ļ�,��"\\Q"��"\\E"��ʾ����������ʽ
				String notRegular = "\\Q";
				// ��%����һ��Ҫ������λ,��Ϊת����������ַ�������%����
				changeUrl = changeUrl.replaceAll("%(?!(2[02357]|3[ce]|7[bcde]|5[bcde]|60))","%25");
				changeUrl = changeUrl.replaceAll(notRegular + " ", "%20");
				changeUrl = changeUrl.replaceAll(notRegular + "<", "%3c");
				changeUrl = changeUrl.replaceAll(notRegular + ">", "%3e");
				changeUrl = changeUrl.replaceAll(notRegular + "\"", "%22");
				changeUrl = changeUrl.replaceAll(notRegular + "#", "%23");
				changeUrl = changeUrl.replaceAll(notRegular + "{", "%7b");
				changeUrl = changeUrl.replaceAll(notRegular + "}", "%7d");
				changeUrl = changeUrl.replaceAll(notRegular + "|", "%7c");
				changeUrl = changeUrl.replaceAll(notRegular + "\\", "%5c");
				changeUrl = changeUrl.replaceAll(notRegular + "^", "%5e");
				changeUrl = changeUrl.replaceAll(notRegular + "~", "%7e");
				changeUrl = changeUrl.replaceAll(notRegular + "[", "%5b");
				changeUrl = changeUrl.replaceAll(notRegular + "]", "%5d");
				changeUrl = changeUrl.replaceAll(notRegular + "`", "%60");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("ת��url�������ַ�(�Էǰ�ȫ�ַ�����ת��)����!");
			}
		}
		return changeUrl;
	}
}
