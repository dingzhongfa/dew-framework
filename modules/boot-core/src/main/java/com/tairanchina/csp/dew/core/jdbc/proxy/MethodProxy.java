package com.tairanchina.csp.dew.core.jdbc.proxy;

import com.tairanchina.csp.dew.core.Dew;
import com.tairanchina.csp.dew.core.jdbc.annotations.Select;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created by 迹_Jason on 2017/7/26.
 */
public class MethodProxy implements InvocationHandler {

    private static Map<Method, MethodHandle> methodHandleCache = new ConcurrentReferenceHashMap<>(10, ConcurrentReferenceHashMap.ReferenceType.WEAK);

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!method.getDeclaringClass().isInterface() && !method.isDefault()) {
            // This proxy is class and method has not impl
            return method.invoke(this, args);
        } else if (method.isDefault()) {
            // This proxy is interface but method has impl
            return getMethodHandle(method).bindTo(proxy)
                    .invokeWithArguments(args);
        } else {
            // This proxy is interface
            return run(method, args);
        }
    }

    private MethodHandle getMethodHandle(Method method) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        MethodHandle handle = methodHandleCache.get(method);
        if (handle == null) {
            final Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class);
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            final Class<?> declaringClass = method.getDeclaringClass();
            handle = constructor.newInstance(declaringClass)
                    .unreflectSpecial(method, declaringClass);
            methodHandleCache.put(method, handle);
        }
        return handle;
    }

    /**
     * 实现接口的核心方法
     */
    public Object run(Method m, Object[] args) {
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
