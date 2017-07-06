package com.hhnz.impl.cfcrm.service.cfcrm;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hhnz.api.cfcrm.model.cfcrm.VipPrimaryInfo;
import com.hhnz.api.cfcrm.service.cfcrm.IVipPrimaryInfoService;

@Service("vipPrimaryInfoService")
@Transactional("cfcrmTransactionManager")
public class VipPrimaryInfoServiceImpl extends ServiceImpl<VipPrimaryInfo> implements IVipPrimaryInfoService {
}