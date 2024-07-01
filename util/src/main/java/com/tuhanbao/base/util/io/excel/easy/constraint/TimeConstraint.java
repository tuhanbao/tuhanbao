package com.td.ca.base.util.io.excel.easy.constraint;

import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.td.ca.base.util.clazz.ClazzUtil;
import com.td.ca.base.util.objutil.ArrayUtil;
import com.td.ca.base.util.objutil.TimeUtil;
import com.td.ca.base.util.rm.ResourceManager;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TimeConstraint implements Constraint<Long> {

    private String timeFormat;

    private static final Map<String, TimeConstraint> CACHE = new ConcurrentHashMap<>();

    private TimeConstraint(String timeFormat) {
        this.timeFormat = timeFormat;
    }

    public static TimeConstraint createTimeConstraint(String timeFormat) {
        if (!CACHE.containsKey(timeFormat)) {
            CACHE.put(timeFormat, new TimeConstraint(timeFormat));
        }
        return CACHE.get(timeFormat);
    }

    @Override
    public Class<?> supportJavaTypeKey() {
        return Long.class;
    }

    @Override
    public Long convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        String value = cellData.getStringValue();
        return TimeUtil.getTime(value, this.timeFormat);
    }

    @Override
    public WriteCellData<?> convertToExcelData(Long value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        return new WriteCellData(TimeUtil.getTimeStr(this.timeFormat, value));
    }

    @Override
    public void apply(int index, WriteSheetHolder holder) {
        // donothing
    }
}