package com.tuhanbao.impl.crm.service.crm;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tuhanbao.api.crm.model.crm.Custom;
import com.tuhanbao.api.crm.service.crm.ICustomService;

@Service("customService")
@Transactional("crmTransactionManager")
public class CustomServiceImpl extends CTServiceImpl<Custom> implements ICustomService {
}