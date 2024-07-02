package com.td.ca.web.db.dynamic.model;


import com.td.ca.base.util.rm.DefaultResourceImpl;
import com.td.ca.base.util.rm.ResourceManager;

public class DynamicEnumItem extends DynamicEnumItemMO {
    public DynamicEnumItem() {

    }

    public String getName() {
        String locale = ResourceManager.getLocale();
        if (DefaultResourceImpl.ZH.equals(locale) || locale.startsWith("zh_")) {
            return this.getNameCn();
        } else {
            return this.getNameEn();
        }
    }

}