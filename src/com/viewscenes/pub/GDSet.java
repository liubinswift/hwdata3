package com.viewscenes.pub;



import java.sql.*;

import java.util.*;

import com.viewscenes.util.StringTool;


/**

 *

 * <p>Title: ͨ�����ݶ�����</p>

 * <p>Description: ͨ�����ݶ����װ��һ����ά���ַ���������ͨ��GDSetTool�ཫResultSetֱ�����õ�GDSet������</p>

 * <p>Company: Novel_Tongfang</p>

 * @version 1.0

 * @see Column GDSetTool

 */





public class GDSet implements java.io.Serializable{

  public final static String DELIM = "$";

  public final static char CODECHAR = '#';

  private int columnCount = 0;

  private String tableName = null;

  private HashMap columnNameToIndex = new HashMap();

  private Column[] columns = null;

  private ArrayList rowList = null;

  private ArrayList colList = null;

  protected GDSet() {

  }

  /**
   * ���ݱ����ƴ���GDSet���󣬿���ʹ��addColumn���������
   * @param tableName ������
   */

  public GDSet(String tableName) {

    this.tableName = tableName;

    rowList = new ArrayList();
    colList = new ArrayList();

  }

  /**
   * ���ݱ����ƺ��ֶ����ƴ���GDSet���ֶ�����ʹ���ַ�����������
   * @param tableName ������
   * @param columnNames ���е�������
   * @throws GDSetException ���������Ϊnull,���������ظ����׳��쳣
   */

  public GDSet(String tableName, String[] columnNames) throws GDSetException {

    this.columnCount = columnNames.length;

    this.tableName = tableName;

    columns = new Column[columnCount];

    for (int i = 0; i < columnNames.length; i++) {

      if (columnNames[i] == null) {

        throw new GDSetException("Column name can not be null!");

      }

      if (columnNameToIndex.get(columnNames[i].toLowerCase()) != null) {

        throw new GDSetException("Duplicated column name!");

      }

      columnNameToIndex.put(columnNames[i].toLowerCase(), new Integer(i));

      columns[i] = new Column();

      columns[i].setColumnName(columnNames[i]);

      columns[i].setColumnLabel(columnNames[i]);

      columns[i].setColumnType(Column.COLUMN_TYPE_STRING);

    }

    rowList = new ArrayList();
    colList = new ArrayList();

  }

  /**
   * ���ݱ����ƺ��ֶ����ƴ���GDSet���ֶ�����ʹ���ַ����������롣
   * ���������ɸ�����
   * @param tableName ������
   * @param columnNames ���е�������
   * @param rowCount ������ٿ���
   * @throws GDSetException ���������Ϊnull,���������ظ����׳��쳣
   */

  public GDSet(String tableName, String[] columnNames, int rowCount) throws

      GDSetException {

    this.columnCount = columnNames.length;

    this.tableName = tableName;

    columns = new Column[columnCount];

    for (int i = 0; i < columnNames.length; i++) {

      if (columnNames[i] == null) {

        throw new GDSetException("Column name can not be null!");

      }

      if (columnNameToIndex.get(columnNames[i].toLowerCase()) != null) {

        throw new GDSetException("Duplicated column name!");

      }

      columnNameToIndex.put(columnNames[i].toLowerCase(), new Integer(i));

      columns[i] = new Column();

      columns[i].setColumnName(columnNames[i]);

      columns[i].setColumnLabel(columnNames[i]);

      columns[i].setColumnType(Column.COLUMN_TYPE_STRING);

    }

    rowList = new ArrayList();
    colList = new ArrayList();

    for (int i = 0; i < rowCount; i++) {

      String[] row = new String[columnCount];

      rowList.add(row);

    }

  }

  /**
   *
   * @return ����
   */

  public int getRowCount() {
	if (colList.size()>0&&rowList.size()==0) {
		return ((ArrayList)colList.get(0)).size();
	}else{
		return rowList.size();
	}

  }

  /**
   * �����еĸ���
   * @return ����
   */

  public int getColumnCount() {

    return columnCount;

  }

  private void setColumnCount(int columnCount) {

    this.columnCount = columnCount;

  }

  /**
   * ���������Ƶõ��е���ţ��е���Ŵ�0��ʼ
   * @param columnName ������
   * @return �����
   */

