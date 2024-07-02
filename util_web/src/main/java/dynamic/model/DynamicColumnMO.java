package com.td.ca.web.db.dynamic.model;

import com.td.ca.base.dataservice.CreateAndModifyTimeRecorder;
import com.td.ca.base.dataservice.MetaObject;
import com.td.ca.base.dataservice.ServiceBean;
import com.td.ca.base.util.db.table.data.BooleanValue;
import com.td.ca.base.util.db.table.data.IntValue;
import com.td.ca.base.util.db.table.data.LongValue;
import com.td.ca.base.util.db.table.data.StringValue;
import com.td.ca.web.db.dynamic.constant.config.DynamicTableConstants;
import com.td.ca.web.db.dynamic.constant.enums.DiyDataType;

import java.util.List;

public class DynamicColumnMO extends ServiceBean implements CreateAndModifyTimeRecorder {
    protected DynamicColumnMO() {
        this(new MetaObject(DynamicTableConstants.DynamicColumn.TABLE));
    }

    protected DynamicColumnMO(MetaObject mo) {
        super(mo);
    }

    public long getId() {
        LongValue value = (LongValue)getValue(DynamicTableConstants.DynamicColumn.ID);
        if (value == null) {
            return 0;
        } else {
            return value.getValue();
        }
    }

    public void setId(Long value) {
        setValue(DynamicTableConstants.DynamicColumn.ID, LongValue.valueOf(value));
    }

    public String getColName() {
        StringValue value = (StringValue)getValue(DynamicTableConstants.DynamicColumn.COL_NAME);
        if (value == null) {
            return null;
        } else {
            return value.getValue();
        }
    }

    public void setColName(String value) {
        setValue(DynamicTableConstants.DynamicColumn.COL_NAME, StringValue.valueOf(value));
    }

    public String getNameCn() {
        StringValue value = (StringValue)getValue(DynamicTableConstants.DynamicColumn.NAME_CN);
        if (value == null) {
            return null;
        } else {
            return value.getValue();
        }
    }

    public void setNameCn(String value) {
        setValue(DynamicTableConstants.DynamicColumn.NAME_CN, StringValue.valueOf(value));
    }

    public String getNameEn() {
        StringValue value = (StringValue)getValue(DynamicTableConstants.DynamicColumn.NAME_EN);
        if (value == null) {
            return null;
        } else {
            return value.getValue();
        }
    }

    public void setNameEn(String value) {
        setValue(DynamicTableConstants.DynamicColumn.NAME_EN, StringValue.valueOf(value));
    }

    public String getTableName() {
        StringValue value = (StringValue)getValue(DynamicTableConstants.DynamicColumn.TABLE_NAME);
        if (value == null) {
            return null;
        } else {
            return value.getValue();
        }
    }

    public void setTableName(String value) {
        setValue(DynamicTableConstants.DynamicColumn.TABLE_NAME, StringValue.valueOf(value));
    }

    public DynamicTable getTableNameByTableName() {
        List<? extends ServiceBean> result = this.getFKBean(DynamicTableConstants.DynamicColumn.TABLE_NAME);
        return result == null || result.isEmpty() ? null : (DynamicTable)result.get(0);
    }

    public DynamicTable removeTableName() {
        List<? extends ServiceBean> result = this.removeFKBean(DynamicTableConstants.DynamicColumn.TABLE_NAME);
        return result == null || result.isEmpty() ? null : (DynamicTable)result.get(0);
    }

    public void setTableNameByTableName(DynamicTable value) {
        this.setFKBean(DynamicTableConstants.DynamicColumn.TABLE_NAME, value);
    }

    public DiyDataType getDataType() {
        IntValue value = (IntValue)getValue(DynamicTableConstants.DynamicColumn.DATA_TYPE);
        if (value == null) {
            return null;
        } else {
            return DiyDataType.getDiyDataType(value.getValue());        }
    }

    public void setDataType(DiyDataType value) {
        setValue(DynamicTableConstants.DynamicColumn.DATA_TYPE, IntValue.valueOf(value == null ? null : value.value));
    }

