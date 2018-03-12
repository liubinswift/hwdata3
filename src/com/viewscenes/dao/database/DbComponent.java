package com.viewscenes.dao.database;



import java.sql.*;




import com.viewscenes.dao.pool.*;

import com.viewscenes.pub.*;

import com.viewscenes.util.*;
import com.viewscenes.dao.innerdao.SQLGenerator;


/**
 * ���ݿ�ײ�ӿ�
 *
 * ������ݿ�Ĵ򿪣��رգ���������ݲ�ѯ����ɾ�ģ��������ɵĹ���
 */

public class DbComponent {

    final static String method = "DBComponent:";

    final static int _INSERT = 0;

    final static int _UPDATE = 1;

    //����

  static long _key;



    static String dbName;

    private String DB_DRIVER ;

    private String DB_URL;

    private String DB_USER ;

    private String DB_PASSWORD ;

    public DbComponent(String dbName){
      this.dbName = dbName;
    }
    public DbComponent(){

    }
    /**
     * ִ��sql(����insert,update,delete)
     * @param sql
     * @return �ɹ����
     * @throws DbException
     */
    public static boolean exeUpdate(String sql) throws DbException {
        String msg = method + "exeUpdate  ";

        //assert (sql != null && sql.compareTo("") != 0):msg + "sqlΪ��";
        Connection con = null;

        Statement st = null;

        try {

            con = getDB();

            st = con.createStatement();

            LogTool.debug(sql);

            if (st.executeUpdate(sql) > 0) {

                return true;

            } else {

                return false;

            }

        } catch (SQLException ex) {

            try{

                con.rollback();

            }catch(Exception ex1){

                ex1.printStackTrace();

            }

            System.out.println(sql);

            throw new DbException(msg + sql, ex);

        } finally {

            closeDB(con, st);

        }
    }

    public static boolean exeUpdate(String sql,String dbName) throws DbException {
        String msg = method + "exeUpdate  ";

        //assert (sql != null && sql.compareTo("") != 0):msg + "sqlΪ��";
        Connection con = null;

        Statement st = null;

        try {

            con = getDB(dbName);

            st = con.createStatement();

            LogTool.debug(sql);

            if (st.executeUpdate(sql) > 0) {

                return true;

            } else {

                return false;

            }

        } catch (SQLException ex) {

            try{

                con.rollback();

            }catch(Exception ex1){

                ex1.printStackTrace();

            }

            System.out.println(sql);

            throw new DbException(msg + sql, ex);

        } finally {

            closeDB(con, st);

        }
    }




