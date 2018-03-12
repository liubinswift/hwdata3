package com.viewscenes.web.runplanManager.gjtRunplan;

import java.math.BigDecimal;

import javax.ejb.Local;

import edu.emory.mathcs.backport.java.util.Arrays;

public class Test {
	
	private static final Test test = new Test();
	
	
	private Test(){};
	
	public static Test getInstance(){
		return test;
	}
	
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		int  num = -13;
//		Date now = new Date();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");        
//		now.setHours(now.getHours()+num);
//		String snow = sdf.format(now);          
//		System.out.println(snow);
//		
//		String aa = "2013-05-10 23:34:11";
//		String bb = aa.substring(11,13);
//		String cc = aa.substring(14,16);
//		System.out.println(bb);
//		System.out.println(cc);
//		System.out.println(Integer.parseInt(bb)+1);
		double i = 3.256;
		System.out.println("四舍五入取整:(3.856)="
			    + new BigDecimal(i).setScale(0, BigDecimal.ROUND_HALF_UP));
		int m=4;
		int n=7;
		System.out.println(Double.parseDouble(m+"")/n);

		 
	}

}
