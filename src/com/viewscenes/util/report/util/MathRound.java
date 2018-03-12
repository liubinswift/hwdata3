package com.viewscenes.util.report.util;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.viewscenes.pub.GDSet;
import com.viewscenes.pub.GDSetTool;

public class MathRound {
    public MathRound() {
    }
public final static int StartRow=18;

    /**
     * ����С��λ��
     * @param scale	С������
     * @param v 	Double���͵Ĳ���
     * @return ��������������ִ�
     */
    public static String round(double v, int scale) {
        String returnValue;
        DecimalFormat dataFormat;
        int scaleValue;
        if (scale == 0) {
            scaleValue = 1;
        } else {
            scaleValue = 10;
        }

        double tempDBValue = v;
        for (int i = 1; i < scale; i++) {
            scaleValue *= 10;
        }
        tempDBValue *= scaleValue;
        tempDBValue = Math.round(tempDBValue);

        if (scale == 0) {
            dataFormat = new DecimalFormat("###");
            returnValue = dataFormat.format(tempDBValue);
        } else {
            String zeros = "";
            if (scale > 0) {
                for (int i = 1; i <= scale; i++) {
                    zeros = zeros + "0";
                }
                zeros = "." + zeros;
            } else {
//        		for(int i=-1;i>=scale;i--){
//            		zeros=zeros+"#";
//            	}
            }
            dataFormat = new DecimalFormat("###" + zeros);
//            dataFormat = new DecimalFormat("###.00");
            returnValue = dataFormat.format(tempDBValue / scaleValue);
        }
        if (Double.parseDouble(returnValue) < 1 &&
            Double.parseDouble(returnValue) > 0) {
            returnValue = "0" + returnValue;
        } else if (Double.parseDouble(returnValue) == 0) {
            returnValue = "0";
        }

        return returnValue;
    }
    /**
     * ��ֵ���ȴ���
     * @param v:��ֵ���ݵ��ַ���
     * @param scale:����
     * @return String
     */
    public static String round(String v, int scale) {
        try {
            if ("null".equals(v) || ("").equals(v) || v == null) {
                return null;
            }
            if (v.indexOf(',') >= 0) {
                v = deleteComma(v);
            }
            double dblv = Double.parseDouble(v);
            return round(dblv, scale);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * ����С��λ��
     * @param scale	С������
     * @param v 	Double���͵Ĳ���
     * @return ������ֵ���޵��ִ�
     */
    public static String ceil(double v, int scale) {
    	String returnValue;
    	DecimalFormat dataFormat;
    	int scaleValue;
    	if (scale <= 0) {
    		scaleValue = 1;
    	} else {
    		scaleValue = 10;
    	}
    	
    	double tempDBValue = v;
    	for (int i = 1; i < scale; i++) {
    		scaleValue *= 10;
    	}
    	for(int i=-1;i>=scale;i--){
    		scaleValue *= 0.1;
    	}
    	tempDBValue *= scaleValue;
    	tempDBValue = Math.ceil(tempDBValue);
    	
    	if (scale == 0) {
    		dataFormat = new DecimalFormat("###");
    		returnValue = dataFormat.format(tempDBValue);
    	} else {
    		String zeros = "";
    		if (scale > 0) {
    			for (int i = 1; i <= scale; i++) {
    				zeros = zeros + "0";
    			}
    			zeros = "." + zeros;
    		} else {
        		for(int i=-1;i>=scale;i--){
            		zeros=zeros+"#";
            	}
    		}
    		dataFormat = new DecimalFormat("###" + zeros);
//            dataFormat = new DecimalFormat("###.00");
    		returnValue = dataFormat.format(tempDBValue / scaleValue);
    	}
    	if (Double.parseDouble(returnValue) < 1 &&
    			Double.parseDouble(returnValue) > 0) {
    		returnValue = "0" + returnValue;
    	} else if (Double.parseDouble(returnValue) == 0) {
    		returnValue = "0";
    	}
    	
    	return returnValue;
    }
    /**
     * ��ֵ���ȴ���
     * @param v:��ֵ���ݵ��ַ���
     * @param scale:����
     * @return String
     */
    public static String ceil(String v, int scale) {
    	try {
    		if ("null".equals(v) || ("").equals(v) || v == null) {
    			return null;
    		}
    		if (v.indexOf(',') >= 0) {
    			v = deleteComma(v);
    		}
    		double dblv = Double.parseDouble(v);
    		return ceil(dblv, scale);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return null;
    }
    
    /**
     * ����С��λ��
     * @param scale	С������
     * @param v 	Double���͵Ĳ���
     * @return ������ֵ���޵��ִ�
     */
    public static String floor(double v, int scale) {
    	String returnValue;
    	DecimalFormat dataFormat;
    	double scaleValue;
    	if (scale <= 0) {
    		scaleValue = 1;
    	} else{
    		scaleValue = 10;
    	}
    	
    	double tempDBValue = v;
    	for (int i = 1; i < scale; i++) {
    		scaleValue *= 10;
    	}
    	for(int i=-1;i>=scale;i--){
    		scaleValue *= 0.1;
    	}
    	tempDBValue *= scaleValue;
    	tempDBValue = Math.floor(tempDBValue);
    	
    	if (scale == 0) {
    		dataFormat = new DecimalFormat("###");
    		returnValue = dataFormat.format(tempDBValue);
    	} else {
    		String zeros = "";
    		if (scale > 0) {
    			for (int i = 1; i <= scale; i++) {
    				zeros = zeros + "0";
    			}
    			zeros = "." + zeros;
    		} else {
        		for(int i=-1;i>=scale;i--){
            		zeros=zeros+"0";
            	}
    		}
    		dataFormat = new DecimalFormat("###" + zeros);
//            dataFormat = new DecimalFormat("###.00");
    		returnValue = dataFormat.format(tempDBValue / scaleValue);
    	}
    	if (Double.parseDouble(returnValue) < 1 &&
    			Double.parseDouble(returnValue) > 0) {
    		returnValue = "0" + returnValue;
    	} else if (Double.parseDouble(returnValue) == 0) {
    		returnValue = "0";
    	}
    	
    	return returnValue;
    }
    /**
     * ��ֵ���ȴ���
     * @param v:��ֵ���ݵ��ַ���
     * @param scale:����
     * @return String
     */
    public static String floor(String v, int scale) {
    	try {
    		if ("null".equals(v) || ("").equals(v) || v == null) {
    			return null;
    		}
    		if (v.indexOf(',') >= 0) {
    			v = deleteComma(v);
    		}
    		double dblv = Double.parseDouble(v);
    		return floor(dblv, scale);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return null;
    }
    

    /**
     * �����ַ����еĶ���
     * @param String Ҫ�����˵��ַ���
     * @return value
     */
    public static String deleteComma(String commastr) {
        String[] comma = commastr.split(",");
        String value = "";
        for (int ii = 0; ii < comma.length; ii++) {
            value += comma[ii];
        }
        return value;
    }


    /**
     * �����ݿ��ѯDouble���ʱ
     * С��0��ֵ��".###"��ʾ
     * Ϊ������������λ��"0"
     */
    public static String addZero(String v) {
		String zero = "0";
		try {
			if (v != null) {
				if (v.length() > 1) {
					v = v.trim();
					if (v.startsWith(".")) {
						v = zero + v;
					}
				}
			}
			return v;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    /**
     * ���һ��������������ֵ
     * @author liqing
     * @param datalist
     * @param column
     * @param ReturnType
     * @return
     */
    public static double max(ArrayList datalist,int column , int ReturnType){
    	double d=0;
        if(ReturnType==GDSetTool.Return_ArrayListRow){
	        if (!(datalist == null || datalist.size() == 0)) {
	            ArrayList list = (ArrayList) datalist.get(0);
	            if (list == null || list.size() == 0) {
	            } else { //��ʾ������
	                for (int i = 0; i < datalist.size(); i++) {
	                    if (((ArrayList) datalist.
	                         get(i)).get(column) != null) {
	                    	d=Math.max(Double.parseDouble(((ArrayList) datalist.
	                         get(i)).get(column).toString()), d);
	                        }
	                    }
	                }
	        }
        }else if(ReturnType==GDSetTool.Return_ArrayListRowWithTitle){
        	if (!(datalist == null || datalist.size() <= 1)) {
                ArrayList list = (ArrayList) datalist.get(0);
                if (list == null || list.size() == 0) {
                } else { //��ʾ������
                    for (int i = 1; i < datalist.size(); i++) {
                        if (((ArrayList) datalist.
                             get(i)).get(column) != null) {
                        	d=Math.max(Double.parseDouble(((ArrayList) datalist.
       	                         get(i)).get(column).toString()), d);
                        }
                    }
                }
            }
	    }else if(ReturnType==GDSetTool.Return_ArrayListCol){
	        if (!(datalist == null || datalist.size() == 0)) {
	            ArrayList list = (ArrayList) datalist.get(column);
	            if (list == null || list.size() == 0) {
	            } else { //��ʾ������
	                for (int i = 0; i < list.size(); i++) {
	                    if (list.get(i) != null) {
	                    	d=Math.max(Double.parseDouble(((ArrayList) datalist.
	   	                         get(column)).get(i).toString()), d);
	                    }
	                }
	            }
	        }
	    }else if(ReturnType==GDSetTool.Return_ArrayListColWithTitle){
	    	 if (!(datalist == null || datalist.size() == 0)) {
		            ArrayList list = (ArrayList) datalist.get(column);
		            if (list == null || list.size() == 0) {
		            } else { //��ʾ������
		                for (int i = 1; i < list.size(); i++) {
		                    if (list.get(i) != null) {
		                    	d=Math.max(Double.parseDouble(((ArrayList) datalist.
			   	                         get(column)).get(i).toString()), d);
		                    }
		                }
		            }
		        }
	    }
        return d;
    }
    /**
     * ���һ�������������Сֵ
     * @author liqing
     * @param datalist
     * @param column
     * @param ReturnType
     * @return
     */
    public static double min(ArrayList datalist,int column , int ReturnType){
    	double d=Double.MAX_VALUE;
    	if(ReturnType==GDSetTool.Return_ArrayListRow){
    		if (!(datalist == null || datalist.size() == 0)) {
    			ArrayList list = (ArrayList) datalist.get(0);
    			if (list == null || list.size() == 0) {
    			} else { //��ʾ������
    				for (int i = 0; i < datalist.size(); i++) {
    					if (((ArrayList) datalist.
    							get(i)).get(column) != null) {
    						d=Math.min(Double.parseDouble(((ArrayList) datalist.
    								get(i)).get(column).toString()), d);
    					}
    				}
    			}
    		}
    	}else if(ReturnType==GDSetTool.Return_ArrayListRowWithTitle){
    		if (!(datalist == null || datalist.size() <= 1)) {
    			ArrayList list = (ArrayList) datalist.get(0);
    			if (list == null || list.size() == 0) {
    			} else { //��ʾ������
    				for (int i = 1; i < datalist.size(); i++) {
    					if (((ArrayList) datalist.
    							get(i)).get(column) != null) {
    						d=Math.min(Double.parseDouble(((ArrayList) datalist.
    								get(i)).get(column).toString()), d);
    					}
    				}
    			}
    		}
    	}else if(ReturnType==GDSetTool.Return_ArrayListCol){
    		if (!(datalist == null || datalist.size() == 0)) {
    			ArrayList list = (ArrayList) datalist.get(column);
    			if (list == null || list.size() == 0) {
    			} else { //��ʾ������
    				for (int i = 0; i < list.size(); i++) {
    					if (list.get(i) != null) {
    						d=Math.min(Double.parseDouble(((ArrayList) datalist.
    								get(column)).get(i).toString()), d);
    					}
    				}
    			}
    		}
    	}else if(ReturnType==GDSetTool.Return_ArrayListColWithTitle){
    		if (!(datalist == null || datalist.size() == 0)) {
    			ArrayList list = (ArrayList) datalist.get(column);
    			if (list == null || list.size() == 0) {
    			} else { //��ʾ������
    				for (int i = 1; i < list.size(); i++) {
    					if (list.get(i) != null) {
    						d=Math.min(Double.parseDouble(((ArrayList) datalist.
    								get(column)).get(i).toString()), d);
    					}
    				}
    			}
    		}
    	}
    	return d;
    }
    /**
     * ���GDSet����ĳһ�е����ֵ
     * @author liqing
     * @param gd
     * @param column
     * @param ReturnType
     * @return
     */
    public static double max(GDSet gd,int column,int ReturnType){
    	double d=0;
    	try {
    		if(ReturnType==GDSetTool.Return_ArrayListCol||ReturnType==GDSetTool.Return_ArrayListRow){
				for (int i = 0; i < gd.getRowCount(); i++) {
					d=Math.max(Double.parseDouble(gd.getCell(i, column)), d);
				}
				if(gd.getRowCount()==0) d=100;
    		}else if(ReturnType==GDSetTool.Return_ArrayListColWithTitle||ReturnType==GDSetTool.Return_ArrayListRowWithTitle){
    			for (int i = 1; i < gd.getRowCount(); i++) {
    				d=Math.max(Double.parseDouble(gd.getCell(i, column)), d);
    			}
    			if(gd.getRowCount()==1) d=100;
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return d;
    }
    /**
     * ���GDSet����ĳһ�е���Сֵ
     * @author liqing
     * @param gd
     * @param column
     * @param ReturnType
     * @return
     */
    public static double min(GDSet gd,int column,int ReturnType){
    	double d=Double.MAX_VALUE;
    	try {
    		if(ReturnType==GDSetTool.Return_ArrayListCol||ReturnType==GDSetTool.Return_ArrayListRow){
    			for (int i = 0; i < gd.getRowCount(); i++) {
    				d=Math.min(Double.parseDouble(gd.getCell(i, column)), d);
    			}
    			if(gd.getRowCount()==0) d=0;
    		}else if(ReturnType==GDSetTool.Return_ArrayListColWithTitle||ReturnType==GDSetTool.Return_ArrayListRowWithTitle){
    			for (int i = 1; i < gd.getRowCount(); i++) {
    				d=Math.min(Double.parseDouble(gd.getCell(i, column)), d);
    			}
    			if(gd.getRowCount()==1) d=0;
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return d;
    }
}