    public static GDSet Query(String sql) throws DbException {

        String msg = method + "Query  ";

        Connection con = null;

        Statement st = null;

        ResultSet rs = null;


        try {

          con = getDB();

          st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                  ResultSet.CONCUR_READ_ONLY);
    
          LogTool.debug(sql);
          rs = st.executeQuery(sql);

          GDSet gd = new GDSet("");
          GDSetTool.addResultSet(gd, rs);

          return gd;

        } catch (GDSetException ex) {
            throw new DbException(msg + "GDSet��addResultSet����", ex);
        }
          catch (SQLException ex) {
            throw new DbException(msg + sql, ex);
        } finally {
            closeDB(con, st, rs);
            
        }
    }

    public static GDSet Query(String sql,String dbName) throws DbException {

        String msg = method + "Query  ";

        Connection con = null;

        Statement st = null;

        ResultSet rs = null;


        try {

          con = getDB(dbName);

          st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                  ResultSet.CONCUR_READ_ONLY);

          LogTool.debug(sql);
          rs = st.executeQuery(sql);

          GDSet gd = new GDSet("");
          GDSetTool.addResultSet(gd, rs);

          return gd;

        } catch (GDSetException ex) {
            throw new DbException(msg + "GDSet��addResultSet����", ex);
        }
          catch (SQLException ex) {
            throw new DbException(msg + sql, ex);
        } finally {
            closeDB(con, st, rs);
        }
    }
    public static GDSet Query(String sql,int ReturnType) throws DbException {
    	
    	String msg = method + "Query  ";
    	
    	Connection con = null;
    	
    	Statement st = null;
    	
    	ResultSet rs = null;
    	
    	
    	try {
    		
    		con = getDB();
    		
    		st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
    				ResultSet.CONCUR_READ_ONLY);
    		
    		LogTool.debug(sql);
    		rs = st.executeQuery(sql);
    		
    		GDSet gd = new GDSet("");
    		GDSetTool.addResultSet(gd, rs, ReturnType);
    		
    		return gd;
    		
    	} catch (GDSetException ex) {
    		throw new DbException(msg + "GDSet��addResultSet����", ex);
    	}
    	catch (SQLException ex) {
    		throw new DbException(msg + sql, ex);
    	} finally {
    		closeDB(con, st, rs);
    	}
    }
    
    public static GDSet Query(String sql,String dbName,int ReturnType) throws DbException {
    	
    	String msg = method + "Query  ";
    	
    	Connection con = null;
    	
    	Statement st = null;
    	
    	ResultSet rs = null;
    	
    	
    	try {
    		
    		con = getDB(dbName);
    		
    		st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
    				ResultSet.CONCUR_READ_ONLY);
    		
    		LogTool.debug(sql);
    		rs = st.executeQuery(sql);
    		
    		GDSet gd = new GDSet("");
    		GDSetTool.addResultSet(gd, rs, ReturnType);
    		
    		return gd;
    		
    	} catch (GDSetException ex) {
    		throw new DbException(msg + "GDSet��addResultSet����", ex);
    	}
    	catch (SQLException ex) {
    		throw new DbException(msg + sql, ex);
    	} finally {
    		closeDB(con, st, rs);
    	}
    }




    /**

     * ִ�в�ѯ��֧��absolute)

     * @param sql

     * @return ResultSet

     */

    public ResultSet exeQuery(String sql) throws DbException {

        String msg = method + "exeQuery  ";

        //assert (sql != null && sql.compareTo("") != 0):msg + "sqlΪ��";

        Connection con = null;

        Statement st = null;

        ResultSet rs = null;



        try {

          //  CachedRowSet rowSet = new CachedRowSet();

            con = getDB();

            //��ѯ�������

            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,

                    ResultSet.CONCUR_READ_ONLY);

            LogTool.debug(sql);

            rs = st.executeQuery(sql);

           // rowSet.populate(rs);

            return rs;



        } catch (SQLException ex) {

            throw new DbException(msg + sql, ex);

        } finally {

            closeDB(con, st, rs);

        }

    }



    /**

     * �����������sql��䣨����INSERT,DELETE,UPDATE)

     * @param s

     * @return

     * @throws DbException

     */

    public static int[] exeBatch(String[] s) throws DbException {

        String msg = method + "exeBatch  ";

        //assert s != null:msg + "����sqlΪ��";



        Connection _con = null;

        Statement stmt = null;

        try {

            //�õ����ݿ�����Ӻʹ���Statement

            _con = getDB();

            _con.setAutoCommit(false);



            stmt = _con.createStatement();

            //�õ�����Sql���

            for (int i = 0; i < s.length; i++) {

                LogTool.debug(s[i]);
                if(s[i]!=null&&s[i].length()!=0)
                {

                    stmt.addBatch(s[i]);
                }


            }

            //ִ��������

            int[] ret = stmt.executeBatch();

            _con.commit();

            _con.setAutoCommit(true); //add by zhaoyahui 2009/12/24

            return ret;

        } catch (Exception ex) {

            try {

                _con.rollback();

            } catch (Exception ex1) {

                ex1.printStackTrace();

            }

            for (int i = 0; i < s.length; i++) {

                System.out.println(s[i]);

            }

            throw new DbException(msg, ex);

        } finally {

            try {

            	if(_con!=null){ //add by zhaoyahui 2009/12/24
            		_con.setAutoCommit(true);
            	}

                closeDB(_con, stmt);

            } catch (Exception ex) {

                ex.printStackTrace();

            }



        }

    }

    public static int[] exeBatch(String[] s,String dbName) throws DbException {

        String msg = method + "exeBatch  ";

        //assert s != null:msg + "����sqlΪ��";



        Connection _con = null;

        Statement stmt = null;

        try {

            //�õ����ݿ�����Ӻʹ���Statement

            _con = getDB(dbName);

            _con.setAutoCommit(false);



            stmt = _con.createStatement();

            //�õ�����Sql���

            for (int i = 0; i < s.length; i++) {

                LogTool.debug(s[i]);

                stmt.addBatch(s[i]);

            }

            //ִ��������

            int[] ret = stmt.executeBatch();

            _con.commit();

            return ret;

        } catch (Exception ex) {

            try {

                _con.rollback();

            } catch (Exception ex1) {

                ex1.printStackTrace();

            }

            for (int i = 0; i < s.length; i++) {

                System.out.println(s[i]);

            }

            throw new DbException(msg, ex);

        } finally {

            try {

//                _con.setAutoCommit(true);

                closeDB(_con, stmt);

            } catch (Exception ex) {

                ex.printStackTrace();

            }



        }

    }




    /**

     * �õ�����

     * @param tab ����

     * @return

     * @throws DbException

     */

    public static String getKey(String tab) throws DbException {

        keyFactory fac = new keyFactory();

        KeyCreator key = fac.create(keyFactory.ORA_KEY);

        try {

            return key.getKey(tab,dbName);

        } catch (DbException ex) {

            throw ex;

        }

    }



    /**

     * ���ļ�

     */

    private void ReadConFile() {

        DB_DRIVER = com.viewscenes.dao.XmlReader.getAttrValue("dbconfig", "para", "driver");

        DB_URL = com.viewscenes.dao.XmlReader.getAttrValue("dbconfig", "para", "url");

        DB_USER = com.viewscenes.dao.XmlReader.getAttrValue("dbconfig", "para", "user");

        DB_PASSWORD = com.viewscenes.dao.XmlReader.getAttrValue("dbconfig", "para", "password");

    }



    /**

     * �õ���������(�������ļ�dbconfig.property�ж�DBPool���ԣ�1��ʹ��POOL;0:ֱ������

     * @return Connection ����

     */
