package com.viewscenes.web.sysmgr.dicManager;


import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.sys.TableInfoCache;

public class DicDao {
	public DicDao() {
		super();
	}
	

	/**
	 * 
	 * <p>class/function:com.viewscenes.web.sysmgr.dicManager
	 * <p>explain:删除数据，如果添加出错会返回错误信息
	 * <p>author-date:张文 2012-7-26
	 * @param:
	 * @return:
	 */
	
	public boolean deleteState(String dellist) throws Exception {
		String sql ="";
		boolean df=false;
		
		sql = "delete from dic_state_tab where state in("+dellist+")";
		
		df=DbComponent.exeUpdate(sql);
		TableInfoCache as =new TableInfoCache();
		return df;
	}
	
	/**
	 * 
	 * <p>class/function:com.viewscenes.web.sysmgr.dicManager
	 * <p>explain:删除前端数据，如果添加出错会返回错误信息
	 * <p>author-date:张文 2012-7-26
	 * @param:
	 * @return:
	 */
	
	public boolean deleteHead(String dellist){
		String sql ="";
		boolean df=false;
		String[] delArr=dellist.split(",");
		String[] delSql=new String[delArr.length];
		for(int i=0;i<delArr.length;i++)
		{
			if(delArr[i].indexOf("V8")!=-1)
			{
				delSql[i]="update res_headend_tab set is_delete=1 where version='V8' and code like "+delArr[i].substring(0, delArr[i].length()-3)+"%'";
				
			}else
			{
				delSql[i]="update res_headend_tab set is_delete=1 where code = "+delArr[i];
					
			}
		}
		//sql = "delete from res_headend_tab t where decode(t.type_id||t.version, '102V8', substr(t.code, 0, length(t.code)-1),t.code) in("+dellist+")";
		
		try {
			DbComponent.exeBatch(delSql);
			return true;
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	
	/**
	 * 
	 * <p>class/function:com.viewscenes.web.sysmgr.dicManager
	 * <p>explain:删除语言数据，如果添加出错会返回错误信息
	 * <p>author-date:张文 2012-7-26
	 * @param:
	 * @return:
	 */
	
	public boolean deleteLanguage(String dellist) throws Exception {
		String sql ="";
		boolean df=false;
		
		sql = "delete from zdic_language_tab where language_id in("+dellist+")";
		
		df=DbComponent.exeUpdate(sql);
		return df;
	}
}
