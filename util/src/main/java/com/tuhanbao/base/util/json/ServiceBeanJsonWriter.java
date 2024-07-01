package com.td.ca.base.util.json;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.annotation.JSONField;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.td.ca.base.dataservice.ServiceBean;
import com.td.ca.base.util.db.table.Column;
import com.td.ca.base.util.db.table.ServiceBeanKeyManager;
import com.td.ca.base.util.db.table.Table;
import com.td.ca.base.util.db.table.data.AbstractDataValue;
import com.td.ca.base.util.exception.AppException;
import com.td.ca.base.util.log.LogUtil;
import com.td.ca.base.util.objutil.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ServiceBeanJsonWriter implements ObjectWriter {

    @Override
    public void write(JSONWriter jsonWriter, Object object, Object fieldName, Type fieldType, long features) {
        jsonWriter.write(serviceBean2JSON((ServiceBean) object));
    }

    public static JSONObject serviceBean2JSON(ServiceBean sb) {
        JSONObject json = toJson(sb);

        if (sb.getClass() != ServiceBean.class) {
            try {
                Map<String, Object> fieldValues = getAllValues(sb);
                for (Map.Entry<String, Object> entry : fieldValues.entrySet()) {
                    json.put(entry.getKey(), JSONUtil.toJSON(entry.getValue()));
                }
            } catch (Exception e) {
                log.error(LogUtil.getMessage(e));
                throw new AppException("toJSON error");
            }
        }

        return json;
    }

    /**
     * 用于解析serviceBean中自定义的属性
     * ServiceBean的处理比较特殊，需要自定义
     *
     * @param sb
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private static Map<String, Object> getAllValues(ServiceBean sb)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Map<String, Object> fieldValues = new LinkedHashMap<String, Object>();
        Class<?> myClass = sb.getClass();

        Class<?>[] interfaces = myClass.getInterfaces();
        if (interfaces != null) {
            for (Class<?> item : interfaces) {
                addDefaultMethodValue(sb, item, fieldValues);
            }
        }

        addMethodValue(sb, myClass, fieldValues);

        for (Field field : myClass.getFields()) {
            if (field.getDeclaringClass() == myClass) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                if (!Modifier.isPublic(field.getModifiers())) {
                    continue;
                }

                String propertyName = field.getName();

                if (!fieldValues.containsKey(propertyName)) {
                    fieldValues.put(propertyName, field.get(sb));
                }
            }
        }

        return fieldValues;
    }

    private static void addDefaultMethodValue(ServiceBean sb, Class<?> myClass, Map<String, Object> fieldValues) throws IllegalAccessException, InvocationTargetException {
        addMethodValue(sb, myClass, fieldValues, true);
    }
    private static void addMethodValue(ServiceBean sb, Class<?> myClass, Map<String, Object> fieldValues) throws InvocationTargetException, IllegalAccessException {
        addMethodValue(sb, myClass, fieldValues, false);
    }

    private static void addMethodValue(ServiceBean sb, Class<?> myClass, Map<String, Object> fieldValues, boolean isInterface) throws InvocationTargetException, IllegalAccessException {
        for (Method method : myClass.getDeclaredMethods()) {
            JSONField annotation = method.getAnnotation(JSONField.class);

            if (annotation == null) {
                annotation = getSuperMethodAnnotation(myClass, method);
            }

            if (annotation != null && !annotation.serialize()) {
                continue;
            }

            String propertyName = null;
            if (isInterface && !method.isDefault()) {
                continue;
            }
            if (Modifier.isStatic(method.getModifiers())) {
                continue;
            }
            if (method.getReturnType().equals(Void.TYPE)) {
                continue;
            }
            if (method.getParameterTypes().length != 0) {
                continue;
            }
            if (method.getReturnType() == ClassLoader.class) {
                continue;
            }
            String methodName = method.getName();
            if (methodName.startsWith("get")) {
                if (method.getName().equals("getMetaClass") && method.getReturnType().getName().equals("groovy.lang.MetaClass")) {
                    continue;
                }
                if (methodName.length() < 4) {
                    continue;
                }
                if (methodName.equals("getClass")) {
                    continue;
                }
                char c3 = methodName.charAt(3);
                if (Character.isUpperCase(c3) || c3 > 512) {
                    propertyName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
                }
            }

            if (methodName.startsWith("is")) {
                if (methodName.length() < 3) {
                    continue;
                }
                char c2 = methodName.charAt(2);
                if (Character.isUpperCase(c2)) {
                    propertyName = Character.toLowerCase(methodName.charAt(2)) + methodName.substring(3);
                }
            }
            if (!StringUtil.isEmpty(propertyName)) {
                try {
                    fieldValues.put(propertyName, method.invoke(sb));
                } catch (Exception e) {
                    log.error("{} set error : {}", propertyName, e.getMessage());
                }
            }
        }
    }

    /**
     * 请不要使用此方法转化json，
     *
     * @return
     */
    public static JSONObject toJson(ServiceBean serviceBean) {
        Map<String, Object> values = new HashMap<String, Object>();
        for (Map.Entry<Column, AbstractDataValue> entry : serviceBean.getProperties().entrySet()) {
            AbstractDataValue value = entry.getValue();
            if (value != null) {
                values.put(entry.getKey().getAliasName(), value.getValue());
            }
        }
        JSONObject json = (JSONObject) JSONUtil.toJSON(values);

        if (serviceBean.getFkBeans() != null) {
            for (Map.Entry<String, List<ServiceBean>> entry : serviceBean.getFkBeans().entrySet()) {
                putJson(json, serviceBean.getTable(), entry.getKey(), entry.getValue());
            }
        }
        return json;
    }

    private static void putJson(JSONObject json, Table table, String key, List<ServiceBean> beans) {
        if (beans == null || beans.isEmpty()) {
            return;
        }

        ServiceBean childBean = beans.get(0);
        if (ServiceBeanKeyManager.isSingleKey(table, key)) {
            // 外键关系只存在1对1,多对1，比如A表的B_ID外键关联B表，B_ID必须为B的唯一属性（在框架系统里面，只能是主键）
            json.put(key, JSONUtil.toJSON(childBean));
        } else {
            // 关系表一定会走到此处，关系表一定是N:N的关系
            if (childBean.getMoTable().isRelationTable()) {
                Map<String, List<ServiceBean>> fkBeans = childBean.getFkBeans();
                if (fkBeans != null && fkBeans.size() > 0) {
                    String childKey = fkBeans.keySet().stream().findFirst().get();

                    List<ServiceBean> newChildBeans = new ArrayList<>();
                    for (ServiceBean rtBean : beans) {
                        // TODO 需要serviceBean提供方法getFkBean(String key)改成public
                        newChildBeans.addAll(getFkBean(rtBean, childKey));
                    }

                    json.put(childKey + "s", JSONUtil.toJSON(newChildBeans));
                    return;
                }
            }

            json.put(key, JSONUtil.toJSON(beans));
        }
    }

    private static List<ServiceBean> getFkBean(ServiceBean rtBean, String key) {
        Map<String, List<ServiceBean>> fkBeans = rtBean.getFkBeans();
        if (fkBeans == null) {
            return null;
        }
        return fkBeans.get(key);
    }

    private static JSONField getSuperMethodAnnotation(final Class<?> clazz, final Method method) {
        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces.length > 0) {
            Class<?>[] types = method.getParameterTypes();
            for (Class<?> interfaceClass : interfaces) {
                for (Method interfaceMethod : interfaceClass.getMethods()) {
                    Class<?>[] interfaceTypes = interfaceMethod.getParameterTypes();
                    if (interfaceTypes.length != types.length) {
                        continue;
                    }
                    if (!interfaceMethod.getName().equals(method.getName())) {
                        continue;
                    }
                    boolean match = true;
                    for (int i = 0; i < types.length; ++i) {
                        if (!interfaceTypes[i].equals(types[i])) {
                            match = false;
                            break;
                        }
                    }
                    if (!match) {
                        continue;
                    }
                    JSONField annotation = interfaceMethod.getAnnotation(JSONField.class);
                    if (annotation != null) {
                        return annotation;
                    }
                }
            }
        }
        Class<?> superClass = clazz.getSuperclass();
        if (superClass == null) {
            return null;
        }
        if (Modifier.isAbstract(superClass.getModifiers())) {
            Class<?>[] types = method.getParameterTypes();
            for (Method interfaceMethod : superClass.getMethods()) {
                Class<?>[] interfaceTypes = interfaceMethod.getParameterTypes();
                if (interfaceTypes.length != types.length) {
                    continue;
                }
                if (!interfaceMethod.getName().equals(method.getName())) {
                    continue;
                }
                boolean match = true;
                for (int i = 0; i < types.length; ++i) {
                    if (!interfaceTypes[i].equals(types[i])) {
                        match = false;
                        break;
                    }
                }
                if (!match) {
                    continue;
                }
                JSONField annotation = interfaceMethod.getAnnotation(JSONField.class);
                if (annotation != null) {
                    return annotation;
                }
            }
        }
        return null;
    }
}
