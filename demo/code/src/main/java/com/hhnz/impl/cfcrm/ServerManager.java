package com.hhnz.impl.cfcrm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.tuhanbao.web.IServerManager;
import com.hhnz.impl.cfcrm.mapper.cfcrm.CfcrmMapper;
import com.tuhanbao.web.filter.SelectorFactory;
import com.tuhanbao.web.filter.MyBatisSelectorFilter;
import com.tuhanbao.base.dataservice.ServiceBean;
import com.tuhanbao.thirdapi.cache.CacheManager;
import com.hhnz.api.cfcrm.constants.TableConstants;


public class ServerManager implements ApplicationListener<ContextRefreshedEvent> {
    private static boolean isInit = false;
    	
    @Autowired
    private IServerManager serverManger;
    
    @Autowired
    private CfcrmMapper<ServiceBean> cfcrmMapper;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        init();
    }
    
    public void init() {
        if (!isInit) {
            //初始化redis缓存
            if (!CacheManager.hasCacheDataGroup(TableConstants.T_VIP_PRIMARY_INFO.TABLE)) {
                CacheManager.save(cfcrmMapper.select(new MyBatisSelectorFilter(SelectorFactory.getTablesSelector(TableConstants.T_VIP_PRIMARY_INFO.TABLE))));
            }


            if (serverManger != null) serverManger.init();
            isInit = true;
        }
    }
    
}
