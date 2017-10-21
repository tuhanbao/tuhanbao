package com.tuhanbao.api.crm.service.crm;

import com.tuhanbao.web.service.IService;

import java.util.List;

import com.tuhanbao.api.crm.model.crm.RolePermission;

public interface IRolePermissionService extends IService<RolePermission> {
    void addRolePermission(long roleId, long permissionId);

    void removeRolePermission(long roleId, long permissionId);
    
    List<RolePermission> getAllAuthByRoleId(long roleId);
}