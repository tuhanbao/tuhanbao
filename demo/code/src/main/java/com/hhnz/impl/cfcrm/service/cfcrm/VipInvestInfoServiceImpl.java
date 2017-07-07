package com.hhnz.impl.cfcrm.service.cfcrm;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hhnz.api.cfcrm.model.cfcrm.VipInvestInfo;
import com.hhnz.api.cfcrm.service.cfcrm.IVipInvestInfoService;

@Service("vipInvestInfoService")
@Transactional("cfcrmTransactionManager")
public class VipInvestInfoServiceImpl extends ServiceImpl<VipInvestInfo> implements IVipInvestInfoService {
}