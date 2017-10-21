package com.tuhanbao.impl.crm.service.crm;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tuhanbao.api.crm.model.crm.CompanySmsInfo;
import com.tuhanbao.api.crm.service.crm.ICompanySmsInfoService;

@Service("companySmsInfoService")
@Transactional("crmTransactionManager")
public class CompanySmsInfoServiceImpl extends ServiceImpl<CompanySmsInfo> implements ICompanySmsInfoService {
}