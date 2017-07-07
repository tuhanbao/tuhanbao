package com.hhnz.api.cfcrm.model.fz;

import com.tuhanbao.base.dataservice.ServiceBean;
import com.hhnz.api.cfcrm.constants.TableConstants;
import com.tuhanbao.base.dataservice.MetaObject;
import com.tuhanbao.base.util.db.table.data.LongValue;
import java.util.List;
import com.tuhanbao.base.util.db.table.data.StringValue;

public class RoleMO extends ServiceBean {
    protected RoleMO() {
        this(new MetaObject(TableConstants.ROLE.TABLE));
    }

    protected RoleMO(MetaObject mo) {
        super(mo);
    }

    public long getId() {
        LongValue value = (LongValue)getValue(TableConstants.ROLE.ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setId(Long value) {
        setValue(TableConstants.ROLE.ID, LongValue.valueOf(value));
    }

    public long getNongzhuangId() {
        LongValue value = (LongValue)getValue(TableConstants.ROLE.NONGZHUANG_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setNongzhuangId(Long value) {
        setValue(TableConstants.ROLE.NONGZHUANG_ID, LongValue.valueOf(value));
    }

    public CExtendInfo getNongzhuang() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.ROLE.NONGZHUANG_ID);
        return result == null || result.isEmpty() ? null : (CExtendInfo)result.get(0);
    }

    public CExtendInfo removeNongzhuang() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.ROLE.NONGZHUANG_ID);
        return result == null || result.isEmpty() ? null : (CExtendInfo)result.get(0);
    }

    public void setNongzhuang(CExtendInfo value) {
        this.setFKBean(TableConstants.ROLE.NONGZHUANG_ID, value);
    }

    public String getRole() {
        StringValue value = (StringValue)getValue(TableConstants.ROLE.ROLE);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setRole(String value) {
        setValue(TableConstants.ROLE.ROLE, StringValue.valueOf(value));
    }

    public String getType() {
        StringValue value = (StringValue)getValue(TableConstants.ROLE.TYPE);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setType(String value) {
        setValue(TableConstants.ROLE.TYPE, StringValue.valueOf(value));
    }

    public String getHighestRole() {
        StringValue value = (StringValue)getValue(TableConstants.ROLE.HIGHEST_ROLE);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setHighestRole(String value) {
        setValue(TableConstants.ROLE.HIGHEST_ROLE, StringValue.valueOf(value));
    }

    public String getYear() {
        StringValue value = (StringValue)getValue(TableConstants.ROLE.YEAR);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setYear(String value) {
        setValue(TableConstants.ROLE.YEAR, StringValue.valueOf(value));
    }

    public String getMonth() {
        StringValue value = (StringValue)getValue(TableConstants.ROLE.MONTH);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setMonth(String value) {
        setValue(TableConstants.ROLE.MONTH, StringValue.valueOf(value));
    }

}