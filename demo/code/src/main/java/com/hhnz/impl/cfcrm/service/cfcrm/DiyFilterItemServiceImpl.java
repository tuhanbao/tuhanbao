package com.hhnz.impl.cfcrm.service.cfcrm;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hhnz.api.cfcrm.model.cfcrm.DiyFilterItem;
import com.hhnz.api.cfcrm.service.cfcrm.IDiyFilterItemService;

@Service("diyFilterItemService")
@Transactional("cfcrmTransactionManager")
public class DiyFilterItemServiceImpl extends ServiceImpl<DiyFilterItem> implements IDiyFilterItemService {
}