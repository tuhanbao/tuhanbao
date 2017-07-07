package com.hhnz.impl.cfcrm.service.cfcrm;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hhnz.api.cfcrm.model.cfcrm.VipAwardManage;
import com.hhnz.api.cfcrm.service.cfcrm.IVipAwardManageService;

@Service("vipAwardManageService")
@Transactional("cfcrmTransactionManager")
public class VipAwardManageServiceImpl extends ServiceImpl<VipAwardManage> implements IVipAwardManageService {
}