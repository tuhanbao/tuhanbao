package com.td.ca.base.util.io.excel.easy;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.td.ca.base.util.objutil.StringUtil;
import lombok.Getter;

@Getter
public enum ExportType {
    CSV("text/csv; charset=UTF-8", ExcelTypeEnum.CSV),

    XLS("application/vnd.ms-excel", ExcelTypeEnum.XLS),

    XLSX("application/vnd.ms-excel", ExcelTypeEnum.XLSX);


    private String contentType;

    private ExcelTypeEnum excelTypeEnum;

    private ExportType(String contentType, ExcelTypeEnum excelTypeEnum) {
        this.contentType = contentType;
        this.excelTypeEnum = excelTypeEnum;
    }

    public String getSuffix() {
        return this.excelTypeEnum.getValue();
    }

    public static ExportType getExportType(String name) {
        for (ExportType item : ExportType.values()) {
            if (StringUtil.isEqualIgnoreCase(item.name(), name)) {
                return item;
            }
        }
        return null;
    }

}
