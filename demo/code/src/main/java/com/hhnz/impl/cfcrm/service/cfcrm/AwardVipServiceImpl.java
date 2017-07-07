package com.hhnz.impl.cfcrm.service.cfcrm;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hhnz.api.cfcrm.model.cfcrm.AwardVip;
import com.hhnz.api.cfcrm.service.cfcrm.IAwardVipService;

@Service("awardVipService")
@Transactional("cfcrmTransactionManager")
public class AwardVipServiceImpl extends ServiceImpl<AwardVip> implements IAwardVipService {
}