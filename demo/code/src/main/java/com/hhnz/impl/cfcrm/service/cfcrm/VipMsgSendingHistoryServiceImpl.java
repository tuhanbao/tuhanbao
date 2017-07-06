package com.hhnz.impl.cfcrm.service.cfcrm;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hhnz.api.cfcrm.model.cfcrm.VipMsgSendingHistory;
import com.hhnz.api.cfcrm.service.cfcrm.IVipMsgSendingHistoryService;

@Service("vipMsgSendingHistoryService")
@Transactional("cfcrmTransactionManager")
public class VipMsgSendingHistoryServiceImpl extends ServiceImpl<VipMsgSendingHistory> implements IVipMsgSendingHistoryService {
}