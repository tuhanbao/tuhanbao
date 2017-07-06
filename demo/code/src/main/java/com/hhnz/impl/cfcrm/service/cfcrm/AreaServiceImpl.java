package com.hhnz.impl.cfcrm.service.cfcrm;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hhnz.api.cfcrm.model.cfcrm.Area;
import com.hhnz.api.cfcrm.service.cfcrm.IAreaService;

@Service("areaService")
@Transactional("cfcrmTransactionManager")
public class AreaServiceImpl extends ServiceImpl<Area> implements IAreaService {
}