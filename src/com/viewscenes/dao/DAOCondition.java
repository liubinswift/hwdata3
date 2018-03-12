package com.viewscenes.dao;



import com.viewscenes.pub.*;

import com.viewscenes.util.*;



public class DAOCondition

        extends GDSet {

    public static final String _NUMBER = "NUMBER";

    public static final String _VARCHAR = "VARCHAR";

    public static final String _DATE = "DATE";



    static String field[] = {

        "field", "type", "operator", "value", "between"};



    public DAOCondition(String tab) throws GDSetException {

        super(tab, field);

    }



    /**

     * ����һ������

     * @param field ����

     * @param type  ����

     * @param operator ������

     * @param value ֵ

     * @param between ��ǰһ�����Ĺ�ϵ(֧��and,or)

     * @throws GDSetException

     */

    public void addCondition(String field, String type, String operator, String value, String between) throws

            GDSetException {

        String[] row = {

            field, type, operator, value, between};

        this.addRow(row);

    }



    /**

     * ����һ������(��ǰһ�����Ĺ�ϵĬ��Ϊand)

     * @param field ����

     * @param type  ����

     * @param operator ������

     * @param value ֵ

     * @throws GDSetException

     */

    public void addCondition(String field, String type, String operator, String value) throws

            GDSetException {

        String[] row = {

            field, type, operator, value, "and"};

        this.addRow(row);

    }

}