  public int getColumnIndex(String columnName) {

    Integer index = (Integer)this.columnNameToIndex.get(columnName.

        toLowerCase());

    if (index == null) {

      return -1;

    }

    return index.intValue();

  }

  /**
   * ��������ŵõ��е����ƣ��е���Ŵ�0��ʼ
   * @param index �����
   * @return ������
   */

  public String getColumnName(int index) {

    if ( (index >= 0) && (index < columns.length)) {

      return columns[index].getColumnName();

    }
    else {

      return "";

    }

  }

  /**
   * ����������
   * @param index
   * @param columnName
   * @throws GDSetException
   */

  public void setColumnName(int index, String columnName) throws GDSetException {

//    if ( (index >= 0) && (index < columns.length)) {
//
//      int i = getColumnIndex(columnName);
//
//      if ( (i >= 0) && (i != index))
//
//        throw new GDSetException("Set column name:column name "
//
//                                 + columnName + " is allready in GDSet."
//
//                                 + " GDSet Can't has two same column name.");
//
//      String oldName = getColumnName(index);
//
//      columnNameToIndex.remove(oldName);
	  this.columnCount++;
      columnNameToIndex.put(columnName.toLowerCase(), new Integer(index));

//      columns[index].setColumnName(columnName.toLowerCase());

//    }

//    else {
//
//      throw new GDSetException("Set column name: column index("
//
//                               + index + ") out of range(0-"
//
//                               + (columns.length - 1) + ")");
//
//    }

  }

  /**
   * �����е����ƣ��е���Ŵ�0��ʼ
   *
   * @return �������������Ƶ��ַ�������
   */

  public String[] getAllColumnName() {

    String[] columnNames = new String[getColumnCount()];

    for (int i = 0; i < getColumnCount(); i++) {

      columnNames[i] = getColumnName(i);

    }

    return columnNames;

  }

  /**
   * ��������ŵõ��е���ʾ���ƣ��е���Ŵ�0��ʼ
   * @param index �����
   * @return ����ʾ����
   */

  public String getColumnLabel(int index) {

    if ( (index >= 0) && (index < columns.length)) {

      return columns[index].getColumnLabel();

    }
    else {

      return "";

    }

  }

  /**
   * ���������Ƶõ��е���ʾ����
   * @param columnName ������
   * @return ����ʾ���ƣ����Ҳ����з���""
   */

  public String getColumnLabel(String columnName) {

    Integer index = (Integer) columnNameToIndex.get(columnName.toLowerCase());

    if (index != null) {

      return columns[index.intValue()].getColumnLabel();

    }
    else {

      return "";

    }

  }

  public void setColumnLabel(String columnName, String columnLabel) {

    Integer index = (Integer) columnNameToIndex.get(columnName.toLowerCase());

    if (index != null) {

      columns[index.intValue()].setColumnLabel(columnLabel);

    }

  }

  public void setColumnLabel(int index, String columnLabel) {

    if ( (index >= 0) && (index < columns.length)) {

      columns[index].setColumnLabel(columnLabel);

    }

  }

  public void setColumnValue(String columnName, String value) {

    Integer index = (Integer) columnNameToIndex.get(columnName.toLowerCase());

    if (index != null) {

      if (value == null) {

        value = "";

      }

      for (int i = 0; i < this.getRowCount(); i++) {

        try {

          this.setString(i, index.intValue(), value);

        }
        catch (GDSetException ex) {

        }

      }

    }

  }

  public void setColumnValue(int index, String value) {

    if ( (index >= 0) && (index < columns.length)) {

      if (value == null) {

        value = "";

      }

      for (int i = 0; i < this.getRowCount(); i++) {

        try {

          this.setString(i, index, value);

        }
        catch (GDSetException ex) {

        }

      }

    }

  }

  /**
   * ��������ŵõ��е����ͣ��е���Ŵ�0��ʼ
   * @param index �����
   * @return ������
   */

  public String getColumnType(int index) {

    if ( (index >= 0) && (index < columns.length)) {

      return columns[index].getColumnType();

    }
    else {

      return "";

    }

  }

  /**
   * ���������Ƶõ��е�����
   * @param columnName ������
   * @return ������
   */

  public String getColumnType(String columnName) {

    Integer index = (Integer) columnNameToIndex.get(columnName.toLowerCase());

    if (index != null) {

      return columns[index.intValue()].getColumnType();

    }
    else {

      return "";

    }

  }

