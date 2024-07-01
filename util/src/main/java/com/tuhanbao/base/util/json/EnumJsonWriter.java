package com.td.ca.base.util.json;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.td.ca.base.util.clazz.ClazzUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class EnumJsonWriter implements ObjectWriter {

    private Map<Enum, Object> cache;

    // 目前只支持string和int
    private boolean isInt = true;

    public EnumJsonWriter(Class<?> enumClass) {
        cache = new HashMap<>();
        for (Enum item : (Enum[]) enumClass.getEnumConstants()) {
            cache.put(item, ClazzUtil.getVarValue(item, "value"));
        }
        isInt = !(ClazzUtil.getVarValue(enumClass.getEnumConstants()[0], "value") instanceof String);
    }

    @Override
    public void write(JSONWriter jsonWriter, Object object, Object fieldName, Type fieldType, long features) {
        if (isInt) {
            jsonWriter.writeInt32((int) cache.get(object));
        } else {
            jsonWriter.writeString((String) cache.get(object));
        }
    }
}
