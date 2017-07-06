package com.hhnz.api.cfcrm.model.fz;

import com.tuhanbao.base.dataservice.ServiceBean;
import com.hhnz.api.cfcrm.constants.TableConstants;
import com.tuhanbao.base.dataservice.MetaObject;
import com.tuhanbao.base.util.db.table.data.LongValue;
import java.util.List;
import com.tuhanbao.base.util.db.table.data.StringValue;

public class SeparateBillsApplyMO extends ServiceBean {
    protected SeparateBillsApplyMO() {
        this(new MetaObject(TableConstants.T_SEPARATE_BILLS_APPLY.TABLE));
    }

    protected SeparateBillsApplyMO(MetaObject mo) {
        super(mo);
    }

    public long getApplyId() {
        LongValue value = (LongValue)getValue(TableConstants.T_SEPARATE_BILLS_APPLY.APPLY_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setApplyId(Long value) {
        setValue(TableConstants.T_SEPARATE_BILLS_APPLY.APPLY_ID, LongValue.valueOf(value));
    }

    public CExtendInfo getApply() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_SEPARATE_BILLS_APPLY.APPLY_ID);
        return result == null || result.isEmpty() ? null : (CExtendInfo)result.get(0);
    }

    public CExtendInfo removeApply() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_SEPARATE_BILLS_APPLY.APPLY_ID);
        return result == null || result.isEmpty() ? null : (CExtendInfo)result.get(0);
    }

    public void setApply(CExtendInfo value) {
        this.setFKBean(TableConstants.T_SEPARATE_BILLS_APPLY.APPLY_ID, value);
    }

    public String getPhone() {
        StringValue value = (StringValue)getValue(TableConstants.T_SEPARATE_BILLS_APPLY.PHONE);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setPhone(String value) {
        setValue(TableConstants.T_SEPARATE_BILLS_APPLY.PHONE, StringValue.valueOf(value));
    }

    public String getAddr() {
        StringValue value = (StringValue)getValue(TableConstants.T_SEPARATE_BILLS_APPLY.ADDR);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setAddr(String value) {
        setValue(TableConstants.T_SEPARATE_BILLS_APPLY.ADDR, StringValue.valueOf(value));
    }

    public String getAddrPcd() {
        StringValue value = (StringValue)getValue(TableConstants.T_SEPARATE_BILLS_APPLY.ADDR_PCD);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setAddrPcd(String value) {
        setValue(TableConstants.T_SEPARATE_BILLS_APPLY.ADDR_PCD, StringValue.valueOf(value));
    }

}