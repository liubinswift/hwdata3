package com.viewscenes.dao.database;



import java.sql.*;



/**

 * ���ݿ������������ӿ�

 */

public interface KeyCreator {



    /**

     * �õ�����

     * @return String

     */

    public String getKey(String tab,String dbName) throws DbException;



    /**

     * ����ʱ���ʽ��ֻ������ORACLE)

     * @param con

     * @throws SQLException

     */

    public void alterSession(Connection con) throws SQLException;



}

