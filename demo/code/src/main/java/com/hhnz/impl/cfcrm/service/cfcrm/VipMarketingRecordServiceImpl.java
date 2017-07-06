package com.hhnz.impl.cfcrm.service.cfcrm;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hhnz.api.cfcrm.model.cfcrm.VipMarketingRecord;
import com.hhnz.api.cfcrm.service.cfcrm.IVipMarketingRecordService;

@Service("vipMarketingRecordService")
@Transactional("cfcrmTransactionManager")
public class VipMarketingRecordServiceImpl extends ServiceImpl<VipMarketingRecord> implements IVipMarketingRecordService {
}