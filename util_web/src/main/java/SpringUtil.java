package com.td.ca.web.util.spring;

import com.td.ca.base.util.clazz.ClazzUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * @author ywx1101589
 * @date 2023/11/6 9:51
 * @description
 */
@Slf4j
@Component
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    private static String serviceName;

    @Override
    public void setApplicationContext(@Nullable ApplicationContext context) throws BeansException {
        SpringUtil.context = context;
    }

    public static String getServiceName() {
        return serviceName;
    }

    public static void setServiceName(String serviceName) {
        SpringUtil.serviceName = serviceName;
    }

    /**
     * 获取bean
     *
     * @param clazz class类
     * @param <T>   泛型
     * @return T
     */
    public static <T> T getBean(Class<T> clazz) {
        if (clazz == null) {
            return null;
        }
        return context.getBean(clazz);
    }

    /**
     * 获取bean
     *
     * @param beanId beanId
     * @param <T>    泛型
     * @return T
     */
    public static <T> T getBean(String beanId) {
        if (beanId == null) {
            return null;
        }
        return (T) context.getBean(beanId);
    }

    /**
     * 获取bean
     *
     * @param beanName bean名称
     * @param clazz    class类
     * @param <T>      泛型
     * @return T
     */
    public static <T> T getBean(String beanName, Class<T> clazz) {
        if (null == beanName || "".equals(beanName.trim())) {
            return null;
        }
        if (clazz == null) {
            return null;
        }
        return (T) context.getBean(beanName, clazz);
    }

    /**
     * 获取 ApplicationContext
     *
     * @return ApplicationContext
     */
    public static ApplicationContext getContext() {
        if (context == null) {
            return null;
        }
        return context;
    }

    /**
     * 发布事件
     *
     * @param event 事件
     */
    public static void publishEvent(ApplicationEvent event) {
        if (context == null) {
            return;
        }
        try {
            context.publishEvent(event);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
    public static void registerBeanDefinition(Class<?> targetClass) {
        registerBeanDefinition(getBeanName(targetClass), targetClass);
    }

    /**
     * 动态注册Bean，而且class的注解会生效，比如Transactional
     *
     * @param beanName    bean名称
     * @param targetClass bean对应的类
     */
    public static void registerBeanDefinition(String beanName, Class<?> targetClass) {
        //获取BeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory =
                (DefaultListableBeanFactory) context.getAutowireCapableBeanFactory();
        //创建bean信息.
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(targetClass);
        //动态注册bean.
        defaultListableBeanFactory.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
    }

    public static void unRegisterBeanDefinition(Class<?> targetClass) {
        unRegisterBeanDefinition(getBeanName(targetClass));
    }

    private static String getBeanName(Class<?> targetClass) {
        return ClazzUtil.firstCharLowerCase(targetClass.getSimpleName());
    }

    /**
     * 动态卸载Bean
     *
     * @param beanName bean名称
     */
    public static void unRegisterBeanDefinition(String beanName) {
        if (!context.containsBean(beanName)) {
            return;
        }
        //获取BeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory =
                (DefaultListableBeanFactory) context.getAutowireCapableBeanFactory();
        defaultListableBeanFactory.removeBeanDefinition(beanName);
    }

    /**
     * 动态注册Controller
     *
     * @param controllerBeanName controller的beanName
     * @throws Exception 反射异常
     */
    public static void registerController(String controllerBeanName)
            throws Exception {
        final RequestMappingHandlerMapping requestMappingHandlerMapping =
                context.getBean(RequestMappingHandlerMapping.class);
        if (requestMappingHandlerMapping != null) {
            Object controller = context.getBean(controllerBeanName);
            if (controller == null) {
                return;
            }
            //注册Controller
            Method method = requestMappingHandlerMapping.getClass().getSuperclass().getSuperclass().
                    getDeclaredMethod("detectHandlerMethods", Object.class);
            //将private改为可使用
            method.setAccessible(true);
            method.invoke(requestMappingHandlerMapping, controllerBeanName);
        }
    }

    /**
     * 动态去掉Controller的Mapping
     *
     * @param controllerBeanName controller的beanName
     */
    public static void unregisterController(String controllerBeanName) {
        final RequestMappingHandlerMapping requestMappingHandlerMapping
                = (RequestMappingHandlerMapping) context.getBean("requestMappingHandlerMapping");
        if (requestMappingHandlerMapping != null) {
            Object controller = context.getBean(controllerBeanName);
            if (controller == null) {
                return;
            }
            final Class<?> targetClass = controller.getClass();
            ReflectionUtils.doWithMethods(targetClass, method -> {
                Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
                try {
                    Method createMappingMethod = RequestMappingHandlerMapping.class.
                            getDeclaredMethod("getMappingForMethod", Method.class, Class.class);
                    createMappingMethod.setAccessible(true);
                    RequestMappingInfo requestMappingInfo = (RequestMappingInfo)
                            createMappingMethod.invoke(requestMappingHandlerMapping, specificMethod, targetClass);
                    if (requestMappingInfo != null) {
                        requestMappingHandlerMapping.unregisterMapping(requestMappingInfo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, ReflectionUtils.USER_DECLARED_METHODS);
        }
    }

}
