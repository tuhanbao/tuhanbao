package com.hhnz.impl.cfcrm.service.cfcrm;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hhnz.api.cfcrm.model.cfcrm.MsgAutoSend;
import com.hhnz.api.cfcrm.service.cfcrm.IMsgAutoSendService;

@Service("msgAutoSendService")
@Transactional("cfcrmTransactionManager")
public class MsgAutoSendServiceImpl extends ServiceImpl<MsgAutoSend> implements IMsgAutoSendService {
}