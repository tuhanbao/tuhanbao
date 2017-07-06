package com.hhnz.impl.cfcrm.service.cfcrm;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hhnz.api.cfcrm.model.cfcrm.Permission;
import com.hhnz.api.cfcrm.service.cfcrm.IPermissionService;

@Service("permissionService")
@Transactional("cfcrmTransactionManager")
public class PermissionServiceImpl extends ServiceImpl<Permission> implements IPermissionService {
}