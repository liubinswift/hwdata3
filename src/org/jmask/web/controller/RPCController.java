package org.jmask.web.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import com.viewscenes.web.sysmgr.user.OnlineUserManager;

import flex.messaging.FlexContext;
import flex.messaging.util.MethodMatcher;

public class RPCController {
    public Object getResult(String mid,String cla,String fun,Object[] param,Boolean isLong){
        Object ret=null;
        if(!fun.equals("login")){
            if(OnlineUserManager.testLog()){
                ret= new EXEException("", "��������Ͽ�����,�����µ�¼", null);
                return ret;
            }
        }
        if(isLong){
            processThread pt=new processThread(mid, cla, fun, param,FlexContext.getFlexClient().getId());
            pt.start();
        }else{
            try {
                Class<?> cl = Class.forName(cla);
                Object obj = cl.newInstance();
                Method method=null;
                try{
                    MethodMatcher mm=new MethodMatcher();
                    method =mm.getMethod(cl, fun, Arrays.asList(param));

                }catch(Exception e){
                    //���䷽����ת����������
                    ret=new EXEException("",e.getMessage(),null);
                    e.printStackTrace();
                }
                if(method!=null){
                    ret=method.invoke(obj, param);
                }

            } catch (SecurityException e) {
                ret=new EXEException("",e.getMessage(),null);
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                //�������ͳ���
                ret=new EXEException("",e.getMessage(),null);
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                ret=new EXEException("",e.getMessage(),null);
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                //ִ�г���
                ret=new EXEException("",e.getMessage(),null);
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                //�Ҳ�����
                ret=new EXEException("",e.getMessage(),null);
                e.printStackTrace();
            } catch (InstantiationException e) {
                //ʵ��������
                ret=new EXEException("",e.getMessage(),null);
                e.printStackTrace();
            } catch(Exception e){
                ret=new EXEException("",e.getMessage(),null);
                e.printStackTrace();
            }
        }
        return ret;
    }
}
