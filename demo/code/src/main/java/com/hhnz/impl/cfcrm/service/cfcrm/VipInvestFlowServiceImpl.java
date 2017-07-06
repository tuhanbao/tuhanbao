package com.hhnz.impl.cfcrm.service.cfcrm;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hhnz.api.cfcrm.model.cfcrm.VipInvestFlow;
import com.hhnz.api.cfcrm.service.cfcrm.IVipInvestFlowService;

@Service("vipInvestFlowService")
@Transactional("cfcrmTransactionManager")
public class VipInvestFlowServiceImpl extends ServiceImpl<VipInvestFlow> implements IVipInvestFlowService {
}