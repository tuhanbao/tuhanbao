package com.hhnz.impl.cfcrm.service.cfcrm;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hhnz.api.cfcrm.model.cfcrm.User;
import com.hhnz.api.cfcrm.service.cfcrm.IUserService;

@Service("userService")
@Transactional("cfcrmTransactionManager")
public class UserServiceImpl extends ServiceImpl<User> implements IUserService {
}