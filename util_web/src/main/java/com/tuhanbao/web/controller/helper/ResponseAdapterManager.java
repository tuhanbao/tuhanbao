package com.tuhanbao.web.controller.helper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.clazz.ClazzUtil;
import com.tuhanbao.base.util.clazz.IClassFilter;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.StringUtil;

public class ResponseAdapterManager {

    private static Map<String, Class<?>> ADAPTERS = new HashMap<String, Class<?>>();

//    static {
//        init();
//    }
//    
//    public static void main(String args[]) {
//        System.out.println();
//    }

    public static void load(String packageName) {
        try {
            List<Class<?>> list = ClazzUtil.getAllClasses(packageName, new IClassFilter() {
                @Override
                public boolean filterClass(Class<?> clazz) {
                    return clazz.getAnnotation(ResponseAdapter.class) != null;
                }
            });
            for (Class<?> clazz : list) {
                String name = clazz.getAnnotation(ResponseAdapter.class).name();
                if (StringUtil.isEmpty(name)) {
                    name = clazz.getSimpleName();
                }
                register(name, clazz);
            }
        }
        catch (IOException e) {
            LogManager.error(e);
        }
    }

    private static void register(String key, Class<?> clazz) {
        ADAPTERS.put(key, clazz);
    }

    public static Class<?> getAdapter(String key) {
        return ADAPTERS.get(key);
    }
    
    /**
     * 获取单项值
     * 为了节省效率，没有使用递归
     * 
     * @param value
     * @param key
     * @return
     */
    private static Object getSingleValue(Object value, String key) {
        if (StringUtil.isEmpty(key) || value == null) {
            return value;
        }
        int wellIndex = key.indexOf(Constants.WELL);
        if (wellIndex != -1) {
            String arrayKey = key.substring(0, wellIndex);
            int index = Integer.valueOf(key.substring(wellIndex + 1));
            return ((JSONArray)getSingleValue(value, arrayKey)).get(index);
        }
        else {
            return ((JSONObject)value).get(key);
        }
    }
    
    /**
     * 获取一个key的值
     * 为了节省效率，没有使用递归
     * 
     * @param value
     * @param key
     * @return
     */
    private static Object getValue(Object value, String key) {
        if (StringUtil.isEmpty(key)) {
            return value;
        }
        StringTokenizer st = new StringTokenizer(key, Constants.STOP_EN);
        while (st.hasMoreTokens()) {
            value = getSingleValue(value, st.nextToken());
        }
        return value;
    }
    
    private static Object getValue(Object value, Class<?> adapter) {
        if (adapter == null || value == null) return value;
        ResponseAdapter classAdapter = adapter.getAnnotation(ResponseAdapter.class);
        if (classAdapter != null) value = getValue(value, classAdapter.value());
        
        if (value instanceof JSONArray) {
            JSONArray result = new JSONArray();
            int size = ((JSONArray)value).size();
            for (int i = 0; i < size; i++) {
                result.add(changeObjectByField(((JSONArray)value).get(i), adapter));
            }
            return result;
        } else {
            return changeObjectByField(value, adapter);
        }
        
    }

    private static Object changeObjectByField(Object object, Class<?> adapter) {
        JSONObject result = new JSONObject();
        
        for (Field field : adapter.getDeclaredFields()) {
            ResponseAdapterAttr fieldAdapter = field.getAnnotation(ResponseAdapterAttr.class);
            if (fieldAdapter != null) {
                if (isAdapter(field.getType())) {
                    result.put(field.getName(), getValue(getValue(object, fieldAdapter.value()), field.getType()));
                }
                else {
                    result.put(field.getName(), getValue(object, fieldAdapter.value()));
                }
            }
        }
        return result;
    }

    private static boolean isAdapter(Class<?> clazz) {
        return clazz != String.class && clazz != Object.class;
    }

    public static Object getValue(Object result, ResponseAdapter adapter) {
        if (adapter == null || result == null) return result;
        Class<?> clazz = getAdapter(adapter.value());
        if (clazz == null) return result;
        
        return getValue(result, clazz);
    }
}
