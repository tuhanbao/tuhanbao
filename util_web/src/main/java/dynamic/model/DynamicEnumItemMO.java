package com.td.ca.web.db.dynamic.model;

import com.td.ca.base.dataservice.CreateAndModifyTimeRecorder;
import com.td.ca.base.dataservice.MetaObject;
import com.td.ca.base.dataservice.ServiceBean;
import com.td.ca.base.util.db.table.data.IntValue;
import com.td.ca.base.util.db.table.data.LongValue;
import com.td.ca.base.util.db.table.data.StringValue;
import com.td.ca.web.db.dynamic.constant.config.DynamicTableConstants;

import java.util.List;

public class DynamicEnumItemMO extends ServiceBean implements CreateAndModifyTimeRecorder {
    protected DynamicEnumItemMO() {
        this(new MetaObject(DynamicTableConstants.DynamicEnumItem.TABLE));
    }

    protected DynamicEnumItemMO(MetaObject mo) {
        super(mo);
    }

    public long getId() {
        LongValue value = (LongValue)getValue(DynamicTableConstants.DynamicEnumItem.ID);
        if (value == null) {
            return 0;
        } else {
            return value.getValue();
        }
    }

    public void setId(Long value) {
        setValue(DynamicTableConstants.DynamicEnumItem.ID, LongValue.valueOf(value));
    }

    public long getEnumId() {
        LongValue value = (LongValue)getValue(DynamicTableConstants.DynamicEnumItem.ENUM_ID);
        if (value == null) {
            return 0;
        } else {
            return value.getValue();
        }
    }

    public void setEnumId(Long value) {
        setValue(DynamicTableConstants.DynamicEnumItem.ENUM_ID, LongValue.valueOf(value));
    }

    public DynamicEnum getEnum() {
        List<? extends ServiceBean> result = this.getFKBean(DynamicTableConstants.DynamicEnumItem.ENUM_ID);
        return result == null || result.isEmpty() ? null : (DynamicEnum)result.get(0);
    }

    public DynamicEnum removeEnum() {
        List<? extends ServiceBean> result = this.removeFKBean(DynamicTableConstants.DynamicEnumItem.ENUM_ID);
        return result == null || result.isEmpty() ? null : (DynamicEnum)result.get(0);
    }

    public void setEnum(DynamicEnum value) {
        this.setFKBean(DynamicTableConstants.DynamicEnumItem.ENUM_ID, value);
    }

    public String getNameCn() {
        StringValue value = (StringValue)getValue(DynamicTableConstants.DynamicEnumItem.NAME_CN);
        if (value == null) {
            return null;
        } else {
            return value.getValue();
        }
    }

    public void setNameCn(String value) {
        setValue(DynamicTableConstants.DynamicEnumItem.NAME_CN, StringValue.valueOf(value));
    }

    public String getNameEn() {
        StringValue value = (StringValue)getValue(DynamicTableConstants.DynamicEnumItem.NAME_EN);
        if (value == null) {
            return null;
        } else {
            return value.getValue();
        }
    }

    public void setNameEn(String value) {
        setValue(DynamicTableConstants.DynamicEnumItem.NAME_EN, StringValue.valueOf(value));
    }

    public int getValue() {
        IntValue value = (IntValue)getValue(DynamicTableConstants.DynamicEnumItem.VALUE);
        if (value == null) {
            return 0;
        } else {
            return value.getValue();
        }
    }

    public void setValue(Integer value) {
        setValue(DynamicTableConstants.DynamicEnumItem.VALUE, IntValue.valueOf(value));
    }

    public long getParentId() {
        LongValue value = (LongValue)getValue(DynamicTableConstants.DynamicEnumItem.PARENT_ID);
        if (value == null) {
            return 0;
        } else {
            return value.getValue();
        }
    }

    public void setParentId(Long value) {
        setValue(DynamicTableConstants.DynamicEnumItem.PARENT_ID, LongValue.valueOf(value));
    }

    public DynamicEnumItem getParent() {
        List<? extends ServiceBean> result = this.getFKBean(DynamicTableConstants.DynamicEnumItem.PARENT_ID);
        return result == null || result.isEmpty() ? null : (DynamicEnumItem)result.get(0);
    }

    public DynamicEnumItem removeParent() {
        List<? extends ServiceBean> result = this.removeFKBean(DynamicTableConstants.DynamicEnumItem.PARENT_ID);
        return result == null || result.isEmpty() ? null : (DynamicEnumItem)result.get(0);
    }

    public void setParent(DynamicEnumItem value) {
        this.setFKBean(DynamicTableConstants.DynamicEnumItem.PARENT_ID, value);
    }

    public int getPriority() {
        IntValue value = (IntValue)getValue(DynamicTableConstants.DynamicEnumItem.PRIORITY);
        if (value == null) {
            return 0;
        } else {
            return value.getValue();
        }
    }

    public void setPriority(Integer value) {
        setValue(DynamicTableConstants.DynamicEnumItem.PRIORITY, IntValue.valueOf(value));
    }

    public long getGmtCreated() {
        LongValue value = (LongValue)getValue(DynamicTableConstants.DynamicEnumItem.GMT_CREATED);
        if (value == null) {
            return 0;
        } else {
            return value.getValue();
        }
    }

    public void setGmtCreated(Long value) {
        setValue(DynamicTableConstants.DynamicEnumItem.GMT_CREATED, LongValue.valueOf(value));
    }

    public long getGmtModified() {
        LongValue value = (LongValue)getValue(DynamicTableConstants.DynamicEnumItem.GMT_MODIFIED);
        if (value == null) {
            return 0;
        } else {
            return value.getValue();
        }
    }

    public void setGmtModified(Long value) {
        setValue(DynamicTableConstants.DynamicEnumItem.GMT_MODIFIED, LongValue.valueOf(value));
    }

    @SuppressWarnings("unchecked")
    public List<DynamicEnumItem> getDynamicEnumItems() {
        List<? extends ServiceBean> result = this.getFKBean(DynamicTableConstants.DynamicEnumItem.PARENT_ID, true);
        return (List<DynamicEnumItem>)result;
    }

    @SuppressWarnings("unchecked")
    public List<DynamicEnumItem> removeDynamicEnumItems() {
        List<? extends ServiceBean> result = this.removeFKBean(DynamicTableConstants.DynamicEnumItem.PARENT_ID, true);
        return (List<DynamicEnumItem>)result;
    }

    public void setDynamicEnumItems(List<DynamicEnumItem> value) {
        this.setFKBean(DynamicTableConstants.DynamicEnumItem.PARENT_ID, value, true);
    }

}