package com.hhnz.impl.cfcrm.service.fz;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hhnz.api.cfcrm.model.fz.Role;
import com.hhnz.api.cfcrm.service.fz.IRoleService;

@Service("roleService")
@Transactional("fzTransactionManager")
public class RoleServiceImpl extends ServiceImpl<Role> implements IRoleService {
}