package com.tuhanbao.impl.crm.service.crm;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tuhanbao.api.crm.model.crm.SmsRecord;
import com.tuhanbao.api.crm.service.crm.ISmsRecordService;

@Service("smsRecordService")
@Transactional("crmTransactionManager")
public class SmsRecordServiceImpl extends CTServiceImpl<SmsRecord> implements ISmsRecordService {
}