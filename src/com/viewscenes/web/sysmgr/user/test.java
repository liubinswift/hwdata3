package com.viewscenes.web.sysmgr.user;

import java.math.BigDecimal;

public class test {

	/** 
	 * @Title: main 
	 * @Description: TODO(������һ�仰�����������������) 
	 * @param @param args    �趨�ļ� 
	 * @return void    �������� 
	 * @throws 
	 */
	public static void main(String[] args) {
     BigDecimal d =new BigDecimal(100.00);
     System.out.println(d.setScale(2,BigDecimal.ROUND_DOWN));
     float listionRate =Float.valueOf("100.00");
     if(listionRate>=80)
     {
    	 System.out.print(listionRate);
     }
	}

}