  public void setColumnType(String columnName, String columnType) {

    Integer index = (Integer) columnNameToIndex.get(columnName.toLowerCase());

    if (index != null) {

      columns[index.intValue()].setColumnType(columnType);

    }

  }

  public void setColumnType(int index, String columnType) {

    if ( (index >= 0) && (index < columns.length)) {

      columns[index].setColumnType(columnType);

    }

  }

  /**
   * �õ�ͨ�����ݶ���洢���߼�������
   * @return ������
   */

  public String getTableName() {

    return tableName;

  }

  /**
   * ����ͨ�����ݶ���洢���߼�������
   * @param tableName ������
   */

  public void setTableName(String tableName) {

    this.tableName = tableName;

  }

  /**
   * �Ӷ�����ɾ��һ�У��кŴ�0��ʼ
   * @param index �к�
   * @throws GDSetException ���кų�����Χ�����׳��쳣
   */

  public void removeRow(int index) throws GDSetException {

    if ( (index >= 0) && (index < rowList.size())) {

      rowList.remove(index);

    }
    else {

      throw new GDSetException("Remove a row: Row index out of range!");

    }

  }
  /**
   * �Ӷ�����ɾ��һ�У��кŴ�0��ʼ
   * @param index �к�
   * @throws GDSetException ���кų�����Χ�����׳��쳣
   */
  
  public void removeCol(int index) throws GDSetException {
	  
	  if ( (index >= 0) && (index < colList.size())) {
		  
		  colList.remove(index);
		  columnCount--;
	  }
	  else {
		  
		  throw new GDSetException("Remove a row: Row index out of range!");
		  
	  }
	  
  }

  /**
   * ��ָ��λ�ò���һ��
   * @param row ��������ݣ�����Ϊ�ַ������飬����ĳ��ȱ�������ݶ�����г�����ͬ
   * @param index �����λ�ã���0��ʼ
   * @throws GDSetException
   */

  public void insertRow(String[] row, int index) throws GDSetException {

    if (row.length == columnCount) {

      rowList.add(index, row);

    }
    else {

      throw new GDSetException("Insert a row: row's columnCount("

                               + row.length +

                               ") doesn't match table's columnCount("

                               + columnCount + ")");

    }

  }

  /**
   * ��ĩβ���һ��
   * @param row ��ӵ����ݣ�����Ϊ�ַ������飬����ĳ��ȱ�������ݶ�����г�����ͬ
   * @return ����������ӵ����кţ���0��ʼ
   * @throws GDSetException
   */

  public int addRow(String[] row) throws GDSetException {

    if (row.length == columnCount) {

      String[] newRow = new String[row.length];

      System.arraycopy(row, 0, newRow, 0, row.length);

      rowList.add(newRow);

      return rowList.size() - 1;

    }
    else {

      throw new GDSetException("Insert a row: row's columnCount("

                               + row.length +

                               ") doesn't match table's columnCount("

                               + columnCount + ")");

    }

  }
  /**
   * ��ĩβ���һ��
   * 
   * @author liqing
   * @param row ��ӵ����ݣ�����Ϊ�ַ������飬����ĳ��ȱ�������ݶ�����г�����ͬ
   * @return ����������ӵ����кţ���0��ʼ
   * @throws GDSetException
   */

  public void addRow(ArrayList row) throws GDSetException {
	  rowList.add(row);
//    if (row.size() == columnCount) {
//      rowList.add(row);
//    }
  }
  public void addRow(ArrayList row,int index) throws GDSetException {
	  rowList.add(index,row);
//	  if (row.size() == columnCount) {
//		  rowList.add(index,row);
//	  }
  }
  //hg add 2005-04-13
  /**
   * ��ָ��λ�����һ��
   * @param row ��ӵ����ݣ�����Ϊ�ַ������飬����ĳ��ȱ�������ݶ�����г�����ͬ
   * @return ��������������������0��ʼ
   * @throws GDSetException
   */
  public int addRow(String[] row, int index) throws GDSetException {

    if (row.length == columnCount) {

      String[] newRow = new String[row.length];

      System.arraycopy(row, 0, newRow, 0, row.length);

      rowList.add(index, newRow);

      return rowList.size() - 1;

    }
    else {

      throw new GDSetException("Insert a row: row's columnCount("

                               + row.length +

                               ") doesn't match table's columnCount("

                               + columnCount + ")");

    }

  }

