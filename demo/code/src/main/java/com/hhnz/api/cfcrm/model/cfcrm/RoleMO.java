package com.hhnz.api.cfcrm.model.cfcrm;

import com.tuhanbao.base.dataservice.ServiceBean;
import com.hhnz.api.cfcrm.constants.TableConstants;
import com.tuhanbao.base.dataservice.MetaObject;
import com.tuhanbao.base.util.db.table.data.LongValue;
import com.tuhanbao.base.util.db.table.data.StringValue;
import java.util.Date;
import com.tuhanbao.base.util.db.table.data.TimeValue;
import java.util.List;

public class RoleMO extends ServiceBean {
    protected RoleMO() {
        this(new MetaObject(TableConstants.T_ROLE.TABLE));
    }

    protected RoleMO(MetaObject mo) {
        super(mo);
    }

    public long getId() {
        LongValue value = (LongValue)getValue(TableConstants.T_ROLE.ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setId(Long value) {
        setValue(TableConstants.T_ROLE.ID, LongValue.valueOf(value));
    }

    public String getRoleName() {
        StringValue value = (StringValue)getValue(TableConstants.T_ROLE.ROLE_NAME);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setRoleName(String value) {
        setValue(TableConstants.T_ROLE.ROLE_NAME, StringValue.valueOf(value));
    }

    public String getMark() {
        StringValue value = (StringValue)getValue(TableConstants.T_ROLE.MARK);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setMark(String value) {
        setValue(TableConstants.T_ROLE.MARK, StringValue.valueOf(value));
    }

    public Date getCreateDate() {
        TimeValue value = (TimeValue)getValue(TableConstants.T_ROLE.CREATE_DATE);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setCreateDate(Date value) {
        setValue(TableConstants.T_ROLE.CREATE_DATE, TimeValue.valueOf(value));
    }

    public long getCreateUserId() {
        LongValue value = (LongValue)getValue(TableConstants.T_ROLE.CREATE_USER_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setCreateUserId(Long value) {
        setValue(TableConstants.T_ROLE.CREATE_USER_ID, LongValue.valueOf(value));
    }

    public User getCreateUser() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_ROLE.CREATE_USER_ID);
        return result == null || result.isEmpty() ? null : (User)result.get(0);
    }

    public User removeCreateUser() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_ROLE.CREATE_USER_ID);
        return result == null || result.isEmpty() ? null : (User)result.get(0);
    }

    public void setCreateUser(User value) {
        this.setFKBean(TableConstants.T_ROLE.CREATE_USER_ID, value);
    }

    @SuppressWarnings("unchecked")
    public List<User> getUsers() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_USER.ROLE_ID);
        return (List<User>)result;
    }

    @SuppressWarnings("unchecked")
    public List<User> removeUsers() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_USER.ROLE_ID);
        return (List<User>)result;
    }

    public void setUsers(List<User> value) {
        this.setFKBean(TableConstants.T_USER.ROLE_ID, value);
    }

    @SuppressWarnings("unchecked")
    public List<RolePermission> getRolePermissions() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_ROLE_PERMISSION.ROLE_ID);
        return (List<RolePermission>)result;
    }

    @SuppressWarnings("unchecked")
    public List<RolePermission> removeRolePermissions() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_ROLE_PERMISSION.ROLE_ID);
        return (List<RolePermission>)result;
    }

    public void setRolePermissions(List<RolePermission> value) {
        this.setFKBean(TableConstants.T_ROLE_PERMISSION.ROLE_ID, value);
    }

}