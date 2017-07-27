package com.sztx.se.dataaccess.hbase.source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sztx.se.core.context.SpringContextHolder;
import com.sztx.se.dataaccess.hbase.template.HbaseTemplateProxy;

/**
 * 动态创建redis数据源的工具类
 * 
 * @author Administrator
 * 
 */
public class DynamicCreateHbaseSourceManager {

	private DynamicHbaseSource dynamicHbaseSource;

	private List<HbaseTemplateProxy> hbaseTemplateProxyList;

	public void setDynamicHbaseSource(DynamicHbaseSource dynamicHbaseSource) {
		this.dynamicHbaseSource = dynamicHbaseSource;
	}

	public void setHbaseTemplateProxyList(List<HbaseTemplateProxy> hbaseTemplateProxyList) {
		this.hbaseTemplateProxyList = hbaseTemplateProxyList;
	}

	/**
	 * 初始化Abase数据源
	 * 
	 * @param applicationContext
	 */
	public void initCreateHbaseSource() {
		registerHbaseSource();
	}

	/**
	 * 动态创建Bean并注册到application中,刷新动态数据源
	 * 
	 */
	private void registerHbaseSource() {
		Map<String, HbaseTemplateProxy> targetHbaseSourceMap = new HashMap<String, HbaseTemplateProxy>();
		List<HbaseTemplateProxy> hbaseTemplateProxyList = new ArrayList<HbaseTemplateProxy>();
		HbaseTemplateProxy defaultTargetHbaseSource = null;

		if (this.hbaseTemplateProxyList == null || this.hbaseTemplateProxyList.isEmpty()) {
			Map<String, HbaseTemplateProxy> hbaseTemplateProxyMap = SpringContextHolder.applicationContext.getBeansOfType(HbaseTemplateProxy.class);

			if (hbaseTemplateProxyMap != null && !hbaseTemplateProxyMap.isEmpty()) {
				for (Entry<String, HbaseTemplateProxy> en : hbaseTemplateProxyMap.entrySet()) {
					hbaseTemplateProxyList.add(en.getValue());
				}
			}
		} else {
			hbaseTemplateProxyList = this.hbaseTemplateProxyList;
		}

		for (HbaseTemplateProxy hbaseTemplateProxy : hbaseTemplateProxyList) {
			String hbaseSourceKey = hbaseTemplateProxy.getHbaseSourceKey();
			targetHbaseSourceMap.put(hbaseSourceKey, hbaseTemplateProxy);
			boolean isDefault = hbaseTemplateProxy.getIsDefault();
			
			if (isDefault) {
				defaultTargetHbaseSource = hbaseTemplateProxy;
			}
		}

		dynamicHbaseSource.setTargetHbaseSources(targetHbaseSourceMap);
		dynamicHbaseSource.setDefaultTargetHbaseSource(defaultTargetHbaseSource);
		dynamicHbaseSource.afterPropertiesSet();
	}

}