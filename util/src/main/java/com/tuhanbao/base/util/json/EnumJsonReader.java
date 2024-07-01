package com.td.ca.base.util.json;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.td.ca.base.util.clazz.ClazzUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class EnumJsonReader implements ObjectReader {

    private Map<Object, Enum> cache;

    public EnumJsonReader(Class<?> enumClass) {
        cache = new HashMap<>();
        for (Enum item : (Enum[]) enumClass.getEnumConstants()) {
            cache.put(ClazzUtil.getVarValue(item, "value"), item);
        }
    }

    @Override
    public Object readObject(JSONReader jsonReader, Type fieldType, Object fieldName, long features) {
        if (jsonReader.isInt()) {
            int intValue = jsonReader.readInt32Value();
            return cache.get(intValue);
        } else if (jsonReader.isString()) {
            String str = jsonReader.readString();
            return cache.get(str);
        }
        return null;
    }
}