    public String getArgs() {
        StringValue value = (StringValue)getValue(DynamicTableConstants.DynamicColumn.ARGS);
        if (value == null) {
            return null;
        } else {
            return value.getValue();
        }
    }

    public void setArgs(String value) {
        setValue(DynamicTableConstants.DynamicColumn.ARGS, StringValue.valueOf(value));
    }

    public boolean getNeedShow() {
        BooleanValue value = (BooleanValue)getValue(DynamicTableConstants.DynamicColumn.NEED_SHOW);
        if (value == null) {
            return false;
        } else {
            return value.getValue();
        }
    }

    public void setNeedShow(Boolean value) {
        setValue(DynamicTableConstants.DynamicColumn.NEED_SHOW, BooleanValue.valueOf(value));
    }

    public boolean getNeedFilter() {
        BooleanValue value = (BooleanValue)getValue(DynamicTableConstants.DynamicColumn.NEED_FILTER);
        if (value == null) {
            return false;
        } else {
            return value.getValue();
        }
    }

    public void setNeedFilter(Boolean value) {
        setValue(DynamicTableConstants.DynamicColumn.NEED_FILTER, BooleanValue.valueOf(value));
    }

    public boolean getNeedOrder() {
        BooleanValue value = (BooleanValue)getValue(DynamicTableConstants.DynamicColumn.NEED_ORDER);
        if (value == null) {
            return false;
        } else {
            return value.getValue();
        }
    }

    public void setNeedOrder(Boolean value) {
        setValue(DynamicTableConstants.DynamicColumn.NEED_ORDER, BooleanValue.valueOf(value));
    }

    public boolean getRequired() {
        BooleanValue value = (BooleanValue)getValue(DynamicTableConstants.DynamicColumn.REQUIRED);
        if (value == null) {
            return false;
        } else {
            return value.getValue();
        }
    }

    public void setRequired(Boolean value) {
        setValue(DynamicTableConstants.DynamicColumn.REQUIRED, BooleanValue.valueOf(value));
    }

    public String getDefaultValue() {
        StringValue value = (StringValue)getValue(DynamicTableConstants.DynamicColumn.DEFAULT_VALUE);
        if (value == null) {
            return null;
        } else {
            return value.getValue();
        }
    }

    public void setDefaultValue(String value) {
        setValue(DynamicTableConstants.DynamicColumn.DEFAULT_VALUE, StringValue.valueOf(value));
    }

    public int getPriority() {
        IntValue value = (IntValue)getValue(DynamicTableConstants.DynamicColumn.PRIORITY);
        if (value == null) {
            return 0;
        } else {
            return value.getValue();
        }
    }

    public void setPriority(Integer value) {
        setValue(DynamicTableConstants.DynamicColumn.PRIORITY, IntValue.valueOf(value));
    }

    public String getRemark() {
        StringValue value = (StringValue)getValue(DynamicTableConstants.DynamicColumn.REMARK);
        if (value == null) {
            return null;
        } else {
            return value.getValue();
        }
    }

    public void setRemark(String value) {
        setValue(DynamicTableConstants.DynamicColumn.REMARK, StringValue.valueOf(value));
    }

    public long getGmtCreated() {
        LongValue value = (LongValue)getValue(DynamicTableConstants.DynamicColumn.GMT_CREATED);
        if (value == null) {
            return 0;
        } else {
            return value.getValue();
        }
    }

    public void setGmtCreated(Long value) {
        setValue(DynamicTableConstants.DynamicColumn.GMT_CREATED, LongValue.valueOf(value));
    }

    public long getGmtModified() {
        LongValue value = (LongValue)getValue(DynamicTableConstants.DynamicColumn.GMT_MODIFIED);
        if (value == null) {
            return 0;
        } else {
            return value.getValue();
        }
    }

    public void setGmtModified(Long value) {
        setValue(DynamicTableConstants.DynamicColumn.GMT_MODIFIED, LongValue.valueOf(value));
    }

}