package com.viewscenes.web.devicemgr.equinstall;
/**
 * 设备安装管理类
 */
import java.util.ArrayList;

import org.jmask.web.controller.EXEException;

import com.viewscenes.bean.devicemgr.RadioEquInstallBean;
import com.viewscenes.dao.database.DbComponent;
import com.viewscenes.dao.database.DbException;
import com.viewscenes.pub.GDSet;
import com.viewscenes.pub.GDSetException;
import com.viewscenes.util.StringTool;

import flex.messaging.io.amf.ASObject;

public class EquInstallManager {
	
	/**
	 * 查询设备安装信息
	 * @param object
	 * @return
	 */
	public Object queryEquList(ASObject object){
		ArrayList<RadioEquInstallBean> beanArr=new ArrayList<RadioEquInstallBean>();
		String type=(String)object.get("type_id");
		String head_name="'"+((String)object.get("head_name")).replaceAll(",", "','")+"'";
		String starttime = (String)object.get("starttime");
		String enddate = (String)object.get("enddate");
		String equ_name="'"+((String)object.get("equ_name")).replaceAll(",", "','")+"'";
		String status=(String)object.get("status");
		String order_no=(String)object.get("order_id");
		StringBuffer bf=new StringBuffer();
		bf.append("select t.* from radio_equ_install_tab t,res_headend_tab r where t.head_id=r.head_id");
		if(type!=null&&type.length()>0){
			bf.append(" and r.type_id  =").append(type);
		}
		if(starttime!=null&&starttime.length()>0){
			bf.append(" and t.mail_time >=to_date('"+starttime+"','yyyy-mm-dd')");
		}
		if(enddate!=null&&enddate.length()>0){
			bf.append(" and t.mail_time <=to_date('"+enddate+"','yyyy-mm-dd')");
		}
		if(equ_name.length()>2){
			bf.append(" and t.equ_name in( ").append(equ_name).append(")");
		}
		if(status!=null&&status.length()>0){
			bf.append(" and t.status  =").append(status);
		}
		if(order_no!=null&&order_no.length()>0){
			bf.append(" and t.order_no like '%").append(order_no).append("%'");
		}
		if(head_name.length()>2) {       	
			bf.append(" and t.head_name in( ").append(head_name).append(")");
        }
		bf.append(" order by head_name,mail_time desc");
		try {
			GDSet set=DbComponent.Query(bf.toString());
			int n=set.getRowCount();
			if(n>0){
				RadioEquInstallBean bean=new RadioEquInstallBean();
				String equId="";
				String equName="";
				String beanId="";
				for(int i=0;i<n;i++){
					if(i==0){
						bean.setHead_name(set.getString(i, "head_name"));
						bean.setMail_time(set.getString(i, "mail_time").substring(0,10));
						bean.setOrder_no(set.getString(i, "order_no"));
						beanId+=set.getString(i, "id");
						equId+=set.getString(i, "equ_id");
						equName+=set.getString(i, "equ_name");
					}else{
						if(bean.getHead_name().equals(set.getString(i, "head_name"))&&bean.getMail_time().equals(set.getString(i, "mail_time").substring(0,10))&&bean.getOrder_no().equals(set.getString(i, "order_no"))){							
							equId+=","+set.getString(i, "equ_id");
							equName+=","+set.getString(i, "equ_name");
							beanId+=","+set.getString(i, "id");
						}else{
							bean.setEqu_id(equId);
							bean.setEqu_name(equName);
							bean.setId(beanId);
							beanArr.add(bean);
							bean=new RadioEquInstallBean();
							bean.setHead_name(set.getString(i, "head_name"));
							bean.setMail_time(set.getString(i, "mail_time").substring(0,10));
							bean.setOrder_no(set.getString(i, "order_no"));
							equId=set.getString(i, "equ_id");
							equName=set.getString(i, "equ_name");
							beanId=set.getString(i, "id");
						}
					}
					if(i==n-1){
						bean.setEqu_id(equId);
						bean.setEqu_name(equName);
						bean.setId(beanId);
						beanArr.add(bean);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return beanArr;
	}
	public Object queryEquInstall(ASObject object){
		ASObject resObj=new ASObject();
		String type=(String)object.get("type_id");
		String head_name=(String)object.get("head_name");
		String starttime = (String)object.get("starttime");
		String enddate = (String)object.get("enddate");
		String equ_name=(String)object.get("equ_name");
		String save_place=(String)object.get("save_place");
		String status=(String)object.get("status");
		String order_no=(String)object.get("order_no");
		StringBuffer bf=new StringBuffer();
		bf.append("select t.* from radio_equ_install_tab t,res_headend_tab r where t.head_id=r.head_id");
		if(type!=null&&type.length()>0){
			bf.append(" and r.type_id  =").append(type);
		}
		if(starttime!=null&&starttime.length()>0){
			bf.append(" and t.mail_time >=to_date('"+starttime+"','yyyy-mm-dd')");
		}
		if(enddate!=null&&enddate.length()>0){
			bf.append(" and t.mail_time <=to_date('"+enddate+"','yyyy-mm-dd')");
		}
		if(equ_name!=null&&equ_name.length()>0){
			bf.append(" and t.equ_name like'& ").append(equ_name).append("%'");
		}
		if(save_place!=null&&save_place.length()>0){
			bf.append(" and t.save_place  =").append(save_place);
		}
		if(status!=null&&status.length()>0){
			bf.append(" and t.status  =").append(status);
		}
		if(order_no!=null&&order_no.length()>0){
			bf.append(" and t.order_no like '%").append(order_no).append("%'");
		}
		if(head_name.length()>2) {       	
			bf.append(" and t.head_name in( ").append(head_name).append(")");
        }
		bf.append(" order by head_name,mail_time desc");
		try {
			resObj=StringTool.pageQuerySql(bf.toString(), object);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("",e.getMessage(),"");
		}
		return resObj;
	}
	public Object queryEquInstallByState(ASObject object){
		ArrayList<RadioEquInstallBean> resObj=new ArrayList<RadioEquInstallBean>();
		String type=(String)object.get("type_id");
		String head_name=(String)object.get("head_name");
		String starttime = (String)object.get("starttime");
		String enddate = (String)object.get("enddate");
		String equ_name=(String)object.get("equ_name");
		String status=(String)object.get("status");
		String order_no=(String)object.get("order_no");
		StringBuffer bf=new StringBuffer();
		bf.append("select t.* from radio_equ_install_tab t,res_headend_tab r where t.head_id=r.head_id");
		if(type!=null&&type.length()>0){
			bf.append(" and r.type_id  =").append(type);
		}
		if(starttime!=null&&starttime.length()>0){
			bf.append(" and t.mail_time >=to_date('"+starttime+"','yyyy-mm-dd')");
		}
		if(enddate!=null&&enddate.length()>0){
			bf.append(" and t.mail_time <=to_date('"+enddate+"','yyyy-mm-dd')");
		}
		if(equ_name.length()>2){
			bf.append(" and t.equ_name in( ").append(equ_name).append(")");
		}
		if(status!=null&&status.length()>0){
			bf.append(" and t.status  =").append(status);
		}
		if(order_no!=null&&order_no.length()>0){
			bf.append(" and t.order_no like '%").append(order_no).append("%'");
		}		      	
		bf.append(" and t.head_name = '").append(head_name).append("'");        
		bf.append(" order by head_name,mail_time desc");
		try {
			GDSet set=DbComponent.Query(bf.toString());
			int n=set.getRowCount();
			if(n>0){
				for(int i=0;i<n;i++){
					RadioEquInstallBean bean=new RadioEquInstallBean();
					bean.setId(set.getString(i, "id"));
					bean.setHead_id(set.getString(i, "head_id"));
					bean.setHead_name(set.getString(i, "head_name"));
					bean.setHead_code(set.getString(i, "head_code"));
					bean.setEqu_id(set.getString(i, "equ_id"));
					bean.setEqu_name(set.getString(i, "equ_name"));
					bean.setStatus(set.getString(i, "status"));
					bean.setMail_time(set.getString(i, "mail_time"));
					bean.setOrder_no(set.getString(i, "order_no"));
					resObj.add(bean);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("",e.getMessage(),"");
		}
		return resObj;
	}
	public Object queryEquInstallByIds(RadioEquInstallBean equbean){
		ArrayList<RadioEquInstallBean> resObj=new ArrayList<RadioEquInstallBean>();

		StringBuffer bf=new StringBuffer();
		bf.append("select t.* from radio_equ_install_tab t,res_headend_tab r where t.head_id=r.head_id");			      	
        bf.append(" and t.id in (").append(equbean.getId());
		bf.append(") order by head_name,mail_time desc");
		try {
			GDSet set=DbComponent.Query(bf.toString());
			int n=set.getRowCount();
			if(n>0){
				for(int i=0;i<n;i++){
					RadioEquInstallBean bean=new RadioEquInstallBean();
					bean.setId(set.getString(i, "id"));
					bean.setHead_id(set.getString(i, "head_id"));
					bean.setHead_name(set.getString(i, "head_name"));
					bean.setHead_code(set.getString(i, "head_code"));
					bean.setEqu_id(set.getString(i, "equ_id"));
					bean.setEqu_name(set.getString(i, "equ_name"));
					String status=set.getString(i, "status");
					if(status.equals("0")){
						bean.setStatus("使用");
					}else if(status.equals("1")){
						bean.setStatus("备用");
					}else{
						bean.setStatus("报废");
					}
					bean.setMail_time(set.getString(i, "mail_time"));
					bean.setOrder_no(set.getString(i, "order_no"));
					resObj.add(bean);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("",e.getMessage(),"");
		}
		return resObj;
	}
	/**
	 * 添加设备安装信息集合
	 */
	public Object insertEquInstallArr(ASObject obj){
		String resault="添加设备安装信息成功";
		ArrayList<RadioEquInstallBean> equList=(ArrayList<RadioEquInstallBean>)obj.get("equList");
		try {
			for(int i=0;i<equList.size();i++){
				insertEquInstall(equList.get(i));
			}
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("","添加设备安装信息异常："+e.getMessage(),"");
		}
		return resault;
	}
	/**
	 * 添加站点设备信息
	 * @param head_name
	 * @param head_id
	 * @param equ_id
	 * @param equ_name
	 * @param status
	 * @param save_place
	 * @param mail_time
	 * @param order_no
	 * @return
	 * @throws DbException
	 */
	public Object insertEquInstall(String head_name,String head_id,String equ_id,String equ_name,String status,String save_place,String mail_time,String order_no ) throws DbException {
		String resault="添加设备安装信息成功";
		StringBuffer bf=new StringBuffer();
		bf.append("insert into radio_equ_install_tab (id,head_id,head_name,equ_name,equ_id,save_place,status,mail_time,order_no) values( radio_equ_install_seq.nextval");
		bf.append(",").append(head_id);
		bf.append(",'").append(head_name);
		bf.append("','").append(equ_name);
		bf.append("','").append(equ_id);
		bf.append("','").append(save_place);
		bf.append("',").append(status);
		if(mail_time!=null&&mail_time.length()>0){
			bf.append(",to_date('").append(mail_time).append("','yyyy-mm-dd')");
		}else{
			bf.append(",null,");
		}
		bf.append(",'").append(order_no).append("')");		
		DbComponent.exeUpdate(bf.toString());
		return resault;
	}
	/**
	 * 添加设备安装信息
	 * @param object
	 * @return
	 */
	public void insertEquInstall(RadioEquInstallBean bean)throws DbException{
		StringBuffer bf=new StringBuffer();
		bf.append("insert into radio_equ_install_tab (id,head_id,head_name,head_code,equ_name,equ_id,status,mail_time,order_no) values( radio_equ_install_seq.nextval");
		bf.append(",").append(bean.getHead_id());
		bf.append(",'").append(bean.getHead_name());
		bf.append("','").append(bean.getHead_code());
		bf.append("','").append(bean.getEqu_name());
		bf.append("','").append(bean.getEqu_id());
		bf.append("',").append(bean.getStatus());
		bf.append(",to_date('").append(bean.getMail_time()).append("','yyyy-mm-dd')");
		bf.append(",'").append(bean.getOrder_no()).append("')");		
		DbComponent.exeUpdate(bf.toString());
	}
	/**
	 * 修改设备安装信息集合
	 */
	public Object updateEquInstallList(ASObject obj){
		String resault="修改设备安装信息成功";
		ArrayList<RadioEquInstallBean> equList=(ArrayList<RadioEquInstallBean>)obj.get("equList");
		try {
			for(int i=0;i<equList.size();i++){
				updateEquInstall(equList.get(i));
			}
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("","添加设备安装信息异常："+e.getMessage(),"");
		}
		return resault;
	}
	/**
	 * 修改设备安装信息
	 * @param object
	 * @return
	 */
	public Object updateEquInstall(ASObject object){
		String resault="修改设备安装信息成功";
		String id=(String)object.get("id");
		String head_id=(String)object.get("head_id");
		String head_code=(String)object.get("head_code");
		String head_name=(String)object.get("head_name"); 
		String equ_id=(String)object.get("equ_id");
		String equ_name=(String)object.get("equ_name");
		String status=(String)object.get("status");
		String mail_time=(String)object.get("mail_time");
		String order_no=(String)object.get("order_no");
		StringBuffer bf=new StringBuffer();
		bf.append("update radio_equ_install_tab set ");
		bf.append(" head_id=").append(head_id);
		bf.append(",head_code='").append(head_code);
		bf.append("',head_name='").append(head_name);
		bf.append("',equ_id='").append(equ_id);
		bf.append("',equ_name='").append(equ_name);
		bf.append("',status=").append(status);
		bf.append(",mail_time=to_date('").append(mail_time.substring(0, 10)).append("','yyyy-mm-dd')");
		bf.append(",order_no='").append(order_no);
		bf.append("' where id=").append(id);
		try {
			DbComponent.exeUpdate(bf.toString());
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("","修改设备安装信息异常："+e.getMessage(),"");
		}
		return resault;
	}
	/**
	 * 修改设备安装信息
	 * @param object
	 * @return
	 * @throws DbException 
	 */
	public void updateEquInstall(RadioEquInstallBean bean) throws DbException{
		StringBuffer bf=new StringBuffer();
		bf.append("update radio_equ_install_tab set ");
		bf.append("head_id=").append(bean.getHead_id());
		bf.append(",head_code='").append(bean.getHead_code());
		bf.append("',head_name='").append(bean.getHead_name());
		bf.append("',equ_id='").append(bean.getEqu_id());
		bf.append("',equ_name='").append(bean.getEqu_name());
		bf.append("',save_place='").append(bean.getSave_place());
		bf.append("',status=").append(bean.getStatus());
		bf.append(",mail_time=to_date('").append(bean.getMail_time().substring(0,10)).append("','yyyy-mm-dd')");
		bf.append(",order_no='").append(bean.getOrder_no());
		bf.append("' where id=").append(bean.getId());
		DbComponent.exeUpdate(bf.toString());
		
		
	}
	/**
	 * 修改设备安装信息
	 * @param object
	 * @return
	 * @throws DbException 
	 */
	public Object updateEquInstallBean(RadioEquInstallBean bean){
		StringBuffer bf=new StringBuffer();
		bf.append("update radio_equ_install_tab set ");
		bf.append("head_id=").append(bean.getHead_id());
		bf.append(",head_code='").append(bean.getHead_code());
		bf.append("',head_name='").append(bean.getHead_name());
		bf.append("',equ_id='").append(bean.getEqu_id());
		bf.append("',equ_name='").append(bean.getEqu_name());
		bf.append("',save_place='").append(bean.getSave_place());
		bf.append("',status=").append(bean.getStatus());
		if(bean.getMail_time()!=null&&bean.getMail_time().length()>10){
			bf.append(",mail_time=to_date('").append(bean.getMail_time().substring(0,10)).append("','yyyy-mm-dd')");
		}
		bf.append(",order_no='").append(bean.getOrder_no());
		bf.append("' where id=").append(bean.getId());
		try {
			DbComponent.exeUpdate(bf.toString());
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return  new EXEException("", "后台出错", "");
		}
		return "修改信息成功";
		
	}
	/**
	 * 删除设备安装信息
	 * @param object
	 * @return
	 */
	public Object delEquList(ASObject object){
		String dellist=(String)object.get("dellist");
		try{
			
			String sql = "delete  from radio_equ_install_tab where id in("+dellist.replace("'","" )+")";
			
			DbComponent.exeUpdate(sql);
			
		 }catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new EXEException("","后台错误!",null);
		}
		return "删除信息成功";
	}
	public static String getInsertEquSql(String head_name,String head_id,String equ_id,String equ_name,String status,String save_place,String mail_time,String order_no ){
		StringBuffer bf=new StringBuffer();
		bf.append("insert into radio_equ_install_tab (id,head_id,head_name,equ_name,equ_id,save_place,status,mail_time,order_no) values( radio_equ_install_seq.nextval");
		bf.append(",").append(head_id);
		bf.append(",'").append(head_name);
		bf.append("','").append(equ_name);
		bf.append("','").append(equ_id);
		bf.append("','").append(save_place);
		bf.append("',").append(status);
		if(mail_time!=null&&mail_time.length()>0){
			bf.append(",to_date('").append(mail_time).append("','yyyy-mm-dd hh24:mi:ss')");
		}else{
			bf.append(",null");
		}
		if(order_no!=null&&order_no.length()!=0){
			bf.append(",'").append(order_no).append("')");
		}else {
			bf.append(",'')");
		}
		return bf.toString();
	}
	public static String getUpdateEquSql(String id,String status,String save_place,String mail_time,String order_no,String head_name ){
		StringBuffer bf=new StringBuffer();
		bf.append("update radio_equ_install_tab set ");
	
		if(status!=null&&status.length()!=0){
			bf.append(" status = '").append(status).append("',");
		}
		if(save_place!=null&&save_place.length()!=0){
			bf.append(" save_place = '").append(save_place).append("',");
		}
		if(order_no!=null&&order_no.length()!=0){
			bf.append(" order_no = '").append(order_no).append("',");
		}
		if(mail_time!=null&&mail_time.length()!=0){
			bf.append(" mail_time = to_date('").append(mail_time).append("','yyyy-mm-dd hh24:mi:ss'),");
		}
		bf.append(" head_name='").append(head_name).append("' where id=").append(id);
		return bf.toString();
	}
	public static String getEquInstallId(String head_name,String equ_id,String equ_name) throws DbException, GDSetException{
		String sql="select id from radio_equ_install_tab where head_name='"+head_name+"' and equ_id='"+equ_id+"' and equ_name='"+equ_name+"'";
		GDSet gd=DbComponent.Query(sql);
		if(gd.getRowCount()>0){
			return gd.getString(0, "id");
		}else {
			return "" ;
		}
	}
}
