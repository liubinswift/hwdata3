package com.viewscenes.dao.innerdao;



/**

 * ��������

 * �����������ֶ�ֵ�����ͣ���������ֵ����ǰһ�������Ĺ�ϵ

 * <p>Title: </p>

 * <p>Description: </p>

 * <p>Copyright: Copyright (c) 2003</p>

 * <p>Company: </p>

 * @author not attributable

 * @version 1.0

 */

public class Condition {



    private String column;//�ֶ�ֵ

    private String type;//����

    private String oper;//������

    private String value;//ֵ

    private String between;//��ǰһ�������Ĺ�ϵ



    public String getBetween() {

        return between;

    }



    public String getColumn() {

        return column;

    }



    public String getOper() {

        return oper;

    }



    public String getType() {

        return type;

    }



    public String getValue() {

        return value;

    }



    public void setBetween(String between) {

        this.between = between;

    }



    public void setColumn(String column) {

        this.column = column;

    }



    public void setOper(String oper) {

        this.oper = oper;

    }



    public void setType(String type) {

        this.type = type;

    }



    public void setValue(String value) {

        this.value = value;

    }



}
