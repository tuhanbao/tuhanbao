package com.tuhanbao.api.crm.service.crm;

import com.tuhanbao.web.service.IService;

import java.util.List;

import com.tuhanbao.api.crm.model.crm.Role;

public interface IRoleService extends IService<Role> {
    void updatePermission(long roleId, List<Long> permissionIds);
}