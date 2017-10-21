package com.tuhanbao.api.crm.service.crm;

import com.tuhanbao.web.service.IService;
import com.tuhanbao.api.crm.model.crm.Permission;

public interface IPermissionService extends IService<Permission> {

    void deletePermission(long permissionId);
}