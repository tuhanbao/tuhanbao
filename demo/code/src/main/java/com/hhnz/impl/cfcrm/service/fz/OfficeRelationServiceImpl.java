package com.hhnz.impl.cfcrm.service.fz;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hhnz.api.cfcrm.model.fz.OfficeRelation;
import com.hhnz.api.cfcrm.service.fz.IOfficeRelationService;

@Service("officeRelationService")
@Transactional("fzTransactionManager")
public class OfficeRelationServiceImpl extends ServiceImpl<OfficeRelation> implements IOfficeRelationService {
}