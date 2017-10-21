package com.tuhanbao.impl.crm.service.crm;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tuhanbao.api.crm.model.crm.DiyFilterItem;
import com.tuhanbao.api.crm.service.crm.IDiyFilterItemService;

@Service("diyFilterItemService")
@Transactional("crmTransactionManager")
public class DiyFilterItemServiceImpl extends ServiceImpl<DiyFilterItem> implements IDiyFilterItemService {
}