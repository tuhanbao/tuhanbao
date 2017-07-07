package com.hhnz.impl.cfcrm.service.fz;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hhnz.api.cfcrm.model.fz.SeparateBillsApply;
import com.hhnz.api.cfcrm.service.fz.ISeparateBillsApplyService;

@Service("separateBillsApplyService")
@Transactional("fzTransactionManager")
public class SeparateBillsApplyServiceImpl extends ServiceImpl<SeparateBillsApply> implements ISeparateBillsApplyService {
}