  public void setRow(String[] row, int index) throws GDSetException {

    if (index >= this.getRowCount()) {
      throw new GDSetException("index > row count!!index=" + index +
                               ",row count=" + this.getRowCount());
    }

    if (row.length == columnCount) {

      String[] newRow = new String[row.length];

      System.arraycopy(row, 0, newRow, 0, row.length);

      rowList.set(index, newRow);

      return;

    }
    else {

      throw new GDSetException("Insert a row: row's columnCount("

                               + row.length +

                               ") doesn't match table's columnCount("

                               + columnCount + ")");

    }

  }

  /**
   * ��ĩβ���һ����
   * @return ����������ӵ����кţ���0��ʼ
   */

  public int addRow() {

    String[] newRow = new String[this.getColumnCount()];

    for (int i = 0; i < this.getColumnCount(); i++) {

      newRow[i] = "";

    }

    rowList.add(newRow);

    return rowList.size() - 1;

  }

  /**
   * �õ�һ������
   * @param index �кţ���0��ʼ
   * @return �����ݣ�����Ϊ�ַ�������
   * @throws GDSetException
   */

  public String[] getRow(int index) throws GDSetException {

    if ( (index >= 0) && (index < rowList.size())) {

    	return (String[]) rowList.get(index);

    }
    else {

      throw new GDSetException("Get a row: Row index out of range(" +

                               rowList.size() + ")!");

    }

  }

  /**
   * ��ͨ�����ݶ����еõ����ݵ��Ӽ�, �������Ϊ0���򷵻�ֻ����Դ��������Ϣ�����ݶ���
   * @param index ��ʼ�кţ���0��ʼ
   * @param length ����
   * @return ͨ�����ݶ���
   * @throws GDSetException
   */

  public GDSet getSubGDSet(int index, int length) throws GDSetException {

    if (index < 0) {

      throw new GDSetException("Get Sub GDSet: Row index can not < 0!");

    }

    GDSet dataset = new GDSet(this.tableName);

    for (int i = 0; i < this.columnCount; i++) {

      dataset.addColumn(columns[i].getColumnName()

                        , columns[i].getColumnLabel()

                        , columns[i].getColumnType());

    }

    int rowCount = 0;

    while ( (rowCount < length) && ( (index + rowCount) < this.getRowCount())) {

      dataset.addRow(this.getRow(index + rowCount));

      rowCount++;

    }

    return dataset;

  }

  /**
   * ��һ�����ݶ����е�������ӵ�ĩβ�����ݶ�����б�����ͬ
   * @param dataset
   * @throws GDSetException
   */

  public void append(GDSet dataset) throws GDSetException {

    if (dataset == null) {

      throw new GDSetException("Append: How can I append a null to GDSet?");

    }

    if (getColumnCount() == dataset.getColumnCount()) {

      for (int i = 0; i < dataset.getRowCount(); i++)

        addRow(dataset.getRow(i));

    }

    else {

      throw new GDSetException("Append: GDSet column count is not match!");

    }

  }

  /**
   * �õ�һ����Ԫ�������
   * @param rowIndex �кţ���0��ʼ
   * @param columnName ������
   * @return �ַ����͵�����
   * @throws GDSetException
   */

  public String getString(int rowIndex, String columnName) throws

      GDSetException {

    Integer columnnum = (Integer) columnNameToIndex.get(columnName.

        toLowerCase());

    if (columnnum == null) {

      throw new GDSetException("Get string: table has not such a column " +

                               columnName);

    }

    if ( (rowIndex < 0) || (rowIndex >= rowList.size())) {

      throw new GDSetException("Get string: row index(" + rowIndex +

                               ") out of range(0--" + rowList.size() +

                               ")");

    }

    String[] row = (String[]) rowList.get(rowIndex);

    return row[columnnum.intValue()];

  }

  /**
   * �õ�һ����Ԫ�������
   * @param rowIndex �кţ���0��ʼ
   * @param columnIndex �кţ���0��ʼ
   * @return �ַ����͵�����
   * @throws GDSetException
   */

  public String getString(int rowIndex, int columnIndex) throws

