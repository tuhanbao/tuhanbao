package com.sztx.se.dataaccess.mongo.source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sztx.se.core.context.SpringContextHolder;
import com.sztx.se.dataaccess.mongo.template.MongoTemplateProxy;

/**
 * 动态创建mongodb数据源的工具类
 * 
 * @author zhihongp
 * 
 */
public class DynamicCreateMongoSourceManager {

	private DynamicMongoSource dynamicMongoSource;

	private List<MongoTemplateProxy> mongoTemplateProxyList;

	public void setDynamicMongoSource(DynamicMongoSource dynamicMongoSource) {
		this.dynamicMongoSource = dynamicMongoSource;
	}

	public void setMongoTemplateProxyList(List<MongoTemplateProxy> mongoTemplateProxyList) {
		this.mongoTemplateProxyList = mongoTemplateProxyList;
	}

	/**
	 * 初始化Abase数据源
	 * 
	 * @param applicationContext
	 */
	public void initCreateMongoSource() {
		registerMongoSource();
	}

	/**
	 * 动态创建Bean并注册到application中,刷新动态数据源
	 * 
	 */
	private void registerMongoSource() {
		Map<String, MongoTemplateProxy> targetMongoSourceMap = new HashMap<String, MongoTemplateProxy>();
		List<MongoTemplateProxy> mongoTemplateProxyList = new ArrayList<MongoTemplateProxy>();
		MongoTemplateProxy defaultTargetMongoSource = null;

		if (this.mongoTemplateProxyList == null || this.mongoTemplateProxyList.isEmpty()) {
			Map<String, MongoTemplateProxy> mongoTemplateProxyMap = SpringContextHolder.applicationContext.getBeansOfType(MongoTemplateProxy.class);

			if (mongoTemplateProxyMap != null && !mongoTemplateProxyMap.isEmpty()) {
				for (Entry<String, MongoTemplateProxy> en : mongoTemplateProxyMap.entrySet()) {
					mongoTemplateProxyList.add(en.getValue());
				}
			}
		} else {
			mongoTemplateProxyList = this.mongoTemplateProxyList;
		}

		for (MongoTemplateProxy mongoTemplateProxy : mongoTemplateProxyList) {
			String mongoSourceKey = mongoTemplateProxy.getMongoSourceKey();
			targetMongoSourceMap.put(mongoSourceKey, mongoTemplateProxy);
			boolean isDefault = mongoTemplateProxy.getIsDefault();
			
			if (isDefault) {
				defaultTargetMongoSource = mongoTemplateProxy;
			}
		}

		dynamicMongoSource.setTargetMongoSources(targetMongoSourceMap);
		dynamicMongoSource.setDefaultTargetMongoSource(defaultTargetMongoSource);
		dynamicMongoSource.afterPropertiesSet();
	}

}