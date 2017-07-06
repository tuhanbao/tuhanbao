package com.hhnz.api.cfcrm.model.cfcrm;

import com.tuhanbao.base.dataservice.ServiceBean;
import com.hhnz.api.cfcrm.constants.TableConstants;
import com.tuhanbao.base.dataservice.MetaObject;
import com.tuhanbao.base.util.db.table.data.LongValue;
import java.util.List;
import com.tuhanbao.base.util.db.table.data.StringValue;
import com.tuhanbao.base.util.db.table.data.BooleanValue;
import java.util.Date;
import com.tuhanbao.base.util.db.table.data.TimeValue;
import com.tuhanbao.base.util.db.table.data.IntValue;

public class PermissionMO extends ServiceBean {
    protected PermissionMO() {
        this(new MetaObject(TableConstants.T_PERMISSION.TABLE));
    }

    protected PermissionMO(MetaObject mo) {
        super(mo);
    }

    public long getId() {
        LongValue value = (LongValue)getValue(TableConstants.T_PERMISSION.ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setId(Long value) {
        setValue(TableConstants.T_PERMISSION.ID, LongValue.valueOf(value));
    }

    public long getParentId() {
        LongValue value = (LongValue)getValue(TableConstants.T_PERMISSION.PARENT_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setParentId(Long value) {
        setValue(TableConstants.T_PERMISSION.PARENT_ID, LongValue.valueOf(value));
    }

    public Permission getParent() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_PERMISSION.PARENT_ID);
        return result == null || result.isEmpty() ? null : (Permission)result.get(0);
    }

    public Permission removeParent() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_PERMISSION.PARENT_ID);
        return result == null || result.isEmpty() ? null : (Permission)result.get(0);
    }

    public void setParent(Permission value) {
        this.setFKBean(TableConstants.T_PERMISSION.PARENT_ID, value);
    }

    public String getName() {
        StringValue value = (StringValue)getValue(TableConstants.T_PERMISSION.NAME);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setName(String value) {
        setValue(TableConstants.T_PERMISSION.NAME, StringValue.valueOf(value));
    }

    public boolean isMenu() {
        BooleanValue value = (BooleanValue)getValue(TableConstants.T_PERMISSION.IS_MENU);
        if (value == null) return false;
        else return value.getValue();
    }

    public void setIsMenu(Boolean value) {
        setValue(TableConstants.T_PERMISSION.IS_MENU, BooleanValue.valueOf(value));
    }

    public String getUrl() {
        StringValue value = (StringValue)getValue(TableConstants.T_PERMISSION.URL);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setUrl(String value) {
        setValue(TableConstants.T_PERMISSION.URL, StringValue.valueOf(value));
    }

    public Date getCreateDate() {
        TimeValue value = (TimeValue)getValue(TableConstants.T_PERMISSION.CREATE_DATE);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setCreateDate(Date value) {
        setValue(TableConstants.T_PERMISSION.CREATE_DATE, TimeValue.valueOf(value));
    }

    public Date getModifyDate() {
        TimeValue value = (TimeValue)getValue(TableConstants.T_PERMISSION.MODIFY_DATE);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setModifyDate(Date value) {
        setValue(TableConstants.T_PERMISSION.MODIFY_DATE, TimeValue.valueOf(value));
    }

    public int getSort() {
        IntValue value = (IntValue)getValue(TableConstants.T_PERMISSION.SORT);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setSort(Integer value) {
        setValue(TableConstants.T_PERMISSION.SORT, IntValue.valueOf(value));
    }

    public String getMark() {
        StringValue value = (StringValue)getValue(TableConstants.T_PERMISSION.MARK);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setMark(String value) {
        setValue(TableConstants.T_PERMISSION.MARK, StringValue.valueOf(value));
    }

    @SuppressWarnings("unchecked")
    public List<Permission> getPermissions() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_PERMISSION.PARENT_ID, true);
        return (List<Permission>)result;
    }

    @SuppressWarnings("unchecked")
    public List<Permission> removePermissions() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_PERMISSION.PARENT_ID, true);
        return (List<Permission>)result;
    }

    public void setPermissions(List<Permission> value) {
        this.setFKBean(TableConstants.T_PERMISSION.PARENT_ID, value, true);
    }

    @SuppressWarnings("unchecked")
    public List<RolePermission> getRolePermissions() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_ROLE_PERMISSION.PERMISSION_ID);
        return (List<RolePermission>)result;
    }

    @SuppressWarnings("unchecked")
    public List<RolePermission> removeRolePermissions() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_ROLE_PERMISSION.PERMISSION_ID);
        return (List<RolePermission>)result;
    }

    public void setRolePermissions(List<RolePermission> value) {
        this.setFKBean(TableConstants.T_ROLE_PERMISSION.PERMISSION_ID, value);
    }

}