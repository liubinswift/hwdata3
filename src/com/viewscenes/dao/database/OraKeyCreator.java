package com.viewscenes.dao.database;



import java.sql.*;

import java.util.*;



/**

 * ����oracle����������

 * Ӧ��oracle��������������

 */



public class OraKeyCreator

        implements KeyCreator {

    String m = "OraKeyCreator:";



    /**

     * ���ݱ����õ�����

     * @param tab ����

     * @return ��1�������,���ʧ�ܷ���-1

     * @throws DbException

     */

    public String getKey(String tab,String dbName) throws DbException {

        String msg = m + "getKey";



        try {

            List l = getSequence(tab, 1,dbName);

            if (l != null && l.isEmpty() == false) {

                return (String) l.get(0);

            }

        } catch (DbException ex) {

            throw ex;

        } catch (Exception ex) {

            throw new DbException(msg, ex);

        }

        return "-1";

    }



    /**

     * �õ��������

     * @param tab ����

     * @param num һ��ȡ�����������Ŀ

     * @return ����ֵ�б�

     * @throws DbException

     */

    private List getSequence(String tab, long num,String dbName) throws DbException {

        String msg = m + "getSequence: ";

        DbComponent db = new DbComponent(dbName);

        Connection con = null;

        String sql = null;

        Statement stmt = null;

        ResultSet rs = null;

        long key = -1;

        ArrayList l = new ArrayList();



        try {

            //�õ����ݿ�����Ӻʹ���Statement

            con = db.getDB();

            stmt = con.createStatement();

            sql = getSeqSql(tab);



            while (num-- > 0) {

                //ÿ��ִ��resultsetǰ�ȹر�

                if (rs != null) {

                    rs.close();

                }

                //�õ��������ֵ

                rs = stmt.executeQuery(sql);

                if (rs.next()) {

                    key = rs.getLong("s");

                    l.add(String.valueOf(key));

                }

            }

        } catch (SQLException ex) {

            throw new DbException(msg + sql, ex);

        } finally {

            db.closeDB(con, stmt, rs);

        }

        return l;

    }



    /**

     * ���ݱ����õ���Ӧ������ֵ

     * @param tab

     * @return

     */

    private String getSeqSql(String tab) {

        //����Ĳ����Ǳ�������Ӧ�����������Ǳ���ȥ��_tab�ټ���_seq.



        String name = tab.toLowerCase().substring(0,

                (tab.length() - 4));

        return "select " + name + "_seq.nextval s from DUAL";



    }



    /**

     * ����ʱ���ʽ��ֻ������ORACLE)

     * @param con

     * @throws SQLException

     */

    public void alterSession(Connection con) throws SQLException {

        try {

            Statement stmt = con.createStatement();

            String sql =

                    "ALTER SESSION SET NLS_DATE_FORMAT = 'YYYY-MM-DD HH24:MI:SS'";

            stmt.execute(sql);

            if (stmt != null) {

                stmt.close();

            }

        } catch (SQLException e) {

            throw new SQLException("OraKeyCreator:alterSession::Exception " + e.toString());

        }



    }



}
