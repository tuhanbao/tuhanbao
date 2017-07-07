package com.hhnz.impl.cfcrm.service.cfcrm;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hhnz.api.cfcrm.model.cfcrm.RolePermission;
import com.hhnz.api.cfcrm.service.cfcrm.IRolePermissionService;

@Service("rolePermissionService")
@Transactional("cfcrmTransactionManager")
public class RolePermissionServiceImpl extends ServiceImpl<RolePermission> implements IRolePermissionService {
}