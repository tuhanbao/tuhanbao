package com.hhnz.api.cfcrm.model.cfcrm;

import com.tuhanbao.base.dataservice.ServiceBean;
import com.hhnz.api.cfcrm.constants.TableConstants;
import com.tuhanbao.base.dataservice.MetaObject;
import com.tuhanbao.base.util.db.table.data.LongValue;
import com.tuhanbao.base.util.db.table.data.StringValue;
import java.util.Date;
import com.tuhanbao.base.util.db.table.data.TimeValue;
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

    public Date getCreateDate() {
        TimeValue value = (TimeValue)getValue(TableConstants.T_USER.CREATE_DATE);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setCreateDate(Date value) {
        setValue(TableConstants.T_USER.CREATE_DATE, TimeValue.valueOf(value));
    }

    public long getCreateUserId() {
        LongValue value = (LongValue)getValue(TableConstants.T_USER.CREATE_USER_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setCreateUserId(Long value) {
        setValue(TableConstants.T_USER.CREATE_USER_ID, LongValue.valueOf(value));
    }

    public User getCreateUser() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_USER.CREATE_USER_ID);
        return result == null || result.isEmpty() ? null : (User)result.get(0);
    }

    public User removeCreateUser() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_USER.CREATE_USER_ID);
        return result == null || result.isEmpty() ? null : (User)result.get(0);
    }

    public void setCreateUser(User value) {
        this.setFKBean(TableConstants.T_USER.CREATE_USER_ID, value);
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

    @SuppressWarnings("unchecked")
    public List<User> getUsers() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_USER.CREATE_USER_ID, true);
        return (List<User>)result;
    }

    @SuppressWarnings("unchecked")
    public List<User> removeUsers() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_USER.CREATE_USER_ID, true);
        return (List<User>)result;
    }

    public void setUsers(List<User> value) {
        this.setFKBean(TableConstants.T_USER.CREATE_USER_ID, value, true);
    }

    @SuppressWarnings("unchecked")
    public List<VipMarketingRecord> getVipMarketingRecords() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_VIP_MARKETING_RECORD.USER_ID);
        return (List<VipMarketingRecord>)result;
    }

    @SuppressWarnings("unchecked")
    public List<VipMarketingRecord> removeVipMarketingRecords() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_VIP_MARKETING_RECORD.USER_ID);
        return (List<VipMarketingRecord>)result;
    }

    public void setVipMarketingRecords(List<VipMarketingRecord> value) {
        this.setFKBean(TableConstants.T_VIP_MARKETING_RECORD.USER_ID, value);
    }

    @SuppressWarnings("unchecked")
    public List<VipAwardManage> getVipAwardManages() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_VIP_AWARD_MANAGE.USER_ID);
        return (List<VipAwardManage>)result;
    }

    @SuppressWarnings("unchecked")
    public List<VipAwardManage> removeVipAwardManages() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_VIP_AWARD_MANAGE.USER_ID);
        return (List<VipAwardManage>)result;
    }

    public void setVipAwardManages(List<VipAwardManage> value) {
        this.setFKBean(TableConstants.T_VIP_AWARD_MANAGE.USER_ID, value);
    }

    @SuppressWarnings("unchecked")
    public List<Role> getRoles() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_ROLE.CREATE_USER_ID);
        return (List<Role>)result;
    }

    @SuppressWarnings("unchecked")
    public List<Role> removeRoles() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_ROLE.CREATE_USER_ID);
        return (List<Role>)result;
    }

    public void setRoles(List<Role> value) {
        this.setFKBean(TableConstants.T_ROLE.CREATE_USER_ID, value);
    }

}