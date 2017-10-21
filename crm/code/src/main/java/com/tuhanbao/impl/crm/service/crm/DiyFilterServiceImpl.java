package com.tuhanbao.impl.crm.service.crm;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tuhanbao.api.crm.model.crm.DiyFilter;
import com.tuhanbao.api.crm.service.crm.IDiyFilterService;

@Service("diyFilterService")
@Transactional("crmTransactionManager")
public class DiyFilterServiceImpl extends ServiceImpl<DiyFilter> implements IDiyFilterService {
}