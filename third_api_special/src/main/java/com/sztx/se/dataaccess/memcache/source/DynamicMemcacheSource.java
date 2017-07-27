package com.sztx.se.dataaccess.memcache.source;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.sztx.se.dataaccess.memcache.callback.MemcacheCallback;
import com.sztx.se.dataaccess.memcache.client.MemcacheClientProxy;
import com.sztx.se.dataaccess.memcache.client.XMemcachedClientProxy;
import com.tuhanbao.base.util.exception.MyException;

/**
 * 
 * @author zhihongp
 * 
 */
public class DynamicMemcacheSource {

	private MemcacheClientProxy defaultTargetMemcacheSource;

	private Map<String, MemcacheClientProxy> targetMemcacheSources;

	public void setDefaultTargetMemcacheSource(MemcacheClientProxy defaultTargetMemcacheSource) {
		this.defaultTargetMemcacheSource = defaultTargetMemcacheSource;
	}

	public void setTargetMemcacheSources(Map<String, MemcacheClientProxy> targetMemcacheSources) {
		this.targetMemcacheSources = targetMemcacheSources;
	}

	public <T> T execute(MemcacheCallback<T> action) {
		XMemcachedClientProxy memcachedClient = getMemcachedClient();

		if (memcachedClient == null) {
			memcachedClient = defaultTargetMemcacheSource.getXMemcachedClientProxy();
		}

		if (memcachedClient == null) {
			throw new MyException("Can not get a redisTemplate!");
		}

		T result = null;

		try {
			result = action.doInMemcache(memcachedClient);
		} catch (Exception e) {
			throw MyException.getMyException(e);
		}

		return result;
	}

	public void afterPropertiesSet() {

	}

	private XMemcachedClientProxy getMemcachedClient() {
		String memcacheSourceType = MemcacheSourceSwitcher.getMemcacheSourceType();

		if (StringUtils.isNotBlank(memcacheSourceType)) {
			MemcacheClientProxy memcacheClientProxy = targetMemcacheSources.get(memcacheSourceType);

			if (memcacheClientProxy != null) {
				XMemcachedClientProxy memcachedClient = memcacheClientProxy.getXMemcachedClientProxy();
				return memcachedClient;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
}
