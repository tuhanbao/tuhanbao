package com.hhnz.impl.cfcrm.service.fz;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hhnz.api.cfcrm.model.fz.CExtendInfo;
import com.hhnz.api.cfcrm.service.fz.ICExtendInfoService;

@Service("cExtendInfoService")
@Transactional("fzTransactionManager")
public class CExtendInfoServiceImpl extends ServiceImpl<CExtendInfo> implements ICExtendInfoService {
}