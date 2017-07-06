package com.hhnz.impl.cfcrm.service.cfcrm;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hhnz.api.cfcrm.model.cfcrm.SendHistoryVip;
import com.hhnz.api.cfcrm.service.cfcrm.ISendHistoryVipService;

@Service("sendHistoryVipService")
@Transactional("cfcrmTransactionManager")
public class SendHistoryVipServiceImpl extends ServiceImpl<SendHistoryVip> implements ISendHistoryVipService {
}