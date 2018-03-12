package org.jmask.web.util;

import java.lang.reflect.Method;
import java.util.Hashtable;

import com.viewscenes.util.LogTool;

/**
 * �෽������ע�ᡣ�ݲ�֧����д�����Ĳ�����ȡ��Ҫ����ķ�����Ψһ��
 * <p>�������������ǻ��ڷ�����û��ƣ����ԣ�Ϊ�˿��ٵ�ȷ��һ��������ԭ�ͣ����Կ��ǽ��Ѿ�ȷ�����ķ���ԭ������һ����̬���򣬿�����߷����ٶȡ�</p>
 * @author user
 *
 */

public class ClassRegister {

        private static Hashtable<String, String[]> classRegist = new Hashtable <String, String[]> ();

        /**
         * ����ָ�������Ĳ��������б�
         * @param className ����
         * @param methodName ������
         * @return String[]
         */
        public static String[] getMethodParaType(String className, String methodName){

                String classMethodName = className + "." + methodName;

                String[] methodParas = classRegist.get(classMethodName);

                if( methodParas != null){

                        return methodParas;

                }

                Class cla = null;

                try {

                        cla = Class.forName(className);

                        Method[] methods = cla.getMethods();

                        for(int i = 0; i < methods.length; i++){

                                String method = methods[i].getName();

                                if(methodName.equals(method)){

                                        Class parameterTypes[] = methods[i].getParameterTypes();

                                        methodParas = new String[parameterTypes.length];

                                        for(int t = 0; t < parameterTypes.length; t++){

                                                methodParas[t] = parameterTypes[t].getName();

                                        }

                                        classRegist.put(classMethodName, methodParas);

                                        break;

                                }

                        }

                } catch (Exception e) {

                        LogTool.warning("ClassRegister:getMethodParameter()::" + e.getClass().getName() + "--" + e.toString() );
                }

                return methodParas;

        }

}