/*
    synchronized Connection getDB() throws DbException {

        String msg = method + "getDB:";

        Connection con = null;

        try {

            String pool = com.viewscenes.dao.XmlReader.getAttrValue("dbconfig", "para", "dbpool");

            //ʹ�����ӳ�

            if (pool.equals("1")) {

                con = DatabaseManager.getConnection();

            } else { //ֱ���������ݿ�

                ReadConFile();

                Class.forName(DB_DRIVER);

                con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

                alterSession(con);

            }

        } catch (ClassNotFoundException ex) {

            throw new DbException(msg + "û���ҵ����ݿ�����" + DB_DRIVER, ex);

        } catch (Exception ex) {

            throw new DbException(msg + "δ֪����", ex);

        }



        return con;

    }
*/
    /**

     * �õ���������(�������ļ�dbconfig.property�ж�DBPool���ԣ�1��ʹ��POOL;0:ֱ������

     * @return Connection ����

     */

    public static Connection getDB() throws DbException {

        String msg = method + "getDB:";

        Connection con = null;
      Throwable t = new Throwable();
      StackTraceElement[] elements = t.getStackTrace();
      if (elements.length>=2){
        String sClass = elements[2].getClassName();
        int lineNum = elements[2].getLineNumber();
        String sMethod = elements[2].getMethodName();
      }

        try {
          if (dbName!=null)
                con = DatabaseManager.getConnection(dbName);
          else
                con = DatabaseManager.getConnection();

        } catch (Exception ex) {
            throw new DbException(msg + "���ݿ����Ӵ���:"+ex.getMessage(), ex);
        }
        return con;
    }

    synchronized static Connection getDB(String dbName) throws DbException {

        String msg = method + "getDB:";

        Connection con = null;
      Throwable t = new Throwable();
      StackTraceElement[] elements = t.getStackTrace();
      if (elements.length>=2){
        String sClass = elements[2].getClassName();
        int lineNum = elements[2].getLineNumber();
        String sMethod = elements[2].getMethodName();
      }

        try {
                con = DatabaseManager.getConnection(dbName);

        } catch (Exception ex) {
            throw new DbException(msg + "���ݿ����Ӵ���:"+ex.getMessage(), ex);
        }
        return con;
    }



    /**

     * �ر��������ӣ�Connection,Statement)

     * @param con

     * @param stmt

     * @throws DbException

     */



    static void closeDB(Connection con, Statement stmt) throws DbException {

        String msg = method + "closeDB(con,stmt):";



        try {

            if (stmt != null) {

                stmt.close();

            }

        } catch (SQLException ex) {

            throw new DbException(msg + "statement close error", ex);

        }



        try {

            if (con != null) {

                con.close();

            }

        } catch (SQLException ex) {

            throw new DbException(msg + "connection close error", ex);

        }



    }



    /**

     * �ر��������ӣ�Connection,Statement,ResultSet)

     * @param con

     * @param stmt

     * @param rs

     * @throws DbException

     */

   static void closeDB(Connection con, Statement stmt, ResultSet rs) throws

            DbException {

        String msg = method + "closeDB(con,stmt,rs):";

        try {

            if (rs != null) {

                rs.close();

            }

        } catch (SQLException ex) {

            throw new DbException(msg + "resultset close error", ex);

        }



        try {

            if (stmt != null) {

                stmt.close();

            }

        } catch (SQLException ex) {

            throw new DbException(msg + "statement close error", ex);

        }



        try {

            if (con != null) {

                con.close();

            }

        } catch (SQLException ex) {

            throw new DbException(msg + "connection close error", ex);

        }

    }



    /**

     * �õ�������ķ���

     * ��ΪexeBatch���ص���int[],����Ҫת����boolean

     * @param a

     * @return

     */

    private boolean getBatReturn(int a[], String[] s) {

        for (int i = 0; i < a.length; i++) {

            if (a[i] <= 0) {

                System.out.println("sql��� :" + s[i] + "ִ�в��ɹ�");

                return false;

            }

        }

        return true;

    }



    private void alterSession(Connection con) {

        try {

            keyFactory fac = new keyFactory();

            KeyCreator key = fac.create(keyFactory.ORA_KEY);

            key.alterSession(con);

        } catch (SQLException ex) {

        }

    }


    /**

   * ���루�Զ����������ģ�֧�ֶ��в��룩���õ�һ������key

   * @param data

   * <pre>

   *     data����Ϊnull���

   *     ���뱣֤data.getRow(0)��data.getTableName()��Ϊ��

   * </pre>

   * @param key[OUT] ����

   * key�ĸ��������data������Ҫһ��

   * return

   * ���������

   */

  public static int[] Insert(GDSet data, long[] key) throws DbException {

    return Insert(data, key, true);

  }

  /**

   * ����(������������֧�ֶ���)

   * @param data

   * <pre>

   *     data����Ϊnull���

   *     ���뱣֤data.getRow(0)��data.getTableName()��Ϊ��

   * </pre>

   * return

   * ���������

   */

  public static int[] Insert(GDSet data) throws DbException {

    return Insert(data, null, false);

  }



  /**

   * ����

   * @param data

   * @param key ���ص�����

   * @param autoCreate �Ƿ��ò�����������

   * <pre>

   *     true:������������

   *     false:����Ҫ��������

   * </pre>

   * @return

   * @throws DbException

   */



  private static int[] Insert(GDSet data, long[] key, boolean autoCreate) throws

      DbException {

//    DbComponent db = new DbComponent(dbName);



    //�õ��������������

    String[] sql = getSqlArray(data, _INSERT, autoCreate, key);

    //ִ��

    int[] ret = exeBatch(sql);



//    //����ɹ���ˢ�»���
//
//    if (IsDirty(ret)) {
//
//      setDirty(data.getTableName());
//
//    }

    return ret;

  }

  /**

   * ���£�֧�ֶ�����¼���£��������ֵʧ�ܣ�����������ع���

   * @param data

   * <pre>

   * ���µ����ݣ�������Ϊ������������������ݱ������������

   *     data����Ϊnull���

   *     ���뱣֤data.getRow(0)��data.getTableName()��Ϊ��

   * </pre>

   * @return

   * ���µ�����

   * @throws DbException

   */

  public static int[] Update(GDSet data) throws DbException {

//    DbComponent db = new DbComponent(dbName);

    //�õ��������������

    String[] sql = getSqlArray(data, _UPDATE, false, null);

    //ִ��

    int[] ret = exeBatch(sql);

    return ret;

  }

  /**

   * �����������£�֧�ֶ�����¼���£��������ֵʧ�ܣ�����������ع���

   * @param data����������������

   * <pre>

   * ���µ�����

   *     data����Ϊnull���

   *     ���뱣֤data.getRow(0)��data.getTableName()��Ϊ��

   * </pre>

   * @param con

   * ��ʾ���µ�����

   * ֻ��Ҫ�趨column�����Լ���

   * @return

   * ���µ�����

   * @throws DbException

   */

  public static int[] UpdateX(GDSet data, GDSet con) throws DbException {

//    DbComponent db = new DbComponent(dbName);

    SQLGenerator sg = new SQLGenerator();

    String sql[] = new String[data.getRowCount()];



    int[] ret = null;

    try {

      for (int i = 0; i < data.getRowCount(); i++) {

        //�õ�sql

        GDSet sub = data.getSubGDSet(i, i + 1);

        sql[i] = sg.generateUpdateXSQL(data, con);

      }

      //ִ��

      ret = exeBatch(sql);

    }

    catch (GDSetException ex) {

      throw new DbException("DAOComponent UpdateX:GDSet����"+ex);

    }



    return ret;

  }

  /**

   * ������������

   * @param content ���µ����ݣ�����������������

   * @param con ���µ�����

   * @return

   * @throws DbException

   */

  public static int[] UpdatebyCon(GDSet content, GDSet con) throws DbException {

//    DbComponent db = new DbComponent(dbName);

    SQLGenerator sg = new SQLGenerator();

    String sql[] = new String[1];

    int[] ret = null;



    //�õ�sql

    sql[0] = sg.generateUpdatebyConSQL(content, con);

    //ִ��

    ret = exeBatch(sql);

    return ret;

  }




  /**

   * ����data�õ�һ��sql���

   * @param data

   * @param keyCreate �����Ƿ��Զ�����

   * true:�Զ�����

   * false:���Զ�����

   * @param type sql�������

   * @return

   */

  private static String[] getSqlArray(GDSet data, int type, boolean keyCreate, long[] key) throws

      DbException {

    String s[] = new String[data.getRowCount()];



    try {

      for (int i = 0; i < data.getRowCount(); i++) {

        //�õ�sql

        GDSet sub = data.getSubGDSet(i, i + 1);

        s[i] = getSql(sub, type, keyCreate);

        //����ǲ�����䲢���������Զ����ɣ����¼����ֵ

        if (type == _INSERT && key != null) {

          key[i] = _key;

        }

      }

    }

    catch (GDSetException ex) {

      throw new DbException("DAOComponent getSqlArray:GDSet����"+ex);

    }



    return s;

  }

  /**

   * ���ݵ�����¼��data�õ�һ��sql

   * @param data

   * @param type ������ͣ�_INSERT��_UPDATE��_DELETE)

   * @param keyCreate

   * ֻ��type = _INSERT��Ч����ʾ�Ƿ���Ҫ������������

   * @return

   * @throws DbException

   */

  private static String getSql(GDSet data, int type, boolean keyCreate) throws DbException {

    String sql = null;



    SQLGenerator sg = new SQLGenerator();

    if (type == _INSERT) {

      sql = sg.generateInsertSQL(data, keyCreate);

      _key = Long.parseLong(sg.getKey());

    }

    if (type == _UPDATE) {

      sql = sg.generateUpdateSQL(data);

    }

    return sql;

  }


  public class DbQuickExeSQL {

         public DbQuickExeSQL(String sql) {
             this.sql = sql;
             System.out.println(sql);
         }

         private PreparedStatement ps = null;
         private Connection cn = null;
         private String sql = "";

         public boolean getConnect() {
             try {
                 cn = getDB();
                 ps = cn.prepareStatement(sql);
                 cn.setAutoCommit(true);
             } catch (Exception ex) {
                 closeConnect();
                 return false;
             }
             return true;
         }

         public boolean closeConnect() {
             try {
                 if (ps != null) {
                     ps.close();
                 }
                 if (cn != null) {
                     cn.close();
                 }
             } catch (SQLException ex) {
                 return false;
             }
             return true;
         }

         public boolean exeSQL() throws Exception{
             //try {
                 ps.execute();
             //} catch (SQLException ex) {
             //    ex.printStackTrace();
                 return false;
             //}
            // return true;
         }

         public ResultSet exeQuery() {
        	 ResultSet rs = null;
             try {
            	 rs = ps.executeQuery();
             } catch (SQLException ex) {
                 return null;
             }
             return rs;
         }

         public void setInt(int index, int intValue) {
             try {
                 ps.setInt(index, intValue);
             } catch (SQLException ex) {
             }
         }

         public void setString(int index, String stringValue) {
             try {
                 ps.setString(index, stringValue);
             } catch (SQLException ex) {
             }
         }

         public void setDate(int index, java.sql.Date dateValue) {
             try {
                 ps.setDate(index, dateValue);

             } catch (SQLException ex) {
             }
         }

         public void setBoolean(int index, boolean booleanValue) {
             try {
                 ps.setBoolean(index, booleanValue);
             } catch (SQLException ex) {
             }

         }

         public void setTime(int index, java.sql.Time timeValue) {
             try {
                 ps.setTime(index, timeValue);
             } catch (SQLException ex) {
             }
         }

         public void setDouble(int index, double doubleValue) {
             try {
                 ps.setDouble(index, doubleValue);
             } catch (SQLException ex) {
             }
         }

         public void setFloat(int index, float floatValue) {
             try {
                 ps.setFloat(index, floatValue);
             } catch (SQLException ex) {
             }

         }

         public void setLong(int index, long longValue) {
             try {
                 ps.setLong(index, longValue);
             } catch (SQLException ex) {
             }
         }

         public void setNull(int index, int typeValue) {
             try {
                 ps.setNull(index, typeValue);
             } catch (SQLException ex) {
             }
         }

         public void setTimestamp(int index, java.sql.Timestamp timestampValue) {
             try {
                 ps.setTimestamp(index, timestampValue);

             } catch (SQLException ex) {
             }
         }
     }



}
