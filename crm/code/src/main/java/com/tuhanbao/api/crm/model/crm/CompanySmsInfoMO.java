package com.tuhanbao.api.crm.model.crm;

import com.tuhanbao.base.dataservice.ServiceBean;
import com.tuhanbao.api.crm.constants.TableConstants;
import com.tuhanbao.base.dataservice.MetaObject;
import com.tuhanbao.base.util.db.table.data.LongValue;
import java.util.List;
import com.tuhanbao.base.util.db.table.data.IntValue;

public class CompanySmsInfoMO extends ServiceBean {
    protected CompanySmsInfoMO() {
        this(new MetaObject(TableConstants.T_COMPANY_SMS_INFO.TABLE));
    }

    protected CompanySmsInfoMO(MetaObject mo) {
        super(mo);
    }

    public long getId() {
        LongValue value = (LongValue)getValue(TableConstants.T_COMPANY_SMS_INFO.ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setId(Long value) {
        setValue(TableConstants.T_COMPANY_SMS_INFO.ID, LongValue.valueOf(value));
    }

    public long getCompanyId() {
        LongValue value = (LongValue)getValue(TableConstants.T_COMPANY_SMS_INFO.COMPANY_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setCompanyId(Long value) {
        setValue(TableConstants.T_COMPANY_SMS_INFO.COMPANY_ID, LongValue.valueOf(value));
    }

    public Company getCompany() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_COMPANY_SMS_INFO.COMPANY_ID);
        return result == null || result.isEmpty() ? null : (Company)result.get(0);
    }

    public Company removeCompany() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_COMPANY_SMS_INFO.COMPANY_ID);
        return result == null || result.isEmpty() ? null : (Company)result.get(0);
    }

    public void setCompany(Company value) {
        this.setFKBean(TableConstants.T_COMPANY_SMS_INFO.COMPANY_ID, value);
    }

    public int getRemainNum() {
        IntValue value = (IntValue)getValue(TableConstants.T_COMPANY_SMS_INFO.REMAIN_NUM);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setRemainNum(Integer value) {
        setValue(TableConstants.T_COMPANY_SMS_INFO.REMAIN_NUM, IntValue.valueOf(value));
    }

    public int getTotalNum() {
        IntValue value = (IntValue)getValue(TableConstants.T_COMPANY_SMS_INFO.TOTAL_NUM);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setTotalNum(Integer value) {
        setValue(TableConstants.T_COMPANY_SMS_INFO.TOTAL_NUM, IntValue.valueOf(value));
    }

    public long getTotalMoney() {
        LongValue value = (LongValue)getValue(TableConstants.T_COMPANY_SMS_INFO.TOTAL_MONEY);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setTotalMoney(Long value) {
        setValue(TableConstants.T_COMPANY_SMS_INFO.TOTAL_MONEY, LongValue.valueOf(value));
    }

    public int getPrice() {
        IntValue value = (IntValue)getValue(TableConstants.T_COMPANY_SMS_INFO.PRICE);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setPrice(Integer value) {
        setValue(TableConstants.T_COMPANY_SMS_INFO.PRICE, IntValue.valueOf(value));
    }

}