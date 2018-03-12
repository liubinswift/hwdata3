package com.viewscenes.dao.innerdao;



import com.viewscenes.dao.*;

import com.viewscenes.pub.*;

import com.viewscenes.dao.database.DbException;



import java.util.*;



import org.jdom.*;



/**

 * �߼�����������Ĺ�����

 * �磺

 * ��mon_center_tab

 * ������ֶ�Ϊ:center_id,loc_id

 * ����Ϊ:loc_id = 1 and center_id = 5

 * �������ļ��ڵ�logicconfig�У�mon_center_tab���߼�������sys_location_tab loc��loc_id����

 * �����Ϊ��

 * ��mon_center_tab tab,sys_location_tab loc

 * ������ֶ�Ϊ:tab.center_id,loc.loc_id

 * ����Ϊ:loc.loc_id = 1 and tab.center_id = 5

 * <p>Title: </p>

 * <p>Description: </p>

 * <p>Copyright: Copyright (c) 2003</p>

 * <p>Company: </p>

 * @author not attributable

 * @version 1.0

 */



public class ConLogicFilter

        extends ConBaseFilter {

    private Element _ele;

    //���������ֶΣ��������������ֶ�

    private static final int _ALIASFIELD = 0;

    private static final int _PHSICSFIELD = 1;

    private final static String _ALIAS = "tab.";



    public ConLogicFilter(String field, GDSet condition) throws DbException {

        _ele = XmlReader.getItem("logicconfig", "table", condition.getTableName());

        //���ø�������

        setField(field);

        setTabName();

        setCondition(condition);

    }



    /**

     * ���ò�ѯ����������

     * @param condition

     * @throws DbException

     */

    private void setCondition(GDSet condition) throws DbException {

        String con = combinCondition(condition);

        con = Common.combineSQL(con,"and");

        //�������еĴӱ������Ĺ����������ⲿ������������

        _condition = _ele.getAttributeValue("condition") + con;

    }



    /**

     * �����ֶε�����

     * @param field

     */

    private void setField(String field) {

        _field = appendField(field);

    }



    /**

     * ���ñ���������

     * @param tabName

     */

    private void setTabName() {

        _tabName = _ele.getAttributeValue("tab");

    }



    /**

     * �õ���i�е�fieldֵ

     * @param condition

     * @param conditioni ������

     * @return �ֶ�ֵ

     * @throws GDSetException

     */

    protected String getConColumn(GDSet condition, int i) throws GDSetException {

        String col = condition.getString(i, ConditionColumn.FIELD);

        return getColAlias(col, _PHSICSFIELD);

    }



    /**

     * �õ��������ļ���Ϻ���ֶ���

     * count(*)->count(*)

     * *->tab.*,tab.loc_id

     * center_id,loc_id->tab.center_id,tab.loc_id

     * center_id,loc_name->tab.center_id,loc.loc_id���Ǹ����ֶ�

     * @param field ���ص��ֶ�

     * @return

     */

    private String appendField(String field) {

        String fd = "";

        //count(*)->count(*)

        if (field.equals("count(*)")) {

            return field;

        }

        // *->tab.*,tab.loc_id

        if (field.equals("*")) {

            fd = _ALIAS + field + getAllField();

        } else {

            fd = getColAlias(field, _ALIASFIELD);

        }

        return fd;

    }



    /**

     * �õ��߼��ֶζ�Ӧ�������ֶ����ƻ��߼��ֶκ������ֶε�ȫ��

     * @param field

     * @param fieldType

     *     fieldType = _ALIASFIELD

     *     ��:head_name->head.name head_name(_field�����xml�ļ��ж��������б�ĸ����ֶ�)

     *     fieldType = _PHSICSFIELD

     *     ��:head_name->head.name(_field�����xml�ļ��ж��������б�ĸ����ֶ�)

     * @return

     */

    private String getColAlias(String field, int fieldType) {

        StringBuffer col = new StringBuffer();

        String[] fieldName = Common.Field2Array(field);

        for (int i = 0; i < fieldName.length; i++) {

            String fd = getEachColAlias(fieldName[i], fieldType);

            col.append(fd);



            if (i < fieldName.length - 1) {

                col.append(",");

            }

        }

        return col.toString();

    }



    /**

     * �õ����ӵ������ֶε����ƻ��ߵõ������ֶεı����������ֶε�����

     * fieldType = _ALIASFIELD

     * ��:head_name->head.name head_name(_field�����xml�ļ��ж��������б�ĸ����ֶ�)

     * fieldType = _PHSICSFIELD

     * ��:head_name->head.name(_field�����xml�ļ��ж��������б�ĸ����ֶ�)

     * @param field �����ֶ�

     * @return

     * ��������ڸ����ֶ�:��tab. + field

     */

    private String getEachColAlias(String field, int fieldType) {

        List list = _ele.getChildren();

        Iterator iter = list.iterator();

        while (iter.hasNext()) {

            Element ele = (Element) iter.next();

            String alias = ele.getAttributeValue("alias_name");

            if (alias != null && alias.equals(field)) {

                if (fieldType == _PHSICSFIELD) {

                    return ele.getAttributeValue("phy_name");

                } else {

                    return ele.getAttributeValue("phy_name") + " " + alias;

                }

            }

        }

        return _ALIAS + field;

    }



    /**

     * �õ����и����ֶ�

     * @param field

     * @return

     */

    private String getAllField() {

        StringBuffer ret = new StringBuffer();

        List list = _ele.getChildren();

        Iterator iter = list.iterator();

        while (iter.hasNext()) {

            Element ele = (Element) iter.next();

            ret.append(",").append(ele.getAttributeValue("phy_name"));

            ret.append(" ").append(ele.getAttributeValue("alias_name"));

        }

        return ret.toString();

    }



}
