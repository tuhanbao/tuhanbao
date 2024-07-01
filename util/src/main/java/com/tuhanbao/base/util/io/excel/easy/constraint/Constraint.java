package com.td.ca.base.util.io.excel.easy.constraint;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;

public interface Constraint<T> extends Converter<T> {
    void apply(int index, WriteSheetHolder holder);
}
