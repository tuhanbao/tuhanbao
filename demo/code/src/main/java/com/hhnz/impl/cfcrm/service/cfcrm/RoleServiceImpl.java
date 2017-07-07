package com.hhnz.impl.cfcrm.service.cfcrm;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hhnz.api.cfcrm.model.cfcrm.Role;
import com.hhnz.api.cfcrm.service.cfcrm.IRoleService;

@Service("roleService")
@Transactional("cfcrmTransactionManager")
public class RoleServiceImpl extends ServiceImpl<Role> implements IRoleService {
}