package com.sztx.se.dataaccess.memcache.source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sztx.se.core.context.SpringContextHolder;
import com.sztx.se.dataaccess.memcache.client.MemcacheClientProxy;

/**
 * 动态创建memcache数据源的工具类
 * 
 * @author Administrator
 * 
 */
public class DynamicCreateMemcacheSourceManager {

	private DynamicMemcacheSource dynamicMemcacheSource;

	private List<MemcacheClientProxy> memcacheClientProxyList;

	public void setDynamicMemcacheSource(DynamicMemcacheSource dynamicMemcacheSource) {
		this.dynamicMemcacheSource = dynamicMemcacheSource;
	}

	public void setMemcacheClientProxyList(List<MemcacheClientProxy> memcacheClientProxyList) {
		this.memcacheClientProxyList = memcacheClientProxyList;
	}

	/**
	 * 初始化缓存数据源
	 * 
	 * @param applicationContext
	 */
	public void initCreateMemcacheSource() {
		registerMemcacheSource();
	}

	/**
	 * 根据公共库中各仓数据源动态创建Bean并注册到application中,刷新动态数据源
	 * 
	 */
	private void registerMemcacheSource() {
		Map<String, MemcacheClientProxy> targetMemcacheSources = new HashMap<String, MemcacheClientProxy>();
		List<MemcacheClientProxy> memcacheClientProxyList = new ArrayList<MemcacheClientProxy>();
		MemcacheClientProxy defaultTargetMemcacheSource = null;
		
		if (this.memcacheClientProxyList == null || this.memcacheClientProxyList.isEmpty()) {
			Map<String, MemcacheClientProxy> memcacheClientProxyMap = SpringContextHolder.applicationContext.getBeansOfType(MemcacheClientProxy.class);

			if (memcacheClientProxyMap != null && !memcacheClientProxyMap.isEmpty()) {
				for (Entry<String, MemcacheClientProxy> en : memcacheClientProxyMap.entrySet()) {
					memcacheClientProxyList.add(en.getValue());
				}
			}
		} else {
			memcacheClientProxyList = this.memcacheClientProxyList;
		}

		for (MemcacheClientProxy memcacheClientProxy : memcacheClientProxyList) {
			String memcacheSourceKey = memcacheClientProxy.getMemcacheSourceKey();
			targetMemcacheSources.put(memcacheSourceKey, memcacheClientProxy);
			boolean isDefault = memcacheClientProxy.getIsDefault();
			
			if (isDefault) {
				defaultTargetMemcacheSource = memcacheClientProxy;
			}
		}

		dynamicMemcacheSource.setTargetMemcacheSources(targetMemcacheSources);
		dynamicMemcacheSource.setDefaultTargetMemcacheSource(defaultTargetMemcacheSource);
		dynamicMemcacheSource.afterPropertiesSet();
	}

}