package com.viewscenes.dao.innerdao;



/**

 * sql������

 * <p>Title: </p>

 * <p>Description: </p>

 * <p>Copyright: Copyright (c) 2003</p>

 * <p>Company: </p>

 * @author not attributable

 * @version 1.0

 */

public class SqlObject {

    private StringBuffer _sql = new StringBuffer();//����

    private String group_sql = "";//group by�Ӿ�

    private String order_sql = "";//order by�Ӿ�



    /**

     * ����������sql����

     * @return

     */

    public String toString() {

        _sql.append(" ").append(group_sql).append(order_sql);

        return _sql.toString().trim();

    }



    /**

     * ����group by�Ӿ�

     * @param group_sql

     */

    public void setGroup_sql(String group_sql) {

        this.group_sql = group_sql;

    }



    /**

     * ����order by�Ӿ�

     * @param order_sql

     */

    public void setOrder_sql(String order_sql) {

        if (this.order_sql.equals("")) {

            this.order_sql = " order by ";

        } else {

            this.order_sql += ",";

        }

        this.order_sql += " " + order_sql;

    }



    /**

     * ����sql����

     * ��������ǵ�һ��������˵�and��or,������ǣ���

     * @param sql

     */

    public void setSql(String sql) {

        if(_sql.length() == 0)

            _sql.append(Common.FilStartOper(sql));

        else

            _sql.append(sql);

    }



}
