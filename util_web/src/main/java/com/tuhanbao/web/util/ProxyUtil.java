package com.tuhanbao.web.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.util.ClassUtils;

public class ProxyUtil {
    /**
     * 获取目标对象
     * 
     * @param proxy 代理对象
     * @return
     * @throws Exception
     */
    public static Object getTarget(Object proxy) throws Exception {
        boolean isJdkDynamicProxy = isJdkDynamicProxy(proxy);;
        boolean isCglibProxy = isCglibProxyProxy(proxy);
        
        if (isJdkDynamicProxy) {
            return getJdkDynamicProxyTargetObject(proxy);
        } else if (isCglibProxy) {
            return getCglibProxyTargetObject(proxy);
        } else {
            return proxy;
        }
    }

    /**
     * 获取代理对象
     * 
     * @param targrt
     * @param handle
     * @return
     */
    public static Object getProxy(Object targrt, InvocationHandler handle) {
        Object proxy = Proxy.newProxyInstance(targrt.getClass().getClassLoader(), targrt.getClass().getInterfaces(), handle);
        return proxy;
    }

    public static boolean isJdkDynamicProxy(Object object) {
        if (Proxy.isProxyClass(object.getClass())) {
            return true;
        } else {
            return false;
        }
    }
    
    private static boolean isCglibProxyProxy(Object object) {
        if (ClassUtils.isCglibProxyClass(object.getClass())) {
            return true;
        } else {
            return false;
        }
    }

    private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);
        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        Object target = ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
        return target;
    }

    private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy) h.get(proxy);
        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        Object target = ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();
        return target;
    }
}
