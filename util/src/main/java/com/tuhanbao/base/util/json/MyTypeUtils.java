package com.tuhanbao.base.util.json;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.util.TypeUtils;
import com.tuhanbao.base.dataservice.ServiceBean;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class MyTypeUtils extends TypeUtils {
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> T cast(Object obj, Class<T> clazz, ParserConfig config) {
        if (obj == null) {
            return null;
        }

        if (clazz == null) {
            throw new IllegalArgumentException("clazz is null");
        }

        if (clazz == obj.getClass()) {
            return (T) obj;
        }
        
        //特殊处理
        if (ServiceBean.class.isAssignableFrom(clazz)) {
            if (obj instanceof String) {
                return getServiceBean(JSONObject.parseObject((String)obj), clazz);
            }
            if (obj instanceof Map) {
                return getServiceBean((Map<String, Object>)obj, clazz);
            }
        }

        if (obj instanceof Map) {
            if (clazz == Map.class) {
                return (T) obj;
            }

            Map map = (Map) obj;
            if (clazz == Object.class && !map.containsKey(JSON.DEFAULT_TYPE_KEY)) {
                return (T) obj;
            }

            return castToJavaBean((Map<String, Object>) obj, clazz, config);
        }

        if (clazz.isArray()) {
            if (obj instanceof Collection) {

                Collection collection = (Collection) obj;
                int index = 0;
                Object array = Array.newInstance(clazz.getComponentType(), collection.size());
                for (Object item : collection) {
                    Object value = cast(item, clazz.getComponentType(), config);
                    Array.set(array, index, value);
                    index++;
                }

                return (T) array;
            }

            if (clazz == byte[].class) {
                return (T) castToBytes(obj);
            }
        }

        if (clazz.isAssignableFrom(obj.getClass())) {
            return (T) obj;
        }

        if (clazz == boolean.class || clazz == Boolean.class) {
            return (T) castToBoolean(obj);
        }

        if (clazz == byte.class || clazz == Byte.class) {
            return (T) castToByte(obj);
        }

        if (clazz == short.class || clazz == Short.class) {
            return (T) castToShort(obj);
        }

        if (clazz == int.class || clazz == Integer.class) {
            return (T) castToInt(obj);
        }

        if (clazz == long.class || clazz == Long.class) {
            return (T) castToLong(obj);
        }

        if (clazz == float.class || clazz == Float.class) {
            return (T) castToFloat(obj);
        }

        if (clazz == double.class || clazz == Double.class) {
            return (T) castToDouble(obj);
        }

        if (clazz == String.class) {
            return (T) castToString(obj);
        }

        if (clazz == BigDecimal.class) {
            return (T) castToBigDecimal(obj);
        }

        if (clazz == BigInteger.class) {
            return (T) castToBigInteger(obj);
        }

        if (clazz == Date.class) {
            return (T) castToDate(obj);
        }

        if (clazz == java.sql.Date.class) {
            return (T) castToSqlDate(obj);
        }

        if (clazz == java.sql.Timestamp.class) {
            return (T) castToTimestamp(obj);
        }

        if (clazz.isEnum()) {
            return (T) castToEnum(obj, clazz, config);
        }

        if (Calendar.class.isAssignableFrom(clazz)) {
            Date date = castToDate(obj);
            Calendar calendar;
            if (clazz == Calendar.class) {
                calendar = Calendar.getInstance(JSON.defaultTimeZone, JSON.defaultLocale);
            } else {
                try {
                    calendar = (Calendar) clazz.newInstance();
                } catch (Exception e) {
                    throw new JSONException("can not cast to : " + clazz.getName(), e);
                }
            }
            calendar.setTime(date);
            return (T) calendar;
        }

        if (obj instanceof String) {
            String strVal = (String) obj;

            if (strVal.length() == 0 //
                || "null".equals(strVal) //
                || "NULL".equals(strVal)) {
                return null;
            }

            if (clazz == java.util.Currency.class) {
                return (T) java.util.Currency.getInstance(strVal);
            }
        }

        throw new JSONException("can not cast to : " + clazz.getName());
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj, Type type, ParserConfig mapping) {
        if (obj == null) {
            return null;
        }

        if (type instanceof Class) {
            return (T) cast(obj, (Class<T>) type, mapping);
        }

        if (type instanceof ParameterizedType) {
            return (T) cast(obj, (ParameterizedType) type, mapping);
        }

        if (obj instanceof String) {
            String strVal = (String) obj;
            if (strVal.length() == 0 //
                || "null".equals(strVal) //
                || "NULL".equals(strVal)) {
                return null;
            }
        }

        if (type instanceof TypeVariable) {
            return (T) obj;
        }

        throw new JSONException("can not cast to : " + type);
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T castToEnum(Object obj, Class<T> clazz, ParserConfig mapping) {
        try {
            if (obj instanceof Number) {
                int value = ((Number) obj).intValue();
                Object[] values = clazz.getEnumConstants();
                
                Field field = clazz.getDeclaredField("value");
                for (Object item : values) {
                    if (field.getInt(item) == value) {
                        return (T)item;
                    }
                }
            }
        } catch (Exception ex) {
        }
        return TypeUtils.castToEnum(obj, clazz, mapping);
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T getServiceBean(Map<String, Object> map, Class<T> clazz) {
        try {
            ParserConfig config = ParserConfig.getGlobalInstance();
            JavaBeanDeserializer javaBeanDeser = (JavaBeanDeserializer)config.getDeserializer(clazz);
            ServiceBean bean = (ServiceBean)javaBeanDeser.createInstance(null, clazz);
            
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
    
                FieldDeserializer fieldDeser = javaBeanDeser.smartMatch(key);
                if (fieldDeser == null) {
                    if (value instanceof JSON) bean.addFKBeanFromJson(key, (JSON)value);
                    continue;
                }
    
                Method method = fieldDeser.fieldInfo.method;
                if (method != null) {
                    Type paramType = method.getGenericParameterTypes()[0];
                    value = MyTypeUtils.cast(value, paramType, config);
                    method.invoke(bean, new Object[] { value });
                } else {
                    Field field = fieldDeser.fieldInfo.field;
                    Type paramType = fieldDeser.fieldInfo.fieldType;
                    value = TypeUtils.cast(value, paramType, config);
                    field.set(bean, value);
                }
            }
            
            return (T)bean;
        } catch (Exception e) {
            throw new JSONException(e.getMessage(), e);
        }
    }
}

