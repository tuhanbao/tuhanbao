package com.tuhanbao.impl.crm.service.crm;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tuhanbao.api.crm.model.crm.DiyColInfo;
import com.tuhanbao.api.crm.service.crm.IDiyColInfoService;

@Service("diyColInfoService")
@Transactional("crmTransactionManager")
public class DiyColInfoServiceImpl extends ServiceImpl<DiyColInfo> implements IDiyColInfoService {
}