package com.sztx.se.dataaccess.redis.source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sztx.se.core.context.SpringContextHolder;
import com.sztx.se.dataaccess.redis.template.RedisTemplateProxy;

/**
 * 动态创建redis数据源的工具类
 * 
 * @author Administrator
 * 
 */
public class DynamicCreateRedisSourceManager {

	private DynamicRedisSource dynamicRedisSource;

	private List<RedisTemplateProxy> redisTemplateProxyList;

	public void setDynamicRedisSource(DynamicRedisSource dynamicRedisSource) {
		this.dynamicRedisSource = dynamicRedisSource;
	}

	public void setRedisTemplateProxyList(List<RedisTemplateProxy> redisTemplateProxyList) {
		this.redisTemplateProxyList = redisTemplateProxyList;
	}

	/**
	 * 初始化缓存数据源
	 * 
	 * @param applicationContext
	 */
	public void initCreateRedisSource() {
		registerRedisSource();
	}

	/**
	 * 根据公共库中各仓数据源动态创建Bean并注册到application中,刷新动态数据源
	 * 
	 */
	private void registerRedisSource() {
		Map<String, RedisTemplateProxy> targetRedisSourceMap = new HashMap<String, RedisTemplateProxy>();
		List<RedisTemplateProxy> redisTemplateProxyList = new ArrayList<RedisTemplateProxy>();
		RedisTemplateProxy defaultTargetRedisSource = null;
		
		if (this.redisTemplateProxyList == null || this.redisTemplateProxyList.isEmpty()) {
			Map<String, RedisTemplateProxy> redisTemplateProxyMap = SpringContextHolder.applicationContext.getBeansOfType(RedisTemplateProxy.class);

			if (redisTemplateProxyMap != null && !redisTemplateProxyMap.isEmpty()) {
				for (Entry<String, RedisTemplateProxy> en : redisTemplateProxyMap.entrySet()) {
					redisTemplateProxyList.add(en.getValue());
				}
			}
		} else {
			redisTemplateProxyList = this.redisTemplateProxyList;
		}

		for (RedisTemplateProxy redisTemplateProxy : redisTemplateProxyList) {
			String redisSourceKey = redisTemplateProxy.getRedisSourceKey();
			targetRedisSourceMap.put(redisSourceKey, redisTemplateProxy);
			boolean isDefault = redisTemplateProxy.getIsDefault();
			
			if (isDefault) {
				defaultTargetRedisSource = redisTemplateProxy;
			}
		}

		dynamicRedisSource.setTargetRedisSources(targetRedisSourceMap);
		dynamicRedisSource.setDefaultTargetRedisSource(defaultTargetRedisSource);
		dynamicRedisSource.afterPropertiesSet();
	}

}