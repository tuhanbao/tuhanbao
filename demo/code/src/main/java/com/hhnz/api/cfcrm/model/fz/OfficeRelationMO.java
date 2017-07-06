package com.hhnz.api.cfcrm.model.fz;

import com.tuhanbao.base.dataservice.ServiceBean;
import com.hhnz.api.cfcrm.constants.TableConstants;
import com.tuhanbao.base.dataservice.MetaObject;
import com.tuhanbao.base.util.db.table.data.StringValue;
import java.util.List;
import com.tuhanbao.base.util.db.table.data.LongValue;

public class OfficeRelationMO extends ServiceBean {
    protected OfficeRelationMO() {
        this(new MetaObject(TableConstants.OFFICE_RELATION.TABLE));
    }

    protected OfficeRelationMO(MetaObject mo) {
        super(mo);
    }

    public String getOfficeId() {
        StringValue value = (StringValue)getValue(TableConstants.OFFICE_RELATION.OFFICE_ID);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setOfficeId(String value) {
        setValue(TableConstants.OFFICE_RELATION.OFFICE_ID, StringValue.valueOf(value));
    }

    public Office getOffice() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.OFFICE_RELATION.OFFICE_ID);
        return result == null || result.isEmpty() ? null : (Office)result.get(0);
    }

    public Office removeOffice() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.OFFICE_RELATION.OFFICE_ID);
        return result == null || result.isEmpty() ? null : (Office)result.get(0);
    }

    public void setOffice(Office value) {
        this.setFKBean(TableConstants.OFFICE_RELATION.OFFICE_ID, value);
    }

    public long getNongzhuangId() {
        LongValue value = (LongValue)getValue(TableConstants.OFFICE_RELATION.NONGZHUANG_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setNongzhuangId(Long value) {
        setValue(TableConstants.OFFICE_RELATION.NONGZHUANG_ID, LongValue.valueOf(value));
    }

    public CExtendInfo getNongzhuang() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.OFFICE_RELATION.NONGZHUANG_ID);
        return result == null || result.isEmpty() ? null : (CExtendInfo)result.get(0);
    }

    public CExtendInfo removeNongzhuang() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.OFFICE_RELATION.NONGZHUANG_ID);
        return result == null || result.isEmpty() ? null : (CExtendInfo)result.get(0);
    }

    public void setNongzhuang(CExtendInfo value) {
        this.setFKBean(TableConstants.OFFICE_RELATION.NONGZHUANG_ID, value);
    }

}