package com.viewscenes.task;

import com.viewscenes.pub.GDSet;
import com.viewscenes.dao.database.DbComponent;

abstract public class TaskProcessor{
     public String task_id,head_code;
    abstract public boolean process();//����Ļ�������

    //��¼��Ϣʧ�ܵ�ǰ��
    public static synchronized void recordError(TaskProcessor task){
        String sql = "select * from task_batch_tab where task_id = "+task.task_id;
        try{
            GDSet gd = DbComponent.Query(sql);
            String errorHeadend = gd.getString(0,"error_headend");
            if(errorHeadend.equals(""))
                errorHeadend = task.head_code;
            else
                errorHeadend += ","+task.head_code;
            String updateSql = "update task_batch_tab set error_headend = '"+errorHeadend+"' where task_id = "+task.task_id;
            DbComponent.exeUpdate(updateSql);
        }catch(Exception ex){

        }
    }
}
