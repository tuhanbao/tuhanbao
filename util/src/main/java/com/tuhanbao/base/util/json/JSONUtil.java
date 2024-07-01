package com.td.ca.base.util.json;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.TypeReference;
import com.alibaba.fastjson2.util.TypeUtils;
import com.td.ca.base.Constants;
import com.td.ca.base.dataservice.CutTableServiceBean;
import com.td.ca.base.dataservice.ServiceBean;
import com.td.ca.base.util.clazz.ClazzUtil;
import com.td.ca.base.util.db.table.Column;
import com.td.ca.base.util.db.table.data.IntValue;
import com.td.ca.base.util.db.table.data.LongValue;
import com.td.ca.base.util.db.table.data.StringValue;
import com.td.ca.base.util.exception.AppException;
import com.td.ca.base.util.io.codegenarator.table.DataType;
import com.td.ca.base.util.objutil.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author tuhanbao
 *
 */
@Slf4j
@SuppressWarnings({"unchecked", "deprecation"})
public class JSONUtil {

    public static final String GAP = "_";

    private static final ServiceBeanJsonWriter SERVICE_BEAN_JSON_WRITER = new ServiceBeanJsonWriter();

    /**
     * 为了处理servicebean和自定义枚举的序列化，需要注册一些序列化解析器
     */
    public static void initProjectJsonReaderAndWriter(String packagePath) {
        // 必须先注册枚举，再注册serviceBean
        initEnumJsonReaderAndWriter(packagePath + ".api.constants.enums");
        initServiceBeanJsonWriter(packagePath + ".api.model");
    }

    public static void initEnumJsonReaderAndWriter(String packagePath) {
        try {
            Set<Class<?>> classSet = ClazzUtil.getAllClasses(packagePath, item -> {
                boolean isEnum = item.isEnum();
                return isEnum && ClazzUtil.getVar(item, "value") != null;
            });
            for (Class<?> clazz : classSet) {
                registerEnum(clazz);
            }
        } catch (Exception e) {
            throw AppException.getAppException(e);
        }
    }

    public static void registerEnum(Class<?> clazz) {
        JSON.register(clazz, new EnumJsonReader(clazz));
        JSON.register(clazz, new EnumJsonWriter(clazz));
    }

    public static void initServiceBeanJsonWriter(String packagePath) {
        try {
            Set<Class<?>> classSet = ClazzUtil.getAllClasses(packagePath, item -> {
                boolean isServiceBean = ServiceBean.class.isAssignableFrom(item);
                // MO直接继承ServiceBean
                Class<?> superclass = item.getSuperclass();
                boolean isMo =
                        ServiceBean.class.equals(superclass) || CutTableServiceBean.class.equals(superclass);
                return isServiceBean && !isMo;
            });
            for (Class<?> clazz : classSet) {
                registerServiceBean(clazz);
            }
        } catch (Exception e) {
            throw AppException.getAppException(e);
        }
    }

    public static void registerServiceBean(Class<?> clazz) {
        JSON.register(clazz, SERVICE_BEAN_JSON_WRITER);
        // fastjson bug: 如果不先parse一下， 自定义枚举的反序列化器不会生效
        try {
            ServiceBean sb = (ServiceBean) clazz.getDeclaredConstructor().newInstance();
            for (Column col : sb.getTable().getColumns()) {
                if (col.getDataType() == DataType.STRING) {
                    sb.getProperties().put(col, StringValue.valueOf("0"));
                }
                if (col.getDataType() == DataType.INT) {
                    sb.getProperties().put(col, IntValue.valueOf(0));
                }
                if (col.getDataType() == DataType.LONG) {
                    sb.getProperties().put(col, LongValue.valueOf(1L));
                }
            }
            JSONUtil.parseObject(JSONUtil.toJSONString(sb), clazz);
        } catch (Exception e) {
            log.error("error", e);
        }
    }

    public static Object toJSON(Object javaObject) {
        return JSON.parse(JSON.toJSONString(javaObject, JSONWriter.Feature.WriteNonStringKeyAsString));
    }

