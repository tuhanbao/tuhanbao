package com.hhnz.impl.cfcrm.service.cfcrm;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hhnz.api.cfcrm.model.cfcrm.MsgGroup;
import com.hhnz.api.cfcrm.service.cfcrm.IMsgGroupService;

@Service("msgGroupService")
@Transactional("cfcrmTransactionManager")
public class MsgGroupServiceImpl extends ServiceImpl<MsgGroup> implements IMsgGroupService {
}