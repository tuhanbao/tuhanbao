package com.tuhanbao.api.crm.model.crm;

import java.util.ArrayList;
import java.util.List;

import com.tuhanbao.web.controller.authority.IPermission;
import com.tuhanbao.web.controller.authority.IRole;

public class Role extends RoleMO implements IRole {
    
    private List<IPermission> permissions;
    
    public Role() {

    }
    
    @Override
    public List<IPermission> getPermissions() {
        if (permissions == null) {
            permissions = new ArrayList<IPermission>();
            List<RolePermission> rps = this.removeRolePermissions();
            if (rps != null) {
                for (RolePermission rp : rps) {
                    Permission permission = rp.getPermission();
                    if (permission != null) {
                        permissions.add(permission);
                    }
                }
            }
        }
        return permissions;
    }

}