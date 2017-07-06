package com.tuhanbao.base.util.io.excel;

public enum ExcelType {
    XLS(".xls"), XLSX(".xlsx");
    
    public final String suffix;
    
    private ExcelType(String suffix) {
        this.suffix = suffix;
    }
    
}
