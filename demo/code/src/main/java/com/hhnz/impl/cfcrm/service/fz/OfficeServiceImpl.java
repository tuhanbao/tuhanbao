package com.hhnz.impl.cfcrm.service.fz;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hhnz.api.cfcrm.model.fz.Office;
import com.hhnz.api.cfcrm.service.fz.IOfficeService;

@Service("officeService")
@Transactional("fzTransactionManager")
public class OfficeServiceImpl extends ServiceImpl<Office> implements IOfficeService {
}