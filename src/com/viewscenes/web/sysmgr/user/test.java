package com.viewscenes.web.sysmgr.user;

import java.math.BigDecimal;

public class test {

	/** 
	 * @Title: main 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param args    设定文件 
	 * @return void    返回类型 
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