      GDSetException {

    if ( (rowIndex < 0) || (rowIndex >= rowList.size())) {

      throw new GDSetException("Get string: row index(" + rowIndex +

                               ") out of range(0--" + rowList.size() +

                               ")");

    }

    if ( (rowIndex < 0) || (columnIndex >= columnCount)) {

      throw new GDSetException("Get string: column index(" + columnIndex +

                               ") out of range(0--" + columnCount + ")");

    }

    String[] row = (String[]) rowList.get(rowIndex);

    return row[columnIndex];

  }
  /**
   * �õ�һ����Ԫ�������
   * 
   * @author liqing
   * @param rowIndex �кţ���0��ʼ
   * @param columnName ������
   * @return �ַ����͵�����
   * @throws GDSetException
   */
  
  public String getCell(int rowIndex, String columnName) throws
  
  GDSetException {
	  
	  Integer columnnum = (Integer) columnNameToIndex.get(columnName.
			  
			  toLowerCase());
	  ArrayList getlist = null;
	  if (rowList.size() > 0 && colList.size() == 0) {
		  getlist = (ArrayList) rowList.get(rowIndex);
		  return (String)getlist.get(columnnum.intValue());
		} else if (rowList.size() == 0 && colList.size() > 0) {
			getlist = (ArrayList) colList.get(columnnum.intValue());
			return (String)getlist.get(rowIndex);
		}
	  
	  return null;
	  
	  
  }
  
  /**
   * �õ�һ����Ԫ�������
   * 
   * @author liqing
   * @param rowIndex �кţ���0��ʼ
   * @param columnIndex �кţ���0��ʼ
   * @return �ַ����͵�����
   * @throws GDSetException
   */
  
  public String getCell(int rowIndex, int columnIndex) throws
  
  GDSetException {
	  
	  ArrayList row =null;
	  if (rowList.size() > 0 && colList.size() == 0) {
			row = (ArrayList) rowList.get(rowIndex);
			return (String) row.get(columnIndex);
		} else if (rowList.size() == 0 && colList.size() > 0) {
			row = (ArrayList) colList.get(columnIndex);
			return (String) row.get(rowIndex);
		}
	  return "-1";
	  
  }

  /**
   * ����һ����Ԫ�������
   * @param rowIndex �кţ���0��ʼ
   * @param columnName ������
   * @param value ���õ�����
   * @throws GDSetException ���õ��к��б����Ƕ������Ѿ����ڵģ������׳��쳣
   */

  public void setString(int rowIndex, String columnName, String value) throws

      GDSetException {

    Integer columnnum = (Integer) columnNameToIndex.get(columnName.

        toLowerCase());

    if (columnnum == null) {

      throw new GDSetException("Set string: table has not such a column " +

                               columnName);

    }

    if ( (rowIndex < 0) || (rowIndex >= rowList.size())) {

      throw new GDSetException("Set string: row index(" + rowIndex +

                               ") out of range(0--" + rowList.size() +

                               ")");

    }

    String[] row = (String[]) rowList.get(rowIndex);

    if (value == null)
      value = "";

    row[columnnum.intValue()] = value;

  }

  /**
   * ����һ����Ԫ�������,���洢���ַ���Ϊnull�����Զ�ת��Ϊ""
   * @param rowIndex �кţ���0��ʼ
   * @param columnName �кţ���0��ʼ
   * @param value ���õ�����
   * @throws GDSetException ���õ��к��б����Ƕ������Ѿ����ڵģ������׳��쳣
   */

  public void setString(int rowIndex, int columnIndex, String value) throws

      GDSetException {

    if ( (rowIndex < 0) || (rowIndex >= rowList.size())) {

      throw new GDSetException("Set string: row index(" + rowIndex +

                               ") out of range(0--" + rowList.size() +

                               ")");

    }

    if ( (rowIndex < 0) || (columnIndex >= columnCount)) {

      throw new GDSetException("Get string: column index(" + columnIndex +

                               ") out of range(" + columnCount + ")");

    }

    String[] row = (String[]) rowList.get(rowIndex);

    if (value == null)
      value = "";

    row[columnIndex] = value;

  }

  /**
   * ���һ��
   * 
   * @author liqing
   * @param ArrayList col 
   */
  
  public void addColumn(ArrayList col)  throws GDSetException {
	  colList.add(col);
  }
  /**
   * ���һ��
   * 
   * @author liqing
   * @param ArrayList col 
   */
  
  public void addColumn(ArrayList col,int index)  throws GDSetException {
	  colList.add(index,col);
	  columnCount++;
  }
  /**
   * ����һ��
   * 
   * @author liqing
   * @param int col 
   * @return ArrayList
   */
  
