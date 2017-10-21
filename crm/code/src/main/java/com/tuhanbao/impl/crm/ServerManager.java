package com.tuhanbao.impl.crm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.tuhanbao.web.IServerManager;
import com.tuhanbao.impl.crm.mapper.crm.CrmMapper;
import com.tuhanbao.web.filter.SelectorFactory;
import com.tuhanbao.web.filter.MyBatisSelectorFilter;
import com.tuhanbao.base.dataservice.ServiceBean;
import com.tuhanbao.thirdapi.cache.CacheManager;
import com.tuhanbao.api.crm.constants.TableConstants;


public class ServerManager implements ApplicationListener<ContextRefreshedEvent> {
    private static boolean isInit = false;
    	
    @Autowired
    private IServerManager serverManger;
    
    @Autowired
    private CrmMapper<ServiceBean> crmMapper;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        init();
    }
    
    public void init() {
        if (!isInit) {
            //初始化redis缓存
            if (!CacheManager.hasCacheDataGroup(TableConstants.T_COMPANY.TABLE)) {
                CacheManager.save(crmMapper.select(new MyBatisSelectorFilter(SelectorFactory.getTablesSelector(TableConstants.T_COMPANY.TABLE))));
            }
            if (!CacheManager.hasCacheDataGroup(TableConstants.T_COMPANY_SMS_INFO.TABLE)) {
                CacheManager.save(crmMapper.select(new MyBatisSelectorFilter(SelectorFactory.getTablesSelector(TableConstants.T_COMPANY_SMS_INFO.TABLE))));
            }
            if (!CacheManager.hasCacheDataGroup(TableConstants.T_DIY_COL_INFO.TABLE)) {
                CacheManager.save(crmMapper.select(new MyBatisSelectorFilter(SelectorFactory.getTablesSelector(TableConstants.T_DIY_COL_INFO.TABLE))));
            }


            if (serverManger != null) serverManger.init();
            isInit = true;
        }
    }
    
}
