package com.tuhanbao.api.crm.model.crm;

import com.tuhanbao.base.dataservice.ServiceBean;
import com.tuhanbao.api.crm.constants.TableConstants;
import com.tuhanbao.base.dataservice.MetaObject;
import com.tuhanbao.base.util.db.table.data.LongValue;
import com.tuhanbao.base.util.db.table.data.StringValue;
import java.util.List;

public class UserMO extends ServiceBean {
    protected UserMO() {
        this(new MetaObject(TableConstants.T_USER.TABLE));
    }

    protected UserMO(MetaObject mo) {
        super(mo);
    }

    public long getId() {
        LongValue value = (LongValue)getValue(TableConstants.T_USER.ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setId(Long value) {
        setValue(TableConstants.T_USER.ID, LongValue.valueOf(value));
    }

    public String getLoginName() {
        StringValue value = (StringValue)getValue(TableConstants.T_USER.LOGIN_NAME);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setLoginName(String value) {
        setValue(TableConstants.T_USER.LOGIN_NAME, StringValue.valueOf(value));
    }

    public String getPassword() {
        StringValue value = (StringValue)getValue(TableConstants.T_USER.PASSWORD);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setPassword(String value) {
        setValue(TableConstants.T_USER.PASSWORD, StringValue.valueOf(value));
    }

    public long getRoleId() {
        LongValue value = (LongValue)getValue(TableConstants.T_USER.ROLE_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setRoleId(Long value) {
        setValue(TableConstants.T_USER.ROLE_ID, LongValue.valueOf(value));
    }

    public Role getRole() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_USER.ROLE_ID);
        return result == null || result.isEmpty() ? null : (Role)result.get(0);
    }

    public Role removeRole() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_USER.ROLE_ID);
        return result == null || result.isEmpty() ? null : (Role)result.get(0);
    }

    public void setRole(Role value) {
        this.setFKBean(TableConstants.T_USER.ROLE_ID, value);
    }

    public String getMark() {
        StringValue value = (StringValue)getValue(TableConstants.T_USER.MARK);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setMark(String value) {
        setValue(TableConstants.T_USER.MARK, StringValue.valueOf(value));
    }

    public long getCompanyId() {
        LongValue value = (LongValue)getValue(TableConstants.T_USER.COMPANY_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setCompanyId(Long value) {
        setValue(TableConstants.T_USER.COMPANY_ID, LongValue.valueOf(value));
    }

    public Company getCompany() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_USER.COMPANY_ID);
        return result == null || result.isEmpty() ? null : (Company)result.get(0);
    }

    public Company removeCompany() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_USER.COMPANY_ID);
        return result == null || result.isEmpty() ? null : (Company)result.get(0);
    }

    public void setCompany(Company value) {
        this.setFKBean(TableConstants.T_USER.COMPANY_ID, value);
    }

    @SuppressWarnings("unchecked")
    public List<Custom> getCustoms() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_CUSTOM.OPERATOR_ID);
        return (List<Custom>)result;
    }

    @SuppressWarnings("unchecked")
    public List<Custom> removeCustoms() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_CUSTOM.OPERATOR_ID);
        return (List<Custom>)result;
    }

    public void setCustoms(List<Custom> value) {
        this.setFKBean(TableConstants.T_CUSTOM.OPERATOR_ID, value);
    }

    @SuppressWarnings("unchecked")
    public List<SmsRecord> getSmsRecords() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_SMS_RECORD.USER_ID);
        return (List<SmsRecord>)result;
    }

    @SuppressWarnings("unchecked")
    public List<SmsRecord> removeSmsRecords() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_SMS_RECORD.USER_ID);
        return (List<SmsRecord>)result;
    }

    public void setSmsRecords(List<SmsRecord> value) {
        this.setFKBean(TableConstants.T_SMS_RECORD.USER_ID, value);
    }

}