    /**
     * 允许使用.来关联对象
     * layDataOneLevel的逆操作
     */
    public static <T> T getBeanWithGap(String json, Class<T> clazz) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        handleJsonWithGap(jsonObject);
        return getBean(jsonObject, clazz);
    }

    private static void handleJsonWithGap(JSONObject jsonObject) {
        handleJsonWithGap(jsonObject, GAP);
    }

    private static void handleJsonWithGap(JSONObject jsonObject, String gap) {
        Set<String> keySet = jsonObject.keySet();
        List<String> newKeys = new ArrayList<>(keySet);
        for (String key : newKeys) {
            Object value = jsonObject.get(key);
            if (value instanceof JSONObject) {
                handleJsonWithGap((JSONObject) value);
            } else if (value instanceof JSONArray) {
                for (Object item : (JSONArray) value) {
                    if (item instanceof JSONObject) {
                        handleJsonWithGap((JSONObject) item);
                    }
                }
            }

            if (key.contains(gap)) {
                String[] nameArray = StringUtil.string2Array(key, gap);
                getParentJson(jsonObject, nameArray).put(nameArray[nameArray.length - 1], value);
                jsonObject.remove(key);
            }
        }
    }

    private static JSONObject getParentJson(JSONObject jsonObject, String[] nameArray) {
        JSONObject parentBean = jsonObject;
        for (int i = 0; i < nameArray.length - 1; i++) {
            String key = nameArray[i];
            if (jsonObject.containsKey(key)) {
                parentBean = parentBean.getJSONObject(key);
            } else {
                JSONObject child = new JSONObject();
                parentBean.put(key, child);
                parentBean = child;
            }
        }
        return parentBean;
    }

    /**
     * 本方法为了通用，如果能准确知道clazz的类型，尽量调用更为精准的parseObject, parseArray等方法
     */
    public static <T> T getBean(Object obj, Class<T> clazz) {
        if (obj == null) {
            return null;
        }
        if (clazz.isAssignableFrom(obj.getClass())) {
            return (T) obj;
        }
        // 原生的方法，obj如果是string, 或者不是map，会报错
        if (obj instanceof String) {
            obj = parse((String) obj);
        }
        if (!(obj instanceof JSONObject)) {
            obj = toJSON(obj);
        }

        T result;
        if (obj instanceof JSONObject) {
            // fastjson2默认不支持smart match， account_id没法set到accountId中
            result = ((JSONObject) obj).to(clazz, JSONReader.Feature.SupportSmartMatch);
        } else {
            result = TypeUtils.cast(obj, clazz, JSONFactory.getDefaultObjectReaderProvider());
        }

        if (result instanceof ServiceBean) {
            ((ServiceBean) result).clearNullValue();
        }
        return result;
    }

    public static <T> T getBean(Object obj, Type t) {
        if (t instanceof Class) {
            return getBean(obj, (Class<T>) t);
        }

        if (obj instanceof String) {
            obj = parse((String) obj);
        }
        return TypeUtils.cast(obj, t);
    }

    public static <T> List<T> getArray(JSONArray json, Class<T> clazz) {
        List<T> list = new ArrayList<T>();
        for (Object item : json) {
            list.add(getBean(item, clazz));
        }
        return list;
    }

    public static JSONArray parseArray(String text) {
        return JSON.parseArray(text, JSONReader.Feature.SupportSmartMatch);
    }

    public static <T> List<T> parseArray(String text, Class<T> clazz) {
        return JSON.parseArray(text, clazz, JSONReader.Feature.SupportSmartMatch);
    }

    public static void layDataOneLevel(JSONObject jsonObject) {
        layDataOneLevel(jsonObject, GAP);
    }

    public static void layDataOneLevel(JSONObject jsonObject, String gap) {
        Set<String> keySet = jsonObject.keySet();
        List<String> newKeys = new ArrayList<>(keySet);
        for (String key : newKeys) {
            Object value = jsonObject.get(key);
            if (value instanceof JSONObject) {
                layDataOneLevel((JSONObject) value);
                jsonObject.remove(key);
                for (Entry<String, Object> entry : ((JSONObject) value).entrySet()) {
                    jsonObject.put(key + gap + entry.getKey(), entry.getValue());
                }
            } else if (value instanceof JSONArray) {
                for (Object item : (JSONArray) value) {
                    if (item instanceof JSONObject) {
                        layDataOneLevel((JSONObject) item);
                    }
                }
            }
        }
    }

    public static <T> T parseObject(String text, Class<T> clazz) {
        // fastjson如果text传的是一个“abc“这种字符串，会报错
        if (clazz == String.class) {
            return (T) text;
        }
        return JSON.parseObject(text, clazz, JSONReader.Feature.SupportSmartMatch);
    }

    public static <T> T parseObject(String text, TypeReference<T> type) {
        return JSON.parseObject(text, type, JSONReader.Feature.SupportSmartMatch);
    }

    public static Object parse(String text) {
        return JSON.parse(text, JSONReader.Feature.SupportSmartMatch);
    }

    public static String toJSONString(Object object) {
        if (object == null) {
            return Constants.EMPTY;
        }
        if (object instanceof String) {
            return (String) object;
        }
        return JSON.toJSONString(object, JSONWriter.Feature.WriteNonStringKeyAsString);
    }

    /**
     * 需要自行保证keyValues的准确性
     * example: createJSON("name", name, "age", 18)
     */
    public static JSONObject createJSON(Object... keyValues) {
        JSONObject json = new JSONObject();
        if (keyValues == null || keyValues.length == 0) {
            return json;
        }

        int size = keyValues.length;
        for (int i = 0; i < size; i += 2) {
            json.put((String) keyValues[i], toJSON(keyValues[i + 1]));
        }
        return json;
    }
}