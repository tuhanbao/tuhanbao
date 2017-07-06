package com.hhnz.api.cfcrm.model.cfcrm;

import com.tuhanbao.base.dataservice.ServiceBean;
import com.hhnz.api.cfcrm.constants.TableConstants;
import com.tuhanbao.base.dataservice.MetaObject;
import com.tuhanbao.base.util.db.table.data.LongValue;
import java.util.List;

public class RolePermissionMO extends ServiceBean {
    protected RolePermissionMO() {
        this(new MetaObject(TableConstants.T_ROLE_PERMISSION.TABLE));
    }

    protected RolePermissionMO(MetaObject mo) {
        super(mo);
    }

    public long getId() {
        LongValue value = (LongValue)getValue(TableConstants.T_ROLE_PERMISSION.ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setId(Long value) {
        setValue(TableConstants.T_ROLE_PERMISSION.ID, LongValue.valueOf(value));
    }

    public long getPermissionId() {
        LongValue value = (LongValue)getValue(TableConstants.T_ROLE_PERMISSION.PERMISSION_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setPermissionId(Long value) {
        setValue(TableConstants.T_ROLE_PERMISSION.PERMISSION_ID, LongValue.valueOf(value));
    }

    public Permission getPermission() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_ROLE_PERMISSION.PERMISSION_ID);
        return result == null || result.isEmpty() ? null : (Permission)result.get(0);
    }

    public Permission removePermission() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_ROLE_PERMISSION.PERMISSION_ID);
        return result == null || result.isEmpty() ? null : (Permission)result.get(0);
    }

    public void setPermission(Permission value) {
        this.setFKBean(TableConstants.T_ROLE_PERMISSION.PERMISSION_ID, value);
    }

    public long getRoleId() {
        LongValue value = (LongValue)getValue(TableConstants.T_ROLE_PERMISSION.ROLE_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setRoleId(Long value) {
        setValue(TableConstants.T_ROLE_PERMISSION.ROLE_ID, LongValue.valueOf(value));
    }

    public Role getRole() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_ROLE_PERMISSION.ROLE_ID);
        return result == null || result.isEmpty() ? null : (Role)result.get(0);
    }

    public Role removeRole() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_ROLE_PERMISSION.ROLE_ID);
        return result == null || result.isEmpty() ? null : (Role)result.get(0);
    }

    public void setRole(Role value) {
        this.setFKBean(TableConstants.T_ROLE_PERMISSION.ROLE_ID, value);
    }

}