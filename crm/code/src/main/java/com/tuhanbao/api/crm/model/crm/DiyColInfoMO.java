package com.tuhanbao.api.crm.model.crm;

import com.tuhanbao.base.dataservice.ServiceBean;
import com.tuhanbao.api.crm.constants.TableConstants;
import com.tuhanbao.base.dataservice.MetaObject;
import com.tuhanbao.base.util.db.table.data.LongValue;
import java.util.List;
import com.tuhanbao.base.util.db.table.data.StringValue;
import com.tuhanbao.base.util.db.table.data.IntValue;
import com.tuhanbao.api.crm.constants.enums.ColType;

public class DiyColInfoMO extends ServiceBean {
    protected DiyColInfoMO() {
        this(new MetaObject(TableConstants.T_DIY_COL_INFO.TABLE));
    }

    protected DiyColInfoMO(MetaObject mo) {
        super(mo);
    }

    public long getId() {
        LongValue value = (LongValue)getValue(TableConstants.T_DIY_COL_INFO.ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setId(Long value) {
        setValue(TableConstants.T_DIY_COL_INFO.ID, LongValue.valueOf(value));
    }

    public long getCompanyId() {
        LongValue value = (LongValue)getValue(TableConstants.T_DIY_COL_INFO.COMPANY_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setCompanyId(Long value) {
        setValue(TableConstants.T_DIY_COL_INFO.COMPANY_ID, LongValue.valueOf(value));
    }

    public Company getCompany() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_DIY_COL_INFO.COMPANY_ID);
        return result == null || result.isEmpty() ? null : (Company)result.get(0);
    }

    public Company removeCompany() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_DIY_COL_INFO.COMPANY_ID);
        return result == null || result.isEmpty() ? null : (Company)result.get(0);
    }

    public void setCompany(Company value) {
        this.setFKBean(TableConstants.T_DIY_COL_INFO.COMPANY_ID, value);
    }

    public String getTableName() {
        StringValue value = (StringValue)getValue(TableConstants.T_DIY_COL_INFO.TABLE_NAME);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setTableName(String value) {
        setValue(TableConstants.T_DIY_COL_INFO.TABLE_NAME, StringValue.valueOf(value));
    }

    public String getColumnName() {
        StringValue value = (StringValue)getValue(TableConstants.T_DIY_COL_INFO.COLUMN_NAME);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setColumnName(String value) {
        setValue(TableConstants.T_DIY_COL_INFO.COLUMN_NAME, StringValue.valueOf(value));
    }

    public ColType getType() {
        IntValue value = (IntValue)getValue(TableConstants.T_DIY_COL_INFO.TYPE);
        if (value == null) return null;
        else return ColType.getColType(value.getValue());
    }

    public void setType(ColType value) {
        setValue(TableConstants.T_DIY_COL_INFO.TYPE, IntValue.valueOf(value.value));
    }

    public String getName() {
        StringValue value = (StringValue)getValue(TableConstants.T_DIY_COL_INFO.NAME);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setName(String value) {
        setValue(TableConstants.T_DIY_COL_INFO.NAME, StringValue.valueOf(value));
    }

    public String getOtherInfo() {
        StringValue value = (StringValue)getValue(TableConstants.T_DIY_COL_INFO.OTHER_INFO);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setOtherInfo(String value) {
        setValue(TableConstants.T_DIY_COL_INFO.OTHER_INFO, StringValue.valueOf(value));
    }

}