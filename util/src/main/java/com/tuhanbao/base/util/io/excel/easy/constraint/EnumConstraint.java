package com.td.ca.base.util.io.excel.easy.constraint;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ConverterKeyBuild;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.td.ca.base.util.clazz.ClazzUtil;
import com.td.ca.base.util.rm.ResourceManager;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EnumConstraint extends ExplicitListConstraint<Object> {

    public static final String VALUE = "value";

    private String[] enumLanguages;

    private Class<?> enumClass;

    private Map<String, Enum> valuesMap = new HashMap<>();

    private static final Map<Class<?>, EnumConstraint> CACHE = new ConcurrentHashMap<>();

    private EnumConstraint(Object[] values, String[] enumLanguages, Map<String, Enum> valuesMap) {
        super(values, enumLanguages);
        this.valuesMap = valuesMap;
    }

    public static EnumConstraint createEnumConstraint(Class<?> enumClass) {
        return createEnumConstraint(enumClass, VALUE);
    }

    /**
     *
     * @param enumClass
     * @param keyFieldName 能够代表枚举的关键属性
     * @return
     */
    public static EnumConstraint createEnumConstraint(Class<?> enumClass, String keyFieldName) {
        if (!CACHE.containsKey(enumClass)) {
            CACHE.put(enumClass, createEnumConstraintInner(enumClass, keyFieldName));
        }
        return CACHE.get(enumClass);
    }

    private static EnumConstraint createEnumConstraintInner(Class<?> enumClass, String keyFieldName) {
        Object[] o = enumClass.getEnumConstants();
        int length = o.length;
        String[] enumLanguages = new String[length];
        Object[] values = new Object[length];
        Map<String, Enum> valuesMap = new HashMap<>();
        Field field = ClazzUtil.getVar(enumClass, keyFieldName);
        for (int i = 0; i < length; i++) {
            Enum item = (Enum) o[i];
            enumLanguages[i] = ResourceManager.getResource(item);
            values[i] = item;
            if (field != null) {
                Object value = ClazzUtil.getVarValue(item, keyFieldName);
                if (value != null) {
                    valuesMap.put(value.toString(), item);
                }
            }
        }
        return new EnumConstraint(values, enumLanguages, valuesMap);
    }

    @Override
    public Class<?> supportJavaTypeKey() {
        return Enum.class;
    }

    @Override
    public WriteCellData<?> convertToExcelData(Object value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        return super.convertToExcelData(getEnum(value), contentProperty, globalConfiguration);
    }

    private Enum getEnum(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Enum) {
            return (Enum) value;
        }
        return valuesMap.get(value.toString());
    }
}