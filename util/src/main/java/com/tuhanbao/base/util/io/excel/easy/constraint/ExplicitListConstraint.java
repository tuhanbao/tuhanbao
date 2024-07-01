package com.td.ca.base.util.io.excel.easy.constraint;

import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.td.ca.base.util.objutil.ArrayUtil;
import com.td.ca.base.util.objutil.ObjectUtil;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.util.Map;

public class ExplicitListConstraint<T extends Object> implements Constraint<T> {

    private final String[] languages;

    private final Object[] values;

    public ExplicitListConstraint(Map<T, String> map) {
        languages = new String[map.size()];
        values = new Object[map.size()];
        int i = 0;
        for (Map.Entry<T, String> entry : map.entrySet()) {
            this.languages[i] = entry.getValue();
            this.values[i] = entry.getKey();
            i++;
        }
    }

    public ExplicitListConstraint(Object[] values, String[] languages) {
        this.languages = languages;
        this.values = values;
    }

    public void apply(int index, WriteSheetHolder writeSheetHolder) {
        CellRangeAddressList cellRangeAddressList =
                new CellRangeAddressList(0, 65535, index, index);
        DataValidationHelper helper = writeSheetHolder.getSheet().getDataValidationHelper();
        DataValidationConstraint validationConstraint = helper.createExplicitListConstraint(languages);
        DataValidation validation = helper.createValidation(validationConstraint, cellRangeAddressList);
        writeSheetHolder.getSheet().addValidationData(validation);
    }


    @Override
    public Class<?> supportJavaTypeKey() {
        return String.class;
    }

    @Override
    public T convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        String value = cellData.getStringValue();
        int index = ArrayUtil.indexOf(languages, value);
        return index == -1 ? null : (T) values[index];
    }

    @Override
    public WriteCellData<?> convertToExcelData(T value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        int index = ArrayUtil.indexOf(values, value);
        String str = index == -1 ? "" : languages[index];
        return new WriteCellData(str);
    }

}
