package com.tuhanbao.web.filter;

import com.tuhanbao.base.util.db.table.Column;
import com.tuhanbao.base.util.rm.ResourceManager;

public class WebFilterItem {

    private String key;
    
    private String name;
    
    private String dataType;
    
    private String enumName;

    public WebFilterItem(Column col) {
        this.key = col.getNameWithTable();
        this.name = ResourceManager.getResource(this.key);
        this.dataType = col.getDataType().name();
        this.enumName = col.getEnumStr();
    }

    public String getKey() {
        return key;
    }

    public String getEnumName() {
        return enumName;
    }

    public String getName() {
        return name;
    }

    public String getDataType() {
        return dataType;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public void setEnumName(String enumName) {
        this.enumName = enumName;
    }
}
