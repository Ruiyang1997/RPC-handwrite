package com.ry.rpc.common.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

//反射工具类
//clazz 带创建对象的类
//<T> 对象类型
//return 创建好的类
public class ReflectionUtils {
    public static <T> T newInstance(Class<T> clazz){
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    //获取某一个类所有的公共方法
    public static Method[] getPublicMethods(Class clazz){
        Method[] methods = clazz.getDeclaredMethods();
        List<Method> pmethods = new ArrayList<>();
        for(Method m : methods){//过滤为公共方法
            if(Modifier.isPublic(m.getModifiers())){
                pmethods.add(m);
            }
        }
        return pmethods.toArray(new Method[0]);//返回方法至list
    }

    //调用某个对象指定方法的工具方法
    public static Object invoke(Object obj, Method method, Object... args){
        try {
            return method.invoke(obj, args);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
