package com.sztx.se.dataaccess.fastdfs.source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sztx.se.core.context.SpringContextHolder;
import com.sztx.se.dataaccess.fastdfs.client.FastdfsClientProxy;

/**
 * 动态创建Fastdfs数据源的工具类
 * 
 * @author Administrator
 * 
 */
public class DynamicCreateFastdfsSourceManager {

	private DynamicFastdfsSource dynamicFastdfsSource;

	private List<FastdfsClientProxy> fastdfsClientProxyList;

	public void setDynamicFastdfsSource(DynamicFastdfsSource dynamicFastdfsSource) {
		this.dynamicFastdfsSource = dynamicFastdfsSource;
	}

	public void setFastdfsClientProxyList(List<FastdfsClientProxy> fastdfsClientProxyList) {
		this.fastdfsClientProxyList = fastdfsClientProxyList;
	}

	/**
	 * 初始化Fastdfs数据源
	 * 
	 * @param applicationContext
	 */
	public void initCreateFastdfsSource() {
		registerFastdfsSource();
	}

	/**
	 * 动态创建Bean并注册到application中,刷新动态数据源
	 * 
	 */
	private void registerFastdfsSource() {
		Map<String, FastdfsClientProxy> targetFastdfsSourceMap = new HashMap<String, FastdfsClientProxy>();
		List<FastdfsClientProxy> fastdfsClientProxyList = new ArrayList<FastdfsClientProxy>();
		FastdfsClientProxy defaultTargetFastdfsSource = null;

		if (this.fastdfsClientProxyList == null || this.fastdfsClientProxyList.isEmpty()) {
			Map<String, FastdfsClientProxy> fastdfsClientProxyMap = SpringContextHolder.applicationContext.getBeansOfType(FastdfsClientProxy.class);

			if (fastdfsClientProxyMap != null && !fastdfsClientProxyMap.isEmpty()) {
				for (Entry<String, FastdfsClientProxy> en : fastdfsClientProxyMap.entrySet()) {
					fastdfsClientProxyList.add(en.getValue());
				}
			}
		} else {
			fastdfsClientProxyList = this.fastdfsClientProxyList;
		}

		for (FastdfsClientProxy fastdfsClientProxy : fastdfsClientProxyList) {
			String fastdfsSourceKey = fastdfsClientProxy.getFastdfsSourceKey();
			targetFastdfsSourceMap.put(fastdfsSourceKey, fastdfsClientProxy);
			boolean isDefault = fastdfsClientProxy.getIsDefault();

			if (isDefault) {
				defaultTargetFastdfsSource = fastdfsClientProxy;
			}
		}

		dynamicFastdfsSource.setTargetFastdfsSources(targetFastdfsSourceMap);
		dynamicFastdfsSource.setDefaultTargetFastdfsSource(defaultTargetFastdfsSource);
		dynamicFastdfsSource.afterPropertiesSet();
	}

}