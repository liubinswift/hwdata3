package com.viewscenes.dao.database;



/**

 *  ��������������

 *  ��ʱֻ����ORACLE������������

 */



public class keyFactory {

    public static final short ORA_KEY = 0;



    public KeyCreator create(short name) {

        if (name == ORA_KEY) {

            return new OraKeyCreator();

        }



        return null;

    }



}
