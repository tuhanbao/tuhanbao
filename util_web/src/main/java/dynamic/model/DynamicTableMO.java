package com.td.ca.web.db.dynamic.model;

import com.td.ca.base.dataservice.CreateAndModifyTimeRecorder;
import com.td.ca.base.dataservice.MetaObject;
import com.td.ca.base.dataservice.ServiceBean;
import com.td.ca.base.util.db.table.data.IntValue;
import com.td.ca.base.util.db.table.data.LongValue;
import com.td.ca.base.util.db.table.data.StringValue;
import com.td.ca.web.db.dynamic.constant.config.DynamicTableConstants;

import java.util.List;

public class DynamicTableMO extends ServiceBean implements CreateAndModifyTimeRecorder {
    protected DynamicTableMO() {
        this(new MetaObject(DynamicTableConstants.DynamicTable.TABLE));
    }

    protected DynamicTableMO(MetaObject mo) {
        super(mo);
    }

    public String getTableName() {
        StringValue value = (StringValue)getValue(DynamicTableConstants.DynamicTable.TABLE_NAME);
        if (value == null) {
            return null;
        } else {
            return value.getValue();
        }
    }

    public void setTableName(String value) {
        setValue(DynamicTableConstants.DynamicTable.TABLE_NAME, StringValue.valueOf(value));
    }

    public String getNameCn() {
        StringValue value = (StringValue)getValue(DynamicTableConstants.DynamicTable.NAME_CN);
        if (value == null) {
            return null;
        } else {
            return value.getValue();
        }
    }

    public void setNameCn(String value) {
        setValue(DynamicTableConstants.DynamicTable.NAME_CN, StringValue.valueOf(value));
    }

    public String getNameEn() {
        StringValue value = (StringValue)getValue(DynamicTableConstants.DynamicTable.NAME_EN);
        if (value == null) {
            return null;
        } else {
            return value.getValue();
        }
    }

    public void setNameEn(String value) {
        setValue(DynamicTableConstants.DynamicTable.NAME_EN, StringValue.valueOf(value));
    }

    public int getCacheType() {
        IntValue value = (IntValue)getValue(DynamicTableConstants.DynamicTable.CACHE_TYPE);
        if (value == null) {
            return 0;
        } else {
            return value.getValue();
        }
    }

    public void setCacheType(Integer value) {
        setValue(DynamicTableConstants.DynamicTable.CACHE_TYPE, IntValue.valueOf(value));
    }

    public String getTag() {
        StringValue value = (StringValue)getValue(DynamicTableConstants.DynamicTable.TAG);
        if (value == null) {
            return null;
        } else {
            return value.getValue();
        }
    }

    public void setTag(String value) {
        setValue(DynamicTableConstants.DynamicTable.TAG, StringValue.valueOf(value));
    }

    public String getIndexes() {
        StringValue value = (StringValue)getValue(DynamicTableConstants.DynamicTable.INDEXES);
        if (value == null) {
            return null;
        } else {
            return value.getValue();
        }
    }

    public void setIndexes(String value) {
        setValue(DynamicTableConstants.DynamicTable.INDEXES, StringValue.valueOf(value));
    }

    public String getRemark() {
        StringValue value = (StringValue)getValue(DynamicTableConstants.DynamicTable.REMARK);
        if (value == null) {
            return null;
        } else {
            return value.getValue();
        }
    }

    public void setRemark(String value) {
        setValue(DynamicTableConstants.DynamicTable.REMARK, StringValue.valueOf(value));
    }

    public long getGmtCreated() {
        LongValue value = (LongValue)getValue(DynamicTableConstants.DynamicTable.GMT_CREATED);
        if (value == null) {
            return 0;
        } else {
            return value.getValue();
        }
    }

    public void setGmtCreated(Long value) {
        setValue(DynamicTableConstants.DynamicTable.GMT_CREATED, LongValue.valueOf(value));
    }

    public long getGmtModified() {
        LongValue value = (LongValue)getValue(DynamicTableConstants.DynamicTable.GMT_MODIFIED);
        if (value == null) {
            return 0;
        } else {
            return value.getValue();
        }
    }

    public void setGmtModified(Long value) {
        setValue(DynamicTableConstants.DynamicTable.GMT_MODIFIED, LongValue.valueOf(value));
    }

    @SuppressWarnings("unchecked")
    public List<DynamicColumn> getDynamicColumns() {
        List<? extends ServiceBean> result = this.getFKBean(DynamicTableConstants.DynamicColumn.TABLE_NAME);
        return (List<DynamicColumn>)result;
    }

    @SuppressWarnings("unchecked")
    public List<DynamicColumn> removeDynamicColumns() {
        List<? extends ServiceBean> result = this.removeFKBean(DynamicTableConstants.DynamicColumn.TABLE_NAME);
        return (List<DynamicColumn>)result;
    }

    public void setDynamicColumns(List<DynamicColumn> value) {
        this.setFKBean(DynamicTableConstants.DynamicColumn.TABLE_NAME, value);
    }

}