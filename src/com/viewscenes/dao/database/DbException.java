package com.viewscenes.dao.database;



import com.viewscenes.pub.exception.*;



/**

 * ���ݿ��쳣

 */



public class DbException

        extends AppException {

    public DbException(String message) {

        super(message);

    }



    public DbException(String message, Throwable cause) {

        super(message, cause);

    }



}
