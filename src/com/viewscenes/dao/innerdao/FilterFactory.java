package com.viewscenes.dao.innerdao;



import com.viewscenes.dao.*;

import org.jdom.*;

import com.viewscenes.pub.*;

import com.viewscenes.dao.database.*;



public class FilterFactory {



    /**

     * �õ�һ��Filter��ʵ��

     * @param field

     * @param condition

     * @return

     * @throws DbException

     */

    public static ConBaseFilter create(String field, GDSet condition) throws DbException {

        String tab = condition.getTableName();

        if (IsLogicTab(tab))

            return new ConLogicFilter(field, condition);



        return new ConBaseFilter(field, condition);

    }



    /**

     * �Ƿ��߼���

     * @param tab

     * @return

     * true:���߼���

     * false:�����

     */

    private static boolean IsLogicTab(String tab) {

        Element _ele = XmlReader.getItem("logicconfig", "table", tab);

        //�����

        if (_ele == null)

            return false;



        return true;

    }

}
