package com.hhnz.impl.cfcrm.service.fz;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hhnz.api.cfcrm.model.fz.Relation;
import com.hhnz.api.cfcrm.service.fz.IRelationService;

@Service("relationService")
@Transactional("fzTransactionManager")
public class RelationServiceImpl extends ServiceImpl<Relation> implements IRelationService {
}