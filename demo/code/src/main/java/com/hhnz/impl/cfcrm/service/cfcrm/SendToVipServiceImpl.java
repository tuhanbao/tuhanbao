package com.hhnz.impl.cfcrm.service.cfcrm;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hhnz.api.cfcrm.model.cfcrm.SendToVip;
import com.hhnz.api.cfcrm.service.cfcrm.ISendToVipService;

@Service("sendToVipService")
@Transactional("cfcrmTransactionManager")
public class SendToVipServiceImpl extends ServiceImpl<SendToVip> implements ISendToVipService {
}