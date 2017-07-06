package com.hhnz.impl.cfcrm.service.fz;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hhnz.api.cfcrm.model.fz.UsersBan;
import com.hhnz.api.cfcrm.service.fz.IUsersBanService;

@Service("usersBanService")
@Transactional("fzTransactionManager")
public class UsersBanServiceImpl extends ServiceImpl<UsersBan> implements IUsersBanService {
}