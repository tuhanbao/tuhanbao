package com.tuhanbao.web.controller.authority;

import java.util.List;

public interface IPermissionManagerService {

    List<IRole> getAllRoles();

    List<IPermission> getAllPermissions();

    void addPermission(long roleId, IPermission permission);

    void removePermission(long roleId, IPermission permission);
    
    void deletePermission(IPermission permission);
    
    void deleteRole(long roleId);
}