  public ArrayList getListColumn(int col)  throws GDSetException {
	  
	  if(!(colList==null||colList.size()==0)){
	   return  (ArrayList) colList.get(col);
	  }else{
		  return null;
	  }

  }
  /**
   * ����һ��
   * 
   * @author liqing
   * @param int row 
   * @return ArrayList
   */
  
  public ArrayList getListRow(int row)  throws GDSetException {
	  
	  if(!(rowList==null||rowList.size()==0)){
		  return  (ArrayList) rowList.get(row);
	  }else{
		  return null;
	  }
	  
  }
  
  /**
   * ���һ�У����е�����ȫ����ʼ��Ϊ""
   * @param columnName ������
   * @param columnLabel ����ʾ����
   * @param columnType �����ͣ��μ�Column�ඨ���������
   * @see Column
   */

  public void addColumn(String columnName, String columnLabel,

                        String columnType) {

    addColumn(columnName, columnLabel, columnType, "");

  }

  /**
   * ���һ�У����е�����ȫ����ʼ��Ϊ""
   * @param columnName ������
   * @param columnLabel ����ʾ����
   * @param columnType �����ͣ��μ�Column�ඨ���������
   * @param value Ĭ��ֵ
   * @see Column
   */

  public void addColumn(String columnName, String columnLabel,

                        String columnType, String value) {

    if (columnType == null) {

      columnType = Column.COLUMN_TYPE_STRING;

    }

    if (columnName != null) {

      if (this.getColumnIndex(columnName) >= 0) {

        return;

      }

      if (value == null) {

        value = "";

      }

      columnCount++;

      Column[] newColumns = new Column[columnCount];

      if ( (columns != null) && (columns.length > 0)) {

        System.arraycopy(columns, 0, newColumns, 0, columnCount - 1);

      }

      Column col = new Column();

      col.setColumnName(columnName);

      col.setColumnLabel(columnLabel);

      col.setColumnType(columnType);

      if (columnType.equalsIgnoreCase("DATE")) {

        col.setColumnType(Column.COLUMN_TYPE_DATE);

      }

      newColumns[columnCount - 1] = col;

      columns = newColumns;

      columnNameToIndex.put(columnName.toLowerCase(),

                            new Integer(columnCount - 1));

      for (int i = 0; i < rowList.size(); i++) {

        String[] row = (String[]) rowList.get(i);

        String[] newRow = new String[columnCount];

        System.arraycopy(row, 0, newRow, 0, columnCount - 1);

        newRow[columnCount - 1] = value;

        rowList.set(i, newRow);

        row = null;

      }

    }

  }

  /**
   * ���һ�У�����values�е�������ӵ�������
   * @param columnName ������
   * @param columnLabel ����ʾ����
   * @param columnType �����ͣ��μ�Column�ඨ���������
   * @param values ����
   * @see Column
   */

  public void addColumn(String columnName, String columnLabel,

                        String columnType, String[] values) {

    if (columnType == null) {

      columnType = Column.COLUMN_TYPE_STRING;

    }

    if (columnName != null) {

      columnCount++;

      Column[] newColumns = new Column[columnCount];

      if ( (columns != null) && (columns.length > 0)) {

        System.arraycopy(columns, 0, newColumns, 0, columnCount - 1);

      }

      Column col = new Column();

      col.setColumnName(columnName);

      col.setColumnLabel(columnLabel);

      col.setColumnType(columnType);

      if (columnType.equalsIgnoreCase("DATE")) {

        col.setColumnType(Column.COLUMN_TYPE_DATE);

      }

      newColumns[columnCount - 1] = col;

      columns = newColumns;

      columnNameToIndex.put(columnName.toLowerCase(),

                            new Integer(columnCount - 1));

      for (int i = 0; i < rowList.size(); i++) {

        String[] row = (String[]) rowList.get(i);

        String[] newRow = new String[columnCount];

        System.arraycopy(row, 0, newRow, 0, columnCount - 1);

        if (i < values.length) {

          if (values[i] == null) {

            values[i] = "";

          }

          newRow[columnCount - 1] = values[i];

        }
        else {

          newRow[columnCount - 1] = "";

        }

        rowList.set(i, newRow);

        row = null;

      }

      for (int i = rowList.size(); i < values.length; i++) {

        String[] newRow = new String[columnCount];

        for (int j = 0; j < columnCount; j++) {

          newRow[j] = "";

        }

        newRow[columnCount - 1] = values[i];

        rowList.add(newRow);

      }

    }

  }

