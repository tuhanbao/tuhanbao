package com.tuhanbao.impl.crm.service.crm;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tuhanbao.api.crm.model.crm.FollowRecord;
import com.tuhanbao.api.crm.service.crm.IFollowRecordService;

@Service("followRecordService")
@Transactional("crmTransactionManager")
public class FollowRecordServiceImpl extends CTServiceImpl<FollowRecord> implements IFollowRecordService {
}