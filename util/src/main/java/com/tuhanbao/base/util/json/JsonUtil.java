package com.tuhanbao.base.util.json;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.JavaBeanSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.util.TypeUtils;
import com.tuhanbao.base.dataservice.ServiceBean;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.objutil.StringUtil;

/**
 * Created by tuhanbao
 */
@SuppressWarnings("unchecked")
public class JsonUtil {
//    /**
//     * @param args
//     */
//    public static void main(String args[]) {
//        ArrayList<ServiceBean> dataList = new ArrayList<ServiceBean>();
//        Pagination p = new Pagination(100, dataList);
//        Area e = new Area();
//        e.setAreaLevelint(2);
//        e.setId(1);
//        dataList.add(e);
//
//        e = new Area();
//        e.setAreaLevelint(3);
//        e.setId(2);
//        e.setParentId(1);
//        dataList.add(e);
//        System.out.println(toJSON(p));
//    }

    public static Object toJSON(Object javaObject) {
        SerializeConfig config = SerializeConfig.globalInstance;
        if (javaObject == null) {
            return null;
        }

        if (javaObject instanceof JSON) {
            return javaObject;
        }

        if (javaObject instanceof ServiceBean) {
            return serviceBean2JSON((ServiceBean)javaObject);
        }

        if (javaObject instanceof Map) {
            Map<Object, Object> map = (Map<Object, Object>)javaObject;

            JSONObject json = new JSONObject(map.size());

            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                Object key = entry.getKey();
                String jsonKey = TypeUtils.castToString(key);
                Object jsonValue = toJSON(entry.getValue());
                json.put(jsonKey, jsonValue);
            }

            return json;
        }

        if (javaObject instanceof Collection) {
            Collection<Object> collection = (Collection<Object>)javaObject;

            JSONArray array = new JSONArray(collection.size());

            for (Object item : collection) {
                Object jsonValue = toJSON(item);
                array.add(jsonValue);
            }

            return array;
        }

        Class<?> clazz = javaObject.getClass();

        if (clazz.isEnum()) {
            return ((Enum<?>)javaObject).name();
        }

        if (clazz.isArray()) {
            int len = Array.getLength(javaObject);

            JSONArray array = new JSONArray(len);

            for (int i = 0; i < len; ++i) {
                Object item = Array.get(javaObject, i);
                Object jsonValue = toJSON(item);
                array.add(jsonValue);
            }

            return array;
        }

        if (ParserConfig.isPrimitive(clazz)) {
            return javaObject;
        }

        ObjectSerializer serializer = config.getObjectWriter(clazz);
        if (serializer instanceof JavaBeanSerializer) {
            JavaBeanSerializer javaBeanSerializer = (JavaBeanSerializer)serializer;

            JSONObject json = new JSONObject();
            try {
                Map<String, Object> values = javaBeanSerializer.getFieldValuesMap(javaObject);
                for (Map.Entry<String, Object> entry : values.entrySet()) {
                    json.put(entry.getKey(), toJSON(entry.getValue()));
                }
            }
            catch (Exception e) {
                throw new JSONException("toJSON error", e);
            }
            return json;
        }

        String text = JSON.toJSONString(javaObject);
        return JSON.parse(text);
    }

    public static JSONObject serviceBean2JSON(ServiceBean sb) {
        JSONObject json = sb.toJson();

        try {
            Map<String, Object> filedValues = getAllValues(sb);
            for (Map.Entry<String, Object> entry : filedValues.entrySet()) {
                json.put(entry.getKey(), toJSON(entry.getValue()));
            }
        }
        catch (Exception e) {
            throw new MyException("toJSON error");
        }

        return json;
    }

    /**
     * 从ali json移植的一段代码，用于解析serviceBean中自定义的属性
     * @param sb
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static Map<String, Object> getAllValues(ServiceBean sb)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Map<String, Object> filedValues = new LinkedHashMap<String, Object>();
        Class<?> myClass = sb.getClass();
        for (Method method : myClass.getMethods()) {
            JSONField annotation = method.getAnnotation(JSONField.class);

            if (annotation == null) {
                annotation = TypeUtils.getSuperMethodAnnotation(myClass, method);
            }

            if (annotation != null && !annotation.serialize()) {
                    continue;
            }
            
            String propertyName = null;
            if (method.getDeclaringClass() == myClass) {
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
                    filedValues.put(propertyName, method.invoke(sb));
                }
            }
        }

        for (Field field : myClass.getFields()) {
            if (field.getDeclaringClass() == myClass) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                if (!Modifier.isPublic(field.getModifiers())) {
                    continue;
                }

                String propertyName = field.getName();

                if (!filedValues.containsKey(propertyName)) {
                    filedValues.put(propertyName, field.get(sb));
                }
            }
        }

        return filedValues;
    }
    
//    public static void main(String args[]) {
//        String s = "{\"id\":1,\"nick_name\":\"aaa\",\"dealers\":[{\"id\":1,\"detail_address\":\"武侯区\",\"area_id\":10,\"mobile_phone\":\"18642832213\",\"create_date\":1477584000000,\"status\":1,\"user_id\":1,\"dealer_name\":\"aaa\",\"is_use\":1,\"parent_id\":12}],\"authority\":\"CUSTOMER\",\"create_date\":1477584000000,\"token\":\"CUSTOMER_1_11-01 16:43:56 172\",\"userId\":1,\"login_name\":\"18642832213\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\"}";
//        User user = getBean(s, User.class);
//        List<Dealer> list = new ArrayList<Dealer>();
//        Dealer e = new Dealer();
//        e.setAreaId(1);
//        list.add(e);
//        user.addFKBean(TableConstants.T_DEALER.USER_ID, list);
//        System.out.println(JsonUtil.toJSON(user));
//    }

    public static <T> T getBean(Object obj, Class<T> clazz) {
        return MyTypeUtils.cast(obj, clazz, ParserConfig.getGlobalInstance());
    }
    
    public static <T> List<T> getServiceBeans(JSONArray json, Class<T> clazz) {
        List<T> list = new ArrayList<T>();
        for (Object item : json) {
            list.add(getBean(item, clazz));
        }
        return list;
    }
}