  /**
   * ����һ�е�����
   * @param columnName ������
   * @param start ��ʼ��������0��ʼ
   * @param values ����
   */

  public void updateColumnData(String columnName, int start, String[] values) {

    if (columnName == null)

      return;

    int index = getColumnIndex(columnName);

    if (index < 0)

      return;

    for (int i = start; i < rowList.size(); i++) {

      if (i - start < values.length) {

        try {

          setString(i, index, values[i - start]);

        }
        catch (GDSetException ex) {

        }

      }

    }

  }

  /**
   * ɾ��һ�У����м����е�����ȫ��ɾ��
   * @param columnName ������
   * @throws GDSetException
   */

  public void removeColumn(String columnName) throws GDSetException {

    if (columnName != null) {

      Integer columnIndex = (Integer) columnNameToIndex.get(columnName.

          toLowerCase());

      if (columnIndex == null) {

        throw new GDSetException("Remove column: not such a column " +

                                 columnName);

      }

      columnCount--;

      int index = columnIndex.intValue();

      Column[] newColumns = new Column[columnCount];

      System.arraycopy(columns, 0, newColumns, 0, index);

      System.arraycopy(columns, index + 1, newColumns, index,

                       columnCount - index);

      columns = newColumns;

      columnNameToIndex.clear();

      for (int i = 0; i < columns.length; i++) {

        columnNameToIndex.put(columns[i].columnName.toLowerCase(),

                              new Integer(i));

      }

      for (int i = 0; i < rowList.size(); i++) {

        String[] row = (String[]) rowList.get(i);

        String[] newRow = new String[columnCount];

        System.arraycopy(row, 0, newRow, 0, index);

        System.arraycopy(row, index + 1, newRow, index,

                         columnCount - index);

        rowList.set(i, newRow);

        row = null;

      }

    }
    else {

      throw new GDSetException(

          "Remove column: Column name can not be null!");

    }

  }
  /**
   * @author liqing
   * ����һ�������� ��Ҫgdset��ReturnType
   * orderStr��д��index �Զ��ŷָ�
   * @param orderStr
   * @param ReturnType
   * @throws GDSetException
   */
  public void orderBy(String orderStr,int ReturnType) throws GDSetException {
	    String[] columnNames = orderStr.split(",");
	    HashMap map = new HashMap();
	    String[] temparr = null;
	    ArrayList sort = new ArrayList();
	    ArrayList templist = new ArrayList();
	    int tempint=0;
	    String temp="";
	    for (int i = 0; i < columnNames.length; i++) {
	    	map.put(columnNames[i], "");
		}
	    if(ReturnType==GDSetTool.Return_ArrayListCol){
    		for(int j=0;j<this.getRowCount();j++){
    			for (int i =0;i< columnNames.length;i++) {
	    			temp+=((ArrayList)this.colList.get(Integer.parseInt(columnNames[i]))).get(j)+"!!";
	    		}
    			for(int i =0;i<this.getColumnCount();i++){
    				if(!map.containsKey(""+i)){
    					temp+=((ArrayList)this.colList.get(i)).get(j)+"!!";
    				}
    			}
    			sort.add(temp.substring(0,temp.length()-2));
    			temp="";
			}
    		Collections.sort(sort);
    		for (int j = 0; j < this.getColumnCount(); j++) {
    			
	    		for(int i=0;i<this.getRowCount();i++){
	    			if(this.getRowCount()==18)
	    			System.out.println(i+"--"+this.getRowCount());
	    			temparr=sort.get(i).toString().split("!!");
	    			templist.add(temparr[j]);
	    			
				}
	    		if(j<columnNames.length){
		    		((ArrayList)this.colList.get(Integer.parseInt(columnNames[j]))).clear();
		    		for (int i = 0; i < templist.size(); i++) {
		    			((ArrayList)this.colList.get(Integer.parseInt(columnNames[j]))).add(templist.get(i));
					}
	    		}else{
	    			for (int i = 0; i < columnNames.length; i++) {
						if(Integer.parseInt(columnNames[i])==tempint){
							tempint++;
						}else{
							break;
						}
					}
	    			((ArrayList)this.colList.get(tempint)).clear();
	    			for (int i = 0; i < templist.size(); i++) {
	    				((ArrayList)this.colList.get(tempint)).add(templist.get(i));
					}
		    		
		    		tempint++;
	    		}
	    		templist.clear();
    		}
	    }
	  }
  
