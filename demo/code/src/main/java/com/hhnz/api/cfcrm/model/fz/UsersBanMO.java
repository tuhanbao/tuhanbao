package com.hhnz.api.cfcrm.model.fz;

import com.tuhanbao.base.dataservice.ServiceBean;
import com.hhnz.api.cfcrm.constants.TableConstants;
import com.tuhanbao.base.dataservice.MetaObject;
import com.tuhanbao.base.util.db.table.data.LongValue;
import java.util.List;
import com.tuhanbao.base.util.db.table.data.IntValue;
import com.tuhanbao.base.util.db.table.data.BooleanValue;

public class UsersBanMO extends ServiceBean {
    protected UsersBanMO() {
        this(new MetaObject(TableConstants.USERS_BAN.TABLE));
    }

    protected UsersBanMO(MetaObject mo) {
        super(mo);
    }

    public long getId() {
        LongValue value = (LongValue)getValue(TableConstants.USERS_BAN.ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setId(Long value) {
        setValue(TableConstants.USERS_BAN.ID, LongValue.valueOf(value));
    }

    public long getNongzhuangId() {
        LongValue value = (LongValue)getValue(TableConstants.USERS_BAN.NONGZHUANG_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setNongzhuangId(Long value) {
        setValue(TableConstants.USERS_BAN.NONGZHUANG_ID, LongValue.valueOf(value));
    }

    public CExtendInfo getNongzhuang() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.USERS_BAN.NONGZHUANG_ID);
        return result == null || result.isEmpty() ? null : (CExtendInfo)result.get(0);
    }

    public CExtendInfo removeNongzhuang() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.USERS_BAN.NONGZHUANG_ID);
        return result == null || result.isEmpty() ? null : (CExtendInfo)result.get(0);
    }

    public void setNongzhuang(CExtendInfo value) {
        this.setFKBean(TableConstants.USERS_BAN.NONGZHUANG_ID, value);
    }

    public int getYear() {
        IntValue value = (IntValue)getValue(TableConstants.USERS_BAN.YEAR);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setYear(Integer value) {
        setValue(TableConstants.USERS_BAN.YEAR, IntValue.valueOf(value));
    }

    public int getMonth() {
        IntValue value = (IntValue)getValue(TableConstants.USERS_BAN.MONTH);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setMonth(Integer value) {
        setValue(TableConstants.USERS_BAN.MONTH, IntValue.valueOf(value));
    }

    public boolean getSalt() {
        BooleanValue value = (BooleanValue)getValue(TableConstants.USERS_BAN.SALT);
        if (value == null) return false;
        else return value.getValue();
    }

    public void setSalt(Boolean value) {
        setValue(TableConstants.USERS_BAN.SALT, BooleanValue.valueOf(value));
    }

}