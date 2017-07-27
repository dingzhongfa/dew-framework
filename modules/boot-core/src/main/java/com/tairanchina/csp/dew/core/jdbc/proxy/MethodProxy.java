package com.tairanchina.csp.dew.core.jdbc.proxy;

import com.tairanchina.csp.dew.core.Dew;
import com.tairanchina.csp.dew.core.jdbc.annotations.Select;
import org.springframework.cglib.proxy.InvocationHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by 迹_Jason on 2017/7/26.
 */
public class MethodProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)  throws Throwable {
        //如果传进来是一个已实现的具体类
        if (Object.class.equals(method.getDeclaringClass())) {
            try {
                return method.invoke(this, args);
            } catch (Throwable t) {
                t.printStackTrace();
            }
            //如果传进来的是一个接口
        } else {
            return run(method, args);
        }
        return null;
    }

    /**
     * 实现接口的核心方法
     * @param m
     * @param args
     * @return
     */
    public Object run(Method m,Object[] args){
        MethodConstruction method = new MethodConstruction(m, args);
        for (Annotation annotation : method.getMethodAnnotations()) {
            if (annotation instanceof Select) {
                if (method.flagOfPaging()) {
                    return Dew.ds().selectForPaging(((Select) annotation).entityClass(), method, ((Select) annotation).value());
                } else {
                    List list = Dew.ds().selectForList(((Select) annotation).entityClass(), method.getParamsMap(), ((Select) annotation).value());
                    if (!method.getReturnType().isAssignableFrom(List.class)) {
                        return list.size() > 0 ? list.get(0) : null;
                    } else {
                        return list;
                    }
                }
            }
        }
        return null;
    }

}
