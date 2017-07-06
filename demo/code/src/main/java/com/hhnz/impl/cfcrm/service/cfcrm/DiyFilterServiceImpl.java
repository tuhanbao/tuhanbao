package com.hhnz.impl.cfcrm.service.cfcrm;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hhnz.api.cfcrm.model.cfcrm.DiyFilter;
import com.hhnz.api.cfcrm.service.cfcrm.IDiyFilterService;

@Service("diyFilterService")
@Transactional("cfcrmTransactionManager")
public class DiyFilterServiceImpl extends ServiceImpl<DiyFilter> implements IDiyFilterService {
}