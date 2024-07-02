package com.td.ca.web.db.dynamic.model;

import com.td.ca.base.dataservice.CreateAndModifyTimeRecorder;
import com.td.ca.base.dataservice.MetaObject;
import com.td.ca.base.dataservice.ServiceBean;
import com.td.ca.base.util.db.table.data.IntValue;
import com.td.ca.base.util.db.table.data.LongValue;
import com.td.ca.base.util.db.table.data.StringValue;
import com.td.ca.web.db.dynamic.constant.config.DynamicTableConstants;
import com.td.ca.web.db.dynamic.constant.enums.EnumType;

import java.util.List;

public class DynamicEnumMO extends ServiceBean implements CreateAndModifyTimeRecorder {
    protected DynamicEnumMO() {
        this(new MetaObject(DynamicTableConstants.DynamicEnum.TABLE));
    }

    protected DynamicEnumMO(MetaObject mo) {
        super(mo);
    }

    public long getId() {
        LongValue value = (LongValue)getValue(DynamicTableConstants.DynamicEnum.ID);
        if (value == null) {
            return 0;
        } else {
            return value.getValue();
        }
    }

    public void setId(Long value) {
        setValue(DynamicTableConstants.DynamicEnum.ID, LongValue.valueOf(value));
    }

    public String getNameCn() {
        StringValue value = (StringValue)getValue(DynamicTableConstants.DynamicEnum.NAME_CN);
        if (value == null) {
            return null;
        } else {
            return value.getValue();
        }
    }

    public void setNameCn(String value) {
        setValue(DynamicTableConstants.DynamicEnum.NAME_CN, StringValue.valueOf(value));
    }

    public String getNameEn() {
        StringValue value = (StringValue)getValue(DynamicTableConstants.DynamicEnum.NAME_EN);
        if (value == null) {
            return null;
        } else {
            return value.getValue();
        }
    }

    public void setNameEn(String value) {
        setValue(DynamicTableConstants.DynamicEnum.NAME_EN, StringValue.valueOf(value));
    }

    public EnumType getEnumType() {
        IntValue value = (IntValue)getValue(DynamicTableConstants.DynamicEnum.ENUM_TYPE);
        if (value == null) {
            return null;
        } else {
            return EnumType.getEnumType(value.getValue());        }
    }

    public void setEnumType(EnumType value) {
        setValue(DynamicTableConstants.DynamicEnum.ENUM_TYPE, IntValue.valueOf(value == null ? null : value.value));
    }

    public long getGmtCreated() {
        LongValue value = (LongValue)getValue(DynamicTableConstants.DynamicEnum.GMT_CREATED);
        if (value == null) {
            return 0;
        } else {
            return value.getValue();
        }
    }

    public void setGmtCreated(Long value) {
        setValue(DynamicTableConstants.DynamicEnum.GMT_CREATED, LongValue.valueOf(value));
    }

    public long getGmtModified() {
        LongValue value = (LongValue)getValue(DynamicTableConstants.DynamicEnum.GMT_MODIFIED);
        if (value == null) {
            return 0;
        } else {
            return value.getValue();
        }
    }

    public void setGmtModified(Long value) {
        setValue(DynamicTableConstants.DynamicEnum.GMT_MODIFIED, LongValue.valueOf(value));
    }

    @SuppressWarnings("unchecked")
    public List<DynamicEnumItem> getDynamicEnumItems() {
        List<? extends ServiceBean> result = this.getFKBean(DynamicTableConstants.DynamicEnumItem.ENUM_ID);
        return (List<DynamicEnumItem>)result;
    }

    @SuppressWarnings("unchecked")
    public List<DynamicEnumItem> removeDynamicEnumItems() {
        List<? extends ServiceBean> result = this.removeFKBean(DynamicTableConstants.DynamicEnumItem.ENUM_ID);
        return (List<DynamicEnumItem>)result;
    }

    public void setDynamicEnumItems(List<DynamicEnumItem> value) {
        this.setFKBean(DynamicTableConstants.DynamicEnumItem.ENUM_ID, value);
    }

}