  /**
   * ��GDSet�еļ�¼�������������ַ�����sql�﷨��order by�־���ͬ��
   * ��:"start_date,end_date desc"ֱ��ʹ���ֶ�����Ϊ�����ֶ����ƺ������descΪ����
   * �����ֶ����Ƶ��Ⱥ�˳��ȷ����������ȼ���
   * @param orderStr �����ַ���
   * @throws GDSetException
   */
  public void orderBy(String orderStr) throws GDSetException {
    String[] columnNames = StringTool.parseTokenString(orderStr, ",");

    for (int i = 0; i < this.getRowCount(); i++) {
      for (int j = i + 1; j < this.getRowCount(); j++) {
        if (compareRow(i, j, columnNames) > 0) {
          String[] rowi = this.getRow(i);
          String[] rowj = this.getRow(j);
          this.setRow(rowi, j);
          this.setRow(rowj, i);
        }
      }
    }
  }

  /**
   * �Ƚ�������¼�Ĵ�С
   * @param i ��¼������
   * @param j ��¼������
   * @param orderColumnNames �����ֶ���������
   * @return ����¼i���ڼ�¼j,����1����ȷ���0��С�ڷ���-1
   * @throws GDSetException �������󷵻��쳣
   */
  public int compareRow(int i, int j, String[] orderColumnNames) throws
      GDSetException {
    int result = 0;
    for (int k = 0; k < orderColumnNames.length; k++) {
      String columnName = orderColumnNames[k].toLowerCase().trim();
      boolean bUpOrder = true;
      //��desc��β��Ϊ��������
      if (columnName.endsWith("desc")) {
        int loc = columnName.indexOf("desc");
        columnName = columnName.substring(0, loc).trim().toLowerCase();
        bUpOrder = false;
      }

      //�Ƚ����ֶε�ֵ�������ֶηֳ���С�����Ƴ�ѭ�����ؽ������������Ƚ���һ���ֶ�
      String valuei = this.getString(i, columnName);
      String valuej = this.getString(j, columnName);
      int compareValue = valuei.compareTo(valuej);

      //��Ϊ�ֶ�Ϊ����
      if (bUpOrder) {
        if (compareValue > 0) {
          result = 1;
          break;
        }
        else if (compareValue < 0) {
          result = -1;
          break;
        }
      }
      //���ֶ�Ϊ����
      else {
        if (compareValue > 0) {
          result = -1;
          break;
        }
        else if (compareValue < 0) {
          result = 1;
          break;
        }
      }
    }
    return result;
  }

  /**
   * �����ݶ������л�Ϊ�ַ�����
   * GDSetTool��parseStringToGDSet�����ɽ��ַ���ת��ΪGDSet
   * @return
   */

  public String toString() {

    StringBuffer buffer = new StringBuffer();

    buffer.append("<tablename>");

    buffer.append(GDSetTool.encode(tableName, DELIM, CODECHAR));

    buffer.append("</tablename>");

    buffer.append("<columns>");

    for (int j = 0; j < this.getColumnCount(); j++) {

      buffer.append(GDSetTool.encode(getColumnName(j), DELIM, CODECHAR));

      buffer.append(DELIM);

    }

    buffer.append("</columns>");

    for (int i = 0; i < this.getRowCount(); i++) {

      try {

        String[] row = this.getRow(i);

        for (int j = 0; j < this.getColumnCount(); j++) {

          buffer.append(GDSetTool.encode(row[j], DELIM, CODECHAR));

          buffer.append(DELIM);

        }

      }

      catch (GDSetException ex) {

        ex.printStackTrace();
      }

    }

    return buffer.toString();

  }

  public ArrayList toArrayList(){
	  ArrayList temp =new ArrayList();
	  if (colList.size()>0&&rowList.size()==0) {
		  temp.addAll(this.colList);
	  }else{
		  temp.addAll(this.rowList);
	  }
	  return temp;
  }
  /**
   * ɾ���������������ݣ����������С�
   */

  public void clearAllRow() {

    rowList.clear();

  }

  /**
   * �ͷŶ��������е�����
   */

  public void destroy() {

    columns = null;

    columnCount = 0;

    rowList.clear();

    rowList = null;
    
    colList.clear();
    
    colList = null;

    columnNameToIndex.clear();

    columnNameToIndex = null;

    tableName = null;

  }


}


