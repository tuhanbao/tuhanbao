package com.hhnz.impl.cfcrm.service.cfcrm;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hhnz.api.cfcrm.model.cfcrm.AwardItem;
import com.hhnz.api.cfcrm.service.cfcrm.IAwardItemService;

@Service("awardItemService")
@Transactional("cfcrmTransactionManager")
public class AwardItemServiceImpl extends ServiceImpl<AwardItem> implements IAwardItemService {
}