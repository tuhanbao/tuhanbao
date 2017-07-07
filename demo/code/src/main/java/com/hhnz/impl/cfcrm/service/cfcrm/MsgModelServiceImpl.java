package com.hhnz.impl.cfcrm.service.cfcrm;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hhnz.api.cfcrm.model.cfcrm.MsgModel;
import com.hhnz.api.cfcrm.service.cfcrm.IMsgModelService;

@Service("msgModelService")
@Transactional("cfcrmTransactionManager")
public class MsgModelServiceImpl extends ServiceImpl<MsgModel> implements IMsgModelService {
}